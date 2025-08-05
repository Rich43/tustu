package javax.print.attribute.standard;

import javax.print.attribute.Attribute;
import javax.print.attribute.IntegerSyntax;
import javax.print.attribute.PrintJobAttribute;
import javax.print.attribute.PrintRequestAttribute;

/* loaded from: rt.jar:javax/print/attribute/standard/JobKOctets.class */
public final class JobKOctets extends IntegerSyntax implements PrintRequestAttribute, PrintJobAttribute {
    private static final long serialVersionUID = -8959710146498202869L;

    public JobKOctets(int i2) {
        super(i2, 0, Integer.MAX_VALUE);
    }

    @Override // javax.print.attribute.IntegerSyntax
    public boolean equals(Object obj) {
        return super.equals(obj) && (obj instanceof JobKOctets);
    }

    @Override // javax.print.attribute.Attribute
    public final Class<? extends Attribute> getCategory() {
        return JobKOctets.class;
    }

    @Override // javax.print.attribute.Attribute
    public final String getName() {
        return "job-k-octets";
    }
}
