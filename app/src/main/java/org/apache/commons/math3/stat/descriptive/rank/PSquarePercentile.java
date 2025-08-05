package org.apache.commons.math3.stat.descriptive.rank;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.analysis.interpolation.LinearInterpolator;
import org.apache.commons.math3.analysis.interpolation.NevilleInterpolator;
import org.apache.commons.math3.analysis.interpolation.UnivariateInterpolator;
import org.apache.commons.math3.exception.InsufficientDataException;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.stat.descriptive.AbstractStorelessUnivariateStatistic;
import org.apache.commons.math3.stat.descriptive.StorelessUnivariateStatistic;
import org.apache.commons.math3.stat.descriptive.UnivariateStatistic;
import org.apache.commons.math3.util.MathArrays;
import org.apache.commons.math3.util.MathUtils;
import org.apache.commons.math3.util.Precision;

/*  JADX ERROR: NullPointerException in pass: ClassModifier
    java.lang.NullPointerException
    */
/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/stat/descriptive/rank/PSquarePercentile.class */
public class PSquarePercentile extends AbstractStorelessUnivariateStatistic implements StorelessUnivariateStatistic, Serializable {
    private static final int PSQUARE_CONSTANT = 5;
    private static final double DEFAULT_QUANTILE_DESIRED = 50.0d;
    private static final long serialVersionUID = 2283912083175715479L;
    private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("00.00");
    private final List<Double> initialFive;
    private final double quantile;
    private transient double lastObservation;
    private PSquareMarkers markers;
    private double pValue;
    private long countOfObservations;

    /* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/stat/descriptive/rank/PSquarePercentile$PSquareMarkers.class */
    protected interface PSquareMarkers extends Cloneable {
        double getPercentileValue();

        Object clone();

        double height(int i2);

        double processDataPoint(double d2);

        double estimate(int i2);
    }

    @Override // org.apache.commons.math3.stat.descriptive.AbstractStorelessUnivariateStatistic, org.apache.commons.math3.stat.descriptive.AbstractUnivariateStatistic, org.apache.commons.math3.stat.descriptive.UnivariateStatistic, org.apache.commons.math3.stat.descriptive.StorelessUnivariateStatistic
    public /* bridge */ /* synthetic */ UnivariateStatistic copy() {
        return copy();
    }

    static {
    }

    public PSquarePercentile(double p2) {
        this.initialFive = new FixedCapacityList(5);
        this.markers = null;
        this.pValue = Double.NaN;
        if (p2 > 100.0d || p2 < 0.0d) {
            throw new OutOfRangeException(LocalizedFormats.OUT_OF_RANGE, Double.valueOf(p2), 0, 100);
        }
        this.quantile = p2 / 100.0d;
    }

    PSquarePercentile() {
        this(DEFAULT_QUANTILE_DESIRED);
    }

    @Override // org.apache.commons.math3.stat.descriptive.AbstractStorelessUnivariateStatistic
    public int hashCode() {
        double result = getResult();
        double result2 = Double.isNaN(result) ? 37.0d : result;
        double markersHash = this.markers == null ? 0.0d : this.markers.hashCode();
        double[] toHash = {result2, this.quantile, markersHash, this.countOfObservations};
        return Arrays.hashCode(toHash);
    }

    @Override // org.apache.commons.math3.stat.descriptive.AbstractStorelessUnivariateStatistic
    public boolean equals(Object o2) {
        boolean result = false;
        if (this == o2) {
            result = true;
        } else if (o2 != null && (o2 instanceof PSquarePercentile)) {
            PSquarePercentile that = (PSquarePercentile) o2;
            boolean isNotNull = (this.markers == null || that.markers == null) ? false : true;
            boolean isNull = this.markers == null && that.markers == null;
            result = (isNotNull ? this.markers.equals(that.markers) : isNull) && getN() == that.getN();
        }
        return result;
    }

    @Override // org.apache.commons.math3.stat.descriptive.AbstractStorelessUnivariateStatistic, org.apache.commons.math3.stat.descriptive.StorelessUnivariateStatistic
    public void increment(double observation) {
        this.countOfObservations++;
        this.lastObservation = observation;
        if (this.markers == null) {
            if (this.initialFive.add(Double.valueOf(observation))) {
                Collections.sort(this.initialFive);
                this.pValue = this.initialFive.get((int) (this.quantile * (this.initialFive.size() - 1))).doubleValue();
                return;
            }
            this.markers = newMarkers(this.initialFive, this.quantile);
        }
        this.pValue = this.markers.processDataPoint(observation);
    }

