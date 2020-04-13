package org.zoomdev.stock.tdx.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.zoomdev.stock.Quote;
import org.zoomdev.stock.tdx.*;
import org.zoomdev.stock.tdx.commands.*;
import org.zoomdev.stock.tdx.reader.TdxBlockReader;
import org.zoomdev.stock.tdx.utils.DataOutputStream;
import org.zoomdev.stock.tdx.utils.HexUtils;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


/**
 * 非线程安全，如要实现多线程，则需要在每一个线程中实例化一个TxdClient
 */
public class TdxClientImpl implements TdxClient {


    public static final int DEFAULT_SO_TIMEOUT = 10000;
    static final int CHUNK_SIZE = 0x7530;
    private static final int DEFAULT_CONNECT_TIMEOUT = 500;
    private static final Log log = LogFactory.getLog(TdxClient.class);
    Socket socket;
    TdxInputStream inputStream;
    private DataOutputStream outputStream;
    private InetSocketAddress address;
    private int soTimeout = DEFAULT_SO_TIMEOUT;
    private int connectTimeout = DEFAULT_CONNECT_TIMEOUT;
    private String tdxRootDir;

    public String getTdxRootDir() {
        return tdxRootDir;
    }

    public void setTdxRootDir(String tdxRootDir) {
        this.tdxRootDir = tdxRootDir;
    }

    public int getSoTimeout() {
        return soTimeout;
    }

    public void setSoTimeout(int soTimeout) {
        this.soTimeout = soTimeout;
    }

    public int getConnectTimeout() {
        return connectTimeout;
    }

    public void setConnectTimeout(int connectTimeout) {
        this.connectTimeout = connectTimeout;
    }

    public InetSocketAddress getSocketAddress() {
        return this.address;
    }

    public void setSocketAddress(InetSocketAddress address) {
        this.address = address;
    }

    public void connect() throws IOException {
        socket = new Socket();
        socket.setSoTimeout(soTimeout);
        socket.connect(address, connectTimeout);
        InputStream inputStream = socket.getInputStream();
        OutputStream out = socket.getOutputStream();
        TdxInputStream txdInput = new TdxInputStream(new BufferedInputStream(inputStream));
        DataOutputStream txdOutput = new DataOutputStream(new BufferedOutputStream(out));
        this.inputStream = txdInput;
        this.outputStream = txdOutput;

        new LoginCommand().process(this.outputStream, this.inputStream);
    }

    public boolean isConnected() {
        if (socket == null) {
            return false;
        }
        return socket.isConnected();
    }

    public void close() {
        if (socket != null) {
            try {
                socket.close();
            } catch (Throwable e) {

            }
        }
    }

    public List<Quote> getIndexQuotes(
            Category category,
            Market market,
            String code,
            int start,
            int count
    ) throws IOException {

        GetIndexQuotesCommand cmd = new GetIndexQuotesCommand(category.ordinal(), market.ordinal(), code, start, count);
        return cmd.process(this.outputStream, this.inputStream);
    }

    public List<Quote> getQuotes(
            Category category,
            Market market,
            String code,
            int start,
            int count


    ) throws IOException {
        GetQuotesCommand cmd = new GetQuotesCommand(category.ordinal(), market.ordinal(), code, start, count);
        return cmd.process(this.outputStream, this.inputStream);
    }

    @Override
    public List<TimePrice> getTimePrice(Market market, String code) throws IOException {
        GetTimePriceCommand cmd = new GetTimePriceCommand(market, code);
        return cmd.process(this.outputStream, this.inputStream);
    }

    @Override
    public List<TimePrice> getHistoryTimePrice(Market market, String code, String date) throws IOException {
        GetHistoryTimePriceCommand cmd = new GetHistoryTimePriceCommand(market, code, date);
        return cmd.process(this.outputStream, this.inputStream);
    }

    public int getCount(Market market) throws IOException {
        outputStream.writeHexString("0c0c186c0001080008004e04");
        outputStream.writeShort(market.ordinal());
        outputStream.writeHexString("75c73301");

        outputStream.flush();

        inputStream.readPack(false);
        return inputStream.readShort();
    }

    public List<StockInfo> getStockList(Market market, int start) throws IOException {
        GetStockCommand cmd = new GetStockCommand(market, start);
        return cmd.process(this.outputStream, this.inputStream);
    }

    private void getStockList(Market market, List<StockInfo> result) throws IOException {
        int start = 0;
        while (true) {
            List<StockInfo> list = getStockList(market, start);
            result.addAll(list);
            if (list.size() < 1000) {
                break;
            }
            start += 1000;
        }
    }

