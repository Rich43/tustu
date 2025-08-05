package sun.security.provider;

import java.util.Arrays;

/* loaded from: rt.jar:sun/security/provider/MD5.class */
public final class MD5 extends DigestBase {
    private int[] state;

    /* renamed from: x, reason: collision with root package name */
    private int[] f13639x;
    private static final int S11 = 7;
    private static final int S12 = 12;
    private static final int S13 = 17;
    private static final int S14 = 22;
    private static final int S21 = 5;
    private static final int S22 = 9;
    private static final int S23 = 14;
    private static final int S24 = 20;
    private static final int S31 = 4;
    private static final int S32 = 11;
    private static final int S33 = 16;
    private static final int S34 = 23;
    private static final int S41 = 6;
    private static final int S42 = 10;
    private static final int S43 = 15;
    private static final int S44 = 21;

    public MD5() {
        super("MD5", 16, 64);
        this.state = new int[4];
        this.f13639x = new int[16];
        resetHashes();
    }

    @Override // sun.security.provider.DigestBase, java.security.MessageDigestSpi
    public Object clone() throws CloneNotSupportedException {
        MD5 md5 = (MD5) super.clone();
        md5.state = (int[]) md5.state.clone();
        md5.f13639x = new int[16];
        return md5;
    }

    @Override // sun.security.provider.DigestBase
    void implReset() {
        resetHashes();
        Arrays.fill(this.f13639x, 0);
    }

    private void resetHashes() {
        this.state[0] = 1732584193;
        this.state[1] = -271733879;
        this.state[2] = -1732584194;
        this.state[3] = 271733878;
    }

    @Override // sun.security.provider.DigestBase
    void implDigest(byte[] bArr, int i2) {
        long j2 = this.bytesProcessed << 3;
        int i3 = ((int) this.bytesProcessed) & 63;
        engineUpdate(padding, 0, i3 < 56 ? 56 - i3 : 120 - i3);
        ByteArrayAccess.i2bLittle4((int) j2, this.buffer, 56);
        ByteArrayAccess.i2bLittle4((int) (j2 >>> 32), this.buffer, 60);
        implCompress(this.buffer, 0);
        ByteArrayAccess.i2bLittle(this.state, 0, bArr, i2, 16);
    }

    private static int FF(int i2, int i3, int i4, int i5, int i6, int i7, int i8) {
        int i9 = i2 + ((i3 & i4) | ((i3 ^ (-1)) & i5)) + i6 + i8;
        return ((i9 << i7) | (i9 >>> (32 - i7))) + i3;
    }

    private static int GG(int i2, int i3, int i4, int i5, int i6, int i7, int i8) {
        int i9 = i2 + ((i3 & i5) | (i4 & (i5 ^ (-1)))) + i6 + i8;
        return ((i9 << i7) | (i9 >>> (32 - i7))) + i3;
    }

    private static int HH(int i2, int i3, int i4, int i5, int i6, int i7, int i8) {
        int i9 = i2 + ((i3 ^ i4) ^ i5) + i6 + i8;
        return ((i9 << i7) | (i9 >>> (32 - i7))) + i3;
    }

    private static int II(int i2, int i3, int i4, int i5, int i6, int i7, int i8) {
        int i9 = i2 + (i4 ^ (i3 | (i5 ^ (-1)))) + i6 + i8;
        return ((i9 << i7) | (i9 >>> (32 - i7))) + i3;
    }

