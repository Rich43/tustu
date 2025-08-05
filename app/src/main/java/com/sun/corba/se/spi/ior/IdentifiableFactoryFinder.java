package com.sun.corba.se.spi.ior;

import org.omg.CORBA_2_3.portable.InputStream;

/* loaded from: rt.jar:com/sun/corba/se/spi/ior/IdentifiableFactoryFinder.class */
public interface IdentifiableFactoryFinder {
    Identifiable create(int i2, InputStream inputStream);

    void registerFactory(IdentifiableFactory identifiableFactory);
}
