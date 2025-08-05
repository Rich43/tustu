package com.sun.xml.internal.bind.v2.runtime.reflect.opt;

import com.sun.xml.internal.bind.Util;
import com.sun.xml.internal.bind.v2.runtime.reflect.Accessor;
import java.lang.ref.WeakReference;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.HashMap;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.logging.Level;
import java.util.logging.Logger;

/* loaded from: rt.jar:com/sun/xml/internal/bind/v2/runtime/reflect/opt/Injector.class */
final class Injector {
    private static final ReentrantReadWriteLock irwl;
    private static final Lock ir;
    private static final Lock iw;
    private static final Map<ClassLoader, WeakReference<Injector>> injectors;
    private static final Logger logger;
    private final Map<String, Class> classes = new HashMap();
    private final ReentrantReadWriteLock rwl = new ReentrantReadWriteLock();

    /* renamed from: r, reason: collision with root package name */
    private final Lock f12070r = this.rwl.readLock();

    /* renamed from: w, reason: collision with root package name */
    private final Lock f12071w = this.rwl.writeLock();
    private final ClassLoader parent;
    private final boolean loadable;
    private static final Method defineClass;
    private static final Method resolveClass;
    private static final Method findLoadedClass;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !Injector.class.desiredAssertionStatus();
        irwl = new ReentrantReadWriteLock();
        ir = irwl.readLock();
        iw = irwl.writeLock();
        injectors = new WeakHashMap();
        logger = Util.getClassLogger();
        try {
            defineClass = ClassLoader.class.getDeclaredMethod("defineClass", String.class, byte[].class, Integer.TYPE, Integer.TYPE);
            resolveClass = ClassLoader.class.getDeclaredMethod("resolveClass", Class.class);
            findLoadedClass = ClassLoader.class.getDeclaredMethod("findLoadedClass", String.class);
            AccessController.doPrivileged(new PrivilegedAction<Void>() { // from class: com.sun.xml.internal.bind.v2.runtime.reflect.opt.Injector.1
                /* JADX WARN: Can't rename method to resolve collision */
                @Override // java.security.PrivilegedAction
                /* renamed from: run */
                public Void run2() {
                    Injector.defineClass.setAccessible(true);
                    Injector.resolveClass.setAccessible(true);
                    Injector.findLoadedClass.setAccessible(true);
                    return null;
                }
            });
        } catch (NoSuchMethodException e2) {
            throw new NoSuchMethodError(e2.getMessage());
        }
    }

    static Class inject(ClassLoader cl, String className, byte[] image) {
        Injector injector = get(cl);
        if (injector != null) {
            return injector.inject(className, image);
        }
        return null;
    }

    static Class find(ClassLoader cl, String className) {
        Injector injector = get(cl);
        if (injector != null) {
            return injector.find(className);
        }
        return null;
    }

    /* JADX WARN: Finally extract failed */
    private static Injector get(ClassLoader cl) {
        Injector injector = null;
        ir.lock();
        try {
            WeakReference<Injector> wr = injectors.get(cl);
            ir.unlock();
            if (wr != null) {
                injector = wr.get();
            }
            if (injector == null) {
                try {
                    Injector injector2 = new Injector(cl);
                    injector = injector2;
                    WeakReference<Injector> wr2 = new WeakReference<>(injector2);
                    iw.lock();
                    try {
                        if (!injectors.containsKey(cl)) {
                            injectors.put(cl, wr2);
                        }
                        iw.unlock();
                    } catch (Throwable th) {
                        iw.unlock();
                        throw th;
                    }
                } catch (SecurityException e2) {
                    logger.log(Level.FINE, "Unable to set up a back-door for the injector", (Throwable) e2);
                    return null;
                }
            }
            return injector;
        } catch (Throwable th2) {
            ir.unlock();
            throw th2;
        }
    }

    private Injector(ClassLoader parent) {
        this.parent = parent;
        if (!$assertionsDisabled && parent == null) {
            throw new AssertionError();
        }
        boolean loadableCheck = false;
        try {
            loadableCheck = parent.loadClass(Accessor.class.getName()) == Accessor.class;
        } catch (ClassNotFoundException e2) {
        }
        this.loadable = loadableCheck;
    }

    private Class inject(String className, byte[] image) {
        if (!this.loadable) {
            return null;
        }
        boolean wlocked = false;
        boolean rlocked = false;
        try {
            this.f12070r.lock();
            Class c2 = this.classes.get(className);
            this.f12070r.unlock();
            rlocked = false;
            if (c2 == null) {
                try {
                    try {
                        try {
                            c2 = (Class) findLoadedClass.invoke(this.parent, className.replace('/', '.'));
                        } catch (IllegalAccessException e2) {
                            logger.log(Level.FINE, "Unable to find " + className, (Throwable) e2);
                        }
                    } catch (IllegalArgumentException e3) {
                        logger.log(Level.FINE, "Unable to find " + className, (Throwable) e3);
                    }
                } catch (InvocationTargetException e4) {
                    logger.log(Level.FINE, "Unable to find " + className, e4.getTargetException());
                }
                if (c2 != null) {
                    this.f12071w.lock();
                    this.classes.put(className, c2);
                    this.f12071w.unlock();
                    Class cls = c2;
                    if (0 != 0) {
                        this.f12070r.unlock();
                    }
                    if (0 != 0) {
                        this.f12071w.unlock();
                    }
                    return cls;
                }
            }
            if (c2 == null) {
                this.f12070r.lock();
                c2 = this.classes.get(className);
                this.f12070r.unlock();
                rlocked = false;
                if (c2 == null) {
                    try {
                        try {
                            try {
                                try {
                                    c2 = (Class) defineClass.invoke(this.parent, className.replace('/', '.'), image, 0, Integer.valueOf(image.length));
                                    resolveClass.invoke(this.parent, c2);
                                    this.f12071w.lock();
                                    if (!this.classes.containsKey(className)) {
                                        this.classes.put(className, c2);
                                    }
                                    this.f12071w.unlock();
                                    wlocked = false;
                                } catch (SecurityException e5) {
                                    logger.log(Level.FINE, "Unable to inject " + className, (Throwable) e5);
                                    if (0 != 0) {
                                        this.f12070r.unlock();
                                    }
                                    if (0 != 0) {
                                        this.f12071w.unlock();
                                    }
                                    return null;
                                }
                            } catch (IllegalAccessException e6) {
                                logger.log(Level.FINE, "Unable to inject " + className, (Throwable) e6);
                                if (0 != 0) {
                                    this.f12070r.unlock();
                                }
                                if (0 != 0) {
                                    this.f12071w.unlock();
                                }
                                return null;
                            }
                        } catch (InvocationTargetException e7) {
                            Throwable t2 = e7.getTargetException();
                            if (t2 instanceof LinkageError) {
                                logger.log(Level.FINE, "duplicate class definition bug occured? Please report this : " + className, t2);
                            } else {
                                logger.log(Level.FINE, "Unable to inject " + className, t2);
                            }
                            if (0 != 0) {
                                this.f12070r.unlock();
                            }
                            if (0 != 0) {
                                this.f12071w.unlock();
                            }
                            return null;
                        }
                    } catch (LinkageError e8) {
                        logger.log(Level.FINE, "Unable to inject " + className, (Throwable) e8);
                        if (0 != 0) {
                            this.f12070r.unlock();
                        }
                        if (0 != 0) {
                            this.f12071w.unlock();
                        }
                        return null;
                    }
                }
            }
            Class cls2 = c2;
            if (rlocked) {
                this.f12070r.unlock();
            }
            if (wlocked) {
                this.f12071w.unlock();
            }
            return cls2;
        } catch (Throwable th) {
            if (rlocked) {
                this.f12070r.unlock();
            }
            if (0 != 0) {
                this.f12071w.unlock();
            }
            throw th;
        }
    }

    private Class find(String className) {
        this.f12070r.lock();
        try {
            return this.classes.get(className);
        } finally {
            this.f12070r.unlock();
        }
    }
}
