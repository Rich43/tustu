package java.nio.file;

/* loaded from: rt.jar:java/nio/file/WatchEvent.class */
public interface WatchEvent<T> {

    /* loaded from: rt.jar:java/nio/file/WatchEvent$Kind.class */
    public interface Kind<T> {
        String name();

        Class<T> type();
    }

    /* loaded from: rt.jar:java/nio/file/WatchEvent$Modifier.class */
    public interface Modifier {
        String name();
    }

    Kind<T> kind();

    int count();

    T context();
}
