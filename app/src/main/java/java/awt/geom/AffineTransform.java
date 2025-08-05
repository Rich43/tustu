package java.awt.geom;

import java.awt.Shape;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.beans.ConstructorProperties;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/* loaded from: rt.jar:java/awt/geom/AffineTransform.class */
public class AffineTransform implements Cloneable, Serializable {
    private static final int TYPE_UNKNOWN = -1;
    public static final int TYPE_IDENTITY = 0;
    public static final int TYPE_TRANSLATION = 1;
    public static final int TYPE_UNIFORM_SCALE = 2;
    public static final int TYPE_GENERAL_SCALE = 4;
    public static final int TYPE_MASK_SCALE = 6;
    public static final int TYPE_FLIP = 64;
    public static final int TYPE_QUADRANT_ROTATION = 8;
    public static final int TYPE_GENERAL_ROTATION = 16;
    public static final int TYPE_MASK_ROTATION = 24;
    public static final int TYPE_GENERAL_TRANSFORM = 32;
    static final int APPLY_IDENTITY = 0;
    static final int APPLY_TRANSLATE = 1;
    static final int APPLY_SCALE = 2;
    static final int APPLY_SHEAR = 4;
    private static final int HI_SHIFT = 3;
    private static final int HI_IDENTITY = 0;
    private static final int HI_TRANSLATE = 8;
    private static final int HI_SCALE = 16;
    private static final int HI_SHEAR = 32;
    double m00;
    double m10;
    double m01;
    double m11;
    double m02;
    double m12;
    transient int state;
    private transient int type;
    private static final int[] rot90conversion = {4, 5, 4, 5, 2, 3, 6, 7};
    private static final long serialVersionUID = 1330973210523860834L;

    private AffineTransform(double d2, double d3, double d4, double d5, double d6, double d7, int i2) {
        this.m00 = d2;
        this.m10 = d3;
        this.m01 = d4;
        this.m11 = d5;
        this.m02 = d6;
        this.m12 = d7;
        this.state = i2;
        this.type = -1;
    }

    public AffineTransform() {
        this.m11 = 1.0d;
        this.m00 = 1.0d;
    }

    public AffineTransform(AffineTransform affineTransform) {
        this.m00 = affineTransform.m00;
        this.m10 = affineTransform.m10;
        this.m01 = affineTransform.m01;
        this.m11 = affineTransform.m11;
        this.m02 = affineTransform.m02;
        this.m12 = affineTransform.m12;
        this.state = affineTransform.state;
        this.type = affineTransform.type;
    }

    @ConstructorProperties({"scaleX", "shearY", "shearX", "scaleY", "translateX", "translateY"})
    public AffineTransform(float f2, float f3, float f4, float f5, float f6, float f7) {
        this.m00 = f2;
        this.m10 = f3;
        this.m01 = f4;
        this.m11 = f5;
        this.m02 = f6;
        this.m12 = f7;
        updateState();
    }

    public AffineTransform(float[] fArr) {
        this.m00 = fArr[0];
        this.m10 = fArr[1];
        this.m01 = fArr[2];
        this.m11 = fArr[3];
        if (fArr.length > 5) {
            this.m02 = fArr[4];
            this.m12 = fArr[5];
        }
        updateState();
    }

    public AffineTransform(double d2, double d3, double d4, double d5, double d6, double d7) {
        this.m00 = d2;
        this.m10 = d3;
        this.m01 = d4;
        this.m11 = d5;
        this.m02 = d6;
        this.m12 = d7;
        updateState();
    }

    public AffineTransform(double[] dArr) {
        this.m00 = dArr[0];
        this.m10 = dArr[1];
        this.m01 = dArr[2];
        this.m11 = dArr[3];
        if (dArr.length > 5) {
            this.m02 = dArr[4];
            this.m12 = dArr[5];
        }
        updateState();
    }

    public static AffineTransform getTranslateInstance(double d2, double d3) {
        AffineTransform affineTransform = new AffineTransform();
        affineTransform.setToTranslation(d2, d3);
        return affineTransform;
    }

    public static AffineTransform getRotateInstance(double d2) {
        AffineTransform affineTransform = new AffineTransform();
        affineTransform.setToRotation(d2);
        return affineTransform;
    }

    public static AffineTransform getRotateInstance(double d2, double d3, double d4) {
        AffineTransform affineTransform = new AffineTransform();
        affineTransform.setToRotation(d2, d3, d4);
        return affineTransform;
    }

    public static AffineTransform getRotateInstance(double d2, double d3) {
        AffineTransform affineTransform = new AffineTransform();
        affineTransform.setToRotation(d2, d3);
        return affineTransform;
    }

    public static AffineTransform getRotateInstance(double d2, double d3, double d4, double d5) {
        AffineTransform affineTransform = new AffineTransform();
        affineTransform.setToRotation(d2, d3, d4, d5);
        return affineTransform;
    }

    public static AffineTransform getQuadrantRotateInstance(int i2) {
        AffineTransform affineTransform = new AffineTransform();
        affineTransform.setToQuadrantRotation(i2);
        return affineTransform;
    }

    public static AffineTransform getQuadrantRotateInstance(int i2, double d2, double d3) {
        AffineTransform affineTransform = new AffineTransform();
        affineTransform.setToQuadrantRotation(i2, d2, d3);
        return affineTransform;
    }

    public static AffineTransform getScaleInstance(double d2, double d3) {
        AffineTransform affineTransform = new AffineTransform();
        affineTransform.setToScale(d2, d3);
        return affineTransform;
    }

    public static AffineTransform getShearInstance(double d2, double d3) {
        AffineTransform affineTransform = new AffineTransform();
        affineTransform.setToShear(d2, d3);
        return affineTransform;
    }

