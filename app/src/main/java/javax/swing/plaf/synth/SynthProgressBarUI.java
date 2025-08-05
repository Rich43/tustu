package javax.swing.plaf.synth;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.JComponent;
import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicProgressBarUI;
import javax.swing.plaf.nimbus.NimbusStyle;
import sun.swing.SwingUtilities2;

/* loaded from: rt.jar:javax/swing/plaf/synth/SynthProgressBarUI.class */
public class SynthProgressBarUI extends BasicProgressBarUI implements SynthUI, PropertyChangeListener {
    private SynthStyle style;
    private int progressPadding;
    private boolean rotateText;
    private boolean paintOutsideClip;
    private boolean tileWhenIndeterminate;
    private int tileWidth;
    private Dimension minBarSize;
    private int glowWidth;

    public static ComponentUI createUI(JComponent jComponent) {
        return new SynthProgressBarUI();
    }

    @Override // javax.swing.plaf.basic.BasicProgressBarUI
    protected void installListeners() {
        super.installListeners();
        this.progressBar.addPropertyChangeListener(this);
    }

    @Override // javax.swing.plaf.basic.BasicProgressBarUI
    protected void uninstallListeners() {
        super.uninstallListeners();
        this.progressBar.removePropertyChangeListener(this);
    }

    @Override // javax.swing.plaf.basic.BasicProgressBarUI
    protected void installDefaults() {
        updateStyle(this.progressBar);
    }

    private void updateStyle(JProgressBar jProgressBar) {
        SynthContext context = getContext(jProgressBar, 1);
        SynthStyle synthStyle = this.style;
        this.style = SynthLookAndFeel.updateStyle(context, this);
        setCellLength(this.style.getInt(context, "ProgressBar.cellLength", 1));
        setCellSpacing(this.style.getInt(context, "ProgressBar.cellSpacing", 0));
        this.progressPadding = this.style.getInt(context, "ProgressBar.progressPadding", 0);
        this.paintOutsideClip = this.style.getBoolean(context, "ProgressBar.paintOutsideClip", false);
        this.rotateText = this.style.getBoolean(context, "ProgressBar.rotateText", false);
        this.tileWhenIndeterminate = this.style.getBoolean(context, "ProgressBar.tileWhenIndeterminate", false);
        this.tileWidth = this.style.getInt(context, "ProgressBar.tileWidth", 15);
        String str = (String) this.progressBar.getClientProperty("JComponent.sizeVariant");
        if (str != null) {
            if (NimbusStyle.LARGE_KEY.equals(str)) {
                this.tileWidth = (int) (this.tileWidth * 1.15d);
            } else if (NimbusStyle.SMALL_KEY.equals(str)) {
                this.tileWidth = (int) (this.tileWidth * 0.857d);
            } else if (NimbusStyle.MINI_KEY.equals(str)) {
                this.tileWidth = (int) (this.tileWidth * 0.784d);
            }
        }
        this.minBarSize = (Dimension) this.style.get(context, "ProgressBar.minBarSize");
        this.glowWidth = this.style.getInt(context, "ProgressBar.glowWidth", 0);
        context.dispose();
    }

    @Override // javax.swing.plaf.basic.BasicProgressBarUI
    protected void uninstallDefaults() {
        SynthContext context = getContext(this.progressBar, 1);
        this.style.uninstallDefaults(context);
        context.dispose();
        this.style = null;
    }

    @Override // javax.swing.plaf.synth.SynthUI
    public SynthContext getContext(JComponent jComponent) {
        return getContext(jComponent, getComponentState(jComponent));
    }

    private SynthContext getContext(JComponent jComponent, int i2) {
        return SynthContext.getContext(jComponent, this.style, i2);
    }

    private int getComponentState(JComponent jComponent) {
        return SynthLookAndFeel.getComponentState(jComponent);
    }

    @Override // javax.swing.plaf.basic.BasicProgressBarUI, javax.swing.plaf.ComponentUI
    public int getBaseline(JComponent jComponent, int i2, int i3) {
        super.getBaseline(jComponent, i2, i3);
        if (this.progressBar.isStringPainted() && this.progressBar.getOrientation() == 0) {
            SynthContext context = getContext(jComponent);
            FontMetrics fontMetrics = this.progressBar.getFontMetrics(context.getStyle().getFont(context));
            context.dispose();
            return (((i3 - fontMetrics.getAscent()) - fontMetrics.getDescent()) / 2) + fontMetrics.getAscent();
        }
        return -1;
    }

    @Override // javax.swing.plaf.basic.BasicProgressBarUI
    protected Rectangle getBox(Rectangle rectangle) {
        if (this.tileWhenIndeterminate) {
            return SwingUtilities.calculateInnerArea(this.progressBar, rectangle);
        }
        return super.getBox(rectangle);
    }

