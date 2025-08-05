package com.sun.corba.se.impl.oa.poa;

import com.sun.corba.se.impl.javax.rmi.CORBA.Util;
import com.sun.corba.se.impl.oa.poa.ActiveObjectMap;
import com.sun.corba.se.impl.orbutil.ORBUtility;
import org.omg.PortableServer.POAPackage.ObjectAlreadyActive;
import org.omg.PortableServer.POAPackage.ObjectNotActive;
import org.omg.PortableServer.POAPackage.ServantAlreadyActive;
import org.omg.PortableServer.POAPackage.ServantNotActive;
import org.omg.PortableServer.POAPackage.WrongPolicy;
import org.omg.PortableServer.Servant;

/* loaded from: rt.jar:com/sun/corba/se/impl/oa/poa/POAPolicyMediatorBase_R.class */
public abstract class POAPolicyMediatorBase_R extends POAPolicyMediatorBase {
    protected ActiveObjectMap activeObjectMap;

    POAPolicyMediatorBase_R(Policies policies, POAImpl pOAImpl) {
        super(policies, pOAImpl);
        if (!policies.retainServants()) {
            throw pOAImpl.invocationWrapper().policyMediatorBadPolicyInFactory();
        }
        this.activeObjectMap = ActiveObjectMap.create(pOAImpl, !this.isUnique);
    }

    @Override // com.sun.corba.se.impl.oa.poa.POAPolicyMediator
    public void returnServant() {
    }

    @Override // com.sun.corba.se.impl.oa.poa.POAPolicyMediator
    public void clearAOM() {
        this.activeObjectMap.clear();
        this.activeObjectMap = null;
    }

    protected Servant internalKeyToServant(ActiveObjectMap.Key key) {
        AOMEntry aOMEntry = this.activeObjectMap.get(key);
        if (aOMEntry == null) {
            return null;
        }
        return this.activeObjectMap.getServant(aOMEntry);
    }

    protected Servant internalIdToServant(byte[] bArr) {
        return internalKeyToServant(new ActiveObjectMap.Key(bArr));
    }

    protected void activateServant(ActiveObjectMap.Key key, AOMEntry aOMEntry, Servant servant) {
        setDelegate(servant, key.id);
        if (this.orb.shutdownDebugFlag) {
            System.out.println("Activating object " + ((Object) servant) + " with POA " + ((Object) this.poa));
        }
        this.activeObjectMap.putServant(servant, aOMEntry);
        if (Util.isInstanceDefined()) {
            ((POAManagerImpl) this.poa.the_POAManager()).getFactory().registerPOAForServant(this.poa, servant);
        }
    }

    @Override // com.sun.corba.se.impl.oa.poa.POAPolicyMediator
    public final void activateObject(byte[] bArr, Servant servant) throws ObjectAlreadyActive, ServantAlreadyActive, WrongPolicy {
        if (this.isUnique && this.activeObjectMap.contains(servant)) {
            throw new ServantAlreadyActive();
        }
        ActiveObjectMap.Key key = new ActiveObjectMap.Key(bArr);
        AOMEntry aOMEntry = this.activeObjectMap.get(key);
        aOMEntry.activateObject();
        activateServant(key, aOMEntry, servant);
    }

    @Override // com.sun.corba.se.impl.oa.poa.POAPolicyMediator
    public Servant deactivateObject(byte[] bArr) throws ObjectNotActive, WrongPolicy {
        return deactivateObject(new ActiveObjectMap.Key(bArr));
    }

    protected void deactivateHelper(ActiveObjectMap.Key key, AOMEntry aOMEntry, Servant servant) throws ObjectNotActive, WrongPolicy {
        this.activeObjectMap.remove(key);
        if (Util.isInstanceDefined()) {
            ((POAManagerImpl) this.poa.the_POAManager()).getFactory().unregisterPOAForServant(this.poa, servant);
        }
    }

    public Servant deactivateObject(ActiveObjectMap.Key key) throws ObjectNotActive, WrongPolicy {
        if (this.orb.poaDebugFlag) {
            ORBUtility.dprint(this, "Calling deactivateObject for key " + ((Object) key));
        }
        try {
            AOMEntry aOMEntry = this.activeObjectMap.get(key);
            if (aOMEntry == null) {
                throw new ObjectNotActive();
            }
            Servant servant = this.activeObjectMap.getServant(aOMEntry);
            if (servant == null) {
                throw new ObjectNotActive();
            }
            if (this.orb.poaDebugFlag) {
                System.out.println("Deactivating object " + ((Object) servant) + " with POA " + ((Object) this.poa));
            }
            deactivateHelper(key, aOMEntry, servant);
            if (this.orb.poaDebugFlag) {
                ORBUtility.dprint(this, "Exiting deactivateObject");
            }
            return servant;
        } catch (Throwable th) {
            if (this.orb.poaDebugFlag) {
                ORBUtility.dprint(this, "Exiting deactivateObject");
            }
            throw th;
        }
    }

    @Override // com.sun.corba.se.impl.oa.poa.POAPolicyMediator
    public byte[] servantToId(Servant servant) throws ServantNotActive, WrongPolicy {
        ActiveObjectMap.Key key;
        if (!this.isUnique && !this.isImplicit) {
            throw new WrongPolicy();
        }
        if (this.isUnique && (key = this.activeObjectMap.getKey(servant)) != null) {
            return key.id;
        }
        if (this.isImplicit) {
            try {
                byte[] bArrNewSystemId = newSystemId();
                activateObject(bArrNewSystemId, servant);
                return bArrNewSystemId;
            } catch (ObjectAlreadyActive e2) {
                throw this.poa.invocationWrapper().servantToIdOaa(e2);
            } catch (ServantAlreadyActive e3) {
                throw this.poa.invocationWrapper().servantToIdSaa(e3);
            } catch (WrongPolicy e4) {
                throw this.poa.invocationWrapper().servantToIdWp(e4);
            }
        }
        throw new ServantNotActive();
    }
}
