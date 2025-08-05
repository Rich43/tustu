package javax.print.attribute.standard;

import javax.print.attribute.Attribute;
import javax.print.attribute.DocAttribute;
import javax.print.attribute.EnumSyntax;
import javax.print.attribute.PrintJobAttribute;
import javax.print.attribute.PrintRequestAttribute;

/* loaded from: rt.jar:javax/print/attribute/standard/Media.class */
public abstract class Media extends EnumSyntax implements DocAttribute, PrintRequestAttribute, PrintJobAttribute {
    private static final long serialVersionUID = -2823970704630722439L;

    protected Media(int i2) {
        super(i2);
    }

    public boolean equals(Object obj) {
        return obj != null && (obj instanceof Media) && obj.getClass() == getClass() && ((Media) obj).getValue() == getValue();
    }

    @Override // javax.print.attribute.Attribute
    public final Class<? extends Attribute> getCategory() {
        return Media.class;
    }

    @Override // javax.print.attribute.Attribute
    public final String getName() {
        return "media";
    }
}
