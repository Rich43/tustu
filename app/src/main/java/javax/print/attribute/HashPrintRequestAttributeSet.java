package javax.print.attribute;

import java.io.Serializable;

/* loaded from: rt.jar:javax/print/attribute/HashPrintRequestAttributeSet.class */
public class HashPrintRequestAttributeSet extends HashAttributeSet implements PrintRequestAttributeSet, Serializable {
    private static final long serialVersionUID = 2364756266107751933L;

    public HashPrintRequestAttributeSet() {
        super((Class<?>) PrintRequestAttribute.class);
    }

    public HashPrintRequestAttributeSet(PrintRequestAttribute printRequestAttribute) {
        super(printRequestAttribute, (Class<?>) PrintRequestAttribute.class);
    }

    public HashPrintRequestAttributeSet(PrintRequestAttribute[] printRequestAttributeArr) {
        super(printRequestAttributeArr, (Class<?>) PrintRequestAttribute.class);
    }

    public HashPrintRequestAttributeSet(PrintRequestAttributeSet printRequestAttributeSet) {
        super(printRequestAttributeSet, (Class<?>) PrintRequestAttribute.class);
    }
}
