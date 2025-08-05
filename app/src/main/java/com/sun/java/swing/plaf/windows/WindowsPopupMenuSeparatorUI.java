package com.sun.java.swing.plaf.windows;

import com.sun.java.swing.plaf.windows.TMSchema;
import com.sun.java.swing.plaf.windows.XPStyle;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import javax.swing.JComponent;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicPopupMenuSeparatorUI;

/* loaded from: rt.jar:com/sun/java/swing/plaf/windows/WindowsPopupMenuSeparatorUI.class */
public class WindowsPopupMenuSeparatorUI extends BasicPopupMenuSeparatorUI {
    public static ComponentUI createUI(JComponent jComponent) {
        return new WindowsPopupMenuSeparatorUI();
    }

    @Override // javax.swing.plaf.basic.BasicPopupMenuSeparatorUI, javax.swing.plaf.basic.BasicSeparatorUI, javax.swing.plaf.ComponentUI
    public void paint(Graphics graphics, JComponent jComponent) {
        Dimension size = jComponent.getSize();
        XPStyle xp = XPStyle.getXP();
        if (WindowsMenuItemUI.isVistaPainting(xp)) {
            int iIntValue = 1;
            Container parent = jComponent.getParent();
            if (parent instanceof JComponent) {
                Object clientProperty = ((JComponent) parent).getClientProperty(WindowsPopupMenuUI.GUTTER_OFFSET_KEY);
                if (clientProperty instanceof Integer) {
                    iIntValue = (((Integer) clientProperty).intValue() - jComponent.getX()) + WindowsPopupMenuUI.getGutterWidth();
                }
            }
            XPStyle.Skin skin = xp.getSkin(jComponent, TMSchema.Part.MP_POPUPSEPARATOR);
            int height = skin.getHeight();
            skin.paintSkin(graphics, iIntValue, (size.height - height) / 2, (size.width - iIntValue) - 1, height, TMSchema.State.NORMAL);
            return;
        }
        int i2 = size.height / 2;
        graphics.setColor(jComponent.getForeground());
        graphics.drawLine(1, i2 - 1, size.width - 2, i2 - 1);
        graphics.setColor(jComponent.getBackground());
        graphics.drawLine(1, i2, size.width - 2, i2);
    }

    @Override // javax.swing.plaf.basic.BasicPopupMenuSeparatorUI, javax.swing.plaf.basic.BasicSeparatorUI, javax.swing.plaf.ComponentUI
    public Dimension getPreferredSize(JComponent jComponent) {
        int height = 0;
        Font font = jComponent.getFont();
        if (font != null) {
            height = jComponent.getFontMetrics(font).getHeight();
        }
        return new Dimension(0, (height / 2) + 2);
    }
}
