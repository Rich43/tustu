package h;

import W.C0171aa;
import W.V;
import W.X;
import W.ak;
import ak.C0544d;
import ak.C0546f;
import ak.aH;
import ak.an;
import bH.W;
import g.C1724b;
import g.InterfaceC1723a;
import j.C1750a;
import java.util.ArrayList;

/* renamed from: h.b, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:h/b.class */
public class C1737b {

    /* renamed from: a, reason: collision with root package name */
    ArrayList f12221a = new ArrayList();

    /* renamed from: p, reason: collision with root package name */
    private InterfaceC1723a f12233p = new C1740e(this);

    /* renamed from: o, reason: collision with root package name */
    private static C1737b f12220o = null;

    /* renamed from: b, reason: collision with root package name */
    public static String f12222b = " Lite!";

    /* renamed from: c, reason: collision with root package name */
    public static String f12223c = "MS";

    /* renamed from: d, reason: collision with root package name */
    public static String f12224d = "(Beta)";

    /* renamed from: e, reason: collision with root package name */
    public static String f12225e = "BigStuff3";

    /* renamed from: f, reason: collision with root package name */
    public static String f12226f = "EmbeddedBS3";

    /* renamed from: g, reason: collision with root package name */
    public static String f12227g = "EmbeddedMS";

    /* renamed from: h, reason: collision with root package name */
    public static String f12228h = "Acquisition";

    /* renamed from: i, reason: collision with root package name */
    public static String f12229i = "HD";

    /* renamed from: j, reason: collision with root package name */
    public static String f12230j = "MegaLogViewer";

    /* renamed from: k, reason: collision with root package name */
    public static String f12231k = f12230j;

    /* renamed from: l, reason: collision with root package name */
    public static String f12232l = "Dyno Spectrum Data";

    /* renamed from: m, reason: collision with root package name */
    public static String[] f12234m = {"MAP", "Load", "TPS", "TP", "TP ADC", "TPS 8Bit"};

    /* renamed from: n, reason: collision with root package name */
    public static Class f12235n = null;

    private C1737b() {
    }

    public static C1737b a() {
        if (f12220o == null) {
            f12220o = new C1737b();
        }
        return f12220o;
    }

