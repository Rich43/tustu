package javax.print.attribute.standard;

import java.util.Date;
import javax.print.attribute.Attribute;
import javax.print.attribute.DateTimeSyntax;
import javax.print.attribute.PrintJobAttribute;
import javax.print.attribute.PrintRequestAttribute;

/* loaded from: rt.jar:javax/print/attribute/standard/JobHoldUntil.class */
public final class JobHoldUntil extends DateTimeSyntax implements PrintRequestAttribute, PrintJobAttribute {
    private static final long serialVersionUID = -1664471048860415024L;

    public JobHoldUntil(Date date) {
        super(date);
    }

    @Override // javax.print.attribute.DateTimeSyntax
    public boolean equals(Object obj) {
        return super.equals(obj) && (obj instanceof JobHoldUntil);
    }

    @Override // javax.print.attribute.Attribute
    public final Class<? extends Attribute> getCategory() {
        return JobHoldUntil.class;
    }

    @Override // javax.print.attribute.Attribute
    public final String getName() {
        return "job-hold-until";
    }
}
