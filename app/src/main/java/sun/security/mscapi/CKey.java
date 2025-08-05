package sun.security.mscapi;

import java.math.BigInteger;
import java.security.Key;
import java.security.interfaces.ECPrivateKey;
import java.security.interfaces.ECPublicKey;
import sun.security.util.KeyUtil;
import sun.security.util.Length;

/* loaded from: sunmscapi.jar:sun/security/mscapi/CKey.class */
abstract class CKey implements Key, Length {
    private static final long serialVersionUID = -1088859394025049194L;
    protected final NativeHandles handles;
    protected final int keyLength;
    protected final String algorithm;

    /* JADX INFO: Access modifiers changed from: private */
    public static native void cleanUp(long j2, long j3);

    protected static native String getContainerName(long j2);

    protected static native String getKeyType(long j2);

    /* loaded from: sunmscapi.jar:sun/security/mscapi/CKey$NativeHandles.class */
    static class NativeHandles {
        long hCryptProv;
        long hCryptKey;

        public NativeHandles(long j2, long j3) {
            this.hCryptProv = 0L;
            this.hCryptKey = 0L;
            this.hCryptProv = j2;
            this.hCryptKey = j3;
        }

        protected void finalize() throws Throwable {
            try {
                synchronized (this) {
                    CKey.cleanUp(this.hCryptProv, this.hCryptKey);
                    this.hCryptProv = 0L;
                    this.hCryptKey = 0L;
                }
            } finally {
                super.finalize();
            }
        }
    }

    protected CKey(String str, NativeHandles nativeHandles, int i2) {
        this.algorithm = str;
        this.handles = nativeHandles;
        this.keyLength = i2;
    }

    @Override // sun.security.util.Length
    public int length() {
        return this.keyLength;
    }

    public long getHCryptKey() {
        return this.handles.hCryptKey;
    }

    public long getHCryptProvider() {
        return this.handles.hCryptProv;
    }

    @Override // java.security.Key
    public String getAlgorithm() {
        return this.algorithm;
    }

    static byte[] generateECBlob(Key key) {
        int keySize = KeyUtil.getKeySize(key);
        int i2 = (keySize + 7) / 8;
        boolean z2 = key instanceof ECPrivateKey;
        byte[] bArr = new byte[8 + (i2 * (z2 ? 3 : 2))];
        bArr[0] = 69;
        bArr[1] = 67;
        bArr[2] = 83;
        if (z2) {
            bArr[3] = (byte) (keySize == 256 ? 50 : keySize == 384 ? 52 : 54);
        } else {
            bArr[3] = (byte) (keySize == 256 ? 49 : keySize == 384 ? 51 : 53);
        }
        if (z2) {
            byte[] byteArray = ((ECPrivateKey) key).getS().toByteArray();
            System.arraycopy(byteArray, 0, bArr, (((8 + i2) + i2) + i2) - byteArray.length, byteArray.length);
        } else {
            ECPublicKey eCPublicKey = (ECPublicKey) key;
            BigInteger affineX = eCPublicKey.getW().getAffineX();
            byte[] byteArray2 = eCPublicKey.getW().getAffineY().toByteArray();
            System.arraycopy(byteArray2, 0, bArr, ((8 + i2) + i2) - byteArray2.length, byteArray2.length);
            byte[] byteArray3 = affineX.toByteArray();
            System.arraycopy(byteArray3, 0, bArr, (8 + i2) - byteArray3.length, byteArray3.length);
        }
        bArr[4] = (byte) i2;
        bArr[7] = 0;
        bArr[6] = 0;
        bArr[5] = 0;
        return bArr;
    }
}
