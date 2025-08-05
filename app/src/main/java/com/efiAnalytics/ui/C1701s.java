package com.efiAnalytics.ui;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

/* renamed from: com.efiAnalytics.ui.s, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:com/efiAnalytics/ui/s.class */
public class C1701s implements W.an, TableModel {

    /* renamed from: h, reason: collision with root package name */
    Double[][] f11738h;

    /* renamed from: g, reason: collision with root package name */
    ArrayList f11737g = new ArrayList();

    /* renamed from: i, reason: collision with root package name */
    Double[][] f11739i = (Double[][]) null;

    /* renamed from: j, reason: collision with root package name */
    C1562b[][] f11740j = (C1562b[][]) null;

    /* renamed from: k, reason: collision with root package name */
    String[] f11741k = null;

    /* renamed from: l, reason: collision with root package name */
    String[] f11742l = null;

    /* renamed from: m, reason: collision with root package name */
    String f11743m = "MAP";

    /* renamed from: n, reason: collision with root package name */
    String f11744n = "RPM";

    /* renamed from: o, reason: collision with root package name */
    String f11745o = "";

    /* renamed from: p, reason: collision with root package name */
    double f11746p = Double.MAX_VALUE;

    /* renamed from: q, reason: collision with root package name */
    double f11747q = Double.MIN_VALUE;

    /* renamed from: r, reason: collision with root package name */
    boolean f11748r = true;

    /* renamed from: a, reason: collision with root package name */
    private boolean f11749a = true;

    /* renamed from: b, reason: collision with root package name */
    private boolean f11750b = true;

    /* renamed from: s, reason: collision with root package name */
    List f11751s = new ArrayList();

    /* renamed from: c, reason: collision with root package name */
    private int f11752c = -1;

    /* renamed from: d, reason: collision with root package name */
    private boolean f11753d = false;

    /* renamed from: e, reason: collision with root package name */
    private List f11754e = new ArrayList();

    /* renamed from: f, reason: collision with root package name */
    private int f11755f = 0;

    public C1701s() {
        a(16, 16);
    }

    public int o() {
        int i2 = 0;
        if (this.f11738h != null) {
            for (int i3 = 0; i3 < this.f11738h.length; i3++) {
                for (int i4 = 0; i4 < this.f11738h[0].length; i4++) {
                    String string = this.f11738h[i3][i4].toString();
                    if (string.indexOf(".") != -1) {
                        String strSubstring = string.substring(string.indexOf(".") + 1);
                        int length = strSubstring.equals("0") ? 0 : strSubstring.length();
                        if (length > i2) {
                            i2 = length;
                        }
                    }
                }
            }
        }
        return i2;
    }

    public void a(boolean z2) {
        this.f11748r = z2;
    }

    public boolean p() {
        return this.f11748r;
    }

    public void a(int i2, int i3) {
        boolean z2 = this.f11741k == null || this.f11741k.length != i3 || this.f11742l == null || this.f11742l.length != i2;
        this.f11738h = new Double[i2][i3];
        this.f11739i = this.f11738h;
        if (this.f11740j != null && (this.f11740j.length != i2 || this.f11740j[0].length != i3)) {
            this.f11740j = (C1562b[][]) null;
        }
        if (this.f11741k == null || this.f11741k.length != i3) {
            this.f11741k = new String[i3];
        }
        if (this.f11742l == null || this.f11742l.length != i2) {
            this.f11742l = new String[i2];
        }
        q();
        if (z2) {
            f(i2, i3);
        }
    }

    public void q() {
        this.f11739i = c(this.f11738h);
    }

