package javax.swing.plaf.synth;

import java.awt.Graphics;
import java.beans.PropertyChangeEvent;
import javax.swing.JComponent;
import javax.swing.JEditorPane;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicEditorPaneUI;
import javax.swing.text.JTextComponent;

/* loaded from: rt.jar:javax/swing/plaf/synth/SynthEditorPaneUI.class */
public class SynthEditorPaneUI extends BasicEditorPaneUI implements SynthUI {
    private SynthStyle style;
    private Boolean localTrue = Boolean.TRUE;

    public static ComponentUI createUI(JComponent jComponent) {
        return new SynthEditorPaneUI();
    }

    @Override // javax.swing.plaf.basic.BasicTextUI
    protected void installDefaults() {
        super.installDefaults();
        JTextComponent component = getComponent();
        if (component.getClientProperty(JEditorPane.HONOR_DISPLAY_PROPERTIES) == null) {
            component.putClientProperty(JEditorPane.HONOR_DISPLAY_PROPERTIES, this.localTrue);
        }
        updateStyle(getComponent());
    }

    @Override // javax.swing.plaf.basic.BasicTextUI
    protected void uninstallDefaults() {
        SynthContext context = getContext(getComponent(), 1);
        JTextComponent component = getComponent();
        component.putClientProperty("caretAspectRatio", null);
        this.style.uninstallDefaults(context);
        context.dispose();
        this.style = null;
        if (component.getClientProperty(JEditorPane.HONOR_DISPLAY_PROPERTIES) == this.localTrue) {
            component.putClientProperty(JEditorPane.HONOR_DISPLAY_PROPERTIES, Boolean.FALSE);
        }
        super.uninstallDefaults();
    }

    @Override // javax.swing.plaf.basic.BasicEditorPaneUI, javax.swing.plaf.basic.BasicTextUI
    protected void propertyChange(PropertyChangeEvent propertyChangeEvent) {
        if (SynthLookAndFeel.shouldUpdateStyle(propertyChangeEvent)) {
            updateStyle((JTextComponent) propertyChangeEvent.getSource());
        }
        super.propertyChange(propertyChangeEvent);
    }

    private void updateStyle(JTextComponent jTextComponent) {
        SynthContext context = getContext(jTextComponent, 1);
        SynthStyle synthStyle = this.style;
        this.style = SynthLookAndFeel.updateStyle(context, this);
        if (this.style != synthStyle) {
            SynthTextFieldUI.updateStyle(jTextComponent, context, getPropertyPrefix());
            if (synthStyle != null) {
                uninstallKeyboardActions();
                installKeyboardActions();
            }
        }
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

    @Override // javax.swing.plaf.basic.BasicTextUI, javax.swing.plaf.ComponentUI
    public void update(Graphics graphics, JComponent jComponent) {
        SynthContext context = getContext(jComponent);
        SynthLookAndFeel.update(context, graphics);
        paintBackground(context, graphics, jComponent);
        paint(context, graphics);
        context.dispose();
    }

    protected void paint(SynthContext synthContext, Graphics graphics) {
        super.paint(graphics, getComponent());
    }

    @Override // javax.swing.plaf.basic.BasicTextUI
    protected void paintBackground(Graphics graphics) {
    }

    void paintBackground(SynthContext synthContext, Graphics graphics, JComponent jComponent) {
        synthContext.getPainter().paintEditorPaneBackground(synthContext, graphics, 0, 0, jComponent.getWidth(), jComponent.getHeight());
    }

    @Override // javax.swing.plaf.synth.SynthUI
    public void paintBorder(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
        synthContext.getPainter().paintEditorPaneBorder(synthContext, graphics, i2, i3, i4, i5);
    }
}
