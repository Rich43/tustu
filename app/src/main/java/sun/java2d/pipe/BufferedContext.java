package sun.java2d.pipe;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Paint;
import java.awt.geom.AffineTransform;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import sun.java2d.InvalidPipeException;
import sun.java2d.SunGraphics2D;
import sun.java2d.loops.XORComposite;
import sun.java2d.pipe.hw.AccelSurface;

/* loaded from: rt.jar:sun/java2d/pipe/BufferedContext.class */
public abstract class BufferedContext {
    public static final int NO_CONTEXT_FLAGS = 0;
    public static final int SRC_IS_OPAQUE = 1;
    public static final int USE_MASK = 2;
    protected RenderQueue rq;
    protected RenderBuffer buf;
    protected static BufferedContext currentContext;
    private Reference<AccelSurface> validSrcDataRef = new WeakReference(null);
    private Reference<AccelSurface> validDstDataRef = new WeakReference(null);
    private Reference<Region> validClipRef = new WeakReference(null);
    private Reference<Composite> validCompRef = new WeakReference(null);
    private Reference<Paint> validPaintRef = new WeakReference(null);
    private boolean isValidatedPaintJustAColor;
    private int validatedRGB;
    private int validatedFlags;
    private boolean xformInUse;
    private AffineTransform transform;

    public abstract RenderQueue getRenderQueue();

    public abstract void saveState();

    public abstract void restoreState();

    protected BufferedContext(RenderQueue renderQueue) {
        this.rq = renderQueue;
        this.buf = renderQueue.getBuffer();
    }

    public static void validateContext(AccelSurface accelSurface, AccelSurface accelSurface2, Region region, Composite composite, AffineTransform affineTransform, Paint paint, SunGraphics2D sunGraphics2D, int i2) {
        accelSurface2.getContext().validate(accelSurface, accelSurface2, region, composite, affineTransform, paint, sunGraphics2D, i2);
    }

    public static void validateContext(AccelSurface accelSurface) {
        validateContext(accelSurface, accelSurface, null, null, null, null, null, 0);
    }

    public void validate(AccelSurface accelSurface, AccelSurface accelSurface2, Region region, Composite composite, AffineTransform affineTransform, Paint paint, SunGraphics2D sunGraphics2D, int i2) {
        boolean z2 = false;
        boolean z3 = false;
        if (!accelSurface2.isValid() || accelSurface2.isSurfaceLost() || accelSurface.isSurfaceLost()) {
            invalidateContext();
            throw new InvalidPipeException("bounds changed or surface lost");
        }
        if (paint instanceof Color) {
            int rgb = ((Color) paint).getRGB();
            if (this.isValidatedPaintJustAColor) {
                if (rgb != this.validatedRGB) {
                    this.validatedRGB = rgb;
                    z3 = true;
                }
            } else {
                this.validatedRGB = rgb;
                z3 = true;
                this.isValidatedPaintJustAColor = true;
            }
        } else if (this.validPaintRef.get() != paint) {
            z3 = true;
            this.isValidatedPaintJustAColor = false;
        }
        AccelSurface accelSurface3 = this.validSrcDataRef.get();
        AccelSurface accelSurface4 = this.validDstDataRef.get();
        if (currentContext != this || accelSurface != accelSurface3 || accelSurface2 != accelSurface4) {
            if (accelSurface2 != accelSurface4) {
                z2 = true;
            }
            if (paint == null) {
                z3 = true;
            }
            setSurfaces(accelSurface, accelSurface2);
            currentContext = this;
            this.validSrcDataRef = new WeakReference(accelSurface);
            this.validDstDataRef = new WeakReference(accelSurface2);
        }
        Region region2 = this.validClipRef.get();
        if (region != region2 || z2) {
            if (region != null) {
                if (z2 || region2 == null || !region2.isRectangular() || !region.isRectangular() || region.getLoX() != region2.getLoX() || region.getLoY() != region2.getLoY() || region.getHiX() != region2.getHiX() || region.getHiY() != region2.getHiY()) {
                    setClip(region);
                }
            } else {
                resetClip();
            }
            this.validClipRef = new WeakReference(region);
        }
        if (composite != this.validCompRef.get() || i2 != this.validatedFlags) {
            if (composite != null) {
                setComposite(composite, i2);
            } else {
                resetComposite();
            }
            z3 = true;
            this.validCompRef = new WeakReference(composite);
            this.validatedFlags = i2;
        }
        boolean z4 = false;
        if (affineTransform == null) {
            if (this.xformInUse) {
                resetTransform();
                this.xformInUse = false;
                z4 = true;
            } else if (sunGraphics2D != null && !sunGraphics2D.transform.equals(this.transform)) {
                z4 = true;
            }
            if (sunGraphics2D != null && z4) {
                this.transform = new AffineTransform(sunGraphics2D.transform);
            }
        } else {
            setTransform(affineTransform);
            this.xformInUse = true;
            z4 = true;
        }
        if (!this.isValidatedPaintJustAColor && z4) {
            z3 = true;
        }
        if (z3) {
            if (paint != null) {
                BufferedPaints.setPaint(this.rq, sunGraphics2D, paint, i2);
            } else {
                BufferedPaints.resetPaint(this.rq);
            }
            this.validPaintRef = new WeakReference(paint);
        }
        accelSurface2.markDirty();
    }

