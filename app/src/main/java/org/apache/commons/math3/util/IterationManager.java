package org.apache.commons.math3.util;

import java.util.Collection;
import java.util.concurrent.CopyOnWriteArrayList;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.util.Incrementor;
import org.apache.commons.math3.util.IntegerSequence;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/util/IterationManager.class */
public class IterationManager {
    private IntegerSequence.Incrementor iterations;
    private final Collection<IterationListener> listeners;

    public IterationManager(int maxIterations) {
        this.iterations = IntegerSequence.Incrementor.create().withMaximalCount(maxIterations);
        this.listeners = new CopyOnWriteArrayList();
    }

    @Deprecated
    public IterationManager(int maxIterations, final Incrementor.MaxCountExceededCallback callBack) {
        this(maxIterations, new IntegerSequence.Incrementor.MaxCountExceededCallback() { // from class: org.apache.commons.math3.util.IterationManager.1
            @Override // org.apache.commons.math3.util.IntegerSequence.Incrementor.MaxCountExceededCallback
            public void trigger(int maximalCount) throws MaxCountExceededException {
                callBack.trigger(maximalCount);
            }
        });
    }

    public IterationManager(int maxIterations, IntegerSequence.Incrementor.MaxCountExceededCallback callBack) {
        this.iterations = IntegerSequence.Incrementor.create().withMaximalCount(maxIterations).withCallback(callBack);
        this.listeners = new CopyOnWriteArrayList();
    }

    public void addIterationListener(IterationListener listener) {
        this.listeners.add(listener);
    }

    public void fireInitializationEvent(IterationEvent e2) {
        for (IterationListener l2 : this.listeners) {
            l2.initializationPerformed(e2);
        }
    }

    public void fireIterationPerformedEvent(IterationEvent e2) {
        for (IterationListener l2 : this.listeners) {
            l2.iterationPerformed(e2);
        }
    }

    public void fireIterationStartedEvent(IterationEvent e2) {
        for (IterationListener l2 : this.listeners) {
            l2.iterationStarted(e2);
        }
    }

    public void fireTerminationEvent(IterationEvent e2) {
        for (IterationListener l2 : this.listeners) {
            l2.terminationPerformed(e2);
        }
    }

    public int getIterations() {
        return this.iterations.getCount();
    }

    public int getMaxIterations() {
        return this.iterations.getMaximalCount();
    }

    public void incrementIterationCount() throws MaxCountExceededException {
        this.iterations.increment();
    }

    public void removeIterationListener(IterationListener listener) {
        this.listeners.remove(listener);
    }

    public void resetIterationCount() {
        this.iterations = this.iterations.withStart(0);
    }
}