    @Override // javax.swing.plaf.basic.BasicProgressBarUI
    protected void setAnimationIndex(int i2) {
        if (this.paintOutsideClip) {
            if (getAnimationIndex() == i2) {
                return;
            }
            super.setAnimationIndex(i2);
            this.progressBar.repaint();
            return;
        }
        super.setAnimationIndex(i2);
    }

    @Override // javax.swing.plaf.ComponentUI
    public void update(Graphics graphics, JComponent jComponent) {
        SynthContext context = getContext(jComponent);
        SynthLookAndFeel.update(context, graphics);
        context.getPainter().paintProgressBarBackground(context, graphics, 0, 0, jComponent.getWidth(), jComponent.getHeight(), this.progressBar.getOrientation());
        paint(context, graphics);
        context.dispose();
    }

    @Override // javax.swing.plaf.basic.BasicProgressBarUI, javax.swing.plaf.ComponentUI
    public void paint(Graphics graphics, JComponent jComponent) {
        SynthContext context = getContext(jComponent);
        paint(context, graphics);
        context.dispose();
    }

    protected void paint(SynthContext synthContext, Graphics graphics) {
        JProgressBar jProgressBar = (JProgressBar) synthContext.getComponent();
        int width = 0;
        int height = 0;
        int width2 = 0;
        int height2 = 0;
        if (!jProgressBar.isIndeterminate()) {
            Insets insets = jProgressBar.getInsets();
            double percentComplete = jProgressBar.getPercentComplete();
            if (percentComplete != 0.0d) {
                if (jProgressBar.getOrientation() == 0) {
                    width = insets.left + this.progressPadding;
                    height = insets.top + this.progressPadding;
                    width2 = (int) (percentComplete * (jProgressBar.getWidth() - (((insets.left + this.progressPadding) + insets.right) + this.progressPadding)));
                    height2 = jProgressBar.getHeight() - (((insets.top + this.progressPadding) + insets.bottom) + this.progressPadding);
                    if (!SynthLookAndFeel.isLeftToRight(jProgressBar)) {
                        width = (((jProgressBar.getWidth() - insets.right) - width2) - this.progressPadding) - this.glowWidth;
                    }
                } else {
                    width = insets.left + this.progressPadding;
                    width2 = jProgressBar.getWidth() - (((insets.left + this.progressPadding) + insets.right) + this.progressPadding);
                    height2 = (int) (percentComplete * (jProgressBar.getHeight() - (((insets.top + this.progressPadding) + insets.bottom) + this.progressPadding)));
                    height = ((jProgressBar.getHeight() - insets.bottom) - height2) - this.progressPadding;
                    if (SynthLookAndFeel.isLeftToRight(jProgressBar)) {
                        height -= this.glowWidth;
                    }
                }
            }
        } else {
            this.boxRect = getBox(this.boxRect);
            width = this.boxRect.f12372x + this.progressPadding;
            height = this.boxRect.f12373y + this.progressPadding;
            width2 = (this.boxRect.width - this.progressPadding) - this.progressPadding;
            height2 = (this.boxRect.height - this.progressPadding) - this.progressPadding;
        }
        if (this.tileWhenIndeterminate && jProgressBar.isIndeterminate()) {
            int animationIndex = (int) ((getAnimationIndex() / getFrameCount()) * this.tileWidth);
            Shape clip = graphics.getClip();
            graphics.clipRect(width, height, width2, height2);
            if (jProgressBar.getOrientation() == 0) {
                int i2 = width - this.tileWidth;
                int i3 = animationIndex;
                while (true) {
                    int i4 = i2 + i3;
                    if (i4 > width2) {
                        break;
                    }
                    synthContext.getPainter().paintProgressBarForeground(synthContext, graphics, i4, height, this.tileWidth, height2, jProgressBar.getOrientation());
                    i2 = i4;
                    i3 = this.tileWidth;
                }
            } else {
                int i5 = height - animationIndex;
                while (true) {
                    int i6 = i5;
                    if (i6 >= height2 + this.tileWidth) {
                        break;
                    }
                    synthContext.getPainter().paintProgressBarForeground(synthContext, graphics, width, i6, width2, this.tileWidth, jProgressBar.getOrientation());
                    i5 = i6 + this.tileWidth;
                }
            }
            graphics.setClip(clip);
        } else if (this.minBarSize == null || (width2 >= this.minBarSize.width && height2 >= this.minBarSize.height)) {
            synthContext.getPainter().paintProgressBarForeground(synthContext, graphics, width, height, width2, height2, jProgressBar.getOrientation());
        }
        if (jProgressBar.isStringPainted()) {
            paintText(synthContext, graphics, jProgressBar.getString());
        }
    }

