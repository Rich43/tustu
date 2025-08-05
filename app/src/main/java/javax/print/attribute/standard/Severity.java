package javax.print.attribute.standard;

import java.util.jar.Pack200;
import javax.print.attribute.Attribute;
import javax.print.attribute.EnumSyntax;

/* loaded from: rt.jar:javax/print/attribute/standard/Severity.class */
public final class Severity extends EnumSyntax implements Attribute {
    private static final long serialVersionUID = 8781881462717925380L;
    public static final Severity REPORT = new Severity(0);
    public static final Severity WARNING = new Severity(1);
    public static final Severity ERROR = new Severity(2);
    private static final String[] myStringTable = {"report", "warning", Pack200.Packer.ERROR};
    private static final Severity[] myEnumValueTable = {REPORT, WARNING, ERROR};

    protected Severity(int i2) {
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
        return Severity.class;
    }

    @Override // javax.print.attribute.Attribute
    public final String getName() {
        return "severity";
    }
}
