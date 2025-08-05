package m;

import am.C0577e;
import com.efiAnalytics.ui.InterfaceC1565bc;
import java.util.List;

/* loaded from: TunerStudioMS.jar:m/i.class */
final class i implements InterfaceC1565bc {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ d f12920a;

    /* renamed from: b, reason: collision with root package name */
    final /* synthetic */ List f12921b;

    /* renamed from: c, reason: collision with root package name */
    final /* synthetic */ c f12922c;

    i(d dVar, List list, c cVar) {
        this.f12920a = dVar;
        this.f12921b = list;
        this.f12922c = cVar;
    }

    @Override // com.efiAnalytics.ui.InterfaceC1565bc
    public void close() {
        List listA = this.f12920a.a();
        int i2 = 0;
        while (i2 < this.f12921b.size()) {
            if (!listA.contains(Integer.valueOf(((C0577e) this.f12921b.get(i2)).h()))) {
                this.f12921b.remove(i2);
                i2--;
            }
            i2++;
        }
        this.f12922c.a(this.f12921b);
    }
}
