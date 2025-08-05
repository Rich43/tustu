package sun.font;

import java.awt.Font;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;
import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

/* loaded from: rt.jar:sun/font/GlyphLayout.class */
public final class GlyphLayout {
    private static volatile GlyphLayout cache;
    private LayoutEngineFactory _lef;
    private int _ercount;
    private int _typo_flags;
    private int _offset;
    private GVData _gvdata = new GVData();
    private TextRecord _textRecord = new TextRecord();
    private ScriptRun _scriptRuns = new ScriptRun();
    private FontRunIterator _fontRuns = new FontRunIterator();
    private ArrayList _erecords = new ArrayList(10);
    private Point2D.Float _pt = new Point2D.Float();
    private FontStrikeDesc _sd = new FontStrikeDesc();
    private float[] _mat = new float[4];

    /* loaded from: rt.jar:sun/font/GlyphLayout$LayoutEngine.class */
    public interface LayoutEngine {
        void layout(FontStrikeDesc fontStrikeDesc, float[] fArr, int i2, int i3, TextRecord textRecord, int i4, Point2D.Float r7, GVData gVData);
    }

    /* loaded from: rt.jar:sun/font/GlyphLayout$LayoutEngineFactory.class */
    public interface LayoutEngineFactory {
        LayoutEngine getEngine(Font2D font2D, int i2, int i3);

        LayoutEngine getEngine(LayoutEngineKey layoutEngineKey);
    }

    /* loaded from: rt.jar:sun/font/GlyphLayout$LayoutEngineKey.class */
    public static final class LayoutEngineKey {
        private Font2D font;
        private int script;
        private int lang;

        LayoutEngineKey() {
        }

        LayoutEngineKey(Font2D font2D, int i2, int i3) {
            init(font2D, i2, i3);
        }

        void init(Font2D font2D, int i2, int i3) {
            this.font = font2D;
            this.script = i2;
            this.lang = i3;
        }

        LayoutEngineKey copy() {
            return new LayoutEngineKey(this.font, this.script, this.lang);
        }

        Font2D font() {
            return this.font;
        }

        int script() {
            return this.script;
        }

        int lang() {
            return this.lang;
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            try {
                LayoutEngineKey layoutEngineKey = (LayoutEngineKey) obj;
                if (this.script == layoutEngineKey.script && this.lang == layoutEngineKey.lang) {
                    if (this.font.equals(layoutEngineKey.font)) {
                        return true;
                    }
                }
                return false;
            } catch (ClassCastException e2) {
                return false;
            }
        }

        public int hashCode() {
            return (this.script ^ this.lang) ^ this.font.hashCode();
        }
    }

    public static GlyphLayout get(LayoutEngineFactory layoutEngineFactory) {
        if (layoutEngineFactory == null) {
            layoutEngineFactory = SunLayoutEngine.instance();
        }
        GlyphLayout glyphLayout = null;
        synchronized (GlyphLayout.class) {
            if (cache != null) {
                glyphLayout = cache;
                cache = null;
            }
        }
        if (glyphLayout == null) {
            glyphLayout = new GlyphLayout();
        }
        glyphLayout._lef = layoutEngineFactory;
        return glyphLayout;
    }

    public static void done(GlyphLayout glyphLayout) {
        glyphLayout._lef = null;
        cache = glyphLayout;
    }

    /* loaded from: rt.jar:sun/font/GlyphLayout$SDCache.class */
    private static final class SDCache {
        public Font key_font;
        public FontRenderContext key_frc;
        public AffineTransform dtx;
        public AffineTransform invdtx;
        public AffineTransform gtx;
        public Point2D.Float delta;
        public FontStrikeDesc sd;
        private static final Point2D.Float ZERO_DELTA = new Point2D.Float();
        private static SoftReference<ConcurrentHashMap<SDKey, SDCache>> cacheRef;

