package aP;

import com.efiAnalytics.remotefileaccess.http.FileAccessPreferences;
import r.C1807j;

/* loaded from: TunerStudioMS.jar:aP/cY.class */
class cY implements FileAccessPreferences {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ bZ f3134a;

    cY(bZ bZVar) {
        this.f3134a = bZVar;
    }

    @Override // com.efiAnalytics.remotefileaccess.http.FileAccessPreferences
    public String getDownloadDirectory() {
        return aE.a.A() != null ? aE.a.A().q() : h.i.e(h.i.f12326as, C1807j.u());
    }

    @Override // com.efiAnalytics.remotefileaccess.http.FileAccessPreferences
    public int getFileExistsPreference() {
        return h.i.b(h.i.f12322ao, h.i.f12323ap);
    }
}
