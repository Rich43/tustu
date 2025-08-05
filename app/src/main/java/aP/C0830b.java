package ap;

import ao.C0804hg;
import bH.C;
import i.C1743c;
import i.C1748h;
import i.InterfaceC1745e;
import i.InterfaceC1749i;
import jdk.internal.dynalink.CallSiteDescriptor;

/* renamed from: ap.b, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:ap/b.class */
public class C0830b implements InterfaceC1745e, InterfaceC1749i {

    /* renamed from: a, reason: collision with root package name */
    double f6192a = -1.0d;

    /* renamed from: b, reason: collision with root package name */
    boolean f6193b = false;

    public C0830b() {
        C1743c.a().a(this);
    }

    @Override // i.InterfaceC1745e
    public boolean a(String str, String str2) {
        if (str.equals("startPlayback")) {
            String[] strArrSplit = str2.split("[|]");
            String[] strArrE = C0804hg.a().E();
            if (strArrE == null || strArrE.length <= 0 || !strArrE[0].equals(strArrSplit[0])) {
                C.c("PIPE_ACTION_START_PLAYBACK: Wrong File open");
                return true;
            }
            this.f6193b = true;
            try {
                this.f6192a = C0804hg.a().i();
                C0804hg.a().a(Double.parseDouble(strArrSplit[0]), false);
            } catch (Exception e2) {
                C.c("PIPE_ACTION_START_PLAYBACK: failed to set playback speed: " + str2);
            }
            C0804hg.a().e();
            this.f6193b = false;
            return true;
        }
        if (!str.equals("stopPlayback")) {
            return false;
        }
        String[] strArrSplit2 = str2.split("[|]");
        String[] strArrE2 = C0804hg.a().E();
        if (strArrE2 == null || strArrE2.length <= 0 || !strArrE2[0].equals(strArrSplit2[0])) {
            C.c("PIPE_ACTION_STOP_PLAYBACK: Wrong File open");
            return true;
        }
        this.f6193b = true;
        C0804hg.a().j();
        if (this.f6192a > 0.0d) {
            C0804hg.a().a(this.f6192a, false);
        }
        this.f6193b = false;
        return true;
    }

    @Override // i.InterfaceC1749i
    public void a() {
        if (this.f6193b) {
            return;
        }
        C1748h.a().a("startPlayback", C0804hg.a().E()[0] + CallSiteDescriptor.OPERATOR_DELIMITER + C0804hg.a().i());
    }

    @Override // i.InterfaceC1749i
    public void b() {
        String[] strArrE;
        if (this.f6193b || (strArrE = C0804hg.a().E()) == null) {
            return;
        }
        C1748h.a().a("stopPlayback", strArrE[0]);
    }
}
