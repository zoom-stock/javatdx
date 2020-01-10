package org.zoomdev.stock.tdx.commands;

import org.zoomdev.stock.tdx.TdxInputStream;
import org.zoomdev.stock.tdx.TdxOutputStream;

import java.io.IOException;

public class GroupCommand implements TdxCommand<Object> {

    private final TdxCommand[] commands;

    public GroupCommand(TdxCommand...commands){
        this.commands = commands;
    }

    @Override
    public Object process(TdxOutputStream outputStream, TdxInputStream inputStream) throws IOException {
        for(TdxCommand c : commands){
            c.process(outputStream,inputStream);
        }
        return null;
    }
}
