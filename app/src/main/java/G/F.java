package G;

import com.efiAnalytics.plugin.ecu.ControllerParameter;
import java.io.Serializable;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXMLLoader;
import jdk.jfr.Enabled;
import jssc.SerialPort;
import org.icepdf.core.util.PdfOps;
import sun.util.locale.LanguageTag;

/* loaded from: TunerStudioMS.jar:G/F.class */
public class F extends Q implements InterfaceC0107cm, Serializable, Cloneable {

    /* renamed from: a, reason: collision with root package name */
    public static int f337a = 14;

    /* renamed from: e, reason: collision with root package name */
    private String f301e = "basicRequestReply";

    /* renamed from: f, reason: collision with root package name */
    private String f302f = "big";

    /* renamed from: g, reason: collision with root package name */
    private int f303g = 0;

    /* renamed from: h, reason: collision with root package name */
    private int f304h = 300;

    /* renamed from: i, reason: collision with root package name */
    private int f305i = 250;

    /* renamed from: j, reason: collision with root package name */
    private boolean f306j = true;

    /* renamed from: k, reason: collision with root package name */
    private boolean f307k = false;

    /* renamed from: l, reason: collision with root package name */
    private boolean f308l = false;

    /* renamed from: m, reason: collision with root package name */
    private int[] f309m = null;

    /* renamed from: n, reason: collision with root package name */
    private int f310n = 0;

    /* renamed from: o, reason: collision with root package name */
    private int f311o = 2;

    /* renamed from: p, reason: collision with root package name */
    private int[] f312p = null;

    /* renamed from: q, reason: collision with root package name */
    private String[] f313q = null;

    /* renamed from: r, reason: collision with root package name */
    private H[] f314r = null;

    /* renamed from: s, reason: collision with root package name */
    private H[] f315s = null;

    /* renamed from: t, reason: collision with root package name */
    private H[] f316t = null;

    /* renamed from: u, reason: collision with root package name */
    private H[] f317u = null;

    /* renamed from: v, reason: collision with root package name */
    private H[] f318v = null;

    /* renamed from: w, reason: collision with root package name */
    private H[] f319w = null;

    /* renamed from: x, reason: collision with root package name */
    private H f320x = null;

    /* renamed from: y, reason: collision with root package name */
    private H f321y = null;

    /* renamed from: z, reason: collision with root package name */
    private H f322z = null;

    /* renamed from: A, reason: collision with root package name */
    private H f323A = null;

    /* renamed from: B, reason: collision with root package name */
    private H f324B = null;

    /* renamed from: C, reason: collision with root package name */
    private H f325C = null;

    /* renamed from: D, reason: collision with root package name */
    private String f326D = null;

    /* renamed from: E, reason: collision with root package name */
    private H f327E = null;

    /* renamed from: F, reason: collision with root package name */
    private H f328F = null;

    /* renamed from: G, reason: collision with root package name */
    private H f329G = null;

    /* renamed from: H, reason: collision with root package name */
    private H f330H = null;

    /* renamed from: I, reason: collision with root package name */
    private int f331I = 0;

    /* renamed from: J, reason: collision with root package name */
    private int f332J = -1;

    /* renamed from: K, reason: collision with root package name */
    private int f333K = -1;

    /* renamed from: L, reason: collision with root package name */
    private int f334L = 15;

    /* renamed from: M, reason: collision with root package name */
    private int f335M = 0;

    /* renamed from: N, reason: collision with root package name */
    private int f336N = 300;

    /* renamed from: O, reason: collision with root package name */
    private int f338O = f337a;

    /* renamed from: P, reason: collision with root package name */
    private int f339P = 0;

    /* renamed from: Q, reason: collision with root package name */
    private final Map f340Q = new HashMap();

    /* renamed from: R, reason: collision with root package name */
    private int f341R = SerialPort.BAUDRATE_115200;

    /* renamed from: S, reason: collision with root package name */
    private int f342S = -1;

    /* renamed from: T, reason: collision with root package name */
    private String f343T = "COM1";

    /* renamed from: U, reason: collision with root package name */
    private String f344U = "t";

    /* renamed from: V, reason: collision with root package name */
    private String f345V = null;

    /* renamed from: W, reason: collision with root package name */
    private boolean f346W = false;

    /* renamed from: X, reason: collision with root package name */
    private int f347X = -1;

    /* renamed from: Y, reason: collision with root package name */
    private int f348Y = -1;

    /* renamed from: Z, reason: collision with root package name */
    private int f349Z = -1;

    /* renamed from: aa, reason: collision with root package name */
    private boolean f350aa = false;

    /* renamed from: ab, reason: collision with root package name */
    private boolean f351ab = true;

    /* renamed from: ac, reason: collision with root package name */
    private int f352ac = 15;

    /* renamed from: ad, reason: collision with root package name */
    private boolean f353ad = false;

    /* renamed from: ae, reason: collision with root package name */
    private boolean f354ae = false;

    /* renamed from: af, reason: collision with root package name */
    private int f355af = -1;

    /* renamed from: ag, reason: collision with root package name */
    private int f356ag = -1;

    /* renamed from: ah, reason: collision with root package name */
    private String f357ah = "";

    /* renamed from: ai, reason: collision with root package name */
    private int[] f358ai = null;

    /* renamed from: aj, reason: collision with root package name */
    private int f359aj = Integer.MAX_VALUE;

    /* renamed from: ak, reason: collision with root package name */
    private boolean f360ak = false;

    /* renamed from: al, reason: collision with root package name */
    private boolean f361al = true;

    /* renamed from: am, reason: collision with root package name */
    private boolean f362am = false;

    /* renamed from: an, reason: collision with root package name */
    private boolean f363an = true;

    /* renamed from: ao, reason: collision with root package name */
    private cT f364ao = null;

    /* renamed from: ap, reason: collision with root package name */
    private cP f365ap = null;

    /* renamed from: aq, reason: collision with root package name */
    private String f366aq = null;

    /* renamed from: ar, reason: collision with root package name */
    private List f367ar = null;

    /* renamed from: as, reason: collision with root package name */
    private List f368as = null;

    /* renamed from: at, reason: collision with root package name */
    private int f369at = 50;

    /* renamed from: au, reason: collision with root package name */
    private boolean f370au = false;

    /* renamed from: av, reason: collision with root package name */
    private boolean f371av = true;

    /* renamed from: aw, reason: collision with root package name */
    private dh f372aw = null;

