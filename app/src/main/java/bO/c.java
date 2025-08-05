package bO;

import G.InterfaceC0109co;
import java.util.ArrayList;
import java.util.List;

/* loaded from: TunerStudioMS.jar:bO/c.class */
public class c {

    /* renamed from: a, reason: collision with root package name */
    public static byte f7343a = 32;

    /* renamed from: b, reason: collision with root package name */
    public static byte f7344b = 16;

    /* renamed from: c, reason: collision with root package name */
    public static byte f7345c = 2;

    /* renamed from: e, reason: collision with root package name */
    private final e f7342e = new e();

    /* renamed from: d, reason: collision with root package name */
    List f7346d = new ArrayList();

    /* renamed from: f, reason: collision with root package name */
    private List f7347f = new ArrayList();

    /* renamed from: g, reason: collision with root package name */
    private int f7348g = 251;

    /* renamed from: h, reason: collision with root package name */
    private int f7349h = 251;

    /* renamed from: i, reason: collision with root package name */
    private int f7350i = 0;

    /* renamed from: j, reason: collision with root package name */
    private byte f7351j = f7344b;

    /* renamed from: k, reason: collision with root package name */
    private int f7352k = -1;

    /* renamed from: l, reason: collision with root package name */
    private int f7353l = 1;

    /* renamed from: m, reason: collision with root package name */
    private int f7354m = 0;

    /* renamed from: n, reason: collision with root package name */
    private int f7355n = -1;

    /* renamed from: o, reason: collision with root package name */
    private int f7356o = -1;

    /* renamed from: p, reason: collision with root package name */
    private long f7357p = 0;

    /* renamed from: q, reason: collision with root package name */
    private final d f7358q = new d(this);

    public int a() {
        return this.f7348g;
    }

    public void a(int i2) {
        this.f7348g = i2;
    }

    public e b() {
        return this.f7342e;
    }

    public int c() {
        return this.f7349h;
    }

    public void b(int i2) {
        this.f7349h = i2;
    }

    public int d() {
        return this.f7350i;
    }

    public k c(int i2) throws j {
        if (i2 < 0) {
            throw new j("Invalid odt number:" + i2);
        }
        if (i2 > a()) {
            throw new j("ODT number (" + i2 + ") exceeds MAX_ODT " + a() + ", , Reduce the number of channels!");
        }
        if (i2 >= this.f7346d.size()) {
            for (int i3 = 0; i3 <= i2; i3++) {
                if (i3 >= this.f7346d.size()) {
                    this.f7346d.add(new k());
                }
            }
        }
        return (k) this.f7346d.get(i2);
    }

    public List e() {
        return this.f7346d;
    }

    public int f() {
        return this.f7346d.size();
    }

    public void d(int i2) {
        this.f7350i = i2;
    }

    public int g() {
        return this.f7352k;
    }

    public void e(int i2) {
        this.f7352k = i2;
    }

    public byte h() {
        return this.f7351j;
    }

    public void a(byte b2) {
        this.f7351j = b2;
    }

    public int i() {
        return this.f7353l;
    }

    public void f(int i2) {
        this.f7353l = i2;
    }

    public int j() {
        return this.f7354m;
    }

    public void g(int i2) {
        this.f7354m = i2;
    }

    public String toString() {
        return "DAQ List: maxOdts = " + this.f7348g + ", maxOdtEntries=" + this.f7349h + ", eventChannelNumber=" + this.f7352k + ", DAQ_LIST_PROPERTIES=" + Integer.toBinaryString(this.f7342e.a()) + ", Current ODT Entries: " + this.f7346d.size();
    }

    public int k() {
        return this.f7355n;
    }

    public void h(int i2) {
        this.f7355n = i2;
    }

    public void a(List list) {
        this.f7347f = this.f7346d;
        this.f7346d = list;
    }

    public void l() {
        List list = this.f7347f;
        this.f7347f = this.f7346d;
        this.f7346d = list;
        this.f7346d.clear();
    }

    public void i(int i2) {
        this.f7356o = i2;
    }

    public boolean m() {
        try {
            if (f() > 0 && c(0).size() > 0) {
                if (((l) c(0).get(0)).b() > 0) {
                    return true;
                }
            }
            return false;
        } catch (j e2) {
            return false;
        }
    }

    public InterfaceC0109co n() {
        return this.f7358q;
    }

    public void a(long j2) {
        this.f7357p = j2;
    }
}
