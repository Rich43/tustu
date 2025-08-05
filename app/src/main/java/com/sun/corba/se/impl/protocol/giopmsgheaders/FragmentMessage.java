package com.sun.corba.se.impl.protocol.giopmsgheaders;

/* loaded from: rt.jar:com/sun/corba/se/impl/protocol/giopmsgheaders/FragmentMessage.class */
public interface FragmentMessage extends Message {
    int getRequestId();

    int getHeaderLength();
}
