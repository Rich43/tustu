package com.sun.xml.internal.ws.spi.db;

import com.sun.javafx.fxml.BeanAdapter;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.security.AccessController;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.namespace.QName;

/* loaded from: rt.jar:com/sun/xml/internal/ws/spi/db/JAXBWrapperAccessor.class */
public class JAXBWrapperAccessor extends WrapperAccessor {
    protected Class<?> contentClass;
    protected HashMap<Object, Class> elementDeclaredTypes;

    public JAXBWrapperAccessor(Class<?> wrapperBean) throws SecurityException {
        String lowerCase;
        this.contentClass = wrapperBean;
        HashMap<Object, PropertySetter> setByQName = new HashMap<>();
        HashMap<Object, PropertySetter> setByLocalpart = new HashMap<>();
        HashMap<String, Method> publicSetters = new HashMap<>();
        HashMap<Object, PropertyGetter> getByQName = new HashMap<>();
        HashMap<Object, PropertyGetter> getByLocalpart = new HashMap<>();
        HashMap<String, Method> publicGetters = new HashMap<>();
        HashMap<Object, Class> elementDeclaredTypesByQName = new HashMap<>();
        HashMap<Object, Class> elementDeclaredTypesByLocalpart = new HashMap<>();
        for (Method method : this.contentClass.getMethods()) {
            if (PropertySetterBase.setterPattern(method)) {
                String key = method.getName().substring(3, method.getName().length()).toLowerCase();
                publicSetters.put(key, method);
            }
            if (PropertyGetterBase.getterPattern(method)) {
                String methodName = method.getName();
                if (methodName.startsWith(BeanAdapter.IS_PREFIX)) {
                    lowerCase = methodName.substring(2, method.getName().length()).toLowerCase();
                } else {
                    lowerCase = methodName.substring(3, method.getName().length()).toLowerCase();
                }
                String key2 = lowerCase;
                publicGetters.put(key2, method);
            }
        }
        HashSet<String> elementLocalNames = new HashSet<>();
        for (Field field : getAllFields(this.contentClass)) {
            XmlElementWrapper xmlElemWrapper = (XmlElementWrapper) field.getAnnotation(XmlElementWrapper.class);
            XmlElement xmlElem = (XmlElement) field.getAnnotation(XmlElement.class);
            XmlElementRef xmlElemRef = (XmlElementRef) field.getAnnotation(XmlElementRef.class);
            String fieldName = field.getName().toLowerCase();
            String namespace = "";
            String localName = field.getName();
            if (xmlElemWrapper != null) {
                namespace = xmlElemWrapper.namespace();
                if (xmlElemWrapper.name() != null && !xmlElemWrapper.name().equals("") && !xmlElemWrapper.name().equals("##default")) {
                    localName = xmlElemWrapper.name();
                }
            } else if (xmlElem != null) {
                namespace = xmlElem.namespace();
                if (xmlElem.name() != null && !xmlElem.name().equals("") && !xmlElem.name().equals("##default")) {
                    localName = xmlElem.name();
                }
            } else if (xmlElemRef != null) {
                namespace = xmlElemRef.namespace();
                if (xmlElemRef.name() != null && !xmlElemRef.name().equals("") && !xmlElemRef.name().equals("##default")) {
                    localName = xmlElemRef.name();
                }
            }
            if (elementLocalNames.contains(localName)) {
                this.elementLocalNameCollision = true;
            } else {
                elementLocalNames.add(localName);
            }
            QName qname = new QName(namespace, localName);
            if (field.getType().equals(JAXBElement.class) && (field.getGenericType() instanceof ParameterizedType)) {
                Type arg = ((ParameterizedType) field.getGenericType()).getActualTypeArguments()[0];
                if (arg instanceof Class) {
                    elementDeclaredTypesByQName.put(qname, (Class) arg);
                    elementDeclaredTypesByLocalpart.put(localName, (Class) arg);
                } else if (arg instanceof GenericArrayType) {
                    Type componentType = ((GenericArrayType) arg).getGenericComponentType();
                    if (componentType instanceof Class) {
                        Class arrayClass = Array.newInstance((Class<?>) componentType, 0).getClass();
                        elementDeclaredTypesByQName.put(qname, arrayClass);
                        elementDeclaredTypesByLocalpart.put(localName, arrayClass);
                    }
                }
            }
            if (fieldName.startsWith("_") && !localName.startsWith("_")) {
                fieldName = fieldName.substring(1);
            }
            Method setMethod = publicSetters.get(fieldName);
            Method getMethod = publicGetters.get(fieldName);
            PropertySetter setter = createPropertySetter(field, setMethod);
            PropertyGetter getter = createPropertyGetter(field, getMethod);
            setByQName.put(qname, setter);
            setByLocalpart.put(localName, setter);
            getByQName.put(qname, getter);
            getByLocalpart.put(localName, getter);
        }
        if (this.elementLocalNameCollision) {
            this.propertySetters = setByQName;
            this.propertyGetters = getByQName;
            this.elementDeclaredTypes = elementDeclaredTypesByQName;
        } else {
            this.propertySetters = setByLocalpart;
            this.propertyGetters = getByLocalpart;
            this.elementDeclaredTypes = elementDeclaredTypesByLocalpart;
        }
    }

