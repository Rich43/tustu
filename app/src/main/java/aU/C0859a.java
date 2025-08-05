package au;

import com.efiAnalytics.ui.InterfaceC1602cm;
import com.efiAnalytics.ui.InterfaceC1662et;
import h.i;
import java.io.File;

/* renamed from: au.a, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:au/a.class */
public class C0859a implements InterfaceC1602cm {

    /* renamed from: a, reason: collision with root package name */
    InterfaceC1662et f6274a;

    /* renamed from: b, reason: collision with root package name */
    private static String f6275b = i.f12283D;

    /* renamed from: c, reason: collision with root package name */
    private static String f6276c = i.a(f6275b);

    public C0859a(InterfaceC1662et interfaceC1662et) {
        this.f6274a = null;
        this.f6274a = interfaceC1662et;
    }

    @Override // com.efiAnalytics.ui.InterfaceC1602cm
    public File a() {
        String strA = this.f6274a.a(f6275b);
        if (strA == null || strA.equals("")) {
            strA = f6276c;
        }
        return new File(strA);
    }

    @Override // com.efiAnalytics.ui.InterfaceC1602cm
    public void a(File file) {
        if (!file.isDirectory()) {
            file = file.getParentFile();
        }
        this.f6274a.a(f6275b, file.getAbsolutePath());
    }

    @Override // com.efiAnalytics.ui.InterfaceC1602cm
    public String[] b() {
        return new String[]{"trig", "csv"};
    }

    @Override // com.efiAnalytics.ui.InterfaceC1602cm
    public String c() {
        return "trig";
    }
}
