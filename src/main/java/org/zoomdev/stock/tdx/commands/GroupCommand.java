package org.zoomdev.stock.tdx.commands;

import org.zoomdev.stock.tdx.TdxInputStream;
import org.zoomdev.stock.tdx.TxdOutputStream;

import java.io.IOException;

public class GroupCommand implements TxdCommand {

    private final TxdCommand[] commands;

    public GroupCommand(TxdCommand...commands){
        this.commands = commands;
    }

    @Override
    public void process(TxdOutputStream outputStream, TdxInputStream inputStream) throws IOException {
        for(TxdCommand c : commands){
            c.process(outputStream,inputStream);
        }
    }
}
