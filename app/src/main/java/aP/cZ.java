package aP;

import br.C1233K;
import com.efiAnalytics.apps.ts.dashboard.C1425x;
import com.efiAnalytics.apps.ts.tuningViews.C1441n;
import java.awt.Window;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JPanel;

/* loaded from: TunerStudioMS.jar:aP/cZ.class */
public class cZ {

    /* renamed from: b, reason: collision with root package name */
    private static cZ f3135b = null;

    /* renamed from: c, reason: collision with root package name */
    private C1425x f3136c = null;

    /* renamed from: d, reason: collision with root package name */
    private JFrame f3137d = null;

    /* renamed from: e, reason: collision with root package name */
    private gW f3138e = null;

    /* renamed from: f, reason: collision with root package name */
    private com.efiAnalytics.ui.dM f3139f = null;

    /* renamed from: g, reason: collision with root package name */
    private iE f3140g = null;

    /* renamed from: h, reason: collision with root package name */
    private iR f3141h = null;

    /* renamed from: i, reason: collision with root package name */
    private JPanel f3142i = null;

    /* renamed from: j, reason: collision with root package name */
    private C0308dx f3143j = null;

    /* renamed from: k, reason: collision with root package name */
    private bT f3144k = null;

    /* renamed from: l, reason: collision with root package name */
    private C0239bh f3145l = null;

    /* renamed from: m, reason: collision with root package name */
    private com.efiAnalytics.tunerStudio.search.l f3146m = null;

    /* renamed from: n, reason: collision with root package name */
    private bZ f3147n = null;

    /* renamed from: o, reason: collision with root package name */
    private C1441n f3148o = null;

    /* renamed from: p, reason: collision with root package name */
    private n.n f3149p = null;

    /* renamed from: q, reason: collision with root package name */
    private C1233K f3150q = null;

    /* renamed from: a, reason: collision with root package name */
    List f3151a = new ArrayList();

    private cZ() {
    }

    public static cZ a() {
        if (f3135b == null) {
            f3135b = new cZ();
        }
        return f3135b;
    }

    public C1425x b() {
        return this.f3136c;
    }

    public void a(C1425x c1425x) {
        this.f3136c = c1425x;
    }

    public JFrame c() {
        return this.f3137d;
    }

    public void a(JFrame jFrame) {
        a((Window) jFrame);
        this.f3137d = jFrame;
    }

    public com.efiAnalytics.ui.dM d() {
        return this.f3139f;
    }

    public void a(com.efiAnalytics.ui.dM dMVar) {
        this.f3139f = dMVar;
    }

    public iE e() {
        return this.f3140g;
    }

    public void a(iE iEVar) {
        this.f3140g = iEVar;
    }

    public iR f() {
        return this.f3141h;
    }

    public void a(iR iRVar) {
        this.f3141h = iRVar;
    }

    public C0308dx g() {
        return this.f3143j;
    }

    public void a(C0308dx c0308dx) {
        this.f3143j = c0308dx;
    }

    public bT h() {
        return this.f3144k;
    }

    public void a(bT bTVar) {
        this.f3144k = bTVar;
    }

    public void a(com.efiAnalytics.tunerStudio.search.l lVar) {
        this.f3146m = lVar;
    }

    public gW i() {
        return this.f3138e;
    }

    public void a(gW gWVar) {
        this.f3138e = gWVar;
    }

    public bZ j() {
        return this.f3147n;
    }

    public void a(bZ bZVar) {
        this.f3147n = bZVar;
    }

    public C1441n k() {
        return this.f3148o;
    }

    public void a(C1441n c1441n) {
        this.f3148o = c1441n;
    }

    public com.efiAnalytics.apps.ts.tuningViews.J l() {
        return k().c_();
    }

    public void a(n.n nVar) {
        this.f3149p = nVar;
    }

    public n.n m() {
        return this.f3149p;
    }

    public void a(Window window) {
        this.f3151a.add(window);
    }

    public void b(Window window) {
        this.f3151a.remove(window);
    }

    public boolean c(Window window) {
        return !this.f3151a.contains(window);
    }

    public JPanel n() {
        return this.f3142i;
    }

    public void a(JPanel jPanel) {
        this.f3142i = jPanel;
    }

    public C1233K o() {
        return this.f3150q;
    }

    public void a(C1233K c1233k) {
        this.f3150q = c1233k;
    }
}
