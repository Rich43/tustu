package bD;

import java.util.List;

/* renamed from: bD.o, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:bD/o.class */
class C0969o extends Thread {

    /* renamed from: a, reason: collision with root package name */
    List f6682a;

    /* renamed from: b, reason: collision with root package name */
    final /* synthetic */ C0963i f6683b;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    C0969o(C0963i c0963i) {
        super("Download");
        this.f6683b = c0963i;
        this.f6682a = null;
        setDaemon(true);
        this.f6682a = this.f6682a;
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public void run() {
        try {
            this.f6683b.h();
        } finally {
            this.f6683b.setEnabled(true);
            this.f6683b.d();
        }
    }
}