    public String toString() {
        if (this.markers == null) {
            return String.format("obs=%s pValue=%s", DECIMAL_FORMAT.format(this.lastObservation), DECIMAL_FORMAT.format(this.pValue));
        }
        return String.format("obs=%s markers=%s", DECIMAL_FORMAT.format(this.lastObservation), this.markers.toString());
    }

    @Override // org.apache.commons.math3.stat.descriptive.StorelessUnivariateStatistic
    public long getN() {
        return this.countOfObservations;
    }

    @Override // org.apache.commons.math3.stat.descriptive.AbstractStorelessUnivariateStatistic, org.apache.commons.math3.stat.descriptive.AbstractUnivariateStatistic, org.apache.commons.math3.stat.descriptive.UnivariateStatistic, org.apache.commons.math3.stat.descriptive.StorelessUnivariateStatistic
    public StorelessUnivariateStatistic copy() {
        PSquarePercentile copy = new PSquarePercentile(100.0d * this.quantile);
        if (this.markers != null) {
            copy.markers = (PSquareMarkers) this.markers.clone();
        }
        copy.countOfObservations = this.countOfObservations;
        copy.pValue = this.pValue;
        copy.initialFive.clear();
        copy.initialFive.addAll(this.initialFive);
        return copy;
    }

    public double quantile() {
        return this.quantile;
    }

    @Override // org.apache.commons.math3.stat.descriptive.AbstractStorelessUnivariateStatistic, org.apache.commons.math3.stat.descriptive.StorelessUnivariateStatistic
    public void clear() {
        this.markers = null;
        this.initialFive.clear();
        this.countOfObservations = 0L;
        this.pValue = Double.NaN;
    }

    @Override // org.apache.commons.math3.stat.descriptive.AbstractStorelessUnivariateStatistic, org.apache.commons.math3.stat.descriptive.StorelessUnivariateStatistic
    public double getResult() {
        if (Double.compare(this.quantile, 1.0d) == 0) {
            this.pValue = maximum();
        } else if (Double.compare(this.quantile, 0.0d) == 0) {
            this.pValue = minimum();
        }
        return this.pValue;
    }

    private double maximum() {
        double val = Double.NaN;
        if (this.markers != null) {
            val = this.markers.height(5);
        } else if (!this.initialFive.isEmpty()) {
            val = this.initialFive.get(this.initialFive.size() - 1).doubleValue();
        }
        return val;
    }

    private double minimum() {
        double val = Double.NaN;
        if (this.markers != null) {
            val = this.markers.height(1);
        } else if (!this.initialFive.isEmpty()) {
            val = this.initialFive.get(0).doubleValue();
        }
        return val;
    }

    /* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/stat/descriptive/rank/PSquarePercentile$Markers.class */
    private static class Markers implements PSquareMarkers, Serializable {
        private static final long serialVersionUID = 1;
        private static final int LOW = 2;
        private static final int HIGH = 4;
        private final Marker[] markerArray;

        /* renamed from: k, reason: collision with root package name */
        private transient int f13103k;

        /* synthetic */ Markers(List x0, double x1, AnonymousClass1 x2) {
            this(x0, x1);
        }

        private Markers(Marker[] theMarkerArray) throws NullArgumentException {
            this.f13103k = -1;
            MathUtils.checkNotNull(theMarkerArray);
            this.markerArray = theMarkerArray;
            for (int i2 = 1; i2 < 5; i2++) {
                this.markerArray[i2].previous(this.markerArray[i2 - 1]).next(this.markerArray[i2 + 1]).index(i2);
            }
            this.markerArray[0].previous(this.markerArray[0]).next(this.markerArray[1]).index(0);
            this.markerArray[5].previous(this.markerArray[4]).next(this.markerArray[5]).index(5);
        }

        private Markers(List<Double> initialFive, double p2) {
            this(createMarkerArray(initialFive, p2));
        }

