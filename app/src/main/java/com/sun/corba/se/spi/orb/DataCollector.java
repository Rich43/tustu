package com.sun.corba.se.spi.orb;

import java.util.Properties;

/* loaded from: rt.jar:com/sun/corba/se/spi/orb/DataCollector.class */
public interface DataCollector {
    boolean isApplet();

    boolean initialHostIsLocal();

    void setParser(PropertyParser propertyParser);

    Properties getProperties();
}
