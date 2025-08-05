package S;

import java.util.TimerTask;

/* loaded from: TunerStudioMS.jar:S/i.class */
class i extends TimerTask {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ h f1839a;

    i(h hVar) {
        this.f1839a = hVar;
    }

    @Override // java.util.TimerTask, java.lang.Runnable
    public void run() {
        this.f1839a.b(false);
    }
}
