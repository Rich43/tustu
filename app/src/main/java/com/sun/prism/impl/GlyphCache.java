package com.sun.prism.impl;

import com.sun.javafx.font.FontStrike;
import com.sun.javafx.font.Glyph;
import com.sun.javafx.geom.Rectangle;
import com.sun.javafx.logging.PulseLogger;
import com.sun.prism.ResourceFactory;
import com.sun.prism.Texture;
import com.sun.prism.impl.packrect.RectanglePacker;
import com.sun.prism.impl.shape.MaskData;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.WeakHashMap;

/* loaded from: jfxrt.jar:com/sun/prism/impl/GlyphCache.class */
public class GlyphCache {
    private static ByteBuffer emptyMask;
    private final BaseContext context;
    private final FontStrike strike;
    private static final int SEGSHIFT = 5;
    private static final int SEGSIZE = 32;
    private static final int SEGMASK = 31;
    HashMap<Integer, GlyphData[]> glyphDataMap = new HashMap<>();
    private static final int SUBPIXEL_SHIFT = 27;
    private RectanglePacker packer;
    private boolean isLCDCache;
    private static final int WIDTH = PrismSettings.glyphCacheWidth;
    private static final int HEIGHT = PrismSettings.glyphCacheHeight;
    static WeakHashMap<BaseContext, RectanglePacker> greyPackerMap = new WeakHashMap<>();
    static WeakHashMap<BaseContext, RectanglePacker> lcdPackerMap = new WeakHashMap<>();

