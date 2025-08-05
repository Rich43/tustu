package com.sun.javafx.property.adapter;

import javafx.beans.property.adapter.ReadOnlyJavaBeanObjectProperty;
import javafx.beans.property.adapter.ReadOnlyJavaBeanObjectPropertyBuilder;

/* loaded from: jfxrt.jar:com/sun/javafx/property/adapter/JavaBeanQuickAccessor.class */
public final class JavaBeanQuickAccessor {
    private JavaBeanQuickAccessor() {
    }

    public static <T> ReadOnlyJavaBeanObjectProperty<T> createReadOnlyJavaBeanObjectProperty(Object bean, String name) throws NoSuchMethodException {
        return ReadOnlyJavaBeanObjectPropertyBuilder.create().bean(bean).name(name).build();
    }
}
