package javax.swing.plaf.basic;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.IllegalComponentStateException;
import java.awt.Insets;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Dictionary;
import java.util.Enumeration;
import javax.swing.AbstractAction;
import javax.swing.BoundedRangeModel;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JSlider;
import javax.swing.LookAndFeel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.MouseInputAdapter;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.InsetsUIResource;
import javax.swing.plaf.SliderUI;
import sun.swing.DefaultLookup;
import sun.swing.UIAction;

/* loaded from: rt.jar:javax/swing/plaf/basic/BasicSliderUI.class */
public class BasicSliderUI extends SliderUI {
    public static final int POSITIVE_SCROLL = 1;
    public static final int NEGATIVE_SCROLL = -1;
    public static final int MIN_SCROLL = -2;
    public static final int MAX_SCROLL = 2;
    protected Timer scrollTimer;
    protected JSlider slider;
    protected Insets focusInsets = null;
    protected Insets insetCache = null;
    protected boolean leftToRightCache = true;
    protected Rectangle focusRect = null;
    protected Rectangle contentRect = null;
    protected Rectangle labelRect = null;
    protected Rectangle tickRect = null;
    protected Rectangle trackRect = null;
    protected Rectangle thumbRect = null;
    protected int trackBuffer = 0;
    private transient boolean isDragging;
    protected TrackListener trackListener;
    protected ChangeListener changeListener;
    protected ComponentListener componentListener;
    protected FocusListener focusListener;
    protected ScrollListener scrollListener;
    protected PropertyChangeListener propertyChangeListener;
    private Handler handler;
    private int lastValue;
    private Color shadowColor;
    private Color highlightColor;
    private Color focusColor;
    private boolean checkedLabelBaselines;
    private boolean sameLabelBaselines;
    private static final Actions SHARED_ACTION = new Actions();
    private static Rectangle unionRect = new Rectangle();

    protected Color getShadowColor() {
        return this.shadowColor;
    }

    protected Color getHighlightColor() {
        return this.highlightColor;
    }

