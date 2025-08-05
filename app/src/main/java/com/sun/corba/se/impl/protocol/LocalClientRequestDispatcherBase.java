package com.sun.corba.se.impl.protocol;

import com.sun.corba.se.spi.ior.IOR;
import com.sun.corba.se.spi.ior.ObjectAdapterId;
import com.sun.corba.se.spi.ior.ObjectKeyTemplate;
import com.sun.corba.se.spi.ior.iiop.IIOPProfile;
import com.sun.corba.se.spi.oa.ObjectAdapterFactory;
import com.sun.corba.se.spi.orb.ORB;
import com.sun.corba.se.spi.protocol.LocalClientRequestDispatcher;
import org.omg.CORBA.Object;
import org.omg.CORBA.portable.ServantObject;

/* loaded from: rt.jar:com/sun/corba/se/impl/protocol/LocalClientRequestDispatcherBase.class */
public abstract class LocalClientRequestDispatcherBase implements LocalClientRequestDispatcher {
    protected ORB orb;
    int scid;
    protected boolean servantIsLocal;
    protected ObjectAdapterFactory oaf;
    protected ObjectAdapterId oaid;
    protected byte[] objectId;
    private static final ThreadLocal isNextCallValid = new ThreadLocal() { // from class: com.sun.corba.se.impl.protocol.LocalClientRequestDispatcherBase.1
        @Override // java.lang.ThreadLocal
        protected synchronized Object initialValue() {
            return Boolean.TRUE;
        }
    };

    protected LocalClientRequestDispatcherBase(ORB orb, int i2, IOR ior) {
        this.orb = orb;
        IIOPProfile profile = ior.getProfile();
        this.servantIsLocal = orb.getORBData().isLocalOptimizationAllowed() && profile.isLocal();
        ObjectKeyTemplate objectKeyTemplate = profile.getObjectKeyTemplate();
        this.scid = objectKeyTemplate.getSubcontractId();
        this.oaf = orb.getRequestDispatcherRegistry().getObjectAdapterFactory(i2);
        this.oaid = objectKeyTemplate.getObjectAdapterId();
        this.objectId = profile.getObjectId().getId();
    }

    public byte[] getObjectId() {
        return this.objectId;
    }

    @Override // com.sun.corba.se.spi.protocol.LocalClientRequestDispatcher
    public boolean is_local(Object object) {
        return false;
    }

    @Override // com.sun.corba.se.spi.protocol.LocalClientRequestDispatcher
    public boolean useLocalInvocation(Object object) {
        if (isNextCallValid.get() == Boolean.TRUE) {
            return this.servantIsLocal;
        }
        isNextCallValid.set(Boolean.TRUE);
        return false;
    }

    protected boolean checkForCompatibleServant(ServantObject servantObject, Class cls) {
        if (servantObject == null) {
            return false;
        }
        if (!cls.isInstance(servantObject.servant)) {
            isNextCallValid.set(Boolean.FALSE);
            return false;
        }
        return true;
    }
}
