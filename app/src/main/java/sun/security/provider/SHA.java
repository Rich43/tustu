package sun.security.provider;

import java.util.Arrays;
import java.util.Objects;

/* loaded from: rt.jar:sun/security/provider/SHA.class */
public final class SHA extends DigestBase {

    /* renamed from: W, reason: collision with root package name */
    private int[] f13640W;
    private int[] state;
    private static final int round1_kt = 1518500249;
    private static final int round2_kt = 1859775393;
    private static final int round3_kt = -1894007588;
    private static final int round4_kt = -899497514;

    public SHA() {
        super("SHA-1", 20, 64);
        this.state = new int[5];
        this.f13640W = new int[80];
        resetHashes();
    }

    @Override // sun.security.provider.DigestBase, java.security.MessageDigestSpi
    public Object clone() throws CloneNotSupportedException {
        SHA sha = (SHA) super.clone();
        sha.state = (int[]) sha.state.clone();
        sha.f13640W = new int[80];
        return sha;
    }

    @Override // sun.security.provider.DigestBase
    void implReset() {
        resetHashes();
        Arrays.fill(this.f13640W, 0);
    }

    private void resetHashes() {
        this.state[0] = 1732584193;
        this.state[1] = -271733879;
        this.state[2] = -1732584194;
        this.state[3] = 271733878;
        this.state[4] = -1009589776;
    }

    @Override // sun.security.provider.DigestBase
    void implDigest(byte[] bArr, int i2) {
        long j2 = this.bytesProcessed << 3;
        int i3 = ((int) this.bytesProcessed) & 63;
        engineUpdate(padding, 0, i3 < 56 ? 56 - i3 : 120 - i3);
        ByteArrayAccess.i2bBig4((int) (j2 >>> 32), this.buffer, 56);
        ByteArrayAccess.i2bBig4((int) j2, this.buffer, 60);
        implCompress(this.buffer, 0);
        ByteArrayAccess.i2bBig(this.state, 0, bArr, i2, 20);
    }

    @Override // sun.security.provider.DigestBase
    void implCompress(byte[] bArr, int i2) {
        implCompressCheck(bArr, i2);
        implCompress0(bArr, i2);
    }

    private void implCompressCheck(byte[] bArr, int i2) {
        Objects.requireNonNull(bArr);
        ByteArrayAccess.b2iBig64(bArr, i2, this.f13640W);
    }

    private void implCompress0(byte[] bArr, int i2) {
        for (int i3 = 16; i3 <= 79; i3++) {
            int i4 = ((this.f13640W[i3 - 3] ^ this.f13640W[i3 - 8]) ^ this.f13640W[i3 - 14]) ^ this.f13640W[i3 - 16];
            this.f13640W[i3] = (i4 << 1) | (i4 >>> 31);
        }
        int i5 = this.state[0];
        int i6 = this.state[1];
        int i7 = this.state[2];
        int i8 = this.state[3];
        int i9 = this.state[4];
        for (int i10 = 0; i10 < 20; i10++) {
            int i11 = ((i5 << 5) | (i5 >>> 27)) + ((i6 & i7) | ((i6 ^ (-1)) & i8)) + i9 + this.f13640W[i10] + round1_kt;
            i9 = i8;
            i8 = i7;
            i7 = (i6 << 30) | (i6 >>> 2);
            i6 = i5;
            i5 = i11;
        }
        for (int i12 = 20; i12 < 40; i12++) {
            int i13 = ((i5 << 5) | (i5 >>> 27)) + ((i6 ^ i7) ^ i8) + i9 + this.f13640W[i12] + round2_kt;
            i9 = i8;
            i8 = i7;
            i7 = (i6 << 30) | (i6 >>> 2);
            i6 = i5;
            i5 = i13;
        }
        for (int i14 = 40; i14 < 60; i14++) {
            int i15 = ((i5 << 5) | (i5 >>> 27)) + ((i6 & i7) | (i6 & i8) | (i7 & i8)) + i9 + this.f13640W[i14] + round3_kt;
            i9 = i8;
            i8 = i7;
            i7 = (i6 << 30) | (i6 >>> 2);
            i6 = i5;
            i5 = i15;
        }
        for (int i16 = 60; i16 < 80; i16++) {
            int i17 = ((i5 << 5) | (i5 >>> 27)) + ((i6 ^ i7) ^ i8) + i9 + this.f13640W[i16] + round4_kt;
            i9 = i8;
            i8 = i7;
            i7 = (i6 << 30) | (i6 >>> 2);
            i6 = i5;
            i5 = i17;
        }
        int[] iArr = this.state;
        iArr[0] = iArr[0] + i5;
        int[] iArr2 = this.state;
        iArr2[1] = iArr2[1] + i6;
        int[] iArr3 = this.state;
        iArr3[2] = iArr3[2] + i7;
        int[] iArr4 = this.state;
        iArr4[3] = iArr4[3] + i8;
        int[] iArr5 = this.state;
        iArr5[4] = iArr5[4] + i9;
    }
}
