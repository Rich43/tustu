package java.util.stream;

import java.util.AbstractMap;
import java.util.AbstractSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.DoubleSummaryStatistics;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.IntSummaryStatistics;
import java.util.Iterator;
import java.util.List;
import java.util.LongSummaryStatistics;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.StringJoiner;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.function.ToDoubleFunction;
import java.util.function.ToIntFunction;
import java.util.function.ToLongFunction;
import java.util.stream.Collector;

/* loaded from: rt.jar:java/util/stream/Collectors.class */
public final class Collectors {
    static final Set<Collector.Characteristics> CH_CONCURRENT_ID = Collections.unmodifiableSet(EnumSet.of(Collector.Characteristics.CONCURRENT, Collector.Characteristics.UNORDERED, Collector.Characteristics.IDENTITY_FINISH));
    static final Set<Collector.Characteristics> CH_CONCURRENT_NOID = Collections.unmodifiableSet(EnumSet.of(Collector.Characteristics.CONCURRENT, Collector.Characteristics.UNORDERED));
    static final Set<Collector.Characteristics> CH_ID = Collections.unmodifiableSet(EnumSet.of(Collector.Characteristics.IDENTITY_FINISH));
    static final Set<Collector.Characteristics> CH_UNORDERED_ID = Collections.unmodifiableSet(EnumSet.of(Collector.Characteristics.UNORDERED, Collector.Characteristics.IDENTITY_FINISH));
    static final Set<Collector.Characteristics> CH_NOID = Collections.emptySet();

    private Collectors() {
    }

