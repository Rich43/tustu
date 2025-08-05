package com.sun.corba.se.spi.oa;

import com.sun.corba.se.spi.ior.IORTemplate;
import com.sun.corba.se.spi.orb.ORB;
import org.omg.CORBA.Object;
import org.omg.CORBA.Policy;
import org.omg.PortableInterceptor.ObjectReferenceFactory;
import org.omg.PortableInterceptor.ObjectReferenceTemplate;

/* loaded from: rt.jar:com/sun/corba/se/spi/oa/ObjectAdapter.class */
public interface ObjectAdapter {
    ORB getORB();

    Policy getEffectivePolicy(int i2);

    IORTemplate getIORTemplate();

    int getManagerId();

    short getState();

    ObjectReferenceTemplate getAdapterTemplate();

    ObjectReferenceFactory getCurrentFactory();

    void setCurrentFactory(ObjectReferenceFactory objectReferenceFactory);

    Object getLocalServant(byte[] bArr);

    void getInvocationServant(OAInvocationInfo oAInvocationInfo);

    void enter() throws OADestroyed;

    void exit();

    void returnServant();

    OAInvocationInfo makeInvocationInfo(byte[] bArr);

    String[] getInterfaces(Object obj, byte[] bArr);
}
