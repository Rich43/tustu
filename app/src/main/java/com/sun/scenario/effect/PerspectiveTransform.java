package com.sun.scenario.effect;

import com.sun.javafx.geom.BaseBounds;
import com.sun.javafx.geom.DirtyRegionContainer;
import com.sun.javafx.geom.DirtyRegionPool;
import com.sun.javafx.geom.Point2D;
import com.sun.javafx.geom.RectBounds;
import com.sun.javafx.geom.Rectangle;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.scenario.effect.Effect;
import com.sun.scenario.effect.impl.state.PerspectiveTransformState;
import com.sun.scenario.effect.impl.state.RenderState;

/* loaded from: jfxrt.jar:com/sun/scenario/effect/PerspectiveTransform.class */
public class PerspectiveTransform extends CoreEffect<RenderState> {
    private float[][] tx;
    private float ulx;
    private float uly;
    private float urx;
    private float ury;
    private float lrx;
    private float lry;
    private float llx;
    private float lly;
    private float[] devcoords;
    private final PerspectiveTransformState state;

    @Override // com.sun.scenario.effect.CoreEffect, com.sun.scenario.effect.Effect
    public /* bridge */ /* synthetic */ Effect.AccelType getAccelType(FilterContext filterContext) {
        return super.getAccelType(filterContext);
    }

    @Override // com.sun.scenario.effect.CoreEffect, com.sun.scenario.effect.FilterEffect
    public /* bridge */ /* synthetic */ ImageData filterImageDatas(FilterContext filterContext, BaseTransform baseTransform, Rectangle rectangle, RenderState renderState, ImageData[] imageDataArr) {
        return super.filterImageDatas(filterContext, baseTransform, rectangle, renderState, imageDataArr);
    }

    public PerspectiveTransform() {
        this(DefaultInput);
    }

