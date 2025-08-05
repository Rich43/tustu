package com.sun.javafx.property.adapter;

import java.lang.ref.PhantomReference;
import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/* loaded from: jfxrt.jar:com/sun/javafx/property/adapter/Disposer.class */
public class Disposer implements Runnable {
    private static final ReferenceQueue queue = new ReferenceQueue();
    private static final Map<Object, Runnable> records = new ConcurrentHashMap();
    private static Disposer disposerInstance = new Disposer();

    static {
        AccessController.doPrivileged(new PrivilegedAction() { // from class: com.sun.javafx.property.adapter.Disposer.1
            @Override // java.security.PrivilegedAction
            /* renamed from: run */
            public Object run2() {
                ThreadGroup tg = Thread.currentThread().getThreadGroup();
                ThreadGroup parent = tg;
                while (true) {
                    ThreadGroup tgn = parent;
                    if (tgn != null) {
                        tg = tgn;
                        parent = tg.getParent();
                    } else {
                        Thread t2 = new Thread(tg, Disposer.disposerInstance, "Property Disposer");
                        t2.setContextClassLoader(null);
                        t2.setDaemon(true);
                        t2.setPriority(10);
                        t2.start();
                        return null;
                    }
                }
            }
        });
    }

    public static void addRecord(Object target, Runnable rec) {
        PhantomReference ref = new PhantomReference(target, queue);
        records.put(ref, rec);
    }

    @Override // java.lang.Runnable
    public void run() {
        while (true) {
            try {
                Object obj = queue.remove();
                ((Reference) obj).clear();
                Runnable rec = records.remove(obj);
                rec.run();
            } catch (Exception e2) {
                System.out.println("Exception while removing reference: " + ((Object) e2));
                e2.printStackTrace();
            }
        }
    }
}
