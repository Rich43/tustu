package bt;

import s.C1818g;

/* renamed from: bt.ac, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:bt/ac.class */
class C1294ac {

    /* renamed from: b, reason: collision with root package name */
    private aH.a f8815b = null;

    /* renamed from: c, reason: collision with root package name */
    private int f8816c;

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C1292aa f8817a;

    C1294ac(C1292aa c1292aa, int i2) {
        this.f8817a = c1292aa;
        this.f8816c = i2;
    }

    public boolean equals(Object obj) {
        return obj instanceof C1294ac ? ((C1294ac) obj).a() == this.f8816c : obj instanceof Integer ? ((Integer) obj).intValue() == this.f8816c : super.equals(obj);
    }

    public String toString() {
        if (this.f8815b == null) {
            return this.f8817a.f8806c.R() ? "<html>" + C1818g.b("Scanning for Devices") + "<br>CAN ID: " + this.f8816c : "<html>" + C1818g.b("Connect for device list") + "<br>CAN ID: " + this.f8816c;
        }
        if (!this.f8815b.c()) {
            return "<html><font color=gray>" + C1818g.b("No Device Found") + "<br>CAN ID: " + this.f8816c;
        }
        String strA = this.f8815b.a();
        if (strA == null || strA.isEmpty()) {
            strA = this.f8815b.d();
        }
        if (strA == null || strA.isEmpty()) {
            strA = "Unknown";
        }
        if (strA.length() > 22) {
            strA = strA.substring(0, 22);
        }
        return "<html><font color=blue>" + strA + "<br>CAN ID: " + this.f8816c + " Online";
    }

    public int a() {
        return this.f8816c;
    }

    public void a(aH.a aVar) {
        this.f8815b = aVar;
    }
}
