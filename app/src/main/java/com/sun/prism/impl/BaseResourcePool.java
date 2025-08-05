package com.sun.prism.impl;

import java.lang.ref.WeakReference;
import java.util.Iterator;

/* loaded from: jfxrt.jar:com/sun/prism/impl/BaseResourcePool.class */
public abstract class BaseResourcePool<T> implements ResourcePool<T> {
    private static final int FOREVER = 1024;
    private static final int RECENTLY_USEFUL = 100;
    private static final int RECENT = 10;
    private static final Predicate[] stageTesters = new Predicate[6];
    private static final String[] stageReasons = new String[6];
    long managedSize;
    final long origTarget;
    long curTarget;
    final long maxSize;
    final ResourcePool<T> sharedParent;
    private final Thread managerThread;
    private WeakLinkedList<T> resourceHead;

    /* loaded from: jfxrt.jar:com/sun/prism/impl/BaseResourcePool$Predicate.class */
    interface Predicate {
        boolean test(ManagedResource<?> managedResource);
    }

    static {
        stageTesters[0] = mr -> {
            return !mr.isInteresting() && mr.getAge() > 1024;
        };
        stageReasons[0] = "Pruning unuseful older than 1024";
        stageTesters[1] = mr2 -> {
            return !mr2.isInteresting() && mr2.getAge() > 512;
        };
        stageReasons[1] = "Pruning unuseful older than 512";
        stageTesters[2] = mr3 -> {
            return !mr3.isInteresting() && mr3.getAge() > 10;
        };
        stageReasons[2] = "Pruning unuseful older than 10";
        stageTesters[3] = mr4 -> {
            return mr4.getAge() > 1024;
        };
        stageReasons[3] = "Pruning all older than 1024";
        stageTesters[4] = mr5 -> {
            return mr5.getAge() > 512;
        };
        stageReasons[4] = "Pruning all older than 512";
        stageTesters[5] = mr6 -> {
            return mr6.getAge() > 100;
        };
        stageReasons[5] = "Pruning all older than 100";
    }

    protected BaseResourcePool(long target, long max) {
        this(null, target, max);
    }

    protected BaseResourcePool(ResourcePool<T> parent) {
        this(parent, parent.target(), parent.max());
    }

    protected BaseResourcePool(ResourcePool<T> parent, long target, long max) {
        this.resourceHead = new WeakLinkedList<>();
        this.sharedParent = parent;
        this.curTarget = target;
        this.origTarget = target;
        this.maxSize = parent == null ? max : Math.min(parent.max(), max);
        this.managerThread = Thread.currentThread();
    }

    public boolean cleanup(long needed) {
        if (used() + needed <= target()) {
            return true;
        }
        long wasused = used();
        long wanted = target() / 16;
        if (wanted < needed) {
            wanted = needed;
        }
        if (PrismSettings.poolDebug) {
            System.err.printf("Need %,d (hoping for %,d) from pool: %s\n", Long.valueOf(needed), Long.valueOf(wanted), this);
            printSummary(false);
        }
        try {
            Disposer.cleanUp();
            if (PrismSettings.poolDebug) {
                System.err.println("Pruning obsolete in pool: " + ((Object) this));
            }
            cleanup(mr -> {
                return false;
            });
            if (used() + wanted <= target()) {
                if (PrismSettings.poolDebug) {
                    System.err.printf("cleaned up %,d from pool: %s\n", Long.valueOf(wasused - used()), this);
                    printSummary(false);
                    System.err.println();
                }
                return true;
            }
            for (int stage = 0; stage < stageTesters.length; stage++) {
                if (PrismSettings.poolDebug) {
                    System.err.println(stageReasons[stage] + " in pool: " + ((Object) this));
                }
                cleanup(stageTesters[stage]);
                if (used() + wanted <= target()) {
                    if (PrismSettings.poolDebug) {
                        System.err.printf("cleaned up %,d from pool: %s\n", Long.valueOf(wasused - used()), this);
                        printSummary(false);
                        System.err.println();
                    }
                    return true;
                }
            }
            long rem = max() - used();
            if (wanted > rem) {
                wanted = needed;
            }
            if (wanted <= rem) {
                long grow = (max() - origTarget()) / 32;
                if (grow < wanted) {
                    grow = wanted;
                } else if (grow > rem) {
                    grow = rem;
                }
                setTarget(used() + grow);
                if (PrismSettings.poolDebug || PrismSettings.verbose) {
                    System.err.printf("Growing pool %s target to %,d\n", this, Long.valueOf(target()));
                }
                if (PrismSettings.poolDebug) {
                    System.err.printf("cleaned up %,d from pool: %s\n", Long.valueOf(wasused - used()), this);
                    printSummary(false);
                    System.err.println();
                }
                return true;
            }
            int i2 = 0;
            while (i2 < 2) {
                pruneLastChance(i2 > 0);
                if (used() + needed <= max()) {
                    if (used() + needed > target()) {
                        setTarget(used() + needed);
                        if (PrismSettings.poolDebug || PrismSettings.verbose) {
                            System.err.printf("Growing pool %s target to %,d\n", this, Long.valueOf(target()));
                        }
                    }
                    if (PrismSettings.poolDebug) {
                        System.err.printf("cleaned up %,d from pool: %s\n", Long.valueOf(wasused - used()), this);
                        printSummary(false);
                        System.err.println();
                    }
                    return true;
                }
                i2++;
            }
            if (PrismSettings.poolDebug) {
                System.err.printf("cleaned up %,d from pool: %s\n", Long.valueOf(wasused - used()), this);
                printSummary(false);
                System.err.println();
            }
            return false;
        } catch (Throwable th) {
            if (PrismSettings.poolDebug) {
                System.err.printf("cleaned up %,d from pool: %s\n", Long.valueOf(wasused - used()), this);
                printSummary(false);
                System.err.println();
            }
            throw th;
        }
    }

