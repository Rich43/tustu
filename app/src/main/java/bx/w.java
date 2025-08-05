package bx;

import javax.swing.table.DefaultTableModel;

/* loaded from: TunerStudioMS.jar:bx/w.class */
class w extends DefaultTableModel {

    /* renamed from: a, reason: collision with root package name */
    String[] f9229a;

    /* renamed from: b, reason: collision with root package name */
    final /* synthetic */ t f9230b;

    w(t tVar) {
        this.f9230b = tVar;
        this.f9229a = new String[]{this.f9230b.f("On"), this.f9230b.f("Filter Name")};
        setColumnIdentifiers(this.f9229a);
    }

    @Override // javax.swing.table.DefaultTableModel, javax.swing.table.AbstractTableModel, javax.swing.table.TableModel
    public boolean isCellEditable(int i2, int i3) {
        return false;
    }

    @Override // javax.swing.table.DefaultTableModel, javax.swing.table.TableModel
    public int getColumnCount() {
        return 2;
    }

    @Override // javax.swing.table.DefaultTableModel, javax.swing.table.TableModel
    public int getRowCount() {
        return this.f9230b.e().size();
    }

    @Override // javax.swing.table.DefaultTableModel, javax.swing.table.TableModel
    public Object getValueAt(int i2, int i3) {
        if (this.f9230b.e().size() <= 0) {
            return null;
        }
        if (i3 == 0) {
            return this.f9230b.c(((j) this.f9230b.e().get(i2)).a()) ? this.f9230b.c() : this.f9230b.d();
        }
        return ((j) this.f9230b.e().get(i2)).a();
    }
}
