package r;

import bH.C;
import bH.W;
import com.efiAnalytics.ui.bU;
import com.efiAnalytics.ui.eJ;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import javafx.fxml.FXMLLoader;

/* renamed from: r.a, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:r/a.class */
public class C1798a implements bU {

    /* renamed from: am, reason: collision with root package name */
    public Properties f13331am = null;

    /* renamed from: an, reason: collision with root package name */
    public Properties f13332an = null;
    private boolean dr = false;
    List dp = new ArrayList();
    private boolean ds = false;
    private File dt = null;

    /* renamed from: a, reason: collision with root package name */
    public static String f13267a = "3.2.05";

    /* renamed from: b, reason: collision with root package name */
    public static String f13268b = "TunerStudio";

    /* renamed from: c, reason: collision with root package name */
    public static String f13269c = "MS";

    /* renamed from: d, reason: collision with root package name */
    public static String f13270d = "com.efiAnalytics.proprietary.MegaSquirtDashFilter";

    /* renamed from: e, reason: collision with root package name */
    public static long f13271e = System.currentTimeMillis();

    /* renamed from: f, reason: collision with root package name */
    public static String f13272f = FXMLLoader.CONTROLLER_SUFFIX;

    /* renamed from: g, reason: collision with root package name */
    public static String f13273g = "controller";

    /* renamed from: h, reason: collision with root package name */
    public static String f13274h = "https://www.efianalytics.com/register/viewProduct.jsp?productCode=TunerStudioMS";

    /* renamed from: i, reason: collision with root package name */
    public static String f13275i = "TunerStudioMS.jar";

    /* renamed from: j, reason: collision with root package name */
    public static String f13276j = "activationKey";

    /* renamed from: k, reason: collision with root package name */
    public static String f13277k = "Dash Echo";

    /* renamed from: l, reason: collision with root package name */
    public static String f13278l = "Seat";

    /* renamed from: m, reason: collision with root package name */
    public static String f13279m = "dashEchoActivationKey";

    /* renamed from: n, reason: collision with root package name */
    public static String f13280n = "registeredEdition";

    /* renamed from: o, reason: collision with root package name */
    public static String f13281o = "hardwareId";

    /* renamed from: p, reason: collision with root package name */
    public static String f13282p = "Hardware ID";

    /* renamed from: q, reason: collision with root package name */
    public static String f13283q = "defaultProject";

    /* renamed from: r, reason: collision with root package name */
    public static String f13284r = "uiPattern";

    /* renamed from: s, reason: collision with root package name */
    public static String f13285s = "previousUiPattern";

    /* renamed from: t, reason: collision with root package name */
    public static String f13286t = "tsproj";

    /* renamed from: u, reason: collision with root package name */
    public static String f13287u = "lastProjectArchiveDir";

    /* renamed from: v, reason: collision with root package name */
    public static String f13288v = "register.html";

    /* renamed from: w, reason: collision with root package name */
    public static String f13289w = "registerAppNotConnected.html";

    /* renamed from: x, reason: collision with root package name */
    public static String f13290x = "lastAdIndex";

    /* renamed from: y, reason: collision with root package name */
    public static String f13291y = "completeUpdate";

    /* renamed from: z, reason: collision with root package name */
    public static String f13292z = "userDashDir";

    /* renamed from: A, reason: collision with root package name */
    public static String f13293A = "userWheelDir";

    /* renamed from: B, reason: collision with root package name */
    public static String f13294B = "userTuneView";

    /* renamed from: C, reason: collision with root package name */
    public static String f13295C = "bin";

    /* renamed from: D, reason: collision with root package name */
    public static String f13296D = "http://www.efianalytics.com/MegaLogViewerHD/download/";

    /* renamed from: E, reason: collision with root package name */
    public static String f13297E = "lastFirmwareDir";

    /* renamed from: F, reason: collision with root package name */
    public static String f13298F = "forceOpenGL";

    /* renamed from: G, reason: collision with root package name */
    public static boolean f13299G = false;

    /* renamed from: H, reason: collision with root package name */
    public static String f13300H = "disableD3d";

    /* renamed from: I, reason: collision with root package name */
    public static boolean f13301I = true;