    private void pruneLastChance(boolean desperate) {
        System.gc();
        if (desperate) {
            try {
                Thread.sleep(20L);
            } catch (InterruptedException e2) {
            }
        }
        Disposer.cleanUp();
        if (PrismSettings.poolDebug) {
            if (desperate) {
                System.err.print("Last chance pruning");
            } else {
                System.err.print("Pruning everything");
            }
            System.err.println(" in pool: " + ((Object) this));
        }
        cleanup(mr -> {
            return true;
        });
    }

    private void cleanup(Predicate predicate) {
        WeakLinkedList<T> prev = this.resourceHead;
        WeakLinkedList<T> cur = prev.next;
        while (cur != null) {
            ManagedResource<?> resource = cur.getResource();
            if (ManagedResource._isgone(resource)) {
                if (PrismSettings.poolDebug) {
                    showLink("unlinking", cur, false);
                }
                recordFree(cur.size);
                cur = cur.next;
                prev.next = cur;
            } else if (!resource.isPermanent() && !resource.isLocked() && predicate.test(resource)) {
                if (PrismSettings.poolDebug) {
                    showLink("pruning", cur, true);
                }
                resource.free();
                resource.resource = null;
                recordFree(cur.size);
                cur = cur.next;
                prev.next = cur;
            } else {
                prev = cur;
                cur = cur.next;
            }
        }
    }

    static void showLink(String label, WeakLinkedList<?> cur, boolean showAge) {
        ManagedResource<?> mr = cur.getResource();
        System.err.printf("%s: %s (size=%,d)", label, mr, Long.valueOf(cur.size));
        if (mr != null) {
            if (showAge) {
                System.err.printf(" (age=%d)", Integer.valueOf(mr.getAge()));
            }
            if (mr.isPermanent()) {
                System.err.print(" perm");
            }
            if (mr.isLocked()) {
                System.err.print(" lock");
            }
            if (mr.isInteresting()) {
                System.err.print(" int");
            }
        }
        System.err.println();
    }

    @Override // com.sun.prism.impl.ResourcePool
    public void freeDisposalRequestedAndCheckResources(boolean forgiveStaleLocks) {
        boolean anyLockedResources = false;
        WeakLinkedList<T> prev = this.resourceHead;
        WeakLinkedList<T> cur = prev.next;
        while (cur != null) {
            ManagedResource<T> resource = cur.getResource();
            if (ManagedResource._isgone(resource)) {
                recordFree(cur.size);
                cur = cur.next;
                prev.next = cur;
            } else {
                if (!resource.isPermanent()) {
                    if (resource.isLocked() && !resource.wasMismatched()) {
                        if (forgiveStaleLocks) {
                            resource.unlockall();
                        } else {
                            resource.setMismatched();
                            anyLockedResources = true;
                        }
                    }
                    resource.bumpAge(1024);
                }
                prev = cur;
                cur = cur.next;
            }
        }
        if (PrismSettings.poolStats || anyLockedResources) {
            if (anyLockedResources) {
                System.err.println("Outstanding resource locks detected:");
            }
            printSummary(true);
            System.err.println();
        }
    }

    static String commas(long v2) {
        return String.format("%,d", Long.valueOf(v2));
    }

