package java.util.concurrent.atomic;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.security.AccessController;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.util.Objects;
import java.util.function.IntBinaryOperator;
import java.util.function.IntUnaryOperator;
import sun.misc.Unsafe;
import sun.reflect.CallerSensitive;
import sun.reflect.Reflection;
import sun.reflect.misc.ReflectUtil;

/* loaded from: rt.jar:java/util/concurrent/atomic/AtomicIntegerFieldUpdater.class */
public abstract class AtomicIntegerFieldUpdater<T> {
    public abstract boolean compareAndSet(T t2, int i2, int i3);

    public abstract boolean weakCompareAndSet(T t2, int i2, int i3);

    public abstract void set(T t2, int i2);

    public abstract void lazySet(T t2, int i2);

    public abstract int get(T t2);

    @CallerSensitive
    public static <U> AtomicIntegerFieldUpdater<U> newUpdater(Class<U> cls, String str) {
        return new AtomicIntegerFieldUpdaterImpl(cls, str, Reflection.getCallerClass());
    }

    protected AtomicIntegerFieldUpdater() {
    }

    public int getAndSet(T t2, int i2) {
        int i3;
        do {
            i3 = get(t2);
        } while (!compareAndSet(t2, i3, i2));
        return i3;
    }

    public int getAndIncrement(T t2) {
        int i2;
        do {
            i2 = get(t2);
        } while (!compareAndSet(t2, i2, i2 + 1));
        return i2;
    }

    public int getAndDecrement(T t2) {
        int i2;
        do {
            i2 = get(t2);
        } while (!compareAndSet(t2, i2, i2 - 1));
        return i2;
    }

    public int getAndAdd(T t2, int i2) {
        int i3;
        do {
            i3 = get(t2);
        } while (!compareAndSet(t2, i3, i3 + i2));
        return i3;
    }

    public int incrementAndGet(T t2) {
        int i2;
        int i3;
        do {
            i2 = get(t2);
            i3 = i2 + 1;
        } while (!compareAndSet(t2, i2, i3));
        return i3;
    }

    public int decrementAndGet(T t2) {
        int i2;
        int i3;
        do {
            i2 = get(t2);
            i3 = i2 - 1;
        } while (!compareAndSet(t2, i2, i3));
        return i3;
    }

    public int addAndGet(T t2, int i2) {
        int i3;
        int i4;
        do {
            i3 = get(t2);
            i4 = i3 + i2;
        } while (!compareAndSet(t2, i3, i4));
        return i4;
    }

    public final int getAndUpdate(T t2, IntUnaryOperator intUnaryOperator) {
        int i2;
        do {
            i2 = get(t2);
        } while (!compareAndSet(t2, i2, intUnaryOperator.applyAsInt(i2)));
        return i2;
    }

    public final int updateAndGet(T t2, IntUnaryOperator intUnaryOperator) {
        int i2;
        int iApplyAsInt;
        do {
            i2 = get(t2);
            iApplyAsInt = intUnaryOperator.applyAsInt(i2);
        } while (!compareAndSet(t2, i2, iApplyAsInt));
        return iApplyAsInt;
    }

    public final int getAndAccumulate(T t2, int i2, IntBinaryOperator intBinaryOperator) {
        int i3;
        do {
            i3 = get(t2);
        } while (!compareAndSet(t2, i3, intBinaryOperator.applyAsInt(i3, i2)));
        return i3;
    }

    public final int accumulateAndGet(T t2, int i2, IntBinaryOperator intBinaryOperator) {
        int i3;
        int iApplyAsInt;
        do {
            i3 = get(t2);
            iApplyAsInt = intBinaryOperator.applyAsInt(i3, i2);
        } while (!compareAndSet(t2, i3, iApplyAsInt));
        return iApplyAsInt;
    }

    /* loaded from: rt.jar:java/util/concurrent/atomic/AtomicIntegerFieldUpdater$AtomicIntegerFieldUpdaterImpl.class */
    private static final class AtomicIntegerFieldUpdaterImpl<T> extends AtomicIntegerFieldUpdater<T> {

        /* renamed from: U, reason: collision with root package name */
        private static final Unsafe f12590U = Unsafe.getUnsafe();
        private final long offset;
        private final Class<?> cclass;
        private final Class<T> tclass;

