package org.zoomdev.stock.tdx.writer;

import org.zoomdev.stock.IndexQuote;
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

        os.writeInt(time);
        os.writeInt((int) Math.round(quote.getOpen()*100));
        os.writeInt((int) Math.round(quote.getHigh()*100));
        os.writeInt((int) Math.round(quote.getLow()*100));
        os.writeInt((int) Math.round(quote.getClose()*100));
        os.writeFloat((float) quote.getAmt());
        os.writeInt(quote.getVol());
        os.writeInt(0);


    }

    public static void writeForMin(Quote quote, TdxOutputStream os) throws IOException {

        String date = quote.getDate();
        int year = Integer.parseInt(date.substring(0,4));
        int month = Integer.parseInt(date.substring(4,6));
        int day = Integer.parseInt(date.substring(6,8));

//        int year = (time / 2048) + 2004;
//        int month = ((time % 2048) / 100);
//        int day = ((time % 2048) % 100);

        os.writeShort(    (year-2004) * 2048 +   month*100 + day         );

        int hour = Integer.parseInt(date.substring(8,10));
        int minute = Integer.parseInt(date.substring(10,12));

        os.writeShort( hour * 60 + minute );

        os.writeFloat((float) quote.getOpen());
        os.writeFloat((float) quote.getHigh());
        os.writeFloat((float) quote.getLow());
        os.writeFloat((float) quote.getClose());
        os.writeFloat((float) quote.getAmt());
        os.writeInt(quote.getVol());
        os.writeInt(0);


    }
}
