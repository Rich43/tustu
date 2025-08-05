package com.sun.corba.se.impl.protocol;

import com.sun.corba.se.spi.protocol.LocalClientRequestDispatcher;
import org.omg.CORBA.Object;
import org.omg.CORBA.portable.ServantObject;

/* loaded from: rt.jar:com/sun/corba/se/impl/protocol/NotLocalLocalCRDImpl.class */
public class NotLocalLocalCRDImpl implements LocalClientRequestDispatcher {
    @Override // com.sun.corba.se.spi.protocol.LocalClientRequestDispatcher
    public boolean useLocalInvocation(Object object) {
        return false;
    }

    @Override // com.sun.corba.se.spi.protocol.LocalClientRequestDispatcher
    public boolean is_local(Object object) {
        return false;
    }

    @Override // com.sun.corba.se.spi.protocol.LocalClientRequestDispatcher
    public ServantObject servant_preinvoke(Object object, String str, Class cls) {
        return null;
    }

    @Override // com.sun.corba.se.spi.protocol.LocalClientRequestDispatcher
    public void servant_postinvoke(Object object, ServantObject servantObject) {
    }
}
