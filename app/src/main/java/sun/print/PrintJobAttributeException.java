package sun.print;

import javax.print.AttributeException;
import javax.print.PrintException;
import javax.print.attribute.Attribute;

/* loaded from: rt.jar:sun/print/PrintJobAttributeException.class */
class PrintJobAttributeException extends PrintException implements AttributeException {
    private Attribute attr;
    private Class category;

    PrintJobAttributeException(String str, Class cls, Attribute attribute) {
        super(str);
        this.attr = attribute;
        this.category = cls;
    }

    @Override // javax.print.AttributeException
    public Class[] getUnsupportedAttributes() {
        if (this.category == null) {
            return null;
        }
        return new Class[]{this.category};
    }

    @Override // javax.print.AttributeException
    public Attribute[] getUnsupportedValues() {
        if (this.attr == null) {
            return null;
        }
        return new Attribute[]{this.attr};
    }
}
