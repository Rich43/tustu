package com.sun.javafx.property;

import com.sun.javafx.fxml.BeanAdapter;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import javafx.beans.property.ReadOnlyProperty;
import sun.reflect.misc.MethodUtil;
import sun.reflect.misc.ReflectUtil;

/* loaded from: jfxrt.jar:com/sun/javafx/property/PropertyReference.class */
public final class PropertyReference<T> {
    private String name;
    private Method getter;
    private Method setter;
    private Method propertyGetter;
    private Class<?> clazz;
    private Class<?> type;
    private boolean reflected = false;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !PropertyReference.class.desiredAssertionStatus();
    }

    public PropertyReference(Class<?> clazz, String name) {
        if (name == null) {
            throw new NullPointerException("Name must be specified");
        }
        if (name.trim().length() == 0) {
            throw new IllegalArgumentException("Name must be specified");
        }
        if (clazz == null) {
            throw new NullPointerException("Class must be specified");
        }
        ReflectUtil.checkPackageAccess(clazz);
        this.name = name;
        this.clazz = clazz;
    }

    public boolean isWritable() {
        reflect();
        return this.setter != null;
    }

    public boolean isReadable() {
        reflect();
        return this.getter != null;
    }

    public boolean hasProperty() {
        reflect();
        return this.propertyGetter != null;
    }

    public String getName() {
        return this.name;
    }

    public Class<?> getContainingClass() {
        return this.clazz;
    }

    public Class<?> getType() {
        reflect();
        return this.type;
    }

    public void set(Object bean, T value) {
        if (!isWritable()) {
            throw new IllegalStateException("Cannot write to readonly property " + this.name);
        }
        if (!$assertionsDisabled && this.setter == null) {
            throw new AssertionError();
        }
        try {
            MethodUtil.invoke(this.setter, bean, new Object[]{value});
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public T get(Object obj) {
        if (!isReadable()) {
            throw new IllegalStateException("Cannot read from unreadable property " + this.name);
        }
        if (!$assertionsDisabled && this.getter == null) {
            throw new AssertionError();
        }
        try {
            return (T) MethodUtil.invoke(this.getter, obj, (Object[]) null);
        } catch (Exception e2) {
            throw new RuntimeException(e2);
        }
    }

    public ReadOnlyProperty<T> getProperty(Object bean) {
        if (!hasProperty()) {
            throw new IllegalStateException("Cannot get property " + this.name);
        }
        if (!$assertionsDisabled && this.propertyGetter == null) {
            throw new AssertionError();
        }
        try {
            return (ReadOnlyProperty) MethodUtil.invoke(this.propertyGetter, bean, (Object[]) null);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public String toString() {
        return this.name;
    }

    private void reflect() {
        String upperCase;
        if (!this.reflected) {
            this.reflected = true;
            try {
                if (this.name.length() == 1) {
                    upperCase = this.name.substring(0, 1).toUpperCase();
                } else {
                    upperCase = Character.toUpperCase(this.name.charAt(0)) + this.name.substring(1);
                }
                String properName = upperCase;
                this.type = null;
                String getterName = "get" + properName;
                try {
                    Method m2 = this.clazz.getMethod(getterName, new Class[0]);
                    if (Modifier.isPublic(m2.getModifiers())) {
                        this.getter = m2;
                    }
                } catch (NoSuchMethodException e2) {
                }
                if (this.getter == null) {
                    String getterName2 = BeanAdapter.IS_PREFIX + properName;
                    try {
                        Method m3 = this.clazz.getMethod(getterName2, new Class[0]);
                        if (Modifier.isPublic(m3.getModifiers())) {
                            this.getter = m3;
                        }
                    } catch (NoSuchMethodException e3) {
                    }
                }
                String setterName = "set" + properName;
                if (this.getter != null) {
                    this.type = this.getter.getReturnType();
                    try {
                        Method m4 = this.clazz.getMethod(setterName, this.type);
                        if (Modifier.isPublic(m4.getModifiers())) {
                            this.setter = m4;
                        }
                    } catch (NoSuchMethodException e4) {
                    }
                } else {
                    Method[] methods = this.clazz.getMethods();
                    int length = methods.length;
                    int i2 = 0;
                    while (true) {
                        if (i2 >= length) {
                            break;
                        }
                        Method m5 = methods[i2];
                        Class<?>[] parameters = m5.getParameterTypes();
                        if (!setterName.equals(m5.getName()) || parameters.length != 1 || !Modifier.isPublic(m5.getModifiers())) {
                            i2++;
                        } else {
                            this.setter = m5;
                            this.type = parameters[0];
                            break;
                        }
                    }
                }
                String propertyGetterName = this.name + BeanAdapter.PROPERTY_SUFFIX;
                try {
                    Method m6 = this.clazz.getMethod(propertyGetterName, new Class[0]);
                    if (Modifier.isPublic(m6.getModifiers())) {
                        this.propertyGetter = m6;
                    } else {
                        this.propertyGetter = null;
                    }
                } catch (NoSuchMethodException e5) {
                }
            } catch (RuntimeException e6) {
                System.err.println("Failed to introspect property " + this.name);
            }
        }
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof PropertyReference)) {
            return false;
        }
        PropertyReference<?> other = (PropertyReference) obj;
        if (this.name != other.name && (this.name == null || !this.name.equals(other.name))) {
            return false;
        }
        if (this.clazz != other.clazz) {
            if (this.clazz == null || !this.clazz.equals(other.clazz)) {
                return false;
            }
            return true;
        }
        return true;
    }

    public int hashCode() {
        int hash = (97 * 5) + (this.name != null ? this.name.hashCode() : 0);
        return (97 * hash) + (this.clazz != null ? this.clazz.hashCode() : 0);
    }
}
