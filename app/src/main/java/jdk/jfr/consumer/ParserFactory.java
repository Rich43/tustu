package jdk.jfr.consumer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import jdk.jfr.EventType;
import jdk.jfr.ValueDescriptor;
import jdk.jfr.internal.MetadataDescriptor;
import jdk.jfr.internal.PrivateAccess;
import jdk.jfr.internal.Type;
import jdk.jfr.internal.consumer.RecordingInput;

/* loaded from: jfr.jar:jdk/jfr/consumer/ParserFactory.class */
final class ParserFactory {
    private final TimeConverter timeConverter;
    private final LongMap<Parser> parsers = new LongMap<>();
    private final LongMap<Type> types = new LongMap<>();
    private final LongMap<ConstantMap> constantPools = new LongMap<>();

    public ParserFactory(MetadataDescriptor metadataDescriptor, TimeConverter timeConverter) throws IOException {
        this.timeConverter = timeConverter;
        for (Type type : metadataDescriptor.getTypes()) {
            this.types.put(type.getId(), type);
        }
        Iterator<Type> it = this.types.iterator();
        while (it.hasNext()) {
            Type next = it.next();
            if (!next.getFields().isEmpty()) {
                CompositeParser compositeParserCreateCompositeParser = createCompositeParser(next);
                if (next.isSimpleType()) {
                    this.parsers.put(next.getId(), compositeParserCreateCompositeParser.parsers[0]);
                }
            }
        }
        for (EventType eventType : metadataDescriptor.getEventTypes()) {
            this.parsers.put(eventType.getId(), createEventParser(eventType));
        }
    }

    public LongMap<Parser> getParsers() {
        return this.parsers;
    }

    public LongMap<ConstantMap> getConstantPools() {
        return this.constantPools;
    }

    public LongMap<Type> getTypeMap() {
        return this.types;
    }

    private EventParser createEventParser(EventType eventType) throws IOException {
        ArrayList arrayList = new ArrayList();
        Iterator<ValueDescriptor> it = eventType.getFields().iterator();
        while (it.hasNext()) {
            arrayList.add(createParser(it.next()));
        }
        return new EventParser(this.timeConverter, eventType, (Parser[]) arrayList.toArray(new Parser[0]));
    }

    private Parser createParser(ValueDescriptor valueDescriptor) throws IOException {
        boolean zIsConstantPool = PrivateAccess.getInstance().isConstantPool(valueDescriptor);
        if (valueDescriptor.isArray()) {
            return new ArrayParser(createParser(PrivateAccess.getInstance().newValueDescriptor(valueDescriptor.getName(), PrivateAccess.getInstance().getType(valueDescriptor), valueDescriptor.getAnnotationElements(), 0, zIsConstantPool, null)));
        }
        long typeId = valueDescriptor.getTypeId();
        Type type = this.types.get(typeId);
        if (type == null) {
            throw new IOException("Type '" + valueDescriptor.getTypeName() + "' is not defined");
        }
        if (zIsConstantPool) {
            ConstantMap constantMap = this.constantPools.get(typeId);
            if (constantMap == null) {
                constantMap = new ConstantMap(ObjectFactory.create(type, this.timeConverter), type.getName());
                this.constantPools.put(typeId, constantMap);
            }
            return new ConstantMapValueParser(constantMap);
        }
        Parser parser = this.parsers.get(typeId);
        if (parser == null) {
            if (!valueDescriptor.getFields().isEmpty()) {
                return createCompositeParser(type);
            }
            return registerParserType(type, createPrimitiveParser(type));
        }
        return parser;
    }

    private Parser createPrimitiveParser(Type type) throws IOException {
        switch (type.getName()) {
            case "int":
                return new IntegerParser();
            case "long":
                return new LongParser();
            case "float":
                return new FloatParser();
            case "double":
                return new DoubleParser();
            case "char":
                return new CharacterParser();
            case "boolean":
                return new BooleanParser();
            case "short":
                return new ShortParser();
            case "byte":
                return new ByteParser();
            case "java.lang.String":
                ConstantMap constantMap = new ConstantMap(ObjectFactory.create(type, this.timeConverter), type.getName());
                this.constantPools.put(type.getId(), constantMap);
                return new StringParser(constantMap);
            default:
                throw new IOException("Unknown primitive type " + type.getName());
        }
    }

    private Parser registerParserType(Type type, Parser parser) {
        Parser parser2 = this.parsers.get(type.getId());
        if (parser2 != null) {
            return parser2;
        }
        this.parsers.put(type.getId(), parser);
        return parser;
    }

    private CompositeParser createCompositeParser(Type type) throws IOException {
        List<ValueDescriptor> fields = type.getFields();
        Parser[] parserArr = new Parser[fields.size()];
        CompositeParser compositeParser = new CompositeParser(parserArr);
        registerParserType(type, compositeParser);
        int i2 = 0;
        Iterator<ValueDescriptor> it = fields.iterator();
        while (it.hasNext()) {
            int i3 = i2;
            i2++;
            parserArr[i3] = createParser(it.next());
        }
        return compositeParser;
    }

    /* loaded from: jfr.jar:jdk/jfr/consumer/ParserFactory$BooleanParser.class */
    private static final class BooleanParser extends Parser {
        private BooleanParser() {
        }

        @Override // jdk.jfr.consumer.Parser
        public Object parse(RecordingInput recordingInput) throws IOException {
            return recordingInput.readBoolean() ? Boolean.TRUE : Boolean.FALSE;
        }
    }

