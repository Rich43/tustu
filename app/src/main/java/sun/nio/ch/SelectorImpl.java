package sun.nio.ch;

import com.sun.jmx.defaults.ServiceName;
import java.io.IOException;
import java.net.SocketException;
import java.nio.channels.ClosedSelectorException;
import java.nio.channels.IllegalSelectorException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.spi.AbstractSelectableChannel;
import java.nio.channels.spi.AbstractSelector;
import java.nio.channels.spi.SelectorProvider;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/* loaded from: rt.jar:sun/nio/ch/SelectorImpl.class */
public abstract class SelectorImpl extends AbstractSelector {
    protected Set<SelectionKey> selectedKeys;
    protected HashSet<SelectionKey> keys;
    private Set<SelectionKey> publicKeys;
    private Set<SelectionKey> publicSelectedKeys;

    protected abstract int doSelect(long j2) throws IOException;

    protected abstract void implClose() throws IOException;

    protected abstract void implRegister(SelectionKeyImpl selectionKeyImpl);

    protected abstract void implDereg(SelectionKeyImpl selectionKeyImpl) throws IOException;

    @Override // java.nio.channels.Selector
    public abstract Selector wakeup();

    protected SelectorImpl(SelectorProvider selectorProvider) {
        super(selectorProvider);
        this.keys = new HashSet<>();
        this.selectedKeys = new HashSet();
        if (Util.atBugLevel(ServiceName.JMX_SPEC_VERSION)) {
            this.publicKeys = this.keys;
            this.publicSelectedKeys = this.selectedKeys;
        } else {
            this.publicKeys = Collections.unmodifiableSet(this.keys);
            this.publicSelectedKeys = Util.ungrowableSet(this.selectedKeys);
        }
    }

    @Override // java.nio.channels.Selector
    public Set<SelectionKey> keys() {
        if (!isOpen() && !Util.atBugLevel(ServiceName.JMX_SPEC_VERSION)) {
            throw new ClosedSelectorException();
        }
        return this.publicKeys;
    }

    @Override // java.nio.channels.Selector
    public Set<SelectionKey> selectedKeys() {
        if (!isOpen() && !Util.atBugLevel(ServiceName.JMX_SPEC_VERSION)) {
            throw new ClosedSelectorException();
        }
        return this.publicSelectedKeys;
    }

    private int lockAndDoSelect(long j2) throws IOException {
        int iDoSelect;
        synchronized (this) {
            if (!isOpen()) {
                throw new ClosedSelectorException();
            }
            synchronized (this.publicKeys) {
                synchronized (this.publicSelectedKeys) {
                    iDoSelect = doSelect(j2);
                }
            }
        }
        return iDoSelect;
    }

    @Override // java.nio.channels.Selector
    public int select(long j2) throws IOException {
        if (j2 < 0) {
            throw new IllegalArgumentException("Negative timeout");
        }
        return lockAndDoSelect(j2 == 0 ? -1L : j2);
    }

    @Override // java.nio.channels.Selector
    public int select() throws IOException {
        return select(0L);
    }

    @Override // java.nio.channels.Selector
    public int selectNow() throws IOException {
        return lockAndDoSelect(0L);
    }

    @Override // java.nio.channels.spi.AbstractSelector
    public void implCloseSelector() throws IOException {
        wakeup();
        synchronized (this) {
            synchronized (this.publicKeys) {
                synchronized (this.publicSelectedKeys) {
                    implClose();
                }
            }
        }
    }

    public void putEventOps(SelectionKeyImpl selectionKeyImpl, int i2) {
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // java.nio.channels.spi.AbstractSelector
    protected final SelectionKey register(AbstractSelectableChannel abstractSelectableChannel, int i2, Object obj) {
        if (!(abstractSelectableChannel instanceof SelChImpl)) {
            throw new IllegalSelectorException();
        }
        SelectionKeyImpl selectionKeyImpl = new SelectionKeyImpl((SelChImpl) abstractSelectableChannel, this);
        selectionKeyImpl.attach(obj);
        synchronized (this.publicKeys) {
            implRegister(selectionKeyImpl);
        }
        selectionKeyImpl.interestOps(i2);
        return selectionKeyImpl;
    }

    void processDeregisterQueue() throws IOException {
        Set<SelectionKey> setCancelledKeys = cancelledKeys();
        synchronized (setCancelledKeys) {
            if (!setCancelledKeys.isEmpty()) {
                Iterator<SelectionKey> it = setCancelledKeys.iterator();
                while (it.hasNext()) {
                    try {
                        try {
                            implDereg((SelectionKeyImpl) it.next());
                            it.remove();
                        } catch (Throwable th) {
                            it.remove();
                            throw th;
                        }
                    } catch (SocketException e2) {
                        throw new IOException("Error deregistering key", e2);
                    }
                }
            }
        }
    }
}
