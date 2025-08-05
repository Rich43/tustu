package f;

import bH.C;
import com.intel.bluetooth.BlueCoveImpl;
import java.io.ByteArrayInputStream;
import java.util.Date;
import java.util.Properties;
import org.icepdf.core.util.PdfOps;

/* renamed from: f.a, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:f/a.class */
public final class C1719a {

    /* renamed from: a, reason: collision with root package name */
    private String f12145a = "";

    /* renamed from: b, reason: collision with root package name */
    private String f12146b = "";

    /* renamed from: c, reason: collision with root package name */
    private String f12147c = "";

    /* renamed from: d, reason: collision with root package name */
    private String f12148d = "";

    /* renamed from: e, reason: collision with root package name */
    private String f12149e = "";

    /* renamed from: f, reason: collision with root package name */
    private String f12150f = "";

    /* renamed from: g, reason: collision with root package name */
    private int f12151g = 2;

    /* renamed from: h, reason: collision with root package name */
    private String f12152h = "";

    /* renamed from: i, reason: collision with root package name */
    private int f12153i = 0;

    /* renamed from: j, reason: collision with root package name */
    private int f12154j = -1;

    /* renamed from: k, reason: collision with root package name */
    private Date f12155k = new Date(0);

    /* renamed from: l, reason: collision with root package name */
    private String f12156l = null;

    public C1719a() {
    }

    public C1719a(String str) throws h {
        a(str);
    }

    public void a(String str) throws h {
        this.f12156l = str;
        Properties properties = new Properties();
        try {
            properties.load(new ByteArrayInputStream(bI.i.b(bI.a.a(str, 0))));
            b(properties.getProperty(BlueCoveImpl.STACK_OSX, ""));
            c(properties.getProperty("hId", ""));
            d(properties.getProperty("mId", ""));
            j(properties.getProperty("dId", ""));
            e(properties.getProperty("rk", ""));
            f(properties.getProperty("em", ""));
            try {
                g(properties.getProperty("ec", ""));
                h(properties.getProperty("msg", ""));
                k(properties.getProperty(PdfOps.sc_TOKEN, "0"));
                a(Integer.parseInt(properties.getProperty("actCount", "1")));
                a(new Date(Long.parseLong(properties.getProperty("renewDate", "0"))));
            } catch (i e2) {
                throw new h(e2.getMessage());
            }
        } catch (Exception e3) {
            throw new h("Invalid activation data.");
        }
    }

    public String a() {
        return this.f12145a;
    }

    public void b(String str) {
        this.f12145a = str;
    }

    public String b() {
        return this.f12146b;
    }

    public void c(String str) {
        this.f12146b = str;
    }

    public String c() {
        return this.f12147c;
    }

    public void d(String str) {
        this.f12147c = str;
    }

    public String d() {
        return this.f12149e;
    }

    public void e(String str) {
        this.f12149e = str;
    }

    public void f(String str) {
        this.f12150f = str;
    }

    public int e() {
        return this.f12154j;
    }

    public void a(int i2) {
        this.f12154j = i2;
    }

    public int f() {
        return this.f12151g;
    }

    public void g(String str) throws i {
        try {
            b(Integer.parseInt(str));
        } catch (Exception e2) {
            throw new i("Invalid Error Code: " + str);
        }
    }

    public void b(int i2) throws i {
        if (i2 > 7) {
            throw new i("Invalid Error Code: " + i2);
        }
        this.f12151g = i2;
    }

    public String g() {
        return this.f12152h;
    }

    public void h(String str) {
        this.f12152h = str;
    }

    public Date h() {
        return this.f12155k;
    }

    public void a(Date date) {
        this.f12155k = date;
    }

    public String i() {
        return this.f12156l;
    }

    public void i(String str) {
        this.f12156l = str;
    }

    public String j() {
        return this.f12148d;
    }

    public void j(String str) {
        this.f12148d = str;
    }

    public void k(String str) {
        try {
            this.f12153i = Integer.parseInt(str);
        } catch (Exception e2) {
            this.f12153i = 0;
            C.a("Invalid Seat Count data: " + str);
        }
    }

    public String toString() {
        return "mac = " + this.f12145a + "\nhId = " + this.f12146b + "\nmId = " + this.f12147c + "\ndId = " + this.f12148d + "\nrk = " + this.f12149e + "\nem = " + this.f12150f + "\nec = " + this.f12151g + "\nmg = " + this.f12152h + "\nsc = " + this.f12153i + "\nac = " + this.f12154j + "\nrd = " + ((Object) this.f12155k) + "\n";
    }
}
