package aI;

import com.efiAnalytics.remotefileaccess.DirectoryIdentifier;

/* loaded from: TunerStudioMS.jar:aI/v.class */
public class v extends DirectoryIdentifier {

    /* renamed from: a, reason: collision with root package name */
    private long f2526a = 0;

    @Override // com.efiAnalytics.remotefileaccess.DirectoryIdentifier
    public String getDirectoryId() {
        return "file://MS3_SD/sectorNumber";
    }

    public void a(long j2) {
        this.f2526a = j2;
    }

    @Override // com.efiAnalytics.remotefileaccess.DirectoryIdentifier
    public long getNumericId() {
        return this.f2526a;
    }
}
