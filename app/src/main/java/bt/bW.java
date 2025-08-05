package bt;

import G.C0072be;
import G.C0083bp;
import G.C0088bu;
import com.efiAnalytics.ui.C1677fh;
import com.efiAnalytics.ui.C1701s;
import com.efiAnalytics.ui.InterfaceC1548am;
import java.util.ArrayList;
import s.C1818g;

/* loaded from: TunerStudioMS.jar:bt/bW.class */
public class bW implements InterfaceC1548am {

    /* renamed from: a, reason: collision with root package name */
    G.R f8979a;

    /* renamed from: b, reason: collision with root package name */
    C0072be f8980b;

    /* renamed from: c, reason: collision with root package name */
    C1701s f8981c;

    /* renamed from: d, reason: collision with root package name */
    G.aM f8982d;

    /* renamed from: e, reason: collision with root package name */
    G.aM f8983e;

    /* renamed from: f, reason: collision with root package name */
    G.aM f8984f;

    /* renamed from: g, reason: collision with root package name */
    G.aM f8985g;

    /* renamed from: h, reason: collision with root package name */
    G.aM f8986h;

    /* renamed from: i, reason: collision with root package name */
    public static int f8987i = 1;

    /* renamed from: j, reason: collision with root package name */
    public static int f8988j = 2;

    /* renamed from: k, reason: collision with root package name */
    public static int f8989k = -1;

    /* renamed from: l, reason: collision with root package name */
    int f8990l = f8989k;

    public bW(G.R r2, C0072be c0072be, C1701s c1701s) {
        this.f8982d = null;
        this.f8983e = null;
        this.f8979a = r2;
        this.f8980b = c0072be;
        this.f8981c = c1701s;
        this.f8985g = r2.c(c0072be.b());
        this.f8984f = r2.c(c0072be.a());
        this.f8986h = r2.c(c0072be.c());
        String[] strArrP = this.f8984f.P();
        if (strArrP == null) {
            bH.C.b("Table xSize Parameter name null, not resizable");
        } else if (strArrP.length != 1) {
            bH.C.b("Table xSize Parameter name not 1 in length, considered not fully resizable");
        } else {
            this.f8983e = r2.c(strArrP[0]);
            if (this.f8983e == null) {
                bH.C.b("X Size Parameter not found in current configuration, resizing disabled.");
            }
        }
        String[] strArrP2 = this.f8985g.P();
        if (strArrP2 == null) {
            bH.C.b("Table ySize Parameter name null, not resizable");
            return;
        }
        if (strArrP2.length != 1) {
            bH.C.b("Table ySize Parameter name not 1 in length, considered not fully resizable");
            return;
        }
        this.f8982d = r2.c(strArrP2[0]);
        if (this.f8982d == null) {
            bH.C.b("Y Size Parameter not found in current configuration, resizing disabled.");
        }
    }

    @Override // com.efiAnalytics.ui.InterfaceC1548am
    public boolean a() {
        return true;
    }

    @Override // com.efiAnalytics.ui.InterfaceC1548am
    public int b() {
        return (int) (this.f8982d != null ? Math.round(this.f8982d.r()) : this.f8985g.b());
    }

    @Override // com.efiAnalytics.ui.InterfaceC1548am
    public int c() {
        return (int) (this.f8982d != null ? Math.round(this.f8982d.q()) : this.f8985g.b());
    }

    @Override // com.efiAnalytics.ui.InterfaceC1548am
    public int d() {
        return (int) (this.f8983e != null ? Math.round(this.f8983e.r()) : this.f8984f.b());
    }

    @Override // com.efiAnalytics.ui.InterfaceC1548am
    public int e() {
        return (int) (this.f8983e != null ? Math.round(this.f8983e.q()) : this.f8984f.b());
    }

    @Override // com.efiAnalytics.ui.InterfaceC1548am
    public int f() {
        int iG = g();
        return i() ? iG - (((int) Math.sqrt(iG)) * 2) : iG;
    }

    private boolean i() {
        if (this.f8990l == f8989k) {
            ArrayList arrayList = new ArrayList();
            arrayList.add(this.f8984f);
            arrayList.add(this.f8985g);
            arrayList.add(this.f8986h);
            if (this.f8984f.n() && this.f8985g.n() && this.f8984f.d() == this.f8986h.d() && this.f8985g.d() == this.f8986h.d() && G.aJ.a(this.f8979a, arrayList)) {
                this.f8990l = f8987i + f8988j;
            } else {
                this.f8990l = 0;
            }
        }
        return this.f8990l == f8987i + f8988j;
    }

