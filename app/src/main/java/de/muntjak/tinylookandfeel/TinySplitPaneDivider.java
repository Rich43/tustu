package de.muntjak.tinylookandfeel;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.Rectangle;
import javax.swing.JButton;
import javax.swing.JSplitPane;
import javax.swing.border.Border;
import javax.swing.plaf.basic.BasicSplitPaneDivider;
import javax.swing.plaf.basic.BasicSplitPaneUI;
import javax.swing.plaf.metal.MetalLookAndFeel;

/* loaded from: tinylaf.jar:de/muntjak/tinylookandfeel/TinySplitPaneDivider.class */
class TinySplitPaneDivider extends BasicSplitPaneDivider {
    private int inset;
    private Color controlColor;

    /* loaded from: tinylaf.jar:de/muntjak/tinylookandfeel/TinySplitPaneDivider$MetalDividerLayout.class */
    public class MetalDividerLayout implements LayoutManager {
        private final TinySplitPaneDivider this$0;

        public MetalDividerLayout(TinySplitPaneDivider tinySplitPaneDivider) {
            this.this$0 = tinySplitPaneDivider;
        }

        @Override // java.awt.LayoutManager
        public void layoutContainer(Container container) {
            JButton leftButtonFromSuper = this.this$0.getLeftButtonFromSuper();
            JButton rightButtonFromSuper = this.this$0.getRightButtonFromSuper();
            JSplitPane splitPaneFromSuper = this.this$0.getSplitPaneFromSuper();
            int orientationFromSuper = this.this$0.getOrientationFromSuper();
            int oneTouchSizeFromSuper = this.this$0.getOneTouchSizeFromSuper();
            int oneTouchOffsetFromSuper = this.this$0.getOneTouchOffsetFromSuper();
            Insets insets = this.this$0.getInsets();
            if (leftButtonFromSuper == null || rightButtonFromSuper == null || container != this.this$0) {
                return;
            }
            if (!splitPaneFromSuper.isOneTouchExpandable()) {
                leftButtonFromSuper.setBounds(-5, -5, 1, 1);
                rightButtonFromSuper.setBounds(-5, -5, 1, 1);
                return;
            }
            if (orientationFromSuper == 0) {
                int i2 = insets != null ? insets.top : 0;
                int dividerSize = this.this$0.getDividerSize();
                if (insets != null) {
                    dividerSize -= insets.top + insets.bottom;
                }
                int iMin = Math.min(dividerSize, oneTouchSizeFromSuper);
                leftButtonFromSuper.setBounds(oneTouchOffsetFromSuper, i2, iMin * 2, iMin);
                rightButtonFromSuper.setBounds(oneTouchOffsetFromSuper + (oneTouchSizeFromSuper * 2), i2, iMin * 2, iMin);
                return;
            }
            int dividerSize2 = this.this$0.getDividerSize();
            int i3 = insets != null ? insets.left : 0;
            if (insets != null) {
                dividerSize2 -= insets.left + insets.right;
            }
            int iMin2 = Math.min(dividerSize2, oneTouchSizeFromSuper);
            leftButtonFromSuper.setBounds(i3, oneTouchOffsetFromSuper, iMin2, iMin2 * 2);
            rightButtonFromSuper.setBounds(i3, oneTouchOffsetFromSuper + (oneTouchSizeFromSuper * 2), iMin2, iMin2 * 2);
        }

        @Override // java.awt.LayoutManager
        public Dimension minimumLayoutSize(Container container) {
            return new Dimension(0, 0);
        }

        @Override // java.awt.LayoutManager
        public Dimension preferredLayoutSize(Container container) {
            return new Dimension(0, 0);
        }

        @Override // java.awt.LayoutManager
        public void removeLayoutComponent(Component component) {
        }

        @Override // java.awt.LayoutManager
        public void addLayoutComponent(String str, Component component) {
        }
    }

    public TinySplitPaneDivider(BasicSplitPaneUI basicSplitPaneUI) {
        super(basicSplitPaneUI);
        this.inset = 2;
        this.controlColor = MetalLookAndFeel.getControl();
        setLayout(new MetalDividerLayout(this));
    }

