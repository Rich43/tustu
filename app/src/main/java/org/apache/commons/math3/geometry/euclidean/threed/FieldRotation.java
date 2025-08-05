package org.apache.commons.math3.geometry.euclidean.threed;

import java.io.Serializable;
import org.apache.commons.math3.Field;
import org.apache.commons.math3.RealFieldElement;
import org.apache.commons.math3.exception.MathArithmeticException;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.util.FastMath;
import org.apache.commons.math3.util.MathArrays;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/geometry/euclidean/threed/FieldRotation.class */
public class FieldRotation<T extends RealFieldElement<T>> implements Serializable {
    private static final long serialVersionUID = 20130224;
    private final T q0;
    private final T q1;
    private final T q2;
    private final T q3;

    public FieldRotation(T q0, T q1, T q2, T q3, boolean needsNormalization) {
        if (needsNormalization) {
            RealFieldElement realFieldElement = (RealFieldElement) ((RealFieldElement) ((RealFieldElement) ((RealFieldElement) ((RealFieldElement) ((RealFieldElement) q0.multiply(q0)).add((RealFieldElement) q1.multiply(q1))).add((RealFieldElement) q2.multiply(q2))).add((RealFieldElement) q3.multiply(q3))).sqrt()).reciprocal();
            this.q0 = (T) realFieldElement.multiply(q0);
            this.q1 = (T) realFieldElement.multiply(q1);
            this.q2 = (T) realFieldElement.multiply(q2);
            this.q3 = (T) realFieldElement.multiply(q3);
            return;
        }
        this.q0 = q0;
        this.q1 = q1;
        this.q2 = q2;
        this.q3 = q3;
    }

    @Deprecated
    public FieldRotation(FieldVector3D<T> axis, T angle) throws MathIllegalArgumentException {
        this(axis, angle, RotationConvention.VECTOR_OPERATOR);
    }

    public FieldRotation(FieldVector3D<T> axis, T angle, RotationConvention convention) throws MathIllegalArgumentException {
        RealFieldElement norm = axis.getNorm();
        if (norm.getReal() == 0.0d) {
            throw new MathIllegalArgumentException(LocalizedFormats.ZERO_NORM_FOR_ROTATION_AXIS, new Object[0]);
        }
        RealFieldElement realFieldElement = (RealFieldElement) angle.multiply(convention == RotationConvention.VECTOR_OPERATOR ? -0.5d : 0.5d);
        RealFieldElement realFieldElement2 = (RealFieldElement) ((RealFieldElement) realFieldElement.sin()).divide(norm);
        this.q0 = (T) realFieldElement.cos();
        this.q1 = (T) realFieldElement2.multiply(axis.getX());
        this.q2 = (T) realFieldElement2.multiply(axis.getY());
        this.q3 = (T) realFieldElement2.multiply(axis.getZ());
    }

    public FieldRotation(T[][] tArr, double d2) throws NotARotationMatrixException {
        if (tArr.length != 3 || tArr[0].length != 3 || tArr[1].length != 3 || tArr[2].length != 3) {
            throw new NotARotationMatrixException(LocalizedFormats.ROTATION_MATRIX_DIMENSIONS, Integer.valueOf(tArr.length), Integer.valueOf(tArr[0].length));
        }
        RealFieldElement[][] realFieldElementArrOrthogonalizeMatrix = orthogonalizeMatrix(tArr, d2);
        RealFieldElement realFieldElement = (RealFieldElement) ((RealFieldElement) ((RealFieldElement) realFieldElementArrOrthogonalizeMatrix[0][0].multiply((RealFieldElement) ((RealFieldElement) realFieldElementArrOrthogonalizeMatrix[1][1].multiply(realFieldElementArrOrthogonalizeMatrix[2][2])).subtract((RealFieldElement) realFieldElementArrOrthogonalizeMatrix[2][1].multiply(realFieldElementArrOrthogonalizeMatrix[1][2])))).subtract((RealFieldElement) realFieldElementArrOrthogonalizeMatrix[1][0].multiply((RealFieldElement) ((RealFieldElement) realFieldElementArrOrthogonalizeMatrix[0][1].multiply(realFieldElementArrOrthogonalizeMatrix[2][2])).subtract((RealFieldElement) realFieldElementArrOrthogonalizeMatrix[2][1].multiply(realFieldElementArrOrthogonalizeMatrix[0][2]))))).add((RealFieldElement) realFieldElementArrOrthogonalizeMatrix[2][0].multiply((RealFieldElement) ((RealFieldElement) realFieldElementArrOrthogonalizeMatrix[0][1].multiply(realFieldElementArrOrthogonalizeMatrix[1][2])).subtract((RealFieldElement) realFieldElementArrOrthogonalizeMatrix[1][1].multiply(realFieldElementArrOrthogonalizeMatrix[0][2]))));
        if (realFieldElement.getReal() < 0.0d) {
            throw new NotARotationMatrixException(LocalizedFormats.CLOSEST_ORTHOGONAL_MATRIX_HAS_NEGATIVE_DETERMINANT, realFieldElement);
        }
        RealFieldElement[] realFieldElementArrMat2quat = mat2quat(realFieldElementArrOrthogonalizeMatrix);
        this.q0 = (T) realFieldElementArrMat2quat[0];
        this.q1 = (T) realFieldElementArrMat2quat[1];
        this.q2 = (T) realFieldElementArrMat2quat[2];
        this.q3 = (T) realFieldElementArrMat2quat[3];
    }

    public FieldRotation(FieldVector3D<T> fieldVector3D, FieldVector3D<T> fieldVector3D2, FieldVector3D<T> fieldVector3D3, FieldVector3D<T> fieldVector3D4) throws MathArithmeticException {
        FieldVector3D<T> fieldVector3DNormalize = FieldVector3D.crossProduct(fieldVector3D, fieldVector3D2).normalize();
        FieldVector3D<T> fieldVector3DNormalize2 = FieldVector3D.crossProduct(fieldVector3DNormalize, fieldVector3D).normalize();
        FieldVector3D<T> fieldVector3DNormalize3 = fieldVector3D.normalize();
        FieldVector3D<T> fieldVector3DNormalize4 = FieldVector3D.crossProduct(fieldVector3D3, fieldVector3D4).normalize();
        FieldVector3D<T> fieldVector3DNormalize5 = FieldVector3D.crossProduct(fieldVector3DNormalize4, fieldVector3D3).normalize();
        FieldVector3D<T> fieldVector3DNormalize6 = fieldVector3D3.normalize();
        RealFieldElement[][] realFieldElementArr = (RealFieldElement[][]) MathArrays.buildArray(fieldVector3DNormalize3.getX().getField2(), 3, 3);
        realFieldElementArr[0][0] = (RealFieldElement) ((RealFieldElement) ((RealFieldElement) fieldVector3DNormalize3.getX().multiply(fieldVector3DNormalize6.getX())).add((RealFieldElement) fieldVector3DNormalize2.getX().multiply(fieldVector3DNormalize5.getX()))).add((RealFieldElement) fieldVector3DNormalize.getX().multiply(fieldVector3DNormalize4.getX()));
        realFieldElementArr[0][1] = (RealFieldElement) ((RealFieldElement) ((RealFieldElement) fieldVector3DNormalize3.getY().multiply(fieldVector3DNormalize6.getX())).add((RealFieldElement) fieldVector3DNormalize2.getY().multiply(fieldVector3DNormalize5.getX()))).add((RealFieldElement) fieldVector3DNormalize.getY().multiply(fieldVector3DNormalize4.getX()));
        realFieldElementArr[0][2] = (RealFieldElement) ((RealFieldElement) ((RealFieldElement) fieldVector3DNormalize3.getZ().multiply(fieldVector3DNormalize6.getX())).add((RealFieldElement) fieldVector3DNormalize2.getZ().multiply(fieldVector3DNormalize5.getX()))).add((RealFieldElement) fieldVector3DNormalize.getZ().multiply(fieldVector3DNormalize4.getX()));
        realFieldElementArr[1][0] = (RealFieldElement) ((RealFieldElement) ((RealFieldElement) fieldVector3DNormalize3.getX().multiply(fieldVector3DNormalize6.getY())).add((RealFieldElement) fieldVector3DNormalize2.getX().multiply(fieldVector3DNormalize5.getY()))).add((RealFieldElement) fieldVector3DNormalize.getX().multiply(fieldVector3DNormalize4.getY()));
        realFieldElementArr[1][1] = (RealFieldElement) ((RealFieldElement) ((RealFieldElement) fieldVector3DNormalize3.getY().multiply(fieldVector3DNormalize6.getY())).add((RealFieldElement) fieldVector3DNormalize2.getY().multiply(fieldVector3DNormalize5.getY()))).add((RealFieldElement) fieldVector3DNormalize.getY().multiply(fieldVector3DNormalize4.getY()));
        realFieldElementArr[1][2] = (RealFieldElement) ((RealFieldElement) ((RealFieldElement) fieldVector3DNormalize3.getZ().multiply(fieldVector3DNormalize6.getY())).add((RealFieldElement) fieldVector3DNormalize2.getZ().multiply(fieldVector3DNormalize5.getY()))).add((RealFieldElement) fieldVector3DNormalize.getZ().multiply(fieldVector3DNormalize4.getY()));
        realFieldElementArr[2][0] = (RealFieldElement) ((RealFieldElement) ((RealFieldElement) fieldVector3DNormalize3.getX().multiply(fieldVector3DNormalize6.getZ())).add((RealFieldElement) fieldVector3DNormalize2.getX().multiply(fieldVector3DNormalize5.getZ()))).add((RealFieldElement) fieldVector3DNormalize.getX().multiply(fieldVector3DNormalize4.getZ()));
        realFieldElementArr[2][1] = (RealFieldElement) ((RealFieldElement) ((RealFieldElement) fieldVector3DNormalize3.getY().multiply(fieldVector3DNormalize6.getZ())).add((RealFieldElement) fieldVector3DNormalize2.getY().multiply(fieldVector3DNormalize5.getZ()))).add((RealFieldElement) fieldVector3DNormalize.getY().multiply(fieldVector3DNormalize4.getZ()));
        realFieldElementArr[2][2] = (RealFieldElement) ((RealFieldElement) ((RealFieldElement) fieldVector3DNormalize3.getZ().multiply(fieldVector3DNormalize6.getZ())).add((RealFieldElement) fieldVector3DNormalize2.getZ().multiply(fieldVector3DNormalize5.getZ()))).add((RealFieldElement) fieldVector3DNormalize.getZ().multiply(fieldVector3DNormalize4.getZ()));
        RealFieldElement[] realFieldElementArrMat2quat = mat2quat(realFieldElementArr);
        this.q0 = (T) realFieldElementArrMat2quat[0];
        this.q1 = (T) realFieldElementArrMat2quat[1];
        this.q2 = (T) realFieldElementArrMat2quat[2];
        this.q3 = (T) realFieldElementArrMat2quat[3];
    }

