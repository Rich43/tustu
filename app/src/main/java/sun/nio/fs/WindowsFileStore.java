package sun.nio.fs;

import java.io.IOException;
import java.nio.file.FileStore;
import java.nio.file.FileSystemException;
import java.nio.file.attribute.AclFileAttributeView;
import java.nio.file.attribute.BasicFileAttributeView;
import java.nio.file.attribute.DosFileAttributeView;
import java.nio.file.attribute.FileAttributeView;
import java.nio.file.attribute.FileOwnerAttributeView;
import java.nio.file.attribute.FileStoreAttributeView;
import java.nio.file.attribute.UserDefinedFileAttributeView;
import org.icepdf.core.util.PdfOps;
import sun.nio.fs.WindowsNativeDispatcher;

/* loaded from: rt.jar:sun/nio/fs/WindowsFileStore.class */
class WindowsFileStore extends FileStore {
    private final String root;
    private final WindowsNativeDispatcher.VolumeInformation volInfo;
    private final int volType;
    private final String displayName;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !WindowsFileStore.class.desiredAssertionStatus();
    }

    private WindowsFileStore(String str) throws WindowsException {
        if (!$assertionsDisabled && str.charAt(str.length() - 1) != '\\') {
            throw new AssertionError();
        }
        this.root = str;
        this.volInfo = WindowsNativeDispatcher.GetVolumeInformation(str);
        this.volType = WindowsNativeDispatcher.GetDriveType(str);
        String strVolumeName = this.volInfo.volumeName();
        if (strVolumeName.length() > 0) {
            this.displayName = strVolumeName;
        } else {
            this.displayName = this.volType == 2 ? "Removable Disk" : "";
        }
    }

    static WindowsFileStore create(String str, boolean z2) throws IOException {
        try {
            return new WindowsFileStore(str);
        } catch (WindowsException e2) {
            if (z2 && e2.lastError() == 21) {
                return null;
            }
            e2.rethrowAsIOException(str);
            return null;
        }
    }

    static WindowsFileStore create(WindowsPath windowsPath) throws WindowsException, IOException {
        String pathForWin32Calls;
        try {
            if (windowsPath.getFileSystem().supportsLinks()) {
                pathForWin32Calls = WindowsLinkSupport.getFinalPath(windowsPath, true);
            } else {
                WindowsFileAttributes.get(windowsPath, true);
                pathForWin32Calls = windowsPath.getPathForWin32Calls();
            }
            try {
                return createFromPath(pathForWin32Calls);
            } catch (WindowsException e2) {
                if (e2.lastError() != 144) {
                    throw e2;
                }
                String finalPath = WindowsLinkSupport.getFinalPath(windowsPath);
                if (finalPath == null) {
                    throw new FileSystemException(windowsPath.getPathForExceptionMessage(), null, "Couldn't resolve path");
                }
                return createFromPath(finalPath);
            }
        } catch (WindowsException e3) {
            e3.rethrowAsIOException(windowsPath);
            return null;
        }
    }

    private static WindowsFileStore createFromPath(String str) throws WindowsException {
        return new WindowsFileStore(WindowsNativeDispatcher.GetVolumePathName(str));
    }

    WindowsNativeDispatcher.VolumeInformation volumeInformation() {
        return this.volInfo;
    }

    int volumeType() {
        return this.volType;
    }

    @Override // java.nio.file.FileStore
    public String name() {
        return this.volInfo.volumeName();
    }

    @Override // java.nio.file.FileStore
    public String type() {
        return this.volInfo.fileSystemName();
    }

    @Override // java.nio.file.FileStore
    public boolean isReadOnly() {
        return (this.volInfo.flags() & 524288) != 0;
    }

    private WindowsNativeDispatcher.DiskFreeSpace readDiskFreeSpace() throws IOException {
        try {
            return WindowsNativeDispatcher.GetDiskFreeSpaceEx(this.root);
        } catch (WindowsException e2) {
            e2.rethrowAsIOException(this.root);
            return null;
        }
    }

    @Override // java.nio.file.FileStore
    public long getTotalSpace() throws IOException {
        return readDiskFreeSpace().totalNumberOfBytes();
    }

    @Override // java.nio.file.FileStore
    public long getUsableSpace() throws IOException {
        return readDiskFreeSpace().freeBytesAvailable();
    }

    @Override // java.nio.file.FileStore
    public long getUnallocatedSpace() throws IOException {
        return readDiskFreeSpace().freeBytesAvailable();
    }

    @Override // java.nio.file.FileStore
    public <V extends FileStoreAttributeView> V getFileStoreAttributeView(Class<V> cls) {
        if (cls == null) {
            throw new NullPointerException();
        }
        return (V) null;
    }

    @Override // java.nio.file.FileStore
    public Object getAttribute(String str) throws IOException {
        if (str.equals("totalSpace")) {
            return Long.valueOf(getTotalSpace());
        }
        if (str.equals("usableSpace")) {
            return Long.valueOf(getUsableSpace());
        }
        if (str.equals("unallocatedSpace")) {
            return Long.valueOf(getUnallocatedSpace());
        }
        if (str.equals("volume:vsn")) {
            return Integer.valueOf(this.volInfo.volumeSerialNumber());
        }
        if (str.equals("volume:isRemovable")) {
            return Boolean.valueOf(this.volType == 2);
        }
        if (str.equals("volume:isCdrom")) {
            return Boolean.valueOf(this.volType == 5);
        }
        throw new UnsupportedOperationException(PdfOps.SINGLE_QUOTE_TOKEN + str + "' not recognized");
    }

    @Override // java.nio.file.FileStore
    public boolean supportsFileAttributeView(Class<? extends FileAttributeView> cls) {
        if (cls == null) {
            throw new NullPointerException();
        }
        if (cls == BasicFileAttributeView.class || cls == DosFileAttributeView.class) {
            return true;
        }
        return (cls == AclFileAttributeView.class || cls == FileOwnerAttributeView.class) ? (this.volInfo.flags() & 8) != 0 : cls == UserDefinedFileAttributeView.class && (this.volInfo.flags() & 262144) != 0;
    }

    @Override // java.nio.file.FileStore
    public boolean supportsFileAttributeView(String str) {
        if (str.equals("basic") || str.equals("dos")) {
            return true;
        }
        if (str.equals("acl")) {
            return supportsFileAttributeView(AclFileAttributeView.class);
        }
        if (str.equals("owner")) {
            return supportsFileAttributeView(FileOwnerAttributeView.class);
        }
        if (str.equals("user")) {
            return supportsFileAttributeView(UserDefinedFileAttributeView.class);
        }
        return false;
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof WindowsFileStore)) {
            return false;
        }
        return this.root.equals(((WindowsFileStore) obj).root);
    }

    public int hashCode() {
        return this.root.hashCode();
    }

    public String toString() {
        StringBuilder sb = new StringBuilder(this.displayName);
        if (sb.length() > 0) {
            sb.append(" ");
        }
        sb.append("(");
        sb.append(this.root.subSequence(0, this.root.length() - 1));
        sb.append(")");
        return sb.toString();
    }
}
