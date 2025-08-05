package javax.swing.plaf.multi;

import java.awt.Dimension;
import java.awt.Graphics;
import java.util.Vector;
import javax.accessibility.Accessible;
import javax.swing.JComponent;
import javax.swing.JSplitPane;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.SplitPaneUI;

/* loaded from: rt.jar:javax/swing/plaf/multi/MultiSplitPaneUI.class */
public class MultiSplitPaneUI extends SplitPaneUI {
    protected Vector uis = new Vector();

    public ComponentUI[] getUIs() {
        return MultiLookAndFeel.uisToArray(this.uis);
    }

    @Override // javax.swing.plaf.SplitPaneUI
    public void resetToPreferredSizes(JSplitPane jSplitPane) {
        for (int i2 = 0; i2 < this.uis.size(); i2++) {
            ((SplitPaneUI) this.uis.elementAt(i2)).resetToPreferredSizes(jSplitPane);
        }
    }

    @Override // javax.swing.plaf.SplitPaneUI
    public void setDividerLocation(JSplitPane jSplitPane, int i2) {
        for (int i3 = 0; i3 < this.uis.size(); i3++) {
            ((SplitPaneUI) this.uis.elementAt(i3)).setDividerLocation(jSplitPane, i2);
        }
    }

    @Override // javax.swing.plaf.SplitPaneUI
    public int getDividerLocation(JSplitPane jSplitPane) {
        int dividerLocation = ((SplitPaneUI) this.uis.elementAt(0)).getDividerLocation(jSplitPane);
        for (int i2 = 1; i2 < this.uis.size(); i2++) {
            ((SplitPaneUI) this.uis.elementAt(i2)).getDividerLocation(jSplitPane);
        }
        return dividerLocation;
    }

    @Override // javax.swing.plaf.SplitPaneUI
    public int getMinimumDividerLocation(JSplitPane jSplitPane) {
        int minimumDividerLocation = ((SplitPaneUI) this.uis.elementAt(0)).getMinimumDividerLocation(jSplitPane);
        for (int i2 = 1; i2 < this.uis.size(); i2++) {
            ((SplitPaneUI) this.uis.elementAt(i2)).getMinimumDividerLocation(jSplitPane);
        }
        return minimumDividerLocation;
    }

    @Override // javax.swing.plaf.SplitPaneUI
    public int getMaximumDividerLocation(JSplitPane jSplitPane) {
        int maximumDividerLocation = ((SplitPaneUI) this.uis.elementAt(0)).getMaximumDividerLocation(jSplitPane);
        for (int i2 = 1; i2 < this.uis.size(); i2++) {
            ((SplitPaneUI) this.uis.elementAt(i2)).getMaximumDividerLocation(jSplitPane);
        }
        return maximumDividerLocation;
    }

    @Override // javax.swing.plaf.SplitPaneUI
    public void finishedPaintingChildren(JSplitPane jSplitPane, Graphics graphics) {
        for (int i2 = 0; i2 < this.uis.size(); i2++) {
            ((SplitPaneUI) this.uis.elementAt(i2)).finishedPaintingChildren(jSplitPane, graphics);
        }
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
        MultiSplitPaneUI multiSplitPaneUI = new MultiSplitPaneUI();
        return MultiLookAndFeel.createUIs(multiSplitPaneUI, multiSplitPaneUI.uis, jComponent);
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
