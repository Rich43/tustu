package sun.management;

import java.io.File;
import java.io.IOException;
import java.security.AccessController;
import java.security.PrivilegedAction;

/* loaded from: rt.jar:sun/management/FileSystemImpl.class */
public class FileSystemImpl extends FileSystem {
    static native void init0();

    static native boolean isSecuritySupported0(String str) throws IOException;

    static native boolean isAccessUserOnly0(String str) throws IOException;

    @Override // sun.management.FileSystem
    public boolean supportsFileSecurity(File file) throws IOException {
        if (file.getAbsolutePath().indexOf(0) >= 0) {
            throw new IOException("illegal filename");
        }
        return isSecuritySupported0(file.getAbsolutePath());
    }

    @Override // sun.management.FileSystem
    public boolean isAccessUserOnly(File file) throws IOException {
        String absolutePath = file.getAbsolutePath();
        if (absolutePath.indexOf(0) >= 0) {
            throw new IOException("illegal filename");
        }
        if (!isSecuritySupported0(absolutePath)) {
            throw new UnsupportedOperationException("File system does not support file security");
        }
        return isAccessUserOnly0(absolutePath);
    }

    static {
        AccessController.doPrivileged(new PrivilegedAction<Void>() { // from class: sun.management.FileSystemImpl.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.security.PrivilegedAction
            public Void run() {
                System.loadLibrary("management");
                return null;
            }
        });
        init0();
    }
}
