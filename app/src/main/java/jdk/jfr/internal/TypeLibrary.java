package jdk.jfr.internal;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.annotation.Repeatable;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.function.Consumer;
import java.util.function.Predicate;
import jdk.jfr.AnnotationElement;
import jdk.jfr.Description;
import jdk.jfr.Event;
import jdk.jfr.Label;
import jdk.jfr.MetadataDefinition;
import jdk.jfr.Name;
import jdk.jfr.SettingDescriptor;
import jdk.jfr.Timespan;
import jdk.jfr.Timestamp;
import jdk.jfr.ValueDescriptor;

/* loaded from: jfr.jar:jdk/jfr/internal/TypeLibrary.class */
public final class TypeLibrary {
    private static TypeLibrary instance;
    private static final Map<Long, Type> types = new LinkedHashMap(100);
    static final ValueDescriptor DURATION_FIELD = createDurationField();
    static final ValueDescriptor THREAD_FIELD = createThreadField();
    static final ValueDescriptor STACK_TRACE_FIELD = createStackTraceField();
    static final ValueDescriptor START_TIME_FIELD = createStartTimeField();

    private TypeLibrary(List<Type> list) {
        visitReachable(list, type -> {
            return !types.containsKey(Long.valueOf(type.getId()));
        }, type2 -> {
            types.put(Long.valueOf(type2.getId()), type2);
        });
        if (Logger.shouldLog(LogTag.JFR_SYSTEM_METADATA, LogLevel.INFO)) {
            types.values().stream().sorted((type3, type4) -> {
                return Long.compare(type3.getId(), type4.getId());
            }).forEach(type5 -> {
                type5.log("Added", LogTag.JFR_SYSTEM_METADATA, LogLevel.INFO);
            });
        }
    }

    private static ValueDescriptor createStartTimeField() {
        List<AnnotationElement> listCreateStandardAnnotations = createStandardAnnotations("Start Time", null);
        listCreateStandardAnnotations.add(new AnnotationElement((Class<? extends Annotation>) Timestamp.class, "TICKS"));
        return PrivateAccess.getInstance().newValueDescriptor("startTime", Type.LONG, listCreateStandardAnnotations, 0, false, "startTime");
    }

    private static ValueDescriptor createStackTraceField() {
        new ArrayList();
        return PrivateAccess.getInstance().newValueDescriptor("stackTrace", Type.STACK_TRACE, createStandardAnnotations("Stack Trace", "Stack Trace starting from the method the event was committed in"), 0, true, "stackTrace");
    }

    private static ValueDescriptor createThreadField() {
        new ArrayList();
        return PrivateAccess.getInstance().newValueDescriptor(EventInstrumentation.FIELD_EVENT_THREAD, Type.THREAD, createStandardAnnotations("Event Thread", "Thread in which event was committed in"), 0, true, EventInstrumentation.FIELD_EVENT_THREAD);
    }

    private static ValueDescriptor createDurationField() {
        new ArrayList();
        List<AnnotationElement> listCreateStandardAnnotations = createStandardAnnotations("Duration", null);
        listCreateStandardAnnotations.add(new AnnotationElement((Class<? extends Annotation>) Timespan.class, "TICKS"));
        return PrivateAccess.getInstance().newValueDescriptor("duration", Type.LONG, listCreateStandardAnnotations, 0, false, "duration");
    }

    public static TypeLibrary getInstance() {
        TypeLibrary typeLibrary;
        synchronized (TypeLibrary.class) {
            if (instance == null) {
                try {
                    List<Type> listCreateTypes = MetadataHandler.createTypes();
                    Collections.sort(listCreateTypes, (type, type2) -> {
                        return Long.compare(type.getId(), type2.getId());
                    });
                    instance = new TypeLibrary(listCreateTypes);
                } catch (IOException e2) {
                    throw new Error("JFR: Could not read metadata");
                }
            }
            typeLibrary = instance;
        }
        return typeLibrary;
    }

    public List<Type> getTypes() {
        return new ArrayList(types.values());
    }

