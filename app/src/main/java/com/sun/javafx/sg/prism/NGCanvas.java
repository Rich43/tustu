package com.sun.javafx.sg.prism;

import com.sun.javafx.font.PGFont;
import com.sun.javafx.geom.Arc2D;
import com.sun.javafx.geom.BaseBounds;
import com.sun.javafx.geom.DirtyRegionContainer;
import com.sun.javafx.geom.DirtyRegionPool;
import com.sun.javafx.geom.Path2D;
import com.sun.javafx.geom.PathIterator;
import com.sun.javafx.geom.RectBounds;
import com.sun.javafx.geom.Rectangle;
import com.sun.javafx.geom.RoundRectangle2D;
import com.sun.javafx.geom.Shape;
import com.sun.javafx.geom.transform.Affine2D;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.javafx.geom.transform.NoninvertibleTransformException;
import com.sun.javafx.text.PrismTextLayout;
import com.sun.javafx.tk.RenderJob;
import com.sun.javafx.tk.ScreenConfigurationAccessor;
import com.sun.javafx.tk.Toolkit;
import com.sun.prism.BasicStroke;
import com.sun.prism.CompositeMode;
import com.sun.prism.Graphics;
import com.sun.prism.GraphicsPipeline;
import com.sun.prism.Image;
import com.sun.prism.MaskTextureGraphics;
import com.sun.prism.PrinterGraphics;
import com.sun.prism.RTTexture;
import com.sun.prism.ResourceFactory;
import com.sun.prism.Texture;
import com.sun.prism.paint.Color;
import com.sun.prism.paint.Paint;
import com.sun.scenario.effect.Blend;
import com.sun.scenario.effect.Effect;
import com.sun.scenario.effect.FilterContext;
import com.sun.scenario.effect.Filterable;
import com.sun.scenario.effect.ImageData;
import com.sun.scenario.effect.impl.prism.PrDrawable;
import com.sun.scenario.effect.impl.prism.PrFilterContext;
import com.sun.scenario.effect.impl.prism.PrTexture;
import java.nio.IntBuffer;
import java.util.LinkedList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import javafx.geometry.VPos;
import javafx.scene.text.Font;
import javafx.scene.text.FontSmoothingType;