    public FieldRotation(FieldVector3D<T> u2, FieldVector3D<T> v2) throws MathArithmeticException {
        RealFieldElement realFieldElement = (RealFieldElement) u2.getNorm().multiply(v2.getNorm());
        if (realFieldElement.getReal() == 0.0d) {
            throw new MathArithmeticException(LocalizedFormats.ZERO_NORM_FOR_ROTATION_DEFINING_VECTOR, new Object[0]);
        }
        RealFieldElement realFieldElementDotProduct = FieldVector3D.dotProduct(u2, v2);
        if (realFieldElementDotProduct.getReal() < (-0.999999999999998d) * realFieldElement.getReal()) {
            FieldVector3D<T> w2 = u2.orthogonal();
            this.q0 = (T) realFieldElement.getField2().getZero();
            this.q1 = (T) w2.getX().negate();
            this.q2 = (T) w2.getY().negate();
            this.q3 = (T) w2.getZ().negate();
            return;
        }
        this.q0 = (T) ((RealFieldElement) ((RealFieldElement) ((RealFieldElement) realFieldElementDotProduct.divide(realFieldElement)).add(1.0d)).multiply(0.5d)).sqrt();
        RealFieldElement realFieldElement2 = (RealFieldElement) ((RealFieldElement) ((RealFieldElement) this.q0.multiply(realFieldElement)).multiply(2.0d)).reciprocal();
        FieldVector3D<T> q2 = FieldVector3D.crossProduct(v2, u2);
        this.q1 = (T) realFieldElement2.multiply(q2.getX());
        this.q2 = (T) realFieldElement2.multiply(q2.getY());
        this.q3 = (T) realFieldElement2.multiply(q2.getZ());
    }

    @Deprecated
    public FieldRotation(RotationOrder order, T alpha1, T alpha2, T alpha3) {
        this(order, RotationConvention.VECTOR_OPERATOR, alpha1, alpha2, alpha3);
    }

    public FieldRotation(RotationOrder order, RotationConvention convention, T alpha1, T alpha2, T alpha3) {
        RealFieldElement realFieldElement = (RealFieldElement) alpha1.getField2().getOne();
        FieldRotation<T> r1 = new FieldRotation<>(new FieldVector3D(realFieldElement, order.getA1()), alpha1, convention);
        FieldRotation<T> r2 = new FieldRotation<>(new FieldVector3D(realFieldElement, order.getA2()), alpha2, convention);
        FieldRotation<T> r3 = new FieldRotation<>(new FieldVector3D(realFieldElement, order.getA3()), alpha3, convention);
        FieldRotation<T> composed = r1.compose(r2.compose(r3, convention), convention);
        this.q0 = composed.q0;
        this.q1 = composed.q1;
        this.q2 = composed.q2;
        this.q3 = composed.q3;
    }

    /* JADX WARN: Multi-variable type inference failed */
    private T[] mat2quat(T[][] tArr) {
        T[] tArr2 = (T[]) ((RealFieldElement[]) MathArrays.buildArray(tArr[0][0].getField2(), 4));
        RealFieldElement realFieldElement = (RealFieldElement) ((RealFieldElement) tArr[0][0].add(tArr[1][1])).add(tArr[2][2]);
        if (realFieldElement.getReal() > -0.19d) {
            tArr2[0] = (RealFieldElement) ((RealFieldElement) ((RealFieldElement) realFieldElement.add(1.0d)).sqrt()).multiply(0.5d);
            RealFieldElement realFieldElement2 = (RealFieldElement) ((RealFieldElement) tArr2[0].reciprocal()).multiply(0.25d);
            tArr2[1] = (RealFieldElement) realFieldElement2.multiply((RealFieldElement) tArr[1][2].subtract(tArr[2][1]));
            tArr2[2] = (RealFieldElement) realFieldElement2.multiply((RealFieldElement) tArr[2][0].subtract(tArr[0][2]));
            tArr2[3] = (RealFieldElement) realFieldElement2.multiply((RealFieldElement) tArr[0][1].subtract(tArr[1][0]));
        } else {
            RealFieldElement realFieldElement3 = (RealFieldElement) ((RealFieldElement) tArr[0][0].subtract(tArr[1][1])).subtract(tArr[2][2]);
            if (realFieldElement3.getReal() > -0.19d) {
                tArr2[1] = (RealFieldElement) ((RealFieldElement) ((RealFieldElement) realFieldElement3.add(1.0d)).sqrt()).multiply(0.5d);
                RealFieldElement realFieldElement4 = (RealFieldElement) ((RealFieldElement) tArr2[1].reciprocal()).multiply(0.25d);
                tArr2[0] = (RealFieldElement) realFieldElement4.multiply((RealFieldElement) tArr[1][2].subtract(tArr[2][1]));
                tArr2[2] = (RealFieldElement) realFieldElement4.multiply((RealFieldElement) tArr[0][1].add(tArr[1][0]));
                tArr2[3] = (RealFieldElement) realFieldElement4.multiply((RealFieldElement) tArr[0][2].add(tArr[2][0]));
            } else {
                RealFieldElement realFieldElement5 = (RealFieldElement) ((RealFieldElement) tArr[1][1].subtract(tArr[0][0])).subtract(tArr[2][2]);
                if (realFieldElement5.getReal() > -0.19d) {
                    tArr2[2] = (RealFieldElement) ((RealFieldElement) ((RealFieldElement) realFieldElement5.add(1.0d)).sqrt()).multiply(0.5d);
                    RealFieldElement realFieldElement6 = (RealFieldElement) ((RealFieldElement) tArr2[2].reciprocal()).multiply(0.25d);
                    tArr2[0] = (RealFieldElement) realFieldElement6.multiply((RealFieldElement) tArr[2][0].subtract(tArr[0][2]));
                    tArr2[1] = (RealFieldElement) realFieldElement6.multiply((RealFieldElement) tArr[0][1].add(tArr[1][0]));
                    tArr2[3] = (RealFieldElement) realFieldElement6.multiply((RealFieldElement) tArr[2][1].add(tArr[1][2]));
                } else {
                    tArr2[3] = (RealFieldElement) ((RealFieldElement) ((RealFieldElement) ((RealFieldElement) ((RealFieldElement) tArr[2][2].subtract(tArr[0][0])).subtract(tArr[1][1])).add(1.0d)).sqrt()).multiply(0.5d);
                    RealFieldElement realFieldElement7 = (RealFieldElement) ((RealFieldElement) tArr2[3].reciprocal()).multiply(0.25d);
                    tArr2[0] = (RealFieldElement) realFieldElement7.multiply((RealFieldElement) tArr[0][1].subtract(tArr[1][0]));
                    tArr2[1] = (RealFieldElement) realFieldElement7.multiply((RealFieldElement) tArr[0][2].add(tArr[2][0]));
                    tArr2[2] = (RealFieldElement) realFieldElement7.multiply((RealFieldElement) tArr[2][1].add(tArr[1][2]));
                }
            }
        }
        return tArr2;
    }

    public FieldRotation<T> revert() {
        return new FieldRotation<>((RealFieldElement) this.q0.negate(), (RealFieldElement) this.q1, (RealFieldElement) this.q2, (RealFieldElement) this.q3, false);
    }

    public T getQ0() {
        return this.q0;
    }

    public T getQ1() {
        return this.q1;
    }

    public T getQ2() {
        return this.q2;
    }

    public T getQ3() {
        return this.q3;
    }

    @Deprecated
    public FieldVector3D<T> getAxis() {
        return getAxis(RotationConvention.VECTOR_OPERATOR);
    }

    public FieldVector3D<T> getAxis(RotationConvention convention) {
        RealFieldElement realFieldElement = (RealFieldElement) ((RealFieldElement) ((RealFieldElement) this.q1.multiply(this.q1)).add((RealFieldElement) this.q2.multiply(this.q2))).add((RealFieldElement) this.q3.multiply(this.q3));
        if (realFieldElement.getReal() == 0.0d) {
            Field<T> field = realFieldElement.getField2();
            return new FieldVector3D<>(convention == RotationConvention.VECTOR_OPERATOR ? (RealFieldElement) field.getOne() : (RealFieldElement) ((RealFieldElement) field.getOne()).negate(), (RealFieldElement) field.getZero(), (RealFieldElement) field.getZero());
        }
        double sgn = convention == RotationConvention.VECTOR_OPERATOR ? 1.0d : -1.0d;
        if (this.q0.getReal() < 0.0d) {
            RealFieldElement realFieldElement2 = (RealFieldElement) ((RealFieldElement) ((RealFieldElement) realFieldElement.sqrt()).reciprocal()).multiply(sgn);
            return new FieldVector3D<>((RealFieldElement) this.q1.multiply(realFieldElement2), (RealFieldElement) this.q2.multiply(realFieldElement2), (RealFieldElement) this.q3.multiply(realFieldElement2));
        }
        RealFieldElement realFieldElement3 = (RealFieldElement) ((RealFieldElement) ((RealFieldElement) ((RealFieldElement) realFieldElement.sqrt()).reciprocal()).negate()).multiply(sgn);
        return new FieldVector3D<>((RealFieldElement) this.q1.multiply(realFieldElement3), (RealFieldElement) this.q2.multiply(realFieldElement3), (RealFieldElement) this.q3.multiply(realFieldElement3));
    }

    public T getAngle() {
        if (this.q0.getReal() < -0.1d || this.q0.getReal() > 0.1d) {
            return (T) ((RealFieldElement) ((RealFieldElement) ((RealFieldElement) ((RealFieldElement) ((RealFieldElement) this.q1.multiply(this.q1)).add((RealFieldElement) this.q2.multiply(this.q2))).add((RealFieldElement) this.q3.multiply(this.q3))).sqrt()).asin()).multiply(2);
        }
        if (this.q0.getReal() < 0.0d) {
            return (T) ((RealFieldElement) ((RealFieldElement) this.q0.negate()).acos()).multiply(2);
        }
        return (T) ((RealFieldElement) this.q0.acos()).multiply(2);
    }

    @Deprecated
    public T[] getAngles(RotationOrder rotationOrder) throws CardanEulerSingularityException {
        return (T[]) getAngles(rotationOrder, RotationConvention.VECTOR_OPERATOR);
    }

