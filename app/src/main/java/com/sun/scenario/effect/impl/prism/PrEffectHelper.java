package com.sun.scenario.effect.impl.prism;

import com.sun.glass.ui.Screen;
import com.sun.javafx.geom.PickRay;
import com.sun.javafx.geom.Point2D;
import com.sun.javafx.geom.Rectangle;
import com.sun.javafx.geom.Vec3d;
import com.sun.javafx.geom.transform.Affine2D;
import com.sun.javafx.geom.transform.Affine3D;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.javafx.geom.transform.NoninvertibleTransformException;
import com.sun.javafx.sg.prism.NGCamera;
import com.sun.javafx.sg.prism.NGPerspectiveCamera;
import com.sun.prism.Graphics;
import com.sun.prism.RenderTarget;
import com.sun.prism.ResourceFactory;
import com.sun.prism.Texture;
import com.sun.scenario.effect.Effect;
import com.sun.scenario.effect.FilterContext;
import com.sun.scenario.effect.ImageData;
import com.sun.scenario.effect.impl.EffectPeer;
import com.sun.scenario.effect.impl.ImagePool;

/* loaded from: jfxrt.jar:com/sun/scenario/effect/impl/prism/PrEffectHelper.class */
public class PrEffectHelper {
    public static void render(Effect effect, Graphics g2, float x2, float y2, Effect defaultInput) {
        BaseTransform transform;
        BaseTransform rendertx;
        FilterContext fctx;
        PrRenderInfo prinfo;
        boolean valid;
        Rectangle rclip = getGraphicsClipNoClone(g2);
        BaseTransform origtx = g2.getTransformNoClone().copy();
        if (origtx.is2D()) {
            if (x2 != 0.0f || y2 != 0.0f || !origtx.isIdentity()) {
                transform = new Affine2D(origtx);
                ((Affine2D) transform).translate(x2, y2);
            } else {
                transform = BaseTransform.IDENTITY_TRANSFORM;
            }
            g2.setTransform(null);
            rendertx = null;
        } else {
            double scalex = Math.hypot(origtx.getMxx(), origtx.getMyx());
            double scaley = Math.hypot(origtx.getMxy(), origtx.getMyy());
            double scale = Math.max(scalex, scaley);
            if (scale <= 1.0d) {
                transform = BaseTransform.IDENTITY_TRANSFORM;
                rendertx = origtx;
            } else {
                transform = BaseTransform.getScaleInstance(scale, scale);
                rendertx = new Affine3D(origtx);
                double scale2 = 1.0d / scale;
                ((Affine3D) rendertx).scale(scale2, scale2);
            }
            NGCamera cam = g2.getCameraNoClone();
            try {
                BaseTransform inv = rendertx.createInverse();
                PickRay ray = new PickRay();
                Vec3d tmpvec = new Vec3d();
                float x1 = rclip.f11913x + 0.5f;
                float y1 = rclip.f11914y + 0.5f;
                float x22 = (rclip.f11913x + rclip.width) - 0.5f;
                float y22 = (rclip.f11914y + rclip.height) - 0.5f;
                double rtw = g2.getRenderTarget().getContentWidth();
                double rth = g2.getRenderTarget().getContentHeight();
                Point2D cul = project(x1, y1, rtw, rth, cam, inv, ray, tmpvec, null);
                Point2D cur = project(x22, y1, rtw, rth, cam, inv, ray, tmpvec, null);
                Point2D cll = project(x1, y22, rtw, rth, cam, inv, ray, tmpvec, null);
                Point2D clr = project(x22, y22, rtw, rth, cam, inv, ray, tmpvec, null);
                rclip = clipbounds(cul, cur, cll, clr);
            } catch (NoninvertibleTransformException e2) {
                return;
            }
        }
        Screen screen = g2.getAssociatedScreen();
        if (screen == null) {
            ResourceFactory factory = g2.getResourceFactory();
            fctx = PrFilterContext.getPrinterContext(factory);
        } else {
            fctx = PrFilterContext.getInstance(screen);
        }
        if (rendertx != null) {
            prinfo = null;
        } else if (g2.isDepthBuffer() && g2.isDepthTest()) {
            prinfo = null;
        } else {
            prinfo = new PrRenderInfo(g2);
        }
        ImagePool.numEffects++;
        do {
            ImageData res = effect.filter(fctx, transform, rclip, prinfo, defaultInput);
            if (res == null) {
                return;
            }
            valid = res.validate(fctx);
            if (valid) {
                Rectangle r2 = res.getUntransformedBounds();
                Texture tex = ((PrTexture) res.getUntransformedImage()).getTextureObject();
                g2.setTransform(rendertx);
                g2.transform(res.getTransform());
                g2.drawTexture(tex, r2.f11913x, r2.f11914y, r2.width, r2.height);
            }
            res.unref();
        } while (!valid);
        g2.setTransform(origtx);
    }

