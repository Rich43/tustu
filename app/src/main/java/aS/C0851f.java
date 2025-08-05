package as;

import com.efiAnalytics.remotefileaccess.http.FileAccessPreferences;

/* renamed from: as.f, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:as/f.class */
class C0851f implements FileAccessPreferences {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C0847b f6251a;

    C0851f(C0847b c0847b) {
        this.f6251a = c0847b;
    }

    @Override // com.efiAnalytics.remotefileaccess.http.FileAccessPreferences
    public String getDownloadDirectory() {
        int iB = h.i.b(h.i.f12327at, h.i.f12328au);
        String strE = h.i.e("lastFileDir", h.h.d());
        return iB == h.i.f12329av ? h.i.e(h.i.f12326as, strE) : strE;
    }

    @Override // com.efiAnalytics.remotefileaccess.http.FileAccessPreferences
    public int getFileExistsPreference() {
        return h.i.b(h.i.f12322ao, h.i.f12323ap);
    }
}
