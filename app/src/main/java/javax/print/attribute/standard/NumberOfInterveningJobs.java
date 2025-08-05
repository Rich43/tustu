package javax.print.attribute.standard;

import javax.print.attribute.Attribute;
import javax.print.attribute.IntegerSyntax;
import javax.print.attribute.PrintJobAttribute;

/* loaded from: rt.jar:javax/print/attribute/standard/NumberOfInterveningJobs.class */
public final class NumberOfInterveningJobs extends IntegerSyntax implements PrintJobAttribute {
    private static final long serialVersionUID = 2568141124844982746L;

    public NumberOfInterveningJobs(int i2) {
        super(i2, 0, Integer.MAX_VALUE);
    }

    @Override // javax.print.attribute.IntegerSyntax
    public boolean equals(Object obj) {
        return super.equals(obj) && (obj instanceof NumberOfInterveningJobs);
    }

    @Override // javax.print.attribute.Attribute
    public final Class<? extends Attribute> getCategory() {
        return NumberOfInterveningJobs.class;
    }

    @Override // javax.print.attribute.Attribute
    public final String getName() {
        return "number-of-intervening-jobs";
    }
}
