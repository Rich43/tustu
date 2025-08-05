package sun.font;

import java.lang.ref.Reference;
import java.util.concurrent.ConcurrentHashMap;
import sun.java2d.Disposer;
import sun.java2d.DisposerRecord;

/* loaded from: rt.jar:sun/font/FontStrikeDisposer.class */
class FontStrikeDisposer implements DisposerRecord, Disposer.PollDisposable {
    ConcurrentHashMap<FontStrikeDesc, Reference> strikeCache;
    FontStrikeDesc desc;
    long[] longGlyphImages;
    int[] intGlyphImages;
    int[][] segIntGlyphImages;
    long[][] segLongGlyphImages;
    long pScalerContext;
    boolean disposed;
    boolean comp;

    public FontStrikeDisposer(Font2D font2D, FontStrikeDesc fontStrikeDesc, long j2, int[] iArr) {
        this.pScalerContext = 0L;
        this.disposed = false;
        this.comp = false;
        this.strikeCache = font2D.strikeCache;
        this.desc = fontStrikeDesc;
        this.pScalerContext = j2;
        this.intGlyphImages = iArr;
    }

    public FontStrikeDisposer(Font2D font2D, FontStrikeDesc fontStrikeDesc, long j2, long[] jArr) {
        this.pScalerContext = 0L;
        this.disposed = false;
        this.comp = false;
        this.strikeCache = font2D.strikeCache;
        this.desc = fontStrikeDesc;
        this.pScalerContext = j2;
        this.longGlyphImages = jArr;
    }

    public FontStrikeDisposer(Font2D font2D, FontStrikeDesc fontStrikeDesc, long j2) {
        this.pScalerContext = 0L;
        this.disposed = false;
        this.comp = false;
        this.strikeCache = font2D.strikeCache;
        this.desc = fontStrikeDesc;
        this.pScalerContext = j2;
    }

    public FontStrikeDisposer(Font2D font2D, FontStrikeDesc fontStrikeDesc) {
        this.pScalerContext = 0L;
        this.disposed = false;
        this.comp = false;
        this.strikeCache = font2D.strikeCache;
        this.desc = fontStrikeDesc;
        this.comp = true;
    }

    @Override // sun.java2d.DisposerRecord
    public synchronized void dispose() {
        if (!this.disposed) {
            Reference reference = this.strikeCache.get(this.desc);
            if (reference != null && reference.get() == null) {
                this.strikeCache.remove(this.desc);
            }
            StrikeCache.disposeStrike(this);
            this.disposed = true;
        }
    }
}
