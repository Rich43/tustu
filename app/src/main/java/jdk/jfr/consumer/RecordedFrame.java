package jdk.jfr.consumer;

import java.util.List;
import jdk.jfr.ValueDescriptor;
import jdk.jfr.internal.Type;

/* loaded from: jfr.jar:jdk/jfr/consumer/RecordedFrame.class */
public final class RecordedFrame extends RecordedObject {
    static ObjectFactory<RecordedFrame> createFactory(Type type, final TimeConverter timeConverter) {
        return new ObjectFactory<RecordedFrame>(type) { // from class: jdk.jfr.consumer.RecordedFrame.1
            @Override // jdk.jfr.consumer.ObjectFactory
            /* bridge */ /* synthetic */ RecordedFrame createTyped(List list, long j2, Object[] objArr) {
                return createTyped((List<ValueDescriptor>) list, j2, objArr);
            }

            /* JADX WARN: Can't rename method to resolve collision */
            @Override // jdk.jfr.consumer.ObjectFactory
            RecordedFrame createTyped(List<ValueDescriptor> list, long j2, Object[] objArr) {
                return new RecordedFrame(list, objArr, timeConverter);
            }
        };
    }

    RecordedFrame(List<ValueDescriptor> list, Object[] objArr, TimeConverter timeConverter) {
        super(list, objArr, timeConverter);
    }

    public boolean isJavaFrame() {
        if (hasField("javaFrame")) {
            return ((Boolean) getTyped("javaFrame", Boolean.class, Boolean.TRUE)).booleanValue();
        }
        return true;
    }

    public int getBytecodeIndex() {
        return ((Integer) getTyped("bytecodeIndex", Integer.class, -1)).intValue();
    }

    public int getLineNumber() {
        return ((Integer) getTyped("lineNumber", Integer.class, -1)).intValue();
    }

    public String getType() {
        return (String) getTyped("type", String.class, null);
    }

    public RecordedMethod getMethod() {
        return (RecordedMethod) getTyped("method", RecordedMethod.class, null);
    }
}
