package com.sun.scenario.effect.impl;

import com.sun.javafx.geom.Rectangle;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.javafx.geom.transform.NoninvertibleTransformException;
import com.sun.scenario.effect.Effect;
import com.sun.scenario.effect.FilterContext;
import com.sun.scenario.effect.ImageData;
import com.sun.scenario.effect.impl.state.RenderState;

/* loaded from: jfxrt.jar:com/sun/scenario/effect/impl/EffectPeer.class */
public abstract class EffectPeer<T extends RenderState> {
    private final FilterContext fctx;
    private final Renderer renderer;
    private final String uniqueName;
    private Effect effect;
    private T renderState;
    private int pass;
    private Rectangle destBounds;
    private final Rectangle[] inputBounds = new Rectangle[2];
    private final BaseTransform[] inputTransforms = new BaseTransform[2];
    private final Rectangle[] inputNativeBounds = new Rectangle[2];
    private final Rectangle destNativeBounds = new Rectangle();

    public abstract ImageData filter(Effect effect, T t2, BaseTransform baseTransform, Rectangle rectangle, ImageData... imageDataArr);

    protected EffectPeer(FilterContext fctx, Renderer renderer, String uniqueName) {
        if (fctx == null) {
            throw new IllegalArgumentException("FilterContext must be non-null");
        }
        this.fctx = fctx;
        this.renderer = renderer;
        this.uniqueName = uniqueName;
    }

    public boolean isImageDataCompatible(ImageData id) {
        return getRenderer().isImageDataCompatible(id);
    }

    public void dispose() {
    }

    public Effect.AccelType getAccelType() {
        return this.renderer.getAccelType();
    }

    protected final FilterContext getFilterContext() {
        return this.fctx;
    }

    protected Renderer getRenderer() {
        return this.renderer;
    }

    public String getUniqueName() {
        return this.uniqueName;
    }

    protected Effect getEffect() {
        return this.effect;
    }

    protected void setEffect(Effect effect) {
        this.effect = effect;
    }

    protected T getRenderState() {
        return this.renderState;
    }

    protected void setRenderState(T renderState) {
        this.renderState = renderState;
    }

    public final int getPass() {
        return this.pass;
    }

    public void setPass(int pass) {
        this.pass = pass;
    }

    protected final Rectangle getInputBounds(int inputIndex) {
        return this.inputBounds[inputIndex];
    }

    protected final void setInputBounds(int inputIndex, Rectangle r2) {
        this.inputBounds[inputIndex] = r2;
    }

    protected final BaseTransform getInputTransform(int inputIndex) {
        return this.inputTransforms[inputIndex];
    }

    protected final void setInputTransform(int inputIndex, BaseTransform tx) {
        this.inputTransforms[inputIndex] = tx;
    }

    protected final Rectangle getInputNativeBounds(int inputIndex) {
        return this.inputNativeBounds[inputIndex];
    }

    protected final void setInputNativeBounds(int inputIndex, Rectangle r2) {
        this.inputNativeBounds[inputIndex] = r2;
    }

    public Rectangle getResultBounds(BaseTransform transform, Rectangle outputClip, ImageData... inputDatas) {
        return getEffect().getResultBounds(transform, outputClip, inputDatas);
    }

    protected float[] getSourceRegion(int inputIndex) {
        return getSourceRegion(getInputBounds(inputIndex), getInputNativeBounds(inputIndex), getDestBounds());
    }

    static float[] getSourceRegion(Rectangle srcBounds, Rectangle srcNativeBounds, Rectangle dstBounds) {
        float x1 = dstBounds.f11913x - srcBounds.f11913x;
        float y1 = dstBounds.f11914y - srcBounds.f11914y;
        float x2 = x1 + dstBounds.width;
        float y2 = y1 + dstBounds.height;
        float sw = srcNativeBounds.width;
        float sh = srcNativeBounds.height;
        return new float[]{x1 / sw, y1 / sh, x2 / sw, y2 / sh};
    }

    public int getTextureCoordinates(int inputIndex, float[] coords, float srcX, float srcY, float srcNativeWidth, float srcNativeHeight, Rectangle dstBounds, BaseTransform transform) {
        return getTextureCoordinates(coords, srcX, srcY, srcNativeWidth, srcNativeHeight, dstBounds, transform);
    }

    public static int getTextureCoordinates(float[] coords, float srcX, float srcY, float srcNativeWidth, float srcNativeHeight, Rectangle dstBounds, BaseTransform transform) {
        int numCoords;
        coords[0] = dstBounds.f11913x;
        coords[1] = dstBounds.f11914y;
        coords[2] = coords[0] + dstBounds.width;
        coords[3] = coords[1] + dstBounds.height;
        if (transform.isTranslateOrIdentity()) {
            srcX += (float) transform.getMxt();
            srcY += (float) transform.getMyt();
            numCoords = 4;
        } else {
            coords[4] = coords[2];
            coords[5] = coords[1];
            coords[6] = coords[0];
            coords[7] = coords[3];
            numCoords = 8;
            try {
                transform.inverseTransform(coords, 0, coords, 0, 4);
            } catch (NoninvertibleTransformException e2) {
                coords[4] = 0.0f;
                coords[2] = 0.0f;
                coords[1] = 0.0f;
                coords[0] = 0.0f;
                return 4;
            }
        }
        for (int i2 = 0; i2 < numCoords; i2 += 2) {
            coords[i2] = (coords[i2] - srcX) / srcNativeWidth;
            coords[i2 + 1] = (coords[i2 + 1] - srcY) / srcNativeHeight;
        }
        return numCoords;
    }

    protected final void setDestBounds(Rectangle r2) {
        this.destBounds = r2;
    }

    protected final Rectangle getDestBounds() {
        return this.destBounds;
    }

    protected final Rectangle getDestNativeBounds() {
        return this.destNativeBounds;
    }

    protected final void setDestNativeBounds(int w2, int h2) {
        this.destNativeBounds.width = w2;
        this.destNativeBounds.height = h2;
    }

    protected Object getSamplerData(int i2) {
        return null;
    }

    protected boolean isOriginUpperLeft() {
        return getAccelType() != Effect.AccelType.OPENGL;
    }
}
