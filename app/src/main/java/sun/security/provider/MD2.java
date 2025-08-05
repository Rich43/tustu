package sun.security.provider;

import com.sun.imageio.plugins.jpeg.JPEG;
import java.util.Arrays;
import org.apache.commons.net.telnet.TelnetCommand;

/* loaded from: rt.jar:sun/security/provider/MD2.class */
public final class MD2 extends DigestBase {

    /* renamed from: X, reason: collision with root package name */
    private int[] f13635X;

    /* renamed from: C, reason: collision with root package name */
    private int[] f13636C;
    private byte[] cBytes;

    /* renamed from: S, reason: collision with root package name */
    private static final int[] f13637S = {41, 46, 67, 201, 162, 216, 124, 1, 61, 54, 84, 161, 236, 240, 6, 19, 98, 167, 5, 243, 192, 199, 115, 140, 152, 147, 43, 217, 188, 76, 130, 202, 30, 155, 87, 60, 253, 212, 224, 22, 103, 66, 111, 24, 138, 23, 229, 18, 190, 78, 196, 214, 218, 158, 222, 73, 160, 251, 245, 142, 187, 47, 238, 122, 169, 104, 121, 145, 21, 178, 7, 63, 148, 194, 16, 137, 11, 34, 95, 33, 128, 127, 93, 154, 90, 144, 50, 39, 53, 62, 204, 231, 191, 247, 151, 3, 255, 25, 48, 179, 72, 165, 181, 209, 215, 94, 146, 42, 172, 86, 170, 198, 79, 184, 56, 210, 150, 164, 125, 182, 118, 252, 107, 226, 156, 116, 4, 241, 69, 157, 112, 89, 100, 113, 135, 32, 134, 91, 207, 101, 230, 45, 168, 2, 27, 96, 37, 173, 174, 176, 185, 246, 28, 70, 97, 105, 52, 64, 126, 15, 85, 71, 163, 35, 221, 81, 175, 58, 195, 92, TelnetCommand.GA, 206, 186, 197, 234, 38, 44, 83, 13, 110, 133, 40, 132, 9, 211, 223, 205, 244, 65, 129, 77, 82, 106, 220, 55, 200, 108, 193, 171, 250, 36, 225, 123, 8, 12, 189, 177, 74, 120, 136, 149, 139, 227, 99, JPEG.APP8, 109, 233, 203, 213, 254, 59, 0, 29, 57, 242, 239, 183, 14, 102, 88, 208, 228, 166, 119, 114, 248, 235, 117, 75, 10, 49, 68, 80, 180, 143, 237, 31, 26, 219, 153, 141, 51, 159, 17, 131, 20};
    private static final byte[][] PADDING = new byte[17];

    public MD2() {
        super("MD2", 16, 16);
        this.f13635X = new int[48];
        this.f13636C = new int[16];
        this.cBytes = new byte[16];
    }

    @Override // sun.security.provider.DigestBase, java.security.MessageDigestSpi
    public Object clone() throws CloneNotSupportedException {
        MD2 md2 = (MD2) super.clone();
        md2.f13635X = (int[]) md2.f13635X.clone();
        md2.f13636C = (int[]) md2.f13636C.clone();
        md2.cBytes = new byte[16];
        return md2;
    }

    @Override // sun.security.provider.DigestBase
    void implReset() {
        Arrays.fill(this.f13635X, 0);
        Arrays.fill(this.f13636C, 0);
    }

    @Override // sun.security.provider.DigestBase
    void implDigest(byte[] bArr, int i2) {
        int i3 = 16 - (((int) this.bytesProcessed) & 15);
        engineUpdate(PADDING[i3], 0, i3);
        for (int i4 = 0; i4 < 16; i4++) {
            this.cBytes[i4] = (byte) this.f13636C[i4];
        }
        implCompress(this.cBytes, 0);
        for (int i5 = 0; i5 < 16; i5++) {
            bArr[i2 + i5] = (byte) this.f13635X[i5];
        }
    }

    @Override // sun.security.provider.DigestBase
    void implCompress(byte[] bArr, int i2) {
        for (int i3 = 0; i3 < 16; i3++) {
            int i4 = bArr[i2 + i3] & 255;
            this.f13635X[16 + i3] = i4;
            this.f13635X[32 + i3] = i4 ^ this.f13635X[i3];
        }
        int i5 = this.f13636C[15];
        for (int i6 = 0; i6 < 16; i6++) {
            int[] iArr = this.f13636C;
            int i7 = i6;
            int i8 = iArr[i7] ^ f13637S[this.f13635X[16 + i6] ^ i5];
            iArr[i7] = i8;
            i5 = i8;
        }
        int i9 = 0;
        for (int i10 = 0; i10 < 18; i10++) {
            for (int i11 = 0; i11 < 48; i11++) {
                int[] iArr2 = this.f13635X;
                int i12 = i11;
                int i13 = iArr2[i12] ^ f13637S[i9];
                iArr2[i12] = i13;
                i9 = i13;
            }
            i9 = (i9 + i10) & 255;
        }
    }

    /* JADX WARN: Type inference failed for: r0v3, types: [byte[], byte[][]] */
    static {
        for (int i2 = 1; i2 < 17; i2++) {
            byte[] bArr = new byte[i2];
            Arrays.fill(bArr, (byte) i2);
            PADDING[i2] = bArr;
        }
    }
}
