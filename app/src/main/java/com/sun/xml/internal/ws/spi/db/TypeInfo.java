package com.sun.xml.internal.ws.spi.db;

import java.lang.annotation.Annotation;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import javax.xml.namespace.QName;

/* loaded from: rt.jar:com/sun/xml/internal/ws/spi/db/TypeInfo.class */
public final class TypeInfo {
    public final QName tagName;
    public Type type;
    public final Annotation[] annotations;
    private Map<String, Object> properties = new HashMap();
    private boolean isGlobalElement = true;
    private TypeInfo parentCollectionType;
    private Type genericType;
    private boolean nillable;

    public TypeInfo(QName tagName, Type type, Annotation... annotations) {
        this.nillable = true;
        if (tagName == null || type == null || annotations == null) {
            String nullArgs = tagName == null ? "tagName" : "";
            if (type == null) {
                nullArgs = nullArgs + (nullArgs.length() > 0 ? ", type" : "type");
            }
            if (annotations == null) {
                nullArgs = nullArgs + (nullArgs.length() > 0 ? ", annotations" : "annotations");
            }
            throw new IllegalArgumentException("Argument(s) \"" + nullArgs + "\" can''t be null.)");
        }
        this.tagName = new QName(tagName.getNamespaceURI().intern(), tagName.getLocalPart().intern(), tagName.getPrefix());
        this.type = type;
        if ((type instanceof Class) && ((Class) type).isPrimitive()) {
            this.nillable = false;
        }
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

    public TypeInfo toItemType() {
        Type t2 = this.genericType != null ? this.genericType : this.type;
        Type base = Utils.REFLECTION_NAVIGATOR.getBaseClass(t2, Collection.class);
        if (base == null) {
            return this;
        }
        return new TypeInfo(this.tagName, Utils.REFLECTION_NAVIGATOR.getTypeArgument(base, 0), new Annotation[0]);
    }

    public Map<String, Object> properties() {
        return this.properties;
    }

    public boolean isGlobalElement() {
        return this.isGlobalElement;
    }

    public void setGlobalElement(boolean isGlobalElement) {
        this.isGlobalElement = isGlobalElement;
    }

    public TypeInfo getParentCollectionType() {
        return this.parentCollectionType;
    }

    public void setParentCollectionType(TypeInfo parentCollectionType) {
        this.parentCollectionType = parentCollectionType;
    }

    public boolean isRepeatedElement() {
        return this.parentCollectionType != null;
    }

    public Type getGenericType() {
        return this.genericType;
    }

    public void setGenericType(Type genericType) {
        this.genericType = genericType;
    }

    public boolean isNillable() {
        return this.nillable;
    }

    public void setNillable(boolean nillable) {
        this.nillable = nillable;
    }

    public String toString() {
        return "TypeInfo: Type = " + ((Object) this.type) + ", tag = " + ((Object) this.tagName);
    }

    public TypeInfo getItemType() {
        if ((this.type instanceof Class) && ((Class) this.type).isArray() && !byte[].class.equals(this.type)) {
            Type componentType = ((Class) this.type).getComponentType();
            Type genericComponentType = null;
            if (this.genericType != null && (this.genericType instanceof GenericArrayType)) {
                GenericArrayType arrayType = (GenericArrayType) this.type;
                genericComponentType = arrayType.getGenericComponentType();
                componentType = arrayType.getGenericComponentType();
            }
            TypeInfo ti = new TypeInfo(this.tagName, componentType, this.annotations);
            if (genericComponentType != null) {
                ti.setGenericType(genericComponentType);
            }
            return ti;
        }
        Type t2 = this.genericType != null ? this.genericType : this.type;
        Type base = Utils.REFLECTION_NAVIGATOR.getBaseClass(t2, Collection.class);
        if (base != null) {
            return new TypeInfo(this.tagName, Utils.REFLECTION_NAVIGATOR.getTypeArgument(base, 0), this.annotations);
        }
        return null;
    }
}
