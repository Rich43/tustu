package com.sun.corba.se.impl.oa.poa;

import com.sun.corba.se.impl.oa.NullServantImpl;
import com.sun.corba.se.impl.oa.poa.ActiveObjectMap;
import com.sun.corba.se.impl.orbutil.ORBUtility;
import com.sun.corba.se.spi.oa.NullServant;
import java.util.Set;
import org.omg.CORBA.SystemException;
import org.omg.PortableServer.ForwardRequest;
import org.omg.PortableServer.POAPackage.NoServant;
import org.omg.PortableServer.POAPackage.ObjectNotActive;
import org.omg.PortableServer.POAPackage.WrongPolicy;
import org.omg.PortableServer.Servant;
import org.omg.PortableServer.ServantActivator;
import org.omg.PortableServer.ServantManager;

/* loaded from: rt.jar:com/sun/corba/se/impl/oa/poa/POAPolicyMediatorImpl_R_USM.class */
public class POAPolicyMediatorImpl_R_USM extends POAPolicyMediatorBase_R {
    protected ServantActivator activator;

    POAPolicyMediatorImpl_R_USM(Policies policies, POAImpl pOAImpl) {
        super(policies, pOAImpl);
        this.activator = null;
        if (!policies.useServantManager()) {
            throw pOAImpl.invocationWrapper().policyMediatorBadPolicyInFactory();
        }
    }

    private AOMEntry enterEntry(ActiveObjectMap.Key key) {
        boolean z2;
        AOMEntry aOMEntry;
        do {
            z2 = false;
            aOMEntry = this.activeObjectMap.get(key);
            try {
                aOMEntry.enter();
            } catch (Exception e2) {
                z2 = true;
            }
        } while (z2);
        return aOMEntry;
    }

    @Override // com.sun.corba.se.impl.oa.poa.POAPolicyMediatorBase
    protected Object internalGetServant(byte[] bArr, String str) throws ForwardRequest {
        if (this.poa.getDebug()) {
            ORBUtility.dprint(this, "Calling POAPolicyMediatorImpl_R_USM.internalGetServant for poa " + ((Object) this.poa) + " operation=" + str);
        }
        try {
            ActiveObjectMap.Key key = new ActiveObjectMap.Key(bArr);
            AOMEntry aOMEntryEnterEntry = enterEntry(key);
            Servant servant = this.activeObjectMap.getServant(aOMEntryEnterEntry);
            if (servant != null) {
                if (this.poa.getDebug()) {
                    ORBUtility.dprint(this, "internalGetServant: servant already activated");
                }
                return servant;
            }
            try {
                if (this.activator == null) {
                    if (this.poa.getDebug()) {
                        ORBUtility.dprint(this, "internalGetServant: no servant activator in POA");
                    }
                    aOMEntryEnterEntry.incarnateFailure();
                    throw this.poa.invocationWrapper().poaNoServantManager();
                }
                try {
                    if (this.poa.getDebug()) {
                        ORBUtility.dprint(this, "internalGetServant: upcall to incarnate");
                    }
                    this.poa.unlock();
                    Object objIncarnate = this.activator.incarnate(bArr, this.poa);
                    if (objIncarnate == null) {
                        objIncarnate = new NullServantImpl(this.poa.omgInvocationWrapper().nullServantReturned());
                    }
                    this.poa.lock();
                    if (objIncarnate == null || (objIncarnate instanceof NullServant)) {
                        if (this.poa.getDebug()) {
                            ORBUtility.dprint(this, "internalGetServant: incarnate failed");
                        }
                        aOMEntryEnterEntry.incarnateFailure();
                    } else {
                        if (this.isUnique && this.activeObjectMap.contains((Servant) objIncarnate)) {
                            if (this.poa.getDebug()) {
                                ORBUtility.dprint(this, "internalGetServant: servant already assigned to ID");
                            }
                            aOMEntryEnterEntry.incarnateFailure();
                            throw this.poa.invocationWrapper().poaServantNotUnique();
                        }
                        if (this.poa.getDebug()) {
                            ORBUtility.dprint(this, "internalGetServant: incarnate complete");
                        }
                        aOMEntryEnterEntry.incarnateComplete();
                        activateServant(key, aOMEntryEnterEntry, (Servant) objIncarnate);
                    }
                    Object obj = objIncarnate;
                    if (this.poa.getDebug()) {
                        ORBUtility.dprint(this, "Exiting POAPolicyMediatorImpl_R_USM.internalGetServant for poa " + ((Object) this.poa));
                    }
                    return obj;
                } catch (SystemException e2) {
                    if (this.poa.getDebug()) {
                        ORBUtility.dprint(this, "internalGetServant: incarnate threw SystemException " + ((Object) e2));
                    }
                    throw e2;
                } catch (ForwardRequest e3) {
                    if (this.poa.getDebug()) {
                        ORBUtility.dprint(this, "internalGetServant: incarnate threw ForwardRequest");
                    }
                    throw e3;
                } catch (Throwable th) {
                    if (this.poa.getDebug()) {
                        ORBUtility.dprint(this, "internalGetServant: incarnate threw Throwable " + ((Object) th));
                    }
                    throw this.poa.invocationWrapper().poaServantActivatorLookupFailed(th);
                }
            } catch (Throwable th2) {
                this.poa.lock();
                if (servant == null || (servant instanceof NullServant)) {
                    if (this.poa.getDebug()) {
                        ORBUtility.dprint(this, "internalGetServant: incarnate failed");
                    }
                    aOMEntryEnterEntry.incarnateFailure();
                } else {
                    if (this.isUnique && this.activeObjectMap.contains(servant)) {
                        if (this.poa.getDebug()) {
                            ORBUtility.dprint(this, "internalGetServant: servant already assigned to ID");
                        }
                        aOMEntryEnterEntry.incarnateFailure();
                        throw this.poa.invocationWrapper().poaServantNotUnique();
                    }
                    if (this.poa.getDebug()) {
                        ORBUtility.dprint(this, "internalGetServant: incarnate complete");
                    }
                    aOMEntryEnterEntry.incarnateComplete();
                    activateServant(key, aOMEntryEnterEntry, servant);
                }
                throw th2;
            }
        } finally {
            if (this.poa.getDebug()) {
                ORBUtility.dprint(this, "Exiting POAPolicyMediatorImpl_R_USM.internalGetServant for poa " + ((Object) this.poa));
            }
        }
    }

