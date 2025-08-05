package com.sun.xml.internal.bind.v2.model.impl;

import com.sun.xml.internal.bind.v2.model.core.NonElement;
import com.sun.xml.internal.bind.v2.model.core.PropertyInfo;
import com.sun.xml.internal.bind.v2.model.core.TypeRef;
import javax.xml.namespace.QName;

/* loaded from: rt.jar:com/sun/xml/internal/bind/v2/model/impl/TypeRefImpl.class */
class TypeRefImpl<TypeT, ClassDeclT> implements TypeRef<TypeT, ClassDeclT> {
    private final QName elementName;
    private final TypeT type;
    protected final ElementPropertyInfoImpl<TypeT, ClassDeclT, ?, ?> owner;
    private NonElement<TypeT, ClassDeclT> ref;
    private final boolean isNillable;
    private String defaultValue;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !TypeRefImpl.class.desiredAssertionStatus();
    }

    public TypeRefImpl(ElementPropertyInfoImpl<TypeT, ClassDeclT, ?, ?> owner, QName elementName, TypeT type, boolean isNillable, String defaultValue) {
        this.owner = owner;
        this.elementName = elementName;
        this.type = type;
        this.isNillable = isNillable;
        this.defaultValue = defaultValue;
        if (!$assertionsDisabled && owner == null) {
            throw new AssertionError();
        }
        if (!$assertionsDisabled && elementName == null) {
            throw new AssertionError();
        }
        if (!$assertionsDisabled && type == null) {
            throw new AssertionError();
        }
    }

    /* renamed from: getTarget */
    public NonElement<TypeT, ClassDeclT> getTarget2() {
        if (this.ref == null) {
            calcRef();
        }
        return this.ref;
    }

    @Override // com.sun.xml.internal.bind.v2.model.core.TypeRef
    public QName getTagName() {
        return this.elementName;
    }

    @Override // com.sun.xml.internal.bind.v2.model.core.TypeRef
    public boolean isNillable() {
        return this.isNillable;
    }

    @Override // com.sun.xml.internal.bind.v2.model.core.TypeRef
    public String getDefaultValue() {
        return this.defaultValue;
    }

    protected void link() {
        calcRef();
    }

    private void calcRef() {
        this.ref = this.owner.parent.builder.getTypeInfo(this.type, this.owner);
        if (!$assertionsDisabled && this.ref == null) {
            throw new AssertionError();
        }
    }

    /* renamed from: getSource */
    public PropertyInfo<TypeT, ClassDeclT> getSource2() {
        return this.owner;
    }
}
