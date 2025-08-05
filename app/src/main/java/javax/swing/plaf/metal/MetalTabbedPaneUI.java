package javax.swing.plaf.metal;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.Rectangle;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.UIManager;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.UIResource;
import javax.swing.plaf.basic.BasicTabbedPaneUI;

/* loaded from: rt.jar:javax/swing/plaf/metal/MetalTabbedPaneUI.class */
public class MetalTabbedPaneUI extends BasicTabbedPaneUI {
    private Color unselectedBackground;
    protected Color tabAreaBackground;
    protected Color selectColor;
    protected Color selectHighlight;
    private boolean ocean;
    private Color oceanSelectedBorderColor;
    protected int minTabWidth = 40;
    private boolean tabsOpaque = true;

    public static ComponentUI createUI(JComponent jComponent) {
        return new MetalTabbedPaneUI();
    }

    @Override // javax.swing.plaf.basic.BasicTabbedPaneUI
    protected LayoutManager createLayoutManager() {
        if (this.tabPane.getTabLayoutPolicy() == 1) {
            return super.createLayoutManager();
        }
        return new TabbedPaneLayout();
    }

    @Override // javax.swing.plaf.basic.BasicTabbedPaneUI
    protected void installDefaults() {
        super.installDefaults();
        this.tabAreaBackground = UIManager.getColor("TabbedPane.tabAreaBackground");
        this.selectColor = UIManager.getColor("TabbedPane.selected");
        this.selectHighlight = UIManager.getColor("TabbedPane.selectHighlight");
        this.tabsOpaque = UIManager.getBoolean("TabbedPane.tabsOpaque");
        this.unselectedBackground = UIManager.getColor("TabbedPane.unselectedBackground");
        this.ocean = MetalLookAndFeel.usingOcean();
        if (this.ocean) {
            this.oceanSelectedBorderColor = UIManager.getColor("TabbedPane.borderHightlightColor");
        }
    }

    @Override // javax.swing.plaf.basic.BasicTabbedPaneUI
    protected void paintTabBorder(Graphics graphics, int i2, int i3, int i4, int i5, int i6, int i7, boolean z2) {
        int i8 = i5 + (i7 - 1);
        int i9 = i4 + (i6 - 1);
        switch (i2) {
            case 1:
            default:
                paintTopTabBorder(i3, graphics, i4, i5, i6, i7, i8, i9, z2);
                break;
            case 2:
                paintLeftTabBorder(i3, graphics, i4, i5, i6, i7, i8, i9, z2);
                break;
            case 3:
                paintBottomTabBorder(i3, graphics, i4, i5, i6, i7, i8, i9, z2);
                break;
            case 4:
                paintRightTabBorder(i3, graphics, i4, i5, i6, i7, i8, i9, z2);
                break;
        }
    }

