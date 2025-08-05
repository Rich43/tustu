package javax.swing.plaf.metal;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.UIManager;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicScrollBarUI;
import sun.swing.SwingUtilities2;

/* loaded from: rt.jar:javax/swing/plaf/metal/MetalScrollBarUI.class */
public class MetalScrollBarUI extends BasicScrollBarUI {
    private static Color shadowColor;
    private static Color highlightColor;
    private static Color darkShadowColor;
    private static Color thumbColor;
    private static Color thumbShadow;
    private static Color thumbHighlightColor;
    protected MetalBumps bumps;
    protected MetalScrollButton increaseButton;
    protected MetalScrollButton decreaseButton;
    protected int scrollBarWidth;
    public static final String FREE_STANDING_PROP = "JScrollBar.isFreeStanding";
    protected boolean isFreeStanding = true;

    public static ComponentUI createUI(JComponent jComponent) {
        return new MetalScrollBarUI();
    }

    @Override // javax.swing.plaf.basic.BasicScrollBarUI
    protected void installDefaults() {
        this.scrollBarWidth = ((Integer) UIManager.get("ScrollBar.width")).intValue();
        super.installDefaults();
        this.bumps = new MetalBumps(10, 10, thumbHighlightColor, thumbShadow, thumbColor);
    }

    @Override // javax.swing.plaf.basic.BasicScrollBarUI
    protected void installListeners() {
        super.installListeners();
        ((ScrollBarListener) this.propertyChangeListener).handlePropertyChange(this.scrollbar.getClientProperty(FREE_STANDING_PROP));
    }

    @Override // javax.swing.plaf.basic.BasicScrollBarUI
    protected PropertyChangeListener createPropertyChangeListener() {
        return new ScrollBarListener();
    }

    @Override // javax.swing.plaf.basic.BasicScrollBarUI
    protected void configureScrollBarColors() {
        super.configureScrollBarColors();
        shadowColor = UIManager.getColor("ScrollBar.shadow");
        highlightColor = UIManager.getColor("ScrollBar.highlight");
        darkShadowColor = UIManager.getColor("ScrollBar.darkShadow");
        thumbColor = UIManager.getColor("ScrollBar.thumb");
        thumbShadow = UIManager.getColor("ScrollBar.thumbShadow");
        thumbHighlightColor = UIManager.getColor("ScrollBar.thumbHighlight");
    }

    @Override // javax.swing.plaf.basic.BasicScrollBarUI, javax.swing.plaf.ComponentUI
    public Dimension getPreferredSize(JComponent jComponent) {
        if (this.scrollbar.getOrientation() == 1) {
            return new Dimension(this.scrollBarWidth, (this.scrollBarWidth * 3) + 10);
        }
        return new Dimension((this.scrollBarWidth * 3) + 10, this.scrollBarWidth);
    }

    @Override // javax.swing.plaf.basic.BasicScrollBarUI
    protected JButton createDecreaseButton(int i2) {
        this.decreaseButton = new MetalScrollButton(i2, this.scrollBarWidth, this.isFreeStanding);
        return this.decreaseButton;
    }

    @Override // javax.swing.plaf.basic.BasicScrollBarUI
    protected JButton createIncreaseButton(int i2) {
        this.increaseButton = new MetalScrollButton(i2, this.scrollBarWidth, this.isFreeStanding);
        return this.increaseButton;
    }

