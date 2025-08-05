package java.beans;

import com.sun.beans.finder.PrimitiveWrapperMap;
import com.sun.javafx.fxml.BeanAdapter;
import com.sun.org.apache.xalan.internal.xsltc.compiler.Constants;
import java.awt.AWTKeyStroke;
import java.awt.BorderLayout;
import java.awt.Choice;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Font;
import java.awt.Insets;
import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuShortcut;
import java.awt.Window;
import java.awt.font.TextAttribute;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Proxy;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.Vector;
import java.util.WeakHashMap;
import javax.swing.Box;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.Icon;
import javax.swing.JLayeredPane;
import javax.swing.JMenu;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.ToolTipManager;
import javax.swing.border.MatteBorder;
import javax.swing.plaf.ColorUIResource;
import javax.swing.tree.DefaultMutableTreeNode;
import sun.reflect.misc.ReflectUtil;

/* loaded from: rt.jar:java/beans/MetaData.class */
class MetaData {
    private static final Map<String, Field> fields = Collections.synchronizedMap(new WeakHashMap());
    private static Hashtable<String, PersistenceDelegate> internalPersistenceDelegates = new Hashtable<>();
    private static PersistenceDelegate nullPersistenceDelegate = new NullPersistenceDelegate();
    private static PersistenceDelegate enumPersistenceDelegate = new EnumPersistenceDelegate();
    private static PersistenceDelegate primitivePersistenceDelegate = new PrimitivePersistenceDelegate();
    private static PersistenceDelegate defaultPersistenceDelegate = new DefaultPersistenceDelegate();
    private static PersistenceDelegate arrayPersistenceDelegate;
    private static PersistenceDelegate proxyPersistenceDelegate;

    MetaData() {
    }

    /* loaded from: rt.jar:java/beans/MetaData$NullPersistenceDelegate.class */
    static final class NullPersistenceDelegate extends PersistenceDelegate {
        NullPersistenceDelegate() {
        }

        @Override // java.beans.PersistenceDelegate
        protected void initialize(Class<?> cls, Object obj, Object obj2, Encoder encoder) {
        }

        @Override // java.beans.PersistenceDelegate
        protected Expression instantiate(Object obj, Encoder encoder) {
            return null;
        }

        @Override // java.beans.PersistenceDelegate
        public void writeObject(Object obj, Encoder encoder) {
        }
    }

    /* loaded from: rt.jar:java/beans/MetaData$EnumPersistenceDelegate.class */
    static final class EnumPersistenceDelegate extends PersistenceDelegate {
        EnumPersistenceDelegate() {
        }

        @Override // java.beans.PersistenceDelegate
        protected boolean mutatesTo(Object obj, Object obj2) {
            return obj == obj2;
        }

        @Override // java.beans.PersistenceDelegate
        protected Expression instantiate(Object obj, Encoder encoder) {
            Enum r0 = (Enum) obj;
            return new Expression(r0, Enum.class, BeanAdapter.VALUE_OF_METHOD_NAME, new Object[]{r0.getDeclaringClass(), r0.name()});
        }
    }

    /* loaded from: rt.jar:java/beans/MetaData$PrimitivePersistenceDelegate.class */
    static final class PrimitivePersistenceDelegate extends PersistenceDelegate {
        PrimitivePersistenceDelegate() {
        }

        @Override // java.beans.PersistenceDelegate
        protected boolean mutatesTo(Object obj, Object obj2) {
            return obj.equals(obj2);
        }

        @Override // java.beans.PersistenceDelegate
        protected Expression instantiate(Object obj, Encoder encoder) {
            return new Expression(obj, obj.getClass(), "new", new Object[]{obj.toString()});
        }
    }

    /* loaded from: rt.jar:java/beans/MetaData$ArrayPersistenceDelegate.class */
    static final class ArrayPersistenceDelegate extends PersistenceDelegate {
        ArrayPersistenceDelegate() {
        }

        @Override // java.beans.PersistenceDelegate
        protected boolean mutatesTo(Object obj, Object obj2) {
            return obj2 != null && obj.getClass() == obj2.getClass() && Array.getLength(obj) == Array.getLength(obj2);
        }

        @Override // java.beans.PersistenceDelegate
        protected Expression instantiate(Object obj, Encoder encoder) {
            return new Expression(obj, Array.class, "newInstance", new Object[]{obj.getClass().getComponentType(), new Integer(Array.getLength(obj))});
        }

        @Override // java.beans.PersistenceDelegate
        protected void initialize(Class<?> cls, Object obj, Object obj2, Encoder encoder) throws IllegalArgumentException {
            int length = Array.getLength(obj);
            for (int i2 = 0; i2 < length; i2++) {
                Integer num = new Integer(i2);
                Expression expression = new Expression(obj, "get", new Object[]{num});
                Expression expression2 = new Expression(obj2, "get", new Object[]{num});
                try {
                    Object value = expression.getValue();
                    Object value2 = expression2.getValue();
                    encoder.writeExpression(expression);
                    if (!Objects.equals(value2, encoder.get(value))) {
                        DefaultPersistenceDelegate.invokeStatement(obj, "set", new Object[]{num, value}, encoder);
                    }
                } catch (Exception e2) {
                    encoder.getExceptionListener().exceptionThrown(e2);
                }
            }
        }
    }

    /* loaded from: rt.jar:java/beans/MetaData$ProxyPersistenceDelegate.class */
    static final class ProxyPersistenceDelegate extends PersistenceDelegate {
        ProxyPersistenceDelegate() {
        }

        @Override // java.beans.PersistenceDelegate
        protected Expression instantiate(Object obj, Encoder encoder) throws IllegalArgumentException {
            Class<?> cls = obj.getClass();
            InvocationHandler invocationHandler = Proxy.getInvocationHandler((Proxy) obj);
            if (invocationHandler instanceof EventHandler) {
                EventHandler eventHandler = (EventHandler) invocationHandler;
                Vector vector = new Vector();
                vector.add(cls.getInterfaces()[0]);
                vector.add(eventHandler.getTarget());
                vector.add(eventHandler.getAction());
                if (eventHandler.getEventPropertyName() != null) {
                    vector.add(eventHandler.getEventPropertyName());
                }
                if (eventHandler.getListenerMethodName() != null) {
                    vector.setSize(4);
                    vector.add(eventHandler.getListenerMethodName());
                }
                return new Expression(obj, EventHandler.class, "create", vector.toArray());
            }
            return new Expression(obj, Proxy.class, "newProxyInstance", new Object[]{cls.getClassLoader(), cls.getInterfaces(), invocationHandler});
        }
    }

    /* loaded from: rt.jar:java/beans/MetaData$java_lang_String_PersistenceDelegate.class */
    static final class java_lang_String_PersistenceDelegate extends PersistenceDelegate {
        java_lang_String_PersistenceDelegate() {
        }

        @Override // java.beans.PersistenceDelegate
        protected Expression instantiate(Object obj, Encoder encoder) {
            return null;
        }

        @Override // java.beans.PersistenceDelegate
        public void writeObject(Object obj, Encoder encoder) {
        }
    }

    /* loaded from: rt.jar:java/beans/MetaData$java_lang_Class_PersistenceDelegate.class */
    static final class java_lang_Class_PersistenceDelegate extends PersistenceDelegate {
        java_lang_Class_PersistenceDelegate() {
        }

        @Override // java.beans.PersistenceDelegate
        protected boolean mutatesTo(Object obj, Object obj2) {
            return obj.equals(obj2);
        }

        @Override // java.beans.PersistenceDelegate
        protected Expression instantiate(Object obj, Encoder encoder) throws SecurityException {
            Class cls = (Class) obj;
            if (cls.isPrimitive()) {
                Field declaredField = null;
                try {
                    declaredField = PrimitiveWrapperMap.getType(cls.getName()).getDeclaredField("TYPE");
                } catch (NoSuchFieldException e2) {
                    System.err.println("Unknown primitive type: " + ((Object) cls));
                }
                return new Expression(obj, declaredField, "get", new Object[]{null});
            }
            if (obj == String.class) {
                return new Expression(obj, "", "getClass", new Object[0]);
            }
            if (obj == Class.class) {
                return new Expression(obj, String.class, "getClass", new Object[0]);
            }
            Expression expression = new Expression(obj, Class.class, "forName", new Object[]{cls.getName()});
            expression.loader = cls.getClassLoader();
            return expression;
        }
    }

