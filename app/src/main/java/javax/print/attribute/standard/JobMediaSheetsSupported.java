package javax.print.attribute.standard;

import javax.print.attribute.Attribute;
import javax.print.attribute.SetOfIntegerSyntax;
import javax.print.attribute.SupportedValuesAttribute;

/* loaded from: rt.jar:javax/print/attribute/standard/JobMediaSheetsSupported.class */
public final class JobMediaSheetsSupported extends SetOfIntegerSyntax implements SupportedValuesAttribute {
    private static final long serialVersionUID = 2953685470388672940L;

    public JobMediaSheetsSupported(int i2, int i3) {
        super(i2, i3);
        if (i2 > i3) {
            throw new IllegalArgumentException("Null range specified");
        }
        if (i2 < 0) {
            throw new IllegalArgumentException("Job K octets value < 0 specified");
        }
    }

    @Override // javax.print.attribute.SetOfIntegerSyntax
    public boolean equals(Object obj) {
        return super.equals(obj) && (obj instanceof JobMediaSheetsSupported);
    }

    @Override // javax.print.attribute.Attribute
    public final Class<? extends Attribute> getCategory() {
        return JobMediaSheetsSupported.class;
    }

    @Override // javax.print.attribute.Attribute
    public final String getName() {
        return "job-media-sheets-supported";
    }
}
