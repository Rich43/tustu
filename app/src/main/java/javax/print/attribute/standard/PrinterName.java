package javax.print.attribute.standard;

import java.util.Locale;
import javax.print.attribute.Attribute;
import javax.print.attribute.PrintServiceAttribute;
import javax.print.attribute.TextSyntax;

/* loaded from: rt.jar:javax/print/attribute/standard/PrinterName.class */
public final class PrinterName extends TextSyntax implements PrintServiceAttribute {
    private static final long serialVersionUID = 299740639137803127L;

    public PrinterName(String str, Locale locale) {
        super(str, locale);
    }

    @Override // javax.print.attribute.TextSyntax
    public boolean equals(Object obj) {
        return super.equals(obj) && (obj instanceof PrinterName);
    }

    @Override // javax.print.attribute.Attribute
    public final Class<? extends Attribute> getCategory() {
        return PrinterName.class;
    }

    @Override // javax.print.attribute.Attribute
    public final String getName() {
        return "printer-name";
    }
}
