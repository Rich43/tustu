package com.sun.xml.internal.bind.v2.model.impl;

import com.sun.xml.internal.bind.v2.model.annotation.Locatable;
import com.sun.xml.internal.bind.v2.model.core.LeafInfo;
import com.sun.xml.internal.bind.v2.runtime.Location;
import javax.xml.namespace.QName;

/* loaded from: rt.jar:com/sun/xml/internal/bind/v2/model/impl/LeafInfoImpl.class */
abstract class LeafInfoImpl<TypeT, ClassDeclT> implements LeafInfo<TypeT, ClassDeclT>, Location {
    private final TypeT type;
    private final QName typeName;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !LeafInfoImpl.class.desiredAssertionStatus();
    }

    protected LeafInfoImpl(TypeT type, QName typeName) {
        if (!$assertionsDisabled && type == null) {
            throw new AssertionError();
        }
        this.type = type;
        this.typeName = typeName;
    }

    /* renamed from: getType */
    public TypeT getType2() {
        return this.type;
    }

    @Override // com.sun.xml.internal.bind.v2.model.core.TypeInfo
    public final boolean canBeReferencedByIDREF() {
        return false;
    }

    public QName getTypeName() {
        return this.typeName;
    }

    public Locatable getUpstream() {
        return null;
    }

    public Location getLocation() {
        return this;
    }

    public boolean isSimpleType() {
        return true;
    }

    public String toString() {
        return this.type.toString();
    }
}
