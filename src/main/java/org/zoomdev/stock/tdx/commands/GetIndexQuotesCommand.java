package org.zoomdev.stock.tdx.commands;

import org.zoomdev.stock.IndexQuote;
import org.zoomdev.stock.Quote;
import org.zoomdev.stock.tdx.TdxInputStream;
import org.zoomdev.stock.tdx.TxdOutputStream;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GetIndexQuotesCommand extends GetQuotesCommand {

    public GetIndexQuotesCommand(int category, int market, String code, int start, int count) {
        super(category, market, code, start, count);
    }

    public List<IndexQuote> quotes;


    @Override
    protected void doInput(TdxInputStream stream) throws IOException {


        //不用解压缩
        int count = stream.readShort();
        quotes = new ArrayList<IndexQuote>(count);
        double pre_diff_base = 0;
        //       assert (count == this.count);
        for (int i = 0; i < count; ++i) {
            String date = getDate(stream);
            double price_open_diff = stream.getPrice();
            double price_close_diff = stream.getPrice();
            double price_high_diff = stream.getPrice();
            double price_low_diff = stream.getPrice();

            int vol_row = stream.readInt();
            double vol = getVolumn(vol_row);
            int dbvol_row = stream.readInt();
            double amt = getVolumn(dbvol_row);

            int upCount = stream.readShort();
            int downCount = stream.readShort();


            double open = getPrice(price_open_diff, pre_diff_base);
            price_open_diff = price_open_diff + pre_diff_base;

            double close = getPrice(price_open_diff, price_close_diff);
            double high = getPrice(price_open_diff, price_high_diff);
            double low = getPrice(price_open_diff, price_low_diff);

            pre_diff_base = price_open_diff + price_close_diff;

            IndexQuote quote = new IndexQuote();
            quote.setDate(date);
            quote.setClose(close);
            quote.setOpen(open);
            quote.setHigh(high);
            quote.setLow(low);
            quote.setVol(vol);
            quote.setAmt(amt);
            quote.setUpCount(upCount);
            quote.setDownCount(downCount);

            quotes.add(quote);
        }


    }

}
