package java.awt.font;

/* loaded from: rt.jar:java/awt/font/GlyphJustificationInfo.class */
public final class GlyphJustificationInfo {
    public static final int PRIORITY_KASHIDA = 0;
    public static final int PRIORITY_WHITESPACE = 1;
    public static final int PRIORITY_INTERCHAR = 2;
    public static final int PRIORITY_NONE = 3;
    public final float weight;
    public final int growPriority;
    public final boolean growAbsorb;
    public final float growLeftLimit;
    public final float growRightLimit;
    public final int shrinkPriority;
    public final boolean shrinkAbsorb;
    public final float shrinkLeftLimit;
    public final float shrinkRightLimit;

    public GlyphJustificationInfo(float f2, boolean z2, int i2, float f3, float f4, boolean z3, int i3, float f5, float f6) {
        if (f2 < 0.0f) {
            throw new IllegalArgumentException("weight is negative");
        }
        if (!priorityIsValid(i2)) {
            throw new IllegalArgumentException("Invalid grow priority");
        }
        if (f3 < 0.0f) {
            throw new IllegalArgumentException("growLeftLimit is negative");
        }
        if (f4 < 0.0f) {
            throw new IllegalArgumentException("growRightLimit is negative");
        }
        if (!priorityIsValid(i3)) {
            throw new IllegalArgumentException("Invalid shrink priority");
        }
        if (f5 < 0.0f) {
            throw new IllegalArgumentException("shrinkLeftLimit is negative");
        }
        if (f6 < 0.0f) {
            throw new IllegalArgumentException("shrinkRightLimit is negative");
        }
        this.weight = f2;
        this.growAbsorb = z2;
        this.growPriority = i2;
        this.growLeftLimit = f3;
        this.growRightLimit = f4;
        this.shrinkAbsorb = z3;
        this.shrinkPriority = i3;
        this.shrinkLeftLimit = f5;
        this.shrinkRightLimit = f6;
    }

    private static boolean priorityIsValid(int i2) {
        return i2 >= 0 && i2 <= 3;
    }
}