    public void a(String str, String str2) {
        this.f12221a = new ArrayList();
        if (str2.contains(f12224d)) {
            str2 = W.b(str2, f12224d, "");
        }
        if (str.equals(f12230j)) {
            if (str2.equals(f12223c)) {
                this.f12221a.add("triggerLogViewer");
                this.f12221a.add("scatterPlots");
                this.f12221a.add("tuningPanelVisible");
                this.f12221a.add("advancedVeAnalyze");
                this.f12221a.add("veAnalyze");
                this.f12221a.add("optionalFields");
                this.f12221a.add("customFields");
                this.f12221a.add("compareMode");
                this.f12221a.add("upgradeOldRegistration");
                this.f12221a.add("searchLogFiles");
                this.f12221a.add("tablePremium");
                this.f12221a.add("fileEditing");
                this.f12221a.add("optionalQuickSelect");
                this.f12221a.add("selectGraphTraceCount");
                this.f12221a.add("selectableFields");
                this.f12221a.add("optionalTableLayouts");
                this.f12221a.add("advancedMathFunctions");
                this.f12221a.add("selectableLookAndFeel");
                this.f12221a.add("tabbedQuickViews");
                this.f12221a.add("spoji[asfi2309jdi234iofwae2344");
                this.f12221a.add("okdsas32lkg09832jnegm7");
                this.f12221a.add("timeslipData");
                this.f12233p = new f(this);
                W.W.a().a(X.f1971m, aH.class);
                W.W.a().a(X.f1993I, an.class);
                V.f1955d = false;
            } else if (str2.equals(f12226f)) {
                this.f12221a.add("tuningPanelVisible");
                this.f12221a.add("advancedVeAnalyze");
                this.f12221a.add("compareMode");
                this.f12221a.add("optionalQuickSelect");
                this.f12221a.add("searchLogFiles");
                this.f12221a.add("tablePremium");
                this.f12221a.add("fileEditing");
                this.f12221a.add("timeslipData");
                this.f12221a.add("selectableFields");
                this.f12221a.add("advancedMathFunctions");
                this.f12221a.add("fieldNameNormalization");
                this.f12221a.add("okdsas32lkg09832jnegm7");
                this.f12221a.add("fieldSmoothing");
                this.f12221a.add("tabbedQuickViews");
                this.f12221a.add("fileEditing");
                i.f12275v = ",";
                this.f12233p = new f(this);
                W.W.a().a(X.f1981w, C0544d.class);
                W.W.a().a(X.f1993I, an.class);
                V.f1955d = false;
                f12235n = C1750a.class;
            } else if (str2.equals(f12227g)) {
                this.f12221a.add("tuningPanelVisible");
                this.f12221a.add("advancedVeAnalyze");
                this.f12221a.add("compareMode");
                this.f12221a.add("searchLogFiles");
                this.f12221a.add("optionalQuickSelect");
                this.f12221a.add("timeslipData");
                this.f12221a.add("tablePremium");
                this.f12221a.add("fileEditing");
                this.f12221a.add("selectableFields");
                this.f12221a.add("advancedMathFunctions");
                this.f12221a.add("fieldNameNormalization");
                this.f12221a.add("tabbedQuickViews");
                this.f12221a.add("spoji[asfi2309jdi234iofwae2344");
                this.f12221a.add("okdsas32lkg09832jnegm7");
                this.f12221a.add("fileEditing");
                this.f12233p = new f(this);
                W.W.a().a(X.f1993I, an.class);
                V.f1955d = false;
            } else if (str2.equals(f12229i)) {
                d();
                this.f12233p = new f(this);
            } else if (str2.equals(f12229i + f12222b)) {
                e();
            } else if (str2.equals(f12225e)) {
                c();
                this.f12233p = new f(this);
                ak.a(new char[]{'E', 'F', 'I', 'A', 'K', 'e', 'y', '!', '0', '8', '8', '2', '8', '3', '5', '4'});
            } else if (str2.equals(f12225e + f12222b)) {
                b();
                ak.a(new char[]{'E', 'F', 'I', 'A', 'K', 'e', 'y', '!', '0', '8', '8', '2', '8', '3', '5', '4'});
            } else if (str2.equals(f12223c + f12222b)) {
                this.f12221a.add("tuningPanelVisible");
                this.f12221a.add("showRegisterTab");
                this.f12221a.add("upgradeOldRegistration");
                this.f12221a.add("limitFileLoadSize");
                this.f12221a.add("veAnalyze");
                W.W.a().a(X.f1993I, an.class);
                V.f1955d = false;
            } else {
                this.f12221a.add("tuningPanelVisible");
                this.f12221a.add("showRegisterTab");
                this.f12221a.add("upgradeOldRegistration");
                W.W.a().a(X.f1993I, an.class);
            }
        } else if (str.equals(f12231k)) {
            if (str2.equals(f12225e)) {
                c();
                this.f12233p = new f(this);
            } else {
                b();
            }
        } else if (str.equals(f12232l)) {
            if (str2.contains(f12222b.trim())) {
                e();
            } else {
                d();
                this.f12233p = new f(this);
            }
            this.f12221a.remove("triggerLogViewer");
            this.f12221a.add("tuningPanelVisible");
        }
        this.f12221a.add("upgradeOldRegistration");
        C0546f.f4834q = a().a("limitFileLoadSize");
    }

    private void b() {
        i.f12266m = "http://www.efianalytics.com/BigStuffLog/";
        i.f12267n = "https://www.efianalytics.com/register/viewProduct.jsp?productCode=BigStuffLog";
        this.f12221a.add("showRegisterTab");
        this.f12221a.add("upgradeOldRegistration");
        this.f12221a.add("optionalQuickSelect");
        this.f12221a.add("tuningPanelVisible");
        this.f12221a.add("fieldNameNormalization");
        C1724b.a(2500);
        C0171aa.f2044d = true;
        W.W.a().a(X.f1959a, C0544d.class);
        W.W.a().a(X.f1981w, C0544d.class);
        W.W.a().a(X.f1993I, an.class);
        V.f1955d = true;
        f12234m = new String[]{"MAP", "TPS_Pct", "Boost", "kPaFromPsi"};
        f12235n = C1750a.class;
        f12235n = C1750a.class;
    }

