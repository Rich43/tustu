package java.util.stream;

import java.util.Spliterator;
import java.util.concurrent.CountedCompleter;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinWorkerThread;
import java.util.stream.AbstractTask;

/* loaded from: rt.jar:java/util/stream/AbstractTask.class */
abstract class AbstractTask<P_IN, P_OUT, R, K extends AbstractTask<P_IN, P_OUT, R, K>> extends CountedCompleter<R> {
    private static final int LEAF_TARGET = ForkJoinPool.getCommonPoolParallelism() << 2;
    protected final PipelineHelper<P_OUT> helper;
    protected Spliterator<P_IN> spliterator;
    protected long targetSize;
    protected K leftChild;
    protected K rightChild;
    private R localResult;

    protected abstract K makeChild(Spliterator<P_IN> spliterator);

    protected abstract R doLeaf();

    /*  JADX ERROR: Failed to decode insn: 0x0014: MOVE_MULTI
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
        	at jadx.core.ProcessClass.process(ProcessClass.java:69)
        	at jadx.core.ProcessClass.generateCode(ProcessClass.java:117)
        	at jadx.core.dex.nodes.ClassNode.generateClassCode(ClassNode.java:401)
        	at jadx.core.dex.nodes.ClassNode.decompile(ClassNode.java:389)
        	at jadx.core.dex.nodes.ClassNode.getCode(ClassNode.java:339)
        */
    protected final long getTargetSize(long r7) {
        /*
            r6 = this;
            r0 = r6
            long r0 = r0.targetSize
            r1 = r0; r0 = r0; 
            r9 = r1
            r1 = 0
            int r0 = (r0 > r1 ? 1 : (r0 == r1 ? 0 : -1))
            if (r0 == 0) goto Lf
            r0 = r9
            goto L18
            r0 = r6
            r1 = r7
            long r1 = suggestTargetSize(r1)
            // decode failed: arraycopy: source index -1 out of bounds for object array[6]
            r0.targetSize = r1
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: java.util.stream.AbstractTask.getTargetSize(long):long");
    }

    protected AbstractTask(PipelineHelper<P_OUT> pipelineHelper, Spliterator<P_IN> spliterator) {
        super(null);
        this.helper = pipelineHelper;
        this.spliterator = spliterator;
        this.targetSize = 0L;
    }

    protected AbstractTask(K k2, Spliterator<P_IN> spliterator) {
        super(k2);
        this.spliterator = spliterator;
        this.helper = k2.helper;
        this.targetSize = k2.targetSize;
    }

    public static int getLeafTarget() {
        Thread threadCurrentThread = Thread.currentThread();
        if (threadCurrentThread instanceof ForkJoinWorkerThread) {
            return ((ForkJoinWorkerThread) threadCurrentThread).getPool().getParallelism() << 2;
        }
        return LEAF_TARGET;
    }

    public static long suggestTargetSize(long j2) {
        long leafTarget = j2 / getLeafTarget();
        if (leafTarget > 0) {
            return leafTarget;
        }
        return 1L;
    }

    @Override // java.util.concurrent.CountedCompleter, java.util.concurrent.ForkJoinTask
    public R getRawResult() {
        return this.localResult;
    }

    @Override // java.util.concurrent.CountedCompleter, java.util.concurrent.ForkJoinTask
    protected void setRawResult(R r2) {
        if (r2 != null) {
            throw new IllegalStateException();
        }
    }

    protected R getLocalResult() {
        return this.localResult;
    }

    protected void setLocalResult(R r2) {
        this.localResult = r2;
    }

    protected boolean isLeaf() {
        return this.leftChild == null;
    }

    protected boolean isRoot() {
        return getParent() == null;
    }

    protected K getParent() {
        return (K) getCompleter();
    }

    @Override // java.util.concurrent.CountedCompleter
    public void compute() {
        Spliterator<P_IN> spliteratorTrySplit;
        AbstractTask<P_IN, P_OUT, R, K> abstractTask;
        Spliterator<P_IN> spliterator = this.spliterator;
        long jEstimateSize = spliterator.estimateSize();
        long targetSize = getTargetSize(jEstimateSize);
        boolean z2 = false;
        AbstractTask<P_IN, P_OUT, R, K> abstractTask2 = this;
        while (jEstimateSize > targetSize && (spliteratorTrySplit = spliterator.trySplit()) != null) {
            AbstractTask<P_IN, P_OUT, R, K> abstractTaskMakeChild = abstractTask2.makeChild(spliteratorTrySplit);
            abstractTask2.leftChild = abstractTaskMakeChild;
            AbstractTask<P_IN, P_OUT, R, K> abstractTaskMakeChild2 = abstractTask2.makeChild(spliterator);
            abstractTask2.rightChild = abstractTaskMakeChild2;
            abstractTask2.setPendingCount(1);
            if (z2) {
                z2 = false;
                spliterator = spliteratorTrySplit;
                abstractTask2 = abstractTaskMakeChild;
                abstractTask = abstractTaskMakeChild2;
            } else {
                z2 = true;
                abstractTask2 = abstractTaskMakeChild2;
                abstractTask = abstractTaskMakeChild;
            }
            abstractTask.fork();
            jEstimateSize = spliterator.estimateSize();
        }
        abstractTask2.setLocalResult(abstractTask2.doLeaf());
        abstractTask2.tryComplete();
    }

    @Override // java.util.concurrent.CountedCompleter
    public void onCompletion(CountedCompleter<?> countedCompleter) {
        this.spliterator = null;
        this.rightChild = null;
        this.leftChild = null;
    }

    protected boolean isLeftmostNode() {
        AbstractTask<P_IN, P_OUT, R, K> abstractTask = this;
        while (true) {
            AbstractTask<P_IN, P_OUT, R, K> abstractTask2 = abstractTask;
            if (abstractTask2 != null) {
                AbstractTask<P_IN, P_OUT, R, K> parent = abstractTask2.getParent();
                if (parent != null && parent.leftChild != abstractTask2) {
                    return false;
                }
                abstractTask = parent;
            } else {
                return true;
            }
        }
    }
}