    /* loaded from: rt.jar:java/beans/MetaData$java_lang_reflect_Field_PersistenceDelegate.class */
    static final class java_lang_reflect_Field_PersistenceDelegate extends PersistenceDelegate {
        java_lang_reflect_Field_PersistenceDelegate() {
        }

        @Override // java.beans.PersistenceDelegate
        protected boolean mutatesTo(Object obj, Object obj2) {
            return obj.equals(obj2);
        }

        @Override // java.beans.PersistenceDelegate
        protected Expression instantiate(Object obj, Encoder encoder) {
            Field field = (Field) obj;
            return new Expression(obj, field.getDeclaringClass(), "getField", new Object[]{field.getName()});
        }
    }

    /* loaded from: rt.jar:java/beans/MetaData$java_lang_reflect_Method_PersistenceDelegate.class */
    static final class java_lang_reflect_Method_PersistenceDelegate extends PersistenceDelegate {
        java_lang_reflect_Method_PersistenceDelegate() {
        }

        @Override // java.beans.PersistenceDelegate
        protected boolean mutatesTo(Object obj, Object obj2) {
            return obj.equals(obj2);
        }

        @Override // java.beans.PersistenceDelegate
        protected Expression instantiate(Object obj, Encoder encoder) {
            Method method = (Method) obj;
            return new Expression(obj, method.getDeclaringClass(), "getMethod", new Object[]{method.getName(), method.getParameterTypes()});
        }
    }

    /* loaded from: rt.jar:java/beans/MetaData$java_util_Date_PersistenceDelegate.class */
    static class java_util_Date_PersistenceDelegate extends PersistenceDelegate {
        java_util_Date_PersistenceDelegate() {
        }

        @Override // java.beans.PersistenceDelegate
        protected boolean mutatesTo(Object obj, Object obj2) {
            return super.mutatesTo(obj, obj2) && ((Date) obj).getTime() == ((Date) obj2).getTime();
        }

        @Override // java.beans.PersistenceDelegate
        protected Expression instantiate(Object obj, Encoder encoder) {
            Date date = (Date) obj;
            return new Expression(date, date.getClass(), "new", new Object[]{Long.valueOf(date.getTime())});
        }
    }

    /* loaded from: rt.jar:java/beans/MetaData$java_sql_Timestamp_PersistenceDelegate.class */
    static final class java_sql_Timestamp_PersistenceDelegate extends java_util_Date_PersistenceDelegate {
        private static final Method getNanosMethod = getNanosMethod();

        java_sql_Timestamp_PersistenceDelegate() {
        }

        private static Method getNanosMethod() {
            try {
                return Class.forName("java.sql.Timestamp", true, null).getMethod("getNanos", new Class[0]);
            } catch (ClassNotFoundException e2) {
                return null;
            } catch (NoSuchMethodException e3) {
                throw new AssertionError(e3);
            }
        }

        private static int getNanos(Object obj) {
            if (getNanosMethod == null) {
                throw new AssertionError((Object) "Should not get here");
            }
            try {
                return ((Integer) getNanosMethod.invoke(obj, new Object[0])).intValue();
            } catch (IllegalAccessException e2) {
                throw new AssertionError(e2);
            } catch (InvocationTargetException e3) {
                Throwable cause = e3.getCause();
                if (cause instanceof RuntimeException) {
                    throw ((RuntimeException) cause);
                }
                if (cause instanceof Error) {
                    throw ((Error) cause);
                }
                throw new AssertionError(e3);
            }
        }

        @Override // java.beans.PersistenceDelegate
        protected void initialize(Class<?> cls, Object obj, Object obj2, Encoder encoder) {
            int nanos = getNanos(obj);
            if (nanos != getNanos(obj2)) {
                encoder.writeStatement(new Statement(obj, "setNanos", new Object[]{Integer.valueOf(nanos)}));
            }
        }
    }

    /* loaded from: rt.jar:java/beans/MetaData$java_util_Collections.class */
    private static abstract class java_util_Collections extends PersistenceDelegate {
        private java_util_Collections() {
        }

        @Override // java.beans.PersistenceDelegate
        protected boolean mutatesTo(Object obj, Object obj2) {
            if (!super.mutatesTo(obj, obj2)) {
                return false;
            }
            if ((obj instanceof List) || (obj instanceof Set) || (obj instanceof Map)) {
                return obj.equals(obj2);
            }
            Collection collection = (Collection) obj;
            Collection<?> collection2 = (Collection) obj2;
            return collection.size() == collection2.size() && collection.containsAll(collection2);
        }

        @Override // java.beans.PersistenceDelegate
        protected void initialize(Class<?> cls, Object obj, Object obj2, Encoder encoder) {
        }

        /* loaded from: rt.jar:java/beans/MetaData$java_util_Collections$EmptyList_PersistenceDelegate.class */
        static final class EmptyList_PersistenceDelegate extends java_util_Collections {
            EmptyList_PersistenceDelegate() {
                super();
            }

            @Override // java.beans.PersistenceDelegate
            protected Expression instantiate(Object obj, Encoder encoder) {
                return new Expression(obj, Collections.class, "emptyList", null);
            }
        }

        /* loaded from: rt.jar:java/beans/MetaData$java_util_Collections$EmptySet_PersistenceDelegate.class */
        static final class EmptySet_PersistenceDelegate extends java_util_Collections {
            EmptySet_PersistenceDelegate() {
                super();
            }

            @Override // java.beans.PersistenceDelegate
            protected Expression instantiate(Object obj, Encoder encoder) {
                return new Expression(obj, Collections.class, "emptySet", null);
            }
        }

        /* loaded from: rt.jar:java/beans/MetaData$java_util_Collections$EmptyMap_PersistenceDelegate.class */
        static final class EmptyMap_PersistenceDelegate extends java_util_Collections {
            EmptyMap_PersistenceDelegate() {
                super();
            }

            @Override // java.beans.PersistenceDelegate
            protected Expression instantiate(Object obj, Encoder encoder) {
                return new Expression(obj, Collections.class, "emptyMap", null);
            }
        }

        /* loaded from: rt.jar:java/beans/MetaData$java_util_Collections$SingletonList_PersistenceDelegate.class */
        static final class SingletonList_PersistenceDelegate extends java_util_Collections {
            SingletonList_PersistenceDelegate() {
                super();
            }

            @Override // java.beans.PersistenceDelegate
            protected Expression instantiate(Object obj, Encoder encoder) {
                return new Expression(obj, Collections.class, "singletonList", new Object[]{((List) obj).get(0)});
            }
        }

        /* loaded from: rt.jar:java/beans/MetaData$java_util_Collections$SingletonSet_PersistenceDelegate.class */
        static final class SingletonSet_PersistenceDelegate extends java_util_Collections {
            SingletonSet_PersistenceDelegate() {
                super();
            }

            @Override // java.beans.PersistenceDelegate
            protected Expression instantiate(Object obj, Encoder encoder) {
                return new Expression(obj, Collections.class, "singleton", new Object[]{((Set) obj).iterator().next()});
            }
        }

        /* loaded from: rt.jar:java/beans/MetaData$java_util_Collections$SingletonMap_PersistenceDelegate.class */
        static final class SingletonMap_PersistenceDelegate extends java_util_Collections {
            SingletonMap_PersistenceDelegate() {
                super();
            }

            @Override // java.beans.PersistenceDelegate
            protected Expression instantiate(Object obj, Encoder encoder) {
                Map map = (Map) obj;
                Object next = map.keySet().iterator().next();
                return new Expression(obj, Collections.class, "singletonMap", new Object[]{next, map.get(next)});
            }
        }

        /* loaded from: rt.jar:java/beans/MetaData$java_util_Collections$UnmodifiableCollection_PersistenceDelegate.class */
        static final class UnmodifiableCollection_PersistenceDelegate extends java_util_Collections {
            UnmodifiableCollection_PersistenceDelegate() {
                super();
            }

            @Override // java.beans.PersistenceDelegate
            protected Expression instantiate(Object obj, Encoder encoder) {
                return new Expression(obj, Collections.class, "unmodifiableCollection", new Object[]{new ArrayList((Collection) obj)});
            }
        }

        /* loaded from: rt.jar:java/beans/MetaData$java_util_Collections$UnmodifiableList_PersistenceDelegate.class */
        static final class UnmodifiableList_PersistenceDelegate extends java_util_Collections {
            UnmodifiableList_PersistenceDelegate() {
                super();
            }

            @Override // java.beans.PersistenceDelegate
            protected Expression instantiate(Object obj, Encoder encoder) {
                return new Expression(obj, Collections.class, "unmodifiableList", new Object[]{new LinkedList((Collection) obj)});
            }
        }

