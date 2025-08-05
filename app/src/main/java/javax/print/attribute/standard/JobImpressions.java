package javax.print.attribute.standard;

import javax.print.attribute.Attribute;
import javax.print.attribute.IntegerSyntax;
import javax.print.attribute.PrintJobAttribute;
import javax.print.attribute.PrintRequestAttribute;

/* loaded from: rt.jar:javax/print/attribute/standard/JobImpressions.class */
public final class JobImpressions extends IntegerSyntax implements PrintRequestAttribute, PrintJobAttribute {
    private static final long serialVersionUID = 8225537206784322464L;

    public JobImpressions(int i2) {
        super(i2, 0, Integer.MAX_VALUE);
    }

    @Override // javax.print.attribute.IntegerSyntax
    public boolean equals(Object obj) {
        return super.equals(obj) && (obj instanceof JobImpressions);
    }

    @Override // javax.print.attribute.Attribute
    public final Class<? extends Attribute> getCategory() {
        return JobImpressions.class;
    }

    @Override // javax.print.attribute.Attribute
    public final String getName() {
        return "job-impressions";
    }
}
