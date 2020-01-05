package org.zoomdev.stock.tdx.commands;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.zoomdev.stock.tdx.HexUtils;
import org.zoomdev.stock.tdx.TdxInputStream;
import org.zoomdev.stock.tdx.TdxOutputStream;
import org.zoomdev.stock.tdx.impl.RecordOutputStream;

import java.io.IOException;

public abstract class BaseCommand implements TdxCommand {


    protected static final Log log = LogFactory.getLog(BaseCommand.class);



    public void process(TdxOutputStream outputStream, TdxInputStream inputStream) throws IOException {
        if(log.isDebugEnabled()){
            RecordOutputStream recordOutputStream = new RecordOutputStream(outputStream);
            doOutput(recordOutputStream);
            log.debug("Send data "+ HexUtils.encodeHexStr(recordOutputStream.toByteArray()));
        }else{
            doOutput(outputStream);
        }
        outputStream.flush();
        inputStream.readPack(false);
        doInput(inputStream);
    }

     protected  abstract void doOutput(TdxOutputStream outputStream) throws IOException;

    protected abstract void doInput(TdxInputStream inputStream) throws IOException;

}