    @Override // javax.swing.plaf.basic.BasicScrollBarUI
    protected void paintTrack(Graphics graphics, JComponent jComponent, Rectangle rectangle) {
        graphics.translate(rectangle.f12372x, rectangle.f12373y);
        boolean zIsLeftToRight = MetalUtils.isLeftToRight(jComponent);
        if (this.scrollbar.getOrientation() == 1) {
            if (!this.isFreeStanding) {
                rectangle.width += 2;
                if (!zIsLeftToRight) {
                    graphics.translate(-1, 0);
                }
            }
            if (jComponent.isEnabled()) {
                graphics.setColor(darkShadowColor);
                SwingUtilities2.drawVLine(graphics, 0, 0, rectangle.height - 1);
                SwingUtilities2.drawVLine(graphics, rectangle.width - 2, 0, rectangle.height - 1);
                SwingUtilities2.drawHLine(graphics, 2, rectangle.width - 1, rectangle.height - 1);
                SwingUtilities2.drawHLine(graphics, 2, rectangle.width - 2, 0);
                graphics.setColor(shadowColor);
                SwingUtilities2.drawVLine(graphics, 1, 1, rectangle.height - 2);
                SwingUtilities2.drawHLine(graphics, 1, rectangle.width - 3, 1);
                if (this.scrollbar.getValue() != this.scrollbar.getMaximum()) {
                    SwingUtilities2.drawHLine(graphics, 1, rectangle.width - 1, (this.thumbRect.f12373y + this.thumbRect.height) - rectangle.f12373y);
                }
                graphics.setColor(highlightColor);
                SwingUtilities2.drawVLine(graphics, rectangle.width - 1, 0, rectangle.height - 1);
            } else {
                MetalUtils.drawDisabledBorder(graphics, 0, 0, rectangle.width, rectangle.height);
            }
            if (!this.isFreeStanding) {
                rectangle.width -= 2;
                if (!zIsLeftToRight) {
                    graphics.translate(1, 0);
                }
            }
        } else {
            if (!this.isFreeStanding) {
                rectangle.height += 2;
            }
            if (jComponent.isEnabled()) {
                graphics.setColor(darkShadowColor);
                SwingUtilities2.drawHLine(graphics, 0, rectangle.width - 1, 0);
                SwingUtilities2.drawVLine(graphics, 0, 2, rectangle.height - 2);
                SwingUtilities2.drawHLine(graphics, 0, rectangle.width - 1, rectangle.height - 2);
                SwingUtilities2.drawVLine(graphics, rectangle.width - 1, 2, rectangle.height - 1);
                graphics.setColor(shadowColor);
                SwingUtilities2.drawHLine(graphics, 1, rectangle.width - 2, 1);
                SwingUtilities2.drawVLine(graphics, 1, 1, rectangle.height - 3);
                SwingUtilities2.drawHLine(graphics, 0, rectangle.width - 1, rectangle.height - 1);
                if (this.scrollbar.getValue() != this.scrollbar.getMaximum()) {
                    SwingUtilities2.drawVLine(graphics, (this.thumbRect.f12372x + this.thumbRect.width) - rectangle.f12372x, 1, rectangle.height - 1);
                }
            } else {
                MetalUtils.drawDisabledBorder(graphics, 0, 0, rectangle.width, rectangle.height);
            }
            if (!this.isFreeStanding) {
                rectangle.height -= 2;
            }
        }
        graphics.translate(-rectangle.f12372x, -rectangle.f12373y);
    }

