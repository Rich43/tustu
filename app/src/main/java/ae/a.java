package aE;

import A.v;
import G.C0135r;
import W.ar;
import bH.C;
import bH.C1011s;
import bH.Q;
import bH.R;
import bH.W;
import bQ.j;
import com.sun.corba.se.impl.util.Version;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import jdk.internal.dynalink.CallSiteDescriptor;
import org.icepdf.core.util.PdfOps;

/* loaded from: TunerStudioMS.jar:aE/a.class */
public class a extends Properties {

    /* renamed from: n, reason: collision with root package name */
    private boolean f2354n = false;

    /* renamed from: o, reason: collision with root package name */
    private final ArrayList f2355o = new ArrayList();

    /* renamed from: q, reason: collision with root package name */
    private String f2357q = "";

    /* renamed from: r, reason: collision with root package name */
    private g f2358r = null;

    /* renamed from: s, reason: collision with root package name */
    private final ArrayList f2359s = new ArrayList();

    /* renamed from: t, reason: collision with root package name */
    private final ArrayList f2360t = new ArrayList();

    /* renamed from: v, reason: collision with root package name */
    private final Properties f2362v = new Properties();

    /* renamed from: w, reason: collision with root package name */
    private final String f2363w = "projectId";

    /* renamed from: x, reason: collision with root package name */
    private boolean f2364x = false;

    /* renamed from: a, reason: collision with root package name */
    public static c f2341a = null;

    /* renamed from: b, reason: collision with root package name */
    public static String f2342b = "15";

    /* renamed from: c, reason: collision with root package name */
    public static String f2343c = "lastDisplayedTuneFile";

    /* renamed from: d, reason: collision with root package name */
    public static String f2344d = "useCommonDashboardDir";

    /* renamed from: e, reason: collision with root package name */
    public static boolean f2345e = false;

    /* renamed from: f, reason: collision with root package name */
    public static String f2346f = "useCommonTuneViewDir";

    /* renamed from: g, reason: collision with root package name */
    public static boolean f2347g = false;

    /* renamed from: h, reason: collision with root package name */
    public static final String f2348h = "projectCfg" + File.separator;

    /* renamed from: i, reason: collision with root package name */
    public static final String f2349i = "restorePoints" + File.separator;

    /* renamed from: j, reason: collision with root package name */
    public static final String f2350j = "DataLogs" + File.separator;

    /* renamed from: k, reason: collision with root package name */
    public static String f2351k = "inc" + File.separator;

    /* renamed from: l, reason: collision with root package name */
    public static final String f2352l = "dashboard" + File.separator;

    /* renamed from: m, reason: collision with root package name */
    private static boolean f2353m = true;

    /* renamed from: p, reason: collision with root package name */
    private static a f2356p = null;

    /* renamed from: u, reason: collision with root package name */
    private static File f2361u = null;

    public a() {
        v.a().a(new ar(this, "CommSetting"));
        j.a().a(new ar(this, "CommSetting"));
    }

    public void a() throws V.a {
        if (t() == null || t().equals("")) {
            throw new V.a("Project working directory must be set before creating project");
        }
        File fileF = f();
        if (fileF.exists()) {
            throw new V.a("Project already exists, can not create in \n" + t());
        }
        try {
            fileF.getParentFile().mkdirs();
            fileF.createNewFile();
            File file = new File(t() + File.separator + f2350j, "dummy");
            file.mkdirs();
            file.delete();
            File file2 = new File(t() + File.separator + f2351k, "dummy");
            file2.mkdirs();
            file2.delete();
            File file3 = new File(t() + File.separator + f2352l, "dummy");
            file3.mkdirs();
            file3.delete();
        } catch (Exception e2) {
            C.a(e2);
            throw new V.a("Error creating project file, message:\n" + e2.getMessage());
        }
    }

