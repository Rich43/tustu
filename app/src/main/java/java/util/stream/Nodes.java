package java.util.stream;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Collection;
import java.util.Deque;
import java.util.Iterator;
import java.util.Objects;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.concurrent.CountedCompleter;
import java.util.function.BinaryOperator;
import java.util.function.Consumer;
import java.util.function.DoubleConsumer;
import java.util.function.IntConsumer;
import java.util.function.IntFunction;
import java.util.function.LongConsumer;
import java.util.function.LongFunction;
import java.util.stream.Node;
import java.util.stream.Sink;
import java.util.stream.SpinedBuffer;

/* loaded from: rt.jar:java/util/stream/Nodes.class */
final class Nodes {
    static final long MAX_ARRAY_SIZE = 2147483639;
    static final String BAD_SIZE = "Stream size exceeds max array size";
    private static final Node EMPTY_NODE = new EmptyNode.OfRef();
    private static final Node.OfInt EMPTY_INT_NODE = new EmptyNode.OfInt();
    private static final Node.OfLong EMPTY_LONG_NODE = new EmptyNode.OfLong();
    private static final Node.OfDouble EMPTY_DOUBLE_NODE = new EmptyNode.OfDouble();
    private static final int[] EMPTY_INT_ARRAY = new int[0];
    private static final long[] EMPTY_LONG_ARRAY = new long[0];
    private static final double[] EMPTY_DOUBLE_ARRAY = new double[0];

    private Nodes() {
        throw new Error("no instances");
    }

    static <T> Node<T> emptyNode(StreamShape streamShape) {
        switch (streamShape) {
            case REFERENCE:
                return EMPTY_NODE;
            case INT_VALUE:
                return EMPTY_INT_NODE;
            case LONG_VALUE:
                return EMPTY_LONG_NODE;
            case DOUBLE_VALUE:
                return EMPTY_DOUBLE_NODE;
            default:
                throw new IllegalStateException("Unknown shape " + ((Object) streamShape));
        }
    }

    static <T> Node<T> conc(StreamShape streamShape, Node<T> node, Node<T> node2) {
        switch (streamShape) {
            case REFERENCE:
                return new ConcNode(node, node2);
            case INT_VALUE:
                return new ConcNode.OfInt((Node.OfInt) node, (Node.OfInt) node2);
            case LONG_VALUE:
                return new ConcNode.OfLong((Node.OfLong) node, (Node.OfLong) node2);
            case DOUBLE_VALUE:
                return new ConcNode.OfDouble((Node.OfDouble) node, (Node.OfDouble) node2);
            default:
                throw new IllegalStateException("Unknown shape " + ((Object) streamShape));
        }
    }

    static <T> Node<T> node(T[] tArr) {
        return new ArrayNode(tArr);
    }

