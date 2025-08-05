package com.sun.xml.internal.bind.api;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Collection;
import javax.xml.namespace.QName;

/* loaded from: rt.jar:com/sun/xml/internal/bind/api/TypeReference.class */
public final class TypeReference {
    public final QName tagName;
    public final Type type;
    public final Annotation[] annotations;

    public TypeReference(QName tagName, Type type, Annotation... annotations) {
        if (tagName == null || type == null || annotations == null) {
            String nullArgs = tagName == null ? "tagName" : "";
            if (type == null) {
                nullArgs = nullArgs + (nullArgs.length() > 0 ? ", type" : "type");
            }
            if (annotations == null) {
                nullArgs = nullArgs + (nullArgs.length() > 0 ? ", annotations" : "annotations");
            }
            Messages.ARGUMENT_CANT_BE_NULL.format(nullArgs);
            throw new IllegalArgumentException(Messages.ARGUMENT_CANT_BE_NULL.format(nullArgs));
        }
        this.tagName = new QName(tagName.getNamespaceURI().intern(), tagName.getLocalPart().intern(), tagName.getPrefix());
        this.type = type;
        this.annotations = annotations;
    }

    public <A extends Annotation> A get(Class<A> annotationType) {
        for (Annotation a2 : this.annotations) {
            if (a2.annotationType() == annotationType) {
                return annotationType.cast(a2);
            }
        }
        return null;
    }

    public TypeReference toItemType() {
        Type base = Utils.REFLECTION_NAVIGATOR.getBaseClass(this.type, Collection.class);
        if (base == null) {
            return this;
        }
        return new TypeReference(this.tagName, Utils.REFLECTION_NAVIGATOR.getTypeArgument(base, 0), new Annotation[0]);
    }

    public boolean equals(Object o2) {
        if (this == o2) {
            return true;
        }
        if (o2 == null || getClass() != o2.getClass()) {
            return false;
        }
        TypeReference that = (TypeReference) o2;
        return Arrays.equals(this.annotations, that.annotations) && this.tagName.equals(that.tagName) && this.type.equals(that.type);
    }

    public int hashCode() {
        int result = this.tagName.hashCode();
        return (31 * ((31 * result) + this.type.hashCode())) + Arrays.hashCode(this.annotations);
    }
}
