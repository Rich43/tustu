package G;

import java.util.ArrayList;
import java.util.List;

/* renamed from: G.ag, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:G/ag.class */
public class C0047ag extends C0088bu {

    /* renamed from: a, reason: collision with root package name */
    private String f742a = null;

    /* renamed from: f, reason: collision with root package name */
    private String f743f = null;

    /* renamed from: g, reason: collision with root package name */
    private dh f744g = new B(55.0d);

    /* renamed from: h, reason: collision with root package name */
    private int f745h = 0;

    public String a() {
        return this.f742a;
    }

    public void a(String str) {
        this.f742a = str;
    }

    public String b() {
        return this.f743f;
    }

    public void b(String str) {
        this.f743f = str;
    }

    public dh c() {
        return this.f744g;
    }

    public void a(dh dhVar) {
        this.f744g = dhVar;
    }

    public void a(int i2) {
        this.f745h = i2;
    }

    public int d() {
        return this.f745h;
    }

    @Override // G.C0088bu
    public List e() {
        ArrayList arrayList = new ArrayList();
        if (this.f742a != null && !this.f742a.isEmpty()) {
            arrayList.add(this.f742a);
        }
        if (this.f743f != null && !this.f743f.isEmpty()) {
            arrayList.add(this.f743f);
        }
        return arrayList;
    }
}
