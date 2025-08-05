package sun.nio.fs;

import java.nio.file.attribute.DosFileAttributes;
import java.nio.file.attribute.FileTime;
import java.security.AccessController;
import java.util.concurrent.TimeUnit;
import sun.misc.Unsafe;
import sun.security.action.GetPropertyAction;

/* loaded from: rt.jar:sun/nio/fs/WindowsFileAttributes.class */
class WindowsFileAttributes implements DosFileAttributes {
    private static final Unsafe unsafe = Unsafe.getUnsafe();
    private static final short SIZEOF_FILE_INFORMATION = 52;
    private static final short OFFSETOF_FILE_INFORMATION_ATTRIBUTES = 0;
    private static final short OFFSETOF_FILE_INFORMATION_CREATETIME = 4;
    private static final short OFFSETOF_FILE_INFORMATION_LASTACCESSTIME = 12;
    private static final short OFFSETOF_FILE_INFORMATION_LASTWRITETIME = 20;
    private static final short OFFSETOF_FILE_INFORMATION_VOLSERIALNUM = 28;
    private static final short OFFSETOF_FILE_INFORMATION_SIZEHIGH = 32;
    private static final short OFFSETOF_FILE_INFORMATION_SIZELOW = 36;
    private static final short OFFSETOF_FILE_INFORMATION_INDEXHIGH = 44;
    private static final short OFFSETOF_FILE_INFORMATION_INDEXLOW = 48;
    private static final short SIZEOF_FILE_ATTRIBUTE_DATA = 36;
    private static final short OFFSETOF_FILE_ATTRIBUTE_DATA_ATTRIBUTES = 0;
    private static final short OFFSETOF_FILE_ATTRIBUTE_DATA_CREATETIME = 4;
    private static final short OFFSETOF_FILE_ATTRIBUTE_DATA_LASTACCESSTIME = 12;
    private static final short OFFSETOF_FILE_ATTRIBUTE_DATA_LASTWRITETIME = 20;
    private static final short OFFSETOF_FILE_ATTRIBUTE_DATA_SIZEHIGH = 28;
    private static final short OFFSETOF_FILE_ATTRIBUTE_DATA_SIZELOW = 32;
    private static final short SIZEOF_FIND_DATA = 592;
    private static final short OFFSETOF_FIND_DATA_ATTRIBUTES = 0;
    private static final short OFFSETOF_FIND_DATA_CREATETIME = 4;
    private static final short OFFSETOF_FIND_DATA_LASTACCESSTIME = 12;
    private static final short OFFSETOF_FIND_DATA_LASTWRITETIME = 20;
    private static final short OFFSETOF_FIND_DATA_SIZEHIGH = 28;
    private static final short OFFSETOF_FIND_DATA_SIZELOW = 32;
    private static final short OFFSETOF_FIND_DATA_RESERVED0 = 36;
    private static final long WINDOWS_EPOCH_IN_MICROSECONDS = -11644473600000000L;
    private static final boolean ensureAccurateMetadata;
    private final int fileAttrs;
    private final long creationTime;
    private final long lastAccessTime;
    private final long lastWriteTime;
    private final long size;
    private final int reparseTag;
    private final int volSerialNumber;
    private final int fileIndexHigh;
    private final int fileIndexLow;

    static {
        String str = (String) AccessController.doPrivileged(new GetPropertyAction("sun.nio.fs.ensureAccurateMetadata", "false"));
        ensureAccurateMetadata = str.length() == 0 ? true : Boolean.valueOf(str).booleanValue();
    }

    static FileTime toFileTime(long j2) {
        return FileTime.from((j2 / 10) + WINDOWS_EPOCH_IN_MICROSECONDS, TimeUnit.MICROSECONDS);
    }

    static long toWindowsTime(FileTime fileTime) {
        return (fileTime.to(TimeUnit.MICROSECONDS) - WINDOWS_EPOCH_IN_MICROSECONDS) * 10;
    }

    private WindowsFileAttributes(int i2, long j2, long j3, long j4, long j5, int i3, int i4, int i5, int i6) {
        this.fileAttrs = i2;
        this.creationTime = j2;
        this.lastAccessTime = j3;
        this.lastWriteTime = j4;
        this.size = j5;
        this.reparseTag = i3;
        this.volSerialNumber = i4;
        this.fileIndexHigh = i5;
        this.fileIndexLow = i6;
    }