    /* renamed from: J, reason: collision with root package name */
    public static String f13302J = "disableRegistration";

    /* renamed from: K, reason: collision with root package name */
    public static String f13303K = "promptOnNoControllerFound";

    /* renamed from: L, reason: collision with root package name */
    public static boolean f13304L = true;

    /* renamed from: M, reason: collision with root package name */
    public static String f13305M = "https://www.efianalytics.com/register/browseProducts.jsp?ecuFamily=MegaSquirt&productCategory=Upgrades";

    /* renamed from: N, reason: collision with root package name */
    public static String f13306N = "https://www.efianalytics.com/register/resendRegistrationEmail.jsp";

    /* renamed from: O, reason: collision with root package name */
    public static String f13307O = "lastImportDir";

    /* renamed from: P, reason: collision with root package name */
    public static String f13308P = "publishDataLogValues";

    /* renamed from: Q, reason: collision with root package name */
    public static boolean f13309Q = true;

    /* renamed from: R, reason: collision with root package name */
    public static String f13310R = "datalogAudioDeviceName";

    /* renamed from: S, reason: collision with root package name */
    public static String f13311S = "datalogAudioEnabled";

    /* renamed from: T, reason: collision with root package name */
    public static boolean f13312T = false;

    /* renamed from: U, reason: collision with root package name */
    public static String f13313U = "projectForSerial_";

    /* renamed from: V, reason: collision with root package name */
    public static String f13314V = "firmwareFileExtensions";

    /* renamed from: W, reason: collision with root package name */
    public static String f13315W = "queuedIniSignature_";

    /* renamed from: X, reason: collision with root package name */
    public static String f13316X = "queuedIniInfo_";

    /* renamed from: Y, reason: collision with root package name */
    public static String f13317Y = "configServerPort";

    /* renamed from: Z, reason: collision with root package name */
    public static String f13318Z = "configServerPassword";

    /* renamed from: aa, reason: collision with root package name */
    public static String f13319aa = "preventSleep";

    /* renamed from: ab, reason: collision with root package name */
    public static boolean f13320ab = false;

    /* renamed from: ac, reason: collision with root package name */
    public static String f13321ac = "slaveServerEnabled";

    /* renamed from: ad, reason: collision with root package name */
    public static String f13322ad = "downloadIniWithoutAsking";

    /* renamed from: ae, reason: collision with root package name */
    public static String f13323ae = "saveTuneToDataLog";

    /* renamed from: af, reason: collision with root package name */
    public static boolean f13324af = true;

    /* renamed from: ag, reason: collision with root package name */
    public static String f13325ag = "userPasswordTimeoutPeriod";

    /* renamed from: ah, reason: collision with root package name */
    public static String f13326ah = "10";

    /* renamed from: ai, reason: collision with root package name */
    public static String f13327ai = "yAxisBarometricPressure";

    /* renamed from: aj, reason: collision with root package name */
    public static int f13328aj = 2024;

    /* renamed from: ak, reason: collision with root package name */
    public static String f13329ak = "onlySubscibeActiveDash";

    /* renamed from: al, reason: collision with root package name */
    public static String f13330al = "maskIndicatorImages";

    /* renamed from: ao, reason: collision with root package name */
    public static Properties f13333ao = null;

    /* renamed from: ap, reason: collision with root package name */
    public static String f13334ap = null;

    /* renamed from: aq, reason: collision with root package name */
    public static String f13335aq = null;

    /* renamed from: ar, reason: collision with root package name */
    public static String f13336ar = "TS Dash";

    /* renamed from: as, reason: collision with root package name */
    public static String f13337as = "BigComm";

    /* renamed from: at, reason: collision with root package name */
    public static String f13338at = "BigComm Gen4";

    /* renamed from: au, reason: collision with root package name */
    public static String f13339au = "Grass Roots";

    /* renamed from: av, reason: collision with root package name */
    public static String f13340av = "Tune Monster";

    /* renamed from: aw, reason: collision with root package name */
    public static String f13341aw = "Fuel Monster";

    /* renamed from: ax, reason: collision with root package name */
    public static String f13342ax = "baudRate";

