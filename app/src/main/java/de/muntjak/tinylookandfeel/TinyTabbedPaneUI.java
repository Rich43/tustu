package de.muntjak.tinylookandfeel;

import de.muntjak.tinylookandfeel.util.ColorRoutines;
import de.muntjak.tinylookandfeel.util.DrawRoutines;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import javax.swing.JComponent;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicGraphicsUtils;
import javax.swing.plaf.basic.BasicTabbedPaneUI;
import javax.swing.text.View;

/* loaded from: tinylaf.jar:de/muntjak/tinylookandfeel/TinyTabbedPaneUI.class */
public class TinyTabbedPaneUI extends BasicTabbedPaneUI {
    int rollover = -1;

    /* loaded from: tinylaf.jar:de/muntjak/tinylookandfeel/TinyTabbedPaneUI$TinyMouseHandler.class */
    public class TinyMouseHandler implements MouseListener, MouseMotionListener {
        private final TinyTabbedPaneUI this$0;

        public TinyMouseHandler(TinyTabbedPaneUI tinyTabbedPaneUI) {
            this.this$0 = tinyTabbedPaneUI;
        }

        @Override // java.awt.event.MouseListener
        public void mousePressed(MouseEvent mouseEvent) {
            int tabAtLocation;
            if (this.this$0.tabPane.isEnabled() && (tabAtLocation = this.this$0.getTabAtLocation(mouseEvent.getX(), mouseEvent.getY())) >= 0 && this.this$0.tabPane.isEnabledAt(tabAtLocation)) {
                if (tabAtLocation != this.this$0.tabPane.getSelectedIndex()) {
                    this.this$0.tabPane.setSelectedIndex(tabAtLocation);
                } else if (this.this$0.tabPane.isRequestFocusEnabled()) {
                    this.this$0.tabPane.requestFocus();
                }
            }
        }

        @Override // java.awt.event.MouseListener
        public void mouseEntered(MouseEvent mouseEvent) {
        }

        @Override // java.awt.event.MouseListener
        public void mouseExited(MouseEvent mouseEvent) {
            if (this.this$0.rollover >= this.this$0.tabPane.getTabCount()) {
                this.this$0.rollover = -1;
            }
            if (this.this$0.rollover != -1) {
                this.this$0.tabPane.repaint(this.this$0.getTabBounds(this.this$0.tabPane, this.this$0.rollover));
                this.this$0.rollover = -1;
            }
        }

        @Override // java.awt.event.MouseListener
        public void mouseClicked(MouseEvent mouseEvent) {
        }

        @Override // java.awt.event.MouseListener
        public void mouseReleased(MouseEvent mouseEvent) {
        }

        @Override // java.awt.event.MouseMotionListener
        public void mouseDragged(MouseEvent mouseEvent) {
        }

        @Override // java.awt.event.MouseMotionListener
        public void mouseMoved(MouseEvent mouseEvent) {
            if (this.this$0.tabPane != null && this.this$0.tabPane.isEnabled()) {
                if (TinyUtils.is1dot4() && this.this$0.scrollableTabLayoutEnabled()) {
                    return;
                }
                this.this$0.checkRollOver(this.this$0.getTabAtLocation(mouseEvent.getX(), mouseEvent.getY()));
            }
        }
    }

    /* loaded from: tinylaf.jar:de/muntjak/tinylookandfeel/TinyTabbedPaneUI$TinyTabbedPaneLayout.class */
    protected class TinyTabbedPaneLayout extends BasicTabbedPaneUI.TabbedPaneLayout {
        private final TinyTabbedPaneUI this$0;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        protected TinyTabbedPaneLayout(TinyTabbedPaneUI tinyTabbedPaneUI) {
            super();
            this.this$0 = tinyTabbedPaneUI;
        }

        @Override // javax.swing.plaf.basic.BasicTabbedPaneUI.TabbedPaneLayout
        protected void rotateTabRuns(int i2, int i3) {
            if (Theme.fixedTabs.getValue()) {
                return;
            }
            super.rotateTabRuns(i2, i3);
        }
    }

    public static ComponentUI createUI(JComponent jComponent) {
        return new TinyTabbedPaneUI();
    }

    @Override // javax.swing.plaf.basic.BasicTabbedPaneUI
    protected void installListeners() {
        super.installListeners();
        this.tabPane.addMouseMotionListener((MouseMotionListener) this.mouseListener);
    }

    @Override // javax.swing.plaf.basic.BasicTabbedPaneUI
    protected MouseListener createMouseListener() {
        return new TinyMouseHandler(this);
    }