    protected void paintTopTabBorder(int i2, Graphics graphics, int i3, int i4, int i5, int i6, int i7, int i8, boolean z2) {
        int runForTab = getRunForTab(this.tabPane.getTabCount(), i2);
        int iLastTabInRun = lastTabInRun(this.tabPane.getTabCount(), runForTab);
        int i9 = this.tabRuns[runForTab];
        boolean zIsLeftToRight = MetalUtils.isLeftToRight(this.tabPane);
        int selectedIndex = this.tabPane.getSelectedIndex();
        int i10 = i6 - 1;
        int i11 = i5 - 1;
        if (shouldFillGap(runForTab, i2, i3, i4)) {
            graphics.translate(i3, i4);
            if (zIsLeftToRight) {
                graphics.setColor(getColorForGap(runForTab, i3, i4 + 1));
                graphics.fillRect(1, 0, 5, 3);
                graphics.fillRect(1, 3, 2, 2);
            } else {
                graphics.setColor(getColorForGap(runForTab, (i3 + i5) - 1, i4 + 1));
                graphics.fillRect(i11 - 5, 0, 5, 3);
                graphics.fillRect(i11 - 2, 3, 2, 2);
            }
            graphics.translate(-i3, -i4);
        }
        graphics.translate(i3, i4);
        if (this.ocean && z2) {
            graphics.setColor(this.oceanSelectedBorderColor);
        } else {
            graphics.setColor(this.darkShadow);
        }
        if (zIsLeftToRight) {
            graphics.drawLine(1, 5, 6, 0);
            graphics.drawLine(6, 0, i11, 0);
            if (i2 == iLastTabInRun) {
                graphics.drawLine(i11, 1, i11, i10);
            }
            if (this.ocean && i2 - 1 == selectedIndex && runForTab == getRunForTab(this.tabPane.getTabCount(), selectedIndex)) {
                graphics.setColor(this.oceanSelectedBorderColor);
            }
            if (i2 != this.tabRuns[this.runCount - 1]) {
                if (this.ocean && z2) {
                    graphics.drawLine(0, 6, 0, i10);
                    graphics.setColor(this.darkShadow);
                    graphics.drawLine(0, 0, 0, 5);
                } else {
                    graphics.drawLine(0, 0, 0, i10);
                }
            } else {
                graphics.drawLine(0, 6, 0, i10);
            }
        } else {
            graphics.drawLine(i11 - 1, 5, i11 - 6, 0);
            graphics.drawLine(i11 - 6, 0, 0, 0);
            if (i2 == iLastTabInRun) {
                graphics.drawLine(0, 1, 0, i10);
            }
            if (this.ocean && i2 - 1 == selectedIndex && runForTab == getRunForTab(this.tabPane.getTabCount(), selectedIndex)) {
                graphics.setColor(this.oceanSelectedBorderColor);
                graphics.drawLine(i11, 0, i11, i10);
            } else if (this.ocean && z2) {
                graphics.drawLine(i11, 6, i11, i10);
                if (i2 != 0) {
                    graphics.setColor(this.darkShadow);
                    graphics.drawLine(i11, 0, i11, 5);
                }
            } else if (i2 != this.tabRuns[this.runCount - 1]) {
                graphics.drawLine(i11, 0, i11, i10);
            } else {
                graphics.drawLine(i11, 6, i11, i10);
            }
        }
        graphics.setColor(z2 ? this.selectHighlight : this.highlight);
        if (zIsLeftToRight) {
            graphics.drawLine(1, 6, 6, 1);
            graphics.drawLine(6, 1, i2 == iLastTabInRun ? i11 - 1 : i11, 1);
            graphics.drawLine(1, 6, 1, i10);
            if (i2 == i9 && i2 != this.tabRuns[this.runCount - 1]) {
                if (this.tabPane.getSelectedIndex() == this.tabRuns[runForTab + 1]) {
                    graphics.setColor(this.selectHighlight);
                } else {
                    graphics.setColor(this.highlight);
                }
                graphics.drawLine(1, 0, 1, 4);
            }
        } else {
            graphics.drawLine(i11 - 1, 6, i11 - 6, 1);
            graphics.drawLine(i11 - 6, 1, 1, 1);
            if (i2 == iLastTabInRun) {
                graphics.drawLine(1, 1, 1, i10);
            } else {
                graphics.drawLine(0, 1, 0, i10);
            }
        }
        graphics.translate(-i3, -i4);
    }

    protected boolean shouldFillGap(int i2, int i3, int i4, int i5) {
        boolean z2 = false;
        if (!this.tabsOpaque) {
            return false;
        }
        if (i2 == this.runCount - 2) {
            Rectangle tabBounds = getTabBounds(this.tabPane, this.tabPane.getTabCount() - 1);
            Rectangle tabBounds2 = getTabBounds(this.tabPane, i3);
            if (MetalUtils.isLeftToRight(this.tabPane)) {
                if ((tabBounds.f12372x + tabBounds.width) - 1 > tabBounds2.f12372x + 2) {
                    return true;
                }
            } else if (tabBounds.f12372x < ((tabBounds2.f12372x + tabBounds2.width) - 1) - 2) {
                return true;
            }
        } else {
            z2 = i2 != this.runCount - 1;
        }
        return z2;
    }

    protected Color getColorForGap(int i2, int i3, int i4) {
        int selectedIndex = this.tabPane.getSelectedIndex();
        int i5 = this.tabRuns[i2 + 1];
        int iLastTabInRun = lastTabInRun(this.tabPane.getTabCount(), i2 + 1);
        int i6 = i5;
        while (i6 <= iLastTabInRun) {
            Rectangle tabBounds = getTabBounds(this.tabPane, i6);
            int i7 = tabBounds.f12372x;
            int i8 = (tabBounds.f12372x + tabBounds.width) - 1;
            if (MetalUtils.isLeftToRight(this.tabPane)) {
                if (i7 <= i3 && i8 - 4 > i3) {
                    return selectedIndex == i6 ? this.selectColor : getUnselectedBackgroundAt(i6);
                }
            } else if (i7 + 4 < i3 && i8 >= i3) {
                return selectedIndex == i6 ? this.selectColor : getUnselectedBackgroundAt(i6);
            }
            i6++;
        }
        return this.tabPane.getBackground();
    }