    public T[] getAngles(RotationOrder rotationOrder, RotationConvention rotationConvention) throws CardanEulerSingularityException {
        if (rotationConvention == RotationConvention.VECTOR_OPERATOR) {
            if (rotationOrder == RotationOrder.XYZ) {
                FieldVector3D<T> fieldVector3DApplyTo = applyTo(vector(0.0d, 0.0d, 1.0d));
                FieldVector3D<T> fieldVector3DApplyInverseTo = applyInverseTo(vector(1.0d, 0.0d, 0.0d));
                if (fieldVector3DApplyInverseTo.getZ().getReal() < -0.9999999999d || fieldVector3DApplyInverseTo.getZ().getReal() > 0.9999999999d) {
                    throw new CardanEulerSingularityException(true);
                }
                return (T[]) buildArray((RealFieldElement) ((RealFieldElement) fieldVector3DApplyTo.getY().negate()).atan2(fieldVector3DApplyTo.getZ()), (RealFieldElement) fieldVector3DApplyInverseTo.getZ().asin(), (RealFieldElement) ((RealFieldElement) fieldVector3DApplyInverseTo.getY().negate()).atan2(fieldVector3DApplyInverseTo.getX()));
            }
            if (rotationOrder == RotationOrder.XZY) {
                FieldVector3D<T> fieldVector3DApplyTo2 = applyTo(vector(0.0d, 1.0d, 0.0d));
                FieldVector3D<T> fieldVector3DApplyInverseTo2 = applyInverseTo(vector(1.0d, 0.0d, 0.0d));
                if (fieldVector3DApplyInverseTo2.getY().getReal() < -0.9999999999d || fieldVector3DApplyInverseTo2.getY().getReal() > 0.9999999999d) {
                    throw new CardanEulerSingularityException(true);
                }
                return (T[]) buildArray((RealFieldElement) fieldVector3DApplyTo2.getZ().atan2(fieldVector3DApplyTo2.getY()), (RealFieldElement) ((RealFieldElement) fieldVector3DApplyInverseTo2.getY().asin()).negate(), (RealFieldElement) fieldVector3DApplyInverseTo2.getZ().atan2(fieldVector3DApplyInverseTo2.getX()));
            }
            if (rotationOrder == RotationOrder.YXZ) {
                FieldVector3D<T> fieldVector3DApplyTo3 = applyTo(vector(0.0d, 0.0d, 1.0d));
                FieldVector3D<T> fieldVector3DApplyInverseTo3 = applyInverseTo(vector(0.0d, 1.0d, 0.0d));
                if (fieldVector3DApplyInverseTo3.getZ().getReal() < -0.9999999999d || fieldVector3DApplyInverseTo3.getZ().getReal() > 0.9999999999d) {
                    throw new CardanEulerSingularityException(true);
                }
                return (T[]) buildArray((RealFieldElement) fieldVector3DApplyTo3.getX().atan2(fieldVector3DApplyTo3.getZ()), (RealFieldElement) ((RealFieldElement) fieldVector3DApplyInverseTo3.getZ().asin()).negate(), (RealFieldElement) fieldVector3DApplyInverseTo3.getX().atan2(fieldVector3DApplyInverseTo3.getY()));
            }
            if (rotationOrder == RotationOrder.YZX) {
                FieldVector3D<T> fieldVector3DApplyTo4 = applyTo(vector(1.0d, 0.0d, 0.0d));
                FieldVector3D<T> fieldVector3DApplyInverseTo4 = applyInverseTo(vector(0.0d, 1.0d, 0.0d));
                if (fieldVector3DApplyInverseTo4.getX().getReal() < -0.9999999999d || fieldVector3DApplyInverseTo4.getX().getReal() > 0.9999999999d) {
                    throw new CardanEulerSingularityException(true);
                }
                return (T[]) buildArray((RealFieldElement) ((RealFieldElement) fieldVector3DApplyTo4.getZ().negate()).atan2(fieldVector3DApplyTo4.getX()), (RealFieldElement) fieldVector3DApplyInverseTo4.getX().asin(), (RealFieldElement) ((RealFieldElement) fieldVector3DApplyInverseTo4.getZ().negate()).atan2(fieldVector3DApplyInverseTo4.getY()));
            }
            if (rotationOrder == RotationOrder.ZXY) {
                FieldVector3D<T> fieldVector3DApplyTo5 = applyTo(vector(0.0d, 1.0d, 0.0d));
                FieldVector3D<T> fieldVector3DApplyInverseTo5 = applyInverseTo(vector(0.0d, 0.0d, 1.0d));
                if (fieldVector3DApplyInverseTo5.getY().getReal() < -0.9999999999d || fieldVector3DApplyInverseTo5.getY().getReal() > 0.9999999999d) {
                    throw new CardanEulerSingularityException(true);
                }
                return (T[]) buildArray((RealFieldElement) ((RealFieldElement) fieldVector3DApplyTo5.getX().negate()).atan2(fieldVector3DApplyTo5.getY()), (RealFieldElement) fieldVector3DApplyInverseTo5.getY().asin(), (RealFieldElement) ((RealFieldElement) fieldVector3DApplyInverseTo5.getX().negate()).atan2(fieldVector3DApplyInverseTo5.getZ()));
            }
            if (rotationOrder == RotationOrder.ZYX) {
                FieldVector3D<T> fieldVector3DApplyTo6 = applyTo(vector(1.0d, 0.0d, 0.0d));
                FieldVector3D<T> fieldVector3DApplyInverseTo6 = applyInverseTo(vector(0.0d, 0.0d, 1.0d));
                if (fieldVector3DApplyInverseTo6.getX().getReal() < -0.9999999999d || fieldVector3DApplyInverseTo6.getX().getReal() > 0.9999999999d) {
                    throw new CardanEulerSingularityException(true);
                }
                return (T[]) buildArray((RealFieldElement) fieldVector3DApplyTo6.getY().atan2(fieldVector3DApplyTo6.getX()), (RealFieldElement) ((RealFieldElement) fieldVector3DApplyInverseTo6.getX().asin()).negate(), (RealFieldElement) fieldVector3DApplyInverseTo6.getY().atan2(fieldVector3DApplyInverseTo6.getZ()));
            }
            if (rotationOrder == RotationOrder.XYX) {
                FieldVector3D<T> fieldVector3DApplyTo7 = applyTo(vector(1.0d, 0.0d, 0.0d));
                FieldVector3D<T> fieldVector3DApplyInverseTo7 = applyInverseTo(vector(1.0d, 0.0d, 0.0d));
                if (fieldVector3DApplyInverseTo7.getX().getReal() < -0.9999999999d || fieldVector3DApplyInverseTo7.getX().getReal() > 0.9999999999d) {
                    throw new CardanEulerSingularityException(false);
                }
                return (T[]) buildArray((RealFieldElement) fieldVector3DApplyTo7.getY().atan2(fieldVector3DApplyTo7.getZ().negate()), (RealFieldElement) fieldVector3DApplyInverseTo7.getX().acos(), (RealFieldElement) fieldVector3DApplyInverseTo7.getY().atan2(fieldVector3DApplyInverseTo7.getZ()));
            }
            if (rotationOrder == RotationOrder.XZX) {
                FieldVector3D<T> fieldVector3DApplyTo8 = applyTo(vector(1.0d, 0.0d, 0.0d));
                FieldVector3D<T> fieldVector3DApplyInverseTo8 = applyInverseTo(vector(1.0d, 0.0d, 0.0d));
                if (fieldVector3DApplyInverseTo8.getX().getReal() < -0.9999999999d || fieldVector3DApplyInverseTo8.getX().getReal() > 0.9999999999d) {
                    throw new CardanEulerSingularityException(false);
                }
                return (T[]) buildArray((RealFieldElement) fieldVector3DApplyTo8.getZ().atan2(fieldVector3DApplyTo8.getY()), (RealFieldElement) fieldVector3DApplyInverseTo8.getX().acos(), (RealFieldElement) fieldVector3DApplyInverseTo8.getZ().atan2(fieldVector3DApplyInverseTo8.getY().negate()));
            }
            if (rotationOrder == RotationOrder.YXY) {
                FieldVector3D<T> fieldVector3DApplyTo9 = applyTo(vector(0.0d, 1.0d, 0.0d));
                FieldVector3D<T> fieldVector3DApplyInverseTo9 = applyInverseTo(vector(0.0d, 1.0d, 0.0d));
                if (fieldVector3DApplyInverseTo9.getY().getReal() < -0.9999999999d || fieldVector3DApplyInverseTo9.getY().getReal() > 0.9999999999d) {
                    throw new CardanEulerSingularityException(false);
                }
                return (T[]) buildArray((RealFieldElement) fieldVector3DApplyTo9.getX().atan2(fieldVector3DApplyTo9.getZ()), (RealFieldElement) fieldVector3DApplyInverseTo9.getY().acos(), (RealFieldElement) fieldVector3DApplyInverseTo9.getX().atan2(fieldVector3DApplyInverseTo9.getZ().negate()));
            }
            if (rotationOrder == RotationOrder.YZY) {
                FieldVector3D<T> fieldVector3DApplyTo10 = applyTo(vector(0.0d, 1.0d, 0.0d));
                FieldVector3D<T> fieldVector3DApplyInverseTo10 = applyInverseTo(vector(0.0d, 1.0d, 0.0d));
                if (fieldVector3DApplyInverseTo10.getY().getReal() < -0.9999999999d || fieldVector3DApplyInverseTo10.getY().getReal() > 0.9999999999d) {
                    throw new CardanEulerSingularityException(false);
                }
                return (T[]) buildArray((RealFieldElement) fieldVector3DApplyTo10.getZ().atan2(fieldVector3DApplyTo10.getX().negate()), (RealFieldElement) fieldVector3DApplyInverseTo10.getY().acos(), (RealFieldElement) fieldVector3DApplyInverseTo10.getZ().atan2(fieldVector3DApplyInverseTo10.getX()));
            }
            if (rotationOrder == RotationOrder.ZXZ) {
                FieldVector3D<T> fieldVector3DApplyTo11 = applyTo(vector(0.0d, 0.0d, 1.0d));
                FieldVector3D<T> fieldVector3DApplyInverseTo11 = applyInverseTo(vector(0.0d, 0.0d, 1.0d));
                if (fieldVector3DApplyInverseTo11.getZ().getReal() < -0.9999999999d || fieldVector3DApplyInverseTo11.getZ().getReal() > 0.9999999999d) {
                    throw new CardanEulerSingularityException(false);
                }
                return (T[]) buildArray((RealFieldElement) fieldVector3DApplyTo11.getX().atan2(fieldVector3DApplyTo11.getY().negate()), (RealFieldElement) fieldVector3DApplyInverseTo11.getZ().acos(), (RealFieldElement) fieldVector3DApplyInverseTo11.getX().atan2(fieldVector3DApplyInverseTo11.getY()));
            }
            FieldVector3D<T> fieldVector3DApplyTo12 = applyTo(vector(0.0d, 0.0d, 1.0d));
            FieldVector3D<T> fieldVector3DApplyInverseTo12 = applyInverseTo(vector(0.0d, 0.0d, 1.0d));
            if (fieldVector3DApplyInverseTo12.getZ().getReal() < -0.9999999999d || fieldVector3DApplyInverseTo12.getZ().getReal() > 0.9999999999d) {
                throw new CardanEulerSingularityException(false);
            }
            return (T[]) buildArray((RealFieldElement) fieldVector3DApplyTo12.getY().atan2(fieldVector3DApplyTo12.getX()), (RealFieldElement) fieldVector3DApplyInverseTo12.getZ().acos(), (RealFieldElement) fieldVector3DApplyInverseTo12.getY().atan2(fieldVector3DApplyInverseTo12.getX().negate()));
        }
        if (rotationOrder == RotationOrder.XYZ) {
            FieldVector3D<T> fieldVector3DApplyTo13 = applyTo(Vector3D.PLUS_I);
            FieldVector3D<T> fieldVector3DApplyInverseTo13 = applyInverseTo(Vector3D.PLUS_K);
            if (fieldVector3DApplyInverseTo13.getX().getReal() < -0.9999999999d || fieldVector3DApplyInverseTo13.getX().getReal() > 0.9999999999d) {
                throw new CardanEulerSingularityException(true);
            }
            return (T[]) buildArray((RealFieldElement) ((RealFieldElement) fieldVector3DApplyInverseTo13.getY().negate()).atan2(fieldVector3DApplyInverseTo13.getZ()), (RealFieldElement) fieldVector3DApplyInverseTo13.getX().asin(), (RealFieldElement) ((RealFieldElement) fieldVector3DApplyTo13.getY().negate()).atan2(fieldVector3DApplyTo13.getX()));
        }
        if (rotationOrder == RotationOrder.XZY) {
            FieldVector3D<T> fieldVector3DApplyTo14 = applyTo(Vector3D.PLUS_I);
            FieldVector3D<T> fieldVector3DApplyInverseTo14 = applyInverseTo(Vector3D.PLUS_J);
            if (fieldVector3DApplyInverseTo14.getX().getReal() < -0.9999999999d || fieldVector3DApplyInverseTo14.getX().getReal() > 0.9999999999d) {
                throw new CardanEulerSingularityException(true);
            }
            return (T[]) buildArray((RealFieldElement) fieldVector3DApplyInverseTo14.getZ().atan2(fieldVector3DApplyInverseTo14.getY()), (RealFieldElement) ((RealFieldElement) fieldVector3DApplyInverseTo14.getX().asin()).negate(), (RealFieldElement) fieldVector3DApplyTo14.getZ().atan2(fieldVector3DApplyTo14.getX()));
        }
        if (rotationOrder == RotationOrder.YXZ) {
            FieldVector3D<T> fieldVector3DApplyTo15 = applyTo(Vector3D.PLUS_J);
            FieldVector3D<T> fieldVector3DApplyInverseTo15 = applyInverseTo(Vector3D.PLUS_K);
            if (fieldVector3DApplyInverseTo15.getY().getReal() < -0.9999999999d || fieldVector3DApplyInverseTo15.getY().getReal() > 0.9999999999d) {
                throw new CardanEulerSingularityException(true);
            }
            return (T[]) buildArray((RealFieldElement) fieldVector3DApplyInverseTo15.getX().atan2(fieldVector3DApplyInverseTo15.getZ()), (RealFieldElement) ((RealFieldElement) fieldVector3DApplyInverseTo15.getY().asin()).negate(), (RealFieldElement) fieldVector3DApplyTo15.getX().atan2(fieldVector3DApplyTo15.getY()));
        }
        if (rotationOrder == RotationOrder.YZX) {
            FieldVector3D<T> fieldVector3DApplyTo16 = applyTo(Vector3D.PLUS_J);
            FieldVector3D<T> fieldVector3DApplyInverseTo16 = applyInverseTo(Vector3D.PLUS_I);
            if (fieldVector3DApplyInverseTo16.getY().getReal() < -0.9999999999d || fieldVector3DApplyInverseTo16.getY().getReal() > 0.9999999999d) {
                throw new CardanEulerSingularityException(true);
            }
            return (T[]) buildArray((RealFieldElement) ((RealFieldElement) fieldVector3DApplyInverseTo16.getZ().negate()).atan2(fieldVector3DApplyInverseTo16.getX()), (RealFieldElement) fieldVector3DApplyInverseTo16.getY().asin(), (RealFieldElement) ((RealFieldElement) fieldVector3DApplyTo16.getZ().negate()).atan2(fieldVector3DApplyTo16.getY()));
        }
        if (rotationOrder == RotationOrder.ZXY) {
            FieldVector3D<T> fieldVector3DApplyTo17 = applyTo(Vector3D.PLUS_K);
            FieldVector3D<T> fieldVector3DApplyInverseTo17 = applyInverseTo(Vector3D.PLUS_J);
            if (fieldVector3DApplyInverseTo17.getZ().getReal() < -0.9999999999d || fieldVector3DApplyInverseTo17.getZ().getReal() > 0.9999999999d) {
                throw new CardanEulerSingularityException(true);
            }
            return (T[]) buildArray((RealFieldElement) ((RealFieldElement) fieldVector3DApplyInverseTo17.getX().negate()).atan2(fieldVector3DApplyInverseTo17.getY()), (RealFieldElement) fieldVector3DApplyInverseTo17.getZ().asin(), (RealFieldElement) ((RealFieldElement) fieldVector3DApplyTo17.getX().negate()).atan2(fieldVector3DApplyTo17.getZ()));
        }
        if (rotationOrder == RotationOrder.ZYX) {
            FieldVector3D<T> fieldVector3DApplyTo18 = applyTo(Vector3D.PLUS_K);
            FieldVector3D<T> fieldVector3DApplyInverseTo18 = applyInverseTo(Vector3D.PLUS_I);
            if (fieldVector3DApplyInverseTo18.getZ().getReal() < -0.9999999999d || fieldVector3DApplyInverseTo18.getZ().getReal() > 0.9999999999d) {
                throw new CardanEulerSingularityException(true);
            }
            return (T[]) buildArray((RealFieldElement) fieldVector3DApplyInverseTo18.getY().atan2(fieldVector3DApplyInverseTo18.getX()), (RealFieldElement) ((RealFieldElement) fieldVector3DApplyInverseTo18.getZ().asin()).negate(), (RealFieldElement) fieldVector3DApplyTo18.getY().atan2(fieldVector3DApplyTo18.getZ()));
        }
        if (rotationOrder == RotationOrder.XYX) {
            FieldVector3D<T> fieldVector3DApplyTo19 = applyTo(Vector3D.PLUS_I);
            FieldVector3D<T> fieldVector3DApplyInverseTo19 = applyInverseTo(Vector3D.PLUS_I);
            if (fieldVector3DApplyInverseTo19.getX().getReal() < -0.9999999999d || fieldVector3DApplyInverseTo19.getX().getReal() > 0.9999999999d) {
                throw new CardanEulerSingularityException(false);
            }
            return (T[]) buildArray((RealFieldElement) fieldVector3DApplyInverseTo19.getY().atan2(fieldVector3DApplyInverseTo19.getZ().negate()), (RealFieldElement) fieldVector3DApplyInverseTo19.getX().acos(), (RealFieldElement) fieldVector3DApplyTo19.getY().atan2(fieldVector3DApplyTo19.getZ()));
        }
        if (rotationOrder == RotationOrder.XZX) {
            FieldVector3D<T> fieldVector3DApplyTo20 = applyTo(Vector3D.PLUS_I);
            FieldVector3D<T> fieldVector3DApplyInverseTo20 = applyInverseTo(Vector3D.PLUS_I);
            if (fieldVector3DApplyInverseTo20.getX().getReal() < -0.9999999999d || fieldVector3DApplyInverseTo20.getX().getReal() > 0.9999999999d) {
                throw new CardanEulerSingularityException(false);
            }
            return (T[]) buildArray((RealFieldElement) fieldVector3DApplyInverseTo20.getZ().atan2(fieldVector3DApplyInverseTo20.getY()), (RealFieldElement) fieldVector3DApplyInverseTo20.getX().acos(), (RealFieldElement) fieldVector3DApplyTo20.getZ().atan2(fieldVector3DApplyTo20.getY().negate()));
        }
        if (rotationOrder == RotationOrder.YXY) {
            FieldVector3D<T> fieldVector3DApplyTo21 = applyTo(Vector3D.PLUS_J);
            FieldVector3D<T> fieldVector3DApplyInverseTo21 = applyInverseTo(Vector3D.PLUS_J);
            if (fieldVector3DApplyInverseTo21.getY().getReal() < -0.9999999999d || fieldVector3DApplyInverseTo21.getY().getReal() > 0.9999999999d) {
                throw new CardanEulerSingularityException(false);
            }
            return (T[]) buildArray((RealFieldElement) fieldVector3DApplyInverseTo21.getX().atan2(fieldVector3DApplyInverseTo21.getZ()), (RealFieldElement) fieldVector3DApplyInverseTo21.getY().acos(), (RealFieldElement) fieldVector3DApplyTo21.getX().atan2(fieldVector3DApplyTo21.getZ().negate()));
        }
        if (rotationOrder == RotationOrder.YZY) {
            FieldVector3D<T> fieldVector3DApplyTo22 = applyTo(Vector3D.PLUS_J);
            FieldVector3D<T> fieldVector3DApplyInverseTo22 = applyInverseTo(Vector3D.PLUS_J);
            if (fieldVector3DApplyInverseTo22.getY().getReal() < -0.9999999999d || fieldVector3DApplyInverseTo22.getY().getReal() > 0.9999999999d) {
                throw new CardanEulerSingularityException(false);
            }
            return (T[]) buildArray((RealFieldElement) fieldVector3DApplyInverseTo22.getZ().atan2(fieldVector3DApplyInverseTo22.getX().negate()), (RealFieldElement) fieldVector3DApplyInverseTo22.getY().acos(), (RealFieldElement) fieldVector3DApplyTo22.getZ().atan2(fieldVector3DApplyTo22.getX()));
        }
        if (rotationOrder == RotationOrder.ZXZ) {
            FieldVector3D<T> fieldVector3DApplyTo23 = applyTo(Vector3D.PLUS_K);
            FieldVector3D<T> fieldVector3DApplyInverseTo23 = applyInverseTo(Vector3D.PLUS_K);
            if (fieldVector3DApplyInverseTo23.getZ().getReal() < -0.9999999999d || fieldVector3DApplyInverseTo23.getZ().getReal() > 0.9999999999d) {
                throw new CardanEulerSingularityException(false);
            }
            return (T[]) buildArray((RealFieldElement) fieldVector3DApplyInverseTo23.getX().atan2(fieldVector3DApplyInverseTo23.getY().negate()), (RealFieldElement) fieldVector3DApplyInverseTo23.getZ().acos(), (RealFieldElement) fieldVector3DApplyTo23.getX().atan2(fieldVector3DApplyTo23.getY()));
        }
        FieldVector3D<T> fieldVector3DApplyTo24 = applyTo(Vector3D.PLUS_K);
        FieldVector3D<T> fieldVector3DApplyInverseTo24 = applyInverseTo(Vector3D.PLUS_K);
        if (fieldVector3DApplyInverseTo24.getZ().getReal() < -0.9999999999d || fieldVector3DApplyInverseTo24.getZ().getReal() > 0.9999999999d) {
            throw new CardanEulerSingularityException(false);
        }
        return (T[]) buildArray((RealFieldElement) fieldVector3DApplyInverseTo24.getY().atan2(fieldVector3DApplyInverseTo24.getX()), (RealFieldElement) fieldVector3DApplyInverseTo24.getZ().acos(), (RealFieldElement) fieldVector3DApplyTo24.getY().atan2(fieldVector3DApplyTo24.getX().negate()));
    }

