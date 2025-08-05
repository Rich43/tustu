package com.sun.imageio.plugins.jpeg;

import java.io.IOException;
import javax.imageio.metadata.IIOInvalidTreeException;
import javax.imageio.metadata.IIOMetadataNode;
import javax.imageio.stream.ImageOutputStream;
import org.w3c.dom.DOMException;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/* loaded from: rt.jar:com/sun/imageio/plugins/jpeg/SOSMarkerSegment.class */
class SOSMarkerSegment extends MarkerSegment {
    int startSpectralSelection;
    int endSpectralSelection;
    int approxHigh;
    int approxLow;
    ScanComponentSpec[] componentSpecs;

    SOSMarkerSegment(boolean z2, byte[] bArr, int i2) {
        super(218);
        this.startSpectralSelection = 0;
        this.endSpectralSelection = 63;
        this.approxHigh = 0;
        this.approxLow = 0;
        this.componentSpecs = new ScanComponentSpec[i2];
        for (int i3 = 0; i3 < i2; i3++) {
            int i4 = 0;
            if (z2 && (i3 == 1 || i3 == 2)) {
                i4 = 1;
            }
            this.componentSpecs[i3] = new ScanComponentSpec(bArr[i3], i4);
        }
    }

    SOSMarkerSegment(JPEGBuffer jPEGBuffer) throws IOException {
        super(jPEGBuffer);
        byte[] bArr = jPEGBuffer.buf;
        int i2 = jPEGBuffer.bufPtr;
        jPEGBuffer.bufPtr = i2 + 1;
        int i3 = bArr[i2];
        this.componentSpecs = new ScanComponentSpec[i3];
        for (int i4 = 0; i4 < i3; i4++) {
            this.componentSpecs[i4] = new ScanComponentSpec(jPEGBuffer);
        }
        byte[] bArr2 = jPEGBuffer.buf;
        int i5 = jPEGBuffer.bufPtr;
        jPEGBuffer.bufPtr = i5 + 1;
        this.startSpectralSelection = bArr2[i5];
        byte[] bArr3 = jPEGBuffer.buf;
        int i6 = jPEGBuffer.bufPtr;
        jPEGBuffer.bufPtr = i6 + 1;
        this.endSpectralSelection = bArr3[i6];
        this.approxHigh = jPEGBuffer.buf[jPEGBuffer.bufPtr] >> 4;
        byte[] bArr4 = jPEGBuffer.buf;
        int i7 = jPEGBuffer.bufPtr;
        jPEGBuffer.bufPtr = i7 + 1;
        this.approxLow = bArr4[i7] & 15;
        jPEGBuffer.bufAvail -= this.length;
    }

    SOSMarkerSegment(Node node) throws IIOInvalidTreeException {
        super(218);
        this.startSpectralSelection = 0;
        this.endSpectralSelection = 63;
        this.approxHigh = 0;
        this.approxLow = 0;
        updateFromNativeNode(node, true);
    }

    @Override // com.sun.imageio.plugins.jpeg.MarkerSegment
    protected Object clone() {
        SOSMarkerSegment sOSMarkerSegment = (SOSMarkerSegment) super.clone();
        if (this.componentSpecs != null) {
            sOSMarkerSegment.componentSpecs = (ScanComponentSpec[]) this.componentSpecs.clone();
            for (int i2 = 0; i2 < this.componentSpecs.length; i2++) {
                sOSMarkerSegment.componentSpecs[i2] = (ScanComponentSpec) this.componentSpecs[i2].clone();
            }
        }
        return sOSMarkerSegment;
    }

    @Override // com.sun.imageio.plugins.jpeg.MarkerSegment
    IIOMetadataNode getNativeNode() throws DOMException {
        IIOMetadataNode iIOMetadataNode = new IIOMetadataNode("sos");
        iIOMetadataNode.setAttribute("numScanComponents", Integer.toString(this.componentSpecs.length));
        iIOMetadataNode.setAttribute("startSpectralSelection", Integer.toString(this.startSpectralSelection));
        iIOMetadataNode.setAttribute("endSpectralSelection", Integer.toString(this.endSpectralSelection));
        iIOMetadataNode.setAttribute("approxHigh", Integer.toString(this.approxHigh));
        iIOMetadataNode.setAttribute("approxLow", Integer.toString(this.approxLow));
        for (int i2 = 0; i2 < this.componentSpecs.length; i2++) {
            iIOMetadataNode.appendChild(this.componentSpecs[i2].getNativeNode());
        }
        return iIOMetadataNode;
    }

