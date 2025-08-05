package sun.nio.fs;

import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.FileSystemException;
import java.nio.file.NoSuchFileException;

/* loaded from: rt.jar:sun/nio/fs/WindowsException.class */
class WindowsException extends Exception {
    static final long serialVersionUID = 2765039493083748820L;
    private int lastError;
    private String msg;

    WindowsException(int i2) {
        this.lastError = i2;
        this.msg = null;
    }

    WindowsException(String str) {
        this.lastError = 0;
        this.msg = str;
    }

    int lastError() {
        return this.lastError;
    }

    String errorString() {
        if (this.msg == null) {
            this.msg = WindowsNativeDispatcher.FormatMessage(this.lastError);
            if (this.msg == null) {
                this.msg = "Unknown error: 0x" + Integer.toHexString(this.lastError);
            }
        }
        return this.msg;
    }

    @Override // java.lang.Throwable
    public String getMessage() {
        return errorString();
    }

    private IOException translateToIOException(String str, String str2) {
        if (lastError() == 0) {
            return new IOException(errorString());
        }
        if (lastError() == 2 || lastError() == 3) {
            return new NoSuchFileException(str, str2, null);
        }
        if (lastError() == 80 || lastError() == 183) {
            return new FileAlreadyExistsException(str, str2, null);
        }
        if (lastError() == 5) {
            return new AccessDeniedException(str, str2, null);
        }
        return new FileSystemException(str, str2, errorString());
    }

    void rethrowAsIOException(String str) throws IOException {
        throw translateToIOException(str, null);
    }

    void rethrowAsIOException(WindowsPath windowsPath, WindowsPath windowsPath2) throws IOException {
        throw translateToIOException(windowsPath == null ? null : windowsPath.getPathForExceptionMessage(), windowsPath2 == null ? null : windowsPath2.getPathForExceptionMessage());
    }

    void rethrowAsIOException(WindowsPath windowsPath) throws IOException {
        rethrowAsIOException(windowsPath, null);
    }

    IOException asIOException(WindowsPath windowsPath) {
        return translateToIOException(windowsPath.getPathForExceptionMessage(), null);
    }
}
