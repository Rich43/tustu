package javax.swing.plaf.metal;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JSlider;
import javax.swing.UIManager;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicSliderUI;

/* loaded from: rt.jar:javax/swing/plaf/metal/MetalSliderUI.class */
public class MetalSliderUI extends BasicSliderUI {
    protected final int TICK_BUFFER = 4;
    protected boolean filledSlider;
    protected static Color thumbColor;
    protected static Color highlightColor;
    protected static Color darkShadowColor;
    protected static int trackWidth;
    protected static int tickLength;
    private int safeLength;
    protected static Icon horizThumbIcon;
    protected static Icon vertThumbIcon;
    private static Icon SAFE_HORIZ_THUMB_ICON;
    private static Icon SAFE_VERT_THUMB_ICON;
    protected final String SLIDER_FILL = "JSlider.isFilled";

    public static ComponentUI createUI(JComponent jComponent) {
        return new MetalSliderUI();
    }

    public MetalSliderUI() {
        super(null);
        this.TICK_BUFFER = 4;
        this.filledSlider = false;
        this.SLIDER_FILL = "JSlider.isFilled";
    }

    private static Icon getHorizThumbIcon() {
        if (System.getSecurityManager() != null) {
            return SAFE_HORIZ_THUMB_ICON;
        }
        return horizThumbIcon;
    }

    private static Icon getVertThumbIcon() {
        if (System.getSecurityManager() != null) {
            return SAFE_VERT_THUMB_ICON;
        }
        return vertThumbIcon;
    }

    @Override // javax.swing.plaf.basic.BasicSliderUI, javax.swing.plaf.ComponentUI
    public void installUI(JComponent jComponent) {
        trackWidth = ((Integer) UIManager.get("Slider.trackWidth")).intValue();
        int iIntValue = ((Integer) UIManager.get("Slider.majorTickLength")).intValue();
        this.safeLength = iIntValue;
        tickLength = iIntValue;
        Icon icon = UIManager.getIcon("Slider.horizontalThumbIcon");
        SAFE_HORIZ_THUMB_ICON = icon;
        horizThumbIcon = icon;
        Icon icon2 = UIManager.getIcon("Slider.verticalThumbIcon");
        SAFE_VERT_THUMB_ICON = icon2;
        vertThumbIcon = icon2;
        super.installUI(jComponent);
        thumbColor = UIManager.getColor("Slider.thumb");
        highlightColor = UIManager.getColor("Slider.highlight");
        darkShadowColor = UIManager.getColor("Slider.darkShadow");
        this.scrollListener.setScrollByBlock(false);
        prepareFilledSliderField();
    }

    @Override // javax.swing.plaf.basic.BasicSliderUI
    protected PropertyChangeListener createPropertyChangeListener(JSlider jSlider) {
        return new MetalPropertyListener();
    }

    /* loaded from: rt.jar:javax/swing/plaf/metal/MetalSliderUI$MetalPropertyListener.class */
    protected class MetalPropertyListener extends BasicSliderUI.PropertyChangeHandler {
        protected MetalPropertyListener() {
            super();
        }

