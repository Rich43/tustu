package sun.nio.fs;

import java.io.IOException;
import java.nio.file.Path;
import java.security.AccessController;
import java.security.PrivilegedAction;

/* loaded from: rt.jar:sun/nio/fs/RegistryFileTypeDetector.class */
public class RegistryFileTypeDetector extends AbstractFileTypeDetector {
    private static native String queryStringValue(long j2, long j3);

    @Override // sun.nio.fs.AbstractFileTypeDetector
    public String implProbeContentType(Path path) throws IOException {
        Path fileName;
        String string;
        int iLastIndexOf;
        if (!(path instanceof Path) || (fileName = path.getFileName()) == null || (iLastIndexOf = (string = fileName.toString()).lastIndexOf(46)) < 0 || iLastIndexOf == string.length() - 1) {
            return null;
        }
        NativeBuffer nativeBufferAsNativeBuffer = null;
        NativeBuffer nativeBufferAsNativeBuffer2 = null;
        try {
            try {
                nativeBufferAsNativeBuffer = WindowsNativeDispatcher.asNativeBuffer(string.substring(iLastIndexOf));
                nativeBufferAsNativeBuffer2 = WindowsNativeDispatcher.asNativeBuffer("Content Type");
                String strQueryStringValue = queryStringValue(nativeBufferAsNativeBuffer.address(), nativeBufferAsNativeBuffer2.address());
                nativeBufferAsNativeBuffer2.release();
                nativeBufferAsNativeBuffer.release();
                return strQueryStringValue;
            } catch (WindowsException e2) {
                e2.rethrowAsIOException(path.toString());
                nativeBufferAsNativeBuffer2.release();
                nativeBufferAsNativeBuffer.release();
                return null;
            }
        } catch (Throwable th) {
            nativeBufferAsNativeBuffer2.release();
            nativeBufferAsNativeBuffer.release();
            throw th;
        }
    }

    static {
        AccessController.doPrivileged(new PrivilegedAction<Void>() { // from class: sun.nio.fs.RegistryFileTypeDetector.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.security.PrivilegedAction
            public Void run() {
                System.loadLibrary("net");
                System.loadLibrary("nio");
                return null;
            }
        });
    }
}
