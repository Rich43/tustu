package com.sun.corba.se.spi.oa;

import com.sun.corba.se.impl.logging.OMGSystemException;
import com.sun.corba.se.impl.logging.POASystemException;
import com.sun.corba.se.impl.oa.poa.Policies;
import com.sun.corba.se.spi.copyobject.ObjectCopierFactory;
import com.sun.corba.se.spi.ior.IORFactories;
import com.sun.corba.se.spi.ior.IORTemplate;
import com.sun.corba.se.spi.ior.ObjectAdapterId;
import com.sun.corba.se.spi.ior.ObjectKeyTemplate;
import com.sun.corba.se.spi.logging.CORBALogDomains;
import com.sun.corba.se.spi.orb.ORB;
import com.sun.corba.se.spi.protocol.PIHandler;
import org.omg.CORBA.LocalObject;
import org.omg.CORBA.Object;
import org.omg.CORBA.Policy;
import org.omg.PortableInterceptor.ObjectReferenceFactory;
import org.omg.PortableInterceptor.ObjectReferenceTemplate;

/* loaded from: rt.jar:com/sun/corba/se/spi/oa/ObjectAdapterBase.class */
public abstract class ObjectAdapterBase extends LocalObject implements ObjectAdapter {
    private ORB orb;
    private final POASystemException _iorWrapper;
    private final POASystemException _invocationWrapper;
    private final POASystemException _lifecycleWrapper;
    private final OMGSystemException _omgInvocationWrapper;
    private final OMGSystemException _omgLifecycleWrapper;
    private IORTemplate iortemp;
    private byte[] adapterId;
    private ObjectReferenceTemplate adapterTemplate;
    private ObjectReferenceFactory currentFactory;

    public abstract Policy getEffectivePolicy(int i2);

    public abstract int getManagerId();

    public abstract short getState();

    public abstract Object getLocalServant(byte[] bArr);

    public abstract void getInvocationServant(OAInvocationInfo oAInvocationInfo);

    public abstract void returnServant();

    public abstract void enter() throws OADestroyed;

    public abstract void exit();

    protected abstract ObjectCopierFactory getObjectCopierFactory();

    public abstract String[] getInterfaces(Object obj, byte[] bArr);

    public ObjectAdapterBase(ORB orb) {
        this.orb = orb;
        this._iorWrapper = POASystemException.get(orb, CORBALogDomains.OA_IOR);
        this._lifecycleWrapper = POASystemException.get(orb, CORBALogDomains.OA_LIFECYCLE);
        this._omgLifecycleWrapper = OMGSystemException.get(orb, CORBALogDomains.OA_LIFECYCLE);
        this._invocationWrapper = POASystemException.get(orb, CORBALogDomains.OA_INVOCATION);
        this._omgInvocationWrapper = OMGSystemException.get(orb, CORBALogDomains.OA_INVOCATION);
    }

    public final POASystemException iorWrapper() {
        return this._iorWrapper;
    }

    public final POASystemException lifecycleWrapper() {
        return this._lifecycleWrapper;
    }

    public final OMGSystemException omgLifecycleWrapper() {
        return this._omgLifecycleWrapper;
    }

    public final POASystemException invocationWrapper() {
        return this._invocationWrapper;
    }

    public final OMGSystemException omgInvocationWrapper() {
        return this._omgInvocationWrapper;
    }

    public final void initializeTemplate(ObjectKeyTemplate objectKeyTemplate, boolean z2, Policies policies, String str, String str2, ObjectAdapterId objectAdapterId) {
        PIHandler pIHandler;
        this.adapterId = objectKeyTemplate.getAdapterId();
        this.iortemp = IORFactories.makeIORTemplate(objectKeyTemplate);
        this.orb.getCorbaTransportManager().addToIORTemplate(this.iortemp, policies, str, str2, objectAdapterId);
        this.adapterTemplate = IORFactories.makeObjectReferenceTemplate(this.orb, this.iortemp);
        this.currentFactory = this.adapterTemplate;
        if (z2 && (pIHandler = this.orb.getPIHandler()) != null) {
            pIHandler.objectAdapterCreated(this);
        }
        this.iortemp.makeImmutable();
    }

    public final Object makeObject(String str, byte[] bArr) {
        return this.currentFactory.make_object(str, bArr);
    }

    public final byte[] getAdapterId() {
        return this.adapterId;
    }

    @Override // com.sun.corba.se.spi.oa.ObjectAdapter
    public final ORB getORB() {
        return this.orb;
    }

    @Override // com.sun.corba.se.spi.oa.ObjectAdapter
    public final IORTemplate getIORTemplate() {
        return this.iortemp;
    }

    @Override // com.sun.corba.se.spi.oa.ObjectAdapter
    public final ObjectReferenceTemplate getAdapterTemplate() {
        return this.adapterTemplate;
    }

    @Override // com.sun.corba.se.spi.oa.ObjectAdapter
    public final ObjectReferenceFactory getCurrentFactory() {
        return this.currentFactory;
    }

    @Override // com.sun.corba.se.spi.oa.ObjectAdapter
    public final void setCurrentFactory(ObjectReferenceFactory objectReferenceFactory) {
        this.currentFactory = objectReferenceFactory;
    }

    @Override // com.sun.corba.se.spi.oa.ObjectAdapter
    public OAInvocationInfo makeInvocationInfo(byte[] bArr) {
        OAInvocationInfo oAInvocationInfo = new OAInvocationInfo(this, bArr);
        oAInvocationInfo.setCopierFactory(getObjectCopierFactory());
        return oAInvocationInfo;
    }
}
