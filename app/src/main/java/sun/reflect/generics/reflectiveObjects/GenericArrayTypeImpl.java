package sun.reflect.generics.reflectiveObjects;

import java.lang.reflect.GenericArrayType;
import java.lang.reflect.Type;
import java.util.Objects;

/* loaded from: rt.jar:sun/reflect/generics/reflectiveObjects/GenericArrayTypeImpl.class */
public class GenericArrayTypeImpl implements GenericArrayType {
    private final Type genericComponentType;

    private GenericArrayTypeImpl(Type type) {
        this.genericComponentType = type;
    }

    public static GenericArrayTypeImpl make(Type type) {
        return new GenericArrayTypeImpl(type);
    }

    @Override // java.lang.reflect.GenericArrayType
    public Type getGenericComponentType() {
        return this.genericComponentType;
    }

    public String toString() {
        Type genericComponentType = getGenericComponentType();
        StringBuilder sb = new StringBuilder();
        if (genericComponentType instanceof Class) {
            sb.append(((Class) genericComponentType).getName());
        } else {
            sb.append(genericComponentType.toString());
        }
        sb.append("[]");
        return sb.toString();
    }

    public boolean equals(Object obj) {
        if (obj instanceof GenericArrayType) {
            return Objects.equals(this.genericComponentType, ((GenericArrayType) obj).getGenericComponentType());
        }
        return false;
    }

    public int hashCode() {
        return Objects.hashCode(this.genericComponentType);
    }
}
