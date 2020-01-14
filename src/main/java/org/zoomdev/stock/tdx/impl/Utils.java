package org.zoomdev.stock.tdx.impl;

import java.io.Closeable;

class Utils {
    public static void close(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (Throwable t) {

            }
        }
    }
}