    /* loaded from: jfr.jar:jdk/jfr/consumer/ParserFactory$ByteParser.class */
    private static final class ByteParser extends Parser {
        private ByteParser() {
        }

        @Override // jdk.jfr.consumer.Parser
        public Object parse(RecordingInput recordingInput) throws IOException {
            return Byte.valueOf(recordingInput.readByte());
        }
    }

    /* loaded from: jfr.jar:jdk/jfr/consumer/ParserFactory$LongParser.class */
    private static final class LongParser extends Parser {
        private LongParser() {
        }

        @Override // jdk.jfr.consumer.Parser
        public Object parse(RecordingInput recordingInput) throws IOException {
            return Long.valueOf(recordingInput.readLong());
        }
    }

    /* loaded from: jfr.jar:jdk/jfr/consumer/ParserFactory$IntegerParser.class */
    private static final class IntegerParser extends Parser {
        private IntegerParser() {
        }

        @Override // jdk.jfr.consumer.Parser
        public Object parse(RecordingInput recordingInput) throws IOException {
            return Integer.valueOf(recordingInput.readInt());
        }
    }

    /* loaded from: jfr.jar:jdk/jfr/consumer/ParserFactory$ShortParser.class */
    private static final class ShortParser extends Parser {
        private ShortParser() {
        }

        @Override // jdk.jfr.consumer.Parser
        public Object parse(RecordingInput recordingInput) throws IOException {
            return Short.valueOf(recordingInput.readShort());
        }
    }

    /* loaded from: jfr.jar:jdk/jfr/consumer/ParserFactory$CharacterParser.class */
    private static final class CharacterParser extends Parser {
        private CharacterParser() {
        }

        @Override // jdk.jfr.consumer.Parser
        public Object parse(RecordingInput recordingInput) throws IOException {
            return Character.valueOf(recordingInput.readChar());
        }
    }

    /* loaded from: jfr.jar:jdk/jfr/consumer/ParserFactory$FloatParser.class */
    private static final class FloatParser extends Parser {
        private FloatParser() {
        }

        @Override // jdk.jfr.consumer.Parser
        public Object parse(RecordingInput recordingInput) throws IOException {
            return Float.valueOf(recordingInput.readFloat());
        }
    }

    /* loaded from: jfr.jar:jdk/jfr/consumer/ParserFactory$DoubleParser.class */
    private static final class DoubleParser extends Parser {
        private DoubleParser() {
        }

        @Override // jdk.jfr.consumer.Parser
        public Object parse(RecordingInput recordingInput) throws IOException {
            return Double.valueOf(recordingInput.readDouble());
        }
    }

    /* loaded from: jfr.jar:jdk/jfr/consumer/ParserFactory$StringParser.class */
    private static final class StringParser extends Parser {
        private final ConstantMap stringConstantMap;
        private String last;

        StringParser(ConstantMap constantMap) {
            this.stringConstantMap = constantMap;
        }

        @Override // jdk.jfr.consumer.Parser
        public Object parse(RecordingInput recordingInput) throws IOException {
            String encodedString = parseEncodedString(recordingInput);
            if (!Objects.equals(encodedString, this.last)) {
                this.last = encodedString;
            }
            return this.last;
        }

        private String parseEncodedString(RecordingInput recordingInput) throws IOException {
            byte b2 = recordingInput.readByte();
            if (b2 == 2) {
                return (String) this.stringConstantMap.get(recordingInput.readLong());
            }
            return recordingInput.readEncodedString(b2);
        }
    }

    /* loaded from: jfr.jar:jdk/jfr/consumer/ParserFactory$ArrayParser.class */
    private static final class ArrayParser extends Parser {
        private final Parser elementParser;

        public ArrayParser(Parser parser) {
            this.elementParser = parser;
        }

        @Override // jdk.jfr.consumer.Parser
        public Object parse(RecordingInput recordingInput) throws IOException {
            int i2 = recordingInput.readInt();
            recordingInput.require(i2, "Array size %d exceeds available data");
            Object[] objArr = new Object[i2];
            for (int i3 = 0; i3 < i2; i3++) {
                objArr[i3] = this.elementParser.parse(recordingInput);
            }
            return objArr;
        }
    }

    /* loaded from: jfr.jar:jdk/jfr/consumer/ParserFactory$CompositeParser.class */
    private static final class CompositeParser extends Parser {
        private final Parser[] parsers;

        public CompositeParser(Parser[] parserArr) {
            this.parsers = parserArr;
        }

        @Override // jdk.jfr.consumer.Parser
        public Object parse(RecordingInput recordingInput) throws IOException {
            Object[] objArr = new Object[this.parsers.length];
            for (int i2 = 0; i2 < objArr.length; i2++) {
                objArr[i2] = this.parsers[i2].parse(recordingInput);
            }
            return objArr;
        }
    }

    /* loaded from: jfr.jar:jdk/jfr/consumer/ParserFactory$ConstantMapValueParser.class */
    private static final class ConstantMapValueParser extends Parser {
        private final ConstantMap pool;

        ConstantMapValueParser(ConstantMap constantMap) {
            this.pool = constantMap;
        }

        @Override // jdk.jfr.consumer.Parser
        public Object parse(RecordingInput recordingInput) throws IOException {
            return this.pool.get(recordingInput.readLong());
        }
    }
}
