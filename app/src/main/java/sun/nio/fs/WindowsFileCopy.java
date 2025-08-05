package sun.nio.fs;

import com.sun.nio.file.ExtendedCopyOption;
import java.io.IOException;
import java.nio.file.AtomicMoveNotSupportedException;
import java.nio.file.CopyOption;
import java.nio.file.DirectoryNotEmptyException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.LinkOption;
import java.nio.file.LinkPermission;
import java.nio.file.StandardCopyOption;
import java.util.concurrent.ExecutionException;
import sun.nio.fs.WindowsSecurity;

/* loaded from: rt.jar:sun/nio/fs/WindowsFileCopy.class */
class WindowsFileCopy {
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !WindowsFileCopy.class.desiredAssertionStatus();
    }

    private WindowsFileCopy() {
    }

    /* JADX WARN: Finally extract failed */
    static void copy(final WindowsPath windowsPath, final WindowsPath windowsPath2, CopyOption... copyOptionArr) throws IOException {
        long jOpenForReadAttributeAccess;
        boolean z2 = false;
        boolean z3 = false;
        boolean z4 = true;
        boolean z5 = false;
        for (CopyOption copyOption : copyOptionArr) {
            if (copyOption == StandardCopyOption.REPLACE_EXISTING) {
                z2 = true;
            } else if (copyOption == LinkOption.NOFOLLOW_LINKS) {
                z4 = false;
            } else if (copyOption == StandardCopyOption.COPY_ATTRIBUTES) {
                z3 = true;
            } else if (copyOption == ExtendedCopyOption.INTERRUPTIBLE) {
                z5 = true;
            } else {
                if (copyOption == null) {
                    throw new NullPointerException();
                }
                throw new UnsupportedOperationException("Unsupported copy option");
            }
        }
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            windowsPath.checkRead();
            windowsPath2.checkWrite();
        }
        WindowsFileAttributes attributes = null;
        WindowsFileAttributes attributes2 = null;
        long jOpenForReadAttributeAccess2 = 0;
        try {
            jOpenForReadAttributeAccess2 = windowsPath.openForReadAttributeAccess(z4);
        } catch (WindowsException e2) {
            e2.rethrowAsIOException(windowsPath);
        }
        try {
            try {
                attributes = WindowsFileAttributes.readAttributes(jOpenForReadAttributeAccess2);
            } catch (WindowsException e3) {
                e3.rethrowAsIOException(windowsPath);
            }
            try {
                jOpenForReadAttributeAccess = windowsPath2.openForReadAttributeAccess(false);
            } catch (WindowsException e4) {
            }
            try {
                attributes2 = WindowsFileAttributes.readAttributes(jOpenForReadAttributeAccess);
                if (!WindowsFileAttributes.isSameFile(attributes, attributes2)) {
                    if (!z2) {
                        throw new FileAlreadyExistsException(windowsPath2.getPathForExceptionMessage());
                    }
                    WindowsNativeDispatcher.CloseHandle(jOpenForReadAttributeAccess);
                    if (securityManager != null && attributes.isSymbolicLink()) {
                        securityManager.checkPermission(new LinkPermission("symbolic"));
                    }
                    final String strAsWin32Path = asWin32Path(windowsPath);
                    final String strAsWin32Path2 = asWin32Path(windowsPath2);
                    if (attributes2 != null) {
                        try {
                            if (attributes2.isDirectory() || attributes2.isDirectoryLink()) {
                                WindowsNativeDispatcher.RemoveDirectory(strAsWin32Path2);
                            } else {
                                WindowsNativeDispatcher.DeleteFile(strAsWin32Path2);
                            }
                        } catch (WindowsException e5) {
                            if (attributes2.isDirectory() && (e5.lastError() == 145 || e5.lastError() == 183)) {
                                throw new DirectoryNotEmptyException(windowsPath2.getPathForExceptionMessage());
                            }
                            e5.rethrowAsIOException(windowsPath2);
                        }
                    }
                    if (!attributes.isDirectory() && !attributes.isDirectoryLink()) {
                        final int i2 = (!windowsPath.getFileSystem().supportsLinks() || z4) ? 0 : 2048;
                        if (z5) {
                            try {
                                Cancellable.runInterruptibly(new Cancellable() { // from class: sun.nio.fs.WindowsFileCopy.1
                                    @Override // sun.nio.fs.Cancellable
                                    public int cancelValue() {
                                        return 1;
                                    }

                                    @Override // sun.nio.fs.Cancellable
                                    public void implRun() throws IOException {
                                        try {
                                            WindowsNativeDispatcher.CopyFileEx(strAsWin32Path, strAsWin32Path2, i2, addressToPollForCancel());
                                        } catch (WindowsException e6) {
                                            e6.rethrowAsIOException(windowsPath, windowsPath2);
                                        }
                                    }
                                });
                            } catch (ExecutionException e6) {
                                Throwable cause = e6.getCause();
                                if (cause instanceof IOException) {
                                    throw ((IOException) cause);
                                }
                                throw new IOException(cause);
                            }
                        } else {
                            try {
                                WindowsNativeDispatcher.CopyFileEx(strAsWin32Path, strAsWin32Path2, i2, 0L);
                            } catch (WindowsException e7) {
                                e7.rethrowAsIOException(windowsPath, windowsPath2);
                            }
                        }
                        if (z3) {
                            try {
                                copySecurityAttributes(windowsPath, windowsPath2, z4);
                                return;
                            } catch (IOException e8) {
                                return;
                            }
                        }
                        return;
                    }
                    try {
                        if (attributes.isDirectory()) {
                            WindowsNativeDispatcher.CreateDirectory(strAsWin32Path2, 0L);
                        } else {
                            WindowsNativeDispatcher.CreateSymbolicLink(strAsWin32Path2, WindowsPath.addPrefixIfNeeded(WindowsLinkSupport.readLink(windowsPath)), 1);
                        }
                    } catch (WindowsException e9) {
                        e9.rethrowAsIOException(windowsPath2);
                    }
                    if (z3) {
                        try {
                            WindowsFileAttributeViews.createDosView(windowsPath2, false).setAttributes(attributes);
                        } catch (IOException e10) {
                            if (attributes.isDirectory()) {
                                try {
                                    WindowsNativeDispatcher.RemoveDirectory(strAsWin32Path2);
                                } catch (WindowsException e11) {
                                }
                            }
                        }
                        try {
                            copySecurityAttributes(windowsPath, windowsPath2, z4);
                            return;
                        } catch (IOException e12) {
                            return;
                        }
                    }
                    return;
                }
                WindowsNativeDispatcher.CloseHandle(jOpenForReadAttributeAccess);
                WindowsNativeDispatcher.CloseHandle(jOpenForReadAttributeAccess2);
            } catch (Throwable th) {
                WindowsNativeDispatcher.CloseHandle(jOpenForReadAttributeAccess);
                throw th;
            }
        } finally {
            WindowsNativeDispatcher.CloseHandle(jOpenForReadAttributeAccess2);
        }
    }

    /* JADX WARN: Finally extract failed */
    static void move(WindowsPath windowsPath, WindowsPath windowsPath2, CopyOption... copyOptionArr) throws IOException {
        long jOpenForReadAttributeAccess;
        boolean z2 = false;
        boolean z3 = false;
        for (CopyOption copyOption : copyOptionArr) {
            if (copyOption == StandardCopyOption.ATOMIC_MOVE) {
                z2 = true;
            } else if (copyOption == StandardCopyOption.REPLACE_EXISTING) {
                z3 = true;
            } else if (copyOption != LinkOption.NOFOLLOW_LINKS) {
                if (copyOption != null) {
                    throw new UnsupportedOperationException("Unsupported copy option");
                }
                throw new NullPointerException();
            }
        }
        if (System.getSecurityManager() != null) {
            windowsPath.checkWrite();
            windowsPath2.checkWrite();
        }
        String strAsWin32Path = asWin32Path(windowsPath);
        String strAsWin32Path2 = asWin32Path(windowsPath2);
        if (z2) {
            try {
                WindowsNativeDispatcher.MoveFileEx(strAsWin32Path, strAsWin32Path2, 1);
                return;
            } catch (WindowsException e2) {
                if (e2.lastError() == 17) {
                    throw new AtomicMoveNotSupportedException(windowsPath.getPathForExceptionMessage(), windowsPath2.getPathForExceptionMessage(), e2.errorString());
                }
                e2.rethrowAsIOException(windowsPath, windowsPath2);
                return;
            }
        }
        WindowsFileAttributes attributes = null;
        WindowsFileAttributes attributes2 = null;
        long jOpenForReadAttributeAccess2 = 0;
        try {
            jOpenForReadAttributeAccess2 = windowsPath.openForReadAttributeAccess(false);
        } catch (WindowsException e3) {
            e3.rethrowAsIOException(windowsPath);
        }
        try {
            try {
                attributes = WindowsFileAttributes.readAttributes(jOpenForReadAttributeAccess2);
            } finally {
                WindowsNativeDispatcher.CloseHandle(jOpenForReadAttributeAccess2);
            }
        } catch (WindowsException e4) {
            e4.rethrowAsIOException(windowsPath);
        }
        try {
            jOpenForReadAttributeAccess = windowsPath2.openForReadAttributeAccess(false);
        } catch (WindowsException e5) {
        }
        try {
            attributes2 = WindowsFileAttributes.readAttributes(jOpenForReadAttributeAccess);
            if (!WindowsFileAttributes.isSameFile(attributes, attributes2)) {
                if (!z3) {
                    throw new FileAlreadyExistsException(windowsPath2.getPathForExceptionMessage());
                }
                WindowsNativeDispatcher.CloseHandle(jOpenForReadAttributeAccess);
                if (attributes2 != null) {
                    try {
                        if (attributes2.isDirectory() || attributes2.isDirectoryLink()) {
                            WindowsNativeDispatcher.RemoveDirectory(strAsWin32Path2);
                        } else {
                            WindowsNativeDispatcher.DeleteFile(strAsWin32Path2);
                        }
                    } catch (WindowsException e6) {
                        if (attributes2.isDirectory() && (e6.lastError() == 145 || e6.lastError() == 183)) {
                            throw new DirectoryNotEmptyException(windowsPath2.getPathForExceptionMessage());
                        }
                        e6.rethrowAsIOException(windowsPath2);
                    }
                }
                try {
                    WindowsNativeDispatcher.MoveFileEx(strAsWin32Path, strAsWin32Path2, 0);
                    return;
                } catch (WindowsException e7) {
                    if (e7.lastError() != 17) {
                        e7.rethrowAsIOException(windowsPath, windowsPath2);
                    }
                    if (!attributes.isDirectory() && !attributes.isDirectoryLink()) {
                        try {
                            WindowsNativeDispatcher.MoveFileEx(strAsWin32Path, strAsWin32Path2, 2);
                        } catch (WindowsException e8) {
                            e8.rethrowAsIOException(windowsPath, windowsPath2);
                        }
                        try {
                            copySecurityAttributes(windowsPath, windowsPath2, false);
                            return;
                        } catch (IOException e9) {
                            return;
                        }
                    }
                    if (!$assertionsDisabled && !attributes.isDirectory() && !attributes.isDirectoryLink()) {
                        throw new AssertionError();
                    }
                    try {
                        if (attributes.isDirectory()) {
                            WindowsNativeDispatcher.CreateDirectory(strAsWin32Path2, 0L);
                        } else {
                            WindowsNativeDispatcher.CreateSymbolicLink(strAsWin32Path2, WindowsPath.addPrefixIfNeeded(WindowsLinkSupport.readLink(windowsPath)), 1);
                        }
                    } catch (WindowsException e10) {
                        e10.rethrowAsIOException(windowsPath2);
                    }
                    try {
                        WindowsFileAttributeViews.createDosView(windowsPath2, false).setAttributes(attributes);
                        try {
                            copySecurityAttributes(windowsPath, windowsPath2, false);
                        } catch (IOException e11) {
                        }
                        try {
                            WindowsNativeDispatcher.RemoveDirectory(strAsWin32Path);
                            return;
                        } catch (WindowsException e12) {
                            try {
                                WindowsNativeDispatcher.RemoveDirectory(strAsWin32Path2);
                            } catch (WindowsException e13) {
                            }
                            if (e12.lastError() == 145 || e12.lastError() == 183) {
                                throw new DirectoryNotEmptyException(windowsPath2.getPathForExceptionMessage());
                            }
                            e12.rethrowAsIOException(windowsPath);
                            return;
                        }
                    } catch (IOException e14) {
                        try {
                            WindowsNativeDispatcher.RemoveDirectory(strAsWin32Path2);
                        } catch (WindowsException e15) {
                        }
                        throw e14;
                    }
                }
            }
            WindowsNativeDispatcher.CloseHandle(jOpenForReadAttributeAccess);
            WindowsNativeDispatcher.CloseHandle(jOpenForReadAttributeAccess2);
        } catch (Throwable th) {
            WindowsNativeDispatcher.CloseHandle(jOpenForReadAttributeAccess);
            throw th;
        }
    }

    private static String asWin32Path(WindowsPath windowsPath) throws IOException {
        try {
            return windowsPath.getPathForWin32Calls();
        } catch (WindowsException e2) {
            e2.rethrowAsIOException(windowsPath);
            return null;
        }
    }

    /* JADX WARN: Finally extract failed */
    private static void copySecurityAttributes(WindowsPath windowsPath, WindowsPath windowsPath2, boolean z2) throws IOException {
        String finalPath = WindowsLinkSupport.getFinalPath(windowsPath, z2);
        WindowsSecurity.Privilege privilegeEnablePrivilege = WindowsSecurity.enablePrivilege("SeRestorePrivilege");
        try {
            NativeBuffer fileSecurity = WindowsAclFileAttributeView.getFileSecurity(finalPath, 7);
            try {
                try {
                    WindowsNativeDispatcher.SetFileSecurity(windowsPath2.getPathForWin32Calls(), 7, fileSecurity.address());
                } catch (Throwable th) {
                    fileSecurity.release();
                    throw th;
                }
            } catch (WindowsException e2) {
                e2.rethrowAsIOException(windowsPath2);
            }
            fileSecurity.release();
        } finally {
            privilegeEnablePrivilege.drop();
        }
    }
}
