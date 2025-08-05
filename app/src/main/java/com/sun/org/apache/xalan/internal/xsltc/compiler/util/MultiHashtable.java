package com.sun.org.apache.xalan.internal.xsltc.compiler.util;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/* loaded from: rt.jar:com/sun/org/apache/xalan/internal/xsltc/compiler/util/MultiHashtable.class */
public final class MultiHashtable<K, V> {
    static final long serialVersionUID = -6151608290510033572L;
    private final Map<K, Set<V>> map = new HashMap();
    private boolean modifiable = true;

    public Set<V> put(K key, V value) {
        if (this.modifiable) {
            Set<V> set = this.map.get(key);
            if (set == null) {
                set = new HashSet();
                this.map.put(key, set);
            }
            set.add(value);
            return set;
        }
        throw new UnsupportedOperationException("The MultiHashtable instance is not modifiable.");
    }

    public V maps(K key, V value) {
        Set<V> set;
        if (key != null && (set = this.map.get(key)) != null) {
            for (V v2 : set) {
                if (v2.equals(value)) {
                    return v2;
                }
            }
            return null;
        }
        return null;
    }

    public void makeUnmodifiable() {
        this.modifiable = false;
    }
}
