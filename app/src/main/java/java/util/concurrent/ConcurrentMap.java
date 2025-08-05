package java.util.concurrent;

import java.util.Map;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

/* loaded from: rt.jar:java/util/concurrent/ConcurrentMap.class */
public interface ConcurrentMap<K, V> extends Map<K, V> {
    @Override // java.util.Map
    V putIfAbsent(K k2, V v2);

    @Override // java.util.Map
    boolean remove(Object obj, Object obj2);

    @Override // java.util.Map
    boolean replace(K k2, V v2, V v3);

    @Override // java.util.Map
    V replace(K k2, V v2);

    @Override // java.util.Map
    default V getOrDefault(Object obj, V v2) {
        V v3 = get(obj);
        return v3 != null ? v3 : v2;
    }

    @Override // java.util.Map
    default void forEach(BiConsumer<? super K, ? super V> biConsumer) {
        Objects.requireNonNull(biConsumer);
        for (Map.Entry<K, V> entry : entrySet()) {
            try {
                biConsumer.accept(entry.getKey(), entry.getValue());
            } catch (IllegalStateException e2) {
            }
        }
    }

    @Override // java.util.Map
    default void replaceAll(BiFunction<? super K, ? super V, ? extends V> biFunction) {
        Objects.requireNonNull(biFunction);
        forEach((obj, obj2) -> {
            while (!replace(obj, obj2, biFunction.apply(obj, obj2))) {
                Object obj = get(obj);
                obj2 = obj;
                if (obj == null) {
                    return;
                }
            }
        });
    }

    @Override // java.util.Map
    default V computeIfAbsent(K k2, Function<? super K, ? extends V> function) {
        V vApply;
        Objects.requireNonNull(function);
        V v2 = get(k2);
        V v3 = v2;
        if (v2 == null && (vApply = function.apply(k2)) != null) {
            V vPutIfAbsent = putIfAbsent(k2, vApply);
            v3 = vPutIfAbsent;
            if (vPutIfAbsent == null) {
                return vApply;
            }
        }
        return v3;
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v3, types: [V, java.lang.Object] */
    @Override // java.util.Map
    default V computeIfPresent(K k2, BiFunction<? super K, ? super V, ? extends V> biFunction) {
        Objects.requireNonNull(biFunction);
        while (true) {
            ?? r0 = (Object) get(k2);
            if (r0 != 0) {
                V vApply = biFunction.apply(k2, r0);
                if (vApply != null) {
                    if (replace(k2, r0, vApply)) {
                        return vApply;
                    }
                } else if (remove(k2, r0)) {
                    return null;
                }
            } else {
                return r0;
            }
        }
    }

    @Override // java.util.Map
    default V compute(K k2, BiFunction<? super K, ? super V, ? extends V> biFunction) {
        Objects.requireNonNull(biFunction);
        V v2 = get(k2);
        while (true) {
            V vApply = biFunction.apply(k2, (Object) v2);
            if (vApply == null) {
                if ((v2 == null && !containsKey(k2)) || remove(k2, v2)) {
                    return null;
                }
                v2 = get(k2);
            } else if (v2 != null) {
                if (replace(k2, v2, vApply)) {
                    return vApply;
                }
                v2 = get(k2);
            } else {
                V vPutIfAbsent = putIfAbsent(k2, vApply);
                v2 = vPutIfAbsent;
                if (vPutIfAbsent == null) {
                    return vApply;
                }
            }
        }
    }

    @Override // java.util.Map
    default V merge(K k2, V v2, BiFunction<? super V, ? super V, ? extends V> biFunction) {
        Objects.requireNonNull(biFunction);
        Objects.requireNonNull(v2);
        V v3 = get(k2);
        while (true) {
            if (v3 != null) {
                V vApply = biFunction.apply((Object) v3, v2);
                if (vApply != null) {
                    if (replace(k2, v3, vApply)) {
                        return vApply;
                    }
                } else if (remove(k2, v3)) {
                    return null;
                }
                v3 = get(k2);
            } else {
                V vPutIfAbsent = putIfAbsent(k2, v2);
                v3 = vPutIfAbsent;
                if (vPutIfAbsent == null) {
                    return v2;
                }
            }
        }
    }
}
