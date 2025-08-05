package javax.swing.plaf.synth;

import java.awt.Graphics;
import java.beans.PropertyChangeEvent;
import javax.swing.JComponent;
import javax.swing.JRootPane;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicRootPaneUI;

/* loaded from: rt.jar:javax/swing/plaf/synth/SynthRootPaneUI.class */
public class SynthRootPaneUI extends BasicRootPaneUI implements SynthUI {
    private SynthStyle style;

    public static ComponentUI createUI(JComponent jComponent) {
        return new SynthRootPaneUI();
    }

    @Override // javax.swing.plaf.basic.BasicRootPaneUI
    protected void installDefaults(JRootPane jRootPane) {
        updateStyle(jRootPane);
    }

    @Override // javax.swing.plaf.basic.BasicRootPaneUI
    protected void uninstallDefaults(JRootPane jRootPane) {
        SynthContext context = getContext(jRootPane, 1);
        this.style.uninstallDefaults(context);
        context.dispose();
        this.style = null;
    }

    @Override // javax.swing.plaf.synth.SynthUI
    public SynthContext getContext(JComponent jComponent) {
        return getContext(jComponent, getComponentState(jComponent));
    }

    private SynthContext getContext(JComponent jComponent, int i2) {
        return SynthContext.getContext(jComponent, this.style, i2);
    }

    private int getComponentState(JComponent jComponent) {
        return SynthLookAndFeel.getComponentState(jComponent);
    }

    private void updateStyle(JComponent jComponent) {
        SynthContext context = getContext(jComponent, 1);
        SynthStyle synthStyle = this.style;
        this.style = SynthLookAndFeel.updateStyle(context, this);
        if (this.style != synthStyle && synthStyle != null) {
            uninstallKeyboardActions((JRootPane) jComponent);
            installKeyboardActions((JRootPane) jComponent);
        }
        context.dispose();
    }

    @Override // javax.swing.plaf.ComponentUI
    public void update(Graphics graphics, JComponent jComponent) {
        SynthContext context = getContext(jComponent);
        SynthLookAndFeel.update(context, graphics);
        context.getPainter().paintRootPaneBackground(context, graphics, 0, 0, jComponent.getWidth(), jComponent.getHeight());
        paint(context, graphics);
        context.dispose();
    }

    @Override // javax.swing.plaf.ComponentUI
    public void paint(Graphics graphics, JComponent jComponent) {
        SynthContext context = getContext(jComponent);
        paint(context, graphics);
        context.dispose();
    }

    protected void paint(SynthContext synthContext, Graphics graphics) {
    }

    @Override // javax.swing.plaf.synth.SynthUI
    public void paintBorder(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
        synthContext.getPainter().paintRootPaneBorder(synthContext, graphics, i2, i3, i4, i5);
    }

    @Override // javax.swing.plaf.basic.BasicRootPaneUI, java.beans.PropertyChangeListener
    public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
        if (SynthLookAndFeel.shouldUpdateStyle(propertyChangeEvent)) {
            updateStyle((JRootPane) propertyChangeEvent.getSource());
        }
        super.propertyChange(propertyChangeEvent);
    }
}
