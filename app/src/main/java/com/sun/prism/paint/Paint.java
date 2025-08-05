package com.sun.prism.paint;

/* loaded from: jfxrt.jar:com/sun/prism/paint/Paint.class */
public abstract class Paint {
    private final Type type;
    private final boolean proportional;
    private final boolean isMutable;

    public abstract boolean isOpaque();

    /* loaded from: jfxrt.jar:com/sun/prism/paint/Paint$Type.class */
    public enum Type {
        COLOR("Color", false, false),
        LINEAR_GRADIENT("LinearGradient", true, false),
        RADIAL_GRADIENT("RadialGradient", true, false),
        IMAGE_PATTERN("ImagePattern", false, true);

        private String name;
        private boolean isGradient;
        private boolean isImagePattern;

        Type(String name, boolean isGradient, boolean isImagePattern) {
            this.name = name;
            this.isGradient = isGradient;
            this.isImagePattern = isImagePattern;
        }

        public String getName() {
            return this.name;
        }

        public boolean isGradient() {
            return this.isGradient;
        }

        public boolean isImagePattern() {
            return this.isImagePattern;
        }
    }

    Paint(Type type, boolean proportional, boolean isMutable) {
        this.type = type;
        this.proportional = proportional;
        this.isMutable = isMutable;
    }

    public final Type getType() {
        return this.type;
    }

    public boolean isProportional() {
        return this.proportional;
    }

    public boolean isMutable() {
        return this.isMutable;
    }
}
