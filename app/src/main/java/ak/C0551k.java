package ak;

import W.C0170a;
import W.C0177c;
import com.efiAnalytics.plugin.ecu.ControllerParameter;
import javafx.fxml.FXMLLoader;
import org.icepdf.core.util.PdfOps;

/* renamed from: ak.k, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:ak/k.class */
public class C0551k {

    /* renamed from: a, reason: collision with root package name */
    public static String f4854a = "Record Number";

    /* renamed from: b, reason: collision with root package name */
    public static String f4855b = "Time";

    /* renamed from: c, reason: collision with root package name */
    public static String f4856c = "Engine Speed";

    /* renamed from: d, reason: collision with root package name */
    public static String f4857d = "Vehicle Speed";

    /* renamed from: e, reason: collision with root package name */
    public static String f4858e = "MAP";

    /* renamed from: f, reason: collision with root package name */
    public static String f4859f = "Engine Temp";

    /* renamed from: g, reason: collision with root package name */
    public static String f4860g = "Intake Air Temp";

    /* renamed from: h, reason: collision with root package name */
    public static String f4861h = "TPS";

    /* renamed from: i, reason: collision with root package name */
    public static String f4862i = "IAC Position";

    /* renamed from: j, reason: collision with root package name */
    public static String f4863j = "Battery Volts";

    /* renamed from: k, reason: collision with root package name */
    public static String f4864k = "AFR Desired";

    /* renamed from: l, reason: collision with root package name */
    public static String f4865l = "Spark Adv Front";

    /* renamed from: m, reason: collision with root package name */
    public static String f4866m = "Spark Adv Rear";

    /* renamed from: n, reason: collision with root package name */
    public static String f4867n = "VE Front";

    /* renamed from: o, reason: collision with root package name */
    public static String f4868o = "VE Rear";

    /* renamed from: p, reason: collision with root package name */
    public static String f4869p = "VE New Front";

    /* renamed from: q, reason: collision with root package name */
    public static String f4870q = "VE New Rear";

    /* renamed from: r, reason: collision with root package name */
    public static String f4871r = "Front AFR";

    /* renamed from: s, reason: collision with root package name */
    public static String f4872s = "Rear AFR";

    /* renamed from: t, reason: collision with root package name */
    public static String f4873t = "Accel Enrich";

    /* renamed from: u, reason: collision with root package name */
    public static String f4874u = "Decel Enrich";

    /* renamed from: v, reason: collision with root package name */
    public static String f4875v = "Throttle Volt";

    /* renamed from: w, reason: collision with root package name */
    public static String f4876w = "Injector BPW Front";

    /* renamed from: x, reason: collision with root package name */
    public static String f4877x = "Injector BPW Rear";

    /* renamed from: y, reason: collision with root package name */
    public static String f4878y = "IAT Volt";

    /* renamed from: z, reason: collision with root package name */
    public static String f4879z = "Desired Idle";

    /* renamed from: A, reason: collision with root package name */
    public static String f4880A = "Warm Up AFR";

    /* renamed from: B, reason: collision with root package name */
    public static String f4881B = "MAP Read 2";

    /* renamed from: C, reason: collision with root package name */
    public static String f4882C = "MAP Load";

    /* renamed from: D, reason: collision with root package name */
    public static String f4883D = "MAP Volt";

    /* renamed from: E, reason: collision with root package name */
    public static String f4884E = "MAP Default";

    /* renamed from: F, reason: collision with root package name */
    public static String f4885F = "Factory 1";

    /* renamed from: G, reason: collision with root package name */
    public static String f4886G = "Factory 2";

    /* renamed from: H, reason: collision with root package name */
    public static String f4887H = "Factory 3";

    /* renamed from: I, reason: collision with root package name */
    public static String f4888I = "ECT Volts";

    /* renamed from: J, reason: collision with root package name */
    public static String f4889J = "Barometer";

    /* renamed from: K, reason: collision with root package name */
    public static String f4890K = "Knock Retard Front";

    /* renamed from: L, reason: collision with root package name */
    public static String f4891L = "Knock Retard Rear";

    /* renamed from: M, reason: collision with root package name */
    public static String f4892M = "O2 Sensor Front";

    /* renamed from: N, reason: collision with root package name */
    public static String f4893N = "O2 Sensor Rear";