    /* renamed from: ay, reason: collision with root package name */
    public static String f13343ay = "miniTermBaudRate";

    /* renamed from: az, reason: collision with root package name */
    public static String f13344az = "miniTermcommPort";

    /* renamed from: aA, reason: collision with root package name */
    public static String f13345aA = "miniTermOutputFormat";

    /* renamed from: aB, reason: collision with root package name */
    public static String f13346aB = "commLoggingActive";

    /* renamed from: aC, reason: collision with root package name */
    public static String f13347aC = "false";

    /* renamed from: aD, reason: collision with root package name */
    public static String f13348aD = "numOfCommDebugToCache";

    /* renamed from: aE, reason: collision with root package name */
    public static String f13349aE = "protocolInitializer";

    /* renamed from: aF, reason: collision with root package name */
    public static String f13350aF = "dashTargetFpsWindowed";

    /* renamed from: aG, reason: collision with root package name */
    public static String f13351aG = "dashMaxCpuWindowed";

    /* renamed from: aH, reason: collision with root package name */
    public static String f13352aH = "fieldFontSize";

    /* renamed from: aI, reason: collision with root package name */
    public static String f13353aI = "defaultFontSizeAdjustment";

    /* renamed from: aJ, reason: collision with root package name */
    public static String f13354aJ = "lastFileDir";

    /* renamed from: aK, reason: collision with root package name */
    public static String f13355aK = "delimiter";

    /* renamed from: aL, reason: collision with root package name */
    public static String f13356aL = "fileExtensions";

    /* renamed from: aM, reason: collision with root package name */
    public static String f13357aM = "lastJpegDir";

    /* renamed from: aN, reason: collision with root package name */
    public static String f13358aN = "uid";

    /* renamed from: aO, reason: collision with root package name */
    public static String f13359aO = "installDate";

    /* renamed from: aP, reason: collision with root package name */
    public static String f13360aP = "version";

    /* renamed from: aQ, reason: collision with root package name */
    public static String f13361aQ = "automaticUpdates";

    /* renamed from: aR, reason: collision with root package name */
    public static String f13362aR = "lastUpdateCheckDate";

    /* renamed from: aS, reason: collision with root package name */
    public static String f13363aS = "lastDate";

    /* renamed from: aT, reason: collision with root package name */
    public static String f13364aT = "loopCount";

    /* renamed from: aU, reason: collision with root package name */
    public static String f13365aU = "tableEditorDisplayFormat";

    /* renamed from: aV, reason: collision with root package name */
    public static String f13366aV = "projectsDir";

    /* renamed from: aW, reason: collision with root package name */
    public static String f13367aW = "appDebugDir";

    /* renamed from: aX, reason: collision with root package name */
    public static String f13368aX = "dashSplashImage";

    /* renamed from: aY, reason: collision with root package name */
    public static String f13369aY = "gpsEnabled";

    /* renamed from: aZ, reason: collision with root package name */
    public static String f13370aZ = "pcShutDownCommand";

    /* renamed from: ba, reason: collision with root package name */
    public static String f13371ba = "Multi Interface MegaSquirt Driver";

    /* renamed from: bb, reason: collision with root package name */
    public static String f13372bb = "updStreamIpAddress";

    /* renamed from: bc, reason: collision with root package name */
    public static String f13373bc = "promptWithConfigurationWarnings";

    /* renamed from: bd, reason: collision with root package name */
    public static boolean f13374bd = false;

    /* renamed from: be, reason: collision with root package name */
    public static boolean f13375be = true;

    /* renamed from: bf, reason: collision with root package name */
    public static String f13376bf = "loadLastProjectOnStart2";

    /* renamed from: bg, reason: collision with root package name */
    public static String f13377bg = "openProjectFullScreenDash";

    /* renamed from: bh, reason: collision with root package name */
    public static String f13378bh = "menuVisibleWhenDisabled";

    /* renamed from: bi, reason: collision with root package name */
    public static boolean f13379bi = true;

    /* renamed from: bj, reason: collision with root package name */
    public static String f13380bj = "promptWhenTuneChangedExternally";

    /* renamed from: bk, reason: collision with root package name */
    public static boolean f13381bk = true;

