package org.apache.commons.math3.analysis.differentiation;

import java.io.Serializable;
import org.apache.commons.math3.Field;
import org.apache.commons.math3.FieldElement;
import org.apache.commons.math3.RealFieldElement;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.MathArithmeticException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.util.FastMath;
import org.apache.commons.math3.util.MathArrays;
import org.apache.commons.math3.util.MathUtils;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/analysis/differentiation/DerivativeStructure.class */
public class DerivativeStructure implements RealFieldElement<DerivativeStructure>, Serializable {
    private static final long serialVersionUID = 20120730;
    private transient DSCompiler compiler;
    private final double[] data;

    private DerivativeStructure(DSCompiler compiler) {
        this.compiler = compiler;
        this.data = new double[compiler.getSize()];
    }

    public DerivativeStructure(int parameters, int order) throws NumberIsTooLargeException {
        this(DSCompiler.getCompiler(parameters, order));
    }

    public DerivativeStructure(int parameters, int order, double value) throws NumberIsTooLargeException {
        this(parameters, order);
        this.data[0] = value;
    }

    public DerivativeStructure(int parameters, int order, int index, double value) throws NumberIsTooLargeException {
        this(parameters, order, value);
        if (index >= parameters) {
            throw new NumberIsTooLargeException(Integer.valueOf(index), Integer.valueOf(parameters), false);
        }
        if (order > 0) {
            this.data[DSCompiler.getCompiler(index, order).getSize()] = 1.0d;
        }
    }

    public DerivativeStructure(double a1, DerivativeStructure ds1, double a2, DerivativeStructure ds2) throws DimensionMismatchException {
        this(ds1.compiler);
        this.compiler.checkCompatibility(ds2.compiler);
        this.compiler.linearCombination(a1, ds1.data, 0, a2, ds2.data, 0, this.data, 0);
    }

    public DerivativeStructure(double a1, DerivativeStructure ds1, double a2, DerivativeStructure ds2, double a3, DerivativeStructure ds3) throws DimensionMismatchException {
        this(ds1.compiler);
        this.compiler.checkCompatibility(ds2.compiler);
        this.compiler.checkCompatibility(ds3.compiler);
        this.compiler.linearCombination(a1, ds1.data, 0, a2, ds2.data, 0, a3, ds3.data, 0, this.data, 0);
    }

    public DerivativeStructure(double a1, DerivativeStructure ds1, double a2, DerivativeStructure ds2, double a3, DerivativeStructure ds3, double a4, DerivativeStructure ds4) throws DimensionMismatchException {
        this(ds1.compiler);
        this.compiler.checkCompatibility(ds2.compiler);
        this.compiler.checkCompatibility(ds3.compiler);
        this.compiler.checkCompatibility(ds4.compiler);
        this.compiler.linearCombination(a1, ds1.data, 0, a2, ds2.data, 0, a3, ds3.data, 0, a4, ds4.data, 0, this.data, 0);
    }

    public DerivativeStructure(int parameters, int order, double... derivatives) throws DimensionMismatchException, NumberIsTooLargeException {
        this(parameters, order);
        if (derivatives.length != this.data.length) {
            throw new DimensionMismatchException(derivatives.length, this.data.length);
        }
        System.arraycopy(derivatives, 0, this.data, 0, this.data.length);
    }

    private DerivativeStructure(DerivativeStructure ds) {
        this.compiler = ds.compiler;
        this.data = (double[]) ds.data.clone();
    }

    public int getFreeParameters() {
        return this.compiler.getFreeParameters();
    }

    public int getOrder() {
        return this.compiler.getOrder();
    }

    public DerivativeStructure createConstant(double c2) {
        return new DerivativeStructure(getFreeParameters(), getOrder(), c2);
    }

    @Override // org.apache.commons.math3.RealFieldElement
    public double getReal() {
        return this.data[0];
    }

    public double getValue() {
        return this.data[0];
    }

