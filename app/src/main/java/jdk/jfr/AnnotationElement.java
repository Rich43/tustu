package jdk.jfr;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.StringJoiner;
import jdk.jfr.internal.Type;
import jdk.jfr.internal.TypeLibrary;
import jdk.jfr.internal.Utils;

/* loaded from: jfr.jar:jdk/jfr/AnnotationElement.class */
public final class AnnotationElement {
    private final Type type;
    private final List<Object> annotationValues;
    private final List<String> annotationNames;
    private final boolean inBootClassLoader;

    AnnotationElement(Type type, List<Object> list, boolean z2) {
        Objects.requireNonNull(type);
        Objects.requireNonNull(list);
        this.type = type;
        if (list.size() != type.getFields().size()) {
            StringJoiner stringJoiner = new StringJoiner(",", "[", "]");
            Iterator<ValueDescriptor> it = type.getFields().iterator();
            while (it.hasNext()) {
                stringJoiner.add(it.next().getName());
            }
            StringJoiner stringJoiner2 = new StringJoiner(",", "[", "]");
            Iterator<Object> it2 = list.iterator();
            while (it2.hasNext()) {
                stringJoiner.add(String.valueOf(it2.next()));
            }
            throw new IllegalArgumentException("Annotation " + ((Object) stringJoiner) + " for " + type.getName() + " doesn't match number of values " + ((Object) stringJoiner2));
        }
        ArrayList arrayList = new ArrayList();
        ArrayList arrayList2 = new ArrayList();
        int i2 = 0;
        for (ValueDescriptor valueDescriptor : type.getFields()) {
            Object obj = list.get(i2);
            if (obj == null) {
                throw new IllegalArgumentException("Annotation value can't be null");
            }
            Class<?> componentType = obj.getClass();
            if (valueDescriptor.isArray()) {
                componentType = componentType.getComponentType();
            }
            checkType(Utils.unboxType(componentType));
            arrayList.add(valueDescriptor.getName());
            arrayList2.add(obj);
            i2++;
        }
        this.annotationValues = Utils.smallUnmodifiable(arrayList2);
        this.annotationNames = Utils.smallUnmodifiable(arrayList);
        this.inBootClassLoader = z2;
    }

    public AnnotationElement(Class<? extends Annotation> cls, Map<String, Object> map) throws SecurityException {
        Objects.requireNonNull(cls);
        Objects.requireNonNull(map);
        Utils.checkRegisterPermission();
        HashMap map2 = new HashMap(map);
        for (Map.Entry entry : map2.entrySet()) {
            if (entry.getKey() == null) {
                throw new NullPointerException("Name of annotation method can't be null");
            }
            if (entry.getValue() == null) {
                throw new NullPointerException("Return value for annotation method can't be null");
            }
        }
        if (AnnotationElement.class.isAssignableFrom(cls) && cls.isInterface()) {
            throw new IllegalArgumentException("Must be interface extending " + Annotation.class.getName());
        }
        if (!isKnownJFRAnnotation(cls) && cls.getAnnotation(MetadataDefinition.class) == null) {
            throw new IllegalArgumentException("Annotation class must be annotated with jdk.jfr.MetadataDefinition to be valid");
        }
        if (isKnownJFRAnnotation(cls)) {
            this.type = new Type(cls.getCanonicalName(), Type.SUPER_TYPE_ANNOTATION, Type.getTypeId(cls));
        } else {
            this.type = TypeLibrary.createAnnotationType(cls);
        }
        Method[] declaredMethods = cls.getDeclaredMethods();
        if (declaredMethods.length != map2.size()) {
            throw new IllegalArgumentException("Number of declared methods must match size of value map");
        }
        ArrayList arrayList = new ArrayList();
        ArrayList arrayList2 = new ArrayList();
        HashSet hashSet = new HashSet();
        for (Method method : declaredMethods) {
            String name = method.getName();
            Object obj = map2.get(name);
            if (obj == null) {
                throw new IllegalArgumentException("No method in annotation interface " + cls.getName() + " matching name " + name);
            }
            Class<?> clsUnboxType = obj.getClass();
            if (clsUnboxType == Class.class) {
                throw new IllegalArgumentException("Annotation value for " + name + " can't be class");
            }
            if (obj instanceof Enum) {
                throw new IllegalArgumentException("Annotation value for " + name + " can't be enum");
            }
            if (!clsUnboxType.equals(obj.getClass())) {
                throw new IllegalArgumentException("Return type of annotation " + clsUnboxType.getName() + " must match type of object" + ((Object) obj.getClass()));
            }
            if (clsUnboxType.isArray()) {
                Class<?> componentType = clsUnboxType.getComponentType();
                checkType(componentType);
                if (componentType.equals(String.class)) {
                    for (String str : (String[]) obj) {
                        if (str == null) {
                            throw new IllegalArgumentException("Annotation value for " + name + " contains null");
                        }
                    }
                }
            } else {
                clsUnboxType = Utils.unboxType(obj.getClass());
                checkType(clsUnboxType);
            }
            if (hashSet.contains(name)) {
                throw new IllegalArgumentException("Value with name '" + name + "' already exists");
            }
            if (isKnownJFRAnnotation(cls)) {
                this.type.add(new ValueDescriptor(clsUnboxType, name, Collections.emptyList(), true));
            }
            arrayList.add(name);
            arrayList2.add(obj);
        }
        this.annotationValues = Utils.smallUnmodifiable(arrayList2);
        this.annotationNames = Utils.smallUnmodifiable(arrayList);
        this.inBootClassLoader = cls.getClassLoader() == null;
    }

