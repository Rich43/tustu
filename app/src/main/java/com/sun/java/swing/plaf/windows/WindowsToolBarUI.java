package com.sun.java.swing.plaf.windows;

import com.sun.java.swing.plaf.windows.TMSchema;
import java.awt.Graphics;
import javax.swing.AbstractButton;
import javax.swing.JComponent;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicToolBarUI;

/* loaded from: rt.jar:com/sun/java/swing/plaf/windows/WindowsToolBarUI.class */
public class WindowsToolBarUI extends BasicToolBarUI {
    public static ComponentUI createUI(JComponent jComponent) {
        return new WindowsToolBarUI();
    }

    @Override // javax.swing.plaf.basic.BasicToolBarUI
    protected void installDefaults() {
        if (XPStyle.getXP() != null) {
            setRolloverBorders(true);
        }
        super.installDefaults();
    }

    @Override // javax.swing.plaf.basic.BasicToolBarUI
    protected Border createRolloverBorder() {
        if (XPStyle.getXP() != null) {
            return new EmptyBorder(3, 3, 3, 3);
        }
        return super.createRolloverBorder();
    }

    @Override // javax.swing.plaf.basic.BasicToolBarUI
    protected Border createNonRolloverBorder() {
        if (XPStyle.getXP() != null) {
            return new EmptyBorder(3, 3, 3, 3);
        }
        return super.createNonRolloverBorder();
    }

    @Override // javax.swing.plaf.ComponentUI
    public void paint(Graphics graphics, JComponent jComponent) {
        XPStyle xp = XPStyle.getXP();
        if (xp != null) {
            xp.getSkin(jComponent, TMSchema.Part.TP_TOOLBAR).paintSkin(graphics, 0, 0, jComponent.getWidth(), jComponent.getHeight(), null, true);
        } else {
            super.paint(graphics, jComponent);
        }
    }

    @Override // javax.swing.plaf.basic.BasicToolBarUI
    protected Border getRolloverBorder(AbstractButton abstractButton) {
        XPStyle xp = XPStyle.getXP();
        if (xp != null) {
            return xp.getBorder(abstractButton, WindowsButtonUI.getXPButtonType(abstractButton));
        }
        return super.getRolloverBorder(abstractButton);
    }
}
