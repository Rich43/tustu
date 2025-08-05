package com.sun.java.swing.plaf.motif;

import com.sun.org.apache.xalan.internal.templates.Constants;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Insets;
import javax.swing.AbstractButton;
import javax.swing.JComponent;
import javax.swing.LookAndFeel;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.UIResource;
import javax.swing.plaf.basic.BasicToggleButtonUI;
import sun.awt.AppContext;

/* loaded from: rt.jar:com/sun/java/swing/plaf/motif/MotifToggleButtonUI.class */
public class MotifToggleButtonUI extends BasicToggleButtonUI {
    private static final Object MOTIF_TOGGLE_BUTTON_UI_KEY = new Object();
    protected Color selectColor;
    private boolean defaults_initialized = false;

    public static ComponentUI createUI(JComponent jComponent) {
        AppContext appContext = AppContext.getAppContext();
        MotifToggleButtonUI motifToggleButtonUI = (MotifToggleButtonUI) appContext.get(MOTIF_TOGGLE_BUTTON_UI_KEY);
        if (motifToggleButtonUI == null) {
            motifToggleButtonUI = new MotifToggleButtonUI();
            appContext.put(MOTIF_TOGGLE_BUTTON_UI_KEY, motifToggleButtonUI);
        }
        return motifToggleButtonUI;
    }

    @Override // javax.swing.plaf.basic.BasicButtonUI
    public void installDefaults(AbstractButton abstractButton) {
        super.installDefaults(abstractButton);
        if (!this.defaults_initialized) {
            this.selectColor = UIManager.getColor(getPropertyPrefix() + Constants.ATTRNAME_SELECT);
            this.defaults_initialized = true;
        }
        LookAndFeel.installProperty(abstractButton, "opaque", Boolean.FALSE);
    }

    @Override // javax.swing.plaf.basic.BasicButtonUI
    protected void uninstallDefaults(AbstractButton abstractButton) {
        super.uninstallDefaults(abstractButton);
        this.defaults_initialized = false;
    }

    protected Color getSelectColor() {
        return this.selectColor;
    }

    @Override // javax.swing.plaf.basic.BasicButtonUI
    protected void paintButtonPressed(Graphics graphics, AbstractButton abstractButton) {
        if (abstractButton.isContentAreaFilled()) {
            Color color = graphics.getColor();
            Dimension size = abstractButton.getSize();
            Insets insets = abstractButton.getInsets();
            Insets margin = abstractButton.getMargin();
            if (abstractButton.getBackground() instanceof UIResource) {
                graphics.setColor(getSelectColor());
            }
            graphics.fillRect(insets.left - margin.left, insets.top - margin.top, (size.width - (insets.left - margin.left)) - (insets.right - margin.right), (size.height - (insets.top - margin.top)) - (insets.bottom - margin.bottom));
            graphics.setColor(color);
        }
    }

    public Insets getInsets(JComponent jComponent) {
        Border border = jComponent.getBorder();
        return border != null ? border.getBorderInsets(jComponent) : new Insets(0, 0, 0, 0);
    }
}
