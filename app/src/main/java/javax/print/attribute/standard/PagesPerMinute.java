package javax.print.attribute.standard;

import javax.print.attribute.Attribute;
import javax.print.attribute.IntegerSyntax;
import javax.print.attribute.PrintServiceAttribute;

/* loaded from: rt.jar:javax/print/attribute/standard/PagesPerMinute.class */
public final class PagesPerMinute extends IntegerSyntax implements PrintServiceAttribute {
    private static final long serialVersionUID = -6366403993072862015L;

    public PagesPerMinute(int i2) {
        super(i2, 0, Integer.MAX_VALUE);
    }

    @Override // javax.print.attribute.IntegerSyntax
    public boolean equals(Object obj) {
        return super.equals(obj) && (obj instanceof PagesPerMinute);
    }

    @Override // javax.print.attribute.Attribute
    public final Class<? extends Attribute> getCategory() {
        return PagesPerMinute.class;
    }

    @Override // javax.print.attribute.Attribute
    public final String getName() {
        return "pages-per-minute";
    }
}
