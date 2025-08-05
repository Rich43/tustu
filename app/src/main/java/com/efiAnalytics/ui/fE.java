package com.efiAnalytics.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.JLabel;

/* loaded from: TunerStudioMS.jar:com/efiAnalytics/ui/fE.class */
public class fE extends JLabel {

    /* renamed from: a, reason: collision with root package name */
    String f11630a = "";

    /* renamed from: b, reason: collision with root package name */
    private Dimension f11631b = null;

    public fE() {
    }

    public fE(String str) {
        setText(str);
        repaint();
    }

    @Override // javax.swing.JLabel
    public void setText(String str) {
        this.f11630a = str;
        setToolTipText(str);
        repaint();
    }

    @Override // javax.swing.JLabel
    public String getText() {
        return this.f11630a;
    }

    @Override // javax.swing.JComponent, java.awt.Container, java.awt.Component
    public void paint(Graphics graphics) {
        int height = getFontMetrics(getFont()).getHeight();
        int i2 = getSize().height;
        int i3 = getSize().width;
        int length = this.f11630a.length() * height;
        if (isOpaque() && getBackground() != null) {
            graphics.setColor(getBackground());
            graphics.fillRect(0, 0, i3, i2);
        }
        if (isEnabled()) {
            graphics.setColor(getForeground());
        } else {
            graphics.setColor(Color.GRAY);
        }
        for (int i4 = 0; i4 < this.f11630a.length(); i4++) {
            int i5 = (height - 1) * (i4 + 1);
            int i6 = i2 - length > 0 ? ((i2 - length) / 2) + i5 : i5;
            int iCharWidth = (i3 - getFontMetrics(getFont()).charWidth(this.f11630a.charAt(i4))) / 2;
            if (i6 > i2 - (2 * getFont().getSize()) && this.f11630a.length() - i4 > 2) {
                graphics.drawString(".", iCharWidth, i6);
                graphics.drawString(".", iCharWidth, i6 + (height / 2));
            } else if (i6 < i2) {
                graphics.drawString("" + this.f11630a.charAt(i4), iCharWidth, i6);
            }
        }
    }

    @Override // javax.swing.JComponent, java.awt.Container, java.awt.Component
    public Dimension getPreferredSize() {
        return new Dimension(getFontMetrics(getFont()).getHeight(), getFont().getSize());
    }

    @Override // javax.swing.JComponent, java.awt.Container, java.awt.Component
    public Dimension getMinimumSize() {
        return this.f11631b != null ? this.f11631b : super.getMinimumSize();
    }
}