    @Override // javax.swing.plaf.basic.BasicSplitPaneDivider, java.awt.Container, java.awt.Component
    public void paint(Graphics graphics) {
        graphics.setColor(this.controlColor);
        Rectangle clipBounds = graphics.getClipBounds();
        Insets insets = getInsets();
        graphics.fillRect(clipBounds.f12372x, clipBounds.f12373y, clipBounds.width, clipBounds.height);
        Dimension size = getSize();
        size.width -= this.inset * 2;
        size.height -= this.inset * 2;
        int i2 = this.inset;
        int i3 = this.inset;
        if (insets != null) {
            size.width -= insets.left + insets.right;
            size.height -= insets.top + insets.bottom;
            int i4 = i2 + insets.left;
            int i5 = i3 + insets.top;
        }
        super.paint(graphics);
    }

    @Override // javax.swing.plaf.basic.BasicSplitPaneDivider
    protected JButton createLeftOneTouchButton() {
        JButton jButton = new JButton(this) { // from class: de.muntjak.tinylookandfeel.TinySplitPaneDivider.1
            private final TinySplitPaneDivider this$0;

            {
                this.this$0 = this;
            }

            @Override // javax.swing.JComponent
            public void setBorder(Border border) {
            }

            @Override // javax.swing.JComponent, java.awt.Container, java.awt.Component
            public void paint(Graphics graphics) {
                if (this.this$0.getSplitPaneFromSuper() != null) {
                    this.this$0.getOneTouchSizeFromSuper();
                    int orientationFromSuper = this.this$0.getOrientationFromSuper();
                    graphics.setColor(Theme.backColor.getColor());
                    graphics.fillRect(0, 0, getWidth(), getHeight());
                    graphics.setColor(Theme.splitPaneButtonColor.getColor());
                    if (orientationFromSuper == 0) {
                        graphics.drawLine(2, 1, 3, 1);
                        graphics.drawLine(1, 2, 4, 2);
                        graphics.drawLine(0, 3, 5, 3);
                    } else {
                        graphics.drawLine(1, 2, 1, 3);
                        graphics.drawLine(2, 1, 2, 4);
                        graphics.drawLine(3, 0, 3, 5);
                    }
                }
            }

            @Override // java.awt.Component
            public boolean isFocusTraversable() {
                return false;
            }
        };
        jButton.setRequestFocusEnabled(false);
        jButton.setCursor(Cursor.getPredefinedCursor(0));
        jButton.setFocusPainted(false);
        jButton.setBorderPainted(false);
        return jButton;
    }

    @Override // javax.swing.plaf.basic.BasicSplitPaneDivider
    protected JButton createRightOneTouchButton() {
        JButton jButton = new JButton(this) { // from class: de.muntjak.tinylookandfeel.TinySplitPaneDivider.2
            private final TinySplitPaneDivider this$0;

            {
                this.this$0 = this;
            }

            @Override // javax.swing.JComponent
            public void setBorder(Border border) {
            }

            @Override // javax.swing.JComponent, java.awt.Container, java.awt.Component
            public void paint(Graphics graphics) {
                if (this.this$0.getSplitPaneFromSuper() != null) {
                    this.this$0.getOneTouchSizeFromSuper();
                    int orientationFromSuper = this.this$0.getOrientationFromSuper();
                    graphics.setColor(Theme.backColor.getColor());
                    graphics.fillRect(0, 0, getWidth(), getHeight());
                    graphics.setColor(Theme.splitPaneButtonColor.getColor());
                    if (orientationFromSuper == 0) {
                        graphics.drawLine(2, 3, 3, 3);
                        graphics.drawLine(1, 2, 4, 2);
                        graphics.drawLine(0, 1, 5, 1);
                    } else {
                        graphics.drawLine(3, 2, 3, 3);
                        graphics.drawLine(2, 1, 2, 4);
                        graphics.drawLine(1, 0, 1, 5);
                    }
                }
            }

            @Override // java.awt.Component
            public boolean isFocusTraversable() {
                return false;
            }
        };
        jButton.setCursor(Cursor.getPredefinedCursor(0));
        jButton.setFocusPainted(false);
        jButton.setBorderPainted(false);
        jButton.setRequestFocusEnabled(false);
        return jButton;
    }

    int getOneTouchSizeFromSuper() {
        return 6;
    }

    int getOneTouchOffsetFromSuper() {
        return 2;
    }

    int getOrientationFromSuper() {
        return this.orientation;
    }

    JSplitPane getSplitPaneFromSuper() {
        return this.splitPane;
    }

    JButton getLeftButtonFromSuper() {
        return this.leftButton;
    }

    JButton getRightButtonFromSuper() {
        return this.rightButton;
    }
}
