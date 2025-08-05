package com.sun.imageio.plugins.jpeg;

import java.io.IOException;
import javax.imageio.IIOException;
import javax.imageio.metadata.IIOInvalidTreeException;
import javax.imageio.metadata.IIOMetadataNode;
import javax.imageio.stream.ImageOutputStream;
import org.w3c.dom.DOMException;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

/* loaded from: rt.jar:com/sun/imageio/plugins/jpeg/MarkerSegment.class */
class MarkerSegment implements Cloneable {
    protected static final int LENGTH_SIZE = 2;
    int tag;
    int length;
    byte[] data;
    boolean unknown;

    MarkerSegment(JPEGBuffer jPEGBuffer) throws IOException {
        this.data = null;
        this.unknown = false;
        jPEGBuffer.loadBuf(3);
        byte[] bArr = jPEGBuffer.buf;
        int i2 = jPEGBuffer.bufPtr;
        jPEGBuffer.bufPtr = i2 + 1;
        this.tag = bArr[i2] & 255;
        byte[] bArr2 = jPEGBuffer.buf;
        int i3 = jPEGBuffer.bufPtr;
        jPEGBuffer.bufPtr = i3 + 1;
        this.length = (bArr2[i3] & 255) << 8;
        int i4 = this.length;
        byte[] bArr3 = jPEGBuffer.buf;
        int i5 = jPEGBuffer.bufPtr;
        jPEGBuffer.bufPtr = i5 + 1;
        this.length = i4 | (bArr3[i5] & 255);
        this.length -= 2;
        if (this.length < 0) {
            throw new IIOException("Invalid segment length: " + this.length);
        }
        jPEGBuffer.bufAvail -= 3;
        jPEGBuffer.loadBuf(this.length);
    }

    MarkerSegment(int i2) {
        this.data = null;
        this.unknown = false;
        this.tag = i2;
        this.length = 0;
    }

    MarkerSegment(Node node) throws IIOInvalidTreeException {
        this.data = null;
        this.unknown = false;
        this.tag = getAttributeValue(node, null, "MarkerTag", 0, 255, true);
        this.length = 0;
        if (node instanceof IIOMetadataNode) {
            try {
                this.data = (byte[]) ((IIOMetadataNode) node).getUserObject();
                return;
            } catch (Exception e2) {
                IIOInvalidTreeException iIOInvalidTreeException = new IIOInvalidTreeException("Can't get User Object", node);
                iIOInvalidTreeException.initCause(e2);
                throw iIOInvalidTreeException;
            }
        }
        throw new IIOInvalidTreeException("Node must have User Object", node);
    }

    protected Object clone() {
        MarkerSegment markerSegment = null;
        try {
            markerSegment = (MarkerSegment) super.clone();
        } catch (CloneNotSupportedException e2) {
        }
        if (this.data != null) {
            markerSegment.data = (byte[]) this.data.clone();
        }
        return markerSegment;
    }

    void loadData(JPEGBuffer jPEGBuffer) throws IOException {
        this.data = new byte[this.length];
        jPEGBuffer.readData(this.data);
    }

    IIOMetadataNode getNativeNode() {
        IIOMetadataNode iIOMetadataNode = new IIOMetadataNode("unknown");
        iIOMetadataNode.setAttribute("MarkerTag", Integer.toString(this.tag));
        iIOMetadataNode.setUserObject(this.data);
        return iIOMetadataNode;
    }

    static int getAttributeValue(Node node, NamedNodeMap namedNodeMap, String str, int i2, int i3, boolean z2) throws IIOInvalidTreeException, DOMException, NumberFormatException {
        if (namedNodeMap == null) {
            namedNodeMap = node.getAttributes();
        }
        String nodeValue = namedNodeMap.getNamedItem(str).getNodeValue();
        int i4 = -1;
        if (nodeValue == null) {
            if (z2) {
                throw new IIOInvalidTreeException(str + " attribute not found", node);
            }
        } else {
            i4 = Integer.parseInt(nodeValue);
            if (i4 < i2 || i4 > i3) {
                throw new IIOInvalidTreeException(str + " attribute out of range", node);
            }
        }
        return i4;
    }

    void writeTag(ImageOutputStream imageOutputStream) throws IOException {
        imageOutputStream.write(255);
        imageOutputStream.write(this.tag);
        write2bytes(imageOutputStream, this.length);
    }

    void write(ImageOutputStream imageOutputStream) throws IOException {
        this.length = 2 + (this.data != null ? this.data.length : 0);
        writeTag(imageOutputStream);
        if (this.data != null) {
            imageOutputStream.write(this.data);
        }
    }

    static void write2bytes(ImageOutputStream imageOutputStream, int i2) throws IOException {
        imageOutputStream.write((i2 >> 8) & 255);
        imageOutputStream.write(i2 & 255);
    }

    void printTag(String str) {
        System.out.println(str + " marker segment - marker = 0x" + Integer.toHexString(this.tag));
        System.out.println("length: " + this.length);
    }

    void print() {
        printTag("Unknown");
        if (this.length > 10) {
            System.out.print("First 5 bytes:");
            for (int i2 = 0; i2 < 5; i2++) {
                System.out.print(" Ox" + Integer.toHexString(this.data[i2]));
            }
            System.out.print("\nLast 5 bytes:");
            for (int length = this.data.length - 5; length < this.data.length; length++) {
                System.out.print(" Ox" + Integer.toHexString(this.data[length]));
            }
        } else {
            System.out.print("Data:");
            for (int i3 = 0; i3 < this.data.length; i3++) {
                System.out.print(" Ox" + Integer.toHexString(this.data[i3]));
            }
        }
        System.out.println();
    }
}
