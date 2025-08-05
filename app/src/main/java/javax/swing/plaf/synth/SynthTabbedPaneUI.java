package javax.swing.plaf.synth;

import java.awt.Component;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JTabbedPane;
import javax.swing.UIManager;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.UIResource;
import javax.swing.plaf.basic.BasicTabbedPaneUI;
import javax.swing.text.View;
import sun.swing.SwingUtilities2;

/* loaded from: rt.jar:javax/swing/plaf/synth/SynthTabbedPaneUI.class */
public class SynthTabbedPaneUI extends BasicTabbedPaneUI implements PropertyChangeListener, SynthUI {
    private SynthContext tabAreaContext;
    private SynthContext tabContext;
    private SynthContext tabContentContext;
    private SynthStyle style;
    private SynthStyle tabStyle;
    private SynthStyle tabAreaStyle;
    private SynthStyle tabContentStyle;
    private int tabOverlap = 0;
    private boolean extendTabsToBase = false;
    private Rectangle textRect = new Rectangle();
    private Rectangle iconRect = new Rectangle();
    private Rectangle tabAreaBounds = new Rectangle();
    private boolean tabAreaStatesMatchSelectedTab = false;
    private boolean nudgeSelectedLabel = true;
    private boolean selectedTabIsPressed = false;

    public static ComponentUI createUI(JComponent jComponent) {
        return new SynthTabbedPaneUI();
    }

    private boolean scrollableTabLayoutEnabled() {
        return this.tabPane.getTabLayoutPolicy() == 1;
    }

    @Override // javax.swing.plaf.basic.BasicTabbedPaneUI
    protected void installDefaults() {
        updateStyle(this.tabPane);
    }

    private void updateStyle(JTabbedPane jTabbedPane) {
        SynthContext context = getContext(jTabbedPane, 1);
        SynthStyle synthStyle = this.style;
        this.style = SynthLookAndFeel.updateStyle(context, this);
        if (this.style != synthStyle) {
            this.tabRunOverlay = this.style.getInt(context, "TabbedPane.tabRunOverlay", 0);
            this.tabOverlap = this.style.getInt(context, "TabbedPane.tabOverlap", 0);
            this.extendTabsToBase = this.style.getBoolean(context, "TabbedPane.extendTabsToBase", false);
            this.textIconGap = this.style.getInt(context, "TabbedPane.textIconGap", 0);
            this.selectedTabPadInsets = (Insets) this.style.get(context, "TabbedPane.selectedTabPadInsets");
            if (this.selectedTabPadInsets == null) {
                this.selectedTabPadInsets = new Insets(0, 0, 0, 0);
            }
            this.tabAreaStatesMatchSelectedTab = this.style.getBoolean(context, "TabbedPane.tabAreaStatesMatchSelectedTab", false);
            this.nudgeSelectedLabel = this.style.getBoolean(context, "TabbedPane.nudgeSelectedLabel", true);
            if (synthStyle != null) {
                uninstallKeyboardActions();
                installKeyboardActions();
            }
        }
        context.dispose();
        if (this.tabContext != null) {
            this.tabContext.dispose();
        }
        this.tabContext = getContext(jTabbedPane, Region.TABBED_PANE_TAB, 1);
        this.tabStyle = SynthLookAndFeel.updateStyle(this.tabContext, this);
        this.tabInsets = this.tabStyle.getInsets(this.tabContext, null);
        if (this.tabAreaContext != null) {
            this.tabAreaContext.dispose();
        }
        this.tabAreaContext = getContext(jTabbedPane, Region.TABBED_PANE_TAB_AREA, 1);
        this.tabAreaStyle = SynthLookAndFeel.updateStyle(this.tabAreaContext, this);
        this.tabAreaInsets = this.tabAreaStyle.getInsets(this.tabAreaContext, null);
        if (this.tabContentContext != null) {
            this.tabContentContext.dispose();
        }
        this.tabContentContext = getContext(jTabbedPane, Region.TABBED_PANE_CONTENT, 1);
        this.tabContentStyle = SynthLookAndFeel.updateStyle(this.tabContentContext, this);
        this.contentBorderInsets = this.tabContentStyle.getInsets(this.tabContentContext, null);
    }

    @Override // javax.swing.plaf.basic.BasicTabbedPaneUI
    protected void installListeners() {
        super.installListeners();
        this.tabPane.addPropertyChangeListener(this);
    }

