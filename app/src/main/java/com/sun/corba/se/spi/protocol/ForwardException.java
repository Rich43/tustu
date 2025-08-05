package com.sun.corba.se.spi.protocol;

import com.sun.corba.se.impl.orbutil.ORBUtility;
import com.sun.corba.se.spi.ior.IOR;
import com.sun.corba.se.spi.orb.ORB;
import org.omg.CORBA.BAD_PARAM;
import org.omg.CORBA.LocalObject;
import org.omg.CORBA.Object;

/* loaded from: rt.jar:com/sun/corba/se/spi/protocol/ForwardException.class */
public class ForwardException extends RuntimeException {
    private ORB orb;
    private Object obj;
    private IOR ior;

    public ForwardException(ORB orb, IOR ior) {
        this.orb = orb;
        this.obj = null;
        this.ior = ior;
    }

    public ForwardException(ORB orb, Object object) {
        if (object instanceof LocalObject) {
            throw new BAD_PARAM();
        }
        this.orb = orb;
        this.obj = object;
        this.ior = null;
    }

    public synchronized Object getObject() {
        if (this.obj == null) {
            this.obj = ORBUtility.makeObjectReference(this.ior);
        }
        return this.obj;
    }

    public synchronized IOR getIOR() {
        if (this.ior == null) {
            this.ior = ORBUtility.getIOR(this.obj);
        }
        return this.ior;
    }
}
