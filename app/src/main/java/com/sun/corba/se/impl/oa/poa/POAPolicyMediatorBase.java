package com.sun.corba.se.impl.oa.poa;

import com.sun.corba.se.impl.orbutil.ORBConstants;
import com.sun.corba.se.impl.orbutil.ORBUtility;
import com.sun.corba.se.spi.orb.ORB;
import org.omg.PortableServer.ForwardRequest;
import org.omg.PortableServer.POAPackage.WrongPolicy;
import org.omg.PortableServer.Servant;

/* loaded from: rt.jar:com/sun/corba/se/impl/oa/poa/POAPolicyMediatorBase.class */
public abstract class POAPolicyMediatorBase implements POAPolicyMediator {
    protected POAImpl poa;
    protected ORB orb;
    private int sysIdCounter;
    private Policies policies;
    private DelegateImpl delegateImpl;
    private int serverid;
    private int scid;
    protected boolean isImplicit;
    protected boolean isUnique;
    protected boolean isSystemId;

    protected abstract Object internalGetServant(byte[] bArr, String str) throws ForwardRequest;

    @Override // com.sun.corba.se.impl.oa.poa.POAPolicyMediator
    public final Policies getPolicies() {
        return this.policies;
    }

    @Override // com.sun.corba.se.impl.oa.poa.POAPolicyMediator
    public final int getScid() {
        return this.scid;
    }

    @Override // com.sun.corba.se.impl.oa.poa.POAPolicyMediator
    public final int getServerId() {
        return this.serverid;
    }

    POAPolicyMediatorBase(Policies policies, POAImpl pOAImpl) {
        if (policies.isSingleThreaded()) {
            throw pOAImpl.invocationWrapper().singleThreadNotSupported();
        }
        this.delegateImpl = (DelegateImpl) ((POAManagerImpl) pOAImpl.the_POAManager()).getFactory().getDelegateImpl();
        this.policies = policies;
        this.poa = pOAImpl;
        this.orb = pOAImpl.getORB();
        switch (policies.servantCachingLevel()) {
            case 0:
                this.scid = 32;
                break;
            case 1:
                this.scid = 36;
                break;
            case 2:
                this.scid = 40;
                break;
            case 3:
                this.scid = 44;
                break;
        }
        if (policies.isTransient()) {
            this.serverid = this.orb.getTransientServerId();
        } else {
            this.serverid = this.orb.getORBData().getPersistentServerId();
            this.scid = ORBConstants.makePersistent(this.scid);
        }
        this.isImplicit = policies.isImplicitlyActivated();
        this.isUnique = policies.isUniqueIds();
        this.isSystemId = policies.isSystemAssignedIds();
        this.sysIdCounter = 0;
    }

    @Override // com.sun.corba.se.impl.oa.poa.POAPolicyMediator
    public final Object getInvocationServant(byte[] bArr, String str) throws ForwardRequest {
        return internalGetServant(bArr, str);
    }

    protected final void setDelegate(Servant servant, byte[] bArr) {
        servant._set_delegate(this.delegateImpl);
    }

    @Override // com.sun.corba.se.impl.oa.poa.POAPolicyMediator
    public synchronized byte[] newSystemId() throws WrongPolicy {
        if (!this.isSystemId) {
            throw new WrongPolicy();
        }
        byte[] bArr = new byte[8];
        int i2 = this.sysIdCounter + 1;
        this.sysIdCounter = i2;
        ORBUtility.intToBytes(i2, bArr, 0);
        ORBUtility.intToBytes(this.poa.getPOAId(), bArr, 4);
        return bArr;
    }
}
