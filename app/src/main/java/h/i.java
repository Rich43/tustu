package h;

import bH.C;
import bH.W;
import g.C1733k;
import java.awt.Color;
import java.awt.Image;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/* loaded from: TunerStudioMS.jar:h/i.class */
public class i {

    /* renamed from: a, reason: collision with root package name */
    public static String f12254a = "4.5.21";

    /* renamed from: b, reason: collision with root package name */
    public static String f12255b = "MegaLogViewer";

    /* renamed from: c, reason: collision with root package name */
    public static String f12256c = C1737b.f12223c + C1737b.f12222b;

    /* renamed from: d, reason: collision with root package name */
    public static Properties f12257d = null;

    /* renamed from: e, reason: collision with root package name */
    public static Properties f12258e = null;

    /* renamed from: f, reason: collision with root package name */
    public static Properties f12259f = null;

    /* renamed from: g, reason: collision with root package name */
    public static String f12260g = "mlv.reg";

    /* renamed from: h, reason: collision with root package name */
    public static String f12261h = ".mlvreg";

    /* renamed from: i, reason: collision with root package name */
    public static String f12262i = "mlvUser.properties";

    /* renamed from: j, reason: collision with root package name */
    public static String f12263j = null;

    /* renamed from: k, reason: collision with root package name */
    public static String f12264k = "MegaLogViewer.exe";

    /* renamed from: l, reason: collision with root package name */
    public static String f12265l = "MegaLogViewer.jar";

    /* renamed from: m, reason: collision with root package name */
    public static String f12266m = "http://www.efiAnalytics.com/MegaLogViewer/";

    /* renamed from: n, reason: collision with root package name */
    public static String f12267n = "https://www.efianalytics.com/register/viewProduct.jsp?productCode=MegaLogViewer";

    /* renamed from: o, reason: collision with root package name */
    public static boolean f12268o = false;

    /* renamed from: p, reason: collision with root package name */
    public static boolean f12269p = true;

    /* renamed from: q, reason: collision with root package name */
    public static boolean f12270q = false;

    /* renamed from: r, reason: collision with root package name */
    public static int f12271r = 30;

    /* renamed from: s, reason: collision with root package name */
    public static int f12272s = 8;

    /* renamed from: t, reason: collision with root package name */
    public static int f12273t = 4;

    /* renamed from: u, reason: collision with root package name */
    public static int f12274u = 4;

    /* renamed from: v, reason: collision with root package name */
    public static String f12275v = "\t";

    /* renamed from: w, reason: collision with root package name */
    public static String f12276w = "bin";

    /* renamed from: x, reason: collision with root package name */
    public static boolean f12277x = true;

    /* renamed from: y, reason: collision with root package name */
    public static String f12278y = "completeUpdate";

    /* renamed from: z, reason: collision with root package name */
    public static boolean f12279z = false;

    /* renamed from: A, reason: collision with root package name */
    public static String f12280A = "./resources/MLVLogoB.gif";

    /* renamed from: B, reason: collision with root package name */
    public static String f12281B = "resources/icon.gif";

    /* renamed from: C, reason: collision with root package name */
    public static String f12282C = "projectDirs";

    /* renamed from: D, reason: collision with root package name */
    public static String f12283D = "loggerFileDir";

    /* renamed from: E, reason: collision with root package name */
    public static String f12284E = "alphabetizeFieldLists";

    /* renamed from: F, reason: collision with root package name */
    public static boolean f12285F = true;

    /* renamed from: G, reason: collision with root package name */
    public static String f12286G = "mouseWheelAction";

    /* renamed from: H, reason: collision with root package name */
    public static String f12287H = "mouseWheelActionZoom";

    /* renamed from: I, reason: collision with root package name */
    public static String f12288I = "mouseWheelActionPage";

    /* renamed from: J, reason: collision with root package name */
    public static String f12289J = f12287H;

    /* renamed from: K, reason: collision with root package name */
    public static String f12290K = "showGraphText";

    /* renamed from: L, reason: collision with root package name */
    public static String f12291L = "showGraphMinMax";

