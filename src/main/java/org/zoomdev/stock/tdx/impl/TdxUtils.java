package org.zoomdev.stock.tdx.impl;

import java.io.Closeable;

public class TdxUtils {
    public static void close(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (Throwable t) {

            }
        }
    }
}
