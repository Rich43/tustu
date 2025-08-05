package com.intel.bluetooth.obex;

import java.io.IOException;
import java.io.InputStream;

/* loaded from: bluecove-2.1.1.jar:com/intel/bluetooth/obex/UnsupportedInputStream.class */
class UnsupportedInputStream extends InputStream {
    UnsupportedInputStream() {
    }

    @Override // java.io.InputStream
    public int read() throws IOException {
        throw new IOException("Input not supported in current operation");
    }
}
