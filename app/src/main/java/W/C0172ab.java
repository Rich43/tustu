package W;

import G.AbstractC0093bz;
import G.C0043ac;
import G.C0047ag;
import G.C0048ah;
import G.C0050aj;
import G.C0051ak;
import G.C0052al;
import G.C0053am;
import G.C0068ba;
import G.C0069bb;
import G.C0070bc;
import G.C0072be;
import G.C0073bf;
import G.C0076bi;
import G.C0077bj;
import G.C0078bk;
import G.C0079bl;
import G.C0080bm;
import G.C0081bn;
import G.C0083bp;
import G.C0084bq;
import G.C0085br;
import G.C0086bs;
import G.C0087bt;
import G.C0088bu;
import G.C0089bv;
import G.C0090bw;
import G.C0091bx;
import G.C0092by;
import G.C0094c;
import G.C0095ca;
import G.C0096cb;
import G.C0097cc;
import G.C0098cd;
import G.C0100cf;
import G.C0101cg;
import G.C0105ck;
import G.C0113cs;
import G.C0117cw;
import G.C0121d;
import G.C0125h;
import G.C0126i;
import G.C0128k;
import G.C0141x;
import G.C0142y;
import G.aH;
import G.aI;
import G.aK;
import G.aM;
import G.aQ;
import G.aS;
import G.aW;
import G.aX;
import G.bA;
import G.bB;
import G.bC;
import G.bD;
import G.bF;
import G.bG;
import G.bH;
import G.bI;
import G.bK;
import G.bO;
import G.bQ;
import G.bW;
import G.bZ;
import G.cY;
import G.cZ;
import G.dc;
import G.dd;
import G.dh;
import G.di;
import G.dk;
import G.dn;
import ax.C0918u;
import bH.C1018z;
import com.efiAnalytics.plugin.ecu.ControllerParameter;
import com.sun.org.apache.xerces.internal.impl.xs.SchemaSymbols;
import com.sun.org.apache.xpath.internal.compiler.Keywords;
import com.sun.xml.internal.ws.transport.http.DeploymentDescriptorParser;
import java.awt.BorderLayout;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXMLLoader;
import javax.management.JMX;
import javax.swing.Action;
import jdk.internal.dynalink.CallSiteDescriptor;
import jdk.jfr.Threshold;
import org.apache.commons.math3.geometry.VectorFormat;
import org.icepdf.core.util.PdfOps;
import org.slf4j.Marker;
import sun.security.pkcs11.wrapper.Constants;
import sun.security.x509.X509CertImpl;
import sun.util.locale.LanguageTag;

/* renamed from: W.ab, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:W/ab.class */
public class C0172ab {

    /* renamed from: j, reason: collision with root package name */
    private boolean f2050j = true;

    /* renamed from: d, reason: collision with root package name */
    double f2051d = 3.77d;

    /* renamed from: k, reason: collision with root package name */
    private final K f2052k = new C0174ad(this);

    /* renamed from: l, reason: collision with root package name */
    private boolean f2053l = false;

    /* renamed from: m, reason: collision with root package name */
    private boolean f2054m = false;

    /* renamed from: e, reason: collision with root package name */
    Map f2055e = new HashMap();

    /* renamed from: a, reason: collision with root package name */
    public static File f2047a = new File("./inc");

    /* renamed from: b, reason: collision with root package name */
    public static File f2048b = null;

    /* renamed from: c, reason: collision with root package name */
    public static File f2049c = null;

    /* renamed from: f, reason: collision with root package name */
    public static int f2056f = 1;

    /* renamed from: g, reason: collision with root package name */
    public static int f2057g = 2;

    /* renamed from: h, reason: collision with root package name */
    public static int f2058h = 4;

    /* renamed from: i, reason: collision with root package name */
    public static int f2059i = 8;

    /* renamed from: n, reason: collision with root package name */
    private static C0173ac f2060n = null;

    /* renamed from: o, reason: collision with root package name */
    private static HashMap f2061o = new HashMap();

    public C0172ab() {
        bH.C.d("Supported iniSpecVersion=" + this.f2051d);
    }

    public G.R a(G.R r2, String str) {
        return a(r2, str, true);
    }

    public G.R a(G.R r2, String str, boolean z2) {
        return a(r2, str, z2, f2056f);
    }

    public G.R a(G.R r2, String str, boolean z2, boolean z3) {
        return z3 ? a(r2, str, z2, f2057g) : a(r2, str, z2, f2056f);
    }

    public G.R a(G.R r2, String str, boolean z2, int i2) throws V.g {
        long jCurrentTimeMillis = System.currentTimeMillis();
        bH.Z z3 = new bH.Z();
        z3.a();
        J jA = a(str);
        z3.b();
        String strSubstring = str.indexOf(File.separator) != -1 ? str.substring(str.lastIndexOf(File.separator) + 1) : str;
        bH.C.d("Read file " + strSubstring + ": " + z3.c() + "ms");
        z3.a();
        jA.a(a(jA, r2));
        z3.b();
        bH.C.d("Filtered ini: " + z3.c() + "ms");
        M mA = jA.a("TunerStudio", "iniSpecVersion");
        if (mA != null) {
            double d2 = -1.0d;
            try {
                d2 = Double.parseDouble(mA.e());
            } catch (Exception e2) {
                a(mA, "Invalid iniSpecVersion, a numeric value is expected.");
            }
            if (d2 > this.f2051d) {
                a(mA, "ECU Definition File " + mA.g() + " requires a newer version of this application.\nRequired Specification: " + d2 + "\nSupported Specification: " + this.f2051d + "\n\nPlease upgrade to a newer version.");
                throw new V.g("A newer version of this application is required to load ECU Definition File " + mA.g() + ".", 2);
            }
        }
        M mA2 = jA.a("TunerStudio", "iniVersion");
        if (mA2 != null) {
            try {
                bH.C.d("iniVersion =" + Double.parseDouble(mA2.e()));
            } catch (Exception e3) {
                a(mA, "Invalid iniVersion, a numeric value is expected.");
            }
        } else {
            bH.C.d("No iniVersion defined");
        }
        try {
            if (z2) {
                try {
                    try {
                        if (r2.i() == null || r2.i().equals("")) {
                            M mA3 = jA.a("MegaTune", X509CertImpl.SIGNATURE);
                            M mA4 = jA.a("MegaTune", "signaturePrefix");
                            if (mA3 == null || mA3.d()) {
                                mA3 = jA.a("TunerStudio", X509CertImpl.SIGNATURE);
                            }
                            if (mA4 == null || mA4.d()) {
                                mA4 = jA.a("TunerStudio", "signaturePrefix");
                            }
                            if (mA3 == null || mA3.d()) {
                                a(mA3, "firmware serial signature entry not found in ECU Definition.");
                            }
                            if (mA4 != null && !mA4.d()) {
                                r2.y(bH.W.b(mA4.e().trim(), PdfOps.DOUBLE_QUOTE__TOKEN, ""));
                            }
                            String strB = bH.W.b(mA3.e().trim(), PdfOps.DOUBLE_QUOTE__TOKEN, "");
                            r2.e(strB);
                            bH.C.c("ini signature: " + strB);
                            if (0 != 0) {
                                bH.C.d("[" + strSubstring + "]Loading Controller Commands");
                            }
                            z3.a();
                            s(r2, jA);
                            z3.b();
                            if (0 != 0) {
                                bH.C.d("[" + strSubstring + "]Loaded Controller Commands: " + z3.c() + "ms");
                            }
                            if (0 != 0) {
                                bH.C.d("[" + strSubstring + "]Initializing offline data store");
                            }
                            z3.a();
                            r2.j();
                            z3.b();
                            if (0 != 0) {
                                bH.C.d("[" + strSubstring + "]Initialized offline data store: " + z3.c() + "ms");
                            }
                            r2.h().a(r2.O().e());
                        }
                    } catch (V.g e4) {
                        e4.printStackTrace();
                        throw new V.g("Invalid Ini entry in ", e4);
                    }
                } catch (Exception e5) {
                    e5.printStackTrace();
                    throw new V.g("Invalid Ini row in ", e5);
                }
            }
            if (C1018z.i().b()) {
                z3.a();
                if (0 != 0) {
                    bH.C.d("[" + strSubstring + "]Loading PcVariables");
                }
                n(r2, jA);
                z3.b();
                if (0 != 0) {
                    bH.C.d("[" + strSubstring + "]Loaded PcVariables: " + z3.c() + "ms");
                }
                z3.a();
                if (0 != 0) {
                    bH.C.d("[" + strSubstring + "]Loading Constants");
                }
                m(r2, jA);
                z3.b();
                if (0 != 0) {
                    bH.C.d("[" + strSubstring + "]Loaded Constants: " + z3.c() + "ms");
                }
                z3.a();
                if (0 != 0) {
                    bH.C.d("[" + strSubstring + "]Loading OutputChannels");
                }
                e(r2, jA);
                z3.b();
                if (0 != 0) {
                    bH.C.d("[" + strSubstring + "]Loaded OutputChannels: " + z3.c() + "ms");
                }
                if (!jA.b("Replay").isEmpty()) {
                    z3.a();
                    if (0 != 0) {
                        bH.C.d("[" + strSubstring + "]Loading Replay Channels");
                    }
                    c(r2, jA, "Replay");
                    z3.b();
                    if (0 != 0) {
                        bH.C.d("[" + strSubstring + "]Loaded Replay Channels: " + z3.c() + "ms");
                    }
                }
                if (!jA.b("ExtendedReplay").isEmpty()) {
                    z3.a();
                    if (0 != 0) {
                        bH.C.d("[" + strSubstring + "]Loading ExtendedReplay Channels");
                    }
                    c(r2, jA, "ExtendedReplay");
                    z3.b();
                    if (0 != 0) {
                        bH.C.d("[" + strSubstring + "]Loaded ExtendedReplay Channels: " + z3.c() + "ms");
                    }
                }
            }
            if ((i2 & f2056f) != 0 || (i2 & f2058h) != 0 || (i2 & f2059i) != 0) {
                z3.a();
                if (0 != 0) {
                    bH.C.d("[" + strSubstring + "]Loading Tables");
                }
                if (r2.e() == null) {
                    r2.a(new bD());
                }
                j(r2, jA);
                z3.b();
                if (0 != 0) {
                    bH.C.d("[" + strSubstring + "]Loaded Tables: " + z3.c() + "ms");
                }
            }
            z3.a();
            if (0 != 0) {
                bH.C.d("[" + strSubstring + "]Loading Gauge Definitions");
            }
            f(r2, jA);
            z3.b();
            if (0 != 0) {
                bH.C.d("[" + strSubstring + "]Loaded Gauge Definitions: " + z3.c() + "ms");
            }
            z3.a();
            if (0 != 0) {
                bH.C.d("[" + strSubstring + "]Loading Custom ControllerCommands");
            }
            b(r2, jA, "ControllerCommands");
            z3.b();
            if (0 != 0) {
                bH.C.d("[" + strSubstring + "]Loaded Custom ControllerCommands: " + z3.c() + "ms");
            }
            z3.a();
            if (0 != 0) {
                bH.C.d("[" + strSubstring + "]Loading PortEditor");
            }
            if (r2.e() == null) {
                r2.a(new bD());
            }
            b(r2, r2.e(), jA);
            z3.b();
            if (0 != 0) {
                bH.C.d("[" + strSubstring + "]Loaded PortEditor: " + z3.c() + "ms");
            }
            if ((i2 & f2056f) != 0 || (i2 & f2059i) != 0) {
                z3.a();
                if (0 != 0) {
                    bH.C.d("[" + strSubstring + "]Loading UI Structure");
                }
                d(r2, jA);
                z3.b();
                if (0 != 0) {
                    bH.C.d("[" + strSubstring + "]Loaded UI Structure: " + z3.c() + "ms");
                }
            }
            z3.a();
            if (0 != 0) {
                bH.C.d("[" + strSubstring + "]Loading DataLog Fields");
            }
            i(r2, jA);
            z3.b();
            if (0 != 0) {
                bH.C.d("[" + strSubstring + "]Loaded DataLog Fields: " + z3.c() + "ms");
            }
            z3.a();
            if (0 != 0) {
                bH.C.d("[" + strSubstring + "]Loading Front Page");
            }
            k(r2, jA);
            z3.b();
            if (0 != 0) {
                bH.C.d("[" + strSubstring + "]Loaded Front Page: " + z3.c() + "ms");
            }
            z3.a();
            if (0 != 0) {
                bH.C.d("[" + strSubstring + "]Loading Verbiage Over-rides");
            }
            a(r2, jA);
            z3.b();
            if (0 != 0) {
                bH.C.d("[" + strSubstring + "]Loaded Verbiage Over-rides: " + z3.c() + "ms");
            }
            z3.a();
            if (0 != 0) {
                bH.C.d("[" + strSubstring + "]Loading ConstantsExtensions");
            }
            a(r2, jA, "ConstantsExtensions");
            z3.b();
            if (0 != 0) {
                bH.C.d("[" + strSubstring + "]Loaded ConstantsExtensions: " + z3.c() + "ms");
            }
            z3.a();
            if (0 != 0) {
                bH.C.d("[" + strSubstring + "]Loading [TurboBaud]");
            }
            p(r2, jA);
            z3.b();
            if (0 != 0) {
                bH.C.d("[" + strSubstring + "]Loaded [TurboBaud]: " + z3.c() + "ms");
            }
            if ((i2 & f2056f) != 0) {
                z3.a();
                if (0 != 0) {
                    bH.C.d("[" + strSubstring + "]Loading [EventTriggers]");
                }
                l(r2, jA);
                z3.b();
                if (0 != 0) {
                    bH.C.d("[" + strSubstring + "]Loaded [EventTriggers]: " + z3.c() + "ms");
                }
            }
            b(r2);
            if ((i2 & f2056f) != 0) {
                z3.a();
                if (0 != 0) {
                    bH.C.d("[" + strSubstring + "]Loading VE Analyze Maps");
                }
                b(r2, jA);
                z3.b();
                if (0 != 0) {
                    bH.C.d("[" + strSubstring + "]Loaded VE Analyze Maps: " + z3.c() + "ms");
                }
                z3.a();
                if (0 != 0) {
                    bH.C.d("[" + strSubstring + "]Loading WUE Analyze Maps");
                }
                c(r2, jA);
                z3.b();
                if (0 != 0) {
                    bH.C.d("[" + strSubstring + "]Loaded WUE Analyze Maps: " + z3.c() + "ms");
                }
                z3.a();
                if (0 != 0) {
                    bH.C.d("[" + strSubstring + "]Loading [Tools]");
                }
                q(r2, jA);
                z3.b();
                if (0 != 0) {
                    bH.C.d("[" + strSubstring + "]Loaded [Tools]: " + z3.c() + "ms");
                }
                z3.a();
                if (0 != 0) {
                    bH.C.d("[" + strSubstring + "]Loading ini [LoggerDefinition]");
                }
                o(r2, jA);
                z3.b();
                if (0 != 0) {
                    bH.C.d("Loaded ini [LoggerDefinition]: " + z3.c() + "ms");
                }
                z3.a();
                if (0 != 0) {
                    bH.C.d("[" + strSubstring + "]Validating ValueProvider expressions");
                }
                a(r2);
                z3.b();
                if (0 != 0) {
                    bH.C.d("Validated ValueProvider expressions: " + z3.c() + "ms");
                }
                z3.a();
                if (0 != 0) {
                    bH.C.d("[" + strSubstring + "]Loading ini [SettingContextHelp]");
                }
                r(r2, jA);
                z3.b();
                if (0 != 0) {
                    bH.C.d("[" + strSubstring + "]Loaded ini [SettingContextHelp]: " + z3.c() + "ms");
                }
                z3.a();
                if (0 != 0) {
                    bH.C.d("[" + strSubstring + "]Loading ini [DatalogViews]");
                }
                h(r2, jA);
                z3.b();
                if (0 != 0) {
                    bH.C.d("[" + strSubstring + "]Loaded ini [DatalogViews]: " + z3.c() + "ms");
                }
                z3.a();
                if (0 != 0) {
                    bH.C.d("[" + strSubstring + "]Loading ini [TuningViews]");
                }
                g(r2, jA);
                z3.b();
                if (0 != 0) {
                    bH.C.d("[" + strSubstring + "]Loaded ini [TuningViews]: " + z3.c() + "ms");
                }
                bH.C.d("Loaded All known ini sections");
            } else {
                bH.C.d("Loaded All Read Only ini sections");
            }
            if (this.f2050j) {
                bH.C.d("Parsed and validated ini \"" + strSubstring + "\" in : " + (System.currentTimeMillis() - jCurrentTimeMillis) + "ms.");
            } else {
                bH.C.d("Parsed ini \"" + strSubstring + "\" in : " + (System.currentTimeMillis() - jCurrentTimeMillis) + "ms.");
            }
            return r2;
        } finally {
            this.f2055e.clear();
        }
    }

    public G.R a(G.R r2, String str, String str2) throws V.g {
        bH.Z z2 = new bH.Z();
        z2.a();
        this.f2054m = true;
        J jA = a(str);
        z2.b();
        String strSubstring = str.contains(File.separator) ? str.substring(str.lastIndexOf(File.separator) + 1) : str;
        bH.C.d("Read file " + strSubstring + ": " + z2.c() + "ms");
        z2.a();
        jA.a(a(jA, r2));
        z2.b();
        bH.C.d("Filtered ini: " + z2.c() + "ms");
        String str3 = "";
        M mA = jA.a("TunerStudio", "iniSpecVersion");
        if (mA != null) {
            double d2 = -1.0d;
            try {
                d2 = Double.parseDouble(mA.e());
            } catch (Exception e2) {
                a(mA, "Invalid iniSpecVersion, a numeric value is expected.");
            }
            if (d2 > this.f2051d) {
                a(mA, "ECU Definition File " + mA.g() + " requires a newer version of this application.\nRequired Specification: " + d2 + "\nSupported Specification: " + this.f2051d + "\n\nPlease upgrade to a newer version.");
                throw new V.g("A newer version of this application is required to load ECU Definition File " + mA.g() + ".", 2);
            }
        }
        try {
            try {
                z2.a();
                bH.C.d("[" + strSubstring + "]Loading ini [EncodedData]");
                d(r2, jA, str2);
                str3 = "Loaded ini [EncodedData]";
                z2.b();
                bH.C.d("[" + strSubstring + "]" + str3 + ": " + z2.c() + "ms");
                this.f2055e.clear();
                return r2;
            } catch (Exception e3) {
                e3.printStackTrace();
                throw new V.g("Invalid Ini row in " + str3, e3);
            }
        } catch (Throwable th) {
            this.f2055e.clear();
            throw th;
        }
    }

    private void a(G.R r2) {
        if (!a()) {
            bH.C.d("Validate Expressions disabled.");
            return;
        }
        for (aM aMVar : r2.l()) {
            if (aMVar.E() instanceof bQ) {
                a(r2, (bQ) aMVar.E(), aMVar.aJ(), "scale", true);
            }
            if (aMVar.F() instanceof bQ) {
                a(r2, (bQ) aMVar.F(), aMVar.aJ(), Keywords.FUNC_TRANSLATE_STRING, true);
            }
            if (aMVar.s() instanceof bQ) {
                a(r2, (bQ) aMVar.s(), aMVar.aJ(), "Maximum", true);
            }
            if (aMVar.t() instanceof bQ) {
                a(r2, (bQ) aMVar.t(), aMVar.aJ(), "Minimum", true);
            }
            if (aMVar.v() instanceof bQ) {
                a(r2, (bQ) aMVar.v(), aMVar.aJ(), "Digits", true);
            }
            if (aMVar.p() instanceof C0121d) {
                try {
                    ((C0121d) aMVar.p()).a();
                } catch (Exception e2) {
                    b((M) null, "[Constant] " + aMVar.aJ() + ": Invalid Units Expression.");
                }
            }
        }
    }

    private void b(G.R r2) {
        if (!this.f2050j || r2 == null || r2.O() == null || r2.O().o() == null || r2.O().n() <= r2.O().G(0) || r2.O().N()) {
            return;
        }
        bH.C.b("blockingFactor smaller than ochBlockSize, but ochCommand does not support blocking.");
    }

    private void a(M m2, String str) {
        if (m2 == null) {
            bH.C.a(str);
        } else {
            bH.C.a(str + ", Problem at:\n" + m2.toString() + "\n");
        }
    }

    private void b(M m2, String str) {
        if (m2 == null) {
            bH.C.b(str);
        } else {
            bH.C.b(str + ", Problem at:\n" + m2.toString() + "\n");
        }
    }

    private void c() {
        if (f2060n != null) {
            f2060n.a();
        } else {
            f2060n = new C0173ac(this);
            f2060n.start();
        }
    }

    public J a(String str) throws V.g {
        c();
        if (f2061o.containsKey(str)) {
            return (J) f2061o.get(str);
        }
        J j2 = new J();
        j2.a(this.f2052k);
        if (!this.f2054m) {
            j2.c("EncodedData");
        }
        try {
            j2.a(new File(str));
            try {
                Iterator itC = j2.c();
                while (itC.hasNext()) {
                    a(j2, new File(str).getParentFile(), (String) itC.next());
                }
                return j2;
            } catch (Exception e2) {
                throw new V.g("Error reading include file. \n\n" + e2.getMessage(), e2);
            }
        } catch (Exception e3) {
            throw new V.g("Error reading ini file. " + str + "\n" + e3.getMessage(), e3);
        }
    }

    protected void a(J j2, File file, String str) {
        J j3 = new J();
        j3.a(this.f2052k);
        if (!this.f2054m) {
            j3.c("EncodedData");
        }
        File file2 = new File(file, str);
        if (!file2.exists()) {
            file2 = new File(file.getParent() + "/inc", str);
        }
        if (!file2.exists() && f2048b != null) {
            file2 = new File(f2048b, str);
        }
        if (!file2.exists()) {
            file2 = new File(f2047a, str);
        }
        j3.a(file2);
        Iterator itB = j3.b();
        while (itB.hasNext()) {
            String str2 = (String) itB.next();
            if (j2.b(str2) != null) {
                if (j2.b(str2) == null) {
                    M m2 = new M(str);
                    m2.a("[" + str2 + "]", -1);
                    j2.a().add(m2);
                }
                Iterator it = j3.b(str2).iterator();
                while (it.hasNext()) {
                    j2.a(str2, (M) it.next());
                }
            }
        }
    }

    private boolean c(String str) {
        return str != null && (str.equals("std_injection") || str.equals("std_realtime") || str.equals("std_accel") || str.equals("std_ms3Rtc") || str.equals("std_ms3SdConsole") || str.equals("std_ftpSdBrowser") || str.equals("std_separator") || str.equals("std_ms2gentherm") || str.equals("std_ms2geno2") || str.equals("std_constants") || str.equals("std_warmup") || str.equals("std_port_edit") || str.equals("std_replay_upload") || str.equals("std_bootstrap") || str.equals("std_trigwiz"));
    }

    private void d(G.R r2, J j2) {
        bD bDVar = r2.e() == null ? new bD() : r2.e();
        c(r2, bDVar, j2, "CurveEditor");
        c(r2, bDVar, j2, "CurveEditorTS");
        try {
            a(r2, bDVar, j2);
        } catch (V.g e2) {
            bH.C.a("Error loading EcuReference Tables");
            e2.printStackTrace();
        }
        try {
            bH.C.d("Loading Trigger Wheels");
            b(r2, bDVar, j2, "TriggerWheel");
        } catch (Exception e3) {
            bH.C.a("Error loading [TriggerWheel] section of ini.");
            e3.printStackTrace();
        }
        ArrayList arrayListA = a(j2, r2, "UserDefined");
        if (arrayListA != null && !arrayListA.isEmpty()) {
            bH.C.d("Loading Depricated ini section [UserDefined], use [UiDialogs]");
            a(r2, bDVar, j2, "UserDefined");
        }
        ArrayList arrayListA2 = a(j2, r2, "UserDefinedTS");
        if (arrayListA2 != null && !arrayListA2.isEmpty()) {
            bH.C.d("Loading Depricated ini section [UserDefinedTS], use [UiDialogs]");
            a(r2, bDVar, j2, "UserDefinedTS");
        }
        a(r2, bDVar, j2, "UiDialogs");
        c(r2, bDVar, j2);
        e(r2, bDVar, j2, "Menu");
        e(r2, bDVar, j2, "MenuTS");
        d(r2, bDVar, j2, "KeyActions");
    }

    private void a(M m2) {
        int iB = bH.W.b(m2.b(), PdfOps.DOUBLE_QUOTE__TOKEN);
        if (iB == -1 || iB % 2 == 0) {
            return;
        }
        b(m2, "Open ended String value");
    }

