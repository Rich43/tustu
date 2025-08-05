package com.sun.corba.se.impl.encoding;

import com.sun.corba.se.impl.orbutil.ORBUtility;
import com.sun.corba.se.pept.encoding.OutputObject;
import com.sun.corba.se.pept.transport.Connection;
import com.sun.corba.se.spi.orb.ORB;
import com.sun.corba.se.spi.orb.ORBData;

/* loaded from: rt.jar:com/sun/corba/se/impl/encoding/BufferManagerWriteGrow.class */
public class BufferManagerWriteGrow extends BufferManagerWrite {
    BufferManagerWriteGrow(ORB orb) {
        super(orb);
    }

    @Override // com.sun.corba.se.impl.encoding.BufferManagerWrite
    public boolean sentFragment() {
        return false;
    }

    @Override // com.sun.corba.se.impl.encoding.BufferManagerWrite
    public int getBufferSize() {
        int gIOPBufferSize = 1024;
        if (this.orb != null) {
            ORBData oRBData = this.orb.getORBData();
            if (oRBData != null) {
                gIOPBufferSize = oRBData.getGIOPBufferSize();
                dprint("BufferManagerWriteGrow.getBufferSize: bufferSize == " + gIOPBufferSize);
            } else {
                dprint("BufferManagerWriteGrow.getBufferSize: orbData reference is NULL");
            }
        } else {
            dprint("BufferManagerWriteGrow.getBufferSize: orb reference is NULL");
        }
        return gIOPBufferSize;
    }

    @Override // com.sun.corba.se.impl.encoding.BufferManagerWrite
    public void overflow(ByteBufferWithInfo byteBufferWithInfo) {
        byteBufferWithInfo.growBuffer(this.orb);
        byteBufferWithInfo.fragmented = false;
    }

    @Override // com.sun.corba.se.impl.encoding.BufferManagerWrite
    public void sendMessage() {
        Connection connection = ((OutputObject) this.outputObject).getMessageMediator().getConnection();
        connection.writeLock();
        try {
            connection.sendWithoutLock((OutputObject) this.outputObject);
            this.sentFullMessage = true;
        } finally {
            connection.writeUnlock();
        }
    }

    @Override // com.sun.corba.se.impl.encoding.BufferManagerWrite
    public void close() {
    }

    private void dprint(String str) {
        if (this.orb.transportDebugFlag) {
            ORBUtility.dprint(this, str);
        }
    }
}
