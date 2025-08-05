package aG;

import com.efiAnalytics.remotefileaccess.DirectoryInformation;

/* loaded from: TunerStudioMS.jar:aG/d.class */
public class d implements DirectoryInformation {

    /* renamed from: a, reason: collision with root package name */
    private int f2387a = 0;

    /* renamed from: b, reason: collision with root package name */
    private long f2388b = -1;

    /* renamed from: c, reason: collision with root package name */
    private long f2389c = 0;

    @Override // com.efiAnalytics.remotefileaccess.DirectoryInformation
    public int getFileCount() {
        return this.f2387a;
    }

    @Override // com.efiAnalytics.remotefileaccess.DirectoryInformation
    public long getTotalBytes() {
        return this.f2388b;
    }

    @Override // com.efiAnalytics.remotefileaccess.DirectoryInformation
    public long getUsedBytes() {
        return this.f2389c;
    }

    public void a(int i2) {
        this.f2387a = i2;
    }

    public void a(long j2) {
        this.f2389c = j2;
    }
}
