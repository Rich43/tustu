package com.sun.prism.impl;

import com.sun.glass.ui.Screen;
import com.sun.javafx.font.FontStrike;
import com.sun.javafx.geom.RectBounds;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.javafx.geom.transform.GeneralTransform3D;
import com.sun.javafx.image.ByteToBytePixelConverter;
import com.sun.javafx.image.impl.ByteGray;
import com.sun.javafx.sg.prism.NGCamera;
import com.sun.prism.PixelFormat;
import com.sun.prism.RTTexture;
import com.sun.prism.RenderTarget;
import com.sun.prism.ResourceFactory;
import com.sun.prism.Texture;
import com.sun.prism.impl.paint.PaintUtil;
import com.sun.prism.impl.shape.MaskData;
import com.sun.prism.paint.Gradient;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/* loaded from: jfxrt.jar:com/sun/prism/impl/BaseContext.class */
public abstract class BaseContext {
    private final Screen screen;
    private final ResourceFactory factory;
    private final VertexBuffer vertexBuffer;
    private static final int MIN_MASK_DIM = 1024;
    private Texture maskTex;
    private ByteBuffer maskBuffer;
    private ByteBuffer clearBuffer;
    private int curMaskRow;
    private int nextMaskRow;
    private int curMaskCol;
    private int highMaskCol;
    private Texture paintTex;
    private int[] paintPixels;
    private ByteBuffer paintBuffer;
    private Texture rectTex;
    private int rectTexMax;
    private Texture wrapRectTex;
    private Texture ovalTex;
    private boolean disposed = false;
    private final GeneralTransform3D perspectiveTransform = new GeneralTransform3D();
    private final Map<FontStrike, GlyphCache> greyGlyphCaches = new HashMap();
    private final Map<FontStrike, GlyphCache> lcdGlyphCaches = new HashMap();

    protected abstract void renderQuads(float[] fArr, byte[] bArr, int i2);

    protected abstract void setRenderTarget(RenderTarget renderTarget, NGCamera nGCamera, boolean z2, boolean z3);

    public abstract void validateClearOp(BaseGraphics baseGraphics);

    public abstract void validatePaintOp(BaseGraphics baseGraphics, BaseTransform baseTransform, Texture texture, float f2, float f3, float f4, float f5);

    public abstract void validateTextureOp(BaseGraphics baseGraphics, BaseTransform baseTransform, Texture texture, PixelFormat pixelFormat);

    public abstract RTTexture getLCDBuffer();

    protected BaseContext(Screen screen, ResourceFactory factory, int vbQuads) {
        this.screen = screen;
        this.factory = factory;
        this.vertexBuffer = new VertexBuffer(this, vbQuads);
    }

    protected void setDeviceParametersFor2D() {
    }

    protected void setDeviceParametersFor3D() {
    }

    public Screen getAssociatedScreen() {
        return this.screen;
    }

    public ResourceFactory getResourceFactory() {
        return this.factory;
    }

    public VertexBuffer getVertexBuffer() {
        return this.vertexBuffer;
    }

    public void flushVertexBuffer() {
        if (checkDisposed()) {
            return;
        }
        this.vertexBuffer.flush();
    }

    protected final void flushMask() {
        if (this.curMaskRow > 0 || this.curMaskCol > 0) {
            this.maskTex.lock();
            this.maskTex.update(this.maskBuffer, this.maskTex.getPixelFormat(), 0, 0, 0, 0, this.highMaskCol, this.nextMaskRow, this.maskTex.getContentWidth(), true);
            this.maskTex.unlock();
            this.highMaskCol = 0;
            this.nextMaskRow = 0;
            this.curMaskCol = 0;
            this.curMaskRow = 0;
        }
    }

    public void drawQuads(float[] coordArray, byte[] colorArray, int numVertices) {
        flushMask();
        renderQuads(coordArray, colorArray, numVertices);
    }

    protected GeneralTransform3D getPerspectiveTransformNoClone() {
        return this.perspectiveTransform;
    }

