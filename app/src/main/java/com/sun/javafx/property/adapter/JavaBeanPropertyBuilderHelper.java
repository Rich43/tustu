package com.sun.javafx.property.adapter;

import java.lang.reflect.Method;
import sun.reflect.misc.ReflectUtil;

/* loaded from: jfxrt.jar:com/sun/javafx/property/adapter/JavaBeanPropertyBuilderHelper.class */
public class JavaBeanPropertyBuilderHelper {
    private static final String IS_PREFIX = "is";
    private static final String GET_PREFIX = "get";
    private static final String SET_PREFIX = "set";
    private String propertyName;
    private Class<?> beanClass;
    private Object bean;
    private String getterName;
    private String setterName;
    private Method getter;
    private Method setter;
    private PropertyDescriptor descriptor;

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
                this.beanClass = newClass;
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

    public void setterName(String setterName) {
        if (setterName == null) {
            if (this.setterName == null) {
                return;
            }
        } else if (setterName.equals(this.setterName)) {
            return;
        }
        this.setterName = setterName;
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

    public void setter(Method setter) {
        if (setter == null) {
            if (this.setter == null) {
                return;
            }
        } else if (setter.equals(this.setter)) {
            return;
        }
        this.setter = setter;
        this.descriptor = null;
    }

    public PropertyDescriptor getDescriptor() throws NoSuchMethodException, SecurityException {
        if (this.descriptor == null) {
            if (this.propertyName == null) {
                throw new NullPointerException("Property name has to be specified");
            }
            if (this.propertyName.isEmpty()) {
                throw new IllegalArgumentException("Property name cannot be empty");
            }
            String capitalizedName = ReadOnlyPropertyDescriptor.capitalizedName(this.propertyName);
            Method getterMethod = this.getter;
            if (getterMethod == null) {
                if (this.getterName != null && !this.getterName.isEmpty()) {
                    getterMethod = this.beanClass.getMethod(this.getterName, new Class[0]);
                } else {
                    try {
                        getterMethod = this.beanClass.getMethod("is" + capitalizedName, new Class[0]);
                    } catch (NoSuchMethodException e2) {
                        getterMethod = this.beanClass.getMethod("get" + capitalizedName, new Class[0]);
                    }
                }
            }
            Method setterMethod = this.setter;
            if (setterMethod == null) {
                Class<?> type = getterMethod.getReturnType();
                if (this.setterName != null && !this.setterName.isEmpty()) {
                    setterMethod = this.beanClass.getMethod(this.setterName, type);
                } else {
                    setterMethod = this.beanClass.getMethod("set" + capitalizedName, type);
                }
            }
            this.descriptor = new PropertyDescriptor(this.propertyName, this.beanClass, getterMethod, setterMethod);
        }
        return this.descriptor;
    }
}
