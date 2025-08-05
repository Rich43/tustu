package com.sun.corba.se.spi.encoding;

import com.sun.corba.se.impl.encoding.BufferManagerWrite;
import com.sun.corba.se.impl.encoding.CDROutputStream;
import com.sun.corba.se.pept.encoding.OutputObject;
import com.sun.corba.se.spi.ior.iiop.GIOPVersion;
import com.sun.corba.se.spi.orb.ORB;
import com.sun.corba.se.spi.transport.CorbaConnection;
import java.io.IOException;

/* loaded from: rt.jar:com/sun/corba/se/spi/encoding/CorbaOutputObject.class */
public abstract class CorbaOutputObject extends CDROutputStream implements OutputObject {
    public abstract void writeTo(CorbaConnection corbaConnection) throws IOException;

    public CorbaOutputObject(ORB orb, GIOPVersion gIOPVersion, byte b2, boolean z2, BufferManagerWrite bufferManagerWrite, byte b3, boolean z3) {
        super(orb, gIOPVersion, b2, z2, bufferManagerWrite, b3, z3);
    }
}