    private void a(G.R r2, bD bDVar, J j2, String str) {
        int i2;
        ArrayList arrayListA = a(j2, r2, str);
        if (arrayListA == null || arrayListA.isEmpty()) {
            return;
        }
        Iterator itN = r2.n();
        while (itN.hasNext()) {
            bDVar.a((C0072be) itN.next());
        }
        Iterator itO = r2.o();
        while (itO.hasNext()) {
            bDVar.a((C0076bi) itO.next());
        }
        C0088bu c0088bu = null;
        C0084bq c0084bq = null;
        C0050aj c0050aj = null;
        C0090bw c0090bw = null;
        C0086bs c0086bs = null;
        C0092by c0092by = null;
        ArrayList arrayList = new ArrayList();
        Iterator it = arrayListA.iterator();
        while (it.hasNext()) {
            M m2 = (M) it.next();
            String strB = m2.b();
            try {
                if (strB.startsWith("dialog")) {
                    c0086bs = null;
                    c0092by = null;
                    c0088bu = new C0088bu();
                    c0088bu.q(this.f2053l);
                    String[] strArrJ = j(q(strB));
                    c0088bu.v(strArrJ[0].trim());
                    if (arrayList.contains(c0088bu.aJ())) {
                        b(m2, "dialog name '" + c0088bu.aJ() + "' already assigned. Menus will reference the last definition");
                    }
                    if (strArrJ.length > 1) {
                        c0088bu.d(cY.a().a(r2, strArrJ[1]));
                    }
                    if (strArrJ.length > 2) {
                        if (strArrJ[2].equals("border")) {
                            c0088bu.i(2);
                        } else if (strArrJ[2].equals("xAxis")) {
                            c0088bu.i(0);
                        } else if (strArrJ[2].equals("yAxis")) {
                            c0088bu.i(1);
                        } else if (strArrJ[2].equals("card")) {
                            c0088bu.i(3);
                        } else if (strArrJ[2].equals("indexCard")) {
                            c0088bu.i(4);
                        } else if (!bH.H.a(strArrJ[2])) {
                            b(m2, "Invalid layout defined for panel.\n Valid values: border, xAxis, yAxis, indexCard. Using default yAxis.");
                            c0088bu.i(1);
                        }
                    }
                    if (strArrJ.length > 3) {
                        c0088bu.y(strArrJ[3]);
                    }
                    bDVar.a(c0088bu);
                } else if (strB.startsWith("panel")) {
                    String[] strArrJ2 = j(q(strB));
                    if (strArrJ2[0].trim().equals(c0088bu.aJ()) || strArrJ2[0].trim().equals("customPressure5v")) {
                        a(m2, "Attempting to add a panel to itself! Ignoring row.");
                    } else {
                        C0088bu c0088buC = bDVar.c(strArrJ2[0].trim());
                        if (c0088buC == null && c(strArrJ2[0].trim())) {
                            c0088buC = new bC();
                            c0088buC.v(strArrJ2[0].trim());
                            bDVar.a(c0088buC);
                        }
                        if (c0088buC == null) {
                            b(m2, "Nested Panel " + strArrJ2[0].trim() + " not found in UI structure. Either it is not defined in this ini file or it is being referenced before it was defined.");
                        } else {
                            C0088bu c0088bu2 = new C0088bu();
                            c0088bu2.h(true);
                            c0088bu2.a(c0088buC);
                            int i3 = 1;
                            if (strArrJ2.length > 1 && (c0088bu.R() == 2 || !strArrJ2[1].startsWith(VectorFormat.DEFAULT_PREFIX))) {
                                try {
                                    i3 = 1 + 1;
                                    String strTrim = strArrJ2[1].trim();
                                    if (c0088bu.R() == 2 && !strTrim.equals(BorderLayout.CENTER) && !strTrim.equals("North") && !strTrim.equals("South") && !strTrim.equals("East") && !strTrim.equals("West")) {
                                        b(m2, "Invalid placement constraint for border layout: " + strTrim + ", Valid border constraints are: North, South, East, West or Center");
                                        strTrim = BorderLayout.CENTER;
                                    }
                                    c0088bu2.t(strTrim);
                                } catch (V.g e2) {
                                    b(m2, e2.getMessage());
                                }
                            }
                            if (strArrJ2.length > i3) {
                                int i4 = i3;
                                i3++;
                                String strTrim2 = bH.W.b(bH.W.b(strArrJ2[i4], VectorFormat.DEFAULT_PREFIX, ""), "}", "").trim();
                                a(r2, m2, strTrim2, false);
                                c0088bu2.u(strTrim2);
                                c0088buC.u(strTrim2);
                            }
                            if (strArrJ2.length > i3) {
                                int i5 = i3;
                                int i6 = i3 + 1;
                                String strTrim3 = bH.W.b(bH.W.b(strArrJ2[i5], VectorFormat.DEFAULT_PREFIX, ""), "}", "").trim();
                                a(r2, m2, strTrim3, false);
                                c0088bu2.x(strTrim3);
                            }
                            c0088bu.a(c0088bu2);
                        }
                    }
                } else if (strB.startsWith("gauge")) {
                    String[] strArrJ3 = j(q(strB));
                    bA bAVar = new bA();
                    bAVar.a(strArrJ3[0].trim());
                    if (r2.k(strArrJ3[0].trim()) == null) {
                        a(m2, "Gauge Template " + strArrJ3[0].trim() + " not found.");
                    } else {
                        if (strArrJ3.length > 1) {
                            try {
                                bAVar.t(strArrJ3[1].trim());
                            } catch (V.g e3) {
                                b(m2, e3.getMessage());
                            }
                        }
                        if (strArrJ3.length > 2) {
                            String strTrim4 = bH.W.b(bH.W.b(strArrJ3[2], VectorFormat.DEFAULT_PREFIX, ""), "}", "").trim();
                            a(r2, m2, strTrim4, false);
                            bAVar.u(strTrim4);
                        }
                        c0088bu.a(bAVar);
                    }
                } else if (strB.startsWith("liveGraph")) {
                    String[] strArrJ4 = j(q(strB));
                    c0084bq = new C0084bq();
                    c0084bq.v(strArrJ4[0]);
                    c0084bq.s(strArrJ4[1]);
                    if (strArrJ4.length > 2) {
                        try {
                            c0084bq.t(strArrJ4[2]);
                        } catch (V.g e4) {
                            b(m2, e4.getMessage());
                        }
                    } else {
                        c0084bq.t("South");
                    }
                    if (strArrJ4.length > 3) {
                        c0084bq.u(strArrJ4[3]);
                        a(r2, m2, c0084bq.aH(), false);
                    }
                    c0088bu.a(c0084bq);
                } else if (strB.startsWith("graphLine")) {
                    String[] strArrJ5 = j(q(strB));
                    C0085br c0085br = new C0085br();
                    int i7 = 0 + 1;
                    c0085br.a(strArrJ5[0]);
                    if (r2.g(c0085br.a()) == null) {
                        b(m2, "LiveGraph graphLine assigned to invalid OutputChannel.");
                    }
                    if (strArrJ5.length > i7) {
                        i7++;
                        c0085br.b(strArrJ5[i7]);
                    }
                    if (strArrJ5.length > i7) {
                        int i8 = i7;
                        i7++;
                        c0085br.a(Double.parseDouble(strArrJ5[i8]));
                    }
                    if (strArrJ5.length > i7) {
                        int i9 = i7;
                        i7++;
                        c0085br.b(Double.parseDouble(strArrJ5[i9]));
                    }
                    if (strArrJ5.length > i7) {
                        int i10 = i7;
                        i7++;
                        if (strArrJ5[i10].equalsIgnoreCase("auto")) {
                            c0085br.a(true);
                        }
                    }
                    if (strArrJ5.length > i7) {
                        int i11 = i7;
                        int i12 = i7 + 1;
                        if (strArrJ5[i11].equalsIgnoreCase("auto")) {
                            c0085br.b(true);
                        }
                    }
                    c0084bq.a(c0085br);
                } else if (strB.startsWith("logFieldSelector")) {
                    String[] strArrJ6 = j(q(strB));
                    C0047ag c0047ag = new C0047ag();
                    int i13 = 0 + 1;
                    c0047ag.v(strArrJ6[0]);
                    if (strArrJ6.length > i13) {
                        i13++;
                        c0047ag.s(bH.W.b(strArrJ6[i13], PdfOps.DOUBLE_QUOTE__TOKEN, ""));
                    }
                    if (strArrJ6.length > i13) {
                        int i14 = i13;
                        i13++;
                        c0047ag.a(strArrJ6[i14]);
                        if (r2.c(c0047ag.a()) == null) {
                            b(m2, "Invalid Offset Parameter Name, " + c0047ag.a() + " not found.");
                        }
                    }
                    if (strArrJ6.length > i13) {
                        int i15 = i13;
                        i13++;
                        c0047ag.b(strArrJ6[i15]);
                        if (r2.c(c0047ag.b()) == null) {
                            b(m2, "Invalid Length Parameter Name, " + c0047ag.b() + " not found.");
                        }
                    }
                    if (strArrJ6.length > i13) {
                        int i16 = i13;
                        i13++;
                        try {
                            c0047ag.a(di.a(r2, bH.W.b(bH.W.b(strArrJ6[i16], VectorFormat.DEFAULT_PREFIX, ""), "}", "")));
                        } catch (Exception e5) {
                            b(m2, "Invalid Size Parameter Name, " + ((Object) c0047ag.c()) + " not found and not numeric.");
                        }
                    }
                    if (strArrJ6.length > i13) {
                        int i17 = i13;
                        int i18 = i13 + 1;
                        String str2 = strArrJ6[i17];
                        try {
                            c0047ag.a(bH.W.g(str2));
                        } catch (Exception e6) {
                            b(m2, "Invalid Inactive size, " + str2 + "  not numeric.");
                        }
                    }
                    bDVar.a(c0047ag);
                } else if (strB.startsWith("replayFieldSelector")) {
                    String[] strArrJ7 = j(q(strB));
                    C0070bc c0070bc = new C0070bc();
                    int i19 = 0 + 1;
                    c0070bc.v(strArrJ7[0]);
                    if (strArrJ7.length > i19) {
                        i19++;
                        c0070bc.s(bH.W.b(strArrJ7[i19], PdfOps.DOUBLE_QUOTE__TOKEN, ""));
                    }
                    if (strArrJ7.length > i19) {
                        int i20 = i19;
                        i19++;
                        c0070bc.b(strArrJ7[i20]);
                        if (r2.c(c0070bc.d()) == null) {
                            b(m2, "Invalid Offset Parameter Name, " + c0070bc.d() + " not found.");
                        }
                    }
                    if (strArrJ7.length > i19) {
                        int i21 = i19;
                        i19++;
                        c0070bc.a(strArrJ7[i21]);
                        if (!c0070bc.b() && r2.c(c0070bc.a()) == null) {
                            b(m2, "Invalid Size Parameter Name, " + c0070bc.a() + " not found and not numeric.");
                        }
                    }
                    if (strArrJ7.length > i19) {
                        int i22 = i19;
                        int i23 = i19 + 1;
                        String str3 = strArrJ7[i22];
                        try {
                            c0070bc.a(bH.W.g(str3));
                        } catch (Exception e7) {
                            b(m2, "Invalid Inactive, " + str3 + "  not numeric.");
                        }
                    }
                    bDVar.a(c0070bc);
                } else if (strB.startsWith("settingSelector") && c0088bu != null) {
                    c0090bw = new C0090bw();
                    String[] strArrJ8 = j(q(strB));
                    int i24 = 0 + 1;
                    c0090bw.b(cY.a().a(r2, strArrJ8[0]));
                    if (strArrJ8.length > i24) {
                        int i25 = i24 + 1;
                        String strTrim5 = bH.W.b(bH.W.b(strArrJ8[i24], VectorFormat.DEFAULT_PREFIX, ""), "}", "").trim();
                        a(r2, m2, strTrim5, false);
                        c0090bw.u(strTrim5);
                        if (strArrJ8.length > i25) {
                            int i26 = i25 + 1;
                            String strTrim6 = bH.W.b(bH.W.b(strArrJ8[i25], VectorFormat.DEFAULT_PREFIX, ""), "}", "").trim();
                            a(r2, m2, strTrim6, false);
                            c0090bw.f(strTrim6);
                        }
                    }
                    c0088bu.a(c0090bw);
                } else if (strB.startsWith("enabledCondition") && c0088bu != null) {
                    String strTrim7 = bH.W.b(bH.W.b(q(strB), VectorFormat.DEFAULT_PREFIX, ""), "}", "").trim();
                    a(r2, m2, strTrim7, false);
                    c0088bu.u(strTrim7);
                } else if (!strB.startsWith("settingOption") || c0088bu == null) {
                    if (strB.startsWith("field") && c0088bu != null) {
                        AbstractC0093bz abstractC0093bzA = a(r2, m2);
                        a(r2, m2, abstractC0093bzA.aH(), false);
                        if (abstractC0093bzA.b() != null && !abstractC0093bzA.b().equals("") && r2.c(abstractC0093bzA.b()) == null) {
                            b(m2, "[" + str + "] field assigned to invalid Constant '" + abstractC0093bzA.b() + "'.");
                        } else if (abstractC0093bzA.b() != null && !abstractC0093bzA.b().equals("") && r2.c(abstractC0093bzA.b()).i().equals(ControllerParameter.PARAM_CLASS_ARRAY) && !strB.contains("[")) {
                            b(m2, "[" + str + "] field assigned to array Constant '" + abstractC0093bzA.b() + "'. Use array1D for array Constants");
                        }
                        c0088bu.a(abstractC0093bzA);
                    } else if (strB.startsWith("radio") && c0088bu != null) {
                        AbstractC0093bz abstractC0093bzC = c(r2, m2);
                        a(r2, m2, abstractC0093bzC.aH(), false);
                        if (abstractC0093bzC.b() != null && !abstractC0093bzC.b().equals("") && r2.c(abstractC0093bzC.b()) == null) {
                            b(m2, "[" + str + "] field assigned to invalid Constant '" + abstractC0093bzC.b() + "'.");
                        } else if (abstractC0093bzC.b() != null && !abstractC0093bzC.b().equals("") && r2.c(abstractC0093bzC.b()).i().equals(ControllerParameter.PARAM_CLASS_ARRAY) && !strB.contains("[")) {
                            b(m2, "[" + str + "] field assigned to array Constant '" + abstractC0093bzC.b() + "'. Use array1D for array Constants");
                        }
                        c0088bu.a(abstractC0093bzC);
                    } else if (strB.startsWith("channelSelector") && c0088bu != null) {
                        try {
                            G.E e8 = e(r2, m2);
                            a(r2, m2, e8.aH(), false);
                            if (e8.a() != null && !e8.a().equals("") && r2.c(e8.a()) == null) {
                                b(m2, "[" + str + "] Channel Selector assigned to invalid offset Constant '" + e8.a() + "'.");
                            }
                            if (e8.c() != null && !e8.c().equals("") && r2.c(e8.c()) == null) {
                                b(m2, "[" + str + "] Channel Selector assigned to invalid Length Constant '" + e8.a() + "'.");
                            }
                            if (r2.c(e8.a()) != null && !r2.c(e8.a()).i().equals(ControllerParameter.PARAM_CLASS_SCALAR)) {
                                a(m2, "Constant: " + e8.a() + " not of type scalar. Only Scalar is supported for channelSelector.");
                            }
                            if (r2.c(e8.c()) != null && !r2.c(e8.c()).i().equals(ControllerParameter.PARAM_CLASS_SCALAR)) {
                                a(m2, "Constant: " + e8.c() + " not of type scalar. Only Scalar is supported for channelSelector.");
                            }
                            c0088bu.a(e8);
                        } catch (Exception e9) {
                            a(m2, "Syntax error. Proper usage:\n   channelSelector = \"Label Text\", offset_scalar, len_scalar, can_id_scaler(optional), { enable condition (optional)}, { visible condition (optional)}");
                        }
                    } else if (strB.startsWith("canDeviceSelector") && c0088bu != null) {
                        try {
                            G.D dF = f(r2, m2);
                            a(r2, m2, dF.aH(), false);
                            if (dF.a() != null && !dF.a().equals("") && r2.c(dF.a()) == null) {
                                b(m2, "[" + str + "] CAN Device Selector assigned to invalid can ID Constant '" + dF.a() + "'.");
                            }
                            if (r2.c(dF.a()) != null && !r2.c(dF.a()).i().equals(ControllerParameter.PARAM_CLASS_SCALAR)) {
                                a(m2, "Constant: " + dF.a() + " not of type scalar. Only Scalar is supported for canDeviceSelector.");
                            }
                            c0088bu.a(dF);
                        } catch (Exception e10) {
                            a(m2, "Syntax error. Proper usage:\n   canDeviceSelector = \"Label Text\", can_id_scaler, { enable condition (optional)}, { visible condition (optional)}");
                        }
                    } else if (strB.startsWith("canClientIdSelector") && c0088bu != null) {
                        try {
                            G.C cG = g(r2, m2);
                            a(r2, m2, cG.aH(), false);
                            if (cG.a() != null && !cG.a().equals("") && r2.c(cG.a()) == null) {
                                b(m2, "[" + str + "] CAN Device Selector assigned to invalid can ID Constant '" + cG.a() + "'.");
                            }
                            if (r2.c(cG.a()) != null && !r2.c(cG.a()).i().equals(ControllerParameter.PARAM_CLASS_SCALAR)) {
                                a(m2, "Constant: " + cG.a() + " not of type scalar. Only Scalar is supported for canDeviceSelector.");
                            }
                            c0088bu.a(cG);
                        } catch (Exception e11) {
                            a(m2, "Syntax error. Proper usage:\n   canDeviceSelector = \"Label Text\", can_id_scaler, { enable condition (optional)}, { visible condition (optional)}");
                        }
                    } else if (strB.startsWith("slider") && c0088bu != null) {
                        AbstractC0093bz abstractC0093bzD = d(r2, m2);
                        a(r2, m2, abstractC0093bzD.aH(), false);
                        if (abstractC0093bzD.b() != null && !abstractC0093bzD.b().equals("") && r2.c(abstractC0093bzD.b()) == null) {
                            b(m2, "[" + str + "] field assigned to invalid Constant '" + abstractC0093bzD.b() + "'.");
                        }
                        if (r2.c(abstractC0093bzD.b()) != null && !r2.c(abstractC0093bzD.b()).i().equals(ControllerParameter.PARAM_CLASS_SCALAR)) {
                            a(m2, "Constant: " + abstractC0093bzD.b() + " not of type scalar. Only Scalar is supported for Sliders.");
                        }
                        c0088bu.a(abstractC0093bzD);
                    } else if (strB.startsWith("commandButton") && c0088bu != null) {
                        C0078bk c0078bk = new C0078bk();
                        c0078bk.v(m2.f());
                        String[] strArrJ9 = j(m2.e());
                        c0078bk.b(cY.a().a(r2, strArrJ9[0]));
                        c0078bk.a(strArrJ9[1]);
                        if (strArrJ9.length <= 2 || !strArrJ9[2].startsWith(VectorFormat.DEFAULT_PREFIX)) {
                            i2 = 2;
                        } else {
                            c0078bk.u(bH.W.b(bH.W.b(strArrJ9[2], VectorFormat.DEFAULT_PREFIX, ""), "}", "").trim());
                            a(r2, m2, c0078bk.aH(), false);
                            i2 = 3;
                        }
                        while (strArrJ9.length > i2) {
                            if (strArrJ9[i2].equals("clickOnClose")) {
                                c0078bk.a(C0078bk.f940f);
                            } else if (strArrJ9[i2].equals("clickOnCloseIfEnabled")) {
                                c0078bk.a(C0078bk.f936b);
                            } else if (strArrJ9[i2].equals("clickOnCloseIfDisabled")) {
                                c0078bk.a(C0078bk.f937c);
                            } else if (strArrJ9[i2].equals("closeDialogOnClick")) {
                                c0078bk.a(C0078bk.f939e);
                            } else if (!strArrJ9[i2].equals("showMessageOnClick")) {
                                b(m2, "Unknown commandButton attribute: " + strArrJ9[3] + ", know attributes include: clickOnCloseIfEnabled, clickOnCloseIfDisabled and clickOnClose");
                            } else if (strArrJ9.length > i2 + 1) {
                                c0078bk.a(C0078bk.f938d);
                                i2++;
                                c0078bk.b(f(strArrJ9[i2]));
                            } else {
                                b(m2, "Message required with showMessageOnClick flag");
                            }
                            i2++;
                        }
                        if (c0078bk.a() == null || c0078bk.a().equals("") || r2.O().b(c0078bk.a()) == null) {
                            b(m2, "[" + str + "] commandButton assigned to invalid ControllerCommands '" + c0078bk.a() + "'.");
                        }
                        c0088bu.a(c0078bk);
                    } else if (strB.startsWith("displayOnlyField") && c0088bu != null) {
                        C0083bp c0083bpA = a(r2, m2);
                        c0083bpA.d(true);
                        a(r2, m2, c0083bpA.aH(), false);
                        if (c0083bpA.b() != null && !c0083bpA.b().equals("") && r2.c(c0083bpA.b()) == null) {
                            b(m2, "[" + str + "] displayField assigned to invalid Constant '" + c0083bpA.b() + "'.");
                        }
                        c0088bu.a(c0083bpA);
                    } else if (strB.startsWith("runtimeValue") && c0088bu != null) {
                        C0083bp c0083bpB = b(r2, m2);
                        c0083bpB.d(true);
                        a(r2, m2, c0083bpB.aH(), false);
                        if (c0083bpB.b() != null && !c0083bpB.b().equals("") && r2.g(c0083bpB.b()) == null) {
                            b(m2, "[" + str + "] displayField assigned to invalid Constant '" + c0083bpB.b() + "'.");
                        }
                        c0088bu.a(c0083bpB);
                    } else if (strB.startsWith("topicHelp") && c0088bu != null) {
                        cZ cZVarA = cY.a().a(r2, q(strB));
                        c0088bu.e(cZVarA);
                        if (cZVarA.a().startsWith("http:/") || cZVarA.a().startsWith("file:/")) {
                            c0050aj = new C0050aj();
                            c0050aj.a(cZVarA);
                            c0050aj.b(cZVarA);
                            if (c0088bu.M() == null || c0088bu.M().length() <= 0) {
                                if (cZVarA.a().startsWith("file:/")) {
                                    c0050aj.b("Manual Help");
                                } else {
                                    c0050aj.b("Web Help");
                                }
                            } else if (cZVarA.a().startsWith("http:/")) {
                                c0050aj.b("Web - " + c0088bu.M());
                            } else {
                                c0050aj.b(c0088bu.M());
                            }
                            bDVar.a(c0050aj);
                        }
                    } else if (strB.startsWith("help")) {
                        c0050aj = new C0050aj();
                        String[] strArrJ10 = j(q(strB));
                        c0050aj.a(cY.a().a(r2, strArrJ10[0]));
                        if (strArrJ10.length > 1) {
                            c0050aj.b(bH.W.b(strArrJ10[1], PdfOps.DOUBLE_QUOTE__TOKEN, ""));
                        }
                        bDVar.a(c0050aj);
                    } else if (strB.startsWith("text") && c0050aj != null) {
                        c0050aj.a(bH.W.b(q(strB), PdfOps.DOUBLE_QUOTE__TOKEN, "") + "\n");
                    } else if (strB.startsWith("webHelp") && c0050aj != null) {
                        c0050aj.b(cY.a().a(r2, q(strB)));
                    } else if (strB.startsWith("array1D")) {
                        if (c0088bu != null) {
                            C0077bj c0077bjD = d(strB);
                            a(r2, m2, c0077bjD.c(), false);
                            c0088bu.a(c0077bjD);
                        } else {
                            a(m2, "Could not load array1D because the parent panel was not defined 1st.");
                        }
                    } else if (m2.f().equals("indicatorPanel")) {
                        String[] strArrJ11 = j(q(strB));
                        c0086bs = new C0086bs();
                        c0086bs.v(strArrJ11[0]);
                        bDVar.a(c0086bs);
                        if (strArrJ11.length > 1) {
                            c0086bs.a((int) bH.H.b(strArrJ11[1]));
                        }
                        if (strArrJ11.length > 2) {
                            String strTrim8 = bH.W.b(bH.W.b(strArrJ11[2], VectorFormat.DEFAULT_PREFIX, ""), "}", "").trim();
                            a(r2, m2, strTrim8, false);
                            c0086bs.u(strTrim8);
                        }
                        if (strArrJ11.length > 3) {
                            String strTrim9 = bH.W.b(bH.W.b(strArrJ11[3], VectorFormat.DEFAULT_PREFIX, ""), "}", "").trim();
                            a(r2, m2, strTrim9, false);
                            c0086bs.x(strTrim9);
                        }
                    } else if (m2.f().equals("indicator")) {
                        if (c0086bs == null) {
                            b(m2, "indicator defined in " + str + " with no indicatorPanel defined, ignoring.");
                        } else {
                            c0086bs.a(a(m2, r2));
                        }
                    } else if (m2.f().equals("readoutPanel")) {
                        String[] strArrJ12 = j(q(strB));
                        c0092by = new C0092by();
                        c0092by.v(strArrJ12[0]);
                        bDVar.a(c0092by);
                        if (strArrJ12.length > 1) {
                            c0092by.a((int) bH.H.b(strArrJ12[1]));
                        }
                        if (strArrJ12.length > 2) {
                            String strTrim10 = bH.W.b(bH.W.b(strArrJ12[2], VectorFormat.DEFAULT_PREFIX, ""), "}", "").trim();
                            a(r2, m2, strTrim10, false);
                            c0092by.u(strTrim10);
                        }
                        if (strArrJ12.length > 3) {
                            String strTrim11 = bH.W.b(bH.W.b(strArrJ12[3], VectorFormat.DEFAULT_PREFIX, ""), "}", "").trim();
                            a(r2, m2, strTrim11, false);
                            c0092by.x(strTrim11);
                        }
                    } else if (m2.f().equals("readout")) {
                        if (c0092by == null) {
                            b(m2, "readout defined in " + str + " with no readoutPanel defined, ignoring.");
                        } else {
                            String[] strArrJ13 = j(q(strB));
                            if (strArrJ13.length == 1) {
                                C0048ah c0048ahK = r2.k(strArrJ13[0]);
                                if (c0048ahK != null) {
                                    c0092by.a(c0048ahK);
                                } else if (r2.g(strArrJ13[0]) != null) {
                                    C0048ah c0048ah = new C0048ah();
                                    c0048ah.a(strArrJ13[0]);
                                    c0048ah.c(strArrJ13[0]);
                                    c0048ah.a(-1.0E9d);
                                    c0048ah.b(1.0E9d);
                                    c0048ah.e(1.0E8d);
                                    c0048ah.d(1.0E8d);
                                    c0092by.a(c0048ah);
                                } else {
                                    b(m2, "Unrecognized readout entry " + str + ", " + strArrJ13[0] + " not recognized as OutputChannel or Guage Definition");
                                }
                            } else {
                                c0092by.a(j(r2, m2));
                            }
                        }
                    } else if (m2.f().equals("userPassword")) {
                        String strE = m2.e();
                        aM aMVarC = r2.c(strE);
                        if (aMVarC == null || aMVarC.y() < 4) {
                            b(m2, "Invalid value for userPassword Parameter. Must be the name of a defined U32 Constant. Invalid Value: " + strE);
                        } else {
                            c0088bu.z(strE);
                            r2.c(true);
                        }
                    } else {
                        b(m2, "Unrecognized ini entry in " + str + ", ignoring.");
                    }
                } else if (c0090bw == null) {
                    a(m2, "Ini error: Found settingOption before settingSelector.");
                } else {
                    C0089bv c0089bv = new C0089bv();
                    String[] strArrJ14 = j(q(strB));
                    c0089bv.b(f(strArrJ14[0]));
                    for (int i27 = 0 + 1; i27 < strArrJ14.length; i27++) {
                        String strP = p(strArrJ14[i27]);
                        double d2 = 0.0d;
                        try {
                            d2 = Double.parseDouble(q(strArrJ14[i27]));
                        } catch (Exception e12) {
                            a(m2, "Value for " + strP + " should be numeric in row.");
                        }
                        c0089bv.a(strP, d2);
                    }
                    c0090bw.a(c0089bv);
                }
            } catch (Exception e13) {
                a(m2, "Invalid record in " + str + ", I don't know how to handle. It may cause more errors");
            }
        }
    }

