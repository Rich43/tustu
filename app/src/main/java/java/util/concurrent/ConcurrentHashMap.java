package java.util.concurrent;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamField;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.AbstractMap;
import java.util.Arrays;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.Spliterator;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.LockSupport;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.DoubleBinaryOperator;
import java.util.function.Function;
import java.util.function.IntBinaryOperator;
import java.util.function.LongBinaryOperator;
import java.util.function.ToDoubleBiFunction;
import java.util.function.ToDoubleFunction;
import java.util.function.ToIntBiFunction;
import java.util.function.ToIntFunction;
import java.util.function.ToLongBiFunction;
import java.util.function.ToLongFunction;
import sun.misc.Contended;
import sun.misc.Unsafe;
import sun.security.pkcs11.wrapper.PKCS11Constants;
import sun.text.normalizer.NormalizerImpl;

/* loaded from: rt.jar:java/util/concurrent/ConcurrentHashMap.class */
public class ConcurrentHashMap<K, V> extends AbstractMap<K, V> implements ConcurrentMap<K, V>, Serializable {
    private static final long serialVersionUID = 7249069246763182397L;
    private static final int MAXIMUM_CAPACITY = 1073741824;
    private static final int DEFAULT_CAPACITY = 16;
    static final int MAX_ARRAY_SIZE = 2147483639;
    private static final int DEFAULT_CONCURRENCY_LEVEL = 16;
    private static final float LOAD_FACTOR = 0.75f;
    static final int TREEIFY_THRESHOLD = 8;
    static final int UNTREEIFY_THRESHOLD = 6;
    static final int MIN_TREEIFY_CAPACITY = 64;
    private static final int MIN_TRANSFER_STRIDE = 16;
    static final int MOVED = -1;
    static final int TREEBIN = -2;
    static final int RESERVED = -3;
    static final int HASH_BITS = Integer.MAX_VALUE;
    volatile transient Node<K, V>[] table;
    private volatile transient Node<K, V>[] nextTable;
    private volatile transient long baseCount;
    private volatile transient int sizeCtl;
    private volatile transient int transferIndex;
    private volatile transient int cellsBusy;
    private volatile transient CounterCell[] counterCells;
    private transient KeySetView<K, V> keySet;
    private transient ValuesView<K, V> values;
    private transient EntrySetView<K, V> entrySet;

    /* renamed from: U, reason: collision with root package name */
    private static final Unsafe f12571U;
    private static final long SIZECTL;
    private static final long TRANSFERINDEX;
    private static final long BASECOUNT;
    private static final long CELLSBUSY;
    private static final long CELLVALUE;
    private static final long ABASE;
    private static final int ASHIFT;
    private static int RESIZE_STAMP_BITS = 16;
    private static final int MAX_RESIZERS = (1 << (32 - RESIZE_STAMP_BITS)) - 1;
    private static final int RESIZE_STAMP_SHIFT = 32 - RESIZE_STAMP_BITS;
    static final int NCPU = Runtime.getRuntime().availableProcessors();
    private static final ObjectStreamField[] serialPersistentFields = {new ObjectStreamField("segments", Segment[].class), new ObjectStreamField("segmentMask", Integer.TYPE), new ObjectStreamField("segmentShift", Integer.TYPE)};

    static {
        try {
            f12571U = Unsafe.getUnsafe();
            SIZECTL = f12571U.objectFieldOffset(ConcurrentHashMap.class.getDeclaredField("sizeCtl"));
            TRANSFERINDEX = f12571U.objectFieldOffset(ConcurrentHashMap.class.getDeclaredField("transferIndex"));
            BASECOUNT = f12571U.objectFieldOffset(ConcurrentHashMap.class.getDeclaredField("baseCount"));
            CELLSBUSY = f12571U.objectFieldOffset(ConcurrentHashMap.class.getDeclaredField("cellsBusy"));
            CELLVALUE = f12571U.objectFieldOffset(CounterCell.class.getDeclaredField("value"));
            ABASE = f12571U.arrayBaseOffset(Node[].class);
            int iArrayIndexScale = f12571U.arrayIndexScale(Node[].class);
            if ((iArrayIndexScale & (iArrayIndexScale - 1)) != 0) {
                throw new Error("data type scale not a power of two");
            }
            ASHIFT = 31 - Integer.numberOfLeadingZeros(iArrayIndexScale);
        } catch (Exception e2) {
            throw new Error(e2);
        }
    }

    /* loaded from: rt.jar:java/util/concurrent/ConcurrentHashMap$Node.class */
    static class Node<K, V> implements Map.Entry<K, V> {
        final int hash;
        final K key;
        volatile V val;
        volatile Node<K, V> next;

        Node(int i2, K k2, V v2, Node<K, V> node) {
            this.hash = i2;
            this.key = k2;
            this.val = v2;
            this.next = node;
        }

        @Override // java.util.Map.Entry
        public final K getKey() {
            return this.key;
        }

        @Override // java.util.Map.Entry
        public final V getValue() {
            return this.val;
        }

        @Override // java.util.Map.Entry
        public final int hashCode() {
            return this.key.hashCode() ^ this.val.hashCode();
        }

        public final String toString() {
            return ((Object) this.key) + "=" + ((Object) this.val);
        }

        @Override // java.util.Map.Entry
        public final V setValue(V v2) {
            throw new UnsupportedOperationException();
        }

        @Override // java.util.Map.Entry
        public final boolean equals(Object obj) {
            Map.Entry entry;
            Object key;
            Object value;
            V v2;
            return (obj instanceof Map.Entry) && (key = (entry = (Map.Entry) obj).getKey()) != null && (value = entry.getValue()) != null && (key == this.key || key.equals(this.key)) && (value == (v2 = this.val) || value.equals(v2));
        }

        Node<K, V> find(int i2, Object obj) {
            Node<K, V> node;
            K k2;
            Node<K, V> node2 = this;
            if (obj != null) {
                do {
                    if (node2.hash == i2 && ((k2 = node2.key) == obj || (k2 != null && obj.equals(k2)))) {
                        return node2;
                    }
                    node = node2.next;
                    node2 = node;
                } while (node != null);
                return null;
            }
            return null;
        }
    }

    static final int spread(int i2) {
        return (i2 ^ (i2 >>> 16)) & Integer.MAX_VALUE;
    }

    private static final int tableSizeFor(int i2) {
        int i3 = i2 - 1;
        int i4 = i3 | (i3 >>> 1);
        int i5 = i4 | (i4 >>> 2);
        int i6 = i5 | (i5 >>> 4);
        int i7 = i6 | (i6 >>> 8);
        int i8 = i7 | (i7 >>> 16);
        if (i8 < 0) {
            return 1;
        }
        if (i8 >= 1073741824) {
            return 1073741824;
        }
        return i8 + 1;
    }

    static Class<?> comparableClassFor(Object obj) {
        Type[] actualTypeArguments;
        if (obj instanceof Comparable) {
            Class<?> cls = obj.getClass();
            if (cls == String.class) {
                return cls;
            }
            Type[] genericInterfaces = cls.getGenericInterfaces();
            if (genericInterfaces != null) {
                for (Type type : genericInterfaces) {
                    if (type instanceof ParameterizedType) {
                        ParameterizedType parameterizedType = (ParameterizedType) type;
                        if (parameterizedType.getRawType() == Comparable.class && (actualTypeArguments = parameterizedType.getActualTypeArguments()) != null && actualTypeArguments.length == 1 && actualTypeArguments[0] == cls) {
                            return cls;
                        }
                    }
                }
                return null;
            }
            return null;
        }
        return null;
    }

    static int compareComparables(Class<?> cls, Object obj, Object obj2) {
        if (obj2 == null || obj2.getClass() != cls) {
            return 0;
        }
        return ((Comparable) obj).compareTo(obj2);
    }

    static final <K, V> Node<K, V> tabAt(Node<K, V>[] nodeArr, int i2) {
        return (Node) f12571U.getObjectVolatile(nodeArr, (i2 << ASHIFT) + ABASE);
    }

    static final <K, V> boolean casTabAt(Node<K, V>[] nodeArr, int i2, Node<K, V> node, Node<K, V> node2) {
        return f12571U.compareAndSwapObject(nodeArr, (i2 << ASHIFT) + ABASE, node, node2);
    }

    static final <K, V> void setTabAt(Node<K, V>[] nodeArr, int i2, Node<K, V> node) {
        f12571U.putObjectVolatile(nodeArr, (i2 << ASHIFT) + ABASE, node);
    }

    public ConcurrentHashMap() {
    }

    public ConcurrentHashMap(int i2) {
        if (i2 < 0) {
            throw new IllegalArgumentException();
        }
        this.sizeCtl = i2 >= 536870912 ? 1073741824 : tableSizeFor(i2 + (i2 >>> 1) + 1);
    }

    public ConcurrentHashMap(Map<? extends K, ? extends V> map) {
        this.sizeCtl = 16;
        putAll(map);
    }

    public ConcurrentHashMap(int i2, float f2) {
        this(i2, f2, 1);
    }

    public ConcurrentHashMap(int i2, float f2, int i3) {
        if (f2 <= 0.0f || i2 < 0 || i3 <= 0) {
            throw new IllegalArgumentException();
        }
        long j2 = (long) (1.0d + ((i2 < i3 ? i3 : i2) / f2));
        this.sizeCtl = j2 >= PKCS11Constants.CKF_ARRAY_ATTRIBUTE ? 1073741824 : tableSizeFor((int) j2);
    }

    @Override // java.util.AbstractMap, java.util.Map
    public int size() {
        long jSumCount = sumCount();
        if (jSumCount < 0) {
            return 0;
        }
        if (jSumCount > 2147483647L) {
            return Integer.MAX_VALUE;
        }
        return (int) jSumCount;
    }

    @Override // java.util.AbstractMap, java.util.Map
    public boolean isEmpty() {
        return sumCount() <= 0;
    }

    @Override // java.util.AbstractMap, java.util.Map
    public V get(Object obj) {
        int length;
        K k2;
        int iSpread = spread(obj.hashCode());
        Node<K, V>[] nodeArr = this.table;
        if (nodeArr != null && (length = nodeArr.length) > 0) {
            Node<K, V> nodeTabAt = tabAt(nodeArr, (length - 1) & iSpread);
            Node<K, V> node = nodeTabAt;
            if (nodeTabAt != null) {
                int i2 = node.hash;
                if (i2 == iSpread) {
                    K k3 = node.key;
                    if (k3 == obj || (k3 != null && obj.equals(k3))) {
                        return node.val;
                    }
                } else if (i2 < 0) {
                    Node<K, V> nodeFind = node.find(iSpread, obj);
                    if (nodeFind != null) {
                        return nodeFind.val;
                    }
                    return null;
                }
                while (true) {
                    Node<K, V> node2 = node.next;
                    node = node2;
                    if (node2 != null) {
                        if (node.hash == iSpread && ((k2 = node.key) == obj || (k2 != null && obj.equals(k2)))) {
                            break;
                        }
                    } else {
                        return null;
                    }
                }
                return node.val;
            }
            return null;
        }
        return null;
    }

    @Override // java.util.AbstractMap, java.util.Map
    public boolean containsKey(Object obj) {
        return get(obj) != null;
    }

    @Override // java.util.AbstractMap, java.util.Map
    public boolean containsValue(Object obj) {
        if (obj == null) {
            throw new NullPointerException();
        }
        Node<K, V>[] nodeArr = this.table;
        if (nodeArr != null) {
            Traverser traverser = new Traverser(nodeArr, nodeArr.length, 0, nodeArr.length);
            while (true) {
                Node<K, V> nodeAdvance = traverser.advance();
                if (nodeAdvance != null) {
                    V v2 = nodeAdvance.val;
                    if (v2 == obj) {
                        return true;
                    }
                    if (v2 != null && obj.equals(v2)) {
                        return true;
                    }
                } else {
                    return false;
                }
            }
        } else {
            return false;
        }
    }

    @Override // java.util.AbstractMap, java.util.Map
    public V put(K k2, V v2) {
        return putVal(k2, v2, false);
    }

    /* JADX WARN: Code restructure failed: missing block: B:37:0x00c6, code lost:
    
        r21 = r23.val;
     */
    /* JADX WARN: Code restructure failed: missing block: B:38:0x00ce, code lost:
    
        if (r13 != false) goto L44;
     */
    /* JADX WARN: Code restructure failed: missing block: B:39:0x00d1, code lost:
    
        r23.val = r12;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    final V putVal(K r11, V r12, boolean r13) {
        /*
            Method dump skipped, instructions count: 362
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: java.util.concurrent.ConcurrentHashMap.putVal(java.lang.Object, java.lang.Object, boolean):java.lang.Object");
    }

    @Override // java.util.AbstractMap, java.util.Map
    public void putAll(Map<? extends K, ? extends V> map) {
        tryPresize(map.size());
        for (Map.Entry<? extends K, ? extends V> entry : map.entrySet()) {
            putVal(entry.getKey(), entry.getValue(), false);
        }
    }

    @Override // java.util.AbstractMap, java.util.Map
    public V remove(Object obj) {
        return replaceNode(obj, null, null);
    }

    final V replaceNode(Object obj, V v2, Object obj2) {
        int length;
        int i2;
        Node<K, V> nodeTabAt;
        TreeNode<K, V> treeNodeFindTreeNode;
        Node<K, V> node;
        K k2;
        int iSpread = spread(obj.hashCode());
        Node<K, V>[] nodeArrHelpTransfer = this.table;
        while (nodeArrHelpTransfer != null && (length = nodeArrHelpTransfer.length) != 0 && (nodeTabAt = tabAt(nodeArrHelpTransfer, (i2 = (length - 1) & iSpread))) != null) {
            int i3 = nodeTabAt.hash;
            if (i3 == -1) {
                nodeArrHelpTransfer = helpTransfer(nodeArrHelpTransfer, nodeTabAt);
            } else {
                V v3 = null;
                boolean z2 = false;
                synchronized (nodeTabAt) {
                    if (tabAt(nodeArrHelpTransfer, i2) == nodeTabAt) {
                        if (i3 >= 0) {
                            z2 = true;
                            Node<K, V> node2 = nodeTabAt;
                            Node<K, V> node3 = null;
                            do {
                                if (node2.hash == iSpread && ((k2 = node2.key) == obj || (k2 != null && obj.equals(k2)))) {
                                    V v4 = node2.val;
                                    if (obj2 == null || obj2 == v4 || (v4 != null && obj2.equals(v4))) {
                                        v3 = v4;
                                        if (v2 != null) {
                                            node2.val = v2;
                                        } else if (node3 != null) {
                                            node3.next = node2.next;
                                        } else {
                                            setTabAt(nodeArrHelpTransfer, i2, node2.next);
                                        }
                                    }
                                } else {
                                    node3 = node2;
                                    node = node2.next;
                                    node2 = node;
                                }
                            } while (node != null);
                        } else if (nodeTabAt instanceof TreeBin) {
                            z2 = true;
                            TreeBin treeBin = (TreeBin) nodeTabAt;
                            TreeNode<K, V> treeNode = treeBin.root;
                            if (treeNode != null && (treeNodeFindTreeNode = treeNode.findTreeNode(iSpread, obj, null)) != null) {
                                V v5 = treeNodeFindTreeNode.val;
                                if (obj2 == null || obj2 == v5 || (v5 != null && obj2.equals(v5))) {
                                    v3 = v5;
                                    if (v2 != null) {
                                        treeNodeFindTreeNode.val = v2;
                                    } else if (treeBin.removeTreeNode(treeNodeFindTreeNode)) {
                                        setTabAt(nodeArrHelpTransfer, i2, untreeify(treeBin.first));
                                    }
                                }
                            }
                        }
                    }
                }
                if (z2) {
                    if (v3 != null) {
                        if (v2 == null) {
                            addCount(-1L, -1);
                        }
                        return v3;
                    }
                    return null;
                }
            }
        }
        return null;
    }

    @Override // java.util.AbstractMap, java.util.Map
    public void clear() {
        long j2 = 0;
        int i2 = 0;
        Node<K, V>[] nodeArrHelpTransfer = this.table;
        while (nodeArrHelpTransfer != null && i2 < nodeArrHelpTransfer.length) {
            Node<K, V> nodeTabAt = tabAt(nodeArrHelpTransfer, i2);
            if (nodeTabAt == null) {
                i2++;
            } else {
                int i3 = nodeTabAt.hash;
                if (i3 == -1) {
                    nodeArrHelpTransfer = helpTransfer(nodeArrHelpTransfer, nodeTabAt);
                    i2 = 0;
                } else {
                    synchronized (nodeTabAt) {
                        if (tabAt(nodeArrHelpTransfer, i2) == nodeTabAt) {
                            for (Node<K, V> node = i3 >= 0 ? nodeTabAt : nodeTabAt instanceof TreeBin ? ((TreeBin) nodeTabAt).first : null; node != null; node = node.next) {
                                j2--;
                            }
                            int i4 = i2;
                            i2++;
                            setTabAt(nodeArrHelpTransfer, i4, null);
                        }
                    }
                }
            }
        }
        if (j2 != 0) {
            addCount(j2, -1);
        }
    }

    @Override // java.util.AbstractMap, java.util.Map
    public KeySetView<K, V> keySet() {
        KeySetView<K, V> keySetView = this.keySet;
        if (keySetView != null) {
            return keySetView;
        }
        KeySetView<K, V> keySetView2 = new KeySetView<>(this, null);
        this.keySet = keySetView2;
        return keySetView2;
    }

    @Override // java.util.AbstractMap, java.util.Map
    public Collection<V> values() {
        ValuesView<K, V> valuesView = this.values;
        if (valuesView != null) {
            return valuesView;
        }
        ValuesView<K, V> valuesView2 = new ValuesView<>(this);
        this.values = valuesView2;
        return valuesView2;
    }

    @Override // java.util.AbstractMap, java.util.Map
    public Set<Map.Entry<K, V>> entrySet() {
        EntrySetView<K, V> entrySetView = this.entrySet;
        if (entrySetView != null) {
            return entrySetView;
        }
        EntrySetView<K, V> entrySetView2 = new EntrySetView<>(this);
        this.entrySet = entrySetView2;
        return entrySetView2;
    }

    @Override // java.util.AbstractMap, java.util.Map
    public int hashCode() {
        int iHashCode = 0;
        Node<K, V>[] nodeArr = this.table;
        if (nodeArr != null) {
            Traverser traverser = new Traverser(nodeArr, nodeArr.length, 0, nodeArr.length);
            while (true) {
                Node<K, V> nodeAdvance = traverser.advance();
                if (nodeAdvance == null) {
                    break;
                }
                iHashCode += nodeAdvance.key.hashCode() ^ nodeAdvance.val.hashCode();
            }
        }
        return iHashCode;
    }

    @Override // java.util.AbstractMap
    public String toString() {
        Node<K, V>[] nodeArr = this.table;
        int length = nodeArr == null ? 0 : nodeArr.length;
        Traverser traverser = new Traverser(nodeArr, length, 0, length);
        StringBuilder sb = new StringBuilder();
        sb.append('{');
        Node<K, V> nodeAdvance = traverser.advance();
        Node<K, V> node = nodeAdvance;
        if (nodeAdvance != null) {
            while (true) {
                K k2 = node.key;
                V v2 = node.val;
                sb.append(k2 == this ? "(this Map)" : k2);
                sb.append('=');
                sb.append(v2 == this ? "(this Map)" : v2);
                Node<K, V> nodeAdvance2 = traverser.advance();
                node = nodeAdvance2;
                if (nodeAdvance2 == null) {
                    break;
                }
                sb.append(',').append(' ');
            }
        }
        return sb.append('}').toString();
    }

    @Override // java.util.AbstractMap, java.util.Map
    public boolean equals(Object obj) {
        V value;
        V v2;
        if (obj != this) {
            if (!(obj instanceof Map)) {
                return false;
            }
            Map map = (Map) obj;
            Node<K, V>[] nodeArr = this.table;
            int length = nodeArr == null ? 0 : nodeArr.length;
            Traverser traverser = new Traverser(nodeArr, length, 0, length);
            while (true) {
                Node<K, V> nodeAdvance = traverser.advance();
                if (nodeAdvance != null) {
                    V v3 = nodeAdvance.val;
                    Object obj2 = map.get(nodeAdvance.key);
                    if (obj2 == null) {
                        return false;
                    }
                    if (obj2 != v3 && !obj2.equals(v3)) {
                        return false;
                    }
                } else {
                    for (Map.Entry<K, V> entry : map.entrySet()) {
                        K key = entry.getKey();
                        if (key != null && (value = entry.getValue()) != null && (v2 = get(key)) != null) {
                            if (value != v2 && !value.equals(v2)) {
                                return false;
                            }
                        } else {
                            return false;
                        }
                    }
                    return true;
                }
            }
        } else {
            return true;
        }
    }

    /* loaded from: rt.jar:java/util/concurrent/ConcurrentHashMap$Segment.class */
    static class Segment<K, V> extends ReentrantLock implements Serializable {
        private static final long serialVersionUID = 2249069246763182397L;
        final float loadFactor;

        Segment(float f2) {
            this.loadFactor = f2;
        }
    }

