package com.sun.xml.internal.bind.v2.model.impl;

import com.sun.xml.internal.bind.v2.model.core.TypeInfo;
import com.sun.xml.internal.bind.v2.model.core.TypeRef;
import com.sun.xml.internal.bind.v2.model.impl.RuntimeClassInfoImpl;
import com.sun.xml.internal.bind.v2.model.runtime.RuntimeElementPropertyInfo;
import com.sun.xml.internal.bind.v2.runtime.reflect.Accessor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.List;
import javax.xml.namespace.QName;

/* loaded from: rt.jar:com/sun/xml/internal/bind/v2/model/impl/RuntimeElementPropertyInfoImpl.class */
class RuntimeElementPropertyInfoImpl extends ElementPropertyInfoImpl<Type, Class, Field, Method> implements RuntimeElementPropertyInfo {
    private final Accessor acc;

    @Override // com.sun.xml.internal.bind.v2.model.impl.PropertyInfoImpl, com.sun.xml.internal.bind.v2.model.runtime.RuntimePropertyInfo
    public /* bridge */ /* synthetic */ Type getIndividualType() {
        return (Type) super.getIndividualType();
    }

    @Override // com.sun.xml.internal.bind.v2.model.impl.PropertyInfoImpl, com.sun.xml.internal.bind.v2.model.runtime.RuntimePropertyInfo
    public /* bridge */ /* synthetic */ Type getRawType() {
        return (Type) super.getRawType();
    }

    RuntimeElementPropertyInfoImpl(RuntimeClassInfoImpl classInfo, PropertySeed<Type, Class, Field, Method> seed) {
        super(classInfo, seed);
        Accessor rawAcc = ((RuntimeClassInfoImpl.RuntimePropertySeed) seed).getAccessor();
        if (getAdapter() != null && !isCollection()) {
            rawAcc = rawAcc.adapt(getAdapter());
        }
        this.acc = rawAcc;
    }

    @Override // com.sun.xml.internal.bind.v2.model.runtime.RuntimePropertyInfo
    public Accessor getAccessor() {
        return this.acc;
    }

    @Override // com.sun.xml.internal.bind.v2.model.runtime.RuntimePropertyInfo
    public boolean elementOnlyContent() {
        return true;
    }

    @Override // com.sun.xml.internal.bind.v2.model.impl.ElementPropertyInfoImpl, com.sun.xml.internal.bind.v2.model.core.PropertyInfo
    /* renamed from: ref */
    public Collection<? extends TypeInfo<Type, Class>> ref2() {
        return super.ref2();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.sun.xml.internal.bind.v2.model.impl.ElementPropertyInfoImpl
    public RuntimeTypeRefImpl createTypeRef(QName name, Type type, boolean isNillable, String defaultValue) {
        return new RuntimeTypeRefImpl(this, name, type, isNillable, defaultValue);
    }

    @Override // com.sun.xml.internal.bind.v2.model.impl.ElementPropertyInfoImpl, com.sun.xml.internal.bind.v2.model.core.ElementPropertyInfo
    public List<? extends TypeRef<Type, Class>> getTypes() {
        return super.getTypes();
    }
}