    @Override // javax.swing.plaf.basic.BasicTabbedPaneUI
    protected void installDefaults() {
        super.installDefaults();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean scrollableTabLayoutEnabled() {
        return this.tabPane.getTabLayoutPolicy() == 1;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void checkRollOver(int i2) {
        if (this.rollover >= this.tabPane.getTabCount()) {
            this.rollover = -1;
        }
        if (i2 == this.rollover) {
            return;
        }
        if (this.rollover != -1) {
            this.tabPane.repaint(getTabBounds(this.tabPane, this.rollover));
            if (i2 == -1) {
                this.rollover = -1;
            }
        }
        if (i2 < 0 || !this.tabPane.isEnabledAt(i2)) {
            return;
        }
        this.rollover = i2;
        this.tabPane.repaint(getTabBounds(this.tabPane, i2));
    }

    @Override // javax.swing.plaf.basic.BasicTabbedPaneUI
    protected LayoutManager createLayoutManager() {
        return this.tabPane.getTabLayoutPolicy() == 1 ? super.createLayoutManager() : new TinyTabbedPaneLayout(this);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int getTabAtLocation(int i2, int i3) {
        if (!TinyUtils.is1dot4()) {
            return tabForCoordinate(this.tabPane, i2, i3);
        }
        ensureCurrentLayout();
        int tabCount = this.tabPane.getTabCount();
        for (int i4 = 0; i4 < tabCount; i4++) {
            if (this.rects[i4].contains(i2, i3)) {
                return i4;
            }
        }
        return -1;
    }

    private void ensureCurrentLayout() {
        if (!this.tabPane.isValid()) {
            this.tabPane.validate();
        }
        if (this.tabPane.isValid()) {
            return;
        }
        ((BasicTabbedPaneUI.TabbedPaneLayout) this.tabPane.getLayout()).calculateLayoutInfo();
    }

    @Override // javax.swing.plaf.basic.BasicTabbedPaneUI
    protected void paintTabBackground(Graphics graphics, int i2, int i3, int i4, int i5, int i6, int i7, boolean z2) {
        boolean zIsEnabled = this.tabPane.isEnabled() & this.tabPane.isEnabledAt(i3);
        if (!z2 || Theme.ignoreSelectedBg.getValue()) {
            if (zIsEnabled) {
                graphics.setColor(this.tabPane.getBackgroundAt(i3));
            } else {
                graphics.setColor(Theme.tabDisabledColor.getColor());
            }
        } else if (zIsEnabled) {
            graphics.setColor(Theme.tabSelectedColor.getColor());
        } else {
            graphics.setColor(Theme.tabDisabledSelectedColor.getColor());
        }
        switch (i2) {
            case 1:
            default:
                graphics.fillRect(i4 + 1, i5 + 1, i6 - 3, i7 - 1);
                break;
            case 2:
                graphics.fillRect(i4 + 1, i5 + 1, i6 - 1, i7 - 3);
                break;
            case 3:
                graphics.fillRect(i4 + 1, i5 - 2, i6 - 3, i7 - 1);
                break;
            case 4:
                graphics.fillRect(i4 - 2, i5 + 1, i6 - 1, i7 - 3);
                break;
        }
    }

    @Override // javax.swing.plaf.basic.BasicTabbedPaneUI
    protected void paintContentBorder(Graphics graphics, int i2, int i3) {
    }

    @Override // javax.swing.plaf.basic.BasicTabbedPaneUI
    protected void paintFocusIndicator(Graphics graphics, int i2, Rectangle[] rectangleArr, int i3, Rectangle rectangle, Rectangle rectangle2, boolean z2) {
        int i4;
        int i5;
        int i6;
        int i7;
        if (Theme.tabFocus.getValue()) {
            Rectangle rectangle3 = rectangleArr[i3];
            if (this.tabPane.hasFocus() && z2) {
                graphics.setColor(Theme.tabFontColor.getColor());
                switch (i2) {
                    case 1:
                    default:
                        i4 = rectangle3.f12372x + 3;
                        i5 = rectangle3.f12373y + 3;
                        i6 = rectangle3.width - 7;
                        i7 = rectangle3.height - 5;
                        break;
                    case 2:
                        i4 = rectangle3.f12372x + 3;
                        i5 = rectangle3.f12373y + 3;
                        i6 = rectangle3.width - 5;
                        i7 = rectangle3.height - 7;
                        break;
                    case 3:
                        i4 = rectangle3.f12372x + 3;
                        i5 = rectangle3.f12373y;
                        i6 = rectangle3.width - 7;
                        i7 = rectangle3.height - 5;
                        break;
                    case 4:
                        i4 = rectangle3.f12372x;
                        i5 = rectangle3.f12373y + 3;
                        i6 = rectangle3.width - 5;
                        i7 = rectangle3.height - 7;
                        break;
                }
                BasicGraphicsUtils.drawDashedRect(graphics, i4, i5, i6, i7);
            }
        }
    }

    @Override // javax.swing.plaf.basic.BasicTabbedPaneUI
    protected void paintTabBorder(Graphics graphics, int i2, int i3, int i4, int i5, int i6, int i7, boolean z2) {
        boolean zIsEnabledAt = this.tabPane.isEnabledAt(i3);
        if (!this.tabPane.isEnabled()) {
            zIsEnabledAt = false;
        }
        drawXpTabBorder(graphics, i2, i4, i5, i6, i7, z2, zIsEnabledAt, this.rollover == i3);
    }

    private void drawXpTabBorder(Graphics graphics, int i2, int i3, int i4, int i5, int i6, boolean z2, boolean z3, boolean z4) {
        if (!z3) {
            DrawRoutines.drawXpTabBorder(graphics, Theme.tabDisabledBorderColor.getColor(), i3, i4, i5, i6, i2);
            return;
        }
        if (z2) {
            DrawRoutines.drawSelectedXpTabBorder(graphics, Theme.tabBorderColor.getColor(), i3, i4, i5, i6, i2);
        } else if (z4 && Theme.tabRollover.getValue()) {
            DrawRoutines.drawSelectedXpTabBorder(graphics, Theme.tabBorderColor.getColor(), i3, i4, i5, i6, i2);
        } else {
            DrawRoutines.drawXpTabBorder(graphics, Theme.tabBorderColor.getColor(), i3, i4, i5, i6, i2);
        }
    }

    @Override // javax.swing.plaf.ComponentUI
    public void update(Graphics graphics, JComponent jComponent) {
        Insets insets = this.tabPane.getInsets();
        int iCalculateTabAreaWidth = insets.left;
        int iCalculateTabAreaHeight = insets.top;
        int width = (this.tabPane.getWidth() - insets.right) - insets.left;
        int height = (this.tabPane.getHeight() - insets.top) - insets.bottom;
        if (jComponent.isOpaque()) {
            graphics.setColor(Theme.backColor.getColor());
            graphics.fillRect(0, 0, jComponent.getWidth(), jComponent.getHeight());
        }
        int tabPlacement = this.tabPane.getTabPlacement();
        switch (tabPlacement) {
            case 1:
            default:
                iCalculateTabAreaHeight += calculateTabAreaHeight(tabPlacement, this.runCount, this.maxTabHeight);
                height -= iCalculateTabAreaHeight - insets.top;
                break;
            case 2:
                iCalculateTabAreaWidth += calculateTabAreaWidth(tabPlacement, this.runCount, this.maxTabWidth);
                width -= iCalculateTabAreaWidth - insets.left;
                break;
            case 3:
                height -= calculateTabAreaHeight(tabPlacement, this.runCount, this.maxTabHeight);
                break;
            case 4:
                width -= calculateTabAreaWidth(tabPlacement, this.runCount, this.maxTabWidth);
                break;
        }
        drawXpContentBorder(graphics, iCalculateTabAreaWidth, iCalculateTabAreaHeight, width, height);
        super.paint(graphics, jComponent);
    }

    private void drawXpContentBorder(Graphics graphics, int i2, int i3, int i4, int i5) {
        if (this.tabPane.isEnabled()) {
            graphics.setColor(Theme.tabPaneBorderColor.getColor());
        } else {
            graphics.setColor(Theme.tabPaneDisabledBorderColor.getColor());
        }
        graphics.drawRect(i2, i3, i4 - 3, i5 - 3);
        graphics.setColor(ColorRoutines.darken(Theme.backColor.getColor(), 15));
        graphics.drawLine((i2 + i4) - 2, i3 + 1, (i2 + i4) - 2, (i3 + i5) - 2);
        graphics.drawLine(i2 + 1, (i3 + i5) - 2, (i2 + i4) - 3, (i3 + i5) - 2);
    }

    @Override // javax.swing.plaf.basic.BasicTabbedPaneUI
    protected int getTabLabelShiftX(int i2, int i3, boolean z2) {
        int i4;
        Rectangle rectangle = this.rects[i3];
        switch (i2) {
            case 1:
            case 3:
            default:
                i4 = 0;
                break;
            case 2:
                i4 = z2 ? -1 : 1;
                break;
            case 4:
                i4 = z2 ? 1 : -1;
                break;
        }
        return i4;
    }

    @Override // javax.swing.plaf.basic.BasicTabbedPaneUI
    protected int getTabLabelShiftY(int i2, int i3, boolean z2) {
        int i4;
        Rectangle rectangle = this.rects[i3];
        switch (i2) {
            case 1:
            default:
                i4 = z2 ? -1 : 1;
                break;
            case 2:
            case 4:
                i4 = rectangle.height % 2;
                break;
            case 3:
                i4 = z2 ? 1 : -1;
                break;
        }
        return i4;
    }

    @Override // javax.swing.plaf.basic.BasicTabbedPaneUI
    protected void paintText(Graphics graphics, int i2, Font font, FontMetrics fontMetrics, int i3, String str, Rectangle rectangle, boolean z2) {
        graphics.setFont(font);
        View textViewForTab = getTextViewForTab(i3);
        if (textViewForTab != null) {
            textViewForTab.paint(graphics, rectangle);
            return;
        }
        int displayedMnemonicIndexAt = this.tabPane.getDisplayedMnemonicIndexAt(i3);
        if (this.tabPane.isEnabled() && this.tabPane.isEnabledAt(i3)) {
            graphics.setColor(this.tabPane.getForegroundAt(i3));
        } else {
            graphics.setColor(Theme.tabDisabledTextColor.getColor());
        }
        TinyUtils.drawStringUnderlineCharAt(this.tabPane, graphics, str, displayedMnemonicIndexAt, rectangle.f12372x, rectangle.f12373y + fontMetrics.getAscent());
    }
}