    protected void setPerspectiveTransform(GeneralTransform3D transform) {
        if (transform == null) {
            this.perspectiveTransform.setIdentity();
        } else {
            this.perspectiveTransform.set(transform);
        }
    }

    public void setRenderTarget(BaseGraphics g2) {
        if (g2 != null) {
            setRenderTarget(g2.getRenderTarget(), g2.getCameraNoClone(), g2.isDepthTest() && g2.isDepthBuffer(), g2.isState3D());
        } else {
            releaseRenderTarget();
        }
    }

    protected void releaseRenderTarget() {
    }

    public void clearGlyphCaches() {
        clearCaches(this.greyGlyphCaches);
        clearCaches(this.lcdGlyphCaches);
    }

    private void clearCaches(Map<FontStrike, GlyphCache> glyphCaches) {
        Iterator<FontStrike> iter = glyphCaches.keySet().iterator();
        while (iter.hasNext()) {
            iter.next().clearDesc();
        }
        for (GlyphCache cache : glyphCaches.values()) {
            if (cache != null) {
                cache.clear();
            }
        }
        glyphCaches.clear();
    }

    public GlyphCache getGlyphCache(FontStrike strike) {
        Map<FontStrike, GlyphCache> glyphCaches = strike.getAAMode() == 1 ? this.lcdGlyphCaches : this.greyGlyphCaches;
        return getGlyphCache(strike, glyphCaches);
    }

    public boolean isSuperShaderEnabled() {
        return false;
    }

    private GlyphCache getGlyphCache(FontStrike strike, Map<FontStrike, GlyphCache> glyphCaches) {
        if (checkDisposed()) {
            return null;
        }
        GlyphCache glyphCache = glyphCaches.get(strike);
        if (glyphCache == null) {
            glyphCache = new GlyphCache(this, strike);
            glyphCaches.put(strike, glyphCache);
        }
        return glyphCache;
    }

    public Texture validateMaskTexture(MaskData maskData, boolean canScale) {
        if (checkDisposed()) {
            return null;
        }
        int pad = canScale ? 1 : 0;
        int needW = maskData.getWidth() + pad + pad;
        int needH = maskData.getHeight() + pad + pad;
        int texW = 0;
        int texH = 0;
        if (this.maskTex != null) {
            this.maskTex.lock();
            if (this.maskTex.isSurfaceLost()) {
                this.maskTex = null;
            } else {
                texW = this.maskTex.getContentWidth();
                texH = this.maskTex.getContentHeight();
            }
        }
        if (this.maskTex == null || texW < needW || texH < needH) {
            if (this.maskTex != null) {
                flushVertexBuffer();
                this.maskTex.dispose();
                this.maskTex = null;
            }
            this.maskBuffer = null;
            int newTexW = Math.max(1024, Math.max(needW, texW));
            int newTexH = Math.max(1024, Math.max(needH, texH));
            this.maskTex = getResourceFactory().createMaskTexture(newTexW, newTexH, Texture.WrapMode.CLAMP_NOT_NEEDED);
            this.maskBuffer = ByteBuffer.allocate(newTexW * newTexH);
            if (this.clearBuffer == null || this.clearBuffer.capacity() < newTexW) {
                this.clearBuffer = null;
                this.clearBuffer = ByteBuffer.allocate(newTexW);
            }
            this.highMaskCol = 0;
            this.nextMaskRow = 0;
            this.curMaskCol = 0;
            this.curMaskRow = 0;
        }
        return this.maskTex;
    }