    /* renamed from: M, reason: collision with root package name */
    public static boolean f12292M = true;

    /* renamed from: N, reason: collision with root package name */
    public static String f12293N = "viewTableLayout";

    /* renamed from: O, reason: collision with root package name */
    public static String f12294O = "0TableLayout";

    /* renamed from: P, reason: collision with root package name */
    public static String f12295P = "1TableLayout";

    /* renamed from: Q, reason: collision with root package name */
    public static String f12296Q = "2TablesLayout";

    /* renamed from: R, reason: collision with root package name */
    public static String f12297R = f12296Q;

    /* renamed from: S, reason: collision with root package name */
    public static String f12298S = "paintCursorValuesPosition";

    /* renamed from: T, reason: collision with root package name */
    public static String f12299T = "withLabels";

    /* renamed from: U, reason: collision with root package name */
    public static String f12300U = "scaleToFitFullLogOnLoad";

    /* renamed from: V, reason: collision with root package name */
    public static boolean f12301V = false;

    /* renamed from: W, reason: collision with root package name */
    public static String f12302W = "verticalDividerLocation";

    /* renamed from: X, reason: collision with root package name */
    public static String f12303X = "horizontalDividerLocation";

    /* renamed from: Y, reason: collision with root package name */
    public static String f12304Y = "Tmr_Enable";

    /* renamed from: Z, reason: collision with root package name */
    public static String f12305Z = "forceOpenGL";

    /* renamed from: aa, reason: collision with root package name */
    public static String f12306aa = "repeatGraphColors";

    /* renamed from: ab, reason: collision with root package name */
    public static boolean f12307ab = true;

    /* renamed from: ac, reason: collision with root package name */
    public static String f12308ac = "preferredVeAnalyzeAfrField";

    /* renamed from: ad, reason: collision with root package name */
    public static String f12309ad = "preferredVeAnalyzeGegoField";

    /* renamed from: ae, reason: collision with root package name */
    public static int f12310ae = 1;

    /* renamed from: af, reason: collision with root package name */
    public static String f12311af = "antialiasGraphLine";

    /* renamed from: ag, reason: collision with root package name */
    public static boolean f12312ag = false;

    /* renamed from: ah, reason: collision with root package name */
    public static String f12313ah = "patterenedGraphLine";

    /* renamed from: ai, reason: collision with root package name */
    public static boolean f12314ai = false;

    /* renamed from: aj, reason: collision with root package name */
    public static String f12315aj = "openLastQuickView";

    /* renamed from: ak, reason: collision with root package name */
    public static boolean f12316ak = true;

    /* renamed from: al, reason: collision with root package name */
    public static String f12317al = "windowMaximized";

    /* renamed from: aJ, reason: collision with root package name */
    private static String f12318aJ = null;

    /* renamed from: aK, reason: collision with root package name */
    private static List f12319aK = new ArrayList();

    /* renamed from: am, reason: collision with root package name */
    public static String f12320am = "remoteFileDialogWidth";

    /* renamed from: an, reason: collision with root package name */
    public static String f12321an = "remoteFileDialogHeight";

    /* renamed from: ao, reason: collision with root package name */
    public static String f12322ao = "remoteFileExists";

    /* renamed from: ap, reason: collision with root package name */
    public static int f12323ap = 0;

    /* renamed from: aq, reason: collision with root package name */
    public static int f12324aq = 1;

    /* renamed from: ar, reason: collision with root package name */
    public static int f12325ar = 3;

    /* renamed from: as, reason: collision with root package name */
    public static String f12326as = "remoteFileDownloadLocation";

    /* renamed from: at, reason: collision with root package name */
    public static String f12327at = "remoteFileDownloadLocationPref";

    /* renamed from: au, reason: collision with root package name */
    public static int f12328au = 0;

    /* renamed from: av, reason: collision with root package name */
    public static int f12329av = 1;

    /* renamed from: aw, reason: collision with root package name */
    public static String f12330aw = "timeslipRolloutTime";

