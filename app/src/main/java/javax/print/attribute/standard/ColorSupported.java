package javax.print.attribute.standard;

import javax.print.attribute.Attribute;
import javax.print.attribute.EnumSyntax;
import javax.print.attribute.PrintServiceAttribute;

/* loaded from: rt.jar:javax/print/attribute/standard/ColorSupported.class */
public final class ColorSupported extends EnumSyntax implements PrintServiceAttribute {
    private static final long serialVersionUID = -2700555589688535545L;
    public static final ColorSupported NOT_SUPPORTED = new ColorSupported(0);
    public static final ColorSupported SUPPORTED = new ColorSupported(1);
    private static final String[] myStringTable = {"not-supported", "supported"};
    private static final ColorSupported[] myEnumValueTable = {NOT_SUPPORTED, SUPPORTED};

    protected ColorSupported(int i2) {
        super(i2);
    }

    @Override // javax.print.attribute.EnumSyntax
    protected String[] getStringTable() {
        return myStringTable;
    }

    @Override // javax.print.attribute.EnumSyntax
    protected EnumSyntax[] getEnumValueTable() {
        return myEnumValueTable;
    }

    @Override // javax.print.attribute.Attribute
    public final Class<? extends Attribute> getCategory() {
        return ColorSupported.class;
    }

    @Override // javax.print.attribute.Attribute
    public final String getName() {
        return "color-supported";
    }
}
