package com.sun.nio.sctp;

import jdk.Exported;

@Exported
/* loaded from: rt.jar:com/sun/nio/sctp/InvalidStreamException.class */
public class InvalidStreamException extends IllegalArgumentException {
    private static final long serialVersionUID = -9172703378046665558L;

    public InvalidStreamException() {
    }

    public InvalidStreamException(String str) {
        super(str);
    }
}
