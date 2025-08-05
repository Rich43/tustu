package bt;

import G.C0072be;
import G.C0126i;
import com.efiAnalytics.ui.C1701s;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

/* loaded from: TunerStudioMS.jar:bt/bQ.class */
class bQ implements G.aN, TableModelListener {

    /* renamed from: a, reason: collision with root package name */
    G.R f8937a;

    /* renamed from: b, reason: collision with root package name */
    G.aM f8938b;

    /* renamed from: c, reason: collision with root package name */
    G.aM f8939c;

    /* renamed from: d, reason: collision with root package name */
    G.aM f8940d;

    /* renamed from: e, reason: collision with root package name */
    C1701s f8941e;

    /* renamed from: f, reason: collision with root package name */
    bR f8942f = null;

    /* renamed from: g, reason: collision with root package name */
    long f8943g = System.currentTimeMillis();

    /* renamed from: h, reason: collision with root package name */
    int f8944h = 3000;

    /* renamed from: i, reason: collision with root package name */
    final /* synthetic */ bO f8945i;

    public bQ(bO bOVar, G.R r2, C1701s c1701s, String str) {
        this.f8945i = bOVar;
        this.f8937a = null;
        this.f8938b = null;
        this.f8939c = null;
        this.f8940d = null;
        this.f8941e = null;
        this.f8941e = c1701s;
        this.f8937a = r2;
        C0072be c0072be = (C0072be) r2.e().c(str);
        this.f8938b = r2.c(c0072be.a());
        this.f8939c = r2.c(c0072be.b());
        this.f8940d = r2.c(c0072be.c());
        G.aR.a().a(r2.c(), this.f8938b.aJ(), this);
        G.aR.a().a(r2.c(), this.f8939c.aJ(), this);
        G.aR.a().a(r2.c(), this.f8940d.aJ(), this);
        C0126i.a(r2.c(), this.f8939c, this);
        C0126i.a(r2.c(), this.f8940d, this);
        C0126i.a(r2.c(), this.f8938b, this);
    }

    public void a() {
        G.aR.a().a(this);
        this.f8941e.removeTableModelListener(this);
    }

