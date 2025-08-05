package sun.nio.fs;

import java.security.AccessController;
import java.security.PrivilegedAction;
import sun.misc.Unsafe;

/* loaded from: rt.jar:sun/nio/fs/WindowsNativeDispatcher.class */
class WindowsNativeDispatcher {
    private static final Unsafe unsafe = Unsafe.getUnsafe();

    static native long CreateEvent(boolean z2, boolean z3) throws WindowsException;

    private static native long CreateFile0(long j2, int i2, int i3, long j3, int i4, int i5) throws WindowsException;

    static native void CloseHandle(long j2);

    private static native void DeleteFile0(long j2) throws WindowsException;

    private static native void CreateDirectory0(long j2, long j3) throws WindowsException;

    private static native void RemoveDirectory0(long j2) throws WindowsException;

    static native void DeviceIoControlSetSparse(long j2) throws WindowsException;

    static native void DeviceIoControlGetReparsePoint(long j2, long j3, int i2) throws WindowsException;

    private static native void FindFirstFile0(long j2, FirstFile firstFile) throws WindowsException;

    private static native long FindFirstFile1(long j2, long j3) throws WindowsException;

    static native String FindNextFile(long j2, long j3) throws WindowsException;

    private static native void FindFirstStream0(long j2, FirstStream firstStream) throws WindowsException;

    static native String FindNextStream(long j2) throws WindowsException;

    static native void FindClose(long j2) throws WindowsException;

    static native void GetFileInformationByHandle(long j2, long j3) throws WindowsException;

    private static native void CopyFileEx0(long j2, long j3, int i2, long j4) throws WindowsException;

    private static native void MoveFileEx0(long j2, long j3, int i2) throws WindowsException;

    private static native int GetFileAttributes0(long j2) throws WindowsException;

    private static native void SetFileAttributes0(long j2, int i2) throws WindowsException;

    private static native void GetFileAttributesEx0(long j2, long j3) throws WindowsException;

    static native void SetFileTime(long j2, long j3, long j4, long j5) throws WindowsException;

    static native void SetEndOfFile(long j2) throws WindowsException;

    static native int GetLogicalDrives() throws WindowsException;

    private static native void GetVolumeInformation0(long j2, VolumeInformation volumeInformation) throws WindowsException;

    private static native int GetDriveType0(long j2) throws WindowsException;

    private static native void GetDiskFreeSpaceEx0(long j2, DiskFreeSpace diskFreeSpace) throws WindowsException;

    private static native String GetVolumePathName0(long j2) throws WindowsException;

    static native void InitializeSecurityDescriptor(long j2) throws WindowsException;

    static native void InitializeAcl(long j2, int i2) throws WindowsException;

    private static native int GetFileSecurity0(long j2, int i2, long j3, int i3) throws WindowsException;

    static native void SetFileSecurity0(long j2, int i2, long j3) throws WindowsException;

    static native long GetSecurityDescriptorOwner(long j2) throws WindowsException;

    static native void SetSecurityDescriptorOwner(long j2, long j3) throws WindowsException;

    static native long GetSecurityDescriptorDacl(long j2);

    static native void SetSecurityDescriptorDacl(long j2, long j3) throws WindowsException;

    private static native void GetAclInformation0(long j2, AclInformation aclInformation);

    static native long GetAce(long j2, int i2);

    static native void AddAccessAllowedAceEx(long j2, int i2, int i3, long j3) throws WindowsException;

    static native void AddAccessDeniedAceEx(long j2, int i2, int i3, long j3) throws WindowsException;

    private static native void LookupAccountSid0(long j2, Account account) throws WindowsException;

    private static native int LookupAccountName0(long j2, long j3, int i2) throws WindowsException;

    static native int GetLengthSid(long j2);

    static native String ConvertSidToStringSid(long j2) throws WindowsException;

    private static native long ConvertStringSidToSid0(long j2) throws WindowsException;

    static native long GetCurrentProcess();

    static native long GetCurrentThread();

    static native long OpenProcessToken(long j2, int i2) throws WindowsException;

    static native long OpenThreadToken(long j2, int i2, boolean z2) throws WindowsException;

    static native long DuplicateTokenEx(long j2, int i2) throws WindowsException;

    static native void SetThreadToken(long j2, long j3) throws WindowsException;

    static native int GetTokenInformation(long j2, int i2, long j3, int i3) throws WindowsException;

    static native void AdjustTokenPrivileges(long j2, long j3, int i2) throws WindowsException;

    static native boolean AccessCheck(long j2, long j3, int i2, int i3, int i4, int i5, int i6) throws WindowsException;

