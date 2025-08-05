package com.sun.javafx.geom.transform;

import com.sun.javafx.geom.Point2D;
import com.sun.javafx.geom.transform.BaseTransform;
import sun.security.pkcs11.wrapper.Constants;

/* loaded from: jfxrt.jar:com/sun/javafx/geom/transform/Affine2D.class */
public class Affine2D extends AffineBase {
    private static final long BASE_HASH;

    private Affine2D(double mxx, double myx, double mxy, double myy, double mxt, double myt, int state) {
        this.mxx = mxx;
        this.myx = myx;
        this.mxy = mxy;
        this.myy = myy;
        this.mxt = mxt;
        this.myt = myt;
        this.state = state;
        this.type = -1;
    }

    public Affine2D() {
        this.myy = 1.0d;
        this.mxx = 1.0d;
    }

    public Affine2D(BaseTransform Tx) {
        setTransform(Tx);
    }

    public Affine2D(float mxx, float myx, float mxy, float myy, float mxt, float myt) {
        this.mxx = mxx;
        this.myx = myx;
        this.mxy = mxy;
        this.myy = myy;
        this.mxt = mxt;
        this.myt = myt;
        updateState2D();
    }

    public Affine2D(double mxx, double myx, double mxy, double myy, double mxt, double myt) {
        this.mxx = mxx;
        this.myx = myx;
        this.mxy = mxy;
        this.myy = myy;
        this.mxt = mxt;
        this.myt = myt;
        updateState2D();
    }

    @Override // com.sun.javafx.geom.transform.BaseTransform
    public BaseTransform.Degree getDegree() {
        return BaseTransform.Degree.AFFINE_2D;
    }

    @Override // com.sun.javafx.geom.transform.AffineBase
    protected void reset3Delements() {
    }

    public void rotate(double theta, double anchorx, double anchory) {
        translate(anchorx, anchory);
        rotate(theta);
        translate(-anchorx, -anchory);
    }