    private T[] buildArray(T t2, T t3, T t4) {
        T[] tArr = (T[]) ((RealFieldElement[]) MathArrays.buildArray(t2.getField2(), 3));
        tArr[0] = t2;
        tArr[1] = t3;
        tArr[2] = t4;
        return tArr;
    }

    private FieldVector3D<T> vector(double x2, double y2, double z2) {
        RealFieldElement realFieldElement = (RealFieldElement) this.q0.getField2().getZero();
        return new FieldVector3D<>((RealFieldElement) realFieldElement.add(x2), (RealFieldElement) realFieldElement.add(y2), (RealFieldElement) realFieldElement.add(z2));
    }

    /* JADX WARN: Multi-variable type inference failed */
    public T[][] getMatrix() {
        RealFieldElement realFieldElement = (RealFieldElement) this.q0.multiply(this.q0);
        RealFieldElement realFieldElement2 = (RealFieldElement) this.q0.multiply(this.q1);
        RealFieldElement realFieldElement3 = (RealFieldElement) this.q0.multiply(this.q2);
        RealFieldElement realFieldElement4 = (RealFieldElement) this.q0.multiply(this.q3);
        RealFieldElement realFieldElement5 = (RealFieldElement) this.q1.multiply(this.q1);
        RealFieldElement realFieldElement6 = (RealFieldElement) this.q1.multiply(this.q2);
        RealFieldElement realFieldElement7 = (RealFieldElement) this.q1.multiply(this.q3);
        RealFieldElement realFieldElement8 = (RealFieldElement) this.q2.multiply(this.q2);
        RealFieldElement realFieldElement9 = (RealFieldElement) this.q2.multiply(this.q3);
        RealFieldElement realFieldElement10 = (RealFieldElement) this.q3.multiply(this.q3);
        T[][] tArr = (T[][]) ((RealFieldElement[][]) MathArrays.buildArray(this.q0.getField2(), 3, 3));
        tArr[0][0] = (RealFieldElement) ((RealFieldElement) ((RealFieldElement) realFieldElement.add(realFieldElement5)).multiply(2)).subtract(1.0d);
        tArr[1][0] = (RealFieldElement) ((RealFieldElement) realFieldElement6.subtract(realFieldElement4)).multiply(2);
        tArr[2][0] = (RealFieldElement) ((RealFieldElement) realFieldElement7.add(realFieldElement3)).multiply(2);
        tArr[0][1] = (RealFieldElement) ((RealFieldElement) realFieldElement6.add(realFieldElement4)).multiply(2);
        tArr[1][1] = (RealFieldElement) ((RealFieldElement) ((RealFieldElement) realFieldElement.add(realFieldElement8)).multiply(2)).subtract(1.0d);
        tArr[2][1] = (RealFieldElement) ((RealFieldElement) realFieldElement9.subtract(realFieldElement2)).multiply(2);
        tArr[0][2] = (RealFieldElement) ((RealFieldElement) realFieldElement7.subtract(realFieldElement3)).multiply(2);
        tArr[1][2] = (RealFieldElement) ((RealFieldElement) realFieldElement9.add(realFieldElement2)).multiply(2);
        tArr[2][2] = (RealFieldElement) ((RealFieldElement) ((RealFieldElement) realFieldElement.add(realFieldElement10)).multiply(2)).subtract(1.0d);
        return tArr;
    }

