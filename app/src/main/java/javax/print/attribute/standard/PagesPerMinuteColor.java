package javax.print.attribute.standard;

import javax.print.attribute.Attribute;
import javax.print.attribute.IntegerSyntax;
import javax.print.attribute.PrintServiceAttribute;

/* loaded from: rt.jar:javax/print/attribute/standard/PagesPerMinuteColor.class */
public final class PagesPerMinuteColor extends IntegerSyntax implements PrintServiceAttribute {
    static final long serialVersionUID = 1684993151687470944L;

    public PagesPerMinuteColor(int i2) {
        super(i2, 0, Integer.MAX_VALUE);
    }

    @Override // javax.print.attribute.IntegerSyntax
    public boolean equals(Object obj) {
        return super.equals(obj) && (obj instanceof PagesPerMinuteColor);
    }

    @Override // javax.print.attribute.Attribute
    public final Class<? extends Attribute> getCategory() {
        return PagesPerMinuteColor.class;
    }

    @Override // javax.print.attribute.Attribute
    public final String getName() {
        return "pages-per-minute-color";
    }
}
