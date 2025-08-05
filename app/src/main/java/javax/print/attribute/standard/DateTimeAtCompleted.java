package javax.print.attribute.standard;

import java.util.Date;
import javax.print.attribute.Attribute;
import javax.print.attribute.DateTimeSyntax;
import javax.print.attribute.PrintJobAttribute;

/* loaded from: rt.jar:javax/print/attribute/standard/DateTimeAtCompleted.class */
public final class DateTimeAtCompleted extends DateTimeSyntax implements PrintJobAttribute {
    private static final long serialVersionUID = 6497399708058490000L;

    public DateTimeAtCompleted(Date date) {
        super(date);
    }

    @Override // javax.print.attribute.DateTimeSyntax
    public boolean equals(Object obj) {
        return super.equals(obj) && (obj instanceof DateTimeAtCompleted);
    }

    @Override // javax.print.attribute.Attribute
    public final Class<? extends Attribute> getCategory() {
        return DateTimeAtCompleted.class;
    }

    @Override // javax.print.attribute.Attribute
    public final String getName() {
        return "date-time-at-completed";
    }
}