    public Rotation toRotation() {
        return new Rotation(this.q0.getReal(), this.q1.getReal(), this.q2.getReal(), this.q3.getReal(), false);
    }

    public FieldVector3D<T> applyTo(FieldVector3D<T> u2) {
        RealFieldElement x2 = u2.getX();
        RealFieldElement y2 = u2.getY();
        RealFieldElement z2 = u2.getZ();
        RealFieldElement realFieldElement = (RealFieldElement) ((RealFieldElement) ((RealFieldElement) this.q1.multiply(x2)).add((RealFieldElement) this.q2.multiply(y2))).add((RealFieldElement) this.q3.multiply(z2));
        return new FieldVector3D<>((RealFieldElement) ((RealFieldElement) ((RealFieldElement) ((RealFieldElement) this.q0.multiply(((RealFieldElement) x2.multiply(this.q0)).subtract((RealFieldElement) ((RealFieldElement) this.q2.multiply(z2)).subtract((RealFieldElement) this.q3.multiply(y2))))).add((RealFieldElement) realFieldElement.multiply(this.q1))).multiply(2)).subtract(x2), (RealFieldElement) ((RealFieldElement) ((RealFieldElement) ((RealFieldElement) this.q0.multiply(((RealFieldElement) y2.multiply(this.q0)).subtract((RealFieldElement) ((RealFieldElement) this.q3.multiply(x2)).subtract((RealFieldElement) this.q1.multiply(z2))))).add((RealFieldElement) realFieldElement.multiply(this.q2))).multiply(2)).subtract(y2), (RealFieldElement) ((RealFieldElement) ((RealFieldElement) ((RealFieldElement) this.q0.multiply(((RealFieldElement) z2.multiply(this.q0)).subtract((RealFieldElement) ((RealFieldElement) this.q1.multiply(y2)).subtract((RealFieldElement) this.q2.multiply(x2))))).add((RealFieldElement) realFieldElement.multiply(this.q3))).multiply(2)).subtract(z2));
    }

    public FieldVector3D<T> applyTo(Vector3D u2) {
        double x2 = u2.getX();
        double y2 = u2.getY();
        double z2 = u2.getZ();
        RealFieldElement realFieldElement = (RealFieldElement) ((RealFieldElement) ((RealFieldElement) this.q1.multiply(x2)).add((RealFieldElement) this.q2.multiply(y2))).add((RealFieldElement) this.q3.multiply(z2));
        return new FieldVector3D<>((RealFieldElement) ((RealFieldElement) ((RealFieldElement) ((RealFieldElement) this.q0.multiply(((RealFieldElement) this.q0.multiply(x2)).subtract((RealFieldElement) ((RealFieldElement) this.q2.multiply(z2)).subtract((RealFieldElement) this.q3.multiply(y2))))).add((RealFieldElement) realFieldElement.multiply(this.q1))).multiply(2)).subtract(x2), (RealFieldElement) ((RealFieldElement) ((RealFieldElement) ((RealFieldElement) this.q0.multiply(((RealFieldElement) this.q0.multiply(y2)).subtract((RealFieldElement) ((RealFieldElement) this.q3.multiply(x2)).subtract((RealFieldElement) this.q1.multiply(z2))))).add((RealFieldElement) realFieldElement.multiply(this.q2))).multiply(2)).subtract(y2), (RealFieldElement) ((RealFieldElement) ((RealFieldElement) ((RealFieldElement) this.q0.multiply(((RealFieldElement) this.q0.multiply(z2)).subtract((RealFieldElement) ((RealFieldElement) this.q1.multiply(y2)).subtract((RealFieldElement) this.q2.multiply(x2))))).add((RealFieldElement) realFieldElement.multiply(this.q3))).multiply(2)).subtract(z2));
    }

    public void applyTo(T[] in, T[] tArr) {
        T x2 = in[0];
        T y2 = in[1];
        T z2 = in[2];
        RealFieldElement realFieldElement = (RealFieldElement) ((RealFieldElement) ((RealFieldElement) this.q1.multiply(x2)).add((RealFieldElement) this.q2.multiply(y2))).add((RealFieldElement) this.q3.multiply(z2));
        tArr[0] = (RealFieldElement) ((RealFieldElement) ((RealFieldElement) ((RealFieldElement) this.q0.multiply(((RealFieldElement) x2.multiply(this.q0)).subtract((RealFieldElement) ((RealFieldElement) this.q2.multiply(z2)).subtract((RealFieldElement) this.q3.multiply(y2))))).add((RealFieldElement) realFieldElement.multiply(this.q1))).multiply(2)).subtract(x2);
        tArr[1] = (RealFieldElement) ((RealFieldElement) ((RealFieldElement) ((RealFieldElement) this.q0.multiply(((RealFieldElement) y2.multiply(this.q0)).subtract((RealFieldElement) ((RealFieldElement) this.q3.multiply(x2)).subtract((RealFieldElement) this.q1.multiply(z2))))).add((RealFieldElement) realFieldElement.multiply(this.q2))).multiply(2)).subtract(y2);
        tArr[2] = (RealFieldElement) ((RealFieldElement) ((RealFieldElement) ((RealFieldElement) this.q0.multiply(((RealFieldElement) z2.multiply(this.q0)).subtract((RealFieldElement) ((RealFieldElement) this.q1.multiply(y2)).subtract((RealFieldElement) this.q2.multiply(x2))))).add((RealFieldElement) realFieldElement.multiply(this.q3))).multiply(2)).subtract(z2);
    }

    public void applyTo(double[] in, T[] tArr) {
        double x2 = in[0];
        double y2 = in[1];
        double z2 = in[2];
        RealFieldElement realFieldElement = (RealFieldElement) ((RealFieldElement) ((RealFieldElement) this.q1.multiply(x2)).add((RealFieldElement) this.q2.multiply(y2))).add((RealFieldElement) this.q3.multiply(z2));
        tArr[0] = (RealFieldElement) ((RealFieldElement) ((RealFieldElement) ((RealFieldElement) this.q0.multiply(((RealFieldElement) this.q0.multiply(x2)).subtract((RealFieldElement) ((RealFieldElement) this.q2.multiply(z2)).subtract((RealFieldElement) this.q3.multiply(y2))))).add((RealFieldElement) realFieldElement.multiply(this.q1))).multiply(2)).subtract(x2);
        tArr[1] = (RealFieldElement) ((RealFieldElement) ((RealFieldElement) ((RealFieldElement) this.q0.multiply(((RealFieldElement) this.q0.multiply(y2)).subtract((RealFieldElement) ((RealFieldElement) this.q3.multiply(x2)).subtract((RealFieldElement) this.q1.multiply(z2))))).add((RealFieldElement) realFieldElement.multiply(this.q2))).multiply(2)).subtract(y2);
        tArr[2] = (RealFieldElement) ((RealFieldElement) ((RealFieldElement) ((RealFieldElement) this.q0.multiply(((RealFieldElement) this.q0.multiply(z2)).subtract((RealFieldElement) ((RealFieldElement) this.q1.multiply(y2)).subtract((RealFieldElement) this.q2.multiply(x2))))).add((RealFieldElement) realFieldElement.multiply(this.q3))).multiply(2)).subtract(z2);
    }

    public static <T extends RealFieldElement<T>> FieldVector3D<T> applyTo(Rotation r2, FieldVector3D<T> u2) {
        RealFieldElement x2 = u2.getX();
        RealFieldElement y2 = u2.getY();
        RealFieldElement z2 = u2.getZ();
        RealFieldElement realFieldElement = (RealFieldElement) ((RealFieldElement) ((RealFieldElement) x2.multiply(r2.getQ1())).add((RealFieldElement) y2.multiply(r2.getQ2()))).add((RealFieldElement) z2.multiply(r2.getQ3()));
        return new FieldVector3D<>((RealFieldElement) ((RealFieldElement) ((RealFieldElement) ((RealFieldElement) ((RealFieldElement) ((RealFieldElement) x2.multiply(r2.getQ0())).subtract((RealFieldElement) ((RealFieldElement) z2.multiply(r2.getQ2())).subtract((RealFieldElement) y2.multiply(r2.getQ3())))).multiply(r2.getQ0())).add((RealFieldElement) realFieldElement.multiply(r2.getQ1()))).multiply(2)).subtract(x2), (RealFieldElement) ((RealFieldElement) ((RealFieldElement) ((RealFieldElement) ((RealFieldElement) ((RealFieldElement) y2.multiply(r2.getQ0())).subtract((RealFieldElement) ((RealFieldElement) x2.multiply(r2.getQ3())).subtract((RealFieldElement) z2.multiply(r2.getQ1())))).multiply(r2.getQ0())).add((RealFieldElement) realFieldElement.multiply(r2.getQ2()))).multiply(2)).subtract(y2), (RealFieldElement) ((RealFieldElement) ((RealFieldElement) ((RealFieldElement) ((RealFieldElement) ((RealFieldElement) z2.multiply(r2.getQ0())).subtract((RealFieldElement) ((RealFieldElement) y2.multiply(r2.getQ1())).subtract((RealFieldElement) x2.multiply(r2.getQ2())))).multiply(r2.getQ0())).add((RealFieldElement) realFieldElement.multiply(r2.getQ3()))).multiply(2)).subtract(z2));
    }

