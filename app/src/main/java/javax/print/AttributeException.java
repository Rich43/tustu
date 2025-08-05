package javax.print;

import javax.print.attribute.Attribute;

/* loaded from: rt.jar:javax/print/AttributeException.class */
public interface AttributeException {
    Class[] getUnsupportedAttributes();

    Attribute[] getUnsupportedValues();
}
