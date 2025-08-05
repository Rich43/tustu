package com.sun.corba.se.spi.legacy.connection;

/* loaded from: rt.jar:com/sun/corba/se/spi/legacy/connection/LegacyServerSocketManager.class */
public interface LegacyServerSocketManager {
    int legacyGetTransientServerPort(String str);

    int legacyGetPersistentServerPort(String str);

    int legacyGetTransientOrPersistentServerPort(String str);

    LegacyServerSocketEndPointInfo legacyGetEndpoint(String str);

    boolean legacyIsLocalServerPort(int i2);
}
