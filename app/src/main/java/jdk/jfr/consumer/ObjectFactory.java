package jdk.jfr.consumer;

import java.util.List;
import jdk.jfr.ValueDescriptor;
import jdk.jfr.internal.Type;

/* loaded from: jfr.jar:jdk/jfr/consumer/ObjectFactory.class */
abstract class ObjectFactory<T> {
    static final String TYPE_PREFIX_VERSION_1 = "com.oracle.jfr.types.";
    static final String TYPE_PREFIX_VERSION_2 = "jdk.types.";
    static final String STACK_FRAME_VERSION_1 = "com.oracle.jfr.types.StackFrame";
    static final String STACK_FRAME_VERSION_2 = "jdk.types.StackFrame";
    private final List<ValueDescriptor> valueDescriptors;

    abstract T createTyped(List<ValueDescriptor> list, long j2, Object[] objArr);

    public static ObjectFactory<?> create(Type type, TimeConverter timeConverter) {
        switch (type.getName()) {
            case "java.lang.Thread":
                return RecordedThread.createFactory(type, timeConverter);
            case "com.oracle.jfr.types.StackFrame":
            case "jdk.types.StackFrame":
                return RecordedFrame.createFactory(type, timeConverter);
            case "com.oracle.jfr.types.Method":
            case "jdk.types.Method":
                return RecordedMethod.createFactory(type, timeConverter);
            case "com.oracle.jfr.types.ThreadGroup":
            case "jdk.types.ThreadGroup":
                return RecordedThreadGroup.createFactory(type, timeConverter);
            case "com.oracle.jfr.types.StackTrace":
            case "jdk.types.StackTrace":
                return RecordedStackTrace.createFactory(type, timeConverter);
            case "com.oracle.jfr.types.ClassLoader":
            case "jdk.types.ClassLoader":
                return RecordedClassLoader.createFactory(type, timeConverter);
            case "java.lang.Class":
                return RecordedClass.createFactory(type, timeConverter);
            default:
                return null;
        }
    }

    ObjectFactory(Type type) {
        this.valueDescriptors = type.getFields();
    }

    T createObject(long j2, Object obj) {
        if (obj == null) {
            return null;
        }
        if (obj instanceof Object[]) {
            return createTyped(this.valueDescriptors, j2, (Object[]) obj);
        }
        throw new InternalError("Object factory must have struct type");
    }
}
