package com.sun.javafx.effect;

/* loaded from: jfxrt.jar:com/sun/javafx/effect/EffectDirtyBits.class */
public enum EffectDirtyBits {
    EFFECT_DIRTY,
    BOUNDS_CHANGED;

    private int mask = 1 << ordinal();

    EffectDirtyBits() {
    }

    public final int getMask() {
        return this.mask;
    }

    public static boolean isSet(int value, EffectDirtyBits dirtyBit) {
        return (value & dirtyBit.getMask()) != 0;
    }
}
