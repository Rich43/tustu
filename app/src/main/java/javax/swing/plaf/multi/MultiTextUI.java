package javax.swing.plaf.multi;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.Vector;
import javax.accessibility.Accessible;
import javax.swing.JComponent;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.TextUI;
import javax.swing.text.BadLocationException;
import javax.swing.text.EditorKit;
import javax.swing.text.JTextComponent;
import javax.swing.text.Position;
import javax.swing.text.View;

/* loaded from: rt.jar:javax/swing/plaf/multi/MultiTextUI.class */
public class MultiTextUI extends TextUI {
    protected Vector uis = new Vector();

    public ComponentUI[] getUIs() {
        return MultiLookAndFeel.uisToArray(this.uis);
    }

    @Override // javax.swing.plaf.TextUI
    public String getToolTipText(JTextComponent jTextComponent, Point point) {
        String toolTipText = ((TextUI) this.uis.elementAt(0)).getToolTipText(jTextComponent, point);
        for (int i2 = 1; i2 < this.uis.size(); i2++) {
            ((TextUI) this.uis.elementAt(i2)).getToolTipText(jTextComponent, point);
        }
        return toolTipText;
    }

    @Override // javax.swing.plaf.TextUI
    public Rectangle modelToView(JTextComponent jTextComponent, int i2) throws BadLocationException {
        Rectangle rectangleModelToView = ((TextUI) this.uis.elementAt(0)).modelToView(jTextComponent, i2);
        for (int i3 = 1; i3 < this.uis.size(); i3++) {
            ((TextUI) this.uis.elementAt(i3)).modelToView(jTextComponent, i2);
        }
        return rectangleModelToView;
    }

    @Override // javax.swing.plaf.TextUI
    public Rectangle modelToView(JTextComponent jTextComponent, int i2, Position.Bias bias) throws BadLocationException {
        Rectangle rectangleModelToView = ((TextUI) this.uis.elementAt(0)).modelToView(jTextComponent, i2, bias);
        for (int i3 = 1; i3 < this.uis.size(); i3++) {
            ((TextUI) this.uis.elementAt(i3)).modelToView(jTextComponent, i2, bias);
        }
        return rectangleModelToView;
    }

    @Override // javax.swing.plaf.TextUI
    public int viewToModel(JTextComponent jTextComponent, Point point) {
        int iViewToModel = ((TextUI) this.uis.elementAt(0)).viewToModel(jTextComponent, point);
        for (int i2 = 1; i2 < this.uis.size(); i2++) {
            ((TextUI) this.uis.elementAt(i2)).viewToModel(jTextComponent, point);
        }
        return iViewToModel;
    }

    @Override // javax.swing.plaf.TextUI
    public int viewToModel(JTextComponent jTextComponent, Point point, Position.Bias[] biasArr) {
        int iViewToModel = ((TextUI) this.uis.elementAt(0)).viewToModel(jTextComponent, point, biasArr);
        for (int i2 = 1; i2 < this.uis.size(); i2++) {
            ((TextUI) this.uis.elementAt(i2)).viewToModel(jTextComponent, point, biasArr);
        }
        return iViewToModel;
    }

    @Override // javax.swing.plaf.TextUI
    public int getNextVisualPositionFrom(JTextComponent jTextComponent, int i2, Position.Bias bias, int i3, Position.Bias[] biasArr) throws BadLocationException {
        int nextVisualPositionFrom = ((TextUI) this.uis.elementAt(0)).getNextVisualPositionFrom(jTextComponent, i2, bias, i3, biasArr);
        for (int i4 = 1; i4 < this.uis.size(); i4++) {
            ((TextUI) this.uis.elementAt(i4)).getNextVisualPositionFrom(jTextComponent, i2, bias, i3, biasArr);
        }
        return nextVisualPositionFrom;
    }

    @Override // javax.swing.plaf.TextUI
    public void damageRange(JTextComponent jTextComponent, int i2, int i3) {
        for (int i4 = 0; i4 < this.uis.size(); i4++) {
            ((TextUI) this.uis.elementAt(i4)).damageRange(jTextComponent, i2, i3);
        }
    }

    @Override // javax.swing.plaf.TextUI
    public void damageRange(JTextComponent jTextComponent, int i2, int i3, Position.Bias bias, Position.Bias bias2) {
        for (int i4 = 0; i4 < this.uis.size(); i4++) {
            ((TextUI) this.uis.elementAt(i4)).damageRange(jTextComponent, i2, i3, bias, bias2);
        }
    }

    @Override // javax.swing.plaf.TextUI
    public EditorKit getEditorKit(JTextComponent jTextComponent) {
        EditorKit editorKit = ((TextUI) this.uis.elementAt(0)).getEditorKit(jTextComponent);
        for (int i2 = 1; i2 < this.uis.size(); i2++) {
            ((TextUI) this.uis.elementAt(i2)).getEditorKit(jTextComponent);
        }
        return editorKit;
    }