    private static native long LookupPrivilegeValue0(long j2) throws WindowsException;

    private static native void CreateSymbolicLink0(long j2, long j3, int i2) throws WindowsException;

    private static native void CreateHardLink0(long j2, long j3) throws WindowsException;

    private static native String GetFullPathName0(long j2) throws WindowsException;

    static native String GetFinalPathNameByHandle(long j2) throws WindowsException;

    static native String FormatMessage(int i2);

    static native void LocalFree(long j2);

    static native long CreateIoCompletionPort(long j2, long j3, long j4) throws WindowsException;

    private static native void GetQueuedCompletionStatus0(long j2, CompletionStatus completionStatus) throws WindowsException;

    static native void PostQueuedCompletionStatus(long j2, long j3) throws WindowsException;

    static native void ReadDirectoryChangesW(long j2, long j3, int i2, boolean z2, int i3, long j4, long j5) throws WindowsException;

    static native void CancelIo(long j2) throws WindowsException;

    static native int GetOverlappedResult(long j2, long j3) throws WindowsException;

    private static native void BackupRead0(long j2, long j3, int i2, boolean z2, long j4, BackupResult backupResult) throws WindowsException;

    static native void BackupSeek(long j2, long j3, long j4) throws WindowsException;

    private static native void initIDs();

    private WindowsNativeDispatcher() {
    }

    static long CreateFile(String str, int i2, int i3, long j2, int i4, int i5) throws WindowsException {
        NativeBuffer nativeBufferAsNativeBuffer = asNativeBuffer(str);
        try {
            long jCreateFile0 = CreateFile0(nativeBufferAsNativeBuffer.address(), i2, i3, j2, i4, i5);
            nativeBufferAsNativeBuffer.release();
            return jCreateFile0;
        } catch (Throwable th) {
            nativeBufferAsNativeBuffer.release();
            throw th;
        }
    }

    static long CreateFile(String str, int i2, int i3, int i4, int i5) throws WindowsException {
        return CreateFile(str, i2, i3, 0L, i4, i5);
    }

    static void DeleteFile(String str) throws WindowsException {
        NativeBuffer nativeBufferAsNativeBuffer = asNativeBuffer(str);
        try {
            DeleteFile0(nativeBufferAsNativeBuffer.address());
        } finally {
            nativeBufferAsNativeBuffer.release();
        }
    }

    static void CreateDirectory(String str, long j2) throws WindowsException {
        NativeBuffer nativeBufferAsNativeBuffer = asNativeBuffer(str);
        try {
            CreateDirectory0(nativeBufferAsNativeBuffer.address(), j2);
            nativeBufferAsNativeBuffer.release();
        } catch (Throwable th) {
            nativeBufferAsNativeBuffer.release();
            throw th;
        }
    }

    static void RemoveDirectory(String str) throws WindowsException {
        NativeBuffer nativeBufferAsNativeBuffer = asNativeBuffer(str);
        try {
            RemoveDirectory0(nativeBufferAsNativeBuffer.address());
        } finally {
            nativeBufferAsNativeBuffer.release();
        }
    }

    static FirstFile FindFirstFile(String str) throws WindowsException {
        NativeBuffer nativeBufferAsNativeBuffer = asNativeBuffer(str);
        try {
            FirstFile firstFile = new FirstFile();
            FindFirstFile0(nativeBufferAsNativeBuffer.address(), firstFile);
            nativeBufferAsNativeBuffer.release();
            return firstFile;
        } catch (Throwable th) {
            nativeBufferAsNativeBuffer.release();
            throw th;
        }
    }

    /* loaded from: rt.jar:sun/nio/fs/WindowsNativeDispatcher$FirstFile.class */
    static class FirstFile {
        private long handle;
        private String name;
        private int attributes;

        private FirstFile() {
        }

        public long handle() {
            return this.handle;
        }

        public String name() {
            return this.name;
        }

        public int attributes() {
            return this.attributes;
        }
    }

    static long FindFirstFile(String str, long j2) throws WindowsException {
        NativeBuffer nativeBufferAsNativeBuffer = asNativeBuffer(str);
        try {
            long jFindFirstFile1 = FindFirstFile1(nativeBufferAsNativeBuffer.address(), j2);
            nativeBufferAsNativeBuffer.release();
            return jFindFirstFile1;
        } catch (Throwable th) {
            nativeBufferAsNativeBuffer.release();
            throw th;
        }
    }

