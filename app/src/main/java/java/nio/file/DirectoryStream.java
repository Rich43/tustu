package java.nio.file;

import java.io.Closeable;
import java.io.IOException;
import java.util.Iterator;

/* loaded from: rt.jar:java/nio/file/DirectoryStream.class */
public interface DirectoryStream<T> extends Closeable, Iterable<T> {

    @FunctionalInterface
    /* loaded from: rt.jar:java/nio/file/DirectoryStream$Filter.class */
    public interface Filter<T> {
        boolean accept(T t2) throws IOException;
    }

    Iterator<T> iterator();
}
