package javax.swing.plaf.synth;

import java.awt.Graphics;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicPanelUI;

/* loaded from: rt.jar:javax/swing/plaf/synth/SynthPanelUI.class */
public class SynthPanelUI extends BasicPanelUI implements PropertyChangeListener, SynthUI {
    private SynthStyle style;

    public static ComponentUI createUI(JComponent jComponent) {
        return new SynthPanelUI();
    }

    @Override // javax.swing.plaf.basic.BasicPanelUI, javax.swing.plaf.ComponentUI
    public void installUI(JComponent jComponent) {
        super.installUI(jComponent);
        installListeners((JPanel) jComponent);
    }

    @Override // javax.swing.plaf.basic.BasicPanelUI, javax.swing.plaf.ComponentUI
    public void uninstallUI(JComponent jComponent) {
        uninstallListeners((JPanel) jComponent);
        super.uninstallUI(jComponent);
    }

    protected void installListeners(JPanel jPanel) {
        jPanel.addPropertyChangeListener(this);
    }

    protected void uninstallListeners(JPanel jPanel) {
        jPanel.removePropertyChangeListener(this);
    }

    @Override // javax.swing.plaf.basic.BasicPanelUI
    protected void installDefaults(JPanel jPanel) {
        updateStyle(jPanel);
    }

    @Override // javax.swing.plaf.basic.BasicPanelUI
    protected void uninstallDefaults(JPanel jPanel) {
        SynthContext context = getContext(jPanel, 1);
        this.style.uninstallDefaults(context);
        context.dispose();
        this.style = null;
    }

    private void updateStyle(JPanel jPanel) {
        SynthContext context = getContext(jPanel, 1);
        this.style = SynthLookAndFeel.updateStyle(context, this);
        context.dispose();
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

    @Override // javax.swing.plaf.ComponentUI
    public void update(Graphics graphics, JComponent jComponent) {
        SynthContext context = getContext(jComponent);
        SynthLookAndFeel.update(context, graphics);
        context.getPainter().paintPanelBackground(context, graphics, 0, 0, jComponent.getWidth(), jComponent.getHeight());
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
        synthContext.getPainter().paintPanelBorder(synthContext, graphics, i2, i3, i4, i5);
    }

    @Override // java.beans.PropertyChangeListener
    public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
        if (SynthLookAndFeel.shouldUpdateStyle(propertyChangeEvent)) {
            updateStyle((JPanel) propertyChangeEvent.getSource());
        }
    }
}