    static Point2D project(float x2, float y2, double vw, double vh, NGCamera cam, BaseTransform inv, PickRay tmpray, Vec3d tmpvec, Point2D ret) {
        double xscale = cam.getViewWidth() / vw;
        double yscale = cam.getViewHeight() / vh;
        PickRay tmpray2 = cam.computePickRay((float) (x2 * xscale), (float) (y2 * yscale), tmpray);
        unscale(tmpray2.getOriginNoClone(), xscale, yscale);
        unscale(tmpray2.getDirectionNoClone(), xscale, yscale);
        return tmpray2.projectToZeroPlane(inv, cam instanceof NGPerspectiveCamera, tmpvec, ret);
    }

    private static void unscale(Vec3d v2, double sx, double sy) {
        v2.f11930x /= sx;
        v2.f11931y /= sy;
    }

    static Rectangle clipbounds(Point2D cul, Point2D cur, Point2D cll, Point2D clr) {
        double x1;
        double x2;
        double y1;
        double y2;
        double x12;
        double x22;
        double y12;
        double y22;
        if (cul != null && cur != null && cll != null && clr != null) {
            if (cul.f11907x < cur.f11907x) {
                x1 = cul.f11907x;
                x2 = cur.f11907x;
            } else {
                x1 = cur.f11907x;
                x2 = cul.f11907x;
            }
            if (cul.f11908y < cur.f11908y) {
                y1 = cul.f11908y;
                y2 = cur.f11908y;
            } else {
                y1 = cur.f11908y;
                y2 = cul.f11908y;
            }
            if (cll.f11907x < clr.f11907x) {
                x12 = Math.min(x1, cll.f11907x);
                x22 = Math.max(x2, clr.f11907x);
            } else {
                x12 = Math.min(x1, clr.f11907x);
                x22 = Math.max(x2, cll.f11907x);
            }
            if (cll.f11908y < clr.f11908y) {
                y12 = Math.min(y1, cll.f11908y);
                y22 = Math.max(y2, clr.f11908y);
            } else {
                y12 = Math.min(y1, clr.f11908y);
                y22 = Math.max(y2, cll.f11908y);
            }
            double x13 = Math.floor(x12 - 0.5d);
            double y13 = Math.floor(y12 - 0.5d);
            double x23 = Math.ceil(x22 + 0.5d) - x13;
            double y23 = Math.ceil(y22 + 0.5d) - y13;
            int x3 = (int) x13;
            int y3 = (int) y13;
            int w2 = (int) x23;
            int h2 = (int) y23;
            if (x3 == x13 && y3 == y13 && w2 == x23 && h2 == y23) {
                return new Rectangle(x3, y3, w2, h2);
            }
            return null;
        }
        return null;
    }

    public static Rectangle getGraphicsClipNoClone(Graphics g2) {
        Rectangle rclip = g2.getClipRectNoClone();
        if (rclip == null) {
            RenderTarget rt = g2.getRenderTarget();
            rclip = new Rectangle(rt.getContentWidth(), rt.getContentHeight());
        }
        return rclip;
    }

    public static void renderImageData(Graphics gdst, ImageData srcData, Rectangle dstBounds) {
        int w2 = dstBounds.width;
        int h2 = dstBounds.height;
        PrDrawable src = (PrDrawable) srcData.getUntransformedImage();
        BaseTransform srcTx = srcData.getTransform();
        Rectangle srcBounds = srcData.getUntransformedBounds();
        float dx2 = 0.0f + w2;
        float dy2 = 0.0f + h2;
        if (srcTx.isTranslateOrIdentity()) {
            float tx = (float) srcTx.getMxt();
            float ty = (float) srcTx.getMyt();
            float sx1 = dstBounds.f11913x - (srcBounds.f11913x + tx);
            float sy1 = dstBounds.f11914y - (srcBounds.f11914y + ty);
            float sx2 = sx1 + w2;
            float sy2 = sy1 + h2;
            gdst.drawTexture(src.getTextureObject(), 0.0f, 0.0f, dx2, dy2, sx1, sy1, sx2, sy2);
            return;
        }
        float[] srcRect = new float[8];
        int srcCoords = EffectPeer.getTextureCoordinates(srcRect, srcBounds.f11913x, srcBounds.f11914y, src.getPhysicalWidth(), src.getPhysicalHeight(), dstBounds, srcTx);
        if (srcCoords < 8) {
            gdst.drawTextureRaw(src.getTextureObject(), 0.0f, 0.0f, dx2, dy2, srcRect[0], srcRect[1], srcRect[2], srcRect[3]);
        } else {
            gdst.drawMappedTextureRaw(src.getTextureObject(), 0.0f, 0.0f, dx2, dy2, srcRect[0], srcRect[1], srcRect[4], srcRect[5], srcRect[6], srcRect[7], srcRect[2], srcRect[3]);
        }
    }
}
