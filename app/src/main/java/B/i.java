package B;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/* loaded from: TunerStudioMS.jar:B/i.class */
public class i {

    /* renamed from: a, reason: collision with root package name */
    private String f155a = "";

    /* renamed from: b, reason: collision with root package name */
    private String f156b = "";

    /* renamed from: c, reason: collision with root package name */
    private String f157c = "";

    /* renamed from: d, reason: collision with root package name */
    private String f158d = "";

    /* renamed from: e, reason: collision with root package name */
    private String f159e = "";

    /* renamed from: f, reason: collision with root package name */
    private String f160f = "";

    /* renamed from: g, reason: collision with root package name */
    private String f161g = "";

    /* renamed from: h, reason: collision with root package name */
    private String f162h = "";

    /* renamed from: i, reason: collision with root package name */
    private String f163i = null;

    /* renamed from: j, reason: collision with root package name */
    private int f164j = -1;

    /* renamed from: k, reason: collision with root package name */
    private int f165k = -1;

    /* renamed from: l, reason: collision with root package name */
    private boolean f166l = false;

    /* renamed from: m, reason: collision with root package name */
    private final List f167m = new ArrayList();

    public Properties a() {
        Properties properties = new Properties();
        properties.setProperty("slaveType", this.f155a);
        properties.setProperty("id", this.f156b);
        properties.setProperty("host", this.f157c);
        properties.setProperty("protocol", this.f159e);
        properties.setProperty("serialNumber", this.f160f);
        properties.setProperty("info", this.f161g);
        properties.setProperty("projectName", this.f162h);
        properties.setProperty("canId", this.f165k + "");
        if (this.f163i != null) {
            properties.setProperty("projectUUID", this.f163i);
        }
        for (int i2 = 0; i2 < this.f167m.size(); i2++) {
            i iVar = (i) this.f167m.get(i2);
            String str = "CAN_DEVICE_" + i2 + "_";
            properties.setProperty(str + "slaveType", iVar.i());
            properties.setProperty(str + "id", iVar.b());
            properties.setProperty(str + "host", iVar.c());
            properties.setProperty(str + "protocol", iVar.d());
            properties.setProperty(str + "serialNumber", iVar.e());
            properties.setProperty(str + "info", iVar.f());
            properties.setProperty(str + "projectName", iVar.j());
            properties.setProperty(str + "canId", iVar.l() + "");
        }
        return properties;
    }

    public static i a(Properties properties) {
        i iVar = new i();
        iVar.f(properties.getProperty("slaveType", ""));
        iVar.a(properties.getProperty("id", ""));
        iVar.b(properties.getProperty("host", ""));
        iVar.c(properties.getProperty("protocol", ""));
        iVar.d(properties.getProperty("serialNumber", ""));
        iVar.e(properties.getProperty("info", ""));
        iVar.g(properties.getProperty("projectName", ""));
        String property = properties.getProperty("canId", "");
        if (!property.isEmpty()) {
            try {
                iVar.b(Integer.parseInt(property));
            } catch (Exception e2) {
            }
        }
        int i2 = 0;
        String str = "CAN_DEVICE_0_";
        while (true) {
            String str2 = str;
            if (properties.get(str2 + "projectName") == null) {
                return iVar;
            }
            i iVar2 = new i();
            iVar2.f(properties.getProperty(str2 + "slaveType"));
            iVar2.a(properties.getProperty(str2 + "id"));
            iVar2.b(properties.getProperty(str2 + "host"));
            iVar2.c(properties.getProperty(str2 + "protocol"));
            iVar2.d(properties.getProperty(str2 + "serialNumber"));
            iVar2.e(properties.getProperty(str2 + "info"));
            iVar2.g(properties.getProperty(str2 + "projectName"));
            properties.setProperty(str2 + "canId", iVar2.l() + "");
            try {
                iVar2.b(Integer.parseInt(properties.getProperty(str2 + "canId")));
            } catch (Exception e3) {
            }
            i2++;
            str = "CAN_DEVICE_" + i2 + "_";
        }
    }

    public String b() {
        return this.f156b;
    }

    public void a(String str) {
        this.f156b = str;
    }

    public String c() {
        return this.f157c;
    }

    public void b(String str) {
        this.f157c = str;
    }

    public String d() {
        return this.f159e;
    }

    public void c(String str) {
        this.f159e = str;
    }

    public String e() {
        return this.f160f;
    }

    public void d(String str) {
        this.f160f = str;
    }

    public String f() {
        return this.f161g;
    }

    public void e(String str) {
        this.f161g = str;
    }

    public int g() {
        return this.f164j;
    }

    public void a(int i2) {
        this.f164j = i2;
    }

    public boolean h() {
        return this.f166l;
    }

    public void a(boolean z2) {
        this.f166l = z2;
    }

    public String i() {
        return this.f155a;
    }

    public void f(String str) {
        this.f155a = str;
    }

    public String j() {
        return this.f162h;
    }

    public void g(String str) {
        this.f162h = str;
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof i)) {
            return super.equals(obj);
        }
        i iVar = (i) obj;
        return a(this.f155a, iVar.i()) && a(this.f156b, iVar.b()) && a(this.f157c, iVar.c()) && a(this.f159e, iVar.d()) && a(this.f160f, iVar.e()) && a(this.f161g, iVar.f()) && a(this.f162h, iVar.j()) && this.f164j == iVar.g() && this.f166l == iVar.h();
    }

    private boolean a(String str, String str2) {
        if ((str == null) ^ (str2 == null)) {
            return false;
        }
        if (str != null) {
            return str.equals(str2);
        }
        return true;
    }

    public String k() {
        return this.f158d;
    }

    public void h(String str) {
        this.f158d = str;
    }

    public int l() {
        return this.f165k;
    }

    public void b(int i2) {
        this.f165k = i2;
    }

    public List m() {
        return this.f167m;
    }

    public void a(i iVar) {
        this.f167m.add(iVar);
    }

    public String n() {
        return this.f163i;
    }

    public void i(String str) {
        this.f163i = str;
    }
}