    /* renamed from: ax, reason: collision with root package name */
    private dh f373ax = null;

    /* renamed from: ay, reason: collision with root package name */
    private dh f374ay = null;

    /* renamed from: az, reason: collision with root package name */
    private dh f375az = null;

    /* renamed from: aA, reason: collision with root package name */
    private dh f376aA = null;

    /* renamed from: aB, reason: collision with root package name */
    private String f377aB = null;

    /* renamed from: aC, reason: collision with root package name */
    private boolean f378aC = false;

    /* renamed from: aD, reason: collision with root package name */
    private boolean f379aD = false;

    /* renamed from: aE, reason: collision with root package name */
    private boolean f380aE = true;

    /* renamed from: aF, reason: collision with root package name */
    private boolean f381aF = true;

    /* renamed from: aG, reason: collision with root package name */
    private boolean f382aG = true;

    /* renamed from: aH, reason: collision with root package name */
    private boolean f383aH = false;

    /* renamed from: aI, reason: collision with root package name */
    private long f384aI = 0;

    /* renamed from: aJ, reason: collision with root package name */
    private boolean f385aJ = false;

    /* renamed from: aK, reason: collision with root package name */
    private String f386aK = null;

    /* renamed from: aL, reason: collision with root package name */
    private dh f387aL = null;

    /* renamed from: aM, reason: collision with root package name */
    private String f388aM = null;

    /* renamed from: aN, reason: collision with root package name */
    private dh f389aN = null;

    /* renamed from: b, reason: collision with root package name */
    List f390b = new ArrayList();

    /* renamed from: c, reason: collision with root package name */
    List f391c = null;

    /* renamed from: d, reason: collision with root package name */
    int f392d = 0;

    public static boolean a(String str) {
        return str.equals("pageReadCommand") || str.equals("pageIdentifier") || str.equals("pageValueWrite") || str.equals("pageChunkWrite") || str.equals("pageActivate") || str.equals("pageActivationDelay") || str.equals("blockReadTimeout") || str.equals("writeBlocks") || str.equals("interWriteDelay") || str.equals("endianness") || str.equals("nPages") || str.equals("ochBlockSize") || str.equals("ochGetCommand") || str.equals("queryCommand") || str.equals("versionInfo") || str.equals("pageSize") || str.equals("burnCommand") || str.equals("tableWriteCommand") || str.equals("blockingFactor") || str.equals("getCommand") || str.equals("crc32CheckCommand") || str.equals("messageEnvelopeFormat") || str.equals("tsWriteBlocks") || str.equals("filterEchoBytes") || str.equals("validateArrayBounds") || str.equals("tableBlockingFactor") || str.equals("tableCrcCommand") || str.equals("sendTablesWithoutEnvelope") || str.equals("maxUnusedRuntimeRange") || str.equals("turboBaudOnCommand") || str.equals("turboBaudOffCommand") || str.equals("scatteredOchGetCommand") || str.equals("retrieveConfigError") || str.equals("useLegacyFTempUnits") || str.equals("enable2ndByteCanId") || str.equals("outputChannelStartOffset") || str.equals("parameterStartOffset") || str.equals("envelopedScanCommands") || str.equals("replayConfigTable") || str.equals("replayReadCommand") || str.equals("replayRecordCountParam") || str.equals("defaultBaudRate") || str.equals("refreshLocalStoreOnActivity") || str.equals("delayAfterPortOpen") || str.equals("protocol") || str.equals("restrictSquirtRelationship") || str.equals("forceBigEndianProtocol") || str.equals("readSdCompressed") || str.equals("noCommReadDelay") || str.equals("defaultIpAddress") || str.equals("defaultIpPort") || str.equals("defaultRuntimeRecordPerSec") || str.equals("ignoreMissingBitOptions");
    }

    public void a(String str, String str2) throws V.g {
        try {
            if (str.equals("pageReadCommand")) {
                c(bH.W.c(str2, ","));
            } else if (str.equals("pageIdentifier")) {
                d(bH.W.c(str2, ","));
            } else if (str.equals("pageValueWrite")) {
                e(bH.W.c(str2, ","));
            } else if (str.equals("pageChunkWrite")) {
                f(bH.W.c(str2, ","));
            } else if (str.equals("crc32CheckCommand")) {
                g(bH.W.c(str2, ","));
            } else if (str.equals("burnCommand")) {
                a(bH.W.c(str2, ","));
            } else if (str.equals("pageActivate")) {
                h(bH.W.c(str2, ","));
            } else if (str.equals("pageActivationDelay")) {
                b(bH.W.g(str2));
            } else if (str.equals("blockReadTimeout")) {
                c(bH.W.g(str2));
            } else if (str.equals("writeBlocks")) {
                a(str2.equals(FXMLLoader.EVENT_HANDLER_PREFIX));
            } else if (str.equals("tsWriteBlocks")) {
                e(str2.equals(FXMLLoader.EVENT_HANDLER_PREFIX));
            } else if (str.equals("interWriteDelay")) {
                d(bH.W.g(str2));
            } else if (str.equals("endianness")) {
                d(str2);
            } else if (str.equals("nPages")) {
                a(bH.W.g(str2));
            } else if (str.equals("pageSize")) {
                a(bH.W.d(str2, ","));
            } else if (str.equals("ochBlockSize")) {
                o(bH.W.g(str2));
            } else if (str.equals("ochGetCommand")) {
                f(str2);
            } else if (str.equals("getCommand")) {
                g(str2);
            } else if (str.equals("queryCommand")) {
                h(str2);
            } else if (str.equals("versionInfo")) {
                i(str2);
            } else if (str.equals("tableWriteCommand")) {
                t(str2);
            } else if (str.equals("blockingFactor")) {
                I(bH.W.g(str2));
            } else if (str.equals("messageEnvelopeFormat")) {
                c(str2);
            } else if (str.equals("filterEchoBytes")) {
                f(Boolean.parseBoolean(str2));
            } else if (str.equals("validateArrayBounds")) {
                g(Boolean.parseBoolean(str2));
            } else if (str.equals("tableBlockingFactor")) {
                H(bH.W.g(str2));
            } else if (str.equals("maxUnusedRuntimeRange")) {
                v(bH.W.g(str2));
            } else if (str.equals("tableCrcCommand")) {
                n(str2);
            } else if (str.equals("sendTablesWithoutEnvelope")) {
                h(Boolean.parseBoolean(str2) || str2.equals(FXMLLoader.EVENT_HANDLER_PREFIX));
            } else if (str.equals("retrieveConfigError")) {
                B(str2);
            } else if (str.equals("turboBaudOnCommand")) {
                D(str2);
            } else if (str.equals("turboBaudOffCommand")) {
                E(str2);
            } else if (str.equals("scatteredOchGetCommand")) {
                o(str2);
            } else if (str.equals("useLegacyFTempUnits")) {
                k(Boolean.parseBoolean(str2));
            } else if (str.equals("enable2ndByteCanId")) {
                l(Boolean.parseBoolean(str2));
            } else if (str.equals("parameterStartOffset")) {
                b(bH.W.d(str2, ","));
            } else if (str.equals("outputChannelStartOffset")) {
                z(bH.W.g(str2));
            } else if (str.equals("envelopedScanCommands")) {
                d(str2.equals(Enabled.NAME));
            } else if (str.equals("replayConfigTable")) {
                q(str2);
            } else if (str.equals("replayReadCommand")) {
                C(str2);
            } else if (str.equals("replayRecordCountParam")) {
                r(str2);
            } else if (str.equals("protocol")) {
                s(str2);
            } else if (str.equals("defaultBaudRate")) {
                J(bH.W.g(str2));
            } else if (str.equals("refreshLocalStoreOnActivity")) {
                m(Boolean.parseBoolean(str2));
            } else if (str.equals("delayAfterPortOpen")) {
                A(bH.W.g(str2));
            } else if (str.equals("restrictSquirtRelationship")) {
                this.f361al = Boolean.parseBoolean(str2);
            } else if (str.equals("forceBigEndianProtocol")) {
                this.f362am = Boolean.parseBoolean(str2);
            } else if (str.equals("readSdCompressed")) {
                this.f382aG = Boolean.parseBoolean(str2);
            } else if (str.equals("noCommReadDelay")) {
                o(Boolean.parseBoolean(str2));
            } else if (str.equals("defaultRuntimeRecordPerSec")) {
                C(Integer.parseInt(str2));
            } else if (str.equals("defaultIpAddress")) {
                A.l.a(str2);
            } else if (str.equals("defaultIpPort")) {
                A.l.a(Integer.parseInt(str2));
            } else {
                if (!str.equals("ignoreMissingBitOptions")) {
                    throw new V.g("Command:" + str + " undefined.");
                }
                p(Boolean.parseBoolean(str2));
            }
        } catch (Exception e2) {
            throw new V.g("Error setting:" + str + " with value:" + str2 + " \nReason: " + e2.getMessage());
        }
    }

