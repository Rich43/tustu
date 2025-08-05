package D;

import W.ak;
import bI.i;
import f.h;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Properties;
import net.lingala.zip4j.util.InternalZipConstants;
import org.icepdf.core.util.PdfOps;

/* loaded from: TunerStudioMS.jar:D/a.class */
public class a {

    /* renamed from: a, reason: collision with root package name */
    private String f232a;

    /* renamed from: b, reason: collision with root package name */
    private String f233b;

    /* renamed from: c, reason: collision with root package name */
    private String f234c = "U";

    /* renamed from: d, reason: collision with root package name */
    private String f235d = "";

    /* renamed from: e, reason: collision with root package name */
    private String f236e = "";

    /* renamed from: f, reason: collision with root package name */
    private String f237f = "";

    /* renamed from: g, reason: collision with root package name */
    private String f238g = "";

    /* renamed from: h, reason: collision with root package name */
    private int f239h = 0;

    /* renamed from: i, reason: collision with root package name */
    private long f240i = System.currentTimeMillis();

    /* renamed from: j, reason: collision with root package name */
    private String f241j = null;

    public String a() {
        return this.f232a;
    }

    public void a(String str) {
        this.f232a = str;
    }

    public String b() {
        return this.f233b;
    }

    public void b(String str) {
        this.f233b = str;
    }

    public String c() {
        return this.f234c;
    }

    public void c(String str) {
        this.f234c = str;
    }

    public String d() {
        return this.f235d;
    }

    public void d(String str) {
        this.f235d = str;
    }

    public String e() {
        return this.f236e;
    }

    public void e(String str) {
        this.f236e = str;
    }

    public String f() {
        return this.f237f;
    }

    public void f(String str) {
        this.f237f = str;
    }

    public String g() {
        return this.f238g;
    }

    public void g(String str) {
        this.f238g = str;
    }

    public void h(String str) throws h {
        Properties properties = new Properties();
        this.f241j = str;
        try {
            properties.load(new ByteArrayInputStream(i.b(bI.a.a(str, 0))));
            c(properties.getProperty(PdfOps.s_TOKEN, ""));
            a(properties.getProperty("DT", ""));
            b(properties.getProperty("SN", ""));
            e(properties.getProperty(PdfOps.f_TOKEN, ""));
            f(properties.getProperty(PdfOps.l_TOKEN, ""));
            d(properties.getProperty("e", ""));
            g(properties.getProperty("p", ""));
            try {
                j(properties.getProperty(InternalZipConstants.READ_MODE, ""));
            } catch (f.i e2) {
                throw new h(e2.getMessage());
            }
        } catch (Exception e3) {
            throw new h("Invalid activation data.");
        }
    }

    public String i(String str) throws IOException {
        Properties properties = new Properties();
        properties.setProperty(PdfOps.s_TOKEN, c());
        properties.setProperty("DT", k(a()));
        properties.setProperty("SN", k(b()));
        properties.setProperty(PdfOps.f_TOKEN, k(e()));
        properties.setProperty(PdfOps.l_TOKEN, k(f()));
        properties.setProperty("e", k(d()));
        properties.setProperty("p", k(g()));
        properties.setProperty("a", str);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        properties.store(byteArrayOutputStream, "");
        return bI.a.a(i.a(byteArrayOutputStream.toByteArray()), 16);
    }

    public byte[] h() throws IOException {
        Properties properties = new Properties();
        properties.setProperty(PdfOps.s_TOKEN, c());
        properties.setProperty("DT", k(a()));
        properties.setProperty("SN", k(b()));
        properties.setProperty(PdfOps.f_TOKEN, k(e()));
        properties.setProperty(PdfOps.l_TOKEN, k(f()));
        properties.setProperty("e", k(d()));
        properties.setProperty("p", k(g()));
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        properties.store(byteArrayOutputStream, "");
        return new ak().a(byteArrayOutputStream.toByteArray(), "");
    }

    public void a(byte[] bArr) throws h {
        Properties properties = new Properties();
        try {
            properties.load(new ByteArrayInputStream(new ak().b(bArr, "")));
            c(properties.getProperty(PdfOps.s_TOKEN, ""));
            a(properties.getProperty("DT", ""));
            b(properties.getProperty("SN", ""));
            e(properties.getProperty(PdfOps.f_TOKEN, ""));
            f(properties.getProperty(PdfOps.l_TOKEN, ""));
            d(properties.getProperty("e", ""));
            g(properties.getProperty("p", ""));
            try {
                j(properties.getProperty(InternalZipConstants.READ_MODE, "32768"));
            } catch (f.i e2) {
                throw new h(e2.getMessage());
            }
        } catch (Exception e3) {
            throw new h("Invalid activation data.");
        }
    }

    private String k(String str) {
        return str == null ? "" : str;
    }

    public int i() {
        return this.f239h;
    }

    public void j(String str) throws f.i {
        try {
            a(Integer.parseInt(str));
        } catch (Exception e2) {
            throw new f.i("Invalid Error Code: " + str);
        }
    }

    public void a(int i2) {
        this.f239h = i2;
    }

    public long j() {
        return this.f240i;
    }

    public void a(long j2) {
        this.f240i = j2;
    }

    public String k() {
        return this.f241j;
    }
}
