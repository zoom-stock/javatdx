package org.zoomdev.stock.txd;

import java.io.*;

public class TypedInputStream extends ByteArrayInputStream {




    public TypedInputStream(byte[] content) {
        super(content);
    }

    public int readByte() throws IOException {
        return read();
    }

    public int readInt() throws IOException {
        int a0 = read();
        int a1 = read();
        int a2 = read();
        int a3 = read();

        return (a0&0xff) | ((a1 <<8)&0xff00) | ((a2<<16)&0xff0000) | ( (a3<<24)&0xff000000 );
    }
    public int readShort() throws IOException {

        int first = read();
        int second = read();

        return (first&0xff) | ((second <<8)&0xff00);
    }

    public void setBuf(byte[] buf){
        this.buf = buf;
        this.pos = 0;
        this.count = buf.length;
    }

    public String readUtf8String(int len) throws IOException {
        return readString(len,"utf-8");
    }

    public String readGbkString(int len) throws IOException{
        return readString(len,"gbk");
    }

    public String readString(int len,String encoding) throws IOException {
        byte[] bytes = new byte[len];
        read(bytes);
        //去掉0
        int count = bytes.length;
        for(int i=bytes.length-1; i>=0; --i){
            if(bytes[i]==0){
                --count;
                continue;
            }
        }

        return new String(bytes,0,count,encoding);
    }


    public void skip(int n) throws IOException {
        pos += n;
    }

    public int getPos() {
        return pos;
    }

    public void setPos(int pos) {
        this.pos = pos;
    }
}
