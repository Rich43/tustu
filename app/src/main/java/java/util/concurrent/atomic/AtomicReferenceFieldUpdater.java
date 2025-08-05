package java.util.concurrent.atomic;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.security.AccessController;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.util.Objects;
import java.util.function.BinaryOperator;
import java.util.function.UnaryOperator;
import sun.misc.Unsafe;
import sun.reflect.CallerSensitive;
import sun.reflect.Reflection;
import sun.reflect.misc.ReflectUtil;

/* loaded from: rt.jar:java/util/concurrent/atomic/AtomicReferenceFieldUpdater.class */
public abstract class AtomicReferenceFieldUpdater<T, V> {
    public abstract boolean compareAndSet(T t2, V v2, V v3);

    public abstract boolean weakCompareAndSet(T t2, V v2, V v3);

    public abstract void set(T t2, V v2);

    public abstract void lazySet(T t2, V v2);

    public abstract V get(T t2);

    @CallerSensitive
    public static <U, W> AtomicReferenceFieldUpdater<U, W> newUpdater(Class<U> cls, Class<W> cls2, String str) {
        return new AtomicReferenceFieldUpdaterImpl(cls, cls2, str, Reflection.getCallerClass());
    }

    protected AtomicReferenceFieldUpdater() {
    }

    public V getAndSet(T t2, V v2) {
        V v3;
        do {
            v3 = get(t2);
        } while (!compareAndSet(t2, v3, v2));
        return v3;
    }

    public final V getAndUpdate(T t2, UnaryOperator<V> unaryOperator) {
        V v2;
        do {
            v2 = get(t2);
        } while (!compareAndSet(t2, v2, unaryOperator.apply(v2)));
        return v2;
    }

    public final V updateAndGet(T t2, UnaryOperator<V> unaryOperator) {
        V v2;
        V v3;
        do {
            v2 = get(t2);
            v3 = (V) unaryOperator.apply(v2);
        } while (!compareAndSet(t2, v2, v3));
        return v3;
    }

    public final V getAndAccumulate(T t2, V v2, BinaryOperator<V> binaryOperator) {
        V v3;
        do {
            v3 = get(t2);
        } while (!compareAndSet(t2, v3, binaryOperator.apply(v3, v2)));
        return v3;
    }

    public final V accumulateAndGet(T t2, V v2, BinaryOperator<V> binaryOperator) {
        V v3;
        V v4;
        do {
            v3 = get(t2);
            v4 = (V) binaryOperator.apply(v3, v2);
        } while (!compareAndSet(t2, v3, v4));
        return v4;
    }

    /* loaded from: rt.jar:java/util/concurrent/atomic/AtomicReferenceFieldUpdater$AtomicReferenceFieldUpdaterImpl.class */
    private static final class AtomicReferenceFieldUpdaterImpl<T, V> extends AtomicReferenceFieldUpdater<T, V> {

        /* renamed from: U, reason: collision with root package name */
        private static final Unsafe f12593U = Unsafe.getUnsafe();
        private final long offset;
        private final Class<?> cclass;
        private final Class<T> tclass;
        private final Class<V> vclass;

