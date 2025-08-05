package org.apache.commons.math3.util;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/util/Pair.class */
public class Pair<K, V> {
    private final K key;
    private final V value;

    public Pair(K k2, V v2) {
        this.key = k2;
        this.value = v2;
    }

    public Pair(Pair<? extends K, ? extends V> entry) {
        this(entry.getKey(), entry.getValue());
    }

    public K getKey() {
        return this.key;
    }

    public V getValue() {
        return this.value;
    }

    public K getFirst() {
        return this.key;
    }

    public V getSecond() {
        return this.value;
    }

    public boolean equals(Object o2) {
        if (this == o2) {
            return true;
        }
        if (!(o2 instanceof Pair)) {
            return false;
        }
        Pair<?, ?> oP = (Pair) o2;
        if (this.key != null ? this.key.equals(oP.key) : oP.key == null) {
            if (this.value != null ? this.value.equals(oP.value) : oP.value == null) {
                return true;
            }
        }
        return false;
    }

    public int hashCode() {
        int result = this.key == null ? 0 : this.key.hashCode();
        int h2 = this.value == null ? 0 : this.value.hashCode();
        return ((37 * result) + h2) ^ (h2 >>> 16);
    }

    public String toString() {
        return "[" + ((Object) getKey()) + ", " + ((Object) getValue()) + "]";
    }

    public static <K, V> Pair<K, V> create(K k2, V v2) {
        return new Pair<>(k2, v2);
    }
}
