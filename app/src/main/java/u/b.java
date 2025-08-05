package U;

import G.C0134q;
import G.R;
import S.e;
import S.j;
import bH.H;
import bH.W;
import java.util.logging.Level;
import java.util.logging.Logger;

/* loaded from: TunerStudioMS.jar:U/b.class */
public class b {

    /* renamed from: a, reason: collision with root package name */
    public static String f1890a = a.f1885a;

    /* renamed from: b, reason: collision with root package name */
    public static String f1891b = a.f1886b;

    /* renamed from: c, reason: collision with root package name */
    public static String f1892c = " && AppEvent.dataLoggingActive == 0 && isTrueFor(!logPlaybackActive(), 2)";

    /* renamed from: d, reason: collision with root package name */
    public static String f1893d = " && AppEvent.dataLoggingActive != 0 && AppEvent.dataLogTime > 1";

    /* renamed from: e, reason: collision with root package name */
    public static String f1894e = "AppEvent.dataLoggingActive == 0";

    /* renamed from: f, reason: collision with root package name */
    public static String f1895f = "AppEvent.dataLoggingActive != 0";

    public static String a(j jVar, int i2) {
        String str;
        if (i2 > 0) {
            str = "AppEvent.dataLogTime > " + i2;
            f1892c = " && AppEvent.dataLoggingActive == 0 && AppEvent.dataLogTime == 0";
        } else {
            str = "";
        }
        String strB = W.b(jVar.d(), f1893d, "");
        if (strB.trim().isEmpty()) {
            strB = str;
        }
        return strB;
    }

    public static double a(R r2) {
        String[] strArrSplit = a(S.b.a().a(r2.c(), f1891b), -1).split(" ");
        int i2 = (strArrSplit.length == 3 && (strArrSplit[1].equals("<") || strArrSplit[1].equals("=") || strArrSplit[1].equals(">")) && H.a(strArrSplit[2]) && strArrSplit[0].trim().equals("AppEvent.dataLogTime")) ? (int) Double.parseDouble(strArrSplit[2]) : -1;
        return i2;
    }

    public static void a(R r2, int i2) {
        j jVarA = S.b.a().a(r2.c(), f1891b);
        jVarA.e(("AppEvent.dataLogTime > " + i2) + f1893d);
        jVarA.f(f1895f);
        try {
            e.a().a(r2.c(), jVarA);
        } catch (C0134q e2) {
            Logger.getLogger(b.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
        }
    }
}
