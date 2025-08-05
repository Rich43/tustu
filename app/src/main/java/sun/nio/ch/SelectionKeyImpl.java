package sun.nio.ch;

import java.nio.channels.CancelledKeyException;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.spi.AbstractSelectionKey;

/* loaded from: rt.jar:sun/nio/ch/SelectionKeyImpl.class */
public class SelectionKeyImpl extends AbstractSelectionKey {
    final SelChImpl channel;
    public final SelectorImpl selector;
    private int index;
    private volatile int interestOps;
    private int readyOps;

    SelectionKeyImpl(SelChImpl selChImpl, SelectorImpl selectorImpl) {
        this.channel = selChImpl;
        this.selector = selectorImpl;
    }

    @Override // java.nio.channels.SelectionKey
    public SelectableChannel channel() {
        return (SelectableChannel) this.channel;
    }

    @Override // java.nio.channels.SelectionKey
    public Selector selector() {
        return this.selector;
    }

    int getIndex() {
        return this.index;
    }

    void setIndex(int i2) {
        this.index = i2;
    }

    private void ensureValid() {
        if (!isValid()) {
            throw new CancelledKeyException();
        }
    }

    @Override // java.nio.channels.SelectionKey
    public int interestOps() {
        ensureValid();
        return this.interestOps;
    }

    @Override // java.nio.channels.SelectionKey
    public SelectionKey interestOps(int i2) {
        ensureValid();
        return nioInterestOps(i2);
    }

    @Override // java.nio.channels.SelectionKey
    public int readyOps() {
        ensureValid();
        return this.readyOps;
    }

    public void nioReadyOps(int i2) {
        this.readyOps = i2;
    }

    public int nioReadyOps() {
        return this.readyOps;
    }

    public SelectionKey nioInterestOps(int i2) {
        if ((i2 & (channel().validOps() ^ (-1))) != 0) {
            throw new IllegalArgumentException();
        }
        this.channel.translateAndSetInterestOps(i2, this);
        this.interestOps = i2;
        return this;
    }

    public int nioInterestOps() {
        return this.interestOps;
    }
}
