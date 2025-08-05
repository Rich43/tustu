package com.sun.corba.se.impl.oa.poa;

import org.omg.PortableServer.ForwardRequest;
import org.omg.PortableServer.POAPackage.NoServant;
import org.omg.PortableServer.POAPackage.ObjectAlreadyActive;
import org.omg.PortableServer.POAPackage.ObjectNotActive;
import org.omg.PortableServer.POAPackage.ServantAlreadyActive;
import org.omg.PortableServer.POAPackage.ServantNotActive;
import org.omg.PortableServer.POAPackage.WrongPolicy;
import org.omg.PortableServer.Servant;
import org.omg.PortableServer.ServantManager;

/* loaded from: rt.jar:com/sun/corba/se/impl/oa/poa/POAPolicyMediatorImpl_NR_UDS.class */
public class POAPolicyMediatorImpl_NR_UDS extends POAPolicyMediatorBase {
    private Servant defaultServant;

    POAPolicyMediatorImpl_NR_UDS(Policies policies, POAImpl pOAImpl) {
        super(policies, pOAImpl);
        if (policies.retainServants()) {
            throw pOAImpl.invocationWrapper().policyMediatorBadPolicyInFactory();
        }
        if (!policies.useDefaultServant()) {
            throw pOAImpl.invocationWrapper().policyMediatorBadPolicyInFactory();
        }
        this.defaultServant = null;
    }

    @Override // com.sun.corba.se.impl.oa.poa.POAPolicyMediatorBase
    protected Object internalGetServant(byte[] bArr, String str) throws ForwardRequest {
        if (this.defaultServant == null) {
            throw this.poa.invocationWrapper().poaNoDefaultServant();
        }
        return this.defaultServant;
    }

    @Override // com.sun.corba.se.impl.oa.poa.POAPolicyMediator
    public void returnServant() {
    }

    @Override // com.sun.corba.se.impl.oa.poa.POAPolicyMediator
    public void etherealizeAll() {
    }

    @Override // com.sun.corba.se.impl.oa.poa.POAPolicyMediator
    public void clearAOM() {
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
    public final void activateObject(byte[] bArr, Servant servant) throws ObjectAlreadyActive, ServantAlreadyActive, WrongPolicy {
        throw new WrongPolicy();
    }

    @Override // com.sun.corba.se.impl.oa.poa.POAPolicyMediator
    public Servant deactivateObject(byte[] bArr) throws ObjectNotActive, WrongPolicy {
        throw new WrongPolicy();
    }

    @Override // com.sun.corba.se.impl.oa.poa.POAPolicyMediator
    public byte[] servantToId(Servant servant) throws ServantNotActive, WrongPolicy {
        throw new WrongPolicy();
    }

    @Override // com.sun.corba.se.impl.oa.poa.POAPolicyMediator
    public Servant idToServant(byte[] bArr) throws ObjectNotActive, WrongPolicy {
        if (this.defaultServant != null) {
            return this.defaultServant;
        }
        throw new ObjectNotActive();
    }
}
