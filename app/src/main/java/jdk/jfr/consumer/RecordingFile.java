package jdk.jfr.consumer;

import java.io.Closeable;
import java.io.EOFException;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import jdk.jfr.EventType;
import jdk.jfr.internal.Type;
import jdk.jfr.internal.consumer.ChunkHeader;
import jdk.jfr.internal.consumer.RecordingInput;
import jdk.jfr.internal.consumer.RecordingInternals;

/* loaded from: jfr.jar:jdk/jfr/consumer/RecordingFile.class */
public final class RecordingFile implements Closeable {
    private boolean isLastEventInChunk;
    private final File file;
    private RecordingInput input;
    private ChunkParser chunkParser;
    private RecordedEvent nextEvent;
    private boolean eof;

    static {
        RecordingInternals.INSTANCE = new RecordingInternals() { // from class: jdk.jfr.consumer.RecordingFile.1
            @Override // jdk.jfr.internal.consumer.RecordingInternals
            public List<Type> readTypes(RecordingFile recordingFile) throws IOException {
                return recordingFile.readTypes();
            }

            @Override // jdk.jfr.internal.consumer.RecordingInternals
            public boolean isLastEventInChunk(RecordingFile recordingFile) {
                return recordingFile.isLastEventInChunk;
            }

            @Override // jdk.jfr.internal.consumer.RecordingInternals
            public Object getOffsetDataTime(RecordedObject recordedObject, String str) {
                return recordedObject.getOffsetDateTime(str);
            }

            @Override // jdk.jfr.internal.consumer.RecordingInternals
            public void sort(List<RecordedEvent> list) {
                Collections.sort(list, (recordedEvent, recordedEvent2) -> {
                    return Long.compare(recordedEvent.endTime, recordedEvent2.endTime);
                });
            }
        };
    }

    public RecordingFile(Path path) throws IOException {
        this.file = path.toFile();
        this.input = new RecordingInput(this.file);
        findNext();
    }

    public RecordedEvent readEvent() throws IOException {
        if (this.eof) {
            ensureOpen();
            throw new EOFException();
        }
        this.isLastEventInChunk = false;
        RecordedEvent recordedEvent = this.nextEvent;
        this.nextEvent = this.chunkParser.readEvent();
        if (this.nextEvent == null) {
            this.isLastEventInChunk = true;
            findNext();
        }
        return recordedEvent;
    }

    public boolean hasMoreEvents() {
        return !this.eof;
    }

    public List<EventType> readEventTypes() throws IOException {
        ensureOpen();
        ArrayList arrayList = new ArrayList();
        HashSet hashSet = new HashSet();
        RecordingInput recordingInput = new RecordingInput(this.file);
        Throwable th = null;
        try {
            try {
                ChunkHeader chunkHeader = new ChunkHeader(recordingInput);
                aggregateEventTypeForChunk(chunkHeader, arrayList, hashSet);
                while (!chunkHeader.isLastChunk()) {
                    chunkHeader = chunkHeader.nextHeader();
                    aggregateEventTypeForChunk(chunkHeader, arrayList, hashSet);
                }
                if (recordingInput != null) {
                    if (0 != 0) {
                        try {
                            recordingInput.close();
                        } catch (Throwable th2) {
                            th.addSuppressed(th2);
                        }
                    } else {
                        recordingInput.close();
                    }
                }
                return arrayList;
            } finally {
            }
        } catch (Throwable th3) {
            if (recordingInput != null) {
                if (th != null) {
                    try {
                        recordingInput.close();
                    } catch (Throwable th4) {
                        th.addSuppressed(th4);
                    }
                } else {
                    recordingInput.close();
                }
            }
            throw th3;
        }
    }

    List<Type> readTypes() throws IOException {
        ensureOpen();
        ArrayList arrayList = new ArrayList();
        HashSet<Long> hashSet = new HashSet<>();
        RecordingInput recordingInput = new RecordingInput(this.file);
        Throwable th = null;
        try {
            try {
                ChunkHeader chunkHeader = new ChunkHeader(recordingInput);
                aggregateTypeForChunk(chunkHeader, arrayList, hashSet);
                while (!chunkHeader.isLastChunk()) {
                    chunkHeader = chunkHeader.nextHeader();
                    aggregateTypeForChunk(chunkHeader, arrayList, hashSet);
                }
                if (recordingInput != null) {
                    if (0 != 0) {
                        try {
                            recordingInput.close();
                        } catch (Throwable th2) {
                            th.addSuppressed(th2);
                        }
                    } else {
                        recordingInput.close();
                    }
                }
                return arrayList;
            } finally {
            }
        } catch (Throwable th3) {
            if (recordingInput != null) {
                if (th != null) {
                    try {
                        recordingInput.close();
                    } catch (Throwable th4) {
                        th.addSuppressed(th4);
                    }
                } else {
                    recordingInput.close();
                }
            }
            throw th3;
        }
    }

    private void aggregateTypeForChunk(ChunkHeader chunkHeader, List<Type> list, HashSet<Long> hashSet) throws IOException {
        for (Type type : chunkHeader.readMetadata().getTypes()) {
            if (!hashSet.contains(Long.valueOf(type.getId()))) {
                list.add(type);
                hashSet.add(Long.valueOf(type.getId()));
            }
        }
    }

    private static void aggregateEventTypeForChunk(ChunkHeader chunkHeader, List<EventType> list, HashSet<Long> hashSet) throws IOException {
        for (EventType eventType : chunkHeader.readMetadata().getEventTypes()) {
            if (!hashSet.contains(Long.valueOf(eventType.getId()))) {
                list.add(eventType);
                hashSet.add(Long.valueOf(eventType.getId()));
            }
        }
    }

    @Override // java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        if (this.input != null) {
            this.eof = true;
            this.input.close();
            this.chunkParser = null;
            this.input = null;
            this.nextEvent = null;
        }
    }

    public static List<RecordedEvent> readAllEvents(Path path) throws IOException {
        RecordingFile recordingFile = new RecordingFile(path);
        Throwable th = null;
        try {
            ArrayList arrayList = new ArrayList();
            while (recordingFile.hasMoreEvents()) {
                arrayList.add(recordingFile.readEvent());
            }
            return arrayList;
        } finally {
            if (recordingFile != null) {
                if (0 != 0) {
                    try {
                        recordingFile.close();
                    } catch (Throwable th2) {
                        th.addSuppressed(th2);
                    }
                } else {
                    recordingFile.close();
                }
            }
        }
    }

    private void findNext() throws IOException {
        while (this.nextEvent == null) {
            if (this.chunkParser == null) {
                this.chunkParser = new ChunkParser(this.input);
            } else if (!this.chunkParser.isLastChunk()) {
                this.chunkParser = this.chunkParser.nextChunkParser();
            } else {
                this.eof = true;
                return;
            }
            this.nextEvent = this.chunkParser.readEvent();
        }
    }

    private void ensureOpen() throws IOException {
        if (this.input == null) {
            throw new IOException("Stream Closed");
        }
    }
}
