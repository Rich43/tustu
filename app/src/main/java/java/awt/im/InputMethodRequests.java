package java.awt.im;

import java.awt.Rectangle;
import java.awt.font.TextHitInfo;
import java.text.AttributedCharacterIterator;

/* loaded from: rt.jar:java/awt/im/InputMethodRequests.class */
public interface InputMethodRequests {
    Rectangle getTextLocation(TextHitInfo textHitInfo);

    TextHitInfo getLocationOffset(int i2, int i3);

    int getInsertPositionOffset();

    AttributedCharacterIterator getCommittedText(int i2, int i3, AttributedCharacterIterator.Attribute[] attributeArr);

    int getCommittedTextLength();

    AttributedCharacterIterator cancelLatestCommittedText(AttributedCharacterIterator.Attribute[] attributeArr);

    AttributedCharacterIterator getSelectedText(AttributedCharacterIterator.Attribute[] attributeArr);
}
