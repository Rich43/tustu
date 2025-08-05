package com.sun.corba.se.impl.encoding;

/* loaded from: rt.jar:com/sun/corba/se/impl/encoding/MarkAndResetHandler.class */
interface MarkAndResetHandler {
    void mark(RestorableInputStream restorableInputStream);

    void fragmentationOccured(ByteBufferWithInfo byteBufferWithInfo);

    void reset();
}
