package java.util;

import java.io.Serializable;
import java.lang.invoke.SerializedLambda;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

/* loaded from: rt.jar:java/util/Map.class */
public interface Map<K, V> {
    int size();

    boolean isEmpty();

    boolean containsKey(Object obj);

    boolean containsValue(Object obj);

    V get(Object obj);

    V put(K k2, V v2);

    V remove(Object obj);

    void putAll(Map<? extends K, ? extends V> map);

    void clear();

    Set<K> keySet();

    Collection<V> values();

    Set<Entry<K, V>> entrySet();

    boolean equals(Object obj);

    int hashCode();

    /* loaded from: rt.jar:java/util/Map$Entry.class */
    public interface Entry<K, V> {
        K getKey();

        V getValue();

        V setValue(V v2);

        boolean equals(Object obj);

        int hashCode();

        private static /* synthetic */ Object $deserializeLambda$(SerializedLambda serializedLambda) {
            switch (serializedLambda.getImplMethodName()) {
                case "lambda$comparingByValue$827a17d5$1":
                    if (serializedLambda.getImplMethodKind() == 6 && serializedLambda.getFunctionalInterfaceClass().equals("java/util/Comparator") && serializedLambda.getFunctionalInterfaceMethodName().equals("compare") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;Ljava/lang/Object;)I") && serializedLambda.getImplClass().equals("java/util/Map$Entry") && serializedLambda.getImplMethodSignature().equals("(Ljava/util/Comparator;Ljava/util/Map$Entry;Ljava/util/Map$Entry;)I")) {
                        Comparator comparator = (Comparator) serializedLambda.getCapturedArg(0);
                        return (entry, entry2) -> {
                            return comparator.compare(entry.getValue(), entry2.getValue());
                        };
                    }
                    break;
                case "lambda$comparingByKey$bbdbfea9$1":
                    if (serializedLambda.getImplMethodKind() == 6 && serializedLambda.getFunctionalInterfaceClass().equals("java/util/Comparator") && serializedLambda.getFunctionalInterfaceMethodName().equals("compare") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;Ljava/lang/Object;)I") && serializedLambda.getImplClass().equals("java/util/Map$Entry") && serializedLambda.getImplMethodSignature().equals("(Ljava/util/Map$Entry;Ljava/util/Map$Entry;)I")) {
                        return (entry3, entry4) -> {
                            return ((Comparable) entry3.getKey()).compareTo(entry4.getKey());
                        };
                    }
                    break;
                case "lambda$comparingByValue$1065357e$1":
                    if (serializedLambda.getImplMethodKind() == 6 && serializedLambda.getFunctionalInterfaceClass().equals("java/util/Comparator") && serializedLambda.getFunctionalInterfaceMethodName().equals("compare") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;Ljava/lang/Object;)I") && serializedLambda.getImplClass().equals("java/util/Map$Entry") && serializedLambda.getImplMethodSignature().equals("(Ljava/util/Map$Entry;Ljava/util/Map$Entry;)I")) {
                        return (entry5, entry6) -> {
                            return ((Comparable) entry5.getValue()).compareTo(entry6.getValue());
                        };
                    }
                    break;
                case "lambda$comparingByKey$6d558cbf$1":
                    if (serializedLambda.getImplMethodKind() == 6 && serializedLambda.getFunctionalInterfaceClass().equals("java/util/Comparator") && serializedLambda.getFunctionalInterfaceMethodName().equals("compare") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;Ljava/lang/Object;)I") && serializedLambda.getImplClass().equals("java/util/Map$Entry") && serializedLambda.getImplMethodSignature().equals("(Ljava/util/Comparator;Ljava/util/Map$Entry;Ljava/util/Map$Entry;)I")) {
                        Comparator comparator2 = (Comparator) serializedLambda.getCapturedArg(0);
                        return (entry7, entry8) -> {
                            return comparator2.compare(entry7.getKey(), entry8.getKey());
                        };
                    }
                    break;
            }
            throw new IllegalArgumentException("Invalid lambda deserialization");
        }

        static <K extends Comparable<? super K>, V> Comparator<Entry<K, V>> comparingByKey() {
            return (Comparator) ((Serializable) (entry3, entry4) -> {
                return ((Comparable) entry3.getKey()).compareTo(entry4.getKey());
            });
        }

        static <K, V extends Comparable<? super V>> Comparator<Entry<K, V>> comparingByValue() {
            return (Comparator) ((Serializable) (entry5, entry6) -> {
                return ((Comparable) entry5.getValue()).compareTo(entry6.getValue());
            });
        }

        static <K, V> Comparator<Entry<K, V>> comparingByKey(Comparator<? super K> comparator) {
            Objects.requireNonNull(comparator);
            return (Comparator) ((Serializable) (entry7, entry8) -> {
                return comparator.compare(entry7.getKey(), entry8.getKey());
            });
        }

        static <K, V> Comparator<Entry<K, V>> comparingByValue(Comparator<? super V> comparator) {
            Objects.requireNonNull(comparator);
            return (Comparator) ((Serializable) (entry, entry2) -> {
                return comparator.compare(entry.getValue(), entry2.getValue());
            });
        }
    }

    default V getOrDefault(Object obj, V v2) {
        V v3 = get(obj);
        return (v3 != null || containsKey(obj)) ? v3 : v2;
    }

    default void forEach(BiConsumer<? super K, ? super V> biConsumer) {
        Objects.requireNonNull(biConsumer);
        for (Entry<K, V> entry : entrySet()) {
            try {
                biConsumer.accept(entry.getKey(), entry.getValue());
            } catch (IllegalStateException e2) {
                throw new ConcurrentModificationException(e2);
            }
        }
    }

    default void replaceAll(BiFunction<? super K, ? super V, ? extends V> biFunction) {
        Objects.requireNonNull(biFunction);
        for (Entry<K, V> entry : entrySet()) {
            try {
                try {
                    entry.setValue(biFunction.apply(entry.getKey(), entry.getValue()));
                } catch (IllegalStateException e2) {
                    throw new ConcurrentModificationException(e2);
                }
            } catch (IllegalStateException e3) {
                throw new ConcurrentModificationException(e3);
            }
        }
    }

    default V putIfAbsent(K k2, V v2) {
        V vPut = get(k2);
        if (vPut == null) {
            vPut = put(k2, v2);
        }
        return vPut;
    }

    default boolean remove(Object obj, Object obj2) {
        V v2 = get(obj);
        if (Objects.equals(v2, obj2)) {
            if (v2 == null && !containsKey(obj)) {
                return false;
            }
            remove(obj);
            return true;
        }
        return false;
    }

    default boolean replace(K k2, V v2, V v3) {
        V v4 = get(k2);
        if (Objects.equals(v4, v2)) {
            if (v4 == null && !containsKey(k2)) {
                return false;
            }
            put(k2, v3);
            return true;
        }
        return false;
    }

    default V replace(K k2, V v2) {
        V v3 = get(k2);
        V vPut = v3;
        if (v3 != null || containsKey(k2)) {
            vPut = put(k2, v2);
        }
        return vPut;
    }

    default V computeIfAbsent(K k2, Function<? super K, ? extends V> function) {
        V vApply;
        Objects.requireNonNull(function);
        V v2 = get(k2);
        if (v2 == null && (vApply = function.apply(k2)) != null) {
            put(k2, vApply);
            return vApply;
        }
        return v2;
    }

    default V computeIfPresent(K k2, BiFunction<? super K, ? super V, ? extends V> biFunction) {
        Objects.requireNonNull(biFunction);
        V v2 = get(k2);
        if (v2 != null) {
            V vApply = biFunction.apply(k2, v2);
            if (vApply != null) {
                put(k2, vApply);
                return vApply;
            }
            remove(k2);
            return null;
        }
        return null;
    }

    default V compute(K k2, BiFunction<? super K, ? super V, ? extends V> biFunction) {
        Objects.requireNonNull(biFunction);
        V v2 = get(k2);
        V vApply = biFunction.apply(k2, v2);
        if (vApply == null) {
            if (v2 != null || containsKey(k2)) {
                remove(k2);
                return null;
            }
            return null;
        }
        put(k2, vApply);
        return vApply;
    }

    default V merge(K k2, V v2, BiFunction<? super V, ? super V, ? extends V> biFunction) {
        Objects.requireNonNull(biFunction);
        Objects.requireNonNull(v2);
        V v3 = get(k2);
        V vApply = v3 == null ? v2 : biFunction.apply(v3, v2);
        if (vApply == null) {
            remove(k2);
        } else {
            put(k2, vApply);
        }
        return vApply;
    }
}
