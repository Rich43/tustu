package defpackage;

import aP.C0293dh;
import aP.C0338f;
import com.efiAnalytics.ui.bV;
import jdk.internal.dynalink.CallSiteDescriptor;
import r.C1798a;
import r.C1806i;
import s.C1818g;

/* loaded from: TunerStudioMS.jar:g.class */
final class g implements Runnable {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ boolean f12174a;

    /* renamed from: b, reason: collision with root package name */
    final /* synthetic */ String[] f12175b;

    g(boolean z2, String[] strArr) {
        this.f12174a = z2;
        this.f12175b = strArr;
    }

    @Override // java.lang.Runnable
    public void run() {
        C0293dh c0293dh = new C0293dh(this.f12174a);
        TunerStudio.f1881a = c0293dh;
        if (TunerStudio.f1883c) {
            bV.d("Unable to delete " + TunerStudio.f1882b + "\nAttempted to clear file instead.", c0293dh);
        }
        if (this.f12175b.length <= 0 || this.f12175b[0].equals("")) {
            c0293dh.i();
        } else {
            for (int i2 = 0; i2 < this.f12175b.length; i2++) {
                System.out.println("arg found " + i2 + CallSiteDescriptor.TOKEN_DELIMITER + this.f12175b[i2]);
            }
            String[] strArrSplit = C1798a.a().a(C1798a.cx, C1798a.cw).split(";");
            boolean z2 = false;
            if (strArrSplit != null) {
                int length = strArrSplit.length;
                int i3 = 0;
                while (true) {
                    if (i3 >= length) {
                        break;
                    }
                    if (this.f12175b[0].toLowerCase().endsWith(strArrSplit[i3].toLowerCase())) {
                        z2 = true;
                        break;
                    }
                    i3++;
                }
            }
            if (this.f12175b[0].toLowerCase().contains(".msq") || z2) {
                C0338f.a().c(c0293dh, this.f12175b[0]);
            } else if (!this.f12175b[0].toLowerCase().endsWith("." + C1798a.f13286t)) {
                c0293dh.i();
            } else if (C1806i.a().a("09RGDKDG;LKIGD")) {
                C0338f.a().a(this.f12175b[0]);
            } else {
                bV.d(C1818g.b("Open Project Archive not supported in this edition."), c0293dh);
                c0293dh.n();
            }
        }
        TunerStudio.f1881a.h();
    }
}
