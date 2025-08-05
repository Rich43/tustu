package ao;

import W.C0184j;
import W.C0188n;
import com.efiAnalytics.ui.C1701s;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/* loaded from: TunerStudioMS.jar:ao/gG.class */
class gG extends C1701s {

    /* renamed from: t, reason: collision with root package name */
    private C0188n f5904t = null;

    /* renamed from: a, reason: collision with root package name */
    C0184j f5905a = null;

    /* renamed from: b, reason: collision with root package name */
    C0184j f5906b = null;

    /* renamed from: c, reason: collision with root package name */
    C0184j f5907c = null;

    /* renamed from: d, reason: collision with root package name */
    List f5908d = new ArrayList();

    /* renamed from: e, reason: collision with root package name */
    HashMap f5909e = new HashMap();

    /* renamed from: f, reason: collision with root package name */
    final /* synthetic */ fX f5910f;

    gG(fX fXVar) {
        this.f5910f = fXVar;
        super.b(false);
    }

    public void a(String str) {
        this.f5905a = c().a(str);
        f();
    }

    public void b(String str) {
        this.f5906b = c().a(str);
        f();
    }

    public void c(String str) {
        this.f5907c = c().a(str);
        f();
    }

    public C0188n c() {
        return this.f5904t;
    }

    public C0184j d() {
        return this.f5905a;
    }

    public C0184j e() {
        return this.f5906b;
    }

    public void a(C0188n c0188n) {
        this.f5904t = c0188n;
        this.f5909e.clear();
        f();
    }

    private void f() {
        Iterator it = this.f5908d.iterator();
        while (it.hasNext()) {
            ((bE.l) it.next()).a();
        }
    }
}
