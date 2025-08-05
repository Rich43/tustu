package com.sun.imageio.plugins.jpeg;

import com.sun.org.apache.xalan.internal.templates.Constants;
import java.io.IOException;
import javax.imageio.metadata.IIOInvalidTreeException;
import javax.imageio.metadata.IIOMetadataNode;
import javax.imageio.stream.ImageOutputStream;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

/* loaded from: rt.jar:com/sun/imageio/plugins/jpeg/AdobeMarkerSegment.class */
class AdobeMarkerSegment extends MarkerSegment {
    int version;
    int flags0;
    int flags1;
    int transform;
    private static final int ID_SIZE = 5;

    AdobeMarkerSegment(int i2) {
        super(238);
        this.version = 101;
        this.flags0 = 0;
        this.flags1 = 0;
        this.transform = i2;
    }

    AdobeMarkerSegment(JPEGBuffer jPEGBuffer) throws IOException {
        super(jPEGBuffer);
        jPEGBuffer.bufPtr += 5;
        byte[] bArr = jPEGBuffer.buf;
        int i2 = jPEGBuffer.bufPtr;
        jPEGBuffer.bufPtr = i2 + 1;
        this.version = (bArr[i2] & 255) << 8;
        int i3 = this.version;
        byte[] bArr2 = jPEGBuffer.buf;
        int i4 = jPEGBuffer.bufPtr;
        jPEGBuffer.bufPtr = i4 + 1;
        this.version = i3 | (bArr2[i4] & 255);
        byte[] bArr3 = jPEGBuffer.buf;
        int i5 = jPEGBuffer.bufPtr;
        jPEGBuffer.bufPtr = i5 + 1;
        this.flags0 = (bArr3[i5] & 255) << 8;
        int i6 = this.flags0;
        byte[] bArr4 = jPEGBuffer.buf;
        int i7 = jPEGBuffer.bufPtr;
        jPEGBuffer.bufPtr = i7 + 1;
        this.flags0 = i6 | (bArr4[i7] & 255);
        byte[] bArr5 = jPEGBuffer.buf;
        int i8 = jPEGBuffer.bufPtr;
        jPEGBuffer.bufPtr = i8 + 1;
        this.flags1 = (bArr5[i8] & 255) << 8;
        int i9 = this.flags1;
        byte[] bArr6 = jPEGBuffer.buf;
        int i10 = jPEGBuffer.bufPtr;
        jPEGBuffer.bufPtr = i10 + 1;
        this.flags1 = i9 | (bArr6[i10] & 255);
        byte[] bArr7 = jPEGBuffer.buf;
        int i11 = jPEGBuffer.bufPtr;
        jPEGBuffer.bufPtr = i11 + 1;
        this.transform = bArr7[i11] & 255;
        jPEGBuffer.bufAvail -= this.length;
    }

    AdobeMarkerSegment(Node node) throws IIOInvalidTreeException {
        this(0);
        updateFromNativeNode(node, true);
    }

    @Override // com.sun.imageio.plugins.jpeg.MarkerSegment
    IIOMetadataNode getNativeNode() {
        IIOMetadataNode iIOMetadataNode = new IIOMetadataNode("app14Adobe");
        iIOMetadataNode.setAttribute("version", Integer.toString(this.version));
        iIOMetadataNode.setAttribute("flags0", Integer.toString(this.flags0));
        iIOMetadataNode.setAttribute("flags1", Integer.toString(this.flags1));
        iIOMetadataNode.setAttribute(Constants.ELEMNAME_TRANSFORM_STRING, Integer.toString(this.transform));
        return iIOMetadataNode;
    }

    void updateFromNativeNode(Node node, boolean z2) throws IIOInvalidTreeException {
        NamedNodeMap attributes = node.getAttributes();
        this.transform = getAttributeValue(node, attributes, Constants.ELEMNAME_TRANSFORM_STRING, 0, 2, true);
        int length = attributes.getLength();
        if (length > 4) {
            throw new IIOInvalidTreeException("Adobe APP14 node cannot have > 4 attributes", node);
        }
        if (length > 1) {
            int attributeValue = getAttributeValue(node, attributes, "version", 100, 255, false);
            this.version = attributeValue != -1 ? attributeValue : this.version;
            int attributeValue2 = getAttributeValue(node, attributes, "flags0", 0, 65535, false);
            this.flags0 = attributeValue2 != -1 ? attributeValue2 : this.flags0;
            int attributeValue3 = getAttributeValue(node, attributes, "flags1", 0, 65535, false);
            this.flags1 = attributeValue3 != -1 ? attributeValue3 : this.flags1;
        }
    }

    @Override // com.sun.imageio.plugins.jpeg.MarkerSegment
    void write(ImageOutputStream imageOutputStream) throws IOException {
        this.length = 14;
        writeTag(imageOutputStream);
        imageOutputStream.write(new byte[]{65, 100, 111, 98, 101});
        write2bytes(imageOutputStream, this.version);
        write2bytes(imageOutputStream, this.flags0);
        write2bytes(imageOutputStream, this.flags1);
        imageOutputStream.write(this.transform);
    }

    static void writeAdobeSegment(ImageOutputStream imageOutputStream, int i2) throws IOException {
        new AdobeMarkerSegment(i2).write(imageOutputStream);
    }

    @Override // com.sun.imageio.plugins.jpeg.MarkerSegment
    void print() {
        printTag("Adobe APP14");
        System.out.print("Version: ");
        System.out.println(this.version);
        System.out.print("Flags0: 0x");
        System.out.println(Integer.toHexString(this.flags0));
        System.out.print("Flags1: 0x");
        System.out.println(Integer.toHexString(this.flags1));
        System.out.print("Transform: ");
        System.out.println(this.transform);
    }
}
