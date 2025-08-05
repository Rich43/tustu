package com.sun.xml.internal.bind.v2.model.impl;

import com.sun.xml.internal.bind.v2.model.runtime.RuntimeNonElement;
import com.sun.xml.internal.bind.v2.model.runtime.RuntimePropertyInfo;
import com.sun.xml.internal.bind.v2.model.runtime.RuntimeTypeRef;
import com.sun.xml.internal.bind.v2.runtime.Transducer;
import java.lang.reflect.Type;
import javax.xml.namespace.QName;

/* loaded from: rt.jar:com/sun/xml/internal/bind/v2/model/impl/RuntimeTypeRefImpl.class */
final class RuntimeTypeRefImpl extends TypeRefImpl<Type, Class> implements RuntimeTypeRef {
    public RuntimeTypeRefImpl(RuntimeElementPropertyInfoImpl elementPropertyInfo, QName elementName, Type type, boolean isNillable, String defaultValue) {
        super(elementPropertyInfo, elementName, type, isNillable, defaultValue);
    }

    @Override // com.sun.xml.internal.bind.v2.model.impl.TypeRefImpl, com.sun.xml.internal.bind.v2.model.core.NonElementRef
    /* renamed from: getTarget */
    public RuntimeNonElement getTarget2() {
        return (RuntimeNonElement) super.getTarget2();
    }

    @Override // com.sun.xml.internal.bind.v2.model.runtime.RuntimeNonElementRef
    public Transducer getTransducer() {
        return RuntimeModelBuilder.createTransducer(this);
    }

    @Override // com.sun.xml.internal.bind.v2.model.impl.TypeRefImpl, com.sun.xml.internal.bind.v2.model.core.NonElementRef
    /* renamed from: getSource */
    public RuntimePropertyInfo getSource2() {
        return (RuntimePropertyInfo) this.owner;
    }
}
