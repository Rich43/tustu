package com.sun.corba.se.spi.ior;

import com.sun.corba.se.spi.protocol.CorbaServerRequestDispatcher;
import org.omg.CORBA.ORB;

/* loaded from: rt.jar:com/sun/corba/se/spi/ior/ObjectKey.class */
public interface ObjectKey extends Writeable {
    ObjectId getId();

    ObjectKeyTemplate getTemplate();

    byte[] getBytes(ORB orb);

    CorbaServerRequestDispatcher getServerRequestDispatcher(com.sun.corba.se.spi.orb.ORB orb);
}
