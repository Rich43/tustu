package java.util.concurrent;

import com.sun.glass.events.WindowEvent;
import com.sun.org.apache.xalan.internal.templates.Constants;
import sun.misc.Contended;
import sun.misc.Unsafe;

/* loaded from: rt.jar:java/util/concurrent/Exchanger.class */
public class Exchanger<V> {
    private static final int ASHIFT = 7;
    private static final int MMASK = 255;
    private static final int SEQ = 256;
    private static final int NCPU = Runtime.getRuntime().availableProcessors();
    static final int FULL;
    private static final int SPINS = 1024;
    private static final Object NULL_ITEM;
    private static final Object TIMED_OUT;
    private final Participant participant = new Participant();
    private volatile Node[] arena;
    private volatile Node slot;
    private volatile int bound;

    /* renamed from: U, reason: collision with root package name */
    private static final Unsafe f12582U;
    private static final long BOUND;
    private static final long SLOT;
    private static final long MATCH;
    private static final long BLOCKER;
    private static final int ABASE;

    static {
        FULL = NCPU >= 510 ? 255 : NCPU >>> 1;
        NULL_ITEM = new Object();
        TIMED_OUT = new Object();
        try {
            f12582U = Unsafe.getUnsafe();
            BOUND = f12582U.objectFieldOffset(Exchanger.class.getDeclaredField("bound"));
            SLOT = f12582U.objectFieldOffset(Exchanger.class.getDeclaredField("slot"));
            MATCH = f12582U.objectFieldOffset(Node.class.getDeclaredField(Constants.ATTRNAME_MATCH));
            BLOCKER = f12582U.objectFieldOffset(Thread.class.getDeclaredField("parkBlocker"));
            int iArrayIndexScale = f12582U.arrayIndexScale(Node[].class);
            ABASE = f12582U.arrayBaseOffset(Node[].class) + 128;
            if ((iArrayIndexScale & (iArrayIndexScale - 1)) != 0 || iArrayIndexScale > 128) {
                throw new Error("Unsupported array scale");
            }
        } catch (Exception e2) {
            throw new Error(e2);
        }
    }

    @Contended
    /* loaded from: rt.jar:java/util/concurrent/Exchanger$Node.class */
    static final class Node {
        int index;
        int bound;
        int collides;
        int hash;
        Object item;
        volatile Object match;
        volatile Thread parked;

        Node() {
        }
    }

