package com.sun.corba.se.impl.encoding;

import com.sun.corba.se.impl.logging.ORBUtilSystemException;
import com.sun.corba.se.impl.orbutil.ORBUtility;
import com.sun.corba.se.impl.protocol.RequestCanceledException;
import com.sun.corba.se.impl.protocol.giopmsgheaders.FragmentMessage;
import com.sun.corba.se.impl.protocol.giopmsgheaders.Message;
import com.sun.corba.se.pept.transport.ByteBufferPool;
import com.sun.corba.se.spi.logging.CORBALogDomains;
import com.sun.corba.se.spi.orb.ORB;
import java.nio.ByteBuffer;
import java.util.Iterator;
import java.util.LinkedList;

/* loaded from: rt.jar:com/sun/corba/se/impl/encoding/BufferManagerReadStream.class */
public class BufferManagerReadStream implements BufferManagerRead, MarkAndResetHandler {
    private ORB orb;
    private ORBUtilSystemException wrapper;
    private boolean debug;
    private boolean receivedCancel = false;
    private int cancelReqId = 0;
    private boolean endOfStream = true;
    private BufferQueue fragmentQueue = new BufferQueue();
    private long FRAGMENT_TIMEOUT = 60000;
    private boolean markEngaged = false;
    private LinkedList fragmentStack = null;
    private RestorableInputStream inputStream = null;
    private Object streamMemento = null;

    BufferManagerReadStream(ORB orb) {
        this.debug = false;
        this.orb = orb;
        this.wrapper = ORBUtilSystemException.get(orb, CORBALogDomains.RPC_ENCODING);
        this.debug = orb.transportDebugFlag;
    }

    @Override // com.sun.corba.se.impl.encoding.BufferManagerRead
    public void cancelProcessing(int i2) {
        synchronized (this.fragmentQueue) {
            this.receivedCancel = true;
            this.cancelReqId = i2;
            this.fragmentQueue.notify();
        }
    }

    @Override // com.sun.corba.se.impl.encoding.BufferManagerRead
    public void processFragment(ByteBuffer byteBuffer, FragmentMessage fragmentMessage) {
        ByteBufferWithInfo byteBufferWithInfo = new ByteBufferWithInfo(this.orb, byteBuffer, fragmentMessage.getHeaderLength());
        synchronized (this.fragmentQueue) {
            if (this.debug) {
                int iIdentityHashCode = System.identityHashCode(byteBuffer);
                StringBuffer stringBuffer = new StringBuffer(80);
                stringBuffer.append("processFragment() - queueing ByteBuffer id (");
                stringBuffer.append(iIdentityHashCode).append(") to fragment queue.");
                dprint(stringBuffer.toString());
            }
            this.fragmentQueue.enqueue(byteBufferWithInfo);
            this.endOfStream = !fragmentMessage.moreFragmentsToFollow();
            this.fragmentQueue.notify();
        }
    }

    @Override // com.sun.corba.se.impl.encoding.BufferManagerRead
    public ByteBufferWithInfo underflow(ByteBufferWithInfo byteBufferWithInfo) {
        ByteBufferWithInfo byteBufferWithInfoDequeue;
        synchronized (this.fragmentQueue) {
            if (this.receivedCancel) {
                throw new RequestCanceledException(this.cancelReqId);
            }
            while (this.fragmentQueue.size() == 0) {
                if (this.endOfStream) {
                    throw this.wrapper.endOfStream();
                }
                boolean z2 = false;
                try {
                    this.fragmentQueue.wait(this.FRAGMENT_TIMEOUT);
                } catch (InterruptedException e2) {
                    z2 = true;
                }
                if (!z2 && this.fragmentQueue.size() == 0) {
                    throw this.wrapper.bufferReadManagerTimeout();
                }
                if (this.receivedCancel) {
                    throw new RequestCanceledException(this.cancelReqId);
                }
            }
            byteBufferWithInfoDequeue = this.fragmentQueue.dequeue();
            byteBufferWithInfoDequeue.fragmented = true;
            if (this.debug) {
                int iIdentityHashCode = System.identityHashCode(byteBufferWithInfoDequeue.byteBuffer);
                StringBuffer stringBuffer = new StringBuffer(80);
                stringBuffer.append("underflow() - dequeued ByteBuffer id (");
                stringBuffer.append(iIdentityHashCode).append(") from fragment queue.");
                dprint(stringBuffer.toString());
            }
            if (!this.markEngaged && byteBufferWithInfo != null && byteBufferWithInfo.byteBuffer != null) {
                ByteBufferPool byteBufferPool = getByteBufferPool();
                if (this.debug) {
                    int iIdentityHashCode2 = System.identityHashCode(byteBufferWithInfo.byteBuffer);
                    StringBuffer stringBuffer2 = new StringBuffer(80);
                    stringBuffer2.append("underflow() - releasing ByteBuffer id (");
                    stringBuffer2.append(iIdentityHashCode2).append(") to ByteBufferPool.");
                    dprint(stringBuffer2.toString());
                }
                byteBufferPool.releaseByteBuffer(byteBufferWithInfo.byteBuffer);
                byteBufferWithInfo.byteBuffer = null;
            }
        }
        return byteBufferWithInfoDequeue;
    }

