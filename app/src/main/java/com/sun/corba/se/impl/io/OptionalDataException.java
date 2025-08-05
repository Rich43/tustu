package com.sun.corba.se.impl.io;

import java.io.IOException;

/* loaded from: rt.jar:com/sun/corba/se/impl/io/OptionalDataException.class */
public class OptionalDataException extends IOException {
    public int length;
    public boolean eof;

    OptionalDataException(int i2) {
        this.eof = false;
        this.length = i2;
    }

    OptionalDataException(boolean z2) {
        this.length = 0;
        this.eof = z2;
    }
}
