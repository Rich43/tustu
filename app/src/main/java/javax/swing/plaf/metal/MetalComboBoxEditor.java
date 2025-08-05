package javax.swing.plaf.metal;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Insets;
import javax.swing.JTextField;
import javax.swing.border.AbstractBorder;
import javax.swing.plaf.basic.BasicComboBoxEditor;

/* loaded from: rt.jar:javax/swing/plaf/metal/MetalComboBoxEditor.class */
public class MetalComboBoxEditor extends BasicComboBoxEditor {
    protected static Insets editorBorderInsets = new Insets(2, 2, 2, 0);

    /* loaded from: rt.jar:javax/swing/plaf/metal/MetalComboBoxEditor$UIResource.class */
    public static class UIResource extends MetalComboBoxEditor implements javax.swing.plaf.UIResource {
    }

    public MetalComboBoxEditor() {
        this.editor = new JTextField("", 9) { // from class: javax.swing.plaf.metal.MetalComboBoxEditor.1
            @Override // javax.swing.text.JTextComponent
            public void setText(String str) {
                if (getText().equals(str)) {
                    return;
                }
                super.setText(str);
            }

            @Override // javax.swing.JTextField, javax.swing.JComponent, java.awt.Container, java.awt.Component
            public Dimension getPreferredSize() {
                Dimension preferredSize = super.getPreferredSize();
                preferredSize.height += 4;
                return preferredSize;
            }

            @Override // javax.swing.JComponent, java.awt.Container, java.awt.Component
            public Dimension getMinimumSize() {
                Dimension minimumSize = super.getMinimumSize();
                minimumSize.height += 4;
                return minimumSize;
            }
        };
        this.editor.setBorder(new EditorBorder());
    }

    /* loaded from: rt.jar:javax/swing/plaf/metal/MetalComboBoxEditor$EditorBorder.class */
    class EditorBorder extends AbstractBorder {
        EditorBorder() {
        }

        @Override // javax.swing.border.AbstractBorder, javax.swing.border.Border
        public void paintBorder(Component component, Graphics graphics, int i2, int i3, int i4, int i5) {
            graphics.translate(i2, i3);
            if (MetalLookAndFeel.usingOcean()) {
                graphics.setColor(MetalLookAndFeel.getControlDarkShadow());
                graphics.drawRect(0, 0, i4, i5 - 1);
                graphics.setColor(MetalLookAndFeel.getControlShadow());
                graphics.drawRect(1, 1, i4 - 2, i5 - 3);
            } else {
                graphics.setColor(MetalLookAndFeel.getControlDarkShadow());
                graphics.drawLine(0, 0, i4 - 1, 0);
                graphics.drawLine(0, 0, 0, i5 - 2);
                graphics.drawLine(0, i5 - 2, i4 - 1, i5 - 2);
                graphics.setColor(MetalLookAndFeel.getControlHighlight());
                graphics.drawLine(1, 1, i4 - 1, 1);
                graphics.drawLine(1, 1, 1, i5 - 1);
                graphics.drawLine(1, i5 - 1, i4 - 1, i5 - 1);
                graphics.setColor(MetalLookAndFeel.getControl());
                graphics.drawLine(1, i5 - 2, 1, i5 - 2);
            }
            graphics.translate(-i2, -i3);
        }

        @Override // javax.swing.border.AbstractBorder
        public Insets getBorderInsets(Component component, Insets insets) {
            insets.set(2, 2, 2, 0);
            return insets;
        }
    }
}