    public void printSummary(boolean printlocksources) {
        int numgone = 0;
        int numlocked = 0;
        int numpermanent = 0;
        int numinteresting = 0;
        int nummismatched = 0;
        int numancient = 0;
        long total_age = 0;
        int total = 0;
        double percentUsed = (used() * 100.0d) / max();
        double percentTarget = (target() * 100.0d) / max();
        System.err.printf("%s: %,d used (%.1f%%), %,d target (%.1f%%), %,d max\n", this, Long.valueOf(used()), Double.valueOf(percentUsed), Long.valueOf(target()), Double.valueOf(percentTarget), Long.valueOf(max()));
        WeakLinkedList<T> weakLinkedList = this.resourceHead.next;
        while (true) {
            WeakLinkedList<T> cur = weakLinkedList;
            if (cur != null) {
                ManagedResource<T> mr = cur.getResource();
                total++;
                if (mr == null || !mr.isValid() || mr.isDisposalRequested()) {
                    numgone++;
                } else {
                    int a2 = mr.getAge();
                    total_age += a2;
                    if (a2 >= 1024) {
                        numancient++;
                    }
                    if (mr.wasMismatched()) {
                        nummismatched++;
                    }
                    if (mr.isPermanent()) {
                        numpermanent++;
                    } else if (mr.isLocked()) {
                        numlocked++;
                        if (0 != 0 && printlocksources) {
                            Iterator<Throwable> it = mr.lockedFrom.iterator();
                            while (it.hasNext()) {
                                Throwable th = it.next();
                                th.printStackTrace(System.err);
                            }
                            mr.lockedFrom.clear();
                        }
                    }
                    if (mr.isInteresting()) {
                        numinteresting++;
                    }
                }
                weakLinkedList = cur.next;
            } else {
                double avg_age = total_age / total;
                System.err.println(total + " total resources being managed");
                System.err.printf("average resource age is %.1f frames\n", Double.valueOf(avg_age));
                printpoolpercent(numancient, total, "at maximum supported age");
                printpoolpercent(numpermanent, total, "marked permanent");
                printpoolpercent(nummismatched, total, "have had mismatched locks");
                printpoolpercent(numlocked, total, "locked");
                printpoolpercent(numinteresting, total, "contain interesting data");
                printpoolpercent(numgone, total, "disappeared");
                return;
            }
        }
    }

    private static void printpoolpercent(int stat, int total, String desc) {
        double percent = (stat * 100.0d) / total;
        System.err.printf("%,d resources %s (%.1f%%)\n", Integer.valueOf(stat), desc, Double.valueOf(percent));
    }

    @Override // com.sun.prism.impl.ResourcePool
    public boolean isManagerThread() {
        return Thread.currentThread() == this.managerThread;
    }

    @Override // com.sun.prism.impl.ResourcePool
    public final long managed() {
        return this.managedSize;
    }

    @Override // com.sun.prism.impl.ResourcePool
    public long used() {
        if (this.sharedParent != null) {
            return this.sharedParent.used();
        }
        return this.managedSize;
    }

    @Override // com.sun.prism.impl.ResourcePool
    public final long max() {
        return this.maxSize;
    }

    @Override // com.sun.prism.impl.ResourcePool
    public final long origTarget() {
        return this.origTarget;
    }

    @Override // com.sun.prism.impl.ResourcePool
    public final long target() {
        return this.curTarget;
    }

    @Override // com.sun.prism.impl.ResourcePool
    public final void setTarget(long newTarget) {
        if (newTarget > this.maxSize) {
            throw new IllegalArgumentException("New target " + newTarget + " larger than max " + this.maxSize);
        }
        if (newTarget < this.origTarget) {
            throw new IllegalArgumentException("New target " + newTarget + " smaller than initial target " + this.origTarget);
        }
        this.curTarget = newTarget;
    }

    @Override // com.sun.prism.impl.ResourcePool
    public boolean prepareForAllocation(long size) {
        return cleanup(size);
    }

    @Override // com.sun.prism.impl.ResourcePool
    public final void recordAllocated(long size) {
        this.managedSize += size;
    }

    @Override // com.sun.prism.impl.ResourcePool
    public final void resourceManaged(ManagedResource<T> mr) {
        long size = size(mr.resource);
        this.resourceHead.insert(mr, size);
        recordAllocated(size);
    }

    @Override // com.sun.prism.impl.ResourcePool
    public final void resourceFreed(ManagedResource<T> freed) {
        WeakLinkedList<T> prev = this.resourceHead;
        WeakLinkedList<T> cur = prev.next;
        while (cur != null) {
            ManagedResource<T> res = cur.getResource();
            if (res == null || res == freed) {
                recordFree(cur.size);
                cur = cur.next;
                prev.next = cur;
                if (res == freed) {
                    return;
                }
            } else {
                prev = cur;
                cur = cur.next;
            }
        }
        if (PrismSettings.poolDebug) {
            System.err.println("Warning: unmanaged resource " + ((Object) freed) + " freed from pool: " + ((Object) this));
        }
    }

    @Override // com.sun.prism.impl.ResourcePool
    public final void recordFree(long size) {
        this.managedSize -= size;
        if (this.managedSize < 0) {
            throw new IllegalStateException("Negative resource amount");
        }
    }

    /* loaded from: jfxrt.jar:com/sun/prism/impl/BaseResourcePool$WeakLinkedList.class */
    static class WeakLinkedList<T> {
        final WeakReference<ManagedResource<T>> theResourceRef;
        final long size;
        WeakLinkedList<T> next;

        WeakLinkedList() {
            this.theResourceRef = null;
            this.size = 0L;
        }

        WeakLinkedList(ManagedResource<T> mresource, long size, WeakLinkedList<T> next) {
            this.theResourceRef = new WeakReference<>(mresource);
            this.size = size;
            this.next = next;
        }

        void insert(ManagedResource<T> mresource, long size) {
            this.next = new WeakLinkedList<>(mresource, size, this.next);
        }

        ManagedResource<T> getResource() {
            return this.theResourceRef.get();
        }
    }
}
