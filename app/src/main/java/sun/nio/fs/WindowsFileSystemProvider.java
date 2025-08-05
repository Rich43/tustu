package sun.nio.fs;

import com.sun.xml.internal.ws.transport.http.DeploymentDescriptorParser;
import java.io.FilePermission;
import java.io.IOException;
import java.net.URI;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.channels.FileChannel;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.AccessDeniedException;
import java.nio.file.AccessMode;
import java.nio.file.CopyOption;
import java.nio.file.DirectoryNotEmptyException;
import java.nio.file.DirectoryStream;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.FileStore;
import java.nio.file.FileSystem;
import java.nio.file.FileSystemAlreadyExistsException;
import java.nio.file.LinkOption;
import java.nio.file.LinkPermission;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.ProviderMismatchException;
import java.nio.file.attribute.AclFileAttributeView;
import java.nio.file.attribute.BasicFileAttributeView;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.DosFileAttributeView;
import java.nio.file.attribute.DosFileAttributes;
import java.nio.file.attribute.FileAttribute;
import java.nio.file.attribute.FileAttributeView;
import java.nio.file.attribute.FileOwnerAttributeView;
import java.nio.file.attribute.UserDefinedFileAttributeView;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import sun.misc.Unsafe;
import sun.nio.ch.ThreadPool;
import sun.security.util.SecurityConstants;

/* loaded from: rt.jar:sun/nio/fs/WindowsFileSystemProvider.class */
public class WindowsFileSystemProvider extends AbstractFileSystemProvider {
    private static final Unsafe unsafe = Unsafe.getUnsafe();
    private static final String USER_DIR = "user.dir";
    private final WindowsFileSystem theFileSystem = new WindowsFileSystem(this, System.getProperty(USER_DIR));

    @Override // java.nio.file.spi.FileSystemProvider
    public String getScheme() {
        return DeploymentDescriptorParser.ATTR_FILE;
    }

    private void checkUri(URI uri) {
        if (!uri.getScheme().equalsIgnoreCase(getScheme())) {
            throw new IllegalArgumentException("URI does not match this provider");
        }
        if (uri.getAuthority() != null) {
            throw new IllegalArgumentException("Authority component present");
        }
        if (uri.getPath() == null) {
            throw new IllegalArgumentException("Path component is undefined");
        }
        if (!uri.getPath().equals("/")) {
            throw new IllegalArgumentException("Path component should be '/'");
        }
        if (uri.getQuery() != null) {
            throw new IllegalArgumentException("Query component present");
        }
        if (uri.getFragment() != null) {
            throw new IllegalArgumentException("Fragment component present");
        }
    }

    @Override // java.nio.file.spi.FileSystemProvider
    public FileSystem newFileSystem(URI uri, Map<String, ?> map) throws IOException {
        checkUri(uri);
        throw new FileSystemAlreadyExistsException();
    }

    @Override // java.nio.file.spi.FileSystemProvider
    public final FileSystem getFileSystem(URI uri) {
        checkUri(uri);
        return this.theFileSystem;
    }

    @Override // java.nio.file.spi.FileSystemProvider
    public Path getPath(URI uri) {
        return WindowsUriSupport.fromUri(this.theFileSystem, uri);
    }

    @Override // java.nio.file.spi.FileSystemProvider
    public FileChannel newFileChannel(Path path, Set<? extends OpenOption> set, FileAttribute<?>... fileAttributeArr) throws IOException {
        if (path == null) {
            throw new NullPointerException();
        }
        if (!(path instanceof WindowsPath)) {
            throw new ProviderMismatchException();
        }
        WindowsPath windowsPath = (WindowsPath) path;
        WindowsSecurityDescriptor windowsSecurityDescriptorFromAttribute = WindowsSecurityDescriptor.fromAttribute(fileAttributeArr);
        try {
            try {
                FileChannel fileChannelNewFileChannel = WindowsChannelFactory.newFileChannel(windowsPath.getPathForWin32Calls(), windowsPath.getPathForPermissionCheck(), set, windowsSecurityDescriptorFromAttribute.address());
                if (windowsSecurityDescriptorFromAttribute != null) {
                    windowsSecurityDescriptorFromAttribute.release();
                }
                return fileChannelNewFileChannel;
            } catch (WindowsException e2) {
                e2.rethrowAsIOException(windowsPath);
                if (windowsSecurityDescriptorFromAttribute != null) {
                    windowsSecurityDescriptorFromAttribute.release();
                }
                return null;
            }
        } catch (Throwable th) {
            if (windowsSecurityDescriptorFromAttribute != null) {
                windowsSecurityDescriptorFromAttribute.release();
            }
            throw th;
        }
    }

