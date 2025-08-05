package com.efiAnalytics.ui;

import java.util.ArrayList;
import java.util.Iterator;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

/* renamed from: com.efiAnalytics.ui.v, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:com/efiAnalytics/ui/v.class */
public class C1704v implements cD, TableModelListener {

    /* renamed from: b, reason: collision with root package name */
    private C1701s f11759b;

    /* renamed from: c, reason: collision with root package name */
    private int f11760c = 1;

    /* renamed from: a, reason: collision with root package name */
    ArrayList f11761a = new ArrayList();

    public C1704v(C1701s c1701s) {
        this.f11759b = null;
        this.f11759b = c1701s;
        c1701s.addTableModelListener(this);
    }

    @Override // com.efiAnalytics.ui.cD
    public int a() {
        return this.f11759b.getColumnCount();
    }

    @Override // com.efiAnalytics.ui.cD
    public double a(int i2) {
        return Double.parseDouble(this.f11759b.b()[i2]);
    }

    @Override // com.efiAnalytics.ui.cD
    public int b() {
        return this.f11759b.getRowCount();
    }

    @Override // com.efiAnalytics.ui.cD
    public double b(int i2) {
        return Double.parseDouble(this.f11759b.a()[i2]);
    }

    @Override // com.efiAnalytics.ui.cD
    public double a(int i2, int i3) {
        if (this.f11760c == 1) {
            return this.f11759b.getValueAt(i3, i2).doubleValue();
        }
        if (this.f11760c == 2) {
            if (this.f11759b.D() != null) {
                return this.f11759b.D()[(this.f11759b.D().length - 1) - i3][i2].k();
            }
            return 0.0d;
        }
        if (this.f11760c == 4) {
            if (this.f11759b.D() != null) {
                return this.f11759b.D()[i3][i2].j();
            }
            return 0.0d;
        }
        if (this.f11760c != 3) {
            bH.C.a("HeatMap: Unknown Z Value Mode");
            return 0.0d;
        }
        if (this.f11759b.D() != null) {
            return this.f11759b.getValueAt(i3, i2).doubleValue() - this.f11759b.c(i3, i2).doubleValue();
        }
        return 0.0d;
    }

    public void c(int i2) {
        this.f11760c = i2;
    }

    @Override // com.efiAnalytics.ui.cD
    public void a(cE cEVar) {
        this.f11761a.add(cEVar);
    }

    private void b(int i2, int i3) {
        Iterator it = this.f11761a.iterator();
        while (it.hasNext()) {
            ((cE) it.next()).a(i2, i3);
        }
    }

    @Override // javax.swing.event.TableModelListener
    public void tableChanged(TableModelEvent tableModelEvent) {
        b(tableModelEvent.getColumn(), tableModelEvent.getFirstRow());
    }

    public void a(C1701s c1701s) {
        this.f11759b.removeTableModelListener(this);
        this.f11759b = c1701s;
        c1701s.addTableModelListener(this);
    }
}
