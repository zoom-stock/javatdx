package org.zoomdev.stock.tdx;

import java.io.DataInput;
import java.io.IOException;

public class DataInputStream {

    protected byte[] buf;
    protected int pos;

    public DataInputStream(){

    }

    public void setBuf(byte[] buf){
        this.buf = buf;
        this.pos = 0;
    }
    public  int read() {
        return buf[pos++] & 0xff;
    }


    public int readInt() {
        int a0 = read();
        int a1 = read();
        int a2 = read();
        int a3 = read();

        return (a0&0xff) | ((a1 <<8)&0xff00) | ((a2<<16)&0xff0000) | ( (a3<<24)&0xff000000 );
    }


    //一定是unsigned short
    public int readShort() {

        int first = read();
        int second = read();

        return (first&0xff) | ((second <<8)&0xff00);
    }

    public float readFloat(){
        return  Float.intBitsToFloat(readInt());
    }


    public void reset(){
        pos = 0;
    }

}
