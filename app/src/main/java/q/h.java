package q;

import java.awt.Component;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/* loaded from: TunerStudioMS.jar:q/h.class */
public class h {

    /* renamed from: a, reason: collision with root package name */
    private static h f13263a = null;

    /* renamed from: b, reason: collision with root package name */
    private Map f13264b = Collections.synchronizedMap(new HashMap());

    /* renamed from: c, reason: collision with root package name */
    private Map f13265c = Collections.synchronizedMap(new HashMap());

    /* renamed from: d, reason: collision with root package name */
    private Map f13266d = new HashMap();

    private h() {
    }

    public static h a() {
        if (f13263a == null) {
            f13263a = new h();
        }
        return f13263a;
    }

    public synchronized Component a(Class cls) {
        List listB = b(cls);
        List listD = d(cls);
        if (listD.isEmpty()) {
            Component component = (Component) cls.newInstance();
            listB.add(component);
            b(component);
            return component;
        }
        Component component2 = (Component) listD.remove(0);
        listB.add(component2);
        b(component2);
        return component2;
    }

    public synchronized void a(Component component) {
        List listB = b(component.getClass());
        List listD = d(component.getClass());
        c(component);
        listB.remove(component);
        if (listD.contains(component)) {
            return;
        }
        listD.add(component);
    }

    private List b(Class cls) {
        List arrayList = (List) this.f13265c.get(cls);
        if (arrayList == null) {
            arrayList = new ArrayList();
            this.f13265c.put(cls, arrayList);
        }
        return arrayList;
    }

    private void b(Component component) {
        Iterator it = c(component.getClass()).iterator();
        while (it.hasNext()) {
            ((i) it.next()).b(component);
        }
    }

    private void c(Component component) {
        Iterator it = c(component.getClass()).iterator();
        while (it.hasNext()) {
            ((i) it.next()).a(component);
        }
    }

    public void a(Class cls, i iVar) {
        c(cls).add(iVar);
    }

    private List c(Class cls) {
        List arrayList = (List) this.f13266d.get(cls);
        if (arrayList == null) {
            arrayList = new ArrayList();
            this.f13266d.put(cls, arrayList);
        }
        return arrayList;
    }

    private List d(Class cls) {
        List listSynchronizedList = (List) this.f13264b.get(cls);
        if (listSynchronizedList == null) {
            listSynchronizedList = Collections.synchronizedList(new ArrayList());
            this.f13264b.put(cls, listSynchronizedList);
        }
        return listSynchronizedList;
    }

    public void a(Class cls, int i2) {
        List listD = d(cls);
        while (listD.size() < i2) {
            listD.add((Component) cls.newInstance());
        }
    }
}
