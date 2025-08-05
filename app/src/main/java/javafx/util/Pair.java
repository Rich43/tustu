package javafx.util;

import java.io.Serializable;
import javafx.beans.NamedArg;

/* loaded from: jfxrt.jar:javafx/util/Pair.class */
public class Pair<K, V> implements Serializable {
    private K key;
    private V value;

    public K getKey() {
        return this.key;
    }

    public V getValue() {
        return this.value;
    }

    public Pair(@NamedArg("key") K key, @NamedArg("value") V value) {
        this.key = key;
        this.value = value;
    }

    public String toString() {
        return ((Object) this.key) + "=" + ((Object) this.value);
    }

    public int hashCode() {
        return (this.key.hashCode() * 13) + (this.value == null ? 0 : this.value.hashCode());
    }

    public boolean equals(Object o2) {
        if (this == o2) {
            return true;
        }
        if (o2 instanceof Pair) {
            Pair pair = (Pair) o2;
            if (this.key != null) {
                if (!this.key.equals(pair.key)) {
                    return false;
                }
            } else if (pair.key != null) {
                return false;
            }
            return this.value != null ? this.value.equals(pair.value) : pair.value == null;
        }
        return false;
    }
}
