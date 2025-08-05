package sun.nio.fs;

import com.sun.org.apache.xalan.internal.templates.Constants;
import java.io.IOException;
import java.nio.file.DirectoryIteratorException;
import java.nio.file.DirectoryStream;
import java.nio.file.NotDirectoryException;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Iterator;
import java.util.NoSuchElementException;
import javafx.fxml.FXMLLoader;
import sun.nio.fs.WindowsNativeDispatcher;

/* loaded from: rt.jar:sun/nio/fs/WindowsDirectoryStream.class */
class WindowsDirectoryStream implements DirectoryStream<Path> {
    private final WindowsPath dir;
    private final DirectoryStream.Filter<? super Path> filter;
    private final long handle;
    private final String firstName;
    private final NativeBuffer findDataBuffer;
    private final Object closeLock = new Object();
    private boolean isOpen = true;
    private Iterator<Path> iterator;

    WindowsDirectoryStream(WindowsPath windowsPath, DirectoryStream.Filter<? super Path> filter) throws IOException {
        String str;
        this.dir = windowsPath;
        this.filter = filter;
        try {
            String pathForWin32Calls = windowsPath.getPathForWin32Calls();
            char cCharAt = pathForWin32Calls.charAt(pathForWin32Calls.length() - 1);
            if (cCharAt == ':' || cCharAt == '\\') {
                str = pathForWin32Calls + "*";
            } else {
                str = pathForWin32Calls + "\\*";
            }
            WindowsNativeDispatcher.FirstFile firstFileFindFirstFile = WindowsNativeDispatcher.FindFirstFile(str);
            this.handle = firstFileFindFirstFile.handle();
            this.firstName = firstFileFindFirstFile.name();
            this.findDataBuffer = WindowsFileAttributes.getBufferForFindData();
        } catch (WindowsException e2) {
            if (e2.lastError() == 267) {
                throw new NotDirectoryException(windowsPath.getPathForExceptionMessage());
            }
            e2.rethrowAsIOException(windowsPath);
            throw new AssertionError();
        }
    }

