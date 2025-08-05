package javax.swing.plaf.synth;

import java.awt.Graphics;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.JComponent;
import javax.swing.JMenuBar;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.UIResource;
import javax.swing.plaf.basic.BasicMenuBarUI;

/* loaded from: rt.jar:javax/swing/plaf/synth/SynthMenuBarUI.class */
public class SynthMenuBarUI extends BasicMenuBarUI implements PropertyChangeListener, SynthUI {
    private SynthStyle style;

    public static ComponentUI createUI(JComponent jComponent) {
        return new SynthMenuBarUI();
    }

    @Override // javax.swing.plaf.basic.BasicMenuBarUI
    protected void installDefaults() {
        if (this.menuBar.getLayout() == null || (this.menuBar.getLayout() instanceof UIResource)) {
            this.menuBar.setLayout(new SynthMenuLayout(this.menuBar, 2));
        }
        updateStyle(this.menuBar);
    }

    @Override // javax.swing.plaf.basic.BasicMenuBarUI
    protected void installListeners() {
        super.installListeners();
        this.menuBar.addPropertyChangeListener(this);
    }

    private void updateStyle(JMenuBar jMenuBar) {
        SynthContext context = getContext(jMenuBar, 1);
        SynthStyle synthStyle = this.style;
        this.style = SynthLookAndFeel.updateStyle(context, this);
        if (this.style != synthStyle && synthStyle != null) {
            uninstallKeyboardActions();
            installKeyboardActions();
        }
        context.dispose();
    }

    @Override // javax.swing.plaf.basic.BasicMenuBarUI
    protected void uninstallDefaults() {
        SynthContext context = getContext(this.menuBar, 1);
        this.style.uninstallDefaults(context);
        context.dispose();
        this.style = null;
    }

    @Override // javax.swing.plaf.basic.BasicMenuBarUI
    protected void uninstallListeners() {
        super.uninstallListeners();
        this.menuBar.removePropertyChangeListener(this);
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
        context.getPainter().paintMenuBarBackground(context, graphics, 0, 0, jComponent.getWidth(), jComponent.getHeight());
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
        synthContext.getPainter().paintMenuBarBorder(synthContext, graphics, i2, i3, i4, i5);
    }

    @Override // java.beans.PropertyChangeListener
    public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
        if (SynthLookAndFeel.shouldUpdateStyle(propertyChangeEvent)) {
            updateStyle((JMenuBar) propertyChangeEvent.getSource());
        }
    }
}
