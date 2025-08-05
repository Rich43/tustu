package sun.java2d.pipe;

import java.awt.AlphaComposite;
import java.awt.Composite;
import sun.font.GlyphList;
import sun.java2d.SunGraphics2D;

/* loaded from: rt.jar:sun/java2d/pipe/BufferedTextPipe.class */
public abstract class BufferedTextPipe extends GlyphListPipe {
    private static final int BYTES_PER_GLYPH_IMAGE = 8;
    private static final int BYTES_PER_GLYPH_POSITION = 8;
    private static final int OFFSET_CONTRAST = 8;
    private static final int OFFSET_RGBORDER = 2;
    private static final int OFFSET_SUBPIXPOS = 1;
    private static final int OFFSET_POSITIONS = 0;
    protected final RenderQueue rq;

    protected abstract void drawGlyphList(int i2, boolean z2, boolean z3, boolean z4, int i3, float f2, float f3, long[] jArr, float[] fArr);

    protected abstract void validateContext(SunGraphics2D sunGraphics2D, Composite composite);

    private static int createPackedParams(SunGraphics2D sunGraphics2D, GlyphList glyphList) {
        return ((glyphList.usePositions() ? 1 : 0) << 0) | ((glyphList.isSubPixPos() ? 1 : 0) << 1) | ((glyphList.isRGBOrder() ? 1 : 0) << 2) | ((sunGraphics2D.lcdTextContrast & 255) << 8);
    }

    protected BufferedTextPipe(RenderQueue renderQueue) {
        this.rq = renderQueue;
    }

    @Override // sun.java2d.pipe.GlyphListPipe
    protected void drawGlyphList(SunGraphics2D sunGraphics2D, GlyphList glyphList) {
        Composite composite = sunGraphics2D.composite;
        if (composite == AlphaComposite.Src) {
            composite = AlphaComposite.SrcOver;
        }
        this.rq.lock();
        try {
            validateContext(sunGraphics2D, composite);
            enqueueGlyphList(sunGraphics2D, glyphList);
            this.rq.unlock();
        } catch (Throwable th) {
            this.rq.unlock();
            throw th;
        }
    }

    private void enqueueGlyphList(final SunGraphics2D sunGraphics2D, final GlyphList glyphList) {
        RenderBuffer buffer = this.rq.getBuffer();
        final int numGlyphs = glyphList.getNumGlyphs();
        int i2 = 24 + (numGlyphs * 8) + (glyphList.usePositions() ? numGlyphs * 8 : 0);
        final long[] images = glyphList.getImages();
        final float x2 = glyphList.getX() + 0.5f;
        final float y2 = glyphList.getY() + 0.5f;
        this.rq.addReference(glyphList.getStrike());
        if (i2 <= buffer.capacity()) {
            if (i2 > buffer.remaining()) {
                this.rq.flushNow();
            }
            this.rq.ensureAlignment(20);
            buffer.putInt(40);
            buffer.putInt(numGlyphs);
            buffer.putInt(createPackedParams(sunGraphics2D, glyphList));
            buffer.putFloat(x2);
            buffer.putFloat(y2);
            buffer.put(images, 0, numGlyphs);
            if (glyphList.usePositions()) {
                buffer.put(glyphList.getPositions(), 0, 2 * numGlyphs);
                return;
            }
            return;
        }
        this.rq.flushAndInvokeNow(new Runnable() { // from class: sun.java2d.pipe.BufferedTextPipe.1
            @Override // java.lang.Runnable
            public void run() {
                BufferedTextPipe.this.drawGlyphList(numGlyphs, glyphList.usePositions(), glyphList.isSubPixPos(), glyphList.isRGBOrder(), sunGraphics2D.lcdTextContrast, x2, y2, images, glyphList.getPositions());
            }
        });
    }
}
