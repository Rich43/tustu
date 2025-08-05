package d;

import G.R;
import G.S;
import G.T;
import bH.W;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/* loaded from: TunerStudioMS.jar:d/m.class */
public class m implements S, InterfaceC1711c {

    /* renamed from: a, reason: collision with root package name */
    public static String f12112a = "actionName";

    /* renamed from: b, reason: collision with root package name */
    public static String f12113b = "displayName";

    /* renamed from: c, reason: collision with root package name */
    public static String f12114c = "actionDescription";

    /* renamed from: d, reason: collision with root package name */
    public static String f12115d = "targetActionName";

    /* renamed from: e, reason: collision with root package name */
    public static String f12116e = "parameterString";

    /* renamed from: g, reason: collision with root package name */
    private String f12117g = "UserAction";

    /* renamed from: h, reason: collision with root package name */
    private String f12118h = "Display Name";

    /* renamed from: i, reason: collision with root package name */
    private String f12119i = "";

    /* renamed from: j, reason: collision with root package name */
    private String f12120j = null;

    /* renamed from: k, reason: collision with root package name */
    private C1710b f12121k = null;

    /* renamed from: f, reason: collision with root package name */
    k f12122f = new k();

    public m() {
        T.a().a(this);
    }

    public String h() {
        return this.f12120j;
    }

    public void a(String str) {
        this.f12120j = str;
    }

    public C1710b i() {
        return this.f12121k;
    }

    public void a(C1710b c1710b) {
        this.f12121k = c1710b;
    }

    public String j() {
        return this.f12119i;
    }

    public void b(String str) {
        this.f12119i = str;
    }

    @Override // d.InterfaceC1711c
    public String a() {
        return this.f12117g;
    }

    @Override // d.InterfaceC1711c
    public String b() {
        return this.f12118h;
    }

    @Override // d.InterfaceC1711c
    public String c() {
        return "User Action";
    }

    @Override // d.InterfaceC1711c
    public boolean d() {
        return true;
    }

    @Override // d.InterfaceC1711c
    public void a(Properties properties) throws e {
        g.a().a(this.f12120j, this.f12121k);
    }

    @Override // d.InterfaceC1711c
    public void b(Properties properties) {
        InterfaceC1711c interfaceC1711cB;
        if (this.f12120j == null || this.f12120j.isEmpty() || (interfaceC1711cB = g.a().b(this.f12120j)) == null) {
            return;
        }
        interfaceC1711cB.b(properties);
    }

    public void k() throws e {
        InterfaceC1711c interfaceC1711cB;
        String str = "";
        if (this.f12117g == null || this.f12117g.isEmpty()) {
            str = str + e("A Unique Action Name is required.") + "\n";
        } else if (W.f(this.f12117g) || this.f12117g.contains(" ")) {
            str = str + e("Action Name cannot contain special characters.") + "\n";
        }
        if (this.f12118h == null || this.f12118h.isEmpty()) {
            str = str + e("Please provide a Display Name.") + "\n";
        }
        if (this.f12121k == null) {
            str = str + e("Parameters have not been set.") + "\n";
        }
        if (this.f12120j == null || (interfaceC1711cB = g.a().b(this.f12120j)) == null) {
            str = str + e("Target Action has not been set.") + "\n";
        } else {
            try {
                interfaceC1711cB.b(this.f12121k);
            } catch (e e2) {
                str = str + e(e2.getLocalizedMessage()) + "\n";
            }
        }
        if (!str.isEmpty()) {
            throw new e(str);
        }
    }

    private String e(String str) {
        try {
            return C.a.a().a(str, str);
        } catch (C.b e2) {
            Logger.getLogger(m.class.getName()).log(Level.SEVERE, "Failed to get Translation", (Throwable) e2);
            return str;
        }
    }

    @Override // d.InterfaceC1711c
    public k e() {
        return this.f12122f;
    }

    @Override // d.InterfaceC1711c
    public boolean f() {
        return false;
    }

    public void c(String str) {
        this.f12118h = str;
    }

    public String l() {
        return this.f12117g;
    }

    public void d(String str) {
        this.f12117g = str;
    }

    public void c(Properties properties) {
        this.f12117g = properties.getProperty(f12112a);
        this.f12118h = properties.getProperty(f12113b);
        this.f12119i = properties.getProperty(f12114c);
        this.f12120j = properties.getProperty(f12115d);
        String property = properties.getProperty(f12116e);
        if (property != null) {
            try {
                this.f12121k = C1710b.a(property);
            } catch (e e2) {
                Logger.getLogger(m.class.getName()).log(Level.WARNING, "Failed to parse ActionParameters: " + property, (Throwable) e2);
            }
        }
    }

    public Properties m() {
        Properties properties = new Properties();
        properties.setProperty(f12112a, this.f12117g);
        properties.setProperty(f12113b, this.f12118h);
        properties.setProperty(f12114c, this.f12119i);
        if (this.f12120j != null) {
            properties.setProperty(f12115d, this.f12120j);
        }
        if (this.f12121k != null) {
            properties.setProperty(f12116e, this.f12121k.a());
        }
        return properties;
    }

    @Override // d.InterfaceC1711c
    public boolean g() {
        if (this.f12120j == null) {
            return false;
        }
        InterfaceC1711c interfaceC1711cB = g.a().b(this.f12120j);
        return interfaceC1711cB == null || interfaceC1711cB.g();
    }

    public void n() {
        if (this.f12120j == null || !this.f12120j.equals("loadCalFile")) {
            return;
        }
        InterfaceC1711c interfaceC1711cB = g.a().b(this.f12120j);
        if (interfaceC1711cB instanceof l) {
            ((l) interfaceC1711cB).c(this.f12121k);
        }
    }

    @Override // G.S
    public void a(R r2) {
    }

    @Override // G.S
    public void b(R r2) {
        T.a.a().b();
    }

    @Override // G.S
    public void c(R r2) {
        if (this.f12120j == null || !this.f12120j.equals("loadCalFile")) {
            return;
        }
        InterfaceC1711c interfaceC1711cB = g.a().b(this.f12120j);
        if (interfaceC1711cB instanceof l) {
            ((l) interfaceC1711cB).c(this.f12121k);
        }
    }
}
