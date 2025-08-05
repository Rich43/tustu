package com.sun.corba.se.spi.legacy.connection;

/* loaded from: rt.jar:com/sun/corba/se/spi/legacy/connection/LegacyServerSocketEndPointInfo.class */
public interface LegacyServerSocketEndPointInfo {
    public static final String DEFAULT_ENDPOINT = "DEFAULT_ENDPOINT";
    public static final String BOOT_NAMING = "BOOT_NAMING";
    public static final String NO_NAME = "NO_NAME";

    String getType();

    String getHostName();

    int getPort();

    int getLocatorPort();

    void setLocatorPort(int i2);

    String getName();
}
