package aS;

import G.InterfaceC0109co;
import aP.cZ;

/* loaded from: TunerStudioMS.jar:aS/e.class */
class e implements InterfaceC0109co {

    /* renamed from: a, reason: collision with root package name */
    int f3907a = 65535;

    /* renamed from: b, reason: collision with root package name */
    b f3908b;

    /* renamed from: c, reason: collision with root package name */
    final /* synthetic */ a f3909c;

    e(a aVar, b bVar) {
        this.f3909c = aVar;
        this.f3908b = bVar;
    }

    @Override // G.InterfaceC0109co
    public void setCurrentOutputChannelValue(String str, double d2) {
        if (str.equals("seconds") && d2 < this.f3907a && this.f3907a < 65533) {
            cZ.a().b().ac();
            this.f3908b.f3899b = false;
            this.f3908b.f3901d = null;
        }
        this.f3907a = (int) d2;
    }
}
