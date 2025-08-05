package com.sun.corba.se.impl.oa.toa;

import com.sun.corba.se.impl.ior.ObjectKeyTemplateBase;
import com.sun.corba.se.impl.javax.rmi.CORBA.Util;
import com.sun.corba.se.impl.logging.ORBUtilSystemException;
import com.sun.corba.se.spi.ior.ObjectAdapterId;
import com.sun.corba.se.spi.logging.CORBALogDomains;
import com.sun.corba.se.spi.oa.ObjectAdapter;
import com.sun.corba.se.spi.oa.ObjectAdapterFactory;
import com.sun.corba.se.spi.orb.ORB;
import java.util.HashMap;
import java.util.Map;

/* loaded from: rt.jar:com/sun/corba/se/impl/oa/toa/TOAFactory.class */
public class TOAFactory implements ObjectAdapterFactory {
    private ORB orb;
    private ORBUtilSystemException wrapper;
    private TOAImpl toa;
    private Map codebaseToTOA;
    private TransientObjectManager tom;

    @Override // com.sun.corba.se.spi.oa.ObjectAdapterFactory
    public ObjectAdapter find(ObjectAdapterId objectAdapterId) {
        if (objectAdapterId.equals(ObjectKeyTemplateBase.JIDL_OAID)) {
            return getTOA();
        }
        throw this.wrapper.badToaOaid();
    }

    @Override // com.sun.corba.se.spi.oa.ObjectAdapterFactory
    public void init(ORB orb) {
        this.orb = orb;
        this.wrapper = ORBUtilSystemException.get(orb, CORBALogDomains.OA_LIFECYCLE);
        this.tom = new TransientObjectManager(orb);
        this.codebaseToTOA = new HashMap();
    }

    @Override // com.sun.corba.se.spi.oa.ObjectAdapterFactory
    public void shutdown(boolean z2) {
        if (Util.isInstanceDefined()) {
            Util.getInstance().unregisterTargetsForORB(this.orb);
        }
    }

    public synchronized TOA getTOA(String str) {
        TOA tOAImpl = (TOA) this.codebaseToTOA.get(str);
        if (tOAImpl == null) {
            tOAImpl = new TOAImpl(this.orb, this.tom, str);
            this.codebaseToTOA.put(str, tOAImpl);
        }
        return tOAImpl;
    }

    public synchronized TOA getTOA() {
        if (this.toa == null) {
            this.toa = new TOAImpl(this.orb, this.tom, null);
        }
        return this.toa;
    }

    @Override // com.sun.corba.se.spi.oa.ObjectAdapterFactory
    public ORB getORB() {
        return this.orb;
    }
}