    @Override
    public List<StockInfo> getStockList() throws IOException {
        List<StockInfo> result = new ArrayList<StockInfo>();
        getStockList(Market.sh, result);
        getStockList(Market.sz, result);
        return result;
    }

    private BlockInfoMeta getBlockInfoMeta(String type) throws IOException {
        outputStream.write(HexUtils.decodeHex("0C39186900012A002A00C502"));
        outputStream.writeUtf8String(type, 0x2a - 2);
        outputStream.flush();

        inputStream.readPack(false);

        //I1s32s1s
        int size = inputStream.readInt();
        inputStream.skip(1);
        String hash = inputStream.readUtf8String(32);
        inputStream.skip(1);

        return new BlockInfoMeta(size, hash);

    }

    public byte[] getRawBlockInfo(String type, int start, int size) throws IOException {


        outputStream.writeHexString("0c37186a00016e006e00b906");
        outputStream.writeInt(start);
        outputStream.writeInt(size);
        outputStream.writeUtf8String(type, 0x6e - 10);
        outputStream.flush();

        inputStream.readPack(false);
        byte[] bytes = inputStream.toByteArray();
        //取出后面4个字节的
        return bytes;

    }

    @Override
    public Collection<BlockStock> getBlockInfo(BlockType type) throws IOException {
        if (type == BlockType.Concept) {
            return getBlockInfoByList(type);
        } else if (type == BlockType.Style) {
            return getBlockInfoByList(type);
        } else if (type == BlockType.Index) {
            return getBlockInfoByList(type);
        }
        //此时可以加载对应的信息
        if (type == BlockType.TdxIndustry) {
            if (tdxRootDir == null) {
                log.warn("没有设置通达信根目录，不能获取全部信息");
            }
            byte[] stockBytes = downFile("tdxhy.cfg");
            Collection<BlockStock> stocks = TdxBlockReader.getBlockStockInfos(tdxRootDir, new ByteArrayInputStream(stockBytes), type);
            return stocks;
        }
        throw new RuntimeException("不支持的type类型" + type);
    }

    private Collection<BlockStock> getBlockInfoByList(BlockType type) throws IOException {
        List<StockInfo> list = getStockList();
        if (type == BlockType.Concept) {
            return TdxBlockReader.fillCode(getBlockInfo(BlockFileType.BLOCK_GN), list,type);
        } else if (type == BlockType.Style) {
            return TdxBlockReader.fillCode(getBlockInfo(BlockFileType.BLOCK_FG), list,type);
        } else if (type == BlockType.Index) {
            return TdxBlockReader.fillCode(getBlockInfo(BlockFileType.BLOCK_ZS), list,type);
        } else {
            throw new RuntimeException("不支持的type类型" + type);
        }
    }

    private Collection<BlockStock> getBlockInfoByFile(BlockType type) throws IOException {

        if (type == BlockType.Concept) {
            return TdxBlockReader.getBlockStockInfos(tdxRootDir, getBlockInfo(BlockFileType.BLOCK_GN), type);
        } else if (type == BlockType.Style) {
            return TdxBlockReader.getBlockStockInfos(tdxRootDir, getBlockInfo(BlockFileType.BLOCK_FG), type);
        } else if (type == BlockType.Index) {
            return TdxBlockReader.getBlockStockInfos(tdxRootDir, getBlockInfo(BlockFileType.BLOCK_ZS), type);
        } else {
            throw new RuntimeException("不支持的type类型" + type);
        }
    }


    public List<BlockStock> getBlockInfo(BlockFileType type) throws IOException {
        byte[] bytes = downFile(type.getName());
        return TdxBlockReader.fromDatFile(bytes, type);
    }


    public byte[] downFile(String name) throws IOException {
        BlockInfoMeta meta = getBlockInfoMeta(name);


        int chuncks = meta.size / CHUNK_SIZE;
        if (meta.size % CHUNK_SIZE != 0) {
            chuncks++;
        }

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        for (int seg = 0; seg < chuncks; ++seg) {
            int start = seg * CHUNK_SIZE;
            byte[] contents = getRawBlockInfo(name, start, meta.size);
            out.write(contents, 4, contents.length - 4);

        }
        return out.toByteArray();
    }


    class BlockInfoMeta {
        int size;
        String hash;

        public BlockInfoMeta(int size, String hash) {
            this.size = size;
            this.hash = hash;
        }
    }

}