    public static Type createAnnotationType(Class<? extends Annotation> cls) throws SecurityException {
        if (shouldPersist(cls)) {
            Type typeDefineType = defineType(cls, Type.SUPER_TYPE_ANNOTATION, false);
            if (typeDefineType != null) {
                SecuritySupport.makeVisibleToJFR(cls);
                for (Method method : cls.getDeclaredMethods()) {
                    typeDefineType.add(PrivateAccess.getInstance().newValueDescriptor(method.getReturnType(), method.getName()));
                }
                ArrayList arrayList = new ArrayList();
                Iterator<Annotation> it = resolveRepeatedAnnotations(cls.getAnnotations()).iterator();
                while (it.hasNext()) {
                    AnnotationElement annotationElementCreateAnnotation = createAnnotation(it.next());
                    if (annotationElementCreateAnnotation != null) {
                        arrayList.add(annotationElementCreateAnnotation);
                    }
                }
                arrayList.trimToSize();
                typeDefineType.setAnnotations(arrayList);
            }
            return getType(cls);
        }
        return null;
    }

    static AnnotationElement createAnnotation(Annotation annotation) throws SecurityException {
        Type typeCreateAnnotationType = createAnnotationType(annotation.annotationType());
        if (typeCreateAnnotationType != null) {
            ArrayList arrayList = new ArrayList();
            Iterator<ValueDescriptor> it = typeCreateAnnotationType.getFields().iterator();
            while (it.hasNext()) {
                arrayList.add(invokeAnnotation(annotation, it.next().getName()));
            }
            return PrivateAccess.getInstance().newAnnotation(typeCreateAnnotationType, arrayList, annotation.annotationType().getClassLoader() == null);
        }
        return null;
    }

