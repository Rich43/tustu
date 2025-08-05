package com.sun.nio.zipfs;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.channels.FileChannel;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.AccessMode;
import java.nio.file.CopyOption;
import java.nio.file.DirectoryStream;
import java.nio.file.FileStore;
import java.nio.file.FileSystem;
import java.nio.file.FileSystemAlreadyExistsException;
import java.nio.file.FileSystemNotFoundException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.ProviderMismatchException;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileAttribute;
import java.nio.file.attribute.FileAttributeView;
import java.nio.file.spi.FileSystemProvider;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.zip.ZipError;
import org.icepdf.core.util.PdfOps;

/* loaded from: zipfs.jar:com/sun/nio/zipfs/ZipFileSystemProvider.class */
public class ZipFileSystemProvider extends FileSystemProvider {
    private final Map<Path, ZipFileSystem> filesystems = new HashMap();

    @Override // java.nio.file.spi.FileSystemProvider
    public String getScheme() {
        return "jar";
    }

    protected Path uriToPath(URI uri) {
        String scheme = uri.getScheme();
        if (scheme == null || !scheme.equalsIgnoreCase(getScheme())) {
            throw new IllegalArgumentException("URI scheme is not '" + getScheme() + PdfOps.SINGLE_QUOTE_TOKEN);
        }
        try {
            String rawSchemeSpecificPart = uri.getRawSchemeSpecificPart();
            int iIndexOf = rawSchemeSpecificPart.indexOf("!/");
            if (iIndexOf != -1) {
                rawSchemeSpecificPart = rawSchemeSpecificPart.substring(0, iIndexOf);
            }
            return Paths.get(new URI(rawSchemeSpecificPart)).toAbsolutePath();
        } catch (URISyntaxException e2) {
            throw new IllegalArgumentException(e2.getMessage(), e2);
        }
    }

    private boolean ensureFile(Path path) {
        try {
            if (!Files.readAttributes(path, BasicFileAttributes.class, new LinkOption[0]).isRegularFile()) {
                throw new UnsupportedOperationException();
            }
            return true;
        } catch (IOException e2) {
            return false;
        }
    }

    @Override // java.nio.file.spi.FileSystemProvider
    public FileSystem newFileSystem(URI uri, Map<String, ?> map) throws IOException {
        ZipFileSystem zipFileSystem;
        Path pathUriToPath = uriToPath(uri);
        synchronized (this.filesystems) {
            Path realPath = null;
            if (ensureFile(pathUriToPath)) {
                realPath = pathUriToPath.toRealPath(new LinkOption[0]);
                if (this.filesystems.containsKey(realPath)) {
                    throw new FileSystemAlreadyExistsException();
                }
            }
            try {
                zipFileSystem = new ZipFileSystem(this, pathUriToPath, map);
                this.filesystems.put(realPath, zipFileSystem);
            } catch (ZipError e2) {
                String string = pathUriToPath.toString();
                if (string.endsWith(".zip") || string.endsWith(".jar")) {
                    throw e2;
                }
                throw new UnsupportedOperationException();
            }
        }
        return zipFileSystem;
    }

    @Override // java.nio.file.spi.FileSystemProvider
    public FileSystem newFileSystem(Path path, Map<String, ?> map) throws IOException {
        if (path.getFileSystem() != FileSystems.getDefault()) {
            throw new UnsupportedOperationException();
        }
        ensureFile(path);
        try {
            return new ZipFileSystem(this, path, map);
        } catch (ZipError e2) {
            String string = path.toString();
            if (string.endsWith(".zip") || string.endsWith(".jar")) {
                throw e2;
            }
            throw new UnsupportedOperationException();
        }
    }

    @Override // java.nio.file.spi.FileSystemProvider
    public Path getPath(URI uri) {
        String schemeSpecificPart = uri.getSchemeSpecificPart();
        int iIndexOf = schemeSpecificPart.indexOf("!/");
        if (iIndexOf == -1) {
            throw new IllegalArgumentException("URI: " + ((Object) uri) + " does not contain path info ex. jar:file:/c:/foo.zip!/BAR");
        }
        return getFileSystem(uri).getPath(schemeSpecificPart.substring(iIndexOf + 1), new String[0]);
    }

    @Override // java.nio.file.spi.FileSystemProvider
    public FileSystem getFileSystem(URI uri) {
        ZipFileSystem zipFileSystem;
        synchronized (this.filesystems) {
            ZipFileSystem zipFileSystem2 = null;
            try {
                zipFileSystem2 = this.filesystems.get(uriToPath(uri).toRealPath(new LinkOption[0]));
            } catch (IOException e2) {
            }
            if (zipFileSystem2 == null) {
                throw new FileSystemNotFoundException();
            }
            zipFileSystem = zipFileSystem2;
        }
        return zipFileSystem;
    }

