package com.sun.corba.se.impl.transport;

import com.sun.corba.se.impl.logging.ORBUtilSystemException;
import com.sun.corba.se.impl.orbutil.ORBUtility;
import com.sun.corba.se.pept.transport.EventHandler;
import com.sun.corba.se.pept.transport.ListenerThread;
import com.sun.corba.se.pept.transport.ReaderThread;
import com.sun.corba.se.pept.transport.Selector;
import com.sun.corba.se.spi.logging.CORBALogDomains;
import com.sun.corba.se.spi.orb.ORB;
import com.sun.corba.se.spi.orbutil.threadpool.NoSuchThreadPoolException;
import com.sun.corba.se.spi.orbutil.threadpool.NoSuchWorkQueueException;
import java.io.IOException;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.ClosedSelectorException;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/* loaded from: rt.jar:com/sun/corba/se/impl/transport/SelectorImpl.class */
class SelectorImpl extends Thread implements Selector {
    private ORB orb;
    private ORBUtilSystemException wrapper;
    private java.nio.channels.Selector selector = null;
    private boolean selectorStarted = false;
    private long timeout = 60000;
    private List deferredRegistrations = new ArrayList();
    private List interestOpsList = new ArrayList();
    private HashMap listenerThreads = new HashMap();
    private Map readerThreads = Collections.synchronizedMap(new HashMap());
    private volatile boolean closed = false;

    public SelectorImpl(ORB orb) {
        this.orb = orb;
        this.wrapper = ORBUtilSystemException.get(orb, CORBALogDomains.RPC_TRANSPORT);
    }

    @Override // com.sun.corba.se.pept.transport.Selector
    public void setTimeout(long j2) {
        this.timeout = j2;
    }

    @Override // com.sun.corba.se.pept.transport.Selector
    public long getTimeout() {
        return this.timeout;
    }

    @Override // com.sun.corba.se.pept.transport.Selector
    public void registerInterestOps(EventHandler eventHandler) {
        if (this.orb.transportDebugFlag) {
            dprint(".registerInterestOps:-> " + ((Object) eventHandler));
        }
        SelectionKey selectionKey = eventHandler.getSelectionKey();
        if (selectionKey.isValid()) {
            SelectionKeyAndOp selectionKeyAndOp = new SelectionKeyAndOp(selectionKey, eventHandler.getInterestOps());
            synchronized (this.interestOpsList) {
                this.interestOpsList.add(selectionKeyAndOp);
            }
            try {
                if (this.selector != null) {
                    this.selector.wakeup();
                }
            } catch (Throwable th) {
                if (this.orb.transportDebugFlag) {
                    dprint(".registerInterestOps: selector.wakeup: ", th);
                }
            }
        } else {
            this.wrapper.selectionKeyInvalid(eventHandler.toString());
            if (this.orb.transportDebugFlag) {
                dprint(".registerInterestOps: EventHandler SelectionKey not valid " + ((Object) eventHandler));
            }
        }
        if (this.orb.transportDebugFlag) {
            dprint(".registerInterestOps:<- ");
        }
    }

    @Override // com.sun.corba.se.pept.transport.Selector
    public void registerForEvent(EventHandler eventHandler) {
        if (this.orb.transportDebugFlag) {
            dprint(".registerForEvent: " + ((Object) eventHandler));
        }
        if (isClosed()) {
            if (this.orb.transportDebugFlag) {
                dprint(".registerForEvent: closed: " + ((Object) eventHandler));
                return;
            }
            return;
        }
        if (eventHandler.shouldUseSelectThreadToWait()) {
            synchronized (this.deferredRegistrations) {
                this.deferredRegistrations.add(eventHandler);
            }
            if (!this.selectorStarted) {
                startSelector();
            }
            this.selector.wakeup();
            return;
        }
        switch (eventHandler.getInterestOps()) {
            case 1:
                createReaderThread(eventHandler);
                return;
            case 16:
                createListenerThread(eventHandler);
                return;
            default:
                if (this.orb.transportDebugFlag) {
                    dprint(".registerForEvent: default: " + ((Object) eventHandler));
                }
                throw new RuntimeException("SelectorImpl.registerForEvent: unknown interest ops");
        }
    }

