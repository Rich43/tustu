package org.apache.commons.math3.ml.neuralnet.sofm;

import java.util.Collection;
import java.util.HashSet;
import java.util.concurrent.atomic.AtomicLong;
import org.apache.commons.math3.analysis.function.Gaussian;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.ml.distance.DistanceMeasure;
import org.apache.commons.math3.ml.neuralnet.MapUtils;
import org.apache.commons.math3.ml.neuralnet.Network;
import org.apache.commons.math3.ml.neuralnet.Neuron;
import org.apache.commons.math3.ml.neuralnet.UpdateAction;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/ml/neuralnet/sofm/KohonenUpdateAction.class */
public class KohonenUpdateAction implements UpdateAction {
    private final DistanceMeasure distance;
    private final LearningFactorFunction learningFactor;
    private final NeighbourhoodSizeFunction neighbourhoodSize;
    private final AtomicLong numberOfCalls = new AtomicLong(0);

    public KohonenUpdateAction(DistanceMeasure distance, LearningFactorFunction learningFactor, NeighbourhoodSizeFunction neighbourhoodSize) {
        this.distance = distance;
        this.learningFactor = learningFactor;
        this.neighbourhoodSize = neighbourhoodSize;
    }

    @Override // org.apache.commons.math3.ml.neuralnet.UpdateAction
    public void update(Network net2, double[] features) throws DimensionMismatchException {
        long numCalls = this.numberOfCalls.incrementAndGet() - 1;
        double currentLearning = this.learningFactor.value(numCalls);
        Neuron best = findAndUpdateBestNeuron(net2, features, currentLearning);
        int currentNeighbourhood = this.neighbourhoodSize.value(numCalls);
        Gaussian neighbourhoodDecay = new Gaussian(currentLearning, 0.0d, currentNeighbourhood);
        if (currentNeighbourhood > 0) {
            Collection<Neuron> neighbours = new HashSet<>();
            neighbours.add(best);
            HashSet<Neuron> exclude = new HashSet<>();
            exclude.add(best);
            int radius = 1;
            do {
                neighbours = net2.getNeighbours(neighbours, exclude);
                for (Neuron n2 : neighbours) {
                    updateNeighbouringNeuron(n2, features, neighbourhoodDecay.value(radius));
                }
                exclude.addAll(neighbours);
                radius++;
            } while (radius <= currentNeighbourhood);
        }
    }

    public long getNumberOfCalls() {
        return this.numberOfCalls.get();
    }

    private boolean attemptNeuronUpdate(Neuron n2, double[] features, double learningRate) {
        double[] expect = n2.getFeatures();
        double[] update = computeFeatures(expect, features, learningRate);
        return n2.compareAndSetFeatures(expect, update);
    }

    private void updateNeighbouringNeuron(Neuron n2, double[] features, double learningRate) {
        while (!attemptNeuronUpdate(n2, features, learningRate)) {
        }
    }

    private Neuron findAndUpdateBestNeuron(Network net2, double[] features, double learningRate) throws DimensionMismatchException {
        Neuron best;
        do {
            best = MapUtils.findBest(features, net2, this.distance);
        } while (!attemptNeuronUpdate(best, features, learningRate));
        return best;
    }

    private double[] computeFeatures(double[] current, double[] sample, double learningRate) {
        ArrayRealVector c2 = new ArrayRealVector(current, false);
        ArrayRealVector s2 = new ArrayRealVector(sample, false);
        return s2.subtract((RealVector) c2).mapMultiplyToSelf(learningRate).add(c2).toArray();
    }
}