    @Override // javax.swing.plaf.basic.BasicScrollBarUI
    protected void paintThumb(Graphics graphics, JComponent jComponent, Rectangle rectangle) {
        if (!jComponent.isEnabled()) {
            return;
        }
        if (MetalLookAndFeel.usingOcean()) {
            oceanPaintThumb(graphics, jComponent, rectangle);
            return;
        }
        boolean zIsLeftToRight = MetalUtils.isLeftToRight(jComponent);
        graphics.translate(rectangle.f12372x, rectangle.f12373y);
        if (this.scrollbar.getOrientation() == 1) {
            if (!this.isFreeStanding) {
                rectangle.width += 2;
                if (!zIsLeftToRight) {
                    graphics.translate(-1, 0);
                }
            }
            graphics.setColor(thumbColor);
            graphics.fillRect(0, 0, rectangle.width - 2, rectangle.height - 1);
            graphics.setColor(thumbShadow);
            SwingUtilities2.drawRect(graphics, 0, 0, rectangle.width - 2, rectangle.height - 1);
            graphics.setColor(thumbHighlightColor);
            SwingUtilities2.drawHLine(graphics, 1, rectangle.width - 3, 1);
            SwingUtilities2.drawVLine(graphics, 1, 1, rectangle.height - 2);
            this.bumps.setBumpArea(rectangle.width - 6, rectangle.height - 7);
            this.bumps.paintIcon(jComponent, graphics, 3, 4);
            if (!this.isFreeStanding) {
                rectangle.width -= 2;
                if (!zIsLeftToRight) {
                    graphics.translate(1, 0);
                }
            }
        } else {
            if (!this.isFreeStanding) {
                rectangle.height += 2;
            }
            graphics.setColor(thumbColor);
            graphics.fillRect(0, 0, rectangle.width - 1, rectangle.height - 2);
            graphics.setColor(thumbShadow);
            SwingUtilities2.drawRect(graphics, 0, 0, rectangle.width - 1, rectangle.height - 2);
            graphics.setColor(thumbHighlightColor);
            SwingUtilities2.drawHLine(graphics, 1, rectangle.width - 3, 1);
            SwingUtilities2.drawVLine(graphics, 1, 1, rectangle.height - 3);
            this.bumps.setBumpArea(rectangle.width - 7, rectangle.height - 6);
            this.bumps.paintIcon(jComponent, graphics, 4, 3);
            if (!this.isFreeStanding) {
                rectangle.height -= 2;
            }
        }
        graphics.translate(-rectangle.f12372x, -rectangle.f12373y);
    }

    private void oceanPaintThumb(Graphics graphics, JComponent jComponent, Rectangle rectangle) {
        boolean zIsLeftToRight = MetalUtils.isLeftToRight(jComponent);
        graphics.translate(rectangle.f12372x, rectangle.f12373y);
        if (this.scrollbar.getOrientation() == 1) {
            if (!this.isFreeStanding) {
                rectangle.width += 2;
                if (!zIsLeftToRight) {
                    graphics.translate(-1, 0);
                }
            }
            if (thumbColor != null) {
                graphics.setColor(thumbColor);
                graphics.fillRect(0, 0, rectangle.width - 2, rectangle.height - 1);
            }
            graphics.setColor(thumbShadow);
            SwingUtilities2.drawRect(graphics, 0, 0, rectangle.width - 2, rectangle.height - 1);
            graphics.setColor(thumbHighlightColor);
            SwingUtilities2.drawHLine(graphics, 1, rectangle.width - 3, 1);
            SwingUtilities2.drawVLine(graphics, 1, 1, rectangle.height - 2);
            MetalUtils.drawGradient(jComponent, graphics, "ScrollBar.gradient", 2, 2, rectangle.width - 4, rectangle.height - 3, false);
            int i2 = rectangle.width - 8;
            if (i2 > 2 && rectangle.height >= 10) {
                graphics.setColor(MetalLookAndFeel.getPrimaryControlDarkShadow());
                int i3 = (rectangle.height / 2) - 2;
                for (int i4 = 0; i4 < 6; i4 += 2) {
                    graphics.fillRect(4, i4 + i3, i2, 1);
                }
                graphics.setColor(MetalLookAndFeel.getWhite());
                int i5 = i3 + 1;
                for (int i6 = 0; i6 < 6; i6 += 2) {
                    graphics.fillRect(5, i6 + i5, i2, 1);
                }
            }
            if (!this.isFreeStanding) {
                rectangle.width -= 2;
                if (!zIsLeftToRight) {
                    graphics.translate(1, 0);
                }
            }
        } else {
            if (!this.isFreeStanding) {
                rectangle.height += 2;
            }
            if (thumbColor != null) {
                graphics.setColor(thumbColor);
                graphics.fillRect(0, 0, rectangle.width - 1, rectangle.height - 2);
            }
            graphics.setColor(thumbShadow);
            SwingUtilities2.drawRect(graphics, 0, 0, rectangle.width - 1, rectangle.height - 2);
            graphics.setColor(thumbHighlightColor);
            SwingUtilities2.drawHLine(graphics, 1, rectangle.width - 2, 1);
            SwingUtilities2.drawVLine(graphics, 1, 1, rectangle.height - 3);
            MetalUtils.drawGradient(jComponent, graphics, "ScrollBar.gradient", 2, 2, rectangle.width - 3, rectangle.height - 4, true);
            int i7 = rectangle.height - 8;
            if (i7 > 2 && rectangle.width >= 10) {
                graphics.setColor(MetalLookAndFeel.getPrimaryControlDarkShadow());
                int i8 = (rectangle.width / 2) - 2;
                for (int i9 = 0; i9 < 6; i9 += 2) {
                    graphics.fillRect(i8 + i9, 4, 1, i7);
                }
                graphics.setColor(MetalLookAndFeel.getWhite());
                int i10 = i8 + 1;
                for (int i11 = 0; i11 < 6; i11 += 2) {
                    graphics.fillRect(i10 + i11, 5, 1, i7);
                }
            }
            if (!this.isFreeStanding) {
                rectangle.height -= 2;
            }
        }
        graphics.translate(-rectangle.f12372x, -rectangle.f12373y);
    }

