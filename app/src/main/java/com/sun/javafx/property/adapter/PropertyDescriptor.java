package com.sun.javafx.property.adapter;

import com.sun.javafx.property.adapter.ReadOnlyPropertyDescriptor;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyVetoException;
import java.beans.VetoableChangeListener;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import javafx.beans.property.Property;
import javafx.beans.property.adapter.ReadOnlyJavaBeanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import sun.reflect.misc.MethodUtil;

/* loaded from: jfxrt.jar:com/sun/javafx/property/adapter/PropertyDescriptor.class */
public class PropertyDescriptor extends ReadOnlyPropertyDescriptor {
    private static final String ADD_VETOABLE_LISTENER_METHOD_NAME = "addVetoableChangeListener";
    private static final String REMOVE_VETOABLE_LISTENER_METHOD_NAME = "removeVetoableChangeListener";
    private static final String ADD_PREFIX = "add";
    private static final String REMOVE_PREFIX = "remove";
    private static final String SUFFIX = "Listener";
    private static final int ADD_VETOABLE_LISTENER_TAKES_NAME = 1;
    private static final int REMOVE_VETOABLE_LISTENER_TAKES_NAME = 2;
    private final Method setter;
    private final Method addVetoListener;
    private final Method removeVetoListener;
    private final int flags;

    public Method getSetter() {
        return this.setter;
    }

    public PropertyDescriptor(String propertyName, Class<?> beanClass, Method getter, Method setter) throws SecurityException {
        super(propertyName, beanClass, getter);
        this.setter = setter;
        Method tmpAddVetoListener = null;
        Method tmpRemoveVetoListener = null;
        int tmpFlags = 0;
        String addMethodName = ADD_PREFIX + capitalizedName(this.name) + SUFFIX;
        try {
            tmpAddVetoListener = beanClass.getMethod(addMethodName, VetoableChangeListener.class);
        } catch (NoSuchMethodException e2) {
            try {
                tmpAddVetoListener = beanClass.getMethod(ADD_VETOABLE_LISTENER_METHOD_NAME, String.class, VetoableChangeListener.class);
                tmpFlags = 0 | 1;
            } catch (NoSuchMethodException e3) {
                try {
                    tmpAddVetoListener = beanClass.getMethod(ADD_VETOABLE_LISTENER_METHOD_NAME, VetoableChangeListener.class);
                } catch (NoSuchMethodException e4) {
                }
            }
        }
        String removeMethodName = REMOVE_PREFIX + capitalizedName(this.name) + SUFFIX;
        try {
            tmpRemoveVetoListener = beanClass.getMethod(removeMethodName, VetoableChangeListener.class);
        } catch (NoSuchMethodException e5) {
            try {
                tmpRemoveVetoListener = beanClass.getMethod(REMOVE_VETOABLE_LISTENER_METHOD_NAME, String.class, VetoableChangeListener.class);
                tmpFlags |= 2;
            } catch (NoSuchMethodException e6) {
                try {
                    tmpRemoveVetoListener = beanClass.getMethod(REMOVE_VETOABLE_LISTENER_METHOD_NAME, VetoableChangeListener.class);
                } catch (NoSuchMethodException e7) {
                }
            }
        }
        this.addVetoListener = tmpAddVetoListener;
        this.removeVetoListener = tmpRemoveVetoListener;
        this.flags = tmpFlags;
    }

    @Override // com.sun.javafx.property.adapter.ReadOnlyPropertyDescriptor
    public void addListener(ReadOnlyPropertyDescriptor.ReadOnlyListener listener) throws IllegalArgumentException {
        super.addListener(listener);
        if (this.addVetoListener != null) {
            try {
                if ((this.flags & 1) > 0) {
                    this.addVetoListener.invoke(listener.getBean(), this.name, listener);
                } else {
                    this.addVetoListener.invoke(listener.getBean(), listener);
                }
            } catch (IllegalAccessException e2) {
            } catch (InvocationTargetException e3) {
            }
        }
    }

    @Override // com.sun.javafx.property.adapter.ReadOnlyPropertyDescriptor
    public void removeListener(ReadOnlyPropertyDescriptor.ReadOnlyListener listener) throws IllegalArgumentException {
        super.removeListener(listener);
        if (this.removeVetoListener != null) {
            try {
                if ((this.flags & 2) > 0) {
                    this.removeVetoListener.invoke(listener.getBean(), this.name, listener);
                } else {
                    this.removeVetoListener.invoke(listener.getBean(), listener);
                }
            } catch (IllegalAccessException e2) {
            } catch (InvocationTargetException e3) {
            }
        }
    }

    /* loaded from: jfxrt.jar:com/sun/javafx/property/adapter/PropertyDescriptor$Listener.class */
    public class Listener<T> extends ReadOnlyPropertyDescriptor.ReadOnlyListener<T> implements ChangeListener<T>, VetoableChangeListener {
        private boolean updating;

        public Listener(Object bean, ReadOnlyJavaBeanProperty<T> property) {
            super(bean, property);
        }

        @Override // javafx.beans.value.ChangeListener
        public void changed(ObservableValue<? extends T> observable, T oldValue, T newValue) {
            ReadOnlyJavaBeanProperty<T> property = checkRef();
            if (property == null) {
                observable.removeListener(this);
                return;
            }
            if (this.updating) {
                return;
            }
            this.updating = true;
            try {
                MethodUtil.invoke(PropertyDescriptor.this.setter, this.bean, new Object[]{newValue});
                property.fireValueChangedEvent();
                this.updating = false;
            } catch (IllegalAccessException e2) {
                this.updating = false;
            } catch (InvocationTargetException e3) {
                this.updating = false;
            } catch (Throwable th) {
                this.updating = false;
                throw th;
            }
        }

        @Override // java.beans.VetoableChangeListener
        public void vetoableChange(PropertyChangeEvent propertyChangeEvent) throws PropertyVetoException {
            if (this.bean.equals(propertyChangeEvent.getSource()) && PropertyDescriptor.this.name.equals(propertyChangeEvent.getPropertyName())) {
                ReadOnlyJavaBeanProperty<T> property = checkRef();
                if ((property instanceof Property) && ((Property) property).isBound() && !this.updating) {
                    throw new PropertyVetoException("A bound value cannot be set.", propertyChangeEvent);
                }
            }
        }
    }
}
