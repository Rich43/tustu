package sun.nio.fs;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import jdk.internal.dynalink.CallSiteDescriptor;
import sun.misc.Unsafe;
import sun.nio.fs.WindowsNativeDispatcher;

/* loaded from: rt.jar:sun/nio/fs/WindowsUserDefinedFileAttributeView.class */
class WindowsUserDefinedFileAttributeView extends AbstractUserDefinedFileAttributeView {
    private static final Unsafe unsafe = Unsafe.getUnsafe();
    private final WindowsPath file;
    private final boolean followLinks;

    private String join(String str, String str2) {
        if (str2 == null) {
            throw new NullPointerException("'name' is null");
        }
        return str + CallSiteDescriptor.TOKEN_DELIMITER + str2;
    }

    private String join(WindowsPath windowsPath, String str) throws WindowsException {
        return join(windowsPath.getPathForWin32Calls(), str);
    }

    WindowsUserDefinedFileAttributeView(WindowsPath windowsPath, boolean z2) {
        this.file = windowsPath;
        this.followLinks = z2;
    }

    /* JADX WARN: Finally extract failed */
    private List<String> listUsingStreamEnumeration() throws IOException {
        ArrayList arrayList = new ArrayList();
        try {
            WindowsNativeDispatcher.FirstStream firstStreamFindFirstStream = WindowsNativeDispatcher.FindFirstStream(this.file.getPathForWin32Calls());
            if (firstStreamFindFirstStream != null) {
                long jHandle = firstStreamFindFirstStream.handle();
                try {
                    String strName = firstStreamFindFirstStream.name();
                    if (!strName.equals("::$DATA")) {
                        arrayList.add(strName.split(CallSiteDescriptor.TOKEN_DELIMITER)[1]);
                    }
                    while (true) {
                        String strFindNextStream = WindowsNativeDispatcher.FindNextStream(jHandle);
                        if (strFindNextStream == null) {
                            break;
                        }
                        arrayList.add(strFindNextStream.split(CallSiteDescriptor.TOKEN_DELIMITER)[1]);
                    }
                    WindowsNativeDispatcher.FindClose(jHandle);
                } catch (Throwable th) {
                    WindowsNativeDispatcher.FindClose(jHandle);
                    throw th;
                }
            }
        } catch (WindowsException e2) {
            e2.rethrowAsIOException(this.file);
        }
        return Collections.unmodifiableList(arrayList);
    }

