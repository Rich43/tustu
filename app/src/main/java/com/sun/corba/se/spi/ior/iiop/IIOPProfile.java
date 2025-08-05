package com.sun.corba.se.spi.ior.iiop;

import com.sun.corba.se.spi.ior.TaggedProfile;
import com.sun.corba.se.spi.orb.ORBVersion;

/* loaded from: rt.jar:com/sun/corba/se/spi/ior/iiop/IIOPProfile.class */
public interface IIOPProfile extends TaggedProfile {
    ORBVersion getORBVersion();

    Object getServant();

    GIOPVersion getGIOPVersion();

    String getCodebase();
}
