package java.beans;

import java.awt.Component;
import java.awt.event.ComponentListener;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.EventListener;
import java.util.Objects;
import javax.swing.JMenuItem;
import javax.swing.event.ChangeListener;
import sun.reflect.misc.MethodUtil;
import sun.reflect.misc.ReflectUtil;

/* loaded from: rt.jar:java/beans/DefaultPersistenceDelegate.class */
public class DefaultPersistenceDelegate extends PersistenceDelegate {
    private static final String[] EMPTY = new String[0];
    private final String[] constructor;
    private Boolean definesEquals;

    public DefaultPersistenceDelegate() {
        this.constructor = EMPTY;
    }

    public DefaultPersistenceDelegate(String[] strArr) {
        this.constructor = strArr == null ? EMPTY : (String[]) strArr.clone();
    }

    private static boolean definesEquals(Class<?> cls) {
        try {
            return cls == cls.getMethod("equals", Object.class).getDeclaringClass();
        } catch (NoSuchMethodException e2) {
            return false;
        }
    }

    private boolean definesEquals(Object obj) {
        if (this.definesEquals != null) {
            return this.definesEquals == Boolean.TRUE;
        }
        boolean zDefinesEquals = definesEquals(obj.getClass());
        this.definesEquals = zDefinesEquals ? Boolean.TRUE : Boolean.FALSE;
        return zDefinesEquals;
    }

    @Override // java.beans.PersistenceDelegate
    protected boolean mutatesTo(Object obj, Object obj2) {
        if (this.constructor.length == 0 || !definesEquals(obj)) {
            return super.mutatesTo(obj, obj2);
        }
        return obj.equals(obj2);
    }

    @Override // java.beans.PersistenceDelegate
    protected Expression instantiate(Object obj, Encoder encoder) {
        int length = this.constructor.length;
        Class<?> cls = obj.getClass();
        Object[] objArr = new Object[length];
        for (int i2 = 0; i2 < length; i2++) {
            try {
                objArr[i2] = MethodUtil.invoke(findMethod(cls, this.constructor[i2]), obj, new Object[0]);
            } catch (Exception e2) {
                encoder.getExceptionListener().exceptionThrown(e2);
            }
        }
        return new Expression(obj, obj.getClass(), "new", objArr);
    }

    private Method findMethod(Class<?> cls, String str) {
        if (str == null) {
            throw new IllegalArgumentException("Property name is null");
        }
        PropertyDescriptor propertyDescriptor = getPropertyDescriptor(cls, str);
        if (propertyDescriptor == null) {
            throw new IllegalStateException("Could not find property by the name " + str);
        }
        Method readMethod = propertyDescriptor.getReadMethod();
        if (readMethod == null) {
            throw new IllegalStateException("Could not find getter for the property " + str);
        }
        return readMethod;
    }

    private void doProperty(Class<?> cls, PropertyDescriptor propertyDescriptor, Object obj, Object obj2, Encoder encoder) throws Exception {
        Method readMethod = propertyDescriptor.getReadMethod();
        Method writeMethod = propertyDescriptor.getWriteMethod();
        if (readMethod != null && writeMethod != null) {
            Expression expression = new Expression(obj, readMethod.getName(), new Object[0]);
            Expression expression2 = new Expression(obj2, readMethod.getName(), new Object[0]);
            Object value = expression.getValue();
            Object value2 = expression2.getValue();
            encoder.writeExpression(expression);
            if (!Objects.equals(value2, encoder.get(value))) {
                Object[] objArr = (Object[]) propertyDescriptor.getValue("enumerationValues");
                if ((objArr instanceof Object[]) && Array.getLength(objArr) % 3 == 0) {
                    Object[] objArr2 = objArr;
                    int i2 = 0;
                    while (true) {
                        int i3 = i2;
                        if (i3 >= objArr2.length) {
                            break;
                        }
                        try {
                            Field field = cls.getField((String) objArr2[i3]);
                            if (field.get(null).equals(value)) {
                                encoder.remove(value);
                                encoder.writeExpression(new Expression(value, field, "get", new Object[]{null}));
                            }
                        } catch (Exception e2) {
                        }
                        i2 = i3 + 3;
                    }
                }
                invokeStatement(obj, writeMethod.getName(), new Object[]{value}, encoder);
            }
        }
    }

