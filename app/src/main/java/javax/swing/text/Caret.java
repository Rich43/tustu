package javax.swing.text;

import java.awt.Graphics;
import java.awt.Point;
import javax.swing.event.ChangeListener;

/* loaded from: rt.jar:javax/swing/text/Caret.class */
public interface Caret {
    void install(JTextComponent jTextComponent);

    void deinstall(JTextComponent jTextComponent);

    void paint(Graphics graphics);

    void addChangeListener(ChangeListener changeListener);

    void removeChangeListener(ChangeListener changeListener);

    boolean isVisible();

    void setVisible(boolean z2);

    boolean isSelectionVisible();

    void setSelectionVisible(boolean z2);

    void setMagicCaretPosition(Point point);

    Point getMagicCaretPosition();

    void setBlinkRate(int i2);

    int getBlinkRate();

    int getDot();

    int getMark();

    void setDot(int i2);

    void moveDot(int i2);
}
