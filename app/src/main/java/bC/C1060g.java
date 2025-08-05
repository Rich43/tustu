package bc;

import javax.swing.JComboBox;

/* renamed from: bc.g, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:bc/g.class */
class C1060g extends JComboBox implements InterfaceC1062i {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C1054a f7873a;

    C1060g(C1054a c1054a) {
        this.f7873a = c1054a;
    }

    @Override // bc.InterfaceC1062i
    public Object a() {
        return super.getSelectedItem();
    }

    @Override // bc.InterfaceC1062i
    public void a(Object obj) {
        if (this.f7873a.f7861a.a(a())) {
            super.setSelectedItem(obj);
        }
    }

    @Override // bc.InterfaceC1062i
    public boolean b() {
        return this.f7873a.f7861a.a(a());
    }
}
