package javax.swing.plaf.multi;

import java.awt.Dimension;
import java.awt.Graphics;
import java.util.Vector;
import javax.accessibility.Accessible;
import javax.swing.JComponent;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.ToolBarUI;

/* loaded from: rt.jar:javax/swing/plaf/multi/MultiToolBarUI.class */
public class MultiToolBarUI extends ToolBarUI {
    protected Vector uis = new Vector();

    public ComponentUI[] getUIs() {
        return MultiLookAndFeel.uisToArray(this.uis);
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
        MultiToolBarUI multiToolBarUI = new MultiToolBarUI();
        return MultiLookAndFeel.createUIs(multiToolBarUI, multiToolBarUI.uis, jComponent);
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