    /* loaded from: rt.jar:java/util/concurrent/Exchanger$Participant.class */
    static final class Participant extends ThreadLocal<Node> {
        Participant() {
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // java.lang.ThreadLocal
        public Node initialValue() {
            return new Node();
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v8, types: [long, sun.misc.Unsafe] */
    private final Object arenaExchange(Object obj, boolean z2, long j2) {
        int i2;
        Node[] nodeArr = this.arena;
        Node node = this.participant.get();
        int i3 = node.index;
        while (true) {
            ?? r0 = f12582U;
            Node node2 = (Node) r0.getObjectVolatile(nodeArr, (i3 << 7) + ABASE);
            if (node2 != null && f12582U.compareAndSwapObject(nodeArr, r0, node2, null)) {
                Object obj2 = node2.item;
                node2.match = obj;
                Thread thread = node2.parked;
                if (thread != null) {
                    f12582U.unpark(thread);
                }
                return obj2;
            }
            int i4 = i3;
            int i5 = this.bound;
            int i6 = i5 & 255;
            if (i4 <= i6 && node2 == null) {
                node.item = obj;
                if (f12582U.compareAndSwapObject(nodeArr, r0, null, node)) {
                    long jNanoTime = (z2 && i6 == 0) ? System.nanoTime() + j2 : 0L;
                    Thread threadCurrentThread = Thread.currentThread();
                    int id = node.hash;
                    int i7 = 1024;
                    while (true) {
                        Object obj3 = node.match;
                        if (obj3 != null) {
                            f12582U.putOrderedObject(node, MATCH, null);
                            node.item = null;
                            node.hash = id;
                            return obj3;
                        }
                        if (i7 > 0) {
                            int i8 = id ^ (id << 1);
                            int i9 = i8 ^ (i8 >>> 3);
                            id = i9 ^ (i9 << 10);
                            if (id == 0) {
                                id = 1024 | ((int) threadCurrentThread.getId());
                            } else if (id < 0) {
                                i7--;
                                if ((i7 & WindowEvent.RESIZE) == 0) {
                                    Thread.yield();
                                }
                            }
                        } else if (f12582U.getObjectVolatile(nodeArr, r0) != node) {
                            i7 = 1024;
                        } else {
                            if (!threadCurrentThread.isInterrupted() && i6 == 0) {
                                if (z2) {
                                    long jNanoTime2 = jNanoTime - System.nanoTime();
                                    j2 = jNanoTime2;
                                    if (jNanoTime2 > 0) {
                                    }
                                }
                                f12582U.putObject(threadCurrentThread, BLOCKER, this);
                                node.parked = threadCurrentThread;
                                if (f12582U.getObjectVolatile(nodeArr, r0) == node) {
                                    f12582U.park(false, j2);
                                }
                                node.parked = null;
                                f12582U.putObject(threadCurrentThread, BLOCKER, (Object) null);
                            }
                            if (f12582U.getObjectVolatile(nodeArr, r0) == node && f12582U.compareAndSwapObject(nodeArr, r0, node, null)) {
                                if (i6 != 0) {
                                    f12582U.compareAndSwapInt(this, BOUND, i5, (i5 + 256) - 1);
                                }
                                node.item = null;
                                node.hash = id;
                                int i10 = node.index >>> 1;
                                node.index = i10;
                                i3 = i10;
                                if (Thread.interrupted()) {
                                    return null;
                                }
                                if (z2 && i6 == 0 && j2 <= 0) {
                                    return TIMED_OUT;
                                }
                            }
                        }
                    }
                } else {
                    node.item = null;
                }
            } else {
                if (node.bound != i5) {
                    node.bound = i5;
                    node.collides = 0;
                    i2 = (i3 != i6 || i6 == 0) ? i6 : i6 - 1;
                } else {
                    int i11 = node.collides;
                    if (i11 < i6 || i6 == FULL || !f12582U.compareAndSwapInt(this, BOUND, i5, i5 + 256 + 1)) {
                        node.collides = i11 + 1;
                        i2 = i3 == 0 ? i6 : i3 - 1;
                    } else {
                        i2 = i6 + 1;
                    }
                }
                i3 = i2;
                node.index = i3;
            }
        }
    }

    private final Object slotExchange(Object obj, boolean z2, long j2) {
        Object obj2;
        Node node = this.participant.get();
        Thread threadCurrentThread = Thread.currentThread();
        if (threadCurrentThread.isInterrupted()) {
            return null;
        }
        while (true) {
            Node node2 = this.slot;
            if (node2 != null) {
                if (f12582U.compareAndSwapObject(this, SLOT, node2, null)) {
                    Object obj3 = node2.item;
                    node2.match = obj;
                    Thread thread = node2.parked;
                    if (thread != null) {
                        f12582U.unpark(thread);
                    }
                    return obj3;
                }
                if (NCPU > 1 && this.bound == 0 && f12582U.compareAndSwapInt(this, BOUND, 0, 256)) {
                    this.arena = new Node[(FULL + 2) << 7];
                }
            } else {
                if (this.arena != null) {
                    return null;
                }
                node.item = obj;
                if (!f12582U.compareAndSwapObject(this, SLOT, null, node)) {
                    node.item = null;
                } else {
                    int id = node.hash;
                    long jNanoTime = z2 ? System.nanoTime() + j2 : 0L;
                    int i2 = NCPU > 1 ? 1024 : 1;
                    while (true) {
                        Object obj4 = node.match;
                        obj2 = obj4;
                        if (obj4 != null) {
                            break;
                        }
                        if (i2 > 0) {
                            int i3 = id ^ (id << 1);
                            int i4 = i3 ^ (i3 >>> 3);
                            id = i4 ^ (i4 << 10);
                            if (id == 0) {
                                id = 1024 | ((int) threadCurrentThread.getId());
                            } else if (id < 0) {
                                i2--;
                                if ((i2 & WindowEvent.RESIZE) == 0) {
                                    Thread.yield();
                                }
                            }
                        } else if (this.slot != node) {
                            i2 = 1024;
                        } else {
                            if (!threadCurrentThread.isInterrupted() && this.arena == null) {
                                if (z2) {
                                    long jNanoTime2 = jNanoTime - System.nanoTime();
                                    j2 = jNanoTime2;
                                    if (jNanoTime2 > 0) {
                                    }
                                }
                                f12582U.putObject(threadCurrentThread, BLOCKER, this);
                                node.parked = threadCurrentThread;
                                if (this.slot == node) {
                                    f12582U.park(false, j2);
                                }
                                node.parked = null;
                                f12582U.putObject(threadCurrentThread, BLOCKER, (Object) null);
                            }
                            if (f12582U.compareAndSwapObject(this, SLOT, node, null)) {
                                obj2 = (!z2 || j2 > 0 || threadCurrentThread.isInterrupted()) ? null : TIMED_OUT;
                            }
                        }
                    }
                    f12582U.putOrderedObject(node, MATCH, null);
                    node.item = null;
                    node.hash = id;
                    return obj2;
                }
            }
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:13:0x002e, code lost:
    
        if (r0 == null) goto L14;
     */
    /* JADX WARN: Removed duplicated region for block: B:10:0x001f  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public V exchange(V r7) throws java.lang.InterruptedException {
        /*
            r6 = this;
            r0 = r7
            if (r0 != 0) goto La
            java.lang.Object r0 = java.util.concurrent.Exchanger.NULL_ITEM
            goto Lb
        La:
            r0 = r7
        Lb:
            r9 = r0
            r0 = r6
            java.util.concurrent.Exchanger$Node[] r0 = r0.arena
            if (r0 != 0) goto L1f
            r0 = r6
            r1 = r9
            r2 = 0
            r3 = 0
            java.lang.Object r0 = r0.slotExchange(r1, r2, r3)
            r1 = r0
            r8 = r1
            if (r0 != 0) goto L39
        L1f:
            boolean r0 = java.lang.Thread.interrupted()
            if (r0 != 0) goto L31
            r0 = r6
            r1 = r9
            r2 = 0
            r3 = 0
            java.lang.Object r0 = r0.arenaExchange(r1, r2, r3)
            r1 = r0
            r8 = r1
            if (r0 != 0) goto L39
        L31:
            java.lang.InterruptedException r0 = new java.lang.InterruptedException
            r1 = r0
            r1.<init>()
            throw r0
        L39:
            r0 = r8
            java.lang.Object r1 = java.util.concurrent.Exchanger.NULL_ITEM
            if (r0 != r1) goto L44
            r0 = 0
            goto L45
        L44:
            r0 = r8
        L45:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: java.util.concurrent.Exchanger.exchange(java.lang.Object):java.lang.Object");
    }

    /* JADX WARN: Code restructure failed: missing block: B:13:0x003d, code lost:
    
        if (r0 == null) goto L14;
     */
    /* JADX WARN: Removed duplicated region for block: B:10:0x002b  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public V exchange(V r7, long r8, java.util.concurrent.TimeUnit r10) throws java.lang.InterruptedException, java.util.concurrent.TimeoutException {
        /*
            r6 = this;
            r0 = r7
            if (r0 != 0) goto La
            java.lang.Object r0 = java.util.concurrent.Exchanger.NULL_ITEM
            goto Lb
        La:
            r0 = r7
        Lb:
            r12 = r0
            r0 = r10
            r1 = r8
            long r0 = r0.toNanos(r1)
            r13 = r0
            r0 = r6
            java.util.concurrent.Exchanger$Node[] r0 = r0.arena
            if (r0 != 0) goto L2b
            r0 = r6
            r1 = r12
            r2 = 1
            r3 = r13
            java.lang.Object r0 = r0.slotExchange(r1, r2, r3)
            r1 = r0
            r11 = r1
            if (r0 != 0) goto L48
        L2b:
            boolean r0 = java.lang.Thread.interrupted()
            if (r0 != 0) goto L40
            r0 = r6
            r1 = r12
            r2 = 1
            r3 = r13
            java.lang.Object r0 = r0.arenaExchange(r1, r2, r3)
            r1 = r0
            r11 = r1
            if (r0 != 0) goto L48
        L40:
            java.lang.InterruptedException r0 = new java.lang.InterruptedException
            r1 = r0
            r1.<init>()
            throw r0
        L48:
            r0 = r11
            java.lang.Object r1 = java.util.concurrent.Exchanger.TIMED_OUT
            if (r0 != r1) goto L58
            java.util.concurrent.TimeoutException r0 = new java.util.concurrent.TimeoutException
            r1 = r0
            r1.<init>()
            throw r0
        L58:
            r0 = r11
            java.lang.Object r1 = java.util.concurrent.Exchanger.NULL_ITEM
            if (r0 != r1) goto L64
            r0 = 0
            goto L66
        L64:
            r0 = r11
        L66:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: java.util.concurrent.Exchanger.exchange(java.lang.Object, long, java.util.concurrent.TimeUnit):java.lang.Object");
    }
}
