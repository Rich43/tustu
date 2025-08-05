package aP;

import bH.C1011s;
import com.efiAnalytics.apps.ts.dashboard.AbstractC1420s;
import com.efiAnalytics.apps.ts.dashboard.C1425x;
import com.efiAnalytics.apps.ts.dashboard.HtmlDisplay;
import com.efiAnalytics.tunerStudio.search.ContinuousIpSearchPanel;
import java.awt.Font;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.math3.geometry.VectorFormat;
import org.icepdf.core.util.PdfOps;
import r.C1798a;
import r.C1806i;
import r.C1807j;
import r.C1811n;
import s.C1818g;

/* loaded from: TunerStudioMS.jar:aP/iK.class */
public class iK {

    /* renamed from: g, reason: collision with root package name */
    private File f3657g;

    /* renamed from: f, reason: collision with root package name */
    private static iK f3652f = null;

    /* renamed from: h, reason: collision with root package name */
    private static String f3658h = "help/register.zip";

    /* renamed from: i, reason: collision with root package name */
    private static String f3659i = "help/ad.zip";

    /* renamed from: j, reason: collision with root package name */
    private static String f3660j = "help/main.zip";

    /* renamed from: l, reason: collision with root package name */
    private static String f3663l = "font-size:";

    /* renamed from: m, reason: collision with root package name */
    private static String f3664m = "width=";

    /* renamed from: n, reason: collision with root package name */
    private static String f3665n = "height=";

    /* renamed from: a, reason: collision with root package name */
    HtmlDisplay f3653a = new HtmlDisplay();

    /* renamed from: b, reason: collision with root package name */
    HtmlDisplay f3654b = new HtmlDisplay();

    /* renamed from: c, reason: collision with root package name */
    HtmlDisplay f3655c = new HtmlDisplay();

    /* renamed from: d, reason: collision with root package name */
    ContinuousIpSearchPanel f3656d = null;

    /* renamed from: e, reason: collision with root package name */
    iN f3661e = new iN();

    /* renamed from: k, reason: collision with root package name */
    private HashMap f3662k = new HashMap();

    private iK() {
        this.f3657g = null;
        this.f3657g = C1807j.d();
        this.f3653a.setFont(new Font("SansSerif", 0, 25));
    }

    public static iK a() {
        if (f3652f == null) {
            f3652f = new iK();
        }
        return f3652f;
    }

    public void a(C1425x c1425x) {
        c();
        b(c1425x);
        c1425x.e(true);
        c1425x.validate();
    }

