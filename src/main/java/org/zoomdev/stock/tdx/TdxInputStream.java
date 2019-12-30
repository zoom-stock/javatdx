package org.zoomdev.stock.tdx;

import com.jcraft.jzlib.Inflater;
import com.jcraft.jzlib.JZlib;

import java.io.*;

public class TdxInputStream extends TypedInputStream {

    public static final byte[] EMPTY = new byte[0];

    private  InputStream is;
    private byte[] header = new byte[0x10];

    public TdxInputStream(byte[] bytes) {
        super(bytes);
    }

    public TdxInputStream(InputStream is){
        super(EMPTY);
        this.is = is;
    }

    public static final int EOF = -1;

    private int ensureRead(byte[] buffer,int offset,int length) throws IOException {
        int remaining = length;
        while (remaining > 0) {
            final int location = length - remaining;
            final int count = is.read(buffer, offset + location, remaining);
            if (EOF == count) { // EOF
                throw new IOException("Cannot read data");
            }
            remaining -= count;
        }
        return length - remaining;
    }

    public void ensureRead(byte[] cache) throws IOException {

        int pos = 0;
        for(int i=0; i < 10; ++i){
            int readed = ensureRead(cache,pos,cache.length-pos);
            pos += readed;
            if(pos >= cache.length){
                return;
            }
        }

    }

    private int packSize;
    private int uncompressSize;

    public void readHeader() throws IOException {

        ensureRead(header);
        packSize = HexUtils.readShort(header, 12);
        uncompressSize = HexUtils.readShort(header, 14);
    }

    public boolean needsToInflate(){
        return packSize != uncompressSize;
    }



    public byte[] readBody(boolean skipInflate) throws IOException {
        byte[] data = new byte[packSize];
        ensureRead(data);
        if(needsToInflate() && !skipInflate){
            //后面的还有104个
            byte[] unzip = new byte[uncompressSize];
            Inflater inflater = new Inflater();
            inflater.setInput(data);
            inflater.setOutput(unzip);
            int err = inflater.init();
            // CHECK_ERR(inflater, err, "inflateInit");

            while (inflater.total_out < uncompressSize &&
                    inflater.total_in < packSize) {
                inflater.avail_in = inflater.avail_out = 1; /* force small buffers */
                err = inflater.inflate(JZlib.Z_NO_FLUSH);
                if (err == JZlib.Z_STREAM_END) break;
                //   CHECK_ERR(inflater, err, "inflate");
            }

            setBuf(unzip);
            //System.out.println(Hex.encodeHexStr(unzip));
            return unzip;
        }
        setBuf(data);
        return data;
    }




    public byte[] toByteArray(){
        return buf;
    }


    public byte[] readPack(boolean skipInflate) throws IOException {
        readHeader();
        byte[] pack= readBody(skipInflate);
        return pack;
    }





//
    public double getPrice() throws IOException {
        int pos_byte = 6;
        int bdata = readByte();
        int intdata = bdata & 0x3f;
        boolean sign;
        if ((bdata & 0x40) != 0) {
            sign = true;
        } else {
            sign = false;
        }
        if ((bdata & 0x80) != 0) {
            while (true) {
                bdata = readByte();
                intdata += (bdata & 0x7f) << pos_byte;
                pos_byte += 7;

                if ((bdata & 0x80) != 0) {
                    continue;
                }
                break;
            }
        }

        if (sign) {
            intdata = -intdata;
        }
        return intdata;
    }



}
