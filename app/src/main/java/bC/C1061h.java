package bc;

import javax.swing.BorderFactory;
import javax.swing.JTextField;

/* renamed from: bc.h, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:bc/h.class */
class C1061h extends JTextField implements InterfaceC1062i {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C1054a f7874a;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    C1061h(C1054a c1054a) {
        super("", 10);
        this.f7874a = c1054a;
        setBorder(BorderFactory.createLoweredBevelBorder());
    }

    @Override // bc.InterfaceC1062i
    public Object a() {
        return super.getText();
    }

    @Override // bc.InterfaceC1062i
    public void a(Object obj) {
        setText(obj.toString());
    }

    @Override // bc.InterfaceC1062i
    public boolean b() {
        return this.f7874a.f7861a.a(a());
    }
}
