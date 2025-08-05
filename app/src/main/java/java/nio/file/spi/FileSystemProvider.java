package java.nio.file.spi;

import com.sun.xml.internal.ws.transport.http.DeploymentDescriptorParser;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.AccessMode;
import java.nio.file.CopyOption;
import java.nio.file.DirectoryStream;
import java.nio.file.FileStore;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.NoSuchFileException;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileAttribute;
import java.nio.file.attribute.FileAttributeView;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import org.icepdf.core.util.PdfOps;

/* loaded from: rt.jar:java/nio/file/spi/FileSystemProvider.class */
public abstract class FileSystemProvider {
    private static volatile List<FileSystemProvider> installedProviders;
    private static final Object lock = new Object();
    private static boolean loadingProviders = false;

    public abstract String getScheme();

    public abstract FileSystem newFileSystem(URI uri, Map<String, ?> map) throws IOException;

    public abstract FileSystem getFileSystem(URI uri);

    public abstract Path getPath(URI uri);

    public abstract SeekableByteChannel newByteChannel(Path path, Set<? extends OpenOption> set, FileAttribute<?>... fileAttributeArr) throws IOException;

    public abstract DirectoryStream<Path> newDirectoryStream(Path path, DirectoryStream.Filter<? super Path> filter) throws IOException;

    public abstract void createDirectory(Path path, FileAttribute<?>... fileAttributeArr) throws IOException;

    public abstract void delete(Path path) throws IOException;

    public abstract void copy(Path path, Path path2, CopyOption... copyOptionArr) throws IOException;

    public abstract void move(Path path, Path path2, CopyOption... copyOptionArr) throws IOException;

    public abstract boolean isSameFile(Path path, Path path2) throws IOException;

    public abstract boolean isHidden(Path path) throws IOException;

    public abstract FileStore getFileStore(Path path) throws IOException;

    public abstract void checkAccess(Path path, AccessMode... accessModeArr) throws IOException;

    public abstract <V extends FileAttributeView> V getFileAttributeView(Path path, Class<V> cls, LinkOption... linkOptionArr);

    public abstract <A extends BasicFileAttributes> A readAttributes(Path path, Class<A> cls, LinkOption... linkOptionArr) throws IOException;

    public abstract Map<String, Object> readAttributes(Path path, String str, LinkOption... linkOptionArr) throws IOException;

    public abstract void setAttribute(Path path, String str, Object obj, LinkOption... linkOptionArr) throws IOException;

    private static Void checkPermission() {
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            securityManager.checkPermission(new RuntimePermission("fileSystemProvider"));
            return null;
        }
        return null;
    }

    private FileSystemProvider(Void r3) {
    }

    protected FileSystemProvider() {
        this(checkPermission());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static List<FileSystemProvider> loadInstalledProviders() {
        ArrayList arrayList = new ArrayList();
        Iterator it = ServiceLoader.load(FileSystemProvider.class, ClassLoader.getSystemClassLoader()).iterator();
        while (it.hasNext()) {
            FileSystemProvider fileSystemProvider = (FileSystemProvider) it.next();
            String scheme = fileSystemProvider.getScheme();
            if (!scheme.equalsIgnoreCase(DeploymentDescriptorParser.ATTR_FILE)) {
                boolean z2 = false;
                Iterator<E> it2 = arrayList.iterator();
                while (true) {
                    if (!it2.hasNext()) {
                        break;
                    }
                    if (((FileSystemProvider) it2.next()).getScheme().equalsIgnoreCase(scheme)) {
                        z2 = true;
                        break;
                    }
                }
                if (!z2) {
                    arrayList.add(fileSystemProvider);
                }
            }
        }
        return arrayList;
    }

    public static List<FileSystemProvider> installedProviders() {
        if (installedProviders == null) {
            FileSystemProvider fileSystemProviderProvider = FileSystems.getDefault().provider();
            synchronized (lock) {
                if (installedProviders == null) {
                    if (loadingProviders) {
                        throw new Error("Circular loading of installed providers detected");
                    }
                    loadingProviders = true;
                    List list = (List) AccessController.doPrivileged(new PrivilegedAction<List<FileSystemProvider>>() { // from class: java.nio.file.spi.FileSystemProvider.1
                        /* JADX WARN: Can't rename method to resolve collision */
                        @Override // java.security.PrivilegedAction
                        public List<FileSystemProvider> run() {
                            return FileSystemProvider.loadInstalledProviders();
                        }
                    });
                    list.add(0, fileSystemProviderProvider);
                    installedProviders = Collections.unmodifiableList(list);
                }
            }
        }
        return installedProviders;
    }

    public FileSystem newFileSystem(Path path, Map<String, ?> map) throws IOException {
        throw new UnsupportedOperationException();
    }

    public InputStream newInputStream(Path path, OpenOption... openOptionArr) throws IOException {
        if (openOptionArr.length > 0) {
            for (OpenOption openOption : openOptionArr) {
                if (openOption == StandardOpenOption.APPEND || openOption == StandardOpenOption.WRITE) {
                    throw new UnsupportedOperationException(PdfOps.SINGLE_QUOTE_TOKEN + ((Object) openOption) + "' not allowed");
                }
            }
        }
        return Channels.newInputStream(Files.newByteChannel(path, openOptionArr));
    }

    public OutputStream newOutputStream(Path path, OpenOption... openOptionArr) throws IOException {
        int length = openOptionArr.length;
        HashSet hashSet = new HashSet(length + 3);
        if (length == 0) {
            hashSet.add(StandardOpenOption.CREATE);
            hashSet.add(StandardOpenOption.TRUNCATE_EXISTING);
        } else {
            for (OpenOption openOption : openOptionArr) {
                if (openOption == StandardOpenOption.READ) {
                    throw new IllegalArgumentException("READ not allowed");
                }
                hashSet.add(openOption);
            }
        }
        hashSet.add(StandardOpenOption.WRITE);
        return Channels.newOutputStream(newByteChannel(path, hashSet, new FileAttribute[0]));
    }

    public FileChannel newFileChannel(Path path, Set<? extends OpenOption> set, FileAttribute<?>... fileAttributeArr) throws IOException {
        throw new UnsupportedOperationException();
    }

    public AsynchronousFileChannel newAsynchronousFileChannel(Path path, Set<? extends OpenOption> set, ExecutorService executorService, FileAttribute<?>... fileAttributeArr) throws IOException {
        throw new UnsupportedOperationException();
    }

    public void createSymbolicLink(Path path, Path path2, FileAttribute<?>... fileAttributeArr) throws IOException {
        throw new UnsupportedOperationException();
    }

    public void createLink(Path path, Path path2) throws IOException {
        throw new UnsupportedOperationException();
    }

    public boolean deleteIfExists(Path path) throws IOException {
        try {
            delete(path);
            return true;
        } catch (NoSuchFileException e2) {
            return false;
        }
    }

    public Path readSymbolicLink(Path path) throws IOException {
        throw new UnsupportedOperationException();
    }
}