    private void b(G.R r2, bD bDVar, J j2, String str) {
        bI bIVar = null;
        P.c cVar = null;
        Iterator it = a(j2, r2, str).iterator();
        while (it.hasNext()) {
            M m2 = (M) it.next();
            String strB = m2.b();
            try {
                if (strB.startsWith("wheelPattern")) {
                    String[] strArrJ = j(q(strB));
                    if (strArrJ.length < 2) {
                        a(m2, "Not enough Parameters for wheelPattern entry.");
                    } else if (strArrJ[1].equals("basicPattern")) {
                        P.b bVar = new P.b();
                        bVar.v(strArrJ[0]);
                        bVar.b(strArrJ[2]);
                        bVar.c(strArrJ[3]);
                        bVar.d(strArrJ[4]);
                        if (strArrJ.length > 5) {
                            bVar.e(strArrJ[5]);
                        }
                        bDVar.a(bVar);
                    } else if (!strArrJ[1].equals("fixedAngle")) {
                        if (!strArrJ[1].equals("bitArrayPattern")) {
                            a(m2, "Invalid WheelPattern class.");
                        } else if (strArrJ.length < 4) {
                            a(m2, "bitArrayPattern requires 4 parameters:\nname, designerPattern, constantBitsArray, constantOffset, [constantCapture]");
                        } else {
                            P.a aVar = new P.a();
                            aVar.v(strArrJ[0]);
                            aVar.a(strArrJ[1]);
                            aVar.b(strArrJ[2]);
                            if (strArrJ.length > 3) {
                                aVar.d(strArrJ[3]);
                            }
                            if (strArrJ.length > 4) {
                                aVar.c(strArrJ[4]);
                            }
                            bDVar.a(aVar);
                        }
                    }
                } else if (strB.startsWith("triggerWheel")) {
                    bIVar = new bI();
                    String[] strArrJ2 = j(q(strB));
                    bIVar.v(strArrJ2[0]);
                    bDVar.a(bIVar);
                    if (strArrJ2.length > 1) {
                        bIVar.s(bH.W.b(strArrJ2[1], PdfOps.DOUBLE_QUOTE__TOKEN, ""));
                    }
                    if (strArrJ2.length > 2) {
                        String strTrim = bH.W.b(bH.W.b(strArrJ2[2], VectorFormat.DEFAULT_PREFIX, ""), "}", "").trim();
                        a(r2, m2, strTrim, false);
                        bIVar.u(strTrim);
                    }
                    bIVar.a(strB.startsWith("triggerWheelDesigner"));
                } else if (strB.startsWith("topicHelp") && !a(bIVar, strB)) {
                    cZ cZVarA = cY.a().a(r2, q(strB));
                    bIVar.e(cZVarA);
                    if (cZVarA.a().startsWith("http:/") || cZVarA.a().startsWith("file:/")) {
                        C0050aj c0050aj = new C0050aj();
                        c0050aj.a(cZVarA);
                        c0050aj.b(cZVarA);
                        if (bIVar == null || bIVar.M() == null || bIVar.M().length() <= 0) {
                            c0050aj.b("Web Help");
                        } else {
                            c0050aj.b(bIVar.M());
                        }
                        bDVar.a(c0050aj);
                    }
                } else if (strB.startsWith("inputCapturePointParameterName") && 0 != 0) {
                    cVar.b(q(strB).trim());
                } else if (strB.startsWith("solutionWheelPattern") && bIVar != null) {
                    String[] strArrJ3 = j(q(strB));
                    String strB2 = bH.W.b(bH.W.b(strArrJ3[1], VectorFormat.DEFAULT_PREFIX, ""), "}", "");
                    a(r2, m2, strB2, false);
                    String str2 = strArrJ3[0];
                    if (bDVar.a(str2) == null) {
                        a(m2, "Attempt to add trigger wheel pattern " + str2 + " to triggerWheel, but wheel pattern is not defined.\nThe wheelPattern should be defined above the triggerWheel you are adding it to.");
                    } else {
                        bK bKVarA = bDVar.a(str2);
                        if (bKVarA == null) {
                            b(m2, "Wheel Pattern: " + str2 + " not found in current configuration. Was it defined after this row?");
                        } else {
                            bIVar.a(strB2, bKVarA);
                        }
                    }
                } else if (strB.startsWith("preferredSize") && bIVar != null) {
                    q(strB);
                    String[] strArrJ4 = j(q(strB));
                    bIVar.a(new G.A(bH.W.g(strArrJ4[0]), bH.W.g(strArrJ4[1])));
                } else if (bIVar == null) {
                    b(m2, "Syntax Error in ini section [" + str + "], a triggerWheel was should be defined first.");
                } else {
                    b(m2, "Syntax Error in ini section [" + str + "], line not understood and ignored.");
                }
            } catch (Exception e2) {
                a(m2, "Sysntax Error in [" + str + "], I don't know how to handle.");
                e2.printStackTrace();
            }
        }
    }

    private void a(G.R r2, J j2, String str) {
        Iterator it = a(j2, r2, str).iterator();
        while (it.hasNext()) {
            M m2 = (M) it.next();
            String strB = m2.b();
            try {
                if (strB.startsWith("requiresPowerCycle")) {
                    aM aMVarC = r2.c(m2.e());
                    if (aMVarC == null) {
                        b(m2, m2.e() + " not found, can not assign requiresPowerCycle.");
                    } else {
                        aMVarC.d(true);
                    }
                } else if (strB.startsWith("readOnly")) {
                    String[] strArrJ = j(q(strB));
                    aM aMVarC2 = r2.c(strArrJ[0]);
                    if (aMVarC2 == null) {
                        b(m2, strArrJ[0] + " not found, can not set to read only.");
                    } else {
                        aMVarC2.f(true);
                    }
                } else if (strB.startsWith(JMX.DEFAULT_VALUE_FIELD)) {
                    String[] strArrJ2 = j(q(strB));
                    aM aMVarC3 = r2.c(strArrJ2[0]);
                    if (aMVarC3 == null) {
                        b(m2, strArrJ2[0] + " not found, can not assign defaultValue.");
                    } else {
                        boolean zG = aMVarC3.G();
                        aMVarC3.f(false);
                        String str2 = strArrJ2[1];
                        if (str2.startsWith(PdfOps.DOUBLE_QUOTE__TOKEN) && aMVarC3.i().equals(ControllerParameter.PARAM_CLASS_BITS)) {
                            aMVarC3.e(str2);
                        } else if (aMVarC3.i().equals(ControllerParameter.PARAM_CLASS_ARRAY)) {
                            double[][] dArrA = new double[aMVarC3.a()][aMVarC3.m()];
                            String strE = m2.e();
                            String strTrim = strE.substring(strE.indexOf(",") + 1).replace("\\n", "\n").replace(Constants.INDENT, " ").trim();
                            if (strTrim.indexOf("\n") == -1) {
                                strTrim = strTrim.replace(" ", "\n");
                            }
                            try {
                                dArrA = bH.W.a(dArrA, strTrim + "\n");
                            } catch (Exception e2) {
                                b(m2, "Wrong number of values?");
                            }
                            aMVarC3.a(r2.h(), dArrA);
                        } else if (aMVarC3.i().equals("string")) {
                            aMVarC3.f(f(str2));
                        } else {
                            aMVarC3.d(Double.parseDouble(str2));
                        }
                        aMVarC3.f(zG);
                    }
                } else if (strB.startsWith("rawValue")) {
                    String[] strArrJ3 = j(q(strB));
                    aM aMVarC4 = r2.c(strArrJ3[0]);
                    if (aMVarC4 == null) {
                        b(m2, strArrJ3[0] + " not found, can not assign rawValue.");
                    } else {
                        boolean zG2 = aMVarC4.G();
                        aMVarC4.f(false);
                        if (strArrJ3[1].startsWith(PdfOps.DOUBLE_QUOTE__TOKEN) && aMVarC4.i().equals(ControllerParameter.PARAM_CLASS_BITS)) {
                            b(m2, "bit types not not supported byte assign rawValue.");
                        } else {
                            int[] iArr = new int[aMVarC4.y()];
                            String strE2 = m2.e();
                            aMVarC4.a(r2.h(), bH.W.d(strE2.substring(strE2.indexOf(",") + 1).replace(Constants.INDENT, " ").trim(), " "));
                        }
                        aMVarC4.f(zG2);
                    }
                } else if (strB.startsWith("useScaleAsDivisor")) {
                    String[] strArrJ4 = j(m2.e());
                    aM aMVarC5 = r2.c(strArrJ4[0]);
                    if (aMVarC5 != null) {
                        aMVarC5.f(di.a(r2, strArrJ4[1]));
                    } else {
                        b(m2, "Constant: " + strArrJ4[0] + " not found.");
                    }
                } else if (strB.startsWith("maintainConstantValue")) {
                    String[] strArrJ5 = j(m2.e());
                    aM aMVarC6 = r2.c(strArrJ5[0]);
                    if (aMVarC6 == null) {
                        a(m2, "Constant " + strArrJ5[0] + " not found! format: maintainConstantValue = scalarConstantName, { expression }");
                    } else if (!aMVarC6.i().equals(ControllerParameter.PARAM_CLASS_SCALAR) && !aMVarC6.i().equals(ControllerParameter.PARAM_CLASS_BITS)) {
                        a(m2, "Only Scalar and Bit Constants supported! format: maintainConstantValue = scalarConstantName, { expression }");
                    } else if (strArrJ5.length <= 1) {
                        a(m2, "Expression not optional! format: maintainConstantValue = scalarConstantName, { expression }");
                    } else {
                        String strB2 = bH.W.b(bH.W.b(strArrJ5[1], VectorFormat.DEFAULT_PREFIX, ""), "}", "");
                        aQ.a().a(aMVarC6, r2, strB2);
                        a(r2, m2, strB2, true);
                    }
                } else if (strB.startsWith("reverseIndex")) {
                    String[] strArrJ6 = j(m2.e());
                    aM aMVarC7 = r2.c(strArrJ6[0]);
                    if (aMVarC7 != null) {
                        aMVarC7.g(di.a(r2, "1"));
                    } else {
                        b(m2, "Constant: " + strArrJ6[0] + " not found.");
                    }
                } else if (strB.startsWith("oppositeEndian")) {
                    String[] strArrJ7 = j(m2.e());
                    aM aMVarC8 = r2.c(strArrJ7[0]);
                    if (aMVarC8 != null) {
                        aMVarC8.h(true);
                    } else {
                        b(m2, "Constant: " + strArrJ7[0] + " not found.");
                    }
                } else if (strB.startsWith("controllerPriority")) {
                    String[] strArrJ8 = j(m2.e());
                    aM aMVarC9 = r2.c(strArrJ8[0]);
                    if (aMVarC9 != null) {
                        aMVarC9.i(true);
                    } else {
                        b(m2, "Constant: " + strArrJ8[0] + " not found.");
                    }
                } else if (strB.startsWith("noMsqSave") || strB.startsWith("noSaveToMsq") || strB.startsWith("noLocalUpdate")) {
                    String[] strArrJ9 = j(m2.e());
                    aM aMVarC10 = r2.c(strArrJ9[0]);
                    if (aMVarC10 != null) {
                        aMVarC10.b(false);
                    } else {
                        b(m2, "Constant: " + strArrJ9[0] + " not found.");
                    }
                } else if (strB.startsWith("maximumElements")) {
                    String[] strArrJ10 = j(m2.e());
                    aM aMVarC11 = r2.c(strArrJ10[0]);
                    if (aMVarC11 != null) {
                        aMVarC11.d(bH.W.g(strArrJ10[1]));
                    } else {
                        b(m2, "Constant: " + strArrJ10[0] + " not found.");
                    }
                } else if (strB.startsWith("useMappingTable")) {
                    String[] strArrJ11 = j(m2.e());
                    aM aMVarC12 = r2.c(strArrJ11[0]);
                    if (aMVarC12 != null && strArrJ11.length > 1) {
                        bH.E eB = bH.E.b(r2.F(), strArrJ11[1]);
                        if (eB != null) {
                            aMVarC12.a(eB);
                        } else {
                            b(m2, "Failed to load Mapping file: " + strArrJ11[1]);
                        }
                    } else if (aMVarC12 != null) {
                        b(m2, "missing required mapping file.");
                    } else {
                        b(m2, "Constant: " + strArrJ11[0] + " not found.");
                    }
                } else {
                    b(m2, "Unrecognized row, don't know what to do.");
                }
            } catch (Exception e3) {
                a(m2, "Syntax Error in " + str + ", values not as expected. Message:\n" + e3.getLocalizedMessage());
            }
        }
    }

    private void b(G.R r2, J j2, String str) {
        Iterator it = a(j2, r2, str).iterator();
        while (it.hasNext()) {
            M m2 = (M) it.next();
            try {
                String strF = m2.f();
                String[] strArrJ = j(m2.e());
                if (!strF.equals("displayCommand")) {
                    r2.O().a(strF, strArrJ);
                } else if (strArrJ.length < 2) {
                    a(m2, "Required parameters missing. Proper usage: displayCommand = commandName, \"Displaylabel\"");
                } else {
                    if (!r2.O().a(strArrJ[0], f(strArrJ[1]), true)) {
                        a(m2, "controllerCommand not found");
                    }
                }
            } catch (Exception e2) {
                a(m2, "Syntax Error in " + str + ", values not as expected.");
            }
        }
    }

    private void c(G.R r2, bD bDVar, J j2, String str) {
        String strB;
        C0079bl c0079bl = null;
        Iterator it = a(j2, r2, str).iterator();
        while (it.hasNext()) {
            M m2 = (M) it.next();
            String strB2 = m2.b();
            try {
                if (strB2.startsWith("curve")) {
                    c0079bl = new C0079bl();
                    c0079bl.q(this.f2053l);
                    String[] strArrJ = j(q(strB2));
                    c0079bl.v(strArrJ[0]);
                    bDVar.a(c0079bl);
                    if (strArrJ.length > 1) {
                        c0079bl.d(cY.a().a(r2, strArrJ[1].trim()));
                    }
                } else if (strB2.startsWith("topicHelp") && !a(c0079bl, strB2)) {
                    cZ cZVarA = cY.a().a(r2, q(strB2));
                    c0079bl.e(cZVarA);
                    if (cZVarA.a().startsWith("http:/") || cZVarA.a().startsWith("file:/")) {
                        C0050aj c0050aj = new C0050aj();
                        c0050aj.a(cZVarA);
                        c0050aj.b(cZVarA);
                        if (c0079bl.M() == null || c0079bl.M().length() <= 0) {
                            c0050aj.b("Web Help");
                        } else {
                            c0050aj.b(c0079bl.M());
                        }
                        bDVar.a(c0050aj);
                    }
                } else if (strB2.startsWith("enabledCondition") && c0079bl != null) {
                    String strTrim = bH.W.b(bH.W.b(q(strB2), VectorFormat.DEFAULT_PREFIX, ""), "}", "").trim();
                    c0079bl.u(strTrim);
                    a(r2, m2, strTrim, false);
                } else if (strB2.startsWith("columnLabel") && !a(c0079bl, strB2)) {
                    String[] strArrJ2 = j(q(strB2));
                    int i2 = 0;
                    while (i2 < strArrJ2.length) {
                        if (strArrJ2.length > i2 + 1) {
                            int i3 = i2;
                            int i4 = i2 + 1;
                            c0079bl.c(cY.a().a(r2, strArrJ2[i3].trim()));
                            i2 = i4 + 1;
                            c0079bl.a(cY.a().a(r2, strArrJ2[i4].trim()));
                        } else {
                            int i5 = i2;
                            i2++;
                            c0079bl.a(cY.a().a(r2, strArrJ2[i5].trim()));
                        }
                    }
                } else if (strB2.startsWith("xAxis") && !a(c0079bl, strB2)) {
                    String[] strArrJ3 = j(q(strB2));
                    c0079bl.c(di.a(r2, strArrJ3[0]));
                    c0079bl.d(di.a(r2, strArrJ3[1]));
                    if (strArrJ3.length > 2) {
                        c0079bl.h(bH.W.g(strArrJ3[2].trim()));
                    }
                } else if (strB2.startsWith("yAxis") && !a(c0079bl, strB2)) {
                    String[] strArrJ4 = j(q(strB2));
                    c0079bl.a(di.a(r2, strArrJ4[0]));
                    c0079bl.b(di.a(r2, strArrJ4[1]));
                    if (strArrJ4.length > 2) {
                        c0079bl.g(bH.W.g(strArrJ4[2].trim()));
                    }
                } else if (strB2.startsWith("xBins") && !a(c0079bl, strB2)) {
                    String[] strArrJ5 = j(q(strB2));
                    String strB3 = bH.W.b(strArrJ5[0].trim(), PdfOps.DOUBLE_QUOTE__TOKEN, "");
                    if (r2.c(strB3) == null) {
                        a(m2, "CurveGraph xBin not a valid Constant.");
                    }
                    c0079bl.c(strB3);
                    if (strArrJ5.length > 1) {
                        c0079bl.d(strArrJ5[1].trim());
                    }
                    if (strArrJ5.length > 2 && strArrJ5[2].equals("readOnly")) {
                        c0079bl.e(true);
                    }
                } else if (strB2.startsWith("yBins") && !a(c0079bl, strB2)) {
                    String[] strArrJ6 = j(q(strB2));
                    int i6 = 0 + 1;
                    String strB4 = bH.W.b(strArrJ6[0].trim(), PdfOps.DOUBLE_QUOTE__TOKEN, "");
                    if (r2.c(strB4) == null) {
                        a(m2, "CurveGraph yBin not a valid Constant.");
                    }
                    if (strArrJ6.length > i6 && !strArrJ6[i6].startsWith(VectorFormat.DEFAULT_PREFIX)) {
                        i6++;
                        c0079bl.b(strArrJ6[i6].trim());
                    }
                    if (strArrJ6.length > i6) {
                        int i7 = i6;
                        int i8 = i6 + 1;
                        strB = bH.W.b(bH.W.b(strArrJ6[i7], VectorFormat.DEFAULT_PREFIX, ""), "}", "");
                    } else {
                        strB = "";
                    }
                    c0079bl.a(strB4, strB);
                } else if (strB2.startsWith("lineLabel") && !a(c0079bl, strB2)) {
                    c0079bl.b(cY.a().a(r2, j(q(strB2))[0].trim()));
                } else if (strB2.startsWith("gauge") && !a(c0079bl, strB2)) {
                    String[] strArrJ7 = j(q(strB2));
                    if (strArrJ7.length > 0) {
                        c0079bl.e(strArrJ7[0].trim());
                    }
                } else if (m2.f().equals("showTextValues") && !a(c0079bl, strB2)) {
                    String[] strArrJ8 = j(q(strB2));
                    if (strArrJ8.length > 0) {
                        try {
                            c0079bl.a(Boolean.parseBoolean(strArrJ8[0].trim()));
                        } catch (Exception e2) {
                            b(m2, "showTextValues expects true or false");
                        }
                    }
                } else if (m2.f().equals("size") && !a(c0079bl, strB2)) {
                    String[] strArrJ9 = j(q(strB2));
                    if (strArrJ9.length != 2) {
                        b(m2, "CurvePanel suggestedSize should have 2 values\nsuggestedSize = width, height");
                    }
                    c0079bl.a(new G.A(bH.W.g(strArrJ9[0]), bH.W.g(strArrJ9[1])));
                } else if (m2.f().equals("showXYDataPlot") && !a(c0079bl, strB2)) {
                    String[] strArrJ10 = j(q(strB2));
                    if (strArrJ10.length < 3) {
                        b(m2, "CurvePanel showXYDataPlot must have at least 3 values\nshowXYDataPlot = displayByDefaultBoolean, xFieldName, yFieldName, [ DataDisplayCondition ] }");
                    } else {
                        c0079bl.f(Boolean.parseBoolean(strArrJ10[0]));
                        c0079bl.f(strArrJ10[1]);
                        c0079bl.g(strArrJ10[2]);
                        if (strArrJ10.length > 3) {
                            c0079bl.h(bH.W.b(bH.W.b(strArrJ10[3], VectorFormat.DEFAULT_PREFIX, ""), "}", ""));
                        }
                    }
                } else if (m2.f().equals("userPassword")) {
                    String strE = m2.e();
                    aM aMVarC = r2.c(strE);
                    if (aMVarC == null || aMVarC.y() < 4) {
                        b(m2, "Invalid value for userPassword Parameter. Must be the name of a defined U32 Constant. Invalid Value: " + strE);
                    } else {
                        c0079bl.z(strE);
                        r2.c(true);
                    }
                } else if (!m2.f().equals("suppressGraph")) {
                    b(m2, "Unrecognizedd entry. Do not know how to handle ini row.");
                } else if (!m2.e().toLowerCase().equals("true") && !m2.e().toLowerCase().equals("false")) {
                    String strB5 = bH.W.b(bH.W.b(m2.e(), VectorFormat.DEFAULT_PREFIX, ""), "}", "");
                    c0079bl.e(di.a(r2, strB5));
                    a(r2, m2, strB5, false);
                } else if (Boolean.parseBoolean(m2.e())) {
                    c0079bl.e(new G.B(1.0d));
                } else {
                    c0079bl.e(new G.B(0.0d));
                }
            } catch (Exception e3) {
                a(m2, "Syntax Error in " + str + ", values not as expected.");
            }
        }
    }

    private void d(G.R r2, bD bDVar, J j2, String str) {
        Iterator it = a(j2, r2, str).iterator();
        while (it.hasNext()) {
            M m2 = (M) it.next();
            try {
                if (m2.f().equals("showPanel")) {
                    String[] strArrJ = j(m2.e());
                    C0053am c0053am = new C0053am(r2);
                    c0053am.c("showPanel");
                    c0053am.b(strArrJ[0]);
                    c0053am.a(strArrJ[1]);
                    bDVar.a(c0053am);
                } else {
                    b(m2, "Unrecognized entry. Ignoring.");
                }
            } catch (Exception e2) {
                a(m2, "Syntax Error.");
            }
        }
    }

    private void e(G.R r2, bD bDVar, J j2, String str) {
        G.aA aAVarA = null;
        G.aA aAVar = null;
        String strQ = null;
        Iterator it = a(j2, r2, str).iterator();
        while (it.hasNext()) {
            M m2 = (M) it.next();
            a(m2);
            String strB = m2.b();
            try {
                if (strB.startsWith("menuDialog")) {
                    strQ = q(strB);
                } else if (m2.f().equals("menu")) {
                    aAVarA = a(m2, false);
                    a(r2, m2, aAVarA.aH(), false);
                    if (strQ == null || strQ.equals("main")) {
                        bDVar.a(aAVarA);
                    } else {
                        C0088bu c0088buC = bDVar.c(strQ);
                        if (c0088buC == null) {
                            b(m2, "Menu: " + aAVarA.e() + " not added, menu dialog \"" + strQ + "\" not found");
                        } else {
                            c0088buC.a(aAVarA);
                        }
                    }
                } else if (strB.startsWith("subMenu")) {
                    if (aAVarA == null) {
                        a(m2, "Adding subMenu before main menu defined");
                    } else {
                        G.aA aAVarA2 = a(m2, true);
                        a(r2, m2, aAVarA2.aH(), false);
                        if (bDVar.c(aAVarA2.d()) == null && c(aAVarA2.d())) {
                            a(r2, aAVarA2.d(), aAVarA2.f());
                        }
                        if (bDVar.c(aAVarA2.d()) == null && !c(aAVarA2.d()) && bDVar.b(aAVarA2.d()) == null) {
                            b(m2, "Unknown Menu Target: " + aAVarA2.d());
                        }
                        aAVarA.a(aAVarA2);
                    }
                } else if (m2.f().equals("groupMenu")) {
                    if (aAVarA == null) {
                        a(m2, "Adding groupMenu before main menu defined");
                    } else {
                        String[] strArrJ = j(m2.e());
                        aAVar = new G.aA();
                        int i2 = 0;
                        if (!strArrJ[0].contains(PdfOps.DOUBLE_QUOTE__TOKEN)) {
                            i2 = 0 + 1;
                            aAVar.v(strArrJ[0]);
                        }
                        int i3 = i2;
                        int i4 = i2 + 1;
                        String strF = f(strArrJ[i3]);
                        aAVar.b(strF);
                        aAVar.d(strF);
                        if (strArrJ.length > i4) {
                            i4++;
                            aAVar.u(bH.W.b(bH.W.b(strArrJ[i4], VectorFormat.DEFAULT_PREFIX, ""), "}", ""));
                            a(r2, m2, aAVar.aH(), false);
                        }
                        if (strArrJ.length > i4) {
                            int i5 = i4;
                            int i6 = i4 + 1;
                            aAVar.e(bH.W.b(bH.W.b(strArrJ[i5], VectorFormat.DEFAULT_PREFIX, ""), "}", ""));
                            a(r2, m2, aAVar.aH(), false);
                        }
                        aAVarA.a(aAVar);
                    }
                } else if (m2.f().equals("groupChildMenu")) {
                    if (aAVar == null) {
                        a(m2, "Adding menuGroupChild before menuGroup defined");
                    } else {
                        G.aA aAVarA3 = a(m2, true);
                        a(r2, m2, aAVarA3.aH(), false);
                        aAVar.a(aAVarA3);
                    }
                } else if (!strB.startsWith("plugIn")) {
                    b(m2, "[Menu] Row not understood, unknown keyword: " + m2.f() + ". Please check syntax.");
                }
            } catch (Exception e2) {
                a(m2, "Malformed ini in " + str + ", I don't know how to handle.");
            }
        }
        r2.a(bDVar);
    }

