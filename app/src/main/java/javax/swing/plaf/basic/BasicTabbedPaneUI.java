package javax.swing.plaf.basic;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ContainerEvent;
import java.awt.event.ContainerListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Hashtable;
import java.util.Vector;
import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.Icon;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JViewport;
import javax.swing.KeyStroke;
import javax.swing.LookAndFeel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.ComponentInputMapUIResource;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.TabbedPaneUI;
import javax.swing.plaf.UIResource;
import javax.swing.text.View;
import sun.swing.DefaultLookup;
import sun.swing.SwingUtilities2;
import sun.swing.UIAction;

/* loaded from: rt.jar:javax/swing/plaf/basic/BasicTabbedPaneUI.class */
public class BasicTabbedPaneUI extends TabbedPaneUI implements SwingConstants {
    protected JTabbedPane tabPane;
    protected Color highlight;
    protected Color lightHighlight;
    protected Color shadow;
    protected Color darkShadow;
    protected Color focus;
    private Color selectedColor;
    protected int textIconGap;
    protected int tabRunOverlay;
    protected Insets tabInsets;
    protected Insets selectedTabPadInsets;
    protected Insets tabAreaInsets;
    protected Insets contentBorderInsets;
    private boolean tabsOverlapBorder;

    @Deprecated
    protected KeyStroke upKey;

    @Deprecated
    protected KeyStroke downKey;

    @Deprecated
    protected KeyStroke leftKey;

    @Deprecated
    protected KeyStroke rightKey;
    protected int maxTabHeight;
    protected int maxTabWidth;
    protected ChangeListener tabChangeListener;
    protected PropertyChangeListener propertyChangeListener;
    protected MouseListener mouseListener;
    protected FocusListener focusListener;
    private Component visibleComponent;
    private Vector<View> htmlViews;
    private Hashtable<Integer, Integer> mnemonicToIndexMap;
    private InputMap mnemonicInputMap;
    private ScrollableTabSupport tabScroller;
    private TabContainer tabContainer;
    private int focusIndex;
    private Handler handler;
    private int rolloverTabIndex;
    private boolean isRunsDirty;
    private boolean calculatedBaseline;
    private int baseline;
    private static int[] xCropLen = {1, 1, 0, 0, 1, 1, 2, 2};
    private static int[] yCropLen = {0, 3, 3, 6, 6, 9, 9, 12};
    private static final int CROP_SEGMENT = 12;
    private boolean tabsOpaque = true;
    private boolean contentOpaque = true;
    protected int[] tabRuns = new int[10];
    protected int runCount = 0;
    protected int selectedRun = -1;
    protected Rectangle[] rects = new Rectangle[0];
    private Insets currentPadInsets = new Insets(0, 0, 0, 0);
    private Insets currentTabAreaInsets = new Insets(0, 0, 0, 0);
    protected transient Rectangle calcRect = new Rectangle(0, 0, 0, 0);

    public static ComponentUI createUI(JComponent jComponent) {
        return new BasicTabbedPaneUI();
    }

    static void loadActionMap(LazyActionMap lazyActionMap) {
        lazyActionMap.put(new Actions("navigateNext"));
        lazyActionMap.put(new Actions("navigatePrevious"));
        lazyActionMap.put(new Actions("navigateRight"));
        lazyActionMap.put(new Actions("navigateLeft"));
        lazyActionMap.put(new Actions("navigateUp"));
        lazyActionMap.put(new Actions("navigateDown"));
        lazyActionMap.put(new Actions("navigatePageUp"));
        lazyActionMap.put(new Actions("navigatePageDown"));
        lazyActionMap.put(new Actions("requestFocus"));
        lazyActionMap.put(new Actions("requestFocusForVisibleComponent"));
        lazyActionMap.put(new Actions("setSelectedIndex"));
        lazyActionMap.put(new Actions("selectTabWithFocus"));
        lazyActionMap.put(new Actions("scrollTabsForwardAction"));
        lazyActionMap.put(new Actions("scrollTabsBackwardAction"));
    }

    @Override // javax.swing.plaf.ComponentUI
    public void installUI(JComponent jComponent) {
        this.tabPane = (JTabbedPane) jComponent;
        this.calculatedBaseline = false;
        this.rolloverTabIndex = -1;
        this.focusIndex = -1;
        jComponent.setLayout(createLayoutManager());
        installComponents();
        installDefaults();
        installListeners();
        installKeyboardActions();
    }

    @Override // javax.swing.plaf.ComponentUI
    public void uninstallUI(JComponent jComponent) {
        uninstallKeyboardActions();
        uninstallListeners();
        uninstallDefaults();
        uninstallComponents();
        jComponent.setLayout(null);
        this.tabPane = null;
    }

