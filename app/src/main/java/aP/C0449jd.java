package aP;

import G.InterfaceC0042ab;
import java.util.Vector;

/* renamed from: aP.jd, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:aP/jd.class */
class C0449jd implements InterfaceC0042ab {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ iX f3783a;

    C0449jd(iX iXVar) {
        this.f3783a = iXVar;
    }

    @Override // G.InterfaceC0042ab
    public void a(String str, int i2, int i3, int[] iArr) {
        if (i2 == this.f3783a.f3704k) {
            int i4 = i3;
            do {
                int i5 = i4 / this.f3783a.f3695b;
                ((Vector) this.f3783a.f3699f.get(i5)).setElementAt(this.f3783a.b(iArr[i4 - i3], this.f3783a.f3694a), i4 % this.f3783a.f3695b);
                i4++;
            } while (i4 < i3 + iArr.length);
            this.f3783a.f3700g.repaint();
        }
    }
}
