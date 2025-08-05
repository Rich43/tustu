package javax.swing.text.rtf;

import java.io.IOException;
import javax.swing.text.AttributeSet;
import javax.swing.text.MutableAttributeSet;

/* loaded from: rt.jar:javax/swing/text/rtf/RTFAttribute.class */
interface RTFAttribute {
    public static final int D_CHARACTER = 0;
    public static final int D_PARAGRAPH = 1;
    public static final int D_SECTION = 2;
    public static final int D_DOCUMENT = 3;
    public static final int D_META = 4;

    int domain();

    Object swingName();

    String rtfName();

    boolean set(MutableAttributeSet mutableAttributeSet);

    boolean set(MutableAttributeSet mutableAttributeSet, int i2);

    boolean setDefault(MutableAttributeSet mutableAttributeSet);

    boolean write(AttributeSet attributeSet, RTFGenerator rTFGenerator, boolean z2) throws IOException;

    boolean writeValue(Object obj, RTFGenerator rTFGenerator, boolean z2) throws IOException;
}
