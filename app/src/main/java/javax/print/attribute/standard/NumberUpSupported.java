package javax.print.attribute.standard;

import javax.print.attribute.Attribute;
import javax.print.attribute.SetOfIntegerSyntax;
import javax.print.attribute.SupportedValuesAttribute;

/* loaded from: rt.jar:javax/print/attribute/standard/NumberUpSupported.class */
public final class NumberUpSupported extends SetOfIntegerSyntax implements SupportedValuesAttribute {
    private static final long serialVersionUID = -1041573395759141805L;

    public NumberUpSupported(int[][] iArr) {
        super(iArr);
        if (iArr == null) {
            throw new NullPointerException("members is null");
        }
        int[][] members = getMembers();
        if (members.length == 0) {
            throw new IllegalArgumentException("members is zero-length");
        }
        for (int[] iArr2 : members) {
            if (iArr2[0] < 1) {
                throw new IllegalArgumentException("Number up value must be > 0");
            }
        }
    }

    public NumberUpSupported(int i2) {
        super(i2);
        if (i2 < 1) {
            throw new IllegalArgumentException("Number up value must be > 0");
        }
    }

    public NumberUpSupported(int i2, int i3) {
        super(i2, i3);
        if (i2 > i3) {
            throw new IllegalArgumentException("Null range specified");
        }
        if (i2 < 1) {
            throw new IllegalArgumentException("Number up value must be > 0");
        }
    }

    @Override // javax.print.attribute.SetOfIntegerSyntax
    public boolean equals(Object obj) {
        return super.equals(obj) && (obj instanceof NumberUpSupported);
    }

    @Override // javax.print.attribute.Attribute
    public final Class<? extends Attribute> getCategory() {
        return NumberUpSupported.class;
    }

    @Override // javax.print.attribute.Attribute
    public final String getName() {
        return "number-up-supported";
    }
}
