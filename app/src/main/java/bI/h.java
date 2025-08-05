package bI;

import java.io.InputStream;

/* loaded from: TunerStudioMS.jar:bI/h.class */
public class h {

    /* renamed from: a, reason: collision with root package name */
    private static h f7100a = null;

    /* renamed from: b, reason: collision with root package name */
    private f f7101b = null;

    private h() {
    }

    public static h a() {
        if (f7100a == null) {
            f7100a = new h();
        }
        return f7100a;
    }

    public void a(f fVar) {
        this.f7101b = fVar;
    }

    public InputStream b() {
        if (this.f7101b == null) {
            return null;
        }
        return this.f7101b.a();
    }
}
