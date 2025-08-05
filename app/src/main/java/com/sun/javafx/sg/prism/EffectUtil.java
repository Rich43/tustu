package com.sun.javafx.sg.prism;

import com.sun.prism.Graphics;
import com.sun.prism.Image;
import com.sun.prism.Texture;
import com.sun.prism.paint.Color;
import com.sun.scenario.effect.Color4f;
import com.sun.scenario.effect.DropShadow;
import com.sun.scenario.effect.Effect;
import com.sun.scenario.effect.InnerShadow;

/* loaded from: jfxrt.jar:com/sun/javafx/sg/prism/EffectUtil.class */
class EffectUtil {
    private static final int TEX_SIZE = 256;
    private static Texture itex;
    private static Texture dtex;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !EffectUtil.class.desiredAssertionStatus();
    }

    static boolean renderEffectForRectangularNode(NGNode node, Graphics g2, Effect effect, float alpha, boolean aa2, float rx, float ry, float rw, float rh) {
        if (!g2.getTransformNoClone().is2D() && g2.isDepthBuffer() && g2.isDepthTest()) {
            return false;
        }
        if ((effect instanceof InnerShadow) && !aa2) {
            InnerShadow shadow = (InnerShadow) effect;
            float radius = shadow.getRadius();
            if (radius > 0.0f && radius < rw / 2.0f && radius < rh / 2.0f && shadow.getChoke() == 0.0f && shadow.getShadowSourceInput() == null && shadow.getContentInput() == null) {
                node.renderContent(g2);
                renderRectInnerShadow(g2, shadow, alpha, rx, ry, rw, rh);
                return true;
            }
            return false;
        }
        if (effect instanceof DropShadow) {
            DropShadow shadow2 = (DropShadow) effect;
            float radius2 = shadow2.getRadius();
            if (radius2 > 0.0f && radius2 < rw / 2.0f && radius2 < rh / 2.0f && shadow2.getSpread() == 0.0f && shadow2.getShadowSourceInput() == null && shadow2.getContentInput() == null) {
                renderRectDropShadow(g2, shadow2, alpha, rx, ry, rw, rh);
                node.renderContent(g2);
                return true;
            }
            return false;
        }
        return false;
    }

    static void renderRectInnerShadow(Graphics g2, InnerShadow shadow, float alpha, float rx, float ry, float rw, float rh) {
        if (itex == null || itex.isSurfaceLost()) {
            byte[] sdata = new byte[65536];
            fillGaussian(sdata, 256, 128.0f, shadow.getChoke(), true);
            Image img = Image.fromByteAlphaData(sdata, 256, 256);
            itex = g2.getResourceFactory().createTexture(img, Texture.Usage.STATIC, Texture.WrapMode.CLAMP_TO_EDGE);
            if (!$assertionsDisabled && itex.getWrapMode() != Texture.WrapMode.CLAMP_TO_EDGE) {
                throw new AssertionError();
            }
            itex.contentsUseful();
            itex.makePermanent();
        }
        float r2 = shadow.getRadius();
        int texsize = itex.getPhysicalWidth();
        int tcx1 = itex.getContentX();
        int tcx2 = tcx1 + itex.getContentWidth();
        float t1 = (tcx1 + 0.5f) / texsize;
        float t2 = (tcx2 - 0.5f) / texsize;
        float cx2 = rx + rw;
        float cy2 = ry + rh;
        float ox1 = rx + shadow.getOffsetX();
        float oy1 = ry + shadow.getOffsetY();
        float ox2 = ox1 + rw;
        float oy2 = oy1 + rh;
        g2.setPaint(toPrismColor(shadow.getColor(), alpha));
        drawClippedTexture(g2, itex, rx, ry, cx2, cy2, rx, ry, cx2, oy1 - r2, t1, t1, t1, t1);
        drawClippedTexture(g2, itex, rx, ry, cx2, cy2, ox1 - r2, oy1 - r2, ox1 + r2, oy1 + r2, t1, t1, t2, t2);
        drawClippedTexture(g2, itex, rx, ry, cx2, cy2, ox1 + r2, oy1 - r2, ox2 - r2, oy1 + r2, t2, t1, t2, t2);
        drawClippedTexture(g2, itex, rx, ry, cx2, cy2, ox2 - r2, oy1 - r2, ox2 + r2, oy1 + r2, t2, t1, t1, t2);
        drawClippedTexture(g2, itex, rx, ry, cx2, cy2, rx, oy1 - r2, ox1 - r2, oy2 + r2, t1, t1, t1, t1);
        drawClippedTexture(g2, itex, rx, ry, cx2, cy2, ox1 - r2, oy1 + r2, ox1 + r2, oy2 - r2, t1, t2, t2, t2);
        drawClippedTexture(g2, itex, rx, ry, cx2, cy2, ox2 - r2, oy1 + r2, ox2 + r2, oy2 - r2, t2, t2, t1, t2);
        drawClippedTexture(g2, itex, rx, ry, cx2, cy2, ox2 + r2, oy1 - r2, cx2, oy2 + r2, t1, t1, t1, t1);
        drawClippedTexture(g2, itex, rx, ry, cx2, cy2, ox1 - r2, oy2 - r2, ox1 + r2, oy2 + r2, t1, t2, t2, t1);
        drawClippedTexture(g2, itex, rx, ry, cx2, cy2, ox1 + r2, oy2 - r2, ox2 - r2, oy2 + r2, t2, t2, t2, t1);
        drawClippedTexture(g2, itex, rx, ry, cx2, cy2, ox2 - r2, oy2 - r2, ox2 + r2, oy2 + r2, t2, t2, t1, t1);
        drawClippedTexture(g2, itex, rx, ry, cx2, cy2, rx, oy2 + r2, cx2, cy2, t1, t1, t1, t1);
    }

    static void drawClippedTexture(Graphics g2, Texture tex, float cx1, float cy1, float cx2, float cy2, float ox1, float oy1, float ox2, float oy2, float tx1, float ty1, float tx2, float ty2) {
        if (ox1 >= ox2 || oy1 >= oy2 || cx1 >= cx2 || cy1 >= cy2 || ox2 <= cx1 || ox1 >= cx2) {
            return;
        }
        if (ox1 < cx1) {
            tx1 += ((tx2 - tx1) * (cx1 - ox1)) / (ox2 - ox1);
            ox1 = cx1;
        }
        if (ox2 > cx2) {
            tx2 -= ((tx2 - tx1) * (ox2 - cx2)) / (ox2 - ox1);
            ox2 = cx2;
        }
        if (oy2 <= cy1 || oy1 >= cy2) {
            return;
        }
        if (oy1 < cy1) {
            ty1 += ((ty2 - ty1) * (cy1 - oy1)) / (oy2 - oy1);
            oy1 = cy1;
        }
        if (oy2 > cy2) {
            ty2 -= ((ty2 - ty1) * (oy2 - cy2)) / (oy2 - oy1);
            oy2 = cy2;
        }
        g2.drawTextureRaw(tex, ox1, oy1, ox2, oy2, tx1, ty1, tx2, ty2);
    }

    static void renderRectDropShadow(Graphics g2, DropShadow shadow, float alpha, float rx, float ry, float rw, float rh) {
        if (dtex == null || dtex.isSurfaceLost()) {
            byte[] sdata = new byte[65536];
            fillGaussian(sdata, 256, 128.0f, shadow.getSpread(), false);
            Image img = Image.fromByteAlphaData(sdata, 256, 256);
            dtex = g2.getResourceFactory().createTexture(img, Texture.Usage.STATIC, Texture.WrapMode.CLAMP_TO_EDGE);
            if (!$assertionsDisabled && dtex.getWrapMode() != Texture.WrapMode.CLAMP_TO_EDGE) {
                throw new AssertionError();
            }
            dtex.contentsUseful();
            dtex.makePermanent();
        }
        float r2 = shadow.getRadius();
        int texsize = dtex.getPhysicalWidth();
        int cx1 = dtex.getContentX();
        int cx2 = cx1 + dtex.getContentWidth();
        float t1 = (cx1 + 0.5f) / texsize;
        float t2 = (cx2 - 0.5f) / texsize;
        float x1 = rx + shadow.getOffsetX();
        float y1 = ry + shadow.getOffsetY();
        float x2 = x1 + rw;
        float y2 = y1 + rh;
        g2.setPaint(toPrismColor(shadow.getColor(), alpha));
        g2.drawTextureRaw(dtex, x1 - r2, y1 - r2, x1 + r2, y1 + r2, t1, t1, t2, t2);
        g2.drawTextureRaw(dtex, x2 - r2, y1 - r2, x2 + r2, y1 + r2, t2, t1, t1, t2);
        g2.drawTextureRaw(dtex, x2 - r2, y2 - r2, x2 + r2, y2 + r2, t2, t2, t1, t1);
        g2.drawTextureRaw(dtex, x1 - r2, y2 - r2, x1 + r2, y2 + r2, t1, t2, t2, t1);
        g2.drawTextureRaw(dtex, x1 + r2, y1 + r2, x2 - r2, y2 - r2, t2, t2, t2, t2);
        g2.drawTextureRaw(dtex, x1 - r2, y1 + r2, x1 + r2, y2 - r2, t1, t2, t2, t2);
        g2.drawTextureRaw(dtex, x2 - r2, y1 + r2, x2 + r2, y2 - r2, t2, t2, t1, t2);
        g2.drawTextureRaw(dtex, x1 + r2, y1 - r2, x2 - r2, y1 + r2, t2, t1, t2, t2);
        g2.drawTextureRaw(dtex, x1 + r2, y2 - r2, x2 - r2, y2 + r2, t2, t2, t2, t1);
    }

    private static void fillGaussian(byte[] pixels, int dim, float r2, float spread, boolean inner) {
        float sigma = r2 / 3.0f;
        float sigma22 = 2.0f * sigma * sigma;
        if (sigma22 < Float.MIN_VALUE) {
            sigma22 = Float.MIN_VALUE;
        }
        float[] kvals = new float[dim];
        int center = (dim + 1) / 2;
        float total = 0.0f;
        for (int i2 = 0; i2 < kvals.length; i2++) {
            int d2 = center - i2;
            total += (float) Math.exp((-(d2 * d2)) / sigma22);
            kvals[i2] = total;
        }
        for (int i3 = 0; i3 < kvals.length; i3++) {
            int i4 = i3;
            kvals[i4] = kvals[i4] / total;
        }
        for (int y2 = 0; y2 < dim; y2++) {
            for (int x2 = 0; x2 < dim; x2++) {
                float v2 = kvals[y2] * kvals[x2];
                if (inner) {
                    v2 = 1.0f - v2;
                }
                int a2 = (int) (v2 * 255.0f);
                if (a2 < 0) {
                    a2 = 0;
                } else if (a2 > 255) {
                    a2 = 255;
                }
                pixels[(y2 * dim) + x2] = (byte) a2;
            }
        }
    }

    private static Color toPrismColor(Color4f decoraColor, float alpha) {
        float r2 = decoraColor.getRed();
        float g2 = decoraColor.getGreen();
        float b2 = decoraColor.getBlue();
        float a2 = decoraColor.getAlpha() * alpha;
        return new Color(r2, g2, b2, a2);
    }

    private EffectUtil() {
    }
}
