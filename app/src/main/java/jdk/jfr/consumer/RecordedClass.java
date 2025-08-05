package jdk.jfr.consumer;

import java.util.List;
import jdk.jfr.ValueDescriptor;
import jdk.jfr.internal.Type;

/* loaded from: jfr.jar:jdk/jfr/consumer/RecordedClass.class */
public final class RecordedClass extends RecordedObject {
    private final long uniqueId;

    static ObjectFactory<RecordedClass> createFactory(Type type, final TimeConverter timeConverter) {
        return new ObjectFactory<RecordedClass>(type) { // from class: jdk.jfr.consumer.RecordedClass.1
            @Override // jdk.jfr.consumer.ObjectFactory
            /* bridge */ /* synthetic */ RecordedClass createTyped(List list, long j2, Object[] objArr) {
                return createTyped((List<ValueDescriptor>) list, j2, objArr);
            }

            /* JADX WARN: Can't rename method to resolve collision */
            @Override // jdk.jfr.consumer.ObjectFactory
            RecordedClass createTyped(List<ValueDescriptor> list, long j2, Object[] objArr) {
                return new RecordedClass(list, j2, objArr, timeConverter);
            }
        };
    }

    private RecordedClass(List<ValueDescriptor> list, long j2, Object[] objArr, TimeConverter timeConverter) {
        super(list, objArr, timeConverter);
        this.uniqueId = j2;
    }

    public int getModifiers() {
        return ((Integer) getTyped("modifiers", Integer.class, -1)).intValue();
    }

    public RecordedClassLoader getClassLoader() {
        return (RecordedClassLoader) getTyped("classLoader", RecordedClassLoader.class, null);
    }

    public String getName() {
        return ((String) getTyped("name", String.class, null)).replace("/", ".");
    }

    public long getId() {
        return this.uniqueId;
    }
}
