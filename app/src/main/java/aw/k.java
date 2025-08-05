package aW;

import java.awt.Dimension;
import javax.swing.JLabel;

/* loaded from: TunerStudioMS.jar:aW/k.class */
class k extends JLabel {

    /* renamed from: a, reason: collision with root package name */
    int f3986a;

    /* renamed from: b, reason: collision with root package name */
    final /* synthetic */ e f3987b;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    k(e eVar, String str) {
        super(str, 4);
        this.f3987b = eVar;
        this.f3986a = 80;
    }

    @Override // javax.swing.JComponent, java.awt.Container, java.awt.Component
    public Dimension getPreferredSize() {
        Dimension preferredSize = super.getPreferredSize();
        if (preferredSize.width < this.f3986a) {
            preferredSize.width = this.f3986a;
        }
        return preferredSize;
    }

    @Override // javax.swing.JComponent, java.awt.Container, java.awt.Component
    public Dimension getMinimumSize() {
        Dimension minimumSize = super.getMinimumSize();
        if (minimumSize.width < this.f3986a) {
            minimumSize.width = this.f3986a;
        }
        return minimumSize;
    }
}