    @Override // javax.swing.plaf.basic.BasicTabbedPaneUI
    protected void uninstallListeners() {
        super.uninstallListeners();
        this.tabPane.removePropertyChangeListener(this);
    }

    @Override // javax.swing.plaf.basic.BasicTabbedPaneUI
    protected void uninstallDefaults() {
        SynthContext context = getContext(this.tabPane, 1);
        this.style.uninstallDefaults(context);
        context.dispose();
        this.style = null;
        this.tabStyle.uninstallDefaults(this.tabContext);
        this.tabContext.dispose();
        this.tabContext = null;
        this.tabStyle = null;
        this.tabAreaStyle.uninstallDefaults(this.tabAreaContext);
        this.tabAreaContext.dispose();
        this.tabAreaContext = null;
        this.tabAreaStyle = null;
        this.tabContentStyle.uninstallDefaults(this.tabContentContext);
        this.tabContentContext.dispose();
        this.tabContentContext = null;
        this.tabContentStyle = null;
    }

    @Override // javax.swing.plaf.synth.SynthUI
    public SynthContext getContext(JComponent jComponent) {
        return getContext(jComponent, SynthLookAndFeel.getComponentState(jComponent));
    }

    private SynthContext getContext(JComponent jComponent, int i2) {
        return SynthContext.getContext(jComponent, this.style, i2);
    }

    private SynthContext getContext(JComponent jComponent, Region region, int i2) {
        SynthStyle synthStyle = null;
        if (region == Region.TABBED_PANE_TAB) {
            synthStyle = this.tabStyle;
        } else if (region == Region.TABBED_PANE_TAB_AREA) {
            synthStyle = this.tabAreaStyle;
        } else if (region == Region.TABBED_PANE_CONTENT) {
            synthStyle = this.tabContentStyle;
        }
        return SynthContext.getContext(jComponent, region, synthStyle, i2);
    }

    @Override // javax.swing.plaf.basic.BasicTabbedPaneUI
    protected JButton createScrollButton(int i2) {
        if (UIManager.getBoolean("TabbedPane.useBasicArrows")) {
            JButton jButtonCreateScrollButton = super.createScrollButton(i2);
            jButtonCreateScrollButton.setBorder(BorderFactory.createEmptyBorder());
            return jButtonCreateScrollButton;
        }
        return new SynthScrollableTabButton(i2);
    }

