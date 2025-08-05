package bC;

import bH.aa;
import com.efiAnalytics.ui.eJ;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.swing.JTable;

/* loaded from: TunerStudioMS.jar:bC/b.class */
public class b extends JTable {

    /* renamed from: a, reason: collision with root package name */
    aa f6568a;

    /* renamed from: b, reason: collision with root package name */
    d f6569b = new d(this);

    /* renamed from: c, reason: collision with root package name */
    private List f6570c = new ArrayList();

    public b(aa aaVar) {
        this.f6568a = null;
        this.f6568a = aaVar;
        setModel(this.f6569b);
        setSelectionMode(0);
        setRowSelectionAllowed(true);
        getColumnModel().getColumn(0).setMinWidth(eJ.a(110));
        getColumnModel().getColumn(0).setMaxWidth(eJ.a(120));
        setRowHeight(getFont().getSize() + eJ.a(2));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String a(String str) {
        return this.f6568a != null ? this.f6568a.a(str) : str;
    }

    public void a(List list) {
        this.f6570c = list;
        this.f6569b.fireTableDataChanged();
    }

    public List a() {
        return this.f6570c;
    }

    public Z.e a(int i2) {
        if (i2 < 0 || i2 >= this.f6570c.size()) {
            return null;
        }
        return (Z.e) this.f6570c.get(i2);
    }

    public boolean a(Z.e eVar) {
        for (int i2 = 0; i2 < this.f6570c.size(); i2++) {
            if (eVar.a().equals(((Z.e) this.f6570c.get(i2)).a())) {
                this.f6570c.set(i2, eVar);
                this.f6569b.fireTableDataChanged();
                return true;
            }
        }
        this.f6570c.add(eVar);
        Collections.sort(this.f6570c, new c(this));
        this.f6569b.fireTableDataChanged();
        return false;
    }
}
