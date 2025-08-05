package java.nio.file;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.WatchEvent;
import java.util.Iterator;

/* loaded from: rt.jar:java/nio/file/Path.class */
public interface Path extends Comparable<Path>, Iterable<Path>, Watchable {
    FileSystem getFileSystem();

    boolean isAbsolute();

    Path getRoot();

    Path getFileName();

    Path getParent();

    int getNameCount();

    Path getName(int i2);

    Path subpath(int i2, int i3);

    boolean startsWith(Path path);

    boolean startsWith(String str);

    boolean endsWith(Path path);

    boolean endsWith(String str);

    Path normalize();

    Path resolve(Path path);

    Path resolve(String str);

    Path resolveSibling(Path path);

    Path resolveSibling(String str);

    Path relativize(Path path);

    URI toUri();

    Path toAbsolutePath();

    Path toRealPath(LinkOption... linkOptionArr) throws IOException;

    File toFile();

    WatchKey register(WatchService watchService, WatchEvent.Kind<?>[] kindArr, WatchEvent.Modifier... modifierArr) throws IOException;

    WatchKey register(WatchService watchService, WatchEvent.Kind<?>... kindArr) throws IOException;

    Iterator<Path> iterator();

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // java.lang.Comparable
    int compareTo(Path path);

    boolean equals(Object obj);

    int hashCode();

    String toString();
}
