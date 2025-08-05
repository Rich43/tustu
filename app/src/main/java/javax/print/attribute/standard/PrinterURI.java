package javax.print.attribute.standard;

import java.net.URI;
import javax.print.attribute.Attribute;
import javax.print.attribute.PrintServiceAttribute;
import javax.print.attribute.URISyntax;

/* loaded from: rt.jar:javax/print/attribute/standard/PrinterURI.class */
public final class PrinterURI extends URISyntax implements PrintServiceAttribute {
    private static final long serialVersionUID = 7923912792485606497L;

    public PrinterURI(URI uri) {
        super(uri);
    }

    @Override // javax.print.attribute.URISyntax
    public boolean equals(Object obj) {
        return super.equals(obj) && (obj instanceof PrinterURI);
    }

    @Override // javax.print.attribute.Attribute
    public final Class<? extends Attribute> getCategory() {
        return PrinterURI.class;
    }

    @Override // javax.print.attribute.Attribute
    public final String getName() {
        return "printer-uri";
    }
}
