package ae;

import G.bS;
import bH.C;
import com.sun.org.apache.xml.internal.security.utils.Constants;
import org.icepdf.core.util.PdfOps;
import sun.security.pkcs11.wrapper.PKCS11Constants;

/* loaded from: TunerStudioMS.jar:ae/o.class */
public class o {

    /* renamed from: a, reason: collision with root package name */
    static boolean f4385a = false;

    /* renamed from: b, reason: collision with root package name */
    public static String f4386b = "MegaSquirt 1";

    /* renamed from: c, reason: collision with root package name */
    public static String f4387c = "MS2/Microsquirt/GPIO";

    /* renamed from: d, reason: collision with root package name */
    public static String f4388d = "MS2 Updated";

    /* renamed from: e, reason: collision with root package name */
    public static String f4389e = "Microsquirt(cased)";

    /* renamed from: f, reason: collision with root package name */
    public static String f4390f = "Megasquirt 2";

    /* renamed from: g, reason: collision with root package name */
    public static String f4391g = "Microsquirt-module";

    /* renamed from: h, reason: collision with root package name */
    public static String f4392h = "GPIO";

    /* renamed from: i, reason: collision with root package name */
    public static String f4393i = "MSPNP";

    /* renamed from: j, reason: collision with root package name */
    public static String f4394j = "MS3";

    /* renamed from: k, reason: collision with root package name */
    public static String f4395k = "Megasquirt-3";

    /* renamed from: l, reason: collision with root package name */
    public static String f4396l = "MS3-Pro";

    /* renamed from: m, reason: collision with root package name */
    public static String f4397m = "MS3-Pro 2nd Gen";

    /* renamed from: n, reason: collision with root package name */
    public static String f4398n = "MS3-Pro Ultimate";

    /* renamed from: o, reason: collision with root package name */
    public static String f4399o = "MS3-Pro Mini";

    /* renamed from: p, reason: collision with root package name */
    public static String f4400p = "MS3-Pro-Plus";

    /* renamed from: q, reason: collision with root package name */
    public static String f4401q = "MS3-Gold";

    /* renamed from: r, reason: collision with root package name */
    public static String f4402r = "BigStuff Gen4";

    /* renamed from: s, reason: collision with root package name */
    public static String f4403s = "XCP Controller";

    /* renamed from: t, reason: collision with root package name */
    public static String f4404t = "Unknown";

    /* renamed from: u, reason: collision with root package name */
    public static int f4405u = 0;

    /* renamed from: v, reason: collision with root package name */
    public static int f4406v = 1;

    /* renamed from: w, reason: collision with root package name */
    public static int f4407w = 2;

    /* renamed from: x, reason: collision with root package name */
    public static int f4408x = 4;

    /* renamed from: y, reason: collision with root package name */
    public static int f4409y = 8;

    /* renamed from: z, reason: collision with root package name */
    public static int f4410z = 16;

    /* renamed from: A, reason: collision with root package name */
    public static int f4411A = 32;

    /* renamed from: B, reason: collision with root package name */
    public static int f4412B = 64;

    /* renamed from: C, reason: collision with root package name */
    public static int f4413C = 128;

    /* renamed from: D, reason: collision with root package name */
    public static int f4414D = 256;

    /* renamed from: E, reason: collision with root package name */
    public static int f4415E = 512;

    /* renamed from: F, reason: collision with root package name */
    public static int f4416F = 1024;

    /* renamed from: G, reason: collision with root package name */
    public static int f4417G = 2048;

    /* renamed from: H, reason: collision with root package name */
    public static int f4418H = 4096;

    /* renamed from: I, reason: collision with root package name */
    public static int f4419I = 8192;

    /* renamed from: J, reason: collision with root package name */
    public static int f4420J = 16384;

    /* renamed from: K, reason: collision with root package name */
    public static int f4421K = 32768;

    /* renamed from: L, reason: collision with root package name */
    public static int f4422L = 65536;

    /* renamed from: M, reason: collision with root package name */
    public static int f4423M = 1073741824;

    /* renamed from: N, reason: collision with root package name */
    public static long f4424N = PKCS11Constants.CKF_EC_COMPRESS;

    /* renamed from: O, reason: collision with root package name */
    public static long f4425O = PKCS11Constants.CKF_EC_UNCOMPRESS;

