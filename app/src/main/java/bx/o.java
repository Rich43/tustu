package bx;

import bH.aa;
import com.efiAnalytics.ui.eJ;
import java.util.ArrayList;
import javax.swing.JTable;

/* loaded from: TunerStudioMS.jar:bx/o.class */
public class o extends JTable {

    /* renamed from: a, reason: collision with root package name */
    aa f9213a;

    /* renamed from: c, reason: collision with root package name */
    private ArrayList f9214c = new ArrayList();

    /* renamed from: b, reason: collision with root package name */
    p f9215b = new p(this);

    public o(aa aaVar) {
        this.f9213a = null;
        this.f9213a = aaVar;
        setModel(this.f9215b);
        setSelectionMode(0);
        setRowSelectionAllowed(true);
        getColumnModel().getColumn(0).setMinWidth(eJ.a(110));
        getColumnModel().getColumn(0).setMaxWidth(eJ.a(120));
        setRowHeight(getFont().getSize() + eJ.a(2));
    }

    public void a(j jVar) {
        this.f9214c.add(jVar);
        a();
    }

    public void b(j jVar) {
        for (int i2 = 0; i2 < this.f9214c.size(); i2++) {
            if (((j) this.f9214c.get(i2)).a().equals(jVar.a())) {
                this.f9214c.set(i2, jVar);
                a();
                return;
            }
        }
        a(jVar);
    }

    public void a() {
        this.f9215b.fireTableDataChanged();
    }

    public boolean a(String str) {
        for (int i2 = 0; i2 < this.f9214c.size(); i2++) {
            if (((j) this.f9214c.get(i2)).a().equals(str)) {
                this.f9214c.remove(i2);
                this.f9215b.fireTableDataChanged();
                return true;
            }
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String b(String str) {
        return this.f9213a != null ? this.f9213a.a(str) : str;
    }

    public ArrayList b() {
        return this.f9214c;
    }

    public j a(int i2) {
        if (i2 < 0 || i2 >= this.f9214c.size()) {
            return null;
        }
        return (j) this.f9214c.get(i2);
    }
}
