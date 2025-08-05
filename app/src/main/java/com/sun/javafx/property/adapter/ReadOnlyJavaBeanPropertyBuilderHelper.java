package com.sun.javafx.property.adapter;

import java.lang.reflect.Method;
import sun.reflect.misc.ReflectUtil;

/* loaded from: jfxrt.jar:com/sun/javafx/property/adapter/ReadOnlyJavaBeanPropertyBuilderHelper.class */
public class ReadOnlyJavaBeanPropertyBuilderHelper {
    private static final String IS_PREFIX = "is";
    private static final String GET_PREFIX = "get";
    private String propertyName;
    private Class<?> beanClass;
    private Object bean;
    private String getterName;
    private Method getter;
    private ReadOnlyPropertyDescriptor descriptor;

    public void name(String propertyName) {
        if (propertyName == null) {
            if (this.propertyName == null) {
                return;
            }
        } else if (propertyName.equals(this.propertyName)) {
            return;
        }
        this.propertyName = propertyName;
        this.descriptor = null;
    }

    public void beanClass(Class<?> beanClass) {
        if (beanClass == null) {
            if (this.beanClass == null) {
                return;
            }
        } else if (beanClass.equals(this.beanClass)) {
            return;
        }
        ReflectUtil.checkPackageAccess(beanClass);
        this.beanClass = beanClass;
        this.descriptor = null;
    }

    public void bean(Object bean) {
        this.bean = bean;
        if (bean != null) {
            Class<?> newClass = bean.getClass();
            if (this.beanClass == null || !this.beanClass.isAssignableFrom(newClass)) {
                ReflectUtil.checkPackageAccess(newClass);
                this.beanClass = bean.getClass();
                this.descriptor = null;
            }
        }
    }

    public Object getBean() {
        return this.bean;
    }

    public void getterName(String getterName) {
        if (getterName == null) {
            if (this.getterName == null) {
                return;
            }
        } else if (getterName.equals(this.getterName)) {
            return;
        }
        this.getterName = getterName;
        this.descriptor = null;
    }

    public void getter(Method getter) {
        if (getter == null) {
            if (this.getter == null) {
                return;
            }
        } else if (getter.equals(this.getter)) {
            return;
        }
        this.getter = getter;
        this.descriptor = null;
    }

    public ReadOnlyPropertyDescriptor getDescriptor() throws NoSuchMethodException {
        if (this.descriptor == null) {
            if (this.propertyName == null || this.bean == null) {
                throw new NullPointerException("Bean and property name have to be specified");
            }
            if (this.propertyName.isEmpty()) {
                throw new IllegalArgumentException("Property name cannot be empty");
            }
            String capitalizedName = ReadOnlyPropertyDescriptor.capitalizedName(this.propertyName);
            if (this.getter == null) {
                if (this.getterName != null && !this.getterName.isEmpty()) {
                    this.getter = this.beanClass.getMethod(this.getterName, new Class[0]);
                } else {
                    try {
                        this.getter = this.beanClass.getMethod("is" + capitalizedName, new Class[0]);
                    } catch (NoSuchMethodException e2) {
                        this.getter = this.beanClass.getMethod("get" + capitalizedName, new Class[0]);
                    }
                }
            }
            this.descriptor = new ReadOnlyPropertyDescriptor(this.propertyName, this.beanClass, this.getter);
        }
        return this.descriptor;
    }
}
