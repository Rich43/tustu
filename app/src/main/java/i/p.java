package I;

import G.R;
import W.C0172ab;
import bH.C;
import bH.H;
import bH.W;
import java.io.File;

/* loaded from: TunerStudioMS.jar:I/p.class */
public class p {

    /* renamed from: a, reason: collision with root package name */
    private static p f1399a = null;

    /* renamed from: b, reason: collision with root package name */
    private File f1400b = new File(".", "inc");

    /* renamed from: c, reason: collision with root package name */
    private final String f1401c = File.separator + "ms1ExtraSupplement.ecu";

    /* renamed from: d, reason: collision with root package name */
    private final String f1402d = File.separator + "speeduinoSupplement.ecu";

    /* renamed from: f, reason: collision with root package name */
    private final String f1404f = File.separator + "ms2Supplement.ecu";

    /* renamed from: g, reason: collision with root package name */
    private final String f1405g = File.separator + "ms3Supplement.ecu";

    /* renamed from: h, reason: collision with root package name */
    private final String f1406h = File.separator + "ms3Supplement_1.3.ecu";

    /* renamed from: i, reason: collision with root package name */
    private final String f1407i = File.separator + "bs3Supplement.ecu";

    /* renamed from: j, reason: collision with root package name */
    private final String f1408j = File.separator + "bsGen4Supplement.ecu";

    /* renamed from: k, reason: collision with root package name */
    private String f1409k = File.separator + "MCXpressSupplement.ecu";

    /* renamed from: l, reason: collision with root package name */
    private boolean f1410l = false;

    /* renamed from: e, reason: collision with root package name */
    private final String f1403e = File.separator + "inc" + File.separator + "ms1BaseSupplement.ecu";

    private p() {
    }

    public static p a() {
        if (f1399a == null) {
            f1399a = new p();
        }
        return f1399a;
    }

    public void a(R r2) {
        if (this.f1410l) {
            String strI = r2.i();
            if (strI == null) {
                C.b("Signature null, can not load supplemental channels!");
                return;
            }
            String str = null;
            if (strI.startsWith("MS/Extra") || strI.startsWith("MSnS-extra") || strI.startsWith("MS1/Extra")) {
                str = this.f1401c;
            } else if (!strI.startsWith("20")) {
                if (strI.startsWith("MS2Extra") || strI.startsWith("MSII Rev") || (strI.startsWith("Monsterfirmware") && !strI.contains("pw"))) {
                    str = this.f1404f;
                } else if (strI.startsWith("MS3")) {
                    str = a(strI) > 434 ? this.f1406h : this.f1405g;
                } else if (strI.startsWith("Ditron/MCXpress")) {
                    str = this.f1409k;
                } else if (strI.startsWith("speeduino") && b(strI) >= 201905) {
                    str = this.f1402d;
                } else if (strI.startsWith("BigStuff Gen4")) {
                    str = this.f1408j;
                } else if (r2.u().size() > 0 && (r2.i().equals("76") || r2.i().equals("77") || r2.i().equals("88") || r2.i().equals("97") || r2.i().equals("98"))) {
                    str = this.f1407i;
                }
            }
            if (str != null) {
                C0172ab c0172ab = new C0172ab();
                c0172ab.a(false);
                c0172ab.a(r2, this.f1400b.getAbsolutePath() + str, false, false);
            }
        }
    }

    private int a(String str) {
        try {
            String strTrim = W.b(str, "MS3 Format", "").trim();
            return Integer.parseInt(strTrim.substring(0, strTrim.indexOf(".")));
        } catch (Exception e2) {
            return 100;
        }
    }

    private int b(String str) {
        String strTrim = W.b(str, "speeduino", "").trim();
        if (strTrim.contains("-dev")) {
            strTrim = W.b(strTrim, "-dev", "");
        }
        if (strTrim.length() == 6 && H.a(strTrim)) {
            return Integer.parseInt(strTrim);
        }
        C.b("unexpected speeduino signature format! " + str);
        return 0;
    }

    public void a(boolean z2) {
        this.f1410l = z2;
    }
}
