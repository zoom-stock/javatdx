package org.zoomdev.stock.tdx.utils;

import java.io.IOException;

public class DataInputStream {

    protected byte[] buf;
    protected int pos;

    public DataInputStream() {

    }

    public void setBuf(byte[] buf) {
        this.buf = buf;
        this.pos = 0;
    }

    public int readByte() {
        return read();
    }


    public int read() {
        return buf[pos++] & 0xff;
    }


    public int readInt() {
        int a0 = read();
        int a1 = read();
        int a2 = read();
        int a3 = read();

        return (a0 & 0xff) | ((a1 << 8) & 0xff00) | ((a2 << 16) & 0xff0000) | ((a3 << 24) & 0xff000000);
    }

    public String readUtf8String(int len) throws IOException {
        return readString(len, "utf-8");
    }

    public String readGbkString(int len) throws IOException {
        return readString(len, "gbk");
    }


    public String readString(int len, String encoding) throws IOException {
        byte[] bytes = new byte[len];
        read(bytes);
        //去掉0
        int count = bytes.length;
        for (int i = bytes.length - 1; i >= 0; --i) {
            if (bytes[i] == 0) {
                --count;
                continue;
            }
        }
        return new String(bytes, 0, count, encoding);
    }

    public int read(byte b[]) throws IOException {
        return read(b, 0, b.length);
    }

    public int read(byte b[], int off, int len) throws IOException {
        if (b == null) {
            throw new NullPointerException();
        } else if (off < 0 || len < 0 || len > b.length - off) {
            throw new IndexOutOfBoundsException();
        } else if (len == 0) {
            return 0;
        }

        int c = read();
        if (c == -1) {
            return -1;
        }
        b[off] = (byte) c;

        int i = 1;
        for (; i < len; i++) {
            c = read();
            if (c == -1) {
                break;
            }
            b[off + i] = (byte) c;
        }
        return i;
    }

    //一定是unsigned short
    public int readShort() {

        int first = read();
        int second = read();

        return (first & 0xff) | ((second << 8) & 0xff00);
    }

    public float readFloat() {
        return Float.intBitsToFloat(readInt());
    }


    public void reset() {
        pos = 0;
    }

    //不判断，一切由外面来处理
    public void skip(int n) {
        pos += n;
    }

    public int getPos() {
        return pos;
    }

    public void setPos(int pos) {
        this.pos = pos;
    }
}