    protected void paintLeftTabBorder(int i2, Graphics graphics, int i3, int i4, int i5, int i6, int i7, int i8, boolean z2) {
        int tabCount = this.tabPane.getTabCount();
        int runForTab = getRunForTab(tabCount, i2);
        int iLastTabInRun = lastTabInRun(tabCount, runForTab);
        int i9 = this.tabRuns[runForTab];
        graphics.translate(i3, i4);
        int i10 = i6 - 1;
        int i11 = i5 - 1;
        if (i2 != i9 && this.tabsOpaque) {
            graphics.setColor(this.tabPane.getSelectedIndex() == i2 - 1 ? this.selectColor : getUnselectedBackgroundAt(i2 - 1));
            graphics.fillRect(2, 0, 4, 3);
            graphics.drawLine(2, 3, 2, 3);
        }
        if (this.ocean) {
            graphics.setColor(z2 ? this.selectHighlight : MetalLookAndFeel.getWhite());
        } else {
            graphics.setColor(z2 ? this.selectHighlight : this.highlight);
        }
        graphics.drawLine(1, 6, 6, 1);
        graphics.drawLine(1, 6, 1, i10);
        graphics.drawLine(6, 1, i11, 1);
        if (i2 != i9) {
            if (this.tabPane.getSelectedIndex() == i2 - 1) {
                graphics.setColor(this.selectHighlight);
            } else {
                graphics.setColor(this.ocean ? MetalLookAndFeel.getWhite() : this.highlight);
            }
            graphics.drawLine(1, 0, 1, 4);
        }
        if (this.ocean && z2) {
            graphics.setColor(this.oceanSelectedBorderColor);
        } else {
            graphics.setColor(this.darkShadow);
        }
        graphics.drawLine(1, 5, 6, 0);
        graphics.drawLine(6, 0, i11, 0);
        if (i2 == iLastTabInRun) {
            graphics.drawLine(0, i10, i11, i10);
        }
        if (this.ocean) {
            if (this.tabPane.getSelectedIndex() == i2 - 1) {
                graphics.drawLine(0, 5, 0, i10);
                graphics.setColor(this.oceanSelectedBorderColor);
                graphics.drawLine(0, 0, 0, 5);
            } else if (z2) {
                graphics.drawLine(0, 6, 0, i10);
                if (i2 != 0) {
                    graphics.setColor(this.darkShadow);
                    graphics.drawLine(0, 0, 0, 5);
                }
            } else if (i2 != i9) {
                graphics.drawLine(0, 0, 0, i10);
            } else {
                graphics.drawLine(0, 6, 0, i10);
            }
        } else if (i2 != i9) {
            graphics.drawLine(0, 0, 0, i10);
        } else {
            graphics.drawLine(0, 6, 0, i10);
        }
        graphics.translate(-i3, -i4);
    }

    protected void paintBottomTabBorder(int i2, Graphics graphics, int i3, int i4, int i5, int i6, int i7, int i8, boolean z2) {
        int tabCount = this.tabPane.getTabCount();
        int runForTab = getRunForTab(tabCount, i2);
        int iLastTabInRun = lastTabInRun(tabCount, runForTab);
        int i9 = this.tabRuns[runForTab];
        boolean zIsLeftToRight = MetalUtils.isLeftToRight(this.tabPane);
        int i10 = i6 - 1;
        int i11 = i5 - 1;
        if (shouldFillGap(runForTab, i2, i3, i4)) {
            graphics.translate(i3, i4);
            if (zIsLeftToRight) {
                graphics.setColor(getColorForGap(runForTab, i3, i4));
                graphics.fillRect(1, i10 - 4, 3, 5);
                graphics.fillRect(4, i10 - 1, 2, 2);
            } else {
                graphics.setColor(getColorForGap(runForTab, (i3 + i5) - 1, i4));
                graphics.fillRect(i11 - 3, i10 - 3, 3, 4);
                graphics.fillRect(i11 - 5, i10 - 1, 2, 2);
                graphics.drawLine(i11 - 1, i10 - 4, i11 - 1, i10 - 4);
            }
            graphics.translate(-i3, -i4);
        }
        graphics.translate(i3, i4);
        if (this.ocean && z2) {
            graphics.setColor(this.oceanSelectedBorderColor);
        } else {
            graphics.setColor(this.darkShadow);
        }
        if (zIsLeftToRight) {
            graphics.drawLine(1, i10 - 5, 6, i10);
            graphics.drawLine(6, i10, i11, i10);
            if (i2 == iLastTabInRun) {
                graphics.drawLine(i11, 0, i11, i10);
            }
            if (this.ocean && z2) {
                graphics.drawLine(0, 0, 0, i10 - 6);
                if ((runForTab == 0 && i2 != 0) || (runForTab > 0 && i2 != this.tabRuns[runForTab - 1])) {
                    graphics.setColor(this.darkShadow);
                    graphics.drawLine(0, i10 - 5, 0, i10);
                }
            } else {
                if (this.ocean && i2 == this.tabPane.getSelectedIndex() + 1) {
                    graphics.setColor(this.oceanSelectedBorderColor);
                }
                if (i2 != this.tabRuns[this.runCount - 1]) {
                    graphics.drawLine(0, 0, 0, i10);
                } else {
                    graphics.drawLine(0, 0, 0, i10 - 6);
                }
            }
        } else {
            graphics.drawLine(i11 - 1, i10 - 5, i11 - 6, i10);
            graphics.drawLine(i11 - 6, i10, 0, i10);
            if (i2 == iLastTabInRun) {
                graphics.drawLine(0, 0, 0, i10);
            }
            if (this.ocean && i2 == this.tabPane.getSelectedIndex() + 1) {
                graphics.setColor(this.oceanSelectedBorderColor);
                graphics.drawLine(i11, 0, i11, i10);
            } else if (this.ocean && z2) {
                graphics.drawLine(i11, 0, i11, i10 - 6);
                if (i2 != i9) {
                    graphics.setColor(this.darkShadow);
                    graphics.drawLine(i11, i10 - 5, i11, i10);
                }
            } else if (i2 != this.tabRuns[this.runCount - 1]) {
                graphics.drawLine(i11, 0, i11, i10);
            } else {
                graphics.drawLine(i11, 0, i11, i10 - 6);
            }
        }
        graphics.setColor(z2 ? this.selectHighlight : this.highlight);
        if (zIsLeftToRight) {
            graphics.drawLine(1, i10 - 6, 6, i10 - 1);
            graphics.drawLine(1, 0, 1, i10 - 6);
            if (i2 == i9 && i2 != this.tabRuns[this.runCount - 1]) {
                if (this.tabPane.getSelectedIndex() == this.tabRuns[runForTab + 1]) {
                    graphics.setColor(this.selectHighlight);
                } else {
                    graphics.setColor(this.highlight);
                }
                graphics.drawLine(1, i10 - 4, 1, i10);
            }
        } else if (i2 == iLastTabInRun) {
            graphics.drawLine(1, 0, 1, i10 - 1);
        } else {
            graphics.drawLine(0, 0, 0, i10 - 1);
        }
        graphics.translate(-i3, -i4);
    }

