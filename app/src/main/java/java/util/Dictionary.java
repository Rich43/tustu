package java.util;

/* loaded from: rt.jar:java/util/Dictionary.class */
public abstract class Dictionary<K, V> {
    public abstract int size();

    public abstract boolean isEmpty();

    public abstract Enumeration<K> keys();

    public abstract Enumeration<V> elements();

    public abstract V get(Object obj);

    public abstract V put(K k2, V v2);

    public abstract V remove(Object obj);
}