        /* loaded from: rt.jar:java/beans/MetaData$java_util_Collections$UnmodifiableRandomAccessList_PersistenceDelegate.class */
        static final class UnmodifiableRandomAccessList_PersistenceDelegate extends java_util_Collections {
            UnmodifiableRandomAccessList_PersistenceDelegate() {
                super();
            }

            @Override // java.beans.PersistenceDelegate
            protected Expression instantiate(Object obj, Encoder encoder) {
                return new Expression(obj, Collections.class, "unmodifiableList", new Object[]{new ArrayList((Collection) obj)});
            }
        }

        /* loaded from: rt.jar:java/beans/MetaData$java_util_Collections$UnmodifiableSet_PersistenceDelegate.class */
        static final class UnmodifiableSet_PersistenceDelegate extends java_util_Collections {
            UnmodifiableSet_PersistenceDelegate() {
                super();
            }

            @Override // java.beans.PersistenceDelegate
            protected Expression instantiate(Object obj, Encoder encoder) {
                return new Expression(obj, Collections.class, "unmodifiableSet", new Object[]{new HashSet((Set) obj)});
            }
        }

        /* loaded from: rt.jar:java/beans/MetaData$java_util_Collections$UnmodifiableSortedSet_PersistenceDelegate.class */
        static final class UnmodifiableSortedSet_PersistenceDelegate extends java_util_Collections {
            UnmodifiableSortedSet_PersistenceDelegate() {
                super();
            }

            @Override // java.beans.PersistenceDelegate
            protected Expression instantiate(Object obj, Encoder encoder) {
                return new Expression(obj, Collections.class, "unmodifiableSortedSet", new Object[]{new TreeSet((SortedSet) obj)});
            }
        }

        /* loaded from: rt.jar:java/beans/MetaData$java_util_Collections$UnmodifiableMap_PersistenceDelegate.class */
        static final class UnmodifiableMap_PersistenceDelegate extends java_util_Collections {
            UnmodifiableMap_PersistenceDelegate() {
                super();
            }

            @Override // java.beans.PersistenceDelegate
            protected Expression instantiate(Object obj, Encoder encoder) {
                return new Expression(obj, Collections.class, "unmodifiableMap", new Object[]{new HashMap((Map) obj)});
            }
        }

        /* loaded from: rt.jar:java/beans/MetaData$java_util_Collections$UnmodifiableSortedMap_PersistenceDelegate.class */
        static final class UnmodifiableSortedMap_PersistenceDelegate extends java_util_Collections {
            UnmodifiableSortedMap_PersistenceDelegate() {
                super();
            }

            @Override // java.beans.PersistenceDelegate
            protected Expression instantiate(Object obj, Encoder encoder) {
                return new Expression(obj, Collections.class, "unmodifiableSortedMap", new Object[]{new TreeMap((SortedMap) obj)});
            }
        }

        /* loaded from: rt.jar:java/beans/MetaData$java_util_Collections$SynchronizedCollection_PersistenceDelegate.class */
        static final class SynchronizedCollection_PersistenceDelegate extends java_util_Collections {
            SynchronizedCollection_PersistenceDelegate() {
                super();
            }

            @Override // java.beans.PersistenceDelegate
            protected Expression instantiate(Object obj, Encoder encoder) {
                return new Expression(obj, Collections.class, "synchronizedCollection", new Object[]{new ArrayList((Collection) obj)});
            }
        }

        /* loaded from: rt.jar:java/beans/MetaData$java_util_Collections$SynchronizedList_PersistenceDelegate.class */
        static final class SynchronizedList_PersistenceDelegate extends java_util_Collections {
            SynchronizedList_PersistenceDelegate() {
                super();
            }

            @Override // java.beans.PersistenceDelegate
            protected Expression instantiate(Object obj, Encoder encoder) {
                return new Expression(obj, Collections.class, "synchronizedList", new Object[]{new LinkedList((Collection) obj)});
            }
        }

        /* loaded from: rt.jar:java/beans/MetaData$java_util_Collections$SynchronizedRandomAccessList_PersistenceDelegate.class */
        static final class SynchronizedRandomAccessList_PersistenceDelegate extends java_util_Collections {
            SynchronizedRandomAccessList_PersistenceDelegate() {
                super();
            }

            @Override // java.beans.PersistenceDelegate
            protected Expression instantiate(Object obj, Encoder encoder) {
                return new Expression(obj, Collections.class, "synchronizedList", new Object[]{new ArrayList((Collection) obj)});
            }
        }

        /* loaded from: rt.jar:java/beans/MetaData$java_util_Collections$SynchronizedSet_PersistenceDelegate.class */
        static final class SynchronizedSet_PersistenceDelegate extends java_util_Collections {
            SynchronizedSet_PersistenceDelegate() {
                super();
            }

            @Override // java.beans.PersistenceDelegate
            protected Expression instantiate(Object obj, Encoder encoder) {
                return new Expression(obj, Collections.class, "synchronizedSet", new Object[]{new HashSet((Set) obj)});
            }
        }

        /* loaded from: rt.jar:java/beans/MetaData$java_util_Collections$SynchronizedSortedSet_PersistenceDelegate.class */
        static final class SynchronizedSortedSet_PersistenceDelegate extends java_util_Collections {
            SynchronizedSortedSet_PersistenceDelegate() {
                super();
            }

            @Override // java.beans.PersistenceDelegate
            protected Expression instantiate(Object obj, Encoder encoder) {
                return new Expression(obj, Collections.class, "synchronizedSortedSet", new Object[]{new TreeSet((SortedSet) obj)});
            }
        }

        /* loaded from: rt.jar:java/beans/MetaData$java_util_Collections$SynchronizedMap_PersistenceDelegate.class */
        static final class SynchronizedMap_PersistenceDelegate extends java_util_Collections {
            SynchronizedMap_PersistenceDelegate() {
                super();
            }

            @Override // java.beans.PersistenceDelegate
            protected Expression instantiate(Object obj, Encoder encoder) {
                return new Expression(obj, Collections.class, "synchronizedMap", new Object[]{new HashMap((Map) obj)});
            }
        }

        /* loaded from: rt.jar:java/beans/MetaData$java_util_Collections$SynchronizedSortedMap_PersistenceDelegate.class */
        static final class SynchronizedSortedMap_PersistenceDelegate extends java_util_Collections {
            SynchronizedSortedMap_PersistenceDelegate() {
                super();
            }

            @Override // java.beans.PersistenceDelegate
            protected Expression instantiate(Object obj, Encoder encoder) {
                return new Expression(obj, Collections.class, "synchronizedSortedMap", new Object[]{new TreeMap((SortedMap) obj)});
            }
        }

        /* loaded from: rt.jar:java/beans/MetaData$java_util_Collections$CheckedCollection_PersistenceDelegate.class */
        static final class CheckedCollection_PersistenceDelegate extends java_util_Collections {
            CheckedCollection_PersistenceDelegate() {
                super();
            }

            @Override // java.beans.PersistenceDelegate
            protected Expression instantiate(Object obj, Encoder encoder) {
                return new Expression(obj, Collections.class, "checkedCollection", new Object[]{new ArrayList((Collection) obj), MetaData.getPrivateFieldValue(obj, "java.util.Collections$CheckedCollection.type")});
            }
        }

        /* loaded from: rt.jar:java/beans/MetaData$java_util_Collections$CheckedList_PersistenceDelegate.class */
        static final class CheckedList_PersistenceDelegate extends java_util_Collections {
            CheckedList_PersistenceDelegate() {
                super();
            }

            @Override // java.beans.PersistenceDelegate
            protected Expression instantiate(Object obj, Encoder encoder) {
                return new Expression(obj, Collections.class, "checkedList", new Object[]{new LinkedList((Collection) obj), MetaData.getPrivateFieldValue(obj, "java.util.Collections$CheckedCollection.type")});
            }
        }

        /* loaded from: rt.jar:java/beans/MetaData$java_util_Collections$CheckedRandomAccessList_PersistenceDelegate.class */
        static final class CheckedRandomAccessList_PersistenceDelegate extends java_util_Collections {
            CheckedRandomAccessList_PersistenceDelegate() {
                super();
            }

            @Override // java.beans.PersistenceDelegate
            protected Expression instantiate(Object obj, Encoder encoder) {
                return new Expression(obj, Collections.class, "checkedList", new Object[]{new ArrayList((Collection) obj), MetaData.getPrivateFieldValue(obj, "java.util.Collections$CheckedCollection.type")});
            }
        }

