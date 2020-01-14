package org.zoomdev.stock.tdx;

import org.zoomdev.stock.Quote;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.List;

public interface TdxClient {
    void setSoTimeout(int soTimeout);

    void setConnectTimeout(int connectTimeout);

    void setSocketAddress(InetSocketAddress address);

    void connect() throws IOException;

    boolean isConnected();

    void close();

    List<Quote> getIndexQuotes(
            Category category,
            Market market,
            String code,
            int start,
            int count
    ) throws IOException;

    List<Quote> getQuotes(
            Category category,
            Market market,
            String code,
            int start,
            int count


    ) throws IOException;

    int getCount(Market market) throws IOException;

    List<StockInfo> getStockList(Market market, int start) throws IOException;

    List<BlockStock> getBlockInfo(BlockType type) throws IOException;
}
