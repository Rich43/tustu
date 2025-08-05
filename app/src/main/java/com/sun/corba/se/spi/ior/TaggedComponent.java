package com.sun.corba.se.spi.ior;

import org.omg.CORBA.ORB;

/* loaded from: rt.jar:com/sun/corba/se/spi/ior/TaggedComponent.class */
public interface TaggedComponent extends Identifiable {
    org.omg.IOP.TaggedComponent getIOPComponent(ORB orb);
}
