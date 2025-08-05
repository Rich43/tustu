package sun.security.provider;

import java.security.AccessController;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivilegedAction;
import java.security.Provider;
import java.security.ProviderException;
import java.util.Arrays;

/* loaded from: rt.jar:sun/security/provider/MD4.class */
public final class MD4 extends DigestBase {
    private int[] state;

    /* renamed from: x, reason: collision with root package name */
    private int[] f13638x;
    private static final int S11 = 3;
    private static final int S12 = 7;
    private static final int S13 = 11;
    private static final int S14 = 19;
    private static final int S21 = 3;
    private static final int S22 = 5;
    private static final int S23 = 9;
    private static final int S24 = 13;
    private static final int S31 = 3;
    private static final int S32 = 9;
    private static final int S33 = 11;
    private static final int S34 = 15;
    private static final Provider md4Provider = new Provider("MD4Provider", 1.8d, "MD4 MessageDigest") { // from class: sun.security.provider.MD4.1
        private static final long serialVersionUID = -8850464997518327965L;
    };

    static {
        AccessController.doPrivileged(new PrivilegedAction<Void>() { // from class: sun.security.provider.MD4.2
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.security.PrivilegedAction
            /* renamed from: run */
            public Void run2() {
                MD4.md4Provider.put("MessageDigest.MD4", "sun.security.provider.MD4");
                return null;
            }
        });
    }

    public static MessageDigest getInstance() {
        try {
            return MessageDigest.getInstance("MD4", md4Provider);
        } catch (NoSuchAlgorithmException e2) {
            throw new ProviderException(e2);
        }
    }

    public MD4() {
        super("MD4", 16, 64);
        this.state = new int[4];
        this.f13638x = new int[16];
        resetHashes();
    }

    @Override // sun.security.provider.DigestBase, java.security.MessageDigestSpi
    public Object clone() throws CloneNotSupportedException {
        MD4 md4 = (MD4) super.clone();
        md4.state = (int[]) md4.state.clone();
        md4.f13638x = new int[16];
        return md4;
    }

