package com.sun.corba.se.spi.ior;

import java.util.Iterator;
import java.util.List;

/* loaded from: rt.jar:com/sun/corba/se/spi/ior/IORTemplate.class */
public interface IORTemplate extends List, IORFactory, MakeImmutable {
    Iterator iteratorById(int i2);

    ObjectKeyTemplate getObjectKeyTemplate();
}