        /* loaded from: rt.jar:java/beans/MetaData$java_util_Collections$CheckedSet_PersistenceDelegate.class */
        static final class CheckedSet_PersistenceDelegate extends java_util_Collections {
            CheckedSet_PersistenceDelegate() {
                super();
            }

            @Override // java.beans.PersistenceDelegate
            protected Expression instantiate(Object obj, Encoder encoder) {
                return new Expression(obj, Collections.class, "checkedSet", new Object[]{new HashSet((Set) obj), MetaData.getPrivateFieldValue(obj, "java.util.Collections$CheckedCollection.type")});
            }
        }

        /* loaded from: rt.jar:java/beans/MetaData$java_util_Collections$CheckedSortedSet_PersistenceDelegate.class */
        static final class CheckedSortedSet_PersistenceDelegate extends java_util_Collections {
            CheckedSortedSet_PersistenceDelegate() {
                super();
            }

            @Override // java.beans.PersistenceDelegate
            protected Expression instantiate(Object obj, Encoder encoder) {
                return new Expression(obj, Collections.class, "checkedSortedSet", new Object[]{new TreeSet((SortedSet) obj), MetaData.getPrivateFieldValue(obj, "java.util.Collections$CheckedCollection.type")});
            }
        }

        /* loaded from: rt.jar:java/beans/MetaData$java_util_Collections$CheckedMap_PersistenceDelegate.class */
        static final class CheckedMap_PersistenceDelegate extends java_util_Collections {
            CheckedMap_PersistenceDelegate() {
                super();
            }

            @Override // java.beans.PersistenceDelegate
            protected Expression instantiate(Object obj, Encoder encoder) {
                return new Expression(obj, Collections.class, "checkedMap", new Object[]{new HashMap((Map) obj), MetaData.getPrivateFieldValue(obj, "java.util.Collections$CheckedMap.keyType"), MetaData.getPrivateFieldValue(obj, "java.util.Collections$CheckedMap.valueType")});
            }
        }

        /* loaded from: rt.jar:java/beans/MetaData$java_util_Collections$CheckedSortedMap_PersistenceDelegate.class */
        static final class CheckedSortedMap_PersistenceDelegate extends java_util_Collections {
            CheckedSortedMap_PersistenceDelegate() {
                super();
            }

            @Override // java.beans.PersistenceDelegate
            protected Expression instantiate(Object obj, Encoder encoder) {
                return new Expression(obj, Collections.class, "checkedSortedMap", new Object[]{new TreeMap((SortedMap) obj), MetaData.getPrivateFieldValue(obj, "java.util.Collections$CheckedMap.keyType"), MetaData.getPrivateFieldValue(obj, "java.util.Collections$CheckedMap.valueType")});
            }
        }
    }

    /* loaded from: rt.jar:java/beans/MetaData$java_util_EnumMap_PersistenceDelegate.class */
    static final class java_util_EnumMap_PersistenceDelegate extends PersistenceDelegate {
        java_util_EnumMap_PersistenceDelegate() {
        }

        @Override // java.beans.PersistenceDelegate
        protected boolean mutatesTo(Object obj, Object obj2) {
            return super.mutatesTo(obj, obj2) && getType(obj) == getType(obj2);
        }

        @Override // java.beans.PersistenceDelegate
        protected Expression instantiate(Object obj, Encoder encoder) {
            return new Expression(obj, EnumMap.class, "new", new Object[]{getType(obj)});
        }

        private static Object getType(Object obj) {
            return MetaData.getPrivateFieldValue(obj, "java.util.EnumMap.keyType");
        }
    }

    /* loaded from: rt.jar:java/beans/MetaData$java_util_EnumSet_PersistenceDelegate.class */
    static final class java_util_EnumSet_PersistenceDelegate extends PersistenceDelegate {
        java_util_EnumSet_PersistenceDelegate() {
        }

        @Override // java.beans.PersistenceDelegate
        protected boolean mutatesTo(Object obj, Object obj2) {
            return super.mutatesTo(obj, obj2) && getType(obj) == getType(obj2);
        }

        @Override // java.beans.PersistenceDelegate
        protected Expression instantiate(Object obj, Encoder encoder) {
            return new Expression(obj, EnumSet.class, "noneOf", new Object[]{getType(obj)});
        }

        private static Object getType(Object obj) {
            return MetaData.getPrivateFieldValue(obj, "java.util.EnumSet.elementType");
        }
    }

    /* loaded from: rt.jar:java/beans/MetaData$java_util_Collection_PersistenceDelegate.class */
    static class java_util_Collection_PersistenceDelegate extends DefaultPersistenceDelegate {
        java_util_Collection_PersistenceDelegate() {
        }

        @Override // java.beans.DefaultPersistenceDelegate, java.beans.PersistenceDelegate
        protected void initialize(Class<?> cls, Object obj, Object obj2, Encoder encoder) {
            Collection collection = (Collection) obj;
            if (((Collection) obj2).size() != 0) {
                invokeStatement(obj, Constants.CLEAR_ATTRIBUTES, new Object[0], encoder);
            }
            Iterator it = collection.iterator();
            while (it.hasNext()) {
                invokeStatement(obj, "add", new Object[]{it.next()}, encoder);
            }
        }
    }

    /* loaded from: rt.jar:java/beans/MetaData$java_util_List_PersistenceDelegate.class */
    static class java_util_List_PersistenceDelegate extends DefaultPersistenceDelegate {
        java_util_List_PersistenceDelegate() {
        }

        @Override // java.beans.DefaultPersistenceDelegate, java.beans.PersistenceDelegate
        protected void initialize(Class<?> cls, Object obj, Object obj2, Encoder encoder) {
            List list = (List) obj;
            List list2 = (List) obj2;
            int size = list.size();
            int size2 = list2 == null ? 0 : list2.size();
            if (size < size2) {
                invokeStatement(obj, Constants.CLEAR_ATTRIBUTES, new Object[0], encoder);
                size2 = 0;
            }
            for (int i2 = 0; i2 < size2; i2++) {
                Integer num = new Integer(i2);
                Expression expression = new Expression(obj, "get", new Object[]{num});
                Expression expression2 = new Expression(obj2, "get", new Object[]{num});
                try {
                    Object value = expression.getValue();
                    Object value2 = expression2.getValue();
                    encoder.writeExpression(expression);
                    if (!Objects.equals(value2, encoder.get(value))) {
                        invokeStatement(obj, "set", new Object[]{num, value}, encoder);
                    }
                } catch (Exception e2) {
                    encoder.getExceptionListener().exceptionThrown(e2);
                }
            }
            for (int i3 = size2; i3 < size; i3++) {
                invokeStatement(obj, "add", new Object[]{list.get(i3)}, encoder);
            }
        }
    }

    /* loaded from: rt.jar:java/beans/MetaData$java_util_Map_PersistenceDelegate.class */
    static class java_util_Map_PersistenceDelegate extends DefaultPersistenceDelegate {
        java_util_Map_PersistenceDelegate() {
        }

        @Override // java.beans.DefaultPersistenceDelegate, java.beans.PersistenceDelegate
        protected void initialize(Class<?> cls, Object obj, Object obj2, Encoder encoder) {
            Map map = (Map) obj;
            Map map2 = (Map) obj2;
            if (map2 != null) {
                for (Object obj3 : map2.keySet().toArray()) {
                    if (!map.containsKey(obj3)) {
                        invokeStatement(obj, "remove", new Object[]{obj3}, encoder);
                    }
                }
            }
            for (Object obj4 : map.keySet()) {
                Expression expression = new Expression(obj, "get", new Object[]{obj4});
                Expression expression2 = new Expression(obj2, "get", new Object[]{obj4});
                try {
                    Object value = expression.getValue();
                    Object value2 = expression2.getValue();
                    encoder.writeExpression(expression);
                    if (!Objects.equals(value2, encoder.get(value))) {
                        invokeStatement(obj, "put", new Object[]{obj4, value}, encoder);
                    } else if (value2 == null && !map2.containsKey(obj4)) {
                        invokeStatement(obj, "put", new Object[]{obj4, value}, encoder);
                    }
                } catch (Exception e2) {
                    encoder.getExceptionListener().exceptionThrown(e2);
                }
            }
        }
    }

    /* loaded from: rt.jar:java/beans/MetaData$java_util_AbstractCollection_PersistenceDelegate.class */
    static final class java_util_AbstractCollection_PersistenceDelegate extends java_util_Collection_PersistenceDelegate {
        java_util_AbstractCollection_PersistenceDelegate() {
        }
    }

