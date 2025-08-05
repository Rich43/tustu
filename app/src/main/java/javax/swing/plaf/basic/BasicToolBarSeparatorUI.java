package javax.swing.plaf.basic;

import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.JComponent;
import javax.swing.JSeparator;
import javax.swing.JToolBar;
import javax.swing.UIManager;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.UIResource;

/* loaded from: rt.jar:javax/swing/plaf/basic/BasicToolBarSeparatorUI.class */
public class BasicToolBarSeparatorUI extends BasicSeparatorUI {
    public static ComponentUI createUI(JComponent jComponent) {
        return new BasicToolBarSeparatorUI();
    }

    @Override // javax.swing.plaf.basic.BasicSeparatorUI
    protected void installDefaults(JSeparator jSeparator) {
        Dimension separatorSize = ((JToolBar.Separator) jSeparator).getSeparatorSize();
        if (separatorSize == null || (separatorSize instanceof UIResource)) {
            JToolBar.Separator separator = (JToolBar.Separator) jSeparator;
            Dimension dimension = (Dimension) UIManager.get("ToolBar.separatorSize");
            if (dimension != null) {
                if (separator.getOrientation() == 0) {
                    dimension = new Dimension(dimension.height, dimension.width);
                }
                separator.setSeparatorSize(dimension);
            }
        }
    }

    @Override // javax.swing.plaf.basic.BasicSeparatorUI, javax.swing.plaf.ComponentUI
    public void paint(Graphics graphics, JComponent jComponent) {
    }

    @Override // javax.swing.plaf.basic.BasicSeparatorUI, javax.swing.plaf.ComponentUI
    public Dimension getPreferredSize(JComponent jComponent) {
        Dimension separatorSize = ((JToolBar.Separator) jComponent).getSeparatorSize();
        if (separatorSize != null) {
            return separatorSize.getSize();
        }
        return null;
    }
}
