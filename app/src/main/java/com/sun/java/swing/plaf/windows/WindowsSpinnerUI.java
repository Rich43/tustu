package com.sun.java.swing.plaf.windows;

import com.sun.java.swing.plaf.windows.TMSchema;
import com.sun.java.swing.plaf.windows.XPStyle;
import java.awt.Component;
import java.awt.Graphics;
import javax.swing.JComponent;
import javax.swing.UIManager;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.UIResource;
import javax.swing.plaf.basic.BasicSpinnerUI;

/* loaded from: rt.jar:com/sun/java/swing/plaf/windows/WindowsSpinnerUI.class */
public class WindowsSpinnerUI extends BasicSpinnerUI {
    public static ComponentUI createUI(JComponent jComponent) {
        return new WindowsSpinnerUI();
    }

    @Override // javax.swing.plaf.ComponentUI
    public void paint(Graphics graphics, JComponent jComponent) {
        if (XPStyle.getXP() != null) {
            paintXPBackground(graphics, jComponent);
        }
        super.paint(graphics, jComponent);
    }

    private TMSchema.State getXPState(JComponent jComponent) {
        TMSchema.State state = TMSchema.State.NORMAL;
        if (!jComponent.isEnabled()) {
            state = TMSchema.State.DISABLED;
        }
        return state;
    }

    private void paintXPBackground(Graphics graphics, JComponent jComponent) {
        XPStyle xp = XPStyle.getXP();
        if (xp == null) {
            return;
        }
        xp.getSkin(jComponent, TMSchema.Part.EP_EDIT).paintSkin(graphics, 0, 0, jComponent.getWidth(), jComponent.getHeight(), getXPState(jComponent));
    }

    @Override // javax.swing.plaf.basic.BasicSpinnerUI
    protected Component createPreviousButton() {
        if (XPStyle.getXP() != null) {
            XPStyle.GlyphButton glyphButton = new XPStyle.GlyphButton(this.spinner, TMSchema.Part.SPNP_DOWN);
            glyphButton.setPreferredSize(UIManager.getDimension("Spinner.arrowButtonSize"));
            glyphButton.setRequestFocusEnabled(false);
            installPreviousButtonListeners(glyphButton);
            return glyphButton;
        }
        return super.createPreviousButton();
    }

    @Override // javax.swing.plaf.basic.BasicSpinnerUI
    protected Component createNextButton() {
        if (XPStyle.getXP() != null) {
            XPStyle.GlyphButton glyphButton = new XPStyle.GlyphButton(this.spinner, TMSchema.Part.SPNP_UP);
            glyphButton.setPreferredSize(UIManager.getDimension("Spinner.arrowButtonSize"));
            glyphButton.setRequestFocusEnabled(false);
            installNextButtonListeners(glyphButton);
            return glyphButton;
        }
        return super.createNextButton();
    }

    private UIResource getUIResource(Object[] objArr) {
        for (int i2 = 0; i2 < objArr.length; i2++) {
            if (objArr[i2] instanceof UIResource) {
                return (UIResource) objArr[i2];
            }
        }
        return null;
    }
}
