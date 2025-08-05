package bC;

import java.util.List;
import javax.swing.table.DefaultTableModel;

/* loaded from: TunerStudioMS.jar:bC/d.class */
class d extends DefaultTableModel {

    /* renamed from: a, reason: collision with root package name */
    String[] f6572a;

    /* renamed from: b, reason: collision with root package name */
    final /* synthetic */ b f6573b;

    d(b bVar) {
        this.f6573b = bVar;
        this.f6572a = new String[]{this.f6573b.a("Standard Name"), this.f6573b.a("Imported Log Names")};
        setColumnIdentifiers(this.f6572a);
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
        if (this.f6573b.a() != null) {
            return this.f6573b.a().size();
        }
        return 0;
    }

    @Override // javax.swing.table.DefaultTableModel, javax.swing.table.TableModel
    public Object getValueAt(int i2, int i3) {
        if (this.f6573b.a().size() <= 0) {
            return null;
        }
        if (i3 == 0) {
            return ((Z.e) this.f6573b.a().get(i2)).a();
        }
        Z.e eVar = (Z.e) this.f6573b.a().get(i2);
        StringBuilder sb = new StringBuilder();
        List listB = eVar.b();
        for (int i4 = 0; i4 < listB.size(); i4++) {
            sb.append((String) listB.get(i4));
            if (i4 < listB.size() - 1) {
                sb.append(", ");
            }
        }
        return sb.toString();
    }
}
