package javax.print.attribute.standard;

import javax.print.attribute.Attribute;
import javax.print.attribute.IntegerSyntax;
import javax.print.attribute.PrintJobAttribute;
import javax.print.attribute.PrintRequestAttribute;

/* loaded from: rt.jar:javax/print/attribute/standard/Copies.class */
public final class Copies extends IntegerSyntax implements PrintRequestAttribute, PrintJobAttribute {
    private static final long serialVersionUID = -6426631521680023833L;

    public Copies(int i2) {
        super(i2, 1, Integer.MAX_VALUE);
    }

    @Override // javax.print.attribute.IntegerSyntax
    public boolean equals(Object obj) {
        return super.equals(obj) && (obj instanceof Copies);
    }

    @Override // javax.print.attribute.Attribute
    public final Class<? extends Attribute> getCategory() {
        return Copies.class;
    }

    @Override // javax.print.attribute.Attribute
    public final String getName() {
        return "copies";
    }
}