    @Override // java.nio.file.spi.FileSystemProvider
    public AsynchronousFileChannel newAsynchronousFileChannel(Path path, Set<? extends OpenOption> set, ExecutorService executorService, FileAttribute<?>... fileAttributeArr) throws IOException {
        if (path == null) {
            throw new NullPointerException();
        }
        if (!(path instanceof WindowsPath)) {
            throw new ProviderMismatchException();
        }
        WindowsPath windowsPath = (WindowsPath) path;
        ThreadPool threadPoolWrap = executorService == null ? null : ThreadPool.wrap(executorService, 0);
        WindowsSecurityDescriptor windowsSecurityDescriptorFromAttribute = WindowsSecurityDescriptor.fromAttribute(fileAttributeArr);
        try {
            try {
                AsynchronousFileChannel asynchronousFileChannelNewAsynchronousFileChannel = WindowsChannelFactory.newAsynchronousFileChannel(windowsPath.getPathForWin32Calls(), windowsPath.getPathForPermissionCheck(), set, windowsSecurityDescriptorFromAttribute.address(), threadPoolWrap);
                if (windowsSecurityDescriptorFromAttribute != null) {
                    windowsSecurityDescriptorFromAttribute.release();
                }
                return asynchronousFileChannelNewAsynchronousFileChannel;
            } catch (WindowsException e2) {
                e2.rethrowAsIOException(windowsPath);
                if (windowsSecurityDescriptorFromAttribute != null) {
                    windowsSecurityDescriptorFromAttribute.release();
                }
                return null;
            }
        } catch (Throwable th) {
            if (windowsSecurityDescriptorFromAttribute != null) {
                windowsSecurityDescriptorFromAttribute.release();
            }
            throw th;
        }
    }

    @Override // java.nio.file.spi.FileSystemProvider
    public <V extends FileAttributeView> V getFileAttributeView(Path path, Class<V> cls, LinkOption... linkOptionArr) {
        WindowsPath windowsPath = WindowsPath.toWindowsPath(path);
        if (cls == null) {
            throw new NullPointerException();
        }
        boolean zFollowLinks = Util.followLinks(linkOptionArr);
        if (cls == BasicFileAttributeView.class) {
            return WindowsFileAttributeViews.createBasicView(windowsPath, zFollowLinks);
        }
        if (cls == DosFileAttributeView.class) {
            return WindowsFileAttributeViews.createDosView(windowsPath, zFollowLinks);
        }
        if (cls == AclFileAttributeView.class) {
            return new WindowsAclFileAttributeView(windowsPath, zFollowLinks);
        }
        if (cls == FileOwnerAttributeView.class) {
            return new FileOwnerAttributeViewImpl(new WindowsAclFileAttributeView(windowsPath, zFollowLinks));
        }
        if (cls == UserDefinedFileAttributeView.class) {
            return new WindowsUserDefinedFileAttributeView(windowsPath, zFollowLinks);
        }
        return (V) null;
    }

    @Override // java.nio.file.spi.FileSystemProvider
    public <A extends BasicFileAttributes> A readAttributes(Path path, Class<A> cls, LinkOption... linkOptionArr) throws IOException {
        Object obj;
        if (cls == BasicFileAttributes.class) {
            obj = BasicFileAttributeView.class;
        } else if (cls == DosFileAttributes.class) {
            obj = DosFileAttributeView.class;
        } else {
            if (cls == null) {
                throw new NullPointerException();
            }
            throw new UnsupportedOperationException();
        }
        return (A) ((BasicFileAttributeView) getFileAttributeView(path, (Class) obj, linkOptionArr)).readAttributes();
    }

