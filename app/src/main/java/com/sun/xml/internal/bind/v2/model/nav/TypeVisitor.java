package com.sun.xml.internal.bind.v2.model.nav;

import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;

/* loaded from: rt.jar:com/sun/xml/internal/bind/v2/model/nav/TypeVisitor.class */
abstract class TypeVisitor<T, P> {
    static final /* synthetic */ boolean $assertionsDisabled;

    protected abstract T onClass(Class cls, P p2);

    protected abstract T onParameterizdType(ParameterizedType parameterizedType, P p2);

    protected abstract T onGenericArray(GenericArrayType genericArrayType, P p2);

    protected abstract T onVariable(TypeVariable typeVariable, P p2);

    protected abstract T onWildcard(WildcardType wildcardType, P p2);

    TypeVisitor() {
    }

    static {
        $assertionsDisabled = !TypeVisitor.class.desiredAssertionStatus();
    }

    public final T visit(Type t2, P param) {
        if (!$assertionsDisabled && t2 == null) {
            throw new AssertionError();
        }
        if (t2 instanceof Class) {
            return onClass((Class) t2, param);
        }
        if (t2 instanceof ParameterizedType) {
            return onParameterizdType((ParameterizedType) t2, param);
        }
        if (t2 instanceof GenericArrayType) {
            return onGenericArray((GenericArrayType) t2, param);
        }
        if (t2 instanceof WildcardType) {
            return onWildcard((WildcardType) t2, param);
        }
        if (t2 instanceof TypeVariable) {
            return onVariable((TypeVariable) t2, param);
        }
        if ($assertionsDisabled) {
            throw new IllegalArgumentException();
        }
        throw new AssertionError();
    }
}
