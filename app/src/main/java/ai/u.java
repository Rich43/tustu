package aI;

import bH.C0995c;
import com.efiAnalytics.remotefileaccess.DirectoryInformation;

/* loaded from: TunerStudioMS.jar:aI/u.class */
public class u implements DirectoryInformation {

    /* renamed from: a, reason: collision with root package name */
    private int f2512a = 0;

    /* renamed from: b, reason: collision with root package name */
    private long f2513b = 0;

    /* renamed from: c, reason: collision with root package name */
    private long f2514c = -1;

    /* renamed from: d, reason: collision with root package name */
    private int f2515d = 0;

    /* renamed from: e, reason: collision with root package name */
    private long f2516e = 0;

    /* renamed from: f, reason: collision with root package name */
    private int f2517f = 0;

    /* renamed from: g, reason: collision with root package name */
    private boolean f2518g = false;

    /* renamed from: h, reason: collision with root package name */
    private boolean f2519h = false;

    /* renamed from: i, reason: collision with root package name */
    private boolean f2520i = false;

    /* renamed from: j, reason: collision with root package name */
    private boolean f2521j = false;

    /* renamed from: k, reason: collision with root package name */
    private boolean f2522k = false;

    /* renamed from: l, reason: collision with root package name */
    private boolean f2523l = false;

    /* renamed from: m, reason: collision with root package name */
    private boolean f2524m = false;

    /* renamed from: n, reason: collision with root package name */
    private boolean f2525n = false;

    protected void a(int[] iArr) {
        this.f2518g = (iArr[0] & 1) == 1;
        this.f2519h = (iArr[0] & 2) == 2;
        this.f2520i = (iArr[0] & 4) == 4;
        this.f2521j = (iArr[0] & 8) == 8;
        this.f2522k = (iArr[0] & 16) == 16;
        this.f2523l = (iArr[0] & 32) == 32;
        this.f2524m = (iArr[0] & 64) == 64;
        this.f2525n = (iArr[0] & 128) == 128;
        this.f2517f = iArr[1];
        this.f2515d = C0995c.b(iArr, 2, 2, true, false);
        this.f2516e = C0995c.c(iArr, 4, 4, true, false);
        this.f2512a = C0995c.b(iArr, 8, 2, true, false);
        this.f2514c = C0995c.c(iArr, 10, 4, true, false);
    }

    @Override // com.efiAnalytics.remotefileaccess.DirectoryInformation
    public int getFileCount() {
        return this.f2512a;
    }

    @Override // com.efiAnalytics.remotefileaccess.DirectoryInformation
    public long getTotalBytes() {
        return this.f2516e * this.f2515d;
    }

    @Override // com.efiAnalytics.remotefileaccess.DirectoryInformation
    public long getUsedBytes() {
        return this.f2513b;
    }

    public void a(int i2) {
        this.f2512a = i2;
    }

    public void a(long j2) {
        this.f2513b = j2;
    }

    public int a() {
        return this.f2515d;
    }
}
