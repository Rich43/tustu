package com.sun.prism.impl.ps;

import com.sun.javafx.geom.RectBounds;
import com.sun.javafx.geom.Shape;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.prism.BasicStroke;
import com.sun.prism.Graphics;
import com.sun.prism.Texture;
import com.sun.prism.impl.Disposer;
import com.sun.prism.impl.PrismSettings;
import com.sun.prism.impl.VertexBuffer;
import com.sun.prism.impl.ps.BaseShaderContext;
import com.sun.prism.impl.shape.MaskData;
import com.sun.prism.impl.shape.ShapeUtil;
import com.sun.prism.paint.Paint;
import com.sun.prism.ps.Shader;
import java.util.Arrays;
import java.util.Comparator;

/* compiled from: CachingShapeRep.java */
/* loaded from: jfxrt.jar:com/sun/prism/impl/ps/CachingShapeRepState.class */
class CachingShapeRepState {
    private static final BaseTransform IDENT = BaseTransform.IDENTITY_TRANSFORM;
    private static final MaskCache maskCache = new MaskCache();
    private int renderCount;
    private Boolean tryCache;
    private BaseTransform lastXform;
    private float[] bbox;
    private final Object disposerReferent = new Object();
    private final MaskTexData texData = new MaskTexData();
    private final Disposer.Record disposerRecord = new CSRDisposerRecord(this.texData);

    /* compiled from: CachingShapeRep.java */
    /* loaded from: jfxrt.jar:com/sun/prism/impl/ps/CachingShapeRepState$MaskTexData.class */
    private static class MaskTexData {
        private CacheEntry cacheEntry;
        private Texture maskTex;
        private float maskX;
        private float maskY;
        private int maskW;
        private int maskH;

        private MaskTexData() {
        }

        void adjustOrigin(BaseTransform xform) {
            float dx = (float) (xform.getMxt() - this.cacheEntry.xform.getMxt());
            float dy = (float) (xform.getMyt() - this.cacheEntry.xform.getMyt());
            this.maskX = this.cacheEntry.texData.maskX + dx;
            this.maskY = this.cacheEntry.texData.maskY + dy;
        }

        MaskTexData copy() {
            MaskTexData data = new MaskTexData();
            data.cacheEntry = this.cacheEntry;
            data.maskTex = this.maskTex;
            data.maskX = this.maskX;
            data.maskY = this.maskY;
            data.maskW = this.maskW;
            data.maskH = this.maskH;
            return data;
        }

        void copyInto(MaskTexData other) {
            if (other == null) {
                throw new InternalError("MaskTexData must be non-null");
            }
            other.cacheEntry = this.cacheEntry;
            other.maskTex = this.maskTex;
            other.maskX = this.maskX;
            other.maskY = this.maskY;
            other.maskW = this.maskW;
            other.maskH = this.maskH;
        }
    }

    /* compiled from: CachingShapeRep.java */
    /* loaded from: jfxrt.jar:com/sun/prism/impl/ps/CachingShapeRepState$CacheEntry.class */
    private static class CacheEntry {
        Shape shape;
        BasicStroke stroke;
        BaseTransform xform;
        RectBounds xformBounds;
        MaskTexData texData;
        boolean antialiasedShape;
        int refCount;

        private CacheEntry() {
        }
    }

    /* compiled from: CachingShapeRep.java */
    /* loaded from: jfxrt.jar:com/sun/prism/impl/ps/CachingShapeRepState$MaskCache.class */
    private static class MaskCache {
        private static final int MAX_MASK_DIM = 512;
        private static final int MAX_SIZE_IN_PIXELS = 4194304;
        private static Comparator<CacheEntry> comparator = (o1, o2) -> {
            int widthCompare = Float.compare(o1.xformBounds.getWidth(), o2.xformBounds.getWidth());
            if (widthCompare != 0) {
                return widthCompare;
            }
            return Float.compare(o1.xformBounds.getHeight(), o2.xformBounds.getHeight());
        };
        private CacheEntry[] entries;
        private int entriesSize;
        private int totalPixels;
        private CacheEntry tmpKey;

        private MaskCache() {
            this.entries = new CacheEntry[8];
            this.entriesSize = 0;
            this.tmpKey = new CacheEntry();
            this.tmpKey.xformBounds = new RectBounds();
        }

