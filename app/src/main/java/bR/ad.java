package br;

import java.awt.Graphics;
import javax.swing.JLabel;

/* loaded from: TunerStudioMS.jar:br/ad.class */
class ad extends JLabel {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ P f8412a;

    ad(P p2) {
        this.f8412a = p2;
    }

    @Override // javax.swing.JComponent, java.awt.Container, java.awt.Component
    public void paint(Graphics graphics) {
        graphics.setColor(getBackground());
        graphics.fillRect(0, 0, getWidth(), getHeight());
        super.paint(graphics);
    }
}
