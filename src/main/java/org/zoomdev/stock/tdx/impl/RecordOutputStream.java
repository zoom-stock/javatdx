package org.zoomdev.stock.tdx.impl;

import org.zoomdev.stock.tdx.TdxOutputStream;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class RecordOutputStream extends TdxOutputStream {


    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

    public RecordOutputStream(OutputStream stream){
        super(stream);

    }

    @Override
    public void write(int b) throws IOException {
        super.write(b);
        byteArrayOutputStream.write(b);
    }

    public byte[] toByteArray(){
        return byteArrayOutputStream.toByteArray();
    }
}
