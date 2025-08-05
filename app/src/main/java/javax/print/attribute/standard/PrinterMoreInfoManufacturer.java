package javax.print.attribute.standard;

import java.net.URI;
import javax.print.attribute.Attribute;
import javax.print.attribute.PrintServiceAttribute;
import javax.print.attribute.URISyntax;

/* loaded from: rt.jar:javax/print/attribute/standard/PrinterMoreInfoManufacturer.class */
public final class PrinterMoreInfoManufacturer extends URISyntax implements PrintServiceAttribute {
    private static final long serialVersionUID = 3323271346485076608L;

    public PrinterMoreInfoManufacturer(URI uri) {
        super(uri);
    }

    @Override // javax.print.attribute.URISyntax
    public boolean equals(Object obj) {
        return super.equals(obj) && (obj instanceof PrinterMoreInfoManufacturer);
    }

    @Override // javax.print.attribute.Attribute
    public final Class<? extends Attribute> getCategory() {
        return PrinterMoreInfoManufacturer.class;
    }

    @Override // javax.print.attribute.Attribute
    public final String getName() {
        return "printer-more-info-manufacturer";
    }
}