        private SDCache(Font font, FontRenderContext fontRenderContext) {
            this.key_font = font;
            this.key_frc = fontRenderContext;
            this.dtx = fontRenderContext.getTransform();
            this.dtx.setTransform(this.dtx.getScaleX(), this.dtx.getShearY(), this.dtx.getShearX(), this.dtx.getScaleY(), 0.0d, 0.0d);
            if (!this.dtx.isIdentity()) {
                try {
                    this.invdtx = this.dtx.createInverse();
                } catch (NoninvertibleTransformException e2) {
                    throw new InternalError(e2);
                }
            }
            float size2D = font.getSize2D();
            if (font.isTransformed()) {
                this.gtx = font.getTransform();
                this.gtx.scale(size2D, size2D);
                this.delta = new Point2D.Float((float) this.gtx.getTranslateX(), (float) this.gtx.getTranslateY());
                this.gtx.setTransform(this.gtx.getScaleX(), this.gtx.getShearY(), this.gtx.getShearX(), this.gtx.getScaleY(), 0.0d, 0.0d);
                this.gtx.preConcatenate(this.dtx);
            } else {
                this.delta = ZERO_DELTA;
                this.gtx = new AffineTransform(this.dtx);
                this.gtx.scale(size2D, size2D);
            }
            this.sd = new FontStrikeDesc(this.dtx, this.gtx, font.getStyle(), FontStrikeDesc.getAAHintIntVal(fontRenderContext.getAntiAliasingHint(), FontUtilities.getFont2D(font), (int) Math.abs(size2D)), FontStrikeDesc.getFMHintIntVal(fontRenderContext.getFractionalMetricsHint()));
        }

        /* loaded from: rt.jar:sun/font/GlyphLayout$SDCache$SDKey.class */
        private static final class SDKey {
            private final Font font;
            private final FontRenderContext frc;
            private final int hash;

            SDKey(Font font, FontRenderContext fontRenderContext) {
                this.font = font;
                this.frc = fontRenderContext;
                this.hash = font.hashCode() ^ fontRenderContext.hashCode();
            }

            public int hashCode() {
                return this.hash;
            }

            public boolean equals(Object obj) {
                try {
                    SDKey sDKey = (SDKey) obj;
                    if (this.hash == sDKey.hash && this.font.equals(sDKey.font)) {
                        if (this.frc.equals(sDKey.frc)) {
                            return true;
                        }
                    }
                    return false;
                } catch (ClassCastException e2) {
                    return false;
                }
            }
        }

