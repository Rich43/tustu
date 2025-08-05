package G;

import com.efiAnalytics.plugin.ecu.ControllerParameter;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.icepdf.core.util.PdfOps;

/* renamed from: G.ac, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:G/ac.class */
public class C0043ac extends Q implements bH.Q, Serializable {

    /* renamed from: b, reason: collision with root package name */
    public static String f696b = "yesNo";

    /* renamed from: c, reason: collision with root package name */
    public static String f697c = "onOff";

    /* renamed from: d, reason: collision with root package name */
    public static String f698d = "highLow";

    /* renamed from: e, reason: collision with root package name */
    public static String f699e = "activeInactive";

    /* renamed from: f, reason: collision with root package name */
    public static String f700f = "hex";

    /* renamed from: g, reason: collision with root package name */
    public static String f701g = ControllerParameter.PARAM_CLASS_BITS;

    /* renamed from: h, reason: collision with root package name */
    public static String f702h = "Numeric";

    /* renamed from: i, reason: collision with root package name */
    private String f689i = null;

    /* renamed from: j, reason: collision with root package name */
    private String f690j = null;

    /* renamed from: k, reason: collision with root package name */
    private String f691k = null;

    /* renamed from: l, reason: collision with root package name */
    private cZ f692l = null;

    /* renamed from: m, reason: collision with root package name */
    private int f693m = 0;

    /* renamed from: n, reason: collision with root package name */
    private int f694n = 0;

    /* renamed from: a, reason: collision with root package name */
    dh f695a = null;

    /* renamed from: o, reason: collision with root package name */
    private String f703o = f702h;

    /* renamed from: p, reason: collision with root package name */
    private String f704p = "Off";

    /* renamed from: q, reason: collision with root package name */
    private String f705q = "On";

    /* renamed from: r, reason: collision with root package name */
    private String f706r = null;

    public void a(String str) {
        int iIndexOf = str.indexOf(46);
        if (iIndexOf != -1 || str.equals(f702h)) {
            try {
                a(Integer.parseInt(str.substring(iIndexOf + 1, iIndexOf + 2)));
                this.f703o = f702h;
                return;
            } catch (Exception e2) {
                a(0);
                bH.C.c("parseFormat error for DataLogField: " + b() + ", set as int");
                return;
            }
        }
        a(0);
        String strB = bH.W.b(str, PdfOps.DOUBLE_QUOTE__TOKEN, "");
        if (strB.equals(f696b)) {
            this.f694n = 5;
            this.f704p = "No";
            this.f705q = "Yes";
            d("Yes/No");
        } else if (strB.equals(f697c)) {
            this.f694n = 4;
            this.f704p = "Off";
            this.f705q = "On";
            d("On/Off");
        } else if (strB.equals(f698d)) {
            this.f694n = 6;
            this.f704p = "Low";
            this.f705q = "High";
            d("High/Low");
        } else if (strB.equals(f699e)) {
            this.f704p = "Inactive";
            this.f705q = "Active";
            this.f694n = 7;
            d("Act/Inact");
        } else if (strB.equals(f700f)) {
            this.f694n = 1;
            d("");
        }
        this.f703o = strB;
    }

    public String a() {
        return this.f689i;
    }

    public void b(String str) {
        this.f689i = str;
    }

    public String b() {
        try {
            return this.f692l != null ? this.f692l.a() : aJ();
        } catch (V.g e2) {
            bH.C.c("Failed to get DataLog Header name for entry of OutputChannel: " + this.f689i);
            return "Error";
        }
    }

    public void c(String str) {
        this.f692l = new C0094c(str);
    }

    public void a(cZ cZVar) {
        this.f692l = cZVar;
        try {
            v(cZVar.a());
        } catch (V.g e2) {
            Logger.getLogger(C0043ac.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
        }
    }

    @Override // G.Q
    public String aJ() {
        String strAJ = super.aJ();
        if (strAJ == null || strAJ.isEmpty()) {
            strAJ = b();
        }
        return strAJ;
    }

    public int d() {
        return this.f693m;
    }

    public void a(int i2) {
        this.f693m = i2;
    }

    public int e() {
        return this.f694n;
    }

    public void b(int i2) {
        this.f694n = i2;
        switch (i2) {
            case 1:
                this.f703o = f700f;
                break;
            case 2:
                this.f703o = f701g;
                break;
            case 3:
            default:
                this.f703o = f702h;
                break;
            case 4:
                this.f703o = f697c;
                break;
            case 5:
                this.f703o = f696b;
                break;
            case 6:
                this.f703o = f698d;
                break;
            case 7:
                this.f703o = f699e;
                break;
        }
    }

    public int f() {
        if (this.f695a == null) {
            return 0;
        }
        return (int) Math.round(this.f695a.a());
    }

    public dh g() {
        return this.f695a;
    }

    public void a(dh dhVar) {
        this.f695a = dhVar;
    }

    public String h() {
        return this.f704p;
    }

    public String i() {
        return this.f705q;
    }

    public String j() {
        return this.f706r;
    }

    public void d(String str) {
        this.f706r = str;
    }

    @Override // bH.Q
    public String c() {
        return b();
    }

    public String k() {
        return this.f703o;
    }

    public boolean l() {
        return (this.f694n == 0 || this.f694n == 1 || this.f694n == 2) ? false : true;
    }

    public String m() {
        return this.f690j;
    }

    public void e(String str) {
        this.f690j = str;
    }

    public String n() {
        return this.f691k;
    }
}
