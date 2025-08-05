package com.sun.java.swing.plaf.motif;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Insets;
import java.awt.event.MouseEvent;
import javax.swing.JComponent;
import javax.swing.JPopupMenu;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicPopupMenuUI;
import sun.swing.SwingUtilities2;

/* loaded from: rt.jar:com/sun/java/swing/plaf/motif/MotifPopupMenuUI.class */
public class MotifPopupMenuUI extends BasicPopupMenuUI {
    private static Border border = null;
    private Font titleFont = null;

    public static ComponentUI createUI(JComponent jComponent) {
        return new MotifPopupMenuUI();
    }

    @Override // javax.swing.plaf.ComponentUI
    public Dimension getPreferredSize(JComponent jComponent) {
        Dimension dimensionPreferredLayoutSize = jComponent.getLayout().preferredLayoutSize(jComponent);
        String label = ((JPopupMenu) jComponent).getLabel();
        if (this.titleFont == null) {
            this.titleFont = UIManager.getLookAndFeelDefaults().getFont("PopupMenu.font");
        }
        FontMetrics fontMetrics = jComponent.getFontMetrics(this.titleFont);
        int iStringWidth = 0;
        if (label != null) {
            iStringWidth = 0 + SwingUtilities2.stringWidth(jComponent, fontMetrics, label);
        }
        if (dimensionPreferredLayoutSize.width < iStringWidth) {
            dimensionPreferredLayoutSize.width = iStringWidth + 8;
            Insets insets = jComponent.getInsets();
            if (insets != null) {
                dimensionPreferredLayoutSize.width += insets.left + insets.right;
            }
            if (border != null) {
                Insets borderInsets = border.getBorderInsets(jComponent);
                dimensionPreferredLayoutSize.width += borderInsets.left + borderInsets.right;
            }
            return dimensionPreferredLayoutSize;
        }
        return null;
    }

    protected ChangeListener createChangeListener(JPopupMenu jPopupMenu) {
        return new ChangeListener() { // from class: com.sun.java.swing.plaf.motif.MotifPopupMenuUI.1
            @Override // javax.swing.event.ChangeListener
            public void stateChanged(ChangeEvent changeEvent) {
            }
        };
    }

    @Override // javax.swing.plaf.basic.BasicPopupMenuUI, javax.swing.plaf.PopupMenuUI
    public boolean isPopupTrigger(MouseEvent mouseEvent) {
        return mouseEvent.getID() == 501 && (mouseEvent.getModifiers() & 4) != 0;
    }
}
