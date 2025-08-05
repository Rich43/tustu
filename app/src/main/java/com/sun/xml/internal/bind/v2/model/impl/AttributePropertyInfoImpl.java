package com.sun.xml.internal.bind.v2.model.impl;

import com.sun.xml.internal.bind.api.impl.NameConverter;
import com.sun.xml.internal.bind.v2.model.core.AttributePropertyInfo;
import com.sun.xml.internal.bind.v2.model.core.PropertyKind;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlSchema;
import javax.xml.namespace.QName;

/* loaded from: rt.jar:com/sun/xml/internal/bind/v2/model/impl/AttributePropertyInfoImpl.class */
class AttributePropertyInfoImpl<TypeT, ClassDeclT, FieldT, MethodT> extends SingleTypePropertyInfoImpl<TypeT, ClassDeclT, FieldT, MethodT> implements AttributePropertyInfo<TypeT, ClassDeclT> {
    private final QName xmlName;
    private final boolean isRequired;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !AttributePropertyInfoImpl.class.desiredAssertionStatus();
    }

    AttributePropertyInfoImpl(ClassInfoImpl<TypeT, ClassDeclT, FieldT, MethodT> parent, PropertySeed<TypeT, ClassDeclT, FieldT, MethodT> seed) {
        super(parent, seed);
        XmlAttribute att = (XmlAttribute) seed.readAnnotation(XmlAttribute.class);
        if (!$assertionsDisabled && att == null) {
            throw new AssertionError();
        }
        if (att.required()) {
            this.isRequired = true;
        } else {
            this.isRequired = nav().isPrimitive(getIndividualType());
        }
        this.xmlName = calcXmlName(att);
    }

    private QName calcXmlName(XmlAttribute xmlAttribute) {
        String strNamespace = xmlAttribute.namespace();
        String strName = xmlAttribute.name();
        if (strName.equals("##default")) {
            strName = NameConverter.standard.toVariableName(getName());
        }
        if (strNamespace.equals("##default")) {
            if (((XmlSchema) reader().getPackageAnnotation(XmlSchema.class, this.parent.getClazz(), this)) != null) {
                switch (r0.attributeFormDefault()) {
                    case QUALIFIED:
                        strNamespace = this.parent.getTypeName().getNamespaceURI();
                        if (strNamespace.length() == 0) {
                            strNamespace = this.parent.builder.defaultNsUri;
                            break;
                        }
                        break;
                    case UNQUALIFIED:
                    case UNSET:
                        strNamespace = "";
                        break;
                }
            } else {
                strNamespace = "";
            }
        }
        return new QName(strNamespace.intern(), strName.intern());
    }

    @Override // com.sun.xml.internal.bind.v2.model.core.AttributePropertyInfo
    public boolean isRequired() {
        return this.isRequired;
    }

    @Override // com.sun.xml.internal.bind.v2.model.core.AttributePropertyInfo
    public final QName getXmlName() {
        return this.xmlName;
    }

    @Override // com.sun.xml.internal.bind.v2.model.core.PropertyInfo
    public final PropertyKind kind() {
        return PropertyKind.ATTRIBUTE;
    }
}
