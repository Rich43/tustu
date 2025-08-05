package org.apache.commons.math3.ml.neuralnet.sofm;

import java.util.Iterator;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.ml.neuralnet.Network;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/ml/neuralnet/sofm/KohonenTrainingTask.class */
public class KohonenTrainingTask implements Runnable {

    /* renamed from: net, reason: collision with root package name */
    private final Network f13042net;
    private final Iterator<double[]> featuresIterator;
    private final KohonenUpdateAction updateAction;

    public KohonenTrainingTask(Network net2, Iterator<double[]> featuresIterator, KohonenUpdateAction updateAction) {
        this.f13042net = net2;
        this.featuresIterator = featuresIterator;
        this.updateAction = updateAction;
    }

    @Override // java.lang.Runnable
    public void run() throws DimensionMismatchException {
        while (this.featuresIterator.hasNext()) {
            this.updateAction.update(this.f13042net, this.featuresIterator.next());
        }
    }
}
