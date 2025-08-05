package com.sun.beans.finder;

import com.sun.beans.util.Cache;
import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import sun.reflect.misc.ReflectUtil;

/* loaded from: rt.jar:com/sun/beans/finder/ConstructorFinder.class */
public final class ConstructorFinder extends AbstractFinder<Constructor<?>> {
    private static final Cache<Signature, Constructor<?>> CACHE = new Cache<Signature, Constructor<?>>(Cache.Kind.SOFT, Cache.Kind.SOFT) { // from class: com.sun.beans.finder.ConstructorFinder.1
        @Override // com.sun.beans.util.Cache
        public Constructor create(Signature signature) {
            try {
                return new ConstructorFinder(signature.getArgs()).find(signature.getType().getConstructors());
            } catch (Exception e2) {
                throw new SignatureException(e2);
            }
        }
    };

    public static Constructor<?> findConstructor(Class<?> cls, Class<?>... clsArr) throws NoSuchMethodException {
        if (cls.isPrimitive()) {
            throw new NoSuchMethodException("Primitive wrapper does not contain constructors");
        }
        if (cls.isInterface()) {
            throw new NoSuchMethodException("Interface does not contain constructors");
        }
        if (Modifier.isAbstract(cls.getModifiers())) {
            throw new NoSuchMethodException("Abstract class cannot be instantiated");
        }
        if (!Modifier.isPublic(cls.getModifiers()) || !ReflectUtil.isPackageAccessible(cls)) {
            throw new NoSuchMethodException("Class is not accessible");
        }
        PrimitiveWrapperMap.replacePrimitivesWithWrappers(clsArr);
        try {
            return CACHE.get(new Signature(cls, clsArr));
        } catch (SignatureException e2) {
            throw e2.toNoSuchMethodException("Constructor is not found");
        }
    }

    private ConstructorFinder(Class<?>[] clsArr) {
        super(clsArr);
    }
}
