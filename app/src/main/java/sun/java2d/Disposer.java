package sun.java2d;

import java.lang.ref.PhantomReference;
import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.ArrayList;
import java.util.Hashtable;
import sun.misc.ThreadGroupUtils;
import sun.security.action.GetPropertyAction;

/* loaded from: rt.jar:sun/java2d/Disposer.class */
public class Disposer implements Runnable {
    private static final ReferenceQueue queue = new ReferenceQueue();
    private static final Hashtable records = new Hashtable();
    private static Disposer disposerInstance;
    public static final int WEAK = 0;
    public static final int PHANTOM = 1;
    public static int refType;
    private static ArrayList<DisposerRecord> deferredRecords;
    public static volatile boolean pollingQueue;

    /* loaded from: rt.jar:sun/java2d/Disposer$PollDisposable.class */
    public interface PollDisposable {
    }

    private static native void initIDs();

    static {
        refType = 1;
        AccessController.doPrivileged(new PrivilegedAction<Void>() { // from class: sun.java2d.Disposer.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.security.PrivilegedAction
            /* renamed from: run */
            public Void run2() {
                System.loadLibrary("awt");
                return null;
            }
        });
        initIDs();
        String str = (String) AccessController.doPrivileged(new GetPropertyAction("sun.java2d.reftype"));
        if (str != null) {
            if (str.equals("weak")) {
                refType = 0;
                System.err.println("Using WEAK refs");
            } else {
                refType = 1;
                System.err.println("Using PHANTOM refs");
            }
        }
        disposerInstance = new Disposer();
        AccessController.doPrivileged(() -> {
            Thread thread = new Thread(ThreadGroupUtils.getRootThreadGroup(), disposerInstance, "Java2D Disposer");
            thread.setContextClassLoader(null);
            thread.setDaemon(true);
            thread.setPriority(10);
            thread.start();
            return null;
        });
        deferredRecords = null;
        pollingQueue = false;
    }

    public static void addRecord(Object obj, long j2, long j3) {
        disposerInstance.add(obj, new DefaultDisposerRecord(j2, j3));
    }

    public static void addRecord(Object obj, DisposerRecord disposerRecord) {
        disposerInstance.add(obj, disposerRecord);
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v6, types: [java.lang.ref.PhantomReference] */
    synchronized void add(Object obj, DisposerRecord disposerRecord) {
        WeakReference weakReference;
        if (obj instanceof DisposerTarget) {
            obj = ((DisposerTarget) obj).getDisposerReferent();
        }
        if (refType == 1) {
            weakReference = new PhantomReference(obj, queue);
        } else {
            weakReference = new WeakReference(obj, queue);
        }
        records.put(weakReference, disposerRecord);
    }

    @Override // java.lang.Runnable
    public void run() {
        while (true) {
            try {
                Reference referenceRemove = queue.remove();
                referenceRemove.clear();
                ((DisposerRecord) records.remove(referenceRemove)).dispose();
                clearDeferredRecords();
            } catch (Exception e2) {
                System.out.println("Exception while removing reference.");
            }
        }
    }

    private static void clearDeferredRecords() {
        if (deferredRecords == null || deferredRecords.isEmpty()) {
            return;
        }
        for (int i2 = 0; i2 < deferredRecords.size(); i2++) {
            try {
                deferredRecords.get(i2).dispose();
            } catch (Exception e2) {
                System.out.println("Exception while disposing deferred rec.");
            }
        }
        deferredRecords.clear();
    }

    /* JADX WARN: Finally extract failed */
    public static void pollRemove() {
        if (pollingQueue) {
            return;
        }
        pollingQueue = true;
        int i2 = 0;
        int i3 = 0;
        while (true) {
            try {
                try {
                    Reference referencePoll = queue.poll();
                    if (referencePoll == null || i2 >= 10000 || i3 >= 100) {
                        break;
                    }
                    i2++;
                    referencePoll.clear();
                    DisposerRecord disposerRecord = (DisposerRecord) records.remove(referencePoll);
                    if (disposerRecord instanceof PollDisposable) {
                        disposerRecord.dispose();
                    } else if (disposerRecord != null) {
                        i3++;
                        if (deferredRecords == null) {
                            deferredRecords = new ArrayList<>(5);
                        }
                        deferredRecords.add(disposerRecord);
                    }
                } catch (Exception e2) {
                    System.out.println("Exception while removing reference.");
                    pollingQueue = false;
                    return;
                }
            } catch (Throwable th) {
                pollingQueue = false;
                throw th;
            }
        }
        pollingQueue = false;
    }

    public static void addReference(Reference reference, DisposerRecord disposerRecord) {
        records.put(reference, disposerRecord);
    }

    public static void addObjectRecord(Object obj, DisposerRecord disposerRecord) {
        records.put(new WeakReference(obj, queue), disposerRecord);
    }

    public static ReferenceQueue getQueue() {
        return queue;
    }
}
