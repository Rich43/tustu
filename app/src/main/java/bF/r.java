package bF;

import javax.swing.Icon;
import javax.swing.JButton;

/* loaded from: TunerStudioMS.jar:bF/r.class */
class r extends JButton {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C0973d f6879a;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public r(C0973d c0973d, String str, Icon icon) {
        super(str, icon);
        this.f6879a = c0973d;
        super.setEnabled(false);
    }

    @Override // javax.swing.AbstractButton, javax.swing.JComponent, java.awt.Component, c.InterfaceC1385d
    public void setEnabled(boolean z2) {
        if (this.f6879a.d()) {
            super.setEnabled(z2);
        } else {
            super.setEnabled(false);
        }
    }
}