    static <T> Node<T> node(Collection<T> collection) {
        return new CollectionNode(collection);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static <T> Node.Builder<T> builder(long j2, IntFunction<T[]> intFunction) {
        return (j2 < 0 || j2 >= MAX_ARRAY_SIZE) ? builder() : new FixedNodeBuilder(j2, intFunction);
    }

    static <T> Node.Builder<T> builder() {
        return new SpinedNodeBuilder();
    }

    static Node.OfInt node(int[] iArr) {
        return new IntArrayNode(iArr);
    }

    static Node.Builder.OfInt intBuilder(long j2) {
        return (j2 < 0 || j2 >= MAX_ARRAY_SIZE) ? intBuilder() : new IntFixedNodeBuilder(j2);
    }

    static Node.Builder.OfInt intBuilder() {
        return new IntSpinedNodeBuilder();
    }

    static Node.OfLong node(long[] jArr) {
        return new LongArrayNode(jArr);
    }

    static Node.Builder.OfLong longBuilder(long j2) {
        return (j2 < 0 || j2 >= MAX_ARRAY_SIZE) ? longBuilder() : new LongFixedNodeBuilder(j2);
    }

    static Node.Builder.OfLong longBuilder() {
        return new LongSpinedNodeBuilder();
    }

    static Node.OfDouble node(double[] dArr) {
        return new DoubleArrayNode(dArr);
    }

    static Node.Builder.OfDouble doubleBuilder(long j2) {
        return (j2 < 0 || j2 >= MAX_ARRAY_SIZE) ? doubleBuilder() : new DoubleFixedNodeBuilder(j2);
    }

    static Node.Builder.OfDouble doubleBuilder() {
        return new DoubleSpinedNodeBuilder();
    }

    public static <P_IN, P_OUT> Node<P_OUT> collect(PipelineHelper<P_OUT> pipelineHelper, Spliterator<P_IN> spliterator, boolean z2, IntFunction<P_OUT[]> intFunction) {
        long jExactOutputSizeIfKnown = pipelineHelper.exactOutputSizeIfKnown(spliterator);
        if (jExactOutputSizeIfKnown < 0 || !spliterator.hasCharacteristics(16384)) {
            Node<P_OUT> node = (Node) new CollectorTask.OfRef(pipelineHelper, intFunction, spliterator).invoke();
            return z2 ? flatten(node, intFunction) : node;
        }
        if (jExactOutputSizeIfKnown >= MAX_ARRAY_SIZE) {
            throw new IllegalArgumentException(BAD_SIZE);
        }
        P_OUT[] p_outArrApply = intFunction.apply((int) jExactOutputSizeIfKnown);
        new SizedCollectorTask.OfRef(spliterator, pipelineHelper, p_outArrApply).invoke();
        return node(p_outArrApply);
    }

    public static <P_IN> Node.OfInt collectInt(PipelineHelper<Integer> pipelineHelper, Spliterator<P_IN> spliterator, boolean z2) {
        long jExactOutputSizeIfKnown = pipelineHelper.exactOutputSizeIfKnown(spliterator);
        if (jExactOutputSizeIfKnown < 0 || !spliterator.hasCharacteristics(16384)) {
            Node.OfInt ofInt = (Node.OfInt) new CollectorTask.OfInt(pipelineHelper, spliterator).invoke();
            return z2 ? flattenInt(ofInt) : ofInt;
        }
        if (jExactOutputSizeIfKnown >= MAX_ARRAY_SIZE) {
            throw new IllegalArgumentException(BAD_SIZE);
        }
        int[] iArr = new int[(int) jExactOutputSizeIfKnown];
        new SizedCollectorTask.OfInt(spliterator, pipelineHelper, iArr).invoke();
        return node(iArr);
    }

    public static <P_IN> Node.OfLong collectLong(PipelineHelper<Long> pipelineHelper, Spliterator<P_IN> spliterator, boolean z2) {
        long jExactOutputSizeIfKnown = pipelineHelper.exactOutputSizeIfKnown(spliterator);
        if (jExactOutputSizeIfKnown < 0 || !spliterator.hasCharacteristics(16384)) {
            Node.OfLong ofLong = (Node.OfLong) new CollectorTask.OfLong(pipelineHelper, spliterator).invoke();
            return z2 ? flattenLong(ofLong) : ofLong;
        }
        if (jExactOutputSizeIfKnown >= MAX_ARRAY_SIZE) {
            throw new IllegalArgumentException(BAD_SIZE);
        }
        long[] jArr = new long[(int) jExactOutputSizeIfKnown];
        new SizedCollectorTask.OfLong(spliterator, pipelineHelper, jArr).invoke();
        return node(jArr);
    }

    public static <P_IN> Node.OfDouble collectDouble(PipelineHelper<Double> pipelineHelper, Spliterator<P_IN> spliterator, boolean z2) {
        long jExactOutputSizeIfKnown = pipelineHelper.exactOutputSizeIfKnown(spliterator);
        if (jExactOutputSizeIfKnown < 0 || !spliterator.hasCharacteristics(16384)) {
            Node.OfDouble ofDouble = (Node.OfDouble) new CollectorTask.OfDouble(pipelineHelper, spliterator).invoke();
            return z2 ? flattenDouble(ofDouble) : ofDouble;
        }
        if (jExactOutputSizeIfKnown >= MAX_ARRAY_SIZE) {
            throw new IllegalArgumentException(BAD_SIZE);
        }
        double[] dArr = new double[(int) jExactOutputSizeIfKnown];
        new SizedCollectorTask.OfDouble(spliterator, pipelineHelper, dArr).invoke();
        return node(dArr);
    }

    public static <T> Node<T> flatten(Node<T> node, IntFunction<T[]> intFunction) {
        if (node.getChildCount() > 0) {
            long jCount = node.count();
            if (jCount >= MAX_ARRAY_SIZE) {
                throw new IllegalArgumentException(BAD_SIZE);
            }
            T[] tArrApply = intFunction.apply((int) jCount);
            new ToArrayTask.OfRef(node, tArrApply, 0).invoke();
            return node(tArrApply);
        }
        return node;
    }

    public static Node.OfInt flattenInt(Node.OfInt ofInt) {
        if (ofInt.getChildCount() > 0) {
            long jCount = ofInt.count();
            if (jCount >= MAX_ARRAY_SIZE) {
                throw new IllegalArgumentException(BAD_SIZE);
            }
            int[] iArr = new int[(int) jCount];
            new ToArrayTask.OfInt(ofInt, iArr, 0).invoke();
            return node(iArr);
        }
        return ofInt;
    }

    public static Node.OfLong flattenLong(Node.OfLong ofLong) {
        if (ofLong.getChildCount() > 0) {
            long jCount = ofLong.count();
            if (jCount >= MAX_ARRAY_SIZE) {
                throw new IllegalArgumentException(BAD_SIZE);
            }
            long[] jArr = new long[(int) jCount];
            new ToArrayTask.OfLong(ofLong, jArr, 0).invoke();
            return node(jArr);
        }
        return ofLong;
    }

    public static Node.OfDouble flattenDouble(Node.OfDouble ofDouble) {
        if (ofDouble.getChildCount() > 0) {
            long jCount = ofDouble.count();
            if (jCount >= MAX_ARRAY_SIZE) {
                throw new IllegalArgumentException(BAD_SIZE);
            }
            double[] dArr = new double[(int) jCount];
            new ToArrayTask.OfDouble(ofDouble, dArr, 0).invoke();
            return node(dArr);
        }
        return ofDouble;
    }

    /* loaded from: rt.jar:java/util/stream/Nodes$EmptyNode.class */
    private static abstract class EmptyNode<T, T_ARR, T_CONS> implements Node<T> {
        EmptyNode() {
        }

        @Override // java.util.stream.Node
        public T[] asArray(IntFunction<T[]> intFunction) {
            return intFunction.apply(0);
        }

        public void copyInto(T_ARR t_arr, int i2) {
        }

        @Override // java.util.stream.Node
        public long count() {
            return 0L;
        }

        public void forEach(T_CONS t_cons) {
        }

        /* loaded from: rt.jar:java/util/stream/Nodes$EmptyNode$OfRef.class */
        private static class OfRef<T> extends EmptyNode<T, T[], Consumer<? super T>> {
            @Override // java.util.stream.Node
            public /* bridge */ /* synthetic */ void copyInto(Object[] objArr, int i2) {
                super.copyInto((OfRef<T>) objArr, i2);
            }

            @Override // java.util.stream.Node
            public /* bridge */ /* synthetic */ void forEach(Consumer consumer) {
                super.forEach((OfRef<T>) consumer);
            }

            private OfRef() {
            }

            @Override // java.util.stream.Node
            public Spliterator<T> spliterator() {
                return Spliterators.emptySpliterator();
            }
        }

        /* loaded from: rt.jar:java/util/stream/Nodes$EmptyNode$OfInt.class */
        private static final class OfInt extends EmptyNode<Integer, int[], IntConsumer> implements Node.OfInt {
            OfInt() {
            }

            @Override // java.util.stream.Node
            public Spliterator.OfInt spliterator() {
                return Spliterators.emptyIntSpliterator();
            }

            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.util.stream.Node.OfPrimitive
            public int[] asPrimitiveArray() {
                return Nodes.EMPTY_INT_ARRAY;
            }
        }

        /* loaded from: rt.jar:java/util/stream/Nodes$EmptyNode$OfLong.class */
        private static final class OfLong extends EmptyNode<Long, long[], LongConsumer> implements Node.OfLong {
            OfLong() {
            }

            @Override // java.util.stream.Node
            public Spliterator.OfLong spliterator() {
                return Spliterators.emptyLongSpliterator();
            }

            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.util.stream.Node.OfPrimitive
            public long[] asPrimitiveArray() {
                return Nodes.EMPTY_LONG_ARRAY;
            }
        }

        /* loaded from: rt.jar:java/util/stream/Nodes$EmptyNode$OfDouble.class */
        private static final class OfDouble extends EmptyNode<Double, double[], DoubleConsumer> implements Node.OfDouble {
            OfDouble() {
            }

            @Override // java.util.stream.Node
            public Spliterator.OfDouble spliterator() {
                return Spliterators.emptyDoubleSpliterator();
            }

            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.util.stream.Node.OfPrimitive
            public double[] asPrimitiveArray() {
                return Nodes.EMPTY_DOUBLE_ARRAY;
            }
        }
    }

    /* loaded from: rt.jar:java/util/stream/Nodes$ArrayNode.class */
    private static class ArrayNode<T> implements Node<T> {
        final T[] array;
        int curSize;

        ArrayNode(long j2, IntFunction<T[]> intFunction) {
            if (j2 >= Nodes.MAX_ARRAY_SIZE) {
                throw new IllegalArgumentException(Nodes.BAD_SIZE);
            }
            this.array = intFunction.apply((int) j2);
            this.curSize = 0;
        }

        ArrayNode(T[] tArr) {
            this.array = tArr;
            this.curSize = tArr.length;
        }

        @Override // java.util.stream.Node
        public Spliterator<T> spliterator() {
            return Arrays.spliterator(this.array, 0, this.curSize);
        }

        @Override // java.util.stream.Node
        public void copyInto(T[] tArr, int i2) {
            System.arraycopy(this.array, 0, tArr, i2, this.curSize);
        }

        @Override // java.util.stream.Node
        public T[] asArray(IntFunction<T[]> intFunction) {
            if (this.array.length == this.curSize) {
                return this.array;
            }
            throw new IllegalStateException();
        }

        @Override // java.util.stream.Node
        public long count() {
            return this.curSize;
        }

        @Override // java.util.stream.Node
        public void forEach(Consumer<? super T> consumer) {
            for (int i2 = 0; i2 < this.curSize; i2++) {
                consumer.accept(this.array[i2]);
            }
        }

        public String toString() {
            return String.format("ArrayNode[%d][%s]", Integer.valueOf(this.array.length - this.curSize), Arrays.toString(this.array));
        }
    }

    /* loaded from: rt.jar:java/util/stream/Nodes$CollectionNode.class */
    private static final class CollectionNode<T> implements Node<T> {

        /* renamed from: c, reason: collision with root package name */
        private final Collection<T> f12601c;

        CollectionNode(Collection<T> collection) {
            this.f12601c = collection;
        }

        @Override // java.util.stream.Node
        public Spliterator<T> spliterator() {
            return this.f12601c.stream().spliterator2();
        }

        @Override // java.util.stream.Node
        public void copyInto(T[] tArr, int i2) {
            Iterator<T> it = this.f12601c.iterator();
            while (it.hasNext()) {
                int i3 = i2;
                i2++;
                tArr[i3] = it.next();
            }
        }

        @Override // java.util.stream.Node
        public T[] asArray(IntFunction<T[]> intFunction) {
            return (T[]) this.f12601c.toArray(intFunction.apply(this.f12601c.size()));
        }

        @Override // java.util.stream.Node
        public long count() {
            return this.f12601c.size();
        }

        @Override // java.util.stream.Node
        public void forEach(Consumer<? super T> consumer) {
            this.f12601c.forEach(consumer);
        }

        public String toString() {
            return String.format("CollectionNode[%d][%s]", Integer.valueOf(this.f12601c.size()), this.f12601c);
        }
    }

    /* loaded from: rt.jar:java/util/stream/Nodes$AbstractConcNode.class */
    private static abstract class AbstractConcNode<T, T_NODE extends Node<T>> implements Node<T> {
        protected final T_NODE left;
        protected final T_NODE right;
        private final long size;

        AbstractConcNode(T_NODE t_node, T_NODE t_node2) {
            this.left = t_node;
            this.right = t_node2;
            this.size = t_node.count() + t_node2.count();
        }

        @Override // java.util.stream.Node
        public int getChildCount() {
            return 2;
        }

        @Override // java.util.stream.Node
        public T_NODE getChild(int i2) {
            if (i2 == 0) {
                return this.left;
            }
            if (i2 == 1) {
                return this.right;
            }
            throw new IndexOutOfBoundsException();
        }

        @Override // java.util.stream.Node
        public long count() {
            return this.size;
        }
    }

    /* loaded from: rt.jar:java/util/stream/Nodes$ConcNode.class */
    static final class ConcNode<T> extends AbstractConcNode<T, Node<T>> implements Node<T> {
        ConcNode(Node<T> node, Node<T> node2) {
            super(node, node2);
        }

        @Override // java.util.stream.Node
        public Spliterator<T> spliterator() {
            return new InternalNodeSpliterator.OfRef(this);
        }

        @Override // java.util.stream.Node
        public void copyInto(T[] tArr, int i2) {
            Objects.requireNonNull(tArr);
            this.left.copyInto(tArr, i2);
            this.right.copyInto(tArr, i2 + ((int) this.left.count()));
        }

        @Override // java.util.stream.Node
        public T[] asArray(IntFunction<T[]> intFunction) {
            long jCount = count();
            if (jCount >= Nodes.MAX_ARRAY_SIZE) {
                throw new IllegalArgumentException(Nodes.BAD_SIZE);
            }
            T[] tArrApply = intFunction.apply((int) jCount);
            copyInto(tArrApply, 0);
            return tArrApply;
        }

        @Override // java.util.stream.Node
        public void forEach(Consumer<? super T> consumer) {
            this.left.forEach(consumer);
            this.right.forEach(consumer);
        }

        @Override // java.util.stream.Node
        public Node<T> truncate(long j2, long j3, IntFunction<T[]> intFunction) {
            if (j2 == 0 && j3 == count()) {
                return this;
            }
            long jCount = this.left.count();
            if (j2 >= jCount) {
                return this.right.truncate(j2 - jCount, j3 - jCount, intFunction);
            }
            if (j3 <= jCount) {
                return this.left.truncate(j2, j3, intFunction);
            }
            return Nodes.conc(getShape(), this.left.truncate(j2, jCount, intFunction), this.right.truncate(0L, j3 - jCount, intFunction));
        }

        public String toString() {
            if (count() < 32) {
                return String.format("ConcNode[%s.%s]", this.left, this.right);
            }
            return String.format("ConcNode[size=%d]", Long.valueOf(count()));
        }

        /* loaded from: rt.jar:java/util/stream/Nodes$ConcNode$OfPrimitive.class */
        private static abstract class OfPrimitive<E, T_CONS, T_ARR, T_SPLITR extends Spliterator.OfPrimitive<E, T_CONS, T_SPLITR>, T_NODE extends Node.OfPrimitive<E, T_CONS, T_ARR, T_SPLITR, T_NODE>> extends AbstractConcNode<E, T_NODE> implements Node.OfPrimitive<E, T_CONS, T_ARR, T_SPLITR, T_NODE> {
            @Override // java.util.stream.Nodes.AbstractConcNode, java.util.stream.Node
            public /* bridge */ /* synthetic */ Node.OfPrimitive getChild(int i2) {
                return (Node.OfPrimitive) super.getChild(i2);
            }

            OfPrimitive(T_NODE t_node, T_NODE t_node2) {
                super(t_node, t_node2);
            }

            @Override // java.util.stream.Node.OfPrimitive
            public void forEach(T_CONS t_cons) {
                ((Node.OfPrimitive) this.left).forEach((Node.OfPrimitive) t_cons);
                ((Node.OfPrimitive) this.right).forEach((Node.OfPrimitive) t_cons);
            }

            @Override // java.util.stream.Node.OfPrimitive
            public void copyInto(T_ARR t_arr, int i2) {
                ((Node.OfPrimitive) this.left).copyInto((Node.OfPrimitive) t_arr, i2);
                ((Node.OfPrimitive) this.right).copyInto((Node.OfPrimitive) t_arr, i2 + ((int) ((Node.OfPrimitive) this.left).count()));
            }

            @Override // java.util.stream.Node.OfPrimitive
            public T_ARR asPrimitiveArray() {
                long jCount = count();
                if (jCount >= Nodes.MAX_ARRAY_SIZE) {
                    throw new IllegalArgumentException(Nodes.BAD_SIZE);
                }
                T_ARR t_arrNewArray = newArray((int) jCount);
                copyInto((OfPrimitive<E, T_CONS, T_ARR, T_SPLITR, T_NODE>) t_arrNewArray, 0);
                return t_arrNewArray;
            }

            public String toString() {
                if (count() < 32) {
                    return String.format("%s[%s.%s]", getClass().getName(), this.left, this.right);
                }
                return String.format("%s[size=%d]", getClass().getName(), Long.valueOf(count()));
            }
        }

        /* loaded from: rt.jar:java/util/stream/Nodes$ConcNode$OfInt.class */
        static final class OfInt extends OfPrimitive<Integer, IntConsumer, int[], Spliterator.OfInt, Node.OfInt> implements Node.OfInt {
            OfInt(Node.OfInt ofInt, Node.OfInt ofInt2) {
                super(ofInt, ofInt2);
            }

            @Override // java.util.stream.Node
            public Spliterator.OfInt spliterator() {
                return new InternalNodeSpliterator.OfInt(this);
            }
        }

        /* loaded from: rt.jar:java/util/stream/Nodes$ConcNode$OfLong.class */
        static final class OfLong extends OfPrimitive<Long, LongConsumer, long[], Spliterator.OfLong, Node.OfLong> implements Node.OfLong {
            OfLong(Node.OfLong ofLong, Node.OfLong ofLong2) {
                super(ofLong, ofLong2);
            }

            @Override // java.util.stream.Node
            public Spliterator.OfLong spliterator() {
                return new InternalNodeSpliterator.OfLong(this);
            }
        }

        /* loaded from: rt.jar:java/util/stream/Nodes$ConcNode$OfDouble.class */
        static final class OfDouble extends OfPrimitive<Double, DoubleConsumer, double[], Spliterator.OfDouble, Node.OfDouble> implements Node.OfDouble {
            OfDouble(Node.OfDouble ofDouble, Node.OfDouble ofDouble2) {
                super(ofDouble, ofDouble2);
            }

            @Override // java.util.stream.Node
            public Spliterator.OfDouble spliterator() {
                return new InternalNodeSpliterator.OfDouble(this);
            }
        }
    }

    /* loaded from: rt.jar:java/util/stream/Nodes$InternalNodeSpliterator.class */
    private static abstract class InternalNodeSpliterator<T, S extends Spliterator<T>, N extends Node<T>> implements Spliterator<T> {
        N curNode;
        int curChildIndex;
        S lastNodeSpliterator;
        S tryAdvanceSpliterator;
        Deque<N> tryAdvanceStack;

        InternalNodeSpliterator(N n2) {
            this.curNode = n2;
        }

        protected final Deque<N> initStack() {
            ArrayDeque arrayDeque = new ArrayDeque(8);
            for (int childCount = this.curNode.getChildCount() - 1; childCount >= this.curChildIndex; childCount--) {
                arrayDeque.addFirst(this.curNode.getChild(childCount));
            }
            return arrayDeque;
        }

        /* JADX WARN: Multi-variable type inference failed */
        protected final N findNextLeafNode(Deque<N> deque) {
            while (true) {
                N n2 = (N) deque.pollFirst();
                if (n2 != null) {
                    if (n2.getChildCount() != 0) {
                        for (int childCount = n2.getChildCount() - 1; childCount >= 0; childCount--) {
                            deque.addFirst(n2.getChild(childCount));
                        }
                    } else if (n2.count() > 0) {
                        return n2;
                    }
                } else {
                    return null;
                }
            }
        }

        protected final boolean initTryAdvance() {
            if (this.curNode == null) {
                return false;
            }
            if (this.tryAdvanceSpliterator == null) {
                if (this.lastNodeSpliterator == null) {
                    this.tryAdvanceStack = initStack();
                    Node nodeFindNextLeafNode = findNextLeafNode(this.tryAdvanceStack);
                    if (nodeFindNextLeafNode != null) {
                        this.tryAdvanceSpliterator = (S) nodeFindNextLeafNode.spliterator();
                        return true;
                    }
                    this.curNode = null;
                    return false;
                }
                this.tryAdvanceSpliterator = this.lastNodeSpliterator;
                return true;
            }
            return true;
        }

        @Override // java.util.Spliterator
        public final S trySplit() {
            if (this.curNode == null || this.tryAdvanceSpliterator != null) {
                return null;
            }
            if (this.lastNodeSpliterator != null) {
                return (S) this.lastNodeSpliterator.trySplit();
            }
            if (this.curChildIndex < this.curNode.getChildCount() - 1) {
                N n2 = this.curNode;
                int i2 = this.curChildIndex;
                this.curChildIndex = i2 + 1;
                return n2.getChild(i2).spliterator();
            }
            this.curNode = (N) this.curNode.getChild(this.curChildIndex);
            if (this.curNode.getChildCount() == 0) {
                this.lastNodeSpliterator = (S) this.curNode.spliterator();
                return (S) this.lastNodeSpliterator.trySplit();
            }
            this.curChildIndex = 0;
            N n3 = this.curNode;
            int i3 = this.curChildIndex;
            this.curChildIndex = i3 + 1;
            return n3.getChild(i3).spliterator();
        }

        @Override // java.util.Spliterator
        public final long estimateSize() {
            if (this.curNode == null) {
                return 0L;
            }
            if (this.lastNodeSpliterator != null) {
                return this.lastNodeSpliterator.estimateSize();
            }
            long jCount = 0;
            for (int i2 = this.curChildIndex; i2 < this.curNode.getChildCount(); i2++) {
                jCount += this.curNode.getChild(i2).count();
            }
            return jCount;
        }

        @Override // java.util.Spliterator
        public final int characteristics() {
            return 64;
        }

        /* loaded from: rt.jar:java/util/stream/Nodes$InternalNodeSpliterator$OfRef.class */
        private static final class OfRef<T> extends InternalNodeSpliterator<T, Spliterator<T>, Node<T>> {
            OfRef(Node<T> node) {
                super(node);
            }

            @Override // java.util.Spliterator
            public boolean tryAdvance(Consumer<? super T> consumer) {
                Node<T> nodeFindNextLeafNode;
                if (!initTryAdvance()) {
                    return false;
                }
                boolean zTryAdvance = this.tryAdvanceSpliterator.tryAdvance(consumer);
                if (!zTryAdvance) {
                    if (this.lastNodeSpliterator == null && (nodeFindNextLeafNode = findNextLeafNode(this.tryAdvanceStack)) != null) {
                        this.tryAdvanceSpliterator = nodeFindNextLeafNode.spliterator();
                        return this.tryAdvanceSpliterator.tryAdvance(consumer);
                    }
                    this.curNode = null;
                }
                return zTryAdvance;
            }

            /* JADX WARN: Multi-variable type inference failed */
            @Override // java.util.Spliterator
            public void forEachRemaining(Consumer<? super T> consumer) {
                if (this.curNode == null) {
                    return;
                }
                if (this.tryAdvanceSpliterator == null) {
                    if (this.lastNodeSpliterator == null) {
                        Deque dequeInitStack = initStack();
                        while (true) {
                            Node nodeFindNextLeafNode = findNextLeafNode(dequeInitStack);
                            if (nodeFindNextLeafNode != null) {
                                nodeFindNextLeafNode.forEach(consumer);
                            } else {
                                this.curNode = null;
                                return;
                            }
                        }
                    } else {
                        this.lastNodeSpliterator.forEachRemaining(consumer);
                    }
                } else {
                    while (tryAdvance(consumer)) {
                    }
                }
            }
        }

        /* loaded from: rt.jar:java/util/stream/Nodes$InternalNodeSpliterator$OfPrimitive.class */
        private static abstract class OfPrimitive<T, T_CONS, T_ARR, T_SPLITR extends Spliterator.OfPrimitive<T, T_CONS, T_SPLITR>, N extends Node.OfPrimitive<T, T_CONS, T_ARR, T_SPLITR, N>> extends InternalNodeSpliterator<T, T_SPLITR, N> implements Spliterator.OfPrimitive<T, T_CONS, T_SPLITR> {
            @Override // java.util.stream.Nodes.InternalNodeSpliterator, java.util.Spliterator
            public /* bridge */ /* synthetic */ Spliterator.OfPrimitive trySplit() {
                return (Spliterator.OfPrimitive) super.trySplit();
            }

            OfPrimitive(N n2) {
                super(n2);
            }

            @Override // java.util.Spliterator.OfPrimitive
            public boolean tryAdvance(T_CONS t_cons) {
                N nFindNextLeafNode;
                if (!initTryAdvance()) {
                    return false;
                }
                boolean zTryAdvance = ((Spliterator.OfPrimitive) this.tryAdvanceSpliterator).tryAdvance((Spliterator.OfPrimitive) t_cons);
                if (!zTryAdvance) {
                    if (this.lastNodeSpliterator == null && (nFindNextLeafNode = findNextLeafNode(this.tryAdvanceStack)) != null) {
                        this.tryAdvanceSpliterator = nFindNextLeafNode.spliterator();
                        return ((Spliterator.OfPrimitive) this.tryAdvanceSpliterator).tryAdvance((Spliterator.OfPrimitive) t_cons);
                    }
                    this.curNode = null;
                }
                return zTryAdvance;
            }

            @Override // java.util.Spliterator.OfPrimitive
            public void forEachRemaining(T_CONS t_cons) {
                if (this.curNode == null) {
                    return;
                }
                if (this.tryAdvanceSpliterator == null) {
                    if (this.lastNodeSpliterator == null) {
                        Deque<N> dequeInitStack = initStack();
                        while (true) {
                            N nFindNextLeafNode = findNextLeafNode(dequeInitStack);
                            if (nFindNextLeafNode != null) {
                                nFindNextLeafNode.forEach(t_cons);
                            } else {
                                this.curNode = null;
                                return;
                            }
                        }
                    } else {
                        ((Spliterator.OfPrimitive) this.lastNodeSpliterator).forEachRemaining((Spliterator.OfPrimitive) t_cons);
                    }
                } else {
                    while (tryAdvance((OfPrimitive<T, T_CONS, T_ARR, T_SPLITR, N>) t_cons)) {
                    }
                }
            }
        }

        /* loaded from: rt.jar:java/util/stream/Nodes$InternalNodeSpliterator$OfInt.class */
        private static final class OfInt extends OfPrimitive<Integer, IntConsumer, int[], Spliterator.OfInt, Node.OfInt> implements Spliterator.OfInt {
            @Override // java.util.Spliterator.OfInt
            public /* bridge */ /* synthetic */ void forEachRemaining(IntConsumer intConsumer) {
                super.forEachRemaining((OfInt) intConsumer);
            }

            @Override // java.util.Spliterator.OfInt
            public /* bridge */ /* synthetic */ boolean tryAdvance(IntConsumer intConsumer) {
                return super.tryAdvance((OfInt) intConsumer);
            }

            @Override // java.util.stream.Nodes.InternalNodeSpliterator.OfPrimitive, java.util.stream.Nodes.InternalNodeSpliterator, java.util.Spliterator
            public /* bridge */ /* synthetic */ Spliterator.OfInt trySplit() {
                return (Spliterator.OfInt) super.trySplit();
            }

            OfInt(Node.OfInt ofInt) {
                super(ofInt);
            }
        }

        /* loaded from: rt.jar:java/util/stream/Nodes$InternalNodeSpliterator$OfLong.class */
        private static final class OfLong extends OfPrimitive<Long, LongConsumer, long[], Spliterator.OfLong, Node.OfLong> implements Spliterator.OfLong {
            @Override // java.util.Spliterator.OfLong
            public /* bridge */ /* synthetic */ void forEachRemaining(LongConsumer longConsumer) {
                super.forEachRemaining((OfLong) longConsumer);
            }

            @Override // java.util.Spliterator.OfLong
            public /* bridge */ /* synthetic */ boolean tryAdvance(LongConsumer longConsumer) {
                return super.tryAdvance((OfLong) longConsumer);
            }

            @Override // java.util.stream.Nodes.InternalNodeSpliterator.OfPrimitive, java.util.stream.Nodes.InternalNodeSpliterator, java.util.Spliterator
            public /* bridge */ /* synthetic */ Spliterator.OfLong trySplit() {
                return (Spliterator.OfLong) super.trySplit();
            }

            OfLong(Node.OfLong ofLong) {
                super(ofLong);
            }
        }

        /* loaded from: rt.jar:java/util/stream/Nodes$InternalNodeSpliterator$OfDouble.class */
        private static final class OfDouble extends OfPrimitive<Double, DoubleConsumer, double[], Spliterator.OfDouble, Node.OfDouble> implements Spliterator.OfDouble {
            @Override // java.util.Spliterator.OfDouble
            public /* bridge */ /* synthetic */ void forEachRemaining(DoubleConsumer doubleConsumer) {
                super.forEachRemaining((OfDouble) doubleConsumer);
            }

            @Override // java.util.Spliterator.OfDouble
            public /* bridge */ /* synthetic */ boolean tryAdvance(DoubleConsumer doubleConsumer) {
                return super.tryAdvance((OfDouble) doubleConsumer);
            }

            @Override // java.util.stream.Nodes.InternalNodeSpliterator.OfPrimitive, java.util.stream.Nodes.InternalNodeSpliterator, java.util.Spliterator
            public /* bridge */ /* synthetic */ Spliterator.OfDouble trySplit() {
                return (Spliterator.OfDouble) super.trySplit();
            }

            OfDouble(Node.OfDouble ofDouble) {
                super(ofDouble);
            }
        }
    }

    /* loaded from: rt.jar:java/util/stream/Nodes$FixedNodeBuilder.class */
    private static final class FixedNodeBuilder<T> extends ArrayNode<T> implements Node.Builder<T> {
        static final /* synthetic */ boolean $assertionsDisabled;

        static {
            $assertionsDisabled = !Nodes.class.desiredAssertionStatus();
        }

        FixedNodeBuilder(long j2, IntFunction<T[]> intFunction) {
            super(j2, intFunction);
            if (!$assertionsDisabled && j2 >= Nodes.MAX_ARRAY_SIZE) {
                throw new AssertionError();
            }
        }

        @Override // java.util.stream.Node.Builder
        /* renamed from: build */
        public Node<T> build2() {
            if (this.curSize < this.array.length) {
                throw new IllegalStateException(String.format("Current size %d is less than fixed size %d", Integer.valueOf(this.curSize), Integer.valueOf(this.array.length)));
            }
            return this;
        }

        @Override // java.util.stream.Sink
        public void begin(long j2) {
            if (j2 != this.array.length) {
                throw new IllegalStateException(String.format("Begin size %d is not equal to fixed size %d", Long.valueOf(j2), Integer.valueOf(this.array.length)));
            }
            this.curSize = 0;
        }

        @Override // java.util.function.Consumer
        public void accept(T t2) {
            if (this.curSize < this.array.length) {
                T[] tArr = this.array;
                int i2 = this.curSize;
                this.curSize = i2 + 1;
                tArr[i2] = t2;
                return;
            }
            throw new IllegalStateException(String.format("Accept exceeded fixed size of %d", Integer.valueOf(this.array.length)));
        }

        @Override // java.util.stream.Sink
        public void end() {
            if (this.curSize < this.array.length) {
                throw new IllegalStateException(String.format("End size %d is less than fixed size %d", Integer.valueOf(this.curSize), Integer.valueOf(this.array.length)));
            }
        }

        @Override // java.util.stream.Nodes.ArrayNode
        public String toString() {
            return String.format("FixedNodeBuilder[%d][%s]", Integer.valueOf(this.array.length - this.curSize), Arrays.toString(this.array));
        }
    }

    /* loaded from: rt.jar:java/util/stream/Nodes$SpinedNodeBuilder.class */
    private static final class SpinedNodeBuilder<T> extends SpinedBuffer<T> implements Node<T>, Node.Builder<T> {
        private boolean building = false;
        static final /* synthetic */ boolean $assertionsDisabled;

        static {
            $assertionsDisabled = !Nodes.class.desiredAssertionStatus();
        }

        SpinedNodeBuilder() {
        }

        @Override // java.util.stream.SpinedBuffer, java.lang.Iterable
        public Spliterator<T> spliterator() {
            if ($assertionsDisabled || !this.building) {
                return super.spliterator();
            }
            throw new AssertionError((Object) "during building");
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // java.util.stream.SpinedBuffer, java.lang.Iterable
        public void forEach(Consumer<? super T> consumer) {
            if (!$assertionsDisabled && this.building) {
                throw new AssertionError((Object) "during building");
            }
            super.forEach(consumer);
        }

        @Override // java.util.stream.Sink
        public void begin(long j2) {
            if (!$assertionsDisabled && this.building) {
                throw new AssertionError((Object) "was already building");
            }
            this.building = true;
            clear();
            ensureCapacity(j2);
        }

        @Override // java.util.stream.SpinedBuffer, java.util.function.Consumer
        public void accept(T t2) {
            if (!$assertionsDisabled && !this.building) {
                throw new AssertionError((Object) "not building");
            }
            super.accept((SpinedNodeBuilder<T>) t2);
        }

        @Override // java.util.stream.Sink
        public void end() {
            if (!$assertionsDisabled && !this.building) {
                throw new AssertionError((Object) "was not building");
            }
            this.building = false;
        }

        @Override // java.util.stream.SpinedBuffer, java.util.stream.Node
        public void copyInto(T[] tArr, int i2) {
            if (!$assertionsDisabled && this.building) {
                throw new AssertionError((Object) "during building");
            }
            super.copyInto(tArr, i2);
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // java.util.stream.SpinedBuffer, java.util.stream.Node
        public T[] asArray(IntFunction<T[]> intFunction) {
            if ($assertionsDisabled || !this.building) {
                return (T[]) super.asArray(intFunction);
            }
            throw new AssertionError((Object) "during building");
        }

        @Override // java.util.stream.Node.Builder
        /* renamed from: build */
        public Node<T> build2() {
            if ($assertionsDisabled || !this.building) {
                return this;
            }
            throw new AssertionError((Object) "during building");
        }
    }

    /* loaded from: rt.jar:java/util/stream/Nodes$IntArrayNode.class */
    private static class IntArrayNode implements Node.OfInt {
        final int[] array;
        int curSize;

        IntArrayNode(long j2) {
            if (j2 >= Nodes.MAX_ARRAY_SIZE) {
                throw new IllegalArgumentException(Nodes.BAD_SIZE);
            }
            this.array = new int[(int) j2];
            this.curSize = 0;
        }

        IntArrayNode(int[] iArr) {
            this.array = iArr;
            this.curSize = iArr.length;
        }

        @Override // java.util.stream.Node.OfPrimitive, java.util.stream.Node
        public Spliterator.OfInt spliterator() {
            return Arrays.spliterator(this.array, 0, this.curSize);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // java.util.stream.Node.OfPrimitive
        public int[] asPrimitiveArray() {
            if (this.array.length == this.curSize) {
                return this.array;
            }
            return Arrays.copyOf(this.array, this.curSize);
        }

        @Override // java.util.stream.Node.OfPrimitive
        public void copyInto(int[] iArr, int i2) {
            System.arraycopy(this.array, 0, iArr, i2, this.curSize);
        }

        @Override // java.util.stream.Node
        public long count() {
            return this.curSize;
        }

        @Override // java.util.stream.Node.OfPrimitive
        public void forEach(IntConsumer intConsumer) {
            for (int i2 = 0; i2 < this.curSize; i2++) {
                intConsumer.accept(this.array[i2]);
            }
        }

        public String toString() {
            return String.format("IntArrayNode[%d][%s]", Integer.valueOf(this.array.length - this.curSize), Arrays.toString(this.array));
        }
    }

    /* loaded from: rt.jar:java/util/stream/Nodes$LongArrayNode.class */
    private static class LongArrayNode implements Node.OfLong {
        final long[] array;
        int curSize;

        LongArrayNode(long j2) {
            if (j2 >= Nodes.MAX_ARRAY_SIZE) {
                throw new IllegalArgumentException(Nodes.BAD_SIZE);
            }
            this.array = new long[(int) j2];
            this.curSize = 0;
        }

        LongArrayNode(long[] jArr) {
            this.array = jArr;
            this.curSize = jArr.length;
        }

        @Override // java.util.stream.Node.OfPrimitive, java.util.stream.Node
        public Spliterator.OfLong spliterator() {
            return Arrays.spliterator(this.array, 0, this.curSize);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // java.util.stream.Node.OfPrimitive
        public long[] asPrimitiveArray() {
            if (this.array.length == this.curSize) {
                return this.array;
            }
            return Arrays.copyOf(this.array, this.curSize);
        }

        @Override // java.util.stream.Node.OfPrimitive
        public void copyInto(long[] jArr, int i2) {
            System.arraycopy(this.array, 0, jArr, i2, this.curSize);
        }

        @Override // java.util.stream.Node
        public long count() {
            return this.curSize;
        }

        @Override // java.util.stream.Node.OfPrimitive
        public void forEach(LongConsumer longConsumer) {
            for (int i2 = 0; i2 < this.curSize; i2++) {
                longConsumer.accept(this.array[i2]);
            }
        }

        public String toString() {
            return String.format("LongArrayNode[%d][%s]", Integer.valueOf(this.array.length - this.curSize), Arrays.toString(this.array));
        }
    }

    /* loaded from: rt.jar:java/util/stream/Nodes$DoubleArrayNode.class */
    private static class DoubleArrayNode implements Node.OfDouble {
        final double[] array;
        int curSize;

        DoubleArrayNode(long j2) {
            if (j2 >= Nodes.MAX_ARRAY_SIZE) {
                throw new IllegalArgumentException(Nodes.BAD_SIZE);
            }
            this.array = new double[(int) j2];
            this.curSize = 0;
        }

        DoubleArrayNode(double[] dArr) {
            this.array = dArr;
            this.curSize = dArr.length;
        }

        @Override // java.util.stream.Node.OfPrimitive, java.util.stream.Node
        public Spliterator.OfDouble spliterator() {
            return Arrays.spliterator(this.array, 0, this.curSize);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // java.util.stream.Node.OfPrimitive
        public double[] asPrimitiveArray() {
            if (this.array.length == this.curSize) {
                return this.array;
            }
            return Arrays.copyOf(this.array, this.curSize);
        }

        @Override // java.util.stream.Node.OfPrimitive
        public void copyInto(double[] dArr, int i2) {
            System.arraycopy(this.array, 0, dArr, i2, this.curSize);
        }

        @Override // java.util.stream.Node
        public long count() {
            return this.curSize;
        }

        @Override // java.util.stream.Node.OfPrimitive
        public void forEach(DoubleConsumer doubleConsumer) {
            for (int i2 = 0; i2 < this.curSize; i2++) {
                doubleConsumer.accept(this.array[i2]);
            }
        }

        public String toString() {
            return String.format("DoubleArrayNode[%d][%s]", Integer.valueOf(this.array.length - this.curSize), Arrays.toString(this.array));
        }
    }

    /* loaded from: rt.jar:java/util/stream/Nodes$IntFixedNodeBuilder.class */
    private static final class IntFixedNodeBuilder extends IntArrayNode implements Node.Builder.OfInt {
        static final /* synthetic */ boolean $assertionsDisabled;

        static {
            $assertionsDisabled = !Nodes.class.desiredAssertionStatus();
        }

        IntFixedNodeBuilder(long j2) {
            super(j2);
            if (!$assertionsDisabled && j2 >= Nodes.MAX_ARRAY_SIZE) {
                throw new AssertionError();
            }
        }

        @Override // java.util.stream.Node.Builder.OfInt, java.util.stream.Node.Builder
        /* renamed from: build */
        public Node<Integer> build2() {
            if (this.curSize < this.array.length) {
                throw new IllegalStateException(String.format("Current size %d is less than fixed size %d", Integer.valueOf(this.curSize), Integer.valueOf(this.array.length)));
            }
            return this;
        }

        @Override // java.util.stream.Sink
        public void begin(long j2) {
            if (j2 != this.array.length) {
                throw new IllegalStateException(String.format("Begin size %d is not equal to fixed size %d", Long.valueOf(j2), Integer.valueOf(this.array.length)));
            }
            this.curSize = 0;
        }

        @Override // java.util.stream.Sink, java.util.stream.Sink.OfInt, java.util.function.IntConsumer
        public void accept(int i2) {
            if (this.curSize < this.array.length) {
                int[] iArr = this.array;
                int i3 = this.curSize;
                this.curSize = i3 + 1;
                iArr[i3] = i2;
                return;
            }
            throw new IllegalStateException(String.format("Accept exceeded fixed size of %d", Integer.valueOf(this.array.length)));
        }

        @Override // java.util.stream.Sink
        public void end() {
            if (this.curSize < this.array.length) {
                throw new IllegalStateException(String.format("End size %d is less than fixed size %d", Integer.valueOf(this.curSize), Integer.valueOf(this.array.length)));
            }
        }

        @Override // java.util.stream.Nodes.IntArrayNode
        public String toString() {
            return String.format("IntFixedNodeBuilder[%d][%s]", Integer.valueOf(this.array.length - this.curSize), Arrays.toString(this.array));
        }
    }

    /* loaded from: rt.jar:java/util/stream/Nodes$LongFixedNodeBuilder.class */
    private static final class LongFixedNodeBuilder extends LongArrayNode implements Node.Builder.OfLong {
        static final /* synthetic */ boolean $assertionsDisabled;

        static {
            $assertionsDisabled = !Nodes.class.desiredAssertionStatus();
        }

        LongFixedNodeBuilder(long j2) {
            super(j2);
            if (!$assertionsDisabled && j2 >= Nodes.MAX_ARRAY_SIZE) {
                throw new AssertionError();
            }
        }

        @Override // java.util.stream.Node.Builder.OfLong, java.util.stream.Node.Builder
        /* renamed from: build */
        public Node<Long> build2() {
            if (this.curSize < this.array.length) {
                throw new IllegalStateException(String.format("Current size %d is less than fixed size %d", Integer.valueOf(this.curSize), Integer.valueOf(this.array.length)));
            }
            return this;
        }

        @Override // java.util.stream.Sink
        public void begin(long j2) {
            if (j2 != this.array.length) {
                throw new IllegalStateException(String.format("Begin size %d is not equal to fixed size %d", Long.valueOf(j2), Integer.valueOf(this.array.length)));
            }
            this.curSize = 0;
        }

        @Override // java.util.stream.Sink, java.util.stream.Sink.OfLong, java.util.function.LongConsumer
        public void accept(long j2) {
            if (this.curSize < this.array.length) {
                long[] jArr = this.array;
                int i2 = this.curSize;
                this.curSize = i2 + 1;
                jArr[i2] = j2;
                return;
            }
            throw new IllegalStateException(String.format("Accept exceeded fixed size of %d", Integer.valueOf(this.array.length)));
        }

        @Override // java.util.stream.Sink
        public void end() {
            if (this.curSize < this.array.length) {
                throw new IllegalStateException(String.format("End size %d is less than fixed size %d", Integer.valueOf(this.curSize), Integer.valueOf(this.array.length)));
            }
        }

        @Override // java.util.stream.Nodes.LongArrayNode
        public String toString() {
            return String.format("LongFixedNodeBuilder[%d][%s]", Integer.valueOf(this.array.length - this.curSize), Arrays.toString(this.array));
        }
    }

    /* loaded from: rt.jar:java/util/stream/Nodes$DoubleFixedNodeBuilder.class */
    private static final class DoubleFixedNodeBuilder extends DoubleArrayNode implements Node.Builder.OfDouble {
        static final /* synthetic */ boolean $assertionsDisabled;

        static {
            $assertionsDisabled = !Nodes.class.desiredAssertionStatus();
        }

        DoubleFixedNodeBuilder(long j2) {
            super(j2);
            if (!$assertionsDisabled && j2 >= Nodes.MAX_ARRAY_SIZE) {
                throw new AssertionError();
            }
        }

        @Override // java.util.stream.Node.Builder.OfDouble, java.util.stream.Node.Builder
        /* renamed from: build, reason: merged with bridge method [inline-methods] */
        public Node<Double> build2() {
            if (this.curSize < this.array.length) {
                throw new IllegalStateException(String.format("Current size %d is less than fixed size %d", Integer.valueOf(this.curSize), Integer.valueOf(this.array.length)));
            }
            return this;
        }

        @Override // java.util.stream.Sink
        public void begin(long j2) {
            if (j2 != this.array.length) {
                throw new IllegalStateException(String.format("Begin size %d is not equal to fixed size %d", Long.valueOf(j2), Integer.valueOf(this.array.length)));
            }
            this.curSize = 0;
        }

        @Override // java.util.stream.Sink, java.util.function.DoubleConsumer
        public void accept(double d2) {
            if (this.curSize < this.array.length) {
                double[] dArr = this.array;
                int i2 = this.curSize;
                this.curSize = i2 + 1;
                dArr[i2] = d2;
                return;
            }
            throw new IllegalStateException(String.format("Accept exceeded fixed size of %d", Integer.valueOf(this.array.length)));
        }

        @Override // java.util.stream.Sink
        public void end() {
            if (this.curSize < this.array.length) {
                throw new IllegalStateException(String.format("End size %d is less than fixed size %d", Integer.valueOf(this.curSize), Integer.valueOf(this.array.length)));
            }
        }

        @Override // java.util.stream.Nodes.DoubleArrayNode
        public String toString() {
            return String.format("DoubleFixedNodeBuilder[%d][%s]", Integer.valueOf(this.array.length - this.curSize), Arrays.toString(this.array));
        }
    }

    /* loaded from: rt.jar:java/util/stream/Nodes$IntSpinedNodeBuilder.class */
    private static final class IntSpinedNodeBuilder extends SpinedBuffer.OfInt implements Node.OfInt, Node.Builder.OfInt {
        private boolean building = false;
        static final /* synthetic */ boolean $assertionsDisabled;

        static {
            $assertionsDisabled = !Nodes.class.desiredAssertionStatus();
        }

        IntSpinedNodeBuilder() {
        }

        @Override // java.util.stream.SpinedBuffer.OfInt, java.lang.Iterable
        public Spliterator.OfInt spliterator() {
            if ($assertionsDisabled || !this.building) {
                return super.spliterator();
            }
            throw new AssertionError((Object) "during building");
        }

        @Override // java.util.stream.SpinedBuffer.OfPrimitive, java.util.stream.Node.OfPrimitive
        public void forEach(IntConsumer intConsumer) {
            if (!$assertionsDisabled && this.building) {
                throw new AssertionError((Object) "during building");
            }
            super.forEach((IntSpinedNodeBuilder) intConsumer);
        }

        @Override // java.util.stream.Sink
        public void begin(long j2) {
            if (!$assertionsDisabled && this.building) {
                throw new AssertionError((Object) "was already building");
            }
            this.building = true;
            clear();
            ensureCapacity(j2);
        }

        @Override // java.util.stream.SpinedBuffer.OfInt, java.util.function.IntConsumer
        public void accept(int i2) {
            if (!$assertionsDisabled && !this.building) {
                throw new AssertionError((Object) "not building");
            }
            super.accept(i2);
        }

        @Override // java.util.stream.Sink
        public void end() {
            if (!$assertionsDisabled && !this.building) {
                throw new AssertionError((Object) "was not building");
            }
            this.building = false;
        }

        @Override // java.util.stream.SpinedBuffer.OfPrimitive, java.util.stream.Node.OfPrimitive
        public void copyInto(int[] iArr, int i2) throws IndexOutOfBoundsException {
            if (!$assertionsDisabled && this.building) {
                throw new AssertionError((Object) "during building");
            }
            super.copyInto((IntSpinedNodeBuilder) iArr, i2);
        }

        @Override // java.util.stream.SpinedBuffer.OfPrimitive, java.util.stream.Node.OfPrimitive
        public int[] asPrimitiveArray() {
            if ($assertionsDisabled || !this.building) {
                return (int[]) super.asPrimitiveArray();
            }
            throw new AssertionError((Object) "during building");
        }

        @Override // java.util.stream.Node.Builder.OfInt, java.util.stream.Node.Builder
        /* renamed from: build */
        public Node<Integer> build2() {
            if ($assertionsDisabled || !this.building) {
                return this;
            }
            throw new AssertionError((Object) "during building");
        }
    }

    /* loaded from: rt.jar:java/util/stream/Nodes$LongSpinedNodeBuilder.class */
    private static final class LongSpinedNodeBuilder extends SpinedBuffer.OfLong implements Node.OfLong, Node.Builder.OfLong {
        private boolean building = false;
        static final /* synthetic */ boolean $assertionsDisabled;

        static {
            $assertionsDisabled = !Nodes.class.desiredAssertionStatus();
        }

        LongSpinedNodeBuilder() {
        }

        @Override // java.util.stream.SpinedBuffer.OfLong, java.lang.Iterable
        public Spliterator.OfLong spliterator() {
            if ($assertionsDisabled || !this.building) {
                return super.spliterator();
            }
            throw new AssertionError((Object) "during building");
        }

        @Override // java.util.stream.SpinedBuffer.OfPrimitive, java.util.stream.Node.OfPrimitive
        public void forEach(LongConsumer longConsumer) {
            if (!$assertionsDisabled && this.building) {
                throw new AssertionError((Object) "during building");
            }
            super.forEach((LongSpinedNodeBuilder) longConsumer);
        }

        @Override // java.util.stream.Sink
        public void begin(long j2) {
            if (!$assertionsDisabled && this.building) {
                throw new AssertionError((Object) "was already building");
            }
            this.building = true;
            clear();
            ensureCapacity(j2);
        }

        @Override // java.util.stream.SpinedBuffer.OfLong, java.util.function.LongConsumer
        public void accept(long j2) {
            if (!$assertionsDisabled && !this.building) {
                throw new AssertionError((Object) "not building");
            }
            super.accept(j2);
        }

        @Override // java.util.stream.Sink
        public void end() {
            if (!$assertionsDisabled && !this.building) {
                throw new AssertionError((Object) "was not building");
            }
            this.building = false;
        }

        @Override // java.util.stream.SpinedBuffer.OfPrimitive, java.util.stream.Node.OfPrimitive
        public void copyInto(long[] jArr, int i2) {
            if (!$assertionsDisabled && this.building) {
                throw new AssertionError((Object) "during building");
            }
            super.copyInto((LongSpinedNodeBuilder) jArr, i2);
        }

        @Override // java.util.stream.SpinedBuffer.OfPrimitive, java.util.stream.Node.OfPrimitive
        public long[] asPrimitiveArray() {
            if ($assertionsDisabled || !this.building) {
                return (long[]) super.asPrimitiveArray();
            }
            throw new AssertionError((Object) "during building");
        }

        @Override // java.util.stream.Node.Builder.OfLong, java.util.stream.Node.Builder
        /* renamed from: build */
        public Node<Long> build2() {
            if ($assertionsDisabled || !this.building) {
                return this;
            }
            throw new AssertionError((Object) "during building");
        }
    }

    /* loaded from: rt.jar:java/util/stream/Nodes$DoubleSpinedNodeBuilder.class */
    private static final class DoubleSpinedNodeBuilder extends SpinedBuffer.OfDouble implements Node.OfDouble, Node.Builder.OfDouble {
        private boolean building = false;
        static final /* synthetic */ boolean $assertionsDisabled;

        static {
            $assertionsDisabled = !Nodes.class.desiredAssertionStatus();
        }

        DoubleSpinedNodeBuilder() {
        }

        @Override // java.util.stream.SpinedBuffer.OfDouble, java.lang.Iterable
        public Spliterator.OfDouble spliterator() {
            if ($assertionsDisabled || !this.building) {
                return super.spliterator();
            }
            throw new AssertionError((Object) "during building");
        }

        @Override // java.util.stream.SpinedBuffer.OfPrimitive, java.util.stream.Node.OfPrimitive
        public void forEach(DoubleConsumer doubleConsumer) {
            if (!$assertionsDisabled && this.building) {
                throw new AssertionError((Object) "during building");
            }
            super.forEach((DoubleSpinedNodeBuilder) doubleConsumer);
        }

        @Override // java.util.stream.Sink
        public void begin(long j2) {
            if (!$assertionsDisabled && this.building) {
                throw new AssertionError((Object) "was already building");
            }
            this.building = true;
            clear();
            ensureCapacity(j2);
        }

        @Override // java.util.stream.SpinedBuffer.OfDouble, java.util.function.DoubleConsumer
        public void accept(double d2) {
            if (!$assertionsDisabled && !this.building) {
                throw new AssertionError((Object) "not building");
            }
            super.accept(d2);
        }

        @Override // java.util.stream.Sink
        public void end() {
            if (!$assertionsDisabled && !this.building) {
                throw new AssertionError((Object) "was not building");
            }
            this.building = false;
        }

        @Override // java.util.stream.SpinedBuffer.OfPrimitive, java.util.stream.Node.OfPrimitive
        public void copyInto(double[] dArr, int i2) {
            if (!$assertionsDisabled && this.building) {
                throw new AssertionError((Object) "during building");
            }
            super.copyInto((DoubleSpinedNodeBuilder) dArr, i2);
        }

        @Override // java.util.stream.SpinedBuffer.OfPrimitive, java.util.stream.Node.OfPrimitive
        public double[] asPrimitiveArray() {
            if ($assertionsDisabled || !this.building) {
                return (double[]) super.asPrimitiveArray();
            }
            throw new AssertionError((Object) "during building");
        }

        @Override // java.util.stream.Node.Builder.OfDouble, java.util.stream.Node.Builder
        /* renamed from: build */
        public Node<Double> build2() {
            if ($assertionsDisabled || !this.building) {
                return this;
            }
            throw new AssertionError((Object) "during building");
        }
    }

    /* loaded from: rt.jar:java/util/stream/Nodes$SizedCollectorTask.class */
    private static abstract class SizedCollectorTask<P_IN, P_OUT, T_SINK extends Sink<P_OUT>, K extends SizedCollectorTask<P_IN, P_OUT, T_SINK, K>> extends CountedCompleter<Void> implements Sink<P_OUT> {
        protected final Spliterator<P_IN> spliterator;
        protected final PipelineHelper<P_OUT> helper;
        protected final long targetSize;
        protected long offset;
        protected long length;
        protected int index;
        protected int fence;
        static final /* synthetic */ boolean $assertionsDisabled;

        abstract K makeChild(Spliterator<P_IN> spliterator, long j2, long j3);

        static {
            $assertionsDisabled = !Nodes.class.desiredAssertionStatus();
        }

        SizedCollectorTask(Spliterator<P_IN> spliterator, PipelineHelper<P_OUT> pipelineHelper, int i2) {
            if (!$assertionsDisabled && !spliterator.hasCharacteristics(16384)) {
                throw new AssertionError();
            }
            this.spliterator = spliterator;
            this.helper = pipelineHelper;
            this.targetSize = AbstractTask.suggestTargetSize(spliterator.estimateSize());
            this.offset = 0L;
            this.length = i2;
        }

        SizedCollectorTask(K k2, Spliterator<P_IN> spliterator, long j2, long j3, int i2) {
            super(k2);
            if (!$assertionsDisabled && !spliterator.hasCharacteristics(16384)) {
                throw new AssertionError();
            }
            this.spliterator = spliterator;
            this.helper = k2.helper;
            this.targetSize = k2.targetSize;
            this.offset = j2;
            this.length = j3;
            if (j2 < 0 || j3 < 0 || (j2 + j3) - 1 >= i2) {
                throw new IllegalArgumentException(String.format("offset and length interval [%d, %d + %d) is not within array size interval [0, %d)", Long.valueOf(j2), Long.valueOf(j2), Long.valueOf(j3), Integer.valueOf(i2)));
            }
        }

        @Override // java.util.concurrent.CountedCompleter
        public void compute() {
            Spliterator<P_IN> spliteratorTrySplit;
            SizedCollectorTask<P_IN, P_OUT, T_SINK, K> sizedCollectorTaskMakeChild = this;
            Spliterator<P_IN> spliterator = this.spliterator;
            while (spliterator.estimateSize() > sizedCollectorTaskMakeChild.targetSize && (spliteratorTrySplit = spliterator.trySplit()) != null) {
                sizedCollectorTaskMakeChild.setPendingCount(1);
                long jEstimateSize = spliteratorTrySplit.estimateSize();
                sizedCollectorTaskMakeChild.makeChild(spliteratorTrySplit, sizedCollectorTaskMakeChild.offset, jEstimateSize).fork();
                sizedCollectorTaskMakeChild = sizedCollectorTaskMakeChild.makeChild(spliterator, sizedCollectorTaskMakeChild.offset + jEstimateSize, sizedCollectorTaskMakeChild.length - jEstimateSize);
            }
            if (!$assertionsDisabled && sizedCollectorTaskMakeChild.offset + sizedCollectorTaskMakeChild.length >= Nodes.MAX_ARRAY_SIZE) {
                throw new AssertionError();
            }
            sizedCollectorTaskMakeChild.helper.wrapAndCopyInto(sizedCollectorTaskMakeChild, spliterator);
            sizedCollectorTaskMakeChild.propagateCompletion();
        }

        @Override // java.util.stream.Sink
        public void begin(long j2) {
            if (j2 > this.length) {
                throw new IllegalStateException("size passed to Sink.begin exceeds array length");
            }
            this.index = (int) this.offset;
            this.fence = this.index + ((int) this.length);
        }

        /* loaded from: rt.jar:java/util/stream/Nodes$SizedCollectorTask$OfRef.class */
        static final class OfRef<P_IN, P_OUT> extends SizedCollectorTask<P_IN, P_OUT, Sink<P_OUT>, OfRef<P_IN, P_OUT>> implements Sink<P_OUT> {
            private final P_OUT[] array;

            OfRef(Spliterator<P_IN> spliterator, PipelineHelper<P_OUT> pipelineHelper, P_OUT[] p_outArr) {
                super(spliterator, pipelineHelper, p_outArr.length);
                this.array = p_outArr;
            }

            OfRef(OfRef<P_IN, P_OUT> ofRef, Spliterator<P_IN> spliterator, long j2, long j3) {
                super(ofRef, spliterator, j2, j3, ofRef.array.length);
                this.array = ofRef.array;
            }

            /* JADX INFO: Access modifiers changed from: package-private */
            @Override // java.util.stream.Nodes.SizedCollectorTask
            public OfRef<P_IN, P_OUT> makeChild(Spliterator<P_IN> spliterator, long j2, long j3) {
                return new OfRef<>(this, spliterator, j2, j3);
            }

            @Override // java.util.function.Consumer
            public void accept(P_OUT p_out) {
                if (this.index >= this.fence) {
                    throw new IndexOutOfBoundsException(Integer.toString(this.index));
                }
                P_OUT[] p_outArr = this.array;
                int i2 = this.index;
                this.index = i2 + 1;
                p_outArr[i2] = p_out;
            }
        }

        /* loaded from: rt.jar:java/util/stream/Nodes$SizedCollectorTask$OfInt.class */
        static final class OfInt<P_IN> extends SizedCollectorTask<P_IN, Integer, Sink.OfInt, OfInt<P_IN>> implements Sink.OfInt {
            private final int[] array;

            OfInt(Spliterator<P_IN> spliterator, PipelineHelper<Integer> pipelineHelper, int[] iArr) {
                super(spliterator, pipelineHelper, iArr.length);
                this.array = iArr;
            }

            OfInt(OfInt<P_IN> ofInt, Spliterator<P_IN> spliterator, long j2, long j3) {
                super(ofInt, spliterator, j2, j3, ofInt.array.length);
                this.array = ofInt.array;
            }

            /* JADX INFO: Access modifiers changed from: package-private */
            @Override // java.util.stream.Nodes.SizedCollectorTask
            public OfInt<P_IN> makeChild(Spliterator<P_IN> spliterator, long j2, long j3) {
                return new OfInt<>(this, spliterator, j2, j3);
            }

            @Override // java.util.stream.Sink, java.util.stream.Sink.OfInt, java.util.function.IntConsumer
            public void accept(int i2) {
                if (this.index >= this.fence) {
                    throw new IndexOutOfBoundsException(Integer.toString(this.index));
                }
                int[] iArr = this.array;
                int i3 = this.index;
                this.index = i3 + 1;
                iArr[i3] = i2;
            }
        }

        /* loaded from: rt.jar:java/util/stream/Nodes$SizedCollectorTask$OfLong.class */
        static final class OfLong<P_IN> extends SizedCollectorTask<P_IN, Long, Sink.OfLong, OfLong<P_IN>> implements Sink.OfLong {
            private final long[] array;

            OfLong(Spliterator<P_IN> spliterator, PipelineHelper<Long> pipelineHelper, long[] jArr) {
                super(spliterator, pipelineHelper, jArr.length);
                this.array = jArr;
            }

            OfLong(OfLong<P_IN> ofLong, Spliterator<P_IN> spliterator, long j2, long j3) {
                super(ofLong, spliterator, j2, j3, ofLong.array.length);
                this.array = ofLong.array;
            }

            /* JADX INFO: Access modifiers changed from: package-private */
            @Override // java.util.stream.Nodes.SizedCollectorTask
            public OfLong<P_IN> makeChild(Spliterator<P_IN> spliterator, long j2, long j3) {
                return new OfLong<>(this, spliterator, j2, j3);
            }

            @Override // java.util.stream.Sink, java.util.stream.Sink.OfLong, java.util.function.LongConsumer
            public void accept(long j2) {
                if (this.index >= this.fence) {
                    throw new IndexOutOfBoundsException(Integer.toString(this.index));
                }
                long[] jArr = this.array;
                int i2 = this.index;
                this.index = i2 + 1;
                jArr[i2] = j2;
            }
        }

        /* loaded from: rt.jar:java/util/stream/Nodes$SizedCollectorTask$OfDouble.class */
        static final class OfDouble<P_IN> extends SizedCollectorTask<P_IN, Double, Sink.OfDouble, OfDouble<P_IN>> implements Sink.OfDouble {
            private final double[] array;

            OfDouble(Spliterator<P_IN> spliterator, PipelineHelper<Double> pipelineHelper, double[] dArr) {
                super(spliterator, pipelineHelper, dArr.length);
                this.array = dArr;
            }

            OfDouble(OfDouble<P_IN> ofDouble, Spliterator<P_IN> spliterator, long j2, long j3) {
                super(ofDouble, spliterator, j2, j3, ofDouble.array.length);
                this.array = ofDouble.array;
            }

            /* JADX INFO: Access modifiers changed from: package-private */
            @Override // java.util.stream.Nodes.SizedCollectorTask
            public OfDouble<P_IN> makeChild(Spliterator<P_IN> spliterator, long j2, long j3) {
                return new OfDouble<>(this, spliterator, j2, j3);
            }

            @Override // java.util.stream.Sink, java.util.function.DoubleConsumer
            public void accept(double d2) {
                if (this.index >= this.fence) {
                    throw new IndexOutOfBoundsException(Integer.toString(this.index));
                }
                double[] dArr = this.array;
                int i2 = this.index;
                this.index = i2 + 1;
                dArr[i2] = d2;
            }
        }
    }

    /* loaded from: rt.jar:java/util/stream/Nodes$ToArrayTask.class */
    private static abstract class ToArrayTask<T, T_NODE extends Node<T>, K extends ToArrayTask<T, T_NODE, K>> extends CountedCompleter<Void> {
        protected final T_NODE node;
        protected final int offset;

        abstract void copyNodeToArray();

        abstract K makeChild(int i2, int i3);

        ToArrayTask(T_NODE t_node, int i2) {
            this.node = t_node;
            this.offset = i2;
        }

        ToArrayTask(K k2, T_NODE t_node, int i2) {
            super(k2);
            this.node = t_node;
            this.offset = i2;
        }

        @Override // java.util.concurrent.CountedCompleter
        public void compute() {
            ToArrayTask<T, T_NODE, K> toArrayTaskMakeChild = this;
            while (true) {
                ToArrayTask<T, T_NODE, K> toArrayTask = toArrayTaskMakeChild;
                if (toArrayTask.node.getChildCount() == 0) {
                    toArrayTask.copyNodeToArray();
                    toArrayTask.propagateCompletion();
                    return;
                }
                toArrayTask.setPendingCount(toArrayTask.node.getChildCount() - 1);
                int iCount = 0;
                int i2 = 0;
                while (i2 < toArrayTask.node.getChildCount() - 1) {
                    ToArrayTask toArrayTaskMakeChild2 = toArrayTask.makeChild(i2, toArrayTask.offset + iCount);
                    iCount = (int) (iCount + toArrayTaskMakeChild2.node.count());
                    toArrayTaskMakeChild2.fork();
                    i2++;
                }
                toArrayTaskMakeChild = toArrayTask.makeChild(i2, toArrayTask.offset + iCount);
            }
        }

        /* loaded from: rt.jar:java/util/stream/Nodes$ToArrayTask$OfRef.class */
        private static final class OfRef<T> extends ToArrayTask<T, Node<T>, OfRef<T>> {
            private final T[] array;

            private OfRef(Node<T> node, T[] tArr, int i2) {
                super(node, i2);
                this.array = tArr;
            }

            private OfRef(OfRef<T> ofRef, Node<T> node, int i2) {
                super(ofRef, node, i2);
                this.array = ofRef.array;
            }

            /* JADX INFO: Access modifiers changed from: package-private */
            @Override // java.util.stream.Nodes.ToArrayTask
            public OfRef<T> makeChild(int i2, int i3) {
                return new OfRef<>(this, this.node.getChild(i2), i3);
            }

            @Override // java.util.stream.Nodes.ToArrayTask
            void copyNodeToArray() {
                this.node.copyInto(this.array, this.offset);
            }
        }

        /* loaded from: rt.jar:java/util/stream/Nodes$ToArrayTask$OfPrimitive.class */
        private static class OfPrimitive<T, T_CONS, T_ARR, T_SPLITR extends Spliterator.OfPrimitive<T, T_CONS, T_SPLITR>, T_NODE extends Node.OfPrimitive<T, T_CONS, T_ARR, T_SPLITR, T_NODE>> extends ToArrayTask<T, T_NODE, OfPrimitive<T, T_CONS, T_ARR, T_SPLITR, T_NODE>> {
            private final T_ARR array;

            private OfPrimitive(T_NODE t_node, T_ARR t_arr, int i2) {
                super(t_node, i2);
                this.array = t_arr;
            }

            private OfPrimitive(OfPrimitive<T, T_CONS, T_ARR, T_SPLITR, T_NODE> ofPrimitive, T_NODE t_node, int i2) {
                super(ofPrimitive, t_node, i2);
                this.array = ofPrimitive.array;
            }

            /* JADX INFO: Access modifiers changed from: package-private */
            @Override // java.util.stream.Nodes.ToArrayTask
            public OfPrimitive<T, T_CONS, T_ARR, T_SPLITR, T_NODE> makeChild(int i2, int i3) {
                return new OfPrimitive<>(this, ((Node.OfPrimitive) this.node).getChild(i2), i3);
            }

            @Override // java.util.stream.Nodes.ToArrayTask
            void copyNodeToArray() {
                ((Node.OfPrimitive) this.node).copyInto((Node.OfPrimitive) this.array, this.offset);
            }
        }

        /* loaded from: rt.jar:java/util/stream/Nodes$ToArrayTask$OfInt.class */
        private static final class OfInt extends OfPrimitive<Integer, IntConsumer, int[], Spliterator.OfInt, Node.OfInt> {
            private OfInt(Node.OfInt ofInt, int[] iArr, int i2) {
                super(ofInt, iArr, i2);
            }
        }

        /* loaded from: rt.jar:java/util/stream/Nodes$ToArrayTask$OfLong.class */
        private static final class OfLong extends OfPrimitive<Long, LongConsumer, long[], Spliterator.OfLong, Node.OfLong> {
            private OfLong(Node.OfLong ofLong, long[] jArr, int i2) {
                super(ofLong, jArr, i2);
            }
        }

        /* loaded from: rt.jar:java/util/stream/Nodes$ToArrayTask$OfDouble.class */
        private static final class OfDouble extends OfPrimitive<Double, DoubleConsumer, double[], Spliterator.OfDouble, Node.OfDouble> {
            private OfDouble(Node.OfDouble ofDouble, double[] dArr, int i2) {
                super(ofDouble, dArr, i2);
            }
        }
    }

    /* loaded from: rt.jar:java/util/stream/Nodes$CollectorTask.class */
    private static class CollectorTask<P_IN, P_OUT, T_NODE extends Node<P_OUT>, T_BUILDER extends Node.Builder<P_OUT>> extends AbstractTask<P_IN, P_OUT, T_NODE, CollectorTask<P_IN, P_OUT, T_NODE, T_BUILDER>> {
        protected final PipelineHelper<P_OUT> helper;
        protected final LongFunction<T_BUILDER> builderFactory;
        protected final BinaryOperator<T_NODE> concFactory;

        CollectorTask(PipelineHelper<P_OUT> pipelineHelper, Spliterator<P_IN> spliterator, LongFunction<T_BUILDER> longFunction, BinaryOperator<T_NODE> binaryOperator) {
            super(pipelineHelper, spliterator);
            this.helper = pipelineHelper;
            this.builderFactory = longFunction;
            this.concFactory = binaryOperator;
        }

        CollectorTask(CollectorTask<P_IN, P_OUT, T_NODE, T_BUILDER> collectorTask, Spliterator<P_IN> spliterator) {
            super(collectorTask, spliterator);
            this.helper = collectorTask.helper;
            this.builderFactory = collectorTask.builderFactory;
            this.concFactory = collectorTask.concFactory;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // java.util.stream.AbstractTask
        public CollectorTask<P_IN, P_OUT, T_NODE, T_BUILDER> makeChild(Spliterator<P_IN> spliterator) {
            return new CollectorTask<>(this, spliterator);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // java.util.stream.AbstractTask
        public T_NODE doLeaf() {
            return (T_NODE) ((Node.Builder) this.helper.wrapAndCopyInto(this.builderFactory.apply(this.helper.exactOutputSizeIfKnown(this.spliterator)), this.spliterator)).build2();
        }

        @Override // java.util.stream.AbstractTask, java.util.concurrent.CountedCompleter
        public void onCompletion(CountedCompleter<?> countedCompleter) {
            if (!isLeaf()) {
                setLocalResult(this.concFactory.apply(((CollectorTask) this.leftChild).getLocalResult(), ((CollectorTask) this.rightChild).getLocalResult()));
            }
            super.onCompletion(countedCompleter);
        }

        /* loaded from: rt.jar:java/util/stream/Nodes$CollectorTask$OfRef.class */
        private static final class OfRef<P_IN, P_OUT> extends CollectorTask<P_IN, P_OUT, Node<P_OUT>, Node.Builder<P_OUT>> {
            @Override // java.util.stream.Nodes.CollectorTask, java.util.stream.AbstractTask
            protected /* bridge */ /* synthetic */ Object doLeaf() {
                return super.doLeaf();
            }

            @Override // java.util.stream.Nodes.CollectorTask, java.util.stream.AbstractTask
            protected /* bridge */ /* synthetic */ AbstractTask makeChild(Spliterator spliterator) {
                return super.makeChild(spliterator);
            }

            OfRef(PipelineHelper<P_OUT> pipelineHelper, IntFunction<P_OUT[]> intFunction, Spliterator<P_IN> spliterator) {
                super(pipelineHelper, spliterator, j2 -> {
                    return Nodes.builder(j2, intFunction);
                }, ConcNode::new);
            }
        }

        /* loaded from: rt.jar:java/util/stream/Nodes$CollectorTask$OfInt.class */
        private static final class OfInt<P_IN> extends CollectorTask<P_IN, Integer, Node.OfInt, Node.Builder.OfInt> {
            @Override // java.util.stream.Nodes.CollectorTask, java.util.stream.AbstractTask
            protected /* bridge */ /* synthetic */ Object doLeaf() {
                return super.doLeaf();
            }

            @Override // java.util.stream.Nodes.CollectorTask, java.util.stream.AbstractTask
            protected /* bridge */ /* synthetic */ AbstractTask makeChild(Spliterator spliterator) {
                return super.makeChild(spliterator);
            }

            OfInt(PipelineHelper<Integer> pipelineHelper, Spliterator<P_IN> spliterator) {
                super(pipelineHelper, spliterator, Nodes::intBuilder, ConcNode.OfInt::new);
            }
        }

        /* loaded from: rt.jar:java/util/stream/Nodes$CollectorTask$OfLong.class */
        private static final class OfLong<P_IN> extends CollectorTask<P_IN, Long, Node.OfLong, Node.Builder.OfLong> {
            @Override // java.util.stream.Nodes.CollectorTask, java.util.stream.AbstractTask
            protected /* bridge */ /* synthetic */ Object doLeaf() {
                return super.doLeaf();
            }

            @Override // java.util.stream.Nodes.CollectorTask, java.util.stream.AbstractTask
            protected /* bridge */ /* synthetic */ AbstractTask makeChild(Spliterator spliterator) {
                return super.makeChild(spliterator);
            }

            OfLong(PipelineHelper<Long> pipelineHelper, Spliterator<P_IN> spliterator) {
                super(pipelineHelper, spliterator, Nodes::longBuilder, ConcNode.OfLong::new);
            }
        }

        /* loaded from: rt.jar:java/util/stream/Nodes$CollectorTask$OfDouble.class */
        private static final class OfDouble<P_IN> extends CollectorTask<P_IN, Double, Node.OfDouble, Node.Builder.OfDouble> {
            @Override // java.util.stream.Nodes.CollectorTask, java.util.stream.AbstractTask
            protected /* bridge */ /* synthetic */ Object doLeaf() {
                return super.doLeaf();
            }

            @Override // java.util.stream.Nodes.CollectorTask, java.util.stream.AbstractTask
            protected /* bridge */ /* synthetic */ AbstractTask makeChild(Spliterator spliterator) {
                return super.makeChild(spliterator);
            }

            OfDouble(PipelineHelper<Double> pipelineHelper, Spliterator<P_IN> spliterator) {
                super(pipelineHelper, spliterator, Nodes::doubleBuilder, ConcNode.OfDouble::new);
            }
        }
    }
}
