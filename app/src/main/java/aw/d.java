package aW;

import A.y;
import java.util.Iterator;
import java.util.List;

/* loaded from: TunerStudioMS.jar:aW/d.class */
class d implements y {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ a f3972a;

    d(a aVar) {
        this.f3972a = aVar;
    }

    @Override // A.y
    public void a(List list) {
        this.f3972a.f3965e = this.f3972a.b();
        if (this.f3972a.f3965e != null) {
            this.f3972a.f3966f.a();
            this.f3972a.f3966f.a(list);
            Iterator it = list.iterator();
            while (it.hasNext()) {
                A.r rVar = (A.r) it.next();
                this.f3972a.f3966f.a(rVar.c(), this.f3972a.f3965e.a(rVar.c()));
            }
            this.f3972a.validate();
        }
    }
}