    public void updateMaskTexture(MaskData maskData, RectBounds maskBounds, boolean canScale) {
        if (checkDisposed()) {
            return;
        }
        this.maskTex.assertLocked();
        int maskW = maskData.getWidth();
        int maskH = maskData.getHeight();
        int texW = this.maskTex.getContentWidth();
        int texH = this.maskTex.getContentHeight();
        int pad = canScale ? 1 : 0;
        int needW = maskW + pad + pad;
        int needH = maskH + pad + pad;
        if (this.curMaskCol + needW > texW) {
            this.curMaskCol = 0;
            this.curMaskRow = this.nextMaskRow;
        }
        if (this.curMaskRow + needH > texH) {
            flushVertexBuffer();
        }
        int offset = (this.curMaskRow * texW) + this.curMaskCol;
        ByteToBytePixelConverter b2bpc = ByteGray.ToByteGrayConverter();
        if (canScale) {
            b2bpc.convert((ByteToBytePixelConverter) this.clearBuffer, 0, 0, (int) this.maskBuffer, offset, texW, maskW + 1, 1);
            int off = offset + maskW + 1;
            b2bpc.convert((ByteToBytePixelConverter) this.clearBuffer, 0, 0, (int) this.maskBuffer, off, texW, 1, maskH + 1);
            int off2 = offset + texW;
            b2bpc.convert((ByteToBytePixelConverter) this.clearBuffer, 0, 0, (int) this.maskBuffer, off2, texW, 1, maskH + 1);
            int off3 = offset + ((maskH + 1) * texW) + 1;
            b2bpc.convert((ByteToBytePixelConverter) this.clearBuffer, 0, 0, (int) this.maskBuffer, off3, texW, maskW + 1, 1);
            offset += texW + 1;
        }
        b2bpc.convert((ByteToBytePixelConverter) maskData.getMaskBuffer(), 0, maskW, (int) this.maskBuffer, offset, texW, maskW, maskH);
        float physW = this.maskTex.getPhysicalWidth();
        float physH = this.maskTex.getPhysicalHeight();
        maskBounds.setMinX((this.curMaskCol + pad) / physW);
        maskBounds.setMinY((this.curMaskRow + pad) / physH);
        maskBounds.setMaxX(((this.curMaskCol + pad) + maskW) / physW);
        maskBounds.setMaxY(((this.curMaskRow + pad) + maskH) / physH);
        this.curMaskCol += needW;
        if (this.highMaskCol < this.curMaskCol) {
            this.highMaskCol = this.curMaskCol;
        }
        if (this.nextMaskRow < this.curMaskRow + needH) {
            this.nextMaskRow = this.curMaskRow + needH;
        }
    }

    public int getRectTextureMaxSize() {
        if (checkDisposed()) {
            return 0;
        }
        if (this.rectTex == null) {
            createRectTexture();
        }
        return this.rectTexMax;
    }

    public Texture getRectTexture() {
        if (checkDisposed()) {
            return null;
        }
        if (this.rectTex == null) {
            createRectTexture();
        }
        this.rectTex.lock();
        return this.rectTex;
    }

    private void createRectTexture() {
        if (checkDisposed()) {
            return;
        }
        int texMax = PrismSettings.primTextureSize;
        if (texMax < 0) {
            texMax = getResourceFactory().getMaximumTextureSize();
        }
        int texDim = 3;
        int nextCellSize = 2;
        while (texDim + nextCellSize + 1 <= texMax) {
            this.rectTexMax = nextCellSize;
            nextCellSize++;
            texDim += nextCellSize;
        }
        byte[] mask = new byte[texDim * texDim];
        int cellY = 1;
        for (int cellH = 1; cellH <= this.rectTexMax; cellH++) {
            int cellX = 1;
            for (int cellW = 1; cellW <= this.rectTexMax; cellW++) {
                int index = (cellY * texDim) + cellX;
                for (int y2 = 0; y2 < cellH; y2++) {
                    for (int x2 = 0; x2 < cellW; x2++) {
                        mask[index + x2] = -1;
                    }
                    index += texDim;
                }
                cellX += cellW + 1;
            }
            cellY += cellH + 1;
        }
        if (PrismSettings.verbose) {
            System.out.println("max rectangle texture cell size = " + this.rectTexMax);
        }
        Texture tex = getResourceFactory().createMaskTexture(texDim, texDim, Texture.WrapMode.CLAMP_NOT_NEEDED);
        tex.contentsUseful();
        tex.makePermanent();
        PixelFormat pf = tex.getPixelFormat();
        int scan = texDim * pf.getBytesPerPixelUnit();
        tex.update(ByteBuffer.wrap(mask), pf, 0, 0, 0, 0, texDim, texDim, scan, false);
        this.rectTex = tex;
    }