    @Override // javax.swing.plaf.basic.BasicScrollBarUI
    protected Dimension getMinimumThumbSize() {
        return new Dimension(this.scrollBarWidth, this.scrollBarWidth);
    }

    @Override // javax.swing.plaf.basic.BasicScrollBarUI
    protected void setThumbBounds(int i2, int i3, int i4, int i5) {
        if (this.thumbRect.f12372x == i2 && this.thumbRect.f12373y == i3 && this.thumbRect.width == i4 && this.thumbRect.height == i5) {
            return;
        }
        int iMin = Math.min(i2, this.thumbRect.f12372x);
        int iMin2 = Math.min(i3, this.thumbRect.f12373y);
        int iMax = Math.max(i2 + i4, this.thumbRect.f12372x + this.thumbRect.width);
        int iMax2 = Math.max(i3 + i5, this.thumbRect.f12373y + this.thumbRect.height);
        this.thumbRect.setBounds(i2, i3, i4, i5);
        this.scrollbar.repaint(iMin, iMin2, (iMax - iMin) + 1, (iMax2 - iMin2) + 1);
    }

    /* loaded from: rt.jar:javax/swing/plaf/metal/MetalScrollBarUI$ScrollBarListener.class */
    class ScrollBarListener extends BasicScrollBarUI.PropertyChangeHandler {
        ScrollBarListener() {
            super();
        }

        @Override // javax.swing.plaf.basic.BasicScrollBarUI.PropertyChangeHandler, java.beans.PropertyChangeListener
        public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
            if (propertyChangeEvent.getPropertyName().equals(MetalScrollBarUI.FREE_STANDING_PROP)) {
                handlePropertyChange(propertyChangeEvent.getNewValue());
            } else {
                super.propertyChange(propertyChangeEvent);
            }
        }

        public void handlePropertyChange(Object obj) {
            if (obj != null) {
                boolean zBooleanValue = ((Boolean) obj).booleanValue();
                boolean z2 = !zBooleanValue && MetalScrollBarUI.this.isFreeStanding;
                boolean z3 = zBooleanValue && !MetalScrollBarUI.this.isFreeStanding;
                MetalScrollBarUI.this.isFreeStanding = zBooleanValue;
                if (z2) {
                    toFlush();
                } else if (z3) {
                    toFreeStanding();
                }
            } else if (!MetalScrollBarUI.this.isFreeStanding) {
                MetalScrollBarUI.this.isFreeStanding = true;
                toFreeStanding();
            }
            if (MetalScrollBarUI.this.increaseButton != null) {
                MetalScrollBarUI.this.increaseButton.setFreeStanding(MetalScrollBarUI.this.isFreeStanding);
            }
            if (MetalScrollBarUI.this.decreaseButton != null) {
                MetalScrollBarUI.this.decreaseButton.setFreeStanding(MetalScrollBarUI.this.isFreeStanding);
            }
        }

        protected void toFlush() {
            MetalScrollBarUI.this.scrollBarWidth -= 2;
        }

        protected void toFreeStanding() {
            MetalScrollBarUI.this.scrollBarWidth += 2;
        }
    }
}
