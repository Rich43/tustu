package ao;

import javax.swing.JCheckBoxMenuItem;

/* renamed from: ao.dn, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:ao/dn.class */
class C0704dn implements gO {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ JCheckBoxMenuItem f5561a;

    /* renamed from: b, reason: collision with root package name */
    final /* synthetic */ bP f5562b;

    C0704dn(bP bPVar, JCheckBoxMenuItem jCheckBoxMenuItem) {
        this.f5562b = bPVar;
        this.f5561a = jCheckBoxMenuItem;
    }

    @Override // ao.gO
    public void b(boolean z2) {
        this.f5561a.setSelected(z2);
    }
}
