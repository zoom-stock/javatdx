package org.zoomdev.stock.tdx.commands;


import org.zoomdev.stock.tdx.Market;
import org.zoomdev.stock.tdx.TimePrice;
import org.zoomdev.stock.tdx.impl.TdxInputStream;
import org.zoomdev.stock.tdx.utils.DataOutputStream;

import java.io.IOException;

public class GetTimePriceCommand extends ListCommand<TimePrice> {

    protected String code;
    protected Market market;

    private double lastPrice = 0;

    public GetTimePriceCommand(Market market, String code) {
        this.market = market;
        this.code = code;
    }

    @Override
    protected TimePrice parseItem(TdxInputStream inputStream) throws IOException {
        double priceRaw = inputStream.readPrice();
        inputStream.readPrice();
        double vol = inputStream.readPrice();
        lastPrice += priceRaw;

        TimePrice price = new TimePrice();
        price.setVol((int) vol);
        price.setClose(lastPrice / 100);
        return price;
    }

    @Override
    protected void doOutput(DataOutputStream outputStream) throws IOException {
        outputStream.writeHexString("0c1b080001010e000e001d05");
        outputStream.writeShort(market.ordinal());
        outputStream.writeAscii(code);
        outputStream.writeInt(0);
    }

    @Override
    protected void skip(TdxInputStream inputStream) {
        inputStream.skip(2);
    }
}
