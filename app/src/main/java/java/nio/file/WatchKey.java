package java.nio.file;

import java.util.List;

/* loaded from: rt.jar:java/nio/file/WatchKey.class */
public interface WatchKey {
    boolean isValid();

    List<WatchEvent<?>> pollEvents();

    boolean reset();

    void cancel();

    Watchable watchable();
}
