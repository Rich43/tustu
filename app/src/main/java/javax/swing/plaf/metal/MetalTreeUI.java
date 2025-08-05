package javax.swing.plaf.metal;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Rectangle;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.JComponent;
import javax.swing.UIManager;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicTreeUI;
import javax.swing.tree.TreePath;

/* loaded from: rt.jar:javax/swing/plaf/metal/MetalTreeUI.class */
public class MetalTreeUI extends BasicTreeUI {
    private static Color lineColor;
    private static final String LINE_STYLE = "JTree.lineStyle";
    private static final String LEG_LINE_STYLE_STRING = "Angled";
    private static final String HORIZ_STYLE_STRING = "Horizontal";
    private static final String NO_STYLE_STRING = "None";
    private static final int LEG_LINE_STYLE = 2;
    private static final int HORIZ_LINE_STYLE = 1;
    private static final int NO_LINE_STYLE = 0;
    private int lineStyle = 2;
    private PropertyChangeListener lineStyleListener = new LineListener();

    public static ComponentUI createUI(JComponent jComponent) {
        return new MetalTreeUI();
    }

    @Override // javax.swing.plaf.basic.BasicTreeUI
    protected int getHorizontalLegBuffer() {
        return 3;
    }

    @Override // javax.swing.plaf.basic.BasicTreeUI, javax.swing.plaf.ComponentUI
    public void installUI(JComponent jComponent) {
        super.installUI(jComponent);
        lineColor = UIManager.getColor("Tree.line");
        decodeLineStyle(jComponent.getClientProperty(LINE_STYLE));
        jComponent.addPropertyChangeListener(this.lineStyleListener);
    }

    @Override // javax.swing.plaf.basic.BasicTreeUI, javax.swing.plaf.ComponentUI
    public void uninstallUI(JComponent jComponent) {
        jComponent.removePropertyChangeListener(this.lineStyleListener);
        super.uninstallUI(jComponent);
    }

    protected void decodeLineStyle(Object obj) {
        if (obj == null || obj.equals(LEG_LINE_STYLE_STRING)) {
            this.lineStyle = 2;
        } else if (obj.equals("None")) {
            this.lineStyle = 0;
        } else if (obj.equals(HORIZ_STYLE_STRING)) {
            this.lineStyle = 1;
        }
    }

    protected boolean isLocationInExpandControl(int i2, int i3, int i4, int i5) {
        int iconWidth;
        if (this.tree != null && !isLeaf(i2)) {
            if (getExpandedIcon() != null) {
                iconWidth = getExpandedIcon().getIconWidth() + 6;
            } else {
                iconWidth = 8;
            }
            Insets insets = this.tree.getInsets();
            int leftChildIndent = (insets != null ? insets.left : 0) + (((((i3 + this.depthOffset) - 1) * this.totalChildIndent) + getLeftChildIndent()) - (iconWidth / 2));
            return i4 >= leftChildIndent && i4 <= leftChildIndent + iconWidth;
        }
        return false;
    }

    @Override // javax.swing.plaf.basic.BasicTreeUI, javax.swing.plaf.ComponentUI
    public void paint(Graphics graphics, JComponent jComponent) {
        super.paint(graphics, jComponent);
        if (this.lineStyle == 1 && !this.largeModel) {
            paintHorizontalSeparators(graphics, jComponent);
        }
    }

    protected void paintHorizontalSeparators(Graphics graphics, JComponent jComponent) {
        Rectangle pathBounds;
        graphics.setColor(lineColor);
        Rectangle clipBounds = graphics.getClipBounds();
        int rowForPath = getRowForPath(this.tree, getClosestPathForLocation(this.tree, 0, clipBounds.f12373y));
        int rowForPath2 = getRowForPath(this.tree, getClosestPathForLocation(this.tree, 0, (clipBounds.f12373y + clipBounds.height) - 1));
        if (rowForPath <= -1 || rowForPath2 <= -1) {
            return;
        }
        for (int i2 = rowForPath; i2 <= rowForPath2; i2++) {
            TreePath pathForRow = getPathForRow(this.tree, i2);
            if (pathForRow != null && pathForRow.getPathCount() == 2 && (pathBounds = getPathBounds(this.tree, getPathForRow(this.tree, i2))) != null) {
                graphics.drawLine(clipBounds.f12372x, pathBounds.f12373y, clipBounds.f12372x + clipBounds.width, pathBounds.f12373y);
            }
        }
    }

    @Override // javax.swing.plaf.basic.BasicTreeUI
    protected void paintVerticalPartOfLeg(Graphics graphics, Rectangle rectangle, Insets insets, TreePath treePath) {
        if (this.lineStyle == 2) {
            super.paintVerticalPartOfLeg(graphics, rectangle, insets, treePath);
        }
    }

    @Override // javax.swing.plaf.basic.BasicTreeUI
    protected void paintHorizontalPartOfLeg(Graphics graphics, Rectangle rectangle, Insets insets, Rectangle rectangle2, TreePath treePath, int i2, boolean z2, boolean z3, boolean z4) {
        if (this.lineStyle == 2) {
            super.paintHorizontalPartOfLeg(graphics, rectangle, insets, rectangle2, treePath, i2, z2, z3, z4);
        }
    }

    /* loaded from: rt.jar:javax/swing/plaf/metal/MetalTreeUI$LineListener.class */
    class LineListener implements PropertyChangeListener {
        LineListener() {
        }

        @Override // java.beans.PropertyChangeListener
        public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
            if (propertyChangeEvent.getPropertyName().equals(MetalTreeUI.LINE_STYLE)) {
                MetalTreeUI.this.decodeLineStyle(propertyChangeEvent.getNewValue());
            }
        }
    }
}