    /* renamed from: ax, reason: collision with root package name */
    public static String f12331ax = "jumpToTimeslipLaunch";

    /* renamed from: ay, reason: collision with root package name */
    public static boolean f12332ay = true;

    /* renamed from: az, reason: collision with root package name */
    public static String f12333az = "generateTimeslip";

    /* renamed from: aA, reason: collision with root package name */
    public static boolean f12334aA = true;

    /* renamed from: aB, reason: collision with root package name */
    public static String f12335aB = "showTimeslip";

    /* renamed from: aC, reason: collision with root package name */
    public static boolean f12336aC = true;

    /* renamed from: aD, reason: collision with root package name */
    public static String f12337aD = "keepSyncWith2ndInstance";

    /* renamed from: aE, reason: collision with root package name */
    public static boolean f12338aE = true;

    /* renamed from: aF, reason: collision with root package name */
    public static String f12339aF = "autoZeroExpression";

    /* renamed from: aG, reason: collision with root package name */
    public static String f12340aG = "enableDeadO2Filter";

    /* renamed from: aH, reason: collision with root package name */
    public static boolean f12341aH = true;

    /* renamed from: aI, reason: collision with root package name */
    public static Image f12342aI = null;

    private i() {
    }

    public static void a(j jVar) {
        f12319aK.add(jVar);
    }

    private static void l() {
        Iterator it = f12319aK.iterator();
        while (it.hasNext()) {
            ((j) it.next()).a();
        }
    }

    public static Properties a() {
        if (f12257d == null) {
            f12257d = new Properties();
            String str = W.b(f12255b, " ", "") + ".properties";
            if (f12263j != null && f12263j.length() > 0) {
                str = f12263j;
            }
            try {
                f12257d.load(new FileInputStream(str));
            } catch (Exception e2) {
                System.out.println("ERROR loading " + str);
                e2.printStackTrace();
                System.out.println("Looking in: " + new File(".").getAbsolutePath());
            }
        }
        return f12257d;
    }

    public static String b() {
        if (a("storePropertiesLocal", "false").equals("true")) {
            return f12260g;
        }
        File file = new File(((Object) h.a()) + File.separator + f12260g);
        if (file.exists()) {
            return file.getAbsolutePath();
        }
        File file2 = (f12256c.equals("MS") || f12256c.equals(new StringBuilder().append(C1737b.f12223c).append(C1737b.f12222b).toString())) ? new File(System.getProperty("user.home") + File.separator + f12261h) : new File(System.getProperty("user.home") + File.separator + f12260g);
        return file2.exists() ? file2.getAbsolutePath() : new File(((Object) h.a()) + File.separator + f12260g).getAbsolutePath();
    }

    public static Properties c() {
        if (f12259f == null) {
            f12259f = new Properties();
            try {
                f12259f.load(new FileInputStream(b()));
            } catch (Exception e2) {
                System.out.println("Registration file not found. ");
            }
        }
        return f12259f;
    }

    public static String a(String str) {
        String strD = null;
        if (str.equals(f12283D)) {
            strD = h.d();
        }
        return strD;
    }

    public static String a(String str, String str2) {
        String strB = b(str);
        return (strB == null || strB.equals("")) ? str2 : strB;
    }

    public static String b(String str) {
        String strE = e(str, null);
        if (strE == null || strE.equals("")) {
            strE = c(str);
        }
        return strE;
    }

    public static String b(String str, String str2) {
        String strC = c(str);
        if (strC == null || strC.isEmpty()) {
            strC = str2;
        }
        return strC;
    }

    public static String c(String str) {
        return h(a().getProperty(str));
    }

    private static String h(String str) {
        if (str == null) {
            return str;
        }
        while (str.indexOf("SUB_FORMULA") != -1) {
            String strSubstring = str.substring(str.indexOf("SUB_FORMULA"), str.indexOf(")", str.indexOf("SUB_FORMULA")) + 1);
            str = C1733k.a(str, strSubstring, "(" + a("SUB_FORMULA_" + strSubstring.substring(strSubstring.indexOf("(") + 1, strSubstring.length() - 1), "") + ")");
        }
        return str;
    }

