package com.sun.corba.se.spi.resolver;

import java.util.Set;
import org.omg.CORBA.Object;

/* loaded from: rt.jar:com/sun/corba/se/spi/resolver/Resolver.class */
public interface Resolver {
    Object resolve(String str);

    Set list();
}