    @Override // sun.security.provider.DigestBase
    void implReset() {
        resetHashes();
        Arrays.fill(this.f13638x, 0);
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

    private static int FF(int i2, int i3, int i4, int i5, int i6, int i7) {
        int i8 = i2 + ((i3 & i4) | ((i3 ^ (-1)) & i5)) + i6;
        return (i8 << i7) | (i8 >>> (32 - i7));
    }

    private static int GG(int i2, int i3, int i4, int i5, int i6, int i7) {
        int i8 = i2 + ((i3 & i4) | (i3 & i5) | (i4 & i5)) + i6 + 1518500249;
        return (i8 << i7) | (i8 >>> (32 - i7));
    }

    private static int HH(int i2, int i3, int i4, int i5, int i6, int i7) {
        int i8 = i2 + ((i3 ^ i4) ^ i5) + i6 + 1859775393;
        return (i8 << i7) | (i8 >>> (32 - i7));
    }

    @Override // sun.security.provider.DigestBase
    void implCompress(byte[] bArr, int i2) {
        ByteArrayAccess.b2iLittle64(bArr, i2, this.f13638x);
        int i3 = this.state[0];
        int i4 = this.state[1];
        int i5 = this.state[2];
        int i6 = this.state[3];
        int iFF = FF(i3, i4, i5, i6, this.f13638x[0], 3);
        int iFF2 = FF(i6, iFF, i4, i5, this.f13638x[1], 7);
        int iFF3 = FF(i5, iFF2, iFF, i4, this.f13638x[2], 11);
        int iFF4 = FF(i4, iFF3, iFF2, iFF, this.f13638x[3], 19);
        int iFF5 = FF(iFF, iFF4, iFF3, iFF2, this.f13638x[4], 3);
        int iFF6 = FF(iFF2, iFF5, iFF4, iFF3, this.f13638x[5], 7);
        int iFF7 = FF(iFF3, iFF6, iFF5, iFF4, this.f13638x[6], 11);
        int iFF8 = FF(iFF4, iFF7, iFF6, iFF5, this.f13638x[7], 19);
        int iFF9 = FF(iFF5, iFF8, iFF7, iFF6, this.f13638x[8], 3);
        int iFF10 = FF(iFF6, iFF9, iFF8, iFF7, this.f13638x[9], 7);
        int iFF11 = FF(iFF7, iFF10, iFF9, iFF8, this.f13638x[10], 11);
        int iFF12 = FF(iFF8, iFF11, iFF10, iFF9, this.f13638x[11], 19);
        int iFF13 = FF(iFF9, iFF12, iFF11, iFF10, this.f13638x[12], 3);
        int iFF14 = FF(iFF10, iFF13, iFF12, iFF11, this.f13638x[13], 7);
        int iFF15 = FF(iFF11, iFF14, iFF13, iFF12, this.f13638x[14], 11);
        int iFF16 = FF(iFF12, iFF15, iFF14, iFF13, this.f13638x[15], 19);
        int iGG = GG(iFF13, iFF16, iFF15, iFF14, this.f13638x[0], 3);
        int iGG2 = GG(iFF14, iGG, iFF16, iFF15, this.f13638x[4], 5);
        int iGG3 = GG(iFF15, iGG2, iGG, iFF16, this.f13638x[8], 9);
        int iGG4 = GG(iFF16, iGG3, iGG2, iGG, this.f13638x[12], 13);
        int iGG5 = GG(iGG, iGG4, iGG3, iGG2, this.f13638x[1], 3);
        int iGG6 = GG(iGG2, iGG5, iGG4, iGG3, this.f13638x[5], 5);
        int iGG7 = GG(iGG3, iGG6, iGG5, iGG4, this.f13638x[9], 9);
        int iGG8 = GG(iGG4, iGG7, iGG6, iGG5, this.f13638x[13], 13);
        int iGG9 = GG(iGG5, iGG8, iGG7, iGG6, this.f13638x[2], 3);
        int iGG10 = GG(iGG6, iGG9, iGG8, iGG7, this.f13638x[6], 5);
        int iGG11 = GG(iGG7, iGG10, iGG9, iGG8, this.f13638x[10], 9);
        int iGG12 = GG(iGG8, iGG11, iGG10, iGG9, this.f13638x[14], 13);
        int iGG13 = GG(iGG9, iGG12, iGG11, iGG10, this.f13638x[3], 3);
        int iGG14 = GG(iGG10, iGG13, iGG12, iGG11, this.f13638x[7], 5);
        int iGG15 = GG(iGG11, iGG14, iGG13, iGG12, this.f13638x[11], 9);
        int iGG16 = GG(iGG12, iGG15, iGG14, iGG13, this.f13638x[15], 13);
        int iHH = HH(iGG13, iGG16, iGG15, iGG14, this.f13638x[0], 3);
        int iHH2 = HH(iGG14, iHH, iGG16, iGG15, this.f13638x[8], 9);
        int iHH3 = HH(iGG15, iHH2, iHH, iGG16, this.f13638x[4], 11);
        int iHH4 = HH(iGG16, iHH3, iHH2, iHH, this.f13638x[12], 15);
        int iHH5 = HH(iHH, iHH4, iHH3, iHH2, this.f13638x[2], 3);
        int iHH6 = HH(iHH2, iHH5, iHH4, iHH3, this.f13638x[10], 9);
        int iHH7 = HH(iHH3, iHH6, iHH5, iHH4, this.f13638x[6], 11);
        int iHH8 = HH(iHH4, iHH7, iHH6, iHH5, this.f13638x[14], 15);
        int iHH9 = HH(iHH5, iHH8, iHH7, iHH6, this.f13638x[1], 3);
        int iHH10 = HH(iHH6, iHH9, iHH8, iHH7, this.f13638x[9], 9);
        int iHH11 = HH(iHH7, iHH10, iHH9, iHH8, this.f13638x[5], 11);
        int iHH12 = HH(iHH8, iHH11, iHH10, iHH9, this.f13638x[13], 15);
        int iHH13 = HH(iHH9, iHH12, iHH11, iHH10, this.f13638x[3], 3);
        int iHH14 = HH(iHH10, iHH13, iHH12, iHH11, this.f13638x[11], 9);
        int iHH15 = HH(iHH11, iHH14, iHH13, iHH12, this.f13638x[7], 11);
        int iHH16 = HH(iHH12, iHH15, iHH14, iHH13, this.f13638x[15], 15);
        int[] iArr = this.state;
        iArr[0] = iArr[0] + iHH13;
        int[] iArr2 = this.state;
        iArr2[1] = iArr2[1] + iHH16;
        int[] iArr3 = this.state;
        iArr3[2] = iArr3[2] + iHH15;
        int[] iArr4 = this.state;
        iArr4[3] = iArr4[3] + iHH14;
    }
}
