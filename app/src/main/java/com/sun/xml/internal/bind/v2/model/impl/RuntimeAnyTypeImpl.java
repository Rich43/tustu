package com.sun.xml.internal.bind.v2.model.impl;

import com.sun.xml.internal.bind.v2.model.runtime.RuntimeNonElement;
import com.sun.xml.internal.bind.v2.runtime.Transducer;
import java.lang.reflect.Type;

/* loaded from: rt.jar:com/sun/xml/internal/bind/v2/model/impl/RuntimeAnyTypeImpl.class */
final class RuntimeAnyTypeImpl extends AnyTypeImpl<Type, Class> implements RuntimeNonElement {
    static final RuntimeNonElement theInstance = new RuntimeAnyTypeImpl();

    private RuntimeAnyTypeImpl() {
        super(Utils.REFLECTION_NAVIGATOR);
    }

    @Override // com.sun.xml.internal.bind.v2.model.runtime.RuntimeNonElement
    public <V> Transducer<V> getTransducer() {
        return null;
    }
}
