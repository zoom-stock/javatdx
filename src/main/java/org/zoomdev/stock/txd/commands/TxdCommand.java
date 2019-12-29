package org.zoomdev.stock.txd.commands;

import org.zoomdev.stock.txd.TxdInputStream;
import org.zoomdev.stock.txd.TxdOutputStream;

import java.io.IOException;

public interface TxdCommand {
    void process(TxdOutputStream outputStream, TxdInputStream inputStream) throws IOException;
}
