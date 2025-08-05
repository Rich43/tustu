package sun.nio.ch;

import java.io.FileDescriptor;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousCloseException;
import java.nio.channels.Pipe;
import java.nio.channels.SocketChannel;
import java.nio.channels.spi.SelectorProvider;

/* loaded from: rt.jar:sun/nio/ch/SourceChannelImpl.class */
class SourceChannelImpl extends Pipe.SourceChannel implements SelChImpl {
    SocketChannel sc;

    @Override // sun.nio.ch.SelChImpl
    public FileDescriptor getFD() {
        return ((SocketChannelImpl) this.sc).getFD();
    }

    @Override // sun.nio.ch.SelChImpl
    public int getFDVal() {
        return ((SocketChannelImpl) this.sc).getFDVal();
    }

    SourceChannelImpl(SelectorProvider selectorProvider, SocketChannel socketChannel) {
        super(selectorProvider);
        this.sc = socketChannel;
    }

    @Override // java.nio.channels.spi.AbstractSelectableChannel
    protected void implCloseSelectableChannel() throws IOException {
        if (!isRegistered()) {
            kill();
        }
    }

    @Override // sun.nio.ch.SelChImpl
    public void kill() throws IOException {
        this.sc.close();
    }

    @Override // java.nio.channels.spi.AbstractSelectableChannel
    protected void implConfigureBlocking(boolean z2) throws IOException {
        this.sc.configureBlocking(z2);
    }

    public boolean translateReadyOps(int i2, int i3, SelectionKeyImpl selectionKeyImpl) {
        int iNioInterestOps = selectionKeyImpl.nioInterestOps();
        int iNioReadyOps = selectionKeyImpl.nioReadyOps();
        int i4 = i3;
        if ((i2 & Net.POLLNVAL) != 0) {
            throw new Error("POLLNVAL detected");
        }
        if ((i2 & (Net.POLLERR | Net.POLLHUP)) != 0) {
            selectionKeyImpl.nioReadyOps(iNioInterestOps);
            return (iNioInterestOps & (iNioReadyOps ^ (-1))) != 0;
        }
        if ((i2 & Net.POLLIN) != 0 && (iNioInterestOps & 1) != 0) {
            i4 |= 1;
        }
        selectionKeyImpl.nioReadyOps(i4);
        return (i4 & (iNioReadyOps ^ (-1))) != 0;
    }

    @Override // sun.nio.ch.SelChImpl
    public boolean translateAndUpdateReadyOps(int i2, SelectionKeyImpl selectionKeyImpl) {
        return translateReadyOps(i2, selectionKeyImpl.nioReadyOps(), selectionKeyImpl);
    }

    @Override // sun.nio.ch.SelChImpl
    public boolean translateAndSetReadyOps(int i2, SelectionKeyImpl selectionKeyImpl) {
        return translateReadyOps(i2, 0, selectionKeyImpl);
    }

    @Override // sun.nio.ch.SelChImpl
    public void translateAndSetInterestOps(int i2, SelectionKeyImpl selectionKeyImpl) {
        if ((i2 & 1) != 0) {
            i2 = Net.POLLIN;
        }
        selectionKeyImpl.selector.putEventOps(selectionKeyImpl, i2);
    }

    @Override // java.nio.channels.ReadableByteChannel
    public int read(ByteBuffer byteBuffer) throws IOException {
        try {
            return this.sc.read(byteBuffer);
        } catch (AsynchronousCloseException e2) {
            close();
            throw e2;
        }
    }

    @Override // java.nio.channels.ScatteringByteChannel
    public long read(ByteBuffer[] byteBufferArr, int i2, int i3) throws IOException {
        if (i2 < 0 || i3 < 0 || i2 > byteBufferArr.length - i3) {
            throw new IndexOutOfBoundsException();
        }
        try {
            return read(Util.subsequence(byteBufferArr, i2, i3));
        } catch (AsynchronousCloseException e2) {
            close();
            throw e2;
        }
    }

    @Override // java.nio.channels.ScatteringByteChannel
    public long read(ByteBuffer[] byteBufferArr) throws IOException {
        try {
            return this.sc.read(byteBufferArr);
        } catch (AsynchronousCloseException e2) {
            close();
            throw e2;
        }
    }
}
