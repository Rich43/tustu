package javax.imageio.metadata;

import org.w3c.dom.Attr;
import org.w3c.dom.Element;

/* compiled from: IIOMetadataNode.java */
/* loaded from: rt.jar:javax/imageio/metadata/IIOAttr.class */
class IIOAttr extends IIOMetadataNode implements Attr {
    Element owner;
    String name;
    String value;

    public IIOAttr(Element element, String str, String str2) {
        this.owner = element;
        this.name = str;
        this.value = str2;
    }

    @Override // org.w3c.dom.Attr
    public String getName() {
        return this.name;
    }

    @Override // javax.imageio.metadata.IIOMetadataNode, org.w3c.dom.Node
    public String getNodeName() {
        return this.name;
    }

    @Override // javax.imageio.metadata.IIOMetadataNode, org.w3c.dom.Node
    public short getNodeType() {
        return (short) 2;
    }

    @Override // org.w3c.dom.Attr
    public boolean getSpecified() {
        return true;
    }

    @Override // org.w3c.dom.Attr
    public String getValue() {
        return this.value;
    }

    @Override // javax.imageio.metadata.IIOMetadataNode, org.w3c.dom.Node
    public String getNodeValue() {
        return this.value;
    }

    @Override // org.w3c.dom.Attr
    public void setValue(String str) {
        this.value = str;
    }

    @Override // javax.imageio.metadata.IIOMetadataNode, org.w3c.dom.Node
    public void setNodeValue(String str) {
        this.value = str;
    }

    @Override // org.w3c.dom.Attr
    public Element getOwnerElement() {
        return this.owner;
    }

    public void setOwnerElement(Element element) {
        this.owner = element;
    }

    @Override // org.w3c.dom.Attr
    public boolean isId() {
        return false;
    }
}
