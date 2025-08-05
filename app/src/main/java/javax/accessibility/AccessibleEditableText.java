package javax.accessibility;

import javax.swing.text.AttributeSet;

/* loaded from: rt.jar:javax/accessibility/AccessibleEditableText.class */
public interface AccessibleEditableText extends AccessibleText {
    void setTextContents(String str);

    void insertTextAtIndex(int i2, String str);

    String getTextRange(int i2, int i3);

    void delete(int i2, int i3);

    void cut(int i2, int i3);

    void paste(int i2);

    void replaceText(int i2, int i3, String str);

    void selectText(int i2, int i3);

    void setAttributes(int i2, int i3, AttributeSet attributeSet);
}