    public void rotate(double vecx, double vecy) {
        if (vecy == 0.0d) {
            if (vecx < 0.0d) {
                rotate180();
                return;
            }
            return;
        }
        if (vecx == 0.0d) {
            if (vecy > 0.0d) {
                rotate90();
                return;
            } else {
                rotate270();
                return;
            }
        }
        double len = Math.sqrt((vecx * vecx) + (vecy * vecy));
        double sin = vecy / len;
        double cos = vecx / len;
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

    public void rotate(double vecx, double vecy, double anchorx, double anchory) {
        translate(anchorx, anchory);
        rotate(vecx, vecy);
        translate(-anchorx, -anchory);
    }

    public void quadrantRotate(int numquadrants) {
        switch (numquadrants & 3) {
            case 1:
                rotate90();
                break;
            case 2:
                rotate180();
                break;
            case 3:
                rotate270();
                break;
        }
    }

    public void quadrantRotate(int numquadrants, double anchorx, double anchory) {
        switch (numquadrants & 3) {
            case 0:
                return;
            case 1:
                this.mxt += (anchorx * (this.mxx - this.mxy)) + (anchory * (this.mxy + this.mxx));
                this.myt += (anchorx * (this.myx - this.myy)) + (anchory * (this.myy + this.myx));
                rotate90();
                break;
            case 2:
                this.mxt += (anchorx * (this.mxx + this.mxx)) + (anchory * (this.mxy + this.mxy));
                this.myt += (anchorx * (this.myx + this.myx)) + (anchory * (this.myy + this.myy));
                rotate180();
                break;
            case 3:
                this.mxt += (anchorx * (this.mxx + this.mxy)) + (anchory * (this.mxy - this.mxx));
                this.myt += (anchorx * (this.myx + this.myy)) + (anchory * (this.myy - this.myx));
                rotate270();
                break;
        }
        if (this.mxt == 0.0d && this.myt == 0.0d) {
            this.state &= -2;
            if (this.type != -1) {
                this.type &= -2;
                return;
            }
            return;
        }
        this.state |= 1;
        this.type |= 1;
    }

    public void setToTranslation(double tx, double ty) {
        this.mxx = 1.0d;
        this.myx = 0.0d;
        this.mxy = 0.0d;
        this.myy = 1.0d;
        this.mxt = tx;
        this.myt = ty;
        if (tx != 0.0d || ty != 0.0d) {
            this.state = 1;
            this.type = 1;
        } else {
            this.state = 0;
            this.type = 0;
        }
    }

    public void setToRotation(double theta) {
        double cos;
        double sin = Math.sin(theta);
        if (sin == 1.0d || sin == -1.0d) {
            cos = 0.0d;
            this.state = 4;
            this.type = 8;
        } else {
            cos = Math.cos(theta);
            if (cos == -1.0d) {
                sin = 0.0d;
                this.state = 2;
                this.type = 8;
            } else if (cos == 1.0d) {
                sin = 0.0d;
                this.state = 0;
                this.type = 0;
            } else {
                this.state = 6;
                this.type = 16;
            }
        }
        this.mxx = cos;
        this.myx = sin;
        this.mxy = -sin;
        this.myy = cos;
        this.mxt = 0.0d;
        this.myt = 0.0d;
    }

    public void setToRotation(double theta, double anchorx, double anchory) {
        setToRotation(theta);
        double sin = this.myx;
        double oneMinusCos = 1.0d - this.mxx;
        this.mxt = (anchorx * oneMinusCos) + (anchory * sin);
        this.myt = (anchory * oneMinusCos) - (anchorx * sin);
        if (this.mxt != 0.0d || this.myt != 0.0d) {
            this.state |= 1;
            this.type |= 1;
        }
    }

    public void setToRotation(double vecx, double vecy) {
        double cos;
        double sin;
        if (vecy == 0.0d) {
            sin = 0.0d;
            if (vecx < 0.0d) {
                cos = -1.0d;
                this.state = 2;
                this.type = 8;
            } else {
                cos = 1.0d;
                this.state = 0;
                this.type = 0;
            }
        } else if (vecx == 0.0d) {
            cos = 0.0d;
            sin = vecy > 0.0d ? 1.0d : -1.0d;
            this.state = 4;
            this.type = 8;
        } else {
            double len = Math.sqrt((vecx * vecx) + (vecy * vecy));
            cos = vecx / len;
            sin = vecy / len;
            this.state = 6;
            this.type = 16;
        }
        this.mxx = cos;
        this.myx = sin;
        this.mxy = -sin;
        this.myy = cos;
        this.mxt = 0.0d;
        this.myt = 0.0d;
    }

    public void setToRotation(double vecx, double vecy, double anchorx, double anchory) {
        setToRotation(vecx, vecy);
        double sin = this.myx;
        double oneMinusCos = 1.0d - this.mxx;
        this.mxt = (anchorx * oneMinusCos) + (anchory * sin);
        this.myt = (anchory * oneMinusCos) - (anchorx * sin);
        if (this.mxt != 0.0d || this.myt != 0.0d) {
            this.state |= 1;
            this.type |= 1;
        }
    }

    public void setToQuadrantRotation(int numquadrants) {
        switch (numquadrants & 3) {
            case 0:
                this.mxx = 1.0d;
                this.myx = 0.0d;
                this.mxy = 0.0d;
                this.myy = 1.0d;
                this.mxt = 0.0d;
                this.myt = 0.0d;
                this.state = 0;
                this.type = 0;
                break;
            case 1:
                this.mxx = 0.0d;
                this.myx = 1.0d;
                this.mxy = -1.0d;
                this.myy = 0.0d;
                this.mxt = 0.0d;
                this.myt = 0.0d;
                this.state = 4;
                this.type = 8;
                break;
            case 2:
                this.mxx = -1.0d;
                this.myx = 0.0d;
                this.mxy = 0.0d;
                this.myy = -1.0d;
                this.mxt = 0.0d;
                this.myt = 0.0d;
                this.state = 2;
                this.type = 8;
                break;
            case 3:
                this.mxx = 0.0d;
                this.myx = -1.0d;
                this.mxy = 1.0d;
                this.myy = 0.0d;
                this.mxt = 0.0d;
                this.myt = 0.0d;
                this.state = 4;
                this.type = 8;
                break;
        }
    }

    public void setToQuadrantRotation(int numquadrants, double anchorx, double anchory) {
        switch (numquadrants & 3) {
            case 0:
                this.mxx = 1.0d;
                this.myx = 0.0d;
                this.mxy = 0.0d;
                this.myy = 1.0d;
                this.mxt = 0.0d;
                this.myt = 0.0d;
                this.state = 0;
                this.type = 0;
                break;
            case 1:
                this.mxx = 0.0d;
                this.myx = 1.0d;
                this.mxy = -1.0d;
                this.myy = 0.0d;
                this.mxt = anchorx + anchory;
                this.myt = anchory - anchorx;
                if (this.mxt == 0.0d && this.myt == 0.0d) {
                    this.state = 4;
                    this.type = 8;
                    break;
                } else {
                    this.state = 5;
                    this.type = 9;
                    break;
                }
                break;
            case 2:
                this.mxx = -1.0d;
                this.myx = 0.0d;
                this.mxy = 0.0d;
                this.myy = -1.0d;
                this.mxt = anchorx + anchorx;
                this.myt = anchory + anchory;
                if (this.mxt == 0.0d && this.myt == 0.0d) {
                    this.state = 2;
                    this.type = 8;
                    break;
                } else {
                    this.state = 3;
                    this.type = 9;
                    break;
                }
                break;
            case 3:
                this.mxx = 0.0d;
                this.myx = -1.0d;
                this.mxy = 1.0d;
                this.myy = 0.0d;
                this.mxt = anchorx - anchory;
                this.myt = anchory + anchorx;
                if (this.mxt == 0.0d && this.myt == 0.0d) {
                    this.state = 4;
                    this.type = 8;
                    break;
                } else {
                    this.state = 5;
                    this.type = 9;
                    break;
                }
                break;
        }
    }

    public void setToScale(double sx, double sy) {
        this.mxx = sx;
        this.myx = 0.0d;
        this.mxy = 0.0d;
        this.myy = sy;
        this.mxt = 0.0d;
        this.myt = 0.0d;
        if (sx != 1.0d || sy != 1.0d) {
            this.state = 2;
            this.type = -1;
        } else {
            this.state = 0;
            this.type = 0;
        }
    }

    @Override // com.sun.javafx.geom.transform.BaseTransform
    public void setTransform(BaseTransform Tx) {
        switch (Tx.getDegree()) {
            case IDENTITY:
                setToIdentity();
                return;
            case TRANSLATE_2D:
                setToTranslation(Tx.getMxt(), Tx.getMyt());
                return;
            case AFFINE_2D:
                break;
            default:
                if (Tx.getType() > 127) {
                    System.out.println(((Object) Tx) + " is " + Tx.getType());
                    System.out.print(Constants.INDENT + Tx.getMxx());
                    System.out.print(", " + Tx.getMxy());
                    System.out.print(", " + Tx.getMxz());
                    System.out.print(", " + Tx.getMxt());
                    System.out.println();
                    System.out.print(Constants.INDENT + Tx.getMyx());
                    System.out.print(", " + Tx.getMyy());
                    System.out.print(", " + Tx.getMyz());
                    System.out.print(", " + Tx.getMyt());
                    System.out.println();
                    System.out.print(Constants.INDENT + Tx.getMzx());
                    System.out.print(", " + Tx.getMzy());
                    System.out.print(", " + Tx.getMzz());
                    System.out.print(", " + Tx.getMzt());
                    System.out.println();
                    degreeError(BaseTransform.Degree.AFFINE_2D);
                    break;
                }
                break;
        }
        this.mxx = Tx.getMxx();
        this.myx = Tx.getMyx();
        this.mxy = Tx.getMxy();
        this.myy = Tx.getMyy();
        this.mxt = Tx.getMxt();
        this.myt = Tx.getMyt();
        if (Tx instanceof AffineBase) {
            this.state = ((AffineBase) Tx).state;
            this.type = ((AffineBase) Tx).type;
        } else {
            updateState2D();
        }
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Removed duplicated region for block: B:19:0x01e4  */
    /* JADX WARN: Removed duplicated region for block: B:22:0x021a  */
    /* JADX WARN: Removed duplicated region for block: B:25:0x0237  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public void preConcatenate(com.sun.javafx.geom.transform.BaseTransform r10) {
        /*
            Method dump skipped, instructions count: 1222
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.sun.javafx.geom.transform.Affine2D.preConcatenate(com.sun.javafx.geom.transform.BaseTransform):void");
    }

    @Override // com.sun.javafx.geom.transform.BaseTransform
    public Affine2D createInverse() throws NoninvertibleTransformException {
        switch (this.state) {
            case 0:
                return new Affine2D();
            case 1:
                return new Affine2D(1.0d, 0.0d, 0.0d, 1.0d, -this.mxt, -this.myt, 1);
            case 2:
                if (this.mxx == 0.0d || this.myy == 0.0d) {
                    throw new NoninvertibleTransformException("Determinant is 0");
                }
                return new Affine2D(1.0d / this.mxx, 0.0d, 0.0d, 1.0d / this.myy, 0.0d, 0.0d, 2);
            case 3:
                if (this.mxx == 0.0d || this.myy == 0.0d) {
                    throw new NoninvertibleTransformException("Determinant is 0");
                }
                return new Affine2D(1.0d / this.mxx, 0.0d, 0.0d, 1.0d / this.myy, (-this.mxt) / this.mxx, (-this.myt) / this.myy, 3);
            case 4:
                if (this.mxy == 0.0d || this.myx == 0.0d) {
                    throw new NoninvertibleTransformException("Determinant is 0");
                }
                return new Affine2D(0.0d, 1.0d / this.mxy, 1.0d / this.myx, 0.0d, 0.0d, 0.0d, 4);
            case 5:
                if (this.mxy == 0.0d || this.myx == 0.0d) {
                    throw new NoninvertibleTransformException("Determinant is 0");
                }
                return new Affine2D(0.0d, 1.0d / this.mxy, 1.0d / this.myx, 0.0d, (-this.myt) / this.myx, (-this.mxt) / this.mxy, 5);
            case 6:
                double det = (this.mxx * this.myy) - (this.mxy * this.myx);
                if (det == 0.0d || Math.abs(det) <= Double.MIN_VALUE) {
                    throw new NoninvertibleTransformException("Determinant is " + det);
                }
                return new Affine2D(this.myy / det, (-this.myx) / det, (-this.mxy) / det, this.mxx / det, 0.0d, 0.0d, 6);
            case 7:
                break;
            default:
                stateError();
                break;
        }
        double det2 = (this.mxx * this.myy) - (this.mxy * this.myx);
        if (det2 == 0.0d || Math.abs(det2) <= Double.MIN_VALUE) {
            throw new NoninvertibleTransformException("Determinant is " + det2);
        }
        return new Affine2D(this.myy / det2, (-this.myx) / det2, (-this.mxy) / det2, this.mxx / det2, ((this.mxy * this.myt) - (this.myy * this.mxt)) / det2, ((this.myx * this.mxt) - (this.mxx * this.myt)) / det2, 7);
    }

    public void transform(Point2D[] ptSrc, int srcOff, Point2D[] ptDst, int dstOff, int numPts) {
        int mystate = this.state;
        while (true) {
            numPts--;
            if (numPts >= 0) {
                int i2 = srcOff;
                srcOff++;
                Point2D src = ptSrc[i2];
                double x2 = src.f11907x;
                double y2 = src.f11908y;
                int i3 = dstOff;
                dstOff++;
                Point2D dst = ptDst[i3];
                if (dst == null) {
                    dst = new Point2D();
                    ptDst[dstOff - 1] = dst;
                }
                switch (mystate) {
                    case 0:
                        dst.setLocation((float) x2, (float) y2);
                        continue;
                    case 1:
                        dst.setLocation((float) (x2 + this.mxt), (float) (y2 + this.myt));
                        continue;
                    case 2:
                        dst.setLocation((float) (x2 * this.mxx), (float) (y2 * this.myy));
                        continue;
                    case 3:
                        dst.setLocation((float) ((x2 * this.mxx) + this.mxt), (float) ((y2 * this.myy) + this.myt));
                        continue;
                    case 4:
                        dst.setLocation((float) (y2 * this.mxy), (float) (x2 * this.myx));
                        continue;
                    case 5:
                        dst.setLocation((float) ((y2 * this.mxy) + this.mxt), (float) ((x2 * this.myx) + this.myt));
                        continue;
                    case 6:
                        dst.setLocation((float) ((x2 * this.mxx) + (y2 * this.mxy)), (float) ((x2 * this.myx) + (y2 * this.myy)));
                        continue;
                    case 7:
                        break;
                    default:
                        stateError();
                        break;
                }
                dst.setLocation((float) ((x2 * this.mxx) + (y2 * this.mxy) + this.mxt), (float) ((x2 * this.myx) + (y2 * this.myy) + this.myt));
            } else {
                return;
            }
        }
    }

    public Point2D deltaTransform(Point2D ptSrc, Point2D ptDst) {
        if (ptDst == null) {
            ptDst = new Point2D();
        }
        double x2 = ptSrc.f11907x;
        double y2 = ptSrc.f11908y;
        switch (this.state) {
            case 0:
            case 1:
                ptDst.setLocation((float) x2, (float) y2);
                return ptDst;
            case 2:
            case 3:
                ptDst.setLocation((float) (x2 * this.mxx), (float) (y2 * this.myy));
                return ptDst;
            case 4:
            case 5:
                ptDst.setLocation((float) (y2 * this.mxy), (float) (x2 * this.myx));
                return ptDst;
            case 6:
            case 7:
                break;
            default:
                stateError();
                break;
        }
        ptDst.setLocation((float) ((x2 * this.mxx) + (y2 * this.mxy)), (float) ((x2 * this.myx) + (y2 * this.myy)));
        return ptDst;
    }

    private static double _matround(double matval) {
        return Math.rint(matval * 1.0E15d) / 1.0E15d;
    }

    @Override // com.sun.javafx.geom.transform.BaseTransform
    public String toString() {
        return "Affine2D[[" + _matround(this.mxx) + ", " + _matround(this.mxy) + ", " + _matround(this.mxt) + "], [" + _matround(this.myx) + ", " + _matround(this.myy) + ", " + _matround(this.myt) + "]]";
    }

    @Override // com.sun.javafx.geom.transform.AffineBase, com.sun.javafx.geom.transform.BaseTransform
    public boolean is2D() {
        return true;
    }

    @Override // com.sun.javafx.geom.transform.BaseTransform
    public void restoreTransform(double mxx, double myx, double mxy, double myy, double mxt, double myt) {
        setTransform(mxx, myx, mxy, myy, mxt, myt);
    }

    @Override // com.sun.javafx.geom.transform.BaseTransform
    public void restoreTransform(double mxx, double mxy, double mxz, double mxt, double myx, double myy, double myz, double myt, double mzx, double mzy, double mzz, double mzt) {
        if (mxz != 0.0d || myz != 0.0d || mzx != 0.0d || mzy != 0.0d || mzz != 1.0d || mzt != 0.0d) {
            degreeError(BaseTransform.Degree.AFFINE_2D);
        }
        setTransform(mxx, myx, mxy, myy, mxt, myt);
    }

    @Override // com.sun.javafx.geom.transform.BaseTransform
    public BaseTransform deriveWithTranslation(double mxt, double myt) {
        translate(mxt, myt);
        return this;
    }

    @Override // com.sun.javafx.geom.transform.BaseTransform
    public BaseTransform deriveWithTranslation(double mxt, double myt, double mzt) {
        if (mzt == 0.0d) {
            translate(mxt, myt);
            return this;
        }
        Affine3D a2 = new Affine3D(this);
        a2.translate(mxt, myt, mzt);
        return a2;
    }

    @Override // com.sun.javafx.geom.transform.BaseTransform
    public BaseTransform deriveWithScale(double mxx, double myy, double mzz) {
        if (mzz == 1.0d) {
            scale(mxx, myy);
            return this;
        }
        Affine3D a2 = new Affine3D(this);
        a2.scale(mxx, myy, mzz);
        return a2;
    }

    @Override // com.sun.javafx.geom.transform.BaseTransform
    public BaseTransform deriveWithRotation(double theta, double axisX, double axisY, double axisZ) {
        if (theta == 0.0d) {
            return this;
        }
        if (almostZero(axisX) && almostZero(axisY)) {
            if (axisZ > 0.0d) {
                rotate(theta);
            } else if (axisZ < 0.0d) {
                rotate(-theta);
            }
            return this;
        }
        Affine3D a2 = new Affine3D(this);
        a2.rotate(theta, axisX, axisY, axisZ);
        return a2;
    }

    @Override // com.sun.javafx.geom.transform.BaseTransform
    public BaseTransform deriveWithPreTranslation(double mxt, double myt) {
        this.mxt += mxt;
        this.myt += myt;
        if (this.mxt != 0.0d || this.myt != 0.0d) {
            this.state |= 1;
            this.type |= 1;
        } else {
            this.state &= -2;
            if (this.type != -1) {
                this.type &= -2;
            }
        }
        return this;
    }

    @Override // com.sun.javafx.geom.transform.BaseTransform
    public BaseTransform deriveWithConcatenation(double mxx, double myx, double mxy, double myy, double mxt, double myt) {
        BaseTransform tmpTx = getInstance(mxx, myx, mxy, myy, mxt, myt);
        concatenate(tmpTx);
        return this;
    }

    @Override // com.sun.javafx.geom.transform.BaseTransform
    public BaseTransform deriveWithConcatenation(double mxx, double mxy, double mxz, double mxt, double myx, double myy, double myz, double myt, double mzx, double mzy, double mzz, double mzt) {
        if (mxz == 0.0d && myz == 0.0d && mzx == 0.0d && mzy == 0.0d && mzz == 1.0d && mzt == 0.0d) {
            concatenate(mxx, mxy, mxt, myx, myy, myt);
            return this;
        }
        Affine3D t3d = new Affine3D(this);
        t3d.concatenate(mxx, mxy, mxz, mxt, myx, myy, myz, myt, mzx, mzy, mzz, mzt);
        return t3d;
    }

    @Override // com.sun.javafx.geom.transform.BaseTransform
    public BaseTransform deriveWithConcatenation(BaseTransform tx) {
        if (tx.is2D()) {
            concatenate(tx);
            return this;
        }
        Affine3D t3d = new Affine3D(this);
        t3d.concatenate(tx);
        return t3d;
    }

    @Override // com.sun.javafx.geom.transform.BaseTransform
    public BaseTransform deriveWithPreConcatenation(BaseTransform tx) {
        if (tx.is2D()) {
            preConcatenate(tx);
            return this;
        }
        Affine3D t3d = new Affine3D(this);
        t3d.preConcatenate(tx);
        return t3d;
    }

    @Override // com.sun.javafx.geom.transform.BaseTransform
    public BaseTransform deriveWithNewTransform(BaseTransform tx) {
        if (tx.is2D()) {
            setTransform(tx);
            return this;
        }
        return getInstance(tx);
    }

    @Override // com.sun.javafx.geom.transform.BaseTransform
    public BaseTransform copy() {
        return new Affine2D(this);
    }

    static {
        long bits = (0 * 31) + Double.doubleToLongBits(IDENTITY_TRANSFORM.getMzz());
        BASE_HASH = (((((((bits * 31) + Double.doubleToLongBits(IDENTITY_TRANSFORM.getMzy())) * 31) + Double.doubleToLongBits(IDENTITY_TRANSFORM.getMzx())) * 31) + Double.doubleToLongBits(IDENTITY_TRANSFORM.getMyz())) * 31) + Double.doubleToLongBits(IDENTITY_TRANSFORM.getMxz());
    }

    @Override // com.sun.javafx.geom.transform.BaseTransform
    public int hashCode() {
        if (isIdentity()) {
            return 0;
        }
        long bits = BASE_HASH;
        long bits2 = (((((((((((((bits * 31) + Double.doubleToLongBits(getMyy())) * 31) + Double.doubleToLongBits(getMyx())) * 31) + Double.doubleToLongBits(getMxy())) * 31) + Double.doubleToLongBits(getMxx())) * 31) + Double.doubleToLongBits(0.0d)) * 31) + Double.doubleToLongBits(getMyt())) * 31) + Double.doubleToLongBits(getMxt());
        return ((int) bits2) ^ ((int) (bits2 >> 32));
    }

    @Override // com.sun.javafx.geom.transform.BaseTransform
    public boolean equals(Object obj) {
        if (obj instanceof BaseTransform) {
            BaseTransform a2 = (BaseTransform) obj;
            return a2.getType() <= 127 && a2.getMxx() == this.mxx && a2.getMxy() == this.mxy && a2.getMxt() == this.mxt && a2.getMyx() == this.myx && a2.getMyy() == this.myy && a2.getMyt() == this.myt;
        }
        return false;
    }
}
