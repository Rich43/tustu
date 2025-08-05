package jdk.jfr.consumer;

import java.util.List;
import jdk.jfr.ValueDescriptor;
import jdk.jfr.internal.Type;

/* loaded from: jfr.jar:jdk/jfr/consumer/RecordedThreadGroup.class */
public final class RecordedThreadGroup extends RecordedObject {
    static ObjectFactory<RecordedThreadGroup> createFactory(Type type, final TimeConverter timeConverter) {
        return new ObjectFactory<RecordedThreadGroup>(type) { // from class: jdk.jfr.consumer.RecordedThreadGroup.1
            @Override // jdk.jfr.consumer.ObjectFactory
            /* bridge */ /* synthetic */ RecordedThreadGroup createTyped(List list, long j2, Object[] objArr) {
                return createTyped((List<ValueDescriptor>) list, j2, objArr);
            }

            /* JADX WARN: Can't rename method to resolve collision */
            @Override // jdk.jfr.consumer.ObjectFactory
            RecordedThreadGroup createTyped(List<ValueDescriptor> list, long j2, Object[] objArr) {
                return new RecordedThreadGroup(list, objArr, timeConverter);
            }
        };
    }

    private RecordedThreadGroup(List<ValueDescriptor> list, Object[] objArr, TimeConverter timeConverter) {
        super(list, objArr, timeConverter);
    }

    public String getName() {
        return (String) getTyped("name", String.class, null);
    }

    public RecordedThreadGroup getParent() {
        return (RecordedThreadGroup) getTyped("parent", RecordedThreadGroup.class, null);
    }
}
