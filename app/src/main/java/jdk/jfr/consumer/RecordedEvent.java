package jdk.jfr.consumer;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import jdk.jfr.EventType;
import jdk.jfr.ValueDescriptor;
import jdk.jfr.internal.EventInstrumentation;

/* loaded from: jfr.jar:jdk/jfr/consumer/RecordedEvent.class */
public final class RecordedEvent extends RecordedObject {
    private final EventType eventType;
    private final long startTime;
    final long endTime;

    RecordedEvent(EventType eventType, List<ValueDescriptor> list, Object[] objArr, long j2, long j3, TimeConverter timeConverter) {
        super(list, objArr, timeConverter);
        this.eventType = eventType;
        this.startTime = j2;
        this.endTime = j3;
    }

    public RecordedStackTrace getStackTrace() {
        return (RecordedStackTrace) getTyped("stackTrace", RecordedStackTrace.class, null);
    }

    public RecordedThread getThread() {
        return (RecordedThread) getTyped(EventInstrumentation.FIELD_EVENT_THREAD, RecordedThread.class, null);
    }

    public EventType getEventType() {
        return this.eventType;
    }

    public Instant getStartTime() {
        return Instant.ofEpochSecond(0L, this.startTime);
    }

    public Instant getEndTime() {
        return Instant.ofEpochSecond(0L, this.endTime);
    }

    public Duration getDuration() {
        return Duration.ofNanos(this.endTime - this.startTime);
    }

    @Override // jdk.jfr.consumer.RecordedObject
    public List<ValueDescriptor> getFields() {
        return getEventType().getFields();
    }
}
