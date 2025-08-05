package javafx.scene.canvas;

import com.sun.javafx.geom.Arc2D;
import com.sun.javafx.geom.IllegalPathStateException;
import com.sun.javafx.geom.Path2D;
import com.sun.javafx.geom.PathIterator;
import com.sun.javafx.geom.transform.Affine2D;
import com.sun.javafx.geom.transform.NoninvertibleTransformException;
import com.sun.javafx.image.BytePixelGetter;
import com.sun.javafx.image.BytePixelSetter;
import com.sun.javafx.image.ByteToBytePixelConverter;
import com.sun.javafx.image.IntPixelGetter;
import com.sun.javafx.image.IntToBytePixelConverter;
import com.sun.javafx.image.PixelConverter;
import com.sun.javafx.image.PixelGetter;
import com.sun.javafx.image.PixelUtils;
import com.sun.javafx.image.impl.ByteBgraPre;
import com.sun.javafx.sg.prism.GrowableDataBuffer;
import com.sun.javafx.tk.Toolkit;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.Arrays;
import java.util.LinkedList;
import javafx.geometry.NodeOrientation;
import javafx.geometry.VPos;
import javafx.scene.effect.Blend;
import javafx.scene.effect.BlendMode;
import javafx.scene.effect.Effect;
import javafx.scene.image.Image;
import javafx.scene.image.PixelFormat;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.ArcType;
import javafx.scene.shape.FillRule;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.shape.StrokeLineJoin;
import javafx.scene.text.Font;
import javafx.scene.text.FontSmoothingType;
import javafx.scene.text.TextAlignment;
import javafx.scene.transform.Affine;

/* loaded from: jfxrt.jar:javafx/scene/canvas/GraphicsContext.class */
public final class GraphicsContext {
    Canvas theCanvas;
    private static final byte[] pgtype = {41, 42, 43, 44, 45};
    private static final int[] numsegs = {2, 2, 4, 6, 0};
    private boolean txdirty;
    private PixelWriter writer;
    private float[] coords = new float[6];
    private float[] polybuf = new float[512];
    Path2D path = new Path2D();
    boolean pathDirty = true;
    State curState = new State();
    LinkedList<State> stateStack = new LinkedList<>();
    LinkedList<Path2D> clipStack = new LinkedList<>();

    GraphicsContext(Canvas theCanvas) {
        this.theCanvas = theCanvas;
    }

    /* loaded from: jfxrt.jar:javafx/scene/canvas/GraphicsContext$State.class */
    static class State {
        double globalAlpha;
        BlendMode blendop;
        Affine2D transform;
        Paint fill;
        Paint stroke;
        double linewidth;
        StrokeLineCap linecap;
        StrokeLineJoin linejoin;
        double miterlimit;
        double[] dashes;
        double dashOffset;
        int numClipPaths;
        Font font;
        FontSmoothingType fontsmoothing;
        TextAlignment textalign;
        VPos textbaseline;
        Effect effect;
        FillRule fillRule;
        boolean imageSmoothing = true;

        State() {
            init();
        }

        final void init() {
            set(1.0d, BlendMode.SRC_OVER, new Affine2D(), Color.BLACK, Color.BLACK, 1.0d, StrokeLineCap.SQUARE, StrokeLineJoin.MITER, 10.0d, null, 0.0d, 0, Font.getDefault(), FontSmoothingType.GRAY, TextAlignment.LEFT, VPos.BASELINE, null, FillRule.NON_ZERO, true);
        }

        State(State copy) {
            set(copy.globalAlpha, copy.blendop, new Affine2D(copy.transform), copy.fill, copy.stroke, copy.linewidth, copy.linecap, copy.linejoin, copy.miterlimit, copy.dashes, copy.dashOffset, copy.numClipPaths, copy.font, copy.fontsmoothing, copy.textalign, copy.textbaseline, copy.effect, copy.fillRule, copy.imageSmoothing);
        }

        final void set(double globalAlpha, BlendMode blendop, Affine2D transform, Paint fill, Paint stroke, double linewidth, StrokeLineCap linecap, StrokeLineJoin linejoin, double miterlimit, double[] dashes, double dashOffset, int numClipPaths, Font font, FontSmoothingType smoothing, TextAlignment align, VPos baseline, Effect effect, FillRule fillRule, boolean imageSmoothing) {
            this.globalAlpha = globalAlpha;
            this.blendop = blendop;
            this.transform = transform;
            this.fill = fill;
            this.stroke = stroke;
            this.linewidth = linewidth;
            this.linecap = linecap;
            this.linejoin = linejoin;
            this.miterlimit = miterlimit;
            this.dashes = dashes;
            this.dashOffset = dashOffset;
            this.numClipPaths = numClipPaths;
            this.font = font;
            this.fontsmoothing = smoothing;
            this.textalign = align;
            this.textbaseline = baseline;
            this.effect = effect;
            this.fillRule = fillRule;
            this.imageSmoothing = imageSmoothing;
        }

        State copy() {
            return new State(this);
        }

