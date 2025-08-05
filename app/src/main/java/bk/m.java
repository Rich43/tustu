package bk;

import aI.u;
import com.efiAnalytics.remotefileaccess.DirectoryIdentifier;
import com.efiAnalytics.remotefileaccess.RemoteAccessException;

/* loaded from: TunerStudioMS.jar:bk/m.class */
class m extends Thread {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ h f8222a;

    m(h hVar) {
        this.f8222a = hVar;
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public void run() {
        u uVarA;
        try {
            if (this.f8222a.f8213z != null && (uVarA = this.f8222a.f8213z.a((DirectoryIdentifier) null)) != null) {
                this.f8222a.f8201j.a(uVarA, this.f8222a.f8214v);
                this.f8222a.f8213z.c();
            }
        } catch (RemoteAccessException e2) {
        }
    }
}
