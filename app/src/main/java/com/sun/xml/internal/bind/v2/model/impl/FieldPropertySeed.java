package com.sun.xml.internal.bind.v2.model.impl;

import com.sun.xml.internal.bind.v2.model.annotation.Locatable;
import com.sun.xml.internal.bind.v2.runtime.Location;
import java.lang.annotation.Annotation;

/* loaded from: rt.jar:com/sun/xml/internal/bind/v2/model/impl/FieldPropertySeed.class */
class FieldPropertySeed<TypeT, ClassDeclT, FieldT, MethodT> implements PropertySeed<TypeT, ClassDeclT, FieldT, MethodT> {
    protected final FieldT field;
    private ClassInfoImpl<TypeT, ClassDeclT, FieldT, MethodT> parent;

    FieldPropertySeed(ClassInfoImpl<TypeT, ClassDeclT, FieldT, MethodT> classInfo, FieldT field) {
        this.parent = classInfo;
        this.field = field;
    }

    @Override // com.sun.xml.internal.bind.v2.model.annotation.AnnotationSource
    public <A extends Annotation> A readAnnotation(Class<A> cls) {
        return (A) this.parent.reader().getFieldAnnotation(cls, this.field, this);
    }

    @Override // com.sun.xml.internal.bind.v2.model.annotation.AnnotationSource
    public boolean hasAnnotation(Class<? extends Annotation> annotationType) {
        return this.parent.reader().hasFieldAnnotation(annotationType, this.field);
    }

    @Override // com.sun.xml.internal.bind.v2.model.impl.PropertySeed
    public String getName() {
        return this.parent.nav().getFieldName(this.field);
    }

    @Override // com.sun.xml.internal.bind.v2.model.impl.PropertySeed
    public TypeT getRawType() {
        return this.parent.nav().getFieldType(this.field);
    }

    @Override // com.sun.xml.internal.bind.v2.model.annotation.Locatable
    public Locatable getUpstream() {
        return this.parent;
    }

    @Override // com.sun.xml.internal.bind.v2.model.annotation.Locatable
    public Location getLocation() {
        return this.parent.nav().getFieldLocation(this.field);
    }
}
