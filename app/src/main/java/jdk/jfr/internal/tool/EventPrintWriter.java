package jdk.jfr.internal.tool;

import com.sun.media.sound.MidiUtils;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import jdk.jfr.EventType;
import jdk.jfr.Timespan;
import jdk.jfr.Timestamp;
import jdk.jfr.ValueDescriptor;
import jdk.jfr.consumer.RecordedEvent;
import jdk.jfr.consumer.RecordedObject;
import jdk.jfr.consumer.RecordingFile;
import jdk.jfr.internal.consumer.RecordingInternals;

/* loaded from: jfr.jar:jdk/jfr/internal/tool/EventPrintWriter.class */
abstract class EventPrintWriter extends StructuredWriter {
    protected static final String STACK_TRACE_FIELD = "stackTrace";
    protected static final String EVENT_THREAD_FIELD = "eventThread";
    private Predicate<EventType> eventFilter;
    private int stackDepth;
    private Map<ValueDescriptor, ValueType> typeOfValues;

    /* loaded from: jfr.jar:jdk/jfr/internal/tool/EventPrintWriter$ValueType.class */
    enum ValueType {
        TIMESPAN,
        TIMESTAMP,
        OTHER
    }

    protected abstract void print(List<RecordedEvent> list);

    EventPrintWriter(PrintWriter printWriter) {
        super(printWriter);
        this.eventFilter = eventType -> {
            return true;
        };
        this.typeOfValues = new HashMap();
    }

    void print(Path path) throws IOException {
        ArrayList arrayList = new ArrayList(MidiUtils.DEFAULT_TEMPO_MPQ);
        printBegin();
        RecordingFile recordingFile = new RecordingFile(path);
        Throwable th = null;
        while (recordingFile.hasMoreEvents()) {
            try {
                try {
                    RecordedEvent event = recordingFile.readEvent();
                    if (acceptEvent(event)) {
                        arrayList.add(event);
                    }
                    if (RecordingInternals.INSTANCE.isLastEventInChunk(recordingFile)) {
                        RecordingInternals.INSTANCE.sort(arrayList);
                        print(arrayList);
                        arrayList.clear();
                    }
                } finally {
                }
            } catch (Throwable th2) {
                if (recordingFile != null) {
                    if (th != null) {
                        try {
                            recordingFile.close();
                        } catch (Throwable th3) {
                            th.addSuppressed(th3);
                        }
                    } else {
                        recordingFile.close();
                    }
                }
                throw th2;
            }
        }
        if (recordingFile != null) {
            if (0 != 0) {
                try {
                    recordingFile.close();
                } catch (Throwable th4) {
                    th.addSuppressed(th4);
                }
            } else {
                recordingFile.close();
            }
        }
        printEnd();
        flush(true);
    }

    protected void printEnd() {
    }

    protected void printBegin() {
    }

    public final void setEventFilter(Predicate<EventType> predicate) {
        this.eventFilter = predicate;
    }

    protected final boolean acceptEvent(RecordedEvent recordedEvent) {
        return this.eventFilter.test(recordedEvent.getEventType());
    }

    protected final int getStackDepth() {
        return this.stackDepth;
    }

    protected final boolean isLateField(String str) {
        return str.equals("eventThread") || str.equals("stackTrace");
    }

    public void setStackDepth(int i2) {
        this.stackDepth = i2;
    }

    protected Object getValue(RecordedObject recordedObject, ValueDescriptor valueDescriptor) {
        ValueType valueTypeDetermineValueType = this.typeOfValues.get(valueDescriptor);
        if (valueTypeDetermineValueType == null) {
            valueTypeDetermineValueType = determineValueType(valueDescriptor);
            this.typeOfValues.put(valueDescriptor, valueTypeDetermineValueType);
        }
        switch (valueTypeDetermineValueType) {
            case TIMESPAN:
                return recordedObject.getDuration(valueDescriptor.getName());
            case TIMESTAMP:
                return RecordingInternals.INSTANCE.getOffsetDataTime(recordedObject, valueDescriptor.getName());
            default:
                return recordedObject.getValue(valueDescriptor.getName());
        }
    }

    private ValueType determineValueType(ValueDescriptor valueDescriptor) {
        if (valueDescriptor.getAnnotation(Timespan.class) != null) {
            return ValueType.TIMESPAN;
        }
        if (valueDescriptor.getAnnotation(Timestamp.class) != null) {
            return ValueType.TIMESTAMP;
        }
        return ValueType.OTHER;
    }
}
