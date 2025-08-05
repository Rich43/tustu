package W;

/* loaded from: TunerStudioMS.jar:W/az.class */
public class az {

    /* renamed from: a, reason: collision with root package name */
    static byte f2112a = 64;

    /* renamed from: b, reason: collision with root package name */
    static byte f2113b = 2;

    public static byte[] a(byte[] bArr) {
        for (int i2 = 0; i2 < bArr.length; i2++) {
            if ((bArr[i2] & f2112a) == f2112a) {
                bArr[i2] = (byte) (bArr[i2] - f2112a);
            } else {
                bArr[i2] = (byte) (bArr[i2] + f2112a);
            }
            if ((bArr[i2] & f2113b) == f2113b) {
                bArr[i2] = (byte) (bArr[i2] - f2113b);
            } else {
                bArr[i2] = (byte) (bArr[i2] + f2113b);
            }
        }
        return bArr;
    }

    public static int a(int i2) {
        int i3 = (i2 & f2112a) == f2112a ? i2 - f2112a : i2 + f2112a;
        return (i3 & f2113b) == f2113b ? (byte) (i3 - f2113b) : (byte) (i3 + f2113b);
    }
}
