package com.sun.corba.se.impl.protocol;

import com.sun.corba.se.spi.ior.IOR;
import com.sun.corba.se.spi.oa.OAInvocationInfo;
import com.sun.corba.se.spi.orb.ORB;
import org.omg.CORBA.Object;
import org.omg.CORBA.portable.ServantObject;

/* loaded from: rt.jar:com/sun/corba/se/impl/protocol/InfoOnlyServantCacheLocalCRDImpl.class */
public class InfoOnlyServantCacheLocalCRDImpl extends ServantCacheLocalCRDBase {
    public InfoOnlyServantCacheLocalCRDImpl(ORB orb, int i2, IOR ior) {
        super(orb, i2, ior);
    }

    @Override // com.sun.corba.se.spi.protocol.LocalClientRequestDispatcher
    public ServantObject servant_preinvoke(Object object, String str, Class cls) {
        OAInvocationInfo cachedInfo = getCachedInfo();
        if (!checkForCompatibleServant(cachedInfo, cls)) {
            return null;
        }
        OAInvocationInfo oAInvocationInfo = new OAInvocationInfo(cachedInfo, str);
        this.orb.pushInvocationInfo(oAInvocationInfo);
        return oAInvocationInfo;
    }

    @Override // com.sun.corba.se.spi.protocol.LocalClientRequestDispatcher
    public void servant_postinvoke(Object object, ServantObject servantObject) {
        this.orb.popInvocationInfo();
    }
}
