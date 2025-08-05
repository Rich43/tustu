package com.intel.bluetooth;

import java.io.IOException;

/* loaded from: bluecove-2.1.1.jar:com/intel/bluetooth/NotImplementedIOException.class */
public class NotImplementedIOException extends IOException {
    private static final long serialVersionUID = 1;
    public static final boolean enabled = true;

    public NotImplementedIOException() {
        super("Not Implemented");
    }
}
