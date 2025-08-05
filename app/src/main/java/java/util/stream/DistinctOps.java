package java.util.stream;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;
import java.util.Spliterator;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;
import java.util.function.IntFunction;
import java.util.stream.ReferencePipeline;
import java.util.stream.Sink;
import java.util.stream.StreamSpliterators;

/* loaded from: rt.jar:java/util/stream/DistinctOps.class */
final class DistinctOps {
    private DistinctOps() {
    }

    static <T> ReferencePipeline<T, T> makeRef(AbstractPipeline<?, T, ?> abstractPipeline) {
        return new ReferencePipeline.StatefulOp<T, T>(abstractPipeline, StreamShape.REFERENCE, StreamOpFlag.IS_DISTINCT | StreamOpFlag.NOT_SIZED) { // from class: java.util.stream.DistinctOps.1
            <P_IN> Node<T> reduce(PipelineHelper<T> pipelineHelper, Spliterator<P_IN> spliterator) {
                return Nodes.node((Collection) ReduceOps.makeRef(LinkedHashSet::new, (v0, v1) -> {
                    v0.add(v1);
                }, (v0, v1) -> {
                    v0.addAll(v1);
                }).evaluateParallel(pipelineHelper, spliterator));
            }

            @Override // java.util.stream.ReferencePipeline.StatefulOp, java.util.stream.AbstractPipeline
            <P_IN> Node<T> opEvaluateParallel(PipelineHelper<T> pipelineHelper, Spliterator<P_IN> spliterator, IntFunction<T[]> intFunction) {
                if (StreamOpFlag.DISTINCT.isKnown(pipelineHelper.getStreamAndOpFlags())) {
                    return pipelineHelper.evaluate(spliterator, false, intFunction);
                }
                if (StreamOpFlag.ORDERED.isKnown(pipelineHelper.getStreamAndOpFlags())) {
                    return reduce(pipelineHelper, spliterator);
                }
                AtomicBoolean atomicBoolean = new AtomicBoolean(false);
                ConcurrentHashMap concurrentHashMap = new ConcurrentHashMap();
                ForEachOps.makeRef(obj -> {
                    if (obj == null) {
                        atomicBoolean.set(true);
                    } else {
                        concurrentHashMap.putIfAbsent(obj, Boolean.TRUE);
                    }
                }, false).evaluateParallel(pipelineHelper, spliterator);
                Set setKeySet = concurrentHashMap.keySet();
                if (atomicBoolean.get()) {
                    setKeySet = new HashSet(setKeySet);
                    setKeySet.add(null);
                }
                return Nodes.node(setKeySet);
            }

            @Override // java.util.stream.AbstractPipeline
            <P_IN> Spliterator<T> opEvaluateParallelLazy(PipelineHelper<T> pipelineHelper, Spliterator<P_IN> spliterator) {
                if (StreamOpFlag.DISTINCT.isKnown(pipelineHelper.getStreamAndOpFlags())) {
                    return pipelineHelper.wrapSpliterator(spliterator);
                }
                if (StreamOpFlag.ORDERED.isKnown(pipelineHelper.getStreamAndOpFlags())) {
                    return reduce(pipelineHelper, spliterator).spliterator();
                }
                return new StreamSpliterators.DistinctSpliterator(pipelineHelper.wrapSpliterator(spliterator));
            }

            @Override // java.util.stream.AbstractPipeline
            Sink<T> opWrapSink(int i2, Sink<T> sink) {
                Objects.requireNonNull(sink);
                if (StreamOpFlag.DISTINCT.isKnown(i2)) {
                    return sink;
                }
                if (StreamOpFlag.SORTED.isKnown(i2)) {
                    return new Sink.ChainedReference<T, T>(sink) { // from class: java.util.stream.DistinctOps.1.1
                        boolean seenNull;
                        T lastSeen;

                        @Override // java.util.stream.Sink.ChainedReference, java.util.stream.Sink
                        public void begin(long j2) {
                            this.seenNull = false;
                            this.lastSeen = null;
                            this.downstream.begin(-1L);
                        }

                        @Override // java.util.stream.Sink.ChainedReference, java.util.stream.Sink
                        public void end() {
                            this.seenNull = false;
                            this.lastSeen = null;
                            this.downstream.end();
                        }

                        @Override // java.util.function.Consumer
                        public void accept(T t2) {
                            if (t2 == null) {
                                if (!this.seenNull) {
                                    this.seenNull = true;
                                    Consumer consumer = this.downstream;
                                    this.lastSeen = null;
                                    consumer.accept(null);
                                    return;
                                }
                                return;
                            }
                            if (this.lastSeen == null || !t2.equals(this.lastSeen)) {
                                Consumer consumer2 = this.downstream;
                                this.lastSeen = t2;
                                consumer2.accept(t2);
                            }
                        }
                    };
                }
                return new Sink.ChainedReference<T, T>(sink) { // from class: java.util.stream.DistinctOps.1.2
                    Set<T> seen;

                    @Override // java.util.stream.Sink.ChainedReference, java.util.stream.Sink
                    public void begin(long j2) {
                        this.seen = new HashSet();
                        this.downstream.begin(-1L);
                    }

                    @Override // java.util.stream.Sink.ChainedReference, java.util.stream.Sink
                    public void end() {
                        this.seen = null;
                        this.downstream.end();
                    }

                    @Override // java.util.function.Consumer
                    public void accept(T t2) {
                        if (!this.seen.contains(t2)) {
                            this.seen.add(t2);
                            this.downstream.accept(t2);
                        }
                    }
                };
            }
        };
    }
}
