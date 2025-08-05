package sun.font;

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GraphicsEnvironment;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.util.concurrent.ConcurrentHashMap;
import sun.java2d.Disposer;
import sun.java2d.DisposerRecord;

/* loaded from: rt.jar:sun/font/FontDesignMetrics.class */
public final class FontDesignMetrics extends FontMetrics {
    static final long serialVersionUID = 4480069578560887773L;
    private static final float UNKNOWN_WIDTH = -1.0f;
    private static final int CURRENT_VERSION = 1;
    private Font font;
    private float ascent;
    private float descent;
    private float leading;
    private float maxAdvance;
    private double[] matrix;
    private int[] cache;
    private int serVersion;
    private boolean isAntiAliased;
    private boolean usesFractionalMetrics;
    private AffineTransform frcTx;
    private transient float[] advCache;
    private transient int height;
    private transient FontRenderContext frc;
    private transient double[] devmatrix;
    private transient FontStrike fontStrike;
    private static final int MAXRECENT = 5;
    private static float roundingUpValue = 0.95f;
    private static FontRenderContext DEFAULT_FRC = null;
    private static final ConcurrentHashMap<Object, KeyReference> metricsCache = new ConcurrentHashMap<>();
    private static final FontDesignMetrics[] recentMetrics = new FontDesignMetrics[5];
    private static int recentIndex = 0;

    private static FontRenderContext getDefaultFrc() {
        AffineTransform defaultTransform;
        if (DEFAULT_FRC == null) {
            if (GraphicsEnvironment.isHeadless()) {
                defaultTransform = new AffineTransform();
            } else {
                defaultTransform = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration().getDefaultTransform();
            }
            DEFAULT_FRC = new FontRenderContext(defaultTransform, false, false);
        }
        return DEFAULT_FRC;
    }

    /* loaded from: rt.jar:sun/font/FontDesignMetrics$KeyReference.class */
    private static class KeyReference extends SoftReference implements DisposerRecord, Disposer.PollDisposable {
        static ReferenceQueue queue = Disposer.getQueue();
        Object key;

        KeyReference(Object obj, Object obj2) {
            super(obj2, queue);
            this.key = obj;
            Disposer.addReference(this, this);
        }

        @Override // sun.java2d.DisposerRecord
        public void dispose() {
            if (FontDesignMetrics.metricsCache.get(this.key) == this) {
                FontDesignMetrics.metricsCache.remove(this.key);
            }
        }
    }

    /* loaded from: rt.jar:sun/font/FontDesignMetrics$MetricsKey.class */
    private static class MetricsKey {
        Font font;
        FontRenderContext frc;
        int hash;
        static final MetricsKey key = new MetricsKey();

        MetricsKey() {
        }

        MetricsKey(Font font, FontRenderContext fontRenderContext) {
            init(font, fontRenderContext);
        }

        void init(Font font, FontRenderContext fontRenderContext) {
            this.font = font;
            this.frc = fontRenderContext;
            this.hash = font.hashCode() + fontRenderContext.hashCode();
        }

        public boolean equals(Object obj) {
            return (obj instanceof MetricsKey) && this.font.equals(((MetricsKey) obj).font) && this.frc.equals(((MetricsKey) obj).frc);
        }

        public int hashCode() {
            return this.hash;
        }
    }

    public static FontDesignMetrics getMetrics(Font font) {
        return getMetrics(font, getDefaultFrc());
    }