    private G.aA a(M m2, boolean z2) {
        String strE = m2.e();
        G.aA aAVar = new G.aA();
        aAVar.q(this.f2053l);
        if (strE.indexOf("\"     ") > 0) {
            b(m2, "Ini entry missing comma, repaired.");
        }
        String[] strArrJ = j(strE);
        int i2 = 0;
        if (z2) {
            i2 = 0 + 1;
            String str = strArrJ[0];
            if (str.equals("std_separator")) {
                aAVar.a(true);
                return aAVar;
            }
            aAVar.a(str);
        }
        try {
            int i3 = i2;
            int i4 = i2 + 1;
            String strB = bH.W.b(strArrJ[i3].trim(), PdfOps.DOUBLE_QUOTE__TOKEN, "");
            aAVar.d(strB);
            if (strB.indexOf("&") != -1) {
                int iIndexOf = strB.indexOf("&");
                aAVar.c(strB.substring(iIndexOf + 1, iIndexOf + 2));
            }
            aAVar.b(bH.W.b(strB, "&", ""));
            if (z2 && strArrJ.length > i4) {
                try {
                    if (bH.H.a(strArrJ[i4])) {
                        i4++;
                        aAVar.a((int) bH.H.b(strArrJ[i4].trim()));
                    }
                } catch (Exception e2) {
                }
            }
            if (strArrJ.length > i4) {
                int i5 = i4;
                i4++;
                aAVar.u(bH.W.b(bH.W.b(strArrJ[i5], VectorFormat.DEFAULT_PREFIX, ""), "}", "").trim());
            }
            if (strArrJ.length > i4) {
                int i6 = i4;
                int i7 = i4 + 1;
                aAVar.e(bH.W.b(bH.W.b(strArrJ[i6], VectorFormat.DEFAULT_PREFIX, ""), "}", "").trim());
            }
        } catch (Exception e3) {
            b(m2, "Error paring Menu.");
            e3.printStackTrace();
        }
        return aAVar;
    }

    private boolean a(Object obj, String str) {
        if (obj != null) {
            return false;
        }
        bH.C.b("ini error, Tried to load row: \n\t" + str + "\n\tbut Object not initialized.");
        return true;
    }

    private C0077bj d(String str) {
        C0077bj c0077bj = new C0077bj();
        c0077bj.q(this.f2053l);
        String[] strArrJ = j(q(str));
        int i2 = 0 + 1;
        String strB = bH.W.b(strArrJ[0], PdfOps.DOUBLE_QUOTE__TOKEN, "");
        if (strB != null && !strB.equals("")) {
            c0077bj.s(strB.trim());
        }
        int i3 = i2 + 1;
        c0077bj.a(bH.W.b(strArrJ[i2], PdfOps.DOUBLE_QUOTE__TOKEN, ""));
        int i4 = i3 + 1;
        c0077bj.b(strArrJ[i3]);
        if (strArrJ.length > i4) {
            int i5 = i4 + 1;
            c0077bj.c(bH.W.b(bH.W.b(strArrJ[i4], VectorFormat.DEFAULT_PREFIX, ""), "}", "").trim());
        }
        return c0077bj;
    }

    private C0083bp a(G.R r2, M m2) {
        String strB = m2.b();
        C0083bp c0083bp = new C0083bp();
        c0083bp.q(this.f2053l);
        String[] strArrJ = j(q(strB));
        int i2 = 0 + 1;
        String strB2 = bH.W.b(strArrJ[0], PdfOps.DOUBLE_QUOTE__TOKEN, "");
        if (strB2.startsWith("!")) {
            c0083bp.a(true);
            strB2 = strB2.substring(1);
        } else if (strB2.startsWith(FXMLLoader.CONTROLLER_METHOD_PREFIX)) {
            c0083bp.b(true);
            strB2 = strB2.substring(1);
        }
        c0083bp.b(cY.a().a(r2, strB2));
        if (strArrJ.length > i2) {
            i2++;
            String str = strArrJ[i2];
            if (str.startsWith(VectorFormat.DEFAULT_PREFIX) && str.endsWith("}")) {
                try {
                    c0083bp.a(cY.a().a(r2, str));
                    str = null;
                } catch (V.g e2) {
                    a(m2, "Error in String expression");
                }
            } else if (str.indexOf("[") > 0) {
                String strSubstring = str.substring(0, str.indexOf("["));
                c0083bp.a(bH.W.g(bH.W.b(bH.W.b(str.substring(str.indexOf("[")), "[", ""), "]", "")));
                str = strSubstring;
            }
            c0083bp.a(str);
        }
        if (strArrJ.length > i2) {
            int i3 = i2;
            i2++;
            c0083bp.u(bH.W.b(bH.W.b(strArrJ[i3], VectorFormat.DEFAULT_PREFIX, ""), "}", "").trim());
        }
        if (strArrJ.length > i2) {
            int i4 = i2;
            i2++;
            c0083bp.f(bH.W.b(bH.W.b(strArrJ[i4], VectorFormat.DEFAULT_PREFIX, ""), "}", "").trim());
        }
        if (strArrJ.length > i2) {
            int i5 = i2;
            int i6 = i2 + 1;
            String str2 = strArrJ[i5];
            if (str2.equals("displayInHex")) {
                c0083bp.e(true);
            } else {
                b(m2, "Unknown 5th position attribute: " + str2 + ", known field Attribute: displayInHex");
            }
        }
        return c0083bp;
    }

    private C0083bp b(G.R r2, M m2) {
        String strB = m2.b();
        C0083bp c0083bp = new C0083bp();
        c0083bp.q(this.f2053l);
        String[] strArrJ = j(q(strB));
        int i2 = 0 + 1;
        String strB2 = bH.W.b(strArrJ[0], PdfOps.DOUBLE_QUOTE__TOKEN, "");
        if (strB2.startsWith("!")) {
            c0083bp.a(true);
            strB2 = strB2.substring(1);
        } else if (strB2.startsWith(FXMLLoader.CONTROLLER_METHOD_PREFIX)) {
            c0083bp.b(true);
            strB2 = strB2.substring(1);
        }
        c0083bp.b(cY.a().a(r2, strB2));
        if (strArrJ.length > i2) {
            i2++;
            c0083bp.a(strArrJ[i2]);
        }
        if (strArrJ.length > i2) {
            int i3 = i2;
            i2++;
            c0083bp.u(bH.W.b(bH.W.b(strArrJ[i3], VectorFormat.DEFAULT_PREFIX, ""), "}", "").trim());
        }
        if (strArrJ.length > i2) {
            int i4 = i2;
            i2++;
            c0083bp.f(bH.W.b(bH.W.b(strArrJ[i4], VectorFormat.DEFAULT_PREFIX, ""), "}", "").trim());
        }
        if (strArrJ.length > i2) {
            int i5 = i2;
            int i6 = i2 + 1;
            String str = strArrJ[i5];
            if (str.equals("displayInHex")) {
                c0083bp.e(true);
            } else {
                b(m2, "Unknown 5th position attribute: " + str + ", known field Attribute: displayInHex");
            }
        }
        return c0083bp;
    }

    private C0091bx c(G.R r2, M m2) {
        String strB = m2.b();
        C0091bx c0091bx = new C0091bx();
        c0091bx.q(this.f2053l);
        String[] strArrJ = j(q(strB));
        int i2 = 0 + 1;
        String str = strArrJ[0];
        if (str.equals("horizontal")) {
            c0091bx.f(true);
        } else if (!str.equals("vertical")) {
            b(m2, "Orientation must be horizontal or vertical. Unknown value: " + str);
        }
        int i3 = i2 + 1;
        c0091bx.b(cY.a().a(r2, bH.W.b(strArrJ[i2], PdfOps.DOUBLE_QUOTE__TOKEN, "")));
        if (strArrJ.length > i3) {
            i3++;
            String str2 = strArrJ[i3];
            if (str2.startsWith(VectorFormat.DEFAULT_PREFIX) && str2.endsWith("}")) {
                try {
                    c0091bx.a(cY.a().a(r2, str2));
                    str2 = null;
                } catch (V.g e2) {
                    a(m2, "Error in String expression");
                }
            }
            c0091bx.a(str2);
        }
        if (strArrJ.length > i3) {
            int i4 = i3;
            i3++;
            c0091bx.u(bH.W.b(bH.W.b(strArrJ[i4], VectorFormat.DEFAULT_PREFIX, ""), "}", "").trim());
        }
        if (strArrJ.length > i3) {
            int i5 = i3;
            int i6 = i3 + 1;
            c0091bx.f(bH.W.b(bH.W.b(strArrJ[i5], VectorFormat.DEFAULT_PREFIX, ""), "}", "").trim());
        }
        return c0091bx;
    }

    private bB d(G.R r2, M m2) {
        String strB = m2.b();
        bB bBVar = new bB();
        bBVar.q(this.f2053l);
        String[] strArrJ = j(q(strB));
        int i2 = 0 + 1;
        String strB2 = bH.W.b(strArrJ[0], PdfOps.DOUBLE_QUOTE__TOKEN, "");
        if (strB2.startsWith("!")) {
            bBVar.a(true);
            strB2 = strB2.substring(1);
        } else if (strB2.startsWith(FXMLLoader.CONTROLLER_METHOD_PREFIX)) {
            bBVar.b(true);
            strB2 = strB2.substring(1);
        }
        bBVar.b(cY.a().a(r2, strB2));
        if (strArrJ.length > i2) {
            i2++;
            String str = strArrJ[i2];
            if (str.indexOf("[") > 0) {
                String strSubstring = str.substring(0, str.indexOf("["));
                bBVar.a(bH.W.g(bH.W.b(bH.W.b(str.substring(str.indexOf("[")), "[", ""), "]", "")));
                str = strSubstring;
            }
            bBVar.a(str);
        }
        if (strArrJ.length > i2) {
            int i3 = i2;
            i2++;
            String str2 = strArrJ[i3];
            if (str2.equals("horizontal")) {
                bBVar.c(bB.f825a);
            } else if (str2.equals("vertical")) {
                bBVar.c(bB.f826b);
            } else {
                b(m2, "Unknown Slider orientation: " + str2 + ", valid values: (horizontal, vertical). using default: horizontal");
                bBVar.c(bB.f825a);
            }
        }
        if (strArrJ.length > i2) {
            int i4 = i2;
            int i5 = i2 + 1;
            bBVar.u(bH.W.b(bH.W.b(strArrJ[i4], VectorFormat.DEFAULT_PREFIX, ""), "}", "").trim());
        }
        return bBVar;
    }

    private G.E e(G.R r2, M m2) {
        G.E e2 = new G.E();
        e2.q(this.f2053l);
        String[] strArrJ = j(m2.e());
        e2.b(cY.a().a(r2, strArrJ[0]));
        e2.a(strArrJ[1]);
        e2.b(strArrJ[2]);
        int i2 = 3;
        while (strArrJ.length > i2 && !strArrJ[i2].trim().startsWith(VectorFormat.DEFAULT_PREFIX)) {
            if (i2 == 3) {
                int i3 = i2;
                i2++;
                e2.c(strArrJ[i3]);
            } else {
                int i4 = i2;
                i2++;
                try {
                    e2.d(strArrJ[i4]);
                } catch (V.g e3) {
                    b(m2, e3.getLocalizedMessage());
                }
            }
        }
        if (strArrJ.length > i2) {
            int i5 = i2;
            i2++;
            e2.u(bH.W.b(bH.W.b(strArrJ[i5], VectorFormat.DEFAULT_PREFIX, ""), "}", ""));
        }
        if (strArrJ.length > i2) {
            int i6 = i2;
            int i7 = i2 + 1;
            e2.f(bH.W.b(bH.W.b(strArrJ[i6], VectorFormat.DEFAULT_PREFIX, ""), "}", ""));
        }
        return e2;
    }

    private G.D f(G.R r2, M m2) {
        G.D d2 = new G.D();
        d2.q(this.f2053l);
        String[] strArrJ = j(m2.e());
        d2.b(cY.a().a(r2, strArrJ[0]));
        d2.a(strArrJ[1]);
        if (strArrJ.length > 2) {
            d2.u(bH.W.b(bH.W.b(strArrJ[2], VectorFormat.DEFAULT_PREFIX, ""), "}", ""));
        }
        if (strArrJ.length > 3) {
            d2.f(bH.W.b(bH.W.b(strArrJ[3], VectorFormat.DEFAULT_PREFIX, ""), "}", ""));
        }
        return d2;
    }

    private G.C g(G.R r2, M m2) {
        G.C c2 = new G.C();
        c2.q(this.f2053l);
        String[] strArrJ = j(m2.e());
        c2.b(cY.a().a(r2, strArrJ[0]));
        c2.a(strArrJ[1]);
        if (strArrJ.length > 2) {
            c2.u(bH.W.b(bH.W.b(strArrJ[2], VectorFormat.DEFAULT_PREFIX, ""), "}", ""));
        }
        if (strArrJ.length > 3) {
            c2.f(bH.W.b(bH.W.b(strArrJ[3], VectorFormat.DEFAULT_PREFIX, ""), "}", ""));
        }
        return c2;
    }

    protected void a(G.R r2, M m2, String str, boolean z2) {
        a(r2, m2, str, (String) null, z2);
    }

    protected void a(G.R r2, M m2, String str, String str2, boolean z2) {
        if (!a() || str == null || str.equals("")) {
            return;
        }
        if (str.startsWith(VectorFormat.DEFAULT_PREFIX)) {
            str = str.substring(1, str.length() - 1);
        }
        try {
            if (str.contains("%INDEX%")) {
                str = bH.W.b(str, "%INDEX%", "0");
            }
            C0126i.a(str, (aI) r2);
        } catch (Exception e2) {
            if (e2 instanceof FileNotFoundException) {
                a(m2, e2.getLocalizedMessage());
                return;
            }
            if (e2 instanceof C0918u) {
                return;
            }
            String message = str2 == null ? e2.getMessage() : str2 + " \n" + e2.getMessage();
            if (!z2) {
                b(m2, message);
            } else {
                Logger.getLogger(C0172ab.class.getName()).log(Level.WARNING, str2, (Throwable) e2);
                a(m2, message);
            }
        } catch (StackOverflowError e3) {
            a(m2, "Circular Dependency! Formula can not reference itself.");
        }
    }

    protected void a(G.R r2, bQ bQVar, String str, String str2, boolean z2) {
        if (!a() || bQVar == null || bQVar.c() == null || bQVar.c().equals("")) {
            return;
        }
        if (str.equals("toggleSwitchOn")) {
            bH.C.c("break: 48s35");
        }
        try {
            String strC = bQVar.c();
            if (strC.contains("%INDEX%")) {
                strC = bH.W.b(strC, "%INDEX%", "0");
            }
            C0126i.a(strC, (aI) r2);
        } catch (Exception e2) {
            if (e2 instanceof C0918u) {
                return;
            }
            String str3 = "expression error in ini entry for " + str + ", attribute: " + str2 + "\n" + e2.getMessage();
            e2.printStackTrace();
            if (z2) {
                bH.C.a(str3);
            } else {
                bH.C.b(str3);
            }
        } catch (StackOverflowError e3) {
            bH.C.a("Circular Dependency! Formula can not reference itself. Ini entry " + str + ", attribute: " + str2 + ", expression:" + bQVar.c());
        }
    }

    private void e(G.R r2, J j2) {
        ArrayList arrayListA = a(j2, r2, "OutputChannels");
        M m2 = null;
        int iA = 0;
        int iA2 = 0;
        for (int i2 = 0; i2 < arrayListA.size(); i2++) {
            try {
                m2 = (M) arrayListA.get(i2);
                String strF = m2.f();
                if (strF.equals("scatteredOffsetArray")) {
                    aM aMVarC = r2.c(m2.e());
                    if (aMVarC == null) {
                        a(m2, "Constant Not Found: " + m2.e());
                    } else if (aMVarC.i().equals(ControllerParameter.PARAM_CLASS_ARRAY)) {
                        r2.O().p(m2.e());
                    } else {
                        a(m2, "scatteredOffsetArray must be of type array");
                    }
                } else if (strF.equals("scatteredGetEnabled")) {
                    r2.O().e(di.a(r2, m2.e()));
                } else if (!G.F.a(strF)) {
                    aH aHVarA = a(m2, r2, iA, iA2);
                    aHVarA.a(r2.O() == null || r2.O().e());
                    r2.a(aHVarA);
                    if (!aHVarA.b().equals("formula")) {
                        iA = aHVarA.a() + aHVarA.l();
                        iA2 = aHVarA.a();
                    }
                }
            } catch (Exception e2) {
                a(m2, "Malformed [OutputChannel] entry");
            }
        }
        for (int i3 = 0; i3 < arrayListA.size(); i3++) {
            try {
                m2 = (M) arrayListA.get(i3);
                String strF2 = m2.f();
                if (!G.F.a(strF2) && !strF2.equals("scatteredOffsetArray") && !strF2.equals("scatteredGetEnabled")) {
                    aH aHVarG = r2.g(strF2);
                    if (aHVarG != null && aHVarG.k() != null && !aHVarG.k().contains("AppEvent.")) {
                        a(r2, m2, aHVarG.k(), true);
                    }
                    if (aHVarG.g() instanceof bQ) {
                        a(r2, m2, ((bQ) aHVarG.g()).c(), true);
                    }
                    if (aHVarG.j() instanceof bQ) {
                        a(r2, m2, ((bQ) aHVarG.j()).c(), true);
                    }
                }
            } catch (Exception e3) {
                a(m2, "Malformed [OutputChannel] entry");
            }
        }
    }

    private void c(G.R r2, J j2, String str) {
        ArrayList arrayListA = a(j2, r2, str);
        M m2 = null;
        int iA = 0;
        int iA2 = 0;
        boolean zEquals = str.equals("ExtendedReplay");
        aI aIVarA = C0125h.a().a(((Object) r2) + "_Replay");
        if (aIVarA == null) {
            aIVarA = new H.e(r2);
            C0125h.a().a(aIVarA);
        }
        aI aIVarA2 = C0125h.a().a(((Object) r2) + "_ExtendedReplay");
        if (aIVarA2 == null) {
            aIVarA2 = new H.d(r2);
            C0125h.a().a(aIVarA2);
        }
        for (int i2 = 0; i2 < arrayListA.size(); i2++) {
            try {
                m2 = (M) arrayListA.get(i2);
                String strF = m2.f();
                if (strF.equals("replayConfigTable")) {
                    aM aMVarC = r2.c(m2.e());
                    if (aMVarC == null) {
                        b(m2, "Replay disabled. Not Found: " + m2.e());
                    } else if (aMVarC.i().equals(ControllerParameter.PARAM_CLASS_ARRAY)) {
                        r2.O().q(m2.e());
                    } else {
                        a(m2, "replayConfigTable must be of type array");
                    }
                } else if (strF.equals("replayStartOffset")) {
                    r2.O().f(di.a(r2, m2.e()));
                } else if (strF.equals("replayRecordLength")) {
                    r2.O().g(di.a(r2, m2.e()));
                } else if (!G.F.a(strF)) {
                    C0069bb c0069bbA = zEquals ? a(m2, r2, iA, iA2, aIVarA2) : a(m2, r2, iA, iA2, aIVarA);
                    boolean z2 = r2.O() == null || r2.O().e();
                    c0069bbA.a(false);
                    if (str.equals("Replay")) {
                        r2.a(c0069bbA);
                    } else if (str.equals("ExtendedReplay")) {
                        r2.b(c0069bbA);
                    } else {
                        a(m2, "Unknown Replay Section: " + str);
                    }
                    if (!c0069bbA.b().equals("formula")) {
                        iA = c0069bbA.a() + c0069bbA.l();
                        iA2 = c0069bbA.a();
                    }
                }
            } catch (Exception e2) {
                a(m2, "Malformed [Replay] entry");
            }
        }
        for (int i3 = 0; i3 < arrayListA.size(); i3++) {
            try {
                m2 = (M) arrayListA.get(i3);
                String strF2 = m2.f();
                if (!G.F.a(strF2) && !strF2.equals("replayConfigTable") && !strF2.equals("replayStartOffset") && !strF2.equals("replayRecordLength")) {
                    C0069bb c0069bbI = zEquals ? r2.i(strF2) : r2.h(strF2);
                    if (c0069bbI == null || c0069bbI.k() != null) {
                    }
                    if (c0069bbI.g() instanceof bQ) {
                        a(r2, m2, ((bQ) c0069bbI.g()).c(), true);
                    }
                    if (c0069bbI.j() instanceof bQ) {
                        a(r2, m2, ((bQ) c0069bbI.j()).c(), true);
                    }
                }
            } catch (Exception e3) {
                a(m2, "Malformed [Replay] entry");
            }
        }
    }

    private void f(G.R r2, J j2) {
        ArrayList arrayListA = a(j2, r2, "GaugeConfigurations");
        String strB = null;
        M m2 = null;
        for (int i2 = 0; i2 < arrayListA.size(); i2++) {
            try {
                m2 = (M) arrayListA.get(i2);
                a(m2);
                if (m2.b().startsWith("gaugeCategory")) {
                    strB = bH.W.b(m2.e(), PdfOps.DOUBLE_QUOTE__TOKEN, "");
                } else {
                    C0048ah c0048ahJ = j(r2, m2);
                    c0048ahJ.d(strB);
                    if (!b(r2, c0048ahJ.i())) {
                        b(m2, "[GaugeConfigurations] gauge template assigned to undefined OutputChannel: " + c0048ahJ.i());
                    }
                    r2.a(c0048ahJ);
                }
            } catch (Exception e2) {
                b(m2, e2.getMessage() + ", Gauge Entry ignored.");
            }
        }
    }

    private boolean b(G.R r2, String str) {
        return str != null && (r2.g(str) != null || str.equals("veTuneValue"));
    }

    private void g(G.R r2, J j2) {
        ArrayList arrayListA = a(j2, r2, "TuningViews");
        C0180f c0180f = new C0180f(r2, f2049c, j2.d());
        int i2 = 0;
        Iterator it = arrayListA.iterator();
        while (it.hasNext()) {
            M m2 = (M) it.next();
            String strF = m2.f();
            if (strF.equals("tuningView")) {
                String[] strArrJ = j(q(m2.e()));
                if (strArrJ.length < 4) {
                    b(m2, "Invalid Entry! tuningView requires 4 parameters.\nFormat:\n   tuningView = referenceName, \"User Title\", [md5 of decoded data], {optional enableCondition}");
                } else {
                    try {
                        C0073bf c0073bf = new C0073bf(c0180f);
                        int i3 = 0 + 1;
                        c0073bf.v(strArrJ[0]);
                        int i4 = i3 + 1;
                        c0073bf.c(f(strArrJ[i3]));
                        int i5 = i4 + 1;
                        c0073bf.b(strArrJ[i4]);
                        int i6 = i5 + 1;
                        String strTrim = strArrJ[i5].trim();
                        if (strTrim.startsWith(VectorFormat.DEFAULT_PREFIX)) {
                            c0073bf.u(bH.W.b(bH.W.b(strTrim, VectorFormat.DEFAULT_PREFIX, ""), "}", ""));
                        }
                        int i7 = i2;
                        i2++;
                        c0073bf.a(i7);
                        r2.a(c0073bf);
                    } catch (Exception e2) {
                        b(m2, "Invalid TuningView Entry! Unable to parse: " + e2.getLocalizedMessage());
                    }
                }
            } else {
                b(m2, "Invalid Entry! Unknown entry: " + strF);
            }
        }
    }

    private void d(G.R r2, J j2, String str) {
        Iterator it = a(j2, r2, "EncodedData").iterator();
        while (it.hasNext()) {
            M m2 = (M) it.next();
            String strF = m2.f();
            if (str != null && strF.equals(str)) {
                String strE = m2.e();
                if (strE == null || strE.isEmpty()) {
                    b(m2, "Now Encoded Data found for: " + strF);
                } else {
                    bO bOVar = new bO(strF);
                    bOVar.a(strE);
                    r2.a(bOVar);
                }
            }
        }
    }

    private void h(G.R r2, J j2) {
        ArrayList arrayListA = a(j2, r2, "DatalogViews");
        C0141x c0141x = new C0141x();
        c0141x.a(Action.DEFAULT);
        int i2 = 0 + 1;
        c0141x.a(0);
        boolean z2 = false;
        Iterator it = arrayListA.iterator();
        while (it.hasNext()) {
            M m2 = (M) it.next();
            String strF = m2.f();
            if (strF.startsWith("graph")) {
                try {
                    String strF2 = f(m2.e());
                    boolean z3 = false;
                    Iterator it2 = r2.g().iterator();
                    while (true) {
                        if (!it2.hasNext()) {
                            break;
                        } else if (((C0043ac) it2.next()).aJ().equals(strF2)) {
                            z3 = true;
                            break;
                        }
                    }
                    if (!z3) {
                        b(m2, "No field by this name found in the [Datalog] section.");
                    }
                    String strSubstring = strF.substring(strF.indexOf(PdfOps.h_TOKEN) + 1);
                    c0141x.a(new C0142y(strF2, Integer.parseInt(strSubstring.substring(0, strSubstring.indexOf("."))), Integer.parseInt(strSubstring.substring(strSubstring.indexOf(".") + 1))));
                    if (!z2 && c0141x.a().equals(Action.DEFAULT) && !c0141x.b().isEmpty()) {
                        r2.a(c0141x);
                        z2 = true;
                    }
                } catch (Exception e2) {
                    b(m2, "Invalid graph definition. Should be format: graphX.Y  Example: graph0.1");
                }
            } else if (strF.equals("logViewName")) {
                String strF3 = f(m2.e());
                c0141x = new C0141x();
                int i3 = 1;
                if (r2.B(strF3) != null) {
                    b(m2, "Each Data logViewName must be unique!");
                    do {
                        int i4 = i3;
                        i3++;
                        strF3 = strF3 + i4;
                    } while (r2.B(strF3) != null);
                }
                c0141x.a(strF3);
                int i5 = i2;
                i2++;
                c0141x.a(i5);
                r2.a(c0141x);
            } else {
                b(m2, "Syntax Error. Do not know how to handle this line.");
            }
        }
    }

    private void i(G.R r2, J j2) {
        ArrayList arrayListA = a(j2, r2, "Datalog");
        ArrayList arrayList = new ArrayList();
        M m2 = null;
        int iA = 0;
        int iA2 = 0;
        String strF = null;
        for (int i2 = 0; i2 < arrayListA.size(); i2++) {
            try {
                m2 = (M) arrayListA.get(i2);
                String strB = m2.b();
                if (!strB.startsWith("delimiter")) {
                    if (strB.startsWith("internalLogField")) {
                        C0052al c0052alA = a(m2, (aI) r2, iA, iA2);
                        r2.a(c0052alA);
                        iA = c0052alA.a() + c0052alA.l();
                        iA2 = c0052alA.a();
                    } else if (!strB.startsWith("defaultExtension")) {
                        if (strB.startsWith("entry")) {
                            C0043ac c0043acK = k(r2, m2);
                            if (this.f2050j && arrayList.contains(c0043acK.b()) && (c0043acK.aH() == null || c0043acK.aH().isEmpty())) {
                                b(m2, "Duplicate Data Log Field naming, the header " + c0043acK.b() + "has already been defined.");
                            }
                            if (r2.g(c0043acK.a()) != null || C0113cs.a().e(c0043acK.a())) {
                                if (this.f2050j) {
                                    arrayList.add(c0043acK.b());
                                }
                                r2.a(c0043acK);
                            } else {
                                a(m2, "DataLog entry references non existent OutputChannel, ignored.");
                            }
                            if (c0043acK.aH() != null && !c0043acK.aH().equals("")) {
                                a(r2, m2, c0043acK.aH(), false);
                            }
                            if (strF != null) {
                                c0043acK.e(strF);
                            }
                        } else if (m2.f().equals("category")) {
                            strF = f(m2.e());
                            if (strF.length() > 34) {
                                b(m2, "DataLogField category cannot be more than 34 in length. Truncating: " + strF);
                                strF = strF.substring(0, 33);
                            }
                        } else {
                            b(m2, "Unrecognized DataLog entry.");
                        }
                    }
                }
            } catch (Exception e2) {
                a(m2, e2.getMessage() + "\nDatalog Entry ignored.");
            }
        }
    }

