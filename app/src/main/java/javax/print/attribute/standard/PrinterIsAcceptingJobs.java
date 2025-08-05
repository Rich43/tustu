package javax.print.attribute.standard;

import javax.print.attribute.Attribute;
import javax.print.attribute.EnumSyntax;
import javax.print.attribute.PrintServiceAttribute;

/* loaded from: rt.jar:javax/print/attribute/standard/PrinterIsAcceptingJobs.class */
public final class PrinterIsAcceptingJobs extends EnumSyntax implements PrintServiceAttribute {
    private static final long serialVersionUID = -5052010680537678061L;
    public static final PrinterIsAcceptingJobs NOT_ACCEPTING_JOBS = new PrinterIsAcceptingJobs(0);
    public static final PrinterIsAcceptingJobs ACCEPTING_JOBS = new PrinterIsAcceptingJobs(1);
    private static final String[] myStringTable = {"not-accepting-jobs", "accepting-jobs"};
    private static final PrinterIsAcceptingJobs[] myEnumValueTable = {NOT_ACCEPTING_JOBS, ACCEPTING_JOBS};

    protected PrinterIsAcceptingJobs(int i2) {
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
        return PrinterIsAcceptingJobs.class;
    }

    @Override // javax.print.attribute.Attribute
    public final String getName() {
        return "printer-is-accepting-jobs";
    }
}
