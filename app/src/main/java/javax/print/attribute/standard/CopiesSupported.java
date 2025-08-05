package javax.print.attribute.standard;

import javax.print.attribute.Attribute;
import javax.print.attribute.SetOfIntegerSyntax;
import javax.print.attribute.SupportedValuesAttribute;

/* loaded from: rt.jar:javax/print/attribute/standard/CopiesSupported.class */
public final class CopiesSupported extends SetOfIntegerSyntax implements SupportedValuesAttribute {
    private static final long serialVersionUID = 6927711687034846001L;

    public CopiesSupported(int i2) {
        super(i2);
        if (i2 < 1) {
            throw new IllegalArgumentException("Copies value < 1 specified");
        }
    }

    public CopiesSupported(int i2, int i3) {
        super(i2, i3);
        if (i2 > i3) {
            throw new IllegalArgumentException("Null range specified");
        }
        if (i2 < 1) {
            throw new IllegalArgumentException("Copies value < 1 specified");
        }
    }

    @Override // javax.print.attribute.SetOfIntegerSyntax
    public boolean equals(Object obj) {
        return super.equals(obj) && (obj instanceof CopiesSupported);
    }

    @Override // javax.print.attribute.Attribute
    public final Class<? extends Attribute> getCategory() {
        return CopiesSupported.class;
    }

    @Override // javax.print.attribute.Attribute
    public final String getName() {
        return "copies-supported";
    }
}
