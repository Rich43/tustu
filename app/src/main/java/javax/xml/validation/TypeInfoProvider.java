package javax.xml.validation;

import org.w3c.dom.TypeInfo;

/* loaded from: rt.jar:javax/xml/validation/TypeInfoProvider.class */
public abstract class TypeInfoProvider {
    public abstract TypeInfo getElementTypeInfo();

    public abstract TypeInfo getAttributeTypeInfo(int i2);

    public abstract boolean isIdAttribute(int i2);

    public abstract boolean isSpecified(int i2);

    protected TypeInfoProvider() {
    }
}
