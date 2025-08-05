package com.sun.prism.impl;

import java.lang.ref.PhantomReference;
import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.util.Hashtable;
import java.util.LinkedList;

/* loaded from: jfxrt.jar:com/sun/prism/impl/Disposer.class */
public class Disposer {
    private static Disposer disposerInstance;
    private static final int WEAK = 0;
    private static final int PHANTOM = 1;
    private static final int SOFT = 2;
    private static int refType;
    private final ReferenceQueue queue = new ReferenceQueue();
    private final Hashtable records = new Hashtable();
    private final LinkedList<Record> disposalQueue = new LinkedList<>();

    /* loaded from: jfxrt.jar:com/sun/prism/impl/Disposer$Record.class */
    public interface Record {
        void dispose();
    }

    /* loaded from: jfxrt.jar:com/sun/prism/impl/Disposer$Target.class */
    public interface Target {
        Object getDisposerReferent();
    }

    static {
        refType = 1;
        String type = PrismSettings.refType;
        if (type != null) {
            if (type.equals("weak")) {
                refType = 0;
                if (PrismSettings.verbose) {
                    System.err.println("Using WEAK refs");
                }
            } else if (type.equals("soft")) {
                refType = 2;
                if (PrismSettings.verbose) {
                    System.err.println("Using SOFT refs");
                }
            } else {
                refType = 1;
                if (PrismSettings.verbose) {
                    System.err.println("Using PHANTOM refs");
                }
            }
        }
        disposerInstance = new Disposer();
    }

    private Disposer() {
    }

    public static void addRecord(Object target, Record rec) {
        disposerInstance.add(target, rec);
    }

    public static void disposeRecord(Record rec) {
        disposerInstance.addToDisposalQueue(rec);
    }

    public static void cleanUp() {
        disposerInstance.disposeUnreachables();
        disposerInstance.processDisposalQueue();
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v5, types: [java.lang.ref.SoftReference] */
    /* JADX WARN: Type inference failed for: r0v9, types: [java.lang.ref.PhantomReference] */
    private synchronized void add(Object target, Record rec) {
        WeakReference weakReference;
        if (target instanceof Target) {
            target = ((Target) target).getDisposerReferent();
        }
        if (refType == 1) {
            weakReference = new PhantomReference(target, this.queue);
        } else if (refType == 2) {
            weakReference = new SoftReference(target, this.queue);
        } else {
            weakReference = new WeakReference(target, this.queue);
        }
        this.records.put(weakReference, rec);
    }

    private synchronized void addToDisposalQueue(Record rec) {
        this.disposalQueue.add(rec);
    }

    private synchronized void disposeUnreachables() {
        while (true) {
            Object obj = this.queue.poll();
            if (obj != null) {
                try {
                    ((Reference) obj).clear();
                    Record rec = (Record) this.records.remove(obj);
                    rec.dispose();
                } catch (Exception e2) {
                    System.out.println("Exception while removing reference: " + ((Object) e2));
                    e2.printStackTrace();
                }
            } else {
                return;
            }
        }
    }

    private synchronized void processDisposalQueue() {
        while (!this.disposalQueue.isEmpty()) {
            this.disposalQueue.remove().dispose();
        }
    }
}