    public void a(String str, String str2, int i2) throws V.g {
        if (i2 < 0) {
            a(str, str2);
            return;
        }
        try {
            if (str.equals("pageReadCommand")) {
                e(str2, i2);
            } else if (str.equals("pageIdentifier")) {
                f(str2, i2);
            } else if (str.equals("pageValueWrite")) {
                g(str2, i2);
            } else if (str.equals("pageChunkWrite")) {
                h(str2, i2);
            } else if (str.equals("crc32CheckCommand")) {
                i(str2, i2);
            } else if (str.equals("burnCommand")) {
                a(str2, i2);
            } else if (str.equals("pageActivate")) {
                j(str2, i2);
            } else if (str.equals("pageActivationDelay")) {
                b(bH.W.g(str2));
            } else if (str.equals("blockReadTimeout")) {
                c(bH.W.g(str2));
            } else if (str.equals("writeBlocks")) {
                a(str2.equals(FXMLLoader.EVENT_HANDLER_PREFIX));
            } else if (str.equals("tsWriteBlocks")) {
                e(str2.equals(FXMLLoader.EVENT_HANDLER_PREFIX));
            } else if (str.equals("interWriteDelay")) {
                d(bH.W.g(str2));
            } else if (str.equals("endianness")) {
                d(str2);
            } else if (str.equals("nPages")) {
                a(bH.W.g(str2));
            } else if (str.equals("pageSize")) {
                b(str2, i2);
            } else if (str.equals("ochBlockSize")) {
                o(bH.W.g(str2));
            } else if (str.equals("ochGetCommand")) {
                f(str2);
            } else if (str.equals("getCommand")) {
                g(str2);
            } else if (str.equals("queryCommand")) {
                h(str2);
            } else if (str.equals("versionInfo")) {
                i(str2);
            } else if (str.equals("tableWriteCommand")) {
                t(str2);
            } else if (str.equals("blockingFactor")) {
                I(bH.W.g(str2));
            } else if (str.equals("messageEnvelopeFormat")) {
                c(str2);
            } else if (str.equals("filterEchoBytes")) {
                f(Boolean.parseBoolean(str2));
            } else if (str.equals("validateArrayBounds")) {
                g(Boolean.parseBoolean(str2));
            } else if (str.equals("tableBlockingFactor")) {
                H(bH.W.g(str2));
            } else if (str.equals("maxUnusedRuntimeRange")) {
                v(bH.W.g(str2));
            } else if (str.equals("tableCrcCommand")) {
                n(str2);
            } else if (str.equals("sendTablesWithoutEnvelope")) {
                h(Boolean.parseBoolean(str2) || str2.equals(FXMLLoader.EVENT_HANDLER_PREFIX));
            } else if (str.equals("retrieveConfigError")) {
                B(str2);
            } else if (str.equals("turboBaudOnCommand")) {
                D(str2);
            } else if (str.equals("turboBaudOffCommand")) {
                E(str2);
            } else if (str.equals("scatteredOchGetCommand")) {
                o(str2);
            } else if (str.equals("useLegacyFTempUnits")) {
                k(Boolean.parseBoolean(str2));
            } else if (str.equals("enable2ndByteCanId")) {
                l(Boolean.parseBoolean(str2));
            } else if (str.equals("parameterStartOffset")) {
                b(bH.W.d(str2, ","));
            } else if (str.equals("outputChannelStartOffset")) {
                z(bH.W.g(str2));
            } else if (str.equals("envelopedScanCommands")) {
                d(str2.equals(Enabled.NAME));
            } else if (str.equals("replayConfigTable")) {
                q(str2);
            } else if (str.equals("replayReadCommand")) {
                C(str2);
            } else if (str.equals("replayRecordCountParam")) {
                r(str2);
            } else if (str.equals("protocol")) {
                s(str2);
            } else if (str.equals("defaultBaudRate")) {
                J(bH.W.g(str2));
            } else if (str.equals("refreshLocalStoreOnActivity")) {
                m(Boolean.parseBoolean(str2));
            } else if (str.equals("delayAfterPortOpen")) {
                A(bH.W.g(str2));
            } else if (str.equals("restrictSquirtRelationship")) {
                this.f361al = Boolean.parseBoolean(str2);
            } else if (str.equals("forceBigEndianProtocol")) {
                this.f362am = Boolean.parseBoolean(str2);
            } else if (str.equals("readSdCompressed")) {
                this.f382aG = Boolean.parseBoolean(str2);
            } else if (str.equals("noCommReadDelay")) {
                o(Boolean.parseBoolean(str2));
            } else if (str.equals("defaultRuntimeRecordPerSec")) {
                C(Integer.parseInt(str2));
            } else if (str.equals("defaultIpAddress")) {
                A.l.a(str2);
            } else if (str.equals("defaultIpPort")) {
                A.l.a(Integer.parseInt(str2));
            } else {
                if (!str.equals("ignoreMissingBitOptions")) {
                    throw new V.g("Command:" + str + " undefined.");
                }
                p(Boolean.parseBoolean(str2));
            }
        } catch (Exception e2) {
            throw new V.g("Error setting:" + str + " with value:" + str2 + " \nReason: " + e2.getMessage());
        }
    }