        private static Marker[] createMarkerArray(List<Double> initialFive, double p2) {
            int countObserved = initialFive == null ? -1 : initialFive.size();
            if (countObserved < 5) {
                throw new InsufficientDataException(LocalizedFormats.INSUFFICIENT_OBSERVED_POINTS_IN_SAMPLE, Integer.valueOf(countObserved), 5);
            }
            Collections.sort(initialFive);
            return new Marker[]{new Marker(), new Marker(initialFive.get(0).doubleValue(), 1.0d, 0.0d, 1.0d), new Marker(initialFive.get(1).doubleValue(), 1.0d + (2.0d * p2), p2 / 2.0d, 2.0d), new Marker(initialFive.get(2).doubleValue(), 1.0d + (4.0d * p2), p2, 3.0d), new Marker(initialFive.get(3).doubleValue(), 3.0d + (2.0d * p2), (1.0d + p2) / 2.0d, 4.0d), new Marker(initialFive.get(4).doubleValue(), 5.0d, 1.0d, 5.0d)};
        }

        public int hashCode() {
            return Arrays.deepHashCode(this.markerArray);
        }

        public boolean equals(Object o2) {
            boolean result = false;
            if (this == o2) {
                result = true;
            } else if (o2 != null && (o2 instanceof Markers)) {
                Markers that = (Markers) o2;
                result = Arrays.deepEquals(this.markerArray, that.markerArray);
            }
            return result;
        }

        @Override // org.apache.commons.math3.stat.descriptive.rank.PSquarePercentile.PSquareMarkers
        public double processDataPoint(double inputDataPoint) {
            int kthCell = findCellAndUpdateMinMax(inputDataPoint);
            incrementPositions(1, kthCell + 1, 5);
            updateDesiredPositions();
            adjustHeightsOfMarkers();
            return getPercentileValue();
        }

        @Override // org.apache.commons.math3.stat.descriptive.rank.PSquarePercentile.PSquareMarkers
        public double getPercentileValue() {
            return height(3);
        }

        /* JADX WARN: Failed to check method for inline after forced processorg.apache.commons.math3.stat.descriptive.rank.PSquarePercentile.Marker.access$502(org.apache.commons.math3.stat.descriptive.rank.PSquarePercentile$Marker, double):double */
        private int findCellAndUpdateMinMax(double observation) {
            this.f13103k = -1;
            if (observation < height(1)) {
                Marker.access$502(this.markerArray[1], observation);
                this.f13103k = 1;
            } else if (observation < height(2)) {
                this.f13103k = 1;
            } else if (observation < height(3)) {
                this.f13103k = 2;
            } else if (observation < height(4)) {
                this.f13103k = 3;
            } else if (observation <= height(5)) {
                this.f13103k = 4;
            } else {
                Marker.access$502(this.markerArray[5], observation);
                this.f13103k = 4;
            }
            return this.f13103k;
        }

        private void adjustHeightsOfMarkers() {
            for (int i2 = 2; i2 <= 4; i2++) {
                estimate(i2);
            }
        }

        @Override // org.apache.commons.math3.stat.descriptive.rank.PSquarePercentile.PSquareMarkers
        public double estimate(int index) {
            if (index < 2 || index > 4) {
                throw new OutOfRangeException(Integer.valueOf(index), 2, 4);
            }
            return this.markerArray[index].estimate();
        }

        private void incrementPositions(int d2, int startIndex, int endIndex) {
            for (int i2 = startIndex; i2 <= endIndex; i2++) {
                this.markerArray[i2].incrementPosition(d2);
            }
        }

        private void updateDesiredPositions() {
            for (int i2 = 1; i2 < this.markerArray.length; i2++) {
                this.markerArray[i2].updateDesiredPosition();
            }
        }

        private void readObject(ObjectInputStream anInputStream) throws IOException, ClassNotFoundException {
            anInputStream.defaultReadObject();
            for (int i2 = 1; i2 < 5; i2++) {
                this.markerArray[i2].previous(this.markerArray[i2 - 1]).next(this.markerArray[i2 + 1]).index(i2);
            }
            this.markerArray[0].previous(this.markerArray[0]).next(this.markerArray[1]).index(0);
            this.markerArray[5].previous(this.markerArray[4]).next(this.markerArray[5]).index(5);
        }

        @Override // org.apache.commons.math3.stat.descriptive.rank.PSquarePercentile.PSquareMarkers
        public double height(int markerIndex) {
            if (markerIndex >= this.markerArray.length || markerIndex <= 0) {
                throw new OutOfRangeException(Integer.valueOf(markerIndex), 1, Integer.valueOf(this.markerArray.length));
            }
            return this.markerArray[markerIndex].markerHeight;
        }

        @Override // org.apache.commons.math3.stat.descriptive.rank.PSquarePercentile.PSquareMarkers
        public Object clone() {
            return new Markers(new Marker[]{new Marker(), (Marker) this.markerArray[1].clone(), (Marker) this.markerArray[2].clone(), (Marker) this.markerArray[3].clone(), (Marker) this.markerArray[4].clone(), (Marker) this.markerArray[5].clone()});
        }

