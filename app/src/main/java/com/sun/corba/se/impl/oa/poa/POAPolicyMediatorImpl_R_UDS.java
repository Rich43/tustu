package com.sun.corba.se.impl.oa.poa;

import com.sun.corba.se.impl.oa.poa.ActiveObjectMap;
import org.omg.PortableServer.ForwardRequest;
import org.omg.PortableServer.POAPackage.NoServant;
import org.omg.PortableServer.POAPackage.ObjectNotActive;
import org.omg.PortableServer.POAPackage.WrongPolicy;
import org.omg.PortableServer.Servant;
import org.omg.PortableServer.ServantManager;

/* loaded from: rt.jar:com/sun/corba/se/impl/oa/poa/POAPolicyMediatorImpl_R_UDS.class */
public class POAPolicyMediatorImpl_R_UDS extends POAPolicyMediatorBase_R {
    private Servant defaultServant;

    POAPolicyMediatorImpl_R_UDS(Policies policies, POAImpl pOAImpl) {
        super(policies, pOAImpl);
        this.defaultServant = null;
        if (!policies.useDefaultServant()) {
            throw pOAImpl.invocationWrapper().policyMediatorBadPolicyInFactory();
        }
    }

    @Override // com.sun.corba.se.impl.oa.poa.POAPolicyMediatorBase
    protected Object internalGetServant(byte[] bArr, String str) throws ForwardRequest {
        Servant servantInternalIdToServant = internalIdToServant(bArr);
        if (servantInternalIdToServant == null) {
            servantInternalIdToServant = this.defaultServant;
        }
        if (servantInternalIdToServant == null) {
            throw this.poa.invocationWrapper().poaNoDefaultServant();
        }
        return servantInternalIdToServant;
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
        if (this.defaultServant == null) {
            throw new NoServant();
        }
        return this.defaultServant;
    }

    @Override // com.sun.corba.se.impl.oa.poa.POAPolicyMediator
    public void setDefaultServant(Servant servant) throws WrongPolicy {
        this.defaultServant = servant;
        setDelegate(this.defaultServant, "DefaultServant".getBytes());
    }

    @Override // com.sun.corba.se.impl.oa.poa.POAPolicyMediator
    public Servant idToServant(byte[] bArr) throws ObjectNotActive, WrongPolicy {
        Servant servantInternalKeyToServant = internalKeyToServant(new ActiveObjectMap.Key(bArr));
        if (servantInternalKeyToServant == null && this.defaultServant != null) {
            servantInternalKeyToServant = this.defaultServant;
        }
        if (servantInternalKeyToServant == null) {
            throw new ObjectNotActive();
        }
        return servantInternalKeyToServant;
    }
}