    private void c(G.R r2, bD bDVar, J j2) {
        ArrayList arrayListA = a(j2, r2, "FTPBrowser");
        M m2 = null;
        C0081bn c0081bn = null;
        for (int i2 = 0; i2 < arrayListA.size(); i2++) {
            try {
                m2 = (M) arrayListA.get(i2);
                String strF = m2.f();
                String strE = m2.e();
                if (strF.equals("ftpBrowser")) {
                    c0081bn = new C0081bn();
                    String[] strArrJ = j(q(strE));
                    c0081bn.v(strArrJ[0]);
                    c0081bn.s(strArrJ[1]);
                    if (strArrJ.length > 2) {
                        c0081bn.u(strArrJ[2]);
                    }
                    bDVar.a(c0081bn);
                } else if (strF.equals("host")) {
                    c0081bn.a(q(strE));
                } else if (strF.equals(DeploymentDescriptorParser.ATTR_PORT)) {
                    c0081bn.a(bH.W.g(strE));
                } else if (strF.equals("user")) {
                    c0081bn.b(strE);
                } else if (strF.equals("password")) {
                    c0081bn.c(strE);
                } else if (strF.equals("passiveMode")) {
                    c0081bn.a(strE.equalsIgnoreCase("true"));
                } else if (strF.equals("browseEnable")) {
                    String[] strArrJ2 = j(q(strE));
                    c0081bn.a(bH.W.b(bH.W.b(strArrJ2[1].trim(), VectorFormat.DEFAULT_PREFIX, ""), "}", ""), f(strArrJ2[0]));
                } else if (strF.equals("readWriteEnable")) {
                    String[] strArrJ3 = j(q(strE));
                    String strF2 = f(strArrJ3[0]);
                    String strB = bH.W.b(bH.W.b(strArrJ3[1].trim(), VectorFormat.DEFAULT_PREFIX, ""), "}", "");
                    a(r2, m2, strB, true);
                    c0081bn.b(strB, strF2);
                } else {
                    b(m2, "Unrecognized DataLog entry.");
                }
            } catch (NullPointerException e2) {
                a(m2, "ftpBrowser must be defined before other attributes!\nftpBrowser = referenceName, \"Title\", enableCondition");
            } catch (Exception e3) {
                a(m2, e3.getMessage() + "\nEntry ignored.");
            }
        }
    }

    private void j(G.R r2, J j2) throws V.g {
        ArrayList arrayListA = a(j2, r2, "TableEditor");
        C0072be c0072be = null;
        C0076bi c0076bi = null;
        for (int i2 = 0; i2 < arrayListA.size(); i2++) {
            M m2 = (M) arrayListA.get(i2);
            String strB = m2.b();
            try {
                if (strB.startsWith("table")) {
                    c0072be = new C0072be();
                    c0072be.q(this.f2053l);
                    c0076bi = new C0076bi();
                    String[] strArrJ = j(q(strB));
                    c0072be.v(strArrJ[0]);
                    c0076bi.a(strArrJ[0]);
                    c0076bi.v(strArrJ[1]);
                    c0072be.d(cY.a().a(r2, strArrJ[2]));
                    c0076bi.s(strArrJ[2]);
                    r2.a(c0072be);
                    r2.a(c0076bi);
                    if (r2.e() != null) {
                        r2.e().a(c0072be);
                    }
                } else if (strB.startsWith("xBins")) {
                    if (c0072be == null) {
                        throw new V.g("Error in [TableEditor] Section, 1st row should be table = XXX, found \n" + ((Object) m2));
                    }
                    String[] strArrJ2 = j(q(strB));
                    c0072be.a(strArrJ2[0]);
                    c0072be.d(strArrJ2[1]);
                    c0072be.f(strArrJ2[1].toUpperCase());
                    if (r2.c(c0072be.a()) == null) {
                        b(m2, "Table: " + c0072be.aJ() + ", Assigned xBin not found: " + c0072be.a());
                    }
                    if (c0072be.d() != null && c0072be.d().length() > 0 && r2.g(c0072be.d()) == null) {
                        b(m2, "Table: " + c0072be.aJ() + ", Assigned xBin Channel not found: " + c0072be.d());
                    }
                    if (strArrJ2.length > 2 && strArrJ2[2].equals("readOnly")) {
                        c0072be.b(true);
                    }
                } else if (strB.startsWith("yBins")) {
                    if (c0072be == null) {
                        throw new V.g("Error in [TableEditor] Section, 1st row should be table = XXX, found \n" + ((Object) m2));
                    }
                    String[] strArrJ3 = j(q(strB));
                    c0072be.b(strArrJ3[0]);
                    c0072be.e(strArrJ3[1]);
                    c0072be.g(strArrJ3[1].toUpperCase());
                    if (r2.c(c0072be.b()) == null) {
                        b(m2, "Table: " + c0072be.aJ() + ", Assigned y Bin not found: " + c0072be.b());
                    }
                    if (c0072be.f() != null && c0072be.f().length() > 0 && r2.g(c0072be.f()) == null) {
                        b(m2, "Table: " + c0072be.aJ() + ", Assigned y Bin Channel not found: " + c0072be.f());
                    }
                    if (strArrJ3.length > 2 && strArrJ3[2].equals("readOnly")) {
                        c0072be.c(true);
                    }
                } else if (strB.startsWith("zBins")) {
                    if (c0072be == null) {
                        throw new V.g("Error in [TableEditor] Section, 1st row should be table = XXX, found \n" + ((Object) m2));
                    }
                    c0072be.c(q(strB));
                    if (r2.c(c0072be.c()) == null) {
                        b(m2, "Table: " + c0072be.aJ() + ", Assigned y Bin not found: " + c0072be.c());
                    }
                } else if (strB.startsWith("upDownLabel")) {
                    if (c0072be == null) {
                        throw new V.g("Error in [TableEditor] Section, 1st row should be table = XXX, found \n" + ((Object) m2));
                    }
                    c0072be.a(j(q(strB)));
                } else if (strB.startsWith("gridOrient")) {
                    if (c0072be == null) {
                        throw new V.g("Error in [TableEditor] Section, 1st row should be table = XXX, found \n" + ((Object) m2));
                    }
                    String[] strArrJ4 = j(q(strB));
                    c0076bi.b((360 - bH.W.g(strArrJ4[0])) + 90);
                    c0076bi.a(360 - bH.W.g(strArrJ4[2]));
                } else if (strB.startsWith("topicHelp")) {
                    if (c0072be == null) {
                        throw new V.g("Error in [TableEditor] Section, 1st row should be table = XXX, found \n" + ((Object) m2));
                    }
                    cZ cZVarA = cY.a().a(r2, q(strB));
                    c0072be.e(cZVarA);
                    if (c0076bi != null) {
                        c0076bi.e(cZVarA);
                    }
                    String strF = f(q(strB));
                    if (strF.startsWith("http:/") || strF.startsWith("file:/")) {
                        C0050aj c0050aj = new C0050aj();
                        c0050aj.a(cZVarA);
                        c0050aj.b(cZVarA);
                        if (c0072be.M() == null || c0072be.M().length() <= 0) {
                            c0050aj.b("Web Help");
                        } else {
                            c0050aj.b(c0072be.M());
                        }
                        r2.e().a(c0050aj);
                    }
                } else if (strB.startsWith("xyLabels")) {
                    String[] strArrJ5 = j(q(strB));
                    if (strArrJ5.length > 2) {
                        b(m2, "Extra attributes, ignoring.");
                    }
                    if (strArrJ5.length > 0) {
                        c0072be.a(cY.a().a(r2, strArrJ5[0]));
                    }
                    if (strArrJ5.length > 1) {
                        c0072be.b(cY.a().a(r2, strArrJ5[1]));
                    }
                } else if (m2.f().equals("userPassword")) {
                    String strE = m2.e();
                    aM aMVarC = r2.c(strE);
                    if (aMVarC == null || aMVarC.y() < 4) {
                        b(m2, "Invalid value for userPassword Parameter. Must be the name of a defined U32 Constant. Invalid Value: " + strE);
                    } else {
                        c0072be.z(strE);
                        r2.c(true);
                    }
                } else if (!strB.startsWith("gridHeight")) {
                    b(m2, "Do not understand row, no known keyword.");
                }
            } catch (V.g e2) {
                b(m2, e2.getMessage());
            } catch (Exception e3) {
                e3.printStackTrace();
                a(m2, "Error in [TableEditor] Section, I don't know how to handle this line.");
                throw new V.g("Error in [TableEditor] Section, I don't know how to handle this line.");
            }
        }
        ArrayList arrayListA2 = a(j2, r2, "Tuning");
        ArrayList arrayList = new ArrayList();
        for (int i3 = 0; i3 < arrayListA2.size(); i3++) {
            String strB2 = ((M) arrayListA2.get(i3)).b();
            try {
                if (strB2.startsWith("gauge") && !strB2.startsWith("gaugeColumns")) {
                    arrayList.add(j(q(strB2))[0]);
                }
            } catch (Exception e4) {
                b((M) arrayListA2.get(i3), "Unable to load Table Gauges");
            }
        }
        String[] strArr = new String[arrayList.size()];
        for (int i4 = 0; i4 < arrayList.size(); i4++) {
            strArr[i4] = (String) arrayList.get(i4);
        }
        if (strArr.length > 0) {
            Iterator itO = r2.o();
            while (itO.hasNext()) {
                r2.a(((C0076bi) itO.next()).aJ(), strArr);
            }
            r2.a(Action.DEFAULT, strArr);
        }
    }

    public void a(G.R r2, J j2) throws V.g {
        ArrayList arrayListA = a(a(j2, r2, "VerbiageOverride"), r2);
        M m2 = null;
        for (int i2 = 0; i2 < arrayListA.size(); i2++) {
            try {
                m2 = (M) arrayListA.get(i2);
                r2.c(bH.W.b(m2.f(), PdfOps.DOUBLE_QUOTE__TOKEN, ""), bH.W.b(m2.e(), PdfOps.DOUBLE_QUOTE__TOKEN, ""));
            } catch (Exception e2) {
                b(m2, "Verbiage Over-ride not formatted properly, ignoring.");
                return;
            }
        }
    }

    public void b(G.R r2, J j2) throws V.g {
        ArrayList arrayListA = a(a(j2, r2, "VeAnalyze"), r2);
        dk dkVarA = null;
        dc dcVar = null;
        for (int i2 = 0; i2 < arrayListA.size(); i2++) {
            M m2 = (M) arrayListA.get(i2);
            String strB = m2.b();
            try {
                if (strB.startsWith("veAnalyzeMap")) {
                    dkVarA = a(r2, new dk(), j(q(strB)));
                    r2.a(dkVarA);
                    dcVar = null;
                } else if (strB.startsWith("trimAnalyzeMap")) {
                    dcVar = (dc) a(r2, new dc(), j(q(strB)));
                    r2.a(dcVar);
                    dkVarA = dcVar;
                } else if (strB.startsWith("trimTable")) {
                    if (dcVar == null) {
                        b(m2, "Found trimTable, but trimAnalyzeMap must be defined 1st.");
                    } else {
                        dcVar.a(h(r2, m2));
                    }
                } else if (strB.startsWith("filter")) {
                    dkVarA.a(i(r2, m2));
                } else if (strB.startsWith("option")) {
                    try {
                        dkVarA.n(m2.e());
                    } catch (V.g e2) {
                        b(m2, e2.getMessage());
                    }
                } else if (strB.startsWith("lambdaTargetTables")) {
                    for (String str : j(q(strB))) {
                        dkVarA.i(str.trim());
                    }
                } else if (strB.startsWith("lambdaTargetChannels")) {
                    for (String str2 : j(q(strB))) {
                        dkVarA.j(str2.trim());
                    }
                } else if (strB.startsWith("pickListSelection")) {
                    for (String str3 : j(q(strB))) {
                        dkVarA.t(str3.trim());
                    }
                } else if (strB.startsWith("lambdaChannels")) {
                    for (String str4 : j(q(strB))) {
                        dkVarA.k(str4.trim());
                    }
                } else if (strB.startsWith("egoCorrectionChannels")) {
                    for (String str5 : j(q(strB))) {
                        dkVarA.l(str5.trim());
                    }
                } else if (strB.startsWith("defaultLambdaTarget")) {
                    dkVarA.s(j(q(strB))[0]);
                } else if (strB.startsWith("zAxisTransform")) {
                    dkVarA.a(Float.parseFloat(m2.e()));
                } else {
                    b(m2, "Unrecognised row in [VeAnaltyzeMaps] section. I don't know how to handle this and will ignore");
                }
            } catch (Exception e3) {
                a(m2, "Error in section [VeAnalyzeMaps], unable to parse ini row. Error: " + e3.getLocalizedMessage());
            }
        }
    }

    public void c(G.R r2, J j2) throws V.g {
        ArrayList arrayListA = a(a(j2, r2, "WueAnalyze"), r2);
        dn dnVarA = null;
        for (int i2 = 0; i2 < arrayListA.size(); i2++) {
            M m2 = (M) arrayListA.get(i2);
            String strB = m2.b();
            try {
                if (strB.startsWith("wueAnalyzeMap")) {
                    dnVarA = a(r2, j(q(strB)));
                    r2.a(dnVarA);
                } else if (strB.startsWith("filter")) {
                    dnVarA.a(i(r2, m2));
                } else if (strB.startsWith("option")) {
                    try {
                        dnVarA.k(m2.e());
                    } catch (V.g e2) {
                        b(m2, e2.getMessage());
                    }
                } else if (strB.startsWith("lambdaTargetTables")) {
                    for (String str : j(q(strB))) {
                        dnVarA.j(str);
                    }
                } else if (strB.startsWith("wuePercentOffset")) {
                    dnVarA.a(Float.parseFloat(m2.e()));
                } else {
                    b(m2, "Unrecognised row in [WueAnaltyzeMaps] section. I don't know how to handle this and will ignore");
                }
            } catch (V.g e3) {
                a(m2, "Error in section [WueAnalyzeMaps]: " + e3.getLocalizedMessage());
            } catch (Exception e4) {
                a(m2, "Error in section [WueAnalyzeMaps], unable to parse ini row");
            }
        }
    }

    private dd h(G.R r2, M m2) {
        dd ddVar = new dd();
        String[] strArrJ = j(m2.e());
        if (strArrJ.length < 3) {
            a(m2, "trimTable requires 3 parameters: trimTable = tableName,  label, afrChannel, egoCorr, enableCondition(optional), or trimTable = tableName, label, egoSensorIndexEpression,  enableCondition");
        } else {
            ddVar.a(strArrJ[0]);
            ddVar.e(f(strArrJ[1]));
            if (strArrJ[2].trim().startsWith(VectorFormat.DEFAULT_PREFIX)) {
                ddVar.f(bH.W.b(bH.W.b(strArrJ[2], VectorFormat.DEFAULT_PREFIX, ""), "}", ""));
                if (strArrJ.length > 3) {
                    ddVar.d(bH.W.b(bH.W.b(strArrJ[3], VectorFormat.DEFAULT_PREFIX, ""), "}", ""));
                }
            } else {
                ddVar.b(strArrJ[2]);
                ddVar.c(strArrJ[3]);
                if (r2.g(ddVar.c()) == null) {
                    b(m2, "Trim Table lambda/afr channel " + ddVar.c() + " not found!");
                }
                if (r2.g(ddVar.d()) == null) {
                    b(m2, "Trim Table EGO Correction channel " + ddVar.d() + " not found!");
                }
                if (strArrJ.length > 4) {
                    ddVar.d(bH.W.b(bH.W.b(strArrJ[4], VectorFormat.DEFAULT_PREFIX, ""), "}", ""));
                }
            }
        }
        return ddVar;
    }

    private aW i(G.R r2, M m2) {
        String[] strArrJ = j(q(m2.b()));
        aW aWVar = new aW();
        if ("std_Expression".equals(strArrJ[0])) {
            aWVar.a(256);
            aWVar.v(strArrJ[0]);
            aWVar.d(bH.W.b(strArrJ[1], PdfOps.DOUBLE_QUOTE__TOKEN, ""));
            String strB = bH.W.b(bH.W.b(strArrJ[2], VectorFormat.DEFAULT_PREFIX, ""), "}", "");
            aWVar.e(strB);
            aWVar.a(Boolean.parseBoolean(strArrJ[3]));
            a(r2, m2, strB, false);
            return aWVar;
        }
        if (aW.c(strArrJ[0])) {
            aWVar.a(256);
            aWVar.v(strArrJ[0]);
            return aWVar;
        }
        aWVar.v(strArrJ[0]);
        aWVar.d(bH.W.b(strArrJ[1], PdfOps.DOUBLE_QUOTE__TOKEN, ""));
        aWVar.a(strArrJ[2]);
        aWVar.b(strArrJ[3]);
        try {
            aWVar.a(di.a(r2, strArrJ[4]));
        } catch (V.g e2) {
            b(m2, "Failed to parse qualifying value or expression.");
        }
        aWVar.a(Boolean.parseBoolean(strArrJ[5]));
        return aWVar;
    }

    private dk a(G.R r2, dk dkVar, String[] strArr) throws V.g {
        dkVar.b(strArr[0]);
        dkVar.c(strArr[1]);
        dkVar.m(strArr[1]);
        if (strArr.length <= 2) {
            throw new V.g("Error loading VeAnalyzeMap for " + dkVar.b() + " There is no lambdaChannel defined.");
        }
        dkVar.e(strArr[2]);
        if (strArr.length > 3) {
            dkVar.f(strArr[3]);
        }
        if (strArr.length > 4) {
            dkVar.d(bH.W.b(bH.W.b(strArr[4], VectorFormat.DEFAULT_PREFIX, ""), "}", ""));
        }
        C0072be c0072be = (C0072be) r2.e().c(dkVar.b());
        if (c0072be == null) {
            throw new V.g("Error loading VeAnalyzeMap, the table " + dkVar.b() + " not found in current configuration. Was it loaded yet?");
        }
        C0072be c0072be2 = (C0072be) r2.e().c(dkVar.c());
        if (c0072be2 == null && !dkVar.c().endsWith("afrTSCustom")) {
            throw new V.g("Error loading VeAnalyzeMap, the table " + dkVar.b() + " was defined as the Lambda Target Table, but not found in current configuration. Was it loaded yet?");
        }
        if (!dkVar.c().endsWith("afrTSCustom")) {
            if (c0072be2.d() != null) {
                dkVar.p(c0072be2.d());
            }
            if (c0072be2.f() != null) {
                dkVar.q(c0072be2.f());
            }
        }
        dkVar.g(c0072be.d());
        dkVar.h(c0072be.f());
        return dkVar;
    }

    private dn a(G.R r2, String[] strArr) throws V.g {
        dn dnVar = new dn();
        dnVar.c(strArr[0]);
        dnVar.g(strArr[1]);
        dnVar.f(strArr[2]);
        dnVar.o(strArr[2]);
        if (strArr.length <= 3) {
            throw new V.g("Error loading VeAnalyzeMap for " + dnVar.c() + " There is no lambdaChannel defined.");
        }
        dnVar.d(strArr[3]);
        if (strArr.length <= 4) {
            throw new V.g("Error loading VeAnalyzeMap for " + dnVar.c() + " There is no Coolant Temp Channel defined.");
        }
        dnVar.e(strArr[4]);
        if (strArr.length <= 5) {
            throw new V.g("Error loading VeAnalyzeMap for " + dnVar.c() + " There is no Warmup Enrichment Channel defined.");
        }
        dnVar.m(strArr[5]);
        if (strArr.length > 6) {
            dnVar.h(strArr[6]);
        }
        if (strArr.length > 7) {
            dnVar.i(bH.W.b(bH.W.b(strArr[7], VectorFormat.DEFAULT_PREFIX, ""), "}", ""));
        }
        C0079bl c0079bl = (C0079bl) r2.e().c(dnVar.c());
        if (c0079bl == null) {
            throw new V.g("Error loading WueAnalyzeMap, the CurveGraph " + dnVar.c() + " not found in current configuration. Was it loaded yet?");
        }
        C0072be c0072be = (C0072be) r2.e().c(dnVar.f());
        if (c0072be == null && !dnVar.f().endsWith("afrTSCustom")) {
            throw new V.g("Error loading WueAnalyzeMap, the Curve Graph " + dnVar.c() + " was defined as the Lambda Target Table, but not found in current configuration. Was it loaded yet?");
        }
        if (!dnVar.f().endsWith("afrTSCustom")) {
            if (c0072be.d() != null) {
                dnVar.p(c0072be.d());
            }
            if (c0072be.f() != null) {
                dnVar.q(c0072be.f());
            }
        }
        dnVar.a(c0079bl.b(0));
        dnVar.b(c0079bl.d(0));
        if (c0079bl.d() < 2) {
            throw new V.g("Error loading WueAnalyzeMap, the Curve Graph " + dnVar.c() + " does not have a wue Analyze working array defined as a second curve.");
        }
        dnVar.n(c0079bl.b(1));
        dnVar.e(c0079bl.l());
        return dnVar;
    }

    private void k(G.R r2, J j2) {
        ArrayList arrayListA = a(j2, r2, "FrontPage");
        ArrayList arrayList = new ArrayList();
        for (int i2 = 0; i2 < arrayListA.size(); i2++) {
            M m2 = (M) arrayListA.get(i2);
            String strB = m2.b();
            if (strB.startsWith("gauge")) {
                String[] strArrJ = j(q(strB));
                if (strArrJ.length < 1 || r2.k(strArrJ[0]) == null) {
                    b(m2, "Attempt to add undefined Gauge to Front Page.");
                } else {
                    arrayList.add(strArrJ[0]);
                }
            } else if (strB.startsWith("indicatorTemplate")) {
                if (e(strB)) {
                    C0051ak c0051akA = a(m2, r2);
                    c0051akA.a(false);
                    r2.a(c0051akA);
                }
            } else if (strB.startsWith("indicator")) {
                if (e(strB)) {
                    r2.a(a(m2, r2));
                }
            } else if (!strB.startsWith("egoLEDs")) {
                b(m2, "Do not understand this line.");
            }
        }
        if (arrayList.size() < 1) {
            return;
        }
        String[] strArr = new String[arrayList.size()];
        for (int i3 = 0; i3 < arrayList.size(); i3++) {
            strArr[i3] = (String) arrayList.get(i3);
        }
        r2.a(strArr);
    }

    private boolean e(String str) {
        return true;
    }

    private void l(G.R r2, J j2) throws V.g {
        Iterator it = a(j2, r2, "EventTriggers").iterator();
        while (it.hasNext()) {
            M m2 = (M) it.next();
            try {
                if (m2.f().equals("timedPageRefresh")) {
                    String[] strArrJ = j(m2.e());
                    if (strArrJ.length == 2 && bH.H.a(strArrJ[0])) {
                        new K.f(r2, bH.W.g(strArrJ[0]) - 1, di.a(r2, strArrJ[1])).a();
                    } else {
                        b(m2, "timedPageRefresh must have 2 numeric parameters: timedPageRefresh = [pageToRefresh], \n[timePeriodBetweenRefreshesInMs]");
                    }
                } else if (m2.f().equals("triggeredPageRefresh")) {
                    String[] strArrJ2 = j(m2.e());
                    if (strArrJ2.length != 2) {
                        b(m2, "triggeredPageRefresh must have 2 parameters: triggeredPageRefresh = [pageToRefresh], \n{[Expression To Trigger Page Read]}");
                    } else {
                        int iG = bH.W.g(strArrJ2[0]) - 1;
                        String strB = bH.W.b(bH.W.b(strArrJ2[1], VectorFormat.DEFAULT_PREFIX, ""), "}", "");
                        a(r2, m2, strB, true);
                        new K.h(r2, iG, strB);
                    }
                } else {
                    b(m2, "Entry in [EventTriggers] not understood, ignoring.");
                }
            } catch (Exception e2) {
                a(m2, "Error loading [EventTriggers], " + e2.getMessage());
                e2.printStackTrace();
                throw new V.g("Critical Error found at or near ini entry:\n" + ((Object) m2));
            }
        }
    }

    private void m(G.R r2, J j2) throws V.g {
        int iG = -1;
        aM aMVar = null;
        Iterator it = a(j2, r2, "Constants").iterator();
        while (it.hasNext()) {
            M m2 = (M) it.next();
            try {
                if (m2.f().equals("page")) {
                    iG = bH.W.g(m2.e()) - 1;
                    aMVar = null;
                } else if (!G.F.a(m2.f())) {
                    if (iG >= 0) {
                        aM aMVarA = a(r2, m2, iG, aMVar);
                        aMVarA.e(r2.O().I());
                        r2.a(aMVarA);
                        aMVar = aMVarA;
                    } else {
                        b(m2, "Entry in [Constants] Section before page assignment, ignoring.");
                    }
                }
            } catch (V.g e2) {
                a(m2, "Error loading [Constant], " + e2.getMessage());
                e2.printStackTrace();
                throw new V.g("Critical Error found at or near ini entry:\n" + ((Object) m2) + "\nDetails: " + e2.getLocalizedMessage());
            } catch (Exception e3) {
                a(m2, "Error loading [Constant], " + e3.getMessage());
                e3.printStackTrace();
                throw new V.g("Critical Error found at or near ini entry:\n" + ((Object) m2));
            }
        }
    }

    private void n(G.R r2, J j2) throws V.g {
        Iterator it = a(j2, r2, "PcVariables").iterator();
        while (it.hasNext()) {
            M m2 = (M) it.next();
            try {
                r2.a(l(r2, m2));
            } catch (V.g e2) {
                a(m2, "Error loading [PcVariables], " + e2.getMessage());
            } catch (Exception e3) {
                a(m2, "Error loading [PcVariables], " + e3.getMessage());
                e3.printStackTrace();
                throw new V.g("Critical Error found at or near ini entry:\n" + ((Object) m2));
            }
        }
    }

