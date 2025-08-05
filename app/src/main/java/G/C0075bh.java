package G;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/* renamed from: G.bh, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:G/bh.class */
public class C0075bh extends C0088bu implements Serializable, Cloneable {

    /* renamed from: a, reason: collision with root package name */
    private ArrayList f916a = new ArrayList();

    /* renamed from: f, reason: collision with root package name */
    private ArrayList f917f = new ArrayList();

    /* renamed from: g, reason: collision with root package name */
    private ArrayList f918g = new ArrayList();

    /* renamed from: h, reason: collision with root package name */
    private ArrayList f919h = new ArrayList();

    /* renamed from: i, reason: collision with root package name */
    private ArrayList f920i = new ArrayList();

    /* renamed from: j, reason: collision with root package name */
    private String f921j = null;

    /* renamed from: k, reason: collision with root package name */
    private ArrayList f922k = new ArrayList();

    /* renamed from: l, reason: collision with root package name */
    private boolean f923l = true;

    /* renamed from: m, reason: collision with root package name */
    private boolean f924m = false;

    public int a() {
        return this.f916a.size();
    }

    public String a(int i2) {
        return i2 >= this.f917f.size() ? "1" : (String) this.f917f.get(i2);
    }

    public String b(int i2) {
        return (String) this.f916a.get(i2);
    }

    public void a(String str, String str2) {
        this.f916a.add(str);
        this.f917f.add(str2);
    }

    public cZ c(int i2) {
        if (this.f919h.size() > i2) {
            return (cZ) this.f919h.get(i2);
        }
        return null;
    }

    public void a(cZ cZVar) {
        this.f919h.add(cZVar);
    }

    public int b() {
        return this.f920i.size();
    }

    public String d(int i2) {
        return (String) this.f920i.get(i2);
    }

    public void a(String str) {
        this.f920i.add(str);
    }

    public void b(cZ cZVar) {
        this.f918g.add(cZVar);
    }

    public cZ e(int i2) {
        return (cZ) this.f918g.get(i2);
    }

    public int c() {
        return this.f918g.size();
    }

    public String d() {
        return this.f921j;
    }

    public void b(String str) {
        this.f921j = str;
    }

    public int f() {
        return this.f922k.size();
    }

    public cZ f(int i2) {
        if (this.f922k.size() > i2) {
            return (cZ) this.f922k.get(i2);
        }
        return null;
    }

    public void c(cZ cZVar) {
        this.f922k.add(cZVar);
    }

    /* renamed from: g, reason: merged with bridge method [inline-methods] */
    public C0075bh clone() {
        C0075bh c0075bh = new C0075bh();
        c0075bh.f916a = this.f916a;
        c0075bh.f919h = this.f919h;
        c0075bh.f920i = this.f920i;
        c0075bh.f921j = this.f921j;
        c0075bh.f922k = this.f922k;
        c0075bh.f918g = this.f918g;
        c0075bh.v(aJ());
        return c0075bh;
    }

    @Override // G.C0088bu
    public List e() {
        ArrayList arrayList = new ArrayList();
        Iterator it = this.f916a.iterator();
        while (it.hasNext()) {
            arrayList.add(it.next());
        }
        Iterator it2 = this.f920i.iterator();
        while (it2.hasNext()) {
            arrayList.add(it2.next());
        }
        return arrayList;
    }

    @Override // G.C0088bu
    public String i(String str) {
        if (this.f916a.contains(str) || this.f920i.contains(str)) {
            return aH();
        }
        return null;
    }

    public void a(boolean z2) {
        this.f924m = z2;
    }

    public boolean h() {
        return this.f924m || this.f920i.isEmpty();
    }

    public boolean i() {
        return this.f923l;
    }

    public void b(boolean z2) {
        this.f923l = z2;
    }
}
