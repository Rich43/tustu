package aW;

import com.efiAnalytics.ui.Cdo;

/* loaded from: TunerStudioMS.jar:aW/j.class */
class j extends Cdo implements n {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ e f3985a;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    j(e eVar) {
        super("", 10);
        this.f3985a = eVar;
        b(0);
    }

    @Override // aW.n
    public Object a() {
        return Integer.valueOf(c());
    }

    int c() {
        return Integer.valueOf((int) Math.round(super.e())).intValue();
    }

    @Override // aW.n
    public void a(Object obj) {
        super.a(Integer.valueOf(Integer.parseInt(obj.toString())).intValue());
    }

    @Override // aW.n
    public boolean b() {
        int iC = c();
        return ((double) iC) >= this.f3985a.f3973a.f() && ((double) iC) <= this.f3985a.f3973a.e();
    }
}
