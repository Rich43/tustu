package java.io;

/* loaded from: rt.jar:java/io/Closeable.class */
public interface Closeable extends AutoCloseable {
    @Override // java.lang.AutoCloseable
    void close() throws IOException;
}
