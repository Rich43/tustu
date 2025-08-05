package javax.print.attribute.standard;

import javax.print.attribute.Attribute;
import javax.print.attribute.DocAttribute;
import javax.print.attribute.EnumSyntax;
import org.icepdf.core.pobjects.graphics.Separation;

/* loaded from: rt.jar:javax/print/attribute/standard/Compression.class */
public class Compression extends EnumSyntax implements DocAttribute {
    private static final long serialVersionUID = -5716748913324997674L;
    public static final Compression NONE = new Compression(0);
    public static final Compression DEFLATE = new Compression(1);
    public static final Compression GZIP = new Compression(2);
    public static final Compression COMPRESS = new Compression(3);
    private static final String[] myStringTable = {Separation.COLORANT_NONE, "deflate", "gzip", "compress"};
    private static final Compression[] myEnumValueTable = {NONE, DEFLATE, GZIP, COMPRESS};

    protected Compression(int i2) {
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
        return Compression.class;
    }

    @Override // javax.print.attribute.Attribute
    public final String getName() {
        return "compression";
    }
}
