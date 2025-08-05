package bc;

import ae.t;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import javax.swing.JPanel;

/* renamed from: bc.j, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:bc/j.class */
public class C1063j extends JPanel implements InterfaceC1066m {

    /* renamed from: a, reason: collision with root package name */
    HashMap f7875a = new HashMap();

    /* renamed from: b, reason: collision with root package name */
    List f7876b = new ArrayList();

    public C1063j() {
        setLayout(new GridLayout(0, 1));
    }

    public void a(List list) {
        a();
        for (int i2 = 0; i2 < list.size(); i2++) {
            C1054a c1054a = new C1054a((t) list.get(i2));
            c1054a.a((InterfaceC1066m) this);
            add(c1054a);
            this.f7875a.put(((t) list.get(i2)).a(), c1054a);
        }
    }

    public void a() {
        this.f7875a.clear();
        removeAll();
    }

    public void a(InterfaceC1066m interfaceC1066m) {
        this.f7876b.add(interfaceC1066m);
    }

    private void b(String str, Object obj) {
        Iterator it = this.f7876b.iterator();
        while (it.hasNext()) {
            ((InterfaceC1066m) it.next()).a(str, obj);
        }
    }

    public boolean b() {
        boolean z2 = true;
        for (C1054a c1054a : this.f7875a.values()) {
            if (!c1054a.d()) {
                z2 = false;
                c1054a.b();
            }
        }
        return z2;
    }

    public void c() {
        for (C1054a c1054a : this.f7875a.values()) {
            b(c1054a.getName(), c1054a.a());
        }
    }

    @Override // bc.InterfaceC1066m
    public void a(String str, Object obj) {
        b(str, obj);
    }
}