    public FieldVector3D<T> applyInverseTo(FieldVector3D<T> u2) {
        RealFieldElement x2 = u2.getX();
        RealFieldElement y2 = u2.getY();
        RealFieldElement z2 = u2.getZ();
        RealFieldElement realFieldElement = (RealFieldElement) ((RealFieldElement) ((RealFieldElement) this.q1.multiply(x2)).add((RealFieldElement) this.q2.multiply(y2))).add((RealFieldElement) this.q3.multiply(z2));
        RealFieldElement realFieldElement2 = (RealFieldElement) this.q0.negate();
        return new FieldVector3D<>((RealFieldElement) ((RealFieldElement) ((RealFieldElement) ((RealFieldElement) realFieldElement2.multiply((RealFieldElement) ((RealFieldElement) x2.multiply(realFieldElement2)).subtract((RealFieldElement) ((RealFieldElement) this.q2.multiply(z2)).subtract((RealFieldElement) this.q3.multiply(y2))))).add((RealFieldElement) realFieldElement.multiply(this.q1))).multiply(2)).subtract(x2), (RealFieldElement) ((RealFieldElement) ((RealFieldElement) ((RealFieldElement) realFieldElement2.multiply((RealFieldElement) ((RealFieldElement) y2.multiply(realFieldElement2)).subtract((RealFieldElement) ((RealFieldElement) this.q3.multiply(x2)).subtract((RealFieldElement) this.q1.multiply(z2))))).add((RealFieldElement) realFieldElement.multiply(this.q2))).multiply(2)).subtract(y2), (RealFieldElement) ((RealFieldElement) ((RealFieldElement) ((RealFieldElement) realFieldElement2.multiply((RealFieldElement) ((RealFieldElement) z2.multiply(realFieldElement2)).subtract((RealFieldElement) ((RealFieldElement) this.q1.multiply(y2)).subtract((RealFieldElement) this.q2.multiply(x2))))).add((RealFieldElement) realFieldElement.multiply(this.q3))).multiply(2)).subtract(z2));
    }

    public FieldVector3D<T> applyInverseTo(Vector3D u2) {
        double x2 = u2.getX();
        double y2 = u2.getY();
        double z2 = u2.getZ();
        RealFieldElement realFieldElement = (RealFieldElement) ((RealFieldElement) ((RealFieldElement) this.q1.multiply(x2)).add((RealFieldElement) this.q2.multiply(y2))).add((RealFieldElement) this.q3.multiply(z2));
        RealFieldElement realFieldElement2 = (RealFieldElement) this.q0.negate();
        return new FieldVector3D<>((RealFieldElement) ((RealFieldElement) ((RealFieldElement) ((RealFieldElement) realFieldElement2.multiply((RealFieldElement) ((RealFieldElement) realFieldElement2.multiply(x2)).subtract((RealFieldElement) ((RealFieldElement) this.q2.multiply(z2)).subtract((RealFieldElement) this.q3.multiply(y2))))).add((RealFieldElement) realFieldElement.multiply(this.q1))).multiply(2)).subtract(x2), (RealFieldElement) ((RealFieldElement) ((RealFieldElement) ((RealFieldElement) realFieldElement2.multiply((RealFieldElement) ((RealFieldElement) realFieldElement2.multiply(y2)).subtract((RealFieldElement) ((RealFieldElement) this.q3.multiply(x2)).subtract((RealFieldElement) this.q1.multiply(z2))))).add((RealFieldElement) realFieldElement.multiply(this.q2))).multiply(2)).subtract(y2), (RealFieldElement) ((RealFieldElement) ((RealFieldElement) ((RealFieldElement) realFieldElement2.multiply((RealFieldElement) ((RealFieldElement) realFieldElement2.multiply(z2)).subtract((RealFieldElement) ((RealFieldElement) this.q1.multiply(y2)).subtract((RealFieldElement) this.q2.multiply(x2))))).add((RealFieldElement) realFieldElement.multiply(this.q3))).multiply(2)).subtract(z2));
    }

    public void applyInverseTo(T[] in, T[] tArr) {
        T x2 = in[0];
        T y2 = in[1];
        T z2 = in[2];
        RealFieldElement realFieldElement = (RealFieldElement) ((RealFieldElement) ((RealFieldElement) this.q1.multiply(x2)).add((RealFieldElement) this.q2.multiply(y2))).add((RealFieldElement) this.q3.multiply(z2));
        RealFieldElement realFieldElement2 = (RealFieldElement) this.q0.negate();
        tArr[0] = (RealFieldElement) ((RealFieldElement) ((RealFieldElement) ((RealFieldElement) realFieldElement2.multiply((RealFieldElement) ((RealFieldElement) x2.multiply(realFieldElement2)).subtract((RealFieldElement) ((RealFieldElement) this.q2.multiply(z2)).subtract((RealFieldElement) this.q3.multiply(y2))))).add((RealFieldElement) realFieldElement.multiply(this.q1))).multiply(2)).subtract(x2);
        tArr[1] = (RealFieldElement) ((RealFieldElement) ((RealFieldElement) ((RealFieldElement) realFieldElement2.multiply((RealFieldElement) ((RealFieldElement) y2.multiply(realFieldElement2)).subtract((RealFieldElement) ((RealFieldElement) this.q3.multiply(x2)).subtract((RealFieldElement) this.q1.multiply(z2))))).add((RealFieldElement) realFieldElement.multiply(this.q2))).multiply(2)).subtract(y2);
        tArr[2] = (RealFieldElement) ((RealFieldElement) ((RealFieldElement) ((RealFieldElement) realFieldElement2.multiply((RealFieldElement) ((RealFieldElement) z2.multiply(realFieldElement2)).subtract((RealFieldElement) ((RealFieldElement) this.q1.multiply(y2)).subtract((RealFieldElement) this.q2.multiply(x2))))).add((RealFieldElement) realFieldElement.multiply(this.q3))).multiply(2)).subtract(z2);
    }

    public void applyInverseTo(double[] in, T[] tArr) {
        double x2 = in[0];
        double y2 = in[1];
        double z2 = in[2];
        RealFieldElement realFieldElement = (RealFieldElement) ((RealFieldElement) ((RealFieldElement) this.q1.multiply(x2)).add((RealFieldElement) this.q2.multiply(y2))).add((RealFieldElement) this.q3.multiply(z2));
        RealFieldElement realFieldElement2 = (RealFieldElement) this.q0.negate();
        tArr[0] = (RealFieldElement) ((RealFieldElement) ((RealFieldElement) ((RealFieldElement) realFieldElement2.multiply((RealFieldElement) ((RealFieldElement) realFieldElement2.multiply(x2)).subtract((RealFieldElement) ((RealFieldElement) this.q2.multiply(z2)).subtract((RealFieldElement) this.q3.multiply(y2))))).add((RealFieldElement) realFieldElement.multiply(this.q1))).multiply(2)).subtract(x2);
        tArr[1] = (RealFieldElement) ((RealFieldElement) ((RealFieldElement) ((RealFieldElement) realFieldElement2.multiply((RealFieldElement) ((RealFieldElement) realFieldElement2.multiply(y2)).subtract((RealFieldElement) ((RealFieldElement) this.q3.multiply(x2)).subtract((RealFieldElement) this.q1.multiply(z2))))).add((RealFieldElement) realFieldElement.multiply(this.q2))).multiply(2)).subtract(y2);
        tArr[2] = (RealFieldElement) ((RealFieldElement) ((RealFieldElement) ((RealFieldElement) realFieldElement2.multiply((RealFieldElement) ((RealFieldElement) realFieldElement2.multiply(z2)).subtract((RealFieldElement) ((RealFieldElement) this.q1.multiply(y2)).subtract((RealFieldElement) this.q2.multiply(x2))))).add((RealFieldElement) realFieldElement.multiply(this.q3))).multiply(2)).subtract(z2);
    }

    public static <T extends RealFieldElement<T>> FieldVector3D<T> applyInverseTo(Rotation r2, FieldVector3D<T> u2) {
        RealFieldElement x2 = u2.getX();
        RealFieldElement y2 = u2.getY();
        RealFieldElement z2 = u2.getZ();
        RealFieldElement realFieldElement = (RealFieldElement) ((RealFieldElement) ((RealFieldElement) x2.multiply(r2.getQ1())).add((RealFieldElement) y2.multiply(r2.getQ2()))).add((RealFieldElement) z2.multiply(r2.getQ3()));
        double m0 = -r2.getQ0();
        return new FieldVector3D<>((RealFieldElement) ((RealFieldElement) ((RealFieldElement) ((RealFieldElement) ((RealFieldElement) ((RealFieldElement) x2.multiply(m0)).subtract((RealFieldElement) ((RealFieldElement) z2.multiply(r2.getQ2())).subtract((RealFieldElement) y2.multiply(r2.getQ3())))).multiply(m0)).add((RealFieldElement) realFieldElement.multiply(r2.getQ1()))).multiply(2)).subtract(x2), (RealFieldElement) ((RealFieldElement) ((RealFieldElement) ((RealFieldElement) ((RealFieldElement) ((RealFieldElement) y2.multiply(m0)).subtract((RealFieldElement) ((RealFieldElement) x2.multiply(r2.getQ3())).subtract((RealFieldElement) z2.multiply(r2.getQ1())))).multiply(m0)).add((RealFieldElement) realFieldElement.multiply(r2.getQ2()))).multiply(2)).subtract(y2), (RealFieldElement) ((RealFieldElement) ((RealFieldElement) ((RealFieldElement) ((RealFieldElement) ((RealFieldElement) z2.multiply(m0)).subtract((RealFieldElement) ((RealFieldElement) y2.multiply(r2.getQ1())).subtract((RealFieldElement) x2.multiply(r2.getQ2())))).multiply(m0)).add((RealFieldElement) realFieldElement.multiply(r2.getQ3()))).multiply(2)).subtract(z2));
    }

    public FieldRotation<T> applyTo(FieldRotation<T> r2) {
        return compose(r2, RotationConvention.VECTOR_OPERATOR);
    }

    public FieldRotation<T> compose(FieldRotation<T> r2, RotationConvention convention) {
        return convention == RotationConvention.VECTOR_OPERATOR ? composeInternal(r2) : r2.composeInternal(this);
    }

