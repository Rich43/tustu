package javax.print.attribute.standard;

import javax.print.attribute.Attribute;
import javax.print.attribute.DocAttribute;
import javax.print.attribute.EnumSyntax;
import javax.print.attribute.PrintJobAttribute;
import javax.print.attribute.PrintRequestAttribute;

/* loaded from: rt.jar:javax/print/attribute/standard/Sides.class */
public final class Sides extends EnumSyntax implements DocAttribute, PrintRequestAttribute, PrintJobAttribute {
    private static final long serialVersionUID = -6890309414893262822L;
    public static final Sides ONE_SIDED = new Sides(0);
    public static final Sides TWO_SIDED_LONG_EDGE = new Sides(1);
    public static final Sides TWO_SIDED_SHORT_EDGE = new Sides(2);
    public static final Sides DUPLEX = TWO_SIDED_LONG_EDGE;
    public static final Sides TUMBLE = TWO_SIDED_SHORT_EDGE;
    private static final String[] myStringTable = {"one-sided", "two-sided-long-edge", "two-sided-short-edge"};
    private static final Sides[] myEnumValueTable = {ONE_SIDED, TWO_SIDED_LONG_EDGE, TWO_SIDED_SHORT_EDGE};

    protected Sides(int i2) {
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
        return Sides.class;
    }

    @Override // javax.print.attribute.Attribute
    public final String getName() {
        return "sides";
    }
}
