package com.sun.xml.internal.bind.v2.model.impl;

import com.sun.istack.internal.FinalArrayList;
import com.sun.istack.internal.localization.Localizable;
import com.sun.xml.internal.bind.v2.model.core.ElementPropertyInfo;
import com.sun.xml.internal.bind.v2.model.core.ID;
import com.sun.xml.internal.bind.v2.model.core.PropertyKind;
import com.sun.xml.internal.bind.v2.model.core.TypeInfo;
import com.sun.xml.internal.bind.v2.runtime.IllegalAnnotationException;
import java.util.AbstractList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlList;
import javax.xml.namespace.QName;

/* loaded from: rt.jar:com/sun/xml/internal/bind/v2/model/impl/ElementPropertyInfoImpl.class */
class ElementPropertyInfoImpl<TypeT, ClassDeclT, FieldT, MethodT> extends ERPropertyInfoImpl<TypeT, ClassDeclT, FieldT, MethodT> implements ElementPropertyInfo<TypeT, ClassDeclT> {
    private List<TypeRefImpl<TypeT, ClassDeclT>> types;
    private final List<TypeInfo<TypeT, ClassDeclT>> ref;
    private Boolean isRequired;
    private final boolean isValueList;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !ElementPropertyInfoImpl.class.desiredAssertionStatus();
    }

    ElementPropertyInfoImpl(ClassInfoImpl<TypeT, ClassDeclT, FieldT, MethodT> parent, PropertySeed<TypeT, ClassDeclT, FieldT, MethodT> propertySeed) {
        super(parent, propertySeed);
        this.ref = new AbstractList<TypeInfo<TypeT, ClassDeclT>>() { // from class: com.sun.xml.internal.bind.v2.model.impl.ElementPropertyInfoImpl.1
            @Override // java.util.AbstractList, java.util.List
            public TypeInfo<TypeT, ClassDeclT> get(int index) {
                return ElementPropertyInfoImpl.this.getTypes().get(index).getTarget2();
            }

            @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
            public int size() {
                return ElementPropertyInfoImpl.this.getTypes().size();
            }
        };
        this.isValueList = this.seed.hasAnnotation(XmlList.class);
    }

    @Override // com.sun.xml.internal.bind.v2.model.core.ElementPropertyInfo
    public List<? extends TypeRefImpl<TypeT, ClassDeclT>> getTypes() {
        if (this.types == null) {
            this.types = new FinalArrayList();
            XmlElement[] xmlElementArrValue = null;
            XmlElement xmlElement = (XmlElement) this.seed.readAnnotation(XmlElement.class);
            XmlElements xmlElements = (XmlElements) this.seed.readAnnotation(XmlElements.class);
            if (xmlElement != null && xmlElements != null) {
                this.parent.builder.reportError(new IllegalAnnotationException(Messages.MUTUALLY_EXCLUSIVE_ANNOTATIONS.format(nav().getClassName(this.parent.getClazz()) + '#' + this.seed.getName(), xmlElement.annotationType().getName(), xmlElements.annotationType().getName()), xmlElement, xmlElements));
            }
            this.isRequired = true;
            if (xmlElement != null) {
                xmlElementArrValue = new XmlElement[]{xmlElement};
            } else if (xmlElements != null) {
                xmlElementArrValue = xmlElements.value();
            }
            if (xmlElementArrValue == null) {
                TypeT individualType = getIndividualType();
                if (!nav().isPrimitive(individualType) || isCollection()) {
                    this.isRequired = false;
                }
                this.types.add(createTypeRef(calcXmlName((XmlElement) null), individualType, isCollection(), null));
            } else {
                for (XmlElement xmlElement2 : xmlElementArrValue) {
                    QName qNameCalcXmlName = calcXmlName(xmlElement2);
                    TypeT classValue = reader().getClassValue2(xmlElement2, "type");
                    if (nav().isSameType(classValue, nav().ref(XmlElement.DEFAULT.class))) {
                        classValue = getIndividualType();
                    }
                    if ((!nav().isPrimitive(classValue) || isCollection()) && !xmlElement2.required()) {
                        this.isRequired = false;
                    }
                    this.types.add(createTypeRef(qNameCalcXmlName, classValue, xmlElement2.nillable(), getDefaultValue(xmlElement2.defaultValue())));
                }
            }
            this.types = Collections.unmodifiableList(this.types);
            if (!$assertionsDisabled && this.types.contains(null)) {
                throw new AssertionError();
            }
        }
        return this.types;
    }

    private String getDefaultValue(String value) {
        if (value.equals(Localizable.NOT_LOCALIZABLE)) {
            return null;
        }
        return value;
    }

    protected TypeRefImpl<TypeT, ClassDeclT> createTypeRef(QName name, TypeT type, boolean isNillable, String defaultValue) {
        return new TypeRefImpl<>(this, name, type, isNillable, defaultValue);
    }

    @Override // com.sun.xml.internal.bind.v2.model.core.ElementPropertyInfo
    public boolean isValueList() {
        return this.isValueList;
    }

    @Override // com.sun.xml.internal.bind.v2.model.core.ElementPropertyInfo
    public boolean isRequired() {
        if (this.isRequired == null) {
            getTypes();
        }
        return this.isRequired.booleanValue();
    }

    @Override // com.sun.xml.internal.bind.v2.model.core.PropertyInfo
    /* renamed from: ref */
    public List<? extends TypeInfo<TypeT, ClassDeclT>> ref2() {
        return this.ref;
    }

    @Override // com.sun.xml.internal.bind.v2.model.core.PropertyInfo
    public final PropertyKind kind() {
        return PropertyKind.ELEMENT;
    }

    @Override // com.sun.xml.internal.bind.v2.model.impl.PropertyInfoImpl
    protected void link() {
        super.link();
        Iterator<? extends TypeRefImpl<TypeT, ClassDeclT>> it = getTypes().iterator();
        while (it.hasNext()) {
            it.next().link();
        }
        if (isValueList()) {
            if (id() != ID.IDREF) {
                Iterator<TypeRefImpl<TypeT, ClassDeclT>> it2 = this.types.iterator();
                while (true) {
                    if (!it2.hasNext()) {
                        break;
                    }
                    TypeRefImpl<TypeT, ClassDeclT> ref = it2.next();
                    if (!ref.getTarget2().isSimpleType()) {
                        this.parent.builder.reportError(new IllegalAnnotationException(Messages.XMLLIST_NEEDS_SIMPLETYPE.format(nav().getTypeName(ref.getTarget2().getType2())), this));
                        break;
                    }
                }
            }
            if (!isCollection()) {
                this.parent.builder.reportError(new IllegalAnnotationException(Messages.XMLLIST_ON_SINGLE_PROPERTY.format(new Object[0]), this));
            }
        }
    }
}