        void restore(GraphicsContext ctx) {
            ctx.setGlobalAlpha(this.globalAlpha);
            ctx.setGlobalBlendMode(this.blendop);
            ctx.setTransform(this.transform.getMxx(), this.transform.getMyx(), this.transform.getMxy(), this.transform.getMyy(), this.transform.getMxt(), this.transform.getMyt());
            ctx.setFill(this.fill);
            ctx.setStroke(this.stroke);
            ctx.setLineWidth(this.linewidth);
            ctx.setLineCap(this.linecap);
            ctx.setLineJoin(this.linejoin);
            ctx.setMiterLimit(this.miterlimit);
            ctx.setLineDashes(this.dashes);
            ctx.setLineDashOffset(this.dashOffset);
            GrowableDataBuffer buf = ctx.getBuffer();
            while (ctx.curState.numClipPaths > this.numClipPaths) {
                ctx.curState.numClipPaths--;
                ctx.clipStack.removeLast();
                buf.putByte((byte) 14);
            }
            ctx.setFillRule(this.fillRule);
            ctx.setFont(this.font);
            ctx.setFontSmoothingType(this.fontsmoothing);
            ctx.setTextAlign(this.textalign);
            ctx.setTextBaseline(this.textbaseline);
            ctx.setEffect(this.effect);
            ctx.setImageSmoothing(this.imageSmoothing);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public GrowableDataBuffer getBuffer() {
        return this.theCanvas.getBuffer();
    }

    private void markPathDirty() {
        this.pathDirty = true;
    }

    private void writePath(byte command) {
        updateTransform();
        GrowableDataBuffer buf = getBuffer();
        if (this.pathDirty) {
            buf.putByte((byte) 40);
            PathIterator pi = this.path.getPathIterator(null);
            while (!pi.isDone()) {
                int pitype = pi.currentSegment(this.coords);
                buf.putByte(pgtype[pitype]);
                for (int i2 = 0; i2 < numsegs[pitype]; i2++) {
                    buf.putFloat(this.coords[i2]);
                }
                pi.next();
            }
            buf.putByte((byte) 46);
            this.pathDirty = false;
        }
        buf.putByte(command);
    }

    private void writePaint(Paint p2, byte command) {
        GrowableDataBuffer buf = getBuffer();
        buf.putByte(command);
        buf.putObject(Toolkit.getPaintAccessor().getPlatformPaint(p2));
    }

    private void writeArcType(ArcType closure) {
        byte type;
        switch (closure) {
            case OPEN:
                type = 0;
                break;
            case CHORD:
                type = 1;
                break;
            case ROUND:
                type = 2;
                break;
            default:
                return;
        }
        writeParam(type, (byte) 15);
    }

    private void writeRectParams(GrowableDataBuffer buf, double x2, double y2, double w2, double h2, byte command) {
        buf.putByte(command);
        buf.putFloat((float) x2);
        buf.putFloat((float) y2);
        buf.putFloat((float) w2);
        buf.putFloat((float) h2);
    }

    private void writeOp4(double x2, double y2, double w2, double h2, byte command) {
        updateTransform();
        writeRectParams(getBuffer(), x2, y2, w2, h2, command);
    }

    private void writeOp6(double x2, double y2, double w2, double h2, double v1, double v2, byte command) {
        updateTransform();
        GrowableDataBuffer buf = getBuffer();
        buf.putByte(command);
        buf.putFloat((float) x2);
        buf.putFloat((float) y2);
        buf.putFloat((float) w2);
        buf.putFloat((float) h2);
        buf.putFloat((float) v1);
        buf.putFloat((float) v2);
    }

    private void flushPolyBuf(GrowableDataBuffer buf, float[] polybuf, int n2, byte command) {
        this.curState.transform.transform(polybuf, 0, polybuf, 0, n2 / 2);
        for (int i2 = 0; i2 < n2; i2 += 2) {
            buf.putByte(command);
            buf.putFloat(polybuf[i2]);
            buf.putFloat(polybuf[i2 + 1]);
            command = 42;
        }
    }

    private void writePoly(double[] xPoints, double[] yPoints, int nPoints, boolean close, byte command) {
        if (xPoints == null || yPoints == null) {
            return;
        }
        GrowableDataBuffer buf = getBuffer();
        buf.putByte((byte) 40);
        int pos = 0;
        byte polycmd = 41;
        for (int i2 = 0; i2 < nPoints; i2++) {
            if (pos >= this.polybuf.length) {
                flushPolyBuf(buf, this.polybuf, pos, polycmd);
                pos = 0;
                polycmd = 42;
            }
            int i3 = pos;
            int pos2 = pos + 1;
            this.polybuf[i3] = (float) xPoints[i2];
            pos = pos2 + 1;
            this.polybuf[pos2] = (float) yPoints[i2];
        }
        flushPolyBuf(buf, this.polybuf, pos, polycmd);
        if (close) {
            buf.putByte((byte) 45);
        }
        buf.putByte((byte) 46);
        updateTransform();
        buf.putByte(command);
        markPathDirty();
    }

    private void writeImage(Image img, double dx, double dy, double dw, double dh) {
        Object platformImg;
        if (img == null || img.getProgress() < 1.0d || (platformImg = img.impl_getPlatformImage()) == null) {
            return;
        }
        updateTransform();
        GrowableDataBuffer buf = getBuffer();
        writeRectParams(buf, dx, dy, dw, dh, (byte) 50);
        buf.putObject(platformImg);
    }

    private void writeImage(Image img, double dx, double dy, double dw, double dh, double sx, double sy, double sw, double sh) {
        Object platformImg;
        if (img == null || img.getProgress() < 1.0d || (platformImg = img.impl_getPlatformImage()) == null) {
            return;
        }
        updateTransform();
        GrowableDataBuffer buf = getBuffer();
        writeRectParams(buf, dx, dy, dw, dh, (byte) 51);
        buf.putFloat((float) sx);
        buf.putFloat((float) sy);
        buf.putFloat((float) sw);
        buf.putFloat((float) sh);
        buf.putObject(platformImg);
    }

    private void writeText(String text, double x2, double y2, double maxWidth, byte command) {
        if (text == null) {
            return;
        }
        updateTransform();
        GrowableDataBuffer buf = getBuffer();
        buf.putByte(command);
        buf.putFloat((float) x2);
        buf.putFloat((float) y2);
        buf.putFloat((float) maxWidth);
        buf.putBoolean(this.theCanvas.getEffectiveNodeOrientation() == NodeOrientation.RIGHT_TO_LEFT);
        buf.putObject(text);
    }

    void writeParam(double v2, byte command) {
        GrowableDataBuffer buf = getBuffer();
        buf.putByte(command);
        buf.putFloat((float) v2);
    }

    private void writeParam(byte v2, byte command) {
        GrowableDataBuffer buf = getBuffer();
        buf.putByte(command);
        buf.putByte(v2);
    }

    private void updateTransform() {
        if (this.txdirty) {
            this.txdirty = false;
            GrowableDataBuffer buf = getBuffer();
            buf.putByte((byte) 11);
            buf.putDouble(this.curState.transform.getMxx());
            buf.putDouble(this.curState.transform.getMxy());
            buf.putDouble(this.curState.transform.getMxt());
            buf.putDouble(this.curState.transform.getMyx());
            buf.putDouble(this.curState.transform.getMyy());
            buf.putDouble(this.curState.transform.getMyt());
        }
    }

    void updateDimensions() {
        GrowableDataBuffer buf = getBuffer();
        buf.putByte((byte) 71);
        buf.putFloat((float) this.theCanvas.getWidth());
        buf.putFloat((float) this.theCanvas.getHeight());
    }

    private void reset() {
        GrowableDataBuffer buf = getBuffer();
        if (buf.writeValuePosition() > 1024 || this.theCanvas.isRendererFallingBehind()) {
            buf.reset();
            buf.putByte((byte) 70);
            updateDimensions();
            this.txdirty = true;
            this.pathDirty = true;
            State s2 = this.curState;
            int numClipPaths = this.curState.numClipPaths;
            this.curState = new State();
            for (int i2 = 0; i2 < numClipPaths; i2++) {
                Path2D clip = this.clipStack.get(i2);
                buf.putByte((byte) 13);
                buf.putObject(clip);
            }
            this.curState.numClipPaths = numClipPaths;
            s2.restore(this);
        }
    }

    private void resetIfCovers(Paint p2, double x2, double y2, double w2, double h2) {
        Affine2D tx = this.curState.transform;
        if (tx.isTranslateOrIdentity()) {
            double x3 = x2 + tx.getMxt();
            double y3 = y2 + tx.getMyt();
            if (x3 > 0.0d || y3 > 0.0d || x3 + w2 < this.theCanvas.getWidth() || y3 + h2 < this.theCanvas.getHeight()) {
                return;
            }
            if ((p2 == null || (this.curState.blendop == BlendMode.SRC_OVER && p2.isOpaque() && this.curState.globalAlpha >= 1.0d)) && this.curState.numClipPaths <= 0 && this.curState.effect == null) {
                reset();
            }
        }
    }

    public Canvas getCanvas() {
        return this.theCanvas;
    }

    public void save() {
        this.stateStack.push(this.curState.copy());
    }

    public void restore() {
        if (!this.stateStack.isEmpty()) {
            State savedState = this.stateStack.pop();
            savedState.restore(this);
            this.txdirty = true;
        }
    }

    public void translate(double x2, double y2) {
        this.curState.transform.translate(x2, y2);
        this.txdirty = true;
    }

    public void scale(double x2, double y2) {
        this.curState.transform.scale(x2, y2);
        this.txdirty = true;
    }

    public void rotate(double degrees) {
        this.curState.transform.rotate(Math.toRadians(degrees));
        this.txdirty = true;
    }

    public void transform(double mxx, double myx, double mxy, double myy, double mxt, double myt) {
        this.curState.transform.concatenate(mxx, mxy, mxt, myx, myy, myt);
        this.txdirty = true;
    }

    public void transform(Affine xform) {
        if (xform == null) {
            return;
        }
        this.curState.transform.concatenate(xform.getMxx(), xform.getMxy(), xform.getTx(), xform.getMyx(), xform.getMyy(), xform.getTy());
        this.txdirty = true;
    }

    public void setTransform(double mxx, double myx, double mxy, double myy, double mxt, double myt) {
        this.curState.transform.setTransform(mxx, myx, mxy, myy, mxt, myt);
        this.txdirty = true;
    }

    public void setTransform(Affine xform) {
        this.curState.transform.setTransform(xform.getMxx(), xform.getMyx(), xform.getMxy(), xform.getMyy(), xform.getTx(), xform.getTy());
        this.txdirty = true;
    }

    public Affine getTransform(Affine xform) {
        if (xform == null) {
            xform = new Affine();
        }
        xform.setMxx(this.curState.transform.getMxx());
        xform.setMxy(this.curState.transform.getMxy());
        xform.setMxz(0.0d);
        xform.setTx(this.curState.transform.getMxt());
        xform.setMyx(this.curState.transform.getMyx());
        xform.setMyy(this.curState.transform.getMyy());
        xform.setMyz(0.0d);
        xform.setTy(this.curState.transform.getMyt());
        xform.setMzx(0.0d);
        xform.setMzy(0.0d);
        xform.setMzz(1.0d);
        xform.setTz(0.0d);
        return xform;
    }

    public Affine getTransform() {
        return getTransform(null);
    }

    public void setGlobalAlpha(double alpha) {
        if (this.curState.globalAlpha != alpha) {
            this.curState.globalAlpha = alpha;
            writeParam(alpha > 1.0d ? 1.0d : alpha < 0.0d ? 0.0d : alpha, (byte) 0);
        }
    }

    public double getGlobalAlpha() {
        return this.curState.globalAlpha;
    }

    public void setGlobalBlendMode(BlendMode op) {
        if (op != null && op != this.curState.blendop) {
            GrowableDataBuffer buf = getBuffer();
            this.curState.blendop = op;
            buf.putByte((byte) 1);
            buf.putObject(Blend.impl_getToolkitMode(op));
        }
    }

    public BlendMode getGlobalBlendMode() {
        return this.curState.blendop;
    }

    public void setFill(Paint p2) {
        if (p2 != null && this.curState.fill != p2) {
            this.curState.fill = p2;
            writePaint(p2, (byte) 2);
        }
    }

    public Paint getFill() {
        return this.curState.fill;
    }

    public void setStroke(Paint p2) {
        if (p2 != null && this.curState.stroke != p2) {
            this.curState.stroke = p2;
            writePaint(p2, (byte) 3);
        }
    }

    public Paint getStroke() {
        return this.curState.stroke;
    }

    public void setLineWidth(double lw) {
        if (lw > 0.0d && lw < Double.POSITIVE_INFINITY && this.curState.linewidth != lw) {
            this.curState.linewidth = lw;
            writeParam(lw, (byte) 4);
        }
    }

    public double getLineWidth() {
        return this.curState.linewidth;
    }

    public void setLineCap(StrokeLineCap cap) {
        byte v2;
        if (cap != null && this.curState.linecap != cap) {
            switch (cap) {
                case BUTT:
                    v2 = 0;
                    break;
                case ROUND:
                    v2 = 1;
                    break;
                case SQUARE:
                    v2 = 2;
                    break;
                default:
                    return;
            }
            this.curState.linecap = cap;
            writeParam(v2, (byte) 5);
        }
    }

    public StrokeLineCap getLineCap() {
        return this.curState.linecap;
    }

    public void setLineJoin(StrokeLineJoin join) {
        byte v2;
        if (join != null && this.curState.linejoin != join) {
            switch (join) {
                case MITER:
                    v2 = 0;
                    break;
                case BEVEL:
                    v2 = 2;
                    break;
                case ROUND:
                    v2 = 1;
                    break;
                default:
                    return;
            }
            this.curState.linejoin = join;
            writeParam(v2, (byte) 6);
        }
    }

    public StrokeLineJoin getLineJoin() {
        return this.curState.linejoin;
    }

    public void setMiterLimit(double ml) {
        if (ml > 0.0d && ml < Double.POSITIVE_INFINITY && this.curState.miterlimit != ml) {
            this.curState.miterlimit = ml;
            writeParam(ml, (byte) 7);
        }
    }

    public double getMiterLimit() {
        return this.curState.miterlimit;
    }

    public void setLineDashes(double... dashes) {
        if (dashes == null || dashes.length == 0) {
            if (this.curState.dashes == null) {
                return;
            } else {
                this.curState.dashes = null;
            }
        } else {
            boolean allZeros = true;
            for (double d2 : dashes) {
                if (d2 >= 0.0d && d2 < Double.POSITIVE_INFINITY) {
                    if (d2 > 0.0d) {
                        allZeros = false;
                    }
                } else {
                    return;
                }
            }
            if (allZeros) {
                if (this.curState.dashes == null) {
                    return;
                } else {
                    this.curState.dashes = null;
                }
            } else {
                int dashlen = dashes.length;
                if ((dashlen & 1) == 0) {
                    this.curState.dashes = Arrays.copyOf(dashes, dashlen);
                } else {
                    this.curState.dashes = Arrays.copyOf(dashes, dashlen * 2);
                    System.arraycopy(dashes, 0, this.curState.dashes, dashlen, dashlen);
                }
            }
        }
        GrowableDataBuffer buf = getBuffer();
        buf.putByte((byte) 17);
        buf.putObject(this.curState.dashes);
    }

    public double[] getLineDashes() {
        if (this.curState.dashes == null) {
            return null;
        }
        return Arrays.copyOf(this.curState.dashes, this.curState.dashes.length);
    }

    public void setLineDashOffset(double dashOffset) {
        if (dashOffset > Double.NEGATIVE_INFINITY && dashOffset < Double.POSITIVE_INFINITY) {
            this.curState.dashOffset = dashOffset;
            writeParam(dashOffset, (byte) 18);
        }
    }

    public double getLineDashOffset() {
        return this.curState.dashOffset;
    }

    public void setFont(Font f2) {
        if (f2 != null && this.curState.font != f2) {
            this.curState.font = f2;
            GrowableDataBuffer buf = getBuffer();
            buf.putByte((byte) 8);
            buf.putObject(f2.impl_getNativeFont());
        }
    }

    public Font getFont() {
        return this.curState.font;
    }

    public void setFontSmoothingType(FontSmoothingType fontsmoothing) {
        if (fontsmoothing != null && fontsmoothing != this.curState.fontsmoothing) {
            this.curState.fontsmoothing = fontsmoothing;
            writeParam((byte) fontsmoothing.ordinal(), (byte) 19);
        }
    }

    public FontSmoothingType getFontSmoothingType() {
        return this.curState.fontsmoothing;
    }

    public void setTextAlign(TextAlignment align) {
        byte a2;
        if (align != null && this.curState.textalign != align) {
            switch (align) {
                case LEFT:
                    a2 = 0;
                    break;
                case CENTER:
                    a2 = 1;
                    break;
                case RIGHT:
                    a2 = 2;
                    break;
                case JUSTIFY:
                    a2 = 3;
                    break;
                default:
                    return;
            }
            this.curState.textalign = align;
            writeParam(a2, (byte) 9);
        }
    }

    public TextAlignment getTextAlign() {
        return this.curState.textalign;
    }

    public void setTextBaseline(VPos baseline) {
        byte b2;
        if (baseline != null && this.curState.textbaseline != baseline) {
            switch (baseline) {
                case TOP:
                    b2 = 0;
                    break;
                case CENTER:
                    b2 = 1;
                    break;
                case BASELINE:
                    b2 = 2;
                    break;
                case BOTTOM:
                    b2 = 3;
                    break;
                default:
                    return;
            }
            this.curState.textbaseline = baseline;
            writeParam(b2, (byte) 10);
        }
    }

    public VPos getTextBaseline() {
        return this.curState.textbaseline;
    }

    public void fillText(String text, double x2, double y2) {
        writeText(text, x2, y2, 0.0d, (byte) 35);
    }

    public void strokeText(String text, double x2, double y2) {
        writeText(text, x2, y2, 0.0d, (byte) 36);
    }

    public void fillText(String text, double x2, double y2, double maxWidth) {
        if (maxWidth <= 0.0d) {
            return;
        }
        writeText(text, x2, y2, maxWidth, (byte) 35);
    }

    public void strokeText(String text, double x2, double y2, double maxWidth) {
        if (maxWidth <= 0.0d) {
            return;
        }
        writeText(text, x2, y2, maxWidth, (byte) 36);
    }

    public void setFillRule(FillRule fillRule) {
        byte b2;
        if (fillRule != null && this.curState.fillRule != fillRule) {
            if (fillRule == FillRule.EVEN_ODD) {
                b2 = 1;
            } else {
                b2 = 0;
            }
            this.curState.fillRule = fillRule;
            writeParam(b2, (byte) 16);
        }
    }

    public FillRule getFillRule() {
        return this.curState.fillRule;
    }

    public void setImageSmoothing(boolean imageSmoothing) {
        if (this.curState.imageSmoothing != imageSmoothing) {
            this.curState.imageSmoothing = imageSmoothing;
            GrowableDataBuffer buf = getBuffer();
            buf.putByte((byte) 20);
            buf.putBoolean(this.curState.imageSmoothing);
        }
    }

    public boolean isImageSmoothing() {
        return this.curState.imageSmoothing;
    }

    public void beginPath() {
        this.path.reset();
        markPathDirty();
    }

    public void moveTo(double x0, double y0) {
        this.coords[0] = (float) x0;
        this.coords[1] = (float) y0;
        this.curState.transform.transform(this.coords, 0, this.coords, 0, 1);
        this.path.moveTo(this.coords[0], this.coords[1]);
        markPathDirty();
    }

    public void lineTo(double x1, double y1) {
        this.coords[0] = (float) x1;
        this.coords[1] = (float) y1;
        this.curState.transform.transform(this.coords, 0, this.coords, 0, 1);
        if (this.path.getNumCommands() == 0) {
            this.path.moveTo(this.coords[0], this.coords[1]);
        }
        this.path.lineTo(this.coords[0], this.coords[1]);
        markPathDirty();
    }

    public void quadraticCurveTo(double xc, double yc, double x1, double y1) {
        this.coords[0] = (float) xc;
        this.coords[1] = (float) yc;
        this.coords[2] = (float) x1;
        this.coords[3] = (float) y1;
        this.curState.transform.transform(this.coords, 0, this.coords, 0, 2);
        if (this.path.getNumCommands() == 0) {
            this.path.moveTo(this.coords[0], this.coords[1]);
        }
        this.path.quadTo(this.coords[0], this.coords[1], this.coords[2], this.coords[3]);
        markPathDirty();
    }

    public void bezierCurveTo(double xc1, double yc1, double xc2, double yc2, double x1, double y1) {
        this.coords[0] = (float) xc1;
        this.coords[1] = (float) yc1;
        this.coords[2] = (float) xc2;
        this.coords[3] = (float) yc2;
        this.coords[4] = (float) x1;
        this.coords[5] = (float) y1;
        this.curState.transform.transform(this.coords, 0, this.coords, 0, 3);
        if (this.path.getNumCommands() == 0) {
            this.path.moveTo(this.coords[0], this.coords[1]);
        }
        this.path.curveTo(this.coords[0], this.coords[1], this.coords[2], this.coords[3], this.coords[4], this.coords[5]);
        markPathDirty();
    }

    public void arcTo(double x1, double y1, double x2, double y2, double radius) {
        if (this.path.getNumCommands() == 0) {
            moveTo(x1, y1);
            lineTo(x1, y1);
        } else if (!tryArcTo((float) x1, (float) y1, (float) x2, (float) y2, (float) radius)) {
            lineTo(x1, y1);
        }
    }

    private static double lenSq(double x0, double y0, double x1, double y1) {
        double x12 = x1 - x0;
        double y12 = y1 - y0;
        return (x12 * x12) + (y12 * y12);
    }

    private boolean tryArcTo(float x1, float y1, float x2, float y2, float radius) {
        float x0;
        float y0;
        if (this.curState.transform.isTranslateOrIdentity()) {
            x0 = (float) (this.path.getCurrentX() - this.curState.transform.getMxt());
            y0 = (float) (this.path.getCurrentY() - this.curState.transform.getMyt());
        } else {
            this.coords[0] = this.path.getCurrentX();
            this.coords[1] = this.path.getCurrentY();
            try {
                this.curState.transform.inverseTransform(this.coords, 0, this.coords, 0, 1);
                x0 = this.coords[0];
                y0 = this.coords[1];
            } catch (NoninvertibleTransformException e2) {
                return false;
            }
        }
        double lsq01 = lenSq(x0, y0, x1, y1);
        double lsq12 = lenSq(x1, y1, x2, y2);
        double lsq02 = lenSq(x0, y0, x2, y2);
        double len01 = Math.sqrt(lsq01);
        double len12 = Math.sqrt(lsq12);
        double cosnum = (lsq01 + lsq12) - lsq02;
        double cosden = 2.0d * len01 * len12;
        if (cosden == 0.0d || radius <= 0.0f) {
            return false;
        }
        double cos_2theta = cosnum / cosden;
        double tansq_den = 1.0d + cos_2theta;
        if (tansq_den == 0.0d) {
            return false;
        }
        double tansq_theta = (1.0d - cos_2theta) / tansq_den;
        double A2 = radius / Math.sqrt(tansq_theta);
        double tx0 = x1 + ((A2 / len01) * (x0 - x1));
        double ty0 = y1 + ((A2 / len01) * (y0 - y1));
        double tx1 = x1 + ((A2 / len12) * (x2 - x1));
        double ty1 = y1 + ((A2 / len12) * (y2 - y1));
        double mx = (tx0 + tx1) / 2.0d;
        double my = (ty0 + ty1) / 2.0d;
        double lenratioden = lenSq(mx, my, x1, y1);
        if (lenratioden == 0.0d) {
            return false;
        }
        double lenratio = lenSq(mx, my, tx0, ty0) / lenratioden;
        double cx = mx + ((mx - x1) * lenratio);
        double cy = my + ((my - y1) * lenratio);
        if (cx != cx || cy != cy) {
            return false;
        }
        if (tx0 != x0 || ty0 != y0) {
            lineTo(tx0, ty0);
        }
        double coshalfarc = Math.sqrt((1.0d - cos_2theta) / 2.0d);
        boolean ccw = (ty0 - cy) * (tx1 - cx) > (ty1 - cy) * (tx0 - cx);
        if (cos_2theta <= 0.0d) {
            double sinhalfarc = Math.sqrt((1.0d + cos_2theta) / 2.0d);
            double cv = (1.3333333333333333d * sinhalfarc) / (1.0d + coshalfarc);
            if (ccw) {
                cv = -cv;
            }
            double cpx0 = tx0 - (cv * (ty0 - cy));
            double cpy0 = ty0 + (cv * (tx0 - cx));
            double cpx1 = tx1 + (cv * (ty1 - cy));
            double cpy1 = ty1 - (cv * (tx1 - cx));
            bezierCurveTo(cpx0, cpy0, cpx1, cpy1, tx1, ty1);
            return true;
        }
        double sinqtrarc = Math.sqrt((1.0d - coshalfarc) / 2.0d);
        double cosqtrarc = Math.sqrt((1.0d + coshalfarc) / 2.0d);
        double cv2 = (1.3333333333333333d * sinqtrarc) / (1.0d + cosqtrarc);
        if (ccw) {
            cv2 = -cv2;
        }
        double midratio = radius / Math.sqrt(lenratioden);
        double midarcx = cx + ((x1 - mx) * midratio);
        double midarcy = cy + ((y1 - my) * midratio);
        double cpx02 = tx0 - (cv2 * (ty0 - cy));
        double cpy02 = ty0 + (cv2 * (tx0 - cx));
        double cpx12 = midarcx + (cv2 * (midarcy - cy));
        double cpy12 = midarcy - (cv2 * (midarcx - cx));
        bezierCurveTo(cpx02, cpy02, cpx12, cpy12, midarcx, midarcy);
        double cpx03 = midarcx - (cv2 * (midarcy - cy));
        double cpy03 = midarcy + (cv2 * (midarcx - cx));
        double cpx13 = tx1 + (cv2 * (ty1 - cy));
        double cpy13 = ty1 - (cv2 * (tx1 - cx));
        bezierCurveTo(cpx03, cpy03, cpx13, cpy13, tx1, ty1);
        return true;
    }

    public void arc(double centerX, double centerY, double radiusX, double radiusY, double startAngle, double length) {
        Arc2D arc = new Arc2D((float) (centerX - radiusX), (float) (centerY - radiusY), (float) (radiusX * 2.0d), (float) (radiusY * 2.0d), (float) startAngle, (float) length, 0);
        this.path.append(arc.getPathIterator(this.curState.transform), true);
        markPathDirty();
    }

    public void rect(double x2, double y2, double w2, double h2) {
        this.coords[0] = (float) x2;
        this.coords[1] = (float) y2;
        this.coords[2] = (float) w2;
        this.coords[3] = 0.0f;
        this.coords[4] = 0.0f;
        this.coords[5] = (float) h2;
        this.curState.transform.deltaTransform(this.coords, 0, this.coords, 0, 3);
        float x0 = this.coords[0] + ((float) this.curState.transform.getMxt());
        float y0 = this.coords[1] + ((float) this.curState.transform.getMyt());
        float dx1 = this.coords[2];
        float dy1 = this.coords[3];
        float dx2 = this.coords[4];
        float dy2 = this.coords[5];
        this.path.moveTo(x0, y0);
        this.path.lineTo(x0 + dx1, y0 + dy1);
        this.path.lineTo(x0 + dx1 + dx2, y0 + dy1 + dy2);
        this.path.lineTo(x0 + dx2, y0 + dy2);
        this.path.closePath();
        markPathDirty();
    }

    public void appendSVGPath(String svgpath) {
        float x0;
        float y0;
        if (svgpath == null) {
            return;
        }
        boolean prependMoveto = true;
        boolean skipMoveto = true;
        int i2 = 0;
        while (true) {
            if (i2 < svgpath.length()) {
                switch (svgpath.charAt(i2)) {
                    case '\t':
                    case '\n':
                    case '\r':
                    case ' ':
                        i2++;
                    case 'M':
                        skipMoveto = false;
                        prependMoveto = false;
                        break;
                    case 'm':
                        if (this.path.getNumCommands() == 0) {
                            prependMoveto = false;
                        }
                        skipMoveto = false;
                        break;
                }
            }
        }
        Path2D p2d = new Path2D();
        if (prependMoveto && this.path.getNumCommands() > 0) {
            if (this.curState.transform.isTranslateOrIdentity()) {
                x0 = (float) (this.path.getCurrentX() - this.curState.transform.getMxt());
                y0 = (float) (this.path.getCurrentY() - this.curState.transform.getMyt());
            } else {
                this.coords[0] = this.path.getCurrentX();
                this.coords[1] = this.path.getCurrentY();
                try {
                    this.curState.transform.inverseTransform(this.coords, 0, this.coords, 0, 1);
                } catch (NoninvertibleTransformException e2) {
                }
                x0 = this.coords[0];
                y0 = this.coords[1];
            }
            p2d.moveTo(x0, y0);
        } else {
            skipMoveto = false;
        }
        try {
            p2d.appendSVGPath(svgpath);
            PathIterator pi = p2d.getPathIterator(this.curState.transform);
            if (skipMoveto) {
                pi.next();
            }
            this.path.append(pi, false);
        } catch (IllegalPathStateException | IllegalArgumentException e3) {
        }
    }

    public void closePath() {
        if (this.path.getNumCommands() > 0) {
            this.path.closePath();
            markPathDirty();
        }
    }

    public void fill() {
        writePath((byte) 47);
    }

    public void stroke() {
        writePath((byte) 48);
    }

    public void clip() {
        Path2D clip = new Path2D(this.path);
        this.clipStack.addLast(clip);
        this.curState.numClipPaths++;
        GrowableDataBuffer buf = getBuffer();
        buf.putByte((byte) 13);
        buf.putObject(clip);
    }

    public boolean isPointInPath(double x2, double y2) {
        return this.path.contains((float) x2, (float) y2);
    }

    public void clearRect(double x2, double y2, double w2, double h2) {
        if (w2 != 0.0d && h2 != 0.0d) {
            resetIfCovers(null, x2, y2, w2, h2);
            writeOp4(x2, y2, w2, h2, (byte) 27);
        }
    }

    public void fillRect(double x2, double y2, double w2, double h2) {
        if (w2 != 0.0d && h2 != 0.0d) {
            resetIfCovers(this.curState.fill, x2, y2, w2, h2);
            writeOp4(x2, y2, w2, h2, (byte) 25);
        }
    }

    public void strokeRect(double x2, double y2, double w2, double h2) {
        if (w2 != 0.0d || h2 != 0.0d) {
            writeOp4(x2, y2, w2, h2, (byte) 26);
        }
    }

    public void fillOval(double x2, double y2, double w2, double h2) {
        if (w2 != 0.0d && h2 != 0.0d) {
            writeOp4(x2, y2, w2, h2, (byte) 29);
        }
    }

    public void strokeOval(double x2, double y2, double w2, double h2) {
        if (w2 != 0.0d || h2 != 0.0d) {
            writeOp4(x2, y2, w2, h2, (byte) 30);
        }
    }

    public void fillArc(double x2, double y2, double w2, double h2, double startAngle, double arcExtent, ArcType closure) {
        if (w2 != 0.0d && h2 != 0.0d && closure != null) {
            writeArcType(closure);
            writeOp6(x2, y2, w2, h2, startAngle, arcExtent, (byte) 33);
        }
    }

    public void strokeArc(double x2, double y2, double w2, double h2, double startAngle, double arcExtent, ArcType closure) {
        if (w2 != 0.0d && h2 != 0.0d && closure != null) {
            writeArcType(closure);
            writeOp6(x2, y2, w2, h2, startAngle, arcExtent, (byte) 34);
        }
    }

    public void fillRoundRect(double x2, double y2, double w2, double h2, double arcWidth, double arcHeight) {
        if (w2 != 0.0d && h2 != 0.0d) {
            writeOp6(x2, y2, w2, h2, arcWidth, arcHeight, (byte) 31);
        }
    }

    public void strokeRoundRect(double x2, double y2, double w2, double h2, double arcWidth, double arcHeight) {
        if (w2 != 0.0d && h2 != 0.0d) {
            writeOp6(x2, y2, w2, h2, arcWidth, arcHeight, (byte) 32);
        }
    }

    public void strokeLine(double x1, double y1, double x2, double y2) {
        writeOp4(x1, y1, x2, y2, (byte) 28);
    }

    public void fillPolygon(double[] xPoints, double[] yPoints, int nPoints) {
        if (nPoints >= 3) {
            writePoly(xPoints, yPoints, nPoints, true, (byte) 47);
        }
    }

    public void strokePolygon(double[] xPoints, double[] yPoints, int nPoints) {
        if (nPoints >= 2) {
            writePoly(xPoints, yPoints, nPoints, true, (byte) 48);
        }
    }

    public void strokePolyline(double[] xPoints, double[] yPoints, int nPoints) {
        if (nPoints >= 2) {
            writePoly(xPoints, yPoints, nPoints, false, (byte) 48);
        }
    }

    public void drawImage(Image img, double x2, double y2) {
        if (img == null) {
            return;
        }
        double sw = img.getWidth();
        double sh = img.getHeight();
        writeImage(img, x2, y2, sw, sh);
    }

    public void drawImage(Image img, double x2, double y2, double w2, double h2) {
        writeImage(img, x2, y2, w2, h2);
    }

    public void drawImage(Image img, double sx, double sy, double sw, double sh, double dx, double dy, double dw, double dh) {
        writeImage(img, dx, dy, dw, dh, sx, sy, sw, sh);
    }

    public PixelWriter getPixelWriter() {
        if (this.writer == null) {
            this.writer = new PixelWriter() { // from class: javafx.scene.canvas.GraphicsContext.1
                @Override // javafx.scene.image.PixelWriter
                public PixelFormat<ByteBuffer> getPixelFormat() {
                    return PixelFormat.getByteBgraPreInstance();
                }

                private BytePixelSetter getSetter() {
                    return ByteBgraPre.setter;
                }

                @Override // javafx.scene.image.PixelWriter
                public void setArgb(int x2, int y2, int argb) {
                    GrowableDataBuffer buf = GraphicsContext.this.getBuffer();
                    buf.putByte((byte) 52);
                    buf.putInt(x2);
                    buf.putInt(y2);
                    buf.putInt(argb);
                }

                @Override // javafx.scene.image.PixelWriter
                public void setColor(int x2, int y2, Color c2) {
                    if (c2 == null) {
                        throw new NullPointerException("Color cannot be null");
                    }
                    int a2 = (int) Math.round(c2.getOpacity() * 255.0d);
                    int r2 = (int) Math.round(c2.getRed() * 255.0d);
                    int g2 = (int) Math.round(c2.getGreen() * 255.0d);
                    int b2 = (int) Math.round(c2.getBlue() * 255.0d);
                    setArgb(x2, y2, (a2 << 24) | (r2 << 16) | (g2 << 8) | b2);
                }

                private void writePixelBuffer(int x2, int y2, int w2, int h2, byte[] pixels) {
                    GrowableDataBuffer buf = GraphicsContext.this.getBuffer();
                    buf.putByte((byte) 53);
                    buf.putInt(x2);
                    buf.putInt(y2);
                    buf.putInt(w2);
                    buf.putInt(h2);
                    buf.putObject(pixels);
                }

                private int[] checkBounds(int x2, int y2, int w2, int h2, PixelFormat<? extends Buffer> pf, int scan) {
                    int cw = (int) Math.ceil(GraphicsContext.this.theCanvas.getWidth());
                    int ch = (int) Math.ceil(GraphicsContext.this.theCanvas.getHeight());
                    if (x2 >= 0 && y2 >= 0 && x2 + w2 <= cw && y2 + h2 <= ch) {
                        return null;
                    }
                    int offset = 0;
                    if (x2 < 0) {
                        w2 += x2;
                        if (w2 < 0) {
                            return null;
                        }
                        if (pf != null) {
                            switch (AnonymousClass2.$SwitchMap$javafx$scene$image$PixelFormat$Type[pf.getType().ordinal()]) {
                                case 1:
                                case 2:
                                    offset = 0 - (x2 * 4);
                                    break;
                                case 3:
                                    offset = 0 - (x2 * 3);
                                    break;
                                case 4:
                                case 5:
                                case 6:
                                    offset = 0 - x2;
                                    break;
                                default:
                                    throw new InternalError("unknown Pixel Format");
                            }
                        }
                        x2 = 0;
                    }
                    if (y2 < 0) {
                        h2 += y2;
                        if (h2 < 0) {
                            return null;
                        }
                        offset -= y2 * scan;
                        y2 = 0;
                    }
                    if (x2 + w2 > cw) {
                        w2 = cw - x2;
                        if (w2 < 0) {
                            return null;
                        }
                    }
                    if (y2 + h2 > ch) {
                        h2 = ch - y2;
                        if (h2 < 0) {
                            return null;
                        }
                    }
                    return new int[]{x2, y2, w2, h2, offset};
                }

                @Override // javafx.scene.image.PixelWriter
                public <T extends Buffer> void setPixels(int x2, int y2, int w2, int h2, PixelFormat<T> pixelformat, T buffer, int scan) {
                    if (pixelformat == null) {
                        throw new NullPointerException("PixelFormat cannot be null");
                    }
                    if (buffer == null) {
                        throw new NullPointerException("Buffer cannot be null");
                    }
                    if (w2 <= 0 || h2 <= 0) {
                        return;
                    }
                    int offset = buffer.position();
                    int[] adjustments = checkBounds(x2, y2, w2, h2, pixelformat, scan);
                    if (adjustments != null) {
                        x2 = adjustments[0];
                        y2 = adjustments[1];
                        w2 = adjustments[2];
                        h2 = adjustments[3];
                        offset += adjustments[4];
                    }
                    byte[] pixels = new byte[w2 * h2 * 4];
                    ByteBuffer dst = ByteBuffer.wrap(pixels);
                    PixelGetter<T> getter = PixelUtils.getGetter(pixelformat);
                    PixelConverter<T, ByteBuffer> converter = PixelUtils.getConverter(getter, getSetter());
                    converter.convert(buffer, offset, scan, dst, 0, w2 * 4, w2, h2);
                    writePixelBuffer(x2, y2, w2, h2, pixels);
                }

                @Override // javafx.scene.image.PixelWriter
                public void setPixels(int x2, int y2, int w2, int h2, PixelFormat<ByteBuffer> pixelformat, byte[] buffer, int offset, int scanlineStride) {
                    if (pixelformat == null) {
                        throw new NullPointerException("PixelFormat cannot be null");
                    }
                    if (buffer == null) {
                        throw new NullPointerException("Buffer cannot be null");
                    }
                    if (w2 <= 0 || h2 <= 0) {
                        return;
                    }
                    int[] adjustments = checkBounds(x2, y2, w2, h2, pixelformat, scanlineStride);
                    if (adjustments != null) {
                        x2 = adjustments[0];
                        y2 = adjustments[1];
                        w2 = adjustments[2];
                        h2 = adjustments[3];
                        offset += adjustments[4];
                    }
                    byte[] pixels = new byte[w2 * h2 * 4];
                    BytePixelGetter getter = PixelUtils.getByteGetter(pixelformat);
                    ByteToBytePixelConverter converter = PixelUtils.getB2BConverter(getter, getSetter());
                    converter.convert(buffer, offset, scanlineStride, pixels, 0, w2 * 4, w2, h2);
                    writePixelBuffer(x2, y2, w2, h2, pixels);
                }

                @Override // javafx.scene.image.PixelWriter
                public void setPixels(int x2, int y2, int w2, int h2, PixelFormat<IntBuffer> pixelformat, int[] buffer, int offset, int scanlineStride) {
                    if (pixelformat == null) {
                        throw new NullPointerException("PixelFormat cannot be null");
                    }
                    if (buffer == null) {
                        throw new NullPointerException("Buffer cannot be null");
                    }
                    if (w2 <= 0 || h2 <= 0) {
                        return;
                    }
                    int[] adjustments = checkBounds(x2, y2, w2, h2, pixelformat, scanlineStride);
                    if (adjustments != null) {
                        x2 = adjustments[0];
                        y2 = adjustments[1];
                        w2 = adjustments[2];
                        h2 = adjustments[3];
                        offset += adjustments[4];
                    }
                    byte[] pixels = new byte[w2 * h2 * 4];
                    IntPixelGetter getter = PixelUtils.getIntGetter(pixelformat);
                    IntToBytePixelConverter converter = PixelUtils.getI2BConverter(getter, getSetter());
                    converter.convert(buffer, offset, scanlineStride, pixels, 0, w2 * 4, w2, h2);
                    writePixelBuffer(x2, y2, w2, h2, pixels);
                }

                @Override // javafx.scene.image.PixelWriter
                public void setPixels(int dstx, int dsty, int w2, int h2, PixelReader reader, int srcx, int srcy) {
                    if (reader == null) {
                        throw new NullPointerException("Reader cannot be null");
                    }
                    if (w2 <= 0 || h2 <= 0) {
                        return;
                    }
                    int[] adjustments = checkBounds(dstx, dsty, w2, h2, null, 0);
                    if (adjustments != null) {
                        int newx = adjustments[0];
                        int newy = adjustments[1];
                        srcx += newx - dstx;
                        srcy += newy - dsty;
                        dstx = newx;
                        dsty = newy;
                        w2 = adjustments[2];
                        h2 = adjustments[3];
                    }
                    byte[] pixels = new byte[w2 * h2 * 4];
                    reader.getPixels(srcx, srcy, w2, h2, PixelFormat.getByteBgraPreInstance(), pixels, 0, w2 * 4);
                    writePixelBuffer(dstx, dsty, w2, h2, pixels);
                }
            };
        }
        return this.writer;
    }

    /* renamed from: javafx.scene.canvas.GraphicsContext$2, reason: invalid class name */
    /* loaded from: jfxrt.jar:javafx/scene/canvas/GraphicsContext$2.class */
    static /* synthetic */ class AnonymousClass2 {
        static final /* synthetic */ int[] $SwitchMap$javafx$scene$image$PixelFormat$Type = new int[PixelFormat.Type.values().length];

        static {
            try {
                $SwitchMap$javafx$scene$image$PixelFormat$Type[PixelFormat.Type.BYTE_BGRA.ordinal()] = 1;
            } catch (NoSuchFieldError e2) {
            }
            try {
                $SwitchMap$javafx$scene$image$PixelFormat$Type[PixelFormat.Type.BYTE_BGRA_PRE.ordinal()] = 2;
            } catch (NoSuchFieldError e3) {
            }
            try {
                $SwitchMap$javafx$scene$image$PixelFormat$Type[PixelFormat.Type.BYTE_RGB.ordinal()] = 3;
            } catch (NoSuchFieldError e4) {
            }
            try {
                $SwitchMap$javafx$scene$image$PixelFormat$Type[PixelFormat.Type.BYTE_INDEXED.ordinal()] = 4;
            } catch (NoSuchFieldError e5) {
            }
            try {
                $SwitchMap$javafx$scene$image$PixelFormat$Type[PixelFormat.Type.INT_ARGB.ordinal()] = 5;
            } catch (NoSuchFieldError e6) {
            }
            try {
                $SwitchMap$javafx$scene$image$PixelFormat$Type[PixelFormat.Type.INT_ARGB_PRE.ordinal()] = 6;
            } catch (NoSuchFieldError e7) {
            }
            $SwitchMap$javafx$geometry$VPos = new int[VPos.values().length];
            try {
                $SwitchMap$javafx$geometry$VPos[VPos.TOP.ordinal()] = 1;
            } catch (NoSuchFieldError e8) {
            }
            try {
                $SwitchMap$javafx$geometry$VPos[VPos.CENTER.ordinal()] = 2;
            } catch (NoSuchFieldError e9) {
            }
            try {
                $SwitchMap$javafx$geometry$VPos[VPos.BASELINE.ordinal()] = 3;
            } catch (NoSuchFieldError e10) {
            }
            try {
                $SwitchMap$javafx$geometry$VPos[VPos.BOTTOM.ordinal()] = 4;
            } catch (NoSuchFieldError e11) {
            }
            $SwitchMap$javafx$scene$text$TextAlignment = new int[TextAlignment.values().length];
            try {
                $SwitchMap$javafx$scene$text$TextAlignment[TextAlignment.LEFT.ordinal()] = 1;
            } catch (NoSuchFieldError e12) {
            }
            try {
                $SwitchMap$javafx$scene$text$TextAlignment[TextAlignment.CENTER.ordinal()] = 2;
            } catch (NoSuchFieldError e13) {
            }
            try {
                $SwitchMap$javafx$scene$text$TextAlignment[TextAlignment.RIGHT.ordinal()] = 3;
            } catch (NoSuchFieldError e14) {
            }
            try {
                $SwitchMap$javafx$scene$text$TextAlignment[TextAlignment.JUSTIFY.ordinal()] = 4;
            } catch (NoSuchFieldError e15) {
            }
            $SwitchMap$javafx$scene$shape$StrokeLineJoin = new int[StrokeLineJoin.values().length];
            try {
                $SwitchMap$javafx$scene$shape$StrokeLineJoin[StrokeLineJoin.MITER.ordinal()] = 1;
            } catch (NoSuchFieldError e16) {
            }
            try {
                $SwitchMap$javafx$scene$shape$StrokeLineJoin[StrokeLineJoin.BEVEL.ordinal()] = 2;
            } catch (NoSuchFieldError e17) {
            }
            try {
                $SwitchMap$javafx$scene$shape$StrokeLineJoin[StrokeLineJoin.ROUND.ordinal()] = 3;
            } catch (NoSuchFieldError e18) {
            }
            $SwitchMap$javafx$scene$shape$StrokeLineCap = new int[StrokeLineCap.values().length];
            try {
                $SwitchMap$javafx$scene$shape$StrokeLineCap[StrokeLineCap.BUTT.ordinal()] = 1;
            } catch (NoSuchFieldError e19) {
            }
            try {
                $SwitchMap$javafx$scene$shape$StrokeLineCap[StrokeLineCap.ROUND.ordinal()] = 2;
            } catch (NoSuchFieldError e20) {
            }
            try {
                $SwitchMap$javafx$scene$shape$StrokeLineCap[StrokeLineCap.SQUARE.ordinal()] = 3;
            } catch (NoSuchFieldError e21) {
            }
            $SwitchMap$javafx$scene$shape$ArcType = new int[ArcType.values().length];
            try {
                $SwitchMap$javafx$scene$shape$ArcType[ArcType.OPEN.ordinal()] = 1;
            } catch (NoSuchFieldError e22) {
            }
            try {
                $SwitchMap$javafx$scene$shape$ArcType[ArcType.CHORD.ordinal()] = 2;
            } catch (NoSuchFieldError e23) {
            }
            try {
                $SwitchMap$javafx$scene$shape$ArcType[ArcType.ROUND.ordinal()] = 3;
            } catch (NoSuchFieldError e24) {
            }
        }
    }

    public void setEffect(Effect e2) {
        GrowableDataBuffer buf = getBuffer();
        buf.putByte((byte) 12);
        if (e2 == null) {
            this.curState.effect = null;
            buf.putObject(null);
        } else {
            this.curState.effect = e2.impl_copy();
            this.curState.effect.impl_sync();
            buf.putObject(this.curState.effect.impl_getImpl());
        }
    }

    public Effect getEffect(Effect e2) {
        if (this.curState.effect == null) {
            return null;
        }
        return this.curState.effect.impl_copy();
    }

    public void applyEffect(Effect e2) {
        if (e2 == null) {
            return;
        }
        GrowableDataBuffer buf = getBuffer();
        buf.putByte((byte) 60);
        Effect effect = e2.impl_copy();
        effect.impl_sync();
        buf.putObject(effect.impl_getImpl());
    }
}