        /* JADX WARN: Multi-variable type inference failed */
        AtomicIntegerFieldUpdaterImpl(final Class<T> cls, final String str, Class<?> cls2) {
            try {
                Field field = (Field) AccessController.doPrivileged(new PrivilegedExceptionAction<Field>() { // from class: java.util.concurrent.atomic.AtomicIntegerFieldUpdater.AtomicIntegerFieldUpdaterImpl.1
                    /* JADX WARN: Can't rename method to resolve collision */
                    @Override // java.security.PrivilegedExceptionAction
                    public Field run() throws NoSuchFieldException {
                        return cls.getDeclaredField(str);
                    }
                });
                int modifiers = field.getModifiers();
                ReflectUtil.ensureMemberAccess(cls2, cls, null, modifiers);
                ClassLoader classLoader = cls.getClassLoader();
                ClassLoader classLoader2 = cls2.getClassLoader();
                if (classLoader2 != null && classLoader2 != classLoader && (classLoader == null || !isAncestor(classLoader, classLoader2))) {
                    ReflectUtil.checkPackageAccess((Class<?>) cls);
                }
                if (field.getType() != Integer.TYPE) {
                    throw new IllegalArgumentException("Must be integer type");
                }
                if (!Modifier.isVolatile(modifiers)) {
                    throw new IllegalArgumentException("Must be volatile type");
                }
                this.cclass = (Modifier.isProtected(modifiers) && cls.isAssignableFrom(cls2) && !isSamePackage(cls, cls2)) ? cls2 : cls;
                this.tclass = cls;
                this.offset = f12590U.objectFieldOffset(field);
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

        @Override // java.util.concurrent.atomic.AtomicIntegerFieldUpdater
        public final boolean compareAndSet(T t2, int i2, int i3) {
            accessCheck(t2);
            return f12590U.compareAndSwapInt(t2, this.offset, i2, i3);
        }

        @Override // java.util.concurrent.atomic.AtomicIntegerFieldUpdater
        public final boolean weakCompareAndSet(T t2, int i2, int i3) {
            accessCheck(t2);
            return f12590U.compareAndSwapInt(t2, this.offset, i2, i3);
        }

        @Override // java.util.concurrent.atomic.AtomicIntegerFieldUpdater
        public final void set(T t2, int i2) {
            accessCheck(t2);
            f12590U.putIntVolatile(t2, this.offset, i2);
        }

        @Override // java.util.concurrent.atomic.AtomicIntegerFieldUpdater
        public final void lazySet(T t2, int i2) {
            accessCheck(t2);
            f12590U.putOrderedInt(t2, this.offset, i2);
        }

        @Override // java.util.concurrent.atomic.AtomicIntegerFieldUpdater
        public final int get(T t2) {
            accessCheck(t2);
            return f12590U.getIntVolatile(t2, this.offset);
        }

        @Override // java.util.concurrent.atomic.AtomicIntegerFieldUpdater
        public final int getAndSet(T t2, int i2) {
            accessCheck(t2);
            return f12590U.getAndSetInt(t2, this.offset, i2);
        }

        @Override // java.util.concurrent.atomic.AtomicIntegerFieldUpdater
        public final int getAndAdd(T t2, int i2) {
            accessCheck(t2);
            return f12590U.getAndAddInt(t2, this.offset, i2);
        }

        @Override // java.util.concurrent.atomic.AtomicIntegerFieldUpdater
        public final int getAndIncrement(T t2) {
            return getAndAdd(t2, 1);
        }

        @Override // java.util.concurrent.atomic.AtomicIntegerFieldUpdater
        public final int getAndDecrement(T t2) {
            return getAndAdd(t2, -1);
        }

        @Override // java.util.concurrent.atomic.AtomicIntegerFieldUpdater
        public final int incrementAndGet(T t2) {
            return getAndAdd(t2, 1) + 1;
        }

        @Override // java.util.concurrent.atomic.AtomicIntegerFieldUpdater
        public final int decrementAndGet(T t2) {
            return getAndAdd(t2, -1) - 1;
        }

        @Override // java.util.concurrent.atomic.AtomicIntegerFieldUpdater
        public final int addAndGet(T t2, int i2) {
            return getAndAdd(t2, i2) + i2;
        }
    }
}
