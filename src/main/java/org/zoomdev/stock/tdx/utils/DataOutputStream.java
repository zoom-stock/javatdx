package org.zoomdev.stock.tdx.utils;

import java.io.IOException;
import java.io.OutputStream;

public class DataOutputStream extends OutputStream {

    private final OutputStream outputStream;

    public DataOutputStream(OutputStream outputStream) {
        this.outputStream = outputStream;
    }

    public void flush() throws IOException {
        this.outputStream.flush();
    }

    public void writeShort(int i) throws IOException {
        write(i & 0xff);
        write((i >> 8) & 0xff);
    }

    public void writeFloat(float f) throws IOException {
        //writeFloat
        writeInt(Float.floatToIntBits(f));
    }


    public void write(int i) throws IOException {
        outputStream.write(i);
    }

    public void writeInt(int i) throws IOException {
        write(i & 0xff);
        write((i >> 8) & 0xff);
        write((i >> 16) & 0xff);
        write((i >> 24) & 0xff);
    }


    public void writeAscii(String code) throws IOException {
        for (byte b : code.getBytes()) {
            write(b);
        }
    }

    public void writeUtf8String(String text, int len) throws IOException {
        byte[] bytes = text.getBytes("utf-8");
        if (bytes.length > len) {
            throw new IllegalArgumentException("text is too long");
        }

        write(bytes);
        for (int i = bytes.length; i < len; ++i) {
            write(0);
        }
    }

    public void writeHexString(String s) throws IOException {
        write(HexUtils.decodeHex(s));
    }
}