        @Override // javax.swing.plaf.basic.BasicSliderUI.PropertyChangeHandler, java.beans.PropertyChangeListener
        public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
            super.propertyChange(propertyChangeEvent);
            if (propertyChangeEvent.getPropertyName().equals("JSlider.isFilled")) {
                MetalSliderUI.this.prepareFilledSliderField();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void prepareFilledSliderField() {
        this.filledSlider = MetalLookAndFeel.usingOcean();
        Object clientProperty = this.slider.getClientProperty("JSlider.isFilled");
        if (clientProperty != null) {
            this.filledSlider = ((Boolean) clientProperty).booleanValue();
        }
    }

    @Override // javax.swing.plaf.basic.BasicSliderUI
    public void paintThumb(Graphics graphics) {
        Rectangle rectangle = this.thumbRect;
        graphics.translate(rectangle.f12372x, rectangle.f12373y);
        if (this.slider.getOrientation() == 0) {
            getHorizThumbIcon().paintIcon(this.slider, graphics, 0, 0);
        } else {
            getVertThumbIcon().paintIcon(this.slider, graphics, 0, 0);
        }
        graphics.translate(-rectangle.f12372x, -rectangle.f12373y);
    }

    private Rectangle getPaintTrackRect() {
        int thumbOverhang;
        int thumbOverhang2;
        int thumbOverhang3 = 0;
        int trackWidth2 = 0;
        if (this.slider.getOrientation() == 0) {
            thumbOverhang2 = (this.trackRect.height - 1) - getThumbOverhang();
            trackWidth2 = thumbOverhang2 - (getTrackWidth() - 1);
            thumbOverhang = this.trackRect.width - 1;
        } else {
            if (MetalUtils.isLeftToRight(this.slider)) {
                thumbOverhang3 = (this.trackRect.width - getThumbOverhang()) - getTrackWidth();
                thumbOverhang = (this.trackRect.width - getThumbOverhang()) - 1;
            } else {
                thumbOverhang3 = getThumbOverhang();
                thumbOverhang = (getThumbOverhang() + getTrackWidth()) - 1;
            }
            thumbOverhang2 = this.trackRect.height - 1;
        }
        return new Rectangle(this.trackRect.f12372x + thumbOverhang3, this.trackRect.f12373y + trackWidth2, thumbOverhang - thumbOverhang3, thumbOverhang2 - trackWidth2);
    }

    @Override // javax.swing.plaf.basic.BasicSliderUI
    public void paintTrack(Graphics graphics) {
        int thumbOverhang;
        int thumbOverhang2;
        int i2;
        int i3;
        int i4;
        int i5;
        if (MetalLookAndFeel.usingOcean()) {
            oceanPaintTrack(graphics);
            return;
        }
        Color controlShadow = !this.slider.isEnabled() ? MetalLookAndFeel.getControlShadow() : this.slider.getForeground();
        boolean zIsLeftToRight = MetalUtils.isLeftToRight(this.slider);
        graphics.translate(this.trackRect.f12372x, this.trackRect.f12373y);
        int thumbOverhang3 = 0;
        int trackWidth2 = 0;
        if (this.slider.getOrientation() == 0) {
            thumbOverhang2 = (this.trackRect.height - 1) - getThumbOverhang();
            trackWidth2 = thumbOverhang2 - (getTrackWidth() - 1);
            thumbOverhang = this.trackRect.width - 1;
        } else {
            if (zIsLeftToRight) {
                thumbOverhang3 = (this.trackRect.width - getThumbOverhang()) - getTrackWidth();
                thumbOverhang = (this.trackRect.width - getThumbOverhang()) - 1;
            } else {
                thumbOverhang3 = getThumbOverhang();
                thumbOverhang = (getThumbOverhang() + getTrackWidth()) - 1;
            }
            thumbOverhang2 = this.trackRect.height - 1;
        }
        if (this.slider.isEnabled()) {
            graphics.setColor(MetalLookAndFeel.getControlDarkShadow());
            graphics.drawRect(thumbOverhang3, trackWidth2, (thumbOverhang - thumbOverhang3) - 1, (thumbOverhang2 - trackWidth2) - 1);
            graphics.setColor(MetalLookAndFeel.getControlHighlight());
            graphics.drawLine(thumbOverhang3 + 1, thumbOverhang2, thumbOverhang, thumbOverhang2);
            graphics.drawLine(thumbOverhang, trackWidth2 + 1, thumbOverhang, thumbOverhang2);
            graphics.setColor(MetalLookAndFeel.getControlShadow());
            graphics.drawLine(thumbOverhang3 + 1, trackWidth2 + 1, thumbOverhang - 2, trackWidth2 + 1);
            graphics.drawLine(thumbOverhang3 + 1, trackWidth2 + 1, thumbOverhang3 + 1, thumbOverhang2 - 2);
        } else {
            graphics.setColor(MetalLookAndFeel.getControlShadow());
            graphics.drawRect(thumbOverhang3, trackWidth2, (thumbOverhang - thumbOverhang3) - 1, (thumbOverhang2 - trackWidth2) - 1);
        }
        if (this.filledSlider) {
            if (this.slider.getOrientation() == 0) {
                int i6 = (this.thumbRect.f12372x + (this.thumbRect.width / 2)) - this.trackRect.f12372x;
                i4 = !this.slider.isEnabled() ? trackWidth2 : trackWidth2 + 1;
                i5 = !this.slider.isEnabled() ? thumbOverhang2 - 1 : thumbOverhang2 - 2;
                if (!drawInverted()) {
                    i2 = !this.slider.isEnabled() ? thumbOverhang3 : thumbOverhang3 + 1;
                    i3 = i6;
                } else {
                    i2 = i6;
                    i3 = !this.slider.isEnabled() ? thumbOverhang - 1 : thumbOverhang - 2;
                }
            } else {
                int i7 = (this.thumbRect.f12373y + (this.thumbRect.height / 2)) - this.trackRect.f12373y;
                i2 = !this.slider.isEnabled() ? thumbOverhang3 : thumbOverhang3 + 1;
                i3 = !this.slider.isEnabled() ? thumbOverhang - 1 : thumbOverhang - 2;
                if (!drawInverted()) {
                    i4 = i7;
                    i5 = !this.slider.isEnabled() ? thumbOverhang2 - 1 : thumbOverhang2 - 2;
                } else {
                    i4 = !this.slider.isEnabled() ? trackWidth2 : trackWidth2 + 1;
                    i5 = i7;
                }
            }
            if (this.slider.isEnabled()) {
                graphics.setColor(this.slider.getBackground());
                graphics.drawLine(i2, i4, i3, i4);
                graphics.drawLine(i2, i4, i2, i5);
                graphics.setColor(MetalLookAndFeel.getControlShadow());
                graphics.fillRect(i2 + 1, i4 + 1, i3 - i2, i5 - i4);
            } else {
                graphics.setColor(MetalLookAndFeel.getControlShadow());
                graphics.fillRect(i2, i4, i3 - i2, i5 - i4);
            }
        }
        graphics.translate(-this.trackRect.f12372x, -this.trackRect.f12373y);
    }

    private void oceanPaintTrack(Graphics graphics) {
        int i2;
        int i3;
        int i4;
        int i5;
        boolean zIsLeftToRight = MetalUtils.isLeftToRight(this.slider);
        boolean zDrawInverted = drawInverted();
        Color color = (Color) UIManager.get("Slider.altTrackColor");
        Rectangle paintTrackRect = getPaintTrackRect();
        graphics.translate(paintTrackRect.f12372x, paintTrackRect.f12373y);
        int i6 = paintTrackRect.width;
        int i7 = paintTrackRect.height;
        if (this.slider.getOrientation() == 0) {
            int i8 = (this.thumbRect.f12372x + (this.thumbRect.width / 2)) - paintTrackRect.f12372x;
            if (this.slider.isEnabled()) {
                if (i8 > 0) {
                    graphics.setColor(zDrawInverted ? MetalLookAndFeel.getControlDarkShadow() : MetalLookAndFeel.getPrimaryControlDarkShadow());
                    graphics.drawRect(0, 0, i8 - 1, i7 - 1);
                }
                if (i8 < i6) {
                    graphics.setColor(zDrawInverted ? MetalLookAndFeel.getPrimaryControlDarkShadow() : MetalLookAndFeel.getControlDarkShadow());
                    graphics.drawRect(i8, 0, (i6 - i8) - 1, i7 - 1);
                }
                if (this.filledSlider) {
                    graphics.setColor(MetalLookAndFeel.getPrimaryControlShadow());
                    if (zDrawInverted) {
                        i4 = i8;
                        i5 = i6 - 2;
                        graphics.drawLine(1, 1, i8, 1);
                    } else {
                        i4 = 1;
                        i5 = i8;
                        graphics.drawLine(i8, 1, i6 - 1, 1);
                    }
                    if (i7 == 6) {
                        graphics.setColor(MetalLookAndFeel.getWhite());
                        graphics.drawLine(i4, 1, i5, 1);
                        graphics.setColor(color);
                        graphics.drawLine(i4, 2, i5, 2);
                        graphics.setColor(MetalLookAndFeel.getControlShadow());
                        graphics.drawLine(i4, 3, i5, 3);
                        graphics.setColor(MetalLookAndFeel.getPrimaryControlShadow());
                        graphics.drawLine(i4, 4, i5, 4);
                    }
                }
            } else {
                graphics.setColor(MetalLookAndFeel.getControlShadow());
                if (i8 > 0) {
                    if (!zDrawInverted && this.filledSlider) {
                        graphics.fillRect(0, 0, i8 - 1, i7 - 1);
                    } else {
                        graphics.drawRect(0, 0, i8 - 1, i7 - 1);
                    }
                }
                if (i8 < i6) {
                    if (zDrawInverted && this.filledSlider) {
                        graphics.fillRect(i8, 0, (i6 - i8) - 1, i7 - 1);
                    } else {
                        graphics.drawRect(i8, 0, (i6 - i8) - 1, i7 - 1);
                    }
                }
            }
        } else {
            int i9 = (this.thumbRect.f12373y + (this.thumbRect.height / 2)) - paintTrackRect.f12373y;
            if (this.slider.isEnabled()) {
                if (i9 > 0) {
                    graphics.setColor(zDrawInverted ? MetalLookAndFeel.getPrimaryControlDarkShadow() : MetalLookAndFeel.getControlDarkShadow());
                    graphics.drawRect(0, 0, i6 - 1, i9 - 1);
                }
                if (i9 < i7) {
                    graphics.setColor(zDrawInverted ? MetalLookAndFeel.getControlDarkShadow() : MetalLookAndFeel.getPrimaryControlDarkShadow());
                    graphics.drawRect(0, i9, i6 - 1, (i7 - i9) - 1);
                }
                if (this.filledSlider) {
                    graphics.setColor(MetalLookAndFeel.getPrimaryControlShadow());
                    if (drawInverted()) {
                        i2 = 1;
                        i3 = i9;
                        if (zIsLeftToRight) {
                            graphics.drawLine(1, i9, 1, i7 - 1);
                        } else {
                            graphics.drawLine(i6 - 2, i9, i6 - 2, i7 - 1);
                        }
                    } else {
                        i2 = i9;
                        i3 = i7 - 2;
                        if (zIsLeftToRight) {
                            graphics.drawLine(1, 1, 1, i9);
                        } else {
                            graphics.drawLine(i6 - 2, 1, i6 - 2, i9);
                        }
                    }
                    if (i6 == 6) {
                        graphics.setColor(zIsLeftToRight ? MetalLookAndFeel.getWhite() : MetalLookAndFeel.getPrimaryControlShadow());
                        graphics.drawLine(1, i2, 1, i3);
                        graphics.setColor(zIsLeftToRight ? color : MetalLookAndFeel.getControlShadow());
                        graphics.drawLine(2, i2, 2, i3);
                        graphics.setColor(zIsLeftToRight ? MetalLookAndFeel.getControlShadow() : color);
                        graphics.drawLine(3, i2, 3, i3);
                        graphics.setColor(zIsLeftToRight ? MetalLookAndFeel.getPrimaryControlShadow() : MetalLookAndFeel.getWhite());
                        graphics.drawLine(4, i2, 4, i3);
                    }
                }
            } else {
                graphics.setColor(MetalLookAndFeel.getControlShadow());
                if (i9 > 0) {
                    if (zDrawInverted && this.filledSlider) {
                        graphics.fillRect(0, 0, i6 - 1, i9 - 1);
                    } else {
                        graphics.drawRect(0, 0, i6 - 1, i9 - 1);
                    }
                }
                if (i9 < i7) {
                    if (!zDrawInverted && this.filledSlider) {
                        graphics.fillRect(0, i9, i6 - 1, (i7 - i9) - 1);
                    } else {
                        graphics.drawRect(0, i9, i6 - 1, (i7 - i9) - 1);
                    }
                }
            }
        }
        graphics.translate(-paintTrackRect.f12372x, -paintTrackRect.f12373y);
    }

    @Override // javax.swing.plaf.basic.BasicSliderUI
    public void paintFocus(Graphics graphics) {
    }

    @Override // javax.swing.plaf.basic.BasicSliderUI
    protected Dimension getThumbSize() {
        Dimension dimension = new Dimension();
        if (this.slider.getOrientation() == 1) {
            dimension.width = getVertThumbIcon().getIconWidth();
            dimension.height = getVertThumbIcon().getIconHeight();
        } else {
            dimension.width = getHorizThumbIcon().getIconWidth();
            dimension.height = getHorizThumbIcon().getIconHeight();
        }
        return dimension;
    }

    @Override // javax.swing.plaf.basic.BasicSliderUI
    public int getTickLength() {
        return this.slider.getOrientation() == 0 ? this.safeLength + 4 + 1 : this.safeLength + 4 + 3;
    }

    protected int getTrackWidth() {
        if (this.slider.getOrientation() == 0) {
            return (int) (0.4375d * this.thumbRect.height);
        }
        return (int) (0.4375d * this.thumbRect.width);
    }

    protected int getTrackLength() {
        if (this.slider.getOrientation() == 0) {
            return this.trackRect.width;
        }
        return this.trackRect.height;
    }

    protected int getThumbOverhang() {
        return ((int) (getThumbSize().getHeight() - getTrackWidth())) / 2;
    }

    @Override // javax.swing.plaf.basic.BasicSliderUI
    protected void scrollDueToClickInTrack(int i2) {
        scrollByUnit(i2);
    }

    @Override // javax.swing.plaf.basic.BasicSliderUI
    protected void paintMinorTickForHorizSlider(Graphics graphics, Rectangle rectangle, int i2) {
        graphics.setColor(this.slider.isEnabled() ? this.slider.getForeground() : MetalLookAndFeel.getControlShadow());
        graphics.drawLine(i2, 4, i2, 4 + (this.safeLength / 2));
    }

    @Override // javax.swing.plaf.basic.BasicSliderUI
    protected void paintMajorTickForHorizSlider(Graphics graphics, Rectangle rectangle, int i2) {
        graphics.setColor(this.slider.isEnabled() ? this.slider.getForeground() : MetalLookAndFeel.getControlShadow());
        graphics.drawLine(i2, 4, i2, 4 + (this.safeLength - 1));
    }

    @Override // javax.swing.plaf.basic.BasicSliderUI
    protected void paintMinorTickForVertSlider(Graphics graphics, Rectangle rectangle, int i2) {
        graphics.setColor(this.slider.isEnabled() ? this.slider.getForeground() : MetalLookAndFeel.getControlShadow());
        if (MetalUtils.isLeftToRight(this.slider)) {
            graphics.drawLine(4, i2, 4 + (this.safeLength / 2), i2);
        } else {
            graphics.drawLine(0, i2, this.safeLength / 2, i2);
        }
    }

    @Override // javax.swing.plaf.basic.BasicSliderUI
    protected void paintMajorTickForVertSlider(Graphics graphics, Rectangle rectangle, int i2) {
        graphics.setColor(this.slider.isEnabled() ? this.slider.getForeground() : MetalLookAndFeel.getControlShadow());
        if (MetalUtils.isLeftToRight(this.slider)) {
            graphics.drawLine(4, i2, 4 + this.safeLength, i2);
        } else {
            graphics.drawLine(0, i2, this.safeLength, i2);
        }
    }
}
