package com.sun.java.swing.plaf.windows;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.Shape;
import javax.swing.plaf.TextUI;
import javax.swing.plaf.UIResource;
import javax.swing.plaf.basic.BasicTextUI;
import javax.swing.text.BadLocationException;
import javax.swing.text.Caret;
import javax.swing.text.DefaultCaret;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Highlighter;
import javax.swing.text.JTextComponent;
import javax.swing.text.LayeredHighlighter;
import javax.swing.text.Position;
import javax.swing.text.View;

/* loaded from: rt.jar:com/sun/java/swing/plaf/windows/WindowsTextUI.class */
public abstract class WindowsTextUI extends BasicTextUI {
    static LayeredHighlighter.LayerPainter WindowsPainter = new WindowsHighlightPainter(null);

    @Override // javax.swing.plaf.basic.BasicTextUI
    protected Caret createCaret() {
        return new WindowsCaret();
    }

    /* loaded from: rt.jar:com/sun/java/swing/plaf/windows/WindowsTextUI$WindowsCaret.class */
    static class WindowsCaret extends DefaultCaret implements UIResource {
        WindowsCaret() {
        }

        @Override // javax.swing.text.DefaultCaret
        protected Highlighter.HighlightPainter getSelectionPainter() {
            return WindowsTextUI.WindowsPainter;
        }
    }

    /* loaded from: rt.jar:com/sun/java/swing/plaf/windows/WindowsTextUI$WindowsHighlightPainter.class */
    static class WindowsHighlightPainter extends DefaultHighlighter.DefaultHighlightPainter {
        WindowsHighlightPainter(Color color) {
            super(color);
        }

        @Override // javax.swing.text.DefaultHighlighter.DefaultHighlightPainter, javax.swing.text.Highlighter.HighlightPainter
        public void paint(Graphics graphics, int i2, int i3, Shape shape, JTextComponent jTextComponent) {
            Rectangle bounds = shape.getBounds();
            try {
                TextUI ui = jTextComponent.getUI();
                Rectangle rectangleModelToView = ui.modelToView(jTextComponent, i2);
                Rectangle rectangleModelToView2 = ui.modelToView(jTextComponent, i3);
                Color color = getColor();
                if (color == null) {
                    graphics.setColor(jTextComponent.getSelectionColor());
                } else {
                    graphics.setColor(color);
                }
                boolean z2 = false;
                boolean z3 = false;
                if (jTextComponent.isEditable()) {
                    int caretPosition = jTextComponent.getCaretPosition();
                    z2 = i2 == caretPosition;
                    z3 = i3 == caretPosition;
                }
                if (rectangleModelToView.f12373y == rectangleModelToView2.f12373y) {
                    Rectangle rectangleUnion = rectangleModelToView.union(rectangleModelToView2);
                    if (rectangleUnion.width > 0) {
                        if (z2) {
                            rectangleUnion.f12372x++;
                            rectangleUnion.width--;
                        } else if (z3) {
                            rectangleUnion.width--;
                        }
                    }
                    graphics.fillRect(rectangleUnion.f12372x, rectangleUnion.f12373y, rectangleUnion.width, rectangleUnion.height);
                } else {
                    int i4 = (bounds.f12372x + bounds.width) - rectangleModelToView.f12372x;
                    if (z2 && i4 > 0) {
                        rectangleModelToView.f12372x++;
                        i4--;
                    }
                    graphics.fillRect(rectangleModelToView.f12372x, rectangleModelToView.f12373y, i4, rectangleModelToView.height);
                    if (rectangleModelToView.f12373y + rectangleModelToView.height != rectangleModelToView2.f12373y) {
                        graphics.fillRect(bounds.f12372x, rectangleModelToView.f12373y + rectangleModelToView.height, bounds.width, rectangleModelToView2.f12373y - (rectangleModelToView.f12373y + rectangleModelToView.height));
                    }
                    if (z3 && rectangleModelToView2.f12372x > bounds.f12372x) {
                        rectangleModelToView2.f12372x--;
                    }
                    graphics.fillRect(bounds.f12372x, rectangleModelToView2.f12373y, rectangleModelToView2.f12372x - bounds.f12372x, rectangleModelToView2.height);
                }
            } catch (BadLocationException e2) {
            }
        }

        @Override // javax.swing.text.DefaultHighlighter.DefaultHighlightPainter, javax.swing.text.LayeredHighlighter.LayerPainter
        public Shape paintLayer(Graphics graphics, int i2, int i3, Shape shape, JTextComponent jTextComponent, View view) {
            Rectangle bounds;
            Color color = getColor();
            if (color == null) {
                graphics.setColor(jTextComponent.getSelectionColor());
            } else {
                graphics.setColor(color);
            }
            boolean z2 = false;
            boolean z3 = false;
            if (jTextComponent.isEditable()) {
                int caretPosition = jTextComponent.getCaretPosition();
                z2 = i2 == caretPosition;
                z3 = i3 == caretPosition;
            }
            if (i2 == view.getStartOffset() && i3 == view.getEndOffset()) {
                if (shape instanceof Rectangle) {
                    bounds = (Rectangle) shape;
                } else {
                    bounds = shape.getBounds();
                }
                if (z2 && bounds.width > 0) {
                    graphics.fillRect(bounds.f12372x + 1, bounds.f12373y, bounds.width - 1, bounds.height);
                } else if (z3 && bounds.width > 0) {
                    graphics.fillRect(bounds.f12372x, bounds.f12373y, bounds.width - 1, bounds.height);
                } else {
                    graphics.fillRect(bounds.f12372x, bounds.f12373y, bounds.width, bounds.height);
                }
                return bounds;
            }
            try {
                Shape shapeModelToView = view.modelToView(i2, Position.Bias.Forward, i3, Position.Bias.Backward, shape);
                Rectangle bounds2 = shapeModelToView instanceof Rectangle ? (Rectangle) shapeModelToView : shapeModelToView.getBounds();
                if (z2 && bounds2.width > 0) {
                    graphics.fillRect(bounds2.f12372x + 1, bounds2.f12373y, bounds2.width - 1, bounds2.height);
                } else if (z3 && bounds2.width > 0) {
                    graphics.fillRect(bounds2.f12372x, bounds2.f12373y, bounds2.width - 1, bounds2.height);
                } else {
                    graphics.fillRect(bounds2.f12372x, bounds2.f12373y, bounds2.width, bounds2.height);
                }
                return bounds2;
            } catch (BadLocationException e2) {
                return null;
            }
        }
    }
}
