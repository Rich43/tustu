package javax.print.attribute.standard;

import javax.print.attribute.Attribute;
import javax.print.attribute.IntegerSyntax;
import javax.print.attribute.SupportedValuesAttribute;

/* loaded from: rt.jar:javax/print/attribute/standard/JobPrioritySupported.class */
public final class JobPrioritySupported extends IntegerSyntax implements SupportedValuesAttribute {
    private static final long serialVersionUID = 2564840378013555894L;

    public JobPrioritySupported(int i2) {
        super(i2, 1, 100);
    }

    @Override // javax.print.attribute.IntegerSyntax
    public boolean equals(Object obj) {
        return super.equals(obj) && (obj instanceof JobPrioritySupported);
    }

    @Override // javax.print.attribute.Attribute
    public final Class<? extends Attribute> getCategory() {
        return JobPrioritySupported.class;
    }

    @Override // javax.print.attribute.Attribute
    public final String getName() {
        return "job-priority-supported";
    }
}
