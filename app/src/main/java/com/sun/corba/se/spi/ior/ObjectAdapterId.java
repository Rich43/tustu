package com.sun.corba.se.spi.ior;

import java.util.Iterator;

/* loaded from: rt.jar:com/sun/corba/se/spi/ior/ObjectAdapterId.class */
public interface ObjectAdapterId extends Writeable {
    int getNumLevels();

    Iterator iterator();

    String[] getAdapterName();
}
