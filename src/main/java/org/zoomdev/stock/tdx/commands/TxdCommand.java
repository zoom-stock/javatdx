package org.zoomdev.stock.tdx.commands;

import org.zoomdev.stock.tdx.TdxInputStream;
import org.zoomdev.stock.tdx.TxdOutputStream;

import java.io.IOException;

public interface TxdCommand {
    void process(TxdOutputStream outputStream, TdxInputStream inputStream) throws IOException;
}