    @Override // sun.nio.fs.AbstractFileSystemProvider
    public DynamicFileAttributeView getFileAttributeView(Path path, String str, LinkOption... linkOptionArr) {
        WindowsPath windowsPath = WindowsPath.toWindowsPath(path);
        boolean zFollowLinks = Util.followLinks(linkOptionArr);
        if (str.equals("basic")) {
            return WindowsFileAttributeViews.createBasicView(windowsPath, zFollowLinks);
        }
        if (str.equals("dos")) {
            return WindowsFileAttributeViews.createDosView(windowsPath, zFollowLinks);
        }
        if (str.equals("acl")) {
            return new WindowsAclFileAttributeView(windowsPath, zFollowLinks);
        }
        if (str.equals("owner")) {
            return new FileOwnerAttributeViewImpl(new WindowsAclFileAttributeView(windowsPath, zFollowLinks));
        }
        if (str.equals("user")) {
            return new WindowsUserDefinedFileAttributeView(windowsPath, zFollowLinks);
        }
        return null;
    }

    @Override // java.nio.file.spi.FileSystemProvider
    public SeekableByteChannel newByteChannel(Path path, Set<? extends OpenOption> set, FileAttribute<?>... fileAttributeArr) throws IOException {
        WindowsPath windowsPath = WindowsPath.toWindowsPath(path);
        WindowsSecurityDescriptor windowsSecurityDescriptorFromAttribute = WindowsSecurityDescriptor.fromAttribute(fileAttributeArr);
        try {
            try {
                FileChannel fileChannelNewFileChannel = WindowsChannelFactory.newFileChannel(windowsPath.getPathForWin32Calls(), windowsPath.getPathForPermissionCheck(), set, windowsSecurityDescriptorFromAttribute.address());
                windowsSecurityDescriptorFromAttribute.release();
                return fileChannelNewFileChannel;
            } catch (WindowsException e2) {
                e2.rethrowAsIOException(windowsPath);
                windowsSecurityDescriptorFromAttribute.release();
                return null;
            }
        } catch (Throwable th) {
            windowsSecurityDescriptorFromAttribute.release();
            throw th;
        }
    }

    @Override // sun.nio.fs.AbstractFileSystemProvider
    boolean implDelete(Path path, boolean z2) throws IOException {
        WindowsPath windowsPath = WindowsPath.toWindowsPath(path);
        windowsPath.checkDelete();
        BasicFileAttributes basicFileAttributes = null;
        try {
            WindowsFileAttributes windowsFileAttributes = WindowsFileAttributes.get(windowsPath, false);
            if (windowsFileAttributes.isDirectory() || windowsFileAttributes.isDirectoryLink()) {
                WindowsNativeDispatcher.RemoveDirectory(windowsPath.getPathForWin32Calls());
                return true;
            }
            WindowsNativeDispatcher.DeleteFile(windowsPath.getPathForWin32Calls());
            return true;
        } catch (WindowsException e2) {
            if (!z2 && (e2.lastError() == 2 || e2.lastError() == 3)) {
                return false;
            }
            if (0 != 0 && basicFileAttributes.isDirectory() && (e2.lastError() == 145 || e2.lastError() == 183)) {
                throw new DirectoryNotEmptyException(windowsPath.getPathForExceptionMessage());
            }
            e2.rethrowAsIOException(windowsPath);
            return false;
        }
    }

    @Override // java.nio.file.spi.FileSystemProvider
    public void copy(Path path, Path path2, CopyOption... copyOptionArr) throws IOException {
        WindowsFileCopy.copy(WindowsPath.toWindowsPath(path), WindowsPath.toWindowsPath(path2), copyOptionArr);
    }

