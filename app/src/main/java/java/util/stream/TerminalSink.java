package java.util.stream;

import java.util.function.Supplier;

/* loaded from: rt.jar:java/util/stream/TerminalSink.class */
interface TerminalSink<T, R> extends Sink<T>, Supplier<R> {
}
