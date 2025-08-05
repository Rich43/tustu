package jdk.jfr.consumer;

import java.util.List;
import jdk.jfr.ValueDescriptor;
import jdk.jfr.internal.Type;

/* loaded from: jfr.jar:jdk/jfr/consumer/RecordedClassLoader.class */
public final class RecordedClassLoader extends RecordedObject {
    private final long uniqueId;

    static ObjectFactory<RecordedClassLoader> createFactory(Type type, final TimeConverter timeConverter) {
        return new ObjectFactory<RecordedClassLoader>(type) { // from class: jdk.jfr.consumer.RecordedClassLoader.1
            @Override // jdk.jfr.consumer.ObjectFactory
            /* bridge */ /* synthetic */ RecordedClassLoader createTyped(List list, long j2, Object[] objArr) {
                return createTyped((List<ValueDescriptor>) list, j2, objArr);
            }

            /* JADX WARN: Can't rename method to resolve collision */
            @Override // jdk.jfr.consumer.ObjectFactory
            RecordedClassLoader createTyped(List<ValueDescriptor> list, long j2, Object[] objArr) {
                return new RecordedClassLoader(list, j2, objArr, timeConverter);
            }
        };
    }

    private RecordedClassLoader(List<ValueDescriptor> list, long j2, Object[] objArr, TimeConverter timeConverter) {
        super(list, objArr, timeConverter);
        this.uniqueId = j2;
    }

    public RecordedClass getType() {
        return (RecordedClass) getTyped("type", RecordedClass.class, null);
    }

    public String getName() {
        return (String) getTyped("name", String.class, null);
    }

    public long getId() {
        return this.uniqueId;
    }
}