    /* renamed from: O, reason: collision with root package name */
    public static String f4894O = "O2 integrator Front";

    /* renamed from: P, reason: collision with root package name */
    public static String f4895P = "O2 integrator Rear";

    /* renamed from: Q, reason: collision with root package name */
    public static String f4896Q = "TGS Volts 1";

    /* renamed from: R, reason: collision with root package name */
    public static String f4897R = "TGS Volts 2";

    /* renamed from: S, reason: collision with root package name */
    public static String f4898S = "TGS";

    /* renamed from: T, reason: collision with root package name */
    public static String f4899T = "CC Target Speed";

    public static C0177c a(C0177c c0177c, int i2) {
        c0177c.a(c(f4854a));
        if (i2 >= 25) {
            c0177c.a(c(f4855b));
        }
        if (i2 >= 32) {
            c0177c.a(c(f4856c));
        }
        if (i2 >= 72) {
            c0177c.a(c(f4857d));
        }
        if (i2 >= 47) {
            c0177c.a(c(f4858e));
        }
        if (i2 >= 37) {
            c0177c.a(c(f4859f));
        }
        if (i2 >= 38) {
            c0177c.a(c(f4860g));
        }
        if (i2 >= 36) {
            c0177c.a(c(f4861h));
        }
        if (i2 >= 66) {
            c0177c.a(c(f4862i));
        }
        if (i2 >= 34) {
            c0177c.a(c(f4863j));
        }
        if (i2 >= 43) {
            c0177c.a(c(f4864k));
        }
        if (i2 >= 58) {
            c0177c.a(c(f4865l));
        }
        if (i2 >= 60) {
            c0177c.a(c(f4866m));
        }
        if (i2 >= 61) {
            c0177c.a(c(f4867n));
        }
        if (i2 >= 62) {
            c0177c.a(c(f4868o));
        }
        if (i2 >= 63) {
            c0177c.a(c(f4869p));
        }
        if (i2 >= 64) {
            c0177c.a(c(f4870q));
        }
        if (i2 >= 44) {
            c0177c.a(c(f4871r));
        }
        if (i2 >= 45) {
            c0177c.a(c(f4872s));
        }
        if (i2 >= 50) {
            c0177c.a(c(f4873t));
        }
        if (i2 >= 49) {
            c0177c.a(c(f4874u));
        }
        if (i2 >= 42) {
            c0177c.a(c(f4875v));
        }
        if (i2 >= 52) {
            c0177c.a(c(f4876w));
        }
        if (i2 >= 54) {
            c0177c.a(c(f4877x));
        }
        if (i2 >= 40) {
            c0177c.a(c(f4878y));
        }
        if (i2 >= 33) {
            c0177c.a(c(f4879z));
        }
        if (i2 >= 65) {
            c0177c.a(c(f4880A));
        }
        if (i2 >= 70) {
            c0177c.a(c(f4889J));
        }
        if (i2 >= 71) {
            c0177c.a(c(f4886G));
        }
        if (i2 >= 48) {
            c0177c.a(c(f4881B));
        }
        if (i2 >= 35) {
            c0177c.a(c(f4882C));
        }
        if (i2 >= 41) {
            c0177c.a(c(f4883D));
        }
        if (i2 >= 77) {
            c0177c.a(c(f4890K));
        }
        if (i2 >= 78) {
            c0177c.a(c(f4891L));
        }
        if (i2 >= 79) {
            c0177c.a(c(f4892M));
        }
        if (i2 >= 80) {
            c0177c.a(c(f4893N));
        }
        if (i2 >= 81) {
            c0177c.a(c(f4894O));
        }
        if (i2 >= 82) {
            c0177c.a(c(f4895P));
        }
        if (i2 >= 93) {
            c0177c.a(c(f4896Q));
        }
        if (i2 >= 94) {
            c0177c.a(c(f4897R));
        }
        if (i2 >= 90) {
            c0177c.a(c(f4898S));
        }
        if (i2 >= 1000) {
            c0177c.a(c(f4899T));
        }
        return c0177c;
    }

