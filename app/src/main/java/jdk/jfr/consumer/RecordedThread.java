package jdk.jfr.consumer;

import java.util.List;
import jdk.jfr.ValueDescriptor;
import jdk.jfr.internal.Type;

/* loaded from: jfr.jar:jdk/jfr/consumer/RecordedThread.class */
public final class RecordedThread extends RecordedObject {
    private final long uniqueId;

    static ObjectFactory<RecordedThread> createFactory(Type type, final TimeConverter timeConverter) {
        return new ObjectFactory<RecordedThread>(type) { // from class: jdk.jfr.consumer.RecordedThread.1
            @Override // jdk.jfr.consumer.ObjectFactory
            /* bridge */ /* synthetic */ RecordedThread createTyped(List list, long j2, Object[] objArr) {
                return createTyped((List<ValueDescriptor>) list, j2, objArr);
            }

            /* JADX WARN: Can't rename method to resolve collision */
            @Override // jdk.jfr.consumer.ObjectFactory
            RecordedThread createTyped(List<ValueDescriptor> list, long j2, Object[] objArr) {
                return new RecordedThread(list, j2, objArr, timeConverter);
            }
        };
    }

    private RecordedThread(List<ValueDescriptor> list, long j2, Object[] objArr, TimeConverter timeConverter) {
        super(list, objArr, timeConverter);
        this.uniqueId = j2;
    }

    public String getOSName() {
        return (String) getTyped("osName", String.class, null);
    }

    public long getOSThreadId() {
        return ((Long) getTyped("osThreadId", Long.class, -1L)).longValue();
    }

    public RecordedThreadGroup getThreadGroup() {
        return (RecordedThreadGroup) getTyped("group", RecordedThreadGroup.class, null);
    }

    public String getJavaName() {
        return (String) getTyped("javaName", String.class, null);
    }

    public long getJavaThreadId() {
        return ((Long) getTyped("javaThreadId", Long.class, -1L)).longValue();
    }

    public long getId() {
        return this.uniqueId;
    }
}
