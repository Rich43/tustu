package com.intel.bluetooth;

import java.io.IOException;

/* loaded from: bluecove-2.1.1.jar:com/intel/bluetooth/NotSupportedIOException.class */
public class NotSupportedIOException extends IOException {
    private static final long serialVersionUID = 1;

    public NotSupportedIOException() {
        super("Not Supported");
    }

    public NotSupportedIOException(String stackName) {
        super(new StringBuffer().append("Not Supported on ").append(stackName).toString());
    }
}
