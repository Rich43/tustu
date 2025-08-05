package jdk.jfr.consumer;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.function.Supplier;
import jdk.jfr.EventType;
import jdk.jfr.internal.LogLevel;
import jdk.jfr.internal.LogTag;
import jdk.jfr.internal.Logger;
import jdk.jfr.internal.MetadataDescriptor;
import jdk.jfr.internal.Type;
import jdk.jfr.internal.consumer.ChunkHeader;
import jdk.jfr.internal.consumer.RecordingInput;

/* loaded from: jfr.jar:jdk/jfr/consumer/ChunkParser.class */
final class ChunkParser {
    private static final long CONSTANT_POOL_TYPE_ID = 1;
    private final RecordingInput input;
    private final LongMap<Parser> parsers;
    private final ChunkHeader chunkHeader;
    private final long absoluteChunkEnd;
    private final MetadataDescriptor metadata;
    private final LongMap<Type> typeMap;
    private final TimeConverter timeConverter;

    public ChunkParser(RecordingInput recordingInput) throws IOException {
        this(new ChunkHeader(recordingInput));
    }

    private ChunkParser(ChunkHeader chunkHeader) throws IOException {
        this.input = chunkHeader.getInput();
        this.chunkHeader = chunkHeader;
        this.metadata = chunkHeader.readMetadata();
        this.absoluteChunkEnd = chunkHeader.getEnd();
        this.timeConverter = new TimeConverter(this.chunkHeader, this.metadata.getGMTOffset());
        ParserFactory parserFactory = new ParserFactory(this.metadata, this.timeConverter);
        LongMap<ConstantMap> constantPools = parserFactory.getConstantPools();
        this.parsers = parserFactory.getParsers();
        this.typeMap = parserFactory.getTypeMap();
        fillConstantPools(this.parsers, constantPools);
        constantPools.forEach((v0) -> {
            v0.setIsResolving();
        });
        constantPools.forEach((v0) -> {
            v0.resolve();
        });
        constantPools.forEach((v0) -> {
            v0.setResolved();
        });
        this.input.position(this.chunkHeader.getEventStart());
    }

    public RecordedEvent readEvent() throws IOException {
        while (this.input.position() < this.absoluteChunkEnd) {
            long jPosition = this.input.position();
            int i2 = this.input.readInt();
            if (i2 == 0) {
                throw new IOException("Event can't have zero size");
            }
            long j2 = this.input.readLong();
            if (j2 > 1) {
                Parser parser = this.parsers.get(j2);
                if (parser instanceof EventParser) {
                    return (RecordedEvent) parser.parse(this.input);
                }
            }
            this.input.position(jPosition + i2);
        }
        return null;
    }

    private void fillConstantPools(LongMap<Parser> longMap, LongMap<ConstantMap> longMap2) throws IOException {
        long absoluteChunkStart = this.chunkHeader.getAbsoluteChunkStart();
        long constantPoolPosition = this.chunkHeader.getConstantPoolPosition();
        while (constantPoolPosition != 0) {
            absoluteChunkStart += constantPoolPosition;
            this.input.position(absoluteChunkStart);
            int i2 = this.input.readInt();
            long j2 = this.input.readLong();
            if (j2 != 1) {
                throw new IOException("Expected check point event (id = 1) at position " + absoluteChunkStart + ", but found type id = " + j2);
            }
            this.input.readLong();
            this.input.readLong();
            constantPoolPosition = this.input.readLong();
            boolean z2 = this.input.readBoolean();
            int i3 = this.input.readInt();
            Logger.log(LogTag.JFR_SYSTEM_PARSER, LogLevel.TRACE, (Supplier<String>) () -> {
                return "New constant pool: startPosition=" + absoluteChunkStart + ", size=" + i2 + ", deltaToNext=" + constantPoolPosition + ", flush=" + z2 + ", poolCount=" + i3;
            });
            for (int i4 = 0; i4 < i3; i4++) {
                long j3 = this.input.readLong();
                ConstantMap constantMap = longMap2.get(j3);
                Type type = this.typeMap.get(j3);
                if (constantMap == null) {
                    Logger.log(LogTag.JFR_SYSTEM_PARSER, LogLevel.INFO, "Found constant pool(" + j3 + ") that is never used");
                    if (type == null) {
                        throw new IOException("Error parsing constant pool type " + getName(j3) + " at position " + this.input.position() + " at check point between [" + absoluteChunkStart + ", " + absoluteChunkStart + i2 + "]");
                    }
                    constantMap = new ConstantMap(ObjectFactory.create(type, this.timeConverter), type.getName());
                    longMap2.put(type.getId(), constantMap);
                }
                Parser parser = longMap.get(j3);
                if (parser == null) {
                    throw new IOException("Could not find constant pool type with id = " + j3);
                }
                try {
                    int i5 = this.input.readInt();
                    Logger.log(LogTag.JFR_SYSTEM_PARSER, LogLevel.TRACE, (Supplier<String>) () -> {
                        return "Constant: " + getName(j3) + "[" + i5 + "]";
                    });
                    for (int i6 = 0; i6 < i5; i6++) {
                        constantMap.put(this.input.readLong(), parser.parse(this.input));
                    }
                } catch (Exception e2) {
                    throw new IOException("Error parsing constant pool type " + getName(j3) + " at position " + this.input.position() + " at check point between [" + absoluteChunkStart + ", " + absoluteChunkStart + i2 + "]", e2);
                }
            }
            if (this.input.position() != absoluteChunkStart + i2) {
                throw new IOException("Size of check point event doesn't match content");
            }
        }
    }

    private String getName(long j2) {
        Type type = this.typeMap.get(j2);
        return type == null ? "unknown(" + j2 + ")" : type.getName();
    }

    public Collection<Type> getTypes() {
        return this.metadata.getTypes();
    }

    public List<EventType> getEventTypes() {
        return this.metadata.getEventTypes();
    }

    public boolean isLastChunk() {
        return this.chunkHeader.isLastChunk();
    }

    public ChunkParser nextChunkParser() throws IOException {
        return new ChunkParser(this.chunkHeader.nextHeader());
    }
}
