package jdk.jfr.consumer;

import java.util.List;
import jdk.jfr.ValueDescriptor;
import jdk.jfr.internal.Type;

/* loaded from: jfr.jar:jdk/jfr/consumer/RecordedMethod.class */
public final class RecordedMethod extends RecordedObject {
    static ObjectFactory<RecordedMethod> createFactory(Type type, final TimeConverter timeConverter) {
        return new ObjectFactory<RecordedMethod>(type) { // from class: jdk.jfr.consumer.RecordedMethod.1
            @Override // jdk.jfr.consumer.ObjectFactory
            /* bridge */ /* synthetic */ RecordedMethod createTyped(List list, long j2, Object[] objArr) {
                return createTyped((List<ValueDescriptor>) list, j2, objArr);
            }

            /* JADX WARN: Can't rename method to resolve collision */
            @Override // jdk.jfr.consumer.ObjectFactory
            RecordedMethod createTyped(List<ValueDescriptor> list, long j2, Object[] objArr) {
                return new RecordedMethod(list, objArr, timeConverter);
            }
        };
    }

    private RecordedMethod(List<ValueDescriptor> list, Object[] objArr, TimeConverter timeConverter) {
        super(list, objArr, timeConverter);
    }

    public RecordedClass getType() {
        return (RecordedClass) getTyped("type", RecordedClass.class, null);
    }

    public String getName() {
        return (String) getTyped("name", String.class, null);
    }

    public String getDescriptor() {
        return (String) getTyped("descriptor", String.class, null);
    }

    public int getModifiers() {
        return ((Integer) getTyped("modifiers", Integer.class, 0)).intValue();
    }

    public boolean isHidden() {
        return ((Boolean) getTyped("hidden", Boolean.class, Boolean.FALSE)).booleanValue();
    }
}