    public double getPartialDerivative(int... orders) throws DimensionMismatchException, NumberIsTooLargeException {
        return this.data[this.compiler.getPartialDerivativeIndex(orders)];
    }

    public double[] getAllDerivatives() {
        return (double[]) this.data.clone();
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.apache.commons.math3.RealFieldElement
    public DerivativeStructure add(double a2) {
        DerivativeStructure ds = new DerivativeStructure(this);
        double[] dArr = ds.data;
        dArr[0] = dArr[0] + a2;
        return ds;
    }

    @Override // org.apache.commons.math3.FieldElement
    public DerivativeStructure add(DerivativeStructure a2) throws DimensionMismatchException {
        this.compiler.checkCompatibility(a2.compiler);
        DerivativeStructure ds = new DerivativeStructure(this);
        this.compiler.add(this.data, 0, a2.data, 0, ds.data, 0);
        return ds;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.apache.commons.math3.RealFieldElement
    public DerivativeStructure subtract(double a2) {
        return add(-a2);
    }

    @Override // org.apache.commons.math3.FieldElement
    public DerivativeStructure subtract(DerivativeStructure a2) throws DimensionMismatchException {
        this.compiler.checkCompatibility(a2.compiler);
        DerivativeStructure ds = new DerivativeStructure(this);
        this.compiler.subtract(this.data, 0, a2.data, 0, ds.data, 0);
        return ds;
    }

    @Override // org.apache.commons.math3.FieldElement
    public DerivativeStructure multiply(int n2) {
        return multiply(n2);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.apache.commons.math3.RealFieldElement
    public DerivativeStructure multiply(double a2) {
        DerivativeStructure ds = new DerivativeStructure(this);
        for (int i2 = 0; i2 < ds.data.length; i2++) {
            double[] dArr = ds.data;
            int i3 = i2;
            dArr[i3] = dArr[i3] * a2;
        }
        return ds;
    }

    @Override // org.apache.commons.math3.FieldElement
    public DerivativeStructure multiply(DerivativeStructure a2) throws DimensionMismatchException {
        this.compiler.checkCompatibility(a2.compiler);
        DerivativeStructure result = new DerivativeStructure(this.compiler);
        this.compiler.multiply(this.data, 0, a2.data, 0, result.data, 0);
        return result;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.apache.commons.math3.RealFieldElement
    public DerivativeStructure divide(double a2) {
        DerivativeStructure ds = new DerivativeStructure(this);
        for (int i2 = 0; i2 < ds.data.length; i2++) {
            double[] dArr = ds.data;
            int i3 = i2;
            dArr[i3] = dArr[i3] / a2;
        }
        return ds;
    }

    @Override // org.apache.commons.math3.FieldElement
    public DerivativeStructure divide(DerivativeStructure a2) throws DimensionMismatchException {
        this.compiler.checkCompatibility(a2.compiler);
        DerivativeStructure result = new DerivativeStructure(this.compiler);
        this.compiler.divide(this.data, 0, a2.data, 0, result.data, 0);
        return result;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.apache.commons.math3.RealFieldElement
    public DerivativeStructure remainder(double a2) {
        DerivativeStructure ds = new DerivativeStructure(this);
        ds.data[0] = FastMath.IEEEremainder(ds.data[0], a2);
        return ds;
    }

    @Override // org.apache.commons.math3.RealFieldElement
    public DerivativeStructure remainder(DerivativeStructure a2) throws DimensionMismatchException {
        this.compiler.checkCompatibility(a2.compiler);
        DerivativeStructure result = new DerivativeStructure(this.compiler);
        this.compiler.remainder(this.data, 0, a2.data, 0, result.data, 0);
        return result;
    }

    @Override // org.apache.commons.math3.FieldElement
    public DerivativeStructure negate() {
        DerivativeStructure ds = new DerivativeStructure(this.compiler);
        for (int i2 = 0; i2 < ds.data.length; i2++) {
            ds.data[i2] = -this.data[i2];
        }
        return ds;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.apache.commons.math3.RealFieldElement
    public DerivativeStructure abs() {
        if (Double.doubleToLongBits(this.data[0]) < 0) {
            return negate();
        }
        return this;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.apache.commons.math3.RealFieldElement
    public DerivativeStructure ceil() {
        return new DerivativeStructure(this.compiler.getFreeParameters(), this.compiler.getOrder(), FastMath.ceil(this.data[0]));
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.apache.commons.math3.RealFieldElement
    public DerivativeStructure floor() {
        return new DerivativeStructure(this.compiler.getFreeParameters(), this.compiler.getOrder(), FastMath.floor(this.data[0]));
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.apache.commons.math3.RealFieldElement
    public DerivativeStructure rint() {
        return new DerivativeStructure(this.compiler.getFreeParameters(), this.compiler.getOrder(), FastMath.rint(this.data[0]));
    }

    @Override // org.apache.commons.math3.RealFieldElement
    public long round() {
        return FastMath.round(this.data[0]);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.apache.commons.math3.RealFieldElement
    public DerivativeStructure signum() {
        return new DerivativeStructure(this.compiler.getFreeParameters(), this.compiler.getOrder(), FastMath.signum(this.data[0]));
    }

    @Override // org.apache.commons.math3.RealFieldElement
    public DerivativeStructure copySign(DerivativeStructure sign) {
        long m2 = Double.doubleToLongBits(this.data[0]);
        long s2 = Double.doubleToLongBits(sign.data[0]);
        if ((m2 >= 0 && s2 >= 0) || (m2 < 0 && s2 < 0)) {
            return this;
        }
        return negate();
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.apache.commons.math3.RealFieldElement
    public DerivativeStructure copySign(double sign) {
        long m2 = Double.doubleToLongBits(this.data[0]);
        long s2 = Double.doubleToLongBits(sign);
        if ((m2 >= 0 && s2 >= 0) || (m2 < 0 && s2 < 0)) {
            return this;
        }
        return negate();
    }

    public int getExponent() {
        return FastMath.getExponent(this.data[0]);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.apache.commons.math3.RealFieldElement
    public DerivativeStructure scalb(int n2) {
        DerivativeStructure ds = new DerivativeStructure(this.compiler);
        for (int i2 = 0; i2 < ds.data.length; i2++) {
            ds.data[i2] = FastMath.scalb(this.data[i2], n2);
        }
        return ds;
    }

    @Override // org.apache.commons.math3.RealFieldElement
    public DerivativeStructure hypot(DerivativeStructure y2) throws DimensionMismatchException {
        this.compiler.checkCompatibility(y2.compiler);
        if (Double.isInfinite(this.data[0]) || Double.isInfinite(y2.data[0])) {
            return new DerivativeStructure(this.compiler.getFreeParameters(), this.compiler.getFreeParameters(), Double.POSITIVE_INFINITY);
        }
        if (Double.isNaN(this.data[0]) || Double.isNaN(y2.data[0])) {
            return new DerivativeStructure(this.compiler.getFreeParameters(), this.compiler.getFreeParameters(), Double.NaN);
        }
        int expX = getExponent();
        int expY = y2.getExponent();
        if (expX > expY + 27) {
            return abs();
        }
        if (expY > expX + 27) {
            return y2.abs();
        }
        int middleExp = (expX + expY) / 2;
        DerivativeStructure scaledX = scalb(-middleExp);
        DerivativeStructure scaledY = y2.scalb(-middleExp);
        DerivativeStructure scaledH = scaledX.multiply(scaledX).add(scaledY.multiply(scaledY)).sqrt();
        return scaledH.scalb(middleExp);
    }

    public static DerivativeStructure hypot(DerivativeStructure x2, DerivativeStructure y2) throws DimensionMismatchException {
        return x2.hypot(y2);
    }

    public DerivativeStructure compose(double... f2) throws DimensionMismatchException {
        if (f2.length != getOrder() + 1) {
            throw new DimensionMismatchException(f2.length, getOrder() + 1);
        }
        DerivativeStructure result = new DerivativeStructure(this.compiler);
        this.compiler.compose(this.data, 0, f2, result.data, 0);
        return result;
    }

    @Override // org.apache.commons.math3.RealFieldElement, org.apache.commons.math3.FieldElement
    public DerivativeStructure reciprocal() {
        DerivativeStructure result = new DerivativeStructure(this.compiler);
        this.compiler.pow(this.data, 0, -1, result.data, 0);
        return result;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.apache.commons.math3.RealFieldElement
    public DerivativeStructure sqrt() {
        return rootN(2);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.apache.commons.math3.RealFieldElement
    public DerivativeStructure cbrt() {
        return rootN(3);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.apache.commons.math3.RealFieldElement
    public DerivativeStructure rootN(int n2) {
        DerivativeStructure result = new DerivativeStructure(this.compiler);
        this.compiler.rootN(this.data, 0, n2, result.data, 0);
        return result;
    }

    @Override // org.apache.commons.math3.FieldElement
    public Field<DerivativeStructure> getField() {
        return new Field<DerivativeStructure>() { // from class: org.apache.commons.math3.analysis.differentiation.DerivativeStructure.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // org.apache.commons.math3.Field
            public DerivativeStructure getZero() {
                return new DerivativeStructure(DerivativeStructure.this.compiler.getFreeParameters(), DerivativeStructure.this.compiler.getOrder(), 0.0d);
            }

            /* JADX WARN: Can't rename method to resolve collision */
            @Override // org.apache.commons.math3.Field
            public DerivativeStructure getOne() {
                return new DerivativeStructure(DerivativeStructure.this.compiler.getFreeParameters(), DerivativeStructure.this.compiler.getOrder(), 1.0d);
            }

            @Override // org.apache.commons.math3.Field
            public Class<? extends FieldElement<DerivativeStructure>> getRuntimeClass() {
                return DerivativeStructure.class;
            }
        };
    }

    public static DerivativeStructure pow(double a2, DerivativeStructure x2) {
        DerivativeStructure result = new DerivativeStructure(x2.compiler);
        x2.compiler.pow(a2, x2.data, 0, result.data, 0);
        return result;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.apache.commons.math3.RealFieldElement
    public DerivativeStructure pow(double p2) {
        DerivativeStructure result = new DerivativeStructure(this.compiler);
        this.compiler.pow(this.data, 0, p2, result.data, 0);
        return result;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.apache.commons.math3.RealFieldElement
    public DerivativeStructure pow(int n2) {
        DerivativeStructure result = new DerivativeStructure(this.compiler);
        this.compiler.pow(this.data, 0, n2, result.data, 0);
        return result;
    }

    @Override // org.apache.commons.math3.RealFieldElement
    public DerivativeStructure pow(DerivativeStructure e2) throws DimensionMismatchException {
        this.compiler.checkCompatibility(e2.compiler);
        DerivativeStructure result = new DerivativeStructure(this.compiler);
        this.compiler.pow(this.data, 0, e2.data, 0, result.data, 0);
        return result;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.apache.commons.math3.RealFieldElement
    public DerivativeStructure exp() {
        DerivativeStructure result = new DerivativeStructure(this.compiler);
        this.compiler.exp(this.data, 0, result.data, 0);
        return result;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.apache.commons.math3.RealFieldElement
    public DerivativeStructure expm1() {
        DerivativeStructure result = new DerivativeStructure(this.compiler);
        this.compiler.expm1(this.data, 0, result.data, 0);
        return result;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.apache.commons.math3.RealFieldElement
    public DerivativeStructure log() {
        DerivativeStructure result = new DerivativeStructure(this.compiler);
        this.compiler.log(this.data, 0, result.data, 0);
        return result;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.apache.commons.math3.RealFieldElement
    public DerivativeStructure log1p() {
        DerivativeStructure result = new DerivativeStructure(this.compiler);
        this.compiler.log1p(this.data, 0, result.data, 0);
        return result;
    }

    public DerivativeStructure log10() {
        DerivativeStructure result = new DerivativeStructure(this.compiler);
        this.compiler.log10(this.data, 0, result.data, 0);
        return result;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.apache.commons.math3.RealFieldElement
    public DerivativeStructure cos() {
        DerivativeStructure result = new DerivativeStructure(this.compiler);
        this.compiler.cos(this.data, 0, result.data, 0);
        return result;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.apache.commons.math3.RealFieldElement
    public DerivativeStructure sin() {
        DerivativeStructure result = new DerivativeStructure(this.compiler);
        this.compiler.sin(this.data, 0, result.data, 0);
        return result;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.apache.commons.math3.RealFieldElement
    public DerivativeStructure tan() {
        DerivativeStructure result = new DerivativeStructure(this.compiler);
        this.compiler.tan(this.data, 0, result.data, 0);
        return result;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.apache.commons.math3.RealFieldElement
    public DerivativeStructure acos() {
        DerivativeStructure result = new DerivativeStructure(this.compiler);
        this.compiler.acos(this.data, 0, result.data, 0);
        return result;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.apache.commons.math3.RealFieldElement
    public DerivativeStructure asin() {
        DerivativeStructure result = new DerivativeStructure(this.compiler);
        this.compiler.asin(this.data, 0, result.data, 0);
        return result;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.apache.commons.math3.RealFieldElement
    public DerivativeStructure atan() {
        DerivativeStructure result = new DerivativeStructure(this.compiler);
        this.compiler.atan(this.data, 0, result.data, 0);
        return result;
    }

    @Override // org.apache.commons.math3.RealFieldElement
    public DerivativeStructure atan2(DerivativeStructure x2) throws DimensionMismatchException {
        this.compiler.checkCompatibility(x2.compiler);
        DerivativeStructure result = new DerivativeStructure(this.compiler);
        this.compiler.atan2(this.data, 0, x2.data, 0, result.data, 0);
        return result;
    }

    public static DerivativeStructure atan2(DerivativeStructure y2, DerivativeStructure x2) throws DimensionMismatchException {
        return y2.atan2(x2);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.apache.commons.math3.RealFieldElement
    public DerivativeStructure cosh() {
        DerivativeStructure result = new DerivativeStructure(this.compiler);
        this.compiler.cosh(this.data, 0, result.data, 0);
        return result;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.apache.commons.math3.RealFieldElement
    public DerivativeStructure sinh() {
        DerivativeStructure result = new DerivativeStructure(this.compiler);
        this.compiler.sinh(this.data, 0, result.data, 0);
        return result;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.apache.commons.math3.RealFieldElement
    public DerivativeStructure tanh() {
        DerivativeStructure result = new DerivativeStructure(this.compiler);
        this.compiler.tanh(this.data, 0, result.data, 0);
        return result;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.apache.commons.math3.RealFieldElement
    public DerivativeStructure acosh() {
        DerivativeStructure result = new DerivativeStructure(this.compiler);
        this.compiler.acosh(this.data, 0, result.data, 0);
        return result;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.apache.commons.math3.RealFieldElement
    public DerivativeStructure asinh() {
        DerivativeStructure result = new DerivativeStructure(this.compiler);
        this.compiler.asinh(this.data, 0, result.data, 0);
        return result;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.apache.commons.math3.RealFieldElement
    public DerivativeStructure atanh() {
        DerivativeStructure result = new DerivativeStructure(this.compiler);
        this.compiler.atanh(this.data, 0, result.data, 0);
        return result;
    }

    public DerivativeStructure toDegrees() {
        DerivativeStructure ds = new DerivativeStructure(this.compiler);
        for (int i2 = 0; i2 < ds.data.length; i2++) {
            ds.data[i2] = FastMath.toDegrees(this.data[i2]);
        }
        return ds;
    }

    public DerivativeStructure toRadians() {
        DerivativeStructure ds = new DerivativeStructure(this.compiler);
        for (int i2 = 0; i2 < ds.data.length; i2++) {
            ds.data[i2] = FastMath.toRadians(this.data[i2]);
        }
        return ds;
    }

    public double taylor(double... delta) throws MathArithmeticException {
        return this.compiler.taylor(this.data, 0, delta);
    }

    @Override // org.apache.commons.math3.RealFieldElement
    public DerivativeStructure linearCombination(DerivativeStructure[] a2, DerivativeStructure[] b2) throws DimensionMismatchException {
        double[] aDouble = new double[a2.length];
        for (int i2 = 0; i2 < a2.length; i2++) {
            aDouble[i2] = a2[i2].getValue();
        }
        double[] bDouble = new double[b2.length];
        for (int i3 = 0; i3 < b2.length; i3++) {
            bDouble[i3] = b2[i3].getValue();
        }
        double accurateValue = MathArrays.linearCombination(aDouble, bDouble);
        DerivativeStructure simpleValue = a2[0].getField().getZero();
        for (int i4 = 0; i4 < a2.length; i4++) {
            simpleValue = simpleValue.add(a2[i4].multiply(b2[i4]));
        }
        double[] all = simpleValue.getAllDerivatives();
        all[0] = accurateValue;
        return new DerivativeStructure(simpleValue.getFreeParameters(), simpleValue.getOrder(), all);
    }

    @Override // org.apache.commons.math3.RealFieldElement
    public DerivativeStructure linearCombination(double[] a2, DerivativeStructure[] b2) throws DimensionMismatchException {
        double[] bDouble = new double[b2.length];
        for (int i2 = 0; i2 < b2.length; i2++) {
            bDouble[i2] = b2[i2].getValue();
        }
        double accurateValue = MathArrays.linearCombination(a2, bDouble);
        DerivativeStructure simpleValue = b2[0].getField().getZero();
        for (int i3 = 0; i3 < a2.length; i3++) {
            simpleValue = simpleValue.add(b2[i3].multiply(a2[i3]));
        }
        double[] all = simpleValue.getAllDerivatives();
        all[0] = accurateValue;
        return new DerivativeStructure(simpleValue.getFreeParameters(), simpleValue.getOrder(), all);
    }

    @Override // org.apache.commons.math3.RealFieldElement
    public DerivativeStructure linearCombination(DerivativeStructure a1, DerivativeStructure b1, DerivativeStructure a2, DerivativeStructure b2) throws DimensionMismatchException {
        double accurateValue = MathArrays.linearCombination(a1.getValue(), b1.getValue(), a2.getValue(), b2.getValue());
        DerivativeStructure simpleValue = a1.multiply(b1).add(a2.multiply(b2));
        double[] all = simpleValue.getAllDerivatives();
        all[0] = accurateValue;
        return new DerivativeStructure(getFreeParameters(), getOrder(), all);
    }

    @Override // org.apache.commons.math3.RealFieldElement
    public DerivativeStructure linearCombination(double a1, DerivativeStructure b1, double a2, DerivativeStructure b2) throws DimensionMismatchException {
        double accurateValue = MathArrays.linearCombination(a1, b1.getValue(), a2, b2.getValue());
        DerivativeStructure simpleValue = b1.multiply(a1).add(b2.multiply(a2));
        double[] all = simpleValue.getAllDerivatives();
        all[0] = accurateValue;
        return new DerivativeStructure(getFreeParameters(), getOrder(), all);
    }

    @Override // org.apache.commons.math3.RealFieldElement
    public DerivativeStructure linearCombination(DerivativeStructure a1, DerivativeStructure b1, DerivativeStructure a2, DerivativeStructure b2, DerivativeStructure a3, DerivativeStructure b3) throws DimensionMismatchException {
        double accurateValue = MathArrays.linearCombination(a1.getValue(), b1.getValue(), a2.getValue(), b2.getValue(), a3.getValue(), b3.getValue());
        DerivativeStructure simpleValue = a1.multiply(b1).add(a2.multiply(b2)).add(a3.multiply(b3));
        double[] all = simpleValue.getAllDerivatives();
        all[0] = accurateValue;
        return new DerivativeStructure(getFreeParameters(), getOrder(), all);
    }

    @Override // org.apache.commons.math3.RealFieldElement
    public DerivativeStructure linearCombination(double a1, DerivativeStructure b1, double a2, DerivativeStructure b2, double a3, DerivativeStructure b3) throws DimensionMismatchException {
        double accurateValue = MathArrays.linearCombination(a1, b1.getValue(), a2, b2.getValue(), a3, b3.getValue());
        DerivativeStructure simpleValue = b1.multiply(a1).add(b2.multiply(a2)).add(b3.multiply(a3));
        double[] all = simpleValue.getAllDerivatives();
        all[0] = accurateValue;
        return new DerivativeStructure(getFreeParameters(), getOrder(), all);
    }

    @Override // org.apache.commons.math3.RealFieldElement
    public DerivativeStructure linearCombination(DerivativeStructure a1, DerivativeStructure b1, DerivativeStructure a2, DerivativeStructure b2, DerivativeStructure a3, DerivativeStructure b3, DerivativeStructure a4, DerivativeStructure b4) throws DimensionMismatchException {
        double accurateValue = MathArrays.linearCombination(a1.getValue(), b1.getValue(), a2.getValue(), b2.getValue(), a3.getValue(), b3.getValue(), a4.getValue(), b4.getValue());
        DerivativeStructure simpleValue = a1.multiply(b1).add(a2.multiply(b2)).add(a3.multiply(b3)).add(a4.multiply(b4));
        double[] all = simpleValue.getAllDerivatives();
        all[0] = accurateValue;
        return new DerivativeStructure(getFreeParameters(), getOrder(), all);
    }

    @Override // org.apache.commons.math3.RealFieldElement
    public DerivativeStructure linearCombination(double a1, DerivativeStructure b1, double a2, DerivativeStructure b2, double a3, DerivativeStructure b3, double a4, DerivativeStructure b4) throws DimensionMismatchException {
        double accurateValue = MathArrays.linearCombination(a1, b1.getValue(), a2, b2.getValue(), a3, b3.getValue(), a4, b4.getValue());
        DerivativeStructure simpleValue = b1.multiply(a1).add(b2.multiply(a2)).add(b3.multiply(a3)).add(b4.multiply(a4));
        double[] all = simpleValue.getAllDerivatives();
        all[0] = accurateValue;
        return new DerivativeStructure(getFreeParameters(), getOrder(), all);
    }

    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (other instanceof DerivativeStructure) {
            DerivativeStructure rhs = (DerivativeStructure) other;
            return getFreeParameters() == rhs.getFreeParameters() && getOrder() == rhs.getOrder() && MathArrays.equals(this.data, rhs.data);
        }
        return false;
    }

    public int hashCode() {
        return 227 + (229 * getFreeParameters()) + (233 * getOrder()) + (239 * MathUtils.hash(this.data));
    }

    private Object writeReplace() {
        return new DataTransferObject(this.compiler.getFreeParameters(), this.compiler.getOrder(), this.data);
    }

    /* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/analysis/differentiation/DerivativeStructure$DataTransferObject.class */
    private static class DataTransferObject implements Serializable {
        private static final long serialVersionUID = 20120730;
        private final int variables;
        private final int order;
        private final double[] data;

        DataTransferObject(int variables, int order, double[] data) {
            this.variables = variables;
            this.order = order;
            this.data = data;
        }

        private Object readResolve() {
            return new DerivativeStructure(this.variables, this.order, this.data);
        }
    }
}
