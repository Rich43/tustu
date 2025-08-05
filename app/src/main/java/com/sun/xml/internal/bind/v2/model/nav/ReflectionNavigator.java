package com.sun.xml.internal.bind.v2.model.nav;

import com.sun.xml.internal.bind.v2.runtime.Location;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.GenericDeclaration;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.util.Arrays;
import java.util.Collection;

/* loaded from: rt.jar:com/sun/xml/internal/bind/v2/model/nav/ReflectionNavigator.class */
final class ReflectionNavigator implements Navigator<Type, Class, Field, Method> {
    private static final ReflectionNavigator INSTANCE;
    private static final TypeVisitor<Type, Class> baseClassFinder;
    private static final TypeVisitor<Type, BinderArg> binder;
    private static final TypeVisitor<Class, Void> eraser;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !ReflectionNavigator.class.desiredAssertionStatus();
        INSTANCE = new ReflectionNavigator();
        baseClassFinder = new TypeVisitor<Type, Class>() { // from class: com.sun.xml.internal.bind.v2.model.nav.ReflectionNavigator.1
            @Override // com.sun.xml.internal.bind.v2.model.nav.TypeVisitor
            public Type onClass(Class c2, Class sup) {
                Type r2;
                if (sup == c2) {
                    return sup;
                }
                Type sc = c2.getGenericSuperclass();
                if (sc != null && (r2 = visit(sc, sup)) != null) {
                    return r2;
                }
                for (Type i2 : c2.getGenericInterfaces()) {
                    Type r3 = visit(i2, sup);
                    if (r3 != null) {
                        return r3;
                    }
                }
                return null;
            }

            @Override // com.sun.xml.internal.bind.v2.model.nav.TypeVisitor
            public Type onParameterizdType(ParameterizedType p2, Class sup) {
                Class raw = (Class) p2.getRawType();
                if (raw == sup) {
                    return p2;
                }
                Type r2 = raw.getGenericSuperclass();
                if (r2 != null) {
                    r2 = visit(bind(r2, raw, p2), sup);
                }
                if (r2 != null) {
                    return r2;
                }
                for (Type i2 : raw.getGenericInterfaces()) {
                    Type r3 = visit(bind(i2, raw, p2), sup);
                    if (r3 != null) {
                        return r3;
                    }
                }
                return null;
            }

            @Override // com.sun.xml.internal.bind.v2.model.nav.TypeVisitor
            public Type onGenericArray(GenericArrayType g2, Class sup) {
                return null;
            }

            @Override // com.sun.xml.internal.bind.v2.model.nav.TypeVisitor
            public Type onVariable(TypeVariable v2, Class sup) {
                return visit(v2.getBounds()[0], sup);
            }

            @Override // com.sun.xml.internal.bind.v2.model.nav.TypeVisitor
            public Type onWildcard(WildcardType w2, Class sup) {
                return null;
            }

            private Type bind(Type t2, GenericDeclaration decl, ParameterizedType args) {
                return (Type) ReflectionNavigator.binder.visit(t2, new BinderArg(decl, args.getActualTypeArguments()));
            }
        };
        binder = new TypeVisitor<Type, BinderArg>() { // from class: com.sun.xml.internal.bind.v2.model.nav.ReflectionNavigator.2
            @Override // com.sun.xml.internal.bind.v2.model.nav.TypeVisitor
            public Type onClass(Class c2, BinderArg args) {
                return c2;
            }

            @Override // com.sun.xml.internal.bind.v2.model.nav.TypeVisitor
            public Type onParameterizdType(ParameterizedType p2, BinderArg args) {
                Type[] params = p2.getActualTypeArguments();
                boolean different = false;
                for (int i2 = 0; i2 < params.length; i2++) {
                    Type t2 = params[i2];
                    params[i2] = visit(t2, args);
                    different |= t2 != params[i2];
                }
                Type newOwner = p2.getOwnerType();
                if (newOwner != null) {
                    newOwner = visit(newOwner, args);
                }
                if (!(different | (p2.getOwnerType() != newOwner))) {
                    return p2;
                }
                return new ParameterizedTypeImpl((Class) p2.getRawType(), params, newOwner);
            }

            @Override // com.sun.xml.internal.bind.v2.model.nav.TypeVisitor
            public Type onGenericArray(GenericArrayType g2, BinderArg types) {
                Type c2 = visit(g2.getGenericComponentType(), types);
                if (c2 == g2.getGenericComponentType()) {
                    return g2;
                }
                return new GenericArrayTypeImpl(c2);
            }

            @Override // com.sun.xml.internal.bind.v2.model.nav.TypeVisitor
            public Type onVariable(TypeVariable v2, BinderArg types) {
                return types.replace(v2);
            }

            @Override // com.sun.xml.internal.bind.v2.model.nav.TypeVisitor
            public Type onWildcard(WildcardType w2, BinderArg types) {
                Type[] lb = w2.getLowerBounds();
                Type[] ub = w2.getUpperBounds();
                boolean diff = false;
                for (int i2 = 0; i2 < lb.length; i2++) {
                    Type t2 = lb[i2];
                    lb[i2] = visit(t2, types);
                    diff |= t2 != lb[i2];
                }
                for (int i3 = 0; i3 < ub.length; i3++) {
                    Type t3 = ub[i3];
                    ub[i3] = visit(t3, types);
                    diff |= t3 != ub[i3];
                }
                if (!diff) {
                    return w2;
                }
                return new WildcardTypeImpl(lb, ub);
            }
        };
        eraser = new TypeVisitor<Class, Void>() { // from class: com.sun.xml.internal.bind.v2.model.nav.ReflectionNavigator.3
            @Override // com.sun.xml.internal.bind.v2.model.nav.TypeVisitor
            public Class onClass(Class c2, Void v2) {
                return c2;
            }

            @Override // com.sun.xml.internal.bind.v2.model.nav.TypeVisitor
            public Class onParameterizdType(ParameterizedType p2, Void v2) {
                return visit(p2.getRawType(), null);
            }

            @Override // com.sun.xml.internal.bind.v2.model.nav.TypeVisitor
            public Class onGenericArray(GenericArrayType g2, Void v2) {
                return Array.newInstance((Class<?>) visit(g2.getGenericComponentType(), null), 0).getClass();
            }

            @Override // com.sun.xml.internal.bind.v2.model.nav.TypeVisitor
            public Class onVariable(TypeVariable tv, Void v2) {
                return visit(tv.getBounds()[0], null);
            }

            @Override // com.sun.xml.internal.bind.v2.model.nav.TypeVisitor
            public Class onWildcard(WildcardType w2, Void v2) {
                return visit(w2.getUpperBounds()[0], null);
            }
        };
    }

    static ReflectionNavigator getInstance() {
        return INSTANCE;
    }

    private ReflectionNavigator() {
    }

    @Override // com.sun.xml.internal.bind.v2.model.nav.Navigator
    public Class getSuperClass(Class clazz) {
        if (clazz == Object.class) {
            return null;
        }
        Class sc = clazz.getSuperclass();
        if (sc == null) {
            sc = Object.class;
        }
        return sc;
    }

    /* loaded from: rt.jar:com/sun/xml/internal/bind/v2/model/nav/ReflectionNavigator$BinderArg.class */
    private static class BinderArg {
        final TypeVariable[] params;
        final Type[] args;
        static final /* synthetic */ boolean $assertionsDisabled;

        static {
            $assertionsDisabled = !ReflectionNavigator.class.desiredAssertionStatus();
        }

        BinderArg(TypeVariable[] params, Type[] args) {
            this.params = params;
            this.args = args;
            if (!$assertionsDisabled && params.length != args.length) {
                throw new AssertionError();
            }
        }

        public BinderArg(GenericDeclaration decl, Type[] args) {
            this(decl.getTypeParameters(), args);
        }

        Type replace(TypeVariable v2) {
            for (int i2 = 0; i2 < this.params.length; i2++) {
                if (this.params[i2].equals(v2)) {
                    return this.args[i2];
                }
            }
            return v2;
        }
    }

    @Override // com.sun.xml.internal.bind.v2.model.nav.Navigator
    public Type getBaseClass(Type t2, Class sup) {
        return baseClassFinder.visit(t2, sup);
    }

    @Override // com.sun.xml.internal.bind.v2.model.nav.Navigator
    public String getClassName(Class clazz) {
        return clazz.getName();
    }

    @Override // com.sun.xml.internal.bind.v2.model.nav.Navigator
    public String getTypeName(Type type) {
        if (type instanceof Class) {
            Class c2 = (Class) type;
            if (c2.isArray()) {
                return getTypeName((Type) c2.getComponentType()) + "[]";
            }
            return c2.getName();
        }
        return type.toString();
    }

    @Override // com.sun.xml.internal.bind.v2.model.nav.Navigator
    public String getClassShortName(Class clazz) {
        return clazz.getSimpleName();
    }

    @Override // com.sun.xml.internal.bind.v2.model.nav.Navigator
    public Collection<? extends Field> getDeclaredFields(Class clazz) {
        return Arrays.asList(clazz.getDeclaredFields());
    }

    @Override // com.sun.xml.internal.bind.v2.model.nav.Navigator
    public Field getDeclaredField(Class clazz, String fieldName) {
        try {
            return clazz.getDeclaredField(fieldName);
        } catch (NoSuchFieldException e2) {
            return null;
        }
    }

    @Override // com.sun.xml.internal.bind.v2.model.nav.Navigator
    public Collection<? extends Method> getDeclaredMethods(Class clazz) {
        return Arrays.asList(clazz.getDeclaredMethods());
    }

    @Override // com.sun.xml.internal.bind.v2.model.nav.Navigator
    public Class getDeclaringClassForField(Field field) {
        return field.getDeclaringClass();
    }

    @Override // com.sun.xml.internal.bind.v2.model.nav.Navigator
    public Class getDeclaringClassForMethod(Method method) {
        return method.getDeclaringClass();
    }

    @Override // com.sun.xml.internal.bind.v2.model.nav.Navigator
    public Type getFieldType(Field field) {
        if (field.getType().isArray()) {
            Class c2 = field.getType().getComponentType();
            if (c2.isPrimitive()) {
                return Array.newInstance((Class<?>) c2, 0).getClass();
            }
        }
        return fix(field.getGenericType());
    }

    @Override // com.sun.xml.internal.bind.v2.model.nav.Navigator
    public String getFieldName(Field field) {
        return field.getName();
    }

    @Override // com.sun.xml.internal.bind.v2.model.nav.Navigator
    public String getMethodName(Method method) {
        return method.getName();
    }

    @Override // com.sun.xml.internal.bind.v2.model.nav.Navigator
    public Type getReturnType(Method method) {
        return fix(method.getGenericReturnType());
    }

    @Override // com.sun.xml.internal.bind.v2.model.nav.Navigator
    public Type[] getMethodParameters(Method method) {
        return method.getGenericParameterTypes();
    }

    @Override // com.sun.xml.internal.bind.v2.model.nav.Navigator
    public boolean isStaticMethod(Method method) {
        return Modifier.isStatic(method.getModifiers());
    }

    @Override // com.sun.xml.internal.bind.v2.model.nav.Navigator
    public boolean isFinalMethod(Method method) {
        return Modifier.isFinal(method.getModifiers());
    }

    @Override // com.sun.xml.internal.bind.v2.model.nav.Navigator
    public boolean isSubClassOf(Type sub, Type sup) {
        return erasure(sup).isAssignableFrom(erasure(sub));
    }

    @Override // com.sun.xml.internal.bind.v2.model.nav.Navigator
    /* renamed from: ref, reason: merged with bridge method [inline-methods] */
    public Type ref2(Class c2) {
        return c2;
    }

    @Override // com.sun.xml.internal.bind.v2.model.nav.Navigator
    public Class use(Class c2) {
        return c2;
    }

    @Override // com.sun.xml.internal.bind.v2.model.nav.Navigator
    public Class asDecl(Type t2) {
        return erasure(t2);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.sun.xml.internal.bind.v2.model.nav.Navigator
    public Class asDecl(Class c2) {
        return c2;
    }

    @Override // com.sun.xml.internal.bind.v2.model.nav.Navigator
    public <T> Class<T> erasure(Type t2) {
        return eraser.visit(t2, null);
    }

    @Override // com.sun.xml.internal.bind.v2.model.nav.Navigator
    public boolean isAbstract(Class clazz) {
        return Modifier.isAbstract(clazz.getModifiers());
    }

    @Override // com.sun.xml.internal.bind.v2.model.nav.Navigator
    public boolean isFinal(Class clazz) {
        return Modifier.isFinal(clazz.getModifiers());
    }

    public Type createParameterizedType(Class rawType, Type... arguments) {
        return new ParameterizedTypeImpl(rawType, arguments, null);
    }

    @Override // com.sun.xml.internal.bind.v2.model.nav.Navigator
    public boolean isArray(Type t2) {
        if (t2 instanceof Class) {
            Class c2 = (Class) t2;
            return c2.isArray();
        }
        if (t2 instanceof GenericArrayType) {
            return true;
        }
        return false;
    }

    @Override // com.sun.xml.internal.bind.v2.model.nav.Navigator
    public boolean isArrayButNotByteArray(Type t2) {
        if (!(t2 instanceof Class)) {
            return (t2 instanceof GenericArrayType) && ((GenericArrayType) t2).getGenericComponentType() != Byte.TYPE;
        }
        Class c2 = (Class) t2;
        return c2.isArray() && c2 != byte[].class;
    }

    @Override // com.sun.xml.internal.bind.v2.model.nav.Navigator
    public Type getComponentType(Type t2) {
        if (t2 instanceof Class) {
            Class c2 = (Class) t2;
            return c2.getComponentType();
        }
        if (t2 instanceof GenericArrayType) {
            return ((GenericArrayType) t2).getGenericComponentType();
        }
        throw new IllegalArgumentException();
    }

    @Override // com.sun.xml.internal.bind.v2.model.nav.Navigator
    public Type getTypeArgument(Type type, int i2) {
        if (type instanceof ParameterizedType) {
            ParameterizedType p2 = (ParameterizedType) type;
            return fix(p2.getActualTypeArguments()[i2]);
        }
        throw new IllegalArgumentException();
    }

    @Override // com.sun.xml.internal.bind.v2.model.nav.Navigator
    public boolean isParameterizedType(Type type) {
        return type instanceof ParameterizedType;
    }

    @Override // com.sun.xml.internal.bind.v2.model.nav.Navigator
    public boolean isPrimitive(Type type) {
        if (type instanceof Class) {
            Class c2 = (Class) type;
            return c2.isPrimitive();
        }
        return false;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.sun.xml.internal.bind.v2.model.nav.Navigator
    public Type getPrimitive(Class primitiveType) {
        if ($assertionsDisabled || primitiveType.isPrimitive()) {
            return primitiveType;
        }
        throw new AssertionError();
    }

    @Override // com.sun.xml.internal.bind.v2.model.nav.Navigator
    public Location getClassLocation(final Class clazz) {
        return new Location() { // from class: com.sun.xml.internal.bind.v2.model.nav.ReflectionNavigator.4
            @Override // com.sun.xml.internal.bind.v2.runtime.Location
            public String toString() {
                return clazz.getName();
            }
        };
    }

    @Override // com.sun.xml.internal.bind.v2.model.nav.Navigator
    public Location getFieldLocation(final Field field) {
        return new Location() { // from class: com.sun.xml.internal.bind.v2.model.nav.ReflectionNavigator.5
            @Override // com.sun.xml.internal.bind.v2.runtime.Location
            public String toString() {
                return field.toString();
            }
        };
    }

    @Override // com.sun.xml.internal.bind.v2.model.nav.Navigator
    public Location getMethodLocation(final Method method) {
        return new Location() { // from class: com.sun.xml.internal.bind.v2.model.nav.ReflectionNavigator.6
            @Override // com.sun.xml.internal.bind.v2.runtime.Location
            public String toString() {
                return method.toString();
            }
        };
    }

    @Override // com.sun.xml.internal.bind.v2.model.nav.Navigator
    public boolean hasDefaultConstructor(Class c2) throws SecurityException {
        try {
            c2.getDeclaredConstructor(new Class[0]);
            return true;
        } catch (NoSuchMethodException e2) {
            return false;
        }
    }

    @Override // com.sun.xml.internal.bind.v2.model.nav.Navigator
    public boolean isStaticField(Field field) {
        return Modifier.isStatic(field.getModifiers());
    }

    @Override // com.sun.xml.internal.bind.v2.model.nav.Navigator
    public boolean isPublicMethod(Method method) {
        return Modifier.isPublic(method.getModifiers());
    }

    @Override // com.sun.xml.internal.bind.v2.model.nav.Navigator
    public boolean isPublicField(Field field) {
        return Modifier.isPublic(field.getModifiers());
    }

    @Override // com.sun.xml.internal.bind.v2.model.nav.Navigator
    public boolean isEnum(Class c2) {
        return Enum.class.isAssignableFrom(c2);
    }

    @Override // com.sun.xml.internal.bind.v2.model.nav.Navigator
    public Field[] getEnumConstants(Class clazz) {
        try {
            Object[] values = clazz.getEnumConstants();
            Field[] fields = new Field[values.length];
            for (int i2 = 0; i2 < values.length; i2++) {
                fields[i2] = clazz.getField(((Enum) values[i2]).name());
            }
            return fields;
        } catch (NoSuchFieldException e2) {
            throw new NoSuchFieldError(e2.getMessage());
        }
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.sun.xml.internal.bind.v2.model.nav.Navigator
    public Type getVoidType() {
        return Void.class;
    }

    @Override // com.sun.xml.internal.bind.v2.model.nav.Navigator
    public String getPackageName(Class clazz) {
        String name = clazz.getName();
        int idx = name.lastIndexOf(46);
        if (idx < 0) {
            return "";
        }
        return name.substring(0, idx);
    }

    @Override // com.sun.xml.internal.bind.v2.model.nav.Navigator
    public Class loadObjectFactory(Class referencePoint, String pkg) {
        ClassLoader cl = SecureLoader.getClassClassLoader(referencePoint);
        if (cl == null) {
            cl = SecureLoader.getSystemClassLoader();
        }
        try {
            return cl.loadClass(pkg + ".ObjectFactory");
        } catch (ClassNotFoundException e2) {
            return null;
        }
    }

    @Override // com.sun.xml.internal.bind.v2.model.nav.Navigator
    public boolean isBridgeMethod(Method method) {
        return method.isBridge();
    }

    @Override // com.sun.xml.internal.bind.v2.model.nav.Navigator
    public boolean isOverriding(Method method, Class base) {
        String name = method.getName();
        Class[] params = method.getParameterTypes();
        while (base != null) {
            if (base.getDeclaredMethod(name, params) != null) {
                return true;
            }
            base = base.getSuperclass();
        }
        return false;
    }

    @Override // com.sun.xml.internal.bind.v2.model.nav.Navigator
    public boolean isInterface(Class clazz) {
        return clazz.isInterface();
    }

    @Override // com.sun.xml.internal.bind.v2.model.nav.Navigator
    public boolean isTransient(Field f2) {
        return Modifier.isTransient(f2.getModifiers());
    }

    @Override // com.sun.xml.internal.bind.v2.model.nav.Navigator
    public boolean isInnerClass(Class clazz) {
        return (clazz.getEnclosingClass() == null || Modifier.isStatic(clazz.getModifiers())) ? false : true;
    }

    @Override // com.sun.xml.internal.bind.v2.model.nav.Navigator
    public boolean isSameType(Type t1, Type t2) {
        return t1.equals(t2);
    }

    private Type fix(Type t2) {
        if (!(t2 instanceof GenericArrayType)) {
            return t2;
        }
        GenericArrayType gat = (GenericArrayType) t2;
        if (gat.getGenericComponentType() instanceof Class) {
            Class c2 = (Class) gat.getGenericComponentType();
            return Array.newInstance((Class<?>) c2, 0).getClass();
        }
        return t2;
    }
}