    private void o(G.R r2, J j2) {
        C0096cb c0096cb = null;
        C0098cd c0098cd = null;
        Iterator it = a(j2, r2, "LoggerDefinition").iterator();
        while (it.hasNext()) {
            M m2 = (M) it.next();
            try {
                String strF = m2.f();
                if (strF.equals("loggerDef")) {
                    if (c0096cb != null && c0096cb.n().equals("UDP_Stream") && c0096cb.o() <= 0) {
                        a(m2, c0096cb.g() + " is defineds as ProcessorType UDP_Stream, but no slave port has been defined.");
                    }
                    c0096cb = new C0096cb();
                    String[] strArrJ = j(m2.e());
                    c0096cb.c(f(strArrJ[1]));
                    c0096cb.a(strArrJ[2]);
                    r2.a(c0096cb);
                } else if (strF.equals("dataReadCommand")) {
                    c0096cb.d(f(m2.e()));
                } else if (strF.equals("startCommand")) {
                    c0096cb.e(f(m2.e()));
                } else if (strF.equals("stopCommand")) {
                    c0096cb.f(f(m2.e()));
                } else if (strF.equals("dataLength")) {
                    c0096cb.c(bH.W.g(m2.e()));
                } else if (strF.equals("continuousRead")) {
                    c0096cb.a(Boolean.parseBoolean(m2.e()));
                } else if (strF.equals("dataReadyCondition")) {
                    c0096cb.b(bH.W.b(bH.W.b(m2.e(), VectorFormat.DEFAULT_PREFIX, ""), "}", ""));
                } else if (strF.equals("dataReadTimeout")) {
                    c0096cb.a(bH.W.g(m2.e()));
                } else if (strF.equals("dataLength")) {
                    bH.W.g(m2.e());
                } else if (strF.equals("logProcessorType")) {
                    c0096cb.g(m2.e());
                } else if (strF.equals("slavePort")) {
                    c0096cb.d(bH.W.g(m2.e()));
                } else if (strF.equals("recordFieldGenerator")) {
                    String[] strArrJ2 = j(m2.e());
                    if (strArrJ2.length <= 1) {
                        b(m2, "Field Generator type required.");
                    } else if (strArrJ2[0].equals("generateFromOutpcOffsets")) {
                        C0100cf c0100cf = new C0100cf();
                        c0100cf.a(r2);
                        if (strArrJ2.length > 1) {
                            c0100cf.a(strArrJ2[1]);
                        } else {
                            b(m2, "1D Array Constant containing Outpc offsets required.");
                        }
                        c0096cb.a(c0100cf);
                    }
                } else if (strF.equals("recordDef")) {
                    c0098cd = new C0098cd();
                    String[] strArrJ3 = j(m2.e());
                    c0098cd.e(bH.W.g(strArrJ3[0]));
                    c0098cd.f(bH.W.g(strArrJ3[1]));
                    c0098cd.d(bH.W.g(strArrJ3[2]));
                    c0096cb.a(c0098cd);
                } else if (strF.equals("recordField")) {
                    C0097cc c0097cc = new C0097cc();
                    String[] strArrJ4 = j(m2.e());
                    c0097cc.b(strArrJ4[0]);
                    c0097cc.a(cY.a().a(r2, bH.W.b(strArrJ4[1], PdfOps.DOUBLE_QUOTE__TOKEN, "")));
                    c0097cc.a(bH.W.g(strArrJ4[3]), bH.W.g(strArrJ4[2]));
                    c0097cc.a(di.a(r2, strArrJ4[4]));
                    int i2 = 5;
                    if (!strArrJ4[5].startsWith(PdfOps.DOUBLE_QUOTE__TOKEN)) {
                        i2 = 5 + 1;
                        c0097cc.b(di.a(r2, strArrJ4[5]));
                    }
                    int i3 = i2;
                    int i4 = i2 + 1;
                    c0097cc.c(f(strArrJ4[i3]));
                    if (strArrJ4.length > i4) {
                        i4++;
                        c0097cc.e(bH.W.b(bH.W.b(strArrJ4[i4], VectorFormat.DEFAULT_PREFIX, ""), "}", ""));
                    }
                    if (strArrJ4.length > i4) {
                        int i5 = i4;
                        int i6 = i4 + 1;
                        if (strArrJ4[i5].equals("hidden")) {
                            c0097cc.a(true);
                        }
                    }
                    c0098cd.a(c0097cc);
                } else if (strF.equals("headerField")) {
                    C0097cc c0097cc2 = new C0097cc();
                    String[] strArrJ5 = j(m2.e());
                    c0097cc2.b(strArrJ5[0]);
                    c0097cc2.a(cY.a().a(r2, bH.W.b(f(strArrJ5[1]), PdfOps.DOUBLE_QUOTE__TOKEN, "")));
                    c0097cc2.a(bH.W.g(strArrJ5[3]), bH.W.g(strArrJ5[2]));
                    c0097cc2.a(di.a(r2, strArrJ5[4]));
                    int i7 = 5;
                    if (!strArrJ5[5].startsWith(PdfOps.DOUBLE_QUOTE__TOKEN)) {
                        i7 = 5 + 1;
                        c0097cc2.b(di.a(r2, strArrJ5[5]));
                    }
                    int i8 = i7;
                    int i9 = i7 + 1;
                    c0097cc2.c(f(strArrJ5[i8]));
                    if (strArrJ5.length > i9) {
                        i9++;
                        c0097cc2.e(bH.W.b(bH.W.b(strArrJ5[i9], VectorFormat.DEFAULT_PREFIX, ""), "}", ""));
                    }
                    if (strArrJ5.length > i9) {
                        int i10 = i9;
                        int i11 = i9 + 1;
                        if (strArrJ5[i10].equals("hidden")) {
                            c0097cc2.a(true);
                        }
                    }
                    c0098cd.c(c0097cc2);
                } else if (strF.equals("calcField")) {
                    C0095ca c0095ca = new C0095ca();
                    String[] strArrJ6 = j(m2.e());
                    c0095ca.b(strArrJ6[0]);
                    c0095ca.a(cY.a().a(r2, bH.W.b(strArrJ6[1], PdfOps.DOUBLE_QUOTE__TOKEN, "")));
                    c0095ca.c(f(strArrJ6[2]));
                    c0095ca.a(bH.W.b(bH.W.b(strArrJ6[3], VectorFormat.DEFAULT_PREFIX, ""), "}", ""));
                    if (strArrJ6.length > 4 && strArrJ6[4].equals("hidden")) {
                        c0095ca.a(true);
                    }
                    c0098cd.a(c0095ca);
                } else if (strF.equals("stdLogger")) {
                    r2.l(m2.e());
                } else if (strF.equals("overlaidDatasetCount")) {
                    c0096cb.e(Integer.parseInt(m2.e()));
                } else if (strF.equals("defaultXAxis")) {
                    c0096cb.i(m2.e());
                } else if (strF.equals("verticalMarker")) {
                    String[] strArrJ7 = j(m2.e());
                    c0096cb.a(new C0101cg(f(strArrJ7[0]), f(strArrJ7[1]), f(strArrJ7[2]), Double.parseDouble(strArrJ7[3])));
                } else if (strF.equals("stopOnExit")) {
                    c0096cb.b(Boolean.parseBoolean(m2.e().trim()));
                } else {
                    b(m2, "Unkown entry in [LoggerDefinition], skipping");
                }
            } catch (V.g e2) {
                a(m2, "Error loading [LoggerDefinition], " + e2.getMessage());
            } catch (Exception e3) {
                a(m2, "Error loading [LoggerDefinition], " + e3.getMessage());
                e3.printStackTrace();
            }
        }
    }

    private void p(G.R r2, J j2) {
        Iterator it = a(j2, r2, "TurboBaud").iterator();
        while (it.hasNext()) {
            M m2 = (M) it.next();
            try {
                String strF = m2.f();
                if (strF.equals("turboBaudSpeed")) {
                    r2.O().a(di.a(r2, m2.e()));
                } else if (strF.equals("sdTurboActive")) {
                    r2.O().b(di.a(r2, m2.e()));
                } else if (strF.equals("fullTimeTurboEnabled")) {
                    r2.O().c(di.a(r2, m2.e()));
                } else if (strF.equals("runtimeTurboActive")) {
                    r2.O().d(di.a(r2, m2.e()));
                } else if (!G.F.a(strF)) {
                    b(m2, "Unknown Command");
                }
            } catch (Exception e2) {
                a(m2, "Error loading [TurboBaud], " + e2.getMessage());
                e2.printStackTrace();
            }
        }
    }

    private void q(G.R r2, J j2) {
        Iterator it = a(j2, r2, "Tools").iterator();
        while (it.hasNext()) {
            M m2 = (M) it.next();
            try {
                String strF = m2.f();
                bH bHVarB = b(m2);
                r2.a(true);
                if (bHVarB.aH() != null && bHVarB.aH().trim().length() > 0) {
                    a(r2, m2, bHVarB.aH(), false);
                }
                if (strF.equals("addTool")) {
                    C0088bu c0088buC = r2.e().c(bHVarB.a());
                    if (c0088buC == null) {
                        b(m2, "Target UI component " + bHVarB.a() + " is not in the currently loaded configuration.\nI can not add tool " + bHVarB.aJ() + ", This line will be ignored.");
                    } else if (bHVarB.aJ().equals("veTableGenerator") || bHVarB.aJ().equals("afrTableGenerator") || bHVarB.aJ().equals("Calculator") || bHVarB.aJ().equals("TwoPointCalculator")) {
                        c0088buC.a(bHVarB);
                    } else {
                        b(m2, "Tool " + bHVarB.aJ() + " is not supported by this version of application.\nI don't know how to add it, this line will be ignored.\nSupported Tools: veTableGenerator, afrTableGenerator, Calculator, TwoPointCalculator");
                    }
                } else if (strF.equals("removeTool")) {
                    C0088bu c0088buC2 = r2.e().c(bHVarB.a());
                    if (c0088buC2 == null) {
                        b(m2, "Target UI component " + bHVarB.a() + " is not in the currently loaded configuration.\nI can not remove tool " + bHVarB.aJ() + ", This line will be ignored.");
                    } else if (bHVarB.aJ().equals("veTableGenerator") || bHVarB.aJ().equals("afrTableGenerator")) {
                        c0088buC2.b(bHVarB);
                    } else {
                        b(m2, "Tool " + bHVarB.aJ() + " is not supported by this version of application.\nI don't know how to remove it, this line will be ignored.");
                    }
                }
            } catch (Exception e2) {
                a(m2, "Error loading [Tools], " + e2.getMessage());
                e2.printStackTrace();
            }
        }
    }

    private void r(G.R r2, J j2) throws V.g {
        Iterator it = a(j2, r2, "SettingContextHelp").iterator();
        while (it.hasNext()) {
            M m2 = (M) it.next();
            String strB = m2.b();
            if (strB.length() <= 3 || strB.indexOf("=") == -1) {
                b(m2, "Do not understand what to do with row, ignoring.");
            } else if (r2.c(m2.f()) == null && r2.O().b(m2.f()) == null) {
                b(m2, "No Constant or Command Button found with name " + m2.f());
            } else {
                r2.d(m2.f(), bH.W.b(bH.W.b(f(m2.e()), "\\n", "\n"), "\\\"", PdfOps.DOUBLE_QUOTE__TOKEN));
            }
        }
    }

    private String f(String str) {
        if (str.charAt(0) == '\"') {
            str = str.substring(1);
        }
        if (str.endsWith(PdfOps.DOUBLE_QUOTE__TOKEN)) {
            str = str.substring(0, str.length() - 1);
        }
        return str;
    }

    private bH b(M m2) {
        String[] strArrJ = j(m2.e());
        bH bHVar = new bH();
        bHVar.q(this.f2053l);
        bHVar.v(strArrJ[0]);
        bHVar.b(bH.W.b(strArrJ[1], PdfOps.DOUBLE_QUOTE__TOKEN, ""));
        bHVar.a(strArrJ[2]);
        if (strArrJ.length > 3) {
            bHVar.u(bH.W.b(bH.W.b(strArrJ[2], VectorFormat.DEFAULT_PREFIX, ""), "}", ""));
        }
        return bHVar;
    }

    private void s(G.R r2, J j2) throws V.g {
        G.F fO = r2.O() != null ? r2.O() : new G.F();
        fO.a(r2);
        r2.a(fO);
        Iterator itB = j2.b();
        while (itB.hasNext()) {
            int i2 = -1;
            Iterator it = a(j2, r2, (String) itB.next()).iterator();
            while (it.hasNext()) {
                M m2 = (M) it.next();
                a(m2);
                try {
                    String strF = m2.f();
                    if (strF.equals("page")) {
                        try {
                            i2 = Integer.parseInt(m2.e()) - 1;
                        } catch (NumberFormatException e2) {
                            b(m2, "Invalid page number: " + m2.e());
                        }
                    }
                    if (G.F.a(strF)) {
                        String strB = bH.W.b(m2.e(), PdfOps.DOUBLE_QUOTE__TOKEN, "");
                        if (i2 >= 0) {
                            fO.a(strF, strB, i2);
                        } else {
                            fO.a(strF, strB);
                        }
                        if (strB.indexOf(FXMLLoader.EXPRESSION_PREFIX) == -1 || strB.indexOf("$tsCanId") == -1) {
                        }
                    } else if (strF.equals("helpManualDownloadRoot")) {
                        r2.x(f(m2.e()));
                    }
                } catch (Exception e3) {
                    e3.printStackTrace();
                    String str = "Invalid row in ini file. Not sure how to handle this row:\n" + ((Object) m2);
                    if (it.hasNext()) {
                        str = str + "\n" + it.next();
                    }
                    if (it.hasNext()) {
                        str = str + "\n" + it.next();
                    }
                    throw new V.g(str);
                }
            }
        }
    }

    public void a(G.R r2, bD bDVar, J j2) {
        aX aXVar = null;
        bF bFVar = null;
        Iterator it = a(a(j2, r2, "ReferenceTables"), r2).iterator();
        while (it.hasNext()) {
            M m2 = (M) it.next();
            String strB = m2.b();
            try {
                if (strB.startsWith("referenceTable")) {
                    aXVar = new aX();
                    String[] strArrJ = j(q(strB));
                    aXVar.v(strArrJ[0].trim());
                    if (strArrJ.length > 1) {
                        aXVar.s(bH.W.b(strArrJ[1], PdfOps.DOUBLE_QUOTE__TOKEN, ""));
                    }
                    bDVar.a(aXVar);
                } else if (strB.startsWith("topicHelp")) {
                    cZ cZVarA = cY.a().a(r2, q(strB));
                    aXVar.e(cZVarA);
                    if (cZVarA.a().startsWith("http:/") || cZVarA.a().startsWith("file:/")) {
                        C0050aj c0050aj = new C0050aj();
                        c0050aj.a(cZVarA);
                        c0050aj.b(cZVarA);
                        if (aXVar.M() == null || aXVar.M().length() <= 0) {
                            c0050aj.b("Web Help");
                        } else {
                            c0050aj.b(aXVar.M());
                        }
                        bDVar.a(c0050aj);
                    }
                } else if (strB.startsWith("tableIdentifier")) {
                    String[] strArrJ2 = j(q(strB));
                    if (strArrJ2.length % 2 != 0) {
                        a(m2, "ReferenceTables::tableIdentifier has wrong number of parameters.");
                    }
                    for (int i2 = 0; i2 + 1 < strArrJ2.length; i2 = i2 + 1 + 1) {
                        aXVar.a(strArrJ2[i2], strArrJ2[i2 + 1]);
                    }
                } else if (strB.startsWith("solutionsLabel")) {
                    aXVar.b(bH.W.b(j(q(strB))[0], PdfOps.DOUBLE_QUOTE__TOKEN, ""));
                } else if (strB.startsWith("tableStartOffset")) {
                    aXVar.c(bH.W.g(j(q(strB))[0]));
                } else if (strB.startsWith("writeCommand")) {
                    aXVar.a(f(j(q(strB))[0]));
                } else if (strB.startsWith("scale")) {
                    aXVar.a(Double.parseDouble(j(q(strB))[0]));
                } else if (strB.startsWith("tableGenerator")) {
                    String[] strArrJ3 = j(q(strB));
                    int i3 = 0 + 1;
                    String str = strArrJ3[0];
                    if (str.equals("thermGenerator")) {
                        bFVar = new bF();
                        bFVar.c(str);
                        if (strArrJ3.length > i3) {
                            int i4 = i3 + 1;
                            bFVar.d(bH.W.b(strArrJ3[i3], PdfOps.DOUBLE_QUOTE__TOKEN, ""));
                        }
                        aXVar.a(bFVar);
                    } else if (str.equals("fileBrowseGenerator")) {
                        C0080bm c0080bm = new C0080bm();
                        c0080bm.c(str);
                        if (strArrJ3.length > i3) {
                            int i5 = i3 + 1;
                            c0080bm.d(bH.W.b(strArrJ3[i3], PdfOps.DOUBLE_QUOTE__TOKEN, ""));
                        }
                        aXVar.a(c0080bm);
                    } else if (str.equals("linearGenerator")) {
                        C0087bt c0087bt = new C0087bt();
                        c0087bt.c(str);
                        if (strArrJ3.length > i3) {
                            i3++;
                            c0087bt.d(bH.W.b(strArrJ3[i3], PdfOps.DOUBLE_QUOTE__TOKEN, ""));
                        }
                        if (strArrJ3.length > i3) {
                            int i6 = i3;
                            i3++;
                            c0087bt.a(bH.W.b(strArrJ3[i6], PdfOps.DOUBLE_QUOTE__TOKEN, ""));
                        }
                        if (strArrJ3.length > i3) {
                            int i7 = i3;
                            i3++;
                            c0087bt.b(bH.W.b(strArrJ3[i7], PdfOps.DOUBLE_QUOTE__TOKEN, ""));
                        }
                        if (strArrJ3.length > i3) {
                            int i8 = i3;
                            i3++;
                            c0087bt.a(Double.parseDouble(strArrJ3[i8]));
                        }
                        if (strArrJ3.length > i3) {
                            int i9 = i3;
                            i3++;
                            c0087bt.b(Double.parseDouble(strArrJ3[i9]));
                        }
                        if (strArrJ3.length > i3) {
                            int i10 = i3;
                            i3++;
                            c0087bt.c(Double.parseDouble(strArrJ3[i10]));
                        }
                        if (strArrJ3.length > i3) {
                            int i11 = i3;
                            int i12 = i3 + 1;
                            c0087bt.d(Double.parseDouble(strArrJ3[i11]));
                        }
                        aXVar.a(c0087bt);
                    }
                } else if (strB.startsWith("adcCount")) {
                    aXVar.a(bH.W.g(j(q(strB))[0]));
                } else if (strB.startsWith("bytesPerAdc")) {
                    aXVar.b(bH.W.g(j(q(strB))[0]));
                } else if (strB.startsWith("tableLimits")) {
                    String[] strArrJ4 = j(q(strB));
                    aXVar.a(strArrJ4[0], Double.parseDouble(strArrJ4[1]), Double.parseDouble(strArrJ4[2]), Double.parseDouble(strArrJ4[3]));
                } else if (strB.startsWith("thermOption")) {
                    String[] strArrJ5 = j(q(strB));
                    try {
                        bG bGVar = new bG();
                        int i13 = 0 + 1;
                        bGVar.a(bH.W.b(strArrJ5[0], PdfOps.DOUBLE_QUOTE__TOKEN, ""));
                        int i14 = i13 + 1;
                        bGVar.a(Double.parseDouble(strArrJ5[i13]));
                        int i15 = i14 + 1;
                        bGVar.b(Double.parseDouble(strArrJ5[i14]));
                        int i16 = i15 + 1;
                        bGVar.c(Double.parseDouble(strArrJ5[i15]));
                        int i17 = i16 + 1;
                        bGVar.d(Double.parseDouble(strArrJ5[i16]));
                        int i18 = i17 + 1;
                        bGVar.e(Double.parseDouble(strArrJ5[i17]));
                        int i19 = i18 + 1;
                        bGVar.f(Double.parseDouble(strArrJ5[i18]));
                        int i20 = i19 + 1;
                        bGVar.g(Double.parseDouble(strArrJ5[i19]));
                        if (bFVar != null) {
                            bFVar.a(bGVar);
                        } else {
                            b(m2, "thermOption defined before a thermGenerator! The following line must be declared after a thermGenerator.");
                        }
                    } catch (Exception e2) {
                        a(m2, "Invalid ini entry. thermOption must have the format:\nthermOption\t= name, resistor bias, tempPoint1(" + bH.S.a() + "C), resPoint1, tempPoint2, resPoint2, tempPoint3, resPoint3");
                    }
                } else if (strB.startsWith("solution")) {
                    String[] strArrJ6 = j(q(strB));
                    C0068ba c0068ba = new C0068ba();
                    c0068ba.a(bH.W.b(strArrJ6[0], PdfOps.DOUBLE_QUOTE__TOKEN, ""));
                    c0068ba.b(bH.W.b(bH.W.b(strArrJ6[1], VectorFormat.DEFAULT_PREFIX, ""), "}", ""));
                    aXVar.a(c0068ba);
                }
            } catch (Exception e3) {
                a(m2, "Invalid Ini entry for reference table, Ignored.");
            }
        }
    }

    public void b(G.R r2, bD bDVar, J j2) {
        aS aSVar = null;
        Iterator it = a(a(j2, r2, "PortEditor"), r2).iterator();
        while (it.hasNext()) {
            M m2 = (M) it.next();
            a(m2);
            String strB = m2.b();
            try {
                if (strB.startsWith("portEditor")) {
                    aSVar = new aS();
                    String[] strArrJ = j(q(strB));
                    aSVar.v(strArrJ[0].trim());
                    if (strArrJ.length > 1) {
                        aSVar.s(bH.W.b(strArrJ[1], PdfOps.DOUBLE_QUOTE__TOKEN, ""));
                    }
                    bDVar.a(aSVar);
                } else if (strB.startsWith("topicHelp")) {
                    cZ cZVarA = cY.a().a(r2, q(strB));
                    aSVar.e(cZVarA);
                    if (cZVarA.a().startsWith("http:/") || cZVarA.a().startsWith("file:/")) {
                        C0050aj c0050aj = new C0050aj();
                        c0050aj.a(cZVarA);
                        c0050aj.b(cZVarA);
                        if (aSVar.M() == null || aSVar.M().length() <= 0) {
                            c0050aj.b("Web Help");
                        } else {
                            c0050aj.b(aSVar.M());
                        }
                        bDVar.a(c0050aj);
                    }
                } else if (strB.startsWith("enabledPorts")) {
                    String[] strArrJ2 = j(q(strB));
                    if (strArrJ2.length < 2) {
                        a(m2, "PortEditor::enabledPorts requires at least 2 parameters.");
                    }
                    aSVar.c(strArrJ2[0]);
                    for (int i2 = 1; i2 < strArrJ2.length; i2++) {
                        aSVar.a(strArrJ2[i2]);
                    }
                } else if (strB.startsWith("outputCanId")) {
                    aSVar.l(q(strB));
                } else if (strB.startsWith("outputCanId")) {
                    aSVar.l(q(strB));
                } else if (strB.startsWith("outputOffset")) {
                    aSVar.e(q(strB));
                } else if (strB.startsWith("outputName")) {
                    aSVar.n(q(strB));
                } else if (strB.startsWith("outputSize")) {
                    aSVar.d(q(strB));
                } else if (strB.startsWith(Threshold.NAME)) {
                    aSVar.g(q(strB));
                } else if (strB.startsWith("hysteresis")) {
                    aSVar.h(q(strB));
                } else if (strB.startsWith("powerOnValue")) {
                    aSVar.a_(q(strB));
                } else if (strB.startsWith("triggerValue")) {
                    aSVar.j(q(strB));
                } else if (strB.startsWith("activateOption")) {
                    aSVar.m(q(strB));
                } else if (strB.startsWith("portActiveDelay")) {
                    aSVar.o(q(strB));
                } else if (strB.startsWith("portInactiveDelay")) {
                    aSVar.p(q(strB));
                } else if (strB.startsWith("portCustomAttributes")) {
                    for (String str : j(q(strB))) {
                        String strTrim = str.trim();
                        if (!strTrim.isEmpty()) {
                            aSVar.q(strTrim);
                        }
                    }
                } else if (strB.startsWith("portCustomLabels")) {
                    for (String str2 : j(q(strB))) {
                        String strF = f(str2.trim());
                        if (!strF.isEmpty()) {
                            aSVar.r(strF);
                        }
                    }
                } else if (strB.startsWith("operators")) {
                    String[] strArrJ3 = j(q(strB));
                    aSVar.f(strArrJ3[0]);
                    aM aMVarC = r2.c(strArrJ3[0]);
                    for (int i3 = 1; i3 < strArrJ3.length; i3++) {
                        String strB2 = bH.W.b(strArrJ3[i3], PdfOps.DOUBLE_QUOTE__TOKEN, "");
                        if (strB2.length() != 1) {
                            b(m2, "Invalid operator in Port Editor.");
                        }
                        aSVar.a(new Character(strB2.charAt(0)));
                        aMVarC.a(r0.charValue());
                    }
                } else if (strB.startsWith("conditionRelationship")) {
                    String[] strArrJ4 = j(q(strB));
                    aSVar.k(strArrJ4[0]);
                    aM aMVarC2 = r2.c(strArrJ4[0]);
                    for (int i4 = 1; i4 < strArrJ4.length; i4++) {
                        String strB3 = bH.W.b(strArrJ4[i4], PdfOps.DOUBLE_QUOTE__TOKEN, "");
                        if (strB3.length() != 1) {
                            b(m2, "Invalid conditionRelationship in Port Editor.");
                        }
                        aSVar.b(new Character(strB3.charAt(0)));
                        aMVarC2.a(r0.charValue());
                    }
                } else if (m2.f().equals("portEnabledCondition")) {
                    String[] strArrJ5 = j(q(strB));
                    if (strArrJ5.length != aSVar.s()) {
                        b(m2, "Number of Ports does not match number of portEnableCondition! " + aSVar.s() + " Ports, " + strArrJ5.length + " portEnabledConditions. Check your indexing");
                    }
                    for (String str3 : strArrJ5) {
                        String strB4 = bH.W.b(bH.W.b(str3, VectorFormat.DEFAULT_PREFIX, ""), "}", "");
                        a(r2, m2, strB4, false);
                        aSVar.b(strB4);
                    }
                }
            } catch (Exception e2) {
                a(m2, "Invalid Ini entry for reference table, Ignored.");
            }
        }
    }

