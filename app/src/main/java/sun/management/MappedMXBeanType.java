package sun.management;

import com.sun.javafx.fxml.BeanAdapter;
import com.sun.management.VMOption;
import com.sun.org.apache.xalan.internal.templates.Constants;
import java.io.InvalidObjectException;
import java.lang.management.LockInfo;
import java.lang.management.MemoryNotificationInfo;
import java.lang.management.MemoryUsage;
import java.lang.management.MonitorInfo;
import java.lang.management.ThreadInfo;
import java.lang.reflect.Array;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;
import javax.management.openmbean.ArrayType;
import javax.management.openmbean.CompositeData;
import javax.management.openmbean.CompositeDataSupport;
import javax.management.openmbean.CompositeType;
import javax.management.openmbean.OpenDataException;
import javax.management.openmbean.OpenType;
import javax.management.openmbean.SimpleType;
import javax.management.openmbean.TabularData;
import javax.management.openmbean.TabularDataSupport;
import javax.management.openmbean.TabularType;

/* loaded from: rt.jar:sun/management/MappedMXBeanType.class */
public abstract class MappedMXBeanType {
    boolean isBasicType = false;
    OpenType<?> openType = inProgress;
    Class<?> mappedTypeClass;
    private static final String KEY = "key";
    private static final String VALUE = "value";
    private static final OpenType<?> inProgress;
    private static final OpenType[] simpleTypes;
    private static final WeakHashMap<Type, MappedMXBeanType> convertedTypes = new WeakHashMap<>();
    private static final String[] mapIndexNames = {"key"};
    private static final String[] mapItemNames = {"key", "value"};
    private static final Class<?> COMPOSITE_DATA_CLASS = CompositeData.class;

    abstract Type getJavaType();

    abstract String getName();

    abstract Object toOpenTypeData(Object obj) throws OpenDataException;

    abstract Object toJavaTypeData(Object obj) throws OpenDataException, InvalidObjectException;

    static {
        try {
            inProgress = new InProgress();
            simpleTypes = new OpenType[]{SimpleType.BIGDECIMAL, SimpleType.BIGINTEGER, SimpleType.BOOLEAN, SimpleType.BYTE, SimpleType.CHARACTER, SimpleType.DATE, SimpleType.DOUBLE, SimpleType.FLOAT, SimpleType.INTEGER, SimpleType.LONG, SimpleType.OBJECTNAME, SimpleType.SHORT, SimpleType.STRING, SimpleType.VOID};
            for (int i2 = 0; i2 < simpleTypes.length; i2++) {
                try {
                    OpenType openType = simpleTypes[i2];
                    try {
                        Class<?> cls = Class.forName(openType.getClassName(), false, MappedMXBeanType.class.getClassLoader());
                        newBasicType(cls, openType);
                        if (cls.getName().startsWith("java.lang.")) {
                            try {
                                newBasicType((Class) cls.getField("TYPE").get(null), openType);
                            } catch (IllegalAccessException e2) {
                                throw new AssertionError(e2);
                            } catch (NoSuchFieldException e3) {
                            }
                        }
                    } catch (ClassNotFoundException e4) {
                        throw new AssertionError(e4);
                    } catch (OpenDataException e5) {
                        throw new AssertionError(e5);
                    }
                } catch (OpenDataException e6) {
                    throw new AssertionError(e6);
                }
            }
        } catch (OpenDataException e7) {
            throw new AssertionError(e7);
        }
    }

