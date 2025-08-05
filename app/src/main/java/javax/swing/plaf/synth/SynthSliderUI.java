package javax.swing.plaf.synth;

import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Dictionary;
import java.util.Enumeration;
import javax.swing.JComponent;
import javax.swing.JSlider;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicSliderUI;
import javax.swing.plaf.nimbus.NimbusStyle;
import sun.swing.SwingUtilities2;

/* loaded from: rt.jar:javax/swing/plaf/synth/SynthSliderUI.class */
public class SynthSliderUI extends BasicSliderUI implements PropertyChangeListener, SynthUI {
    private Rectangle valueRect;
    private boolean paintValue;
    private Dimension lastSize;
    private int trackHeight;
    private int trackBorder;
    private int thumbWidth;
    private int thumbHeight;
    private SynthStyle style;
    private SynthStyle sliderTrackStyle;
    private SynthStyle sliderThumbStyle;
    private transient boolean thumbActive;
    private transient boolean thumbPressed;

    public static ComponentUI createUI(JComponent jComponent) {
        return new SynthSliderUI((JSlider) jComponent);
    }

    protected SynthSliderUI(JSlider jSlider) {
        super(jSlider);
        this.valueRect = new Rectangle();
    }

    @Override // javax.swing.plaf.basic.BasicSliderUI
    protected void installDefaults(JSlider jSlider) {
        updateStyle(jSlider);
    }

    @Override // javax.swing.plaf.basic.BasicSliderUI
    protected void uninstallDefaults(JSlider jSlider) {
        SynthContext context = getContext(jSlider, 1);
        this.style.uninstallDefaults(context);
        context.dispose();
        this.style = null;
        SynthContext context2 = getContext(jSlider, Region.SLIDER_TRACK, 1);
        this.sliderTrackStyle.uninstallDefaults(context2);
        context2.dispose();
        this.sliderTrackStyle = null;
        SynthContext context3 = getContext(jSlider, Region.SLIDER_THUMB, 1);
        this.sliderThumbStyle.uninstallDefaults(context3);
        context3.dispose();
        this.sliderThumbStyle = null;
    }

    @Override // javax.swing.plaf.basic.BasicSliderUI
    protected void installListeners(JSlider jSlider) {
        super.installListeners(jSlider);
        jSlider.addPropertyChangeListener(this);
    }

    @Override // javax.swing.plaf.basic.BasicSliderUI
    protected void uninstallListeners(JSlider jSlider) {
        jSlider.removePropertyChangeListener(this);
        super.uninstallListeners(jSlider);
    }

    private void updateStyle(JSlider jSlider) {
        SynthContext context = getContext(jSlider, 1);
        SynthStyle synthStyle = this.style;
        this.style = SynthLookAndFeel.updateStyle(context, this);
        if (this.style != synthStyle) {
            this.thumbWidth = this.style.getInt(context, "Slider.thumbWidth", 30);
            this.thumbHeight = this.style.getInt(context, "Slider.thumbHeight", 14);
            String str = (String) this.slider.getClientProperty("JComponent.sizeVariant");
            if (str != null) {
                if (NimbusStyle.LARGE_KEY.equals(str)) {
                    this.thumbWidth = (int) (this.thumbWidth * 1.15d);
                    this.thumbHeight = (int) (this.thumbHeight * 1.15d);
                } else if (NimbusStyle.SMALL_KEY.equals(str)) {
                    this.thumbWidth = (int) (this.thumbWidth * 0.857d);
                    this.thumbHeight = (int) (this.thumbHeight * 0.857d);
                } else if (NimbusStyle.MINI_KEY.equals(str)) {
                    this.thumbWidth = (int) (this.thumbWidth * 0.784d);
                    this.thumbHeight = (int) (this.thumbHeight * 0.784d);
                }
            }
            this.trackBorder = this.style.getInt(context, "Slider.trackBorder", 1);
            this.trackHeight = this.thumbHeight + (this.trackBorder * 2);
            this.paintValue = this.style.getBoolean(context, "Slider.paintValue", true);
            if (synthStyle != null) {
                uninstallKeyboardActions(jSlider);
                installKeyboardActions(jSlider);
            }
        }
        context.dispose();
        SynthContext context2 = getContext(jSlider, Region.SLIDER_TRACK, 1);
        this.sliderTrackStyle = SynthLookAndFeel.updateStyle(context2, this);
        context2.dispose();
        SynthContext context3 = getContext(jSlider, Region.SLIDER_THUMB, 1);
        this.sliderThumbStyle = SynthLookAndFeel.updateStyle(context3, this);
        context3.dispose();
    }

