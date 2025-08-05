package javax.print.attribute.standard;

import java.net.URI;
import javax.print.attribute.Attribute;
import javax.print.attribute.PrintJobAttribute;
import javax.print.attribute.PrintRequestAttribute;
import javax.print.attribute.URISyntax;

/* loaded from: rt.jar:javax/print/attribute/standard/Destination.class */
public final class Destination extends URISyntax implements PrintJobAttribute, PrintRequestAttribute {
    private static final long serialVersionUID = 6776739171700415321L;

    public Destination(URI uri) {
        super(uri);
    }

    @Override // javax.print.attribute.URISyntax
    public boolean equals(Object obj) {
        return super.equals(obj) && (obj instanceof Destination);
    }

    @Override // javax.print.attribute.Attribute
    public final Class<? extends Attribute> getCategory() {
        return Destination.class;
    }

    @Override // javax.print.attribute.Attribute
    public final String getName() {
        return "spool-data-destination";
    }
}