    @Override // java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        synchronized (this.closeLock) {
            if (this.isOpen) {
                this.isOpen = false;
                this.findDataBuffer.release();
                try {
                    WindowsNativeDispatcher.FindClose(this.handle);
                } catch (WindowsException e2) {
                    e2.rethrowAsIOException(this.dir);
                }
            }
        }
    }

    @Override // java.nio.file.DirectoryStream, java.lang.Iterable, java.util.List
    public Iterator<Path> iterator() {
        Iterator<Path> it;
        if (!this.isOpen) {
            throw new IllegalStateException("Directory stream is closed");
        }
        synchronized (this) {
            if (this.iterator != null) {
                throw new IllegalStateException("Iterator already obtained");
            }
            this.iterator = new WindowsDirectoryIterator(this.firstName);
            it = this.iterator;
        }
        return it;
    }

    /* loaded from: rt.jar:sun/nio/fs/WindowsDirectoryStream$WindowsDirectoryIterator.class */
    private class WindowsDirectoryIterator implements Iterator<Path> {
        private boolean atEof = false;
        private String first;
        private Path nextEntry;
        private String prefix;

        WindowsDirectoryIterator(String str) {
            this.first = str;
            if (WindowsDirectoryStream.this.dir.needsSlashWhenResolving()) {
                this.prefix = WindowsDirectoryStream.this.dir.toString() + FXMLLoader.ESCAPE_PREFIX;
            } else {
                this.prefix = WindowsDirectoryStream.this.dir.toString();
            }
        }

        private boolean isSelfOrParent(String str) {
            return str.equals(".") || str.equals(Constants.ATTRVAL_PARENT);
        }

        private Path acceptEntry(String str, BasicFileAttributes basicFileAttributes) {
            WindowsPath windowsPathCreateFromNormalizedPath = WindowsPath.createFromNormalizedPath(WindowsDirectoryStream.this.dir.getFileSystem(), this.prefix + str, basicFileAttributes);
            try {
                if (WindowsDirectoryStream.this.filter.accept(windowsPathCreateFromNormalizedPath)) {
                    return windowsPathCreateFromNormalizedPath;
                }
                return null;
            } catch (IOException e2) {
                throw new DirectoryIteratorException(e2);
            }
        }

        /* JADX WARN: Code restructure failed: missing block: B:39:0x00b0, code lost:
        
            r0 = acceptEntry(r6, r0);
         */
        /* JADX WARN: Code restructure failed: missing block: B:40:0x00b8, code lost:
        
            if (r0 == null) goto L51;
         */
        /* JADX WARN: Code restructure failed: missing block: B:42:0x00bc, code lost:
        
            return r0;
         */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct code enable 'Show inconsistent code' option in preferences
        */
        private java.nio.file.Path readNextEntry() {
            /*
                r5 = this;
                r0 = r5
                java.lang.String r0 = r0.first
                if (r0 == 0) goto L34
                r0 = r5
                r1 = r5
                r2 = r5
                java.lang.String r2 = r2.first
                boolean r1 = r1.isSelfOrParent(r2)
                if (r1 == 0) goto L17
                r1 = 0
                goto L20
            L17:
                r1 = r5
                r2 = r5
                java.lang.String r2 = r2.first
                r3 = 0
                java.nio.file.Path r1 = r1.acceptEntry(r2, r3)
            L20:
                r0.nextEntry = r1
                r0 = r5
                r1 = 0
                r0.first = r1
                r0 = r5
                java.nio.file.Path r0 = r0.nextEntry
                if (r0 == 0) goto L34
                r0 = r5
                java.nio.file.Path r0 = r0.nextEntry
                return r0
            L34:
                r0 = 0
                r6 = r0
                r0 = r5
                sun.nio.fs.WindowsDirectoryStream r0 = sun.nio.fs.WindowsDirectoryStream.this
                java.lang.Object r0 = sun.nio.fs.WindowsDirectoryStream.access$200(r0)
                r1 = r0
                r8 = r1
                monitor-enter(r0)
                r0 = r5
                sun.nio.fs.WindowsDirectoryStream r0 = sun.nio.fs.WindowsDirectoryStream.this     // Catch: sun.nio.fs.WindowsException -> L62 java.lang.Throwable -> La9
                boolean r0 = sun.nio.fs.WindowsDirectoryStream.access$300(r0)     // Catch: sun.nio.fs.WindowsException -> L62 java.lang.Throwable -> La9
                if (r0 == 0) goto L5f
                r0 = r5
                sun.nio.fs.WindowsDirectoryStream r0 = sun.nio.fs.WindowsDirectoryStream.this     // Catch: sun.nio.fs.WindowsException -> L62 java.lang.Throwable -> La9
                long r0 = sun.nio.fs.WindowsDirectoryStream.access$400(r0)     // Catch: sun.nio.fs.WindowsException -> L62 java.lang.Throwable -> La9
                r1 = r5
                sun.nio.fs.WindowsDirectoryStream r1 = sun.nio.fs.WindowsDirectoryStream.this     // Catch: sun.nio.fs.WindowsException -> L62 java.lang.Throwable -> La9
                sun.nio.fs.NativeBuffer r1 = sun.nio.fs.WindowsDirectoryStream.access$500(r1)     // Catch: sun.nio.fs.WindowsException -> L62 java.lang.Throwable -> La9
                long r1 = r1.address()     // Catch: sun.nio.fs.WindowsException -> L62 java.lang.Throwable -> La9
                java.lang.String r0 = sun.nio.fs.WindowsNativeDispatcher.FindNextFile(r0, r1)     // Catch: sun.nio.fs.WindowsException -> L62 java.lang.Throwable -> La9
                r6 = r0
            L5f:
                goto L7c
            L62:
                r9 = move-exception
                r0 = r9
                r1 = r5
                sun.nio.fs.WindowsDirectoryStream r1 = sun.nio.fs.WindowsDirectoryStream.this     // Catch: java.lang.Throwable -> La9
                sun.nio.fs.WindowsPath r1 = sun.nio.fs.WindowsDirectoryStream.access$000(r1)     // Catch: java.lang.Throwable -> La9
                java.io.IOException r0 = r0.asIOException(r1)     // Catch: java.lang.Throwable -> La9
                r10 = r0
                java.nio.file.DirectoryIteratorException r0 = new java.nio.file.DirectoryIteratorException     // Catch: java.lang.Throwable -> La9
                r1 = r0
                r2 = r10
                r1.<init>(r2)     // Catch: java.lang.Throwable -> La9
                throw r0     // Catch: java.lang.Throwable -> La9
            L7c:
                r0 = r6
                if (r0 != 0) goto L89
                r0 = r5
                r1 = 1
                r0.atEof = r1     // Catch: java.lang.Throwable -> La9
                r0 = 0
                r1 = r8
                monitor-exit(r1)     // Catch: java.lang.Throwable -> La9
                return r0
            L89:
                r0 = r5
                r1 = r6
                boolean r0 = r0.isSelfOrParent(r1)     // Catch: java.lang.Throwable -> La9
                if (r0 == 0) goto L96
                r0 = r8
                monitor-exit(r0)     // Catch: java.lang.Throwable -> La9
                goto L34
            L96:
                r0 = r5
                sun.nio.fs.WindowsDirectoryStream r0 = sun.nio.fs.WindowsDirectoryStream.this     // Catch: java.lang.Throwable -> La9
                sun.nio.fs.NativeBuffer r0 = sun.nio.fs.WindowsDirectoryStream.access$500(r0)     // Catch: java.lang.Throwable -> La9
                long r0 = r0.address()     // Catch: java.lang.Throwable -> La9
                sun.nio.fs.WindowsFileAttributes r0 = sun.nio.fs.WindowsFileAttributes.fromFindData(r0)     // Catch: java.lang.Throwable -> La9
                r7 = r0
                r0 = r8
                monitor-exit(r0)     // Catch: java.lang.Throwable -> La9
                goto Lb0
            La9:
                r11 = move-exception
                r0 = r8
                monitor-exit(r0)     // Catch: java.lang.Throwable -> La9
                r0 = r11
                throw r0
            Lb0:
                r0 = r5
                r1 = r6
                r2 = r7
                java.nio.file.Path r0 = r0.acceptEntry(r1, r2)
                r8 = r0
                r0 = r8
                if (r0 == 0) goto Lbd
                r0 = r8
                return r0
            Lbd:
                goto L34
            */
            throw new UnsupportedOperationException("Method not decompiled: sun.nio.fs.WindowsDirectoryStream.WindowsDirectoryIterator.readNextEntry():java.nio.file.Path");
        }

        @Override // java.util.Iterator
        public synchronized boolean hasNext() {
            if (this.nextEntry == null && !this.atEof) {
                this.nextEntry = readNextEntry();
            }
            return this.nextEntry != null;
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // java.util.Iterator
        public synchronized Path next() {
            Path nextEntry;
            if (this.nextEntry == null && !this.atEof) {
                nextEntry = readNextEntry();
            } else {
                nextEntry = this.nextEntry;
                this.nextEntry = null;
            }
            if (nextEntry == null) {
                throw new NoSuchElementException();
            }
            return nextEntry;
        }

        @Override // java.util.Iterator
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }
}
