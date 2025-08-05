package aW;

import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import javax.swing.JPanel;

/* loaded from: TunerStudioMS.jar:aW/o.class */
public class o extends JPanel implements p {

    /* renamed from: a, reason: collision with root package name */
    HashMap f3990a = new HashMap();

    /* renamed from: b, reason: collision with root package name */
    List f3991b = new ArrayList();

    /* renamed from: c, reason: collision with root package name */
    a f3992c;

    public o(a aVar) {
        setLayout(new GridLayout(0, 1));
        this.f3992c = aVar;
    }

    public void a(List list) {
        a();
        for (int i2 = 0; i2 < list.size(); i2++) {
            e eVar = new e(this.f3992c, (A.r) list.get(i2));
            eVar.a((p) this);
            add(eVar);
            this.f3990a.put(((A.r) list.get(i2)).c(), eVar);
        }
    }

    public void a() {
        this.f3990a.clear();
        removeAll();
    }

    public Object a(String str) {
        e eVar = (e) this.f3990a.get(str);
        if (eVar != null) {
            return eVar.a();
        }
        return null;
    }

    public boolean a(String str, Object obj) {
        e eVar = (e) this.f3990a.get(str);
        if (eVar == null) {
            return false;
        }
        eVar.a(obj);
        return true;
    }

    public void a(p pVar) {
        this.f3991b.add(pVar);
    }

    private void b(String str, String str2) {
        Iterator it = this.f3991b.iterator();
        while (it.hasNext()) {
            ((p) it.next()).a(str, str2);
        }
    }

    public boolean b() {
        boolean z2 = true;
        for (e eVar : this.f3990a.values()) {
            if (!eVar.d()) {
                z2 = false;
                eVar.b();
            }
        }
        return z2;
    }

    @Override // aW.p
    public void a(String str, String str2) {
        b(str, str2);
    }
}
