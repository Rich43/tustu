package java.util.concurrent;

import A.a;
import com.sun.org.apache.xalan.internal.xsltc.compiler.Constants;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.invoke.SerializedLambda;
import java.util.AbstractCollection;
import java.util.AbstractMap;
import java.util.AbstractSet;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.NavigableSet;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.Spliterator;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import javax.swing.JSplitPane;
import sun.misc.Unsafe;

/* loaded from: rt.jar:java/util/concurrent/ConcurrentSkipListMap.class */
public class ConcurrentSkipListMap<K, V> extends AbstractMap<K, V> implements ConcurrentNavigableMap<K, V>, Cloneable, Serializable {
    private static final long serialVersionUID = -8627078645895051609L;
    private static final Object BASE_HEADER = new Object();
    private volatile transient HeadIndex<K, V> head;
    final Comparator<? super K> comparator;
    private transient KeySet<K> keySet;
    private transient EntrySet<K, V> entrySet;
    private transient Values<V> values;
    private transient ConcurrentNavigableMap<K, V> descendingMap;
    private static final int EQ = 1;
    private static final int LT = 2;
    private static final int GT = 0;
    private static final Unsafe UNSAFE;
    private static final long headOffset;
    private static final long SECONDARY;

    /* JADX WARN: Multi-variable type inference failed */
    @Override // java.util.concurrent.ConcurrentNavigableMap, java.util.NavigableMap, java.util.SortedMap
    public /* bridge */ /* synthetic */ SortedMap tailMap(Object obj) {
        return tailMap((ConcurrentSkipListMap<K, V>) obj);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // java.util.concurrent.ConcurrentNavigableMap, java.util.NavigableMap, java.util.SortedMap
    public /* bridge */ /* synthetic */ SortedMap headMap(Object obj) {
        return headMap((ConcurrentSkipListMap<K, V>) obj);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // java.util.concurrent.ConcurrentNavigableMap, java.util.NavigableMap
    public /* bridge */ /* synthetic */ NavigableMap tailMap(Object obj, boolean z2) {
        return tailMap((ConcurrentSkipListMap<K, V>) obj, z2);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // java.util.concurrent.ConcurrentNavigableMap, java.util.NavigableMap
    public /* bridge */ /* synthetic */ NavigableMap headMap(Object obj, boolean z2) {
        return headMap((ConcurrentSkipListMap<K, V>) obj, z2);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // java.util.concurrent.ConcurrentNavigableMap, java.util.NavigableMap
    public /* bridge */ /* synthetic */ NavigableMap subMap(Object obj, boolean z2, Object obj2, boolean z3) {
        return subMap((boolean) obj, z2, (boolean) obj2, z3);
    }

    static {
        try {
            UNSAFE = Unsafe.getUnsafe();
            headOffset = UNSAFE.objectFieldOffset(ConcurrentSkipListMap.class.getDeclaredField("head"));
            SECONDARY = UNSAFE.objectFieldOffset(Thread.class.getDeclaredField("threadLocalRandomSecondarySeed"));
        } catch (Exception e2) {
            throw new Error(e2);
        }
    }

    private void initialize() {
        this.keySet = null;
        this.entrySet = null;
        this.values = null;
        this.descendingMap = null;
        this.head = new HeadIndex<>(new Node(null, BASE_HEADER, null), null, null, 1);
    }

    private boolean casHead(HeadIndex<K, V> headIndex, HeadIndex<K, V> headIndex2) {
        return UNSAFE.compareAndSwapObject(this, headOffset, headIndex, headIndex2);
    }

    /* loaded from: rt.jar:java/util/concurrent/ConcurrentSkipListMap$Node.class */
    static final class Node<K, V> {
        final K key;
        volatile Object value;
        volatile Node<K, V> next;
        private static final Unsafe UNSAFE;
        private static final long valueOffset;
        private static final long nextOffset;

        Node(K k2, Object obj, Node<K, V> node) {
            this.key = k2;
            this.value = obj;
            this.next = node;
        }

        Node(Node<K, V> node) {
            this.key = null;
            this.value = this;
            this.next = node;
        }

        boolean casValue(Object obj, Object obj2) {
            return UNSAFE.compareAndSwapObject(this, valueOffset, obj, obj2);
        }

        boolean casNext(Node<K, V> node, Node<K, V> node2) {
            return UNSAFE.compareAndSwapObject(this, nextOffset, node, node2);
        }

        boolean isMarker() {
            return this.value == this;
        }

        boolean isBaseHeader() {
            return this.value == ConcurrentSkipListMap.BASE_HEADER;
        }

        boolean appendMarker(Node<K, V> node) {
            return casNext(node, new Node<>(node));
        }

        void helpDelete(Node<K, V> node, Node<K, V> node2) {
            if (node2 == this.next && this == node.next) {
                if (node2 == null || node2.value != node2) {
                    casNext(node2, new Node<>(node2));
                } else {
                    node.casNext(this, node2.next);
                }
            }
        }

        V getValidValue() {
            V v2 = (V) this.value;
            if (v2 == this || v2 == ConcurrentSkipListMap.BASE_HEADER) {
                return null;
            }
            return v2;
        }

        AbstractMap.SimpleImmutableEntry<K, V> createSnapshot() {
            Object obj = this.value;
            if (obj == null || obj == this || obj == ConcurrentSkipListMap.BASE_HEADER) {
                return null;
            }
            return new AbstractMap.SimpleImmutableEntry<>(this.key, obj);
        }

        static {
            try {
                UNSAFE = Unsafe.getUnsafe();
                valueOffset = UNSAFE.objectFieldOffset(Node.class.getDeclaredField("value"));
                nextOffset = UNSAFE.objectFieldOffset(Node.class.getDeclaredField(Constants.NEXT));
            } catch (Exception e2) {
                throw new Error(e2);
            }
        }
    }

    /* loaded from: rt.jar:java/util/concurrent/ConcurrentSkipListMap$Index.class */
    static class Index<K, V> {
        final Node<K, V> node;
        final Index<K, V> down;
        volatile Index<K, V> right;
        private static final Unsafe UNSAFE;
        private static final long rightOffset;

        Index(Node<K, V> node, Index<K, V> index, Index<K, V> index2) {
            this.node = node;
            this.down = index;
            this.right = index2;
        }

        final boolean casRight(Index<K, V> index, Index<K, V> index2) {
            return UNSAFE.compareAndSwapObject(this, rightOffset, index, index2);
        }

        final boolean indexesDeletedNode() {
            return this.node.value == null;
        }

        final boolean link(Index<K, V> index, Index<K, V> index2) {
            Node<K, V> node = this.node;
            index2.right = index;
            return node.value != null && casRight(index, index2);
        }

        final boolean unlink(Index<K, V> index) {
            return this.node.value != null && casRight(index, index.right);
        }

        static {
            try {
                UNSAFE = Unsafe.getUnsafe();
                rightOffset = UNSAFE.objectFieldOffset(Index.class.getDeclaredField(JSplitPane.RIGHT));
            } catch (Exception e2) {
                throw new Error(e2);
            }
        }
    }

    /* loaded from: rt.jar:java/util/concurrent/ConcurrentSkipListMap$HeadIndex.class */
    static final class HeadIndex<K, V> extends Index<K, V> {
        final int level;

        HeadIndex(Node<K, V> node, Index<K, V> index, Index<K, V> index2, int i2) {
            super(node, index, index2);
            this.level = i2;
        }
    }

    static final int cpr(Comparator comparator, Object obj, Object obj2) {
        return comparator != null ? comparator.compare(obj, obj2) : ((Comparable) obj).compareTo(obj2);
    }

    private Node<K, V> findPredecessor(Object obj, Comparator<? super K> comparator) {
        if (obj == null) {
            throw new NullPointerException();
        }
        while (true) {
            HeadIndex<K, V> headIndex = this.head;
            Index<K, V> index = headIndex.right;
            while (true) {
                Index<K, V> index2 = index;
                if (index2 != null) {
                    Node<K, V> node = index2.node;
                    K k2 = node.key;
                    if (node.value == null) {
                        if (!headIndex.unlink(index2)) {
                            break;
                        }
                        index = headIndex.right;
                    } else if (cpr(comparator, obj, k2) > 0) {
                        headIndex = index2;
                        index = index2.right;
                    }
                }
                Index<K, V> index3 = headIndex.down;
                if (index3 == null) {
                    return headIndex.node;
                }
                headIndex = index3;
                index = index3.right;
            }
        }
    }

    private Node<K, V> findNode(Object obj) {
        if (obj == null) {
            throw new NullPointerException();
        }
        Comparator<? super K> comparator = this.comparator;
        while (true) {
            Node<K, V> nodeFindPredecessor = findPredecessor(obj, comparator);
            Node<K, V> node = nodeFindPredecessor.next;
            while (true) {
                Node<K, V> node2 = node;
                if (node2 != null) {
                    Node<K, V> node3 = node2.next;
                    if (node2 == nodeFindPredecessor.next) {
                        Object obj2 = node2.value;
                        if (obj2 == null) {
                            node2.helpDelete(nodeFindPredecessor, node3);
                            break;
                        }
                        if (nodeFindPredecessor.value == null || obj2 == node2) {
                            break;
                        }
                        int iCpr = cpr(comparator, obj, node2.key);
                        if (iCpr == 0) {
                            return node2;
                        }
                        if (iCpr >= 0) {
                            nodeFindPredecessor = node2;
                            node = node3;
                        } else {
                            return null;
                        }
                    }
                } else {
                    return null;
                }
            }
        }
    }

    private V doGet(Object obj) {
        if (obj == null) {
            throw new NullPointerException();
        }
        Comparator<? super K> comparator = this.comparator;
        while (true) {
            Node<K, V> nodeFindPredecessor = findPredecessor(obj, comparator);
            Node<K, V> node = nodeFindPredecessor.next;
            while (true) {
                Node<K, V> node2 = node;
                if (node2 != null) {
                    Node<K, V> node3 = node2.next;
                    if (node2 == nodeFindPredecessor.next) {
                        V v2 = (V) node2.value;
                        if (v2 == null) {
                            node2.helpDelete(nodeFindPredecessor, node3);
                            break;
                        }
                        if (nodeFindPredecessor.value == null || v2 == node2) {
                            break;
                        }
                        int iCpr = cpr(comparator, obj, node2.key);
                        if (iCpr == 0) {
                            return v2;
                        }
                        if (iCpr >= 0) {
                            nodeFindPredecessor = node2;
                            node = node3;
                        } else {
                            return null;
                        }
                    }
                } else {
                    return null;
                }
            }
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:100:0x0284, code lost:
    
        return null;
     */
    /* JADX WARN: Code restructure failed: missing block: B:111:0x0012, code lost:
    
        continue;
     */
    /* JADX WARN: Code restructure failed: missing block: B:140:?, code lost:
    
        return null;
     */
    /* JADX WARN: Code restructure failed: missing block: B:141:?, code lost:
    
        return null;
     */
    /* JADX WARN: Code restructure failed: missing block: B:142:?, code lost:
    
        return null;
     */
    /* JADX WARN: Code restructure failed: missing block: B:143:?, code lost:
    
        return null;
     */
    /* JADX WARN: Code restructure failed: missing block: B:31:0x009a, code lost:
    
        return r0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:32:0x009b, code lost:
    
        r0 = new java.util.concurrent.ConcurrentSkipListMap.Node<>(r9, r10, r15);
     */
    /* JADX WARN: Code restructure failed: missing block: B:33:0x00b1, code lost:
    
        if (r14.casNext(r15, r0) != false) goto L108;
     */
    /* JADX WARN: Code restructure failed: missing block: B:36:0x00ba, code lost:
    
        r14 = java.util.concurrent.ThreadLocalRandom.nextSecondarySeed();
     */
    /* JADX WARN: Code restructure failed: missing block: B:37:0x00c4, code lost:
    
        if ((r14 & (-2147483647)) != 0) goto L100;
     */
    /* JADX WARN: Code restructure failed: missing block: B:38:0x00c7, code lost:
    
        r15 = 1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:39:0x00ca, code lost:
    
        r0 = r14 >>> 1;
        r14 = r0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:40:0x00d3, code lost:
    
        if ((r0 & 1) == 0) goto L117;
     */
    /* JADX WARN: Code restructure failed: missing block: B:41:0x00d6, code lost:
    
        r15 = r15 + 1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:42:0x00dc, code lost:
    
        r17 = null;
        r18 = r8.head;
        r0 = r15;
        r1 = r18.level;
     */
    /* JADX WARN: Code restructure failed: missing block: B:43:0x00ef, code lost:
    
        if (r0 > r1) goto L49;
     */
    /* JADX WARN: Code restructure failed: missing block: B:44:0x00f2, code lost:
    
        r19 = 1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:46:0x00f9, code lost:
    
        if (r19 > r15) goto L118;
     */
    /* JADX WARN: Code restructure failed: missing block: B:47:0x00fc, code lost:
    
        r17 = new java.util.concurrent.ConcurrentSkipListMap.Index<>(r0, r17, null);
        r19 = r19 + 1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:49:0x0113, code lost:
    
        r15 = r1 + 1;
        r0 = new java.util.concurrent.ConcurrentSkipListMap.Index[r15 + 1];
        r20 = 1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:51:0x012c, code lost:
    
        if (r20 > r15) goto L135;
     */
    /* JADX WARN: Code restructure failed: missing block: B:52:0x012f, code lost:
    
        r2 = new java.util.concurrent.ConcurrentSkipListMap.Index<>(r0, r17, null);
        r17 = r2;
        r0[r20] = r2;
        r20 = r20 + 1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:53:0x0149, code lost:
    
        r18 = r8.head;
        r0 = r18.level;
     */
    /* JADX WARN: Code restructure failed: missing block: B:54:0x015a, code lost:
    
        if (r15 > r0) goto L56;
     */
    /* JADX WARN: Code restructure failed: missing block: B:56:0x0160, code lost:
    
        r21 = r18;
        r0 = r18.node;
        r23 = r0 + 1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:58:0x0175, code lost:
    
        if (r23 > r15) goto L139;
     */
    /* JADX WARN: Code restructure failed: missing block: B:59:0x0178, code lost:
    
        r21 = new java.util.concurrent.ConcurrentSkipListMap.HeadIndex<>(r0, r21, r0[r23], r23);
        r23 = r23 + 1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:61:0x019a, code lost:
    
        if (casHead(r18, r21) == false) goto L138;
     */
    /* JADX WARN: Code restructure failed: missing block: B:62:0x019d, code lost:
    
        r18 = r21;
        r15 = r0;
        r17 = r0[r0];
     */
    /* JADX WARN: Code restructure failed: missing block: B:64:0x01b1, code lost:
    
        r19 = r15;
     */
    /* JADX WARN: Code restructure failed: missing block: B:65:0x01b5, code lost:
    
        r20 = r18.level;
        r21 = r18;
        r22 = r21.right;
        r23 = r17;
     */
    /* JADX WARN: Code restructure failed: missing block: B:67:0x01cd, code lost:
    
        if (r21 == null) goto L119;
     */
    /* JADX WARN: Code restructure failed: missing block: B:69:0x01d2, code lost:
    
        if (r23 != null) goto L71;
     */
    /* JADX WARN: Code restructure failed: missing block: B:72:0x01da, code lost:
    
        if (r22 == null) goto L126;
     */
    /* JADX WARN: Code restructure failed: missing block: B:73:0x01dd, code lost:
    
        r0 = r22.node;
        r0 = cpr(r0, r9, r0.key);
     */
    /* JADX WARN: Code restructure failed: missing block: B:74:0x01f6, code lost:
    
        if (r0.value != null) goto L127;
     */
    /* JADX WARN: Code restructure failed: missing block: B:76:0x0200, code lost:
    
        if (r21.unlink(r22) != false) goto L78;
     */
    /* JADX WARN: Code restructure failed: missing block: B:78:0x0206, code lost:
    
        r22 = r21.right;
     */
    /* JADX WARN: Code restructure failed: missing block: B:80:0x0212, code lost:
    
        if (r0 <= 0) goto L128;
     */
    /* JADX WARN: Code restructure failed: missing block: B:81:0x0215, code lost:
    
        r21 = r22;
        r22 = r22.right;
     */
    /* JADX WARN: Code restructure failed: missing block: B:83:0x0227, code lost:
    
        if (r20 != r19) goto L93;
     */
    /* JADX WARN: Code restructure failed: missing block: B:85:0x0233, code lost:
    
        if (r21.link(r22, r23) != false) goto L87;
     */
    /* JADX WARN: Code restructure failed: missing block: B:88:0x0241, code lost:
    
        if (r23.node.value != null) goto L90;
     */
    /* JADX WARN: Code restructure failed: missing block: B:89:0x0244, code lost:
    
        findNode(r9);
     */
    /* JADX WARN: Code restructure failed: missing block: B:90:0x024d, code lost:
    
        r19 = r19 - 1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:91:0x0252, code lost:
    
        if (r19 != 0) goto L93;
     */
    /* JADX WARN: Code restructure failed: missing block: B:93:0x0258, code lost:
    
        r20 = r20 - 1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:94:0x025f, code lost:
    
        if (r20 < r19) goto L98;
     */
    /* JADX WARN: Code restructure failed: missing block: B:96:0x0266, code lost:
    
        if (r20 >= r15) goto L98;
     */
    /* JADX WARN: Code restructure failed: missing block: B:97:0x0269, code lost:
    
        r23 = r23.down;
     */
    /* JADX WARN: Code restructure failed: missing block: B:98:0x0270, code lost:
    
        r21 = r21.down;
        r22 = r21.right;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private V doPut(K r9, V r10, boolean r11) {
        /*
            Method dump skipped, instructions count: 646
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: java.util.concurrent.ConcurrentSkipListMap.doPut(java.lang.Object, java.lang.Object, boolean):java.lang.Object");
    }

    final V doRemove(Object obj, Object obj2) {
        if (obj == null) {
            throw new NullPointerException();
        }
        Comparator<? super K> comparator = this.comparator;
        while (true) {
            Node<K, V> nodeFindPredecessor = findPredecessor(obj, comparator);
            Node<K, V> node = nodeFindPredecessor.next;
            while (true) {
                Node<K, V> node2 = node;
                if (node2 != null) {
                    Node<K, V> node3 = node2.next;
                    if (node2 == nodeFindPredecessor.next) {
                        V v2 = (V) node2.value;
                        if (v2 == null) {
                            node2.helpDelete(nodeFindPredecessor, node3);
                            break;
                        }
                        if (nodeFindPredecessor.value == null || v2 == node2) {
                            break;
                        }
                        int iCpr = cpr(comparator, obj, node2.key);
                        if (iCpr >= 0) {
                            if (iCpr > 0) {
                                nodeFindPredecessor = node2;
                                node = node3;
                            } else if (obj2 == null || obj2.equals(v2)) {
                                if (node2.casValue(v2, null)) {
                                    if (!node2.appendMarker(node3) || !nodeFindPredecessor.casNext(node2, node3)) {
                                        findNode(obj);
                                    } else {
                                        findPredecessor(obj, comparator);
                                        if (this.head.right == null) {
                                            tryReduceLevel();
                                        }
                                    }
                                    return v2;
                                }
                            } else {
                                return null;
                            }
                        } else {
                            return null;
                        }
                    }
                } else {
                    return null;
                }
            }
        }
    }

    private void tryReduceLevel() {
        HeadIndex<K, V> headIndex;
        HeadIndex headIndex2;
        HeadIndex<K, V> headIndex3 = this.head;
        if (headIndex3.level > 3 && (headIndex = (HeadIndex) headIndex3.down) != null && (headIndex2 = (HeadIndex) headIndex.down) != null && headIndex2.right == null && headIndex.right == null && headIndex3.right == null && casHead(headIndex3, headIndex) && headIndex3.right != null) {
            casHead(headIndex, headIndex3);
        }
    }

    final Node<K, V> findFirst() {
        while (true) {
            Node<K, V> node = this.head.node;
            Node<K, V> node2 = node.next;
            if (node2 == null) {
                return null;
            }
            if (node2.value != null) {
                return node2;
            }
            node2.helpDelete(node, node2.next);
        }
    }

    private Map.Entry<K, V> doRemoveFirstEntry() {
        while (true) {
            Node<K, V> node = this.head.node;
            Node<K, V> node2 = node.next;
            if (node2 == null) {
                return null;
            }
            Node<K, V> node3 = node2.next;
            if (node2 == node.next) {
                Object obj = node2.value;
                if (obj == null) {
                    node2.helpDelete(node, node3);
                } else if (node2.casValue(obj, null)) {
                    if (!node2.appendMarker(node3) || !node.casNext(node2, node3)) {
                        findFirst();
                    }
                    clearIndexToFirst();
                    return new AbstractMap.SimpleImmutableEntry(node2.key, obj);
                }
            }
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:14:0x0033  */
    /* JADX WARN: Removed duplicated region for block: B:21:? A[RETURN, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private void clearIndexToFirst() {
        /*
            r3 = this;
        L0:
            r0 = r3
            java.util.concurrent.ConcurrentSkipListMap$HeadIndex<K, V> r0 = r0.head
            r4 = r0
        L5:
            r0 = r4
            java.util.concurrent.ConcurrentSkipListMap$Index<K, V> r0 = r0.right
            r5 = r0
            r0 = r5
            if (r0 == 0) goto L20
            r0 = r5
            boolean r0 = r0.indexesDeletedNode()
            if (r0 == 0) goto L20
            r0 = r4
            r1 = r5
            boolean r0 = r0.unlink(r1)
            if (r0 != 0) goto L20
            goto L3b
        L20:
            r0 = r4
            java.util.concurrent.ConcurrentSkipListMap$Index<K, V> r0 = r0.down
            r1 = r0
            r4 = r1
            if (r0 != 0) goto L38
            r0 = r3
            java.util.concurrent.ConcurrentSkipListMap$HeadIndex<K, V> r0 = r0.head
            java.util.concurrent.ConcurrentSkipListMap$Index<K, V> r0 = r0.right
            if (r0 != 0) goto L37
            r0 = r3
            r0.tryReduceLevel()
        L37:
            return
        L38:
            goto L5
        L3b:
            goto L0
        */
        throw new UnsupportedOperationException("Method not decompiled: java.util.concurrent.ConcurrentSkipListMap.clearIndexToFirst():void");
    }

    /* JADX WARN: Code restructure failed: missing block: B:43:0x0000, code lost:
    
        continue;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private java.util.Map.Entry<K, V> doRemoveLastEntry() {
        /*
            r5 = this;
        L0:
            r0 = r5
            java.util.concurrent.ConcurrentSkipListMap$Node r0 = r0.findPredecessorOfLast()
            r6 = r0
            r0 = r6
            java.util.concurrent.ConcurrentSkipListMap$Node<K, V> r0 = r0.next
            r7 = r0
            r0 = r7
            if (r0 != 0) goto L17
            r0 = r6
            boolean r0 = r0.isBaseHeader()
            if (r0 == 0) goto L0
            r0 = 0
            return r0
        L17:
            r0 = r7
            java.util.concurrent.ConcurrentSkipListMap$Node<K, V> r0 = r0.next
            r8 = r0
            r0 = r7
            r1 = r6
            java.util.concurrent.ConcurrentSkipListMap$Node<K, V> r1 = r1.next
            if (r0 == r1) goto L27
            goto Lad
        L27:
            r0 = r7
            java.lang.Object r0 = r0.value
            r9 = r0
            r0 = r9
            if (r0 != 0) goto L3b
            r0 = r7
            r1 = r6
            r2 = r8
            r0.helpDelete(r1, r2)
            goto Lad
        L3b:
            r0 = r6
            java.lang.Object r0 = r0.value
            if (r0 == 0) goto Lad
            r0 = r9
            r1 = r7
            if (r0 != r1) goto L4b
            goto Lad
        L4b:
            r0 = r8
            if (r0 == 0) goto L56
            r0 = r7
            r6 = r0
            r0 = r8
            r7 = r0
            goto L17
        L56:
            r0 = r7
            r1 = r9
            r2 = 0
            boolean r0 = r0.casValue(r1, r2)
            if (r0 != 0) goto L63
            goto Lad
        L63:
            r0 = r7
            K r0 = r0.key
            r10 = r0
            r0 = r7
            r1 = r8
            boolean r0 = r0.appendMarker(r1)
            if (r0 == 0) goto L7a
            r0 = r6
            r1 = r7
            r2 = r8
            boolean r0 = r0.casNext(r1, r2)
            if (r0 != 0) goto L84
        L7a:
            r0 = r5
            r1 = r10
            java.util.concurrent.ConcurrentSkipListMap$Node r0 = r0.findNode(r1)
            goto L9d
        L84:
            r0 = r5
            r1 = r10
            r2 = r5
            java.util.Comparator<? super K> r2 = r2.comparator
            java.util.concurrent.ConcurrentSkipListMap$Node r0 = r0.findPredecessor(r1, r2)
            r0 = r5
            java.util.concurrent.ConcurrentSkipListMap$HeadIndex<K, V> r0 = r0.head
            java.util.concurrent.ConcurrentSkipListMap$Index<K, V> r0 = r0.right
            if (r0 != 0) goto L9d
            r0 = r5
            r0.tryReduceLevel()
        L9d:
            r0 = r9
            r11 = r0
            java.util.AbstractMap$SimpleImmutableEntry r0 = new java.util.AbstractMap$SimpleImmutableEntry
            r1 = r0
            r2 = r10
            r3 = r11
            r1.<init>(r2, r3)
            return r0
        Lad:
            goto L0
        */
        throw new UnsupportedOperationException("Method not decompiled: java.util.concurrent.ConcurrentSkipListMap.doRemoveLastEntry():java.util.Map$Entry");
    }

    final Node<K, V> findLast() {
        Node<K, V> node;
        HeadIndex<K, V> headIndex = this.head;
        loop0: while (true) {
            HeadIndex<K, V> headIndex2 = headIndex;
            Index<K, V> index = headIndex2.right;
            if (index != null) {
                if (index.indexesDeletedNode()) {
                    headIndex2.unlink(index);
                    headIndex = this.head;
                } else {
                    headIndex = index;
                }
            } else {
                Index<K, V> index2 = headIndex2.down;
                if (index2 != null) {
                    headIndex = index2;
                } else {
                    node = headIndex2.node;
                    Node<K, V> node2 = node.next;
                    while (true) {
                        Node<K, V> node3 = node2;
                        if (node3 != null) {
                            Node<K, V> node4 = node3.next;
                            if (node3 != node.next) {
                                break;
                            }
                            Object obj = node3.value;
                            if (obj == null) {
                                node3.helpDelete(node, node4);
                                break;
                            }
                            if (node.value == null || obj == node3) {
                                break;
                            }
                            node = node3;
                            node2 = node4;
                        } else {
                            break loop0;
                        }
                    }
                    headIndex = this.head;
                }
            }
        }
        if (node.isBaseHeader()) {
            return null;
        }
        return node;
    }

    private Node<K, V> findPredecessorOfLast() {
        HeadIndex<K, V> headIndex;
        Index<K, V> index;
        while (true) {
            HeadIndex<K, V> headIndex2 = this.head;
            while (true) {
                headIndex = headIndex2;
                index = headIndex.right;
                if (index != null) {
                    if (index.indexesDeletedNode()) {
                        break;
                    }
                    if (index.node.next != null) {
                        headIndex2 = index;
                    }
                }
                Index<K, V> index2 = headIndex.down;
                if (index2 != null) {
                    headIndex2 = index2;
                } else {
                    return headIndex.node;
                }
            }
            headIndex.unlink(index);
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:36:0x0096, code lost:
    
        return r9;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    final java.util.concurrent.ConcurrentSkipListMap.Node<K, V> findNear(K r5, int r6, java.util.Comparator<? super K> r7) {
        /*
            r4 = this;
            r0 = r5
            if (r0 != 0) goto Lc
            java.lang.NullPointerException r0 = new java.lang.NullPointerException
            r1 = r0
            r1.<init>()
            throw r0
        Lc:
            r0 = r4
            r1 = r5
            r2 = r7
            java.util.concurrent.ConcurrentSkipListMap$Node r0 = r0.findPredecessor(r1, r2)
            r8 = r0
            r0 = r8
            java.util.concurrent.ConcurrentSkipListMap$Node<K, V> r0 = r0.next
            r9 = r0
        L1b:
            r0 = r9
            if (r0 != 0) goto L35
            r0 = r6
            r1 = 2
            r0 = r0 & r1
            if (r0 == 0) goto L2e
            r0 = r8
            boolean r0 = r0.isBaseHeader()
            if (r0 == 0) goto L32
        L2e:
            r0 = 0
            goto L34
        L32:
            r0 = r8
        L34:
            return r0
        L35:
            r0 = r9
            java.util.concurrent.ConcurrentSkipListMap$Node<K, V> r0 = r0.next
            r11 = r0
            r0 = r9
            r1 = r8
            java.util.concurrent.ConcurrentSkipListMap$Node<K, V> r1 = r1.next
            if (r0 == r1) goto L49
            goto Lbc
        L49:
            r0 = r9
            java.lang.Object r0 = r0.value
            r1 = r0
            r10 = r1
            if (r0 != 0) goto L60
            r0 = r9
            r1 = r8
            r2 = r11
            r0.helpDelete(r1, r2)
            goto Lbc
        L60:
            r0 = r8
            java.lang.Object r0 = r0.value
            if (r0 == 0) goto Lbc
            r0 = r10
            r1 = r9
            if (r0 != r1) goto L72
            goto Lbc
        L72:
            r0 = r7
            r1 = r5
            r2 = r9
            K r2 = r2.key
            int r0 = cpr(r0, r1, r2)
            r12 = r0
            r0 = r12
            if (r0 != 0) goto L89
            r0 = r6
            r1 = 1
            r0 = r0 & r1
            if (r0 != 0) goto L94
        L89:
            r0 = r12
            if (r0 >= 0) goto L97
            r0 = r6
            r1 = 2
            r0 = r0 & r1
            if (r0 != 0) goto L97
        L94:
            r0 = r9
            return r0
        L97:
            r0 = r12
            if (r0 > 0) goto Lb1
            r0 = r6
            r1 = 2
            r0 = r0 & r1
            if (r0 == 0) goto Lb1
            r0 = r8
            boolean r0 = r0.isBaseHeader()
            if (r0 == 0) goto Lae
            r0 = 0
            goto Lb0
        Lae:
            r0 = r8
        Lb0:
            return r0
        Lb1:
            r0 = r9
            r8 = r0
            r0 = r11
            r9 = r0
            goto L1b
        Lbc:
            goto Lc
        */
        throw new UnsupportedOperationException("Method not decompiled: java.util.concurrent.ConcurrentSkipListMap.findNear(java.lang.Object, int, java.util.Comparator):java.util.concurrent.ConcurrentSkipListMap$Node");
    }

    final AbstractMap.SimpleImmutableEntry<K, V> getNear(K k2, int i2) {
        AbstractMap.SimpleImmutableEntry<K, V> simpleImmutableEntryCreateSnapshot;
        Comparator<? super K> comparator = this.comparator;
        do {
            Node<K, V> nodeFindNear = findNear(k2, i2, comparator);
            if (nodeFindNear == null) {
                return null;
            }
            simpleImmutableEntryCreateSnapshot = nodeFindNear.createSnapshot();
        } while (simpleImmutableEntryCreateSnapshot == null);
        return simpleImmutableEntryCreateSnapshot;
    }

    public ConcurrentSkipListMap() {
        this.comparator = null;
        initialize();
    }

    public ConcurrentSkipListMap(Comparator<? super K> comparator) {
        this.comparator = comparator;
        initialize();
    }

    public ConcurrentSkipListMap(Map<? extends K, ? extends V> map) {
        this.comparator = null;
        initialize();
        putAll(map);
    }

    public ConcurrentSkipListMap(SortedMap<K, ? extends V> sortedMap) {
        this.comparator = sortedMap.comparator();
        initialize();
        buildFromSorted(sortedMap);
    }

    @Override // java.util.AbstractMap
    public ConcurrentSkipListMap<K, V> clone() {
        try {
            ConcurrentSkipListMap<K, V> concurrentSkipListMap = (ConcurrentSkipListMap) super.clone();
            concurrentSkipListMap.initialize();
            concurrentSkipListMap.buildFromSorted(this);
            return concurrentSkipListMap;
        } catch (CloneNotSupportedException e2) {
            throw new InternalError();
        }
    }

    private void buildFromSorted(SortedMap<K, ? extends V> sortedMap) {
        int i2;
        if (sortedMap == null) {
            throw new NullPointerException();
        }
        HeadIndex<K, V> headIndex = this.head;
        Node<K, V> node = headIndex.node;
        ArrayList arrayList = new ArrayList();
        for (int i3 = 0; i3 <= headIndex.level; i3++) {
            arrayList.add(null);
        }
        HeadIndex<K, V> headIndex2 = headIndex;
        for (int i4 = headIndex.level; i4 > 0; i4--) {
            arrayList.set(i4, headIndex2);
            headIndex2 = headIndex2.down;
        }
        for (Map.Entry<K, ? extends V> entry : sortedMap.entrySet()) {
            int iNextInt = ThreadLocalRandom.current().nextInt();
            int i5 = 0;
            if ((iNextInt & (-2147483647)) == 0) {
                do {
                    i5++;
                    i2 = iNextInt >>> 1;
                    iNextInt = i2;
                } while ((i2 & 1) != 0);
                if (i5 > headIndex.level) {
                    i5 = headIndex.level + 1;
                }
            }
            K key = entry.getKey();
            V value = entry.getValue();
            if (key == null || value == null) {
                throw new NullPointerException();
            }
            Node<K, V> node2 = new Node<>(key, value, null);
            node.next = node2;
            node = node2;
            if (i5 > 0) {
                Index<K, V> index = null;
                for (int i6 = 1; i6 <= i5; i6++) {
                    index = new Index<>(node2, index, null);
                    if (i6 > headIndex.level) {
                        headIndex = new HeadIndex<>(headIndex.node, headIndex, index, i6);
                    }
                    if (i6 < arrayList.size()) {
                        ((Index) arrayList.get(i6)).right = index;
                        arrayList.set(i6, index);
                    } else {
                        arrayList.add(index);
                    }
                }
            }
        }
        this.head = headIndex;
    }

    private void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
        objectOutputStream.defaultWriteObject();
        Node<K, V> nodeFindFirst = findFirst();
        while (true) {
            Node<K, V> node = nodeFindFirst;
            if (node != null) {
                V validValue = node.getValidValue();
                if (validValue != null) {
                    objectOutputStream.writeObject(node.key);
                    objectOutputStream.writeObject(validValue);
                }
                nodeFindFirst = node.next;
            } else {
                objectOutputStream.writeObject(null);
                return;
            }
        }
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        int i2;
        objectInputStream.defaultReadObject();
        initialize();
        HeadIndex<K, V> headIndex = this.head;
        Node<K, V> node = headIndex.node;
        ArrayList arrayList = new ArrayList();
        for (int i3 = 0; i3 <= headIndex.level; i3++) {
            arrayList.add(null);
        }
        HeadIndex<K, V> headIndex2 = headIndex;
        for (int i4 = headIndex.level; i4 > 0; i4--) {
            arrayList.set(i4, headIndex2);
            headIndex2 = headIndex2.down;
        }
        while (true) {
            Object object = objectInputStream.readObject();
            if (object != null) {
                Object object2 = objectInputStream.readObject();
                if (object2 == null) {
                    throw new NullPointerException();
                }
                int iNextInt = ThreadLocalRandom.current().nextInt();
                int i5 = 0;
                if ((iNextInt & (-2147483647)) == 0) {
                    do {
                        i5++;
                        i2 = iNextInt >>> 1;
                        iNextInt = i2;
                    } while ((i2 & 1) != 0);
                    if (i5 > headIndex.level) {
                        i5 = headIndex.level + 1;
                    }
                }
                Node<K, V> node2 = new Node<>(object, object2, null);
                node.next = node2;
                node = node2;
                if (i5 > 0) {
                    Index<K, V> index = null;
                    for (int i6 = 1; i6 <= i5; i6++) {
                        index = new Index<>(node2, index, null);
                        if (i6 > headIndex.level) {
                            headIndex = new HeadIndex<>(headIndex.node, headIndex, index, i6);
                        }
                        if (i6 < arrayList.size()) {
                            ((Index) arrayList.get(i6)).right = index;
                            arrayList.set(i6, index);
                        } else {
                            arrayList.add(index);
                        }
                    }
                }
            } else {
                this.head = headIndex;
                return;
            }
        }
    }

    @Override // java.util.AbstractMap, java.util.Map
    public boolean containsKey(Object obj) {
        return doGet(obj) != null;
    }

    @Override // java.util.AbstractMap, java.util.Map
    public V get(Object obj) {
        return doGet(obj);
    }

    @Override // java.util.Map
    public V getOrDefault(Object obj, V v2) {
        V vDoGet = doGet(obj);
        return vDoGet == null ? v2 : vDoGet;
    }

    @Override // java.util.AbstractMap, java.util.Map
    public V put(K k2, V v2) {
        if (v2 == null) {
            throw new NullPointerException();
        }
        return doPut(k2, v2, false);
    }

    @Override // java.util.AbstractMap, java.util.Map
    public V remove(Object obj) {
        return doRemove(obj, null);
    }

    @Override // java.util.AbstractMap, java.util.Map
    public boolean containsValue(Object obj) {
        if (obj == null) {
            throw new NullPointerException();
        }
        Node<K, V> nodeFindFirst = findFirst();
        while (true) {
            Node<K, V> node = nodeFindFirst;
            if (node != null) {
                V validValue = node.getValidValue();
                if (validValue == null || !obj.equals(validValue)) {
                    nodeFindFirst = node.next;
                } else {
                    return true;
                }
            } else {
                return false;
            }
        }
    }

    @Override // java.util.AbstractMap, java.util.Map
    public int size() {
        long j2 = 0;
        Node<K, V> nodeFindFirst = findFirst();
        while (true) {
            Node<K, V> node = nodeFindFirst;
            if (node == null) {
                break;
            }
            if (node.getValidValue() != null) {
                j2++;
            }
            nodeFindFirst = node.next;
        }
        if (j2 >= 2147483647L) {
            return Integer.MAX_VALUE;
        }
        return (int) j2;
    }

    @Override // java.util.AbstractMap, java.util.Map
    public boolean isEmpty() {
        return findFirst() == null;
    }

    @Override // java.util.AbstractMap, java.util.Map
    public void clear() {
        Node<K, V> node;
        while (true) {
            HeadIndex<K, V> headIndex = this.head;
            HeadIndex<K, V> headIndex2 = (HeadIndex) headIndex.down;
            if (headIndex2 != null) {
                casHead(headIndex, headIndex2);
            } else {
                Node<K, V> node2 = headIndex.node;
                if (node2 != null && (node = node2.next) != null) {
                    Node<K, V> node3 = node.next;
                    if (node == node2.next) {
                        Object obj = node.value;
                        if (obj == null) {
                            node.helpDelete(node2, node3);
                        } else if (node.casValue(obj, null) && node.appendMarker(node3)) {
                            node2.casNext(node, node3);
                        }
                    }
                } else {
                    return;
                }
            }
        }
    }

    @Override // java.util.Map
    public V computeIfAbsent(K k2, Function<? super K, ? extends V> function) {
        V vApply;
        if (k2 == null || function == null) {
            throw new NullPointerException();
        }
        V vDoGet = doGet(k2);
        V v2 = vDoGet;
        if (vDoGet == null && (vApply = function.apply(k2)) != null) {
            V vDoPut = doPut(k2, vApply, true);
            v2 = vDoPut == null ? vApply : vDoPut;
        }
        return v2;
    }

    @Override // java.util.Map
    public V computeIfPresent(K k2, BiFunction<? super K, ? super V, ? extends V> biFunction) {
        if (k2 == null || biFunction == null) {
            throw new NullPointerException();
        }
        while (true) {
            Node<K, V> nodeFindNode = findNode(k2);
            if (nodeFindNode != null) {
                a aVar = (Object) nodeFindNode.value;
                if (aVar != null) {
                    V vApply = biFunction.apply(k2, aVar);
                    if (vApply != null) {
                        if (nodeFindNode.casValue(aVar, vApply)) {
                            return vApply;
                        }
                    } else if (doRemove(k2, aVar) != null) {
                        return null;
                    }
                }
            } else {
                return null;
            }
        }
    }

    @Override // java.util.Map
    public V compute(K k2, BiFunction<? super K, ? super V, ? extends V> biFunction) {
        if (k2 == null || biFunction == null) {
            throw new NullPointerException();
        }
        while (true) {
            Node<K, V> nodeFindNode = findNode(k2);
            if (nodeFindNode == null) {
                V vApply = biFunction.apply(k2, null);
                if (vApply != null) {
                    if (doPut(k2, vApply, true) == null) {
                        return vApply;
                    }
                } else {
                    return null;
                }
            } else {
                a aVar = (Object) nodeFindNode.value;
                if (aVar == null) {
                    continue;
                } else {
                    V vApply2 = biFunction.apply(k2, aVar);
                    if (vApply2 != null) {
                        if (nodeFindNode.casValue(aVar, vApply2)) {
                            return vApply2;
                        }
                    } else if (doRemove(k2, aVar) != null) {
                        return null;
                    }
                }
            }
        }
    }

    @Override // java.util.Map
    public V merge(K k2, V v2, BiFunction<? super V, ? super V, ? extends V> biFunction) {
        if (k2 == null || v2 == null || biFunction == null) {
            throw new NullPointerException();
        }
        while (true) {
            Node<K, V> nodeFindNode = findNode(k2);
            if (nodeFindNode == null) {
                if (doPut(k2, v2, true) == null) {
                    return v2;
                }
            } else {
                a aVar = (Object) nodeFindNode.value;
                if (aVar == null) {
                    continue;
                } else {
                    V vApply = biFunction.apply(aVar, v2);
                    if (vApply != null) {
                        if (nodeFindNode.casValue(aVar, vApply)) {
                            return vApply;
                        }
                    } else if (doRemove(k2, aVar) != null) {
                        return null;
                    }
                }
            }
        }
    }

    @Override // java.util.AbstractMap, java.util.Map
    public NavigableSet<K> keySet() {
        KeySet<K> keySet = this.keySet;
        if (keySet != null) {
            return keySet;
        }
        KeySet<K> keySet2 = new KeySet<>(this);
        this.keySet = keySet2;
        return keySet2;
    }

    @Override // java.util.concurrent.ConcurrentNavigableMap, java.util.NavigableMap
    public NavigableSet<K> navigableKeySet() {
        KeySet<K> keySet = this.keySet;
        if (keySet != null) {
            return keySet;
        }
        KeySet<K> keySet2 = new KeySet<>(this);
        this.keySet = keySet2;
        return keySet2;
    }

    @Override // java.util.AbstractMap, java.util.Map
    public Collection<V> values() {
        Values<V> values = this.values;
        if (values != null) {
            return values;
        }
        Values<V> values2 = new Values<>(this);
        this.values = values2;
        return values2;
    }

    @Override // java.util.AbstractMap, java.util.Map
    public Set<Map.Entry<K, V>> entrySet() {
        EntrySet<K, V> entrySet = this.entrySet;
        if (entrySet != null) {
            return entrySet;
        }
        EntrySet<K, V> entrySet2 = new EntrySet<>(this);
        this.entrySet = entrySet2;
        return entrySet2;
    }

    @Override // java.util.concurrent.ConcurrentNavigableMap, java.util.NavigableMap
    public ConcurrentNavigableMap<K, V> descendingMap() {
        ConcurrentNavigableMap<K, V> concurrentNavigableMap = this.descendingMap;
        if (concurrentNavigableMap != null) {
            return concurrentNavigableMap;
        }
        SubMap subMap = new SubMap(this, null, false, null, false, true);
        this.descendingMap = subMap;
        return subMap;
    }

    @Override // java.util.concurrent.ConcurrentNavigableMap, java.util.NavigableMap
    public NavigableSet<K> descendingKeySet() {
        return descendingMap().navigableKeySet();
    }

    @Override // java.util.AbstractMap, java.util.Map
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof Map)) {
            return false;
        }
        Map map = (Map) obj;
        try {
            for (Map.Entry<K, V> entry : entrySet()) {
                if (!entry.getValue().equals(map.get(entry.getKey()))) {
                    return false;
                }
            }
            for (Map.Entry<K, V> entry2 : map.entrySet()) {
                K key = entry2.getKey();
                V value = entry2.getValue();
                if (key == null || value == null || !value.equals(get(key))) {
                    return false;
                }
            }
            return true;
        } catch (ClassCastException e2) {
            return false;
        } catch (NullPointerException e3) {
            return false;
        }
    }

    @Override // java.util.Map
    public V putIfAbsent(K k2, V v2) {
        if (v2 == null) {
            throw new NullPointerException();
        }
        return doPut(k2, v2, true);
    }

    @Override // java.util.Map
    public boolean remove(Object obj, Object obj2) {
        if (obj == null) {
            throw new NullPointerException();
        }
        return (obj2 == null || doRemove(obj, obj2) == null) ? false : true;
    }

    @Override // java.util.Map
    public boolean replace(K k2, V v2, V v3) {
        if (k2 == null || v2 == null || v3 == null) {
            throw new NullPointerException();
        }
        while (true) {
            Node<K, V> nodeFindNode = findNode(k2);
            if (nodeFindNode == null) {
                return false;
            }
            Object obj = nodeFindNode.value;
            if (obj != null) {
                if (!v2.equals(obj)) {
                    return false;
                }
                if (nodeFindNode.casValue(obj, v3)) {
                    return true;
                }
            }
        }
    }

    @Override // java.util.Map
    public V replace(K k2, V v2) {
        if (k2 == null || v2 == null) {
            throw new NullPointerException();
        }
        while (true) {
            Node<K, V> nodeFindNode = findNode(k2);
            if (nodeFindNode == null) {
                return null;
            }
            V v3 = (V) nodeFindNode.value;
            if (v3 != null && nodeFindNode.casValue(v3, v2)) {
                return v3;
            }
        }
    }

    @Override // java.util.SortedMap
    public Comparator<? super K> comparator() {
        return this.comparator;
    }

    @Override // java.util.SortedMap
    public K firstKey() {
        Node<K, V> nodeFindFirst = findFirst();
        if (nodeFindFirst == null) {
            throw new NoSuchElementException();
        }
        return nodeFindFirst.key;
    }

    @Override // java.util.SortedMap
    public K lastKey() {
        Node<K, V> nodeFindLast = findLast();
        if (nodeFindLast == null) {
            throw new NoSuchElementException();
        }
        return nodeFindLast.key;
    }

    @Override // java.util.concurrent.ConcurrentNavigableMap, java.util.NavigableMap
    public ConcurrentNavigableMap<K, V> subMap(K k2, boolean z2, K k3, boolean z3) {
        if (k2 == null || k3 == null) {
            throw new NullPointerException();
        }
        return new SubMap(this, k2, z2, k3, z3, false);
    }

    @Override // java.util.concurrent.ConcurrentNavigableMap, java.util.NavigableMap
    public ConcurrentNavigableMap<K, V> headMap(K k2, boolean z2) {
        if (k2 == null) {
            throw new NullPointerException();
        }
        return new SubMap(this, null, false, k2, z2, false);
    }

    @Override // java.util.concurrent.ConcurrentNavigableMap, java.util.NavigableMap
    public ConcurrentNavigableMap<K, V> tailMap(K k2, boolean z2) {
        if (k2 == null) {
            throw new NullPointerException();
        }
        return new SubMap(this, k2, z2, null, false, false);
    }

    @Override // java.util.concurrent.ConcurrentNavigableMap, java.util.NavigableMap, java.util.SortedMap
    public ConcurrentNavigableMap<K, V> subMap(K k2, K k3) {
        return subMap((boolean) k2, true, (boolean) k3, false);
    }

    @Override // java.util.concurrent.ConcurrentNavigableMap, java.util.NavigableMap, java.util.SortedMap
    public ConcurrentNavigableMap<K, V> headMap(K k2) {
        return headMap((ConcurrentSkipListMap<K, V>) k2, false);
    }

    @Override // java.util.concurrent.ConcurrentNavigableMap, java.util.NavigableMap, java.util.SortedMap
    public ConcurrentNavigableMap<K, V> tailMap(K k2) {
        return tailMap((ConcurrentSkipListMap<K, V>) k2, true);
    }

    @Override // java.util.NavigableMap
    public Map.Entry<K, V> lowerEntry(K k2) {
        return getNear(k2, 2);
    }

    @Override // java.util.NavigableMap
    public K lowerKey(K k2) {
        Node<K, V> nodeFindNear = findNear(k2, 2, this.comparator);
        if (nodeFindNear == null) {
            return null;
        }
        return nodeFindNear.key;
    }

    @Override // java.util.NavigableMap
    public Map.Entry<K, V> floorEntry(K k2) {
        return getNear(k2, 3);
    }

    @Override // java.util.NavigableMap
    public K floorKey(K k2) {
        Node<K, V> nodeFindNear = findNear(k2, 3, this.comparator);
        if (nodeFindNear == null) {
            return null;
        }
        return nodeFindNear.key;
    }

    @Override // java.util.NavigableMap
    public Map.Entry<K, V> ceilingEntry(K k2) {
        return getNear(k2, 1);
    }

    @Override // java.util.NavigableMap
    public K ceilingKey(K k2) {
        Node<K, V> nodeFindNear = findNear(k2, 1, this.comparator);
        if (nodeFindNear == null) {
            return null;
        }
        return nodeFindNear.key;
    }

    @Override // java.util.NavigableMap
    public Map.Entry<K, V> higherEntry(K k2) {
        return getNear(k2, 0);
    }

    @Override // java.util.NavigableMap
    public K higherKey(K k2) {
        Node<K, V> nodeFindNear = findNear(k2, 0, this.comparator);
        if (nodeFindNear == null) {
            return null;
        }
        return nodeFindNear.key;
    }

    @Override // java.util.NavigableMap
    public Map.Entry<K, V> firstEntry() {
        AbstractMap.SimpleImmutableEntry<K, V> simpleImmutableEntryCreateSnapshot;
        do {
            Node<K, V> nodeFindFirst = findFirst();
            if (nodeFindFirst == null) {
                return null;
            }
            simpleImmutableEntryCreateSnapshot = nodeFindFirst.createSnapshot();
        } while (simpleImmutableEntryCreateSnapshot == null);
        return simpleImmutableEntryCreateSnapshot;
    }

    @Override // java.util.NavigableMap
    public Map.Entry<K, V> lastEntry() {
        AbstractMap.SimpleImmutableEntry<K, V> simpleImmutableEntryCreateSnapshot;
        do {
            Node<K, V> nodeFindLast = findLast();
            if (nodeFindLast == null) {
                return null;
            }
            simpleImmutableEntryCreateSnapshot = nodeFindLast.createSnapshot();
        } while (simpleImmutableEntryCreateSnapshot == null);
        return simpleImmutableEntryCreateSnapshot;
    }

    @Override // java.util.NavigableMap
    public Map.Entry<K, V> pollFirstEntry() {
        return doRemoveFirstEntry();
    }

    @Override // java.util.NavigableMap
    public Map.Entry<K, V> pollLastEntry() {
        return doRemoveLastEntry();
    }

    /* loaded from: rt.jar:java/util/concurrent/ConcurrentSkipListMap$Iter.class */
    abstract class Iter<T> implements Iterator<T> {
        Node<K, V> lastReturned;
        Node<K, V> next;
        V nextValue;

        Iter() {
            while (true) {
                Node<K, V> nodeFindFirst = ConcurrentSkipListMap.this.findFirst();
                this.next = nodeFindFirst;
                if (nodeFindFirst != null) {
                    V v2 = (V) this.next.value;
                    if (v2 != null && v2 != this.next) {
                        this.nextValue = v2;
                        return;
                    }
                } else {
                    return;
                }
            }
        }

        @Override // java.util.Iterator
        public final boolean hasNext() {
            return this.next != null;
        }

        final void advance() {
            if (this.next == null) {
                throw new NoSuchElementException();
            }
            this.lastReturned = this.next;
            while (true) {
                Node<K, V> node = this.next.next;
                this.next = node;
                if (node != null) {
                    V v2 = (V) this.next.value;
                    if (v2 != null && v2 != this.next) {
                        this.nextValue = v2;
                        return;
                    }
                } else {
                    return;
                }
            }
        }

        @Override // java.util.Iterator
        public void remove() {
            Node<K, V> node = this.lastReturned;
            if (node == null) {
                throw new IllegalStateException();
            }
            ConcurrentSkipListMap.this.remove(node.key);
            this.lastReturned = null;
        }
    }

    /* loaded from: rt.jar:java/util/concurrent/ConcurrentSkipListMap$ValueIterator.class */
    final class ValueIterator extends ConcurrentSkipListMap<K, V>.Iter<V> {
        ValueIterator() {
            super();
        }

        @Override // java.util.Iterator
        public V next() {
            V v2 = this.nextValue;
            advance();
            return v2;
        }
    }

    /* loaded from: rt.jar:java/util/concurrent/ConcurrentSkipListMap$KeyIterator.class */
    final class KeyIterator extends ConcurrentSkipListMap<K, V>.Iter<K> {
        KeyIterator() {
            super();
        }

        @Override // java.util.Iterator
        public K next() {
            Node<K, V> node = this.next;
            advance();
            return node.key;
        }
    }

    /* loaded from: rt.jar:java/util/concurrent/ConcurrentSkipListMap$EntryIterator.class */
    final class EntryIterator extends ConcurrentSkipListMap<K, V>.Iter<Map.Entry<K, V>> {
        EntryIterator() {
            super();
        }

        @Override // java.util.Iterator
        public Map.Entry<K, V> next() {
            Node<K, V> node = this.next;
            V v2 = this.nextValue;
            advance();
            return new AbstractMap.SimpleImmutableEntry(node.key, v2);
        }
    }

    Iterator<K> keyIterator() {
        return new KeyIterator();
    }

    Iterator<V> valueIterator() {
        return new ValueIterator();
    }

    Iterator<Map.Entry<K, V>> entryIterator() {
        return new EntryIterator();
    }

    static final <E> List<E> toList(Collection<E> collection) {
        ArrayList arrayList = new ArrayList();
        Iterator<E> it = collection.iterator();
        while (it.hasNext()) {
            arrayList.add(it.next());
        }
        return arrayList;
    }

    /* loaded from: rt.jar:java/util/concurrent/ConcurrentSkipListMap$KeySet.class */
    static final class KeySet<E> extends AbstractSet<E> implements NavigableSet<E> {

        /* renamed from: m, reason: collision with root package name */
        final ConcurrentNavigableMap<E, ?> f12574m;

        /* JADX WARN: Multi-variable type inference failed */
        @Override // java.util.NavigableSet
        public /* bridge */ /* synthetic */ SortedSet tailSet(Object obj) {
            return tailSet((KeySet<E>) obj);
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // java.util.NavigableSet
        public /* bridge */ /* synthetic */ SortedSet headSet(Object obj) {
            return headSet((KeySet<E>) obj);
        }

        KeySet(ConcurrentNavigableMap<E, ?> concurrentNavigableMap) {
            this.f12574m = concurrentNavigableMap;
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public int size() {
            return this.f12574m.size();
        }

        @Override // java.util.AbstractCollection, java.util.Collection
        public boolean isEmpty() {
            return this.f12574m.isEmpty();
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean contains(Object obj) {
            return this.f12574m.containsKey(obj);
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean remove(Object obj) {
            return this.f12574m.remove(obj) != null;
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
        public void clear() {
            this.f12574m.clear();
        }

        @Override // java.util.NavigableSet
        public E lower(E e2) {
            return this.f12574m.lowerKey(e2);
        }

        @Override // java.util.NavigableSet
        public E floor(E e2) {
            return this.f12574m.floorKey(e2);
        }

        @Override // java.util.NavigableSet
        public E ceiling(E e2) {
            return this.f12574m.ceilingKey(e2);
        }

        @Override // java.util.NavigableSet
        public E higher(E e2) {
            return this.f12574m.higherKey(e2);
        }

        @Override // java.util.SortedSet
        public Comparator<? super E> comparator() {
            return this.f12574m.comparator();
        }

        @Override // java.util.SortedSet
        public E first() {
            return this.f12574m.firstKey();
        }

        @Override // java.util.SortedSet
        public E last() {
            return this.f12574m.lastKey();
        }

        @Override // java.util.NavigableSet
        public E pollFirst() {
            Map.Entry<E, ?> entryPollFirstEntry = this.f12574m.pollFirstEntry();
            if (entryPollFirstEntry == null) {
                return null;
            }
            return entryPollFirstEntry.getKey();
        }

        @Override // java.util.NavigableSet
        public E pollLast() {
            Map.Entry<E, ?> entryPollLastEntry = this.f12574m.pollLastEntry();
            if (entryPollLastEntry == null) {
                return null;
            }
            return entryPollLastEntry.getKey();
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.List
        public Iterator<E> iterator() {
            if (this.f12574m instanceof ConcurrentSkipListMap) {
                return ((ConcurrentSkipListMap) this.f12574m).keyIterator();
            }
            return ((SubMap) this.f12574m).keyIterator();
        }

        @Override // java.util.AbstractSet, java.util.Collection, java.util.List
        public boolean equals(Object obj) {
            if (obj == this) {
                return true;
            }
            if (!(obj instanceof Set)) {
                return false;
            }
            Collection<?> collection = (Collection) obj;
            try {
                if (containsAll(collection)) {
                    if (collection.containsAll(this)) {
                        return true;
                    }
                }
                return false;
            } catch (ClassCastException e2) {
                return false;
            } catch (NullPointerException e3) {
                return false;
            }
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
        public Object[] toArray() {
            return ConcurrentSkipListMap.toList(this).toArray();
        }

        @Override // java.util.AbstractCollection, java.util.Collection
        public <T> T[] toArray(T[] tArr) {
            return (T[]) ConcurrentSkipListMap.toList(this).toArray(tArr);
        }

        @Override // java.util.NavigableSet
        public Iterator<E> descendingIterator() {
            return descendingSet().iterator();
        }

        @Override // java.util.NavigableSet
        public NavigableSet<E> subSet(E e2, boolean z2, E e3, boolean z3) {
            return new KeySet(this.f12574m.subMap((boolean) e2, z2, (boolean) e3, z3));
        }

        @Override // java.util.NavigableSet
        public NavigableSet<E> headSet(E e2, boolean z2) {
            return new KeySet(this.f12574m.headMap((ConcurrentNavigableMap<E, ?>) e2, z2));
        }

        @Override // java.util.NavigableSet
        public NavigableSet<E> tailSet(E e2, boolean z2) {
            return new KeySet(this.f12574m.tailMap((ConcurrentNavigableMap<E, ?>) e2, z2));
        }

        @Override // java.util.NavigableSet
        public NavigableSet<E> subSet(E e2, E e3) {
            return subSet(e2, true, e3, false);
        }

        @Override // java.util.NavigableSet
        public NavigableSet<E> headSet(E e2) {
            return headSet(e2, false);
        }

        @Override // java.util.NavigableSet
        public NavigableSet<E> tailSet(E e2) {
            return tailSet(e2, true);
        }

        @Override // java.util.NavigableSet
        public NavigableSet<E> descendingSet() {
            return new KeySet(this.f12574m.descendingMap());
        }

        @Override // java.util.Collection, java.lang.Iterable
        public Spliterator<E> spliterator() {
            if (this.f12574m instanceof ConcurrentSkipListMap) {
                return ((ConcurrentSkipListMap) this.f12574m).keySpliterator();
            }
            return (Spliterator) ((SubMap) this.f12574m).keyIterator();
        }
    }

    /* loaded from: rt.jar:java/util/concurrent/ConcurrentSkipListMap$Values.class */
    static final class Values<E> extends AbstractCollection<E> {

        /* renamed from: m, reason: collision with root package name */
        final ConcurrentNavigableMap<?, E> f12576m;

        Values(ConcurrentNavigableMap<?, E> concurrentNavigableMap) {
            this.f12576m = concurrentNavigableMap;
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.List
        public Iterator<E> iterator() {
            if (this.f12576m instanceof ConcurrentSkipListMap) {
                return ((ConcurrentSkipListMap) this.f12576m).valueIterator();
            }
            return ((SubMap) this.f12576m).valueIterator();
        }

        @Override // java.util.AbstractCollection, java.util.Collection
        public boolean isEmpty() {
            return this.f12576m.isEmpty();
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public int size() {
            return this.f12576m.size();
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean contains(Object obj) {
            return this.f12576m.containsValue(obj);
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
        public void clear() {
            this.f12576m.clear();
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
        public Object[] toArray() {
            return ConcurrentSkipListMap.toList(this).toArray();
        }

        @Override // java.util.AbstractCollection, java.util.Collection
        public <T> T[] toArray(T[] tArr) {
            return (T[]) ConcurrentSkipListMap.toList(this).toArray(tArr);
        }

        @Override // java.util.Collection, java.lang.Iterable
        public Spliterator<E> spliterator() {
            if (this.f12576m instanceof ConcurrentSkipListMap) {
                return ((ConcurrentSkipListMap) this.f12576m).valueSpliterator();
            }
            return (Spliterator) ((SubMap) this.f12576m).valueIterator();
        }
    }

    /* loaded from: rt.jar:java/util/concurrent/ConcurrentSkipListMap$EntrySet.class */
    static final class EntrySet<K1, V1> extends AbstractSet<Map.Entry<K1, V1>> {

        /* renamed from: m, reason: collision with root package name */
        final ConcurrentNavigableMap<K1, V1> f12573m;

        EntrySet(ConcurrentNavigableMap<K1, V1> concurrentNavigableMap) {
            this.f12573m = concurrentNavigableMap;
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.List
        public Iterator<Map.Entry<K1, V1>> iterator() {
            if (this.f12573m instanceof ConcurrentSkipListMap) {
                return ((ConcurrentSkipListMap) this.f12573m).entryIterator();
            }
            return ((SubMap) this.f12573m).entryIterator();
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean contains(Object obj) {
            if (!(obj instanceof Map.Entry)) {
                return false;
            }
            Map.Entry entry = (Map.Entry) obj;
            V1 v1 = this.f12573m.get(entry.getKey());
            return v1 != null && v1.equals(entry.getValue());
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean remove(Object obj) {
            if (!(obj instanceof Map.Entry)) {
                return false;
            }
            Map.Entry entry = (Map.Entry) obj;
            return this.f12573m.remove(entry.getKey(), entry.getValue());
        }

        @Override // java.util.AbstractCollection, java.util.Collection
        public boolean isEmpty() {
            return this.f12573m.isEmpty();
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public int size() {
            return this.f12573m.size();
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
        public void clear() {
            this.f12573m.clear();
        }

        @Override // java.util.AbstractSet, java.util.Collection, java.util.List
        public boolean equals(Object obj) {
            if (obj == this) {
                return true;
            }
            if (!(obj instanceof Set)) {
                return false;
            }
            Collection<?> collection = (Collection) obj;
            try {
                if (containsAll(collection)) {
                    if (collection.containsAll(this)) {
                        return true;
                    }
                }
                return false;
            } catch (ClassCastException e2) {
                return false;
            } catch (NullPointerException e3) {
                return false;
            }
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
        public Object[] toArray() {
            return ConcurrentSkipListMap.toList(this).toArray();
        }

        @Override // java.util.AbstractCollection, java.util.Collection
        public <T> T[] toArray(T[] tArr) {
            return (T[]) ConcurrentSkipListMap.toList(this).toArray(tArr);
        }

        @Override // java.util.Collection, java.lang.Iterable
        public Spliterator<Map.Entry<K1, V1>> spliterator() {
            if (this.f12573m instanceof ConcurrentSkipListMap) {
                return ((ConcurrentSkipListMap) this.f12573m).entrySpliterator();
            }
            return (Spliterator) ((SubMap) this.f12573m).entryIterator();
        }
    }

    /* loaded from: rt.jar:java/util/concurrent/ConcurrentSkipListMap$SubMap.class */
    static final class SubMap<K, V> extends AbstractMap<K, V> implements ConcurrentNavigableMap<K, V>, Cloneable, Serializable {
        private static final long serialVersionUID = -7647078645895051609L;

        /* renamed from: m, reason: collision with root package name */
        private final ConcurrentSkipListMap<K, V> f12575m;
        private final K lo;
        private final K hi;
        private final boolean loInclusive;
        private final boolean hiInclusive;
        private final boolean isDescending;
        private transient KeySet<K> keySetView;
        private transient Set<Map.Entry<K, V>> entrySetView;
        private transient Collection<V> valuesView;

        /* JADX WARN: Multi-variable type inference failed */
        @Override // java.util.concurrent.ConcurrentNavigableMap, java.util.NavigableMap, java.util.SortedMap
        public /* bridge */ /* synthetic */ ConcurrentNavigableMap tailMap(Object obj) {
            return tailMap((SubMap<K, V>) obj);
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // java.util.concurrent.ConcurrentNavigableMap, java.util.NavigableMap, java.util.SortedMap
        public /* bridge */ /* synthetic */ ConcurrentNavigableMap headMap(Object obj) {
            return headMap((SubMap<K, V>) obj);
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // java.util.concurrent.ConcurrentNavigableMap, java.util.NavigableMap
        public /* bridge */ /* synthetic */ ConcurrentNavigableMap tailMap(Object obj, boolean z2) {
            return tailMap((SubMap<K, V>) obj, z2);
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // java.util.concurrent.ConcurrentNavigableMap, java.util.NavigableMap
        public /* bridge */ /* synthetic */ ConcurrentNavigableMap headMap(Object obj, boolean z2) {
            return headMap((SubMap<K, V>) obj, z2);
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // java.util.concurrent.ConcurrentNavigableMap, java.util.NavigableMap
        public /* bridge */ /* synthetic */ ConcurrentNavigableMap subMap(Object obj, boolean z2, Object obj2, boolean z3) {
            return subMap((boolean) obj, z2, (boolean) obj2, z3);
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // java.util.concurrent.ConcurrentNavigableMap, java.util.NavigableMap, java.util.SortedMap
        public /* bridge */ /* synthetic */ SortedMap tailMap(Object obj) {
            return tailMap((SubMap<K, V>) obj);
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // java.util.concurrent.ConcurrentNavigableMap, java.util.NavigableMap, java.util.SortedMap
        public /* bridge */ /* synthetic */ SortedMap headMap(Object obj) {
            return headMap((SubMap<K, V>) obj);
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // java.util.concurrent.ConcurrentNavigableMap, java.util.NavigableMap
        public /* bridge */ /* synthetic */ NavigableMap tailMap(Object obj, boolean z2) {
            return tailMap((SubMap<K, V>) obj, z2);
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // java.util.concurrent.ConcurrentNavigableMap, java.util.NavigableMap
        public /* bridge */ /* synthetic */ NavigableMap headMap(Object obj, boolean z2) {
            return headMap((SubMap<K, V>) obj, z2);
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // java.util.concurrent.ConcurrentNavigableMap, java.util.NavigableMap
        public /* bridge */ /* synthetic */ NavigableMap subMap(Object obj, boolean z2, Object obj2, boolean z3) {
            return subMap((boolean) obj, z2, (boolean) obj2, z3);
        }

        SubMap(ConcurrentSkipListMap<K, V> concurrentSkipListMap, K k2, boolean z2, K k3, boolean z3, boolean z4) {
            Comparator<? super K> comparator = concurrentSkipListMap.comparator;
            if (k2 != null && k3 != null && ConcurrentSkipListMap.cpr(comparator, k2, k3) > 0) {
                throw new IllegalArgumentException("inconsistent range");
            }
            this.f12575m = concurrentSkipListMap;
            this.lo = k2;
            this.hi = k3;
            this.loInclusive = z2;
            this.hiInclusive = z3;
            this.isDescending = z4;
        }

        boolean tooLow(Object obj, Comparator<? super K> comparator) {
            int iCpr;
            return this.lo != null && ((iCpr = ConcurrentSkipListMap.cpr(comparator, obj, this.lo)) < 0 || (iCpr == 0 && !this.loInclusive));
        }

        boolean tooHigh(Object obj, Comparator<? super K> comparator) {
            int iCpr;
            return this.hi != null && ((iCpr = ConcurrentSkipListMap.cpr(comparator, obj, this.hi)) > 0 || (iCpr == 0 && !this.hiInclusive));
        }

        boolean inBounds(Object obj, Comparator<? super K> comparator) {
            return (tooLow(obj, comparator) || tooHigh(obj, comparator)) ? false : true;
        }

        void checkKeyBounds(K k2, Comparator<? super K> comparator) {
            if (k2 == null) {
                throw new NullPointerException();
            }
            if (!inBounds(k2, comparator)) {
                throw new IllegalArgumentException("key out of range");
            }
        }

        boolean isBeforeEnd(Node<K, V> node, Comparator<? super K> comparator) {
            K k2;
            if (node == null) {
                return false;
            }
            if (this.hi == null || (k2 = node.key) == null) {
                return true;
            }
            int iCpr = ConcurrentSkipListMap.cpr(comparator, k2, this.hi);
            if (iCpr > 0) {
                return false;
            }
            if (iCpr == 0 && !this.hiInclusive) {
                return false;
            }
            return true;
        }

        Node<K, V> loNode(Comparator<? super K> comparator) {
            if (this.lo == null) {
                return this.f12575m.findFirst();
            }
            if (this.loInclusive) {
                return this.f12575m.findNear(this.lo, 1, comparator);
            }
            return this.f12575m.findNear(this.lo, 0, comparator);
        }

        Node<K, V> hiNode(Comparator<? super K> comparator) {
            if (this.hi == null) {
                return this.f12575m.findLast();
            }
            if (this.hiInclusive) {
                return this.f12575m.findNear(this.hi, 3, comparator);
            }
            return this.f12575m.findNear(this.hi, 2, comparator);
        }

        K lowestKey() {
            Comparator<? super K> comparator = this.f12575m.comparator;
            Node<K, V> nodeLoNode = loNode(comparator);
            if (isBeforeEnd(nodeLoNode, comparator)) {
                return nodeLoNode.key;
            }
            throw new NoSuchElementException();
        }

        K highestKey() {
            Comparator<? super K> comparator = this.f12575m.comparator;
            Node<K, V> nodeHiNode = hiNode(comparator);
            if (nodeHiNode != null) {
                K k2 = nodeHiNode.key;
                if (inBounds(k2, comparator)) {
                    return k2;
                }
            }
            throw new NoSuchElementException();
        }

        Map.Entry<K, V> lowestEntry() {
            AbstractMap.SimpleImmutableEntry<K, V> simpleImmutableEntryCreateSnapshot;
            Comparator<? super K> comparator = this.f12575m.comparator;
            do {
                Node<K, V> nodeLoNode = loNode(comparator);
                if (!isBeforeEnd(nodeLoNode, comparator)) {
                    return null;
                }
                simpleImmutableEntryCreateSnapshot = nodeLoNode.createSnapshot();
            } while (simpleImmutableEntryCreateSnapshot == null);
            return simpleImmutableEntryCreateSnapshot;
        }

        Map.Entry<K, V> highestEntry() {
            AbstractMap.SimpleImmutableEntry<K, V> simpleImmutableEntryCreateSnapshot;
            Comparator<? super K> comparator = this.f12575m.comparator;
            do {
                Node<K, V> nodeHiNode = hiNode(comparator);
                if (nodeHiNode == null || !inBounds(nodeHiNode.key, comparator)) {
                    return null;
                }
                simpleImmutableEntryCreateSnapshot = nodeHiNode.createSnapshot();
            } while (simpleImmutableEntryCreateSnapshot == null);
            return simpleImmutableEntryCreateSnapshot;
        }

        Map.Entry<K, V> removeLowest() {
            K k2;
            V vDoRemove;
            Comparator<? super K> comparator = this.f12575m.comparator;
            do {
                Node<K, V> nodeLoNode = loNode(comparator);
                if (nodeLoNode == null) {
                    return null;
                }
                k2 = nodeLoNode.key;
                if (!inBounds(k2, comparator)) {
                    return null;
                }
                vDoRemove = this.f12575m.doRemove(k2, null);
            } while (vDoRemove == null);
            return new AbstractMap.SimpleImmutableEntry(k2, vDoRemove);
        }

        Map.Entry<K, V> removeHighest() {
            K k2;
            V vDoRemove;
            Comparator<? super K> comparator = this.f12575m.comparator;
            do {
                Node<K, V> nodeHiNode = hiNode(comparator);
                if (nodeHiNode == null) {
                    return null;
                }
                k2 = nodeHiNode.key;
                if (!inBounds(k2, comparator)) {
                    return null;
                }
                vDoRemove = this.f12575m.doRemove(k2, null);
            } while (vDoRemove == null);
            return new AbstractMap.SimpleImmutableEntry(k2, vDoRemove);
        }

        Map.Entry<K, V> getNearEntry(K k2, int i2) {
            K k3;
            V validValue;
            Comparator<? super K> comparator = this.f12575m.comparator;
            if (this.isDescending) {
                if ((i2 & 2) == 0) {
                    i2 |= 2;
                } else {
                    i2 &= -3;
                }
            }
            if (tooLow(k2, comparator)) {
                if ((i2 & 2) != 0) {
                    return null;
                }
                return lowestEntry();
            }
            if (tooHigh(k2, comparator)) {
                if ((i2 & 2) != 0) {
                    return highestEntry();
                }
                return null;
            }
            do {
                Node<K, V> nodeFindNear = this.f12575m.findNear(k2, i2, comparator);
                if (nodeFindNear == null || !inBounds(nodeFindNear.key, comparator)) {
                    return null;
                }
                k3 = nodeFindNear.key;
                validValue = nodeFindNear.getValidValue();
            } while (validValue == null);
            return new AbstractMap.SimpleImmutableEntry(k3, validValue);
        }

        K getNearKey(K k2, int i2) {
            Node<K, V> nodeFindNear;
            K k3;
            Node<K, V> nodeHiNode;
            Comparator<? super K> comparator = this.f12575m.comparator;
            if (this.isDescending) {
                if ((i2 & 2) == 0) {
                    i2 |= 2;
                } else {
                    i2 &= -3;
                }
            }
            if (tooLow(k2, comparator)) {
                if ((i2 & 2) == 0) {
                    Node<K, V> nodeLoNode = loNode(comparator);
                    if (isBeforeEnd(nodeLoNode, comparator)) {
                        return nodeLoNode.key;
                    }
                    return null;
                }
                return null;
            }
            if (tooHigh(k2, comparator)) {
                if ((i2 & 2) != 0 && (nodeHiNode = hiNode(comparator)) != null) {
                    K k4 = nodeHiNode.key;
                    if (inBounds(k4, comparator)) {
                        return k4;
                    }
                    return null;
                }
                return null;
            }
            do {
                nodeFindNear = this.f12575m.findNear(k2, i2, comparator);
                if (nodeFindNear == null || !inBounds(nodeFindNear.key, comparator)) {
                    return null;
                }
                k3 = nodeFindNear.key;
            } while (nodeFindNear.getValidValue() == null);
            return k3;
        }

        @Override // java.util.AbstractMap, java.util.Map
        public boolean containsKey(Object obj) {
            if (obj == null) {
                throw new NullPointerException();
            }
            return inBounds(obj, this.f12575m.comparator) && this.f12575m.containsKey(obj);
        }

        @Override // java.util.AbstractMap, java.util.Map
        public V get(Object obj) {
            if (obj == null) {
                throw new NullPointerException();
            }
            if (inBounds(obj, this.f12575m.comparator)) {
                return this.f12575m.get(obj);
            }
            return null;
        }

        @Override // java.util.AbstractMap, java.util.Map
        public V put(K k2, V v2) {
            checkKeyBounds(k2, this.f12575m.comparator);
            return this.f12575m.put(k2, v2);
        }

        @Override // java.util.AbstractMap, java.util.Map
        public V remove(Object obj) {
            if (inBounds(obj, this.f12575m.comparator)) {
                return this.f12575m.remove(obj);
            }
            return null;
        }

        @Override // java.util.AbstractMap, java.util.Map
        public int size() {
            Comparator<? super K> comparator = this.f12575m.comparator;
            long j2 = 0;
            Node<K, V> nodeLoNode = loNode(comparator);
            while (true) {
                Node<K, V> node = nodeLoNode;
                if (!isBeforeEnd(node, comparator)) {
                    break;
                }
                if (node.getValidValue() != null) {
                    j2++;
                }
                nodeLoNode = node.next;
            }
            if (j2 >= 2147483647L) {
                return Integer.MAX_VALUE;
            }
            return (int) j2;
        }

        @Override // java.util.AbstractMap, java.util.Map
        public boolean isEmpty() {
            Comparator<? super K> comparator = this.f12575m.comparator;
            return !isBeforeEnd(loNode(comparator), comparator);
        }

        @Override // java.util.AbstractMap, java.util.Map
        public boolean containsValue(Object obj) {
            if (obj == null) {
                throw new NullPointerException();
            }
            Comparator<? super K> comparator = this.f12575m.comparator;
            Node<K, V> nodeLoNode = loNode(comparator);
            while (true) {
                Node<K, V> node = nodeLoNode;
                if (isBeforeEnd(node, comparator)) {
                    V validValue = node.getValidValue();
                    if (validValue == null || !obj.equals(validValue)) {
                        nodeLoNode = node.next;
                    } else {
                        return true;
                    }
                } else {
                    return false;
                }
            }
        }

        @Override // java.util.AbstractMap, java.util.Map
        public void clear() {
            Comparator<? super K> comparator = this.f12575m.comparator;
            Node<K, V> nodeLoNode = loNode(comparator);
            while (true) {
                Node<K, V> node = nodeLoNode;
                if (isBeforeEnd(node, comparator)) {
                    if (node.getValidValue() != null) {
                        this.f12575m.remove(node.key);
                    }
                    nodeLoNode = node.next;
                } else {
                    return;
                }
            }
        }

        @Override // java.util.Map
        public V putIfAbsent(K k2, V v2) {
            checkKeyBounds(k2, this.f12575m.comparator);
            return this.f12575m.putIfAbsent(k2, v2);
        }

        @Override // java.util.Map
        public boolean remove(Object obj, Object obj2) {
            return inBounds(obj, this.f12575m.comparator) && this.f12575m.remove(obj, obj2);
        }

        @Override // java.util.Map
        public boolean replace(K k2, V v2, V v3) {
            checkKeyBounds(k2, this.f12575m.comparator);
            return this.f12575m.replace(k2, v2, v3);
        }

        @Override // java.util.Map
        public V replace(K k2, V v2) {
            checkKeyBounds(k2, this.f12575m.comparator);
            return this.f12575m.replace(k2, v2);
        }

        @Override // java.util.SortedMap
        public Comparator<? super K> comparator() {
            Comparator<? super K> comparator = this.f12575m.comparator();
            if (this.isDescending) {
                return Collections.reverseOrder(comparator);
            }
            return comparator;
        }

        SubMap<K, V> newSubMap(K k2, boolean z2, K k3, boolean z3) {
            Comparator<? super K> comparator = this.f12575m.comparator;
            if (this.isDescending) {
                k2 = k3;
                k3 = k2;
                z2 = z3;
                z3 = z2;
            }
            if (this.lo != null) {
                if (k2 == null) {
                    k2 = this.lo;
                    z2 = this.loInclusive;
                } else {
                    int iCpr = ConcurrentSkipListMap.cpr(comparator, k2, this.lo);
                    if (iCpr < 0 || (iCpr == 0 && !this.loInclusive && z2)) {
                        throw new IllegalArgumentException("key out of range");
                    }
                }
            }
            if (this.hi != null) {
                if (k3 == null) {
                    k3 = this.hi;
                    z3 = this.hiInclusive;
                } else {
                    int iCpr2 = ConcurrentSkipListMap.cpr(comparator, k3, this.hi);
                    if (iCpr2 > 0 || (iCpr2 == 0 && !this.hiInclusive && z3)) {
                        throw new IllegalArgumentException("key out of range");
                    }
                }
            }
            return new SubMap<>(this.f12575m, k2, z2, k3, z3, this.isDescending);
        }

        @Override // java.util.concurrent.ConcurrentNavigableMap, java.util.NavigableMap
        public SubMap<K, V> subMap(K k2, boolean z2, K k3, boolean z3) {
            if (k2 == null || k3 == null) {
                throw new NullPointerException();
            }
            return newSubMap(k2, z2, k3, z3);
        }

        @Override // java.util.concurrent.ConcurrentNavigableMap, java.util.NavigableMap
        public SubMap<K, V> headMap(K k2, boolean z2) {
            if (k2 == null) {
                throw new NullPointerException();
            }
            return newSubMap(null, false, k2, z2);
        }

        @Override // java.util.concurrent.ConcurrentNavigableMap, java.util.NavigableMap
        public SubMap<K, V> tailMap(K k2, boolean z2) {
            if (k2 == null) {
                throw new NullPointerException();
            }
            return newSubMap(k2, z2, null, false);
        }

        @Override // java.util.concurrent.ConcurrentNavigableMap, java.util.NavigableMap, java.util.SortedMap
        public SubMap<K, V> subMap(K k2, K k3) {
            return subMap((boolean) k2, true, (boolean) k3, false);
        }

        @Override // java.util.concurrent.ConcurrentNavigableMap, java.util.NavigableMap, java.util.SortedMap
        public SubMap<K, V> headMap(K k2) {
            return headMap((SubMap<K, V>) k2, false);
        }

        @Override // java.util.concurrent.ConcurrentNavigableMap, java.util.NavigableMap, java.util.SortedMap
        public SubMap<K, V> tailMap(K k2) {
            return tailMap((SubMap<K, V>) k2, true);
        }

        @Override // java.util.concurrent.ConcurrentNavigableMap, java.util.NavigableMap
        public SubMap<K, V> descendingMap() {
            return new SubMap<>(this.f12575m, this.lo, this.loInclusive, this.hi, this.hiInclusive, !this.isDescending);
        }

        @Override // java.util.NavigableMap
        public Map.Entry<K, V> ceilingEntry(K k2) {
            return getNearEntry(k2, 1);
        }

        @Override // java.util.NavigableMap
        public K ceilingKey(K k2) {
            return getNearKey(k2, 1);
        }

        @Override // java.util.NavigableMap
        public Map.Entry<K, V> lowerEntry(K k2) {
            return getNearEntry(k2, 2);
        }

        @Override // java.util.NavigableMap
        public K lowerKey(K k2) {
            return getNearKey(k2, 2);
        }

        @Override // java.util.NavigableMap
        public Map.Entry<K, V> floorEntry(K k2) {
            return getNearEntry(k2, 3);
        }

        @Override // java.util.NavigableMap
        public K floorKey(K k2) {
            return getNearKey(k2, 3);
        }

        @Override // java.util.NavigableMap
        public Map.Entry<K, V> higherEntry(K k2) {
            return getNearEntry(k2, 0);
        }

        @Override // java.util.NavigableMap
        public K higherKey(K k2) {
            return getNearKey(k2, 0);
        }

        @Override // java.util.SortedMap
        public K firstKey() {
            return this.isDescending ? highestKey() : lowestKey();
        }

        @Override // java.util.SortedMap
        public K lastKey() {
            return this.isDescending ? lowestKey() : highestKey();
        }

        @Override // java.util.NavigableMap
        public Map.Entry<K, V> firstEntry() {
            return this.isDescending ? highestEntry() : lowestEntry();
        }

        @Override // java.util.NavigableMap
        public Map.Entry<K, V> lastEntry() {
            return this.isDescending ? lowestEntry() : highestEntry();
        }

        @Override // java.util.NavigableMap
        public Map.Entry<K, V> pollFirstEntry() {
            return this.isDescending ? removeHighest() : removeLowest();
        }

        @Override // java.util.NavigableMap
        public Map.Entry<K, V> pollLastEntry() {
            return this.isDescending ? removeLowest() : removeHighest();
        }

        @Override // java.util.AbstractMap, java.util.Map
        public NavigableSet<K> keySet() {
            KeySet<K> keySet = this.keySetView;
            if (keySet != null) {
                return keySet;
            }
            KeySet<K> keySet2 = new KeySet<>(this);
            this.keySetView = keySet2;
            return keySet2;
        }

        @Override // java.util.concurrent.ConcurrentNavigableMap, java.util.NavigableMap
        public NavigableSet<K> navigableKeySet() {
            KeySet<K> keySet = this.keySetView;
            if (keySet != null) {
                return keySet;
            }
            KeySet<K> keySet2 = new KeySet<>(this);
            this.keySetView = keySet2;
            return keySet2;
        }

        @Override // java.util.AbstractMap, java.util.Map
        public Collection<V> values() {
            Collection<V> collection = this.valuesView;
            if (collection != null) {
                return collection;
            }
            Values values = new Values(this);
            this.valuesView = values;
            return values;
        }

        @Override // java.util.AbstractMap, java.util.Map
        public Set<Map.Entry<K, V>> entrySet() {
            Set<Map.Entry<K, V>> set = this.entrySetView;
            if (set != null) {
                return set;
            }
            EntrySet entrySet = new EntrySet(this);
            this.entrySetView = entrySet;
            return entrySet;
        }

        @Override // java.util.concurrent.ConcurrentNavigableMap, java.util.NavigableMap
        public NavigableSet<K> descendingKeySet() {
            return descendingMap().navigableKeySet();
        }

        Iterator<K> keyIterator() {
            return new SubMapKeyIterator();
        }

        Iterator<V> valueIterator() {
            return new SubMapValueIterator();
        }

        Iterator<Map.Entry<K, V>> entryIterator() {
            return new SubMapEntryIterator();
        }

        /* loaded from: rt.jar:java/util/concurrent/ConcurrentSkipListMap$SubMap$SubMapIter.class */
        abstract class SubMapIter<T> implements Iterator<T>, Spliterator<T> {
            Node<K, V> lastReturned;
            Node<K, V> next;
            V nextValue;

            SubMapIter() {
                Comparator<? super K> comparator = SubMap.this.f12575m.comparator;
                while (true) {
                    this.next = SubMap.this.isDescending ? SubMap.this.hiNode(comparator) : SubMap.this.loNode(comparator);
                    if (this.next != null) {
                        V v2 = (V) this.next.value;
                        if (v2 != null && v2 != this.next) {
                            if (!SubMap.this.inBounds(this.next.key, comparator)) {
                                this.next = null;
                                return;
                            } else {
                                this.nextValue = v2;
                                return;
                            }
                        }
                    } else {
                        return;
                    }
                }
            }

            @Override // java.util.Iterator
            public final boolean hasNext() {
                return this.next != null;
            }

            final void advance() {
                if (this.next == null) {
                    throw new NoSuchElementException();
                }
                this.lastReturned = this.next;
                if (SubMap.this.isDescending) {
                    descend();
                } else {
                    ascend();
                }
            }

            private void ascend() {
                Comparator<? super K> comparator = SubMap.this.f12575m.comparator;
                while (true) {
                    this.next = this.next.next;
                    if (this.next != null) {
                        V v2 = (V) this.next.value;
                        if (v2 != null && v2 != this.next) {
                            if (SubMap.this.tooHigh(this.next.key, comparator)) {
                                this.next = null;
                                return;
                            } else {
                                this.nextValue = v2;
                                return;
                            }
                        }
                    } else {
                        return;
                    }
                }
            }

            private void descend() {
                Comparator<? super K> comparator = SubMap.this.f12575m.comparator;
                while (true) {
                    this.next = SubMap.this.f12575m.findNear(this.lastReturned.key, 2, comparator);
                    if (this.next != null) {
                        V v2 = (V) this.next.value;
                        if (v2 != null && v2 != this.next) {
                            if (SubMap.this.tooLow(this.next.key, comparator)) {
                                this.next = null;
                                return;
                            } else {
                                this.nextValue = v2;
                                return;
                            }
                        }
                    } else {
                        return;
                    }
                }
            }

            @Override // java.util.Iterator
            public void remove() {
                Node<K, V> node = this.lastReturned;
                if (node != null) {
                    SubMap.this.f12575m.remove(node.key);
                    this.lastReturned = null;
                    return;
                }
                throw new IllegalStateException();
            }

            @Override // java.util.Spliterator
            public Spliterator<T> trySplit() {
                return null;
            }

            @Override // java.util.Spliterator
            public boolean tryAdvance(Consumer<? super T> consumer) {
                if (hasNext()) {
                    consumer.accept(next());
                    return true;
                }
                return false;
            }

            @Override // java.util.Iterator
            public void forEachRemaining(Consumer<? super T> consumer) {
                while (hasNext()) {
                    consumer.accept(next());
                }
            }

            @Override // java.util.Spliterator
            public long estimateSize() {
                return Long.MAX_VALUE;
            }
        }

        /* loaded from: rt.jar:java/util/concurrent/ConcurrentSkipListMap$SubMap$SubMapValueIterator.class */
        final class SubMapValueIterator extends SubMap<K, V>.SubMapIter<V> {
            SubMapValueIterator() {
                super();
            }

            @Override // java.util.Iterator
            public V next() {
                V v2 = this.nextValue;
                advance();
                return v2;
            }

            @Override // java.util.Spliterator
            public int characteristics() {
                return 0;
            }
        }

        /* loaded from: rt.jar:java/util/concurrent/ConcurrentSkipListMap$SubMap$SubMapKeyIterator.class */
        final class SubMapKeyIterator extends SubMap<K, V>.SubMapIter<K> {
            SubMapKeyIterator() {
                super();
            }

            @Override // java.util.Iterator
            public K next() {
                Node<K, V> node = this.next;
                advance();
                return node.key;
            }

            @Override // java.util.Spliterator
            public int characteristics() {
                return 21;
            }

            @Override // java.util.Spliterator
            public final Comparator<? super K> getComparator() {
                return SubMap.this.comparator();
            }
        }

        /* loaded from: rt.jar:java/util/concurrent/ConcurrentSkipListMap$SubMap$SubMapEntryIterator.class */
        final class SubMapEntryIterator extends SubMap<K, V>.SubMapIter<Map.Entry<K, V>> {
            SubMapEntryIterator() {
                super();
            }

            @Override // java.util.Iterator
            public Map.Entry<K, V> next() {
                Node<K, V> node = this.next;
                V v2 = this.nextValue;
                advance();
                return new AbstractMap.SimpleImmutableEntry(node.key, v2);
            }

            @Override // java.util.Spliterator
            public int characteristics() {
                return 1;
            }
        }
    }

    @Override // java.util.Map
    public void forEach(BiConsumer<? super K, ? super V> biConsumer) {
        if (biConsumer == null) {
            throw new NullPointerException();
        }
        Node<K, V> nodeFindFirst = findFirst();
        while (true) {
            Node<K, V> node = nodeFindFirst;
            if (node != null) {
                Object validValue = node.getValidValue();
                if (validValue != null) {
                    biConsumer.accept(node.key, validValue);
                }
                nodeFindFirst = node.next;
            } else {
                return;
            }
        }
    }

    @Override // java.util.Map
    public void replaceAll(BiFunction<? super K, ? super V, ? extends V> biFunction) {
        Object validValue;
        V vApply;
        if (biFunction == null) {
            throw new NullPointerException();
        }
        Node<K, V> nodeFindFirst = findFirst();
        while (true) {
            Node<K, V> node = nodeFindFirst;
            if (node != null) {
                do {
                    validValue = node.getValidValue();
                    if (validValue == null) {
                        break;
                    }
                    vApply = biFunction.apply(node.key, validValue);
                    if (vApply == null) {
                        throw new NullPointerException();
                    }
                    nodeFindFirst = node.next;
                } while (!node.casValue(validValue, vApply));
                nodeFindFirst = node.next;
            } else {
                return;
            }
        }
    }

    /* loaded from: rt.jar:java/util/concurrent/ConcurrentSkipListMap$CSLMSpliterator.class */
    static abstract class CSLMSpliterator<K, V> {
        final Comparator<? super K> comparator;
        final K fence;
        Index<K, V> row;
        Node<K, V> current;
        int est;

        CSLMSpliterator(Comparator<? super K> comparator, Index<K, V> index, Node<K, V> node, K k2, int i2) {
            this.comparator = comparator;
            this.row = index;
            this.current = node;
            this.fence = k2;
            this.est = i2;
        }

        public final long estimateSize() {
            return this.est;
        }
    }

    /* loaded from: rt.jar:java/util/concurrent/ConcurrentSkipListMap$KeySpliterator.class */
    static final class KeySpliterator<K, V> extends CSLMSpliterator<K, V> implements Spliterator<K> {
        KeySpliterator(Comparator<? super K> comparator, Index<K, V> index, Node<K, V> node, K k2, int i2) {
            super(comparator, index, node, k2, i2);
        }

        @Override // java.util.Spliterator
        public Spliterator<K> trySplit() {
            K k2;
            Index<K, V> index;
            Index<K, V> index2;
            Node<K, V> node;
            Node<K, V> node2;
            K k3;
            Comparator<? super K> comparator = this.comparator;
            K k4 = this.fence;
            Node<K, V> node3 = this.current;
            if (node3 != null && (k2 = node3.key) != null) {
                Index<K, V> index3 = this.row;
                while (true) {
                    index = index3;
                    if (index != null) {
                        index2 = index.right;
                        if (index2 != null && (node = index2.node) != null && (node2 = node.next) != null && node2.value != null && (k3 = node2.key) != null && ConcurrentSkipListMap.cpr(comparator, k3, k2) > 0 && (k4 == null || ConcurrentSkipListMap.cpr(comparator, k3, k4) < 0)) {
                            break;
                        }
                        Index<K, V> index4 = index.down;
                        index3 = index4;
                        this.row = index4;
                    } else {
                        return null;
                    }
                }
                this.current = node2;
                Index<K, V> index5 = index.down;
                this.row = index2.right != null ? index2 : index2.down;
                this.est -= this.est >>> 2;
                return new KeySpliterator(comparator, index5, node3, k3, this.est);
            }
            return null;
        }

        @Override // java.util.Spliterator
        public void forEachRemaining(Consumer<? super K> consumer) {
            if (consumer == null) {
                throw new NullPointerException();
            }
            Comparator<? super K> comparator = this.comparator;
            K k2 = this.fence;
            this.current = null;
            for (Node<K, V> node = this.current; node != null; node = node.next) {
                K k3 = node.key;
                if (k3 == null || k2 == null || ConcurrentSkipListMap.cpr(comparator, k2, k3) > 0) {
                    Object obj = node.value;
                    if (obj != null && obj != node) {
                        consumer.accept(k3);
                    }
                } else {
                    return;
                }
            }
        }

        /* JADX WARN: Code restructure failed: missing block: B:23:0x006f, code lost:
        
            r4.current = r8;
         */
        /* JADX WARN: Code restructure failed: missing block: B:24:0x0076, code lost:
        
            return false;
         */
        @Override // java.util.Spliterator
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct code enable 'Show inconsistent code' option in preferences
        */
        public boolean tryAdvance(java.util.function.Consumer<? super K> r5) {
            /*
                r4 = this;
                r0 = r5
                if (r0 != 0) goto Lc
                java.lang.NullPointerException r0 = new java.lang.NullPointerException
                r1 = r0
                r1.<init>()
                throw r0
            Lc:
                r0 = r4
                java.util.Comparator<? super K> r0 = r0.comparator
                r6 = r0
                r0 = r4
                K r0 = r0.fence
                r7 = r0
                r0 = r4
                java.util.concurrent.ConcurrentSkipListMap$Node<K, V> r0 = r0.current
                r8 = r0
            L1c:
                r0 = r8
                if (r0 == 0) goto L6f
                r0 = r8
                K r0 = r0.key
                r1 = r0
                r9 = r1
                if (r0 == 0) goto L40
                r0 = r7
                if (r0 == 0) goto L40
                r0 = r6
                r1 = r7
                r2 = r9
                int r0 = java.util.concurrent.ConcurrentSkipListMap.cpr(r0, r1, r2)
                if (r0 > 0) goto L40
                r0 = 0
                r8 = r0
                goto L6f
            L40:
                r0 = r8
                java.lang.Object r0 = r0.value
                r1 = r0
                r10 = r1
                if (r0 == 0) goto L65
                r0 = r10
                r1 = r8
                if (r0 == r1) goto L65
                r0 = r4
                r1 = r8
                java.util.concurrent.ConcurrentSkipListMap$Node<K, V> r1 = r1.next
                r0.current = r1
                r0 = r5
                r1 = r9
                r0.accept(r1)
                r0 = 1
                return r0
            L65:
                r0 = r8
                java.util.concurrent.ConcurrentSkipListMap$Node<K, V> r0 = r0.next
                r8 = r0
                goto L1c
            L6f:
                r0 = r4
                r1 = r8
                r0.current = r1
                r0 = 0
                return r0
            */
            throw new UnsupportedOperationException("Method not decompiled: java.util.concurrent.ConcurrentSkipListMap.KeySpliterator.tryAdvance(java.util.function.Consumer):boolean");
        }

        @Override // java.util.Spliterator
        public int characteristics() {
            return 4373;
        }

        @Override // java.util.Spliterator
        public final Comparator<? super K> getComparator() {
            return this.comparator;
        }
    }

    final KeySpliterator<K, V> keySpliterator() {
        HeadIndex<K, V> headIndex;
        Node<K, V> node;
        Comparator<? super K> comparator = this.comparator;
        while (true) {
            headIndex = this.head;
            Node<K, V> node2 = headIndex.node;
            node = node2.next;
            if (node == null || node.value != null) {
                break;
            }
            node.helpDelete(node2, node.next);
        }
        return new KeySpliterator<>(comparator, headIndex, node, null, node == null ? 0 : Integer.MAX_VALUE);
    }

    /* loaded from: rt.jar:java/util/concurrent/ConcurrentSkipListMap$ValueSpliterator.class */
    static final class ValueSpliterator<K, V> extends CSLMSpliterator<K, V> implements Spliterator<V> {
        ValueSpliterator(Comparator<? super K> comparator, Index<K, V> index, Node<K, V> node, K k2, int i2) {
            super(comparator, index, node, k2, i2);
        }

        @Override // java.util.Spliterator
        public Spliterator<V> trySplit() {
            K k2;
            Index<K, V> index;
            Index<K, V> index2;
            Node<K, V> node;
            Node<K, V> node2;
            K k3;
            Comparator<? super K> comparator = this.comparator;
            K k4 = this.fence;
            Node<K, V> node3 = this.current;
            if (node3 != null && (k2 = node3.key) != null) {
                Index<K, V> index3 = this.row;
                while (true) {
                    index = index3;
                    if (index != null) {
                        index2 = index.right;
                        if (index2 != null && (node = index2.node) != null && (node2 = node.next) != null && node2.value != null && (k3 = node2.key) != null && ConcurrentSkipListMap.cpr(comparator, k3, k2) > 0 && (k4 == null || ConcurrentSkipListMap.cpr(comparator, k3, k4) < 0)) {
                            break;
                        }
                        Index<K, V> index4 = index.down;
                        index3 = index4;
                        this.row = index4;
                    } else {
                        return null;
                    }
                }
                this.current = node2;
                Index<K, V> index5 = index.down;
                this.row = index2.right != null ? index2 : index2.down;
                this.est -= this.est >>> 2;
                return new ValueSpliterator(comparator, index5, node3, k3, this.est);
            }
            return null;
        }

        @Override // java.util.Spliterator
        public void forEachRemaining(Consumer<? super V> consumer) {
            if (consumer == null) {
                throw new NullPointerException();
            }
            Comparator<? super K> comparator = this.comparator;
            K k2 = this.fence;
            this.current = null;
            for (Node<K, V> node = this.current; node != null; node = node.next) {
                K k3 = node.key;
                if (k3 == null || k2 == null || ConcurrentSkipListMap.cpr(comparator, k2, k3) > 0) {
                    Node<K, V> node2 = (Object) node.value;
                    if (node2 != null && node2 != node) {
                        consumer.accept(node2);
                    }
                } else {
                    return;
                }
            }
        }

        /* JADX WARN: Code restructure failed: missing block: B:23:0x0073, code lost:
        
            r4.current = r8;
         */
        /* JADX WARN: Code restructure failed: missing block: B:24:0x007a, code lost:
        
            return false;
         */
        @Override // java.util.Spliterator
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct code enable 'Show inconsistent code' option in preferences
        */
        public boolean tryAdvance(java.util.function.Consumer<? super V> r5) {
            /*
                r4 = this;
                r0 = r5
                if (r0 != 0) goto Lc
                java.lang.NullPointerException r0 = new java.lang.NullPointerException
                r1 = r0
                r1.<init>()
                throw r0
            Lc:
                r0 = r4
                java.util.Comparator<? super K> r0 = r0.comparator
                r6 = r0
                r0 = r4
                K r0 = r0.fence
                r7 = r0
                r0 = r4
                java.util.concurrent.ConcurrentSkipListMap$Node<K, V> r0 = r0.current
                r8 = r0
            L1c:
                r0 = r8
                if (r0 == 0) goto L73
                r0 = r8
                K r0 = r0.key
                r1 = r0
                r9 = r1
                if (r0 == 0) goto L40
                r0 = r7
                if (r0 == 0) goto L40
                r0 = r6
                r1 = r7
                r2 = r9
                int r0 = java.util.concurrent.ConcurrentSkipListMap.cpr(r0, r1, r2)
                if (r0 > 0) goto L40
                r0 = 0
                r8 = r0
                goto L73
            L40:
                r0 = r8
                java.lang.Object r0 = r0.value
                r1 = r0
                r10 = r1
                if (r0 == 0) goto L69
                r0 = r10
                r1 = r8
                if (r0 == r1) goto L69
                r0 = r4
                r1 = r8
                java.util.concurrent.ConcurrentSkipListMap$Node<K, V> r1 = r1.next
                r0.current = r1
                r0 = r10
                r11 = r0
                r0 = r5
                r1 = r11
                r0.accept(r1)
                r0 = 1
                return r0
            L69:
                r0 = r8
                java.util.concurrent.ConcurrentSkipListMap$Node<K, V> r0 = r0.next
                r8 = r0
                goto L1c
            L73:
                r0 = r4
                r1 = r8
                r0.current = r1
                r0 = 0
                return r0
            */
            throw new UnsupportedOperationException("Method not decompiled: java.util.concurrent.ConcurrentSkipListMap.ValueSpliterator.tryAdvance(java.util.function.Consumer):boolean");
        }

        @Override // java.util.Spliterator
        public int characteristics() {
            return 4368;
        }
    }

    final ValueSpliterator<K, V> valueSpliterator() {
        HeadIndex<K, V> headIndex;
        Node<K, V> node;
        Comparator<? super K> comparator = this.comparator;
        while (true) {
            headIndex = this.head;
            Node<K, V> node2 = headIndex.node;
            node = node2.next;
            if (node == null || node.value != null) {
                break;
            }
            node.helpDelete(node2, node.next);
        }
        return new ValueSpliterator<>(comparator, headIndex, node, null, node == null ? 0 : Integer.MAX_VALUE);
    }

    /* loaded from: rt.jar:java/util/concurrent/ConcurrentSkipListMap$EntrySpliterator.class */
    static final class EntrySpliterator<K, V> extends CSLMSpliterator<K, V> implements Spliterator<Map.Entry<K, V>> {
        private static /* synthetic */ Object $deserializeLambda$(SerializedLambda serializedLambda) {
            switch (serializedLambda.getImplMethodName()) {
                case "lambda$getComparator$d5a01062$1":
                    if (serializedLambda.getImplMethodKind() == 6 && serializedLambda.getFunctionalInterfaceClass().equals("java/util/Comparator") && serializedLambda.getFunctionalInterfaceMethodName().equals("compare") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;Ljava/lang/Object;)I") && serializedLambda.getImplClass().equals("java/util/concurrent/ConcurrentSkipListMap$EntrySpliterator") && serializedLambda.getImplMethodSignature().equals("(Ljava/util/Map$Entry;Ljava/util/Map$Entry;)I")) {
                        return (entry, entry2) -> {
                            return ((Comparable) entry.getKey()).compareTo(entry2.getKey());
                        };
                    }
                    break;
            }
            throw new IllegalArgumentException("Invalid lambda deserialization");
        }

        EntrySpliterator(Comparator<? super K> comparator, Index<K, V> index, Node<K, V> node, K k2, int i2) {
            super(comparator, index, node, k2, i2);
        }

        @Override // java.util.Spliterator
        public Spliterator<Map.Entry<K, V>> trySplit() {
            K k2;
            Index<K, V> index;
            Index<K, V> index2;
            Node<K, V> node;
            Node<K, V> node2;
            K k3;
            Comparator<? super K> comparator = this.comparator;
            K k4 = this.fence;
            Node<K, V> node3 = this.current;
            if (node3 != null && (k2 = node3.key) != null) {
                Index<K, V> index3 = this.row;
                while (true) {
                    index = index3;
                    if (index != null) {
                        index2 = index.right;
                        if (index2 != null && (node = index2.node) != null && (node2 = node.next) != null && node2.value != null && (k3 = node2.key) != null && ConcurrentSkipListMap.cpr(comparator, k3, k2) > 0 && (k4 == null || ConcurrentSkipListMap.cpr(comparator, k3, k4) < 0)) {
                            break;
                        }
                        Index<K, V> index4 = index.down;
                        index3 = index4;
                        this.row = index4;
                    } else {
                        return null;
                    }
                }
                this.current = node2;
                Index<K, V> index5 = index.down;
                this.row = index2.right != null ? index2 : index2.down;
                this.est -= this.est >>> 2;
                return new EntrySpliterator(comparator, index5, node3, k3, this.est);
            }
            return null;
        }

        @Override // java.util.Spliterator
        public void forEachRemaining(Consumer<? super Map.Entry<K, V>> consumer) {
            if (consumer == null) {
                throw new NullPointerException();
            }
            Comparator<? super K> comparator = this.comparator;
            K k2 = this.fence;
            this.current = null;
            for (Node<K, V> node = this.current; node != null; node = node.next) {
                K k3 = node.key;
                if (k3 == null || k2 == null || ConcurrentSkipListMap.cpr(comparator, k2, k3) > 0) {
                    Object obj = node.value;
                    if (obj != null && obj != node) {
                        consumer.accept(new AbstractMap.SimpleImmutableEntry(k3, obj));
                    }
                } else {
                    return;
                }
            }
        }

        /* JADX WARN: Code restructure failed: missing block: B:23:0x007c, code lost:
        
            r6.current = r10;
         */
        /* JADX WARN: Code restructure failed: missing block: B:24:0x0083, code lost:
        
            return false;
         */
        @Override // java.util.Spliterator
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct code enable 'Show inconsistent code' option in preferences
        */
        public boolean tryAdvance(java.util.function.Consumer<? super java.util.Map.Entry<K, V>> r7) {
            /*
                r6 = this;
                r0 = r7
                if (r0 != 0) goto Lc
                java.lang.NullPointerException r0 = new java.lang.NullPointerException
                r1 = r0
                r1.<init>()
                throw r0
            Lc:
                r0 = r6
                java.util.Comparator<? super K> r0 = r0.comparator
                r8 = r0
                r0 = r6
                K r0 = r0.fence
                r9 = r0
                r0 = r6
                java.util.concurrent.ConcurrentSkipListMap$Node<K, V> r0 = r0.current
                r10 = r0
            L1c:
                r0 = r10
                if (r0 == 0) goto L7c
                r0 = r10
                K r0 = r0.key
                r1 = r0
                r11 = r1
                if (r0 == 0) goto L40
                r0 = r9
                if (r0 == 0) goto L40
                r0 = r8
                r1 = r9
                r2 = r11
                int r0 = java.util.concurrent.ConcurrentSkipListMap.cpr(r0, r1, r2)
                if (r0 > 0) goto L40
                r0 = 0
                r10 = r0
                goto L7c
            L40:
                r0 = r10
                java.lang.Object r0 = r0.value
                r1 = r0
                r12 = r1
                if (r0 == 0) goto L72
                r0 = r12
                r1 = r10
                if (r0 == r1) goto L72
                r0 = r6
                r1 = r10
                java.util.concurrent.ConcurrentSkipListMap$Node<K, V> r1 = r1.next
                r0.current = r1
                r0 = r12
                r13 = r0
                r0 = r7
                java.util.AbstractMap$SimpleImmutableEntry r1 = new java.util.AbstractMap$SimpleImmutableEntry
                r2 = r1
                r3 = r11
                r4 = r13
                r2.<init>(r3, r4)
                r0.accept(r1)
                r0 = 1
                return r0
            L72:
                r0 = r10
                java.util.concurrent.ConcurrentSkipListMap$Node<K, V> r0 = r0.next
                r10 = r0
                goto L1c
            L7c:
                r0 = r6
                r1 = r10
                r0.current = r1
                r0 = 0
                return r0
            */
            throw new UnsupportedOperationException("Method not decompiled: java.util.concurrent.ConcurrentSkipListMap.EntrySpliterator.tryAdvance(java.util.function.Consumer):boolean");
        }

        @Override // java.util.Spliterator
        public int characteristics() {
            return 4373;
        }

        @Override // java.util.Spliterator
        public final Comparator<Map.Entry<K, V>> getComparator() {
            if (this.comparator != null) {
                return Map.Entry.comparingByKey(this.comparator);
            }
            return (Comparator) ((Serializable) (entry, entry2) -> {
                return ((Comparable) entry.getKey()).compareTo(entry2.getKey());
            });
        }
    }

    final EntrySpliterator<K, V> entrySpliterator() {
        HeadIndex<K, V> headIndex;
        Node<K, V> node;
        Comparator<? super K> comparator = this.comparator;
        while (true) {
            headIndex = this.head;
            Node<K, V> node2 = headIndex.node;
            node = node2.next;
            if (node == null || node.value != null) {
                break;
            }
            node.helpDelete(node2, node.next);
        }
        return new EntrySpliterator<>(comparator, headIndex, node, null, node == null ? 0 : Integer.MAX_VALUE);
    }
}
