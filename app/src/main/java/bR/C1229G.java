package br;

import java.awt.Graphics;
import javax.swing.JLabel;

/* renamed from: br.G, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:br/G.class */
class C1229G extends JLabel {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C1255s f8346a;

    C1229G(C1255s c1255s) {
        this.f8346a = c1255s;
    }

    @Override // javax.swing.JComponent, java.awt.Container, java.awt.Component
    public void paint(Graphics graphics) {
        graphics.setColor(getBackground());
        graphics.fillRect(0, 0, getWidth(), getHeight());
        super.paint(graphics);
    }
}