    @Override // sun.security.provider.DigestBase
    void implCompress(byte[] bArr, int i2) {
        ByteArrayAccess.b2iLittle64(bArr, i2, this.f13639x);
        int i3 = this.state[0];
        int i4 = this.state[1];
        int i5 = this.state[2];
        int i6 = this.state[3];
        int iFF = FF(i3, i4, i5, i6, this.f13639x[0], 7, -680876936);
        int iFF2 = FF(i6, iFF, i4, i5, this.f13639x[1], 12, -389564586);
        int iFF3 = FF(i5, iFF2, iFF, i4, this.f13639x[2], 17, 606105819);
        int iFF4 = FF(i4, iFF3, iFF2, iFF, this.f13639x[3], 22, -1044525330);
        int iFF5 = FF(iFF, iFF4, iFF3, iFF2, this.f13639x[4], 7, -176418897);
        int iFF6 = FF(iFF2, iFF5, iFF4, iFF3, this.f13639x[5], 12, 1200080426);
        int iFF7 = FF(iFF3, iFF6, iFF5, iFF4, this.f13639x[6], 17, -1473231341);
        int iFF8 = FF(iFF4, iFF7, iFF6, iFF5, this.f13639x[7], 22, -45705983);
        int iFF9 = FF(iFF5, iFF8, iFF7, iFF6, this.f13639x[8], 7, 1770035416);
        int iFF10 = FF(iFF6, iFF9, iFF8, iFF7, this.f13639x[9], 12, -1958414417);
        int iFF11 = FF(iFF7, iFF10, iFF9, iFF8, this.f13639x[10], 17, -42063);
        int iFF12 = FF(iFF8, iFF11, iFF10, iFF9, this.f13639x[11], 22, -1990404162);
        int iFF13 = FF(iFF9, iFF12, iFF11, iFF10, this.f13639x[12], 7, 1804603682);
        int iFF14 = FF(iFF10, iFF13, iFF12, iFF11, this.f13639x[13], 12, -40341101);
        int iFF15 = FF(iFF11, iFF14, iFF13, iFF12, this.f13639x[14], 17, -1502002290);
        int iFF16 = FF(iFF12, iFF15, iFF14, iFF13, this.f13639x[15], 22, 1236535329);
        int iGG = GG(iFF13, iFF16, iFF15, iFF14, this.f13639x[1], 5, -165796510);
        int iGG2 = GG(iFF14, iGG, iFF16, iFF15, this.f13639x[6], 9, -1069501632);
        int iGG3 = GG(iFF15, iGG2, iGG, iFF16, this.f13639x[11], 14, 643717713);
        int iGG4 = GG(iFF16, iGG3, iGG2, iGG, this.f13639x[0], 20, -373897302);
        int iGG5 = GG(iGG, iGG4, iGG3, iGG2, this.f13639x[5], 5, -701558691);
        int iGG6 = GG(iGG2, iGG5, iGG4, iGG3, this.f13639x[10], 9, 38016083);
        int iGG7 = GG(iGG3, iGG6, iGG5, iGG4, this.f13639x[15], 14, -660478335);
        int iGG8 = GG(iGG4, iGG7, iGG6, iGG5, this.f13639x[4], 20, -405537848);
        int iGG9 = GG(iGG5, iGG8, iGG7, iGG6, this.f13639x[9], 5, 568446438);
        int iGG10 = GG(iGG6, iGG9, iGG8, iGG7, this.f13639x[14], 9, -1019803690);
        int iGG11 = GG(iGG7, iGG10, iGG9, iGG8, this.f13639x[3], 14, -187363961);
        int iGG12 = GG(iGG8, iGG11, iGG10, iGG9, this.f13639x[8], 20, 1163531501);
        int iGG13 = GG(iGG9, iGG12, iGG11, iGG10, this.f13639x[13], 5, -1444681467);
        int iGG14 = GG(iGG10, iGG13, iGG12, iGG11, this.f13639x[2], 9, -51403784);
        int iGG15 = GG(iGG11, iGG14, iGG13, iGG12, this.f13639x[7], 14, 1735328473);
        int iGG16 = GG(iGG12, iGG15, iGG14, iGG13, this.f13639x[12], 20, -1926607734);
        int iHH = HH(iGG13, iGG16, iGG15, iGG14, this.f13639x[5], 4, -378558);
        int iHH2 = HH(iGG14, iHH, iGG16, iGG15, this.f13639x[8], 11, -2022574463);
        int iHH3 = HH(iGG15, iHH2, iHH, iGG16, this.f13639x[11], 16, 1839030562);
        int iHH4 = HH(iGG16, iHH3, iHH2, iHH, this.f13639x[14], 23, -35309556);
        int iHH5 = HH(iHH, iHH4, iHH3, iHH2, this.f13639x[1], 4, -1530992060);
        int iHH6 = HH(iHH2, iHH5, iHH4, iHH3, this.f13639x[4], 11, 1272893353);
        int iHH7 = HH(iHH3, iHH6, iHH5, iHH4, this.f13639x[7], 16, -155497632);
        int iHH8 = HH(iHH4, iHH7, iHH6, iHH5, this.f13639x[10], 23, -1094730640);
        int iHH9 = HH(iHH5, iHH8, iHH7, iHH6, this.f13639x[13], 4, 681279174);
        int iHH10 = HH(iHH6, iHH9, iHH8, iHH7, this.f13639x[0], 11, -358537222);
        int iHH11 = HH(iHH7, iHH10, iHH9, iHH8, this.f13639x[3], 16, -722521979);
        int iHH12 = HH(iHH8, iHH11, iHH10, iHH9, this.f13639x[6], 23, 76029189);
        int iHH13 = HH(iHH9, iHH12, iHH11, iHH10, this.f13639x[9], 4, -640364487);
        int iHH14 = HH(iHH10, iHH13, iHH12, iHH11, this.f13639x[12], 11, -421815835);
        int iHH15 = HH(iHH11, iHH14, iHH13, iHH12, this.f13639x[15], 16, 530742520);
        int iHH16 = HH(iHH12, iHH15, iHH14, iHH13, this.f13639x[2], 23, -995338651);
        int iII = II(iHH13, iHH16, iHH15, iHH14, this.f13639x[0], 6, -198630844);
        int iII2 = II(iHH14, iII, iHH16, iHH15, this.f13639x[7], 10, 1126891415);
        int iII3 = II(iHH15, iII2, iII, iHH16, this.f13639x[14], 15, -1416354905);
        int iII4 = II(iHH16, iII3, iII2, iII, this.f13639x[5], 21, -57434055);
        int iII5 = II(iII, iII4, iII3, iII2, this.f13639x[12], 6, 1700485571);
        int iII6 = II(iII2, iII5, iII4, iII3, this.f13639x[3], 10, -1894986606);
        int iII7 = II(iII3, iII6, iII5, iII4, this.f13639x[10], 15, -1051523);
        int iII8 = II(iII4, iII7, iII6, iII5, this.f13639x[1], 21, -2054922799);
        int iII9 = II(iII5, iII8, iII7, iII6, this.f13639x[8], 6, 1873313359);
        int iII10 = II(iII6, iII9, iII8, iII7, this.f13639x[15], 10, -30611744);
        int iII11 = II(iII7, iII10, iII9, iII8, this.f13639x[6], 15, -1560198380);
        int iII12 = II(iII8, iII11, iII10, iII9, this.f13639x[13], 21, 1309151649);
        int iII13 = II(iII9, iII12, iII11, iII10, this.f13639x[4], 6, -145523070);
        int iII14 = II(iII10, iII13, iII12, iII11, this.f13639x[11], 10, -1120210379);
        int iII15 = II(iII11, iII14, iII13, iII12, this.f13639x[2], 15, 718787259);
        int iII16 = II(iII12, iII15, iII14, iII13, this.f13639x[9], 21, -343485551);
        int[] iArr = this.state;
        iArr[0] = iArr[0] + iII13;
        int[] iArr2 = this.state;
        iArr2[1] = iArr2[1] + iII16;
        int[] iArr3 = this.state;
        iArr3[2] = iArr3[2] + iII15;
        int[] iArr4 = this.state;
        iArr4[3] = iArr4[3] + iII14;
    }
}