    public void a() {
        if (this.f365ap != null) {
            this.f365ap.e();
        }
    }

    public void a(String str, String[] strArr) throws V.g {
        G g2 = new G(this);
        for (int i2 = 0; i2 < strArr.length; i2++) {
            if (this.f340Q.containsKey(strArr[i2])) {
                Iterator it = ((G) this.f340Q.get(strArr[i2])).iterator();
                while (it.hasNext()) {
                    g2.add(it.next());
                }
            } else {
                if (!strArr[i2].trim().startsWith(PdfOps.DOUBLE_QUOTE__TOKEN) || !strArr[i2].trim().endsWith(PdfOps.DOUBLE_QUOTE__TOKEN)) {
                    throw new V.g("Malformed Command String: " + strArr[i2]);
                }
                H hC = c(x(strArr[i2]), 0);
                if (hC.c()) {
                    a(hC);
                }
                G g3 = new G(this);
                g3.add(hC);
                this.f340Q.put(strArr[i2], g3);
                g2.add(hC);
            }
        }
        this.f340Q.put(str, g2);
    }

    private String x(String str) {
        if (str.charAt(0) == '\"') {
            str = str.substring(1);
        }
        if (str.endsWith(PdfOps.DOUBLE_QUOTE__TOKEN)) {
            str = str.substring(0, str.length() - 1);
        }
        return str;
    }

    public G b(String str) {
        return (G) this.f340Q.get(str);
    }

    public boolean b() {
        Iterator it = this.f340Q.keySet().iterator();
        while (it.hasNext()) {
            if (b((String) it.next()).a()) {
                return true;
            }
        }
        return false;
    }

    public Collection c() {
        ArrayList arrayList = new ArrayList();
        Iterator it = this.f340Q.keySet().iterator();
        while (it.hasNext()) {
            G gB = b((String) it.next());
            if (gB.a()) {
                arrayList.add(gB);
            }
        }
        return arrayList;
    }

    public List d() {
        if (this.f391c == null) {
            this.f391c = new ArrayList();
            for (String str : this.f340Q.keySet()) {
                if (!str.contains(PdfOps.DOUBLE_QUOTE__TOKEN)) {
                    this.f391c.add(str);
                }
            }
        }
        return this.f391c;
    }

    public boolean a(String str, String str2, boolean z2) {
        Iterator it = this.f340Q.keySet().iterator();
        while (it.hasNext()) {
            if (((String) it.next()).equals(str)) {
                b(str).a(z2);
                b(str).a(str2);
                return true;
            }
        }
        return false;
    }

    public void c(String str) throws V.g {
        if (!str.equals("msEnvelope_1.0")) {
            throw new V.g("Unknown Envelope format defined for setting messageEnvelopeFormat\nKnown format(s): msEnvelope_1.0");
        }
        J.f fVar = new J.f();
        fVar.d(this.f332J);
        a(fVar);
        a(new J.g());
    }

    public boolean e() {
        return f().equals("big");
    }

    public String f() {
        return this.f302f;
    }

    public void d(String str) {
        this.f302f = str;
    }

    public int g() {
        return this.f303g;
    }

    public void a(int i2) {
        this.f303g = i2;
    }

    public int h() {
        return this.f304h;
    }

    public void b(int i2) {
        this.f304h = i2;
    }

    public int i() {
        return this.f365ap != null ? this.f305i : (this.f305i * 3) / 2;
    }

    public void c(int i2) {
        this.f305i = i2;
    }

    public boolean j() {
        return H() || (this.f306j && (D() instanceof J.f));
    }

    public void a(boolean z2) {
        bH.C.d("Set Write Blocks on = " + z2);
        this.f306j = z2;
    }

    public int k() {
        if (!this.f306j || this.f311o >= 3) {
            return this.f311o;
        }
        return 3;
    }

    public void d(int i2) {
        this.f311o = i2;
    }

    public H e(int i2) {
        if (az() == null) {
            return null;
        }
        return (i2 < 0 || az().length <= i2) ? az()[0] : az()[i2];
    }

    public void a(String[] strArr) {
        a(a(strArr, this.f309m));
    }

    public void a(String str, int i2) throws V.g {
        if (this.f314r == null) {
            this.f314r = new H[this.f303g];
        }
        a(this.f314r, str, i2);
    }

    public void b(String[] strArr) {
        H[] hArrA = a(strArr, this.f309m);
        if (this.f314r != null && this.f314r.length == hArrA.length) {
            for (int i2 = 0; i2 < hArrA.length; i2++) {
                if (this.f314r[i2].b().isEmpty()) {
                    hArrA[i2] = this.f314r[i2];
                }
            }
        }
        a(hArrA);
    }

    public int f(int i2) {
        return this.f312p[i2];
    }

    public int[] l() {
        return this.f312p;
    }

    public void a(int[] iArr) {
        this.f312p = iArr;
    }

