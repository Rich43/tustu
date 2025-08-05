package com.sun.corba.se.impl.encoding;

import com.sun.corba.se.impl.logging.ORBUtilSystemException;
import com.sun.corba.se.spi.logging.CORBALogDomains;
import com.sun.corba.se.spi.orb.ORB;

/* loaded from: rt.jar:com/sun/corba/se/impl/encoding/BufferManagerWrite.class */
public abstract class BufferManagerWrite {
    protected ORB orb;
    protected ORBUtilSystemException wrapper;
    protected Object outputObject;
    protected boolean sentFullMessage = false;

    public abstract boolean sentFragment();

    public abstract int getBufferSize();

    public abstract void overflow(ByteBufferWithInfo byteBufferWithInfo);

    public abstract void sendMessage();

    public abstract void close();

    BufferManagerWrite(ORB orb) {
        this.orb = orb;
        this.wrapper = ORBUtilSystemException.get(orb, CORBALogDomains.RPC_ENCODING);
    }

    public boolean sentFullMessage() {
        return this.sentFullMessage;
    }

    public void setOutputObject(Object obj) {
        this.outputObject = obj;
    }
}