    @Override // javax.swing.event.TableModelListener
    public void tableChanged(TableModelEvent tableModelEvent) throws NumberFormatException {
        C1701s c1701s = (C1701s) tableModelEvent.getSource();
        if (tableModelEvent.getFirstRow() == -1 && tableModelEvent.getColumn() < this.f8938b.b()) {
            try {
                a(tableModelEvent.getColumn(), c1701s.b()[tableModelEvent.getColumn()] + "", this.f8938b.a(this.f8937a.p())[tableModelEvent.getColumn()][0] + "");
                return;
            } catch (V.g e2) {
                Logger.getLogger(bO.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
                return;
            }
        }
        if (tableModelEvent.getColumn() == -1) {
            try {
                if (this.f8941e.getRowCount() != this.f8939c.a() / 2) {
                    b(tableModelEvent.getFirstRow(), c1701s.a()[tableModelEvent.getFirstRow()] + "", this.f8939c.a(this.f8937a.p())[tableModelEvent.getFirstRow()][0] + "");
                }
                return;
            } catch (V.g e3) {
                Logger.getLogger(bO.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e3);
                return;
            }
        }
        try {
            double dDoubleValue = c1701s.e(tableModelEvent.getFirstRow(), tableModelEvent.getColumn()).doubleValue();
            if (dDoubleValue > -2.14711111E8d) {
                this.f8940d.a(this.f8937a.p(), dDoubleValue, tableModelEvent.getFirstRow(), tableModelEvent.getColumn());
                if (this.f8941e.D() != null) {
                    this.f8941e.D()[tableModelEvent.getFirstRow()][tableModelEvent.getColumn()].a(dDoubleValue);
                }
            }
        } catch (V.g e4) {
            e4.printStackTrace();
            com.efiAnalytics.ui.bV.d("Error updating table:\n" + e4.getMessage(), com.efiAnalytics.ui.bV.c());
        } catch (V.j e5) {
            c1701s.a((Object) new Double(e5.c()), tableModelEvent.getFirstRow(), tableModelEvent.getColumn());
        } catch (Exception e6) {
            e6.printStackTrace();
            com.efiAnalytics.ui.bV.d("Error updating table:\n" + e6.getMessage() + "\nSee log for more detail.", com.efiAnalytics.ui.bV.c());
        }
    }

    public void a(int i2, String str, String str2) throws NumberFormatException {
        try {
            this.f8938b.a(this.f8937a.p(), Double.parseDouble(str), i2, 0);
        } catch (V.g e2) {
            e2.printStackTrace();
            bH.C.a("Error updating y Axis.", e2, com.efiAnalytics.ui.bV.c());
        } catch (V.j e3) {
            if (System.currentTimeMillis() - this.f8944h > this.f8943g) {
                if (e3.d() == i2 && e3.a() == 1) {
                    com.efiAnalytics.ui.bV.d(this.f8945i.a("The attempted value exceeds the set maximum for the X axis.") + " (" + this.f8938b.aJ() + ")\n" + this.f8945i.a("Attempted Value") + ": " + e3.b() + "\n" + this.f8945i.a("Set Limit") + ": " + e3.c(), com.efiAnalytics.ui.bV.c());
                    this.f8943g = System.currentTimeMillis();
                } else if (e3.d() == i2 && e3.a() == 2) {
                    com.efiAnalytics.ui.bV.d(this.f8945i.a("The attempted value is below the set minimum for the X axis.") + " (" + this.f8938b.aJ() + ")\n" + this.f8945i.a("Attempted Value") + ": " + e3.b() + "\n" + this.f8945i.a("Set Limit") + ": " + e3.c(), com.efiAnalytics.ui.bV.c());
                    this.f8943g = System.currentTimeMillis();
                }
            }
            bH.C.b(str + " is out of range.\nLimit:" + e3.c());
            if (Double.parseDouble(str) > this.f8938b.r()) {
                this.f8941e.a(this.f8938b.r() + "", i2);
            } else {
                this.f8941e.a(this.f8938b.q() + "", i2);
            }
        }
    }

    public void b(int i2, String str, String str2) throws NumberFormatException {
        if (this.f8941e.getRowCount() != this.f8939c.a() / 2) {
            try {
                this.f8939c.a(this.f8937a.p(), Double.parseDouble(str), (this.f8939c.b() - 1) - i2, 0);
            } catch (V.g e2) {
                e2.printStackTrace();
                bH.C.a("Error updating y Axis.", e2, com.efiAnalytics.ui.bV.c());
            } catch (V.j e3) {
                if (System.currentTimeMillis() - this.f8944h > this.f8943g) {
                    if (e3.d() == i2 && e3.a() == 1) {
                        com.efiAnalytics.ui.bV.d(this.f8945i.a("The attempted value exceeds the set maximum for the Y axis.") + " (" + this.f8939c.aJ() + ")\n" + this.f8945i.a("Attempted Value") + ": " + e3.b() + "\n" + this.f8945i.a("Set Limit") + ": " + e3.c(), com.efiAnalytics.ui.bV.c());
                        this.f8943g = System.currentTimeMillis();
                    } else if (e3.d() == i2 && e3.a() == 2) {
                        com.efiAnalytics.ui.bV.d(this.f8945i.a("The attempted value is below the set minimum for the Y axis.") + " (" + this.f8939c.aJ() + ")\n" + this.f8945i.a("Attempted Value") + ": " + e3.b() + "\n" + this.f8945i.a("Set Limit") + ": " + e3.c(), com.efiAnalytics.ui.bV.c());
                        this.f8943g = System.currentTimeMillis();
                    }
                }
                bH.C.b(str + " is out of range.\nLimit:" + e3.c());
                Double.parseDouble(str);
                if (e3.a() == 1) {
                    this.f8941e.b(e3.c() + "", i2);
                } else {
                    this.f8941e.b(e3.c() + "", i2);
                }
            }
        }
    }

    @Override // G.aN
    public void a(String str, String str2) {
        if (str2 != null && str2.equals(this.f8940d.aJ())) {
            d();
            return;
        }
        if (str2 != null && str2.equals(this.f8939c.aJ())) {
            f();
            return;
        }
        if (str2 != null && str2.equals(this.f8938b.aJ())) {
            e();
            return;
        }
        f();
        d();
        e();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void b() {
        try {
            String[] strArrA = a(this.f8939c.i(this.f8937a.p()), this.f8939c.u());
            if (this.f8941e.getRowCount() == strArrA.length / 2) {
                this.f8941e.d(strArrA);
                String[] strArrA2 = this.f8941e.a();
                for (int i2 = 0; i2 < strArrA2.length; i2++) {
                    this.f8941e.a(Double.valueOf(Double.parseDouble(strArrA2[i2])), i2, -1);
                }
            }
            for (int i3 = 0; i3 < strArrA.length; i3++) {
                if (this.f8941e.getRowCount() != strArrA.length / 2) {
                    this.f8941e.b(strArrA[i3], (strArrA.length - 1) - i3);
                } else if (i3 % 2 == 1) {
                }
            }
        } catch (V.g e2) {
            e2.printStackTrace();
            bH.C.a("Y Axis failed on update, it may now be out of sync.", e2, null);
        }
    }

    private String[] a(double[][] dArr, int i2) {
        String[] strArr = new String[dArr.length];
        for (int i3 = 0; i3 < dArr.length; i3++) {
            strArr[i3] = "" + bH.W.b(dArr[i3][0], i2);
        }
        return strArr;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void c() {
        try {
            String[] strArrA = a(this.f8938b.i(this.f8937a.p()), this.f8938b.u());
            for (int i2 = 0; i2 < strArrA.length; i2++) {
                this.f8941e.a(strArrA[i2], i2);
            }
        } catch (V.g e2) {
            e2.printStackTrace();
            bH.C.a("X Axis failed on update, it may now be out of sync.", e2, null);
        }
    }

    private void d() {
        if (this.f8942f != null) {
            this.f8942f.a();
            return;
        }
        this.f8942f = new bR(this);
        this.f8942f.a();
        this.f8942f.start();
    }

    private void e() {
        if (this.f8942f != null) {
            this.f8942f.b();
            return;
        }
        this.f8942f = new bR(this);
        this.f8942f.b();
        this.f8942f.start();
    }

    private void f() {
        if (this.f8942f != null) {
            this.f8942f.c();
            return;
        }
        this.f8942f = new bR(this);
        this.f8942f.c();
        this.f8942f.start();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void g() {
        try {
            double[][] dArrI = this.f8940d.i(this.f8937a.p());
            for (int i2 = 0; i2 < dArrI.length; i2++) {
                for (int i3 = 0; i3 < dArrI[0].length; i3++) {
                    if (dArrI[i2][i3] != this.f8941e.e(i2, i3).doubleValue()) {
                        this.f8941e.a((Object) Double.valueOf(dArrI[i2][i3]), i2, i3);
                    }
                }
            }
        } catch (V.g e2) {
            e2.printStackTrace();
        }
    }
}
