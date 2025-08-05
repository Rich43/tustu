package com.sun.xml.internal.bind.v2.model.nav;

import java.lang.reflect.GenericArrayType;
import java.lang.reflect.Type;

/* loaded from: rt.jar:com/sun/xml/internal/bind/v2/model/nav/GenericArrayTypeImpl.class */
final class GenericArrayTypeImpl implements GenericArrayType {
    private Type genericComponentType;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !GenericArrayTypeImpl.class.desiredAssertionStatus();
    }

    GenericArrayTypeImpl(Type ct) {
        if (!$assertionsDisabled && ct == null) {
            throw new AssertionError();
        }
        this.genericComponentType = ct;
    }

    @Override // java.lang.reflect.GenericArrayType
    public Type getGenericComponentType() {
        return this.genericComponentType;
    }

    public String toString() {
        Type componentType = getGenericComponentType();
        StringBuilder sb = new StringBuilder();
        if (componentType instanceof Class) {
            sb.append(((Class) componentType).getName());
        } else {
            sb.append(componentType.toString());
        }
        sb.append("[]");
        return sb.toString();
    }

    public boolean equals(Object o2) {
        if (o2 instanceof GenericArrayType) {
            GenericArrayType that = (GenericArrayType) o2;
            Type thatComponentType = that.getGenericComponentType();
            return this.genericComponentType.equals(thatComponentType);
        }
        return false;
    }

    public int hashCode() {
        return this.genericComponentType.hashCode();
    }
}
