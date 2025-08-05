package java.util.stream;

import java.util.Iterator;
import java.util.Spliterator;
import java.util.stream.BaseStream;

/* loaded from: rt.jar:java/util/stream/BaseStream.class */
public interface BaseStream<T, S extends BaseStream<T, S>> extends AutoCloseable {
    /* renamed from: iterator */
    Iterator<T> iterator2();

    /* renamed from: spliterator */
    Spliterator<T> spliterator2();

    boolean isParallel();

    S sequential();

    S parallel();

    S unordered();

    S onClose(Runnable runnable);

    @Override // java.lang.AutoCloseable
    void close();
}
