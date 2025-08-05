package A;

import bH.C;
import java.util.List;

/* loaded from: TunerStudioMS.jar:A/n.class */
class n extends Thread {

    /* renamed from: a, reason: collision with root package name */
    boolean f41a;

    /* renamed from: b, reason: collision with root package name */
    List f42b;

    /* renamed from: c, reason: collision with root package name */
    f f43c;

    /* renamed from: d, reason: collision with root package name */
    boolean f44d;

    /* renamed from: e, reason: collision with root package name */
    final /* synthetic */ m f45e;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    n(m mVar) {
        super("check");
        this.f45e = mVar;
        this.f41a = true;
        this.f42b = null;
        this.f43c = null;
        this.f44d = false;
        setDaemon(true);
        C.c("Created CheckThread");
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public void run() {
        while (this.f41a) {
            if (this.f43c == null || this.f42b == null) {
                try {
                    Thread.sleep(50L);
                } catch (InterruptedException e2) {
                }
            } else {
                this.f45e.b(this.f43c, this.f42b);
                this.f44d = false;
            }
        }
        C.c("CheckThread exiting");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void a(f fVar, List list) {
        this.f43c = fVar;
        this.f42b = list;
        this.f44d = true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void a() {
        this.f43c = null;
        this.f42b = null;
    }
}
