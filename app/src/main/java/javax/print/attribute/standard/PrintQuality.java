package javax.print.attribute.standard;

import javax.print.attribute.Attribute;
import javax.print.attribute.DocAttribute;
import javax.print.attribute.EnumSyntax;
import javax.print.attribute.PrintJobAttribute;
import javax.print.attribute.PrintRequestAttribute;

/* loaded from: rt.jar:javax/print/attribute/standard/PrintQuality.class */
public class PrintQuality extends EnumSyntax implements DocAttribute, PrintRequestAttribute, PrintJobAttribute {
    private static final long serialVersionUID = -3072341285225858365L;
    public static final PrintQuality DRAFT = new PrintQuality(3);
    public static final PrintQuality NORMAL = new PrintQuality(4);
    public static final PrintQuality HIGH = new PrintQuality(5);
    private static final String[] myStringTable = {"draft", "normal", "high"};
    private static final PrintQuality[] myEnumValueTable = {DRAFT, NORMAL, HIGH};

    protected PrintQuality(int i2) {
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

    @Override // javax.print.attribute.EnumSyntax
    protected int getOffset() {
        return 3;
    }

    @Override // javax.print.attribute.Attribute
    public final Class<? extends Attribute> getCategory() {
        return PrintQuality.class;
    }

    @Override // javax.print.attribute.Attribute
    public final String getName() {
        return "print-quality";
    }
}