    private FieldRotation<T> composeInternal(FieldRotation<T> r2) {
        return new FieldRotation<>((RealFieldElement) ((RealFieldElement) r2.q0.multiply(this.q0)).subtract((RealFieldElement) ((RealFieldElement) ((RealFieldElement) r2.q1.multiply(this.q1)).add((RealFieldElement) r2.q2.multiply(this.q2))).add((RealFieldElement) r2.q3.multiply(this.q3))), (RealFieldElement) ((RealFieldElement) ((RealFieldElement) r2.q1.multiply(this.q0)).add((RealFieldElement) r2.q0.multiply(this.q1))).add((RealFieldElement) ((RealFieldElement) r2.q2.multiply(this.q3)).subtract((RealFieldElement) r2.q3.multiply(this.q2))), (RealFieldElement) ((RealFieldElement) ((RealFieldElement) r2.q2.multiply(this.q0)).add((RealFieldElement) r2.q0.multiply(this.q2))).add((RealFieldElement) ((RealFieldElement) r2.q3.multiply(this.q1)).subtract((RealFieldElement) r2.q1.multiply(this.q3))), (RealFieldElement) ((RealFieldElement) ((RealFieldElement) r2.q3.multiply(this.q0)).add((RealFieldElement) r2.q0.multiply(this.q3))).add((RealFieldElement) ((RealFieldElement) r2.q1.multiply(this.q2)).subtract((RealFieldElement) r2.q2.multiply(this.q1))), false);
    }

    public FieldRotation<T> applyTo(Rotation r2) {
        return compose(r2, RotationConvention.VECTOR_OPERATOR);
    }

    public FieldRotation<T> compose(Rotation r2, RotationConvention convention) {
        return convention == RotationConvention.VECTOR_OPERATOR ? composeInternal(r2) : applyTo(r2, this);
    }

    private FieldRotation<T> composeInternal(Rotation r2) {
        return new FieldRotation<>((RealFieldElement) ((RealFieldElement) this.q0.multiply(r2.getQ0())).subtract((RealFieldElement) ((RealFieldElement) ((RealFieldElement) this.q1.multiply(r2.getQ1())).add((RealFieldElement) this.q2.multiply(r2.getQ2()))).add((RealFieldElement) this.q3.multiply(r2.getQ3()))), (RealFieldElement) ((RealFieldElement) ((RealFieldElement) this.q0.multiply(r2.getQ1())).add((RealFieldElement) this.q1.multiply(r2.getQ0()))).add((RealFieldElement) ((RealFieldElement) this.q3.multiply(r2.getQ2())).subtract((RealFieldElement) this.q2.multiply(r2.getQ3()))), (RealFieldElement) ((RealFieldElement) ((RealFieldElement) this.q0.multiply(r2.getQ2())).add((RealFieldElement) this.q2.multiply(r2.getQ0()))).add((RealFieldElement) ((RealFieldElement) this.q1.multiply(r2.getQ3())).subtract((RealFieldElement) this.q3.multiply(r2.getQ1()))), (RealFieldElement) ((RealFieldElement) ((RealFieldElement) this.q0.multiply(r2.getQ3())).add((RealFieldElement) this.q3.multiply(r2.getQ0()))).add((RealFieldElement) ((RealFieldElement) this.q2.multiply(r2.getQ1())).subtract((RealFieldElement) this.q1.multiply(r2.getQ2()))), false);
    }

    public static <T extends RealFieldElement<T>> FieldRotation<T> applyTo(Rotation r1, FieldRotation<T> rInner) {
        return new FieldRotation<>((RealFieldElement) ((RealFieldElement) ((FieldRotation) rInner).q0.multiply(r1.getQ0())).subtract((RealFieldElement) ((RealFieldElement) ((RealFieldElement) ((FieldRotation) rInner).q1.multiply(r1.getQ1())).add((RealFieldElement) ((FieldRotation) rInner).q2.multiply(r1.getQ2()))).add((RealFieldElement) ((FieldRotation) rInner).q3.multiply(r1.getQ3()))), (RealFieldElement) ((RealFieldElement) ((RealFieldElement) ((FieldRotation) rInner).q1.multiply(r1.getQ0())).add((RealFieldElement) ((FieldRotation) rInner).q0.multiply(r1.getQ1()))).add((RealFieldElement) ((RealFieldElement) ((FieldRotation) rInner).q2.multiply(r1.getQ3())).subtract((RealFieldElement) ((FieldRotation) rInner).q3.multiply(r1.getQ2()))), (RealFieldElement) ((RealFieldElement) ((RealFieldElement) ((FieldRotation) rInner).q2.multiply(r1.getQ0())).add((RealFieldElement) ((FieldRotation) rInner).q0.multiply(r1.getQ2()))).add((RealFieldElement) ((RealFieldElement) ((FieldRotation) rInner).q3.multiply(r1.getQ1())).subtract((RealFieldElement) ((FieldRotation) rInner).q1.multiply(r1.getQ3()))), (RealFieldElement) ((RealFieldElement) ((RealFieldElement) ((FieldRotation) rInner).q3.multiply(r1.getQ0())).add((RealFieldElement) ((FieldRotation) rInner).q0.multiply(r1.getQ3()))).add((RealFieldElement) ((RealFieldElement) ((FieldRotation) rInner).q1.multiply(r1.getQ2())).subtract((RealFieldElement) ((FieldRotation) rInner).q2.multiply(r1.getQ1()))), false);
    }

    public FieldRotation<T> applyInverseTo(FieldRotation<T> r2) {
        return composeInverse(r2, RotationConvention.VECTOR_OPERATOR);
    }

    public FieldRotation<T> composeInverse(FieldRotation<T> r2, RotationConvention convention) {
        return convention == RotationConvention.VECTOR_OPERATOR ? composeInverseInternal(r2) : r2.composeInternal(revert());
    }

    private FieldRotation<T> composeInverseInternal(FieldRotation<T> r2) {
        return new FieldRotation<>((RealFieldElement) ((RealFieldElement) ((RealFieldElement) r2.q0.multiply(this.q0)).add((RealFieldElement) ((RealFieldElement) ((RealFieldElement) r2.q1.multiply(this.q1)).add((RealFieldElement) r2.q2.multiply(this.q2))).add((RealFieldElement) r2.q3.multiply(this.q3)))).negate(), (RealFieldElement) ((RealFieldElement) ((RealFieldElement) r2.q0.multiply(this.q1)).add((RealFieldElement) ((RealFieldElement) r2.q2.multiply(this.q3)).subtract((RealFieldElement) r2.q3.multiply(this.q2)))).subtract((RealFieldElement) r2.q1.multiply(this.q0)), (RealFieldElement) ((RealFieldElement) ((RealFieldElement) r2.q0.multiply(this.q2)).add((RealFieldElement) ((RealFieldElement) r2.q3.multiply(this.q1)).subtract((RealFieldElement) r2.q1.multiply(this.q3)))).subtract((RealFieldElement) r2.q2.multiply(this.q0)), (RealFieldElement) ((RealFieldElement) ((RealFieldElement) r2.q0.multiply(this.q3)).add((RealFieldElement) ((RealFieldElement) r2.q1.multiply(this.q2)).subtract((RealFieldElement) r2.q2.multiply(this.q1)))).subtract((RealFieldElement) r2.q3.multiply(this.q0)), false);
    }

    public FieldRotation<T> applyInverseTo(Rotation r2) {
        return composeInverse(r2, RotationConvention.VECTOR_OPERATOR);
    }

    public FieldRotation<T> composeInverse(Rotation r2, RotationConvention convention) {
        return convention == RotationConvention.VECTOR_OPERATOR ? composeInverseInternal(r2) : applyTo(r2, revert());
    }

    private FieldRotation<T> composeInverseInternal(Rotation r2) {
        return new FieldRotation<>((RealFieldElement) ((RealFieldElement) ((RealFieldElement) this.q0.multiply(r2.getQ0())).add((RealFieldElement) ((RealFieldElement) ((RealFieldElement) this.q1.multiply(r2.getQ1())).add((RealFieldElement) this.q2.multiply(r2.getQ2()))).add((RealFieldElement) this.q3.multiply(r2.getQ3())))).negate(), (RealFieldElement) ((RealFieldElement) ((RealFieldElement) this.q1.multiply(r2.getQ0())).add((RealFieldElement) ((RealFieldElement) this.q3.multiply(r2.getQ2())).subtract((RealFieldElement) this.q2.multiply(r2.getQ3())))).subtract((RealFieldElement) this.q0.multiply(r2.getQ1())), (RealFieldElement) ((RealFieldElement) ((RealFieldElement) this.q2.multiply(r2.getQ0())).add((RealFieldElement) ((RealFieldElement) this.q1.multiply(r2.getQ3())).subtract((RealFieldElement) this.q3.multiply(r2.getQ1())))).subtract((RealFieldElement) this.q0.multiply(r2.getQ2())), (RealFieldElement) ((RealFieldElement) ((RealFieldElement) this.q3.multiply(r2.getQ0())).add((RealFieldElement) ((RealFieldElement) this.q2.multiply(r2.getQ1())).subtract((RealFieldElement) this.q1.multiply(r2.getQ2())))).subtract((RealFieldElement) this.q0.multiply(r2.getQ3())), false);
    }

