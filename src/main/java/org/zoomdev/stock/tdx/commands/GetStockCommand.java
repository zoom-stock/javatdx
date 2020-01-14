package org.zoomdev.stock.tdx.commands;

import org.zoomdev.stock.tdx.Market;
import org.zoomdev.stock.tdx.StockInfo;
import org.zoomdev.stock.tdx.impl.TdxInputStream;
import org.zoomdev.stock.tdx.utils.DataOutputStream;
import org.zoomdev.stock.tdx.utils.HexUtils;

import java.io.IOException;

public class GetStockCommand extends ListCommand<StockInfo> {
    Market market;
    int start;

    public GetStockCommand(Market market, int start) {
        this.market = market;
        this.start = start;
    }

    @Override
    protected void doOutput(DataOutputStream outputStream) throws IOException {
        outputStream.write(HexUtils.decodeHex("0c0118640101060006005004"));
        outputStream.writeShort(market.ordinal());
        outputStream.writeShort(start);
    }


    @Override
    protected StockInfo parseItem(TdxInputStream inputStream) throws IOException {
        //6sH8s4sBI4s
        String code = inputStream.readUtf8String(6);
        int volunit = inputStream.readShort();
        String name = inputStream.readGbkString(8);
        inputStream.skip(4);
        int decimal_point = inputStream.readByte();
        int pre_close_raw = inputStream.readInt();
        inputStream.skip(4);
        double pre_close = GetQuotesCommand.getVolumn(pre_close_raw);
        StockInfo stockInfo = new StockInfo();
        stockInfo.setCode(code);

        stockInfo.setDecimalPoint(decimal_point);
        stockInfo.setPrice(pre_close);
        stockInfo.setName(name);
        return stockInfo;
    }
}
