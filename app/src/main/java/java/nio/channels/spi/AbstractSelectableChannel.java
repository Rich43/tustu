package java.nio.channels.spi;

import java.io.IOException;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.IllegalBlockingModeException;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;

/* loaded from: rt.jar:java/nio/channels/spi/AbstractSelectableChannel.class */
public abstract class AbstractSelectableChannel extends SelectableChannel {
    private final SelectorProvider provider;
    private SelectionKey[] keys = null;
    private int keyCount = 0;
    private final Object keyLock = new Object();
    private final Object regLock = new Object();
    private volatile boolean nonBlocking;
    static final /* synthetic */ boolean $assertionsDisabled;

    protected abstract void implCloseSelectableChannel() throws IOException;

    protected abstract void implConfigureBlocking(boolean z2) throws IOException;

    static {
        $assertionsDisabled = !AbstractSelectableChannel.class.desiredAssertionStatus();
    }

    protected AbstractSelectableChannel(SelectorProvider selectorProvider) {
        this.provider = selectorProvider;
    }

    @Override // java.nio.channels.SelectableChannel
    public final SelectorProvider provider() {
        return this.provider;
    }

    private void addKey(SelectionKey selectionKey) {
        if (!$assertionsDisabled && !Thread.holdsLock(this.keyLock)) {
            throw new AssertionError();
        }
        int i2 = 0;
        if (this.keys != null && this.keyCount < this.keys.length) {
            i2 = 0;
            while (i2 < this.keys.length && this.keys[i2] != null) {
                i2++;
            }
        } else if (this.keys == null) {
            this.keys = new SelectionKey[3];
        } else {
            SelectionKey[] selectionKeyArr = new SelectionKey[this.keys.length * 2];
            for (int i3 = 0; i3 < this.keys.length; i3++) {
                selectionKeyArr[i3] = this.keys[i3];
            }
            this.keys = selectionKeyArr;
            i2 = this.keyCount;
        }
        this.keys[i2] = selectionKey;
        this.keyCount++;
    }

    private SelectionKey findKey(Selector selector) {
        synchronized (this.keyLock) {
            if (this.keys == null) {
                return null;
            }
            for (int i2 = 0; i2 < this.keys.length; i2++) {
                if (this.keys[i2] != null && this.keys[i2].selector() == selector) {
                    return this.keys[i2];
                }
            }
            return null;
        }
    }

    void removeKey(SelectionKey selectionKey) {
        synchronized (this.keyLock) {
            for (int i2 = 0; i2 < this.keys.length; i2++) {
                if (this.keys[i2] == selectionKey) {
                    this.keys[i2] = null;
                    this.keyCount--;
                }
            }
            ((AbstractSelectionKey) selectionKey).invalidate();
        }
    }

    private boolean haveValidKeys() {
        synchronized (this.keyLock) {
            if (this.keyCount == 0) {
                return false;
            }
            for (int i2 = 0; i2 < this.keys.length; i2++) {
                if (this.keys[i2] != null && this.keys[i2].isValid()) {
                    return true;
                }
            }
            return false;
        }
    }

    @Override // java.nio.channels.SelectableChannel
    public final boolean isRegistered() {
        boolean z2;
        synchronized (this.keyLock) {
            z2 = this.keyCount != 0;
        }
        return z2;
    }

    @Override // java.nio.channels.SelectableChannel
    public final SelectionKey keyFor(Selector selector) {
        return findKey(selector);
    }

    @Override // java.nio.channels.SelectableChannel
    public final SelectionKey register(Selector selector, int i2, Object obj) throws ClosedChannelException {
        SelectionKey selectionKey;
        synchronized (this.regLock) {
            if (!isOpen()) {
                throw new ClosedChannelException();
            }
            if ((i2 & (validOps() ^ (-1))) != 0) {
                throw new IllegalArgumentException();
            }
            if (isBlocking()) {
                throw new IllegalBlockingModeException();
            }
            SelectionKey selectionKeyFindKey = findKey(selector);
            if (selectionKeyFindKey != null) {
                selectionKeyFindKey.interestOps(i2);
                selectionKeyFindKey.attach(obj);
            }
            if (selectionKeyFindKey == null) {
                synchronized (this.keyLock) {
                    if (!isOpen()) {
                        throw new ClosedChannelException();
                    }
                    selectionKeyFindKey = ((AbstractSelector) selector).register(this, i2, obj);
                    addKey(selectionKeyFindKey);
                }
            }
            selectionKey = selectionKeyFindKey;
        }
        return selectionKey;
    }

    @Override // java.nio.channels.spi.AbstractInterruptibleChannel
    protected final void implCloseChannel() throws IOException {
        implCloseSelectableChannel();
        synchronized (this.keyLock) {
            int length = this.keys == null ? 0 : this.keys.length;
            for (int i2 = 0; i2 < length; i2++) {
                SelectionKey selectionKey = this.keys[i2];
                if (selectionKey != null) {
                    selectionKey.cancel();
                }
            }
        }
    }

    @Override // java.nio.channels.SelectableChannel
    public final boolean isBlocking() {
        return !this.nonBlocking;
    }

    @Override // java.nio.channels.SelectableChannel
    public final Object blockingLock() {
        return this.regLock;
    }

    @Override // java.nio.channels.SelectableChannel
    public final SelectableChannel configureBlocking(boolean z2) throws IOException {
        synchronized (this.regLock) {
            if (!isOpen()) {
                throw new ClosedChannelException();
            }
            if (z2 != (!this.nonBlocking)) {
                if (z2 && haveValidKeys()) {
                    throw new IllegalBlockingModeException();
                }
                implConfigureBlocking(z2);
                this.nonBlocking = !z2;
            }
        }
        return this;
    }
}
