package bx;

import bH.C;
import bH.aa;
import com.efiAnalytics.ui.eJ;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JTable;

/* loaded from: TunerStudioMS.jar:bx/t.class */
public class t extends JTable {

    /* renamed from: a, reason: collision with root package name */
    aa f9221a;

    /* renamed from: g, reason: collision with root package name */
    private ArrayList f9222g = new ArrayList();

    /* renamed from: b, reason: collision with root package name */
    w f9223b = new w(this);

    /* renamed from: c, reason: collision with root package name */
    HashMap f9224c = new HashMap();

    /* renamed from: d, reason: collision with root package name */
    ArrayList f9225d = new ArrayList();

    /* renamed from: e, reason: collision with root package name */
    Icon f9226e = null;

    /* renamed from: f, reason: collision with root package name */
    Icon f9227f = null;

    public t(aa aaVar) {
        this.f9221a = null;
        this.f9221a = aaVar;
        setModel(this.f9223b);
        setSelectionMode(0);
        setRowSelectionAllowed(true);
        getColumnModel().getColumn(0).setMinWidth(eJ.a(20));
        getColumnModel().getColumn(0).setPreferredWidth(eJ.a(26));
        getColumnModel().getColumn(0).setMaxWidth(eJ.a(60));
        setRowHeight(getFont().getSize() + eJ.a(2));
        getSelectionModel().addListSelectionListener(new v(this));
    }

    public void a(j jVar) {
        this.f9222g.add(jVar);
        a();
    }

    public void a() {
        this.f9223b.fireTableDataChanged();
    }

    public void a(int i2) {
        if (i2 < 0 || i2 >= this.f9222g.size()) {
            return;
        }
        j jVar = (j) this.f9222g.get(getSelectedRow());
        if (c(jVar.a())) {
            b(jVar.a());
        } else {
            a(jVar.a());
        }
    }

    public void a(String str) {
        j jVarE = e(str);
        if (jVarE == null) {
            C.d("Can Not set Filter Active, it is not loaded.");
            return;
        }
        this.f9224c.put(str, jVarE);
        b(jVarE);
        a();
    }

    public void b(String str) {
        j jVarE = e(str);
        if (jVarE == null) {
            C.d("Can Not set Filter Active, it is not loaded.");
            return;
        }
        this.f9224c.remove(str);
        c(jVarE);
        a();
    }

    public void a(s sVar) {
        this.f9225d.add(sVar);
    }

    private void b(j jVar) {
        Iterator it = this.f9225d.iterator();
        while (it.hasNext()) {
            ((s) it.next()).a(jVar);
        }
    }

    private void c(j jVar) {
        Iterator it = this.f9225d.iterator();
        while (it.hasNext()) {
            ((s) it.next()).b(jVar);
        }
    }

    public Collection b() {
        return this.f9224c.values();
    }

    public boolean c(String str) {
        return this.f9224c.get(str) != null;
    }

    @Override // javax.swing.JTable
    public Class getColumnClass(int i2) {
        return i2 == 0 ? ImageIcon.class : Object.class;
    }

    Icon c() {
        if (this.f9226e == null) {
            this.f9226e = new ImageIcon(Toolkit.getDefaultToolkit().getImage(getClass().getResource("redBulb.png")));
        }
        return this.f9226e;
    }

    Icon d() {
        if (this.f9227f == null) {
            this.f9227f = new ImageIcon(Toolkit.getDefaultToolkit().getImage(getClass().getResource("greyBulb.png")));
        }
        return this.f9227f;
    }

    public boolean d(String str) {
        for (int i2 = 0; i2 < this.f9222g.size(); i2++) {
            if (((j) this.f9222g.get(i2)).a().equals(str)) {
                b(str);
                this.f9222g.remove(i2);
                this.f9223b.fireTableDataChanged();
                return true;
            }
        }
        return false;
    }

    public j e(String str) {
        for (int i2 = 0; i2 < this.f9222g.size(); i2++) {
            if (((j) this.f9222g.get(i2)).a().equals(str)) {
                return (j) this.f9222g.get(i2);
            }
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String f(String str) {
        return this.f9221a != null ? this.f9221a.a(str) : str;
    }

    public ArrayList e() {
        return this.f9222g;
    }
}
