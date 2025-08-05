package sun.nio.fs;

import java.io.IOException;
import java.nio.file.FileSystemException;
import java.nio.file.NotLinkException;
import java.nio.file.Path;
import java.security.AccessController;
import java.security.PrivilegedAction;
import javafx.fxml.FXMLLoader;
import sun.misc.Unsafe;

/* loaded from: rt.jar:sun/nio/fs/WindowsLinkSupport.class */
class WindowsLinkSupport {
    private static final Unsafe unsafe;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !WindowsLinkSupport.class.desiredAssertionStatus();
        unsafe = Unsafe.getUnsafe();
    }

    private WindowsLinkSupport() {
    }

    static String readLink(WindowsPath windowsPath) throws IOException {
        long jOpenForReadAttributeAccess = 0;
        try {
            jOpenForReadAttributeAccess = windowsPath.openForReadAttributeAccess(false);
        } catch (WindowsException e2) {
            e2.rethrowAsIOException(windowsPath);
        }
        try {
            String linkImpl = readLinkImpl(jOpenForReadAttributeAccess);
            WindowsNativeDispatcher.CloseHandle(jOpenForReadAttributeAccess);
            return linkImpl;
        } catch (Throwable th) {
            WindowsNativeDispatcher.CloseHandle(jOpenForReadAttributeAccess);
            throw th;
        }
    }

    static String getFinalPath(WindowsPath windowsPath) throws IOException {
        long jOpenForReadAttributeAccess = 0;
        try {
            jOpenForReadAttributeAccess = windowsPath.openForReadAttributeAccess(true);
        } catch (WindowsException e2) {
            e2.rethrowAsIOException(windowsPath);
        }
        try {
            try {
                String strStripPrefix = stripPrefix(WindowsNativeDispatcher.GetFinalPathNameByHandle(jOpenForReadAttributeAccess));
                WindowsNativeDispatcher.CloseHandle(jOpenForReadAttributeAccess);
                return strStripPrefix;
            } catch (WindowsException e3) {
                if (e3.lastError() != 124) {
                    e3.rethrowAsIOException(windowsPath);
                }
                WindowsNativeDispatcher.CloseHandle(jOpenForReadAttributeAccess);
                return null;
            }
        } catch (Throwable th) {
            WindowsNativeDispatcher.CloseHandle(jOpenForReadAttributeAccess);
            throw th;
        }
    }

    static String getFinalPath(WindowsPath windowsPath, boolean z2) throws IOException {
        WindowsFileSystem fileSystem = windowsPath.getFileSystem();
        if (z2) {
            try {
            } catch (WindowsException e2) {
                e2.rethrowAsIOException(windowsPath);
            }
            if (fileSystem.supportsLinks()) {
                if (!WindowsFileAttributes.get(windowsPath, false).isSymbolicLink()) {
                    return windowsPath.getPathForWin32Calls();
                }
                String finalPath = getFinalPath(windowsPath);
                if (finalPath != null) {
                    return finalPath;
                }
                WindowsPath windowsPathResolve = windowsPath;
                int i2 = 0;
                do {
                    try {
                        if (!WindowsFileAttributes.get(windowsPathResolve, false).isSymbolicLink()) {
                            return windowsPathResolve.getPathForWin32Calls();
                        }
                    } catch (WindowsException e3) {
                        e3.rethrowAsIOException(windowsPathResolve);
                    }
                    WindowsPath windowsPathCreateFromNormalizedPath = WindowsPath.createFromNormalizedPath(fileSystem, readLink(windowsPathResolve));
                    WindowsPath parent = windowsPathResolve.getParent();
                    if (parent == null) {
                        final WindowsPath windowsPath2 = windowsPathResolve;
                        parent = ((WindowsPath) AccessController.doPrivileged(new PrivilegedAction<WindowsPath>() { // from class: sun.nio.fs.WindowsLinkSupport.1
                            /* JADX WARN: Can't rename method to resolve collision */
                            @Override // java.security.PrivilegedAction
                            /* renamed from: run */
                            public WindowsPath run2() {
                                return windowsPath2.toAbsolutePath();
                            }
                        })).getParent();
                    }
                    windowsPathResolve = parent.resolve((Path) windowsPathCreateFromNormalizedPath);
                    i2++;
                } while (i2 < 32);
                throw new FileSystemException(windowsPath.getPathForExceptionMessage(), null, "Too many links");
            }
        }
        return windowsPath.getPathForWin32Calls();
    }

    /* JADX WARN: Code restructure failed: missing block: B:71:0x01d0, code lost:
    
        return r19;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    static java.lang.String getRealPath(sun.nio.fs.WindowsPath r6, boolean r7) throws java.io.IOException {
        /*
            Method dump skipped, instructions count: 516
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: sun.nio.fs.WindowsLinkSupport.getRealPath(sun.nio.fs.WindowsPath, boolean):java.lang.String");
    }

    private static String readLinkImpl(long j2) throws IOException {
        NativeBuffer nativeBuffer = NativeBuffers.getNativeBuffer(16384);
        try {
            try {
                WindowsNativeDispatcher.DeviceIoControlGetReparsePoint(j2, nativeBuffer.address(), 16384);
            } catch (WindowsException e2) {
                if (e2.lastError() == 4390) {
                    throw new NotLinkException(null, null, e2.errorString());
                }
                e2.rethrowAsIOException((String) null);
            }
            if (((int) unsafe.getLong(nativeBuffer.address() + 0)) != -1610612724) {
                throw new NotLinkException(null, null, "Reparse point is not a symbolic link");
            }
            short s2 = unsafe.getShort(nativeBuffer.address() + 8);
            short s3 = unsafe.getShort(nativeBuffer.address() + 10);
            if (s3 % 2 != 0) {
                throw new FileSystemException(null, null, "Symbolic link corrupted");
            }
            char[] cArr = new char[s3 / 2];
            unsafe.copyMemory(null, nativeBuffer.address() + 20 + s2, cArr, Unsafe.ARRAY_CHAR_BASE_OFFSET, s3);
            String strStripPrefix = stripPrefix(new String(cArr));
            if (strStripPrefix.length() == 0) {
                throw new IOException("Symbolic link target is invalid");
            }
            return strStripPrefix;
        } finally {
            nativeBuffer.release();
        }
    }

    private static WindowsPath resolveAllLinks(WindowsPath windowsPath) throws IOException {
        if (!$assertionsDisabled && !windowsPath.isAbsolute()) {
            throw new AssertionError();
        }
        WindowsFileSystem fileSystem = windowsPath.getFileSystem();
        int i2 = 0;
        int i3 = 0;
        while (i3 < windowsPath.getNameCount()) {
            WindowsPath windowsPathResolve = windowsPath.getRoot().resolve((Path) windowsPath.subpath(0, i3 + 1));
            WindowsFileAttributes windowsFileAttributes = null;
            try {
                windowsFileAttributes = WindowsFileAttributes.get(windowsPathResolve, false);
            } catch (WindowsException e2) {
                e2.rethrowAsIOException(windowsPathResolve);
            }
            if (windowsFileAttributes.isSymbolicLink()) {
                i2++;
                if (i2 > 32) {
                    throw new IOException("Too many links");
                }
                WindowsPath windowsPathCreateFromNormalizedPath = WindowsPath.createFromNormalizedPath(fileSystem, readLink(windowsPathResolve));
                WindowsPath windowsPathSubpath = null;
                int nameCount = windowsPath.getNameCount();
                if (i3 + 1 < nameCount) {
                    windowsPathSubpath = windowsPath.subpath(i3 + 1, nameCount);
                }
                windowsPath = windowsPathResolve.getParent().resolve((Path) windowsPathCreateFromNormalizedPath);
                try {
                    String strGetFullPathName = WindowsNativeDispatcher.GetFullPathName(windowsPath.toString());
                    if (!strGetFullPathName.equals(windowsPath.toString())) {
                        windowsPath = WindowsPath.createFromNormalizedPath(fileSystem, strGetFullPathName);
                    }
                } catch (WindowsException e3) {
                    e3.rethrowAsIOException(windowsPath);
                }
                if (windowsPathSubpath != null) {
                    windowsPath = windowsPath.resolve((Path) windowsPathSubpath);
                }
                i3 = 0;
            } else {
                i3++;
            }
        }
        return windowsPath;
    }

    private static String stripPrefix(String str) {
        String strSubstring;
        String strSubstring2;
        if (str.startsWith("\\\\?\\")) {
            if (str.startsWith("\\\\?\\UNC\\")) {
                strSubstring2 = FXMLLoader.ESCAPE_PREFIX + str.substring(7);
            } else {
                strSubstring2 = str.substring(4);
            }
            return strSubstring2;
        }
        if (str.startsWith("\\??\\")) {
            if (str.startsWith("\\??\\UNC\\")) {
                strSubstring = FXMLLoader.ESCAPE_PREFIX + str.substring(7);
            } else {
                strSubstring = str.substring(4);
            }
            return strSubstring;
        }
        return str;
    }
}
