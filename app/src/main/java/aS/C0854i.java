package as;

import java.awt.FontMetrics;
import java.awt.Graphics;
import javax.swing.JTabbedPane;

/* renamed from: as.i, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:as/i.class */
class C0854i extends JTabbedPane {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C0852g f6257a;

    C0854i(C0852g c0852g) {
        this.f6257a = c0852g;
    }

    @Override // javax.swing.JComponent, java.awt.Container, java.awt.Component
    public void paint(Graphics graphics) {
        super.paint(graphics);
        if (super.getTabCount() == 0) {
            int height = getHeight() / 2;
            FontMetrics fontMetrics = graphics.getFontMetrics();
            graphics.drawString("No log Servers found on the network.", (getWidth() - fontMetrics.stringWidth("No log Servers found on the network.")) / 2, height);
            graphics.drawString("Make sure the log server is running and connected to the same network.", (getWidth() - fontMetrics.stringWidth("Make sure the log server is running and connected to the same network.")) / 2, height + graphics.getFont().getSize());
        }
    }
}
