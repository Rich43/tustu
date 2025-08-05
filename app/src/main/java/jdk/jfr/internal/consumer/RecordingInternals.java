package jdk.jfr.internal.consumer;

import java.io.IOException;
import java.util.List;
import jdk.jfr.consumer.RecordedEvent;
import jdk.jfr.consumer.RecordedObject;
import jdk.jfr.consumer.RecordingFile;
import jdk.jfr.internal.Type;

/* loaded from: jfr.jar:jdk/jfr/internal/consumer/RecordingInternals.class */
public abstract class RecordingInternals {
    public static RecordingInternals INSTANCE;

    public abstract boolean isLastEventInChunk(RecordingFile recordingFile);

    public abstract Object getOffsetDataTime(RecordedObject recordedObject, String str);

    public abstract List<Type> readTypes(RecordingFile recordingFile) throws IOException;

    public abstract void sort(List<RecordedEvent> list);
}