        public static SDCache get(Font font, FontRenderContext fontRenderContext) {
            if (fontRenderContext.isTransformed()) {
                AffineTransform transform = fontRenderContext.getTransform();
                if (transform.getTranslateX() != 0.0d || transform.getTranslateY() != 0.0d) {
                    fontRenderContext = new FontRenderContext(new AffineTransform(transform.getScaleX(), transform.getShearY(), transform.getShearX(), transform.getScaleY(), 0.0d, 0.0d), fontRenderContext.getAntiAliasingHint(), fontRenderContext.getFractionalMetricsHint());
                }
            }
            SDKey sDKey = new SDKey(font, fontRenderContext);
            ConcurrentHashMap<SDKey, SDCache> concurrentHashMap = null;
            SDCache sDCache = null;
            if (cacheRef != null) {
                concurrentHashMap = cacheRef.get();
                if (concurrentHashMap != null) {
                    sDCache = concurrentHashMap.get(sDKey);
                }
            }
            if (sDCache == null) {
                sDCache = new SDCache(font, fontRenderContext);
                if (concurrentHashMap == null) {
                    concurrentHashMap = new ConcurrentHashMap<>(10);
                    cacheRef = new SoftReference<>(concurrentHashMap);
                } else if (concurrentHashMap.size() >= 512) {
                    concurrentHashMap.clear();
                }
                concurrentHashMap.put(sDKey, sDCache);
            }
            return sDCache;
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r21v0 */
    public StandardGlyphVector layout(Font font, FontRenderContext fontRenderContext, char[] cArr, int i2, int i3, int i4, StandardGlyphVector standardGlyphVector) {
        StandardGlyphVector standardGlyphVectorCreateGlyphVector;
        if (cArr == null || i2 < 0 || i3 < 0 || i3 > cArr.length - i2) {
            throw new IllegalArgumentException();
        }
        init(i3);
        if (font.hasLayoutAttributes()) {
            AttributeValues values = ((AttributeMap) font.getAttributes()).getValues();
            if (values.getKerning() != 0) {
                this._typo_flags |= 1;
            }
            if (values.getLigatures() != 0) {
                this._typo_flags |= 2;
            }
        }
        this._offset = i2;
        SDCache sDCache = SDCache.get(font, fontRenderContext);
        this._mat[0] = (float) sDCache.gtx.getScaleX();
        this._mat[1] = (float) sDCache.gtx.getShearY();
        this._mat[2] = (float) sDCache.gtx.getShearX();
        this._mat[3] = (float) sDCache.gtx.getScaleY();
        this._pt.setLocation(sDCache.delta);
        int i5 = i2 + i3;
        int i6 = 0;
        int length = cArr.length;
        if (i4 != 0) {
            if ((i4 & 1) != 0) {
                this._typo_flags |= Integer.MIN_VALUE;
            }
            if ((i4 & 2) != 0) {
                i6 = i2;
            }
            if ((i4 & 4) != 0) {
                length = i5;
            }
        }
        ?? font2D = FontUtilities.getFont2D(font);
        boolean z2 = font2D instanceof FontSubstitution;
        CompositeFont compositeFont2D = font2D;
        if (z2) {
            compositeFont2D = ((FontSubstitution) font2D).getCompositeFont2D();
        }
        this._textRecord.init(cArr, i2, i5, i6, length);
        int i7 = i2;
        if (compositeFont2D instanceof CompositeFont) {
            this._scriptRuns.init(cArr, i2, i3);
            this._fontRuns.init(compositeFont2D, cArr, i2, i5);
            while (this._scriptRuns.next()) {
                int scriptLimit = this._scriptRuns.getScriptLimit();
                int scriptCode = this._scriptRuns.getScriptCode();
                while (this._fontRuns.next(scriptCode, scriptLimit)) {
                    PhysicalFont font2 = this._fontRuns.getFont();
                    if (font2 instanceof NativeFont) {
                        font2 = ((NativeFont) font2).getDelegateFont();
                    }
                    int glyphMask = this._fontRuns.getGlyphMask();
                    int pos = this._fontRuns.getPos();
                    nextEngineRecord(i7, pos, scriptCode, -1, font2, glyphMask);
                    i7 = pos;
                }
            }
        } else {
            this._scriptRuns.init(cArr, i2, i3);
            while (this._scriptRuns.next()) {
                int scriptLimit2 = this._scriptRuns.getScriptLimit();
                nextEngineRecord(i7, scriptLimit2, this._scriptRuns.getScriptCode(), -1, compositeFont2D, 0);
                i7 = scriptLimit2;
            }
        }
        int i8 = 0;
        int i9 = this._ercount;
        int i10 = 1;
        if (this._typo_flags < 0) {
            i8 = i9 - 1;
            i9 = -1;
            i10 = -1;
        }
        this._sd = sDCache.sd;
        while (i8 != i9) {
            EngineRecord engineRecord = (EngineRecord) this._erecords.get(i8);
            while (true) {
                try {
                    engineRecord.layout();
                    break;
                } catch (IndexOutOfBoundsException e2) {
                    if (this._gvdata._count >= 0) {
                        this._gvdata.grow();
                    }
                }
            }
            if (this._gvdata._count < 0) {
                break;
            }
            i8 += i10;
        }
        if (this._gvdata._count < 0) {
            standardGlyphVectorCreateGlyphVector = new StandardGlyphVector(font, cArr, i2, i3, fontRenderContext);
            if (FontUtilities.debugFonts()) {
                FontUtilities.getLogger().warning("OpenType layout failed on font: " + ((Object) font));
            }
        } else {
            standardGlyphVectorCreateGlyphVector = this._gvdata.createGlyphVector(font, fontRenderContext, standardGlyphVector);
        }
        return standardGlyphVectorCreateGlyphVector;
    }

    private GlyphLayout() {
    }

    private void init(int i2) {
        this._typo_flags = 0;
        this._ercount = 0;
        this._gvdata.init(i2);
    }

    private void nextEngineRecord(int i2, int i3, int i4, int i5, Font2D font2D, int i6) {
        EngineRecord engineRecord;
        if (this._ercount == this._erecords.size()) {
            engineRecord = new EngineRecord();
            this._erecords.add(engineRecord);
        } else {
            engineRecord = (EngineRecord) this._erecords.get(this._ercount);
        }
        engineRecord.init(i2, i3, font2D, i4, i5, i6);
        this._ercount++;
    }

    /* loaded from: rt.jar:sun/font/GlyphLayout$GVData.class */
    public static final class GVData {
        public int _count;
        public int _flags;
        public int[] _glyphs;
        public float[] _positions;
        public int[] _indices;
        private static final int UNINITIALIZED_FLAGS = -1;

        public void init(int i2) {
            this._count = 0;
            this._flags = -1;
            if (this._glyphs == null || this._glyphs.length < i2) {
                if (i2 < 20) {
                    i2 = 20;
                }
                this._glyphs = new int[i2];
                this._positions = new float[(i2 * 2) + 2];
                this._indices = new int[i2];
            }
        }

        public void grow() {
            grow(this._glyphs.length / 4);
        }

        public void grow(int i2) {
            int length = this._glyphs.length + i2;
            int[] iArr = new int[length];
            System.arraycopy(this._glyphs, 0, iArr, 0, this._count);
            this._glyphs = iArr;
            float[] fArr = new float[(length * 2) + 2];
            System.arraycopy(this._positions, 0, fArr, 0, (this._count * 2) + 2);
            this._positions = fArr;
            int[] iArr2 = new int[length];
            System.arraycopy(this._indices, 0, iArr2, 0, this._count);
            this._indices = iArr2;
        }

        public void adjustPositions(AffineTransform affineTransform) {
            affineTransform.transform(this._positions, 0, this._positions, 0, this._count);
        }

        /* JADX WARN: Removed duplicated region for block: B:24:0x0066 A[PHI: r15
  0x0066: PHI (r15v5 int) = (r15v4 int), (r15v7 int) binds: [B:20:0x0055, B:22:0x005f] A[DONT_GENERATE, DONT_INLINE]] */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct code enable 'Show inconsistent code' option in preferences
        */
        public sun.font.StandardGlyphVector createGlyphVector(java.awt.Font r10, java.awt.font.FontRenderContext r11, sun.font.StandardGlyphVector r12) {
            /*
                Method dump skipped, instructions count: 299
                To view this dump change 'Code comments level' option to 'DEBUG'
            */
            throw new UnsupportedOperationException("Method not decompiled: sun.font.GlyphLayout.GVData.createGlyphVector(java.awt.Font, java.awt.font.FontRenderContext, sun.font.StandardGlyphVector):sun.font.StandardGlyphVector");
        }
    }

    /* loaded from: rt.jar:sun/font/GlyphLayout$EngineRecord.class */
    private final class EngineRecord {
        private int start;
        private int limit;
        private int gmask;
        private int eflags;
        private LayoutEngineKey key = new LayoutEngineKey();
        private LayoutEngine engine;

        EngineRecord() {
        }

        /* JADX WARN: Multi-variable type inference failed */
        /* JADX WARN: Type inference failed for: r0v32, types: [int] */
        void init(int i2, int i3, Font2D font2D, int i4, int i5, int i6) {
            this.start = i2;
            this.limit = i3;
            this.gmask = i6;
            this.key.init(font2D, i4, i5);
            this.eflags = 0;
            int i7 = i2;
            while (i7 < i3) {
                char codePoint = GlyphLayout.this._textRecord.text[i7];
                if (Character.isHighSurrogate(codePoint) && i7 < i3 - 1 && Character.isLowSurrogate(GlyphLayout.this._textRecord.text[i7 + 1])) {
                    i7++;
                    codePoint = Character.toCodePoint(codePoint, GlyphLayout.this._textRecord.text[i7]);
                }
                int type = Character.getType((int) codePoint);
                if (type != 6 && type != 7 && type != 8) {
                    i7++;
                } else {
                    this.eflags = 4;
                    break;
                }
            }
            this.engine = GlyphLayout.this._lef.getEngine(this.key);
        }

        void layout() {
            GlyphLayout.this._textRecord.start = this.start;
            GlyphLayout.this._textRecord.limit = this.limit;
            this.engine.layout(GlyphLayout.this._sd, GlyphLayout.this._mat, this.gmask, this.start - GlyphLayout.this._offset, GlyphLayout.this._textRecord, GlyphLayout.this._typo_flags | this.eflags, GlyphLayout.this._pt, GlyphLayout.this._gvdata);
        }
    }
}
