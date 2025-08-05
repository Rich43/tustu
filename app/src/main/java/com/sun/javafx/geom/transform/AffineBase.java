package com.sun.javafx.geom.transform;

import com.sun.javafx.geom.BaseBounds;
import com.sun.javafx.geom.Path2D;
import com.sun.javafx.geom.Point2D;
import com.sun.javafx.geom.RectBounds;
import com.sun.javafx.geom.Rectangle;
import com.sun.javafx.geom.Shape;
import com.sun.javafx.geom.Vec3d;
import com.sun.javafx.geom.transform.BaseTransform;

/* loaded from: jfxrt.jar:com/sun/javafx/geom/transform/AffineBase.class */
public abstract class AffineBase extends BaseTransform {
    protected static final int APPLY_IDENTITY = 0;
    protected static final int APPLY_TRANSLATE = 1;
    protected static final int APPLY_SCALE = 2;
    protected static final int APPLY_SHEAR = 4;
    protected static final int APPLY_3D = 8;
    protected static final int APPLY_2D_MASK = 7;
    protected static final int APPLY_2D_DELTA_MASK = 6;
    protected static final int HI_SHIFT = 4;
    protected static final int HI_IDENTITY = 0;
    protected static final int HI_TRANSLATE = 16;
    protected static final int HI_SCALE = 32;
    protected static final int HI_SHEAR = 64;
    protected static final int HI_3D = 128;
    protected double mxx;
    protected double myx;
    protected double mxy;
    protected double myy;
    protected double mxt;
    protected double myt;
    protected transient int state;
    protected transient int type;
    private static final int[] rot90conversion = {4, 5, 4, 5, 2, 3, 6, 7};

    protected abstract void reset3Delements();

    protected static void stateError() {
        throw new InternalError("missing case in transform state switch");
    }

    protected void updateState() {
        updateState2D();
    }

    protected void updateState2D() {
        if (this.mxy != 0.0d || this.myx != 0.0d) {
            if (this.mxx == 0.0d && this.myy == 0.0d) {
                if (this.mxt == 0.0d && this.myt == 0.0d) {
                    this.state = 4;
                } else {
                    this.state = 5;
                }
            } else if (this.mxt == 0.0d && this.myt == 0.0d) {
                this.state = 6;
            } else {
                this.state = 7;
            }
            this.type = -1;
            return;
        }
        if (this.mxx != 1.0d || this.myy != 1.0d) {
            if (this.mxt == 0.0d && this.myt == 0.0d) {
                this.state = 2;
            } else {
                this.state = 3;
            }
            this.type = -1;
            return;
        }
        if (this.mxt == 0.0d && this.myt == 0.0d) {
            this.state = 0;
            this.type = 0;
        } else {
            this.state = 1;
            this.type = 1;
        }
    }

