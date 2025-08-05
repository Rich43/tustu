package java.util.concurrent.atomic;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.security.AccessController;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.util.Objects;
import java.util.function.LongBinaryOperator;
import java.util.function.LongUnaryOperator;
import sun.misc.Unsafe;
import sun.reflect.CallerSensitive;
import sun.reflect.Reflection;
import sun.reflect.misc.ReflectUtil;

/* loaded from: rt.jar:java/util/concurrent/atomic/AtomicLongFieldUpdater.class */
public abstract class AtomicLongFieldUpdater<T> {
    public abstract boolean compareAndSet(T t2, long j2, long j3);

    public abstract boolean weakCompareAndSet(T t2, long j2, long j3);

    public abstract void set(T t2, long j2);

    public abstract void lazySet(T t2, long j2);

    public abstract long get(T t2);

    @CallerSensitive
    public static <U> AtomicLongFieldUpdater<U> newUpdater(Class<U> cls, String str) {
        Class<?> callerClass = Reflection.getCallerClass();
        if (AtomicLong.VM_SUPPORTS_LONG_CAS) {
            return new CASUpdater(cls, str, callerClass);
        }
        return new LockedUpdater(cls, str, callerClass);
    }

    protected AtomicLongFieldUpdater() {
    }

    public long getAndSet(T t2, long j2) {
        long j3;
        do {
            j3 = get(t2);
        } while (!compareAndSet(t2, j3, j2));
        return j3;
    }

    public long getAndIncrement(T t2) {
        long j2;
        do {
            j2 = get(t2);
        } while (!compareAndSet(t2, j2, j2 + 1));
        return j2;
    }

    public long getAndDecrement(T t2) {
        long j2;
        do {
            j2 = get(t2);
        } while (!compareAndSet(t2, j2, j2 - 1));
        return j2;
    }

    public long getAndAdd(T t2, long j2) {
        long j3;
        do {
            j3 = get(t2);
        } while (!compareAndSet(t2, j3, j3 + j2));
        return j3;
    }

    public long incrementAndGet(T t2) {
        long j2;
        long j3;
        do {
            j2 = get(t2);
            j3 = j2 + 1;
        } while (!compareAndSet(t2, j2, j3));
        return j3;
    }

    public long decrementAndGet(T t2) {
        long j2;
        long j3;
        do {
            j2 = get(t2);
            j3 = j2 - 1;
        } while (!compareAndSet(t2, j2, j3));
        return j3;
    }

    public long addAndGet(T t2, long j2) {
        long j3;
        long j4;
        do {
            j3 = get(t2);
            j4 = j3 + j2;
        } while (!compareAndSet(t2, j3, j4));
        return j4;
    }

    public final long getAndUpdate(T t2, LongUnaryOperator longUnaryOperator) {
        long j2;
        do {
            j2 = get(t2);
        } while (!compareAndSet(t2, j2, longUnaryOperator.applyAsLong(j2)));
        return j2;
    }

    public final long updateAndGet(T t2, LongUnaryOperator longUnaryOperator) {
        long j2;
        long jApplyAsLong;
        do {
            j2 = get(t2);
            jApplyAsLong = longUnaryOperator.applyAsLong(j2);
        } while (!compareAndSet(t2, j2, jApplyAsLong));
        return jApplyAsLong;
    }

    public final long getAndAccumulate(T t2, long j2, LongBinaryOperator longBinaryOperator) {
        long j3;
        do {
            j3 = get(t2);
        } while (!compareAndSet(t2, j3, longBinaryOperator.applyAsLong(j3, j2)));
        return j3;
    }

    public final long accumulateAndGet(T t2, long j2, LongBinaryOperator longBinaryOperator) {
        long j3;
        long jApplyAsLong;
        do {
            j3 = get(t2);
            jApplyAsLong = longBinaryOperator.applyAsLong(j3, j2);
        } while (!compareAndSet(t2, j3, jApplyAsLong));
        return jApplyAsLong;
    }

