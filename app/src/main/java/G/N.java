package G;

import java.io.Serializable;

/* loaded from: TunerStudioMS.jar:G/N.class */
public class N implements Serializable {

    /* renamed from: a, reason: collision with root package name */
    int[][] f459a = (int[][]) null;

    /* renamed from: b, reason: collision with root package name */
    boolean[][] f460b = (boolean[][]) null;

    /* renamed from: c, reason: collision with root package name */
    final /* synthetic */ J f461c;

    public N(J j2) {
        this.f461c = j2;
    }

    public void a(F f2) {
        c(f2.g());
        int[] iArrL = f2.l();
        for (int i2 = 0; i2 < iArrL.length; i2++) {
            a(i2, iArrL[i2]);
        }
    }

    private void c(int i2) {
        this.f459a = new int[i2][1];
        this.f460b = new boolean[i2][1];
    }

    private void a(int i2, int i3) {
        this.f459a[i2] = new int[i3];
        this.f460b[i2] = new boolean[i3];
        for (int i4 = 0; i4 < this.f460b[i2].length; i4++) {
            this.f460b[i2][i4] = true;
        }
    }

    public void a(int i2, int i3, int[] iArr, boolean z2) {
        for (int i4 = 0; i4 < iArr.length; i4++) {
            if (i2 == -1 || i3 + i4 == -1) {
                bH.C.c("Local Controller Data Write Error!!!");
            }
            if ((z2 || this.f460b[i2][i3 + i4]) && this.f459a[i2][i3 + i4] != iArr[i4]) {
                this.f459a[i2][i3 + i4] = iArr[i4];
            }
        }
    }

    protected void a(int i2, int i3, boolean z2) {
        this.f460b[i2][i3] = z2;
    }

    protected int[] a(int i2) {
        return this.f459a[i2];
    }

    public int[] b(int i2) {
        int[] iArrA = a(i2);
        int[] iArr = new int[iArrA.length];
        for (int i3 = 0; i3 < iArrA.length; i3++) {
            iArr[i3] = iArrA[i3];
        }
        return iArr;
    }

    public int[] a(int i2, int i3, int i4) {
        int[] iArrA = a(i2);
        if (iArrA == null) {
            return new int[0];
        }
        int[] iArr = new int[i4];
        System.arraycopy(iArrA, i3, iArr, 0, i4);
        return iArr;
    }
}
