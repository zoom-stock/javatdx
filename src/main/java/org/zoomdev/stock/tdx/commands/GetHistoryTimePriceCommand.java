package org.zoomdev.stock.tdx.commands;

import org.zoomdev.stock.tdx.Market;
import org.zoomdev.stock.tdx.impl.TdxInputStream;
import org.zoomdev.stock.tdx.utils.DataOutputStream;

import java.io.IOException;

public class GetHistoryTimePriceCommand extends GetTimePriceCommand {

    /// yyyMMdd
    private String date;

    public GetHistoryTimePriceCommand(Market market, String code, String date) {
        super(market, code);
        this.date = date;
    }

    @Override
    protected void doOutput(DataOutputStream outputStream) throws IOException {
        outputStream.writeHexString("0c01300001010d000d00b40f");
        outputStream.writeInt(Integer.parseInt(date));
        outputStream.write(market.ordinal());
        outputStream.writeAscii(code);
    }

    @Override
    protected void skip(TdxInputStream inputStream) {
        inputStream.skip(4);
    }
}
