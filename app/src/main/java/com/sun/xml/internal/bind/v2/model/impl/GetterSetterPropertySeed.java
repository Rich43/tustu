package com.sun.xml.internal.bind.v2.model.impl;

import com.sun.javafx.fxml.BeanAdapter;
import com.sun.xml.internal.bind.v2.model.annotation.Locatable;
import com.sun.xml.internal.bind.v2.runtime.Location;
import java.beans.Introspector;
import java.lang.annotation.Annotation;

/* loaded from: rt.jar:com/sun/xml/internal/bind/v2/model/impl/GetterSetterPropertySeed.class */
class GetterSetterPropertySeed<TypeT, ClassDeclT, FieldT, MethodT> implements PropertySeed<TypeT, ClassDeclT, FieldT, MethodT> {
    protected final MethodT getter;
    protected final MethodT setter;
    private ClassInfoImpl<TypeT, ClassDeclT, FieldT, MethodT> parent;

    GetterSetterPropertySeed(ClassInfoImpl<TypeT, ClassDeclT, FieldT, MethodT> parent, MethodT getter, MethodT setter) {
        this.parent = parent;
        this.getter = getter;
        this.setter = setter;
        if (getter == null && setter == null) {
            throw new IllegalArgumentException();
        }
    }

    @Override // com.sun.xml.internal.bind.v2.model.impl.PropertySeed
    public TypeT getRawType() {
        if (this.getter != null) {
            return this.parent.nav().getReturnType(this.getter);
        }
        return this.parent.nav().getMethodParameters(this.setter)[0];
    }

    @Override // com.sun.xml.internal.bind.v2.model.annotation.AnnotationSource
    public <A extends Annotation> A readAnnotation(Class<A> cls) {
        return (A) this.parent.reader().getMethodAnnotation(cls, this.getter, this.setter, this);
    }

    @Override // com.sun.xml.internal.bind.v2.model.annotation.AnnotationSource
    public boolean hasAnnotation(Class<? extends Annotation> annotationType) {
        return this.parent.reader().hasMethodAnnotation(annotationType, getName(), this.getter, this.setter, this);
    }

    @Override // com.sun.xml.internal.bind.v2.model.impl.PropertySeed
    public String getName() {
        if (this.getter != null) {
            return getName(this.getter);
        }
        return getName(this.setter);
    }

    private String getName(MethodT m2) {
        String seed = this.parent.nav().getMethodName(m2);
        String lseed = seed.toLowerCase();
        if (lseed.startsWith("get") || lseed.startsWith("set")) {
            return camelize(seed.substring(3));
        }
        if (lseed.startsWith(BeanAdapter.IS_PREFIX)) {
            return camelize(seed.substring(2));
        }
        return seed;
    }

    private static String camelize(String s2) {
        return Introspector.decapitalize(s2);
    }

    @Override // com.sun.xml.internal.bind.v2.model.annotation.Locatable
    public Locatable getUpstream() {
        return this.parent;
    }

    @Override // com.sun.xml.internal.bind.v2.model.annotation.Locatable
    public Location getLocation() {
        if (this.getter != null) {
            return this.parent.nav().getMethodLocation(this.getter);
        }
        return this.parent.nav().getMethodLocation(this.setter);
    }
}
