package com.sun.corba.se.impl.interceptors;

import com.sun.corba.se.impl.logging.InterceptorsSystemException;
import com.sun.corba.se.impl.logging.OMGSystemException;
import com.sun.corba.se.impl.logging.ORBUtilSystemException;
import com.sun.corba.se.spi.ior.TaggedProfileTemplate;
import com.sun.corba.se.spi.legacy.interceptor.IORInfoExt;
import com.sun.corba.se.spi.legacy.interceptor.UnknownType;
import com.sun.corba.se.spi.logging.CORBALogDomains;
import com.sun.corba.se.spi.oa.ObjectAdapter;
import com.sun.corba.se.spi.orb.ORB;
import java.util.Iterator;
import org.omg.CORBA.LocalObject;
import org.omg.CORBA.Policy;
import org.omg.IOP.TaggedComponent;
import org.omg.PortableInterceptor.IORInfo;
import org.omg.PortableInterceptor.ObjectReferenceFactory;
import org.omg.PortableInterceptor.ObjectReferenceTemplate;

/* loaded from: rt.jar:com/sun/corba/se/impl/interceptors/IORInfoImpl.class */
public final class IORInfoImpl extends LocalObject implements IORInfo, IORInfoExt {
    private static final int STATE_INITIAL = 0;
    private static final int STATE_ESTABLISHED = 1;
    private static final int STATE_DONE = 2;
    private int state = 0;
    private ObjectAdapter adapter;
    private ORB orb;
    private ORBUtilSystemException orbutilWrapper;
    private InterceptorsSystemException wrapper;
    private OMGSystemException omgWrapper;

    IORInfoImpl(ObjectAdapter objectAdapter) {
        this.orb = objectAdapter.getORB();
        this.orbutilWrapper = ORBUtilSystemException.get(this.orb, CORBALogDomains.RPC_PROTOCOL);
        this.wrapper = InterceptorsSystemException.get(this.orb, CORBALogDomains.RPC_PROTOCOL);
        this.omgWrapper = OMGSystemException.get(this.orb, CORBALogDomains.RPC_PROTOCOL);
        this.adapter = objectAdapter;
    }

    @Override // org.omg.PortableInterceptor.IORInfoOperations
    public Policy get_effective_policy(int i2) {
        checkState(0, 1);
        return this.adapter.getEffectivePolicy(i2);
    }

    @Override // org.omg.PortableInterceptor.IORInfoOperations
    public void add_ior_component(TaggedComponent taggedComponent) {
        checkState(0);
        if (taggedComponent == null) {
            nullParam();
        }
        addIORComponentToProfileInternal(taggedComponent, this.adapter.getIORTemplate().iterator());
    }

    @Override // org.omg.PortableInterceptor.IORInfoOperations
    public void add_ior_component_to_profile(TaggedComponent taggedComponent, int i2) {
        checkState(0);
        if (taggedComponent == null) {
            nullParam();
        }
        addIORComponentToProfileInternal(taggedComponent, this.adapter.getIORTemplate().iteratorById(i2));
    }

    @Override // com.sun.corba.se.spi.legacy.interceptor.IORInfoExt
    public int getServerPort(String str) throws UnknownType {
        checkState(0, 1);
        int iLegacyGetTransientOrPersistentServerPort = this.orb.getLegacyServerSocketManager().legacyGetTransientOrPersistentServerPort(str);
        if (iLegacyGetTransientOrPersistentServerPort == -1) {
            throw new UnknownType();
        }
        return iLegacyGetTransientOrPersistentServerPort;
    }

    @Override // com.sun.corba.se.spi.legacy.interceptor.IORInfoExt
    public ObjectAdapter getObjectAdapter() {
        return this.adapter;
    }

    @Override // org.omg.PortableInterceptor.IORInfoOperations
    public int manager_id() {
        checkState(0, 1);
        return this.adapter.getManagerId();
    }

    @Override // org.omg.PortableInterceptor.IORInfoOperations
    public short state() {
        checkState(0, 1);
        return this.adapter.getState();
    }

    @Override // org.omg.PortableInterceptor.IORInfoOperations
    public ObjectReferenceTemplate adapter_template() {
        checkState(1);
        return this.adapter.getAdapterTemplate();
    }

    @Override // org.omg.PortableInterceptor.IORInfoOperations
    public ObjectReferenceFactory current_factory() {
        checkState(1);
        return this.adapter.getCurrentFactory();
    }

    @Override // org.omg.PortableInterceptor.IORInfoOperations
    public void current_factory(ObjectReferenceFactory objectReferenceFactory) {
        checkState(1);
        this.adapter.setCurrentFactory(objectReferenceFactory);
    }

    private void addIORComponentToProfileInternal(TaggedComponent taggedComponent, Iterator it) {
        com.sun.corba.se.spi.ior.TaggedComponent taggedComponentCreate = this.orb.getTaggedComponentFactoryFinder().create(this.orb, taggedComponent);
        boolean z2 = false;
        while (it.hasNext()) {
            z2 = true;
            ((TaggedProfileTemplate) it.next()).add(taggedComponentCreate);
        }
        if (!z2) {
            throw this.omgWrapper.invalidProfileId();
        }
    }

    private void nullParam() {
        throw this.orbutilWrapper.nullParam();
    }

    private void checkState(int i2) {
        if (i2 != this.state) {
            throw this.wrapper.badState1(new Integer(i2), new Integer(this.state));
        }
    }

    private void checkState(int i2, int i3) {
        if (i2 != this.state && i3 != this.state) {
            throw this.wrapper.badState2(new Integer(i2), new Integer(i3), new Integer(this.state));
        }
    }

    void makeStateEstablished() {
        checkState(0);
        this.state = 1;
    }

    void makeStateDone() {
        checkState(1);
        this.state = 2;
    }
}
