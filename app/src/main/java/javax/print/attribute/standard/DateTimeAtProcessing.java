package javax.print.attribute.standard;

import java.util.Date;
import javax.print.attribute.Attribute;
import javax.print.attribute.DateTimeSyntax;
import javax.print.attribute.PrintJobAttribute;

/* loaded from: rt.jar:javax/print/attribute/standard/DateTimeAtProcessing.class */
public final class DateTimeAtProcessing extends DateTimeSyntax implements PrintJobAttribute {
    private static final long serialVersionUID = -3710068197278263244L;

    public DateTimeAtProcessing(Date date) {
        super(date);
    }

    @Override // javax.print.attribute.DateTimeSyntax
    public boolean equals(Object obj) {
        return super.equals(obj) && (obj instanceof DateTimeAtProcessing);
    }

    @Override // javax.print.attribute.Attribute
    public final Class<? extends Attribute> getCategory() {
        return DateTimeAtProcessing.class;
    }

    @Override // javax.print.attribute.Attribute
    public final String getName() {
        return "date-time-at-processing";
    }
}
