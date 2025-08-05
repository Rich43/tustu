package javax.imageio.metadata;

import java.util.List;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/* compiled from: IIOMetadataNode.java */
/* loaded from: rt.jar:javax/imageio/metadata/IIONodeList.class */
class IIONodeList implements NodeList {
    List nodes;

    public IIONodeList(List list) {
        this.nodes = list;
    }

    @Override // org.w3c.dom.NodeList
    public int getLength() {
        return this.nodes.size();
    }

    @Override // org.w3c.dom.NodeList
    public Node item(int i2) {
        if (i2 < 0 || i2 >= this.nodes.size()) {
            return null;
        }
        return (Node) this.nodes.get(i2);
    }
}
