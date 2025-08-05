package java.util;

import java.io.IOException;
import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import sun.misc.SharedSecrets;
import sun.misc.Unsafe;

/* loaded from: rt.jar:java/util/HashMap.class */
public class HashMap<K, V> extends AbstractMap<K, V> implements Map<K, V>, Cloneable, Serializable {
    private static final long serialVersionUID = 362498820763181265L;
    static final int DEFAULT_INITIAL_CAPACITY = 16;
    static final int MAXIMUM_CAPACITY = 1073741824;
    static final float DEFAULT_LOAD_FACTOR = 0.75f;
    static final int TREEIFY_THRESHOLD = 8;
    static final int UNTREEIFY_THRESHOLD = 6;
    static final int MIN_TREEIFY_CAPACITY = 64;
    transient Node<K, V>[] table;
    transient Set<Map.Entry<K, V>> entrySet;
    transient int size;
    transient int modCount;
    int threshold;
    final float loadFactor;

    /* loaded from: rt.jar:java/util/HashMap$Node.class */
    static class Node<K, V> implements Map.Entry<K, V> {
        final int hash;
        final K key;
        V value;
        Node<K, V> next;

        Node(int i2, K k2, V v2, Node<K, V> node) {
            this.hash = i2;
            this.key = k2;
            this.value = v2;
            this.next = node;
        }

        @Override // java.util.Map.Entry
        public final K getKey() {
            return this.key;
        }

        @Override // java.util.Map.Entry
        public final V getValue() {
            return this.value;
        }

        public final String toString() {
            return ((Object) this.key) + "=" + ((Object) this.value);
        }

        @Override // java.util.Map.Entry
        public final int hashCode() {
            return Objects.hashCode(this.key) ^ Objects.hashCode(this.value);
        }

        @Override // java.util.Map.Entry
        public final V setValue(V v2) {
            V v3 = this.value;
            this.value = v2;
            return v3;
        }

        @Override // java.util.Map.Entry
        public final boolean equals(Object obj) {
            if (obj == this) {
                return true;
            }
            if (obj instanceof Map.Entry) {
                Map.Entry entry = (Map.Entry) obj;
                if (Objects.equals(this.key, entry.getKey()) && Objects.equals(this.value, entry.getValue())) {
                    return true;
                }
                return false;
            }
            return false;
        }
    }

