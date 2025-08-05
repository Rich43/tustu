package javax.print.attribute.standard;

import javax.print.attribute.Attribute;
import javax.print.attribute.IntegerSyntax;
import javax.print.attribute.PrintJobAttribute;

/* loaded from: rt.jar:javax/print/attribute/standard/JobImpressionsCompleted.class */
public final class JobImpressionsCompleted extends IntegerSyntax implements PrintJobAttribute {
    private static final long serialVersionUID = 6722648442432393294L;

    public JobImpressionsCompleted(int i2) {
        super(i2, 0, Integer.MAX_VALUE);
    }

    @Override // javax.print.attribute.IntegerSyntax
    public boolean equals(Object obj) {
        return super.equals(obj) && (obj instanceof JobImpressionsCompleted);
    }

    @Override // javax.print.attribute.Attribute
    public final Class<? extends Attribute> getCategory() {
        return JobImpressionsCompleted.class;
    }

    @Override // javax.print.attribute.Attribute
    public final String getName() {
        return "job-impressions-completed";
    }
}
