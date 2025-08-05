package org.apache.commons.math3.geometry.euclidean.threed;

import java.io.Serializable;
import org.apache.commons.math3.util.FastMath;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/geometry/euclidean/threed/SphericalCoordinates.class */
public class SphericalCoordinates implements Serializable {
    private static final long serialVersionUID = 20130206;

    /* renamed from: v, reason: collision with root package name */
    private final Vector3D f13010v;

    /* renamed from: r, reason: collision with root package name */
    private final double f13011r;
    private final double theta;
    private final double phi;
    private double[][] jacobian;
    private double[][] rHessian;
    private double[][] thetaHessian;
    private double[][] phiHessian;

    public SphericalCoordinates(Vector3D v2) {
        this.f13010v = v2;
        this.f13011r = v2.getNorm();
        this.theta = v2.getAlpha();
        this.phi = FastMath.acos(v2.getZ() / this.f13011r);
    }

    public SphericalCoordinates(double r2, double theta, double phi) {
        double cosTheta = FastMath.cos(theta);
        double sinTheta = FastMath.sin(theta);
        double cosPhi = FastMath.cos(phi);
        double sinPhi = FastMath.sin(phi);
        this.f13011r = r2;
        this.theta = theta;
        this.phi = phi;
        this.f13010v = new Vector3D(r2 * cosTheta * sinPhi, r2 * sinTheta * sinPhi, r2 * cosPhi);
    }

    public Vector3D getCartesian() {
        return this.f13010v;
    }

    public double getR() {
        return this.f13011r;
    }

    public double getTheta() {
        return this.theta;
    }

    public double getPhi() {
        return this.phi;
    }

    public double[] toCartesianGradient(double[] sGradient) {
        computeJacobian();
        return new double[]{(sGradient[0] * this.jacobian[0][0]) + (sGradient[1] * this.jacobian[1][0]) + (sGradient[2] * this.jacobian[2][0]), (sGradient[0] * this.jacobian[0][1]) + (sGradient[1] * this.jacobian[1][1]) + (sGradient[2] * this.jacobian[2][1]), (sGradient[0] * this.jacobian[0][2]) + (sGradient[2] * this.jacobian[2][2])};
    }

    public double[][] toCartesianHessian(double[][] sHessian, double[] sGradient) {
        computeJacobian();
        computeHessians();
        double[][] hj = new double[3][3];
        double[][] cHessian = new double[3][3];
        hj[0][0] = (sHessian[0][0] * this.jacobian[0][0]) + (sHessian[1][0] * this.jacobian[1][0]) + (sHessian[2][0] * this.jacobian[2][0]);
        hj[0][1] = (sHessian[0][0] * this.jacobian[0][1]) + (sHessian[1][0] * this.jacobian[1][1]) + (sHessian[2][0] * this.jacobian[2][1]);
        hj[0][2] = (sHessian[0][0] * this.jacobian[0][2]) + (sHessian[2][0] * this.jacobian[2][2]);
        hj[1][0] = (sHessian[1][0] * this.jacobian[0][0]) + (sHessian[1][1] * this.jacobian[1][0]) + (sHessian[2][1] * this.jacobian[2][0]);
        hj[1][1] = (sHessian[1][0] * this.jacobian[0][1]) + (sHessian[1][1] * this.jacobian[1][1]) + (sHessian[2][1] * this.jacobian[2][1]);
        hj[2][0] = (sHessian[2][0] * this.jacobian[0][0]) + (sHessian[2][1] * this.jacobian[1][0]) + (sHessian[2][2] * this.jacobian[2][0]);
        hj[2][1] = (sHessian[2][0] * this.jacobian[0][1]) + (sHessian[2][1] * this.jacobian[1][1]) + (sHessian[2][2] * this.jacobian[2][1]);
        hj[2][2] = (sHessian[2][0] * this.jacobian[0][2]) + (sHessian[2][2] * this.jacobian[2][2]);
        cHessian[0][0] = (this.jacobian[0][0] * hj[0][0]) + (this.jacobian[1][0] * hj[1][0]) + (this.jacobian[2][0] * hj[2][0]);
        cHessian[1][0] = (this.jacobian[0][1] * hj[0][0]) + (this.jacobian[1][1] * hj[1][0]) + (this.jacobian[2][1] * hj[2][0]);
        cHessian[2][0] = (this.jacobian[0][2] * hj[0][0]) + (this.jacobian[2][2] * hj[2][0]);
        cHessian[1][1] = (this.jacobian[0][1] * hj[0][1]) + (this.jacobian[1][1] * hj[1][1]) + (this.jacobian[2][1] * hj[2][1]);
        cHessian[2][1] = (this.jacobian[0][2] * hj[0][1]) + (this.jacobian[2][2] * hj[2][1]);
        cHessian[2][2] = (this.jacobian[0][2] * hj[0][2]) + (this.jacobian[2][2] * hj[2][2]);
        double[] dArr = cHessian[0];
        dArr[0] = dArr[0] + (sGradient[0] * this.rHessian[0][0]) + (sGradient[1] * this.thetaHessian[0][0]) + (sGradient[2] * this.phiHessian[0][0]);
        double[] dArr2 = cHessian[1];
        dArr2[0] = dArr2[0] + (sGradient[0] * this.rHessian[1][0]) + (sGradient[1] * this.thetaHessian[1][0]) + (sGradient[2] * this.phiHessian[1][0]);
        double[] dArr3 = cHessian[2];
        dArr3[0] = dArr3[0] + (sGradient[0] * this.rHessian[2][0]) + (sGradient[2] * this.phiHessian[2][0]);
        double[] dArr4 = cHessian[1];
        dArr4[1] = dArr4[1] + (sGradient[0] * this.rHessian[1][1]) + (sGradient[1] * this.thetaHessian[1][1]) + (sGradient[2] * this.phiHessian[1][1]);
        double[] dArr5 = cHessian[2];
        dArr5[1] = dArr5[1] + (sGradient[0] * this.rHessian[2][1]) + (sGradient[2] * this.phiHessian[2][1]);
        double[] dArr6 = cHessian[2];
        dArr6[2] = dArr6[2] + (sGradient[0] * this.rHessian[2][2]) + (sGradient[2] * this.phiHessian[2][2]);
        cHessian[0][1] = cHessian[1][0];
        cHessian[0][2] = cHessian[2][0];
        cHessian[1][2] = cHessian[2][1];
        return cHessian;
    }