        public String toString() {
            return String.format("m1=[%s],m2=[%s],m3=[%s],m4=[%s],m5=[%s]", this.markerArray[1].toString(), this.markerArray[2].toString(), this.markerArray[3].toString(), this.markerArray[4].toString(), this.markerArray[5].toString());
        }
    }

    /* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/stat/descriptive/rank/PSquarePercentile$Marker.class */
    private static class Marker implements Serializable, Cloneable {
        private static final long serialVersionUID = -3575879478288538431L;
        private int index;
        private double intMarkerPosition;
        private double desiredMarkerPosition;
        private double markerHeight;
        private double desiredMarkerIncrement;
        private transient Marker next;
        private transient Marker previous;
        private final UnivariateInterpolator nonLinear;
        private transient UnivariateInterpolator linear;

        /*  JADX ERROR: Failed to decode insn: 0x0002: MOVE_MULTI
            java.lang.ArrayIndexOutOfBoundsException: arraycopy: source index -1 out of bounds for object array[6]
            	at java.base/java.lang.System.arraycopy(Native Method)
            	at jadx.plugins.input.java.data.code.StackState.insert(StackState.java:52)
            	at jadx.plugins.input.java.data.code.CodeDecodeState.insert(CodeDecodeState.java:137)
            	at jadx.plugins.input.java.data.code.JavaInsnsRegister.dup2x1(JavaInsnsRegister.java:313)
            	at jadx.plugins.input.java.data.code.JavaInsnData.decode(JavaInsnData.java:46)
            	at jadx.core.dex.instructions.InsnDecoder.lambda$process$0(InsnDecoder.java:50)
            	at jadx.plugins.input.java.data.code.JavaCodeReader.visitInstructions(JavaCodeReader.java:85)
            	at jadx.core.dex.instructions.InsnDecoder.process(InsnDecoder.java:46)
            	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:158)
            	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:458)
            	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:464)
            	at jadx.core.ProcessClass.process(ProcessClass.java:69)
            	at jadx.core.ProcessClass.generateCode(ProcessClass.java:117)
            	at jadx.core.dex.nodes.ClassNode.generateClassCode(ClassNode.java:401)
            	at jadx.core.dex.nodes.ClassNode.decompile(ClassNode.java:389)
            	at jadx.core.dex.nodes.ClassNode.getCode(ClassNode.java:339)
            */
        static /* synthetic */ double access$502(org.apache.commons.math3.stat.descriptive.rank.PSquarePercentile.Marker r6, double r7) {
            /*
                r0 = r6
                r1 = r7
                // decode failed: arraycopy: source index -1 out of bounds for object array[6]
                r0.markerHeight = r1
                return r-1
            */
            throw new UnsupportedOperationException("Method not decompiled: org.apache.commons.math3.stat.descriptive.rank.PSquarePercentile.Marker.access$502(org.apache.commons.math3.stat.descriptive.rank.PSquarePercentile$Marker, double):double");
        }

        private Marker() {
            this.nonLinear = new NevilleInterpolator();
            this.linear = new LinearInterpolator();
            this.previous = this;
            this.next = this;
        }

