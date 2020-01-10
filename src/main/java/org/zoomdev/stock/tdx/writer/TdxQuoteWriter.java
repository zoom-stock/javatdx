package org.zoomdev.stock.tdx.writer;

import org.zoomdev.stock.Quote;
import org.zoomdev.stock.tdx.TdxOutputStream;

import java.io.IOException;

public class TdxQuoteWriter {

    public static void writeForDay(Quote quote, TdxOutputStream os) throws IOException {

        String date = quote.getDate();
        int year = Integer.parseInt(date.substring(0,4));
        int month = Integer.parseInt(date.substring(4,6));
        int day = Integer.parseInt(date.substring(6,8));
        int time = year * 10000 + month * 100 + day;

        os.writeInt((int) Math.round(quote.getOpen()*100));
        os.writeInt((int) Math.round(quote.getHigh()*100));
        os.writeInt((int) Math.round(quote.getLow()*100));
        os.writeInt((int) Math.round(quote.getClose()*100));
        os.writeInt((int) Math.round(quote.getAmt()*100));

    }
}
