package com.sun.java.swing.plaf.motif;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import javax.swing.JComponent;
import javax.swing.UIManager;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicTabbedPaneUI;

/* loaded from: rt.jar:com/sun/java/swing/plaf/motif/MotifTabbedPaneUI.class */
public class MotifTabbedPaneUI extends BasicTabbedPaneUI {
    protected Color unselectedTabBackground;
    protected Color unselectedTabForeground;
    protected Color unselectedTabShadow;
    protected Color unselectedTabHighlight;

    public static ComponentUI createUI(JComponent jComponent) {
        return new MotifTabbedPaneUI();
    }

    @Override // javax.swing.plaf.basic.BasicTabbedPaneUI
    protected void installDefaults() {
        super.installDefaults();
        this.unselectedTabBackground = UIManager.getColor("TabbedPane.unselectedTabBackground");
        this.unselectedTabForeground = UIManager.getColor("TabbedPane.unselectedTabForeground");
        this.unselectedTabShadow = UIManager.getColor("TabbedPane.unselectedTabShadow");
        this.unselectedTabHighlight = UIManager.getColor("TabbedPane.unselectedTabHighlight");
    }

    @Override // javax.swing.plaf.basic.BasicTabbedPaneUI
    protected void uninstallDefaults() {
        super.uninstallDefaults();
        this.unselectedTabBackground = null;
        this.unselectedTabForeground = null;
        this.unselectedTabShadow = null;
        this.unselectedTabHighlight = null;
    }

    @Override // javax.swing.plaf.basic.BasicTabbedPaneUI
    protected void paintContentBorderTopEdge(Graphics graphics, int i2, int i3, int i4, int i5, int i6, int i7) {
        Rectangle tabBounds = i3 < 0 ? null : getTabBounds(i3, this.calcRect);
        graphics.setColor(this.lightHighlight);
        if (i2 != 1 || i3 < 0 || tabBounds.f12372x < i4 || tabBounds.f12372x > i4 + i6) {
            graphics.drawLine(i4, i5, (i4 + i6) - 2, i5);
            return;
        }
        graphics.drawLine(i4, i5, tabBounds.f12372x - 1, i5);
        if (tabBounds.f12372x + tabBounds.width < (i4 + i6) - 2) {
            graphics.drawLine(tabBounds.f12372x + tabBounds.width, i5, (i4 + i6) - 2, i5);
        }
    }

    @Override // javax.swing.plaf.basic.BasicTabbedPaneUI
    protected void paintContentBorderBottomEdge(Graphics graphics, int i2, int i3, int i4, int i5, int i6, int i7) {
        Rectangle tabBounds = i3 < 0 ? null : getTabBounds(i3, this.calcRect);
        graphics.setColor(this.shadow);
        if (i2 != 3 || i3 < 0 || tabBounds.f12372x < i4 || tabBounds.f12372x > i4 + i6) {
            graphics.drawLine(i4 + 1, (i5 + i7) - 1, (i4 + i6) - 1, (i5 + i7) - 1);
            return;
        }
        graphics.drawLine(i4 + 1, (i5 + i7) - 1, tabBounds.f12372x - 1, (i5 + i7) - 1);
        if (tabBounds.f12372x + tabBounds.width < (i4 + i6) - 2) {
            graphics.drawLine(tabBounds.f12372x + tabBounds.width, (i5 + i7) - 1, (i4 + i6) - 2, (i5 + i7) - 1);
        }
    }

    @Override // javax.swing.plaf.basic.BasicTabbedPaneUI
    protected void paintContentBorderRightEdge(Graphics graphics, int i2, int i3, int i4, int i5, int i6, int i7) {
        Rectangle tabBounds = i3 < 0 ? null : getTabBounds(i3, this.calcRect);
        graphics.setColor(this.shadow);
        if (i2 != 4 || i3 < 0 || tabBounds.f12373y < i5 || tabBounds.f12373y > i5 + i7) {
            graphics.drawLine((i4 + i6) - 1, i5 + 1, (i4 + i6) - 1, (i5 + i7) - 1);
            return;
        }
        graphics.drawLine((i4 + i6) - 1, i5 + 1, (i4 + i6) - 1, tabBounds.f12373y - 1);
        if (tabBounds.f12373y + tabBounds.height < (i5 + i7) - 2) {
            graphics.drawLine((i4 + i6) - 1, tabBounds.f12373y + tabBounds.height, (i4 + i6) - 1, (i5 + i7) - 2);
        }
    }

