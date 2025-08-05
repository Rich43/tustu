package javax.print.attribute.standard;

import javax.print.attribute.Attribute;
import javax.print.attribute.IntegerSyntax;
import javax.print.attribute.PrintJobAttribute;
import javax.print.attribute.PrintRequestAttribute;

/* loaded from: rt.jar:javax/print/attribute/standard/JobMediaSheets.class */
public class JobMediaSheets extends IntegerSyntax implements PrintRequestAttribute, PrintJobAttribute {
    private static final long serialVersionUID = 408871131531979741L;

    public JobMediaSheets(int i2) {
        super(i2, 0, Integer.MAX_VALUE);
    }

    @Override // javax.print.attribute.IntegerSyntax
    public boolean equals(Object obj) {
        return super.equals(obj) && (obj instanceof JobMediaSheets);
    }

    @Override // javax.print.attribute.Attribute
    public final Class<? extends Attribute> getCategory() {
        return JobMediaSheets.class;
    }

    @Override // javax.print.attribute.Attribute
    public final String getName() {
        return "job-media-sheets";
    }
}
