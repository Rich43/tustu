package com.sun.xml.internal.bind.v2.model.impl;

import com.sun.xml.internal.bind.v2.model.core.Element;
import com.sun.xml.internal.bind.v2.model.core.TypeInfo;
import com.sun.xml.internal.bind.v2.model.impl.RuntimeClassInfoImpl;
import com.sun.xml.internal.bind.v2.model.runtime.RuntimeReferencePropertyInfo;
import com.sun.xml.internal.bind.v2.runtime.reflect.Accessor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Set;

/* loaded from: rt.jar:com/sun/xml/internal/bind/v2/model/impl/RuntimeReferencePropertyInfoImpl.class */
class RuntimeReferencePropertyInfoImpl extends ReferencePropertyInfoImpl<Type, Class, Field, Method> implements RuntimeReferencePropertyInfo {
    private final Accessor acc;

    @Override // com.sun.xml.internal.bind.v2.model.impl.PropertyInfoImpl, com.sun.xml.internal.bind.v2.model.runtime.RuntimePropertyInfo
    public /* bridge */ /* synthetic */ Type getIndividualType() {
        return (Type) super.getIndividualType();
    }

    @Override // com.sun.xml.internal.bind.v2.model.impl.PropertyInfoImpl, com.sun.xml.internal.bind.v2.model.runtime.RuntimePropertyInfo
    public /* bridge */ /* synthetic */ Type getRawType() {
        return (Type) super.getRawType();
    }

    public RuntimeReferencePropertyInfoImpl(RuntimeClassInfoImpl classInfo, PropertySeed<Type, Class, Field, Method> seed) {
        super(classInfo, seed);
        Accessor rawAcc = ((RuntimeClassInfoImpl.RuntimePropertySeed) seed).getAccessor();
        if (getAdapter() != null && !isCollection()) {
            rawAcc = rawAcc.adapt(getAdapter());
        }
        this.acc = rawAcc;
    }

    @Override // com.sun.xml.internal.bind.v2.model.impl.ReferencePropertyInfoImpl, com.sun.xml.internal.bind.v2.model.core.ReferencePropertyInfo
    public Set<? extends Element<Type, Class>> getElements() {
        return super.getElements();
    }

    @Override // com.sun.xml.internal.bind.v2.model.impl.ReferencePropertyInfoImpl, com.sun.xml.internal.bind.v2.model.core.PropertyInfo
    /* renamed from: ref */
    public Collection<? extends TypeInfo<Type, Class>> ref2() {
        return super.ref2();
    }

    @Override // com.sun.xml.internal.bind.v2.model.runtime.RuntimePropertyInfo
    public Accessor getAccessor() {
        return this.acc;
    }

    @Override // com.sun.xml.internal.bind.v2.model.runtime.RuntimePropertyInfo
    public boolean elementOnlyContent() {
        return !isMixed();
    }
}
