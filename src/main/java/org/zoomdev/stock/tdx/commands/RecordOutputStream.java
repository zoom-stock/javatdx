package org.zoomdev.stock.tdx.commands;

import org.zoomdev.stock.tdx.utils.DataOutputStream;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

class RecordOutputStream extends DataOutputStream {


    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

    public RecordOutputStream(OutputStream stream) {
        super(stream);

    }

    @Override
    public void write(int b) throws IOException {
        super.write(b);
        byteArrayOutputStream.write(b);
    }

    public byte[] toByteArray() {
        return byteArrayOutputStream.toByteArray();
    }
}