    @Override // java.beans.PropertyChangeListener
    public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
        if (SynthLookAndFeel.shouldUpdateStyle(propertyChangeEvent)) {
            updateStyle(this.tabPane);
        }
    }

    @Override // javax.swing.plaf.basic.BasicTabbedPaneUI
    protected MouseListener createMouseListener() {
        final MouseListener mouseListenerCreateMouseListener = super.createMouseListener();
        final MouseMotionListener mouseMotionListener = (MouseMotionListener) mouseListenerCreateMouseListener;
        return new MouseListener() { // from class: javax.swing.plaf.synth.SynthTabbedPaneUI.1
            @Override // java.awt.event.MouseListener
            public void mouseClicked(MouseEvent mouseEvent) {
                mouseListenerCreateMouseListener.mouseClicked(mouseEvent);
            }

            @Override // java.awt.event.MouseListener
            public void mouseEntered(MouseEvent mouseEvent) {
                mouseListenerCreateMouseListener.mouseEntered(mouseEvent);
            }

            @Override // java.awt.event.MouseListener
            public void mouseExited(MouseEvent mouseEvent) {
                mouseListenerCreateMouseListener.mouseExited(mouseEvent);
            }

            @Override // java.awt.event.MouseListener
            public void mousePressed(MouseEvent mouseEvent) {
                if (SynthTabbedPaneUI.this.tabPane.isEnabled()) {
                    int iTabForCoordinate = SynthTabbedPaneUI.this.tabForCoordinate(SynthTabbedPaneUI.this.tabPane, mouseEvent.getX(), mouseEvent.getY());
                    if (iTabForCoordinate >= 0 && SynthTabbedPaneUI.this.tabPane.isEnabledAt(iTabForCoordinate) && iTabForCoordinate == SynthTabbedPaneUI.this.tabPane.getSelectedIndex()) {
                        SynthTabbedPaneUI.this.selectedTabIsPressed = true;
                        SynthTabbedPaneUI.this.tabPane.repaint();
                    }
                    mouseListenerCreateMouseListener.mousePressed(mouseEvent);
                }
            }

            @Override // java.awt.event.MouseListener
            public void mouseReleased(MouseEvent mouseEvent) {
                if (SynthTabbedPaneUI.this.selectedTabIsPressed) {
                    SynthTabbedPaneUI.this.selectedTabIsPressed = false;
                    SynthTabbedPaneUI.this.tabPane.repaint();
                }
                mouseListenerCreateMouseListener.mouseReleased(mouseEvent);
                mouseMotionListener.mouseMoved(mouseEvent);
            }
        };
    }

    @Override // javax.swing.plaf.basic.BasicTabbedPaneUI
    protected int getTabLabelShiftX(int i2, int i3, boolean z2) {
        if (this.nudgeSelectedLabel) {
            return super.getTabLabelShiftX(i2, i3, z2);
        }
        return 0;
    }

    @Override // javax.swing.plaf.basic.BasicTabbedPaneUI
    protected int getTabLabelShiftY(int i2, int i3, boolean z2) {
        if (this.nudgeSelectedLabel) {
            return super.getTabLabelShiftY(i2, i3, z2);
        }
        return 0;
    }

    @Override // javax.swing.plaf.ComponentUI
    public void update(Graphics graphics, JComponent jComponent) {
        SynthContext context = getContext(jComponent);
        SynthLookAndFeel.update(context, graphics);
        context.getPainter().paintTabbedPaneBackground(context, graphics, 0, 0, jComponent.getWidth(), jComponent.getHeight());
        paint(context, graphics);
        context.dispose();
    }

    @Override // javax.swing.plaf.basic.BasicTabbedPaneUI
    protected int getBaseline(int i2) {
        if (this.tabPane.getTabComponentAt(i2) != null || getTextViewForTab(i2) != null) {
            return super.getBaseline(i2);
        }
        String titleAt = this.tabPane.getTitleAt(i2);
        FontMetrics fontMetrics = getFontMetrics(this.tabContext.getStyle().getFont(this.tabContext));
        Icon iconForTab = getIconForTab(i2);
        this.textRect.setBounds(0, 0, 0, 0);
        this.iconRect.setBounds(0, 0, 0, 0);
        this.calcRect.setBounds(0, 0, Short.MAX_VALUE, this.maxTabHeight);
        this.tabContext.getStyle().getGraphicsUtils(this.tabContext).layoutText(this.tabContext, fontMetrics, titleAt, iconForTab, 0, 0, 10, 0, this.calcRect, this.iconRect, this.textRect, this.textIconGap);
        return this.textRect.f12373y + fontMetrics.getAscent() + getBaselineOffset();
    }

    @Override // javax.swing.plaf.synth.SynthUI
    public void paintBorder(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
        synthContext.getPainter().paintTabbedPaneBorder(synthContext, graphics, i2, i3, i4, i5);
    }

    @Override // javax.swing.plaf.basic.BasicTabbedPaneUI, javax.swing.plaf.ComponentUI
    public void paint(Graphics graphics, JComponent jComponent) {
        SynthContext context = getContext(jComponent);
        paint(context, graphics);
        context.dispose();
    }

    protected void paint(SynthContext synthContext, Graphics graphics) {
        int selectedIndex = this.tabPane.getSelectedIndex();
        int tabPlacement = this.tabPane.getTabPlacement();
        ensureCurrentLayout();
        if (!scrollableTabLayoutEnabled()) {
            Insets insets = this.tabPane.getInsets();
            int i2 = insets.left;
            int i3 = insets.top;
            int width = (this.tabPane.getWidth() - insets.left) - insets.right;
            int height = (this.tabPane.getHeight() - insets.top) - insets.bottom;
            switch (tabPlacement) {
                case 1:
                default:
                    height = calculateTabAreaHeight(tabPlacement, this.runCount, this.maxTabHeight);
                    break;
                case 2:
                    width = calculateTabAreaWidth(tabPlacement, this.runCount, this.maxTabWidth);
                    break;
                case 3:
                    int iCalculateTabAreaHeight = calculateTabAreaHeight(tabPlacement, this.runCount, this.maxTabHeight);
                    i3 = (i3 + height) - iCalculateTabAreaHeight;
                    height = iCalculateTabAreaHeight;
                    break;
                case 4:
                    int iCalculateTabAreaWidth = calculateTabAreaWidth(tabPlacement, this.runCount, this.maxTabWidth);
                    i2 = (i2 + width) - iCalculateTabAreaWidth;
                    width = iCalculateTabAreaWidth;
                    break;
            }
            this.tabAreaBounds.setBounds(i2, i3, width, height);
            if (graphics.getClipBounds().intersects(this.tabAreaBounds)) {
                paintTabArea(this.tabAreaContext, graphics, tabPlacement, selectedIndex, this.tabAreaBounds);
            }
        }
        paintContentBorder(this.tabContentContext, graphics, tabPlacement, selectedIndex);
    }

    @Override // javax.swing.plaf.basic.BasicTabbedPaneUI
    protected void paintTabArea(Graphics graphics, int i2, int i3) {
        Insets insets = this.tabPane.getInsets();
        paintTabArea(this.tabAreaContext, graphics, i2, i3, new Rectangle(insets.left, insets.top, (this.tabPane.getWidth() - insets.left) - insets.right, (this.tabPane.getHeight() - insets.top) - insets.bottom));
    }

    private void paintTabArea(SynthContext synthContext, Graphics graphics, int i2, int i3, Rectangle rectangle) {
        Rectangle clipBounds = graphics.getClipBounds();
        if (this.tabAreaStatesMatchSelectedTab && i3 >= 0) {
            updateTabContext(i3, true, this.selectedTabIsPressed, getRolloverTab() == i3, getFocusIndex() == i3);
            synthContext.setComponentState(this.tabContext.getComponentState());
        } else {
            synthContext.setComponentState(1);
        }
        SynthLookAndFeel.updateSubregion(synthContext, graphics, rectangle);
        synthContext.getPainter().paintTabbedPaneTabAreaBackground(synthContext, graphics, rectangle.f12372x, rectangle.f12373y, rectangle.width, rectangle.height, i2);
        synthContext.getPainter().paintTabbedPaneTabAreaBorder(synthContext, graphics, rectangle.f12372x, rectangle.f12373y, rectangle.width, rectangle.height, i2);
        int tabCount = this.tabPane.getTabCount();
        this.iconRect.setBounds(0, 0, 0, 0);
        this.textRect.setBounds(0, 0, 0, 0);
        int i4 = this.runCount - 1;
        while (i4 >= 0) {
            int i5 = this.tabRuns[i4];
            int i6 = this.tabRuns[i4 == this.runCount - 1 ? 0 : i4 + 1];
            int i7 = i6 != 0 ? i6 - 1 : tabCount - 1;
            for (int i8 = i5; i8 <= i7; i8++) {
                if (this.rects[i8].intersects(clipBounds) && i3 != i8) {
                    paintTab(this.tabContext, graphics, i2, this.rects, i8, this.iconRect, this.textRect);
                }
            }
            i4--;
        }
        if (i3 >= 0 && this.rects[i3].intersects(clipBounds)) {
            paintTab(this.tabContext, graphics, i2, this.rects, i3, this.iconRect, this.textRect);
        }
    }

    @Override // javax.swing.plaf.basic.BasicTabbedPaneUI
    protected void setRolloverTab(int i2) {
        Rectangle tabBounds;
        Rectangle tabBounds2;
        int rolloverTab = getRolloverTab();
        super.setRolloverTab(i2);
        if (rolloverTab != i2 && this.tabAreaStatesMatchSelectedTab) {
            this.tabPane.repaint();
            return;
        }
        if (rolloverTab >= 0 && rolloverTab < this.tabPane.getTabCount() && (tabBounds2 = getTabBounds(this.tabPane, rolloverTab)) != null) {
            this.tabPane.repaint(tabBounds2);
        }
        if (i2 >= 0 && (tabBounds = getTabBounds(this.tabPane, i2)) != null) {
            this.tabPane.repaint(tabBounds);
        }
    }

    private void paintTab(SynthContext synthContext, Graphics graphics, int i2, Rectangle[] rectangleArr, int i3, Rectangle rectangle, Rectangle rectangle2) {
        Rectangle rectangle3 = rectangleArr[i3];
        int selectedIndex = this.tabPane.getSelectedIndex();
        boolean z2 = selectedIndex == i3;
        updateTabContext(i3, z2, z2 && this.selectedTabIsPressed, getRolloverTab() == i3, getFocusIndex() == i3);
        SynthLookAndFeel.updateSubregion(synthContext, graphics, rectangle3);
        int i4 = rectangle3.f12372x;
        int i5 = rectangle3.f12373y;
        int i6 = rectangle3.height;
        int i7 = rectangle3.width;
        int tabPlacement = this.tabPane.getTabPlacement();
        if (this.extendTabsToBase && this.runCount > 1 && selectedIndex >= 0) {
            Rectangle rectangle4 = rectangleArr[selectedIndex];
            switch (tabPlacement) {
                case 1:
                    i6 = (rectangle4.f12373y + rectangle4.height) - rectangle3.f12373y;
                    break;
                case 2:
                    i7 = (rectangle4.f12372x + rectangle4.width) - rectangle3.f12372x;
                    break;
                case 3:
                    int i8 = rectangle4.f12373y;
                    i6 = (rectangle3.f12373y + rectangle3.height) - i8;
                    i5 = i8;
                    break;
                case 4:
                    int i9 = rectangle4.f12372x;
                    i7 = (rectangle3.f12372x + rectangle3.width) - i9;
                    i4 = i9;
                    break;
            }
        }
        this.tabContext.getPainter().paintTabbedPaneTabBackground(this.tabContext, graphics, i4, i5, i7, i6, i3, tabPlacement);
        this.tabContext.getPainter().paintTabbedPaneTabBorder(this.tabContext, graphics, i4, i5, i7, i6, i3, tabPlacement);
        if (this.tabPane.getTabComponentAt(i3) == null) {
            String titleAt = this.tabPane.getTitleAt(i3);
            Font font = synthContext.getStyle().getFont(synthContext);
            FontMetrics fontMetrics = SwingUtilities2.getFontMetrics(this.tabPane, graphics, font);
            Icon iconForTab = getIconForTab(i3);
            layoutLabel(synthContext, i2, fontMetrics, i3, titleAt, iconForTab, rectangle3, rectangle, rectangle2, z2);
            paintText(synthContext, graphics, i2, font, fontMetrics, i3, titleAt, rectangle2, z2);
            paintIcon(graphics, i2, i3, iconForTab, rectangle, z2);
        }
    }

    private void layoutLabel(SynthContext synthContext, int i2, FontMetrics fontMetrics, int i3, String str, Icon icon, Rectangle rectangle, Rectangle rectangle2, Rectangle rectangle3, boolean z2) {
        View textViewForTab = getTextViewForTab(i3);
        if (textViewForTab != null) {
            this.tabPane.putClientProperty("html", textViewForTab);
        }
        rectangle2.f12373y = 0;
        rectangle2.f12372x = 0;
        rectangle3.f12373y = 0;
        rectangle3.f12372x = 0;
        synthContext.getStyle().getGraphicsUtils(synthContext).layoutText(synthContext, fontMetrics, str, icon, 0, 0, 10, 0, rectangle, rectangle2, rectangle3, this.textIconGap);
        this.tabPane.putClientProperty("html", null);
        int tabLabelShiftX = getTabLabelShiftX(i2, i3, z2);
        int tabLabelShiftY = getTabLabelShiftY(i2, i3, z2);
        rectangle2.f12372x += tabLabelShiftX;
        rectangle2.f12373y += tabLabelShiftY;
        rectangle3.f12372x += tabLabelShiftX;
        rectangle3.f12373y += tabLabelShiftY;
    }

    private void paintText(SynthContext synthContext, Graphics graphics, int i2, Font font, FontMetrics fontMetrics, int i3, String str, Rectangle rectangle, boolean z2) {
        graphics.setFont(font);
        View textViewForTab = getTextViewForTab(i3);
        if (textViewForTab != null) {
            textViewForTab.paint(graphics, rectangle);
            return;
        }
        int displayedMnemonicIndexAt = this.tabPane.getDisplayedMnemonicIndexAt(i3);
        graphics.setColor(synthContext.getStyle().getColor(synthContext, ColorType.TEXT_FOREGROUND));
        synthContext.getStyle().getGraphicsUtils(synthContext).paintText(synthContext, graphics, str, rectangle, displayedMnemonicIndexAt);
    }

    private void paintContentBorder(SynthContext synthContext, Graphics graphics, int i2, int i3) {
        int width = this.tabPane.getWidth();
        int height = this.tabPane.getHeight();
        Insets insets = this.tabPane.getInsets();
        int iCalculateTabAreaWidth = insets.left;
        int iCalculateTabAreaHeight = insets.top;
        int iCalculateTabAreaWidth2 = (width - insets.right) - insets.left;
        int iCalculateTabAreaHeight2 = (height - insets.top) - insets.bottom;
        switch (i2) {
            case 1:
            default:
                iCalculateTabAreaHeight += calculateTabAreaHeight(i2, this.runCount, this.maxTabHeight);
                iCalculateTabAreaHeight2 -= iCalculateTabAreaHeight - insets.top;
                break;
            case 2:
                iCalculateTabAreaWidth += calculateTabAreaWidth(i2, this.runCount, this.maxTabWidth);
                iCalculateTabAreaWidth2 -= iCalculateTabAreaWidth - insets.left;
                break;
            case 3:
                iCalculateTabAreaHeight2 -= calculateTabAreaHeight(i2, this.runCount, this.maxTabHeight);
                break;
            case 4:
                iCalculateTabAreaWidth2 -= calculateTabAreaWidth(i2, this.runCount, this.maxTabWidth);
                break;
        }
        SynthLookAndFeel.updateSubregion(synthContext, graphics, new Rectangle(iCalculateTabAreaWidth, iCalculateTabAreaHeight, iCalculateTabAreaWidth2, iCalculateTabAreaHeight2));
        synthContext.getPainter().paintTabbedPaneContentBackground(synthContext, graphics, iCalculateTabAreaWidth, iCalculateTabAreaHeight, iCalculateTabAreaWidth2, iCalculateTabAreaHeight2);
        synthContext.getPainter().paintTabbedPaneContentBorder(synthContext, graphics, iCalculateTabAreaWidth, iCalculateTabAreaHeight, iCalculateTabAreaWidth2, iCalculateTabAreaHeight2);
    }

    private void ensureCurrentLayout() {
        if (!this.tabPane.isValid()) {
            this.tabPane.validate();
        }
        if (!this.tabPane.isValid()) {
            ((BasicTabbedPaneUI.TabbedPaneLayout) this.tabPane.getLayout()).calculateLayoutInfo();
        }
    }

    @Override // javax.swing.plaf.basic.BasicTabbedPaneUI
    protected int calculateMaxTabHeight(int i2) {
        FontMetrics fontMetrics = getFontMetrics(this.tabContext.getStyle().getFont(this.tabContext));
        int tabCount = this.tabPane.getTabCount();
        int iMax = 0;
        int height = fontMetrics.getHeight();
        for (int i3 = 0; i3 < tabCount; i3++) {
            iMax = Math.max(calculateTabHeight(i2, i3, height), iMax);
        }
        return iMax;
    }

    @Override // javax.swing.plaf.basic.BasicTabbedPaneUI
    protected int calculateTabWidth(int i2, int i3, FontMetrics fontMetrics) {
        int iComputeStringWidth;
        Icon iconForTab = getIconForTab(i3);
        Insets tabInsets = getTabInsets(i2, i3);
        int iconWidth = tabInsets.left + tabInsets.right;
        Component tabComponentAt = this.tabPane.getTabComponentAt(i3);
        if (tabComponentAt != null) {
            iComputeStringWidth = iconWidth + tabComponentAt.getPreferredSize().width;
        } else {
            if (iconForTab != null) {
                iconWidth += iconForTab.getIconWidth() + this.textIconGap;
            }
            View textViewForTab = getTextViewForTab(i3);
            if (textViewForTab != null) {
                iComputeStringWidth = iconWidth + ((int) textViewForTab.getPreferredSpan(0));
            } else {
                iComputeStringWidth = iconWidth + this.tabContext.getStyle().getGraphicsUtils(this.tabContext).computeStringWidth(this.tabContext, fontMetrics.getFont(), fontMetrics, this.tabPane.getTitleAt(i3));
            }
        }
        return iComputeStringWidth;
    }

    @Override // javax.swing.plaf.basic.BasicTabbedPaneUI
    protected int calculateMaxTabWidth(int i2) {
        FontMetrics fontMetrics = getFontMetrics(this.tabContext.getStyle().getFont(this.tabContext));
        int tabCount = this.tabPane.getTabCount();
        int iMax = 0;
        for (int i3 = 0; i3 < tabCount; i3++) {
            iMax = Math.max(calculateTabWidth(i2, i3, fontMetrics), iMax);
        }
        return iMax;
    }

    @Override // javax.swing.plaf.basic.BasicTabbedPaneUI
    protected Insets getTabInsets(int i2, int i3) {
        updateTabContext(i3, false, false, false, getFocusIndex() == i3);
        return this.tabInsets;
    }

    @Override // javax.swing.plaf.basic.BasicTabbedPaneUI
    protected FontMetrics getFontMetrics() {
        return getFontMetrics(this.tabContext.getStyle().getFont(this.tabContext));
    }

    private FontMetrics getFontMetrics(Font font) {
        return this.tabPane.getFontMetrics(font);
    }

    private void updateTabContext(int i2, boolean z2, boolean z3, boolean z4, boolean z5) {
        int componentState;
        if (!this.tabPane.isEnabled() || !this.tabPane.isEnabledAt(i2)) {
            componentState = 0 | 8;
            if (z2) {
                componentState |= 512;
            }
        } else if (z2) {
            componentState = 0 | 513;
            if (z4 && UIManager.getBoolean("TabbedPane.isTabRollover")) {
                componentState |= 2;
            }
        } else {
            componentState = z4 ? 0 | 3 : SynthLookAndFeel.getComponentState(this.tabPane) & (-257);
        }
        if (z5 && this.tabPane.hasFocus()) {
            componentState |= 256;
        }
        if (z3) {
            componentState |= 4;
        }
        this.tabContext.setComponentState(componentState);
    }

    @Override // javax.swing.plaf.basic.BasicTabbedPaneUI
    protected LayoutManager createLayoutManager() {
        if (this.tabPane.getTabLayoutPolicy() == 1) {
            return super.createLayoutManager();
        }
        return new BasicTabbedPaneUI.TabbedPaneLayout() { // from class: javax.swing.plaf.synth.SynthTabbedPaneUI.2
            @Override // javax.swing.plaf.basic.BasicTabbedPaneUI.TabbedPaneLayout
            public void calculateLayoutInfo() {
                super.calculateLayoutInfo();
                if (SynthTabbedPaneUI.this.tabOverlap != 0) {
                    int tabCount = SynthTabbedPaneUI.this.tabPane.getTabCount();
                    boolean zIsLeftToRight = SynthTabbedPaneUI.this.tabPane.getComponentOrientation().isLeftToRight();
                    int i2 = SynthTabbedPaneUI.this.runCount - 1;
                    while (i2 >= 0) {
                        int i3 = SynthTabbedPaneUI.this.tabRuns[i2];
                        int i4 = SynthTabbedPaneUI.this.tabRuns[i2 == SynthTabbedPaneUI.this.runCount - 1 ? 0 : i2 + 1];
                        int i5 = i4 != 0 ? i4 - 1 : tabCount - 1;
                        for (int i6 = i3 + 1; i6 <= i5; i6++) {
                            int i7 = 0;
                            int i8 = 0;
                            switch (SynthTabbedPaneUI.this.tabPane.getTabPlacement()) {
                                case 1:
                                case 3:
                                    i7 = zIsLeftToRight ? SynthTabbedPaneUI.this.tabOverlap : -SynthTabbedPaneUI.this.tabOverlap;
                                    break;
                                case 2:
                                case 4:
                                    i8 = SynthTabbedPaneUI.this.tabOverlap;
                                    break;
                            }
                            SynthTabbedPaneUI.this.rects[i6].f12372x += i7;
                            SynthTabbedPaneUI.this.rects[i6].f12373y += i8;
                            SynthTabbedPaneUI.this.rects[i6].width += Math.abs(i7);
                            SynthTabbedPaneUI.this.rects[i6].height += Math.abs(i8);
                        }
                        i2--;
                    }
                }
            }
        };
    }

    /* loaded from: rt.jar:javax/swing/plaf/synth/SynthTabbedPaneUI$SynthScrollableTabButton.class */
    private class SynthScrollableTabButton extends SynthArrowButton implements UIResource {
        public SynthScrollableTabButton(int i2) {
            super(i2);
            setName("TabbedPane.button");
        }
    }
}
