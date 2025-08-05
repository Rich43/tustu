package com.sun.corba.se.impl.encoding;

import com.sun.corba.se.impl.logging.ORBUtilSystemException;
import com.sun.corba.se.spi.ior.iiop.GIOPVersion;
import com.sun.corba.se.spi.logging.CORBALogDomains;
import com.sun.corba.se.spi.orb.ORB;
import org.omg.CORBA.INTERNAL;

/* loaded from: rt.jar:com/sun/corba/se/impl/encoding/BufferManagerFactory.class */
public class BufferManagerFactory {
    public static final int GROW = 0;
    public static final int COLLECT = 1;
    public static final int STREAM = 2;

    public static BufferManagerRead newBufferManagerRead(GIOPVersion gIOPVersion, byte b2, ORB orb) {
        if (b2 != 0) {
            return new BufferManagerReadGrow(orb);
        }
        switch (gIOPVersion.intValue()) {
            case 256:
                return new BufferManagerReadGrow(orb);
            case 257:
            case 258:
                return new BufferManagerReadStream(orb);
            default:
                throw new INTERNAL("Unknown GIOP version: " + ((Object) gIOPVersion));
        }
    }

    public static BufferManagerRead newBufferManagerRead(int i2, byte b2, ORB orb) {
        if (b2 != 0) {
            if (i2 != 0) {
                throw ORBUtilSystemException.get(orb, CORBALogDomains.RPC_ENCODING).invalidBuffMgrStrategy("newBufferManagerRead");
            }
            return new BufferManagerReadGrow(orb);
        }
        switch (i2) {
            case 0:
                return new BufferManagerReadGrow(orb);
            case 1:
                throw new INTERNAL("Collect strategy invalid for reading");
            case 2:
                return new BufferManagerReadStream(orb);
            default:
                throw new INTERNAL("Unknown buffer manager read strategy: " + i2);
        }
    }

    public static BufferManagerWrite newBufferManagerWrite(int i2, byte b2, ORB orb) {
        if (b2 != 0) {
            if (i2 != 0) {
                throw ORBUtilSystemException.get(orb, CORBALogDomains.RPC_ENCODING).invalidBuffMgrStrategy("newBufferManagerWrite");
            }
            return new BufferManagerWriteGrow(orb);
        }
        switch (i2) {
            case 0:
                return new BufferManagerWriteGrow(orb);
            case 1:
                return new BufferManagerWriteCollect(orb);
            case 2:
                return new BufferManagerWriteStream(orb);
            default:
                throw new INTERNAL("Unknown buffer manager write strategy: " + i2);
        }
    }

    public static BufferManagerWrite newBufferManagerWrite(GIOPVersion gIOPVersion, byte b2, ORB orb) {
        if (b2 != 0) {
            return new BufferManagerWriteGrow(orb);
        }
        return newBufferManagerWrite(orb.getORBData().getGIOPBuffMgrStrategy(gIOPVersion), b2, orb);
    }

    public static BufferManagerRead defaultBufferManagerRead(ORB orb) {
        return new BufferManagerReadGrow(orb);
    }
}
