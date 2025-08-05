package com.sun.corba.se.spi.ior;

import com.sun.corba.se.spi.ior.iiop.IIOPProfile;
import com.sun.corba.se.spi.orb.ORB;
import java.util.Iterator;
import java.util.List;

/* loaded from: rt.jar:com/sun/corba/se/spi/ior/IOR.class */
public interface IOR extends List, Writeable, MakeImmutable {
    ORB getORB();

    String getTypeId();

    Iterator iteratorById(int i2);

    String stringify();

    org.omg.IOP.IOR getIOPIOR();

    boolean isNil();

    boolean isEquivalent(IOR ior);

    IORTemplateList getIORTemplates();

    IIOPProfile getProfile();
}
