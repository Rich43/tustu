package javax.print.attribute.standard;

import javax.print.attribute.Attribute;
import javax.print.attribute.IntegerSyntax;
import javax.print.attribute.PrintJobAttribute;

/* loaded from: rt.jar:javax/print/attribute/standard/JobKOctetsProcessed.class */
public final class JobKOctetsProcessed extends IntegerSyntax implements PrintJobAttribute {
    private static final long serialVersionUID = -6265238509657881806L;

    public JobKOctetsProcessed(int i2) {
        super(i2, 0, Integer.MAX_VALUE);
    }

    @Override // javax.print.attribute.IntegerSyntax
    public boolean equals(Object obj) {
        return super.equals(obj) && (obj instanceof JobKOctetsProcessed);
    }

    @Override // javax.print.attribute.Attribute
    public final Class<? extends Attribute> getCategory() {
        return JobKOctetsProcessed.class;
    }

    @Override // javax.print.attribute.Attribute
    public final String getName() {
        return "job-k-octets-processed";
    }
}
