package javax.print.attribute;

import java.io.Serializable;

/* loaded from: rt.jar:javax/print/attribute/HashPrintJobAttributeSet.class */
public class HashPrintJobAttributeSet extends HashAttributeSet implements PrintJobAttributeSet, Serializable {
    private static final long serialVersionUID = -4204473656070350348L;

    public HashPrintJobAttributeSet() {
        super((Class<?>) PrintJobAttribute.class);
    }

    public HashPrintJobAttributeSet(PrintJobAttribute printJobAttribute) {
        super(printJobAttribute, (Class<?>) PrintJobAttribute.class);
    }

    public HashPrintJobAttributeSet(PrintJobAttribute[] printJobAttributeArr) {
        super(printJobAttributeArr, (Class<?>) PrintJobAttribute.class);
    }

    public HashPrintJobAttributeSet(PrintJobAttributeSet printJobAttributeSet) {
        super(printJobAttributeSet, (Class<?>) PrintJobAttribute.class);
    }
}
