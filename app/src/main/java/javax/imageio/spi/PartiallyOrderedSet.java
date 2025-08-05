package javax.imageio.spi;

import java.util.AbstractSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/* loaded from: rt.jar:javax/imageio/spi/PartiallyOrderedSet.class */
class PartiallyOrderedSet extends AbstractSet {
    private Map poNodes = new HashMap();
    private Set nodes = this.poNodes.keySet();

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public int size() {
        return this.nodes.size();
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public boolean contains(Object obj) {
        return this.nodes.contains(obj);
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.List
    public Iterator iterator() {
        return new PartialOrderIterator(this.poNodes.values().iterator());
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
    public boolean add(Object obj) {
        if (this.nodes.contains(obj)) {
            return false;
        }
        this.poNodes.put(obj, new DigraphNode(obj));
        return true;
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public boolean remove(Object obj) {
        DigraphNode digraphNode = (DigraphNode) this.poNodes.get(obj);
        if (digraphNode == null) {
            return false;
        }
        this.poNodes.remove(obj);
        digraphNode.dispose();
        return true;
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
    public void clear() {
        this.poNodes.clear();
    }

    public boolean setOrdering(Object obj, Object obj2) {
        DigraphNode digraphNode = (DigraphNode) this.poNodes.get(obj);
        DigraphNode digraphNode2 = (DigraphNode) this.poNodes.get(obj2);
        digraphNode2.removeEdge(digraphNode);
        return digraphNode.addEdge(digraphNode2);
    }

    public boolean unsetOrdering(Object obj, Object obj2) {
        DigraphNode digraphNode = (DigraphNode) this.poNodes.get(obj);
        DigraphNode digraphNode2 = (DigraphNode) this.poNodes.get(obj2);
        return digraphNode.removeEdge(digraphNode2) || digraphNode2.removeEdge(digraphNode);
    }

    public boolean hasOrdering(Object obj, Object obj2) {
        return ((DigraphNode) this.poNodes.get(obj)).hasEdge((DigraphNode) this.poNodes.get(obj2));
    }
}
