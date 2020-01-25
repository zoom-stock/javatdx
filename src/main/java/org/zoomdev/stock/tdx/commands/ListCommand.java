package org.zoomdev.stock.tdx.commands;


import org.zoomdev.stock.tdx.impl.TdxInputStream;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public abstract class ListCommand<T> extends BaseCommand<List<T>> {


    @Override
    protected List<T> doInput(TdxInputStream inputStream) throws IOException {
        int count = inputStream.readShort();

        List<T> list = new ArrayList<T>(count);
        for (int i = 0; i < count; ++i) {
            list.add(parseItem(inputStream));
        }
        return list;
    }

    protected void skip(TdxInputStream inputStream) {

    }

    protected abstract T parseItem(TdxInputStream inputStream) throws IOException;

}
