package javax.swing.plaf.nimbus;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import javax.swing.JComponent;
import javax.swing.plaf.nimbus.AbstractRegionPainter;

/* loaded from: rt.jar:javax/swing/plaf/nimbus/RadioButtonMenuItemPainter.class */
final class RadioButtonMenuItemPainter extends AbstractRegionPainter {
    static final int BACKGROUND_DISABLED = 1;
    static final int BACKGROUND_ENABLED = 2;
    static final int BACKGROUND_MOUSEOVER = 3;
    static final int BACKGROUND_SELECTED_MOUSEOVER = 4;
    static final int CHECKICON_DISABLED_SELECTED = 5;
    static final int CHECKICON_ENABLED_SELECTED = 6;
    static final int CHECKICON_SELECTED_MOUSEOVER = 7;
    static final int ICON_DISABLED = 8;
    static final int ICON_ENABLED = 9;
    static final int ICON_MOUSEOVER = 10;
    private int state;
    private AbstractRegionPainter.PaintContext ctx;
    private Path2D path = new Path2D.Float();
    private Rectangle2D rect = new Rectangle2D.Float(0.0f, 0.0f, 0.0f, 0.0f);
    private RoundRectangle2D roundRect = new RoundRectangle2D.Float(0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f);
    private Ellipse2D ellipse = new Ellipse2D.Float(0.0f, 0.0f, 0.0f, 0.0f);
    private Color color1 = decodeColor("nimbusSelection", 0.0f, 0.0f, 0.0f, 0);
    private Color color2 = decodeColor("nimbusBlueGrey", 0.0f, -0.08983666f, -0.17647058f, 0);
    private Color color3 = decodeColor("nimbusBlueGrey", 0.055555582f, -0.09663743f, -0.4627451f, 0);
    private Color color4 = decodeColor("nimbusBlueGrey", 0.0f, -0.110526316f, 0.25490195f, 0);
    private Object[] componentColors;

    public RadioButtonMenuItemPainter(AbstractRegionPainter.PaintContext paintContext, int i2) {
        this.state = i2;
        this.ctx = paintContext;
    }

    @Override // javax.swing.plaf.nimbus.AbstractRegionPainter
    protected void doPaint(Graphics2D graphics2D, JComponent jComponent, int i2, int i3, Object[] objArr) {
        this.componentColors = objArr;
        switch (this.state) {
            case 3:
                paintBackgroundMouseOver(graphics2D);
                break;
            case 4:
                paintBackgroundSelectedAndMouseOver(graphics2D);
                break;
            case 5:
                paintcheckIconDisabledAndSelected(graphics2D);
                break;
            case 6:
                paintcheckIconEnabledAndSelected(graphics2D);
                break;
            case 7:
                paintcheckIconSelectedAndMouseOver(graphics2D);
                break;
        }
    }

    @Override // javax.swing.plaf.nimbus.AbstractRegionPainter
    protected final AbstractRegionPainter.PaintContext getPaintContext() {
        return this.ctx;
    }

    private void paintBackgroundMouseOver(Graphics2D graphics2D) {
        this.rect = decodeRect1();
        graphics2D.setPaint(this.color1);
        graphics2D.fill(this.rect);
    }

    private void paintBackgroundSelectedAndMouseOver(Graphics2D graphics2D) {
        this.rect = decodeRect1();
        graphics2D.setPaint(this.color1);
        graphics2D.fill(this.rect);
    }

    private void paintcheckIconDisabledAndSelected(Graphics2D graphics2D) {
        this.path = decodePath1();
        graphics2D.setPaint(this.color2);
        graphics2D.fill(this.path);
    }

    private void paintcheckIconEnabledAndSelected(Graphics2D graphics2D) {
        this.path = decodePath2();
        graphics2D.setPaint(this.color3);
        graphics2D.fill(this.path);
    }

    private void paintcheckIconSelectedAndMouseOver(Graphics2D graphics2D) {
        this.path = decodePath2();
        graphics2D.setPaint(this.color4);
        graphics2D.fill(this.path);
    }

    private Rectangle2D decodeRect1() {
        this.rect.setRect(decodeX(1.0f), decodeY(1.0f), decodeX(2.0f) - decodeX(1.0f), decodeY(2.0f) - decodeY(1.0f));
        return this.rect;
    }

    private Path2D decodePath1() {
        this.path.reset();
        this.path.moveTo(decodeX(0.0f), decodeY(2.097561f));
        this.path.lineTo(decodeX(0.90975606f), decodeY(0.20243903f));
        this.path.lineTo(decodeX(3.0f), decodeY(2.102439f));
        this.path.lineTo(decodeX(0.90731704f), decodeY(3.0f));
        this.path.lineTo(decodeX(0.0f), decodeY(2.097561f));
        this.path.closePath();
        return this.path;
    }

    private Path2D decodePath2() {
        this.path.reset();
        this.path.moveTo(decodeX(0.0024390244f), decodeY(2.097561f));
        this.path.lineTo(decodeX(0.90975606f), decodeY(0.20243903f));
        this.path.lineTo(decodeX(3.0f), decodeY(2.102439f));
        this.path.lineTo(decodeX(0.90731704f), decodeY(3.0f));
        this.path.lineTo(decodeX(0.0024390244f), decodeY(2.097561f));
        this.path.closePath();
        return this.path;
    }
}
