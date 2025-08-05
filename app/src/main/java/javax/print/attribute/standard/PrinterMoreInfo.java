package javax.print.attribute.standard;

import java.net.URI;
import javax.print.attribute.Attribute;
import javax.print.attribute.PrintServiceAttribute;
import javax.print.attribute.URISyntax;

/* loaded from: rt.jar:javax/print/attribute/standard/PrinterMoreInfo.class */
public final class PrinterMoreInfo extends URISyntax implements PrintServiceAttribute {
    private static final long serialVersionUID = 4555850007675338574L;

    public PrinterMoreInfo(URI uri) {
        super(uri);
    }

    @Override // javax.print.attribute.URISyntax
    public boolean equals(Object obj) {
        return super.equals(obj) && (obj instanceof PrinterMoreInfo);
    }

    @Override // javax.print.attribute.Attribute
    public final Class<? extends Attribute> getCategory() {
        return PrinterMoreInfo.class;
    }

    @Override // javax.print.attribute.Attribute
    public final String getName() {
        return "printer-more-info";
    }
}
