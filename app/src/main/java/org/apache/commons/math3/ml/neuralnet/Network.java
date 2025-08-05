package org.apache.commons.math3.ml.neuralnet;

import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.MathIllegalStateException;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/ml/neuralnet/Network.class */
public class Network implements Iterable<Neuron>, Serializable {
    private static final long serialVersionUID = 20130207;
    private final AtomicLong nextId;
    private final int featureSize;
    private final ConcurrentHashMap<Long, Neuron> neuronMap = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<Long, Set<Long>> linkMap = new ConcurrentHashMap<>();

    /* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/ml/neuralnet/Network$NeuronIdentifierComparator.class */
    public static class NeuronIdentifierComparator implements Comparator<Neuron>, Serializable {
        private static final long serialVersionUID = 20130207;

        @Override // java.util.Comparator
        public int compare(Neuron a2, Neuron b2) {
            long aId = a2.getIdentifier();
            long bId = b2.getIdentifier();
            if (aId < bId) {
                return -1;
            }
            return aId > bId ? 1 : 0;
        }
    }

    Network(long nextId, int featureSize, Neuron[] neuronList, long[][] neighbourIdList) {
        int numNeurons = neuronList.length;
        if (numNeurons != neighbourIdList.length) {
            throw new MathIllegalStateException();
        }
        for (Neuron n2 : neuronList) {
            long id = n2.getIdentifier();
            if (id >= nextId) {
                throw new MathIllegalStateException();
            }
            this.neuronMap.put(Long.valueOf(id), n2);
            this.linkMap.put(Long.valueOf(id), new HashSet());
        }
        for (int i2 = 0; i2 < numNeurons; i2++) {
            long aId = neuronList[i2].getIdentifier();
            Set<Long> aLinks = this.linkMap.get(Long.valueOf(aId));
            long[] arr$ = neighbourIdList[i2];
            for (long j2 : arr$) {
                Long bId = Long.valueOf(j2);
                if (this.neuronMap.get(bId) == null) {
                    throw new MathIllegalStateException();
                }
                addLinkToLinkSet(aLinks, bId.longValue());
            }
        }
        this.nextId = new AtomicLong(nextId);
        this.featureSize = featureSize;
    }

    public Network(long initialIdentifier, int featureSize) {
        this.nextId = new AtomicLong(initialIdentifier);
        this.featureSize = featureSize;
    }

    public synchronized Network copy() {
        Network copy = new Network(this.nextId.get(), this.featureSize);
        for (Map.Entry<Long, Neuron> e2 : this.neuronMap.entrySet()) {
            copy.neuronMap.put(e2.getKey(), e2.getValue().copy());
        }
        for (Map.Entry<Long, Set<Long>> e3 : this.linkMap.entrySet()) {
            copy.linkMap.put(e3.getKey(), new HashSet(e3.getValue()));
        }
        return copy;
    }

    @Override // java.lang.Iterable, java.util.List
    public Iterator<Neuron> iterator() {
        return this.neuronMap.values().iterator();
    }

    public Collection<Neuron> getNeurons(Comparator<Neuron> comparator) {
        List<Neuron> neurons = new ArrayList<>();
        neurons.addAll(this.neuronMap.values());
        Collections.sort(neurons, comparator);
        return neurons;
    }

    public long createNeuron(double[] features) {
        if (features.length != this.featureSize) {
            throw new DimensionMismatchException(features.length, this.featureSize);
        }
        long id = createNextId().longValue();
        this.neuronMap.put(Long.valueOf(id), new Neuron(id, features));
        this.linkMap.put(Long.valueOf(id), new HashSet());
        return id;
    }

    public void deleteNeuron(Neuron neuron) {
        Collection<Neuron> neighbours = getNeighbours(neuron);
        for (Neuron n2 : neighbours) {
            deleteLink(n2, neuron);
        }
        this.neuronMap.remove(Long.valueOf(neuron.getIdentifier()));
    }

    public int getFeaturesSize() {
        return this.featureSize;
    }

    public void addLink(Neuron a2, Neuron b2) {
        long aId = a2.getIdentifier();
        long bId = b2.getIdentifier();
        if (a2 != getNeuron(aId)) {
            throw new NoSuchElementException(Long.toString(aId));
        }
        if (b2 != getNeuron(bId)) {
            throw new NoSuchElementException(Long.toString(bId));
        }
        addLinkToLinkSet(this.linkMap.get(Long.valueOf(aId)), bId);
    }

