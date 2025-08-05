package javax.swing.plaf.synth;

import java.awt.Graphics;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.JComponent;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.ViewportUI;

/* loaded from: rt.jar:javax/swing/plaf/synth/SynthViewportUI.class */
public class SynthViewportUI extends ViewportUI implements PropertyChangeListener, SynthUI {
    private SynthStyle style;

    public static ComponentUI createUI(JComponent jComponent) {
        return new SynthViewportUI();
    }

    @Override // javax.swing.plaf.ComponentUI
    public void installUI(JComponent jComponent) {
        super.installUI(jComponent);
        installDefaults(jComponent);
        installListeners(jComponent);
    }

    @Override // javax.swing.plaf.ComponentUI
    public void uninstallUI(JComponent jComponent) {
        super.uninstallUI(jComponent);
        uninstallListeners(jComponent);
        uninstallDefaults(jComponent);
    }

    protected void installDefaults(JComponent jComponent) {
        updateStyle(jComponent);
    }

    private void updateStyle(JComponent jComponent) {
        SynthContext context = getContext(jComponent, 1);
        SynthStyle style = SynthLookAndFeel.getStyle(context.getComponent(), context.getRegion());
        SynthStyle style2 = context.getStyle();
        if (style != style2) {
            if (style2 != null) {
                style2.uninstallDefaults(context);
            }
            context.setStyle(style);
            style.installDefaults(context);
        }
        this.style = style;
        context.dispose();
    }

    protected void installListeners(JComponent jComponent) {
        jComponent.addPropertyChangeListener(this);
    }

    protected void uninstallListeners(JComponent jComponent) {
        jComponent.removePropertyChangeListener(this);
    }

    protected void uninstallDefaults(JComponent jComponent) {
        SynthContext context = getContext(jComponent, 1);
        this.style.uninstallDefaults(context);
        context.dispose();
        this.style = null;
    }

    @Override // javax.swing.plaf.synth.SynthUI
    public SynthContext getContext(JComponent jComponent) {
        return getContext(jComponent, SynthLookAndFeel.getComponentState(jComponent));
    }

    private SynthContext getContext(JComponent jComponent, int i2) {
        return SynthContext.getContext(jComponent, this.style, i2);
    }

    private Region getRegion(JComponent jComponent) {
        return SynthLookAndFeel.getRegion(jComponent);
    }

    @Override // javax.swing.plaf.ComponentUI
    public void update(Graphics graphics, JComponent jComponent) {
        SynthContext context = getContext(jComponent);
        SynthLookAndFeel.update(context, graphics);
        context.getPainter().paintViewportBackground(context, graphics, 0, 0, jComponent.getWidth(), jComponent.getHeight());
        paint(context, graphics);
        context.dispose();
    }

    @Override // javax.swing.plaf.synth.SynthUI
    public void paintBorder(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
    }

    @Override // javax.swing.plaf.ComponentUI
    public void paint(Graphics graphics, JComponent jComponent) {
        SynthContext context = getContext(jComponent);
        paint(context, graphics);
        context.dispose();
    }

    protected void paint(SynthContext synthContext, Graphics graphics) {
    }

    @Override // java.beans.PropertyChangeListener
    public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
        if (SynthLookAndFeel.shouldUpdateStyle(propertyChangeEvent)) {
            updateStyle((JComponent) propertyChangeEvent.getSource());
        }
    }
}
