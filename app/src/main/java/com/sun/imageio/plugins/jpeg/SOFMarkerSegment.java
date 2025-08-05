package com.sun.imageio.plugins.jpeg;

import java.io.IOException;
import javax.imageio.metadata.IIOInvalidTreeException;
import javax.imageio.metadata.IIOMetadataNode;
import javax.imageio.stream.ImageOutputStream;
import org.w3c.dom.DOMException;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/* loaded from: rt.jar:com/sun/imageio/plugins/jpeg/SOFMarkerSegment.class */
class SOFMarkerSegment extends MarkerSegment {
    int samplePrecision;
    int numLines;
    int samplesPerLine;
    ComponentSpec[] componentSpecs;

    SOFMarkerSegment(boolean z2, boolean z3, boolean z4, byte[] bArr, int i2) {
        super(z2 ? 194 : z3 ? 193 : 192);
        this.samplePrecision = 8;
        this.numLines = 0;
        this.samplesPerLine = 0;
        this.componentSpecs = new ComponentSpec[i2];
        for (int i3 = 0; i3 < i2; i3++) {
            int i4 = 1;
            int i5 = 0;
            if (z4) {
                i4 = 2;
                if (i3 == 1 || i3 == 2) {
                    i4 = 1;
                    i5 = 1;
                }
            }
            this.componentSpecs[i3] = new ComponentSpec(bArr[i3], i4, i5);
        }
    }

    SOFMarkerSegment(JPEGBuffer jPEGBuffer) throws IOException {
        super(jPEGBuffer);
        byte[] bArr = jPEGBuffer.buf;
        int i2 = jPEGBuffer.bufPtr;
        jPEGBuffer.bufPtr = i2 + 1;
        this.samplePrecision = bArr[i2];
        byte[] bArr2 = jPEGBuffer.buf;
        int i3 = jPEGBuffer.bufPtr;
        jPEGBuffer.bufPtr = i3 + 1;
        this.numLines = (bArr2[i3] & 255) << 8;
        int i4 = this.numLines;
        byte[] bArr3 = jPEGBuffer.buf;
        int i5 = jPEGBuffer.bufPtr;
        jPEGBuffer.bufPtr = i5 + 1;
        this.numLines = i4 | (bArr3[i5] & 255);
        byte[] bArr4 = jPEGBuffer.buf;
        int i6 = jPEGBuffer.bufPtr;
        jPEGBuffer.bufPtr = i6 + 1;
        this.samplesPerLine = (bArr4[i6] & 255) << 8;
        int i7 = this.samplesPerLine;
        byte[] bArr5 = jPEGBuffer.buf;
        int i8 = jPEGBuffer.bufPtr;
        jPEGBuffer.bufPtr = i8 + 1;
        this.samplesPerLine = i7 | (bArr5[i8] & 255);
        byte[] bArr6 = jPEGBuffer.buf;
        int i9 = jPEGBuffer.bufPtr;
        jPEGBuffer.bufPtr = i9 + 1;
        int i10 = bArr6[i9] & 255;
        this.componentSpecs = new ComponentSpec[i10];
        for (int i11 = 0; i11 < i10; i11++) {
            this.componentSpecs[i11] = new ComponentSpec(jPEGBuffer);
        }
        jPEGBuffer.bufAvail -= this.length;
    }

    SOFMarkerSegment(Node node) throws IIOInvalidTreeException {
        super(192);
        this.samplePrecision = 8;
        this.numLines = 0;
        this.samplesPerLine = 0;
        updateFromNativeNode(node, true);
    }

    @Override // com.sun.imageio.plugins.jpeg.MarkerSegment
    protected Object clone() {
        SOFMarkerSegment sOFMarkerSegment = (SOFMarkerSegment) super.clone();
        if (this.componentSpecs != null) {
            sOFMarkerSegment.componentSpecs = (ComponentSpec[]) this.componentSpecs.clone();
            for (int i2 = 0; i2 < this.componentSpecs.length; i2++) {
                sOFMarkerSegment.componentSpecs[i2] = (ComponentSpec) this.componentSpecs[i2].clone();
            }
        }
        return sOFMarkerSegment;
    }

    @Override // com.sun.imageio.plugins.jpeg.MarkerSegment
    IIOMetadataNode getNativeNode() throws DOMException {
        IIOMetadataNode iIOMetadataNode = new IIOMetadataNode("sof");
        iIOMetadataNode.setAttribute("process", Integer.toString(this.tag - 192));
        iIOMetadataNode.setAttribute("samplePrecision", Integer.toString(this.samplePrecision));
        iIOMetadataNode.setAttribute("numLines", Integer.toString(this.numLines));
        iIOMetadataNode.setAttribute("samplesPerLine", Integer.toString(this.samplesPerLine));
        iIOMetadataNode.setAttribute("numFrameComponents", Integer.toString(this.componentSpecs.length));
        for (int i2 = 0; i2 < this.componentSpecs.length; i2++) {
            iIOMetadataNode.appendChild(this.componentSpecs[i2].getNativeNode());
        }
        return iIOMetadataNode;
    }

    void updateFromNativeNode(Node node, boolean z2) throws IIOInvalidTreeException {
        NamedNodeMap attributes = node.getAttributes();
        int attributeValue = getAttributeValue(node, attributes, "process", 0, 2, false);
        this.tag = attributeValue != -1 ? attributeValue + 192 : this.tag;
        getAttributeValue(node, attributes, "samplePrecision", 8, 8, false);
        int attributeValue2 = getAttributeValue(node, attributes, "numLines", 0, 65535, false);
        this.numLines = attributeValue2 != -1 ? attributeValue2 : this.numLines;
        int attributeValue3 = getAttributeValue(node, attributes, "samplesPerLine", 0, 65535, false);
        this.samplesPerLine = attributeValue3 != -1 ? attributeValue3 : this.samplesPerLine;
        int attributeValue4 = getAttributeValue(node, attributes, "numFrameComponents", 1, 4, false);
        NodeList childNodes = node.getChildNodes();
        if (childNodes.getLength() != attributeValue4) {
            throw new IIOInvalidTreeException("numFrameComponents must match number of children", node);
        }
        this.componentSpecs = new ComponentSpec[attributeValue4];
        for (int i2 = 0; i2 < attributeValue4; i2++) {
            this.componentSpecs[i2] = new ComponentSpec(childNodes.item(i2));
        }
    }