    @Override // com.sun.corba.se.pept.transport.Selector
    public void unregisterForEvent(EventHandler eventHandler) {
        SelectionKey selectionKey;
        if (this.orb.transportDebugFlag) {
            dprint(".unregisterForEvent: " + ((Object) eventHandler));
        }
        if (isClosed()) {
            if (this.orb.transportDebugFlag) {
                dprint(".unregisterForEvent: closed: " + ((Object) eventHandler));
                return;
            }
            return;
        }
        if (eventHandler.shouldUseSelectThreadToWait()) {
            synchronized (this.deferredRegistrations) {
                selectionKey = eventHandler.getSelectionKey();
            }
            if (selectionKey != null) {
                selectionKey.cancel();
            }
            if (this.selector != null) {
                this.selector.wakeup();
                return;
            }
            return;
        }
        switch (eventHandler.getInterestOps()) {
            case 1:
                destroyReaderThread(eventHandler);
                return;
            case 16:
                destroyListenerThread(eventHandler);
                return;
            default:
                if (this.orb.transportDebugFlag) {
                    dprint(".unregisterForEvent: default: " + ((Object) eventHandler));
                }
                throw new RuntimeException("SelectorImpl.uregisterForEvent: unknown interest ops");
        }
    }

    @Override // com.sun.corba.se.pept.transport.Selector
    public void close() {
        if (this.orb.transportDebugFlag) {
            dprint(".close");
        }
        if (isClosed()) {
            if (this.orb.transportDebugFlag) {
                dprint(".close: already closed");
                return;
            }
            return;
        }
        setClosed(true);
        Iterator it = this.listenerThreads.values().iterator();
        while (it.hasNext()) {
            ((ListenerThread) it.next()).close();
        }
        Iterator it2 = this.readerThreads.values().iterator();
        while (it2.hasNext()) {
            ((ReaderThread) it2.next()).close();
        }
        clearDeferredRegistrations();
        try {
            if (this.selector != null) {
                this.selector.wakeup();
            }
        } catch (Throwable th) {
            if (this.orb.transportDebugFlag) {
                dprint(".close: selector.wakeup: ", th);
            }
        }
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public void run() {
        int iSelect;
        setName("SelectorThread");
        while (!this.closed) {
            try {
                iSelect = 0;
                if (this.timeout == 0 && this.orb.transportDebugFlag) {
                    dprint(".run: Beginning of selection cycle");
                }
                handleDeferredRegistrations();
                enableInterestOps();
                try {
                    iSelect = this.selector.select(this.timeout);
                } catch (IOException e2) {
                    if (this.orb.transportDebugFlag) {
                        dprint(".run: selector.select: ", e2);
                    }
                } catch (ClosedSelectorException e3) {
                    if (this.orb.transportDebugFlag) {
                        dprint(".run: selector.select: ", e3);
                    }
                    break;
                }
            } catch (Throwable th) {
                if (this.orb.transportDebugFlag) {
                    dprint(".run: ignoring", th);
                }
            }
            if (this.closed) {
                break;
            }
            Iterator<SelectionKey> it = this.selector.selectedKeys().iterator();
            if (this.orb.transportDebugFlag && it.hasNext()) {
                dprint(".run: n = " + iSelect);
            }
            while (it.hasNext()) {
                SelectionKey next = it.next();
                it.remove();
                try {
                    ((EventHandler) next.attachment()).handleEvent();
                } catch (Throwable th2) {
                    if (this.orb.transportDebugFlag) {
                        dprint(".run: eventHandler.handleEvent", th2);
                    }
                }
            }
            if (this.timeout == 0 && this.orb.transportDebugFlag) {
                dprint(".run: End of selection cycle");
            }
        }
        try {
            if (this.selector != null) {
                if (this.orb.transportDebugFlag) {
                    dprint(".run: selector.close ");
                }
                this.selector.close();
            }
        } catch (Throwable th3) {
            if (this.orb.transportDebugFlag) {
                dprint(".run: selector.close: ", th3);
            }
        }
    }

    private void clearDeferredRegistrations() {
        synchronized (this.deferredRegistrations) {
            int size = this.deferredRegistrations.size();
            if (this.orb.transportDebugFlag) {
                dprint(".clearDeferredRegistrations:deferred list size == " + size);
            }
            for (int i2 = 0; i2 < size; i2++) {
                EventHandler eventHandler = (EventHandler) this.deferredRegistrations.get(i2);
                if (this.orb.transportDebugFlag) {
                    dprint(".clearDeferredRegistrations: " + ((Object) eventHandler));
                }
                SelectableChannel channel = eventHandler.getChannel();
                try {
                    if (this.orb.transportDebugFlag) {
                        dprint(".clearDeferredRegistrations:close channel == " + ((Object) channel));
                        dprint(".clearDeferredRegistrations:close channel class == " + channel.getClass().getName());
                    }
                    channel.close();
                    SelectionKey selectionKey = eventHandler.getSelectionKey();
                    if (selectionKey != null) {
                        selectionKey.cancel();
                        selectionKey.attach(null);
                    }
                } catch (IOException e2) {
                    if (this.orb.transportDebugFlag) {
                        dprint(".clearDeferredRegistrations: ", e2);
                    }
                }
            }
            this.deferredRegistrations.clear();
        }
    }

    private synchronized boolean isClosed() {
        return this.closed;
    }

    private synchronized void setClosed(boolean z2) {
        this.closed = z2;
    }

    private void startSelector() {
        try {
            this.selector = java.nio.channels.Selector.open();
            setDaemon(true);
            start();
            this.selectorStarted = true;
            if (this.orb.transportDebugFlag) {
                dprint(".startSelector: selector.start completed.");
            }
        } catch (IOException e2) {
            if (this.orb.transportDebugFlag) {
                dprint(".startSelector: Selector.open: IOException: ", e2);
            }
            RuntimeException runtimeException = new RuntimeException(".startSelector: Selector.open exception");
            runtimeException.initCause(e2);
            throw runtimeException;
        }
    }

    private void handleDeferredRegistrations() {
        synchronized (this.deferredRegistrations) {
            int size = this.deferredRegistrations.size();
            for (int i2 = 0; i2 < size; i2++) {
                EventHandler eventHandler = (EventHandler) this.deferredRegistrations.get(i2);
                if (this.orb.transportDebugFlag) {
                    dprint(".handleDeferredRegistrations: " + ((Object) eventHandler));
                }
                SelectionKey selectionKeyRegister = null;
                try {
                    selectionKeyRegister = eventHandler.getChannel().register(this.selector, eventHandler.getInterestOps(), eventHandler);
                } catch (ClosedChannelException e2) {
                    if (this.orb.transportDebugFlag) {
                        dprint(".handleDeferredRegistrations: ", e2);
                    }
                }
                eventHandler.setSelectionKey(selectionKeyRegister);
            }
            this.deferredRegistrations.clear();
        }
    }

    private void enableInterestOps() {
        synchronized (this.interestOpsList) {
            int size = this.interestOpsList.size();
            if (size > 0) {
                if (this.orb.transportDebugFlag) {
                    dprint(".enableInterestOps:->");
                }
                for (int i2 = 0; i2 < size; i2++) {
                    SelectionKeyAndOp selectionKeyAndOp = (SelectionKeyAndOp) this.interestOpsList.get(i2);
                    SelectionKey selectionKey = selectionKeyAndOp.selectionKey;
                    if (selectionKey.isValid()) {
                        if (this.orb.transportDebugFlag) {
                            dprint(".enableInterestOps: " + ((Object) selectionKeyAndOp));
                        }
                        selectionKey.interestOps(selectionKey.interestOps() | selectionKeyAndOp.keyOp);
                    }
                }
                this.interestOpsList.clear();
                if (this.orb.transportDebugFlag) {
                    dprint(".enableInterestOps:<-");
                }
            }
        }
    }

    private void createListenerThread(EventHandler eventHandler) {
        if (this.orb.transportDebugFlag) {
            dprint(".createListenerThread: " + ((Object) eventHandler));
        }
        ListenerThreadImpl listenerThreadImpl = new ListenerThreadImpl(this.orb, eventHandler.getAcceptor(), this);
        this.listenerThreads.put(eventHandler, listenerThreadImpl);
        Throwable th = null;
        try {
            this.orb.getThreadPoolManager().getThreadPool(0).getWorkQueue(0).addWork(listenerThreadImpl);
        } catch (NoSuchThreadPoolException e2) {
            th = e2;
        } catch (NoSuchWorkQueueException e3) {
            th = e3;
        }
        if (th != null) {
            RuntimeException runtimeException = new RuntimeException(th.toString());
            runtimeException.initCause(th);
            throw runtimeException;
        }
    }

    private void destroyListenerThread(EventHandler eventHandler) {
        if (this.orb.transportDebugFlag) {
            dprint(".destroyListenerThread: " + ((Object) eventHandler));
        }
        ListenerThread listenerThread = (ListenerThread) this.listenerThreads.get(eventHandler);
        if (listenerThread == null) {
            if (this.orb.transportDebugFlag) {
                dprint(".destroyListenerThread: cannot find ListenerThread - ignoring.");
            }
        } else {
            this.listenerThreads.remove(eventHandler);
            listenerThread.close();
        }
    }

    private void createReaderThread(EventHandler eventHandler) {
        if (this.orb.transportDebugFlag) {
            dprint(".createReaderThread: " + ((Object) eventHandler));
        }
        ReaderThreadImpl readerThreadImpl = new ReaderThreadImpl(this.orb, eventHandler.getConnection(), this);
        this.readerThreads.put(eventHandler, readerThreadImpl);
        Throwable th = null;
        try {
            this.orb.getThreadPoolManager().getThreadPool(0).getWorkQueue(0).addWork(readerThreadImpl);
        } catch (NoSuchThreadPoolException e2) {
            th = e2;
        } catch (NoSuchWorkQueueException e3) {
            th = e3;
        }
        if (th != null) {
            RuntimeException runtimeException = new RuntimeException(th.toString());
            runtimeException.initCause(th);
            throw runtimeException;
        }
    }

    private void destroyReaderThread(EventHandler eventHandler) {
        if (this.orb.transportDebugFlag) {
            dprint(".destroyReaderThread: " + ((Object) eventHandler));
        }
        ReaderThread readerThread = (ReaderThread) this.readerThreads.get(eventHandler);
        if (readerThread == null) {
            if (this.orb.transportDebugFlag) {
                dprint(".destroyReaderThread: cannot find ReaderThread - ignoring.");
            }
        } else {
            this.readerThreads.remove(eventHandler);
            readerThread.close();
        }
    }

    private void dprint(String str) {
        ORBUtility.dprint("SelectorImpl", str);
    }

    protected void dprint(String str, Throwable th) {
        dprint(str);
        th.printStackTrace(System.out);
    }

    /* loaded from: rt.jar:com/sun/corba/se/impl/transport/SelectorImpl$SelectionKeyAndOp.class */
    private class SelectionKeyAndOp {
        public int keyOp;
        public SelectionKey selectionKey;

        public SelectionKeyAndOp(SelectionKey selectionKey, int i2) {
            this.selectionKey = selectionKey;
            this.keyOp = i2;
        }
    }
}
