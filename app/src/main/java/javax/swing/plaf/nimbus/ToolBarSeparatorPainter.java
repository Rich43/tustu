package javax.swing.plaf.nimbus;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Insets;
import javax.swing.JComponent;
import javax.swing.plaf.nimbus.AbstractRegionPainter;

/* loaded from: rt.jar:javax/swing/plaf/nimbus/ToolBarSeparatorPainter.class */
final class ToolBarSeparatorPainter extends AbstractRegionPainter {
    private static final int SPACE = 3;
    private static final int INSET = 2;

    ToolBarSeparatorPainter() {
    }

    @Override // javax.swing.plaf.nimbus.AbstractRegionPainter
    protected AbstractRegionPainter.PaintContext getPaintContext() {
        return new AbstractRegionPainter.PaintContext(new Insets(1, 0, 1, 0), new Dimension(38, 7), false, AbstractRegionPainter.PaintContext.CacheMode.NO_CACHING, 1.0d, 1.0d);
    }

    @Override // javax.swing.plaf.nimbus.AbstractRegionPainter
    protected void doPaint(Graphics2D graphics2D, JComponent jComponent, int i2, int i3, Object[] objArr) {
        graphics2D.setColor(jComponent.getForeground());
        int i4 = i3 / 2;
        for (int i5 = 2; i5 <= i2 - 2; i5 += 3) {
            graphics2D.fillRect(i5, i4, 1, 1);
        }
    }
}