    /* loaded from: rt.jar:java/beans/MetaData$java_util_AbstractList_PersistenceDelegate.class */
    static final class java_util_AbstractList_PersistenceDelegate extends java_util_List_PersistenceDelegate {
        java_util_AbstractList_PersistenceDelegate() {
        }
    }

    /* loaded from: rt.jar:java/beans/MetaData$java_util_AbstractMap_PersistenceDelegate.class */
    static final class java_util_AbstractMap_PersistenceDelegate extends java_util_Map_PersistenceDelegate {
        java_util_AbstractMap_PersistenceDelegate() {
        }
    }

    /* loaded from: rt.jar:java/beans/MetaData$java_util_Hashtable_PersistenceDelegate.class */
    static final class java_util_Hashtable_PersistenceDelegate extends java_util_Map_PersistenceDelegate {
        java_util_Hashtable_PersistenceDelegate() {
        }
    }

    /* loaded from: rt.jar:java/beans/MetaData$java_beans_beancontext_BeanContextSupport_PersistenceDelegate.class */
    static final class java_beans_beancontext_BeanContextSupport_PersistenceDelegate extends java_util_Collection_PersistenceDelegate {
        java_beans_beancontext_BeanContextSupport_PersistenceDelegate() {
        }
    }

    /* loaded from: rt.jar:java/beans/MetaData$java_awt_Insets_PersistenceDelegate.class */
    static final class java_awt_Insets_PersistenceDelegate extends PersistenceDelegate {
        java_awt_Insets_PersistenceDelegate() {
        }

        @Override // java.beans.PersistenceDelegate
        protected boolean mutatesTo(Object obj, Object obj2) {
            return obj.equals(obj2);
        }

        @Override // java.beans.PersistenceDelegate
        protected Expression instantiate(Object obj, Encoder encoder) {
            Insets insets = (Insets) obj;
            return new Expression(insets, insets.getClass(), "new", new Object[]{Integer.valueOf(insets.top), Integer.valueOf(insets.left), Integer.valueOf(insets.bottom), Integer.valueOf(insets.right)});
        }
    }

    /* loaded from: rt.jar:java/beans/MetaData$java_awt_Font_PersistenceDelegate.class */
    static final class java_awt_Font_PersistenceDelegate extends PersistenceDelegate {
        java_awt_Font_PersistenceDelegate() {
        }

        @Override // java.beans.PersistenceDelegate
        protected boolean mutatesTo(Object obj, Object obj2) {
            return obj.equals(obj2);
        }

        @Override // java.beans.PersistenceDelegate
        protected Expression instantiate(Object obj, Encoder encoder) {
            Font font = (Font) obj;
            int i2 = 0;
            String str = null;
            int i3 = 0;
            int iIntValue = 12;
            Map<TextAttribute, ?> attributes = font.getAttributes();
            HashMap map = new HashMap(attributes.size());
            for (TextAttribute textAttribute : attributes.keySet()) {
                Object obj2 = attributes.get(textAttribute);
                if (obj2 != null) {
                    map.put(textAttribute, obj2);
                }
                if (textAttribute == TextAttribute.FAMILY) {
                    if (obj2 instanceof String) {
                        i2++;
                        str = (String) obj2;
                    }
                } else if (textAttribute == TextAttribute.WEIGHT) {
                    if (TextAttribute.WEIGHT_REGULAR.equals(obj2)) {
                        i2++;
                    } else if (TextAttribute.WEIGHT_BOLD.equals(obj2)) {
                        i2++;
                        i3 |= 1;
                    }
                } else if (textAttribute == TextAttribute.POSTURE) {
                    if (TextAttribute.POSTURE_REGULAR.equals(obj2)) {
                        i2++;
                    } else if (TextAttribute.POSTURE_OBLIQUE.equals(obj2)) {
                        i2++;
                        i3 |= 2;
                    }
                } else if (textAttribute == TextAttribute.SIZE && (obj2 instanceof Number)) {
                    Number number = (Number) obj2;
                    iIntValue = number.intValue();
                    if (iIntValue == number.floatValue()) {
                        i2++;
                    }
                }
            }
            Class<?> cls = font.getClass();
            if (i2 == map.size()) {
                return new Expression(font, cls, "new", new Object[]{str, Integer.valueOf(i3), Integer.valueOf(iIntValue)});
            }
            if (cls == Font.class) {
                return new Expression(font, cls, "getFont", new Object[]{map});
            }
            return new Expression(font, cls, "new", new Object[]{Font.getFont(map)});
        }
    }

    /* loaded from: rt.jar:java/beans/MetaData$java_awt_AWTKeyStroke_PersistenceDelegate.class */
    static final class java_awt_AWTKeyStroke_PersistenceDelegate extends PersistenceDelegate {
        java_awt_AWTKeyStroke_PersistenceDelegate() {
        }

        @Override // java.beans.PersistenceDelegate
        protected boolean mutatesTo(Object obj, Object obj2) {
            return obj.equals(obj2);
        }

        @Override // java.beans.PersistenceDelegate
        protected Expression instantiate(Object obj, Encoder encoder) {
            AWTKeyStroke aWTKeyStroke = (AWTKeyStroke) obj;
            char keyChar = aWTKeyStroke.getKeyChar();
            int keyCode = aWTKeyStroke.getKeyCode();
            int modifiers = aWTKeyStroke.getModifiers();
            boolean zIsOnKeyRelease = aWTKeyStroke.isOnKeyRelease();
            Object[] objArr = null;
            if (keyChar == 65535) {
                objArr = !zIsOnKeyRelease ? new Object[]{Integer.valueOf(keyCode), Integer.valueOf(modifiers)} : new Object[]{Integer.valueOf(keyCode), Integer.valueOf(modifiers), Boolean.valueOf(zIsOnKeyRelease)};
            } else if (keyCode == 0) {
                if (!zIsOnKeyRelease) {
                    objArr = modifiers == 0 ? new Object[]{Character.valueOf(keyChar)} : new Object[]{Character.valueOf(keyChar), Integer.valueOf(modifiers)};
                } else if (modifiers == 0) {
                    objArr = new Object[]{Character.valueOf(keyChar), Boolean.valueOf(zIsOnKeyRelease)};
                }
            }
            if (objArr == null) {
                throw new IllegalStateException("Unsupported KeyStroke: " + ((Object) aWTKeyStroke));
            }
            Class<?> cls = aWTKeyStroke.getClass();
            String name = cls.getName();
            int iLastIndexOf = name.lastIndexOf(46) + 1;
            if (iLastIndexOf > 0) {
                name = name.substring(iLastIndexOf);
            }
            return new Expression(aWTKeyStroke, cls, "get" + name, objArr);
        }
    }

    /* loaded from: rt.jar:java/beans/MetaData$StaticFieldsPersistenceDelegate.class */
    static class StaticFieldsPersistenceDelegate extends PersistenceDelegate {
        StaticFieldsPersistenceDelegate() {
        }

        protected void installFields(Encoder encoder, Class<?> cls) throws SecurityException {
            if (Modifier.isPublic(cls.getModifiers()) && ReflectUtil.isPackageAccessible(cls)) {
                for (Field field : cls.getFields()) {
                    if (Object.class.isAssignableFrom(field.getType())) {
                        encoder.writeExpression(new Expression(field, "get", new Object[]{null}));
                    }
                }
            }
        }

        @Override // java.beans.PersistenceDelegate
        protected Expression instantiate(Object obj, Encoder encoder) {
            throw new RuntimeException("Unrecognized instance: " + obj);
        }

        @Override // java.beans.PersistenceDelegate
        public void writeObject(Object obj, Encoder encoder) throws SecurityException {
            if (encoder.getAttribute(this) == null) {
                encoder.setAttribute(this, Boolean.TRUE);
                installFields(encoder, obj.getClass());
            }
            super.writeObject(obj, encoder);
        }
    }

    /* loaded from: rt.jar:java/beans/MetaData$java_awt_SystemColor_PersistenceDelegate.class */
    static final class java_awt_SystemColor_PersistenceDelegate extends StaticFieldsPersistenceDelegate {
        java_awt_SystemColor_PersistenceDelegate() {
        }
    }

    /* loaded from: rt.jar:java/beans/MetaData$java_awt_font_TextAttribute_PersistenceDelegate.class */
    static final class java_awt_font_TextAttribute_PersistenceDelegate extends StaticFieldsPersistenceDelegate {
        java_awt_font_TextAttribute_PersistenceDelegate() {
        }
    }

