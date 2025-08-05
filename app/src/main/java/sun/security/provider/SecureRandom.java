package sun.security.provider;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandomSpi;

/* loaded from: rt.jar:sun/security/provider/SecureRandom.class */
public final class SecureRandom extends SecureRandomSpi implements Serializable {
    private static final long serialVersionUID = 3581829991155417889L;
    private static final int DIGEST_SIZE = 20;
    private transient MessageDigest digest;
    private byte[] state;
    private byte[] remainder;
    private int remCount;

    public SecureRandom() {
        init(null);
    }

    private SecureRandom(byte[] bArr) {
        init(bArr);
    }

    private void init(byte[] bArr) {
        try {
            this.digest = MessageDigest.getInstance("SHA", "SUN");
        } catch (NoSuchAlgorithmException | NoSuchProviderException e2) {
            try {
                this.digest = MessageDigest.getInstance("SHA");
            } catch (NoSuchAlgorithmException e3) {
                throw new InternalError("internal error: SHA-1 not available.", e3);
            }
        }
        if (bArr != null) {
            engineSetSeed(bArr);
        }
    }

    @Override // java.security.SecureRandomSpi
    public byte[] engineGenerateSeed(int i2) {
        byte[] bArr = new byte[i2];
        SeedGenerator.generateSeed(bArr);
        return bArr;
    }

    @Override // java.security.SecureRandomSpi
    public synchronized void engineSetSeed(byte[] bArr) {
        if (this.state != null) {
            this.digest.update(this.state);
            for (int i2 = 0; i2 < this.state.length; i2++) {
                this.state[i2] = 0;
            }
        }
        this.state = this.digest.digest(bArr);
    }

    private static void updateState(byte[] bArr, byte[] bArr2) {
        int i2 = 1;
        boolean z2 = false;
        for (int i3 = 0; i3 < bArr.length; i3++) {
            int i4 = bArr[i3] + bArr2[i3] + i2;
            byte b2 = (byte) i4;
            z2 |= bArr[i3] != b2;
            bArr[i3] = b2;
            i2 = i4 >> 8;
        }
        if (!z2) {
            bArr[0] = (byte) (bArr[0] + 1);
        }
    }

    /* loaded from: rt.jar:sun/security/provider/SecureRandom$SeederHolder.class */
    private static class SeederHolder {
        private static final SecureRandom seeder = new SecureRandom(SeedGenerator.getSystemEntropy());

        private SeederHolder() {
        }

        static {
            byte[] bArr = new byte[20];
            SeedGenerator.generateSeed(bArr);
            seeder.engineSetSeed(bArr);
        }
    }

    @Override // java.security.SecureRandomSpi
    public synchronized void engineNextBytes(byte[] bArr) {
        int i2 = 0;
        byte[] bArrDigest = this.remainder;
        if (this.state == null) {
            byte[] bArr2 = new byte[20];
            SeederHolder.seeder.engineNextBytes(bArr2);
            this.state = this.digest.digest(bArr2);
        }
        int i3 = this.remCount;
        if (i3 > 0) {
            int length = bArr.length - 0 < 20 - i3 ? bArr.length - 0 : 20 - i3;
            for (int i4 = 0; i4 < length; i4++) {
                bArr[i4] = bArrDigest[i3];
                int i5 = i3;
                i3++;
                bArrDigest[i5] = 0;
            }
            this.remCount += length;
            i2 = 0 + length;
        }
        while (i2 < bArr.length) {
            this.digest.update(this.state);
            bArrDigest = this.digest.digest();
            updateState(this.state, bArrDigest);
            int length2 = bArr.length - i2 > 20 ? 20 : bArr.length - i2;
            for (int i6 = 0; i6 < length2; i6++) {
                int i7 = i2;
                i2++;
                bArr[i7] = bArrDigest[i6];
                bArrDigest[i6] = 0;
            }
            this.remCount += length2;
        }
        this.remainder = bArrDigest;
        this.remCount %= 20;
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        objectInputStream.defaultReadObject();
        try {
            this.digest = MessageDigest.getInstance("SHA", "SUN");
        } catch (NoSuchAlgorithmException | NoSuchProviderException e2) {
            try {
                this.digest = MessageDigest.getInstance("SHA");
            } catch (NoSuchAlgorithmException e3) {
                throw new InternalError("internal error: SHA-1 not available.", e3);
            }
        }
    }
}
