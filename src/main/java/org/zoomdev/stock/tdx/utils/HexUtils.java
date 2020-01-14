package org.zoomdev.stock.tdx.utils;

import java.io.IOException;
import java.io.OutputStream;

public class HexUtils {
    private static final char[] DIGITS_LOWER = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    /**
     * @param data
     * @param start
     * @return
     */
    public static int readShort(byte[] data, int start) {
        return (((data[start + 1] << 8) & 0xff00) |
                (data[start] & 0xff));
    }

    public static int readInt(byte[] data, int start) {
        return ((data[start + 3] << 24) & 0xff000000) |
                ((data[start + 2] << 16) & 0xff0000) |
                ((data[start + 1] << 8) & 0xff00) |
                (data[start] & 0xff);
    }

    public static void writeShort(byte[] data, int start, int value) {
        data[start] = (byte) (value & 0xff);
        data[start + 1] = (byte) ((value & 0xff00) >> 8);
    }

    public static void writeInt(byte[] data, int start, int value) {

    }

    public static void writeShort(OutputStream stream, int value) {
        try {
            stream.write(value & 0xff);
            stream.write((value & 0xff00) >> 8);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void writeInt(OutputStream stream, int value) {
        try {
            stream.write(value & 0xff);
            stream.write((value & 0xff00) >> 8);
            stream.write((value & 0xff0000) >> 16);
            stream.write((value & 0xff000000) >> 32);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String encodeHexStr(byte[] data) {
        return new String(encodeHex(data, 0, data.length, DIGITS_LOWER));
    }

    protected static char[] encodeHex(byte[] data, int start, int l, char[] toDigits) {
        char[] out = new char[l << 1];
        int i = start;

        for (int j = 0; i < l; ++i) {
            out[j++] = toDigits[(240 & data[i]) >>> 4];
            out[j++] = toDigits[15 & data[i]];
        }

        return out;
    }

    public static byte[] decodeHex(String data) {
        return decodeHex(data.toCharArray());
    }

    public static byte[] decodeHex(char[] data) {
        int len = data.length;
        if ((len & 1) != 0) {
            throw new RuntimeException("Odd number of characters.");
        } else {
            byte[] out = new byte[len >> 1];
            int i = 0;

            for (int j = 0; j < len; ++i) {
                int f = toDigit(data[j], j) << 4;
                ++j;
                f |= toDigit(data[j], j);
                ++j;
                out[i] = (byte) (f & 255);
            }

            return out;
        }
    }

    protected static int toDigit(char ch, int index) {
        int digit = Character.digit(ch, 16);
        if (digit == -1) {
            throw new RuntimeException("Illegal hexadecimal character " + ch + " at index " + index);
        } else {
            return digit;
        }
    }
}