    public static FontDesignMetrics getMetrics(Font font, FontRenderContext fontRenderContext) {
        KeyReference keyReference;
        if (SunFontManager.getInstance().maybeUsingAlternateCompositeFonts() && (FontUtilities.getFont2D(font) instanceof CompositeFont)) {
            return new FontDesignMetrics(font, fontRenderContext);
        }
        FontDesignMetrics fontDesignMetrics = null;
        boolean zEquals = fontRenderContext.equals(getDefaultFrc());
        if (zEquals) {
            keyReference = metricsCache.get(font);
        } else {
            synchronized (MetricsKey.class) {
                MetricsKey.key.init(font, fontRenderContext);
                keyReference = metricsCache.get(MetricsKey.key);
            }
        }
        if (keyReference != null) {
            fontDesignMetrics = (FontDesignMetrics) keyReference.get();
        }
        if (fontDesignMetrics == null) {
            fontDesignMetrics = new FontDesignMetrics(font, fontRenderContext);
            if (zEquals) {
                metricsCache.put(font, new KeyReference(font, fontDesignMetrics));
            } else {
                MetricsKey metricsKey = new MetricsKey(font, fontRenderContext);
                metricsCache.put(metricsKey, new KeyReference(metricsKey, fontDesignMetrics));
            }
        }
        for (int i2 = 0; i2 < recentMetrics.length; i2++) {
            if (recentMetrics[i2] == fontDesignMetrics) {
                return fontDesignMetrics;
            }
        }
        synchronized (recentMetrics) {
            FontDesignMetrics[] fontDesignMetricsArr = recentMetrics;
            int i3 = recentIndex;
            recentIndex = i3 + 1;
            fontDesignMetricsArr[i3] = fontDesignMetrics;
            if (recentIndex == 5) {
                recentIndex = 0;
            }
        }
        return fontDesignMetrics;
    }

    private FontDesignMetrics(Font font) {
        this(font, getDefaultFrc());
    }

    private FontDesignMetrics(Font font, FontRenderContext fontRenderContext) {
        super(font);
        this.serVersion = 0;
        this.height = -1;
        this.devmatrix = null;
        this.font = font;
        this.frc = fontRenderContext;
        this.isAntiAliased = fontRenderContext.isAntiAliased();
        this.usesFractionalMetrics = fontRenderContext.usesFractionalMetrics();
        this.frcTx = fontRenderContext.getTransform();
        this.matrix = new double[4];
        initMatrixAndMetrics();
        initAdvCache();
    }

    private void initMatrixAndMetrics() {
        this.fontStrike = FontUtilities.getFont2D(this.font).getStrike(this.font, this.frc);
        StrikeMetrics fontMetrics = this.fontStrike.getFontMetrics();
        this.ascent = fontMetrics.getAscent();
        this.descent = fontMetrics.getDescent();
        this.leading = fontMetrics.getLeading();
        this.maxAdvance = fontMetrics.getMaxAdvance();
        this.devmatrix = new double[4];
        this.frcTx.getMatrix(this.devmatrix);
    }

    private void initAdvCache() {
        this.advCache = new float[256];
        for (int i2 = 0; i2 < 256; i2++) {
            this.advCache[i2] = -1.0f;
        }
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        objectInputStream.defaultReadObject();
        if (this.serVersion != 1) {
            this.frc = getDefaultFrc();
            this.isAntiAliased = this.frc.isAntiAliased();
            this.usesFractionalMetrics = this.frc.usesFractionalMetrics();
            this.frcTx = this.frc.getTransform();
        } else {
            this.frc = new FontRenderContext(this.frcTx, this.isAntiAliased, this.usesFractionalMetrics);
        }
        this.height = -1;
        this.cache = null;
        initMatrixAndMetrics();
        initAdvCache();
    }