    /* loaded from: rt.jar:java/beans/MetaData$java_awt_MenuShortcut_PersistenceDelegate.class */
    static final class java_awt_MenuShortcut_PersistenceDelegate extends PersistenceDelegate {
        java_awt_MenuShortcut_PersistenceDelegate() {
        }

        @Override // java.beans.PersistenceDelegate
        protected boolean mutatesTo(Object obj, Object obj2) {
            return obj.equals(obj2);
        }

        @Override // java.beans.PersistenceDelegate
        protected Expression instantiate(Object obj, Encoder encoder) {
            MenuShortcut menuShortcut = (MenuShortcut) obj;
            return new Expression(obj, menuShortcut.getClass(), "new", new Object[]{new Integer(menuShortcut.getKey()), Boolean.valueOf(menuShortcut.usesShiftModifier())});
        }
    }

    /* loaded from: rt.jar:java/beans/MetaData$java_awt_Component_PersistenceDelegate.class */
    static final class java_awt_Component_PersistenceDelegate extends DefaultPersistenceDelegate {
        java_awt_Component_PersistenceDelegate() {
        }

        @Override // java.beans.DefaultPersistenceDelegate, java.beans.PersistenceDelegate
        protected void initialize(Class<?> cls, Object obj, Object obj2, Encoder encoder) {
            super.initialize(cls, obj, obj2, encoder);
            Component component = (Component) obj;
            Component component2 = (Component) obj2;
            if (!(obj instanceof Window)) {
                Color background = component.isBackgroundSet() ? component.getBackground() : null;
                if (!Objects.equals(background, component2.isBackgroundSet() ? component2.getBackground() : null)) {
                    invokeStatement(obj, "setBackground", new Object[]{background}, encoder);
                }
                Color foreground = component.isForegroundSet() ? component.getForeground() : null;
                if (!Objects.equals(foreground, component2.isForegroundSet() ? component2.getForeground() : null)) {
                    invokeStatement(obj, "setForeground", new Object[]{foreground}, encoder);
                }
                Font font = component.isFontSet() ? component.getFont() : null;
                if (!Objects.equals(font, component2.isFontSet() ? component2.getFont() : null)) {
                    invokeStatement(obj, "setFont", new Object[]{font}, encoder);
                }
            }
            Container parent = component.getParent();
            if (parent == null || parent.getLayout() == null) {
                boolean zEquals = component.getLocation().equals(component2.getLocation());
                boolean zEquals2 = component.getSize().equals(component2.getSize());
                if (!zEquals && !zEquals2) {
                    invokeStatement(obj, "setBounds", new Object[]{component.getBounds()}, encoder);
                } else if (!zEquals) {
                    invokeStatement(obj, "setLocation", new Object[]{component.getLocation()}, encoder);
                } else if (!zEquals2) {
                    invokeStatement(obj, "setSize", new Object[]{component.getSize()}, encoder);
                }
            }
        }
    }

    /* loaded from: rt.jar:java/beans/MetaData$java_awt_Container_PersistenceDelegate.class */
    static final class java_awt_Container_PersistenceDelegate extends DefaultPersistenceDelegate {
        java_awt_Container_PersistenceDelegate() {
        }

        @Override // java.beans.DefaultPersistenceDelegate, java.beans.PersistenceDelegate
        protected void initialize(Class<?> cls, Object obj, Object obj2, Encoder encoder) {
            super.initialize(cls, obj, obj2, encoder);
            if (obj instanceof JScrollPane) {
                return;
            }
            Container container = (Container) obj;
            Component[] components = container.getComponents();
            Container container2 = (Container) obj2;
            Component[] components2 = container2 == null ? new Component[0] : container2.getComponents();
            BorderLayout borderLayout = container.getLayout() instanceof BorderLayout ? (BorderLayout) container.getLayout() : null;
            JLayeredPane jLayeredPane = obj instanceof JLayeredPane ? (JLayeredPane) obj : null;
            for (int length = components2.length; length < components.length; length++) {
                invokeStatement(obj, "add", borderLayout != null ? new Object[]{components[length], borderLayout.getConstraints(components[length])} : jLayeredPane != null ? new Object[]{components[length], Integer.valueOf(jLayeredPane.getLayer(components[length])), -1} : new Object[]{components[length]}, encoder);
            }
        }
    }

    /* loaded from: rt.jar:java/beans/MetaData$java_awt_Choice_PersistenceDelegate.class */
    static final class java_awt_Choice_PersistenceDelegate extends DefaultPersistenceDelegate {
        java_awt_Choice_PersistenceDelegate() {
        }

        @Override // java.beans.DefaultPersistenceDelegate, java.beans.PersistenceDelegate
        protected void initialize(Class<?> cls, Object obj, Object obj2, Encoder encoder) {
            super.initialize(cls, obj, obj2, encoder);
            Choice choice = (Choice) obj;
            for (int itemCount = ((Choice) obj2).getItemCount(); itemCount < choice.getItemCount(); itemCount++) {
                invokeStatement(obj, "add", new Object[]{choice.getItem(itemCount)}, encoder);
            }
        }
    }

    /* loaded from: rt.jar:java/beans/MetaData$java_awt_Menu_PersistenceDelegate.class */
    static final class java_awt_Menu_PersistenceDelegate extends DefaultPersistenceDelegate {
        java_awt_Menu_PersistenceDelegate() {
        }

        @Override // java.beans.DefaultPersistenceDelegate, java.beans.PersistenceDelegate
        protected void initialize(Class<?> cls, Object obj, Object obj2, Encoder encoder) {
            super.initialize(cls, obj, obj2, encoder);
            Menu menu = (Menu) obj;
            for (int itemCount = ((Menu) obj2).getItemCount(); itemCount < menu.getItemCount(); itemCount++) {
                invokeStatement(obj, "add", new Object[]{menu.getItem(itemCount)}, encoder);
            }
        }
    }

    /* loaded from: rt.jar:java/beans/MetaData$java_awt_MenuBar_PersistenceDelegate.class */
    static final class java_awt_MenuBar_PersistenceDelegate extends DefaultPersistenceDelegate {
        java_awt_MenuBar_PersistenceDelegate() {
        }

        @Override // java.beans.DefaultPersistenceDelegate, java.beans.PersistenceDelegate
        protected void initialize(Class<?> cls, Object obj, Object obj2, Encoder encoder) {
            super.initialize(cls, obj, obj2, encoder);
            MenuBar menuBar = (MenuBar) obj;
            for (int menuCount = ((MenuBar) obj2).getMenuCount(); menuCount < menuBar.getMenuCount(); menuCount++) {
                invokeStatement(obj, "add", new Object[]{menuBar.getMenu(menuCount)}, encoder);
            }
        }
    }

    /* loaded from: rt.jar:java/beans/MetaData$java_awt_List_PersistenceDelegate.class */
    static final class java_awt_List_PersistenceDelegate extends DefaultPersistenceDelegate {
        java_awt_List_PersistenceDelegate() {
        }

        @Override // java.beans.DefaultPersistenceDelegate, java.beans.PersistenceDelegate
        protected void initialize(Class<?> cls, Object obj, Object obj2, Encoder encoder) {
            super.initialize(cls, obj, obj2, encoder);
            java.awt.List list = (java.awt.List) obj;
            for (int itemCount = ((java.awt.List) obj2).getItemCount(); itemCount < list.getItemCount(); itemCount++) {
                invokeStatement(obj, "add", new Object[]{list.getItem(itemCount)}, encoder);
            }
        }
    }

    /* loaded from: rt.jar:java/beans/MetaData$java_awt_BorderLayout_PersistenceDelegate.class */
    static final class java_awt_BorderLayout_PersistenceDelegate extends DefaultPersistenceDelegate {
        private static final String[] CONSTRAINTS = {"North", "South", "East", "West", BorderLayout.CENTER, "First", "Last", "Before", "After"};

        java_awt_BorderLayout_PersistenceDelegate() {
        }

        @Override // java.beans.DefaultPersistenceDelegate, java.beans.PersistenceDelegate
        protected void initialize(Class<?> cls, Object obj, Object obj2, Encoder encoder) {
            super.initialize(cls, obj, obj2, encoder);
            BorderLayout borderLayout = (BorderLayout) obj;
            BorderLayout borderLayout2 = (BorderLayout) obj2;
            for (String str : CONSTRAINTS) {
                Component layoutComponent = borderLayout.getLayoutComponent(str);
                Component layoutComponent2 = borderLayout2.getLayoutComponent(str);
                if (layoutComponent != null && layoutComponent2 == null) {
                    invokeStatement(obj, "addLayoutComponent", new Object[]{layoutComponent, str}, encoder);
                }
            }
        }
    }