    public static void c(String str, String str2) {
        if (str2 == null || str2.equals("")) {
            f().remove(str);
        } else {
            f().setProperty(str, str2);
        }
    }

    public static void d(String str, String str2) {
        if (str2 == null || str2.equals("")) {
            c().remove(str);
        } else {
            c().setProperty(str, str2);
        }
    }

    public static String d() {
        if (f12318aJ == null) {
            try {
                f12318aJ = new String("*".getBytes(), "UTF-8");
            } catch (UnsupportedEncodingException e2) {
                Logger.getLogger(i.class.getName()).log(Level.WARNING, "Bad Asterick??", (Throwable) e2);
                f12318aJ = "~";
            }
        }
        return f12318aJ;
    }

    public static void d(String str) {
        f().remove(str);
    }

    public static String e(String str, String str2) {
        String property = f().getProperty(str);
        if (property == null || property.equals("")) {
            property = c().getProperty(str);
        }
        if (property == null || property.equals("")) {
            property = str2;
        }
        return property;
    }

    public static String f(String str, String str2) {
        String property = f().getProperty(str);
        if (property == null || property.equals("")) {
            property = c().getProperty(str);
        }
        if (property == null || property.equals("")) {
            property = c(str);
        }
        if (property == null || property.equals("")) {
            property = str2;
        }
        return property;
    }

    public static boolean e() {
        String property = f().getProperty("uid");
        return property != null && property.length() > 0;
    }

    private static String m() {
        File file = new File(((Object) h.a()) + File.separator + f12262i);
        if (!a("storePropertiesLocal", "false").equals("true")) {
            if (!file.exists()) {
                file = new File(System.getProperty("user.home") + File.separator + f12262i);
                file.deleteOnExit();
            }
            if (!file.exists()) {
                file = new File(n().getAbsolutePath() + ".bak");
            }
            if (file.exists()) {
                return file.getAbsolutePath();
            }
        }
        return f12262i;
    }

    private static File n() {
        return a("storePropertiesLocal", "false").equals("true") ? new File(f12262i) : new File(((Object) h.a()) + File.separator + f12262i);
    }

    public static Properties f() {
        if (f12258e == null) {
            f12258e = new Properties();
            try {
                File file = new File(m());
                File file2 = new File(((Object) n()) + ".bak");
                if (a(file)) {
                    f12258e.load(new FileInputStream(file));
                } else if (a(file2)) {
                    f12258e.load(new FileInputStream(file2));
                }
            } catch (FileNotFoundException e2) {
                C.c("user Properties file not found. " + e2.getLocalizedMessage());
            } catch (Exception e3) {
                System.out.println("ERROR loadinguser properties");
                e3.printStackTrace();
            }
        }
        return f12258e;
    }

