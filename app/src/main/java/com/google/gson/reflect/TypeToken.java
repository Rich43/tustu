package com.google.gson.reflect;

import com.google.gson.internal.C$Gson$Preconditions;
import com.google.gson.internal.C$Gson$Types;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.HashMap;
import java.util.Map;

/* loaded from: gson-2.9.0.jar:com/google/gson/reflect/TypeToken.class */
public class TypeToken<T> {
    final Class<? super T> rawType;
    final Type type;
    final int hashCode;

    protected TypeToken() {
        this.type = getSuperclassTypeParameter(getClass());
        this.rawType = (Class<? super T>) C$Gson$Types.getRawType(this.type);
        this.hashCode = this.type.hashCode();
    }

    TypeToken(Type type) {
        this.type = C$Gson$Types.canonicalize((Type) C$Gson$Preconditions.checkNotNull(type));
        this.rawType = (Class<? super T>) C$Gson$Types.getRawType(this.type);
        this.hashCode = this.type.hashCode();
    }

    static Type getSuperclassTypeParameter(Class<?> subclass) {
        Type superclass = subclass.getGenericSuperclass();
        if (superclass instanceof Class) {
            throw new RuntimeException("Missing type parameter.");
        }
        ParameterizedType parameterized = (ParameterizedType) superclass;
        return C$Gson$Types.canonicalize(parameterized.getActualTypeArguments()[0]);
    }

    public final Class<? super T> getRawType() {
        return this.rawType;
    }

    public final Type getType() {
        return this.type;
    }

    @Deprecated
    public boolean isAssignableFrom(Class<?> cls) {
        return isAssignableFrom((Type) cls);
    }

    @Deprecated
    public boolean isAssignableFrom(Type from) {
        if (from == null) {
            return false;
        }
        if (this.type.equals(from)) {
            return true;
        }
        if (this.type instanceof Class) {
            return this.rawType.isAssignableFrom(C$Gson$Types.getRawType(from));
        }
        if (this.type instanceof ParameterizedType) {
            return isAssignableFrom(from, (ParameterizedType) this.type, new HashMap());
        }
        if (this.type instanceof GenericArrayType) {
            return this.rawType.isAssignableFrom(C$Gson$Types.getRawType(from)) && isAssignableFrom(from, (GenericArrayType) this.type);
        }
        throw buildUnexpectedTypeError(this.type, Class.class, ParameterizedType.class, GenericArrayType.class);
    }

    @Deprecated
    public boolean isAssignableFrom(TypeToken<?> token) {
        return isAssignableFrom(token.getType());
    }

    private static boolean isAssignableFrom(Type from, GenericArrayType to) {
        Class<?> classType;
        Type toGenericComponentType = to.getGenericComponentType();
        if (toGenericComponentType instanceof ParameterizedType) {
            Type t2 = from;
            if (from instanceof GenericArrayType) {
                t2 = ((GenericArrayType) from).getGenericComponentType();
            } else if (from instanceof Class) {
                Class<?> componentType = (Class) from;
                while (true) {
                    classType = componentType;
                    if (!classType.isArray()) {
                        break;
                    }
                    componentType = classType.getComponentType();
                }
                t2 = classType;
            }
            return isAssignableFrom(t2, (ParameterizedType) toGenericComponentType, new HashMap());
        }
        return true;
    }

    private static boolean isAssignableFrom(Type from, ParameterizedType to, Map<String, Type> typeVarMap) {
        if (from == null) {
            return false;
        }
        if (to.equals(from)) {
            return true;
        }
        Class<?> clazz = C$Gson$Types.getRawType(from);
        ParameterizedType ptype = null;
        if (from instanceof ParameterizedType) {
            ptype = (ParameterizedType) from;
        }
        if (ptype != null) {
            Type[] tArgs = ptype.getActualTypeArguments();
            TypeVariable<?>[] tParams = clazz.getTypeParameters();
            for (int i2 = 0; i2 < tArgs.length; i2++) {
                Type arg = tArgs[i2];
                TypeVariable<?> var = tParams[i2];
                while (arg instanceof TypeVariable) {
                    TypeVariable<?> v2 = (TypeVariable) arg;
                    arg = typeVarMap.get(v2.getName());
                }
                typeVarMap.put(var.getName(), arg);
            }
            if (typeEquals(ptype, to, typeVarMap)) {
                return true;
            }
        }
        for (Type itype : clazz.getGenericInterfaces()) {
            if (isAssignableFrom(itype, to, new HashMap(typeVarMap))) {
                return true;
            }
        }
        Type sType = clazz.getGenericSuperclass();
        return isAssignableFrom(sType, to, new HashMap(typeVarMap));
    }

    private static boolean typeEquals(ParameterizedType from, ParameterizedType to, Map<String, Type> typeVarMap) {
        if (from.getRawType().equals(to.getRawType())) {
            Type[] fromArgs = from.getActualTypeArguments();
            Type[] toArgs = to.getActualTypeArguments();
            for (int i2 = 0; i2 < fromArgs.length; i2++) {
                if (!matches(fromArgs[i2], toArgs[i2], typeVarMap)) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    private static AssertionError buildUnexpectedTypeError(Type token, Class<?>... expected) {
        StringBuilder exceptionMessage = new StringBuilder("Unexpected type. Expected one of: ");
        for (Class<?> clazz : expected) {
            exceptionMessage.append(clazz.getName()).append(", ");
        }
        exceptionMessage.append("but got: ").append(token.getClass().getName()).append(", for type token: ").append(token.toString()).append('.');
        return new AssertionError((Object) exceptionMessage.toString());
    }

    private static boolean matches(Type from, Type to, Map<String, Type> typeMap) {
        return to.equals(from) || ((from instanceof TypeVariable) && to.equals(typeMap.get(((TypeVariable) from).getName())));
    }

    public final int hashCode() {
        return this.hashCode;
    }

    public final boolean equals(Object o2) {
        return (o2 instanceof TypeToken) && C$Gson$Types.equals(this.type, ((TypeToken) o2).type);
    }

    public final String toString() {
        return C$Gson$Types.typeToString(this.type);
    }

    public static TypeToken<?> get(Type type) {
        return new TypeToken<>(type);
    }

    public static <T> TypeToken<T> get(Class<T> type) {
        return new TypeToken<>(type);
    }

    public static TypeToken<?> getParameterized(Type rawType, Type... typeArguments) {
        return new TypeToken<>(C$Gson$Types.newParameterizedTypeWithOwner(null, rawType, typeArguments));
    }

    public static TypeToken<?> getArray(Type componentType) {
        return new TypeToken<>(C$Gson$Types.arrayOf(componentType));
    }
}