    protected void paintRightTabBorder(int i2, Graphics graphics, int i3, int i4, int i5, int i6, int i7, int i8, boolean z2) {
        int tabCount = this.tabPane.getTabCount();
        int runForTab = getRunForTab(tabCount, i2);
        int iLastTabInRun = lastTabInRun(tabCount, runForTab);
        int i9 = this.tabRuns[runForTab];
        graphics.translate(i3, i4);
        int i10 = i6 - 1;
        int i11 = i5 - 1;
        if (i2 != i9 && this.tabsOpaque) {
            graphics.setColor(this.tabPane.getSelectedIndex() == i2 - 1 ? this.selectColor : getUnselectedBackgroundAt(i2 - 1));
            graphics.fillRect(i11 - 5, 0, 5, 3);
            graphics.fillRect(i11 - 2, 3, 2, 2);
        }
        graphics.setColor(z2 ? this.selectHighlight : this.highlight);
        graphics.drawLine(i11 - 6, 1, i11 - 1, 6);
        graphics.drawLine(0, 1, i11 - 6, 1);
        if (!z2) {
            graphics.drawLine(0, 1, 0, i10);
        }
        if (this.ocean && z2) {
            graphics.setColor(this.oceanSelectedBorderColor);
        } else {
            graphics.setColor(this.darkShadow);
        }
        if (i2 == iLastTabInRun) {
            graphics.drawLine(0, i10, i11, i10);
        }
        if (this.ocean && this.tabPane.getSelectedIndex() == i2 - 1) {
            graphics.setColor(this.oceanSelectedBorderColor);
        }
        graphics.drawLine(i11 - 6, 0, i11, 6);
        graphics.drawLine(0, 0, i11 - 6, 0);
        if (this.ocean && z2) {
            graphics.drawLine(i11, 6, i11, i10);
            if (i2 != i9) {
                graphics.setColor(this.darkShadow);
                graphics.drawLine(i11, 0, i11, 5);
            }
        } else if (this.ocean && this.tabPane.getSelectedIndex() == i2 - 1) {
            graphics.setColor(this.oceanSelectedBorderColor);
            graphics.drawLine(i11, 0, i11, 6);
            graphics.setColor(this.darkShadow);
            graphics.drawLine(i11, 6, i11, i10);
        } else if (i2 != i9) {
            graphics.drawLine(i11, 0, i11, i10);
        } else {
            graphics.drawLine(i11, 6, i11, i10);
        }
        graphics.translate(-i3, -i4);
    }

    @Override // javax.swing.plaf.ComponentUI
    public void update(Graphics graphics, JComponent jComponent) {
        if (jComponent.isOpaque()) {
            graphics.setColor(this.tabAreaBackground);
            graphics.fillRect(0, 0, jComponent.getWidth(), jComponent.getHeight());
        }
        paint(graphics, jComponent);
    }

