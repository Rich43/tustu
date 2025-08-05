package com.sun.corba.se.spi.copyobject;

/* loaded from: rt.jar:com/sun/corba/se/spi/copyobject/CopierManager.class */
public interface CopierManager {
    void setDefaultId(int i2);

    int getDefaultId();

    ObjectCopierFactory getObjectCopierFactory(int i2);

    ObjectCopierFactory getDefaultObjectCopierFactory();

    void registerObjectCopierFactory(ObjectCopierFactory objectCopierFactory, int i2);
}
