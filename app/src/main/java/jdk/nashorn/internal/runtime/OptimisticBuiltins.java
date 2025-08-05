package jdk.nashorn.internal.runtime;

import jdk.nashorn.internal.objects.annotations.SpecializedFunction;

/* loaded from: nashorn.jar:jdk/nashorn/internal/runtime/OptimisticBuiltins.class */
public interface OptimisticBuiltins {
    SpecializedFunction.LinkLogic getLinkLogic(Class<? extends SpecializedFunction.LinkLogic> cls);

    boolean hasPerInstanceAssumptions();
}
