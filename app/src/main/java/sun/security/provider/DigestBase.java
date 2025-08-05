package sun.security.provider;

import java.security.DigestException;
import java.security.MessageDigestSpi;
import java.security.ProviderException;
import java.util.Arrays;
import java.util.Objects;

/* loaded from: rt.jar:sun/security/provider/DigestBase.class */
abstract class DigestBase extends MessageDigestSpi implements Cloneable {
    private byte[] oneByte;
    private final String algorithm;
    private final int digestLength;
    private final int blockSize;
    byte[] buffer;
    private int bufOfs;
    long bytesProcessed;
    static final byte[] padding = new byte[136];

    abstract void implCompress(byte[] bArr, int i2);

    abstract void implDigest(byte[] bArr, int i2);

    abstract void implReset();

    DigestBase(String str, int i2, int i3) {
        this.algorithm = str;
        this.digestLength = i2;
        this.blockSize = i3;
        this.buffer = new byte[i3];
    }

    @Override // java.security.MessageDigestSpi
    protected final int engineGetDigestLength() {
        return this.digestLength;
    }

    @Override // java.security.MessageDigestSpi
    protected final void engineUpdate(byte b2) {
        if (this.oneByte == null) {
            this.oneByte = new byte[1];
        }
        this.oneByte[0] = b2;
        engineUpdate(this.oneByte, 0, 1);
    }

    @Override // java.security.MessageDigestSpi
    protected final void engineUpdate(byte[] bArr, int i2, int i3) {
        if (i3 == 0) {
            return;
        }
        if (i2 < 0 || i3 < 0 || i2 > bArr.length - i3) {
            throw new ArrayIndexOutOfBoundsException();
        }
        if (this.bytesProcessed < 0) {
            engineReset();
        }
        this.bytesProcessed += i3;
        if (this.bufOfs != 0) {
            int iMin = Math.min(i3, this.blockSize - this.bufOfs);
            System.arraycopy(bArr, i2, this.buffer, this.bufOfs, iMin);
            this.bufOfs += iMin;
            i2 += iMin;
            i3 -= iMin;
            if (this.bufOfs >= this.blockSize) {
                implCompress(this.buffer, 0);
                this.bufOfs = 0;
            }
        }
        if (i3 >= this.blockSize) {
            int i4 = i2 + i3;
            i2 = implCompressMultiBlock(bArr, i2, i4 - this.blockSize);
            i3 = i4 - i2;
        }
        if (i3 > 0) {
            System.arraycopy(bArr, i2, this.buffer, 0, i3);
            this.bufOfs = i3;
        }
    }

    private int implCompressMultiBlock(byte[] bArr, int i2, int i3) {
        implCompressMultiBlockCheck(bArr, i2, i3);
        return implCompressMultiBlock0(bArr, i2, i3);
    }

    private int implCompressMultiBlock0(byte[] bArr, int i2, int i3) {
        while (i2 <= i3) {
            implCompress(bArr, i2);
            i2 += this.blockSize;
        }
        return i2;
    }

    private void implCompressMultiBlockCheck(byte[] bArr, int i2, int i3) {
        if (i3 < 0) {
            return;
        }
        Objects.requireNonNull(bArr);
        if (i2 < 0 || i2 >= bArr.length) {
            throw new ArrayIndexOutOfBoundsException(i2);
        }
        int i4 = (((i3 / this.blockSize) * this.blockSize) + this.blockSize) - 1;
        if (i4 >= bArr.length) {
            throw new ArrayIndexOutOfBoundsException(i4);
        }
    }

    @Override // java.security.MessageDigestSpi
    protected final void engineReset() {
        if (this.bytesProcessed == 0) {
            return;
        }
        implReset();
        this.bufOfs = 0;
        this.bytesProcessed = 0L;
        Arrays.fill(this.buffer, (byte) 0);
    }

    @Override // java.security.MessageDigestSpi
    protected final byte[] engineDigest() {
        byte[] bArr = new byte[this.digestLength];
        try {
            engineDigest(bArr, 0, bArr.length);
            return bArr;
        } catch (DigestException e2) {
            throw ((ProviderException) new ProviderException("Internal error").initCause(e2));
        }
    }

    @Override // java.security.MessageDigestSpi
    protected final int engineDigest(byte[] bArr, int i2, int i3) throws DigestException {
        if (i3 < this.digestLength) {
            throw new DigestException("Length must be at least " + this.digestLength + " for " + this.algorithm + "digests");
        }
        if (i2 < 0 || i3 < 0 || i2 > bArr.length - i3) {
            throw new DigestException("Buffer too short to store digest");
        }
        if (this.bytesProcessed < 0) {
            engineReset();
        }
        implDigest(bArr, i2);
        this.bytesProcessed = -1L;
        return this.digestLength;
    }

    @Override // java.security.MessageDigestSpi
    public Object clone() throws CloneNotSupportedException {
        DigestBase digestBase = (DigestBase) super.clone();
        digestBase.buffer = (byte[]) digestBase.buffer.clone();
        return digestBase;
    }

    static {
        padding[0] = Byte.MIN_VALUE;
    }
}
