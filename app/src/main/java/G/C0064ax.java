package G;

import java.io.IOException;

/* renamed from: G.ax, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:G/ax.class */
class C0064ax extends Thread {

    /* renamed from: a, reason: collision with root package name */
    C0132o f818a;

    /* renamed from: b, reason: collision with root package name */
    final /* synthetic */ C0054an f819b;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    C0064ax(C0054an c0054an, C0132o c0132o) {
        super("MSyncReadProcess");
        this.f819b = c0054an;
        this.f818a = null;
        setDaemon(true);
        this.f818a = c0132o;
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public void run() throws IOException {
        this.f819b.a(this.f818a);
    }
}
