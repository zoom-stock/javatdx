package org.zoomdev.stock.txd.commands;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.zoomdev.stock.txd.HexUtils;
import org.zoomdev.stock.txd.TxdInputStream;
import org.zoomdev.stock.txd.TxdOutputStream;
import org.zoomdev.stock.txd.impl.RecordOutputStream;

import java.io.IOException;
import java.util.List;

public abstract class BaseCommand implements TxdCommand {


    protected static final Log log = LogFactory.getLog(BaseCommand.class);



    public void process(TxdOutputStream outputStream, TxdInputStream inputStream) throws IOException {
        if(log.isDebugEnabled()){
            RecordOutputStream recordOutputStream = new RecordOutputStream(outputStream);
            doOutput(recordOutputStream);
            log.debug("Send data "+ HexUtils.encodeHexStr(recordOutputStream.toByteArray()));
        }else{
            doOutput(outputStream);
        }

        inputStream.readPack(false);
        doInput(inputStream);
    }

     protected  abstract void doOutput(TxdOutputStream outputStream) throws IOException;

    protected abstract void doInput(TxdInputStream inputStream) throws IOException;

}
