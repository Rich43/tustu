package G;

import java.util.ArrayList;
import java.util.Collections;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/* renamed from: G.cn, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:G/cn.class */
public class C0108cn {

    /* renamed from: a, reason: collision with root package name */
    int f1130a;

    /* renamed from: b, reason: collision with root package name */
    R f1131b;

    /* renamed from: c, reason: collision with root package name */
    final List f1132c = Collections.synchronizedList(new ArrayList());

    /* renamed from: d, reason: collision with root package name */
    final List f1133d = Collections.synchronizedList(new ArrayList());

    /* renamed from: e, reason: collision with root package name */
    ArrayList f1134e = new ArrayList();

    /* renamed from: f, reason: collision with root package name */
    ArrayList f1135f = new ArrayList();

    /* renamed from: g, reason: collision with root package name */
    boolean f1136g = true;

    public C0108cn(R r2) {
        this.f1130a = 50;
        this.f1131b = null;
        this.f1131b = r2;
        this.f1130a = r2.O().M();
    }

    public void a(InterfaceC0107cm interfaceC0107cm) {
        this.f1134e.add(interfaceC0107cm);
    }

    public void b(InterfaceC0107cm interfaceC0107cm) {
        this.f1135f.add(interfaceC0107cm);
    }

    private void c() {
        Iterator it = this.f1134e.iterator();
        while (it.hasNext()) {
            InterfaceC0107cm interfaceC0107cm = (InterfaceC0107cm) it.next();
            if (this.f1132c.isEmpty()) {
                interfaceC0107cm.a(null);
            } else {
                interfaceC0107cm.a(this.f1132c);
            }
        }
        Iterator it2 = this.f1135f.iterator();
        while (it2.hasNext()) {
            InterfaceC0107cm interfaceC0107cm2 = (InterfaceC0107cm) it2.next();
            if (!this.f1133d.isEmpty()) {
                interfaceC0107cm2.a(this.f1133d);
            } else if (interfaceC0107cm2 != null) {
                interfaceC0107cm2.a(null);
            }
        }
    }

    public List a(Set set) {
        if (this.f1136g || this.f1132c.isEmpty()) {
            synchronized (this.f1132c) {
                b(set);
            }
        }
        return this.f1132c;
    }

    public void a() {
        synchronized (this.f1132c) {
            this.f1132c.clear();
        }
        synchronized (this.f1133d) {
            this.f1133d.clear();
        }
        c();
    }

    private void b(Set set) {
        this.f1136g = false;
        bH.Z z2 = new bH.Z();
        z2.a();
        try {
            Iterator it = set.iterator();
            while (it.hasNext()) {
                a(this.f1131b.g((String) it.next()));
            }
        } catch (ConcurrentModificationException e2) {
            a();
        }
        z2.b();
        int iC = 0;
        for (C0140w c0140w : this.f1132c) {
            if (J.I()) {
                bH.C.d(c0140w.toString());
            }
            iC += c0140w.c();
        }
        a(this.f1133d);
        c();
    }

    private List a(List list) {
        System.currentTimeMillis();
        for (int i2 = 0; i2 < list.size(); i2++) {
            for (int i3 = i2 + 1; i3 < list.size(); i3++) {
                C0140w c0140w = (C0140w) list.get(i2);
                C0140w c0140w2 = (C0140w) list.get(i3);
                if (c0140w.a() > c0140w2.a()) {
                    list.set(i2, c0140w2);
                    list.set(i3, c0140w);
                }
            }
        }
        return list;
    }

    /* JADX WARN: Removed duplicated region for block: B:36:0x0104  */
    /* JADX WARN: Removed duplicated region for block: B:40:0x0139  */
    /* JADX WARN: Removed duplicated region for block: B:49:0x01a8  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private void a(G.aH r5) {
        /*
            Method dump skipped, instructions count: 555
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: G.C0108cn.a(G.aH):void");
    }

    public void b() {
        a();
    }
}
