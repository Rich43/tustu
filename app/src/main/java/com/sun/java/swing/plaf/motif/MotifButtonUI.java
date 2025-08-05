package com.sun.java.swing.plaf.motif;

import com.sun.org.apache.xalan.internal.templates.Constants;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.Shape;
import javax.swing.AbstractButton;
import javax.swing.JComponent;
import javax.swing.LookAndFeel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.AbstractBorder;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicButtonListener;
import javax.swing.plaf.basic.BasicButtonUI;
import sun.awt.AppContext;

/* loaded from: rt.jar:com/sun/java/swing/plaf/motif/MotifButtonUI.class */
public class MotifButtonUI extends BasicButtonUI {
    protected Color selectColor;
    private boolean defaults_initialized = false;
    private static final Object MOTIF_BUTTON_UI_KEY = new Object();

    public static ComponentUI createUI(JComponent jComponent) {
        AppContext appContext = AppContext.getAppContext();
        MotifButtonUI motifButtonUI = (MotifButtonUI) appContext.get(MOTIF_BUTTON_UI_KEY);
        if (motifButtonUI == null) {
            motifButtonUI = new MotifButtonUI();
            appContext.put(MOTIF_BUTTON_UI_KEY, motifButtonUI);
        }
        return motifButtonUI;
    }

    @Override // javax.swing.plaf.basic.BasicButtonUI
    protected BasicButtonListener createButtonListener(AbstractButton abstractButton) {
        return new MotifButtonListener(abstractButton);
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

    @Override // javax.swing.plaf.basic.BasicButtonUI, javax.swing.plaf.ComponentUI
    public void paint(Graphics graphics, JComponent jComponent) {
        fillContentArea(graphics, (AbstractButton) jComponent, jComponent.getBackground());
        super.paint(graphics, jComponent);
    }

    @Override // javax.swing.plaf.basic.BasicButtonUI
    protected void paintIcon(Graphics graphics, JComponent jComponent, Rectangle rectangle) {
        Shape clip = graphics.getClip();
        Rectangle interiorRectangle = AbstractBorder.getInteriorRectangle(jComponent, jComponent.getBorder(), 0, 0, jComponent.getWidth(), jComponent.getHeight());
        Rectangle bounds = clip.getBounds();
        graphics.setClip(SwingUtilities.computeIntersection(bounds.f12372x, bounds.f12373y, bounds.width, bounds.height, interiorRectangle));
        super.paintIcon(graphics, jComponent, rectangle);
        graphics.setClip(clip);
    }

    @Override // javax.swing.plaf.basic.BasicButtonUI
    protected void paintFocus(Graphics graphics, AbstractButton abstractButton, Rectangle rectangle, Rectangle rectangle2, Rectangle rectangle3) {
    }

    @Override // javax.swing.plaf.basic.BasicButtonUI
    protected void paintButtonPressed(Graphics graphics, AbstractButton abstractButton) {
        fillContentArea(graphics, abstractButton, this.selectColor);
    }

    protected void fillContentArea(Graphics graphics, AbstractButton abstractButton, Color color) {
        if (abstractButton.isContentAreaFilled()) {
            Insets margin = abstractButton.getMargin();
            Insets insets = abstractButton.getInsets();
            Dimension size = abstractButton.getSize();
            graphics.setColor(color);
            graphics.fillRect(insets.left - margin.left, insets.top - margin.top, (size.width - (insets.left - margin.left)) - (insets.right - margin.right), (size.height - (insets.top - margin.top)) - (insets.bottom - margin.bottom));
        }
    }
}
