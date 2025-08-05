package javax.print.attribute.standard;

import javax.print.attribute.Attribute;
import javax.print.attribute.IntegerSyntax;
import javax.print.attribute.PrintJobAttribute;

/* loaded from: rt.jar:javax/print/attribute/standard/NumberOfDocuments.class */
public final class NumberOfDocuments extends IntegerSyntax implements PrintJobAttribute {
    private static final long serialVersionUID = 7891881310684461097L;

    public NumberOfDocuments(int i2) {
        super(i2, 0, Integer.MAX_VALUE);
    }

    @Override // javax.print.attribute.IntegerSyntax
    public boolean equals(Object obj) {
        return super.equals(obj) && (obj instanceof NumberOfDocuments);
    }

    @Override // javax.print.attribute.Attribute
    public final Class<? extends Attribute> getCategory() {
        return NumberOfDocuments.class;
    }

    @Override // javax.print.attribute.Attribute
    public final String getName() {
        return "number-of-documents";
    }
}
