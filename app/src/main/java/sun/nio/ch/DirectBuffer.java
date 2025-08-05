package sun.nio.ch;

import sun.misc.Cleaner;

/* loaded from: rt.jar:sun/nio/ch/DirectBuffer.class */
public interface DirectBuffer {
    long address();

    Object attachment();

    Cleaner cleaner();
}
