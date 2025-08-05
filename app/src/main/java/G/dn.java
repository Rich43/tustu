package G;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/* loaded from: TunerStudioMS.jar:G/dn.class */
public class dn implements Serializable {

    /* renamed from: f, reason: collision with root package name */
    private String f1220f = null;

    /* renamed from: g, reason: collision with root package name */
    private String f1221g = null;

    /* renamed from: h, reason: collision with root package name */
    private String f1222h = null;

    /* renamed from: i, reason: collision with root package name */
    private double f1223i = 0.0d;

    /* renamed from: j, reason: collision with root package name */
    private String f1224j = null;

    /* renamed from: k, reason: collision with root package name */
    private String f1225k = null;

    /* renamed from: l, reason: collision with root package name */
    private String f1226l = null;

    /* renamed from: m, reason: collision with root package name */
    private String f1227m = null;

    /* renamed from: n, reason: collision with root package name */
    private String f1228n = null;

    /* renamed from: o, reason: collision with root package name */
    private String f1229o = null;

    /* renamed from: p, reason: collision with root package name */
    private String f1230p = null;

    /* renamed from: q, reason: collision with root package name */
    private String f1231q = null;

    /* renamed from: r, reason: collision with root package name */
    private String f1232r = null;

    /* renamed from: s, reason: collision with root package name */
    private String f1233s = null;

    /* renamed from: t, reason: collision with root package name */
    private String f1234t = null;

    /* renamed from: a, reason: collision with root package name */
    List f1235a = new ArrayList();

    /* renamed from: b, reason: collision with root package name */
    ArrayList f1236b = new ArrayList();

    /* renamed from: c, reason: collision with root package name */
    ArrayList f1237c = new ArrayList();

    /* renamed from: d, reason: collision with root package name */
    public static String f1238d = "disableLiveUpdates";

    /* renamed from: e, reason: collision with root package name */
    public static String f1239e = "burnOnSend";

    public String a() {
        return this.f1220f;
    }

    public void a(String str) {
        this.f1220f = str;
    }

    public String b() {
        return this.f1222h;
    }

    public void b(String str) {
        this.f1222h = str;
    }

    public String c() {
        return this.f1224j;
    }

    public void c(String str) {
        this.f1224j = str;
    }

    public String d() {
        return this.f1225k;
    }

    public void d(String str) {
        this.f1225k = str;
    }

    public String e() {
        return this.f1227m;
    }

    public void e(String str) {
        this.f1227m = str;
    }

    public String f() {
        return this.f1229o;
    }

    public void f(String str) {
        this.f1229o = str;
    }

    public String g() {
        return this.f1231q;
    }

    public void g(String str) {
        this.f1231q = str;
    }

    public String h() {
        return this.f1226l;
    }

    public void h(String str) {
        this.f1226l = str;
    }

    public void a(double d2) {
        this.f1223i = d2;
    }

    public void i(String str) {
        this.f1232r = str;
    }

    public Iterator i() {
        return this.f1236b.iterator();
    }

    public void a(aW aWVar) {
        this.f1236b.add(aWVar);
    }

    public void j(String str) {
        if (this.f1237c.contains(str)) {
            return;
        }
        this.f1237c.add(str);
    }

    public Iterator j() {
        return this.f1237c.iterator();
    }

    public void k(String str) throws V.g {
        if (!str.equals(f1238d) && !str.equals(f1239e)) {
            throw new V.g("Unknown Option: " + str);
        }
        this.f1235a.add(str);
    }

    public boolean l(String str) {
        return this.f1235a.contains(str);
    }

    public void m(String str) {
        this.f1228n = str;
    }

    public String k() {
        return this.f1228n;
    }

    public void n(String str) {
        this.f1221g = str;
    }

    public String l() {
        return this.f1221g;
    }

    public String m() {
        return this.f1230p;
    }

    public void o(String str) {
        this.f1230p = str;
    }

    public String n() {
        return this.f1233s;
    }

    public void p(String str) {
        this.f1233s = str;
    }

    public String o() {
        return this.f1234t;
    }

    public void q(String str) {
        this.f1234t = str;
    }
}