    private void c() throws V.a {
        d();
        e();
        f();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void d() throws V.a {
        File fileB = b("main");
        File file = new File(fileB, "startScreen.html");
        if (!file.exists()) {
            try {
                bH.ad.a(new File(".", f3660j), fileB, (String) null);
            } catch (Exception e2) {
                throw new V.a("Problem extracting Main Start Screen files. It appears your installation is corrupt or file permissions incorrect.");
            }
        }
        if (!file.exists()) {
            throw new V.a("Start Screen files missing. It appears your instalation is corrupt or file permissions incorrect.");
        }
        File file2 = new File(fileB, "startScreen_" + C1818g.c().getLanguage() + ".html");
        if (!file2.exists()) {
            file2 = new File(fileB, "startScreen.html");
        }
        this.f3653a.setDocumentUrl(a(file2.getAbsolutePath()).getAbsolutePath());
        this.f3653a.validate();
        this.f3661e.b(fileB, "main", new iL(this));
    }

    private void e() throws V.a {
        File fileB = b("register");
        File file = new File(fileB, "registeredMain.html");
        if (!file.exists()) {
            try {
                bH.ad.a(new File(".", f3658h), fileB, (String) null);
            } catch (Exception e2) {
                throw new V.a("Problem extracting Start Screen files. It appears your installation is corrupt or file permissions incorrect.");
            }
        }
        if (!file.exists()) {
            throw new V.a("Start Screen files missing. It appears your instalation is corrupt or file permissions incorrect.");
        }
        if (C1806i.a().a("-0ofdspok54sg")) {
            File file2 = new File(fileB, "registeredMain_" + C1818g.c().getLanguage() + ".html");
            if (!file2.exists()) {
                file2 = new File(fileB, "registeredMain.html");
            }
            this.f3654b.setDocumentUrl(a(file2.getAbsolutePath()).getAbsolutePath());
            return;
        }
        File file3 = new File(fileB, "registerMain_" + C1818g.c().getLanguage() + ".html");
        if (!file3.exists()) {
            file3 = new File(fileB, "registerMain.html");
        }
        this.f3654b.setDocumentUrl(a(file3.getAbsolutePath()).getAbsolutePath());
    }

    private File b(String str) throws V.a {
        File file = new File(this.f3657g, str);
        if (!file.exists()) {
            File file2 = new File(file, "tmp");
            if (!file2.mkdirs()) {
                throw new V.a("Unable to create Start Screen Root dir:\n" + file.getAbsolutePath());
            }
            file2.delete();
        } else if (file.isFile()) {
            file.delete();
            file.mkdir();
        }
        if (!C1818g.c().equals(C.a.a().c())) {
            File file3 = new File(file, C1818g.c().getLanguage());
            if (file3.exists() && file3.isDirectory()) {
                file = file3;
            }
        }
        return file;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void f() throws V.a {
        File file;
        File fileB = b("ad");
        File file2 = new File(fileB, "index.html");
        if (!file2.exists()) {
            try {
                bH.ad.a(new File(".", f3659i), fileB, (String) null);
            } catch (Exception e2) {
                throw new V.a("Problem extracting Main Start Screen files. It appears your installation is corrupt or file permissions incorrect.");
            }
        }
        if (!file2.exists()) {
            throw new V.a("Start Screen files missing. It appears your instalation is corrupt or file permissions incorrect.");
        }
        int iA = C1798a.a().a(C1798a.f13290x, -1) + 1;
        if (iA == 0) {
            file = new File(fileB, "index_" + C1818g.c().getLanguage() + ".html");
            if (!file.exists()) {
                file = new File(fileB, "index.html");
            }
        } else {
            file = new File(fileB, "index" + iA + "_" + C1818g.c().getLanguage() + ".html");
            if (!file.exists()) {
                file = new File(fileB, "index" + iA + ".html");
            }
            if (!file.exists()) {
                iA = 0;
                file = new File(fileB, "index.html");
            }
        }
        File fileA = a(file.getAbsolutePath());
        C1798a.a().b(C1798a.f13290x, "" + iA);
        this.f3655c.setDocumentUrl(fileA.getAbsolutePath());
        this.f3661e.b(fileB, "ad", new iM(this));
    }

    private void b(C1425x c1425x) {
        c1425x.c();
        this.f3654b.setRelativeX(0.75d);
        this.f3654b.setRelativeY(0.0d);
        this.f3654b.setRelativeWidth(0.25d);
        this.f3654b.setRelativeHeight(0.28d);
        c1425x.b((AbstractC1420s) this.f3654b);
        this.f3655c.setRelativeX(0.75d);
        this.f3655c.setRelativeY(0.28d);
        this.f3655c.setRelativeWidth(0.25d);
        this.f3655c.setRelativeHeight(0.72d);
        c1425x.b((AbstractC1420s) this.f3655c);
        this.f3653a.setRelativeX(0.0d);
        this.f3653a.setRelativeY(0.0d);
        this.f3653a.setRelativeWidth(0.75d);
        this.f3653a.setRelativeHeight(1.0d);
        c1425x.b((AbstractC1420s) this.f3653a);
    }

    public File a(String str) {
        File file = new File(str);
        File file2 = new File(file.getParentFile(), "prepped_" + file.getName());
        file2.deleteOnExit();
        return a(file, file2);
    }

    /* JADX WARN: Removed duplicated region for block: B:16:0x009c  */
    /* JADX WARN: Removed duplicated region for block: B:30:0x00f0  */
    /* JADX WARN: Removed duplicated region for block: B:43:0x0133  */
    /* JADX WARN: Removed duplicated region for block: B:50:0x0097 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:55:0x012e A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:60:0x00eb A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public java.io.File a(java.io.File r7, java.io.File r8) {
        /*
            Method dump skipped, instructions count: 334
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: aP.iK.a(java.io.File, java.io.File):java.io.File");
    }

    private String c(String str) {
        if (str.contains("$RecentProjectLinkList")) {
            str = C1806i.a().a("h-0ewkfd[pfd[pew") ? bH.W.b(str, "$RecentProjectLinkList", "<br><a href=\"appAction:browseProjects\">" + C1818g.b("Open Project") + "</a>") : bH.W.b(str, "$RecentProjectLinkList", g());
        }
        if (str.contains("$lastProjectName")) {
            str = bH.W.b(str, "$lastProjectName", h());
        }
        if (str.contains("$lastProjectName")) {
            str = bH.W.b(str, "$lastProjectName", h());
        }
        if (str.contains("$firstName")) {
            str = bH.W.b(str, "$firstName", C1798a.a().a(C1798a.cC, "Unknown"));
        }
        if (str.contains("$lastName")) {
            str = bH.W.b(str, "$lastName", C1798a.a().a(C1798a.cD, "Unknown"));
        }
        if (str.contains("$upgradeLink")) {
            String strC = C1806i.a().c();
            if (strC == null) {
                strC = "";
            }
            str = bH.W.b(str, "$upgradeLink", strC);
        }
        if (str.contains("$Year")) {
            str = bH.W.b(str, "$Year", "" + C1798a.f13328aj);
        }
        if (str.contains("$VersionNumber")) {
            str = bH.W.b(str, "$VersionNumber", "" + C1798a.f13267a);
        }
        if (str.contains("$appFullName")) {
            str = bH.W.b(str, "$appFullName", C1798a.a().b());
        }
        for (iV iVVar : this.f3662k.values()) {
            while (str.contains(iVVar.a())) {
                str = bH.W.b(str, iVVar.a(), iVVar.b());
            }
        }
        while (str.contains("$scaleToDpi(")) {
            String strSubstring = str.substring(0, str.indexOf("$scaleToDpi("));
            try {
                str = strSubstring + com.efiAnalytics.ui.eJ.a(Integer.parseInt(str.substring(strSubstring.length() + "$scaleToDpi(".length(), str.indexOf(")", strSubstring.length() + "$scaleToDpi(".length())))) + str.substring(str.indexOf(")", strSubstring.length() + "$scaleToDpi(".length()) + 1);
            } catch (NumberFormatException e2) {
                bH.C.b("Poorly formatted $scaleToDpi() function in line:\n" + str);
            }
        }
        return a(f3665n, a(f3664m, str));
    }

    private String a(String str, String str2) {
        try {
            if (com.efiAnalytics.ui.eJ.b() && ((str2.contains(VectorFormat.DEFAULT_PREFIX) || str2.contains("<img") || str2.contains("background=")) && str2.contains(str))) {
                String strSubstring = str2.substring(0, str2.indexOf(str) + str.length());
                int length = strSubstring.length();
                String strB = "";
                while (true) {
                    if (str2.charAt(length) != ' ' && !bH.H.a(str2.charAt(length) + "")) {
                        break;
                    }
                    int i2 = length;
                    length++;
                    strB = strB + str2.charAt(i2);
                }
                String strSubstring2 = str2.substring(strSubstring.length() + strB.length());
                boolean zContains = strB.contains("px");
                if (zContains) {
                    strB = bH.W.b(strB, "px", "");
                }
                boolean zContains2 = strB.contains(";");
                if (zContains2) {
                    strB = bH.W.b(strB, ";", "");
                }
                String str3 = com.efiAnalytics.ui.eJ.a(Integer.parseInt(bH.W.b(strB, PdfOps.DOUBLE_QUOTE__TOKEN, "").trim())) + "";
                if (zContains) {
                    str3 = str3 + "px";
                }
                if (zContains2) {
                    str3 = str3 + ";";
                }
                str2 = strSubstring + str3 + " " + strSubstring2;
            }
        } catch (Exception e2) {
            bH.C.b("Failed to parse tag:" + str + " in line:\n" + str2);
        }
        return str2;
    }

    public void a(iV iVVar) {
        this.f3662k.put(iVVar.a(), iVVar);
    }

    private static String g() {
        ArrayList arrayListA = new C1811n().a();
        if (arrayListA.isEmpty()) {
            File[] fileArrListFiles = new File(C1807j.u()).listFiles();
            for (int i2 = 0; i2 < fileArrListFiles.length; i2++) {
                if (aE.a.a(fileArrListFiles[i2])) {
                    arrayListA.add(fileArrListFiles[i2].getAbsolutePath());
                }
            }
        }
        StringBuilder sb = new StringBuilder("<ul>\n");
        if (arrayListA.size() > 0) {
            Iterator it = arrayListA.iterator();
            while (it.hasNext()) {
                String str = (String) it.next();
                try {
                    sb.append("<li><a href=\"appAction:openProject?projectPath=" + URLEncoder.encode(str, "UTF-8") + "\">" + str.substring(str.lastIndexOf(File.separator) + 1) + "</a></li>\n");
                } catch (UnsupportedEncodingException e2) {
                    Logger.getLogger(iK.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
                }
            }
        }
        sb.append("<br><a href=\"appAction:browseProjects\">").append(C1818g.b("Other Projects") + "</a>");
        return sb.toString();
    }

    private static String h() {
        ArrayList arrayListA = new C1811n().a();
        if (arrayListA.size() <= 0) {
            return "None";
        }
        String str = (String) arrayListA.get(0);
        return str.substring(str.lastIndexOf(File.separator) + 1);
    }

    public void b() {
        C1011s.b(this.f3657g);
        this.f3661e.a();
    }
}