    public GlyphCache(BaseContext context, FontStrike strike) {
        this.context = context;
        this.strike = strike;
        this.isLCDCache = strike.getAAMode() == 1;
        WeakHashMap<BaseContext, RectanglePacker> packerMap = this.isLCDCache ? lcdPackerMap : greyPackerMap;
        this.packer = packerMap.get(context);
        if (this.packer == null) {
            ResourceFactory factory = context.getResourceFactory();
            Texture tex = factory.createMaskTexture(WIDTH, HEIGHT, Texture.WrapMode.CLAMP_NOT_NEEDED);
            tex.contentsUseful();
            tex.makePermanent();
            if (!this.isLCDCache) {
                factory.setGlyphTexture(tex);
            }
            tex.setLinearFiltering(false);
            this.packer = new RectanglePacker(tex, WIDTH, HEIGHT);
            packerMap.put(context, this.packer);
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:22:0x00d0  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public void render(com.sun.prism.impl.BaseContext r10, com.sun.javafx.scene.text.GlyphList r11, float r12, float r13, int r14, int r15, com.sun.prism.paint.Color r16, com.sun.prism.paint.Color r17, com.sun.javafx.geom.transform.BaseTransform r18, com.sun.javafx.geom.BaseBounds r19) {
        /*
            Method dump skipped, instructions count: 326
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.sun.prism.impl.GlyphCache.render(com.sun.prism.impl.BaseContext, com.sun.javafx.scene.text.GlyphList, float, float, int, int, com.sun.prism.paint.Color, com.sun.prism.paint.Color, com.sun.javafx.geom.transform.BaseTransform, com.sun.javafx.geom.BaseBounds):void");
    }

    private void addDataToQuad(GlyphData data, VertexBuffer vb, Texture tex, float x2, float y2, float dstw, float dsth) {
        float y3 = Math.round(y2);
        Rectangle rect = data.getRect();
        if (rect == null) {
            return;
        }
        int border = data.getBlankBoundary();
        float gw = rect.width - (border * 2);
        float gh = rect.height - (border * 2);
        float dx1 = data.getOriginX() + x2;
        float dy1 = data.getOriginY() + y3;
        float dy2 = dy1 + gh;
        float tw = tex.getPhysicalWidth();
        float th = tex.getPhysicalHeight();
        float tx1 = (rect.f11913x + border) / tw;
        float ty1 = (rect.f11914y + border) / th;
        float tx2 = tx1 + (gw / tw);
        float ty2 = ty1 + (gh / th);
        if (this.isLCDCache) {
            float dx12 = Math.round(dx1 * 3.0f) / 3.0f;
            float dx2 = dx12 + (gw / 3.0f);
            float t2x1 = dx12 / dstw;
            float t2x2 = dx2 / dstw;
            float t2y1 = dy1 / dsth;
            float t2y2 = dy2 / dsth;
            vb.addQuad(dx12, dy1, dx2, dy2, tx1, ty1, tx2, ty2, t2x1, t2y1, t2x2, t2y2);
            return;
        }
        float dx13 = Math.round(dx1);
        float dx22 = dx13 + gw;
        if (this.context.isSuperShaderEnabled()) {
            vb.addSuperQuad(dx13, dy1, dx22, dy2, tx1, ty1, tx2, ty2, true);
        } else {
            vb.addQuad(dx13, dy1, dx22, dy2, tx1, ty1, tx2, ty2);
        }
    }

    public Texture getBackingStore() {
        return this.packer.getBackingStore();
    }

    public void clear() {
        this.glyphDataMap.clear();
    }

    private void clearAll() {
        this.context.flushVertexBuffer();
        this.context.clearGlyphCaches();
        this.packer.clear();
    }

    private GlyphData getCachedGlyph(int glyphCode, int subPixel) {
        int subIndex = glyphCode & 31;
        int segIndex = (glyphCode >>> 5) | (subPixel << 27);
        GlyphData[] segment = this.glyphDataMap.get(Integer.valueOf(segIndex));
        if (segment != null) {
            if (segment[subIndex] != null) {
                return segment[subIndex];
            }
        } else {
            segment = new GlyphData[32];
            this.glyphDataMap.put(Integer.valueOf(segIndex), segment);
        }
        GlyphData data = null;
        Glyph glyph = this.strike.getGlyph(glyphCode);
        if (glyph != null) {
            byte[] glyphImage = glyph.getPixelData(subPixel);
            if (glyphImage == null || glyphImage.length == 0) {
                data = new GlyphData(0, 0, 0, glyph.getPixelXAdvance(), glyph.getPixelYAdvance(), null);
            } else {
                MaskData maskData = MaskData.create(glyphImage, glyph.getOriginX(), glyph.getOriginY(), glyph.getWidth(), glyph.getHeight());
                int rectW = maskData.getWidth() + (2 * 1);
                int rectH = maskData.getHeight() + (2 * 1);
                int originX = maskData.getOriginX();
                int originY = maskData.getOriginY();
                Rectangle rect = new Rectangle(0, 0, rectW, rectH);
                data = new GlyphData(originX, originY, 1, glyph.getPixelXAdvance(), glyph.getPixelYAdvance(), rect);
                if (!this.packer.add(rect)) {
                    if (PulseLogger.PULSE_LOGGING_ENABLED) {
                        PulseLogger.incrementCounter("Font Glyph Cache Cleared");
                    }
                    clearAll();
                    if (!this.packer.add(rect)) {
                        if (PrismSettings.verbose) {
                            System.out.println(((Object) rect) + " won't fit in GlyphCache");
                            return null;
                        }
                        return null;
                    }
                }
                Texture backingStore = getBackingStore();
                int emw = rect.width;
                int emh = rect.height;
                int bpp = backingStore.getPixelFormat().getBytesPerPixelUnit();
                int stride = emw * bpp;
                int size = stride * emh;
                if (emptyMask == null || size > emptyMask.capacity()) {
                    emptyMask = BufferUtil.newByteBuffer(size);
                }
                try {
                    backingStore.update(emptyMask, backingStore.getPixelFormat(), rect.f11913x, rect.f11914y, 0, 0, emw, emh, stride, true);
                    maskData.uploadToTexture(backingStore, 1 + rect.f11913x, 1 + rect.f11914y, true);
                } catch (Exception e2) {
                    if (PrismSettings.verbose) {
                        e2.printStackTrace();
                        return null;
                    }
                    return null;
                }
            }
            segment[subIndex] = data;
        }
        return data;
    }

    /* loaded from: jfxrt.jar:com/sun/prism/impl/GlyphCache$GlyphData.class */
    static class GlyphData {
        private final int originX;
        private final int originY;
        private final int blankBoundary;
        private final float xAdvance;
        private final float yAdvance;
        private final Rectangle rect;

        GlyphData(int originX, int originY, int blankBoundary, float xAdvance, float yAdvance, Rectangle rect) {
            this.originX = originX;
            this.originY = originY;
            this.blankBoundary = blankBoundary;
            this.xAdvance = xAdvance;
            this.yAdvance = yAdvance;
            this.rect = rect;
        }

        int getOriginX() {
            return this.originX;
        }

        int getOriginY() {
            return this.originY;
        }

        int getBlankBoundary() {
            return this.blankBoundary;
        }

        float getXAdvance() {
            return this.xAdvance;
        }

        float getYAdvance() {
            return this.yAdvance;
        }

        Rectangle getRect() {
            return this.rect;
        }
    }

    private static void disposePackerForContext(BaseContext ctx, WeakHashMap<BaseContext, RectanglePacker> packerMap) {
        RectanglePacker packer = packerMap.remove(ctx);
        if (packer != null) {
            packer.dispose();
        }
    }

    public static void disposeForContext(BaseContext ctx) {
        disposePackerForContext(ctx, greyPackerMap);
        disposePackerForContext(ctx, lcdPackerMap);
    }
}
