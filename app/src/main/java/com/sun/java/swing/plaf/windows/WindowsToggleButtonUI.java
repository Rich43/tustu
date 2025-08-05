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
import javax.swing.plaf.basic.BasicToggleButtonUI;
import sun.awt.AppContext;

/* loaded from: rt.jar:com/sun/java/swing/plaf/windows/WindowsToggleButtonUI.class */
public class WindowsToggleButtonUI extends BasicToggleButtonUI {
    protected int dashedRectGapX;
    protected int dashedRectGapY;
    protected int dashedRectGapWidth;
    protected int dashedRectGapHeight;
    protected Color focusColor;
    private static final Object WINDOWS_TOGGLE_BUTTON_UI_KEY = new Object();
    private boolean defaults_initialized = false;
    private transient Color cachedSelectedColor = null;
    private transient Color cachedBackgroundColor = null;
    private transient Color cachedHighlightColor = null;

    public static ComponentUI createUI(JComponent jComponent) {
        AppContext appContext = AppContext.getAppContext();
        WindowsToggleButtonUI windowsToggleButtonUI = (WindowsToggleButtonUI) appContext.get(WINDOWS_TOGGLE_BUTTON_UI_KEY);
        if (windowsToggleButtonUI == null) {
            windowsToggleButtonUI = new WindowsToggleButtonUI();
            appContext.put(WINDOWS_TOGGLE_BUTTON_UI_KEY, windowsToggleButtonUI);
        }
        return windowsToggleButtonUI;
    }

    @Override // javax.swing.plaf.basic.BasicButtonUI
    protected void installDefaults(AbstractButton abstractButton) {
        super.installDefaults(abstractButton);
        if (!this.defaults_initialized) {
            String propertyPrefix = getPropertyPrefix();
            this.dashedRectGapX = ((Integer) UIManager.get("Button.dashedRectGapX")).intValue();
            this.dashedRectGapY = ((Integer) UIManager.get("Button.dashedRectGapY")).intValue();
            this.dashedRectGapWidth = ((Integer) UIManager.get("Button.dashedRectGapWidth")).intValue();
            this.dashedRectGapHeight = ((Integer) UIManager.get("Button.dashedRectGapHeight")).intValue();
            this.focusColor = UIManager.getColor(propertyPrefix + "focus");
            this.defaults_initialized = true;
        }
        XPStyle xp = XPStyle.getXP();
        if (xp != null) {
            abstractButton.setBorder(xp.getBorder(abstractButton, WindowsButtonUI.getXPButtonType(abstractButton)));
            LookAndFeel.installProperty(abstractButton, "opaque", Boolean.FALSE);
            LookAndFeel.installProperty(abstractButton, AbstractButton.ROLLOVER_ENABLED_CHANGED_PROPERTY, Boolean.TRUE);
        }
    }

    @Override // javax.swing.plaf.basic.BasicButtonUI
    protected void uninstallDefaults(AbstractButton abstractButton) {
        super.uninstallDefaults(abstractButton);
        this.defaults_initialized = false;
    }

    protected Color getFocusColor() {
        return this.focusColor;
    }

    @Override // javax.swing.plaf.basic.BasicButtonUI
    protected void paintButtonPressed(Graphics graphics, AbstractButton abstractButton) {
        if (XPStyle.getXP() == null && abstractButton.isContentAreaFilled()) {
            Color color = graphics.getColor();
            Color background = abstractButton.getBackground();
            Color color2 = UIManager.getColor("ToggleButton.highlight");
            if (background != this.cachedBackgroundColor || color2 != this.cachedHighlightColor) {
                int red = background.getRed();
                int red2 = color2.getRed();
                int green = background.getGreen();
                int green2 = color2.getGreen();
                int blue = background.getBlue();
                int blue2 = color2.getBlue();
                this.cachedSelectedColor = new Color(Math.min(red, red2) + (Math.abs(red - red2) / 2), Math.min(green, green2) + (Math.abs(green - green2) / 2), Math.min(blue, blue2) + (Math.abs(blue - blue2) / 2));
                this.cachedBackgroundColor = background;
                this.cachedHighlightColor = color2;
            }
            graphics.setColor(this.cachedSelectedColor);
            graphics.fillRect(0, 0, abstractButton.getWidth(), abstractButton.getHeight());
            graphics.setColor(color);
        }
    }

    @Override // javax.swing.plaf.basic.BasicToggleButtonUI, javax.swing.plaf.basic.BasicButtonUI, javax.swing.plaf.ComponentUI
    public void paint(Graphics graphics, JComponent jComponent) {
        if (XPStyle.getXP() != null) {
            WindowsButtonUI.paintXPButtonBackground(graphics, jComponent);
        }
        super.paint(graphics, jComponent);
    }

    @Override // javax.swing.plaf.basic.BasicButtonUI
    protected void paintText(Graphics graphics, AbstractButton abstractButton, Rectangle rectangle, String str) {
        WindowsGraphicsUtils.paintText(graphics, abstractButton, rectangle, str, getTextShiftOffset());
    }

    @Override // javax.swing.plaf.basic.BasicButtonUI
    protected void paintFocus(Graphics graphics, AbstractButton abstractButton, Rectangle rectangle, Rectangle rectangle2, Rectangle rectangle3) {
        graphics.setColor(getFocusColor());
        BasicGraphicsUtils.drawDashedRect(graphics, this.dashedRectGapX, this.dashedRectGapY, abstractButton.getWidth() - this.dashedRectGapWidth, abstractButton.getHeight() - this.dashedRectGapHeight);
    }

    @Override // javax.swing.plaf.basic.BasicButtonUI, javax.swing.plaf.ComponentUI
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