    private void X() {
        File fileF = f();
        C.c("############################################# Save Project Backup ##########################################");
        if (d(fileF)) {
            File file = new File(fileF.getParentFile(), fileF.getName() + ".bkup");
            if (file.exists() && !file.delete()) {
                C.c("Unable to delete project backup.");
                return;
            }
            try {
                C1011s.a(fileF, file);
            } catch (V.a e2) {
                Logger.getLogger(a.class.getName()).log(Level.WARNING, "Failed to save project file backup:", (Throwable) e2);
            }
        }
    }

    public synchronized void b() {
        BufferedOutputStream bufferedOutputStream = null;
        try {
            try {
                X();
                Z();
                Y();
                z("userDash");
                ab();
                bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(f()));
                store(bufferedOutputStream, "Project Attributes.\n#" + f2341a.a() + " by EFI Analytics, Inc\n#Last Saved on: " + new Date().toString());
                bufferedOutputStream.flush();
                if (this.f2358r != null) {
                    File fileH = h();
                    if (!fileH.exists()) {
                        fileH.getParentFile().mkdirs();
                        fileH.createNewFile();
                    }
                    this.f2358r.a(fileH);
                }
                if (U() && this.f2354n) {
                    BufferedOutputStream bufferedOutputStream2 = null;
                    try {
                        try {
                            bufferedOutputStream2 = new BufferedOutputStream(new FileOutputStream(ac()));
                            this.f2362v.store(bufferedOutputStream2, "Common Dash.\n#" + f2341a.a() + " by EFI Analytics, Inc\n#Last Saved on: " + new Date().toString());
                            bufferedOutputStream2.flush();
                            if (bufferedOutputStream2 != null) {
                                try {
                                    bufferedOutputStream2.close();
                                } catch (Exception e2) {
                                }
                            }
                        } catch (Exception e3) {
                            C.a("Failed to save Common Dash Properties: " + e3.getLocalizedMessage());
                            if (bufferedOutputStream2 != null) {
                                try {
                                    bufferedOutputStream2.close();
                                } catch (Exception e4) {
                                }
                            }
                        }
                    } catch (Throwable th) {
                        if (bufferedOutputStream2 != null) {
                            try {
                                bufferedOutputStream2.close();
                            } catch (Exception e5) {
                            }
                        }
                        throw th;
                    }
                }
                try {
                    bufferedOutputStream.close();
                } catch (Exception e6) {
                    e6.printStackTrace();
                }
            } catch (Throwable th2) {
                try {
                    bufferedOutputStream.close();
                } catch (Exception e7) {
                    e7.printStackTrace();
                }
                throw th2;
            }
        } catch (Exception e8) {
            e8.printStackTrace();
            throw new V.a("Error Saving Project. \n" + e8.getMessage() + "\nSee log file for more detail.");
        }
    }

    public static boolean a(File file) {
        if (!file.isDirectory() || C1011s.a(file)) {
            return false;
        }
        if (new File(file, f2348h + "project.properties").exists()) {
            return true;
        }
        return new File(file, f2348h + "project.properties.bkup").exists();
    }

    public a a(String str) {
        return b(new File(str));
    }

    /* JADX WARN: Removed duplicated region for block: B:14:0x003c  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private boolean d(java.io.File r7) {
        /*
            r6 = this;
            r0 = r7
            boolean r0 = r0.exists()
            if (r0 == 0) goto L12
            r0 = r7
            long r0 = r0.length()
            r1 = 100
            int r0 = (r0 > r1 ? 1 : (r0 == r1 ? 0 : -1))
            if (r0 >= 0) goto L14
        L12:
            r0 = 0
            return r0
        L14:
            r0 = 0
            r8 = r0
            java.io.BufferedReader r0 = new java.io.BufferedReader     // Catch: java.io.FileNotFoundException -> L4f java.io.IOException -> L63 java.lang.Throwable -> L77
            r1 = r0
            java.io.FileReader r2 = new java.io.FileReader     // Catch: java.io.FileNotFoundException -> L4f java.io.IOException -> L63 java.lang.Throwable -> L77
            r3 = r2
            r4 = r7
            r3.<init>(r4)     // Catch: java.io.FileNotFoundException -> L4f java.io.IOException -> L63 java.lang.Throwable -> L77
            r1.<init>(r2)     // Catch: java.io.FileNotFoundException -> L4f java.io.IOException -> L63 java.lang.Throwable -> L77
            r8 = r0
            r0 = r8
            java.lang.String r0 = r0.readLine()     // Catch: java.io.FileNotFoundException -> L4f java.io.IOException -> L63 java.lang.Throwable -> L77
            r9 = r0
            r0 = r9
            if (r0 == 0) goto L3c
            r0 = r9
            java.lang.String r1 = "#Project"
            boolean r0 = r0.startsWith(r1)     // Catch: java.io.FileNotFoundException -> L4f java.io.IOException -> L63 java.lang.Throwable -> L77
            if (r0 == 0) goto L3c
            r0 = 1
            goto L3d
        L3c:
            r0 = 0
        L3d:
            r10 = r0
            r0 = r8
            if (r0 == 0) goto L4c
            r0 = r8
            r0.close()     // Catch: java.io.IOException -> L4a
            goto L4c
        L4a:
            r11 = move-exception
        L4c:
            r0 = r10
            return r0
        L4f:
            r9 = move-exception
            r0 = 0
            r10 = r0
            r0 = r8
            if (r0 == 0) goto L60
            r0 = r8
            r0.close()     // Catch: java.io.IOException -> L5e
            goto L60
        L5e:
            r11 = move-exception
        L60:
            r0 = r10
            return r0
        L63:
            r9 = move-exception
            r0 = 0
            r10 = r0
            r0 = r8
            if (r0 == 0) goto L74
            r0 = r8
            r0.close()     // Catch: java.io.IOException -> L72
            goto L74
        L72:
            r11 = move-exception
        L74:
            r0 = r10
            return r0
        L77:
            r12 = move-exception
            r0 = r8
            if (r0 == 0) goto L86
            r0 = r8
            r0.close()     // Catch: java.io.IOException -> L84
            goto L86
        L84:
            r13 = move-exception
        L86:
            r0 = r12
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: aE.a.d(java.io.File):boolean");
    }

    public a b(File file) {
        return a(file, false);
    }

    /* JADX WARN: Removed duplicated region for block: B:11:0x004f A[Catch: Exception -> 0x00a9, TryCatch #2 {Exception -> 0x00a9, blocks: (B:9:0x0046, B:15:0x008b, B:16:0x009c, B:11:0x004f, B:13:0x007b, B:14:0x0086), top: B:66:0x0046 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public aE.a a(java.io.File r7, boolean r8) throws V.a {
        /*
            Method dump skipped, instructions count: 459
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: aE.a.a(java.io.File, boolean):aE.a");
    }

    public void a(int i2) {
        setProperty("canId", "" + i2);
    }

    public int c() {
        try {
            return Integer.parseInt(getProperty("canId"));
        } catch (NumberFormatException e2) {
            return 0;
        }
    }

    public int b(int i2) {
        try {
            return Integer.parseInt(getProperty("canId"));
        } catch (NumberFormatException e2) {
            a(i2);
            return i2;
        }
    }

    public String d() {
        String property = getProperty("protocolEnvelope");
        return property == null ? "" : property;
    }

    public void b(String str) {
        setProperty("protocolEnvelope", str);
    }

    private void Y() {
        for (int i2 = 0; i2 < this.f2359s.size(); i2++) {
            d dVar = (d) this.f2359s.get(i2);
            String str = "CAN_Device_" + i2 + ".";
            setProperty(str + "deviceName", dVar.a());
            setProperty(str + "deviceDescription", dVar.b());
            setProperty(str + "ecuConfigFile", dVar.c());
            setProperty(str + "canId", "" + dVar.e());
            setProperty(str + "runtimeActive", "" + (!dVar.f()));
            b(str + "ecuSettings", dVar.d());
        }
    }

    private void Z() {
        z("CAN_Device_");
    }

    private void z(String str) {
        Iterator<Object> it = keySet().iterator();
        while (it.hasNext()) {
            if (((String) it.next()).startsWith(str)) {
                it.remove();
            }
        }
        Iterator<Object> it2 = this.f2362v.keySet().iterator();
        while (it2.hasNext()) {
            if (((String) it2.next()).startsWith(str)) {
                it2.remove();
            }
        }
    }

    private void aa() {
        int i2 = 0;
        String str = "CAN_Device_0.";
        String str2 = str + "deviceName";
        while (getProperty(str2) != null) {
            d dVar = new d();
            dVar.a(getProperty(str + "deviceName"));
            dVar.b(getProperty(str + "deviceDescription"));
            dVar.c(getProperty(str + "ecuConfigFile"));
            dVar.a(A(str + "ecuSettings"));
            try {
                dVar.a(Integer.parseInt(getProperty(str + "canId")));
            } catch (NumberFormatException e2) {
            }
            String property = getProperty(str + "runtimeActive");
            dVar.a(property == null || property.equals("false"));
            this.f2359s.add(dVar);
            i2++;
            str = "CAN_Device_" + i2 + ".";
            str2 = str + "deviceName";
        }
    }

    public void e() {
        int i2 = 0;
        String str = "userDash0.";
        String str2 = str + "DashName";
        while (f(true).getProperty(str2) != null) {
            f fVar = new f();
            fVar.a(f(true).getProperty(str + "DashName"));
            fVar.b(f(true).getProperty(str + "FileName"));
            this.f2360t.add(fVar);
            i2++;
            str = "userDash" + i2 + ".";
            str2 = str + "DashName";
        }
    }

    private void ab() {
        for (int i2 = 0; i2 < this.f2360t.size(); i2++) {
            f fVar = (f) this.f2360t.get(i2);
            String str = "userDash" + i2 + ".";
            f(false).setProperty(str + "DashName", fVar.a());
            f(false).setProperty(str + "FileName", fVar.b());
        }
    }

    private Properties f(boolean z2) {
        if (!U()) {
            return this;
        }
        if (z2 && this.f2362v.isEmpty()) {
            try {
                File fileAc = ac();
                if (fileAc.exists()) {
                    this.f2362v.load(new FileInputStream(fileAc));
                }
            } catch (Exception e2) {
                C.a("Error occured trying to open common dash properties.\n" + ((Object) ac()) + "\nError Message: " + e2.getMessage() + "\nCheck Log for details.");
            }
        }
        return this.f2362v;
    }

    private File ac() {
        return new File(m(), "dashboards.properties");
    }

    public File f() {
        return new File(t() + File.separator + f2348h, "project.properties");
    }

    public File g() {
        return new File(t() + File.separator + f2348h, "persistedChannelValues.properties");
    }

    public File h() {
        return new File(t() + File.separator + f2348h, "vehicle.properties");
    }

    public File c(String str) {
        return str.equals(u()) ? new File(t() + File.separator, N()) : new File(t() + File.separator, str + "_" + N());
    }

    public File d(String str) {
        return str.equals(u()) ? new File(t() + File.separator, f2348h + M()) : new File(t() + File.separator, f2348h + str + "_" + M());
    }

    public g i() {
        if (this.f2358r != null) {
            return this.f2358r;
        }
        this.f2358r = new g(f2341a.a());
        File fileH = h();
        if (fileH.exists()) {
            this.f2358r.b(fileH);
        }
        return this.f2358r;
    }

    public File j() {
        return new File(v());
    }

    public String k() {
        String property = getProperty("lastSdLogFileName", "");
        if (property == null || property.equals("")) {
            property = q();
        }
        return property;
    }

    public void e(String str) {
        setProperty("lastSdLogFileName", str);
    }

    public File f(String str) {
        if (str.equals(u())) {
            return j();
        }
        d dVarT = t(str);
        if (dVarT != null) {
            return new File(t() + f2348h, dVarT.c());
        }
        return null;
    }

    public File l() {
        return U() ? new File(T(), f2352l + File.separator + "dashboard.dash") : new File(t() + File.separator + f2352l + File.separator + "dashboard.dash");
    }

    public String m() {
        return U() ? T().getAbsolutePath() + File.separator + f2352l : t() + File.separator + f2352l;
    }

    public String n() {
        return File.separator + f2352l + File.separator + "dashboard.dash";
    }

    public File o() {
        new File(y());
        return new File(m() + File.separator + "dashboard.dash");
    }

    public String p() {
        return t() + File.separator + f2351k;
    }

    public String q() {
        return t() + File.separator + f2350j;
    }

    public void a(boolean z2) {
        setProperty("verifyOnBurn", "" + z2);
    }

    public boolean r() {
        return getProperty("verifyOnBurn", "").equals("true");
    }

    public String s() {
        return t();
    }

    public void g(String str) {
        setProperty("tuneFileDir", str);
    }

    public String t() {
        return this.f2357q;
    }

    public void h(String str) {
        if (!str.endsWith("/") && !str.endsWith(PdfOps.DOUBLE_QUOTE__TOKEN)) {
            str = str + File.separator;
        }
        this.f2357q = str;
    }

    public String u() {
        return getProperty("projectName");
    }

    public void i(String str) {
        setProperty("projectName", str);
    }

    public void j(String str) {
        setProperty("logFileDir", str);
    }

    public String v() {
        return getProperty("ecuConfigFile") != null ? t() + f2348h + getProperty("ecuConfigFile") : t() + f2348h + "mainController.ini";
    }

    public String k(String str) {
        return str.equals(u()) ? x() : t() + f2348h + str + "_custom.ini";
    }

    public File w() {
        return new File(t(), f2348h);
    }

    public String x() {
        return t() + f2348h + "custom.ini";
    }

    public void l(String str) {
        setProperty("ecuConfigFile", str);
    }

    public String m(String str) {
        return str.equals(u()) ? getProperty("baudRate", getProperty("baudRate")) : getProperty("baudRate", getProperty(str + "_baudRate"));
    }

    public void a(String str, String str2) {
        if (str.equals(u())) {
            setProperty("baudRate", str2);
        } else {
            setProperty(str + "_baudRate", str2);
        }
    }

    public String n(String str) {
        return getProperty("commPort", getProperty(str + "_commPort"));
    }

    public void b(String str, String str2) {
        if (str2 == null) {
            str2 = "";
        }
        if (str.equals(u())) {
            setProperty("commPort", str2);
        } else {
            setProperty(str + "_commPort", str2);
        }
    }

    public String y() {
        return getProperty("dashBoardFile", "dashboard.dash");
    }

    public void o(String str) {
        setProperty("dashBoardFile", W.b(str, t(), ""));
    }

    public boolean a(C0135r[] c0135rArr) {
        String property = getProperty("ecuSettings");
        a("ecuSettings", c0135rArr);
        String property2 = getProperty("ecuSettings");
        return property == null || property2 == null || !property.equals(property2);
    }

    private void a(String str, C0135r[] c0135rArr) {
        String str2 = "";
        if (c0135rArr != null) {
            for (int i2 = 0; i2 < c0135rArr.length; i2++) {
                c0135rArr[i2].a(new b(this, c0135rArr[i2]));
            }
            c0135rArr = (C0135r[]) R.a((Q[]) c0135rArr);
        }
        if (c0135rArr != null) {
            for (int i3 = 0; i3 < c0135rArr.length; i3++) {
                if (!c0135rArr[i3].aJ().equals("DEFAULT")) {
                    str2 = str2 + c0135rArr[i3].aJ() + CallSiteDescriptor.OPERATOR_DELIMITER;
                }
            }
        }
        setProperty(str, str2);
    }

    private void b(String str, String[] strArr) {
        String str2 = "";
        if (strArr != null) {
            for (String str3 : strArr) {
                str2 = str2 + str3 + CallSiteDescriptor.OPERATOR_DELIMITER;
            }
        }
        setProperty(str, str2);
    }

    public String[] z() {
        return A("ecuSettings");
    }

    private String[] A(String str) {
        String property = getProperty(str);
        if (property == null) {
            return null;
        }
        StringTokenizer stringTokenizer = new StringTokenizer(property, CallSiteDescriptor.OPERATOR_DELIMITER);
        String[] strArr = new String[stringTokenizer.countTokens()];
        int i2 = 0;
        while (stringTokenizer.hasMoreTokens()) {
            String strNextToken = stringTokenizer.nextToken();
            if (strNextToken.equals("DEFAULT")) {
                strArr[i2] = "";
            } else {
                strArr[i2] = strNextToken;
            }
            i2++;
        }
        return strArr;
    }

    public static a A() {
        return f2356p;
    }

    public static void a(a aVar) {
        f2356p = aVar;
    }

    public void p(String str) {
        setProperty("projectDescription", str);
    }

    public String B() {
        return getProperty("projectDescription", "");
    }

    public void q(String str) {
        setProperty("recordsPerSec", str);
    }

    public int c(int i2) {
        String property = getProperty("recordsPerSec");
        return (property == null || property.isEmpty()) ? i2 : Integer.parseInt(property);
    }

    public void c(String str, String str2) {
        setProperty("selectedComDriver", str2);
    }

    public String C() {
        return getProperty("selectedComDriver");
    }

    public ArrayList D() {
        return this.f2355o;
    }

    public G.R E() {
        if (this.f2355o.size() > 0) {
            return (G.R) this.f2355o.get(0);
        }
        return null;
    }

    public void a(G.R r2) {
        this.f2355o.add(r2);
    }

    public void a(f fVar) {
        this.f2360t.add(fVar);
    }

    public String r(String str) {
        if (!str.equals(u())) {
            d dVarT = t(str);
            if (dVarT != null) {
                return dVarT.h();
            }
            C.a("Request for Config UID, but this is not a valid device name: " + str);
            return "XXXXX-Invalid-Device-Name-XXXXX";
        }
        String property = getProperty("ecuUid");
        if (this.f2364x) {
            String strD = f.f.d();
            if (strD == null || strD.isEmpty()) {
                strD = f.f.h();
            }
            if (property == null || property.isEmpty() || !property.startsWith(strD)) {
                property = strD + UUID.randomUUID().toString();
                setProperty("ecuUid", property);
            }
        } else if (property == null || property.isEmpty()) {
            property = UUID.randomUUID().toString();
            setProperty("ecuUid", property);
        }
        return property;
    }

    public boolean s(String str) {
        Iterator it = this.f2360t.iterator();
        while (it.hasNext()) {
            if (((f) it.next()).a().equals(str)) {
                it.remove();
                return true;
            }
        }
        return false;
    }

    public Iterator F() {
        return this.f2360t.iterator();
    }

    public int G() {
        return this.f2360t.size();
    }

    public void a(d dVar) {
        this.f2359s.add(dVar);
    }

    public void H() {
        this.f2359s.clear();
    }

    public Iterator I() {
        return this.f2359s.iterator();
    }

    public d t(String str) {
        Iterator itI = I();
        while (itI.hasNext()) {
            d dVar = (d) itI.next();
            if (dVar.a().equals(str)) {
                return dVar;
            }
        }
        return null;
    }

    public static boolean u(String str) {
        return (str.indexOf(new StringBuilder().append(File.separator).append("Temp").append(File.separator).toString()) == -1 && str.indexOf(new StringBuilder().append(File.separator).append("Temp/").toString()) == -1) ? false : true;
    }

    public boolean J() {
        return u(t());
    }

    public File K() {
        return new File(t() + f2349i);
    }

    public File L() {
        return new File(t(), f2350j);
    }

    public static String M() {
        return "pcVariableValues." + f2341a.b();
    }

    public static String N() {
        return "CurrentTune." + f2341a.b();
    }

    public void a(String str, String[] strArr) {
        b("disabledLogFields." + str, strArr);
    }

    public void v(String str) {
        setProperty("selectedDatalogProfiles", str);
    }

    public String O() {
        return getProperty("selectedDatalogProfiles", "");
    }

    public String[] w(String str) {
        String[] strArrA = A("disabledLogFields." + str);
        if (strArrA == null) {
            strArrA = new String[0];
        }
        for (int i2 = 0; i2 < strArrA.length; i2++) {
            if (strArrA[i2] != null) {
                strArrA[i2] = strArrA[i2].trim();
            }
        }
        return R.a(strArrA);
    }

    public void a(String[] strArr) {
        b("datalogProfiles", strArr);
    }

    public String[] P() {
        String[] strArrA = A("datalogProfiles");
        if (strArrA == null) {
            strArrA = new String[]{""};
        }
        return strArrA;
    }

    public void b(boolean z2) {
        setProperty("slaveServerActive", Boolean.toString(z2));
    }

    public boolean Q() {
        return Boolean.parseBoolean(getProperty("slaveServerActive", Boolean.toString(false)));
    }

    public static void c(boolean z2) {
        f2353m = z2;
    }

    public String R() {
        return getProperty("appVersionCheckedForNewerIni", Version.BUILD);
    }

    public void x(String str) {
        setProperty("appVersionCheckedForNewerIni", str);
    }

    public boolean S() {
        boolean z2;
        if (f2361u == null) {
            C.a("Project set to use common TuningViews, but no common root folder set.");
            return false;
        }
        try {
            z2 = Boolean.parseBoolean(getProperty(f2346f, "" + f2347g));
            setProperty(f2346f, "" + z2);
        } catch (Exception e2) {
            setProperty(f2346f, "" + f2347g);
            z2 = f2347g;
        }
        return z2;
    }

    public static File T() {
        return f2361u;
    }

    public static void c(File file) {
        f2361u = file;
    }

    public boolean U() {
        boolean z2;
        String property = getProperty(f2344d, "" + f2345e);
        if (f2361u == null) {
            if (!Boolean.parseBoolean(property)) {
                return false;
            }
            C.a("Project set to use common Dashboards, but no common root folder set.");
            return false;
        }
        try {
            z2 = Boolean.parseBoolean(property);
            setProperty(f2344d, "" + z2);
        } catch (Exception e2) {
            setProperty(f2344d, "" + f2345e);
            z2 = f2345e;
        }
        return z2;
    }

    public String V() {
        String property = super.getProperty("projectUuid");
        if (this.f2364x) {
            String strD = f.f.d();
            if (strD == null || strD.isEmpty()) {
                strD = f.f.h();
            }
            if (property == null || property.isEmpty() || !property.startsWith(strD)) {
                property = strD + UUID.randomUUID().toString();
                setProperty("projectUuid", property);
                try {
                    b();
                } catch (V.a e2) {
                    Logger.getLogger(a.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
                }
            }
        } else if (property == null || property.isEmpty()) {
            property = UUID.randomUUID().toString();
            setProperty("projectUuid", property);
            try {
                b();
            } catch (V.a e3) {
                Logger.getLogger(a.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e3);
            }
        }
        return property;
    }

    public void d(boolean z2) {
        this.f2354n = z2;
    }

    public void e(boolean z2) {
        this.f2364x = z2;
    }

    public void y(String str) {
        setProperty(f2343c, str);
    }

    public String W() {
        return getProperty(f2343c, "");
    }
}
