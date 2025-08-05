package com.sun.imageio.plugins.wbmp;

import com.sun.imageio.plugins.common.I18N;
import com.sun.imageio.plugins.common.ImageUtil;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.metadata.IIOMetadataFormatImpl;
import javax.imageio.metadata.IIOMetadataNode;
import org.w3c.dom.DOMException;
import org.w3c.dom.Node;

/* loaded from: rt.jar:com/sun/imageio/plugins/wbmp/WBMPMetadata.class */
public class WBMPMetadata extends IIOMetadata {
    static final String nativeMetadataFormatName = "javax_imageio_wbmp_1.0";
    public int wbmpType;
    public int width;
    public int height;

    public WBMPMetadata() {
        super(true, nativeMetadataFormatName, "com.sun.imageio.plugins.wbmp.WBMPMetadataFormat", null, null);
    }

    @Override // javax.imageio.metadata.IIOMetadata
    public boolean isReadOnly() {
        return true;
    }

    @Override // javax.imageio.metadata.IIOMetadata
    public Node getAsTree(String str) {
        if (str.equals(nativeMetadataFormatName)) {
            return getNativeTree();
        }
        if (str.equals(IIOMetadataFormatImpl.standardMetadataFormatName)) {
            return getStandardTree();
        }
        throw new IllegalArgumentException(I18N.getString("WBMPMetadata0"));
    }

    private Node getNativeTree() throws DOMException {
        IIOMetadataNode iIOMetadataNode = new IIOMetadataNode(nativeMetadataFormatName);
        addChildNode(iIOMetadataNode, "WBMPType", new Integer(this.wbmpType));
        addChildNode(iIOMetadataNode, "Width", new Integer(this.width));
        addChildNode(iIOMetadataNode, "Height", new Integer(this.height));
        return iIOMetadataNode;
    }

    @Override // javax.imageio.metadata.IIOMetadata
    public void setFromTree(String str, Node node) {
        throw new IllegalStateException(I18N.getString("WBMPMetadata1"));
    }

    @Override // javax.imageio.metadata.IIOMetadata
    public void mergeTree(String str, Node node) {
        throw new IllegalStateException(I18N.getString("WBMPMetadata1"));
    }

    @Override // javax.imageio.metadata.IIOMetadata
    public void reset() {
        throw new IllegalStateException(I18N.getString("WBMPMetadata1"));
    }

    private IIOMetadataNode addChildNode(IIOMetadataNode iIOMetadataNode, String str, Object obj) throws DOMException {
        IIOMetadataNode iIOMetadataNode2 = new IIOMetadataNode(str);
        if (obj != null) {
            iIOMetadataNode2.setUserObject(obj);
            iIOMetadataNode2.setNodeValue(ImageUtil.convertObjectToString(obj));
        }
        iIOMetadataNode.appendChild(iIOMetadataNode2);
        return iIOMetadataNode2;
    }

    @Override // javax.imageio.metadata.IIOMetadata
    protected IIOMetadataNode getStandardChromaNode() throws DOMException {
        IIOMetadataNode iIOMetadataNode = new IIOMetadataNode("Chroma");
        IIOMetadataNode iIOMetadataNode2 = new IIOMetadataNode("BlackIsZero");
        iIOMetadataNode2.setAttribute("value", "TRUE");
        iIOMetadataNode.appendChild(iIOMetadataNode2);
        return iIOMetadataNode;
    }

    @Override // javax.imageio.metadata.IIOMetadata
    protected IIOMetadataNode getStandardDimensionNode() throws DOMException {
        IIOMetadataNode iIOMetadataNode = new IIOMetadataNode("Dimension");
        IIOMetadataNode iIOMetadataNode2 = new IIOMetadataNode("ImageOrientation");
        iIOMetadataNode2.setAttribute("value", "Normal");
        iIOMetadataNode.appendChild(iIOMetadataNode2);
        return iIOMetadataNode;
    }
}
