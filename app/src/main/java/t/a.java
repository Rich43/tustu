package T;

import G.R;
import G.S;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/* loaded from: TunerStudioMS.jar:T/a.class */
public class a implements S {

    /* renamed from: d, reason: collision with root package name */
    private static a f1857d = null;

    /* renamed from: a, reason: collision with root package name */
    Map f1858a = new HashMap();

    /* renamed from: b, reason: collision with root package name */
    b f1859b = new b(this);

    /* renamed from: c, reason: collision with root package name */
    c f1860c = null;

    private a() {
    }

    public static a a() {
        if (f1857d == null) {
            f1857d = new a();
        }
        return f1857d;
    }

    public void a(String str, R r2, List list, String str2) {
        List listA = a(str);
        e eVar = new e(str, r2);
        eVar.a(str2);
        eVar.a(list);
        listA.add(eVar);
    }

    public void b() {
        this.f1858a.clear();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public c c() {
        if (this.f1860c == null || !this.f1860c.isAlive()) {
            this.f1860c = new c(this);
            this.f1860c.start();
        }
        return this.f1860c;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized List a(String str) {
        List arrayList = (List) this.f1858a.get(str);
        if (arrayList == null) {
            arrayList = new ArrayList();
            this.f1858a.put(str, arrayList);
        }
        return arrayList;
    }

    @Override // G.S
    public void a(R r2) {
    }

    @Override // G.S
    public void b(R r2) {
        r2.h().b(this.f1859b);
    }

    @Override // G.S
    public void c(R r2) {
        r2.h().a(this.f1859b);
    }
}
