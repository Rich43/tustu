package javax.swing.plaf.basic;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.BoundedRangeModel;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JViewport;
import javax.swing.LookAndFeel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.ScrollBarUI;
import javax.swing.plaf.UIResource;
import javax.swing.plaf.basic.BasicSliderUI;
import javax.swing.plaf.nimbus.NimbusStyle;
import sun.swing.DefaultLookup;
import sun.swing.SwingUtilities2;
import sun.swing.UIAction;

/* loaded from: rt.jar:javax/swing/plaf/basic/BasicScrollBarUI.class */
public class BasicScrollBarUI extends ScrollBarUI implements LayoutManager, SwingConstants {
    private static final int POSITIVE_SCROLL = 1;
    private static final int NEGATIVE_SCROLL = -1;
    private static final int MIN_SCROLL = 2;
    private static final int MAX_SCROLL = 3;
    protected Dimension minimumThumbSize;
    protected Dimension maximumThumbSize;
    protected Color thumbHighlightColor;
    protected Color thumbLightShadowColor;
    protected Color thumbDarkShadowColor;
    protected Color thumbColor;
    protected Color trackColor;
    protected Color trackHighlightColor;
    protected JScrollBar scrollbar;
    protected JButton incrButton;
    protected JButton decrButton;
    protected boolean isDragging;
    protected TrackListener trackListener;
    protected ArrowButtonListener buttonListener;
    protected ModelListener modelListener;
    protected Rectangle thumbRect;
    protected Rectangle trackRect;
    protected int trackHighlight;
    protected static final int NO_HIGHLIGHT = 0;
    protected static final int DECREASE_HIGHLIGHT = 1;
    protected static final int INCREASE_HIGHLIGHT = 2;
    protected ScrollListener scrollListener;
    protected PropertyChangeListener propertyChangeListener;
    protected Timer scrollTimer;
    private static final int scrollSpeedThrottle = 60;
    private boolean supportsAbsolutePositioning;
    protected int scrollBarWidth;
    private Handler handler;
    private boolean thumbActive;
    private boolean useCachedValue = false;
    private int scrollBarValue;
    protected int incrGap;
    protected int decrGap;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !BasicScrollBarUI.class.desiredAssertionStatus();
    }

    static void loadActionMap(LazyActionMap lazyActionMap) {
        lazyActionMap.put(new Actions(BasicSliderUI.Actions.POSITIVE_UNIT_INCREMENT));
        lazyActionMap.put(new Actions(BasicSliderUI.Actions.POSITIVE_BLOCK_INCREMENT));
        lazyActionMap.put(new Actions(BasicSliderUI.Actions.NEGATIVE_UNIT_INCREMENT));
        lazyActionMap.put(new Actions(BasicSliderUI.Actions.NEGATIVE_BLOCK_INCREMENT));
        lazyActionMap.put(new Actions(BasicSliderUI.Actions.MIN_SCROLL_INCREMENT));
        lazyActionMap.put(new Actions(BasicSliderUI.Actions.MAX_SCROLL_INCREMENT));
    }

    public static ComponentUI createUI(JComponent jComponent) {
        return new BasicScrollBarUI();
    }

    protected void configureScrollBarColors() {
        LookAndFeel.installColors(this.scrollbar, "ScrollBar.background", "ScrollBar.foreground");
        this.thumbHighlightColor = UIManager.getColor("ScrollBar.thumbHighlight");
        this.thumbLightShadowColor = UIManager.getColor("ScrollBar.thumbShadow");
        this.thumbDarkShadowColor = UIManager.getColor("ScrollBar.thumbDarkShadow");
        this.thumbColor = UIManager.getColor("ScrollBar.thumb");
        this.trackColor = UIManager.getColor("ScrollBar.track");
        this.trackHighlightColor = UIManager.getColor("ScrollBar.trackHighlight");
    }

    @Override // javax.swing.plaf.ComponentUI
    public void installUI(JComponent jComponent) {
        this.scrollbar = (JScrollBar) jComponent;
        this.thumbRect = new Rectangle(0, 0, 0, 0);
        this.trackRect = new Rectangle(0, 0, 0, 0);
        installDefaults();
        installComponents();
        installListeners();
        installKeyboardActions();
    }

    @Override // javax.swing.plaf.ComponentUI
    public void uninstallUI(JComponent jComponent) {
        this.scrollbar = (JScrollBar) jComponent;
        uninstallListeners();
        uninstallDefaults();
        uninstallComponents();
        uninstallKeyboardActions();
        this.thumbRect = null;
        this.scrollbar = null;
        this.incrButton = null;
        this.decrButton = null;
    }

    protected void installDefaults() {
        this.scrollBarWidth = UIManager.getInt("ScrollBar.width");
        if (this.scrollBarWidth <= 0) {
            this.scrollBarWidth = 16;
        }
        this.minimumThumbSize = (Dimension) UIManager.get("ScrollBar.minimumThumbSize");
        this.maximumThumbSize = (Dimension) UIManager.get("ScrollBar.maximumThumbSize");
        Boolean bool = (Boolean) UIManager.get("ScrollBar.allowsAbsolutePositioning");
        this.supportsAbsolutePositioning = bool != null ? bool.booleanValue() : false;
        this.trackHighlight = 0;
        if (this.scrollbar.getLayout() == null || (this.scrollbar.getLayout() instanceof UIResource)) {
            this.scrollbar.setLayout(this);
        }
        configureScrollBarColors();
        LookAndFeel.installBorder(this.scrollbar, "ScrollBar.border");
        LookAndFeel.installProperty(this.scrollbar, "opaque", Boolean.TRUE);
        this.scrollBarValue = this.scrollbar.getValue();
        this.incrGap = UIManager.getInt("ScrollBar.incrementButtonGap");
        this.decrGap = UIManager.getInt("ScrollBar.decrementButtonGap");
        String str = (String) this.scrollbar.getClientProperty("JComponent.sizeVariant");
        if (str != null) {
            if (NimbusStyle.LARGE_KEY.equals(str)) {
                this.scrollBarWidth = (int) (this.scrollBarWidth * 1.15d);
                this.incrGap = (int) (this.incrGap * 1.15d);
                this.decrGap = (int) (this.decrGap * 1.15d);
            } else if (NimbusStyle.SMALL_KEY.equals(str)) {
                this.scrollBarWidth = (int) (this.scrollBarWidth * 0.857d);
                this.incrGap = (int) (this.incrGap * 0.857d);
                this.decrGap = (int) (this.decrGap * 0.714d);
            } else if (NimbusStyle.MINI_KEY.equals(str)) {
                this.scrollBarWidth = (int) (this.scrollBarWidth * 0.714d);
                this.incrGap = (int) (this.incrGap * 0.714d);
                this.decrGap = (int) (this.decrGap * 0.714d);
            }
        }
    }

    protected void installComponents() {
        switch (this.scrollbar.getOrientation()) {
            case 0:
                if (this.scrollbar.getComponentOrientation().isLeftToRight()) {
                    this.incrButton = createIncreaseButton(3);
                    this.decrButton = createDecreaseButton(7);
                    break;
                } else {
                    this.incrButton = createIncreaseButton(7);
                    this.decrButton = createDecreaseButton(3);
                    break;
                }
            case 1:
                this.incrButton = createIncreaseButton(5);
                this.decrButton = createDecreaseButton(1);
                break;
        }
        this.scrollbar.add(this.incrButton);
        this.scrollbar.add(this.decrButton);
        this.scrollbar.setEnabled(this.scrollbar.isEnabled());
    }

    protected void uninstallComponents() {
        this.scrollbar.remove(this.incrButton);
        this.scrollbar.remove(this.decrButton);
    }

    protected void installListeners() {
        this.trackListener = createTrackListener();
        this.buttonListener = createArrowButtonListener();
        this.modelListener = createModelListener();
        this.propertyChangeListener = createPropertyChangeListener();
        this.scrollbar.addMouseListener(this.trackListener);
        this.scrollbar.addMouseMotionListener(this.trackListener);
        this.scrollbar.getModel().addChangeListener(this.modelListener);
        this.scrollbar.addPropertyChangeListener(this.propertyChangeListener);
        this.scrollbar.addFocusListener(getHandler());
        if (this.incrButton != null) {
            this.incrButton.addMouseListener(this.buttonListener);
        }
        if (this.decrButton != null) {
            this.decrButton.addMouseListener(this.buttonListener);
        }
        this.scrollListener = createScrollListener();
        this.scrollTimer = new Timer(60, this.scrollListener);
        this.scrollTimer.setInitialDelay(300);
    }

    protected void installKeyboardActions() {
        LazyActionMap.installLazyActionMap(this.scrollbar, BasicScrollBarUI.class, "ScrollBar.actionMap");
        SwingUtilities.replaceUIInputMap(this.scrollbar, 0, getInputMap(0));
        SwingUtilities.replaceUIInputMap(this.scrollbar, 1, getInputMap(1));
    }

    protected void uninstallKeyboardActions() {
        SwingUtilities.replaceUIInputMap(this.scrollbar, 0, null);
        SwingUtilities.replaceUIActionMap(this.scrollbar, null);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public InputMap getInputMap(int i2) {
        InputMap inputMap;
        InputMap inputMap2;
        if (i2 == 0) {
            InputMap inputMap3 = (InputMap) DefaultLookup.get(this.scrollbar, this, "ScrollBar.focusInputMap");
            if (this.scrollbar.getComponentOrientation().isLeftToRight() || (inputMap2 = (InputMap) DefaultLookup.get(this.scrollbar, this, "ScrollBar.focusInputMap.RightToLeft")) == null) {
                return inputMap3;
            }
            inputMap2.setParent(inputMap3);
            return inputMap2;
        }
        if (i2 == 1) {
            InputMap inputMap4 = (InputMap) DefaultLookup.get(this.scrollbar, this, "ScrollBar.ancestorInputMap");
            if (this.scrollbar.getComponentOrientation().isLeftToRight() || (inputMap = (InputMap) DefaultLookup.get(this.scrollbar, this, "ScrollBar.ancestorInputMap.RightToLeft")) == null) {
                return inputMap4;
            }
            inputMap.setParent(inputMap4);
            return inputMap;
        }
        return null;
    }

    protected void uninstallListeners() {
        this.scrollTimer.stop();
        this.scrollTimer = null;
        if (this.decrButton != null) {
            this.decrButton.removeMouseListener(this.buttonListener);
        }
        if (this.incrButton != null) {
            this.incrButton.removeMouseListener(this.buttonListener);
        }
        this.scrollbar.getModel().removeChangeListener(this.modelListener);
        this.scrollbar.removeMouseListener(this.trackListener);
        this.scrollbar.removeMouseMotionListener(this.trackListener);
        this.scrollbar.removePropertyChangeListener(this.propertyChangeListener);
        this.scrollbar.removeFocusListener(getHandler());
        this.handler = null;
    }

    protected void uninstallDefaults() {
        LookAndFeel.uninstallBorder(this.scrollbar);
        if (this.scrollbar.getLayout() == this) {
            this.scrollbar.setLayout(null);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Handler getHandler() {
        if (this.handler == null) {
            this.handler = new Handler();
        }
        return this.handler;
    }

    protected TrackListener createTrackListener() {
        return new TrackListener();
    }

    protected ArrowButtonListener createArrowButtonListener() {
        return new ArrowButtonListener();
    }

    protected ModelListener createModelListener() {
        return new ModelListener();
    }

    protected ScrollListener createScrollListener() {
        return new ScrollListener();
    }

    protected PropertyChangeListener createPropertyChangeListener() {
        return getHandler();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateThumbState(int i2, int i3) {
        setThumbRollover(getThumbBounds().contains(i2, i3));
    }

    protected void setThumbRollover(boolean z2) {
        if (this.thumbActive != z2) {
            this.thumbActive = z2;
            this.scrollbar.repaint(getThumbBounds());
        }
    }

    public boolean isThumbRollover() {
        return this.thumbActive;
    }

    @Override // javax.swing.plaf.ComponentUI
    public void paint(Graphics graphics, JComponent jComponent) {
        paintTrack(graphics, jComponent, getTrackBounds());
        Rectangle thumbBounds = getThumbBounds();
        if (thumbBounds.intersects(graphics.getClipBounds())) {
            paintThumb(graphics, jComponent, thumbBounds);
        }
    }

    @Override // javax.swing.plaf.ComponentUI
    public Dimension getPreferredSize(JComponent jComponent) {
        return this.scrollbar.getOrientation() == 1 ? new Dimension(this.scrollBarWidth, 48) : new Dimension(48, this.scrollBarWidth);
    }

    @Override // javax.swing.plaf.ComponentUI
    public Dimension getMaximumSize(JComponent jComponent) {
        return new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE);
    }

    protected JButton createDecreaseButton(int i2) {
        return new BasicArrowButton(i2, UIManager.getColor("ScrollBar.thumb"), UIManager.getColor("ScrollBar.thumbShadow"), UIManager.getColor("ScrollBar.thumbDarkShadow"), UIManager.getColor("ScrollBar.thumbHighlight"));
    }

    protected JButton createIncreaseButton(int i2) {
        return new BasicArrowButton(i2, UIManager.getColor("ScrollBar.thumb"), UIManager.getColor("ScrollBar.thumbShadow"), UIManager.getColor("ScrollBar.thumbDarkShadow"), UIManager.getColor("ScrollBar.thumbHighlight"));
    }

    protected void paintDecreaseHighlight(Graphics graphics) {
        int i2;
        int i3;
        Insets insets = this.scrollbar.getInsets();
        Rectangle thumbBounds = getThumbBounds();
        graphics.setColor(this.trackHighlightColor);
        if (this.scrollbar.getOrientation() == 1) {
            int i4 = insets.left;
            int i5 = this.trackRect.f12373y;
            graphics.fillRect(i4, i5, this.scrollbar.getWidth() - (insets.left + insets.right), thumbBounds.f12373y - i5);
            return;
        }
        if (this.scrollbar.getComponentOrientation().isLeftToRight()) {
            i2 = this.trackRect.f12372x;
            i3 = thumbBounds.f12372x - i2;
        } else {
            i2 = thumbBounds.f12372x + thumbBounds.width;
            i3 = (this.trackRect.f12372x + this.trackRect.width) - i2;
        }
        graphics.fillRect(i2, insets.top, i3, this.scrollbar.getHeight() - (insets.top + insets.bottom));
    }

    protected void paintIncreaseHighlight(Graphics graphics) {
        int i2;
        int i3;
        Insets insets = this.scrollbar.getInsets();
        Rectangle thumbBounds = getThumbBounds();
        graphics.setColor(this.trackHighlightColor);
        if (this.scrollbar.getOrientation() == 1) {
            int i4 = insets.left;
            int i5 = thumbBounds.f12373y + thumbBounds.height;
            graphics.fillRect(i4, i5, this.scrollbar.getWidth() - (insets.left + insets.right), (this.trackRect.f12373y + this.trackRect.height) - i5);
            return;
        }
        if (this.scrollbar.getComponentOrientation().isLeftToRight()) {
            i2 = thumbBounds.f12372x + thumbBounds.width;
            i3 = (this.trackRect.f12372x + this.trackRect.width) - i2;
        } else {
            i2 = this.trackRect.f12372x;
            i3 = thumbBounds.f12372x - i2;
        }
        graphics.fillRect(i2, insets.top, i3, this.scrollbar.getHeight() - (insets.top + insets.bottom));
    }

    protected void paintTrack(Graphics graphics, JComponent jComponent, Rectangle rectangle) {
        graphics.setColor(this.trackColor);
        graphics.fillRect(rectangle.f12372x, rectangle.f12373y, rectangle.width, rectangle.height);
        if (this.trackHighlight == 1) {
            paintDecreaseHighlight(graphics);
        } else if (this.trackHighlight == 2) {
            paintIncreaseHighlight(graphics);
        }
    }

    protected void paintThumb(Graphics graphics, JComponent jComponent, Rectangle rectangle) {
        if (rectangle.isEmpty() || !this.scrollbar.isEnabled()) {
            return;
        }
        int i2 = rectangle.width;
        int i3 = rectangle.height;
        graphics.translate(rectangle.f12372x, rectangle.f12373y);
        graphics.setColor(this.thumbDarkShadowColor);
        SwingUtilities2.drawRect(graphics, 0, 0, i2 - 1, i3 - 1);
        graphics.setColor(this.thumbColor);
        graphics.fillRect(0, 0, i2 - 1, i3 - 1);
        graphics.setColor(this.thumbHighlightColor);
        SwingUtilities2.drawVLine(graphics, 1, 1, i3 - 2);
        SwingUtilities2.drawHLine(graphics, 2, i2 - 3, 1);
        graphics.setColor(this.thumbLightShadowColor);
        SwingUtilities2.drawHLine(graphics, 2, i2 - 2, i3 - 2);
        SwingUtilities2.drawVLine(graphics, i2 - 2, 1, i3 - 3);
        graphics.translate(-rectangle.f12372x, -rectangle.f12373y);
    }

    protected Dimension getMinimumThumbSize() {
        return this.minimumThumbSize;
    }

    protected Dimension getMaximumThumbSize() {
        return this.maximumThumbSize;
    }

    @Override // java.awt.LayoutManager
    public void addLayoutComponent(String str, Component component) {
    }

    @Override // java.awt.LayoutManager
    public void removeLayoutComponent(Component component) {
    }

    @Override // java.awt.LayoutManager
    public Dimension preferredLayoutSize(Container container) {
        return getPreferredSize((JComponent) container);
    }

    @Override // java.awt.LayoutManager
    public Dimension minimumLayoutSize(Container container) {
        return getMinimumSize((JComponent) container);
    }

    private int getValue(JScrollBar jScrollBar) {
        return this.useCachedValue ? this.scrollBarValue : jScrollBar.getValue();
    }

    protected void layoutVScrollbar(JScrollBar jScrollBar) {
        Dimension size = jScrollBar.getSize();
        Insets insets = jScrollBar.getInsets();
        int i2 = size.width - (insets.left + insets.right);
        int i3 = insets.left;
        boolean z2 = DefaultLookup.getBoolean(this.scrollbar, this, "ScrollBar.squareButtons", false);
        int i4 = z2 ? i2 : this.decrButton.getPreferredSize().height;
        int i5 = insets.top;
        int i6 = z2 ? i2 : this.incrButton.getPreferredSize().height;
        int i7 = size.height - (insets.bottom + i6);
        int i8 = insets.top + insets.bottom;
        int i9 = i4 + i6;
        float f2 = (size.height - (i8 + i9)) - (this.decrGap + this.incrGap);
        float minimum = jScrollBar.getMinimum();
        float visibleAmount = jScrollBar.getVisibleAmount();
        float maximum = jScrollBar.getMaximum() - minimum;
        float value = getValue(jScrollBar);
        int iMin = Math.min(Math.max(maximum <= 0.0f ? getMaximumThumbSize().height : (int) (f2 * (visibleAmount / maximum)), getMinimumThumbSize().height), getMaximumThumbSize().height);
        int i10 = (i7 - this.incrGap) - iMin;
        if (value < jScrollBar.getMaximum() - jScrollBar.getVisibleAmount()) {
            i10 = ((int) (0.5f + ((f2 - iMin) * ((value - minimum) / (maximum - visibleAmount))))) + i5 + i4 + this.decrGap;
        }
        int i11 = size.height - i8;
        if (i11 < i9) {
            int i12 = i11 / 2;
            i4 = i12;
            i6 = i12;
            i7 = size.height - (insets.bottom + i6);
        }
        this.decrButton.setBounds(i3, i5, i2, i4);
        this.incrButton.setBounds(i3, i7, i2, i6);
        int i13 = i5 + i4 + this.decrGap;
        int i14 = (i7 - this.incrGap) - i13;
        this.trackRect.setBounds(i3, i13, i2, i14);
        if (iMin >= ((int) f2)) {
            if (UIManager.getBoolean("ScrollBar.alwaysShowThumb")) {
                setThumbBounds(i3, i13, i2, i14);
                return;
            } else {
                setThumbBounds(0, 0, 0, 0);
                return;
            }
        }
        if (i10 + iMin > i7 - this.incrGap) {
            i10 = (i7 - this.incrGap) - iMin;
        }
        if (i10 < i5 + i4 + this.decrGap) {
            i10 = i5 + i4 + this.decrGap + 1;
        }
        setThumbBounds(i3, i10, i2, iMin);
    }

    protected void layoutHScrollbar(JScrollBar jScrollBar) {
        int i2;
        Dimension size = jScrollBar.getSize();
        Insets insets = jScrollBar.getInsets();
        int i3 = size.height - (insets.top + insets.bottom);
        int i4 = insets.top;
        boolean zIsLeftToRight = jScrollBar.getComponentOrientation().isLeftToRight();
        boolean z2 = DefaultLookup.getBoolean(this.scrollbar, this, "ScrollBar.squareButtons", false);
        int i5 = z2 ? i3 : this.decrButton.getPreferredSize().width;
        int i6 = z2 ? i3 : this.incrButton.getPreferredSize().width;
        if (!zIsLeftToRight) {
            i5 = i6;
            i6 = i5;
        }
        int i7 = insets.left;
        int i8 = size.width - (insets.right + i6);
        int i9 = zIsLeftToRight ? this.decrGap : this.incrGap;
        int i10 = zIsLeftToRight ? this.incrGap : this.decrGap;
        int i11 = insets.left + insets.right;
        int i12 = i5 + i6;
        float f2 = (size.width - (i11 + i12)) - (i9 + i10);
        float minimum = jScrollBar.getMinimum();
        float maximum = jScrollBar.getMaximum();
        float visibleAmount = jScrollBar.getVisibleAmount();
        float f3 = maximum - minimum;
        float value = getValue(jScrollBar);
        int iMin = Math.min(Math.max(f3 <= 0.0f ? getMaximumThumbSize().width : (int) (f2 * (visibleAmount / f3)), getMinimumThumbSize().width), getMaximumThumbSize().width);
        int i13 = zIsLeftToRight ? (i8 - i10) - iMin : i7 + i5 + i9;
        if (value < maximum - jScrollBar.getVisibleAmount()) {
            float f4 = f2 - iMin;
            if (zIsLeftToRight) {
                i2 = (int) (0.5f + (f4 * ((value - minimum) / (f3 - visibleAmount))));
            } else {
                i2 = (int) (0.5f + (f4 * (((maximum - visibleAmount) - value) / (f3 - visibleAmount))));
            }
            i13 = i2 + i7 + i5 + i9;
        }
        int i14 = size.width - i11;
        if (i14 < i12) {
            int i15 = i14 / 2;
            i5 = i15;
            i6 = i15;
            i8 = size.width - ((insets.right + i6) + i10);
        }
        (zIsLeftToRight ? this.decrButton : this.incrButton).setBounds(i7, i4, i5, i3);
        (zIsLeftToRight ? this.incrButton : this.decrButton).setBounds(i8, i4, i6, i3);
        int i16 = i7 + i5 + i9;
        int i17 = (i8 - i10) - i16;
        this.trackRect.setBounds(i16, i4, i17, i3);
        if (iMin >= ((int) f2)) {
            if (UIManager.getBoolean("ScrollBar.alwaysShowThumb")) {
                setThumbBounds(i16, i4, i17, i3);
                return;
            } else {
                setThumbBounds(0, 0, 0, 0);
                return;
            }
        }
        if (i13 + iMin > i8 - i10) {
            i13 = (i8 - i10) - iMin;
        }
        if (i13 < i7 + i5 + i9) {
            i13 = i7 + i5 + i9 + 1;
        }
        setThumbBounds(i13, i4, iMin, i3);
    }

    @Override // java.awt.LayoutManager
    public void layoutContainer(Container container) {
        if (this.isDragging) {
        }
        JScrollBar jScrollBar = (JScrollBar) container;
        switch (jScrollBar.getOrientation()) {
            case 0:
                layoutHScrollbar(jScrollBar);
                break;
            case 1:
                layoutVScrollbar(jScrollBar);
                break;
        }
    }

    protected void setThumbBounds(int i2, int i3, int i4, int i5) {
        if (this.thumbRect.f12372x == i2 && this.thumbRect.f12373y == i3 && this.thumbRect.width == i4 && this.thumbRect.height == i5) {
            return;
        }
        int iMin = Math.min(i2, this.thumbRect.f12372x);
        int iMin2 = Math.min(i3, this.thumbRect.f12373y);
        int iMax = Math.max(i2 + i4, this.thumbRect.f12372x + this.thumbRect.width);
        int iMax2 = Math.max(i3 + i5, this.thumbRect.f12373y + this.thumbRect.height);
        this.thumbRect.setBounds(i2, i3, i4, i5);
        this.scrollbar.repaint(iMin, iMin2, iMax - iMin, iMax2 - iMin2);
        setThumbRollover(false);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public Rectangle getThumbBounds() {
        return this.thumbRect;
    }

    protected Rectangle getTrackBounds() {
        return this.trackRect;
    }

    static void scrollByBlock(JScrollBar jScrollBar, int i2) {
        int value = jScrollBar.getValue();
        int blockIncrement = jScrollBar.getBlockIncrement(i2) * (i2 > 0 ? 1 : -1);
        int minimum = value + blockIncrement;
        if (blockIncrement > 0 && minimum < value) {
            minimum = jScrollBar.getMaximum();
        } else if (blockIncrement < 0 && minimum > value) {
            minimum = jScrollBar.getMinimum();
        }
        jScrollBar.setValue(minimum);
    }

    protected void scrollByBlock(int i2) {
        scrollByBlock(this.scrollbar, i2);
        this.trackHighlight = i2 > 0 ? 2 : 1;
        Rectangle trackBounds = getTrackBounds();
        this.scrollbar.repaint(trackBounds.f12372x, trackBounds.f12373y, trackBounds.width, trackBounds.height);
    }

    static void scrollByUnits(JScrollBar jScrollBar, int i2, int i3, boolean z2) {
        int unitIncrement;
        int value = -1;
        if (z2) {
            if (i2 < 0) {
                value = jScrollBar.getValue() - jScrollBar.getBlockIncrement(i2);
            } else {
                value = jScrollBar.getValue() + jScrollBar.getBlockIncrement(i2);
            }
        }
        for (int i4 = 0; i4 < i3; i4++) {
            if (i2 > 0) {
                unitIncrement = jScrollBar.getUnitIncrement(i2);
            } else {
                unitIncrement = -jScrollBar.getUnitIncrement(i2);
            }
            int value2 = jScrollBar.getValue();
            int minimum = value2 + unitIncrement;
            if (unitIncrement > 0 && minimum < value2) {
                minimum = jScrollBar.getMaximum();
            } else if (unitIncrement < 0 && minimum > value2) {
                minimum = jScrollBar.getMinimum();
            }
            if (value2 != minimum) {
                if (z2 && i4 > 0) {
                    if (!$assertionsDisabled && value == -1) {
                        throw new AssertionError();
                    }
                    if (i2 < 0 && minimum < value) {
                        return;
                    }
                    if (i2 > 0 && minimum > value) {
                        return;
                    }
                }
                jScrollBar.setValue(minimum);
            } else {
                return;
            }
        }
    }

    protected void scrollByUnit(int i2) {
        scrollByUnits(this.scrollbar, i2, 1, false);
    }

    public boolean getSupportsAbsolutePositioning() {
        return this.supportsAbsolutePositioning;
    }

    /* loaded from: rt.jar:javax/swing/plaf/basic/BasicScrollBarUI$ModelListener.class */
    protected class ModelListener implements ChangeListener {
        protected ModelListener() {
        }

        @Override // javax.swing.event.ChangeListener
        public void stateChanged(ChangeEvent changeEvent) {
            if (!BasicScrollBarUI.this.useCachedValue) {
                BasicScrollBarUI.this.scrollBarValue = BasicScrollBarUI.this.scrollbar.getValue();
            }
            BasicScrollBarUI.this.layoutContainer(BasicScrollBarUI.this.scrollbar);
            BasicScrollBarUI.this.useCachedValue = false;
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* loaded from: rt.jar:javax/swing/plaf/basic/BasicScrollBarUI$TrackListener.class */
    public class TrackListener extends MouseAdapter implements MouseMotionListener {
        protected transient int offset;
        protected transient int currentMouseX;
        protected transient int currentMouseY;
        private transient int direction = 1;

        protected TrackListener() {
        }

        @Override // java.awt.event.MouseAdapter, java.awt.event.MouseListener
        public void mouseReleased(MouseEvent mouseEvent) {
            if (BasicScrollBarUI.this.isDragging) {
                BasicScrollBarUI.this.updateThumbState(mouseEvent.getX(), mouseEvent.getY());
            }
            if (!SwingUtilities.isRightMouseButton(mouseEvent)) {
                if ((!BasicScrollBarUI.this.getSupportsAbsolutePositioning() && SwingUtilities.isMiddleMouseButton(mouseEvent)) || !BasicScrollBarUI.this.scrollbar.isEnabled()) {
                    return;
                }
                Rectangle trackBounds = BasicScrollBarUI.this.getTrackBounds();
                BasicScrollBarUI.this.scrollbar.repaint(trackBounds.f12372x, trackBounds.f12373y, trackBounds.width, trackBounds.height);
                BasicScrollBarUI.this.trackHighlight = 0;
                BasicScrollBarUI.this.isDragging = false;
                this.offset = 0;
                BasicScrollBarUI.this.scrollTimer.stop();
                BasicScrollBarUI.this.useCachedValue = true;
                BasicScrollBarUI.this.scrollbar.setValueIsAdjusting(false);
            }
        }

        @Override // java.awt.event.MouseAdapter, java.awt.event.MouseListener
        public void mousePressed(MouseEvent mouseEvent) {
            if (!SwingUtilities.isRightMouseButton(mouseEvent)) {
                if ((!BasicScrollBarUI.this.getSupportsAbsolutePositioning() && SwingUtilities.isMiddleMouseButton(mouseEvent)) || !BasicScrollBarUI.this.scrollbar.isEnabled()) {
                    return;
                }
                if (!BasicScrollBarUI.this.scrollbar.hasFocus() && BasicScrollBarUI.this.scrollbar.isRequestFocusEnabled()) {
                    BasicScrollBarUI.this.scrollbar.requestFocus();
                }
                BasicScrollBarUI.this.useCachedValue = true;
                BasicScrollBarUI.this.scrollbar.setValueIsAdjusting(true);
                this.currentMouseX = mouseEvent.getX();
                this.currentMouseY = mouseEvent.getY();
                if (BasicScrollBarUI.this.getThumbBounds().contains(this.currentMouseX, this.currentMouseY)) {
                    switch (BasicScrollBarUI.this.scrollbar.getOrientation()) {
                        case 0:
                            this.offset = this.currentMouseX - BasicScrollBarUI.this.getThumbBounds().f12372x;
                            break;
                        case 1:
                            this.offset = this.currentMouseY - BasicScrollBarUI.this.getThumbBounds().f12373y;
                            break;
                    }
                    BasicScrollBarUI.this.isDragging = true;
                    return;
                }
                if (BasicScrollBarUI.this.getSupportsAbsolutePositioning() && SwingUtilities.isMiddleMouseButton(mouseEvent)) {
                    switch (BasicScrollBarUI.this.scrollbar.getOrientation()) {
                        case 0:
                            this.offset = BasicScrollBarUI.this.getThumbBounds().width / 2;
                            break;
                        case 1:
                            this.offset = BasicScrollBarUI.this.getThumbBounds().height / 2;
                            break;
                    }
                    BasicScrollBarUI.this.isDragging = true;
                    setValueFrom(mouseEvent);
                    return;
                }
                BasicScrollBarUI.this.isDragging = false;
                Dimension size = BasicScrollBarUI.this.scrollbar.getSize();
                this.direction = 1;
                switch (BasicScrollBarUI.this.scrollbar.getOrientation()) {
                    case 0:
                        if (BasicScrollBarUI.this.getThumbBounds().isEmpty()) {
                            this.direction = this.currentMouseX < size.width / 2 ? -1 : 1;
                        } else {
                            this.direction = this.currentMouseX < BasicScrollBarUI.this.getThumbBounds().f12372x ? -1 : 1;
                        }
                        if (!BasicScrollBarUI.this.scrollbar.getComponentOrientation().isLeftToRight()) {
                            this.direction = -this.direction;
                            break;
                        }
                        break;
                    case 1:
                        if (BasicScrollBarUI.this.getThumbBounds().isEmpty()) {
                            this.direction = this.currentMouseY < size.height / 2 ? -1 : 1;
                            break;
                        } else {
                            this.direction = this.currentMouseY < BasicScrollBarUI.this.getThumbBounds().f12373y ? -1 : 1;
                            break;
                        }
                }
                BasicScrollBarUI.this.scrollByBlock(this.direction);
                BasicScrollBarUI.this.scrollTimer.stop();
                BasicScrollBarUI.this.scrollListener.setDirection(this.direction);
                BasicScrollBarUI.this.scrollListener.setScrollByBlock(true);
                startScrollTimerIfNecessary();
            }
        }

        @Override // java.awt.event.MouseAdapter, java.awt.event.MouseMotionListener
        public void mouseDragged(MouseEvent mouseEvent) {
            if (!SwingUtilities.isRightMouseButton(mouseEvent)) {
                if ((!BasicScrollBarUI.this.getSupportsAbsolutePositioning() && SwingUtilities.isMiddleMouseButton(mouseEvent)) || !BasicScrollBarUI.this.scrollbar.isEnabled() || BasicScrollBarUI.this.getThumbBounds().isEmpty()) {
                    return;
                }
                if (BasicScrollBarUI.this.isDragging) {
                    setValueFrom(mouseEvent);
                    return;
                }
                this.currentMouseX = mouseEvent.getX();
                this.currentMouseY = mouseEvent.getY();
                BasicScrollBarUI.this.updateThumbState(this.currentMouseX, this.currentMouseY);
                startScrollTimerIfNecessary();
            }
        }

        private void setValueFrom(MouseEvent mouseEvent) {
            int i2;
            int i3;
            int iMin;
            int i4;
            boolean zIsThumbRollover = BasicScrollBarUI.this.isThumbRollover();
            BoundedRangeModel model = BasicScrollBarUI.this.scrollbar.getModel();
            Rectangle thumbBounds = BasicScrollBarUI.this.getThumbBounds();
            if (BasicScrollBarUI.this.scrollbar.getOrientation() == 1) {
                i2 = BasicScrollBarUI.this.trackRect.f12373y;
                i3 = (BasicScrollBarUI.this.trackRect.f12373y + BasicScrollBarUI.this.trackRect.height) - thumbBounds.height;
                iMin = Math.min(i3, Math.max(i2, mouseEvent.getY() - this.offset));
                BasicScrollBarUI.this.setThumbBounds(thumbBounds.f12372x, iMin, thumbBounds.width, thumbBounds.height);
                float f2 = BasicScrollBarUI.this.getTrackBounds().height;
            } else {
                i2 = BasicScrollBarUI.this.trackRect.f12372x;
                i3 = (BasicScrollBarUI.this.trackRect.f12372x + BasicScrollBarUI.this.trackRect.width) - thumbBounds.width;
                iMin = Math.min(i3, Math.max(i2, mouseEvent.getX() - this.offset));
                BasicScrollBarUI.this.setThumbBounds(iMin, thumbBounds.f12373y, thumbBounds.width, thumbBounds.height);
                float f3 = BasicScrollBarUI.this.getTrackBounds().width;
            }
            if (iMin == i3) {
                if (BasicScrollBarUI.this.scrollbar.getOrientation() == 1 || BasicScrollBarUI.this.scrollbar.getComponentOrientation().isLeftToRight()) {
                    BasicScrollBarUI.this.scrollbar.setValue(model.getMaximum() - model.getExtent());
                } else {
                    BasicScrollBarUI.this.scrollbar.setValue(model.getMinimum());
                }
            } else {
                float maximum = (model.getMaximum() - model.getExtent()) - model.getMinimum();
                float f4 = iMin - i2;
                float f5 = i3 - i2;
                if (BasicScrollBarUI.this.scrollbar.getOrientation() == 1 || BasicScrollBarUI.this.scrollbar.getComponentOrientation().isLeftToRight()) {
                    i4 = (int) (0.5d + ((f4 / f5) * maximum));
                } else {
                    i4 = (int) (0.5d + (((i3 - iMin) / f5) * maximum));
                }
                BasicScrollBarUI.this.useCachedValue = true;
                BasicScrollBarUI.this.scrollBarValue = i4 + model.getMinimum();
                BasicScrollBarUI.this.scrollbar.setValue(adjustValueIfNecessary(BasicScrollBarUI.this.scrollBarValue));
            }
            BasicScrollBarUI.this.setThumbRollover(zIsThumbRollover);
        }

        private int adjustValueIfNecessary(int i2) {
            int iLocationToIndex;
            Rectangle cellBounds;
            if (BasicScrollBarUI.this.scrollbar.getParent() instanceof JScrollPane) {
                JScrollPane jScrollPane = (JScrollPane) BasicScrollBarUI.this.scrollbar.getParent();
                JViewport viewport = jScrollPane.getViewport();
                Component view = viewport.getView();
                if (view instanceof JList) {
                    JList jList = (JList) view;
                    if (DefaultLookup.getBoolean(jList, jList.getUI(), "List.lockToPositionOnScroll", false)) {
                        int i3 = i2;
                        int layoutOrientation = jList.getLayoutOrientation();
                        int orientation = BasicScrollBarUI.this.scrollbar.getOrientation();
                        if (orientation == 1 && layoutOrientation == 0 && (cellBounds = jList.getCellBounds((iLocationToIndex = jList.locationToIndex(new Point(0, i2))), iLocationToIndex)) != null) {
                            i3 = cellBounds.f12373y;
                        }
                        if (orientation == 0 && (layoutOrientation == 1 || layoutOrientation == 2)) {
                            if (jScrollPane.getComponentOrientation().isLeftToRight()) {
                                int iLocationToIndex2 = jList.locationToIndex(new Point(i2, 0));
                                Rectangle cellBounds2 = jList.getCellBounds(iLocationToIndex2, iLocationToIndex2);
                                if (cellBounds2 != null) {
                                    i3 = cellBounds2.f12372x;
                                }
                            } else {
                                Point point = new Point(i2, 0);
                                int i4 = viewport.getExtentSize().width;
                                point.f12370x += i4 - 1;
                                int iLocationToIndex3 = jList.locationToIndex(point);
                                Rectangle cellBounds3 = jList.getCellBounds(iLocationToIndex3, iLocationToIndex3);
                                if (cellBounds3 != null) {
                                    i3 = (cellBounds3.f12372x + cellBounds3.width) - i4;
                                }
                            }
                        }
                        i2 = i3;
                    }
                }
            }
            return i2;
        }

        private void startScrollTimerIfNecessary() {
            if (BasicScrollBarUI.this.scrollTimer.isRunning()) {
            }
            Rectangle thumbBounds = BasicScrollBarUI.this.getThumbBounds();
            switch (BasicScrollBarUI.this.scrollbar.getOrientation()) {
                case 0:
                    if ((this.direction > 0 && BasicScrollBarUI.this.isMouseAfterThumb()) || (this.direction < 0 && BasicScrollBarUI.this.isMouseBeforeThumb())) {
                        BasicScrollBarUI.this.scrollTimer.start();
                        break;
                    }
                    break;
                case 1:
                    if (this.direction > 0) {
                        if (thumbBounds.f12373y + thumbBounds.height < BasicScrollBarUI.this.trackListener.currentMouseY) {
                            BasicScrollBarUI.this.scrollTimer.start();
                            break;
                        }
                    } else if (thumbBounds.f12373y > BasicScrollBarUI.this.trackListener.currentMouseY) {
                        BasicScrollBarUI.this.scrollTimer.start();
                        break;
                    }
                    break;
            }
        }

        @Override // java.awt.event.MouseAdapter, java.awt.event.MouseMotionListener
        public void mouseMoved(MouseEvent mouseEvent) {
            if (!BasicScrollBarUI.this.isDragging) {
                BasicScrollBarUI.this.updateThumbState(mouseEvent.getX(), mouseEvent.getY());
            }
        }

        @Override // java.awt.event.MouseAdapter, java.awt.event.MouseListener
        public void mouseExited(MouseEvent mouseEvent) {
            if (!BasicScrollBarUI.this.isDragging) {
                BasicScrollBarUI.this.setThumbRollover(false);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* loaded from: rt.jar:javax/swing/plaf/basic/BasicScrollBarUI$ArrowButtonListener.class */
    public class ArrowButtonListener extends MouseAdapter {
        boolean handledEvent;

        protected ArrowButtonListener() {
        }

        @Override // java.awt.event.MouseAdapter, java.awt.event.MouseListener
        public void mousePressed(MouseEvent mouseEvent) {
            if (BasicScrollBarUI.this.scrollbar.isEnabled() && SwingUtilities.isLeftMouseButton(mouseEvent)) {
                int i2 = mouseEvent.getSource() == BasicScrollBarUI.this.incrButton ? 1 : -1;
                BasicScrollBarUI.this.scrollByUnit(i2);
                BasicScrollBarUI.this.scrollTimer.stop();
                BasicScrollBarUI.this.scrollListener.setDirection(i2);
                BasicScrollBarUI.this.scrollListener.setScrollByBlock(false);
                BasicScrollBarUI.this.scrollTimer.start();
                this.handledEvent = true;
                if (!BasicScrollBarUI.this.scrollbar.hasFocus() && BasicScrollBarUI.this.scrollbar.isRequestFocusEnabled()) {
                    BasicScrollBarUI.this.scrollbar.requestFocus();
                }
            }
        }

        @Override // java.awt.event.MouseAdapter, java.awt.event.MouseListener
        public void mouseReleased(MouseEvent mouseEvent) {
            BasicScrollBarUI.this.scrollTimer.stop();
            this.handledEvent = false;
            BasicScrollBarUI.this.scrollbar.setValueIsAdjusting(false);
        }
    }

    /* loaded from: rt.jar:javax/swing/plaf/basic/BasicScrollBarUI$ScrollListener.class */
    protected class ScrollListener implements ActionListener {
        int direction;
        boolean useBlockIncrement;

        public ScrollListener() {
            this.direction = 1;
            this.direction = 1;
            this.useBlockIncrement = false;
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
                BasicScrollBarUI.this.scrollByBlock(this.direction);
                if (BasicScrollBarUI.this.scrollbar.getOrientation() == 1) {
                    if (this.direction > 0) {
                        if (BasicScrollBarUI.this.getThumbBounds().f12373y + BasicScrollBarUI.this.getThumbBounds().height >= BasicScrollBarUI.this.trackListener.currentMouseY) {
                            ((Timer) actionEvent.getSource()).stop();
                        }
                    } else if (BasicScrollBarUI.this.getThumbBounds().f12373y <= BasicScrollBarUI.this.trackListener.currentMouseY) {
                        ((Timer) actionEvent.getSource()).stop();
                    }
                } else if ((this.direction > 0 && !BasicScrollBarUI.this.isMouseAfterThumb()) || (this.direction < 0 && !BasicScrollBarUI.this.isMouseBeforeThumb())) {
                    ((Timer) actionEvent.getSource()).stop();
                }
            } else {
                BasicScrollBarUI.this.scrollByUnit(this.direction);
            }
            if (this.direction > 0 && BasicScrollBarUI.this.scrollbar.getValue() + BasicScrollBarUI.this.scrollbar.getVisibleAmount() >= BasicScrollBarUI.this.scrollbar.getMaximum()) {
                ((Timer) actionEvent.getSource()).stop();
            } else if (this.direction < 0 && BasicScrollBarUI.this.scrollbar.getValue() <= BasicScrollBarUI.this.scrollbar.getMinimum()) {
                ((Timer) actionEvent.getSource()).stop();
            }
        }
    }

    private boolean isMouseLeftOfThumb() {
        return this.trackListener.currentMouseX < getThumbBounds().f12372x;
    }

    private boolean isMouseRightOfThumb() {
        Rectangle thumbBounds = getThumbBounds();
        return this.trackListener.currentMouseX > thumbBounds.f12372x + thumbBounds.width;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isMouseBeforeThumb() {
        if (this.scrollbar.getComponentOrientation().isLeftToRight()) {
            return isMouseLeftOfThumb();
        }
        return isMouseRightOfThumb();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isMouseAfterThumb() {
        if (this.scrollbar.getComponentOrientation().isLeftToRight()) {
            return isMouseRightOfThumb();
        }
        return isMouseLeftOfThumb();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateButtonDirections() {
        int orientation = this.scrollbar.getOrientation();
        if (this.scrollbar.getComponentOrientation().isLeftToRight()) {
            if (this.incrButton instanceof BasicArrowButton) {
                ((BasicArrowButton) this.incrButton).setDirection(orientation == 0 ? 3 : 5);
            }
            if (this.decrButton instanceof BasicArrowButton) {
                ((BasicArrowButton) this.decrButton).setDirection(orientation == 0 ? 7 : 1);
                return;
            }
            return;
        }
        if (this.incrButton instanceof BasicArrowButton) {
            ((BasicArrowButton) this.incrButton).setDirection(orientation == 0 ? 7 : 5);
        }
        if (this.decrButton instanceof BasicArrowButton) {
            ((BasicArrowButton) this.decrButton).setDirection(orientation == 0 ? 3 : 1);
        }
    }

    /* loaded from: rt.jar:javax/swing/plaf/basic/BasicScrollBarUI$PropertyChangeHandler.class */
    public class PropertyChangeHandler implements PropertyChangeListener {
        public PropertyChangeHandler() {
        }

        @Override // java.beans.PropertyChangeListener
        public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
            BasicScrollBarUI.this.getHandler().propertyChange(propertyChangeEvent);
        }
    }

    /* loaded from: rt.jar:javax/swing/plaf/basic/BasicScrollBarUI$Actions.class */
    private static class Actions extends UIAction {
        private static final String POSITIVE_UNIT_INCREMENT = "positiveUnitIncrement";
        private static final String POSITIVE_BLOCK_INCREMENT = "positiveBlockIncrement";
        private static final String NEGATIVE_UNIT_INCREMENT = "negativeUnitIncrement";
        private static final String NEGATIVE_BLOCK_INCREMENT = "negativeBlockIncrement";
        private static final String MIN_SCROLL = "minScroll";
        private static final String MAX_SCROLL = "maxScroll";

        Actions(String str) {
            super(str);
        }

        @Override // java.awt.event.ActionListener
        public void actionPerformed(ActionEvent actionEvent) {
            JScrollBar jScrollBar = (JScrollBar) actionEvent.getSource();
            String name = getName();
            if (name == "positiveUnitIncrement") {
                scroll(jScrollBar, 1, false);
                return;
            }
            if (name == "positiveBlockIncrement") {
                scroll(jScrollBar, 1, true);
                return;
            }
            if (name == "negativeUnitIncrement") {
                scroll(jScrollBar, -1, false);
                return;
            }
            if (name == "negativeBlockIncrement") {
                scroll(jScrollBar, -1, true);
            } else if (name == "minScroll") {
                scroll(jScrollBar, 2, true);
            } else if (name == "maxScroll") {
                scroll(jScrollBar, 3, true);
            }
        }

        private void scroll(JScrollBar jScrollBar, int i2, boolean z2) {
            int unitIncrement;
            if (i2 == -1 || i2 == 1) {
                if (z2) {
                    if (i2 == -1) {
                        unitIncrement = (-1) * jScrollBar.getBlockIncrement(-1);
                    } else {
                        unitIncrement = jScrollBar.getBlockIncrement(1);
                    }
                } else if (i2 == -1) {
                    unitIncrement = (-1) * jScrollBar.getUnitIncrement(-1);
                } else {
                    unitIncrement = jScrollBar.getUnitIncrement(1);
                }
                jScrollBar.setValue(jScrollBar.getValue() + unitIncrement);
                return;
            }
            if (i2 == 2) {
                jScrollBar.setValue(jScrollBar.getMinimum());
            } else if (i2 == 3) {
                jScrollBar.setValue(jScrollBar.getMaximum());
            }
        }
    }

    /* loaded from: rt.jar:javax/swing/plaf/basic/BasicScrollBarUI$Handler.class */
    private class Handler implements FocusListener, PropertyChangeListener {
        private Handler() {
        }

        @Override // java.awt.event.FocusListener
        public void focusGained(FocusEvent focusEvent) {
            BasicScrollBarUI.this.scrollbar.repaint();
        }

        @Override // java.awt.event.FocusListener
        public void focusLost(FocusEvent focusEvent) {
            BasicScrollBarUI.this.scrollbar.repaint();
        }

        @Override // java.beans.PropertyChangeListener
        public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
            String propertyName = propertyChangeEvent.getPropertyName();
            if ("model" == propertyName) {
                BoundedRangeModel boundedRangeModel = (BoundedRangeModel) propertyChangeEvent.getOldValue();
                BoundedRangeModel boundedRangeModel2 = (BoundedRangeModel) propertyChangeEvent.getNewValue();
                boundedRangeModel.removeChangeListener(BasicScrollBarUI.this.modelListener);
                boundedRangeModel2.addChangeListener(BasicScrollBarUI.this.modelListener);
                BasicScrollBarUI.this.scrollBarValue = BasicScrollBarUI.this.scrollbar.getValue();
                BasicScrollBarUI.this.scrollbar.repaint();
                BasicScrollBarUI.this.scrollbar.revalidate();
                return;
            }
            if ("orientation" == propertyName) {
                BasicScrollBarUI.this.updateButtonDirections();
            } else if ("componentOrientation" == propertyName) {
                BasicScrollBarUI.this.updateButtonDirections();
                SwingUtilities.replaceUIInputMap(BasicScrollBarUI.this.scrollbar, 0, BasicScrollBarUI.this.getInputMap(0));
            }
        }
    }
}
