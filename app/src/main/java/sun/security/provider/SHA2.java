package sun.security.provider;

import java.util.Arrays;
import java.util.Objects;

/* loaded from: rt.jar:sun/security/provider/SHA2.class */
abstract class SHA2 extends DigestBase {
    private static final int ITERATION = 64;
    private static final int[] ROUND_CONSTS = {1116352408, 1899447441, -1245643825, -373957723, 961987163, 1508970993, -1841331548, -1424204075, -670586216, 310598401, 607225278, 1426881987, 1925078388, -2132889090, -1680079193, -1046744716, -459576895, -272742522, 264347078, 604807628, 770255983, 1249150122, 1555081692, 1996064986, -1740746414, -1473132947, -1341970488, -1084653625, -958395405, -710438585, 113926993, 338241895, 666307205, 773529912, 1294757372, 1396182291, 1695183700, 1986661051, -2117940946, -1838011259, -1564481375, -1474664885, -1035236496, -949202525, -778901479, -694614492, -200395387, 275423344, 430227734, 506948616, 659060556, 883997877, 958139571, 1322822218, 1537002063, 1747873779, 1955562222, 2024104815, -2067236844, -1933114872, -1866530822, -1538233109, -1090935817, -965641998};

    /* renamed from: W, reason: collision with root package name */
    private int[] f13641W;
    private int[] state;
    private final int[] initialHashes;

    SHA2(String str, int i2, int[] iArr) {
        super(str, i2, 64);
        this.initialHashes = iArr;
        this.state = new int[8];
        this.f13641W = new int[64];
        resetHashes();
    }

    @Override // sun.security.provider.DigestBase
    void implReset() {
        resetHashes();
        Arrays.fill(this.f13641W, 0);
    }

    private void resetHashes() {
        System.arraycopy(this.initialHashes, 0, this.state, 0, this.state.length);
    }

    @Override // sun.security.provider.DigestBase
    void implDigest(byte[] bArr, int i2) {
        long j2 = this.bytesProcessed << 3;
        int i3 = ((int) this.bytesProcessed) & 63;
        engineUpdate(padding, 0, i3 < 56 ? 56 - i3 : 120 - i3);
        ByteArrayAccess.i2bBig4((int) (j2 >>> 32), this.buffer, 56);
        ByteArrayAccess.i2bBig4((int) j2, this.buffer, 60);
        implCompress(this.buffer, 0);
        ByteArrayAccess.i2bBig(this.state, 0, bArr, i2, engineGetDigestLength());
    }

    private static int lf_ch(int i2, int i3, int i4) {
        return (i2 & i3) ^ ((i2 ^ (-1)) & i4);
    }

    private static int lf_maj(int i2, int i3, int i4) {
        return ((i2 & i3) ^ (i2 & i4)) ^ (i3 & i4);
    }

    private static int lf_R(int i2, int i3) {
        return i2 >>> i3;
    }

    private static int lf_S(int i2, int i3) {
        return (i2 >>> i3) | (i2 << (32 - i3));
    }

    private static int lf_sigma0(int i2) {
        return (lf_S(i2, 2) ^ lf_S(i2, 13)) ^ lf_S(i2, 22);
    }

    private static int lf_sigma1(int i2) {
        return (lf_S(i2, 6) ^ lf_S(i2, 11)) ^ lf_S(i2, 25);
    }

    private static int lf_delta0(int i2) {
        return (lf_S(i2, 7) ^ lf_S(i2, 18)) ^ lf_R(i2, 3);
    }

    private static int lf_delta1(int i2) {
        return (lf_S(i2, 17) ^ lf_S(i2, 19)) ^ lf_R(i2, 10);
    }

    @Override // sun.security.provider.DigestBase
    void implCompress(byte[] bArr, int i2) {
        implCompressCheck(bArr, i2);
        implCompress0(bArr, i2);
    }

    private void implCompressCheck(byte[] bArr, int i2) {
        Objects.requireNonNull(bArr);
        ByteArrayAccess.b2iBig64(bArr, i2, this.f13641W);
    }

    private void implCompress0(byte[] bArr, int i2) {
        for (int i3 = 16; i3 < 64; i3++) {
            this.f13641W[i3] = lf_delta1(this.f13641W[i3 - 2]) + this.f13641W[i3 - 7] + lf_delta0(this.f13641W[i3 - 15]) + this.f13641W[i3 - 16];
        }
        int i4 = this.state[0];
        int i5 = this.state[1];
        int i6 = this.state[2];
        int i7 = this.state[3];
        int i8 = this.state[4];
        int i9 = this.state[5];
        int i10 = this.state[6];
        int i11 = this.state[7];
        for (int i12 = 0; i12 < 64; i12++) {
            int iLf_sigma1 = i11 + lf_sigma1(i8) + lf_ch(i8, i9, i10) + ROUND_CONSTS[i12] + this.f13641W[i12];
            int iLf_sigma0 = lf_sigma0(i4) + lf_maj(i4, i5, i6);
            i11 = i10;
            i10 = i9;
            i9 = i8;
            i8 = i7 + iLf_sigma1;
            i7 = i6;
            i6 = i5;
            i5 = i4;
            i4 = iLf_sigma1 + iLf_sigma0;
        }
        int[] iArr = this.state;
        iArr[0] = iArr[0] + i4;
        int[] iArr2 = this.state;
        iArr2[1] = iArr2[1] + i5;
        int[] iArr3 = this.state;
        iArr3[2] = iArr3[2] + i6;
        int[] iArr4 = this.state;
        iArr4[3] = iArr4[3] + i7;
        int[] iArr5 = this.state;
        iArr5[4] = iArr5[4] + i8;
        int[] iArr6 = this.state;
        iArr6[5] = iArr6[5] + i9;
        int[] iArr7 = this.state;
        iArr7[6] = iArr7[6] + i10;
        int[] iArr8 = this.state;
        iArr8[7] = iArr8[7] + i11;
    }

    @Override // sun.security.provider.DigestBase, java.security.MessageDigestSpi
    public Object clone() throws CloneNotSupportedException {
        SHA2 sha2 = (SHA2) super.clone();
        sha2.state = (int[]) sha2.state.clone();
        sha2.f13641W = new int[64];
        return sha2;
    }

    /* loaded from: rt.jar:sun/security/provider/SHA2$SHA224.class */
    public static final class SHA224 extends SHA2 {
        private static final int[] INITIAL_HASHES = {-1056596264, 914150663, 812702999, -150054599, -4191439, 1750603025, 1694076839, -1090891868};

        @Override // sun.security.provider.SHA2, sun.security.provider.DigestBase, java.security.MessageDigestSpi
        public /* bridge */ /* synthetic */ Object clone() throws CloneNotSupportedException {
            return super.clone();
        }

        public SHA224() {
            super("SHA-224", 28, INITIAL_HASHES);
        }
    }

    /* loaded from: rt.jar:sun/security/provider/SHA2$SHA256.class */
    public static final class SHA256 extends SHA2 {
        private static final int[] INITIAL_HASHES = {1779033703, -1150833019, 1013904242, -1521486534, 1359893119, -1694144372, 528734635, 1541459225};

        @Override // sun.security.provider.SHA2, sun.security.provider.DigestBase, java.security.MessageDigestSpi
        public /* bridge */ /* synthetic */ Object clone() throws CloneNotSupportedException {
            return super.clone();
        }

        public SHA256() {
            super("SHA-256", 32, INITIAL_HASHES);
        }
    }
}
