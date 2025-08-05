package bc;

import com.efiAnalytics.ui.Cdo;

/* renamed from: bc.e, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:bc/e.class */
class C1058e extends Cdo implements InterfaceC1062i {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C1054a f7870a;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    C1058e(C1054a c1054a) {
        super("", 10);
        this.f7870a = c1054a;
        b(0);
    }

    @Override // bc.InterfaceC1062i
    public Object a() {
        return Integer.valueOf(c());
    }

    int c() {
        return Integer.valueOf((int) Math.round(super.e())).intValue();
    }

    @Override // bc.InterfaceC1062i
    public void a(Object obj) {
        super.a(Integer.valueOf(Integer.parseInt(obj.toString())).intValue());
    }

    @Override // bc.InterfaceC1062i
    public boolean b() {
        int iC = c();
        return ((double) iC) >= this.f7870a.f7861a.d() && ((double) iC) <= this.f7870a.f7861a.c();
    }
}
