package com.sun.corba.se.impl.encoding;

import com.sun.corba.se.impl.orbutil.ORBUtility;
import com.sun.corba.se.pept.transport.ByteBufferPool;
import com.sun.corba.se.spi.orb.ORB;
import java.nio.ByteBuffer;
import javafx.fxml.FXMLLoader;

/* loaded from: rt.jar:com/sun/corba/se/impl/encoding/ByteBufferWithInfo.class */
public class ByteBufferWithInfo {
    private ORB orb;
    private boolean debug;
    private int index;
    public ByteBuffer byteBuffer;
    public int buflen;
    public int needed;
    public boolean fragmented;

    public ByteBufferWithInfo(org.omg.CORBA.ORB orb, ByteBuffer byteBuffer, int i2) {
        this.orb = (ORB) orb;
        this.debug = this.orb.transportDebugFlag;
        this.byteBuffer = byteBuffer;
        if (byteBuffer != null) {
            this.buflen = byteBuffer.limit();
        }
        position(i2);
        this.needed = 0;
        this.fragmented = false;
    }

    public ByteBufferWithInfo(org.omg.CORBA.ORB orb, ByteBuffer byteBuffer) {
        this(orb, byteBuffer, 0);
    }

    public ByteBufferWithInfo(org.omg.CORBA.ORB orb, BufferManagerWrite bufferManagerWrite) {
        this(orb, bufferManagerWrite, true);
    }

    public ByteBufferWithInfo(org.omg.CORBA.ORB orb, BufferManagerWrite bufferManagerWrite, boolean z2) {
        this.orb = (ORB) orb;
        this.debug = this.orb.transportDebugFlag;
        int bufferSize = bufferManagerWrite.getBufferSize();
        if (z2) {
            this.byteBuffer = this.orb.getByteBufferPool().getByteBuffer(bufferSize);
            if (this.debug) {
                int iIdentityHashCode = System.identityHashCode(this.byteBuffer);
                StringBuffer stringBuffer = new StringBuffer(80);
                stringBuffer.append("constructor (ORB, BufferManagerWrite) - got ").append("ByteBuffer id (").append(iIdentityHashCode).append(") from ByteBufferPool.");
                dprint(stringBuffer.toString());
            }
        } else {
            this.byteBuffer = ByteBuffer.allocate(bufferSize);
        }
        position(0);
        this.buflen = bufferSize;
        this.byteBuffer.limit(this.buflen);
        this.needed = 0;
        this.fragmented = false;
    }

    public ByteBufferWithInfo(ByteBufferWithInfo byteBufferWithInfo) {
        this.orb = byteBufferWithInfo.orb;
        this.debug = byteBufferWithInfo.debug;
        this.byteBuffer = byteBufferWithInfo.byteBuffer;
        this.buflen = byteBufferWithInfo.buflen;
        this.byteBuffer.limit(this.buflen);
        position(byteBufferWithInfo.position());
        this.needed = byteBufferWithInfo.needed;
        this.fragmented = byteBufferWithInfo.fragmented;
    }

    public int getSize() {
        return position();
    }

    public int getLength() {
        return this.buflen;
    }

    public int position() {
        return this.index;
    }

    public void position(int i2) {
        this.byteBuffer.position(i2);
        this.index = i2;
    }

    public void setLength(int i2) {
        this.buflen = i2;
        this.byteBuffer.limit(this.buflen);
    }

    public void growBuffer(ORB orb) {
        int i2;
        int iLimit = this.byteBuffer.limit();
        while (true) {
            i2 = iLimit * 2;
            if (position() + this.needed < i2) {
                break;
            } else {
                iLimit = i2;
            }
        }
        ByteBufferPool byteBufferPool = orb.getByteBufferPool();
        ByteBuffer byteBuffer = byteBufferPool.getByteBuffer(i2);
        if (this.debug) {
            int iIdentityHashCode = System.identityHashCode(byteBuffer);
            StringBuffer stringBuffer = new StringBuffer(80);
            stringBuffer.append("growBuffer() - got ByteBuffer id (");
            stringBuffer.append(iIdentityHashCode).append(") from ByteBufferPool.");
            dprint(stringBuffer.toString());
        }
        this.byteBuffer.position(0);
        byteBuffer.put(this.byteBuffer);
        if (this.debug) {
            int iIdentityHashCode2 = System.identityHashCode(this.byteBuffer);
            StringBuffer stringBuffer2 = new StringBuffer(80);
            stringBuffer2.append("growBuffer() - releasing ByteBuffer id (");
            stringBuffer2.append(iIdentityHashCode2).append(") to ByteBufferPool.");
            dprint(stringBuffer2.toString());
        }
        byteBufferPool.releaseByteBuffer(this.byteBuffer);
        this.byteBuffer = byteBuffer;
        this.buflen = i2;
        this.byteBuffer.limit(this.buflen);
    }

    public String toString() {
        StringBuffer stringBuffer = new StringBuffer("ByteBufferWithInfo:");
        stringBuffer.append(" buflen = " + this.buflen);
        stringBuffer.append(" byteBuffer.limit = " + this.byteBuffer.limit());
        stringBuffer.append(" index = " + this.index);
        stringBuffer.append(" position = " + position());
        stringBuffer.append(" needed = " + this.needed);
        stringBuffer.append(" byteBuffer = " + (this.byteBuffer == null ? FXMLLoader.NULL_KEYWORD : "not null"));
        stringBuffer.append(" fragmented = " + this.fragmented);
        return stringBuffer.toString();
    }

    protected void dprint(String str) {
        ORBUtility.dprint("ByteBufferWithInfo", str);
    }
}
