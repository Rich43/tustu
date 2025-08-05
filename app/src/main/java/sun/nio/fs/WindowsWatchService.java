package sun.nio.fs;

import com.sun.nio.file.ExtendedWatchEventModifier;
import com.sun.nio.file.SensitivityWatchEventModifier;
import java.io.IOException;
import java.nio.file.NotDirectoryException;
import java.nio.file.Path;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import sun.misc.Unsafe;
import sun.nio.fs.WindowsNativeDispatcher;

/* loaded from: rt.jar:sun/nio/fs/WindowsWatchService.class */
class WindowsWatchService extends AbstractWatchService {
    private static final int WAKEUP_COMPLETION_KEY = 0;
    private final Poller poller;
    private static final int ALL_FILE_NOTIFY_EVENTS = 351;

    WindowsWatchService(WindowsFileSystem windowsFileSystem) throws IOException {
        try {
            this.poller = new Poller(windowsFileSystem, this, WindowsNativeDispatcher.CreateIoCompletionPort(-1L, 0L, 0L));
            this.poller.start();
        } catch (WindowsException e2) {
            throw new IOException(e2.getMessage());
        }
    }

    @Override // sun.nio.fs.AbstractWatchService
    WatchKey register(Path path, WatchEvent.Kind<?>[] kindArr, WatchEvent.Modifier... modifierArr) throws IOException {
        return this.poller.register(path, kindArr, modifierArr);
    }

    @Override // sun.nio.fs.AbstractWatchService
    void implClose() throws IOException {
        this.poller.close();
    }

    /* loaded from: rt.jar:sun/nio/fs/WindowsWatchService$WindowsWatchKey.class */
    private static class WindowsWatchKey extends AbstractWatchKey {
        private final FileKey fileKey;
        private volatile long handle;
        private Set<? extends WatchEvent.Kind<?>> events;
        private boolean watchSubtree;
        private NativeBuffer buffer;
        private long countAddress;
        private long overlappedAddress;
        private int completionKey;
        private boolean errorStartingOverlapped;

        WindowsWatchKey(Path path, AbstractWatchService abstractWatchService, FileKey fileKey) {
            super(path, abstractWatchService);
            this.handle = -1L;
            this.fileKey = fileKey;
        }

        WindowsWatchKey init(long j2, Set<? extends WatchEvent.Kind<?>> set, boolean z2, NativeBuffer nativeBuffer, long j3, long j4, int i2) {
            this.handle = j2;
            this.events = set;
            this.watchSubtree = z2;
            this.buffer = nativeBuffer;
            this.countAddress = j3;
            this.overlappedAddress = j4;
            this.completionKey = i2;
            return this;
        }

        long handle() {
            return this.handle;
        }

        Set<? extends WatchEvent.Kind<?>> events() {
            return this.events;
        }

        void setEvents(Set<? extends WatchEvent.Kind<?>> set) {
            this.events = set;
        }

        boolean watchSubtree() {
            return this.watchSubtree;
        }

        NativeBuffer buffer() {
            return this.buffer;
        }

        long countAddress() {
            return this.countAddress;
        }

        long overlappedAddress() {
            return this.overlappedAddress;
        }

        FileKey fileKey() {
            return this.fileKey;
        }

        int completionKey() {
            return this.completionKey;
        }

        void setErrorStartingOverlapped(boolean z2) {
            this.errorStartingOverlapped = z2;
        }

        boolean isErrorStartingOverlapped() {
            return this.errorStartingOverlapped;
        }

        void invalidate() {
            ((WindowsWatchService) watcher()).poller.releaseResources(this);
            this.handle = -1L;
            this.buffer = null;
            this.countAddress = 0L;
            this.overlappedAddress = 0L;
            this.errorStartingOverlapped = false;
        }

        @Override // java.nio.file.WatchKey
        public boolean isValid() {
            return this.handle != -1;
        }

        @Override // java.nio.file.WatchKey
        public void cancel() {
            if (isValid()) {
                ((WindowsWatchService) watcher()).poller.cancel(this);
            }
        }
    }

    /* loaded from: rt.jar:sun/nio/fs/WindowsWatchService$FileKey.class */
    private static class FileKey {
        private final int volSerialNumber;
        private final int fileIndexHigh;
        private final int fileIndexLow;

