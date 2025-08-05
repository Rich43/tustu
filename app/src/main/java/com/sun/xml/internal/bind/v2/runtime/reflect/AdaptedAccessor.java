package com.sun.xml.internal.bind.v2.runtime.reflect;

import com.sun.xml.internal.bind.api.AccessorException;
import com.sun.xml.internal.bind.v2.ClassFactory;
import com.sun.xml.internal.bind.v2.runtime.Coordinator;
import javax.xml.bind.annotation.adapters.XmlAdapter;

/* loaded from: rt.jar:com/sun/xml/internal/bind/v2/runtime/reflect/AdaptedAccessor.class */
final class AdaptedAccessor<BeanT, InMemValueT, OnWireValueT> extends Accessor<BeanT, OnWireValueT> {
    private final Accessor<BeanT, InMemValueT> core;
    private final Class<? extends XmlAdapter<OnWireValueT, InMemValueT>> adapter;
    private XmlAdapter<OnWireValueT, InMemValueT> staticAdapter;

    AdaptedAccessor(Class<OnWireValueT> targetType, Accessor<BeanT, InMemValueT> extThis, Class<? extends XmlAdapter<OnWireValueT, InMemValueT>> adapter) {
        super(targetType);
        this.core = extThis;
        this.adapter = adapter;
    }

    @Override // com.sun.xml.internal.bind.v2.runtime.reflect.Accessor
    public boolean isAdapted() {
        return true;
    }

    @Override // com.sun.xml.internal.bind.v2.runtime.reflect.Accessor
    public OnWireValueT get(BeanT bean) throws AccessorException {
        InMemValueT v2 = this.core.get(bean);
        XmlAdapter<OnWireValueT, InMemValueT> a2 = getAdapter();
        try {
            return a2.marshal(v2);
        } catch (Exception e2) {
            throw new AccessorException(e2);
        }
    }

    @Override // com.sun.xml.internal.bind.v2.runtime.reflect.Accessor
    public void set(BeanT bean, OnWireValueT o2) throws AccessorException {
        XmlAdapter<OnWireValueT, InMemValueT> a2 = getAdapter();
        try {
            this.core.set(bean, o2 == null ? null : a2.unmarshal(o2));
        } catch (Exception e2) {
            throw new AccessorException(e2);
        }
    }

    @Override // com.sun.xml.internal.bind.v2.runtime.reflect.Accessor
    public Object getUnadapted(BeanT bean) throws AccessorException {
        return this.core.getUnadapted(bean);
    }

    @Override // com.sun.xml.internal.bind.v2.runtime.reflect.Accessor
    public void setUnadapted(BeanT bean, Object value) throws AccessorException {
        this.core.setUnadapted(bean, value);
    }

    private XmlAdapter<OnWireValueT, InMemValueT> getAdapter() {
        Coordinator coordinator = Coordinator._getInstance();
        if (coordinator != null) {
            return coordinator.getAdapter(this.adapter);
        }
        synchronized (this) {
            if (this.staticAdapter == null) {
                this.staticAdapter = (XmlAdapter) ClassFactory.create(this.adapter);
            }
        }
        return this.staticAdapter;
    }
}
