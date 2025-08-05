package p;

import javax.swing.table.DefaultTableModel;

/* loaded from: TunerStudioMS.jar:p/S.class */
class S extends DefaultTableModel {

    /* renamed from: a, reason: collision with root package name */
    String[] f13181a;

    /* renamed from: b, reason: collision with root package name */
    final /* synthetic */ R f13182b;

    S(R r2) {
        this.f13182b = r2;
        this.f13181a = new String[]{this.f13182b.b("Name"), this.f13182b.b("Display Name"), this.f13182b.b("Description")};
        setColumnIdentifiers(this.f13181a);
    }

    @Override // javax.swing.table.DefaultTableModel, javax.swing.table.AbstractTableModel, javax.swing.table.TableModel
    public boolean isCellEditable(int i2, int i3) {
        return false;
    }

    @Override // javax.swing.table.DefaultTableModel, javax.swing.table.TableModel
    public int getColumnCount() {
        return 3;
    }

    @Override // javax.swing.table.DefaultTableModel, javax.swing.table.TableModel
    public int getRowCount() {
        return this.f13182b.f13179c.size();
    }

    @Override // javax.swing.table.DefaultTableModel, javax.swing.table.TableModel
    public Object getValueAt(int i2, int i3) {
        if (this.f13182b.f13179c.size() > i2) {
            return i3 == 0 ? ((d.m) this.f13182b.f13179c.get(i2)).l() : i3 == 1 ? ((d.m) this.f13182b.f13179c.get(i2)).b() : ((d.m) this.f13182b.f13179c.get(i2)).j();
        }
        return null;
    }
}
