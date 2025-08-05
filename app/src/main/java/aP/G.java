package aP;

import java.awt.Window;

/* loaded from: TunerStudioMS.jar:aP/G.class */
class G extends Thread {

    /* renamed from: a, reason: collision with root package name */
    String f2745a;

    /* renamed from: b, reason: collision with root package name */
    Window f2746b;

    /* renamed from: c, reason: collision with root package name */
    String f2747c;

    /* renamed from: e, reason: collision with root package name */
    private boolean f2748e;

    /* renamed from: d, reason: collision with root package name */
    final /* synthetic */ C0338f f2749d;

    public G(C0338f c0338f, Window window, String str) {
        this(c0338f, window, str, null);
        setDaemon(true);
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public G(C0338f c0338f, Window window, String str, String str2) {
        super("ProjectOpenThread");
        this.f2749d = c0338f;
        this.f2745a = null;
        this.f2746b = null;
        this.f2747c = null;
        this.f2748e = true;
        this.f2746b = window;
        this.f2745a = str;
        this.f2747c = str2;
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public void run() {
        try {
            try {
                try {
                    cZ.a().g().b(false);
                    this.f2749d.a(this.f2746b, this.f2745a, this.f2748e);
                    if (this.f2747c != null) {
                        this.f2749d.a(this.f2746b, G.T.a().c(), this.f2747c);
                    }
                    cZ.a().g().b(true);
                } catch (Error e2) {
                    e2.printStackTrace();
                    com.efiAnalytics.ui.bV.d("There was a problem opening the project!\nError: " + e2.getLocalizedMessage(), this.f2746b);
                    cZ.a().g().b(true);
                }
            } catch (Exception e3) {
                if (e3 instanceof W.aj) {
                    bH.C.b("Invalid Password for file: " + this.f2747c);
                } else {
                    bH.C.a("Unexpected Error occured opening project.", e3, this.f2746b);
                }
                cZ.a().g().b(true);
            }
        } catch (Throwable th) {
            cZ.a().g().b(true);
            throw th;
        }
    }

    public void a(boolean z2) {
        this.f2748e = z2;
    }
}
