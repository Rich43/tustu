package com.sun.corba.se.spi.ior;

import org.omg.CORBA_2_3.portable.InputStream;

/* loaded from: rt.jar:com/sun/corba/se/spi/ior/IdentifiableFactory.class */
public interface IdentifiableFactory {
    int getId();

    Identifiable create(InputStream inputStream);
}
