package av;

import G.aN;
import bH.C;
import bH.W;
import com.efiAnalytics.ui.C1701s;
import com.efiAnalytics.ui.bV;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

/* renamed from: av.j, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:av/j.class */
class C0871j implements aN, TableModelListener {

    /* renamed from: a, reason: collision with root package name */
    C0872k f6294a = null;

    /* renamed from: b, reason: collision with root package name */
    final /* synthetic */ C0870i f6295b;

    C0871j(C0870i c0870i) {
        this.f6295b = c0870i;
    }

    @Override // javax.swing.event.TableModelListener
    public void tableChanged(TableModelEvent tableModelEvent) throws NumberFormatException {
        C1701s c1701s = (C1701s) tableModelEvent.getSource();
        if (tableModelEvent.getFirstRow() == -1) {
            a(tableModelEvent.getColumn(), c1701s.b()[tableModelEvent.getColumn()] + "", c1701s.b()[tableModelEvent.getColumn()] + "");
            return;
        }
        if (tableModelEvent.getColumn() == -1) {
            return;
        }
        try {
            this.f6295b.n().a(this.f6295b.f6292a, c1701s.e(tableModelEvent.getFirstRow(), tableModelEvent.getColumn()).doubleValue(), tableModelEvent.getFirstRow(), tableModelEvent.getColumn());
            if (this.f6295b.D() != null) {
                this.f6295b.D()[tableModelEvent.getFirstRow()][tableModelEvent.getColumn()].a(c1701s.e(tableModelEvent.getFirstRow(), tableModelEvent.getColumn()).doubleValue());
            }
        } catch (V.g e2) {
            e2.printStackTrace();
            bV.d("Error updating table:\n" + e2.getMessage(), null);
        } catch (V.j e3) {
            c1701s.a((Object) new Double(e3.c()), tableModelEvent.getFirstRow(), tableModelEvent.getColumn());
        } catch (Exception e4) {
            e4.printStackTrace();
            bV.d("Error updating table:\n" + e4.getMessage() + "\nSee log for more detail.", null);
        }
    }

    public void a(int i2, String str, String str2) throws NumberFormatException {
        try {
            this.f6295b.l().a(this.f6295b.f6292a, Double.parseDouble(str), i2, 0);
        } catch (V.g e2) {
            e2.printStackTrace();
            C.a("Error updating y Axis.", e2, null);
        } catch (V.j e3) {
            C.b(str + " is out of range.\nLimit:" + e3.c());
            this.f6295b.a(str2, i2);
        }
    }

    @Override // G.aN
    public void a(String str, String str2) {
        if (str2 != null && str2.equals(this.f6295b.n().aJ())) {
            d();
            return;
        }
        if (str2 != null && str2.equals(this.f6295b.m().aJ())) {
            f();
        } else {
            if (str2 == null || !str2.equals(this.f6295b.l().aJ())) {
                return;
            }
            e();
        }
    }

    public void a() {
        b();
        c();
        g();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void b() {
        try {
            String[] strArrA = a(this.f6295b.m().i(this.f6295b.f6292a), this.f6295b.m().u());
            for (int i2 = 0; i2 < strArrA.length; i2++) {
                if (strArrA.length / 2 != this.f6295b.getRowCount()) {
                    this.f6295b.b(strArrA[i2], (strArrA.length - 1) - i2);
                } else if (i2 % 2 == 0) {
                    this.f6295b.b(strArrA[i2 + 1], (this.f6295b.getRowCount() - 1) - (i2 / 2));
                }
            }
        } catch (V.g e2) {
            e2.printStackTrace();
            C.a("Y Axis failed on update, it may now be out of sync.", e2, null);
        }
    }

    private String[] a(double[][] dArr, int i2) {
        String[] strArr = new String[dArr.length];
        for (int i3 = 0; i3 < dArr.length; i3++) {
            strArr[i3] = "" + W.b(dArr[i3][0], i2);
        }
        return strArr;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void c() {
        try {
            String[] strArrA = a(this.f6295b.l().i(this.f6295b.f6292a), this.f6295b.l().u());
            for (int i2 = 0; i2 < strArrA.length; i2++) {
                this.f6295b.a(strArrA[i2], i2);
            }
        } catch (V.g e2) {
            e2.printStackTrace();
            C.a("X Axis failed on update, it may now be out of sync.", e2, null);
        }
    }

    private void d() {
        if (this.f6294a != null) {
            this.f6294a.a();
            return;
        }
        this.f6294a = new C0872k(this);
        this.f6294a.a();
        this.f6294a.start();
    }

    private void e() {
        if (this.f6294a != null) {
            this.f6294a.b();
            return;
        }
        this.f6294a = new C0872k(this);
        this.f6294a.b();
        this.f6294a.start();
    }

    private void f() {
        if (this.f6294a != null) {
            this.f6294a.c();
            return;
        }
        this.f6294a = new C0872k(this);
        this.f6294a.c();
        this.f6294a.start();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void g() {
        try {
            double[][] dArrI = this.f6295b.n().i(this.f6295b.f6292a);
            for (int i2 = 0; i2 < dArrI.length; i2++) {
                for (int i3 = 0; i3 < dArrI[0].length; i3++) {
                    if (dArrI[i2][i3] != this.f6295b.e(i2, i3).doubleValue()) {
                        this.f6295b.a((Object) Double.valueOf(dArrI[i2][i3]), i2, i3);
                    }
                }
            }
        } catch (V.g e2) {
            e2.printStackTrace();
        }
    }
}