    private void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
        int i2;
        int i3 = 0;
        int i4 = 1;
        while (true) {
            i2 = i4;
            if (i2 >= 16) {
                break;
            }
            i3++;
            i4 = i2 << 1;
        }
        int i5 = 32 - i3;
        int i6 = i2 - 1;
        Segment[] segmentArr = new Segment[16];
        for (int i7 = 0; i7 < segmentArr.length; i7++) {
            segmentArr[i7] = new Segment(0.75f);
        }
        objectOutputStream.putFields().put("segments", segmentArr);
        objectOutputStream.putFields().put("segmentShift", i5);
        objectOutputStream.putFields().put("segmentMask", i6);
        objectOutputStream.writeFields();
        Node<K, V>[] nodeArr = this.table;
        if (nodeArr != null) {
            Traverser traverser = new Traverser(nodeArr, nodeArr.length, 0, nodeArr.length);
            while (true) {
                Node<K, V> nodeAdvance = traverser.advance();
                if (nodeAdvance == null) {
                    break;
                }
                objectOutputStream.writeObject(nodeAdvance.key);
                objectOutputStream.writeObject(nodeAdvance.val);
            }
        }
        objectOutputStream.writeObject(null);
        objectOutputStream.writeObject(null);
    }

    /* JADX WARN: Code restructure failed: missing block: B:38:0x0119, code lost:
    
        r18 = false;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private void readObject(java.io.ObjectInputStream r9) throws java.io.IOException, java.lang.ClassNotFoundException {
        /*
            Method dump skipped, instructions count: 482
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: java.util.concurrent.ConcurrentHashMap.readObject(java.io.ObjectInputStream):void");
    }

    @Override // java.util.Map
    public V putIfAbsent(K k2, V v2) {
        return putVal(k2, v2, true);
    }

    @Override // java.util.Map
    public boolean remove(Object obj, Object obj2) {
        if (obj == null) {
            throw new NullPointerException();
        }
        return (obj2 == null || replaceNode(obj, null, obj2) == null) ? false : true;
    }

    @Override // java.util.Map
    public boolean replace(K k2, V v2, V v3) {
        if (k2 == null || v2 == null || v3 == null) {
            throw new NullPointerException();
        }
        return replaceNode(k2, v3, v2) != null;
    }

    @Override // java.util.Map
    public V replace(K k2, V v2) {
        if (k2 == null || v2 == null) {
            throw new NullPointerException();
        }
        return replaceNode(k2, v2, null);
    }

    @Override // java.util.Map
    public V getOrDefault(Object obj, V v2) {
        V v3 = get(obj);
        return v3 == null ? v2 : v3;
    }

    @Override // java.util.Map
    public void forEach(BiConsumer<? super K, ? super V> biConsumer) {
        if (biConsumer == null) {
            throw new NullPointerException();
        }
        Node<K, V>[] nodeArr = this.table;
        if (nodeArr != null) {
            Traverser traverser = new Traverser(nodeArr, nodeArr.length, 0, nodeArr.length);
            while (true) {
                Node<K, V> nodeAdvance = traverser.advance();
                if (nodeAdvance != null) {
                    biConsumer.accept(nodeAdvance.key, nodeAdvance.val);
                } else {
                    return;
                }
            }
        }
    }

    @Override // java.util.Map
    public void replaceAll(BiFunction<? super K, ? super V, ? extends V> biFunction) {
        V v2;
        if (biFunction == null) {
            throw new NullPointerException();
        }
        Node<K, V>[] nodeArr = this.table;
        if (nodeArr != null) {
            Traverser traverser = new Traverser(nodeArr, nodeArr.length, 0, nodeArr.length);
            while (true) {
                Node<K, V> nodeAdvance = traverser.advance();
                if (nodeAdvance != null) {
                    V v3 = nodeAdvance.val;
                    K k2 = nodeAdvance.key;
                    do {
                        V vApply = biFunction.apply(k2, (Object) v3);
                        if (vApply == null) {
                            throw new NullPointerException();
                        }
                        if (replaceNode(k2, vApply, v3) == null) {
                            v2 = get(k2);
                            v3 = v2;
                        }
                    } while (v2 != null);
                } else {
                    return;
                }
            }
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:60:0x011c, code lost:
    
        r12 = r21.val;
     */
    /* JADX WARN: Finally extract failed */
    @Override // java.util.Map
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public V computeIfAbsent(K r9, java.util.function.Function<? super K, ? extends V> r10) {
        /*
            Method dump skipped, instructions count: 493
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: java.util.concurrent.ConcurrentHashMap.computeIfAbsent(java.lang.Object, java.util.function.Function):java.lang.Object");
    }

    @Override // java.util.Map
    public V computeIfPresent(K k2, BiFunction<? super K, ? super V, ? extends V> biFunction) {
        int length;
        TreeNode<K, V> treeNodeFindTreeNode;
        K k3;
        if (k2 == null || biFunction == null) {
            throw new NullPointerException();
        }
        int iSpread = spread(k2.hashCode());
        V vApply = null;
        int i2 = 0;
        int i3 = 0;
        Node<K, V>[] nodeArrInitTable = this.table;
        while (true) {
            if (nodeArrInitTable == null || (length = nodeArrInitTable.length) == 0) {
                nodeArrInitTable = initTable();
            } else {
                int i4 = (length - 1) & iSpread;
                Node<K, V> nodeTabAt = tabAt(nodeArrInitTable, i4);
                if (nodeTabAt == null) {
                    break;
                }
                int i5 = nodeTabAt.hash;
                if (i5 == -1) {
                    nodeArrInitTable = helpTransfer(nodeArrInitTable, nodeTabAt);
                } else {
                    synchronized (nodeTabAt) {
                        if (tabAt(nodeArrInitTable, i4) == nodeTabAt) {
                            if (i5 >= 0) {
                                i3 = 1;
                                Node<K, V> node = nodeTabAt;
                                Node<K, V> node2 = null;
                                while (true) {
                                    if (node.hash == iSpread && ((k3 = node.key) == k2 || (k3 != null && k2.equals(k3)))) {
                                        break;
                                    }
                                    node2 = node;
                                    Node<K, V> node3 = node.next;
                                    node = node3;
                                    if (node3 == null) {
                                        break;
                                    }
                                    i3++;
                                }
                                vApply = biFunction.apply(k2, node.val);
                                if (vApply != null) {
                                    node.val = vApply;
                                } else {
                                    i2 = -1;
                                    Node<K, V> node4 = node.next;
                                    if (node2 != null) {
                                        node2.next = node4;
                                    } else {
                                        setTabAt(nodeArrInitTable, i4, node4);
                                    }
                                }
                            } else if (nodeTabAt instanceof TreeBin) {
                                i3 = 2;
                                TreeBin treeBin = (TreeBin) nodeTabAt;
                                TreeNode<K, V> treeNode = treeBin.root;
                                if (treeNode != null && (treeNodeFindTreeNode = treeNode.findTreeNode(iSpread, k2, null)) != null) {
                                    vApply = biFunction.apply(k2, treeNodeFindTreeNode.val);
                                    if (vApply != null) {
                                        treeNodeFindTreeNode.val = vApply;
                                    } else {
                                        i2 = -1;
                                        if (treeBin.removeTreeNode(treeNodeFindTreeNode)) {
                                            setTabAt(nodeArrInitTable, i4, untreeify(treeBin.first));
                                        }
                                    }
                                }
                            }
                        }
                    }
                    if (i3 != 0) {
                        break;
                    }
                }
            }
        }
        if (i2 != 0) {
            addCount(i2, i3);
        }
        return vApply;
    }

    /* JADX WARN: Finally extract failed */
    @Override // java.util.Map
    public V compute(K k2, BiFunction<? super K, ? super V, ? extends V> biFunction) {
        int length;
        TreeNode<K, V> treeNodeFindTreeNode;
        K k3;
        if (k2 == null || biFunction == null) {
            throw new NullPointerException();
        }
        int iSpread = spread(k2.hashCode());
        V vApply = null;
        int i2 = 0;
        int i3 = 0;
        Node<K, V>[] nodeArrInitTable = this.table;
        while (true) {
            if (nodeArrInitTable == null || (length = nodeArrInitTable.length) == 0) {
                nodeArrInitTable = initTable();
            } else {
                int i4 = (length - 1) & iSpread;
                Node<K, V> nodeTabAt = tabAt(nodeArrInitTable, i4);
                if (nodeTabAt == null) {
                    ReservationNode reservationNode = new ReservationNode();
                    synchronized (reservationNode) {
                        if (casTabAt(nodeArrInitTable, i4, null, reservationNode)) {
                            i3 = 1;
                            Node node = null;
                            try {
                                V vApply2 = biFunction.apply(k2, null);
                                vApply = vApply2;
                                if (vApply2 != null) {
                                    i2 = 1;
                                    node = new Node(iSpread, k2, vApply, null);
                                }
                                setTabAt(nodeArrInitTable, i4, node);
                            } catch (Throwable th) {
                                setTabAt(nodeArrInitTable, i4, null);
                                throw th;
                            }
                        }
                    }
                    if (i3 != 0) {
                        break;
                    }
                } else {
                    int i5 = nodeTabAt.hash;
                    if (i5 == -1) {
                        nodeArrInitTable = helpTransfer(nodeArrInitTable, nodeTabAt);
                    } else {
                        synchronized (nodeTabAt) {
                            if (tabAt(nodeArrInitTable, i4) == nodeTabAt) {
                                if (i5 >= 0) {
                                    i3 = 1;
                                    Node<K, V> node2 = nodeTabAt;
                                    Node<K, V> node3 = null;
                                    while (true) {
                                        if (node2.hash == iSpread && ((k3 = node2.key) == k2 || (k3 != null && k2.equals(k3)))) {
                                            break;
                                        }
                                        node3 = node2;
                                        Node<K, V> node4 = node2.next;
                                        node2 = node4;
                                        if (node4 != null) {
                                            i3++;
                                        } else {
                                            vApply = biFunction.apply(k2, null);
                                            if (vApply != null) {
                                                i2 = 1;
                                                node3.next = new Node<>(iSpread, k2, vApply, null);
                                            }
                                        }
                                    }
                                    vApply = biFunction.apply(k2, node2.val);
                                    if (vApply != null) {
                                        node2.val = vApply;
                                    } else {
                                        i2 = -1;
                                        Node<K, V> node5 = node2.next;
                                        if (node3 != null) {
                                            node3.next = node5;
                                        } else {
                                            setTabAt(nodeArrInitTable, i4, node5);
                                        }
                                    }
                                } else if (nodeTabAt instanceof TreeBin) {
                                    i3 = 1;
                                    TreeBin treeBin = (TreeBin) nodeTabAt;
                                    TreeNode<K, V> treeNode = treeBin.root;
                                    if (treeNode != null) {
                                        treeNodeFindTreeNode = treeNode.findTreeNode(iSpread, k2, null);
                                    } else {
                                        treeNodeFindTreeNode = null;
                                    }
                                    vApply = biFunction.apply(k2, (Object) (treeNodeFindTreeNode == null ? null : treeNodeFindTreeNode.val));
                                    if (vApply != null) {
                                        if (treeNodeFindTreeNode != null) {
                                            treeNodeFindTreeNode.val = vApply;
                                        } else {
                                            i2 = 1;
                                            treeBin.putTreeVal(iSpread, k2, vApply);
                                        }
                                    } else if (treeNodeFindTreeNode != null) {
                                        i2 = -1;
                                        if (treeBin.removeTreeNode(treeNodeFindTreeNode)) {
                                            setTabAt(nodeArrInitTable, i4, untreeify(treeBin.first));
                                        }
                                    }
                                }
                            }
                        }
                        if (i3 != 0) {
                            if (i3 >= 8) {
                                treeifyBin(nodeArrInitTable, i4);
                            }
                        }
                    }
                }
            }
        }
        if (i2 != 0) {
            addCount(i2, i3);
        }
        return vApply;
    }

    @Override // java.util.Map
    public V merge(K k2, V v2, BiFunction<? super V, ? super V, ? extends V> biFunction) {
        int length;
        K k3;
        if (k2 == null || v2 == null || biFunction == null) {
            throw new NullPointerException();
        }
        int iSpread = spread(k2.hashCode());
        V vApply = null;
        int i2 = 0;
        int i3 = 0;
        Node<K, V>[] nodeArrInitTable = this.table;
        while (true) {
            if (nodeArrInitTable == null || (length = nodeArrInitTable.length) == 0) {
                nodeArrInitTable = initTable();
            } else {
                int i4 = (length - 1) & iSpread;
                Node<K, V> nodeTabAt = tabAt(nodeArrInitTable, i4);
                if (nodeTabAt == null) {
                    if (casTabAt(nodeArrInitTable, i4, null, new Node(iSpread, k2, v2, null))) {
                        i2 = 1;
                        vApply = v2;
                        break;
                    }
                } else {
                    int i5 = nodeTabAt.hash;
                    if (i5 == -1) {
                        nodeArrInitTable = helpTransfer(nodeArrInitTable, nodeTabAt);
                    } else {
                        synchronized (nodeTabAt) {
                            if (tabAt(nodeArrInitTable, i4) == nodeTabAt) {
                                if (i5 >= 0) {
                                    i3 = 1;
                                    Node<K, V> node = nodeTabAt;
                                    Node<K, V> node2 = null;
                                    while (true) {
                                        if (node.hash == iSpread && ((k3 = node.key) == k2 || (k3 != null && k2.equals(k3)))) {
                                            break;
                                        }
                                        node2 = node;
                                        Node<K, V> node3 = node.next;
                                        node = node3;
                                        if (node3 != null) {
                                            i3++;
                                        } else {
                                            i2 = 1;
                                            vApply = v2;
                                            node2.next = new Node<>(iSpread, k2, vApply, null);
                                            break;
                                        }
                                    }
                                    vApply = biFunction.apply(node.val, v2);
                                    if (vApply != null) {
                                        node.val = vApply;
                                    } else {
                                        i2 = -1;
                                        Node<K, V> node4 = node.next;
                                        if (node2 != null) {
                                            node2.next = node4;
                                        } else {
                                            setTabAt(nodeArrInitTable, i4, node4);
                                        }
                                    }
                                } else if (nodeTabAt instanceof TreeBin) {
                                    i3 = 2;
                                    TreeBin treeBin = (TreeBin) nodeTabAt;
                                    TreeNode<K, V> treeNode = treeBin.root;
                                    TreeNode<K, V> treeNodeFindTreeNode = treeNode == null ? null : treeNode.findTreeNode(iSpread, k2, null);
                                    vApply = treeNodeFindTreeNode == null ? v2 : biFunction.apply(treeNodeFindTreeNode.val, v2);
                                    if (vApply != null) {
                                        if (treeNodeFindTreeNode != null) {
                                            treeNodeFindTreeNode.val = vApply;
                                        } else {
                                            i2 = 1;
                                            treeBin.putTreeVal(iSpread, k2, vApply);
                                        }
                                    } else if (treeNodeFindTreeNode != null) {
                                        i2 = -1;
                                        if (treeBin.removeTreeNode(treeNodeFindTreeNode)) {
                                            setTabAt(nodeArrInitTable, i4, untreeify(treeBin.first));
                                        }
                                    }
                                }
                            }
                        }
                        if (i3 != 0) {
                            if (i3 >= 8) {
                                treeifyBin(nodeArrInitTable, i4);
                            }
                        }
                    }
                }
            }
        }
        if (i2 != 0) {
            addCount(i2, i3);
        }
        return vApply;
    }

    public boolean contains(Object obj) {
        return containsValue(obj);
    }

    public Enumeration<K> keys() {
        Node<K, V>[] nodeArr = this.table;
        int length = nodeArr == null ? 0 : nodeArr.length;
        return new KeyIterator(nodeArr, length, 0, length, this);
    }

    public Enumeration<V> elements() {
        Node<K, V>[] nodeArr = this.table;
        int length = nodeArr == null ? 0 : nodeArr.length;
        return new ValueIterator(nodeArr, length, 0, length, this);
    }

    public long mappingCount() {
        long jSumCount = sumCount();
        if (jSumCount < 0) {
            return 0L;
        }
        return jSumCount;
    }

    public static <K> KeySetView<K, Boolean> newKeySet() {
        return new KeySetView<>(new ConcurrentHashMap(), Boolean.TRUE);
    }

    public static <K> KeySetView<K, Boolean> newKeySet(int i2) {
        return new KeySetView<>(new ConcurrentHashMap(i2), Boolean.TRUE);
    }

    public KeySetView<K, V> keySet(V v2) {
        if (v2 == null) {
            throw new NullPointerException();
        }
        return new KeySetView<>(this, v2);
    }

    /* loaded from: rt.jar:java/util/concurrent/ConcurrentHashMap$ForwardingNode.class */
    static final class ForwardingNode<K, V> extends Node<K, V> {
        final Node<K, V>[] nextTable;

        ForwardingNode(Node<K, V>[] nodeArr) {
            super(-1, null, null, null);
            this.nextTable = nodeArr;
        }

        /* JADX WARN: Code restructure failed: missing block: B:22:0x004f, code lost:
        
            return r8;
         */
        @Override // java.util.concurrent.ConcurrentHashMap.Node
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct code enable 'Show inconsistent code' option in preferences
        */
        java.util.concurrent.ConcurrentHashMap.Node<K, V> find(int r5, java.lang.Object r6) {
            /*
                r4 = this;
                r0 = r4
                java.util.concurrent.ConcurrentHashMap$Node<K, V>[] r0 = r0.nextTable
                r7 = r0
            L5:
                r0 = r6
                if (r0 == 0) goto L25
                r0 = r7
                if (r0 == 0) goto L25
                r0 = r7
                int r0 = r0.length
                r1 = r0
                r9 = r1
                if (r0 == 0) goto L25
                r0 = r7
                r1 = r9
                r2 = 1
                int r1 = r1 - r2
                r2 = r5
                r1 = r1 & r2
                java.util.concurrent.ConcurrentHashMap$Node r0 = java.util.concurrent.ConcurrentHashMap.tabAt(r0, r1)
                r1 = r0
                r8 = r1
                if (r0 != 0) goto L27
            L25:
                r0 = 0
                return r0
            L27:
                r0 = r8
                int r0 = r0.hash
                r1 = r0
                r10 = r1
                r1 = r5
                if (r0 != r1) goto L50
                r0 = r8
                K r0 = r0.key
                r1 = r0
                r11 = r1
                r1 = r6
                if (r0 == r1) goto L4d
                r0 = r11
                if (r0 == 0) goto L50
                r0 = r6
                r1 = r11
                boolean r0 = r0.equals(r1)
                if (r0 == 0) goto L50
            L4d:
                r0 = r8
                return r0
            L50:
                r0 = r10
                if (r0 >= 0) goto L71
                r0 = r8
                boolean r0 = r0 instanceof java.util.concurrent.ConcurrentHashMap.ForwardingNode
                if (r0 == 0) goto L69
                r0 = r8
                java.util.concurrent.ConcurrentHashMap$ForwardingNode r0 = (java.util.concurrent.ConcurrentHashMap.ForwardingNode) r0
                java.util.concurrent.ConcurrentHashMap$Node<K, V>[] r0 = r0.nextTable
                r7 = r0
                goto L5
            L69:
                r0 = r8
                r1 = r5
                r2 = r6
                java.util.concurrent.ConcurrentHashMap$Node r0 = r0.find(r1, r2)
                return r0
            L71:
                r0 = r8
                java.util.concurrent.ConcurrentHashMap$Node<K, V> r0 = r0.next
                r1 = r0
                r8 = r1
                if (r0 != 0) goto L7e
                r0 = 0
                return r0
            L7e:
                goto L27
            */
            throw new UnsupportedOperationException("Method not decompiled: java.util.concurrent.ConcurrentHashMap.ForwardingNode.find(int, java.lang.Object):java.util.concurrent.ConcurrentHashMap$Node");
        }
    }

    /* loaded from: rt.jar:java/util/concurrent/ConcurrentHashMap$ReservationNode.class */
    static final class ReservationNode<K, V> extends Node<K, V> {
        ReservationNode() {
            super(-3, null, null, null);
        }

        @Override // java.util.concurrent.ConcurrentHashMap.Node
        Node<K, V> find(int i2, Object obj) {
            return null;
        }
    }

    static final int resizeStamp(int i2) {
        return Integer.numberOfLeadingZeros(i2) | (1 << (RESIZE_STAMP_BITS - 1));
    }

    /* JADX WARN: Code restructure failed: missing block: B:28:0x0072, code lost:
    
        return r8;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private final java.util.concurrent.ConcurrentHashMap.Node<K, V>[] initTable() {
        /*
            r7 = this;
        L0:
            r0 = r7
            java.util.concurrent.ConcurrentHashMap$Node<K, V>[] r0 = r0.table
            r1 = r0
            r8 = r1
            if (r0 == 0) goto Le
            r0 = r8
            int r0 = r0.length
            if (r0 != 0) goto L71
        Le:
            r0 = r7
            int r0 = r0.sizeCtl
            r1 = r0
            r9 = r1
            if (r0 >= 0) goto L1d
            java.lang.Thread.yield()
            goto L0
        L1d:
            sun.misc.Unsafe r0 = java.util.concurrent.ConcurrentHashMap.f12571U
            r1 = r7
            long r2 = java.util.concurrent.ConcurrentHashMap.SIZECTL
            r3 = r9
            r4 = -1
            boolean r0 = r0.compareAndSwapInt(r1, r2, r3, r4)
            if (r0 == 0) goto L0
            r0 = r7
            java.util.concurrent.ConcurrentHashMap$Node<K, V>[] r0 = r0.table     // Catch: java.lang.Throwable -> L64
            r1 = r0
            r8 = r1
            if (r0 == 0) goto L3a
            r0 = r8
            int r0 = r0.length     // Catch: java.lang.Throwable -> L64
            if (r0 != 0) goto L5c
        L3a:
            r0 = r9
            if (r0 <= 0) goto L42
            r0 = r9
            goto L44
        L42:
            r0 = 16
        L44:
            r10 = r0
            r0 = r10
            java.util.concurrent.ConcurrentHashMap$Node[] r0 = new java.util.concurrent.ConcurrentHashMap.Node[r0]     // Catch: java.lang.Throwable -> L64
            java.util.concurrent.ConcurrentHashMap$Node[] r0 = (java.util.concurrent.ConcurrentHashMap.Node[]) r0     // Catch: java.lang.Throwable -> L64
            r11 = r0
            r0 = r7
            r1 = r11
            r2 = r1
            r8 = r2
            r0.table = r1     // Catch: java.lang.Throwable -> L64
            r0 = r10
            r1 = r10
            r2 = 2
            int r1 = r1 >>> r2
            int r0 = r0 - r1
            r9 = r0
        L5c:
            r0 = r7
            r1 = r9
            r0.sizeCtl = r1
            goto L6e
        L64:
            r12 = move-exception
            r0 = r7
            r1 = r9
            r0.sizeCtl = r1
            r0 = r12
            throw r0
        L6e:
            goto L71
        L71:
            r0 = r8
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: java.util.concurrent.ConcurrentHashMap.initTable():java.util.concurrent.ConcurrentHashMap$Node[]");
    }

    /* JADX WARN: Removed duplicated region for block: B:6:0x0025  */
    /* JADX WARN: Type inference failed for: r0v40, types: [sun.misc.Unsafe] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private final void addCount(long r12, int r14) {
        /*
            Method dump skipped, instructions count: 281
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: java.util.concurrent.ConcurrentHashMap.addCount(long, int):void");
    }

    final Node<K, V>[] helpTransfer(Node<K, V>[] nodeArr, Node<K, V> node) {
        Node<K, V>[] nodeArr2;
        int i2;
        if (nodeArr != null && (node instanceof ForwardingNode) && (nodeArr2 = ((ForwardingNode) node).nextTable) != null) {
            int iResizeStamp = resizeStamp(nodeArr.length) << RESIZE_STAMP_SHIFT;
            while (true) {
                if (nodeArr2 != this.nextTable || this.table != nodeArr || (i2 = this.sizeCtl) >= 0 || i2 == iResizeStamp + MAX_RESIZERS || i2 == iResizeStamp + 1 || this.transferIndex <= 0) {
                    break;
                }
                if (f12571U.compareAndSwapInt(this, SIZECTL, i2, i2 + 1)) {
                    transfer(nodeArr, nodeArr2);
                    break;
                }
            }
            return nodeArr2;
        }
        return this.table;
    }

    private final void tryPresize(int i2) {
        int length;
        Node<K, V>[] nodeArr;
        int iTableSizeFor = i2 >= 536870912 ? 1073741824 : tableSizeFor(i2 + (i2 >>> 1) + 1);
        while (true) {
            int i3 = this.sizeCtl;
            int i4 = i3;
            if (i3 >= 0) {
                Node<K, V>[] nodeArr2 = this.table;
                if (nodeArr2 == null || (length = nodeArr2.length) == 0) {
                    int i5 = i4 > iTableSizeFor ? i4 : iTableSizeFor;
                    if (f12571U.compareAndSwapInt(this, SIZECTL, i4, -1)) {
                        try {
                            if (this.table == nodeArr2) {
                                this.table = new Node[i5];
                                i4 = i5 - (i5 >>> 2);
                            }
                            this.sizeCtl = i4;
                        } catch (Throwable th) {
                            this.sizeCtl = i4;
                            throw th;
                        }
                    } else {
                        continue;
                    }
                } else if (iTableSizeFor > i4 && length < 1073741824) {
                    if (nodeArr2 == this.table) {
                        int iResizeStamp = resizeStamp(length);
                        if (i4 < 0) {
                            if ((i4 >>> RESIZE_STAMP_SHIFT) == iResizeStamp && i4 != iResizeStamp + 1 && i4 != iResizeStamp + MAX_RESIZERS && (nodeArr = this.nextTable) != null && this.transferIndex > 0) {
                                if (f12571U.compareAndSwapInt(this, SIZECTL, i4, i4 + 1)) {
                                    transfer(nodeArr2, nodeArr);
                                }
                            } else {
                                return;
                            }
                        } else if (f12571U.compareAndSwapInt(this, SIZECTL, i4, (iResizeStamp << RESIZE_STAMP_SHIFT) + 2)) {
                            transfer(nodeArr2, null);
                        }
                    } else {
                        continue;
                    }
                } else {
                    return;
                }
            } else {
                return;
            }
        }
    }

    private final void transfer(Node<K, V>[] nodeArr, Node<K, V>[] nodeArr2) {
        Node<K, V> node;
        Node<K, V> node2;
        int length = nodeArr.length;
        int i2 = NCPU > 1 ? (length >>> 3) / NCPU : length;
        int i3 = i2;
        if (i2 < 16) {
            i3 = 16;
        }
        if (nodeArr2 == null) {
            try {
                nodeArr2 = new Node[length << 1];
                this.nextTable = nodeArr2;
                this.transferIndex = length;
            } catch (Throwable th) {
                this.sizeCtl = Integer.MAX_VALUE;
                return;
            }
        }
        int length2 = nodeArr2.length;
        ForwardingNode forwardingNode = new ForwardingNode(nodeArr2);
        boolean zCasTabAt = true;
        boolean z2 = false;
        int i4 = 0;
        int i5 = 0;
        while (true) {
            if (zCasTabAt) {
                i4--;
                if (i4 >= i5 || z2) {
                    zCasTabAt = false;
                } else {
                    int i6 = this.transferIndex;
                    if (i6 <= 0) {
                        i4 = -1;
                        zCasTabAt = false;
                    } else {
                        Unsafe unsafe = f12571U;
                        long j2 = TRANSFERINDEX;
                        int i7 = i6 > i3 ? i6 - i3 : 0;
                        int i8 = i7;
                        if (unsafe.compareAndSwapInt(this, j2, i6, i7)) {
                            i5 = i8;
                            i4 = i6 - 1;
                            zCasTabAt = false;
                        }
                    }
                }
            } else if (i4 < 0 || i4 >= length || i4 + length >= length2) {
                if (z2) {
                    this.nextTable = null;
                    this.table = nodeArr2;
                    this.sizeCtl = (length << 1) - (length >>> 1);
                    return;
                }
                Unsafe unsafe2 = f12571U;
                long j3 = SIZECTL;
                int i9 = this.sizeCtl;
                if (!unsafe2.compareAndSwapInt(this, j3, i9, i9 - 1)) {
                    continue;
                } else {
                    if (i9 - 2 != (resizeStamp(length) << RESIZE_STAMP_SHIFT)) {
                        return;
                    }
                    zCasTabAt = true;
                    z2 = true;
                    i4 = length;
                }
            } else {
                Node<K, V> nodeTabAt = tabAt(nodeArr, i4);
                if (nodeTabAt == null) {
                    zCasTabAt = casTabAt(nodeArr, i4, null, forwardingNode);
                } else {
                    int i10 = nodeTabAt.hash;
                    if (i10 == -1) {
                        zCasTabAt = true;
                    } else {
                        synchronized (nodeTabAt) {
                            if (tabAt(nodeArr, i4) == nodeTabAt) {
                                if (i10 >= 0) {
                                    int i11 = i10 & length;
                                    Node<K, V> node3 = nodeTabAt;
                                    for (Node<K, V> node4 = nodeTabAt.next; node4 != null; node4 = node4.next) {
                                        int i12 = node4.hash & length;
                                        if (i12 != i11) {
                                            i11 = i12;
                                            node3 = node4;
                                        }
                                    }
                                    if (i11 == 0) {
                                        node2 = node3;
                                        node = null;
                                    } else {
                                        node = node3;
                                        node2 = null;
                                    }
                                    for (Node<K, V> node5 = nodeTabAt; node5 != node3; node5 = node5.next) {
                                        int i13 = node5.hash;
                                        K k2 = node5.key;
                                        V v2 = node5.val;
                                        if ((i13 & length) == 0) {
                                            node2 = new Node<>(i13, k2, v2, node2);
                                        } else {
                                            node = new Node<>(i13, k2, v2, node);
                                        }
                                    }
                                    setTabAt(nodeArr2, i4, node2);
                                    setTabAt(nodeArr2, i4 + length, node);
                                    setTabAt(nodeArr, i4, forwardingNode);
                                    zCasTabAt = true;
                                } else if (nodeTabAt instanceof TreeBin) {
                                    TreeBin treeBin = (TreeBin) nodeTabAt;
                                    TreeNode<K, V> treeNode = null;
                                    TreeNode<K, V> treeNode2 = null;
                                    TreeNode<K, V> treeNode3 = null;
                                    TreeNode<K, V> treeNode4 = null;
                                    int i14 = 0;
                                    int i15 = 0;
                                    for (TreeNode<K, V> treeNode5 = treeBin.first; treeNode5 != null; treeNode5 = treeNode5.next) {
                                        int i16 = treeNode5.hash;
                                        TreeNode<K, V> treeNode6 = new TreeNode<>(i16, treeNode5.key, treeNode5.val, null, null);
                                        if ((i16 & length) == 0) {
                                            TreeNode<K, V> treeNode7 = treeNode2;
                                            treeNode6.prev = treeNode7;
                                            if (treeNode7 == null) {
                                                treeNode = treeNode6;
                                            } else {
                                                treeNode2.next = treeNode6;
                                            }
                                            treeNode2 = treeNode6;
                                            i14++;
                                        } else {
                                            TreeNode<K, V> treeNode8 = treeNode4;
                                            treeNode6.prev = treeNode8;
                                            if (treeNode8 == null) {
                                                treeNode3 = treeNode6;
                                            } else {
                                                treeNode4.next = treeNode6;
                                            }
                                            treeNode4 = treeNode6;
                                            i15++;
                                        }
                                    }
                                    Node nodeUntreeify = i14 <= 6 ? untreeify(treeNode) : i15 != 0 ? new TreeBin(treeNode) : treeBin;
                                    Node nodeUntreeify2 = i15 <= 6 ? untreeify(treeNode3) : i14 != 0 ? new TreeBin(treeNode3) : treeBin;
                                    setTabAt(nodeArr2, i4, nodeUntreeify);
                                    setTabAt(nodeArr2, i4 + length, nodeUntreeify2);
                                    setTabAt(nodeArr, i4, forwardingNode);
                                    zCasTabAt = true;
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @Contended
    /* loaded from: rt.jar:java/util/concurrent/ConcurrentHashMap$CounterCell.class */
    static final class CounterCell {
        volatile long value;

        CounterCell(long j2) {
            this.value = j2;
        }
    }

    final long sumCount() {
        CounterCell[] counterCellArr = this.counterCells;
        long j2 = this.baseCount;
        if (counterCellArr != null) {
            for (CounterCell counterCell : counterCellArr) {
                if (counterCell != null) {
                    j2 += counterCell.value;
                }
            }
        }
        return j2;
    }

    private final void fullAddCount(long j2, boolean z2) {
        int length;
        int length2;
        int probe = ThreadLocalRandom.getProbe();
        int iAdvanceProbe = probe;
        if (probe == 0) {
            ThreadLocalRandom.localInit();
            iAdvanceProbe = ThreadLocalRandom.getProbe();
            z2 = true;
        }
        boolean z3 = false;
        while (true) {
            CounterCell[] counterCellArr = this.counterCells;
            if (counterCellArr != null && (length = counterCellArr.length) > 0) {
                CounterCell counterCell = counterCellArr[(length - 1) & iAdvanceProbe];
                if (counterCell == null) {
                    if (this.cellsBusy == 0) {
                        CounterCell counterCell2 = new CounterCell(j2);
                        if (this.cellsBusy == 0 && f12571U.compareAndSwapInt(this, CELLSBUSY, 0, 1)) {
                            boolean z4 = false;
                            try {
                                CounterCell[] counterCellArr2 = this.counterCells;
                                if (counterCellArr2 != null && (length2 = counterCellArr2.length) > 0) {
                                    int i2 = (length2 - 1) & iAdvanceProbe;
                                    if (counterCellArr2[i2] == null) {
                                        counterCellArr2[i2] = counterCell2;
                                        z4 = true;
                                    }
                                }
                                if (z4) {
                                    return;
                                }
                            } finally {
                                this.cellsBusy = 0;
                            }
                        }
                    }
                    z3 = false;
                    iAdvanceProbe = ThreadLocalRandom.advanceProbe(iAdvanceProbe);
                } else {
                    if (!z2) {
                        z2 = true;
                    } else {
                        Unsafe unsafe = f12571U;
                        long j3 = CELLVALUE;
                        long j4 = counterCell.value;
                        if (!unsafe.compareAndSwapLong(unsafe, j3, j4, j4 + j2)) {
                            if (this.counterCells != counterCellArr || length >= NCPU) {
                                z3 = false;
                            } else if (!z3) {
                                z3 = true;
                            } else if (this.cellsBusy == 0 && f12571U.compareAndSwapInt(this, CELLSBUSY, 0, 1)) {
                                try {
                                    if (this.counterCells == counterCellArr) {
                                        CounterCell[] counterCellArr3 = new CounterCell[length << 1];
                                        for (int i3 = 0; i3 < length; i3++) {
                                            counterCellArr3[i3] = counterCellArr[i3];
                                        }
                                        this.counterCells = counterCellArr3;
                                    }
                                    this.cellsBusy = 0;
                                    z3 = false;
                                } finally {
                                    this.cellsBusy = 0;
                                }
                            }
                        } else {
                            return;
                        }
                    }
                    iAdvanceProbe = ThreadLocalRandom.advanceProbe(iAdvanceProbe);
                }
            } else if (this.cellsBusy == 0 && this.counterCells == counterCellArr && f12571U.compareAndSwapInt(this, CELLSBUSY, 0, 1)) {
                boolean z5 = false;
                try {
                    if (this.counterCells == counterCellArr) {
                        CounterCell[] counterCellArr4 = new CounterCell[2];
                        counterCellArr4[iAdvanceProbe & 1] = new CounterCell(j2);
                        this.counterCells = counterCellArr4;
                        z5 = true;
                    }
                    this.cellsBusy = 0;
                    if (z5) {
                        return;
                    }
                } finally {
                    this.cellsBusy = 0;
                }
            } else {
                Unsafe unsafe2 = f12571U;
                long j5 = BASECOUNT;
                long j6 = this.baseCount;
                if (unsafe2.compareAndSwapLong(unsafe2, j5, j6, j6 + j2)) {
                    return;
                }
            }
        }
    }

    private final void treeifyBin(Node<K, V>[] nodeArr, int i2) {
        if (nodeArr != null) {
            int length = nodeArr.length;
            if (length < 64) {
                tryPresize(length << 1);
                return;
            }
            Node<K, V> nodeTabAt = tabAt(nodeArr, i2);
            if (nodeTabAt != null && nodeTabAt.hash >= 0) {
                synchronized (nodeTabAt) {
                    if (tabAt(nodeArr, i2) == nodeTabAt) {
                        TreeNode<K, V> treeNode = null;
                        TreeNode<K, V> treeNode2 = null;
                        for (Node<K, V> node = nodeTabAt; node != null; node = node.next) {
                            TreeNode<K, V> treeNode3 = new TreeNode<>(node.hash, node.key, node.val, null, null);
                            TreeNode<K, V> treeNode4 = treeNode2;
                            treeNode3.prev = treeNode4;
                            if (treeNode4 == null) {
                                treeNode = treeNode3;
                            } else {
                                treeNode2.next = treeNode3;
                            }
                            treeNode2 = treeNode3;
                        }
                        setTabAt(nodeArr, i2, new TreeBin(treeNode));
                    }
                }
            }
        }
    }

    static <K, V> Node<K, V> untreeify(Node<K, V> node) {
        Node<K, V> node2 = null;
        Node<K, V> node3 = null;
        Node<K, V> node4 = node;
        while (true) {
            Node<K, V> node5 = node4;
            if (node5 != null) {
                Node<K, V> node6 = new Node<>(node5.hash, node5.key, node5.val, null);
                if (node3 == null) {
                    node2 = node6;
                } else {
                    node3.next = node6;
                }
                node3 = node6;
                node4 = node5.next;
            } else {
                return node2;
            }
        }
    }

    /* loaded from: rt.jar:java/util/concurrent/ConcurrentHashMap$TreeNode.class */
    static final class TreeNode<K, V> extends Node<K, V> {
        TreeNode<K, V> parent;
        TreeNode<K, V> left;
        TreeNode<K, V> right;
        TreeNode<K, V> prev;
        boolean red;

        TreeNode(int i2, K k2, V v2, Node<K, V> node, TreeNode<K, V> treeNode) {
            super(i2, k2, v2, node);
            this.parent = treeNode;
        }

        @Override // java.util.concurrent.ConcurrentHashMap.Node
        Node<K, V> find(int i2, Object obj) {
            return findTreeNode(i2, obj, null);
        }

        /* JADX WARN: Removed duplicated region for block: B:29:0x0077 A[PHI: r8
  0x0077: PHI (r8v2 java.lang.Class<?>) = (r8v1 java.lang.Class<?>), (r8v4 java.lang.Class<?>) binds: [B:26:0x006b, B:28:0x0074] A[DONT_GENERATE, DONT_INLINE]] */
        /* JADX WARN: Removed duplicated region for block: B:36:0x0095 A[PHI: r8
  0x0095: PHI (r8v3 java.lang.Class<?>) = (r8v2 java.lang.Class<?>), (r8v4 java.lang.Class<?>) binds: [B:30:0x0081, B:28:0x0074] A[DONT_GENERATE, DONT_INLINE]] */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct code enable 'Show inconsistent code' option in preferences
        */
        final java.util.concurrent.ConcurrentHashMap.TreeNode<K, V> findTreeNode(int r6, java.lang.Object r7, java.lang.Class<?> r8) {
            /*
                r5 = this;
                r0 = r7
                if (r0 == 0) goto Laf
                r0 = r5
                r9 = r0
            L7:
                r0 = r9
                java.util.concurrent.ConcurrentHashMap$TreeNode<K, V> r0 = r0.left
                r14 = r0
                r0 = r9
                java.util.concurrent.ConcurrentHashMap$TreeNode<K, V> r0 = r0.right
                r15 = r0
                r0 = r9
                int r0 = r0.hash
                r1 = r0
                r10 = r1
                r1 = r6
                if (r0 <= r1) goto L28
                r0 = r14
                r9 = r0
                goto Laa
            L28:
                r0 = r10
                r1 = r6
                if (r0 >= r1) goto L35
                r0 = r15
                r9 = r0
                goto Laa
            L35:
                r0 = r9
                K r0 = r0.key
                r1 = r0
                r12 = r1
                r1 = r7
                if (r0 == r1) goto L4f
                r0 = r12
                if (r0 == 0) goto L52
                r0 = r7
                r1 = r12
                boolean r0 = r0.equals(r1)
                if (r0 == 0) goto L52
            L4f:
                r0 = r9
                return r0
            L52:
                r0 = r14
                if (r0 != 0) goto L5e
                r0 = r15
                r9 = r0
                goto Laa
            L5e:
                r0 = r15
                if (r0 != 0) goto L6a
                r0 = r14
                r9 = r0
                goto Laa
            L6a:
                r0 = r8
                if (r0 != 0) goto L77
                r0 = r7
                java.lang.Class r0 = java.util.concurrent.ConcurrentHashMap.comparableClassFor(r0)
                r1 = r0
                r8 = r1
                if (r0 == 0) goto L95
            L77:
                r0 = r8
                r1 = r7
                r2 = r12
                int r0 = java.util.concurrent.ConcurrentHashMap.compareComparables(r0, r1, r2)
                r1 = r0
                r11 = r1
                if (r0 == 0) goto L95
                r0 = r11
                if (r0 >= 0) goto L8e
                r0 = r14
                goto L90
            L8e:
                r0 = r15
            L90:
                r9 = r0
                goto Laa
            L95:
                r0 = r15
                r1 = r6
                r2 = r7
                r3 = r8
                java.util.concurrent.ConcurrentHashMap$TreeNode r0 = r0.findTreeNode(r1, r2, r3)
                r1 = r0
                r13 = r1
                if (r0 == 0) goto La6
                r0 = r13
                return r0
            La6:
                r0 = r14
                r9 = r0
            Laa:
                r0 = r9
                if (r0 != 0) goto L7
            Laf:
                r0 = 0
                return r0
            */
            throw new UnsupportedOperationException("Method not decompiled: java.util.concurrent.ConcurrentHashMap.TreeNode.findTreeNode(int, java.lang.Object, java.lang.Class):java.util.concurrent.ConcurrentHashMap$TreeNode");
        }
    }

    /* loaded from: rt.jar:java/util/concurrent/ConcurrentHashMap$TreeBin.class */
    static final class TreeBin<K, V> extends Node<K, V> {
        TreeNode<K, V> root;
        volatile TreeNode<K, V> first;
        volatile Thread waiter;
        volatile int lockState;
        static final int WRITER = 1;
        static final int WAITER = 2;
        static final int READER = 4;

        /* renamed from: U, reason: collision with root package name */
        private static final Unsafe f12572U;
        private static final long LOCKSTATE;
        static final /* synthetic */ boolean $assertionsDisabled;

        static {
            $assertionsDisabled = !ConcurrentHashMap.class.desiredAssertionStatus();
            try {
                f12572U = Unsafe.getUnsafe();
                LOCKSTATE = f12572U.objectFieldOffset(TreeBin.class.getDeclaredField("lockState"));
            } catch (Exception e2) {
                throw new Error(e2);
            }
        }

        /* JADX WARN: Removed duplicated region for block: B:8:0x001e  */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct code enable 'Show inconsistent code' option in preferences
        */
        static int tieBreakOrder(java.lang.Object r3, java.lang.Object r4) {
            /*
                r0 = r3
                if (r0 == 0) goto L1e
                r0 = r4
                if (r0 == 0) goto L1e
                r0 = r3
                java.lang.Class r0 = r0.getClass()
                java.lang.String r0 = r0.getName()
                r1 = r4
                java.lang.Class r1 = r1.getClass()
                java.lang.String r1 = r1.getName()
                int r0 = r0.compareTo(r1)
                r1 = r0
                r5 = r1
                if (r0 != 0) goto L2f
            L1e:
                r0 = r3
                int r0 = java.lang.System.identityHashCode(r0)
                r1 = r4
                int r1 = java.lang.System.identityHashCode(r1)
                if (r0 > r1) goto L2d
                r0 = -1
                goto L2e
            L2d:
                r0 = 1
            L2e:
                r5 = r0
            L2f:
                r0 = r5
                return r0
            */
            throw new UnsupportedOperationException("Method not decompiled: java.util.concurrent.ConcurrentHashMap.TreeBin.tieBreakOrder(java.lang.Object, java.lang.Object):int");
        }

        /* JADX WARN: Removed duplicated region for block: B:19:0x0085 A[PHI: r13
  0x0085: PHI (r13v2 java.lang.Class<?>) = (r13v1 java.lang.Class<?>), (r13v4 java.lang.Class<?>) binds: [B:16:0x0077, B:18:0x0082] A[DONT_GENERATE, DONT_INLINE]] */
        /* JADX WARN: Removed duplicated region for block: B:21:0x0094 A[PHI: r13
  0x0094: PHI (r13v3 java.lang.Class<?>) = (r13v2 java.lang.Class<?>), (r13v4 java.lang.Class<?>) binds: [B:20:0x0091, B:18:0x0082] A[DONT_GENERATE, DONT_INLINE]] */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct code enable 'Show inconsistent code' option in preferences
        */
        TreeBin(java.util.concurrent.ConcurrentHashMap.TreeNode<K, V> r7) {
            /*
                Method dump skipped, instructions count: 259
                To view this dump change 'Code comments level' option to 'DEBUG'
            */
            throw new UnsupportedOperationException("Method not decompiled: java.util.concurrent.ConcurrentHashMap.TreeBin.<init>(java.util.concurrent.ConcurrentHashMap$TreeNode):void");
        }

        private final void lockRoot() {
            if (!f12572U.compareAndSwapInt(this, LOCKSTATE, 0, 1)) {
                contendedLock();
            }
        }

        private final void unlockRoot() {
            this.lockState = 0;
        }

        private final void contendedLock() {
            boolean z2 = false;
            while (true) {
                int i2 = this.lockState;
                if ((i2 & (-3)) == 0) {
                    if (f12572U.compareAndSwapInt(this, LOCKSTATE, i2, 1)) {
                        break;
                    }
                } else if ((i2 & 2) == 0) {
                    if (f12572U.compareAndSwapInt(this, LOCKSTATE, i2, i2 | 2)) {
                        z2 = true;
                        this.waiter = Thread.currentThread();
                    }
                } else if (z2) {
                    LockSupport.park(this);
                }
            }
            if (z2) {
                this.waiter = null;
            }
        }

        @Override // java.util.concurrent.ConcurrentHashMap.Node
        final Node<K, V> find(int i2, Object obj) {
            K k2;
            Thread thread;
            Thread thread2;
            if (obj != null) {
                TreeNode<K, V> treeNode = this.first;
                while (treeNode != null) {
                    int i3 = this.lockState;
                    if ((i3 & 3) != 0) {
                        if (treeNode.hash == i2 && ((k2 = treeNode.key) == obj || (k2 != null && obj.equals(k2)))) {
                            return treeNode;
                        }
                        treeNode = treeNode.next;
                    } else if (f12572U.compareAndSwapInt(this, LOCKSTATE, i3, i3 + 4)) {
                        try {
                            TreeNode<K, V> treeNode2 = this.root;
                            TreeNode<K, V> treeNodeFindTreeNode = treeNode2 == null ? null : treeNode2.findTreeNode(i2, obj, null);
                            if (f12572U.getAndAddInt(this, LOCKSTATE, -4) == 6 && (thread2 = this.waiter) != null) {
                                LockSupport.unpark(thread2);
                            }
                            return treeNodeFindTreeNode;
                        } catch (Throwable th) {
                            if (f12572U.getAndAddInt(this, LOCKSTATE, -4) == 6 && (thread = this.waiter) != null) {
                                LockSupport.unpark(thread);
                            }
                            throw th;
                        }
                    }
                }
                return null;
            }
            return null;
        }

        /* JADX WARN: Code restructure failed: missing block: B:19:0x0063, code lost:
        
            return r16;
         */
        /* JADX WARN: Code restructure failed: missing block: B:37:0x00bf, code lost:
        
            return r20;
         */
        /* JADX WARN: Code restructure failed: missing block: B:65:0x015c, code lost:
        
            if (java.util.concurrent.ConcurrentHashMap.TreeBin.$assertionsDisabled != false) goto L70;
         */
        /* JADX WARN: Code restructure failed: missing block: B:67:0x0166, code lost:
        
            if (checkInvariants(r10.root) != false) goto L81;
         */
        /* JADX WARN: Code restructure failed: missing block: B:69:0x0170, code lost:
        
            throw new java.lang.AssertionError();
         */
        /* JADX WARN: Code restructure failed: missing block: B:70:0x0171, code lost:
        
            return null;
         */
        /* JADX WARN: Code restructure failed: missing block: B:81:?, code lost:
        
            return null;
         */
        /* JADX WARN: Removed duplicated region for block: B:24:0x0073 A[PHI: r14
  0x0073: PHI (r14v2 java.lang.Class<?>) = (r14v1 java.lang.Class<?>), (r14v4 java.lang.Class<?>) binds: [B:21:0x0066, B:23:0x0070] A[DONT_GENERATE, DONT_INLINE]] */
        /* JADX WARN: Removed duplicated region for block: B:26:0x0081 A[PHI: r14
  0x0081: PHI (r14v3 java.lang.Class<?>) = (r14v2 java.lang.Class<?>), (r14v4 java.lang.Class<?>) binds: [B:25:0x007e, B:23:0x0070] A[DONT_GENERATE, DONT_INLINE]] */
        /* JADX WARN: Removed duplicated region for block: B:32:0x00a3  */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct code enable 'Show inconsistent code' option in preferences
        */
        final java.util.concurrent.ConcurrentHashMap.TreeNode<K, V> putTreeVal(int r11, K r12, V r13) {
            /*
                Method dump skipped, instructions count: 371
                To view this dump change 'Code comments level' option to 'DEBUG'
            */
            throw new UnsupportedOperationException("Method not decompiled: java.util.concurrent.ConcurrentHashMap.TreeBin.putTreeVal(int, java.lang.Object, java.lang.Object):java.util.concurrent.ConcurrentHashMap$TreeNode");
        }

        final boolean removeTreeNode(TreeNode<K, V> treeNode) {
            TreeNode<K, V> treeNode2;
            TreeNode<K, V> treeNode3;
            TreeNode<K, V> treeNode4;
            TreeNode<K, V> treeNode5 = (TreeNode) treeNode.next;
            TreeNode<K, V> treeNode6 = treeNode.prev;
            if (treeNode6 == null) {
                this.first = treeNode5;
            } else {
                treeNode6.next = treeNode5;
            }
            if (treeNode5 != null) {
                treeNode5.prev = treeNode6;
            }
            if (this.first == null) {
                this.root = null;
                return true;
            }
            TreeNode<K, V> treeNode7 = this.root;
            TreeNode<K, V> treeNode8 = treeNode7;
            if (treeNode7 == null || treeNode8.right == null || (treeNode2 = treeNode8.left) == null || treeNode2.left == null) {
                return true;
            }
            lockRoot();
            try {
                TreeNode<K, V> treeNode9 = treeNode.left;
                TreeNode<K, V> treeNode10 = treeNode.right;
                if (treeNode9 != null && treeNode10 != null) {
                    TreeNode<K, V> treeNode11 = treeNode10;
                    while (true) {
                        TreeNode<K, V> treeNode12 = treeNode11.left;
                        if (treeNode12 == null) {
                            break;
                        }
                        treeNode11 = treeNode12;
                    }
                    boolean z2 = treeNode11.red;
                    treeNode11.red = treeNode.red;
                    treeNode.red = z2;
                    TreeNode<K, V> treeNode13 = treeNode11.right;
                    TreeNode<K, V> treeNode14 = treeNode.parent;
                    if (treeNode11 == treeNode10) {
                        treeNode.parent = treeNode11;
                        treeNode11.right = treeNode;
                    } else {
                        TreeNode<K, V> treeNode15 = treeNode11.parent;
                        treeNode.parent = treeNode15;
                        if (treeNode15 != null) {
                            if (treeNode11 == treeNode15.left) {
                                treeNode15.left = treeNode;
                            } else {
                                treeNode15.right = treeNode;
                            }
                        }
                        treeNode11.right = treeNode10;
                        if (treeNode10 != null) {
                            treeNode10.parent = treeNode11;
                        }
                    }
                    treeNode.left = null;
                    treeNode.right = treeNode13;
                    if (treeNode13 != null) {
                        treeNode13.parent = treeNode;
                    }
                    treeNode11.left = treeNode9;
                    if (treeNode9 != null) {
                        treeNode9.parent = treeNode11;
                    }
                    treeNode11.parent = treeNode14;
                    if (treeNode14 == null) {
                        treeNode8 = treeNode11;
                    } else if (treeNode == treeNode14.left) {
                        treeNode14.left = treeNode11;
                    } else {
                        treeNode14.right = treeNode11;
                    }
                    if (treeNode13 != null) {
                        treeNode3 = treeNode13;
                    } else {
                        treeNode3 = treeNode;
                    }
                } else if (treeNode9 != null) {
                    treeNode3 = treeNode9;
                } else if (treeNode10 != null) {
                    treeNode3 = treeNode10;
                } else {
                    treeNode3 = treeNode;
                }
                if (treeNode3 != treeNode) {
                    TreeNode<K, V> treeNode16 = treeNode.parent;
                    treeNode3.parent = treeNode16;
                    if (treeNode16 == null) {
                        treeNode8 = treeNode3;
                    } else if (treeNode == treeNode16.left) {
                        treeNode16.left = treeNode3;
                    } else {
                        treeNode16.right = treeNode3;
                    }
                    treeNode.parent = null;
                    treeNode.right = null;
                    treeNode.left = null;
                }
                this.root = treeNode.red ? treeNode8 : balanceDeletion(treeNode8, treeNode3);
                if (treeNode == treeNode3 && (treeNode4 = treeNode.parent) != null) {
                    if (treeNode == treeNode4.left) {
                        treeNode4.left = null;
                    } else if (treeNode == treeNode4.right) {
                        treeNode4.right = null;
                    }
                    treeNode.parent = null;
                }
                if ($assertionsDisabled || checkInvariants(this.root)) {
                    return false;
                }
                throw new AssertionError();
            } finally {
                unlockRoot();
            }
        }

        static <K, V> TreeNode<K, V> rotateLeft(TreeNode<K, V> treeNode, TreeNode<K, V> treeNode2) {
            TreeNode<K, V> treeNode3;
            if (treeNode2 != null && (treeNode3 = treeNode2.right) != null) {
                TreeNode<K, V> treeNode4 = treeNode3.left;
                treeNode2.right = treeNode4;
                if (treeNode4 != null) {
                    treeNode4.parent = treeNode2;
                }
                TreeNode<K, V> treeNode5 = treeNode2.parent;
                treeNode3.parent = treeNode5;
                if (treeNode5 == null) {
                    treeNode = treeNode3;
                    treeNode3.red = false;
                } else if (treeNode5.left == treeNode2) {
                    treeNode5.left = treeNode3;
                } else {
                    treeNode5.right = treeNode3;
                }
                treeNode3.left = treeNode2;
                treeNode2.parent = treeNode3;
            }
            return treeNode;
        }

        static <K, V> TreeNode<K, V> rotateRight(TreeNode<K, V> treeNode, TreeNode<K, V> treeNode2) {
            TreeNode<K, V> treeNode3;
            if (treeNode2 != null && (treeNode3 = treeNode2.left) != null) {
                TreeNode<K, V> treeNode4 = treeNode3.right;
                treeNode2.left = treeNode4;
                if (treeNode4 != null) {
                    treeNode4.parent = treeNode2;
                }
                TreeNode<K, V> treeNode5 = treeNode2.parent;
                treeNode3.parent = treeNode5;
                if (treeNode5 == null) {
                    treeNode = treeNode3;
                    treeNode3.red = false;
                } else if (treeNode5.right == treeNode2) {
                    treeNode5.right = treeNode3;
                } else {
                    treeNode5.left = treeNode3;
                }
                treeNode3.right = treeNode2;
                treeNode2.parent = treeNode3;
            }
            return treeNode;
        }

        static <K, V> TreeNode<K, V> balanceInsertion(TreeNode<K, V> treeNode, TreeNode<K, V> treeNode2) {
            treeNode2.red = true;
            while (true) {
                TreeNode<K, V> treeNode3 = treeNode2.parent;
                TreeNode<K, V> treeNode4 = treeNode3;
                if (treeNode3 == null) {
                    treeNode2.red = false;
                    return treeNode2;
                }
                if (!treeNode4.red) {
                    break;
                }
                TreeNode<K, V> treeNode5 = treeNode4.parent;
                TreeNode<K, V> treeNode6 = treeNode5;
                if (treeNode5 == null) {
                    break;
                }
                TreeNode<K, V> treeNode7 = treeNode6.left;
                if (treeNode4 == treeNode7) {
                    TreeNode<K, V> treeNode8 = treeNode6.right;
                    if (treeNode8 != null && treeNode8.red) {
                        treeNode8.red = false;
                        treeNode4.red = false;
                        treeNode6.red = true;
                        treeNode2 = treeNode6;
                    } else {
                        if (treeNode2 == treeNode4.right) {
                            treeNode2 = treeNode4;
                            treeNode = rotateLeft(treeNode, treeNode4);
                            TreeNode<K, V> treeNode9 = treeNode2.parent;
                            treeNode4 = treeNode9;
                            treeNode6 = treeNode9 == null ? null : treeNode4.parent;
                        }
                        if (treeNode4 != null) {
                            treeNode4.red = false;
                            if (treeNode6 != null) {
                                treeNode6.red = true;
                                treeNode = rotateRight(treeNode, treeNode6);
                            }
                        }
                    }
                } else if (treeNode7 != null && treeNode7.red) {
                    treeNode7.red = false;
                    treeNode4.red = false;
                    treeNode6.red = true;
                    treeNode2 = treeNode6;
                } else {
                    if (treeNode2 == treeNode4.left) {
                        treeNode2 = treeNode4;
                        treeNode = rotateRight(treeNode, treeNode4);
                        TreeNode<K, V> treeNode10 = treeNode2.parent;
                        treeNode4 = treeNode10;
                        treeNode6 = treeNode10 == null ? null : treeNode4.parent;
                    }
                    if (treeNode4 != null) {
                        treeNode4.red = false;
                        if (treeNode6 != null) {
                            treeNode6.red = true;
                            treeNode = rotateLeft(treeNode, treeNode6);
                        }
                    }
                }
            }
            return treeNode;
        }

        static <K, V> TreeNode<K, V> balanceDeletion(TreeNode<K, V> treeNode, TreeNode<K, V> treeNode2) {
            while (treeNode2 != null && treeNode2 != treeNode) {
                TreeNode<K, V> treeNode3 = treeNode2.parent;
                TreeNode<K, V> treeNode4 = treeNode3;
                if (treeNode3 == null) {
                    treeNode2.red = false;
                    return treeNode2;
                }
                if (treeNode2.red) {
                    treeNode2.red = false;
                    return treeNode;
                }
                TreeNode<K, V> treeNode5 = treeNode4.left;
                TreeNode<K, V> treeNode6 = treeNode5;
                if (treeNode5 == treeNode2) {
                    TreeNode<K, V> treeNode7 = treeNode4.right;
                    TreeNode<K, V> treeNode8 = treeNode7;
                    if (treeNode7 != null && treeNode8.red) {
                        treeNode8.red = false;
                        treeNode4.red = true;
                        treeNode = rotateLeft(treeNode, treeNode4);
                        TreeNode<K, V> treeNode9 = treeNode2.parent;
                        treeNode4 = treeNode9;
                        treeNode8 = treeNode9 == null ? null : treeNode4.right;
                    }
                    if (treeNode8 == null) {
                        treeNode2 = treeNode4;
                    } else {
                        TreeNode<K, V> treeNode10 = treeNode8.left;
                        TreeNode<K, V> treeNode11 = treeNode8.right;
                        if ((treeNode11 == null || !treeNode11.red) && (treeNode10 == null || !treeNode10.red)) {
                            treeNode8.red = true;
                            treeNode2 = treeNode4;
                        } else {
                            if (treeNode11 == null || !treeNode11.red) {
                                if (treeNode10 != null) {
                                    treeNode10.red = false;
                                }
                                treeNode8.red = true;
                                treeNode = rotateRight(treeNode, treeNode8);
                                TreeNode<K, V> treeNode12 = treeNode2.parent;
                                treeNode4 = treeNode12;
                                treeNode8 = treeNode12 == null ? null : treeNode4.right;
                            }
                            if (treeNode8 != null) {
                                treeNode8.red = treeNode4 == null ? false : treeNode4.red;
                                TreeNode<K, V> treeNode13 = treeNode8.right;
                                if (treeNode13 != null) {
                                    treeNode13.red = false;
                                }
                            }
                            if (treeNode4 != null) {
                                treeNode4.red = false;
                                treeNode = rotateLeft(treeNode, treeNode4);
                            }
                            treeNode2 = treeNode;
                        }
                    }
                } else {
                    if (treeNode6 != null && treeNode6.red) {
                        treeNode6.red = false;
                        treeNode4.red = true;
                        treeNode = rotateRight(treeNode, treeNode4);
                        TreeNode<K, V> treeNode14 = treeNode2.parent;
                        treeNode4 = treeNode14;
                        treeNode6 = treeNode14 == null ? null : treeNode4.left;
                    }
                    if (treeNode6 == null) {
                        treeNode2 = treeNode4;
                    } else {
                        TreeNode<K, V> treeNode15 = treeNode6.left;
                        TreeNode<K, V> treeNode16 = treeNode6.right;
                        if ((treeNode15 == null || !treeNode15.red) && (treeNode16 == null || !treeNode16.red)) {
                            treeNode6.red = true;
                            treeNode2 = treeNode4;
                        } else {
                            if (treeNode15 == null || !treeNode15.red) {
                                if (treeNode16 != null) {
                                    treeNode16.red = false;
                                }
                                treeNode6.red = true;
                                treeNode = rotateLeft(treeNode, treeNode6);
                                TreeNode<K, V> treeNode17 = treeNode2.parent;
                                treeNode4 = treeNode17;
                                treeNode6 = treeNode17 == null ? null : treeNode4.left;
                            }
                            if (treeNode6 != null) {
                                treeNode6.red = treeNode4 == null ? false : treeNode4.red;
                                TreeNode<K, V> treeNode18 = treeNode6.left;
                                if (treeNode18 != null) {
                                    treeNode18.red = false;
                                }
                            }
                            if (treeNode4 != null) {
                                treeNode4.red = false;
                                treeNode = rotateRight(treeNode, treeNode4);
                            }
                            treeNode2 = treeNode;
                        }
                    }
                }
            }
            return treeNode;
        }

        static <K, V> boolean checkInvariants(TreeNode<K, V> treeNode) {
            TreeNode<K, V> treeNode2 = treeNode.parent;
            TreeNode<K, V> treeNode3 = treeNode.left;
            TreeNode<K, V> treeNode4 = treeNode.right;
            TreeNode<K, V> treeNode5 = treeNode.prev;
            TreeNode treeNode6 = (TreeNode) treeNode.next;
            if (treeNode5 != null && treeNode5.next != treeNode) {
                return false;
            }
            if (treeNode6 != null && treeNode6.prev != treeNode) {
                return false;
            }
            if (treeNode2 != null && treeNode != treeNode2.left && treeNode != treeNode2.right) {
                return false;
            }
            if (treeNode3 != null && (treeNode3.parent != treeNode || treeNode3.hash > treeNode.hash)) {
                return false;
            }
            if (treeNode4 != null && (treeNode4.parent != treeNode || treeNode4.hash < treeNode.hash)) {
                return false;
            }
            if (treeNode.red && treeNode3 != null && treeNode3.red && treeNode4 != null && treeNode4.red) {
                return false;
            }
            if (treeNode3 != null && !checkInvariants(treeNode3)) {
                return false;
            }
            if (treeNode4 != null && !checkInvariants(treeNode4)) {
                return false;
            }
            return true;
        }
    }

    /* loaded from: rt.jar:java/util/concurrent/ConcurrentHashMap$TableStack.class */
    static final class TableStack<K, V> {
        int length;
        int index;
        Node<K, V>[] tab;
        TableStack<K, V> next;

        TableStack() {
        }
    }

    /* loaded from: rt.jar:java/util/concurrent/ConcurrentHashMap$Traverser.class */
    static class Traverser<K, V> {
        Node<K, V>[] tab;
        Node<K, V> next = null;
        TableStack<K, V> stack;
        TableStack<K, V> spare;
        int index;
        int baseIndex;
        int baseLimit;
        final int baseSize;

        Traverser(Node<K, V>[] nodeArr, int i2, int i3, int i4) {
            this.tab = nodeArr;
            this.baseSize = i2;
            this.index = i3;
            this.baseIndex = i3;
            this.baseLimit = i4;
        }

        final Node<K, V> advance() {
            Node<K, V>[] nodeArr;
            int length;
            int i2;
            Node<K, V> node = this.next;
            Node<K, V> node2 = node;
            if (node != null) {
                node2 = node2.next;
            }
            while (node2 == null) {
                if (this.baseIndex >= this.baseLimit || (nodeArr = this.tab) == null || (length = nodeArr.length) <= (i2 = this.index) || i2 < 0) {
                    this.next = null;
                    return null;
                }
                Node<K, V> nodeTabAt = ConcurrentHashMap.tabAt(nodeArr, i2);
                node2 = nodeTabAt;
                if (nodeTabAt != null && node2.hash < 0) {
                    if (node2 instanceof ForwardingNode) {
                        this.tab = ((ForwardingNode) node2).nextTable;
                        node2 = null;
                        pushState(nodeArr, i2, length);
                    } else if (node2 instanceof TreeBin) {
                        node2 = ((TreeBin) node2).first;
                    } else {
                        node2 = null;
                    }
                }
                if (this.stack != null) {
                    recoverState(length);
                } else {
                    int i3 = i2 + this.baseSize;
                    this.index = i3;
                    if (i3 >= length) {
                        int i4 = this.baseIndex + 1;
                        this.baseIndex = i4;
                        this.index = i4;
                    }
                }
            }
            Node<K, V> node3 = node2;
            this.next = node3;
            return node3;
        }

        private void pushState(Node<K, V>[] nodeArr, int i2, int i3) {
            TableStack<K, V> tableStack = this.spare;
            if (tableStack != null) {
                this.spare = tableStack.next;
            } else {
                tableStack = new TableStack<>();
            }
            tableStack.tab = nodeArr;
            tableStack.length = i3;
            tableStack.index = i2;
            tableStack.next = this.stack;
            this.stack = tableStack;
        }

        private void recoverState(int i2) {
            TableStack<K, V> tableStack;
            while (true) {
                tableStack = this.stack;
                if (tableStack == null) {
                    break;
                }
                int i3 = this.index;
                int i4 = tableStack.length;
                int i5 = i3 + i4;
                this.index = i5;
                if (i5 < i2) {
                    break;
                }
                i2 = i4;
                this.index = tableStack.index;
                this.tab = tableStack.tab;
                tableStack.tab = null;
                TableStack<K, V> tableStack2 = tableStack.next;
                tableStack.next = this.spare;
                this.stack = tableStack2;
                this.spare = tableStack;
            }
            if (tableStack == null) {
                int i6 = this.index + this.baseSize;
                this.index = i6;
                if (i6 >= i2) {
                    int i7 = this.baseIndex + 1;
                    this.baseIndex = i7;
                    this.index = i7;
                }
            }
        }
    }

    /* loaded from: rt.jar:java/util/concurrent/ConcurrentHashMap$BaseIterator.class */
    static class BaseIterator<K, V> extends Traverser<K, V> {
        final ConcurrentHashMap<K, V> map;
        Node<K, V> lastReturned;

        BaseIterator(Node<K, V>[] nodeArr, int i2, int i3, int i4, ConcurrentHashMap<K, V> concurrentHashMap) {
            super(nodeArr, i2, i3, i4);
            this.map = concurrentHashMap;
            advance();
        }

        public final boolean hasNext() {
            return this.next != null;
        }

        public final boolean hasMoreElements() {
            return this.next != null;
        }

        public final void remove() {
            Node<K, V> node = this.lastReturned;
            if (node == null) {
                throw new IllegalStateException();
            }
            this.lastReturned = null;
            this.map.replaceNode(node.key, null, null);
        }
    }

    /* loaded from: rt.jar:java/util/concurrent/ConcurrentHashMap$KeyIterator.class */
    static final class KeyIterator<K, V> extends BaseIterator<K, V> implements Iterator<K>, Enumeration<K> {
        KeyIterator(Node<K, V>[] nodeArr, int i2, int i3, int i4, ConcurrentHashMap<K, V> concurrentHashMap) {
            super(nodeArr, i2, i3, i4, concurrentHashMap);
        }

        @Override // java.util.Iterator
        public final K next() {
            Node<K, V> node = this.next;
            if (node == null) {
                throw new NoSuchElementException();
            }
            K k2 = node.key;
            this.lastReturned = node;
            advance();
            return k2;
        }

        @Override // java.util.Enumeration
        /* renamed from: nextElement */
        public final K nextElement2() {
            return next();
        }
    }

    /* loaded from: rt.jar:java/util/concurrent/ConcurrentHashMap$ValueIterator.class */
    static final class ValueIterator<K, V> extends BaseIterator<K, V> implements Iterator<V>, Enumeration<V> {
        ValueIterator(Node<K, V>[] nodeArr, int i2, int i3, int i4, ConcurrentHashMap<K, V> concurrentHashMap) {
            super(nodeArr, i2, i3, i4, concurrentHashMap);
        }

        @Override // java.util.Iterator
        public final V next() {
            Node<K, V> node = this.next;
            if (node == null) {
                throw new NoSuchElementException();
            }
            V v2 = node.val;
            this.lastReturned = node;
            advance();
            return v2;
        }

        @Override // java.util.Enumeration
        /* renamed from: nextElement */
        public final V nextElement2() {
            return next();
        }
    }

    /* loaded from: rt.jar:java/util/concurrent/ConcurrentHashMap$EntryIterator.class */
    static final class EntryIterator<K, V> extends BaseIterator<K, V> implements Iterator<Map.Entry<K, V>> {
        EntryIterator(Node<K, V>[] nodeArr, int i2, int i3, int i4, ConcurrentHashMap<K, V> concurrentHashMap) {
            super(nodeArr, i2, i3, i4, concurrentHashMap);
        }

        @Override // java.util.Iterator
        public final Map.Entry<K, V> next() {
            Node<K, V> node = this.next;
            if (node == null) {
                throw new NoSuchElementException();
            }
            K k2 = node.key;
            V v2 = node.val;
            this.lastReturned = node;
            advance();
            return new MapEntry(k2, v2, this.map);
        }
    }

    /* loaded from: rt.jar:java/util/concurrent/ConcurrentHashMap$MapEntry.class */
    static final class MapEntry<K, V> implements Map.Entry<K, V> {
        final K key;
        V val;
        final ConcurrentHashMap<K, V> map;

        MapEntry(K k2, V v2, ConcurrentHashMap<K, V> concurrentHashMap) {
            this.key = k2;
            this.val = v2;
            this.map = concurrentHashMap;
        }

        @Override // java.util.Map.Entry
        public K getKey() {
            return this.key;
        }

        @Override // java.util.Map.Entry
        public V getValue() {
            return this.val;
        }

        @Override // java.util.Map.Entry
        public int hashCode() {
            return this.key.hashCode() ^ this.val.hashCode();
        }

        public String toString() {
            return ((Object) this.key) + "=" + ((Object) this.val);
        }

        @Override // java.util.Map.Entry
        public boolean equals(Object obj) {
            Map.Entry entry;
            Object key;
            Object value;
            return (obj instanceof Map.Entry) && (key = (entry = (Map.Entry) obj).getKey()) != null && (value = entry.getValue()) != null && (key == this.key || key.equals(this.key)) && (value == this.val || value.equals(this.val));
        }

        @Override // java.util.Map.Entry
        public V setValue(V v2) {
            if (v2 == null) {
                throw new NullPointerException();
            }
            V v3 = this.val;
            this.val = v2;
            this.map.put(this.key, v2);
            return v3;
        }
    }

    /* loaded from: rt.jar:java/util/concurrent/ConcurrentHashMap$KeySpliterator.class */
    static final class KeySpliterator<K, V> extends Traverser<K, V> implements Spliterator<K> {
        long est;

        KeySpliterator(Node<K, V>[] nodeArr, int i2, int i3, int i4, long j2) {
            super(nodeArr, i2, i3, i4);
            this.est = j2;
        }

        @Override // java.util.Spliterator
        public Spliterator<K> trySplit() {
            int i2 = this.baseIndex;
            int i3 = this.baseLimit;
            int i4 = (i2 + i3) >>> 1;
            if (i4 <= i2) {
                return null;
            }
            Node<K, V>[] nodeArr = this.tab;
            int i5 = this.baseSize;
            this.baseLimit = i4;
            long j2 = this.est >>> 1;
            this.est = j2;
            return new KeySpliterator(nodeArr, i5, i4, i3, j2);
        }

        @Override // java.util.Spliterator
        public void forEachRemaining(Consumer<? super K> consumer) {
            if (consumer == null) {
                throw new NullPointerException();
            }
            while (true) {
                Node<K, V> nodeAdvance = advance();
                if (nodeAdvance != null) {
                    consumer.accept(nodeAdvance.key);
                } else {
                    return;
                }
            }
        }

        @Override // java.util.Spliterator
        public boolean tryAdvance(Consumer<? super K> consumer) {
            if (consumer == null) {
                throw new NullPointerException();
            }
            Node<K, V> nodeAdvance = advance();
            if (nodeAdvance == null) {
                return false;
            }
            consumer.accept(nodeAdvance.key);
            return true;
        }

        @Override // java.util.Spliterator
        public long estimateSize() {
            return this.est;
        }

        @Override // java.util.Spliterator
        public int characteristics() {
            return 4353;
        }
    }

    /* loaded from: rt.jar:java/util/concurrent/ConcurrentHashMap$ValueSpliterator.class */
    static final class ValueSpliterator<K, V> extends Traverser<K, V> implements Spliterator<V> {
        long est;

        ValueSpliterator(Node<K, V>[] nodeArr, int i2, int i3, int i4, long j2) {
            super(nodeArr, i2, i3, i4);
            this.est = j2;
        }

        @Override // java.util.Spliterator
        public Spliterator<V> trySplit() {
            int i2 = this.baseIndex;
            int i3 = this.baseLimit;
            int i4 = (i2 + i3) >>> 1;
            if (i4 <= i2) {
                return null;
            }
            Node<K, V>[] nodeArr = this.tab;
            int i5 = this.baseSize;
            this.baseLimit = i4;
            long j2 = this.est >>> 1;
            this.est = j2;
            return new ValueSpliterator(nodeArr, i5, i4, i3, j2);
        }

        @Override // java.util.Spliterator
        public void forEachRemaining(Consumer<? super V> consumer) {
            if (consumer == null) {
                throw new NullPointerException();
            }
            while (true) {
                Node<K, V> nodeAdvance = advance();
                if (nodeAdvance != null) {
                    consumer.accept(nodeAdvance.val);
                } else {
                    return;
                }
            }
        }

        @Override // java.util.Spliterator
        public boolean tryAdvance(Consumer<? super V> consumer) {
            if (consumer == null) {
                throw new NullPointerException();
            }
            Node<K, V> nodeAdvance = advance();
            if (nodeAdvance == null) {
                return false;
            }
            consumer.accept(nodeAdvance.val);
            return true;
        }

        @Override // java.util.Spliterator
        public long estimateSize() {
            return this.est;
        }

        @Override // java.util.Spliterator
        public int characteristics() {
            return NormalizerImpl.JAMO_L_BASE;
        }
    }

    /* loaded from: rt.jar:java/util/concurrent/ConcurrentHashMap$EntrySpliterator.class */
    static final class EntrySpliterator<K, V> extends Traverser<K, V> implements Spliterator<Map.Entry<K, V>> {
        final ConcurrentHashMap<K, V> map;
        long est;

        EntrySpliterator(Node<K, V>[] nodeArr, int i2, int i3, int i4, long j2, ConcurrentHashMap<K, V> concurrentHashMap) {
            super(nodeArr, i2, i3, i4);
            this.map = concurrentHashMap;
            this.est = j2;
        }

        @Override // java.util.Spliterator
        public Spliterator<Map.Entry<K, V>> trySplit() {
            int i2 = this.baseIndex;
            int i3 = this.baseLimit;
            int i4 = (i2 + i3) >>> 1;
            if (i4 <= i2) {
                return null;
            }
            Node<K, V>[] nodeArr = this.tab;
            int i5 = this.baseSize;
            this.baseLimit = i4;
            long j2 = this.est >>> 1;
            this.est = j2;
            return new EntrySpliterator(nodeArr, i5, i4, i3, j2, this.map);
        }

        @Override // java.util.Spliterator
        public void forEachRemaining(Consumer<? super Map.Entry<K, V>> consumer) {
            if (consumer == null) {
                throw new NullPointerException();
            }
            while (true) {
                Node<K, V> nodeAdvance = advance();
                if (nodeAdvance != null) {
                    consumer.accept(new MapEntry(nodeAdvance.key, nodeAdvance.val, this.map));
                } else {
                    return;
                }
            }
        }

        @Override // java.util.Spliterator
        public boolean tryAdvance(Consumer<? super Map.Entry<K, V>> consumer) {
            if (consumer == null) {
                throw new NullPointerException();
            }
            Node<K, V> nodeAdvance = advance();
            if (nodeAdvance == null) {
                return false;
            }
            consumer.accept(new MapEntry(nodeAdvance.key, nodeAdvance.val, this.map));
            return true;
        }

        @Override // java.util.Spliterator
        public long estimateSize() {
            return this.est;
        }

        @Override // java.util.Spliterator
        public int characteristics() {
            return 4353;
        }
    }

    final int batchFor(long j2) {
        if (j2 == Long.MAX_VALUE) {
            return 0;
        }
        long jSumCount = sumCount();
        if (jSumCount <= 1 || jSumCount < j2) {
            return 0;
        }
        int commonPoolParallelism = ForkJoinPool.getCommonPoolParallelism() << 2;
        if (j2 > 0) {
            long j3 = jSumCount / j2;
            if (j3 < commonPoolParallelism) {
                return (int) j3;
            }
        }
        return commonPoolParallelism;
    }

    public void forEach(long j2, BiConsumer<? super K, ? super V> biConsumer) {
        if (biConsumer == null) {
            throw new NullPointerException();
        }
        new ForEachMappingTask(null, batchFor(j2), 0, 0, this.table, biConsumer).invoke();
    }

    public <U> void forEach(long j2, BiFunction<? super K, ? super V, ? extends U> biFunction, Consumer<? super U> consumer) {
        if (biFunction == null || consumer == null) {
            throw new NullPointerException();
        }
        new ForEachTransformedMappingTask(null, batchFor(j2), 0, 0, this.table, biFunction, consumer).invoke();
    }

    public <U> U search(long j2, BiFunction<? super K, ? super V, ? extends U> biFunction) {
        if (biFunction == null) {
            throw new NullPointerException();
        }
        return new SearchMappingsTask(null, batchFor(j2), 0, 0, this.table, biFunction, new AtomicReference()).invoke();
    }

    public <U> U reduce(long j2, BiFunction<? super K, ? super V, ? extends U> biFunction, BiFunction<? super U, ? super U, ? extends U> biFunction2) {
        if (biFunction == null || biFunction2 == null) {
            throw new NullPointerException();
        }
        return new MapReduceMappingsTask(null, batchFor(j2), 0, 0, this.table, null, biFunction, biFunction2).invoke();
    }

    public double reduceToDouble(long j2, ToDoubleBiFunction<? super K, ? super V> toDoubleBiFunction, double d2, DoubleBinaryOperator doubleBinaryOperator) {
        if (toDoubleBiFunction == null || doubleBinaryOperator == null) {
            throw new NullPointerException();
        }
        return new MapReduceMappingsToDoubleTask(null, batchFor(j2), 0, 0, this.table, null, toDoubleBiFunction, d2, doubleBinaryOperator).invoke().doubleValue();
    }

    public long reduceToLong(long j2, ToLongBiFunction<? super K, ? super V> toLongBiFunction, long j3, LongBinaryOperator longBinaryOperator) {
        if (toLongBiFunction == null || longBinaryOperator == null) {
            throw new NullPointerException();
        }
        return new MapReduceMappingsToLongTask(null, batchFor(j2), 0, 0, this.table, null, toLongBiFunction, j3, longBinaryOperator).invoke().longValue();
    }

    public int reduceToInt(long j2, ToIntBiFunction<? super K, ? super V> toIntBiFunction, int i2, IntBinaryOperator intBinaryOperator) {
        if (toIntBiFunction == null || intBinaryOperator == null) {
            throw new NullPointerException();
        }
        return new MapReduceMappingsToIntTask(null, batchFor(j2), 0, 0, this.table, null, toIntBiFunction, i2, intBinaryOperator).invoke().intValue();
    }

    public void forEachKey(long j2, Consumer<? super K> consumer) {
        if (consumer == null) {
            throw new NullPointerException();
        }
        new ForEachKeyTask(null, batchFor(j2), 0, 0, this.table, consumer).invoke();
    }

    public <U> void forEachKey(long j2, Function<? super K, ? extends U> function, Consumer<? super U> consumer) {
        if (function == null || consumer == null) {
            throw new NullPointerException();
        }
        new ForEachTransformedKeyTask(null, batchFor(j2), 0, 0, this.table, function, consumer).invoke();
    }

    public <U> U searchKeys(long j2, Function<? super K, ? extends U> function) {
        if (function == null) {
            throw new NullPointerException();
        }
        return new SearchKeysTask(null, batchFor(j2), 0, 0, this.table, function, new AtomicReference()).invoke();
    }

    public K reduceKeys(long j2, BiFunction<? super K, ? super K, ? extends K> biFunction) {
        if (biFunction == null) {
            throw new NullPointerException();
        }
        return new ReduceKeysTask(null, batchFor(j2), 0, 0, this.table, null, biFunction).invoke();
    }

    public <U> U reduceKeys(long j2, Function<? super K, ? extends U> function, BiFunction<? super U, ? super U, ? extends U> biFunction) {
        if (function == null || biFunction == null) {
            throw new NullPointerException();
        }
        return new MapReduceKeysTask(null, batchFor(j2), 0, 0, this.table, null, function, biFunction).invoke();
    }

    public double reduceKeysToDouble(long j2, ToDoubleFunction<? super K> toDoubleFunction, double d2, DoubleBinaryOperator doubleBinaryOperator) {
        if (toDoubleFunction == null || doubleBinaryOperator == null) {
            throw new NullPointerException();
        }
        return new MapReduceKeysToDoubleTask(null, batchFor(j2), 0, 0, this.table, null, toDoubleFunction, d2, doubleBinaryOperator).invoke().doubleValue();
    }

    public long reduceKeysToLong(long j2, ToLongFunction<? super K> toLongFunction, long j3, LongBinaryOperator longBinaryOperator) {
        if (toLongFunction == null || longBinaryOperator == null) {
            throw new NullPointerException();
        }
        return new MapReduceKeysToLongTask(null, batchFor(j2), 0, 0, this.table, null, toLongFunction, j3, longBinaryOperator).invoke().longValue();
    }

    public int reduceKeysToInt(long j2, ToIntFunction<? super K> toIntFunction, int i2, IntBinaryOperator intBinaryOperator) {
        if (toIntFunction == null || intBinaryOperator == null) {
            throw new NullPointerException();
        }
        return new MapReduceKeysToIntTask(null, batchFor(j2), 0, 0, this.table, null, toIntFunction, i2, intBinaryOperator).invoke().intValue();
    }

    public void forEachValue(long j2, Consumer<? super V> consumer) {
        if (consumer == null) {
            throw new NullPointerException();
        }
        new ForEachValueTask(null, batchFor(j2), 0, 0, this.table, consumer).invoke();
    }

    public <U> void forEachValue(long j2, Function<? super V, ? extends U> function, Consumer<? super U> consumer) {
        if (function == null || consumer == null) {
            throw new NullPointerException();
        }
        new ForEachTransformedValueTask(null, batchFor(j2), 0, 0, this.table, function, consumer).invoke();
    }

    public <U> U searchValues(long j2, Function<? super V, ? extends U> function) {
        if (function == null) {
            throw new NullPointerException();
        }
        return new SearchValuesTask(null, batchFor(j2), 0, 0, this.table, function, new AtomicReference()).invoke();
    }

    public V reduceValues(long j2, BiFunction<? super V, ? super V, ? extends V> biFunction) {
        if (biFunction == null) {
            throw new NullPointerException();
        }
        return new ReduceValuesTask(null, batchFor(j2), 0, 0, this.table, null, biFunction).invoke();
    }

    public <U> U reduceValues(long j2, Function<? super V, ? extends U> function, BiFunction<? super U, ? super U, ? extends U> biFunction) {
        if (function == null || biFunction == null) {
            throw new NullPointerException();
        }
        return new MapReduceValuesTask(null, batchFor(j2), 0, 0, this.table, null, function, biFunction).invoke();
    }

    public double reduceValuesToDouble(long j2, ToDoubleFunction<? super V> toDoubleFunction, double d2, DoubleBinaryOperator doubleBinaryOperator) {
        if (toDoubleFunction == null || doubleBinaryOperator == null) {
            throw new NullPointerException();
        }
        return new MapReduceValuesToDoubleTask(null, batchFor(j2), 0, 0, this.table, null, toDoubleFunction, d2, doubleBinaryOperator).invoke().doubleValue();
    }

    public long reduceValuesToLong(long j2, ToLongFunction<? super V> toLongFunction, long j3, LongBinaryOperator longBinaryOperator) {
        if (toLongFunction == null || longBinaryOperator == null) {
            throw new NullPointerException();
        }
        return new MapReduceValuesToLongTask(null, batchFor(j2), 0, 0, this.table, null, toLongFunction, j3, longBinaryOperator).invoke().longValue();
    }

    public int reduceValuesToInt(long j2, ToIntFunction<? super V> toIntFunction, int i2, IntBinaryOperator intBinaryOperator) {
        if (toIntFunction == null || intBinaryOperator == null) {
            throw new NullPointerException();
        }
        return new MapReduceValuesToIntTask(null, batchFor(j2), 0, 0, this.table, null, toIntFunction, i2, intBinaryOperator).invoke().intValue();
    }

    public void forEachEntry(long j2, Consumer<? super Map.Entry<K, V>> consumer) {
        if (consumer == null) {
            throw new NullPointerException();
        }
        new ForEachEntryTask(null, batchFor(j2), 0, 0, this.table, consumer).invoke();
    }

    public <U> void forEachEntry(long j2, Function<Map.Entry<K, V>, ? extends U> function, Consumer<? super U> consumer) {
        if (function == null || consumer == null) {
            throw new NullPointerException();
        }
        new ForEachTransformedEntryTask(null, batchFor(j2), 0, 0, this.table, function, consumer).invoke();
    }

    public <U> U searchEntries(long j2, Function<Map.Entry<K, V>, ? extends U> function) {
        if (function == null) {
            throw new NullPointerException();
        }
        return new SearchEntriesTask(null, batchFor(j2), 0, 0, this.table, function, new AtomicReference()).invoke();
    }

    public Map.Entry<K, V> reduceEntries(long j2, BiFunction<Map.Entry<K, V>, Map.Entry<K, V>, ? extends Map.Entry<K, V>> biFunction) {
        if (biFunction == null) {
            throw new NullPointerException();
        }
        return new ReduceEntriesTask(null, batchFor(j2), 0, 0, this.table, null, biFunction).invoke();
    }

    public <U> U reduceEntries(long j2, Function<Map.Entry<K, V>, ? extends U> function, BiFunction<? super U, ? super U, ? extends U> biFunction) {
        if (function == null || biFunction == null) {
            throw new NullPointerException();
        }
        return new MapReduceEntriesTask(null, batchFor(j2), 0, 0, this.table, null, function, biFunction).invoke();
    }

    public double reduceEntriesToDouble(long j2, ToDoubleFunction<Map.Entry<K, V>> toDoubleFunction, double d2, DoubleBinaryOperator doubleBinaryOperator) {
        if (toDoubleFunction == null || doubleBinaryOperator == null) {
            throw new NullPointerException();
        }
        return new MapReduceEntriesToDoubleTask(null, batchFor(j2), 0, 0, this.table, null, toDoubleFunction, d2, doubleBinaryOperator).invoke().doubleValue();
    }

    public long reduceEntriesToLong(long j2, ToLongFunction<Map.Entry<K, V>> toLongFunction, long j3, LongBinaryOperator longBinaryOperator) {
        if (toLongFunction == null || longBinaryOperator == null) {
            throw new NullPointerException();
        }
        return new MapReduceEntriesToLongTask(null, batchFor(j2), 0, 0, this.table, null, toLongFunction, j3, longBinaryOperator).invoke().longValue();
    }

    public int reduceEntriesToInt(long j2, ToIntFunction<Map.Entry<K, V>> toIntFunction, int i2, IntBinaryOperator intBinaryOperator) {
        if (toIntFunction == null || intBinaryOperator == null) {
            throw new NullPointerException();
        }
        return new MapReduceEntriesToIntTask(null, batchFor(j2), 0, 0, this.table, null, toIntFunction, i2, intBinaryOperator).invoke().intValue();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: rt.jar:java/util/concurrent/ConcurrentHashMap$CollectionView.class */
    public static abstract class CollectionView<K, V, E> implements Collection<E>, Serializable {
        private static final long serialVersionUID = 7249069246763182397L;
        final ConcurrentHashMap<K, V> map;
        private static final String oomeMsg = "Required array size too large";

        @Override // java.util.Collection, java.lang.Iterable, java.util.List
        public abstract Iterator<E> iterator();

        @Override // java.util.Collection, java.util.Set
        public abstract boolean contains(Object obj);

        @Override // java.util.Collection, java.util.Set
        public abstract boolean remove(Object obj);

        CollectionView(ConcurrentHashMap<K, V> concurrentHashMap) {
            this.map = concurrentHashMap;
        }

        public ConcurrentHashMap<K, V> getMap() {
            return this.map;
        }

        @Override // java.util.Collection, java.util.List
        public final void clear() {
            this.map.clear();
        }

        @Override // java.util.Collection, java.util.Set
        public final int size() {
            return this.map.size();
        }

        @Override // java.util.Collection
        public final boolean isEmpty() {
            return this.map.isEmpty();
        }

        @Override // java.util.Collection, java.util.List
        public final Object[] toArray() {
            long jMappingCount = this.map.mappingCount();
            if (jMappingCount > 2147483639) {
                throw new OutOfMemoryError(oomeMsg);
            }
            int i2 = (int) jMappingCount;
            Object[] objArrCopyOf = new Object[i2];
            int i3 = 0;
            Iterator<E> it = iterator();
            while (it.hasNext()) {
                E next = it.next();
                if (i3 == i2) {
                    if (i2 >= ConcurrentHashMap.MAX_ARRAY_SIZE) {
                        throw new OutOfMemoryError(oomeMsg);
                    }
                    if (i2 >= 1073741819) {
                        i2 = ConcurrentHashMap.MAX_ARRAY_SIZE;
                    } else {
                        i2 += (i2 >>> 1) + 1;
                    }
                    objArrCopyOf = Arrays.copyOf(objArrCopyOf, i2);
                }
                int i4 = i3;
                i3++;
                objArrCopyOf[i4] = next;
            }
            return i3 == i2 ? objArrCopyOf : Arrays.copyOf(objArrCopyOf, i3);
        }

        /* JADX WARN: Multi-variable type inference failed */
        /* JADX WARN: Type inference failed for: r0v14, types: [java.lang.Object[]] */
        /* JADX WARN: Type inference failed for: r0v34 */
        /* JADX WARN: Type inference failed for: r0v41, types: [java.lang.Object[]] */
        @Override // java.util.Collection
        public final <T> T[] toArray(T[] tArr) {
            long jMappingCount = this.map.mappingCount();
            if (jMappingCount > 2147483639) {
                throw new OutOfMemoryError(oomeMsg);
            }
            int i2 = (int) jMappingCount;
            T[] tArrCopyOf = tArr.length >= i2 ? tArr : (Object[]) Array.newInstance(tArr.getClass().getComponentType(), i2);
            int length = tArrCopyOf.length;
            int i3 = 0;
            Iterator<E> it = iterator();
            while (it.hasNext()) {
                E next = it.next();
                if (i3 == length) {
                    if (length >= ConcurrentHashMap.MAX_ARRAY_SIZE) {
                        throw new OutOfMemoryError(oomeMsg);
                    }
                    if (length >= 1073741819) {
                        length = ConcurrentHashMap.MAX_ARRAY_SIZE;
                    } else {
                        length += (length >>> 1) + 1;
                    }
                    tArrCopyOf = Arrays.copyOf(tArrCopyOf, length);
                }
                int i4 = i3;
                i3++;
                tArrCopyOf[i4] = next;
            }
            if (tArr != tArrCopyOf || i3 >= length) {
                return i3 == length ? tArrCopyOf : (T[]) Arrays.copyOf(tArrCopyOf, i3);
            }
            tArrCopyOf[i3] = null;
            return tArrCopyOf;
        }

        public final String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append('[');
            Iterator<E> it = iterator();
            if (it.hasNext()) {
                while (true) {
                    E next = it.next();
                    sb.append(next == this ? "(this Collection)" : next);
                    if (!it.hasNext()) {
                        break;
                    }
                    sb.append(',').append(' ');
                }
            }
            return sb.append(']').toString();
        }

        @Override // java.util.Collection
        public final boolean containsAll(Collection<?> collection) {
            if (collection != this) {
                for (Object obj : collection) {
                    if (obj == null || !contains(obj)) {
                        return false;
                    }
                }
                return true;
            }
            return true;
        }

        @Override // java.util.Collection
        public final boolean removeAll(Collection<?> collection) {
            if (collection == null) {
                throw new NullPointerException();
            }
            boolean z2 = false;
            Iterator<E> it = iterator();
            while (it.hasNext()) {
                if (collection.contains(it.next())) {
                    it.remove();
                    z2 = true;
                }
            }
            return z2;
        }

        @Override // java.util.Collection
        public final boolean retainAll(Collection<?> collection) {
            if (collection == null) {
                throw new NullPointerException();
            }
            boolean z2 = false;
            Iterator<E> it = iterator();
            while (it.hasNext()) {
                if (!collection.contains(it.next())) {
                    it.remove();
                    z2 = true;
                }
            }
            return z2;
        }
    }

    /* loaded from: rt.jar:java/util/concurrent/ConcurrentHashMap$KeySetView.class */
    public static class KeySetView<K, V> extends CollectionView<K, V, K> implements Set<K>, Serializable {
        private static final long serialVersionUID = 7249069246763182397L;
        private final V value;

        @Override // java.util.concurrent.ConcurrentHashMap.CollectionView
        public /* bridge */ /* synthetic */ ConcurrentHashMap getMap() {
            return super.getMap();
        }

        KeySetView(ConcurrentHashMap<K, V> concurrentHashMap, V v2) {
            super(concurrentHashMap);
            this.value = v2;
        }

        public V getMappedValue() {
            return this.value;
        }

        @Override // java.util.concurrent.ConcurrentHashMap.CollectionView, java.util.Collection, java.util.Set
        public boolean contains(Object obj) {
            return this.map.containsKey(obj);
        }

        @Override // java.util.concurrent.ConcurrentHashMap.CollectionView, java.util.Collection, java.util.Set
        public boolean remove(Object obj) {
            return this.map.remove(obj) != null;
        }

        @Override // java.util.concurrent.ConcurrentHashMap.CollectionView, java.util.Collection, java.lang.Iterable, java.util.List
        public Iterator<K> iterator() {
            ConcurrentHashMap<K, V> concurrentHashMap = this.map;
            Node<K, V>[] nodeArr = concurrentHashMap.table;
            int length = nodeArr == null ? 0 : nodeArr.length;
            return new KeyIterator(nodeArr, length, 0, length, concurrentHashMap);
        }

        @Override // java.util.Collection, java.util.List
        public boolean add(K k2) {
            V v2 = this.value;
            if (v2 == null) {
                throw new UnsupportedOperationException();
            }
            return this.map.putVal(k2, v2, true) == null;
        }

        @Override // java.util.Collection
        public boolean addAll(Collection<? extends K> collection) {
            boolean z2 = false;
            V v2 = this.value;
            if (v2 == null) {
                throw new UnsupportedOperationException();
            }
            Iterator<? extends K> it = collection.iterator();
            while (it.hasNext()) {
                if (this.map.putVal(it.next(), v2, true) == null) {
                    z2 = true;
                }
            }
            return z2;
        }

        @Override // java.util.Collection, java.util.List
        public int hashCode() {
            int iHashCode = 0;
            Iterator<K> it = iterator();
            while (it.hasNext()) {
                iHashCode += it.next().hashCode();
            }
            return iHashCode;
        }

        @Override // java.util.Collection, java.util.List
        public boolean equals(Object obj) {
            Set set;
            return (obj instanceof Set) && ((set = (Set) obj) == this || (containsAll(set) && set.containsAll(this)));
        }

        @Override // java.util.Collection, java.lang.Iterable
        public Spliterator<K> spliterator() {
            ConcurrentHashMap<K, V> concurrentHashMap = this.map;
            long jSumCount = concurrentHashMap.sumCount();
            Node<K, V>[] nodeArr = concurrentHashMap.table;
            int length = nodeArr == null ? 0 : nodeArr.length;
            return new KeySpliterator(nodeArr, length, 0, length, jSumCount < 0 ? 0L : jSumCount);
        }

        @Override // java.lang.Iterable
        public void forEach(Consumer<? super K> consumer) {
            if (consumer == null) {
                throw new NullPointerException();
            }
            Node<K, V>[] nodeArr = this.map.table;
            if (nodeArr != null) {
                Traverser traverser = new Traverser(nodeArr, nodeArr.length, 0, nodeArr.length);
                while (true) {
                    Node<K, V> nodeAdvance = traverser.advance();
                    if (nodeAdvance != null) {
                        consumer.accept(nodeAdvance.key);
                    } else {
                        return;
                    }
                }
            }
        }
    }

    /* loaded from: rt.jar:java/util/concurrent/ConcurrentHashMap$ValuesView.class */
    static final class ValuesView<K, V> extends CollectionView<K, V, V> implements Collection<V>, Serializable {
        private static final long serialVersionUID = 2249069246763182397L;

        ValuesView(ConcurrentHashMap<K, V> concurrentHashMap) {
            super(concurrentHashMap);
        }

        @Override // java.util.concurrent.ConcurrentHashMap.CollectionView, java.util.Collection, java.util.Set
        public final boolean contains(Object obj) {
            return this.map.containsValue(obj);
        }

        @Override // java.util.concurrent.ConcurrentHashMap.CollectionView, java.util.Collection, java.util.Set
        public final boolean remove(Object obj) {
            if (obj != null) {
                Iterator<V> it = iterator();
                while (it.hasNext()) {
                    if (obj.equals(it.next())) {
                        it.remove();
                        return true;
                    }
                }
                return false;
            }
            return false;
        }

        @Override // java.util.concurrent.ConcurrentHashMap.CollectionView, java.util.Collection, java.lang.Iterable, java.util.List
        public final Iterator<V> iterator() {
            ConcurrentHashMap<K, V> concurrentHashMap = this.map;
            Node<K, V>[] nodeArr = concurrentHashMap.table;
            int length = nodeArr == null ? 0 : nodeArr.length;
            return new ValueIterator(nodeArr, length, 0, length, concurrentHashMap);
        }

        @Override // java.util.Collection, java.util.List
        public final boolean add(V v2) {
            throw new UnsupportedOperationException();
        }

        @Override // java.util.Collection
        public final boolean addAll(Collection<? extends V> collection) {
            throw new UnsupportedOperationException();
        }

        @Override // java.util.Collection, java.lang.Iterable
        public Spliterator<V> spliterator() {
            ConcurrentHashMap<K, V> concurrentHashMap = this.map;
            long jSumCount = concurrentHashMap.sumCount();
            Node<K, V>[] nodeArr = concurrentHashMap.table;
            int length = nodeArr == null ? 0 : nodeArr.length;
            return new ValueSpliterator(nodeArr, length, 0, length, jSumCount < 0 ? 0L : jSumCount);
        }

        @Override // java.lang.Iterable
        public void forEach(Consumer<? super V> consumer) {
            if (consumer == null) {
                throw new NullPointerException();
            }
            Node<K, V>[] nodeArr = this.map.table;
            if (nodeArr != null) {
                Traverser traverser = new Traverser(nodeArr, nodeArr.length, 0, nodeArr.length);
                while (true) {
                    Node<K, V> nodeAdvance = traverser.advance();
                    if (nodeAdvance != null) {
                        consumer.accept(nodeAdvance.val);
                    } else {
                        return;
                    }
                }
            }
        }
    }

    /* loaded from: rt.jar:java/util/concurrent/ConcurrentHashMap$EntrySetView.class */
    static final class EntrySetView<K, V> extends CollectionView<K, V, Map.Entry<K, V>> implements Set<Map.Entry<K, V>>, Serializable {
        private static final long serialVersionUID = 2249069246763182397L;

        EntrySetView(ConcurrentHashMap<K, V> concurrentHashMap) {
            super(concurrentHashMap);
        }

        @Override // java.util.concurrent.ConcurrentHashMap.CollectionView, java.util.Collection, java.util.Set
        public boolean contains(Object obj) {
            Map.Entry entry;
            Object key;
            V v2;
            Object value;
            return (!(obj instanceof Map.Entry) || (key = (entry = (Map.Entry) obj).getKey()) == null || (v2 = this.map.get(key)) == null || (value = entry.getValue()) == null || (value != v2 && !value.equals(v2))) ? false : true;
        }

        @Override // java.util.concurrent.ConcurrentHashMap.CollectionView, java.util.Collection, java.util.Set
        public boolean remove(Object obj) {
            Map.Entry entry;
            Object key;
            Object value;
            return (obj instanceof Map.Entry) && (key = (entry = (Map.Entry) obj).getKey()) != null && (value = entry.getValue()) != null && this.map.remove(key, value);
        }

        @Override // java.util.concurrent.ConcurrentHashMap.CollectionView, java.util.Collection, java.lang.Iterable, java.util.List
        public Iterator<Map.Entry<K, V>> iterator() {
            ConcurrentHashMap<K, V> concurrentHashMap = this.map;
            Node<K, V>[] nodeArr = concurrentHashMap.table;
            int length = nodeArr == null ? 0 : nodeArr.length;
            return new EntryIterator(nodeArr, length, 0, length, concurrentHashMap);
        }

        @Override // java.util.Collection, java.util.List
        public boolean add(Map.Entry<K, V> entry) {
            return this.map.putVal(entry.getKey(), entry.getValue(), false) == null;
        }

        @Override // java.util.Collection
        public boolean addAll(Collection<? extends Map.Entry<K, V>> collection) {
            boolean z2 = false;
            Iterator<? extends Map.Entry<K, V>> it = collection.iterator();
            while (it.hasNext()) {
                if (add((Map.Entry) it.next())) {
                    z2 = true;
                }
            }
            return z2;
        }

        @Override // java.util.Collection, java.util.List
        public final int hashCode() {
            int iHashCode = 0;
            Node<K, V>[] nodeArr = this.map.table;
            if (nodeArr != null) {
                Traverser traverser = new Traverser(nodeArr, nodeArr.length, 0, nodeArr.length);
                while (true) {
                    Node<K, V> nodeAdvance = traverser.advance();
                    if (nodeAdvance == null) {
                        break;
                    }
                    iHashCode += nodeAdvance.hashCode();
                }
            }
            return iHashCode;
        }

        @Override // java.util.Collection, java.util.List
        public final boolean equals(Object obj) {
            Set set;
            return (obj instanceof Set) && ((set = (Set) obj) == this || (containsAll(set) && set.containsAll(this)));
        }

        @Override // java.util.Collection, java.lang.Iterable
        public Spliterator<Map.Entry<K, V>> spliterator() {
            ConcurrentHashMap<K, V> concurrentHashMap = this.map;
            long jSumCount = concurrentHashMap.sumCount();
            Node<K, V>[] nodeArr = concurrentHashMap.table;
            int length = nodeArr == null ? 0 : nodeArr.length;
            return new EntrySpliterator(nodeArr, length, 0, length, jSumCount < 0 ? 0L : jSumCount, concurrentHashMap);
        }

        @Override // java.lang.Iterable
        public void forEach(Consumer<? super Map.Entry<K, V>> consumer) {
            if (consumer == null) {
                throw new NullPointerException();
            }
            Node<K, V>[] nodeArr = this.map.table;
            if (nodeArr != null) {
                Traverser traverser = new Traverser(nodeArr, nodeArr.length, 0, nodeArr.length);
                while (true) {
                    Node<K, V> nodeAdvance = traverser.advance();
                    if (nodeAdvance != null) {
                        consumer.accept(new MapEntry(nodeAdvance.key, nodeAdvance.val, this.map));
                    } else {
                        return;
                    }
                }
            }
        }
    }

    /* loaded from: rt.jar:java/util/concurrent/ConcurrentHashMap$BulkTask.class */
    static abstract class BulkTask<K, V, R> extends CountedCompleter<R> {
        Node<K, V>[] tab;
        Node<K, V> next;
        TableStack<K, V> stack;
        TableStack<K, V> spare;
        int index;
        int baseIndex;
        int baseLimit;
        final int baseSize;
        int batch;

        BulkTask(BulkTask<K, V, ?> bulkTask, int i2, int i3, int i4, Node<K, V>[] nodeArr) {
            super(bulkTask);
            this.batch = i2;
            this.baseIndex = i3;
            this.index = i3;
            this.tab = nodeArr;
            if (nodeArr == null) {
                this.baseLimit = 0;
                this.baseSize = 0;
            } else if (bulkTask == null) {
                int length = nodeArr.length;
                this.baseLimit = length;
                this.baseSize = length;
            } else {
                this.baseLimit = i4;
                this.baseSize = bulkTask.baseSize;
            }
        }

        final Node<K, V> advance() {
            Node<K, V>[] nodeArr;
            int length;
            int i2;
            Node<K, V> node = this.next;
            Node<K, V> node2 = node;
            if (node != null) {
                node2 = node2.next;
            }
            while (node2 == null) {
                if (this.baseIndex >= this.baseLimit || (nodeArr = this.tab) == null || (length = nodeArr.length) <= (i2 = this.index) || i2 < 0) {
                    this.next = null;
                    return null;
                }
                Node<K, V> nodeTabAt = ConcurrentHashMap.tabAt(nodeArr, i2);
                node2 = nodeTabAt;
                if (nodeTabAt != null && node2.hash < 0) {
                    if (node2 instanceof ForwardingNode) {
                        this.tab = ((ForwardingNode) node2).nextTable;
                        node2 = null;
                        pushState(nodeArr, i2, length);
                    } else if (node2 instanceof TreeBin) {
                        node2 = ((TreeBin) node2).first;
                    } else {
                        node2 = null;
                    }
                }
                if (this.stack != null) {
                    recoverState(length);
                } else {
                    int i3 = i2 + this.baseSize;
                    this.index = i3;
                    if (i3 >= length) {
                        int i4 = this.baseIndex + 1;
                        this.baseIndex = i4;
                        this.index = i4;
                    }
                }
            }
            Node<K, V> node3 = node2;
            this.next = node3;
            return node3;
        }

        private void pushState(Node<K, V>[] nodeArr, int i2, int i3) {
            TableStack<K, V> tableStack = this.spare;
            if (tableStack != null) {
                this.spare = tableStack.next;
            } else {
                tableStack = new TableStack<>();
            }
            tableStack.tab = nodeArr;
            tableStack.length = i3;
            tableStack.index = i2;
            tableStack.next = this.stack;
            this.stack = tableStack;
        }

        private void recoverState(int i2) {
            TableStack<K, V> tableStack;
            while (true) {
                tableStack = this.stack;
                if (tableStack == null) {
                    break;
                }
                int i3 = this.index;
                int i4 = tableStack.length;
                int i5 = i3 + i4;
                this.index = i5;
                if (i5 < i2) {
                    break;
                }
                i2 = i4;
                this.index = tableStack.index;
                this.tab = tableStack.tab;
                tableStack.tab = null;
                TableStack<K, V> tableStack2 = tableStack.next;
                tableStack.next = this.spare;
                this.stack = tableStack2;
                this.spare = tableStack;
            }
            if (tableStack == null) {
                int i6 = this.index + this.baseSize;
                this.index = i6;
                if (i6 >= i2) {
                    int i7 = this.baseIndex + 1;
                    this.baseIndex = i7;
                    this.index = i7;
                }
            }
        }
    }

    /* loaded from: rt.jar:java/util/concurrent/ConcurrentHashMap$ForEachKeyTask.class */
    static final class ForEachKeyTask<K, V> extends BulkTask<K, V, Void> {
        final Consumer<? super K> action;

        ForEachKeyTask(BulkTask<K, V, ?> bulkTask, int i2, int i3, int i4, Node<K, V>[] nodeArr, Consumer<? super K> consumer) {
            super(bulkTask, i2, i3, i4, nodeArr);
            this.action = consumer;
        }

        @Override // java.util.concurrent.CountedCompleter
        public final void compute() {
            Consumer<? super K> consumer = this.action;
            if (consumer != null) {
                int i2 = this.baseIndex;
                while (this.batch > 0) {
                    int i3 = this.baseLimit;
                    int i4 = (i3 + i2) >>> 1;
                    if (i4 <= i2) {
                        break;
                    }
                    addToPendingCount(1);
                    int i5 = this.batch >>> 1;
                    this.batch = i5;
                    this.baseLimit = i4;
                    new ForEachKeyTask(this, i5, i4, i3, this.tab, consumer).fork();
                }
                while (true) {
                    Node<K, V> nodeAdvance = advance();
                    if (nodeAdvance != null) {
                        consumer.accept(nodeAdvance.key);
                    } else {
                        propagateCompletion();
                        return;
                    }
                }
            }
        }
    }

    /* loaded from: rt.jar:java/util/concurrent/ConcurrentHashMap$ForEachValueTask.class */
    static final class ForEachValueTask<K, V> extends BulkTask<K, V, Void> {
        final Consumer<? super V> action;

        ForEachValueTask(BulkTask<K, V, ?> bulkTask, int i2, int i3, int i4, Node<K, V>[] nodeArr, Consumer<? super V> consumer) {
            super(bulkTask, i2, i3, i4, nodeArr);
            this.action = consumer;
        }

        @Override // java.util.concurrent.CountedCompleter
        public final void compute() {
            Consumer<? super V> consumer = this.action;
            if (consumer != null) {
                int i2 = this.baseIndex;
                while (this.batch > 0) {
                    int i3 = this.baseLimit;
                    int i4 = (i3 + i2) >>> 1;
                    if (i4 <= i2) {
                        break;
                    }
                    addToPendingCount(1);
                    int i5 = this.batch >>> 1;
                    this.batch = i5;
                    this.baseLimit = i4;
                    new ForEachValueTask(this, i5, i4, i3, this.tab, consumer).fork();
                }
                while (true) {
                    Node<K, V> nodeAdvance = advance();
                    if (nodeAdvance != null) {
                        consumer.accept(nodeAdvance.val);
                    } else {
                        propagateCompletion();
                        return;
                    }
                }
            }
        }
    }

    /* loaded from: rt.jar:java/util/concurrent/ConcurrentHashMap$ForEachEntryTask.class */
    static final class ForEachEntryTask<K, V> extends BulkTask<K, V, Void> {
        final Consumer<? super Map.Entry<K, V>> action;

        ForEachEntryTask(BulkTask<K, V, ?> bulkTask, int i2, int i3, int i4, Node<K, V>[] nodeArr, Consumer<? super Map.Entry<K, V>> consumer) {
            super(bulkTask, i2, i3, i4, nodeArr);
            this.action = consumer;
        }

        @Override // java.util.concurrent.CountedCompleter
        public final void compute() {
            Consumer<? super Map.Entry<K, V>> consumer = this.action;
            if (consumer != null) {
                int i2 = this.baseIndex;
                while (this.batch > 0) {
                    int i3 = this.baseLimit;
                    int i4 = (i3 + i2) >>> 1;
                    if (i4 <= i2) {
                        break;
                    }
                    addToPendingCount(1);
                    int i5 = this.batch >>> 1;
                    this.batch = i5;
                    this.baseLimit = i4;
                    new ForEachEntryTask(this, i5, i4, i3, this.tab, consumer).fork();
                }
                while (true) {
                    Node<K, V> nodeAdvance = advance();
                    if (nodeAdvance != null) {
                        consumer.accept(nodeAdvance);
                    } else {
                        propagateCompletion();
                        return;
                    }
                }
            }
        }
    }

    /* loaded from: rt.jar:java/util/concurrent/ConcurrentHashMap$ForEachMappingTask.class */
    static final class ForEachMappingTask<K, V> extends BulkTask<K, V, Void> {
        final BiConsumer<? super K, ? super V> action;

        ForEachMappingTask(BulkTask<K, V, ?> bulkTask, int i2, int i3, int i4, Node<K, V>[] nodeArr, BiConsumer<? super K, ? super V> biConsumer) {
            super(bulkTask, i2, i3, i4, nodeArr);
            this.action = biConsumer;
        }

        @Override // java.util.concurrent.CountedCompleter
        public final void compute() {
            BiConsumer<? super K, ? super V> biConsumer = this.action;
            if (biConsumer != null) {
                int i2 = this.baseIndex;
                while (this.batch > 0) {
                    int i3 = this.baseLimit;
                    int i4 = (i3 + i2) >>> 1;
                    if (i4 <= i2) {
                        break;
                    }
                    addToPendingCount(1);
                    int i5 = this.batch >>> 1;
                    this.batch = i5;
                    this.baseLimit = i4;
                    new ForEachMappingTask(this, i5, i4, i3, this.tab, biConsumer).fork();
                }
                while (true) {
                    Node<K, V> nodeAdvance = advance();
                    if (nodeAdvance != null) {
                        biConsumer.accept(nodeAdvance.key, nodeAdvance.val);
                    } else {
                        propagateCompletion();
                        return;
                    }
                }
            }
        }
    }

    /* loaded from: rt.jar:java/util/concurrent/ConcurrentHashMap$ForEachTransformedKeyTask.class */
    static final class ForEachTransformedKeyTask<K, V, U> extends BulkTask<K, V, Void> {
        final Function<? super K, ? extends U> transformer;
        final Consumer<? super U> action;

        ForEachTransformedKeyTask(BulkTask<K, V, ?> bulkTask, int i2, int i3, int i4, Node<K, V>[] nodeArr, Function<? super K, ? extends U> function, Consumer<? super U> consumer) {
            super(bulkTask, i2, i3, i4, nodeArr);
            this.transformer = function;
            this.action = consumer;
        }

        @Override // java.util.concurrent.CountedCompleter
        public final void compute() {
            Consumer<? super U> consumer;
            Function<? super K, ? extends U> function = this.transformer;
            if (function != null && (consumer = this.action) != null) {
                int i2 = this.baseIndex;
                while (this.batch > 0) {
                    int i3 = this.baseLimit;
                    int i4 = (i3 + i2) >>> 1;
                    if (i4 <= i2) {
                        break;
                    }
                    addToPendingCount(1);
                    int i5 = this.batch >>> 1;
                    this.batch = i5;
                    this.baseLimit = i4;
                    new ForEachTransformedKeyTask(this, i5, i4, i3, this.tab, function, consumer).fork();
                }
                while (true) {
                    Node<K, V> nodeAdvance = advance();
                    if (nodeAdvance != null) {
                        U uApply = function.apply(nodeAdvance.key);
                        if (uApply != null) {
                            consumer.accept(uApply);
                        }
                    } else {
                        propagateCompletion();
                        return;
                    }
                }
            }
        }
    }

    /* loaded from: rt.jar:java/util/concurrent/ConcurrentHashMap$ForEachTransformedValueTask.class */
    static final class ForEachTransformedValueTask<K, V, U> extends BulkTask<K, V, Void> {
        final Function<? super V, ? extends U> transformer;
        final Consumer<? super U> action;

        ForEachTransformedValueTask(BulkTask<K, V, ?> bulkTask, int i2, int i3, int i4, Node<K, V>[] nodeArr, Function<? super V, ? extends U> function, Consumer<? super U> consumer) {
            super(bulkTask, i2, i3, i4, nodeArr);
            this.transformer = function;
            this.action = consumer;
        }

        @Override // java.util.concurrent.CountedCompleter
        public final void compute() {
            Consumer<? super U> consumer;
            Function<? super V, ? extends U> function = this.transformer;
            if (function != null && (consumer = this.action) != null) {
                int i2 = this.baseIndex;
                while (this.batch > 0) {
                    int i3 = this.baseLimit;
                    int i4 = (i3 + i2) >>> 1;
                    if (i4 <= i2) {
                        break;
                    }
                    addToPendingCount(1);
                    int i5 = this.batch >>> 1;
                    this.batch = i5;
                    this.baseLimit = i4;
                    new ForEachTransformedValueTask(this, i5, i4, i3, this.tab, function, consumer).fork();
                }
                while (true) {
                    Node<K, V> nodeAdvance = advance();
                    if (nodeAdvance != null) {
                        U uApply = function.apply(nodeAdvance.val);
                        if (uApply != null) {
                            consumer.accept(uApply);
                        }
                    } else {
                        propagateCompletion();
                        return;
                    }
                }
            }
        }
    }

    /* loaded from: rt.jar:java/util/concurrent/ConcurrentHashMap$ForEachTransformedEntryTask.class */
    static final class ForEachTransformedEntryTask<K, V, U> extends BulkTask<K, V, Void> {
        final Function<Map.Entry<K, V>, ? extends U> transformer;
        final Consumer<? super U> action;

        ForEachTransformedEntryTask(BulkTask<K, V, ?> bulkTask, int i2, int i3, int i4, Node<K, V>[] nodeArr, Function<Map.Entry<K, V>, ? extends U> function, Consumer<? super U> consumer) {
            super(bulkTask, i2, i3, i4, nodeArr);
            this.transformer = function;
            this.action = consumer;
        }

        @Override // java.util.concurrent.CountedCompleter
        public final void compute() {
            Consumer<? super U> consumer;
            Function<Map.Entry<K, V>, ? extends U> function = this.transformer;
            if (function != null && (consumer = this.action) != null) {
                int i2 = this.baseIndex;
                while (this.batch > 0) {
                    int i3 = this.baseLimit;
                    int i4 = (i3 + i2) >>> 1;
                    if (i4 <= i2) {
                        break;
                    }
                    addToPendingCount(1);
                    int i5 = this.batch >>> 1;
                    this.batch = i5;
                    this.baseLimit = i4;
                    new ForEachTransformedEntryTask(this, i5, i4, i3, this.tab, function, consumer).fork();
                }
                while (true) {
                    Node<K, V> nodeAdvance = advance();
                    if (nodeAdvance != null) {
                        U uApply = function.apply(nodeAdvance);
                        if (uApply != null) {
                            consumer.accept(uApply);
                        }
                    } else {
                        propagateCompletion();
                        return;
                    }
                }
            }
        }
    }

    /* loaded from: rt.jar:java/util/concurrent/ConcurrentHashMap$ForEachTransformedMappingTask.class */
    static final class ForEachTransformedMappingTask<K, V, U> extends BulkTask<K, V, Void> {
        final BiFunction<? super K, ? super V, ? extends U> transformer;
        final Consumer<? super U> action;

        ForEachTransformedMappingTask(BulkTask<K, V, ?> bulkTask, int i2, int i3, int i4, Node<K, V>[] nodeArr, BiFunction<? super K, ? super V, ? extends U> biFunction, Consumer<? super U> consumer) {
            super(bulkTask, i2, i3, i4, nodeArr);
            this.transformer = biFunction;
            this.action = consumer;
        }

        @Override // java.util.concurrent.CountedCompleter
        public final void compute() {
            Consumer<? super U> consumer;
            BiFunction<? super K, ? super V, ? extends U> biFunction = this.transformer;
            if (biFunction != null && (consumer = this.action) != null) {
                int i2 = this.baseIndex;
                while (this.batch > 0) {
                    int i3 = this.baseLimit;
                    int i4 = (i3 + i2) >>> 1;
                    if (i4 <= i2) {
                        break;
                    }
                    addToPendingCount(1);
                    int i5 = this.batch >>> 1;
                    this.batch = i5;
                    this.baseLimit = i4;
                    new ForEachTransformedMappingTask(this, i5, i4, i3, this.tab, biFunction, consumer).fork();
                }
                while (true) {
                    Node<K, V> nodeAdvance = advance();
                    if (nodeAdvance != null) {
                        U uApply = biFunction.apply(nodeAdvance.key, nodeAdvance.val);
                        if (uApply != null) {
                            consumer.accept(uApply);
                        }
                    } else {
                        propagateCompletion();
                        return;
                    }
                }
            }
        }
    }

    /* loaded from: rt.jar:java/util/concurrent/ConcurrentHashMap$SearchKeysTask.class */
    static final class SearchKeysTask<K, V, U> extends BulkTask<K, V, U> {
        final Function<? super K, ? extends U> searchFunction;
        final AtomicReference<U> result;

        SearchKeysTask(BulkTask<K, V, ?> bulkTask, int i2, int i3, int i4, Node<K, V>[] nodeArr, Function<? super K, ? extends U> function, AtomicReference<U> atomicReference) {
            super(bulkTask, i2, i3, i4, nodeArr);
            this.searchFunction = function;
            this.result = atomicReference;
        }

        @Override // java.util.concurrent.CountedCompleter, java.util.concurrent.ForkJoinTask
        public final U getRawResult() {
            return this.result.get();
        }

        @Override // java.util.concurrent.CountedCompleter
        public final void compute() {
            AtomicReference<U> atomicReference;
            Function<? super K, ? extends U> function = this.searchFunction;
            if (function != null && (atomicReference = this.result) != null) {
                int i2 = this.baseIndex;
                while (this.batch > 0) {
                    int i3 = this.baseLimit;
                    int i4 = (i3 + i2) >>> 1;
                    if (i4 <= i2) {
                        break;
                    }
                    if (atomicReference.get() != null) {
                        return;
                    }
                    addToPendingCount(1);
                    int i5 = this.batch >>> 1;
                    this.batch = i5;
                    this.baseLimit = i4;
                    new SearchKeysTask(this, i5, i4, i3, this.tab, function, atomicReference).fork();
                }
                while (atomicReference.get() == null) {
                    Node<K, V> nodeAdvance = advance();
                    if (nodeAdvance == null) {
                        propagateCompletion();
                        return;
                    }
                    U uApply = function.apply(nodeAdvance.key);
                    if (uApply != null) {
                        if (atomicReference.compareAndSet(null, uApply)) {
                            quietlyCompleteRoot();
                            return;
                        }
                        return;
                    }
                }
            }
        }
    }

    /* loaded from: rt.jar:java/util/concurrent/ConcurrentHashMap$SearchValuesTask.class */
    static final class SearchValuesTask<K, V, U> extends BulkTask<K, V, U> {
        final Function<? super V, ? extends U> searchFunction;
        final AtomicReference<U> result;

        SearchValuesTask(BulkTask<K, V, ?> bulkTask, int i2, int i3, int i4, Node<K, V>[] nodeArr, Function<? super V, ? extends U> function, AtomicReference<U> atomicReference) {
            super(bulkTask, i2, i3, i4, nodeArr);
            this.searchFunction = function;
            this.result = atomicReference;
        }

        @Override // java.util.concurrent.CountedCompleter, java.util.concurrent.ForkJoinTask
        public final U getRawResult() {
            return this.result.get();
        }

        @Override // java.util.concurrent.CountedCompleter
        public final void compute() {
            AtomicReference<U> atomicReference;
            Function<? super V, ? extends U> function = this.searchFunction;
            if (function != null && (atomicReference = this.result) != null) {
                int i2 = this.baseIndex;
                while (this.batch > 0) {
                    int i3 = this.baseLimit;
                    int i4 = (i3 + i2) >>> 1;
                    if (i4 <= i2) {
                        break;
                    }
                    if (atomicReference.get() != null) {
                        return;
                    }
                    addToPendingCount(1);
                    int i5 = this.batch >>> 1;
                    this.batch = i5;
                    this.baseLimit = i4;
                    new SearchValuesTask(this, i5, i4, i3, this.tab, function, atomicReference).fork();
                }
                while (atomicReference.get() == null) {
                    Node<K, V> nodeAdvance = advance();
                    if (nodeAdvance == null) {
                        propagateCompletion();
                        return;
                    }
                    U uApply = function.apply(nodeAdvance.val);
                    if (uApply != null) {
                        if (atomicReference.compareAndSet(null, uApply)) {
                            quietlyCompleteRoot();
                            return;
                        }
                        return;
                    }
                }
            }
        }
    }

    /* loaded from: rt.jar:java/util/concurrent/ConcurrentHashMap$SearchEntriesTask.class */
    static final class SearchEntriesTask<K, V, U> extends BulkTask<K, V, U> {
        final Function<Map.Entry<K, V>, ? extends U> searchFunction;
        final AtomicReference<U> result;

        SearchEntriesTask(BulkTask<K, V, ?> bulkTask, int i2, int i3, int i4, Node<K, V>[] nodeArr, Function<Map.Entry<K, V>, ? extends U> function, AtomicReference<U> atomicReference) {
            super(bulkTask, i2, i3, i4, nodeArr);
            this.searchFunction = function;
            this.result = atomicReference;
        }

        @Override // java.util.concurrent.CountedCompleter, java.util.concurrent.ForkJoinTask
        public final U getRawResult() {
            return this.result.get();
        }

        @Override // java.util.concurrent.CountedCompleter
        public final void compute() {
            AtomicReference<U> atomicReference;
            Function<Map.Entry<K, V>, ? extends U> function = this.searchFunction;
            if (function != null && (atomicReference = this.result) != null) {
                int i2 = this.baseIndex;
                while (this.batch > 0) {
                    int i3 = this.baseLimit;
                    int i4 = (i3 + i2) >>> 1;
                    if (i4 <= i2) {
                        break;
                    }
                    if (atomicReference.get() != null) {
                        return;
                    }
                    addToPendingCount(1);
                    int i5 = this.batch >>> 1;
                    this.batch = i5;
                    this.baseLimit = i4;
                    new SearchEntriesTask(this, i5, i4, i3, this.tab, function, atomicReference).fork();
                }
                while (atomicReference.get() == null) {
                    Node<K, V> nodeAdvance = advance();
                    if (nodeAdvance == null) {
                        propagateCompletion();
                        return;
                    }
                    U uApply = function.apply(nodeAdvance);
                    if (uApply != null) {
                        if (atomicReference.compareAndSet(null, uApply)) {
                            quietlyCompleteRoot();
                            return;
                        }
                        return;
                    }
                }
            }
        }
    }

    /* loaded from: rt.jar:java/util/concurrent/ConcurrentHashMap$SearchMappingsTask.class */
    static final class SearchMappingsTask<K, V, U> extends BulkTask<K, V, U> {
        final BiFunction<? super K, ? super V, ? extends U> searchFunction;
        final AtomicReference<U> result;

        SearchMappingsTask(BulkTask<K, V, ?> bulkTask, int i2, int i3, int i4, Node<K, V>[] nodeArr, BiFunction<? super K, ? super V, ? extends U> biFunction, AtomicReference<U> atomicReference) {
            super(bulkTask, i2, i3, i4, nodeArr);
            this.searchFunction = biFunction;
            this.result = atomicReference;
        }

        @Override // java.util.concurrent.CountedCompleter, java.util.concurrent.ForkJoinTask
        public final U getRawResult() {
            return this.result.get();
        }

        @Override // java.util.concurrent.CountedCompleter
        public final void compute() {
            AtomicReference<U> atomicReference;
            BiFunction<? super K, ? super V, ? extends U> biFunction = this.searchFunction;
            if (biFunction != null && (atomicReference = this.result) != null) {
                int i2 = this.baseIndex;
                while (this.batch > 0) {
                    int i3 = this.baseLimit;
                    int i4 = (i3 + i2) >>> 1;
                    if (i4 <= i2) {
                        break;
                    }
                    if (atomicReference.get() != null) {
                        return;
                    }
                    addToPendingCount(1);
                    int i5 = this.batch >>> 1;
                    this.batch = i5;
                    this.baseLimit = i4;
                    new SearchMappingsTask(this, i5, i4, i3, this.tab, biFunction, atomicReference).fork();
                }
                while (atomicReference.get() == null) {
                    Node<K, V> nodeAdvance = advance();
                    if (nodeAdvance == null) {
                        propagateCompletion();
                        return;
                    }
                    U uApply = biFunction.apply(nodeAdvance.key, nodeAdvance.val);
                    if (uApply != null) {
                        if (atomicReference.compareAndSet(null, uApply)) {
                            quietlyCompleteRoot();
                            return;
                        }
                        return;
                    }
                }
            }
        }
    }

    /* loaded from: rt.jar:java/util/concurrent/ConcurrentHashMap$ReduceKeysTask.class */
    static final class ReduceKeysTask<K, V> extends BulkTask<K, V, K> {
        final BiFunction<? super K, ? super K, ? extends K> reducer;
        K result;
        ReduceKeysTask<K, V> rights;
        ReduceKeysTask<K, V> nextRight;

        ReduceKeysTask(BulkTask<K, V, ?> bulkTask, int i2, int i3, int i4, Node<K, V>[] nodeArr, ReduceKeysTask<K, V> reduceKeysTask, BiFunction<? super K, ? super K, ? extends K> biFunction) {
            super(bulkTask, i2, i3, i4, nodeArr);
            this.nextRight = reduceKeysTask;
            this.reducer = biFunction;
        }

        @Override // java.util.concurrent.CountedCompleter, java.util.concurrent.ForkJoinTask
        public final K getRawResult() {
            return this.result;
        }

        /* JADX WARN: Multi-variable type inference failed */
        /* JADX WARN: Type inference failed for: r0v24, types: [K, java.lang.Object] */
        /* JADX WARN: Type inference failed for: r13v1, types: [K, java.lang.Object] */
        @Override // java.util.concurrent.CountedCompleter
        public final void compute() {
            ?? r13;
            BiFunction<? super K, ? super K, ? extends K> biFunction = this.reducer;
            if (biFunction != null) {
                int i2 = this.baseIndex;
                while (this.batch > 0) {
                    int i3 = this.baseLimit;
                    int i4 = (i3 + i2) >>> 1;
                    if (i4 <= i2) {
                        break;
                    }
                    addToPendingCount(1);
                    int i5 = this.batch >>> 1;
                    this.batch = i5;
                    this.baseLimit = i4;
                    ReduceKeysTask<K, V> reduceKeysTask = new ReduceKeysTask<>(this, i5, i4, i3, this.tab, this.rights, biFunction);
                    this.rights = reduceKeysTask;
                    reduceKeysTask.fork();
                }
                Object objApply = null;
                while (true) {
                    r13 = (Object) objApply;
                    Node<K, V> nodeAdvance = advance();
                    if (nodeAdvance == null) {
                        break;
                    }
                    Object obj = (K) nodeAdvance.key;
                    objApply = r13 == 0 ? obj : obj == null ? r13 : biFunction.apply(r13, obj);
                }
                this.result = r13;
                CountedCompleter<?> countedCompleterFirstComplete = firstComplete();
                while (true) {
                    CountedCompleter<?> countedCompleter = countedCompleterFirstComplete;
                    if (countedCompleter != null) {
                        ReduceKeysTask reduceKeysTask2 = (ReduceKeysTask) countedCompleter;
                        ReduceKeysTask<K, V> reduceKeysTask3 = reduceKeysTask2.rights;
                        while (true) {
                            ReduceKeysTask<K, V> reduceKeysTask4 = reduceKeysTask3;
                            if (reduceKeysTask4 != null) {
                                K k2 = reduceKeysTask4.result;
                                if (k2 != 0) {
                                    Object obj2 = (K) reduceKeysTask2.result;
                                    reduceKeysTask2.result = obj2 == null ? k2 : biFunction.apply(obj2, k2);
                                }
                                ReduceKeysTask<K, V> reduceKeysTask5 = reduceKeysTask4.nextRight;
                                reduceKeysTask3 = reduceKeysTask5;
                                reduceKeysTask2.rights = reduceKeysTask5;
                            }
                        }
                        countedCompleterFirstComplete = countedCompleter.nextComplete();
                    } else {
                        return;
                    }
                }
            }
        }
    }

    /* loaded from: rt.jar:java/util/concurrent/ConcurrentHashMap$ReduceValuesTask.class */
    static final class ReduceValuesTask<K, V> extends BulkTask<K, V, V> {
        final BiFunction<? super V, ? super V, ? extends V> reducer;
        V result;
        ReduceValuesTask<K, V> rights;
        ReduceValuesTask<K, V> nextRight;

        ReduceValuesTask(BulkTask<K, V, ?> bulkTask, int i2, int i3, int i4, Node<K, V>[] nodeArr, ReduceValuesTask<K, V> reduceValuesTask, BiFunction<? super V, ? super V, ? extends V> biFunction) {
            super(bulkTask, i2, i3, i4, nodeArr);
            this.nextRight = reduceValuesTask;
            this.reducer = biFunction;
        }

        @Override // java.util.concurrent.CountedCompleter, java.util.concurrent.ForkJoinTask
        public final V getRawResult() {
            return this.result;
        }

        /* JADX WARN: Multi-variable type inference failed */
        /* JADX WARN: Type inference failed for: r0v24, types: [V, java.lang.Object] */
        /* JADX WARN: Type inference failed for: r13v1, types: [V, java.lang.Object] */
        @Override // java.util.concurrent.CountedCompleter
        public final void compute() {
            ?? r13;
            BiFunction<? super V, ? super V, ? extends V> biFunction = this.reducer;
            if (biFunction != null) {
                int i2 = this.baseIndex;
                while (this.batch > 0) {
                    int i3 = this.baseLimit;
                    int i4 = (i3 + i2) >>> 1;
                    if (i4 <= i2) {
                        break;
                    }
                    addToPendingCount(1);
                    int i5 = this.batch >>> 1;
                    this.batch = i5;
                    this.baseLimit = i4;
                    ReduceValuesTask<K, V> reduceValuesTask = new ReduceValuesTask<>(this, i5, i4, i3, this.tab, this.rights, biFunction);
                    this.rights = reduceValuesTask;
                    reduceValuesTask.fork();
                }
                Object objApply = null;
                while (true) {
                    r13 = (Object) objApply;
                    Node<K, V> nodeAdvance = advance();
                    if (nodeAdvance == null) {
                        break;
                    }
                    Object obj = (V) nodeAdvance.val;
                    objApply = r13 == 0 ? obj : biFunction.apply(r13, obj);
                }
                this.result = r13;
                CountedCompleter<?> countedCompleterFirstComplete = firstComplete();
                while (true) {
                    CountedCompleter<?> countedCompleter = countedCompleterFirstComplete;
                    if (countedCompleter != null) {
                        ReduceValuesTask reduceValuesTask2 = (ReduceValuesTask) countedCompleter;
                        ReduceValuesTask<K, V> reduceValuesTask3 = reduceValuesTask2.rights;
                        while (true) {
                            ReduceValuesTask<K, V> reduceValuesTask4 = reduceValuesTask3;
                            if (reduceValuesTask4 != null) {
                                V v2 = reduceValuesTask4.result;
                                if (v2 != 0) {
                                    Object obj2 = (V) reduceValuesTask2.result;
                                    reduceValuesTask2.result = obj2 == null ? v2 : biFunction.apply(obj2, v2);
                                }
                                ReduceValuesTask<K, V> reduceValuesTask5 = reduceValuesTask4.nextRight;
                                reduceValuesTask3 = reduceValuesTask5;
                                reduceValuesTask2.rights = reduceValuesTask5;
                            }
                        }
                        countedCompleterFirstComplete = countedCompleter.nextComplete();
                    } else {
                        return;
                    }
                }
            }
        }
    }

    /* loaded from: rt.jar:java/util/concurrent/ConcurrentHashMap$ReduceEntriesTask.class */
    static final class ReduceEntriesTask<K, V> extends BulkTask<K, V, Map.Entry<K, V>> {
        final BiFunction<Map.Entry<K, V>, Map.Entry<K, V>, ? extends Map.Entry<K, V>> reducer;
        Map.Entry<K, V> result;
        ReduceEntriesTask<K, V> rights;
        ReduceEntriesTask<K, V> nextRight;

        ReduceEntriesTask(BulkTask<K, V, ?> bulkTask, int i2, int i3, int i4, Node<K, V>[] nodeArr, ReduceEntriesTask<K, V> reduceEntriesTask, BiFunction<Map.Entry<K, V>, Map.Entry<K, V>, ? extends Map.Entry<K, V>> biFunction) {
            super(bulkTask, i2, i3, i4, nodeArr);
            this.nextRight = reduceEntriesTask;
            this.reducer = biFunction;
        }

        @Override // java.util.concurrent.CountedCompleter, java.util.concurrent.ForkJoinTask
        public final Map.Entry<K, V> getRawResult() {
            return this.result;
        }

        @Override // java.util.concurrent.CountedCompleter
        public final void compute() {
            Map.Entry<K, V> entry;
            BiFunction<Map.Entry<K, V>, Map.Entry<K, V>, ? extends Map.Entry<K, V>> biFunction = this.reducer;
            if (biFunction != null) {
                int i2 = this.baseIndex;
                while (this.batch > 0) {
                    int i3 = this.baseLimit;
                    int i4 = (i3 + i2) >>> 1;
                    if (i4 <= i2) {
                        break;
                    }
                    addToPendingCount(1);
                    int i5 = this.batch >>> 1;
                    this.batch = i5;
                    this.baseLimit = i4;
                    ReduceEntriesTask<K, V> reduceEntriesTask = new ReduceEntriesTask<>(this, i5, i4, i3, this.tab, this.rights, biFunction);
                    this.rights = reduceEntriesTask;
                    reduceEntriesTask.fork();
                }
                Map.Entry<K, V> entryApply = null;
                while (true) {
                    entry = entryApply;
                    Node<K, V> nodeAdvance = advance();
                    if (nodeAdvance == null) {
                        break;
                    } else {
                        entryApply = entry == null ? nodeAdvance : biFunction.apply(entry, nodeAdvance);
                    }
                }
                this.result = entry;
                CountedCompleter<?> countedCompleterFirstComplete = firstComplete();
                while (true) {
                    CountedCompleter<?> countedCompleter = countedCompleterFirstComplete;
                    if (countedCompleter != null) {
                        ReduceEntriesTask reduceEntriesTask2 = (ReduceEntriesTask) countedCompleter;
                        ReduceEntriesTask<K, V> reduceEntriesTask3 = reduceEntriesTask2.rights;
                        while (true) {
                            ReduceEntriesTask<K, V> reduceEntriesTask4 = reduceEntriesTask3;
                            if (reduceEntriesTask4 != null) {
                                Map.Entry<K, V> entry2 = reduceEntriesTask4.result;
                                if (entry2 != null) {
                                    Map.Entry<K, V> entry3 = reduceEntriesTask2.result;
                                    reduceEntriesTask2.result = entry3 == null ? entry2 : biFunction.apply(entry3, entry2);
                                }
                                ReduceEntriesTask<K, V> reduceEntriesTask5 = reduceEntriesTask4.nextRight;
                                reduceEntriesTask3 = reduceEntriesTask5;
                                reduceEntriesTask2.rights = reduceEntriesTask5;
                            }
                        }
                        countedCompleterFirstComplete = countedCompleter.nextComplete();
                    } else {
                        return;
                    }
                }
            }
        }
    }

    /* loaded from: rt.jar:java/util/concurrent/ConcurrentHashMap$MapReduceKeysTask.class */
    static final class MapReduceKeysTask<K, V, U> extends BulkTask<K, V, U> {
        final Function<? super K, ? extends U> transformer;
        final BiFunction<? super U, ? super U, ? extends U> reducer;
        U result;
        MapReduceKeysTask<K, V, U> rights;
        MapReduceKeysTask<K, V, U> nextRight;

        MapReduceKeysTask(BulkTask<K, V, ?> bulkTask, int i2, int i3, int i4, Node<K, V>[] nodeArr, MapReduceKeysTask<K, V, U> mapReduceKeysTask, Function<? super K, ? extends U> function, BiFunction<? super U, ? super U, ? extends U> biFunction) {
            super(bulkTask, i2, i3, i4, nodeArr);
            this.nextRight = mapReduceKeysTask;
            this.transformer = function;
            this.reducer = biFunction;
        }

        @Override // java.util.concurrent.CountedCompleter, java.util.concurrent.ForkJoinTask
        public final U getRawResult() {
            return this.result;
        }

        /* JADX WARN: Multi-variable type inference failed */
        /* JADX WARN: Type inference failed for: r0v25, types: [U, java.lang.Object] */
        /* JADX WARN: Type inference failed for: r0v30, types: [java.lang.Object] */
        @Override // java.util.concurrent.CountedCompleter
        public final void compute() {
            BiFunction<? super U, ? super U, ? extends U> biFunction;
            Function<? super K, ? extends U> function = this.transformer;
            if (function != null && (biFunction = this.reducer) != null) {
                int i2 = this.baseIndex;
                while (this.batch > 0) {
                    int i3 = this.baseLimit;
                    int i4 = (i3 + i2) >>> 1;
                    if (i4 <= i2) {
                        break;
                    }
                    addToPendingCount(1);
                    int i5 = this.batch >>> 1;
                    this.batch = i5;
                    this.baseLimit = i4;
                    MapReduceKeysTask<K, V, U> mapReduceKeysTask = new MapReduceKeysTask<>(this, i5, i4, i3, this.tab, this.rights, function, biFunction);
                    this.rights = mapReduceKeysTask;
                    mapReduceKeysTask.fork();
                }
                U uApply = null;
                while (true) {
                    Node<K, V> nodeAdvance = advance();
                    if (nodeAdvance == null) {
                        break;
                    }
                    U uApply2 = function.apply((K) nodeAdvance.key);
                    if (uApply2 != 0) {
                        uApply = uApply == null ? uApply2 : biFunction.apply((Object) uApply, uApply2);
                    }
                }
                this.result = uApply;
                CountedCompleter<?> countedCompleterFirstComplete = firstComplete();
                while (true) {
                    CountedCompleter<?> countedCompleter = countedCompleterFirstComplete;
                    if (countedCompleter != null) {
                        MapReduceKeysTask mapReduceKeysTask2 = (MapReduceKeysTask) countedCompleter;
                        MapReduceKeysTask<K, V, U> mapReduceKeysTask3 = mapReduceKeysTask2.rights;
                        while (true) {
                            MapReduceKeysTask<K, V, U> mapReduceKeysTask4 = mapReduceKeysTask3;
                            if (mapReduceKeysTask4 != null) {
                                U u2 = mapReduceKeysTask4.result;
                                if (u2 != 0) {
                                    Object obj = (U) mapReduceKeysTask2.result;
                                    mapReduceKeysTask2.result = obj == null ? u2 : biFunction.apply(obj, u2);
                                }
                                MapReduceKeysTask<K, V, U> mapReduceKeysTask5 = mapReduceKeysTask4.nextRight;
                                mapReduceKeysTask3 = mapReduceKeysTask5;
                                mapReduceKeysTask2.rights = mapReduceKeysTask5;
                            }
                        }
                        countedCompleterFirstComplete = countedCompleter.nextComplete();
                    } else {
                        return;
                    }
                }
            }
        }
    }

    /* loaded from: rt.jar:java/util/concurrent/ConcurrentHashMap$MapReduceValuesTask.class */
    static final class MapReduceValuesTask<K, V, U> extends BulkTask<K, V, U> {
        final Function<? super V, ? extends U> transformer;
        final BiFunction<? super U, ? super U, ? extends U> reducer;
        U result;
        MapReduceValuesTask<K, V, U> rights;
        MapReduceValuesTask<K, V, U> nextRight;

        MapReduceValuesTask(BulkTask<K, V, ?> bulkTask, int i2, int i3, int i4, Node<K, V>[] nodeArr, MapReduceValuesTask<K, V, U> mapReduceValuesTask, Function<? super V, ? extends U> function, BiFunction<? super U, ? super U, ? extends U> biFunction) {
            super(bulkTask, i2, i3, i4, nodeArr);
            this.nextRight = mapReduceValuesTask;
            this.transformer = function;
            this.reducer = biFunction;
        }

        @Override // java.util.concurrent.CountedCompleter, java.util.concurrent.ForkJoinTask
        public final U getRawResult() {
            return this.result;
        }

        /* JADX WARN: Multi-variable type inference failed */
        /* JADX WARN: Type inference failed for: r0v25, types: [U, java.lang.Object] */
        /* JADX WARN: Type inference failed for: r0v30, types: [java.lang.Object] */
        @Override // java.util.concurrent.CountedCompleter
        public final void compute() {
            BiFunction<? super U, ? super U, ? extends U> biFunction;
            Function<? super V, ? extends U> function = this.transformer;
            if (function != null && (biFunction = this.reducer) != null) {
                int i2 = this.baseIndex;
                while (this.batch > 0) {
                    int i3 = this.baseLimit;
                    int i4 = (i3 + i2) >>> 1;
                    if (i4 <= i2) {
                        break;
                    }
                    addToPendingCount(1);
                    int i5 = this.batch >>> 1;
                    this.batch = i5;
                    this.baseLimit = i4;
                    MapReduceValuesTask<K, V, U> mapReduceValuesTask = new MapReduceValuesTask<>(this, i5, i4, i3, this.tab, this.rights, function, biFunction);
                    this.rights = mapReduceValuesTask;
                    mapReduceValuesTask.fork();
                }
                U uApply = null;
                while (true) {
                    Node<K, V> nodeAdvance = advance();
                    if (nodeAdvance == null) {
                        break;
                    }
                    U uApply2 = function.apply((V) nodeAdvance.val);
                    if (uApply2 != 0) {
                        uApply = uApply == null ? uApply2 : biFunction.apply((Object) uApply, uApply2);
                    }
                }
                this.result = uApply;
                CountedCompleter<?> countedCompleterFirstComplete = firstComplete();
                while (true) {
                    CountedCompleter<?> countedCompleter = countedCompleterFirstComplete;
                    if (countedCompleter != null) {
                        MapReduceValuesTask mapReduceValuesTask2 = (MapReduceValuesTask) countedCompleter;
                        MapReduceValuesTask<K, V, U> mapReduceValuesTask3 = mapReduceValuesTask2.rights;
                        while (true) {
                            MapReduceValuesTask<K, V, U> mapReduceValuesTask4 = mapReduceValuesTask3;
                            if (mapReduceValuesTask4 != null) {
                                U u2 = mapReduceValuesTask4.result;
                                if (u2 != 0) {
                                    Object obj = (U) mapReduceValuesTask2.result;
                                    mapReduceValuesTask2.result = obj == null ? u2 : biFunction.apply(obj, u2);
                                }
                                MapReduceValuesTask<K, V, U> mapReduceValuesTask5 = mapReduceValuesTask4.nextRight;
                                mapReduceValuesTask3 = mapReduceValuesTask5;
                                mapReduceValuesTask2.rights = mapReduceValuesTask5;
                            }
                        }
                        countedCompleterFirstComplete = countedCompleter.nextComplete();
                    } else {
                        return;
                    }
                }
            }
        }
    }

    /* loaded from: rt.jar:java/util/concurrent/ConcurrentHashMap$MapReduceEntriesTask.class */
    static final class MapReduceEntriesTask<K, V, U> extends BulkTask<K, V, U> {
        final Function<Map.Entry<K, V>, ? extends U> transformer;
        final BiFunction<? super U, ? super U, ? extends U> reducer;
        U result;
        MapReduceEntriesTask<K, V, U> rights;
        MapReduceEntriesTask<K, V, U> nextRight;

        MapReduceEntriesTask(BulkTask<K, V, ?> bulkTask, int i2, int i3, int i4, Node<K, V>[] nodeArr, MapReduceEntriesTask<K, V, U> mapReduceEntriesTask, Function<Map.Entry<K, V>, ? extends U> function, BiFunction<? super U, ? super U, ? extends U> biFunction) {
            super(bulkTask, i2, i3, i4, nodeArr);
            this.nextRight = mapReduceEntriesTask;
            this.transformer = function;
            this.reducer = biFunction;
        }

        @Override // java.util.concurrent.CountedCompleter, java.util.concurrent.ForkJoinTask
        public final U getRawResult() {
            return this.result;
        }

        /* JADX WARN: Multi-variable type inference failed */
        /* JADX WARN: Type inference failed for: r0v25, types: [U, java.lang.Object] */
        /* JADX WARN: Type inference failed for: r0v30, types: [java.lang.Object] */
        @Override // java.util.concurrent.CountedCompleter
        public final void compute() {
            BiFunction<? super U, ? super U, ? extends U> biFunction;
            Function<Map.Entry<K, V>, ? extends U> function = this.transformer;
            if (function != null && (biFunction = this.reducer) != null) {
                int i2 = this.baseIndex;
                while (this.batch > 0) {
                    int i3 = this.baseLimit;
                    int i4 = (i3 + i2) >>> 1;
                    if (i4 <= i2) {
                        break;
                    }
                    addToPendingCount(1);
                    int i5 = this.batch >>> 1;
                    this.batch = i5;
                    this.baseLimit = i4;
                    MapReduceEntriesTask<K, V, U> mapReduceEntriesTask = new MapReduceEntriesTask<>(this, i5, i4, i3, this.tab, this.rights, function, biFunction);
                    this.rights = mapReduceEntriesTask;
                    mapReduceEntriesTask.fork();
                }
                U uApply = null;
                while (true) {
                    Node<K, V> nodeAdvance = advance();
                    if (nodeAdvance == null) {
                        break;
                    }
                    U uApply2 = function.apply(nodeAdvance);
                    if (uApply2 != 0) {
                        uApply = uApply == null ? uApply2 : biFunction.apply((Object) uApply, uApply2);
                    }
                }
                this.result = uApply;
                CountedCompleter<?> countedCompleterFirstComplete = firstComplete();
                while (true) {
                    CountedCompleter<?> countedCompleter = countedCompleterFirstComplete;
                    if (countedCompleter != null) {
                        MapReduceEntriesTask mapReduceEntriesTask2 = (MapReduceEntriesTask) countedCompleter;
                        MapReduceEntriesTask<K, V, U> mapReduceEntriesTask3 = mapReduceEntriesTask2.rights;
                        while (true) {
                            MapReduceEntriesTask<K, V, U> mapReduceEntriesTask4 = mapReduceEntriesTask3;
                            if (mapReduceEntriesTask4 != null) {
                                U u2 = mapReduceEntriesTask4.result;
                                if (u2 != 0) {
                                    Object obj = (U) mapReduceEntriesTask2.result;
                                    mapReduceEntriesTask2.result = obj == null ? u2 : biFunction.apply(obj, u2);
                                }
                                MapReduceEntriesTask<K, V, U> mapReduceEntriesTask5 = mapReduceEntriesTask4.nextRight;
                                mapReduceEntriesTask3 = mapReduceEntriesTask5;
                                mapReduceEntriesTask2.rights = mapReduceEntriesTask5;
                            }
                        }
                        countedCompleterFirstComplete = countedCompleter.nextComplete();
                    } else {
                        return;
                    }
                }
            }
        }
    }

    /* loaded from: rt.jar:java/util/concurrent/ConcurrentHashMap$MapReduceMappingsTask.class */
    static final class MapReduceMappingsTask<K, V, U> extends BulkTask<K, V, U> {
        final BiFunction<? super K, ? super V, ? extends U> transformer;
        final BiFunction<? super U, ? super U, ? extends U> reducer;
        U result;
        MapReduceMappingsTask<K, V, U> rights;
        MapReduceMappingsTask<K, V, U> nextRight;

        MapReduceMappingsTask(BulkTask<K, V, ?> bulkTask, int i2, int i3, int i4, Node<K, V>[] nodeArr, MapReduceMappingsTask<K, V, U> mapReduceMappingsTask, BiFunction<? super K, ? super V, ? extends U> biFunction, BiFunction<? super U, ? super U, ? extends U> biFunction2) {
            super(bulkTask, i2, i3, i4, nodeArr);
            this.nextRight = mapReduceMappingsTask;
            this.transformer = biFunction;
            this.reducer = biFunction2;
        }

        @Override // java.util.concurrent.CountedCompleter, java.util.concurrent.ForkJoinTask
        public final U getRawResult() {
            return this.result;
        }

        /* JADX WARN: Multi-variable type inference failed */
        /* JADX WARN: Type inference failed for: r0v25, types: [U, java.lang.Object] */
        /* JADX WARN: Type inference failed for: r0v30, types: [java.lang.Object] */
        @Override // java.util.concurrent.CountedCompleter
        public final void compute() {
            BiFunction<? super U, ? super U, ? extends U> biFunction;
            BiFunction<? super K, ? super V, ? extends U> biFunction2 = this.transformer;
            if (biFunction2 != null && (biFunction = this.reducer) != null) {
                int i2 = this.baseIndex;
                while (this.batch > 0) {
                    int i3 = this.baseLimit;
                    int i4 = (i3 + i2) >>> 1;
                    if (i4 <= i2) {
                        break;
                    }
                    addToPendingCount(1);
                    int i5 = this.batch >>> 1;
                    this.batch = i5;
                    this.baseLimit = i4;
                    MapReduceMappingsTask<K, V, U> mapReduceMappingsTask = new MapReduceMappingsTask<>(this, i5, i4, i3, this.tab, this.rights, biFunction2, biFunction);
                    this.rights = mapReduceMappingsTask;
                    mapReduceMappingsTask.fork();
                }
                U uApply = null;
                while (true) {
                    Node<K, V> nodeAdvance = advance();
                    if (nodeAdvance == null) {
                        break;
                    }
                    U uApply2 = biFunction2.apply((K) nodeAdvance.key, (V) nodeAdvance.val);
                    if (uApply2 != 0) {
                        uApply = uApply == null ? uApply2 : biFunction.apply((Object) uApply, uApply2);
                    }
                }
                this.result = uApply;
                CountedCompleter<?> countedCompleterFirstComplete = firstComplete();
                while (true) {
                    CountedCompleter<?> countedCompleter = countedCompleterFirstComplete;
                    if (countedCompleter != null) {
                        MapReduceMappingsTask mapReduceMappingsTask2 = (MapReduceMappingsTask) countedCompleter;
                        MapReduceMappingsTask<K, V, U> mapReduceMappingsTask3 = mapReduceMappingsTask2.rights;
                        while (true) {
                            MapReduceMappingsTask<K, V, U> mapReduceMappingsTask4 = mapReduceMappingsTask3;
                            if (mapReduceMappingsTask4 != null) {
                                U u2 = mapReduceMappingsTask4.result;
                                if (u2 != 0) {
                                    Object obj = (U) mapReduceMappingsTask2.result;
                                    mapReduceMappingsTask2.result = obj == null ? u2 : biFunction.apply(obj, u2);
                                }
                                MapReduceMappingsTask<K, V, U> mapReduceMappingsTask5 = mapReduceMappingsTask4.nextRight;
                                mapReduceMappingsTask3 = mapReduceMappingsTask5;
                                mapReduceMappingsTask2.rights = mapReduceMappingsTask5;
                            }
                        }
                        countedCompleterFirstComplete = countedCompleter.nextComplete();
                    } else {
                        return;
                    }
                }
            }
        }
    }

    /* loaded from: rt.jar:java/util/concurrent/ConcurrentHashMap$MapReduceKeysToDoubleTask.class */
    static final class MapReduceKeysToDoubleTask<K, V> extends BulkTask<K, V, Double> {
        final ToDoubleFunction<? super K> transformer;
        final DoubleBinaryOperator reducer;
        final double basis;
        double result;
        MapReduceKeysToDoubleTask<K, V> rights;
        MapReduceKeysToDoubleTask<K, V> nextRight;

        MapReduceKeysToDoubleTask(BulkTask<K, V, ?> bulkTask, int i2, int i3, int i4, Node<K, V>[] nodeArr, MapReduceKeysToDoubleTask<K, V> mapReduceKeysToDoubleTask, ToDoubleFunction<? super K> toDoubleFunction, double d2, DoubleBinaryOperator doubleBinaryOperator) {
            super(bulkTask, i2, i3, i4, nodeArr);
            this.nextRight = mapReduceKeysToDoubleTask;
            this.transformer = toDoubleFunction;
            this.basis = d2;
            this.reducer = doubleBinaryOperator;
        }

        @Override // java.util.concurrent.CountedCompleter, java.util.concurrent.ForkJoinTask
        public final Double getRawResult() {
            return Double.valueOf(this.result);
        }

        @Override // java.util.concurrent.CountedCompleter
        public final void compute() {
            DoubleBinaryOperator doubleBinaryOperator;
            ToDoubleFunction<? super K> toDoubleFunction = this.transformer;
            if (toDoubleFunction != null && (doubleBinaryOperator = this.reducer) != null) {
                double dApplyAsDouble = this.basis;
                int i2 = this.baseIndex;
                while (this.batch > 0) {
                    int i3 = this.baseLimit;
                    int i4 = (i3 + i2) >>> 1;
                    if (i4 <= i2) {
                        break;
                    }
                    addToPendingCount(1);
                    int i5 = this.batch >>> 1;
                    this.batch = i5;
                    this.baseLimit = i4;
                    MapReduceKeysToDoubleTask<K, V> mapReduceKeysToDoubleTask = new MapReduceKeysToDoubleTask<>(this, i5, i4, i3, this.tab, this.rights, toDoubleFunction, dApplyAsDouble, doubleBinaryOperator);
                    this.rights = mapReduceKeysToDoubleTask;
                    mapReduceKeysToDoubleTask.fork();
                }
                while (true) {
                    Node<K, V> nodeAdvance = advance();
                    if (nodeAdvance == null) {
                        break;
                    } else {
                        dApplyAsDouble = doubleBinaryOperator.applyAsDouble(dApplyAsDouble, toDoubleFunction.applyAsDouble(nodeAdvance.key));
                    }
                }
                this.result = dApplyAsDouble;
                CountedCompleter<?> countedCompleterFirstComplete = firstComplete();
                while (true) {
                    CountedCompleter<?> countedCompleter = countedCompleterFirstComplete;
                    if (countedCompleter != null) {
                        MapReduceKeysToDoubleTask mapReduceKeysToDoubleTask2 = (MapReduceKeysToDoubleTask) countedCompleter;
                        MapReduceKeysToDoubleTask<K, V> mapReduceKeysToDoubleTask3 = mapReduceKeysToDoubleTask2.rights;
                        while (true) {
                            MapReduceKeysToDoubleTask<K, V> mapReduceKeysToDoubleTask4 = mapReduceKeysToDoubleTask3;
                            if (mapReduceKeysToDoubleTask4 != null) {
                                mapReduceKeysToDoubleTask2.result = doubleBinaryOperator.applyAsDouble(mapReduceKeysToDoubleTask2.result, mapReduceKeysToDoubleTask4.result);
                                MapReduceKeysToDoubleTask<K, V> mapReduceKeysToDoubleTask5 = mapReduceKeysToDoubleTask4.nextRight;
                                mapReduceKeysToDoubleTask3 = mapReduceKeysToDoubleTask5;
                                mapReduceKeysToDoubleTask2.rights = mapReduceKeysToDoubleTask5;
                            }
                        }
                        countedCompleterFirstComplete = countedCompleter.nextComplete();
                    } else {
                        return;
                    }
                }
            }
        }
    }

    /* loaded from: rt.jar:java/util/concurrent/ConcurrentHashMap$MapReduceValuesToDoubleTask.class */
    static final class MapReduceValuesToDoubleTask<K, V> extends BulkTask<K, V, Double> {
        final ToDoubleFunction<? super V> transformer;
        final DoubleBinaryOperator reducer;
        final double basis;
        double result;
        MapReduceValuesToDoubleTask<K, V> rights;
        MapReduceValuesToDoubleTask<K, V> nextRight;

        MapReduceValuesToDoubleTask(BulkTask<K, V, ?> bulkTask, int i2, int i3, int i4, Node<K, V>[] nodeArr, MapReduceValuesToDoubleTask<K, V> mapReduceValuesToDoubleTask, ToDoubleFunction<? super V> toDoubleFunction, double d2, DoubleBinaryOperator doubleBinaryOperator) {
            super(bulkTask, i2, i3, i4, nodeArr);
            this.nextRight = mapReduceValuesToDoubleTask;
            this.transformer = toDoubleFunction;
            this.basis = d2;
            this.reducer = doubleBinaryOperator;
        }

        @Override // java.util.concurrent.CountedCompleter, java.util.concurrent.ForkJoinTask
        public final Double getRawResult() {
            return Double.valueOf(this.result);
        }

        @Override // java.util.concurrent.CountedCompleter
        public final void compute() {
            DoubleBinaryOperator doubleBinaryOperator;
            ToDoubleFunction<? super V> toDoubleFunction = this.transformer;
            if (toDoubleFunction != null && (doubleBinaryOperator = this.reducer) != null) {
                double dApplyAsDouble = this.basis;
                int i2 = this.baseIndex;
                while (this.batch > 0) {
                    int i3 = this.baseLimit;
                    int i4 = (i3 + i2) >>> 1;
                    if (i4 <= i2) {
                        break;
                    }
                    addToPendingCount(1);
                    int i5 = this.batch >>> 1;
                    this.batch = i5;
                    this.baseLimit = i4;
                    MapReduceValuesToDoubleTask<K, V> mapReduceValuesToDoubleTask = new MapReduceValuesToDoubleTask<>(this, i5, i4, i3, this.tab, this.rights, toDoubleFunction, dApplyAsDouble, doubleBinaryOperator);
                    this.rights = mapReduceValuesToDoubleTask;
                    mapReduceValuesToDoubleTask.fork();
                }
                while (true) {
                    Node<K, V> nodeAdvance = advance();
                    if (nodeAdvance == null) {
                        break;
                    } else {
                        dApplyAsDouble = doubleBinaryOperator.applyAsDouble(dApplyAsDouble, toDoubleFunction.applyAsDouble(nodeAdvance.val));
                    }
                }
                this.result = dApplyAsDouble;
                CountedCompleter<?> countedCompleterFirstComplete = firstComplete();
                while (true) {
                    CountedCompleter<?> countedCompleter = countedCompleterFirstComplete;
                    if (countedCompleter != null) {
                        MapReduceValuesToDoubleTask mapReduceValuesToDoubleTask2 = (MapReduceValuesToDoubleTask) countedCompleter;
                        MapReduceValuesToDoubleTask<K, V> mapReduceValuesToDoubleTask3 = mapReduceValuesToDoubleTask2.rights;
                        while (true) {
                            MapReduceValuesToDoubleTask<K, V> mapReduceValuesToDoubleTask4 = mapReduceValuesToDoubleTask3;
                            if (mapReduceValuesToDoubleTask4 != null) {
                                mapReduceValuesToDoubleTask2.result = doubleBinaryOperator.applyAsDouble(mapReduceValuesToDoubleTask2.result, mapReduceValuesToDoubleTask4.result);
                                MapReduceValuesToDoubleTask<K, V> mapReduceValuesToDoubleTask5 = mapReduceValuesToDoubleTask4.nextRight;
                                mapReduceValuesToDoubleTask3 = mapReduceValuesToDoubleTask5;
                                mapReduceValuesToDoubleTask2.rights = mapReduceValuesToDoubleTask5;
                            }
                        }
                        countedCompleterFirstComplete = countedCompleter.nextComplete();
                    } else {
                        return;
                    }
                }
            }
        }
    }

    /* loaded from: rt.jar:java/util/concurrent/ConcurrentHashMap$MapReduceEntriesToDoubleTask.class */
    static final class MapReduceEntriesToDoubleTask<K, V> extends BulkTask<K, V, Double> {
        final ToDoubleFunction<Map.Entry<K, V>> transformer;
        final DoubleBinaryOperator reducer;
        final double basis;
        double result;
        MapReduceEntriesToDoubleTask<K, V> rights;
        MapReduceEntriesToDoubleTask<K, V> nextRight;

        MapReduceEntriesToDoubleTask(BulkTask<K, V, ?> bulkTask, int i2, int i3, int i4, Node<K, V>[] nodeArr, MapReduceEntriesToDoubleTask<K, V> mapReduceEntriesToDoubleTask, ToDoubleFunction<Map.Entry<K, V>> toDoubleFunction, double d2, DoubleBinaryOperator doubleBinaryOperator) {
            super(bulkTask, i2, i3, i4, nodeArr);
            this.nextRight = mapReduceEntriesToDoubleTask;
            this.transformer = toDoubleFunction;
            this.basis = d2;
            this.reducer = doubleBinaryOperator;
        }

        @Override // java.util.concurrent.CountedCompleter, java.util.concurrent.ForkJoinTask
        public final Double getRawResult() {
            return Double.valueOf(this.result);
        }

        @Override // java.util.concurrent.CountedCompleter
        public final void compute() {
            DoubleBinaryOperator doubleBinaryOperator;
            ToDoubleFunction<Map.Entry<K, V>> toDoubleFunction = this.transformer;
            if (toDoubleFunction != null && (doubleBinaryOperator = this.reducer) != null) {
                double dApplyAsDouble = this.basis;
                int i2 = this.baseIndex;
                while (this.batch > 0) {
                    int i3 = this.baseLimit;
                    int i4 = (i3 + i2) >>> 1;
                    if (i4 <= i2) {
                        break;
                    }
                    addToPendingCount(1);
                    int i5 = this.batch >>> 1;
                    this.batch = i5;
                    this.baseLimit = i4;
                    MapReduceEntriesToDoubleTask<K, V> mapReduceEntriesToDoubleTask = new MapReduceEntriesToDoubleTask<>(this, i5, i4, i3, this.tab, this.rights, toDoubleFunction, dApplyAsDouble, doubleBinaryOperator);
                    this.rights = mapReduceEntriesToDoubleTask;
                    mapReduceEntriesToDoubleTask.fork();
                }
                while (true) {
                    Node<K, V> nodeAdvance = advance();
                    if (nodeAdvance == null) {
                        break;
                    } else {
                        dApplyAsDouble = doubleBinaryOperator.applyAsDouble(dApplyAsDouble, toDoubleFunction.applyAsDouble(nodeAdvance));
                    }
                }
                this.result = dApplyAsDouble;
                CountedCompleter<?> countedCompleterFirstComplete = firstComplete();
                while (true) {
                    CountedCompleter<?> countedCompleter = countedCompleterFirstComplete;
                    if (countedCompleter != null) {
                        MapReduceEntriesToDoubleTask mapReduceEntriesToDoubleTask2 = (MapReduceEntriesToDoubleTask) countedCompleter;
                        MapReduceEntriesToDoubleTask<K, V> mapReduceEntriesToDoubleTask3 = mapReduceEntriesToDoubleTask2.rights;
                        while (true) {
                            MapReduceEntriesToDoubleTask<K, V> mapReduceEntriesToDoubleTask4 = mapReduceEntriesToDoubleTask3;
                            if (mapReduceEntriesToDoubleTask4 != null) {
                                mapReduceEntriesToDoubleTask2.result = doubleBinaryOperator.applyAsDouble(mapReduceEntriesToDoubleTask2.result, mapReduceEntriesToDoubleTask4.result);
                                MapReduceEntriesToDoubleTask<K, V> mapReduceEntriesToDoubleTask5 = mapReduceEntriesToDoubleTask4.nextRight;
                                mapReduceEntriesToDoubleTask3 = mapReduceEntriesToDoubleTask5;
                                mapReduceEntriesToDoubleTask2.rights = mapReduceEntriesToDoubleTask5;
                            }
                        }
                        countedCompleterFirstComplete = countedCompleter.nextComplete();
                    } else {
                        return;
                    }
                }
            }
        }
    }

    /* loaded from: rt.jar:java/util/concurrent/ConcurrentHashMap$MapReduceMappingsToDoubleTask.class */
    static final class MapReduceMappingsToDoubleTask<K, V> extends BulkTask<K, V, Double> {
        final ToDoubleBiFunction<? super K, ? super V> transformer;
        final DoubleBinaryOperator reducer;
        final double basis;
        double result;
        MapReduceMappingsToDoubleTask<K, V> rights;
        MapReduceMappingsToDoubleTask<K, V> nextRight;

        MapReduceMappingsToDoubleTask(BulkTask<K, V, ?> bulkTask, int i2, int i3, int i4, Node<K, V>[] nodeArr, MapReduceMappingsToDoubleTask<K, V> mapReduceMappingsToDoubleTask, ToDoubleBiFunction<? super K, ? super V> toDoubleBiFunction, double d2, DoubleBinaryOperator doubleBinaryOperator) {
            super(bulkTask, i2, i3, i4, nodeArr);
            this.nextRight = mapReduceMappingsToDoubleTask;
            this.transformer = toDoubleBiFunction;
            this.basis = d2;
            this.reducer = doubleBinaryOperator;
        }

        @Override // java.util.concurrent.CountedCompleter, java.util.concurrent.ForkJoinTask
        public final Double getRawResult() {
            return Double.valueOf(this.result);
        }

        @Override // java.util.concurrent.CountedCompleter
        public final void compute() {
            DoubleBinaryOperator doubleBinaryOperator;
            ToDoubleBiFunction<? super K, ? super V> toDoubleBiFunction = this.transformer;
            if (toDoubleBiFunction != null && (doubleBinaryOperator = this.reducer) != null) {
                double dApplyAsDouble = this.basis;
                int i2 = this.baseIndex;
                while (this.batch > 0) {
                    int i3 = this.baseLimit;
                    int i4 = (i3 + i2) >>> 1;
                    if (i4 <= i2) {
                        break;
                    }
                    addToPendingCount(1);
                    int i5 = this.batch >>> 1;
                    this.batch = i5;
                    this.baseLimit = i4;
                    MapReduceMappingsToDoubleTask<K, V> mapReduceMappingsToDoubleTask = new MapReduceMappingsToDoubleTask<>(this, i5, i4, i3, this.tab, this.rights, toDoubleBiFunction, dApplyAsDouble, doubleBinaryOperator);
                    this.rights = mapReduceMappingsToDoubleTask;
                    mapReduceMappingsToDoubleTask.fork();
                }
                while (true) {
                    Node<K, V> nodeAdvance = advance();
                    if (nodeAdvance == null) {
                        break;
                    } else {
                        dApplyAsDouble = doubleBinaryOperator.applyAsDouble(dApplyAsDouble, toDoubleBiFunction.applyAsDouble(nodeAdvance.key, nodeAdvance.val));
                    }
                }
                this.result = dApplyAsDouble;
                CountedCompleter<?> countedCompleterFirstComplete = firstComplete();
                while (true) {
                    CountedCompleter<?> countedCompleter = countedCompleterFirstComplete;
                    if (countedCompleter != null) {
                        MapReduceMappingsToDoubleTask mapReduceMappingsToDoubleTask2 = (MapReduceMappingsToDoubleTask) countedCompleter;
                        MapReduceMappingsToDoubleTask<K, V> mapReduceMappingsToDoubleTask3 = mapReduceMappingsToDoubleTask2.rights;
                        while (true) {
                            MapReduceMappingsToDoubleTask<K, V> mapReduceMappingsToDoubleTask4 = mapReduceMappingsToDoubleTask3;
                            if (mapReduceMappingsToDoubleTask4 != null) {
                                mapReduceMappingsToDoubleTask2.result = doubleBinaryOperator.applyAsDouble(mapReduceMappingsToDoubleTask2.result, mapReduceMappingsToDoubleTask4.result);
                                MapReduceMappingsToDoubleTask<K, V> mapReduceMappingsToDoubleTask5 = mapReduceMappingsToDoubleTask4.nextRight;
                                mapReduceMappingsToDoubleTask3 = mapReduceMappingsToDoubleTask5;
                                mapReduceMappingsToDoubleTask2.rights = mapReduceMappingsToDoubleTask5;
                            }
                        }
                        countedCompleterFirstComplete = countedCompleter.nextComplete();
                    } else {
                        return;
                    }
                }
            }
        }
    }

    /* loaded from: rt.jar:java/util/concurrent/ConcurrentHashMap$MapReduceKeysToLongTask.class */
    static final class MapReduceKeysToLongTask<K, V> extends BulkTask<K, V, Long> {
        final ToLongFunction<? super K> transformer;
        final LongBinaryOperator reducer;
        final long basis;
        long result;
        MapReduceKeysToLongTask<K, V> rights;
        MapReduceKeysToLongTask<K, V> nextRight;

        MapReduceKeysToLongTask(BulkTask<K, V, ?> bulkTask, int i2, int i3, int i4, Node<K, V>[] nodeArr, MapReduceKeysToLongTask<K, V> mapReduceKeysToLongTask, ToLongFunction<? super K> toLongFunction, long j2, LongBinaryOperator longBinaryOperator) {
            super(bulkTask, i2, i3, i4, nodeArr);
            this.nextRight = mapReduceKeysToLongTask;
            this.transformer = toLongFunction;
            this.basis = j2;
            this.reducer = longBinaryOperator;
        }

        @Override // java.util.concurrent.CountedCompleter, java.util.concurrent.ForkJoinTask
        public final Long getRawResult() {
            return Long.valueOf(this.result);
        }

        @Override // java.util.concurrent.CountedCompleter
        public final void compute() {
            LongBinaryOperator longBinaryOperator;
            ToLongFunction<? super K> toLongFunction = this.transformer;
            if (toLongFunction != null && (longBinaryOperator = this.reducer) != null) {
                long jApplyAsLong = this.basis;
                int i2 = this.baseIndex;
                while (this.batch > 0) {
                    int i3 = this.baseLimit;
                    int i4 = (i3 + i2) >>> 1;
                    if (i4 <= i2) {
                        break;
                    }
                    addToPendingCount(1);
                    int i5 = this.batch >>> 1;
                    this.batch = i5;
                    this.baseLimit = i4;
                    MapReduceKeysToLongTask<K, V> mapReduceKeysToLongTask = new MapReduceKeysToLongTask<>(this, i5, i4, i3, this.tab, this.rights, toLongFunction, jApplyAsLong, longBinaryOperator);
                    this.rights = mapReduceKeysToLongTask;
                    mapReduceKeysToLongTask.fork();
                }
                while (true) {
                    Node<K, V> nodeAdvance = advance();
                    if (nodeAdvance == null) {
                        break;
                    } else {
                        jApplyAsLong = longBinaryOperator.applyAsLong(jApplyAsLong, toLongFunction.applyAsLong(nodeAdvance.key));
                    }
                }
                this.result = jApplyAsLong;
                CountedCompleter<?> countedCompleterFirstComplete = firstComplete();
                while (true) {
                    CountedCompleter<?> countedCompleter = countedCompleterFirstComplete;
                    if (countedCompleter != null) {
                        MapReduceKeysToLongTask mapReduceKeysToLongTask2 = (MapReduceKeysToLongTask) countedCompleter;
                        MapReduceKeysToLongTask<K, V> mapReduceKeysToLongTask3 = mapReduceKeysToLongTask2.rights;
                        while (true) {
                            MapReduceKeysToLongTask<K, V> mapReduceKeysToLongTask4 = mapReduceKeysToLongTask3;
                            if (mapReduceKeysToLongTask4 != null) {
                                mapReduceKeysToLongTask2.result = longBinaryOperator.applyAsLong(mapReduceKeysToLongTask2.result, mapReduceKeysToLongTask4.result);
                                MapReduceKeysToLongTask<K, V> mapReduceKeysToLongTask5 = mapReduceKeysToLongTask4.nextRight;
                                mapReduceKeysToLongTask3 = mapReduceKeysToLongTask5;
                                mapReduceKeysToLongTask2.rights = mapReduceKeysToLongTask5;
                            }
                        }
                        countedCompleterFirstComplete = countedCompleter.nextComplete();
                    } else {
                        return;
                    }
                }
            }
        }
    }

    /* loaded from: rt.jar:java/util/concurrent/ConcurrentHashMap$MapReduceValuesToLongTask.class */
    static final class MapReduceValuesToLongTask<K, V> extends BulkTask<K, V, Long> {
        final ToLongFunction<? super V> transformer;
        final LongBinaryOperator reducer;
        final long basis;
        long result;
        MapReduceValuesToLongTask<K, V> rights;
        MapReduceValuesToLongTask<K, V> nextRight;

        MapReduceValuesToLongTask(BulkTask<K, V, ?> bulkTask, int i2, int i3, int i4, Node<K, V>[] nodeArr, MapReduceValuesToLongTask<K, V> mapReduceValuesToLongTask, ToLongFunction<? super V> toLongFunction, long j2, LongBinaryOperator longBinaryOperator) {
            super(bulkTask, i2, i3, i4, nodeArr);
            this.nextRight = mapReduceValuesToLongTask;
            this.transformer = toLongFunction;
            this.basis = j2;
            this.reducer = longBinaryOperator;
        }

        @Override // java.util.concurrent.CountedCompleter, java.util.concurrent.ForkJoinTask
        public final Long getRawResult() {
            return Long.valueOf(this.result);
        }

        @Override // java.util.concurrent.CountedCompleter
        public final void compute() {
            LongBinaryOperator longBinaryOperator;
            ToLongFunction<? super V> toLongFunction = this.transformer;
            if (toLongFunction != null && (longBinaryOperator = this.reducer) != null) {
                long jApplyAsLong = this.basis;
                int i2 = this.baseIndex;
                while (this.batch > 0) {
                    int i3 = this.baseLimit;
                    int i4 = (i3 + i2) >>> 1;
                    if (i4 <= i2) {
                        break;
                    }
                    addToPendingCount(1);
                    int i5 = this.batch >>> 1;
                    this.batch = i5;
                    this.baseLimit = i4;
                    MapReduceValuesToLongTask<K, V> mapReduceValuesToLongTask = new MapReduceValuesToLongTask<>(this, i5, i4, i3, this.tab, this.rights, toLongFunction, jApplyAsLong, longBinaryOperator);
                    this.rights = mapReduceValuesToLongTask;
                    mapReduceValuesToLongTask.fork();
                }
                while (true) {
                    Node<K, V> nodeAdvance = advance();
                    if (nodeAdvance == null) {
                        break;
                    } else {
                        jApplyAsLong = longBinaryOperator.applyAsLong(jApplyAsLong, toLongFunction.applyAsLong(nodeAdvance.val));
                    }
                }
                this.result = jApplyAsLong;
                CountedCompleter<?> countedCompleterFirstComplete = firstComplete();
                while (true) {
                    CountedCompleter<?> countedCompleter = countedCompleterFirstComplete;
                    if (countedCompleter != null) {
                        MapReduceValuesToLongTask mapReduceValuesToLongTask2 = (MapReduceValuesToLongTask) countedCompleter;
                        MapReduceValuesToLongTask<K, V> mapReduceValuesToLongTask3 = mapReduceValuesToLongTask2.rights;
                        while (true) {
                            MapReduceValuesToLongTask<K, V> mapReduceValuesToLongTask4 = mapReduceValuesToLongTask3;
                            if (mapReduceValuesToLongTask4 != null) {
                                mapReduceValuesToLongTask2.result = longBinaryOperator.applyAsLong(mapReduceValuesToLongTask2.result, mapReduceValuesToLongTask4.result);
                                MapReduceValuesToLongTask<K, V> mapReduceValuesToLongTask5 = mapReduceValuesToLongTask4.nextRight;
                                mapReduceValuesToLongTask3 = mapReduceValuesToLongTask5;
                                mapReduceValuesToLongTask2.rights = mapReduceValuesToLongTask5;
                            }
                        }
                        countedCompleterFirstComplete = countedCompleter.nextComplete();
                    } else {
                        return;
                    }
                }
            }
        }
    }

    /* loaded from: rt.jar:java/util/concurrent/ConcurrentHashMap$MapReduceEntriesToLongTask.class */
    static final class MapReduceEntriesToLongTask<K, V> extends BulkTask<K, V, Long> {
        final ToLongFunction<Map.Entry<K, V>> transformer;
        final LongBinaryOperator reducer;
        final long basis;
        long result;
        MapReduceEntriesToLongTask<K, V> rights;
        MapReduceEntriesToLongTask<K, V> nextRight;

        MapReduceEntriesToLongTask(BulkTask<K, V, ?> bulkTask, int i2, int i3, int i4, Node<K, V>[] nodeArr, MapReduceEntriesToLongTask<K, V> mapReduceEntriesToLongTask, ToLongFunction<Map.Entry<K, V>> toLongFunction, long j2, LongBinaryOperator longBinaryOperator) {
            super(bulkTask, i2, i3, i4, nodeArr);
            this.nextRight = mapReduceEntriesToLongTask;
            this.transformer = toLongFunction;
            this.basis = j2;
            this.reducer = longBinaryOperator;
        }

        @Override // java.util.concurrent.CountedCompleter, java.util.concurrent.ForkJoinTask
        public final Long getRawResult() {
            return Long.valueOf(this.result);
        }

        @Override // java.util.concurrent.CountedCompleter
        public final void compute() {
            LongBinaryOperator longBinaryOperator;
            ToLongFunction<Map.Entry<K, V>> toLongFunction = this.transformer;
            if (toLongFunction != null && (longBinaryOperator = this.reducer) != null) {
                long jApplyAsLong = this.basis;
                int i2 = this.baseIndex;
                while (this.batch > 0) {
                    int i3 = this.baseLimit;
                    int i4 = (i3 + i2) >>> 1;
                    if (i4 <= i2) {
                        break;
                    }
                    addToPendingCount(1);
                    int i5 = this.batch >>> 1;
                    this.batch = i5;
                    this.baseLimit = i4;
                    MapReduceEntriesToLongTask<K, V> mapReduceEntriesToLongTask = new MapReduceEntriesToLongTask<>(this, i5, i4, i3, this.tab, this.rights, toLongFunction, jApplyAsLong, longBinaryOperator);
                    this.rights = mapReduceEntriesToLongTask;
                    mapReduceEntriesToLongTask.fork();
                }
                while (true) {
                    Node<K, V> nodeAdvance = advance();
                    if (nodeAdvance == null) {
                        break;
                    } else {
                        jApplyAsLong = longBinaryOperator.applyAsLong(jApplyAsLong, toLongFunction.applyAsLong(nodeAdvance));
                    }
                }
                this.result = jApplyAsLong;
                CountedCompleter<?> countedCompleterFirstComplete = firstComplete();
                while (true) {
                    CountedCompleter<?> countedCompleter = countedCompleterFirstComplete;
                    if (countedCompleter != null) {
                        MapReduceEntriesToLongTask mapReduceEntriesToLongTask2 = (MapReduceEntriesToLongTask) countedCompleter;
                        MapReduceEntriesToLongTask<K, V> mapReduceEntriesToLongTask3 = mapReduceEntriesToLongTask2.rights;
                        while (true) {
                            MapReduceEntriesToLongTask<K, V> mapReduceEntriesToLongTask4 = mapReduceEntriesToLongTask3;
                            if (mapReduceEntriesToLongTask4 != null) {
                                mapReduceEntriesToLongTask2.result = longBinaryOperator.applyAsLong(mapReduceEntriesToLongTask2.result, mapReduceEntriesToLongTask4.result);
                                MapReduceEntriesToLongTask<K, V> mapReduceEntriesToLongTask5 = mapReduceEntriesToLongTask4.nextRight;
                                mapReduceEntriesToLongTask3 = mapReduceEntriesToLongTask5;
                                mapReduceEntriesToLongTask2.rights = mapReduceEntriesToLongTask5;
                            }
                        }
                        countedCompleterFirstComplete = countedCompleter.nextComplete();
                    } else {
                        return;
                    }
                }
            }
        }
    }

    /* loaded from: rt.jar:java/util/concurrent/ConcurrentHashMap$MapReduceMappingsToLongTask.class */
    static final class MapReduceMappingsToLongTask<K, V> extends BulkTask<K, V, Long> {
        final ToLongBiFunction<? super K, ? super V> transformer;
        final LongBinaryOperator reducer;
        final long basis;
        long result;
        MapReduceMappingsToLongTask<K, V> rights;
        MapReduceMappingsToLongTask<K, V> nextRight;

        MapReduceMappingsToLongTask(BulkTask<K, V, ?> bulkTask, int i2, int i3, int i4, Node<K, V>[] nodeArr, MapReduceMappingsToLongTask<K, V> mapReduceMappingsToLongTask, ToLongBiFunction<? super K, ? super V> toLongBiFunction, long j2, LongBinaryOperator longBinaryOperator) {
            super(bulkTask, i2, i3, i4, nodeArr);
            this.nextRight = mapReduceMappingsToLongTask;
            this.transformer = toLongBiFunction;
            this.basis = j2;
            this.reducer = longBinaryOperator;
        }

        @Override // java.util.concurrent.CountedCompleter, java.util.concurrent.ForkJoinTask
        public final Long getRawResult() {
            return Long.valueOf(this.result);
        }

        @Override // java.util.concurrent.CountedCompleter
        public final void compute() {
            LongBinaryOperator longBinaryOperator;
            ToLongBiFunction<? super K, ? super V> toLongBiFunction = this.transformer;
            if (toLongBiFunction != null && (longBinaryOperator = this.reducer) != null) {
                long jApplyAsLong = this.basis;
                int i2 = this.baseIndex;
                while (this.batch > 0) {
                    int i3 = this.baseLimit;
                    int i4 = (i3 + i2) >>> 1;
                    if (i4 <= i2) {
                        break;
                    }
                    addToPendingCount(1);
                    int i5 = this.batch >>> 1;
                    this.batch = i5;
                    this.baseLimit = i4;
                    MapReduceMappingsToLongTask<K, V> mapReduceMappingsToLongTask = new MapReduceMappingsToLongTask<>(this, i5, i4, i3, this.tab, this.rights, toLongBiFunction, jApplyAsLong, longBinaryOperator);
                    this.rights = mapReduceMappingsToLongTask;
                    mapReduceMappingsToLongTask.fork();
                }
                while (true) {
                    Node<K, V> nodeAdvance = advance();
                    if (nodeAdvance == null) {
                        break;
                    } else {
                        jApplyAsLong = longBinaryOperator.applyAsLong(jApplyAsLong, toLongBiFunction.applyAsLong(nodeAdvance.key, nodeAdvance.val));
                    }
                }
                this.result = jApplyAsLong;
                CountedCompleter<?> countedCompleterFirstComplete = firstComplete();
                while (true) {
                    CountedCompleter<?> countedCompleter = countedCompleterFirstComplete;
                    if (countedCompleter != null) {
                        MapReduceMappingsToLongTask mapReduceMappingsToLongTask2 = (MapReduceMappingsToLongTask) countedCompleter;
                        MapReduceMappingsToLongTask<K, V> mapReduceMappingsToLongTask3 = mapReduceMappingsToLongTask2.rights;
                        while (true) {
                            MapReduceMappingsToLongTask<K, V> mapReduceMappingsToLongTask4 = mapReduceMappingsToLongTask3;
                            if (mapReduceMappingsToLongTask4 != null) {
                                mapReduceMappingsToLongTask2.result = longBinaryOperator.applyAsLong(mapReduceMappingsToLongTask2.result, mapReduceMappingsToLongTask4.result);
                                MapReduceMappingsToLongTask<K, V> mapReduceMappingsToLongTask5 = mapReduceMappingsToLongTask4.nextRight;
                                mapReduceMappingsToLongTask3 = mapReduceMappingsToLongTask5;
                                mapReduceMappingsToLongTask2.rights = mapReduceMappingsToLongTask5;
                            }
                        }
                        countedCompleterFirstComplete = countedCompleter.nextComplete();
                    } else {
                        return;
                    }
                }
            }
        }
    }

    /* loaded from: rt.jar:java/util/concurrent/ConcurrentHashMap$MapReduceKeysToIntTask.class */
    static final class MapReduceKeysToIntTask<K, V> extends BulkTask<K, V, Integer> {
        final ToIntFunction<? super K> transformer;
        final IntBinaryOperator reducer;
        final int basis;
        int result;
        MapReduceKeysToIntTask<K, V> rights;
        MapReduceKeysToIntTask<K, V> nextRight;

        MapReduceKeysToIntTask(BulkTask<K, V, ?> bulkTask, int i2, int i3, int i4, Node<K, V>[] nodeArr, MapReduceKeysToIntTask<K, V> mapReduceKeysToIntTask, ToIntFunction<? super K> toIntFunction, int i5, IntBinaryOperator intBinaryOperator) {
            super(bulkTask, i2, i3, i4, nodeArr);
            this.nextRight = mapReduceKeysToIntTask;
            this.transformer = toIntFunction;
            this.basis = i5;
            this.reducer = intBinaryOperator;
        }

        @Override // java.util.concurrent.CountedCompleter, java.util.concurrent.ForkJoinTask
        public final Integer getRawResult() {
            return Integer.valueOf(this.result);
        }

        @Override // java.util.concurrent.CountedCompleter
        public final void compute() {
            IntBinaryOperator intBinaryOperator;
            ToIntFunction<? super K> toIntFunction = this.transformer;
            if (toIntFunction != null && (intBinaryOperator = this.reducer) != null) {
                int iApplyAsInt = this.basis;
                int i2 = this.baseIndex;
                while (this.batch > 0) {
                    int i3 = this.baseLimit;
                    int i4 = (i3 + i2) >>> 1;
                    if (i4 <= i2) {
                        break;
                    }
                    addToPendingCount(1);
                    int i5 = this.batch >>> 1;
                    this.batch = i5;
                    this.baseLimit = i4;
                    MapReduceKeysToIntTask<K, V> mapReduceKeysToIntTask = new MapReduceKeysToIntTask<>(this, i5, i4, i3, this.tab, this.rights, toIntFunction, iApplyAsInt, intBinaryOperator);
                    this.rights = mapReduceKeysToIntTask;
                    mapReduceKeysToIntTask.fork();
                }
                while (true) {
                    Node<K, V> nodeAdvance = advance();
                    if (nodeAdvance == null) {
                        break;
                    } else {
                        iApplyAsInt = intBinaryOperator.applyAsInt(iApplyAsInt, toIntFunction.applyAsInt(nodeAdvance.key));
                    }
                }
                this.result = iApplyAsInt;
                CountedCompleter<?> countedCompleterFirstComplete = firstComplete();
                while (true) {
                    CountedCompleter<?> countedCompleter = countedCompleterFirstComplete;
                    if (countedCompleter != null) {
                        MapReduceKeysToIntTask mapReduceKeysToIntTask2 = (MapReduceKeysToIntTask) countedCompleter;
                        MapReduceKeysToIntTask<K, V> mapReduceKeysToIntTask3 = mapReduceKeysToIntTask2.rights;
                        while (true) {
                            MapReduceKeysToIntTask<K, V> mapReduceKeysToIntTask4 = mapReduceKeysToIntTask3;
                            if (mapReduceKeysToIntTask4 != null) {
                                mapReduceKeysToIntTask2.result = intBinaryOperator.applyAsInt(mapReduceKeysToIntTask2.result, mapReduceKeysToIntTask4.result);
                                MapReduceKeysToIntTask<K, V> mapReduceKeysToIntTask5 = mapReduceKeysToIntTask4.nextRight;
                                mapReduceKeysToIntTask3 = mapReduceKeysToIntTask5;
                                mapReduceKeysToIntTask2.rights = mapReduceKeysToIntTask5;
                            }
                        }
                        countedCompleterFirstComplete = countedCompleter.nextComplete();
                    } else {
                        return;
                    }
                }
            }
        }
    }

    /* loaded from: rt.jar:java/util/concurrent/ConcurrentHashMap$MapReduceValuesToIntTask.class */
    static final class MapReduceValuesToIntTask<K, V> extends BulkTask<K, V, Integer> {
        final ToIntFunction<? super V> transformer;
        final IntBinaryOperator reducer;
        final int basis;
        int result;
        MapReduceValuesToIntTask<K, V> rights;
        MapReduceValuesToIntTask<K, V> nextRight;

        MapReduceValuesToIntTask(BulkTask<K, V, ?> bulkTask, int i2, int i3, int i4, Node<K, V>[] nodeArr, MapReduceValuesToIntTask<K, V> mapReduceValuesToIntTask, ToIntFunction<? super V> toIntFunction, int i5, IntBinaryOperator intBinaryOperator) {
            super(bulkTask, i2, i3, i4, nodeArr);
            this.nextRight = mapReduceValuesToIntTask;
            this.transformer = toIntFunction;
            this.basis = i5;
            this.reducer = intBinaryOperator;
        }

        @Override // java.util.concurrent.CountedCompleter, java.util.concurrent.ForkJoinTask
        public final Integer getRawResult() {
            return Integer.valueOf(this.result);
        }

        @Override // java.util.concurrent.CountedCompleter
        public final void compute() {
            IntBinaryOperator intBinaryOperator;
            ToIntFunction<? super V> toIntFunction = this.transformer;
            if (toIntFunction != null && (intBinaryOperator = this.reducer) != null) {
                int iApplyAsInt = this.basis;
                int i2 = this.baseIndex;
                while (this.batch > 0) {
                    int i3 = this.baseLimit;
                    int i4 = (i3 + i2) >>> 1;
                    if (i4 <= i2) {
                        break;
                    }
                    addToPendingCount(1);
                    int i5 = this.batch >>> 1;
                    this.batch = i5;
                    this.baseLimit = i4;
                    MapReduceValuesToIntTask<K, V> mapReduceValuesToIntTask = new MapReduceValuesToIntTask<>(this, i5, i4, i3, this.tab, this.rights, toIntFunction, iApplyAsInt, intBinaryOperator);
                    this.rights = mapReduceValuesToIntTask;
                    mapReduceValuesToIntTask.fork();
                }
                while (true) {
                    Node<K, V> nodeAdvance = advance();
                    if (nodeAdvance == null) {
                        break;
                    } else {
                        iApplyAsInt = intBinaryOperator.applyAsInt(iApplyAsInt, toIntFunction.applyAsInt(nodeAdvance.val));
                    }
                }
                this.result = iApplyAsInt;
                CountedCompleter<?> countedCompleterFirstComplete = firstComplete();
                while (true) {
                    CountedCompleter<?> countedCompleter = countedCompleterFirstComplete;
                    if (countedCompleter != null) {
                        MapReduceValuesToIntTask mapReduceValuesToIntTask2 = (MapReduceValuesToIntTask) countedCompleter;
                        MapReduceValuesToIntTask<K, V> mapReduceValuesToIntTask3 = mapReduceValuesToIntTask2.rights;
                        while (true) {
                            MapReduceValuesToIntTask<K, V> mapReduceValuesToIntTask4 = mapReduceValuesToIntTask3;
                            if (mapReduceValuesToIntTask4 != null) {
                                mapReduceValuesToIntTask2.result = intBinaryOperator.applyAsInt(mapReduceValuesToIntTask2.result, mapReduceValuesToIntTask4.result);
                                MapReduceValuesToIntTask<K, V> mapReduceValuesToIntTask5 = mapReduceValuesToIntTask4.nextRight;
                                mapReduceValuesToIntTask3 = mapReduceValuesToIntTask5;
                                mapReduceValuesToIntTask2.rights = mapReduceValuesToIntTask5;
                            }
                        }
                        countedCompleterFirstComplete = countedCompleter.nextComplete();
                    } else {
                        return;
                    }
                }
            }
        }
    }

    /* loaded from: rt.jar:java/util/concurrent/ConcurrentHashMap$MapReduceEntriesToIntTask.class */
    static final class MapReduceEntriesToIntTask<K, V> extends BulkTask<K, V, Integer> {
        final ToIntFunction<Map.Entry<K, V>> transformer;
        final IntBinaryOperator reducer;
        final int basis;
        int result;
        MapReduceEntriesToIntTask<K, V> rights;
        MapReduceEntriesToIntTask<K, V> nextRight;

        MapReduceEntriesToIntTask(BulkTask<K, V, ?> bulkTask, int i2, int i3, int i4, Node<K, V>[] nodeArr, MapReduceEntriesToIntTask<K, V> mapReduceEntriesToIntTask, ToIntFunction<Map.Entry<K, V>> toIntFunction, int i5, IntBinaryOperator intBinaryOperator) {
            super(bulkTask, i2, i3, i4, nodeArr);
            this.nextRight = mapReduceEntriesToIntTask;
            this.transformer = toIntFunction;
            this.basis = i5;
            this.reducer = intBinaryOperator;
        }

        @Override // java.util.concurrent.CountedCompleter, java.util.concurrent.ForkJoinTask
        public final Integer getRawResult() {
            return Integer.valueOf(this.result);
        }

        @Override // java.util.concurrent.CountedCompleter
        public final void compute() {
            IntBinaryOperator intBinaryOperator;
            ToIntFunction<Map.Entry<K, V>> toIntFunction = this.transformer;
            if (toIntFunction != null && (intBinaryOperator = this.reducer) != null) {
                int iApplyAsInt = this.basis;
                int i2 = this.baseIndex;
                while (this.batch > 0) {
                    int i3 = this.baseLimit;
                    int i4 = (i3 + i2) >>> 1;
                    if (i4 <= i2) {
                        break;
                    }
                    addToPendingCount(1);
                    int i5 = this.batch >>> 1;
                    this.batch = i5;
                    this.baseLimit = i4;
                    MapReduceEntriesToIntTask<K, V> mapReduceEntriesToIntTask = new MapReduceEntriesToIntTask<>(this, i5, i4, i3, this.tab, this.rights, toIntFunction, iApplyAsInt, intBinaryOperator);
                    this.rights = mapReduceEntriesToIntTask;
                    mapReduceEntriesToIntTask.fork();
                }
                while (true) {
                    Node<K, V> nodeAdvance = advance();
                    if (nodeAdvance == null) {
                        break;
                    } else {
                        iApplyAsInt = intBinaryOperator.applyAsInt(iApplyAsInt, toIntFunction.applyAsInt(nodeAdvance));
                    }
                }
                this.result = iApplyAsInt;
                CountedCompleter<?> countedCompleterFirstComplete = firstComplete();
                while (true) {
                    CountedCompleter<?> countedCompleter = countedCompleterFirstComplete;
                    if (countedCompleter != null) {
                        MapReduceEntriesToIntTask mapReduceEntriesToIntTask2 = (MapReduceEntriesToIntTask) countedCompleter;
                        MapReduceEntriesToIntTask<K, V> mapReduceEntriesToIntTask3 = mapReduceEntriesToIntTask2.rights;
                        while (true) {
                            MapReduceEntriesToIntTask<K, V> mapReduceEntriesToIntTask4 = mapReduceEntriesToIntTask3;
                            if (mapReduceEntriesToIntTask4 != null) {
                                mapReduceEntriesToIntTask2.result = intBinaryOperator.applyAsInt(mapReduceEntriesToIntTask2.result, mapReduceEntriesToIntTask4.result);
                                MapReduceEntriesToIntTask<K, V> mapReduceEntriesToIntTask5 = mapReduceEntriesToIntTask4.nextRight;
                                mapReduceEntriesToIntTask3 = mapReduceEntriesToIntTask5;
                                mapReduceEntriesToIntTask2.rights = mapReduceEntriesToIntTask5;
                            }
                        }
                        countedCompleterFirstComplete = countedCompleter.nextComplete();
                    } else {
                        return;
                    }
                }
            }
        }
    }

    /* loaded from: rt.jar:java/util/concurrent/ConcurrentHashMap$MapReduceMappingsToIntTask.class */
    static final class MapReduceMappingsToIntTask<K, V> extends BulkTask<K, V, Integer> {
        final ToIntBiFunction<? super K, ? super V> transformer;
        final IntBinaryOperator reducer;
        final int basis;
        int result;
        MapReduceMappingsToIntTask<K, V> rights;
        MapReduceMappingsToIntTask<K, V> nextRight;

        MapReduceMappingsToIntTask(BulkTask<K, V, ?> bulkTask, int i2, int i3, int i4, Node<K, V>[] nodeArr, MapReduceMappingsToIntTask<K, V> mapReduceMappingsToIntTask, ToIntBiFunction<? super K, ? super V> toIntBiFunction, int i5, IntBinaryOperator intBinaryOperator) {
            super(bulkTask, i2, i3, i4, nodeArr);
            this.nextRight = mapReduceMappingsToIntTask;
            this.transformer = toIntBiFunction;
            this.basis = i5;
            this.reducer = intBinaryOperator;
        }

        @Override // java.util.concurrent.CountedCompleter, java.util.concurrent.ForkJoinTask
        public final Integer getRawResult() {
            return Integer.valueOf(this.result);
        }

        @Override // java.util.concurrent.CountedCompleter
        public final void compute() {
            IntBinaryOperator intBinaryOperator;
            ToIntBiFunction<? super K, ? super V> toIntBiFunction = this.transformer;
            if (toIntBiFunction != null && (intBinaryOperator = this.reducer) != null) {
                int iApplyAsInt = this.basis;
                int i2 = this.baseIndex;
                while (this.batch > 0) {
                    int i3 = this.baseLimit;
                    int i4 = (i3 + i2) >>> 1;
                    if (i4 <= i2) {
                        break;
                    }
                    addToPendingCount(1);
                    int i5 = this.batch >>> 1;
                    this.batch = i5;
                    this.baseLimit = i4;
                    MapReduceMappingsToIntTask<K, V> mapReduceMappingsToIntTask = new MapReduceMappingsToIntTask<>(this, i5, i4, i3, this.tab, this.rights, toIntBiFunction, iApplyAsInt, intBinaryOperator);
                    this.rights = mapReduceMappingsToIntTask;
                    mapReduceMappingsToIntTask.fork();
                }
                while (true) {
                    Node<K, V> nodeAdvance = advance();
                    if (nodeAdvance == null) {
                        break;
                    } else {
                        iApplyAsInt = intBinaryOperator.applyAsInt(iApplyAsInt, toIntBiFunction.applyAsInt(nodeAdvance.key, nodeAdvance.val));
                    }
                }
                this.result = iApplyAsInt;
                CountedCompleter<?> countedCompleterFirstComplete = firstComplete();
                while (true) {
                    CountedCompleter<?> countedCompleter = countedCompleterFirstComplete;
                    if (countedCompleter != null) {
                        MapReduceMappingsToIntTask mapReduceMappingsToIntTask2 = (MapReduceMappingsToIntTask) countedCompleter;
                        MapReduceMappingsToIntTask<K, V> mapReduceMappingsToIntTask3 = mapReduceMappingsToIntTask2.rights;
                        while (true) {
                            MapReduceMappingsToIntTask<K, V> mapReduceMappingsToIntTask4 = mapReduceMappingsToIntTask3;
                            if (mapReduceMappingsToIntTask4 != null) {
                                mapReduceMappingsToIntTask2.result = intBinaryOperator.applyAsInt(mapReduceMappingsToIntTask2.result, mapReduceMappingsToIntTask4.result);
                                MapReduceMappingsToIntTask<K, V> mapReduceMappingsToIntTask5 = mapReduceMappingsToIntTask4.nextRight;
                                mapReduceMappingsToIntTask3 = mapReduceMappingsToIntTask5;
                                mapReduceMappingsToIntTask2.rights = mapReduceMappingsToIntTask5;
                            }
                        }
                        countedCompleterFirstComplete = countedCompleter.nextComplete();
                    } else {
                        return;
                    }
                }
            }
        }
    }
}
