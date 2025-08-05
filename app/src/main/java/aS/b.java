package aS;

import G.C0130m;
import G.C0132o;
import G.InterfaceC0109co;
import G.R;
import G.T;
import G.aG;
import G.bS;
import G.cL;
import G.da;
import aP.cZ;
import bH.W;
import c.C1382a;
import com.efiAnalytics.apps.ts.dashboard.C1425x;
import r.C1798a;
import s.C1818g;
import sun.java2d.marlin.MarlinConst;

/* loaded from: TunerStudioMS.jar:aS/b.class */
class b implements aG, cL, InterfaceC0109co {

    /* renamed from: a, reason: collision with root package name */
    String f3898a;

    /* renamed from: b, reason: collision with root package name */
    boolean f3899b = false;

    /* renamed from: c, reason: collision with root package name */
    String f3900c = null;

    /* renamed from: d, reason: collision with root package name */
    String f3901d = "";

    /* renamed from: e, reason: collision with root package name */
    int f3902e = 0;

    /* renamed from: f, reason: collision with root package name */
    C1425x f3903f = null;

    /* renamed from: g, reason: collision with root package name */
    final /* synthetic */ a f3904g;

    public b(a aVar, String str) {
        this.f3904g = aVar;
        this.f3898a = "";
        this.f3898a = str;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String a() {
        R rC = T.a().c(this.f3898a);
        if (this.f3901d != null && this.f3901d.length() > 0) {
            return a(this.f3901d, rC != null ? rC.i() : "");
        }
        if (rC == null || rC.O().O() == null) {
            return c(rC != null ? rC.i() : "");
        }
        C0132o c0132oA = new da().a(rC, C0130m.f(rC.O()), rC.O().i() * 2);
        if (c0132oA.a() != 1) {
            return c(rC.i());
        }
        this.f3901d = c0132oA.c();
        return a(this.f3901d, rC.i());
    }

    private String c(String str) {
        String strA = C1382a.a(str, C1798a.f13272f);
        return "<html><b>You have a settings error!</b> on " + this.f3898a + "<br>This means there are conflicting settings and the " + strA + " does not know how to handle them <br> <br>To resolve this you need to correct your settings then powercycle the " + strA + " <br>You do not need to reload firmware to fix this problem.<br><br>To see the " + strA + " reported error, briefly power cycle the " + strA + " now.<br> <br>If you still receive no " + strA + " reported error;<br>You can get the error message by performing the following steps:<br> - Open the Mini-Terminal under the menu:<br>     &nbsp;&nbsp;&nbsp;&nbsp;" + C1818g.b("Communications") + "-->" + C1818g.b("Mini Terminal") + "<br> - Power Cycle the " + strA + ".<br><br>The condition causing the conflict will be reported in the lower text box.</html>";
    }

    private String a(String str, String str2) {
        String strB = W.b(str, "\n", "<br>");
        if (this.f3904g.f3895f == a.f3894e) {
            return "<html><b>Protocol Error Reported</b> for " + this.f3898a + "<br> <br>The following error was reported during communication attempts:<br><b>" + strB + "</b><br><br></html>";
        }
        String strA = C1382a.a(str2, C1798a.f13272f);
        return "<html><b>You have a settings error!</b> on " + this.f3898a + "<br>This means there are conflicting settings and the " + strA + " does not know how to handle them <br> <br>The following error was reported by the " + strA + ":<br><b>" + strB + "</b><br><br>Unless directed in the above message, you do not need to reload firmware to fix this problem.<br><br>To resolve this you need to correct this error in your settings then powercycle the " + strA + " <br></html>";
    }

    private void b() {
        this.f3899b = true;
        new c(this).start();
    }

    @Override // G.InterfaceC0109co
    public void setCurrentOutputChannelValue(String str, double d2) {
        if (d2 != 0.0d && this.f3904g.f3896c.R()) {
            this.f3902e++;
            if (this.f3902e <= 2 || this.f3899b || System.currentTimeMillis() - this.f3904g.f3892b <= MarlinConst.statDump) {
                return;
            }
            b();
            return;
        }
        if (!this.f3899b) {
            this.f3902e = 0;
            this.f3901d = null;
            return;
        }
        this.f3899b = false;
        if (this.f3903f != null) {
            this.f3903f.ab();
            this.f3903f = null;
        }
        this.f3901d = null;
        cZ.a().b().ac();
    }

    @Override // G.cL
    public void a(boolean z2, int i2, String str, int[] iArr, int[] iArr2) {
        this.f3901d = str;
        if (!z2 || this.f3899b) {
            return;
        }
        this.f3904g.f3895f = a.f3894e;
        b();
    }

    @Override // G.cL
    public void b(String str) {
        this.f3901d = str;
        if (this.f3899b) {
            return;
        }
        this.f3904g.f3895f = a.f3893d;
        b();
    }

    @Override // G.aG
    public boolean a(String str, bS bSVar) {
        return true;
    }

    @Override // G.aG
    public void a(String str) {
        cZ.a().b().ac();
        this.f3899b = false;
        this.f3901d = null;
        this.f3902e = 0;
    }
}
