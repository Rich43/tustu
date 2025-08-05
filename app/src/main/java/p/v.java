package p;

import bH.aa;
import com.efiAnalytics.ui.eJ;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JTable;

/* loaded from: TunerStudioMS.jar:p/v.class */
public class v extends JTable {

    /* renamed from: a, reason: collision with root package name */
    aa f13247a;

    /* renamed from: c, reason: collision with root package name */
    private final List f13248c = new ArrayList();

    /* renamed from: b, reason: collision with root package name */
    w f13249b = new w(this);

    public v(aa aaVar) {
        this.f13247a = null;
        this.f13247a = aaVar;
        setModel(this.f13249b);
        super.getSelectionModel().setSelectionMode(0);
        super.setRowHeight(getFont().getSize() + eJ.a(3));
    }

    public void a() {
        this.f13249b.fireTableDataChanged();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String b(String str) {
        return this.f13247a != null ? this.f13247a.a(str) : str;
    }

    public void a(S.n nVar) {
        for (int i2 = 0; i2 < this.f13248c.size(); i2++) {
            if (((S.n) this.f13248c.get(i2)).a().equals(nVar.a())) {
                this.f13248c.set(i2, nVar);
                return;
            }
        }
        this.f13248c.add(nVar);
        this.f13249b.fireTableDataChanged();
    }

    S.n a(int i2) {
        if (i2 < 0 || i2 >= this.f13248c.size()) {
            return null;
        }
        return (S.n) this.f13248c.get(i2);
    }

    public S.n b() {
        int selectedRow = getSelectedRow();
        if (selectedRow < 0 || selectedRow >= this.f13248c.size()) {
            return null;
        }
        return (S.n) this.f13248c.get(selectedRow);
    }

    public boolean a(String str) {
        for (int i2 = 0; i2 < this.f13248c.size(); i2++) {
            if (((S.n) this.f13248c.get(i2)).a().equals(str)) {
                this.f13248c.remove(i2);
                return true;
            }
        }
        return false;
    }
}
