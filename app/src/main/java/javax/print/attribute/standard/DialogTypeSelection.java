package javax.print.attribute.standard;

import javax.print.attribute.EnumSyntax;
import javax.print.attribute.PrintRequestAttribute;

/* loaded from: rt.jar:javax/print/attribute/standard/DialogTypeSelection.class */
public final class DialogTypeSelection extends EnumSyntax implements PrintRequestAttribute {
    private static final long serialVersionUID = 7518682952133256029L;
    public static final DialogTypeSelection NATIVE = new DialogTypeSelection(0);
    public static final DialogTypeSelection COMMON = new DialogTypeSelection(1);
    private static final String[] myStringTable = {"native", "common"};
    private static final DialogTypeSelection[] myEnumValueTable = {NATIVE, COMMON};

    protected DialogTypeSelection(int i2) {
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
    public final Class getCategory() {
        return DialogTypeSelection.class;
    }

    @Override // javax.print.attribute.Attribute
    public final String getName() {
        return "dialog-type-selection";
    }
}