        private void ensureSize(int size) {
            if (this.entries.length < size) {
                CacheEntry[] newEntries = new CacheEntry[(size * 3) / 2];
                System.arraycopy(this.entries, 0, newEntries, 0, this.entries.length);
                this.entries = newEntries;
            }
        }

        private void addEntry(CacheEntry entry) {
            ensureSize(this.entriesSize + 1);
            int pos = Arrays.binarySearch(this.entries, 0, this.entriesSize, entry, comparator);
            if (pos < 0) {
                pos ^= -1;
            }
            System.arraycopy(this.entries, pos, this.entries, pos + 1, this.entriesSize - pos);
            this.entries[pos] = entry;
            this.entriesSize++;
        }

        private void removeEntry(CacheEntry entry) {
            int pos = Arrays.binarySearch(this.entries, 0, this.entriesSize, entry, comparator);
            if (pos < 0) {
                throw new IllegalStateException("Trying to remove a cached item that's not in the cache");
            }
            if (this.entries[pos] != entry) {
                this.tmpKey.xformBounds.deriveWithNewBounds(0.0f, 0.0f, 0.0f, entry.xformBounds.getWidth(), Math.nextAfter(entry.xformBounds.getHeight(), Double.NEGATIVE_INFINITY), 0.0f);
                pos = Arrays.binarySearch(this.entries, 0, this.entriesSize, this.tmpKey, comparator);
                if (pos < 0) {
                    pos ^= -1;
                }
                this.tmpKey.xformBounds.deriveWithNewBounds(0.0f, 0.0f, 0.0f, entry.xformBounds.getWidth(), Math.nextAfter(entry.xformBounds.getHeight(), Double.POSITIVE_INFINITY), 0.0f);
                int toPos = Arrays.binarySearch(this.entries, 0, this.entriesSize, this.tmpKey, comparator);
                if (toPos < 0) {
                    toPos ^= -1;
                }
                while (this.entries[pos] != entry && pos < toPos) {
                    pos++;
                }
                if (pos >= toPos) {
                    throw new IllegalStateException("Trying to remove a cached item that's not in the cache");
                }
            }
            System.arraycopy(this.entries, pos + 1, this.entries, pos, (this.entriesSize - pos) - 1);
            this.entriesSize--;
        }

        boolean hasRoom(RectBounds xformBounds) {
            int w2 = (int) (xformBounds.getWidth() + 0.5f);
            int h2 = (int) (xformBounds.getHeight() + 0.5f);
            int size = w2 * h2;
            return w2 <= 512 && h2 <= 512 && this.totalPixels + size <= 4194304;
        }

        boolean entryMatches(CacheEntry entry, Shape shape, BasicStroke stroke, BaseTransform xform, boolean antialiasedShape) {
            return entry.antialiasedShape == antialiasedShape && CachingShapeRepState.equalsIgnoreTranslation(xform, entry.xform) && entry.shape.equals(shape) && (stroke != null ? stroke.equals(entry.stroke) : entry.stroke == null);
        }

