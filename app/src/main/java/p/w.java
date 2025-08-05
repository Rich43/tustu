package p;

import javax.swing.table.DefaultTableModel;

/* loaded from: TunerStudioMS.jar:p/w.class */
class w extends DefaultTableModel {

    /* renamed from: a, reason: collision with root package name */
    String[] f13250a;

    /* renamed from: b, reason: collision with root package name */
    final /* synthetic */ v f13251b;

    w(v vVar) {
        this.f13251b = vVar;
        this.f13250a = new String[]{this.f13251b.b("Name"), this.f13251b.b("Trigger Expression"), this.f13251b.b("Reset Expression")};
        setColumnIdentifiers(this.f13250a);
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
        return this.f13251b.f13248c.size();
    }

    @Override // javax.swing.table.DefaultTableModel, javax.swing.table.TableModel
    public Object getValueAt(int i2, int i3) {
        if (this.f13251b.f13248c.size() > i2) {
            return i3 == 0 ? ((S.n) this.f13251b.f13248c.get(i2)).a() : i3 == 1 ? ((S.n) this.f13251b.f13248c.get(i2)).d() : ((S.n) this.f13251b.f13248c.get(i2)).e();
        }
        return null;
    }
}
