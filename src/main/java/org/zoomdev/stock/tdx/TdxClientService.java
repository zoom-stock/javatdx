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
            final Category category,
            final Market market,
            final String code,
            final int start,
            final int count
    );

    Future<List<Quote>> getQuotes(
            final Category category,
            final Market market,
            final String code,
            final int start,
            final int count


    );
}
