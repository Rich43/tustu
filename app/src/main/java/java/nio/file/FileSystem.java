package java.nio.file;

import java.io.Closeable;
import java.io.IOException;
import java.nio.file.attribute.UserPrincipalLookupService;
import java.nio.file.spi.FileSystemProvider;
import java.util.Set;

/* loaded from: rt.jar:java/nio/file/FileSystem.class */
public abstract class FileSystem implements Closeable {
    public abstract FileSystemProvider provider();

    @Override // java.io.Closeable, java.lang.AutoCloseable
    public abstract void close() throws IOException;

    public abstract boolean isOpen();

    public abstract boolean isReadOnly();

    public abstract String getSeparator();

    public abstract Iterable<Path> getRootDirectories();

    public abstract Iterable<FileStore> getFileStores();

    public abstract Set<String> supportedFileAttributeViews();

    public abstract Path getPath(String str, String... strArr);

    public abstract PathMatcher getPathMatcher(String str);

    public abstract UserPrincipalLookupService getUserPrincipalLookupService();

    public abstract WatchService newWatchService() throws IOException;

    protected FileSystem() {
    }
}