    protected void paintText(SynthContext synthContext, Graphics graphics, String str) {
        AffineTransform rotateInstance;
        Point point;
        if (this.progressBar.isStringPainted()) {
            SynthStyle style = synthContext.getStyle();
            Font font = style.getFont(synthContext);
            FontMetrics fontMetrics = SwingUtilities2.getFontMetrics(this.progressBar, graphics, font);
            int iComputeStringWidth = style.getGraphicsUtils(synthContext).computeStringWidth(synthContext, font, fontMetrics, str);
            Rectangle bounds = this.progressBar.getBounds();
            if (this.rotateText && this.progressBar.getOrientation() == 1) {
                Graphics2D graphics2D = (Graphics2D) graphics;
                if (this.progressBar.getComponentOrientation().isLeftToRight()) {
                    rotateInstance = AffineTransform.getRotateInstance(-1.5707963267948966d);
                    point = new Point(((bounds.width + fontMetrics.getAscent()) - fontMetrics.getDescent()) / 2, (bounds.height + iComputeStringWidth) / 2);
                } else {
                    rotateInstance = AffineTransform.getRotateInstance(1.5707963267948966d);
                    point = new Point(((bounds.width - fontMetrics.getAscent()) + fontMetrics.getDescent()) / 2, (bounds.height - iComputeStringWidth) / 2);
                }
                if (point.f12370x < 0) {
                    return;
                }
                graphics2D.setFont(font.deriveFont(rotateInstance));
                graphics2D.setColor(style.getColor(synthContext, ColorType.TEXT_FOREGROUND));
                style.getGraphicsUtils(synthContext).paintText(synthContext, graphics, str, point.f12370x, point.f12371y, -1);
                return;
            }
            Rectangle rectangle = new Rectangle((bounds.width / 2) - (iComputeStringWidth / 2), (bounds.height - (fontMetrics.getAscent() + fontMetrics.getDescent())) / 2, 0, 0);
            if (rectangle.f12373y < 0) {
                return;
            }
            graphics.setColor(style.getColor(synthContext, ColorType.TEXT_FOREGROUND));
            graphics.setFont(font);
            style.getGraphicsUtils(synthContext).paintText(synthContext, graphics, str, rectangle.f12372x, rectangle.f12373y, -1);
        }
    }

    @Override // javax.swing.plaf.synth.SynthUI
    public void paintBorder(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
        synthContext.getPainter().paintProgressBarBorder(synthContext, graphics, i2, i3, i4, i5, this.progressBar.getOrientation());
    }

    @Override // java.beans.PropertyChangeListener
    public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
        if (SynthLookAndFeel.shouldUpdateStyle(propertyChangeEvent) || "indeterminate".equals(propertyChangeEvent.getPropertyName())) {
            updateStyle((JProgressBar) propertyChangeEvent.getSource());
        }
    }

    @Override // javax.swing.plaf.basic.BasicProgressBarUI, javax.swing.plaf.ComponentUI
    public Dimension getPreferredSize(JComponent jComponent) {
        Dimension dimension;
        Insets insets = this.progressBar.getInsets();
        FontMetrics fontMetrics = this.progressBar.getFontMetrics(this.progressBar.getFont());
        String string = this.progressBar.getString();
        int height = fontMetrics.getHeight() + fontMetrics.getDescent();
        if (this.progressBar.getOrientation() == 0) {
            dimension = new Dimension(getPreferredInnerHorizontal());
            if (this.progressBar.isStringPainted()) {
                if (height > dimension.height) {
                    dimension.height = height;
                }
                int iStringWidth = SwingUtilities2.stringWidth(this.progressBar, fontMetrics, string);
                if (iStringWidth > dimension.width) {
                    dimension.width = iStringWidth;
                }
            }
        } else {
            dimension = new Dimension(getPreferredInnerVertical());
            if (this.progressBar.isStringPainted()) {
                if (height > dimension.width) {
                    dimension.width = height;
                }
                int iStringWidth2 = SwingUtilities2.stringWidth(this.progressBar, fontMetrics, string);
                if (iStringWidth2 > dimension.height) {
                    dimension.height = iStringWidth2;
                }
            }
        }
        String str = (String) this.progressBar.getClientProperty("JComponent.sizeVariant");
        if (str != null) {
            if (NimbusStyle.LARGE_KEY.equals(str)) {
                dimension.width = (int) (r0.width * 1.15f);
                dimension.height = (int) (r0.height * 1.15f);
            } else if (NimbusStyle.SMALL_KEY.equals(str)) {
                dimension.width = (int) (r0.width * 0.9f);
                dimension.height = (int) (r0.height * 0.9f);
            } else if (NimbusStyle.MINI_KEY.equals(str)) {
                dimension.width = (int) (r0.width * 0.784f);
                dimension.height = (int) (r0.height * 0.784f);
            }
        }
        dimension.width += insets.left + insets.right;
        dimension.height += insets.top + insets.bottom;
        return dimension;
    }
}
