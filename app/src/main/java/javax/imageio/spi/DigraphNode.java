package javax.imageio.spi;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/* loaded from: rt.jar:javax/imageio/spi/DigraphNode.class */
class DigraphNode implements Cloneable, Serializable {
    protected Object data;
    protected Set outNodes = new HashSet();
    protected int inDegree = 0;
    private Set inNodes = new HashSet();

    public DigraphNode(Object obj) {
        this.data = obj;
    }

    public Object getData() {
        return this.data;
    }

    public Iterator getOutNodes() {
        return this.outNodes.iterator();
    }

    public boolean addEdge(DigraphNode digraphNode) {
        if (this.outNodes.contains(digraphNode)) {
            return false;
        }
        this.outNodes.add(digraphNode);
        digraphNode.inNodes.add(this);
        digraphNode.incrementInDegree();
        return true;
    }

    public boolean hasEdge(DigraphNode digraphNode) {
        return this.outNodes.contains(digraphNode);
    }

    public boolean removeEdge(DigraphNode digraphNode) {
        if (!this.outNodes.contains(digraphNode)) {
            return false;
        }
        this.outNodes.remove(digraphNode);
        digraphNode.inNodes.remove(this);
        digraphNode.decrementInDegree();
        return true;
    }

    public void dispose() {
        for (Object obj : this.inNodes.toArray()) {
            ((DigraphNode) obj).removeEdge(this);
        }
        for (Object obj2 : this.outNodes.toArray()) {
            removeEdge((DigraphNode) obj2);
        }
    }

    public int getInDegree() {
        return this.inDegree;
    }

    private void incrementInDegree() {
        this.inDegree++;
    }

    private void decrementInDegree() {
        this.inDegree--;
    }
}