    static FirstStream FindFirstStream(String str) throws WindowsException {
        NativeBuffer nativeBufferAsNativeBuffer = asNativeBuffer(str);
        try {
            FirstStream firstStream = new FirstStream();
            FindFirstStream0(nativeBufferAsNativeBuffer.address(), firstStream);
            if (firstStream.handle() == -1) {
                return null;
            }
            nativeBufferAsNativeBuffer.release();
            return firstStream;
        } finally {
            nativeBufferAsNativeBuffer.release();
        }
    }

    /* loaded from: rt.jar:sun/nio/fs/WindowsNativeDispatcher$FirstStream.class */
    static class FirstStream {
        private long handle;
        private String name;

        private FirstStream() {
        }

        public long handle() {
            return this.handle;
        }

        public String name() {
            return this.name;
        }
    }

    static void CopyFileEx(String str, String str2, int i2, long j2) throws WindowsException {
        NativeBuffer nativeBufferAsNativeBuffer = asNativeBuffer(str);
        NativeBuffer nativeBufferAsNativeBuffer2 = asNativeBuffer(str2);
        try {
            CopyFileEx0(nativeBufferAsNativeBuffer.address(), nativeBufferAsNativeBuffer2.address(), i2, j2);
            nativeBufferAsNativeBuffer2.release();
            nativeBufferAsNativeBuffer.release();
        } catch (Throwable th) {
            nativeBufferAsNativeBuffer2.release();
            nativeBufferAsNativeBuffer.release();
            throw th;
        }
    }

    static void MoveFileEx(String str, String str2, int i2) throws WindowsException {
        NativeBuffer nativeBufferAsNativeBuffer = asNativeBuffer(str);
        NativeBuffer nativeBufferAsNativeBuffer2 = asNativeBuffer(str2);
        try {
            MoveFileEx0(nativeBufferAsNativeBuffer.address(), nativeBufferAsNativeBuffer2.address(), i2);
            nativeBufferAsNativeBuffer2.release();
            nativeBufferAsNativeBuffer.release();
        } catch (Throwable th) {
            nativeBufferAsNativeBuffer2.release();
            nativeBufferAsNativeBuffer.release();
            throw th;
        }
    }

    static int GetFileAttributes(String str) throws WindowsException {
        NativeBuffer nativeBufferAsNativeBuffer = asNativeBuffer(str);
        try {
            return GetFileAttributes0(nativeBufferAsNativeBuffer.address());
        } finally {
            nativeBufferAsNativeBuffer.release();
        }
    }

    static void SetFileAttributes(String str, int i2) throws WindowsException {
        NativeBuffer nativeBufferAsNativeBuffer = asNativeBuffer(str);
        try {
            SetFileAttributes0(nativeBufferAsNativeBuffer.address(), i2);
        } finally {
            nativeBufferAsNativeBuffer.release();
        }
    }

    static void GetFileAttributesEx(String str, long j2) throws WindowsException {
        NativeBuffer nativeBufferAsNativeBuffer = asNativeBuffer(str);
        try {
            GetFileAttributesEx0(nativeBufferAsNativeBuffer.address(), j2);
            nativeBufferAsNativeBuffer.release();
        } catch (Throwable th) {
            nativeBufferAsNativeBuffer.release();
            throw th;
        }
    }

    static VolumeInformation GetVolumeInformation(String str) throws WindowsException {
        NativeBuffer nativeBufferAsNativeBuffer = asNativeBuffer(str);
        try {
            VolumeInformation volumeInformation = new VolumeInformation();
            GetVolumeInformation0(nativeBufferAsNativeBuffer.address(), volumeInformation);
            nativeBufferAsNativeBuffer.release();
            return volumeInformation;
        } catch (Throwable th) {
            nativeBufferAsNativeBuffer.release();
            throw th;
        }
    }

    /* loaded from: rt.jar:sun/nio/fs/WindowsNativeDispatcher$VolumeInformation.class */
    static class VolumeInformation {
        private String fileSystemName;
        private String volumeName;
        private int volumeSerialNumber;
        private int flags;

        private VolumeInformation() {
        }

        public String fileSystemName() {
            return this.fileSystemName;
        }

        public String volumeName() {
            return this.volumeName;
        }

        public int volumeSerialNumber() {
            return this.volumeSerialNumber;
        }

        public int flags() {
            return this.flags;
        }
    }

    static int GetDriveType(String str) throws WindowsException {
        NativeBuffer nativeBufferAsNativeBuffer = asNativeBuffer(str);
        try {
            return GetDriveType0(nativeBufferAsNativeBuffer.address());
        } finally {
            nativeBufferAsNativeBuffer.release();
        }
    }