    public Texture getWrapRectTexture() {
        if (checkDisposed()) {
            return null;
        }
        if (this.wrapRectTex == null) {
            Texture tex = getResourceFactory().createMaskTexture(2, 2, Texture.WrapMode.CLAMP_TO_EDGE);
            tex.contentsUseful();
            tex.makePermanent();
            int w2 = tex.getPhysicalWidth();
            int h2 = tex.getPhysicalHeight();
            if (PrismSettings.verbose) {
                System.out.println("wrap rectangle texture = " + w2 + " x " + h2);
            }
            byte[] mask = new byte[w2 * h2];
            int off = w2;
            for (int y2 = 1; y2 < h2; y2++) {
                for (int x2 = 1; x2 < h2; x2++) {
                    mask[off + x2] = -1;
                }
                off += w2;
            }
            PixelFormat pf = tex.getPixelFormat();
            int scan = w2 * pf.getBytesPerPixelUnit();
            tex.update(ByteBuffer.wrap(mask), pf, 0, 0, 0, 0, w2, h2, scan, false);
            this.wrapRectTex = tex;
        }
        this.wrapRectTex.lock();
        return this.wrapRectTex;
    }

    public Texture getOvalTexture() {
        if (checkDisposed()) {
            return null;
        }
        if (this.ovalTex == null) {
            int cellMax = getRectTextureMaxSize();
            int texDim = ((cellMax * (cellMax + 1)) / 2) + cellMax + 1;
            byte[] mask = new byte[texDim * texDim];
            int cellY = 1;
            for (int cellH = 1; cellH <= cellMax; cellH++) {
                int cellX = 1;
                for (int cellW = 1; cellW <= cellMax; cellW++) {
                    int index = (cellY * texDim) + cellX;
                    for (int y2 = 0; y2 < cellH; y2++) {
                        if (y2 * 2 >= cellH) {
                            int reflecty = (cellH - 1) - y2;
                            int rindex = index + ((reflecty - y2) * texDim);
                            for (int x2 = 0; x2 < cellW; x2++) {
                                mask[index + x2] = mask[rindex + x2];
                            }
                        } else {
                            float ovalY = y2 + 0.0625f;
                            for (int i2 = 0; i2 < 8; i2++) {
                                float ovalX = (ovalY / cellH) - 0.5f;
                                int oxi = Math.round(cellW * 4.0f * (1.0f - (((float) Math.sqrt(0.25f - (ovalX * ovalX))) * 2.0f)));
                                int edgeX = oxi >> 3;
                                int subX = oxi & 7;
                                int i3 = index + edgeX;
                                mask[i3] = (byte) (mask[i3] + (8 - subX));
                                int i4 = index + edgeX + 1;
                                mask[i4] = (byte) (mask[i4] + subX);
                                ovalY += 0.125f;
                            }
                            int accum = 0;
                            for (int x3 = 0; x3 < cellW; x3++) {
                                if (x3 * 2 >= cellW) {
                                    mask[index + x3] = mask[((index + cellW) - 1) - x3];
                                } else {
                                    accum += mask[index + x3];
                                    mask[index + x3] = (byte) (((accum * 255) + 32) / 64);
                                }
                            }
                            mask[index + cellW] = 0;
                        }
                        index += texDim;
                    }
                    cellX += cellW + 1;
                }
                cellY += cellH + 1;
            }
            Texture tex = getResourceFactory().createMaskTexture(texDim, texDim, Texture.WrapMode.CLAMP_NOT_NEEDED);
            tex.contentsUseful();
            tex.makePermanent();
            PixelFormat pf = tex.getPixelFormat();
            int scan = texDim * pf.getBytesPerPixelUnit();
            tex.update(ByteBuffer.wrap(mask), pf, 0, 0, 0, 0, texDim, texDim, scan, false);
            this.ovalTex = tex;
        }
        this.ovalTex.lock();
        return this.ovalTex;
    }