    /* renamed from: bl, reason: collision with root package name */
    public static String f13382bl = "showTooltipEnableConditions";

    /* renamed from: bm, reason: collision with root package name */
    public static boolean f13383bm = false;

    /* renamed from: bn, reason: collision with root package name */
    public static String f13384bn = "modalDialogs";

    /* renamed from: bo, reason: collision with root package name */
    public static String f13385bo = "gaugeFloatDown";

    /* renamed from: bp, reason: collision with root package name */
    public static String f13386bp = "runInLiteMode";

    /* renamed from: bq, reason: collision with root package name */
    public static String f13387bq = "showDashOnlyMenu";

    /* renamed from: br, reason: collision with root package name */
    public static boolean f13388br = false;

    /* renamed from: bs, reason: collision with root package name */
    public static String f13389bs = "runInDashOnlyMode";

    /* renamed from: bt, reason: collision with root package name */
    public static boolean f13390bt = false;

    /* renamed from: bu, reason: collision with root package name */
    public static String f13391bu = "performDiffOnConnect";

    /* renamed from: bv, reason: collision with root package name */
    public static boolean f13392bv = true;

    /* renamed from: bw, reason: collision with root package name */
    public static String f13393bw = "autoSaveOfflineTune";

    /* renamed from: bx, reason: collision with root package name */
    public static String f13394bx = "alwaysPromptSaveTune";

    /* renamed from: by, reason: collision with root package name */
    public static boolean f13395by = false;

    /* renamed from: bz, reason: collision with root package name */
    public static String f13396bz = "saveRestorePointOnProjectClose";

    /* renamed from: bA, reason: collision with root package name */
    public static boolean f13397bA = true;

    /* renamed from: bB, reason: collision with root package name */
    public static String f13398bB = "saveRestorePointOnLoad";

    /* renamed from: bC, reason: collision with root package name */
    public static boolean f13399bC = true;

    /* renamed from: bD, reason: collision with root package name */
    public static String f13400bD = "saveRestorePointOnConnect";

    /* renamed from: bE, reason: collision with root package name */
    public static boolean f13401bE = true;

    /* renamed from: bF, reason: collision with root package name */
    public static String f13402bF = "saveRestorePointOnLoad";

    /* renamed from: bG, reason: collision with root package name */
    public static boolean f13403bG = true;

    /* renamed from: bH, reason: collision with root package name */
    public static String f13404bH = "maxRestorePointSpace";

    /* renamed from: bI, reason: collision with root package name */
    public static int f13405bI = 10;

    /* renamed from: bJ, reason: collision with root package name */
    public static String f13406bJ = "skipRestorePointWhenNoChange";

    /* renamed from: bK, reason: collision with root package name */
    public static boolean f13407bK = true;

    /* renamed from: bL, reason: collision with root package name */
    public static String f13408bL = "showVeAnalyzeGauges";

    /* renamed from: bM, reason: collision with root package name */
    public static boolean f13409bM = true;

    /* renamed from: bN, reason: collision with root package name */
    public static String f13410bN = "showVeAnalyzeLiveGraph";

    /* renamed from: bO, reason: collision with root package name */
    public static boolean f13411bO = true;

    /* renamed from: bP, reason: collision with root package name */
    public static String f13412bP = "alwaysAllowMultipleInstances";

    /* renamed from: bQ, reason: collision with root package name */
    public static boolean f13413bQ = false;

    /* renamed from: bR, reason: collision with root package name */
    public static String f13414bR = "boldTableColoring";

    /* renamed from: bS, reason: collision with root package name */
    public static boolean f13415bS = false;

    /* renamed from: bT, reason: collision with root package name */
    public static String f13416bT = "alwaysLaunchNewMlv";

    /* renamed from: bU, reason: collision with root package name */
    public static boolean f13417bU = false;

    /* renamed from: bV, reason: collision with root package name */
    public static String f13418bV = "blackTableBackgrounds";

    /* renamed from: bW, reason: collision with root package name */
    public static boolean f13419bW = false;

