package com.sun.corba.se.spi.ior;

import org.omg.CORBA.ORB;

/* loaded from: rt.jar:com/sun/corba/se/spi/ior/TaggedComponentFactoryFinder.class */
public interface TaggedComponentFactoryFinder extends IdentifiableFactoryFinder {
    TaggedComponent create(ORB orb, org.omg.IOP.TaggedComponent taggedComponent);
}
