package com.sun.corba.se.impl.protocol;

import com.sun.corba.se.impl.logging.POASystemException;
import com.sun.corba.se.spi.ior.IOR;
import com.sun.corba.se.spi.logging.CORBALogDomains;
import com.sun.corba.se.spi.oa.OADestroyed;
import com.sun.corba.se.spi.oa.OAInvocationInfo;
import com.sun.corba.se.spi.oa.ObjectAdapter;
import com.sun.corba.se.spi.orb.ORB;
import com.sun.corba.se.spi.protocol.ForwardException;

/* loaded from: rt.jar:com/sun/corba/se/impl/protocol/ServantCacheLocalCRDBase.class */
public abstract class ServantCacheLocalCRDBase extends LocalClientRequestDispatcherBase {
    private OAInvocationInfo cachedInfo;
    protected POASystemException wrapper;

    protected ServantCacheLocalCRDBase(ORB orb, int i2, IOR ior) {
        super(orb, i2, ior);
        this.wrapper = POASystemException.get(orb, CORBALogDomains.RPC_PROTOCOL);
    }

    protected synchronized OAInvocationInfo getCachedInfo() {
        if (!this.servantIsLocal) {
            throw this.wrapper.servantMustBeLocal();
        }
        if (this.cachedInfo == null) {
            ObjectAdapter objectAdapterFind = this.oaf.find(this.oaid);
            this.cachedInfo = objectAdapterFind.makeInvocationInfo(this.objectId);
            this.orb.pushInvocationInfo(this.cachedInfo);
            try {
                try {
                    objectAdapterFind.enter();
                    objectAdapterFind.getInvocationServant(this.cachedInfo);
                    objectAdapterFind.returnServant();
                    objectAdapterFind.exit();
                    this.orb.popInvocationInfo();
                } catch (OADestroyed e2) {
                    throw this.wrapper.adapterDestroyed(e2);
                } catch (ForwardException e3) {
                    throw this.wrapper.illegalForwardRequest(e3);
                }
            } catch (Throwable th) {
                objectAdapterFind.returnServant();
                objectAdapterFind.exit();
                this.orb.popInvocationInfo();
                throw th;
            }
        }
        return this.cachedInfo;
    }
}
