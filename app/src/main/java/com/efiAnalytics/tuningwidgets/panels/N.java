package com.efiAnalytics.tuningwidgets.panels;

import java.awt.Dimension;
import java.awt.Insets;
import javax.swing.JLabel;

/* loaded from: TunerStudioMS.jar:com/efiAnalytics/tuningwidgets/panels/N.class */
class N extends JLabel {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ M f10269a;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public N(M m2, String str, int i2) {
        super(str, null, i2);
        this.f10269a = m2;
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