    /* renamed from: bX, reason: collision with root package name */
    public static String f13420bX = "dataLogNamingOnStop";
    public static String bY = "dataLogNamingOnStart";
    public static String bZ = "dataLogNamingAuto";
    public static String ca = "sdRenameOnDownload";
    public static boolean cb = false;
    public static String cc = "showPsiOnYAxis";
    public static boolean cd = false;
    public static String ce = "dataLogNamingMode";
    public static String cf = bY;
    public static String cg = "mouseWheelTableActions";
    public static boolean ch = true;
    public static String ci = "showDialogHelpPane";
    public static boolean cj = true;
    public static String ck = "dashShowFPS";
    public static String cl = "autoBurnOnCloseDialog";
    public static String cm = "autoBurnOnPageChange";
    public static String cn = "selectedEdition";
    public static String co = "dash";
    public static String cp = "tuneView";
    public static String cq = "gauge";
    public static String cr = "mlg";
    public static String cs = "msl";
    public static String ct = "csv";
    public static String cu = "frd";
    public static String cv = "ms3";
    public static String cw = "msq";
    public static String cx = "validTuneFileExtensions";
    public static String cy = "part";
    public static String cz = cw + cy;
    public static String cA = "dataLogFormat";
    public static String cB = cr;
    public static String cC = "firstName";
    public static String cD = "lastName";
    public static String cE = "userEmail";
    public static String cF = "registrationKey";
    public static String cG = "dashEchoRegistrationKey";
    public static String cH = "hubRegistrationKey";
    public static String cI = "fallbackRegistrationKey";
    public static String cJ = "registrationUrl";
    public static String cK = "valid";
    public static String cL = "quadraticInterpolation";
    public static String cM = "immutableInterpolation";
    public static String cN = "triedBadKeys";
    public static String cO = "lastConnectedFirmwareSignature";
    private static C1798a dq = null;
    public static boolean cP = false;
    public static String cQ = "hideGaugeDesignerMode";
    public static boolean cR = false;
    public static String cS = "hideMenuLookAndFeel";
    public static boolean cT = false;
    public static String cU = "hideMenuOptions";
    public static boolean cV = false;
    public static String cW = "hideDevTools";
    public static boolean cX = false;
    public static String cY = "hideFileImport";
    public static boolean cZ = false;
    public static String da = "hideGaugeClusterRightClick";
    public static boolean db = false;
    public static String dc = "hideMiniTerm";
    public static boolean dd = false;

    /* renamed from: de, reason: collision with root package name */
    public static String f13421de = "hideCommLogging";
    public static boolean df = false;
    public static String dg = "hideHelpMenu";
    public static boolean dh = false;
    public static String di = "veAnalyzeRunCount";
    public static String dj = "userReviewActive";
    public static String dk = "userReviewPrompted";
    public static String dl = "defaultTuneMenuStyle";
    public static String dm = "lastPluginDir";
    public static String dn = "logFileTransformMenuText";

    /* renamed from: do, reason: not valid java name */
    public static String[] f0do = {C1806i.f13444f, C1806i.f13445g, C1806i.f13446h};

    protected C1798a() {
    }

    public static C1798a a() {
        if (dq == null) {
            dq = new C1798a();
        }
        return dq;
    }

    public void a(String str) {
        this.dp.add(str);
    }

    public boolean b(String str) {
        return this.dp.contains(str);
    }

    public String b() {
        return f13268b + " " + f13269c;
    }

    public Properties c() {
        if (this.f13331am == null) {
            this.f13331am = new Properties();
            String strB = f13268b + ".properties";
            try {
                strB = W.b(strB, " ", "");
                this.f13331am.load(new FileInputStream(strB));
            } catch (FileNotFoundException e2) {
                C.b("File Not Found: " + strB);
            } catch (Exception e3) {
                System.out.println("ERROR loading " + strB);
                e3.printStackTrace();
                System.out.println("Looking in: " + new File(".").getAbsolutePath());
            }
        }
        return this.f13331am;
    }

    public String a(String str, String str2) {
        String strC = c(str);
        return (strC == null || strC.equals("")) ? str2 : strC;
    }

    public boolean a(String str, boolean z2) {
        String strA = a(str, "" + z2);
        return strA != null && strA.equals("true");
    }

    public String c(String str) {
        String strC = c(str, (String) null);
        if (strC == null || strC.equals("")) {
            strC = d(str);
        }
        return strC;
    }