    private void c() {
        i.f12266m = "http://www.efianalytics.com/BigStuffLog/";
        i.f12267n = "https://www.efianalytics.com/register/viewProduct.jsp?productCode=BigStuffLog";
        this.f12221a.add("scatterPlots");
        this.f12221a.add("tableGenerator");
        this.f12221a.add("tuningPanelVisible");
        this.f12221a.add("hideLoadReversedOption");
        this.f12221a.add("hideRpmX100Option");
        this.f12221a.add("advancedVeAnalyze");
        this.f12221a.add("veAnalyze");
        this.f12221a.add("optionalFields");
        this.f12221a.add("customFields");
        this.f12221a.add("compareMode");
        this.f12221a.add("searchLogFiles");
        this.f12221a.add("tablePremium");
        this.f12221a.add("fileEditing");
        this.f12221a.add("advancedMathFunctions");
        this.f12221a.add("optionalQuickSelect");
        this.f12221a.add("selectGraphTraceCount");
        this.f12221a.add("selectableFields");
        this.f12221a.add("optionalTableLayouts");
        this.f12221a.add("timeslipData");
        this.f12221a.add("hideYaxisSelection");
        this.f12221a.add("fieldNameNormalization");
        this.f12221a.add("tabbedQuickViews");
        this.f12221a.add("fieldSmoothing");
        this.f12221a.add("triggerLogViewer");
        this.f12221a.add("okdsas32lkg09832jnegm7");
        this.f12221a.add("timeslipData");
        i.f12275v = ",";
        C0171aa.f2044d = true;
        W.W.a().a(X.f1959a, C0546f.class);
        W.W.a().a(X.f1981w, C0544d.class);
        W.W.a().a(X.f1993I, an.class);
        V.f1955d = false;
        f12234m = new String[]{"MAP", "Scaled_Load", "Scaled Load", "Scld_Load", "TPS", "TP ADC", "kPaFromPsi"};
    }

    private void d() {
        this.f12221a.add("scatterPlots");
        this.f12221a.add("triggerLogViewer");
        this.f12221a.add("tableGenerator");
        this.f12221a.add("tuningPanelVisible");
        this.f12221a.add("advancedVeAnalyze");
        this.f12221a.add("veAnalyze");
        this.f12221a.add("tablePremium");
        this.f12221a.add("optionalFields");
        this.f12221a.add("customFields");
        this.f12221a.add("compareMode");
        this.f12221a.add("hideRpmX100Option");
        this.f12221a.add("searchLogFiles");
        this.f12221a.add("fileEditing");
        this.f12221a.add("selectableLookAndFeel");
        this.f12221a.add("optionalQuickSelect");
        this.f12221a.add("selectGraphTraceCount");
        this.f12221a.add("selectableFields");
        this.f12221a.add("optionalTableLayouts");
        this.f12221a.add("advancedMathFunctions");
        this.f12221a.add("fillNaN");
        this.f12221a.add("timeslipData");
        this.f12221a.add("fieldSmoothing");
        this.f12221a.add("fieldNameNormalization");
        this.f12221a.add("fieldNameNormalizationEditable");
        this.f12221a.add("tabbedQuickViews");
        this.f12221a.add("fa-9fdspoijoijnfdz09jfdsa098j");
        this.f12221a.add("fdsahoirew098rew3284lksafd");
        this.f12221a.add("spoji[asfi2309jdi234iofwae2344");
        this.f12221a.add("okdsas32lkg09832jnegm7");
        this.f12221a.add("4e345hggsdhd4812hfd43-0gdk");
        this.f12221a.add("lkj094320/    q0-fmtg8vc");
        i.f12269p = false;
        new C1738c(this).start();
    }

    private void e() {
        this.f12221a.add("scatterPlots");
        this.f12221a.add("tuningPanelVisible");
        this.f12221a.add("tableGenerator");
        this.f12221a.add("veAnalyze");
        this.f12221a.add("hideRpmX100Option");
        this.f12221a.add("showRegisterTab");
        this.f12221a.add("fieldSmoothing");
        this.f12221a.add("fieldNameNormalizationEditable");
        this.f12221a.add("fieldNameNormalization");
        this.f12221a.add("tabbedQuickViews");
        this.f12221a.add("4e345hggsdhd4812hfd43-0gdk");
        this.f12221a.add("lkj094320/    q0-fmtg8vc");
        i.f12269p = false;
        C1724b.a(1000);
        new C1739d(this).start();
    }

    public boolean a(String str) {
        return this.f12221a.contains(str);
    }

    public boolean b(String str) {
        return this.f12233p.a(str);
    }
}