    private static Object invokeAnnotation(Annotation annotation, String str) throws SecurityException {
        try {
            Method method = annotation.getClass().getMethod(str, new Class[0]);
            SecuritySupport.setAccessible(method);
            try {
                return method.invoke(annotation, new Object[0]);
            } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e2) {
                throw new InternalError("Could not get value for method " + str + " in annotation " + annotation.getClass().getName());
            }
        } catch (NoSuchMethodException e3) {
            throw new InternalError("Could not loacate method " + str + " in annotation " + annotation.getClass().getName());
        }
    }

    private static boolean shouldPersist(Class<? extends Annotation> cls) {
        if (cls == MetadataDefinition.class || cls.getAnnotation(MetadataDefinition.class) == null) {
            return false;
        }
        return true;
    }

    private static boolean isDefined(Class<?> cls) {
        return types.containsKey(Long.valueOf(Type.getTypeId(cls)));
    }

    private static Type getType(Class<?> cls) {
        return types.get(Long.valueOf(Type.getTypeId(cls)));
    }

    private static Type defineType(Class<?> cls, String str, boolean z2) {
        Type type;
        if (!isDefined(cls)) {
            Name name = (Name) cls.getAnnotation(Name.class);
            String strValue = name != null ? name.value() : cls.getName();
            long typeId = Type.getTypeId(cls);
            if (z2) {
                type = new PlatformEventType(strValue, typeId, cls.getClassLoader() == null, true);
            } else {
                type = new Type(strValue, str, typeId);
            }
            types.put(Long.valueOf(type.getId()), type);
            return type;
        }
        return null;
    }

    public static Type createType(Class<?> cls) {
        return createType(cls, Collections.emptyList(), Collections.emptyList());
    }

    public static Type createType(Class<?> cls, List<AnnotationElement> list, List<ValueDescriptor> list2) throws SecurityException {
        if (Thread.class == cls) {
            return Type.THREAD;
        }
        if (Class.class.isAssignableFrom(cls)) {
            return Type.CLASS;
        }
        if (String.class.equals(cls)) {
            return Type.STRING;
        }
        if (isDefined(cls)) {
            return getType(cls);
        }
        if (cls.isPrimitive()) {
            return defineType(cls, null, false);
        }
        if (cls.isArray()) {
            throw new InternalError("Arrays not supported");
        }
        String str = null;
        boolean z2 = false;
        if (Event.class.isAssignableFrom(cls)) {
            str = Type.SUPER_TYPE_EVENT;
            z2 = true;
        }
        if (Control.class.isAssignableFrom(cls)) {
            str = Type.SUPER_TYPE_SETTING;
        }
        defineType(cls, str, z2);
        Type type = getType(cls);
        if (z2) {
            addImplicitFields(type, true, true, true, true, false);
            addUserFields(cls, type, list2);
            type.trimFields();
        }
        addAnnotations(cls, type, list);
        if (cls.getClassLoader() == null) {
            type.log("Added", LogTag.JFR_SYSTEM_METADATA, LogLevel.INFO);
        } else {
            type.log("Added", LogTag.JFR_METADATA, LogLevel.INFO);
        }
        return type;
    }

    private static void addAnnotations(Class<?> cls, Type type, List<AnnotationElement> list) throws SecurityException {
        ArrayList arrayList = new ArrayList();
        if (list.isEmpty()) {
            Iterator<Annotation> it = Utils.getAnnotations(cls).iterator();
            while (it.hasNext()) {
                AnnotationElement annotationElementCreateAnnotation = createAnnotation(it.next());
                if (annotationElementCreateAnnotation != null) {
                    arrayList.add(annotationElementCreateAnnotation);
                }
            }
        } else {
            ArrayList arrayList2 = new ArrayList();
            arrayList.addAll(list);
            Iterator<AnnotationElement> it2 = list.iterator();
            while (it2.hasNext()) {
                arrayList2.add(PrivateAccess.getInstance().getType(it2.next()));
            }
            addTypes(arrayList2);
        }
        type.setAnnotations(arrayList);
        arrayList.trimToSize();
    }

    /* JADX WARN: Multi-variable type inference failed */
    private static void addUserFields(Class<?> cls, Type type, List<ValueDescriptor> list) throws SecurityException {
        HashMap map = new HashMap();
        for (ValueDescriptor valueDescriptor : list) {
            map.put(valueDescriptor.getName(), valueDescriptor);
        }
        ArrayList arrayList = new ArrayList();
        for (Field field : Utils.getVisibleEventFields(cls)) {
            ValueDescriptor valueDescriptorCreateField = (ValueDescriptor) map.get(field.getName());
            if (valueDescriptorCreateField != null) {
                if (!valueDescriptorCreateField.getTypeName().equals(field.getType().getName())) {
                    throw new InternalError("Type expected to match for field " + valueDescriptorCreateField.getName() + " expected " + field.getName() + " but got " + valueDescriptorCreateField.getName());
                }
                Iterator<AnnotationElement> it = valueDescriptorCreateField.getAnnotationElements().iterator();
                while (it.hasNext()) {
                    arrayList.add(PrivateAccess.getInstance().getType(it.next()));
                }
                arrayList.add(PrivateAccess.getInstance().getType(valueDescriptorCreateField));
            } else {
                valueDescriptorCreateField = createField(field);
            }
            if (valueDescriptorCreateField != null) {
                type.add(valueDescriptorCreateField);
            }
        }
        addTypes(arrayList);
    }

    static void addImplicitFields(Type type, boolean z2, boolean z3, boolean z4, boolean z5, boolean z6) throws SecurityException {
        createAnnotationType(Timespan.class);
        createAnnotationType(Timestamp.class);
        createAnnotationType(Label.class);
        defineType(Long.TYPE, null, false);
        addFields(type, z2, z3, z4, z5, z6);
    }

    private static void addFields(Type type, boolean z2, boolean z3, boolean z4, boolean z5, boolean z6) {
        type.add(START_TIME_FIELD);
        if (z3 || z6) {
            type.add(DURATION_FIELD);
        }
        if (z4) {
            type.add(THREAD_FIELD);
        }
        if (z5) {
            type.add(STACK_TRACE_FIELD);
        }
    }

    private static List<AnnotationElement> createStandardAnnotations(String str, String str2) {
        ArrayList arrayList = new ArrayList(2);
        arrayList.add(new AnnotationElement((Class<? extends Annotation>) Label.class, str));
        if (str2 != null) {
            arrayList.add(new AnnotationElement((Class<? extends Annotation>) Description.class, str2));
        }
        return arrayList;
    }

    private static ValueDescriptor createField(Field field) throws SecurityException {
        int modifiers = field.getModifiers();
        if (Modifier.isTransient(modifiers) || Modifier.isStatic(modifiers)) {
            return null;
        }
        Class<?> type = field.getType();
        if (!Type.isKnownType(type)) {
            return null;
        }
        boolean z2 = Thread.class == type || type == Class.class;
        Type typeCreateType = createType(type);
        String name = field.getName();
        Name name2 = (Name) field.getAnnotation(Name.class);
        String strValue = name;
        if (name2 != null) {
            strValue = name2.value();
        }
        ArrayList arrayList = new ArrayList();
        Iterator<Annotation> it = resolveRepeatedAnnotations(field.getAnnotations()).iterator();
        while (it.hasNext()) {
            AnnotationElement annotationElementCreateAnnotation = createAnnotation(it.next());
            if (annotationElementCreateAnnotation != null) {
                arrayList.add(annotationElementCreateAnnotation);
            }
        }
        return PrivateAccess.getInstance().newValueDescriptor(strValue, typeCreateType, arrayList, 0, z2, name);
    }

    private static List<Annotation> resolveRepeatedAnnotations(Annotation[] annotationArr) {
        Object objInvoke;
        ArrayList arrayList = new ArrayList(annotationArr.length);
        for (Annotation annotation : annotationArr) {
            boolean z2 = false;
            try {
                Method method = annotation.annotationType().getMethod("value", new Class[0]);
                Class<?> returnType = method.getReturnType();
                if (returnType.isArray()) {
                    Class<?> componentType = returnType.getComponentType();
                    if (Annotation.class.isAssignableFrom(componentType) && componentType.getAnnotation(Repeatable.class) != null && (objInvoke = method.invoke(annotation, new Object[0])) != null && Annotation[].class.isAssignableFrom(objInvoke.getClass())) {
                        for (Annotation annotation2 : (Annotation[]) method.invoke(annotation, new Object[0])) {
                            arrayList.add(annotation2);
                        }
                        z2 = true;
                    }
                }
            } catch (IllegalAccessException | IllegalArgumentException | NoSuchMethodException | SecurityException | InvocationTargetException e2) {
            }
            if (!z2) {
                arrayList.add(annotation);
            }
        }
        return arrayList;
    }

    public boolean clearUnregistered() {
        Logger.log(LogTag.JFR_METADATA, LogLevel.TRACE, "Cleaning out obsolete metadata");
        ArrayList arrayList = new ArrayList();
        for (Type type : types.values()) {
            if ((type instanceof PlatformEventType) && ((PlatformEventType) type).isRegistered()) {
                arrayList.add(type);
            }
        }
        visitReachable(arrayList, type2 -> {
            return type2.getRemove();
        }, type3 -> {
            type3.setRemove(false);
        });
        ArrayList arrayList2 = new ArrayList();
        for (Type type4 : types.values()) {
            if (type4.getRemove() && !Type.isDefinedByJVM(type4.getId())) {
                arrayList2.add(Long.valueOf(type4.getId()));
                if (Logger.shouldLog(LogTag.JFR_METADATA, LogLevel.TRACE)) {
                    Logger.log(LogTag.JFR_METADATA, LogLevel.TRACE, "Removed obsolete metadata " + type4.getName());
                }
            }
            type4.setRemove(true);
        }
        Iterator<E> it = arrayList2.iterator();
        while (it.hasNext()) {
            types.remove((Long) it.next());
        }
        return !arrayList2.isEmpty();
    }

    public void addType(Type type) {
        addTypes(Collections.singletonList(type));
    }

    public static void addTypes(List<Type> list) {
        if (!list.isEmpty()) {
            visitReachable(list, type -> {
                return !types.containsKey(Long.valueOf(type.getId()));
            }, type2 -> {
                types.put(Long.valueOf(type2.getId()), type2);
            });
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    private static void visitReachable(Collection<Type> collection, Predicate<Type> predicate, Consumer<Type> consumer) {
        ArrayDeque arrayDeque = new ArrayDeque(collection);
        while (!arrayDeque.isEmpty()) {
            Type type = (Type) arrayDeque.poll();
            if (predicate.test(type)) {
                consumer.accept(type);
                visitAnnotations(arrayDeque, type.getAnnotationElements());
                for (ValueDescriptor valueDescriptor : type.getFields()) {
                    arrayDeque.add(PrivateAccess.getInstance().getType(valueDescriptor));
                    visitAnnotations(arrayDeque, valueDescriptor.getAnnotationElements());
                }
                if (type instanceof PlatformEventType) {
                    for (SettingDescriptor settingDescriptor : ((PlatformEventType) type).getAllSettings()) {
                        arrayDeque.add(PrivateAccess.getInstance().getType(settingDescriptor));
                        visitAnnotations(arrayDeque, settingDescriptor.getAnnotationElements());
                    }
                }
            }
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    private static void visitAnnotations(Queue<Type> queue, List<AnnotationElement> list) {
        ArrayDeque arrayDeque = new ArrayDeque(list);
        HashSet hashSet = new HashSet();
        while (!arrayDeque.isEmpty()) {
            AnnotationElement annotationElement = (AnnotationElement) arrayDeque.poll();
            if (!hashSet.contains(annotationElement)) {
                queue.add(PrivateAccess.getInstance().getType(annotationElement));
                hashSet.add(annotationElement);
            }
            arrayDeque.addAll(annotationElement.getAnnotationElements());
        }
    }
}