    /* JADX WARN: Code restructure failed: missing block: B:36:0x012a, code lost:
    
        if (r22 == 0) goto L51;
     */
    /* JADX WARN: Code restructure failed: missing block: B:37:0x012d, code lost:
    
        sun.nio.fs.WindowsNativeDispatcher.BackupRead(r11, 0, 0, true, r22);
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private java.util.List<java.lang.String> listUsingBackupRead() throws java.io.IOException {
        /*
            Method dump skipped, instructions count: 403
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: sun.nio.fs.WindowsUserDefinedFileAttributeView.listUsingBackupRead():java.util.List");
    }

    @Override // java.nio.file.attribute.UserDefinedFileAttributeView
    public List<String> list() throws IOException {
        if (System.getSecurityManager() != null) {
            checkAccess(this.file.getPathForPermissionCheck(), true, false);
        }
        if (this.file.getFileSystem().supportsStreamEnumeration()) {
            return listUsingStreamEnumeration();
        }
        return listUsingBackupRead();
    }

    @Override // java.nio.file.attribute.UserDefinedFileAttributeView
    public int size(String str) throws IOException {
        if (System.getSecurityManager() != null) {
            checkAccess(this.file.getPathForPermissionCheck(), true, false);
        }
        FileChannel fileChannelNewFileChannel = null;
        try {
            HashSet hashSet = new HashSet();
            hashSet.add(StandardOpenOption.READ);
            if (!this.followLinks) {
                hashSet.add(WindowsChannelFactory.OPEN_REPARSE_POINT);
            }
            fileChannelNewFileChannel = WindowsChannelFactory.newFileChannel(join(this.file, str), null, hashSet, 0L);
        } catch (WindowsException e2) {
            e2.rethrowAsIOException(join(this.file.getPathForPermissionCheck(), str));
        }
        try {
            long size = fileChannelNewFileChannel.size();
            if (size > 2147483647L) {
                throw new ArithmeticException("Stream too large");
            }
            int i2 = (int) size;
            fileChannelNewFileChannel.close();
            return i2;
        } catch (Throwable th) {
            fileChannelNewFileChannel.close();
            throw th;
        }
    }

    @Override // java.nio.file.attribute.UserDefinedFileAttributeView
    public int read(String str, ByteBuffer byteBuffer) throws IOException {
        int i2;
        if (System.getSecurityManager() != null) {
            checkAccess(this.file.getPathForPermissionCheck(), true, false);
        }
        FileChannel fileChannelNewFileChannel = null;
        try {
            HashSet hashSet = new HashSet();
            hashSet.add(StandardOpenOption.READ);
            if (!this.followLinks) {
                hashSet.add(WindowsChannelFactory.OPEN_REPARSE_POINT);
            }
            fileChannelNewFileChannel = WindowsChannelFactory.newFileChannel(join(this.file, str), null, hashSet, 0L);
        } catch (WindowsException e2) {
            e2.rethrowAsIOException(join(this.file.getPathForPermissionCheck(), str));
        }
        try {
            if (fileChannelNewFileChannel.size() > byteBuffer.remaining()) {
                throw new IOException("Stream too large");
            }
            int i3 = 0;
            while (byteBuffer.hasRemaining() && (i2 = fileChannelNewFileChannel.read(byteBuffer)) >= 0) {
                i3 += i2;
            }
            return i3;
        } finally {
            fileChannelNewFileChannel.close();
        }
    }

    /* JADX WARN: Finally extract failed */
    @Override // java.nio.file.attribute.UserDefinedFileAttributeView
    public int write(String str, ByteBuffer byteBuffer) throws IOException {
        if (System.getSecurityManager() != null) {
            checkAccess(this.file.getPathForPermissionCheck(), false, true);
        }
        long jCreateFile = -1;
        try {
            int i2 = 33554432;
            if (!this.followLinks) {
                i2 = 33554432 | 2097152;
            }
            jCreateFile = WindowsNativeDispatcher.CreateFile(this.file.getPathForWin32Calls(), Integer.MIN_VALUE, 7, 3, i2);
        } catch (WindowsException e2) {
            e2.rethrowAsIOException(this.file);
        }
        try {
            HashSet hashSet = new HashSet();
            if (!this.followLinks) {
                hashSet.add(WindowsChannelFactory.OPEN_REPARSE_POINT);
            }
            hashSet.add(StandardOpenOption.CREATE);
            hashSet.add(StandardOpenOption.WRITE);
            hashSet.add(StandardOpenOption.TRUNCATE_EXISTING);
            FileChannel fileChannelNewFileChannel = null;
            try {
                fileChannelNewFileChannel = WindowsChannelFactory.newFileChannel(join(this.file, str), null, hashSet, 0L);
            } catch (WindowsException e3) {
                e3.rethrowAsIOException(join(this.file.getPathForPermissionCheck(), str));
            }
            try {
                int iRemaining = byteBuffer.remaining();
                while (byteBuffer.hasRemaining()) {
                    fileChannelNewFileChannel.write(byteBuffer);
                }
                fileChannelNewFileChannel.close();
                WindowsNativeDispatcher.CloseHandle(jCreateFile);
                return iRemaining;
            } catch (Throwable th) {
                fileChannelNewFileChannel.close();
                throw th;
            }
        } catch (Throwable th2) {
            WindowsNativeDispatcher.CloseHandle(jCreateFile);
            throw th2;
        }
    }

    @Override // java.nio.file.attribute.UserDefinedFileAttributeView
    public void delete(String str) throws IOException {
        if (System.getSecurityManager() != null) {
            checkAccess(this.file.getPathForPermissionCheck(), false, true);
        }
        String strJoin = join(WindowsLinkSupport.getFinalPath(this.file, this.followLinks), str);
        try {
            WindowsNativeDispatcher.DeleteFile(strJoin);
        } catch (WindowsException e2) {
            e2.rethrowAsIOException(strJoin);
        }
    }
}
