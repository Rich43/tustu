package com.sun.corba.se.spi.ior;

import com.sun.corba.se.spi.orb.ORB;

/* loaded from: rt.jar:com/sun/corba/se/spi/ior/IORFactory.class */
public interface IORFactory extends Writeable, MakeImmutable {
    IOR makeIOR(ORB orb, String str, ObjectId objectId);

    boolean isEquivalent(IORFactory iORFactory);
}
