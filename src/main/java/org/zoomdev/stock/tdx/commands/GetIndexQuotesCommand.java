package org.zoomdev.stock.tdx.commands;

import org.zoomdev.stock.IndexQuote;
import org.zoomdev.stock.Quote;
import org.zoomdev.stock.tdx.TdxInputStream;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GetIndexQuotesCommand extends GetQuotesCommand {

    public GetIndexQuotesCommand(int category, int market, String code, int start, int count) {
        super(category, market, code, start, count);
    }

    @Override
    protected Quote parseItem(TdxInputStream stream) throws IOException {
        IndexQuote quote= (IndexQuote) super.parseItem(stream);
        int upCount = stream.readShort();
        int downCount = stream.readShort();
        quote.setUpCount(upCount);
        quote.setDownCount(downCount);
        return quote;
    }


    @Override
    protected Quote createQuote() {
        return new IndexQuote();
    }


}
