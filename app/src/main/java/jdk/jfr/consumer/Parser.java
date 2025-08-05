package jdk.jfr.consumer;

import java.io.IOException;
import jdk.jfr.internal.consumer.RecordingInput;

/* loaded from: jfr.jar:jdk/jfr/consumer/Parser.class */
abstract class Parser {
    abstract Object parse(RecordingInput recordingInput) throws IOException;

    Parser() {
    }
}
