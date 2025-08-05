package sun.nio.ch;

import java.io.FileDescriptor;
import java.io.IOException;
import java.nio.channels.Channel;

/* loaded from: rt.jar:sun/nio/ch/SelChImpl.class */
public interface SelChImpl extends Channel {
    FileDescriptor getFD();

    int getFDVal();

    boolean translateAndUpdateReadyOps(int i2, SelectionKeyImpl selectionKeyImpl);

    boolean translateAndSetReadyOps(int i2, SelectionKeyImpl selectionKeyImpl);

    void translateAndSetInterestOps(int i2, SelectionKeyImpl selectionKeyImpl);

    int validOps();

    void kill() throws IOException;
}
