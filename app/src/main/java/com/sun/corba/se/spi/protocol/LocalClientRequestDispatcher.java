package com.sun.corba.se.spi.protocol;

import org.omg.CORBA.Object;
import org.omg.CORBA.portable.ServantObject;

/* loaded from: rt.jar:com/sun/corba/se/spi/protocol/LocalClientRequestDispatcher.class */
public interface LocalClientRequestDispatcher {
    boolean useLocalInvocation(Object object);

    boolean is_local(Object object);

    ServantObject servant_preinvoke(Object object, String str, Class cls);

    void servant_postinvoke(Object object, ServantObject servantObject);
}
