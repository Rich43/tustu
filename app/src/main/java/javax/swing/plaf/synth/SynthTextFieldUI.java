package javax.swing.plaf.synth;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.beans.PropertyChangeEvent;
import javax.swing.JComponent;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.UIResource;
import javax.swing.plaf.basic.BasicTextFieldUI;
import javax.swing.text.Caret;
import javax.swing.text.JTextComponent;

/* loaded from: rt.jar:javax/swing/plaf/synth/SynthTextFieldUI.class */
public class SynthTextFieldUI extends BasicTextFieldUI implements SynthUI {
    private Handler handler = new Handler();
    private SynthStyle style;

    public static ComponentUI createUI(JComponent jComponent) {
        return new SynthTextFieldUI();
    }

    private void updateStyle(JTextComponent jTextComponent) {
        SynthContext context = getContext(jTextComponent, 1);
        SynthStyle synthStyle = this.style;
        this.style = SynthLookAndFeel.updateStyle(context, this);
        if (this.style != synthStyle) {
            updateStyle(jTextComponent, context, getPropertyPrefix());
            if (synthStyle != null) {
                uninstallKeyboardActions();
                installKeyboardActions();
            }
        }
        context.dispose();
    }

    static void updateStyle(JTextComponent jTextComponent, SynthContext synthContext, String str) {
        Color colorForState;
        Object obj;
        SynthStyle style = synthContext.getStyle();
        Color caretColor = jTextComponent.getCaretColor();
        if (caretColor == null || (caretColor instanceof UIResource)) {
            jTextComponent.setCaretColor((Color) style.get(synthContext, str + ".caretForeground"));
        }
        Color foreground = jTextComponent.getForeground();
        if ((foreground == null || (foreground instanceof UIResource)) && (colorForState = style.getColorForState(synthContext, ColorType.TEXT_FOREGROUND)) != null) {
            jTextComponent.setForeground(colorForState);
        }
        Object obj2 = style.get(synthContext, str + ".caretAspectRatio");
        if (obj2 instanceof Number) {
            jTextComponent.putClientProperty("caretAspectRatio", obj2);
        }
        synthContext.setComponentState(768);
        Color selectionColor = jTextComponent.getSelectionColor();
        if (selectionColor == null || (selectionColor instanceof UIResource)) {
            jTextComponent.setSelectionColor(style.getColor(synthContext, ColorType.TEXT_BACKGROUND));
        }
        Color selectedTextColor = jTextComponent.getSelectedTextColor();
        if (selectedTextColor == null || (selectedTextColor instanceof UIResource)) {
            jTextComponent.setSelectedTextColor(style.getColor(synthContext, ColorType.TEXT_FOREGROUND));
        }
        synthContext.setComponentState(8);
        Color disabledTextColor = jTextComponent.getDisabledTextColor();
        if (disabledTextColor == null || (disabledTextColor instanceof UIResource)) {
            jTextComponent.setDisabledTextColor(style.getColor(synthContext, ColorType.TEXT_FOREGROUND));
        }
        Insets margin = jTextComponent.getMargin();
        if (margin == null || (margin instanceof UIResource)) {
            Insets insets = (Insets) style.get(synthContext, str + ".margin");
            if (insets == null) {
                insets = SynthLookAndFeel.EMPTY_UIRESOURCE_INSETS;
            }
            jTextComponent.setMargin(insets);
        }
        Caret caret = jTextComponent.getCaret();
        if ((caret instanceof UIResource) && (obj = style.get(synthContext, str + ".caretBlinkRate")) != null && (obj instanceof Integer)) {
            caret.setBlinkRate(((Integer) obj).intValue());
        }
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
        paintBackground(context, graphics, jComponent);
        paint(context, graphics);
        context.dispose();
    }

    protected void paint(SynthContext synthContext, Graphics graphics) {
        super.paint(graphics, getComponent());
    }

    void paintBackground(SynthContext synthContext, Graphics graphics, JComponent jComponent) {
        synthContext.getPainter().paintTextFieldBackground(synthContext, graphics, 0, 0, jComponent.getWidth(), jComponent.getHeight());
    }

    public void paintBorder(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
        synthContext.getPainter().paintTextFieldBorder(synthContext, graphics, i2, i3, i4, i5);
    }

    @Override // javax.swing.plaf.basic.BasicTextUI
    protected void paintBackground(Graphics graphics) {
    }

    @Override // javax.swing.plaf.basic.BasicTextUI
    protected void propertyChange(PropertyChangeEvent propertyChangeEvent) {
        if (SynthLookAndFeel.shouldUpdateStyle(propertyChangeEvent)) {
            updateStyle((JTextComponent) propertyChangeEvent.getSource());
        }
        super.propertyChange(propertyChangeEvent);
    }

    @Override // javax.swing.plaf.basic.BasicTextUI
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

    /* loaded from: rt.jar:javax/swing/plaf/synth/SynthTextFieldUI$Handler.class */
    private final class Handler implements FocusListener {
        private Handler() {
        }

        @Override // java.awt.event.FocusListener
        public void focusGained(FocusEvent focusEvent) {
            SynthTextFieldUI.this.getComponent().repaint();
        }

        @Override // java.awt.event.FocusListener
        public void focusLost(FocusEvent focusEvent) {
            SynthTextFieldUI.this.getComponent().repaint();
        }
    }
}