    private static WindowsFileAttributes fromFileInformation(long j2, int i2) {
        return new WindowsFileAttributes(unsafe.getInt(j2 + 0), unsafe.getLong(j2 + 4), unsafe.getLong(j2 + 12), unsafe.getLong(j2 + 20), (unsafe.getInt(j2 + 32) << 32) + (unsafe.getInt(j2 + 36) & 4294967295L), i2, unsafe.getInt(j2 + 28), unsafe.getInt(j2 + 44), unsafe.getInt(j2 + 48));
    }

    private static WindowsFileAttributes fromFileAttributeData(long j2, int i2) {
        return new WindowsFileAttributes(unsafe.getInt(j2 + 0), unsafe.getLong(j2 + 4), unsafe.getLong(j2 + 12), unsafe.getLong(j2 + 20), (unsafe.getInt(j2 + 28) << 32) + (unsafe.getInt(j2 + 32) & 4294967295L), i2, 0, 0, 0);
    }

    static NativeBuffer getBufferForFindData() {
        return NativeBuffers.getNativeBuffer(SIZEOF_FIND_DATA);
    }

    static WindowsFileAttributes fromFindData(long j2) {
        int i2 = unsafe.getInt(j2 + 0);
        return new WindowsFileAttributes(i2, unsafe.getLong(j2 + 4), unsafe.getLong(j2 + 12), unsafe.getLong(j2 + 20), (unsafe.getInt(j2 + 28) << 32) + (unsafe.getInt(j2 + 32) & 4294967295L), isReparsePoint(i2) ? unsafe.getInt(j2 + 36) : 0, 0, 0, 0);
    }

    static WindowsFileAttributes readAttributes(long j2) throws WindowsException {
        NativeBuffer nativeBuffer = NativeBuffers.getNativeBuffer(52);
        try {
            long jAddress = nativeBuffer.address();
            WindowsNativeDispatcher.GetFileInformationByHandle(j2, jAddress);
            int i2 = 0;
            if (isReparsePoint(unsafe.getInt(jAddress + 0))) {
                nativeBuffer = NativeBuffers.getNativeBuffer(16384);
                try {
                    WindowsNativeDispatcher.DeviceIoControlGetReparsePoint(j2, nativeBuffer.address(), 16384);
                    i2 = (int) unsafe.getLong(nativeBuffer.address());
                    nativeBuffer.release();
                } finally {
                    nativeBuffer.release();
                }
            }
            return fromFileInformation(jAddress, i2);
        } catch (Throwable th) {
            nativeBuffer.release();
            throw th;
        }
    }

    static WindowsFileAttributes get(WindowsPath windowsPath, boolean z2) throws WindowsException {
        long jAddress;
        if (!ensureAccurateMetadata) {
            WindowsException windowsException = null;
            NativeBuffer nativeBuffer = NativeBuffers.getNativeBuffer(36);
            try {
                try {
                    jAddress = nativeBuffer.address();
                    WindowsNativeDispatcher.GetFileAttributesEx(windowsPath.getPathForWin32Calls(), jAddress);
                } catch (WindowsException e2) {
                    if (e2.lastError() != 32) {
                        throw e2;
                    }
                    windowsException = e2;
                    nativeBuffer.release();
                }
                if (!isReparsePoint(unsafe.getInt(jAddress + 0))) {
                    WindowsFileAttributes windowsFileAttributesFromFileAttributeData = fromFileAttributeData(jAddress, 0);
                    nativeBuffer.release();
                    return windowsFileAttributesFromFileAttributeData;
                }
                nativeBuffer.release();
                if (windowsException != null) {
                    String pathForWin32Calls = windowsPath.getPathForWin32Calls();
                    char cCharAt = pathForWin32Calls.charAt(pathForWin32Calls.length() - 1);
                    if (cCharAt == ':' || cCharAt == '\\') {
                        throw windowsException;
                    }
                    NativeBuffer bufferForFindData = getBufferForFindData();
                    try {
                        try {
                            WindowsNativeDispatcher.FindClose(WindowsNativeDispatcher.FindFirstFile(pathForWin32Calls, bufferForFindData.address()));
                            WindowsFileAttributes windowsFileAttributesFromFindData = fromFindData(bufferForFindData.address());
                            if (windowsFileAttributesFromFindData.isReparsePoint()) {
                                throw windowsException;
                            }
                            return windowsFileAttributesFromFindData;
                        } catch (WindowsException e3) {
                            throw windowsException;
                        }
                    } finally {
                        bufferForFindData.release();
                    }
                }
            } catch (Throwable th) {
                nativeBuffer.release();
                throw th;
            }
        }
        long jOpenForReadAttributeAccess = windowsPath.openForReadAttributeAccess(z2);
        try {
            WindowsFileAttributes attributes = readAttributes(jOpenForReadAttributeAccess);
            WindowsNativeDispatcher.CloseHandle(jOpenForReadAttributeAccess);
            return attributes;
        } catch (Throwable th2) {
            WindowsNativeDispatcher.CloseHandle(jOpenForReadAttributeAccess);
            throw th2;
        }
    }

