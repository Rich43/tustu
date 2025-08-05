package java.util;

import java.io.Serializable;
import java.lang.invoke.SerializedLambda;
import java.util.Comparators;
import java.util.function.Function;
import java.util.function.ToDoubleFunction;
import java.util.function.ToIntFunction;
import java.util.function.ToLongFunction;

@FunctionalInterface
/* loaded from: rt.jar:java/util/Comparator.class */
public interface Comparator<T> {
    int compare(T t2, T t3);

    boolean equals(Object obj);

    private static /* synthetic */ Object $deserializeLambda$(SerializedLambda serializedLambda) {
        switch (serializedLambda.getImplMethodName()) {
            case "lambda$comparingLong$6043328a$1":
                if (serializedLambda.getImplMethodKind() == 6 && serializedLambda.getFunctionalInterfaceClass().equals("java/util/Comparator") && serializedLambda.getFunctionalInterfaceMethodName().equals("compare") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;Ljava/lang/Object;)I") && serializedLambda.getImplClass().equals("java/util/Comparator") && serializedLambda.getImplMethodSignature().equals("(Ljava/util/function/ToLongFunction;Ljava/lang/Object;Ljava/lang/Object;)I")) {
                    ToLongFunction toLongFunction = (ToLongFunction) serializedLambda.getCapturedArg(0);
                    return (obj, obj2) -> {
                        return Long.compare(toLongFunction.applyAsLong(obj), toLongFunction.applyAsLong(obj2));
                    };
                }
                break;
            case "lambda$comparing$ea9a8b3a$1":
                if (serializedLambda.getImplMethodKind() == 6 && serializedLambda.getFunctionalInterfaceClass().equals("java/util/Comparator") && serializedLambda.getFunctionalInterfaceMethodName().equals("compare") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;Ljava/lang/Object;)I") && serializedLambda.getImplClass().equals("java/util/Comparator") && serializedLambda.getImplMethodSignature().equals("(Ljava/util/Comparator;Ljava/util/function/Function;Ljava/lang/Object;Ljava/lang/Object;)I")) {
                    Comparator comparator = (Comparator) serializedLambda.getCapturedArg(0);
                    Function function = (Function) serializedLambda.getCapturedArg(1);
                    return (obj3, obj4) -> {
                        return comparator.compare(function.apply(obj3), function.apply(obj4));
                    };
                }
                break;
            case "lambda$comparing$77a9974f$1":
                if (serializedLambda.getImplMethodKind() == 6 && serializedLambda.getFunctionalInterfaceClass().equals("java/util/Comparator") && serializedLambda.getFunctionalInterfaceMethodName().equals("compare") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;Ljava/lang/Object;)I") && serializedLambda.getImplClass().equals("java/util/Comparator") && serializedLambda.getImplMethodSignature().equals("(Ljava/util/function/Function;Ljava/lang/Object;Ljava/lang/Object;)I")) {
                    Function function2 = (Function) serializedLambda.getCapturedArg(0);
                    return (obj5, obj6) -> {
                        return ((Comparable) function2.apply(obj5)).compareTo(function2.apply(obj6));
                    };
                }
                break;
            case "lambda$comparingDouble$8dcf42ea$1":
                if (serializedLambda.getImplMethodKind() == 6 && serializedLambda.getFunctionalInterfaceClass().equals("java/util/Comparator") && serializedLambda.getFunctionalInterfaceMethodName().equals("compare") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;Ljava/lang/Object;)I") && serializedLambda.getImplClass().equals("java/util/Comparator") && serializedLambda.getImplMethodSignature().equals("(Ljava/util/function/ToDoubleFunction;Ljava/lang/Object;Ljava/lang/Object;)I")) {
                    ToDoubleFunction toDoubleFunction = (ToDoubleFunction) serializedLambda.getCapturedArg(0);
                    return (obj7, obj8) -> {
                        return Double.compare(toDoubleFunction.applyAsDouble(obj7), toDoubleFunction.applyAsDouble(obj8));
                    };
                }
                break;
            case "lambda$thenComparing$36697e65$1":
                if (serializedLambda.getImplMethodKind() == 7 && serializedLambda.getFunctionalInterfaceClass().equals("java/util/Comparator") && serializedLambda.getFunctionalInterfaceMethodName().equals("compare") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;Ljava/lang/Object;)I") && serializedLambda.getImplClass().equals("java/util/Comparator") && serializedLambda.getImplMethodSignature().equals("(Ljava/util/Comparator;Ljava/lang/Object;Ljava/lang/Object;)I")) {
                    Comparator comparator2 = (Comparator) serializedLambda.getCapturedArg(0);
                    Comparator comparator3 = (Comparator) serializedLambda.getCapturedArg(1);
                    return (obj9, obj10) -> {
                        int iCompare = compare(obj9, obj10);
                        return iCompare != 0 ? iCompare : comparator3.compare(obj9, obj10);
                    };
                }
                break;
            case "lambda$comparingInt$7b0bb60$1":
                if (serializedLambda.getImplMethodKind() == 6 && serializedLambda.getFunctionalInterfaceClass().equals("java/util/Comparator") && serializedLambda.getFunctionalInterfaceMethodName().equals("compare") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;Ljava/lang/Object;)I") && serializedLambda.getImplClass().equals("java/util/Comparator") && serializedLambda.getImplMethodSignature().equals("(Ljava/util/function/ToIntFunction;Ljava/lang/Object;Ljava/lang/Object;)I")) {
                    ToIntFunction toIntFunction = (ToIntFunction) serializedLambda.getCapturedArg(0);
                    return (obj11, obj12) -> {
                        return Integer.compare(toIntFunction.applyAsInt(obj11), toIntFunction.applyAsInt(obj12));
                    };
                }
                break;
        }
        throw new IllegalArgumentException("Invalid lambda deserialization");
    }

    default Comparator<T> reversed() {
        return Collections.reverseOrder(this);
    }

    default Comparator<T> thenComparing(Comparator<? super T> comparator) {
        Objects.requireNonNull(comparator);
        return (Comparator) ((Serializable) (obj9, obj10) -> {
            int iCompare = compare(obj9, obj10);
            return iCompare != 0 ? iCompare : comparator.compare(obj9, obj10);
        });
    }

    default <U> Comparator<T> thenComparing(Function<? super T, ? extends U> function, Comparator<? super U> comparator) {
        return thenComparing(comparing(function, comparator));
    }

    default <U extends Comparable<? super U>> Comparator<T> thenComparing(Function<? super T, ? extends U> function) {
        return thenComparing(comparing(function));
    }

    default Comparator<T> thenComparingInt(ToIntFunction<? super T> toIntFunction) {
        return thenComparing(comparingInt(toIntFunction));
    }

    default Comparator<T> thenComparingLong(ToLongFunction<? super T> toLongFunction) {
        return thenComparing(comparingLong(toLongFunction));
    }

    default Comparator<T> thenComparingDouble(ToDoubleFunction<? super T> toDoubleFunction) {
        return thenComparing(comparingDouble(toDoubleFunction));
    }

    static <T extends Comparable<? super T>> Comparator<T> reverseOrder() {
        return Collections.reverseOrder();
    }

    static <T extends Comparable<? super T>> Comparator<T> naturalOrder() {
        return Comparators.NaturalOrderComparator.INSTANCE;
    }

    static <T> Comparator<T> nullsFirst(Comparator<? super T> comparator) {
        return new Comparators.NullComparator(true, comparator);
    }

    static <T> Comparator<T> nullsLast(Comparator<? super T> comparator) {
        return new Comparators.NullComparator(false, comparator);
    }

    static <T, U> Comparator<T> comparing(Function<? super T, ? extends U> function, Comparator<? super U> comparator) {
        Objects.requireNonNull(function);
        Objects.requireNonNull(comparator);
        return (Comparator) ((Serializable) (obj3, obj4) -> {
            return comparator.compare(function.apply(obj3), function.apply(obj4));
        });
    }

    static <T, U extends Comparable<? super U>> Comparator<T> comparing(Function<? super T, ? extends U> function) {
        Objects.requireNonNull(function);
        return (Comparator) ((Serializable) (obj5, obj6) -> {
            return ((Comparable) function.apply(obj5)).compareTo(function.apply(obj6));
        });
    }

    static <T> Comparator<T> comparingInt(ToIntFunction<? super T> toIntFunction) {
        Objects.requireNonNull(toIntFunction);
        return (Comparator) ((Serializable) (obj11, obj12) -> {
            return Integer.compare(toIntFunction.applyAsInt(obj11), toIntFunction.applyAsInt(obj12));
        });
    }

    static <T> Comparator<T> comparingLong(ToLongFunction<? super T> toLongFunction) {
        Objects.requireNonNull(toLongFunction);
        return (Comparator) ((Serializable) (obj, obj2) -> {
            return Long.compare(toLongFunction.applyAsLong(obj), toLongFunction.applyAsLong(obj2));
        });
    }

    static <T> Comparator<T> comparingDouble(ToDoubleFunction<? super T> toDoubleFunction) {
        Objects.requireNonNull(toDoubleFunction);
        return (Comparator) ((Serializable) (obj7, obj8) -> {
            return Double.compare(toDoubleFunction.applyAsDouble(obj7), toDoubleFunction.applyAsDouble(obj8));
        });
    }
}
