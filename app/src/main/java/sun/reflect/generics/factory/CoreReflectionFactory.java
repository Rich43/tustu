package sun.reflect.generics.factory;

import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.GenericDeclaration;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import sun.reflect.generics.reflectiveObjects.GenericArrayTypeImpl;
import sun.reflect.generics.reflectiveObjects.ParameterizedTypeImpl;
import sun.reflect.generics.reflectiveObjects.TypeVariableImpl;
import sun.reflect.generics.reflectiveObjects.WildcardTypeImpl;
import sun.reflect.generics.scope.Scope;
import sun.reflect.generics.tree.FieldTypeSignature;

/* loaded from: rt.jar:sun/reflect/generics/factory/CoreReflectionFactory.class */
public class CoreReflectionFactory implements GenericsFactory {
    private final GenericDeclaration decl;
    private final Scope scope;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !CoreReflectionFactory.class.desiredAssertionStatus();
    }

    private CoreReflectionFactory(GenericDeclaration genericDeclaration, Scope scope) {
        this.decl = genericDeclaration;
        this.scope = scope;
    }

    private GenericDeclaration getDecl() {
        return this.decl;
    }

    private Scope getScope() {
        return this.scope;
    }

    private ClassLoader getDeclsLoader() {
        if (this.decl instanceof Class) {
            return ((Class) this.decl).getClassLoader();
        }
        if (this.decl instanceof Method) {
            return ((Method) this.decl).getDeclaringClass().getClassLoader();
        }
        if ($assertionsDisabled || (this.decl instanceof Constructor)) {
            return ((Constructor) this.decl).getDeclaringClass().getClassLoader();
        }
        throw new AssertionError((Object) "Constructor expected");
    }

    public static CoreReflectionFactory make(GenericDeclaration genericDeclaration, Scope scope) {
        return new CoreReflectionFactory(genericDeclaration, scope);
    }

    @Override // sun.reflect.generics.factory.GenericsFactory
    public TypeVariable<?> makeTypeVariable(String str, FieldTypeSignature[] fieldTypeSignatureArr) {
        return TypeVariableImpl.make(getDecl(), str, fieldTypeSignatureArr, this);
    }

    @Override // sun.reflect.generics.factory.GenericsFactory
    public WildcardType makeWildcard(FieldTypeSignature[] fieldTypeSignatureArr, FieldTypeSignature[] fieldTypeSignatureArr2) {
        return WildcardTypeImpl.make(fieldTypeSignatureArr, fieldTypeSignatureArr2, this);
    }

    @Override // sun.reflect.generics.factory.GenericsFactory
    public ParameterizedType makeParameterizedType(Type type, Type[] typeArr, Type type2) {
        return ParameterizedTypeImpl.make((Class) type, typeArr, type2);
    }

    @Override // sun.reflect.generics.factory.GenericsFactory
    public TypeVariable<?> findTypeVariable(String str) {
        return getScope().lookup(str);
    }

    @Override // sun.reflect.generics.factory.GenericsFactory
    public Type makeNamedType(String str) {
        try {
            return Class.forName(str, false, getDeclsLoader());
        } catch (ClassNotFoundException e2) {
            throw new TypeNotPresentException(str, e2);
        }
    }

    @Override // sun.reflect.generics.factory.GenericsFactory
    public Type makeArrayType(Type type) {
        if (type instanceof Class) {
            return Array.newInstance((Class<?>) type, 0).getClass();
        }
        return GenericArrayTypeImpl.make(type);
    }

    @Override // sun.reflect.generics.factory.GenericsFactory
    public Type makeByte() {
        return Byte.TYPE;
    }

    @Override // sun.reflect.generics.factory.GenericsFactory
    public Type makeBool() {
        return Boolean.TYPE;
    }

    @Override // sun.reflect.generics.factory.GenericsFactory
    public Type makeShort() {
        return Short.TYPE;
    }

    @Override // sun.reflect.generics.factory.GenericsFactory
    public Type makeChar() {
        return Character.TYPE;
    }

    @Override // sun.reflect.generics.factory.GenericsFactory
    public Type makeInt() {
        return Integer.TYPE;
    }

    @Override // sun.reflect.generics.factory.GenericsFactory
    public Type makeLong() {
        return Long.TYPE;
    }

    @Override // sun.reflect.generics.factory.GenericsFactory
    public Type makeFloat() {
        return Float.TYPE;
    }

    @Override // sun.reflect.generics.factory.GenericsFactory
    public Type makeDouble() {
        return Double.TYPE;
    }

    @Override // sun.reflect.generics.factory.GenericsFactory
    public Type makeVoid() {
        return Void.TYPE;
    }
}