    /* loaded from: rt.jar:java/beans/MetaData$java_awt_CardLayout_PersistenceDelegate.class */
    static final class java_awt_CardLayout_PersistenceDelegate extends DefaultPersistenceDelegate {
        java_awt_CardLayout_PersistenceDelegate() {
        }

        @Override // java.beans.DefaultPersistenceDelegate, java.beans.PersistenceDelegate
        protected void initialize(Class<?> cls, Object obj, Object obj2, Encoder encoder) {
            super.initialize(cls, obj, obj2, encoder);
            if (getVector(obj2).isEmpty()) {
                Iterator<?> it = getVector(obj).iterator();
                while (it.hasNext()) {
                    Object next = it.next();
                    invokeStatement(obj, "addLayoutComponent", new Object[]{MetaData.getPrivateFieldValue(next, "java.awt.CardLayout$Card.name"), MetaData.getPrivateFieldValue(next, "java.awt.CardLayout$Card.comp")}, encoder);
                }
            }
        }

        @Override // java.beans.DefaultPersistenceDelegate, java.beans.PersistenceDelegate
        protected boolean mutatesTo(Object obj, Object obj2) {
            return super.mutatesTo(obj, obj2) && getVector(obj2).isEmpty();
        }

        private static Vector<?> getVector(Object obj) {
            return (Vector) MetaData.getPrivateFieldValue(obj, "java.awt.CardLayout.vector");
        }
    }

    /* loaded from: rt.jar:java/beans/MetaData$java_awt_GridBagLayout_PersistenceDelegate.class */
    static final class java_awt_GridBagLayout_PersistenceDelegate extends DefaultPersistenceDelegate {
        java_awt_GridBagLayout_PersistenceDelegate() {
        }

        @Override // java.beans.DefaultPersistenceDelegate, java.beans.PersistenceDelegate
        protected void initialize(Class<?> cls, Object obj, Object obj2, Encoder encoder) {
            super.initialize(cls, obj, obj2, encoder);
            if (getHashtable(obj2).isEmpty()) {
                for (Map.Entry<?, ?> entry : getHashtable(obj).entrySet()) {
                    invokeStatement(obj, "addLayoutComponent", new Object[]{entry.getKey(), entry.getValue()}, encoder);
                }
            }
        }

        @Override // java.beans.DefaultPersistenceDelegate, java.beans.PersistenceDelegate
        protected boolean mutatesTo(Object obj, Object obj2) {
            return super.mutatesTo(obj, obj2) && getHashtable(obj2).isEmpty();
        }

        private static Hashtable<?, ?> getHashtable(Object obj) {
            return (Hashtable) MetaData.getPrivateFieldValue(obj, "java.awt.GridBagLayout.comptable");
        }
    }

    /* loaded from: rt.jar:java/beans/MetaData$javax_swing_JFrame_PersistenceDelegate.class */
    static final class javax_swing_JFrame_PersistenceDelegate extends DefaultPersistenceDelegate {
        javax_swing_JFrame_PersistenceDelegate() {
        }

        @Override // java.beans.DefaultPersistenceDelegate, java.beans.PersistenceDelegate
        protected void initialize(Class<?> cls, Object obj, Object obj2, Encoder encoder) {
            super.initialize(cls, obj, obj2, encoder);
            boolean zIsVisible = ((Window) obj).isVisible();
            if (((Window) obj2).isVisible() != zIsVisible) {
                boolean z2 = encoder.executeStatements;
                encoder.executeStatements = false;
                invokeStatement(obj, "setVisible", new Object[]{Boolean.valueOf(zIsVisible)}, encoder);
                encoder.executeStatements = z2;
            }
        }
    }

    /* loaded from: rt.jar:java/beans/MetaData$javax_swing_DefaultListModel_PersistenceDelegate.class */
    static final class javax_swing_DefaultListModel_PersistenceDelegate extends DefaultPersistenceDelegate {
        javax_swing_DefaultListModel_PersistenceDelegate() {
        }

        @Override // java.beans.DefaultPersistenceDelegate, java.beans.PersistenceDelegate
        protected void initialize(Class<?> cls, Object obj, Object obj2, Encoder encoder) {
            super.initialize(cls, obj, obj2, encoder);
            DefaultListModel defaultListModel = (DefaultListModel) obj;
            for (int size = ((DefaultListModel) obj2).getSize(); size < defaultListModel.getSize(); size++) {
                invokeStatement(obj, "add", new Object[]{defaultListModel.getElementAt(size)}, encoder);
            }
        }
    }

    /* loaded from: rt.jar:java/beans/MetaData$javax_swing_DefaultComboBoxModel_PersistenceDelegate.class */
    static final class javax_swing_DefaultComboBoxModel_PersistenceDelegate extends DefaultPersistenceDelegate {
        javax_swing_DefaultComboBoxModel_PersistenceDelegate() {
        }

        @Override // java.beans.DefaultPersistenceDelegate, java.beans.PersistenceDelegate
        protected void initialize(Class<?> cls, Object obj, Object obj2, Encoder encoder) {
            super.initialize(cls, obj, obj2, encoder);
            DefaultComboBoxModel defaultComboBoxModel = (DefaultComboBoxModel) obj;
            for (int i2 = 0; i2 < defaultComboBoxModel.getSize(); i2++) {
                invokeStatement(obj, "addElement", new Object[]{defaultComboBoxModel.getElementAt(i2)}, encoder);
            }
        }
    }

    /* loaded from: rt.jar:java/beans/MetaData$javax_swing_tree_DefaultMutableTreeNode_PersistenceDelegate.class */
    static final class javax_swing_tree_DefaultMutableTreeNode_PersistenceDelegate extends DefaultPersistenceDelegate {
        javax_swing_tree_DefaultMutableTreeNode_PersistenceDelegate() {
        }

        @Override // java.beans.DefaultPersistenceDelegate, java.beans.PersistenceDelegate
        protected void initialize(Class<?> cls, Object obj, Object obj2, Encoder encoder) {
            super.initialize(cls, obj, obj2, encoder);
            DefaultMutableTreeNode defaultMutableTreeNode = (DefaultMutableTreeNode) obj;
            for (int childCount = ((DefaultMutableTreeNode) obj2).getChildCount(); childCount < defaultMutableTreeNode.getChildCount(); childCount++) {
                invokeStatement(obj, "add", new Object[]{defaultMutableTreeNode.getChildAt(childCount)}, encoder);
            }
        }
    }

    /* loaded from: rt.jar:java/beans/MetaData$javax_swing_ToolTipManager_PersistenceDelegate.class */
    static final class javax_swing_ToolTipManager_PersistenceDelegate extends PersistenceDelegate {
        javax_swing_ToolTipManager_PersistenceDelegate() {
        }

        @Override // java.beans.PersistenceDelegate
        protected Expression instantiate(Object obj, Encoder encoder) {
            return new Expression(obj, ToolTipManager.class, "sharedInstance", new Object[0]);
        }
    }

    /* loaded from: rt.jar:java/beans/MetaData$javax_swing_JTabbedPane_PersistenceDelegate.class */
    static final class javax_swing_JTabbedPane_PersistenceDelegate extends DefaultPersistenceDelegate {
        javax_swing_JTabbedPane_PersistenceDelegate() {
        }

        @Override // java.beans.DefaultPersistenceDelegate, java.beans.PersistenceDelegate
        protected void initialize(Class<?> cls, Object obj, Object obj2, Encoder encoder) {
            super.initialize(cls, obj, obj2, encoder);
            JTabbedPane jTabbedPane = (JTabbedPane) obj;
            for (int i2 = 0; i2 < jTabbedPane.getTabCount(); i2++) {
                invokeStatement(obj, "addTab", new Object[]{jTabbedPane.getTitleAt(i2), jTabbedPane.getIconAt(i2), jTabbedPane.getComponentAt(i2)}, encoder);
            }
        }
    }

    /* loaded from: rt.jar:java/beans/MetaData$javax_swing_Box_PersistenceDelegate.class */
    static final class javax_swing_Box_PersistenceDelegate extends DefaultPersistenceDelegate {
        javax_swing_Box_PersistenceDelegate() {
        }

        @Override // java.beans.DefaultPersistenceDelegate, java.beans.PersistenceDelegate
        protected boolean mutatesTo(Object obj, Object obj2) {
            return super.mutatesTo(obj, obj2) && getAxis(obj).equals(getAxis(obj2));
        }

