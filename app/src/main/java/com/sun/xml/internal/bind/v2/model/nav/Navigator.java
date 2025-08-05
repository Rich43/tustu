package com.sun.xml.internal.bind.v2.model.nav;

import com.sun.xml.internal.bind.v2.runtime.Location;
import java.util.Collection;

/* loaded from: rt.jar:com/sun/xml/internal/bind/v2/model/nav/Navigator.class */
public interface Navigator<T, C, F, M> {
    C getSuperClass(C c2);

    T getBaseClass(T t2, C c2);

    String getClassName(C c2);

    String getTypeName(T t2);

    String getClassShortName(C c2);

    Collection<? extends F> getDeclaredFields(C c2);

    F getDeclaredField(C c2, String str);

    Collection<? extends M> getDeclaredMethods(C c2);

    C getDeclaringClassForField(F f2);

    C getDeclaringClassForMethod(M m2);

    T getFieldType(F f2);

    String getFieldName(F f2);

    String getMethodName(M m2);

    T getReturnType(M m2);

    T[] getMethodParameters(M m2);

    boolean isStaticMethod(M m2);

    boolean isSubClassOf(T t2, T t3);

    T ref(Class cls);

    T use(C c2);

    C asDecl(T t2);

    C asDecl(Class cls);

    boolean isArray(T t2);

    boolean isArrayButNotByteArray(T t2);

    T getComponentType(T t2);

    T getTypeArgument(T t2, int i2);

    boolean isParameterizedType(T t2);

    boolean isPrimitive(T t2);

    T getPrimitive(Class cls);

    Location getClassLocation(C c2);

    Location getFieldLocation(F f2);

    Location getMethodLocation(M m2);

    boolean hasDefaultConstructor(C c2);

    boolean isStaticField(F f2);

    boolean isPublicMethod(M m2);

    boolean isFinalMethod(M m2);

    boolean isPublicField(F f2);

    boolean isEnum(C c2);

    <P> T erasure(T t2);

    boolean isAbstract(C c2);

    boolean isFinal(C c2);

    F[] getEnumConstants(C c2);

    T getVoidType();

    String getPackageName(C c2);

    C loadObjectFactory(C c2, String str);

    boolean isBridgeMethod(M m2);

    boolean isOverriding(M m2, C c2);

    boolean isInterface(C c2);

    boolean isTransient(F f2);

    boolean isInnerClass(C c2);

    boolean isSameType(T t2, T t3);
}
