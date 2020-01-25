package org.zoomdev.stock.tdx;

import org.zoomdev.stock.Quote;

import java.util.List;
import java.util.concurrent.Future;

public interface TdxClientService {
    void start();

    void stop();

    Future<Integer> getCount(Market market);

    Future<List<StockInfo>> getStockList(Market market, final int start);

    Future<List<BlockStock>> getBlockInfo(BlockType type);

    Future<List<Quote>> getIndexQuotes(
            Category category,
            Market market,
            String code,
            int start,
            int count
    );


    //全部
    Future<List<StockInfo>> getStockList();

    Future<List<Quote>> getQuotes(
            Category category,
            Market market,
            String code,
            int start,
            int count


    );


    // 设置断线之后重连的延时（毫秒)
    void setReconnectTimeout(int timeout);
}