    public ArrayList a(J j2, G.R r2, String str) {
        return j2.b(str);
    }

    public ArrayList a(J j2, G.R r2) {
        return a(j2.a(), r2);
    }

    public ArrayList a(ArrayList arrayList, G.R r2) throws V.g {
        ArrayList arrayList2 = new ArrayList();
        M m2 = null;
        while (arrayList.size() > 0) {
            try {
                m2 = (M) arrayList.get(0);
                String strTrim = m2.b().trim();
                if (strTrim.startsWith("#if")) {
                    arrayList2 = a(r2, arrayList, arrayList2, 0);
                } else if (strTrim.startsWith("#elif") || strTrim.startsWith("#endif") || strTrim.startsWith("#else")) {
                    a(m2, "#endif, #elif or #else found but no owning #if found");
                    arrayList.remove(0);
                } else {
                    arrayList.remove(0);
                    if (strTrim.startsWith("#define")) {
                        String[] strArrSplit = m2.f().split(" ");
                        if (strArrSplit.length != 2) {
                            b(m2, "Invalid define entry, Name format error: " + m2.f());
                        } else {
                            this.f2055e.put(strArrSplit[1], c(m2, m2.e()));
                        }
                    }
                    if (!strTrim.startsWith(FXMLLoader.CONTROLLER_METHOD_PREFIX)) {
                        arrayList2.add(m2);
                    } else if (strTrim.trim().startsWith("#error")) {
                        throw new V.c("A critical Error was encountered in this projects ini file.\n\nThe following message was provided:\n" + bH.W.b(strTrim, "#error", "").trim());
                    }
                }
            } catch (V.g e2) {
                throw e2;
            } catch (Exception e3) {
                a(m2, "Encountered a problem while applying user settings to ini.");
            }
        }
        return arrayList2;
    }

    private ArrayList a(G.R r2, ArrayList arrayList, ArrayList arrayList2, int i2) throws V.g {
        int i3 = 0;
        boolean zA = false;
        M m2 = null;
        while (i2 < arrayList.size()) {
            try {
                m2 = (M) arrayList.get(i2);
                String strB = m2.b();
                arrayList.remove(i2);
                if (strB.startsWith("#if")) {
                    i3++;
                } else if (strB.startsWith("#endif")) {
                    i3--;
                }
                if (i3 <= 1 && strB.startsWith("#if")) {
                    zA = a(strB, r2);
                } else if (strB.startsWith("#if") && zA) {
                    arrayList.add(i2, m2);
                    arrayList2 = a(r2, arrayList, arrayList2, i2);
                    i3--;
                } else if (i3 > 1 || !strB.startsWith("#elif")) {
                    if (i3 == 1 && strB.startsWith("#else")) {
                        if (zA) {
                            a(arrayList);
                            int i4 = i3 - 1;
                            return arrayList2;
                        }
                        zA = true;
                    } else {
                        if (i3 == 0 && strB.startsWith("#endif")) {
                            return arrayList2;
                        }
                        if (strB.startsWith("#error")) {
                            throw new V.c("A critical Error was encountered in this projects ini file.\nThe following message was provided:\n\n" + bH.W.b(strB, "#error", "").trim());
                        }
                        if (!strB.startsWith(FXMLLoader.CONTROLLER_METHOD_PREFIX) && zA) {
                            arrayList2.add(m2);
                        }
                    }
                } else {
                    if (zA) {
                        a(arrayList);
                        int i5 = i3 - 1;
                        return arrayList2;
                    }
                    zA = a(strB, r2);
                }
            } catch (V.g e2) {
                throw e2;
            } catch (Exception e3) {
                e3.printStackTrace();
                a(m2, "Error while applying #if #else Conditions at row:");
            }
        }
        return arrayList2;
    }

    private void a(ArrayList arrayList) {
        int i2 = 0;
        while (arrayList.size() > 0) {
            String strB = ((M) arrayList.get(0)).b();
            arrayList.remove(0);
            if (strB.startsWith("#if")) {
                i2++;
            } else if (!strB.startsWith("#endif")) {
                continue;
            } else if (i2 == 0) {
                return;
            } else {
                i2--;
            }
        }
    }

    private boolean a(String str, G.R r2) {
        String strR = str.startsWith(FXMLLoader.CONTROLLER_METHOD_PREFIX) ? r(str) : str.trim();
        return r2.d(strR) != null || strR.equals("TUNERSTUDIO") || strR.equals("INI_VERSION_2");
    }

    private C0048ah j(G.R r2, M m2) throws V.g {
        String strB = m2.b();
        C0048ah c0048ah = new C0048ah();
        c0048ah.q(this.f2053l);
        c0048ah.v(p(strB));
        String[] strArrJ = j(q(strB));
        try {
            int i2 = 0 + 1;
            c0048ah.a(strArrJ[0]);
            int i3 = i2 + 1;
            c0048ah.b(cY.a().a(r2, bH.W.b(strArrJ[i2], PdfOps.DOUBLE_QUOTE__TOKEN, "")));
            int i4 = i3 + 1;
            c0048ah.a(cY.a().a(r2, bH.W.b(strArrJ[i3], PdfOps.DOUBLE_QUOTE__TOKEN, "")));
            int i5 = i4 + 1;
            dh dhVarA = di.a(r2, strArrJ[i4]);
            c0048ah.a(dhVarA);
            int i6 = i5 + 1;
            dh dhVarA2 = di.a(r2, strArrJ[i5]);
            c0048ah.b(dhVarA2);
            if (strArrJ.length > i6) {
                a(r2, m2, strArrJ[i6], "Invalid Low Critical Expression", false);
                i6++;
                c0048ah.h(di.a(r2, strArrJ[i6]));
            }
            if (strArrJ.length > i6) {
                a(r2, m2, strArrJ[i6], "Invalid Low Warning Expression", false);
                int i7 = i6;
                i6++;
                c0048ah.c(di.a(r2, strArrJ[i7]));
            }
            if (strArrJ.length > i6) {
                a(r2, m2, strArrJ[i6], "Invalid High Warning Expression", false);
                int i8 = i6;
                i6++;
                c0048ah.d(di.a(r2, strArrJ[i8]));
            }
            if (strArrJ.length > i6) {
                a(r2, m2, strArrJ[i6], "Invalid LHigh Critical Expression", false);
                int i9 = i6;
                i6++;
                c0048ah.e(di.a(r2, strArrJ[i9]));
            }
            if (strArrJ.length > i6) {
                a(r2, m2, strArrJ[i6], "Invalid Value Digits Expression", false);
                int i10 = i6;
                i6++;
                c0048ah.f(di.a(r2, strArrJ[i10]));
            }
            if (strArrJ.length > i6) {
                a(r2, m2, strArrJ[i6], "Invalid Label Digits Expression", false);
                int i11 = i6;
                i6++;
                c0048ah.g(di.a(r2, strArrJ[i11]));
            }
            if (strArrJ.length > i6) {
                int i12 = i6;
                int i13 = i6 + 1;
                String strB2 = bH.W.b(bH.W.b(strArrJ[i12], VectorFormat.DEFAULT_PREFIX, ""), "}", "");
                c0048ah.u(strB2);
                a(r2, m2, strB2, "Invalid Enabled Expression", false);
            }
            if ((dhVarA instanceof G.B) && (dhVarA2 instanceof G.B) && c0048ah.a() > c0048ah.d()) {
                double dA = c0048ah.a();
                c0048ah.a(c0048ah.d());
                c0048ah.b(dA);
                c0048ah.a(true);
            }
            if (c0048ah.o() instanceof bQ) {
                a(r2, m2, ((bQ) c0048ah.o()).c(), "Invalid Low Critical Expression", false);
            }
            if (c0048ah.f() instanceof bQ) {
                a(r2, m2, ((bQ) c0048ah.f()).c(), "Invalid Low Warning Expression", false);
            }
            if (c0048ah.h() instanceof bQ) {
                a(r2, m2, ((bQ) c0048ah.h()).c(), "Invalid High Critical Expression", false);
            }
            if (c0048ah.g() instanceof bQ) {
                a(r2, m2, ((bQ) c0048ah.g()).c(), "Invalid high Warning Expression", false);
            }
            return c0048ah;
        } catch (Exception e2) {
            throw new V.g("Corrupt Gauge Entry :\n" + strB);
        }
    }

    private C0128k g(String str) {
        String strTrim = str.trim();
        if (strTrim.equals("white")) {
            return C0128k.f1250a;
        }
        if (strTrim.equalsIgnoreCase("red")) {
            return C0128k.f1260k;
        }
        if (strTrim.equalsIgnoreCase("black")) {
            return C0128k.f1258i;
        }
        if (strTrim.equalsIgnoreCase("green")) {
            return C0128k.f1268s;
        }
        if (strTrim.equalsIgnoreCase("cyan")) {
            return C0128k.f1272w;
        }
        if (strTrim.equalsIgnoreCase("blue")) {
            return C0128k.f1274y;
        }
        if (!strTrim.equalsIgnoreCase("grey") && !strTrim.equalsIgnoreCase("gray")) {
            return strTrim.equalsIgnoreCase("darkGray") ? C0128k.f1256g : strTrim.equalsIgnoreCase("lightGray") ? C0128k.f1252c : strTrim.equalsIgnoreCase("darkGrey") ? C0128k.f1256g : strTrim.equalsIgnoreCase("lightGrey") ? C0128k.f1252c : strTrim.equalsIgnoreCase("yellow") ? C0128k.f1266q : strTrim.equalsIgnoreCase("transparent") ? C0128k.f1276A : strTrim.equalsIgnoreCase("magenta") ? C0128k.f1271v : C0128k.f1261l;
        }
        return C0128k.f1254e;
    }

    private C0051ak a(M m2, G.R r2) {
        C0051ak c0051ak = new C0051ak();
        c0051ak.q(this.f2053l);
        String[] strArrJ = j(m2.e());
        try {
            int i2 = 0 + 1;
            String strB = bH.W.b(bH.W.b(strArrJ[0], VectorFormat.DEFAULT_PREFIX, ""), "}", "");
            int i3 = i2 + 1;
            c0051ak.b(cY.a().a(r2, bH.W.b(strArrJ[i2], PdfOps.DOUBLE_QUOTE__TOKEN, "")));
            int i4 = i3 + 1;
            c0051ak.a(cY.a().a(r2, bH.W.b(strArrJ[i3], PdfOps.DOUBLE_QUOTE__TOKEN, "")));
            c0051ak.v(i(c0051ak.b()));
            if (strArrJ.length > i4) {
                i4++;
                c0051ak.b(g(strArrJ[i4]));
            }
            if (strArrJ.length > i4) {
                int i5 = i4;
                i4++;
                c0051ak.d(g(strArrJ[i5]));
            }
            if (strArrJ.length > i4) {
                int i6 = i4;
                i4++;
                c0051ak.a(g(strArrJ[i6]));
            }
            if (strArrJ.length > i4) {
                int i7 = i4;
                int i8 = i4 + 1;
                c0051ak.c(g(strArrJ[i7]));
            }
            aH aHVar = new aH(r2.c());
            aHVar.v(c0051ak.aJ() + "OC");
            aHVar.e(strB);
            aHVar.a("formula");
            aHVar.c(di.a(0.0d));
            a(r2, m2, strB, "Invalid Indicator Expression: ", false);
            r2.a(aHVar);
            if (r2.g(strB.trim()) != null) {
                c0051ak.a(strB.trim());
            } else {
                String strH = h(strB);
                if (strH.length() > 1 && !bH.H.a(strH.substring(0, 1))) {
                    aH aHVar2 = new aH(r2.c());
                    aHVar2.v(strH + "_OC");
                    aHVar2.e(strB);
                    aHVar2.a("formula");
                    a(r2, m2, strB, "Invalid Indicator Expression: ", false);
                    r2.a(aHVar2);
                    c0051ak.a(aHVar2.aJ());
                }
            }
        } catch (Exception e2) {
            a(m2, "Corrupt Indicator Entry");
        }
        return c0051ak;
    }

    private String h(String str) {
        return bH.W.b(bH.W.b(bH.W.b(bH.W.b(bH.W.b(bH.W.b(bH.W.b(bH.W.b(bH.W.b(bH.W.b(bH.W.b(bH.W.b(bH.W.b(bH.W.b(bH.W.b(bH.W.b(bH.W.b(bH.W.b(bH.W.b(bH.W.b(str, " ", ""), "<", "LT"), ">", "GT"), "&", "AND"), CallSiteDescriptor.OPERATOR_DELIMITER, "OR"), "=", "EQ"), LanguageTag.SEP, "MN"), Marker.ANY_NON_NULL_MARKER, "PL"), FXMLLoader.RESOURCE_KEY_PREFIX, "MOD"), "/", "DIV"), "!", "NOT"), "^", "XOR"), "*", LanguageTag.PRIVATEUSE), ",", "Com"), "[", "LB"), "]", "RB"), "(", "LP"), ")", "RP"), "?", "QU"), CallSiteDescriptor.TOKEN_DELIMITER, "COL");
    }

    private String i(String str) {
        return bH.W.e(str);
    }

    private String[] j(String str) {
        return Q.c(str);
    }

    private C0043ac k(G.R r2, M m2) throws V.g {
        C0043ac c0043ac = new C0043ac();
        String strB = m2.b();
        c0043ac.q(this.f2053l);
        String[] strArrJ = j(q(strB));
        try {
            int i2 = 0 + 1;
            String str = strArrJ[0];
            if (str.equals(SchemaSymbols.ATTVAL_TIME)) {
                str = "dataLogTime";
                if (r2.g(str) == null) {
                    aH aHVar = new aH(r2.c());
                    aHVar.a("formula");
                    aHVar.e("AppEvent.dataLogTime");
                    aHVar.v(str);
                    aHVar.c(PdfOps.s_TOKEN);
                    r2.a(aHVar);
                }
            }
            c0043ac.b(str);
            int i3 = i2 + 1;
            String strB2 = bH.W.b(strArrJ[i2], PdfOps.DOUBLE_QUOTE__TOKEN, "");
            cZ cZVarA = cY.a().a(r2, strB2);
            if (cZVarA instanceof C0094c) {
                String strA = cZVarA.a();
                if (strA.length() > 0 && (strA.getBytes()[0] == 42 || strA.getBytes()[0] == 37 || strA.getBytes()[0] == 47 || strA.getBytes()[0] == 94 || strA.getBytes()[0] == 33 || strA.getBytes()[0] == 38 || strA.getBytes()[0] == 43 || strA.getBytes()[0] == 126 || strA.getBytes()[0] == 61 || strA.getBytes()[0] == 126 || strA.getBytes()[0] == 45)) {
                    a(m2, "Log Field units cannot start with special / mathematical characters.");
                    ((C0094c) cZVarA).a(b(strA));
                }
            }
            c0043ac.a(cZVarA);
            c0043ac.v(strB2);
            int i4 = i3 + 1;
            int i5 = i4 + 1;
            c0043ac.a(strArrJ[i4]);
            if (strArrJ.length > i5) {
                int i6 = i5 + 1;
                c0043ac.u(bH.W.b(bH.W.b(strArrJ[i5], VectorFormat.DEFAULT_PREFIX, ""), "}", "").trim());
                if (strArrJ.length > i6) {
                    int i7 = i6 + 1;
                    c0043ac.a(new bQ(r2, bH.W.b(bH.W.b(strArrJ[i6], VectorFormat.DEFAULT_PREFIX, ""), "}", "").trim()));
                }
            }
            return c0043ac;
        } catch (Exception e2) {
            throw new V.g("Corrupt DataLog Entry :\n" + strB);
        }
    }

    public String b(String str) {
        if (str != null) {
            str = bH.W.b(bH.W.b(bH.W.b(bH.W.b(bH.W.b(bH.W.b(bH.W.b(bH.W.b(bH.W.b(bH.W.b(bH.W.b(bH.W.b(str, "/", ""), Marker.ANY_NON_NULL_MARKER, "_"), "*", LanguageTag.PRIVATEUSE), FXMLLoader.RESOURCE_KEY_PREFIX, ""), "^", ""), CallSiteDescriptor.OPERATOR_DELIMITER, ""), "(", "_"), ")", "_"), VectorFormat.DEFAULT_PREFIX, ""), "}", ""), FXMLLoader.EXPRESSION_PREFIX, ""), "&", "");
        }
        return str;
    }

    private aH a(M m2, G.R r2, int i2, int i3) throws V.g {
        aH aHVar = new aH(r2.c());
        aHVar.q(this.f2053l);
        String strB = m2.b();
        aHVar.v(p(strB));
        boolean z2 = r2.O() != null && r2.O().al().equals("XCP");
        String strQ = q(strB);
        String[] strArrJ = j(strQ);
        try {
            if (strQ.startsWith(ControllerParameter.PARAM_CLASS_SCALAR) || strQ.startsWith("dotScalar")) {
                int i4 = 0 + 1;
                aHVar.a(strArrJ[0]);
                int i5 = i4 + 1;
                aHVar.b(strArrJ[i4]);
                int i6 = i5 + 1;
                String str = strArrJ[i5];
                if (z2) {
                    aHVar.a(r2.O().af() + bH.W.g(str));
                    str = "nextOffset";
                }
                if (str.equals("nextOffset")) {
                    aHVar.a(i2);
                } else if (str.equals("lastOffset")) {
                    aHVar.a(i3);
                } else {
                    int iG = bH.W.g(str);
                    if (iG < i2) {
                    }
                    aHVar.a(iG);
                }
                if (strArrJ.length > i6) {
                    if (strArrJ.length > i6) {
                        i6++;
                        aHVar.a(cY.a().a(r2, bH.W.b(strArrJ[i6], PdfOps.DOUBLE_QUOTE__TOKEN, "")));
                    }
                    int i7 = i6;
                    int i8 = i6 + 1;
                    aHVar.a(di.a(r2, strArrJ[i7]));
                    if (strQ.startsWith("dotScalar")) {
                        i6 = i8 + 1;
                        aHVar.c(bH.W.g(strArrJ[i8]));
                    } else {
                        i6 = i8 + 1;
                        aHVar.b(di.a(r2, strArrJ[i8]));
                    }
                }
                while (strArrJ.length > i6) {
                    int i9 = i6;
                    i6++;
                    String str2 = strArrJ[i9];
                    if (str2.equals("hidden")) {
                        aHVar.c(di.a(0.0d));
                    } else if (str2.startsWith(VectorFormat.DEFAULT_PREFIX)) {
                        aHVar.c(di.a(r2, str2));
                    } else if (!str2.equals("persistValue")) {
                        b(m2, "Unknown OutputChannel visible condition or flag");
                    }
                }
            } else if (strArrJ.length > 0 && strArrJ[0].equals(ControllerParameter.PARAM_CLASS_BITS)) {
                int i10 = 0 + 1;
                aHVar.a(strArrJ[0]);
                int i11 = i10 + 1;
                aHVar.b(strArrJ[i10]);
                int i12 = i11 + 1;
                String str3 = strArrJ[i11];
                if (z2) {
                    aHVar.a(r2.O().af() + bH.W.g(str3));
                    str3 = "nextOffset";
                }
                if (str3.equals("nextOffset")) {
                    aHVar.a(i2);
                } else if (str3.equals("lastOffset")) {
                    aHVar.a(i3);
                } else {
                    int iG2 = bH.W.g(str3);
                    if (iG2 < i2 - aHVar.l()) {
                    }
                    aHVar.a(iG2);
                }
                String str4 = strArrJ[i12];
                aHVar.d(m(str4));
                aHVar.e(n(str4));
                for (int i13 = i12 + 1; i13 < strArrJ.length; i13++) {
                    try {
                        aHVar.d(bH.W.b(strArrJ[i13], PdfOps.DOUBLE_QUOTE__TOKEN, ""));
                    } catch (V.g e2) {
                        b(m2, e2.getLocalizedMessage());
                    }
                }
            } else {
                if (!strQ.startsWith(VectorFormat.DEFAULT_PREFIX)) {
                    throw new V.g("Malformed OutputChannel entry.");
                }
                aHVar.a("formula");
                int i14 = 0 + 1;
                aHVar.e(k(bH.W.b(bH.W.b(strArrJ[0], VectorFormat.DEFAULT_PREFIX, ""), "}", "")));
                if (strArrJ.length > 1 && strArrJ.length > i14) {
                    i14++;
                    aHVar.a(cY.a().a(r2, bH.W.b(strArrJ[i14], PdfOps.DOUBLE_QUOTE__TOKEN, "")));
                }
                while (strArrJ.length > i14) {
                    int i15 = i14;
                    i14++;
                    String str5 = strArrJ[i15];
                    if (str5.equals("hidden")) {
                        aHVar.c(di.a(0.0d));
                    } else if (str5.startsWith(VectorFormat.DEFAULT_PREFIX)) {
                        aHVar.c(di.a(r2, str5));
                    } else if (!str5.equals("persistValue")) {
                        b(m2, "Unknown OutputChannel visible condition or flag");
                    }
                }
            }
            return aHVar;
        } catch (NumberFormatException e3) {
            throw new V.g("Malformed OutputChannel entry.");
        }
    }

    private C0069bb a(M m2, G.R r2, int i2, int i3, aI aIVar) throws V.g {
        C0069bb c0069bb = new C0069bb(aIVar.ac());
        String strB = m2.b();
        c0069bb.v(p(strB));
        String strQ = q(strB);
        String[] strArrJ = j(strQ);
        try {
            if (strQ.startsWith(ControllerParameter.PARAM_CLASS_SCALAR)) {
                int i4 = 0 + 1;
                c0069bb.a(strArrJ[0]);
                int i5 = i4 + 1;
                c0069bb.b(strArrJ[i4]);
                int i6 = i5 + 1;
                String str = strArrJ[i5];
                if (str.equals("nextOffset")) {
                    c0069bb.a(i2);
                } else if (str.equals("lastOffset")) {
                    c0069bb.a(i3);
                } else {
                    int iG = bH.W.g(str);
                    if (iG < i2) {
                    }
                    c0069bb.a(iG);
                }
                if (strArrJ.length > i6) {
                    if (strArrJ.length > i6) {
                        i6++;
                        c0069bb.a(cY.a().a(r2, bH.W.b(strArrJ[i6], PdfOps.DOUBLE_QUOTE__TOKEN, "")));
                    }
                    int i7 = i6;
                    int i8 = i6 + 1;
                    c0069bb.a(di.a(r2, strArrJ[i7]));
                    i6 = i8 + 1;
                    c0069bb.b(di.a(r2, strArrJ[i8]));
                }
                if (strArrJ.length > i6) {
                    try {
                        int i9 = i6;
                        i6++;
                        c0069bb.f(bH.W.g(strArrJ[i9]));
                    } catch (NumberFormatException e2) {
                        b(m2, "Invalid DLRAM_AddPad value");
                    }
                } else {
                    b(m2, "Pad Address not optional.");
                }
                if (strArrJ.length > i6) {
                    try {
                        int i10 = i6;
                        i6++;
                        c0069bb.b(bH.W.g(strArrJ[i10]));
                    } catch (NumberFormatException e3) {
                        b(m2, "Invalid digits value");
                    }
                }
                if (strArrJ.length > i6) {
                    int i11 = i6;
                    int i12 = i6 + 1;
                    String str2 = strArrJ[i11];
                    if (str2.equals("hidden")) {
                        c0069bb.c(di.a(0.0d));
                    } else if (str2.startsWith(VectorFormat.DEFAULT_PREFIX)) {
                        c0069bb.c(di.a(r2, str2));
                    } else {
                        b(m2, "Unknown Replay visible condition or flag");
                    }
                }
            } else if (strQ.startsWith("dotScalar")) {
                int i13 = 0 + 1;
                c0069bb.a(strArrJ[0]);
                int i14 = i13 + 1;
                c0069bb.b(strArrJ[i13]);
                int i15 = i14 + 1;
                String str3 = strArrJ[i14];
                if (str3.equals("nextOffset")) {
                    c0069bb.a(i2);
                } else if (str3.equals("lastOffset")) {
                    c0069bb.a(i3);
                } else {
                    int iG2 = bH.W.g(str3);
                    if (iG2 < i2) {
                    }
                    c0069bb.a(iG2);
                }
                if (strArrJ.length > i15) {
                    if (strArrJ.length > i15) {
                        i15++;
                        c0069bb.a(cY.a().a(r2, bH.W.b(strArrJ[i15], PdfOps.DOUBLE_QUOTE__TOKEN, "")));
                    }
                    int i16 = i15;
                    i15++;
                    c0069bb.a(di.a(r2, strArrJ[i16]));
                    if (strArrJ.length > i15) {
                        try {
                            i15++;
                            c0069bb.c(bH.W.g(strArrJ[i15]));
                        } catch (NumberFormatException e4) {
                            b(m2, "Invalid decimal bits value");
                        }
                    } else {
                        b(m2, "Decimal bits is required for paramClass dotScalar");
                    }
                }
                if (strArrJ.length > i15) {
                    try {
                        int i17 = i15;
                        i15++;
                        c0069bb.f(bH.W.g(strArrJ[i17]));
                    } catch (NumberFormatException e5) {
                        b(m2, "Invalid DLRAM_AddPad value");
                    }
                }
                if (strArrJ.length > i15) {
                    try {
                        int i18 = i15;
                        i15++;
                        c0069bb.b(bH.W.g(strArrJ[i18]));
                    } catch (NumberFormatException e6) {
                        b(m2, "Invalid digits value");
                    }
                }
                while (strArrJ.length > i15) {
                    int i19 = i15;
                    i15++;
                    String str4 = strArrJ[i19];
                    if (str4.equals("hidden")) {
                        c0069bb.c(di.a(0.0d));
                    } else if (str4.startsWith(VectorFormat.DEFAULT_PREFIX)) {
                        c0069bb.c(di.a(r2, str4));
                    } else {
                        b(m2, "Unknown Replay visible condition or flag");
                    }
                }
            } else if (strArrJ.length > 0 && strArrJ[0].equals(ControllerParameter.PARAM_CLASS_BITS)) {
                int i20 = 0 + 1;
                c0069bb.a(strArrJ[0]);
                int i21 = i20 + 1;
                c0069bb.b(strArrJ[i20]);
                int i22 = i21 + 1;
                String str5 = strArrJ[i21];
                if (str5.equals("nextOffset")) {
                    c0069bb.a(i2);
                } else if (str5.equals("lastOffset")) {
                    c0069bb.a(i3);
                } else {
                    int iG3 = bH.W.g(str5);
                    if (iG3 < i2 - c0069bb.l()) {
                    }
                    c0069bb.a(iG3);
                }
                String str6 = strArrJ[i22];
                c0069bb.d(m(str6));
                c0069bb.e(n(str6));
                for (int i23 = i22 + 1; i23 < strArrJ.length; i23++) {
                    try {
                        c0069bb.d(bH.W.b(strArrJ[i23], PdfOps.DOUBLE_QUOTE__TOKEN, ""));
                    } catch (V.g e7) {
                        b(m2, e7.getLocalizedMessage());
                    }
                }
            } else {
                if (!strQ.startsWith(VectorFormat.DEFAULT_PREFIX)) {
                    throw new V.g("Malformed OutputChannel entry.");
                }
                c0069bb.a("formula");
                int i24 = 0 + 1;
                c0069bb.e(k(bH.W.b(bH.W.b(strArrJ[0], VectorFormat.DEFAULT_PREFIX, ""), "}", "")));
                if (strArrJ.length > 1 && strArrJ.length > i24) {
                    i24++;
                    c0069bb.a(cY.a().a(r2, bH.W.b(strArrJ[i24], PdfOps.DOUBLE_QUOTE__TOKEN, "")));
                }
                if (strArrJ.length > 2) {
                    int i25 = i24;
                    int i26 = i24 + 1;
                    String str7 = strArrJ[i25];
                    if (str7.equals("hidden")) {
                        c0069bb.c(di.a(0.0d));
                    } else if (str7.startsWith(VectorFormat.DEFAULT_PREFIX)) {
                        c0069bb.c(di.a(r2, str7));
                    } else {
                        b(m2, "Unknown Replay visible condition or flag");
                    }
                }
            }
            return c0069bb;
        } catch (NumberFormatException e8) {
            throw new V.g("Malformed OutputChannel entry.");
        }
    }

