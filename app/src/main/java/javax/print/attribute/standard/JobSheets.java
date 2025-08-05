package javax.print.attribute.standard;

import javax.print.attribute.Attribute;
import javax.print.attribute.EnumSyntax;
import javax.print.attribute.PrintJobAttribute;
import javax.print.attribute.PrintRequestAttribute;
import org.icepdf.core.pobjects.graphics.Separation;

/* loaded from: rt.jar:javax/print/attribute/standard/JobSheets.class */
public class JobSheets extends EnumSyntax implements PrintRequestAttribute, PrintJobAttribute {
    private static final long serialVersionUID = -4735258056132519759L;
    public static final JobSheets NONE = new JobSheets(0);
    public static final JobSheets STANDARD = new JobSheets(1);
    private static final String[] myStringTable = {Separation.COLORANT_NONE, "standard"};
    private static final JobSheets[] myEnumValueTable = {NONE, STANDARD};

    protected JobSheets(int i2) {
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
        return JobSheets.class;
    }

    @Override // javax.print.attribute.Attribute
    public final String getName() {
        return "job-sheets";
    }
}
