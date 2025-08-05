package com.sun.java.swing.plaf.windows;

import com.sun.java.swing.plaf.windows.TMSchema;
import com.sun.java.swing.plaf.windows.XPStyle;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.JComponent;
import javax.swing.JSeparator;
import javax.swing.JToolBar;
import javax.swing.UIDefaults;
import javax.swing.UIManager;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicToolBarSeparatorUI;

/* loaded from: rt.jar:com/sun/java/swing/plaf/windows/WindowsToolBarSeparatorUI.class */
public class WindowsToolBarSeparatorUI extends BasicToolBarSeparatorUI {
    public static ComponentUI createUI(JComponent jComponent) {
        return new WindowsToolBarSeparatorUI();
    }

    @Override // javax.swing.plaf.basic.BasicToolBarSeparatorUI, javax.swing.plaf.basic.BasicSeparatorUI, javax.swing.plaf.ComponentUI
    public Dimension getPreferredSize(JComponent jComponent) {
        Dimension dimension;
        Dimension separatorSize = ((JToolBar.Separator) jComponent).getSeparatorSize();
        if (separatorSize != null) {
            dimension = separatorSize.getSize();
        } else {
            dimension = new Dimension(6, 6);
            XPStyle xp = XPStyle.getXP();
            if (xp != null) {
                XPStyle.Skin skin = xp.getSkin(jComponent, ((JSeparator) jComponent).getOrientation() == 1 ? TMSchema.Part.TP_SEPARATOR : TMSchema.Part.TP_SEPARATORVERT);
                dimension.width = skin.getWidth();
                dimension.height = skin.getHeight();
            }
            if (((JSeparator) jComponent).getOrientation() == 1) {
                dimension.height = 0;
            } else {
                dimension.width = 0;
            }
        }
        return dimension;
    }

    @Override // javax.swing.plaf.basic.BasicSeparatorUI, javax.swing.plaf.ComponentUI
    public Dimension getMaximumSize(JComponent jComponent) {
        Dimension preferredSize = getPreferredSize(jComponent);
        if (((JSeparator) jComponent).getOrientation() == 1) {
            return new Dimension(preferredSize.width, Short.MAX_VALUE);
        }
        return new Dimension(Short.MAX_VALUE, preferredSize.height);
    }

    @Override // javax.swing.plaf.basic.BasicToolBarSeparatorUI, javax.swing.plaf.basic.BasicSeparatorUI, javax.swing.plaf.ComponentUI
    public void paint(Graphics graphics, JComponent jComponent) {
        boolean z2 = ((JSeparator) jComponent).getOrientation() == 1;
        Dimension size = jComponent.getSize();
        XPStyle xp = XPStyle.getXP();
        if (xp != null) {
            XPStyle.Skin skin = xp.getSkin(jComponent, z2 ? TMSchema.Part.TP_SEPARATOR : TMSchema.Part.TP_SEPARATORVERT);
            skin.paintSkin(graphics, z2 ? (size.width - skin.getWidth()) / 2 : 0, z2 ? 0 : (size.height - skin.getHeight()) / 2, z2 ? skin.getWidth() : size.width, z2 ? size.height : skin.getHeight(), null);
            return;
        }
        Color color = graphics.getColor();
        UIDefaults lookAndFeelDefaults = UIManager.getLookAndFeelDefaults();
        Color color2 = lookAndFeelDefaults.getColor("ToolBar.shadow");
        Color color3 = lookAndFeelDefaults.getColor("ToolBar.highlight");
        if (z2) {
            int i2 = (size.width / 2) - 1;
            graphics.setColor(color2);
            graphics.drawLine(i2, 2, i2, size.height - 2);
            graphics.setColor(color3);
            graphics.drawLine(i2 + 1, 2, i2 + 1, size.height - 2);
        } else {
            int i3 = (size.height / 2) - 1;
            graphics.setColor(color2);
            graphics.drawLine(2, i3, size.width - 2, i3);
            graphics.setColor(color3);
            graphics.drawLine(2, i3 + 1, size.width - 2, i3 + 1);
        }
        graphics.setColor(color);
    }
}