        private Marker(double heightOfMarker, double makerPositionDesired, double markerPositionIncrement, double markerPositionNumber) {
            this();
            this.markerHeight = heightOfMarker;
            this.desiredMarkerPosition = makerPositionDesired;
            this.desiredMarkerIncrement = markerPositionIncrement;
            this.intMarkerPosition = markerPositionNumber;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public Marker previous(Marker previousMarker) throws NullArgumentException {
            MathUtils.checkNotNull(previousMarker);
            this.previous = previousMarker;
            return this;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public Marker next(Marker nextMarker) throws NullArgumentException {
            MathUtils.checkNotNull(nextMarker);
            this.next = nextMarker;
            return this;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public Marker index(int indexOfMarker) {
            this.index = indexOfMarker;
            return this;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void updateDesiredPosition() {
            this.desiredMarkerPosition += this.desiredMarkerIncrement;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void incrementPosition(int d2) {
            this.intMarkerPosition += d2;
        }

        private double difference() {
            return this.desiredMarkerPosition - this.intMarkerPosition;
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* JADX WARN: Type inference failed for: r1v30, types: [double[], double[][]] */
        public double estimate() throws MathIllegalArgumentException {
            double di = difference();
            boolean isNextHigher = this.next.intMarkerPosition - this.intMarkerPosition > 1.0d;
            boolean isPreviousLower = this.previous.intMarkerPosition - this.intMarkerPosition < -1.0d;
            if ((di >= 1.0d && isNextHigher) || (di <= -1.0d && isPreviousLower)) {
                int d2 = di >= 0.0d ? 1 : -1;
                double[] xval = {this.previous.intMarkerPosition, this.intMarkerPosition, this.next.intMarkerPosition};
                double[] yval = {this.previous.markerHeight, this.markerHeight, this.next.markerHeight};
                double xD = this.intMarkerPosition + d2;
                UnivariateFunction univariateFunction = this.nonLinear.interpolate(xval, yval);
                this.markerHeight = univariateFunction.value(xD);
                if (isEstimateBad(yval, this.markerHeight)) {
                    int delta = xD - xval[1] > 0.0d ? 1 : -1;
                    double[] xBad = {xval[1], xval[1 + delta]};
                    double[] yBad = {yval[1], yval[1 + delta]};
                    MathArrays.sortInPlace(xBad, new double[]{yBad});
                    UnivariateFunction univariateFunction2 = this.linear.interpolate(xBad, yBad);
                    this.markerHeight = univariateFunction2.value(xD);
                }
                incrementPosition(d2);
            }
            return this.markerHeight;
        }

        private boolean isEstimateBad(double[] y2, double yD) {
            return yD <= y2[0] || yD >= y2[2];
        }

        public boolean equals(Object o2) {
            boolean result = false;
            if (this == o2) {
                result = true;
            } else if (o2 != null && (o2 instanceof Marker)) {
                Marker that = (Marker) o2;
                result = (((((Double.compare(this.markerHeight, that.markerHeight) == 0) && Double.compare(this.intMarkerPosition, that.intMarkerPosition) == 0) && Double.compare(this.desiredMarkerPosition, that.desiredMarkerPosition) == 0) && Double.compare(this.desiredMarkerIncrement, that.desiredMarkerIncrement) == 0) && this.next.index == that.next.index) && this.previous.index == that.previous.index;
            }
            return result;
        }

        public int hashCode() {
            return Arrays.hashCode(new double[]{this.markerHeight, this.intMarkerPosition, this.desiredMarkerIncrement, this.desiredMarkerPosition, this.previous.index, this.next.index});
        }

        private void readObject(ObjectInputStream anInstream) throws IOException, ClassNotFoundException {
            anInstream.defaultReadObject();
            this.next = this;
            this.previous = this;
            this.linear = new LinearInterpolator();
        }

        public Object clone() {
            return new Marker(this.markerHeight, this.desiredMarkerPosition, this.desiredMarkerIncrement, this.intMarkerPosition);
        }

        public String toString() {
            return String.format("index=%.0f,n=%.0f,np=%.2f,q=%.2f,dn=%.2f,prev=%d,next=%d", Double.valueOf(this.index), Double.valueOf(Precision.round(this.intMarkerPosition, 0)), Double.valueOf(Precision.round(this.desiredMarkerPosition, 2)), Double.valueOf(Precision.round(this.markerHeight, 2)), Double.valueOf(Precision.round(this.desiredMarkerIncrement, 2)), Integer.valueOf(this.previous.index), Integer.valueOf(this.next.index));
        }
    }

    /* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/stat/descriptive/rank/PSquarePercentile$FixedCapacityList.class */
    private static class FixedCapacityList<E> extends ArrayList<E> implements Serializable {
        private static final long serialVersionUID = 2283952083075725479L;
        private final int capacity;

        FixedCapacityList(int fixedCapacity) {
            super(fixedCapacity);
            this.capacity = fixedCapacity;
        }

        @Override // java.util.ArrayList, java.util.AbstractList, java.util.AbstractCollection, java.util.Collection, java.util.List
        public boolean add(E e2) {
            if (size() < this.capacity) {
                return super.add(e2);
            }
            return false;
        }

        @Override // java.util.ArrayList, java.util.AbstractCollection, java.util.Collection
        public boolean addAll(Collection<? extends E> collection) {
            boolean isCollectionLess = collection != null && collection.size() + size() <= this.capacity;
            if (isCollectionLess) {
                return super.addAll(collection);
            }
            return false;
        }
    }

    public static PSquareMarkers newMarkers(List<Double> initialFive, double p2) {
        return new Markers(initialFive, p2, null);
    }
}
