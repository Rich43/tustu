package aW;

import javax.swing.BorderFactory;
import javax.swing.JTextField;

/* loaded from: TunerStudioMS.jar:aW/m.class */
class m extends JTextField implements n {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ e f3989a;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    m(e eVar) {
        super("", 10);
        this.f3989a = eVar;
        setBorder(BorderFactory.createLoweredBevelBorder());
    }

    @Override // aW.n
    public Object a() {
        return super.getText();
    }

    @Override // aW.n
    public void a(Object obj) {
        setText(obj.toString());
    }

    @Override // aW.n
    public boolean b() {
        return this.f3989a.f3973a.b(a());
    }
}
