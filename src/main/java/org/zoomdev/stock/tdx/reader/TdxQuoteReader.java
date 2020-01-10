package org.zoomdev.stock.tdx.reader;

import org.zoomdev.stock.Quote;
import org.zoomdev.stock.tdx.DataInputStream;
import org.zoomdev.stock.tdx.HexUtils;

import java.io.DataInput;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;


/**
 * 通达信本地文件解析工具类
 */
public class TdxQuoteReader {

    /**
     * 日线数据取出之后
     * @param bytes
     * @param start
     * @return
     */
    public static Quote parseForDay(byte[] bytes,int start){
        int time = HexUtils.readInt(bytes, start);

        double open = (double) HexUtils.readInt(bytes, start + 4) / 100;
        double high = (double) HexUtils.readInt(bytes, start + 8) / 100;
        double low = (double) HexUtils.readInt(bytes, start + 12) / 100;
        double close = (double) HexUtils.readInt(bytes, start + 16) / 100;
        double amt = (double) HexUtils.readInt(bytes, start + 20) / 100;
        int vol = HexUtils.readInt(bytes, start + 24);


        Quote quote = new Quote();
        quote.setDate(String.format("%04d%02d%02d", time / 10000, time % 10000 / 100, time % 10000 % 100));
        quote.setClose(close);
        quote.setOpen(open);
        quote.setLow(low);
        quote.setHigh(high);
        quote.setVol(vol);
        quote.setTor(0);
        quote.setAmt(amt);
        return quote;
    }

    public static Quote parseForDay(DataInputStream is) throws IOException {
        int time = is.readInt();

        double open = (double) is.readInt() / 100;
        double high = (double) is.readInt() / 100;
        double low = (double) is.readInt() / 100;
        double close = (double) is.readInt() / 100;

        double amt = (double) is.readFloat() ;
        int vol =  is.readInt();


        Quote quote = new Quote();
        quote.setDate(String.format("%04d%02d%02d", time / 10000, time % 10000 / 100, time % 10000 % 100));
        quote.setClose(close);
        quote.setOpen(open);
        quote.setLow(low);
        quote.setHigh(high);
        quote.setVol(vol);
        quote.setTor(0);
        quote.setAmt(amt);
        return quote;
    }


    public static Quote parseForMin(DataInputStream is) throws IOException {
        int time = is.readShort();
        int year = (time / 2048) + 2004;
        int month = ((time % 2048) / 100);
        int day = ((time % 2048) % 100);
        int minute =is.readShort();
        int hour = minute / 60;
        minute = minute % 60;
        String date= getDate(year, month, day, hour, minute);

        double open = is.readFloat();
        double high = is.readFloat();
        double low = is.readFloat();
        double close = is.readFloat();
        double amt = is.readFloat();
        int vol = is.readInt();


        Quote quote = new Quote();
        quote.setDate(date);
        quote.setClose(close);
        quote.setOpen(open);
        quote.setLow(low);
        quote.setHigh(high);
        quote.setVol(vol);
        quote.setTor(0);
        quote.setAmt(amt);
        return quote;
    }

    /**
     * 1分钟数据加载出来之后，解析，每32个字节一个k线
     * @param bytes
     * @param start
     * @return
     */
    public static Quote parseForMin(byte[] bytes,int start){
        String date = parseDate(bytes,start);
        double open = getFloat(bytes, start + 4);
        double high = getFloat(bytes, start + 8);
        double low = getFloat(bytes, start + 12);
        double close = getFloat(bytes, start + 16);
        double amt = getFloat(bytes, start + 20);
        int vol = HexUtils.readInt(bytes, start + 24);


        Quote quote = new Quote();
        quote.setDate(date);
        quote.setClose(close);
        quote.setOpen(open);
        quote.setLow(low);
        quote.setHigh(high);
        quote.setVol(vol);
        quote.setTor(0);
        quote.setAmt(amt);
        return quote;
    }
    public static double getFloat(byte[] b, int index) {
        int l;
        l = b[index + 0];
        l &= 0xff;
        l |= ((long) b[index + 1] << 8);
        l &= 0xffff;
        l |= ((long) b[index + 2] << 16);
        l &= 0xffffff;
        l |= ((long) b[index + 3] << 24);
        //保留两位小数
        float r = Float.intBitsToFloat(l);
        BigDecimal bg = new BigDecimal(r).setScale(2, RoundingMode.HALF_UP);
        return bg.doubleValue();

    }

    static String getDate(int year, int month, int date, int hour, int minute) {
        return String.format("%04d%02d%02d%02d%02d", year, month, date, hour, minute);
    }
    public static String parseDate(byte[] bytes, int start){
        int time = HexUtils.readShort(bytes, start);
        int year = (time / 2048) + 2004;
        int month = ((time % 2048) / 100);
        int day = ((time % 2048) % 100);
        int minute = HexUtils.readShort(bytes, start + 2);
        int hour = minute / 60;
        minute = minute % 60;
        return getDate(year, month, day, hour, minute);
    }




}
