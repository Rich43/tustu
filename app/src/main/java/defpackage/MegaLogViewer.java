package defpackage;

import ao.bP;
import com.efiAnalytics.ui.aN;
import g.C1733k;
import java.awt.Component;

/* loaded from: TunerStudioMS.jar:MegaLogViewer.class */
public class MegaLogViewer {

    /* renamed from: a, reason: collision with root package name */
    static bP f1738a = null;

    /* JADX WARN: Removed duplicated region for block: B:84:0x03b8 A[Catch: Exception -> 0x0416, TryCatch #4 {Exception -> 0x0416, blocks: (B:82:0x03af, B:85:0x03bf, B:88:0x03d7, B:84:0x03b8), top: B:146:0x03af }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static void main(java.lang.String[] r6) {
        /*
            Method dump skipped, instructions count: 1575
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: defpackage.MegaLogViewer.main(java.lang.String[]):void");
    }

    public static void a() {
        try {
            double d2 = Double.parseDouble(System.getProperty("java.specification.version"));
            if (d2 < 1.6d && C1733k.a("You are currently have JRE version " + d2 + " installed.\nThis application requires JRE 1.6 or higher.\nIf you continue, you will experience problems.\n \nWould you like to get the latest JRE now?", (Component) null, true)) {
                aN.a("http://www.java.com/en/download/manual.jsp");
            }
        } catch (Exception e2) {
            C1733k.a("Unable to determine the JRE version.\nJRE version 1.5 is required for this application", (Component) null);
        }
    }
}
