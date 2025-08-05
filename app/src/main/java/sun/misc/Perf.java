package sun.misc;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.security.PrivilegedAction;
import net.lingala.zip4j.util.InternalZipConstants;

/* loaded from: rt.jar:sun/misc/Perf.class */
public final class Perf {
    private static Perf instance;
    private static final int PERF_MODE_RO = 0;
    private static final int PERF_MODE_RW = 1;

    private native ByteBuffer attach(String str, int i2, int i3) throws IOException, IllegalArgumentException;

    /* JADX INFO: Access modifiers changed from: private */
    public native void detach(ByteBuffer byteBuffer);

    public native ByteBuffer createLong(String str, int i2, int i3, long j2);

    public native ByteBuffer createByteArray(String str, int i2, int i3, byte[] bArr, int i4);

    public native long highResCounter();

    public native long highResFrequency();

    private static native void registerNatives();

    private Perf() {
    }

    /* loaded from: rt.jar:sun/misc/Perf$GetPerfAction.class */
    public static class GetPerfAction implements PrivilegedAction<Perf> {
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // java.security.PrivilegedAction
        /* renamed from: run */
        public Perf run2() {
            return Perf.getPerf();
        }
    }

    public static Perf getPerf() {
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            securityManager.checkPermission(new RuntimePermission("sun.misc.Perf.getPerf"));
        }
        return instance;
    }

    public ByteBuffer attach(int i2, String str) throws IOException, IllegalArgumentException {
        if (str.compareTo(InternalZipConstants.READ_MODE) == 0) {
            return attachImpl(null, i2, 0);
        }
        if (str.compareTo(InternalZipConstants.WRITE_MODE) == 0) {
            return attachImpl(null, i2, 1);
        }
        throw new IllegalArgumentException("unknown mode");
    }

    public ByteBuffer attach(String str, int i2, String str2) throws IOException, IllegalArgumentException {
        if (str2.compareTo(InternalZipConstants.READ_MODE) == 0) {
            return attachImpl(str, i2, 0);
        }
        if (str2.compareTo(InternalZipConstants.WRITE_MODE) == 0) {
            return attachImpl(str, i2, 1);
        }
        throw new IllegalArgumentException("unknown mode");
    }

    private ByteBuffer attachImpl(String str, int i2, int i3) throws IOException, IllegalArgumentException {
        final ByteBuffer byteBufferAttach = attach(str, i2, i3);
        if (i2 == 0) {
            return byteBufferAttach;
        }
        ByteBuffer byteBufferDuplicate = byteBufferAttach.duplicate();
        Cleaner.create(byteBufferDuplicate, new Runnable() { // from class: sun.misc.Perf.1
            static final /* synthetic */ boolean $assertionsDisabled;

            static {
                $assertionsDisabled = !Perf.class.desiredAssertionStatus();
            }

            @Override // java.lang.Runnable
            public void run() {
                boolean z2;
                AssertionError assertionError;
                try {
                    Perf.instance.detach(byteBufferAttach);
                } finally {
                    if (!z2) {
                    }
                }
            }
        });
        return byteBufferDuplicate;
    }

    public ByteBuffer createString(String str, int i2, int i3, String str2, int i4) {
        byte[] bytes = getBytes(str2);
        byte[] bArr = new byte[bytes.length + 1];
        System.arraycopy(bytes, 0, bArr, 0, bytes.length);
        bArr[bytes.length] = 0;
        return createByteArray(str, i2, i3, bArr, Math.max(bArr.length, i4));
    }

    public ByteBuffer createString(String str, int i2, int i3, String str2) {
        byte[] bytes = getBytes(str2);
        byte[] bArr = new byte[bytes.length + 1];
        System.arraycopy(bytes, 0, bArr, 0, bytes.length);
        bArr[bytes.length] = 0;
        return createByteArray(str, i2, i3, bArr, bArr.length);
    }

    private static byte[] getBytes(String str) {
        byte[] bytes = null;
        try {
            bytes = str.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e2) {
        }
        return bytes;
    }

    static {
        registerNatives();
        instance = new Perf();
    }
}
