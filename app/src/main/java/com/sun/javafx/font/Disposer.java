package com.sun.javafx.font;

import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Hashtable;

/* loaded from: jfxrt.jar:com/sun/javafx/font/Disposer.class */
public class Disposer implements Runnable {
    private static final ReferenceQueue queue = new ReferenceQueue();
    private static final Hashtable records = new Hashtable();
    private static Disposer disposerInstance = new Disposer();

    static {
        Thread.currentThread().getThreadGroup();
        AccessController.doPrivileged(new PrivilegedAction() { // from class: com.sun.javafx.font.Disposer.1
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
                        Thread t2 = new Thread(tg, Disposer.disposerInstance, "Prism Font Disposer");
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

    public static WeakReference addRecord(Object target, DisposerRecord rec) {
        WeakReference ref = new WeakReference(target, queue);
        Disposer disposer = disposerInstance;
        records.put(ref, rec);
        return ref;
    }

    @Override // java.lang.Runnable
    public void run() {
        while (true) {
            try {
                Object obj = queue.remove();
                ((Reference) obj).clear();
                DisposerRecord rec = (DisposerRecord) records.remove(obj);
                rec.dispose();
            } catch (Exception e2) {
                System.out.println("Exception while removing reference: " + ((Object) e2));
                e2.printStackTrace();
            }
        }
    }
}