    @Override // com.sun.imageio.plugins.jpeg.MarkerSegment
    void write(ImageOutputStream imageOutputStream) throws IOException {
    }

    @Override // com.sun.imageio.plugins.jpeg.MarkerSegment
    void print() {
        printTag("SOF");
        System.out.print("Sample precision: ");
        System.out.println(this.samplePrecision);
        System.out.print("Number of lines: ");
        System.out.println(this.numLines);
        System.out.print("Samples per line: ");
        System.out.println(this.samplesPerLine);
        System.out.print("Number of components: ");
        System.out.println(this.componentSpecs.length);
        for (int i2 = 0; i2 < this.componentSpecs.length; i2++) {
            this.componentSpecs[i2].print();
        }
    }

    int getIDencodedCSType() {
        for (int i2 = 0; i2 < this.componentSpecs.length; i2++) {
            if (this.componentSpecs[i2].componentId < 65) {
                return 0;
            }
        }
        switch (this.componentSpecs.length) {
            case 3:
                if (this.componentSpecs[0].componentId == 82 && this.componentSpecs[0].componentId == 71 && this.componentSpecs[0].componentId == 66) {
                    return 2;
                }
                if (this.componentSpecs[0].componentId == 89 && this.componentSpecs[0].componentId == 67 && this.componentSpecs[0].componentId == 99) {
                    return 5;
                }
                return 0;
            case 4:
                if (this.componentSpecs[0].componentId == 82 && this.componentSpecs[0].componentId == 71 && this.componentSpecs[0].componentId == 66 && this.componentSpecs[0].componentId == 65) {
                    return 6;
                }
                if (this.componentSpecs[0].componentId == 89 && this.componentSpecs[0].componentId == 67 && this.componentSpecs[0].componentId == 99 && this.componentSpecs[0].componentId == 65) {
                    return 10;
                }
                return 0;
            default:
                return 0;
        }
    }

    ComponentSpec getComponentSpec(byte b2, int i2, int i3) {
        return new ComponentSpec(b2, i2, i3);
    }

    /* loaded from: rt.jar:com/sun/imageio/plugins/jpeg/SOFMarkerSegment$ComponentSpec.class */
    class ComponentSpec implements Cloneable {
        int componentId;
        int HsamplingFactor;
        int VsamplingFactor;
        int QtableSelector;

        ComponentSpec(byte b2, int i2, int i3) {
            this.componentId = b2;
            this.HsamplingFactor = i2;
            this.VsamplingFactor = i2;
            this.QtableSelector = i3;
        }

        ComponentSpec(JPEGBuffer jPEGBuffer) {
            byte[] bArr = jPEGBuffer.buf;
            int i2 = jPEGBuffer.bufPtr;
            jPEGBuffer.bufPtr = i2 + 1;
            this.componentId = bArr[i2];
            this.HsamplingFactor = jPEGBuffer.buf[jPEGBuffer.bufPtr] >>> 4;
            byte[] bArr2 = jPEGBuffer.buf;
            int i3 = jPEGBuffer.bufPtr;
            jPEGBuffer.bufPtr = i3 + 1;
            this.VsamplingFactor = bArr2[i3] & 15;
            byte[] bArr3 = jPEGBuffer.buf;
            int i4 = jPEGBuffer.bufPtr;
            jPEGBuffer.bufPtr = i4 + 1;
            this.QtableSelector = bArr3[i4];
        }

        ComponentSpec(Node node) throws IIOInvalidTreeException {
            NamedNodeMap attributes = node.getAttributes();
            this.componentId = MarkerSegment.getAttributeValue(node, attributes, "componentId", 0, 255, true);
            this.HsamplingFactor = MarkerSegment.getAttributeValue(node, attributes, "HsamplingFactor", 1, 255, true);
            this.VsamplingFactor = MarkerSegment.getAttributeValue(node, attributes, "VsamplingFactor", 1, 255, true);
            this.QtableSelector = MarkerSegment.getAttributeValue(node, attributes, "QtableSelector", 0, 3, true);
        }

        protected Object clone() {
            try {
                return super.clone();
            } catch (CloneNotSupportedException e2) {
                return null;
            }
        }

        IIOMetadataNode getNativeNode() {
            IIOMetadataNode iIOMetadataNode = new IIOMetadataNode("componentSpec");
            iIOMetadataNode.setAttribute("componentId", Integer.toString(this.componentId));
            iIOMetadataNode.setAttribute("HsamplingFactor", Integer.toString(this.HsamplingFactor));
            iIOMetadataNode.setAttribute("VsamplingFactor", Integer.toString(this.VsamplingFactor));
            iIOMetadataNode.setAttribute("QtableSelector", Integer.toString(this.QtableSelector));
            return iIOMetadataNode;
        }

        void print() {
            System.out.print("Component ID: ");
            System.out.println(this.componentId);
            System.out.print("H sampling factor: ");
            System.out.println(this.HsamplingFactor);
            System.out.print("V sampling factor: ");
            System.out.println(this.VsamplingFactor);
            System.out.print("Q table selector: ");
            System.out.println(this.QtableSelector);
        }
    }
}
