package javax.print.attribute.standard;

import java.util.Locale;
import javax.print.attribute.Attribute;
import javax.print.attribute.PrintJobAttribute;
import javax.print.attribute.TextSyntax;

/* loaded from: rt.jar:javax/print/attribute/standard/OutputDeviceAssigned.class */
public final class OutputDeviceAssigned extends TextSyntax implements PrintJobAttribute {
    private static final long serialVersionUID = 5486733778854271081L;

    public OutputDeviceAssigned(String str, Locale locale) {
        super(str, locale);
    }

    @Override // javax.print.attribute.TextSyntax
    public boolean equals(Object obj) {
        return super.equals(obj) && (obj instanceof OutputDeviceAssigned);
    }

    @Override // javax.print.attribute.Attribute
    public final Class<? extends Attribute> getCategory() {
        return OutputDeviceAssigned.class;
    }

    @Override // javax.print.attribute.Attribute
    public final String getName() {
        return "output-device-assigned";
    }
}
