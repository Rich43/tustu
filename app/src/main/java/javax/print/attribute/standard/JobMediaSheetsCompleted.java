package javax.print.attribute.standard;

import javax.print.attribute.Attribute;
import javax.print.attribute.IntegerSyntax;
import javax.print.attribute.PrintJobAttribute;

/* loaded from: rt.jar:javax/print/attribute/standard/JobMediaSheetsCompleted.class */
public final class JobMediaSheetsCompleted extends IntegerSyntax implements PrintJobAttribute {
    private static final long serialVersionUID = 1739595973810840475L;

    public JobMediaSheetsCompleted(int i2) {
        super(i2, 0, Integer.MAX_VALUE);
    }

    @Override // javax.print.attribute.IntegerSyntax
    public boolean equals(Object obj) {
        return super.equals(obj) && (obj instanceof JobMediaSheetsCompleted);
    }

    @Override // javax.print.attribute.Attribute
    public final Class<? extends Attribute> getCategory() {
        return JobMediaSheetsCompleted.class;
    }

    @Override // javax.print.attribute.Attribute
    public final String getName() {
        return "job-media-sheets-completed";
    }
}
