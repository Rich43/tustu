package e;

import G.C0126i;
import G.T;
import L.ab;
import V.g;
import d.InterfaceC1711c;
import d.k;
import java.util.Properties;

/* renamed from: e.f, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:e/f.class */
public class C1718f implements InterfaceC1711c {

    /* renamed from: a, reason: collision with root package name */
    public static String f12142a = "ecuConfigName";

    /* renamed from: b, reason: collision with root package name */
    String f12143b = null;

    /* renamed from: c, reason: collision with root package name */
    String[] f12144c = {"zeroTo30MPH", "zeroTo60MPH", "zeroTo100MPH", "timeTo60Ft", "timeTo330Ft", "timeTo660Ft", "timeTo1320Ft", "timeTo2640Ft", "timeTo5280Ft", "speedAt60Ft", "speedAt330Ft", "speedAt660Ft", "speedAt1320Ft", "speedAt5280Ft", "etDistanceFt", "zeroTo50KPH", "zeroTo100KPH", "zeroTo150KPH", "zeroTo200KPH", "speedAt2640Ft"};

    @Override // d.InterfaceC1711c
    public String b() {
        return "Reset Speed & Distance Channels";
    }

    @Override // d.InterfaceC1711c
    public String c() {
        return "Runtime Values";
    }

    @Override // d.InterfaceC1711c
    public boolean d() {
        return true;
    }

    @Override // d.InterfaceC1711c
    public void a(Properties properties) {
        String property = properties.getProperty(f12142a);
        for (String str : this.f12144c) {
            try {
                C0126i.a(property, str);
            } catch (g e2) {
            }
        }
    }

    @Override // d.InterfaceC1711c
    public String a() {
        return "resetPerformanceChannels";
    }

    @Override // d.InterfaceC1711c
    public void b(Properties properties) throws d.e {
        String property = properties.getProperty(f12142a);
        if (((property == null || property.isEmpty()) ? T.a().c() : T.a().c(property)) == null) {
            if (property != null) {
                throw new d.e("No working configuration and no config name requested");
            }
            throw new d.e("Configuration Name not found: " + property);
        }
        if (!ab.a().b()) {
            throw new d.e("Performance functions are not enabled on this edition");
        }
    }

    @Override // d.InterfaceC1711c
    public k e() {
        return new k();
    }

    @Override // d.InterfaceC1711c
    public boolean f() {
        return false;
    }

    @Override // d.InterfaceC1711c
    public boolean g() {
        return true;
    }
}
