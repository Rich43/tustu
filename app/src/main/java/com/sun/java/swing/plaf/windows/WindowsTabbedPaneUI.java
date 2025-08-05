package com.sun.java.swing.plaf.windows;

import com.sun.java.swing.plaf.windows.TMSchema;
import com.sun.java.swing.plaf.windows.XPStyle;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Rectangle;
import java.util.HashSet;
import java.util.Set;
import javax.swing.JComponent;
import javax.swing.KeyStroke;
import javax.swing.UIManager;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicTabbedPaneUI;

/* loaded from: rt.jar:com/sun/java/swing/plaf/windows/WindowsTabbedPaneUI.class */
public class WindowsTabbedPaneUI extends BasicTabbedPaneUI {
    private static Set<KeyStroke> managingFocusForwardTraversalKeys;
    private static Set<KeyStroke> managingFocusBackwardTraversalKeys;
    private boolean contentOpaque = true;

    @Override // javax.swing.plaf.basic.BasicTabbedPaneUI
    protected void installDefaults() {
        super.installDefaults();
        this.contentOpaque = UIManager.getBoolean("TabbedPane.contentOpaque");
        if (managingFocusForwardTraversalKeys == null) {
            managingFocusForwardTraversalKeys = new HashSet();
            managingFocusForwardTraversalKeys.add(KeyStroke.getKeyStroke(9, 0));
        }
        this.tabPane.setFocusTraversalKeys(0, managingFocusForwardTraversalKeys);
        if (managingFocusBackwardTraversalKeys == null) {
            managingFocusBackwardTraversalKeys = new HashSet();
            managingFocusBackwardTraversalKeys.add(KeyStroke.getKeyStroke(9, 1));
        }
        this.tabPane.setFocusTraversalKeys(1, managingFocusBackwardTraversalKeys);
    }

    @Override // javax.swing.plaf.basic.BasicTabbedPaneUI
    protected void uninstallDefaults() {
        this.tabPane.setFocusTraversalKeys(0, null);
        this.tabPane.setFocusTraversalKeys(1, null);
        super.uninstallDefaults();
    }

    public static ComponentUI createUI(JComponent jComponent) {
        return new WindowsTabbedPaneUI();
    }

    @Override // javax.swing.plaf.basic.BasicTabbedPaneUI
    protected void setRolloverTab(int i2) {
        if (XPStyle.getXP() != null) {
            int rolloverTab = getRolloverTab();
            super.setRolloverTab(i2);
            Rectangle tabBounds = null;
            Rectangle tabBounds2 = null;
            if (rolloverTab >= 0 && rolloverTab < this.tabPane.getTabCount()) {
                tabBounds = getTabBounds(this.tabPane, rolloverTab);
            }
            if (i2 >= 0) {
                tabBounds2 = getTabBounds(this.tabPane, i2);
            }
            if (tabBounds != null) {
                if (tabBounds2 != null) {
                    this.tabPane.repaint(tabBounds.union(tabBounds2));
                    return;
                } else {
                    this.tabPane.repaint(tabBounds);
                    return;
                }
            }
            if (tabBounds2 != null) {
                this.tabPane.repaint(tabBounds2);
            }
        }
    }

