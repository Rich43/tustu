package javax.print.attribute.standard;

import java.util.Date;
import javax.print.attribute.Attribute;
import javax.print.attribute.DateTimeSyntax;
import javax.print.attribute.PrintJobAttribute;

/* loaded from: rt.jar:javax/print/attribute/standard/DateTimeAtCreation.class */
public final class DateTimeAtCreation extends DateTimeSyntax implements PrintJobAttribute {
    private static final long serialVersionUID = -2923732231056647903L;

    public DateTimeAtCreation(Date date) {
        super(date);
    }

    @Override // javax.print.attribute.DateTimeSyntax
    public boolean equals(Object obj) {
        return super.equals(obj) && (obj instanceof DateTimeAtCreation);
    }

    @Override // javax.print.attribute.Attribute
    public final Class<? extends Attribute> getCategory() {
        return DateTimeAtCreation.class;
    }

    @Override // javax.print.attribute.Attribute
    public final String getName() {
        return "date-time-at-creation";
    }
}
