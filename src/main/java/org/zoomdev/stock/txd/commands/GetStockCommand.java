package org.zoomdev.stock.txd.commands;

import org.zoomdev.stock.txd.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GetStockCommand extends BaseCommand {
    public GetStockCommand(Market market, int start) {
        this.market = market;
        this.start = start;
    }

    Market market;
    int start;


    @Override
    protected void doOutput(TxdOutputStream outputStream) throws IOException {
        outputStream.write(HexUtils.decodeHex("0c0118640101060006005004"));
        outputStream.writeInt(market.ordinal());
        outputStream.writeInt(start);
    }



    List<StockInfo> list;
    public  List<StockInfo> getStockList(){

        return list;
    }

    @Override
    protected void doInput(TxdInputStream inputStream) throws IOException {
        int count = inputStream.readShort();
        list = new ArrayList<StockInfo>(count);
        for(int i=0; i < count; ++i){
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
            list.add(stockInfo);
        }
    }
}