        void get(BaseShaderContext context, MaskTexData texData, Shape shape, BasicStroke stroke, BaseTransform xform, RectBounds xformBounds, boolean xformBoundsIsACopy, boolean antialiasedShape) {
            if (texData != null) {
                if (texData.cacheEntry != null) {
                    throw new InternalError("CacheEntry should already be null");
                }
                this.tmpKey.xformBounds.deriveWithNewBounds(0.0f, 0.0f, 0.0f, xformBounds.getWidth(), Math.nextAfter(xformBounds.getHeight(), Double.NEGATIVE_INFINITY), 0.0f);
                int i2 = Arrays.binarySearch(this.entries, 0, this.entriesSize, this.tmpKey, comparator);
                if (i2 < 0) {
                    i2 ^= -1;
                }
                this.tmpKey.xformBounds.deriveWithNewBounds(0.0f, 0.0f, 0.0f, xformBounds.getWidth(), Math.nextAfter(xformBounds.getHeight(), Double.POSITIVE_INFINITY), 0.0f);
                int toPos = Arrays.binarySearch(this.entries, 0, this.entriesSize, this.tmpKey, comparator);
                if (toPos < 0) {
                    toPos ^= -1;
                }
                while (i2 < toPos) {
                    CacheEntry entry = this.entries[i2];
                    if (entryMatches(entry, shape, stroke, xform, antialiasedShape)) {
                        entry.texData.maskTex.lock();
                        if (entry.texData.maskTex.isSurfaceLost()) {
                            entry.texData.maskTex.unlock();
                        } else {
                            entry.refCount++;
                            entry.texData.copyInto(texData);
                            texData.cacheEntry = entry;
                            texData.adjustOrigin(xform);
                            return;
                        }
                    }
                    i2++;
                }
                MaskData maskData = ShapeUtil.rasterizeShape(shape, stroke, xformBounds, xform, true, antialiasedShape);
                int mw = maskData.getWidth();
                int mh = maskData.getHeight();
                texData.maskX = maskData.getOriginX();
                texData.maskY = maskData.getOriginY();
                texData.maskW = mw;
                texData.maskH = mh;
                texData.maskTex = context.getResourceFactory().createMaskTexture(mw, mh, Texture.WrapMode.CLAMP_TO_ZERO);
                maskData.uploadToTexture(texData.maskTex, 0, 0, false);
                texData.maskTex.contentsUseful();
                CacheEntry entry2 = new CacheEntry();
                entry2.shape = shape.copy();
                if (stroke != null) {
                    entry2.stroke = stroke.copy();
                }
                entry2.xform = xform.copy();
                entry2.xformBounds = xformBoundsIsACopy ? xformBounds : (RectBounds) xformBounds.copy();
                entry2.texData = texData.copy();
                entry2.antialiasedShape = antialiasedShape;
                entry2.refCount = 1;
                texData.cacheEntry = entry2;
                addEntry(entry2);
                this.totalPixels += mw * mh;
                return;
            }
            throw new InternalError("MaskTexData must be non-null");
        }

