package aP;

import java.awt.Graphics;
import javax.swing.JLabel;

/* renamed from: aP.in, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:aP/in.class */
class C0432in extends JLabel {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C0431im f3739a;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public C0432in(C0431im c0431im, String str) {
        super(str);
        this.f3739a = c0431im;
    }

    @Override // javax.swing.JComponent, java.awt.Container, java.awt.Component
    public void paint(Graphics graphics) {
        super.paint(graphics);
        graphics.drawLine(3, 13, getWidth() - 3, 13);
    }
}