    public AnnotationElement(Class<? extends Annotation> cls, Object obj) {
        this(cls, (Map<String, Object>) Collections.singletonMap("value", Objects.requireNonNull(obj)));
    }

    public AnnotationElement(Class<? extends Annotation> cls) {
        this(cls, (Map<String, Object>) Collections.emptyMap());
    }

    public List<Object> getValues() {
        return this.annotationValues;
    }

    public List<ValueDescriptor> getValueDescriptors() {
        return Collections.unmodifiableList(this.type.getFields());
    }

    public List<AnnotationElement> getAnnotationElements() {
        return this.type.getAnnotationElements();
    }

    public String getTypeName() {
        return this.type.getName();
    }

    public Object getValue(String str) {
        Objects.requireNonNull(str);
        int i2 = 0;
        Iterator<String> it = this.annotationNames.iterator();
        while (it.hasNext()) {
            if (str.equals(it.next())) {
                return this.annotationValues.get(i2);
            }
            i2++;
        }
        StringJoiner stringJoiner = new StringJoiner(",", "[", "]");
        Iterator<ValueDescriptor> it2 = this.type.getFields().iterator();
        while (it2.hasNext()) {
            stringJoiner.add(it2.next().getName());
        }
        throw new IllegalArgumentException("No value with name '" + str + "'. Valid names are " + ((Object) stringJoiner));
    }

    public boolean hasValue(String str) {
        Objects.requireNonNull(str);
        Iterator<String> it = this.annotationNames.iterator();
        while (it.hasNext()) {
            if (str.equals(it.next())) {
                return true;
            }
        }
        return false;
    }

    public final <A> A getAnnotation(Class<? extends Annotation> cls) {
        Objects.requireNonNull(cls);
        return (A) this.type.getAnnotation(cls);
    }

    public long getTypeId() {
        return this.type.getId();
    }

    Type getType() {
        return this.type;
    }

    private static void checkType(Class<?> cls) {
        if (cls.isPrimitive() || cls == String.class) {
        } else {
            throw new IllegalArgumentException("Only primitives types or java.lang.String are allowed");
        }
    }

    private static boolean isKnownJFRAnnotation(Class<? extends Annotation> cls) {
        if (cls == Registered.class || cls == Threshold.class || cls == StackTrace.class || cls == Period.class || cls == Enabled.class) {
            return true;
        }
        return false;
    }

    boolean isInBoot() {
        return this.inBootClassLoader;
    }
}
