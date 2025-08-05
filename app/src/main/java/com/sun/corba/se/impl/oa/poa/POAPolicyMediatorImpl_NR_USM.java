package com.sun.corba.se.impl.oa.poa;

import com.sun.corba.se.impl.oa.NullServantImpl;
import com.sun.corba.se.spi.oa.OAInvocationInfo;
import org.omg.PortableServer.ForwardRequest;
import org.omg.PortableServer.POA;
import org.omg.PortableServer.POAPackage.NoServant;
import org.omg.PortableServer.POAPackage.ObjectAlreadyActive;
import org.omg.PortableServer.POAPackage.ObjectNotActive;
import org.omg.PortableServer.POAPackage.ServantAlreadyActive;
import org.omg.PortableServer.POAPackage.ServantNotActive;
import org.omg.PortableServer.POAPackage.WrongPolicy;
import org.omg.PortableServer.Servant;
import org.omg.PortableServer.ServantLocator;
import org.omg.PortableServer.ServantLocatorPackage.CookieHolder;
import org.omg.PortableServer.ServantManager;

/* loaded from: rt.jar:com/sun/corba/se/impl/oa/poa/POAPolicyMediatorImpl_NR_USM.class */
public class POAPolicyMediatorImpl_NR_USM extends POAPolicyMediatorBase {
    private ServantLocator locator;

    POAPolicyMediatorImpl_NR_USM(Policies policies, POAImpl pOAImpl) {
        super(policies, pOAImpl);
        if (policies.retainServants()) {
            throw pOAImpl.invocationWrapper().policyMediatorBadPolicyInFactory();
        }
        if (!policies.useServantManager()) {
            throw pOAImpl.invocationWrapper().policyMediatorBadPolicyInFactory();
        }
        this.locator = null;
    }

    @Override // com.sun.corba.se.impl.oa.poa.POAPolicyMediatorBase
    protected Object internalGetServant(byte[] bArr, String str) throws ForwardRequest {
        if (this.locator == null) {
            throw this.poa.invocationWrapper().poaNoServantManager();
        }
        CookieHolder cookieHolder = this.orb.peekInvocationInfo().getCookieHolder();
        try {
            this.poa.unlock();
            Object objPreinvoke = this.locator.preinvoke(bArr, this.poa, str, cookieHolder);
            if (objPreinvoke == null) {
                objPreinvoke = new NullServantImpl(this.poa.omgInvocationWrapper().nullServantReturned());
            } else {
                setDelegate((Servant) objPreinvoke, bArr);
            }
            return objPreinvoke;
        } finally {
            this.poa.lock();
        }
    }

    @Override // com.sun.corba.se.impl.oa.poa.POAPolicyMediator
    public void returnServant() {
        OAInvocationInfo oAInvocationInfoPeekInvocationInfo = this.orb.peekInvocationInfo();
        if (this.locator == null) {
            return;
        }
        try {
            this.poa.unlock();
            this.locator.postinvoke(oAInvocationInfoPeekInvocationInfo.id(), (POA) oAInvocationInfoPeekInvocationInfo.oa(), oAInvocationInfoPeekInvocationInfo.getOperation(), oAInvocationInfoPeekInvocationInfo.getCookieHolder().value, (Servant) oAInvocationInfoPeekInvocationInfo.getServantContainer());
        } finally {
            this.poa.lock();
        }
    }

    @Override // com.sun.corba.se.impl.oa.poa.POAPolicyMediator
    public void etherealizeAll() {
    }

    @Override // com.sun.corba.se.impl.oa.poa.POAPolicyMediator
    public void clearAOM() {
    }

    @Override // com.sun.corba.se.impl.oa.poa.POAPolicyMediator
    public ServantManager getServantManager() throws WrongPolicy {
        return this.locator;
    }

    @Override // com.sun.corba.se.impl.oa.poa.POAPolicyMediator
    public void setServantManager(ServantManager servantManager) throws WrongPolicy {
        if (this.locator != null) {
            throw this.poa.invocationWrapper().servantManagerAlreadySet();
        }
        if (servantManager instanceof ServantLocator) {
            this.locator = (ServantLocator) servantManager;
            return;
        }
        throw this.poa.invocationWrapper().servantManagerBadType();
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
        throw new WrongPolicy();
    }
}
