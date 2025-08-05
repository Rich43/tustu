package javax.print.attribute;

import com.sun.org.apache.xalan.internal.templates.Constants;
import java.io.InvalidObjectException;
import java.io.ObjectStreamException;
import java.io.Serializable;

/* loaded from: rt.jar:javax/print/attribute/EnumSyntax.class */
public abstract class EnumSyntax implements Serializable, Cloneable {
    private static final long serialVersionUID = -2739521845085831642L;
    private int value;

    protected EnumSyntax(int i2) {
        this.value = i2;
    }

    public int getValue() {
        return this.value;
    }

    public Object clone() {
        return this;
    }

    public int hashCode() {
        return this.value;
    }

    public String toString() {
        String[] stringTable = getStringTable();
        int offset = this.value - getOffset();
        return (stringTable == null || offset < 0 || offset >= stringTable.length) ? Integer.toString(this.value) : stringTable[offset];
    }

    protected Object readResolve() throws ObjectStreamException {
        EnumSyntax[] enumValueTable = getEnumValueTable();
        if (enumValueTable == null) {
            throw new InvalidObjectException("Null enumeration value table for class " + ((Object) getClass()));
        }
        int offset = getOffset();
        int i2 = this.value - offset;
        if (0 > i2 || i2 >= enumValueTable.length) {
            throw new InvalidObjectException("Integer value = " + this.value + " not in valid range " + offset + Constants.ATTRVAL_PARENT + ((offset + enumValueTable.length) - 1) + "for class " + ((Object) getClass()));
        }
        EnumSyntax enumSyntax = enumValueTable[i2];
        if (enumSyntax == null) {
            throw new InvalidObjectException("No enumeration value for integer value = " + this.value + "for class " + ((Object) getClass()));
        }
        return enumSyntax;
    }

    protected String[] getStringTable() {
        return null;
    }

    protected EnumSyntax[] getEnumValueTable() {
        return null;
    }

    protected int getOffset() {
        return 0;
    }
}