    @Override // javax.swing.plaf.basic.BasicTabbedPaneUI
    protected void paintTabBackground(Graphics graphics, int i2, int i3, int i4, int i5, int i6, int i7, boolean z2) {
        graphics.setColor(z2 ? this.tabPane.getBackgroundAt(i3) : this.unselectedTabBackground);
        switch (i2) {
            case 1:
            default:
                graphics.fillRect(i4 + 1, i5 + 3, i6 - 2, i7 - 3);
                graphics.drawLine(i4 + 2, i5 + 2, (i4 + i6) - 3, i5 + 2);
                graphics.drawLine(i4 + 3, i5 + 1, (i4 + i6) - 4, i5 + 1);
                break;
            case 2:
                graphics.fillRect(i4 + 1, i5 + 1, i6 - 1, i7 - 2);
                break;
            case 3:
                graphics.fillRect(i4 + 1, i5, i6 - 2, i7 - 3);
                graphics.drawLine(i4 + 2, (i5 + i7) - 3, (i4 + i6) - 3, (i5 + i7) - 3);
                graphics.drawLine(i4 + 3, (i5 + i7) - 2, (i4 + i6) - 4, (i5 + i7) - 2);
                break;
            case 4:
                graphics.fillRect(i4, i5 + 1, i6 - 1, i7 - 2);
                break;
        }
    }

    @Override // javax.swing.plaf.basic.BasicTabbedPaneUI
    protected void paintTabBorder(Graphics graphics, int i2, int i3, int i4, int i5, int i6, int i7, boolean z2) {
        graphics.setColor(z2 ? this.lightHighlight : this.unselectedTabHighlight);
        switch (i2) {
            case 1:
            default:
                graphics.drawLine(i4, i5 + 2, i4, (i5 + i7) - 1);
                graphics.drawLine(i4 + 1, i5 + 1, i4 + 1, i5 + 2);
                graphics.drawLine(i4 + 2, i5, i4 + 2, i5 + 1);
                graphics.drawLine(i4 + 3, i5, (i4 + i6) - 4, i5);
                graphics.setColor(z2 ? this.shadow : this.unselectedTabShadow);
                graphics.drawLine((i4 + i6) - 3, i5, (i4 + i6) - 3, i5 + 1);
                graphics.drawLine((i4 + i6) - 2, i5 + 1, (i4 + i6) - 2, i5 + 2);
                graphics.drawLine((i4 + i6) - 1, i5 + 2, (i4 + i6) - 1, (i5 + i7) - 1);
                break;
            case 2:
                graphics.drawLine(i4, i5 + 2, i4, (i5 + i7) - 3);
                graphics.drawLine(i4 + 1, i5 + 1, i4 + 1, i5 + 2);
                graphics.drawLine(i4 + 2, i5, i4 + 2, i5 + 1);
                graphics.drawLine(i4 + 3, i5, (i4 + i6) - 1, i5);
                graphics.setColor(z2 ? this.shadow : this.unselectedTabShadow);
                graphics.drawLine(i4 + 1, (i5 + i7) - 3, i4 + 1, (i5 + i7) - 2);
                graphics.drawLine(i4 + 2, (i5 + i7) - 2, i4 + 2, (i5 + i7) - 1);
                graphics.drawLine(i4 + 3, (i5 + i7) - 1, (i4 + i6) - 1, (i5 + i7) - 1);
                break;
            case 3:
                graphics.drawLine(i4, i5, i4, (i5 + i7) - 3);
                graphics.drawLine(i4 + 1, (i5 + i7) - 3, i4 + 1, (i5 + i7) - 2);
                graphics.drawLine(i4 + 2, (i5 + i7) - 2, i4 + 2, (i5 + i7) - 1);
                graphics.setColor(z2 ? this.shadow : this.unselectedTabShadow);
                graphics.drawLine(i4 + 3, (i5 + i7) - 1, (i4 + i6) - 4, (i5 + i7) - 1);
                graphics.drawLine((i4 + i6) - 3, (i5 + i7) - 2, (i4 + i6) - 3, (i5 + i7) - 1);
                graphics.drawLine((i4 + i6) - 2, (i5 + i7) - 3, (i4 + i6) - 2, (i5 + i7) - 2);
                graphics.drawLine((i4 + i6) - 1, i5, (i4 + i6) - 1, (i5 + i7) - 3);
                break;
            case 4:
                graphics.drawLine(i4, i5, (i4 + i6) - 3, i5);
                graphics.setColor(z2 ? this.shadow : this.unselectedTabShadow);
                graphics.drawLine((i4 + i6) - 3, i5, (i4 + i6) - 3, i5 + 1);
                graphics.drawLine((i4 + i6) - 2, i5 + 1, (i4 + i6) - 2, i5 + 2);
                graphics.drawLine((i4 + i6) - 1, i5 + 2, (i4 + i6) - 1, (i5 + i7) - 3);
                graphics.drawLine((i4 + i6) - 2, (i5 + i7) - 3, (i4 + i6) - 2, (i5 + i7) - 2);
                graphics.drawLine((i4 + i6) - 3, (i5 + i7) - 2, (i4 + i6) - 3, (i5 + i7) - 1);
                graphics.drawLine(i4, (i5 + i7) - 1, (i4 + i6) - 3, (i5 + i7) - 1);
                break;
        }
    }

