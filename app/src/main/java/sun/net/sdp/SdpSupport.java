package sun.net.sdp;

import java.io.FileDescriptor;
import java.io.IOException;
import java.security.AccessController;
import java.security.PrivilegedAction;
import sun.misc.JavaIOFileDescriptorAccess;
import sun.misc.SharedSecrets;
import sun.security.action.GetPropertyAction;

/* loaded from: rt.jar:sun/net/sdp/SdpSupport.class */
public final class SdpSupport {
    private static final String os = (String) AccessController.doPrivileged(new GetPropertyAction("os.name"));
    private static final boolean isSupported;
    private static final JavaIOFileDescriptorAccess fdAccess;

    private static native int create0() throws IOException;

    private static native void convert0(int i2) throws IOException;

    static {
        isSupported = os.equals("SunOS") || os.equals("Linux");
        fdAccess = SharedSecrets.getJavaIOFileDescriptorAccess();
        AccessController.doPrivileged(new PrivilegedAction<Void>() { // from class: sun.net.sdp.SdpSupport.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.security.PrivilegedAction
            /* renamed from: run */
            public Void run2() {
                System.loadLibrary("net");
                return null;
            }
        });
    }

    private SdpSupport() {
    }

    public static FileDescriptor createSocket() throws IOException {
        if (!isSupported) {
            throw new UnsupportedOperationException("SDP not supported on this platform");
        }
        int iCreate0 = create0();
        FileDescriptor fileDescriptor = new FileDescriptor();
        fdAccess.set(fileDescriptor, iCreate0);
        return fileDescriptor;
    }

    public static void convertSocket(FileDescriptor fileDescriptor) throws IOException {
        if (!isSupported) {
            throw new UnsupportedOperationException("SDP not supported on this platform");
        }
        convert0(fdAccess.get(fileDescriptor));
    }
}