    @Override // com.sun.corba.se.impl.oa.poa.POAPolicyMediatorBase_R, com.sun.corba.se.impl.oa.poa.POAPolicyMediator
    public void returnServant() {
        this.activeObjectMap.get(new ActiveObjectMap.Key(this.orb.peekInvocationInfo().id())).exit();
    }

    @Override // com.sun.corba.se.impl.oa.poa.POAPolicyMediator
    public void etherealizeAll() {
        if (this.activator != null) {
            Set setKeySet = this.activeObjectMap.keySet();
            ActiveObjectMap.Key[] keyArr = (ActiveObjectMap.Key[]) setKeySet.toArray(new ActiveObjectMap.Key[setKeySet.size()]);
            for (int i2 = 0; i2 < setKeySet.size(); i2++) {
                ActiveObjectMap.Key key = keyArr[i2];
                AOMEntry aOMEntry = this.activeObjectMap.get(key);
                Servant servant = this.activeObjectMap.getServant(aOMEntry);
                if (servant != null) {
                    boolean zHasMultipleIDs = this.activeObjectMap.hasMultipleIDs(aOMEntry);
                    aOMEntry.startEtherealize(null);
                    try {
                        this.poa.unlock();
                        try {
                            this.activator.etherealize(key.id, this.poa, servant, true, zHasMultipleIDs);
                        } catch (Exception e2) {
                        }
                    } finally {
                        this.poa.lock();
                        aOMEntry.etherealizeComplete();
                    }
                }
            }
        }
    }

    @Override // com.sun.corba.se.impl.oa.poa.POAPolicyMediator
    public ServantManager getServantManager() throws WrongPolicy {
        return this.activator;
    }

    @Override // com.sun.corba.se.impl.oa.poa.POAPolicyMediator
    public void setServantManager(ServantManager servantManager) throws WrongPolicy {
        if (this.activator != null) {
            throw this.poa.invocationWrapper().servantManagerAlreadySet();
        }
        if (servantManager instanceof ServantActivator) {
            this.activator = (ServantActivator) servantManager;
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

    /* loaded from: rt.jar:com/sun/corba/se/impl/oa/poa/POAPolicyMediatorImpl_R_USM$Etherealizer.class */
    class Etherealizer extends Thread {
        private POAPolicyMediatorImpl_R_USM mediator;
        private ActiveObjectMap.Key key;
        private AOMEntry entry;
        private Servant servant;
        private boolean debug;

        public Etherealizer(POAPolicyMediatorImpl_R_USM pOAPolicyMediatorImpl_R_USM, ActiveObjectMap.Key key, AOMEntry aOMEntry, Servant servant, boolean z2) {
            this.mediator = pOAPolicyMediatorImpl_R_USM;
            this.key = key;
            this.entry = aOMEntry;
            this.servant = servant;
            this.debug = z2;
        }

        @Override // java.lang.Thread, java.lang.Runnable
        public void run() {
            if (this.debug) {
                ORBUtility.dprint(this, "Calling Etherealizer.run on key " + ((Object) this.key));
            }
            try {
                try {
                    this.mediator.activator.etherealize(this.key.id, this.mediator.poa, this.servant, false, this.mediator.activeObjectMap.hasMultipleIDs(this.entry));
                } catch (Exception e2) {
                }
                try {
                    this.mediator.poa.lock();
                    this.entry.etherealizeComplete();
                    this.mediator.activeObjectMap.remove(this.key);
                    ((POAManagerImpl) this.mediator.poa.the_POAManager()).getFactory().unregisterPOAForServant(this.mediator.poa, this.servant);
                    this.mediator.poa.unlock();
                } catch (Throwable th) {
                    this.mediator.poa.unlock();
                    throw th;
                }
            } finally {
                if (this.debug) {
                    ORBUtility.dprint(this, "Exiting Etherealizer.run");
                }
            }
        }
    }

    @Override // com.sun.corba.se.impl.oa.poa.POAPolicyMediatorBase_R
    public void deactivateHelper(ActiveObjectMap.Key key, AOMEntry aOMEntry, Servant servant) throws ObjectNotActive, WrongPolicy {
        if (this.activator == null) {
            throw this.poa.invocationWrapper().poaNoServantManager();
        }
        aOMEntry.startEtherealize(new Etherealizer(this, key, aOMEntry, servant, this.poa.getDebug()));
    }

    @Override // com.sun.corba.se.impl.oa.poa.POAPolicyMediator
    public Servant idToServant(byte[] bArr) throws ObjectNotActive, WrongPolicy {
        Servant servant = this.activeObjectMap.getServant(this.activeObjectMap.get(new ActiveObjectMap.Key(bArr)));
        if (servant != null) {
            return servant;
        }
        throw new ObjectNotActive();
    }
}
