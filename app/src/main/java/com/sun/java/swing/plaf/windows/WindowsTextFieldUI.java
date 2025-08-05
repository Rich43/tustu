package com.sun.java.swing.plaf.windows;

import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Rectangle;
import javax.swing.BoundedRangeModel;
import javax.swing.JComponent;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.TextUI;
import javax.swing.plaf.UIResource;
import javax.swing.plaf.basic.BasicTextFieldUI;
import javax.swing.text.BadLocationException;
import javax.swing.text.Caret;
import javax.swing.text.DefaultCaret;
import javax.swing.text.Highlighter;
import javax.swing.text.Position;

/* loaded from: rt.jar:com/sun/java/swing/plaf/windows/WindowsTextFieldUI.class */
public class WindowsTextFieldUI extends BasicTextFieldUI {
    public static ComponentUI createUI(JComponent jComponent) {
        return new WindowsTextFieldUI();
    }

    @Override // javax.swing.plaf.basic.BasicTextUI
    protected void paintBackground(Graphics graphics) {
        super.paintBackground(graphics);
    }

    @Override // javax.swing.plaf.basic.BasicTextUI
    protected Caret createCaret() {
        return new WindowsFieldCaret();
    }

    /* loaded from: rt.jar:com/sun/java/swing/plaf/windows/WindowsTextFieldUI$WindowsFieldCaret.class */
    static class WindowsFieldCaret extends DefaultCaret implements UIResource {
        @Override // javax.swing.text.DefaultCaret
        protected void adjustVisibility(Rectangle rectangle) {
            SwingUtilities.invokeLater(new SafeScroller(rectangle));
        }

        @Override // javax.swing.text.DefaultCaret
        protected Highlighter.HighlightPainter getSelectionPainter() {
            return WindowsTextUI.WindowsPainter;
        }

        /* loaded from: rt.jar:com/sun/java/swing/plaf/windows/WindowsTextFieldUI$WindowsFieldCaret$SafeScroller.class */
        private class SafeScroller implements Runnable {

            /* renamed from: r, reason: collision with root package name */
            private Rectangle f11842r;

            SafeScroller(Rectangle rectangle) {
                this.f11842r = rectangle;
            }

            @Override // java.lang.Runnable
            public void run() {
                JTextField jTextField = (JTextField) WindowsFieldCaret.this.getComponent();
                if (jTextField != null) {
                    TextUI ui = jTextField.getUI();
                    int dot = WindowsFieldCaret.this.getDot();
                    Position.Bias bias = Position.Bias.Forward;
                    Rectangle rectangleModelToView = null;
                    try {
                        rectangleModelToView = ui.modelToView(jTextField, dot, bias);
                    } catch (BadLocationException e2) {
                    }
                    Insets insets = jTextField.getInsets();
                    BoundedRangeModel horizontalVisibility = jTextField.getHorizontalVisibility();
                    int value = (this.f11842r.f12372x + horizontalVisibility.getValue()) - insets.left;
                    int extent = horizontalVisibility.getExtent() / 4;
                    if (this.f11842r.f12372x < insets.left) {
                        horizontalVisibility.setValue(value - extent);
                    } else if (this.f11842r.f12372x + this.f11842r.width > insets.left + horizontalVisibility.getExtent()) {
                        horizontalVisibility.setValue(value - (3 * extent));
                    }
                    if (rectangleModelToView != null) {
                        try {
                            Rectangle rectangleModelToView2 = ui.modelToView(jTextField, dot, bias);
                            if (rectangleModelToView2 != null && !rectangleModelToView2.equals(rectangleModelToView)) {
                                WindowsFieldCaret.this.damage(rectangleModelToView2);
                            }
                        } catch (BadLocationException e3) {
                        }
                    }
                }
            }
        }
    }
}
