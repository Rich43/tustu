package bc;

import bH.C;
import javax.swing.JCheckBox;

/* renamed from: bc.d, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:bc/d.class */
class C1057d extends JCheckBox implements InterfaceC1062i {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C1054a f7869a;

    C1057d(C1054a c1054a) {
        this.f7869a = c1054a;
    }

    @Override // bc.InterfaceC1062i
    public Object a() {
        return Boolean.valueOf(super.isSelected());
    }

    @Override // bc.InterfaceC1062i
    public void a(Object obj) {
        if (obj instanceof Boolean) {
            super.setSelected(((Boolean) obj).booleanValue());
        } else if (obj instanceof String) {
            super.setSelected(Boolean.parseBoolean(obj.toString()));
        } else {
            C.c("Invalid Boolean value: " + obj);
            this.f7869a.b();
        }
    }

    @Override // bc.InterfaceC1062i
    public boolean b() {
        return true;
    }
}
