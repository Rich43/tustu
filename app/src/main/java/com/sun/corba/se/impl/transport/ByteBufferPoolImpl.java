package com.sun.corba.se.impl.transport;

import com.sun.corba.se.pept.transport.ByteBufferPool;
import com.sun.corba.se.spi.orb.ORB;
import java.nio.ByteBuffer;
import java.util.ArrayList;

/* loaded from: rt.jar:com/sun/corba/se/impl/transport/ByteBufferPoolImpl.class */
public class ByteBufferPoolImpl implements ByteBufferPool {
    private ORB itsOrb;
    private int itsByteBufferSize;
    private boolean debug;
    private int itsObjectCounter = 0;
    private ArrayList itsPool = new ArrayList();

    public ByteBufferPoolImpl(ORB orb) {
        this.itsByteBufferSize = orb.getORBData().getGIOPFragmentSize();
        this.itsOrb = orb;
        this.debug = orb.transportDebugFlag;
    }

    @Override // com.sun.corba.se.pept.transport.ByteBufferPool
    public ByteBuffer getByteBuffer(int i2) {
        int size;
        ByteBuffer byteBufferAllocate = null;
        if (i2 <= this.itsByteBufferSize && !this.itsOrb.getORBData().disableDirectByteBufferUse()) {
            synchronized (this.itsPool) {
                size = this.itsPool.size();
                if (size > 0) {
                    byteBufferAllocate = (ByteBuffer) this.itsPool.remove(size - 1);
                    byteBufferAllocate.clear();
                }
            }
            if (size <= 0) {
                byteBufferAllocate = ByteBuffer.allocateDirect(this.itsByteBufferSize);
            }
            this.itsObjectCounter++;
        } else {
            byteBufferAllocate = ByteBuffer.allocate(i2);
        }
        return byteBufferAllocate;
    }

    @Override // com.sun.corba.se.pept.transport.ByteBufferPool
    public void releaseByteBuffer(ByteBuffer byteBuffer) {
        if (byteBuffer.isDirect()) {
            synchronized (this.itsPool) {
                boolean z2 = false;
                int iIdentityHashCode = 0;
                if (this.debug) {
                    for (int i2 = 0; i2 < this.itsPool.size() && !z2; i2++) {
                        if (byteBuffer == ((ByteBuffer) this.itsPool.get(i2))) {
                            z2 = true;
                            iIdentityHashCode = System.identityHashCode(byteBuffer);
                        }
                    }
                }
                if (!z2 || !this.debug) {
                    this.itsPool.add(byteBuffer);
                } else {
                    new Throwable(Thread.currentThread().getName() + ": Duplicate ByteBuffer reference (" + iIdentityHashCode + ")").printStackTrace(System.out);
                }
            }
            this.itsObjectCounter--;
        }
    }

    @Override // com.sun.corba.se.pept.transport.ByteBufferPool
    public int activeCount() {
        return this.itsObjectCounter;
    }
}
