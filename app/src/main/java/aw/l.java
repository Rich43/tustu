package aW;

import javax.swing.JComboBox;

/* loaded from: TunerStudioMS.jar:aW/l.class */
class l extends JComboBox implements n {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ e f3988a;

    l(e eVar) {
        this.f3988a = eVar;
        super.setEditable(true);
    }

    @Override // aW.n
    public Object a() {
        return super.getSelectedItem();
    }

    @Override // aW.n
    public void a(Object obj) {
        if (this.f3988a.f3973a.b(a())) {
            super.setSelectedItem(obj);
        }
    }

    @Override // aW.n
    public boolean b() {
        return this.f3988a.f3973a.b(a());
    }
}
