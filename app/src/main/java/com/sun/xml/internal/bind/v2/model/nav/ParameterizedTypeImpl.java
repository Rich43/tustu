package com.sun.xml.internal.bind.v2.model.nav;

import java.lang.reflect.MalformedParameterizedTypeException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.Arrays;
import javafx.fxml.FXMLLoader;

/* loaded from: rt.jar:com/sun/xml/internal/bind/v2/model/nav/ParameterizedTypeImpl.class */
class ParameterizedTypeImpl implements ParameterizedType {
    private Type[] actualTypeArguments;
    private Class<?> rawType;
    private Type ownerType;

    ParameterizedTypeImpl(Class<?> rawType, Type[] actualTypeArguments, Type ownerType) {
        this.actualTypeArguments = actualTypeArguments;
        this.rawType = rawType;
        if (ownerType != null) {
            this.ownerType = ownerType;
        } else {
            this.ownerType = rawType.getDeclaringClass();
        }
        validateConstructorArguments();
    }

    private void validateConstructorArguments() {
        TypeVariable[] formals = this.rawType.getTypeParameters();
        if (formals.length != this.actualTypeArguments.length) {
            throw new MalformedParameterizedTypeException();
        }
    }

    @Override // java.lang.reflect.ParameterizedType
    public Type[] getActualTypeArguments() {
        return (Type[]) this.actualTypeArguments.clone();
    }

    @Override // java.lang.reflect.ParameterizedType
    public Class<?> getRawType() {
        return this.rawType;
    }

    @Override // java.lang.reflect.ParameterizedType
    public Type getOwnerType() {
        return this.ownerType;
    }

    public boolean equals(Object o2) {
        if (o2 instanceof ParameterizedType) {
            ParameterizedType that = (ParameterizedType) o2;
            if (this == that) {
                return true;
            }
            Type thatOwner = that.getOwnerType();
            Type thatRawType = that.getRawType();
            if (this.ownerType != null ? this.ownerType.equals(thatOwner) : thatOwner == null) {
                if (this.rawType != null ? this.rawType.equals(thatRawType) : thatRawType == null) {
                    if (Arrays.equals(this.actualTypeArguments, that.getActualTypeArguments())) {
                        return true;
                    }
                }
            }
            return false;
        }
        return false;
    }

    public int hashCode() {
        return (Arrays.hashCode(this.actualTypeArguments) ^ (this.ownerType == null ? 0 : this.ownerType.hashCode())) ^ (this.rawType == null ? 0 : this.rawType.hashCode());
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (this.ownerType != null) {
            if (this.ownerType instanceof Class) {
                sb.append(((Class) this.ownerType).getName());
            } else {
                sb.append(this.ownerType.toString());
            }
            sb.append(".");
            if (this.ownerType instanceof ParameterizedTypeImpl) {
                sb.append(this.rawType.getName().replace(((ParameterizedTypeImpl) this.ownerType).rawType.getName() + FXMLLoader.EXPRESSION_PREFIX, ""));
            } else {
                sb.append(this.rawType.getName());
            }
        } else {
            sb.append(this.rawType.getName());
        }
        if (this.actualTypeArguments != null && this.actualTypeArguments.length > 0) {
            sb.append("<");
            boolean first = true;
            for (Type t2 : this.actualTypeArguments) {
                if (!first) {
                    sb.append(", ");
                }
                if (t2 instanceof Class) {
                    sb.append(((Class) t2).getName());
                } else {
                    sb.append(t2.toString());
                }
                first = false;
            }
            sb.append(">");
        }
        return sb.toString();
    }
}
