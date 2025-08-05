package jdk.jfr.internal.consumer;

import java.io.DataInput;
import java.io.IOException;
import jdk.jfr.internal.LogLevel;
import jdk.jfr.internal.LogTag;
import jdk.jfr.internal.Logger;
import jdk.jfr.internal.MetadataDescriptor;

/* loaded from: jfr.jar:jdk/jfr/internal/consumer/ChunkHeader.class */
public final class ChunkHeader {
    private static final long METADATA_TYPE_ID = 0;
    private static final byte[] FILE_MAGIC = {70, 76, 82, 0};
    private final short major;
    private final short minor;
    private final long chunkSize;
    private final long chunkStartTicks;
    private final long ticksPerSecond;
    private final long chunkStartNanos;
    private final long metadataPosition;
    private final long absoluteChunkEnd;
    private final long absoluteEventStart;
    private final long absoluteChunkStart;
    private final boolean lastChunk;
    private final RecordingInput input;
    private final long durationNanos;
    private final long id;
    private long constantPoolPosition;

    public ChunkHeader(RecordingInput recordingInput) throws IOException {
        this(recordingInput, 0L, 0L);
    }

    private ChunkHeader(RecordingInput recordingInput, long j2, long j3) throws IOException {
        recordingInput.position(j2);
        if (recordingInput.position() >= recordingInput.size()) {
            throw new IOException("Chunk contains no data");
        }
        verifyMagic(recordingInput);
        this.input = recordingInput;
        this.id = j3;
        Logger.log(LogTag.JFR_SYSTEM_PARSER, LogLevel.INFO, "Chunk " + j3);
        Logger.log(LogTag.JFR_SYSTEM_PARSER, LogLevel.INFO, "Chunk: startPosition=" + j2);
        this.major = recordingInput.readRawShort();
        Logger.log(LogTag.JFR_SYSTEM_PARSER, LogLevel.INFO, "Chunk: major=" + ((int) this.major));
        this.minor = recordingInput.readRawShort();
        Logger.log(LogTag.JFR_SYSTEM_PARSER, LogLevel.INFO, "Chunk: minor=" + ((int) this.minor));
        if (this.major != 1 && this.major != 2) {
            throw new IOException("File version " + ((int) this.major) + "." + ((int) this.minor) + ". Only Flight Recorder files of version 1.x and 2.x can be read by this JDK.");
        }
        this.chunkSize = recordingInput.readRawLong();
        Logger.log(LogTag.JFR_SYSTEM_PARSER, LogLevel.INFO, "Chunk: chunkSize=" + this.chunkSize);
        this.constantPoolPosition = recordingInput.readRawLong();
        Logger.log(LogTag.JFR_SYSTEM_PARSER, LogLevel.INFO, "Chunk: constantPoolPosition=" + this.constantPoolPosition);
        this.metadataPosition = recordingInput.readRawLong();
        Logger.log(LogTag.JFR_SYSTEM_PARSER, LogLevel.INFO, "Chunk: metadataPosition=" + this.metadataPosition);
        this.chunkStartNanos = recordingInput.readRawLong();
        Logger.log(LogTag.JFR_SYSTEM_PARSER, LogLevel.INFO, "Chunk: startNanos=" + this.chunkStartNanos);
        this.durationNanos = recordingInput.readRawLong();
        Logger.log(LogTag.JFR_SYSTEM_PARSER, LogLevel.INFO, "Chunk: durationNanos=" + this.durationNanos);
        this.chunkStartTicks = recordingInput.readRawLong();
        Logger.log(LogTag.JFR_SYSTEM_PARSER, LogLevel.INFO, "Chunk: startTicks=" + this.chunkStartTicks);
        this.ticksPerSecond = recordingInput.readRawLong();
        Logger.log(LogTag.JFR_SYSTEM_PARSER, LogLevel.INFO, "Chunk: ticksPerSecond=" + this.ticksPerSecond);
        recordingInput.readRawInt();
        this.absoluteChunkStart = j2;
        this.absoluteChunkEnd = j2 + this.chunkSize;
        this.lastChunk = recordingInput.size() == this.absoluteChunkEnd;
        this.absoluteEventStart = recordingInput.position();
        recordingInput.position(this.absoluteEventStart);
    }

    public ChunkHeader nextHeader() throws IOException {
        return new ChunkHeader(this.input, this.absoluteChunkEnd, this.id + 1);
    }

    public MetadataDescriptor readMetadata() throws IOException {
        this.input.position(this.absoluteChunkStart + this.metadataPosition);
        this.input.readInt();
        long j2 = this.input.readLong();
        if (j2 != 0) {
            throw new IOException("Expected metadata event. Type id=" + j2 + ", should have been 0");
        }
        this.input.readLong();
        this.input.readLong();
        Logger.log(LogTag.JFR_SYSTEM_PARSER, LogLevel.TRACE, "Metadata id=" + this.input.readLong());
        return MetadataDescriptor.read(this.input);
    }

    public boolean isLastChunk() {
        return this.lastChunk;
    }

    public short getMajor() {
        return this.major;
    }

    public short getMinor() {
        return this.minor;
    }

    public long getAbsoluteChunkStart() {
        return this.absoluteChunkStart;
    }

    public long getConstantPoolPosition() {
        return this.constantPoolPosition;
    }

    public long getStartTicks() {
        return this.chunkStartTicks;
    }

    public double getTicksPerSecond() {
        return this.ticksPerSecond;
    }

    public long getStartNanos() {
        return this.chunkStartNanos;
    }

    public long getEnd() {
        return this.absoluteChunkEnd;
    }

    public long getSize() {
        return this.chunkSize;
    }

    public long getDurationNanos() {
        return this.durationNanos;
    }

    public RecordingInput getInput() {
        return this.input;
    }

    private static void verifyMagic(DataInput dataInput) throws IOException {
        for (byte b2 : FILE_MAGIC) {
            if (dataInput.readByte() != b2) {
                throw new IOException("Not a Flight Recorder file");
            }
        }
    }

    public long getEventStart() {
        return this.absoluteEventStart;
    }
}
