package com.intel.bluetooth;

/* loaded from: bluecove-2.1.1.jar:com/intel/bluetooth/NotSupportedRuntimeException.class */
public class NotSupportedRuntimeException extends RuntimeException {
    private static final long serialVersionUID = 1;

    public NotSupportedRuntimeException() {
        super("Not Supported");
    }

    public NotSupportedRuntimeException(String stackName) {
        super(new StringBuffer().append("Not Supported on ").append(stackName).toString());
    }
}
