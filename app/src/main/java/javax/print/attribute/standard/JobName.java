package javax.print.attribute.standard;

import java.util.Locale;
import javax.print.attribute.Attribute;
import javax.print.attribute.PrintJobAttribute;
import javax.print.attribute.PrintRequestAttribute;
import javax.print.attribute.TextSyntax;

/* loaded from: rt.jar:javax/print/attribute/standard/JobName.class */
public final class JobName extends TextSyntax implements PrintRequestAttribute, PrintJobAttribute {
    private static final long serialVersionUID = 4660359192078689545L;

    public JobName(String str, Locale locale) {
        super(str, locale);
    }

    @Override // javax.print.attribute.TextSyntax
    public boolean equals(Object obj) {
        return super.equals(obj) && (obj instanceof JobName);
    }

    @Override // javax.print.attribute.Attribute
    public final Class<? extends Attribute> getCategory() {
        return JobName.class;
    }

    @Override // javax.print.attribute.Attribute
    public final String getName() {
        return "job-name";
    }
}
