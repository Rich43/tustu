package javax.accessibility;

import java.awt.Point;
import java.awt.Rectangle;
import javax.swing.text.AttributeSet;

/* loaded from: rt.jar:javax/accessibility/AccessibleText.class */
public interface AccessibleText {
    public static final int CHARACTER = 1;
    public static final int WORD = 2;
    public static final int SENTENCE = 3;

    int getIndexAtPoint(Point point);

    Rectangle getCharacterBounds(int i2);

    int getCharCount();

    int getCaretPosition();

    String getAtIndex(int i2, int i3);

    String getAfterIndex(int i2, int i3);

    String getBeforeIndex(int i2, int i3);

    AttributeSet getCharacterAttribute(int i2);

    int getSelectionStart();

    int getSelectionEnd();

    String getSelectedText();
}
