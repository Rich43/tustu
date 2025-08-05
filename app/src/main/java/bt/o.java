package bT;

import G.C0113cs;
import G.R;
import G.aR;
import bN.t;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/* loaded from: TunerStudioMS.jar:bT/o.class */
public class o {

    /* renamed from: h, reason: collision with root package name */
    private R f7607h;

    /* renamed from: d, reason: collision with root package name */
    r f7608d;

    /* renamed from: i, reason: collision with root package name */
    private bN.k f7609i;

    /* renamed from: e, reason: collision with root package name */
    j f7610e;

    /* renamed from: f, reason: collision with root package name */
    bN.p f7611f;

    /* renamed from: j, reason: collision with root package name */
    private bN.r f7612j;

    /* renamed from: t, reason: collision with root package name */
    private d f7623t;

    /* renamed from: a, reason: collision with root package name */
    public static String f7604a = "Dash Echo Server";

    /* renamed from: b, reason: collision with root package name */
    public static int f7605b = -1;

    /* renamed from: c, reason: collision with root package name */
    public static int f7606c = -2;

    /* renamed from: r, reason: collision with root package name */
    private static final k f7621r = new k();

    /* renamed from: g, reason: collision with root package name */
    p f7613g = new p(this);

    /* renamed from: k, reason: collision with root package name */
    private int f7614k = -1;

    /* renamed from: l, reason: collision with root package name */
    private int f7615l = -1;

    /* renamed from: m, reason: collision with root package name */
    private bN.i f7616m = new bN.i(-128);

    /* renamed from: n, reason: collision with root package name */
    private int f7617n = f7605b;

    /* renamed from: o, reason: collision with root package name */
    private byte[] f7618o = null;

    /* renamed from: p, reason: collision with root package name */
    private int f7619p = -1;

    /* renamed from: q, reason: collision with root package name */
    private int f7620q = 255;

    /* renamed from: s, reason: collision with root package name */
    private final List f7622s = new ArrayList();

    /* renamed from: u, reason: collision with root package name */
    private bP.b f7624u = new bP.b(this);

    public o(R r2, r rVar) {
        this.f7607h = r2;
        this.f7608d = rVar;
        o();
        this.f7611f = new bN.p(rVar.i(), this.f7609i);
        this.f7612j = new bN.r(rVar.j(), this.f7609i);
        this.f7610e = new j(this);
        n();
        this.f7611f.a(bN.p.f7294h);
        a(r2);
    }

    private void a(R r2) {
        this.f7623t = new d(r2, this.f7612j);
        bO.f fVarB = this.f7623t.a().b();
        fVarB.b(r2.r());
        fVarB.a(1);
        fVarB.c(0);
        this.f7623t.a().c().a(32);
        this.f7623t.a().c().e(1);
        bO.c cVar = new bO.c();
        cVar.b().a((byte) 4);
        this.f7623t.a(cVar, 0);
        bO.c cVar2 = new bO.c();
        cVar2.b().a((byte) 4);
        this.f7623t.a(cVar2, 1);
    }

    public void a() {
        Iterator it = this.f7623t.f7569a.iterator();
        while (it.hasNext()) {
            C0113cs.a().a(((b) it.next()).a().n());
        }
        this.f7611f.c();
        this.f7612j.c();
        this.f7611f.b(this.f7613g);
        b((bN.f) this.f7610e);
        b((bN.g) this.f7610e);
        for (e eVar : this.f7622s) {
            eVar.f7574a.h().b(eVar);
            eVar.a();
        }
        this.f7622s.clear();
        aR.a().b(this.f7624u);
    }

    private void n() {
        this.f7611f.a(this.f7613g);
        a((bN.f) this.f7610e);
        a((bN.g) this.f7610e);
    }

    public synchronized int a(List list) {
        return this.f7612j.a(list);
    }

    public synchronized void a(t tVar) {
        this.f7612j.a(tVar);
    }

    private void o() {
        this.f7609i = new bN.k();
        this.f7609i.b(this.f7608d.s());
        this.f7609i.f(255);
        e eVar = new e(this, this.f7607h);
        this.f7622s.add(eVar);
        this.f7607h.h().a(eVar);
        aR.a().a(this.f7624u);
    }

    public void b() {
        a(-1);
        this.f7608d.g();
    }

    public int c() {
        return this.f7614k;
    }

    public void a(int i2) {
        this.f7614k = i2;
    }

    public bN.k d() {
        return this.f7609i;
    }

    public void a(bN.f fVar) {
        this.f7611f.a(fVar);
    }

    public void b(bN.f fVar) {
        this.f7611f.b(fVar);
    }

    public void a(bN.g gVar) {
        this.f7612j.a(gVar);
    }

    public void b(bN.g gVar) {
        this.f7612j.b(gVar);
    }

    protected bN.r e() {
        return this.f7612j;
    }

    public R f() {
        return this.f7607h;
    }

    public int g() {
        return this.f7615l;
    }

    public void b(int i2) {
        this.f7615l = i2;
    }

    public void c(int i2) {
        this.f7620q = i2;
    }

    public void d(int i2) {
        this.f7617n = i2;
    }

    public int h() {
        return this.f7617n;
    }

    public k i() {
        return f7621r;
    }

    public bN.i j() {
        return this.f7616m;
    }

    public d k() {
        return this.f7623t;
    }

    public byte[] l() {
        return this.f7618o;
    }

    public void a(byte[] bArr) {
        this.f7618o = bArr;
    }

    public String m() {
        return this.f7608d.n();
    }
}
