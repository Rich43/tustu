package com.sun.corba.se.impl.oa.toa;

import com.sun.corba.se.spi.oa.ObjectAdapter;
import org.omg.CORBA.Object;

/* loaded from: rt.jar:com/sun/corba/se/impl/oa/toa/TOA.class */
public interface TOA extends ObjectAdapter {
    void connect(Object object);

    void disconnect(Object object);
}
