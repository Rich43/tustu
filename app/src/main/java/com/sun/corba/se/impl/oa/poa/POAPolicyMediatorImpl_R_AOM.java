package com.sun.corba.se.impl.oa.poa;

import com.sun.corba.se.impl.oa.NullServantImpl;
import org.omg.PortableServer.ForwardRequest;
import org.omg.PortableServer.POAPackage.NoServant;
import org.omg.PortableServer.POAPackage.ObjectNotActive;
import org.omg.PortableServer.POAPackage.WrongPolicy;
import org.omg.PortableServer.Servant;
import org.omg.PortableServer.ServantManager;

/* loaded from: rt.jar:com/sun/corba/se/impl/oa/poa/POAPolicyMediatorImpl_R_AOM.class */
public class POAPolicyMediatorImpl_R_AOM extends POAPolicyMediatorBase_R {
    POAPolicyMediatorImpl_R_AOM(Policies policies, POAImpl pOAImpl) {
        super(policies, pOAImpl);
        if (!policies.useActiveMapOnly()) {
            throw pOAImpl.invocationWrapper().policyMediatorBadPolicyInFactory();
        }
    }

    @Override // com.sun.corba.se.impl.oa.poa.POAPolicyMediatorBase
    protected Object internalGetServant(byte[] bArr, String str) throws ForwardRequest {
        Object objInternalIdToServant = internalIdToServant(bArr);
        if (objInternalIdToServant == null) {
            objInternalIdToServant = new NullServantImpl(this.poa.invocationWrapper().nullServant());
        }
        return objInternalIdToServant;
    }

    @Override // com.sun.corba.se.impl.oa.poa.POAPolicyMediator
    public void etherealizeAll() {
    }

    @Override // com.sun.corba.se.impl.oa.poa.POAPolicyMediator
    public ServantManager getServantManager() throws WrongPolicy {
        throw new WrongPolicy();
    }

    @Override // com.sun.corba.se.impl.oa.poa.POAPolicyMediator
    public void setServantManager(ServantManager servantManager) throws WrongPolicy {
        throw new WrongPolicy();
    }

    @Override // com.sun.corba.se.impl.oa.poa.POAPolicyMediator
    public Servant getDefaultServant() throws NoServant, WrongPolicy {
        throw new WrongPolicy();
    }

    @Override // com.sun.corba.se.impl.oa.poa.POAPolicyMediator
    public void setDefaultServant(Servant servant) throws WrongPolicy {
        throw new WrongPolicy();
    }

    @Override // com.sun.corba.se.impl.oa.poa.POAPolicyMediator
    public Servant idToServant(byte[] bArr) throws ObjectNotActive, WrongPolicy {
        Servant servantInternalIdToServant = internalIdToServant(bArr);
        if (servantInternalIdToServant == null) {
            throw new ObjectNotActive();
        }
        return servantInternalIdToServant;
    }
}