    public Texture getGradientTexture(Gradient grad, BaseTransform xform, int paintW, int paintH, MaskData maskData, float bx2, float by2, float bw2, float bh2) {
        if (checkDisposed()) {
            return null;
        }
        int sizeInPixels = paintW * paintH;
        int sizeInBytes = sizeInPixels * 4;
        if (this.paintBuffer == null || this.paintBuffer.capacity() < sizeInBytes) {
            this.paintPixels = new int[sizeInPixels];
            this.paintBuffer = ByteBuffer.wrap(new byte[sizeInBytes]);
        }
        if (this.paintTex != null) {
            this.paintTex.lock();
            if (this.paintTex.isSurfaceLost()) {
                this.paintTex = null;
            }
        }
        if (this.paintTex == null || this.paintTex.getContentWidth() < paintW || this.paintTex.getContentHeight() < paintH) {
            int newTexW = paintW;
            int newTexH = paintH;
            if (this.paintTex != null) {
                newTexW = Math.max(paintW, this.paintTex.getContentWidth());
                newTexH = Math.max(paintH, this.paintTex.getContentHeight());
                this.paintTex.dispose();
            }
            this.paintTex = getResourceFactory().createTexture(PixelFormat.BYTE_BGRA_PRE, Texture.Usage.DEFAULT, Texture.WrapMode.CLAMP_NOT_NEEDED, newTexW, newTexH);
        }
        PaintUtil.fillImageWithGradient(this.paintPixels, grad, xform, 0, 0, paintW, paintH, bx2, by2, bw2, bh2);
        byte[] bytePixels = this.paintBuffer.array();
        if (maskData != null) {
            byte[] maskPixels = maskData.getMaskBuffer().array();
            int j2 = 0;
            for (int i2 = 0; i2 < sizeInPixels; i2++) {
                int pixel = this.paintPixels[i2];
                int maskA = maskPixels[i2] & 255;
                int i3 = j2;
                int j3 = j2 + 1;
                bytePixels[i3] = (byte) (((pixel & 255) * maskA) / 255);
                int j4 = j3 + 1;
                bytePixels[j3] = (byte) ((((pixel >> 8) & 255) * maskA) / 255);
                int j5 = j4 + 1;
                bytePixels[j4] = (byte) ((((pixel >> 16) & 255) * maskA) / 255);
                j2 = j5 + 1;
                bytePixels[j5] = (byte) (((pixel >>> 24) * maskA) / 255);
            }
        } else {
            int j6 = 0;
            for (int i4 = 0; i4 < sizeInPixels; i4++) {
                int pixel2 = this.paintPixels[i4];
                int i5 = j6;
                int j7 = j6 + 1;
                bytePixels[i5] = (byte) (pixel2 & 255);
                int j8 = j7 + 1;
                bytePixels[j7] = (byte) ((pixel2 >> 8) & 255);
                int j9 = j8 + 1;
                bytePixels[j8] = (byte) ((pixel2 >> 16) & 255);
                j6 = j9 + 1;
                bytePixels[j9] = (byte) (pixel2 >>> 24);
            }
        }
        this.paintTex.update(this.paintBuffer, PixelFormat.BYTE_BGRA_PRE, 0, 0, 0, 0, paintW, paintH, paintW * 4, false);
        return this.paintTex;
    }

    public void dispose() {
        clearGlyphCaches();
        GlyphCache.disposeForContext(this);
        if (this.maskTex != null) {
            this.maskTex.dispose();
            this.maskTex = null;
        }
        if (this.paintTex != null) {
            this.paintTex.dispose();
            this.paintTex = null;
        }
        if (this.rectTex != null) {
            this.rectTex.dispose();
            this.rectTex = null;
        }
        if (this.wrapRectTex != null) {
            this.wrapRectTex.dispose();
            this.wrapRectTex = null;
        }
        if (this.ovalTex != null) {
            this.ovalTex.dispose();
            this.ovalTex = null;
        }
        this.disposed = true;
    }

    public final boolean isDisposed() {
        return this.disposed;
    }

    protected boolean checkDisposed() {
        if (PrismSettings.verbose && isDisposed()) {
            try {
                throw new IllegalStateException("attempt to use resource after context is disposed");
            } catch (RuntimeException ex) {
                ex.printStackTrace();
            }
        }
        return isDisposed();
    }
}
