package org.zoomdev.stock.tdx.commands;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.zoomdev.stock.tdx.TdxCommand;
import org.zoomdev.stock.tdx.impl.TdxInputStream;
import org.zoomdev.stock.tdx.utils.DataOutputStream;
import org.zoomdev.stock.tdx.utils.HexUtils;

import java.io.IOException;

public abstract class BaseCommand<R> implements TdxCommand<R> {


    protected static final Log log = LogFactory.getLog(BaseCommand.class);


    public R process(DataOutputStream outputStream, TdxInputStream inputStream) throws IOException {
        if (log.isDebugEnabled()) {
            RecordOutputStream recordOutputStream = new RecordOutputStream(outputStream);
            doOutput(recordOutputStream);
            log.debug("Send data " + HexUtils.encodeHexStr(recordOutputStream.toByteArray()));
        } else {
            doOutput(outputStream);
        }
        outputStream.flush();
        inputStream.readPack(false);
        return doInput(inputStream);
    }

    protected abstract void doOutput(DataOutputStream outputStream) throws IOException;

    protected abstract R doInput(TdxInputStream inputStream) throws IOException;

}
