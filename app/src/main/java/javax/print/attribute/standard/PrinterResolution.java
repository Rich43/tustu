package javax.print.attribute.standard;

import javax.print.attribute.Attribute;
import javax.print.attribute.DocAttribute;
import javax.print.attribute.PrintJobAttribute;
import javax.print.attribute.PrintRequestAttribute;
import javax.print.attribute.ResolutionSyntax;

/* loaded from: rt.jar:javax/print/attribute/standard/PrinterResolution.class */
public final class PrinterResolution extends ResolutionSyntax implements DocAttribute, PrintRequestAttribute, PrintJobAttribute {
    private static final long serialVersionUID = 13090306561090558L;

    public PrinterResolution(int i2, int i3, int i4) {
        super(i2, i3, i4);
    }

    @Override // javax.print.attribute.ResolutionSyntax
    public boolean equals(Object obj) {
        return super.equals(obj) && (obj instanceof PrinterResolution);
    }

    @Override // javax.print.attribute.Attribute
    public final Class<? extends Attribute> getCategory() {
        return PrinterResolution.class;
    }

    @Override // javax.print.attribute.Attribute
    public final String getName() {
        return "printer-resolution";
    }
}
