package javax.accessibility;

import java.awt.Rectangle;

/* loaded from: rt.jar:javax/accessibility/AccessibleExtendedText.class */
public interface AccessibleExtendedText {
    public static final int LINE = 4;
    public static final int ATTRIBUTE_RUN = 5;

    String getTextRange(int i2, int i3);

    AccessibleTextSequence getTextSequenceAt(int i2, int i3);

    AccessibleTextSequence getTextSequenceAfter(int i2, int i3);

    AccessibleTextSequence getTextSequenceBefore(int i2, int i3);

    Rectangle getTextBounds(int i2, int i3);
}