    private static <T> BinaryOperator<T> throwingMerger() {
        return (obj, obj2) -> {
            throw new IllegalStateException(String.format("Duplicate key %s", obj));
        };
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static <I, R> Function<I, R> castingIdentity() {
        return obj -> {
            return obj;
        };
    }

    /* loaded from: rt.jar:java/util/stream/Collectors$CollectorImpl.class */
    static class CollectorImpl<T, A, R> implements Collector<T, A, R> {
        private final Supplier<A> supplier;
        private final BiConsumer<A, T> accumulator;
        private final BinaryOperator<A> combiner;
        private final Function<A, R> finisher;
        private final Set<Collector.Characteristics> characteristics;

        CollectorImpl(Supplier<A> supplier, BiConsumer<A, T> biConsumer, BinaryOperator<A> binaryOperator, Function<A, R> function, Set<Collector.Characteristics> set) {
            this.supplier = supplier;
            this.accumulator = biConsumer;
            this.combiner = binaryOperator;
            this.finisher = function;
            this.characteristics = set;
        }

        CollectorImpl(Supplier<A> supplier, BiConsumer<A, T> biConsumer, BinaryOperator<A> binaryOperator, Set<Collector.Characteristics> set) {
            this(supplier, biConsumer, binaryOperator, Collectors.castingIdentity(), set);
        }

        @Override // java.util.stream.Collector
        public BiConsumer<A, T> accumulator() {
            return this.accumulator;
        }

        @Override // java.util.stream.Collector
        public Supplier<A> supplier() {
            return this.supplier;
        }

        @Override // java.util.stream.Collector
        public BinaryOperator<A> combiner() {
            return this.combiner;
        }

        @Override // java.util.stream.Collector
        public Function<A, R> finisher() {
            return this.finisher;
        }

        @Override // java.util.stream.Collector
        public Set<Collector.Characteristics> characteristics() {
            return this.characteristics;
        }
    }

    public static <T, C extends Collection<T>> Collector<T, ?, C> toCollection(Supplier<C> supplier) {
        return new CollectorImpl(supplier, (v0, v1) -> {
            v0.add(v1);
        }, (collection, collection2) -> {
            collection.addAll(collection2);
            return collection;
        }, CH_ID);
    }

    public static <T> Collector<T, ?, List<T>> toList() {
        return new CollectorImpl(ArrayList::new, (v0, v1) -> {
            v0.add(v1);
        }, (list, list2) -> {
            list.addAll(list2);
            return list;
        }, CH_ID);
    }

    public static <T> Collector<T, ?, Set<T>> toSet() {
        return new CollectorImpl(HashSet::new, (v0, v1) -> {
            v0.add(v1);
        }, (set, set2) -> {
            set.addAll(set2);
            return set;
        }, CH_UNORDERED_ID);
    }

    public static Collector<CharSequence, ?, String> joining() {
        return new CollectorImpl(StringBuilder::new, (v0, v1) -> {
            v0.append(v1);
        }, (sb, sb2) -> {
            sb.append((CharSequence) sb2);
            return sb;
        }, (v0) -> {
            return v0.toString();
        }, CH_NOID);
    }

    public static Collector<CharSequence, ?, String> joining(CharSequence charSequence) {
        return joining(charSequence, "", "");
    }

    public static Collector<CharSequence, ?, String> joining(CharSequence charSequence, CharSequence charSequence2, CharSequence charSequence3) {
        return new CollectorImpl(() -> {
            return new StringJoiner(charSequence, charSequence2, charSequence3);
        }, (v0, v1) -> {
            v0.add(v1);
        }, (v0, v1) -> {
            return v0.merge(v1);
        }, (v0) -> {
            return v0.toString();
        }, CH_NOID);
    }

    private static <K, V, M extends Map<K, V>> BinaryOperator<M> mapMerger(BinaryOperator<V> binaryOperator) {
        return (map, map2) -> {
            for (Map.Entry entry : map2.entrySet()) {
                map.merge(entry.getKey(), entry.getValue(), binaryOperator);
            }
            return map;
        };
    }

    public static <T, U, A, R> Collector<T, ?, R> mapping(Function<? super T, ? extends U> function, Collector<? super U, A, R> collector) {
        BiConsumer<A, ? super U> biConsumerAccumulator = collector.accumulator();
        return new CollectorImpl(collector.supplier(), (obj, obj2) -> {
            biConsumerAccumulator.accept(obj, function.apply(obj2));
        }, collector.combiner(), collector.finisher(), collector.characteristics());
    }

    /* JADX WARN: Multi-variable type inference failed */
    public static <T, A, R, RR> Collector<T, A, RR> collectingAndThen(Collector<T, A, R> collector, Function<R, RR> function) {
        Set<Collector.Characteristics> setCharacteristics = collector.characteristics();
        if (setCharacteristics.contains(Collector.Characteristics.IDENTITY_FINISH)) {
            if (setCharacteristics.size() == 1) {
                setCharacteristics = CH_NOID;
            } else {
                EnumSet enumSetCopyOf = EnumSet.copyOf(setCharacteristics);
                enumSetCopyOf.remove(Collector.Characteristics.IDENTITY_FINISH);
                setCharacteristics = Collections.unmodifiableSet(enumSetCopyOf);
            }
        }
        return new CollectorImpl(collector.supplier(), collector.accumulator(), collector.combiner(), collector.finisher().andThen(function), setCharacteristics);
    }

    public static <T> Collector<T, ?, Long> counting() {
        return reducing(0L, obj -> {
            return 1L;
        }, (v0, v1) -> {
            return Long.sum(v0, v1);
        });
    }

    public static <T> Collector<T, ?, Optional<T>> minBy(Comparator<? super T> comparator) {
        return reducing(BinaryOperator.minBy(comparator));
    }

    public static <T> Collector<T, ?, Optional<T>> maxBy(Comparator<? super T> comparator) {
        return reducing(BinaryOperator.maxBy(comparator));
    }

    public static <T> Collector<T, ?, Integer> summingInt(ToIntFunction<? super T> toIntFunction) {
        return new CollectorImpl(() -> {
            return new int[1];
        }, (iArr, obj) -> {
            iArr[0] = iArr[0] + toIntFunction.applyAsInt(obj);
        }, (iArr2, iArr3) -> {
            iArr2[0] = iArr2[0] + iArr3[0];
            return iArr2;
        }, iArr4 -> {
            return Integer.valueOf(iArr4[0]);
        }, CH_NOID);
    }

    public static <T> Collector<T, ?, Long> summingLong(ToLongFunction<? super T> toLongFunction) {
        return new CollectorImpl(() -> {
            return new long[1];
        }, (jArr, obj) -> {
            jArr[0] = jArr[0] + toLongFunction.applyAsLong(obj);
        }, (jArr2, jArr3) -> {
            jArr2[0] = jArr2[0] + jArr3[0];
            return jArr2;
        }, jArr4 -> {
            return Long.valueOf(jArr4[0]);
        }, CH_NOID);
    }

    public static <T> Collector<T, ?, Double> summingDouble(ToDoubleFunction<? super T> toDoubleFunction) {
        return new CollectorImpl(() -> {
            return new double[3];
        }, (dArr, obj) -> {
            sumWithCompensation(dArr, toDoubleFunction.applyAsDouble(obj));
            dArr[2] = dArr[2] + toDoubleFunction.applyAsDouble(obj);
        }, (dArr2, dArr3) -> {
            sumWithCompensation(dArr2, dArr3[0]);
            dArr2[2] = dArr2[2] + dArr3[2];
            return sumWithCompensation(dArr2, dArr3[1]);
        }, dArr4 -> {
            return Double.valueOf(computeFinalSum(dArr4));
        }, CH_NOID);
    }

    static double[] sumWithCompensation(double[] dArr, double d2) {
        double d3 = d2 - dArr[1];
        double d4 = dArr[0];
        double d5 = d4 + d3;
        dArr[1] = (d5 - d4) - d3;
        dArr[0] = d5;
        return dArr;
    }

    static double computeFinalSum(double[] dArr) {
        double d2 = dArr[0] + dArr[1];
        double d3 = dArr[dArr.length - 1];
        if (Double.isNaN(d2) && Double.isInfinite(d3)) {
            return d3;
        }
        return d2;
    }

    public static <T> Collector<T, ?, Double> averagingInt(ToIntFunction<? super T> toIntFunction) {
        return new CollectorImpl(() -> {
            return new long[2];
        }, (jArr, obj) -> {
            jArr[0] = jArr[0] + toIntFunction.applyAsInt(obj);
            jArr[1] = jArr[1] + 1;
        }, (jArr2, jArr3) -> {
            jArr2[0] = jArr2[0] + jArr3[0];
            jArr2[1] = jArr2[1] + jArr3[1];
            return jArr2;
        }, jArr4 -> {
            return Double.valueOf(jArr4[1] == 0 ? 0.0d : jArr4[0] / jArr4[1]);
        }, CH_NOID);
    }

    public static <T> Collector<T, ?, Double> averagingLong(ToLongFunction<? super T> toLongFunction) {
        return new CollectorImpl(() -> {
            return new long[2];
        }, (jArr, obj) -> {
            jArr[0] = jArr[0] + toLongFunction.applyAsLong(obj);
            jArr[1] = jArr[1] + 1;
        }, (jArr2, jArr3) -> {
            jArr2[0] = jArr2[0] + jArr3[0];
            jArr2[1] = jArr2[1] + jArr3[1];
            return jArr2;
        }, jArr4 -> {
            return Double.valueOf(jArr4[1] == 0 ? 0.0d : jArr4[0] / jArr4[1]);
        }, CH_NOID);
    }

    public static <T> Collector<T, ?, Double> averagingDouble(ToDoubleFunction<? super T> toDoubleFunction) {
        return new CollectorImpl(() -> {
            return new double[4];
        }, (dArr, obj) -> {
            sumWithCompensation(dArr, toDoubleFunction.applyAsDouble(obj));
            dArr[2] = dArr[2] + 1.0d;
            dArr[3] = dArr[3] + toDoubleFunction.applyAsDouble(obj);
        }, (dArr2, dArr3) -> {
            sumWithCompensation(dArr2, dArr3[0]);
            sumWithCompensation(dArr2, dArr3[1]);
            dArr2[2] = dArr2[2] + dArr3[2];
            dArr2[3] = dArr2[3] + dArr3[3];
            return dArr2;
        }, dArr4 -> {
            return Double.valueOf(dArr4[2] == 0.0d ? 0.0d : computeFinalSum(dArr4) / dArr4[2]);
        }, CH_NOID);
    }

    public static <T> Collector<T, ?, T> reducing(T t2, BinaryOperator<T> binaryOperator) {
        return new CollectorImpl(boxSupplier(t2), (objArr, obj) -> {
            objArr[0] = binaryOperator.apply(objArr[0], obj);
        }, (objArr2, objArr3) -> {
            objArr2[0] = binaryOperator.apply(objArr2[0], objArr3[0]);
            return objArr2;
        }, objArr4 -> {
            return objArr4[0];
        }, CH_NOID);
    }

    private static <T> Supplier<T[]> boxSupplier(T t2) {
        return () -> {
            return new Object[]{t2};
        };
    }

    /* JADX INFO: Add missing generic type declarations: [T] */
    /* renamed from: java.util.stream.Collectors$1OptionalBox, reason: invalid class name */
    /* loaded from: rt.jar:java/util/stream/Collectors$1OptionalBox.class */
    class C1OptionalBox<T> implements Consumer<T> {
        T value = null;
        boolean present = false;
        final /* synthetic */ BinaryOperator val$op;

        C1OptionalBox(BinaryOperator binaryOperator) {
            this.val$op = binaryOperator;
        }

        @Override // java.util.function.Consumer
        public void accept(T t2) {
            if (this.present) {
                this.value = this.val$op.apply(this.value, t2);
            } else {
                this.value = t2;
                this.present = true;
            }
        }
    }

    public static <T> Collector<T, ?, Optional<T>> reducing(BinaryOperator<T> binaryOperator) {
        return new CollectorImpl(() -> {
            return new C1OptionalBox(binaryOperator);
        }, (v0, v1) -> {
            v0.accept(v1);
        }, (c1OptionalBox, c1OptionalBox2) -> {
            if (c1OptionalBox2.present) {
                c1OptionalBox.accept(c1OptionalBox2.value);
            }
            return c1OptionalBox;
        }, c1OptionalBox3 -> {
            return Optional.ofNullable(c1OptionalBox3.value);
        }, CH_NOID);
    }

    public static <T, U> Collector<T, ?, U> reducing(U u2, Function<? super T, ? extends U> function, BinaryOperator<U> binaryOperator) {
        return new CollectorImpl(boxSupplier(u2), (objArr, obj) -> {
            objArr[0] = binaryOperator.apply(objArr[0], function.apply(obj));
        }, (objArr2, objArr3) -> {
            objArr2[0] = binaryOperator.apply(objArr2[0], objArr3[0]);
            return objArr2;
        }, objArr4 -> {
            return objArr4[0];
        }, CH_NOID);
    }

    public static <T, K> Collector<T, ?, Map<K, List<T>>> groupingBy(Function<? super T, ? extends K> function) {
        return groupingBy(function, toList());
    }

    public static <T, K, A, D> Collector<T, ?, Map<K, D>> groupingBy(Function<? super T, ? extends K> function, Collector<? super T, A, D> collector) {
        return groupingBy(function, HashMap::new, collector);
    }

    public static <T, K, D, A, M extends Map<K, D>> Collector<T, ?, M> groupingBy(Function<? super T, ? extends K> function, Supplier<M> supplier, Collector<? super T, A, D> collector) {
        Supplier<A> supplier2 = collector.supplier();
        BiConsumer<A, ? super T> biConsumerAccumulator = collector.accumulator();
        BiConsumer biConsumer = (map, obj) -> {
            biConsumerAccumulator.accept(map.computeIfAbsent(Objects.requireNonNull(function.apply(obj), "element cannot be mapped to a null key"), obj -> {
                return supplier2.get();
            }), obj);
        };
        BinaryOperator binaryOperatorMapMerger = mapMerger(collector.combiner());
        if (collector.characteristics().contains(Collector.Characteristics.IDENTITY_FINISH)) {
            return new CollectorImpl(supplier, biConsumer, binaryOperatorMapMerger, CH_ID);
        }
        Function<A, D> functionFinisher = collector.finisher();
        return new CollectorImpl(supplier, biConsumer, binaryOperatorMapMerger, map2 -> {
            map2.replaceAll((obj2, obj3) -> {
                return functionFinisher.apply(obj3);
            });
            return map2;
        }, CH_NOID);
    }

    public static <T, K> Collector<T, ?, ConcurrentMap<K, List<T>>> groupingByConcurrent(Function<? super T, ? extends K> function) {
        return groupingByConcurrent(function, ConcurrentHashMap::new, toList());
    }

    public static <T, K, A, D> Collector<T, ?, ConcurrentMap<K, D>> groupingByConcurrent(Function<? super T, ? extends K> function, Collector<? super T, A, D> collector) {
        return groupingByConcurrent(function, ConcurrentHashMap::new, collector);
    }

    public static <T, K, A, D, M extends ConcurrentMap<K, D>> Collector<T, ?, M> groupingByConcurrent(Function<? super T, ? extends K> function, Supplier<M> supplier, Collector<? super T, A, D> collector) {
        BiConsumer biConsumer;
        Supplier<A> supplier2 = collector.supplier();
        BiConsumer<A, ? super T> biConsumerAccumulator = collector.accumulator();
        BinaryOperator binaryOperatorMapMerger = mapMerger(collector.combiner());
        if (collector.characteristics().contains(Collector.Characteristics.CONCURRENT)) {
            biConsumer = (concurrentMap, obj) -> {
                biConsumerAccumulator.accept(concurrentMap.computeIfAbsent(Objects.requireNonNull(function.apply(obj), "element cannot be mapped to a null key"), obj -> {
                    return supplier2.get();
                }), obj);
            };
        } else {
            biConsumer = (concurrentMap2, obj2) -> {
                Object objComputeIfAbsent = concurrentMap2.computeIfAbsent(Objects.requireNonNull(function.apply(obj2), "element cannot be mapped to a null key"), obj2 -> {
                    return supplier2.get();
                });
                synchronized (objComputeIfAbsent) {
                    biConsumerAccumulator.accept(objComputeIfAbsent, obj2);
                }
            };
        }
        if (collector.characteristics().contains(Collector.Characteristics.IDENTITY_FINISH)) {
            return new CollectorImpl(supplier, biConsumer, binaryOperatorMapMerger, CH_CONCURRENT_ID);
        }
        Function<A, D> functionFinisher = collector.finisher();
        return new CollectorImpl(supplier, biConsumer, binaryOperatorMapMerger, concurrentMap3 -> {
            concurrentMap3.replaceAll((obj3, obj4) -> {
                return functionFinisher.apply(obj4);
            });
            return concurrentMap3;
        }, CH_CONCURRENT_NOID);
    }

    public static <T> Collector<T, ?, Map<Boolean, List<T>>> partitioningBy(Predicate<? super T> predicate) {
        return partitioningBy(predicate, toList());
    }

    public static <T, D, A> Collector<T, ?, Map<Boolean, D>> partitioningBy(Predicate<? super T> predicate, Collector<? super T, A, D> collector) {
        BiConsumer<A, ? super T> biConsumerAccumulator = collector.accumulator();
        BiConsumer biConsumer = (partition, obj) -> {
            biConsumerAccumulator.accept(predicate.test(obj) ? partition.forTrue : partition.forFalse, obj);
        };
        BinaryOperator<A> binaryOperatorCombiner = collector.combiner();
        BinaryOperator binaryOperator = (partition2, partition3) -> {
            return new Partition(binaryOperatorCombiner.apply(partition2.forTrue, partition3.forTrue), binaryOperatorCombiner.apply(partition2.forFalse, partition3.forFalse));
        };
        Supplier supplier = () -> {
            return new Partition(collector.supplier().get(), collector.supplier().get());
        };
        if (collector.characteristics().contains(Collector.Characteristics.IDENTITY_FINISH)) {
            return new CollectorImpl(supplier, biConsumer, binaryOperator, CH_ID);
        }
        return new CollectorImpl(supplier, biConsumer, binaryOperator, partition4 -> {
            return new Partition(collector.finisher().apply(partition4.forTrue), collector.finisher().apply(partition4.forFalse));
        }, CH_NOID);
    }

    public static <T, K, U> Collector<T, ?, Map<K, U>> toMap(Function<? super T, ? extends K> function, Function<? super T, ? extends U> function2) {
        return toMap(function, function2, throwingMerger(), HashMap::new);
    }

    public static <T, K, U> Collector<T, ?, Map<K, U>> toMap(Function<? super T, ? extends K> function, Function<? super T, ? extends U> function2, BinaryOperator<U> binaryOperator) {
        return toMap(function, function2, binaryOperator, HashMap::new);
    }

    public static <T, K, U, M extends Map<K, U>> Collector<T, ?, M> toMap(Function<? super T, ? extends K> function, Function<? super T, ? extends U> function2, BinaryOperator<U> binaryOperator, Supplier<M> supplier) {
        return new CollectorImpl(supplier, (map, obj) -> {
            map.merge(function.apply(obj), function2.apply(obj), binaryOperator);
        }, mapMerger(binaryOperator), CH_ID);
    }

    public static <T, K, U> Collector<T, ?, ConcurrentMap<K, U>> toConcurrentMap(Function<? super T, ? extends K> function, Function<? super T, ? extends U> function2) {
        return toConcurrentMap(function, function2, throwingMerger(), ConcurrentHashMap::new);
    }

    public static <T, K, U> Collector<T, ?, ConcurrentMap<K, U>> toConcurrentMap(Function<? super T, ? extends K> function, Function<? super T, ? extends U> function2, BinaryOperator<U> binaryOperator) {
        return toConcurrentMap(function, function2, binaryOperator, ConcurrentHashMap::new);
    }

    public static <T, K, U, M extends ConcurrentMap<K, U>> Collector<T, ?, M> toConcurrentMap(Function<? super T, ? extends K> function, Function<? super T, ? extends U> function2, BinaryOperator<U> binaryOperator, Supplier<M> supplier) {
        return new CollectorImpl(supplier, (concurrentMap, obj) -> {
            concurrentMap.merge(function.apply(obj), function2.apply(obj), binaryOperator);
        }, mapMerger(binaryOperator), CH_CONCURRENT_ID);
    }

    public static <T> Collector<T, ?, IntSummaryStatistics> summarizingInt(ToIntFunction<? super T> toIntFunction) {
        return new CollectorImpl(IntSummaryStatistics::new, (intSummaryStatistics, obj) -> {
            intSummaryStatistics.accept(toIntFunction.applyAsInt(obj));
        }, (intSummaryStatistics2, intSummaryStatistics3) -> {
            intSummaryStatistics2.combine(intSummaryStatistics3);
            return intSummaryStatistics2;
        }, CH_ID);
    }

    public static <T> Collector<T, ?, LongSummaryStatistics> summarizingLong(ToLongFunction<? super T> toLongFunction) {
        return new CollectorImpl(LongSummaryStatistics::new, (longSummaryStatistics, obj) -> {
            longSummaryStatistics.accept(toLongFunction.applyAsLong(obj));
        }, (longSummaryStatistics2, longSummaryStatistics3) -> {
            longSummaryStatistics2.combine(longSummaryStatistics3);
            return longSummaryStatistics2;
        }, CH_ID);
    }

    public static <T> Collector<T, ?, DoubleSummaryStatistics> summarizingDouble(ToDoubleFunction<? super T> toDoubleFunction) {
        return new CollectorImpl(DoubleSummaryStatistics::new, (doubleSummaryStatistics, obj) -> {
            doubleSummaryStatistics.accept(toDoubleFunction.applyAsDouble(obj));
        }, (doubleSummaryStatistics2, doubleSummaryStatistics3) -> {
            doubleSummaryStatistics2.combine(doubleSummaryStatistics3);
            return doubleSummaryStatistics2;
        }, CH_ID);
    }

    /* loaded from: rt.jar:java/util/stream/Collectors$Partition.class */
    private static final class Partition<T> extends AbstractMap<Boolean, T> implements Map<Boolean, T> {
        final T forTrue;
        final T forFalse;

        Partition(T t2, T t3) {
            this.forTrue = t2;
            this.forFalse = t3;
        }

        @Override // java.util.AbstractMap, java.util.Map
        public Set<Map.Entry<Boolean, T>> entrySet() {
            return new AbstractSet<Map.Entry<Boolean, T>>() { // from class: java.util.stream.Collectors.Partition.1
                @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.List
                public Iterator<Map.Entry<Boolean, T>> iterator() {
                    return Arrays.asList(new AbstractMap.SimpleImmutableEntry(false, Partition.this.forFalse), new AbstractMap.SimpleImmutableEntry(true, Partition.this.forTrue)).iterator();
                }

                @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
                public int size() {
                    return 2;
                }
            };
        }
    }
}
