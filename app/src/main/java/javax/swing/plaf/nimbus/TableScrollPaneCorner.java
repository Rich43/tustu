package javax.swing.plaf.nimbus;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import javax.swing.JComponent;
import javax.swing.Painter;
import javax.swing.UIManager;
import javax.swing.plaf.UIResource;

/* loaded from: rt.jar:javax/swing/plaf/nimbus/TableScrollPaneCorner.class */
class TableScrollPaneCorner extends JComponent implements UIResource {
    TableScrollPaneCorner() {
    }

    @Override // javax.swing.JComponent
    protected void paintComponent(Graphics graphics) {
        Painter painter = (Painter) UIManager.get("TableHeader:\"TableHeader.renderer\"[Enabled].backgroundPainter");
        if (painter != null) {
            if (graphics instanceof Graphics2D) {
                painter.paint((Graphics2D) graphics, this, getWidth() + 1, getHeight());
                return;
            }
            BufferedImage bufferedImage = new BufferedImage(getWidth(), getHeight(), 2);
            Graphics2D graphics2D = (Graphics2D) bufferedImage.getGraphics();
            painter.paint(graphics2D, this, getWidth() + 1, getHeight());
            graphics2D.dispose();
            graphics.drawImage(bufferedImage, 0, 0, null);
        }
    }
}
