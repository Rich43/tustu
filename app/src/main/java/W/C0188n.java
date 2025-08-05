package W;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

/* renamed from: W.n, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:W/n.class */
public class C0188n extends ArrayList {

    /* renamed from: c, reason: collision with root package name */
    private HashMap f2165c = new HashMap();

    /* renamed from: d, reason: collision with root package name */
    private HashMap f2166d = new HashMap();

    /* renamed from: e, reason: collision with root package name */
    private HashMap f2167e = new HashMap();

    /* renamed from: f, reason: collision with root package name */
    private HashMap f2168f = new HashMap();

    /* renamed from: g, reason: collision with root package name */
    private HashMap f2169g = new HashMap();

    /* renamed from: h, reason: collision with root package name */
    private String f2170h = "\t";

    /* renamed from: a, reason: collision with root package name */
    String f2171a = "";

    /* renamed from: b, reason: collision with root package name */
    String f2172b = "";

    /* renamed from: i, reason: collision with root package name */
    private boolean f2173i = false;

    /* renamed from: j, reason: collision with root package name */
    private File f2174j = null;

    public void a(C0184j c0184j) {
        if (c0184j.a().isEmpty()) {
            return;
        }
        add(c0184j);
        this.f2168f.put(c0184j.a(), c0184j);
    }

    public C0184j a(String str) {
        return (C0184j) this.f2168f.get(str);
    }

    @Override // java.util.ArrayList, java.util.AbstractList, java.util.List
    /* renamed from: a, reason: merged with bridge method [inline-methods] */
    public C0184j remove(int i2) {
        C0184j c0184j = (C0184j) super.remove(i2);
        this.f2168f.remove(c0184j.a());
        return c0184j;
    }

    public boolean b(C0184j c0184j) {
        this.f2168f.remove(c0184j.a());
        return super.remove(c0184j);
    }

    public C0184j b(String str) {
        C0184j c0184j = (C0184j) this.f2168f.get(str);
        if (c0184j == null) {
            for (String str2 : this.f2168f.keySet()) {
                if (str2.equalsIgnoreCase(str)) {
                    return (C0184j) this.f2168f.get(str2);
                }
            }
        }
        return c0184j;
    }

    public void a(int i2, String str) {
        a().put(Integer.valueOf(i2), str);
    }

    public String b(int i2) {
        return (String) a().get(Integer.valueOf(i2));
    }

    public HashMap a() {
        return this.f2165c;
    }

    public void a(HashMap map) {
        this.f2165c = map;
    }

    public void b(int i2, String str) {
        b().put(Integer.valueOf(i2), str);
    }

    public String c(int i2) {
        return (String) b().get(Integer.valueOf(i2));
    }

    public HashMap b() {
        return this.f2167e;
    }

    public void b(HashMap map) {
        this.f2167e = map;
    }

    public void c(int i2, String str) {
        this.f2166d.put(Integer.valueOf(i2), str);
    }

    public String d(int i2) {
        return (String) c().get(Integer.valueOf(i2));
    }

    public HashMap c() {
        return this.f2166d;
    }

    public int d() {
        if (size() == 0) {
            return 0;
        }
        return ((C0184j) get(0)).v();
    }

    public boolean e() {
        Iterator it = iterator();
        while (it.hasNext()) {
            C0184j c0184j = (C0184j) it.next();
            if (c0184j.n() != null && c0184j.n().length() > 0) {
                return true;
            }
        }
        return false;
    }

    public void c(String str) {
        this.f2171a = str;
    }

    public String f() {
        return this.f2171a;
    }

    public String g() {
        return (this.f2172b == null || this.f2172b.isEmpty()) ? f() : this.f2172b;
    }

    public void d(String str) {
        this.f2172b = str;
    }

    @Override // java.util.ArrayList, java.util.AbstractList, java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.List
    public Iterator iterator() {
        return super.listIterator();
    }

    public void a(int i2, int i3) {
        Iterator it = iterator();
        while (it.hasNext()) {
            ((C0184j) it.next()).e(i2, i3);
        }
        a(true);
    }

    public void a(boolean z2) {
        this.f2173i = z2;
    }

    public boolean e(String str) {
        Iterator it = iterator();
        while (it.hasNext()) {
            C0184j c0184j = (C0184j) it.next();
            if (c0184j.a().equals(str)) {
                return b(c0184j);
            }
        }
        return false;
    }

    public void a(String str, String str2) {
        this.f2169g.put(str, str2);
    }

    public String f(String str) {
        return (String) this.f2169g.get(str);
    }

    public boolean h() {
        return !this.f2169g.isEmpty();
    }

    public Collection i() {
        return this.f2169g.keySet();
    }

    public boolean j() {
        return (this.f2171a == null || this.f2171a.isEmpty()) ? false : true;
    }

    public File k() {
        return this.f2174j;
    }

    public void a(File file) {
        this.f2174j = file;
    }

    public void c(HashMap map) {
        this.f2169g = map;
    }

    @Override // java.util.ArrayList, java.util.AbstractList, java.util.AbstractCollection, java.util.Collection, java.util.List
    public void clear() {
        this.f2165c.clear();
        this.f2166d.clear();
        this.f2167e.clear();
        this.f2168f.clear();
        this.f2169g.clear();
        super.clear();
    }

    public String l() {
        return this.f2170h;
    }

    public void g(String str) {
        this.f2170h = str;
    }
}