    public static m a(int i2) {
        int i3 = 0;
        String str = "Unknown Monitor Version";
        if (i2 == 12546 || i2 == 513) {
            i3 = f4406v;
            str = f4387c;
        } else if (i2 == 12547) {
            i3 = f4406v | f4413C;
            str = f4388d;
        } else if (i2 >= 528 && i2 <= 543) {
            i3 = f4406v | f4413C | f4415E;
            str = f4389e;
        } else if (i2 >= 544 && i2 <= 559) {
            i3 = f4406v | f4412B | f4415E;
            str = f4390f;
        } else if (i2 >= 560 && i2 <= 575) {
            i3 = f4406v | f4416F | f4415E;
            str = f4391g;
        } else if (i2 >= 576 && i2 <= 591) {
            i3 = f4406v | f4415E;
            str = f4392h;
        } else if (i2 >= 592 && i2 <= 607) {
            i3 = f4406v | f4414D | f4415E;
            str = f4393i;
        } else if (i2 == 0) {
            i3 = f4407w;
            str = f4394j;
        } else if (i2 == 928) {
            i3 = f4407w | f4419I;
            str = f4397m;
        } else if (i2 == 944) {
            i3 = f4407w | f4420J;
            str = f4398n;
        } else if (i2 >= 960 && i2 <= 975) {
            i3 = f4407w | f4422L;
            str = f4400p;
        } else if (i2 >= 976 && i2 <= 991) {
            i3 = f4407w | f4421K;
            str = f4399o;
        } else if (i2 >= 768 && i2 <= 783) {
            i3 = f4407w | f4410z;
            str = f4395k;
        } else if (i2 >= 896 && i2 <= 911) {
            i3 = f4407w | f4411A;
            str = f4396l;
        } else if (i2 >= 912 && i2 <= 927) {
            i3 = f4407w | f4410z | f4418H;
            str = f4401q;
        }
        if (f4385a) {
            C.d(str + " hardware found.\n");
        }
        m mVar = new m();
        mVar.b(i2);
        mVar.a(str);
        mVar.a(i3);
        return mVar;
    }

    public static boolean a(bS bSVar) {
        byte[] bArrA;
        return (bSVar != null && (bArrA = bSVar.a()) != null && (bArrA[0] & 224) == 224 && (bArrA[1] & 240) == 0 && bArrA[2] == 62) ? false : true;
    }

    public static m b(bS bSVar) {
        if (bSVar == null || bSVar.b() == null) {
            return null;
        }
        String strB = bSVar.b();
        int i2 = 0;
        int i3 = 0;
        String str = "Unknown Monitor Version";
        if (strB.startsWith(f4402r)) {
            str = f4402r;
        } else if (strB.startsWith("MS3")) {
            if (strB.endsWith(Constants._TAG_P)) {
                i2 = f4407w | f4411A;
                str = f4396l;
            } else if (strB.endsWith("U")) {
                i2 = f4407w | f4420J;
                str = f4398n;
            } else if (strB.endsWith("E")) {
                i2 = f4407w | f4419I;
                str = f4397m;
            } else if (strB.endsWith("I")) {
                i2 = f4407w | f4421K;
                str = f4399o;
            } else if (strB.endsWith(PdfOps.M_TOKEN)) {
                i2 = f4407w | f4422L;
                str = f4400p;
            } else {
                i2 = f4407w | f4410z;
                str = f4395k;
            }
            i3 = 896;
        } else if (strB.startsWith("MS2")) {
            if (strB.endsWith("U")) {
                i2 = f4406v | f4413C | f4415E;
                str = f4389e;
                i3 = 544;
            } else if (strB.endsWith(PdfOps.M_TOKEN)) {
                i2 = f4406v | f4416F | f4415E;
                str = f4391g;
                i3 = 576;
            } else if (strB.endsWith(Constants._TAG_P)) {
                i2 = f4406v | f4414D | f4415E;
                str = f4393i;
                i3 = 592;
            } else if (strB.endsWith("2")) {
                i2 = f4406v | f4412B | f4415E;
                str = f4390f;
                i3 = 544;
            } else {
                i2 = f4406v;
                str = f4387c;
                i3 = 12546;
            }
        } else if (strB.startsWith("MS1") || strB.startsWith("MS/Extra") || strB.startsWith("MSnS")) {
            i2 = 0;
            str = f4386b;
            i3 = 0;
        } else if (!strB.startsWith("MS4")) {
            i2 = f4406v;
            str = f4387c;
            i3 = 0;
        }
        if (f4385a) {
            C.d(str + " hardware found.\n");
        }
        m mVar = new m();
        mVar.b(i3);
        mVar.a(str);
        mVar.a(i2);
        return mVar;
    }
}