        FileKey(int i2, int i3, int i4) {
            this.volSerialNumber = i2;
            this.fileIndexHigh = i3;
            this.fileIndexLow = i4;
        }

        public int hashCode() {
            return (this.volSerialNumber ^ this.fileIndexHigh) ^ this.fileIndexLow;
        }

        public boolean equals(Object obj) {
            if (obj == this) {
                return true;
            }
            if (!(obj instanceof FileKey)) {
                return false;
            }
            FileKey fileKey = (FileKey) obj;
            return this.volSerialNumber == fileKey.volSerialNumber && this.fileIndexHigh == fileKey.fileIndexHigh && this.fileIndexLow == fileKey.fileIndexLow;
        }
    }

    /* loaded from: rt.jar:sun/nio/fs/WindowsWatchService$Poller.class */
    private static class Poller extends AbstractPoller {
        private static final Unsafe UNSAFE = Unsafe.getUnsafe();
        private static final short SIZEOF_DWORD = 4;
        private static final short SIZEOF_OVERLAPPED = 32;
        private static final short OFFSETOF_HEVENT;
        private static final short OFFSETOF_NEXTENTRYOFFSET = 0;
        private static final short OFFSETOF_ACTION = 4;
        private static final short OFFSETOF_FILENAMELENGTH = 8;
        private static final short OFFSETOF_FILENAME = 12;
        private static final int CHANGES_BUFFER_SIZE = 16384;
        private final WindowsFileSystem fs;
        private final WindowsWatchService watcher;
        private final long port;
        private final Map<Integer, WindowsWatchKey> ck2key = new HashMap();
        private final Map<FileKey, WindowsWatchKey> fk2key = new HashMap();
        private int lastCompletionKey = 0;

        static {
            OFFSETOF_HEVENT = UNSAFE.addressSize() == 4 ? (short) 16 : (short) 24;
        }

        Poller(WindowsFileSystem windowsFileSystem, WindowsWatchService windowsWatchService, long j2) {
            this.fs = windowsFileSystem;
            this.watcher = windowsWatchService;
            this.port = j2;
        }

        @Override // sun.nio.fs.AbstractPoller
        void wakeup() throws IOException {
            try {
                WindowsNativeDispatcher.PostQueuedCompletionStatus(this.port, 0L);
            } catch (WindowsException e2) {
                throw new IOException(e2.getMessage());
            }
        }