    public void b(String str, int i2) throws V.g {
        if (this.f303g < i2) {
            throw new V.g("Attempt to set page size on page number higher than set number of pages! Was nPages set before page declaration?");
        }
        try {
            int i3 = Integer.parseInt(str);
            if (this.f312p == null) {
                this.f312p = new int[this.f303g];
            }
            this.f312p[i2] = i3;
        } catch (NumberFormatException e2) {
            throw new V.g("Invalid integer value for pageSize: " + str);
        }
    }

    public byte[] g(int i2) {
        return a(i2, 0, f(i2));
    }

    public H h(int i2) {
        return i2 >= aA().length ? aA()[0] : aA()[i2];
    }

    public byte[] a(int i2, int i3, int i4) {
        return h(i2).a(i3, i4, null);
    }

    public byte[] a(String str, int[] iArr) throws V.g {
        if (aw() == null || aw().length() < 1) {
            throw new V.g("Table write command not set in current configuration.\nCan not generate a valid send command.");
        }
        byte[] bArr = new byte[iArr.length + 2];
        try {
            bArr[0] = aw().getBytes()[0];
            bArr[1] = z(str);
            for (int i2 = 0; i2 < iArr.length; i2++) {
                bArr[i2 + 2] = (byte) iArr[i2];
            }
            return bArr;
        } catch (Exception e2) {
            throw new V.g("Error generating Table write command.", e2);
        }
    }

    public int m() {
        return i() * 3;
    }

    public H c(String str, int i2) {
        H h2 = new H(this, i2);
        h2.a(str);
        if (!h2.c()) {
            return a(h2);
        }
        T.a().a(h2);
        return h2;
    }

    public H e(String str) {
        H h2 = new H(this, 0);
        h2.a(str);
        return a(h2);
    }

    public H a(H h2) throws V.g {
        String strB = h2.b();
        try {
            boolean zC = h2.c();
            if (zC) {
                strB = y(strB);
            }
            int iIndexOf = strB.indexOf(FXMLLoader.RESOURCE_KEY_PREFIX) != -1 ? strB.indexOf(FXMLLoader.RESOURCE_KEY_PREFIX) : strB.length();
            if (iIndexOf == -1 && strB.indexOf(FXMLLoader.ESCAPE_PREFIX) == -1 && strB.indexOf(FXMLLoader.RESOURCE_KEY_PREFIX) == -1) {
                h2.b("");
                h2.a(strB.getBytes());
                return h2;
            }
            h2.b(strB.substring(iIndexOf));
            if (h2.e().indexOf("%2o") != -1) {
                h2.a(2);
            } else if (h2.e().indexOf("%o") != -1) {
                h2.a(1);
            }
            if (h2.e().indexOf("%2c") != -1) {
                h2.b(2);
            } else if (h2.e().indexOf("%c") != -1) {
                h2.b(1);
            }
            String strSubstring = strB.substring(0, iIndexOf);
            A(strSubstring);
            h2.a(d(strSubstring, zC ? -1 : x()));
            return h2;
        } catch (NumberFormatException e2) {
            e2.printStackTrace();
            throw new V.g("Unable to parse SendCommand:" + strB + "\nCould not convert to number " + e2.getMessage(), e2);
        } catch (Exception e3) {
            e3.printStackTrace();
            throw new V.g("Unable to parse SendCommand:" + strB, e3);
        }
    }

