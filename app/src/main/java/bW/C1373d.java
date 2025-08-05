package bw;

import bH.ab;
import java.awt.Dimension;
import java.awt.Insets;
import javax.swing.JLabel;

/* renamed from: bw.d, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:bw/d.class */
class C1373d extends JLabel {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C1371b f9165a;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public C1373d(C1371b c1371b, String str, int i2) {
        super(ab.a().a(str), null, i2);
        this.f9165a = c1371b;
    }

    @Override // javax.swing.JComponent, java.awt.Container, java.awt.Component
    public Dimension getPreferredSize() {
        Dimension preferredSize = super.getPreferredSize();
        Insets insets = getInsets();
        preferredSize.width = getFontMetrics(getFont()).stringWidth(getText()) + insets.left + insets.right;
        return preferredSize;
    }

    @Override // javax.swing.JComponent, java.awt.Container, java.awt.Component
    public Dimension getMinimumSize() {
        Dimension preferredSize = super.getPreferredSize();
        Insets insets = getInsets();
        preferredSize.width = getFontMetrics(getFont()).stringWidth(getText()) + insets.left + insets.right;
        return preferredSize;
    }
}
