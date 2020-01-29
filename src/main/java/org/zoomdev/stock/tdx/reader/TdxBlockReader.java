package org.zoomdev.stock.tdx.reader;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.zoomdev.stock.tdx.BlockFileType;
import org.zoomdev.stock.tdx.BlockStock;
import org.zoomdev.stock.tdx.BlockType;
import org.zoomdev.stock.tdx.StockInfo;
import org.zoomdev.stock.tdx.impl.TdxUtils;
import org.zoomdev.stock.tdx.utils.DataInputStream;

import java.io.*;
import java.util.*;

public class TdxBlockReader {


    public static final Log log = LogFactory.getLog(TdxBlockReader.class);

    public static Map<String, BlockStock> getMap(
            InputStream is, final BlockType blockType
    ) throws IOException {
        final Map<String, BlockStock> map = new HashMap<String, BlockStock>();
        read(is, new Visitor() {
            @Override
            public void visit(String line) {
                String[] parts = line.split("\\|");
                if (parts.length == 6) {
                    String name = parts[0];
                    String code = parts[1];
                    int type = Integer.parseInt(parts[2]);
                    String typeCode = parts[5];
                    if (type == blockType.getType()) {
                        //行业
                        //System.out.println(name);
                        BlockStock blockStock = new BlockStock();
                        blockStock.setType(blockType);
                        blockStock.setName(name);
                        blockStock.setCode(code);
                        if (blockType == BlockType.TdxIndustry) {
                            blockStock.setLevel((typeCode.length() - 1) / 2);
                        }
                        map.put(typeCode, blockStock);
                    }
                }
            }
        });
        return map;
    }
    //    File dir = new File(path,"T0002/hq_cache");
    //    //指数
    //    File type = new File(dir,"tdxzs.cfg");
    //    new File(dir, "tdxhy.cfg")

    public static Map<String, BlockStock> getMap(
            String path, BlockType blockType
    ) throws IOException {
        return getMap(new FileInputStream(new File(path, "T0002/hq_cache/tdxzs.cfg")), blockType);
    }

    public static Collection<BlockStock> getBlockStockInfos(
            String path, InputStream stockMapStream, BlockType blockType
    ) throws IOException {
        final Map<String, BlockStock> map = getMap(path, blockType);
        read(stockMapStream, new Visitor() {
            @Override
            public void visit(String line) {
                String[] parts = line.split("\\|");
                if (parts.length == 5) {
                   // int market = Integer.parseInt(parts[0]);
                    String code = parts[1];
                    String type = parts[2];
                    addTo(map, type, code);
                }
            }
        });

        return map.values();
    }

    public static Collection<BlockStock> getBlockStockInfos(
            String path, Collection<BlockStock> stocks, BlockType blockType
    ) throws IOException {
        final Map<String, BlockStock> map = getMap(path, blockType);
        fillCode(stocks, map);
        return stocks;
    }

    public static Collection<BlockStock> fillCode(Collection<BlockStock> stocks, List<StockInfo> allStocks) {
        Map<String, String> map = new HashMap<String, String>();
        for (StockInfo info : allStocks) {
            map.put(info.getName(), info.getCode());
        }
        for (BlockStock stock : stocks) {
            String code = map.get(stock.getName());
            if (code == null) {
                log.warn("没有找到[" + stock.getName() + "]对应的code");
                continue;
            }
            stock.setCode(code);
        }
        return stocks;
    }

    public static void fillCode(Collection<BlockStock> stocks, Map<String, BlockStock> map) {
        for (BlockStock stock : stocks) {
            BlockStock stockWithCode = map.get(stock.getName());
            if (stockWithCode == null) {
                log.warn("没有找到[" + stock.getName() + "]对应的板块配置");
                continue;
            }
            stock.setCode(stockWithCode.getCode());
        }
    }

    /**
     * 提供通达信目录
     * 先提供通达信行业分类再说
     *
     * @param path
     * @return
     */
    public static Collection<BlockStock> getTdxHy(
            String path
    ) throws IOException {
        File hy = new File(path, "T0002/hq_cache/tdxhy.cfg");  //个股和行业映射表
        return getBlockStockInfos(path, new FileInputStream(hy), BlockType.TdxIndustry);
    }


    private static void addTo(Map<String, BlockStock> map, String type, String code) {
        BlockStock blockStock = map.get(type);
        if (blockStock == null) {
            return;
        }

        if (type.length() > 3) {
            addTo(map, type.substring(0, type.length() - 2), code);
        }

        List<String> codes = blockStock.getCodes();
        if (codes == null) {
            codes = new ArrayList<String>();
            blockStock.setCodes(codes);
        }
        codes.add(code);
    }

    private static void read(InputStream is, Visitor visitor) throws IOException {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(
                    new InputStreamReader(is, "gbk")
            );

            String line = null;
            while ((line = reader.readLine()) != null) {
                visitor.visit(line);
            }
        } finally {
            TdxUtils.close(reader);
        }

    }

    private static void read(File file, Visitor visitor) throws IOException {
        read(new FileInputStream(file), visitor);
    }

    /**
     * dat文件
     *
     * @param content
     * @return
     * @throws IOException
     */
    public static List<BlockStock> fromDatFile(byte[] content, BlockFileType type) throws IOException {
        DataInputStream inputStream = new DataInputStream();
        inputStream.setBuf(content);
        inputStream.skip(384);
        int blockCount = inputStream.readShort();
        List<BlockStock> result = new ArrayList<BlockStock>(blockCount);
        for (int i = 0; i < blockCount; ++i) {
            String blockName = inputStream.readGbkString(9);
            int stockCount = inputStream.readShort();
            assert (stockCount < 10000);
            int blockType = inputStream.readShort();
            int block_stock_begin = inputStream.getPos();
            BlockStock blockStock = new BlockStock();
            result.add(blockStock);
            blockStock.setName(blockName);
            blockStock.setLevel(blockType);
            if (type == BlockFileType.BLOCK_FG) {
                blockStock.setType(BlockType.Style);
            } else if (type == BlockFileType.BLOCK_GN) {
                blockStock.setType(BlockType.Concept);
            } else if (type == BlockFileType.BLOCK_ZS) {
                blockStock.setType(BlockType.Index);
            }
            List<String> codes = new ArrayList<String>(stockCount);
            blockStock.setCodes(codes);
            for (int code_index = 0; code_index < stockCount; ++code_index) {
                String code = inputStream.readUtf8String(7);
                codes.add(code);
            }
            inputStream.setPos(block_stock_begin + 2800);

        }

        return result;

    }


    interface Visitor {
        void visit(String line);
    }
}
