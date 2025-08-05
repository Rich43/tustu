package bF;

import java.awt.Color;
import javax.swing.UIManager;

/* renamed from: bF.p, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:bF/p.class */
class C0985p extends v {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C0973d f6877a;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    C0985p(C0973d c0973d) {
        super(c0973d.f6846a);
        this.f6877a = c0973d;
        a(false);
        setBackground(UIManager.getColor("Label.background"));
        setFont(UIManager.getFont("Menu.font"));
    }

    @Override // javax.swing.table.DefaultTableCellRenderer, javax.swing.JComponent, java.awt.Component
    public void setBackground(Color color) {
        super.setBackground(UIManager.getColor("Label.background"));
    }
}