    static synchronized MappedMXBeanType newMappedType(Type type) throws OpenDataException {
        MappedMXBeanType genericArrayMXBeanType = null;
        if (type instanceof Class) {
            Class cls = (Class) type;
            genericArrayMXBeanType = cls.isEnum() ? new EnumMXBeanType(cls) : cls.isArray() ? new ArrayMXBeanType(cls) : new CompositeDataMXBeanType(cls);
        } else if (type instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) type;
            Type rawType = parameterizedType.getRawType();
            if (rawType instanceof Class) {
                Class cls2 = (Class) rawType;
                if (cls2 == List.class) {
                    genericArrayMXBeanType = new ListMXBeanType(parameterizedType);
                } else if (cls2 == Map.class) {
                    genericArrayMXBeanType = new MapMXBeanType(parameterizedType);
                }
            }
        } else if (type instanceof GenericArrayType) {
            genericArrayMXBeanType = new GenericArrayMXBeanType((GenericArrayType) type);
        }
        if (genericArrayMXBeanType == null) {
            throw new OpenDataException(((Object) type) + " is not a supported MXBean type.");
        }
        convertedTypes.put(type, genericArrayMXBeanType);
        return genericArrayMXBeanType;
    }

    static synchronized MappedMXBeanType newBasicType(Class<?> cls, OpenType<?> openType) throws OpenDataException {
        BasicMXBeanType basicMXBeanType = new BasicMXBeanType(cls, openType);
        convertedTypes.put(cls, basicMXBeanType);
        return basicMXBeanType;
    }

    static synchronized MappedMXBeanType getMappedType(Type type) throws OpenDataException {
        MappedMXBeanType mappedMXBeanTypeNewMappedType = convertedTypes.get(type);
        if (mappedMXBeanTypeNewMappedType == null) {
            mappedMXBeanTypeNewMappedType = newMappedType(type);
        }
        if (mappedMXBeanTypeNewMappedType.getOpenType() instanceof InProgress) {
            throw new OpenDataException("Recursive data structure");
        }
        return mappedMXBeanTypeNewMappedType;
    }

    public static synchronized OpenType<?> toOpenType(Type type) throws OpenDataException {
        return getMappedType(type).getOpenType();
    }

    public static Object toJavaTypeData(Object obj, Type type) throws OpenDataException, InvalidObjectException {
        if (obj == null) {
            return null;
        }
        return getMappedType(type).toJavaTypeData(obj);
    }

    public static Object toOpenTypeData(Object obj, Type type) throws OpenDataException {
        if (obj == null) {
            return null;
        }
        return getMappedType(type).toOpenTypeData(obj);
    }

    OpenType<?> getOpenType() {
        return this.openType;
    }

    boolean isBasicType() {
        return this.isBasicType;
    }

    String getTypeName() {
        return getMappedTypeClass().getName();
    }

    Class<?> getMappedTypeClass() {
        return this.mappedTypeClass;
    }

    /* loaded from: rt.jar:sun/management/MappedMXBeanType$BasicMXBeanType.class */
    static class BasicMXBeanType extends MappedMXBeanType {
        final Class<?> basicType;

        BasicMXBeanType(Class<?> cls, OpenType<?> openType) {
            this.basicType = cls;
            this.openType = openType;
            this.mappedTypeClass = cls;
            this.isBasicType = true;
        }

        @Override // sun.management.MappedMXBeanType
        Type getJavaType() {
            return this.basicType;
        }

        @Override // sun.management.MappedMXBeanType
        String getName() {
            return this.basicType.getName();
        }

        @Override // sun.management.MappedMXBeanType
        Object toOpenTypeData(Object obj) throws OpenDataException {
            return obj;
        }

        @Override // sun.management.MappedMXBeanType
        Object toJavaTypeData(Object obj) throws OpenDataException, InvalidObjectException {
            return obj;
        }
    }

    /* loaded from: rt.jar:sun/management/MappedMXBeanType$EnumMXBeanType.class */
    static class EnumMXBeanType extends MappedMXBeanType {
        final Class enumClass;

        EnumMXBeanType(Class<?> cls) {
            this.enumClass = cls;
            this.openType = SimpleType.STRING;
            this.mappedTypeClass = String.class;
        }

        @Override // sun.management.MappedMXBeanType
        Type getJavaType() {
            return this.enumClass;
        }

        @Override // sun.management.MappedMXBeanType
        String getName() {
            return this.enumClass.getName();
        }

        @Override // sun.management.MappedMXBeanType
        Object toOpenTypeData(Object obj) throws OpenDataException {
            return ((Enum) obj).name();
        }

        @Override // sun.management.MappedMXBeanType
        Object toJavaTypeData(Object obj) throws OpenDataException, InvalidObjectException {
            try {
                return Enum.valueOf(this.enumClass, (String) obj);
            } catch (IllegalArgumentException e2) {
                InvalidObjectException invalidObjectException = new InvalidObjectException("Enum constant named " + ((String) obj) + " is missing");
                invalidObjectException.initCause(e2);
                throw invalidObjectException;
            }
        }
    }

    /* loaded from: rt.jar:sun/management/MappedMXBeanType$ArrayMXBeanType.class */
    static class ArrayMXBeanType extends MappedMXBeanType {
        final Class<?> arrayClass;
        protected MappedMXBeanType componentType;
        protected MappedMXBeanType baseElementType;

        ArrayMXBeanType(Class<?> cls) throws OpenDataException {
            this.arrayClass = cls;
            this.componentType = getMappedType(cls.getComponentType());
            StringBuilder sb = new StringBuilder();
            Class<?> componentType = cls;
            int i2 = 0;
            while (componentType.isArray()) {
                sb.append('[');
                componentType = componentType.getComponentType();
                i2++;
            }
            this.baseElementType = getMappedType(componentType);
            if (componentType.isPrimitive()) {
                sb = new StringBuilder(cls.getName());
            } else {
                sb.append("L" + this.baseElementType.getTypeName() + ";");
            }
            try {
                this.mappedTypeClass = Class.forName(sb.toString());
                this.openType = new ArrayType(i2, this.baseElementType.getOpenType());
            } catch (ClassNotFoundException e2) {
                OpenDataException openDataException = new OpenDataException("Cannot obtain array class");
                openDataException.initCause(e2);
                throw openDataException;
            }
        }

        protected ArrayMXBeanType() {
            this.arrayClass = null;
        }

        @Override // sun.management.MappedMXBeanType
        Type getJavaType() {
            return this.arrayClass;
        }

        @Override // sun.management.MappedMXBeanType
        String getName() {
            return this.arrayClass.getName();
        }

        @Override // sun.management.MappedMXBeanType
        Object toOpenTypeData(Object obj) throws OpenDataException {
            if (this.baseElementType.isBasicType()) {
                return obj;
            }
            Object[] objArr = (Object[]) obj;
            Object[] objArr2 = (Object[]) Array.newInstance(this.componentType.getMappedTypeClass(), objArr.length);
            int i2 = 0;
            for (Object obj2 : objArr) {
                if (obj2 == null) {
                    objArr2[i2] = null;
                } else {
                    objArr2[i2] = this.componentType.toOpenTypeData(obj2);
                }
                i2++;
            }
            return objArr2;
        }

        @Override // sun.management.MappedMXBeanType
        Object toJavaTypeData(Object obj) throws OpenDataException, InvalidObjectException {
            if (this.baseElementType.isBasicType()) {
                return obj;
            }
            Object[] objArr = (Object[]) obj;
            Object[] objArr2 = (Object[]) Array.newInstance((Class<?>) this.componentType.getJavaType(), objArr.length);
            int i2 = 0;
            for (Object obj2 : objArr) {
                if (obj2 == null) {
                    objArr2[i2] = null;
                } else {
                    objArr2[i2] = this.componentType.toJavaTypeData(obj2);
                }
                i2++;
            }
            return objArr2;
        }
    }

    /* loaded from: rt.jar:sun/management/MappedMXBeanType$GenericArrayMXBeanType.class */
    static class GenericArrayMXBeanType extends ArrayMXBeanType {
        final GenericArrayType gtype;

        /* JADX WARN: Multi-variable type inference failed */
        /* JADX WARN: Type inference failed for: r0v28, types: [java.lang.reflect.Type] */
        GenericArrayMXBeanType(GenericArrayType genericArrayType) throws OpenDataException {
            this.gtype = genericArrayType;
            this.componentType = getMappedType(genericArrayType.getGenericComponentType());
            StringBuilder sb = new StringBuilder();
            GenericArrayType genericComponentType = genericArrayType;
            int i2 = 0;
            while (genericComponentType instanceof GenericArrayType) {
                sb.append('[');
                genericComponentType = genericComponentType.getGenericComponentType();
                i2++;
            }
            this.baseElementType = getMappedType(genericComponentType);
            if ((genericComponentType instanceof Class) && ((Class) genericComponentType).isPrimitive()) {
                sb = new StringBuilder(genericArrayType.toString());
            } else {
                sb.append("L" + this.baseElementType.getTypeName() + ";");
            }
            try {
                this.mappedTypeClass = Class.forName(sb.toString());
                this.openType = new ArrayType(i2, this.baseElementType.getOpenType());
            } catch (ClassNotFoundException e2) {
                OpenDataException openDataException = new OpenDataException("Cannot obtain array class");
                openDataException.initCause(e2);
                throw openDataException;
            }
        }

        @Override // sun.management.MappedMXBeanType.ArrayMXBeanType, sun.management.MappedMXBeanType
        Type getJavaType() {
            return this.gtype;
        }

        @Override // sun.management.MappedMXBeanType.ArrayMXBeanType, sun.management.MappedMXBeanType
        String getName() {
            return this.gtype.toString();
        }
    }

    /* loaded from: rt.jar:sun/management/MappedMXBeanType$ListMXBeanType.class */
    static class ListMXBeanType extends MappedMXBeanType {
        final ParameterizedType javaType;
        final MappedMXBeanType paramType;
        final String typeName;
        static final /* synthetic */ boolean $assertionsDisabled;

        static {
            $assertionsDisabled = !MappedMXBeanType.class.desiredAssertionStatus();
        }

        ListMXBeanType(ParameterizedType parameterizedType) throws OpenDataException {
            this.javaType = parameterizedType;
            Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
            if (!$assertionsDisabled && actualTypeArguments.length != 1) {
                throw new AssertionError();
            }
            if (!(actualTypeArguments[0] instanceof Class)) {
                throw new OpenDataException("Element Type for " + ((Object) parameterizedType) + " not supported");
            }
            Class cls = (Class) actualTypeArguments[0];
            if (cls.isArray()) {
                throw new OpenDataException("Element Type for " + ((Object) parameterizedType) + " not supported");
            }
            this.paramType = getMappedType(cls);
            this.typeName = "List<" + this.paramType.getName() + ">";
            try {
                this.mappedTypeClass = Class.forName("[L" + this.paramType.getTypeName() + ";");
                this.openType = new ArrayType(1, this.paramType.getOpenType());
            } catch (ClassNotFoundException e2) {
                OpenDataException openDataException = new OpenDataException("Array class not found");
                openDataException.initCause(e2);
                throw openDataException;
            }
        }

        @Override // sun.management.MappedMXBeanType
        Type getJavaType() {
            return this.javaType;
        }

        @Override // sun.management.MappedMXBeanType
        String getName() {
            return this.typeName;
        }

        @Override // sun.management.MappedMXBeanType
        Object toOpenTypeData(Object obj) throws OpenDataException {
            List list = (List) obj;
            Object[] objArr = (Object[]) Array.newInstance(this.paramType.getMappedTypeClass(), list.size());
            int i2 = 0;
            Iterator it = list.iterator();
            while (it.hasNext()) {
                int i3 = i2;
                i2++;
                objArr[i3] = this.paramType.toOpenTypeData(it.next());
            }
            return objArr;
        }

        @Override // sun.management.MappedMXBeanType
        Object toJavaTypeData(Object obj) throws OpenDataException, InvalidObjectException {
            Object[] objArr = (Object[]) obj;
            ArrayList arrayList = new ArrayList(objArr.length);
            for (Object obj2 : objArr) {
                arrayList.add(this.paramType.toJavaTypeData(obj2));
            }
            return arrayList;
        }
    }

    /* loaded from: rt.jar:sun/management/MappedMXBeanType$MapMXBeanType.class */
    static class MapMXBeanType extends MappedMXBeanType {
        final ParameterizedType javaType;
        final MappedMXBeanType keyType;
        final MappedMXBeanType valueType;
        final String typeName;
        static final /* synthetic */ boolean $assertionsDisabled;

        static {
            $assertionsDisabled = !MappedMXBeanType.class.desiredAssertionStatus();
        }

        MapMXBeanType(ParameterizedType parameterizedType) throws OpenDataException {
            this.javaType = parameterizedType;
            Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
            if (!$assertionsDisabled && actualTypeArguments.length != 2) {
                throw new AssertionError();
            }
            this.keyType = getMappedType(actualTypeArguments[0]);
            this.valueType = getMappedType(actualTypeArguments[1]);
            this.typeName = "Map<" + this.keyType.getName() + "," + this.valueType.getName() + ">";
            this.openType = new TabularType(this.typeName, this.typeName, new CompositeType(this.typeName, this.typeName, MappedMXBeanType.mapItemNames, MappedMXBeanType.mapItemNames, new OpenType[]{this.keyType.getOpenType(), this.valueType.getOpenType()}), MappedMXBeanType.mapIndexNames);
            this.mappedTypeClass = TabularData.class;
        }

        @Override // sun.management.MappedMXBeanType
        Type getJavaType() {
            return this.javaType;
        }

        @Override // sun.management.MappedMXBeanType
        String getName() {
            return this.typeName;
        }

        @Override // sun.management.MappedMXBeanType
        Object toOpenTypeData(Object obj) throws OpenDataException {
            TabularType tabularType = (TabularType) this.openType;
            TabularDataSupport tabularDataSupport = new TabularDataSupport(tabularType);
            CompositeType rowType = tabularType.getRowType();
            for (Map.Entry entry : ((Map) obj).entrySet()) {
                tabularDataSupport.put(new CompositeDataSupport(rowType, MappedMXBeanType.mapItemNames, new Object[]{this.keyType.toOpenTypeData(entry.getKey()), this.valueType.toOpenTypeData(entry.getValue())}));
            }
            return tabularDataSupport;
        }

        @Override // sun.management.MappedMXBeanType
        Object toJavaTypeData(Object obj) throws OpenDataException, InvalidObjectException {
            HashMap map = new HashMap();
            Iterator<?> it = ((TabularData) obj).values().iterator();
            while (it.hasNext()) {
                CompositeData compositeData = (CompositeData) it.next();
                map.put(this.keyType.toJavaTypeData(compositeData.get("key")), this.valueType.toJavaTypeData(compositeData.get("value")));
            }
            return map;
        }
    }

    /* loaded from: rt.jar:sun/management/MappedMXBeanType$CompositeDataMXBeanType.class */
    static class CompositeDataMXBeanType extends MappedMXBeanType {
        final Class<?> javaClass;
        final boolean isCompositeData;
        Method fromMethod;

        CompositeDataMXBeanType(Class<?> cls) throws OpenDataException {
            String strSubstring;
            this.fromMethod = null;
            this.javaClass = cls;
            this.mappedTypeClass = MappedMXBeanType.COMPOSITE_DATA_CLASS;
            try {
                this.fromMethod = (Method) AccessController.doPrivileged(new PrivilegedExceptionAction<Method>() { // from class: sun.management.MappedMXBeanType.CompositeDataMXBeanType.1
                    /* JADX WARN: Can't rename method to resolve collision */
                    @Override // java.security.PrivilegedExceptionAction
                    public Method run() throws NoSuchMethodException {
                        return CompositeDataMXBeanType.this.javaClass.getMethod(Constants.ATTRNAME_FROM, MappedMXBeanType.COMPOSITE_DATA_CLASS);
                    }
                });
            } catch (PrivilegedActionException e2) {
            }
            if (MappedMXBeanType.COMPOSITE_DATA_CLASS.isAssignableFrom(cls)) {
                this.isCompositeData = true;
                this.openType = null;
                return;
            }
            this.isCompositeData = false;
            Method[] methodArr = (Method[]) AccessController.doPrivileged(new PrivilegedAction<Method[]>() { // from class: sun.management.MappedMXBeanType.CompositeDataMXBeanType.2
                /* JADX WARN: Can't rename method to resolve collision */
                @Override // java.security.PrivilegedAction
                /* renamed from: run */
                public Method[] run2() {
                    return CompositeDataMXBeanType.this.javaClass.getMethods();
                }
            });
            ArrayList arrayList = new ArrayList();
            ArrayList arrayList2 = new ArrayList();
            for (Method method : methodArr) {
                String name = method.getName();
                Type genericReturnType = method.getGenericReturnType();
                if (name.startsWith("get")) {
                    strSubstring = name.substring(3);
                } else if (name.startsWith(BeanAdapter.IS_PREFIX) && (genericReturnType instanceof Class) && ((Class) genericReturnType) == Boolean.TYPE) {
                    strSubstring = name.substring(2);
                }
                if (!strSubstring.equals("") && method.getParameterTypes().length <= 0 && genericReturnType != Void.TYPE && !strSubstring.equals("Class")) {
                    arrayList.add(MappedMXBeanType.decapitalize(strSubstring));
                    arrayList2.add(toOpenType(genericReturnType));
                }
            }
            String[] strArr = (String[]) arrayList.toArray(new String[0]);
            this.openType = new CompositeType(cls.getName(), cls.getName(), strArr, strArr, (OpenType[]) arrayList2.toArray(new OpenType[0]));
        }

        @Override // sun.management.MappedMXBeanType
        Type getJavaType() {
            return this.javaClass;
        }

        @Override // sun.management.MappedMXBeanType
        String getName() {
            return this.javaClass.getName();
        }

        @Override // sun.management.MappedMXBeanType
        Object toOpenTypeData(Object obj) throws OpenDataException {
            if (obj instanceof MemoryUsage) {
                return MemoryUsageCompositeData.toCompositeData((MemoryUsage) obj);
            }
            if (obj instanceof ThreadInfo) {
                return ThreadInfoCompositeData.toCompositeData((ThreadInfo) obj);
            }
            if (obj instanceof LockInfo) {
                if (obj instanceof MonitorInfo) {
                    return MonitorInfoCompositeData.toCompositeData((MonitorInfo) obj);
                }
                return LockInfoCompositeData.toCompositeData((LockInfo) obj);
            }
            if (obj instanceof MemoryNotificationInfo) {
                return MemoryNotifInfoCompositeData.toCompositeData((MemoryNotificationInfo) obj);
            }
            if (obj instanceof VMOption) {
                return VMOptionCompositeData.toCompositeData((VMOption) obj);
            }
            if (this.isCompositeData) {
                CompositeData compositeData = (CompositeData) obj;
                CompositeType compositeType = compositeData.getCompositeType();
                String[] strArr = (String[]) compositeType.keySet().toArray(new String[0]);
                return new CompositeDataSupport(compositeType, strArr, compositeData.getAll(strArr));
            }
            throw new OpenDataException(this.javaClass.getName() + " is not supported for platform MXBeans");
        }

        @Override // sun.management.MappedMXBeanType
        Object toJavaTypeData(Object obj) throws OpenDataException, InvalidObjectException {
            if (this.fromMethod == null) {
                throw new AssertionError((Object) "Does not support data conversion");
            }
            try {
                return this.fromMethod.invoke(null, obj);
            } catch (IllegalAccessException e2) {
                throw new AssertionError(e2);
            } catch (InvocationTargetException e3) {
                OpenDataException openDataException = new OpenDataException("Failed to invoke " + this.fromMethod.getName() + " to convert CompositeData  to " + this.javaClass.getName());
                openDataException.initCause(e3);
                throw openDataException;
            }
        }
    }

    /* loaded from: rt.jar:sun/management/MappedMXBeanType$InProgress.class */
    private static class InProgress extends OpenType {
        private static final String description = "Marker to detect recursive type use -- internal use only!";
        private static final long serialVersionUID = -3413063475064374490L;

        InProgress() throws OpenDataException {
            super("java.lang.String", "java.lang.String", description);
        }

        @Override // javax.management.openmbean.OpenType
        public String toString() {
            return description;
        }

        @Override // javax.management.openmbean.OpenType
        public int hashCode() {
            return 0;
        }

        @Override // javax.management.openmbean.OpenType
        public boolean equals(Object obj) {
            return false;
        }

        @Override // javax.management.openmbean.OpenType
        public boolean isValue(Object obj) {
            return false;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static String decapitalize(String str) {
        if (str == null || str.length() == 0) {
            return str;
        }
        if (str.length() > 1 && Character.isUpperCase(str.charAt(1)) && Character.isUpperCase(str.charAt(0))) {
            return str;
        }
        char[] charArray = str.toCharArray();
        charArray[0] = Character.toLowerCase(charArray[0]);
        return new String(charArray);
    }
}