    protected LayoutManager createLayoutManager() {
        if (this.tabPane.getTabLayoutPolicy() == 1) {
            return new TabbedPaneScrollLayout();
        }
        return new TabbedPaneLayout();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean scrollableTabLayoutEnabled() {
        return this.tabPane.getLayout() instanceof TabbedPaneScrollLayout;
    }

    protected void installComponents() {
        if (scrollableTabLayoutEnabled() && this.tabScroller == null) {
            this.tabScroller = new ScrollableTabSupport(this.tabPane.getTabPlacement());
            this.tabPane.add(this.tabScroller.viewport);
        }
        installTabContainer();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void installTabContainer() {
        for (int i2 = 0; i2 < this.tabPane.getTabCount(); i2++) {
            Component tabComponentAt = this.tabPane.getTabComponentAt(i2);
            if (tabComponentAt != null) {
                if (this.tabContainer == null) {
                    this.tabContainer = new TabContainer();
                }
                this.tabContainer.add(tabComponentAt);
            }
        }
        if (this.tabContainer == null) {
            return;
        }
        if (scrollableTabLayoutEnabled()) {
            this.tabScroller.tabPanel.add(this.tabContainer);
        } else {
            this.tabPane.add(this.tabContainer);
        }
    }

    protected JButton createScrollButton(int i2) {
        if (i2 != 5 && i2 != 1 && i2 != 3 && i2 != 7) {
            throw new IllegalArgumentException("Direction must be one of: SOUTH, NORTH, EAST or WEST");
        }
        return new ScrollableTabButton(i2);
    }

    protected void uninstallComponents() {
        uninstallTabContainer();
        if (scrollableTabLayoutEnabled()) {
            this.tabPane.remove(this.tabScroller.viewport);
            this.tabPane.remove(this.tabScroller.scrollForwardButton);
            this.tabPane.remove(this.tabScroller.scrollBackwardButton);
            this.tabScroller = null;
        }
    }

    private void uninstallTabContainer() {
        if (this.tabContainer == null) {
            return;
        }
        this.tabContainer.notifyTabbedPane = false;
        this.tabContainer.removeAll();
        if (scrollableTabLayoutEnabled()) {
            this.tabContainer.remove(this.tabScroller.croppedEdge);
            this.tabScroller.tabPanel.remove(this.tabContainer);
        } else {
            this.tabPane.remove(this.tabContainer);
        }
        this.tabContainer = null;
    }

    protected void installDefaults() {
        LookAndFeel.installColorsAndFont(this.tabPane, "TabbedPane.background", "TabbedPane.foreground", "TabbedPane.font");
        this.highlight = UIManager.getColor("TabbedPane.light");
        this.lightHighlight = UIManager.getColor("TabbedPane.highlight");
        this.shadow = UIManager.getColor("TabbedPane.shadow");
        this.darkShadow = UIManager.getColor("TabbedPane.darkShadow");
        this.focus = UIManager.getColor("TabbedPane.focus");
        this.selectedColor = UIManager.getColor("TabbedPane.selected");
        this.textIconGap = UIManager.getInt("TabbedPane.textIconGap");
        this.tabInsets = UIManager.getInsets("TabbedPane.tabInsets");
        this.selectedTabPadInsets = UIManager.getInsets("TabbedPane.selectedTabPadInsets");
        this.tabAreaInsets = UIManager.getInsets("TabbedPane.tabAreaInsets");
        this.tabsOverlapBorder = UIManager.getBoolean("TabbedPane.tabsOverlapBorder");
        this.contentBorderInsets = UIManager.getInsets("TabbedPane.contentBorderInsets");
        this.tabRunOverlay = UIManager.getInt("TabbedPane.tabRunOverlay");
        this.tabsOpaque = UIManager.getBoolean("TabbedPane.tabsOpaque");
        this.contentOpaque = UIManager.getBoolean("TabbedPane.contentOpaque");
        Object obj = UIManager.get("TabbedPane.opaque");
        if (obj == null) {
            obj = Boolean.FALSE;
        }
        LookAndFeel.installProperty(this.tabPane, "opaque", obj);
        if (this.tabInsets == null) {
            this.tabInsets = new Insets(0, 4, 1, 4);
        }
        if (this.selectedTabPadInsets == null) {
            this.selectedTabPadInsets = new Insets(2, 2, 2, 1);
        }
        if (this.tabAreaInsets == null) {
            this.tabAreaInsets = new Insets(3, 2, 0, 2);
        }
        if (this.contentBorderInsets == null) {
            this.contentBorderInsets = new Insets(2, 2, 3, 3);
        }
    }

    protected void uninstallDefaults() {
        this.highlight = null;
        this.lightHighlight = null;
        this.shadow = null;
        this.darkShadow = null;
        this.focus = null;
        this.tabInsets = null;
        this.selectedTabPadInsets = null;
        this.tabAreaInsets = null;
        this.contentBorderInsets = null;
    }

    protected void installListeners() {
        PropertyChangeListener propertyChangeListenerCreatePropertyChangeListener = createPropertyChangeListener();
        this.propertyChangeListener = propertyChangeListenerCreatePropertyChangeListener;
        if (propertyChangeListenerCreatePropertyChangeListener != null) {
            this.tabPane.addPropertyChangeListener(this.propertyChangeListener);
        }
        ChangeListener changeListenerCreateChangeListener = createChangeListener();
        this.tabChangeListener = changeListenerCreateChangeListener;
        if (changeListenerCreateChangeListener != null) {
            this.tabPane.addChangeListener(this.tabChangeListener);
        }
        MouseListener mouseListenerCreateMouseListener = createMouseListener();
        this.mouseListener = mouseListenerCreateMouseListener;
        if (mouseListenerCreateMouseListener != null) {
            this.tabPane.addMouseListener(this.mouseListener);
        }
        this.tabPane.addMouseMotionListener(getHandler());
        FocusListener focusListenerCreateFocusListener = createFocusListener();
        this.focusListener = focusListenerCreateFocusListener;
        if (focusListenerCreateFocusListener != null) {
            this.tabPane.addFocusListener(this.focusListener);
        }
        this.tabPane.addContainerListener(getHandler());
        if (this.tabPane.getTabCount() > 0) {
            this.htmlViews = createHTMLVector();
        }
    }

    protected void uninstallListeners() {
        if (this.mouseListener != null) {
            this.tabPane.removeMouseListener(this.mouseListener);
            this.mouseListener = null;
        }
        this.tabPane.removeMouseMotionListener(getHandler());
        if (this.focusListener != null) {
            this.tabPane.removeFocusListener(this.focusListener);
            this.focusListener = null;
        }
        this.tabPane.removeContainerListener(getHandler());
        if (this.htmlViews != null) {
            this.htmlViews.removeAllElements();
            this.htmlViews = null;
        }
        if (this.tabChangeListener != null) {
            this.tabPane.removeChangeListener(this.tabChangeListener);
            this.tabChangeListener = null;
        }
        if (this.propertyChangeListener != null) {
            this.tabPane.removePropertyChangeListener(this.propertyChangeListener);
            this.propertyChangeListener = null;
        }
        this.handler = null;
    }

    protected MouseListener createMouseListener() {
        return getHandler();
    }

    protected FocusListener createFocusListener() {
        return getHandler();
    }

    protected ChangeListener createChangeListener() {
        return getHandler();
    }

    protected PropertyChangeListener createPropertyChangeListener() {
        return getHandler();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Handler getHandler() {
        if (this.handler == null) {
            this.handler = new Handler();
        }
        return this.handler;
    }

    protected void installKeyboardActions() {
        SwingUtilities.replaceUIInputMap(this.tabPane, 1, getInputMap(1));
        SwingUtilities.replaceUIInputMap(this.tabPane, 0, getInputMap(0));
        LazyActionMap.installLazyActionMap(this.tabPane, BasicTabbedPaneUI.class, "TabbedPane.actionMap");
        updateMnemonics();
    }

    InputMap getInputMap(int i2) {
        if (i2 == 1) {
            return (InputMap) DefaultLookup.get(this.tabPane, this, "TabbedPane.ancestorInputMap");
        }
        if (i2 == 0) {
            return (InputMap) DefaultLookup.get(this.tabPane, this, "TabbedPane.focusInputMap");
        }
        return null;
    }

    protected void uninstallKeyboardActions() {
        SwingUtilities.replaceUIActionMap(this.tabPane, null);
        SwingUtilities.replaceUIInputMap(this.tabPane, 1, null);
        SwingUtilities.replaceUIInputMap(this.tabPane, 0, null);
        SwingUtilities.replaceUIInputMap(this.tabPane, 2, null);
        this.mnemonicToIndexMap = null;
        this.mnemonicInputMap = null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateMnemonics() {
        resetMnemonics();
        for (int tabCount = this.tabPane.getTabCount() - 1; tabCount >= 0; tabCount--) {
            int mnemonicAt = this.tabPane.getMnemonicAt(tabCount);
            if (mnemonicAt > 0) {
                addMnemonic(tabCount, mnemonicAt);
            }
        }
    }

    private void resetMnemonics() {
        if (this.mnemonicToIndexMap != null) {
            this.mnemonicToIndexMap.clear();
            this.mnemonicInputMap.clear();
        }
    }

    private void addMnemonic(int i2, int i3) {
        if (this.mnemonicToIndexMap == null) {
            initMnemonics();
        }
        this.mnemonicInputMap.put(KeyStroke.getKeyStroke(i3, BasicLookAndFeel.getFocusAcceleratorKeyMask()), "setSelectedIndex");
        this.mnemonicInputMap.put(KeyStroke.getKeyStroke(i3, SwingUtilities2.setAltGraphMask(BasicLookAndFeel.getFocusAcceleratorKeyMask())), "setSelectedIndex");
        this.mnemonicToIndexMap.put(Integer.valueOf(i3), Integer.valueOf(i2));
    }

    private void initMnemonics() {
        this.mnemonicToIndexMap = new Hashtable<>();
        this.mnemonicInputMap = new ComponentInputMapUIResource(this.tabPane);
        this.mnemonicInputMap.setParent(SwingUtilities.getUIInputMap(this.tabPane, 2));
        SwingUtilities.replaceUIInputMap(this.tabPane, 2, this.mnemonicInputMap);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setRolloverTab(int i2, int i3) {
        setRolloverTab(tabForCoordinate(this.tabPane, i2, i3, false));
    }

    protected void setRolloverTab(int i2) {
        this.rolloverTabIndex = i2;
    }

    protected int getRolloverTab() {
        return this.rolloverTabIndex;
    }

    @Override // javax.swing.plaf.ComponentUI
    public Dimension getMinimumSize(JComponent jComponent) {
        return null;
    }

    @Override // javax.swing.plaf.ComponentUI
    public Dimension getMaximumSize(JComponent jComponent) {
        return null;
    }

    @Override // javax.swing.plaf.ComponentUI
    public int getBaseline(JComponent jComponent, int i2, int i3) {
        super.getBaseline(jComponent, i2, i3);
        int iCalculateBaselineIfNecessary = calculateBaselineIfNecessary();
        if (iCalculateBaselineIfNecessary != -1) {
            int tabPlacement = this.tabPane.getTabPlacement();
            Insets insets = this.tabPane.getInsets();
            Insets tabAreaInsets = getTabAreaInsets(tabPlacement);
            switch (tabPlacement) {
                case 1:
                    return iCalculateBaselineIfNecessary + insets.top + tabAreaInsets.top;
                case 2:
                case 4:
                    return iCalculateBaselineIfNecessary + insets.top + tabAreaInsets.top;
                case 3:
                    return (((i3 - insets.bottom) - tabAreaInsets.bottom) - this.maxTabHeight) + iCalculateBaselineIfNecessary;
                default:
                    return -1;
            }
        }
        return -1;
    }

    @Override // javax.swing.plaf.ComponentUI
    public Component.BaselineResizeBehavior getBaselineResizeBehavior(JComponent jComponent) {
        super.getBaselineResizeBehavior(jComponent);
        switch (this.tabPane.getTabPlacement()) {
            case 1:
            case 2:
            case 4:
                return Component.BaselineResizeBehavior.CONSTANT_ASCENT;
            case 3:
                return Component.BaselineResizeBehavior.CONSTANT_DESCENT;
            default:
                return Component.BaselineResizeBehavior.OTHER;
        }
    }

    protected int getBaseline(int i2) {
        if (this.tabPane.getTabComponentAt(i2) != null) {
            if (getBaselineOffset() != 0) {
                return -1;
            }
            Component tabComponentAt = this.tabPane.getTabComponentAt(i2);
            Dimension preferredSize = tabComponentAt.getPreferredSize();
            Insets tabInsets = getTabInsets(this.tabPane.getTabPlacement(), i2);
            return tabComponentAt.getBaseline(preferredSize.width, preferredSize.height) + ((((this.maxTabHeight - tabInsets.top) - tabInsets.bottom) - preferredSize.height) / 2) + tabInsets.top;
        }
        View textViewForTab = getTextViewForTab(i2);
        if (textViewForTab != null) {
            int preferredSpan = (int) textViewForTab.getPreferredSpan(1);
            int hTMLBaseline = BasicHTML.getHTMLBaseline(textViewForTab, (int) textViewForTab.getPreferredSpan(0), preferredSpan);
            if (hTMLBaseline >= 0) {
                return ((this.maxTabHeight / 2) - (preferredSpan / 2)) + hTMLBaseline + getBaselineOffset();
            }
            return -1;
        }
        FontMetrics fontMetrics = getFontMetrics();
        int height = fontMetrics.getHeight();
        return ((this.maxTabHeight / 2) - (height / 2)) + fontMetrics.getAscent() + getBaselineOffset();
    }

    protected int getBaselineOffset() {
        switch (this.tabPane.getTabPlacement()) {
            case 1:
                if (this.tabPane.getTabCount() > 1) {
                    return 1;
                }
                return -1;
            case 3:
                if (this.tabPane.getTabCount() > 1) {
                    return -1;
                }
                return 1;
            default:
                return this.maxTabHeight % 2;
        }
    }

    private int calculateBaselineIfNecessary() {
        if (!this.calculatedBaseline) {
            this.calculatedBaseline = true;
            this.baseline = -1;
            if (this.tabPane.getTabCount() > 0) {
                calculateBaseline();
            }
        }
        return this.baseline;
    }

    private void calculateBaseline() {
        int tabCount = this.tabPane.getTabCount();
        int tabPlacement = this.tabPane.getTabPlacement();
        this.maxTabHeight = calculateMaxTabHeight(tabPlacement);
        this.baseline = getBaseline(0);
        if (isHorizontalTabPlacement()) {
            for (int i2 = 1; i2 < tabCount; i2++) {
                if (getBaseline(i2) != this.baseline) {
                    this.baseline = -1;
                    return;
                }
            }
            return;
        }
        int height = getFontMetrics().getHeight();
        int iCalculateTabHeight = calculateTabHeight(tabPlacement, 0, height);
        for (int i3 = 1; i3 < tabCount; i3++) {
            if (iCalculateTabHeight != calculateTabHeight(tabPlacement, i3, height)) {
                this.baseline = -1;
                return;
            }
        }
    }

    @Override // javax.swing.plaf.ComponentUI
    public void paint(Graphics graphics, JComponent jComponent) {
        int selectedIndex = this.tabPane.getSelectedIndex();
        int tabPlacement = this.tabPane.getTabPlacement();
        ensureCurrentLayout();
        if (this.tabsOverlapBorder) {
            paintContentBorder(graphics, tabPlacement, selectedIndex);
        }
        if (!scrollableTabLayoutEnabled()) {
            paintTabArea(graphics, tabPlacement, selectedIndex);
        }
        if (!this.tabsOverlapBorder) {
            paintContentBorder(graphics, tabPlacement, selectedIndex);
        }
    }

    protected void paintTabArea(Graphics graphics, int i2, int i3) {
        int tabCount = this.tabPane.getTabCount();
        Rectangle rectangle = new Rectangle();
        Rectangle rectangle2 = new Rectangle();
        Rectangle clipBounds = graphics.getClipBounds();
        int i4 = this.runCount - 1;
        while (i4 >= 0) {
            int i5 = this.tabRuns[i4];
            int i6 = this.tabRuns[i4 == this.runCount - 1 ? 0 : i4 + 1];
            int i7 = i6 != 0 ? i6 - 1 : tabCount - 1;
            for (int i8 = i5; i8 <= i7; i8++) {
                if (i8 != i3 && this.rects[i8].intersects(clipBounds)) {
                    paintTab(graphics, i2, this.rects, i8, rectangle, rectangle2);
                }
            }
            i4--;
        }
        if (i3 >= 0 && this.rects[i3].intersects(clipBounds)) {
            paintTab(graphics, i2, this.rects, i3, rectangle, rectangle2);
        }
    }

    protected void paintTab(Graphics graphics, int i2, Rectangle[] rectangleArr, int i3, Rectangle rectangle, Rectangle rectangle2) {
        Rectangle rectangle3 = rectangleArr[i3];
        boolean z2 = this.tabPane.getSelectedIndex() == i3;
        if (this.tabsOpaque || this.tabPane.isOpaque()) {
            paintTabBackground(graphics, i2, i3, rectangle3.f12372x, rectangle3.f12373y, rectangle3.width, rectangle3.height, z2);
        }
        paintTabBorder(graphics, i2, i3, rectangle3.f12372x, rectangle3.f12373y, rectangle3.width, rectangle3.height, z2);
        String titleAt = this.tabPane.getTitleAt(i3);
        Font font = this.tabPane.getFont();
        FontMetrics fontMetrics = SwingUtilities2.getFontMetrics(this.tabPane, graphics, font);
        Icon iconForTab = getIconForTab(i3);
        layoutLabel(i2, fontMetrics, i3, titleAt, iconForTab, rectangle3, rectangle, rectangle2, z2);
        if (this.tabPane.getTabComponentAt(i3) == null) {
            String strClipStringIfNecessary = titleAt;
            if (scrollableTabLayoutEnabled() && this.tabScroller.croppedEdge.isParamsSet() && this.tabScroller.croppedEdge.getTabIndex() == i3 && isHorizontalTabPlacement()) {
                strClipStringIfNecessary = SwingUtilities2.clipStringIfNecessary(null, fontMetrics, titleAt, (this.tabScroller.croppedEdge.getCropline() - (rectangle2.f12372x - rectangle3.f12372x)) - this.tabScroller.croppedEdge.getCroppedSideWidth());
            } else if (!scrollableTabLayoutEnabled() && isHorizontalTabPlacement()) {
                strClipStringIfNecessary = SwingUtilities2.clipStringIfNecessary(null, fontMetrics, titleAt, rectangle2.width);
            }
            paintText(graphics, i2, font, fontMetrics, i3, strClipStringIfNecessary, rectangle2, z2);
            paintIcon(graphics, i2, i3, iconForTab, rectangle, z2);
        }
        paintFocusIndicator(graphics, i2, rectangleArr, i3, rectangle, rectangle2, z2);
    }

    private boolean isHorizontalTabPlacement() {
        return this.tabPane.getTabPlacement() == 1 || this.tabPane.getTabPlacement() == 3;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static Polygon createCroppedTabShape(int i2, Rectangle rectangle, int i3) {
        int i4;
        int i5;
        int i6;
        int i7;
        switch (i2) {
            case 1:
            case 3:
            default:
                i4 = rectangle.height;
                i5 = rectangle.f12373y;
                i6 = rectangle.f12373y + rectangle.height;
                i7 = rectangle.f12372x + rectangle.width;
                break;
            case 2:
            case 4:
                i4 = rectangle.width;
                i5 = rectangle.f12372x;
                i6 = rectangle.f12372x + rectangle.width;
                i7 = rectangle.f12373y + rectangle.height;
                break;
        }
        int i8 = i4 / 12;
        if (i4 % 12 > 0) {
            i8++;
        }
        int i9 = 2 + (i8 * 8);
        int[] iArr = new int[i9];
        int[] iArr2 = new int[i9];
        iArr[0] = i7;
        int i10 = 0 + 1;
        iArr2[0] = i6;
        iArr[i10] = i7;
        int i11 = i10 + 1;
        iArr2[i10] = i5;
        for (int i12 = 0; i12 < i8; i12++) {
            int i13 = 0;
            while (true) {
                if (i13 < xCropLen.length) {
                    iArr[i11] = i3 - xCropLen[i13];
                    iArr2[i11] = i5 + (i12 * 12) + yCropLen[i13];
                    if (iArr2[i11] >= i6) {
                        iArr2[i11] = i6;
                        i11++;
                    } else {
                        i11++;
                        i13++;
                    }
                }
            }
        }
        if (i2 == 1 || i2 == 3) {
            return new Polygon(iArr, iArr2, i11);
        }
        return new Polygon(iArr2, iArr, i11);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void paintCroppedTabEdge(Graphics graphics) {
        int tabIndex = this.tabScroller.croppedEdge.getTabIndex();
        int cropline = this.tabScroller.croppedEdge.getCropline();
        switch (this.tabPane.getTabPlacement()) {
            case 1:
            case 3:
            default:
                int i2 = this.rects[tabIndex].f12373y;
                graphics.setColor(this.shadow);
                for (int i3 = i2; i3 <= i2 + this.rects[tabIndex].height; i3 += 12) {
                    for (int i4 = 0; i4 < xCropLen.length; i4 += 2) {
                        graphics.drawLine(cropline - xCropLen[i4], i3 + yCropLen[i4], cropline - xCropLen[i4 + 1], (i3 + yCropLen[i4 + 1]) - 1);
                    }
                }
                break;
            case 2:
            case 4:
                int i5 = this.rects[tabIndex].f12372x;
                graphics.setColor(this.shadow);
                for (int i6 = i5; i6 <= i5 + this.rects[tabIndex].width; i6 += 12) {
                    for (int i7 = 0; i7 < xCropLen.length; i7 += 2) {
                        graphics.drawLine(i6 + yCropLen[i7], cropline - xCropLen[i7], (i6 + yCropLen[i7 + 1]) - 1, cropline - xCropLen[i7 + 1]);
                    }
                }
                break;
        }
    }

    protected void layoutLabel(int i2, FontMetrics fontMetrics, int i3, String str, Icon icon, Rectangle rectangle, Rectangle rectangle2, Rectangle rectangle3, boolean z2) {
        rectangle2.f12373y = 0;
        rectangle2.f12372x = 0;
        rectangle3.f12373y = 0;
        rectangle3.f12372x = 0;
        View textViewForTab = getTextViewForTab(i3);
        if (textViewForTab != null) {
            this.tabPane.putClientProperty("html", textViewForTab);
        }
        SwingUtilities.layoutCompoundLabel(this.tabPane, fontMetrics, str, icon, 0, 0, 0, 11, rectangle, rectangle2, rectangle3, this.textIconGap);
        this.tabPane.putClientProperty("html", null);
        int tabLabelShiftX = getTabLabelShiftX(i2, i3, z2);
        int tabLabelShiftY = getTabLabelShiftY(i2, i3, z2);
        rectangle2.f12372x += tabLabelShiftX;
        rectangle2.f12373y += tabLabelShiftY;
        rectangle3.f12372x += tabLabelShiftX;
        rectangle3.f12373y += tabLabelShiftY;
    }

    protected void paintIcon(Graphics graphics, int i2, int i3, Icon icon, Rectangle rectangle, boolean z2) {
        if (icon != null) {
            icon.paintIcon(this.tabPane, graphics, rectangle.f12372x, rectangle.f12373y);
        }
    }

    protected void paintText(Graphics graphics, int i2, Font font, FontMetrics fontMetrics, int i3, String str, Rectangle rectangle, boolean z2) {
        Color color;
        graphics.setFont(font);
        View textViewForTab = getTextViewForTab(i3);
        if (textViewForTab != null) {
            textViewForTab.paint(graphics, rectangle);
            return;
        }
        int displayedMnemonicIndexAt = this.tabPane.getDisplayedMnemonicIndexAt(i3);
        if (this.tabPane.isEnabled() && this.tabPane.isEnabledAt(i3)) {
            Color foregroundAt = this.tabPane.getForegroundAt(i3);
            if (z2 && (foregroundAt instanceof UIResource) && (color = UIManager.getColor("TabbedPane.selectedForeground")) != null) {
                foregroundAt = color;
            }
            graphics.setColor(foregroundAt);
            SwingUtilities2.drawStringUnderlineCharAt(this.tabPane, graphics, str, displayedMnemonicIndexAt, rectangle.f12372x, rectangle.f12373y + fontMetrics.getAscent());
            return;
        }
        graphics.setColor(this.tabPane.getBackgroundAt(i3).brighter());
        SwingUtilities2.drawStringUnderlineCharAt(this.tabPane, graphics, str, displayedMnemonicIndexAt, rectangle.f12372x, rectangle.f12373y + fontMetrics.getAscent());
        graphics.setColor(this.tabPane.getBackgroundAt(i3).darker());
        SwingUtilities2.drawStringUnderlineCharAt(this.tabPane, graphics, str, displayedMnemonicIndexAt, rectangle.f12372x - 1, (rectangle.f12373y + fontMetrics.getAscent()) - 1);
    }

    protected int getTabLabelShiftX(int i2, int i3, boolean z2) {
        Rectangle rectangle = this.rects[i3];
        int i4 = DefaultLookup.getInt(this.tabPane, this, "TabbedPane." + (z2 ? "selectedLabelShift" : "labelShift"), 1);
        switch (i2) {
            case 1:
            case 3:
            default:
                return rectangle.width % 2;
            case 2:
                return i4;
            case 4:
                return -i4;
        }
    }

    protected int getTabLabelShiftY(int i2, int i3, boolean z2) {
        Rectangle rectangle = this.rects[i3];
        int i4 = z2 ? DefaultLookup.getInt(this.tabPane, this, "TabbedPane.selectedLabelShift", -1) : DefaultLookup.getInt(this.tabPane, this, "TabbedPane.labelShift", 1);
        switch (i2) {
            case 1:
            default:
                return i4;
            case 2:
            case 4:
                return rectangle.height % 2;
            case 3:
                return -i4;
        }
    }

    protected void paintFocusIndicator(Graphics graphics, int i2, Rectangle[] rectangleArr, int i3, Rectangle rectangle, Rectangle rectangle2, boolean z2) {
        int i4;
        int i5;
        int i6;
        int i7;
        Rectangle rectangle3 = rectangleArr[i3];
        if (this.tabPane.hasFocus() && z2) {
            graphics.setColor(this.focus);
            switch (i2) {
                case 1:
                default:
                    i4 = rectangle3.f12372x + 3;
                    i5 = rectangle3.f12373y + 3;
                    i6 = rectangle3.width - 6;
                    i7 = rectangle3.height - 5;
                    break;
                case 2:
                    i4 = rectangle3.f12372x + 3;
                    i5 = rectangle3.f12373y + 3;
                    i6 = rectangle3.width - 5;
                    i7 = rectangle3.height - 6;
                    break;
                case 3:
                    i4 = rectangle3.f12372x + 3;
                    i5 = rectangle3.f12373y + 2;
                    i6 = rectangle3.width - 6;
                    i7 = rectangle3.height - 5;
                    break;
                case 4:
                    i4 = rectangle3.f12372x + 2;
                    i5 = rectangle3.f12373y + 3;
                    i6 = rectangle3.width - 5;
                    i7 = rectangle3.height - 6;
                    break;
            }
            BasicGraphicsUtils.drawDashedRect(graphics, i4, i5, i6, i7);
        }
    }

    protected void paintTabBorder(Graphics graphics, int i2, int i3, int i4, int i5, int i6, int i7, boolean z2) {
        graphics.setColor(this.lightHighlight);
        switch (i2) {
            case 1:
            default:
                graphics.drawLine(i4, i5 + 2, i4, (i5 + i7) - 1);
                graphics.drawLine(i4 + 1, i5 + 1, i4 + 1, i5 + 1);
                graphics.drawLine(i4 + 2, i5, (i4 + i6) - 3, i5);
                graphics.setColor(this.shadow);
                graphics.drawLine((i4 + i6) - 2, i5 + 2, (i4 + i6) - 2, (i5 + i7) - 1);
                graphics.setColor(this.darkShadow);
                graphics.drawLine((i4 + i6) - 1, i5 + 2, (i4 + i6) - 1, (i5 + i7) - 1);
                graphics.drawLine((i4 + i6) - 2, i5 + 1, (i4 + i6) - 2, i5 + 1);
                break;
            case 2:
                graphics.drawLine(i4 + 1, (i5 + i7) - 2, i4 + 1, (i5 + i7) - 2);
                graphics.drawLine(i4, i5 + 2, i4, (i5 + i7) - 3);
                graphics.drawLine(i4 + 1, i5 + 1, i4 + 1, i5 + 1);
                graphics.drawLine(i4 + 2, i5, (i4 + i6) - 1, i5);
                graphics.setColor(this.shadow);
                graphics.drawLine(i4 + 2, (i5 + i7) - 2, (i4 + i6) - 1, (i5 + i7) - 2);
                graphics.setColor(this.darkShadow);
                graphics.drawLine(i4 + 2, (i5 + i7) - 1, (i4 + i6) - 1, (i5 + i7) - 1);
                break;
            case 3:
                graphics.drawLine(i4, i5, i4, (i5 + i7) - 3);
                graphics.drawLine(i4 + 1, (i5 + i7) - 2, i4 + 1, (i5 + i7) - 2);
                graphics.setColor(this.shadow);
                graphics.drawLine(i4 + 2, (i5 + i7) - 2, (i4 + i6) - 3, (i5 + i7) - 2);
                graphics.drawLine((i4 + i6) - 2, i5, (i4 + i6) - 2, (i5 + i7) - 3);
                graphics.setColor(this.darkShadow);
                graphics.drawLine(i4 + 2, (i5 + i7) - 1, (i4 + i6) - 3, (i5 + i7) - 1);
                graphics.drawLine((i4 + i6) - 2, (i5 + i7) - 2, (i4 + i6) - 2, (i5 + i7) - 2);
                graphics.drawLine((i4 + i6) - 1, i5, (i4 + i6) - 1, (i5 + i7) - 3);
                break;
            case 4:
                graphics.drawLine(i4, i5, (i4 + i6) - 3, i5);
                graphics.setColor(this.shadow);
                graphics.drawLine(i4, (i5 + i7) - 2, (i4 + i6) - 3, (i5 + i7) - 2);
                graphics.drawLine((i4 + i6) - 2, i5 + 2, (i4 + i6) - 2, (i5 + i7) - 3);
                graphics.setColor(this.darkShadow);
                graphics.drawLine((i4 + i6) - 2, i5 + 1, (i4 + i6) - 2, i5 + 1);
                graphics.drawLine((i4 + i6) - 2, (i5 + i7) - 2, (i4 + i6) - 2, (i5 + i7) - 2);
                graphics.drawLine((i4 + i6) - 1, i5 + 2, (i4 + i6) - 1, (i5 + i7) - 3);
                graphics.drawLine(i4, (i5 + i7) - 1, (i4 + i6) - 3, (i5 + i7) - 1);
                break;
        }
    }

    protected void paintTabBackground(Graphics graphics, int i2, int i3, int i4, int i5, int i6, int i7, boolean z2) {
        graphics.setColor((!z2 || this.selectedColor == null) ? this.tabPane.getBackgroundAt(i3) : this.selectedColor);
        switch (i2) {
            case 1:
            default:
                graphics.fillRect(i4 + 1, i5 + 1, i6 - 3, i7 - 1);
                break;
            case 2:
                graphics.fillRect(i4 + 1, i5 + 1, i6 - 1, i7 - 3);
                break;
            case 3:
                graphics.fillRect(i4 + 1, i5, i6 - 3, i7 - 1);
                break;
            case 4:
                graphics.fillRect(i4, i5 + 1, i6 - 2, i7 - 3);
                break;
        }
    }

    protected void paintContentBorder(Graphics graphics, int i2, int i3) {
        int width = this.tabPane.getWidth();
        int height = this.tabPane.getHeight();
        Insets insets = this.tabPane.getInsets();
        Insets tabAreaInsets = getTabAreaInsets(i2);
        int iCalculateTabAreaWidth = insets.left;
        int iCalculateTabAreaHeight = insets.top;
        int iCalculateTabAreaWidth2 = (width - insets.right) - insets.left;
        int iCalculateTabAreaHeight2 = (height - insets.top) - insets.bottom;
        switch (i2) {
            case 1:
            default:
                iCalculateTabAreaHeight += calculateTabAreaHeight(i2, this.runCount, this.maxTabHeight);
                if (this.tabsOverlapBorder) {
                    iCalculateTabAreaHeight -= tabAreaInsets.bottom;
                }
                iCalculateTabAreaHeight2 -= iCalculateTabAreaHeight - insets.top;
                break;
            case 2:
                iCalculateTabAreaWidth += calculateTabAreaWidth(i2, this.runCount, this.maxTabWidth);
                if (this.tabsOverlapBorder) {
                    iCalculateTabAreaWidth -= tabAreaInsets.right;
                }
                iCalculateTabAreaWidth2 -= iCalculateTabAreaWidth - insets.left;
                break;
            case 3:
                iCalculateTabAreaHeight2 -= calculateTabAreaHeight(i2, this.runCount, this.maxTabHeight);
                if (this.tabsOverlapBorder) {
                    iCalculateTabAreaHeight2 += tabAreaInsets.top;
                    break;
                }
                break;
            case 4:
                iCalculateTabAreaWidth2 -= calculateTabAreaWidth(i2, this.runCount, this.maxTabWidth);
                if (this.tabsOverlapBorder) {
                    iCalculateTabAreaWidth2 += tabAreaInsets.left;
                    break;
                }
                break;
        }
        if (this.tabPane.getTabCount() > 0 && (this.contentOpaque || this.tabPane.isOpaque())) {
            Color color = UIManager.getColor("TabbedPane.contentAreaColor");
            if (color != null) {
                graphics.setColor(color);
            } else if (this.selectedColor == null || i3 == -1) {
                graphics.setColor(this.tabPane.getBackground());
            } else {
                graphics.setColor(this.selectedColor);
            }
            graphics.fillRect(iCalculateTabAreaWidth, iCalculateTabAreaHeight, iCalculateTabAreaWidth2, iCalculateTabAreaHeight2);
        }
        paintContentBorderTopEdge(graphics, i2, i3, iCalculateTabAreaWidth, iCalculateTabAreaHeight, iCalculateTabAreaWidth2, iCalculateTabAreaHeight2);
        paintContentBorderLeftEdge(graphics, i2, i3, iCalculateTabAreaWidth, iCalculateTabAreaHeight, iCalculateTabAreaWidth2, iCalculateTabAreaHeight2);
        paintContentBorderBottomEdge(graphics, i2, i3, iCalculateTabAreaWidth, iCalculateTabAreaHeight, iCalculateTabAreaWidth2, iCalculateTabAreaHeight2);
        paintContentBorderRightEdge(graphics, i2, i3, iCalculateTabAreaWidth, iCalculateTabAreaHeight, iCalculateTabAreaWidth2, iCalculateTabAreaHeight2);
    }

    protected void paintContentBorderTopEdge(Graphics graphics, int i2, int i3, int i4, int i5, int i6, int i7) {
        Rectangle tabBounds = i3 < 0 ? null : getTabBounds(i3, this.calcRect);
        graphics.setColor(this.lightHighlight);
        if (i2 != 1 || i3 < 0 || tabBounds.f12373y + tabBounds.height + 1 < i5 || tabBounds.f12372x < i4 || tabBounds.f12372x > i4 + i6) {
            graphics.drawLine(i4, i5, (i4 + i6) - 2, i5);
            return;
        }
        graphics.drawLine(i4, i5, tabBounds.f12372x - 1, i5);
        if (tabBounds.f12372x + tabBounds.width < (i4 + i6) - 2) {
            graphics.drawLine(tabBounds.f12372x + tabBounds.width, i5, (i4 + i6) - 2, i5);
        } else {
            graphics.setColor(this.shadow);
            graphics.drawLine((i4 + i6) - 2, i5, (i4 + i6) - 2, i5);
        }
    }

    protected void paintContentBorderLeftEdge(Graphics graphics, int i2, int i3, int i4, int i5, int i6, int i7) {
        Rectangle tabBounds = i3 < 0 ? null : getTabBounds(i3, this.calcRect);
        graphics.setColor(this.lightHighlight);
        if (i2 != 2 || i3 < 0 || tabBounds.f12372x + tabBounds.width + 1 < i4 || tabBounds.f12373y < i5 || tabBounds.f12373y > i5 + i7) {
            graphics.drawLine(i4, i5, i4, (i5 + i7) - 2);
            return;
        }
        graphics.drawLine(i4, i5, i4, tabBounds.f12373y - 1);
        if (tabBounds.f12373y + tabBounds.height < (i5 + i7) - 2) {
            graphics.drawLine(i4, tabBounds.f12373y + tabBounds.height, i4, (i5 + i7) - 2);
        }
    }

    protected void paintContentBorderBottomEdge(Graphics graphics, int i2, int i3, int i4, int i5, int i6, int i7) {
        Rectangle tabBounds = i3 < 0 ? null : getTabBounds(i3, this.calcRect);
        graphics.setColor(this.shadow);
        if (i2 != 3 || i3 < 0 || tabBounds.f12373y - 1 > i7 || tabBounds.f12372x < i4 || tabBounds.f12372x > i4 + i6) {
            graphics.drawLine(i4 + 1, (i5 + i7) - 2, (i4 + i6) - 2, (i5 + i7) - 2);
            graphics.setColor(this.darkShadow);
            graphics.drawLine(i4, (i5 + i7) - 1, (i4 + i6) - 1, (i5 + i7) - 1);
            return;
        }
        graphics.drawLine(i4 + 1, (i5 + i7) - 2, tabBounds.f12372x - 1, (i5 + i7) - 2);
        graphics.setColor(this.darkShadow);
        graphics.drawLine(i4, (i5 + i7) - 1, tabBounds.f12372x - 1, (i5 + i7) - 1);
        if (tabBounds.f12372x + tabBounds.width < (i4 + i6) - 2) {
            graphics.setColor(this.shadow);
            graphics.drawLine(tabBounds.f12372x + tabBounds.width, (i5 + i7) - 2, (i4 + i6) - 2, (i5 + i7) - 2);
            graphics.setColor(this.darkShadow);
            graphics.drawLine(tabBounds.f12372x + tabBounds.width, (i5 + i7) - 1, (i4 + i6) - 1, (i5 + i7) - 1);
        }
    }

    protected void paintContentBorderRightEdge(Graphics graphics, int i2, int i3, int i4, int i5, int i6, int i7) {
        Rectangle tabBounds = i3 < 0 ? null : getTabBounds(i3, this.calcRect);
        graphics.setColor(this.shadow);
        if (i2 != 4 || i3 < 0 || tabBounds.f12372x - 1 > i6 || tabBounds.f12373y < i5 || tabBounds.f12373y > i5 + i7) {
            graphics.drawLine((i4 + i6) - 2, i5 + 1, (i4 + i6) - 2, (i5 + i7) - 3);
            graphics.setColor(this.darkShadow);
            graphics.drawLine((i4 + i6) - 1, i5, (i4 + i6) - 1, (i5 + i7) - 1);
            return;
        }
        graphics.drawLine((i4 + i6) - 2, i5 + 1, (i4 + i6) - 2, tabBounds.f12373y - 1);
        graphics.setColor(this.darkShadow);
        graphics.drawLine((i4 + i6) - 1, i5, (i4 + i6) - 1, tabBounds.f12373y - 1);
        if (tabBounds.f12373y + tabBounds.height < (i5 + i7) - 2) {
            graphics.setColor(this.shadow);
            graphics.drawLine((i4 + i6) - 2, tabBounds.f12373y + tabBounds.height, (i4 + i6) - 2, (i5 + i7) - 2);
            graphics.setColor(this.darkShadow);
            graphics.drawLine((i4 + i6) - 1, tabBounds.f12373y + tabBounds.height, (i4 + i6) - 1, (i5 + i7) - 2);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void ensureCurrentLayout() {
        if (!this.tabPane.isValid()) {
            this.tabPane.validate();
        }
        if (!this.tabPane.isValid()) {
            ((TabbedPaneLayout) this.tabPane.getLayout()).calculateLayoutInfo();
        }
    }

    @Override // javax.swing.plaf.TabbedPaneUI
    public Rectangle getTabBounds(JTabbedPane jTabbedPane, int i2) {
        ensureCurrentLayout();
        return getTabBounds(i2, new Rectangle());
    }

    @Override // javax.swing.plaf.TabbedPaneUI
    public int getTabRunCount(JTabbedPane jTabbedPane) {
        ensureCurrentLayout();
        return this.runCount;
    }

    @Override // javax.swing.plaf.TabbedPaneUI
    public int tabForCoordinate(JTabbedPane jTabbedPane, int i2, int i3) {
        return tabForCoordinate(jTabbedPane, i2, i3, true);
    }

    private int tabForCoordinate(JTabbedPane jTabbedPane, int i2, int i3, boolean z2) {
        if (z2) {
            ensureCurrentLayout();
        }
        if (this.isRunsDirty) {
            return -1;
        }
        Point point = new Point(i2, i3);
        if (scrollableTabLayoutEnabled()) {
            translatePointToTabPanel(i2, i3, point);
            if (!this.tabScroller.viewport.getViewRect().contains(point)) {
                return -1;
            }
        }
        int tabCount = this.tabPane.getTabCount();
        for (int i4 = 0; i4 < tabCount; i4++) {
            if (this.rects[i4].contains(point.f12370x, point.f12371y)) {
                return i4;
            }
        }
        return -1;
    }

    protected Rectangle getTabBounds(int i2, Rectangle rectangle) {
        rectangle.width = this.rects[i2].width;
        rectangle.height = this.rects[i2].height;
        if (scrollableTabLayoutEnabled()) {
            Point location = this.tabScroller.viewport.getLocation();
            Point viewPosition = this.tabScroller.viewport.getViewPosition();
            rectangle.f12372x = (this.rects[i2].f12372x + location.f12370x) - viewPosition.f12370x;
            rectangle.f12373y = (this.rects[i2].f12373y + location.f12371y) - viewPosition.f12371y;
        } else {
            rectangle.f12372x = this.rects[i2].f12372x;
            rectangle.f12373y = this.rects[i2].f12373y;
        }
        return rectangle;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int getClosestTab(int i2, int i3) {
        int i4;
        int i5;
        int i6 = 0;
        int iMin = Math.min(this.rects.length, this.tabPane.getTabCount());
        int i7 = iMin;
        int tabPlacement = this.tabPane.getTabPlacement();
        boolean z2 = tabPlacement == 1 || tabPlacement == 3;
        int i8 = z2 ? i2 : i3;
        while (i6 != i7) {
            int i9 = (i7 + i6) / 2;
            if (z2) {
                i4 = this.rects[i9].f12372x;
                i5 = i4 + this.rects[i9].width;
            } else {
                i4 = this.rects[i9].f12373y;
                i5 = i4 + this.rects[i9].height;
            }
            if (i8 < i4) {
                i7 = i9;
                if (i6 == i7) {
                    return Math.max(0, i9 - 1);
                }
            } else if (i8 >= i5) {
                i6 = i9;
                if (i7 - i6 <= 1) {
                    return Math.max(i9 + 1, iMin - 1);
                }
            } else {
                return i9;
            }
        }
        return i6;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Point translatePointToTabPanel(int i2, int i3, Point point) {
        Point location = this.tabScroller.viewport.getLocation();
        Point viewPosition = this.tabScroller.viewport.getViewPosition();
        point.f12370x = (i2 - location.f12370x) + viewPosition.f12370x;
        point.f12371y = (i3 - location.f12371y) + viewPosition.f12371y;
        return point;
    }

    protected Component getVisibleComponent() {
        return this.visibleComponent;
    }

    protected void setVisibleComponent(Component component) {
        if (this.visibleComponent != null && this.visibleComponent != component && this.visibleComponent.getParent() == this.tabPane && this.visibleComponent.isVisible()) {
            this.visibleComponent.setVisible(false);
        }
        if (component != null && !component.isVisible()) {
            component.setVisible(true);
        }
        this.visibleComponent = component;
    }

    protected void assureRectsCreated(int i2) {
        int length = this.rects.length;
        if (i2 != length) {
            Rectangle[] rectangleArr = new Rectangle[i2];
            System.arraycopy(this.rects, 0, rectangleArr, 0, Math.min(length, i2));
            this.rects = rectangleArr;
            for (int i3 = length; i3 < i2; i3++) {
                this.rects[i3] = new Rectangle();
            }
        }
    }

    protected void expandTabRunsArray() {
        int[] iArr = new int[this.tabRuns.length + 10];
        System.arraycopy(this.tabRuns, 0, iArr, 0, this.runCount);
        this.tabRuns = iArr;
    }

    protected int getRunForTab(int i2, int i3) {
        for (int i4 = 0; i4 < this.runCount; i4++) {
            int i5 = this.tabRuns[i4];
            int iLastTabInRun = lastTabInRun(i2, i4);
            if (i3 >= i5 && i3 <= iLastTabInRun) {
                return i4;
            }
        }
        return 0;
    }

    protected int lastTabInRun(int i2, int i3) {
        if (this.runCount == 1) {
            return i2 - 1;
        }
        int i4 = i3 == this.runCount - 1 ? 0 : i3 + 1;
        if (this.tabRuns[i4] == 0) {
            return i2 - 1;
        }
        return this.tabRuns[i4] - 1;
    }

    protected int getTabRunOverlay(int i2) {
        return this.tabRunOverlay;
    }

    protected int getTabRunIndent(int i2, int i3) {
        return 0;
    }

    protected boolean shouldPadTabRun(int i2, int i3) {
        return this.runCount > 1;
    }

    protected boolean shouldRotateTabRuns(int i2) {
        return true;
    }

    protected Icon getIconForTab(int i2) {
        return (this.tabPane.isEnabled() && this.tabPane.isEnabledAt(i2)) ? this.tabPane.getIconAt(i2) : this.tabPane.getDisabledIconAt(i2);
    }

    protected View getTextViewForTab(int i2) {
        if (this.htmlViews != null) {
            return this.htmlViews.elementAt(i2);
        }
        return null;
    }

    protected int calculateTabHeight(int i2, int i3, int i4) {
        int iMax;
        Component tabComponentAt = this.tabPane.getTabComponentAt(i3);
        if (tabComponentAt != null) {
            iMax = tabComponentAt.getPreferredSize().height;
        } else {
            View textViewForTab = getTextViewForTab(i3);
            if (textViewForTab != null) {
                iMax = 0 + ((int) textViewForTab.getPreferredSpan(1));
            } else {
                iMax = 0 + i4;
            }
            Icon iconForTab = getIconForTab(i3);
            if (iconForTab != null) {
                iMax = Math.max(iMax, iconForTab.getIconHeight());
            }
        }
        Insets tabInsets = getTabInsets(i2, i3);
        return iMax + tabInsets.top + tabInsets.bottom + 2;
    }

    protected int calculateMaxTabHeight(int i2) {
        FontMetrics fontMetrics = getFontMetrics();
        int tabCount = this.tabPane.getTabCount();
        int iMax = 0;
        int height = fontMetrics.getHeight();
        for (int i3 = 0; i3 < tabCount; i3++) {
            iMax = Math.max(calculateTabHeight(i2, i3, height), iMax);
        }
        return iMax;
    }

    protected int calculateTabWidth(int i2, int i3, FontMetrics fontMetrics) {
        int iStringWidth;
        Insets tabInsets = getTabInsets(i2, i3);
        int iconWidth = tabInsets.left + tabInsets.right + 3;
        Component tabComponentAt = this.tabPane.getTabComponentAt(i3);
        if (tabComponentAt != null) {
            iStringWidth = iconWidth + tabComponentAt.getPreferredSize().width;
        } else {
            Icon iconForTab = getIconForTab(i3);
            if (iconForTab != null) {
                iconWidth += iconForTab.getIconWidth() + this.textIconGap;
            }
            View textViewForTab = getTextViewForTab(i3);
            if (textViewForTab != null) {
                iStringWidth = iconWidth + ((int) textViewForTab.getPreferredSpan(0));
            } else {
                iStringWidth = iconWidth + SwingUtilities2.stringWidth(this.tabPane, fontMetrics, this.tabPane.getTitleAt(i3));
            }
        }
        return iStringWidth;
    }

    protected int calculateMaxTabWidth(int i2) {
        FontMetrics fontMetrics = getFontMetrics();
        int tabCount = this.tabPane.getTabCount();
        int iMax = 0;
        for (int i3 = 0; i3 < tabCount; i3++) {
            iMax = Math.max(calculateTabWidth(i2, i3, fontMetrics), iMax);
        }
        return iMax;
    }

    protected int calculateTabAreaHeight(int i2, int i3, int i4) {
        Insets tabAreaInsets = getTabAreaInsets(i2);
        int tabRunOverlay = getTabRunOverlay(i2);
        if (i3 > 0) {
            return (i3 * (i4 - tabRunOverlay)) + tabRunOverlay + tabAreaInsets.top + tabAreaInsets.bottom;
        }
        return 0;
    }

    protected int calculateTabAreaWidth(int i2, int i3, int i4) {
        Insets tabAreaInsets = getTabAreaInsets(i2);
        int tabRunOverlay = getTabRunOverlay(i2);
        if (i3 > 0) {
            return (i3 * (i4 - tabRunOverlay)) + tabRunOverlay + tabAreaInsets.left + tabAreaInsets.right;
        }
        return 0;
    }

    protected Insets getTabInsets(int i2, int i3) {
        return this.tabInsets;
    }

    protected Insets getSelectedTabPadInsets(int i2) {
        rotateInsets(this.selectedTabPadInsets, this.currentPadInsets, i2);
        return this.currentPadInsets;
    }

    protected Insets getTabAreaInsets(int i2) {
        rotateInsets(this.tabAreaInsets, this.currentTabAreaInsets, i2);
        return this.currentTabAreaInsets;
    }

    protected Insets getContentBorderInsets(int i2) {
        return this.contentBorderInsets;
    }

    protected FontMetrics getFontMetrics() {
        return this.tabPane.getFontMetrics(this.tabPane.getFont());
    }

    protected void navigateSelectedTab(int i2) {
        int tabPlacement = this.tabPane.getTabPlacement();
        int selectedIndex = DefaultLookup.getBoolean(this.tabPane, this, "TabbedPane.selectionFollowsFocus", true) ? this.tabPane.getSelectedIndex() : getFocusIndex();
        int tabCount = this.tabPane.getTabCount();
        boolean zIsLeftToRight = BasicGraphicsUtils.isLeftToRight(this.tabPane);
        if (tabCount <= 0) {
        }
        switch (tabPlacement) {
            case 1:
            case 3:
            default:
                switch (i2) {
                    case 1:
                        selectAdjacentRunTab(tabPlacement, selectedIndex, getTabRunOffset(tabPlacement, tabCount, selectedIndex, false));
                        break;
                    case 3:
                        if (zIsLeftToRight) {
                            selectNextTabInRun(selectedIndex);
                            break;
                        } else {
                            selectPreviousTabInRun(selectedIndex);
                            break;
                        }
                    case 5:
                        selectAdjacentRunTab(tabPlacement, selectedIndex, getTabRunOffset(tabPlacement, tabCount, selectedIndex, true));
                        break;
                    case 7:
                        if (zIsLeftToRight) {
                            selectPreviousTabInRun(selectedIndex);
                            break;
                        } else {
                            selectNextTabInRun(selectedIndex);
                            break;
                        }
                    case 12:
                        selectNextTab(selectedIndex);
                        break;
                    case 13:
                        selectPreviousTab(selectedIndex);
                        break;
                }
            case 2:
            case 4:
                switch (i2) {
                    case 1:
                        selectPreviousTabInRun(selectedIndex);
                        break;
                    case 3:
                        selectAdjacentRunTab(tabPlacement, selectedIndex, getTabRunOffset(tabPlacement, tabCount, selectedIndex, true));
                        break;
                    case 5:
                        selectNextTabInRun(selectedIndex);
                        break;
                    case 7:
                        selectAdjacentRunTab(tabPlacement, selectedIndex, getTabRunOffset(tabPlacement, tabCount, selectedIndex, false));
                        break;
                    case 12:
                        selectNextTab(selectedIndex);
                        break;
                    case 13:
                        selectPreviousTab(selectedIndex);
                        break;
                }
        }
    }

    protected void selectNextTabInRun(int i2) {
        int i3;
        int tabCount = this.tabPane.getTabCount();
        int nextTabIndexInRun = getNextTabIndexInRun(tabCount, i2);
        while (true) {
            i3 = nextTabIndexInRun;
            if (i3 == i2 || this.tabPane.isEnabledAt(i3)) {
                break;
            } else {
                nextTabIndexInRun = getNextTabIndexInRun(tabCount, i3);
            }
        }
        navigateTo(i3);
    }

    protected void selectPreviousTabInRun(int i2) {
        int i3;
        int tabCount = this.tabPane.getTabCount();
        int previousTabIndexInRun = getPreviousTabIndexInRun(tabCount, i2);
        while (true) {
            i3 = previousTabIndexInRun;
            if (i3 == i2 || this.tabPane.isEnabledAt(i3)) {
                break;
            } else {
                previousTabIndexInRun = getPreviousTabIndexInRun(tabCount, i3);
            }
        }
        navigateTo(i3);
    }

    protected void selectNextTab(int i2) {
        int i3;
        int nextTabIndex = getNextTabIndex(i2);
        while (true) {
            i3 = nextTabIndex;
            if (i3 == i2 || this.tabPane.isEnabledAt(i3)) {
                break;
            } else {
                nextTabIndex = getNextTabIndex(i3);
            }
        }
        navigateTo(i3);
    }

    protected void selectPreviousTab(int i2) {
        int i3;
        int previousTabIndex = getPreviousTabIndex(i2);
        while (true) {
            i3 = previousTabIndex;
            if (i3 == i2 || this.tabPane.isEnabledAt(i3)) {
                break;
            } else {
                previousTabIndex = getPreviousTabIndex(i3);
            }
        }
        navigateTo(i3);
    }

    protected void selectAdjacentRunTab(int i2, int i3, int i4) {
        int iTabForCoordinate;
        if (this.runCount < 2) {
            return;
        }
        Rectangle rectangle = this.rects[i3];
        switch (i2) {
            case 1:
            case 3:
            default:
                iTabForCoordinate = tabForCoordinate(this.tabPane, rectangle.f12372x + (rectangle.width / 2), rectangle.f12373y + (rectangle.height / 2) + i4);
                break;
            case 2:
            case 4:
                iTabForCoordinate = tabForCoordinate(this.tabPane, rectangle.f12372x + (rectangle.width / 2) + i4, rectangle.f12373y + (rectangle.height / 2));
                break;
        }
        if (iTabForCoordinate != -1) {
            while (!this.tabPane.isEnabledAt(iTabForCoordinate) && iTabForCoordinate != i3) {
                iTabForCoordinate = getNextTabIndex(iTabForCoordinate);
            }
            navigateTo(iTabForCoordinate);
        }
    }

    private void navigateTo(int i2) {
        if (DefaultLookup.getBoolean(this.tabPane, this, "TabbedPane.selectionFollowsFocus", true)) {
            this.tabPane.setSelectedIndex(i2);
        } else {
            setFocusIndex(i2, true);
        }
    }

    void setFocusIndex(int i2, boolean z2) {
        if (z2 && !this.isRunsDirty) {
            repaintTab(this.focusIndex);
            this.focusIndex = i2;
            repaintTab(this.focusIndex);
            return;
        }
        this.focusIndex = i2;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void repaintTab(int i2) {
        if (!this.isRunsDirty && i2 >= 0 && i2 < this.tabPane.getTabCount()) {
            this.tabPane.repaint(getTabBounds(this.tabPane, i2));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void validateFocusIndex() {
        if (this.focusIndex >= this.tabPane.getTabCount()) {
            setFocusIndex(this.tabPane.getSelectedIndex(), false);
        }
    }

    protected int getFocusIndex() {
        return this.focusIndex;
    }

    protected int getTabRunOffset(int i2, int i3, int i4, boolean z2) {
        int iCalculateTabAreaHeight;
        int runForTab = getRunForTab(i3, i4);
        switch (i2) {
            case 1:
            default:
                if (runForTab == 0) {
                    iCalculateTabAreaHeight = z2 ? -(calculateTabAreaHeight(i2, this.runCount, this.maxTabHeight) - this.maxTabHeight) : -this.maxTabHeight;
                    break;
                } else if (runForTab == this.runCount - 1) {
                    iCalculateTabAreaHeight = z2 ? this.maxTabHeight : calculateTabAreaHeight(i2, this.runCount, this.maxTabHeight) - this.maxTabHeight;
                    break;
                } else {
                    iCalculateTabAreaHeight = z2 ? this.maxTabHeight : -this.maxTabHeight;
                    break;
                }
            case 2:
                if (runForTab == 0) {
                    iCalculateTabAreaHeight = z2 ? -(calculateTabAreaWidth(i2, this.runCount, this.maxTabWidth) - this.maxTabWidth) : -this.maxTabWidth;
                    break;
                } else if (runForTab == this.runCount - 1) {
                    iCalculateTabAreaHeight = z2 ? this.maxTabWidth : calculateTabAreaWidth(i2, this.runCount, this.maxTabWidth) - this.maxTabWidth;
                    break;
                } else {
                    iCalculateTabAreaHeight = z2 ? this.maxTabWidth : -this.maxTabWidth;
                    break;
                }
            case 3:
                if (runForTab == 0) {
                    iCalculateTabAreaHeight = z2 ? this.maxTabHeight : calculateTabAreaHeight(i2, this.runCount, this.maxTabHeight) - this.maxTabHeight;
                    break;
                } else if (runForTab == this.runCount - 1) {
                    iCalculateTabAreaHeight = z2 ? -(calculateTabAreaHeight(i2, this.runCount, this.maxTabHeight) - this.maxTabHeight) : -this.maxTabHeight;
                    break;
                } else {
                    iCalculateTabAreaHeight = z2 ? this.maxTabHeight : -this.maxTabHeight;
                    break;
                }
            case 4:
                if (runForTab == 0) {
                    iCalculateTabAreaHeight = z2 ? this.maxTabWidth : calculateTabAreaWidth(i2, this.runCount, this.maxTabWidth) - this.maxTabWidth;
                    break;
                } else if (runForTab == this.runCount - 1) {
                    iCalculateTabAreaHeight = z2 ? -(calculateTabAreaWidth(i2, this.runCount, this.maxTabWidth) - this.maxTabWidth) : -this.maxTabWidth;
                    break;
                } else {
                    iCalculateTabAreaHeight = z2 ? this.maxTabWidth : -this.maxTabWidth;
                    break;
                }
        }
        return iCalculateTabAreaHeight;
    }

    protected int getPreviousTabIndex(int i2) {
        int tabCount = i2 - 1 >= 0 ? i2 - 1 : this.tabPane.getTabCount() - 1;
        if (tabCount >= 0) {
            return tabCount;
        }
        return 0;
    }

    protected int getNextTabIndex(int i2) {
        return (i2 + 1) % this.tabPane.getTabCount();
    }

    protected int getNextTabIndexInRun(int i2, int i3) {
        if (this.runCount < 2) {
            return getNextTabIndex(i3);
        }
        int runForTab = getRunForTab(i2, i3);
        int nextTabIndex = getNextTabIndex(i3);
        if (nextTabIndex == this.tabRuns[getNextTabRun(runForTab)]) {
            return this.tabRuns[runForTab];
        }
        return nextTabIndex;
    }

    protected int getPreviousTabIndexInRun(int i2, int i3) {
        if (this.runCount < 2) {
            return getPreviousTabIndex(i3);
        }
        int runForTab = getRunForTab(i2, i3);
        if (i3 == this.tabRuns[runForTab]) {
            int i4 = this.tabRuns[getNextTabRun(runForTab)] - 1;
            return i4 != -1 ? i4 : i2 - 1;
        }
        return getPreviousTabIndex(i3);
    }

    protected int getPreviousTabRun(int i2) {
        int i3 = i2 - 1 >= 0 ? i2 - 1 : this.runCount - 1;
        if (i3 >= 0) {
            return i3;
        }
        return 0;
    }

    protected int getNextTabRun(int i2) {
        return (i2 + 1) % this.runCount;
    }

    protected static void rotateInsets(Insets insets, Insets insets2, int i2) {
        switch (i2) {
            case 1:
            default:
                insets2.top = insets.top;
                insets2.left = insets.left;
                insets2.bottom = insets.bottom;
                insets2.right = insets.right;
                break;
            case 2:
                insets2.top = insets.left;
                insets2.left = insets.top;
                insets2.bottom = insets.right;
                insets2.right = insets.bottom;
                break;
            case 3:
                insets2.top = insets.bottom;
                insets2.left = insets.left;
                insets2.bottom = insets.top;
                insets2.right = insets.right;
                break;
            case 4:
                insets2.top = insets.left;
                insets2.left = insets.bottom;
                insets2.bottom = insets.right;
                insets2.right = insets.top;
                break;
        }
    }

    boolean requestFocusForVisibleComponent() {
        return SwingUtilities2.tabbedPaneChangeFocusTo(getVisibleComponent());
    }

    /* loaded from: rt.jar:javax/swing/plaf/basic/BasicTabbedPaneUI$Actions.class */
    private static class Actions extends UIAction {
        static final String NEXT = "navigateNext";
        static final String PREVIOUS = "navigatePrevious";
        static final String RIGHT = "navigateRight";
        static final String LEFT = "navigateLeft";
        static final String UP = "navigateUp";
        static final String DOWN = "navigateDown";
        static final String PAGE_UP = "navigatePageUp";
        static final String PAGE_DOWN = "navigatePageDown";
        static final String REQUEST_FOCUS = "requestFocus";
        static final String REQUEST_FOCUS_FOR_VISIBLE = "requestFocusForVisibleComponent";
        static final String SET_SELECTED = "setSelectedIndex";
        static final String SELECT_FOCUSED = "selectTabWithFocus";
        static final String SCROLL_FORWARD = "scrollTabsForwardAction";
        static final String SCROLL_BACKWARD = "scrollTabsBackwardAction";

        Actions(String str) {
            super(str);
        }

        @Override // java.awt.event.ActionListener
        public void actionPerformed(ActionEvent actionEvent) {
            String name = getName();
            JTabbedPane jTabbedPane = (JTabbedPane) actionEvent.getSource();
            BasicTabbedPaneUI basicTabbedPaneUI = (BasicTabbedPaneUI) BasicLookAndFeel.getUIOfType(jTabbedPane.getUI(), BasicTabbedPaneUI.class);
            if (basicTabbedPaneUI == null) {
                return;
            }
            if (name == NEXT) {
                basicTabbedPaneUI.navigateSelectedTab(12);
                return;
            }
            if (name == PREVIOUS) {
                basicTabbedPaneUI.navigateSelectedTab(13);
                return;
            }
            if (name == RIGHT) {
                basicTabbedPaneUI.navigateSelectedTab(3);
                return;
            }
            if (name == LEFT) {
                basicTabbedPaneUI.navigateSelectedTab(7);
                return;
            }
            if (name == UP) {
                basicTabbedPaneUI.navigateSelectedTab(1);
                return;
            }
            if (name == DOWN) {
                basicTabbedPaneUI.navigateSelectedTab(5);
                return;
            }
            if (name == PAGE_UP) {
                int tabPlacement = jTabbedPane.getTabPlacement();
                if (tabPlacement == 1 || tabPlacement == 3) {
                    basicTabbedPaneUI.navigateSelectedTab(7);
                    return;
                } else {
                    basicTabbedPaneUI.navigateSelectedTab(1);
                    return;
                }
            }
            if (name == PAGE_DOWN) {
                int tabPlacement2 = jTabbedPane.getTabPlacement();
                if (tabPlacement2 == 1 || tabPlacement2 == 3) {
                    basicTabbedPaneUI.navigateSelectedTab(3);
                    return;
                } else {
                    basicTabbedPaneUI.navigateSelectedTab(5);
                    return;
                }
            }
            if (name == REQUEST_FOCUS) {
                jTabbedPane.requestFocus();
                return;
            }
            if (name == REQUEST_FOCUS_FOR_VISIBLE) {
                basicTabbedPaneUI.requestFocusForVisibleComponent();
                return;
            }
            if (name == SET_SELECTED) {
                String actionCommand = actionEvent.getActionCommand();
                if (actionCommand != null && actionCommand.length() > 0) {
                    int iCharAt = actionEvent.getActionCommand().charAt(0);
                    if (iCharAt >= 97 && iCharAt <= 122) {
                        iCharAt -= 32;
                    }
                    Integer num = (Integer) basicTabbedPaneUI.mnemonicToIndexMap.get(Integer.valueOf(iCharAt));
                    if (num != null && jTabbedPane.isEnabledAt(num.intValue())) {
                        jTabbedPane.setSelectedIndex(num.intValue());
                        return;
                    }
                    return;
                }
                return;
            }
            if (name == SELECT_FOCUSED) {
                int focusIndex = basicTabbedPaneUI.getFocusIndex();
                if (focusIndex != -1) {
                    jTabbedPane.setSelectedIndex(focusIndex);
                    return;
                }
                return;
            }
            if (name == SCROLL_FORWARD) {
                if (basicTabbedPaneUI.scrollableTabLayoutEnabled()) {
                    basicTabbedPaneUI.tabScroller.scrollForward(jTabbedPane.getTabPlacement());
                }
            } else if (name == SCROLL_BACKWARD && basicTabbedPaneUI.scrollableTabLayoutEnabled()) {
                basicTabbedPaneUI.tabScroller.scrollBackward(jTabbedPane.getTabPlacement());
            }
        }
    }

    /* loaded from: rt.jar:javax/swing/plaf/basic/BasicTabbedPaneUI$TabbedPaneLayout.class */
    public class TabbedPaneLayout implements LayoutManager {
        public TabbedPaneLayout() {
        }

        @Override // java.awt.LayoutManager
        public void addLayoutComponent(String str, Component component) {
        }

        @Override // java.awt.LayoutManager
        public void removeLayoutComponent(Component component) {
        }

        @Override // java.awt.LayoutManager
        public Dimension preferredLayoutSize(Container container) {
            return calculateSize(false);
        }

        @Override // java.awt.LayoutManager
        public Dimension minimumLayoutSize(Container container) {
            return calculateSize(true);
        }

        protected Dimension calculateSize(boolean z2) {
            int iMax;
            int iPreferredTabAreaHeight;
            int tabPlacement = BasicTabbedPaneUI.this.tabPane.getTabPlacement();
            Insets insets = BasicTabbedPaneUI.this.tabPane.getInsets();
            Insets contentBorderInsets = BasicTabbedPaneUI.this.getContentBorderInsets(tabPlacement);
            Insets tabAreaInsets = BasicTabbedPaneUI.this.getTabAreaInsets(tabPlacement);
            new Dimension(0, 0);
            int iMax2 = 0;
            int iMax3 = 0;
            for (int i2 = 0; i2 < BasicTabbedPaneUI.this.tabPane.getTabCount(); i2++) {
                Component componentAt = BasicTabbedPaneUI.this.tabPane.getComponentAt(i2);
                if (componentAt != null) {
                    Dimension minimumSize = z2 ? componentAt.getMinimumSize() : componentAt.getPreferredSize();
                    if (minimumSize != null) {
                        iMax3 = Math.max(minimumSize.height, iMax3);
                        iMax2 = Math.max(minimumSize.width, iMax2);
                    }
                }
            }
            int i3 = 0 + iMax2;
            int i4 = 0 + iMax3;
            switch (tabPlacement) {
                case 1:
                case 3:
                default:
                    iMax = Math.max(i3, BasicTabbedPaneUI.this.calculateMaxTabWidth(tabPlacement));
                    iPreferredTabAreaHeight = i4 + preferredTabAreaHeight(tabPlacement, (iMax - tabAreaInsets.left) - tabAreaInsets.right);
                    break;
                case 2:
                case 4:
                    iPreferredTabAreaHeight = Math.max(i4, BasicTabbedPaneUI.this.calculateMaxTabHeight(tabPlacement));
                    iMax = i3 + preferredTabAreaWidth(tabPlacement, (iPreferredTabAreaHeight - tabAreaInsets.top) - tabAreaInsets.bottom);
                    break;
            }
            return new Dimension(iMax + insets.left + insets.right + contentBorderInsets.left + contentBorderInsets.right, iPreferredTabAreaHeight + insets.bottom + insets.top + contentBorderInsets.top + contentBorderInsets.bottom);
        }

        protected int preferredTabAreaHeight(int i2, int i3) {
            FontMetrics fontMetrics = BasicTabbedPaneUI.this.getFontMetrics();
            int tabCount = BasicTabbedPaneUI.this.tabPane.getTabCount();
            int iCalculateTabAreaHeight = 0;
            if (tabCount > 0) {
                int i4 = 1;
                int i5 = 0;
                int iCalculateMaxTabHeight = BasicTabbedPaneUI.this.calculateMaxTabHeight(i2);
                for (int i6 = 0; i6 < tabCount; i6++) {
                    int iCalculateTabWidth = BasicTabbedPaneUI.this.calculateTabWidth(i2, i6, fontMetrics);
                    if (i5 != 0 && i5 + iCalculateTabWidth > i3) {
                        i4++;
                        i5 = 0;
                    }
                    i5 += iCalculateTabWidth;
                }
                iCalculateTabAreaHeight = BasicTabbedPaneUI.this.calculateTabAreaHeight(i2, i4, iCalculateMaxTabHeight);
            }
            return iCalculateTabAreaHeight;
        }

        protected int preferredTabAreaWidth(int i2, int i3) {
            FontMetrics fontMetrics = BasicTabbedPaneUI.this.getFontMetrics();
            int tabCount = BasicTabbedPaneUI.this.tabPane.getTabCount();
            int iCalculateTabAreaWidth = 0;
            if (tabCount > 0) {
                int i4 = 1;
                int i5 = 0;
                int height = fontMetrics.getHeight();
                BasicTabbedPaneUI.this.maxTabWidth = BasicTabbedPaneUI.this.calculateMaxTabWidth(i2);
                for (int i6 = 0; i6 < tabCount; i6++) {
                    int iCalculateTabHeight = BasicTabbedPaneUI.this.calculateTabHeight(i2, i6, height);
                    if (i5 != 0 && i5 + iCalculateTabHeight > i3) {
                        i4++;
                        i5 = 0;
                    }
                    i5 += iCalculateTabHeight;
                }
                iCalculateTabAreaWidth = BasicTabbedPaneUI.this.calculateTabAreaWidth(i2, i4, BasicTabbedPaneUI.this.maxTabWidth);
            }
            return iCalculateTabAreaWidth;
        }

        @Override // java.awt.LayoutManager
        public void layoutContainer(Container container) {
            int i2;
            int i3;
            BasicTabbedPaneUI.this.setRolloverTab(-1);
            int tabPlacement = BasicTabbedPaneUI.this.tabPane.getTabPlacement();
            Insets insets = BasicTabbedPaneUI.this.tabPane.getInsets();
            int selectedIndex = BasicTabbedPaneUI.this.tabPane.getSelectedIndex();
            Component visibleComponent = BasicTabbedPaneUI.this.getVisibleComponent();
            calculateLayoutInfo();
            Component componentAt = null;
            if (selectedIndex < 0) {
                if (visibleComponent != null) {
                    BasicTabbedPaneUI.this.setVisibleComponent(null);
                }
            } else {
                componentAt = BasicTabbedPaneUI.this.tabPane.getComponentAt(selectedIndex);
            }
            int iCalculateTabAreaWidth = 0;
            int iCalculateTabAreaHeight = 0;
            Insets contentBorderInsets = BasicTabbedPaneUI.this.getContentBorderInsets(tabPlacement);
            boolean z2 = false;
            if (componentAt != null) {
                if (componentAt != visibleComponent && visibleComponent != null && SwingUtilities.findFocusOwner(visibleComponent) != null) {
                    z2 = true;
                }
                BasicTabbedPaneUI.this.setVisibleComponent(componentAt);
            }
            Rectangle bounds = BasicTabbedPaneUI.this.tabPane.getBounds();
            int componentCount = BasicTabbedPaneUI.this.tabPane.getComponentCount();
            if (componentCount > 0) {
                switch (tabPlacement) {
                    case 1:
                    default:
                        iCalculateTabAreaHeight = BasicTabbedPaneUI.this.calculateTabAreaHeight(tabPlacement, BasicTabbedPaneUI.this.runCount, BasicTabbedPaneUI.this.maxTabHeight);
                        i2 = insets.left + contentBorderInsets.left;
                        i3 = insets.top + iCalculateTabAreaHeight + contentBorderInsets.top;
                        break;
                    case 2:
                        iCalculateTabAreaWidth = BasicTabbedPaneUI.this.calculateTabAreaWidth(tabPlacement, BasicTabbedPaneUI.this.runCount, BasicTabbedPaneUI.this.maxTabWidth);
                        i2 = insets.left + iCalculateTabAreaWidth + contentBorderInsets.left;
                        i3 = insets.top + contentBorderInsets.top;
                        break;
                    case 3:
                        iCalculateTabAreaHeight = BasicTabbedPaneUI.this.calculateTabAreaHeight(tabPlacement, BasicTabbedPaneUI.this.runCount, BasicTabbedPaneUI.this.maxTabHeight);
                        i2 = insets.left + contentBorderInsets.left;
                        i3 = insets.top + contentBorderInsets.top;
                        break;
                    case 4:
                        iCalculateTabAreaWidth = BasicTabbedPaneUI.this.calculateTabAreaWidth(tabPlacement, BasicTabbedPaneUI.this.runCount, BasicTabbedPaneUI.this.maxTabWidth);
                        i2 = insets.left + contentBorderInsets.left;
                        i3 = insets.top + contentBorderInsets.top;
                        break;
                }
                int i4 = ((((bounds.width - iCalculateTabAreaWidth) - insets.left) - insets.right) - contentBorderInsets.left) - contentBorderInsets.right;
                int i5 = ((((bounds.height - iCalculateTabAreaHeight) - insets.top) - insets.bottom) - contentBorderInsets.top) - contentBorderInsets.bottom;
                for (int i6 = 0; i6 < componentCount; i6++) {
                    Component component = BasicTabbedPaneUI.this.tabPane.getComponent(i6);
                    if (component == BasicTabbedPaneUI.this.tabContainer) {
                        int i7 = iCalculateTabAreaWidth == 0 ? bounds.width : iCalculateTabAreaWidth + insets.left + insets.right + contentBorderInsets.left + contentBorderInsets.right;
                        int i8 = iCalculateTabAreaHeight == 0 ? bounds.height : iCalculateTabAreaHeight + insets.top + insets.bottom + contentBorderInsets.top + contentBorderInsets.bottom;
                        int i9 = 0;
                        int i10 = 0;
                        if (tabPlacement == 3) {
                            i10 = bounds.height - i8;
                        } else if (tabPlacement == 4) {
                            i9 = bounds.width - i7;
                        }
                        component.setBounds(i9, i10, i7, i8);
                    } else {
                        component.setBounds(i2, i3, i4, i5);
                    }
                }
            }
            layoutTabComponents();
            if (z2 && !BasicTabbedPaneUI.this.requestFocusForVisibleComponent()) {
                BasicTabbedPaneUI.this.tabPane.requestFocus();
            }
        }

        public void calculateLayoutInfo() {
            int tabCount = BasicTabbedPaneUI.this.tabPane.getTabCount();
            BasicTabbedPaneUI.this.assureRectsCreated(tabCount);
            calculateTabRects(BasicTabbedPaneUI.this.tabPane.getTabPlacement(), tabCount);
            BasicTabbedPaneUI.this.isRunsDirty = false;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void layoutTabComponents() {
            if (BasicTabbedPaneUI.this.tabContainer == null) {
                return;
            }
            Rectangle rectangle = new Rectangle();
            Point point = new Point(-BasicTabbedPaneUI.this.tabContainer.getX(), -BasicTabbedPaneUI.this.tabContainer.getY());
            if (BasicTabbedPaneUI.this.scrollableTabLayoutEnabled()) {
                BasicTabbedPaneUI.this.translatePointToTabPanel(0, 0, point);
            }
            int i2 = 0;
            while (i2 < BasicTabbedPaneUI.this.tabPane.getTabCount()) {
                Component tabComponentAt = BasicTabbedPaneUI.this.tabPane.getTabComponentAt(i2);
                if (tabComponentAt != null) {
                    BasicTabbedPaneUI.this.getTabBounds(i2, rectangle);
                    Dimension preferredSize = tabComponentAt.getPreferredSize();
                    Insets tabInsets = BasicTabbedPaneUI.this.getTabInsets(BasicTabbedPaneUI.this.tabPane.getTabPlacement(), i2);
                    int i3 = rectangle.f12372x + tabInsets.left + point.f12370x;
                    int i4 = rectangle.f12373y + tabInsets.top + point.f12371y;
                    int i5 = (rectangle.width - tabInsets.left) - tabInsets.right;
                    int i6 = (rectangle.height - tabInsets.top) - tabInsets.bottom;
                    int i7 = i3 + ((i5 - preferredSize.width) / 2);
                    int i8 = i4 + ((i6 - preferredSize.height) / 2);
                    int tabPlacement = BasicTabbedPaneUI.this.tabPane.getTabPlacement();
                    boolean z2 = i2 == BasicTabbedPaneUI.this.tabPane.getSelectedIndex();
                    tabComponentAt.setBounds(i7 + BasicTabbedPaneUI.this.getTabLabelShiftX(tabPlacement, i2, z2), i8 + BasicTabbedPaneUI.this.getTabLabelShiftY(tabPlacement, i2, z2), preferredSize.width, preferredSize.height);
                }
                i2++;
            }
        }

        protected void calculateTabRects(int i2, int i3) {
            int i4;
            int i5;
            int i6;
            FontMetrics fontMetrics = BasicTabbedPaneUI.this.getFontMetrics();
            Dimension size = BasicTabbedPaneUI.this.tabPane.getSize();
            Insets insets = BasicTabbedPaneUI.this.tabPane.getInsets();
            Insets tabAreaInsets = BasicTabbedPaneUI.this.getTabAreaInsets(i2);
            int height = fontMetrics.getHeight();
            int selectedIndex = BasicTabbedPaneUI.this.tabPane.getSelectedIndex();
            boolean z2 = i2 == 2 || i2 == 4;
            boolean zIsLeftToRight = BasicGraphicsUtils.isLeftToRight(BasicTabbedPaneUI.this.tabPane);
            switch (i2) {
                case 1:
                default:
                    BasicTabbedPaneUI.this.maxTabHeight = BasicTabbedPaneUI.this.calculateMaxTabHeight(i2);
                    i4 = insets.left + tabAreaInsets.left;
                    i5 = insets.top + tabAreaInsets.top;
                    i6 = size.width - (insets.right + tabAreaInsets.right);
                    break;
                case 2:
                    BasicTabbedPaneUI.this.maxTabWidth = BasicTabbedPaneUI.this.calculateMaxTabWidth(i2);
                    i4 = insets.left + tabAreaInsets.left;
                    i5 = insets.top + tabAreaInsets.top;
                    i6 = size.height - (insets.bottom + tabAreaInsets.bottom);
                    break;
                case 3:
                    BasicTabbedPaneUI.this.maxTabHeight = BasicTabbedPaneUI.this.calculateMaxTabHeight(i2);
                    i4 = insets.left + tabAreaInsets.left;
                    i5 = ((size.height - insets.bottom) - tabAreaInsets.bottom) - BasicTabbedPaneUI.this.maxTabHeight;
                    i6 = size.width - (insets.right + tabAreaInsets.right);
                    break;
                case 4:
                    BasicTabbedPaneUI.this.maxTabWidth = BasicTabbedPaneUI.this.calculateMaxTabWidth(i2);
                    i4 = ((size.width - insets.right) - tabAreaInsets.right) - BasicTabbedPaneUI.this.maxTabWidth;
                    i5 = insets.top + tabAreaInsets.top;
                    i6 = size.height - (insets.bottom + tabAreaInsets.bottom);
                    break;
            }
            int tabRunOverlay = BasicTabbedPaneUI.this.getTabRunOverlay(i2);
            BasicTabbedPaneUI.this.runCount = 0;
            BasicTabbedPaneUI.this.selectedRun = -1;
            if (i3 == 0) {
                return;
            }
            for (int i7 = 0; i7 < i3; i7++) {
                Rectangle rectangle = BasicTabbedPaneUI.this.rects[i7];
                if (!z2) {
                    if (i7 > 0) {
                        rectangle.f12372x = BasicTabbedPaneUI.this.rects[i7 - 1].f12372x + BasicTabbedPaneUI.this.rects[i7 - 1].width;
                    } else {
                        BasicTabbedPaneUI.this.tabRuns[0] = 0;
                        BasicTabbedPaneUI.this.runCount = 1;
                        BasicTabbedPaneUI.this.maxTabWidth = 0;
                        rectangle.f12372x = i4;
                    }
                    rectangle.width = BasicTabbedPaneUI.this.calculateTabWidth(i2, i7, fontMetrics);
                    BasicTabbedPaneUI.this.maxTabWidth = Math.max(BasicTabbedPaneUI.this.maxTabWidth, rectangle.width);
                    if (rectangle.f12372x != i4 && rectangle.f12372x + rectangle.width > i6) {
                        if (BasicTabbedPaneUI.this.runCount > BasicTabbedPaneUI.this.tabRuns.length - 1) {
                            BasicTabbedPaneUI.this.expandTabRunsArray();
                        }
                        BasicTabbedPaneUI.this.tabRuns[BasicTabbedPaneUI.this.runCount] = i7;
                        BasicTabbedPaneUI.this.runCount++;
                        rectangle.f12372x = i4;
                    }
                    rectangle.f12373y = i5;
                    rectangle.height = BasicTabbedPaneUI.this.maxTabHeight;
                } else {
                    if (i7 > 0) {
                        rectangle.f12373y = BasicTabbedPaneUI.this.rects[i7 - 1].f12373y + BasicTabbedPaneUI.this.rects[i7 - 1].height;
                    } else {
                        BasicTabbedPaneUI.this.tabRuns[0] = 0;
                        BasicTabbedPaneUI.this.runCount = 1;
                        BasicTabbedPaneUI.this.maxTabHeight = 0;
                        rectangle.f12373y = i5;
                    }
                    rectangle.height = BasicTabbedPaneUI.this.calculateTabHeight(i2, i7, height);
                    BasicTabbedPaneUI.this.maxTabHeight = Math.max(BasicTabbedPaneUI.this.maxTabHeight, rectangle.height);
                    if (rectangle.f12373y != i5 && rectangle.f12373y + rectangle.height > i6) {
                        if (BasicTabbedPaneUI.this.runCount > BasicTabbedPaneUI.this.tabRuns.length - 1) {
                            BasicTabbedPaneUI.this.expandTabRunsArray();
                        }
                        BasicTabbedPaneUI.this.tabRuns[BasicTabbedPaneUI.this.runCount] = i7;
                        BasicTabbedPaneUI.this.runCount++;
                        rectangle.f12373y = i5;
                    }
                    rectangle.f12372x = i4;
                    rectangle.width = BasicTabbedPaneUI.this.maxTabWidth;
                }
                if (i7 == selectedIndex) {
                    BasicTabbedPaneUI.this.selectedRun = BasicTabbedPaneUI.this.runCount - 1;
                }
            }
            if (BasicTabbedPaneUI.this.runCount > 1) {
                normalizeTabRuns(i2, i3, z2 ? i5 : i4, i6);
                BasicTabbedPaneUI.this.selectedRun = BasicTabbedPaneUI.this.getRunForTab(i3, selectedIndex);
                if (BasicTabbedPaneUI.this.shouldRotateTabRuns(i2)) {
                    rotateTabRuns(i2, BasicTabbedPaneUI.this.selectedRun);
                }
            }
            int i8 = BasicTabbedPaneUI.this.runCount - 1;
            while (i8 >= 0) {
                int i9 = BasicTabbedPaneUI.this.tabRuns[i8];
                int i10 = BasicTabbedPaneUI.this.tabRuns[i8 == BasicTabbedPaneUI.this.runCount - 1 ? 0 : i8 + 1];
                int i11 = i10 != 0 ? i10 - 1 : i3 - 1;
                if (!z2) {
                    for (int i12 = i9; i12 <= i11; i12++) {
                        Rectangle rectangle2 = BasicTabbedPaneUI.this.rects[i12];
                        rectangle2.f12373y = i5;
                        rectangle2.f12372x += BasicTabbedPaneUI.this.getTabRunIndent(i2, i8);
                    }
                    if (BasicTabbedPaneUI.this.shouldPadTabRun(i2, i8)) {
                        padTabRun(i2, i9, i11, i6);
                    }
                    if (i2 == 3) {
                        i5 -= BasicTabbedPaneUI.this.maxTabHeight - tabRunOverlay;
                    } else {
                        i5 += BasicTabbedPaneUI.this.maxTabHeight - tabRunOverlay;
                    }
                } else {
                    for (int i13 = i9; i13 <= i11; i13++) {
                        Rectangle rectangle3 = BasicTabbedPaneUI.this.rects[i13];
                        rectangle3.f12372x = i4;
                        rectangle3.f12373y += BasicTabbedPaneUI.this.getTabRunIndent(i2, i8);
                    }
                    if (BasicTabbedPaneUI.this.shouldPadTabRun(i2, i8)) {
                        padTabRun(i2, i9, i11, i6);
                    }
                    if (i2 == 4) {
                        i4 -= BasicTabbedPaneUI.this.maxTabWidth - tabRunOverlay;
                    } else {
                        i4 += BasicTabbedPaneUI.this.maxTabWidth - tabRunOverlay;
                    }
                }
                i8--;
            }
            padSelectedTab(i2, selectedIndex);
            if (!zIsLeftToRight && !z2) {
                int i14 = size.width - (insets.right + tabAreaInsets.right);
                for (int i15 = 0; i15 < i3; i15++) {
                    BasicTabbedPaneUI.this.rects[i15].f12372x = (i14 - BasicTabbedPaneUI.this.rects[i15].f12372x) - BasicTabbedPaneUI.this.rects[i15].width;
                }
            }
        }

        protected void rotateTabRuns(int i2, int i3) {
            for (int i4 = 0; i4 < i3; i4++) {
                int i5 = BasicTabbedPaneUI.this.tabRuns[0];
                for (int i6 = 1; i6 < BasicTabbedPaneUI.this.runCount; i6++) {
                    BasicTabbedPaneUI.this.tabRuns[i6 - 1] = BasicTabbedPaneUI.this.tabRuns[i6];
                }
                BasicTabbedPaneUI.this.tabRuns[BasicTabbedPaneUI.this.runCount - 1] = i5;
            }
        }

        protected void normalizeTabRuns(int i2, int i3, int i4, int i5) {
            int i6;
            int i7;
            boolean z2 = i2 == 2 || i2 == 4;
            int i8 = BasicTabbedPaneUI.this.runCount - 1;
            boolean z3 = true;
            double d2 = 1.25d;
            while (z3) {
                int iLastTabInRun = BasicTabbedPaneUI.this.lastTabInRun(i3, i8);
                int iLastTabInRun2 = BasicTabbedPaneUI.this.lastTabInRun(i3, i8 - 1);
                if (!z2) {
                    i6 = BasicTabbedPaneUI.this.rects[iLastTabInRun].f12372x + BasicTabbedPaneUI.this.rects[iLastTabInRun].width;
                    i7 = (int) (BasicTabbedPaneUI.this.maxTabWidth * d2);
                } else {
                    i6 = BasicTabbedPaneUI.this.rects[iLastTabInRun].f12373y + BasicTabbedPaneUI.this.rects[iLastTabInRun].height;
                    i7 = (int) (BasicTabbedPaneUI.this.maxTabHeight * d2 * 2.0d);
                }
                if (i5 - i6 > i7) {
                    BasicTabbedPaneUI.this.tabRuns[i8] = iLastTabInRun2;
                    if (!z2) {
                        BasicTabbedPaneUI.this.rects[iLastTabInRun2].f12372x = i4;
                    } else {
                        BasicTabbedPaneUI.this.rects[iLastTabInRun2].f12373y = i4;
                    }
                    for (int i9 = iLastTabInRun2 + 1; i9 <= iLastTabInRun; i9++) {
                        if (!z2) {
                            BasicTabbedPaneUI.this.rects[i9].f12372x = BasicTabbedPaneUI.this.rects[i9 - 1].f12372x + BasicTabbedPaneUI.this.rects[i9 - 1].width;
                        } else {
                            BasicTabbedPaneUI.this.rects[i9].f12373y = BasicTabbedPaneUI.this.rects[i9 - 1].f12373y + BasicTabbedPaneUI.this.rects[i9 - 1].height;
                        }
                    }
                } else if (i8 == BasicTabbedPaneUI.this.runCount - 1) {
                    z3 = false;
                }
                if (i8 - 1 > 0) {
                    i8--;
                } else {
                    i8 = BasicTabbedPaneUI.this.runCount - 1;
                    d2 += 0.25d;
                }
            }
        }

        protected void padTabRun(int i2, int i3, int i4, int i5) {
            Rectangle rectangle = BasicTabbedPaneUI.this.rects[i4];
            if (i2 == 1 || i2 == 3) {
                float f2 = (i5 - (rectangle.f12372x + rectangle.width)) / ((rectangle.f12372x + rectangle.width) - BasicTabbedPaneUI.this.rects[i3].f12372x);
                for (int i6 = i3; i6 <= i4; i6++) {
                    Rectangle rectangle2 = BasicTabbedPaneUI.this.rects[i6];
                    if (i6 > i3) {
                        rectangle2.f12372x = BasicTabbedPaneUI.this.rects[i6 - 1].f12372x + BasicTabbedPaneUI.this.rects[i6 - 1].width;
                    }
                    rectangle2.width += Math.round(rectangle2.width * f2);
                }
                rectangle.width = i5 - rectangle.f12372x;
                return;
            }
            float f3 = (i5 - (rectangle.f12373y + rectangle.height)) / ((rectangle.f12373y + rectangle.height) - BasicTabbedPaneUI.this.rects[i3].f12373y);
            for (int i7 = i3; i7 <= i4; i7++) {
                Rectangle rectangle3 = BasicTabbedPaneUI.this.rects[i7];
                if (i7 > i3) {
                    rectangle3.f12373y = BasicTabbedPaneUI.this.rects[i7 - 1].f12373y + BasicTabbedPaneUI.this.rects[i7 - 1].height;
                }
                rectangle3.height += Math.round(rectangle3.height * f3);
            }
            rectangle.height = i5 - rectangle.f12373y;
        }

        protected void padSelectedTab(int i2, int i3) {
            if (i3 >= 0) {
                Rectangle rectangle = BasicTabbedPaneUI.this.rects[i3];
                Insets selectedTabPadInsets = BasicTabbedPaneUI.this.getSelectedTabPadInsets(i2);
                rectangle.f12372x -= selectedTabPadInsets.left;
                rectangle.width += selectedTabPadInsets.left + selectedTabPadInsets.right;
                rectangle.f12373y -= selectedTabPadInsets.top;
                rectangle.height += selectedTabPadInsets.top + selectedTabPadInsets.bottom;
                if (!BasicTabbedPaneUI.this.scrollableTabLayoutEnabled()) {
                    Dimension size = BasicTabbedPaneUI.this.tabPane.getSize();
                    Insets insets = BasicTabbedPaneUI.this.tabPane.getInsets();
                    if (i2 == 2 || i2 == 4) {
                        int i4 = insets.top - rectangle.f12373y;
                        if (i4 > 0) {
                            rectangle.f12373y += i4;
                            rectangle.height -= i4;
                        }
                        int i5 = ((rectangle.f12373y + rectangle.height) + insets.bottom) - size.height;
                        if (i5 > 0) {
                            rectangle.height -= i5;
                            return;
                        }
                        return;
                    }
                    int i6 = insets.left - rectangle.f12372x;
                    if (i6 > 0) {
                        rectangle.f12372x += i6;
                        rectangle.width -= i6;
                    }
                    int i7 = ((rectangle.f12372x + rectangle.width) + insets.right) - size.width;
                    if (i7 > 0) {
                        rectangle.width -= i7;
                    }
                }
            }
        }
    }

    /* loaded from: rt.jar:javax/swing/plaf/basic/BasicTabbedPaneUI$TabbedPaneScrollLayout.class */
    private class TabbedPaneScrollLayout extends TabbedPaneLayout {
        private TabbedPaneScrollLayout() {
            super();
        }

        @Override // javax.swing.plaf.basic.BasicTabbedPaneUI.TabbedPaneLayout
        protected int preferredTabAreaHeight(int i2, int i3) {
            return BasicTabbedPaneUI.this.calculateMaxTabHeight(i2);
        }

        @Override // javax.swing.plaf.basic.BasicTabbedPaneUI.TabbedPaneLayout
        protected int preferredTabAreaWidth(int i2, int i3) {
            return BasicTabbedPaneUI.this.calculateMaxTabWidth(i2);
        }

        @Override // javax.swing.plaf.basic.BasicTabbedPaneUI.TabbedPaneLayout, java.awt.LayoutManager
        public void layoutContainer(Container container) {
            int iCalculateTabAreaWidth;
            int iCalculateTabAreaHeight;
            int i2;
            int i3;
            int i4;
            int i5;
            int i6;
            int i7;
            BasicTabbedPaneUI.this.setRolloverTab(-1);
            int tabPlacement = BasicTabbedPaneUI.this.tabPane.getTabPlacement();
            int tabCount = BasicTabbedPaneUI.this.tabPane.getTabCount();
            Insets insets = BasicTabbedPaneUI.this.tabPane.getInsets();
            int selectedIndex = BasicTabbedPaneUI.this.tabPane.getSelectedIndex();
            Component visibleComponent = BasicTabbedPaneUI.this.getVisibleComponent();
            calculateLayoutInfo();
            Component componentAt = null;
            if (selectedIndex < 0) {
                if (visibleComponent != null) {
                    BasicTabbedPaneUI.this.setVisibleComponent(null);
                }
            } else {
                componentAt = BasicTabbedPaneUI.this.tabPane.getComponentAt(selectedIndex);
            }
            if (BasicTabbedPaneUI.this.tabPane.getTabCount() == 0) {
                BasicTabbedPaneUI.this.tabScroller.croppedEdge.resetParams();
                BasicTabbedPaneUI.this.tabScroller.scrollForwardButton.setVisible(false);
                BasicTabbedPaneUI.this.tabScroller.scrollBackwardButton.setVisible(false);
                return;
            }
            boolean z2 = false;
            if (componentAt != null) {
                if (componentAt != visibleComponent && visibleComponent != null && SwingUtilities.findFocusOwner(visibleComponent) != null) {
                    z2 = true;
                }
                BasicTabbedPaneUI.this.setVisibleComponent(componentAt);
            }
            Insets contentBorderInsets = BasicTabbedPaneUI.this.getContentBorderInsets(tabPlacement);
            Rectangle bounds = BasicTabbedPaneUI.this.tabPane.getBounds();
            int componentCount = BasicTabbedPaneUI.this.tabPane.getComponentCount();
            if (componentCount > 0) {
                switch (tabPlacement) {
                    case 1:
                    default:
                        iCalculateTabAreaWidth = (bounds.width - insets.left) - insets.right;
                        iCalculateTabAreaHeight = BasicTabbedPaneUI.this.calculateTabAreaHeight(tabPlacement, BasicTabbedPaneUI.this.runCount, BasicTabbedPaneUI.this.maxTabHeight);
                        i2 = insets.left;
                        i3 = insets.top;
                        i4 = i2 + contentBorderInsets.left;
                        i5 = i3 + iCalculateTabAreaHeight + contentBorderInsets.top;
                        i6 = (((bounds.width - insets.left) - insets.right) - contentBorderInsets.left) - contentBorderInsets.right;
                        i7 = ((((bounds.height - insets.top) - insets.bottom) - iCalculateTabAreaHeight) - contentBorderInsets.top) - contentBorderInsets.bottom;
                        break;
                    case 2:
                        iCalculateTabAreaWidth = BasicTabbedPaneUI.this.calculateTabAreaWidth(tabPlacement, BasicTabbedPaneUI.this.runCount, BasicTabbedPaneUI.this.maxTabWidth);
                        iCalculateTabAreaHeight = (bounds.height - insets.top) - insets.bottom;
                        i2 = insets.left;
                        i3 = insets.top;
                        i4 = i2 + iCalculateTabAreaWidth + contentBorderInsets.left;
                        i5 = i3 + contentBorderInsets.top;
                        i6 = ((((bounds.width - insets.left) - insets.right) - iCalculateTabAreaWidth) - contentBorderInsets.left) - contentBorderInsets.right;
                        i7 = (((bounds.height - insets.top) - insets.bottom) - contentBorderInsets.top) - contentBorderInsets.bottom;
                        break;
                    case 3:
                        iCalculateTabAreaWidth = (bounds.width - insets.left) - insets.right;
                        iCalculateTabAreaHeight = BasicTabbedPaneUI.this.calculateTabAreaHeight(tabPlacement, BasicTabbedPaneUI.this.runCount, BasicTabbedPaneUI.this.maxTabHeight);
                        i2 = insets.left;
                        i3 = (bounds.height - insets.bottom) - iCalculateTabAreaHeight;
                        i4 = insets.left + contentBorderInsets.left;
                        i5 = insets.top + contentBorderInsets.top;
                        i6 = (((bounds.width - insets.left) - insets.right) - contentBorderInsets.left) - contentBorderInsets.right;
                        i7 = ((((bounds.height - insets.top) - insets.bottom) - iCalculateTabAreaHeight) - contentBorderInsets.top) - contentBorderInsets.bottom;
                        break;
                    case 4:
                        iCalculateTabAreaWidth = BasicTabbedPaneUI.this.calculateTabAreaWidth(tabPlacement, BasicTabbedPaneUI.this.runCount, BasicTabbedPaneUI.this.maxTabWidth);
                        iCalculateTabAreaHeight = (bounds.height - insets.top) - insets.bottom;
                        i2 = (bounds.width - insets.right) - iCalculateTabAreaWidth;
                        i3 = insets.top;
                        i4 = insets.left + contentBorderInsets.left;
                        i5 = insets.top + contentBorderInsets.top;
                        i6 = ((((bounds.width - insets.left) - insets.right) - iCalculateTabAreaWidth) - contentBorderInsets.left) - contentBorderInsets.right;
                        i7 = (((bounds.height - insets.top) - insets.bottom) - contentBorderInsets.top) - contentBorderInsets.bottom;
                        break;
                }
                for (int i8 = 0; i8 < componentCount; i8++) {
                    Component component = BasicTabbedPaneUI.this.tabPane.getComponent(i8);
                    if (BasicTabbedPaneUI.this.tabScroller == null || component != BasicTabbedPaneUI.this.tabScroller.viewport) {
                        if (BasicTabbedPaneUI.this.tabScroller != null && (component == BasicTabbedPaneUI.this.tabScroller.scrollForwardButton || component == BasicTabbedPaneUI.this.tabScroller.scrollBackwardButton)) {
                            Dimension preferredSize = component.getPreferredSize();
                            int i9 = 0;
                            int i10 = 0;
                            int i11 = preferredSize.width;
                            int i12 = preferredSize.height;
                            boolean z3 = false;
                            switch (tabPlacement) {
                                case 1:
                                case 3:
                                default:
                                    if (BasicTabbedPaneUI.this.rects[tabCount - 1].f12372x + BasicTabbedPaneUI.this.rects[tabCount - 1].width > iCalculateTabAreaWidth) {
                                        z3 = true;
                                        i9 = component == BasicTabbedPaneUI.this.tabScroller.scrollForwardButton ? (bounds.width - insets.left) - preferredSize.width : (bounds.width - insets.left) - (2 * preferredSize.width);
                                        i10 = tabPlacement == 1 ? (i3 + iCalculateTabAreaHeight) - preferredSize.height : i3;
                                        break;
                                    }
                                    break;
                                case 2:
                                case 4:
                                    if (BasicTabbedPaneUI.this.rects[tabCount - 1].f12373y + BasicTabbedPaneUI.this.rects[tabCount - 1].height > iCalculateTabAreaHeight) {
                                        z3 = true;
                                        i9 = tabPlacement == 2 ? (i2 + iCalculateTabAreaWidth) - preferredSize.width : i2;
                                        i10 = component == BasicTabbedPaneUI.this.tabScroller.scrollForwardButton ? (bounds.height - insets.bottom) - preferredSize.height : (bounds.height - insets.bottom) - (2 * preferredSize.height);
                                        break;
                                    }
                                    break;
                            }
                            component.setVisible(z3);
                            if (z3) {
                                component.setBounds(i9, i10, i11, i12);
                            }
                        } else {
                            component.setBounds(i4, i5, i6, i7);
                        }
                    } else {
                        Rectangle viewRect = ((JViewport) component).getViewRect();
                        int i13 = iCalculateTabAreaWidth;
                        int i14 = iCalculateTabAreaHeight;
                        Dimension preferredSize2 = BasicTabbedPaneUI.this.tabScroller.scrollForwardButton.getPreferredSize();
                        switch (tabPlacement) {
                            case 1:
                            case 3:
                            default:
                                int i15 = BasicTabbedPaneUI.this.rects[tabCount - 1].f12372x + BasicTabbedPaneUI.this.rects[tabCount - 1].width;
                                if (i15 > iCalculateTabAreaWidth) {
                                    i13 = iCalculateTabAreaWidth > 2 * preferredSize2.width ? iCalculateTabAreaWidth - (2 * preferredSize2.width) : 0;
                                    if (i15 - viewRect.f12372x <= i13) {
                                        i13 = i15 - viewRect.f12372x;
                                        break;
                                    }
                                }
                                break;
                            case 2:
                            case 4:
                                int i16 = BasicTabbedPaneUI.this.rects[tabCount - 1].f12373y + BasicTabbedPaneUI.this.rects[tabCount - 1].height;
                                if (i16 > iCalculateTabAreaHeight) {
                                    i14 = iCalculateTabAreaHeight > 2 * preferredSize2.height ? iCalculateTabAreaHeight - (2 * preferredSize2.height) : 0;
                                    if (i16 - viewRect.f12373y <= i14) {
                                        i14 = i16 - viewRect.f12373y;
                                        break;
                                    }
                                }
                                break;
                        }
                        component.setBounds(i2, i3, i13, i14);
                    }
                }
                layoutTabComponents();
                layoutCroppedEdge();
                if (z2 && !BasicTabbedPaneUI.this.requestFocusForVisibleComponent()) {
                    BasicTabbedPaneUI.this.tabPane.requestFocus();
                }
            }
        }

        private void layoutCroppedEdge() {
            BasicTabbedPaneUI.this.tabScroller.croppedEdge.resetParams();
            Rectangle viewRect = BasicTabbedPaneUI.this.tabScroller.viewport.getViewRect();
            for (int i2 = 0; i2 < BasicTabbedPaneUI.this.rects.length; i2++) {
                Rectangle rectangle = BasicTabbedPaneUI.this.rects[i2];
                switch (BasicTabbedPaneUI.this.tabPane.getTabPlacement()) {
                    case 1:
                    case 3:
                    default:
                        int i3 = viewRect.f12372x + viewRect.width;
                        if (rectangle.f12372x >= i3 - 1 || rectangle.f12372x + rectangle.width <= i3) {
                            break;
                        } else {
                            BasicTabbedPaneUI.this.tabScroller.croppedEdge.setParams(i2, (i3 - rectangle.f12372x) - 1, 0, -BasicTabbedPaneUI.this.currentTabAreaInsets.top);
                            break;
                        }
                    case 2:
                    case 4:
                        int i4 = viewRect.f12373y + viewRect.height;
                        if (rectangle.f12373y >= i4 || rectangle.f12373y + rectangle.height <= i4) {
                            break;
                        } else {
                            BasicTabbedPaneUI.this.tabScroller.croppedEdge.setParams(i2, (i4 - rectangle.f12373y) - 1, -BasicTabbedPaneUI.this.currentTabAreaInsets.left, 0);
                            break;
                        }
                }
            }
        }

        @Override // javax.swing.plaf.basic.BasicTabbedPaneUI.TabbedPaneLayout
        protected void calculateTabRects(int i2, int i3) {
            FontMetrics fontMetrics = BasicTabbedPaneUI.this.getFontMetrics();
            Dimension size = BasicTabbedPaneUI.this.tabPane.getSize();
            Insets insets = BasicTabbedPaneUI.this.tabPane.getInsets();
            Insets tabAreaInsets = BasicTabbedPaneUI.this.getTabAreaInsets(i2);
            int height = fontMetrics.getHeight();
            int selectedIndex = BasicTabbedPaneUI.this.tabPane.getSelectedIndex();
            boolean z2 = i2 == 2 || i2 == 4;
            boolean zIsLeftToRight = BasicGraphicsUtils.isLeftToRight(BasicTabbedPaneUI.this.tabPane);
            int i4 = tabAreaInsets.left;
            int i5 = tabAreaInsets.top;
            int i6 = 0;
            int i7 = 0;
            switch (i2) {
                case 1:
                case 3:
                default:
                    BasicTabbedPaneUI.this.maxTabHeight = BasicTabbedPaneUI.this.calculateMaxTabHeight(i2);
                    break;
                case 2:
                case 4:
                    BasicTabbedPaneUI.this.maxTabWidth = BasicTabbedPaneUI.this.calculateMaxTabWidth(i2);
                    break;
            }
            BasicTabbedPaneUI.this.runCount = 0;
            BasicTabbedPaneUI.this.selectedRun = -1;
            if (i3 == 0) {
                return;
            }
            BasicTabbedPaneUI.this.selectedRun = 0;
            BasicTabbedPaneUI.this.runCount = 1;
            for (int i8 = 0; i8 < i3; i8++) {
                Rectangle rectangle = BasicTabbedPaneUI.this.rects[i8];
                if (!z2) {
                    if (i8 > 0) {
                        rectangle.f12372x = BasicTabbedPaneUI.this.rects[i8 - 1].f12372x + BasicTabbedPaneUI.this.rects[i8 - 1].width;
                    } else {
                        BasicTabbedPaneUI.this.tabRuns[0] = 0;
                        BasicTabbedPaneUI.this.maxTabWidth = 0;
                        i7 += BasicTabbedPaneUI.this.maxTabHeight;
                        rectangle.f12372x = i4;
                    }
                    rectangle.width = BasicTabbedPaneUI.this.calculateTabWidth(i2, i8, fontMetrics);
                    i6 = rectangle.f12372x + rectangle.width;
                    BasicTabbedPaneUI.this.maxTabWidth = Math.max(BasicTabbedPaneUI.this.maxTabWidth, rectangle.width);
                    rectangle.f12373y = i5;
                    rectangle.height = BasicTabbedPaneUI.this.maxTabHeight;
                } else {
                    if (i8 > 0) {
                        rectangle.f12373y = BasicTabbedPaneUI.this.rects[i8 - 1].f12373y + BasicTabbedPaneUI.this.rects[i8 - 1].height;
                    } else {
                        BasicTabbedPaneUI.this.tabRuns[0] = 0;
                        BasicTabbedPaneUI.this.maxTabHeight = 0;
                        i6 = BasicTabbedPaneUI.this.maxTabWidth;
                        rectangle.f12373y = i5;
                    }
                    rectangle.height = BasicTabbedPaneUI.this.calculateTabHeight(i2, i8, height);
                    i7 = rectangle.f12373y + rectangle.height;
                    BasicTabbedPaneUI.this.maxTabHeight = Math.max(BasicTabbedPaneUI.this.maxTabHeight, rectangle.height);
                    rectangle.f12372x = i4;
                    rectangle.width = BasicTabbedPaneUI.this.maxTabWidth;
                }
            }
            if (BasicTabbedPaneUI.this.tabsOverlapBorder) {
                padSelectedTab(i2, selectedIndex);
            }
            if (!zIsLeftToRight && !z2) {
                int i9 = size.width - (insets.right + tabAreaInsets.right);
                for (int i10 = 0; i10 < i3; i10++) {
                    BasicTabbedPaneUI.this.rects[i10].f12372x = (i9 - BasicTabbedPaneUI.this.rects[i10].f12372x) - BasicTabbedPaneUI.this.rects[i10].width;
                }
            }
            BasicTabbedPaneUI.this.tabScroller.tabPanel.setPreferredSize(new Dimension(i6, i7));
            BasicTabbedPaneUI.this.tabScroller.tabPanel.invalidate();
        }
    }

    /* loaded from: rt.jar:javax/swing/plaf/basic/BasicTabbedPaneUI$ScrollableTabSupport.class */
    private class ScrollableTabSupport implements ActionListener, ChangeListener {
        public ScrollableTabViewport viewport;
        public ScrollableTabPanel tabPanel;
        public JButton scrollForwardButton;
        public JButton scrollBackwardButton;
        public CroppedEdge croppedEdge;
        public int leadingTabIndex;
        private Point tabViewPosition = new Point(0, 0);

        ScrollableTabSupport(int i2) {
            this.viewport = BasicTabbedPaneUI.this.new ScrollableTabViewport();
            this.tabPanel = BasicTabbedPaneUI.this.new ScrollableTabPanel();
            this.viewport.setView(this.tabPanel);
            this.viewport.addChangeListener(this);
            this.croppedEdge = BasicTabbedPaneUI.this.new CroppedEdge();
            createButtons();
        }

        void createButtons() {
            if (this.scrollForwardButton != null) {
                BasicTabbedPaneUI.this.tabPane.remove(this.scrollForwardButton);
                this.scrollForwardButton.removeActionListener(this);
                BasicTabbedPaneUI.this.tabPane.remove(this.scrollBackwardButton);
                this.scrollBackwardButton.removeActionListener(this);
            }
            int tabPlacement = BasicTabbedPaneUI.this.tabPane.getTabPlacement();
            if (tabPlacement == 1 || tabPlacement == 3) {
                this.scrollForwardButton = BasicTabbedPaneUI.this.createScrollButton(3);
                this.scrollBackwardButton = BasicTabbedPaneUI.this.createScrollButton(7);
            } else {
                this.scrollForwardButton = BasicTabbedPaneUI.this.createScrollButton(5);
                this.scrollBackwardButton = BasicTabbedPaneUI.this.createScrollButton(1);
            }
            this.scrollForwardButton.addActionListener(this);
            this.scrollBackwardButton.addActionListener(this);
            BasicTabbedPaneUI.this.tabPane.add(this.scrollForwardButton);
            BasicTabbedPaneUI.this.tabPane.add(this.scrollBackwardButton);
        }

        public void scrollForward(int i2) {
            Dimension viewSize = this.viewport.getViewSize();
            Rectangle viewRect = this.viewport.getViewRect();
            if (i2 == 1 || i2 == 3) {
                if (viewRect.width >= viewSize.width - viewRect.f12372x) {
                    return;
                }
            } else if (viewRect.height >= viewSize.height - viewRect.f12373y) {
                return;
            }
            setLeadingTabIndex(i2, this.leadingTabIndex + 1);
        }

        public void scrollBackward(int i2) {
            if (this.leadingTabIndex == 0) {
                return;
            }
            setLeadingTabIndex(i2, this.leadingTabIndex - 1);
        }

        public void setLeadingTabIndex(int i2, int i3) {
            this.leadingTabIndex = i3;
            Dimension viewSize = this.viewport.getViewSize();
            Rectangle viewRect = this.viewport.getViewRect();
            switch (i2) {
                case 1:
                case 3:
                    this.tabViewPosition.f12370x = this.leadingTabIndex == 0 ? 0 : BasicTabbedPaneUI.this.rects[this.leadingTabIndex].f12372x;
                    if (viewSize.width - this.tabViewPosition.f12370x < viewRect.width) {
                        this.viewport.setExtentSize(new Dimension(viewSize.width - this.tabViewPosition.f12370x, viewRect.height));
                        break;
                    }
                    break;
                case 2:
                case 4:
                    this.tabViewPosition.f12371y = this.leadingTabIndex == 0 ? 0 : BasicTabbedPaneUI.this.rects[this.leadingTabIndex].f12373y;
                    if (viewSize.height - this.tabViewPosition.f12371y < viewRect.height) {
                        this.viewport.setExtentSize(new Dimension(viewRect.width, viewSize.height - this.tabViewPosition.f12371y));
                        break;
                    }
                    break;
            }
            this.viewport.setViewPosition(this.tabViewPosition);
        }

        @Override // javax.swing.event.ChangeListener
        public void stateChanged(ChangeEvent changeEvent) {
            updateView();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void updateView() {
            int tabPlacement = BasicTabbedPaneUI.this.tabPane.getTabPlacement();
            int tabCount = BasicTabbedPaneUI.this.tabPane.getTabCount();
            BasicTabbedPaneUI.this.assureRectsCreated(tabCount);
            Rectangle bounds = this.viewport.getBounds();
            Dimension viewSize = this.viewport.getViewSize();
            Rectangle viewRect = this.viewport.getViewRect();
            this.leadingTabIndex = BasicTabbedPaneUI.this.getClosestTab(viewRect.f12372x, viewRect.f12373y);
            if (this.leadingTabIndex + 1 < tabCount) {
                switch (tabPlacement) {
                    case 1:
                    case 3:
                        if (BasicTabbedPaneUI.this.rects[this.leadingTabIndex].f12372x < viewRect.f12372x) {
                            this.leadingTabIndex++;
                            break;
                        }
                        break;
                    case 2:
                    case 4:
                        if (BasicTabbedPaneUI.this.rects[this.leadingTabIndex].f12373y < viewRect.f12373y) {
                            this.leadingTabIndex++;
                            break;
                        }
                        break;
                }
            }
            Insets contentBorderInsets = BasicTabbedPaneUI.this.getContentBorderInsets(tabPlacement);
            switch (tabPlacement) {
                case 1:
                default:
                    BasicTabbedPaneUI.this.tabPane.repaint(bounds.f12372x, bounds.f12373y + bounds.height, bounds.width, contentBorderInsets.top);
                    this.scrollBackwardButton.setEnabled(viewRect.f12372x > 0 && this.leadingTabIndex > 0);
                    this.scrollForwardButton.setEnabled(this.leadingTabIndex < tabCount - 1 && viewSize.width - viewRect.f12372x > viewRect.width);
                    break;
                case 2:
                    BasicTabbedPaneUI.this.tabPane.repaint(bounds.f12372x + bounds.width, bounds.f12373y, contentBorderInsets.left, bounds.height);
                    this.scrollBackwardButton.setEnabled(viewRect.f12373y > 0 && this.leadingTabIndex > 0);
                    this.scrollForwardButton.setEnabled(this.leadingTabIndex < tabCount - 1 && viewSize.height - viewRect.f12373y > viewRect.height);
                    break;
                case 3:
                    BasicTabbedPaneUI.this.tabPane.repaint(bounds.f12372x, bounds.f12373y - contentBorderInsets.bottom, bounds.width, contentBorderInsets.bottom);
                    this.scrollBackwardButton.setEnabled(viewRect.f12372x > 0 && this.leadingTabIndex > 0);
                    this.scrollForwardButton.setEnabled(this.leadingTabIndex < tabCount - 1 && viewSize.width - viewRect.f12372x > viewRect.width);
                    break;
                case 4:
                    BasicTabbedPaneUI.this.tabPane.repaint(bounds.f12372x - contentBorderInsets.right, bounds.f12373y, contentBorderInsets.right, bounds.height);
                    this.scrollBackwardButton.setEnabled(viewRect.f12373y > 0 && this.leadingTabIndex > 0);
                    this.scrollForwardButton.setEnabled(this.leadingTabIndex < tabCount - 1 && viewSize.height - viewRect.f12373y > viewRect.height);
                    break;
            }
        }

        @Override // java.awt.event.ActionListener
        public void actionPerformed(ActionEvent actionEvent) {
            Object obj;
            ActionMap actionMap = BasicTabbedPaneUI.this.tabPane.getActionMap();
            if (actionMap != null) {
                if (actionEvent.getSource() == this.scrollForwardButton) {
                    obj = "scrollTabsForwardAction";
                } else {
                    obj = "scrollTabsBackwardAction";
                }
                Action action = actionMap.get(obj);
                if (action != null && action.isEnabled()) {
                    action.actionPerformed(new ActionEvent(BasicTabbedPaneUI.this.tabPane, 1001, null, actionEvent.getWhen(), actionEvent.getModifiers()));
                }
            }
        }

        public String toString() {
            return "viewport.viewSize=" + ((Object) this.viewport.getViewSize()) + "\nviewport.viewRectangle=" + ((Object) this.viewport.getViewRect()) + "\nleadingTabIndex=" + this.leadingTabIndex + "\ntabViewPosition=" + ((Object) this.tabViewPosition);
        }
    }

    /* loaded from: rt.jar:javax/swing/plaf/basic/BasicTabbedPaneUI$ScrollableTabViewport.class */
    private class ScrollableTabViewport extends JViewport implements UIResource {
        public ScrollableTabViewport() {
            setName("TabbedPane.scrollableViewport");
            setScrollMode(0);
            setOpaque(BasicTabbedPaneUI.this.tabPane.isOpaque());
            Color color = UIManager.getColor("TabbedPane.tabAreaBackground");
            setBackground(color == null ? BasicTabbedPaneUI.this.tabPane.getBackground() : color);
        }
    }

    /* loaded from: rt.jar:javax/swing/plaf/basic/BasicTabbedPaneUI$ScrollableTabPanel.class */
    private class ScrollableTabPanel extends JPanel implements UIResource {
        public ScrollableTabPanel() {
            super((LayoutManager) null);
            setOpaque(BasicTabbedPaneUI.this.tabPane.isOpaque());
            Color color = UIManager.getColor("TabbedPane.tabAreaBackground");
            setBackground(color == null ? BasicTabbedPaneUI.this.tabPane.getBackground() : color);
        }

        @Override // javax.swing.JComponent
        public void paintComponent(Graphics graphics) {
            super.paintComponent(graphics);
            BasicTabbedPaneUI.this.paintTabArea(graphics, BasicTabbedPaneUI.this.tabPane.getTabPlacement(), BasicTabbedPaneUI.this.tabPane.getSelectedIndex());
            if (BasicTabbedPaneUI.this.tabScroller.croppedEdge.isParamsSet() && BasicTabbedPaneUI.this.tabContainer == null) {
                Rectangle rectangle = BasicTabbedPaneUI.this.rects[BasicTabbedPaneUI.this.tabScroller.croppedEdge.getTabIndex()];
                graphics.translate(rectangle.f12372x, rectangle.f12373y);
                BasicTabbedPaneUI.this.tabScroller.croppedEdge.paintComponent(graphics);
                graphics.translate(-rectangle.f12372x, -rectangle.f12373y);
            }
        }

        @Override // java.awt.Container, java.awt.Component
        public void doLayout() {
            if (getComponentCount() > 0) {
                getComponent(0).setBounds(0, 0, getWidth(), getHeight());
            }
        }
    }

    /* loaded from: rt.jar:javax/swing/plaf/basic/BasicTabbedPaneUI$ScrollableTabButton.class */
    private class ScrollableTabButton extends BasicArrowButton implements UIResource, SwingConstants {
        public ScrollableTabButton(int i2) {
            super(i2, UIManager.getColor("TabbedPane.selected"), UIManager.getColor("TabbedPane.shadow"), UIManager.getColor("TabbedPane.darkShadow"), UIManager.getColor("TabbedPane.highlight"));
        }
    }

    /* loaded from: rt.jar:javax/swing/plaf/basic/BasicTabbedPaneUI$Handler.class */
    private class Handler implements ChangeListener, ContainerListener, FocusListener, MouseListener, MouseMotionListener, PropertyChangeListener {
        private Handler() {
        }

        @Override // java.beans.PropertyChangeListener
        public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
            JTabbedPane jTabbedPane = (JTabbedPane) propertyChangeEvent.getSource();
            String propertyName = propertyChangeEvent.getPropertyName();
            boolean zScrollableTabLayoutEnabled = BasicTabbedPaneUI.this.scrollableTabLayoutEnabled();
            if (propertyName == "mnemonicAt") {
                BasicTabbedPaneUI.this.updateMnemonics();
                jTabbedPane.repaint();
                return;
            }
            if (propertyName == "displayedMnemonicIndexAt") {
                jTabbedPane.repaint();
                return;
            }
            if (propertyName == "indexForTitle") {
                BasicTabbedPaneUI.this.calculatedBaseline = false;
                Integer num = (Integer) propertyChangeEvent.getNewValue();
                if (BasicTabbedPaneUI.this.htmlViews != null) {
                    BasicTabbedPaneUI.this.htmlViews.removeElementAt(num.intValue());
                }
                updateHtmlViews(num.intValue());
                return;
            }
            if (propertyName == "tabLayoutPolicy") {
                BasicTabbedPaneUI.this.uninstallUI(jTabbedPane);
                BasicTabbedPaneUI.this.installUI(jTabbedPane);
                BasicTabbedPaneUI.this.calculatedBaseline = false;
                return;
            }
            if (propertyName == "tabPlacement") {
                if (BasicTabbedPaneUI.this.scrollableTabLayoutEnabled()) {
                    BasicTabbedPaneUI.this.tabScroller.createButtons();
                }
                BasicTabbedPaneUI.this.calculatedBaseline = false;
                return;
            }
            if (propertyName == "opaque" && zScrollableTabLayoutEnabled) {
                boolean zBooleanValue = ((Boolean) propertyChangeEvent.getNewValue()).booleanValue();
                BasicTabbedPaneUI.this.tabScroller.tabPanel.setOpaque(zBooleanValue);
                BasicTabbedPaneUI.this.tabScroller.viewport.setOpaque(zBooleanValue);
                return;
            }
            if (propertyName == "background" && zScrollableTabLayoutEnabled) {
                Color color = (Color) propertyChangeEvent.getNewValue();
                BasicTabbedPaneUI.this.tabScroller.tabPanel.setBackground(color);
                BasicTabbedPaneUI.this.tabScroller.viewport.setBackground(color);
                Color color2 = BasicTabbedPaneUI.this.selectedColor == null ? color : BasicTabbedPaneUI.this.selectedColor;
                BasicTabbedPaneUI.this.tabScroller.scrollForwardButton.setBackground(color2);
                BasicTabbedPaneUI.this.tabScroller.scrollBackwardButton.setBackground(color2);
                return;
            }
            if (propertyName != "indexForTabComponent") {
                if (propertyName == "indexForNullComponent") {
                    BasicTabbedPaneUI.this.isRunsDirty = true;
                    updateHtmlViews(((Integer) propertyChangeEvent.getNewValue()).intValue());
                    return;
                } else {
                    if (propertyName == "font") {
                        BasicTabbedPaneUI.this.calculatedBaseline = false;
                        return;
                    }
                    return;
                }
            }
            if (BasicTabbedPaneUI.this.tabContainer != null) {
                BasicTabbedPaneUI.this.tabContainer.removeUnusedTabComponents();
            }
            Component tabComponentAt = BasicTabbedPaneUI.this.tabPane.getTabComponentAt(((Integer) propertyChangeEvent.getNewValue()).intValue());
            if (tabComponentAt != null) {
                if (BasicTabbedPaneUI.this.tabContainer == null) {
                    BasicTabbedPaneUI.this.installTabContainer();
                } else {
                    BasicTabbedPaneUI.this.tabContainer.add(tabComponentAt);
                }
            }
            BasicTabbedPaneUI.this.tabPane.revalidate();
            BasicTabbedPaneUI.this.tabPane.repaint();
            BasicTabbedPaneUI.this.calculatedBaseline = false;
        }

        private void updateHtmlViews(int i2) {
            String titleAt = BasicTabbedPaneUI.this.tabPane.getTitleAt(i2);
            if (BasicHTML.isHTMLString(titleAt)) {
                if (BasicTabbedPaneUI.this.htmlViews == null) {
                    BasicTabbedPaneUI.this.htmlViews = BasicTabbedPaneUI.this.createHTMLVector();
                } else {
                    BasicTabbedPaneUI.this.htmlViews.insertElementAt(BasicHTML.createHTMLView(BasicTabbedPaneUI.this.tabPane, titleAt), i2);
                }
            } else if (BasicTabbedPaneUI.this.htmlViews != null) {
                BasicTabbedPaneUI.this.htmlViews.insertElementAt(null, i2);
            }
            BasicTabbedPaneUI.this.updateMnemonics();
        }

        @Override // javax.swing.event.ChangeListener
        public void stateChanged(ChangeEvent changeEvent) {
            JTabbedPane jTabbedPane = (JTabbedPane) changeEvent.getSource();
            jTabbedPane.revalidate();
            jTabbedPane.repaint();
            BasicTabbedPaneUI.this.setFocusIndex(jTabbedPane.getSelectedIndex(), false);
            if (BasicTabbedPaneUI.this.scrollableTabLayoutEnabled()) {
                BasicTabbedPaneUI.this.ensureCurrentLayout();
                int selectedIndex = jTabbedPane.getSelectedIndex();
                if (selectedIndex < BasicTabbedPaneUI.this.rects.length && selectedIndex != -1) {
                    BasicTabbedPaneUI.this.tabScroller.tabPanel.scrollRectToVisible((Rectangle) BasicTabbedPaneUI.this.rects[selectedIndex].clone());
                }
            }
        }

        @Override // java.awt.event.MouseListener
        public void mouseClicked(MouseEvent mouseEvent) {
        }

        @Override // java.awt.event.MouseListener
        public void mouseReleased(MouseEvent mouseEvent) {
        }

        @Override // java.awt.event.MouseListener
        public void mouseEntered(MouseEvent mouseEvent) {
            BasicTabbedPaneUI.this.setRolloverTab(mouseEvent.getX(), mouseEvent.getY());
        }

        @Override // java.awt.event.MouseListener
        public void mouseExited(MouseEvent mouseEvent) {
            BasicTabbedPaneUI.this.setRolloverTab(-1);
        }

        @Override // java.awt.event.MouseListener
        public void mousePressed(MouseEvent mouseEvent) {
            int iTabForCoordinate;
            if (BasicTabbedPaneUI.this.tabPane.isEnabled() && (iTabForCoordinate = BasicTabbedPaneUI.this.tabForCoordinate(BasicTabbedPaneUI.this.tabPane, mouseEvent.getX(), mouseEvent.getY())) >= 0 && BasicTabbedPaneUI.this.tabPane.isEnabledAt(iTabForCoordinate)) {
                if (iTabForCoordinate != BasicTabbedPaneUI.this.tabPane.getSelectedIndex()) {
                    BasicTabbedPaneUI.this.tabPane.setSelectedIndex(iTabForCoordinate);
                } else if (BasicTabbedPaneUI.this.tabPane.isRequestFocusEnabled()) {
                    BasicTabbedPaneUI.this.tabPane.requestFocus();
                }
            }
        }

        @Override // java.awt.event.MouseMotionListener
        public void mouseDragged(MouseEvent mouseEvent) {
        }

        @Override // java.awt.event.MouseMotionListener
        public void mouseMoved(MouseEvent mouseEvent) {
            BasicTabbedPaneUI.this.setRolloverTab(mouseEvent.getX(), mouseEvent.getY());
        }

        @Override // java.awt.event.FocusListener
        public void focusGained(FocusEvent focusEvent) {
            BasicTabbedPaneUI.this.setFocusIndex(BasicTabbedPaneUI.this.tabPane.getSelectedIndex(), true);
        }

        @Override // java.awt.event.FocusListener
        public void focusLost(FocusEvent focusEvent) {
            BasicTabbedPaneUI.this.repaintTab(BasicTabbedPaneUI.this.focusIndex);
        }

        @Override // java.awt.event.ContainerListener
        public void componentAdded(ContainerEvent containerEvent) {
            JTabbedPane jTabbedPane = (JTabbedPane) containerEvent.getContainer();
            Component child = containerEvent.getChild();
            if (!(child instanceof UIResource)) {
                BasicTabbedPaneUI.this.isRunsDirty = true;
                updateHtmlViews(jTabbedPane.indexOfComponent(child));
            }
        }

        @Override // java.awt.event.ContainerListener
        public void componentRemoved(ContainerEvent containerEvent) {
            JTabbedPane jTabbedPane = (JTabbedPane) containerEvent.getContainer();
            if (containerEvent.getChild() instanceof UIResource) {
                return;
            }
            Integer num = (Integer) jTabbedPane.getClientProperty("__index_to_remove__");
            if (num != null) {
                int iIntValue = num.intValue();
                if (BasicTabbedPaneUI.this.htmlViews != null && BasicTabbedPaneUI.this.htmlViews.size() > iIntValue) {
                    BasicTabbedPaneUI.this.htmlViews.removeElementAt(iIntValue);
                }
                jTabbedPane.putClientProperty("__index_to_remove__", null);
            }
            BasicTabbedPaneUI.this.isRunsDirty = true;
            BasicTabbedPaneUI.this.updateMnemonics();
            BasicTabbedPaneUI.this.validateFocusIndex();
        }
    }

    /* loaded from: rt.jar:javax/swing/plaf/basic/BasicTabbedPaneUI$PropertyChangeHandler.class */
    public class PropertyChangeHandler implements PropertyChangeListener {
        public PropertyChangeHandler() {
        }

        @Override // java.beans.PropertyChangeListener
        public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
            BasicTabbedPaneUI.this.getHandler().propertyChange(propertyChangeEvent);
        }
    }

    /* loaded from: rt.jar:javax/swing/plaf/basic/BasicTabbedPaneUI$TabSelectionHandler.class */
    public class TabSelectionHandler implements ChangeListener {
        public TabSelectionHandler() {
        }

        @Override // javax.swing.event.ChangeListener
        public void stateChanged(ChangeEvent changeEvent) {
            BasicTabbedPaneUI.this.getHandler().stateChanged(changeEvent);
        }
    }

    /* loaded from: rt.jar:javax/swing/plaf/basic/BasicTabbedPaneUI$MouseHandler.class */
    public class MouseHandler extends MouseAdapter {
        public MouseHandler() {
        }

        @Override // java.awt.event.MouseAdapter, java.awt.event.MouseListener
        public void mousePressed(MouseEvent mouseEvent) {
            BasicTabbedPaneUI.this.getHandler().mousePressed(mouseEvent);
        }
    }

    /* loaded from: rt.jar:javax/swing/plaf/basic/BasicTabbedPaneUI$FocusHandler.class */
    public class FocusHandler extends FocusAdapter {
        public FocusHandler() {
        }

        @Override // java.awt.event.FocusAdapter, java.awt.event.FocusListener
        public void focusGained(FocusEvent focusEvent) {
            BasicTabbedPaneUI.this.getHandler().focusGained(focusEvent);
        }

        @Override // java.awt.event.FocusAdapter, java.awt.event.FocusListener
        public void focusLost(FocusEvent focusEvent) {
            BasicTabbedPaneUI.this.getHandler().focusLost(focusEvent);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Vector<View> createHTMLVector() {
        Vector<View> vector = new Vector<>();
        int tabCount = this.tabPane.getTabCount();
        if (tabCount > 0) {
            for (int i2 = 0; i2 < tabCount; i2++) {
                String titleAt = this.tabPane.getTitleAt(i2);
                if (BasicHTML.isHTMLString(titleAt)) {
                    vector.addElement(BasicHTML.createHTMLView(this.tabPane, titleAt));
                } else {
                    vector.addElement(null);
                }
            }
        }
        return vector;
    }

    /* loaded from: rt.jar:javax/swing/plaf/basic/BasicTabbedPaneUI$TabContainer.class */
    private class TabContainer extends JPanel implements UIResource {
        private boolean notifyTabbedPane;

        public TabContainer() {
            super((LayoutManager) null);
            this.notifyTabbedPane = true;
            setOpaque(false);
        }

        @Override // java.awt.Container
        public void remove(Component component) {
            int iIndexOfTabComponent = BasicTabbedPaneUI.this.tabPane.indexOfTabComponent(component);
            super.remove(component);
            if (this.notifyTabbedPane && iIndexOfTabComponent != -1) {
                BasicTabbedPaneUI.this.tabPane.setTabComponentAt(iIndexOfTabComponent, null);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void removeUnusedTabComponents() {
            for (Component component : getComponents()) {
                if (!(component instanceof UIResource) && BasicTabbedPaneUI.this.tabPane.indexOfTabComponent(component) == -1) {
                    super.remove(component);
                }
            }
        }

        @Override // javax.swing.JComponent
        public boolean isOptimizedDrawingEnabled() {
            return (BasicTabbedPaneUI.this.tabScroller == null || BasicTabbedPaneUI.this.tabScroller.croppedEdge.isParamsSet()) ? false : true;
        }

        @Override // java.awt.Container, java.awt.Component
        public void doLayout() {
            if (BasicTabbedPaneUI.this.scrollableTabLayoutEnabled()) {
                BasicTabbedPaneUI.this.tabScroller.tabPanel.repaint();
                BasicTabbedPaneUI.this.tabScroller.updateView();
            } else {
                BasicTabbedPaneUI.this.tabPane.repaint(getBounds());
            }
        }
    }

    /* loaded from: rt.jar:javax/swing/plaf/basic/BasicTabbedPaneUI$CroppedEdge.class */
    private class CroppedEdge extends JPanel implements UIResource {
        private Shape shape;
        private int tabIndex;
        private int cropline;
        private int cropx;
        private int cropy;

        public CroppedEdge() {
            setOpaque(false);
        }

        public void setParams(int i2, int i3, int i4, int i5) {
            this.tabIndex = i2;
            this.cropline = i3;
            this.cropx = i4;
            this.cropy = i5;
            Rectangle rectangle = BasicTabbedPaneUI.this.rects[i2];
            setBounds(rectangle);
            this.shape = BasicTabbedPaneUI.createCroppedTabShape(BasicTabbedPaneUI.this.tabPane.getTabPlacement(), rectangle, i3);
            if (getParent() == null && BasicTabbedPaneUI.this.tabContainer != null) {
                BasicTabbedPaneUI.this.tabContainer.add(this, 0);
            }
        }

        public void resetParams() {
            this.shape = null;
            if (getParent() == BasicTabbedPaneUI.this.tabContainer && BasicTabbedPaneUI.this.tabContainer != null) {
                BasicTabbedPaneUI.this.tabContainer.remove(this);
            }
        }

        public boolean isParamsSet() {
            return this.shape != null;
        }

        public int getTabIndex() {
            return this.tabIndex;
        }

        public int getCropline() {
            return this.cropline;
        }

        public int getCroppedSideWidth() {
            return 3;
        }

        private Color getBgColor() {
            Color background;
            Container parent = BasicTabbedPaneUI.this.tabPane.getParent();
            if (parent != null && (background = parent.getBackground()) != null) {
                return background;
            }
            return UIManager.getColor("control");
        }

        @Override // javax.swing.JComponent
        protected void paintComponent(Graphics graphics) {
            super.paintComponent(graphics);
            if (isParamsSet() && (graphics instanceof Graphics2D)) {
                Graphics2D graphics2D = (Graphics2D) graphics;
                graphics2D.clipRect(0, 0, getWidth(), getHeight());
                graphics2D.setColor(getBgColor());
                graphics2D.translate(this.cropx, this.cropy);
                graphics2D.fill(this.shape);
                BasicTabbedPaneUI.this.paintCroppedTabEdge(graphics);
                graphics2D.translate(-this.cropx, -this.cropy);
            }
        }
    }
}