    public static C0170a a(String str) {
        if (str.equals(f4857d)) {
            C0170a c0170a = new C0170a();
            c0170a.a(f4857d);
            c0170a.c(1);
            c0170a.b(36);
            c0170a.a(1);
            c0170a.d(1);
            c0170a.b(0.623d);
            c0170a.a(0.0d);
            return c0170a;
        }
        if (str.equals(f4865l)) {
            C0170a c0170a2 = new C0170a();
            c0170a2.a(f4865l);
            c0170a2.c(1);
            c0170a2.b(46);
            c0170a2.a(1);
            c0170a2.d(2);
            c0170a2.b(0.25d);
            c0170a2.a(0.0d);
            c0170a2.b(bH.S.a() + "");
            return c0170a2;
        }
        if (str.equals(f4866m)) {
            C0170a c0170a3 = new C0170a();
            c0170a3.a(f4866m);
            c0170a3.c(1);
            c0170a3.b(48);
            c0170a3.a(1);
            c0170a3.d(2);
            c0170a3.b(0.25d);
            c0170a3.a(0.0d);
            c0170a3.b(bH.S.a() + "");
            return c0170a3;
        }
        if (!str.equals(f4889J)) {
            return c(str);
        }
        C0170a c0170a4 = new C0170a();
        c0170a4.a(f4889J);
        c0170a4.c(1);
        c0170a4.b(34);
        c0170a4.a(0);
        c0170a4.d(1);
        c0170a4.b(0.3691d);
        c0170a4.a(10.57d);
        c0170a4.b("kPa");
        return c0170a4;
    }

    public static C0170a b(String str) {
        if (str.equals(f4865l)) {
            C0170a c0170aC = c(str);
            c0170aC.b(0.5d);
            c0170aC.b(37);
            return c0170aC;
        }
        if (str.equals(f4866m)) {
            C0170a c0170aC2 = c(str);
            c0170aC2.b(0.5d);
            c0170aC2.b(38);
            return c0170aC2;
        }
        if (str.equals(f4890K)) {
            C0170a c0170aC3 = c(str);
            c0170aC3.b(39);
            return c0170aC3;
        }
        if (str.equals(f4891L)) {
            C0170a c0170aC4 = c(str);
            c0170aC4.b(40);
            return c0170aC4;
        }
        if (str.equals(f4859f)) {
            C0170a c0170aC5 = c(str);
            c0170aC5.b(45);
            return c0170aC5;
        }
        if (str.equals(f4861h)) {
            C0170a c0170aC6 = c(str);
            c0170aC6.b(36);
            return c0170aC6;
        }
        if (str.equals(f4860g)) {
            C0170a c0170aC7 = c(str);
            c0170aC7.b(43);
            return c0170aC7;
        }
        if (str.equals(f4879z)) {
            C0170a c0170aC8 = c(str);
            c0170aC8.b(33);
            return c0170aC8;
        }
        if (str.equals(f4863j)) {
            C0170a c0170aC9 = c(str);
            c0170aC9.b(34);
            return c0170aC9;
        }
        if (str.equals(f4858e)) {
            C0170a c0170aC10 = c(str);
            c0170aC10.b(35);
            return c0170aC10;
        }
        if (!str.equals(f4875v)) {
            return c(str);
        }
        C0170a c0170aC11 = c(str);
        c0170aC11.b(48);
        return c0170aC11;
    }