        @Override // java.beans.DefaultPersistenceDelegate, java.beans.PersistenceDelegate
        protected Expression instantiate(Object obj, Encoder encoder) {
            return new Expression(obj, obj.getClass(), "new", new Object[]{getAxis(obj)});
        }

        private Integer getAxis(Object obj) {
            return (Integer) MetaData.getPrivateFieldValue(((Box) obj).getLayout(), "javax.swing.BoxLayout.axis");
        }
    }

    /* loaded from: rt.jar:java/beans/MetaData$javax_swing_JMenu_PersistenceDelegate.class */
    static final class javax_swing_JMenu_PersistenceDelegate extends DefaultPersistenceDelegate {
        javax_swing_JMenu_PersistenceDelegate() {
        }

        @Override // java.beans.DefaultPersistenceDelegate, java.beans.PersistenceDelegate
        protected void initialize(Class<?> cls, Object obj, Object obj2, Encoder encoder) {
            super.initialize(cls, obj, obj2, encoder);
            for (Component component : ((JMenu) obj).getMenuComponents()) {
                invokeStatement(obj, "add", new Object[]{component}, encoder);
            }
        }
    }

    /* loaded from: rt.jar:java/beans/MetaData$javax_swing_border_MatteBorder_PersistenceDelegate.class */
    static final class javax_swing_border_MatteBorder_PersistenceDelegate extends PersistenceDelegate {
        javax_swing_border_MatteBorder_PersistenceDelegate() {
        }

        @Override // java.beans.PersistenceDelegate
        protected Expression instantiate(Object obj, Encoder encoder) {
            MatteBorder matteBorder = (MatteBorder) obj;
            Insets borderInsets = matteBorder.getBorderInsets();
            Icon tileIcon = matteBorder.getTileIcon();
            if (tileIcon == null) {
                tileIcon = matteBorder.getMatteColor();
            }
            return new Expression(matteBorder, matteBorder.getClass(), "new", new Object[]{Integer.valueOf(borderInsets.top), Integer.valueOf(borderInsets.left), Integer.valueOf(borderInsets.bottom), Integer.valueOf(borderInsets.right), tileIcon});
        }
    }

    /* loaded from: rt.jar:java/beans/MetaData$sun_swing_PrintColorUIResource_PersistenceDelegate.class */
    static final class sun_swing_PrintColorUIResource_PersistenceDelegate extends PersistenceDelegate {
        sun_swing_PrintColorUIResource_PersistenceDelegate() {
        }

        @Override // java.beans.PersistenceDelegate
        protected boolean mutatesTo(Object obj, Object obj2) {
            return obj.equals(obj2);
        }

        @Override // java.beans.PersistenceDelegate
        protected Expression instantiate(Object obj, Encoder encoder) {
            Color color = (Color) obj;
            return new Expression(color, ColorUIResource.class, "new", new Object[]{Integer.valueOf(color.getRGB())});
        }
    }

    static {
        internalPersistenceDelegates.put("java.net.URI", new PrimitivePersistenceDelegate());
        internalPersistenceDelegates.put("javax.swing.plaf.BorderUIResource$MatteBorderUIResource", new javax_swing_border_MatteBorder_PersistenceDelegate());
        internalPersistenceDelegates.put("javax.swing.plaf.FontUIResource", new java_awt_Font_PersistenceDelegate());
        internalPersistenceDelegates.put("javax.swing.KeyStroke", new java_awt_AWTKeyStroke_PersistenceDelegate());
        internalPersistenceDelegates.put("java.sql.Date", new java_util_Date_PersistenceDelegate());
        internalPersistenceDelegates.put("java.sql.Time", new java_util_Date_PersistenceDelegate());
        internalPersistenceDelegates.put("java.util.JumboEnumSet", new java_util_EnumSet_PersistenceDelegate());
        internalPersistenceDelegates.put("java.util.RegularEnumSet", new java_util_EnumSet_PersistenceDelegate());
    }

    public static synchronized PersistenceDelegate getPersistenceDelegate(Class cls) throws SecurityException {
        if (cls == null) {
            return nullPersistenceDelegate;
        }
        if (Enum.class.isAssignableFrom(cls)) {
            return enumPersistenceDelegate;
        }
        if (null != XMLEncoder.primitiveTypeFor(cls)) {
            return primitivePersistenceDelegate;
        }
        if (cls.isArray()) {
            if (arrayPersistenceDelegate == null) {
                arrayPersistenceDelegate = new ArrayPersistenceDelegate();
            }
            return arrayPersistenceDelegate;
        }
        try {
            if (Proxy.isProxyClass(cls)) {
                if (proxyPersistenceDelegate == null) {
                    proxyPersistenceDelegate = new ProxyPersistenceDelegate();
                }
                return proxyPersistenceDelegate;
            }
        } catch (Exception e2) {
        }
        String name = cls.getName();
        PersistenceDelegate defaultPersistenceDelegate2 = (PersistenceDelegate) getBeanAttribute(cls, "persistenceDelegate");
        if (defaultPersistenceDelegate2 == null) {
            defaultPersistenceDelegate2 = internalPersistenceDelegates.get(name);
            if (defaultPersistenceDelegate2 != null) {
                return defaultPersistenceDelegate2;
            }
            internalPersistenceDelegates.put(name, defaultPersistenceDelegate);
            try {
                defaultPersistenceDelegate2 = (PersistenceDelegate) Class.forName("java.beans.MetaData$" + cls.getName().replace('.', '_') + "_PersistenceDelegate").newInstance();
                internalPersistenceDelegates.put(name, defaultPersistenceDelegate2);
            } catch (ClassNotFoundException e3) {
                String[] constructorProperties = getConstructorProperties(cls);
                if (constructorProperties != null) {
                    defaultPersistenceDelegate2 = new DefaultPersistenceDelegate(constructorProperties);
                    internalPersistenceDelegates.put(name, defaultPersistenceDelegate2);
                }
            } catch (Exception e4) {
                System.err.println("Internal error: " + ((Object) e4));
            }
        }
        return defaultPersistenceDelegate2 != null ? defaultPersistenceDelegate2 : defaultPersistenceDelegate;
    }

    private static String[] getConstructorProperties(Class<?> cls) throws SecurityException {
        String[] strArr = null;
        int length = 0;
        for (Constructor<?> constructor : cls.getConstructors()) {
            String[] annotationValue = getAnnotationValue(constructor);
            if (annotationValue != null && length < annotationValue.length && isValid(constructor, annotationValue)) {
                strArr = annotationValue;
                length = annotationValue.length;
            }
        }
        return strArr;
    }

    private static String[] getAnnotationValue(Constructor<?> constructor) {
        ConstructorProperties constructorProperties = (ConstructorProperties) constructor.getAnnotation(ConstructorProperties.class);
        if (constructorProperties != null) {
            return constructorProperties.value();
        }
        return null;
    }

    private static boolean isValid(Constructor<?> constructor, String[] strArr) {
        if (strArr.length != constructor.getParameterTypes().length) {
            return false;
        }
        for (String str : strArr) {
            if (str == null) {
                return false;
            }
        }
        return true;
    }

    private static Object getBeanAttribute(Class<?> cls, String str) {
        try {
            return Introspector.getBeanInfo(cls).getBeanDescriptor().getValue(str);
        } catch (IntrospectionException e2) {
            return null;
        }
    }

    static Object getPrivateFieldValue(Object obj, String str) {
        Field field = fields.get(str);
        if (field == null) {
            int iLastIndexOf = str.lastIndexOf(46);
            final String strSubstring = str.substring(0, iLastIndexOf);
            final String strSubstring2 = str.substring(1 + iLastIndexOf);
            field = (Field) AccessController.doPrivileged(new PrivilegedAction<Field>() { // from class: java.beans.MetaData.1
                /* JADX WARN: Can't rename method to resolve collision */
                @Override // java.security.PrivilegedAction
                /* renamed from: run */
                public Field run2() throws SecurityException {
                    try {
                        Field declaredField = Class.forName(strSubstring).getDeclaredField(strSubstring2);
                        declaredField.setAccessible(true);
                        return declaredField;
                    } catch (ClassNotFoundException e2) {
                        throw new IllegalStateException("Could not find class", e2);
                    } catch (NoSuchFieldException e3) {
                        throw new IllegalStateException("Could not find field", e3);
                    }
                }
            });
            fields.put(str, field);
        }
        try {
            return field.get(obj);
        } catch (IllegalAccessException e2) {
            throw new IllegalStateException("Could not get value of the field", e2);
        }
    }
}
