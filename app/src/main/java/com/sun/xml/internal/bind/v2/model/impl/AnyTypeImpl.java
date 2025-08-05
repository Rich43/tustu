package com.sun.xml.internal.bind.v2.model.impl;

import com.sun.xml.internal.bind.v2.model.annotation.Locatable;
import com.sun.xml.internal.bind.v2.model.core.NonElement;
import com.sun.xml.internal.bind.v2.model.nav.Navigator;
import com.sun.xml.internal.bind.v2.runtime.Location;
import javax.xml.namespace.QName;

/* loaded from: rt.jar:com/sun/xml/internal/bind/v2/model/impl/AnyTypeImpl.class */
class AnyTypeImpl<T, C> implements NonElement<T, C> {
    private final T type;
    private final Navigator<T, C, ?, ?> nav;

    public AnyTypeImpl(Navigator<T, C, ?, ?> nav) {
        this.type = nav.ref(Object.class);
        this.nav = nav;
    }

    @Override // com.sun.xml.internal.bind.v2.model.core.NonElement
    public QName getTypeName() {
        return ANYTYPE_NAME;
    }

    @Override // com.sun.xml.internal.bind.v2.model.core.TypeInfo
    /* renamed from: getType */
    public T getType2() {
        return this.type;
    }

    @Override // com.sun.xml.internal.bind.v2.model.annotation.Locatable
    public Locatable getUpstream() {
        return null;
    }

    @Override // com.sun.xml.internal.bind.v2.model.core.NonElement
    public boolean isSimpleType() {
        return false;
    }

    @Override // com.sun.xml.internal.bind.v2.model.annotation.Locatable
    public Location getLocation() {
        return this.nav.getClassLocation(this.nav.asDecl(Object.class));
    }

    @Override // com.sun.xml.internal.bind.v2.model.core.TypeInfo
    public final boolean canBeReferencedByIDREF() {
        return true;
    }
}
