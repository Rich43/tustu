package com.sun.imageio.plugins.jpeg;

import java.io.IOException;
import javax.imageio.metadata.IIOInvalidTreeException;
import javax.imageio.metadata.IIOMetadataNode;
import javax.imageio.stream.ImageOutputStream;
import org.w3c.dom.Node;

/* loaded from: rt.jar:com/sun/imageio/plugins/jpeg/DRIMarkerSegment.class */
class DRIMarkerSegment extends MarkerSegment {
    int restartInterval;

    DRIMarkerSegment(JPEGBuffer jPEGBuffer) throws IOException {
        super(jPEGBuffer);
        this.restartInterval = 0;
        byte[] bArr = jPEGBuffer.buf;
        int i2 = jPEGBuffer.bufPtr;
        jPEGBuffer.bufPtr = i2 + 1;
        this.restartInterval = (bArr[i2] & 255) << 8;
        int i3 = this.restartInterval;
        byte[] bArr2 = jPEGBuffer.buf;
        int i4 = jPEGBuffer.bufPtr;
        jPEGBuffer.bufPtr = i4 + 1;
        this.restartInterval = i3 | (bArr2[i4] & 255);
        jPEGBuffer.bufAvail -= this.length;
    }

    DRIMarkerSegment(Node node) throws IIOInvalidTreeException {
        super(221);
        this.restartInterval = 0;
        updateFromNativeNode(node, true);
    }

    @Override // com.sun.imageio.plugins.jpeg.MarkerSegment
    IIOMetadataNode getNativeNode() {
        IIOMetadataNode iIOMetadataNode = new IIOMetadataNode("dri");
        iIOMetadataNode.setAttribute("interval", Integer.toString(this.restartInterval));
        return iIOMetadataNode;
    }

    void updateFromNativeNode(Node node, boolean z2) throws IIOInvalidTreeException {
        this.restartInterval = getAttributeValue(node, null, "interval", 0, 65535, true);
    }

    @Override // com.sun.imageio.plugins.jpeg.MarkerSegment
    void write(ImageOutputStream imageOutputStream) throws IOException {
    }

    @Override // com.sun.imageio.plugins.jpeg.MarkerSegment
    void print() {
        printTag("DRI");
        System.out.println("Interval: " + Integer.toString(this.restartInterval));
    }
}
