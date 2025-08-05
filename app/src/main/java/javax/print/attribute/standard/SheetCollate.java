package javax.print.attribute.standard;

import javax.print.attribute.Attribute;
import javax.print.attribute.DocAttribute;
import javax.print.attribute.EnumSyntax;
import javax.print.attribute.PrintJobAttribute;
import javax.print.attribute.PrintRequestAttribute;

/* loaded from: rt.jar:javax/print/attribute/standard/SheetCollate.class */
public final class SheetCollate extends EnumSyntax implements DocAttribute, PrintRequestAttribute, PrintJobAttribute {
    private static final long serialVersionUID = 7080587914259873003L;
    public static final SheetCollate UNCOLLATED = new SheetCollate(0);
    public static final SheetCollate COLLATED = new SheetCollate(1);
    private static final String[] myStringTable = {"uncollated", "collated"};
    private static final SheetCollate[] myEnumValueTable = {UNCOLLATED, COLLATED};

    protected SheetCollate(int i2) {
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
        return SheetCollate.class;
    }

    @Override // javax.print.attribute.Attribute
    public final String getName() {
        return "sheet-collate";
    }
}
