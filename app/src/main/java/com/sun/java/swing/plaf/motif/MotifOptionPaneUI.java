package com.sun.java.swing.plaf.motif;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicOptionPaneUI;

/* loaded from: rt.jar:com/sun/java/swing/plaf/motif/MotifOptionPaneUI.class */
public class MotifOptionPaneUI extends BasicOptionPaneUI {
    public static ComponentUI createUI(JComponent jComponent) {
        return new MotifOptionPaneUI();
    }

    @Override // javax.swing.plaf.basic.BasicOptionPaneUI
    protected Container createButtonArea() {
        Container containerCreateButtonArea = super.createButtonArea();
        if (containerCreateButtonArea != null && (containerCreateButtonArea.getLayout() instanceof BasicOptionPaneUI.ButtonAreaLayout)) {
            ((BasicOptionPaneUI.ButtonAreaLayout) containerCreateButtonArea.getLayout()).setCentersChildren(false);
        }
        return containerCreateButtonArea;
    }

    @Override // javax.swing.plaf.basic.BasicOptionPaneUI
    public Dimension getMinimumOptionPaneSize() {
        return null;
    }

    @Override // javax.swing.plaf.basic.BasicOptionPaneUI
    protected Container createSeparator() {
        return new JPanel() { // from class: com.sun.java.swing.plaf.motif.MotifOptionPaneUI.1
            @Override // javax.swing.JComponent, java.awt.Container, java.awt.Component
            public Dimension getPreferredSize() {
                return new Dimension(10, 2);
            }

            @Override // javax.swing.JComponent, java.awt.Container, java.awt.Component
            public void paint(Graphics graphics) {
                int width = getWidth();
                graphics.setColor(Color.darkGray);
                graphics.drawLine(0, 0, width, 0);
                graphics.setColor(Color.white);
                graphics.drawLine(0, 1, width, 1);
            }
        };
    }

    @Override // javax.swing.plaf.basic.BasicOptionPaneUI
    protected void addIcon(Container container) {
        Icon icon = getIcon();
        if (icon != null) {
            JLabel jLabel = new JLabel(icon);
            jLabel.setVerticalAlignment(0);
            container.add(jLabel, "West");
        }
    }
}
