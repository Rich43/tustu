package com.sun.beans;

import java.lang.reflect.Array;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.util.HashMap;
import java.util.Map;
import sun.reflect.generics.reflectiveObjects.GenericArrayTypeImpl;
import sun.reflect.generics.reflectiveObjects.ParameterizedTypeImpl;

/* loaded from: rt.jar:com/sun/beans/TypeResolver.class */
public final class TypeResolver {
    private static final WeakCache<Type, Map<Type, Type>> CACHE;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !TypeResolver.class.desiredAssertionStatus();
        CACHE = new WeakCache<>();
    }

    public static Type resolveInClass(Class<?> cls, Type type) {
        return resolve(getActualType(cls), type);
    }

    public static Type[] resolveInClass(Class<?> cls, Type[] typeArr) {
        return resolve(getActualType(cls), typeArr);
    }

    public static Type resolve(Type type, Type type2) {
        Map<Type, Type> map;
        if (type2 instanceof Class) {
            return type2;
        }
        if (type2 instanceof GenericArrayType) {
            Type typeResolve = resolve(type, ((GenericArrayType) type2).getGenericComponentType());
            if (typeResolve instanceof Class) {
                return Array.newInstance((Class<?>) typeResolve, 0).getClass();
            }
            return GenericArrayTypeImpl.make(typeResolve);
        }
        if (type2 instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) type2;
            return ParameterizedTypeImpl.make((Class) parameterizedType.getRawType(), resolve(type, parameterizedType.getActualTypeArguments()), parameterizedType.getOwnerType());
        }
        if (type2 instanceof WildcardType) {
            WildcardType wildcardType = (WildcardType) type2;
            return new WildcardTypeImpl(resolve(type, wildcardType.getUpperBounds()), resolve(type, wildcardType.getLowerBounds()));
        }
        if (type2 instanceof TypeVariable) {
            synchronized (CACHE) {
                map = CACHE.get(type);
                if (map == null) {
                    map = new HashMap();
                    prepare(map, type);
                    CACHE.put(type, map);
                }
            }
            Type type3 = map.get(type2);
            if (type3 == null || type3.equals(type2)) {
                return type2;
            }
            return resolve(type, fixGenericArray(type3));
        }
        throw new IllegalArgumentException("Bad Type kind: " + ((Object) type2.getClass()));
    }

    public static Type[] resolve(Type type, Type[] typeArr) {
        int length = typeArr.length;
        Type[] typeArr2 = new Type[length];
        for (int i2 = 0; i2 < length; i2++) {
            typeArr2[i2] = resolve(type, typeArr[i2]);
        }
        return typeArr2;
    }

    public static Class<?> erase(Type type) {
        if (type instanceof Class) {
            return (Class) type;
        }
        if (type instanceof ParameterizedType) {
            return (Class) ((ParameterizedType) type).getRawType();
        }
        if (type instanceof TypeVariable) {
            Type[] bounds = ((TypeVariable) type).getBounds();
            return 0 < bounds.length ? erase(bounds[0]) : Object.class;
        }
        if (type instanceof WildcardType) {
            Type[] upperBounds = ((WildcardType) type).getUpperBounds();
            return 0 < upperBounds.length ? erase(upperBounds[0]) : Object.class;
        }
        if (type instanceof GenericArrayType) {
            return Array.newInstance(erase(((GenericArrayType) type).getGenericComponentType()), 0).getClass();
        }
        throw new IllegalArgumentException("Unknown Type kind: " + ((Object) type.getClass()));
    }

    public static Class[] erase(Type[] typeArr) {
        int length = typeArr.length;
        Class[] clsArr = new Class[length];
        for (int i2 = 0; i2 < length; i2++) {
            clsArr[i2] = erase(typeArr[i2]);
        }
        return clsArr;
    }

    private static void prepare(Map<Type, Type> map, Type type) {
        Class cls = (Class) (type instanceof Class ? type : ((ParameterizedType) type).getRawType());
        TypeVariable[] typeParameters = cls.getTypeParameters();
        Type[] actualTypeArguments = type instanceof Class ? typeParameters : ((ParameterizedType) type).getActualTypeArguments();
        if (!$assertionsDisabled && typeParameters.length != actualTypeArguments.length) {
            throw new AssertionError();
        }
        for (int i2 = 0; i2 < typeParameters.length; i2++) {
            map.put(typeParameters[i2], actualTypeArguments[i2]);
        }
        Type genericSuperclass = cls.getGenericSuperclass();
        if (genericSuperclass != null) {
            prepare(map, genericSuperclass);
        }
        for (Type type2 : cls.getGenericInterfaces()) {
            prepare(map, type2);
        }
        if ((type instanceof Class) && typeParameters.length > 0) {
            for (Map.Entry<Type, Type> entry : map.entrySet()) {
                entry.setValue(erase(entry.getValue()));
            }
        }
    }

    private static Type fixGenericArray(Type type) {
        if (type instanceof GenericArrayType) {
            Type typeFixGenericArray = fixGenericArray(((GenericArrayType) type).getGenericComponentType());
            if (typeFixGenericArray instanceof Class) {
                return Array.newInstance((Class<?>) typeFixGenericArray, 0).getClass();
            }
        }
        return type;
    }

    private static Type getActualType(Class<?> cls) {
        TypeVariable<Class<?>>[] typeParameters = cls.getTypeParameters();
        return typeParameters.length == 0 ? cls : ParameterizedTypeImpl.make(cls, typeParameters, cls.getEnclosingClass());
    }
}
