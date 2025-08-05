package com.sun.corba.se.impl.encoding;

import com.sun.corba.se.impl.protocol.giopmsgheaders.MessageBase;
import com.sun.corba.se.pept.encoding.OutputObject;
import com.sun.corba.se.pept.transport.Connection;
import com.sun.corba.se.spi.orb.ORB;
import org.omg.CORBA.SystemException;

/* loaded from: rt.jar:com/sun/corba/se/impl/encoding/BufferManagerWriteStream.class */
public class BufferManagerWriteStream extends BufferManagerWrite {
    private int fragmentCount;

    BufferManagerWriteStream(ORB orb) {
        super(orb);
        this.fragmentCount = 0;
    }

    @Override // com.sun.corba.se.impl.encoding.BufferManagerWrite
    public boolean sentFragment() {
        return this.fragmentCount > 0;
    }

    @Override // com.sun.corba.se.impl.encoding.BufferManagerWrite
    public int getBufferSize() {
        return this.orb.getORBData().getGIOPFragmentSize();
    }

    @Override // com.sun.corba.se.impl.encoding.BufferManagerWrite
    public void overflow(ByteBufferWithInfo byteBufferWithInfo) {
        MessageBase.setFlag(byteBufferWithInfo.byteBuffer, 2);
        try {
            sendFragment(false);
            byteBufferWithInfo.position(0);
            byteBufferWithInfo.buflen = byteBufferWithInfo.byteBuffer.limit();
            byteBufferWithInfo.fragmented = true;
            ((CDROutputObject) this.outputObject).getMessageHeader().createFragmentMessage().write((CDROutputObject) this.outputObject);
        } catch (SystemException e2) {
            this.orb.getPIHandler().invokeClientPIEndingPoint(2, e2);
            throw e2;
        }
    }

    private void sendFragment(boolean z2) {
        Connection connection = ((OutputObject) this.outputObject).getMessageMediator().getConnection();
        connection.writeLock();
        try {
            connection.sendWithoutLock((OutputObject) this.outputObject);
            this.fragmentCount++;
        } finally {
            connection.writeUnlock();
        }
    }

    @Override // com.sun.corba.se.impl.encoding.BufferManagerWrite
    public void sendMessage() {
        sendFragment(true);
        this.sentFullMessage = true;
    }

    @Override // com.sun.corba.se.impl.encoding.BufferManagerWrite
    public void close() {
    }
}
