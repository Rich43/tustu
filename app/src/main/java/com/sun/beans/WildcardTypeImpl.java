package com.sun.beans;

import java.lang.reflect.Type;
import java.lang.reflect.WildcardType;
import java.util.Arrays;

/* loaded from: rt.jar:com/sun/beans/WildcardTypeImpl.class */
final class WildcardTypeImpl implements WildcardType {
    private final Type[] upperBounds;
    private final Type[] lowerBounds;

    WildcardTypeImpl(Type[] typeArr, Type[] typeArr2) {
        this.upperBounds = typeArr;
        this.lowerBounds = typeArr2;
    }

    @Override // java.lang.reflect.WildcardType
    public Type[] getUpperBounds() {
        return (Type[]) this.upperBounds.clone();
    }

    @Override // java.lang.reflect.WildcardType
    public Type[] getLowerBounds() {
        return (Type[]) this.lowerBounds.clone();
    }

    public boolean equals(Object obj) {
        if (obj instanceof WildcardType) {
            WildcardType wildcardType = (WildcardType) obj;
            return Arrays.equals(this.upperBounds, wildcardType.getUpperBounds()) && Arrays.equals(this.lowerBounds, wildcardType.getLowerBounds());
        }
        return false;
    }

    public int hashCode() {
        return Arrays.hashCode(this.upperBounds) ^ Arrays.hashCode(this.lowerBounds);
    }

    public String toString() {
        Type[] typeArr;
        StringBuilder sb;
        String string;
        if (this.lowerBounds.length == 0) {
            if (this.upperBounds.length == 0 || Object.class == this.upperBounds[0]) {
                return "?";
            }
            typeArr = this.upperBounds;
            sb = new StringBuilder("? extends ");
        } else {
            typeArr = this.lowerBounds;
            sb = new StringBuilder("? super ");
        }
        for (int i2 = 0; i2 < typeArr.length; i2++) {
            if (i2 > 0) {
                sb.append(" & ");
            }
            StringBuilder sb2 = sb;
            if (typeArr[i2] instanceof Class) {
                string = ((Class) typeArr[i2]).getName();
            } else {
                string = typeArr[i2].toString();
            }
            sb2.append(string);
        }
        return sb.toString();
    }
}
