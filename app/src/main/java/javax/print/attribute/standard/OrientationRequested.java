package javax.print.attribute.standard;

import javax.print.attribute.Attribute;
import javax.print.attribute.DocAttribute;
import javax.print.attribute.EnumSyntax;
import javax.print.attribute.PrintJobAttribute;
import javax.print.attribute.PrintRequestAttribute;

/* loaded from: rt.jar:javax/print/attribute/standard/OrientationRequested.class */
public final class OrientationRequested extends EnumSyntax implements DocAttribute, PrintRequestAttribute, PrintJobAttribute {
    private static final long serialVersionUID = -4447437289862822276L;
    public static final OrientationRequested PORTRAIT = new OrientationRequested(3);
    public static final OrientationRequested LANDSCAPE = new OrientationRequested(4);
    public static final OrientationRequested REVERSE_LANDSCAPE = new OrientationRequested(5);
    public static final OrientationRequested REVERSE_PORTRAIT = new OrientationRequested(6);
    private static final String[] myStringTable = {"portrait", "landscape", "reverse-landscape", "reverse-portrait"};
    private static final OrientationRequested[] myEnumValueTable = {PORTRAIT, LANDSCAPE, REVERSE_LANDSCAPE, REVERSE_PORTRAIT};

    protected OrientationRequested(int i2) {
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

    @Override // javax.print.attribute.EnumSyntax
    protected int getOffset() {
        return 3;
    }

    @Override // javax.print.attribute.Attribute
    public final Class<? extends Attribute> getCategory() {
        return OrientationRequested.class;
    }

    @Override // javax.print.attribute.Attribute
    public final String getName() {
        return "orientation-requested";
    }
}