    public PerspectiveTransform(Effect input) {
        super(input);
        this.tx = new float[3][3];
        this.devcoords = new float[8];
        this.state = new PerspectiveTransformState();
        setQuadMapping(0.0f, 0.0f, 100.0f, 0.0f, 100.0f, 100.0f, 0.0f, 100.0f);
        updatePeerKey("PerspectiveTransform");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.sun.scenario.effect.Effect
    public Object getState() {
        return this.state;
    }

    public final Effect getInput() {
        return getInputs().get(0);
    }

    public void setInput(Effect input) {
        setInput(0, input);
    }

    private void setUnitQuadMapping(float ulx, float uly, float urx, float ury, float lrx, float lry, float llx, float lly) {
        float dx3 = ((ulx - urx) + lrx) - llx;
        float dy3 = ((uly - ury) + lry) - lly;
        this.tx[2][2] = 1.0f;
        if (dx3 == 0.0f && dy3 == 0.0f) {
            this.tx[0][0] = urx - ulx;
            this.tx[0][1] = lrx - urx;
            this.tx[0][2] = ulx;
            this.tx[1][0] = ury - uly;
            this.tx[1][1] = lry - ury;
            this.tx[1][2] = uly;
            this.tx[2][0] = 0.0f;
            this.tx[2][1] = 0.0f;
        } else {
            float dx1 = urx - lrx;
            float dy1 = ury - lry;
            float dx2 = llx - lrx;
            float dy2 = lly - lry;
            float invdet = 1.0f / ((dx1 * dy2) - (dx2 * dy1));
            this.tx[2][0] = ((dx3 * dy2) - (dx2 * dy3)) * invdet;
            this.tx[2][1] = ((dx1 * dy3) - (dx3 * dy1)) * invdet;
            this.tx[0][0] = (urx - ulx) + (this.tx[2][0] * urx);
            this.tx[0][1] = (llx - ulx) + (this.tx[2][1] * llx);
            this.tx[0][2] = ulx;
            this.tx[1][0] = (ury - uly) + (this.tx[2][0] * ury);
            this.tx[1][1] = (lly - uly) + (this.tx[2][1] * lly);
            this.tx[1][2] = uly;
        }
        this.state.updateTx(this.tx);
    }

    public final void setQuadMapping(float ulx, float uly, float urx, float ury, float lrx, float lry, float llx, float lly) {
        this.ulx = ulx;
        this.uly = uly;
        this.urx = urx;
        this.ury = ury;
        this.lrx = lrx;
        this.lry = lry;
        this.llx = llx;
        this.lly = lly;
    }

    @Override // com.sun.scenario.effect.FilterEffect, com.sun.scenario.effect.Effect
    public RectBounds getBounds(BaseTransform transform, Effect defaultInput) {
        setupDevCoords(transform);
        float f2 = this.devcoords[0];
        float maxx = f2;
        float minx = f2;
        float f3 = this.devcoords[1];
        float maxy = f3;
        float miny = f3;
        for (int i2 = 2; i2 < this.devcoords.length; i2 += 2) {
            if (minx > this.devcoords[i2]) {
                minx = this.devcoords[i2];
            } else if (maxx < this.devcoords[i2]) {
                maxx = this.devcoords[i2];
            }
            if (miny > this.devcoords[i2 + 1]) {
                miny = this.devcoords[i2 + 1];
            } else if (maxy < this.devcoords[i2 + 1]) {
                maxy = this.devcoords[i2 + 1];
            }
        }
        return new RectBounds(minx, miny, maxx, maxy);
    }

    private void setupDevCoords(BaseTransform transform) {
        this.devcoords[0] = this.ulx;
        this.devcoords[1] = this.uly;
        this.devcoords[2] = this.urx;
        this.devcoords[3] = this.ury;
        this.devcoords[4] = this.lrx;
        this.devcoords[5] = this.lry;
        this.devcoords[6] = this.llx;
        this.devcoords[7] = this.lly;
        transform.transform(this.devcoords, 0, this.devcoords, 0, 4);
    }

    @Override // com.sun.scenario.effect.FilterEffect, com.sun.scenario.effect.Effect
    public ImageData filter(FilterContext fctx, BaseTransform transform, Rectangle outputClip, Object renderHelper, Effect defaultInput) {
        setupTransforms(transform);
        RenderState rstate = getRenderState(fctx, transform, outputClip, renderHelper, defaultInput);
        Effect input = getDefaultedInput(0, defaultInput);
        Rectangle inputClip = rstate.getInputClip(0, outputClip);
        ImageData inputData = input.filter(fctx, BaseTransform.IDENTITY_TRANSFORM, inputClip, null, defaultInput);
        if (!inputData.validate(fctx)) {
            inputData.unref();
            return new ImageData(fctx, (Filterable) null, inputData.getUntransformedBounds());
        }
        ImageData ret = filterImageDatas(fctx, transform, outputClip, rstate, new ImageData[]{inputData});
        inputData.unref();
        return ret;
    }

    @Override // com.sun.scenario.effect.Effect
    public Rectangle getResultBounds(BaseTransform transform, Rectangle outputClip, ImageData... inputDatas) {
        Rectangle ob = new Rectangle(getBounds(transform, (Effect) null));
        ob.intersectWith(outputClip);
        return ob;
    }

    @Override // com.sun.scenario.effect.FilterEffect, com.sun.scenario.effect.Effect
    public Point2D transform(Point2D p2, Effect defaultInput) {
        setupTransforms(BaseTransform.IDENTITY_TRANSFORM);
        Effect input = getDefaultedInput(0, defaultInput);
        Point2D p3 = input.transform(p2, defaultInput);
        BaseBounds b2 = input.getBounds(BaseTransform.IDENTITY_TRANSFORM, defaultInput);
        float sx = (p3.f11907x - b2.getMinX()) / b2.getWidth();
        float sy = (p3.f11908y - b2.getMinY()) / b2.getHeight();
        float dx = (this.tx[0][0] * sx) + (this.tx[0][1] * sy) + this.tx[0][2];
        float dy = (this.tx[1][0] * sx) + (this.tx[1][1] * sy) + this.tx[1][2];
        float dw = (this.tx[2][0] * sx) + (this.tx[2][1] * sy) + this.tx[2][2];
        Point2D p4 = new Point2D(dx / dw, dy / dw);
        return p4;
    }

    @Override // com.sun.scenario.effect.FilterEffect, com.sun.scenario.effect.Effect
    public Point2D untransform(Point2D p2, Effect defaultInput) {
        setupTransforms(BaseTransform.IDENTITY_TRANSFORM);
        Effect input = getDefaultedInput(0, defaultInput);
        float dx = p2.f11907x;
        float dy = p2.f11908y;
        float[][] itx = this.state.getITX();
        float sx = (itx[0][0] * dx) + (itx[0][1] * dy) + itx[0][2];
        float sy = (itx[1][0] * dx) + (itx[1][1] * dy) + itx[1][2];
        float sw = (itx[2][0] * dx) + (itx[2][1] * dy) + itx[2][2];
        BaseBounds b2 = input.getBounds(BaseTransform.IDENTITY_TRANSFORM, defaultInput);
        return getDefaultedInput(0, defaultInput).untransform(new Point2D(b2.getMinX() + ((sx / sw) * b2.getWidth()), b2.getMinY() + ((sy / sw) * b2.getHeight())), defaultInput);
    }

    private void setupTransforms(BaseTransform transform) {
        setupDevCoords(transform);
        setUnitQuadMapping(this.devcoords[0], this.devcoords[1], this.devcoords[2], this.devcoords[3], this.devcoords[4], this.devcoords[5], this.devcoords[6], this.devcoords[7]);
    }

    @Override // com.sun.scenario.effect.FilterEffect
    public RenderState getRenderState(FilterContext fctx, BaseTransform transform, Rectangle outputClip, Object renderHelper, Effect defaultInput) {
        return RenderState.UnclippedUserSpaceRenderState;
    }

    @Override // com.sun.scenario.effect.Effect
    public boolean reducesOpaquePixels() {
        return true;
    }

    @Override // com.sun.scenario.effect.Effect
    public DirtyRegionContainer getDirtyRegions(Effect defaultInput, DirtyRegionPool regionPool) {
        DirtyRegionContainer drc = regionPool.checkOut();
        drc.deriveWithNewRegion(getBounds(BaseTransform.IDENTITY_TRANSFORM, defaultInput));
        return drc;
    }
}
