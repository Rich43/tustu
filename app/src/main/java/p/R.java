package p;

import bH.aa;
import com.efiAnalytics.ui.eJ;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JTable;

/* loaded from: TunerStudioMS.jar:p/R.class */
public class R extends JTable {

    /* renamed from: a, reason: collision with root package name */
    aa f13178a;

    /* renamed from: c, reason: collision with root package name */
    private final List f13179c = new ArrayList();

    /* renamed from: b, reason: collision with root package name */
    S f13180b = new S(this);

    public R(aa aaVar) {
        this.f13178a = null;
        this.f13178a = aaVar;
        setModel(this.f13180b);
        super.getSelectionModel().setSelectionMode(0);
        super.setRowHeight(getFont().getSize() + eJ.a(3));
    }

    public void a() {
        this.f13180b.fireTableDataChanged();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String b(String str) {
        return this.f13178a != null ? this.f13178a.a(str) : str;
    }

    public void a(d.m mVar) {
        for (int i2 = 0; i2 < this.f13179c.size(); i2++) {
            if (((d.m) this.f13179c.get(i2)).l().equals(mVar.l())) {
                this.f13179c.set(i2, mVar);
                return;
            }
        }
        this.f13179c.add(mVar);
        this.f13180b.fireTableDataChanged();
    }

    d.m a(int i2) {
        if (i2 < 0 || i2 >= this.f13179c.size()) {
            return null;
        }
        return (d.m) this.f13179c.get(i2);
    }

    public d.m b() {
        int selectedRow = getSelectedRow();
        if (selectedRow < 0 || selectedRow >= this.f13179c.size()) {
            return null;
        }
        return (d.m) this.f13179c.get(selectedRow);
    }

    public boolean a(String str) {
        for (int i2 = 0; i2 < this.f13179c.size(); i2++) {
            if (((d.m) this.f13179c.get(i2)).a().equals(str)) {
                this.f13179c.remove(i2);
                return true;
            }
        }
        return false;
    }
}
