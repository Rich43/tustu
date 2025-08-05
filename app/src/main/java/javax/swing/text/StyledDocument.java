package javax.swing.text;

import java.awt.Color;
import java.awt.Font;

/* loaded from: rt.jar:javax/swing/text/StyledDocument.class */
public interface StyledDocument extends Document {
    Style addStyle(String str, Style style);

    void removeStyle(String str);

    Style getStyle(String str);

    void setCharacterAttributes(int i2, int i3, AttributeSet attributeSet, boolean z2);

    void setParagraphAttributes(int i2, int i3, AttributeSet attributeSet, boolean z2);

    void setLogicalStyle(int i2, Style style);

    Style getLogicalStyle(int i2);

    Element getParagraphElement(int i2);

    Element getCharacterElement(int i2);

    Color getForeground(AttributeSet attributeSet);

    Color getBackground(AttributeSet attributeSet);

    Font getFont(AttributeSet attributeSet);
}