    /* JADX WARN: Removed duplicated region for block: B:14:0x003c  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private static boolean a(java.io.File r6) {
        /*
            r0 = r6
            boolean r0 = r0.exists()
            if (r0 == 0) goto L12
            r0 = r6
            long r0 = r0.length()
            r1 = 150(0x96, double:7.4E-322)
            int r0 = (r0 > r1 ? 1 : (r0 == r1 ? 0 : -1))
            if (r0 >= 0) goto L14
        L12:
            r0 = 0
            return r0
        L14:
            r0 = 0
            r7 = r0
            java.io.BufferedReader r0 = new java.io.BufferedReader     // Catch: java.io.FileNotFoundException -> L4d java.io.IOException -> L5f java.lang.Throwable -> L71
            r1 = r0
            java.io.FileReader r2 = new java.io.FileReader     // Catch: java.io.FileNotFoundException -> L4d java.io.IOException -> L5f java.lang.Throwable -> L71
            r3 = r2
            r4 = r6
            r3.<init>(r4)     // Catch: java.io.FileNotFoundException -> L4d java.io.IOException -> L5f java.lang.Throwable -> L71
            r1.<init>(r2)     // Catch: java.io.FileNotFoundException -> L4d java.io.IOException -> L5f java.lang.Throwable -> L71
            r7 = r0
            r0 = r7
            java.lang.String r0 = r0.readLine()     // Catch: java.io.FileNotFoundException -> L4d java.io.IOException -> L5f java.lang.Throwable -> L71
            r8 = r0
            r0 = r8
            if (r0 == 0) goto L3c
            r0 = r8
            java.lang.String r1 = "#Do not edit this file the Application will maintain this."
            boolean r0 = r0.startsWith(r1)     // Catch: java.io.FileNotFoundException -> L4d java.io.IOException -> L5f java.lang.Throwable -> L71
            if (r0 == 0) goto L3c
            r0 = 1
            goto L3d
        L3c:
            r0 = 0
        L3d:
            r9 = r0
            r0 = r7
            if (r0 == 0) goto L4b
            r0 = r7
            r0.close()     // Catch: java.io.IOException -> L49
            goto L4b
        L49:
            r10 = move-exception
        L4b:
            r0 = r9
            return r0
        L4d:
            r8 = move-exception
            r0 = 0
            r9 = r0
            r0 = r7
            if (r0 == 0) goto L5d
            r0 = r7
            r0.close()     // Catch: java.io.IOException -> L5b
            goto L5d
        L5b:
            r10 = move-exception
        L5d:
            r0 = r9
            return r0
        L5f:
            r8 = move-exception
            r0 = 0
            r9 = r0
            r0 = r7
            if (r0 == 0) goto L6f
            r0 = r7
            r0.close()     // Catch: java.io.IOException -> L6d
            goto L6f
        L6d:
            r10 = move-exception
        L6f:
            r0 = r9
            return r0
        L71:
            r11 = move-exception
            r0 = r7
            if (r0 == 0) goto L80
            r0 = r7
            r0.close()     // Catch: java.io.IOException -> L7e
            goto L80
        L7e:
            r12 = move-exception
        L80:
            r0 = r11
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: h.i.a(java.io.File):boolean");
    }

    public static void g() {
        if (f12258e == null || f12258e.get("version") == null) {
            return;
        }
        File fileN = n();
        FileOutputStream fileOutputStream = null;
        try {
            try {
                File file = new File(fileN.getAbsolutePath() + ".bak");
                if (a(fileN) && fileN.length() > 300) {
                    try {
                        file.delete();
                        File file2 = new File(fileN.getAbsolutePath() + ".bak");
                        if (!fileN.renameTo(file2)) {
                            C.c("Save user properties backup failed rename, try again.");
                            Thread.sleep(250L);
                            file2.delete();
                            fileN.renameTo(new File(fileN.getAbsolutePath() + ".bak"));
                        }
                    } catch (Exception e2) {
                        C.a("Error saving User Setting Backup.");
                    }
                }
                fileOutputStream = new FileOutputStream(fileN);
                f12258e.store(fileOutputStream, "Do not edit this file the Application will maintain this.\nAll editable properties are in " + f12263j);
                C.c("Saved user log settings.");
                l();
                if (fileOutputStream != null) {
                    try {
                        fileOutputStream.close();
                    } catch (Exception e3) {
                    }
                }
            } catch (Throwable th) {
                if (fileOutputStream != null) {
                    try {
                        fileOutputStream.close();
                    } catch (Exception e4) {
                        throw th;
                    }
                }
                throw th;
            }
        } catch (Exception e5) {
            System.out.println("Error saving user.properties:");
            e5.printStackTrace();
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.close();
                } catch (Exception e6) {
                }
            }
        }
    }

    public static void h() throws V.a {
        if (f12258e != null) {
            String str = f12260g;
            if (!a("storePropertiesLocal", "false").equals("true")) {
                str = ((Object) h.a()) + File.separator + f12260g;
            }
            try {
                FileOutputStream fileOutputStream = new FileOutputStream(new File(str));
                c().store(fileOutputStream, f12255b + " " + f12256c + " registration info. EFI Analytics.");
                fileOutputStream.flush();
            } catch (Exception e2) {
                System.out.println("Error saving: " + str);
                e2.printStackTrace();
                throw new V.a("Unable to save registration information to disk.\nDo you have write access to the installation folder?");
            }
        }
    }

    public static Color a(String str, Color color) {
        try {
            String strE = e(str, null);
            if (strE == null || strE.equals("")) {
                strE = c(str);
            }
            if (strE == null) {
                strE = c(str.toLowerCase());
            }
            return new Color(Integer.parseInt(c(strE)));
        } catch (Exception e2) {
            return color;
        }
    }

    public static int a(String str, int i2) {
        try {
            return Integer.parseInt(b(str));
        } catch (Exception e2) {
            return i2;
        }
    }

    public static int b(String str, int i2) {
        try {
            return Integer.parseInt(e(str, "" + i2));
        } catch (Exception e2) {
            return i2;
        }
    }

    public static long a(String str, long j2) {
        try {
            return Long.parseLong(e(str, "" + j2));
        } catch (Exception e2) {
            return j2;
        }
    }

    public static double a(String str, double d2) {
        try {
            return Double.parseDouble(e(str, "" + d2));
        } catch (Exception e2) {
            return d2;
        }
    }

    public static boolean a(String str, boolean z2) {
        try {
            return f(str, "" + z2).equals("true");
        } catch (Exception e2) {
            return z2;
        }
    }

    public static String[] e(String str) {
        ArrayList arrayList = new ArrayList();
        Iterator<Object> it = a().keySet().iterator();
        while (it.hasNext()) {
            String str2 = (String) it.next();
            if (str2.startsWith(str) && !arrayList.contains(str2)) {
                arrayList.add(str2);
            }
        }
        Iterator<Object> it2 = f().keySet().iterator();
        while (it2.hasNext()) {
            String str3 = (String) it2.next();
            if (str3.startsWith(str) && !arrayList.contains(str3)) {
                arrayList.add(str3);
            }
        }
        Object[] array = arrayList.toArray();
        String[] strArr = new String[array.length];
        for (int i2 = 0; i2 < strArr.length; i2++) {
            strArr[i2] = (String) array[i2];
        }
        return strArr;
    }

    public static String[] f(String str) {
        ArrayList arrayList = new ArrayList();
        Iterator<Object> it = f().keySet().iterator();
        while (it.hasNext()) {
            String str2 = (String) it.next();
            if (str2.startsWith(str)) {
                arrayList.add(str2);
            }
        }
        Object[] array = arrayList.toArray();
        String[] strArr = new String[array.length];
        for (int i2 = 0; i2 < strArr.length; i2++) {
            strArr[i2] = (String) array[i2];
        }
        return strArr;
    }

    public static String[] g(String str) {
        ArrayList arrayList = new ArrayList();
        Iterator<Object> it = a().keySet().iterator();
        while (it.hasNext()) {
            String str2 = (String) it.next();
            if (str2.startsWith(str)) {
                arrayList.add(str2);
            }
        }
        Object[] array = arrayList.toArray();
        String[] strArr = new String[array.length];
        for (int i2 = 0; i2 < strArr.length; i2++) {
            strArr[i2] = (String) array[i2];
        }
        return strArr;
    }

    public static String i() {
        return f12255b + " v" + f12254a + "\nBy\nEFI Analytics\n \n \nMulti-Platform Windows, Linux and MAC\nJava Runtime version: " + System.getProperty("java.vendor") + " - " + System.getProperty("os.arch") + " " + System.getProperty("java.version") + "\nProcessor Score: " + a("loopCount", "") + "\n \n \n" + f12266m + " \nsupport@efianalytics.com ©2005-" + Calendar.getInstance().get(1) + "\n";
    }

    public static boolean j() {
        return a("delimiter", "Auto").equals("Auto");
    }

    public static String k() {
        return W.b(W.b(f12255b + W.b(f12256c, C1737b.f12222b, ""), " ", ""), C1737b.f12222b, "");
    }
}