    @Override // java.nio.file.spi.FileSystemProvider
    public void move(Path path, Path path2, CopyOption... copyOptionArr) throws IOException {
        WindowsFileCopy.move(WindowsPath.toWindowsPath(path), WindowsPath.toWindowsPath(path2), copyOptionArr);
    }

    private static boolean hasDesiredAccess(WindowsPath windowsPath, int i2) throws IOException {
        boolean zCheckAccessMask = false;
        NativeBuffer fileSecurity = WindowsAclFileAttributeView.getFileSecurity(WindowsLinkSupport.getFinalPath(windowsPath, true), 7);
        try {
            try {
                zCheckAccessMask = WindowsSecurity.checkAccessMask(fileSecurity.address(), i2, WindowsConstants.FILE_GENERIC_READ, WindowsConstants.FILE_GENERIC_WRITE, WindowsConstants.FILE_GENERIC_EXECUTE, WindowsConstants.FILE_ALL_ACCESS);
                fileSecurity.release();
            } catch (WindowsException e2) {
                e2.rethrowAsIOException(windowsPath);
                fileSecurity.release();
            }
            return zCheckAccessMask;
        } catch (Throwable th) {
            fileSecurity.release();
            throw th;
        }
    }

    private void checkReadAccess(WindowsPath windowsPath) throws IOException {
        try {
            WindowsChannelFactory.newFileChannel(windowsPath.getPathForWin32Calls(), windowsPath.getPathForPermissionCheck(), Collections.emptySet(), 0L).close();
        } catch (WindowsException e2) {
            try {
                new WindowsDirectoryStream(windowsPath, null).close();
            } catch (IOException e3) {
                e2.rethrowAsIOException(windowsPath);
            }
        }
    }

    @Override // java.nio.file.spi.FileSystemProvider
    public void checkAccess(Path path, AccessMode... accessModeArr) throws IOException {
        WindowsFileAttributes windowsFileAttributes;
        WindowsPath windowsPath = WindowsPath.toWindowsPath(path);
        boolean z2 = false;
        boolean z3 = false;
        boolean z4 = false;
        int length = accessModeArr.length;
        for (int i2 = 0; i2 < length; i2++) {
            switch (accessModeArr[i2]) {
                case READ:
                    z2 = true;
                    break;
                case WRITE:
                    z3 = true;
                    break;
                case EXECUTE:
                    z4 = true;
                    break;
                default:
                    throw new AssertionError((Object) "Should not get here");
            }
        }
        if (!z3 && !z4) {
            checkReadAccess(windowsPath);
            return;
        }
        int i3 = 0;
        if (z2) {
            windowsPath.checkRead();
            i3 = 0 | 1;
        }
        if (z3) {
            windowsPath.checkWrite();
            i3 |= 2;
        }
        if (z4) {
            SecurityManager securityManager = System.getSecurityManager();
            if (securityManager != null) {
                securityManager.checkExec(windowsPath.getPathForPermissionCheck());
            }
            i3 |= 32;
        }
        if (!hasDesiredAccess(windowsPath, i3)) {
            throw new AccessDeniedException(windowsPath.getPathForExceptionMessage(), null, "Permissions does not allow requested access");
        }
        if (z3) {
            try {
                windowsFileAttributes = WindowsFileAttributes.get(windowsPath, true);
            } catch (WindowsException e2) {
                e2.rethrowAsIOException(windowsPath);
            }
            if (!windowsFileAttributes.isDirectory() && windowsFileAttributes.isReadOnly()) {
                throw new AccessDeniedException(windowsPath.getPathForExceptionMessage(), null, "DOS readonly attribute is set");
            }
            if (WindowsFileStore.create(windowsPath).isReadOnly()) {
                throw new AccessDeniedException(windowsPath.getPathForExceptionMessage(), null, "Read-only file system");
            }
        }
    }

