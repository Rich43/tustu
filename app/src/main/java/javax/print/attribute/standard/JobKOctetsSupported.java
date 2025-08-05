package javax.print.attribute.standard;

import javax.print.attribute.Attribute;
import javax.print.attribute.SetOfIntegerSyntax;
import javax.print.attribute.SupportedValuesAttribute;

/* loaded from: rt.jar:javax/print/attribute/standard/JobKOctetsSupported.class */
public final class JobKOctetsSupported extends SetOfIntegerSyntax implements SupportedValuesAttribute {
    private static final long serialVersionUID = -2867871140549897443L;

    public JobKOctetsSupported(int i2, int i3) {
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
        return super.equals(obj) && (obj instanceof JobKOctetsSupported);
    }

    @Override // javax.print.attribute.Attribute
    public final Class<? extends Attribute> getCategory() {
        return JobKOctetsSupported.class;
    }

    @Override // javax.print.attribute.Attribute
    public final String getName() {
        return "job-k-octets-supported";
    }
}
