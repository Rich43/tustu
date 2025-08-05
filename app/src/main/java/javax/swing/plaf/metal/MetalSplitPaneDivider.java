package javax.swing.plaf.metal;

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
import javax.swing.JComponent;
import javax.swing.JSplitPane;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.plaf.basic.BasicSplitPaneDivider;
import javax.swing.plaf.basic.BasicSplitPaneUI;

/* loaded from: rt.jar:javax/swing/plaf/metal/MetalSplitPaneDivider.class */
class MetalSplitPaneDivider extends BasicSplitPaneDivider {
    private MetalBumps bumps;
    private MetalBumps focusBumps;
    private int inset;
    private Color controlColor;
    private Color primaryControlColor;

    public MetalSplitPaneDivider(BasicSplitPaneUI basicSplitPaneUI) {
        super(basicSplitPaneUI);
        this.bumps = new MetalBumps(10, 10, MetalLookAndFeel.getControlHighlight(), MetalLookAndFeel.getControlDarkShadow(), MetalLookAndFeel.getControl());
        this.focusBumps = new MetalBumps(10, 10, MetalLookAndFeel.getPrimaryControlHighlight(), MetalLookAndFeel.getPrimaryControlDarkShadow(), UIManager.getColor("SplitPane.dividerFocusColor"));
        this.inset = 2;
        this.controlColor = MetalLookAndFeel.getControl();
        this.primaryControlColor = UIManager.getColor("SplitPane.dividerFocusColor");
    }

