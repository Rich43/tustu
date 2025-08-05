package bb;

import javax.swing.table.DefaultTableModel;

/* renamed from: bb.m, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:bb/m.class */
class C1048m extends DefaultTableModel {

    /* renamed from: a, reason: collision with root package name */
    String[] f7782a;

    /* renamed from: b, reason: collision with root package name */
    final /* synthetic */ C1047l f7783b;

    C1048m(C1047l c1047l) {
        this.f7783b = c1047l;
        this.f7782a = new String[]{this.f7783b.a("Firmware Loader"), this.f7783b.a("Description")};
        setColumnIdentifiers(this.f7782a);
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
        return this.f7783b.b().size();
    }

    @Override // javax.swing.table.DefaultTableModel, javax.swing.table.TableModel
    public Object getValueAt(int i2, int i3) {
        if (this.f7783b.b().size() > 0) {
            return i3 == 0 ? ((ae.q) this.f7783b.b().get(i2)).a() : ((ae.q) this.f7783b.b().get(i2)).b();
        }
        return null;
    }
}
