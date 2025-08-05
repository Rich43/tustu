package com.sun.xml.internal.bind.v2.runtime.property;

import com.sun.xml.internal.bind.api.AccessorException;
import com.sun.xml.internal.bind.v2.model.runtime.RuntimePropertyInfo;
import com.sun.xml.internal.bind.v2.runtime.JAXBContextImpl;
import com.sun.xml.internal.bind.v2.runtime.reflect.Accessor;
import com.sun.xml.internal.bind.v2.runtime.reflect.Lister;

/* loaded from: rt.jar:com/sun/xml/internal/bind/v2/runtime/property/ArrayProperty.class */
abstract class ArrayProperty<BeanT, ListT, ItemT> extends PropertyImpl<BeanT> {
    protected final Accessor<BeanT, ListT> acc;
    protected final Lister<BeanT, ListT, ItemT, Object> lister;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !ArrayProperty.class.desiredAssertionStatus();
    }

    protected ArrayProperty(JAXBContextImpl context, RuntimePropertyInfo prop) {
        super(context, prop);
        if (!$assertionsDisabled && !prop.isCollection()) {
            throw new AssertionError();
        }
        this.lister = Lister.create(Utils.REFLECTION_NAVIGATOR.erasure(prop.getRawType()), prop.id(), prop.getAdapter());
        if (!$assertionsDisabled && this.lister == null) {
            throw new AssertionError();
        }
        this.acc = prop.getAccessor().optimize(context);
        if (!$assertionsDisabled && this.acc == null) {
            throw new AssertionError();
        }
    }

    @Override // com.sun.xml.internal.bind.v2.runtime.property.Property
    public void reset(BeanT o2) throws AccessorException {
        this.lister.reset(o2, this.acc);
    }

    @Override // com.sun.xml.internal.bind.v2.runtime.property.Property
    public final String getIdValue(BeanT bean) {
        return null;
    }
}
