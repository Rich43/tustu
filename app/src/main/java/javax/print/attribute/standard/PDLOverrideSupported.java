package javax.print.attribute.standard;

import javax.print.attribute.Attribute;
import javax.print.attribute.EnumSyntax;
import javax.print.attribute.PrintServiceAttribute;

/* loaded from: rt.jar:javax/print/attribute/standard/PDLOverrideSupported.class */
public class PDLOverrideSupported extends EnumSyntax implements PrintServiceAttribute {
    private static final long serialVersionUID = -4393264467928463934L;
    public static final PDLOverrideSupported NOT_ATTEMPTED = new PDLOverrideSupported(0);
    public static final PDLOverrideSupported ATTEMPTED = new PDLOverrideSupported(1);
    private static final String[] myStringTable = {"not-attempted", "attempted"};
    private static final PDLOverrideSupported[] myEnumValueTable = {NOT_ATTEMPTED, ATTEMPTED};

    protected PDLOverrideSupported(int i2) {
        super(i2);
    }

    @Override // javax.print.attribute.EnumSyntax
    protected String[] getStringTable() {
        return (String[]) myStringTable.clone();
    }

    @Override // javax.print.attribute.EnumSyntax
    protected EnumSyntax[] getEnumValueTable() {
        return (EnumSyntax[]) myEnumValueTable.clone();
    }

    @Override // javax.print.attribute.Attribute
    public final Class<? extends Attribute> getCategory() {
        return PDLOverrideSupported.class;
    }

    @Override // javax.print.attribute.Attribute
    public final String getName() {
        return "pdl-override-supported";
    }
}