        /* JADX WARN: Multi-variable type inference failed */
        AtomicReferenceFieldUpdaterImpl(final Class<T> cls, Class<V> cls2, final String str, Class<?> cls3) {
            try {
                Field field = (Field) AccessController.doPrivileged(new PrivilegedExceptionAction<Field>() { // from class: java.util.concurrent.atomic.AtomicReferenceFieldUpdater.AtomicReferenceFieldUpdaterImpl.1
                    /* JADX WARN: Can't rename method to resolve collision */
                    @Override // java.security.PrivilegedExceptionAction
                    public Field run() throws NoSuchFieldException {
                        return cls.getDeclaredField(str);
                    }
                });
                int modifiers = field.getModifiers();
                ReflectUtil.ensureMemberAccess(cls3, cls, null, modifiers);
                ClassLoader classLoader = cls.getClassLoader();
                ClassLoader classLoader2 = cls3.getClassLoader();
                if (classLoader2 != null && classLoader2 != classLoader && (classLoader == null || !isAncestor(classLoader, classLoader2))) {
                    ReflectUtil.checkPackageAccess((Class<?>) cls);
                }
                if (cls2 != field.getType()) {
                    throw new ClassCastException();
                }
                if (cls2.isPrimitive()) {
                    throw new IllegalArgumentException("Must be reference type");
                }
                if (!Modifier.isVolatile(modifiers)) {
                    throw new IllegalArgumentException("Must be volatile type");
                }
                this.cclass = (Modifier.isProtected(modifiers) && cls.isAssignableFrom(cls3) && !isSamePackage(cls, cls3)) ? cls3 : cls;
                this.tclass = cls;
                this.vclass = cls2;
                this.offset = f12593U.objectFieldOffset(field);
            } catch (PrivilegedActionException e2) {
                throw new RuntimeException(e2.getException());
            } catch (Exception e3) {
                throw new RuntimeException(e3);
            }
        }

        private static boolean isAncestor(ClassLoader classLoader, ClassLoader classLoader2) {
            ClassLoader parent = classLoader;
            do {
                parent = parent.getParent();
                if (classLoader2 == parent) {
                    return true;
                }
            } while (parent != null);
            return false;
        }

        private static boolean isSamePackage(Class<?> cls, Class<?> cls2) {
            return cls.getClassLoader() == cls2.getClassLoader() && Objects.equals(getPackageName(cls), getPackageName(cls2));
        }

        private static String getPackageName(Class<?> cls) {
            String name = cls.getName();
            int iLastIndexOf = name.lastIndexOf(46);
            return iLastIndexOf != -1 ? name.substring(0, iLastIndexOf) : "";
        }

        private final void accessCheck(T t2) {
            if (!this.cclass.isInstance(t2)) {
                throwAccessCheckException(t2);
            }
        }

        private final void throwAccessCheckException(T t2) {
            if (this.cclass == this.tclass) {
                throw new ClassCastException();
            }
            throw new RuntimeException(new IllegalAccessException("Class " + this.cclass.getName() + " can not access a protected member of class " + this.tclass.getName() + " using an instance of " + t2.getClass().getName()));
        }

        private final void valueCheck(V v2) {
            if (v2 != null && !this.vclass.isInstance(v2)) {
                throwCCE();
            }
        }

        static void throwCCE() {
            throw new ClassCastException();
        }

        @Override // java.util.concurrent.atomic.AtomicReferenceFieldUpdater
        public final boolean compareAndSet(T t2, V v2, V v3) {
            accessCheck(t2);
            valueCheck(v3);
            return f12593U.compareAndSwapObject(t2, this.offset, v2, v3);
        }

        @Override // java.util.concurrent.atomic.AtomicReferenceFieldUpdater
        public final boolean weakCompareAndSet(T t2, V v2, V v3) {
            accessCheck(t2);
            valueCheck(v3);
            return f12593U.compareAndSwapObject(t2, this.offset, v2, v3);
        }

        @Override // java.util.concurrent.atomic.AtomicReferenceFieldUpdater
        public final void set(T t2, V v2) {
            accessCheck(t2);
            valueCheck(v2);
            f12593U.putObjectVolatile(t2, this.offset, v2);
        }

        @Override // java.util.concurrent.atomic.AtomicReferenceFieldUpdater
        public final void lazySet(T t2, V v2) {
            accessCheck(t2);
            valueCheck(v2);
            f12593U.putOrderedObject(t2, this.offset, v2);
        }

        @Override // java.util.concurrent.atomic.AtomicReferenceFieldUpdater
        public final V get(T t2) {
            accessCheck(t2);
            return (V) f12593U.getObjectVolatile(t2, this.offset);
        }

        @Override // java.util.concurrent.atomic.AtomicReferenceFieldUpdater
        public final V getAndSet(T t2, V v2) {
            accessCheck(t2);
            valueCheck(v2);
            return (V) f12593U.getAndSetObject(t2, this.offset, v2);
        }
    }
}
