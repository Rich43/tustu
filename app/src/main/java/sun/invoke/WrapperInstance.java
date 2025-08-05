package sun.invoke;

import java.lang.invoke.MethodHandle;

/* loaded from: rt.jar:sun/invoke/WrapperInstance.class */
public interface WrapperInstance {
    MethodHandle getWrapperInstanceTarget();

    Class<?> getWrapperInstanceType();
}