    private void computeJacobian() {
        if (this.jacobian == null) {
            double x2 = this.f13010v.getX();
            double y2 = this.f13010v.getY();
            double z2 = this.f13010v.getZ();
            double rho2 = (x2 * x2) + (y2 * y2);
            double rho = FastMath.sqrt(rho2);
            double r2 = rho2 + (z2 * z2);
            this.jacobian = new double[3][3];
            this.jacobian[0][0] = x2 / this.f13011r;
            this.jacobian[0][1] = y2 / this.f13011r;
            this.jacobian[0][2] = z2 / this.f13011r;
            this.jacobian[1][0] = (-y2) / rho2;
            this.jacobian[1][1] = x2 / rho2;
            this.jacobian[2][0] = (x2 * z2) / (rho * r2);
            this.jacobian[2][1] = (y2 * z2) / (rho * r2);
            this.jacobian[2][2] = (-rho) / r2;
        }
    }

    private void computeHessians() {
        if (this.rHessian == null) {
            double x2 = this.f13010v.getX();
            double y2 = this.f13010v.getY();
            double z2 = this.f13010v.getZ();
            double x22 = x2 * x2;
            double y22 = y2 * y2;
            double z22 = z2 * z2;
            double rho2 = x22 + y22;
            double rho = FastMath.sqrt(rho2);
            double r2 = rho2 + z22;
            double xOr = x2 / this.f13011r;
            double yOr = y2 / this.f13011r;
            double zOr = z2 / this.f13011r;
            double xOrho2 = x2 / rho2;
            double yOrho2 = y2 / rho2;
            double xOr3 = xOr / r2;
            double yOr3 = yOr / r2;
            double zOr3 = zOr / r2;
            this.rHessian = new double[3][3];
            this.rHessian[0][0] = (y2 * yOr3) + (z2 * zOr3);
            this.rHessian[1][0] = (-x2) * yOr3;
            this.rHessian[2][0] = (-z2) * xOr3;
            this.rHessian[1][1] = (x2 * xOr3) + (z2 * zOr3);
            this.rHessian[2][1] = (-y2) * zOr3;
            this.rHessian[2][2] = (x2 * xOr3) + (y2 * yOr3);
            this.rHessian[0][1] = this.rHessian[1][0];
            this.rHessian[0][2] = this.rHessian[2][0];
            this.rHessian[1][2] = this.rHessian[2][1];
            this.thetaHessian = new double[2][2];
            this.thetaHessian[0][0] = 2.0d * xOrho2 * yOrho2;
            this.thetaHessian[1][0] = (yOrho2 * yOrho2) - (xOrho2 * xOrho2);
            this.thetaHessian[1][1] = (-2.0d) * xOrho2 * yOrho2;
            this.thetaHessian[0][1] = this.thetaHessian[1][0];
            double rhor2 = rho * r2;
            double rho2r2 = rho * rhor2;
            double rhor4 = rhor2 * r2;
            double rho3r4 = rhor4 * rho2;
            double r2P2rho2 = (3.0d * rho2) + z22;
            this.phiHessian = new double[3][3];
            this.phiHessian[0][0] = (z2 * (rho2r2 - (x22 * r2P2rho2))) / rho3r4;
            this.phiHessian[1][0] = ((((-x2) * y2) * z2) * r2P2rho2) / rho3r4;
            this.phiHessian[2][0] = (x2 * (rho2 - z22)) / rhor4;
            this.phiHessian[1][1] = (z2 * (rho2r2 - (y22 * r2P2rho2))) / rho3r4;
            this.phiHessian[2][1] = (y2 * (rho2 - z22)) / rhor4;
            this.phiHessian[2][2] = ((2.0d * rho) * zOr3) / this.f13011r;
            this.phiHessian[0][1] = this.phiHessian[1][0];
            this.phiHessian[0][2] = this.phiHessian[2][0];
            this.phiHessian[1][2] = this.phiHessian[2][1];
        }
    }

    private Object writeReplace() {
        return new DataTransferObject(this.f13010v.getX(), this.f13010v.getY(), this.f13010v.getZ());
    }

    /* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/geometry/euclidean/threed/SphericalCoordinates$DataTransferObject.class */
    private static class DataTransferObject implements Serializable {
        private static final long serialVersionUID = 20130206;

        /* renamed from: x, reason: collision with root package name */
        private final double f13012x;

        /* renamed from: y, reason: collision with root package name */
        private final double f13013y;

        /* renamed from: z, reason: collision with root package name */
        private final double f13014z;

        DataTransferObject(double x2, double y2, double z2) {
            this.f13012x = x2;
            this.f13013y = y2;
            this.f13014z = z2;
        }

        private Object readResolve() {
            return new SphericalCoordinates(new Vector3D(this.f13012x, this.f13013y, this.f13014z));
        }
    }
}
