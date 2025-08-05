package ao;

import java.awt.Dimension;
import javax.swing.JComponent;
import javax.swing.JScrollPane;

/* renamed from: ao.he, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:ao/he.class */
class C0802he extends JScrollPane {

    /* renamed from: a, reason: collision with root package name */
    JComponent f6049a;

    /* renamed from: b, reason: collision with root package name */
    final /* synthetic */ gS f6050b;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    C0802he(gS gSVar, JComponent jComponent) {
        super(jComponent);
        this.f6050b = gSVar;
        this.f6049a = jComponent;
        setHorizontalScrollBarPolicy(31);
    }

    @Override // javax.swing.JComponent, java.awt.Container, java.awt.Component
    public Dimension getMinimumSize() {
        Dimension minimumSize = super.getMinimumSize();
        if (super.getVerticalScrollBar() != null) {
            minimumSize.width = this.f6049a.getMinimumSize().width + super.getVerticalScrollBar().getWidth();
        } else {
            minimumSize.width = this.f6049a.getMinimumSize().width + com.efiAnalytics.ui.eJ.a(17);
        }
        return minimumSize;
    }

    @Override // javax.swing.JComponent, java.awt.Container, java.awt.Component
    public Dimension getPreferredSize() {
        Dimension preferredSize = super.getPreferredSize();
        if (super.getVerticalScrollBar() != null) {
            preferredSize.width = this.f6049a.getPreferredSize().width + super.getVerticalScrollBar().getWidth();
        } else {
            preferredSize.width = this.f6049a.getPreferredSize().width + com.efiAnalytics.ui.eJ.a(17);
        }
        preferredSize.height = this.f6049a.getPreferredSize().height + 2;
        return preferredSize;
    }
}
