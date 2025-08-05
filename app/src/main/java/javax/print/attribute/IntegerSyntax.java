package javax.print.attribute;

import com.sun.org.apache.xalan.internal.templates.Constants;
import java.io.Serializable;

/* loaded from: rt.jar:javax/print/attribute/IntegerSyntax.class */
public abstract class IntegerSyntax implements Serializable, Cloneable {
    private static final long serialVersionUID = 3644574816328081943L;
    private int value;

    protected IntegerSyntax(int i2) {
        this.value = i2;
    }

    protected IntegerSyntax(int i2, int i3, int i4) {
        if (i3 > i2 || i2 > i4) {
            throw new IllegalArgumentException("Value " + i2 + " not in range " + i3 + Constants.ATTRVAL_PARENT + i4);
        }
        this.value = i2;
    }

    public int getValue() {
        return this.value;
    }

    public boolean equals(Object obj) {
        return obj != null && (obj instanceof IntegerSyntax) && this.value == ((IntegerSyntax) obj).value;
    }

    public int hashCode() {
        return this.value;
    }

    public String toString() {
        return "" + this.value;
    }
}
