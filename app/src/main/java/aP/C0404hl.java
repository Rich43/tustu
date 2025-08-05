package aP;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import s.C1818g;

/* renamed from: aP.hl, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:aP/hl.class */
public class C0404hl implements n.i {

    /* renamed from: a, reason: collision with root package name */
    private static C0404hl f3586a = null;

    /* renamed from: b, reason: collision with root package name */
    private n.i f3587b = null;

    private C0404hl() {
    }

    public static C0404hl a() {
        if (f3586a == null) {
            f3586a = new C0404hl();
        }
        return f3586a;
    }

    @Override // n.i
    public void a(String str) {
        if (this.f3587b != null) {
            this.f3587b.a(str);
        } else if (SwingUtilities.isEventDispatchThread()) {
            e(str);
        } else {
            SwingUtilities.invokeLater(new RunnableC0405hm(this, str));
        }
        bH.C.d(str);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void e(String str) throws IllegalArgumentException {
        String strB = C1818g.b(str);
        iE iEVarE = cZ.a().e();
        iR iRVarF = cZ.a().f();
        if (iEVarE != null) {
            iEVarE.a(strB);
        } else if (iRVarF != null) {
            iRVarF.c(strB);
        }
    }

    public void d(String str) {
        iR iRVarF = cZ.a().f();
        if (iRVarF != null) {
            iRVarF.c(str);
        }
    }

    @Override // n.i
    public void b(String str) {
        if (this.f3587b != null) {
            this.f3587b.b(str);
        } else {
            com.efiAnalytics.ui.bV.d(str, cZ.a().c());
        }
    }

    @Override // n.i
    public boolean c(String str) {
        return this.f3587b != null ? this.f3587b.c(str) : JOptionPane.showConfirmDialog(cZ.a().c(), str, C1818g.b("Approve"), 0, 2) == 0;
    }

    @Override // n.i
    public int a(String str, String[] strArr) {
        return this.f3587b != null ? this.f3587b.a(str, strArr) : com.efiAnalytics.ui.bV.b(str, "Prompt", cZ.a().c(), strArr);
    }
}