/* loaded from: jfxrt.jar:com/sun/javafx/sg/prism/NGCanvas.class */
public class NGCanvas extends NGNode {
    public static final byte ATTR_BASE = 0;
    public static final byte GLOBAL_ALPHA = 0;
    public static final byte COMP_MODE = 1;
    public static final byte FILL_PAINT = 2;
    public static final byte STROKE_PAINT = 3;
    public static final byte LINE_WIDTH = 4;
    public static final byte LINE_CAP = 5;
    public static final byte LINE_JOIN = 6;
    public static final byte MITER_LIMIT = 7;
    public static final byte FONT = 8;
    public static final byte TEXT_ALIGN = 9;
    public static final byte TEXT_BASELINE = 10;
    public static final byte TRANSFORM = 11;
    public static final byte EFFECT = 12;
    public static final byte PUSH_CLIP = 13;
    public static final byte POP_CLIP = 14;
    public static final byte ARC_TYPE = 15;
    public static final byte FILL_RULE = 16;
    public static final byte DASH_ARRAY = 17;
    public static final byte DASH_OFFSET = 18;
    public static final byte FONT_SMOOTH = 19;
    public static final byte IMAGE_SMOOTH = 20;
    public static final byte OP_BASE = 25;
    public static final byte FILL_RECT = 25;
    public static final byte STROKE_RECT = 26;
    public static final byte CLEAR_RECT = 27;
    public static final byte STROKE_LINE = 28;
    public static final byte FILL_OVAL = 29;
    public static final byte STROKE_OVAL = 30;
    public static final byte FILL_ROUND_RECT = 31;
    public static final byte STROKE_ROUND_RECT = 32;
    public static final byte FILL_ARC = 33;
    public static final byte STROKE_ARC = 34;
    public static final byte FILL_TEXT = 35;
    public static final byte STROKE_TEXT = 36;
    public static final byte PATH_BASE = 40;
    public static final byte PATHSTART = 40;
    public static final byte MOVETO = 41;
    public static final byte LINETO = 42;
    public static final byte QUADTO = 43;
    public static final byte CUBICTO = 44;
    public static final byte CLOSEPATH = 45;
    public static final byte PATHEND = 46;
    public static final byte FILL_PATH = 47;
    public static final byte STROKE_PATH = 48;
    public static final byte IMG_BASE = 50;
    public static final byte DRAW_IMAGE = 50;
    public static final byte DRAW_SUBIMAGE = 51;
    public static final byte PUT_ARGB = 52;
    public static final byte PUT_ARGBPRE_BUF = 53;
    public static final byte FX_BASE = 60;
    public static final byte FX_APPLY_EFFECT = 60;
    public static final byte UTIL_BASE = 70;
    public static final byte RESET = 70;
    public static final byte SET_DIMS = 71;
    public static final byte CAP_BUTT = 0;
    public static final byte CAP_ROUND = 1;
    public static final byte CAP_SQUARE = 2;
    public static final byte JOIN_MITER = 0;
    public static final byte JOIN_ROUND = 1;
    public static final byte JOIN_BEVEL = 2;
    public static final byte ARC_OPEN = 0;
    public static final byte ARC_CHORD = 1;
    public static final byte ARC_PIE = 2;
    public static final byte ALIGN_LEFT = 0;
    public static final byte ALIGN_CENTER = 1;
    public static final byte ALIGN_RIGHT = 2;
    public static final byte ALIGN_JUSTIFY = 3;
    public static final byte BASE_TOP = 0;
    public static final byte BASE_MIDDLE = 1;
    public static final byte BASE_ALPHABETIC = 2;
    public static final byte BASE_BOTTOM = 3;
    public static final byte FILL_RULE_NON_ZERO = 0;
    public static final byte FILL_RULE_EVEN_ODD = 1;
    private GrowableDataBuffer thebuf;
    private final float highestPixelScale;
    private int tw;
    private int th;
    private int cw;
    private int ch;
    private RenderBuf cv;
    private RenderBuf temp;
    private RenderBuf clip;
    private float globalAlpha;
    private Blend.Mode blendmode;
    private Paint fillPaint;
    private Paint strokePaint;
    private float linewidth;
    private int linecap;
    private int linejoin;
    private float miterlimit;
    private double[] dashes;
    private float dashOffset;
    private BasicStroke stroke;
    private Path2D path;
    private NGText ngtext;
    private PrismTextLayout textLayout;
    private PGFont pgfont;
    private int smoothing;
    private boolean imageSmoothing;
    private int align;
    private int baseline;
    private Affine2D transform;
    private Affine2D inverseTransform;
    private boolean inversedirty;
    private LinkedList<Path2D> clipStack;
    private int clipsRendered;
    private boolean clipIsRect;
    private Rectangle clipRect;
    private Effect effect;
    private int arctype;
    Shape untransformedPath = new Shape() { // from class: com.sun.javafx.sg.prism.NGCanvas.1
        @Override // com.sun.javafx.geom.Shape
        public RectBounds getBounds() {
            if (NGCanvas.this.transform.isTranslateOrIdentity()) {
                RectBounds rb = NGCanvas.this.path.getBounds();
                if (!NGCanvas.this.transform.isIdentity()) {
                    float tx = (float) NGCanvas.this.transform.getMxt();
                    float ty = (float) NGCanvas.this.transform.getMyt();
                    return new RectBounds(rb.getMinX() - tx, rb.getMinY() - ty, rb.getMaxX() - tx, rb.getMaxY() - ty);
                }
                return rb;
            }
            float x0 = Float.POSITIVE_INFINITY;
            float y0 = Float.POSITIVE_INFINITY;
            float x1 = Float.NEGATIVE_INFINITY;
            float y1 = Float.NEGATIVE_INFINITY;
            PathIterator pi = NGCanvas.this.path.getPathIterator(NGCanvas.this.getInverseTransform());
            while (!pi.isDone()) {
                int ncoords = NGCanvas.numCoords[pi.currentSegment(NGCanvas.TEMP_COORDS)];
                for (int i2 = 0; i2 < ncoords; i2 += 2) {
                    if (x0 > NGCanvas.TEMP_COORDS[i2 + 0]) {
                        x0 = NGCanvas.TEMP_COORDS[i2 + 0];
                    }
                    if (x1 < NGCanvas.TEMP_COORDS[i2 + 0]) {
                        x1 = NGCanvas.TEMP_COORDS[i2 + 0];
                    }
                    if (y0 > NGCanvas.TEMP_COORDS[i2 + 1]) {
                        y0 = NGCanvas.TEMP_COORDS[i2 + 1];
                    }
                    if (y1 < NGCanvas.TEMP_COORDS[i2 + 1]) {
                        y1 = NGCanvas.TEMP_COORDS[i2 + 1];
                    }
                }
                pi.next();
            }
            return new RectBounds(x0, y0, x1, y1);
        }

        @Override // com.sun.javafx.geom.Shape
        public boolean contains(float x2, float y2) {
            NGCanvas.TEMP_COORDS[0] = x2;
            NGCanvas.TEMP_COORDS[1] = y2;
            NGCanvas.this.transform.transform(NGCanvas.TEMP_COORDS, 0, NGCanvas.TEMP_COORDS, 0, 1);
            float x3 = NGCanvas.TEMP_COORDS[0];
            float y3 = NGCanvas.TEMP_COORDS[1];
            return NGCanvas.this.path.contains(x3, y3);
        }

        @Override // com.sun.javafx.geom.Shape
        public boolean intersects(float x2, float y2, float w2, float h2) {
            if (NGCanvas.this.transform.isTranslateOrIdentity()) {
                return NGCanvas.this.path.intersects((float) (x2 + NGCanvas.this.transform.getMxt()), (float) (y2 + NGCanvas.this.transform.getMyt()), w2, h2);
            }
            PathIterator pi = NGCanvas.this.path.getPathIterator(NGCanvas.this.getInverseTransform());
            int crossings = Shape.rectCrossingsForPath(pi, x2, y2, x2 + w2, y2 + h2);
            return crossings != 0;
        }

        @Override // com.sun.javafx.geom.Shape
        public boolean contains(float x2, float y2, float w2, float h2) {
            if (NGCanvas.this.transform.isTranslateOrIdentity()) {
                return NGCanvas.this.path.contains((float) (x2 + NGCanvas.this.transform.getMxt()), (float) (y2 + NGCanvas.this.transform.getMyt()), w2, h2);
            }
            PathIterator pi = NGCanvas.this.path.getPathIterator(NGCanvas.this.getInverseTransform());
            int crossings = Shape.rectCrossingsForPath(pi, x2, y2, x2 + w2, y2 + h2);
            return (crossings == Integer.MIN_VALUE || crossings == 0) ? false : true;
        }

        public BaseTransform getCombinedTransform(BaseTransform tx) {
            if (NGCanvas.this.transform.isIdentity()) {
                return tx;
            }
            if (NGCanvas.this.transform.equals(tx)) {
                return null;
            }
            Affine2D inv = NGCanvas.this.getInverseTransform();
            if (tx == null || tx.isIdentity()) {
                return inv;
            }
            NGCanvas.TEMP_PATH_TX.setTransform(tx);
            NGCanvas.TEMP_PATH_TX.concatenate(inv);
            return NGCanvas.TEMP_PATH_TX;
        }

        @Override // com.sun.javafx.geom.Shape
        public PathIterator getPathIterator(BaseTransform tx) {
            return NGCanvas.this.path.getPathIterator(getCombinedTransform(tx));
        }

        @Override // com.sun.javafx.geom.Shape
        public PathIterator getPathIterator(BaseTransform tx, float flatness) {
            return NGCanvas.this.path.getPathIterator(getCombinedTransform(tx), flatness);
        }

        @Override // com.sun.javafx.geom.Shape
        public Shape copy() {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    };
    private static final float CLIPRECT_TOLERANCE = 0.00390625f;
    public static final byte SMOOTH_GRAY = (byte) FontSmoothingType.GRAY.ordinal();
    public static final byte SMOOTH_LCD = (byte) FontSmoothingType.LCD.ordinal();
    private static Blend BLENDER = new MyBlend(Blend.Mode.SRC_OVER, null, null);
    static float[] TEMP_COORDS = new float[6];
    private static Arc2D TEMP_ARC = new Arc2D();
    private static RectBounds TEMP_RECTBOUNDS = new RectBounds();
    static final Affine2D TEMP_PATH_TX = new Affine2D();
    static final int[] numCoords = {2, 2, 4, 6, 0};
    private static final Rectangle TEMP_RECT = new Rectangle();
    private static final int[] prcaps = {0, 1, 2};
    private static final int[] prjoins = {0, 1, 2};
    private static final int[] prbases = {VPos.TOP.ordinal(), VPos.CENTER.ordinal(), VPos.BASELINE.ordinal(), VPos.BOTTOM.ordinal()};
    private static final Affine2D TEMP_TX = new Affine2D();

    /* loaded from: jfxrt.jar:com/sun/javafx/sg/prism/NGCanvas$InitType.class */
    enum InitType {
        CLEAR,
        FILL_WHITE,
        PRESERVE_UPPER_LEFT
    }

    /* loaded from: jfxrt.jar:com/sun/javafx/sg/prism/NGCanvas$RenderBuf.class */
    static class RenderBuf {
        final InitType init_type;
        RTTexture tex;

        /* renamed from: g, reason: collision with root package name */
        Graphics f11949g;
        EffectInput input;
        private PixelData savedPixelData = null;

        public RenderBuf(InitType init_type) {
            this.init_type = init_type;
        }

        public void dispose() {
            if (this.tex != null) {
                this.tex.dispose();
            }
            this.tex = null;
            this.f11949g = null;
            this.input = null;
        }

        public boolean validate(Graphics resg, int tw, int th) {
            int cw;
            int ch;
            boolean create;
            ResourceFactory resourceFactory;
            ResourceFactory resourceFactory2;
            if (this.tex == null) {
                ch = 0;
                cw = 0;
                create = true;
            } else {
                cw = this.tex.getContentWidth();
                ch = this.tex.getContentHeight();
                this.tex.lock();
                create = this.tex.isSurfaceLost() || cw < tw || ch < th;
            }
            if (create) {
                RTTexture oldtex = this.tex;
                if (resg == null) {
                    resourceFactory2 = GraphicsPipeline.getDefaultResourceFactory();
                } else {
                    resourceFactory2 = resg.getResourceFactory();
                }
                ResourceFactory factory = resourceFactory2;
                RTTexture newtex = factory.createRTTexture(tw, th, Texture.WrapMode.CLAMP_TO_ZERO);
                this.tex = newtex;
                this.f11949g = newtex.createGraphics();
                this.input = new EffectInput(newtex);
                if (oldtex != null) {
                    if (this.init_type == InitType.PRESERVE_UPPER_LEFT) {
                        this.f11949g.setCompositeMode(CompositeMode.SRC);
                        if (oldtex.isSurfaceLost()) {
                            if (this.savedPixelData != null) {
                                this.savedPixelData.restore(this.f11949g, cw, ch);
                            }
                        } else {
                            this.f11949g.drawTexture(oldtex, 0.0f, 0.0f, cw, ch);
                        }
                        this.f11949g.setCompositeMode(CompositeMode.SRC_OVER);
                    }
                    oldtex.unlock();
                    oldtex.dispose();
                }
                if (this.init_type == InitType.FILL_WHITE) {
                    this.f11949g.clear(Color.WHITE);
                    return true;
                }
                return true;
            }
            if (this.f11949g == null) {
                this.f11949g = this.tex.createGraphics();
                if (this.f11949g == null) {
                    this.tex.dispose();
                    if (resg == null) {
                        resourceFactory = GraphicsPipeline.getDefaultResourceFactory();
                    } else {
                        resourceFactory = resg.getResourceFactory();
                    }
                    ResourceFactory factory2 = resourceFactory;
                    this.tex = factory2.createRTTexture(tw, th, Texture.WrapMode.CLAMP_TO_ZERO);
                    this.f11949g = this.tex.createGraphics();
                    this.input = new EffectInput(this.tex);
                    if (this.savedPixelData != null) {
                        this.f11949g.setCompositeMode(CompositeMode.SRC);
                        this.savedPixelData.restore(this.f11949g, tw, th);
                        this.f11949g.setCompositeMode(CompositeMode.SRC_OVER);
                        return true;
                    }
                    if (this.init_type == InitType.FILL_WHITE) {
                        this.f11949g.clear(Color.WHITE);
                        return true;
                    }
                    return true;
                }
            }
            if (this.init_type == InitType.CLEAR) {
                this.f11949g.clear();
                return false;
            }
            return false;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void save(int tw, int th) {
            if (this.tex.isVolatile()) {
                if (this.savedPixelData == null) {
                    this.savedPixelData = new PixelData(tw, th);
                }
                this.savedPixelData.save(this.tex);
            }
        }
    }

    /* loaded from: jfxrt.jar:com/sun/javafx/sg/prism/NGCanvas$PixelData.class */
    private static class PixelData {
        private IntBuffer pixels;
        private boolean validPixels;
        private int cw;
        private int ch;

        private PixelData(int cw, int ch) {
            this.pixels = null;
            this.validPixels = false;
            this.cw = cw;
            this.ch = ch;
            this.pixels = IntBuffer.allocate(cw * ch);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void save(RTTexture tex) {
            int tw = tex.getContentWidth();
            int th = tex.getContentHeight();
            if (this.cw < tw || this.ch < th) {
                this.cw = tw;
                this.ch = th;
                this.pixels = IntBuffer.allocate(this.cw * this.ch);
            }
            this.pixels.rewind();
            tex.readPixels(this.pixels);
            this.validPixels = true;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void restore(Graphics g2, int tw, int th) {
            if (this.validPixels) {
                Image img = Image.fromIntArgbPreData(this.pixels, tw, th);
                ResourceFactory factory = g2.getResourceFactory();
                Texture tempTex = factory.createTexture(img, Texture.Usage.DEFAULT, Texture.WrapMode.CLAMP_TO_EDGE);
                g2.drawTexture(tempTex, 0.0f, 0.0f, tw, th);
                tempTex.dispose();
            }
        }
    }

    public NGCanvas() {
        Toolkit tk = Toolkit.getToolkit();
        ScreenConfigurationAccessor screenAccessor = tk.getScreenConfigurationAccessor();
        float hPS = 1.0f;
        for (Object screen : tk.getScreens()) {
            hPS = Math.max(screenAccessor.getRenderScale(screen), hPS);
        }
        this.highestPixelScale = hPS;
        this.cv = new RenderBuf(InitType.PRESERVE_UPPER_LEFT);
        this.temp = new RenderBuf(InitType.CLEAR);
        this.clip = new RenderBuf(InitType.FILL_WHITE);
        this.path = new Path2D();
        this.ngtext = new NGText();
        this.textLayout = new PrismTextLayout();
        this.transform = new Affine2D();
        this.clipStack = new LinkedList<>();
        initAttributes();
    }

    private void initAttributes() {
        this.globalAlpha = 1.0f;
        this.blendmode = Blend.Mode.SRC_OVER;
        this.fillPaint = Color.BLACK;
        this.strokePaint = Color.BLACK;
        this.linewidth = 1.0f;
        this.linecap = 2;
        this.linejoin = 0;
        this.miterlimit = 10.0f;
        this.dashes = null;
        this.dashOffset = 0.0f;
        this.stroke = null;
        this.path.setWindingRule(1);
        this.pgfont = (PGFont) Font.getDefault().impl_getNativeFont();
        this.smoothing = SMOOTH_GRAY;
        this.imageSmoothing = true;
        this.align = 0;
        this.baseline = VPos.BASELINE.ordinal();
        this.transform.setToScale(this.highestPixelScale, this.highestPixelScale);
        this.clipStack.clear();
        resetClip(false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Affine2D getInverseTransform() {
        if (this.inverseTransform == null) {
            this.inverseTransform = new Affine2D();
            this.inversedirty = true;
        }
        if (this.inversedirty) {
            this.inverseTransform.setTransform(this.transform);
            try {
                this.inverseTransform.invert();
            } catch (NoninvertibleTransformException e2) {
                this.inverseTransform.setToScale(0.0d, 0.0d);
            }
            this.inversedirty = false;
        }
        return this.inverseTransform;
    }

    @Override // com.sun.javafx.sg.prism.NGNode
    protected boolean hasOverlappingContents() {
        return true;
    }

    private static void shapebounds(Shape shape, RectBounds bounds, BaseTransform transform) {
        float[] fArr = TEMP_COORDS;
        TEMP_COORDS[1] = Float.POSITIVE_INFINITY;
        fArr[0] = Float.POSITIVE_INFINITY;
        float[] fArr2 = TEMP_COORDS;
        TEMP_COORDS[3] = Float.NEGATIVE_INFINITY;
        fArr2[2] = Float.NEGATIVE_INFINITY;
        Shape.accumulate(TEMP_COORDS, shape, transform);
        bounds.setBounds(TEMP_COORDS[0], TEMP_COORDS[1], TEMP_COORDS[2], TEMP_COORDS[3]);
    }

    private static void strokebounds(BasicStroke stroke, Shape shape, RectBounds bounds, BaseTransform transform) {
        float[] fArr = TEMP_COORDS;
        TEMP_COORDS[1] = Float.POSITIVE_INFINITY;
        fArr[0] = Float.POSITIVE_INFINITY;
        float[] fArr2 = TEMP_COORDS;
        TEMP_COORDS[3] = Float.NEGATIVE_INFINITY;
        fArr2[2] = Float.NEGATIVE_INFINITY;
        stroke.accumulateShapeBounds(TEMP_COORDS, shape, transform);
        bounds.setBounds(TEMP_COORDS[0], TEMP_COORDS[1], TEMP_COORDS[2], TEMP_COORDS[3]);
    }

    private static void runOnRenderThread(Runnable r2) {
        if (Thread.currentThread().getName().startsWith("QuantumRenderer")) {
            r2.run();
            return;
        }
        FutureTask<Void> f2 = new FutureTask<>(r2, null);
        Toolkit.getToolkit().addRenderJob(new RenderJob(f2));
        try {
            f2.get();
        } catch (InterruptedException e2) {
        } catch (ExecutionException ex) {
            throw new AssertionError(ex);
        }
    }

    private boolean printedCanvas(Graphics g2) {
        RTTexture localTex = this.cv.tex;
        if (!(g2 instanceof PrinterGraphics) || localTex == null) {
            return false;
        }
        ResourceFactory factory = g2.getResourceFactory();
        boolean isCompatTex = factory.isCompatibleTexture(localTex);
        if (isCompatTex) {
            return false;
        }
        int tw = localTex.getContentWidth();
        int th = localTex.getContentHeight();
        RTTexture tmpTex = factory.createRTTexture(tw, th, Texture.WrapMode.CLAMP_TO_ZERO);
        Graphics texg = tmpTex.createGraphics();
        texg.setCompositeMode(CompositeMode.SRC);
        if (this.cv.savedPixelData != null) {
            this.cv.savedPixelData.restore(texg, tw, th);
        } else {
            PixelData pd = new PixelData(this.cw, this.ch);
            runOnRenderThread(() -> {
                pd.save(localTex);
                pd.restore(texg, tw, th);
            });
        }
        g2.drawTexture(tmpTex, 0.0f, 0.0f, tw, th);
        tmpTex.unlock();
        tmpTex.dispose();
        return true;
    }

    @Override // com.sun.javafx.sg.prism.NGNode
    protected void renderContent(Graphics g2) {
        if (printedCanvas(g2)) {
            return;
        }
        initCanvas(g2);
        if (this.cv.tex != null) {
            if (this.thebuf != null) {
                renderStream(this.thebuf);
                GrowableDataBuffer.returnBuffer(this.thebuf);
                this.thebuf = null;
            }
            float dw = this.tw / this.highestPixelScale;
            float dh = this.th / this.highestPixelScale;
            g2.drawTexture(this.cv.tex, 0.0f, 0.0f, dw, dh, 0.0f, 0.0f, this.tw, this.th);
            this.cv.save(this.tw, this.th);
        }
        RenderBuf renderBuf = this.temp;
        RenderBuf renderBuf2 = this.clip;
        this.cv.f11949g = null;
        renderBuf2.f11949g = null;
        renderBuf.f11949g = null;
    }

    @Override // com.sun.javafx.sg.prism.NGNode
    public void renderForcedContent(Graphics gOptional) {
        if (this.thebuf != null) {
            initCanvas(gOptional);
            if (this.cv.tex != null) {
                renderStream(this.thebuf);
                GrowableDataBuffer.returnBuffer(this.thebuf);
                this.thebuf = null;
                this.cv.save(this.tw, this.th);
            }
            RenderBuf renderBuf = this.temp;
            RenderBuf renderBuf2 = this.clip;
            this.cv.f11949g = null;
            renderBuf2.f11949g = null;
            renderBuf.f11949g = null;
        }
    }

    private void initCanvas(Graphics g2) {
        if (this.tw <= 0 || this.th <= 0) {
            this.cv.dispose();
        } else if (this.cv.validate(g2, this.tw, this.th)) {
            this.cv.tex.contentsUseful();
            this.cv.tex.makePermanent();
            this.cv.tex.lock();
        }
    }

    private void clearCanvas(int x2, int y2, int w2, int h2) {
        this.cv.f11949g.setCompositeMode(CompositeMode.CLEAR);
        this.cv.f11949g.setTransform(BaseTransform.IDENTITY_TRANSFORM);
        this.cv.f11949g.fillQuad(x2, y2, x2 + w2, y2 + h2);
        this.cv.f11949g.setCompositeMode(CompositeMode.SRC_OVER);
    }

    private void resetClip(boolean andDispose) {
        if (andDispose) {
            this.clip.dispose();
        }
        this.clipsRendered = 0;
        this.clipIsRect = true;
        this.clipRect = null;
    }

    private boolean initClip() {
        boolean clipValidated;
        if (this.clipIsRect) {
            clipValidated = false;
        } else {
            clipValidated = true;
            if (this.clip.validate(this.cv.f11949g, this.tw, this.th)) {
                this.clip.tex.contentsUseful();
                resetClip(false);
            }
        }
        int clipSize = this.clipStack.size();
        while (this.clipsRendered < clipSize) {
            LinkedList<Path2D> linkedList = this.clipStack;
            int i2 = this.clipsRendered;
            this.clipsRendered = i2 + 1;
            Path2D clippath = linkedList.get(i2);
            if (this.clipIsRect) {
                if (clippath.checkAndGetIntRect(TEMP_RECT, CLIPRECT_TOLERANCE)) {
                    if (this.clipRect == null) {
                        this.clipRect = new Rectangle(TEMP_RECT);
                    } else {
                        this.clipRect.intersectWith(TEMP_RECT);
                    }
                } else {
                    this.clipIsRect = false;
                    if (!clipValidated) {
                        clipValidated = true;
                        if (this.clip.validate(this.cv.f11949g, this.tw, this.th)) {
                            this.clip.tex.contentsUseful();
                        }
                    }
                    if (this.clipRect != null) {
                        renderClip(new RoundRectangle2D(this.clipRect.f11913x, this.clipRect.f11914y, this.clipRect.width, this.clipRect.height, 0.0f, 0.0f));
                    }
                }
            }
            shapebounds(clippath, TEMP_RECTBOUNDS, BaseTransform.IDENTITY_TRANSFORM);
            TEMP_RECT.setBounds(TEMP_RECTBOUNDS);
            if (this.clipRect == null) {
                this.clipRect = new Rectangle(TEMP_RECT);
            } else {
                this.clipRect.intersectWith(TEMP_RECT);
            }
            renderClip(clippath);
        }
        if (clipValidated && this.clipIsRect) {
            this.clip.tex.unlock();
        }
        return !this.clipIsRect;
    }

    private void renderClip(Shape clippath) {
        this.temp.validate(this.cv.f11949g, this.tw, this.th);
        this.temp.f11949g.setPaint(Color.WHITE);
        this.temp.f11949g.setTransform(BaseTransform.IDENTITY_TRANSFORM);
        this.temp.f11949g.fill(clippath);
        blendAthruBintoC(this.temp, Blend.Mode.SRC_IN, this.clip, null, CompositeMode.SRC, this.clip);
        this.temp.tex.unlock();
    }

    private Rectangle applyEffectOnAintoC(Effect definput, Effect effect, BaseTransform transform, Rectangle outputClip, CompositeMode comp, RenderBuf destbuf) {
        FilterContext fctx = PrFilterContext.getInstance(destbuf.tex.getAssociatedScreen());
        ImageData id = effect.filter(fctx, transform, outputClip, null, definput);
        Rectangle r2 = id.getUntransformedBounds();
        Texture tex = ((PrTexture) id.getUntransformedImage()).getTextureObject();
        destbuf.f11949g.setTransform(id.getTransform());
        destbuf.f11949g.setCompositeMode(comp);
        destbuf.f11949g.drawTexture(tex, r2.f11913x, r2.f11914y, r2.width, r2.height);
        destbuf.f11949g.setTransform(BaseTransform.IDENTITY_TRANSFORM);
        destbuf.f11949g.setCompositeMode(CompositeMode.SRC_OVER);
        Rectangle resultBounds = id.getTransformedBounds(outputClip);
        id.unref();
        return resultBounds;
    }

    private void blendAthruBintoC(RenderBuf drawbuf, Blend.Mode mode, RenderBuf clipbuf, RectBounds bounds, CompositeMode comp, RenderBuf destbuf) {
        Rectangle blendclip;
        BLENDER.setTopInput(drawbuf.input);
        BLENDER.setBottomInput(clipbuf.input);
        BLENDER.setMode(mode);
        if (bounds != null) {
            blendclip = new Rectangle(bounds);
        } else {
            blendclip = null;
        }
        applyEffectOnAintoC(null, BLENDER, BaseTransform.IDENTITY_TRANSFORM, blendclip, comp, destbuf);
    }

    private void setupFill(Graphics gr) {
        gr.setPaint(this.fillPaint);
    }

    private BasicStroke getStroke() {
        if (this.stroke == null) {
            this.stroke = new BasicStroke(this.linewidth, this.linecap, this.linejoin, this.miterlimit, this.dashes, this.dashOffset);
        }
        return this.stroke;
    }

    private void setupStroke(Graphics gr) {
        gr.setStroke(getStroke());
        gr.setPaint(this.strokePaint);
    }

    private void renderStream(GrowableDataBuffer buf) {
        boolean tempvalidated;
        RenderBuf dest;
        CompositeMode compmode;
        BaseTransform tx;
        while (buf.hasValues()) {
            int token = buf.getByte();
            switch (token) {
                case 0:
                    this.globalAlpha = buf.getFloat();
                    break;
                case 1:
                    this.blendmode = (Blend.Mode) buf.getObject();
                    break;
                case 2:
                    this.fillPaint = (Paint) buf.getObject();
                    break;
                case 3:
                    this.strokePaint = (Paint) buf.getObject();
                    break;
                case 4:
                    this.linewidth = buf.getFloat();
                    this.stroke = null;
                    break;
                case 5:
                    this.linecap = prcaps[buf.getUByte()];
                    this.stroke = null;
                    break;
                case 6:
                    this.linejoin = prjoins[buf.getUByte()];
                    this.stroke = null;
                    break;
                case 7:
                    this.miterlimit = buf.getFloat();
                    this.stroke = null;
                    break;
                case 8:
                    this.pgfont = (PGFont) buf.getObject();
                    break;
                case 9:
                    this.align = buf.getUByte();
                    break;
                case 10:
                    this.baseline = prbases[buf.getUByte()];
                    break;
                case 11:
                    double mxx = buf.getDouble() * this.highestPixelScale;
                    double mxy = buf.getDouble() * this.highestPixelScale;
                    double mxt = buf.getDouble() * this.highestPixelScale;
                    double myx = buf.getDouble() * this.highestPixelScale;
                    double myy = buf.getDouble() * this.highestPixelScale;
                    double myt = buf.getDouble() * this.highestPixelScale;
                    this.transform.setTransform(mxx, myx, mxy, myy, mxt, myt);
                    this.inversedirty = true;
                    break;
                case 12:
                    this.effect = (Effect) buf.getObject();
                    break;
                case 13:
                    Path2D clippath = (Path2D) buf.getObject();
                    if (this.highestPixelScale != 1.0f) {
                        TEMP_TX.setToScale(this.highestPixelScale, this.highestPixelScale);
                        clippath.transform(TEMP_TX);
                    }
                    this.clipStack.addLast(clippath);
                    break;
                case 14:
                    resetClip(true);
                    this.clipStack.removeLast();
                    break;
                case 15:
                    byte type = buf.getByte();
                    switch (type) {
                        case 0:
                            this.arctype = 0;
                            break;
                        case 1:
                            this.arctype = 1;
                            break;
                        case 2:
                            this.arctype = 2;
                            break;
                    }
                case 16:
                    if (buf.getByte() == 0) {
                        this.path.setWindingRule(1);
                        break;
                    } else {
                        this.path.setWindingRule(0);
                        break;
                    }
                case 17:
                    this.dashes = (double[]) buf.getObject();
                    this.stroke = null;
                    break;
                case 18:
                    this.dashOffset = buf.getFloat();
                    this.stroke = null;
                    break;
                case 19:
                    this.smoothing = buf.getUByte();
                    break;
                case 20:
                    this.imageSmoothing = buf.getBoolean();
                    break;
                case 21:
                case 22:
                case 23:
                case 24:
                case 37:
                case 38:
                case 39:
                case 49:
                case 54:
                case 55:
                case 56:
                case 57:
                case 58:
                case 59:
                case 61:
                case 62:
                case 63:
                case 64:
                case 65:
                case 66:
                case 67:
                case 68:
                case 69:
                default:
                    throw new InternalError("Unrecognized PGCanvas token: " + token);
                case 25:
                case 26:
                case 27:
                case 28:
                case 29:
                case 30:
                case 31:
                case 32:
                case 33:
                case 34:
                case 35:
                case 36:
                case 47:
                case 48:
                case 50:
                case 51:
                    boolean clipvalidated = initClip();
                    if (clipvalidated || this.blendmode != Blend.Mode.SRC_OVER) {
                        this.temp.validate(this.cv.f11949g, this.tw, this.th);
                        tempvalidated = true;
                        dest = this.temp;
                    } else {
                        tempvalidated = false;
                        dest = this.cv;
                    }
                    if (this.effect != null) {
                        buf.save();
                        handleRenderOp(token, buf, null, TEMP_RECTBOUNDS);
                        RenderInput ri = new RenderInput(token, buf, this.transform, TEMP_RECTBOUNDS);
                        Rectangle resultBounds = applyEffectOnAintoC(ri, this.effect, this.transform, this.clipRect, CompositeMode.SRC_OVER, dest);
                        if (dest != this.cv) {
                            TEMP_RECTBOUNDS.setBounds(resultBounds.f11913x, resultBounds.f11914y, resultBounds.f11913x + resultBounds.width, resultBounds.f11914y + resultBounds.height);
                        }
                    } else {
                        Graphics g2 = dest.f11949g;
                        g2.setExtraAlpha(this.globalAlpha);
                        g2.setTransform(this.transform);
                        g2.setClipRect(this.clipRect);
                        RectBounds optSaveBounds = dest != this.cv ? TEMP_RECTBOUNDS : null;
                        handleRenderOp(token, buf, g2, optSaveBounds);
                        g2.setClipRect(null);
                    }
                    if (clipvalidated) {
                        if (this.blendmode == Blend.Mode.SRC_OVER) {
                            dest = this.cv;
                            compmode = CompositeMode.SRC_OVER;
                        } else {
                            compmode = CompositeMode.SRC;
                        }
                        if (this.clipRect != null) {
                            TEMP_RECTBOUNDS.intersectWith(this.clipRect);
                        }
                        if (!TEMP_RECTBOUNDS.isEmpty()) {
                            if (dest == this.cv && (this.cv.f11949g instanceof MaskTextureGraphics)) {
                                MaskTextureGraphics mtg = (MaskTextureGraphics) this.cv.f11949g;
                                int dx = (int) Math.floor(TEMP_RECTBOUNDS.getMinX());
                                int dy = (int) Math.floor(TEMP_RECTBOUNDS.getMinY());
                                int dw = ((int) Math.ceil(TEMP_RECTBOUNDS.getMaxX())) - dx;
                                int dh = ((int) Math.ceil(TEMP_RECTBOUNDS.getMaxY())) - dy;
                                mtg.drawPixelsMasked(this.temp.tex, this.clip.tex, dx, dy, dw, dh, dx, dy, dx, dy);
                            } else {
                                blendAthruBintoC(this.temp, Blend.Mode.SRC_IN, this.clip, TEMP_RECTBOUNDS, compmode, dest);
                            }
                        }
                    }
                    if (this.blendmode != Blend.Mode.SRC_OVER) {
                        if (this.clipRect != null) {
                            TEMP_RECTBOUNDS.intersectWith(this.clipRect);
                        }
                        blendAthruBintoC(this.temp, this.blendmode, this.cv, TEMP_RECTBOUNDS, CompositeMode.SRC, this.cv);
                    }
                    if (clipvalidated) {
                        this.clip.tex.unlock();
                    }
                    if (!tempvalidated) {
                        break;
                    } else {
                        this.temp.tex.unlock();
                        break;
                    }
                    break;
                case 40:
                    this.path.reset();
                    break;
                case 41:
                    this.path.moveTo(buf.getFloat(), buf.getFloat());
                    break;
                case 42:
                    this.path.lineTo(buf.getFloat(), buf.getFloat());
                    break;
                case 43:
                    this.path.quadTo(buf.getFloat(), buf.getFloat(), buf.getFloat(), buf.getFloat());
                    break;
                case 44:
                    this.path.curveTo(buf.getFloat(), buf.getFloat(), buf.getFloat(), buf.getFloat(), buf.getFloat(), buf.getFloat());
                    break;
                case 45:
                    this.path.closePath();
                    break;
                case 46:
                    if (this.highestPixelScale == 1.0f) {
                        break;
                    } else {
                        TEMP_TX.setToScale(this.highestPixelScale, this.highestPixelScale);
                        this.path.transform(TEMP_TX);
                        break;
                    }
                case 52:
                    float dx1 = buf.getInt();
                    float dy1 = buf.getInt();
                    int argb = buf.getInt();
                    Graphics gr = this.cv.f11949g;
                    gr.setExtraAlpha(1.0f);
                    gr.setCompositeMode(CompositeMode.SRC);
                    gr.setTransform(BaseTransform.IDENTITY_TRANSFORM);
                    float dx12 = dx1 * this.highestPixelScale;
                    float dy12 = dy1 * this.highestPixelScale;
                    float a2 = (argb >>> 24) / 255.0f;
                    float r2 = ((argb >> 16) & 255) / 255.0f;
                    float g3 = ((argb >> 8) & 255) / 255.0f;
                    float b2 = (argb & 255) / 255.0f;
                    gr.setPaint(new Color(r2, g3, b2, a2));
                    gr.fillQuad(dx12, dy12, dx12 + this.highestPixelScale, dy12 + this.highestPixelScale);
                    gr.setCompositeMode(CompositeMode.SRC_OVER);
                    break;
                case 53:
                    float dx13 = buf.getInt();
                    float dy13 = buf.getInt();
                    int w2 = buf.getInt();
                    int h2 = buf.getInt();
                    byte[] data = (byte[]) buf.getObject();
                    Image img = Image.fromByteBgraPreData(data, w2, h2);
                    Graphics gr2 = this.cv.f11949g;
                    ResourceFactory factory = gr2.getResourceFactory();
                    Texture tex = factory.getCachedTexture(img, Texture.WrapMode.CLAMP_TO_EDGE);
                    gr2.setTransform(BaseTransform.IDENTITY_TRANSFORM);
                    gr2.setCompositeMode(CompositeMode.SRC);
                    float dx2 = dx13 + w2;
                    float dy2 = dy13 + h2;
                    gr2.drawTexture(tex, dx13 * this.highestPixelScale, dy13 * this.highestPixelScale, dx2 * this.highestPixelScale, dy2 * this.highestPixelScale, 0.0f, 0.0f, w2, h2);
                    tex.contentsNotUseful();
                    tex.unlock();
                    gr2.setCompositeMode(CompositeMode.SRC_OVER);
                    break;
                case 60:
                    Effect e2 = (Effect) buf.getObject();
                    RenderBuf dest2 = this.clipStack.isEmpty() ? this.cv : this.temp;
                    if (this.highestPixelScale != 1.0f) {
                        TEMP_TX.setToScale(this.highestPixelScale, this.highestPixelScale);
                        tx = TEMP_TX;
                        this.cv.input.setPixelScale(this.highestPixelScale);
                    } else {
                        tx = BaseTransform.IDENTITY_TRANSFORM;
                    }
                    applyEffectOnAintoC(this.cv.input, e2, tx, null, CompositeMode.SRC, dest2);
                    this.cv.input.setPixelScale(1.0f);
                    if (dest2 == this.cv) {
                        break;
                    } else {
                        blendAthruBintoC(dest2, Blend.Mode.SRC_IN, this.clip, null, CompositeMode.SRC, this.cv);
                        break;
                    }
                case 70:
                    initAttributes();
                    this.cw = this.tw;
                    this.ch = this.th;
                    clearCanvas(0, 0, this.tw, this.th);
                    break;
                case 71:
                    int neww = (int) Math.ceil(buf.getFloat() * this.highestPixelScale);
                    int newh = (int) Math.ceil(buf.getFloat() * this.highestPixelScale);
                    int clearx = Math.min(neww, this.cw);
                    int cleary = Math.min(newh, this.ch);
                    if (clearx < this.tw) {
                        clearCanvas(clearx, 0, this.tw - clearx, this.th);
                    }
                    if (cleary < this.th) {
                        clearCanvas(0, cleary, this.tw, this.th - cleary);
                    }
                    this.cw = neww;
                    this.ch = newh;
                    break;
            }
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:128:0x0688  */
    /* JADX WARN: Removed duplicated region for block: B:140:? A[RETURN, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public void handleRenderOp(int r13, com.sun.javafx.sg.prism.GrowableDataBuffer r14, com.sun.prism.Graphics r15, com.sun.javafx.geom.RectBounds r16) {
        /*
            Method dump skipped, instructions count: 1737
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.sun.javafx.sg.prism.NGCanvas.handleRenderOp(int, com.sun.javafx.sg.prism.GrowableDataBuffer, com.sun.prism.Graphics, com.sun.javafx.geom.RectBounds):void");
    }

    void computeTextLayoutBounds(RectBounds bounds, BaseTransform transform, float scaleX, float layoutX, float layoutY, int token) {
        this.textLayout.getBounds(null, bounds);
        TEMP_TX.setTransform(transform);
        TEMP_TX.scale(scaleX, 1.0d);
        TEMP_TX.translate(layoutX, layoutY);
        TEMP_TX.transform(bounds, bounds);
        if (token == 36) {
            Shape textShape = this.textLayout.getShape(1, null);
            RectBounds shapeBounds = new RectBounds();
            strokebounds(getStroke(), textShape, shapeBounds, TEMP_TX);
            bounds.unionWith(shapeBounds);
        }
    }

    static void txBounds(RectBounds bounds, BaseTransform transform) {
        switch (transform.getType()) {
            case 0:
                break;
            case 1:
                float tx = (float) transform.getMxt();
                float ty = (float) transform.getMyt();
                bounds.setBounds(bounds.getMinX() + tx, bounds.getMinY() + ty, bounds.getMaxX() + tx, bounds.getMaxY() + ty);
                break;
            default:
                BaseBounds txbounds = transform.transform(bounds, bounds);
                if (txbounds != bounds) {
                    bounds.setBounds(txbounds.getMinX(), txbounds.getMinY(), txbounds.getMaxX(), txbounds.getMaxY());
                    break;
                }
                break;
        }
    }

    static void inverseTxBounds(RectBounds bounds, BaseTransform transform) {
        switch (transform.getType()) {
            case 0:
                break;
            case 1:
                float tx = (float) transform.getMxt();
                float ty = (float) transform.getMyt();
                bounds.setBounds(bounds.getMinX() - tx, bounds.getMinY() - ty, bounds.getMaxX() - tx, bounds.getMaxY() - ty);
                break;
            default:
                try {
                    BaseBounds txbounds = transform.inverseTransform(bounds, bounds);
                    if (txbounds != bounds) {
                        bounds.setBounds(txbounds.getMinX(), txbounds.getMinY(), txbounds.getMaxX(), txbounds.getMaxY());
                    }
                    break;
                } catch (NoninvertibleTransformException e2) {
                    bounds.makeEmpty();
                }
        }
    }

    public void updateBounds(float w2, float h2) {
        this.tw = (int) Math.ceil(w2 * this.highestPixelScale);
        this.th = (int) Math.ceil(h2 * this.highestPixelScale);
        geometryChanged();
    }

    public boolean updateRendering(GrowableDataBuffer buf) {
        GrowableDataBuffer retbuf;
        if (buf.isEmpty()) {
            GrowableDataBuffer.returnBuffer(buf);
            return this.thebuf != null;
        }
        boolean reset = buf.peekByte(0) == 70;
        if (reset || this.thebuf == null) {
            retbuf = this.thebuf;
            this.thebuf = buf;
        } else {
            this.thebuf.append(buf);
            retbuf = buf;
        }
        geometryChanged();
        if (retbuf != null) {
            GrowableDataBuffer.returnBuffer(retbuf);
            return true;
        }
        return false;
    }

    /* loaded from: jfxrt.jar:com/sun/javafx/sg/prism/NGCanvas$RenderInput.class */
    class RenderInput extends Effect {

        /* renamed from: x, reason: collision with root package name */
        float f11950x;

        /* renamed from: y, reason: collision with root package name */
        float f11951y;

        /* renamed from: w, reason: collision with root package name */
        float f11952w;

        /* renamed from: h, reason: collision with root package name */
        float f11953h;
        int token;
        GrowableDataBuffer buf;
        Affine2D savedBoundsTx = new Affine2D();

        public RenderInput(int token, GrowableDataBuffer buf, BaseTransform boundsTx, RectBounds rb) {
            this.token = token;
            this.buf = buf;
            this.savedBoundsTx.setTransform(boundsTx);
            this.f11950x = rb.getMinX();
            this.f11951y = rb.getMinY();
            this.f11952w = rb.getWidth();
            this.f11953h = rb.getHeight();
        }

        @Override // com.sun.scenario.effect.Effect
        public ImageData filter(FilterContext fctx, BaseTransform transform, Rectangle outputClip, Object renderHelper, Effect defaultInput) {
            BaseBounds bounds = getBounds(transform, defaultInput);
            if (outputClip != null) {
                bounds.intersectWith(outputClip);
            }
            Rectangle r2 = new Rectangle(bounds);
            if (r2.width < 1) {
                r2.width = 1;
            }
            if (r2.height < 1) {
                r2.height = 1;
            }
            PrDrawable ret = (PrDrawable) Effect.getCompatibleImage(fctx, r2.width, r2.height);
            if (ret != null) {
                Graphics g2 = ret.createGraphics();
                g2.setExtraAlpha(NGCanvas.this.globalAlpha);
                g2.translate(-r2.f11913x, -r2.f11914y);
                if (transform != null) {
                    g2.transform(transform);
                }
                this.buf.restore();
                NGCanvas.this.handleRenderOp(this.token, this.buf, g2, null);
            }
            return new ImageData(fctx, ret, r2);
        }

        @Override // com.sun.scenario.effect.Effect
        public Effect.AccelType getAccelType(FilterContext fctx) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override // com.sun.scenario.effect.Effect
        public BaseBounds getBounds(BaseTransform transform, Effect defaultInput) {
            RectBounds ret = new RectBounds(this.f11950x, this.f11951y, this.f11950x + this.f11952w, this.f11951y + this.f11953h);
            if (!transform.equals(this.savedBoundsTx)) {
                NGCanvas.inverseTxBounds(ret, this.savedBoundsTx);
                NGCanvas.txBounds(ret, transform);
            }
            return ret;
        }

        @Override // com.sun.scenario.effect.Effect
        public boolean reducesOpaquePixels() {
            return false;
        }

        @Override // com.sun.scenario.effect.Effect
        public DirtyRegionContainer getDirtyRegions(Effect defaultInput, DirtyRegionPool regionPool) {
            return null;
        }
    }

    /* loaded from: jfxrt.jar:com/sun/javafx/sg/prism/NGCanvas$MyBlend.class */
    static class MyBlend extends Blend {
        public MyBlend(Blend.Mode mode, Effect bottomInput, Effect topInput) {
            super(mode, bottomInput, topInput);
        }

        @Override // com.sun.scenario.effect.Effect
        public Rectangle getResultBounds(BaseTransform transform, Rectangle outputClip, ImageData... inputDatas) {
            Rectangle r2 = super.getResultBounds(transform, outputClip, inputDatas);
            r2.intersectWith(outputClip);
            return r2;
        }
    }

    /* loaded from: jfxrt.jar:com/sun/javafx/sg/prism/NGCanvas$EffectInput.class */
    static class EffectInput extends Effect {
        RTTexture tex;
        float pixelscale = 1.0f;

        EffectInput(RTTexture tex) {
            this.tex = tex;
        }

        public void setPixelScale(float scale) {
            this.pixelscale = scale;
        }

        @Override // com.sun.scenario.effect.Effect
        public ImageData filter(FilterContext fctx, BaseTransform transform, Rectangle outputClip, Object renderHelper, Effect defaultInput) {
            Filterable f2 = PrDrawable.create(fctx, this.tex);
            Rectangle r2 = new Rectangle(this.tex.getContentWidth(), this.tex.getContentHeight());
            f2.lock();
            ImageData id = new ImageData(fctx, f2, r2);
            id.setReusable(true);
            if (this.pixelscale != 1.0f || !transform.isIdentity()) {
                Affine2D a2d = new Affine2D();
                a2d.scale(1.0f / this.pixelscale, 1.0f / this.pixelscale);
                a2d.concatenate(transform);
                id = id.transform(a2d);
            }
            return id;
        }

        @Override // com.sun.scenario.effect.Effect
        public Effect.AccelType getAccelType(FilterContext fctx) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override // com.sun.scenario.effect.Effect
        public BaseBounds getBounds(BaseTransform transform, Effect defaultInput) {
            Rectangle r2 = new Rectangle(this.tex.getContentWidth(), this.tex.getContentHeight());
            return transformBounds(transform, new RectBounds(r2));
        }

        @Override // com.sun.scenario.effect.Effect
        public boolean reducesOpaquePixels() {
            return false;
        }

        @Override // com.sun.scenario.effect.Effect
        public DirtyRegionContainer getDirtyRegions(Effect defaultInput, DirtyRegionPool regionPool) {
            return null;
        }
    }
}
