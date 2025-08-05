package bb;

import java.io.File;

/* renamed from: bb.K, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:bb/K.class */
class C1034K {

    /* renamed from: c, reason: collision with root package name */
    private File f7735c;

    /* renamed from: d, reason: collision with root package name */
    private ae.k f7736d;

    /* renamed from: a, reason: collision with root package name */
    boolean f7737a;

    /* renamed from: b, reason: collision with root package name */
    final /* synthetic */ C1028E f7738b;

    C1034K(C1028E c1028e, ae.k kVar, File file, boolean z2) {
        this.f7738b = c1028e;
        this.f7737a = false;
        this.f7735c = file;
        this.f7736d = kVar;
        this.f7737a = z2;
    }

    public File a() {
        return this.f7735c;
    }

    public boolean equals(Object obj) {
        return (!(obj instanceof C1034K) || this.f7735c == null) ? (!(obj instanceof File) || this.f7735c == null) ? super.equals(obj) : ((File) obj).equals(this.f7735c) : ((C1034K) obj).a().equals(this.f7735c);
    }

    public String toString() {
        String strA = null;
        if (this.f7738b.f7727j != null) {
            strA = this.f7738b.f7727j.a(this.f7735c);
        }
        if (strA == null) {
            strA = this.f7736d.h().a(this.f7735c);
        }
        return (strA == null || strA.isEmpty()) ? this.f7735c.getName() : this.f7737a ? strA + " (" + this.f7735c.getName() + ") - Recommended" : strA + " (" + this.f7735c.getName() + ")";
    }
}
