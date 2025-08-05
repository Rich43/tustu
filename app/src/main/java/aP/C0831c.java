package ap;

import ao.C0804hg;
import bH.C;
import i.C1743c;
import i.InterfaceC1741a;
import i.InterfaceC1745e;

/* renamed from: ap.c, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:ap/c.class */
public class C0831c implements InterfaceC1741a, InterfaceC1745e {

    /* renamed from: a, reason: collision with root package name */
    C0832d f6194a = null;

    /* renamed from: b, reason: collision with root package name */
    boolean f6195b = false;

    public C0831c() {
        C1743c.a().a(this);
    }

    @Override // i.InterfaceC1745e
    public boolean a(String str, String str2) {
        if (str == null || !str.equals("indexChanged")) {
            return false;
        }
        String[] strArrSplit = str2.split("[|]");
        if (strArrSplit.length != 2) {
            return true;
        }
        String str3 = strArrSplit[0];
        try {
            int i2 = Integer.parseInt(strArrSplit[1]);
            String[] strArrE = C0804hg.a().E();
            if (strArrE == null || strArrE.length != 1 || strArrE[0] == null || !strArrE[0].equals(str3)) {
                return true;
            }
            if (C0804hg.a().D() && (!C0804hg.a().D() || Math.abs(C0804hg.a().p() - i2) <= 3)) {
                return true;
            }
            this.f6195b = true;
            C0804hg.a().c(i2);
            this.f6195b = false;
            return true;
        } catch (Exception e2) {
            C.a("PIPE_ACTION_INDEX_CHANGED: Invalid index value: " + strArrSplit[1]);
            return true;
        }
    }

    @Override // i.InterfaceC1741a
    public void a(int i2) {
        String[] strArrE = C0804hg.a().E();
        if (this.f6195b || strArrE == null || strArrE.length != 1) {
            return;
        }
        a().a(strArrE[0]);
        a().a(i2);
    }

    private C0832d a() {
        if (this.f6194a == null || !this.f6194a.isAlive()) {
            this.f6194a = new C0832d(this);
            this.f6194a.start();
        }
        return this.f6194a;
    }
}
