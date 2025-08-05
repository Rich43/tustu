package jdk.jfr.consumer;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import jdk.jfr.ValueDescriptor;
import jdk.jfr.internal.Type;

/* loaded from: jfr.jar:jdk/jfr/consumer/RecordedStackTrace.class */
public final class RecordedStackTrace extends RecordedObject {
    static ObjectFactory<RecordedStackTrace> createFactory(Type type, final TimeConverter timeConverter) {
        return new ObjectFactory<RecordedStackTrace>(type) { // from class: jdk.jfr.consumer.RecordedStackTrace.1
            @Override // jdk.jfr.consumer.ObjectFactory
            /* bridge */ /* synthetic */ RecordedStackTrace createTyped(List list, long j2, Object[] objArr) {
                return createTyped((List<ValueDescriptor>) list, j2, objArr);
            }

            /* JADX WARN: Can't rename method to resolve collision */
            @Override // jdk.jfr.consumer.ObjectFactory
            RecordedStackTrace createTyped(List<ValueDescriptor> list, long j2, Object[] objArr) {
                return new RecordedStackTrace(list, objArr, timeConverter);
            }
        };
    }

    private RecordedStackTrace(List<ValueDescriptor> list, Object[] objArr, TimeConverter timeConverter) {
        super(list, objArr, timeConverter);
    }

    public List<RecordedFrame> getFrames() {
        Object[] objArr = (Object[]) getTyped("frames", Object[].class, null);
        if (objArr == null) {
            return Collections.EMPTY_LIST;
        }
        return Arrays.asList(objArr);
    }

    public boolean isTruncated() {
        return ((Boolean) getTyped("truncated", Boolean.class, true)).booleanValue();
    }
}
