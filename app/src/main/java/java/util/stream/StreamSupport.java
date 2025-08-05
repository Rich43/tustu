package java.util.stream;

import java.util.Objects;
import java.util.Spliterator;
import java.util.function.Supplier;
import java.util.stream.DoublePipeline;
import java.util.stream.IntPipeline;
import java.util.stream.LongPipeline;
import java.util.stream.ReferencePipeline;

/* loaded from: rt.jar:java/util/stream/StreamSupport.class */
public final class StreamSupport {
    private StreamSupport() {
    }

    public static <T> Stream<T> stream(Spliterator<T> spliterator, boolean z2) {
        Objects.requireNonNull(spliterator);
        return new ReferencePipeline.Head((Spliterator<?>) spliterator, StreamOpFlag.fromCharacteristics((Spliterator<?>) spliterator), z2);
    }

    public static <T> Stream<T> stream(Supplier<? extends Spliterator<T>> supplier, int i2, boolean z2) {
        Objects.requireNonNull(supplier);
        return new ReferencePipeline.Head((Supplier<? extends Spliterator<?>>) supplier, StreamOpFlag.fromCharacteristics(i2), z2);
    }

    public static IntStream intStream(Spliterator.OfInt ofInt, boolean z2) {
        return new IntPipeline.Head(ofInt, StreamOpFlag.fromCharacteristics(ofInt), z2);
    }

    public static IntStream intStream(Supplier<? extends Spliterator.OfInt> supplier, int i2, boolean z2) {
        return new IntPipeline.Head(supplier, StreamOpFlag.fromCharacteristics(i2), z2);
    }

    public static LongStream longStream(Spliterator.OfLong ofLong, boolean z2) {
        return new LongPipeline.Head(ofLong, StreamOpFlag.fromCharacteristics(ofLong), z2);
    }

    public static LongStream longStream(Supplier<? extends Spliterator.OfLong> supplier, int i2, boolean z2) {
        return new LongPipeline.Head(supplier, StreamOpFlag.fromCharacteristics(i2), z2);
    }

    public static DoubleStream doubleStream(Spliterator.OfDouble ofDouble, boolean z2) {
        return new DoublePipeline.Head(ofDouble, StreamOpFlag.fromCharacteristics(ofDouble), z2);
    }

    public static DoubleStream doubleStream(Supplier<? extends Spliterator.OfDouble> supplier, int i2, boolean z2) {
        return new DoublePipeline.Head(supplier, StreamOpFlag.fromCharacteristics(i2), z2);
    }
}
