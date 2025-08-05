package jdk.internal.dynalink.linker;

import java.lang.invoke.MethodHandle;

/* loaded from: nashorn.jar:jdk/internal/dynalink/linker/MethodHandleTransformer.class */
public interface MethodHandleTransformer {
    MethodHandle transform(MethodHandle methodHandle);
}
