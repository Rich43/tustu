package org.apache.commons.math3.filter;

import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.CholeskyDecomposition;
import org.apache.commons.math3.linear.MatrixDimensionMismatchException;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.NonSquareMatrixException;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.SingularMatrixException;
import org.apache.commons.math3.util.MathUtils;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/filter/KalmanFilter.class */
public class KalmanFilter {
    private final ProcessModel processModel;
    private final MeasurementModel measurementModel;
    private RealMatrix transitionMatrix;
    private RealMatrix transitionMatrixT;
    private RealMatrix controlMatrix;
    private RealMatrix measurementMatrix;
    private RealMatrix measurementMatrixT;
    private RealVector stateEstimation;
    private RealMatrix errorCovariance;

    public KalmanFilter(ProcessModel process, MeasurementModel measurement) throws MatrixDimensionMismatchException, NullArgumentException, DimensionMismatchException {
        MathUtils.checkNotNull(process);
        MathUtils.checkNotNull(measurement);
        this.processModel = process;
        this.measurementModel = measurement;
        this.transitionMatrix = this.processModel.getStateTransitionMatrix();
        MathUtils.checkNotNull(this.transitionMatrix);
        this.transitionMatrixT = this.transitionMatrix.transpose();
        if (this.processModel.getControlMatrix() == null) {
            this.controlMatrix = new Array2DRowRealMatrix();
        } else {
            this.controlMatrix = this.processModel.getControlMatrix();
        }
        this.measurementMatrix = this.measurementModel.getMeasurementMatrix();
        MathUtils.checkNotNull(this.measurementMatrix);
        this.measurementMatrixT = this.measurementMatrix.transpose();
        RealMatrix processNoise = this.processModel.getProcessNoise();
        MathUtils.checkNotNull(processNoise);
        RealMatrix measNoise = this.measurementModel.getMeasurementNoise();
        MathUtils.checkNotNull(measNoise);
        if (this.processModel.getInitialStateEstimate() == null) {
            this.stateEstimation = new ArrayRealVector(this.transitionMatrix.getColumnDimension());
        } else {
            this.stateEstimation = this.processModel.getInitialStateEstimate();
        }
        if (this.transitionMatrix.getColumnDimension() != this.stateEstimation.getDimension()) {
            throw new DimensionMismatchException(this.transitionMatrix.getColumnDimension(), this.stateEstimation.getDimension());
        }
        if (this.processModel.getInitialErrorCovariance() == null) {
            this.errorCovariance = processNoise.copy();
        } else {
            this.errorCovariance = this.processModel.getInitialErrorCovariance();
        }
        if (!this.transitionMatrix.isSquare()) {
            throw new NonSquareMatrixException(this.transitionMatrix.getRowDimension(), this.transitionMatrix.getColumnDimension());
        }
        if (this.controlMatrix != null && this.controlMatrix.getRowDimension() > 0 && this.controlMatrix.getColumnDimension() > 0 && this.controlMatrix.getRowDimension() != this.transitionMatrix.getRowDimension()) {
            throw new MatrixDimensionMismatchException(this.controlMatrix.getRowDimension(), this.controlMatrix.getColumnDimension(), this.transitionMatrix.getRowDimension(), this.controlMatrix.getColumnDimension());
        }
        MatrixUtils.checkAdditionCompatible(this.transitionMatrix, processNoise);
        if (this.measurementMatrix.getColumnDimension() != this.transitionMatrix.getRowDimension()) {
            throw new MatrixDimensionMismatchException(this.measurementMatrix.getRowDimension(), this.measurementMatrix.getColumnDimension(), this.measurementMatrix.getRowDimension(), this.transitionMatrix.getRowDimension());
        }
        if (measNoise.getRowDimension() != this.measurementMatrix.getRowDimension()) {
            throw new MatrixDimensionMismatchException(measNoise.getRowDimension(), measNoise.getColumnDimension(), this.measurementMatrix.getRowDimension(), measNoise.getColumnDimension());
        }
    }

    public int getStateDimension() {
        return this.stateEstimation.getDimension();
    }

    public int getMeasurementDimension() {
        return this.measurementMatrix.getRowDimension();
    }

    public double[] getStateEstimation() {
        return this.stateEstimation.toArray();
    }

    public RealVector getStateEstimationVector() {
        return this.stateEstimation.copy();
    }

    public double[][] getErrorCovariance() {
        return this.errorCovariance.getData();
    }

    public RealMatrix getErrorCovarianceMatrix() {
        return this.errorCovariance.copy();
    }

    public void predict() throws DimensionMismatchException {
        predict((RealVector) null);
    }

    public void predict(double[] u2) throws DimensionMismatchException {
        predict(new ArrayRealVector(u2, false));
    }

    public void predict(RealVector u2) throws DimensionMismatchException {
        if (u2 != null && u2.getDimension() != this.controlMatrix.getColumnDimension()) {
            throw new DimensionMismatchException(u2.getDimension(), this.controlMatrix.getColumnDimension());
        }
        this.stateEstimation = this.transitionMatrix.operate(this.stateEstimation);
        if (u2 != null) {
            this.stateEstimation = this.stateEstimation.add(this.controlMatrix.operate(u2));
        }
        this.errorCovariance = this.transitionMatrix.multiply(this.errorCovariance).multiply(this.transitionMatrixT).add(this.processModel.getProcessNoise());
    }

    public void correct(double[] z2) throws OutOfRangeException, MatrixDimensionMismatchException, NullArgumentException, DimensionMismatchException, SingularMatrixException {
        correct(new ArrayRealVector(z2, false));
    }

    public void correct(RealVector z2) throws OutOfRangeException, MatrixDimensionMismatchException, NullArgumentException, DimensionMismatchException, SingularMatrixException {
        MathUtils.checkNotNull(z2);
        if (z2.getDimension() != this.measurementMatrix.getRowDimension()) {
            throw new DimensionMismatchException(z2.getDimension(), this.measurementMatrix.getRowDimension());
        }
        RealMatrix s2 = this.measurementMatrix.multiply(this.errorCovariance).multiply(this.measurementMatrixT).add(this.measurementModel.getMeasurementNoise());
        RealVector innovation = z2.subtract(this.measurementMatrix.operate(this.stateEstimation));
        RealMatrix kalmanGain = new CholeskyDecomposition(s2).getSolver().solve(this.measurementMatrix.multiply(this.errorCovariance.transpose())).transpose();
        this.stateEstimation = this.stateEstimation.add(kalmanGain.operate(innovation));
        RealMatrix identity = MatrixUtils.createRealIdentityMatrix(kalmanGain.getRowDimension());
        this.errorCovariance = identity.subtract(kalmanGain.multiply(this.measurementMatrix)).multiply(this.errorCovariance);
    }
}