    private void invalidateSurfaces() {
        this.validSrcDataRef.clear();
        this.validDstDataRef.clear();
    }

    private void setSurfaces(AccelSurface accelSurface, AccelSurface accelSurface2) {
        this.rq.ensureCapacityAndAlignment(20, 4);
        this.buf.putInt(70);
        this.buf.putLong(accelSurface.getNativeOps());
        this.buf.putLong(accelSurface2.getNativeOps());
    }

    private void resetClip() {
        this.rq.ensureCapacity(4);
        this.buf.putInt(55);
    }

    private void setClip(Region region) {
        if (region.isRectangular()) {
            this.rq.ensureCapacity(20);
            this.buf.putInt(51);
            this.buf.putInt(region.getLoX()).putInt(region.getLoY());
            this.buf.putInt(region.getHiX()).putInt(region.getHiY());
            return;
        }
        this.rq.ensureCapacity(28);
        this.buf.putInt(52);
        this.buf.putInt(53);
        int iPosition = this.buf.position();
        this.buf.putInt(0);
        int i2 = 0;
        int iRemaining = this.buf.remaining() / 16;
        int[] iArr = new int[4];
        SpanIterator spanIterator = region.getSpanIterator();
        while (spanIterator.nextSpan(iArr)) {
            if (iRemaining == 0) {
                this.buf.putInt(iPosition, i2);
                this.rq.flushNow();
                this.buf.putInt(53);
                iPosition = this.buf.position();
                this.buf.putInt(0);
                i2 = 0;
                iRemaining = this.buf.remaining() / 16;
            }
            this.buf.putInt(iArr[0]);
            this.buf.putInt(iArr[1]);
            this.buf.putInt(iArr[2]);
            this.buf.putInt(iArr[3]);
            i2++;
            iRemaining--;
        }
        this.buf.putInt(iPosition, i2);
        this.rq.ensureCapacity(4);
        this.buf.putInt(54);
    }

    private void resetComposite() {
        this.rq.ensureCapacity(4);
        this.buf.putInt(58);
    }

    private void setComposite(Composite composite, int i2) {
        if (composite instanceof AlphaComposite) {
            AlphaComposite alphaComposite = (AlphaComposite) composite;
            this.rq.ensureCapacity(16);
            this.buf.putInt(56);
            this.buf.putInt(alphaComposite.getRule());
            this.buf.putFloat(alphaComposite.getAlpha());
            this.buf.putInt(i2);
            return;
        }
        if (composite instanceof XORComposite) {
            int xorPixel = ((XORComposite) composite).getXorPixel();
            this.rq.ensureCapacity(8);
            this.buf.putInt(57);
            this.buf.putInt(xorPixel);
            return;
        }
        throw new InternalError("not yet implemented");
    }

    private void resetTransform() {
        this.rq.ensureCapacity(4);
        this.buf.putInt(60);
    }

    private void setTransform(AffineTransform affineTransform) {
        this.rq.ensureCapacityAndAlignment(52, 4);
        this.buf.putInt(59);
        this.buf.putDouble(affineTransform.getScaleX());
        this.buf.putDouble(affineTransform.getShearY());
        this.buf.putDouble(affineTransform.getShearX());
        this.buf.putDouble(affineTransform.getScaleY());
        this.buf.putDouble(affineTransform.getTranslateX());
        this.buf.putDouble(affineTransform.getTranslateY());
    }

    public void invalidateContext() {
        resetTransform();
        resetComposite();
        resetClip();
        BufferedPaints.resetPaint(this.rq);
        invalidateSurfaces();
        this.validCompRef.clear();
        this.validClipRef.clear();
        this.validPaintRef.clear();
        this.isValidatedPaintJustAColor = false;
        this.xformInUse = false;
    }
}
