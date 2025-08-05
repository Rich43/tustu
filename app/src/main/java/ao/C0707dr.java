package ao;

import javax.swing.JCheckBoxMenuItem;

/* renamed from: ao.dr, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:ao/dr.class */
class C0707dr implements gO {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ JCheckBoxMenuItem f5566a;

    /* renamed from: b, reason: collision with root package name */
    final /* synthetic */ bP f5567b;

    C0707dr(bP bPVar, JCheckBoxMenuItem jCheckBoxMenuItem) {
        this.f5567b = bPVar;
        this.f5566a = jCheckBoxMenuItem;
    }

    @Override // ao.gO
    public void b(boolean z2) {
        this.f5566a.setSelected(z2);
    }
}
