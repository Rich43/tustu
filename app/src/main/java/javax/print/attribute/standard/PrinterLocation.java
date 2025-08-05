package javax.print.attribute.standard;

import java.util.Locale;
import javax.print.attribute.Attribute;
import javax.print.attribute.PrintServiceAttribute;
import javax.print.attribute.TextSyntax;

/* loaded from: rt.jar:javax/print/attribute/standard/PrinterLocation.class */
public final class PrinterLocation extends TextSyntax implements PrintServiceAttribute {
    private static final long serialVersionUID = -1598610039865566337L;

    public PrinterLocation(String str, Locale locale) {
        super(str, locale);
    }

    @Override // javax.print.attribute.TextSyntax
    public boolean equals(Object obj) {
        return super.equals(obj) && (obj instanceof PrinterLocation);
    }

    @Override // javax.print.attribute.Attribute
    public final Class<? extends Attribute> getCategory() {
        return PrinterLocation.class;
    }

    @Override // javax.print.attribute.Attribute
    public final String getName() {
        return "printer-location";
    }
}
