package bF;

import bH.W;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

/* loaded from: TunerStudioMS.jar:bF/y.class */
public class y implements TableModel {

    /* renamed from: a, reason: collision with root package name */
    List f6893a = new ArrayList();

    /* renamed from: d, reason: collision with root package name */
    private x f6894d = new z(this);

    /* renamed from: b, reason: collision with root package name */
    ArrayList f6895b = new ArrayList();

    /* renamed from: c, reason: collision with root package name */
    ArrayList f6896c = new ArrayList();

    /* renamed from: e, reason: collision with root package name */
    private boolean f6897e = false;

    /* renamed from: f, reason: collision with root package name */
    private boolean f6898f = false;

    /* renamed from: g, reason: collision with root package name */
    private boolean f6899g = false;

    /* renamed from: h, reason: collision with root package name */
    private boolean f6900h = false;

    /* renamed from: i, reason: collision with root package name */
    private boolean f6901i = false;

    /* renamed from: j, reason: collision with root package name */
    private List f6902j = new ArrayList();

    public void a(C c2) {
        this.f6893a.add(c2);
    }

    public int a() {
        return this.f6893a.size();
    }

    @Override // javax.swing.table.TableModel
    public int getRowCount() {
        return this.f6897e ? b() : k();
    }

    public int b() {
        int iA = 0;
        for (C c2 : this.f6893a) {
            if (c2.a() > iA) {
                iA = c2.a();
            }
        }
        return iA;
    }

    @Override // javax.swing.table.TableModel
    public int getColumnCount() {
        return this.f6897e ? k() : b();
    }

    @Override // javax.swing.table.TableModel
    public String getColumnName(int i2) {
        return this.f6897e ? c(i2).b() : this.f6894d.a(i2);
    }

    public void a(x xVar) {
        this.f6894d = xVar;
    }

    @Override // javax.swing.table.TableModel
    public Class getColumnClass(int i2) {
        return Double.class;
    }

    @Override // javax.swing.table.TableModel
    public boolean isCellEditable(int i2, int i3) {
        return true;
    }

    @Override // javax.swing.table.TableModel
    public Object getValueAt(int i2, int i3) {
        return this.f6897e ? c(i3).a(i2) : c(i2).a(i3);
    }

    @Override // javax.swing.table.TableModel
    public void setValueAt(Object obj, int i2, int i3) {
        Double dA = bH.H.a(obj);
        if (dA == null) {
            return;
        }
        if (this.f6897e) {
            i3 = i2;
            i2 = i3;
        }
        int iA = a(i2);
        if (((C) this.f6893a.get(iA)).a(i3).equals(dA)) {
            return;
        }
        ((C) this.f6893a.get(iA)).a(i3, dA);
        if (!this.f6901i) {
            a(dA, iA, i3);
        } else {
            if (d(iA, i3)) {
                return;
            }
            this.f6902j.add(new A(this, iA, i3));
        }
    }

    private boolean d(int i2, int i3) {
        for (A a2 : this.f6902j) {
            if (a2.f6778b == i3 && a2.f6777a == i2) {
                return true;
            }
        }
        return false;
    }

    public void a(Object obj, int i2, int i3) {
        if (obj instanceof String) {
            obj = Double.valueOf(Double.parseDouble((String) obj));
        }
        Double d2 = (Double) obj;
        if (this.f6897e) {
            i3 = i2;
            i2 = i3;
        }
        if (((C) this.f6893a.get(i2)).a(i3).equals(d2)) {
            return;
        }
        ((C) this.f6893a.get(i2)).a(i3, d2);
        a(d2, i2, i3);
    }

    private void a(Double d2, int i2, int i3) {
        Iterator it = this.f6895b.iterator();
        while (it.hasNext()) {
            ((B) it.next()).a(i2, i3, d2.doubleValue());
        }
        if (this.f6897e) {
            i3 = i2;
            i2 = i3;
        }
        int iB = b(i2);
        Iterator it2 = this.f6896c.iterator();
        while (it2.hasNext()) {
            ((TableModelListener) it2.next()).tableChanged(new TableModelEvent(this, iB, iB, i3));
        }
    }

    public C a(int i2, int i3) {
        return this.f6897e ? c(i3) : c(i2);
    }

    public boolean b(int i2, int i3) {
        if (this.f6897e) {
            i3 = i2;
            i2 = i3;
        }
        return c(i2).b(i3);
    }

    public double c(int i2, int i3) {
        if (this.f6897e) {
            i3 = i2;
            i2 = i3;
        }
        return c(i2).c(i3);
    }

    public void a(B b2) {
        if (this.f6895b.contains(b2)) {
            return;
        }
        this.f6895b.add(b2);
    }

    public void b(B b2) {
        this.f6895b.remove(b2);
    }

