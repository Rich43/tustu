package com.sun.corba.se.impl.oa.toa;

import com.sun.corba.se.impl.ior.JIDLObjectKeyTemplate;
import com.sun.corba.se.impl.oa.NullServantImpl;
import com.sun.corba.se.impl.oa.poa.Policies;
import com.sun.corba.se.impl.protocol.JIDLLocalCRDImpl;
import com.sun.corba.se.pept.protocol.ClientDelegate;
import com.sun.corba.se.spi.copyobject.ObjectCopierFactory;
import com.sun.corba.se.spi.oa.OADestroyed;
import com.sun.corba.se.spi.oa.OAInvocationInfo;
import com.sun.corba.se.spi.oa.ObjectAdapterBase;
import com.sun.corba.se.spi.orb.ORB;
import com.sun.corba.se.spi.presentation.rmi.StubAdapter;
import com.sun.corba.se.spi.protocol.LocalClientRequestDispatcher;
import com.sun.corba.se.spi.transport.CorbaContactInfoList;
import org.omg.CORBA.Object;
import org.omg.CORBA.Policy;
import org.omg.CORBA.portable.Delegate;

/* loaded from: rt.jar:com/sun/corba/se/impl/oa/toa/TOAImpl.class */
public class TOAImpl extends ObjectAdapterBase implements TOA {
    private TransientObjectManager servants;

    public TOAImpl(ORB orb, TransientObjectManager transientObjectManager, String str) {
        super(orb);
        this.servants = transientObjectManager;
        JIDLObjectKeyTemplate jIDLObjectKeyTemplate = new JIDLObjectKeyTemplate(orb, 2, getORB().getTransientServerId());
        initializeTemplate(jIDLObjectKeyTemplate, true, Policies.defaultPolicies, str, null, jIDLObjectKeyTemplate.getObjectAdapterId());
    }

    @Override // com.sun.corba.se.spi.oa.ObjectAdapterBase
    public ObjectCopierFactory getObjectCopierFactory() {
        return getORB().getCopierManager().getDefaultObjectCopierFactory();
    }

    @Override // com.sun.corba.se.spi.oa.ObjectAdapterBase, com.sun.corba.se.spi.oa.ObjectAdapter
    public Object getLocalServant(byte[] bArr) {
        return (Object) this.servants.lookupServant(bArr);
    }

    @Override // com.sun.corba.se.spi.oa.ObjectAdapterBase, com.sun.corba.se.spi.oa.ObjectAdapter
    public void getInvocationServant(OAInvocationInfo oAInvocationInfo) {
        Object objLookupServant = this.servants.lookupServant(oAInvocationInfo.id());
        if (objLookupServant == null) {
            objLookupServant = new NullServantImpl(lifecycleWrapper().nullServant());
        }
        oAInvocationInfo.setServant(objLookupServant);
    }

    @Override // com.sun.corba.se.spi.oa.ObjectAdapterBase, com.sun.corba.se.spi.oa.ObjectAdapter
    public void returnServant() {
    }

    @Override // com.sun.corba.se.spi.oa.ObjectAdapterBase, com.sun.corba.se.spi.oa.ObjectAdapter
    public String[] getInterfaces(Object obj, byte[] bArr) {
        return StubAdapter.getTypeIds(obj);
    }

    @Override // com.sun.corba.se.spi.oa.ObjectAdapterBase, com.sun.corba.se.spi.oa.ObjectAdapter
    public Policy getEffectivePolicy(int i2) {
        return null;
    }

    @Override // com.sun.corba.se.spi.oa.ObjectAdapterBase, com.sun.corba.se.spi.oa.ObjectAdapter
    public int getManagerId() {
        return -1;
    }

    @Override // com.sun.corba.se.spi.oa.ObjectAdapterBase, com.sun.corba.se.spi.oa.ObjectAdapter
    public short getState() {
        return (short) 1;
    }

    @Override // com.sun.corba.se.spi.oa.ObjectAdapterBase, com.sun.corba.se.spi.oa.ObjectAdapter
    public void enter() throws OADestroyed {
    }

    @Override // com.sun.corba.se.spi.oa.ObjectAdapterBase, com.sun.corba.se.spi.oa.ObjectAdapter
    public void exit() {
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.sun.corba.se.impl.oa.toa.TOA
    public void connect(Object object) {
        Delegate delegate = StubAdapter.getDelegate(getCurrentFactory().make_object(StubAdapter.getTypeIds(object)[0], this.servants.storeServant(object, null)));
        LocalClientRequestDispatcher localClientRequestDispatcher = ((CorbaContactInfoList) ((ClientDelegate) delegate).getContactInfoList()).getLocalClientRequestDispatcher();
        if (localClientRequestDispatcher instanceof JIDLLocalCRDImpl) {
            ((JIDLLocalCRDImpl) localClientRequestDispatcher).setServant(object);
            StubAdapter.setDelegate(object, delegate);
            return;
        }
        throw new RuntimeException("TOAImpl.connect can not be called on " + ((Object) localClientRequestDispatcher));
    }

    @Override // com.sun.corba.se.impl.oa.toa.TOA
    public void disconnect(Object object) {
        LocalClientRequestDispatcher localClientRequestDispatcher = ((CorbaContactInfoList) ((ClientDelegate) StubAdapter.getDelegate(object)).getContactInfoList()).getLocalClientRequestDispatcher();
        if (localClientRequestDispatcher instanceof JIDLLocalCRDImpl) {
            JIDLLocalCRDImpl jIDLLocalCRDImpl = (JIDLLocalCRDImpl) localClientRequestDispatcher;
            this.servants.deleteServant(jIDLLocalCRDImpl.getObjectId());
            jIDLLocalCRDImpl.unexport();
            return;
        }
        throw new RuntimeException("TOAImpl.disconnect can not be called on " + ((Object) localClientRequestDispatcher));
    }
}