    void updateFromNativeNode(Node node, boolean z2) throws IIOInvalidTreeException {
        NamedNodeMap attributes = node.getAttributes();
        int attributeValue = getAttributeValue(node, attributes, "numScanComponents", 1, 4, true);
        int attributeValue2 = getAttributeValue(node, attributes, "startSpectralSelection", 0, 63, false);
        this.startSpectralSelection = attributeValue2 != -1 ? attributeValue2 : this.startSpectralSelection;
        int attributeValue3 = getAttributeValue(node, attributes, "endSpectralSelection", 0, 63, false);
        this.endSpectralSelection = attributeValue3 != -1 ? attributeValue3 : this.endSpectralSelection;
        int attributeValue4 = getAttributeValue(node, attributes, "approxHigh", 0, 15, false);
        this.approxHigh = attributeValue4 != -1 ? attributeValue4 : this.approxHigh;
        int attributeValue5 = getAttributeValue(node, attributes, "approxLow", 0, 15, false);
        this.approxLow = attributeValue5 != -1 ? attributeValue5 : this.approxLow;
        NodeList childNodes = node.getChildNodes();
        if (childNodes.getLength() != attributeValue) {
            throw new IIOInvalidTreeException("numScanComponents must match the number of children", node);
        }
        this.componentSpecs = new ScanComponentSpec[attributeValue];
        for (int i2 = 0; i2 < attributeValue; i2++) {
            this.componentSpecs[i2] = new ScanComponentSpec(childNodes.item(i2));
        }
    }

    @Override // com.sun.imageio.plugins.jpeg.MarkerSegment
    void write(ImageOutputStream imageOutputStream) throws IOException {
    }

    @Override // com.sun.imageio.plugins.jpeg.MarkerSegment
    void print() {
        printTag("SOS");
        System.out.print("Start spectral selection: ");
        System.out.println(this.startSpectralSelection);
        System.out.print("End spectral selection: ");
        System.out.println(this.endSpectralSelection);
        System.out.print("Approx high: ");
        System.out.println(this.approxHigh);
        System.out.print("Approx low: ");
        System.out.println(this.approxLow);
        System.out.print("Num scan components: ");
        System.out.println(this.componentSpecs.length);
        for (int i2 = 0; i2 < this.componentSpecs.length; i2++) {
            this.componentSpecs[i2].print();
        }
    }

    ScanComponentSpec getScanComponentSpec(byte b2, int i2) {
        return new ScanComponentSpec(b2, i2);
    }

    /* loaded from: rt.jar:com/sun/imageio/plugins/jpeg/SOSMarkerSegment$ScanComponentSpec.class */
    class ScanComponentSpec implements Cloneable {
        int componentSelector;
        int dcHuffTable;
        int acHuffTable;

        ScanComponentSpec(byte b2, int i2) {
            this.componentSelector = b2;
            this.dcHuffTable = i2;
            this.acHuffTable = i2;
        }

        ScanComponentSpec(JPEGBuffer jPEGBuffer) {
            byte[] bArr = jPEGBuffer.buf;
            int i2 = jPEGBuffer.bufPtr;
            jPEGBuffer.bufPtr = i2 + 1;
            this.componentSelector = bArr[i2];
            this.dcHuffTable = jPEGBuffer.buf[jPEGBuffer.bufPtr] >> 4;
            byte[] bArr2 = jPEGBuffer.buf;
            int i3 = jPEGBuffer.bufPtr;
            jPEGBuffer.bufPtr = i3 + 1;
            this.acHuffTable = bArr2[i3] & 15;
        }

        ScanComponentSpec(Node node) throws IIOInvalidTreeException {
            NamedNodeMap attributes = node.getAttributes();
            this.componentSelector = MarkerSegment.getAttributeValue(node, attributes, "componentSelector", 0, 255, true);
            this.dcHuffTable = MarkerSegment.getAttributeValue(node, attributes, "dcHuffTable", 0, 3, true);
            this.acHuffTable = MarkerSegment.getAttributeValue(node, attributes, "acHuffTable", 0, 3, true);
        }

        protected Object clone() {
            try {
                return super.clone();
            } catch (CloneNotSupportedException e2) {
                return null;
            }
        }

        IIOMetadataNode getNativeNode() {
            IIOMetadataNode iIOMetadataNode = new IIOMetadataNode("scanComponentSpec");
            iIOMetadataNode.setAttribute("componentSelector", Integer.toString(this.componentSelector));
            iIOMetadataNode.setAttribute("dcHuffTable", Integer.toString(this.dcHuffTable));
            iIOMetadataNode.setAttribute("acHuffTable", Integer.toString(this.acHuffTable));
            return iIOMetadataNode;
        }

        void print() {
            System.out.print("Component Selector: ");
            System.out.println(this.componentSelector);
            System.out.print("DC huffman table: ");
            System.out.println(this.dcHuffTable);
            System.out.print("AC huffman table: ");
            System.out.println(this.acHuffTable);
        }
    }
}
