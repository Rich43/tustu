package sun.security.pkcs11;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.security.NoSuchAlgorithmException;
import java.security.ProviderException;
import java.security.SecureRandom;
import java.security.SecureRandomSpi;
import sun.security.pkcs11.wrapper.PKCS11Exception;

/* loaded from: sunpkcs11.jar:sun/security/pkcs11/P11SecureRandom.class */
final class P11SecureRandom extends SecureRandomSpi {
    private static final long serialVersionUID = -8939510236124553291L;
    private final Token token;
    private volatile SecureRandom mixRandom;
    private byte[] mixBuffer;
    private int buffered;
    private static final long MAX_IBUFFER_TIME = 100;
    private static final int IBUFFER_SIZE = 32;
    private transient byte[] iBuffer = new byte[32];
    private transient int ibuffered = 0;
    private transient long lastRead = 0;

    P11SecureRandom(Token token) {
        this.token = token;
    }

    @Override // java.security.SecureRandomSpi
    protected synchronized void engineSetSeed(byte[] bArr) {
        if (bArr == null) {
            throw new NullPointerException("seed must not be null");
        }
        Session opSession = null;
        try {
            try {
                opSession = this.token.getOpSession();
                this.token.p11.C_SeedRandom(opSession.id(), bArr);
                this.token.releaseSession(opSession);
            } catch (PKCS11Exception e2) {
                SecureRandom secureRandom = this.mixRandom;
                if (secureRandom != null) {
                    secureRandom.setSeed(bArr);
                } else {
                    try {
                        this.mixBuffer = new byte[20];
                        SecureRandom secureRandom2 = SecureRandom.getInstance("SHA1PRNG");
                        secureRandom2.setSeed(bArr);
                        this.mixRandom = secureRandom2;
                    } catch (NoSuchAlgorithmException e3) {
                        throw new ProviderException(e3);
                    }
                }
                this.token.releaseSession(opSession);
            }
        } catch (Throwable th) {
            this.token.releaseSession(opSession);
            throw th;
        }
    }

    @Override // java.security.SecureRandomSpi
    protected void engineNextBytes(byte[] bArr) {
        if (bArr == null || bArr.length == 0) {
            return;
        }
        if (bArr.length <= 32) {
            int i2 = 0;
            synchronized (this.iBuffer) {
                while (i2 < bArr.length) {
                    long jCurrentTimeMillis = System.currentTimeMillis();
                    if (this.ibuffered == 0 || jCurrentTimeMillis - this.lastRead >= 100) {
                        this.lastRead = jCurrentTimeMillis;
                        implNextBytes(this.iBuffer);
                        this.ibuffered = 32;
                    }
                    while (i2 < bArr.length && this.ibuffered > 0) {
                        int i3 = i2;
                        i2++;
                        byte[] bArr2 = this.iBuffer;
                        int i4 = this.ibuffered;
                        this.ibuffered = i4 - 1;
                        bArr[i3] = bArr2[32 - i4];
                    }
                }
            }
            return;
        }
        implNextBytes(bArr);
    }

    @Override // java.security.SecureRandomSpi
    protected byte[] engineGenerateSeed(int i2) {
        byte[] bArr = new byte[i2];
        engineNextBytes(bArr);
        return bArr;
    }

    private void mix(byte[] bArr) {
        SecureRandom secureRandom = this.mixRandom;
        if (secureRandom == null) {
            return;
        }
        synchronized (this) {
            int i2 = 0;
            int length = bArr.length;
            while (true) {
                int i3 = length;
                length--;
                if (i3 > 0) {
                    if (this.buffered == 0) {
                        secureRandom.nextBytes(this.mixBuffer);
                        this.buffered = this.mixBuffer.length;
                    }
                    int i4 = i2;
                    i2++;
                    bArr[i4] = (byte) (bArr[i4] ^ this.mixBuffer[this.mixBuffer.length - this.buffered]);
                    this.buffered--;
                }
            }
        }
    }

    private void implNextBytes(byte[] bArr) {
        Session opSession = null;
        try {
            try {
                opSession = this.token.getOpSession();
                this.token.p11.C_GenerateRandom(opSession.id(), bArr);
                mix(bArr);
                this.token.releaseSession(opSession);
            } catch (PKCS11Exception e2) {
                throw new ProviderException("nextBytes() failed", e2);
            }
        } catch (Throwable th) {
            this.token.releaseSession(opSession);
            throw th;
        }
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        objectInputStream.defaultReadObject();
        this.iBuffer = new byte[32];
        this.ibuffered = 0;
        this.lastRead = 0L;
    }
}
