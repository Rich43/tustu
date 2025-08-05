package javax.print.attribute.standard;

import java.util.Locale;
import javax.print.attribute.Attribute;
import javax.print.attribute.PrintServiceAttribute;
import javax.print.attribute.TextSyntax;

/* loaded from: rt.jar:javax/print/attribute/standard/PrinterMessageFromOperator.class */
public final class PrinterMessageFromOperator extends TextSyntax implements PrintServiceAttribute {
    static final long serialVersionUID = -4486871203218629318L;

    public PrinterMessageFromOperator(String str, Locale locale) {
        super(str, locale);
    }

    @Override // javax.print.attribute.TextSyntax
    public boolean equals(Object obj) {
        return super.equals(obj) && (obj instanceof PrinterMessageFromOperator);
    }

    @Override // javax.print.attribute.Attribute
    public final Class<? extends Attribute> getCategory() {
        return PrinterMessageFromOperator.class;
    }

    @Override // javax.print.attribute.Attribute
    public final String getName() {
        return "printer-message-from-operator";
    }
}
