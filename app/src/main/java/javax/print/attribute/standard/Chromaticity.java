package javax.print.attribute.standard;

import javax.print.attribute.Attribute;
import javax.print.attribute.DocAttribute;
import javax.print.attribute.EnumSyntax;
import javax.print.attribute.PrintJobAttribute;
import javax.print.attribute.PrintRequestAttribute;

/* loaded from: rt.jar:javax/print/attribute/standard/Chromaticity.class */
public final class Chromaticity extends EnumSyntax implements DocAttribute, PrintRequestAttribute, PrintJobAttribute {
    private static final long serialVersionUID = 4660543931355214012L;
    public static final Chromaticity MONOCHROME = new Chromaticity(0);
    public static final Chromaticity COLOR = new Chromaticity(1);
    private static final String[] myStringTable = {"monochrome", "color"};
    private static final Chromaticity[] myEnumValueTable = {MONOCHROME, COLOR};

    protected Chromaticity(int i2) {
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
        return Chromaticity.class;
    }

    @Override // javax.print.attribute.Attribute
    public final String getName() {
        return "chromaticity";
    }
}
