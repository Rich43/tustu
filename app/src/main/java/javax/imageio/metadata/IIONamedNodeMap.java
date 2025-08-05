package javax.imageio.metadata;

import java.util.List;
import org.w3c.dom.DOMException;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

/* compiled from: IIOMetadataNode.java */
/* loaded from: rt.jar:javax/imageio/metadata/IIONamedNodeMap.class */
class IIONamedNodeMap implements NamedNodeMap {
    List nodes;

    public IIONamedNodeMap(List list) {
        this.nodes = list;
    }

    @Override // org.w3c.dom.NamedNodeMap
    public int getLength() {
        return this.nodes.size();
    }

    @Override // org.w3c.dom.NamedNodeMap
    public Node getNamedItem(String str) {
        for (Node node : this.nodes) {
            if (str.equals(node.getNodeName())) {
                return node;
            }
        }
        return null;
    }

    @Override // org.w3c.dom.NamedNodeMap
    public Node item(int i2) {
        return (Node) this.nodes.get(i2);
    }

    @Override // org.w3c.dom.NamedNodeMap
    public Node removeNamedItem(String str) {
        throw new DOMException((short) 7, "This NamedNodeMap is read-only!");
    }

    @Override // org.w3c.dom.NamedNodeMap
    public Node setNamedItem(Node node) {
        throw new DOMException((short) 7, "This NamedNodeMap is read-only!");
    }

    @Override // org.w3c.dom.NamedNodeMap
    public Node getNamedItemNS(String str, String str2) {
        return getNamedItem(str2);
    }

    @Override // org.w3c.dom.NamedNodeMap
    public Node setNamedItemNS(Node node) {
        return setNamedItem(node);
    }

    @Override // org.w3c.dom.NamedNodeMap
    public Node removeNamedItemNS(String str, String str2) {
        return removeNamedItem(str2);
    }
}