    protected static List<Field> getAllFields(Class<?> clz) {
        List<Field> list = new ArrayList<>();
        while (!Object.class.equals(clz)) {
            list.addAll(Arrays.asList(getDeclaredFields(clz)));
            clz = clz.getSuperclass();
        }
        return list;
    }

    protected static Field[] getDeclaredFields(final Class<?> clz) {
        try {
            return System.getSecurityManager() == null ? clz.getDeclaredFields() : (Field[]) AccessController.doPrivileged(new PrivilegedExceptionAction<Field[]>() { // from class: com.sun.xml.internal.ws.spi.db.JAXBWrapperAccessor.1
                /* JADX WARN: Can't rename method to resolve collision */
                @Override // java.security.PrivilegedExceptionAction
                public Field[] run() throws IllegalAccessException {
                    return clz.getDeclaredFields();
                }
            });
        } catch (PrivilegedActionException e2) {
            e2.printStackTrace();
            return null;
        }
    }

    protected static PropertyGetter createPropertyGetter(Field field, Method getMethod) {
        if (!field.isAccessible() && getMethod != null) {
            MethodGetter methodGetter = new MethodGetter(getMethod);
            if (methodGetter.getType().toString().equals(field.getType().toString())) {
                return methodGetter;
            }
        }
        return new FieldGetter(field);
    }

    protected static PropertySetter createPropertySetter(Field field, Method setter) {
        if (!field.isAccessible() && setter != null) {
            MethodSetter injection = new MethodSetter(setter);
            if (injection.getType().toString().equals(field.getType().toString())) {
                return injection;
            }
        }
        return new FieldSetter(field);
    }

    private Class getElementDeclaredType(QName name) {
        Object key = this.elementLocalNameCollision ? name : name.getLocalPart();
        return this.elementDeclaredTypes.get(key);
    }

    @Override // com.sun.xml.internal.ws.spi.db.WrapperAccessor
    public PropertyAccessor getPropertyAccessor(String ns, String name) {
        final QName n2 = new QName(ns, name);
        final PropertySetter setter = getPropertySetter(n2);
        final PropertyGetter getter = getPropertyGetter(n2);
        final boolean isJAXBElement = setter.getType().equals(JAXBElement.class);
        final boolean isListType = List.class.isAssignableFrom(setter.getType());
        final Class elementDeclaredType = isJAXBElement ? getElementDeclaredType(n2) : null;
        return new PropertyAccessor() { // from class: com.sun.xml.internal.ws.spi.db.JAXBWrapperAccessor.2
            @Override // com.sun.xml.internal.ws.spi.db.PropertyAccessor
            public Object get(Object bean) throws DatabindingException {
                Object val;
                if (isJAXBElement) {
                    JAXBElement<Object> jaxbElement = (JAXBElement) getter.get(bean);
                    val = jaxbElement == null ? null : jaxbElement.getValue();
                } else {
                    val = getter.get(bean);
                }
                if (val == null && isListType) {
                    val = new ArrayList();
                    set(bean, val);
                }
                return val;
            }

            @Override // com.sun.xml.internal.ws.spi.db.PropertyAccessor
            public void set(Object bean, Object value) throws DatabindingException {
                if (isJAXBElement) {
                    JAXBElement<Object> jaxbElement = new JAXBElement<>(n2, elementDeclaredType, JAXBWrapperAccessor.this.contentClass, value);
                    setter.set(bean, jaxbElement);
                } else {
                    setter.set(bean, value);
                }
            }
        };
    }
}