    @Override // javax.swing.plaf.basic.BasicTabbedPaneUI
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
                    i6 = rectangle3.width - 7;
                    i7 = rectangle3.height - 6;
                    break;
                case 2:
                    i4 = rectangle3.f12372x + 3;
                    i5 = rectangle3.f12373y + 3;
                    i6 = rectangle3.width - 6;
                    i7 = rectangle3.height - 7;
                    break;
                case 3:
                    i4 = rectangle3.f12372x + 3;
                    i5 = rectangle3.f12373y + 2;
                    i6 = rectangle3.width - 7;
                    i7 = rectangle3.height - 6;
                    break;
                case 4:
                    i4 = rectangle3.f12372x + 2;
                    i5 = rectangle3.f12373y + 3;
                    i6 = rectangle3.width - 6;
                    i7 = rectangle3.height - 7;
                    break;
            }
            graphics.drawRect(i4, i5, i6, i7);
        }
    }

    @Override // javax.swing.plaf.basic.BasicTabbedPaneUI
    protected int getTabRunIndent(int i2, int i3) {
        return i3 * 3;
    }

    @Override // javax.swing.plaf.basic.BasicTabbedPaneUI
    protected int getTabRunOverlay(int i2) {
        int iRound;
        if (i2 == 2 || i2 == 4) {
            iRound = (int) Math.round(this.maxTabWidth * 0.1d);
        } else {
            iRound = (int) Math.round(this.maxTabHeight * 0.22d);
        }
        this.tabRunOverlay = iRound;
        switch (i2) {
            case 1:
                if (this.tabRunOverlay > this.tabInsets.bottom - 2) {
                    this.tabRunOverlay = this.tabInsets.bottom - 2;
                    break;
                }
                break;
            case 2:
                if (this.tabRunOverlay > this.tabInsets.right - 2) {
                    this.tabRunOverlay = this.tabInsets.right - 2;
                    break;
                }
                break;
            case 3:
                if (this.tabRunOverlay > this.tabInsets.top - 2) {
                    this.tabRunOverlay = this.tabInsets.top - 2;
                    break;
                }
                break;
            case 4:
                if (this.tabRunOverlay > this.tabInsets.left - 2) {
                    this.tabRunOverlay = this.tabInsets.left - 2;
                    break;
                }
                break;
        }
        return this.tabRunOverlay;
    }
}
