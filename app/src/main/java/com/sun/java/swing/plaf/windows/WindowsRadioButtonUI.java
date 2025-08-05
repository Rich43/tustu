package com.sun.java.swing.plaf.windows;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;
import javax.swing.AbstractButton;
import javax.swing.JComponent;
import javax.swing.LookAndFeel;
import javax.swing.UIManager;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicGraphicsUtils;
import javax.swing.plaf.basic.BasicRadioButtonUI;
import sun.awt.AppContext;

/* loaded from: rt.jar:com/sun/java/swing/plaf/windows/WindowsRadioButtonUI.class */
public class WindowsRadioButtonUI extends BasicRadioButtonUI {
    private static final Object WINDOWS_RADIO_BUTTON_UI_KEY = new Object();
    protected int dashedRectGapX;
    protected int dashedRectGapY;
    protected int dashedRectGapWidth;
    protected int dashedRectGapHeight;
    protected Color focusColor;
    private boolean initialized = false;

    public static ComponentUI createUI(JComponent jComponent) {
        AppContext appContext = AppContext.getAppContext();
        WindowsRadioButtonUI windowsRadioButtonUI = (WindowsRadioButtonUI) appContext.get(WINDOWS_RADIO_BUTTON_UI_KEY);
        if (windowsRadioButtonUI == null) {
            windowsRadioButtonUI = new WindowsRadioButtonUI();
            appContext.put(WINDOWS_RADIO_BUTTON_UI_KEY, windowsRadioButtonUI);
        }
        return windowsRadioButtonUI;
    }

    @Override // javax.swing.plaf.basic.BasicRadioButtonUI, javax.swing.plaf.basic.BasicButtonUI
    public void installDefaults(AbstractButton abstractButton) {
        super.installDefaults(abstractButton);
        if (!this.initialized) {
            this.dashedRectGapX = ((Integer) UIManager.get("Button.dashedRectGapX")).intValue();
            this.dashedRectGapY = ((Integer) UIManager.get("Button.dashedRectGapY")).intValue();
            this.dashedRectGapWidth = ((Integer) UIManager.get("Button.dashedRectGapWidth")).intValue();
            this.dashedRectGapHeight = ((Integer) UIManager.get("Button.dashedRectGapHeight")).intValue();
            this.focusColor = UIManager.getColor(getPropertyPrefix() + "focus");
            this.initialized = true;
        }
        if (XPStyle.getXP() != null) {
            LookAndFeel.installProperty(abstractButton, AbstractButton.ROLLOVER_ENABLED_CHANGED_PROPERTY, Boolean.TRUE);
        }
    }

    @Override // javax.swing.plaf.basic.BasicRadioButtonUI, javax.swing.plaf.basic.BasicButtonUI
    protected void uninstallDefaults(AbstractButton abstractButton) {
        super.uninstallDefaults(abstractButton);
        this.initialized = false;
    }

    protected Color getFocusColor() {
        return this.focusColor;
    }

    @Override // javax.swing.plaf.basic.BasicButtonUI
    protected void paintText(Graphics graphics, AbstractButton abstractButton, Rectangle rectangle, String str) {
        WindowsGraphicsUtils.paintText(graphics, abstractButton, rectangle, str, getTextShiftOffset());
    }

    @Override // javax.swing.plaf.basic.BasicRadioButtonUI
    protected void paintFocus(Graphics graphics, Rectangle rectangle, Dimension dimension) {
        graphics.setColor(getFocusColor());
        BasicGraphicsUtils.drawDashedRect(graphics, rectangle.f12372x, rectangle.f12373y, rectangle.width, rectangle.height);
    }

    @Override // javax.swing.plaf.basic.BasicRadioButtonUI, javax.swing.plaf.basic.BasicButtonUI, javax.swing.plaf.ComponentUI
    public Dimension getPreferredSize(JComponent jComponent) {
        Dimension preferredSize = super.getPreferredSize(jComponent);
        AbstractButton abstractButton = (AbstractButton) jComponent;
        if (preferredSize != null && abstractButton.isFocusPainted()) {
            if (preferredSize.width % 2 == 0) {
                preferredSize.width++;
            }
            if (preferredSize.height % 2 == 0) {
                preferredSize.height++;
            }
        }
        return preferredSize;
    }
}