    public boolean r() {
        for (int i2 = 0; i2 < this.f11738h.length; i2++) {
            for (int i3 = 0; i3 < this.f11738h[0].length; i3++) {
                if (!this.f11738h[i2][i3].equals(this.f11739i[i2][i3])) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean b(int i2, int i3) {
        int length = (this.f11738h.length - i2) - 1;
        return (this.f11738h[length][i3] == null || this.f11739i[length][i3] == null || !this.f11738h[length][i3].equals(this.f11739i[length][i3])) ? false : true;
    }

    public Double c(int i2, int i3) {
        return Double.valueOf(this.f11739i[(this.f11739i.length - 1) - i2][i3] != null ? this.f11739i[(this.f11739i.length - 1) - i2][i3].doubleValue() : Double.NaN);
    }

    public Double[][] s() {
        return this.f11739i;
    }

    public void a(Double[][] dArr) {
        if (dArr.length != this.f11738h.length) {
            System.out.println("Clean values sizes don't match for BinTable, Don't know what to do with it");
        }
        this.f11739i = dArr;
    }

    public int t() {
        return this.f11738h.length * this.f11738h[0].length;
    }

    public void u() {
        this.f11746p = Double.MAX_VALUE;
        this.f11747q = -1.7976931348623157E308d;
    }

    public void d(String str) {
        this.f11743m = str;
    }

    public String v() {
        return this.f11743m;
    }

    public void e(String str) {
        this.f11744n = str;
    }

    public String w() {
        return this.f11744n;
    }

    private double a(String str) {
        if (str == null) {
            return Double.NaN;
        }
        try {
            return Double.parseDouble(str);
        } catch (Exception e2) {
            return Double.NaN;
        }
    }

    public void c(String[] strArr) {
        if (this.f11741k == null || this.f11741k.length != strArr.length) {
            this.f11741k = new String[strArr.length];
            for (int i2 = 0; i2 < this.f11741k.length; i2++) {
                double dA = a(strArr[i2]);
                this.f11741k[i2] = strArr[i2];
                a(Double.valueOf(dA), -1, i2);
            }
            return;
        }
        for (int i3 = 0; i3 < this.f11741k.length; i3++) {
            double dA2 = a(this.f11741k[i3]);
            double dA3 = a(strArr[i3]);
            if (dA2 != dA3) {
                this.f11741k[i3] = strArr[i3];
                a(Double.valueOf(dA3), -1, i3);
            }
        }
    }

    public void d(String[] strArr) {
        if (this.f11742l != null && this.f11742l.length == strArr.length / 2) {
            for (int i2 = 0; i2 < strArr.length; i2++) {
                if ((i2 + 1) % 2 == 0) {
                    this.f11742l[(i2 - 1) / 2] = strArr[strArr.length - i2];
                }
            }
            return;
        }
        this.f11742l = new String[strArr.length];
        for (int i3 = 0; i3 < strArr.length; i3++) {
            if (H()) {
                this.f11742l[i3] = strArr[(strArr.length - 1) - i3];
            } else {
                this.f11742l[i3] = strArr[i3];
            }
        }
    }

    public void e(String[] strArr) {
        if (this.f11742l == null || this.f11742l.length != strArr.length) {
            this.f11742l = new String[strArr.length];
            for (int i2 = 0; i2 < this.f11742l.length; i2++) {
                double dA = a(strArr[i2]);
                this.f11742l[i2] = strArr[i2];
                a(Double.valueOf(dA), i2, -1);
            }
            return;
        }
        for (int i3 = 0; i3 < this.f11742l.length; i3++) {
            double dA2 = a(this.f11742l[i3]);
            double dA3 = a(strArr[i3]);
            if (dA2 != dA3) {
                this.f11742l[i3] = strArr[i3];
                a(Double.valueOf(dA3), i3, -1);
            }
        }
    }

    public void a(String str, int i2) {
        this.f11741k[i2] = str;
        a(Double.valueOf(str), -1, i2);
    }

    public void b(String str, int i2) {
        this.f11742l[i2] = str;
        a(Double.valueOf(str), i2, -1);
    }

    @Override // W.an
    public String[] a() {
        if (this.f11742l.length / 2 == getRowCount()) {
            String[] strArr = new String[getRowCount()];
            int i2 = 0;
            for (int i3 = 0; i3 < this.f11742l.length; i3 = i3 + 1 + 1) {
                strArr[i2] = this.f11742l[i3];
                i2++;
            }
            this.f11742l = strArr;
            this.f11749a = false;
        }
        return this.f11742l;
    }

    @Override // W.an
    public String[] b() {
        return this.f11741k;
    }

    public Object[][] x() {
        Object[][] objArr = new Object[this.f11742l.length][1];
        for (int i2 = 0; i2 < this.f11742l.length; i2++) {
            objArr[i2][0] = this.f11742l[i2];
        }
        return objArr;
    }

    public Object[][] y() {
        Object[][] objArr = new Object[this.f11742l.length][1];
        for (int i2 = 0; i2 < this.f11742l.length; i2++) {
            if (this.f11742l[i2] != null) {
                objArr[i2][0] = Double.valueOf((Double.parseDouble(this.f11742l[i2]) - 100.0d) * 0.145038d);
            }
        }
        return objArr;
    }

    public void f(String str) {
        this.f11745o = str;
    }

    public String z() {
        return this.f11745o;
    }

    @Override // javax.swing.table.TableModel
    public int getRowCount() {
        if (this.f11738h == null) {
            return 0;
        }
        return this.f11738h.length;
    }

    @Override // javax.swing.table.TableModel
    public int getColumnCount() {
        return this.f11738h[0].length;
    }

    @Override // javax.swing.table.TableModel
    public String getColumnName(int i2) {
        return (this.f11741k == null || this.f11741k.length <= i2) ? "" : this.f11741k[i2];
    }

    public double A() {
        return this.f11746p;
    }

    public double B() {
        return this.f11747q;
    }

    @Override // javax.swing.table.TableModel
    public Class getColumnClass(int i2) {
        return Double.class;
    }

    @Override // javax.swing.table.TableModel
    public boolean isCellEditable(int i2, int i3) {
        return true;
    }

    @Override // W.an, javax.swing.table.TableModel
    /* renamed from: d, reason: merged with bridge method [inline-methods] */
    public Double getValueAt(int i2, int i3) {
        return (this.f11755f != 1 || this.f11740j == null) ? (this.f11755f != 3 || this.f11740j == null) ? (this.f11755f != 2 || this.f11740j == null) ? this.f11738h[(this.f11738h.length - 1) - i2][i3] : this.f11740j[(this.f11740j.length - 1) - i2][i3].g() : this.f11740j[(this.f11740j.length - 1) - i2][i3].f() : this.f11740j[(this.f11740j.length - 1) - i2][i3].i();
    }

    public Double e(int i2, int i3) {
        return this.f11738h[i2][i3];
    }

    public double a(double d2, double d3) {
        int i2 = (int) d2;
        int i3 = (int) d3;
        int i4 = i2 < this.f11738h.length - 1 ? i2 + 1 : i2;
        int i5 = i3 < this.f11738h[0].length - 1 ? i3 + 1 : i3;
        double d4 = H() ? d2 - i2 : i2;
        double d5 = H() ? d3 - i3 : i3;
        return (this.f11738h[i2][i3].doubleValue() * (1.0d - d5) * (1.0d - d4)) + (this.f11738h[i2][i5].doubleValue() * d5 * (1.0d - d4)) + (this.f11738h[i4][i3].doubleValue() * (1.0d - d5) * d4) + (this.f11738h[i4][i5].doubleValue() * d5 * d4);
    }

    public void b(Double[][] dArr) {
        this.f11738h = dArr;
    }

    public void a(double[][] dArr) {
        a(dArr.length, dArr[0].length);
        Double[][] dArr2 = new Double[dArr.length][dArr[0].length];
        for (int i2 = 0; i2 < dArr.length; i2++) {
            for (int i3 = 0; i3 < dArr[0].length; i3++) {
                dArr2[i2][i3] = new Double(dArr[i2][i3]);
            }
        }
        b(dArr2);
    }

    public void a(Object obj, int i2, int i3) {
        Double dA = bH.H.a(obj);
        if (dA == null) {
            return;
        }
        boolean z2 = this.f11738h[i2][i3] != null && (this.f11738h[i2][i3].doubleValue() == this.f11747q || this.f11738h[i2][i3].doubleValue() == this.f11746p);
        if (dA.doubleValue() > this.f11747q) {
            this.f11747q = dA.doubleValue();
        }
        if (dA.doubleValue() < this.f11746p) {
            this.f11746p = dA.doubleValue();
        }
        this.f11738h[i2][i3] = dA;
        if (z2) {
            C();
        }
        if (!this.f11753d) {
            a(dA, i2, i3);
        } else {
            if (g(i2, i3)) {
                return;
            }
            this.f11754e.add(new C1702t(this, i2, i3));
        }
    }

    private boolean g(int i2, int i3) {
        for (C1702t c1702t : this.f11754e) {
            if (c1702t.f11757b == i3 && c1702t.f11756a == i2) {
                return true;
            }
        }
        return false;
    }

    private void c() {
        for (C1702t c1702t : this.f11754e) {
            a(Double.valueOf(this.f11738h[c1702t.f11756a][c1702t.f11757b].doubleValue()), c1702t.f11756a, c1702t.f11757b);
        }
    }

    @Override // javax.swing.table.TableModel
    public void setValueAt(Object obj, int i2, int i3) {
        a(obj, H() ? (this.f11738h.length - 1) - i2 : i2, i3);
    }

    public void C() {
        u();
        for (int i2 = 0; i2 < this.f11738h.length; i2++) {
            for (int i3 = 0; i3 < this.f11738h[0].length; i3++) {
                if (getValueAt(i2, i3) != null) {
                    Double valueAt = getValueAt(i2, i3);
                    double dDoubleValue = valueAt != null ? valueAt.doubleValue() : Double.NaN;
                    if (dDoubleValue > this.f11747q) {
                        this.f11747q = dDoubleValue;
                    }
                    if (dDoubleValue < this.f11746p) {
                        this.f11746p = dDoubleValue;
                    }
                }
            }
        }
    }

    public void a(Double d2, int i2, int i3) {
        Iterator it = this.f11737g.iterator();
        while (it.hasNext()) {
            ((TableModelListener) it.next()).tableChanged(new TableModelEvent(this, i2, i2, i3));
        }
    }

    @Override // javax.swing.table.TableModel
    public void addTableModelListener(TableModelListener tableModelListener) {
        this.f11737g.add(tableModelListener);
    }

    @Override // javax.swing.table.TableModel
    public void removeTableModelListener(TableModelListener tableModelListener) {
        this.f11737g.remove(tableModelListener);
    }

    public String toString() {
        String str = "";
        for (int i2 = 0; i2 < this.f11738h.length; i2++) {
            String str2 = str + this.f11742l[i2] + "\t";
            for (int i3 = 0; i3 < this.f11738h[i2].length; i3++) {
                str2 = this.f11752c < 0 ? str2 + ((Object) getValueAt(i2, i3)) + "\t" : str2 + bH.W.b(getValueAt(i2, i3).doubleValue(), this.f11752c) + "\t";
            }
            str = str2 + "\n";
        }
        String str3 = str + "    \t";
        for (int i4 = 0; i4 < this.f11741k.length; i4++) {
            str3 = str3 + this.f11741k[i4] + "\t";
        }
        return str3;
    }

    private Double[][] c(Double[][] dArr) {
        Double[][] dArr2 = new Double[dArr.length][dArr[0].length];
        for (int i2 = 0; i2 < dArr2.length; i2++) {
            for (int i3 = 0; i3 < dArr2[0].length; i3++) {
                dArr2[i2][i3] = dArr[i2][i3];
            }
        }
        return dArr2;
    }

    public C1562b[][] D() {
        return this.f11740j;
    }

    public C1562b[][] a(C1701s c1701s, C1589c c1589c) {
        String[] strArrA = c1701s.a();
        if (this.f11740j == null) {
            C1562b[][] c1562bArr = new C1562b[getRowCount()][getColumnCount()];
            for (int i2 = 0; i2 < c1562bArr.length; i2++) {
                for (int i3 = 0; i3 < c1562bArr[0].length; i3++) {
                    c1562bArr[(c1562bArr.length - i2) - 1][i3] = new C1562b(c1589c);
                    c1562bArr[(c1562bArr.length - i2) - 1][i3].a(getValueAt(i2, i3).doubleValue());
                    c1562bArr[(c1562bArr.length - i2) - 1][i3].c(c1701s.a((strArrA.length - C1677fh.a(strArrA, Double.parseDouble(a()[i2]))) - 1.0d, C1677fh.b(c1701s.b(), Double.parseDouble(b()[i3]))));
                }
            }
            this.f11740j = c1562bArr;
        } else {
            C1562b[][] c1562bArr2 = this.f11740j;
            for (int i4 = 0; i4 < c1562bArr2.length; i4++) {
                for (int i5 = 0; i5 < c1562bArr2[0].length; i5++) {
                    c1562bArr2[(c1562bArr2.length - i4) - 1][i5].n();
                    c1562bArr2[(c1562bArr2.length - i4) - 1][i5].a(getValueAt(i4, i5).doubleValue());
                    if (c1589c != null) {
                        c1562bArr2[(c1562bArr2.length - i4) - 1][i5].a(c1589c);
                    }
                    c1562bArr2[(c1562bArr2.length - i4) - 1][i5].c(c1701s.a((c1701s.getRowCount() - C1677fh.a(strArrA, Double.parseDouble(a()[i4]))) - 1.0d, C1677fh.b(c1701s.b(), Double.parseDouble(b()[i5]))));
                }
            }
        }
        return this.f11740j;
    }

    public C1562b[][] a(C1589c c1589c) {
        if (this.f11740j == null) {
            C1562b[][] c1562bArr = new C1562b[getRowCount()][getColumnCount()];
            for (int i2 = 0; i2 < c1562bArr.length; i2++) {
                for (int i3 = 0; i3 < c1562bArr[0].length; i3++) {
                    c1562bArr[(c1562bArr.length - i2) - 1][i3] = new C1562b(c1589c);
                    Double valueAt = getValueAt(i2, i3);
                    if (valueAt == null) {
                        valueAt = Double.valueOf(Double.NaN);
                    }
                    c1562bArr[(c1562bArr.length - i2) - 1][i3].a(valueAt.doubleValue());
                }
            }
            this.f11740j = c1562bArr;
        } else {
            C1562b[][] c1562bArr2 = this.f11740j;
            for (int i4 = 0; i4 < c1562bArr2.length; i4++) {
                for (int i5 = 0; i5 < c1562bArr2[0].length; i5++) {
                    if (c1589c != null) {
                        c1562bArr2[(c1562bArr2.length - i4) - 1][i5].a(c1589c);
                    }
                    Double valueAt2 = getValueAt(i4, i5);
                    if (valueAt2 == null) {
                        c1562bArr2[(c1562bArr2.length - i4) - 1][i5].b(Double.NaN);
                    } else {
                        c1562bArr2[(c1562bArr2.length - i4) - 1][i5].b(valueAt2.doubleValue());
                    }
                }
            }
        }
        return this.f11740j;
    }

    public int E() {
        return this.f11755f;
    }

    public void c(int i2) {
        if (i2 == 0 || i2 == 1 || i2 == 3 || i2 == 2) {
            this.f11755f = i2;
        }
    }

    public double d(int i2) {
        return a(this.f11741k[i2]);
    }

    public double e(int i2) {
        return a(this.f11742l[(this.f11738h.length - i2) - 1]);
    }

    public double F() {
        return Math.max(e(0), e(this.f11742l.length - 1));
    }

    public void a(C1562b[][] c1562bArr) {
        this.f11740j = c1562bArr;
    }

    public boolean G() {
        return this.f11749a;
    }

    public void f(int i2, int i3) {
        Iterator it = this.f11751s.iterator();
        while (it.hasNext()) {
            ((InterfaceC1670fa) it.next()).a(i2, i3);
        }
    }

    public void a(InterfaceC1670fa interfaceC1670fa) {
        this.f11751s.add(interfaceC1670fa);
    }

    public void b(InterfaceC1670fa interfaceC1670fa) {
        this.f11751s.remove(interfaceC1670fa);
    }

    public void a(float f2, float f3) {
        for (int i2 = 0; i2 < this.f11741k.length; i2++) {
            this.f11741k[i2] = bH.W.b(f2 + ((i2 * (f3 - f2)) / (this.f11741k.length - 1)), 6);
        }
    }

    public void b(float f2, float f3) {
        for (int i2 = 0; i2 < this.f11742l.length; i2++) {
            this.f11742l[(this.f11742l.length - i2) - 1] = bH.W.b(f2 + ((i2 * (f3 - f2)) / (this.f11742l.length - 1)), 6);
        }
    }

    public boolean H() {
        if (this.f11750b) {
            return true;
        }
        double dE = e(0);
        double dE2 = e(this.f11742l.length - 1);
        return Double.isNaN(dE) || Double.isNaN(dE2) || dE < dE2;
    }

    public boolean I() {
        return this.f11750b;
    }

    public void b(boolean z2) {
        this.f11750b = z2;
    }

    public int J() {
        return this.f11752c;
    }

    public void f(int i2) {
        this.f11752c = i2;
    }

    public boolean K() {
        return this.f11753d;
    }

    public void c(boolean z2) {
        this.f11753d = z2;
        if (z2) {
            return;
        }
        c();
    }
}
