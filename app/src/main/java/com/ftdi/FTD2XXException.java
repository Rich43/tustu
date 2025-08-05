package com.ftdi;

import java.io.IOException;

/* loaded from: JavaFTD2XX.jar:com/ftdi/FTD2XXException.class */
public class FTD2XXException extends IOException {
    public FTD2XXException(FT_STATUS ftStatus) {
        super("D2XX error, ftStatus:" + ((Object) ftStatus));
    }

    public FTD2XXException(int ftStatus) {
        this(FT_STATUS.values()[ftStatus]);
    }

    public FTD2XXException(Throwable cause) {
        super(cause);
    }

    public FTD2XXException(String message, Throwable cause) {
        super(message, cause);
    }

    public FTD2XXException(String message) {
        super(message);
    }

    public FTD2XXException() {
    }
}