    @Override // javax.swing.plaf.basic.BasicSplitPaneDivider, java.awt.Container, java.awt.Component
    public void paint(Graphics graphics) {
        MetalBumps metalBumps;
        if (this.splitPane.hasFocus()) {
            metalBumps = this.focusBumps;
            graphics.setColor(this.primaryControlColor);
        } else {
            metalBumps = this.bumps;
            graphics.setColor(this.controlColor);
        }
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
            i2 += insets.left;
            i3 += insets.top;
        }
        metalBumps.setBumpArea(size);
        metalBumps.paintIcon(this, graphics, i2, i3);
        super.paint(graphics);
    }

    @Override // javax.swing.plaf.basic.BasicSplitPaneDivider
    protected JButton createLeftOneTouchButton() {
        JButton jButton = new JButton() { // from class: javax.swing.plaf.metal.MetalSplitPaneDivider.1
            int[][] buffer = {new int[]{0, 0, 0, 2, 2, 0, 0, 0, 0}, new int[]{0, 0, 2, 1, 1, 1, 0, 0, 0}, new int[]{0, 2, 1, 1, 1, 1, 1, 0, 0}, new int[]{2, 1, 1, 1, 1, 1, 1, 1, 0}, new int[]{0, 3, 3, 3, 3, 3, 3, 3, 3}};

            @Override // javax.swing.JComponent
            public void setBorder(Border border) {
            }

            @Override // javax.swing.JComponent, java.awt.Container, java.awt.Component
            public void paint(Graphics graphics) {
                if (MetalSplitPaneDivider.this.getSplitPaneFromSuper() != null) {
                    int oneTouchSizeFromSuper = MetalSplitPaneDivider.this.getOneTouchSizeFromSuper();
                    int orientationFromSuper = MetalSplitPaneDivider.this.getOrientationFromSuper();
                    int iMin = Math.min(MetalSplitPaneDivider.this.getDividerSize(), oneTouchSizeFromSuper);
                    Color[] colorArr = {getBackground(), MetalLookAndFeel.getPrimaryControlDarkShadow(), MetalLookAndFeel.getPrimaryControlInfo(), MetalLookAndFeel.getPrimaryControlHighlight()};
                    graphics.setColor(getBackground());
                    if (isOpaque()) {
                        graphics.fillRect(0, 0, getWidth(), getHeight());
                    }
                    if (getModel().isPressed()) {
                        colorArr[1] = colorArr[2];
                    }
                    if (orientationFromSuper == 0) {
                        for (int i2 = 1; i2 <= this.buffer[0].length; i2++) {
                            for (int i3 = 1; i3 < iMin; i3++) {
                                if (this.buffer[i3 - 1][i2 - 1] != 0) {
                                    graphics.setColor(colorArr[this.buffer[i3 - 1][i2 - 1]]);
                                    graphics.drawLine(i2, i3, i2, i3);
                                }
                            }
                        }
                        return;
                    }
                    for (int i4 = 1; i4 <= this.buffer[0].length; i4++) {
                        for (int i5 = 1; i5 < iMin; i5++) {
                            if (this.buffer[i5 - 1][i4 - 1] != 0) {
                                graphics.setColor(colorArr[this.buffer[i5 - 1][i4 - 1]]);
                                graphics.drawLine(i5, i4, i5, i4);
                            }
                        }
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
        maybeMakeButtonOpaque(jButton);
        return jButton;
    }

    private void maybeMakeButtonOpaque(JComponent jComponent) {
        Object obj = UIManager.get("SplitPane.oneTouchButtonsOpaque");
        if (obj != null) {
            jComponent.setOpaque(((Boolean) obj).booleanValue());
        }
    }

    @Override // javax.swing.plaf.basic.BasicSplitPaneDivider
    protected JButton createRightOneTouchButton() {
        JButton jButton = new JButton() { // from class: javax.swing.plaf.metal.MetalSplitPaneDivider.2
            int[][] buffer = {new int[]{2, 2, 2, 2, 2, 2, 2, 2}, new int[]{0, 1, 1, 1, 1, 1, 1, 3}, new int[]{0, 0, 1, 1, 1, 1, 3, 0}, new int[]{0, 0, 0, 1, 1, 3, 0, 0}, new int[]{0, 0, 0, 0, 3, 0, 0, 0}};

            @Override // javax.swing.JComponent
            public void setBorder(Border border) {
            }

            @Override // javax.swing.JComponent, java.awt.Container, java.awt.Component
            public void paint(Graphics graphics) {
                if (MetalSplitPaneDivider.this.getSplitPaneFromSuper() != null) {
                    int oneTouchSizeFromSuper = MetalSplitPaneDivider.this.getOneTouchSizeFromSuper();
                    int orientationFromSuper = MetalSplitPaneDivider.this.getOrientationFromSuper();
                    int iMin = Math.min(MetalSplitPaneDivider.this.getDividerSize(), oneTouchSizeFromSuper);
                    Color[] colorArr = {getBackground(), MetalLookAndFeel.getPrimaryControlDarkShadow(), MetalLookAndFeel.getPrimaryControlInfo(), MetalLookAndFeel.getPrimaryControlHighlight()};
                    graphics.setColor(getBackground());
                    if (isOpaque()) {
                        graphics.fillRect(0, 0, getWidth(), getHeight());
                    }
                    if (getModel().isPressed()) {
                        colorArr[1] = colorArr[2];
                    }
                    if (orientationFromSuper == 0) {
                        for (int i2 = 1; i2 <= this.buffer[0].length; i2++) {
                            for (int i3 = 1; i3 < iMin; i3++) {
                                if (this.buffer[i3 - 1][i2 - 1] != 0) {
                                    graphics.setColor(colorArr[this.buffer[i3 - 1][i2 - 1]]);
                                    graphics.drawLine(i2, i3, i2, i3);
                                }
                            }
                        }
                        return;
                    }
                    for (int i4 = 1; i4 <= this.buffer[0].length; i4++) {
                        for (int i5 = 1; i5 < iMin; i5++) {
                            if (this.buffer[i5 - 1][i4 - 1] != 0) {
                                graphics.setColor(colorArr[this.buffer[i5 - 1][i4 - 1]]);
                                graphics.drawLine(i5, i4, i5, i4);
                            }
                        }
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
        maybeMakeButtonOpaque(jButton);
        return jButton;
    }

    /* loaded from: rt.jar:javax/swing/plaf/metal/MetalSplitPaneDivider$MetalDividerLayout.class */
    public class MetalDividerLayout implements LayoutManager {
        public MetalDividerLayout() {
        }

        @Override // java.awt.LayoutManager
        public void layoutContainer(Container container) {
            JButton leftButtonFromSuper = MetalSplitPaneDivider.this.getLeftButtonFromSuper();
            JButton rightButtonFromSuper = MetalSplitPaneDivider.this.getRightButtonFromSuper();
            JSplitPane splitPaneFromSuper = MetalSplitPaneDivider.this.getSplitPaneFromSuper();
            int orientationFromSuper = MetalSplitPaneDivider.this.getOrientationFromSuper();
            int oneTouchSizeFromSuper = MetalSplitPaneDivider.this.getOneTouchSizeFromSuper();
            int oneTouchOffsetFromSuper = MetalSplitPaneDivider.this.getOneTouchOffsetFromSuper();
            Insets insets = MetalSplitPaneDivider.this.getInsets();
            if (leftButtonFromSuper != null && rightButtonFromSuper != null && container == MetalSplitPaneDivider.this) {
                if (splitPaneFromSuper.isOneTouchExpandable()) {
                    if (orientationFromSuper == 0) {
                        int i2 = insets != null ? insets.top : 0;
                        int dividerSize = MetalSplitPaneDivider.this.getDividerSize();
                        if (insets != null) {
                            dividerSize -= insets.top + insets.bottom;
                        }
                        int iMin = Math.min(dividerSize, oneTouchSizeFromSuper);
                        leftButtonFromSuper.setBounds(oneTouchOffsetFromSuper, i2, iMin * 2, iMin);
                        rightButtonFromSuper.setBounds(oneTouchOffsetFromSuper + (oneTouchSizeFromSuper * 2), i2, iMin * 2, iMin);
                        return;
                    }
                    int dividerSize2 = MetalSplitPaneDivider.this.getDividerSize();
                    int i3 = insets != null ? insets.left : 0;
                    if (insets != null) {
                        dividerSize2 -= insets.left + insets.right;
                    }
                    int iMin2 = Math.min(dividerSize2, oneTouchSizeFromSuper);
                    leftButtonFromSuper.setBounds(i3, oneTouchOffsetFromSuper, iMin2, iMin2 * 2);
                    rightButtonFromSuper.setBounds(i3, oneTouchOffsetFromSuper + (oneTouchSizeFromSuper * 2), iMin2, iMin2 * 2);
                    return;
                }
                leftButtonFromSuper.setBounds(-5, -5, 1, 1);
                rightButtonFromSuper.setBounds(-5, -5, 1, 1);
            }
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