    @Override // javax.swing.table.TableModel
    public void addTableModelListener(TableModelListener tableModelListener) {
        if (this.f6896c.contains(tableModelListener)) {
            return;
        }
        this.f6896c.add(tableModelListener);
    }

    @Override // javax.swing.table.TableModel
    public void removeTableModelListener(TableModelListener tableModelListener) {
        this.f6896c.remove(tableModelListener);
    }

    public x c() {
        return this.f6894d;
    }

    public String[][] d() {
        if (this.f6897e) {
            String[][] strArr = new String[b()][1];
            for (int i2 = 0; i2 < strArr.length; i2++) {
                strArr[i2][0] = this.f6894d.a(i2);
            }
            return strArr;
        }
        String[][] strArr2 = new String[k()][1];
        for (int i3 = 0; i3 < strArr2.length; i3++) {
            strArr2[i3][0] = c(i3).b();
        }
        return strArr2;
    }

    public String[] e() {
        if (g()) {
            String[] strArr = new String[((C) this.f6893a.get(0)).a()];
            for (int i2 = 0; i2 < strArr.length; i2++) {
                strArr[i2] = W.c(((C) this.f6893a.get(0)).a(i2).doubleValue(), ((C) this.f6893a.get(0)).c());
            }
            return strArr;
        }
        if (h()) {
            String[] strArr2 = new String[((C) this.f6893a.get(this.f6893a.size() - 1)).a()];
            for (int i3 = 0; i3 < strArr2.length; i3++) {
                strArr2[i3] = W.c(((C) this.f6893a.get(this.f6893a.size() - 1)).a(i3).doubleValue(), ((C) this.f6893a.get(this.f6893a.size() - 1)).c());
            }
            return strArr2;
        }
        String[] strArr3 = new String[b()];
        for (int i4 = 0; i4 < strArr3.length; i4++) {
            strArr3[i4] = this.f6894d.a(i4);
        }
        return strArr3;
    }

    public boolean f() {
        return this.f6897e;
    }

    public void a(boolean z2) {
        this.f6897e = z2;
    }

    public boolean g() {
        return this.f6898f;
    }

    public void b(boolean z2) {
        this.f6898f = z2;
    }

    public void c(boolean z2) {
        this.f6900h = z2;
    }

    private int k() {
        int i2 = 0;
        for (int i3 = 0; i3 < this.f6893a.size(); i3++) {
            if (!((C) this.f6893a.get(i3)).g()) {
                i2++;
            }
        }
        return this.f6893a.size() - i2;
    }

    private int a(int i2) {
        int i3 = 0;
        for (int i4 = 0; i4 < this.f6893a.size(); i4++) {
            if (!((C) this.f6893a.get(i4)).g()) {
                i3++;
            } else if (i4 - i3 == i2) {
                return i4;
            }
        }
        return i2;
    }

    private int b(int i2) {
        int i3 = 0;
        for (int i4 = 0; i4 < this.f6893a.size() && i4 < i2; i4++) {
            if (!((C) this.f6893a.get(i4)).g()) {
                i3++;
            } else if (i4 - i3 == i2) {
                return i4;
            }
        }
        return i2 - i3;
    }

    private C c(int i2) {
        int i3 = 0;
        for (int i4 = 0; i4 < this.f6893a.size(); i4++) {
            if (!((C) this.f6893a.get(i4)).g()) {
                i3++;
            } else if (i4 - i3 == i2) {
                return (C) this.f6893a.get(i4);
            }
        }
        return null;
    }

    public boolean h() {
        return this.f6899g;
    }

    public void d(boolean z2) {
        this.f6899g = z2;
    }

    public Double[][] i() {
        Double[][] dArr = new Double[this.f6893a.size()][((C) this.f6893a.get(0)).a()];
        for (int i2 = 0; i2 < this.f6893a.size(); i2++) {
            C c2 = (C) this.f6893a.get(i2);
            for (int i3 = 0; i3 < c2.a(); i3++) {
                dArr[i2][i3] = c2.a(i3);
            }
        }
        return dArr;
    }

    public void a(Double[][] dArr) {
        for (int i2 = 0; i2 < this.f6893a.size(); i2++) {
            C c2 = (C) this.f6893a.get(i2);
            for (int i3 = 0; i3 < c2.a(); i3++) {
                c2.b(i3, dArr[i2][i3]);
            }
        }
    }

    private void l() {
        for (A a2 : this.f6902j) {
            a(Double.valueOf(((C) this.f6893a.get(a2.f6777a)).a(a2.f6778b).doubleValue()), a2.f6777a, a2.f6778b);
        }
    }

    public boolean j() {
        return this.f6901i;
    }

    public void e(boolean z2) {
        this.f6901i = z2;
        if (z2) {
            return;
        }
        l();
    }
}
