package java.util.stream;

import java.util.Spliterator;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.AbstractShortCircuitTask;

/* loaded from: rt.jar:java/util/stream/AbstractShortCircuitTask.class */
abstract class AbstractShortCircuitTask<P_IN, P_OUT, R, K extends AbstractShortCircuitTask<P_IN, P_OUT, R, K>> extends AbstractTask<P_IN, P_OUT, R, K> {
    protected final AtomicReference<R> sharedResult;
    protected volatile boolean canceled;

    protected abstract R getEmptyResult();

    protected AbstractShortCircuitTask(PipelineHelper<P_OUT> pipelineHelper, Spliterator<P_IN> spliterator) {
        super(pipelineHelper, spliterator);
        this.sharedResult = new AtomicReference<>(null);
    }

    protected AbstractShortCircuitTask(K k2, Spliterator<P_IN> spliterator) {
        super(k2, spliterator);
        this.sharedResult = k2.sharedResult;
    }

    @Override // java.util.stream.AbstractTask, java.util.concurrent.CountedCompleter
    public void compute() {
        R emptyResult;
        Spliterator<P_IN> spliteratorTrySplit;
        AbstractShortCircuitTask<P_IN, P_OUT, R, K> abstractShortCircuitTask;
        Spliterator<P_IN> spliterator = this.spliterator;
        long jEstimateSize = spliterator.estimateSize();
        long targetSize = getTargetSize(jEstimateSize);
        boolean z2 = false;
        AbstractShortCircuitTask<P_IN, P_OUT, R, K> abstractShortCircuitTask2 = this;
        AtomicReference<R> atomicReference = this.sharedResult;
        while (true) {
            R r2 = atomicReference.get();
            emptyResult = r2;
            if (r2 != null) {
                break;
            }
            if (abstractShortCircuitTask2.taskCanceled()) {
                emptyResult = abstractShortCircuitTask2.getEmptyResult();
                break;
            }
            if (jEstimateSize <= targetSize || (spliteratorTrySplit = spliterator.trySplit()) == null) {
                break;
            }
            K kMakeChild = abstractShortCircuitTask2.makeChild(spliteratorTrySplit);
            abstractShortCircuitTask2.leftChild = kMakeChild;
            K kMakeChild2 = abstractShortCircuitTask2.makeChild(spliterator);
            abstractShortCircuitTask2.rightChild = kMakeChild2;
            abstractShortCircuitTask2.setPendingCount(1);
            if (z2) {
                z2 = false;
                spliterator = spliteratorTrySplit;
                abstractShortCircuitTask2 = kMakeChild;
                abstractShortCircuitTask = kMakeChild2;
            } else {
                z2 = true;
                abstractShortCircuitTask2 = kMakeChild2;
                abstractShortCircuitTask = kMakeChild;
            }
            abstractShortCircuitTask.fork();
            jEstimateSize = spliterator.estimateSize();
        }
        emptyResult = abstractShortCircuitTask2.doLeaf();
        abstractShortCircuitTask2.setLocalResult(emptyResult);
        abstractShortCircuitTask2.tryComplete();
    }

    protected void shortCircuit(R r2) {
        if (r2 != null) {
            this.sharedResult.compareAndSet(null, r2);
        }
    }

    @Override // java.util.stream.AbstractTask
    protected void setLocalResult(R r2) {
        if (isRoot()) {
            if (r2 != null) {
                this.sharedResult.compareAndSet(null, r2);
                return;
            }
            return;
        }
        super.setLocalResult(r2);
    }

    @Override // java.util.stream.AbstractTask, java.util.concurrent.CountedCompleter, java.util.concurrent.ForkJoinTask
    public R getRawResult() {
        return getLocalResult();
    }

    @Override // java.util.stream.AbstractTask
    public R getLocalResult() {
        if (isRoot()) {
            R r2 = this.sharedResult.get();
            return r2 == null ? getEmptyResult() : r2;
        }
        return (R) super.getLocalResult();
    }

    protected void cancel() {
        this.canceled = true;
    }

    protected boolean taskCanceled() {
        boolean z2 = this.canceled;
        if (!z2) {
            K parent = getParent();
            while (true) {
                AbstractShortCircuitTask abstractShortCircuitTask = parent;
                if (z2 || abstractShortCircuitTask == null) {
                    break;
                }
                z2 = abstractShortCircuitTask.canceled;
                parent = (K) abstractShortCircuitTask.getParent();
            }
        }
        return z2;
    }

    protected void cancelLaterNodes() {
        AbstractShortCircuitTask<P_IN, P_OUT, R, K> abstractShortCircuitTask = this;
        for (AbstractShortCircuitTask<P_IN, P_OUT, R, K> parent = getParent(); parent != null; parent = parent.getParent()) {
            if (parent.leftChild == abstractShortCircuitTask) {
                AbstractShortCircuitTask abstractShortCircuitTask2 = (AbstractShortCircuitTask) parent.rightChild;
                if (!abstractShortCircuitTask2.canceled) {
                    abstractShortCircuitTask2.cancel();
                }
            }
            abstractShortCircuitTask = parent;
        }
    }
}