        void unref(MaskTexData texData) {
            if (texData != null) {
                CacheEntry entry = texData.cacheEntry;
                if (entry != null) {
                    texData.cacheEntry = null;
                    texData.maskTex = null;
                    entry.refCount--;
                    if (entry.refCount <= 0) {
                        removeEntry(entry);
                        entry.shape = null;
                        entry.stroke = null;
                        entry.xform = null;
                        entry.xformBounds = null;
                        entry.texData.maskTex.dispose();
                        entry.antialiasedShape = false;
                        entry.texData = null;
                        this.totalPixels -= texData.maskW * texData.maskH;
                        return;
                    }
                    return;
                }
                return;
            }
            throw new InternalError("MaskTexData must be non-null");
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean equalsIgnoreTranslation(BaseTransform a2, BaseTransform b2) {
        if (a2 == b2) {
            return true;
        }
        return a2.getMxx() == b2.getMxx() && a2.getMxy() == b2.getMxy() && a2.getMyx() == b2.getMyx() && a2.getMyy() == b2.getMyy();
    }

    CachingShapeRepState() {
        Disposer.addRecord(this.disposerReferent, this.disposerRecord);
    }

    void fillNoCache(Graphics g2, Shape shape) {
        g2.fill(shape);
    }

    void drawNoCache(Graphics g2, Shape shape) {
        g2.draw(shape);
    }

    void invalidate() {
        this.renderCount = 0;
        this.tryCache = null;
        this.lastXform = null;
        this.bbox = null;
    }

    private void invalidateMaskTexData() {
        this.tryCache = null;
        this.lastXform = null;
        maskCache.unref(this.texData);
    }

    void render(Graphics g2, Shape shape, RectBounds shapeBounds, BasicStroke stroke) {
        BaseTransform xform = g2.getTransformNoClone();
        if (this.lastXform == null || !equalsIgnoreTranslation(xform, this.lastXform)) {
            invalidateMaskTexData();
            if (this.lastXform != null) {
                this.renderCount = 0;
            }
        }
        if (this.texData.cacheEntry != null) {
            this.texData.maskTex.lock();
            if (this.texData.maskTex.isSurfaceLost()) {
                this.texData.maskTex.unlock();
                invalidateMaskTexData();
            }
        }
        RectBounds xformBounds = null;
        boolean boundsCopy = false;
        if (this.tryCache == null) {
            if (xform.isIdentity()) {
                xformBounds = shapeBounds;
            } else {
                RectBounds xformBounds2 = new RectBounds();
                boundsCopy = true;
                xformBounds = (RectBounds) xform.transform(shapeBounds, xformBounds2);
            }
            this.tryCache = Boolean.valueOf(!xformBounds.isEmpty() && maskCache.hasRoom(xformBounds));
        }
        this.renderCount++;
        if (this.tryCache == Boolean.FALSE || this.renderCount <= 1 || !(g2 instanceof BaseShaderGraphics) || ((BaseShaderGraphics) g2).isComplexPaint()) {
            if (stroke == null) {
                fillNoCache(g2, shape);
                return;
            } else {
                drawNoCache(g2, shape);
                return;
            }
        }
        BaseShaderGraphics bsg = (BaseShaderGraphics) g2;
        BaseShaderContext context = bsg.getContext();
        if (this.lastXform == null || !this.lastXform.equals(xform)) {
            if (xformBounds == null) {
                if (xform.isIdentity()) {
                    xformBounds = shapeBounds;
                } else {
                    RectBounds xformBounds3 = new RectBounds();
                    boundsCopy = true;
                    xformBounds = (RectBounds) xform.transform(shapeBounds, xformBounds3);
                }
            }
            if (this.texData.cacheEntry != null) {
                this.texData.adjustOrigin(xform);
            } else {
                maskCache.get(context, this.texData, shape, stroke, xform, xformBounds, boundsCopy, g2.isAntialiasedShape());
            }
            if (this.lastXform == null) {
                this.lastXform = xform.copy();
            } else {
                this.lastXform.setTransform(xform);
            }
        }
        Paint paint = bsg.getPaint();
        float bx2 = 0.0f;
        float by2 = 0.0f;
        float bw2 = 0.0f;
        float bh2 = 0.0f;
        if (paint.isProportional()) {
            if (this.bbox == null) {
                this.bbox = new float[]{Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY, Float.NEGATIVE_INFINITY, Float.NEGATIVE_INFINITY};
                Shape.accumulate(this.bbox, shape, BaseTransform.IDENTITY_TRANSFORM);
            }
            bx2 = this.bbox[0];
            by2 = this.bbox[1];
            bw2 = this.bbox[2] - bx2;
            bh2 = this.bbox[3] - by2;
        }
        int mw = this.texData.maskW;
        int mh = this.texData.maskH;
        Texture maskTex = this.texData.maskTex;
        float tw = maskTex.getPhysicalWidth();
        float th = maskTex.getPhysicalHeight();
        float dx1 = this.texData.maskX;
        float dy1 = this.texData.maskY;
        float dx2 = dx1 + mw;
        float dy2 = dy1 + mh;
        float tx1 = maskTex.getContentX() / tw;
        float ty1 = maskTex.getContentY() / th;
        float tx2 = tx1 + (mw / tw);
        float ty2 = ty1 + (mh / th);
        if (PrismSettings.primTextureSize != 0) {
            Shader shader = context.validatePaintOp(bsg, IDENT, BaseShaderContext.MaskType.ALPHA_TEXTURE, this.texData.maskTex, bx2, by2, bw2, bh2);
            VertexBuffer vb = context.getVertexBuffer();
            vb.addQuad(dx1, dy1, dx2, dy2, tx1, ty1, tx2, ty2, bsg.getPaintTextureTx(xform, shader, bx2, by2, bw2, bh2));
        } else {
            context.validatePaintOp(bsg, IDENT, this.texData.maskTex, bx2, by2, bw2, bh2);
            VertexBuffer vb2 = context.getVertexBuffer();
            vb2.addQuad(dx1, dy1, dx2, dy2, tx1, ty1, tx2, ty2);
        }
        maskTex.unlock();
    }

    void dispose() {
        invalidate();
    }

    /* compiled from: CachingShapeRep.java */
    /* loaded from: jfxrt.jar:com/sun/prism/impl/ps/CachingShapeRepState$CSRDisposerRecord.class */
    private static class CSRDisposerRecord implements Disposer.Record {
        private MaskTexData texData;

        private CSRDisposerRecord(MaskTexData texData) {
            this.texData = texData;
        }

        @Override // com.sun.prism.impl.Disposer.Record
        public void dispose() {
            if (this.texData != null) {
                CachingShapeRepState.maskCache.unref(this.texData);
                this.texData = null;
            }
        }
    }
}
