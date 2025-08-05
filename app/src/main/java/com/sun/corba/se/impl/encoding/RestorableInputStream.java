package com.sun.corba.se.impl.encoding;

/* loaded from: rt.jar:com/sun/corba/se/impl/encoding/RestorableInputStream.class */
interface RestorableInputStream {
    Object createStreamMemento();

    void restoreInternalState(Object obj);
}
