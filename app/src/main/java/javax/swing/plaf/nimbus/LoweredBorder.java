package javax.swing.plaf.nimbus;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import javax.swing.JComponent;
import javax.swing.border.Border;
import javax.swing.plaf.nimbus.AbstractRegionPainter;
import javax.swing.plaf.nimbus.ImageScalingHelper;

/* loaded from: rt.jar:javax/swing/plaf/nimbus/LoweredBorder.class */
class LoweredBorder extends AbstractRegionPainter implements Border {
    private static final int IMG_SIZE = 30;
    private static final int RADIUS = 13;
    private static final Insets INSETS = new Insets(10, 10, 10, 10);
    private static final AbstractRegionPainter.PaintContext PAINT_CONTEXT = new AbstractRegionPainter.PaintContext(INSETS, new Dimension(30, 30), false, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, 2.147483647E9d, 2.147483647E9d);

    LoweredBorder() {
    }

    @Override // javax.swing.plaf.nimbus.AbstractRegionPainter
    protected Object[] getExtendedCacheKeys(JComponent jComponent) {
        if (jComponent != null) {
            return new Object[]{jComponent.getBackground()};
        }
        return null;
    }

    @Override // javax.swing.plaf.nimbus.AbstractRegionPainter
    protected void doPaint(Graphics2D graphics2D, JComponent jComponent, int i2, int i3, Object[] objArr) {
        Color background = jComponent == null ? Color.BLACK : jComponent.getBackground();
        BufferedImage bufferedImage = new BufferedImage(30, 30, 2);
        BufferedImage bufferedImage2 = new BufferedImage(30, 30, 2);
        Graphics2D graphics2D2 = (Graphics2D) bufferedImage.getGraphics();
        graphics2D2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics2D2.setColor(background);
        graphics2D2.fillRoundRect(2, 0, 26, 26, 13, 13);
        graphics2D2.dispose();
        InnerShadowEffect innerShadowEffect = new InnerShadowEffect();
        innerShadowEffect.setDistance(1);
        innerShadowEffect.setSize(3);
        innerShadowEffect.setColor(getLighter(background, 2.1f));
        innerShadowEffect.setAngle(90);
        innerShadowEffect.applyEffect(bufferedImage, bufferedImage2, 30, 30);
        Graphics2D graphics2D3 = (Graphics2D) bufferedImage2.getGraphics();
        graphics2D3.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics2D3.setClip(0, 28, 30, 1);
        graphics2D3.setColor(getLighter(background, 0.9f));
        graphics2D3.drawRoundRect(2, 1, 25, 25, 13, 13);
        graphics2D3.dispose();
        if (i2 != 30 || i3 != 30) {
            ImageScalingHelper.paint(graphics2D, 0, 0, i2, i3, bufferedImage2, INSETS, INSETS, ImageScalingHelper.PaintType.PAINT9_STRETCH, 512);
        } else {
            graphics2D.drawImage(bufferedImage2, 0, 0, jComponent);
        }
    }

    @Override // javax.swing.plaf.nimbus.AbstractRegionPainter
    protected AbstractRegionPainter.PaintContext getPaintContext() {
        return PAINT_CONTEXT;
    }

    @Override // javax.swing.border.Border
    public Insets getBorderInsets(Component component) {
        return (Insets) INSETS.clone();
    }

    @Override // javax.swing.border.Border
    public boolean isBorderOpaque() {
        return false;
    }

    @Override // javax.swing.border.Border
    public void paintBorder(Component component, Graphics graphics, int i2, int i3, int i4, int i5) {
        JComponent jComponent = component instanceof JComponent ? (JComponent) component : null;
        if (graphics instanceof Graphics2D) {
            Graphics2D graphics2D = (Graphics2D) graphics;
            graphics2D.translate(i2, i3);
            paint(graphics2D, jComponent, i4, i5);
            graphics2D.translate(-i2, -i3);
            return;
        }
        BufferedImage bufferedImage = new BufferedImage(30, 30, 2);
        Graphics2D graphics2D2 = (Graphics2D) bufferedImage.getGraphics();
        paint(graphics2D2, jComponent, i4, i5);
        graphics2D2.dispose();
        ImageScalingHelper.paint(graphics, i2, i3, i4, i5, bufferedImage, INSETS, INSETS, ImageScalingHelper.PaintType.PAINT9_STRETCH, 512);
    }

    private Color getLighter(Color color, float f2) {
        return new Color(Math.min((int) (color.getRed() / f2), 255), Math.min((int) (color.getGreen() / f2), 255), Math.min((int) (color.getBlue() / f2), 255));
    }
}