    @Override // java.nio.file.spi.FileSystemProvider
    public boolean isSameFile(Path path, Path path2) throws IOException {
        long jOpenForReadAttributeAccess;
        WindowsPath windowsPath = WindowsPath.toWindowsPath(path);
        if (windowsPath.equals(path2)) {
            return true;
        }
        if (path2 == null) {
            throw new NullPointerException();
        }
        if (!(path2 instanceof WindowsPath)) {
            return false;
        }
        WindowsPath windowsPath2 = (WindowsPath) path2;
        windowsPath.checkRead();
        windowsPath2.checkRead();
        long jOpenForReadAttributeAccess2 = 0;
        try {
            jOpenForReadAttributeAccess2 = windowsPath.openForReadAttributeAccess(true);
        } catch (WindowsException e2) {
            e2.rethrowAsIOException(windowsPath);
        }
        WindowsFileAttributes attributes = null;
        try {
            try {
                attributes = WindowsFileAttributes.readAttributes(jOpenForReadAttributeAccess);
            } catch (WindowsException e3) {
                e3.rethrowAsIOException(windowsPath);
            }
            jOpenForReadAttributeAccess = 0;
            try {
                jOpenForReadAttributeAccess = windowsPath2.openForReadAttributeAccess(true);
            } catch (WindowsException e4) {
                e4.rethrowAsIOException(windowsPath2);
            }
            WindowsFileAttributes attributes2 = null;
            try {
                try {
                    attributes2 = WindowsFileAttributes.readAttributes(jOpenForReadAttributeAccess);
                } catch (WindowsException e5) {
                    e5.rethrowAsIOException(windowsPath2);
                }
                boolean zIsSameFile = WindowsFileAttributes.isSameFile(attributes, attributes2);
                WindowsNativeDispatcher.CloseHandle(jOpenForReadAttributeAccess);
                return zIsSameFile;
            } finally {
                WindowsNativeDispatcher.CloseHandle(jOpenForReadAttributeAccess);
            }
        } catch (Throwable th) {
            WindowsNativeDispatcher.CloseHandle(jOpenForReadAttributeAccess);
            throw th;
        }
    }

    @Override // java.nio.file.spi.FileSystemProvider
    public boolean isHidden(Path path) throws IOException {
        WindowsPath windowsPath = WindowsPath.toWindowsPath(path);
        windowsPath.checkRead();
        WindowsFileAttributes windowsFileAttributes = null;
        try {
            windowsFileAttributes = WindowsFileAttributes.get(windowsPath, true);
        } catch (WindowsException e2) {
            e2.rethrowAsIOException(windowsPath);
        }
        if (windowsFileAttributes.isDirectory()) {
            return false;
        }
        return windowsFileAttributes.isHidden();
    }