    static DiskFreeSpace GetDiskFreeSpaceEx(String str) throws WindowsException {
        NativeBuffer nativeBufferAsNativeBuffer = asNativeBuffer(str);
        try {
            DiskFreeSpace diskFreeSpace = new DiskFreeSpace();
            GetDiskFreeSpaceEx0(nativeBufferAsNativeBuffer.address(), diskFreeSpace);
            nativeBufferAsNativeBuffer.release();
            return diskFreeSpace;
        } catch (Throwable th) {
            nativeBufferAsNativeBuffer.release();
            throw th;
        }
    }

    /* loaded from: rt.jar:sun/nio/fs/WindowsNativeDispatcher$DiskFreeSpace.class */
    static class DiskFreeSpace {
        private long freeBytesAvailable;
        private long totalNumberOfBytes;
        private long totalNumberOfFreeBytes;

        private DiskFreeSpace() {
        }

        public long freeBytesAvailable() {
            return this.freeBytesAvailable;
        }

        public long totalNumberOfBytes() {
            return this.totalNumberOfBytes;
        }

        public long totalNumberOfFreeBytes() {
            return this.totalNumberOfFreeBytes;
        }
    }

    static String GetVolumePathName(String str) throws WindowsException {
        NativeBuffer nativeBufferAsNativeBuffer = asNativeBuffer(str);
        try {
            return GetVolumePathName0(nativeBufferAsNativeBuffer.address());
        } finally {
            nativeBufferAsNativeBuffer.release();
        }
    }

    static int GetFileSecurity(String str, int i2, long j2, int i3) throws WindowsException {
        NativeBuffer nativeBufferAsNativeBuffer = asNativeBuffer(str);
        try {
            int iGetFileSecurity0 = GetFileSecurity0(nativeBufferAsNativeBuffer.address(), i2, j2, i3);
            nativeBufferAsNativeBuffer.release();
            return iGetFileSecurity0;
        } catch (Throwable th) {
            nativeBufferAsNativeBuffer.release();
            throw th;
        }
    }

    static void SetFileSecurity(String str, int i2, long j2) throws WindowsException {
        NativeBuffer nativeBufferAsNativeBuffer = asNativeBuffer(str);
        try {
            SetFileSecurity0(nativeBufferAsNativeBuffer.address(), i2, j2);
            nativeBufferAsNativeBuffer.release();
        } catch (Throwable th) {
            nativeBufferAsNativeBuffer.release();
            throw th;
        }
    }

    static AclInformation GetAclInformation(long j2) {
        AclInformation aclInformation = new AclInformation();
        GetAclInformation0(j2, aclInformation);
        return aclInformation;
    }

    /* loaded from: rt.jar:sun/nio/fs/WindowsNativeDispatcher$AclInformation.class */
    static class AclInformation {
        private int aceCount;

        private AclInformation() {
        }

        public int aceCount() {
            return this.aceCount;
        }
    }

    static Account LookupAccountSid(long j2) throws WindowsException {
        Account account = new Account();
        LookupAccountSid0(j2, account);
        return account;
    }

    /* loaded from: rt.jar:sun/nio/fs/WindowsNativeDispatcher$Account.class */
    static class Account {
        private String domain;
        private String name;
        private int use;

        private Account() {
        }

        public String domain() {
            return this.domain;
        }

        public String name() {
            return this.name;
        }

        public int use() {
            return this.use;
        }
    }

    static int LookupAccountName(String str, long j2, int i2) throws WindowsException {
        NativeBuffer nativeBufferAsNativeBuffer = asNativeBuffer(str);
        try {
            int iLookupAccountName0 = LookupAccountName0(nativeBufferAsNativeBuffer.address(), j2, i2);
            nativeBufferAsNativeBuffer.release();
            return iLookupAccountName0;
        } catch (Throwable th) {
            nativeBufferAsNativeBuffer.release();
            throw th;
        }
    }

    static long ConvertStringSidToSid(String str) throws WindowsException {
        NativeBuffer nativeBufferAsNativeBuffer = asNativeBuffer(str);
        try {
            long jConvertStringSidToSid0 = ConvertStringSidToSid0(nativeBufferAsNativeBuffer.address());
            nativeBufferAsNativeBuffer.release();
            return jConvertStringSidToSid0;
        } catch (Throwable th) {
            nativeBufferAsNativeBuffer.release();
            throw th;
        }
    }

