package javax.swing.text;

import java.awt.Container;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.Shape;
import javax.swing.JPasswordField;
import javax.swing.text.Position;
import sun.swing.SwingUtilities2;

/* loaded from: rt.jar:javax/swing/text/PasswordView.class */
public class PasswordView extends FieldView {
    static char[] ONE = new char[1];

    public PasswordView(Element element) {
        super(element);
    }

    @Override // javax.swing.text.PlainView
    protected int drawUnselectedText(Graphics graphics, int i2, int i3, int i4, int i5) throws BadLocationException {
        Container container = getContainer();
        if (container instanceof JPasswordField) {
            JPasswordField jPasswordField = (JPasswordField) container;
            if (!jPasswordField.echoCharIsSet()) {
                return super.drawUnselectedText(graphics, i2, i3, i4, i5);
            }
            if (jPasswordField.isEnabled()) {
                graphics.setColor(jPasswordField.getForeground());
            } else {
                graphics.setColor(jPasswordField.getDisabledTextColor());
            }
            char echoChar = jPasswordField.getEchoChar();
            int i6 = i5 - i4;
            for (int i7 = 0; i7 < i6; i7++) {
                i2 = drawEchoCharacter(graphics, i2, i3, echoChar);
            }
        }
        return i2;
    }

    @Override // javax.swing.text.PlainView
    protected int drawSelectedText(Graphics graphics, int i2, int i3, int i4, int i5) throws BadLocationException {
        graphics.setColor(this.selected);
        Container container = getContainer();
        if (container instanceof JPasswordField) {
            JPasswordField jPasswordField = (JPasswordField) container;
            if (!jPasswordField.echoCharIsSet()) {
                return super.drawSelectedText(graphics, i2, i3, i4, i5);
            }
            char echoChar = jPasswordField.getEchoChar();
            int i6 = i5 - i4;
            for (int i7 = 0; i7 < i6; i7++) {
                i2 = drawEchoCharacter(graphics, i2, i3, echoChar);
            }
        }
        return i2;
    }

    protected int drawEchoCharacter(Graphics graphics, int i2, int i3, char c2) {
        ONE[0] = c2;
        SwingUtilities2.drawChars(Utilities.getJComponent(this), graphics, ONE, 0, 1, i2, i3);
        return i2 + graphics.getFontMetrics().charWidth(c2);
    }

    @Override // javax.swing.text.FieldView, javax.swing.text.PlainView, javax.swing.text.View
    public Shape modelToView(int i2, Shape shape, Position.Bias bias) throws BadLocationException {
        Container container = getContainer();
        if (container instanceof JPasswordField) {
            JPasswordField jPasswordField = (JPasswordField) container;
            if (!jPasswordField.echoCharIsSet()) {
                return super.modelToView(i2, shape, bias);
            }
            char echoChar = jPasswordField.getEchoChar();
            FontMetrics fontMetrics = jPasswordField.getFontMetrics(jPasswordField.getFont());
            Rectangle bounds = adjustAllocation(shape).getBounds();
            bounds.f12372x += (i2 - getStartOffset()) * fontMetrics.charWidth(echoChar);
            bounds.width = 1;
            return bounds;
        }
        return null;
    }

    @Override // javax.swing.text.FieldView, javax.swing.text.PlainView, javax.swing.text.View
    public int viewToModel(float f2, float f3, Shape shape, Position.Bias[] biasArr) {
        biasArr[0] = Position.Bias.Forward;
        int length = 0;
        Container container = getContainer();
        if (container instanceof JPasswordField) {
            JPasswordField jPasswordField = (JPasswordField) container;
            if (!jPasswordField.echoCharIsSet()) {
                return super.viewToModel(f2, f3, shape, biasArr);
            }
            int iCharWidth = jPasswordField.getFontMetrics(jPasswordField.getFont()).charWidth(jPasswordField.getEchoChar());
            Shape shapeAdjustAllocation = adjustAllocation(shape);
            length = iCharWidth > 0 ? (((int) f2) - (shapeAdjustAllocation instanceof Rectangle ? (Rectangle) shapeAdjustAllocation : shapeAdjustAllocation.getBounds()).f12372x) / iCharWidth : Integer.MAX_VALUE;
            if (length < 0) {
                length = 0;
            } else if (length > getStartOffset() + getDocument().getLength()) {
                length = getDocument().getLength() - getStartOffset();
            }
        }
        return getStartOffset() + length;
    }

    @Override // javax.swing.text.FieldView, javax.swing.text.PlainView, javax.swing.text.View
    public float getPreferredSpan(int i2) {
        switch (i2) {
            case 0:
                Container container = getContainer();
                if (container instanceof JPasswordField) {
                    JPasswordField jPasswordField = (JPasswordField) container;
                    if (jPasswordField.echoCharIsSet()) {
                        char echoChar = jPasswordField.getEchoChar();
                        FontMetrics fontMetrics = jPasswordField.getFontMetrics(jPasswordField.getFont());
                        getDocument();
                        return fontMetrics.charWidth(echoChar) * getDocument().getLength();
                    }
                }
                break;
        }
        return super.getPreferredSpan(i2);
    }
}
