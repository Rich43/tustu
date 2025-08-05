package com.sun.scenario.effect;

import com.sun.javafx.geom.BaseBounds;
import com.sun.javafx.geom.DirtyRegionContainer;
import com.sun.javafx.geom.DirtyRegionPool;
import com.sun.javafx.geom.Point2D;
import com.sun.javafx.geom.RectBounds;
import com.sun.javafx.geom.Rectangle;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.scenario.effect.impl.Renderer;
import com.sun.scenario.effect.impl.state.AccessHelper;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/* loaded from: jfxrt.jar:com/sun/scenario/effect/Effect.class */
public abstract class Effect {
    public static final Effect DefaultInput = null;
    private final List<Effect> inputs;
    private final List<Effect> unmodifiableInputs;
    private final int maxInputs;

    public abstract ImageData filter(FilterContext filterContext, BaseTransform baseTransform, Rectangle rectangle, Object obj, Effect effect);

    public abstract BaseBounds getBounds(BaseTransform baseTransform, Effect effect);

    public abstract boolean reducesOpaquePixels();

    public abstract AccelType getAccelType(FilterContext filterContext);

    static {
        AccessHelper.setStateAccessor(effect -> {
            return effect.getState();
        });
    }

    protected Effect() {
        this.inputs = Collections.emptyList();
        this.unmodifiableInputs = this.inputs;
        this.maxInputs = 0;
    }

    protected Effect(Effect input) {
        this.inputs = new ArrayList(1);
        this.unmodifiableInputs = Collections.unmodifiableList(this.inputs);
        this.maxInputs = 1;
        setInput(0, input);
    }

    protected Effect(Effect input1, Effect input2) {
        this.inputs = new ArrayList(2);
        this.unmodifiableInputs = Collections.unmodifiableList(this.inputs);
        this.maxInputs = 2;
        setInput(0, input1);
        setInput(1, input2);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Object getState() {
        return null;
    }

    public int getNumInputs() {
        return this.inputs.size();
    }

    public final List<Effect> getInputs() {
        return this.unmodifiableInputs;
    }

    protected void setInput(int index, Effect input) {
        if (index < 0 || index >= this.maxInputs) {
            throw new IllegalArgumentException("Index must be within allowable range");
        }
        if (index < this.inputs.size()) {
            this.inputs.set(index, input);
        } else {
            this.inputs.add(input);
        }
    }

    public static BaseBounds combineBounds(BaseBounds... inputBounds) {
        BaseBounds ret = null;
        if (inputBounds.length == 1) {
            ret = inputBounds[0];
        } else {
            for (BaseBounds r2 : inputBounds) {
                if (r2 != null && !r2.isEmpty()) {
                    if (ret == null) {
                        BaseBounds ret2 = new RectBounds();
                        ret = ret2.deriveWithNewBounds(r2);
                    } else {
                        ret = ret.deriveWithUnion(r2);
                    }
                }
            }
        }
        if (ret == null) {
            ret = new RectBounds();
        }
        return ret;
    }

    public static Rectangle combineBounds(Rectangle... inputBounds) {
        Rectangle ret = null;
        if (inputBounds.length == 1) {
            ret = inputBounds[0];
        } else {
            for (Rectangle r2 : inputBounds) {
                if (r2 != null && !r2.isEmpty()) {
                    if (ret == null) {
                        ret = new Rectangle(r2);
                    } else {
                        ret.add(r2);
                    }
                }
            }
        }
        if (ret == null) {
            ret = new Rectangle();
        }
        return ret;
    }

    public Rectangle getResultBounds(BaseTransform transform, Rectangle outputClip, ImageData... inputDatas) {
        int numinputs = inputDatas.length;
        Rectangle[] inputBounds = new Rectangle[numinputs];
        for (int i2 = 0; i2 < numinputs; i2++) {
            inputBounds[i2] = inputDatas[i2].getTransformedBounds(outputClip);
        }
        Rectangle rb = combineBounds(inputBounds);
        return rb;
    }

    public static BaseBounds transformBounds(BaseTransform tx, BaseBounds r2) {
        if (tx == null || tx.isIdentity()) {
            return r2;
        }
        BaseBounds ret = new RectBounds();
        return tx.transform(r2, ret);
    }

    protected ImageData ensureTransform(FilterContext fctx, ImageData original, BaseTransform transform, Rectangle clip) {
        if (transform == null || transform.isIdentity()) {
            return original;
        }
        if (!original.validate(fctx)) {
            original.unref();
            return new ImageData(fctx, (Filterable) null, new Rectangle());
        }
        return original.transform(transform);
    }

    public DirtyRegionContainer getDirtyRegions(Effect defaultInput, DirtyRegionPool regionPool) {
        DirtyRegionContainer merge = null;
        for (int i2 = 0; i2 < this.inputs.size(); i2++) {
            DirtyRegionContainer drc = getDefaultedInput(i2, defaultInput).getDirtyRegions(defaultInput, regionPool);
            if (merge == null) {
                merge = drc;
            } else {
                merge.merge(drc);
                regionPool.checkIn(drc);
            }
        }
        if (merge == null) {
            merge = regionPool.checkOut();
        }
        return merge;
    }

    Effect getDefaultedInput(int inputIndex, Effect defaultInput) {
        return getDefaultedInput(this.inputs.get(inputIndex), defaultInput);
    }

    static Effect getDefaultedInput(Effect listedInput, Effect defaultInput) {
        return listedInput == null ? defaultInput : listedInput;
    }

    public Point2D transform(Point2D p2, Effect defaultInput) {
        return p2;
    }

    public Point2D untransform(Point2D p2, Effect defaultInput) {
        return p2;
    }

    public static Filterable createCompatibleImage(FilterContext fctx, int w2, int h2) {
        return Renderer.getRenderer(fctx).createCompatibleImage(w2, h2);
    }

    public static Filterable getCompatibleImage(FilterContext fctx, int w2, int h2) {
        return Renderer.getRenderer(fctx).getCompatibleImage(w2, h2);
    }

    public static void releaseCompatibleImage(FilterContext fctx, Filterable image) {
        Renderer.getRenderer(fctx).releaseCompatibleImage(image);
    }

    /* loaded from: jfxrt.jar:com/sun/scenario/effect/Effect$AccelType.class */
    public enum AccelType {
        INTRINSIC("Intrinsic"),
        NONE("CPU/Java"),
        SIMD("CPU/SIMD"),
        FIXED("CPU/Fixed"),
        OPENGL("OpenGL"),
        DIRECT3D("Direct3D");

        private String text;

        AccelType(String text) {
            this.text = text;
        }

        @Override // java.lang.Enum
        public String toString() {
            return this.text;
        }
    }
}