    private void addLinkToLinkSet(Set<Long> linkSet, long id) {
        linkSet.add(Long.valueOf(id));
    }

    public void deleteLink(Neuron a2, Neuron b2) {
        long aId = a2.getIdentifier();
        long bId = b2.getIdentifier();
        if (a2 != getNeuron(aId)) {
            throw new NoSuchElementException(Long.toString(aId));
        }
        if (b2 != getNeuron(bId)) {
            throw new NoSuchElementException(Long.toString(bId));
        }
        deleteLinkFromLinkSet(this.linkMap.get(Long.valueOf(aId)), bId);
    }

    private void deleteLinkFromLinkSet(Set<Long> linkSet, long id) {
        linkSet.remove(Long.valueOf(id));
    }

    public Neuron getNeuron(long id) {
        Neuron n2 = this.neuronMap.get(Long.valueOf(id));
        if (n2 == null) {
            throw new NoSuchElementException(Long.toString(id));
        }
        return n2;
    }

    public Collection<Neuron> getNeighbours(Iterable<Neuron> neurons) {
        return getNeighbours(neurons, (Iterable<Neuron>) null);
    }

    public Collection<Neuron> getNeighbours(Iterable<Neuron> neurons, Iterable<Neuron> exclude) {
        Set<Long> idList = new HashSet<>();
        for (Neuron n2 : neurons) {
            idList.addAll(this.linkMap.get(Long.valueOf(n2.getIdentifier())));
        }
        if (exclude != null) {
            for (Neuron n3 : exclude) {
                idList.remove(Long.valueOf(n3.getIdentifier()));
            }
        }
        List<Neuron> neuronList = new ArrayList<>();
        for (Long id : idList) {
            neuronList.add(getNeuron(id.longValue()));
        }
        return neuronList;
    }

    public Collection<Neuron> getNeighbours(Neuron neuron) {
        return getNeighbours(neuron, (Iterable<Neuron>) null);
    }

    public Collection<Neuron> getNeighbours(Neuron neuron, Iterable<Neuron> exclude) {
        Set<Long> idList = this.linkMap.get(Long.valueOf(neuron.getIdentifier()));
        if (exclude != null) {
            for (Neuron n2 : exclude) {
                idList.remove(Long.valueOf(n2.getIdentifier()));
            }
        }
        List<Neuron> neuronList = new ArrayList<>();
        for (Long id : idList) {
            neuronList.add(getNeuron(id.longValue()));
        }
        return neuronList;
    }

    private Long createNextId() {
        return Long.valueOf(this.nextId.getAndIncrement());
    }

    private void readObject(ObjectInputStream in) {
        throw new IllegalStateException();
    }

    /* JADX WARN: Type inference failed for: r0v7, types: [long[], long[][]] */
    private Object writeReplace() {
        Neuron[] neuronList = (Neuron[]) this.neuronMap.values().toArray(new Neuron[0]);
        ?? r0 = new long[neuronList.length];
        for (int i2 = 0; i2 < neuronList.length; i2++) {
            Collection<Neuron> neighbours = getNeighbours(neuronList[i2]);
            long[] neighboursId = new long[neighbours.size()];
            int count = 0;
            for (Neuron n2 : neighbours) {
                neighboursId[count] = n2.getIdentifier();
                count++;
            }
            r0[i2] = neighboursId;
        }
        return new SerializationProxy(this.nextId.get(), this.featureSize, neuronList, r0);
    }

    /* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/ml/neuralnet/Network$SerializationProxy.class */
    private static class SerializationProxy implements Serializable {
        private static final long serialVersionUID = 20130207;
        private final long nextId;
        private final int featureSize;
        private final Neuron[] neuronList;
        private final long[][] neighbourIdList;

        SerializationProxy(long nextId, int featureSize, Neuron[] neuronList, long[][] neighbourIdList) {
            this.nextId = nextId;
            this.featureSize = featureSize;
            this.neuronList = neuronList;
            this.neighbourIdList = neighbourIdList;
        }

        private Object readResolve() {
            return new Network(this.nextId, this.featureSize, this.neuronList, this.neighbourIdList);
        }
    }
}