    static final ZipPath toZipPath(Path path) {
        if (path == null) {
            throw new NullPointerException();
        }
        if (!(path instanceof ZipPath)) {
            throw new ProviderMismatchException();
        }
        return (ZipPath) path;
    }

    @Override // java.nio.file.spi.FileSystemProvider
    public void checkAccess(Path path, AccessMode... accessModeArr) throws IOException {
        toZipPath(path).checkAccess(accessModeArr);
    }

    @Override // java.nio.file.spi.FileSystemProvider
    public void copy(Path path, Path path2, CopyOption... copyOptionArr) throws IOException {
        toZipPath(path).copy(toZipPath(path2), copyOptionArr);
    }

    @Override // java.nio.file.spi.FileSystemProvider
    public void createDirectory(Path path, FileAttribute<?>... fileAttributeArr) throws IOException {
        toZipPath(path).createDirectory(fileAttributeArr);
    }

    @Override // java.nio.file.spi.FileSystemProvider
    public final void delete(Path path) throws IOException {
        toZipPath(path).delete();
    }

    @Override // java.nio.file.spi.FileSystemProvider
    public <V extends FileAttributeView> V getFileAttributeView(Path path, Class<V> cls, LinkOption... linkOptionArr) {
        return (V) ZipFileAttributeView.get(toZipPath(path), cls);
    }

    @Override // java.nio.file.spi.FileSystemProvider
    public FileStore getFileStore(Path path) throws IOException {
        return toZipPath(path).getFileStore();
    }

    @Override // java.nio.file.spi.FileSystemProvider
    public boolean isHidden(Path path) {
        return toZipPath(path).isHidden();
    }

    @Override // java.nio.file.spi.FileSystemProvider
    public boolean isSameFile(Path path, Path path2) throws IOException {
        return toZipPath(path).isSameFile(path2);
    }

    @Override // java.nio.file.spi.FileSystemProvider
    public void move(Path path, Path path2, CopyOption... copyOptionArr) throws IOException {
        toZipPath(path).move(toZipPath(path2), copyOptionArr);
    }

    @Override // java.nio.file.spi.FileSystemProvider
    public AsynchronousFileChannel newAsynchronousFileChannel(Path path, Set<? extends OpenOption> set, ExecutorService executorService, FileAttribute<?>... fileAttributeArr) throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override // java.nio.file.spi.FileSystemProvider
    public SeekableByteChannel newByteChannel(Path path, Set<? extends OpenOption> set, FileAttribute<?>... fileAttributeArr) throws IOException {
        return toZipPath(path).newByteChannel(set, fileAttributeArr);
    }

    @Override // java.nio.file.spi.FileSystemProvider
    public DirectoryStream<Path> newDirectoryStream(Path path, DirectoryStream.Filter<? super Path> filter) throws IOException {
        return toZipPath(path).newDirectoryStream(filter);
    }

    @Override // java.nio.file.spi.FileSystemProvider
    public FileChannel newFileChannel(Path path, Set<? extends OpenOption> set, FileAttribute<?>... fileAttributeArr) throws IOException {
        return toZipPath(path).newFileChannel(set, fileAttributeArr);
    }

    @Override // java.nio.file.spi.FileSystemProvider
    public InputStream newInputStream(Path path, OpenOption... openOptionArr) throws IOException {
        return toZipPath(path).newInputStream(openOptionArr);
    }

    @Override // java.nio.file.spi.FileSystemProvider
    public OutputStream newOutputStream(Path path, OpenOption... openOptionArr) throws IOException {
        return toZipPath(path).newOutputStream(openOptionArr);
    }

    @Override // java.nio.file.spi.FileSystemProvider
    public <A extends BasicFileAttributes> A readAttributes(Path path, Class<A> cls, LinkOption... linkOptionArr) throws IOException {
        if (cls == BasicFileAttributes.class || cls == ZipFileAttributes.class) {
            return toZipPath(path).getAttributes();
        }
        return null;
    }

    @Override // java.nio.file.spi.FileSystemProvider
    public Map<String, Object> readAttributes(Path path, String str, LinkOption... linkOptionArr) throws IOException {
        return toZipPath(path).readAttributes(str, linkOptionArr);
    }

    @Override // java.nio.file.spi.FileSystemProvider
    public Path readSymbolicLink(Path path) throws IOException {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override // java.nio.file.spi.FileSystemProvider
    public void setAttribute(Path path, String str, Object obj, LinkOption... linkOptionArr) throws IOException {
        toZipPath(path).setAttribute(str, obj, linkOptionArr);
    }

    void removeFileSystem(Path path, ZipFileSystem zipFileSystem) throws IOException {
        synchronized (this.filesystems) {
            Path realPath = path.toRealPath(new LinkOption[0]);
            if (this.filesystems.get(realPath) == zipFileSystem) {
                this.filesystems.remove(realPath);
            }
        }
    }
}