    public int g() {
        int iG;
        if (i()) {
            iG = Math.min(this.f8986h.g(), Math.min(this.f8984f.g(), this.f8985g.g()));
        } else {
            iG = this.f8986h.g();
        }
        return Math.floorDiv(G.aJ.a(this.f8979a, this.f8986h.d(), iG), this.f8986h.e());
    }

    public boolean a(int i2, int i3) {
        boolean zI = i();
        int iA = G.aJ.a(this.f8979a, this.f8986h.d(), zI ? Math.min(this.f8986h.g(), Math.min(this.f8984f.g(), this.f8985g.g())) : this.f8986h.g());
        if (zI) {
            return Math.floorDiv((iA - (this.f8984f.e() * i3)) - (this.f8985g.e() * i2), this.f8986h.e()) <= i2 * i3;
        }
        return Math.floorDiv(iA, this.f8986h.e()) <= i2 * i3;
    }

    private void b(int i2, int i3) throws V.a {
        if (i2 > b()) {
            throw new V.a(C1818g.b("Maximum Rows already reached"));
        }
        if (i2 < c()) {
            throw new V.a(C1818g.b("Minimum rows") + ": " + c());
        }
        if (i3 > d()) {
            throw new V.a(C1818g.b("Maximum Columns already reached"));
        }
        if (i3 < e()) {
            throw new V.a(C1818g.b("Minimum columns") + ": " + e());
        }
        if (i3 * i2 > f()) {
            throw new V.a(C1818g.b("Requires more table cells than permitted."));
        }
        if (!a(i2, i3)) {
            throw new V.a(C1818g.b("Requires more table cells than available contiguous RAM."));
        }
    }

    @Override // com.efiAnalytics.ui.InterfaceC1548am
    public void h() {
        C0088bu c0088bu = new C0088bu();
        c0088bu.s(C1818g.b("Resize Table") + " " + G.bL.c(this.f8979a, this.f8980b.aJ()));
        C0083bp c0083bp = new C0083bp();
        c0083bp.e(" ");
        c0088bu.a(c0083bp);
        C0083bp c0083bp2 = new C0083bp();
        c0083bp2.a(this.f8982d.aJ());
        c0083bp2.e(C1818g.b("Number of Rows"));
        c0088bu.a(c0083bp2);
        C0083bp c0083bp3 = new C0083bp();
        c0083bp3.a(this.f8983e.aJ());
        c0083bp3.e(C1818g.b("Number of Columns"));
        c0088bu.a(c0083bp3);
        n.h.a().a(this.f8979a, c0088bu, com.efiAnalytics.ui.bV.c());
    }

    @Override // com.efiAnalytics.ui.InterfaceC1548am
    public void a(int i2) throws V.a {
        b(this.f8981c.getRowCount() + 1, this.f8981c.getColumnCount());
        try {
            try {
                bO.a().b(false);
                C1701s c1701sB = C1677fh.b(this.f8981c);
                this.f8982d.l(this.f8979a.h());
                int iJ = (int) this.f8982d.j(this.f8979a.h());
                int iJ2 = (int) this.f8983e.j(this.f8979a.h());
                this.f8981c.a(iJ, iJ2);
                bH.C.c("Set new Model size: Rows: " + iJ + ", Cols: " + iJ2);
                int rowCount = c1701sB.getRowCount();
                String[] strArr = new String[iJ];
                int i3 = 0;
                while (i3 < iJ) {
                    String strB = i3 == i2 ? i3 == iJ - 1 ? c1701sB.a()[rowCount - 1] : i3 == 0 ? c1701sB.a()[0] : bH.W.b((Double.parseDouble(c1701sB.a()[i3 - 1]) + Double.parseDouble(c1701sB.a()[i3])) / 2.0d, this.f8981c.J()) : c1701sB.a()[i3 <= i2 ? i3 : i3 - 1];
                    strArr[(strArr.length - i3) - 1] = strB;
                    bH.C.c("set row " + i3 + " to " + strB);
                    i3++;
                }
                this.f8981c.d(strArr);
                bH.C.c("Copy Z vals");
                C1677fh.b(c1701sB, this.f8981c);
                this.f8979a.h().m();
                bO.a().b(true);
            } catch (Exception e2) {
                e2.printStackTrace();
                throw new V.a("Error trying to delete Row.\n" + e2.getLocalizedMessage());
            }
        } catch (Throwable th) {
            bO.a().b(true);
            throw th;
        }
    }

