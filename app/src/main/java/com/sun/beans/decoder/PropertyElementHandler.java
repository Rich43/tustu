package com.sun.beans.decoder;

import com.sun.beans.finder.MethodFinder;
import java.beans.IndexedPropertyDescriptor;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import sun.reflect.misc.MethodUtil;

/* loaded from: rt.jar:com/sun/beans/decoder/PropertyElementHandler.class */
final class PropertyElementHandler extends AccessorElementHandler {
    static final String GETTER = "get";
    static final String SETTER = "set";
    private Integer index;

    PropertyElementHandler() {
    }

    @Override // com.sun.beans.decoder.AccessorElementHandler, com.sun.beans.decoder.ElementHandler
    public void addAttribute(String str, String str2) {
        if (str.equals("index")) {
            this.index = Integer.valueOf(str2);
        } else {
            super.addAttribute(str, str2);
        }
    }

    @Override // com.sun.beans.decoder.ElementHandler
    protected boolean isArgument() {
        return false;
    }

    @Override // com.sun.beans.decoder.AccessorElementHandler
    protected Object getValue(String str) {
        try {
            return getPropertyValue(getContextBean(), str, this.index);
        } catch (Exception e2) {
            getOwner().handleException(e2);
            return null;
        }
    }

    @Override // com.sun.beans.decoder.AccessorElementHandler
    protected void setValue(String str, Object obj) {
        try {
            setPropertyValue(getContextBean(), str, this.index, obj);
        } catch (Exception e2) {
            getOwner().handleException(e2);
        }
    }

    private static Object getPropertyValue(Object obj, String str, Integer num) throws IllegalAccessException, NoSuchMethodException, IntrospectionException, InvocationTargetException {
        Class<?> cls = obj.getClass();
        if (num == null) {
            return MethodUtil.invoke(findGetter(cls, str, new Class[0]), obj, new Object[0]);
        }
        return (cls.isArray() && str == null) ? Array.get(obj, num.intValue()) : MethodUtil.invoke(findGetter(cls, str, Integer.TYPE), obj, new Object[]{num});
    }

    private static void setPropertyValue(Object obj, String str, Integer num, Object obj2) throws IllegalAccessException, NoSuchMethodException, ArrayIndexOutOfBoundsException, IllegalArgumentException, InvocationTargetException, IntrospectionException {
        Class<?> cls = obj.getClass();
        Class<?> cls2 = obj2 != null ? obj2.getClass() : null;
        if (num == null) {
            MethodUtil.invoke(findSetter(cls, str, cls2), obj, new Object[]{obj2});
        } else if (cls.isArray() && str == null) {
            Array.set(obj, num.intValue(), obj2);
        } else {
            MethodUtil.invoke(findSetter(cls, str, Integer.TYPE, cls2), obj, new Object[]{num, obj2});
        }
    }

    private static Method findGetter(Class<?> cls, String str, Class<?>... clsArr) throws NoSuchMethodException, IntrospectionException {
        Method indexedReadMethod;
        if (str == null) {
            return MethodFinder.findInstanceMethod(cls, "get", clsArr);
        }
        PropertyDescriptor property = getProperty(cls, str);
        if (clsArr.length == 0) {
            Method readMethod = property.getReadMethod();
            if (readMethod != null) {
                return readMethod;
            }
        } else if ((property instanceof IndexedPropertyDescriptor) && (indexedReadMethod = ((IndexedPropertyDescriptor) property).getIndexedReadMethod()) != null) {
            return indexedReadMethod;
        }
        throw new IntrospectionException("Could not find getter for the " + str + " property");
    }

    private static Method findSetter(Class<?> cls, String str, Class<?>... clsArr) throws NoSuchMethodException, IntrospectionException {
        Method indexedWriteMethod;
        if (str == null) {
            return MethodFinder.findInstanceMethod(cls, "set", clsArr);
        }
        PropertyDescriptor property = getProperty(cls, str);
        if (clsArr.length == 1) {
            Method writeMethod = property.getWriteMethod();
            if (writeMethod != null) {
                return writeMethod;
            }
        } else if ((property instanceof IndexedPropertyDescriptor) && (indexedWriteMethod = ((IndexedPropertyDescriptor) property).getIndexedWriteMethod()) != null) {
            return indexedWriteMethod;
        }
        throw new IntrospectionException("Could not find setter for the " + str + " property");
    }

    private static PropertyDescriptor getProperty(Class<?> cls, String str) throws IntrospectionException {
        for (PropertyDescriptor propertyDescriptor : Introspector.getBeanInfo(cls).getPropertyDescriptors()) {
            if (str.equals(propertyDescriptor.getName())) {
                return propertyDescriptor;
            }
        }
        throw new IntrospectionException("Could not find the " + str + " property descriptor");
    }
}
