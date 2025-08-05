package javax.print.attribute.standard;

import javax.print.attribute.Attribute;
import javax.print.attribute.EnumSyntax;
import javax.print.attribute.PrintServiceAttribute;

/* loaded from: rt.jar:javax/print/attribute/standard/PrinterState.class */
public final class PrinterState extends EnumSyntax implements PrintServiceAttribute {
    private static final long serialVersionUID = -649578618346507718L;
    public static final PrinterState UNKNOWN = new PrinterState(0);
    public static final PrinterState IDLE = new PrinterState(3);
    public static final PrinterState PROCESSING = new PrinterState(4);
    public static final PrinterState STOPPED = new PrinterState(5);
    private static final String[] myStringTable = {"unknown", null, null, "idle", "processing", "stopped"};
    private static final PrinterState[] myEnumValueTable = {UNKNOWN, null, null, IDLE, PROCESSING, STOPPED};

    protected PrinterState(int i2) {
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
        return PrinterState.class;
    }

    @Override // javax.print.attribute.Attribute
    public final String getName() {
        return "printer-state";
    }
}
