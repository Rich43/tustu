package bx;

import javax.swing.table.DefaultTableModel;

/* loaded from: TunerStudioMS.jar:bx/p.class */
class p extends DefaultTableModel {

    /* renamed from: a, reason: collision with root package name */
    String[] f9216a;

    /* renamed from: b, reason: collision with root package name */
    final /* synthetic */ o f9217b;

    p(o oVar) {
        this.f9217b = oVar;
        this.f9216a = new String[]{this.f9217b.b("Name"), this.f9217b.b("Description")};
        setColumnIdentifiers(this.f9216a);
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
        return this.f9217b.b().size();
    }

    @Override // javax.swing.table.DefaultTableModel, javax.swing.table.TableModel
    public Object getValueAt(int i2, int i3) {
        if (this.f9217b.b().size() > 0) {
            return i3 == 0 ? ((j) this.f9217b.b().get(i2)).a() : ((j) this.f9217b.b().get(i2)).b();
        }
        return null;
    }
}