    @Override // javax.swing.plaf.basic.BasicTabbedPaneUI
    protected void paintContentBorder(Graphics graphics, int i2, int i3) {
        XPStyle.Skin skin;
        XPStyle xp = XPStyle.getXP();
        if (xp != null && ((this.contentOpaque || this.tabPane.isOpaque()) && (skin = xp.getSkin(this.tabPane, TMSchema.Part.TABP_PANE)) != null)) {
            Insets insets = this.tabPane.getInsets();
            Insets insets2 = UIManager.getInsets("TabbedPane.tabAreaInsets");
            int i4 = insets.left;
            int i5 = insets.top;
            int width = (this.tabPane.getWidth() - insets.right) - insets.left;
            int height = (this.tabPane.getHeight() - insets.top) - insets.bottom;
            if (i2 == 2 || i2 == 4) {
                int iCalculateTabAreaWidth = calculateTabAreaWidth(i2, this.runCount, this.maxTabWidth);
                if (i2 == 2) {
                    i4 += iCalculateTabAreaWidth - insets2.bottom;
                }
                width -= iCalculateTabAreaWidth - insets2.bottom;
            } else {
                int iCalculateTabAreaHeight = calculateTabAreaHeight(i2, this.runCount, this.maxTabHeight);
                if (i2 == 1) {
                    i5 += iCalculateTabAreaHeight - insets2.bottom;
                }
                height -= iCalculateTabAreaHeight - insets2.bottom;
            }
            paintRotatedSkin(graphics, skin, i2, i4, i5, width, height, null);
            return;
        }
        super.paintContentBorder(graphics, i2, i3);
    }

    @Override // javax.swing.plaf.basic.BasicTabbedPaneUI
    protected void paintTabBackground(Graphics graphics, int i2, int i3, int i4, int i5, int i6, int i7, boolean z2) {
        if (XPStyle.getXP() == null) {
            super.paintTabBackground(graphics, i2, i3, i4, i5, i6, i7, z2);
        }
    }

    @Override // javax.swing.plaf.basic.BasicTabbedPaneUI
    protected void paintTabBorder(Graphics graphics, int i2, int i3, int i4, int i5, int i6, int i7, boolean z2) {
        TMSchema.Part part;
        XPStyle xp = XPStyle.getXP();
        if (xp != null) {
            int tabCount = this.tabPane.getTabCount();
            int runForTab = getRunForTab(tabCount, i3);
            if (this.tabRuns[runForTab] == i3) {
                part = TMSchema.Part.TABP_TABITEMLEFTEDGE;
            } else if (tabCount > 1 && lastTabInRun(tabCount, runForTab) == i3) {
                part = TMSchema.Part.TABP_TABITEMRIGHTEDGE;
                if (z2) {
                    if (i2 == 1 || i2 == 3) {
                        i6++;
                    } else {
                        i7++;
                    }
                }
            } else {
                part = TMSchema.Part.TABP_TABITEM;
            }
            TMSchema.State state = TMSchema.State.NORMAL;
            if (z2) {
                state = TMSchema.State.SELECTED;
            } else if (i3 == getRolloverTab()) {
                state = TMSchema.State.HOT;
            }
            paintRotatedSkin(graphics, xp.getSkin(this.tabPane, part), i2, i4, i5, i6, i7, state);
            return;
        }
        super.paintTabBorder(graphics, i2, i3, i4, i5, i6, i7, z2);
    }

    private void paintRotatedSkin(Graphics graphics, XPStyle.Skin skin, int i2, int i3, int i4, int i5, int i6, TMSchema.State state) {
        Graphics2D graphics2D = (Graphics2D) graphics.create();
        graphics2D.translate(i3, i4);
        switch (i2) {
            case 1:
            default:
                skin.paintSkin(graphics2D, 0, 0, i5, i6, state);
                break;
            case 2:
                graphics2D.scale(-1.0d, 1.0d);
                graphics2D.rotate(Math.toRadians(90.0d));
                skin.paintSkin(graphics2D, 0, 0, i6, i5, state);
                break;
            case 3:
                graphics2D.translate(0, i6);
                graphics2D.scale(-1.0d, 1.0d);
                graphics2D.rotate(Math.toRadians(180.0d));
                skin.paintSkin(graphics2D, 0, 0, i5, i6, state);
                break;
            case 4:
                graphics2D.translate(i5, 0);
                graphics2D.rotate(Math.toRadians(90.0d));
                skin.paintSkin(graphics2D, 0, 0, i6, i5, state);
                break;
        }
        graphics2D.dispose();
    }
}
