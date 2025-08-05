package javax.swing.plaf.synth;

import java.awt.Graphics;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.beans.PropertyChangeEvent;
import javax.swing.JComponent;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicTextAreaUI;
import javax.swing.text.JTextComponent;

/* loaded from: rt.jar:javax/swing/plaf/synth/SynthTextAreaUI.class */
public class SynthTextAreaUI extends BasicTextAreaUI implements SynthUI {
    private Handler handler = new Handler();
    private SynthStyle style;

    public static ComponentUI createUI(JComponent jComponent) {
        return new SynthTextAreaUI();
    }

    @Override // javax.swing.plaf.basic.BasicTextAreaUI, javax.swing.plaf.basic.BasicTextUI
    protected void installDefaults() {
        super.installDefaults();
        updateStyle(getComponent());
        getComponent().addFocusListener(this.handler);
    }

    @Override // javax.swing.plaf.basic.BasicTextUI
    protected void uninstallDefaults() {
        SynthContext context = getContext(getComponent(), 1);
        getComponent().putClientProperty("caretAspectRatio", null);
        getComponent().removeFocusListener(this.handler);
        this.style.uninstallDefaults(context);
        context.dispose();
        this.style = null;
        super.uninstallDefaults();
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
        return getContext(jComponent, SynthLookAndFeel.getComponentState(jComponent));
    }

    private SynthContext getContext(JComponent jComponent, int i2) {
        return SynthContext.getContext(jComponent, this.style, i2);
    }

    @Override // javax.swing.plaf.basic.BasicTextUI, javax.swing.plaf.ComponentUI
    public void update(Graphics graphics, JComponent jComponent) {
        SynthContext context = getContext(jComponent);
        SynthLookAndFeel.update(context, graphics);
        context.getPainter().paintTextAreaBackground(context, graphics, 0, 0, jComponent.getWidth(), jComponent.getHeight());
        paint(context, graphics);
        context.dispose();
    }

    protected void paint(SynthContext synthContext, Graphics graphics) {
        super.paint(graphics, getComponent());
    }

    @Override // javax.swing.plaf.basic.BasicTextUI
    protected void paintBackground(Graphics graphics) {
    }

    @Override // javax.swing.plaf.synth.SynthUI
    public void paintBorder(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
        synthContext.getPainter().paintTextAreaBorder(synthContext, graphics, i2, i3, i4, i5);
    }

    @Override // javax.swing.plaf.basic.BasicTextAreaUI, javax.swing.plaf.basic.BasicTextUI
    protected void propertyChange(PropertyChangeEvent propertyChangeEvent) {
        if (SynthLookAndFeel.shouldUpdateStyle(propertyChangeEvent)) {
            updateStyle((JTextComponent) propertyChangeEvent.getSource());
        }
        super.propertyChange(propertyChangeEvent);
    }

    /* loaded from: rt.jar:javax/swing/plaf/synth/SynthTextAreaUI$Handler.class */
    private final class Handler implements FocusListener {
        private Handler() {
        }

        @Override // java.awt.event.FocusListener
        public void focusGained(FocusEvent focusEvent) {
            SynthTextAreaUI.this.getComponent().repaint();
        }

        @Override // java.awt.event.FocusListener
        public void focusLost(FocusEvent focusEvent) {
            SynthTextAreaUI.this.getComponent().repaint();
        }
    }
}
