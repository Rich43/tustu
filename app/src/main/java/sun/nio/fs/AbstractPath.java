package sun.nio.fs;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.Iterator;
import java.util.NoSuchElementException;

/* loaded from: rt.jar:sun/nio/fs/AbstractPath.class */
abstract class AbstractPath implements Path {
    protected AbstractPath() {
    }

    @Override // java.nio.file.Path
    public final boolean startsWith(String str) {
        return startsWith(getFileSystem().getPath(str, new String[0]));
    }

    @Override // java.nio.file.Path
    public final boolean endsWith(String str) {
        return endsWith(getFileSystem().getPath(str, new String[0]));
    }

    @Override // java.nio.file.Path
    public final Path resolve(String str) {
        return resolve(getFileSystem().getPath(str, new String[0]));
    }

    @Override // java.nio.file.Path
    public final Path resolveSibling(Path path) {
        if (path == null) {
            throw new NullPointerException();
        }
        Path parent = getParent();
        return parent == null ? path : parent.resolve(path);
    }

    @Override // java.nio.file.Path
    public final Path resolveSibling(String str) {
        return resolveSibling(getFileSystem().getPath(str, new String[0]));
    }

    @Override // java.nio.file.Path, java.lang.Iterable, java.util.List
    public final Iterator<Path> iterator() {
        return new Iterator<Path>() { // from class: sun.nio.fs.AbstractPath.1

            /* renamed from: i, reason: collision with root package name */
            private int f13595i = 0;

            @Override // java.util.Iterator
            public boolean hasNext() {
                return this.f13595i < AbstractPath.this.getNameCount();
            }

            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.util.Iterator
            public Path next() {
                if (this.f13595i < AbstractPath.this.getNameCount()) {
                    Path name = AbstractPath.this.getName(this.f13595i);
                    this.f13595i++;
                    return name;
                }
                throw new NoSuchElementException();
            }

            @Override // java.util.Iterator
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }

    @Override // java.nio.file.Path
    public final File toFile() {
        return new File(toString());
    }

    @Override // java.nio.file.Path, java.nio.file.Watchable
    public final WatchKey register(WatchService watchService, WatchEvent.Kind<?>... kindArr) throws IOException {
        return register(watchService, kindArr, new WatchEvent.Modifier[0]);
    }
}
