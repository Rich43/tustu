package com.sun.corba.se.impl.protocol;

/* loaded from: rt.jar:com/sun/corba/se/impl/protocol/AddressingDispositionException.class */
public class AddressingDispositionException extends RuntimeException {
    private short expectedAddrDisp;

    public AddressingDispositionException(short s2) {
        this.expectedAddrDisp = (short) 0;
        this.expectedAddrDisp = s2;
    }

    public short expectedAddrDisp() {
        return this.expectedAddrDisp;
    }
}
