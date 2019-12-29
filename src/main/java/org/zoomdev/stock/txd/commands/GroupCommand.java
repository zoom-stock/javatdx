package org.zoomdev.stock.txd.commands;

import org.zoomdev.stock.txd.TxdInputStream;
import org.zoomdev.stock.txd.TxdOutputStream;

import java.io.IOException;

public class GroupCommand implements TxdCommand {

    private final TxdCommand[] commands;

    public GroupCommand(TxdCommand...commands){
        this.commands = commands;
    }

    @Override
    public void process(TxdOutputStream outputStream, TxdInputStream inputStream) throws IOException {
        for(TxdCommand c : commands){
            c.process(outputStream,inputStream);
        }
    }
}
