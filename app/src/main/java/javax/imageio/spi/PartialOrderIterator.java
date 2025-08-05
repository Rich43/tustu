package javax.imageio.spi;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;

/* compiled from: PartiallyOrderedSet.java */
/* loaded from: rt.jar:javax/imageio/spi/PartialOrderIterator.class */
class PartialOrderIterator implements Iterator {
    LinkedList zeroList = new LinkedList();
    Map inDegrees = new HashMap();

    public PartialOrderIterator(Iterator it) {
        while (it.hasNext()) {
            DigraphNode digraphNode = (DigraphNode) it.next();
            int inDegree = digraphNode.getInDegree();
            this.inDegrees.put(digraphNode, new Integer(inDegree));
            if (inDegree == 0) {
                this.zeroList.add(digraphNode);
            }
        }
    }

    @Override // java.util.Iterator
    public boolean hasNext() {
        return !this.zeroList.isEmpty();
    }

    @Override // java.util.Iterator
    public Object next() {
        DigraphNode digraphNode = (DigraphNode) this.zeroList.removeFirst();
        Iterator outNodes = digraphNode.getOutNodes();
        while (outNodes.hasNext()) {
            DigraphNode digraphNode2 = (DigraphNode) outNodes.next();
            int iIntValue = ((Integer) this.inDegrees.get(digraphNode2)).intValue() - 1;
            this.inDegrees.put(digraphNode2, new Integer(iIntValue));
            if (iIntValue == 0) {
                this.zeroList.add(digraphNode2);
            }
        }
        return digraphNode.getData();
    }

    @Override // java.util.Iterator
    public void remove() {
        throw new UnsupportedOperationException();
    }
}