    private String y(String str) {
        ArrayList arrayListA = dj.a(str);
        aI aIVarE = E();
        Iterator it = arrayListA.iterator();
        while (it.hasNext()) {
            String str2 = (String) it.next();
            String strB = bH.W.b(str2, FXMLLoader.EXPRESSION_PREFIX, "");
            try {
                int iA = (int) C0126i.a(aIVarE, strB);
                if (aIVarE.c(strB).i().equals(ControllerParameter.PARAM_CLASS_ARRAY)) {
                    bH.C.a("$Var used in config file, Can not be an array. Line:\n" + str, null, null);
                } else {
                    String upperCase = Integer.toString(iA, 16).toUpperCase();
                    if (upperCase.length() > 2) {
                        upperCase = upperCase.substring(upperCase.length() - 2, upperCase.length());
                    }
                    while (upperCase.length() < 2) {
                        upperCase = "0" + upperCase;
                    }
                    str = bH.W.b(str, str2, LanguageTag.PRIVATEUSE + upperCase);
                }
            } catch (V.g e2) {
                int i2 = this.f392d;
                this.f392d = i2 + 1;
                if (i2 < 3) {
                    bH.C.a("$Var name used in config file, but not found as a valid Parameter. Line:\n" + str, e2, null);
                }
            } catch (Exception e3) {
                int i3 = this.f392d;
                this.f392d = i3 + 1;
                if (i3 < 3) {
                    bH.C.a("Error trying to use $Var name config file row. Line:\n" + str, e3, null);
                }
            }
        }
        return str;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void e(H h2) {
        aR.a().a(h2);
        Iterator it = dj.a(h2.b()).iterator();
        while (it.hasNext()) {
            String strB = bH.W.b((String) it.next(), FXMLLoader.EXPRESSION_PREFIX, "");
            try {
                aR.a().a(this.f357ah, strB, h2);
            } catch (V.a e2) {
                bH.C.d(strB + " appears to be an invalid EcuParameter");
                Logger.getLogger(F.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
            }
        }
    }

    public static byte[] d(String str, int i2) {
        StringTokenizer stringTokenizer = new StringTokenizer(str, FXMLLoader.ESCAPE_PREFIX);
        byte[] bArr = new byte[stringTokenizer.countTokens()];
        int i3 = 0;
        while (stringTokenizer.hasMoreTokens()) {
            String strNextToken = stringTokenizer.nextToken();
            if (i3 == 0 && !str.startsWith(FXMLLoader.ESCAPE_PREFIX)) {
                bArr[i3] = strNextToken.getBytes()[0];
            } else if (i3 != 1 || i2 < 0 || bArr.length <= 2 || z(strNextToken) > 16) {
                bArr[i3] = z(strNextToken);
            } else {
                bArr[i3] = (byte) i2;
            }
            i3++;
        }
        return bArr;
    }

    private static byte z(String str) {
        int i2;
        if (str.startsWith(LanguageTag.PRIVATEUSE)) {
            str = str.substring(1);
            i2 = 16;
        } else {
            i2 = 8;
        }
        return (byte) Integer.parseInt(str, i2);
    }

    private H[] a(String[] strArr, int[] iArr) {
        H[] hArr = new H[strArr.length];
        for (int i2 = 0; i2 < hArr.length; i2++) {
            String strB = strArr[i2];
            if (strB != null && strB.indexOf("%2i") != -1) {
                strB = bH.W.b(strB, "%2i", aF()[i2]);
            }
            hArr[i2] = c(strB, y(i2));
        }
        return hArr;
    }

    private H[] a(H[] hArr, String str, int i2) throws V.g {
        if (str != null && str.indexOf("%2i") != -1) {
            if (aF() == null || aF().length < i2 - 1 || aF()[i2] == null || aF()[i2].isEmpty()) {
                throw new V.g("2 byte pageIdentifier used, but no page identifier has been defined for previously for page");
            }
            str = bH.W.b(str, "%2i", aF()[i2]);
        }
        if (i2 > hArr.length - 1) {
            throw new V.g("Attempt to set command on page higher than defined page count.");
        }
        hArr[i2] = c(str, y(i2));
        return hArr;
    }

    public void c(String[] strArr) {
        b(a(strArr, this.f309m));
    }

    public void e(String str, int i2) throws V.g {
        if (this.f315s == null) {
            this.f315s = new H[this.f303g];
        }
        a(this.f315s, str, i2);
    }

    public void d(String[] strArr) {
        this.f313q = strArr;
    }

    public void f(String str, int i2) {
        if (this.f313q == null) {
            this.f313q = new String[this.f303g];
        }
        this.f313q[i2] = str;
    }

    public H i(int i2) {
        if (aB() == null || aB().length <= i2) {
            return null;
        }
        return aB()[i2];
    }

    public void e(String[] strArr) {
        if (strArr != null) {
            c(a(strArr, this.f309m));
        } else {
            c((H[]) null);
        }
    }

    public void g(String str, int i2) throws V.g {
        if (this.f316t == null) {
            this.f316t = new H[this.f303g];
        }
        a(this.f316t, str, i2);
    }

    public H j(int i2) {
        if (aC() == null || aC().length <= i2) {
            return null;
        }
        return aC()[i2];
    }

    public void f(String[] strArr) {
        d(a(strArr, this.f309m));
    }

    public void h(String str, int i2) throws V.g {
        if (this.f317u == null) {
            this.f317u = new H[this.f303g];
        }
        a(this.f317u, str, i2);
    }

    public boolean k(int i2) {
        return aD() != null && aD().length > i2;
    }

    public H l(int i2) {
        if (aD() != null) {
            return aD()[i2];
        }
        return null;
    }

    public void g(String[] strArr) {
        e(a(strArr, this.f309m));
    }

    public void i(String str, int i2) throws V.g {
        if (this.f319w == null) {
            this.f319w = new H[this.f303g];
        }
        a(this.f319w, str, i2);
    }

    public byte[] m(int i2) {
        if (aE() == null || aE().length == 0) {
            return null;
        }
        return i2 >= aE().length ? aE()[0].d() : aE()[i2].d();
    }

    public void h(String[] strArr) {
        f(a(strArr, this.f309m));
    }

    public void j(String str, int i2) throws V.g {
        if (this.f318v == null) {
            this.f318v = new H[this.f303g];
        }
        a(this.f318v, str, i2);
    }

    public void n(int i2) {
        this.f331I = i2;
    }

    public int n() {
        aI aIVarE = E();
        if (this.f332J == -1 && aIVarE != null) {
            int iA = 0;
            Iterator itQ = aIVarE.K().q();
            while (itQ.hasNext()) {
                aH aHVar = (aH) itQ.next();
                if (aHVar.a() + aHVar.l() > iA) {
                    iA = aHVar.a() + aHVar.l();
                }
            }
            this.f332J = iA;
        }
        return this.f332J;
    }

    public void o(int i2) {
        this.f332J = i2;
        if (D() == null || !(D() instanceof J.f)) {
            return;
        }
        ((J.f) D()).d(i2);
    }

    public H o() {
        return this.f320x;
    }

    public void f(String str) {
        this.f370au = str.contains("%2o") && str.contains("%2c");
        this.f320x = c(str, this.f310n);
    }

    public void g(String str) {
        this.f321y = c(str, this.f310n);
    }

    public H p() {
        return this.f322z;
    }

    public void h(String str) {
        this.f322z = c(str, 0);
    }

    public H q() {
        return this.f323A;
    }

    public void i(String str) {
        this.f323A = c(str, 0);
    }

    public int r() {
        return this.f342S > 0 ? this.f342S : aO();
    }

    public void j(String str) {
        if (str != null) {
            this.f342S = bH.W.g(str);
        }
    }

    public void p(int i2) {
        this.f342S = i2;
    }

    public String s() {
        return this.f343T;
    }

    public void k(String str) {
        this.f343T = str;
    }

    public int t() {
        return this.f334L;
    }

    public void q(int i2) {
        this.f334L = i2;
    }

    public String u() {
        return this.f357ah;
    }

    public void l(String str) {
        this.f357ah = str;
    }

    public boolean v() {
        return this.f346W;
    }

    public void b(boolean z2) {
        this.f346W = z2;
    }

    public H w() {
        return this.f321y != null ? this.f321y : this.f320x;
    }

    private void f(H h2) {
        this.f321y = h2;
    }

    public int x() {
        return this.f347X == -1 ? this.f348Y : this.f347X;
    }

    public void r(int i2) throws SecurityException {
        if (this.f380aE) {
            this.f347X = i2;
        }
        aN();
    }

    public String y() {
        return this.f345V;
    }

    public void m(String str) {
        this.f345V = str;
    }

    public int z() {
        return this.f339P;
    }

    public void s(int i2) {
        this.f339P = i2;
    }

    public int A() {
        return this.f355af;
    }

    public void t(int i2) {
        this.f355af = i2;
    }

    public int B() {
        return this.f356ag;
    }

    public void u(int i2) {
        this.f356ag = i2;
    }

    public cT C() {
        return this.f364ao;
    }

    public void a(cT cTVar) {
        this.f364ao = cTVar;
    }

    public cP D() {
        return this.f365ap;
    }

    public void a(cP cPVar) {
        this.f365ap = cPVar;
    }

    public aI E() {
        if (this.f366aq != null) {
            return C0125h.a().a(this.f366aq);
        }
        return null;
    }

    public void a(aI aIVar) {
        if (aIVar != null) {
            this.f366aq = aIVar.ac();
        } else {
            this.f366aq = null;
        }
    }

    public boolean F() {
        return this.f351ab;
    }

    public void c(boolean z2) {
        this.f351ab = z2;
    }

    public boolean G() {
        return this.f350aa;
    }

    public void d(boolean z2) {
        this.f350aa = z2;
    }

    private int A(String str) {
        if (this.f348Y == -1 && str.length() > 2 && str.startsWith(PdfOps.w_TOKEN)) {
            StringTokenizer stringTokenizer = new StringTokenizer(str, FXMLLoader.ESCAPE_PREFIX);
            int iCountTokens = stringTokenizer.countTokens();
            int i2 = 0;
            while (stringTokenizer.hasMoreTokens()) {
                String strNextToken = stringTokenizer.nextToken();
                if (i2 == 1 && strNextToken.startsWith(LanguageTag.PRIVATEUSE) && iCountTokens > 2) {
                    this.f348Y = z(strNextToken);
                }
                i2++;
            }
        }
        return this.f348Y;
    }

    public boolean H() {
        return this.f307k;
    }

    public void e(boolean z2) {
        this.f307k = z2;
    }

    public void f(boolean z2) {
        bH.C.d("FilterEchoBytes activated:" + z2);
        this.f354ae = z2;
    }

    public boolean I() {
        return this.f360ak;
    }

    public void g(boolean z2) {
        this.f360ak = z2;
    }

    public String J() {
        return this.f326D;
    }

    public void n(String str) {
        this.f326D = str;
    }

    public boolean K() {
        return this.f308l;
    }

    public void h(boolean z2) {
        this.f308l = z2;
    }

    @Override // G.InterfaceC0107cm
    public void a(List list) {
        if (list == null) {
            this.f367ar = null;
        } else {
            this.f367ar = new ArrayList();
            this.f367ar.addAll(list);
        }
    }

    public List L() {
        return this.f367ar;
    }

    public int M() {
        return this.f369at;
    }

    public void v(int i2) {
        this.f369at = i2;
    }

    public boolean N() {
        return this.f370au && this.f371av;
    }

    private void B(String str) {
        this.f325C = c(str, 0);
    }

    public H O() {
        return this.f325C;
    }

    private void C(String str) {
        this.f327E = c(str, 0);
    }

    public int P() {
        return this.f349Z;
    }

    public void w(int i2) {
        this.f349Z = i2;
    }

    private void D(String str) {
        this.f328F = c(str, 0);
    }

    private void E(String str) {
        this.f329G = c(str, 0);
    }

    public H Q() {
        return this.f328F;
    }

    public H R() {
        return this.f329G;
    }

    public dh S() {
        return this.f372aw;
    }

    public void a(dh dhVar) {
        this.f372aw = dhVar;
    }

    public boolean T() {
        return (this.f373ax == null || this.f373ax.a() == 0.0d) ? false : true;
    }

    public void b(dh dhVar) {
        this.f373ax = dhVar;
    }

    public boolean U() {
        return (this.f374ay == null || this.f374ay.a() == 0.0d) ? false : true;
    }

    public void c(dh dhVar) {
        this.f374ay = dhVar;
    }

    public boolean V() {
        return (this.f375az == null || this.f375az.a() == 0.0d) ? false : true;
    }

    public void d(dh dhVar) {
        this.f375az = dhVar;
    }

    public boolean W() {
        return this.f378aC;
    }

    public void i(boolean z2) {
        this.f378aC = z2;
        r(z2);
    }

    private void r(boolean z2) {
        Iterator it = this.f390b.iterator();
        while (it.hasNext()) {
            ((de) it.next()).a(z2);
        }
    }

    public void a(de deVar) {
        this.f390b.add(deVar);
    }

    public void b(de deVar) {
        this.f390b.remove(deVar);
    }

    public boolean X() {
        return (this.f376aA == null || this.f376aA.a() == 0.0d) ? false : true;
    }

    public void e(dh dhVar) {
        this.f376aA = dhVar;
    }

    public String Y() {
        if (this.f376aA instanceof bQ) {
            return ((bQ) this.f376aA).c();
        }
        return null;
    }

    public H Z() {
        return this.f330H;
    }

    public void o(String str) {
        this.f330H = c(str, 0);
    }

    public String aa() {
        return this.f377aB;
    }

    public void p(String str) {
        this.f377aB = str;
    }

    public List ab() {
        return this.f368as;
    }

    public void b(List list) {
        this.f333K = -1;
        if (list != null) {
            int iC = 0;
            Iterator it = list.iterator();
            while (it.hasNext()) {
                iC += ((C0140w) it.next()).c();
            }
            this.f333K = iC;
        }
        this.f368as = list;
    }

    public boolean ac() {
        return X() && this.f368as != null && ad() > 0 && !this.f379aD;
    }

    public int ad() {
        return this.f333K;
    }

    public boolean x(int i2) {
        return this.f314r != null && ((this.f314r.length > i2 && this.f314r[i2].d() != null && this.f314r[i2].d().length > 0) || this.f314r.length == 1);
    }

    public void j(boolean z2) {
        this.f379aD = z2;
    }

    public boolean ae() {
        return this.f363an;
    }

    public void k(boolean z2) {
        this.f363an = z2;
    }

    public void l(boolean z2) {
        this.f380aE = z2;
    }

    public int y(int i2) {
        if (i2 >= 0 && this.f309m != null && this.f309m.length > i2) {
            return this.f309m[i2];
        }
        return 0;
    }

    public void b(int[] iArr) {
        this.f309m = iArr;
    }

    public int af() {
        return this.f310n;
    }

    public void z(int i2) {
        this.f310n = i2;
    }

    public String ag() {
        return this.f386aK;
    }

    public void q(String str) {
        this.f386aK = str;
    }

    public void f(dh dhVar) {
        this.f387aL = dhVar;
    }

    public String ah() {
        return this.f388aM;
    }

    public void r(String str) {
        this.f388aM = str;
    }

    public int ai() {
        return (int) (this.f389aN == null ? 0.0d : this.f389aN.a());
    }

    public void g(dh dhVar) {
        this.f389aN = dhVar;
    }

    public boolean aj() {
        return this.f381aF;
    }

    public void m(boolean z2) {
        this.f381aF = z2;
    }

    public int ak() {
        return this.f335M;
    }

    public void A(int i2) {
        this.f335M = i2;
    }

    public boolean B(int i2) {
        return this.f301e.equals("XCP") || (this.f315s.length > i2 && this.f315s[i2].f() > 0 && this.f315s[i2].g() > 0);
    }

    public String al() {
        return this.f301e;
    }

    public void s(String str) throws V.g {
        if (!str.equals("basicRequestReply") && !str.equals("XCP")) {
            throw new V.g("Unsupported Protocol " + str + ", supported values: \"basicRequestReply\", XCP");
        }
        this.f301e = str;
    }

    public int am() {
        return this.f301e.equals("basicRequestReply") ? 6 : 0;
    }

    public boolean an() {
        return this.f361al;
    }

    public boolean ao() {
        return this.f362am;
    }

    public boolean ap() {
        return this.f353ad;
    }

    public void n(boolean z2) {
        this.f353ad = z2;
    }

    public boolean aq() {
        return this.f382aG;
    }

    public boolean ar() {
        return this.f383aH;
    }

    public void o(boolean z2) {
        this.f383aH = z2;
    }

    public int as() {
        return this.f352ac;
    }

    public void C(int i2) {
        if (i2 == 0 || i2 == 1 || i2 == 5 || i2 == 10 || i2 == 15 || i2 == 20 || i2 == 25 || i2 == 50 || i2 == 100 || i2 == 250 || i2 == 500 || i2 == 1000) {
            this.f352ac = i2;
        } else {
            bH.C.b("Invalid Default Runtime Read Per Second. Valid values: 0, 1, 5, 10, 15, 20, 25, 50, 100, 250, 500, 1000");
        }
    }

    public boolean at() {
        return this.f385aJ;
    }

    public void p(boolean z2) {
        this.f385aJ = z2;
    }

    public int au() {
        return this.f336N;
    }

    public void D(int i2) {
        this.f336N = i2;
    }

    public int av() {
        return this.f338O;
    }

    public void E(int i2) {
        boolean z2 = this.f338O != i2;
        this.f338O = i2;
        if (z2) {
            this.f334L = F(i2);
            this.f339P = 0;
        }
    }

    public int F(int i2) {
        int i3 = this.f338O > 0 ? 750 / this.f338O : 750;
        int i4 = i3 < 120 ? i3 : 120;
        if (!this.f383aH) {
            i4 = this.f342S < 19200 ? 20 : i4 > 6 ? i4 : 6;
        }
        return i4;
    }

    public String aw() {
        return this.f344U;
    }

    public void t(String str) {
        this.f344U = str;
    }

    public int G(int i2) {
        if (this.f358ai == null || i2 > this.f358ai.length || i2 < 0) {
            return Integer.MAX_VALUE;
        }
        return this.f358ai[i2];
    }

    public void H(int i2) {
        this.f359aj = i2;
    }

    public int ax() {
        return this.f359aj;
    }

    public void I(int i2) {
        if (this.f303g == 0) {
            bH.C.a("nPages not defined, blockingFactor can not be set before nPages");
        }
        this.f358ai = new int[this.f303g];
        for (int i3 = 0; i3 < this.f358ai.length; i3++) {
            this.f358ai[i3] = i2;
        }
    }

    /* renamed from: ay, reason: merged with bridge method [inline-methods] */
    public F clone() throws SecurityException {
        F f2 = new F();
        f2.d(f());
        f2.a(this.f303g);
        f2.b(h());
        f2.c(this.f305i);
        f2.a(this.f306j);
        f2.d(this.f311o);
        f2.a(this.f312p);
        f2.d(aF());
        f2.n(this.f331I);
        f2.o(this.f332J);
        f2.q(this.f334L);
        f2.D(this.f336N);
        f2.E(this.f338O);
        f2.p(this.f342S);
        f2.k(this.f343T);
        f2.t(this.f344U);
        f2.c(this.f358ai);
        f2.r(this.f347X);
        f2.w(this.f349Z);
        f2.c(this.f320x);
        f2.f(this.f321y);
        f2.d(this.f322z);
        f2.b(this.f323A);
        f2.a(I.a(az()));
        f2.b(I.a(aA()));
        f2.c(I.a(aB()));
        f2.d(I.a(aC()));
        f2.f(I.a(aE()));
        f2.f366aq = this.f366aq;
        return f2;
    }

    private void aN() throws SecurityException {
        Field[] declaredFields = getClass().getDeclaredFields();
        AccessibleObject.setAccessible(declaredFields, true);
        for (Field field : declaredFields) {
            try {
                if (H[].class.isInstance(field.get(this))) {
                    H[] hArr = (H[]) field.get(this);
                    for (int i2 = 0; i2 < hArr.length; i2++) {
                        if (hArr[i2] != null) {
                            try {
                                hArr[i2] = a(hArr[i2]);
                            } catch (V.g e2) {
                                Logger.getLogger(F.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
                            }
                        }
                    }
                } else if (H.class.isInstance(field.get(this))) {
                    try {
                        H h2 = (H) field.get(this);
                        if (h2 != null) {
                            a(h2);
                        }
                    } catch (Exception e3) {
                    }
                }
            } catch (IllegalAccessException e4) {
                Logger.getLogger(F.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e4);
            } catch (IllegalArgumentException e5) {
                Logger.getLogger(F.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e5);
            }
        }
    }

    protected void c(int[] iArr) {
        this.f358ai = iArr;
    }

    protected void b(H h2) {
        this.f323A = h2;
    }

    protected void c(H h2) {
        this.f320x = h2;
    }

    protected void d(H h2) {
        this.f322z = h2;
    }

    protected H[] az() {
        return this.f314r;
    }

    protected void a(H[] hArr) {
        this.f314r = hArr;
    }

    protected H[] aA() {
        return this.f315s;
    }

    protected void b(H[] hArr) {
        this.f315s = hArr;
    }

    protected H[] aB() {
        return this.f316t;
    }

    protected void c(H[] hArr) {
        this.f316t = hArr;
    }

    protected H[] aC() {
        return this.f317u;
    }

    protected H[] aD() {
        return this.f319w;
    }

    protected void d(H[] hArr) {
        this.f317u = hArr;
    }

    protected void e(H[] hArr) {
        this.f319w = hArr;
    }

    protected H[] aE() {
        return this.f318v;
    }

    protected void f(H[] hArr) {
        this.f318v = hArr;
    }

    protected String[] aF() {
        return this.f313q;
    }

    protected int[] aG() {
        return this.f358ai;
    }

    private void J(int i2) {
        this.f341R = i2;
    }

    private int aO() {
        return this.f341R;
    }
}
