package bl;

import G.C0072be;
import com.efiAnalytics.plugin.ecu.UiTable;

/* renamed from: bl.p, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:bl/p.class */
public class C1194p implements UiTable {

    /* renamed from: a, reason: collision with root package name */
    private String f8261a = null;

    /* renamed from: b, reason: collision with root package name */
    private String f8262b = null;

    /* renamed from: c, reason: collision with root package name */
    private String f8263c = null;

    /* renamed from: d, reason: collision with root package name */
    private String f8264d = null;

    /* renamed from: e, reason: collision with root package name */
    private String f8265e = null;

    /* renamed from: f, reason: collision with root package name */
    private String f8266f = null;

    @Override // com.efiAnalytics.plugin.ecu.UiTable
    public String getName() {
        return this.f8261a;
    }

    public void a(String str) {
        this.f8261a = str;
    }

    @Override // com.efiAnalytics.plugin.ecu.UiTable
    public String getXParameterName() {
        return this.f8262b;
    }

    public void b(String str) {
        this.f8262b = str;
    }

    @Override // com.efiAnalytics.plugin.ecu.UiTable
    public String getYParameterName() {
        return this.f8263c;
    }

    public void c(String str) {
        this.f8263c = str;
    }

    @Override // com.efiAnalytics.plugin.ecu.UiTable
    public String getZParameterName() {
        return this.f8264d;
    }

    public void d(String str) {
        this.f8264d = str;
    }

    @Override // com.efiAnalytics.plugin.ecu.UiTable
    public String getXOutputChannel() {
        return this.f8265e;
    }

    public void e(String str) {
        this.f8265e = str;
    }

    @Override // com.efiAnalytics.plugin.ecu.UiTable
    public String getYOutputChannel() {
        return this.f8266f;
    }

    public void f(String str) {
        this.f8266f = str;
    }

    public static C1194p a(C0072be c0072be) {
        C1194p c1194p = new C1194p();
        c1194p.a(c0072be.aJ());
        c1194p.e(c0072be.d());
        c1194p.f(c0072be.f());
        c1194p.b(c0072be.a());
        c1194p.c(c0072be.b());
        c1194p.d(c0072be.c());
        return c1194p;
    }
}