    public int getType() {
        if (this.type == -1) {
            calculateType();
        }
        return this.type;
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Removed duplicated region for block: B:10:0x0069  */
    /* JADX WARN: Removed duplicated region for block: B:41:0x0108  */
    /* JADX WARN: Removed duplicated region for block: B:42:0x010c  */
    /* JADX WARN: Removed duplicated region for block: B:45:0x011a  */
    /* JADX WARN: Removed duplicated region for block: B:46:0x011e  */
    /* JADX WARN: Removed duplicated region for block: B:49:0x0125  */
    /* JADX WARN: Removed duplicated region for block: B:58:0x0156  */
    /* JADX WARN: Removed duplicated region for block: B:65:0x017c  */
    /* JADX WARN: Removed duplicated region for block: B:66:0x0180  */
    /* JADX WARN: Removed duplicated region for block: B:69:0x018e  */
    /* JADX WARN: Removed duplicated region for block: B:70:0x0192  */
    /* JADX WARN: Removed duplicated region for block: B:73:0x0199  */
    /* JADX WARN: Removed duplicated region for block: B:86:0x01dc  */
    /* JADX WARN: Removed duplicated region for block: B:8:0x0062  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private void calculateType() {
        /*
            Method dump skipped, instructions count: 536
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: java.awt.geom.AffineTransform.calculateType():void");
    }

    public double getDeterminant() {
        switch (this.state) {
            case 0:
            case 1:
                return 1.0d;
            case 2:
            case 3:
                return this.m00 * this.m11;
            case 4:
            case 5:
                return -(this.m01 * this.m10);
            case 6:
            case 7:
                break;
            default:
                stateError();
                break;
        }
        return (this.m00 * this.m11) - (this.m01 * this.m10);
    }

    void updateState() {
        if (this.m01 == 0.0d && this.m10 == 0.0d) {
            if (this.m00 == 1.0d && this.m11 == 1.0d) {
                if (this.m02 == 0.0d && this.m12 == 0.0d) {
                    this.state = 0;
                    this.type = 0;
                    return;
                } else {
                    this.state = 1;
                    this.type = 1;
                    return;
                }
            }
            if (this.m02 == 0.0d && this.m12 == 0.0d) {
                this.state = 2;
                this.type = -1;
                return;
            } else {
                this.state = 3;
                this.type = -1;
                return;
            }
        }
        if (this.m00 == 0.0d && this.m11 == 0.0d) {
            if (this.m02 == 0.0d && this.m12 == 0.0d) {
                this.state = 4;
                this.type = -1;
                return;
            } else {
                this.state = 5;
                this.type = -1;
                return;
            }
        }
        if (this.m02 == 0.0d && this.m12 == 0.0d) {
            this.state = 6;
            this.type = -1;
        } else {
            this.state = 7;
            this.type = -1;
        }
    }

    private void stateError() {
        throw new InternalError("missing case in transform state switch");
    }

    public void getMatrix(double[] dArr) {
        dArr[0] = this.m00;
        dArr[1] = this.m10;
        dArr[2] = this.m01;
        dArr[3] = this.m11;
        if (dArr.length > 5) {
            dArr[4] = this.m02;
            dArr[5] = this.m12;
        }
    }

    public double getScaleX() {
        return this.m00;
    }

    public double getScaleY() {
        return this.m11;
    }

    public double getShearX() {
        return this.m01;
    }

    public double getShearY() {
        return this.m10;
    }

    public double getTranslateX() {
        return this.m02;
    }

    public double getTranslateY() {
        return this.m12;
    }

    public void translate(double d2, double d3) {
        switch (this.state) {
            case 0:
                this.m02 = d2;
                this.m12 = d3;
                if (d2 != 0.0d || d3 != 0.0d) {
                    this.state = 1;
                    this.type = 1;
                    break;
                }
                break;
            case 1:
                this.m02 = d2 + this.m02;
                this.m12 = d3 + this.m12;
                if (this.m02 == 0.0d && this.m12 == 0.0d) {
                    this.state = 0;
                    this.type = 0;
                    break;
                }
                break;
            case 2:
                this.m02 = d2 * this.m00;
                this.m12 = d3 * this.m11;
                if (this.m02 != 0.0d || this.m12 != 0.0d) {
                    this.state = 3;
                    this.type |= 1;
                    break;
                }
                break;
            case 3:
                this.m02 = (d2 * this.m00) + this.m02;
                this.m12 = (d3 * this.m11) + this.m12;
                if (this.m02 == 0.0d && this.m12 == 0.0d) {
                    this.state = 2;
                    if (this.type != -1) {
                        this.type--;
                        break;
                    }
                }
                break;
            case 4:
                this.m02 = d3 * this.m01;
                this.m12 = d2 * this.m10;
                if (this.m02 != 0.0d || this.m12 != 0.0d) {
                    this.state = 5;
                    this.type |= 1;
                    break;
                }
                break;
            case 5:
                this.m02 = (d3 * this.m01) + this.m02;
                this.m12 = (d2 * this.m10) + this.m12;
                if (this.m02 == 0.0d && this.m12 == 0.0d) {
                    this.state = 4;
                    if (this.type != -1) {
                        this.type--;
                        break;
                    }
                }
                break;
            case 6:
                this.m02 = (d2 * this.m00) + (d3 * this.m01);
                this.m12 = (d2 * this.m10) + (d3 * this.m11);
                if (this.m02 != 0.0d || this.m12 != 0.0d) {
                    this.state = 7;
                    this.type |= 1;
                    break;
                }
                break;
            case 7:
                this.m02 = (d2 * this.m00) + (d3 * this.m01) + this.m02;
                this.m12 = (d2 * this.m10) + (d3 * this.m11) + this.m12;
                if (this.m02 == 0.0d && this.m12 == 0.0d) {
                    this.state = 6;
                    if (this.type != -1) {
                        this.type--;
                        break;
                    }
                }
                break;
            default:
                stateError();
                break;
        }
    }

    private final void rotate90() {
        double d2 = this.m00;
        this.m00 = this.m01;
        this.m01 = -d2;
        double d3 = this.m10;
        this.m10 = this.m11;
        this.m11 = -d3;
        int i2 = rot90conversion[this.state];
        if ((i2 & 6) == 2 && this.m00 == 1.0d && this.m11 == 1.0d) {
            i2 -= 2;
        }
        this.state = i2;
        this.type = -1;
    }

    private final void rotate180() {
        this.m00 = -this.m00;
        this.m11 = -this.m11;
        int i2 = this.state;
        if ((i2 & 4) != 0) {
            this.m01 = -this.m01;
            this.m10 = -this.m10;
        } else if (this.m00 == 1.0d && this.m11 == 1.0d) {
            this.state = i2 & (-3);
        } else {
            this.state = i2 | 2;
        }
        this.type = -1;
    }

    private final void rotate270() {
        double d2 = this.m00;
        this.m00 = -this.m01;
        this.m01 = d2;
        double d3 = this.m10;
        this.m10 = -this.m11;
        this.m11 = d3;
        int i2 = rot90conversion[this.state];
        if ((i2 & 6) == 2 && this.m00 == 1.0d && this.m11 == 1.0d) {
            i2 -= 2;
        }
        this.state = i2;
        this.type = -1;
    }

    public void rotate(double d2) {
        double dSin = Math.sin(d2);
        if (dSin == 1.0d) {
            rotate90();
            return;
        }
        if (dSin == -1.0d) {
            rotate270();
            return;
        }
        double dCos = Math.cos(d2);
        if (dCos == -1.0d) {
            rotate180();
            return;
        }
        if (dCos != 1.0d) {
            double d3 = this.m00;
            double d4 = this.m01;
            this.m00 = (dCos * d3) + (dSin * d4);
            this.m01 = ((-dSin) * d3) + (dCos * d4);
            double d5 = this.m10;
            double d6 = this.m11;
            this.m10 = (dCos * d5) + (dSin * d6);
            this.m11 = ((-dSin) * d5) + (dCos * d6);
            updateState();
        }
    }

    public void rotate(double d2, double d3, double d4) {
        translate(d3, d4);
        rotate(d2);
        translate(-d3, -d4);
    }

    public void rotate(double d2, double d3) {
        if (d3 == 0.0d) {
            if (d2 < 0.0d) {
                rotate180();
                return;
            }
            return;
        }
        if (d2 == 0.0d) {
            if (d3 > 0.0d) {
                rotate90();
                return;
            } else {
                rotate270();
                return;
            }
        }
        double dSqrt = Math.sqrt((d2 * d2) + (d3 * d3));
        double d4 = d3 / dSqrt;
        double d5 = d2 / dSqrt;
        double d6 = this.m00;
        double d7 = this.m01;
        this.m00 = (d5 * d6) + (d4 * d7);
        this.m01 = ((-d4) * d6) + (d5 * d7);
        double d8 = this.m10;
        double d9 = this.m11;
        this.m10 = (d5 * d8) + (d4 * d9);
        this.m11 = ((-d4) * d8) + (d5 * d9);
        updateState();
    }

    public void rotate(double d2, double d3, double d4, double d5) {
        translate(d4, d5);
        rotate(d2, d3);
        translate(-d4, -d5);
    }

    public void quadrantRotate(int i2) {
        switch (i2 & 3) {
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

    public void quadrantRotate(int i2, double d2, double d3) {
        switch (i2 & 3) {
            case 0:
                return;
            case 1:
                this.m02 += (d2 * (this.m00 - this.m01)) + (d3 * (this.m01 + this.m00));
                this.m12 += (d2 * (this.m10 - this.m11)) + (d3 * (this.m11 + this.m10));
                rotate90();
                break;
            case 2:
                this.m02 += (d2 * (this.m00 + this.m00)) + (d3 * (this.m01 + this.m01));
                this.m12 += (d2 * (this.m10 + this.m10)) + (d3 * (this.m11 + this.m11));
                rotate180();
                break;
            case 3:
                this.m02 += (d2 * (this.m00 + this.m01)) + (d3 * (this.m01 - this.m00));
                this.m12 += (d2 * (this.m10 + this.m11)) + (d3 * (this.m11 - this.m10));
                rotate270();
                break;
        }
        if (this.m02 == 0.0d && this.m12 == 0.0d) {
            this.state &= -2;
        } else {
            this.state |= 1;
        }
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Removed duplicated region for block: B:8:0x006d  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public void scale(double r7, double r9) {
        /*
            Method dump skipped, instructions count: 285
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: java.awt.geom.AffineTransform.scale(double, double):void");
    }

    public void shear(double d2, double d3) {
        int i2 = this.state;
        switch (i2) {
            case 0:
            case 1:
                this.m01 = d2;
                this.m10 = d3;
                if (this.m01 != 0.0d || this.m10 != 0.0d) {
                    this.state = i2 | 2 | 4;
                    this.type = -1;
                    break;
                }
                break;
            case 2:
            case 3:
                this.m01 = this.m00 * d2;
                this.m10 = this.m11 * d3;
                if (this.m01 != 0.0d || this.m10 != 0.0d) {
                    this.state = i2 | 4;
                }
                this.type = -1;
                break;
            case 4:
            case 5:
                this.m00 = this.m01 * d3;
                this.m11 = this.m10 * d2;
                if (this.m00 != 0.0d || this.m11 != 0.0d) {
                    this.state = i2 | 2;
                }
                this.type = -1;
                break;
            case 6:
            case 7:
                double d4 = this.m00;
                double d5 = this.m01;
                this.m00 = d4 + (d5 * d3);
                this.m01 = (d4 * d2) + d5;
                double d6 = this.m10;
                double d7 = this.m11;
                this.m10 = d6 + (d7 * d3);
                this.m11 = (d6 * d2) + d7;
                updateState();
                break;
            default:
                stateError();
                break;
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r4v0, types: [java.awt.geom.AffineTransform] */
    public void setToIdentity() {
        this.m11 = 1.0d;
        this.m00 = 1.0d;
        ?? r4 = 0;
        this.m12 = 0.0d;
        this.m02 = 0.0d;
        r4.m01 = this;
        this.m10 = this;
        this.state = 0;
        this.type = 0;
    }

    public void setToTranslation(double d2, double d3) {
        this.m00 = 1.0d;
        this.m10 = 0.0d;
        this.m01 = 0.0d;
        this.m11 = 1.0d;
        this.m02 = d2;
        this.m12 = d3;
        if (d2 != 0.0d || d3 != 0.0d) {
            this.state = 1;
            this.type = 1;
        } else {
            this.state = 0;
            this.type = 0;
        }
    }

    public void setToRotation(double d2) {
        double dCos;
        double dSin = Math.sin(d2);
        if (dSin == 1.0d || dSin == -1.0d) {
            dCos = 0.0d;
            this.state = 4;
            this.type = 8;
        } else {
            dCos = Math.cos(d2);
            if (dCos == -1.0d) {
                dSin = 0.0d;
                this.state = 2;
                this.type = 8;
            } else if (dCos == 1.0d) {
                dSin = 0.0d;
                this.state = 0;
                this.type = 0;
            } else {
                this.state = 6;
                this.type = 16;
            }
        }
        this.m00 = dCos;
        this.m10 = dSin;
        this.m01 = -dSin;
        this.m11 = dCos;
        this.m02 = 0.0d;
        this.m12 = 0.0d;
    }

    public void setToRotation(double d2, double d3, double d4) {
        setToRotation(d2);
        double d5 = this.m10;
        double d6 = 1.0d - this.m00;
        this.m02 = (d3 * d6) + (d4 * d5);
        this.m12 = (d4 * d6) - (d3 * d5);
        if (this.m02 != 0.0d || this.m12 != 0.0d) {
            this.state |= 1;
            this.type |= 1;
        }
    }

    public void setToRotation(double d2, double d3) {
        double d4;
        double d5;
        if (d3 == 0.0d) {
            d5 = 0.0d;
            if (d2 < 0.0d) {
                d4 = -1.0d;
                this.state = 2;
                this.type = 8;
            } else {
                d4 = 1.0d;
                this.state = 0;
                this.type = 0;
            }
        } else if (d2 == 0.0d) {
            d4 = 0.0d;
            d5 = d3 > 0.0d ? 1.0d : -1.0d;
            this.state = 4;
            this.type = 8;
        } else {
            double dSqrt = Math.sqrt((d2 * d2) + (d3 * d3));
            d4 = d2 / dSqrt;
            d5 = d3 / dSqrt;
            this.state = 6;
            this.type = 16;
        }
        this.m00 = d4;
        this.m10 = d5;
        this.m01 = -d5;
        this.m11 = d4;
        this.m02 = 0.0d;
        this.m12 = 0.0d;
    }

    public void setToRotation(double d2, double d3, double d4, double d5) {
        setToRotation(d2, d3);
        double d6 = this.m10;
        double d7 = 1.0d - this.m00;
        this.m02 = (d4 * d7) + (d5 * d6);
        this.m12 = (d5 * d7) - (d4 * d6);
        if (this.m02 != 0.0d || this.m12 != 0.0d) {
            this.state |= 1;
            this.type |= 1;
        }
    }

    public void setToQuadrantRotation(int i2) {
        switch (i2 & 3) {
            case 0:
                this.m00 = 1.0d;
                this.m10 = 0.0d;
                this.m01 = 0.0d;
                this.m11 = 1.0d;
                this.m02 = 0.0d;
                this.m12 = 0.0d;
                this.state = 0;
                this.type = 0;
                break;
            case 1:
                this.m00 = 0.0d;
                this.m10 = 1.0d;
                this.m01 = -1.0d;
                this.m11 = 0.0d;
                this.m02 = 0.0d;
                this.m12 = 0.0d;
                this.state = 4;
                this.type = 8;
                break;
            case 2:
                this.m00 = -1.0d;
                this.m10 = 0.0d;
                this.m01 = 0.0d;
                this.m11 = -1.0d;
                this.m02 = 0.0d;
                this.m12 = 0.0d;
                this.state = 2;
                this.type = 8;
                break;
            case 3:
                this.m00 = 0.0d;
                this.m10 = -1.0d;
                this.m01 = 1.0d;
                this.m11 = 0.0d;
                this.m02 = 0.0d;
                this.m12 = 0.0d;
                this.state = 4;
                this.type = 8;
                break;
        }
    }

    public void setToQuadrantRotation(int i2, double d2, double d3) {
        switch (i2 & 3) {
            case 0:
                this.m00 = 1.0d;
                this.m10 = 0.0d;
                this.m01 = 0.0d;
                this.m11 = 1.0d;
                this.m02 = 0.0d;
                this.m12 = 0.0d;
                this.state = 0;
                this.type = 0;
                break;
            case 1:
                this.m00 = 0.0d;
                this.m10 = 1.0d;
                this.m01 = -1.0d;
                this.m11 = 0.0d;
                this.m02 = d2 + d3;
                this.m12 = d3 - d2;
                if (this.m02 == 0.0d && this.m12 == 0.0d) {
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
                this.m00 = -1.0d;
                this.m10 = 0.0d;
                this.m01 = 0.0d;
                this.m11 = -1.0d;
                this.m02 = d2 + d2;
                this.m12 = d3 + d3;
                if (this.m02 == 0.0d && this.m12 == 0.0d) {
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
                this.m00 = 0.0d;
                this.m10 = -1.0d;
                this.m01 = 1.0d;
                this.m11 = 0.0d;
                this.m02 = d2 - d3;
                this.m12 = d3 + d2;
                if (this.m02 == 0.0d && this.m12 == 0.0d) {
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

    public void setToScale(double d2, double d3) {
        this.m00 = d2;
        this.m10 = 0.0d;
        this.m01 = 0.0d;
        this.m11 = d3;
        this.m02 = 0.0d;
        this.m12 = 0.0d;
        if (d2 != 1.0d || d3 != 1.0d) {
            this.state = 2;
            this.type = -1;
        } else {
            this.state = 0;
            this.type = 0;
        }
    }

    public void setToShear(double d2, double d3) {
        this.m00 = 1.0d;
        this.m01 = d2;
        this.m10 = d3;
        this.m11 = 1.0d;
        this.m02 = 0.0d;
        this.m12 = 0.0d;
        if (d2 != 0.0d || d3 != 0.0d) {
            this.state = 6;
            this.type = -1;
        } else {
            this.state = 0;
            this.type = 0;
        }
    }

    public void setTransform(AffineTransform affineTransform) {
        this.m00 = affineTransform.m00;
        this.m10 = affineTransform.m10;
        this.m01 = affineTransform.m01;
        this.m11 = affineTransform.m11;
        this.m02 = affineTransform.m02;
        this.m12 = affineTransform.m12;
        this.state = affineTransform.state;
        this.type = affineTransform.type;
    }

    public void setTransform(double d2, double d3, double d4, double d5, double d6, double d7) {
        this.m00 = d2;
        this.m10 = d3;
        this.m01 = d4;
        this.m11 = d5;
        this.m02 = d6;
        this.m12 = d7;
        updateState();
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    public void concatenate(AffineTransform affineTransform) {
        int i2 = this.state;
        int i3 = affineTransform.state;
        switch ((i3 << 3) | i2) {
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
                this.m02 = affineTransform.m02;
                this.m12 = affineTransform.m12;
                this.state = i3;
                this.type = affineTransform.type;
                break;
            case 9:
            case 10:
            case 11:
            case 12:
            case 13:
            case 14:
            case 15:
                translate(affineTransform.m02, affineTransform.m12);
                break;
            case 16:
                this.m00 = affineTransform.m00;
                this.m11 = affineTransform.m11;
                this.state = i3;
                this.type = affineTransform.type;
                break;
            case 17:
            case 18:
            case 19:
            case 20:
            case 21:
            case 22:
            case 23:
                scale(affineTransform.m00, affineTransform.m11);
                break;
            case 24:
                this.m00 = affineTransform.m00;
                this.m11 = affineTransform.m11;
                this.m02 = affineTransform.m02;
                this.m12 = affineTransform.m12;
                this.state = i3;
                this.type = affineTransform.type;
                break;
            case 25:
            case 26:
            case 27:
            case 28:
            case 29:
            case 30:
            case 31:
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
            default:
                double d2 = affineTransform.m00;
                double d3 = affineTransform.m01;
                double d4 = affineTransform.m02;
                double d5 = affineTransform.m10;
                double d6 = affineTransform.m11;
                double d7 = affineTransform.m12;
                switch (i2) {
                    case 1:
                        this.m00 = d2;
                        this.m01 = d3;
                        this.m02 += d4;
                        this.m10 = d5;
                        this.m11 = d6;
                        this.m12 += d7;
                        this.state = i3 | 1;
                        this.type = -1;
                        break;
                    case 2:
                    case 3:
                        double d8 = this.m00;
                        this.m00 = d2 * d8;
                        this.m01 = d3 * d8;
                        this.m02 += d4 * d8;
                        double d9 = this.m11;
                        this.m10 = d5 * d9;
                        this.m11 = d6 * d9;
                        this.m12 += d7 * d9;
                        updateState();
                        break;
                    case 4:
                    case 5:
                        double d10 = this.m01;
                        this.m00 = d5 * d10;
                        this.m01 = d6 * d10;
                        this.m02 += d7 * d10;
                        double d11 = this.m10;
                        this.m10 = d2 * d11;
                        this.m11 = d3 * d11;
                        this.m12 += d4 * d11;
                        updateState();
                        break;
                    case 6:
                        this.state = i2 | i3;
                        double d12 = this.m00;
                        double d13 = this.m01;
                        this.m00 = (d2 * d12) + (d5 * d13);
                        this.m01 = (d3 * d12) + (d6 * d13);
                        this.m02 += (d4 * d12) + (d7 * d13);
                        double d14 = this.m10;
                        double d15 = this.m11;
                        this.m10 = (d2 * d14) + (d5 * d15);
                        this.m11 = (d3 * d14) + (d6 * d15);
                        this.m12 += (d4 * d14) + (d7 * d15);
                        this.type = -1;
                        break;
                    case 7:
                        double d122 = this.m00;
                        double d132 = this.m01;
                        this.m00 = (d2 * d122) + (d5 * d132);
                        this.m01 = (d3 * d122) + (d6 * d132);
                        this.m02 += (d4 * d122) + (d7 * d132);
                        double d142 = this.m10;
                        double d152 = this.m11;
                        this.m10 = (d2 * d142) + (d5 * d152);
                        this.m11 = (d3 * d142) + (d6 * d152);
                        this.m12 += (d4 * d142) + (d7 * d152);
                        this.type = -1;
                        break;
                    default:
                        stateError();
                        this.state = i2 | i3;
                        double d1222 = this.m00;
                        double d1322 = this.m01;
                        this.m00 = (d2 * d1222) + (d5 * d1322);
                        this.m01 = (d3 * d1222) + (d6 * d1322);
                        this.m02 += (d4 * d1222) + (d7 * d1322);
                        double d1422 = this.m10;
                        double d1522 = this.m11;
                        this.m10 = (d2 * d1422) + (d5 * d1522);
                        this.m11 = (d3 * d1422) + (d6 * d1522);
                        this.m12 += (d4 * d1422) + (d7 * d1522);
                        this.type = -1;
                        break;
                }
            case 32:
                this.m01 = affineTransform.m01;
                this.m10 = affineTransform.m10;
                this.m11 = 0.0d;
                this.m00 = 0.0d;
                this.state = i3;
                this.type = affineTransform.type;
                break;
            case 33:
                this.m00 = 0.0d;
                this.m01 = affineTransform.m01;
                this.m10 = affineTransform.m10;
                this.m11 = 0.0d;
                this.state = 5;
                this.type = -1;
                break;
            case 34:
            case 35:
                this.m01 = this.m00 * affineTransform.m01;
                this.m00 = 0.0d;
                this.m10 = this.m11 * affineTransform.m10;
                this.m11 = 0.0d;
                this.state = i2 ^ 6;
                this.type = -1;
                break;
            case 36:
            case 37:
                this.m00 = this.m01 * affineTransform.m10;
                this.m01 = 0.0d;
                this.m11 = this.m10 * affineTransform.m01;
                this.m10 = 0.0d;
                this.state = i2 ^ 6;
                this.type = -1;
                break;
            case 38:
            case 39:
                double d16 = affineTransform.m01;
                double d17 = affineTransform.m10;
                double d18 = this.m00;
                this.m00 = this.m01 * d17;
                this.m01 = d18 * d16;
                double d19 = this.m10;
                this.m10 = this.m11 * d17;
                this.m11 = d19 * d16;
                this.type = -1;
                break;
            case 40:
                this.m02 = affineTransform.m02;
                this.m12 = affineTransform.m12;
                this.m01 = affineTransform.m01;
                this.m10 = affineTransform.m10;
                this.m11 = 0.0d;
                this.m00 = 0.0d;
                this.state = i3;
                this.type = affineTransform.type;
                break;
            case 48:
                this.m01 = affineTransform.m01;
                this.m10 = affineTransform.m10;
                this.m00 = affineTransform.m00;
                this.m11 = affineTransform.m11;
                this.state = i3;
                this.type = affineTransform.type;
                break;
            case 56:
                this.m01 = affineTransform.m01;
                this.m10 = affineTransform.m10;
                this.m00 = affineTransform.m00;
                this.m11 = affineTransform.m11;
                this.m02 = affineTransform.m02;
                this.m12 = affineTransform.m12;
                this.state = i3;
                this.type = affineTransform.type;
                break;
        }
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Removed duplicated region for block: B:12:0x011a  */
    /* JADX WARN: Removed duplicated region for block: B:15:0x0150  */
    /* JADX WARN: Removed duplicated region for block: B:18:0x016d  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public void preConcatenate(java.awt.geom.AffineTransform r10) {
        /*
            Method dump skipped, instructions count: 1011
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: java.awt.geom.AffineTransform.preConcatenate(java.awt.geom.AffineTransform):void");
    }

    public AffineTransform createInverse() throws NoninvertibleTransformException {
        switch (this.state) {
            case 0:
                return new AffineTransform();
            case 1:
                return new AffineTransform(1.0d, 0.0d, 0.0d, 1.0d, -this.m02, -this.m12, 1);
            case 2:
                if (this.m00 == 0.0d || this.m11 == 0.0d) {
                    throw new NoninvertibleTransformException("Determinant is 0");
                }
                return new AffineTransform(1.0d / this.m00, 0.0d, 0.0d, 1.0d / this.m11, 0.0d, 0.0d, 2);
            case 3:
                if (this.m00 == 0.0d || this.m11 == 0.0d) {
                    throw new NoninvertibleTransformException("Determinant is 0");
                }
                return new AffineTransform(1.0d / this.m00, 0.0d, 0.0d, 1.0d / this.m11, (-this.m02) / this.m00, (-this.m12) / this.m11, 3);
            case 4:
                if (this.m01 == 0.0d || this.m10 == 0.0d) {
                    throw new NoninvertibleTransformException("Determinant is 0");
                }
                return new AffineTransform(0.0d, 1.0d / this.m01, 1.0d / this.m10, 0.0d, 0.0d, 0.0d, 4);
            case 5:
                if (this.m01 == 0.0d || this.m10 == 0.0d) {
                    throw new NoninvertibleTransformException("Determinant is 0");
                }
                return new AffineTransform(0.0d, 1.0d / this.m01, 1.0d / this.m10, 0.0d, (-this.m12) / this.m10, (-this.m02) / this.m01, 5);
            case 6:
                double d2 = (this.m00 * this.m11) - (this.m01 * this.m10);
                if (Math.abs(d2) <= Double.MIN_VALUE) {
                    throw new NoninvertibleTransformException("Determinant is " + d2);
                }
                return new AffineTransform(this.m11 / d2, (-this.m10) / d2, (-this.m01) / d2, this.m00 / d2, 0.0d, 0.0d, 6);
            case 7:
                double d3 = (this.m00 * this.m11) - (this.m01 * this.m10);
                if (Math.abs(d3) <= Double.MIN_VALUE) {
                    throw new NoninvertibleTransformException("Determinant is " + d3);
                }
                return new AffineTransform(this.m11 / d3, (-this.m10) / d3, (-this.m01) / d3, this.m00 / d3, ((this.m01 * this.m12) - (this.m11 * this.m02)) / d3, ((this.m10 * this.m02) - (this.m00 * this.m12)) / d3, 7);
            default:
                stateError();
                return null;
        }
    }

    public void invert() throws NoninvertibleTransformException {
        switch (this.state) {
            case 0:
                return;
            case 1:
                this.m02 = -this.m02;
                this.m12 = -this.m12;
                return;
            case 2:
                double d2 = this.m00;
                double d3 = this.m11;
                if (d2 == 0.0d || d3 == 0.0d) {
                    throw new NoninvertibleTransformException("Determinant is 0");
                }
                this.m00 = 1.0d / d2;
                this.m11 = 1.0d / d3;
                return;
            case 3:
                double d4 = this.m00;
                double d5 = this.m02;
                double d6 = this.m11;
                double d7 = this.m12;
                if (d4 == 0.0d || d6 == 0.0d) {
                    throw new NoninvertibleTransformException("Determinant is 0");
                }
                this.m00 = 1.0d / d4;
                this.m11 = 1.0d / d6;
                this.m02 = (-d5) / d4;
                this.m12 = (-d7) / d6;
                return;
            case 4:
                double d8 = this.m01;
                double d9 = this.m10;
                if (d8 == 0.0d || d9 == 0.0d) {
                    throw new NoninvertibleTransformException("Determinant is 0");
                }
                this.m10 = 1.0d / d8;
                this.m01 = 1.0d / d9;
                return;
            case 5:
                double d10 = this.m01;
                double d11 = this.m02;
                double d12 = this.m10;
                double d13 = this.m12;
                if (d10 == 0.0d || d12 == 0.0d) {
                    throw new NoninvertibleTransformException("Determinant is 0");
                }
                this.m10 = 1.0d / d10;
                this.m01 = 1.0d / d12;
                this.m02 = (-d13) / d12;
                this.m12 = (-d11) / d10;
                return;
            case 6:
                double d14 = this.m00;
                double d15 = this.m01;
                double d16 = this.m10;
                double d17 = this.m11;
                double d18 = (d14 * d17) - (d15 * d16);
                if (Math.abs(d18) <= Double.MIN_VALUE) {
                    throw new NoninvertibleTransformException("Determinant is " + d18);
                }
                this.m00 = d17 / d18;
                this.m10 = (-d16) / d18;
                this.m01 = (-d15) / d18;
                this.m11 = d14 / d18;
                return;
            case 7:
                double d19 = this.m00;
                double d20 = this.m01;
                double d21 = this.m02;
                double d22 = this.m10;
                double d23 = this.m11;
                double d24 = this.m12;
                double d25 = (d19 * d23) - (d20 * d22);
                if (Math.abs(d25) <= Double.MIN_VALUE) {
                    throw new NoninvertibleTransformException("Determinant is " + d25);
                }
                this.m00 = d23 / d25;
                this.m10 = (-d22) / d25;
                this.m01 = (-d20) / d25;
                this.m11 = d19 / d25;
                this.m02 = ((d20 * d24) - (d23 * d21)) / d25;
                this.m12 = ((d22 * d21) - (d19 * d24)) / d25;
                return;
            default:
                stateError();
                return;
        }
    }

    public Point2D transform(Point2D point2D, Point2D point2D2) {
        if (point2D2 == null) {
            if (point2D instanceof Point2D.Double) {
                point2D2 = new Point2D.Double();
            } else {
                point2D2 = new Point2D.Float();
            }
        }
        double x2 = point2D.getX();
        double y2 = point2D.getY();
        switch (this.state) {
            case 0:
                point2D2.setLocation(x2, y2);
                return point2D2;
            case 1:
                point2D2.setLocation(x2 + this.m02, y2 + this.m12);
                return point2D2;
            case 2:
                point2D2.setLocation(x2 * this.m00, y2 * this.m11);
                return point2D2;
            case 3:
                point2D2.setLocation((x2 * this.m00) + this.m02, (y2 * this.m11) + this.m12);
                return point2D2;
            case 4:
                point2D2.setLocation(y2 * this.m01, x2 * this.m10);
                return point2D2;
            case 5:
                point2D2.setLocation((y2 * this.m01) + this.m02, (x2 * this.m10) + this.m12);
                return point2D2;
            case 6:
                point2D2.setLocation((x2 * this.m00) + (y2 * this.m01), (x2 * this.m10) + (y2 * this.m11));
                return point2D2;
            case 7:
                point2D2.setLocation((x2 * this.m00) + (y2 * this.m01) + this.m02, (x2 * this.m10) + (y2 * this.m11) + this.m12);
                return point2D2;
            default:
                stateError();
                return null;
        }
    }

    public void transform(Point2D[] point2DArr, int i2, Point2D[] point2DArr2, int i3, int i4) {
        int i5 = this.state;
        while (true) {
            i4--;
            if (i4 >= 0) {
                int i6 = i2;
                i2++;
                Point2D point2D = point2DArr[i6];
                double x2 = point2D.getX();
                double y2 = point2D.getY();
                int i7 = i3;
                i3++;
                Point2D point2D2 = point2DArr2[i7];
                if (point2D2 == null) {
                    if (point2D instanceof Point2D.Double) {
                        point2D2 = new Point2D.Double();
                    } else {
                        point2D2 = new Point2D.Float();
                    }
                    point2DArr2[i3 - 1] = point2D2;
                }
                switch (i5) {
                    case 0:
                        point2D2.setLocation(x2, y2);
                        break;
                    case 1:
                        point2D2.setLocation(x2 + this.m02, y2 + this.m12);
                        break;
                    case 2:
                        point2D2.setLocation(x2 * this.m00, y2 * this.m11);
                        break;
                    case 3:
                        point2D2.setLocation((x2 * this.m00) + this.m02, (y2 * this.m11) + this.m12);
                        break;
                    case 4:
                        point2D2.setLocation(y2 * this.m01, x2 * this.m10);
                        break;
                    case 5:
                        point2D2.setLocation((y2 * this.m01) + this.m02, (x2 * this.m10) + this.m12);
                        break;
                    case 6:
                        point2D2.setLocation((x2 * this.m00) + (y2 * this.m01), (x2 * this.m10) + (y2 * this.m11));
                        break;
                    case 7:
                        point2D2.setLocation((x2 * this.m00) + (y2 * this.m01) + this.m02, (x2 * this.m10) + (y2 * this.m11) + this.m12);
                        break;
                    default:
                        stateError();
                        return;
                }
            } else {
                return;
            }
        }
    }

    public void transform(float[] fArr, int i2, float[] fArr2, int i3, int i4) {
        if (fArr2 == fArr && i3 > i2 && i3 < i2 + (i4 * 2)) {
            System.arraycopy(fArr, i2, fArr2, i3, i4 * 2);
            i2 = i3;
        }
        switch (this.state) {
            case 0:
                if (fArr != fArr2 || i2 != i3) {
                    System.arraycopy(fArr, i2, fArr2, i3, i4 * 2);
                    break;
                }
                break;
            case 1:
                double d2 = this.m02;
                double d3 = this.m12;
                while (true) {
                    i4--;
                    if (i4 < 0) {
                        break;
                    } else {
                        int i5 = i3;
                        int i6 = i3 + 1;
                        int i7 = i2;
                        fArr2[i5] = (float) (fArr[i7] + d2);
                        i3 = i6 + 1;
                        i2 = i2 + 1 + 1;
                        fArr2[i6] = (float) (fArr[r11] + d3);
                    }
                }
            case 2:
                double d4 = this.m00;
                double d5 = this.m11;
                while (true) {
                    i4--;
                    if (i4 < 0) {
                        break;
                    } else {
                        int i8 = i3;
                        int i9 = i3 + 1;
                        int i10 = i2;
                        fArr2[i8] = (float) (d4 * fArr[i10]);
                        i3 = i9 + 1;
                        i2 = i2 + 1 + 1;
                        fArr2[i9] = (float) (d5 * fArr[r11]);
                    }
                }
            case 3:
                double d6 = this.m00;
                double d7 = this.m02;
                double d8 = this.m11;
                double d9 = this.m12;
                while (true) {
                    i4--;
                    if (i4 < 0) {
                        break;
                    } else {
                        int i11 = i3;
                        int i12 = i3 + 1;
                        int i13 = i2;
                        fArr2[i11] = (float) ((d6 * fArr[i13]) + d7);
                        i3 = i12 + 1;
                        i2 = i2 + 1 + 1;
                        fArr2[i12] = (float) ((d8 * fArr[r11]) + d9);
                    }
                }
            case 4:
                double d10 = this.m01;
                double d11 = this.m10;
                while (true) {
                    i4--;
                    if (i4 < 0) {
                        break;
                    } else {
                        int i14 = i2;
                        double d12 = fArr[i14];
                        int i15 = i3;
                        int i16 = i3 + 1;
                        i2 = i2 + 1 + 1;
                        fArr2[i15] = (float) (d10 * fArr[r11]);
                        i3 = i16 + 1;
                        fArr2[i16] = (float) (d11 * d12);
                    }
                }
            case 5:
                double d13 = this.m01;
                double d14 = this.m02;
                double d15 = this.m10;
                double d16 = this.m12;
                while (true) {
                    i4--;
                    if (i4 < 0) {
                        break;
                    } else {
                        int i17 = i2;
                        double d17 = fArr[i17];
                        int i18 = i3;
                        int i19 = i3 + 1;
                        i2 = i2 + 1 + 1;
                        fArr2[i18] = (float) ((d13 * fArr[r11]) + d14);
                        i3 = i19 + 1;
                        fArr2[i19] = (float) ((d15 * d17) + d16);
                    }
                }
            case 6:
                double d18 = this.m00;
                double d19 = this.m01;
                double d20 = this.m10;
                double d21 = this.m11;
                while (true) {
                    i4--;
                    if (i4 < 0) {
                        break;
                    } else {
                        int i20 = i2;
                        int i21 = i2 + 1;
                        double d22 = fArr[i20];
                        i2 = i21 + 1;
                        double d23 = fArr[i21];
                        int i22 = i3;
                        int i23 = i3 + 1;
                        fArr2[i22] = (float) ((d18 * d22) + (d19 * d23));
                        i3 = i23 + 1;
                        fArr2[i23] = (float) ((d20 * d22) + (d21 * d23));
                    }
                }
            case 7:
                double d24 = this.m00;
                double d25 = this.m01;
                double d26 = this.m02;
                double d27 = this.m10;
                double d28 = this.m11;
                double d29 = this.m12;
                while (true) {
                    i4--;
                    if (i4 < 0) {
                        break;
                    } else {
                        int i24 = i2;
                        int i25 = i2 + 1;
                        double d30 = fArr[i24];
                        i2 = i25 + 1;
                        double d31 = fArr[i25];
                        int i26 = i3;
                        int i27 = i3 + 1;
                        fArr2[i26] = (float) ((d24 * d30) + (d25 * d31) + d26);
                        i3 = i27 + 1;
                        fArr2[i27] = (float) ((d27 * d30) + (d28 * d31) + d29);
                    }
                }
            default:
                stateError();
                break;
        }
    }

    public void transform(double[] dArr, int i2, double[] dArr2, int i3, int i4) {
        if (dArr2 == dArr && i3 > i2 && i3 < i2 + (i4 * 2)) {
            System.arraycopy(dArr, i2, dArr2, i3, i4 * 2);
            i2 = i3;
        }
        switch (this.state) {
            case 0:
                if (dArr != dArr2 || i2 != i3) {
                    System.arraycopy(dArr, i2, dArr2, i3, i4 * 2);
                    break;
                }
                break;
            case 1:
                double d2 = this.m02;
                double d3 = this.m12;
                while (true) {
                    i4--;
                    if (i4 < 0) {
                        break;
                    } else {
                        int i5 = i3;
                        int i6 = i3 + 1;
                        int i7 = i2;
                        int i8 = i2 + 1;
                        dArr2[i5] = dArr[i7] + d2;
                        i3 = i6 + 1;
                        i2 = i8 + 1;
                        dArr2[i6] = dArr[i8] + d3;
                    }
                }
            case 2:
                double d4 = this.m00;
                double d5 = this.m11;
                while (true) {
                    i4--;
                    if (i4 < 0) {
                        break;
                    } else {
                        int i9 = i3;
                        int i10 = i3 + 1;
                        int i11 = i2;
                        int i12 = i2 + 1;
                        dArr2[i9] = d4 * dArr[i11];
                        i3 = i10 + 1;
                        i2 = i12 + 1;
                        dArr2[i10] = d5 * dArr[i12];
                    }
                }
            case 3:
                double d6 = this.m00;
                double d7 = this.m02;
                double d8 = this.m11;
                double d9 = this.m12;
                while (true) {
                    i4--;
                    if (i4 < 0) {
                        break;
                    } else {
                        int i13 = i3;
                        int i14 = i3 + 1;
                        int i15 = i2;
                        int i16 = i2 + 1;
                        dArr2[i13] = (d6 * dArr[i15]) + d7;
                        i3 = i14 + 1;
                        i2 = i16 + 1;
                        dArr2[i14] = (d8 * dArr[i16]) + d9;
                    }
                }
            case 4:
                double d10 = this.m01;
                double d11 = this.m10;
                while (true) {
                    i4--;
                    if (i4 < 0) {
                        break;
                    } else {
                        int i17 = i2;
                        int i18 = i2 + 1;
                        double d12 = dArr[i17];
                        int i19 = i3;
                        int i20 = i3 + 1;
                        i2 = i18 + 1;
                        dArr2[i19] = d10 * dArr[i18];
                        i3 = i20 + 1;
                        dArr2[i20] = d11 * d12;
                    }
                }
            case 5:
                double d13 = this.m01;
                double d14 = this.m02;
                double d15 = this.m10;
                double d16 = this.m12;
                while (true) {
                    i4--;
                    if (i4 < 0) {
                        break;
                    } else {
                        int i21 = i2;
                        int i22 = i2 + 1;
                        double d17 = dArr[i21];
                        int i23 = i3;
                        int i24 = i3 + 1;
                        i2 = i22 + 1;
                        dArr2[i23] = (d13 * dArr[i22]) + d14;
                        i3 = i24 + 1;
                        dArr2[i24] = (d15 * d17) + d16;
                    }
                }
            case 6:
                double d18 = this.m00;
                double d19 = this.m01;
                double d20 = this.m10;
                double d21 = this.m11;
                while (true) {
                    i4--;
                    if (i4 < 0) {
                        break;
                    } else {
                        int i25 = i2;
                        int i26 = i2 + 1;
                        double d22 = dArr[i25];
                        i2 = i26 + 1;
                        double d23 = dArr[i26];
                        int i27 = i3;
                        int i28 = i3 + 1;
                        dArr2[i27] = (d18 * d22) + (d19 * d23);
                        i3 = i28 + 1;
                        dArr2[i28] = (d20 * d22) + (d21 * d23);
                    }
                }
            case 7:
                double d24 = this.m00;
                double d25 = this.m01;
                double d26 = this.m02;
                double d27 = this.m10;
                double d28 = this.m11;
                double d29 = this.m12;
                while (true) {
                    i4--;
                    if (i4 < 0) {
                        break;
                    } else {
                        int i29 = i2;
                        int i30 = i2 + 1;
                        double d30 = dArr[i29];
                        i2 = i30 + 1;
                        double d31 = dArr[i30];
                        int i31 = i3;
                        int i32 = i3 + 1;
                        dArr2[i31] = (d24 * d30) + (d25 * d31) + d26;
                        i3 = i32 + 1;
                        dArr2[i32] = (d27 * d30) + (d28 * d31) + d29;
                    }
                }
            default:
                stateError();
                break;
        }
    }

    public void transform(float[] fArr, int i2, double[] dArr, int i3, int i4) {
        switch (this.state) {
            case 0:
                break;
            case 1:
                double d2 = this.m02;
                double d3 = this.m12;
                while (true) {
                    i4--;
                    if (i4 >= 0) {
                        int i5 = i3;
                        int i6 = i3 + 1;
                        int i7 = i2;
                        dArr[i5] = fArr[i7] + d2;
                        i3 = i6 + 1;
                        i2 = i2 + 1 + 1;
                        dArr[i6] = fArr[r11] + d3;
                    } else {
                        return;
                    }
                }
            case 2:
                double d4 = this.m00;
                double d5 = this.m11;
                while (true) {
                    i4--;
                    if (i4 >= 0) {
                        int i8 = i3;
                        int i9 = i3 + 1;
                        int i10 = i2;
                        dArr[i8] = d4 * fArr[i10];
                        i3 = i9 + 1;
                        i2 = i2 + 1 + 1;
                        dArr[i9] = d5 * fArr[r11];
                    } else {
                        return;
                    }
                }
            case 3:
                double d6 = this.m00;
                double d7 = this.m02;
                double d8 = this.m11;
                double d9 = this.m12;
                while (true) {
                    i4--;
                    if (i4 >= 0) {
                        int i11 = i3;
                        int i12 = i3 + 1;
                        int i13 = i2;
                        dArr[i11] = (d6 * fArr[i13]) + d7;
                        i3 = i12 + 1;
                        i2 = i2 + 1 + 1;
                        dArr[i12] = (d8 * fArr[r11]) + d9;
                    } else {
                        return;
                    }
                }
            case 4:
                double d10 = this.m01;
                double d11 = this.m10;
                while (true) {
                    i4--;
                    if (i4 >= 0) {
                        int i14 = i2;
                        double d12 = fArr[i14];
                        int i15 = i3;
                        int i16 = i3 + 1;
                        i2 = i2 + 1 + 1;
                        dArr[i15] = d10 * fArr[r11];
                        i3 = i16 + 1;
                        dArr[i16] = d11 * d12;
                    } else {
                        return;
                    }
                }
            case 5:
                double d13 = this.m01;
                double d14 = this.m02;
                double d15 = this.m10;
                double d16 = this.m12;
                while (true) {
                    i4--;
                    if (i4 >= 0) {
                        int i17 = i2;
                        double d17 = fArr[i17];
                        int i18 = i3;
                        int i19 = i3 + 1;
                        i2 = i2 + 1 + 1;
                        dArr[i18] = (d13 * fArr[r11]) + d14;
                        i3 = i19 + 1;
                        dArr[i19] = (d15 * d17) + d16;
                    } else {
                        return;
                    }
                }
            case 6:
                double d18 = this.m00;
                double d19 = this.m01;
                double d20 = this.m10;
                double d21 = this.m11;
                while (true) {
                    i4--;
                    if (i4 >= 0) {
                        int i20 = i2;
                        int i21 = i2 + 1;
                        double d22 = fArr[i20];
                        i2 = i21 + 1;
                        double d23 = fArr[i21];
                        int i22 = i3;
                        int i23 = i3 + 1;
                        dArr[i22] = (d18 * d22) + (d19 * d23);
                        i3 = i23 + 1;
                        dArr[i23] = (d20 * d22) + (d21 * d23);
                    } else {
                        return;
                    }
                }
            case 7:
                double d24 = this.m00;
                double d25 = this.m01;
                double d26 = this.m02;
                double d27 = this.m10;
                double d28 = this.m11;
                double d29 = this.m12;
                while (true) {
                    i4--;
                    if (i4 >= 0) {
                        int i24 = i2;
                        int i25 = i2 + 1;
                        double d30 = fArr[i24];
                        i2 = i25 + 1;
                        double d31 = fArr[i25];
                        int i26 = i3;
                        int i27 = i3 + 1;
                        dArr[i26] = (d24 * d30) + (d25 * d31) + d26;
                        i3 = i27 + 1;
                        dArr[i27] = (d27 * d30) + (d28 * d31) + d29;
                    } else {
                        return;
                    }
                }
            default:
                stateError();
                return;
        }
        while (true) {
            i4--;
            if (i4 >= 0) {
                int i28 = i3;
                int i29 = i3 + 1;
                int i30 = i2;
                dArr[i28] = fArr[i30];
                i3 = i29 + 1;
                i2 = i2 + 1 + 1;
                dArr[i29] = fArr[r11];
            } else {
                return;
            }
        }
    }

    public void transform(double[] dArr, int i2, float[] fArr, int i3, int i4) {
        switch (this.state) {
            case 0:
                break;
            case 1:
                double d2 = this.m02;
                double d3 = this.m12;
                while (true) {
                    i4--;
                    if (i4 >= 0) {
                        int i5 = i3;
                        int i6 = i3 + 1;
                        int i7 = i2;
                        int i8 = i2 + 1;
                        fArr[i5] = (float) (dArr[i7] + d2);
                        i3 = i6 + 1;
                        i2 = i8 + 1;
                        fArr[i6] = (float) (dArr[i8] + d3);
                    } else {
                        return;
                    }
                }
            case 2:
                double d4 = this.m00;
                double d5 = this.m11;
                while (true) {
                    i4--;
                    if (i4 >= 0) {
                        int i9 = i3;
                        int i10 = i3 + 1;
                        int i11 = i2;
                        int i12 = i2 + 1;
                        fArr[i9] = (float) (d4 * dArr[i11]);
                        i3 = i10 + 1;
                        i2 = i12 + 1;
                        fArr[i10] = (float) (d5 * dArr[i12]);
                    } else {
                        return;
                    }
                }
            case 3:
                double d6 = this.m00;
                double d7 = this.m02;
                double d8 = this.m11;
                double d9 = this.m12;
                while (true) {
                    i4--;
                    if (i4 >= 0) {
                        int i13 = i3;
                        int i14 = i3 + 1;
                        int i15 = i2;
                        int i16 = i2 + 1;
                        fArr[i13] = (float) ((d6 * dArr[i15]) + d7);
                        i3 = i14 + 1;
                        i2 = i16 + 1;
                        fArr[i14] = (float) ((d8 * dArr[i16]) + d9);
                    } else {
                        return;
                    }
                }
            case 4:
                double d10 = this.m01;
                double d11 = this.m10;
                while (true) {
                    i4--;
                    if (i4 >= 0) {
                        int i17 = i2;
                        int i18 = i2 + 1;
                        double d12 = dArr[i17];
                        int i19 = i3;
                        int i20 = i3 + 1;
                        i2 = i18 + 1;
                        fArr[i19] = (float) (d10 * dArr[i18]);
                        i3 = i20 + 1;
                        fArr[i20] = (float) (d11 * d12);
                    } else {
                        return;
                    }
                }
            case 5:
                double d13 = this.m01;
                double d14 = this.m02;
                double d15 = this.m10;
                double d16 = this.m12;
                while (true) {
                    i4--;
                    if (i4 >= 0) {
                        int i21 = i2;
                        int i22 = i2 + 1;
                        double d17 = dArr[i21];
                        int i23 = i3;
                        int i24 = i3 + 1;
                        i2 = i22 + 1;
                        fArr[i23] = (float) ((d13 * dArr[i22]) + d14);
                        i3 = i24 + 1;
                        fArr[i24] = (float) ((d15 * d17) + d16);
                    } else {
                        return;
                    }
                }
            case 6:
                double d18 = this.m00;
                double d19 = this.m01;
                double d20 = this.m10;
                double d21 = this.m11;
                while (true) {
                    i4--;
                    if (i4 >= 0) {
                        int i25 = i2;
                        int i26 = i2 + 1;
                        double d22 = dArr[i25];
                        i2 = i26 + 1;
                        double d23 = dArr[i26];
                        int i27 = i3;
                        int i28 = i3 + 1;
                        fArr[i27] = (float) ((d18 * d22) + (d19 * d23));
                        i3 = i28 + 1;
                        fArr[i28] = (float) ((d20 * d22) + (d21 * d23));
                    } else {
                        return;
                    }
                }
            case 7:
                double d24 = this.m00;
                double d25 = this.m01;
                double d26 = this.m02;
                double d27 = this.m10;
                double d28 = this.m11;
                double d29 = this.m12;
                while (true) {
                    i4--;
                    if (i4 >= 0) {
                        int i29 = i2;
                        int i30 = i2 + 1;
                        double d30 = dArr[i29];
                        i2 = i30 + 1;
                        double d31 = dArr[i30];
                        int i31 = i3;
                        int i32 = i3 + 1;
                        fArr[i31] = (float) ((d24 * d30) + (d25 * d31) + d26);
                        i3 = i32 + 1;
                        fArr[i32] = (float) ((d27 * d30) + (d28 * d31) + d29);
                    } else {
                        return;
                    }
                }
            default:
                stateError();
                return;
        }
        while (true) {
            i4--;
            if (i4 >= 0) {
                int i33 = i3;
                int i34 = i3 + 1;
                int i35 = i2;
                int i36 = i2 + 1;
                fArr[i33] = (float) dArr[i35];
                i3 = i34 + 1;
                i2 = i36 + 1;
                fArr[i34] = (float) dArr[i36];
            } else {
                return;
            }
        }
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Removed duplicated region for block: B:14:0x0091  */
    /* JADX WARN: Removed duplicated region for block: B:16:0x00ad  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public java.awt.geom.Point2D inverseTransform(java.awt.geom.Point2D r11, java.awt.geom.Point2D r12) throws java.awt.geom.NoninvertibleTransformException {
        /*
            Method dump skipped, instructions count: 367
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: java.awt.geom.AffineTransform.inverseTransform(java.awt.geom.Point2D, java.awt.geom.Point2D):java.awt.geom.Point2D");
    }

    public void inverseTransform(double[] dArr, int i2, double[] dArr2, int i3, int i4) throws NoninvertibleTransformException {
        if (dArr2 == dArr && i3 > i2 && i3 < i2 + (i4 * 2)) {
            System.arraycopy(dArr, i2, dArr2, i3, i4 * 2);
            i2 = i3;
        }
        switch (this.state) {
            case 0:
                if (dArr != dArr2 || i2 != i3) {
                    System.arraycopy(dArr, i2, dArr2, i3, i4 * 2);
                    return;
                }
                return;
            case 1:
                double d2 = this.m02;
                double d3 = this.m12;
                while (true) {
                    i4--;
                    if (i4 >= 0) {
                        int i5 = i3;
                        int i6 = i3 + 1;
                        int i7 = i2;
                        int i8 = i2 + 1;
                        dArr2[i5] = dArr[i7] - d2;
                        i3 = i6 + 1;
                        i2 = i8 + 1;
                        dArr2[i6] = dArr[i8] - d3;
                    } else {
                        return;
                    }
                }
            case 2:
                double d4 = this.m00;
                double d5 = this.m11;
                if (d4 == 0.0d || d5 == 0.0d) {
                    throw new NoninvertibleTransformException("Determinant is 0");
                }
                while (true) {
                    i4--;
                    if (i4 >= 0) {
                        int i9 = i3;
                        int i10 = i3 + 1;
                        int i11 = i2;
                        int i12 = i2 + 1;
                        dArr2[i9] = dArr[i11] / d4;
                        i3 = i10 + 1;
                        i2 = i12 + 1;
                        dArr2[i10] = dArr[i12] / d5;
                    } else {
                        return;
                    }
                }
                break;
            case 3:
                double d6 = this.m00;
                double d7 = this.m02;
                double d8 = this.m11;
                double d9 = this.m12;
                if (d6 == 0.0d || d8 == 0.0d) {
                    throw new NoninvertibleTransformException("Determinant is 0");
                }
                while (true) {
                    i4--;
                    if (i4 >= 0) {
                        int i13 = i3;
                        int i14 = i3 + 1;
                        int i15 = i2;
                        int i16 = i2 + 1;
                        dArr2[i13] = (dArr[i15] - d7) / d6;
                        i3 = i14 + 1;
                        i2 = i16 + 1;
                        dArr2[i14] = (dArr[i16] - d9) / d8;
                    } else {
                        return;
                    }
                }
                break;
            case 4:
                double d10 = this.m01;
                double d11 = this.m10;
                if (d10 == 0.0d || d11 == 0.0d) {
                    throw new NoninvertibleTransformException("Determinant is 0");
                }
                while (true) {
                    i4--;
                    if (i4 >= 0) {
                        int i17 = i2;
                        int i18 = i2 + 1;
                        double d12 = dArr[i17];
                        int i19 = i3;
                        int i20 = i3 + 1;
                        i2 = i18 + 1;
                        dArr2[i19] = dArr[i18] / d11;
                        i3 = i20 + 1;
                        dArr2[i20] = d12 / d10;
                    } else {
                        return;
                    }
                }
                break;
            case 5:
                double d13 = this.m01;
                double d14 = this.m02;
                double d15 = this.m10;
                double d16 = this.m12;
                if (d13 == 0.0d || d15 == 0.0d) {
                    throw new NoninvertibleTransformException("Determinant is 0");
                }
                while (true) {
                    i4--;
                    if (i4 >= 0) {
                        int i21 = i2;
                        int i22 = i2 + 1;
                        double d17 = dArr[i21] - d14;
                        int i23 = i3;
                        int i24 = i3 + 1;
                        i2 = i22 + 1;
                        dArr2[i23] = (dArr[i22] - d16) / d15;
                        i3 = i24 + 1;
                        dArr2[i24] = d17 / d13;
                    } else {
                        return;
                    }
                }
                break;
            case 6:
                double d18 = this.m00;
                double d19 = this.m01;
                double d20 = this.m10;
                double d21 = this.m11;
                double d22 = (d18 * d21) - (d19 * d20);
                if (Math.abs(d22) <= Double.MIN_VALUE) {
                    throw new NoninvertibleTransformException("Determinant is " + d22);
                }
                while (true) {
                    i4--;
                    if (i4 >= 0) {
                        int i25 = i2;
                        int i26 = i2 + 1;
                        double d23 = dArr[i25];
                        i2 = i26 + 1;
                        double d24 = dArr[i26];
                        int i27 = i3;
                        int i28 = i3 + 1;
                        dArr2[i27] = ((d23 * d21) - (d24 * d19)) / d22;
                        i3 = i28 + 1;
                        dArr2[i28] = ((d24 * d18) - (d23 * d20)) / d22;
                    } else {
                        return;
                    }
                }
            case 7:
                double d25 = this.m00;
                double d26 = this.m01;
                double d27 = this.m02;
                double d28 = this.m10;
                double d29 = this.m11;
                double d30 = this.m12;
                double d31 = (d25 * d29) - (d26 * d28);
                if (Math.abs(d31) <= Double.MIN_VALUE) {
                    throw new NoninvertibleTransformException("Determinant is " + d31);
                }
                while (true) {
                    i4--;
                    if (i4 >= 0) {
                        int i29 = i2;
                        int i30 = i2 + 1;
                        double d32 = dArr[i29] - d27;
                        i2 = i30 + 1;
                        double d33 = dArr[i30] - d30;
                        int i31 = i3;
                        int i32 = i3 + 1;
                        dArr2[i31] = ((d32 * d29) - (d33 * d26)) / d31;
                        i3 = i32 + 1;
                        dArr2[i32] = ((d33 * d25) - (d32 * d28)) / d31;
                    } else {
                        return;
                    }
                }
            default:
                stateError();
                return;
        }
    }

    public Point2D deltaTransform(Point2D point2D, Point2D point2D2) {
        if (point2D2 == null) {
            if (point2D instanceof Point2D.Double) {
                point2D2 = new Point2D.Double();
            } else {
                point2D2 = new Point2D.Float();
            }
        }
        double x2 = point2D.getX();
        double y2 = point2D.getY();
        switch (this.state) {
            case 0:
            case 1:
                point2D2.setLocation(x2, y2);
                return point2D2;
            case 2:
            case 3:
                point2D2.setLocation(x2 * this.m00, y2 * this.m11);
                return point2D2;
            case 4:
            case 5:
                point2D2.setLocation(y2 * this.m01, x2 * this.m10);
                return point2D2;
            case 6:
            case 7:
                point2D2.setLocation((x2 * this.m00) + (y2 * this.m01), (x2 * this.m10) + (y2 * this.m11));
                return point2D2;
            default:
                stateError();
                return null;
        }
    }

    public void deltaTransform(double[] dArr, int i2, double[] dArr2, int i3, int i4) {
        if (dArr2 == dArr && i3 > i2 && i3 < i2 + (i4 * 2)) {
            System.arraycopy(dArr, i2, dArr2, i3, i4 * 2);
            i2 = i3;
        }
        switch (this.state) {
            case 0:
            case 1:
                if (dArr != dArr2 || i2 != i3) {
                    System.arraycopy(dArr, i2, dArr2, i3, i4 * 2);
                    break;
                }
                break;
            case 2:
            case 3:
                double d2 = this.m00;
                double d3 = this.m11;
                while (true) {
                    i4--;
                    if (i4 < 0) {
                        break;
                    } else {
                        int i5 = i3;
                        int i6 = i3 + 1;
                        int i7 = i2;
                        int i8 = i2 + 1;
                        dArr2[i5] = dArr[i7] * d2;
                        i3 = i6 + 1;
                        i2 = i8 + 1;
                        dArr2[i6] = dArr[i8] * d3;
                    }
                }
            case 4:
            case 5:
                double d4 = this.m01;
                double d5 = this.m10;
                while (true) {
                    i4--;
                    if (i4 < 0) {
                        break;
                    } else {
                        int i9 = i2;
                        int i10 = i2 + 1;
                        double d6 = dArr[i9];
                        int i11 = i3;
                        int i12 = i3 + 1;
                        i2 = i10 + 1;
                        dArr2[i11] = dArr[i10] * d4;
                        i3 = i12 + 1;
                        dArr2[i12] = d6 * d5;
                    }
                }
            case 6:
            case 7:
                double d7 = this.m00;
                double d8 = this.m01;
                double d9 = this.m10;
                double d10 = this.m11;
                while (true) {
                    i4--;
                    if (i4 < 0) {
                        break;
                    } else {
                        int i13 = i2;
                        int i14 = i2 + 1;
                        double d11 = dArr[i13];
                        i2 = i14 + 1;
                        double d12 = dArr[i14];
                        int i15 = i3;
                        int i16 = i3 + 1;
                        dArr2[i15] = (d11 * d7) + (d12 * d8);
                        i3 = i16 + 1;
                        dArr2[i16] = (d11 * d9) + (d12 * d10);
                    }
                }
            default:
                stateError();
                break;
        }
    }

    public Shape createTransformedShape(Shape shape) {
        if (shape == null) {
            return null;
        }
        return new Path2D.Double(shape, this);
    }

    private static double _matround(double d2) {
        return Math.rint(d2 * 1.0E15d) / 1.0E15d;
    }

    public String toString() {
        return "AffineTransform[[" + _matround(this.m00) + ", " + _matround(this.m01) + ", " + _matround(this.m02) + "], [" + _matround(this.m10) + ", " + _matround(this.m11) + ", " + _matround(this.m12) + "]]";
    }

    public boolean isIdentity() {
        return this.state == 0 || getType() == 0;
    }

    public Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException e2) {
            throw new InternalError(e2);
        }
    }

    public int hashCode() {
        long jDoubleToLongBits = (((((((((Double.doubleToLongBits(this.m00) * 31) + Double.doubleToLongBits(this.m01)) * 31) + Double.doubleToLongBits(this.m02)) * 31) + Double.doubleToLongBits(this.m10)) * 31) + Double.doubleToLongBits(this.m11)) * 31) + Double.doubleToLongBits(this.m12);
        return ((int) jDoubleToLongBits) ^ ((int) (jDoubleToLongBits >> 32));
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof AffineTransform)) {
            return false;
        }
        AffineTransform affineTransform = (AffineTransform) obj;
        return this.m00 == affineTransform.m00 && this.m01 == affineTransform.m01 && this.m02 == affineTransform.m02 && this.m10 == affineTransform.m10 && this.m11 == affineTransform.m11 && this.m12 == affineTransform.m12;
    }

    private void writeObject(ObjectOutputStream objectOutputStream) throws IOException, ClassNotFoundException {
        objectOutputStream.defaultWriteObject();
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        objectInputStream.defaultReadObject();
        updateState();
    }
}