    /* loaded from: rt.jar:java/util/concurrent/atomic/AtomicLongFieldUpdater$CASUpdater.class */
    private static final class CASUpdater<T> extends AtomicLongFieldUpdater<T> {

        /* renamed from: U, reason: collision with root package name */
        private static final Unsafe f12591U = Unsafe.getUnsafe();
        private final long offset;
        private final Class<?> cclass;
        private final Class<T> tclass;

        /* JADX WARN: Multi-variable type inference failed */
        CASUpdater(final Class<T> cls, final String str, Class<?> cls2) {
            try {
                Field field = (Field) AccessController.doPrivileged(new PrivilegedExceptionAction<Field>() { // from class: java.util.concurrent.atomic.AtomicLongFieldUpdater.CASUpdater.1
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
                if (field.getType() != Long.TYPE) {
                    throw new IllegalArgumentException("Must be long type");
                }
                if (!Modifier.isVolatile(modifiers)) {
                    throw new IllegalArgumentException("Must be volatile type");
                }
                this.cclass = (Modifier.isProtected(modifiers) && cls.isAssignableFrom(cls2) && !AtomicLongFieldUpdater.isSamePackage(cls, cls2)) ? cls2 : cls;
                this.tclass = cls;
                this.offset = f12591U.objectFieldOffset(field);
            } catch (PrivilegedActionException e2) {
                throw new RuntimeException(e2.getException());
            } catch (Exception e3) {
                throw new RuntimeException(e3);
            }
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

        @Override // java.util.concurrent.atomic.AtomicLongFieldUpdater
        public final boolean compareAndSet(T t2, long j2, long j3) {
            accessCheck(t2);
            return f12591U.compareAndSwapLong(t2, this.offset, j2, j3);
        }

        @Override // java.util.concurrent.atomic.AtomicLongFieldUpdater
        public final boolean weakCompareAndSet(T t2, long j2, long j3) {
            accessCheck(t2);
            return f12591U.compareAndSwapLong(t2, this.offset, j2, j3);
        }

        @Override // java.util.concurrent.atomic.AtomicLongFieldUpdater
        public final void set(T t2, long j2) {
            accessCheck(t2);
            f12591U.putLongVolatile(t2, this.offset, j2);
        }

        @Override // java.util.concurrent.atomic.AtomicLongFieldUpdater
        public final void lazySet(T t2, long j2) {
            accessCheck(t2);
            f12591U.putOrderedLong(t2, this.offset, j2);
        }

        @Override // java.util.concurrent.atomic.AtomicLongFieldUpdater
        public final long get(T t2) {
            accessCheck(t2);
            return f12591U.getLongVolatile(t2, this.offset);
        }

        @Override // java.util.concurrent.atomic.AtomicLongFieldUpdater
        public final long getAndSet(T t2, long j2) {
            accessCheck(t2);
            return f12591U.getAndSetLong(t2, this.offset, j2);
        }

        @Override // java.util.concurrent.atomic.AtomicLongFieldUpdater
        public final long getAndAdd(T t2, long j2) {
            accessCheck(t2);
            return f12591U.getAndAddLong(t2, this.offset, j2);
        }

        @Override // java.util.concurrent.atomic.AtomicLongFieldUpdater
        public final long getAndIncrement(T t2) {
            return getAndAdd(t2, 1L);
        }

        @Override // java.util.concurrent.atomic.AtomicLongFieldUpdater
        public final long getAndDecrement(T t2) {
            return getAndAdd(t2, -1L);
        }

        @Override // java.util.concurrent.atomic.AtomicLongFieldUpdater
        public final long incrementAndGet(T t2) {
            return getAndAdd(t2, 1L) + 1;
        }

        @Override // java.util.concurrent.atomic.AtomicLongFieldUpdater
        public final long decrementAndGet(T t2) {
            return getAndAdd(t2, -1L) - 1;
        }

        @Override // java.util.concurrent.atomic.AtomicLongFieldUpdater
        public final long addAndGet(T t2, long j2) {
            return getAndAdd(t2, j2) + j2;
        }
    }

    /* loaded from: rt.jar:java/util/concurrent/atomic/AtomicLongFieldUpdater$LockedUpdater.class */
    private static final class LockedUpdater<T> extends AtomicLongFieldUpdater<T> {

        /* renamed from: U, reason: collision with root package name */
        private static final Unsafe f12592U = Unsafe.getUnsafe();
        private final long offset;
        private final Class<?> cclass;
        private final Class<T> tclass;

        /* JADX WARN: Multi-variable type inference failed */
        LockedUpdater(final Class<T> cls, final String str, Class<?> cls2) {
            try {
                Field field = (Field) AccessController.doPrivileged(new PrivilegedExceptionAction<Field>() { // from class: java.util.concurrent.atomic.AtomicLongFieldUpdater.LockedUpdater.1
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
                if (field.getType() != Long.TYPE) {
                    throw new IllegalArgumentException("Must be long type");
                }
                if (!Modifier.isVolatile(modifiers)) {
                    throw new IllegalArgumentException("Must be volatile type");
                }
                this.cclass = (Modifier.isProtected(modifiers) && cls.isAssignableFrom(cls2) && !AtomicLongFieldUpdater.isSamePackage(cls, cls2)) ? cls2 : cls;
                this.tclass = cls;
                this.offset = f12592U.objectFieldOffset(field);
            } catch (PrivilegedActionException e2) {
                throw new RuntimeException(e2.getException());
            } catch (Exception e3) {
                throw new RuntimeException(e3);
            }
        }

        private final void accessCheck(T t2) {
            if (!this.cclass.isInstance(t2)) {
                throw accessCheckException(t2);
            }
        }

        private final RuntimeException accessCheckException(T t2) {
            if (this.cclass == this.tclass) {
                return new ClassCastException();
            }
            return new RuntimeException(new IllegalAccessException("Class " + this.cclass.getName() + " can not access a protected member of class " + this.tclass.getName() + " using an instance of " + t2.getClass().getName()));
        }

        @Override // java.util.concurrent.atomic.AtomicLongFieldUpdater
        public final boolean compareAndSet(T t2, long j2, long j3) {
            accessCheck(t2);
            synchronized (this) {
                if (f12592U.getLong(t2, this.offset) != j2) {
                    return false;
                }
                f12592U.putLong(t2, this.offset, j3);
                return true;
            }
        }

        @Override // java.util.concurrent.atomic.AtomicLongFieldUpdater
        public final boolean weakCompareAndSet(T t2, long j2, long j3) {
            return compareAndSet(t2, j2, j3);
        }

        @Override // java.util.concurrent.atomic.AtomicLongFieldUpdater
        public final void set(T t2, long j2) {
            accessCheck(t2);
            synchronized (this) {
                f12592U.putLong(t2, this.offset, j2);
            }
        }

        @Override // java.util.concurrent.atomic.AtomicLongFieldUpdater
        public final void lazySet(T t2, long j2) {
            set(t2, j2);
        }

        @Override // java.util.concurrent.atomic.AtomicLongFieldUpdater
        public final long get(T t2) {
            long j2;
            accessCheck(t2);
            synchronized (this) {
                j2 = f12592U.getLong(t2, this.offset);
            }
            return j2;
        }
    }

    static boolean isAncestor(ClassLoader classLoader, ClassLoader classLoader2) {
        ClassLoader parent = classLoader;
        do {
            parent = parent.getParent();
            if (classLoader2 == parent) {
                return true;
            }
        } while (parent != null);
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean isSamePackage(Class<?> cls, Class<?> cls2) {
        return cls.getClassLoader() == cls2.getClassLoader() && Objects.equals(getPackageName(cls), getPackageName(cls2));
    }

    private static String getPackageName(Class<?> cls) {
        String name = cls.getName();
        int iLastIndexOf = name.lastIndexOf(46);
        return iLastIndexOf != -1 ? name.substring(0, iLastIndexOf) : "";
    }
}
