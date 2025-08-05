package com.sun.corba.se.spi.ior;

import com.sun.corba.se.spi.orb.ORB;
import com.sun.corba.se.spi.orb.ORBVersion;
import com.sun.corba.se.spi.protocol.CorbaServerRequestDispatcher;
import org.omg.CORBA_2_3.portable.OutputStream;

/* loaded from: rt.jar:com/sun/corba/se/spi/ior/ObjectKeyTemplate.class */
public interface ObjectKeyTemplate extends Writeable {
    ORBVersion getORBVersion();

    int getSubcontractId();

    int getServerId();

    String getORBId();

    ObjectAdapterId getObjectAdapterId();

    byte[] getAdapterId();

    void write(ObjectId objectId, OutputStream outputStream);

    CorbaServerRequestDispatcher getServerRequestDispatcher(ORB orb, ObjectId objectId);
}
