package bt;

import org.icepdf.core.util.PdfOps;

/* renamed from: bt.z, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:bt/z.class */
class C1367z {

    /* renamed from: a, reason: collision with root package name */
    String f9139a;

    /* renamed from: b, reason: collision with root package name */
    String f9140b;

    /* renamed from: c, reason: collision with root package name */
    final /* synthetic */ C1366y f9141c;

    C1367z(C1366y c1366y, String str, String str2) {
        this.f9141c = c1366y;
        this.f9139a = null;
        this.f9140b = null;
        this.f9139a = str2;
        this.f9140b = str;
    }

    C1367z(C1366y c1366y, String str) {
        this.f9141c = c1366y;
        this.f9139a = null;
        this.f9140b = null;
        this.f9140b = str;
    }

    public String a() {
        return this.f9140b;
    }

    public boolean equals(Object obj) {
        return ((obj instanceof String) || (obj instanceof C1367z)) ? obj.equals(this.f9140b) : super.equals(obj);
    }

    public String toString() {
        return this.f9139a != null ? bH.W.b(this.f9139a, PdfOps.DOUBLE_QUOTE__TOKEN, "") : bH.W.b(this.f9140b, PdfOps.DOUBLE_QUOTE__TOKEN, "");
    }
}
