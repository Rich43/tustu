package com.sun.prism.impl.shape;

import com.sun.glass.utils.NativeLibLoader;
import com.sun.javafx.geom.Path2D;
import com.sun.javafx.geom.RectBounds;
import com.sun.javafx.geom.Shape;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.javafx.util.Logging;
import com.sun.prism.BasicStroke;
import com.sun.prism.impl.PrismSettings;
import java.nio.ByteBuffer;
import java.security.AccessController;

/* loaded from: jfxrt.jar:com/sun/prism/impl/shape/NativePiscesRasterizer.class */
public class NativePiscesRasterizer implements ShapeRasterizer {
    private static MaskData emptyData = MaskData.create(new byte[1], 0, 0, 1, 1);
    private static final byte SEG_MOVETO = 0;
    private static final byte SEG_LINETO = 1;
    private static final byte SEG_QUADTO = 2;
    private static final byte SEG_CUBICTO = 3;
    private static final byte SEG_CLOSE = 4;
    private byte[] cachedMask;
    private ByteBuffer cachedBuffer;
    private MaskData cachedData;
    private boolean lastAntialiasedShape;
    private int[] bounds = new int[4];
    private boolean firstTimeAASetting = true;

    static native void init(int i2, int i3);

    static native void produceFillAlphas(float[] fArr, byte[] bArr, int i2, boolean z2, double d2, double d3, double d4, double d5, double d6, double d7, int[] iArr, byte[] bArr2);

    static native void produceStrokeAlphas(float[] fArr, byte[] bArr, int i2, float f2, int i3, int i4, float f3, float[] fArr2, float f4, double d2, double d3, double d4, double d5, double d6, double d7, int[] iArr, byte[] bArr2);

    static {
        AccessController.doPrivileged(() -> {
            if (PrismSettings.verbose) {
                System.out.println("Loading Prism common native library ...");
            }
            NativeLibLoader.loadLibrary("prism_common");
            if (PrismSettings.verbose) {
                System.out.println("\tsucceeded.");
                return null;
            }
            return null;
        });
    }

    @Override // com.sun.prism.impl.shape.ShapeRasterizer
    public MaskData getMaskData(Shape shape, BasicStroke stroke, RectBounds xformBounds, BaseTransform xform, boolean close, boolean antialiasedShape) {
        double myy;
        double mxx;
        double myx;
        double mxy;
        double myt;
        double mxt;
        if (this.firstTimeAASetting || this.lastAntialiasedShape != antialiasedShape) {
            int subpixelLgPositions = antialiasedShape ? 3 : 0;
            init(subpixelLgPositions, subpixelLgPositions);
            this.firstTimeAASetting = false;
            this.lastAntialiasedShape = antialiasedShape;
        }
        if (stroke != null && stroke.getType() != 0) {
            shape = stroke.createStrokedShape(shape);
            stroke = null;
        }
        if (xformBounds == null) {
            if (stroke != null) {
                shape = stroke.createStrokedShape(shape);
                stroke = null;
            }
            xformBounds = (RectBounds) xform.transform(shape.getBounds(), new RectBounds());
        }
        this.bounds[0] = (int) Math.floor(xformBounds.getMinX());
        this.bounds[1] = (int) Math.floor(xformBounds.getMinY());
        this.bounds[2] = (int) Math.ceil(xformBounds.getMaxX());
        this.bounds[3] = (int) Math.ceil(xformBounds.getMaxY());
        if (this.bounds[2] <= this.bounds[0] || this.bounds[3] <= this.bounds[1]) {
            return emptyData;
        }
        Path2D p2d = shape instanceof Path2D ? (Path2D) shape : new Path2D(shape);
        if (xform == null || xform.isIdentity()) {
            myy = 1.0d;
            mxx = 1.0d;
            myx = 0.0d;
            mxy = 0.0d;
            myt = 0.0d;
            mxt = 0.0d;
        } else {
            mxx = xform.getMxx();
            mxy = xform.getMxy();
            mxt = xform.getMxt();
            myx = xform.getMyx();
            myy = xform.getMyy();
            myt = xform.getMyt();
        }
        int x2 = this.bounds[0];
        int y2 = this.bounds[1];
        int w2 = this.bounds[2] - x2;
        int h2 = this.bounds[3] - y2;
        if (w2 <= 0 || h2 <= 0) {
            return emptyData;
        }
        if (this.cachedMask == null || w2 * h2 > this.cachedMask.length) {
            this.cachedMask = null;
            this.cachedBuffer = null;
            this.cachedData = new MaskData();
            int csize = ((w2 * h2) + 4095) & (-4096);
            this.cachedMask = new byte[csize];
            this.cachedBuffer = ByteBuffer.wrap(this.cachedMask);
        }
        try {
            if (stroke != null) {
                produceStrokeAlphas(p2d.getFloatCoordsNoClone(), p2d.getCommandsNoClone(), p2d.getNumCommands(), stroke.getLineWidth(), stroke.getEndCap(), stroke.getLineJoin(), stroke.getMiterLimit(), stroke.getDashArray(), stroke.getDashPhase(), mxx, mxy, mxt, myx, myy, myt, this.bounds, this.cachedMask);
            } else {
                produceFillAlphas(p2d.getFloatCoordsNoClone(), p2d.getCommandsNoClone(), p2d.getNumCommands(), p2d.getWindingRule() == 1, mxx, mxy, mxt, myx, myy, myt, this.bounds, this.cachedMask);
            }
            int x3 = this.bounds[0];
            int y3 = this.bounds[1];
            int w3 = this.bounds[2] - x3;
            int h3 = this.bounds[3] - y3;
            if (w3 <= 0 || h3 <= 0) {
                return emptyData;
            }
            this.cachedData.update(this.cachedBuffer, x3, y3, w3, h3);
            return this.cachedData;
        } catch (Throwable ex) {
            if (PrismSettings.verbose) {
                ex.printStackTrace();
            }
            Logging.getJavaFXLogger().warning("Cannot rasterize Shape: " + ex.toString());
            return emptyData;
        }
    }
}