    private String k(String str) {
        if (str.trim().equals("60000.0 / rpm * (2.0-(twoStroke&1))")) {
            str = "rpm > 0 ? 60000.0 / rpm * (2.0-(twoStroke&1)) : 0";
        } else if (str.trim().equals("60000.0 / rpm * (2.0-twoStroke)")) {
            str = "rpm > 0 ? 60000.0 / rpm * (2.0-twoStroke) : 0";
        } else if (str.trim().equals("100.0*nSquirts1/altDiv1*pulseWidth1/cycleTime1")) {
            str = "rpm > 0 ? 100.0*nSquirts1/altDiv1*pulseWidth1/cycleTime1 : 0";
        } else if (str.trim().equals("100.0*nSquirts2/altDiv2*pulseWidth2/cycleTime2")) {
            str = "rpm > 0 ? 100.0*nSquirts2/altDiv2*pulseWidth2/cycleTime2 : 0";
        }
        return str;
    }

    private C0052al a(M m2, aI aIVar, int i2, int i3) {
        C0052al c0052al = new C0052al(aIVar.ac());
        c0052al.q(this.f2053l);
        try {
            String[] strArrJ = j(q(m2.b()));
            int i4 = 0 + 1;
            c0052al.v(bH.W.b(strArrJ[0], PdfOps.DOUBLE_QUOTE__TOKEN, ""));
            int i5 = i4 + 1;
            String str = strArrJ[i4];
            if (str.equals(ControllerParameter.PARAM_CLASS_SCALAR)) {
                c0052al.a(str);
                int i6 = i5 + 1;
                c0052al.b(strArrJ[i5]);
                int i7 = i6 + 1;
                String str2 = strArrJ[i6];
                if (str2.equals("nextOffset")) {
                    c0052al.a(i2);
                } else if (str2.equals("lastOffset")) {
                    c0052al.a(i3);
                } else {
                    c0052al.a(bH.W.g(str2));
                }
                if (strArrJ.length > i7) {
                    int i8 = i7 + 1;
                    c0052al.c(strArrJ[i7]);
                    int i9 = i8 + 1;
                    c0052al.a(Double.parseDouble(strArrJ[i8]));
                    int i10 = i9 + 1;
                    c0052al.b(Double.parseDouble(strArrJ[i9]));
                }
            } else if (str.equals(ControllerParameter.PARAM_CLASS_BITS)) {
                int i11 = i5 + 1;
                c0052al.a(strArrJ[i5]);
                int i12 = i11 + 1;
                c0052al.b(strArrJ[i11]);
                int i13 = i12 + 1;
                c0052al.a(bH.W.g(strArrJ[i12]));
                int i14 = i13 + 1;
                String str3 = strArrJ[i13];
                c0052al.d(m(str3));
                c0052al.e(n(str3));
            }
        } catch (Exception e2) {
            a(m2, "Malformed Internal Log Field entry.");
        }
        return c0052al;
    }

    private aM a(G.R r2, M m2, int i2, aM aMVar) throws V.g {
        String strTrim;
        String strSubstring;
        String strB = m2.b();
        String strP = p(strB);
        String strQ = q(strB);
        aM aKVar = strQ.startsWith("oddArray") ? new aK() : new aM();
        aKVar.q(this.f2053l);
        aKVar.v(strP);
        aKVar.c(i2);
        String[] strArrJ = j(strQ);
        if (strQ.startsWith(ControllerParameter.PARAM_CLASS_BITS)) {
            int i3 = 0 + 1;
            aKVar.a(strArrJ[0]);
            int i4 = i3 + 1;
            String str = strArrJ[i3];
            if (str.indexOf("8  ") == -1) {
                aKVar.b(str);
                i4++;
                strTrim = strArrJ[i4];
            } else {
                aKVar.b(str.substring(0, str.indexOf("8  ")));
                strTrim = str.substring(str.indexOf("8  ") + 1).trim();
                b(m2, "Constants entry missing comma, but corrected.");
            }
            if (strTrim.indexOf("[") == -1) {
                if (strTrim.equals("nextOffset")) {
                    if (aMVar != null) {
                        aKVar.a(new C0105ck(aMVar));
                    } else {
                        aKVar.a(new bW(0));
                    }
                } else if (!strTrim.equals("lastOffset")) {
                    aKVar.a(new bW(bH.W.g(strTrim)));
                } else if (aMVar != null) {
                    aKVar.a(aMVar.f());
                } else {
                    aKVar.a(new bW(0));
                }
                int i5 = i4;
                i4++;
                strSubstring = strArrJ[i5];
            } else {
                aKVar.a(new bW(bH.W.g(strTrim.substring(0, strTrim.indexOf("[")))));
                strSubstring = strTrim.substring(strTrim.indexOf("["));
            }
            aKVar.f(m(strSubstring));
            aKVar.g(n(strSubstring));
            aKVar.c(o(strSubstring));
            if (strArrJ.length <= i4 || !strArrJ[i4].trim().startsWith(FXMLLoader.EXPRESSION_PREFIX)) {
                HashMap map = null;
                ArrayList arrayList = new ArrayList();
                int iIntValue = -1;
                while (i4 < strArrJ.length) {
                    try {
                        if (strArrJ[i4].indexOf("=") <= 0 || strArrJ[i4].indexOf("=") >= strArrJ[i4].indexOf(PdfOps.DOUBLE_QUOTE__TOKEN)) {
                            arrayList.add(strArrJ[i4]);
                        } else {
                            if (map == null) {
                                map = new HashMap();
                            }
                            String strSubstring2 = strArrJ[i4].substring(0, strArrJ[i4].indexOf("="));
                            String strSubstring3 = strArrJ[i4].substring(strArrJ[i4].indexOf("=") + 1, strArrJ[i4].length());
                            Integer numValueOf = Integer.valueOf(Integer.parseInt(strSubstring2));
                            if (numValueOf.intValue() > iIntValue) {
                                iIntValue = numValueOf.intValue();
                            }
                            map.put(numValueOf, strSubstring3);
                        }
                        i4++;
                    } catch (Exception e2) {
                        b(m2, "Invalid Option List. Expected formats are: \"Option1\", \"Option2\", etc OR: index=\"Option1\", index=\"Option2\", etc. index must be a valid integer.");
                    }
                }
                if (map == null) {
                    Iterator it = arrayList.iterator();
                    while (it.hasNext()) {
                        try {
                            aKVar.d((String) it.next());
                        } catch (V.g e3) {
                            b(m2, e3.getLocalizedMessage());
                        }
                    }
                } else {
                    int iPow = (int) Math.pow(2.0d, aKVar.w());
                    for (int i6 = 0; i6 < iPow; i6++) {
                        if (map.get(Integer.valueOf(i6)) != null) {
                            try {
                                aKVar.d((String) map.get(Integer.valueOf(i6)));
                            } catch (V.g e4) {
                                b(m2, e4.getLocalizedMessage());
                            }
                        } else {
                            try {
                                aKVar.d("INVALID");
                            } catch (V.g e5) {
                                b(m2, e5.getLocalizedMessage());
                            }
                        }
                    }
                }
            } else {
                String strSubstring4 = strArrJ[i4].trim().substring(1);
                String str2 = (String) this.f2055e.get(strSubstring4);
                if (str2 == null || str2.isEmpty()) {
                    b(m2, "String List not found. " + strSubstring4 + " Must be defined in ini file.");
                } else {
                    String[] strArrJ2 = j(str2);
                    while (true) {
                        i4++;
                        if (strArrJ.length <= i4) {
                            break;
                        }
                        try {
                            String[] strArrSplit = strArrJ[i4].split("=");
                            strArrJ2[bH.W.g(strArrSplit[0].trim())] = strArrSplit[1].trim();
                        } catch (Exception e6) {
                            b(m2, "Malformed Option over-ride: " + strArrJ[i4] + " Proper format for index 1: 1=\"My Override String\"");
                        }
                    }
                    for (String str3 : strArrJ2) {
                        try {
                            aKVar.d(str3);
                        } catch (V.g e7) {
                            b(m2, e7.getLocalizedMessage());
                        }
                    }
                }
            }
            int iPow2 = (int) Math.pow(2.0d, aKVar.w());
            int size = aKVar.x().size();
            if (iPow2 > size) {
                if (!r2.O().at()) {
                    b(m2, "bit Constant " + aKVar.aJ() + ", contains fewer options (" + aKVar.x().size() + ") than expected(" + iPow2 + "), padding remaining with \"INVALID\"");
                }
                for (int i7 = size; i7 < iPow2; i7++) {
                    aKVar.d("INVALID");
                }
            }
        } else if (strQ.startsWith(ControllerParameter.PARAM_CLASS_ARRAY) || strQ.startsWith("oddArray")) {
            int i8 = 0 + 1;
            aKVar.a(strArrJ[0]);
            int i9 = i8 + 1;
            aKVar.b(strArrJ[i8]);
            int i10 = i9 + 1;
            String str4 = strArrJ[i9];
            if (str4.equals("nextOffset")) {
                if (aMVar != null) {
                    aKVar.a(new C0105ck(aMVar));
                } else {
                    aKVar.a(new bW(0));
                }
            } else if (!str4.equals("lastOffset")) {
                aKVar.a(new bW(bH.W.g(str4)));
            } else if (aMVar != null) {
                aKVar.a(aMVar.f());
            } else {
                aKVar.a(new bW(0));
            }
            int i11 = i10 + 1;
            aKVar.a(a(r2, m2, strArrJ[i10]));
            if (strArrJ.length > i11) {
                i11++;
                aKVar.a(cY.a().a(r2, bH.W.b(strArrJ[i11], PdfOps.DOUBLE_QUOTE__TOKEN, "")));
            }
            if (strArrJ.length > i11) {
                int i12 = i11;
                i11++;
                aKVar.a(di.a(r2, strArrJ[i12]));
            }
            if (strArrJ.length > i11) {
                int i13 = i11;
                i11++;
                aKVar.b(di.a(r2, strArrJ[i13]));
            }
            if (strArrJ.length > i11) {
                int i14 = i11;
                i11++;
                aKVar.c(di.a(r2, strArrJ[i14]));
            }
            if (strArrJ.length > i11) {
                int i15 = i11;
                i11++;
                aKVar.d(di.a(r2, strArrJ[i15]));
            }
            try {
                if (strArrJ.length > i11) {
                    int i16 = i11;
                    i11++;
                    aKVar.e(di.a(r2, strArrJ[i16]));
                }
            } catch (V.g e8) {
                b(m2, e8.getMessage());
            }
            if (strArrJ.length > i11) {
                while (i11 < strArrJ.length) {
                    if (strArrJ[i11].equals("noSizeMutation")) {
                        aKVar.c(false);
                    } else if (strArrJ[i11].equals("noMsqSave") || strArrJ[i11].equals("noSaveToMsq")) {
                        aKVar.b(false);
                    }
                    i11++;
                }
            }
        } else if (strQ.startsWith(ControllerParameter.PARAM_CLASS_SCALAR)) {
            int i17 = 0 + 1;
            aKVar.a(strArrJ[0]);
            int i18 = i17 + 1;
            aKVar.b(strArrJ[i17]);
            int i19 = i18 + 1;
            String str5 = strArrJ[i18];
            if (str5.equals("nextOffset")) {
                if (aMVar != null) {
                    aKVar.a(new C0105ck(aMVar));
                } else {
                    aKVar.a(new bW(0));
                }
            } else if (!str5.equals("lastOffset")) {
                aKVar.a(new bW(bH.W.g(str5)));
            } else if (aMVar != null) {
                aKVar.a(aMVar.f());
            } else {
                aKVar.a(new bW(0));
            }
            if (strArrJ.length > i19) {
                int i20 = i19 + 1;
                aKVar.a(cY.a().a(r2, bH.W.b(strArrJ[i19], PdfOps.DOUBLE_QUOTE__TOKEN, "")));
                int i21 = i20 + 1;
                aKVar.a(di.a(r2, strArrJ[i20]));
                int i22 = i21 + 1;
                aKVar.b(di.a(r2, strArrJ[i21]));
                if (strArrJ.length > i22 + 2) {
                    int i23 = i22 + 1;
                    aKVar.c(di.a(r2, strArrJ[i22]));
                    int i24 = i23 + 1;
                    aKVar.d(di.a(r2, strArrJ[i23]));
                    int i25 = i24 + 1;
                    aKVar.e(di.a(r2, strArrJ[i24]));
                    if (strArrJ.length > i25 && (strArrJ[i25].equals("noLocalUpdate") || strArrJ[i25].equals("noSaveToMsq") || strArrJ[i25].equals("noMsqSave"))) {
                        aKVar.b(false);
                    } else if (strArrJ.length > i25 && strArrJ[i25].equals("controllerPriority")) {
                        aKVar.i(true);
                    } else if (strArrJ.length > i25) {
                        b(m2, "Unknown Constant Option: " + strArrJ[i25]);
                    }
                    int i26 = i25 + 1;
                }
            }
        } else if (strQ.startsWith("string")) {
            int i27 = 0 + 1;
            aKVar.a(strArrJ[0]);
            int i28 = i27 + 1;
            aKVar.b(strArrJ[i27]);
            int i29 = i28 + 1;
            String str6 = strArrJ[i28];
            if (str6.equals("nextOffset")) {
                if (aMVar != null) {
                    aKVar.a(new C0105ck(aMVar));
                } else {
                    aKVar.a(new bW(0));
                }
            } else if (!str6.equals("lastOffset")) {
                aKVar.a(new bW(bH.W.g(str6)));
            } else if (aMVar != null) {
                aKVar.a(aMVar.f());
            } else {
                aKVar.a(new bW(0));
            }
            int i30 = i29 + 1;
            aKVar.a(1, bH.W.g(strArrJ[i29]));
            if (strArrJ.length > i30) {
                i30++;
                String str7 = strArrJ[i30];
                try {
                    aKVar.e(di.a(r2, str7));
                } catch (V.g e9) {
                    b(m2, "Invalid String parameter Option for columns: " + str7);
                }
            }
            if (strArrJ.length > i30) {
                b(m2, "Unknown Constant Option: " + strArrJ[i30]);
            }
        } else {
            a(m2, "Unknown paramClass for ini entry.");
        }
        return aKVar;
    }

    private aM l(G.R r2, M m2) throws V.g {
        String strB = m2.b();
        bZ bZVar = new bZ();
        String strP = p(strB);
        String strQ = q(strB);
        bZVar.v(strP);
        bZVar.c(-1);
        bZVar.a(new bW(0));
        String[] strArrJ = j(strQ);
        if (strArrJ[0].equals(bZ.f883d) || strArrJ[0].equals(bZ.f884e)) {
            if (strArrJ.length < 2) {
                a(m2, "2 parameters required for paramClass: channelValueOnConnect or continuousChannelValue");
            } else {
                bZVar.a(strArrJ[0]);
                bZVar.g(strArrJ[1]);
                bZVar.b("S32");
                C0117cw.a(r2).a(bZVar);
            }
        } else if (strQ.startsWith(ControllerParameter.PARAM_CLASS_BITS)) {
            int i2 = 0 + 1;
            bZVar.a(strArrJ[0]);
            int i3 = i2 + 1;
            bZVar.b(strArrJ[i2]);
            String str = strArrJ[i3];
            bZVar.f(m(str));
            bZVar.g(n(str));
            bZVar.c(o(str));
            for (int i4 = i3 + 1; i4 < strArrJ.length; i4++) {
                bZVar.d(strArrJ[i4]);
            }
        } else if (strQ.startsWith(ControllerParameter.PARAM_CLASS_ARRAY)) {
            int i5 = 0 + 1;
            bZVar.a(strArrJ[0]);
            int i6 = i5 + 1;
            bZVar.b(strArrJ[i5]);
            int i7 = i6 + 1;
            bZVar.a(a(r2, m2, strArrJ[i6]));
            if (strArrJ.length > i7) {
                i7++;
                bZVar.a(cY.a().a(r2, bH.W.b(strArrJ[i7], PdfOps.DOUBLE_QUOTE__TOKEN, "")));
            }
            if (strArrJ.length > i7) {
                int i8 = i7;
                i7++;
                bZVar.a(di.a(r2, strArrJ[i8]));
            }
            if (strArrJ.length > i7) {
                int i9 = i7;
                i7++;
                bZVar.b(di.a(r2, strArrJ[i9]));
            }
            if (strArrJ.length > i7) {
                int i10 = i7;
                i7++;
                bZVar.c(di.a(r2, strArrJ[i10]));
            }
            if (strArrJ.length > i7) {
                int i11 = i7;
                i7++;
                bZVar.d(di.a(r2, strArrJ[i11]));
            }
            if (strArrJ.length > i7) {
                int i12 = i7;
                i7++;
                bZVar.e(di.a(r2, strArrJ[i12]));
            }
            if (strArrJ.length > i7) {
                while (i7 < strArrJ.length) {
                    if (strArrJ[i7].equals("noSizeMutation")) {
                        bZVar.c(false);
                    } else if (strArrJ[i7].equals("noMsqSave") || strArrJ[i7].equals("noSaveToMsq")) {
                        bZVar.b(false);
                    }
                    i7++;
                }
            }
        } else if (strQ.startsWith(ControllerParameter.PARAM_CLASS_SCALAR)) {
            int i13 = 0 + 1;
            bZVar.a(strArrJ[0]);
            int i14 = i13 + 1;
            bZVar.b(strArrJ[i13]);
            if (strArrJ.length > i14) {
                int i15 = i14 + 1;
                bZVar.a(cY.a().a(r2, bH.W.b(strArrJ[i14], PdfOps.DOUBLE_QUOTE__TOKEN, "")));
                int i16 = i15 + 1;
                bZVar.a(di.a(r2, strArrJ[i15]));
                int i17 = i16 + 1;
                bZVar.b(di.a(r2, strArrJ[i16]));
                if (strArrJ.length > i17 + 2) {
                    int i18 = i17 + 1;
                    bZVar.c(di.a(r2, strArrJ[i17]));
                    int i19 = i18 + 1;
                    bZVar.d(di.a(r2, strArrJ[i18]));
                    int i20 = i19 + 1;
                    bZVar.e(di.a(r2, strArrJ[i19]));
                    if (strArrJ.length > i20 && (strArrJ[i20].equals("noLocalUpdate") || strArrJ[i20].equals("noSaveToMsq") || strArrJ[i20].equals("noMsqSave"))) {
                        bZVar.b(false);
                    } else if (strArrJ.length > i20) {
                        b(m2, "Unknown Constant Option: " + strArrJ[i20]);
                    }
                    int i21 = i20 + 1;
                }
            }
        } else if (strQ.startsWith("string")) {
            int i22 = 0 + 1;
            bZVar.a(strArrJ[0]);
            int i23 = i22 + 1;
            bZVar.b(strArrJ[i22]);
            int i24 = i23 + 1;
            bZVar.a(1, bH.W.g(strArrJ[i23]));
            if (strArrJ.length > i24) {
                i24++;
                String str2 = strArrJ[i24];
                try {
                    bZVar.e(di.a(r2, str2));
                } catch (V.g e2) {
                    b(m2, "Invalid String parameter Option for columns: " + str2);
                }
            }
            if (strArrJ.length > i24) {
            }
        } else {
            b(m2, "Unknown paramClass for ini entry.");
        }
        return bZVar;
    }

    /* JADX WARN: Multi-variable type inference failed */
    private G.A a(G.R r2, M m2, String str) throws V.g {
        G.B b2;
        dh b3;
        if (str.contains(VectorFormat.DEFAULT_PREFIX)) {
            String[] strArrL = l(str);
            if (strArrL.length == 2) {
                bQ bQVar = new bQ(r2, strArrL[0]);
                if (bQVar.b().length > 1) {
                    b(m2, "Columns Expression should have 1 variable for Table Widgets to offer resizing.");
                }
                bQ bQVar2 = new bQ(r2, strArrL[1]);
                if (bQVar2.b().length > 1) {
                    b(m2, "Row Expression should have 1 variable for Table Widgets to offer resizing.");
                }
                b2 = bQVar;
                b3 = bQVar2;
            } else {
                if (strArrL.length != 1) {
                    throw new V.g("Failed to parse array dimension from: " + str);
                }
                b2 = new G.B(1.0d);
                b3 = new bQ(r2, strArrL[0]);
            }
        } else {
            if (str.toLowerCase().indexOf(LanguageTag.PRIVATEUSE) != -1) {
                b2 = new G.B(bH.W.g(str.substring(1, r0).trim()));
                b3 = new G.B(bH.W.g(str.substring(r0 + 1, str.indexOf("]", r0)).trim()));
            } else {
                b2 = new G.B(1.0d);
                b3 = new G.B(bH.W.g(str.substring(str.indexOf("[") + 1, str.indexOf("]")).trim()));
            }
        }
        return new G.A(b2, b3);
    }

    private String[] l(String str) {
        String strTrim = str.trim();
        if (strTrim.startsWith("[")) {
            strTrim = strTrim.substring(1).trim();
        }
        if (strTrim.endsWith("]")) {
            strTrim = strTrim.substring(0, strTrim.indexOf("]")).trim();
        }
        int i2 = 0;
        boolean z2 = false;
        ArrayList arrayList = new ArrayList();
        int i3 = 0;
        while (i3 < strTrim.length()) {
            if (strTrim.charAt(i3) == '{') {
                i2 = i3 + 1;
                z2 = true;
            } else if (strTrim.charAt(i3) == '}' || ((!z2 && strTrim.charAt(i3) == 'x') || i3 == strTrim.length() - 1)) {
                arrayList.add(strTrim.substring(i2, i3));
                z2 = false;
                i2 = i3 + 1;
                while (i3 < strTrim.length() - 1 && strTrim.charAt(i3 + 1) != '{') {
                    i3++;
                }
            }
            i3++;
        }
        return (String[]) arrayList.toArray(new String[arrayList.size()]);
    }

    private int m(String str) {
        return bH.W.g(str.substring(1, str.indexOf(CallSiteDescriptor.TOKEN_DELIMITER)));
    }

    private int n(String str) {
        String strSubstring = str.substring(str.indexOf(CallSiteDescriptor.TOKEN_DELIMITER) + 1);
        return bH.W.g((strSubstring.indexOf(Marker.ANY_NON_NULL_MARKER) != -1 ? strSubstring.substring(0, strSubstring.indexOf(Marker.ANY_NON_NULL_MARKER)) : strSubstring.substring(0, strSubstring.indexOf("]"))).trim());
    }

    private double o(String str) {
        if (str.indexOf(Marker.ANY_NON_NULL_MARKER) != -1) {
            return Double.parseDouble(str.substring(str.indexOf(Marker.ANY_NON_NULL_MARKER) + 1, str.indexOf("]")));
        }
        return 0.0d;
    }

    private String p(String str) {
        return Q.b(str);
    }

    private String q(String str) {
        return Q.a(str);
    }

    private String r(String str) {
        String strTrim = str.trim();
        int iIndexOf = strTrim.indexOf(" ");
        String strTrim2 = strTrim.substring(iIndexOf).trim();
        int iIndexOf2 = strTrim2.indexOf(" ", iIndexOf + 1);
        if (iIndexOf2 < 0) {
            iIndexOf2 = strTrim2.length();
        }
        return strTrim2.substring(0, iIndexOf2).trim();
    }

    public boolean a() {
        return this.f2050j;
    }

    public void a(boolean z2) {
        this.f2050j = z2;
    }

    private void a(G.R r2, String str, int i2) {
        if (str == null || str.equals("") || r2.e().c(str) != null) {
            return;
        }
        if (str.equals("std_warmup")) {
            r2.e().a(new K.i(r2, i2));
        } else if (str.equals("std_accel")) {
            r2.e().a(new K.a(r2, i2, true));
        }
    }

    public void b(boolean z2) {
        this.f2053l = z2;
    }

    private String c(M m2, String str) {
        if (!str.contains(FXMLLoader.EXPRESSION_PREFIX)) {
            return str;
        }
        String[] strArrJ = j(str);
        StringBuilder sb = new StringBuilder();
        for (int i2 = 0; i2 < strArrJ.length; i2++) {
            String str2 = strArrJ[i2];
            if (str2.trim().startsWith(FXMLLoader.EXPRESSION_PREFIX)) {
                String strSubstring = strArrJ[i2].trim().substring(1);
                String str3 = (String) this.f2055e.get(strSubstring);
                if (str3 == null || str3.isEmpty()) {
                    b(m2, "#defines String List not found. " + strSubstring + " Must be defined in ini above usage point.");
                } else {
                    sb.append(str3);
                }
            } else {
                sb.append(str2);
            }
            if (i2 < strArrJ.length - 1) {
                sb.append(", ");
            }
        }
        return sb.toString();
    }
}