    @Override // javax.swing.plaf.basic.BasicTabbedPaneUI
    protected void paintTabBackground(Graphics graphics, int i2, int i3, int i4, int i5, int i6, int i7, boolean z2) {
        int i8 = i7 / 2;
        if (z2) {
            graphics.setColor(this.selectColor);
        } else {
            graphics.setColor(getUnselectedBackgroundAt(i3));
        }
        if (MetalUtils.isLeftToRight(this.tabPane)) {
            switch (i2) {
                case 1:
                default:
                    graphics.fillRect(i4 + 4, i5 + 2, (i6 - 1) - 3, (i7 - 1) - 1);
                    graphics.fillRect(i4 + 2, i5 + 5, 2, i7 - 5);
                    break;
                case 2:
                    graphics.fillRect(i4 + 5, i5 + 1, i6 - 5, i7 - 1);
                    graphics.fillRect(i4 + 2, i5 + 4, 3, i7 - 4);
                    break;
                case 3:
                    graphics.fillRect(i4 + 2, i5, i6 - 2, i7 - 4);
                    graphics.fillRect(i4 + 5, (i5 + (i7 - 1)) - 3, i6 - 5, 3);
                    break;
                case 4:
                    graphics.fillRect(i4, i5 + 2, i6 - 4, i7 - 2);
                    graphics.fillRect((i4 + (i6 - 1)) - 3, i5 + 5, 3, i7 - 5);
                    break;
            }
        }
        switch (i2) {
            case 1:
            default:
                graphics.fillRect(i4, i5 + 2, (i6 - 1) - 3, (i7 - 1) - 1);
                graphics.fillRect((i4 + (i6 - 1)) - 3, i5 + 5, 3, i7 - 3);
                break;
            case 2:
                graphics.fillRect(i4 + 5, i5 + 1, i6 - 5, i7 - 1);
                graphics.fillRect(i4 + 2, i5 + 4, 3, i7 - 4);
                break;
            case 3:
                graphics.fillRect(i4, i5, i6 - 5, i7 - 1);
                graphics.fillRect((i4 + (i6 - 1)) - 4, i5, 4, i7 - 5);
                graphics.fillRect((i4 + (i6 - 1)) - 4, (i5 + (i7 - 1)) - 4, 2, 2);
                break;
            case 4:
                graphics.fillRect(i4 + 1, i5 + 1, i6 - 5, i7 - 1);
                graphics.fillRect((i4 + (i6 - 1)) - 3, i5 + 5, 3, i7 - 5);
                break;
        }
    }

    @Override // javax.swing.plaf.basic.BasicTabbedPaneUI
    protected int getTabLabelShiftX(int i2, int i3, boolean z2) {
        return 0;
    }

    @Override // javax.swing.plaf.basic.BasicTabbedPaneUI
    protected int getTabLabelShiftY(int i2, int i3, boolean z2) {
        return 0;
    }

    @Override // javax.swing.plaf.basic.BasicTabbedPaneUI
    protected int getBaselineOffset() {
        return 0;
    }

    @Override // javax.swing.plaf.basic.BasicTabbedPaneUI, javax.swing.plaf.ComponentUI
    public void paint(Graphics graphics, JComponent jComponent) {
        int tabPlacement = this.tabPane.getTabPlacement();
        Insets insets = jComponent.getInsets();
        Dimension size = jComponent.getSize();
        if (this.tabPane.isOpaque()) {
            Color background = jComponent.getBackground();
            if ((background instanceof UIResource) && this.tabAreaBackground != null) {
                graphics.setColor(this.tabAreaBackground);
            } else {
                graphics.setColor(background);
            }
            switch (tabPlacement) {
                case 1:
                default:
                    graphics.fillRect(insets.left, insets.top, (size.width - insets.right) - insets.left, calculateTabAreaHeight(tabPlacement, this.runCount, this.maxTabHeight));
                    paintHighlightBelowTab();
                    break;
                case 2:
                    graphics.fillRect(insets.left, insets.top, calculateTabAreaWidth(tabPlacement, this.runCount, this.maxTabWidth), (size.height - insets.bottom) - insets.top);
                    break;
                case 3:
                    int iCalculateTabAreaHeight = calculateTabAreaHeight(tabPlacement, this.runCount, this.maxTabHeight);
                    graphics.fillRect(insets.left, (size.height - insets.bottom) - iCalculateTabAreaHeight, (size.width - insets.left) - insets.right, iCalculateTabAreaHeight);
                    break;
                case 4:
                    int iCalculateTabAreaWidth = calculateTabAreaWidth(tabPlacement, this.runCount, this.maxTabWidth);
                    graphics.fillRect((size.width - insets.right) - iCalculateTabAreaWidth, insets.top, iCalculateTabAreaWidth, (size.height - insets.top) - insets.bottom);
                    break;
            }
        }
        super.paint(graphics, jComponent);
    }

    protected void paintHighlightBelowTab() {
    }

