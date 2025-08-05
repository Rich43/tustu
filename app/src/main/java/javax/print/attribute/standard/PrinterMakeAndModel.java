package javax.print.attribute.standard;

import java.util.Locale;
import javax.print.attribute.Attribute;
import javax.print.attribute.PrintServiceAttribute;
import javax.print.attribute.TextSyntax;

/* loaded from: rt.jar:javax/print/attribute/standard/PrinterMakeAndModel.class */
public final class PrinterMakeAndModel extends TextSyntax implements PrintServiceAttribute {
    private static final long serialVersionUID = 4580461489499351411L;

    public PrinterMakeAndModel(String str, Locale locale) {
        super(str, locale);
    }

    @Override // javax.print.attribute.TextSyntax
    public boolean equals(Object obj) {
        return super.equals(obj) && (obj instanceof PrinterMakeAndModel);
    }

    @Override // javax.print.attribute.Attribute
    public final Class<? extends Attribute> getCategory() {
        return PrinterMakeAndModel.class;
    }

    @Override // javax.print.attribute.Attribute
    public final String getName() {
        return "printer-make-and-model";
    }
}