    protected Color getFocusColor() {
        return this.focusColor;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public boolean isDragging() {
        return this.isDragging;
    }

    public static ComponentUI createUI(JComponent jComponent) {
        return new BasicSliderUI((JSlider) jComponent);
    }

    public BasicSliderUI(JSlider jSlider) {
    }

    @Override // javax.swing.plaf.ComponentUI
    public void installUI(JComponent jComponent) {
        this.slider = (JSlider) jComponent;
        this.checkedLabelBaselines = false;
        this.slider.setEnabled(this.slider.isEnabled());
        LookAndFeel.installProperty(this.slider, "opaque", Boolean.TRUE);
        this.isDragging = false;
        this.trackListener = createTrackListener(this.slider);
        this.changeListener = createChangeListener(this.slider);
        this.componentListener = createComponentListener(this.slider);
        this.focusListener = createFocusListener(this.slider);
        this.scrollListener = createScrollListener(this.slider);
        this.propertyChangeListener = createPropertyChangeListener(this.slider);
        installDefaults(this.slider);
        installListeners(this.slider);
        installKeyboardActions(this.slider);
        this.scrollTimer = new Timer(100, this.scrollListener);
        this.scrollTimer.setInitialDelay(300);
        this.insetCache = this.slider.getInsets();
        this.leftToRightCache = BasicGraphicsUtils.isLeftToRight(this.slider);
        this.focusRect = new Rectangle();
        this.contentRect = new Rectangle();
        this.labelRect = new Rectangle();
        this.tickRect = new Rectangle();
        this.trackRect = new Rectangle();
        this.thumbRect = new Rectangle();
        this.lastValue = this.slider.getValue();
        calculateGeometry();
    }

    @Override // javax.swing.plaf.ComponentUI
    public void uninstallUI(JComponent jComponent) {
        if (jComponent != this.slider) {
            throw new IllegalComponentStateException(((Object) this) + " was asked to deinstall() " + ((Object) jComponent) + " when it only knows about " + ((Object) this.slider) + ".");
        }
        this.scrollTimer.stop();
        this.scrollTimer = null;
        uninstallDefaults(this.slider);
        uninstallListeners(this.slider);
        uninstallKeyboardActions(this.slider);
        this.insetCache = null;
        this.leftToRightCache = true;
        this.focusRect = null;
        this.contentRect = null;
        this.labelRect = null;
        this.tickRect = null;
        this.trackRect = null;
        this.thumbRect = null;
        this.trackListener = null;
        this.changeListener = null;
        this.componentListener = null;
        this.focusListener = null;
        this.scrollListener = null;
        this.propertyChangeListener = null;
        this.slider = null;
    }

    protected void installDefaults(JSlider jSlider) {
        LookAndFeel.installBorder(jSlider, "Slider.border");
        LookAndFeel.installColorsAndFont(jSlider, "Slider.background", "Slider.foreground", "Slider.font");
        this.highlightColor = UIManager.getColor("Slider.highlight");
        this.shadowColor = UIManager.getColor("Slider.shadow");
        this.focusColor = UIManager.getColor("Slider.focus");
        this.focusInsets = (Insets) UIManager.get("Slider.focusInsets");
        if (this.focusInsets == null) {
            this.focusInsets = new InsetsUIResource(2, 2, 2, 2);
        }
    }

    protected void uninstallDefaults(JSlider jSlider) {
        LookAndFeel.uninstallBorder(jSlider);
        this.focusInsets = null;
    }

    protected TrackListener createTrackListener(JSlider jSlider) {
        return new TrackListener();
    }

    protected ChangeListener createChangeListener(JSlider jSlider) {
        return getHandler();
    }

    protected ComponentListener createComponentListener(JSlider jSlider) {
        return getHandler();
    }

    protected FocusListener createFocusListener(JSlider jSlider) {
        return getHandler();
    }

    protected ScrollListener createScrollListener(JSlider jSlider) {
        return new ScrollListener();
    }

    protected PropertyChangeListener createPropertyChangeListener(JSlider jSlider) {
        return getHandler();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Handler getHandler() {
        if (this.handler == null) {
            this.handler = new Handler();
        }
        return this.handler;
    }

    protected void installListeners(JSlider jSlider) {
        jSlider.addMouseListener(this.trackListener);
        jSlider.addMouseMotionListener(this.trackListener);
        jSlider.addFocusListener(this.focusListener);
        jSlider.addComponentListener(this.componentListener);
        jSlider.addPropertyChangeListener(this.propertyChangeListener);
        jSlider.getModel().addChangeListener(this.changeListener);
    }

    protected void uninstallListeners(JSlider jSlider) {
        jSlider.removeMouseListener(this.trackListener);
        jSlider.removeMouseMotionListener(this.trackListener);
        jSlider.removeFocusListener(this.focusListener);
        jSlider.removeComponentListener(this.componentListener);
        jSlider.removePropertyChangeListener(this.propertyChangeListener);
        jSlider.getModel().removeChangeListener(this.changeListener);
        this.handler = null;
    }

    protected void installKeyboardActions(JSlider jSlider) {
        SwingUtilities.replaceUIInputMap(jSlider, 0, getInputMap(0, jSlider));
        LazyActionMap.installLazyActionMap(jSlider, BasicSliderUI.class, "Slider.actionMap");
    }

    InputMap getInputMap(int i2, JSlider jSlider) {
        InputMap inputMap;
        if (i2 == 0) {
            InputMap inputMap2 = (InputMap) DefaultLookup.get(jSlider, this, "Slider.focusInputMap");
            if (jSlider.getComponentOrientation().isLeftToRight() || (inputMap = (InputMap) DefaultLookup.get(jSlider, this, "Slider.focusInputMap.RightToLeft")) == null) {
                return inputMap2;
            }
            inputMap.setParent(inputMap2);
            return inputMap;
        }
        return null;
    }

    static void loadActionMap(LazyActionMap lazyActionMap) {
        lazyActionMap.put(new Actions(Actions.POSITIVE_UNIT_INCREMENT));
        lazyActionMap.put(new Actions(Actions.POSITIVE_BLOCK_INCREMENT));
        lazyActionMap.put(new Actions(Actions.NEGATIVE_UNIT_INCREMENT));
        lazyActionMap.put(new Actions(Actions.NEGATIVE_BLOCK_INCREMENT));
        lazyActionMap.put(new Actions(Actions.MIN_SCROLL_INCREMENT));
        lazyActionMap.put(new Actions(Actions.MAX_SCROLL_INCREMENT));
    }

    protected void uninstallKeyboardActions(JSlider jSlider) {
        SwingUtilities.replaceUIActionMap(jSlider, null);
        SwingUtilities.replaceUIInputMap(jSlider, 0, null);
    }

    @Override // javax.swing.plaf.ComponentUI
    public int getBaseline(JComponent jComponent, int i2, int i3) {
        super.getBaseline(jComponent, i2, i3);
        if (this.slider.getPaintLabels() && labelsHaveSameBaselines()) {
            FontMetrics fontMetrics = this.slider.getFontMetrics(this.slider.getFont());
            Insets insets = this.slider.getInsets();
            Dimension thumbSize = getThumbSize();
            if (this.slider.getOrientation() == 0) {
                int tickLength = getTickLength();
                int i4 = (((i3 - insets.top) - insets.bottom) - this.focusInsets.top) - this.focusInsets.bottom;
                int i5 = thumbSize.height;
                int i6 = i5;
                if (this.slider.getPaintTicks()) {
                    i6 += tickLength;
                }
                int heightOfTallestLabel = insets.top + this.focusInsets.top + (((i4 - (i6 + getHeightOfTallestLabel())) - 1) / 2) + i5;
                int i7 = tickLength;
                if (!this.slider.getPaintTicks()) {
                    i7 = 0;
                }
                return heightOfTallestLabel + i7 + fontMetrics.getAscent();
            }
            Integer lowestValue = this.slider.getInverted() ? getLowestValue() : getHighestValue();
            if (lowestValue != null) {
                int iMax = Math.max(fontMetrics.getHeight() / 2, thumbSize.height / 2);
                return (yPositionForValue(lowestValue.intValue(), (this.focusInsets.top + insets.top) + iMax, (((((i3 - this.focusInsets.top) - this.focusInsets.bottom) - insets.top) - insets.bottom) - iMax) - iMax) - (fontMetrics.getHeight() / 2)) + fontMetrics.getAscent();
            }
            return 0;
        }
        return 0;
    }

    @Override // javax.swing.plaf.ComponentUI
    public Component.BaselineResizeBehavior getBaselineResizeBehavior(JComponent jComponent) {
        super.getBaselineResizeBehavior(jComponent);
        return Component.BaselineResizeBehavior.OTHER;
    }

    protected boolean labelsHaveSameBaselines() {
        if (!this.checkedLabelBaselines) {
            this.checkedLabelBaselines = true;
            Dictionary labelTable = this.slider.getLabelTable();
            if (labelTable != null) {
                this.sameLabelBaselines = true;
                Enumeration enumerationElements = labelTable.elements();
                int i2 = -1;
                while (true) {
                    if (!enumerationElements.hasMoreElements()) {
                        break;
                    }
                    JComponent jComponent = (JComponent) enumerationElements.nextElement2();
                    Dimension preferredSize = jComponent.getPreferredSize();
                    int baseline = jComponent.getBaseline(preferredSize.width, preferredSize.height);
                    if (baseline >= 0) {
                        if (i2 == -1) {
                            i2 = baseline;
                        } else if (i2 != baseline) {
                            this.sameLabelBaselines = false;
                            break;
                        }
                    } else {
                        this.sameLabelBaselines = false;
                        break;
                    }
                }
            } else {
                this.sameLabelBaselines = false;
            }
        }
        return this.sameLabelBaselines;
    }

    public Dimension getPreferredHorizontalSize() {
        Dimension dimension = (Dimension) DefaultLookup.get(this.slider, this, "Slider.horizontalSize");
        if (dimension == null) {
            dimension = new Dimension(200, 21);
        }
        return dimension;
    }

    public Dimension getPreferredVerticalSize() {
        Dimension dimension = (Dimension) DefaultLookup.get(this.slider, this, "Slider.verticalSize");
        if (dimension == null) {
            dimension = new Dimension(21, 200);
        }
        return dimension;
    }

    public Dimension getMinimumHorizontalSize() {
        Dimension dimension = (Dimension) DefaultLookup.get(this.slider, this, "Slider.minimumHorizontalSize");
        if (dimension == null) {
            dimension = new Dimension(36, 21);
        }
        return dimension;
    }

    public Dimension getMinimumVerticalSize() {
        Dimension dimension = (Dimension) DefaultLookup.get(this.slider, this, "Slider.minimumVerticalSize");
        if (dimension == null) {
            dimension = new Dimension(21, 36);
        }
        return dimension;
    }

    @Override // javax.swing.plaf.ComponentUI
    public Dimension getPreferredSize(JComponent jComponent) {
        Dimension dimension;
        recalculateIfInsetsChanged();
        if (this.slider.getOrientation() == 1) {
            dimension = new Dimension(getPreferredVerticalSize());
            dimension.width = this.insetCache.left + this.insetCache.right;
            dimension.width += this.focusInsets.left + this.focusInsets.right;
            dimension.width += this.trackRect.width + this.tickRect.width + this.labelRect.width;
        } else {
            dimension = new Dimension(getPreferredHorizontalSize());
            dimension.height = this.insetCache.top + this.insetCache.bottom;
            dimension.height += this.focusInsets.top + this.focusInsets.bottom;
            dimension.height += this.trackRect.height + this.tickRect.height + this.labelRect.height;
        }
        return dimension;
    }

    @Override // javax.swing.plaf.ComponentUI
    public Dimension getMinimumSize(JComponent jComponent) {
        Dimension dimension;
        recalculateIfInsetsChanged();
        if (this.slider.getOrientation() == 1) {
            dimension = new Dimension(getMinimumVerticalSize());
            dimension.width = this.insetCache.left + this.insetCache.right;
            dimension.width += this.focusInsets.left + this.focusInsets.right;
            dimension.width += this.trackRect.width + this.tickRect.width + this.labelRect.width;
        } else {
            dimension = new Dimension(getMinimumHorizontalSize());
            dimension.height = this.insetCache.top + this.insetCache.bottom;
            dimension.height += this.focusInsets.top + this.focusInsets.bottom;
            dimension.height += this.trackRect.height + this.tickRect.height + this.labelRect.height;
        }
        return dimension;
    }

    @Override // javax.swing.plaf.ComponentUI
    public Dimension getMaximumSize(JComponent jComponent) {
        Dimension preferredSize = getPreferredSize(jComponent);
        if (this.slider.getOrientation() == 1) {
            preferredSize.height = Short.MAX_VALUE;
        } else {
            preferredSize.width = Short.MAX_VALUE;
        }
        return preferredSize;
    }

    protected void calculateGeometry() {
        calculateFocusRect();
        calculateContentRect();
        calculateThumbSize();
        calculateTrackBuffer();
        calculateTrackRect();
        calculateTickRect();
        calculateLabelRect();
        calculateThumbLocation();
    }

    protected void calculateFocusRect() {
        this.focusRect.f12372x = this.insetCache.left;
        this.focusRect.f12373y = this.insetCache.top;
        this.focusRect.width = this.slider.getWidth() - (this.insetCache.left + this.insetCache.right);
        this.focusRect.height = this.slider.getHeight() - (this.insetCache.top + this.insetCache.bottom);
    }

    protected void calculateThumbSize() {
        Dimension thumbSize = getThumbSize();
        this.thumbRect.setSize(thumbSize.width, thumbSize.height);
    }

    protected void calculateContentRect() {
        this.contentRect.f12372x = this.focusRect.f12372x + this.focusInsets.left;
        this.contentRect.f12373y = this.focusRect.f12373y + this.focusInsets.top;
        this.contentRect.width = this.focusRect.width - (this.focusInsets.left + this.focusInsets.right);
        this.contentRect.height = this.focusRect.height - (this.focusInsets.top + this.focusInsets.bottom);
    }

    private int getTickSpacing() {
        int i2;
        int majorTickSpacing = this.slider.getMajorTickSpacing();
        int minorTickSpacing = this.slider.getMinorTickSpacing();
        if (minorTickSpacing > 0) {
            i2 = minorTickSpacing;
        } else if (majorTickSpacing > 0) {
            i2 = majorTickSpacing;
        } else {
            i2 = 0;
        }
        return i2;
    }

    protected void calculateThumbLocation() {
        if (this.slider.getSnapToTicks()) {
            int value = this.slider.getValue();
            int minimum = value;
            int tickSpacing = getTickSpacing();
            if (tickSpacing != 0) {
                if ((value - this.slider.getMinimum()) % tickSpacing != 0) {
                    int iRound = Math.round((value - this.slider.getMinimum()) / tickSpacing);
                    if (r0 - ((int) r0) == 0.5d && value < this.lastValue) {
                        iRound--;
                    }
                    minimum = this.slider.getMinimum() + (iRound * tickSpacing);
                }
                if (minimum != value) {
                    this.slider.setValue(minimum);
                }
            }
        }
        if (this.slider.getOrientation() == 0) {
            this.thumbRect.f12372x = xPositionForValue(this.slider.getValue()) - (this.thumbRect.width / 2);
            this.thumbRect.f12373y = this.trackRect.f12373y;
            return;
        }
        int iYPositionForValue = yPositionForValue(this.slider.getValue());
        this.thumbRect.f12372x = this.trackRect.f12372x;
        this.thumbRect.f12373y = iYPositionForValue - (this.thumbRect.height / 2);
    }

    protected void calculateTrackBuffer() {
        if (this.slider.getPaintLabels() && this.slider.getLabelTable() != null) {
            Component highestValueLabel = getHighestValueLabel();
            Component lowestValueLabel = getLowestValueLabel();
            if (this.slider.getOrientation() == 0) {
                this.trackBuffer = Math.max(highestValueLabel.getBounds().width, lowestValueLabel.getBounds().width) / 2;
                this.trackBuffer = Math.max(this.trackBuffer, this.thumbRect.width / 2);
                return;
            } else {
                this.trackBuffer = Math.max(highestValueLabel.getBounds().height, lowestValueLabel.getBounds().height) / 2;
                this.trackBuffer = Math.max(this.trackBuffer, this.thumbRect.height / 2);
                return;
            }
        }
        if (this.slider.getOrientation() == 0) {
            this.trackBuffer = this.thumbRect.width / 2;
        } else {
            this.trackBuffer = this.thumbRect.height / 2;
        }
    }

    protected void calculateTrackRect() {
        if (this.slider.getOrientation() == 0) {
            int heightOfTallestLabel = this.thumbRect.height;
            if (this.slider.getPaintTicks()) {
                heightOfTallestLabel += getTickLength();
            }
            if (this.slider.getPaintLabels()) {
                heightOfTallestLabel += getHeightOfTallestLabel();
            }
            this.trackRect.f12372x = this.contentRect.f12372x + this.trackBuffer;
            this.trackRect.f12373y = this.contentRect.f12373y + (((this.contentRect.height - heightOfTallestLabel) - 1) / 2);
            this.trackRect.width = this.contentRect.width - (this.trackBuffer * 2);
            this.trackRect.height = this.thumbRect.height;
            return;
        }
        int widthOfWidestLabel = this.thumbRect.width;
        if (BasicGraphicsUtils.isLeftToRight(this.slider)) {
            if (this.slider.getPaintTicks()) {
                widthOfWidestLabel += getTickLength();
            }
            if (this.slider.getPaintLabels()) {
                widthOfWidestLabel += getWidthOfWidestLabel();
            }
        } else {
            if (this.slider.getPaintTicks()) {
                widthOfWidestLabel -= getTickLength();
            }
            if (this.slider.getPaintLabels()) {
                widthOfWidestLabel -= getWidthOfWidestLabel();
            }
        }
        this.trackRect.f12372x = this.contentRect.f12372x + (((this.contentRect.width - widthOfWidestLabel) - 1) / 2);
        this.trackRect.f12373y = this.contentRect.f12373y + this.trackBuffer;
        this.trackRect.width = this.thumbRect.width;
        this.trackRect.height = this.contentRect.height - (this.trackBuffer * 2);
    }

    protected int getTickLength() {
        return 8;
    }

    protected void calculateTickRect() {
        if (this.slider.getOrientation() == 0) {
            this.tickRect.f12372x = this.trackRect.f12372x;
            this.tickRect.f12373y = this.trackRect.f12373y + this.trackRect.height;
            this.tickRect.width = this.trackRect.width;
            this.tickRect.height = this.slider.getPaintTicks() ? getTickLength() : 0;
            return;
        }
        this.tickRect.width = this.slider.getPaintTicks() ? getTickLength() : 0;
        if (BasicGraphicsUtils.isLeftToRight(this.slider)) {
            this.tickRect.f12372x = this.trackRect.f12372x + this.trackRect.width;
        } else {
            this.tickRect.f12372x = this.trackRect.f12372x - this.tickRect.width;
        }
        this.tickRect.f12373y = this.trackRect.f12373y;
        this.tickRect.height = this.trackRect.height;
    }

    protected void calculateLabelRect() {
        if (this.slider.getPaintLabels()) {
            if (this.slider.getOrientation() == 0) {
                this.labelRect.f12372x = this.tickRect.f12372x - this.trackBuffer;
                this.labelRect.f12373y = this.tickRect.f12373y + this.tickRect.height;
                this.labelRect.width = this.tickRect.width + (this.trackBuffer * 2);
                this.labelRect.height = getHeightOfTallestLabel();
                return;
            }
            if (BasicGraphicsUtils.isLeftToRight(this.slider)) {
                this.labelRect.f12372x = this.tickRect.f12372x + this.tickRect.width;
                this.labelRect.width = getWidthOfWidestLabel();
            } else {
                this.labelRect.width = getWidthOfWidestLabel();
                this.labelRect.f12372x = this.tickRect.f12372x - this.labelRect.width;
            }
            this.labelRect.f12373y = this.tickRect.f12373y - this.trackBuffer;
            this.labelRect.height = this.tickRect.height + (this.trackBuffer * 2);
            return;
        }
        if (this.slider.getOrientation() == 0) {
            this.labelRect.f12372x = this.tickRect.f12372x;
            this.labelRect.f12373y = this.tickRect.f12373y + this.tickRect.height;
            this.labelRect.width = this.tickRect.width;
            this.labelRect.height = 0;
            return;
        }
        if (BasicGraphicsUtils.isLeftToRight(this.slider)) {
            this.labelRect.f12372x = this.tickRect.f12372x + this.tickRect.width;
        } else {
            this.labelRect.f12372x = this.tickRect.f12372x;
        }
        this.labelRect.f12373y = this.tickRect.f12373y;
        this.labelRect.width = 0;
        this.labelRect.height = this.tickRect.height;
    }

    protected Dimension getThumbSize() {
        Dimension dimension = new Dimension();
        if (this.slider.getOrientation() == 1) {
            dimension.width = 20;
            dimension.height = 11;
        } else {
            dimension.width = 11;
            dimension.height = 20;
        }
        return dimension;
    }

    /* loaded from: rt.jar:javax/swing/plaf/basic/BasicSliderUI$PropertyChangeHandler.class */
    public class PropertyChangeHandler implements PropertyChangeListener {
        public PropertyChangeHandler() {
        }

        @Override // java.beans.PropertyChangeListener
        public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
            BasicSliderUI.this.getHandler().propertyChange(propertyChangeEvent);
        }
    }

    protected int getWidthOfWidestLabel() {
        Dictionary labelTable = this.slider.getLabelTable();
        int iMax = 0;
        if (labelTable != null) {
            Enumeration enumerationKeys = labelTable.keys();
            while (enumerationKeys.hasMoreElements()) {
                iMax = Math.max(((JComponent) labelTable.get(enumerationKeys.nextElement2())).getPreferredSize().width, iMax);
            }
        }
        return iMax;
    }

    protected int getHeightOfTallestLabel() {
        Dictionary labelTable = this.slider.getLabelTable();
        int iMax = 0;
        if (labelTable != null) {
            Enumeration enumerationKeys = labelTable.keys();
            while (enumerationKeys.hasMoreElements()) {
                iMax = Math.max(((JComponent) labelTable.get(enumerationKeys.nextElement2())).getPreferredSize().height, iMax);
            }
        }
        return iMax;
    }

    protected int getWidthOfHighValueLabel() {
        Component highestValueLabel = getHighestValueLabel();
        int i2 = 0;
        if (highestValueLabel != null) {
            i2 = highestValueLabel.getPreferredSize().width;
        }
        return i2;
    }

    protected int getWidthOfLowValueLabel() {
        Component lowestValueLabel = getLowestValueLabel();
        int i2 = 0;
        if (lowestValueLabel != null) {
            i2 = lowestValueLabel.getPreferredSize().width;
        }
        return i2;
    }

    protected int getHeightOfHighValueLabel() {
        Component highestValueLabel = getHighestValueLabel();
        int i2 = 0;
        if (highestValueLabel != null) {
            i2 = highestValueLabel.getPreferredSize().height;
        }
        return i2;
    }

    protected int getHeightOfLowValueLabel() {
        Component lowestValueLabel = getLowestValueLabel();
        int i2 = 0;
        if (lowestValueLabel != null) {
            i2 = lowestValueLabel.getPreferredSize().height;
        }
        return i2;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public boolean drawInverted() {
        if (this.slider.getOrientation() == 0) {
            if (BasicGraphicsUtils.isLeftToRight(this.slider)) {
                return this.slider.getInverted();
            }
            return !this.slider.getInverted();
        }
        return this.slider.getInverted();
    }

    protected Integer getHighestValue() {
        Dictionary labelTable = this.slider.getLabelTable();
        if (labelTable == null) {
            return null;
        }
        Enumeration enumerationKeys = labelTable.keys();
        Integer num = null;
        while (enumerationKeys.hasMoreElements()) {
            Integer num2 = (Integer) enumerationKeys.nextElement2();
            if (num == null || num2.intValue() > num.intValue()) {
                num = num2;
            }
        }
        return num;
    }

    protected Integer getLowestValue() {
        Dictionary labelTable = this.slider.getLabelTable();
        if (labelTable == null) {
            return null;
        }
        Enumeration enumerationKeys = labelTable.keys();
        Integer num = null;
        while (enumerationKeys.hasMoreElements()) {
            Integer num2 = (Integer) enumerationKeys.nextElement2();
            if (num == null || num2.intValue() < num.intValue()) {
                num = num2;
            }
        }
        return num;
    }

    protected Component getLowestValueLabel() {
        Integer lowestValue = getLowestValue();
        if (lowestValue != null) {
            return (Component) this.slider.getLabelTable().get(lowestValue);
        }
        return null;
    }

    protected Component getHighestValueLabel() {
        Integer highestValue = getHighestValue();
        if (highestValue != null) {
            return (Component) this.slider.getLabelTable().get(highestValue);
        }
        return null;
    }

    @Override // javax.swing.plaf.ComponentUI
    public void paint(Graphics graphics, JComponent jComponent) {
        recalculateIfInsetsChanged();
        recalculateIfOrientationChanged();
        Rectangle clipBounds = graphics.getClipBounds();
        if (!clipBounds.intersects(this.trackRect) && this.slider.getPaintTrack()) {
            calculateGeometry();
        }
        if (this.slider.getPaintTrack() && clipBounds.intersects(this.trackRect)) {
            paintTrack(graphics);
        }
        if (this.slider.getPaintTicks() && clipBounds.intersects(this.tickRect)) {
            paintTicks(graphics);
        }
        if (this.slider.getPaintLabels() && clipBounds.intersects(this.labelRect)) {
            paintLabels(graphics);
        }
        if (this.slider.hasFocus() && clipBounds.intersects(this.focusRect)) {
            paintFocus(graphics);
        }
        if (clipBounds.intersects(this.thumbRect)) {
            paintThumb(graphics);
        }
    }

    protected void recalculateIfInsetsChanged() {
        Insets insets = this.slider.getInsets();
        if (!insets.equals(this.insetCache)) {
            this.insetCache = insets;
            calculateGeometry();
        }
    }

    protected void recalculateIfOrientationChanged() {
        boolean zIsLeftToRight = BasicGraphicsUtils.isLeftToRight(this.slider);
        if (zIsLeftToRight != this.leftToRightCache) {
            this.leftToRightCache = zIsLeftToRight;
            calculateGeometry();
        }
    }

    public void paintFocus(Graphics graphics) {
        graphics.setColor(getFocusColor());
        BasicGraphicsUtils.drawDashedRect(graphics, this.focusRect.f12372x, this.focusRect.f12373y, this.focusRect.width, this.focusRect.height);
    }

    public void paintTrack(Graphics graphics) {
        Rectangle rectangle = this.trackRect;
        if (this.slider.getOrientation() == 0) {
            int i2 = (rectangle.height / 2) - 2;
            int i3 = rectangle.width;
            graphics.translate(rectangle.f12372x, rectangle.f12373y + i2);
            graphics.setColor(getShadowColor());
            graphics.drawLine(0, 0, i3 - 1, 0);
            graphics.drawLine(0, 1, 0, 2);
            graphics.setColor(getHighlightColor());
            graphics.drawLine(0, 3, i3, 3);
            graphics.drawLine(i3, 0, i3, 3);
            graphics.setColor(Color.black);
            graphics.drawLine(1, 1, i3 - 2, 1);
            graphics.translate(-rectangle.f12372x, -(rectangle.f12373y + i2));
            return;
        }
        int i4 = (rectangle.width / 2) - 2;
        int i5 = rectangle.height;
        graphics.translate(rectangle.f12372x + i4, rectangle.f12373y);
        graphics.setColor(getShadowColor());
        graphics.drawLine(0, 0, 0, i5 - 1);
        graphics.drawLine(1, 0, 2, 0);
        graphics.setColor(getHighlightColor());
        graphics.drawLine(3, 0, 3, i5);
        graphics.drawLine(0, i5, 3, i5);
        graphics.setColor(Color.black);
        graphics.drawLine(1, 1, 1, i5 - 2);
        graphics.translate(-(rectangle.f12372x + i4), -rectangle.f12373y);
    }

    public void paintTicks(Graphics graphics) {
        Rectangle rectangle = this.tickRect;
        graphics.setColor(DefaultLookup.getColor(this.slider, this, "Slider.tickColor", Color.black));
        if (this.slider.getOrientation() == 0) {
            graphics.translate(0, rectangle.f12373y);
            if (this.slider.getMinorTickSpacing() > 0) {
                int minimum = this.slider.getMinimum();
                while (true) {
                    int i2 = minimum;
                    if (i2 > this.slider.getMaximum()) {
                        break;
                    }
                    paintMinorTickForHorizSlider(graphics, rectangle, xPositionForValue(i2));
                    if (Integer.MAX_VALUE - this.slider.getMinorTickSpacing() < i2) {
                        break;
                    } else {
                        minimum = i2 + this.slider.getMinorTickSpacing();
                    }
                }
            }
            if (this.slider.getMajorTickSpacing() > 0) {
                int minimum2 = this.slider.getMinimum();
                while (true) {
                    int i3 = minimum2;
                    if (i3 > this.slider.getMaximum()) {
                        break;
                    }
                    paintMajorTickForHorizSlider(graphics, rectangle, xPositionForValue(i3));
                    if (Integer.MAX_VALUE - this.slider.getMajorTickSpacing() < i3) {
                        break;
                    } else {
                        minimum2 = i3 + this.slider.getMajorTickSpacing();
                    }
                }
            }
            graphics.translate(0, -rectangle.f12373y);
            return;
        }
        graphics.translate(rectangle.f12372x, 0);
        if (this.slider.getMinorTickSpacing() > 0) {
            int i4 = 0;
            if (!BasicGraphicsUtils.isLeftToRight(this.slider)) {
                i4 = rectangle.width - (rectangle.width / 2);
                graphics.translate(i4, 0);
            }
            int minimum3 = this.slider.getMinimum();
            while (true) {
                int i5 = minimum3;
                if (i5 > this.slider.getMaximum()) {
                    break;
                }
                paintMinorTickForVertSlider(graphics, rectangle, yPositionForValue(i5));
                if (Integer.MAX_VALUE - this.slider.getMinorTickSpacing() < i5) {
                    break;
                } else {
                    minimum3 = i5 + this.slider.getMinorTickSpacing();
                }
            }
            if (!BasicGraphicsUtils.isLeftToRight(this.slider)) {
                graphics.translate(-i4, 0);
            }
        }
        if (this.slider.getMajorTickSpacing() > 0) {
            if (!BasicGraphicsUtils.isLeftToRight(this.slider)) {
                graphics.translate(2, 0);
            }
            int minimum4 = this.slider.getMinimum();
            while (true) {
                int i6 = minimum4;
                if (i6 > this.slider.getMaximum()) {
                    break;
                }
                paintMajorTickForVertSlider(graphics, rectangle, yPositionForValue(i6));
                if (Integer.MAX_VALUE - this.slider.getMajorTickSpacing() < i6) {
                    break;
                } else {
                    minimum4 = i6 + this.slider.getMajorTickSpacing();
                }
            }
            if (!BasicGraphicsUtils.isLeftToRight(this.slider)) {
                graphics.translate(-2, 0);
            }
        }
        graphics.translate(-rectangle.f12372x, 0);
    }

    protected void paintMinorTickForHorizSlider(Graphics graphics, Rectangle rectangle, int i2) {
        graphics.drawLine(i2, 0, i2, (rectangle.height / 2) - 1);
    }

    protected void paintMajorTickForHorizSlider(Graphics graphics, Rectangle rectangle, int i2) {
        graphics.drawLine(i2, 0, i2, rectangle.height - 2);
    }

    protected void paintMinorTickForVertSlider(Graphics graphics, Rectangle rectangle, int i2) {
        graphics.drawLine(0, i2, (rectangle.width / 2) - 1, i2);
    }

    protected void paintMajorTickForVertSlider(Graphics graphics, Rectangle rectangle, int i2) {
        graphics.drawLine(0, i2, rectangle.width - 2, i2);
    }

    public void paintLabels(Graphics graphics) {
        Rectangle rectangle = this.labelRect;
        Dictionary labelTable = this.slider.getLabelTable();
        if (labelTable != null) {
            Enumeration enumerationKeys = labelTable.keys();
            int minimum = this.slider.getMinimum();
            int maximum = this.slider.getMaximum();
            boolean zIsEnabled = this.slider.isEnabled();
            while (enumerationKeys.hasMoreElements()) {
                Integer num = (Integer) enumerationKeys.nextElement2();
                int iIntValue = num.intValue();
                if (iIntValue >= minimum && iIntValue <= maximum) {
                    JComponent jComponent = (JComponent) labelTable.get(num);
                    jComponent.setEnabled(zIsEnabled);
                    if (jComponent instanceof JLabel) {
                        Icon icon = jComponent.isEnabled() ? ((JLabel) jComponent).getIcon() : ((JLabel) jComponent).getDisabledIcon();
                        if (icon instanceof ImageIcon) {
                            Toolkit.getDefaultToolkit().checkImage(((ImageIcon) icon).getImage(), -1, -1, this.slider);
                        }
                    }
                    if (this.slider.getOrientation() == 0) {
                        graphics.translate(0, rectangle.f12373y);
                        paintHorizontalLabel(graphics, iIntValue, jComponent);
                        graphics.translate(0, -rectangle.f12373y);
                    } else {
                        int i2 = 0;
                        if (!BasicGraphicsUtils.isLeftToRight(this.slider)) {
                            i2 = rectangle.width - jComponent.getPreferredSize().width;
                        }
                        graphics.translate(rectangle.f12372x + i2, 0);
                        paintVerticalLabel(graphics, iIntValue, jComponent);
                        graphics.translate((-rectangle.f12372x) - i2, 0);
                    }
                }
            }
        }
    }

    protected void paintHorizontalLabel(Graphics graphics, int i2, Component component) {
        int iXPositionForValue = xPositionForValue(i2) - (component.getPreferredSize().width / 2);
        graphics.translate(iXPositionForValue, 0);
        component.paint(graphics);
        graphics.translate(-iXPositionForValue, 0);
    }

    protected void paintVerticalLabel(Graphics graphics, int i2, Component component) {
        int iYPositionForValue = yPositionForValue(i2) - (component.getPreferredSize().height / 2);
        graphics.translate(0, iYPositionForValue);
        component.paint(graphics);
        graphics.translate(0, -iYPositionForValue);
    }

    public void paintThumb(Graphics graphics) {
        Rectangle rectangle = this.thumbRect;
        int i2 = rectangle.width;
        int i3 = rectangle.height;
        graphics.translate(rectangle.f12372x, rectangle.f12373y);
        if (this.slider.isEnabled()) {
            graphics.setColor(this.slider.getBackground());
        } else {
            graphics.setColor(this.slider.getBackground().darker());
        }
        Boolean bool = (Boolean) this.slider.getClientProperty("Slider.paintThumbArrowShape");
        if ((!this.slider.getPaintTicks() && bool == null) || bool == Boolean.FALSE) {
            graphics.fillRect(0, 0, i2, i3);
            graphics.setColor(Color.black);
            graphics.drawLine(0, i3 - 1, i2 - 1, i3 - 1);
            graphics.drawLine(i2 - 1, 0, i2 - 1, i3 - 1);
            graphics.setColor(this.highlightColor);
            graphics.drawLine(0, 0, 0, i3 - 2);
            graphics.drawLine(1, 0, i2 - 2, 0);
            graphics.setColor(this.shadowColor);
            graphics.drawLine(1, i3 - 2, i2 - 2, i3 - 2);
            graphics.drawLine(i2 - 2, 1, i2 - 2, i3 - 3);
        } else if (this.slider.getOrientation() == 0) {
            int i4 = i2 / 2;
            graphics.fillRect(1, 1, i2 - 3, (i3 - 1) - i4);
            Polygon polygon = new Polygon();
            polygon.addPoint(1, i3 - i4);
            polygon.addPoint(i4 - 1, i3 - 1);
            polygon.addPoint(i2 - 2, (i3 - 1) - i4);
            graphics.fillPolygon(polygon);
            graphics.setColor(this.highlightColor);
            graphics.drawLine(0, 0, i2 - 2, 0);
            graphics.drawLine(0, 1, 0, (i3 - 1) - i4);
            graphics.drawLine(0, i3 - i4, i4 - 1, i3 - 1);
            graphics.setColor(Color.black);
            graphics.drawLine(i2 - 1, 0, i2 - 1, (i3 - 2) - i4);
            graphics.drawLine(i2 - 1, (i3 - 1) - i4, (i2 - 1) - i4, i3 - 1);
            graphics.setColor(this.shadowColor);
            graphics.drawLine(i2 - 2, 1, i2 - 2, (i3 - 2) - i4);
            graphics.drawLine(i2 - 2, (i3 - 1) - i4, (i2 - 1) - i4, i3 - 2);
        } else {
            int i5 = i3 / 2;
            if (BasicGraphicsUtils.isLeftToRight(this.slider)) {
                graphics.fillRect(1, 1, (i2 - 1) - i5, i3 - 3);
                Polygon polygon2 = new Polygon();
                polygon2.addPoint((i2 - i5) - 1, 0);
                polygon2.addPoint(i2 - 1, i5);
                polygon2.addPoint((i2 - 1) - i5, i3 - 2);
                graphics.fillPolygon(polygon2);
                graphics.setColor(this.highlightColor);
                graphics.drawLine(0, 0, 0, i3 - 2);
                graphics.drawLine(1, 0, (i2 - 1) - i5, 0);
                graphics.drawLine((i2 - i5) - 1, 0, i2 - 1, i5);
                graphics.setColor(Color.black);
                graphics.drawLine(0, i3 - 1, (i2 - 2) - i5, i3 - 1);
                graphics.drawLine((i2 - 1) - i5, i3 - 1, i2 - 1, (i3 - 1) - i5);
                graphics.setColor(this.shadowColor);
                graphics.drawLine(1, i3 - 2, (i2 - 2) - i5, i3 - 2);
                graphics.drawLine((i2 - 1) - i5, i3 - 2, i2 - 2, (i3 - i5) - 1);
            } else {
                graphics.fillRect(5, 1, (i2 - 1) - i5, i3 - 3);
                Polygon polygon3 = new Polygon();
                polygon3.addPoint(i5, 0);
                polygon3.addPoint(0, i5);
                polygon3.addPoint(i5, i3 - 2);
                graphics.fillPolygon(polygon3);
                graphics.setColor(this.highlightColor);
                graphics.drawLine(i5 - 1, 0, i2 - 2, 0);
                graphics.drawLine(0, i5, i5, 0);
                graphics.setColor(Color.black);
                graphics.drawLine(0, (i3 - 1) - i5, i5, i3 - 1);
                graphics.drawLine(i5, i3 - 1, i2 - 1, i3 - 1);
                graphics.setColor(this.shadowColor);
                graphics.drawLine(i5, i3 - 2, i2 - 2, i3 - 2);
                graphics.drawLine(i2 - 1, 1, i2 - 1, i3 - 2);
            }
        }
        graphics.translate(-rectangle.f12372x, -rectangle.f12373y);
    }

    public void setThumbLocation(int i2, int i3) {
        unionRect.setBounds(this.thumbRect);
        this.thumbRect.setLocation(i2, i3);
        SwingUtilities.computeUnion(this.thumbRect.f12372x, this.thumbRect.f12373y, this.thumbRect.width, this.thumbRect.height, unionRect);
        this.slider.repaint(unionRect.f12372x, unionRect.f12373y, unionRect.width, unionRect.height);
    }

    public void scrollByBlock(int i2) {
        int tickSpacing;
        synchronized (this.slider) {
            int maximum = (this.slider.getMaximum() - this.slider.getMinimum()) / 10;
            if (maximum == 0) {
                maximum = 1;
            }
            if (this.slider.getSnapToTicks() && maximum < (tickSpacing = getTickSpacing())) {
                maximum = tickSpacing;
            }
            this.slider.setValue(this.slider.getValue() + (maximum * (i2 > 0 ? 1 : -1)));
        }
    }

    public void scrollByUnit(int i2) {
        synchronized (this.slider) {
            int tickSpacing = i2 > 0 ? 1 : -1;
            if (this.slider.getSnapToTicks()) {
                tickSpacing *= getTickSpacing();
            }
            this.slider.setValue(this.slider.getValue() + tickSpacing);
        }
    }

    protected void scrollDueToClickInTrack(int i2) {
        scrollByBlock(i2);
    }

    protected int xPositionForValue(int i2) {
        int iRound;
        int minimum = this.slider.getMinimum();
        double maximum = this.trackRect.width / (this.slider.getMaximum() - minimum);
        int i3 = this.trackRect.f12372x;
        int i4 = this.trackRect.f12372x + (this.trackRect.width - 1);
        if (!drawInverted()) {
            iRound = (int) (i3 + Math.round(maximum * (i2 - minimum)));
        } else {
            iRound = (int) (i4 - Math.round(maximum * (i2 - minimum)));
        }
        return Math.min(i4, Math.max(i3, iRound));
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public int yPositionForValue(int i2) {
        return yPositionForValue(i2, this.trackRect.f12373y, this.trackRect.height);
    }

    protected int yPositionForValue(int i2, int i3, int i4) {
        int iRound;
        int minimum = this.slider.getMinimum();
        int maximum = this.slider.getMaximum();
        double d2 = i4 / (maximum - minimum);
        int i5 = i3 + (i4 - 1);
        if (!drawInverted()) {
            iRound = (int) (i3 + Math.round(d2 * (maximum - i2)));
        } else {
            iRound = (int) (i3 + Math.round(d2 * (i2 - minimum)));
        }
        return Math.min(i5, Math.max(i3, iRound));
    }

    public int valueForYPosition(int i2) {
        int i3;
        int minimum = this.slider.getMinimum();
        int maximum = this.slider.getMaximum();
        int i4 = this.trackRect.height;
        int i5 = this.trackRect.f12373y;
        int i6 = this.trackRect.f12373y + (this.trackRect.height - 1);
        if (i2 <= i5) {
            i3 = drawInverted() ? minimum : maximum;
        } else if (i2 >= i6) {
            i3 = drawInverted() ? maximum : minimum;
        } else {
            int iRound = (int) Math.round((i2 - i5) * ((maximum - minimum) / i4));
            i3 = drawInverted() ? minimum + iRound : maximum - iRound;
        }
        return i3;
    }

    public int valueForXPosition(int i2) {
        int i3;
        int minimum = this.slider.getMinimum();
        int maximum = this.slider.getMaximum();
        int i4 = this.trackRect.width;
        int i5 = this.trackRect.f12372x;
        int i6 = this.trackRect.f12372x + (this.trackRect.width - 1);
        if (i2 <= i5) {
            i3 = drawInverted() ? maximum : minimum;
        } else if (i2 >= i6) {
            i3 = drawInverted() ? minimum : maximum;
        } else {
            int iRound = (int) Math.round((i2 - i5) * ((maximum - minimum) / i4));
            i3 = drawInverted() ? maximum - iRound : minimum + iRound;
        }
        return i3;
    }

    /* loaded from: rt.jar:javax/swing/plaf/basic/BasicSliderUI$Handler.class */
    private class Handler implements ChangeListener, ComponentListener, FocusListener, PropertyChangeListener {
        private Handler() {
        }

        @Override // javax.swing.event.ChangeListener
        public void stateChanged(ChangeEvent changeEvent) {
            if (!BasicSliderUI.this.isDragging) {
                BasicSliderUI.this.calculateThumbLocation();
                BasicSliderUI.this.slider.repaint();
            }
            BasicSliderUI.this.lastValue = BasicSliderUI.this.slider.getValue();
        }

        @Override // java.awt.event.ComponentListener
        public void componentHidden(ComponentEvent componentEvent) {
        }

        @Override // java.awt.event.ComponentListener
        public void componentMoved(ComponentEvent componentEvent) {
        }

        @Override // java.awt.event.ComponentListener
        public void componentResized(ComponentEvent componentEvent) {
            BasicSliderUI.this.calculateGeometry();
            BasicSliderUI.this.slider.repaint();
        }

        @Override // java.awt.event.ComponentListener
        public void componentShown(ComponentEvent componentEvent) {
        }

        @Override // java.awt.event.FocusListener
        public void focusGained(FocusEvent focusEvent) {
            BasicSliderUI.this.slider.repaint();
        }

        @Override // java.awt.event.FocusListener
        public void focusLost(FocusEvent focusEvent) {
            BasicSliderUI.this.slider.repaint();
        }

        @Override // java.beans.PropertyChangeListener
        public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
            String propertyName = propertyChangeEvent.getPropertyName();
            if (propertyName == "orientation" || propertyName == "inverted" || propertyName == "labelTable" || propertyName == "majorTickSpacing" || propertyName == "minorTickSpacing" || propertyName == "paintTicks" || propertyName == "paintTrack" || propertyName == "font" || propertyName == "paintLabels" || propertyName == "Slider.paintThumbArrowShape") {
                BasicSliderUI.this.checkedLabelBaselines = false;
                BasicSliderUI.this.calculateGeometry();
                BasicSliderUI.this.slider.repaint();
            } else {
                if (propertyName == "componentOrientation") {
                    BasicSliderUI.this.calculateGeometry();
                    BasicSliderUI.this.slider.repaint();
                    SwingUtilities.replaceUIInputMap(BasicSliderUI.this.slider, 0, BasicSliderUI.this.getInputMap(0, BasicSliderUI.this.slider));
                    return;
                }
                if (propertyName == "model") {
                    ((BoundedRangeModel) propertyChangeEvent.getOldValue()).removeChangeListener(BasicSliderUI.this.changeListener);
                    ((BoundedRangeModel) propertyChangeEvent.getNewValue()).addChangeListener(BasicSliderUI.this.changeListener);
                    BasicSliderUI.this.calculateThumbLocation();
                    BasicSliderUI.this.slider.repaint();
                }
            }
        }
    }

    /* loaded from: rt.jar:javax/swing/plaf/basic/BasicSliderUI$ChangeHandler.class */
    public class ChangeHandler implements ChangeListener {
        public ChangeHandler() {
        }

        @Override // javax.swing.event.ChangeListener
        public void stateChanged(ChangeEvent changeEvent) {
            BasicSliderUI.this.getHandler().stateChanged(changeEvent);
        }
    }

    /* loaded from: rt.jar:javax/swing/plaf/basic/BasicSliderUI$TrackListener.class */
    public class TrackListener extends MouseInputAdapter {
        protected transient int offset;
        protected transient int currentMouseX;
        protected transient int currentMouseY;

        public TrackListener() {
        }

        @Override // java.awt.event.MouseAdapter, java.awt.event.MouseListener
        public void mouseReleased(MouseEvent mouseEvent) {
            if (!BasicSliderUI.this.slider.isEnabled()) {
                return;
            }
            this.offset = 0;
            BasicSliderUI.this.scrollTimer.stop();
            BasicSliderUI.this.isDragging = false;
            BasicSliderUI.this.slider.setValueIsAdjusting(false);
            BasicSliderUI.this.slider.repaint();
        }

        @Override // java.awt.event.MouseAdapter, java.awt.event.MouseListener
        public void mousePressed(MouseEvent mouseEvent) {
            if (!BasicSliderUI.this.slider.isEnabled()) {
                return;
            }
            BasicSliderUI.this.calculateGeometry();
            this.currentMouseX = mouseEvent.getX();
            this.currentMouseY = mouseEvent.getY();
            if (BasicSliderUI.this.slider.isRequestFocusEnabled()) {
                BasicSliderUI.this.slider.requestFocus();
            }
            if (BasicSliderUI.this.thumbRect.contains(this.currentMouseX, this.currentMouseY)) {
                if (UIManager.getBoolean("Slider.onlyLeftMouseButtonDrag") && !SwingUtilities.isLeftMouseButton(mouseEvent)) {
                    return;
                }
                switch (BasicSliderUI.this.slider.getOrientation()) {
                    case 0:
                        this.offset = this.currentMouseX - BasicSliderUI.this.thumbRect.f12372x;
                        break;
                    case 1:
                        this.offset = this.currentMouseY - BasicSliderUI.this.thumbRect.f12373y;
                        break;
                }
                BasicSliderUI.this.isDragging = true;
                return;
            }
            if (SwingUtilities.isLeftMouseButton(mouseEvent)) {
                BasicSliderUI.this.isDragging = false;
                BasicSliderUI.this.slider.setValueIsAdjusting(true);
                Dimension size = BasicSliderUI.this.slider.getSize();
                int i2 = 1;
                switch (BasicSliderUI.this.slider.getOrientation()) {
                    case 0:
                        if (BasicSliderUI.this.thumbRect.isEmpty()) {
                            int i3 = size.width / 2;
                            if (!BasicSliderUI.this.drawInverted()) {
                                i2 = this.currentMouseX < i3 ? -1 : 1;
                                break;
                            } else {
                                i2 = this.currentMouseX < i3 ? 1 : -1;
                                break;
                            }
                        } else {
                            int i4 = BasicSliderUI.this.thumbRect.f12372x;
                            if (!BasicSliderUI.this.drawInverted()) {
                                i2 = this.currentMouseX < i4 ? -1 : 1;
                                break;
                            } else {
                                i2 = this.currentMouseX < i4 ? 1 : -1;
                                break;
                            }
                        }
                    case 1:
                        if (BasicSliderUI.this.thumbRect.isEmpty()) {
                            int i5 = size.height / 2;
                            if (!BasicSliderUI.this.drawInverted()) {
                                i2 = this.currentMouseY < i5 ? 1 : -1;
                                break;
                            } else {
                                i2 = this.currentMouseY < i5 ? -1 : 1;
                                break;
                            }
                        } else {
                            int i6 = BasicSliderUI.this.thumbRect.f12373y;
                            if (!BasicSliderUI.this.drawInverted()) {
                                i2 = this.currentMouseY < i6 ? 1 : -1;
                                break;
                            } else {
                                i2 = this.currentMouseY < i6 ? -1 : 1;
                                break;
                            }
                        }
                }
                if (shouldScroll(i2)) {
                    BasicSliderUI.this.scrollDueToClickInTrack(i2);
                }
                if (shouldScroll(i2)) {
                    BasicSliderUI.this.scrollTimer.stop();
                    BasicSliderUI.this.scrollListener.setDirection(i2);
                    BasicSliderUI.this.scrollTimer.start();
                }
            }
        }

        /* JADX WARN: Code restructure failed: missing block: B:12:0x0033, code lost:
        
            if (r0.f12373y > r3.currentMouseY) goto L34;
         */
        /* JADX WARN: Code restructure failed: missing block: B:13:0x0036, code lost:
        
            return false;
         */
        /* JADX WARN: Code restructure failed: missing block: B:27:0x006c, code lost:
        
            if ((r0.f12372x + r0.width) < r3.currentMouseX) goto L34;
         */
        /* JADX WARN: Code restructure failed: missing block: B:28:0x006f, code lost:
        
            return false;
         */
        /* JADX WARN: Removed duplicated region for block: B:17:0x0048 A[RETURN] */
        /* JADX WARN: Removed duplicated region for block: B:32:0x007c A[RETURN] */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct code enable 'Show inconsistent code' option in preferences
        */
        public boolean shouldScroll(int r4) {
            /*
                r3 = this;
                r0 = r3
                javax.swing.plaf.basic.BasicSliderUI r0 = javax.swing.plaf.basic.BasicSliderUI.this
                java.awt.Rectangle r0 = r0.thumbRect
                r5 = r0
                r0 = r3
                javax.swing.plaf.basic.BasicSliderUI r0 = javax.swing.plaf.basic.BasicSliderUI.this
                javax.swing.JSlider r0 = r0.slider
                int r0 = r0.getOrientation()
                r1 = 1
                if (r0 != r1) goto L4a
                r0 = r3
                javax.swing.plaf.basic.BasicSliderUI r0 = javax.swing.plaf.basic.BasicSliderUI.this
                boolean r0 = r0.drawInverted()
                if (r0 == 0) goto L27
                r0 = r4
                if (r0 >= 0) goto L38
                goto L2b
            L27:
                r0 = r4
                if (r0 <= 0) goto L38
            L2b:
                r0 = r5
                int r0 = r0.f12373y
                r1 = r3
                int r1 = r1.currentMouseY
                if (r0 > r1) goto L7e
                r0 = 0
                return r0
            L38:
                r0 = r5
                int r0 = r0.f12373y
                r1 = r5
                int r1 = r1.height
                int r0 = r0 + r1
                r1 = r3
                int r1 = r1.currentMouseY
                if (r0 < r1) goto L7e
                r0 = 0
                return r0
            L4a:
                r0 = r3
                javax.swing.plaf.basic.BasicSliderUI r0 = javax.swing.plaf.basic.BasicSliderUI.this
                boolean r0 = r0.drawInverted()
                if (r0 == 0) goto L5b
                r0 = r4
                if (r0 >= 0) goto L71
                goto L5f
            L5b:
                r0 = r4
                if (r0 <= 0) goto L71
            L5f:
                r0 = r5
                int r0 = r0.f12372x
                r1 = r5
                int r1 = r1.width
                int r0 = r0 + r1
                r1 = r3
                int r1 = r1.currentMouseX
                if (r0 < r1) goto L7e
                r0 = 0
                return r0
            L71:
                r0 = r5
                int r0 = r0.f12372x
                r1 = r3
                int r1 = r1.currentMouseX
                if (r0 > r1) goto L7e
                r0 = 0
                return r0
            L7e:
                r0 = r4
                if (r0 <= 0) goto La6
                r0 = r3
                javax.swing.plaf.basic.BasicSliderUI r0 = javax.swing.plaf.basic.BasicSliderUI.this
                javax.swing.JSlider r0 = r0.slider
                int r0 = r0.getValue()
                r1 = r3
                javax.swing.plaf.basic.BasicSliderUI r1 = javax.swing.plaf.basic.BasicSliderUI.this
                javax.swing.JSlider r1 = r1.slider
                int r1 = r1.getExtent()
                int r0 = r0 + r1
                r1 = r3
                javax.swing.plaf.basic.BasicSliderUI r1 = javax.swing.plaf.basic.BasicSliderUI.this
                javax.swing.JSlider r1 = r1.slider
                int r1 = r1.getMaximum()
                if (r0 < r1) goto La6
                r0 = 0
                return r0
            La6:
                r0 = r4
                if (r0 >= 0) goto Lc3
                r0 = r3
                javax.swing.plaf.basic.BasicSliderUI r0 = javax.swing.plaf.basic.BasicSliderUI.this
                javax.swing.JSlider r0 = r0.slider
                int r0 = r0.getValue()
                r1 = r3
                javax.swing.plaf.basic.BasicSliderUI r1 = javax.swing.plaf.basic.BasicSliderUI.this
                javax.swing.JSlider r1 = r1.slider
                int r1 = r1.getMinimum()
                if (r0 > r1) goto Lc3
                r0 = 0
                return r0
            Lc3:
                r0 = 1
                return r0
            */
            throw new UnsupportedOperationException("Method not decompiled: javax.swing.plaf.basic.BasicSliderUI.TrackListener.shouldScroll(int):boolean");
        }

        @Override // java.awt.event.MouseAdapter, java.awt.event.MouseMotionListener
        public void mouseDragged(MouseEvent mouseEvent) {
            if (!BasicSliderUI.this.slider.isEnabled()) {
            }
            this.currentMouseX = mouseEvent.getX();
            this.currentMouseY = mouseEvent.getY();
            if (!BasicSliderUI.this.isDragging) {
                return;
            }
            BasicSliderUI.this.slider.setValueIsAdjusting(true);
            switch (BasicSliderUI.this.slider.getOrientation()) {
                case 0:
                    int i2 = BasicSliderUI.this.thumbRect.width / 2;
                    int x2 = mouseEvent.getX() - this.offset;
                    int i3 = BasicSliderUI.this.trackRect.f12372x;
                    int i4 = BasicSliderUI.this.trackRect.f12372x + (BasicSliderUI.this.trackRect.width - 1);
                    int iXPositionForValue = BasicSliderUI.this.xPositionForValue(BasicSliderUI.this.slider.getMaximum() - BasicSliderUI.this.slider.getExtent());
                    if (BasicSliderUI.this.drawInverted()) {
                        i3 = iXPositionForValue;
                    } else {
                        i4 = iXPositionForValue;
                    }
                    int iMin = Math.min(Math.max(x2, i3 - i2), i4 - i2);
                    BasicSliderUI.this.setThumbLocation(iMin, BasicSliderUI.this.thumbRect.f12373y);
                    BasicSliderUI.this.slider.setValue(BasicSliderUI.this.valueForXPosition(iMin + i2));
                    break;
                case 1:
                    int i5 = BasicSliderUI.this.thumbRect.height / 2;
                    int y2 = mouseEvent.getY() - this.offset;
                    int i6 = BasicSliderUI.this.trackRect.f12373y;
                    int i7 = BasicSliderUI.this.trackRect.f12373y + (BasicSliderUI.this.trackRect.height - 1);
                    int iYPositionForValue = BasicSliderUI.this.yPositionForValue(BasicSliderUI.this.slider.getMaximum() - BasicSliderUI.this.slider.getExtent());
                    if (BasicSliderUI.this.drawInverted()) {
                        i7 = iYPositionForValue;
                    } else {
                        i6 = iYPositionForValue;
                    }
                    int iMin2 = Math.min(Math.max(y2, i6 - i5), i7 - i5);
                    BasicSliderUI.this.setThumbLocation(BasicSliderUI.this.thumbRect.f12372x, iMin2);
                    BasicSliderUI.this.slider.setValue(BasicSliderUI.this.valueForYPosition(iMin2 + i5));
                    break;
            }
        }

        @Override // java.awt.event.MouseAdapter, java.awt.event.MouseMotionListener
        public void mouseMoved(MouseEvent mouseEvent) {
        }
    }

    /* loaded from: rt.jar:javax/swing/plaf/basic/BasicSliderUI$ScrollListener.class */
    public class ScrollListener implements ActionListener {
        int direction;
        boolean useBlockIncrement;

        public ScrollListener() {
            this.direction = 1;
            this.direction = 1;
            this.useBlockIncrement = true;
        }

        public ScrollListener(int i2, boolean z2) {
            this.direction = 1;
            this.direction = i2;
            this.useBlockIncrement = z2;
        }

        public void setDirection(int i2) {
            this.direction = i2;
        }

        public void setScrollByBlock(boolean z2) {
            this.useBlockIncrement = z2;
        }

        @Override // java.awt.event.ActionListener
        public void actionPerformed(ActionEvent actionEvent) {
            if (this.useBlockIncrement) {
                BasicSliderUI.this.scrollByBlock(this.direction);
            } else {
                BasicSliderUI.this.scrollByUnit(this.direction);
            }
            if (!BasicSliderUI.this.trackListener.shouldScroll(this.direction)) {
                ((Timer) actionEvent.getSource()).stop();
            }
        }
    }

    /* loaded from: rt.jar:javax/swing/plaf/basic/BasicSliderUI$ComponentHandler.class */
    public class ComponentHandler extends ComponentAdapter {
        public ComponentHandler() {
        }

        @Override // java.awt.event.ComponentAdapter, java.awt.event.ComponentListener
        public void componentResized(ComponentEvent componentEvent) {
            BasicSliderUI.this.getHandler().componentResized(componentEvent);
        }
    }

    /* loaded from: rt.jar:javax/swing/plaf/basic/BasicSliderUI$FocusHandler.class */
    public class FocusHandler implements FocusListener {
        public FocusHandler() {
        }

        @Override // java.awt.event.FocusListener
        public void focusGained(FocusEvent focusEvent) {
            BasicSliderUI.this.getHandler().focusGained(focusEvent);
        }

        @Override // java.awt.event.FocusListener
        public void focusLost(FocusEvent focusEvent) {
            BasicSliderUI.this.getHandler().focusLost(focusEvent);
        }
    }

    /* loaded from: rt.jar:javax/swing/plaf/basic/BasicSliderUI$ActionScroller.class */
    public class ActionScroller extends AbstractAction {
        int dir;
        boolean block;
        JSlider slider;

        public ActionScroller(JSlider jSlider, int i2, boolean z2) {
            this.dir = i2;
            this.block = z2;
            this.slider = jSlider;
        }

        @Override // java.awt.event.ActionListener
        public void actionPerformed(ActionEvent actionEvent) {
            BasicSliderUI.SHARED_ACTION.scroll(this.slider, BasicSliderUI.this, this.dir, this.block);
        }

        @Override // javax.swing.AbstractAction, javax.swing.Action
        public boolean isEnabled() {
            boolean zIsEnabled = true;
            if (this.slider != null) {
                zIsEnabled = this.slider.isEnabled();
            }
            return zIsEnabled;
        }
    }

    /* loaded from: rt.jar:javax/swing/plaf/basic/BasicSliderUI$SharedActionScroller.class */
    static class SharedActionScroller extends AbstractAction {
        int dir;
        boolean block;

        public SharedActionScroller(int i2, boolean z2) {
            this.dir = i2;
            this.block = z2;
        }

        @Override // java.awt.event.ActionListener
        public void actionPerformed(ActionEvent actionEvent) {
            JSlider jSlider = (JSlider) actionEvent.getSource();
            BasicSliderUI basicSliderUI = (BasicSliderUI) BasicLookAndFeel.getUIOfType(jSlider.getUI(), BasicSliderUI.class);
            if (basicSliderUI == null) {
                return;
            }
            BasicSliderUI.SHARED_ACTION.scroll(jSlider, basicSliderUI, this.dir, this.block);
        }
    }

    /* loaded from: rt.jar:javax/swing/plaf/basic/BasicSliderUI$Actions.class */
    private static class Actions extends UIAction {
        public static final String POSITIVE_UNIT_INCREMENT = "positiveUnitIncrement";
        public static final String POSITIVE_BLOCK_INCREMENT = "positiveBlockIncrement";
        public static final String NEGATIVE_UNIT_INCREMENT = "negativeUnitIncrement";
        public static final String NEGATIVE_BLOCK_INCREMENT = "negativeBlockIncrement";
        public static final String MIN_SCROLL_INCREMENT = "minScroll";
        public static final String MAX_SCROLL_INCREMENT = "maxScroll";

        Actions() {
            super(null);
        }

        public Actions(String str) {
            super(str);
        }

        @Override // java.awt.event.ActionListener
        public void actionPerformed(ActionEvent actionEvent) {
            JSlider jSlider = (JSlider) actionEvent.getSource();
            BasicSliderUI basicSliderUI = (BasicSliderUI) BasicLookAndFeel.getUIOfType(jSlider.getUI(), BasicSliderUI.class);
            String name = getName();
            if (basicSliderUI == null) {
                return;
            }
            if (POSITIVE_UNIT_INCREMENT == name) {
                scroll(jSlider, basicSliderUI, 1, false);
                return;
            }
            if (NEGATIVE_UNIT_INCREMENT == name) {
                scroll(jSlider, basicSliderUI, -1, false);
                return;
            }
            if (POSITIVE_BLOCK_INCREMENT == name) {
                scroll(jSlider, basicSliderUI, 1, true);
                return;
            }
            if (NEGATIVE_BLOCK_INCREMENT == name) {
                scroll(jSlider, basicSliderUI, -1, true);
            } else if (MIN_SCROLL_INCREMENT == name) {
                scroll(jSlider, basicSliderUI, -2, false);
            } else if (MAX_SCROLL_INCREMENT == name) {
                scroll(jSlider, basicSliderUI, 2, false);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void scroll(JSlider jSlider, BasicSliderUI basicSliderUI, int i2, boolean z2) {
            boolean inverted = jSlider.getInverted();
            if (i2 == -1 || i2 == 1) {
                if (inverted) {
                    i2 = i2 == 1 ? -1 : 1;
                }
                if (z2) {
                    basicSliderUI.scrollByBlock(i2);
                    return;
                } else {
                    basicSliderUI.scrollByUnit(i2);
                    return;
                }
            }
            if (inverted) {
                i2 = i2 == -2 ? 2 : -2;
            }
            jSlider.setValue(i2 == -2 ? jSlider.getMinimum() : jSlider.getMaximum());
        }
    }
}
