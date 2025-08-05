package javax.print.attribute.standard;

import java.util.Locale;
import javax.print.attribute.Attribute;
import javax.print.attribute.PrintRequestAttribute;
import javax.print.attribute.TextSyntax;

/* loaded from: rt.jar:javax/print/attribute/standard/RequestingUserName.class */
public final class RequestingUserName extends TextSyntax implements PrintRequestAttribute {
    private static final long serialVersionUID = -2683049894310331454L;

    public RequestingUserName(String str, Locale locale) {
        super(str, locale);
    }

    @Override // javax.print.attribute.TextSyntax
    public boolean equals(Object obj) {
        return super.equals(obj) && (obj instanceof RequestingUserName);
    }

    @Override // javax.print.attribute.Attribute
    public final Class<? extends Attribute> getCategory() {
        return RequestingUserName.class;
    }

    @Override // javax.print.attribute.Attribute
    public final String getName() {
        return "requesting-user-name";
    }
}
