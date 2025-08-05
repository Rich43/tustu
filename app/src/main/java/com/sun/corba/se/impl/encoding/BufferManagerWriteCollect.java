package com.sun.corba.se.impl.encoding;

import com.sun.corba.se.impl.orbutil.ORBUtility;
import com.sun.corba.se.impl.protocol.giopmsgheaders.MessageBase;
import com.sun.corba.se.pept.encoding.OutputObject;
import com.sun.corba.se.pept.transport.ByteBufferPool;
import com.sun.corba.se.pept.transport.Connection;
import com.sun.corba.se.spi.orb.ORB;
import java.util.Iterator;

/* loaded from: rt.jar:com/sun/corba/se/impl/encoding/BufferManagerWriteCollect.class */
public class BufferManagerWriteCollect extends BufferManagerWrite {
    private BufferQueue queue;
    private boolean sentFragment;
    private boolean debug;

    BufferManagerWriteCollect(ORB orb) {
        super(orb);
        this.queue = new BufferQueue();
        this.sentFragment = false;
        this.debug = false;
        if (orb != null) {
            this.debug = orb.transportDebugFlag;
        }
    }

    @Override // com.sun.corba.se.impl.encoding.BufferManagerWrite
    public boolean sentFragment() {
        return this.sentFragment;
    }

    @Override // com.sun.corba.se.impl.encoding.BufferManagerWrite
    public int getBufferSize() {
        return this.orb.getORBData().getGIOPFragmentSize();
    }

    @Override // com.sun.corba.se.impl.encoding.BufferManagerWrite
    public void overflow(ByteBufferWithInfo byteBufferWithInfo) {
        MessageBase.setFlag(byteBufferWithInfo.byteBuffer, 2);
        this.queue.enqueue(byteBufferWithInfo);
        ByteBufferWithInfo byteBufferWithInfo2 = new ByteBufferWithInfo(this.orb, this);
        byteBufferWithInfo2.fragmented = true;
        ((CDROutputObject) this.outputObject).setByteBufferWithInfo(byteBufferWithInfo2);
        ((CDROutputObject) this.outputObject).getMessageHeader().createFragmentMessage().write((CDROutputObject) this.outputObject);
    }

    @Override // com.sun.corba.se.impl.encoding.BufferManagerWrite
    public void sendMessage() {
        this.queue.enqueue(((CDROutputObject) this.outputObject).getByteBufferWithInfo());
        Iterator it = iterator();
        Connection connection = ((OutputObject) this.outputObject).getMessageMediator().getConnection();
        connection.writeLock();
        try {
            ByteBufferPool byteBufferPool = this.orb.getByteBufferPool();
            while (it.hasNext()) {
                ByteBufferWithInfo byteBufferWithInfo = (ByteBufferWithInfo) it.next();
                ((CDROutputObject) this.outputObject).setByteBufferWithInfo(byteBufferWithInfo);
                connection.sendWithoutLock((CDROutputObject) this.outputObject);
                this.sentFragment = true;
                if (this.debug) {
                    int iIdentityHashCode = System.identityHashCode(byteBufferWithInfo.byteBuffer);
                    StringBuffer stringBuffer = new StringBuffer(80);
                    stringBuffer.append("sendMessage() - releasing ByteBuffer id (");
                    stringBuffer.append(iIdentityHashCode).append(") to ByteBufferPool.");
                    dprint(stringBuffer.toString());
                }
                byteBufferPool.releaseByteBuffer(byteBufferWithInfo.byteBuffer);
                byteBufferWithInfo.byteBuffer = null;
            }
            this.sentFullMessage = true;
            connection.writeUnlock();
        } catch (Throwable th) {
            connection.writeUnlock();
            throw th;
        }
    }

    @Override // com.sun.corba.se.impl.encoding.BufferManagerWrite
    public void close() {
        Iterator it = iterator();
        ByteBufferPool byteBufferPool = this.orb.getByteBufferPool();
        while (it.hasNext()) {
            ByteBufferWithInfo byteBufferWithInfo = (ByteBufferWithInfo) it.next();
            if (byteBufferWithInfo != null && byteBufferWithInfo.byteBuffer != null) {
                if (this.debug) {
                    int iIdentityHashCode = System.identityHashCode(byteBufferWithInfo.byteBuffer);
                    StringBuffer stringBuffer = new StringBuffer(80);
                    stringBuffer.append("close() - releasing ByteBuffer id (");
                    stringBuffer.append(iIdentityHashCode).append(") to ByteBufferPool.");
                    dprint(stringBuffer.toString());
                }
                byteBufferPool.releaseByteBuffer(byteBufferWithInfo.byteBuffer);
                byteBufferWithInfo.byteBuffer = null;
            }
        }
    }

    private void dprint(String str) {
        ORBUtility.dprint("BufferManagerWriteCollect", str);
    }

    private Iterator iterator() {
        return new BufferManagerWriteCollectIterator();
    }

    /* loaded from: rt.jar:com/sun/corba/se/impl/encoding/BufferManagerWriteCollect$BufferManagerWriteCollectIterator.class */
    private class BufferManagerWriteCollectIterator implements Iterator {
        private BufferManagerWriteCollectIterator() {
        }

        @Override // java.util.Iterator
        public boolean hasNext() {
            return BufferManagerWriteCollect.this.queue.size() != 0;
        }

        @Override // java.util.Iterator
        public Object next() {
            return BufferManagerWriteCollect.this.queue.dequeue();
        }

        @Override // java.util.Iterator
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }
}