    @Override // javax.swing.plaf.basic.BasicTabbedPaneUI
    protected void paintFocusIndicator(Graphics graphics, int i2, Rectangle[] rectangleArr, int i3, Rectangle rectangle, Rectangle rectangle2, boolean z2) {
        if (this.tabPane.hasFocus() && z2) {
            Rectangle rectangle3 = rectangleArr[i3];
            boolean zIsLastInRun = isLastInRun(i3);
            graphics.setColor(this.focus);
            graphics.translate(rectangle3.f12372x, rectangle3.f12373y);
            int i4 = rectangle3.width - 1;
            int i5 = rectangle3.height - 1;
            boolean zIsLeftToRight = MetalUtils.isLeftToRight(this.tabPane);
            switch (i2) {
                case 1:
                default:
                    if (zIsLeftToRight) {
                        graphics.drawLine(2, 6, 6, 2);
                        graphics.drawLine(2, 6, 2, i5 - 1);
                        graphics.drawLine(6, 2, i4, 2);
                        graphics.drawLine(i4, 2, i4, i5 - 1);
                        graphics.drawLine(2, i5 - 1, i4, i5 - 1);
                        break;
                    } else {
                        graphics.drawLine(i4 - 2, 6, i4 - 6, 2);
                        graphics.drawLine(i4 - 2, 6, i4 - 2, i5 - 1);
                        if (zIsLastInRun) {
                            graphics.drawLine(i4 - 6, 2, 2, 2);
                            graphics.drawLine(2, 2, 2, i5 - 1);
                            graphics.drawLine(i4 - 2, i5 - 1, 2, i5 - 1);
                            break;
                        } else {
                            graphics.drawLine(i4 - 6, 2, 1, 2);
                            graphics.drawLine(1, 2, 1, i5 - 1);
                            graphics.drawLine(i4 - 2, i5 - 1, 1, i5 - 1);
                            break;
                        }
                    }
                case 2:
                    graphics.drawLine(2, 6, 6, 2);
                    graphics.drawLine(2, 6, 2, i5 - 1);
                    graphics.drawLine(6, 2, i4, 2);
                    graphics.drawLine(i4, 2, i4, i5 - 1);
                    graphics.drawLine(2, i5 - 1, i4, i5 - 1);
                    break;
                case 3:
                    if (zIsLeftToRight) {
                        graphics.drawLine(2, i5 - 6, 6, i5 - 2);
                        graphics.drawLine(6, i5 - 2, i4, i5 - 2);
                        graphics.drawLine(2, 0, 2, i5 - 6);
                        graphics.drawLine(2, 0, i4, 0);
                        graphics.drawLine(i4, 0, i4, i5 - 2);
                        break;
                    } else {
                        graphics.drawLine(i4 - 2, i5 - 6, i4 - 6, i5 - 2);
                        graphics.drawLine(i4 - 2, 0, i4 - 2, i5 - 6);
                        if (zIsLastInRun) {
                            graphics.drawLine(2, i5 - 2, i4 - 6, i5 - 2);
                            graphics.drawLine(2, 0, i4 - 2, 0);
                            graphics.drawLine(2, 0, 2, i5 - 2);
                            break;
                        } else {
                            graphics.drawLine(1, i5 - 2, i4 - 6, i5 - 2);
                            graphics.drawLine(1, 0, i4 - 2, 0);
                            graphics.drawLine(1, 0, 1, i5 - 2);
                            break;
                        }
                    }
                case 4:
                    graphics.drawLine(i4 - 6, 2, i4 - 2, 6);
                    graphics.drawLine(1, 2, i4 - 6, 2);
                    graphics.drawLine(i4 - 2, 6, i4 - 2, i5);
                    graphics.drawLine(1, 2, 1, i5);
                    graphics.drawLine(1, i5, i4 - 2, i5);
                    break;
            }
            graphics.translate(-rectangle3.f12372x, -rectangle3.f12373y);
        }
    }

