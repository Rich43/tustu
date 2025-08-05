package jdk.nashorn.internal.ir;

import jdk.nashorn.internal.codegen.types.Type;

/* loaded from: nashorn.jar:jdk/nashorn/internal/ir/Optimistic.class */
public interface Optimistic {
    int getProgramPoint();

    Optimistic setProgramPoint(int i2);

    boolean canBeOptimistic();

    Type getMostOptimisticType();

    Type getMostPessimisticType();

    Optimistic setType(Type type);
}