    public String d(String str) {
        return c().getProperty(str);
    }

    public boolean b(String str, boolean z2) {
        String property = c().getProperty(str);
        return property != null ? Boolean.parseBoolean(property) : z2;
    }

    @Override // com.efiAnalytics.ui.bU
    public void b(String str, String str2) {
        if (str2 == null || str2.equals("")) {
            d().remove(str);
        } else {
            d().setProperty(str, str2);
        }
    }

    public void e(String str) {
        d().remove(str);
    }

    public String c(String str, String str2) {
        String property = d().getProperty(str);
        if (property == null || property.trim().equals("")) {
            property = h().getProperty(str);
        }
        if (property == null || property.trim().equals("")) {
            property = d(str);
        }
        if (property == null || property.equals("")) {
            property = str2;
        }
        return property;
    }

    private String t() {
        String str = (f13335aq == null || f13335aq.isEmpty()) ? W.a(f13268b).toLowerCase() + "User.properties" : f13335aq;
        return !a("storePropertiesLocal", "false").equals("true") ? new File(System.getProperty("user.home") + File.separator + ".efiAnalytics" + File.separator + str).getAbsolutePath() : str;
    }

    private String u() {
        String str = W.a(f13268b).toLowerCase() + "User.properties";
        return !a("storePropertiesLocal", "false").equals("true") ? new File(System.getProperty("user.home") + File.separator + str).getAbsolutePath() : str;
    }

    public Properties d() {
        if (this.f13332an == null) {
            this.f13332an = new Properties();
            try {
                String strT = t();
                if (!new File(strT).exists()) {
                    strT = u();
                    if (strT != null) {
                        File file = new File(strT);
                        if (file.exists()) {
                            file.deleteOnExit();
                        }
                    }
                }
                this.f13332an.load(new FileInputStream(strT));
            } catch (FileNotFoundException e2) {
                C.c("Unable to load user properties, looking in:\n" + new File(".").getAbsolutePath());
            } catch (Exception e3) {
                C.c("ERROR loading tsUser.properties");
                e3.printStackTrace();
                C.c("Looking in: " + new File(".").getAbsolutePath());
            }
        }
        return this.f13332an;
    }

    public void e() {
        if (d() != null) {
            File file = new File(t());
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            for (int i2 = 0; i2 < 3; i2++) {
                try {
                    d().store(new FileOutputStream(file), "Do not edit this file the Application will maintain this.");
                    System.out.println("Saved user properties successfully");
                    return;
                } catch (Exception e2) {
                    if (i2 >= 2) {
                        System.out.println("Error saving user properties, giving up.:");
                        e2.printStackTrace();
                        throw new V.a("Unable to save user preferences.\nError message:\n" + e2.getMessage());
                    }
                    System.out.println("Error saving user properties, trying again.");
                    try {
                        Thread.sleep(100L);
                    } catch (InterruptedException e3) {
                    }
                }
            }
        }
    }

    public void d(String str, String str2) {
        if (str2 == null || str2.equals("")) {
            h().remove(str);
        } else {
            h().setProperty(str, str2.trim());
        }
    }

    public String f() {
        if (f13334ap == null || f13334ap.isEmpty()) {
            String lowerCase = W.a(f13268b).toLowerCase();
            String strB = W.b(W.b(f13269c, " Lite!", ""), "(Beta)", "");
            if (strB.contains(" ")) {
                strB = strB.substring(0, strB.indexOf(" "));
            }
            f13334ap = lowerCase + strB + ".reg";
        }
        return !a("storePropertiesLocal", "false").equals("true") ? new File(System.getProperty("user.home") + File.separator + ".efiAnalytics" + File.separator + f13334ap).getAbsolutePath() : f13334ap;
    }

    public String g() {
        String str = W.a(f13268b).toLowerCase() + W.b(W.b(f13269c, " Lite!", ""), "(Beta)", "") + ".reg";
        return !a("storePropertiesLocal", "false").equals("true") ? new File(System.getProperty("user.home") + File.separator + str).getAbsolutePath() : str;
    }

