package org.zoomdev.stock.tdx.commands;

import org.zoomdev.stock.Quote;
import org.zoomdev.stock.tdx.TdxInputStream;
import org.zoomdev.stock.tdx.TxdOutputStream;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GetQuotesCommand extends BaseCommand {

    private final String code;
    private final int market;
    private final int start;
    private final int count;
    private final int category;

    public List<Quote> getQuotes() {
        return quotes;
    }


    private List<Quote> quotes;

    public GetQuotesCommand(
            int category,
            int market,
            String code,

            int start,
            int count
    ) {

        this.code = code;
        this.market = market;
        this.start = start;
        this.count = count;
        this.category = category;
    }

    @Override
    protected void doOutput(TxdOutputStream outputStream) throws IOException {

        outputStream.writeShort(0x10c);
        outputStream.writeInt(0x01016408);
        outputStream.writeShort(0x1c);
        outputStream.writeShort(0x1c);
        outputStream.writeShort(0x052d);

        outputStream.writeShort(market);
        outputStream.writeAscii(code);
        outputStream.writeShort(category);
        outputStream.writeShort(1);
        outputStream.writeShort(start);
        outputStream.writeShort(count);

        outputStream.writeInt(0);
        outputStream.writeInt(0);
        outputStream.writeShort(0);

    }

    static String getDate(int year, int month, int date, int hour, int minute) {
        return String.format("%d-%02d-%02d %02d:%02d", year, month, date, hour, minute);
    }

    static String getDate(int year, int month, int date) {
        return String.format("%d-%02d-%02d", year, month, date);
    }

    String getDate(TdxInputStream inputStream) throws IOException {
        int year;
        int month;
        int hour;
        int minute;
        int day;
        if (category < 4 || category == 7 || category == 8) {
            int zipday = inputStream.readShort();
            int tminutes = inputStream.readShort();
            year = (zipday >> 11) + 2004;
            month = (int) ((zipday % 2048) / 100);
            day = (zipday % 2048) % 100;

            hour = (tminutes / 60);
            minute = tminutes % 60;
            return getDate(year, month, day, hour, minute);
        } else {
            int zipday = inputStream.readInt();
            year = (zipday / 10000);
            month = ((zipday % 10000) / 100);
            day = zipday % 100;
            return getDate(year, month, day);
        }


    }

    @Override
    protected void doInput(TdxInputStream stream) throws IOException {


        //不用解压缩
        int count = stream.readShort();
        quotes = new ArrayList<Quote>(count);
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

            double open = getPrice(price_open_diff, pre_diff_base);
            price_open_diff = price_open_diff + pre_diff_base;

            double close = getPrice(price_open_diff, price_close_diff);
            double high = getPrice(price_open_diff, price_high_diff);
            double low = getPrice(price_open_diff, price_low_diff);

            pre_diff_base = price_open_diff + price_close_diff;

            Quote quote = new Quote();
            quote.setDate(date);
            quote.setClose(close);
            quote.setOpen(open);
            quote.setHigh(high);
            quote.setLow(low);
            quote.setVol(vol);
            quote.setAmt(amt);

            quotes.add(quote);
        }


    }


    private double getPrice(double base, double diff) {
        return (base + diff) / 1000;
    }

    public static double getVolumn(int ivol) {
        int logpoint = ivol >> (8 * 3);
        int hheax = ivol >> (8 * 3);
        int hleax = (ivol >> (8 * 2)) & 0xff;
        int lheax = (ivol >> 8) & 0xff;
        int lleax = ivol & 0xff;
        double dbl_1 = 1.0;
        double dbl_2 = 2.0;
        double dbl_128 = 128.0;

        int dwEcx = logpoint * 2 - 0x7f;
        int dwEdx = logpoint * 2 - 0x86;
        int dwEsi = logpoint * 2 - 0x8e;
        int dwEax = logpoint * 2 - 0x96;

        int tmpEax;
        if (dwEcx < 0) {
            tmpEax = -dwEcx;
        } else {
            tmpEax = dwEcx;
        }

        double dbl_xmm6 = 0.0;
        dbl_xmm6 = Math.pow(2.0, tmpEax);
        if (dwEcx < 0) {
            dbl_xmm6 = 1.0 / dbl_xmm6;
        }

        double dbl_xmm4 = 0;
        double tmpdbl_xmm3;
        double tmpdbl_xmm1;
        if (hleax > 0x80) {
            tmpdbl_xmm3 = 0.0;
            tmpdbl_xmm1 = 0.0;
            int dwtmpeax = dwEdx + 1;
            tmpdbl_xmm3 = Math.pow(2.0, dwtmpeax);
            double dbl_xmm0 = Math.pow(2.0, dwEdx) * 128.0;
            dbl_xmm0 += (hleax & 0x7f) * tmpdbl_xmm3;
            dbl_xmm4 = dbl_xmm0;
        } else {
            double dbl_xmm0 = 0.0;
            if (dwEdx >= 0) {
                dbl_xmm0 = Math.pow(2.0, dwEdx) * hleax;
            } else {
                dbl_xmm0 = (1 / Math.pow(2.0, dwEdx)) * hleax;
                dbl_xmm4 = dbl_xmm0;
            }

        }


        double dbl_xmm3 = Math.pow(2.0, dwEsi) * lheax;
        double dbl_xmm1 = Math.pow(2.0, dwEax) * lleax;
        if ((hleax & 0x80) != 0) {
            dbl_xmm3 *= 2.0;
            dbl_xmm1 *= 2.0;
        }


        double dbl_ret = dbl_xmm6 + dbl_xmm4 + dbl_xmm3 + dbl_xmm1;
        return dbl_ret;
    }


}
