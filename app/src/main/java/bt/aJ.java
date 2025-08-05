package bt;

import java.awt.Component;
import java.awt.Dimension;
import javax.swing.BorderFactory;
import javax.swing.JScrollPane;

/* loaded from: TunerStudioMS.jar:bt/aJ.class */
class aJ extends JScrollPane {

    /* renamed from: b, reason: collision with root package name */
    private int f8758b;

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C1303al f8759a;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public aJ(C1303al c1303al, Component component) {
        super(component);
        this.f8759a = c1303al;
        this.f8758b = 380;
        super.setHorizontalScrollBarPolicy(31);
        setBorder(BorderFactory.createEmptyBorder());
    }

    @Override // javax.swing.JComponent, java.awt.Container, java.awt.Component
    public Dimension getMinimumSize() {
        Dimension minimumSize = super.getMinimumSize();
        if (minimumSize.getHeight() > this.f8758b) {
            minimumSize.height = this.f8758b;
        }
        return minimumSize;
    }

    @Override // javax.swing.JComponent, java.awt.Container, java.awt.Component
    public Dimension getPreferredSize() {
        Dimension preferredSize = super.getPreferredSize();
        if (preferredSize.getHeight() > this.f8758b) {
            preferredSize.height = this.f8758b;
        }
        return preferredSize;
    }
}
