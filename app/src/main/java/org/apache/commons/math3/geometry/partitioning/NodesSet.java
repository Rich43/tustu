package org.apache.commons.math3.geometry.partitioning;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.math3.geometry.Space;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/geometry/partitioning/NodesSet.class */
public class NodesSet<S extends Space> implements Iterable<BSPTree<S>> {
    private List<BSPTree<S>> list = new ArrayList();

    public void add(BSPTree<S> node) {
        for (BSPTree<S> existing : this.list) {
            if (node == existing) {
                return;
            }
        }
        this.list.add(node);
    }

    public void addAll(Iterable<BSPTree<S>> iterator) {
        for (BSPTree<S> node : iterator) {
            add(node);
        }
    }

    @Override // java.lang.Iterable, java.util.List
    public Iterator<BSPTree<S>> iterator() {
        return this.list.iterator();
    }
}
