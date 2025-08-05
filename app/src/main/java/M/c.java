package m;

import java.util.List;

/* loaded from: TunerStudioMS.jar:m/c.class */
public class c {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C1760a f12910a;

    public c(C1760a c1760a) {
        this.f12910a = c1760a;
    }

    public void a(List list) {
        this.f12910a.f12907b = list;
        synchronized (this) {
            notifyAll();
        }
    }
}
