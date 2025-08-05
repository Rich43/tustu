package de.muntjak.tinylookandfeel;

import de.muntjak.tinylookandfeel.util.DrawRoutines;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Insets;
import javax.swing.JComponent;
import javax.swing.JTextField;
import javax.swing.border.AbstractBorder;
import javax.swing.plaf.metal.MetalComboBoxEditor;

/* loaded from: tinylaf.jar:de/muntjak/tinylookandfeel/TinyComboBoxEditor.class */
public class TinyComboBoxEditor extends MetalComboBoxEditor {

    /* loaded from: tinylaf.jar:de/muntjak/tinylookandfeel/TinyComboBoxEditor$EditorBorder.class */
    class EditorBorder extends AbstractBorder {
        private final TinyComboBoxEditor this$0;

        EditorBorder(TinyComboBoxEditor tinyComboBoxEditor) {
            this.this$0 = tinyComboBoxEditor;
        }

        @Override // javax.swing.border.AbstractBorder, javax.swing.border.Border
        public Insets getBorderInsets(Component component) {
            return new Insets(1, Theme.comboInsets.left + 1, 1, 0);
        }

        @Override // javax.swing.border.AbstractBorder, javax.swing.border.Border
        public void paintBorder(Component component, Graphics graphics, int i2, int i3, int i4, int i5) {
            JComponent jComponent = (JComponent) this.this$0.editor.getParent();
            if (jComponent == null || jComponent.getBorder() != null) {
                this.this$0.drawXpBorder(component, graphics, i2, i3, i4, i5);
            }
        }
    }

    /* loaded from: tinylaf.jar:de/muntjak/tinylookandfeel/TinyComboBoxEditor$UIResource.class */
    public static class UIResource extends TinyComboBoxEditor implements javax.swing.plaf.UIResource {
    }

    public TinyComboBoxEditor() {
        this.editor = new JTextField(this, "", 9) { // from class: de.muntjak.tinylookandfeel.TinyComboBoxEditor.1
            private final TinyComboBoxEditor this$0;

            {
                this.this$0 = this;
            }

            @Override // javax.swing.text.JTextComponent
            public void setText(String str) {
                if (getText().equals(str)) {
                    return;
                }
                super.setText(str);
            }
        };
        this.editor.setBorder(new EditorBorder(this));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void drawXpBorder(Component component, Graphics graphics, int i2, int i3, int i4, int i5) {
        if (component != null && component.getParent() != null && component.getParent().getParent() != null) {
            graphics.setColor(component.getParent().getParent().getBackground());
        }
        graphics.drawLine(i2, i3, (i2 + i4) - 1, i3);
        graphics.drawLine(i2, i3, i2, (i3 + i5) - 1);
        graphics.drawLine(i2, (i3 + i5) - 1, (i2 + i4) - 1, (i3 + i5) - 1);
        if (component.isEnabled()) {
            DrawRoutines.drawEditableComboBorder(graphics, Theme.comboBorderColor.getColor(), 0, 0, i4, i5);
        } else {
            DrawRoutines.drawEditableComboBorder(graphics, Theme.comboBorderDisabledColor.getColor(), 0, 0, i4, i5);
        }
    }
}
