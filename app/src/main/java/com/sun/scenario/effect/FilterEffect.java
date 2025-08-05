package com.sun.scenario.effect;

import com.sun.javafx.geom.BaseBounds;
import com.sun.javafx.geom.Point2D;
import com.sun.javafx.geom.RectBounds;
import com.sun.javafx.geom.Rectangle;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.javafx.geom.transform.NoninvertibleTransformException;
import com.sun.scenario.effect.impl.state.RenderState;

/* loaded from: jfxrt.jar:com/sun/scenario/effect/FilterEffect.class */
public abstract class FilterEffect<T extends RenderState> extends Effect {
    public abstract T getRenderState(FilterContext filterContext, BaseTransform baseTransform, Rectangle rectangle, Object obj, Effect effect);

    protected abstract ImageData filterImageDatas(FilterContext filterContext, BaseTransform baseTransform, Rectangle rectangle, T t2, ImageData... imageDataArr);

    protected FilterEffect() {
    }

    protected FilterEffect(Effect input) {
        super(input);
    }

    protected FilterEffect(Effect input1, Effect input2) {
        super(input1, input2);
    }

    @Override // com.sun.scenario.effect.Effect
    public BaseBounds getBounds(BaseTransform transform, Effect defaultInput) {
        BaseBounds ret;
        int numinputs = getNumInputs();
        RenderState rstate = getRenderState(null, transform, null, null, defaultInput);
        BaseTransform inputtx = rstate.getInputTransform(transform);
        if (numinputs == 1) {
            Effect input = getDefaultedInput(0, defaultInput);
            ret = input.getBounds(inputtx, defaultInput);
        } else {
            BaseBounds[] inputBounds = new BaseBounds[numinputs];
            for (int i2 = 0; i2 < numinputs; i2++) {
                Effect input2 = getDefaultedInput(i2, defaultInput);
                inputBounds[i2] = input2.getBounds(inputtx, defaultInput);
            }
            ret = combineBounds(inputBounds);
        }
        return transformBounds(rstate.getResultTransform(transform), ret);
    }

    protected static Rectangle untransformClip(BaseTransform transform, Rectangle clip) {
        if (transform.isIdentity() || clip == null || clip.isEmpty()) {
            return clip;
        }
        Rectangle transformedBounds = new Rectangle();
        if (transform.isTranslateOrIdentity()) {
            transformedBounds.setBounds(clip);
            double tx = -transform.getMxt();
            double ty = -transform.getMyt();
            int itx = (int) Math.floor(tx);
            int ity = (int) Math.floor(ty);
            transformedBounds.translate(itx, ity);
            if (itx != tx) {
                transformedBounds.width++;
            }
            if (ity != ty) {
                transformedBounds.height++;
            }
            return transformedBounds;
        }
        RectBounds b2 = new RectBounds(clip);
        try {
            b2.grow(-0.5f, -0.5f);
            RectBounds b3 = (RectBounds) transform.inverseTransform(b2, b2);
            b3.grow(0.5f, 0.5f);
            transformedBounds.setBounds(b3);
        } catch (NoninvertibleTransformException e2) {
        }
        return transformedBounds;
    }

    @Override // com.sun.scenario.effect.Effect
    public ImageData filter(FilterContext fctx, BaseTransform transform, Rectangle outputClip, Object renderHelper, Effect defaultInput) {
        Rectangle filterClip;
        RenderState renderState = getRenderState(fctx, transform, outputClip, renderHelper, defaultInput);
        int numinputs = getNumInputs();
        ImageData[] inputDatas = new ImageData[numinputs];
        BaseTransform inputtx = renderState.getInputTransform(transform);
        BaseTransform resulttx = renderState.getResultTransform(transform);
        if (resulttx.isIdentity()) {
            filterClip = outputClip;
        } else {
            filterClip = untransformClip(resulttx, outputClip);
        }
        for (int i2 = 0; i2 < numinputs; i2++) {
            Effect input = getDefaultedInput(i2, defaultInput);
            inputDatas[i2] = input.filter(fctx, inputtx, renderState.getInputClip(i2, filterClip), null, defaultInput);
            if (!inputDatas[i2].validate(fctx)) {
                for (int j2 = 0; j2 <= i2; j2++) {
                    inputDatas[j2].unref();
                }
                return new ImageData(fctx, (Filterable) null, (Rectangle) null);
            }
        }
        ImageData ret = filterImageDatas(fctx, inputtx, filterClip, renderState, inputDatas);
        for (int i3 = 0; i3 < numinputs; i3++) {
            inputDatas[i3].unref();
        }
        if (!resulttx.isIdentity()) {
            if (renderHelper instanceof ImageDataRenderer) {
                ImageDataRenderer renderer = (ImageDataRenderer) renderHelper;
                renderer.renderImage(ret, resulttx, fctx);
                ret.unref();
                ret = null;
            } else {
                ret = ret.transform(resulttx);
            }
        }
        return ret;
    }

    @Override // com.sun.scenario.effect.Effect
    public Point2D transform(Point2D p2, Effect defaultInput) {
        return getDefaultedInput(0, defaultInput).transform(p2, defaultInput);
    }

    @Override // com.sun.scenario.effect.Effect
    public Point2D untransform(Point2D p2, Effect defaultInput) {
        return getDefaultedInput(0, defaultInput).untransform(p2, defaultInput);
    }
}