        @Override // sun.nio.fs.AbstractPoller
        Object implRegister(Path path, Set<? extends WatchEvent.Kind<?>> set, WatchEvent.Modifier... modifierArr) {
            WindowsWatchKey windowsWatchKeyInit;
            WindowsPath windowsPath = (WindowsPath) path;
            boolean z2 = false;
            for (WatchEvent.Modifier modifier : modifierArr) {
                if (modifier == ExtendedWatchEventModifier.FILE_TREE) {
                    z2 = true;
                } else {
                    if (modifier == null) {
                        return new NullPointerException();
                    }
                    if (!(modifier instanceof SensitivityWatchEventModifier)) {
                        return new UnsupportedOperationException("Modifier not supported");
                    }
                }
            }
            try {
                long jCreateFile = WindowsNativeDispatcher.CreateFile(windowsPath.getPathForWin32Calls(), 1, 7, 3, 1107296256);
                try {
                    try {
                        WindowsFileAttributes attributes = WindowsFileAttributes.readAttributes(jCreateFile);
                        if (!attributes.isDirectory()) {
                            NotDirectoryException notDirectoryException = new NotDirectoryException(windowsPath.getPathForExceptionMessage());
                            if (0 == 0) {
                                WindowsNativeDispatcher.CloseHandle(jCreateFile);
                            }
                            return notDirectoryException;
                        }
                        FileKey fileKey = new FileKey(attributes.volSerialNumber(), attributes.fileIndexHigh(), attributes.fileIndexLow());
                        WindowsWatchKey windowsWatchKey = this.fk2key.get(fileKey);
                        if (windowsWatchKey != null && z2 == windowsWatchKey.watchSubtree()) {
                            windowsWatchKey.setEvents(set);
                            if (0 == 0) {
                                WindowsNativeDispatcher.CloseHandle(jCreateFile);
                            }
                            return windowsWatchKey;
                        }
                        int i2 = this.lastCompletionKey + 1;
                        this.lastCompletionKey = i2;
                        int i3 = i2;
                        if (i3 == 0) {
                            int i4 = this.lastCompletionKey + 1;
                            this.lastCompletionKey = i4;
                            i3 = i4;
                        }
                        try {
                            WindowsNativeDispatcher.CreateIoCompletionPort(jCreateFile, this.port, i3);
                            NativeBuffer nativeBuffer = NativeBuffers.getNativeBuffer(16420);
                            long jAddress = nativeBuffer.address();
                            long j2 = (jAddress + 16420) - 32;
                            long j3 = j2 - 4;
                            UNSAFE.setMemory(j2, 32L, (byte) 0);
                            try {
                                createAndAttachEvent(j2);
                                WindowsNativeDispatcher.ReadDirectoryChangesW(jCreateFile, jAddress, 16384, z2, WindowsWatchService.ALL_FILE_NOTIFY_EVENTS, j3, j2);
                                if (windowsWatchKey == null) {
                                    windowsWatchKeyInit = new WindowsWatchKey(windowsPath, this.watcher, fileKey).init(jCreateFile, set, z2, nativeBuffer, j3, j2, i3);
                                    this.fk2key.put(fileKey, windowsWatchKeyInit);
                                } else {
                                    this.ck2key.remove(Integer.valueOf(windowsWatchKey.completionKey()));
                                    releaseResources(windowsWatchKey);
                                    windowsWatchKeyInit = windowsWatchKey.init(jCreateFile, set, z2, nativeBuffer, j3, j2, i3);
                                }
                                this.ck2key.put(Integer.valueOf(i3), windowsWatchKeyInit);
                                WindowsWatchKey windowsWatchKey2 = windowsWatchKeyInit;
                                if (1 == 0) {
                                    WindowsNativeDispatcher.CloseHandle(jCreateFile);
                                }
                                return windowsWatchKey2;
                            } catch (WindowsException e2) {
                                closeAttachedEvent(j2);
                                nativeBuffer.release();
                                IOException iOException = new IOException(e2.getMessage());
                                if (0 == 0) {
                                    WindowsNativeDispatcher.CloseHandle(jCreateFile);
                                }
                                return iOException;
                            }
                        } catch (WindowsException e3) {
                            IOException iOException2 = new IOException(e3.getMessage());
                            if (0 == 0) {
                                WindowsNativeDispatcher.CloseHandle(jCreateFile);
                            }
                            return iOException2;
                        }
                    } catch (WindowsException e4) {
                        IOException iOExceptionAsIOException = e4.asIOException(windowsPath);
                        if (0 == 0) {
                            WindowsNativeDispatcher.CloseHandle(jCreateFile);
                        }
                        return iOExceptionAsIOException;
                    }
                } catch (Throwable th) {
                    if (0 == 0) {
                        WindowsNativeDispatcher.CloseHandle(jCreateFile);
                    }
                    throw th;
                }
            } catch (WindowsException e5) {
                return e5.asIOException(windowsPath);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void releaseResources(WindowsWatchKey windowsWatchKey) {
            if (!windowsWatchKey.isErrorStartingOverlapped()) {
                try {
                    WindowsNativeDispatcher.CancelIo(windowsWatchKey.handle());
                    WindowsNativeDispatcher.GetOverlappedResult(windowsWatchKey.handle(), windowsWatchKey.overlappedAddress());
                } catch (WindowsException e2) {
                }
            }
            WindowsNativeDispatcher.CloseHandle(windowsWatchKey.handle());
            closeAttachedEvent(windowsWatchKey.overlappedAddress());
            windowsWatchKey.buffer().cleaner().clean();
        }

        private void createAndAttachEvent(long j2) throws WindowsException {
            UNSAFE.putAddress(j2 + OFFSETOF_HEVENT, WindowsNativeDispatcher.CreateEvent(false, false));
        }

        private void closeAttachedEvent(long j2) {
            long address = UNSAFE.getAddress(j2 + OFFSETOF_HEVENT);
            if (address != 0 && address != -1) {
                WindowsNativeDispatcher.CloseHandle(address);
            }
        }

        @Override // sun.nio.fs.AbstractPoller
        void implCancelKey(WatchKey watchKey) {
            WindowsWatchKey windowsWatchKey = (WindowsWatchKey) watchKey;
            if (windowsWatchKey.isValid()) {
                this.fk2key.remove(windowsWatchKey.fileKey());
                this.ck2key.remove(Integer.valueOf(windowsWatchKey.completionKey()));
                windowsWatchKey.invalidate();
            }
        }

        @Override // sun.nio.fs.AbstractPoller
        void implCloseAll() {
            this.ck2key.values().forEach((v0) -> {
                v0.invalidate();
            });
            this.fk2key.clear();
            this.ck2key.clear();
            WindowsNativeDispatcher.CloseHandle(this.port);
        }

        private WatchEvent.Kind<?> translateActionToEvent(int i2) {
            switch (i2) {
                case 1:
                case 5:
                    return StandardWatchEventKinds.ENTRY_CREATE;
                case 2:
                case 4:
                    return StandardWatchEventKinds.ENTRY_DELETE;
                case 3:
                    return StandardWatchEventKinds.ENTRY_MODIFY;
                default:
                    return null;
            }
        }

        private void processEvents(WindowsWatchKey windowsWatchKey, int i2) {
            int i3;
            long jAddress = windowsWatchKey.buffer().address();
            do {
                WatchEvent.Kind<?> kindTranslateActionToEvent = translateActionToEvent(UNSAFE.getInt(jAddress + 4));
                if (windowsWatchKey.events().contains(kindTranslateActionToEvent)) {
                    int i4 = UNSAFE.getInt(jAddress + 8);
                    if (i4 % 2 != 0) {
                        throw new AssertionError((Object) "FileNameLength is not a multiple of 2");
                    }
                    char[] cArr = new char[i4 / 2];
                    UNSAFE.copyMemory(null, jAddress + 12, cArr, Unsafe.ARRAY_CHAR_BASE_OFFSET, i4);
                    windowsWatchKey.signalEvent(kindTranslateActionToEvent, WindowsPath.createFromNormalizedPath(this.fs, new String(cArr)));
                }
                i3 = UNSAFE.getInt(jAddress + 0);
                jAddress += i3;
            } while (i3 != 0);
        }

        @Override // java.lang.Runnable
        public void run() {
            while (true) {
                try {
                    WindowsNativeDispatcher.CompletionStatus completionStatusGetQueuedCompletionStatus = WindowsNativeDispatcher.GetQueuedCompletionStatus(this.port);
                    if (completionStatusGetQueuedCompletionStatus.completionKey() == 0) {
                        if (processRequests()) {
                            return;
                        }
                    } else {
                        WindowsWatchKey windowsWatchKey = this.ck2key.get(Integer.valueOf((int) completionStatusGetQueuedCompletionStatus.completionKey()));
                        if (windowsWatchKey != null) {
                            boolean z2 = false;
                            int iError = completionStatusGetQueuedCompletionStatus.error();
                            int iBytesTransferred = completionStatusGetQueuedCompletionStatus.bytesTransferred();
                            if (iError == 1022) {
                                windowsWatchKey.signalEvent(StandardWatchEventKinds.OVERFLOW, null);
                            } else if (iError != 0 && iError != 234) {
                                z2 = true;
                            } else {
                                if (iBytesTransferred > 0) {
                                    processEvents(windowsWatchKey, iBytesTransferred);
                                } else if (iError == 0) {
                                    windowsWatchKey.signalEvent(StandardWatchEventKinds.OVERFLOW, null);
                                }
                                try {
                                    WindowsNativeDispatcher.ReadDirectoryChangesW(windowsWatchKey.handle(), windowsWatchKey.buffer().address(), 16384, windowsWatchKey.watchSubtree(), WindowsWatchService.ALL_FILE_NOTIFY_EVENTS, windowsWatchKey.countAddress(), windowsWatchKey.overlappedAddress());
                                } catch (WindowsException e2) {
                                    z2 = true;
                                    windowsWatchKey.setErrorStartingOverlapped(true);
                                }
                            }
                            if (z2) {
                                implCancelKey(windowsWatchKey);
                                windowsWatchKey.signal();
                            }
                        }
                    }
                } catch (WindowsException e3) {
                    e3.printStackTrace();
                    return;
                }
            }
        }
    }
}