    static final int hash(Object obj) {
        if (obj == null) {
            return 0;
        }
        int iHashCode = obj.hashCode();
        return iHashCode ^ (iHashCode >>> 16);
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

    static final int tableSizeFor(int i2) {
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

    public HashMap(int i2, float f2) {
        if (i2 < 0) {
            throw new IllegalArgumentException("Illegal initial capacity: " + i2);
        }
        i2 = i2 > 1073741824 ? 1073741824 : i2;
        if (f2 <= 0.0f || Float.isNaN(f2)) {
            throw new IllegalArgumentException("Illegal load factor: " + f2);
        }
        this.loadFactor = f2;
        this.threshold = tableSizeFor(i2);
    }

    public HashMap(int i2) {
        this(i2, 0.75f);
    }

    public HashMap() {
        this.loadFactor = 0.75f;
    }

    public HashMap(Map<? extends K, ? extends V> map) {
        this.loadFactor = 0.75f;
        putMapEntries(map, false);
    }

    final void putMapEntries(Map<? extends K, ? extends V> map, boolean z2) {
        int size = map.size();
        if (size > 0) {
            if (this.table == null) {
                float f2 = (size / this.loadFactor) + 1.0f;
                int i2 = f2 < 1.0737418E9f ? (int) f2 : 1073741824;
                if (i2 > this.threshold) {
                    this.threshold = tableSizeFor(i2);
                }
            } else if (size > this.threshold) {
                resize();
            }
            for (Map.Entry<? extends K, ? extends V> entry : map.entrySet()) {
                K key = entry.getKey();
                putVal(hash(key), key, entry.getValue(), false, z2);
            }
        }
    }

    @Override // java.util.AbstractMap, java.util.Map
    public int size() {
        return this.size;
    }

    @Override // java.util.AbstractMap, java.util.Map
    public boolean isEmpty() {
        return this.size == 0;
    }

    @Override // java.util.AbstractMap, java.util.Map
    public V get(Object obj) {
        Node<K, V> node = getNode(hash(obj), obj);
        if (node == null) {
            return null;
        }
        return node.value;
    }

    final Node<K, V> getNode(int i2, Object obj) {
        int length;
        Node<K, V> node;
        Node<K, V> node2;
        K k2;
        K k3;
        Node<K, V>[] nodeArr = this.table;
        if (nodeArr != null && (length = nodeArr.length) > 0 && (node = nodeArr[(length - 1) & i2]) != null) {
            if (node.hash == i2 && ((k3 = node.key) == obj || (obj != null && obj.equals(k3)))) {
                return node;
            }
            Node<K, V> node3 = node.next;
            Node<K, V> node4 = node3;
            if (node3 != null) {
                if (node instanceof TreeNode) {
                    return ((TreeNode) node).getTreeNode(i2, obj);
                }
                do {
                    if (node4.hash == i2 && ((k2 = node4.key) == obj || (obj != null && obj.equals(k2)))) {
                        return node4;
                    }
                    node2 = node4.next;
                    node4 = node2;
                } while (node2 != null);
                return null;
            }
            return null;
        }
        return null;
    }

    @Override // java.util.AbstractMap, java.util.Map
    public boolean containsKey(Object obj) {
        return getNode(hash(obj), obj) != null;
    }

    @Override // java.util.AbstractMap, java.util.Map
    public V put(K k2, V v2) {
        return putVal(hash(k2), k2, v2, false, true);
    }

    /* JADX WARN: Removed duplicated region for block: B:6:0x0013  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    final V putVal(int r9, K r10, V r11, boolean r12, boolean r13) {
        /*
            Method dump skipped, instructions count: 300
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: java.util.HashMap.putVal(int, java.lang.Object, java.lang.Object, boolean, boolean):java.lang.Object");
    }

    final Node<K, V>[] resize() {
        int i2;
        Node<K, V> node;
        Node<K, V>[] nodeArr = this.table;
        int length = nodeArr == null ? 0 : nodeArr.length;
        int i3 = this.threshold;
        int i4 = 0;
        if (length > 0) {
            if (length >= 1073741824) {
                this.threshold = Integer.MAX_VALUE;
                return nodeArr;
            }
            int i5 = length << 1;
            i2 = i5;
            if (i5 < 1073741824 && length >= 16) {
                i4 = i3 << 1;
            }
        } else if (i3 > 0) {
            i2 = i3;
        } else {
            i2 = 16;
            i4 = 12;
        }
        if (i4 == 0) {
            float f2 = i2 * this.loadFactor;
            i4 = (i2 >= 1073741824 || f2 >= 1.0737418E9f) ? Integer.MAX_VALUE : (int) f2;
        }
        this.threshold = i4;
        Node<K, V>[] nodeArr2 = new Node[i2];
        this.table = nodeArr2;
        if (nodeArr != null) {
            for (int i6 = 0; i6 < length; i6++) {
                Node<K, V> node2 = nodeArr[i6];
                Node<K, V> node3 = node2;
                if (node2 != null) {
                    nodeArr[i6] = null;
                    if (node3.next == null) {
                        nodeArr2[node3.hash & (i2 - 1)] = node3;
                    } else if (node3 instanceof TreeNode) {
                        ((TreeNode) node3).split(this, nodeArr2, i6, length);
                    } else {
                        Node<K, V> node4 = null;
                        Node<K, V> node5 = null;
                        Node<K, V> node6 = null;
                        Node<K, V> node7 = null;
                        do {
                            node = node3.next;
                            if ((node3.hash & length) == 0) {
                                if (node5 == null) {
                                    node4 = node3;
                                } else {
                                    node5.next = node3;
                                }
                                node5 = node3;
                            } else {
                                if (node7 == null) {
                                    node6 = node3;
                                } else {
                                    node7.next = node3;
                                }
                                node7 = node3;
                            }
                            node3 = node;
                        } while (node != null);
                        if (node5 != null) {
                            node5.next = null;
                            nodeArr2[i6] = node4;
                        }
                        if (node7 != null) {
                            node7.next = null;
                            nodeArr2[i6 + length] = node6;
                        }
                    }
                }
            }
        }
        return nodeArr2;
    }

    final void treeifyBin(Node<K, V>[] nodeArr, int i2) {
        int length;
        Node<K, V> node;
        if (nodeArr == null || (length = nodeArr.length) < 64) {
            resize();
            return;
        }
        int i3 = (length - 1) & i2;
        Node<K, V> node2 = nodeArr[i3];
        Node<K, V> node3 = node2;
        if (node2 != null) {
            TreeNode<K, V> treeNode = null;
            TreeNode<K, V> treeNode2 = null;
            do {
                TreeNode<K, V> treeNodeReplacementTreeNode = replacementTreeNode(node3, null);
                if (treeNode2 == null) {
                    treeNode = treeNodeReplacementTreeNode;
                } else {
                    treeNodeReplacementTreeNode.prev = treeNode2;
                    treeNode2.next = treeNodeReplacementTreeNode;
                }
                treeNode2 = treeNodeReplacementTreeNode;
                node = node3.next;
                node3 = node;
            } while (node != null);
            Node<K, V> node4 = treeNode;
            nodeArr[i3] = node4;
            if (node4 != null) {
                treeNode.treeify(nodeArr);
            }
        }
    }

    @Override // java.util.AbstractMap, java.util.Map
    public void putAll(Map<? extends K, ? extends V> map) {
        putMapEntries(map, true);
    }

    @Override // java.util.AbstractMap, java.util.Map
    public V remove(Object obj) {
        Node<K, V> nodeRemoveNode = removeNode(hash(obj), obj, null, false, true);
        if (nodeRemoveNode == null) {
            return null;
        }
        return nodeRemoveNode.value;
    }

    final Node<K, V> removeNode(int i2, Object obj, Object obj2, boolean z2, boolean z3) {
        int length;
        Node<K, V> node;
        K k2;
        V v2;
        K k3;
        Node<K, V>[] nodeArr = this.table;
        if (nodeArr == null || (length = nodeArr.length) <= 0) {
            return null;
        }
        int i3 = (length - 1) & i2;
        Node<K, V> node2 = nodeArr[i3];
        Node<K, V> node3 = node2;
        if (node2 != null) {
            Node<K, V> treeNode = null;
            if (node3.hash == i2 && ((k3 = node3.key) == obj || (obj != null && obj.equals(k3)))) {
                treeNode = node3;
            } else {
                Node<K, V> node4 = node3.next;
                Node<K, V> node5 = node4;
                if (node4 != null) {
                    if (!(node3 instanceof TreeNode)) {
                        do {
                            if (node5.hash == i2 && ((k2 = node5.key) == obj || (obj != null && obj.equals(k2)))) {
                                treeNode = node5;
                                break;
                            }
                            node3 = node5;
                            node = node5.next;
                            node5 = node;
                        } while (node != null);
                    } else {
                        treeNode = ((TreeNode) node3).getTreeNode(i2, obj);
                    }
                }
            }
            if (treeNode != null) {
                if (!z2 || (v2 = treeNode.value) == obj2 || (obj2 != null && obj2.equals(v2))) {
                    if (treeNode instanceof TreeNode) {
                        ((TreeNode) treeNode).removeTreeNode(this, nodeArr, z3);
                    } else if (treeNode == node3) {
                        nodeArr[i3] = treeNode.next;
                    } else {
                        node3.next = treeNode.next;
                    }
                    this.modCount++;
                    this.size--;
                    afterNodeRemoval(treeNode);
                    return treeNode;
                }
                return null;
            }
            return null;
        }
        return null;
    }

    @Override // java.util.AbstractMap, java.util.Map
    public void clear() {
        this.modCount++;
        Node<K, V>[] nodeArr = this.table;
        if (nodeArr != null && this.size > 0) {
            this.size = 0;
            for (int i2 = 0; i2 < nodeArr.length; i2++) {
                nodeArr[i2] = null;
            }
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:21:0x0048, code lost:
    
        r7 = r7 + 1;
     */
    @Override // java.util.AbstractMap, java.util.Map
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public boolean containsValue(java.lang.Object r4) {
        /*
            r3 = this;
            r0 = r3
            java.util.HashMap$Node<K, V>[] r0 = r0.table
            r1 = r0
            r5 = r1
            if (r0 == 0) goto L4e
            r0 = r3
            int r0 = r0.size
            if (r0 <= 0) goto L4e
            r0 = 0
            r7 = r0
        L13:
            r0 = r7
            r1 = r5
            int r1 = r1.length
            if (r0 >= r1) goto L4e
            r0 = r5
            r1 = r7
            r0 = r0[r1]
            r8 = r0
        L20:
            r0 = r8
            if (r0 == 0) goto L48
            r0 = r8
            V r0 = r0.value
            r1 = r0
            r6 = r1
            r1 = r4
            if (r0 == r1) goto L3c
            r0 = r4
            if (r0 == 0) goto L3e
            r0 = r4
            r1 = r6
            boolean r0 = r0.equals(r1)
            if (r0 == 0) goto L3e
        L3c:
            r0 = 1
            return r0
        L3e:
            r0 = r8
            java.util.HashMap$Node<K, V> r0 = r0.next
            r8 = r0
            goto L20
        L48:
            int r7 = r7 + 1
            goto L13
        L4e:
            r0 = 0
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: java.util.HashMap.containsValue(java.lang.Object):boolean");
    }

    @Override // java.util.AbstractMap, java.util.Map
    public Set<K> keySet() {
        Set<K> keySet = this.keySet;
        if (keySet == null) {
            keySet = new KeySet();
            this.keySet = keySet;
        }
        return keySet;
    }

    /* loaded from: rt.jar:java/util/HashMap$KeySet.class */
    final class KeySet extends AbstractSet<K> {
        KeySet() {
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public final int size() {
            return HashMap.this.size;
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
        public final void clear() {
            HashMap.this.clear();
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.List
        public final Iterator<K> iterator() {
            return new KeyIterator();
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public final boolean contains(Object obj) {
            return HashMap.this.containsKey(obj);
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public final boolean remove(Object obj) {
            return HashMap.this.removeNode(HashMap.hash(obj), obj, null, false, true) != null;
        }

        @Override // java.util.Collection, java.lang.Iterable
        public final Spliterator<K> spliterator() {
            return new KeySpliterator(HashMap.this, 0, -1, 0, 0);
        }

        @Override // java.lang.Iterable
        public final void forEach(Consumer<? super K> consumer) {
            Node<K, V>[] nodeArr;
            if (consumer == null) {
                throw new NullPointerException();
            }
            if (HashMap.this.size > 0 && (nodeArr = HashMap.this.table) != null) {
                int i2 = HashMap.this.modCount;
                for (Node<K, V> node : nodeArr) {
                    while (true) {
                        Node<K, V> node2 = node;
                        if (node2 != null) {
                            consumer.accept(node2.key);
                            node = node2.next;
                        }
                    }
                }
                if (HashMap.this.modCount != i2) {
                    throw new ConcurrentModificationException();
                }
            }
        }
    }

    @Override // java.util.AbstractMap, java.util.Map
    public Collection<V> values() {
        Collection<V> values = this.values;
        if (values == null) {
            values = new Values();
            this.values = values;
        }
        return values;
    }

    /* loaded from: rt.jar:java/util/HashMap$Values.class */
    final class Values extends AbstractCollection<V> {
        Values() {
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public final int size() {
            return HashMap.this.size;
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
        public final void clear() {
            HashMap.this.clear();
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.List
        public final Iterator<V> iterator() {
            return new ValueIterator();
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public final boolean contains(Object obj) {
            return HashMap.this.containsValue(obj);
        }

        @Override // java.util.Collection, java.lang.Iterable
        public final Spliterator<V> spliterator() {
            return new ValueSpliterator(HashMap.this, 0, -1, 0, 0);
        }

        @Override // java.lang.Iterable
        public final void forEach(Consumer<? super V> consumer) {
            Node<K, V>[] nodeArr;
            if (consumer == null) {
                throw new NullPointerException();
            }
            if (HashMap.this.size > 0 && (nodeArr = HashMap.this.table) != null) {
                int i2 = HashMap.this.modCount;
                for (Node<K, V> node : nodeArr) {
                    while (true) {
                        Node<K, V> node2 = node;
                        if (node2 != null) {
                            consumer.accept(node2.value);
                            node = node2.next;
                        }
                    }
                }
                if (HashMap.this.modCount != i2) {
                    throw new ConcurrentModificationException();
                }
            }
        }
    }

    @Override // java.util.AbstractMap, java.util.Map
    public Set<Map.Entry<K, V>> entrySet() {
        Set<Map.Entry<K, V>> set = this.entrySet;
        if (set != null) {
            return set;
        }
        EntrySet entrySet = new EntrySet();
        this.entrySet = entrySet;
        return entrySet;
    }

    /* loaded from: rt.jar:java/util/HashMap$EntrySet.class */
    final class EntrySet extends AbstractSet<Map.Entry<K, V>> {
        EntrySet() {
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public final int size() {
            return HashMap.this.size;
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
        public final void clear() {
            HashMap.this.clear();
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.List
        public final Iterator<Map.Entry<K, V>> iterator() {
            return new EntryIterator();
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public final boolean contains(Object obj) {
            if (!(obj instanceof Map.Entry)) {
                return false;
            }
            Map.Entry entry = (Map.Entry) obj;
            Object key = entry.getKey();
            Node<K, V> node = HashMap.this.getNode(HashMap.hash(key), key);
            return node != null && node.equals(entry);
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public final boolean remove(Object obj) {
            if (obj instanceof Map.Entry) {
                Map.Entry entry = (Map.Entry) obj;
                Object key = entry.getKey();
                return HashMap.this.removeNode(HashMap.hash(key), key, entry.getValue(), true, true) != null;
            }
            return false;
        }

        @Override // java.util.Collection, java.lang.Iterable
        public final Spliterator<Map.Entry<K, V>> spliterator() {
            return new EntrySpliterator(HashMap.this, 0, -1, 0, 0);
        }

        @Override // java.lang.Iterable
        public final void forEach(Consumer<? super Map.Entry<K, V>> consumer) {
            Node<K, V>[] nodeArr;
            if (consumer == null) {
                throw new NullPointerException();
            }
            if (HashMap.this.size > 0 && (nodeArr = HashMap.this.table) != null) {
                int i2 = HashMap.this.modCount;
                for (Node<K, V> node : nodeArr) {
                    while (true) {
                        Node<K, V> node2 = node;
                        if (node2 != null) {
                            consumer.accept(node2);
                            node = node2.next;
                        }
                    }
                }
                if (HashMap.this.modCount != i2) {
                    throw new ConcurrentModificationException();
                }
            }
        }
    }

    @Override // java.util.Map
    public V getOrDefault(Object obj, V v2) {
        Node<K, V> node = getNode(hash(obj), obj);
        return node == null ? v2 : node.value;
    }

    @Override // java.util.Map
    public V putIfAbsent(K k2, V v2) {
        return putVal(hash(k2), k2, v2, true, true);
    }

    @Override // java.util.Map
    public boolean remove(Object obj, Object obj2) {
        return removeNode(hash(obj), obj, obj2, true, true) != null;
    }

    @Override // java.util.Map
    public boolean replace(K k2, V v2, V v3) {
        Node<K, V> node = getNode(hash(k2), k2);
        if (node != null) {
            V v4 = node.value;
            if (v4 == v2 || (v4 != null && v4.equals(v2))) {
                node.value = v3;
                afterNodeAccess(node);
                return true;
            }
            return false;
        }
        return false;
    }

    @Override // java.util.Map
    public V replace(K k2, V v2) {
        Node<K, V> node = getNode(hash(k2), k2);
        if (node != null) {
            V v3 = node.value;
            node.value = v2;
            afterNodeAccess(node);
            return v3;
        }
        return null;
    }

    /* JADX WARN: Removed duplicated region for block: B:12:0x0038  */
    @Override // java.util.Map
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public V computeIfAbsent(K r9, java.util.function.Function<? super K, ? extends V> r10) {
        /*
            Method dump skipped, instructions count: 309
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: java.util.HashMap.computeIfAbsent(java.lang.Object, java.util.function.Function):java.lang.Object");
    }

    @Override // java.util.Map
    public V computeIfPresent(K k2, BiFunction<? super K, ? super V, ? extends V> biFunction) {
        V v2;
        if (biFunction == null) {
            throw new NullPointerException();
        }
        int iHash = hash(k2);
        Node<K, V> node = getNode(iHash, k2);
        if (node != null && (v2 = node.value) != null) {
            V vApply = biFunction.apply(k2, v2);
            if (vApply != null) {
                node.value = vApply;
                afterNodeAccess(node);
                return vApply;
            }
            removeNode(iHash, k2, null, false, true);
            return null;
        }
        return null;
    }

    /* JADX WARN: Removed duplicated region for block: B:12:0x0038  */
    @Override // java.util.Map
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public V compute(K r9, java.util.function.BiFunction<? super K, ? super V, ? extends V> r10) {
        /*
            Method dump skipped, instructions count: 318
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: java.util.HashMap.compute(java.lang.Object, java.util.function.BiFunction):java.lang.Object");
    }

    /* JADX WARN: Removed duplicated region for block: B:16:0x0045  */
    @Override // java.util.Map
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public V merge(K r9, V r10, java.util.function.BiFunction<? super V, ? super V, ? extends V> r11) {
        /*
            Method dump skipped, instructions count: 335
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: java.util.HashMap.merge(java.lang.Object, java.lang.Object, java.util.function.BiFunction):java.lang.Object");
    }

    @Override // java.util.Map
    public void forEach(BiConsumer<? super K, ? super V> biConsumer) {
        Node<K, V>[] nodeArr;
        if (biConsumer == null) {
            throw new NullPointerException();
        }
        if (this.size > 0 && (nodeArr = this.table) != null) {
            int i2 = this.modCount;
            for (Node<K, V> node : nodeArr) {
                while (true) {
                    Node<K, V> node2 = node;
                    if (node2 != null) {
                        biConsumer.accept(node2.key, node2.value);
                        node = node2.next;
                    }
                }
            }
            if (this.modCount != i2) {
                throw new ConcurrentModificationException();
            }
        }
    }

    @Override // java.util.Map
    public void replaceAll(BiFunction<? super K, ? super V, ? extends V> biFunction) {
        Node<K, V>[] nodeArr;
        if (biFunction == null) {
            throw new NullPointerException();
        }
        if (this.size > 0 && (nodeArr = this.table) != null) {
            int i2 = this.modCount;
            for (Node<K, V> node : nodeArr) {
                while (true) {
                    Node<K, V> node2 = node;
                    if (node2 != null) {
                        node2.value = biFunction.apply(node2.key, node2.value);
                        node = node2.next;
                    }
                }
            }
            if (this.modCount != i2) {
                throw new ConcurrentModificationException();
            }
        }
    }

    @Override // java.util.AbstractMap
    public Object clone() {
        try {
            HashMap map = (HashMap) super.clone();
            map.reinitialize();
            map.putMapEntries(this, false);
            return map;
        } catch (CloneNotSupportedException e2) {
            throw new InternalError(e2);
        }
    }

    final float loadFactor() {
        return this.loadFactor;
    }

    final int capacity() {
        if (this.table != null) {
            return this.table.length;
        }
        if (this.threshold > 0) {
            return this.threshold;
        }
        return 16;
    }

    private void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
        int iCapacity = capacity();
        objectOutputStream.defaultWriteObject();
        objectOutputStream.writeInt(iCapacity);
        objectOutputStream.writeInt(this.size);
        internalWriteEntries(objectOutputStream);
    }

    /* JADX WARN: Multi-variable type inference failed */
    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        int iTableSizeFor;
        float f2 = objectInputStream.readFields().get("loadFactor", 0.75f);
        if (f2 <= 0.0f || Float.isNaN(f2)) {
            throw new InvalidObjectException("Illegal load factor: " + f2);
        }
        float fMin = Math.min(Math.max(0.25f, f2), 4.0f);
        UnsafeHolder.putLoadFactor(this, fMin);
        reinitialize();
        objectInputStream.readInt();
        int i2 = objectInputStream.readInt();
        if (i2 < 0) {
            throw new InvalidObjectException("Illegal mappings count: " + i2);
        }
        if (i2 != 0 && i2 > 0) {
            float f3 = (i2 / fMin) + 1.0f;
            if (f3 < 16.0f) {
                iTableSizeFor = 16;
            } else {
                iTableSizeFor = f3 >= 1.0737418E9f ? 1073741824 : tableSizeFor((int) f3);
            }
            int i3 = iTableSizeFor;
            float f4 = i3 * fMin;
            this.threshold = (i3 >= 1073741824 || f4 >= 1.0737418E9f) ? Integer.MAX_VALUE : (int) f4;
            SharedSecrets.getJavaOISAccess().checkArray(objectInputStream, Map.Entry[].class, i3);
            this.table = new Node[i3];
            for (int i4 = 0; i4 < i2; i4++) {
                Object object = objectInputStream.readObject();
                putVal(hash(object), object, objectInputStream.readObject(), false, false);
            }
        }
    }

    /* loaded from: rt.jar:java/util/HashMap$UnsafeHolder.class */
    private static final class UnsafeHolder {
        private static final Unsafe unsafe = Unsafe.getUnsafe();
        private static final long LF_OFFSET;

        private UnsafeHolder() {
            throw new InternalError();
        }

        static {
            try {
                LF_OFFSET = unsafe.objectFieldOffset(HashMap.class.getDeclaredField("loadFactor"));
            } catch (NoSuchFieldException e2) {
                throw new InternalError();
            }
        }

        static void putLoadFactor(HashMap<?, ?> map, float f2) {
            unsafe.putFloat(map, LF_OFFSET, f2);
        }
    }

    /* loaded from: rt.jar:java/util/HashMap$HashIterator.class */
    abstract class HashIterator {
        Node<K, V> next;
        Node<K, V> current;
        int expectedModCount;
        int index;

        HashIterator() {
            this.expectedModCount = HashMap.this.modCount;
            Node<K, V>[] nodeArr = HashMap.this.table;
            this.next = null;
            this.current = null;
            this.index = 0;
            if (nodeArr != null && HashMap.this.size > 0) {
                while (this.index < nodeArr.length) {
                    int i2 = this.index;
                    this.index = i2 + 1;
                    Node<K, V> node = nodeArr[i2];
                    this.next = node;
                    if (node != null) {
                        return;
                    }
                }
            }
        }

        public final boolean hasNext() {
            return this.next != null;
        }

        final Node<K, V> nextNode() {
            Node<K, V>[] nodeArr;
            Node<K, V> node = this.next;
            if (HashMap.this.modCount != this.expectedModCount) {
                throw new ConcurrentModificationException();
            }
            if (node == null) {
                throw new NoSuchElementException();
            }
            this.current = node;
            Node<K, V> node2 = node.next;
            this.next = node2;
            if (node2 == null && (nodeArr = HashMap.this.table) != null) {
                while (this.index < nodeArr.length) {
                    int i2 = this.index;
                    this.index = i2 + 1;
                    Node<K, V> node3 = nodeArr[i2];
                    this.next = node3;
                    if (node3 != null) {
                        break;
                    }
                }
            }
            return node;
        }

        public final void remove() {
            Node<K, V> node = this.current;
            if (node == null) {
                throw new IllegalStateException();
            }
            if (HashMap.this.modCount != this.expectedModCount) {
                throw new ConcurrentModificationException();
            }
            this.current = null;
            K k2 = node.key;
            HashMap.this.removeNode(HashMap.hash(k2), k2, null, false, false);
            this.expectedModCount = HashMap.this.modCount;
        }
    }

    /* loaded from: rt.jar:java/util/HashMap$KeyIterator.class */
    final class KeyIterator extends HashMap<K, V>.HashIterator implements Iterator<K> {
        KeyIterator() {
            super();
        }

        @Override // java.util.Iterator
        public final K next() {
            return nextNode().key;
        }
    }

    /* loaded from: rt.jar:java/util/HashMap$ValueIterator.class */
    final class ValueIterator extends HashMap<K, V>.HashIterator implements Iterator<V> {
        ValueIterator() {
            super();
        }

        @Override // java.util.Iterator
        public final V next() {
            return nextNode().value;
        }
    }

    /* loaded from: rt.jar:java/util/HashMap$EntryIterator.class */
    final class EntryIterator extends HashMap<K, V>.HashIterator implements Iterator<Map.Entry<K, V>> {
        EntryIterator() {
            super();
        }

        @Override // java.util.Iterator
        public final Map.Entry<K, V> next() {
            return nextNode();
        }
    }

    /* loaded from: rt.jar:java/util/HashMap$HashMapSpliterator.class */
    static class HashMapSpliterator<K, V> {
        final HashMap<K, V> map;
        Node<K, V> current;
        int index;
        int fence;
        int est;
        int expectedModCount;

        HashMapSpliterator(HashMap<K, V> map, int i2, int i3, int i4, int i5) {
            this.map = map;
            this.index = i2;
            this.fence = i3;
            this.est = i4;
            this.expectedModCount = i5;
        }

        final int getFence() {
            int i2 = this.fence;
            int i3 = i2;
            if (i2 < 0) {
                HashMap<K, V> map = this.map;
                this.est = map.size;
                this.expectedModCount = map.modCount;
                Node<K, V>[] nodeArr = map.table;
                int length = nodeArr == null ? 0 : nodeArr.length;
                this.fence = length;
                i3 = length;
            }
            return i3;
        }

        public final long estimateSize() {
            getFence();
            return this.est;
        }
    }

    /* loaded from: rt.jar:java/util/HashMap$KeySpliterator.class */
    static final class KeySpliterator<K, V> extends HashMapSpliterator<K, V> implements Spliterator<K> {
        KeySpliterator(HashMap<K, V> map, int i2, int i3, int i4, int i5) {
            super(map, i2, i3, i4, i5);
        }

        @Override // java.util.Spliterator
        public KeySpliterator<K, V> trySplit() {
            int fence = getFence();
            int i2 = this.index;
            int i3 = (i2 + fence) >>> 1;
            if (i2 >= i3 || this.current != null) {
                return null;
            }
            HashMap<K, V> map = this.map;
            this.index = i3;
            int i4 = this.est >>> 1;
            this.est = i4;
            return new KeySpliterator<>(map, i2, i3, i4, this.expectedModCount);
        }

        @Override // java.util.Spliterator
        public void forEachRemaining(Consumer<? super K> consumer) {
            int i2;
            if (consumer == null) {
                throw new NullPointerException();
            }
            HashMap<K, V> map = this.map;
            Node<K, V>[] nodeArr = map.table;
            int i3 = this.fence;
            int i4 = i3;
            if (i3 < 0) {
                int i5 = map.modCount;
                this.expectedModCount = i5;
                i2 = i5;
                int length = nodeArr == null ? 0 : nodeArr.length;
                this.fence = length;
                i4 = length;
            } else {
                i2 = this.expectedModCount;
            }
            if (nodeArr == null || nodeArr.length < i4) {
                return;
            }
            int i6 = this.index;
            int i7 = i6;
            if (i6 >= 0) {
                int i8 = i4;
                this.index = i8;
                if (i7 < i8 || this.current != null) {
                    Node<K, V> node = this.current;
                    this.current = null;
                    while (true) {
                        if (node == null) {
                            int i9 = i7;
                            i7++;
                            node = nodeArr[i9];
                        } else {
                            consumer.accept(node.key);
                            node = node.next;
                        }
                        if (node == null && i7 >= i4) {
                            break;
                        }
                    }
                    if (map.modCount != i2) {
                        throw new ConcurrentModificationException();
                    }
                }
            }
        }

        @Override // java.util.Spliterator
        public boolean tryAdvance(Consumer<? super K> consumer) {
            if (consumer == null) {
                throw new NullPointerException();
            }
            Node<K, V>[] nodeArr = this.map.table;
            if (nodeArr == null) {
                return false;
            }
            int length = nodeArr.length;
            int fence = getFence();
            if (length < fence || this.index < 0) {
                return false;
            }
            while (true) {
                if (this.current != null || this.index < fence) {
                    if (this.current == null) {
                        int i2 = this.index;
                        this.index = i2 + 1;
                        this.current = nodeArr[i2];
                    } else {
                        K k2 = this.current.key;
                        this.current = this.current.next;
                        consumer.accept(k2);
                        if (this.map.modCount != this.expectedModCount) {
                            throw new ConcurrentModificationException();
                        }
                        return true;
                    }
                } else {
                    return false;
                }
            }
        }

        @Override // java.util.Spliterator
        public int characteristics() {
            return ((this.fence < 0 || this.est == this.map.size) ? 64 : 0) | 1;
        }
    }

    /* loaded from: rt.jar:java/util/HashMap$ValueSpliterator.class */
    static final class ValueSpliterator<K, V> extends HashMapSpliterator<K, V> implements Spliterator<V> {
        ValueSpliterator(HashMap<K, V> map, int i2, int i3, int i4, int i5) {
            super(map, i2, i3, i4, i5);
        }

        @Override // java.util.Spliterator
        public ValueSpliterator<K, V> trySplit() {
            int fence = getFence();
            int i2 = this.index;
            int i3 = (i2 + fence) >>> 1;
            if (i2 >= i3 || this.current != null) {
                return null;
            }
            HashMap<K, V> map = this.map;
            this.index = i3;
            int i4 = this.est >>> 1;
            this.est = i4;
            return new ValueSpliterator<>(map, i2, i3, i4, this.expectedModCount);
        }

        @Override // java.util.Spliterator
        public void forEachRemaining(Consumer<? super V> consumer) {
            int i2;
            if (consumer == null) {
                throw new NullPointerException();
            }
            HashMap<K, V> map = this.map;
            Node<K, V>[] nodeArr = map.table;
            int i3 = this.fence;
            int i4 = i3;
            if (i3 < 0) {
                int i5 = map.modCount;
                this.expectedModCount = i5;
                i2 = i5;
                int length = nodeArr == null ? 0 : nodeArr.length;
                this.fence = length;
                i4 = length;
            } else {
                i2 = this.expectedModCount;
            }
            if (nodeArr == null || nodeArr.length < i4) {
                return;
            }
            int i6 = this.index;
            int i7 = i6;
            if (i6 >= 0) {
                int i8 = i4;
                this.index = i8;
                if (i7 < i8 || this.current != null) {
                    Node<K, V> node = this.current;
                    this.current = null;
                    while (true) {
                        if (node == null) {
                            int i9 = i7;
                            i7++;
                            node = nodeArr[i9];
                        } else {
                            consumer.accept(node.value);
                            node = node.next;
                        }
                        if (node == null && i7 >= i4) {
                            break;
                        }
                    }
                    if (map.modCount != i2) {
                        throw new ConcurrentModificationException();
                    }
                }
            }
        }

        @Override // java.util.Spliterator
        public boolean tryAdvance(Consumer<? super V> consumer) {
            if (consumer == null) {
                throw new NullPointerException();
            }
            Node<K, V>[] nodeArr = this.map.table;
            if (nodeArr == null) {
                return false;
            }
            int length = nodeArr.length;
            int fence = getFence();
            if (length < fence || this.index < 0) {
                return false;
            }
            while (true) {
                if (this.current != null || this.index < fence) {
                    if (this.current == null) {
                        int i2 = this.index;
                        this.index = i2 + 1;
                        this.current = nodeArr[i2];
                    } else {
                        V v2 = this.current.value;
                        this.current = this.current.next;
                        consumer.accept(v2);
                        if (this.map.modCount != this.expectedModCount) {
                            throw new ConcurrentModificationException();
                        }
                        return true;
                    }
                } else {
                    return false;
                }
            }
        }

        @Override // java.util.Spliterator
        public int characteristics() {
            return (this.fence < 0 || this.est == this.map.size) ? 64 : 0;
        }
    }

    /* loaded from: rt.jar:java/util/HashMap$EntrySpliterator.class */
    static final class EntrySpliterator<K, V> extends HashMapSpliterator<K, V> implements Spliterator<Map.Entry<K, V>> {
        EntrySpliterator(HashMap<K, V> map, int i2, int i3, int i4, int i5) {
            super(map, i2, i3, i4, i5);
        }

        @Override // java.util.Spliterator
        public EntrySpliterator<K, V> trySplit() {
            int fence = getFence();
            int i2 = this.index;
            int i3 = (i2 + fence) >>> 1;
            if (i2 >= i3 || this.current != null) {
                return null;
            }
            HashMap<K, V> map = this.map;
            this.index = i3;
            int i4 = this.est >>> 1;
            this.est = i4;
            return new EntrySpliterator<>(map, i2, i3, i4, this.expectedModCount);
        }

        @Override // java.util.Spliterator
        public void forEachRemaining(Consumer<? super Map.Entry<K, V>> consumer) {
            int i2;
            if (consumer == null) {
                throw new NullPointerException();
            }
            HashMap<K, V> map = this.map;
            Node<K, V>[] nodeArr = map.table;
            int i3 = this.fence;
            int i4 = i3;
            if (i3 < 0) {
                int i5 = map.modCount;
                this.expectedModCount = i5;
                i2 = i5;
                int length = nodeArr == null ? 0 : nodeArr.length;
                this.fence = length;
                i4 = length;
            } else {
                i2 = this.expectedModCount;
            }
            if (nodeArr == null || nodeArr.length < i4) {
                return;
            }
            int i6 = this.index;
            int i7 = i6;
            if (i6 >= 0) {
                int i8 = i4;
                this.index = i8;
                if (i7 < i8 || this.current != null) {
                    Node<K, V> node = this.current;
                    this.current = null;
                    while (true) {
                        if (node == null) {
                            int i9 = i7;
                            i7++;
                            node = nodeArr[i9];
                        } else {
                            consumer.accept(node);
                            node = node.next;
                        }
                        if (node == null && i7 >= i4) {
                            break;
                        }
                    }
                    if (map.modCount != i2) {
                        throw new ConcurrentModificationException();
                    }
                }
            }
        }

        @Override // java.util.Spliterator
        public boolean tryAdvance(Consumer<? super Map.Entry<K, V>> consumer) {
            if (consumer == null) {
                throw new NullPointerException();
            }
            Node<K, V>[] nodeArr = this.map.table;
            if (nodeArr == null) {
                return false;
            }
            int length = nodeArr.length;
            int fence = getFence();
            if (length < fence || this.index < 0) {
                return false;
            }
            while (true) {
                if (this.current != null || this.index < fence) {
                    if (this.current == null) {
                        int i2 = this.index;
                        this.index = i2 + 1;
                        this.current = nodeArr[i2];
                    } else {
                        Node<K, V> node = this.current;
                        this.current = this.current.next;
                        consumer.accept(node);
                        if (this.map.modCount != this.expectedModCount) {
                            throw new ConcurrentModificationException();
                        }
                        return true;
                    }
                } else {
                    return false;
                }
            }
        }

        @Override // java.util.Spliterator
        public int characteristics() {
            return ((this.fence < 0 || this.est == this.map.size) ? 64 : 0) | 1;
        }
    }

    Node<K, V> newNode(int i2, K k2, V v2, Node<K, V> node) {
        return new Node<>(i2, k2, v2, node);
    }

    Node<K, V> replacementNode(Node<K, V> node, Node<K, V> node2) {
        return new Node<>(node.hash, node.key, node.value, node2);
    }

    TreeNode<K, V> newTreeNode(int i2, K k2, V v2, Node<K, V> node) {
        return new TreeNode<>(i2, k2, v2, node);
    }

    TreeNode<K, V> replacementTreeNode(Node<K, V> node, Node<K, V> node2) {
        return new TreeNode<>(node.hash, node.key, node.value, node2);
    }

    void reinitialize() {
        this.table = null;
        this.entrySet = null;
        this.keySet = null;
        this.values = null;
        this.modCount = 0;
        this.threshold = 0;
        this.size = 0;
    }

    void afterNodeAccess(Node<K, V> node) {
    }

    void afterNodeInsertion(boolean z2) {
    }

    void afterNodeRemoval(Node<K, V> node) {
    }

    void internalWriteEntries(ObjectOutputStream objectOutputStream) throws IOException {
        Node<K, V>[] nodeArr;
        if (this.size > 0 && (nodeArr = this.table) != null) {
            for (Node<K, V> node : nodeArr) {
                while (true) {
                    Node<K, V> node2 = node;
                    if (node2 != null) {
                        objectOutputStream.writeObject(node2.key);
                        objectOutputStream.writeObject(node2.value);
                        node = node2.next;
                    }
                }
            }
        }
    }

    /* loaded from: rt.jar:java/util/HashMap$TreeNode.class */
    static final class TreeNode<K, V> extends LinkedHashMap.Entry<K, V> {
        TreeNode<K, V> parent;
        TreeNode<K, V> left;
        TreeNode<K, V> right;
        TreeNode<K, V> prev;
        boolean red;
        static final /* synthetic */ boolean $assertionsDisabled;

        static {
            $assertionsDisabled = !HashMap.class.desiredAssertionStatus();
        }

        TreeNode(int i2, K k2, V v2, Node<K, V> node) {
            super(i2, k2, v2, node);
        }

        final TreeNode<K, V> root() {
            TreeNode<K, V> treeNode = this;
            while (true) {
                TreeNode<K, V> treeNode2 = treeNode;
                TreeNode<K, V> treeNode3 = treeNode2.parent;
                if (treeNode3 == null) {
                    return treeNode2;
                }
                treeNode = treeNode3;
            }
        }

        static <K, V> void moveRootToFront(Node<K, V>[] nodeArr, TreeNode<K, V> treeNode) {
            int length;
            if (treeNode != null && nodeArr != null && (length = nodeArr.length) > 0) {
                int i2 = (length - 1) & treeNode.hash;
                TreeNode<K, V> treeNode2 = (TreeNode) nodeArr[i2];
                if (treeNode != treeNode2) {
                    nodeArr[i2] = treeNode;
                    TreeNode<K, V> treeNode3 = treeNode.prev;
                    Node<K, V> node = treeNode.next;
                    if (node != null) {
                        ((TreeNode) node).prev = treeNode3;
                    }
                    if (treeNode3 != null) {
                        treeNode3.next = node;
                    }
                    if (treeNode2 != null) {
                        treeNode2.prev = treeNode;
                    }
                    treeNode.next = treeNode2;
                    treeNode.prev = null;
                }
                if (!$assertionsDisabled && !checkInvariants(treeNode)) {
                    throw new AssertionError();
                }
            }
        }

        /* JADX WARN: Removed duplicated region for block: B:27:0x0072 A[PHI: r8
  0x0072: PHI (r8v2 java.lang.Class<?>) = (r8v1 java.lang.Class<?>), (r8v4 java.lang.Class<?>) binds: [B:24:0x0066, B:26:0x006f] A[DONT_GENERATE, DONT_INLINE]] */
        /* JADX WARN: Removed duplicated region for block: B:34:0x0090 A[PHI: r8
  0x0090: PHI (r8v3 java.lang.Class<?>) = (r8v2 java.lang.Class<?>), (r8v4 java.lang.Class<?>) binds: [B:28:0x007c, B:26:0x006f] A[DONT_GENERATE, DONT_INLINE]] */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct code enable 'Show inconsistent code' option in preferences
        */
        final java.util.HashMap.TreeNode<K, V> find(int r6, java.lang.Object r7, java.lang.Class<?> r8) {
            /*
                r5 = this;
                r0 = r5
                r9 = r0
            L3:
                r0 = r9
                java.util.HashMap$TreeNode<K, V> r0 = r0.left
                r13 = r0
                r0 = r9
                java.util.HashMap$TreeNode<K, V> r0 = r0.right
                r14 = r0
                r0 = r9
                int r0 = r0.hash
                r1 = r0
                r10 = r1
                r1 = r6
                if (r0 <= r1) goto L24
                r0 = r13
                r9 = r0
                goto La5
            L24:
                r0 = r10
                r1 = r6
                if (r0 >= r1) goto L31
                r0 = r14
                r9 = r0
                goto La5
            L31:
                r0 = r9
                K r0 = r0.key
                r1 = r0
                r12 = r1
                r1 = r7
                if (r0 == r1) goto L4a
                r0 = r7
                if (r0 == 0) goto L4d
                r0 = r7
                r1 = r12
                boolean r0 = r0.equals(r1)
                if (r0 == 0) goto L4d
            L4a:
                r0 = r9
                return r0
            L4d:
                r0 = r13
                if (r0 != 0) goto L59
                r0 = r14
                r9 = r0
                goto La5
            L59:
                r0 = r14
                if (r0 != 0) goto L65
                r0 = r13
                r9 = r0
                goto La5
            L65:
                r0 = r8
                if (r0 != 0) goto L72
                r0 = r7
                java.lang.Class r0 = java.util.HashMap.comparableClassFor(r0)
                r1 = r0
                r8 = r1
                if (r0 == 0) goto L90
            L72:
                r0 = r8
                r1 = r7
                r2 = r12
                int r0 = java.util.HashMap.compareComparables(r0, r1, r2)
                r1 = r0
                r11 = r1
                if (r0 == 0) goto L90
                r0 = r11
                if (r0 >= 0) goto L89
                r0 = r13
                goto L8b
            L89:
                r0 = r14
            L8b:
                r9 = r0
                goto La5
            L90:
                r0 = r14
                r1 = r6
                r2 = r7
                r3 = r8
                java.util.HashMap$TreeNode r0 = r0.find(r1, r2, r3)
                r1 = r0
                r15 = r1
                if (r0 == 0) goto La1
                r0 = r15
                return r0
            La1:
                r0 = r13
                r9 = r0
            La5:
                r0 = r9
                if (r0 != 0) goto L3
                r0 = 0
                return r0
            */
            throw new UnsupportedOperationException("Method not decompiled: java.util.HashMap.TreeNode.find(int, java.lang.Object, java.lang.Class):java.util.HashMap$TreeNode");
        }

        final TreeNode<K, V> getTreeNode(int i2, Object obj) {
            return (this.parent != null ? root() : this).find(i2, obj, null);
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
            throw new UnsupportedOperationException("Method not decompiled: java.util.HashMap.TreeNode.tieBreakOrder(java.lang.Object, java.lang.Object):int");
        }

        /* JADX WARN: Removed duplicated region for block: B:19:0x0077 A[PHI: r12
  0x0077: PHI (r12v2 java.lang.Class<?>) = (r12v1 java.lang.Class<?>), (r12v4 java.lang.Class<?>) binds: [B:16:0x0069, B:18:0x0074] A[DONT_GENERATE, DONT_INLINE]] */
        /* JADX WARN: Removed duplicated region for block: B:21:0x0086 A[PHI: r12
  0x0086: PHI (r12v3 java.lang.Class<?>) = (r12v2 java.lang.Class<?>), (r12v4 java.lang.Class<?>) binds: [B:20:0x0083, B:18:0x0074] A[DONT_GENERATE, DONT_INLINE]] */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct code enable 'Show inconsistent code' option in preferences
        */
        final void treeify(java.util.HashMap.Node<K, V>[] r6) {
            /*
                Method dump skipped, instructions count: 221
                To view this dump change 'Code comments level' option to 'DEBUG'
            */
            throw new UnsupportedOperationException("Method not decompiled: java.util.HashMap.TreeNode.treeify(java.util.HashMap$Node[]):void");
        }

        final Node<K, V> untreeify(HashMap<K, V> map) {
            Node<K, V> node = null;
            Node<K, V> node2 = null;
            Node<K, V> node3 = this;
            while (true) {
                Node<K, V> node4 = node3;
                if (node4 != null) {
                    Node<K, V> nodeReplacementNode = map.replacementNode(node4, null);
                    if (node2 == null) {
                        node = nodeReplacementNode;
                    } else {
                        node2.next = nodeReplacementNode;
                    }
                    node2 = nodeReplacementNode;
                    node3 = node4.next;
                } else {
                    return node;
                }
            }
        }

        /* JADX WARN: Removed duplicated region for block: B:25:0x0068 A[PHI: r12
  0x0068: PHI (r12v2 java.lang.Class<?>) = (r12v1 java.lang.Class<?>), (r12v4 java.lang.Class<?>) binds: [B:22:0x005a, B:24:0x0065] A[DONT_GENERATE, DONT_INLINE]] */
        /* JADX WARN: Removed duplicated region for block: B:27:0x0077 A[PHI: r12
  0x0077: PHI (r12v3 java.lang.Class<?>) = (r12v2 java.lang.Class<?>), (r12v4 java.lang.Class<?>) binds: [B:26:0x0074, B:24:0x0065] A[DONT_GENERATE, DONT_INLINE]] */
        /* JADX WARN: Removed duplicated region for block: B:33:0x009a  */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct code enable 'Show inconsistent code' option in preferences
        */
        final java.util.HashMap.TreeNode<K, V> putTreeVal(java.util.HashMap<K, V> r7, java.util.HashMap.Node<K, V>[] r8, int r9, K r10, V r11) {
            /*
                Method dump skipped, instructions count: 314
                To view this dump change 'Code comments level' option to 'DEBUG'
            */
            throw new UnsupportedOperationException("Method not decompiled: java.util.HashMap.TreeNode.putTreeVal(java.util.HashMap, java.util.HashMap$Node[], int, java.lang.Object, java.lang.Object):java.util.HashMap$TreeNode");
        }

        final void removeTreeNode(HashMap<K, V> map, Node<K, V>[] nodeArr, boolean z2) {
            int length;
            TreeNode<K, V> treeNode;
            TreeNode<K, V> treeNode2;
            TreeNode<K, V> treeNode3;
            if (nodeArr == null || (length = nodeArr.length) == 0) {
                return;
            }
            int i2 = (length - 1) & this.hash;
            TreeNode<K, V> treeNode4 = (TreeNode) nodeArr[i2];
            TreeNode<K, V> treeNodeRoot = treeNode4;
            TreeNode<K, V> treeNode5 = (TreeNode) this.next;
            TreeNode<K, V> treeNode6 = this.prev;
            if (treeNode6 == null) {
                treeNode4 = treeNode5;
                nodeArr[i2] = treeNode5;
            } else {
                treeNode6.next = treeNode5;
            }
            if (treeNode5 != null) {
                treeNode5.prev = treeNode6;
            }
            if (treeNode4 == null) {
                return;
            }
            if (treeNodeRoot.parent != null) {
                treeNodeRoot = treeNodeRoot.root();
            }
            if (treeNodeRoot == null || (z2 && (treeNodeRoot.right == null || (treeNode3 = treeNodeRoot.left) == null || treeNode3.left == null))) {
                nodeArr[i2] = treeNode4.untreeify(map);
                return;
            }
            TreeNode<K, V> treeNode7 = this.left;
            TreeNode<K, V> treeNode8 = this.right;
            if (treeNode7 != null && treeNode8 != null) {
                TreeNode<K, V> treeNode9 = treeNode8;
                while (true) {
                    treeNode2 = treeNode9;
                    TreeNode<K, V> treeNode10 = treeNode2.left;
                    if (treeNode10 == null) {
                        break;
                    } else {
                        treeNode9 = treeNode10;
                    }
                }
                boolean z3 = treeNode2.red;
                treeNode2.red = this.red;
                this.red = z3;
                TreeNode<K, V> treeNode11 = treeNode2.right;
                TreeNode<K, V> treeNode12 = this.parent;
                if (treeNode2 == treeNode8) {
                    this.parent = treeNode2;
                    treeNode2.right = this;
                } else {
                    TreeNode<K, V> treeNode13 = treeNode2.parent;
                    this.parent = treeNode13;
                    if (treeNode13 != null) {
                        if (treeNode2 == treeNode13.left) {
                            treeNode13.left = this;
                        } else {
                            treeNode13.right = this;
                        }
                    }
                    treeNode2.right = treeNode8;
                    if (treeNode8 != null) {
                        treeNode8.parent = treeNode2;
                    }
                }
                this.left = null;
                this.right = treeNode11;
                if (treeNode11 != null) {
                    treeNode11.parent = this;
                }
                treeNode2.left = treeNode7;
                if (treeNode7 != null) {
                    treeNode7.parent = treeNode2;
                }
                treeNode2.parent = treeNode12;
                if (treeNode12 == null) {
                    treeNodeRoot = treeNode2;
                } else if (this == treeNode12.left) {
                    treeNode12.left = treeNode2;
                } else {
                    treeNode12.right = treeNode2;
                }
                if (treeNode11 != null) {
                    treeNode = treeNode11;
                } else {
                    treeNode = this;
                }
            } else if (treeNode7 != null) {
                treeNode = treeNode7;
            } else if (treeNode8 != null) {
                treeNode = treeNode8;
            } else {
                treeNode = this;
            }
            if (treeNode != this) {
                TreeNode<K, V> treeNode14 = this.parent;
                treeNode.parent = treeNode14;
                if (treeNode14 == null) {
                    treeNodeRoot = treeNode;
                } else if (this == treeNode14.left) {
                    treeNode14.left = treeNode;
                } else {
                    treeNode14.right = treeNode;
                }
                this.parent = null;
                this.right = null;
                this.left = null;
            }
            TreeNode<K, V> treeNodeBalanceDeletion = this.red ? treeNodeRoot : balanceDeletion(treeNodeRoot, treeNode);
            if (treeNode == this) {
                TreeNode<K, V> treeNode15 = this.parent;
                this.parent = null;
                if (treeNode15 != null) {
                    if (this == treeNode15.left) {
                        treeNode15.left = null;
                    } else if (this == treeNode15.right) {
                        treeNode15.right = null;
                    }
                }
            }
            if (z2) {
                moveRootToFront(nodeArr, treeNodeBalanceDeletion);
            }
        }

        final void split(HashMap<K, V> map, Node<K, V>[] nodeArr, int i2, int i3) {
            TreeNode<K, V> treeNode = null;
            TreeNode<K, V> treeNode2 = null;
            TreeNode<K, V> treeNode3 = null;
            TreeNode<K, V> treeNode4 = null;
            int i4 = 0;
            int i5 = 0;
            TreeNode<K, V> treeNode5 = this;
            while (true) {
                TreeNode<K, V> treeNode6 = treeNode5;
                if (treeNode6 == null) {
                    break;
                }
                TreeNode<K, V> treeNode7 = (TreeNode) treeNode6.next;
                treeNode6.next = null;
                if ((treeNode6.hash & i3) == 0) {
                    TreeNode<K, V> treeNode8 = treeNode2;
                    treeNode6.prev = treeNode8;
                    if (treeNode8 == null) {
                        treeNode = treeNode6;
                    } else {
                        treeNode2.next = treeNode6;
                    }
                    treeNode2 = treeNode6;
                    i4++;
                } else {
                    TreeNode<K, V> treeNode9 = treeNode4;
                    treeNode6.prev = treeNode9;
                    if (treeNode9 == null) {
                        treeNode3 = treeNode6;
                    } else {
                        treeNode4.next = treeNode6;
                    }
                    treeNode4 = treeNode6;
                    i5++;
                }
                treeNode5 = treeNode7;
            }
            if (treeNode != null) {
                if (i4 <= 6) {
                    nodeArr[i2] = treeNode.untreeify(map);
                } else {
                    nodeArr[i2] = treeNode;
                    if (treeNode3 != null) {
                        treeNode.treeify(nodeArr);
                    }
                }
            }
            if (treeNode3 != null) {
                if (i5 <= 6) {
                    nodeArr[i2 + i3] = treeNode3.untreeify(map);
                    return;
                }
                nodeArr[i2 + i3] = treeNode3;
                if (treeNode != null) {
                    treeNode3.treeify(nodeArr);
                }
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
}
