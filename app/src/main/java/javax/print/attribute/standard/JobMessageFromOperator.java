package javax.print.attribute.standard;

import java.util.Locale;
import javax.print.attribute.Attribute;
import javax.print.attribute.PrintJobAttribute;
import javax.print.attribute.TextSyntax;

/* loaded from: rt.jar:javax/print/attribute/standard/JobMessageFromOperator.class */
public final class JobMessageFromOperator extends TextSyntax implements PrintJobAttribute {
    private static final long serialVersionUID = -4620751846003142047L;

    public JobMessageFromOperator(String str, Locale locale) {
        super(str, locale);
    }

    @Override // javax.print.attribute.TextSyntax
    public boolean equals(Object obj) {
        return super.equals(obj) && (obj instanceof JobMessageFromOperator);
    }

    @Override // javax.print.attribute.Attribute
    public final Class<? extends Attribute> getCategory() {
        return JobMessageFromOperator.class;
    }

    @Override // javax.print.attribute.Attribute
    public final String getName() {
        return "job-message-from-operator";
    }
}
