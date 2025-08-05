package javax.swing.plaf;

import java.awt.Point;
import java.awt.Rectangle;
import javax.swing.text.BadLocationException;
import javax.swing.text.EditorKit;
import javax.swing.text.JTextComponent;
import javax.swing.text.Position;
import javax.swing.text.View;

/* loaded from: rt.jar:javax/swing/plaf/TextUI.class */
public abstract class TextUI extends ComponentUI {
    public abstract Rectangle modelToView(JTextComponent jTextComponent, int i2) throws BadLocationException;

    public abstract Rectangle modelToView(JTextComponent jTextComponent, int i2, Position.Bias bias) throws BadLocationException;

    public abstract int viewToModel(JTextComponent jTextComponent, Point point);

    public abstract int viewToModel(JTextComponent jTextComponent, Point point, Position.Bias[] biasArr);

    public abstract int getNextVisualPositionFrom(JTextComponent jTextComponent, int i2, Position.Bias bias, int i3, Position.Bias[] biasArr) throws BadLocationException;

    public abstract void damageRange(JTextComponent jTextComponent, int i2, int i3);

    public abstract void damageRange(JTextComponent jTextComponent, int i2, int i3, Position.Bias bias, Position.Bias bias2);

    public abstract EditorKit getEditorKit(JTextComponent jTextComponent);

    public abstract View getRootView(JTextComponent jTextComponent);

    public String getToolTipText(JTextComponent jTextComponent, Point point) {
        return null;
    }
}
