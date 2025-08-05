package com.sun.imageio.plugins.jpeg;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import javax.imageio.metadata.IIOInvalidTreeException;
import javax.imageio.metadata.IIOMetadataNode;
import javax.imageio.stream.ImageOutputStream;
import org.w3c.dom.DOMException;
import org.w3c.dom.Node;

/* loaded from: rt.jar:com/sun/imageio/plugins/jpeg/COMMarkerSegment.class */
class COMMarkerSegment extends MarkerSegment {
    private static final String ENCODING = "ISO-8859-1";

    COMMarkerSegment(JPEGBuffer jPEGBuffer) throws IOException {
        super(jPEGBuffer);
        loadData(jPEGBuffer);
    }

    COMMarkerSegment(String str) {
        super(254);
        this.data = str.getBytes();
    }

    COMMarkerSegment(Node node) throws IIOInvalidTreeException, DOMException {
        super(254);
        if (node instanceof IIOMetadataNode) {
            this.data = (byte[]) ((IIOMetadataNode) node).getUserObject();
        }
        if (this.data == null) {
            String nodeValue = node.getAttributes().getNamedItem("comment").getNodeValue();
            if (nodeValue != null) {
                this.data = nodeValue.getBytes();
                return;
            }
            throw new IIOInvalidTreeException("Empty comment node!", node);
        }
    }

    String getComment() {
        try {
            return new String(this.data, "ISO-8859-1");
        } catch (UnsupportedEncodingException e2) {
            return null;
        }
    }

    @Override // com.sun.imageio.plugins.jpeg.MarkerSegment
    IIOMetadataNode getNativeNode() {
        IIOMetadataNode iIOMetadataNode = new IIOMetadataNode("com");
        iIOMetadataNode.setAttribute("comment", getComment());
        if (this.data != null) {
            iIOMetadataNode.setUserObject(this.data.clone());
        }
        return iIOMetadataNode;
    }

    @Override // com.sun.imageio.plugins.jpeg.MarkerSegment
    void write(ImageOutputStream imageOutputStream) throws IOException {
        this.length = 2 + this.data.length;
        writeTag(imageOutputStream);
        imageOutputStream.write(this.data);
    }

    @Override // com.sun.imageio.plugins.jpeg.MarkerSegment
    void print() {
        printTag("COM");
        System.out.println("<" + getComment() + ">");
    }
}
