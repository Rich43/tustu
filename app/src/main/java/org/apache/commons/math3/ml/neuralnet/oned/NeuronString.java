package org.apache.commons.math3.ml.neuralnet.oned;

import java.io.ObjectInputStream;
import java.io.Serializable;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.ml.neuralnet.FeatureInitializer;
import org.apache.commons.math3.ml.neuralnet.Network;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/ml/neuralnet/oned/NeuronString.class */
public class NeuronString implements Serializable {
    private static final long serialVersionUID = 1;
    private final Network network;
    private final int size;
    private final boolean wrap;
    private final long[] identifiers;

    NeuronString(boolean wrap, double[][] featuresList) {
        this.size = featuresList.length;
        if (this.size < 2) {
            throw new NumberIsTooSmallException(Integer.valueOf(this.size), 2, true);
        }
        this.wrap = wrap;
        int fLen = featuresList[0].length;
        this.network = new Network(0L, fLen);
        this.identifiers = new long[this.size];
        for (int i2 = 0; i2 < this.size; i2++) {
            this.identifiers[i2] = this.network.createNeuron(featuresList[i2]);
        }
        createLinks();
    }

    public NeuronString(int num, boolean wrap, FeatureInitializer[] featureInit) {
        if (num < 2) {
            throw new NumberIsTooSmallException(Integer.valueOf(num), 2, true);
        }
        this.size = num;
        this.wrap = wrap;
        this.identifiers = new long[num];
        int fLen = featureInit.length;
        this.network = new Network(0L, fLen);
        for (int i2 = 0; i2 < num; i2++) {
            double[] features = new double[fLen];
            for (int fIndex = 0; fIndex < fLen; fIndex++) {
                features[fIndex] = featureInit[fIndex].value();
            }
            this.identifiers[i2] = this.network.createNeuron(features);
        }
        createLinks();
    }

    public Network getNetwork() {
        return this.network;
    }

    public int getSize() {
        return this.size;
    }

    public double[] getFeatures(int i2) {
        if (i2 < 0 || i2 >= this.size) {
            throw new OutOfRangeException(Integer.valueOf(i2), 0, Integer.valueOf(this.size - 1));
        }
        return this.network.getNeuron(this.identifiers[i2]).getFeatures();
    }

    private void createLinks() {
        for (int i2 = 0; i2 < this.size - 1; i2++) {
            this.network.addLink(this.network.getNeuron(i2), this.network.getNeuron(i2 + 1));
        }
        for (int i3 = this.size - 1; i3 > 0; i3--) {
            this.network.addLink(this.network.getNeuron(i3), this.network.getNeuron(i3 - 1));
        }
        if (this.wrap) {
            this.network.addLink(this.network.getNeuron(0L), this.network.getNeuron(this.size - 1));
            this.network.addLink(this.network.getNeuron(this.size - 1), this.network.getNeuron(0L));
        }
    }

    private void readObject(ObjectInputStream in) {
        throw new IllegalStateException();
    }

    /* JADX WARN: Type inference failed for: r0v2, types: [double[], double[][]] */
    private Object writeReplace() {
        ?? r0 = new double[this.size];
        for (int i2 = 0; i2 < this.size; i2++) {
            r0[i2] = getFeatures(i2);
        }
        return new SerializationProxy(this.wrap, r0);
    }

    /* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/ml/neuralnet/oned/NeuronString$SerializationProxy.class */
    private static class SerializationProxy implements Serializable {
        private static final long serialVersionUID = 20130226;
        private final boolean wrap;
        private final double[][] featuresList;

        SerializationProxy(boolean wrap, double[][] featuresList) {
            this.wrap = wrap;
            this.featuresList = featuresList;
        }

        private Object readResolve() {
            return new NeuronString(this.wrap, this.featuresList);
        }
    }
}