    @Override // javax.swing.plaf.TextUI
    public View getRootView(JTextComponent jTextComponent) {
        View rootView = ((TextUI) this.uis.elementAt(0)).getRootView(jTextComponent);
        for (int i2 = 1; i2 < this.uis.size(); i2++) {
            ((TextUI) this.uis.elementAt(i2)).getRootView(jTextComponent);
        }
        return rootView;
    }

    @Override // javax.swing.plaf.ComponentUI
    public boolean contains(JComponent jComponent, int i2, int i3) {
        boolean zContains = ((ComponentUI) this.uis.elementAt(0)).contains(jComponent, i2, i3);
        for (int i4 = 1; i4 < this.uis.size(); i4++) {
            ((ComponentUI) this.uis.elementAt(i4)).contains(jComponent, i2, i3);
        }
        return zContains;
    }

    @Override // javax.swing.plaf.ComponentUI
    public void update(Graphics graphics, JComponent jComponent) {
        for (int i2 = 0; i2 < this.uis.size(); i2++) {
            ((ComponentUI) this.uis.elementAt(i2)).update(graphics, jComponent);
        }
    }

    public static ComponentUI createUI(JComponent jComponent) {
        MultiTextUI multiTextUI = new MultiTextUI();
        return MultiLookAndFeel.createUIs(multiTextUI, multiTextUI.uis, jComponent);
    }

    @Override // javax.swing.plaf.ComponentUI
    public void installUI(JComponent jComponent) {
        for (int i2 = 0; i2 < this.uis.size(); i2++) {
            ((ComponentUI) this.uis.elementAt(i2)).installUI(jComponent);
        }
    }

    @Override // javax.swing.plaf.ComponentUI
    public void uninstallUI(JComponent jComponent) {
        for (int i2 = 0; i2 < this.uis.size(); i2++) {
            ((ComponentUI) this.uis.elementAt(i2)).uninstallUI(jComponent);
        }
    }

    @Override // javax.swing.plaf.ComponentUI
    public void paint(Graphics graphics, JComponent jComponent) {
        for (int i2 = 0; i2 < this.uis.size(); i2++) {
            ((ComponentUI) this.uis.elementAt(i2)).paint(graphics, jComponent);
        }
    }

    @Override // javax.swing.plaf.ComponentUI
    public Dimension getPreferredSize(JComponent jComponent) {
        Dimension preferredSize = ((ComponentUI) this.uis.elementAt(0)).getPreferredSize(jComponent);
        for (int i2 = 1; i2 < this.uis.size(); i2++) {
            ((ComponentUI) this.uis.elementAt(i2)).getPreferredSize(jComponent);
        }
        return preferredSize;
    }

    @Override // javax.swing.plaf.ComponentUI
    public Dimension getMinimumSize(JComponent jComponent) {
        Dimension minimumSize = ((ComponentUI) this.uis.elementAt(0)).getMinimumSize(jComponent);
        for (int i2 = 1; i2 < this.uis.size(); i2++) {
            ((ComponentUI) this.uis.elementAt(i2)).getMinimumSize(jComponent);
        }
        return minimumSize;
    }

    @Override // javax.swing.plaf.ComponentUI
    public Dimension getMaximumSize(JComponent jComponent) {
        Dimension maximumSize = ((ComponentUI) this.uis.elementAt(0)).getMaximumSize(jComponent);
        for (int i2 = 1; i2 < this.uis.size(); i2++) {
            ((ComponentUI) this.uis.elementAt(i2)).getMaximumSize(jComponent);
        }
        return maximumSize;
    }

    @Override // javax.swing.plaf.ComponentUI
    public int getAccessibleChildrenCount(JComponent jComponent) {
        int accessibleChildrenCount = ((ComponentUI) this.uis.elementAt(0)).getAccessibleChildrenCount(jComponent);
        for (int i2 = 1; i2 < this.uis.size(); i2++) {
            ((ComponentUI) this.uis.elementAt(i2)).getAccessibleChildrenCount(jComponent);
        }
        return accessibleChildrenCount;
    }

    @Override // javax.swing.plaf.ComponentUI
    public Accessible getAccessibleChild(JComponent jComponent, int i2) {
        Accessible accessibleChild = ((ComponentUI) this.uis.elementAt(0)).getAccessibleChild(jComponent, i2);
        for (int i3 = 1; i3 < this.uis.size(); i3++) {
            ((ComponentUI) this.uis.elementAt(i3)).getAccessibleChild(jComponent, i2);
        }
        return accessibleChild;
    }
}
