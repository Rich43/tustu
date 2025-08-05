package bl;

import G.C0079bl;
import com.efiAnalytics.plugin.ecu.UiCurve;
import java.util.ArrayList;
import java.util.List;

/* renamed from: bl.g, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:bl/g.class */
public class C1185g implements UiCurve {

    /* renamed from: a, reason: collision with root package name */
    private String f8246a = null;

    /* renamed from: b, reason: collision with root package name */
    private List f8247b = new ArrayList();

    /* renamed from: c, reason: collision with root package name */
    private String f8248c = null;

    /* renamed from: d, reason: collision with root package name */
    private List f8249d = new ArrayList();

    /* renamed from: e, reason: collision with root package name */
    private String f8250e = null;

    @Override // com.efiAnalytics.plugin.ecu.UiCurve
    public String getName() {
        return this.f8246a;
    }

    public void a(String str) {
        this.f8246a = str;
    }

    @Override // com.efiAnalytics.plugin.ecu.UiCurve
    public String getYAxisOutputChannelName() {
        return this.f8248c;
    }

    public void b(String str) {
        this.f8248c = str;
    }

    @Override // com.efiAnalytics.plugin.ecu.UiCurve
    public String getXAxisOutputChannelName() {
        return this.f8250e;
    }

    public void c(String str) {
        this.f8250e = str;
    }

    @Override // com.efiAnalytics.plugin.ecu.UiCurve
    public int getYAxisParameterCount() {
        if (this.f8247b == null) {
            return 0;
        }
        return this.f8247b.size();
    }

    @Override // com.efiAnalytics.plugin.ecu.UiCurve
    public String getYAxisParameterName(int i2) {
        return (String) this.f8247b.get(i2);
    }

    @Override // com.efiAnalytics.plugin.ecu.UiCurve
    public int getXAxisParameterCount() {
        if (this.f8249d == null) {
            return 0;
        }
        return this.f8249d.size();
    }

    @Override // com.efiAnalytics.plugin.ecu.UiCurve
    public String getXAxisParameterName(int i2) {
        return (String) this.f8249d.get(i2);
    }

    public static C1185g a(C0079bl c0079bl) {
        C1185g c1185g = new C1185g();
        c1185g.a(c0079bl.aJ());
        c1185g.c(c0079bl.l());
        for (int i2 = 0; i2 < c0079bl.j(); i2++) {
            c1185g.f8249d.add(c0079bl.d(i2));
        }
        c1185g.b(c0079bl.f());
        for (int i3 = 0; i3 < c0079bl.d(); i3++) {
            c1185g.f8247b.add(c0079bl.b(i3));
        }
        return c1185g;
    }
}
