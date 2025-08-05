package aP;

import java.awt.Color;
import s.C1818g;

/* loaded from: TunerStudioMS.jar:aP/U.class */
public class U {

    /* renamed from: b, reason: collision with root package name */
    private aH.a f2783b = null;

    /* renamed from: c, reason: collision with root package name */
    private int f2784c;

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ S f2785a;

    U(S s2, int i2) {
        this.f2785a = s2;
        this.f2784c = i2;
    }

    public boolean equals(Object obj) {
        return obj instanceof U ? ((U) obj).a() == this.f2784c : obj instanceof Integer ? ((Integer) obj).intValue() == this.f2784c : super.equals(obj);
    }

    public String toString() {
        Color background = this.f2785a.getBackground();
        boolean z2 = (background.getRed() + background.getGreen()) + background.getBlue() < 192;
        String str = !z2 ? "<font color=\"#505050\">" : "<font color=\"silver\">";
        if (this.f2783b == null || this.f2785a.f2780g) {
            return this.f2785a.f2774b.R() ? "<html>" + C1818g.b("Scanning for Devices") + "<br>CAN ID: " + this.f2784c : "<html>" + C1818g.b("Connect for device list") + "<br>CAN ID: " + this.f2784c;
        }
        if (!this.f2783b.c()) {
            return "<html>" + str + C1818g.b("No Device Found") + "<br>CAN ID: " + this.f2784c;
        }
        String strA = this.f2783b.a();
        String strD = this.f2783b.d();
        if (strA == null || strA.isEmpty()) {
            strA = this.f2783b.d();
        }
        if (strA == null || strA.isEmpty()) {
            strA = "Unknown";
        }
        if (strA.length() > 32) {
            strA = strA.substring(0, 32);
        }
        return z2 ? "<html><font color=\"yellow\">" + strA + "<br>Signature: " + strD + "<br>CAN ID: " + this.f2784c + " Online" : "<html><font color=blue>" + strA + "<br>CAN ID: " + this.f2784c + " Online";
    }

    public int a() {
        return this.f2784c;
    }

    public aH.a b() {
        return this.f2783b;
    }

    public void a(aH.a aVar) {
        this.f2783b = aVar;
    }
}
