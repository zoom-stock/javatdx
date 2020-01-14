package org.zoomdev.stock.tdx;

import org.zoomdev.stock.tdx.impl.TdxInputStream;
import org.zoomdev.stock.tdx.utils.DataOutputStream;

import java.io.IOException;

public interface TdxCommand<R> {
    R process(DataOutputStream outputStream, TdxInputStream inputStream) throws IOException;
}