    public static <T extends RealFieldElement<T>> FieldRotation<T> applyInverseTo(Rotation rOuter, FieldRotation<T> rInner) {
        return new FieldRotation<>((RealFieldElement) ((RealFieldElement) ((RealFieldElement) ((FieldRotation) rInner).q0.multiply(rOuter.getQ0())).add((RealFieldElement) ((RealFieldElement) ((RealFieldElement) ((FieldRotation) rInner).q1.multiply(rOuter.getQ1())).add((RealFieldElement) ((FieldRotation) rInner).q2.multiply(rOuter.getQ2()))).add((RealFieldElement) ((FieldRotation) rInner).q3.multiply(rOuter.getQ3())))).negate(), (RealFieldElement) ((RealFieldElement) ((RealFieldElement) ((FieldRotation) rInner).q0.multiply(rOuter.getQ1())).add((RealFieldElement) ((RealFieldElement) ((FieldRotation) rInner).q2.multiply(rOuter.getQ3())).subtract((RealFieldElement) ((FieldRotation) rInner).q3.multiply(rOuter.getQ2())))).subtract((RealFieldElement) ((FieldRotation) rInner).q1.multiply(rOuter.getQ0())), (RealFieldElement) ((RealFieldElement) ((RealFieldElement) ((FieldRotation) rInner).q0.multiply(rOuter.getQ2())).add((RealFieldElement) ((RealFieldElement) ((FieldRotation) rInner).q3.multiply(rOuter.getQ1())).subtract((RealFieldElement) ((FieldRotation) rInner).q1.multiply(rOuter.getQ3())))).subtract((RealFieldElement) ((FieldRotation) rInner).q2.multiply(rOuter.getQ0())), (RealFieldElement) ((RealFieldElement) ((RealFieldElement) ((FieldRotation) rInner).q0.multiply(rOuter.getQ3())).add((RealFieldElement) ((RealFieldElement) ((FieldRotation) rInner).q1.multiply(rOuter.getQ2())).subtract((RealFieldElement) ((FieldRotation) rInner).q2.multiply(rOuter.getQ1())))).subtract((RealFieldElement) ((FieldRotation) rInner).q3.multiply(rOuter.getQ0())), false);
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v119 */
    /* JADX WARN: Type inference failed for: r0v121 */
    /* JADX WARN: Type inference failed for: r0v123 */
    /* JADX WARN: Type inference failed for: r0v125 */
    /* JADX WARN: Type inference failed for: r0v127 */
    /* JADX WARN: Type inference failed for: r0v129 */
    /* JADX WARN: Type inference failed for: r0v131 */
    /* JADX WARN: Type inference failed for: r0v133 */
    /* JADX WARN: Type inference failed for: r0v135 */
    /* JADX WARN: Type inference failed for: r0v137 */
    /* JADX WARN: Type inference failed for: r0v138, types: [org.apache.commons.math3.RealFieldElement] */
    /* JADX WARN: Type inference failed for: r0v142 */
    /* JADX WARN: Type inference failed for: r0v143, types: [org.apache.commons.math3.RealFieldElement] */
    /* JADX WARN: Type inference failed for: r0v147 */
    /* JADX WARN: Type inference failed for: r0v148, types: [org.apache.commons.math3.RealFieldElement] */
    /* JADX WARN: Type inference failed for: r0v152 */
    /* JADX WARN: Type inference failed for: r0v153, types: [org.apache.commons.math3.RealFieldElement] */
    /* JADX WARN: Type inference failed for: r0v157 */
    /* JADX WARN: Type inference failed for: r0v158, types: [org.apache.commons.math3.RealFieldElement] */
    /* JADX WARN: Type inference failed for: r0v162 */
    /* JADX WARN: Type inference failed for: r0v163, types: [org.apache.commons.math3.RealFieldElement] */
    /* JADX WARN: Type inference failed for: r0v167 */
    /* JADX WARN: Type inference failed for: r0v168, types: [org.apache.commons.math3.RealFieldElement] */
    /* JADX WARN: Type inference failed for: r0v172 */
    /* JADX WARN: Type inference failed for: r0v173, types: [org.apache.commons.math3.RealFieldElement] */
    /* JADX WARN: Type inference failed for: r0v177 */
    /* JADX WARN: Type inference failed for: r0v178, types: [org.apache.commons.math3.RealFieldElement] */
    /* JADX WARN: Type inference failed for: r0v197 */
    /* JADX WARN: Type inference failed for: r0v198 */
    /* JADX WARN: Type inference failed for: r0v200 */
    /* JADX WARN: Type inference failed for: r0v201 */
    /* JADX WARN: Type inference failed for: r0v203 */
    /* JADX WARN: Type inference failed for: r0v204 */
    /* JADX WARN: Type inference failed for: r0v206 */
    /* JADX WARN: Type inference failed for: r0v207 */
    /* JADX WARN: Type inference failed for: r0v209 */
    /* JADX WARN: Type inference failed for: r0v210 */
    /* JADX WARN: Type inference failed for: r0v212 */
    /* JADX WARN: Type inference failed for: r0v213 */
    /* JADX WARN: Type inference failed for: r0v215 */
    /* JADX WARN: Type inference failed for: r0v216 */
    /* JADX WARN: Type inference failed for: r0v218 */
    /* JADX WARN: Type inference failed for: r0v219 */
    /* JADX WARN: Type inference failed for: r0v221 */
    /* JADX WARN: Type inference failed for: r0v222 */
    /* JADX WARN: Type inference failed for: r0v33, types: [T extends org.apache.commons.math3.RealFieldElement<T>[][], org.apache.commons.math3.RealFieldElement[][]] */
    private T[][] orthogonalizeMatrix(T[][] tArr, double d2) throws NotARotationMatrixException {
        T t2 = tArr[0][0];
        T t3 = tArr[0][1];
        T t4 = tArr[0][2];
        T t5 = tArr[1][0];
        T t6 = tArr[1][1];
        T t7 = tArr[1][2];
        T t8 = tArr[2][0];
        T t9 = tArr[2][1];
        T t10 = tArr[2][2];
        double d3 = 0.0d;
        ?? r0 = (T[][]) ((RealFieldElement[][]) MathArrays.buildArray(tArr[0][0].getField2(), 3, 3));
        int i2 = 0;
        while (true) {
            i2++;
            if (i2 < 11) {
                RealFieldElement realFieldElement = (RealFieldElement) ((RealFieldElement) ((RealFieldElement) tArr[0][0].multiply(t2)).add((RealFieldElement) tArr[1][0].multiply(t5))).add((RealFieldElement) tArr[2][0].multiply(t8));
                RealFieldElement realFieldElement2 = (RealFieldElement) ((RealFieldElement) ((RealFieldElement) tArr[0][1].multiply(t2)).add((RealFieldElement) tArr[1][1].multiply(t5))).add((RealFieldElement) tArr[2][1].multiply(t8));
                RealFieldElement realFieldElement3 = (RealFieldElement) ((RealFieldElement) ((RealFieldElement) tArr[0][2].multiply(t2)).add((RealFieldElement) tArr[1][2].multiply(t5))).add((RealFieldElement) tArr[2][2].multiply(t8));
                RealFieldElement realFieldElement4 = (RealFieldElement) ((RealFieldElement) ((RealFieldElement) tArr[0][0].multiply(t3)).add((RealFieldElement) tArr[1][0].multiply(t6))).add((RealFieldElement) tArr[2][0].multiply(t9));
                RealFieldElement realFieldElement5 = (RealFieldElement) ((RealFieldElement) ((RealFieldElement) tArr[0][1].multiply(t3)).add((RealFieldElement) tArr[1][1].multiply(t6))).add((RealFieldElement) tArr[2][1].multiply(t9));
                RealFieldElement realFieldElement6 = (RealFieldElement) ((RealFieldElement) ((RealFieldElement) tArr[0][2].multiply(t3)).add((RealFieldElement) tArr[1][2].multiply(t6))).add((RealFieldElement) tArr[2][2].multiply(t9));
                RealFieldElement realFieldElement7 = (RealFieldElement) ((RealFieldElement) ((RealFieldElement) tArr[0][0].multiply(t4)).add((RealFieldElement) tArr[1][0].multiply(t7))).add((RealFieldElement) tArr[2][0].multiply(t10));
                RealFieldElement realFieldElement8 = (RealFieldElement) ((RealFieldElement) ((RealFieldElement) tArr[0][1].multiply(t4)).add((RealFieldElement) tArr[1][1].multiply(t7))).add((RealFieldElement) tArr[2][1].multiply(t10));
                RealFieldElement realFieldElement9 = (RealFieldElement) ((RealFieldElement) ((RealFieldElement) tArr[0][2].multiply(t4)).add((RealFieldElement) tArr[1][2].multiply(t7))).add((RealFieldElement) tArr[2][2].multiply(t10));
                r0[0][0] = (RealFieldElement) t2.subtract(((RealFieldElement) ((RealFieldElement) ((RealFieldElement) ((RealFieldElement) t2.multiply(realFieldElement)).add((RealFieldElement) t3.multiply(realFieldElement2))).add((RealFieldElement) t4.multiply(realFieldElement3))).subtract(tArr[0][0])).multiply(0.5d));
                r0[0][1] = (RealFieldElement) t3.subtract(((RealFieldElement) ((RealFieldElement) ((RealFieldElement) ((RealFieldElement) t2.multiply(realFieldElement4)).add((RealFieldElement) t3.multiply(realFieldElement5))).add((RealFieldElement) t4.multiply(realFieldElement6))).subtract(tArr[0][1])).multiply(0.5d));
                r0[0][2] = (RealFieldElement) t4.subtract(((RealFieldElement) ((RealFieldElement) ((RealFieldElement) ((RealFieldElement) t2.multiply(realFieldElement7)).add((RealFieldElement) t3.multiply(realFieldElement8))).add((RealFieldElement) t4.multiply(realFieldElement9))).subtract(tArr[0][2])).multiply(0.5d));
                r0[1][0] = (RealFieldElement) t5.subtract(((RealFieldElement) ((RealFieldElement) ((RealFieldElement) ((RealFieldElement) t5.multiply(realFieldElement)).add((RealFieldElement) t6.multiply(realFieldElement2))).add((RealFieldElement) t7.multiply(realFieldElement3))).subtract(tArr[1][0])).multiply(0.5d));
                r0[1][1] = (RealFieldElement) t6.subtract(((RealFieldElement) ((RealFieldElement) ((RealFieldElement) ((RealFieldElement) t5.multiply(realFieldElement4)).add((RealFieldElement) t6.multiply(realFieldElement5))).add((RealFieldElement) t7.multiply(realFieldElement6))).subtract(tArr[1][1])).multiply(0.5d));
                r0[1][2] = (RealFieldElement) t7.subtract(((RealFieldElement) ((RealFieldElement) ((RealFieldElement) ((RealFieldElement) t5.multiply(realFieldElement7)).add((RealFieldElement) t6.multiply(realFieldElement8))).add((RealFieldElement) t7.multiply(realFieldElement9))).subtract(tArr[1][2])).multiply(0.5d));
                r0[2][0] = (RealFieldElement) t8.subtract(((RealFieldElement) ((RealFieldElement) ((RealFieldElement) ((RealFieldElement) t8.multiply(realFieldElement)).add((RealFieldElement) t9.multiply(realFieldElement2))).add((RealFieldElement) t10.multiply(realFieldElement3))).subtract(tArr[2][0])).multiply(0.5d));
                r0[2][1] = (RealFieldElement) t9.subtract(((RealFieldElement) ((RealFieldElement) ((RealFieldElement) ((RealFieldElement) t8.multiply(realFieldElement4)).add((RealFieldElement) t9.multiply(realFieldElement5))).add((RealFieldElement) t10.multiply(realFieldElement6))).subtract(tArr[2][1])).multiply(0.5d));
                r0[2][2] = (RealFieldElement) t10.subtract(((RealFieldElement) ((RealFieldElement) ((RealFieldElement) ((RealFieldElement) t8.multiply(realFieldElement7)).add((RealFieldElement) t9.multiply(realFieldElement8))).add((RealFieldElement) t10.multiply(realFieldElement9))).subtract(tArr[2][2])).multiply(0.5d));
                double real = r0[0][0].getReal() - tArr[0][0].getReal();
                double real2 = r0[0][1].getReal() - tArr[0][1].getReal();
                double real3 = r0[0][2].getReal() - tArr[0][2].getReal();
                double real4 = r0[1][0].getReal() - tArr[1][0].getReal();
                double real5 = r0[1][1].getReal() - tArr[1][1].getReal();
                double real6 = r0[1][2].getReal() - tArr[1][2].getReal();
                double real7 = r0[2][0].getReal() - tArr[2][0].getReal();
                double real8 = r0[2][1].getReal() - tArr[2][1].getReal();
                double real9 = r0[2][2].getReal() - tArr[2][2].getReal();
                double d4 = (real * real) + (real2 * real2) + (real3 * real3) + (real4 * real4) + (real5 * real5) + (real6 * real6) + (real7 * real7) + (real8 * real8) + (real9 * real9);
                if (FastMath.abs(d4 - d3) <= d2) {
                    return r0;
                }
                t2 = r0[0][0];
                t3 = r0[0][1];
                t4 = r0[0][2];
                t5 = r0[1][0];
                t6 = r0[1][1];
                t7 = r0[1][2];
                t8 = r0[2][0];
                t9 = r0[2][1];
                t10 = r0[2][2];
                d3 = d4;
            } else {
                throw new NotARotationMatrixException(LocalizedFormats.UNABLE_TO_ORTHOGONOLIZE_MATRIX, Integer.valueOf(i2 - 1));
            }
        }
    }

    public static <T extends RealFieldElement<T>> T distance(FieldRotation<T> fieldRotation, FieldRotation<T> fieldRotation2) {
        return (T) fieldRotation.composeInverseInternal(fieldRotation2).getAngle();
    }
}
