package bB;

import javax.swing.table.DefaultTableModel;

/* loaded from: TunerStudioMS.jar:bB/k.class */
class k extends DefaultTableModel {

    /* renamed from: a, reason: collision with root package name */
    String[] f6555a;

    /* renamed from: b, reason: collision with root package name */
    final /* synthetic */ h f6556b;

    k(h hVar) {
        this.f6556b = hVar;
        this.f6555a = new String[]{this.f6556b.b("Field Name"), this.f6556b.b("Scaling"), this.f6556b.b("Digits")};
        setColumnIdentifiers(this.f6555a);
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
        if (this.f6556b.c() != null) {
            return this.f6556b.c().size();
        }
        return 0;
    }

    @Override // javax.swing.table.DefaultTableModel, javax.swing.table.TableModel
    public Object getValueAt(int i2, int i3) {
        if (this.f6556b.c().size() <= 0) {
            return null;
        }
        if (i3 == 0) {
            return ((r) this.f6556b.c().get(i2)).e();
        }
        if (i3 != 1) {
            r rVar = (r) this.f6556b.c().get(i2);
            return rVar.f() == -1 ? "Auto" : Integer.valueOf(rVar.f());
        }
        r rVar2 = (r) this.f6556b.c().get(i2);
        StringBuilder sb = new StringBuilder();
        sb.append(this.f6556b.b("Min")).append(": ").append(rVar2.c() ? "Auto" : Double.valueOf(rVar2.a())).append(" / ").append(this.f6556b.b("Max")).append(": ").append(rVar2.d() ? "Auto" : Double.valueOf(rVar2.b()));
        return sb.toString();
    }
}
