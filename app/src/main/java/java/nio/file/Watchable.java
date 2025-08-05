package java.nio.file;

import java.io.IOException;
import java.nio.file.WatchEvent;

/* loaded from: rt.jar:java/nio/file/Watchable.class */
public interface Watchable {
    WatchKey register(WatchService watchService, WatchEvent.Kind<?>[] kindArr, WatchEvent.Modifier... modifierArr) throws IOException;

    WatchKey register(WatchService watchService, WatchEvent.Kind<?>... kindArr) throws IOException;
}