    @Override // javax.swing.plaf.basic.BasicTabbedPaneUI
    protected void paintContentBorderTopEdge(Graphics graphics, int i2, int i3, int i4, int i5, int i6, int i7) {
        boolean zIsLeftToRight = MetalUtils.isLeftToRight(this.tabPane);
        int i8 = (i4 + i6) - 1;
        Rectangle tabBounds = i3 < 0 ? null : getTabBounds(i3, this.calcRect);
        if (this.ocean) {
            graphics.setColor(this.oceanSelectedBorderColor);
        } else {
            graphics.setColor(this.selectHighlight);
        }
        if (i2 != 1 || i3 < 0 || tabBounds.f12373y + tabBounds.height + 1 < i5 || tabBounds.f12372x < i4 || tabBounds.f12372x > i4 + i6) {
            graphics.drawLine(i4, i5, (i4 + i6) - 2, i5);
            if (this.ocean && i2 == 1) {
                graphics.setColor(MetalLookAndFeel.getWhite());
                graphics.drawLine(i4, i5 + 1, (i4 + i6) - 2, i5 + 1);
                return;
            }
            return;
        }
        boolean zIsLastInRun = isLastInRun(i3);
        if (zIsLeftToRight || zIsLastInRun) {
            graphics.drawLine(i4, i5, tabBounds.f12372x + 1, i5);
        } else {
            graphics.drawLine(i4, i5, tabBounds.f12372x, i5);
        }
        if (tabBounds.f12372x + tabBounds.width < i8 - 1) {
            if (zIsLeftToRight && !zIsLastInRun) {
                graphics.drawLine(tabBounds.f12372x + tabBounds.width, i5, i8 - 1, i5);
            } else {
                graphics.drawLine((tabBounds.f12372x + tabBounds.width) - 1, i5, i8 - 1, i5);
            }
        } else {
            graphics.setColor(this.shadow);
            graphics.drawLine((i4 + i6) - 2, i5, (i4 + i6) - 2, i5);
        }
        if (this.ocean) {
            graphics.setColor(MetalLookAndFeel.getWhite());
            if (zIsLeftToRight || zIsLastInRun) {
                graphics.drawLine(i4, i5 + 1, tabBounds.f12372x + 1, i5 + 1);
            } else {
                graphics.drawLine(i4, i5 + 1, tabBounds.f12372x, i5 + 1);
            }
            if (tabBounds.f12372x + tabBounds.width < i8 - 1) {
                if (zIsLeftToRight && !zIsLastInRun) {
                    graphics.drawLine(tabBounds.f12372x + tabBounds.width, i5 + 1, i8 - 1, i5 + 1);
                    return;
                } else {
                    graphics.drawLine((tabBounds.f12372x + tabBounds.width) - 1, i5 + 1, i8 - 1, i5 + 1);
                    return;
                }
            }
            graphics.setColor(this.shadow);
            graphics.drawLine((i4 + i6) - 2, i5 + 1, (i4 + i6) - 2, i5 + 1);
        }
    }

    @Override // javax.swing.plaf.basic.BasicTabbedPaneUI
    protected void paintContentBorderBottomEdge(Graphics graphics, int i2, int i3, int i4, int i5, int i6, int i7) {
        boolean zIsLeftToRight = MetalUtils.isLeftToRight(this.tabPane);
        int i8 = (i5 + i7) - 1;
        int i9 = (i4 + i6) - 1;
        Rectangle tabBounds = i3 < 0 ? null : getTabBounds(i3, this.calcRect);
        graphics.setColor(this.darkShadow);
        if (i2 != 3 || i3 < 0 || tabBounds.f12373y - 1 > i7 || tabBounds.f12372x < i4 || tabBounds.f12372x > i4 + i6) {
            if (this.ocean && i2 == 3) {
                graphics.setColor(this.oceanSelectedBorderColor);
            }
            graphics.drawLine(i4, (i5 + i7) - 1, (i4 + i6) - 1, (i5 + i7) - 1);
            return;
        }
        boolean zIsLastInRun = isLastInRun(i3);
        if (this.ocean) {
            graphics.setColor(this.oceanSelectedBorderColor);
        }
        if (zIsLeftToRight || zIsLastInRun) {
            graphics.drawLine(i4, i8, tabBounds.f12372x, i8);
        } else {
            graphics.drawLine(i4, i8, tabBounds.f12372x - 1, i8);
        }
        if (tabBounds.f12372x + tabBounds.width < (i4 + i6) - 2) {
            if (zIsLeftToRight && !zIsLastInRun) {
                graphics.drawLine(tabBounds.f12372x + tabBounds.width, i8, i9, i8);
            } else {
                graphics.drawLine((tabBounds.f12372x + tabBounds.width) - 1, i8, i9, i8);
            }
        }
    }

    @Override // javax.swing.plaf.basic.BasicTabbedPaneUI
    protected void paintContentBorderLeftEdge(Graphics graphics, int i2, int i3, int i4, int i5, int i6, int i7) {
        Rectangle tabBounds = i3 < 0 ? null : getTabBounds(i3, this.calcRect);
        if (this.ocean) {
            graphics.setColor(this.oceanSelectedBorderColor);
        } else {
            graphics.setColor(this.selectHighlight);
        }
        if (i2 != 2 || i3 < 0 || tabBounds.f12372x + tabBounds.width + 1 < i4 || tabBounds.f12373y < i5 || tabBounds.f12373y > i5 + i7) {
            graphics.drawLine(i4, i5 + 1, i4, (i5 + i7) - 2);
            if (this.ocean && i2 == 2) {
                graphics.setColor(MetalLookAndFeel.getWhite());
                graphics.drawLine(i4 + 1, i5, i4 + 1, (i5 + i7) - 2);
                return;
            }
            return;
        }
        graphics.drawLine(i4, i5, i4, tabBounds.f12373y + 1);
        if (tabBounds.f12373y + tabBounds.height < (i5 + i7) - 2) {
            graphics.drawLine(i4, tabBounds.f12373y + tabBounds.height + 1, i4, i5 + i7 + 2);
        }
        if (this.ocean) {
            graphics.setColor(MetalLookAndFeel.getWhite());
            graphics.drawLine(i4 + 1, i5 + 1, i4 + 1, tabBounds.f12373y + 1);
            if (tabBounds.f12373y + tabBounds.height < (i5 + i7) - 2) {
                graphics.drawLine(i4 + 1, tabBounds.f12373y + tabBounds.height + 1, i4 + 1, i5 + i7 + 2);
            }
        }
    }