    static void invokeStatement(Object obj, String str, Object[] objArr, Encoder encoder) {
        encoder.writeStatement(new Statement(obj, str, objArr));
    }

    private void initBean(Class<?> cls, Object obj, Object obj2, Encoder encoder) throws SecurityException {
        Class<?> listenerType;
        EventListener[] eventListenerArr;
        EventListener[] eventListenerArr2;
        for (Field field : cls.getFields()) {
            if (ReflectUtil.isPackageAccessible(field.getDeclaringClass())) {
                int modifiers = field.getModifiers();
                if (!Modifier.isFinal(modifiers) && !Modifier.isStatic(modifiers) && !Modifier.isTransient(modifiers)) {
                    try {
                        Expression expression = new Expression(field, "get", new Object[]{obj});
                        Expression expression2 = new Expression(field, "get", new Object[]{obj2});
                        Object value = expression.getValue();
                        Object value2 = expression2.getValue();
                        encoder.writeExpression(expression);
                        if (!Objects.equals(value2, encoder.get(value))) {
                            encoder.writeStatement(new Statement(field, "set", new Object[]{obj, value}));
                        }
                    } catch (Exception e2) {
                        encoder.getExceptionListener().exceptionThrown(e2);
                    }
                }
            }
        }
        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(cls);
            for (PropertyDescriptor propertyDescriptor : beanInfo.getPropertyDescriptors()) {
                if (!propertyDescriptor.isTransient()) {
                    try {
                        doProperty(cls, propertyDescriptor, obj, obj2, encoder);
                    } catch (Exception e3) {
                        encoder.getExceptionListener().exceptionThrown(e3);
                    }
                }
            }
            if (!Component.class.isAssignableFrom(cls)) {
                return;
            }
            for (EventSetDescriptor eventSetDescriptor : beanInfo.getEventSetDescriptors()) {
                if (!eventSetDescriptor.isTransient() && (listenerType = eventSetDescriptor.getListenerType()) != ComponentListener.class && (listenerType != ChangeListener.class || cls != JMenuItem.class)) {
                    EventListener[] eventListenerArr3 = new EventListener[0];
                    EventListener[] eventListenerArr4 = new EventListener[0];
                    try {
                        Method getListenerMethod = eventSetDescriptor.getGetListenerMethod();
                        eventListenerArr = (EventListener[]) MethodUtil.invoke(getListenerMethod, obj, new Object[0]);
                        eventListenerArr2 = (EventListener[]) MethodUtil.invoke(getListenerMethod, obj2, new Object[0]);
                    } catch (Exception e4) {
                        try {
                            Method method = cls.getMethod("getListeners", Class.class);
                            eventListenerArr = (EventListener[]) MethodUtil.invoke(method, obj, new Object[]{listenerType});
                            eventListenerArr2 = (EventListener[]) MethodUtil.invoke(method, obj2, new Object[]{listenerType});
                        } catch (Exception e5) {
                            return;
                        }
                    }
                    String name = eventSetDescriptor.getAddListenerMethod().getName();
                    for (int length = eventListenerArr2.length; length < eventListenerArr.length; length++) {
                        invokeStatement(obj, name, new Object[]{eventListenerArr[length]}, encoder);
                    }
                    String name2 = eventSetDescriptor.getRemoveListenerMethod().getName();
                    for (int length2 = eventListenerArr.length; length2 < eventListenerArr2.length; length2++) {
                        invokeStatement(obj, name2, new Object[]{eventListenerArr2[length2]}, encoder);
                    }
                }
            }
        } catch (IntrospectionException e6) {
        }
    }

    @Override // java.beans.PersistenceDelegate
    protected void initialize(Class<?> cls, Object obj, Object obj2, Encoder encoder) {
        super.initialize(cls, obj, obj2, encoder);
        if (obj.getClass() == cls) {
            initBean(cls, obj, obj2, encoder);
        }
    }

    private static PropertyDescriptor getPropertyDescriptor(Class<?> cls, String str) {
        try {
            for (PropertyDescriptor propertyDescriptor : Introspector.getBeanInfo(cls).getPropertyDescriptors()) {
                if (str.equals(propertyDescriptor.getName())) {
                    return propertyDescriptor;
                }
            }
            return null;
        } catch (IntrospectionException e2) {
            return null;
        }
    }
}
