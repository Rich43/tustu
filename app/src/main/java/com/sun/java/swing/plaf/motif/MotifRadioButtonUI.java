package com.sun.java.swing.plaf.motif;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;
import javax.swing.AbstractButton;
import javax.swing.JComponent;
import javax.swing.UIManager;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicRadioButtonUI;
import sun.awt.AppContext;

/* loaded from: rt.jar:com/sun/java/swing/plaf/motif/MotifRadioButtonUI.class */
public class MotifRadioButtonUI extends BasicRadioButtonUI {
    private static final Object MOTIF_RADIO_BUTTON_UI_KEY = new Object();
    protected Color focusColor;
    private boolean defaults_initialized = false;

    public static ComponentUI createUI(JComponent jComponent) {
        AppContext appContext = AppContext.getAppContext();
        MotifRadioButtonUI motifRadioButtonUI = (MotifRadioButtonUI) appContext.get(MOTIF_RADIO_BUTTON_UI_KEY);
        if (motifRadioButtonUI == null) {
            motifRadioButtonUI = new MotifRadioButtonUI();
            appContext.put(MOTIF_RADIO_BUTTON_UI_KEY, motifRadioButtonUI);
        }
        return motifRadioButtonUI;
    }

    @Override // javax.swing.plaf.basic.BasicRadioButtonUI, javax.swing.plaf.basic.BasicButtonUI
    public void installDefaults(AbstractButton abstractButton) {
        super.installDefaults(abstractButton);
        if (!this.defaults_initialized) {
            this.focusColor = UIManager.getColor(getPropertyPrefix() + "focus");
            this.defaults_initialized = true;
        }
    }

    @Override // javax.swing.plaf.basic.BasicRadioButtonUI, javax.swing.plaf.basic.BasicButtonUI
    protected void uninstallDefaults(AbstractButton abstractButton) {
        super.uninstallDefaults(abstractButton);
        this.defaults_initialized = false;
    }

    protected Color getFocusColor() {
        return this.focusColor;
    }

    @Override // javax.swing.plaf.basic.BasicRadioButtonUI
    protected void paintFocus(Graphics graphics, Rectangle rectangle, Dimension dimension) {
        graphics.setColor(getFocusColor());
        graphics.drawRect(0, 0, dimension.width - 1, dimension.height - 1);
    }
}
