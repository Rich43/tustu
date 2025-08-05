package com.sun.scenario.effect;

import com.sun.javafx.geom.BaseBounds;
import com.sun.javafx.geom.DirtyRegionContainer;
import com.sun.javafx.geom.DirtyRegionPool;
import com.sun.javafx.geom.Point2D;
import com.sun.javafx.geom.Rectangle;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.javafx.geom.transform.Translate2D;
import com.sun.scenario.effect.Effect;

/* loaded from: jfxrt.jar:com/sun/scenario/effect/Offset.class */
public class Offset extends Effect {
    private int xoff;
    private int yoff;

    public Offset(int xoff, int yoff, Effect input) {
        super(input);
        this.xoff = xoff;
        this.yoff = yoff;
    }

    public final Effect getInput() {
        return getInputs().get(0);
    }

    public void setInput(Effect input) {
        setInput(0, input);
    }

    public int getX() {
        return this.xoff;
    }

    public void setX(int xoff) {
        int i2 = this.xoff;
        this.xoff = xoff;
    }

    public int getY() {
        return this.yoff;
    }

    public void setY(int yoff) {
        float f2 = this.yoff;
        this.yoff = yoff;
    }

    static BaseTransform getOffsetTransform(BaseTransform transform, double xoff, double yoff) {
        if (transform == null || transform.isIdentity()) {
            return Translate2D.getInstance(xoff, yoff);
        }
        return transform.copy().deriveWithTranslation(xoff, yoff);
    }

    @Override // com.sun.scenario.effect.Effect
    public BaseBounds getBounds(BaseTransform transform, Effect defaultInput) {
        BaseTransform at2 = getOffsetTransform(transform, this.xoff, this.yoff);
        Effect input = getDefaultedInput(0, defaultInput);
        return input.getBounds(at2, defaultInput);
    }

    @Override // com.sun.scenario.effect.Effect
    public ImageData filter(FilterContext fctx, BaseTransform transform, Rectangle outputClip, Object renderHelper, Effect defaultInput) {
        BaseTransform at2 = getOffsetTransform(transform, this.xoff, this.yoff);
        Effect input = getDefaultedInput(0, defaultInput);
        return input.filter(fctx, at2, outputClip, renderHelper, defaultInput);
    }

    @Override // com.sun.scenario.effect.Effect
    public Point2D transform(Point2D p2, Effect defaultInput) {
        Point2D p3 = getDefaultedInput(0, defaultInput).transform(p2, defaultInput);
        float x2 = p3.f11907x + this.xoff;
        float y2 = p3.f11908y + this.yoff;
        Point2D p4 = new Point2D(x2, y2);
        return p4;
    }

    @Override // com.sun.scenario.effect.Effect
    public Point2D untransform(Point2D p2, Effect defaultInput) {
        float x2 = p2.f11907x - this.xoff;
        float y2 = p2.f11908y - this.yoff;
        return getDefaultedInput(0, defaultInput).untransform(new Point2D(x2, y2), defaultInput);
    }

    @Override // com.sun.scenario.effect.Effect
    public Effect.AccelType getAccelType(FilterContext fctx) {
        return getInputs().get(0).getAccelType(fctx);
    }

    @Override // com.sun.scenario.effect.Effect
    public boolean reducesOpaquePixels() {
        return (getX() == 0 && getY() == 0 && (getInput() == null || !getInput().reducesOpaquePixels())) ? false : true;
    }

    @Override // com.sun.scenario.effect.Effect
    public DirtyRegionContainer getDirtyRegions(Effect defaultInput, DirtyRegionPool regionPool) {
        Effect di = getDefaultedInput(0, defaultInput);
        DirtyRegionContainer drc = di.getDirtyRegions(defaultInput, regionPool);
        if (this.xoff != 0 || this.yoff != 0) {
            for (int i2 = 0; i2 < drc.size(); i2++) {
                drc.getDirtyRegion(i2).translate(this.xoff, this.yoff, 0.0f);
            }
        }
        return drc;
    }
}
