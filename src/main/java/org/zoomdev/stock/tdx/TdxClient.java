package org.zoomdev.stock.tdx;

import org.zoomdev.stock.Quote;
import org.zoomdev.stock.tdx.commands.GetQuotesCommand;
import org.zoomdev.stock.tdx.commands.GetStockCommand;
import org.zoomdev.stock.tdx.commands.LoginCommand;
import org.zoomdev.stock.tdx.reader.BlockReader;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.List;


/**
 * 非线程安全，如要实现多线程，则需要在每一个线程中实例化一个TxdClient
 *
 */
public class TdxClient {


    public static final int DEFAULT_SO_TIMEOUT = 10000;
    private static final int DEFAULT_CONNECT_TIMEOUT = 500;

    Socket socket ;
    TdxInputStream inputStream;
    private TxdOutputStream outputStream;
    private InetSocketAddress address;

    public int getSoTimeout() {
        return soTimeout;
    }

    public void setSoTimeout(int soTimeout) {
        this.soTimeout = soTimeout;
    }

    private int soTimeout = DEFAULT_SO_TIMEOUT;

    public int getConnectTimeout() {
        return connectTimeout;
    }

    public void setConnectTimeout(int connectTimeout) {
        this.connectTimeout = connectTimeout;
    }

    private int connectTimeout = DEFAULT_CONNECT_TIMEOUT;

    public void setSocketAddress(InetSocketAddress address){
        this.address = address;
    }

    public void connect() throws IOException {
        socket = new Socket();
        socket.setSoTimeout(soTimeout);
        socket.connect(address,connectTimeout);
        InputStream inputStream = socket.getInputStream();
        OutputStream out = socket.getOutputStream();
        TdxInputStream txdInput = new TdxInputStream(new BufferedInputStream(inputStream));
        TxdOutputStream txdOutput = new TxdOutputStream(new BufferedOutputStream(out));
        this.inputStream = txdInput;
        this.outputStream = txdOutput;

        new LoginCommand().process(this.outputStream,this.inputStream);
    }





    public boolean isConnected(){
        if(socket==null){
            return false;
        }
        return socket.isConnected();
    }

    public void close(){
        if(socket!=null){
            try{
                socket.close();
            }catch (Throwable e){

            }
        }
    }

    public List<Quote> getQuotes(
            Category category,
            Market market,
            String code,
            int start,
            int count


    ) throws IOException {
        GetQuotesCommand cmd = new GetQuotesCommand(category.ordinal(),market.ordinal(),code,start,count);
        cmd.process(this.outputStream,this.inputStream);
        return cmd.getQuotes();
    }



    public List<StockInfo> getStockList(Market market,int start) throws IOException {
        GetStockCommand cmd = new GetStockCommand(market,start);
        cmd.process(this.outputStream,this.inputStream);
        return cmd.getStockList();
    }

    


    class BlockInfoMeta{
        int size;
        String hash;

        public BlockInfoMeta(int size, String hash) {
            this.size = size;
            this.hash = hash;
        }
    }

    private BlockInfoMeta getBlockInfoMeta(String type) throws IOException {
        outputStream.write(HexUtils.decodeHex("0C39186900012A002A00C502"));
        outputStream.writeUtf8String(type,0x2a - 2);
        outputStream.flush();

        inputStream.readPack(false);

        //I1s32s1s
        int size = inputStream.readInt();
        inputStream.skip(1);
        String hash = inputStream.readUtf8String(32);
        inputStream.skip(1);

        return new BlockInfoMeta(size,hash);

    }

    public byte[] getRawBlockInfo(String type,int start,int size) throws IOException {


        outputStream.writeHexString("0c37186a00016e006e00b906");
        outputStream.writeInt(start);
        outputStream.writeInt(size);
        outputStream.writeUtf8String(type,0x6e-10);
        outputStream.flush();

        inputStream.readPack(false);
        byte[] bytes = inputStream.toByteArray();
        //取出后面4个字节的
        return bytes;

    }

    static final int CHUNK_SIZE = 0x7530;

    public List<BlockStock> getBlockInfo(BlockType type) throws IOException {
        BlockInfoMeta meta = getBlockInfoMeta(type.getName());


        int chuncks = meta.size / CHUNK_SIZE;
        if(meta.size % CHUNK_SIZE !=0){
            chuncks ++;
        }

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        for(int seg=0; seg < chuncks; ++seg){
            int start = seg * CHUNK_SIZE;
            byte[] contents = getRawBlockInfo(type.getName(),start,meta.size);
            out.write(contents,4,contents.length-4);

        }

        return BlockReader.read(out.toByteArray());
    }

}
