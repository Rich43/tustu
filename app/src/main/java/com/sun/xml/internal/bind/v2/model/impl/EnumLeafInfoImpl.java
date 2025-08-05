package com.sun.xml.internal.bind.v2.model.impl;

import com.sun.org.apache.xerces.internal.impl.xs.SchemaSymbols;
import com.sun.xml.internal.bind.v2.model.annotation.Locatable;
import com.sun.xml.internal.bind.v2.model.core.ClassInfo;
import com.sun.xml.internal.bind.v2.model.core.Element;
import com.sun.xml.internal.bind.v2.model.core.EnumLeafInfo;
import com.sun.xml.internal.bind.v2.model.core.NonElement;
import com.sun.xml.internal.bind.v2.runtime.Location;
import java.util.Iterator;
import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.namespace.QName;

/* loaded from: rt.jar:com/sun/xml/internal/bind/v2/model/impl/EnumLeafInfoImpl.class */
class EnumLeafInfoImpl<T, C, F, M> extends TypeInfoImpl<T, C, F, M> implements EnumLeafInfo<T, C>, Element<T, C>, Iterable<EnumConstantImpl<T, C, F, M>> {
    final C clazz;
    NonElement<T, C> baseType;
    private final T type;
    private final QName typeName;
    private EnumConstantImpl<T, C, F, M> firstConstant;
    private QName elementName;
    protected boolean tokenStringType;

    public EnumLeafInfoImpl(ModelBuilder<T, C, F, M> builder, Locatable upstream, C clazz, T type) {
        super(builder, upstream);
        this.clazz = clazz;
        this.type = type;
        this.elementName = parseElementName(clazz);
        this.typeName = parseTypeName(clazz);
        XmlEnum xe = (XmlEnum) builder.reader.getClassAnnotation(XmlEnum.class, clazz, this);
        if (xe != null) {
            T base = builder.reader.getClassValue(xe, "value");
            this.baseType = builder.getTypeInfo(base, this);
        } else {
            this.baseType = builder.getTypeInfo(builder.nav.ref(String.class), this);
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    protected void calcConstants() {
        XmlSchemaType xmlSchemaType;
        EnumConstantImpl<T, C, F, M> enumConstantImplCreateEnumConstant = null;
        Iterator<? extends F> it = nav().getDeclaredFields(this.clazz).iterator();
        while (true) {
            if (!it.hasNext()) {
                break;
            }
            F next = it.next();
            if (nav().isSameType(nav().getFieldType(next), nav().ref(String.class)) && (xmlSchemaType = (XmlSchemaType) this.builder.reader.getFieldAnnotation(XmlSchemaType.class, next, this)) != null && SchemaSymbols.ATTVAL_TOKEN.equals(xmlSchemaType.name())) {
                this.tokenStringType = true;
                break;
            }
        }
        Object[] enumConstants = nav().getEnumConstants(this.clazz);
        for (int length = enumConstants.length - 1; length >= 0; length--) {
            Object obj = enumConstants[length];
            String fieldName = nav().getFieldName(obj);
            XmlEnumValue xmlEnumValue = (XmlEnumValue) this.builder.reader.getFieldAnnotation(XmlEnumValue.class, obj, this);
            enumConstantImplCreateEnumConstant = createEnumConstant(fieldName, xmlEnumValue == null ? fieldName : xmlEnumValue.value(), obj, enumConstantImplCreateEnumConstant);
        }
        this.firstConstant = enumConstantImplCreateEnumConstant;
    }

    protected EnumConstantImpl<T, C, F, M> createEnumConstant(String name, String literal, F constant, EnumConstantImpl<T, C, F, M> last) {
        return new EnumConstantImpl<>(this, name, literal, last);
    }

    @Override // com.sun.xml.internal.bind.v2.model.core.TypeInfo
    public T getType() {
        return this.type;
    }

    public boolean isToken() {
        return this.tokenStringType;
    }

    @Override // com.sun.xml.internal.bind.v2.model.core.TypeInfo
    public final boolean canBeReferencedByIDREF() {
        return false;
    }

    @Override // com.sun.xml.internal.bind.v2.model.core.NonElement
    public QName getTypeName() {
        return this.typeName;
    }

    @Override // com.sun.xml.internal.bind.v2.model.core.EnumLeafInfo
    public C getClazz() {
        return this.clazz;
    }

    @Override // com.sun.xml.internal.bind.v2.model.core.EnumLeafInfo
    public NonElement<T, C> getBaseType() {
        return this.baseType;
    }

    @Override // com.sun.xml.internal.bind.v2.model.core.NonElement
    public boolean isSimpleType() {
        return true;
    }

    @Override // com.sun.xml.internal.bind.v2.model.annotation.Locatable
    public Location getLocation() {
        return nav().getClassLocation(this.clazz);
    }

    @Override // com.sun.xml.internal.bind.v2.model.core.EnumLeafInfo
    public Iterable<? extends EnumConstantImpl<T, C, F, M>> getConstants() {
        if (this.firstConstant == null) {
            calcConstants();
        }
        return this;
    }

    @Override // com.sun.xml.internal.bind.v2.model.impl.TypeInfoImpl
    public void link() {
        getConstants();
        super.link();
    }

    @Override // com.sun.xml.internal.bind.v2.model.core.Element
    public Element<T, C> getSubstitutionHead() {
        return null;
    }

    @Override // com.sun.xml.internal.bind.v2.model.core.MaybeElement
    public QName getElementName() {
        return this.elementName;
    }

    @Override // com.sun.xml.internal.bind.v2.model.core.MaybeElement
    public boolean isElement() {
        return this.elementName != null;
    }

    @Override // com.sun.xml.internal.bind.v2.model.core.MaybeElement
    public Element<T, C> asElement() {
        if (isElement()) {
            return this;
        }
        return null;
    }

    @Override // com.sun.xml.internal.bind.v2.model.core.Element
    public ClassInfo<T, C> getScope() {
        return null;
    }

    @Override // java.lang.Iterable, java.util.List
    public Iterator<EnumConstantImpl<T, C, F, M>> iterator() {
        return new Iterator<EnumConstantImpl<T, C, F, M>>() { // from class: com.sun.xml.internal.bind.v2.model.impl.EnumLeafInfoImpl.1
            private EnumConstantImpl<T, C, F, M> next;

            {
                this.next = EnumLeafInfoImpl.this.firstConstant;
            }

            @Override // java.util.Iterator
            public boolean hasNext() {
                return this.next != null;
            }

            @Override // java.util.Iterator
            public EnumConstantImpl<T, C, F, M> next() {
                EnumConstantImpl<T, C, F, M> r2 = this.next;
                this.next = this.next.next;
                return r2;
            }

            @Override // java.util.Iterator
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }
}
