package java.util;

import java.io.IOException;
import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import sun.misc.SharedSecrets;

/* loaded from: rt.jar:java/util/HashSet.class */
public class HashSet<E> extends AbstractSet<E> implements Set<E>, Cloneable, Serializable {
    static final long serialVersionUID = -5024744406713321676L;
    private transient HashMap<E, Object> map;
    private static final Object PRESENT = new Object();

    public HashSet() {
        this.map = new HashMap<>();
    }

    public HashSet(Collection<? extends E> collection) {
        this.map = new HashMap<>(Math.max(((int) (collection.size() / 0.75f)) + 1, 16));
        addAll(collection);
    }

    public HashSet(int i2, float f2) {
        this.map = new HashMap<>(i2, f2);
    }

    public HashSet(int i2) {
        this.map = new HashMap<>(i2);
    }

    HashSet(int i2, float f2, boolean z2) {
        this.map = new LinkedHashMap(i2, f2);
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.List
    public Iterator<E> iterator() {
        return this.map.keySet().iterator();
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public int size() {
        return this.map.size();
    }

    @Override // java.util.AbstractCollection, java.util.Collection
    public boolean isEmpty() {
        return this.map.isEmpty();
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public boolean contains(Object obj) {
        return this.map.containsKey(obj);
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
    public boolean add(E e2) {
        return this.map.put(e2, PRESENT) == null;
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public boolean remove(Object obj) {
        return this.map.remove(obj) == PRESENT;
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
    public void clear() {
        this.map.clear();
    }

    public Object clone() {
        try {
            HashSet hashSet = (HashSet) super.clone();
            hashSet.map = (HashMap) this.map.clone();
            return hashSet;
        } catch (CloneNotSupportedException e2) {
            throw new InternalError(e2);
        }
    }

    private void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
        objectOutputStream.defaultWriteObject();
        objectOutputStream.writeInt(this.map.capacity());
        objectOutputStream.writeFloat(this.map.loadFactor());
        objectOutputStream.writeInt(this.map.size());
        Iterator<E> it = this.map.keySet().iterator();
        while (it.hasNext()) {
            objectOutputStream.writeObject(it.next());
        }
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        objectInputStream.readFields();
        int i2 = objectInputStream.readInt();
        if (i2 < 0) {
            throw new InvalidObjectException("Illegal capacity: " + i2);
        }
        float f2 = objectInputStream.readFloat();
        if (f2 <= 0.0f || Float.isNaN(f2)) {
            throw new InvalidObjectException("Illegal load factor: " + f2);
        }
        float fMin = Math.min(Math.max(0.25f, f2), 4.0f);
        int i3 = objectInputStream.readInt();
        if (i3 < 0) {
            throw new InvalidObjectException("Illegal size: " + i3);
        }
        int iMin = (int) Math.min(i3 * Math.min(1.0f / fMin, 4.0f), 1.0737418E9f);
        SharedSecrets.getJavaOISAccess().checkArray(objectInputStream, Map.Entry[].class, HashMap.tableSizeFor(iMin));
        this.map = this instanceof LinkedHashSet ? new LinkedHashMap<>(iMin, fMin) : new HashMap<>(iMin, fMin);
        for (int i4 = 0; i4 < i3; i4++) {
            this.map.put(objectInputStream.readObject(), PRESENT);
        }
    }

    @Override // java.util.Collection, java.lang.Iterable
    public Spliterator<E> spliterator() {
        return new HashMap.KeySpliterator(this.map, 0, -1, 0, 0);
    }
}
