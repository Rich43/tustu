package aW;

import javax.swing.JCheckBox;

/* loaded from: TunerStudioMS.jar:aW/i.class */
class i extends JCheckBox implements n {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ e f3984a;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    i(e eVar, String str) {
        super(str);
        this.f3984a = eVar;
    }

    @Override // aW.n
    public Object a() {
        return new Boolean(super.isSelected());
    }

    @Override // aW.n
    public void a(Object obj) {
        super.setSelected(((Boolean) obj).booleanValue());
    }

    @Override // aW.n
    public boolean b() {
        return true;
    }
}
