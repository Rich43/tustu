package javax.print.attribute;

import java.io.Serializable;

/* loaded from: rt.jar:javax/print/attribute/HashPrintServiceAttributeSet.class */
public class HashPrintServiceAttributeSet extends HashAttributeSet implements PrintServiceAttributeSet, Serializable {
    private static final long serialVersionUID = 6642904616179203070L;

    public HashPrintServiceAttributeSet() {
        super((Class<?>) PrintServiceAttribute.class);
    }

    public HashPrintServiceAttributeSet(PrintServiceAttribute printServiceAttribute) {
        super(printServiceAttribute, (Class<?>) PrintServiceAttribute.class);
    }

    public HashPrintServiceAttributeSet(PrintServiceAttribute[] printServiceAttributeArr) {
        super(printServiceAttributeArr, (Class<?>) PrintServiceAttribute.class);
    }

    public HashPrintServiceAttributeSet(PrintServiceAttributeSet printServiceAttributeSet) {
        super(printServiceAttributeSet, (Class<?>) PrintServiceAttribute.class);
    }
}