    @Override // com.sun.corba.se.impl.encoding.BufferManagerRead
    public void init(Message message) {
        if (message != null) {
            this.endOfStream = !message.moreFragmentsToFollow();
        }
    }

    @Override // com.sun.corba.se.impl.encoding.BufferManagerRead
    public void close(ByteBufferWithInfo byteBufferWithInfo) {
        int iIdentityHashCode;
        int iIdentityHashCode2 = 0;
        if (this.fragmentQueue != null) {
            synchronized (this.fragmentQueue) {
                if (byteBufferWithInfo != null) {
                    iIdentityHashCode2 = System.identityHashCode(byteBufferWithInfo.byteBuffer);
                }
                ByteBufferPool byteBufferPool = getByteBufferPool();
                while (this.fragmentQueue.size() != 0) {
                    ByteBufferWithInfo byteBufferWithInfoDequeue = this.fragmentQueue.dequeue();
                    if (byteBufferWithInfoDequeue != null && byteBufferWithInfoDequeue.byteBuffer != null) {
                        int iIdentityHashCode3 = System.identityHashCode(byteBufferWithInfoDequeue.byteBuffer);
                        if (iIdentityHashCode2 != iIdentityHashCode3 && this.debug) {
                            StringBuffer stringBuffer = new StringBuffer(80);
                            stringBuffer.append("close() - fragmentQueue is ").append("releasing ByteBuffer id (").append(iIdentityHashCode3).append(") to ").append("ByteBufferPool.");
                            dprint(stringBuffer.toString());
                        }
                        byteBufferPool.releaseByteBuffer(byteBufferWithInfoDequeue.byteBuffer);
                    }
                }
            }
            this.fragmentQueue = null;
        }
        if (this.fragmentStack != null && this.fragmentStack.size() != 0) {
            if (byteBufferWithInfo != null) {
                iIdentityHashCode2 = System.identityHashCode(byteBufferWithInfo.byteBuffer);
            }
            ByteBufferPool byteBufferPool2 = getByteBufferPool();
            Iterator itListIterator = this.fragmentStack.listIterator();
            while (itListIterator.hasNext()) {
                ByteBufferWithInfo byteBufferWithInfo2 = (ByteBufferWithInfo) itListIterator.next();
                if (byteBufferWithInfo2 != null && byteBufferWithInfo2.byteBuffer != null && iIdentityHashCode2 != (iIdentityHashCode = System.identityHashCode(byteBufferWithInfo2.byteBuffer))) {
                    if (this.debug) {
                        StringBuffer stringBuffer2 = new StringBuffer(80);
                        stringBuffer2.append("close() - fragmentStack - releasing ").append("ByteBuffer id (" + iIdentityHashCode + ") to ").append("ByteBufferPool.");
                        dprint(stringBuffer2.toString());
                    }
                    byteBufferPool2.releaseByteBuffer(byteBufferWithInfo2.byteBuffer);
                }
            }
            this.fragmentStack = null;
        }
    }

    protected ByteBufferPool getByteBufferPool() {
        return this.orb.getByteBufferPool();
    }

    private void dprint(String str) {
        ORBUtility.dprint("BufferManagerReadStream", str);
    }

    @Override // com.sun.corba.se.impl.encoding.MarkAndResetHandler
    public void mark(RestorableInputStream restorableInputStream) {
        this.inputStream = restorableInputStream;
        this.markEngaged = true;
        this.streamMemento = restorableInputStream.createStreamMemento();
        if (this.fragmentStack != null) {
            this.fragmentStack.clear();
        }
    }

    @Override // com.sun.corba.se.impl.encoding.MarkAndResetHandler
    public void fragmentationOccured(ByteBufferWithInfo byteBufferWithInfo) {
        if (!this.markEngaged) {
            return;
        }
        if (this.fragmentStack == null) {
            this.fragmentStack = new LinkedList();
        }
        this.fragmentStack.addFirst(new ByteBufferWithInfo(byteBufferWithInfo));
    }

    @Override // com.sun.corba.se.impl.encoding.MarkAndResetHandler
    public void reset() {
        if (!this.markEngaged) {
            return;
        }
        this.markEngaged = false;
        if (this.fragmentStack != null && this.fragmentStack.size() != 0) {
            Iterator itListIterator = this.fragmentStack.listIterator();
            synchronized (this.fragmentQueue) {
                while (itListIterator.hasNext()) {
                    this.fragmentQueue.push((ByteBufferWithInfo) itListIterator.next());
                }
            }
            this.fragmentStack.clear();
        }
        this.inputStream.restoreInternalState(this.streamMemento);
    }

    @Override // com.sun.corba.se.impl.encoding.BufferManagerRead
    public MarkAndResetHandler getMarkAndResetHandler() {
        return this;
    }
}
