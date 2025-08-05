package r;

import bH.W;
import java.io.File;

/* renamed from: r.g, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:r/g.class */
class C1804g {

    /* renamed from: a, reason: collision with root package name */
    File f13435a;

    /* renamed from: b, reason: collision with root package name */
    final /* synthetic */ C1799b f13436b;

    public C1804g(C1799b c1799b, File file) {
        this.f13436b = c1799b;
        this.f13435a = null;
        this.f13435a = file;
    }

    public File a() {
        return this.f13435a;
    }

    public String toString() {
        return W.b(this.f13435a.getName(), ".dash", "");
    }
}