    public Properties h() {
        if (f13333ao == null) {
            f13333ao = new Properties();
            try {
                boolean z2 = false;
                String strF = f();
                if (!new File(strF).exists()) {
                    strF = g();
                    if (new File(strF).exists()) {
                        z2 = true;
                    }
                }
                f13333ao.load(new FileInputStream(strF));
                if (z2) {
                    i();
                }
            } catch (Exception e2) {
                System.out.println("Registration file not found. ");
            }
        }
        return f13333ao;
    }

    public void i() {
        File file = new File(f());
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        String absolutePath = file.getAbsolutePath();
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(new File(absolutePath));
            h().store(fileOutputStream, f13268b + " registration info. EFI Analytics.");
            fileOutputStream.flush();
        } catch (Exception e2) {
            System.out.println("Error saving: " + absolutePath);
            e2.printStackTrace();
            throw new V.a("Unable to save registration information to disk.\nIs '" + absolutePath + "' write protected?");
        }
    }

    public int a(String str, int i2) {
        try {
            return Integer.parseInt(c(str));
        } catch (Exception e2) {
            return i2;
        }
    }

    public int b(String str, int i2) {
        try {
            return Integer.parseInt(d(str));
        } catch (Exception e2) {
            return i2;
        }
    }

    public float a(String str, float f2) {
        try {
            return Float.parseFloat(c(str, "" + f2));
        } catch (Exception e2) {
            C.c("Invalid value for " + str);
            return f2;
        }
    }

    public int c(String str, int i2) {
        try {
            return Integer.parseInt(c(str, "" + i2));
        } catch (Exception e2) {
            return i2;
        }
    }

    public long a(String str, long j2) {
        try {
            return Long.parseLong(c(str, "" + j2));
        } catch (Exception e2) {
            return j2;
        }
    }

    public double a(String str, double d2) {
        try {
            return Double.parseDouble(c(str, "" + d2));
        } catch (Exception e2) {
            return d2;
        }
    }

    public boolean c(String str, boolean z2) {
        try {
            return c(str, "" + z2).equals("true");
        } catch (Exception e2) {
            return z2;
        }
    }

    public String[] f(String str) {
        ArrayList arrayList = new ArrayList();
        Iterator<Object> it = d().keySet().iterator();
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

    public boolean j() {
        return Boolean.parseBoolean(a("debugCommTimings", "false"));
    }

    public String k() {
        return a().b() + " v" + f13267a + "\nBy\nEFI Analytics\n \nSimplifying EFI\n \nPerformance, Drivability & Efficiency Simplified! \nTuning, dashboard and higher level analytics \nMulti-Platform Windows, Linux, OS X\nJava Runtime version: " + System.getProperty("java.version") + " " + System.getProperty("os.arch") + "\nProcessor Score: " + a().a(f13364aT, "") + "\n \nhttp://www.EFIAnalytics.com/ \nsupport@efianalytics.com copyright 2007-" + Calendar.getInstance().get(1) + "\n";
    }

    @Override // com.efiAnalytics.ui.bU
    public String l() {
        return f13268b;
    }

    @Override // com.efiAnalytics.ui.bU
    public String m() {
        return f13269c;
    }

    @Override // com.efiAnalytics.ui.bU
    public String n() {
        return f13354aJ;
    }

    public int o() {
        int i2 = 0;
        try {
            i2 = Integer.parseInt(a(f13353aI, "0"));
        } catch (Exception e2) {
            C.a("Invalid defaultFontSizeAdjustment in properties, value must be an integer");
        }
        return eJ.a() + i2;
    }

    public int p() {
        int i2 = 0;
        try {
            i2 = Integer.parseInt(a(f13353aI, "0"));
        } catch (Exception e2) {
            C.a("Invalid defaultFontSizeAdjustment in properties, value must be an integer");
        }
        return i2;
    }

    public void a(boolean z2) {
        b(z2);
    }

    public boolean q() {
        return this.ds;
    }

    public void b(boolean z2) {
        this.ds = z2;
    }

    public void a(File file) {
        this.dt = file;
    }

    public File r() {
        return this.dt;
    }

    public boolean s() {
        return this.dr;
    }

    public void c(boolean z2) {
        this.dr = z2;
    }
}
