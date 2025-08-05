package bg;

import G.C0073bf;
import bH.W;
import java.util.Collections;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import s.C1818g;

/* renamed from: bg.q, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:bg/q.class */
class C1137q extends DefaultTableModel {

    /* renamed from: a, reason: collision with root package name */
    String[] f8094a = {C1818g.b("Name"), C1818g.b("Tab Title"), C1818g.b("Enabled Condition"), C1818g.b("Default Index")};

    /* renamed from: b, reason: collision with root package name */
    final /* synthetic */ C1135o f8095b;

    C1137q(C1135o c1135o) {
        this.f8095b = c1135o;
    }

    @Override // javax.swing.table.DefaultTableModel, javax.swing.table.TableModel
    public int getRowCount() {
        return this.f8095b.f8090b.size();
    }

    @Override // javax.swing.table.DefaultTableModel, javax.swing.table.TableModel
    public int getColumnCount() {
        return 4;
    }

    @Override // javax.swing.table.DefaultTableModel, javax.swing.table.AbstractTableModel, javax.swing.table.TableModel
    public String getColumnName(int i2) {
        return this.f8094a[i2];
    }

    @Override // javax.swing.table.DefaultTableModel, javax.swing.table.AbstractTableModel, javax.swing.table.TableModel
    public boolean isCellEditable(int i2, int i3) {
        return i3 <= 3;
    }

    @Override // javax.swing.table.DefaultTableModel, javax.swing.table.TableModel
    public Object getValueAt(int i2, int i3) {
        if (i3 == 0) {
            return ((C0073bf) this.f8095b.f8090b.get(i2)).aJ();
        }
        if (i3 == 1) {
            return ((C0073bf) this.f8095b.f8090b.get(i2)).c();
        }
        if (i3 == 2) {
            return ((C0073bf) this.f8095b.f8090b.get(i2)).aH().trim().isEmpty() ? "Always" : ((C0073bf) this.f8095b.f8090b.get(i2)).aH();
        }
        if (i3 == 3) {
            return Integer.valueOf(((C0073bf) this.f8095b.f8090b.get(i2)).f());
        }
        return null;
    }

    @Override // javax.swing.table.DefaultTableModel, javax.swing.table.AbstractTableModel, javax.swing.table.TableModel
    public void setValueAt(Object obj, int i2, int i3) {
        C0073bf c0073bf = (C0073bf) this.f8095b.f8090b.get(i2);
        switch (i3) {
            case 0:
                c0073bf.v(obj.toString());
                break;
            case 1:
                c0073bf.c(obj.toString());
                break;
            case 2:
                c0073bf.u(W.b(obj.toString(), "Always", "").trim());
                break;
            case 3:
                c0073bf.a(Integer.parseInt(obj.toString()));
                a();
                break;
        }
    }

    private void a() {
        Collections.sort(this.f8095b.f8090b, new C1138r(this));
        for (int i2 = 0; i2 < this.f8095b.f8090b.size(); i2++) {
            ((C0073bf) this.f8095b.f8090b.get(i2)).a(i2);
        }
        SwingUtilities.invokeLater(new RunnableC1139s(this));
    }
}