    static long LookupPrivilegeValue(String str) throws WindowsException {
        NativeBuffer nativeBufferAsNativeBuffer = asNativeBuffer(str);
        try {
            long jLookupPrivilegeValue0 = LookupPrivilegeValue0(nativeBufferAsNativeBuffer.address());
            nativeBufferAsNativeBuffer.release();
            return jLookupPrivilegeValue0;
        } catch (Throwable th) {
            nativeBufferAsNativeBuffer.release();
            throw th;
        }
    }

    static void CreateSymbolicLink(String str, String str2, int i2) throws WindowsException {
        NativeBuffer nativeBufferAsNativeBuffer = asNativeBuffer(str);
        NativeBuffer nativeBufferAsNativeBuffer2 = asNativeBuffer(str2);
        try {
            CreateSymbolicLink0(nativeBufferAsNativeBuffer.address(), nativeBufferAsNativeBuffer2.address(), i2);
            nativeBufferAsNativeBuffer2.release();
            nativeBufferAsNativeBuffer.release();
        } catch (Throwable th) {
            nativeBufferAsNativeBuffer2.release();
            nativeBufferAsNativeBuffer.release();
            throw th;
        }
    }

    static void CreateHardLink(String str, String str2) throws WindowsException {
        NativeBuffer nativeBufferAsNativeBuffer = asNativeBuffer(str);
        NativeBuffer nativeBufferAsNativeBuffer2 = asNativeBuffer(str2);
        try {
            CreateHardLink0(nativeBufferAsNativeBuffer.address(), nativeBufferAsNativeBuffer2.address());
            nativeBufferAsNativeBuffer2.release();
            nativeBufferAsNativeBuffer.release();
        } catch (Throwable th) {
            nativeBufferAsNativeBuffer2.release();
            nativeBufferAsNativeBuffer.release();
            throw th;
        }
    }

    static String GetFullPathName(String str) throws WindowsException {
        NativeBuffer nativeBufferAsNativeBuffer = asNativeBuffer(str);
        try {
            return GetFullPathName0(nativeBufferAsNativeBuffer.address());
        } finally {
            nativeBufferAsNativeBuffer.release();
        }
    }

    static CompletionStatus GetQueuedCompletionStatus(long j2) throws WindowsException {
        CompletionStatus completionStatus = new CompletionStatus();
        GetQueuedCompletionStatus0(j2, completionStatus);
        return completionStatus;
    }

    /* loaded from: rt.jar:sun/nio/fs/WindowsNativeDispatcher$CompletionStatus.class */
    static class CompletionStatus {
        private int error;
        private int bytesTransferred;
        private long completionKey;

        private CompletionStatus() {
        }

        int error() {
            return this.error;
        }

        int bytesTransferred() {
            return this.bytesTransferred;
        }

        long completionKey() {
            return this.completionKey;
        }
    }

    static BackupResult BackupRead(long j2, long j3, int i2, boolean z2, long j4) throws WindowsException {
        BackupResult backupResult = new BackupResult();
        BackupRead0(j2, j3, i2, z2, j4, backupResult);
        return backupResult;
    }

    /* loaded from: rt.jar:sun/nio/fs/WindowsNativeDispatcher$BackupResult.class */
    static class BackupResult {
        private int bytesTransferred;
        private long context;

        private BackupResult() {
        }

        int bytesTransferred() {
            return this.bytesTransferred;
        }

        long context() {
            return this.context;
        }
    }

    static {
        AccessController.doPrivileged(new PrivilegedAction<Void>() { // from class: sun.nio.fs.WindowsNativeDispatcher.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.security.PrivilegedAction
            public Void run() {
                System.loadLibrary("net");
                System.loadLibrary("nio");
                return null;
            }
        });
        initIDs();
    }

    static NativeBuffer asNativeBuffer(String str) throws WindowsException {
        if (str.length() > 1073741822) {
            throw new WindowsException("String too long to convert to native buffer");
        }
        int length = str.length() << 1;
        int i2 = length + 2;
        NativeBuffer nativeBufferFromCache = NativeBuffers.getNativeBufferFromCache(i2);
        if (nativeBufferFromCache == null) {
            nativeBufferFromCache = NativeBuffers.allocNativeBuffer(i2);
        } else if (nativeBufferFromCache.owner() == str) {
            return nativeBufferFromCache;
        }
        unsafe.copyMemory(str.toCharArray(), Unsafe.ARRAY_CHAR_BASE_OFFSET, null, nativeBufferFromCache.address(), length);
        unsafe.putChar(nativeBufferFromCache.address() + length, (char) 0);
        nativeBufferFromCache.setOwner(str);
        return nativeBufferFromCache;
    }
}