    public static C0170a c(String str) {
        if (str.equals(f4854a)) {
            C0170a c0170a = new C0170a();
            c0170a.a(f4854a);
            c0170a.c(4);
            c0170a.b(0);
            c0170a.a(1);
            c0170a.d(0);
            c0170a.b(1.0d);
            c0170a.a(0.0d);
            return c0170a;
        }
        if (str.equals(f4855b)) {
            C0170a c0170a2 = new C0170a();
            c0170a2.a(f4855b);
            c0170a2.c(4);
            c0170a2.b(22);
            c0170a2.a(1);
            c0170a2.d(3);
            c0170a2.b(0.001d);
            c0170a2.a(0.0d);
            c0170a2.b(PdfOps.s_TOKEN);
            return c0170a2;
        }
        if (str.equals(f4856c)) {
            C0170a c0170a3 = new C0170a();
            c0170a3.a(f4856c);
            c0170a3.c(2);
            c0170a3.b(31);
            c0170a3.a(0);
            c0170a3.d(0);
            c0170a3.b(1.0d);
            c0170a3.a(0.0d);
            return c0170a3;
        }
        if (str.equals(f4857d)) {
            C0170a c0170a4 = new C0170a();
            c0170a4.a(f4857d);
            c0170a4.c(1);
            c0170a4.b(72);
            c0170a4.a(1);
            c0170a4.d(1);
            c0170a4.b(0.623d);
            c0170a4.a(0.0d);
            return c0170a4;
        }
        if (str.equals(f4858e)) {
            C0170a c0170a5 = new C0170a();
            c0170a5.a(f4858e);
            c0170a5.c(1);
            c0170a5.b(35);
            c0170a5.a(0);
            c0170a5.d(1);
            c0170a5.b(0.3691d);
            c0170a5.a(10.41d);
            c0170a5.b("kPa");
            return c0170a5;
        }
        if (str.equals(f4859f)) {
            C0170a c0170a6 = new C0170a();
            c0170a6.a(f4859f);
            c0170a6.c(1);
            c0170a6.b(37);
            c0170a6.a(0);
            c0170a6.d(1);
            c0170a6.b(1.83d);
            c0170a6.a(0.0d);
            c0170a6.b(bH.S.a() + PdfOps.F_TOKEN);
            return c0170a6;
        }
        if (str.equals(f4860g)) {
            C0170a c0170a7 = new C0170a();
            c0170a7.a(f4860g);
            c0170a7.c(1);
            c0170a7.b(38);
            c0170a7.a(0);
            c0170a7.d(1);
            c0170a7.b(1.785d);
            c0170a7.a(4.0d);
            c0170a7.b(bH.S.a() + PdfOps.F_TOKEN);
            return c0170a7;
        }
        if (str.equals(f4861h)) {
            C0170a c0170a8 = new C0170a();
            c0170a8.a(f4861h);
            c0170a8.c(1);
            c0170a8.b(36);
            c0170a8.a(0);
            c0170a8.d(1);
            c0170a8.b(0.45454545d);
            c0170a8.a(0.0d);
            c0170a8.b(FXMLLoader.RESOURCE_KEY_PREFIX);
            return c0170a8;
        }
        if (str.equals(f4862i)) {
            C0170a c0170a9 = new C0170a();
            c0170a9.a(f4862i);
            c0170a9.c(1);
            c0170a9.b(66);
            c0170a9.a(0);
            c0170a9.d(0);
            c0170a9.b(1.0d);
            c0170a9.a(0.0d);
            c0170a9.b(FXMLLoader.CONTROLLER_METHOD_PREFIX);
            return c0170a9;
        }
        if (str.equals(f4863j)) {
            C0170a c0170a10 = new C0170a();
            c0170a10.a(f4863j);
            c0170a10.c(1);
            c0170a10.b(34);
            c0170a10.a(0);
            c0170a10.d(1);
            c0170a10.b(0.1d);
            c0170a10.a(0.0d);
            c0170a10.b(PdfOps.v_TOKEN);
            return c0170a10;
        }
        if (str.equals(f4864k)) {
            C0170a c0170a11 = new C0170a();
            c0170a11.a(f4864k);
            c0170a11.c(1);
            c0170a11.b(43);
            c0170a11.a(0);
            c0170a11.d(1);
            c0170a11.b(0.1d);
            c0170a11.a(0.0d);
            c0170a11.b(":1");
            return c0170a11;
        }
        if (str.equals(f4865l)) {
            C0170a c0170a12 = new C0170a();
            c0170a12.a(f4865l);
            c0170a12.c(1);
            c0170a12.b(58);
            c0170a12.a(1);
            c0170a12.d(2);
            c0170a12.b(0.25d);
            c0170a12.a(0.0d);
            c0170a12.b(bH.S.a() + "");
            return c0170a12;
        }
        if (str.equals(f4866m)) {
            C0170a c0170a13 = new C0170a();
            c0170a13.a(f4866m);
            c0170a13.c(1);
            c0170a13.b(60);
            c0170a13.a(1);
            c0170a13.d(2);
            c0170a13.b(0.25d);
            c0170a13.a(0.0d);
            c0170a13.b(bH.S.a() + "");
            return c0170a13;
        }
        if (str.equals(f4867n)) {
            C0170a c0170a14 = new C0170a();
            c0170a14.a(f4867n);
            c0170a14.c(1);
            c0170a14.b(61);
            c0170a14.a(0);
            c0170a14.d(1);
            c0170a14.b(0.5d);
            c0170a14.a(0.0d);
            c0170a14.b(FXMLLoader.RESOURCE_KEY_PREFIX);
            return c0170a14;
        }
        if (str.equals(f4868o)) {
            C0170a c0170a15 = new C0170a();
            c0170a15.a(f4868o);
            c0170a15.c(1);
            c0170a15.b(62);
            c0170a15.a(0);
            c0170a15.d(1);
            c0170a15.b(0.5d);
            c0170a15.a(0.0d);
            c0170a15.b(FXMLLoader.RESOURCE_KEY_PREFIX);
            return c0170a15;
        }
        if (str.equals(f4869p)) {
            C0170a c0170a16 = new C0170a();
            c0170a16.a(f4869p);
            c0170a16.c(1);
            c0170a16.b(63);
            c0170a16.a(0);
            c0170a16.d(1);
            c0170a16.b(0.5d);
            c0170a16.a(0.0d);
            c0170a16.b("");
            return c0170a16;
        }
        if (str.equals(f4870q)) {
            C0170a c0170a17 = new C0170a();
            c0170a17.a(f4870q);
            c0170a17.c(1);
            c0170a17.b(64);
            c0170a17.a(0);
            c0170a17.d(1);
            c0170a17.b(0.5d);
            c0170a17.a(0.0d);
            c0170a17.b("");
            return c0170a17;
        }
        if (str.equals(f4871r)) {
            C0170a c0170a18 = new C0170a();
            c0170a18.a(f4871r);
            c0170a18.c(1);
            c0170a18.b(44);
            c0170a18.a(0);
            c0170a18.d(1);
            c0170a18.b(0.1d);
            c0170a18.a(0.0d);
            c0170a18.b(":1");
            return c0170a18;
        }
        if (str.equals(f4872s)) {
            C0170a c0170a19 = new C0170a();
            c0170a19.a(f4872s);
            c0170a19.c(1);
            c0170a19.b(45);
            c0170a19.a(0);
            c0170a19.d(1);
            c0170a19.b(0.1d);
            c0170a19.a(0.0d);
            c0170a19.b(":1");
            return c0170a19;
        }
        if (str.equals(f4888I)) {
            C0170a c0170a20 = new C0170a();
            c0170a20.a(f4888I);
            c0170a20.c(1);
            c0170a20.b(39);
            c0170a20.a(0);
            c0170a20.d(3);
            c0170a20.b(0.0196078431d);
            c0170a20.a(0.0d);
            c0170a20.b(PdfOps.v_TOKEN);
            return c0170a20;
        }
        if (str.equals(f4873t)) {
            C0170a c0170a21 = new C0170a();
            c0170a21.a(f4873t);
            c0170a21.c(1);
            c0170a21.b(50);
            c0170a21.a(0);
            c0170a21.d(3);
            c0170a21.b(0.00401d);
            c0170a21.a(0.0d);
            c0170a21.b("ms");
            return c0170a21;
        }
        if (str.equals(f4874u)) {
            C0170a c0170a22 = new C0170a();
            c0170a22.a(f4874u);
            c0170a22.c(1);
            c0170a22.b(56);
            c0170a22.a(0);
            c0170a22.d(3);
            c0170a22.b(0.00401d);
            c0170a22.a(0.0d);
            c0170a22.b("ms");
            return c0170a22;
        }
        if (str.equals(f4875v)) {
            C0170a c0170a23 = new C0170a();
            c0170a23.a(f4875v);
            c0170a23.c(1);
            c0170a23.b(42);
            c0170a23.a(0);
            c0170a23.d(3);
            c0170a23.b(0.0196078431d);
            c0170a23.a(0.0d);
            c0170a23.b(PdfOps.v_TOKEN);
            return c0170a23;
        }
        if (str.equals(f4876w)) {
            C0170a c0170a24 = new C0170a();
            c0170a24.a(f4876w);
            c0170a24.c(2);
            c0170a24.b(51);
            c0170a24.a(0);
            c0170a24.d(3);
            c0170a24.b(0.004d);
            c0170a24.a(0.0d);
            c0170a24.b("ms");
            return c0170a24;
        }
        if (str.equals(f4877x)) {
            C0170a c0170a25 = new C0170a();
            c0170a25.a(f4877x);
            c0170a25.c(2);
            c0170a25.b(53);
            c0170a25.a(0);
            c0170a25.d(3);
            c0170a25.b(0.004d);
            c0170a25.a(0.0d);
            c0170a25.b("ms");
            return c0170a25;
        }
        if (str.equals(f4878y)) {
            C0170a c0170a26 = new C0170a();
            c0170a26.a(f4878y);
            c0170a26.c(1);
            c0170a26.b(40);
            c0170a26.a(0);
            c0170a26.d(3);
            c0170a26.b(0.0196078431d);
            c0170a26.a(0.0d);
            c0170a26.b(PdfOps.v_TOKEN);
            return c0170a26;
        }
        if (str.equals(f4879z)) {
            C0170a c0170a27 = new C0170a();
            c0170a27.a(f4879z);
            c0170a27.c(1);
            c0170a27.b(33);
            c0170a27.a(0);
            c0170a27.d(0);
            c0170a27.b(8.0d);
            c0170a27.a(0.0d);
            c0170a27.b("RPM");
            return c0170a27;
        }
        if (str.equals(f4880A)) {
            C0170a c0170a28 = new C0170a();
            c0170a28.a(f4880A);
            c0170a28.c(1);
            c0170a28.b(65);
            c0170a28.a(0);
            c0170a28.d(1);
            c0170a28.b(0.1d);
            c0170a28.a(0.0d);
            c0170a28.b(":1");
            return c0170a28;
        }
        if (str.equals(f4885F)) {
            C0170a c0170a29 = new C0170a();
            c0170a29.a(f4885F);
            c0170a29.c(1);
            c0170a29.b(70);
            c0170a29.a(0);
            c0170a29.d(0);
            c0170a29.b(1.0d);
            c0170a29.a(0.0d);
            c0170a29.e(2);
            c0170a29.b(ControllerParameter.PARAM_CLASS_BITS);
            return c0170a29;
        }
        if (str.equals(f4886G)) {
            C0170a c0170a30 = new C0170a();
            c0170a30.a(f4886G);
            c0170a30.c(1);
            c0170a30.b(71);
            c0170a30.a(0);
            c0170a30.d(0);
            c0170a30.b(1.0d);
            c0170a30.a(0.0d);
            c0170a30.b(ControllerParameter.PARAM_CLASS_BITS);
            c0170a30.e(2);
            return c0170a30;
        }
        if (str.equals(f4887H)) {
            C0170a c0170a31 = new C0170a();
            c0170a31.a(f4887H);
            c0170a31.c(1);
            c0170a31.b(72);
            c0170a31.a(0);
            c0170a31.d(0);
            c0170a31.b(1.0d);
            c0170a31.a(0.0d);
            c0170a31.b(ControllerParameter.PARAM_CLASS_BITS);
            c0170a31.e(2);
            return c0170a31;
        }
        if (str.equals(f4884E)) {
            C0170a c0170a32 = new C0170a();
            c0170a32.a(f4884E);
            c0170a32.c(1);
            c0170a32.b(47);
            c0170a32.a(0);
            c0170a32.d(1);
            c0170a32.b(0.3691d);
            c0170a32.a(10.41d);
            c0170a32.b("kPa");
            return c0170a32;
        }
        if (str.equals(f4889J)) {
            C0170a c0170a33 = new C0170a();
            c0170a33.a(f4889J);
            c0170a33.c(1);
            c0170a33.b(70);
            c0170a33.a(0);
            c0170a33.d(1);
            c0170a33.b(0.3691d);
            c0170a33.a(10.57d);
            c0170a33.b("kPa");
            return c0170a33;
        }
        if (str.equals(f4881B)) {
            C0170a c0170a34 = new C0170a();
            c0170a34.a(f4881B);
            c0170a34.c(1);
            c0170a34.b(48);
            c0170a34.a(0);
            c0170a34.d(1);
            c0170a34.b(0.3691d);
            c0170a34.a(10.41d);
            c0170a34.b("kPa");
            return c0170a34;
        }
        if (str.equals(f4882C)) {
            C0170a c0170a35 = new C0170a();
            c0170a35.a(f4882C);
            c0170a35.c(1);
            c0170a35.b(35);
            c0170a35.a(0);
            c0170a35.d(1);
            c0170a35.b(0.3691d);
            c0170a35.a(10.41d);
            c0170a35.b("kPa");
            return c0170a35;
        }
        if (str.equals(f4883D)) {
            C0170a c0170a36 = new C0170a();
            c0170a36.a(f4883D);
            c0170a36.c(1);
            c0170a36.b(41);
            c0170a36.a(0);
            c0170a36.d(3);
            c0170a36.b(0.0196078431d);
            c0170a36.a(0.0d);
            c0170a36.b(PdfOps.v_TOKEN);
            return c0170a36;
        }
        if (str.equals(f4890K)) {
            C0170a c0170a37 = new C0170a();
            c0170a37.a(f4890K);
            c0170a37.c(1);
            c0170a37.b(75);
            c0170a37.a(1);
            c0170a37.d(1);
            c0170a37.b(0.5d);
            c0170a37.a(0.0d);
            c0170a37.b(bH.S.a() + "");
            return c0170a37;
        }
        if (str.equals(f4891L)) {
            C0170a c0170a38 = new C0170a();
            c0170a38.a(f4891L);
            c0170a38.c(1);
            c0170a38.b(76);
            c0170a38.a(1);
            c0170a38.d(1);
            c0170a38.b(0.5d);
            c0170a38.a(0.0d);
            c0170a38.b(bH.S.a() + "");
            return c0170a38;
        }
        if (str.equals(f4892M)) {
            C0170a c0170a39 = new C0170a();
            c0170a39.a(f4892M);
            c0170a39.c(1);
            c0170a39.b(79);
            c0170a39.a(0);
            c0170a39.d(3);
            c0170a39.b(0.02d);
            c0170a39.a(0.0d);
            c0170a39.b(PdfOps.v_TOKEN);
            return c0170a39;
        }
        if (str.equals(f4893N)) {
            C0170a c0170a40 = new C0170a();
            c0170a40.a(f4893N);
            c0170a40.c(1);
            c0170a40.b(80);
            c0170a40.a(0);
            c0170a40.d(3);
            c0170a40.b(0.02d);
            c0170a40.a(0.0d);
            c0170a40.b(PdfOps.v_TOKEN);
            return c0170a40;
        }
        if (str.equals(f4894O)) {
            C0170a c0170a41 = new C0170a();
            c0170a41.a(f4894O);
            c0170a41.c(1);
            c0170a41.b(81);
            c0170a41.a(0);
            c0170a41.d(1);
            c0170a41.b(0.78125d);
            c0170a41.a(0.0d);
            c0170a41.b(FXMLLoader.RESOURCE_KEY_PREFIX);
            return c0170a41;
        }
        if (str.equals(f4895P)) {
            C0170a c0170a42 = new C0170a();
            c0170a42.a(f4895P);
            c0170a42.c(1);
            c0170a42.b(82);
            c0170a42.a(0);
            c0170a42.d(1);
            c0170a42.b(0.78125d);
            c0170a42.a(0.0d);
            c0170a42.b(FXMLLoader.RESOURCE_KEY_PREFIX);
            return c0170a42;
        }
        if (str.equals(f4896Q)) {
            C0170a c0170a43 = new C0170a();
            c0170a43.a(f4896Q);
            c0170a43.c(1);
            c0170a43.b(93);
            c0170a43.a(0);
            c0170a43.d(3);
            c0170a43.b(0.0196078431d);
            c0170a43.a(0.0d);
            c0170a43.b(PdfOps.v_TOKEN);
            return c0170a43;
        }
        if (str.equals(f4897R)) {
            C0170a c0170a44 = new C0170a();
            c0170a44.a(f4897R);
            c0170a44.c(1);
            c0170a44.b(94);
            c0170a44.a(0);
            c0170a44.d(3);
            c0170a44.b(0.0196078431d);
            c0170a44.a(0.0d);
            c0170a44.b(PdfOps.v_TOKEN);
            return c0170a44;
        }
        if (!str.equals(f4898S)) {
            return null;
        }
        C0170a c0170a45 = new C0170a();
        c0170a45.a(f4898S);
        c0170a45.c(1);
        c0170a45.b(90);
        c0170a45.a(0);
        c0170a45.d(1);
        c0170a45.b(0.78125d);
        c0170a45.a(0.0d);
        c0170a45.b(FXMLLoader.RESOURCE_KEY_PREFIX);
        return c0170a45;
    }
}