    private void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
        this.cache = new int[256];
        for (int i2 = 0; i2 < 256; i2++) {
            this.cache[i2] = -1;
        }
        this.serVersion = 1;
        objectOutputStream.defaultWriteObject();
        this.cache = null;
    }

    private float handleCharWidth(int i2) {
        return this.fontStrike.getCodePointAdvance(i2);
    }

    private float getLatinCharWidth(char c2) {
        float fHandleCharWidth = this.advCache[c2];
        if (fHandleCharWidth == UNKNOWN_WIDTH) {
            fHandleCharWidth = handleCharWidth(c2);
            this.advCache[c2] = fHandleCharWidth;
        }
        return fHandleCharWidth;
    }

    @Override // java.awt.FontMetrics
    public FontRenderContext getFontRenderContext() {
        return this.frc;
    }

    @Override // java.awt.FontMetrics
    public int charWidth(char c2) {
        float fHandleCharWidth;
        if (c2 < 256) {
            fHandleCharWidth = getLatinCharWidth(c2);
        } else {
            fHandleCharWidth = handleCharWidth(c2);
        }
        return (int) (0.5d + fHandleCharWidth);
    }

    @Override // java.awt.FontMetrics
    public int charWidth(int i2) {
        if (!Character.isValidCodePoint(i2)) {
            i2 = 65535;
        }
        return (int) (0.5d + handleCharWidth(i2));
    }

    @Override // java.awt.FontMetrics
    public int stringWidth(String str) {
        float f2;
        float fHandleCharWidth;
        float advance = 0.0f;
        if (this.font.hasLayoutAttributes()) {
            if (str == null) {
                throw new NullPointerException("str is null");
            }
            if (str.length() == 0) {
                return 0;
            }
            advance = new TextLayout(str, this.font, this.frc).getAdvance();
        } else {
            int length = str.length();
            int i2 = 0;
            while (true) {
                if (i2 >= length) {
                    break;
                }
                char cCharAt = str.charAt(i2);
                if (cCharAt < 256) {
                    f2 = advance;
                    fHandleCharWidth = getLatinCharWidth(cCharAt);
                } else {
                    if (FontUtilities.isNonSimpleChar(cCharAt)) {
                        advance = new TextLayout(str, this.font, this.frc).getAdvance();
                        break;
                    }
                    f2 = advance;
                    fHandleCharWidth = handleCharWidth(cCharAt);
                }
                advance = f2 + fHandleCharWidth;
                i2++;
            }
        }
        return (int) (0.5d + advance);
    }

    @Override // java.awt.FontMetrics
    public int charsWidth(char[] cArr, int i2, int i3) {
        float f2;
        float fHandleCharWidth;
        float advance = 0.0f;
        if (this.font.hasLayoutAttributes()) {
            if (i3 == 0) {
                return 0;
            }
            advance = new TextLayout(new String(cArr, i2, i3), this.font, this.frc).getAdvance();
        } else {
            if (i3 < 0) {
                throw new IndexOutOfBoundsException("len=" + i3);
            }
            int i4 = i2 + i3;
            int i5 = i2;
            while (true) {
                if (i5 >= i4) {
                    break;
                }
                char c2 = cArr[i5];
                if (c2 < 256) {
                    f2 = advance;
                    fHandleCharWidth = getLatinCharWidth(c2);
                } else {
                    if (FontUtilities.isNonSimpleChar(c2)) {
                        advance = new TextLayout(new String(cArr, i2, i3), this.font, this.frc).getAdvance();
                        break;
                    }
                    f2 = advance;
                    fHandleCharWidth = handleCharWidth(c2);
                }
                advance = f2 + fHandleCharWidth;
                i5++;
            }
        }
        return (int) (0.5d + advance);
    }

    @Override // java.awt.FontMetrics
    public int[] getWidths() {
        int[] iArr = new int[256];
        char c2 = 0;
        while (true) {
            char c3 = c2;
            if (c3 < 256) {
                float f2 = this.advCache[c3];
                if (f2 == UNKNOWN_WIDTH) {
                    float[] fArr = this.advCache;
                    float fHandleCharWidth = handleCharWidth(c3);
                    fArr[c3] = fHandleCharWidth;
                    f2 = fHandleCharWidth;
                }
                iArr[c3] = (int) (0.5d + f2);
                c2 = (char) (c3 + 1);
            } else {
                return iArr;
            }
        }
    }

    @Override // java.awt.FontMetrics
    public int getMaxAdvance() {
        return (int) (0.99f + this.maxAdvance);
    }

    @Override // java.awt.FontMetrics
    public int getAscent() {
        return (int) (roundingUpValue + this.ascent);
    }

    @Override // java.awt.FontMetrics
    public int getDescent() {
        return (int) (roundingUpValue + this.descent);
    }

    @Override // java.awt.FontMetrics
    public int getLeading() {
        return ((int) ((roundingUpValue + this.descent) + this.leading)) - ((int) (roundingUpValue + this.descent));
    }

    @Override // java.awt.FontMetrics
    public int getHeight() {
        if (this.height < 0) {
            this.height = getAscent() + ((int) (roundingUpValue + this.descent + this.leading));
        }
        return this.height;
    }
}
