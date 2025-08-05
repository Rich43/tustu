package bN;

import bH.C0995c;

/* loaded from: TunerStudioMS.jar:bN/n.class */
public class n {

    /* renamed from: a, reason: collision with root package name */
    public static String f7269a = "Error on Sync";

    /* renamed from: b, reason: collision with root package name */
    public static String f7270b = "Busy";

    /* renamed from: c, reason: collision with root package name */
    public static String f7271c = "DAQ running";

    /* renamed from: d, reason: collision with root package name */
    public static String f7272d = "PGM Running";

    /* renamed from: e, reason: collision with root package name */
    public static String f7273e = "Unknown Command";

    /* renamed from: f, reason: collision with root package name */
    public static String f7274f = "PGM Syntax";

    /* renamed from: g, reason: collision with root package name */
    public static String f7275g = "Out of Range";

    /* renamed from: h, reason: collision with root package name */
    public static String f7276h = "Write Protected Memory";

    /* renamed from: i, reason: collision with root package name */
    public static String f7277i = "Memory location not accessible";

    /* renamed from: j, reason: collision with root package name */
    public static String f7278j = "Access denied";

    /* renamed from: k, reason: collision with root package name */
    public static String f7279k = "Page not available";

    /* renamed from: l, reason: collision with root package name */
    public static String f7280l = "Mode not valid";

    /* renamed from: m, reason: collision with root package name */
    public static String f7281m = "Segment not valid";

    /* renamed from: n, reason: collision with root package name */
    public static String f7282n = "Sequence Error";

    /* renamed from: o, reason: collision with root package name */
    public static String f7283o = "DAQ configuration not valid";

    /* renamed from: p, reason: collision with root package name */
    public static String f7284p = "Memory overflow";

    /* renamed from: q, reason: collision with root package name */
    public static String f7285q = "Generic error";

    /* renamed from: r, reason: collision with root package name */
    public static String f7286r = "Failed verify";

    public static String a(byte b2) {
        String str;
        int iA = C0995c.a(b2);
        switch (iA) {
            case 0:
                str = f7269a;
                break;
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
            case 8:
            case 9:
            case 10:
            case 11:
            case 12:
            case 13:
            case 14:
            case 15:
            case 19:
            case 20:
            case 21:
            case 22:
            case 23:
            case 24:
            case 25:
            case 26:
            case 27:
            case 28:
            case 29:
            case 30:
            case 31:
            case 43:
            case 44:
            case 45:
            case 46:
            case 47:
            default:
                str = "Unknown Error Code: 0x" + Integer.toHexString(iA).toUpperCase();
                break;
            case 16:
                str = f7270b;
                break;
            case 17:
                str = f7271c;
                break;
            case 18:
                str = f7272d;
                break;
            case 32:
                str = f7273e;
                break;
            case 33:
                str = f7274f;
                break;
            case 34:
                str = f7275g;
                break;
            case 35:
                str = f7276h;
                break;
            case 36:
                str = f7277i;
                break;
            case 37:
                str = f7278j;
                break;
            case 38:
                str = f7279k;
                break;
            case 39:
                str = f7280l;
                break;
            case 40:
                str = f7281m;
                break;
            case 41:
                str = f7282n;
                break;
            case 42:
                str = f7283o;
                break;
            case 48:
                str = f7284p;
                break;
            case 49:
                str = f7285q;
                break;
            case 50:
                str = f7286r;
                break;
        }
        return str;
    }
}
