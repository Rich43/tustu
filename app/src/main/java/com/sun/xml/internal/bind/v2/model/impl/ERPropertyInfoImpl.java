package com.sun.xml.internal.bind.v2.model.impl;

import com.sun.xml.internal.bind.v2.runtime.IllegalAnnotationException;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.namespace.QName;

/* loaded from: rt.jar:com/sun/xml/internal/bind/v2/model/impl/ERPropertyInfoImpl.class */
abstract class ERPropertyInfoImpl<TypeT, ClassDeclT, FieldT, MethodT> extends PropertyInfoImpl<TypeT, ClassDeclT, FieldT, MethodT> {
    private final QName xmlName;
    private final boolean wrapperNillable;
    private final boolean wrapperRequired;

    public ERPropertyInfoImpl(ClassInfoImpl<TypeT, ClassDeclT, FieldT, MethodT> classInfoImpl, PropertySeed<TypeT, ClassDeclT, FieldT, MethodT> propertySeed) {
        super(classInfoImpl, propertySeed);
        XmlElementWrapper xmlElementWrapper = (XmlElementWrapper) this.seed.readAnnotation(XmlElementWrapper.class);
        boolean zNillable = false;
        boolean zRequired = false;
        if (!isCollection()) {
            this.xmlName = null;
            if (xmlElementWrapper != null) {
                classInfoImpl.builder.reportError(new IllegalAnnotationException(Messages.XML_ELEMENT_WRAPPER_ON_NON_COLLECTION.format(nav().getClassName(this.parent.getClazz()) + '.' + this.seed.getName()), xmlElementWrapper));
            }
        } else if (xmlElementWrapper != null) {
            this.xmlName = calcXmlName(xmlElementWrapper);
            zNillable = xmlElementWrapper.nillable();
            zRequired = xmlElementWrapper.required();
        } else {
            this.xmlName = null;
        }
        this.wrapperNillable = zNillable;
        this.wrapperRequired = zRequired;
    }

    public final QName getXmlName() {
        return this.xmlName;
    }

    public final boolean isCollectionNillable() {
        return this.wrapperNillable;
    }

    public final boolean isCollectionRequired() {
        return this.wrapperRequired;
    }
}