    @Override // com.sun.javafx.geom.transform.BaseTransform
    public int getType() {
        if (this.type == -1) {
            updateState();
            if (this.type == -1) {
                this.type = calculateType();
            }
        }
        return this.type;
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Removed duplicated region for block: B:12:0x0067  */
    /* JADX WARN: Removed duplicated region for block: B:13:0x006f  */
    /* JADX WARN: Removed duplicated region for block: B:44:0x0131  */
    /* JADX WARN: Removed duplicated region for block: B:45:0x0135  */
    /* JADX WARN: Removed duplicated region for block: B:48:0x0140  */
    /* JADX WARN: Removed duplicated region for block: B:49:0x0144  */
    /* JADX WARN: Removed duplicated region for block: B:52:0x014b  */
    /* JADX WARN: Removed duplicated region for block: B:61:0x0184  */
    /* JADX WARN: Removed duplicated region for block: B:68:0x01ad  */
    /* JADX WARN: Removed duplicated region for block: B:69:0x01b1  */
    /* JADX WARN: Removed duplicated region for block: B:72:0x01bc  */
    /* JADX WARN: Removed duplicated region for block: B:73:0x01c0  */
    /* JADX WARN: Removed duplicated region for block: B:76:0x01c7  */
    /* JADX WARN: Removed duplicated region for block: B:89:0x0214  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    protected int calculateType() {
        /*
            Method dump skipped, instructions count: 598
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.sun.javafx.geom.transform.AffineBase.calculateType():int");
    }

    @Override // com.sun.javafx.geom.transform.BaseTransform
    public double getMxx() {
        return this.mxx;
    }

    @Override // com.sun.javafx.geom.transform.BaseTransform
    public double getMyy() {
        return this.myy;
    }

    @Override // com.sun.javafx.geom.transform.BaseTransform
    public double getMxy() {
        return this.mxy;
    }

    @Override // com.sun.javafx.geom.transform.BaseTransform
    public double getMyx() {
        return this.myx;
    }

    @Override // com.sun.javafx.geom.transform.BaseTransform
    public double getMxt() {
        return this.mxt;
    }

    @Override // com.sun.javafx.geom.transform.BaseTransform
    public double getMyt() {
        return this.myt;
    }

    @Override // com.sun.javafx.geom.transform.BaseTransform
    public boolean isIdentity() {
        return this.state == 0 || getType() == 0;
    }

    @Override // com.sun.javafx.geom.transform.BaseTransform
    public boolean isTranslateOrIdentity() {
        return this.state <= 1 || getType() <= 1;
    }

    @Override // com.sun.javafx.geom.transform.BaseTransform
    public boolean is2D() {
        return this.state < 8 || getType() <= 127;
    }

    @Override // com.sun.javafx.geom.transform.BaseTransform
    public double getDeterminant() {
        switch (this.state) {
            case 0:
            case 1:
                return 1.0d;
            case 2:
            case 3:
                return this.mxx * this.myy;
            case 4:
            case 5:
                return -(this.mxy * this.myx);
            case 6:
            case 7:
                break;
            default:
                stateError();
                break;
        }
        return (this.mxx * this.myy) - (this.mxy * this.myx);
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r4v0, types: [com.sun.javafx.geom.transform.AffineBase] */
    @Override // com.sun.javafx.geom.transform.BaseTransform
    public void setToIdentity() {
        this.myy = 1.0d;
        this.mxx = 1.0d;
        ?? r4 = 0;
        this.myt = 0.0d;
        this.mxt = 0.0d;
        r4.mxy = this;
        this.myx = this;
        reset3Delements();
        this.state = 0;
        this.type = 0;
    }

    public void setTransform(double mxx, double myx, double mxy, double myy, double mxt, double myt) {
        this.mxx = mxx;
        this.myx = myx;
        this.mxy = mxy;
        this.myy = myy;
        this.mxt = mxt;
        this.myt = myt;
        reset3Delements();
        updateState2D();
    }

    public void setToShear(double shx, double shy) {
        this.mxx = 1.0d;
        this.mxy = shx;
        this.myx = shy;
        this.myy = 1.0d;
        this.mxt = 0.0d;
        this.myt = 0.0d;
        reset3Delements();
        if (shx != 0.0d || shy != 0.0d) {
            this.state = 6;
            this.type = -1;
        } else {
            this.state = 0;
            this.type = 0;
        }
    }

    public Point2D transform(Point2D pt) {
        return transform(pt, pt);
    }

    @Override // com.sun.javafx.geom.transform.BaseTransform
    public Point2D transform(Point2D ptSrc, Point2D ptDst) {
        if (ptDst == null) {
            ptDst = new Point2D();
        }
        double x2 = ptSrc.f11907x;
        double y2 = ptSrc.f11908y;
        switch (this.state & 7) {
            case 0:
                ptDst.setLocation((float) x2, (float) y2);
                return ptDst;
            case 1:
                ptDst.setLocation((float) (x2 + this.mxt), (float) (y2 + this.myt));
                return ptDst;
            case 2:
                ptDst.setLocation((float) (x2 * this.mxx), (float) (y2 * this.myy));
                return ptDst;
            case 3:
                ptDst.setLocation((float) ((x2 * this.mxx) + this.mxt), (float) ((y2 * this.myy) + this.myt));
                return ptDst;
            case 4:
                ptDst.setLocation((float) (y2 * this.mxy), (float) (x2 * this.myx));
                return ptDst;
            case 5:
                ptDst.setLocation((float) ((y2 * this.mxy) + this.mxt), (float) ((x2 * this.myx) + this.myt));
                return ptDst;
            case 6:
                ptDst.setLocation((float) ((x2 * this.mxx) + (y2 * this.mxy)), (float) ((x2 * this.myx) + (y2 * this.myy)));
                return ptDst;
            case 7:
                break;
            default:
                stateError();
                break;
        }
        ptDst.setLocation((float) ((x2 * this.mxx) + (y2 * this.mxy) + this.mxt), (float) ((x2 * this.myx) + (y2 * this.myy) + this.myt));
        return ptDst;
    }

    @Override // com.sun.javafx.geom.transform.BaseTransform, com.sun.javafx.geom.transform.CanTransformVec3d
    public Vec3d transform(Vec3d src, Vec3d dst) {
        if (dst == null) {
            dst = new Vec3d();
        }
        double x2 = src.f11930x;
        double y2 = src.f11931y;
        double z2 = src.f11932z;
        switch (this.state) {
            case 0:
                dst.f11930x = x2;
                dst.f11931y = y2;
                dst.f11932z = z2;
                return dst;
            case 1:
                dst.f11930x = x2 + this.mxt;
                dst.f11931y = y2 + this.myt;
                dst.f11932z = z2;
                return dst;
            case 2:
                dst.f11930x = x2 * this.mxx;
                dst.f11931y = y2 * this.myy;
                dst.f11932z = z2;
                return dst;
            case 3:
                dst.f11930x = (x2 * this.mxx) + this.mxt;
                dst.f11931y = (y2 * this.myy) + this.myt;
                dst.f11932z = z2;
                return dst;
            case 4:
                dst.f11930x = y2 * this.mxy;
                dst.f11931y = x2 * this.myx;
                dst.f11932z = z2;
                return dst;
            case 5:
                dst.f11930x = (y2 * this.mxy) + this.mxt;
                dst.f11931y = (x2 * this.myx) + this.myt;
                dst.f11932z = z2;
                return dst;
            case 6:
                dst.f11930x = (x2 * this.mxx) + (y2 * this.mxy);
                dst.f11931y = (x2 * this.myx) + (y2 * this.myy);
                dst.f11932z = z2;
                return dst;
            case 7:
                break;
            default:
                stateError();
                break;
        }
        dst.f11930x = (x2 * this.mxx) + (y2 * this.mxy) + this.mxt;
        dst.f11931y = (x2 * this.myx) + (y2 * this.myy) + this.myt;
        dst.f11932z = z2;
        return dst;
    }

    @Override // com.sun.javafx.geom.transform.BaseTransform
    public Vec3d deltaTransform(Vec3d src, Vec3d dst) {
        if (dst == null) {
            dst = new Vec3d();
        }
        double x2 = src.f11930x;
        double y2 = src.f11931y;
        double z2 = src.f11932z;
        switch (this.state) {
            case 0:
            case 1:
                dst.f11930x = x2;
                dst.f11931y = y2;
                dst.f11932z = z2;
                return dst;
            case 2:
            case 3:
                dst.f11930x = x2 * this.mxx;
                dst.f11931y = y2 * this.myy;
                dst.f11932z = z2;
                return dst;
            case 4:
            case 5:
                dst.f11930x = y2 * this.mxy;
                dst.f11931y = x2 * this.myx;
                dst.f11932z = z2;
                return dst;
            case 6:
            case 7:
                break;
            default:
                stateError();
                break;
        }
        dst.f11930x = (x2 * this.mxx) + (y2 * this.mxy);
        dst.f11931y = (x2 * this.myx) + (y2 * this.myy);
        dst.f11932z = z2;
        return dst;
    }

    private BaseBounds transform2DBounds(RectBounds src, RectBounds dst) {
        switch (this.state & 7) {
            case 0:
                if (src != dst) {
                    dst.setBounds(src);
                    break;
                }
                break;
            case 1:
                dst.setBounds((float) (src.getMinX() + this.mxt), (float) (src.getMinY() + this.myt), (float) (src.getMaxX() + this.mxt), (float) (src.getMaxY() + this.myt));
                break;
            case 2:
                dst.setBoundsAndSort((float) (src.getMinX() * this.mxx), (float) (src.getMinY() * this.myy), (float) (src.getMaxX() * this.mxx), (float) (src.getMaxY() * this.myy));
                break;
            case 3:
                dst.setBoundsAndSort((float) ((src.getMinX() * this.mxx) + this.mxt), (float) ((src.getMinY() * this.myy) + this.myt), (float) ((src.getMaxX() * this.mxx) + this.mxt), (float) ((src.getMaxY() * this.myy) + this.myt));
                break;
            case 4:
                dst.setBoundsAndSort((float) (src.getMinY() * this.mxy), (float) (src.getMinX() * this.myx), (float) (src.getMaxY() * this.mxy), (float) (src.getMaxX() * this.myx));
                break;
            case 5:
                dst.setBoundsAndSort((float) ((src.getMinY() * this.mxy) + this.mxt), (float) ((src.getMinX() * this.myx) + this.myt), (float) ((src.getMaxY() * this.mxy) + this.mxt), (float) ((src.getMaxX() * this.myx) + this.myt));
                break;
            default:
                stateError();
            case 6:
            case 7:
                double x1 = src.getMinX();
                double y1 = src.getMinY();
                double x2 = src.getMaxX();
                double y2 = src.getMaxY();
                dst.setBoundsAndSort((float) ((x1 * this.mxx) + (y1 * this.mxy)), (float) ((x1 * this.myx) + (y1 * this.myy)), (float) ((x2 * this.mxx) + (y2 * this.mxy)), (float) ((x2 * this.myx) + (y2 * this.myy)));
                dst.add((float) ((x1 * this.mxx) + (y2 * this.mxy)), (float) ((x1 * this.myx) + (y2 * this.myy)));
                dst.add((float) ((x2 * this.mxx) + (y1 * this.mxy)), (float) ((x2 * this.myx) + (y1 * this.myy)));
                dst.setBounds((float) (dst.getMinX() + this.mxt), (float) (dst.getMinY() + this.myt), (float) (dst.getMaxX() + this.mxt), (float) (dst.getMaxY() + this.myt));
                break;
        }
        return dst;
    }

    private BaseBounds transform3DBounds(BaseBounds src, BaseBounds dst) {
        switch (this.state & 7) {
            case 0:
                if (src != dst) {
                    dst = dst.deriveWithNewBounds(src);
                    break;
                }
                break;
            case 1:
                dst = dst.deriveWithNewBounds((float) (src.getMinX() + this.mxt), (float) (src.getMinY() + this.myt), src.getMinZ(), (float) (src.getMaxX() + this.mxt), (float) (src.getMaxY() + this.myt), src.getMaxZ());
                break;
            case 2:
                dst = dst.deriveWithNewBoundsAndSort((float) (src.getMinX() * this.mxx), (float) (src.getMinY() * this.myy), src.getMinZ(), (float) (src.getMaxX() * this.mxx), (float) (src.getMaxY() * this.myy), src.getMaxZ());
                break;
            case 3:
                dst = dst.deriveWithNewBoundsAndSort((float) ((src.getMinX() * this.mxx) + this.mxt), (float) ((src.getMinY() * this.myy) + this.myt), src.getMinZ(), (float) ((src.getMaxX() * this.mxx) + this.mxt), (float) ((src.getMaxY() * this.myy) + this.myt), src.getMaxZ());
                break;
            case 4:
                dst = dst.deriveWithNewBoundsAndSort((float) (src.getMinY() * this.mxy), (float) (src.getMinX() * this.myx), src.getMinZ(), (float) (src.getMaxY() * this.mxy), (float) (src.getMaxX() * this.myx), src.getMaxZ());
                break;
            case 5:
                dst = dst.deriveWithNewBoundsAndSort((float) ((src.getMinY() * this.mxy) + this.mxt), (float) ((src.getMinX() * this.myx) + this.myt), src.getMinZ(), (float) ((src.getMaxY() * this.mxy) + this.mxt), (float) ((src.getMaxX() * this.myx) + this.myt), src.getMaxZ());
                break;
            default:
                stateError();
            case 6:
            case 7:
                double x1 = src.getMinX();
                double y1 = src.getMinY();
                double z1 = src.getMinZ();
                double x2 = src.getMaxX();
                double y2 = src.getMaxY();
                double z2 = src.getMaxZ();
                dst.setBoundsAndSort((float) ((x1 * this.mxx) + (y1 * this.mxy)), (float) ((x1 * this.myx) + (y1 * this.myy)), (float) z1, (float) ((x2 * this.mxx) + (y2 * this.mxy)), (float) ((x2 * this.myx) + (y2 * this.myy)), (float) z2);
                dst.add((float) ((x1 * this.mxx) + (y2 * this.mxy)), (float) ((x1 * this.myx) + (y2 * this.myy)), 0.0f);
                dst.add((float) ((x2 * this.mxx) + (y1 * this.mxy)), (float) ((x2 * this.myx) + (y1 * this.myy)), 0.0f);
                dst.deriveWithNewBounds((float) (dst.getMinX() + this.mxt), (float) (dst.getMinY() + this.myt), dst.getMinZ(), (float) (dst.getMaxX() + this.mxt), (float) (dst.getMaxY() + this.myt), dst.getMaxZ());
                break;
        }
        return dst;
    }

    @Override // com.sun.javafx.geom.transform.BaseTransform
    public BaseBounds transform(BaseBounds src, BaseBounds dst) {
        if (!src.is2D() || !dst.is2D()) {
            return transform3DBounds(src, dst);
        }
        return transform2DBounds((RectBounds) src, (RectBounds) dst);
    }

    @Override // com.sun.javafx.geom.transform.BaseTransform
    public void transform(Rectangle src, Rectangle dst) {
        switch (this.state & 7) {
            case 0:
                if (dst != src) {
                    dst.setBounds(src);
                    return;
                }
                return;
            case 1:
                Translate2D.transform(src, dst, this.mxt, this.myt);
                return;
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
                break;
            default:
                stateError();
                break;
        }
        RectBounds b2 = new RectBounds(src);
        dst.setBounds((RectBounds) transform(b2, b2));
    }

    @Override // com.sun.javafx.geom.transform.BaseTransform
    public void transform(float[] srcPts, int srcOff, float[] dstPts, int dstOff, int numPts) {
        doTransform(srcPts, srcOff, dstPts, dstOff, numPts, this.state & 7);
    }

    @Override // com.sun.javafx.geom.transform.BaseTransform
    public void deltaTransform(float[] srcPts, int srcOff, float[] dstPts, int dstOff, int numPts) {
        doTransform(srcPts, srcOff, dstPts, dstOff, numPts, this.state & 6);
    }

    private void doTransform(float[] srcPts, int srcOff, float[] dstPts, int dstOff, int numPts, int thestate) {
        if (dstPts == srcPts && dstOff > srcOff && dstOff < srcOff + (numPts * 2)) {
            System.arraycopy(srcPts, srcOff, dstPts, dstOff, numPts * 2);
            srcOff = dstOff;
        }
        switch (thestate) {
            case 0:
                if (srcPts != dstPts || srcOff != dstOff) {
                    System.arraycopy(srcPts, srcOff, dstPts, dstOff, numPts * 2);
                    return;
                }
                return;
            case 1:
                double Mxt = this.mxt;
                double Myt = this.myt;
                while (true) {
                    numPts--;
                    if (numPts >= 0) {
                        int i2 = dstOff;
                        int dstOff2 = dstOff + 1;
                        int i3 = srcOff;
                        dstPts[i2] = (float) (srcPts[i3] + Mxt);
                        dstOff = dstOff2 + 1;
                        srcOff = srcOff + 1 + 1;
                        dstPts[dstOff2] = (float) (srcPts[r11] + Myt);
                    } else {
                        return;
                    }
                }
            case 2:
                double Mxx = this.mxx;
                double Myy = this.myy;
                while (true) {
                    numPts--;
                    if (numPts >= 0) {
                        int i4 = dstOff;
                        int dstOff3 = dstOff + 1;
                        int i5 = srcOff;
                        dstPts[i4] = (float) (Mxx * srcPts[i5]);
                        dstOff = dstOff3 + 1;
                        srcOff = srcOff + 1 + 1;
                        dstPts[dstOff3] = (float) (Myy * srcPts[r11]);
                    } else {
                        return;
                    }
                }
            case 3:
                double Mxx2 = this.mxx;
                double Mxt2 = this.mxt;
                double Myy2 = this.myy;
                double Myt2 = this.myt;
                while (true) {
                    numPts--;
                    if (numPts >= 0) {
                        int i6 = dstOff;
                        int dstOff4 = dstOff + 1;
                        int i7 = srcOff;
                        dstPts[i6] = (float) ((Mxx2 * srcPts[i7]) + Mxt2);
                        dstOff = dstOff4 + 1;
                        srcOff = srcOff + 1 + 1;
                        dstPts[dstOff4] = (float) ((Myy2 * srcPts[r11]) + Myt2);
                    } else {
                        return;
                    }
                }
            case 4:
                double Mxy = this.mxy;
                double Myx = this.myx;
                while (true) {
                    numPts--;
                    if (numPts >= 0) {
                        int i8 = srcOff;
                        double x2 = srcPts[i8];
                        int i9 = dstOff;
                        int dstOff5 = dstOff + 1;
                        srcOff = srcOff + 1 + 1;
                        dstPts[i9] = (float) (Mxy * srcPts[r11]);
                        dstOff = dstOff5 + 1;
                        dstPts[dstOff5] = (float) (Myx * x2);
                    } else {
                        return;
                    }
                }
            case 5:
                double Mxy2 = this.mxy;
                double Mxt3 = this.mxt;
                double Myx2 = this.myx;
                double Myt3 = this.myt;
                while (true) {
                    numPts--;
                    if (numPts >= 0) {
                        int i10 = srcOff;
                        double x3 = srcPts[i10];
                        int i11 = dstOff;
                        int dstOff6 = dstOff + 1;
                        srcOff = srcOff + 1 + 1;
                        dstPts[i11] = (float) ((Mxy2 * srcPts[r11]) + Mxt3);
                        dstOff = dstOff6 + 1;
                        dstPts[dstOff6] = (float) ((Myx2 * x3) + Myt3);
                    } else {
                        return;
                    }
                }
            case 6:
                double Mxx3 = this.mxx;
                double Mxy3 = this.mxy;
                double Myx3 = this.myx;
                double Myy3 = this.myy;
                while (true) {
                    numPts--;
                    if (numPts >= 0) {
                        int i12 = srcOff;
                        int srcOff2 = srcOff + 1;
                        double x4 = srcPts[i12];
                        srcOff = srcOff2 + 1;
                        double y2 = srcPts[srcOff2];
                        int i13 = dstOff;
                        int dstOff7 = dstOff + 1;
                        dstPts[i13] = (float) ((Mxx3 * x4) + (Mxy3 * y2));
                        dstOff = dstOff7 + 1;
                        dstPts[dstOff7] = (float) ((Myx3 * x4) + (Myy3 * y2));
                    } else {
                        return;
                    }
                }
            case 7:
                break;
            default:
                stateError();
                break;
        }
        double Mxx4 = this.mxx;
        double Mxy4 = this.mxy;
        double Mxt4 = this.mxt;
        double Myx4 = this.myx;
        double Myy4 = this.myy;
        double Myt4 = this.myt;
        while (true) {
            numPts--;
            if (numPts >= 0) {
                int i14 = srcOff;
                int srcOff3 = srcOff + 1;
                double x5 = srcPts[i14];
                srcOff = srcOff3 + 1;
                double y3 = srcPts[srcOff3];
                int i15 = dstOff;
                int dstOff8 = dstOff + 1;
                dstPts[i15] = (float) ((Mxx4 * x5) + (Mxy4 * y3) + Mxt4);
                dstOff = dstOff8 + 1;
                dstPts[dstOff8] = (float) ((Myx4 * x5) + (Myy4 * y3) + Myt4);
            } else {
                return;
            }
        }
    }

    @Override // com.sun.javafx.geom.transform.BaseTransform
    public void transform(double[] srcPts, int srcOff, double[] dstPts, int dstOff, int numPts) {
        doTransform(srcPts, srcOff, dstPts, dstOff, numPts, this.state & 7);
    }

    @Override // com.sun.javafx.geom.transform.BaseTransform
    public void deltaTransform(double[] srcPts, int srcOff, double[] dstPts, int dstOff, int numPts) {
        doTransform(srcPts, srcOff, dstPts, dstOff, numPts, this.state & 6);
    }

    private void doTransform(double[] srcPts, int srcOff, double[] dstPts, int dstOff, int numPts, int thestate) {
        if (dstPts == srcPts && dstOff > srcOff && dstOff < srcOff + (numPts * 2)) {
            System.arraycopy(srcPts, srcOff, dstPts, dstOff, numPts * 2);
            srcOff = dstOff;
        }
        switch (thestate) {
            case 0:
                if (srcPts != dstPts || srcOff != dstOff) {
                    System.arraycopy(srcPts, srcOff, dstPts, dstOff, numPts * 2);
                    return;
                }
                return;
            case 1:
                double Mxt = this.mxt;
                double Myt = this.myt;
                while (true) {
                    numPts--;
                    if (numPts >= 0) {
                        int i2 = dstOff;
                        int dstOff2 = dstOff + 1;
                        int i3 = srcOff;
                        int srcOff2 = srcOff + 1;
                        dstPts[i2] = srcPts[i3] + Mxt;
                        dstOff = dstOff2 + 1;
                        srcOff = srcOff2 + 1;
                        dstPts[dstOff2] = srcPts[srcOff2] + Myt;
                    } else {
                        return;
                    }
                }
            case 2:
                double Mxx = this.mxx;
                double Myy = this.myy;
                while (true) {
                    numPts--;
                    if (numPts >= 0) {
                        int i4 = dstOff;
                        int dstOff3 = dstOff + 1;
                        int i5 = srcOff;
                        int srcOff3 = srcOff + 1;
                        dstPts[i4] = Mxx * srcPts[i5];
                        dstOff = dstOff3 + 1;
                        srcOff = srcOff3 + 1;
                        dstPts[dstOff3] = Myy * srcPts[srcOff3];
                    } else {
                        return;
                    }
                }
            case 3:
                double Mxx2 = this.mxx;
                double Mxt2 = this.mxt;
                double Myy2 = this.myy;
                double Myt2 = this.myt;
                while (true) {
                    numPts--;
                    if (numPts >= 0) {
                        int i6 = dstOff;
                        int dstOff4 = dstOff + 1;
                        int i7 = srcOff;
                        int srcOff4 = srcOff + 1;
                        dstPts[i6] = (Mxx2 * srcPts[i7]) + Mxt2;
                        dstOff = dstOff4 + 1;
                        srcOff = srcOff4 + 1;
                        dstPts[dstOff4] = (Myy2 * srcPts[srcOff4]) + Myt2;
                    } else {
                        return;
                    }
                }
            case 4:
                double Mxy = this.mxy;
                double Myx = this.myx;
                while (true) {
                    numPts--;
                    if (numPts >= 0) {
                        int i8 = srcOff;
                        int srcOff5 = srcOff + 1;
                        double x2 = srcPts[i8];
                        int i9 = dstOff;
                        int dstOff5 = dstOff + 1;
                        srcOff = srcOff5 + 1;
                        dstPts[i9] = Mxy * srcPts[srcOff5];
                        dstOff = dstOff5 + 1;
                        dstPts[dstOff5] = Myx * x2;
                    } else {
                        return;
                    }
                }
            case 5:
                double Mxy2 = this.mxy;
                double Mxt3 = this.mxt;
                double Myx2 = this.myx;
                double Myt3 = this.myt;
                while (true) {
                    numPts--;
                    if (numPts >= 0) {
                        int i10 = srcOff;
                        int srcOff6 = srcOff + 1;
                        double x3 = srcPts[i10];
                        int i11 = dstOff;
                        int dstOff6 = dstOff + 1;
                        srcOff = srcOff6 + 1;
                        dstPts[i11] = (Mxy2 * srcPts[srcOff6]) + Mxt3;
                        dstOff = dstOff6 + 1;
                        dstPts[dstOff6] = (Myx2 * x3) + Myt3;
                    } else {
                        return;
                    }
                }
            case 6:
                double Mxx3 = this.mxx;
                double Mxy3 = this.mxy;
                double Myx3 = this.myx;
                double Myy3 = this.myy;
                while (true) {
                    numPts--;
                    if (numPts >= 0) {
                        int i12 = srcOff;
                        int srcOff7 = srcOff + 1;
                        double x4 = srcPts[i12];
                        srcOff = srcOff7 + 1;
                        double y2 = srcPts[srcOff7];
                        int i13 = dstOff;
                        int dstOff7 = dstOff + 1;
                        dstPts[i13] = (Mxx3 * x4) + (Mxy3 * y2);
                        dstOff = dstOff7 + 1;
                        dstPts[dstOff7] = (Myx3 * x4) + (Myy3 * y2);
                    } else {
                        return;
                    }
                }
            case 7:
                break;
            default:
                stateError();
                break;
        }
        double Mxx4 = this.mxx;
        double Mxy4 = this.mxy;
        double Mxt4 = this.mxt;
        double Myx4 = this.myx;
        double Myy4 = this.myy;
        double Myt4 = this.myt;
        while (true) {
            numPts--;
            if (numPts >= 0) {
                int i14 = srcOff;
                int srcOff8 = srcOff + 1;
                double x5 = srcPts[i14];
                srcOff = srcOff8 + 1;
                double y3 = srcPts[srcOff8];
                int i15 = dstOff;
                int dstOff8 = dstOff + 1;
                dstPts[i15] = (Mxx4 * x5) + (Mxy4 * y3) + Mxt4;
                dstOff = dstOff8 + 1;
                dstPts[dstOff8] = (Myx4 * x5) + (Myy4 * y3) + Myt4;
            } else {
                return;
            }
        }
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Removed duplicated region for block: B:44:0x00a2 A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:8:0x0063 A[LOOP:0: B:6:0x005b->B:8:0x0063, LOOP_END] */
    @Override // com.sun.javafx.geom.transform.BaseTransform
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public void transform(float[] r10, int r11, double[] r12, int r13, int r14) {
        /*
            Method dump skipped, instructions count: 627
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.sun.javafx.geom.transform.AffineBase.transform(float[], int, double[], int, int):void");
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Removed duplicated region for block: B:44:0x00a2 A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:8:0x0063 A[LOOP:0: B:6:0x005b->B:8:0x0063, LOOP_END] */
    @Override // com.sun.javafx.geom.transform.BaseTransform
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public void transform(double[] r10, int r11, float[] r12, int r13, int r14) {
        /*
            Method dump skipped, instructions count: 627
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.sun.javafx.geom.transform.AffineBase.transform(double[], int, float[], int, int):void");
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Removed duplicated region for block: B:11:0x007b  */
    @Override // com.sun.javafx.geom.transform.BaseTransform
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public com.sun.javafx.geom.Point2D inverseTransform(com.sun.javafx.geom.Point2D r10, com.sun.javafx.geom.Point2D r11) throws com.sun.javafx.geom.transform.NoninvertibleTransformException {
        /*
            Method dump skipped, instructions count: 367
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.sun.javafx.geom.transform.AffineBase.inverseTransform(com.sun.javafx.geom.Point2D, com.sun.javafx.geom.Point2D):com.sun.javafx.geom.Point2D");
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Removed duplicated region for block: B:11:0x007f  */
    @Override // com.sun.javafx.geom.transform.BaseTransform
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public com.sun.javafx.geom.Vec3d inverseTransform(com.sun.javafx.geom.Vec3d r11, com.sun.javafx.geom.Vec3d r12) throws com.sun.javafx.geom.transform.NoninvertibleTransformException {
        /*
            Method dump skipped, instructions count: 371
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.sun.javafx.geom.transform.AffineBase.inverseTransform(com.sun.javafx.geom.Vec3d, com.sun.javafx.geom.Vec3d):com.sun.javafx.geom.Vec3d");
    }

    @Override // com.sun.javafx.geom.transform.BaseTransform
    public Vec3d inverseDeltaTransform(Vec3d src, Vec3d dst) throws NoninvertibleTransformException {
        if (dst == null) {
            dst = new Vec3d();
        }
        double x2 = src.f11930x;
        double y2 = src.f11931y;
        double z2 = src.f11932z;
        switch (this.state) {
            case 0:
            case 1:
                dst.set(x2, y2, z2);
                return dst;
            case 2:
            case 3:
                if (this.mxx == 0.0d || this.myy == 0.0d) {
                    throw new NoninvertibleTransformException("Determinant is 0");
                }
                dst.set(x2 / this.mxx, y2 / this.myy, z2);
                return dst;
            case 4:
            case 5:
                if (this.mxy == 0.0d || this.myx == 0.0d) {
                    throw new NoninvertibleTransformException("Determinant is 0");
                }
                dst.set(y2 / this.myx, x2 / this.mxy, z2);
                return dst;
            case 6:
            case 7:
                break;
            default:
                stateError();
                break;
        }
        double det = (this.mxx * this.myy) - (this.mxy * this.myx);
        if (det == 0.0d || Math.abs(det) <= Double.MIN_VALUE) {
            throw new NoninvertibleTransformException("Determinant is " + det);
        }
        dst.set(((x2 * this.myy) - (y2 * this.mxy)) / det, ((y2 * this.mxx) - (x2 * this.myx)) / det, z2);
        return dst;
    }

    private BaseBounds inversTransform2DBounds(RectBounds src, RectBounds dst) throws NoninvertibleTransformException {
        switch (this.state) {
            case 0:
                if (dst != src) {
                    dst.setBounds(src);
                }
                return dst;
            case 1:
                dst.setBounds((float) (src.getMinX() - this.mxt), (float) (src.getMinY() - this.myt), (float) (src.getMaxX() - this.mxt), (float) (src.getMaxY() - this.myt));
                return dst;
            case 2:
                if (this.mxx == 0.0d || this.myy == 0.0d) {
                    throw new NoninvertibleTransformException("Determinant is 0");
                }
                dst.setBoundsAndSort((float) (src.getMinX() / this.mxx), (float) (src.getMinY() / this.myy), (float) (src.getMaxX() / this.mxx), (float) (src.getMaxY() / this.myy));
                return dst;
            case 3:
                if (this.mxx == 0.0d || this.myy == 0.0d) {
                    throw new NoninvertibleTransformException("Determinant is 0");
                }
                dst.setBoundsAndSort((float) ((src.getMinX() - this.mxt) / this.mxx), (float) ((src.getMinY() - this.myt) / this.myy), (float) ((src.getMaxX() - this.mxt) / this.mxx), (float) ((src.getMaxY() - this.myt) / this.myy));
                return dst;
            case 4:
                if (this.mxy == 0.0d || this.myx == 0.0d) {
                    throw new NoninvertibleTransformException("Determinant is 0");
                }
                dst.setBoundsAndSort((float) (src.getMinY() / this.myx), (float) (src.getMinX() / this.mxy), (float) (src.getMaxY() / this.myx), (float) (src.getMaxX() / this.mxy));
                return dst;
            case 5:
                if (this.mxy == 0.0d || this.myx == 0.0d) {
                    throw new NoninvertibleTransformException("Determinant is 0");
                }
                dst.setBoundsAndSort((float) ((src.getMinY() - this.myt) / this.myx), (float) ((src.getMinX() - this.mxt) / this.mxy), (float) ((src.getMaxY() - this.myt) / this.myx), (float) ((src.getMaxX() - this.mxt) / this.mxy));
                return dst;
            default:
                stateError();
            case 6:
            case 7:
                double det = (this.mxx * this.myy) - (this.mxy * this.myx);
                if (det == 0.0d || Math.abs(det) <= Double.MIN_VALUE) {
                    throw new NoninvertibleTransformException("Determinant is " + det);
                }
                double x1 = src.getMinX() - this.mxt;
                double y1 = src.getMinY() - this.myt;
                double x2 = src.getMaxX() - this.mxt;
                double y2 = src.getMaxY() - this.myt;
                dst.setBoundsAndSort((float) (((x1 * this.myy) - (y1 * this.mxy)) / det), (float) (((y1 * this.mxx) - (x1 * this.myx)) / det), (float) (((x2 * this.myy) - (y2 * this.mxy)) / det), (float) (((y2 * this.mxx) - (x2 * this.myx)) / det));
                dst.add((float) (((x2 * this.myy) - (y1 * this.mxy)) / det), (float) (((y1 * this.mxx) - (x2 * this.myx)) / det));
                dst.add((float) (((x1 * this.myy) - (y2 * this.mxy)) / det), (float) (((y2 * this.mxx) - (x1 * this.myx)) / det));
                return dst;
        }
    }

    private BaseBounds inversTransform3DBounds(BaseBounds src, BaseBounds dst) throws NoninvertibleTransformException {
        switch (this.state) {
            case 0:
                if (dst != src) {
                    dst = dst.deriveWithNewBounds(src);
                }
                return dst;
            case 1:
                dst = dst.deriveWithNewBounds((float) (src.getMinX() - this.mxt), (float) (src.getMinY() - this.myt), src.getMinZ(), (float) (src.getMaxX() - this.mxt), (float) (src.getMaxY() - this.myt), src.getMaxZ());
                return dst;
            case 2:
                if (this.mxx == 0.0d || this.myy == 0.0d) {
                    throw new NoninvertibleTransformException("Determinant is 0");
                }
                dst = dst.deriveWithNewBoundsAndSort((float) (src.getMinX() / this.mxx), (float) (src.getMinY() / this.myy), src.getMinZ(), (float) (src.getMaxX() / this.mxx), (float) (src.getMaxY() / this.myy), src.getMaxZ());
                return dst;
            case 3:
                if (this.mxx == 0.0d || this.myy == 0.0d) {
                    throw new NoninvertibleTransformException("Determinant is 0");
                }
                dst = dst.deriveWithNewBoundsAndSort((float) ((src.getMinX() - this.mxt) / this.mxx), (float) ((src.getMinY() - this.myt) / this.myy), src.getMinZ(), (float) ((src.getMaxX() - this.mxt) / this.mxx), (float) ((src.getMaxY() - this.myt) / this.myy), src.getMaxZ());
                return dst;
            default:
                stateError();
            case 4:
            case 5:
            case 6:
            case 7:
                double det = (this.mxx * this.myy) - (this.mxy * this.myx);
                if (det == 0.0d || Math.abs(det) <= Double.MIN_VALUE) {
                    throw new NoninvertibleTransformException("Determinant is " + det);
                }
                double x1 = src.getMinX() - this.mxt;
                double y1 = src.getMinY() - this.myt;
                double z1 = src.getMinZ();
                double x2 = src.getMaxX() - this.mxt;
                double y2 = src.getMaxY() - this.myt;
                double z2 = src.getMaxZ();
                BaseBounds dst2 = dst.deriveWithNewBoundsAndSort((float) (((x1 * this.myy) - (y1 * this.mxy)) / det), (float) (((y1 * this.mxx) - (x1 * this.myx)) / det), (float) (z1 / det), (float) (((x2 * this.myy) - (y2 * this.mxy)) / det), (float) (((y2 * this.mxx) - (x2 * this.myx)) / det), (float) (z2 / det));
                dst2.add((float) (((x2 * this.myy) - (y1 * this.mxy)) / det), (float) (((y1 * this.mxx) - (x2 * this.myx)) / det), 0.0f);
                dst2.add((float) (((x1 * this.myy) - (y2 * this.mxy)) / det), (float) (((y2 * this.mxx) - (x1 * this.myx)) / det), 0.0f);
                return dst2;
        }
    }

    @Override // com.sun.javafx.geom.transform.BaseTransform
    public BaseBounds inverseTransform(BaseBounds src, BaseBounds dst) throws NoninvertibleTransformException {
        if (!src.is2D() || !dst.is2D()) {
            return inversTransform3DBounds(src, dst);
        }
        return inversTransform2DBounds((RectBounds) src, (RectBounds) dst);
    }

    @Override // com.sun.javafx.geom.transform.BaseTransform
    public void inverseTransform(Rectangle src, Rectangle dst) throws NoninvertibleTransformException {
        switch (this.state) {
            case 0:
                if (dst != src) {
                    dst.setBounds(src);
                    return;
                }
                return;
            case 1:
                Translate2D.transform(src, dst, -this.mxt, -this.myt);
                return;
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
                break;
            default:
                stateError();
                break;
        }
        RectBounds b2 = new RectBounds(src);
        dst.setBounds((RectBounds) inverseTransform(b2, b2));
    }

    @Override // com.sun.javafx.geom.transform.BaseTransform
    public void inverseTransform(float[] srcPts, int srcOff, float[] dstPts, int dstOff, int numPts) throws NoninvertibleTransformException {
        doInverseTransform(srcPts, srcOff, dstPts, dstOff, numPts, this.state);
    }

    @Override // com.sun.javafx.geom.transform.BaseTransform
    public void inverseDeltaTransform(float[] srcPts, int srcOff, float[] dstPts, int dstOff, int numPts) throws NoninvertibleTransformException {
        doInverseTransform(srcPts, srcOff, dstPts, dstOff, numPts, this.state & (-2));
    }

    private void doInverseTransform(float[] srcPts, int srcOff, float[] dstPts, int dstOff, int numPts, int thestate) throws NoninvertibleTransformException {
        if (dstPts == srcPts && dstOff > srcOff && dstOff < srcOff + (numPts * 2)) {
            System.arraycopy(srcPts, srcOff, dstPts, dstOff, numPts * 2);
            srcOff = dstOff;
        }
        switch (thestate) {
            case 0:
                if (srcPts != dstPts || srcOff != dstOff) {
                    System.arraycopy(srcPts, srcOff, dstPts, dstOff, numPts * 2);
                    return;
                }
                return;
            case 1:
                double Mxt = this.mxt;
                double Myt = this.myt;
                while (true) {
                    numPts--;
                    if (numPts >= 0) {
                        int i2 = dstOff;
                        int dstOff2 = dstOff + 1;
                        int i3 = srcOff;
                        dstPts[i2] = (float) (srcPts[i3] - Mxt);
                        dstOff = dstOff2 + 1;
                        srcOff = srcOff + 1 + 1;
                        dstPts[dstOff2] = (float) (srcPts[r11] - Myt);
                    } else {
                        return;
                    }
                }
            case 2:
                double Mxx = this.mxx;
                double Myy = this.myy;
                if (Mxx == 0.0d || Myy == 0.0d) {
                    throw new NoninvertibleTransformException("Determinant is 0");
                }
                while (true) {
                    numPts--;
                    if (numPts >= 0) {
                        int i4 = dstOff;
                        int dstOff3 = dstOff + 1;
                        int i5 = srcOff;
                        dstPts[i4] = (float) (srcPts[i5] / Mxx);
                        dstOff = dstOff3 + 1;
                        srcOff = srcOff + 1 + 1;
                        dstPts[dstOff3] = (float) (srcPts[r11] / Myy);
                    } else {
                        return;
                    }
                }
                break;
            case 3:
                double Mxx2 = this.mxx;
                double Mxt2 = this.mxt;
                double Myy2 = this.myy;
                double Myt2 = this.myt;
                if (Mxx2 == 0.0d || Myy2 == 0.0d) {
                    throw new NoninvertibleTransformException("Determinant is 0");
                }
                while (true) {
                    numPts--;
                    if (numPts >= 0) {
                        int i6 = dstOff;
                        int dstOff4 = dstOff + 1;
                        int i7 = srcOff;
                        dstPts[i6] = (float) ((srcPts[i7] - Mxt2) / Mxx2);
                        dstOff = dstOff4 + 1;
                        srcOff = srcOff + 1 + 1;
                        dstPts[dstOff4] = (float) ((srcPts[r11] - Myt2) / Myy2);
                    } else {
                        return;
                    }
                }
                break;
            case 4:
                double Mxy = this.mxy;
                double Myx = this.myx;
                if (Mxy == 0.0d || Myx == 0.0d) {
                    throw new NoninvertibleTransformException("Determinant is 0");
                }
                while (true) {
                    numPts--;
                    if (numPts >= 0) {
                        int i8 = srcOff;
                        double x2 = srcPts[i8];
                        int i9 = dstOff;
                        int dstOff5 = dstOff + 1;
                        srcOff = srcOff + 1 + 1;
                        dstPts[i9] = (float) (srcPts[r11] / Myx);
                        dstOff = dstOff5 + 1;
                        dstPts[dstOff5] = (float) (x2 / Mxy);
                    } else {
                        return;
                    }
                }
                break;
            case 5:
                double Mxy2 = this.mxy;
                double Mxt3 = this.mxt;
                double Myx2 = this.myx;
                double Myt3 = this.myt;
                if (Mxy2 == 0.0d || Myx2 == 0.0d) {
                    throw new NoninvertibleTransformException("Determinant is 0");
                }
                while (true) {
                    numPts--;
                    if (numPts >= 0) {
                        int i10 = srcOff;
                        double x3 = srcPts[i10] - Mxt3;
                        int i11 = dstOff;
                        int dstOff6 = dstOff + 1;
                        srcOff = srcOff + 1 + 1;
                        dstPts[i11] = (float) ((srcPts[r11] - Myt3) / Myx2);
                        dstOff = dstOff6 + 1;
                        dstPts[dstOff6] = (float) (x3 / Mxy2);
                    } else {
                        return;
                    }
                }
                break;
            case 6:
                double Mxx3 = this.mxx;
                double Mxy3 = this.mxy;
                double Myx3 = this.myx;
                double Myy3 = this.myy;
                double det = (Mxx3 * Myy3) - (Mxy3 * Myx3);
                if (det == 0.0d || Math.abs(det) <= Double.MIN_VALUE) {
                    throw new NoninvertibleTransformException("Determinant is " + det);
                }
                while (true) {
                    numPts--;
                    if (numPts >= 0) {
                        int i12 = srcOff;
                        int srcOff2 = srcOff + 1;
                        double x4 = srcPts[i12];
                        srcOff = srcOff2 + 1;
                        double y2 = srcPts[srcOff2];
                        int i13 = dstOff;
                        int dstOff7 = dstOff + 1;
                        dstPts[i13] = (float) (((x4 * Myy3) - (y2 * Mxy3)) / det);
                        dstOff = dstOff7 + 1;
                        dstPts[dstOff7] = (float) (((y2 * Mxx3) - (x4 * Myx3)) / det);
                    } else {
                        return;
                    }
                }
                break;
            case 7:
                break;
            default:
                stateError();
                break;
        }
        double Mxx4 = this.mxx;
        double Mxy4 = this.mxy;
        double Mxt4 = this.mxt;
        double Myx4 = this.myx;
        double Myy4 = this.myy;
        double Myt4 = this.myt;
        double det2 = (Mxx4 * Myy4) - (Mxy4 * Myx4);
        if (det2 == 0.0d || Math.abs(det2) <= Double.MIN_VALUE) {
            throw new NoninvertibleTransformException("Determinant is " + det2);
        }
        while (true) {
            numPts--;
            if (numPts >= 0) {
                int i14 = srcOff;
                double x5 = srcPts[i14] - Mxt4;
                srcOff = srcOff + 1 + 1;
                double y3 = srcPts[r11] - Myt4;
                int i15 = dstOff;
                int dstOff8 = dstOff + 1;
                dstPts[i15] = (float) (((x5 * Myy4) - (y3 * Mxy4)) / det2);
                dstOff = dstOff8 + 1;
                dstPts[dstOff8] = (float) (((y3 * Mxx4) - (x5 * Myx4)) / det2);
            } else {
                return;
            }
        }
    }

    @Override // com.sun.javafx.geom.transform.BaseTransform
    public void inverseTransform(double[] srcPts, int srcOff, double[] dstPts, int dstOff, int numPts) throws NoninvertibleTransformException {
        if (dstPts == srcPts && dstOff > srcOff && dstOff < srcOff + (numPts * 2)) {
            System.arraycopy(srcPts, srcOff, dstPts, dstOff, numPts * 2);
            srcOff = dstOff;
        }
        switch (this.state) {
            case 0:
                if (srcPts != dstPts || srcOff != dstOff) {
                    System.arraycopy(srcPts, srcOff, dstPts, dstOff, numPts * 2);
                    return;
                }
                return;
            case 1:
                double Mxt = this.mxt;
                double Myt = this.myt;
                while (true) {
                    numPts--;
                    if (numPts >= 0) {
                        int i2 = dstOff;
                        int dstOff2 = dstOff + 1;
                        int i3 = srcOff;
                        int srcOff2 = srcOff + 1;
                        dstPts[i2] = srcPts[i3] - Mxt;
                        dstOff = dstOff2 + 1;
                        srcOff = srcOff2 + 1;
                        dstPts[dstOff2] = srcPts[srcOff2] - Myt;
                    } else {
                        return;
                    }
                }
            case 2:
                double Mxx = this.mxx;
                double Myy = this.myy;
                if (Mxx == 0.0d || Myy == 0.0d) {
                    throw new NoninvertibleTransformException("Determinant is 0");
                }
                while (true) {
                    numPts--;
                    if (numPts >= 0) {
                        int i4 = dstOff;
                        int dstOff3 = dstOff + 1;
                        int i5 = srcOff;
                        int srcOff3 = srcOff + 1;
                        dstPts[i4] = srcPts[i5] / Mxx;
                        dstOff = dstOff3 + 1;
                        srcOff = srcOff3 + 1;
                        dstPts[dstOff3] = srcPts[srcOff3] / Myy;
                    } else {
                        return;
                    }
                }
                break;
            case 3:
                double Mxx2 = this.mxx;
                double Mxt2 = this.mxt;
                double Myy2 = this.myy;
                double Myt2 = this.myt;
                if (Mxx2 == 0.0d || Myy2 == 0.0d) {
                    throw new NoninvertibleTransformException("Determinant is 0");
                }
                while (true) {
                    numPts--;
                    if (numPts >= 0) {
                        int i6 = dstOff;
                        int dstOff4 = dstOff + 1;
                        int i7 = srcOff;
                        int srcOff4 = srcOff + 1;
                        dstPts[i6] = (srcPts[i7] - Mxt2) / Mxx2;
                        dstOff = dstOff4 + 1;
                        srcOff = srcOff4 + 1;
                        dstPts[dstOff4] = (srcPts[srcOff4] - Myt2) / Myy2;
                    } else {
                        return;
                    }
                }
                break;
            case 4:
                double Mxy = this.mxy;
                double Myx = this.myx;
                if (Mxy == 0.0d || Myx == 0.0d) {
                    throw new NoninvertibleTransformException("Determinant is 0");
                }
                while (true) {
                    numPts--;
                    if (numPts >= 0) {
                        int i8 = srcOff;
                        int srcOff5 = srcOff + 1;
                        double x2 = srcPts[i8];
                        int i9 = dstOff;
                        int dstOff5 = dstOff + 1;
                        srcOff = srcOff5 + 1;
                        dstPts[i9] = srcPts[srcOff5] / Myx;
                        dstOff = dstOff5 + 1;
                        dstPts[dstOff5] = x2 / Mxy;
                    } else {
                        return;
                    }
                }
                break;
            case 5:
                double Mxy2 = this.mxy;
                double Mxt3 = this.mxt;
                double Myx2 = this.myx;
                double Myt3 = this.myt;
                if (Mxy2 == 0.0d || Myx2 == 0.0d) {
                    throw new NoninvertibleTransformException("Determinant is 0");
                }
                while (true) {
                    numPts--;
                    if (numPts >= 0) {
                        int i10 = srcOff;
                        int srcOff6 = srcOff + 1;
                        double x3 = srcPts[i10] - Mxt3;
                        int i11 = dstOff;
                        int dstOff6 = dstOff + 1;
                        srcOff = srcOff6 + 1;
                        dstPts[i11] = (srcPts[srcOff6] - Myt3) / Myx2;
                        dstOff = dstOff6 + 1;
                        dstPts[dstOff6] = x3 / Mxy2;
                    } else {
                        return;
                    }
                }
                break;
            case 6:
                double Mxx3 = this.mxx;
                double Mxy3 = this.mxy;
                double Myx3 = this.myx;
                double Myy3 = this.myy;
                double det = (Mxx3 * Myy3) - (Mxy3 * Myx3);
                if (det == 0.0d || Math.abs(det) <= Double.MIN_VALUE) {
                    throw new NoninvertibleTransformException("Determinant is " + det);
                }
                while (true) {
                    numPts--;
                    if (numPts >= 0) {
                        int i12 = srcOff;
                        int srcOff7 = srcOff + 1;
                        double x4 = srcPts[i12];
                        srcOff = srcOff7 + 1;
                        double y2 = srcPts[srcOff7];
                        int i13 = dstOff;
                        int dstOff7 = dstOff + 1;
                        dstPts[i13] = ((x4 * Myy3) - (y2 * Mxy3)) / det;
                        dstOff = dstOff7 + 1;
                        dstPts[dstOff7] = ((y2 * Mxx3) - (x4 * Myx3)) / det;
                    } else {
                        return;
                    }
                }
                break;
            case 7:
                break;
            default:
                stateError();
                break;
        }
        double Mxx4 = this.mxx;
        double Mxy4 = this.mxy;
        double Mxt4 = this.mxt;
        double Myx4 = this.myx;
        double Myy4 = this.myy;
        double Myt4 = this.myt;
        double det2 = (Mxx4 * Myy4) - (Mxy4 * Myx4);
        if (det2 == 0.0d || Math.abs(det2) <= Double.MIN_VALUE) {
            throw new NoninvertibleTransformException("Determinant is " + det2);
        }
        while (true) {
            numPts--;
            if (numPts >= 0) {
                int i14 = srcOff;
                int srcOff8 = srcOff + 1;
                double x5 = srcPts[i14] - Mxt4;
                srcOff = srcOff8 + 1;
                double y3 = srcPts[srcOff8] - Myt4;
                int i15 = dstOff;
                int dstOff8 = dstOff + 1;
                dstPts[i15] = ((x5 * Myy4) - (y3 * Mxy4)) / det2;
                dstOff = dstOff8 + 1;
                dstPts[dstOff8] = ((y3 * Mxx4) - (x5 * Myx4)) / det2;
            } else {
                return;
            }
        }
    }

    @Override // com.sun.javafx.geom.transform.BaseTransform
    public Shape createTransformedShape(Shape s2) {
        if (s2 == null) {
            return null;
        }
        return new Path2D(s2, this);
    }

    public void translate(double tx, double ty) {
        switch (this.state) {
            case 0:
                this.mxt = tx;
                this.myt = ty;
                if (tx != 0.0d || ty != 0.0d) {
                    this.state = 1;
                    this.type = 1;
                    return;
                }
                return;
            case 1:
                this.mxt = tx + this.mxt;
                this.myt = ty + this.myt;
                if (this.mxt == 0.0d && this.myt == 0.0d) {
                    this.state = 0;
                    this.type = 0;
                    return;
                }
                return;
            case 2:
                this.mxt = tx * this.mxx;
                this.myt = ty * this.myy;
                if (this.mxt != 0.0d || this.myt != 0.0d) {
                    this.state = 3;
                    this.type |= 1;
                    return;
                }
                return;
            case 3:
                this.mxt = (tx * this.mxx) + this.mxt;
                this.myt = (ty * this.myy) + this.myt;
                if (this.mxt == 0.0d && this.myt == 0.0d) {
                    this.state = 2;
                    if (this.type != -1) {
                        this.type &= -2;
                        return;
                    }
                    return;
                }
                return;
            case 4:
                this.mxt = ty * this.mxy;
                this.myt = tx * this.myx;
                if (this.mxt != 0.0d || this.myt != 0.0d) {
                    this.state = 5;
                    this.type |= 1;
                    return;
                }
                return;
            case 5:
                this.mxt = (ty * this.mxy) + this.mxt;
                this.myt = (tx * this.myx) + this.myt;
                if (this.mxt == 0.0d && this.myt == 0.0d) {
                    this.state = 4;
                    if (this.type != -1) {
                        this.type &= -2;
                        return;
                    }
                    return;
                }
                return;
            case 6:
                this.mxt = (tx * this.mxx) + (ty * this.mxy);
                this.myt = (tx * this.myx) + (ty * this.myy);
                if (this.mxt != 0.0d || this.myt != 0.0d) {
                    this.state = 7;
                    this.type |= 1;
                    return;
                }
                return;
            case 7:
                break;
            default:
                stateError();
                break;
        }
        this.mxt = (tx * this.mxx) + (ty * this.mxy) + this.mxt;
        this.myt = (tx * this.myx) + (ty * this.myy) + this.myt;
        if (this.mxt == 0.0d && this.myt == 0.0d) {
            this.state = 6;
            if (this.type != -1) {
                this.type &= -2;
            }
        }
    }

    protected final void rotate90() {
        double M0 = this.mxx;
        this.mxx = this.mxy;
        this.mxy = -M0;
        double M02 = this.myx;
        this.myx = this.myy;
        this.myy = -M02;
        int newstate = rot90conversion[this.state];
        if ((newstate & 6) == 2 && this.mxx == 1.0d && this.myy == 1.0d) {
            newstate -= 2;
        }
        this.state = newstate;
        this.type = -1;
    }

    protected final void rotate180() {
        this.mxx = -this.mxx;
        this.myy = -this.myy;
        int oldstate = this.state;
        if ((oldstate & 4) != 0) {
            this.mxy = -this.mxy;
            this.myx = -this.myx;
        } else if (this.mxx == 1.0d && this.myy == 1.0d) {
            this.state = oldstate & (-3);
        } else {
            this.state = oldstate | 2;
        }
        this.type = -1;
    }

    protected final void rotate270() {
        double M0 = this.mxx;
        this.mxx = -this.mxy;
        this.mxy = M0;
        double M02 = this.myx;
        this.myx = -this.myy;
        this.myy = M02;
        int newstate = rot90conversion[this.state];
        if ((newstate & 6) == 2 && this.mxx == 1.0d && this.myy == 1.0d) {
            newstate -= 2;
        }
        this.state = newstate;
        this.type = -1;
    }

    public void rotate(double theta) {
        double sin = Math.sin(theta);
        if (sin == 1.0d) {
            rotate90();
            return;
        }
        if (sin == -1.0d) {
            rotate270();
            return;
        }
        double cos = Math.cos(theta);
        if (cos == -1.0d) {
            rotate180();
            return;
        }
        if (cos != 1.0d) {
            double M0 = this.mxx;
            double M1 = this.mxy;
            this.mxx = (cos * M0) + (sin * M1);
            this.mxy = ((-sin) * M0) + (cos * M1);
            double M02 = this.myx;
            double M12 = this.myy;
            this.myx = (cos * M02) + (sin * M12);
            this.myy = ((-sin) * M02) + (cos * M12);
            updateState2D();
        }
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Removed duplicated region for block: B:8:0x006c  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public void scale(double r7, double r9) {
        /*
            Method dump skipped, instructions count: 284
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.sun.javafx.geom.transform.AffineBase.scale(double, double):void");
    }

    public void shear(double shx, double shy) {
        int mystate = this.state;
        switch (mystate) {
            case 0:
            case 1:
                this.mxy = shx;
                this.myx = shy;
                if (this.mxy != 0.0d || this.myx != 0.0d) {
                    this.state = mystate | 2 | 4;
                    this.type = -1;
                    return;
                }
                return;
            case 2:
            case 3:
                this.mxy = this.mxx * shx;
                this.myx = this.myy * shy;
                if (this.mxy != 0.0d || this.myx != 0.0d) {
                    this.state = mystate | 4;
                }
                this.type = -1;
                return;
            case 4:
            case 5:
                this.mxx = this.mxy * shy;
                this.myy = this.myx * shx;
                if (this.mxx != 0.0d || this.myy != 0.0d) {
                    this.state = mystate | 2;
                }
                this.type = -1;
                return;
            case 6:
            case 7:
                break;
            default:
                stateError();
                break;
        }
        double M0 = this.mxx;
        double M1 = this.mxy;
        this.mxx = M0 + (M1 * shy);
        this.mxy = (M0 * shx) + M1;
        double M02 = this.myx;
        double M12 = this.myy;
        this.myx = M02 + (M12 * shy);
        this.myy = (M02 * shx) + M12;
        updateState2D();
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    public void concatenate(BaseTransform Tx) {
        switch (Tx.getDegree()) {
            case IDENTITY:
            case TRANSLATE_2D:
                translate(Tx.getMxt(), Tx.getMyt());
                return;
            case AFFINE_2D:
                break;
            default:
                if (!Tx.is2D()) {
                    degreeError(BaseTransform.Degree.AFFINE_2D);
                }
                if (!(Tx instanceof AffineBase)) {
                    Tx = new Affine2D(Tx);
                    break;
                }
                break;
        }
        int mystate = this.state;
        AffineBase at2 = (AffineBase) Tx;
        int txstate = at2.state;
        switch ((txstate << 4) | mystate) {
            case 0:
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
                break;
            case 8:
            case 9:
            case 10:
            case 11:
            case 12:
            case 13:
            case 14:
            case 15:
            case 24:
            case 25:
            case 26:
            case 27:
            case 28:
            case 29:
            case 30:
            case 31:
            case 40:
            case 41:
            case 42:
            case 43:
            case 44:
            case 45:
            case 46:
            case 47:
            case 49:
            case 50:
            case 51:
            case 52:
            case 53:
            case 54:
            case 55:
            case 56:
            case 57:
            case 58:
            case 59:
            case 60:
            case 61:
            case 62:
            case 63:
            case 72:
            case 73:
            case 74:
            case 75:
            case 76:
            case 77:
            case 78:
            case 79:
            case 81:
            case 82:
            case 83:
            case 84:
            case 85:
            case 86:
            case 87:
            case 88:
            case 89:
            case 90:
            case 91:
            case 92:
            case 93:
            case 94:
            case 95:
            case 97:
            case 98:
            case 99:
            case 100:
            case 101:
            case 102:
            case 103:
            case 104:
            case 105:
            case 106:
            case 107:
            case 108:
            case 109:
            case 110:
            case 111:
            default:
                double Txx = at2.mxx;
                double Txy = at2.mxy;
                double Txt = at2.mxt;
                double Tyx = at2.myx;
                double Tyy = at2.myy;
                double Tyt = at2.myt;
                switch (mystate) {
                    case 1:
                        this.mxx = Txx;
                        this.mxy = Txy;
                        this.mxt += Txt;
                        this.myx = Tyx;
                        this.myy = Tyy;
                        this.myt += Tyt;
                        this.state = txstate | 1;
                        this.type = -1;
                        break;
                    case 2:
                    case 3:
                        double M0 = this.mxx;
                        this.mxx = Txx * M0;
                        this.mxy = Txy * M0;
                        this.mxt += Txt * M0;
                        double M02 = this.myy;
                        this.myx = Tyx * M02;
                        this.myy = Tyy * M02;
                        this.myt += Tyt * M02;
                        updateState2D();
                        break;
                    case 4:
                    case 5:
                        double M03 = this.mxy;
                        this.mxx = Tyx * M03;
                        this.mxy = Tyy * M03;
                        this.mxt += Tyt * M03;
                        double M04 = this.myx;
                        this.myx = Txx * M04;
                        this.myy = Txy * M04;
                        this.myt += Txt * M04;
                        updateState2D();
                        break;
                    case 6:
                        this.state = mystate | txstate;
                        double M05 = this.mxx;
                        double M1 = this.mxy;
                        this.mxx = (Txx * M05) + (Tyx * M1);
                        this.mxy = (Txy * M05) + (Tyy * M1);
                        this.mxt += (Txt * M05) + (Tyt * M1);
                        double M06 = this.myx;
                        double M12 = this.myy;
                        this.myx = (Txx * M06) + (Tyx * M12);
                        this.myy = (Txy * M06) + (Tyy * M12);
                        this.myt += (Txt * M06) + (Tyt * M12);
                        this.type = -1;
                        break;
                    case 7:
                        double M052 = this.mxx;
                        double M13 = this.mxy;
                        this.mxx = (Txx * M052) + (Tyx * M13);
                        this.mxy = (Txy * M052) + (Tyy * M13);
                        this.mxt += (Txt * M052) + (Tyt * M13);
                        double M062 = this.myx;
                        double M122 = this.myy;
                        this.myx = (Txx * M062) + (Tyx * M122);
                        this.myy = (Txy * M062) + (Tyy * M122);
                        this.myt += (Txt * M062) + (Tyt * M122);
                        this.type = -1;
                        break;
                    default:
                        stateError();
                        this.state = mystate | txstate;
                        double M0522 = this.mxx;
                        double M132 = this.mxy;
                        this.mxx = (Txx * M0522) + (Tyx * M132);
                        this.mxy = (Txy * M0522) + (Tyy * M132);
                        this.mxt += (Txt * M0522) + (Tyt * M132);
                        double M0622 = this.myx;
                        double M1222 = this.myy;
                        this.myx = (Txx * M0622) + (Tyx * M1222);
                        this.myy = (Txy * M0622) + (Tyy * M1222);
                        this.myt += (Txt * M0622) + (Tyt * M1222);
                        this.type = -1;
                        break;
                }
            case 16:
                this.mxt = at2.mxt;
                this.myt = at2.myt;
                this.state = txstate;
                this.type = at2.type;
                break;
            case 17:
            case 18:
            case 19:
            case 20:
            case 21:
            case 22:
            case 23:
                translate(at2.mxt, at2.myt);
                break;
            case 32:
                this.mxx = at2.mxx;
                this.myy = at2.myy;
                this.state = txstate;
                this.type = at2.type;
                break;
            case 33:
            case 34:
            case 35:
            case 36:
            case 37:
            case 38:
            case 39:
                scale(at2.mxx, at2.myy);
                break;
            case 48:
                this.mxx = at2.mxx;
                this.myy = at2.myy;
                this.mxt = at2.mxt;
                this.myt = at2.myt;
                this.state = txstate;
                this.type = at2.type;
                break;
            case 64:
                this.mxy = at2.mxy;
                this.myx = at2.myx;
                this.myy = 0.0d;
                this.mxx = 0.0d;
                this.state = txstate;
                this.type = at2.type;
                break;
            case 65:
                this.mxx = 0.0d;
                this.mxy = at2.mxy;
                this.myx = at2.myx;
                this.myy = 0.0d;
                this.state = 5;
                this.type = -1;
                break;
            case 66:
            case 67:
                this.mxy = this.mxx * at2.mxy;
                this.mxx = 0.0d;
                this.myx = this.myy * at2.myx;
                this.myy = 0.0d;
                this.state = mystate ^ 6;
                this.type = -1;
                break;
            case 68:
            case 69:
                this.mxx = this.mxy * at2.myx;
                this.mxy = 0.0d;
                this.myy = this.myx * at2.mxy;
                this.myx = 0.0d;
                this.state = mystate ^ 6;
                this.type = -1;
                break;
            case 70:
            case 71:
                double Txy2 = at2.mxy;
                double Tyx2 = at2.myx;
                double M07 = this.mxx;
                this.mxx = this.mxy * Tyx2;
                this.mxy = M07 * Txy2;
                double M08 = this.myx;
                this.myx = this.myy * Tyx2;
                this.myy = M08 * Txy2;
                this.type = -1;
                break;
            case 80:
                this.mxt = at2.mxt;
                this.myt = at2.myt;
                this.mxy = at2.mxy;
                this.myx = at2.myx;
                this.myy = 0.0d;
                this.mxx = 0.0d;
                this.state = txstate;
                this.type = at2.type;
                break;
            case 96:
                this.mxy = at2.mxy;
                this.myx = at2.myx;
                this.mxx = at2.mxx;
                this.myy = at2.myy;
                this.state = txstate;
                this.type = at2.type;
                break;
            case 112:
                this.mxy = at2.mxy;
                this.myx = at2.myx;
                this.mxx = at2.mxx;
                this.myy = at2.myy;
                this.mxt = at2.mxt;
                this.myt = at2.myt;
                this.state = txstate;
                this.type = at2.type;
                break;
        }
    }

    public void concatenate(double Txx, double Txy, double Txt, double Tyx, double Tyy, double Tyt) {
        double rxx = (this.mxx * Txx) + (this.mxy * Tyx);
        double rxy = (this.mxx * Txy) + (this.mxy * Tyy);
        double rxt = (this.mxx * Txt) + (this.mxy * Tyt) + this.mxt;
        double ryx = (this.myx * Txx) + (this.myy * Tyx);
        double ryy = (this.myx * Txy) + (this.myy * Tyy);
        double ryt = (this.myx * Txt) + (this.myy * Tyt) + this.myt;
        this.mxx = rxx;
        this.mxy = rxy;
        this.mxt = rxt;
        this.myx = ryx;
        this.myy = ryy;
        this.myt = ryt;
        updateState();
    }

    @Override // com.sun.javafx.geom.transform.BaseTransform
    public void invert() throws NoninvertibleTransformException {
        switch (this.state) {
            case 0:
                return;
            case 1:
                this.mxt = -this.mxt;
                this.myt = -this.myt;
                return;
            case 2:
                double Mxx = this.mxx;
                double Myy = this.myy;
                if (Mxx == 0.0d || Myy == 0.0d) {
                    throw new NoninvertibleTransformException("Determinant is 0");
                }
                this.mxx = 1.0d / Mxx;
                this.myy = 1.0d / Myy;
                return;
            case 3:
                double Mxx2 = this.mxx;
                double Mxt = this.mxt;
                double Myy2 = this.myy;
                double Myt = this.myt;
                if (Mxx2 == 0.0d || Myy2 == 0.0d) {
                    throw new NoninvertibleTransformException("Determinant is 0");
                }
                this.mxx = 1.0d / Mxx2;
                this.myy = 1.0d / Myy2;
                this.mxt = (-Mxt) / Mxx2;
                this.myt = (-Myt) / Myy2;
                return;
            case 4:
                double Mxy = this.mxy;
                double Myx = this.myx;
                if (Mxy == 0.0d || Myx == 0.0d) {
                    throw new NoninvertibleTransformException("Determinant is 0");
                }
                this.myx = 1.0d / Mxy;
                this.mxy = 1.0d / Myx;
                return;
            case 5:
                double Mxy2 = this.mxy;
                double Mxt2 = this.mxt;
                double Myx2 = this.myx;
                double Myt2 = this.myt;
                if (Mxy2 == 0.0d || Myx2 == 0.0d) {
                    throw new NoninvertibleTransformException("Determinant is 0");
                }
                this.myx = 1.0d / Mxy2;
                this.mxy = 1.0d / Myx2;
                this.mxt = (-Myt2) / Myx2;
                this.myt = (-Mxt2) / Mxy2;
                return;
            case 6:
                double Mxx3 = this.mxx;
                double Mxy3 = this.mxy;
                double Myx3 = this.myx;
                double Myy3 = this.myy;
                double det = (Mxx3 * Myy3) - (Mxy3 * Myx3);
                if (det == 0.0d || Math.abs(det) <= Double.MIN_VALUE) {
                    throw new NoninvertibleTransformException("Determinant is " + det);
                }
                this.mxx = Myy3 / det;
                this.myx = (-Myx3) / det;
                this.mxy = (-Mxy3) / det;
                this.myy = Mxx3 / det;
                return;
            case 7:
                break;
            default:
                stateError();
                break;
        }
        double Mxx4 = this.mxx;
        double Mxy4 = this.mxy;
        double Mxt3 = this.mxt;
        double Myx4 = this.myx;
        double Myy4 = this.myy;
        double Myt3 = this.myt;
        double det2 = (Mxx4 * Myy4) - (Mxy4 * Myx4);
        if (det2 == 0.0d || Math.abs(det2) <= Double.MIN_VALUE) {
            throw new NoninvertibleTransformException("Determinant is " + det2);
        }
        this.mxx = Myy4 / det2;
        this.myx = (-Myx4) / det2;
        this.mxy = (-Mxy4) / det2;
        this.myy = Mxx4 / det2;
        this.mxt = ((Mxy4 * Myt3) - (Myy4 * Mxt3)) / det2;
        this.myt = ((Myx4 * Mxt3) - (Mxx4 * Myt3)) / det2;
    }
}
