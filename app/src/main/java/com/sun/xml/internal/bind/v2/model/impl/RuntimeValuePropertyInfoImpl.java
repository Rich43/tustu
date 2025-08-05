package com.sun.xml.internal.bind.v2.model.impl;

import com.sun.xml.internal.bind.v2.model.core.TypeInfo;
import com.sun.xml.internal.bind.v2.model.runtime.RuntimeNonElement;
import com.sun.xml.internal.bind.v2.model.runtime.RuntimePropertyInfo;
import com.sun.xml.internal.bind.v2.model.runtime.RuntimeValuePropertyInfo;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Collection;

/* loaded from: rt.jar:com/sun/xml/internal/bind/v2/model/impl/RuntimeValuePropertyInfoImpl.class */
final class RuntimeValuePropertyInfoImpl extends ValuePropertyInfoImpl<Type, Class, Field, Method> implements RuntimeValuePropertyInfo {
    @Override // com.sun.xml.internal.bind.v2.model.impl.PropertyInfoImpl, com.sun.xml.internal.bind.v2.model.runtime.RuntimePropertyInfo
    public /* bridge */ /* synthetic */ Type getIndividualType() {
        return (Type) super.getIndividualType();
    }

    @Override // com.sun.xml.internal.bind.v2.model.impl.PropertyInfoImpl, com.sun.xml.internal.bind.v2.model.runtime.RuntimePropertyInfo
    public /* bridge */ /* synthetic */ Type getRawType() {
        return (Type) super.getRawType();
    }

    RuntimeValuePropertyInfoImpl(RuntimeClassInfoImpl classInfo, PropertySeed<Type, Class, Field, Method> seed) {
        super(classInfo, seed);
    }

    @Override // com.sun.xml.internal.bind.v2.model.runtime.RuntimePropertyInfo
    public boolean elementOnlyContent() {
        return false;
    }

    @Override // com.sun.xml.internal.bind.v2.model.impl.SingleTypePropertyInfoImpl, com.sun.xml.internal.bind.v2.model.core.NonElementRef
    /* renamed from: getSource */
    public RuntimePropertyInfo getSource2() {
        return (RuntimePropertyInfo) super.getSource2();
    }

    @Override // com.sun.xml.internal.bind.v2.model.impl.SingleTypePropertyInfoImpl, com.sun.xml.internal.bind.v2.model.core.AttributePropertyInfo, com.sun.xml.internal.bind.v2.model.core.NonElementRef
    /* renamed from: getTarget */
    public RuntimeNonElement getTarget2() {
        return (RuntimeNonElement) super.getTarget2();
    }

    @Override // com.sun.xml.internal.bind.v2.model.impl.SingleTypePropertyInfoImpl, com.sun.xml.internal.bind.v2.model.core.PropertyInfo
    /* renamed from: ref */
    public Collection<? extends TypeInfo<Type, Class>> ref2() {
        return super.ref2();
    }

    @Override // com.sun.xml.internal.bind.v2.model.impl.SingleTypePropertyInfoImpl, com.sun.xml.internal.bind.v2.model.impl.PropertyInfoImpl
    public void link() {
        getTransducer();
        super.link();
    }
}
