package com.sun.jmx.mbeanserver;

import com.sun.javafx.fxml.BeanAdapter;
import com.sun.jmx.remote.util.EnvHelp;
import com.sun.org.apache.xalan.internal.templates.Constants;
import java.io.InvalidObjectException;
import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.ref.WeakReference;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Proxy;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.BitSet;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.WeakHashMap;
import javax.management.JMX;
import javax.management.ObjectName;
import javax.management.openmbean.ArrayType;
import javax.management.openmbean.CompositeData;
import javax.management.openmbean.CompositeDataInvocationHandler;
import javax.management.openmbean.CompositeDataSupport;
import javax.management.openmbean.CompositeDataView;
import javax.management.openmbean.CompositeType;
import javax.management.openmbean.OpenDataException;
import javax.management.openmbean.OpenType;
import javax.management.openmbean.SimpleType;
import javax.management.openmbean.TabularData;
import javax.management.openmbean.TabularDataSupport;
import javax.management.openmbean.TabularType;
import org.apache.commons.math3.geometry.VectorFormat;
import sun.reflect.misc.MethodUtil;
import sun.reflect.misc.ReflectUtil;

/* loaded from: rt.jar:com/sun/jmx/mbeanserver/DefaultMXBeanMappingFactory.class */
public class DefaultMXBeanMappingFactory extends MXBeanMappingFactory {
    private static final Mappings mappings;
    private static final List<MXBeanMapping> permanentMappings;
    private static final String[] keyArray;
    private static final String[] keyValueArray;
    private static final Map<Type, Type> inProgress;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !DefaultMXBeanMappingFactory.class.desiredAssertionStatus();
        mappings = new Mappings();
        permanentMappings = Util.newList();
        for (OpenType openType : new OpenType[]{SimpleType.BIGDECIMAL, SimpleType.BIGINTEGER, SimpleType.BOOLEAN, SimpleType.BYTE, SimpleType.CHARACTER, SimpleType.DATE, SimpleType.DOUBLE, SimpleType.FLOAT, SimpleType.INTEGER, SimpleType.LONG, SimpleType.OBJECTNAME, SimpleType.SHORT, SimpleType.STRING, SimpleType.VOID}) {
            try {
                Class<?> cls = Class.forName(openType.getClassName(), false, ObjectName.class.getClassLoader());
                putPermanentMapping(cls, new IdentityMapping(cls, openType));
                if (cls.getName().startsWith("java.lang.")) {
                    try {
                        Class<Void> cls2 = (Class) cls.getField("TYPE").get(null);
                        putPermanentMapping(cls2, new IdentityMapping(cls2, openType));
                        if (cls2 != Void.TYPE) {
                            Class<?> cls3 = Array.newInstance(cls2, 0).getClass();
                            putPermanentMapping(cls3, new IdentityMapping(cls3, ArrayType.getPrimitiveArrayType(cls3)));
                        }
                    } catch (IllegalAccessException e2) {
                        if (!$assertionsDisabled) {
                            throw new AssertionError();
                        }
                    } catch (NoSuchFieldException e3) {
                    }
                }
            } catch (ClassNotFoundException e4) {
                throw new Error(e4);
            }
        }
        keyArray = new String[]{"key"};
        keyValueArray = new String[]{"key", "value"};
        inProgress = Util.newIdentityHashMap();
    }

    /* loaded from: rt.jar:com/sun/jmx/mbeanserver/DefaultMXBeanMappingFactory$NonNullMXBeanMapping.class */
    static abstract class NonNullMXBeanMapping extends MXBeanMapping {
        abstract Object fromNonNullOpenValue(Object obj) throws InvalidObjectException;

        abstract Object toNonNullOpenValue(Object obj) throws OpenDataException;

        NonNullMXBeanMapping(Type type, OpenType<?> openType) {
            super(type, openType);
        }

        @Override // com.sun.jmx.mbeanserver.MXBeanMapping
        public final Object fromOpenValue(Object obj) throws InvalidObjectException {
            if (obj == null) {
                return null;
            }
            return fromNonNullOpenValue(obj);
        }

        @Override // com.sun.jmx.mbeanserver.MXBeanMapping
        public final Object toOpenValue(Object obj) throws OpenDataException {
            if (obj == null) {
                return null;
            }
            return toNonNullOpenValue(obj);
        }

        boolean isIdentity() {
            return false;
        }
    }

    static boolean isIdentity(MXBeanMapping mXBeanMapping) {
        return (mXBeanMapping instanceof NonNullMXBeanMapping) && ((NonNullMXBeanMapping) mXBeanMapping).isIdentity();
    }

    /* loaded from: rt.jar:com/sun/jmx/mbeanserver/DefaultMXBeanMappingFactory$Mappings.class */
    private static final class Mappings extends WeakHashMap<Type, WeakReference<MXBeanMapping>> {
        private Mappings() {
        }
    }

    private static synchronized MXBeanMapping getMapping(Type type) {
        WeakReference<MXBeanMapping> weakReference = mappings.get(type);
        if (weakReference == null) {
            return null;
        }
        return weakReference.get();
    }

    private static synchronized void putMapping(Type type, MXBeanMapping mXBeanMapping) {
        mappings.put(type, new WeakReference(mXBeanMapping));
    }

    private static synchronized void putPermanentMapping(Type type, MXBeanMapping mXBeanMapping) {
        putMapping(type, mXBeanMapping);
        permanentMappings.add(mXBeanMapping);
    }

    @Override // com.sun.jmx.mbeanserver.MXBeanMappingFactory
    public synchronized MXBeanMapping mappingForType(Type type, MXBeanMappingFactory mXBeanMappingFactory) throws OpenDataException {
        if (inProgress.containsKey(type)) {
            throw new OpenDataException("Recursive data structure, including " + MXBeanIntrospector.typeName(type));
        }
        MXBeanMapping mapping = getMapping(type);
        if (mapping != null) {
            return mapping;
        }
        inProgress.put(type, type);
        try {
            try {
                MXBeanMapping mXBeanMappingMakeMapping = makeMapping(type, mXBeanMappingFactory);
                inProgress.remove(type);
                putMapping(type, mXBeanMappingMakeMapping);
                return mXBeanMappingMakeMapping;
            } catch (OpenDataException e2) {
                throw openDataException("Cannot convert type: " + MXBeanIntrospector.typeName(type), e2);
            }
        } catch (Throwable th) {
            inProgress.remove(type);
            throw th;
        }
    }

    private MXBeanMapping makeMapping(Type type, MXBeanMappingFactory mXBeanMappingFactory) throws OpenDataException {
        if (type instanceof GenericArrayType) {
            return makeArrayOrCollectionMapping(type, ((GenericArrayType) type).getGenericComponentType(), mXBeanMappingFactory);
        }
        if (type instanceof Class) {
            Class<?> cls = (Class) type;
            if (cls.isEnum()) {
                return makeEnumMapping(cls, ElementType.class);
            }
            if (cls.isArray()) {
                return makeArrayOrCollectionMapping(cls, cls.getComponentType(), mXBeanMappingFactory);
            }
            if (JMX.isMXBeanInterface(cls)) {
                return makeMXBeanRefMapping(cls);
            }
            return makeCompositeMapping(cls, mXBeanMappingFactory);
        }
        if (type instanceof ParameterizedType) {
            return makeParameterizedTypeMapping((ParameterizedType) type, mXBeanMappingFactory);
        }
        throw new OpenDataException("Cannot map type: " + ((Object) type));
    }

    private static <T extends Enum<T>> MXBeanMapping makeEnumMapping(Class<?> cls, Class<T> cls2) {
        ReflectUtil.checkPackageAccess(cls);
        return new EnumMapping((Class) Util.cast(cls));
    }

    private MXBeanMapping makeArrayOrCollectionMapping(Type type, Type type2, MXBeanMappingFactory mXBeanMappingFactory) throws OpenDataException {
        String str;
        MXBeanMapping mXBeanMappingMappingForType = mXBeanMappingFactory.mappingForType(type2, mXBeanMappingFactory);
        ArrayType arrayType = ArrayType.getArrayType(mXBeanMappingMappingForType.getOpenType());
        Class<?> openClass = mXBeanMappingMappingForType.getOpenClass();
        if (openClass.isArray()) {
            str = "[" + openClass.getName();
        } else {
            str = "[L" + openClass.getName() + ";";
        }
        try {
            Class<?> cls = Class.forName(str);
            if (type instanceof ParameterizedType) {
                return new CollectionMapping(type, arrayType, cls, mXBeanMappingMappingForType);
            }
            if (isIdentity(mXBeanMappingMappingForType)) {
                return new IdentityMapping(type, arrayType);
            }
            return new ArrayMapping(type, arrayType, cls, mXBeanMappingMappingForType);
        } catch (ClassNotFoundException e2) {
            throw openDataException("Cannot obtain array class", e2);
        }
    }

    private MXBeanMapping makeTabularMapping(Type type, boolean z2, Type type2, Type type3, MXBeanMappingFactory mXBeanMappingFactory) throws OpenDataException {
        String strTypeName = MXBeanIntrospector.typeName(type);
        MXBeanMapping mXBeanMappingMappingForType = mXBeanMappingFactory.mappingForType(type2, mXBeanMappingFactory);
        MXBeanMapping mXBeanMappingMappingForType2 = mXBeanMappingFactory.mappingForType(type3, mXBeanMappingFactory);
        return new TabularMapping(type, z2, new TabularType(strTypeName, strTypeName, new CompositeType(strTypeName, strTypeName, keyValueArray, keyValueArray, new OpenType[]{mXBeanMappingMappingForType.getOpenType(), mXBeanMappingMappingForType2.getOpenType()}), keyArray), mXBeanMappingMappingForType, mXBeanMappingMappingForType2);
    }

    private MXBeanMapping makeParameterizedTypeMapping(ParameterizedType parameterizedType, MXBeanMappingFactory mXBeanMappingFactory) throws OpenDataException {
        Type rawType = parameterizedType.getRawType();
        if (rawType instanceof Class) {
            Class cls = (Class) rawType;
            if (cls == List.class || cls == Set.class || cls == SortedSet.class) {
                Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
                if (!$assertionsDisabled && actualTypeArguments.length != 1) {
                    throw new AssertionError();
                }
                if (cls == SortedSet.class) {
                    mustBeComparable(cls, actualTypeArguments[0]);
                }
                return makeArrayOrCollectionMapping(parameterizedType, actualTypeArguments[0], mXBeanMappingFactory);
            }
            boolean z2 = cls == SortedMap.class;
            if (cls == Map.class || z2) {
                Type[] actualTypeArguments2 = parameterizedType.getActualTypeArguments();
                if (!$assertionsDisabled && actualTypeArguments2.length != 2) {
                    throw new AssertionError();
                }
                if (z2) {
                    mustBeComparable(cls, actualTypeArguments2[0]);
                }
                return makeTabularMapping(parameterizedType, z2, actualTypeArguments2[0], actualTypeArguments2[1], mXBeanMappingFactory);
            }
        }
        throw new OpenDataException("Cannot convert type: " + ((Object) parameterizedType));
    }

    private static MXBeanMapping makeMXBeanRefMapping(Type type) throws OpenDataException {
        return new MXBeanRefMapping(type);
    }

    /* JADX WARN: Multi-variable type inference failed */
    private MXBeanMapping makeCompositeMapping(Class<?> cls, MXBeanMappingFactory mXBeanMappingFactory) throws OpenDataException {
        boolean z2 = cls.getName().equals("com.sun.management.GcInfo") && cls.getClassLoader() == null;
        ReflectUtil.checkPackageAccess(cls);
        List<Method> listEliminateCovariantMethods = MBeanAnalyzer.eliminateCovariantMethods(Arrays.asList(cls.getMethods()));
        SortedMap sortedMapNewSortedMap = Util.newSortedMap();
        for (Method method : listEliminateCovariantMethods) {
            String strPropertyName = propertyName(method);
            if (strPropertyName != null && (!z2 || !strPropertyName.equals("CompositeType"))) {
                Method method2 = (Method) sortedMapNewSortedMap.put(decapitalize(strPropertyName), method);
                if (method2 != null) {
                    throw new OpenDataException("Class " + cls.getName() + " has method name clash: " + method2.getName() + ", " + method.getName());
                }
            }
        }
        int size = sortedMapNewSortedMap.size();
        if (size == 0) {
            throw new OpenDataException("Can't map " + cls.getName() + " to an open data type");
        }
        Method[] methodArr = new Method[size];
        String[] strArr = new String[size];
        OpenType[] openTypeArr = new OpenType[size];
        int i2 = 0;
        for (Map.Entry entry : sortedMapNewSortedMap.entrySet()) {
            strArr[i2] = (String) entry.getKey();
            Method method3 = (Method) entry.getValue();
            methodArr[i2] = method3;
            openTypeArr[i2] = mXBeanMappingFactory.mappingForType(method3.getGenericReturnType(), mXBeanMappingFactory).getOpenType();
            i2++;
        }
        return new CompositeMapping(cls, new CompositeType(cls.getName(), cls.getName(), strArr, strArr, openTypeArr), strArr, methodArr, mXBeanMappingFactory);
    }

    /* loaded from: rt.jar:com/sun/jmx/mbeanserver/DefaultMXBeanMappingFactory$IdentityMapping.class */
    private static final class IdentityMapping extends NonNullMXBeanMapping {
        IdentityMapping(Type type, OpenType<?> openType) {
            super(type, openType);
        }

        @Override // com.sun.jmx.mbeanserver.DefaultMXBeanMappingFactory.NonNullMXBeanMapping
        boolean isIdentity() {
            return true;
        }

        @Override // com.sun.jmx.mbeanserver.DefaultMXBeanMappingFactory.NonNullMXBeanMapping
        Object fromNonNullOpenValue(Object obj) throws InvalidObjectException {
            return obj;
        }

        @Override // com.sun.jmx.mbeanserver.DefaultMXBeanMappingFactory.NonNullMXBeanMapping
        Object toNonNullOpenValue(Object obj) throws OpenDataException {
            return obj;
        }
    }

    /* loaded from: rt.jar:com/sun/jmx/mbeanserver/DefaultMXBeanMappingFactory$EnumMapping.class */
    private static final class EnumMapping<T extends Enum<T>> extends NonNullMXBeanMapping {
        private final Class<T> enumClass;

        EnumMapping(Class<T> cls) {
            super(cls, SimpleType.STRING);
            this.enumClass = cls;
        }

        @Override // com.sun.jmx.mbeanserver.DefaultMXBeanMappingFactory.NonNullMXBeanMapping
        final Object toNonNullOpenValue(Object obj) {
            return ((Enum) obj).name();
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        @Override // com.sun.jmx.mbeanserver.DefaultMXBeanMappingFactory.NonNullMXBeanMapping
        public final T fromNonNullOpenValue(Object obj) throws InvalidObjectException {
            try {
                return (T) Enum.valueOf(this.enumClass, (String) obj);
            } catch (Exception e2) {
                throw DefaultMXBeanMappingFactory.invalidObjectException("Cannot convert to enum: " + obj, e2);
            }
        }
    }

    /* loaded from: rt.jar:com/sun/jmx/mbeanserver/DefaultMXBeanMappingFactory$ArrayMapping.class */
    private static final class ArrayMapping extends NonNullMXBeanMapping {
        private final MXBeanMapping elementMapping;

        ArrayMapping(Type type, ArrayType<?> arrayType, Class<?> cls, MXBeanMapping mXBeanMapping) {
            super(type, arrayType);
            this.elementMapping = mXBeanMapping;
        }

        @Override // com.sun.jmx.mbeanserver.DefaultMXBeanMappingFactory.NonNullMXBeanMapping
        final Object toNonNullOpenValue(Object obj) throws OpenDataException {
            Object[] objArr = (Object[]) obj;
            int length = objArr.length;
            Object[] objArr2 = (Object[]) Array.newInstance(getOpenClass().getComponentType(), length);
            for (int i2 = 0; i2 < length; i2++) {
                objArr2[i2] = this.elementMapping.toOpenValue(objArr[i2]);
            }
            return objArr2;
        }

        @Override // com.sun.jmx.mbeanserver.DefaultMXBeanMappingFactory.NonNullMXBeanMapping
        final Object fromNonNullOpenValue(Object obj) throws InvalidObjectException {
            Object componentType;
            Object[] objArr = (Object[]) obj;
            Type javaType = getJavaType();
            if (javaType instanceof GenericArrayType) {
                componentType = ((GenericArrayType) javaType).getGenericComponentType();
            } else if ((javaType instanceof Class) && ((Class) javaType).isArray()) {
                componentType = ((Class) javaType).getComponentType();
            } else {
                throw new IllegalArgumentException("Not an array: " + ((Object) javaType));
            }
            Object[] objArr2 = (Object[]) Array.newInstance((Class<?>) componentType, objArr.length);
            for (int i2 = 0; i2 < objArr.length; i2++) {
                objArr2[i2] = this.elementMapping.fromOpenValue(objArr[i2]);
            }
            return objArr2;
        }

        @Override // com.sun.jmx.mbeanserver.MXBeanMapping
        public void checkReconstructible() throws InvalidObjectException {
            this.elementMapping.checkReconstructible();
        }
    }

    /* loaded from: rt.jar:com/sun/jmx/mbeanserver/DefaultMXBeanMappingFactory$CollectionMapping.class */
    private static final class CollectionMapping extends NonNullMXBeanMapping {
        private final Class<? extends Collection<?>> collectionClass;
        private final MXBeanMapping elementMapping;
        static final /* synthetic */ boolean $assertionsDisabled;

        static {
            $assertionsDisabled = !DefaultMXBeanMappingFactory.class.desiredAssertionStatus();
        }

        CollectionMapping(Type type, ArrayType<?> arrayType, Class<?> cls, MXBeanMapping mXBeanMapping) {
            Object obj;
            super(type, arrayType);
            this.elementMapping = mXBeanMapping;
            Class cls2 = (Class) ((ParameterizedType) type).getRawType();
            if (cls2 == List.class) {
                obj = ArrayList.class;
            } else if (cls2 == Set.class) {
                obj = HashSet.class;
            } else if (cls2 == SortedSet.class) {
                obj = TreeSet.class;
            } else {
                if (!$assertionsDisabled) {
                    throw new AssertionError();
                }
                obj = null;
            }
            this.collectionClass = (Class) Util.cast(obj);
        }

        @Override // com.sun.jmx.mbeanserver.DefaultMXBeanMappingFactory.NonNullMXBeanMapping
        final Object toNonNullOpenValue(Object obj) throws OpenDataException {
            Comparator comparator;
            Collection collection = (Collection) obj;
            if ((collection instanceof SortedSet) && (comparator = ((SortedSet) collection).comparator()) != null) {
                String str = "Cannot convert SortedSet with non-null comparator: " + ((Object) comparator);
                throw DefaultMXBeanMappingFactory.openDataException(str, new IllegalArgumentException(str));
            }
            Object[] objArr = (Object[]) Array.newInstance(getOpenClass().getComponentType(), collection.size());
            int i2 = 0;
            Iterator it = collection.iterator();
            while (it.hasNext()) {
                int i3 = i2;
                i2++;
                objArr[i3] = this.elementMapping.toOpenValue(it.next());
            }
            return objArr;
        }

        @Override // com.sun.jmx.mbeanserver.DefaultMXBeanMappingFactory.NonNullMXBeanMapping
        final Object fromNonNullOpenValue(Object obj) throws InvalidObjectException {
            Object[] objArr = (Object[]) obj;
            try {
                Collection collection = (Collection) Util.cast(this.collectionClass.newInstance());
                for (Object obj2 : objArr) {
                    if (!collection.add(this.elementMapping.fromOpenValue(obj2))) {
                        throw new InvalidObjectException("Could not add " + obj2 + " to " + this.collectionClass.getName() + " (duplicate set element?)");
                    }
                }
                return collection;
            } catch (Exception e2) {
                throw DefaultMXBeanMappingFactory.invalidObjectException("Cannot create collection", e2);
            }
        }

        @Override // com.sun.jmx.mbeanserver.MXBeanMapping
        public void checkReconstructible() throws InvalidObjectException {
            this.elementMapping.checkReconstructible();
        }
    }

    /* loaded from: rt.jar:com/sun/jmx/mbeanserver/DefaultMXBeanMappingFactory$MXBeanRefMapping.class */
    private static final class MXBeanRefMapping extends NonNullMXBeanMapping {
        MXBeanRefMapping(Type type) {
            super(type, SimpleType.OBJECTNAME);
        }

        @Override // com.sun.jmx.mbeanserver.DefaultMXBeanMappingFactory.NonNullMXBeanMapping
        final Object toNonNullOpenValue(Object obj) throws OpenDataException, IllegalArgumentException {
            ObjectName objectNameMxbeanToObjectName = lookupNotNull(OpenDataException.class).mxbeanToObjectName(obj);
            if (objectNameMxbeanToObjectName == null) {
                throw new OpenDataException("No name for object: " + obj);
            }
            return objectNameMxbeanToObjectName;
        }

        @Override // com.sun.jmx.mbeanserver.DefaultMXBeanMappingFactory.NonNullMXBeanMapping
        final Object fromNonNullOpenValue(Object obj) throws InvalidObjectException {
            ObjectName objectName = (ObjectName) obj;
            Object objObjectNameToMXBean = lookupNotNull(InvalidObjectException.class).objectNameToMXBean(objectName, (Class) getJavaType());
            if (objObjectNameToMXBean == null) {
                throw new InvalidObjectException("No MXBean for name: " + ((Object) objectName));
            }
            return objObjectNameToMXBean;
        }

        /* JADX INFO: Thrown type has an unknown type hierarchy: T extends java.lang.Exception */
        private <T extends Exception> MXBeanLookup lookupNotNull(Class<T> cls) throws Exception {
            MXBeanLookup lookup = MXBeanLookup.getLookup();
            if (lookup == null) {
                try {
                    throw cls.getConstructor(String.class).newInstance("Cannot convert MXBean interface in this context");
                } catch (Exception e2) {
                    throw new RuntimeException(e2);
                }
            }
            return lookup;
        }
    }

    /* loaded from: rt.jar:com/sun/jmx/mbeanserver/DefaultMXBeanMappingFactory$TabularMapping.class */
    private static final class TabularMapping extends NonNullMXBeanMapping {
        private final boolean sortedMap;
        private final MXBeanMapping keyMapping;
        private final MXBeanMapping valueMapping;

        TabularMapping(Type type, boolean z2, TabularType tabularType, MXBeanMapping mXBeanMapping, MXBeanMapping mXBeanMapping2) {
            super(type, tabularType);
            this.sortedMap = z2;
            this.keyMapping = mXBeanMapping;
            this.valueMapping = mXBeanMapping2;
        }

        @Override // com.sun.jmx.mbeanserver.DefaultMXBeanMappingFactory.NonNullMXBeanMapping
        final Object toNonNullOpenValue(Object obj) throws OpenDataException {
            Comparator comparator;
            Map map = (Map) Util.cast(obj);
            if ((map instanceof SortedMap) && (comparator = ((SortedMap) map).comparator()) != null) {
                String str = "Cannot convert SortedMap with non-null comparator: " + ((Object) comparator);
                throw DefaultMXBeanMappingFactory.openDataException(str, new IllegalArgumentException(str));
            }
            TabularType tabularType = (TabularType) getOpenType();
            TabularDataSupport tabularDataSupport = new TabularDataSupport(tabularType);
            CompositeType rowType = tabularType.getRowType();
            for (Map.Entry entry : map.entrySet()) {
                tabularDataSupport.put(new CompositeDataSupport(rowType, DefaultMXBeanMappingFactory.keyValueArray, new Object[]{this.keyMapping.toOpenValue(entry.getKey()), this.valueMapping.toOpenValue(entry.getValue())}));
            }
            return tabularDataSupport;
        }

        @Override // com.sun.jmx.mbeanserver.DefaultMXBeanMappingFactory.NonNullMXBeanMapping
        final Object fromNonNullOpenValue(Object obj) throws InvalidObjectException {
            Collection<CompositeData> collection = (Collection) Util.cast(((TabularData) obj).values());
            Map mapNewSortedMap = this.sortedMap ? Util.newSortedMap() : Util.newInsertionOrderMap();
            for (CompositeData compositeData : collection) {
                Object objFromOpenValue = this.keyMapping.fromOpenValue(compositeData.get("key"));
                if (mapNewSortedMap.put(objFromOpenValue, this.valueMapping.fromOpenValue(compositeData.get("value"))) != null) {
                    throw new InvalidObjectException("Duplicate entry in TabularData: key=" + objFromOpenValue);
                }
            }
            return mapNewSortedMap;
        }

        @Override // com.sun.jmx.mbeanserver.MXBeanMapping
        public void checkReconstructible() throws InvalidObjectException {
            this.keyMapping.checkReconstructible();
            this.valueMapping.checkReconstructible();
        }
    }

    /* loaded from: rt.jar:com/sun/jmx/mbeanserver/DefaultMXBeanMappingFactory$CompositeMapping.class */
    private final class CompositeMapping extends NonNullMXBeanMapping {
        private final String[] itemNames;
        private final Method[] getters;
        private final MXBeanMapping[] getterMappings;
        private CompositeBuilder compositeBuilder;
        static final /* synthetic */ boolean $assertionsDisabled;

        static {
            $assertionsDisabled = !DefaultMXBeanMappingFactory.class.desiredAssertionStatus();
        }

        CompositeMapping(Class<?> cls, CompositeType compositeType, String[] strArr, Method[] methodArr, MXBeanMappingFactory mXBeanMappingFactory) throws OpenDataException {
            super(cls, compositeType);
            if (!$assertionsDisabled && strArr.length != methodArr.length) {
                throw new AssertionError();
            }
            this.itemNames = strArr;
            this.getters = methodArr;
            this.getterMappings = new MXBeanMapping[methodArr.length];
            for (int i2 = 0; i2 < methodArr.length; i2++) {
                this.getterMappings[i2] = mXBeanMappingFactory.mappingForType(methodArr[i2].getGenericReturnType(), mXBeanMappingFactory);
            }
        }

        @Override // com.sun.jmx.mbeanserver.DefaultMXBeanMappingFactory.NonNullMXBeanMapping
        final Object toNonNullOpenValue(Object obj) throws OpenDataException {
            CompositeType compositeType = (CompositeType) getOpenType();
            if (obj instanceof CompositeDataView) {
                return ((CompositeDataView) obj).toCompositeData(compositeType);
            }
            if (obj == null) {
                return null;
            }
            Object[] objArr = new Object[this.getters.length];
            for (int i2 = 0; i2 < this.getters.length; i2++) {
                try {
                    objArr[i2] = this.getterMappings[i2].toOpenValue(MethodUtil.invoke(this.getters[i2], obj, (Object[]) null));
                } catch (Exception e2) {
                    throw DefaultMXBeanMappingFactory.openDataException("Error calling getter for " + this.itemNames[i2] + ": " + ((Object) e2), e2);
                }
            }
            return new CompositeDataSupport(compositeType, this.itemNames, objArr);
        }

        /* JADX WARN: Multi-variable type inference failed */
        private synchronized void makeCompositeBuilder() throws InvalidObjectException {
            if (this.compositeBuilder != null) {
                return;
            }
            Class cls = (Class) getJavaType();
            CompositeBuilder[] compositeBuilderArr = {new CompositeBuilder[]{new CompositeBuilderViaFrom(cls, this.itemNames)}, new CompositeBuilder[]{new CompositeBuilderViaConstructor(cls, this.itemNames)}, new CompositeBuilder[]{new CompositeBuilderCheckGetters(cls, this.itemNames, this.getterMappings), new CompositeBuilderViaSetters(cls, this.itemNames), new CompositeBuilderViaProxy(cls, this.itemNames)}};
            CompositeBuilder compositeBuilder = null;
            StringBuilder sb = new StringBuilder();
            Throwable th = null;
            int length = compositeBuilderArr.length;
            int i2 = 0;
            loop0: while (true) {
                if (i2 >= length) {
                    break;
                }
                Object[] objArr = compositeBuilderArr[i2];
                for (int i3 = 0; i3 < objArr.length; i3++) {
                    CompositeBuilderCheckGetters compositeBuilderCheckGetters = objArr[i3];
                    String strApplicable = compositeBuilderCheckGetters.applicable(this.getters);
                    if (strApplicable == null) {
                        compositeBuilder = compositeBuilderCheckGetters;
                        break loop0;
                    }
                    Throwable thPossibleCause = compositeBuilderCheckGetters.possibleCause();
                    if (thPossibleCause != null) {
                        th = thPossibleCause;
                    }
                    if (strApplicable.length() > 0) {
                        if (sb.length() > 0) {
                            sb.append(VectorFormat.DEFAULT_SEPARATOR);
                        }
                        sb.append(strApplicable);
                        if (i3 == 0) {
                            break;
                        }
                    }
                }
                i2++;
            }
            if (compositeBuilder == null) {
                String str = "Do not know how to make a " + cls.getName() + " from a CompositeData: " + ((Object) sb);
                if (th != null) {
                    str = str + ". Remaining exceptions show a POSSIBLE cause.";
                }
                throw DefaultMXBeanMappingFactory.invalidObjectException(str, th);
            }
            this.compositeBuilder = compositeBuilder;
        }

        @Override // com.sun.jmx.mbeanserver.MXBeanMapping
        public void checkReconstructible() throws InvalidObjectException {
            makeCompositeBuilder();
        }

        @Override // com.sun.jmx.mbeanserver.DefaultMXBeanMappingFactory.NonNullMXBeanMapping
        final Object fromNonNullOpenValue(Object obj) throws InvalidObjectException {
            makeCompositeBuilder();
            return this.compositeBuilder.fromCompositeData((CompositeData) obj, this.itemNames, this.getterMappings);
        }
    }

    /* loaded from: rt.jar:com/sun/jmx/mbeanserver/DefaultMXBeanMappingFactory$CompositeBuilder.class */
    private static abstract class CompositeBuilder {
        private final Class<?> targetClass;
        private final String[] itemNames;

        abstract String applicable(Method[] methodArr) throws InvalidObjectException;

        abstract Object fromCompositeData(CompositeData compositeData, String[] strArr, MXBeanMapping[] mXBeanMappingArr) throws InvalidObjectException;

        CompositeBuilder(Class<?> cls, String[] strArr) {
            this.targetClass = cls;
            this.itemNames = strArr;
        }

        Class<?> getTargetClass() {
            return this.targetClass;
        }

        String[] getItemNames() {
            return this.itemNames;
        }

        Throwable possibleCause() {
            return null;
        }
    }

    /* loaded from: rt.jar:com/sun/jmx/mbeanserver/DefaultMXBeanMappingFactory$CompositeBuilderViaFrom.class */
    private static final class CompositeBuilderViaFrom extends CompositeBuilder {
        private Method fromMethod;

        CompositeBuilderViaFrom(Class<?> cls, String[] strArr) {
            super(cls, strArr);
        }

        @Override // com.sun.jmx.mbeanserver.DefaultMXBeanMappingFactory.CompositeBuilder
        String applicable(Method[] methodArr) throws InvalidObjectException {
            Class<?> targetClass = getTargetClass();
            try {
                Method method = targetClass.getMethod(Constants.ATTRNAME_FROM, CompositeData.class);
                if (!Modifier.isStatic(method.getModifiers())) {
                    throw new InvalidObjectException("Method from(CompositeData) is not static");
                }
                if (method.getReturnType() != getTargetClass()) {
                    throw new InvalidObjectException("Method from(CompositeData) returns " + MXBeanIntrospector.typeName(method.getReturnType()) + " not " + MXBeanIntrospector.typeName(targetClass));
                }
                this.fromMethod = method;
                return null;
            } catch (InvalidObjectException e2) {
                throw e2;
            } catch (Exception e3) {
                return "no method from(CompositeData)";
            }
        }

        @Override // com.sun.jmx.mbeanserver.DefaultMXBeanMappingFactory.CompositeBuilder
        final Object fromCompositeData(CompositeData compositeData, String[] strArr, MXBeanMapping[] mXBeanMappingArr) throws InvalidObjectException {
            try {
                return MethodUtil.invoke(this.fromMethod, null, new Object[]{compositeData});
            } catch (Exception e2) {
                throw DefaultMXBeanMappingFactory.invalidObjectException("Failed to invoke from(CompositeData)", e2);
            }
        }
    }

    /* loaded from: rt.jar:com/sun/jmx/mbeanserver/DefaultMXBeanMappingFactory$CompositeBuilderCheckGetters.class */
    private static class CompositeBuilderCheckGetters extends CompositeBuilder {
        private final MXBeanMapping[] getterConverters;
        private Throwable possibleCause;

        CompositeBuilderCheckGetters(Class<?> cls, String[] strArr, MXBeanMapping[] mXBeanMappingArr) {
            super(cls, strArr);
            this.getterConverters = mXBeanMappingArr;
        }

        @Override // com.sun.jmx.mbeanserver.DefaultMXBeanMappingFactory.CompositeBuilder
        String applicable(Method[] methodArr) {
            for (int i2 = 0; i2 < methodArr.length; i2++) {
                try {
                    this.getterConverters[i2].checkReconstructible();
                } catch (InvalidObjectException e2) {
                    this.possibleCause = e2;
                    return "method " + methodArr[i2].getName() + " returns type that cannot be mapped back from OpenData";
                }
            }
            return "";
        }

        @Override // com.sun.jmx.mbeanserver.DefaultMXBeanMappingFactory.CompositeBuilder
        Throwable possibleCause() {
            return this.possibleCause;
        }

        @Override // com.sun.jmx.mbeanserver.DefaultMXBeanMappingFactory.CompositeBuilder
        final Object fromCompositeData(CompositeData compositeData, String[] strArr, MXBeanMapping[] mXBeanMappingArr) {
            throw new Error();
        }
    }

    /* loaded from: rt.jar:com/sun/jmx/mbeanserver/DefaultMXBeanMappingFactory$CompositeBuilderViaSetters.class */
    private static class CompositeBuilderViaSetters extends CompositeBuilder {
        private Method[] setters;

        CompositeBuilderViaSetters(Class<?> cls, String[] strArr) {
            super(cls, strArr);
        }

        @Override // com.sun.jmx.mbeanserver.DefaultMXBeanMappingFactory.CompositeBuilder
        String applicable(Method[] methodArr) throws Exception {
            try {
                getTargetClass().getConstructor(new Class[0]);
                Method[] methodArr2 = new Method[methodArr.length];
                for (int i2 = 0; i2 < methodArr.length; i2++) {
                    Method method = methodArr[i2];
                    try {
                        Method method2 = getTargetClass().getMethod("set" + DefaultMXBeanMappingFactory.propertyName(method), method.getReturnType());
                        if (method2.getReturnType() != Void.TYPE) {
                            throw new Exception();
                        }
                        methodArr2[i2] = method2;
                    } catch (Exception e2) {
                        return "not all getters have corresponding setters (" + ((Object) method) + ")";
                    }
                }
                this.setters = methodArr2;
                return null;
            } catch (Exception e3) {
                return "does not have a public no-arg constructor";
            }
        }

        @Override // com.sun.jmx.mbeanserver.DefaultMXBeanMappingFactory.CompositeBuilder
        Object fromCompositeData(CompositeData compositeData, String[] strArr, MXBeanMapping[] mXBeanMappingArr) throws InvalidObjectException {
            try {
                Class<?> targetClass = getTargetClass();
                ReflectUtil.checkPackageAccess(targetClass);
                Object objNewInstance = targetClass.newInstance();
                for (int i2 = 0; i2 < strArr.length; i2++) {
                    if (compositeData.containsKey(strArr[i2])) {
                        MethodUtil.invoke(this.setters[i2], objNewInstance, new Object[]{mXBeanMappingArr[i2].fromOpenValue(compositeData.get(strArr[i2]))});
                    }
                }
                return objNewInstance;
            } catch (Exception e2) {
                throw DefaultMXBeanMappingFactory.invalidObjectException(e2);
            }
        }
    }

    /* loaded from: rt.jar:com/sun/jmx/mbeanserver/DefaultMXBeanMappingFactory$CompositeBuilderViaConstructor.class */
    private static final class CompositeBuilderViaConstructor extends CompositeBuilder {
        private List<Constr> annotatedConstructors;

        /* loaded from: rt.jar:com/sun/jmx/mbeanserver/DefaultMXBeanMappingFactory$CompositeBuilderViaConstructor$AnnotationHelper.class */
        static class AnnotationHelper {
            private static Class<? extends Annotation> constructorPropertiesClass;
            private static Method valueMethod;

            AnnotationHelper() {
            }

            static {
                findConstructorPropertiesClass();
            }

            private static void findConstructorPropertiesClass() {
                try {
                    constructorPropertiesClass = Class.forName("java.beans.ConstructorProperties", false, DefaultMXBeanMappingFactory.class.getClassLoader());
                    valueMethod = constructorPropertiesClass.getMethod("value", new Class[0]);
                } catch (ClassNotFoundException e2) {
                } catch (NoSuchMethodException e3) {
                    throw new InternalError(e3);
                }
            }

            static boolean isAvailable() {
                return constructorPropertiesClass != null;
            }

            static String[] getPropertyNames(Constructor<?> constructor) {
                Annotation annotation;
                if (!isAvailable() || (annotation = constructor.getAnnotation(constructorPropertiesClass)) == null) {
                    return null;
                }
                try {
                    return (String[]) valueMethod.invoke(annotation, new Object[0]);
                } catch (IllegalAccessException e2) {
                    throw new InternalError(e2);
                } catch (InvocationTargetException e3) {
                    throw new InternalError(e3);
                }
            }
        }

        CompositeBuilderViaConstructor(Class<?> cls, String[] strArr) {
            super(cls, strArr);
        }

        @Override // com.sun.jmx.mbeanserver.DefaultMXBeanMappingFactory.CompositeBuilder
        String applicable(Method[] methodArr) throws InvalidObjectException, SecurityException {
            if (!AnnotationHelper.isAvailable()) {
                return "@ConstructorProperties annotation not available";
            }
            Constructor<?>[] constructors = getTargetClass().getConstructors();
            List<Constructor> listNewList = Util.newList();
            for (Constructor<?> constructor : constructors) {
                if (Modifier.isPublic(constructor.getModifiers()) && AnnotationHelper.getPropertyNames(constructor) != null) {
                    listNewList.add(constructor);
                }
            }
            if (listNewList.isEmpty()) {
                return "no constructor has @ConstructorProperties annotation";
            }
            this.annotatedConstructors = Util.newList();
            Map mapNewMap = Util.newMap();
            String[] itemNames = getItemNames();
            for (int i2 = 0; i2 < itemNames.length; i2++) {
                mapNewMap.put(itemNames[i2], Integer.valueOf(i2));
            }
            Set<BitSet> setNewSet = Util.newSet();
            for (Constructor constructor2 : listNewList) {
                String[] propertyNames = AnnotationHelper.getPropertyNames(constructor2);
                Type[] genericParameterTypes = constructor2.getGenericParameterTypes();
                if (genericParameterTypes.length != propertyNames.length) {
                    throw new InvalidObjectException("Number of constructor params does not match @ConstructorProperties annotation: " + ((Object) constructor2));
                }
                int[] iArr = new int[methodArr.length];
                for (int i3 = 0; i3 < methodArr.length; i3++) {
                    iArr[i3] = -1;
                }
                BitSet bitSet = new BitSet();
                for (int i4 = 0; i4 < propertyNames.length; i4++) {
                    String str = propertyNames[i4];
                    if (!mapNewMap.containsKey(str)) {
                        String str2 = "@ConstructorProperties includes name " + str + " which does not correspond to a property";
                        for (String str3 : mapNewMap.keySet()) {
                            if (str3.equalsIgnoreCase(str)) {
                                str2 = str2 + " (differs only in case from property " + str3 + ")";
                            }
                        }
                        throw new InvalidObjectException(str2 + ": " + ((Object) constructor2));
                    }
                    int iIntValue = ((Integer) mapNewMap.get(str)).intValue();
                    iArr[iIntValue] = i4;
                    if (bitSet.get(iIntValue)) {
                        throw new InvalidObjectException("@ConstructorProperties contains property " + str + " more than once: " + ((Object) constructor2));
                    }
                    bitSet.set(iIntValue);
                    Type genericReturnType = methodArr[iIntValue].getGenericReturnType();
                    if (!genericReturnType.equals(genericParameterTypes[i4])) {
                        throw new InvalidObjectException("@ConstructorProperties gives property " + str + " of type " + ((Object) genericReturnType) + " for parameter  of type " + ((Object) genericParameterTypes[i4]) + ": " + ((Object) constructor2));
                    }
                }
                if (!setNewSet.add(bitSet)) {
                    throw new InvalidObjectException("More than one constructor has a @ConstructorProperties annotation with this set of names: " + Arrays.toString(propertyNames));
                }
                this.annotatedConstructors.add(new Constr(constructor2, iArr, bitSet));
            }
            for (BitSet bitSet2 : setNewSet) {
                boolean z2 = false;
                for (BitSet bitSet3 : setNewSet) {
                    if (bitSet2 == bitSet3) {
                        z2 = true;
                    } else if (z2) {
                        BitSet bitSet4 = new BitSet();
                        bitSet4.or(bitSet2);
                        bitSet4.or(bitSet3);
                        if (!setNewSet.contains(bitSet4)) {
                            TreeSet treeSet = new TreeSet();
                            int iNextSetBit = bitSet4.nextSetBit(0);
                            while (true) {
                                int i5 = iNextSetBit;
                                if (i5 >= 0) {
                                    treeSet.add(itemNames[i5]);
                                    iNextSetBit = bitSet4.nextSetBit(i5 + 1);
                                } else {
                                    throw new InvalidObjectException("Constructors with @ConstructorProperties annotation  would be ambiguous for these items: " + ((Object) treeSet));
                                }
                            }
                        }
                    } else {
                        continue;
                    }
                }
            }
            return null;
        }

        @Override // com.sun.jmx.mbeanserver.DefaultMXBeanMappingFactory.CompositeBuilder
        final Object fromCompositeData(CompositeData compositeData, String[] strArr, MXBeanMapping[] mXBeanMappingArr) throws InvalidObjectException {
            CompositeType compositeType = compositeData.getCompositeType();
            BitSet bitSet = new BitSet();
            for (int i2 = 0; i2 < strArr.length; i2++) {
                if (compositeType.getType(strArr[i2]) != null) {
                    bitSet.set(i2);
                }
            }
            Constr constr = null;
            for (Constr constr2 : this.annotatedConstructors) {
                if (subset(constr2.presentParams, bitSet) && (constr == null || subset(constr.presentParams, constr2.presentParams))) {
                    constr = constr2;
                }
            }
            if (constr == null) {
                throw new InvalidObjectException("No constructor has a @ConstructorProperties for this set of items: " + ((Object) compositeType.keySet()));
            }
            Object[] objArr = new Object[constr.presentParams.cardinality()];
            for (int i3 = 0; i3 < strArr.length; i3++) {
                if (constr.presentParams.get(i3)) {
                    Object objFromOpenValue = mXBeanMappingArr[i3].fromOpenValue(compositeData.get(strArr[i3]));
                    int i4 = constr.paramIndexes[i3];
                    if (i4 >= 0) {
                        objArr[i4] = objFromOpenValue;
                    }
                }
            }
            try {
                ReflectUtil.checkPackageAccess(constr.constructor.getDeclaringClass());
                return constr.constructor.newInstance(objArr);
            } catch (Exception e2) {
                throw DefaultMXBeanMappingFactory.invalidObjectException("Exception constructing " + getTargetClass().getName(), e2);
            }
        }

        private static boolean subset(BitSet bitSet, BitSet bitSet2) {
            BitSet bitSet3 = (BitSet) bitSet.clone();
            bitSet3.andNot(bitSet2);
            return bitSet3.isEmpty();
        }

        /* loaded from: rt.jar:com/sun/jmx/mbeanserver/DefaultMXBeanMappingFactory$CompositeBuilderViaConstructor$Constr.class */
        private static class Constr {
            final Constructor<?> constructor;
            final int[] paramIndexes;
            final BitSet presentParams;

            Constr(Constructor<?> constructor, int[] iArr, BitSet bitSet) {
                this.constructor = constructor;
                this.paramIndexes = iArr;
                this.presentParams = bitSet;
            }
        }
    }

    /* loaded from: rt.jar:com/sun/jmx/mbeanserver/DefaultMXBeanMappingFactory$CompositeBuilderViaProxy.class */
    private static final class CompositeBuilderViaProxy extends CompositeBuilder {
        CompositeBuilderViaProxy(Class<?> cls, String[] strArr) {
            super(cls, strArr);
        }

        @Override // com.sun.jmx.mbeanserver.DefaultMXBeanMappingFactory.CompositeBuilder
        String applicable(Method[] methodArr) {
            Class<?> targetClass = getTargetClass();
            if (!targetClass.isInterface()) {
                return "not an interface";
            }
            Set<Method> setNewSet = Util.newSet(Arrays.asList(targetClass.getMethods()));
            setNewSet.removeAll(Arrays.asList(methodArr));
            String str = null;
            for (Method method : setNewSet) {
                String name = method.getName();
                try {
                    if (!Modifier.isPublic(Object.class.getMethod(name, method.getParameterTypes()).getModifiers())) {
                        str = name;
                    }
                } catch (NoSuchMethodException e2) {
                    str = name;
                }
            }
            if (str != null) {
                return "contains methods other than getters (" + str + ")";
            }
            return null;
        }

        @Override // com.sun.jmx.mbeanserver.DefaultMXBeanMappingFactory.CompositeBuilder
        final Object fromCompositeData(CompositeData compositeData, String[] strArr, MXBeanMapping[] mXBeanMappingArr) {
            Class<?> targetClass = getTargetClass();
            return Proxy.newProxyInstance(targetClass.getClassLoader(), new Class[]{targetClass}, new CompositeDataInvocationHandler(compositeData));
        }
    }

    static InvalidObjectException invalidObjectException(String str, Throwable th) {
        return (InvalidObjectException) EnvHelp.initCause(new InvalidObjectException(str), th);
    }

    static InvalidObjectException invalidObjectException(Throwable th) {
        return invalidObjectException(th.getMessage(), th);
    }

    static OpenDataException openDataException(String str, Throwable th) {
        return (OpenDataException) EnvHelp.initCause(new OpenDataException(str), th);
    }

    static OpenDataException openDataException(Throwable th) {
        return openDataException(th.getMessage(), th);
    }

    static void mustBeComparable(Class<?> cls, Type type) throws OpenDataException {
        if (!(type instanceof Class) || !Comparable.class.isAssignableFrom((Class) type)) {
            throw new OpenDataException("Parameter class " + ((Object) type) + " of " + cls.getName() + " does not implement " + Comparable.class.getName());
        }
    }

    public static String decapitalize(String str) {
        if (str == null || str.length() == 0) {
            return str;
        }
        int iOffsetByCodePoints = Character.offsetByCodePoints(str, 0, 1);
        if (iOffsetByCodePoints < str.length() && Character.isUpperCase(str.codePointAt(iOffsetByCodePoints))) {
            return str;
        }
        return str.substring(0, iOffsetByCodePoints).toLowerCase() + str.substring(iOffsetByCodePoints);
    }

    static String capitalize(String str) {
        if (str == null || str.length() == 0) {
            return str;
        }
        int iOffsetByCodePoints = str.offsetByCodePoints(0, 1);
        return str.substring(0, iOffsetByCodePoints).toUpperCase() + str.substring(iOffsetByCodePoints);
    }

    public static String propertyName(Method method) {
        String strSubstring = null;
        String name = method.getName();
        if (name.startsWith("get")) {
            strSubstring = name.substring(3);
        } else if (name.startsWith(BeanAdapter.IS_PREFIX) && method.getReturnType() == Boolean.TYPE) {
            strSubstring = name.substring(2);
        }
        if (strSubstring == null || strSubstring.length() == 0 || method.getParameterTypes().length > 0 || method.getReturnType() == Void.TYPE || name.equals("getClass")) {
            return null;
        }
        return strSubstring;
    }
}
