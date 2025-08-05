package com.sun.xml.internal.bind.v2.runtime.reflect;

import com.sun.istack.internal.Nullable;
import com.sun.xml.internal.bind.Util;
import com.sun.xml.internal.bind.api.AccessorException;
import com.sun.xml.internal.bind.v2.model.core.Adapter;
import com.sun.xml.internal.bind.v2.runtime.JAXBContextImpl;
import com.sun.xml.internal.bind.v2.runtime.reflect.opt.OptimizedAccessorFactory;
import com.sun.xml.internal.bind.v2.runtime.unmarshaller.Loader;
import com.sun.xml.internal.bind.v2.runtime.unmarshaller.Receiver;
import com.sun.xml.internal.bind.v2.runtime.unmarshaller.UnmarshallingContext;
import java.awt.Image;
import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.URL;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.activation.DataHandler;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.adapters.XmlAdapter;
import javax.xml.datatype.Duration;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.transform.Source;
import org.xml.sax.SAXException;

/* loaded from: rt.jar:com/sun/xml/internal/bind/v2/runtime/reflect/Accessor.class */
public abstract class Accessor<BeanT, ValueT> implements Receiver {
    public final Class<ValueT> valueType;
    private static List<Class> nonAbstractableClasses = Arrays.asList(Object.class, Calendar.class, Duration.class, XMLGregorianCalendar.class, Image.class, DataHandler.class, Source.class, Date.class, File.class, URI.class, URL.class, Class.class, String.class, Source.class);
    private static boolean accessWarned = false;
    private static final Accessor ERROR = new Accessor<Object, Object>(Object.class) { // from class: com.sun.xml.internal.bind.v2.runtime.reflect.Accessor.1
        @Override // com.sun.xml.internal.bind.v2.runtime.reflect.Accessor
        public Object get(Object o2) {
            return null;
        }

        @Override // com.sun.xml.internal.bind.v2.runtime.reflect.Accessor
        public void set(Object o2, Object o1) {
        }
    };
    public static final Accessor<JAXBElement, Object> JAXB_ELEMENT_VALUE = new Accessor<JAXBElement, Object>(Object.class) { // from class: com.sun.xml.internal.bind.v2.runtime.reflect.Accessor.2
        @Override // com.sun.xml.internal.bind.v2.runtime.reflect.Accessor
        public Object get(JAXBElement jaxbElement) {
            return jaxbElement.getValue();
        }

        @Override // com.sun.xml.internal.bind.v2.runtime.reflect.Accessor
        public void set(JAXBElement jaxbElement, Object o2) {
            jaxbElement.setValue(o2);
        }
    };
    private static final Map<Class, Object> uninitializedValues = new HashMap();

    public abstract ValueT get(BeanT beant) throws AccessorException;

    public abstract void set(BeanT beant, ValueT valuet) throws AccessorException;

    public Class<ValueT> getValueType() {
        return this.valueType;
    }

    protected Accessor(Class<ValueT> valueType) {
        this.valueType = valueType;
    }

    public Accessor<BeanT, ValueT> optimize(@Nullable JAXBContextImpl context) {
        return this;
    }

    public Object getUnadapted(BeanT bean) throws AccessorException {
        return get(bean);
    }

