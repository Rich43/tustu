package javax.swing.plaf.synth;

import java.awt.Graphics;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.JColorChooser;
import javax.swing.JComponent;
import javax.swing.colorchooser.AbstractColorChooserPanel;
import javax.swing.colorchooser.ColorChooserComponentFactory;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicColorChooserUI;

/* loaded from: rt.jar:javax/swing/plaf/synth/SynthColorChooserUI.class */
public class SynthColorChooserUI extends BasicColorChooserUI implements PropertyChangeListener, SynthUI {
    private SynthStyle style;

    public static ComponentUI createUI(JComponent jComponent) {
        return new SynthColorChooserUI();
    }

    @Override // javax.swing.plaf.basic.BasicColorChooserUI
    protected AbstractColorChooserPanel[] createDefaultChoosers() {
        SynthContext context = getContext(this.chooser, 1);
        AbstractColorChooserPanel[] defaultChooserPanels = (AbstractColorChooserPanel[]) context.getStyle().get(context, "ColorChooser.panels");
        context.dispose();
        if (defaultChooserPanels == null) {
            defaultChooserPanels = ColorChooserComponentFactory.getDefaultChooserPanels();
        }
        return defaultChooserPanels;
    }

    @Override // javax.swing.plaf.basic.BasicColorChooserUI
    protected void installDefaults() {
        super.installDefaults();
        updateStyle(this.chooser);
    }

    private void updateStyle(JComponent jComponent) {
        SynthContext context = getContext(jComponent, 1);
        this.style = SynthLookAndFeel.updateStyle(context, this);
        context.dispose();
    }

    @Override // javax.swing.plaf.basic.BasicColorChooserUI
    protected void uninstallDefaults() {
        SynthContext context = getContext(this.chooser, 1);
        this.style.uninstallDefaults(context);
        context.dispose();
        this.style = null;
        super.uninstallDefaults();
    }

    @Override // javax.swing.plaf.basic.BasicColorChooserUI
    protected void installListeners() {
        super.installListeners();
        this.chooser.addPropertyChangeListener(this);
    }

    @Override // javax.swing.plaf.basic.BasicColorChooserUI
    protected void uninstallListeners() {
        this.chooser.removePropertyChangeListener(this);
        super.uninstallListeners();
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
        context.getPainter().paintColorChooserBackground(context, graphics, 0, 0, jComponent.getWidth(), jComponent.getHeight());
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
        synthContext.getPainter().paintColorChooserBorder(synthContext, graphics, i2, i3, i4, i5);
    }

    @Override // java.beans.PropertyChangeListener
    public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
        if (SynthLookAndFeel.shouldUpdateStyle(propertyChangeEvent)) {
            updateStyle((JColorChooser) propertyChangeEvent.getSource());
        }
    }
}