    @Override // javax.swing.plaf.basic.BasicSliderUI
    protected BasicSliderUI.TrackListener createTrackListener(JSlider jSlider) {
        return new SynthTrackListener();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateThumbState(int i2, int i3) {
        setThumbActive(this.thumbRect.contains(i2, i3));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateThumbState(int i2, int i3, boolean z2) {
        updateThumbState(i2, i3);
        setThumbPressed(z2);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setThumbActive(boolean z2) {
        if (this.thumbActive != z2) {
            this.thumbActive = z2;
            this.slider.repaint(this.thumbRect);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setThumbPressed(boolean z2) {
        if (this.thumbPressed != z2) {
            this.thumbPressed = z2;
            this.slider.repaint(this.thumbRect);
        }
    }

    @Override // javax.swing.plaf.basic.BasicSliderUI, javax.swing.plaf.ComponentUI
    public int getBaseline(JComponent jComponent, int i2, int i3) {
        if (jComponent == null) {
            throw new NullPointerException("Component must be non-null");
        }
        if (i2 < 0 || i3 < 0) {
            throw new IllegalArgumentException("Width and height must be >= 0");
        }
        if (this.slider.getPaintLabels() && labelsHaveSameBaselines()) {
            Insets insets = new Insets(0, 0, 0, 0);
            SynthContext context = getContext(this.slider, Region.SLIDER_TRACK);
            this.style.getInsets(context, insets);
            context.dispose();
            if (this.slider.getOrientation() == 0) {
                int maximumCharHeight = 0;
                if (this.paintValue) {
                    SynthContext context2 = getContext(this.slider);
                    maximumCharHeight = context2.getStyle().getGraphicsUtils(context2).getMaximumCharHeight(context2);
                    context2.dispose();
                }
                int tickLength = 0;
                if (this.slider.getPaintTicks()) {
                    tickLength = getTickLength();
                }
                int heightOfTallestLabel = ((i3 / 2) - (((((((maximumCharHeight + this.trackHeight) + insets.top) + insets.bottom) + tickLength) + getHeightOfTallestLabel()) + 4) / 2)) + maximumCharHeight + 2 + this.trackHeight + insets.top + insets.bottom + tickLength + 2;
                JComponent jComponent2 = (JComponent) this.slider.getLabelTable().elements().nextElement2();
                Dimension preferredSize = jComponent2.getPreferredSize();
                return heightOfTallestLabel + jComponent2.getBaseline(preferredSize.width, preferredSize.height);
            }
            Integer lowestValue = this.slider.getInverted() ? getLowestValue() : getHighestValue();
            if (lowestValue != null) {
                int i4 = this.insetCache.top;
                int maximumCharHeight2 = 0;
                if (this.paintValue) {
                    SynthContext context3 = getContext(this.slider);
                    maximumCharHeight2 = context3.getStyle().getGraphicsUtils(context3).getMaximumCharHeight(context3);
                    context3.dispose();
                }
                int iYPositionForValue = yPositionForValue(lowestValue.intValue(), i4 + maximumCharHeight2, ((i3 - this.insetCache.top) - this.insetCache.bottom) - maximumCharHeight2);
                JComponent jComponent3 = (JComponent) this.slider.getLabelTable().get(lowestValue);
                Dimension preferredSize2 = jComponent3.getPreferredSize();
                return (iYPositionForValue - (preferredSize2.height / 2)) + jComponent3.getBaseline(preferredSize2.width, preferredSize2.height);
            }
            return -1;
        }
        return -1;
    }

    @Override // javax.swing.plaf.basic.BasicSliderUI, javax.swing.plaf.ComponentUI
    public Dimension getPreferredSize(JComponent jComponent) {
        recalculateIfInsetsChanged();
        Dimension dimension = new Dimension(this.contentRect.width, this.contentRect.height);
        if (this.slider.getOrientation() == 1) {
            dimension.height = 200;
        } else {
            dimension.width = 200;
        }
        Insets insets = this.slider.getInsets();
        dimension.width += insets.left + insets.right;
        dimension.height += insets.top + insets.bottom;
        return dimension;
    }

    @Override // javax.swing.plaf.basic.BasicSliderUI, javax.swing.plaf.ComponentUI
    public Dimension getMinimumSize(JComponent jComponent) {
        recalculateIfInsetsChanged();
        Dimension dimension = new Dimension(this.contentRect.width, this.contentRect.height);
        if (this.slider.getOrientation() == 1) {
            dimension.height = this.thumbRect.height + this.insetCache.top + this.insetCache.bottom;
        } else {
            dimension.width = this.thumbRect.width + this.insetCache.left + this.insetCache.right;
        }
        return dimension;
    }

    @Override // javax.swing.plaf.basic.BasicSliderUI
    protected void calculateGeometry() {
        calculateThumbSize();
        layout();
        calculateThumbLocation();
    }

    protected void layout() {
        SynthContext context = getContext(this.slider);
        SynthGraphicsUtils graphicsUtils = this.style.getGraphicsUtils(context);
        Insets insets = new Insets(0, 0, 0, 0);
        SynthContext context2 = getContext(this.slider, Region.SLIDER_TRACK);
        this.style.getInsets(context2, insets);
        context2.dispose();
        if (this.slider.getOrientation() == 0) {
            this.valueRect.height = 0;
            if (this.paintValue) {
                this.valueRect.height = graphicsUtils.getMaximumCharHeight(context);
            }
            this.trackRect.height = this.trackHeight;
            this.tickRect.height = 0;
            if (this.slider.getPaintTicks()) {
                this.tickRect.height = getTickLength();
            }
            this.labelRect.height = 0;
            if (this.slider.getPaintLabels()) {
                this.labelRect.height = getHeightOfTallestLabel();
            }
            this.contentRect.height = this.valueRect.height + this.trackRect.height + insets.top + insets.bottom + this.tickRect.height + this.labelRect.height + 4;
            this.contentRect.width = (this.slider.getWidth() - this.insetCache.left) - this.insetCache.right;
            int iMax = 0;
            if (this.slider.getPaintLabels()) {
                this.trackRect.f12372x = this.insetCache.left;
                this.trackRect.width = this.contentRect.width;
                Dictionary labelTable = this.slider.getLabelTable();
                if (labelTable != null) {
                    int minimum = this.slider.getMinimum();
                    int maximum = this.slider.getMaximum();
                    int i2 = Integer.MAX_VALUE;
                    int i3 = Integer.MIN_VALUE;
                    Enumeration enumerationKeys = labelTable.keys();
                    while (enumerationKeys.hasMoreElements()) {
                        int iIntValue = ((Integer) enumerationKeys.nextElement2()).intValue();
                        if (iIntValue >= minimum && iIntValue < i2) {
                            i2 = iIntValue;
                        }
                        if (iIntValue <= maximum && iIntValue > i3) {
                            i3 = iIntValue;
                        }
                    }
                    iMax = Math.max(getPadForLabel(i2), getPadForLabel(i3));
                }
            }
            Rectangle rectangle = this.valueRect;
            Rectangle rectangle2 = this.trackRect;
            Rectangle rectangle3 = this.tickRect;
            Rectangle rectangle4 = this.labelRect;
            int i4 = this.insetCache.left + iMax;
            rectangle4.f12372x = i4;
            rectangle3.f12372x = i4;
            rectangle2.f12372x = i4;
            rectangle.f12372x = i4;
            Rectangle rectangle5 = this.valueRect;
            Rectangle rectangle6 = this.trackRect;
            Rectangle rectangle7 = this.tickRect;
            Rectangle rectangle8 = this.labelRect;
            int i5 = this.contentRect.width - (iMax * 2);
            rectangle8.width = i5;
            rectangle7.width = i5;
            rectangle6.width = i5;
            rectangle5.width = i5;
            int height = (this.slider.getHeight() / 2) - (this.contentRect.height / 2);
            this.valueRect.f12373y = height;
            int i6 = height + this.valueRect.height + 2;
            this.trackRect.f12373y = i6 + insets.top;
            int i7 = i6 + this.trackRect.height + insets.top + insets.bottom;
            this.tickRect.f12373y = i7;
            int i8 = i7 + this.tickRect.height + 2;
            this.labelRect.f12373y = i8;
            int i9 = i8 + this.labelRect.height;
        } else {
            this.trackRect.width = this.trackHeight;
            this.tickRect.width = 0;
            if (this.slider.getPaintTicks()) {
                this.tickRect.width = getTickLength();
            }
            this.labelRect.width = 0;
            if (this.slider.getPaintLabels()) {
                this.labelRect.width = getWidthOfWidestLabel();
            }
            this.valueRect.f12373y = this.insetCache.top;
            this.valueRect.height = 0;
            if (this.paintValue) {
                this.valueRect.height = graphicsUtils.getMaximumCharHeight(context);
            }
            FontMetrics fontMetrics = this.slider.getFontMetrics(this.slider.getFont());
            this.valueRect.width = Math.max(graphicsUtils.computeStringWidth(context, this.slider.getFont(), fontMetrics, "" + this.slider.getMaximum()), graphicsUtils.computeStringWidth(context, this.slider.getFont(), fontMetrics, "" + this.slider.getMinimum()));
            int i10 = this.valueRect.width / 2;
            int i11 = insets.left + (this.trackRect.width / 2);
            int i12 = (this.trackRect.width / 2) + insets.right + this.tickRect.width + this.labelRect.width;
            this.contentRect.width = Math.max(i11, i10) + Math.max(i12, i10) + 2 + this.insetCache.left + this.insetCache.right;
            this.contentRect.height = (this.slider.getHeight() - this.insetCache.top) - this.insetCache.bottom;
            Rectangle rectangle9 = this.trackRect;
            Rectangle rectangle10 = this.tickRect;
            Rectangle rectangle11 = this.labelRect;
            int i13 = this.valueRect.f12373y + this.valueRect.height;
            rectangle11.f12373y = i13;
            rectangle10.f12373y = i13;
            rectangle9.f12373y = i13;
            Rectangle rectangle12 = this.trackRect;
            Rectangle rectangle13 = this.tickRect;
            Rectangle rectangle14 = this.labelRect;
            int i14 = this.contentRect.height - this.valueRect.height;
            rectangle14.height = i14;
            rectangle13.height = i14;
            rectangle12.height = i14;
            int width = (this.slider.getWidth() / 2) - (this.contentRect.width / 2);
            if (SynthLookAndFeel.isLeftToRight(this.slider)) {
                if (i10 > i11) {
                    width += i10 - i11;
                }
                this.trackRect.f12372x = width + insets.left;
                int i15 = width + insets.left + this.trackRect.width + insets.right;
                this.tickRect.f12372x = i15;
                this.labelRect.f12372x = i15 + this.tickRect.width + 2;
            } else {
                if (i10 > i12) {
                    width += i10 - i12;
                }
                this.labelRect.f12372x = width;
                int i16 = width + this.labelRect.width + 2;
                this.tickRect.f12372x = i16;
                this.trackRect.f12372x = i16 + this.tickRect.width + insets.left;
            }
        }
        context.dispose();
        this.lastSize = this.slider.getSize();
    }

    private int getPadForLabel(int i2) {
        int iMax = 0;
        JComponent jComponent = (JComponent) this.slider.getLabelTable().get(Integer.valueOf(i2));
        if (jComponent != null) {
            int iXPositionForValue = xPositionForValue(i2);
            int i3 = jComponent.getPreferredSize().width / 2;
            if (iXPositionForValue - i3 < this.insetCache.left) {
                iMax = Math.max(0, this.insetCache.left - (iXPositionForValue - i3));
            }
            if (iXPositionForValue + i3 > this.slider.getWidth() - this.insetCache.right) {
                iMax = Math.max(iMax, (iXPositionForValue + i3) - (this.slider.getWidth() - this.insetCache.right));
            }
        }
        return iMax;
    }

    @Override // javax.swing.plaf.basic.BasicSliderUI
    protected void calculateThumbLocation() {
        super.calculateThumbLocation();
        if (this.slider.getOrientation() == 0) {
            this.thumbRect.f12373y += this.trackBorder;
        } else {
            this.thumbRect.f12372x += this.trackBorder;
        }
        Point mousePosition = this.slider.getMousePosition();
        if (mousePosition != null) {
            updateThumbState(mousePosition.f12370x, mousePosition.f12371y);
        }
    }

    @Override // javax.swing.plaf.basic.BasicSliderUI
    public void setThumbLocation(int i2, int i3) {
        super.setThumbLocation(i2, i3);
        this.slider.repaint(this.valueRect.f12372x, this.valueRect.f12373y, this.valueRect.width, this.valueRect.height);
        setThumbActive(false);
    }

    @Override // javax.swing.plaf.basic.BasicSliderUI
    protected int xPositionForValue(int i2) {
        int iRound;
        int minimum = this.slider.getMinimum();
        int maximum = this.slider.getMaximum();
        int i3 = this.trackRect.f12372x + (this.thumbRect.width / 2) + this.trackBorder;
        int i4 = ((this.trackRect.f12372x + this.trackRect.width) - (this.thumbRect.width / 2)) - this.trackBorder;
        double d2 = (i4 - i3) / (maximum - minimum);
        if (!drawInverted()) {
            iRound = (int) (i3 + Math.round(d2 * (i2 - minimum)));
        } else {
            iRound = (int) (i4 - Math.round(d2 * (i2 - minimum)));
        }
        return Math.min(i4, Math.max(i3, iRound));
    }

    @Override // javax.swing.plaf.basic.BasicSliderUI
    protected int yPositionForValue(int i2, int i3, int i4) {
        int iRound;
        int minimum = this.slider.getMinimum();
        int maximum = this.slider.getMaximum();
        int i5 = i3 + (this.thumbRect.height / 2) + this.trackBorder;
        int i6 = ((i3 + i4) - (this.thumbRect.height / 2)) - this.trackBorder;
        double d2 = (i6 - i5) / (maximum - minimum);
        if (!drawInverted()) {
            iRound = (int) (i5 + Math.round(d2 * (maximum - i2)));
        } else {
            iRound = (int) (i5 + Math.round(d2 * (i2 - minimum)));
        }
        return Math.min(i6, Math.max(i5, iRound));
    }

    @Override // javax.swing.plaf.basic.BasicSliderUI
    public int valueForYPosition(int i2) {
        int i3;
        int minimum = this.slider.getMinimum();
        int maximum = this.slider.getMaximum();
        int i4 = this.trackRect.f12373y + (this.thumbRect.height / 2) + this.trackBorder;
        int i5 = ((this.trackRect.f12373y + this.trackRect.height) - (this.thumbRect.height / 2)) - this.trackBorder;
        int i6 = i5 - i4;
        if (i2 <= i4) {
            i3 = drawInverted() ? minimum : maximum;
        } else if (i2 >= i5) {
            i3 = drawInverted() ? maximum : minimum;
        } else {
            int iRound = (int) Math.round((i2 - i4) * ((maximum - minimum) / i6));
            i3 = drawInverted() ? minimum + iRound : maximum - iRound;
        }
        return i3;
    }

    @Override // javax.swing.plaf.basic.BasicSliderUI
    public int valueForXPosition(int i2) {
        int i3;
        int minimum = this.slider.getMinimum();
        int maximum = this.slider.getMaximum();
        int i4 = this.trackRect.f12372x + (this.thumbRect.width / 2) + this.trackBorder;
        int i5 = ((this.trackRect.f12372x + this.trackRect.width) - (this.thumbRect.width / 2)) - this.trackBorder;
        int i6 = i5 - i4;
        if (i2 <= i4) {
            i3 = drawInverted() ? maximum : minimum;
        } else if (i2 >= i5) {
            i3 = drawInverted() ? minimum : maximum;
        } else {
            int iRound = (int) Math.round((i2 - i4) * ((maximum - minimum) / i6));
            i3 = drawInverted() ? maximum - iRound : minimum + iRound;
        }
        return i3;
    }

    @Override // javax.swing.plaf.basic.BasicSliderUI
    protected Dimension getThumbSize() {
        Dimension dimension = new Dimension();
        if (this.slider.getOrientation() == 1) {
            dimension.width = this.thumbHeight;
            dimension.height = this.thumbWidth;
        } else {
            dimension.width = this.thumbWidth;
            dimension.height = this.thumbHeight;
        }
        return dimension;
    }

    @Override // javax.swing.plaf.basic.BasicSliderUI
    protected void recalculateIfInsetsChanged() {
        SynthContext context = getContext(this.slider);
        Insets insets = this.style.getInsets(context, null);
        Insets insets2 = this.slider.getInsets();
        insets.left += insets2.left;
        insets.right += insets2.right;
        insets.top += insets2.top;
        insets.bottom += insets2.bottom;
        if (!insets.equals(this.insetCache)) {
            this.insetCache = insets;
            calculateGeometry();
        }
        context.dispose();
    }

    @Override // javax.swing.plaf.synth.SynthUI
    public SynthContext getContext(JComponent jComponent) {
        return getContext(jComponent, SynthLookAndFeel.getComponentState(jComponent));
    }

    private SynthContext getContext(JComponent jComponent, int i2) {
        return SynthContext.getContext(jComponent, this.style, i2);
    }

    private SynthContext getContext(JComponent jComponent, Region region) {
        return getContext(jComponent, region, getComponentState(jComponent, region));
    }

    private SynthContext getContext(JComponent jComponent, Region region, int i2) {
        SynthStyle synthStyle = null;
        if (region == Region.SLIDER_TRACK) {
            synthStyle = this.sliderTrackStyle;
        } else if (region == Region.SLIDER_THUMB) {
            synthStyle = this.sliderThumbStyle;
        }
        return SynthContext.getContext(jComponent, region, synthStyle, i2);
    }

    private int getComponentState(JComponent jComponent, Region region) {
        if (region == Region.SLIDER_THUMB && this.thumbActive && jComponent.isEnabled()) {
            int i2 = this.thumbPressed ? 4 : 2;
            if (jComponent.isFocusOwner()) {
                i2 |= 256;
            }
            return i2;
        }
        return SynthLookAndFeel.getComponentState(jComponent);
    }

    @Override // javax.swing.plaf.ComponentUI
    public void update(Graphics graphics, JComponent jComponent) {
        SynthContext context = getContext(jComponent);
        SynthLookAndFeel.update(context, graphics);
        context.getPainter().paintSliderBackground(context, graphics, 0, 0, jComponent.getWidth(), jComponent.getHeight(), this.slider.getOrientation());
        paint(context, graphics);
        context.dispose();
    }

    @Override // javax.swing.plaf.basic.BasicSliderUI, javax.swing.plaf.ComponentUI
    public void paint(Graphics graphics, JComponent jComponent) {
        SynthContext context = getContext(jComponent);
        paint(context, graphics);
        context.dispose();
    }

    protected void paint(SynthContext synthContext, Graphics graphics) {
        recalculateIfInsetsChanged();
        recalculateIfOrientationChanged();
        Rectangle clipBounds = graphics.getClipBounds();
        if (this.lastSize == null || !this.lastSize.equals(this.slider.getSize())) {
            calculateGeometry();
        }
        if (this.paintValue) {
            int iComputeStringWidth = synthContext.getStyle().getGraphicsUtils(synthContext).computeStringWidth(synthContext, graphics.getFont(), SwingUtilities2.getFontMetrics(this.slider, graphics), "" + this.slider.getValue());
            this.valueRect.f12372x = this.thumbRect.f12372x + ((this.thumbRect.width - iComputeStringWidth) / 2);
            if (this.slider.getOrientation() == 0) {
                if (this.valueRect.f12372x + iComputeStringWidth > this.insetCache.left + this.contentRect.width) {
                    this.valueRect.f12372x = (this.insetCache.left + this.contentRect.width) - iComputeStringWidth;
                }
                this.valueRect.f12372x = Math.max(this.valueRect.f12372x, 0);
            }
            graphics.setColor(synthContext.getStyle().getColor(synthContext, ColorType.TEXT_FOREGROUND));
            synthContext.getStyle().getGraphicsUtils(synthContext).paintText(synthContext, graphics, "" + this.slider.getValue(), this.valueRect.f12372x, this.valueRect.f12373y, -1);
        }
        if (this.slider.getPaintTrack() && clipBounds.intersects(this.trackRect)) {
            SynthContext context = getContext(this.slider, Region.SLIDER_TRACK);
            paintTrack(context, graphics, this.trackRect);
            context.dispose();
        }
        if (clipBounds.intersects(this.thumbRect)) {
            SynthContext context2 = getContext(this.slider, Region.SLIDER_THUMB);
            paintThumb(context2, graphics, this.thumbRect);
            context2.dispose();
        }
        if (this.slider.getPaintTicks() && clipBounds.intersects(this.tickRect)) {
            paintTicks(graphics);
        }
        if (this.slider.getPaintLabels() && clipBounds.intersects(this.labelRect)) {
            paintLabels(graphics);
        }
    }

    @Override // javax.swing.plaf.synth.SynthUI
    public void paintBorder(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
        synthContext.getPainter().paintSliderBorder(synthContext, graphics, i2, i3, i4, i5, this.slider.getOrientation());
    }

    protected void paintThumb(SynthContext synthContext, Graphics graphics, Rectangle rectangle) {
        int orientation = this.slider.getOrientation();
        SynthLookAndFeel.updateSubregion(synthContext, graphics, rectangle);
        synthContext.getPainter().paintSliderThumbBackground(synthContext, graphics, rectangle.f12372x, rectangle.f12373y, rectangle.width, rectangle.height, orientation);
        synthContext.getPainter().paintSliderThumbBorder(synthContext, graphics, rectangle.f12372x, rectangle.f12373y, rectangle.width, rectangle.height, orientation);
    }

    protected void paintTrack(SynthContext synthContext, Graphics graphics, Rectangle rectangle) {
        int orientation = this.slider.getOrientation();
        SynthLookAndFeel.updateSubregion(synthContext, graphics, rectangle);
        synthContext.getPainter().paintSliderTrackBackground(synthContext, graphics, rectangle.f12372x, rectangle.f12373y, rectangle.width, rectangle.height, orientation);
        synthContext.getPainter().paintSliderTrackBorder(synthContext, graphics, rectangle.f12372x, rectangle.f12373y, rectangle.width, rectangle.height, orientation);
    }

    @Override // java.beans.PropertyChangeListener
    public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
        if (SynthLookAndFeel.shouldUpdateStyle(propertyChangeEvent)) {
            updateStyle((JSlider) propertyChangeEvent.getSource());
        }
    }

    /* loaded from: rt.jar:javax/swing/plaf/synth/SynthSliderUI$SynthTrackListener.class */
    private class SynthTrackListener extends BasicSliderUI.TrackListener {
        private SynthTrackListener() {
            super();
        }

        @Override // java.awt.event.MouseAdapter, java.awt.event.MouseListener
        public void mouseExited(MouseEvent mouseEvent) {
            SynthSliderUI.this.setThumbActive(false);
        }

        @Override // javax.swing.plaf.basic.BasicSliderUI.TrackListener, java.awt.event.MouseAdapter, java.awt.event.MouseListener
        public void mousePressed(MouseEvent mouseEvent) {
            super.mousePressed(mouseEvent);
            SynthSliderUI.this.setThumbPressed(SynthSliderUI.this.thumbRect.contains(mouseEvent.getX(), mouseEvent.getY()));
        }

        @Override // javax.swing.plaf.basic.BasicSliderUI.TrackListener, java.awt.event.MouseAdapter, java.awt.event.MouseListener
        public void mouseReleased(MouseEvent mouseEvent) {
            super.mouseReleased(mouseEvent);
            SynthSliderUI.this.updateThumbState(mouseEvent.getX(), mouseEvent.getY(), false);
        }

        @Override // javax.swing.plaf.basic.BasicSliderUI.TrackListener, java.awt.event.MouseAdapter, java.awt.event.MouseMotionListener
        public void mouseDragged(MouseEvent mouseEvent) {
            int i2;
            if (!SynthSliderUI.this.slider.isEnabled()) {
                return;
            }
            this.currentMouseX = mouseEvent.getX();
            this.currentMouseY = mouseEvent.getY();
            if (SynthSliderUI.this.isDragging()) {
                SynthSliderUI.this.slider.setValueIsAdjusting(true);
                switch (SynthSliderUI.this.slider.getOrientation()) {
                    case 0:
                        int i3 = SynthSliderUI.this.thumbRect.width / 2;
                        int x2 = mouseEvent.getX() - this.offset;
                        int i4 = SynthSliderUI.this.trackRect.f12372x + i3 + SynthSliderUI.this.trackBorder;
                        int i5 = ((SynthSliderUI.this.trackRect.f12372x + SynthSliderUI.this.trackRect.width) - i3) - SynthSliderUI.this.trackBorder;
                        int iXPositionForValue = SynthSliderUI.this.xPositionForValue(SynthSliderUI.this.slider.getMaximum() - SynthSliderUI.this.slider.getExtent());
                        if (SynthSliderUI.this.drawInverted()) {
                            i4 = iXPositionForValue;
                        } else {
                            i5 = iXPositionForValue;
                        }
                        int iMin = Math.min(Math.max(x2, i4 - i3), i5 - i3);
                        SynthSliderUI.this.setThumbLocation(iMin, SynthSliderUI.this.thumbRect.f12373y);
                        SynthSliderUI.this.slider.setValue(SynthSliderUI.this.valueForXPosition(iMin + i3));
                        break;
                    case 1:
                        int i6 = SynthSliderUI.this.thumbRect.height / 2;
                        int y2 = mouseEvent.getY() - this.offset;
                        int i7 = SynthSliderUI.this.trackRect.f12373y;
                        int i8 = ((SynthSliderUI.this.trackRect.f12373y + SynthSliderUI.this.trackRect.height) - i6) - SynthSliderUI.this.trackBorder;
                        int iYPositionForValue = SynthSliderUI.this.yPositionForValue(SynthSliderUI.this.slider.getMaximum() - SynthSliderUI.this.slider.getExtent());
                        if (SynthSliderUI.this.drawInverted()) {
                            i8 = iYPositionForValue;
                            i2 = i7 + i6;
                        } else {
                            i2 = iYPositionForValue;
                        }
                        int iMin2 = Math.min(Math.max(y2, i2 - i6), i8 - i6);
                        SynthSliderUI.this.setThumbLocation(SynthSliderUI.this.thumbRect.f12372x, iMin2);
                        SynthSliderUI.this.slider.setValue(SynthSliderUI.this.valueForYPosition(iMin2 + i6));
                        break;
                    default:
                        return;
                }
                if (SynthSliderUI.this.slider.getValueIsAdjusting()) {
                    SynthSliderUI.this.setThumbActive(true);
                }
            }
        }

        @Override // javax.swing.plaf.basic.BasicSliderUI.TrackListener, java.awt.event.MouseAdapter, java.awt.event.MouseMotionListener
        public void mouseMoved(MouseEvent mouseEvent) {
            SynthSliderUI.this.updateThumbState(mouseEvent.getX(), mouseEvent.getY());
        }
    }
}
