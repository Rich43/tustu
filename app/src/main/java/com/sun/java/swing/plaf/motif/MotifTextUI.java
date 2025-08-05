package com.sun.java.swing.plaf.motif;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.FocusEvent;
import javax.swing.KeyStroke;
import javax.swing.plaf.UIResource;
import javax.swing.text.BadLocationException;
import javax.swing.text.Caret;
import javax.swing.text.DefaultCaret;
import javax.swing.text.DefaultEditorKit;
import javax.swing.text.JTextComponent;

/* loaded from: rt.jar:com/sun/java/swing/plaf/motif/MotifTextUI.class */
public class MotifTextUI {
    static final JTextComponent.KeyBinding[] defaultBindings = {new JTextComponent.KeyBinding(KeyStroke.getKeyStroke(155, 2), DefaultEditorKit.copyAction), new JTextComponent.KeyBinding(KeyStroke.getKeyStroke(155, 1), DefaultEditorKit.pasteAction), new JTextComponent.KeyBinding(KeyStroke.getKeyStroke(127, 1), DefaultEditorKit.cutAction), new JTextComponent.KeyBinding(KeyStroke.getKeyStroke(37, 1), DefaultEditorKit.selectionBackwardAction), new JTextComponent.KeyBinding(KeyStroke.getKeyStroke(39, 1), DefaultEditorKit.selectionForwardAction)};

    public static Caret createCaret() {
        return new MotifCaret();
    }

    /* loaded from: rt.jar:com/sun/java/swing/plaf/motif/MotifTextUI$MotifCaret.class */
    public static class MotifCaret extends DefaultCaret implements UIResource {
        static final int IBeamOverhang = 2;

        @Override // javax.swing.text.DefaultCaret, java.awt.event.FocusListener
        public void focusGained(FocusEvent focusEvent) {
            super.focusGained(focusEvent);
            getComponent().repaint();
        }

        @Override // javax.swing.text.DefaultCaret, java.awt.event.FocusListener
        public void focusLost(FocusEvent focusEvent) {
            super.focusLost(focusEvent);
            getComponent().repaint();
        }

        @Override // javax.swing.text.DefaultCaret
        protected void damage(Rectangle rectangle) {
            if (rectangle != null) {
                this.f12372x = (rectangle.f12372x - 2) - 1;
                this.f12373y = rectangle.f12373y;
                this.width = rectangle.width + 4 + 3;
                this.height = rectangle.height;
                repaint();
            }
        }

        @Override // javax.swing.text.DefaultCaret, javax.swing.text.Caret
        public void paint(Graphics graphics) {
            if (isVisible()) {
                try {
                    JTextComponent component = getComponent();
                    Color caretColor = component.hasFocus() ? component.getCaretColor() : component.getDisabledTextColor();
                    Rectangle rectangleModelToView = component.getUI().modelToView(component, getDot());
                    int i2 = rectangleModelToView.f12372x - 2;
                    int i3 = rectangleModelToView.f12372x + 2;
                    int i4 = rectangleModelToView.f12373y + 1;
                    int i5 = (rectangleModelToView.f12373y + rectangleModelToView.height) - 2;
                    graphics.setColor(caretColor);
                    graphics.drawLine(rectangleModelToView.f12372x, i4, rectangleModelToView.f12372x, i5);
                    graphics.drawLine(i2, i4, i3, i4);
                    graphics.drawLine(i2, i5, i3, i5);
                } catch (BadLocationException e2) {
                }
            }
        }
    }
}