    @Override // java.nio.file.spi.FileSystemProvider
    public FileStore getFileStore(Path path) throws IOException {
        WindowsPath windowsPath = WindowsPath.toWindowsPath(path);
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            securityManager.checkPermission(new RuntimePermission("getFileStoreAttributes"));
            windowsPath.checkRead();
        }
        return WindowsFileStore.create(windowsPath);
    }

    @Override // java.nio.file.spi.FileSystemProvider
    public void createDirectory(Path path, FileAttribute<?>... fileAttributeArr) throws IOException {
        WindowsPath windowsPath = WindowsPath.toWindowsPath(path);
        windowsPath.checkWrite();
        WindowsSecurityDescriptor windowsSecurityDescriptorFromAttribute = WindowsSecurityDescriptor.fromAttribute(fileAttributeArr);
        try {
            try {
                WindowsNativeDispatcher.CreateDirectory(windowsPath.getPathForWin32Calls(), windowsSecurityDescriptorFromAttribute.address());
                windowsSecurityDescriptorFromAttribute.release();
            } catch (WindowsException e2) {
                if (e2.lastError() == 5) {
                    try {
                        if (WindowsFileAttributes.get(windowsPath, false).isDirectory()) {
                            throw new FileAlreadyExistsException(windowsPath.toString());
                        }
                    } catch (WindowsException e3) {
                    }
                }
                e2.rethrowAsIOException(windowsPath);
                windowsSecurityDescriptorFromAttribute.release();
            }
        } catch (Throwable th) {
            windowsSecurityDescriptorFromAttribute.release();
            throw th;
        }
    }

    @Override // java.nio.file.spi.FileSystemProvider
    public DirectoryStream<Path> newDirectoryStream(Path path, DirectoryStream.Filter<? super Path> filter) throws IOException {
        WindowsPath windowsPath = WindowsPath.toWindowsPath(path);
        windowsPath.checkRead();
        if (filter == null) {
            throw new NullPointerException();
        }
        return new WindowsDirectoryStream(windowsPath, filter);
    }

    @Override // java.nio.file.spi.FileSystemProvider
    public void createSymbolicLink(Path path, Path path2, FileAttribute<?>... fileAttributeArr) throws IOException {
        WindowsPath windowsPathResolve;
        WindowsPath windowsPath = WindowsPath.toWindowsPath(path);
        WindowsPath windowsPath2 = WindowsPath.toWindowsPath(path2);
        if (!windowsPath.getFileSystem().supportsLinks()) {
            throw new UnsupportedOperationException("Symbolic links not supported on this operating system");
        }
        if (fileAttributeArr.length > 0) {
            WindowsSecurityDescriptor.fromAttribute(fileAttributeArr);
            throw new UnsupportedOperationException("Initial file attributesnot supported when creating symbolic link");
        }
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            securityManager.checkPermission(new LinkPermission("symbolic"));
            windowsPath.checkWrite();
        }
        if (windowsPath2.type() == WindowsPathType.DRIVE_RELATIVE) {
            throw new IOException("Cannot create symbolic link to working directory relative target");
        }
        if (windowsPath2.type() == WindowsPathType.RELATIVE) {
            WindowsPath parent = windowsPath.getParent();
            windowsPathResolve = parent == null ? windowsPath2 : parent.resolve((Path) windowsPath2);
        } else {
            windowsPathResolve = windowsPath.resolve((Path) windowsPath2);
        }
        int i2 = 0;
        try {
            WindowsFileAttributes windowsFileAttributes = WindowsFileAttributes.get(windowsPathResolve, false);
            if (windowsFileAttributes.isDirectory() || windowsFileAttributes.isDirectoryLink()) {
                i2 = 0 | 1;
            }
        } catch (WindowsException e2) {
        }
        try {
            WindowsNativeDispatcher.CreateSymbolicLink(windowsPath.getPathForWin32Calls(), WindowsPath.addPrefixIfNeeded(windowsPath2.toString()), i2);
        } catch (WindowsException e3) {
            if (e3.lastError() == 4392) {
                e3.rethrowAsIOException(windowsPath, windowsPath2);
            } else {
                e3.rethrowAsIOException(windowsPath);
            }
        }
    }

    @Override // java.nio.file.spi.FileSystemProvider
    public void createLink(Path path, Path path2) throws IOException {
        WindowsPath windowsPath = WindowsPath.toWindowsPath(path);
        WindowsPath windowsPath2 = WindowsPath.toWindowsPath(path2);
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            securityManager.checkPermission(new LinkPermission("hard"));
            windowsPath.checkWrite();
            windowsPath2.checkWrite();
        }
        try {
            WindowsNativeDispatcher.CreateHardLink(windowsPath.getPathForWin32Calls(), windowsPath2.getPathForWin32Calls());
        } catch (WindowsException e2) {
            e2.rethrowAsIOException(windowsPath, windowsPath2);
        }
    }

    @Override // java.nio.file.spi.FileSystemProvider
    public Path readSymbolicLink(Path path) throws IOException {
        WindowsPath windowsPath = WindowsPath.toWindowsPath(path);
        WindowsFileSystem fileSystem = windowsPath.getFileSystem();
        if (!fileSystem.supportsLinks()) {
            throw new UnsupportedOperationException("symbolic links not supported");
        }
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            securityManager.checkPermission(new FilePermission(windowsPath.getPathForPermissionCheck(), SecurityConstants.FILE_READLINK_ACTION));
        }
        return WindowsPath.createFromNormalizedPath(fileSystem, WindowsLinkSupport.readLink(windowsPath));
    }
}
