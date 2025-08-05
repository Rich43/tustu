package com.sun.scenario.effect.impl.state;

import com.sun.scenario.effect.Effect;

/* loaded from: jfxrt.jar:com/sun/scenario/effect/impl/state/AccessHelper.class */
public class AccessHelper {
    private static StateAccessor theStateAccessor;

    /* loaded from: jfxrt.jar:com/sun/scenario/effect/impl/state/AccessHelper$StateAccessor.class */
    public interface StateAccessor {
        Object getState(Effect effect);
    }

    public static void setStateAccessor(StateAccessor accessor) {
        if (theStateAccessor != null) {
            throw new InternalError("EffectAccessor already initialized");
        }
        theStateAccessor = accessor;
    }

    public static Object getState(Effect effect) {
        if (effect == null) {
            return null;
        }
        return theStateAccessor.getState(effect);
    }
}