    @Override // javax.swing.plaf.basic.BasicTabbedPaneUI
    protected void paintContentBorderRightEdge(Graphics graphics, int i2, int i3, int i4, int i5, int i6, int i7) {
        Rectangle tabBounds = i3 < 0 ? null : getTabBounds(i3, this.calcRect);
        graphics.setColor(this.darkShadow);
        if (i2 != 4 || i3 < 0 || tabBounds.f12372x - 1 > i6 || tabBounds.f12373y < i5 || tabBounds.f12373y > i5 + i7) {
            if (this.ocean && i2 == 4) {
                graphics.setColor(this.oceanSelectedBorderColor);
            }
            graphics.drawLine((i4 + i6) - 1, i5, (i4 + i6) - 1, (i5 + i7) - 1);
            return;
        }
        if (this.ocean) {
            graphics.setColor(this.oceanSelectedBorderColor);
        }
        graphics.drawLine((i4 + i6) - 1, i5, (i4 + i6) - 1, tabBounds.f12373y);
        if (tabBounds.f12373y + tabBounds.height < (i5 + i7) - 2) {
            graphics.drawLine((i4 + i6) - 1, tabBounds.f12373y + tabBounds.height, (i4 + i6) - 1, (i5 + i7) - 2);
        }
    }

    @Override // javax.swing.plaf.basic.BasicTabbedPaneUI
    protected int calculateMaxTabHeight(int i2) {
        int height = getFontMetrics().getHeight();
        boolean z2 = false;
        int i3 = 0;
        while (true) {
            if (i3 < this.tabPane.getTabCount()) {
                Icon iconAt = this.tabPane.getIconAt(i3);
                if (iconAt == null || iconAt.getIconHeight() <= height) {
                    i3++;
                } else {
                    z2 = true;
                    break;
                }
            } else {
                break;
            }
        }
        return super.calculateMaxTabHeight(i2) - (z2 ? this.tabInsets.top + this.tabInsets.bottom : 0);
    }

    @Override // javax.swing.plaf.basic.BasicTabbedPaneUI
    protected int getTabRunOverlay(int i2) {
        if (i2 == 2 || i2 == 4) {
            return calculateMaxTabHeight(i2) / 2;
        }
        return 0;
    }

    protected boolean shouldRotateTabRuns(int i2, int i3) {
        return false;
    }

    @Override // javax.swing.plaf.basic.BasicTabbedPaneUI
    protected boolean shouldPadTabRun(int i2, int i3) {
        return this.runCount > 1 && i3 < this.runCount - 1;
    }

    private boolean isLastInRun(int i2) {
        return i2 == lastTabInRun(this.tabPane.getTabCount(), getRunForTab(this.tabPane.getTabCount(), i2));
    }

    private Color getUnselectedBackgroundAt(int i2) {
        Color backgroundAt = this.tabPane.getBackgroundAt(i2);
        if ((backgroundAt instanceof UIResource) && this.unselectedBackground != null) {
            return this.unselectedBackground;
        }
        return backgroundAt;
    }

    int getRolloverTabIndex() {
        return getRolloverTab();
    }

    /* loaded from: rt.jar:javax/swing/plaf/metal/MetalTabbedPaneUI$TabbedPaneLayout.class */
    public class TabbedPaneLayout extends BasicTabbedPaneUI.TabbedPaneLayout {
        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public TabbedPaneLayout() {
            super();
            MetalTabbedPaneUI.this.getClass();
        }

        @Override // javax.swing.plaf.basic.BasicTabbedPaneUI.TabbedPaneLayout
        protected void normalizeTabRuns(int i2, int i3, int i4, int i5) {
            if (i2 == 1 || i2 == 3) {
                super.normalizeTabRuns(i2, i3, i4, i5);
            }
        }

        @Override // javax.swing.plaf.basic.BasicTabbedPaneUI.TabbedPaneLayout
        protected void rotateTabRuns(int i2, int i3) {
        }

        @Override // javax.swing.plaf.basic.BasicTabbedPaneUI.TabbedPaneLayout
        protected void padSelectedTab(int i2, int i3) {
        }
    }
}