    static boolean isSameFile(WindowsFileAttributes windowsFileAttributes, WindowsFileAttributes windowsFileAttributes2) {
        return windowsFileAttributes.volSerialNumber == windowsFileAttributes2.volSerialNumber && windowsFileAttributes.fileIndexHigh == windowsFileAttributes2.fileIndexHigh && windowsFileAttributes.fileIndexLow == windowsFileAttributes2.fileIndexLow;
    }

    static boolean isReparsePoint(int i2) {
        return (i2 & 1024) != 0;
    }

    int attributes() {
        return this.fileAttrs;
    }

    int volSerialNumber() {
        return this.volSerialNumber;
    }

    int fileIndexHigh() {
        return this.fileIndexHigh;
    }

    int fileIndexLow() {
        return this.fileIndexLow;
    }

    @Override // java.nio.file.attribute.BasicFileAttributes
    public long size() {
        return this.size;
    }

    @Override // java.nio.file.attribute.BasicFileAttributes
    public FileTime lastModifiedTime() {
        return toFileTime(this.lastWriteTime);
    }

    @Override // java.nio.file.attribute.BasicFileAttributes
    public FileTime lastAccessTime() {
        return toFileTime(this.lastAccessTime);
    }

    @Override // java.nio.file.attribute.BasicFileAttributes
    public FileTime creationTime() {
        return toFileTime(this.creationTime);
    }

    @Override // java.nio.file.attribute.BasicFileAttributes
    public Object fileKey() {
        return null;
    }

    boolean isReparsePoint() {
        return isReparsePoint(this.fileAttrs);
    }

    boolean isDirectoryLink() {
        return isSymbolicLink() && (this.fileAttrs & 16) != 0;
    }

    @Override // java.nio.file.attribute.BasicFileAttributes
    public boolean isSymbolicLink() {
        return this.reparseTag == -1610612724;
    }

    @Override // java.nio.file.attribute.BasicFileAttributes
    public boolean isDirectory() {
        return (isSymbolicLink() || (this.fileAttrs & 16) == 0) ? false : true;
    }

    @Override // java.nio.file.attribute.BasicFileAttributes
    public boolean isOther() {
        return (isSymbolicLink() || (this.fileAttrs & 1088) == 0) ? false : true;
    }

    @Override // java.nio.file.attribute.BasicFileAttributes
    public boolean isRegularFile() {
        return (isSymbolicLink() || isDirectory() || isOther()) ? false : true;
    }

    @Override // java.nio.file.attribute.DosFileAttributes
    public boolean isReadOnly() {
        return (this.fileAttrs & 1) != 0;
    }

    @Override // java.nio.file.attribute.DosFileAttributes
    public boolean isHidden() {
        return (this.fileAttrs & 2) != 0;
    }

    @Override // java.nio.file.attribute.DosFileAttributes
    public boolean isArchive() {
        return (this.fileAttrs & 32) != 0;
    }

    @Override // java.nio.file.attribute.DosFileAttributes
    public boolean isSystem() {
        return (this.fileAttrs & 4) != 0;
    }
}