    @Override // com.efiAnalytics.ui.InterfaceC1548am
    public void b(int i2) throws V.a {
        b(this.f8981c.getRowCount() - 1, this.f8981c.getColumnCount());
        try {
            try {
                bO.a().b(false);
                C1701s c1701sB = C1677fh.b(this.f8981c);
                this.f8982d.m(this.f8979a.h());
                int iJ = (int) this.f8982d.j(this.f8979a.h());
                int iJ2 = (int) this.f8983e.j(this.f8979a.h());
                this.f8981c.a(iJ, iJ2);
                bH.C.c("Set new Model size: Rows: " + iJ + ", Cols: " + iJ2);
                int rowCount = c1701sB.getRowCount();
                String[] strArr = new String[iJ];
                int i3 = 0;
                while (i3 < rowCount) {
                    if (i3 != i2) {
                        int i4 = i3 < i2 ? i3 : i3 - 1;
                        String str = c1701sB.a()[i3];
                        strArr[(strArr.length - i4) - 1] = str;
                        bH.C.c("set row " + i4 + " to " + str);
                    }
                    i3++;
                }
                this.f8981c.d(strArr);
                bH.C.c("Copy Z vals");
                C1677fh.b(c1701sB, this.f8981c);
                this.f8979a.h().m();
                bO.a().b(true);
            } catch (Exception e2) {
                throw new V.a("Error trying to delete Row.\n" + e2.getLocalizedMessage());
            }
        } catch (Throwable th) {
            bO.a().b(true);
            throw th;
        }
    }

    @Override // com.efiAnalytics.ui.InterfaceC1548am
    public void c(int i2) throws V.a {
        b(this.f8981c.getRowCount(), this.f8981c.getColumnCount() + 1);
        try {
            try {
                bO.a().b(false);
                C1701s c1701sB = C1677fh.b(this.f8981c);
                this.f8983e.l(this.f8979a.h());
                int iJ = (int) this.f8982d.j(this.f8979a.h());
                int iJ2 = (int) this.f8983e.j(this.f8979a.h());
                this.f8981c.a(iJ, iJ2);
                bH.C.c("Set new Model size: Rows: " + iJ + ", Cols: " + iJ2);
                int columnCount = c1701sB.getColumnCount();
                String[] strArr = new String[iJ2];
                int i3 = 0;
                while (i3 < iJ2) {
                    String strB = i3 == i2 ? i3 == iJ2 - 1 ? c1701sB.b()[columnCount - 1] : i3 == 0 ? c1701sB.b()[0] : bH.W.b((Double.parseDouble(c1701sB.b()[i3 - 1]) + Double.parseDouble(c1701sB.b()[i3])) / 2.0d, this.f8981c.J()) : c1701sB.b()[i3 <= i2 ? i3 : i3 - 1];
                    strArr[i3] = strB;
                    bH.C.c("set col " + i3 + " to " + strB);
                    i3++;
                }
                this.f8981c.c(strArr);
                bH.C.c("Copy Z vals");
                C1677fh.b(c1701sB, this.f8981c);
                this.f8979a.h().m();
                bO.a().b(true);
            } catch (Exception e2) {
                e2.printStackTrace();
                throw new V.a("Error trying to delete Column.\n" + e2.getLocalizedMessage());
            }
        } catch (Throwable th) {
            bO.a().b(true);
            throw th;
        }
    }

    @Override // com.efiAnalytics.ui.InterfaceC1548am
    public void d(int i2) throws V.a {
        b(this.f8981c.getRowCount(), this.f8981c.getColumnCount() - 1);
        try {
            try {
                bO.a().b(false);
                C1701s c1701sB = C1677fh.b(this.f8981c);
                this.f8983e.m(this.f8979a.h());
                int iJ = (int) this.f8982d.j(this.f8979a.h());
                int iJ2 = (int) this.f8983e.j(this.f8979a.h());
                this.f8981c.a(iJ, iJ2);
                bH.C.c("Set new Model size: Rows: " + iJ + ", Cols: " + iJ2);
                int columnCount = c1701sB.getColumnCount();
                String[] strArr = new String[iJ2];
                int i3 = 0;
                while (i3 < columnCount) {
                    if (i3 != i2) {
                        int i4 = i3 < i2 ? i3 : i3 - 1;
                        String str = c1701sB.b()[i3];
                        strArr[i4] = str;
                        bH.C.c("set col " + i4 + " to " + str);
                    }
                    i3++;
                }
                this.f8981c.c(strArr);
                bH.C.c("Copy Z vals");
                C1677fh.b(c1701sB, this.f8981c);
                this.f8979a.h().m();
                bO.a().b(true);
            } catch (Exception e2) {
                e2.printStackTrace();
                throw new V.a("Error trying to delete Column.\n" + e2.getLocalizedMessage());
            }
        } catch (Throwable th) {
            bO.a().b(true);
            throw th;
        }
    }
}
