package javax.print.attribute.standard;

import java.util.Locale;
import javax.print.attribute.Attribute;
import javax.print.attribute.PrintJobAttribute;
import javax.print.attribute.TextSyntax;

/* loaded from: rt.jar:javax/print/attribute/standard/JobOriginatingUserName.class */
public final class JobOriginatingUserName extends TextSyntax implements PrintJobAttribute {
    private static final long serialVersionUID = -8052537926362933477L;

    public JobOriginatingUserName(String str, Locale locale) {
        super(str, locale);
    }

    @Override // javax.print.attribute.TextSyntax
    public boolean equals(Object obj) {
        return super.equals(obj) && (obj instanceof JobOriginatingUserName);
    }

    @Override // javax.print.attribute.Attribute
    public final Class<? extends Attribute> getCategory() {
        return JobOriginatingUserName.class;
    }

    @Override // javax.print.attribute.Attribute
    public final String getName() {
        return "job-originating-user-name";
    }
}