    public boolean isAdapted() {
        return false;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public void setUnadapted(BeanT bean, Object obj) throws AccessorException {
        set(bean, obj);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.sun.xml.internal.bind.v2.runtime.unmarshaller.Receiver
    public void receive(UnmarshallingContext.State state, Object o2) throws SAXException {
        try {
            set(state.getTarget(), o2);
        } catch (AccessorException e2) {
            Loader.handleGenericException(e2, true);
        } catch (IllegalAccessError iae) {
            Loader.handleGenericError(iae);
        }
    }

    static {
        uninitializedValues.put(Byte.TYPE, (byte) 0);
        uninitializedValues.put(Boolean.TYPE, false);
        uninitializedValues.put(Character.TYPE, (char) 0);
        uninitializedValues.put(Float.TYPE, Float.valueOf(0.0f));
        uninitializedValues.put(Double.TYPE, Double.valueOf(0.0d));
        uninitializedValues.put(Integer.TYPE, 0);
        uninitializedValues.put(Long.TYPE, 0L);
        uninitializedValues.put(Short.TYPE, (short) 0);
    }

    public boolean isValueTypeAbstractable() {
        return !nonAbstractableClasses.contains(getValueType());
    }

    public boolean isAbstractable(Class clazz) {
        return !nonAbstractableClasses.contains(clazz);
    }

    public final <T> Accessor<BeanT, T> adapt(Class<T> targetType, Class<? extends XmlAdapter<T, ValueT>> adapter) {
        return new AdaptedAccessor(targetType, this, adapter);
    }

    public final <T> Accessor<BeanT, T> adapt(Adapter<Type, Class> adapter) {
        return new AdaptedAccessor((Class) Utils.REFLECTION_NAVIGATOR.erasure(adapter.defaultType), this, adapter.adapterType);
    }

    /* loaded from: rt.jar:com/sun/xml/internal/bind/v2/runtime/reflect/Accessor$FieldReflection.class */
    public static class FieldReflection<BeanT, ValueT> extends Accessor<BeanT, ValueT> {

        /* renamed from: f, reason: collision with root package name */
        public final Field f12068f;
        private static final Logger logger = Util.getClassLogger();

        public FieldReflection(Field f2) {
            this(f2, false);
        }

        public FieldReflection(Field f2, boolean supressAccessorWarnings) {
            super(f2.getType());
            this.f12068f = f2;
            int mod = f2.getModifiers();
            if (!Modifier.isPublic(mod) || Modifier.isFinal(mod) || !Modifier.isPublic(f2.getDeclaringClass().getModifiers())) {
                try {
                    f2.setAccessible(true);
                } catch (SecurityException e2) {
                    if (!Accessor.accessWarned && !supressAccessorWarnings) {
                        logger.log(Level.WARNING, Messages.UNABLE_TO_ACCESS_NON_PUBLIC_FIELD.format(f2.getDeclaringClass().getName(), f2.getName()), (Throwable) e2);
                    }
                    boolean unused = Accessor.accessWarned = true;
                }
            }
        }

        @Override // com.sun.xml.internal.bind.v2.runtime.reflect.Accessor
        public ValueT get(BeanT beant) {
            try {
                return (ValueT) this.f12068f.get(beant);
            } catch (IllegalAccessException e2) {
                throw new IllegalAccessError(e2.getMessage());
            }
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // com.sun.xml.internal.bind.v2.runtime.reflect.Accessor
        public void set(BeanT bean, ValueT value) throws IllegalArgumentException {
            if (value == null) {
                try {
                    value = Accessor.uninitializedValues.get(this.valueType);
                } catch (IllegalAccessException e2) {
                    throw new IllegalAccessError(e2.getMessage());
                }
            }
            this.f12068f.set(bean, value);
        }

        @Override // com.sun.xml.internal.bind.v2.runtime.reflect.Accessor
        public Accessor<BeanT, ValueT> optimize(JAXBContextImpl context) {
            if (context != null && context.fastBoot) {
                return this;
            }
            Accessor<BeanT, ValueT> acc = OptimizedAccessorFactory.get(this.f12068f);
            if (acc != null) {
                return acc;
            }
            return this;
        }
    }

    /* loaded from: rt.jar:com/sun/xml/internal/bind/v2/runtime/reflect/Accessor$ReadOnlyFieldReflection.class */
    public static final class ReadOnlyFieldReflection<BeanT, ValueT> extends FieldReflection<BeanT, ValueT> {
        public ReadOnlyFieldReflection(Field f2, boolean supressAccessorWarnings) {
            super(f2, supressAccessorWarnings);
        }

        public ReadOnlyFieldReflection(Field f2) {
            super(f2);
        }

        @Override // com.sun.xml.internal.bind.v2.runtime.reflect.Accessor.FieldReflection, com.sun.xml.internal.bind.v2.runtime.reflect.Accessor
        public void set(BeanT bean, ValueT value) {
        }

        @Override // com.sun.xml.internal.bind.v2.runtime.reflect.Accessor.FieldReflection, com.sun.xml.internal.bind.v2.runtime.reflect.Accessor
        public Accessor<BeanT, ValueT> optimize(JAXBContextImpl context) {
            return this;
        }
    }

    /* loaded from: rt.jar:com/sun/xml/internal/bind/v2/runtime/reflect/Accessor$GetterSetterReflection.class */
    public static class GetterSetterReflection<BeanT, ValueT> extends Accessor<BeanT, ValueT> {
        public final Method getter;
        public final Method setter;
        private static final Logger logger = Util.getClassLogger();

        /* JADX WARN: Illegal instructions before constructor call */
        public GetterSetterReflection(Method getter, Method setter) {
            Class<?> returnType;
            if (getter != null) {
                returnType = getter.getReturnType();
            } else {
                returnType = setter.getParameterTypes()[0];
            }
            super(returnType);
            this.getter = getter;
            this.setter = setter;
            if (getter != null) {
                makeAccessible(getter);
            }
            if (setter != null) {
                makeAccessible(setter);
            }
        }

        private void makeAccessible(Method m2) {
            if (!Modifier.isPublic(m2.getModifiers()) || !Modifier.isPublic(m2.getDeclaringClass().getModifiers())) {
                try {
                    m2.setAccessible(true);
                } catch (SecurityException e2) {
                    if (!Accessor.accessWarned) {
                        logger.log(Level.WARNING, Messages.UNABLE_TO_ACCESS_NON_PUBLIC_FIELD.format(m2.getDeclaringClass().getName(), m2.getName()), (Throwable) e2);
                    }
                    boolean unused = Accessor.accessWarned = true;
                }
            }
        }

        @Override // com.sun.xml.internal.bind.v2.runtime.reflect.Accessor
        public ValueT get(BeanT beant) throws AccessorException {
            try {
                return (ValueT) this.getter.invoke(beant, new Object[0]);
            } catch (IllegalAccessException e2) {
                throw new IllegalAccessError(e2.getMessage());
            } catch (InvocationTargetException e3) {
                throw handleInvocationTargetException(e3);
            }
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // com.sun.xml.internal.bind.v2.runtime.reflect.Accessor
        public void set(BeanT bean, ValueT value) throws AccessorException, IllegalArgumentException {
            if (value == null) {
                try {
                    value = Accessor.uninitializedValues.get(this.valueType);
                } catch (IllegalAccessException e2) {
                    throw new IllegalAccessError(e2.getMessage());
                } catch (InvocationTargetException e3) {
                    throw handleInvocationTargetException(e3);
                }
            }
            this.setter.invoke(bean, value);
        }

        private AccessorException handleInvocationTargetException(InvocationTargetException e2) {
            Throwable t2 = e2.getTargetException();
            if (t2 instanceof RuntimeException) {
                throw ((RuntimeException) t2);
            }
            if (t2 instanceof Error) {
                throw ((Error) t2);
            }
            return new AccessorException(t2);
        }

        @Override // com.sun.xml.internal.bind.v2.runtime.reflect.Accessor
        public Accessor<BeanT, ValueT> optimize(JAXBContextImpl context) {
            if (this.getter == null || this.setter == null) {
                return this;
            }
            if (context != null && context.fastBoot) {
                return this;
            }
            Accessor<BeanT, ValueT> acc = OptimizedAccessorFactory.get(this.getter, this.setter);
            if (acc != null) {
                return acc;
            }
            return this;
        }
    }

    /* loaded from: rt.jar:com/sun/xml/internal/bind/v2/runtime/reflect/Accessor$GetterOnlyReflection.class */
    public static class GetterOnlyReflection<BeanT, ValueT> extends GetterSetterReflection<BeanT, ValueT> {
        public GetterOnlyReflection(Method getter) {
            super(getter, null);
        }

        @Override // com.sun.xml.internal.bind.v2.runtime.reflect.Accessor.GetterSetterReflection, com.sun.xml.internal.bind.v2.runtime.reflect.Accessor
        public void set(BeanT bean, ValueT value) throws AccessorException {
            throw new AccessorException(Messages.NO_SETTER.format(this.getter.toString()));
        }
    }

    /* loaded from: rt.jar:com/sun/xml/internal/bind/v2/runtime/reflect/Accessor$SetterOnlyReflection.class */
    public static class SetterOnlyReflection<BeanT, ValueT> extends GetterSetterReflection<BeanT, ValueT> {
        public SetterOnlyReflection(Method setter) {
            super(null, setter);
        }

        @Override // com.sun.xml.internal.bind.v2.runtime.reflect.Accessor.GetterSetterReflection, com.sun.xml.internal.bind.v2.runtime.reflect.Accessor
        public ValueT get(BeanT bean) throws AccessorException {
            throw new AccessorException(Messages.NO_GETTER.format(this.setter.toString()));
        }
    }

    public static <A, B> Accessor<A, B> getErrorInstance() {
        return ERROR;
    }
}
