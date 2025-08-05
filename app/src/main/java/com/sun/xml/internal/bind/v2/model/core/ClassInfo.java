package com.sun.xml.internal.bind.v2.model.core;

import java.util.List;

/* loaded from: rt.jar:com/sun/xml/internal/bind/v2/model/core/ClassInfo.class */
public interface ClassInfo<T, C> extends MaybeElement<T, C> {
    ClassInfo<T, C> getBaseClass();

    C getClazz();

    String getName();

    List<? extends PropertyInfo<T, C>> getProperties();

    boolean hasValueProperty();

    PropertyInfo<T, C> getProperty(String str);

    boolean hasProperties();

    boolean isAbstract();

    boolean isOrdered();

    boolean isFinal();

    boolean hasSubClasses();

    boolean hasAttributeWildcard();

    boolean inheritsAttributeWildcard();

    boolean declaresAttributeWildcard();
}
