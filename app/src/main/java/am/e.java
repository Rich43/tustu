package aM;

import W.InterfaceC0191q;
import javafx.fxml.FXMLLoader;

/* loaded from: TunerStudioMS.jar:aM/e.class */
class e implements InterfaceC0191q {

    /* renamed from: a, reason: collision with root package name */
    long f2624a = -1;

    /* renamed from: b, reason: collision with root package name */
    String f2625b = "";

    /* renamed from: c, reason: collision with root package name */
    final /* synthetic */ a f2626c;

    e(a aVar) {
        this.f2626c = aVar;
    }

    @Override // W.InterfaceC0191q
    public void started(long j2) {
        this.f2624a = j2;
        this.f2625b = (j2 / 1024) + " KB";
    }

    @Override // W.InterfaceC0191q
    public void updateProgress(long j2, double d2) {
        this.f2626c.f2616c.a(((j2 / 1024) + " KB") + " of " + this.f2625b + ", " + Math.round(d2 * 100.0d) + FXMLLoader.RESOURCE_KEY_PREFIX);
    }

    @Override // W.InterfaceC0191q
    public void completed() {
    }
}
