package org.apache.commons.math3.util;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.apache.commons.math3.exception.MathIllegalArgumentException;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/util/TransformerMap.class */
public class TransformerMap implements NumberTransformer, Serializable {
    private static final long serialVersionUID = 4605318041528645258L;
    private NumberTransformer defaultTransformer;
    private Map<Class<?>, NumberTransformer> map;

    public TransformerMap() {
        this.defaultTransformer = null;
        this.map = null;
        this.map = new HashMap();
        this.defaultTransformer = new DefaultTransformer();
    }

    public boolean containsClass(Class<?> key) {
        return this.map.containsKey(key);
    }

    public boolean containsTransformer(NumberTransformer value) {
        return this.map.containsValue(value);
    }

    public NumberTransformer getTransformer(Class<?> key) {
        return this.map.get(key);
    }

    public NumberTransformer putTransformer(Class<?> key, NumberTransformer transformer) {
        return this.map.put(key, transformer);
    }

    public NumberTransformer removeTransformer(Class<?> key) {
        return this.map.remove(key);
    }

    public void clear() {
        this.map.clear();
    }

    public Set<Class<?>> classes() {
        return this.map.keySet();
    }

    public Collection<NumberTransformer> transformers() {
        return this.map.values();
    }

    @Override // org.apache.commons.math3.util.NumberTransformer
    public double transform(Object o2) throws MathIllegalArgumentException {
        double value = Double.NaN;
        if ((o2 instanceof Number) || (o2 instanceof String)) {
            value = this.defaultTransformer.transform(o2);
        } else {
            NumberTransformer trans = getTransformer(o2.getClass());
            if (trans != null) {
                value = trans.transform(o2);
            }
        }
        return value;
    }

    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (other instanceof TransformerMap) {
            TransformerMap rhs = (TransformerMap) other;
            if (!this.defaultTransformer.equals(rhs.defaultTransformer) || this.map.size() != rhs.map.size()) {
                return false;
            }
            for (Map.Entry<Class<?>, NumberTransformer> entry : this.map.entrySet()) {
                if (!entry.getValue().equals(rhs.map.get(entry.getKey()))) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    public int hashCode() {
        int hash = this.defaultTransformer.hashCode();
        for (NumberTransformer t2 : this.map.values()) {
            hash = (hash * 31) + t2.hashCode();
        }
        return hash;
    }
}
