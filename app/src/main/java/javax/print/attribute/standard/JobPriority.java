package javax.print.attribute.standard;

import javax.print.attribute.Attribute;
import javax.print.attribute.IntegerSyntax;
import javax.print.attribute.PrintJobAttribute;
import javax.print.attribute.PrintRequestAttribute;

/* loaded from: rt.jar:javax/print/attribute/standard/JobPriority.class */
public final class JobPriority extends IntegerSyntax implements PrintRequestAttribute, PrintJobAttribute {
    private static final long serialVersionUID = -4599900369040602769L;

    public JobPriority(int i2) {
        super(i2, 1, 100);
    }

    @Override // javax.print.attribute.IntegerSyntax
    public boolean equals(Object obj) {
        return super.equals(obj) && (obj instanceof JobPriority);
    }

    @Override // javax.print.attribute.Attribute
    public final Class<? extends Attribute> getCategory() {
        return JobPriority.class;
    }

    @Override // javax.print.attribute.Attribute
    public final String getName() {
        return "job-priority";
    }
}
