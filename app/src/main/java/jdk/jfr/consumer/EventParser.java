package jdk.jfr.consumer;

import java.io.IOException;
import java.util.List;
import jdk.jfr.EventType;
import jdk.jfr.ValueDescriptor;
import jdk.jfr.internal.consumer.RecordingInput;

/* loaded from: jfr.jar:jdk/jfr/consumer/EventParser.class */
final class EventParser extends Parser {
    private final Parser[] parsers;
    private final EventType eventType;
    private final TimeConverter timeConverter;
    private final boolean hasDuration;
    private final List<ValueDescriptor> valueDescriptors;

    EventParser(TimeConverter timeConverter, EventType eventType, Parser[] parserArr) {
        this.timeConverter = timeConverter;
        this.parsers = parserArr;
        this.eventType = eventType;
        this.hasDuration = eventType.getField("duration") != null;
        this.valueDescriptors = eventType.getFields();
    }

    @Override // jdk.jfr.consumer.Parser
    public Object parse(RecordingInput recordingInput) throws IOException {
        Object[] objArr = new Object[this.parsers.length];
        for (int i2 = 0; i2 < this.parsers.length; i2++) {
            objArr[i2] = this.parsers[i2].parse(recordingInput);
        }
        Long l2 = (Long) objArr[0];
        long jConvertTimestamp = this.timeConverter.convertTimestamp(l2.longValue());
        if (this.hasDuration) {
            return new RecordedEvent(this.eventType, this.valueDescriptors, objArr, jConvertTimestamp, this.timeConverter.convertTimestamp(l2.longValue() + ((Long) objArr[1]).longValue()), this.timeConverter);
        }
        return new RecordedEvent(this.eventType, this.valueDescriptors, objArr, jConvertTimestamp, jConvertTimestamp, this.timeConverter);
    }
}
