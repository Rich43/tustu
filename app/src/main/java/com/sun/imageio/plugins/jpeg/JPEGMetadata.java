package com.sun.imageio.plugins.jpeg;

import com.sun.imageio.plugins.jpeg.DHTMarkerSegment;
import com.sun.imageio.plugins.jpeg.DQTMarkerSegment;
import com.sun.imageio.plugins.jpeg.JPEG;
import com.sun.imageio.plugins.jpeg.SOFMarkerSegment;
import com.sun.imageio.plugins.jpeg.SOSMarkerSegment;
import com.sun.org.apache.xalan.internal.templates.Constants;
import java.awt.Point;
import java.awt.color.ColorSpace;
import java.awt.color.ICC_ColorSpace;
import java.awt.color.ICC_Profile;
import java.awt.image.ColorModel;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import javax.imageio.IIOException;
import javax.imageio.ImageTypeSpecifier;
import javax.imageio.ImageWriteParam;
import javax.imageio.metadata.IIOInvalidTreeException;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.metadata.IIOMetadataFormatImpl;
import javax.imageio.metadata.IIOMetadataNode;
import javax.imageio.plugins.jpeg.JPEGHuffmanTable;
import javax.imageio.plugins.jpeg.JPEGImageWriteParam;
import javax.imageio.stream.ImageInputStream;
import javax.imageio.stream.ImageOutputStream;
import org.icepdf.core.pobjects.graphics.Separation;
import org.icepdf.core.pobjects.graphics.SoftMask;
import org.w3c.dom.DOMException;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/* loaded from: rt.jar:com/sun/imageio/plugins/jpeg/JPEGMetadata.class */
public class JPEGMetadata extends IIOMetadata implements Cloneable {
    private static final boolean debug = false;
    private List resetSequence;
    private boolean inThumb;
    private boolean hasAlpha;
    List markerSequence;
    final boolean isStream;
    private boolean transparencyDone;

    JPEGMetadata(boolean z2, boolean z3) {
        super(true, JPEG.nativeImageMetadataFormatName, JPEG.nativeImageMetadataFormatClassName, null, null);
        this.resetSequence = null;
        this.inThumb = false;
        this.markerSequence = new ArrayList();
        this.inThumb = z3;
        this.isStream = z2;
        if (z2) {
            this.nativeMetadataFormatName = JPEG.nativeStreamMetadataFormatName;
            this.nativeMetadataFormatClassName = JPEG.nativeStreamMetadataFormatClassName;
        }
    }

    JPEGMetadata(boolean z2, boolean z3, ImageInputStream imageInputStream, JPEGImageReader jPEGImageReader) throws IOException {
        this(z2, z3);
        JPEGBuffer jPEGBuffer = new JPEGBuffer(imageInputStream);
        jPEGBuffer.loadBuf(0);
        if ((jPEGBuffer.buf[0] & 255) != 255 || (jPEGBuffer.buf[1] & 255) != 216 || (jPEGBuffer.buf[2] & 255) != 255) {
            throw new IIOException("Image format error");
        }
        boolean z4 = false;
        jPEGBuffer.bufAvail -= 2;
        jPEGBuffer.bufPtr = 2;
        MarkerSegment markerSegment = null;
        while (!z4) {
            jPEGBuffer.loadBuf(1);
            jPEGBuffer.scanForFF(jPEGImageReader);
            switch (jPEGBuffer.buf[jPEGBuffer.bufPtr] & 255) {
                case 0:
                    jPEGBuffer.bufAvail--;
                    jPEGBuffer.bufPtr++;
                    break;
                case 192:
                case 193:
                case 194:
                    if (z2) {
                        throw new IIOException("SOF not permitted in stream metadata");
                    }
                    markerSegment = new SOFMarkerSegment(jPEGBuffer);
                    break;
                case 196:
                    markerSegment = new DHTMarkerSegment(jPEGBuffer);
                    break;
                case 208:
                case 209:
                case 210:
                case 211:
                case 212:
                case 213:
                case 214:
                case 215:
                    jPEGBuffer.bufPtr++;
                    jPEGBuffer.bufAvail--;
                    break;
                case 217:
                    z4 = true;
                    jPEGBuffer.bufPtr++;
                    jPEGBuffer.bufAvail--;
                    break;
                case 218:
                    if (z2) {
                        throw new IIOException("SOS not permitted in stream metadata");
                    }
                    markerSegment = new SOSMarkerSegment(jPEGBuffer);
                    break;
                case 219:
                    markerSegment = new DQTMarkerSegment(jPEGBuffer);
                    break;
                case 221:
                    markerSegment = new DRIMarkerSegment(jPEGBuffer);
                    break;
                case 224:
                    jPEGBuffer.loadBuf(8);
                    byte[] bArr = jPEGBuffer.buf;
                    int i2 = jPEGBuffer.bufPtr;
                    if (bArr[i2 + 3] == 74 && bArr[i2 + 4] == 70 && bArr[i2 + 5] == 73 && bArr[i2 + 6] == 70 && bArr[i2 + 7] == 0) {
                        if (!this.inThumb) {
                            if (z2) {
                                throw new IIOException("JFIF not permitted in stream metadata");
                            }
                            if (!this.markerSequence.isEmpty()) {
                                throw new IIOException("JFIF APP0 must be first marker after SOI");
                            }
                            markerSegment = new JFIFMarkerSegment(jPEGBuffer);
                            break;
                        } else {
                            jPEGImageReader.warningOccurred(1);
                            new JFIFMarkerSegment(jPEGBuffer);
                            break;
                        }
                    } else if (bArr[i2 + 3] == 74 && bArr[i2 + 4] == 70 && bArr[i2 + 5] == 88 && bArr[i2 + 6] == 88 && bArr[i2 + 7] == 0) {
                        if (z2) {
                            throw new IIOException("JFXX not permitted in stream metadata");
                        }
                        if (this.inThumb) {
                            throw new IIOException("JFXX markers not allowed in JFIF JPEG thumbnail");
                        }
                        JFIFMarkerSegment jFIFMarkerSegment = (JFIFMarkerSegment) findMarkerSegment(JFIFMarkerSegment.class, true);
                        if (jFIFMarkerSegment == null) {
                            throw new IIOException("JFXX encountered without prior JFIF!");
                        }
                        jFIFMarkerSegment.addJFXX(jPEGBuffer, jPEGImageReader);
                        break;
                    } else {
                        markerSegment = new MarkerSegment(jPEGBuffer);
                        markerSegment.loadData(jPEGBuffer);
                        break;
                    }
                    break;
                case 226:
                    jPEGBuffer.loadBuf(15);
                    if (jPEGBuffer.buf[jPEGBuffer.bufPtr + 3] == 73 && jPEGBuffer.buf[jPEGBuffer.bufPtr + 4] == 67 && jPEGBuffer.buf[jPEGBuffer.bufPtr + 5] == 67 && jPEGBuffer.buf[jPEGBuffer.bufPtr + 6] == 95 && jPEGBuffer.buf[jPEGBuffer.bufPtr + 7] == 80 && jPEGBuffer.buf[jPEGBuffer.bufPtr + 8] == 82 && jPEGBuffer.buf[jPEGBuffer.bufPtr + 9] == 79 && jPEGBuffer.buf[jPEGBuffer.bufPtr + 10] == 70 && jPEGBuffer.buf[jPEGBuffer.bufPtr + 11] == 73 && jPEGBuffer.buf[jPEGBuffer.bufPtr + 12] == 76 && jPEGBuffer.buf[jPEGBuffer.bufPtr + 13] == 69 && jPEGBuffer.buf[jPEGBuffer.bufPtr + 14] == 0) {
                        if (z2) {
                            throw new IIOException("ICC profiles not permitted in stream metadata");
                        }
                        JFIFMarkerSegment jFIFMarkerSegment2 = (JFIFMarkerSegment) findMarkerSegment(JFIFMarkerSegment.class, true);
                        if (jFIFMarkerSegment2 == null) {
                            markerSegment = new MarkerSegment(jPEGBuffer);
                            markerSegment.loadData(jPEGBuffer);
                            break;
                        } else {
                            jFIFMarkerSegment2.addICC(jPEGBuffer);
                            break;
                        }
                    } else {
                        markerSegment = new MarkerSegment(jPEGBuffer);
                        markerSegment.loadData(jPEGBuffer);
                        break;
                    }
                    break;
                case 238:
                    jPEGBuffer.loadBuf(8);
                    if (jPEGBuffer.buf[jPEGBuffer.bufPtr + 3] == 65 && jPEGBuffer.buf[jPEGBuffer.bufPtr + 4] == 100 && jPEGBuffer.buf[jPEGBuffer.bufPtr + 5] == 111 && jPEGBuffer.buf[jPEGBuffer.bufPtr + 6] == 98 && jPEGBuffer.buf[jPEGBuffer.bufPtr + 7] == 101) {
                        if (z2) {
                            throw new IIOException("Adobe APP14 markers not permitted in stream metadata");
                        }
                        markerSegment = new AdobeMarkerSegment(jPEGBuffer);
                        break;
                    } else {
                        markerSegment = new MarkerSegment(jPEGBuffer);
                        markerSegment.loadData(jPEGBuffer);
                        break;
                    }
                    break;
                case 254:
                    markerSegment = new COMMarkerSegment(jPEGBuffer);
                    break;
                default:
                    markerSegment = new MarkerSegment(jPEGBuffer);
                    markerSegment.loadData(jPEGBuffer);
                    markerSegment.unknown = true;
                    break;
            }
            if (markerSegment != null) {
                this.markerSequence.add(markerSegment);
                markerSegment = null;
            }
        }
        jPEGBuffer.pushBack();
        if (!isConsistent()) {
            throw new IIOException("Inconsistent metadata read from stream");
        }
    }

    JPEGMetadata(ImageWriteParam imageWriteParam, JPEGImageWriter jPEGImageWriter) {
        this(true, false);
        JPEGImageWriteParam jPEGImageWriteParam = null;
        if (imageWriteParam != null && (imageWriteParam instanceof JPEGImageWriteParam)) {
            jPEGImageWriteParam = (JPEGImageWriteParam) imageWriteParam;
            if (!jPEGImageWriteParam.areTablesSet()) {
                jPEGImageWriteParam = null;
            }
        }
        if (jPEGImageWriteParam != null) {
            this.markerSequence.add(new DQTMarkerSegment(jPEGImageWriteParam.getQTables()));
            this.markerSequence.add(new DHTMarkerSegment(jPEGImageWriteParam.getDCHuffmanTables(), jPEGImageWriteParam.getACHuffmanTables()));
        } else {
            this.markerSequence.add(new DQTMarkerSegment(JPEG.getDefaultQTables()));
            this.markerSequence.add(new DHTMarkerSegment(JPEG.getDefaultHuffmanTables(true), JPEG.getDefaultHuffmanTables(false)));
        }
        if (!isConsistent()) {
            throw new InternalError("Default stream metadata is inconsistent");
        }
    }

    JPEGMetadata(ImageTypeSpecifier imageTypeSpecifier, ImageWriteParam imageWriteParam, JPEGImageWriter jPEGImageWriter) {
        this(false, false);
        boolean z2 = true;
        boolean z3 = false;
        int i2 = 0;
        boolean z4 = true;
        boolean z5 = false;
        boolean z6 = false;
        boolean z7 = false;
        boolean z8 = true;
        boolean z9 = true;
        float compressionQuality = 0.75f;
        byte[] bArr = {1, 2, 3, 4};
        int numComponents = 0;
        ImageTypeSpecifier destinationType = null;
        if (imageWriteParam != null) {
            destinationType = imageWriteParam.getDestinationType();
            if (destinationType != null && imageTypeSpecifier != null) {
                jPEGImageWriter.warningOccurred(0);
                destinationType = null;
            }
            if (imageWriteParam.canWriteProgressive() && imageWriteParam.getProgressiveMode() == 1) {
                z6 = true;
                z9 = false;
            }
            if (imageWriteParam instanceof JPEGImageWriteParam) {
                JPEGImageWriteParam jPEGImageWriteParam = (JPEGImageWriteParam) imageWriteParam;
                if (jPEGImageWriteParam.areTablesSet()) {
                    z8 = false;
                    z9 = false;
                    if (jPEGImageWriteParam.getDCHuffmanTables().length > 2 || jPEGImageWriteParam.getACHuffmanTables().length > 2) {
                        z7 = true;
                    }
                }
                if (!z6 && jPEGImageWriteParam.getOptimizeHuffmanTables()) {
                    z9 = false;
                }
            }
            if (imageWriteParam.canWriteCompressed() && imageWriteParam.getCompressionMode() == 2) {
                compressionQuality = imageWriteParam.getCompressionQuality();
            }
        }
        ColorSpace colorSpace = null;
        if (destinationType == null) {
            if (imageTypeSpecifier != null) {
                ColorModel colorModel = imageTypeSpecifier.getColorModel();
                numComponents = colorModel.getNumComponents();
                boolean z10 = colorModel.getNumColorComponents() != numComponents;
                boolean zHasAlpha = colorModel.hasAlpha();
                colorSpace = colorModel.getColorSpace();
                switch (colorSpace.getType()) {
                    case 3:
                        if (z10) {
                            z2 = false;
                            if (!zHasAlpha) {
                                z3 = true;
                                i2 = 2;
                                break;
                            }
                        }
                        break;
                    case 4:
                    case 7:
                    case 8:
                    case 10:
                    case 11:
                    case 12:
                    default:
                        z2 = false;
                        z4 = false;
                        break;
                    case 5:
                        if (zHasAlpha) {
                            z2 = false;
                            break;
                        }
                        break;
                    case 6:
                        z4 = false;
                        if (z10) {
                            z2 = false;
                            break;
                        }
                        break;
                    case 9:
                        z2 = false;
                        z3 = true;
                        i2 = 2;
                        break;
                    case 13:
                        z2 = false;
                        z4 = false;
                        if (colorSpace.equals(ColorSpace.getInstance(1002))) {
                            z4 = true;
                            z3 = true;
                            bArr[0] = 89;
                            bArr[1] = 67;
                            bArr[2] = 99;
                            if (zHasAlpha) {
                                bArr[3] = 65;
                                break;
                            }
                        }
                        break;
                }
            }
        } else {
            ColorModel colorModel2 = destinationType.getColorModel();
            numComponents = colorModel2.getNumComponents();
            boolean z11 = colorModel2.getNumColorComponents() != numComponents;
            boolean zHasAlpha2 = colorModel2.hasAlpha();
            colorSpace = colorModel2.getColorSpace();
            switch (colorSpace.getType()) {
                case 3:
                    if (z11) {
                        z2 = false;
                        if (!zHasAlpha2) {
                            z3 = true;
                            i2 = 2;
                            break;
                        }
                    }
                    break;
                case 5:
                    z2 = false;
                    z3 = true;
                    z4 = false;
                    bArr[0] = 82;
                    bArr[1] = 71;
                    bArr[2] = 66;
                    if (zHasAlpha2) {
                        bArr[3] = 65;
                        break;
                    }
                    break;
                case 6:
                    z4 = false;
                    if (z11) {
                        z2 = false;
                        break;
                    }
                    break;
                case 13:
                    if (colorSpace == JPEG.JCS.getYCC()) {
                        z2 = false;
                        bArr[0] = 89;
                        bArr[1] = 67;
                        bArr[2] = 99;
                        if (zHasAlpha2) {
                            bArr[3] = 65;
                            break;
                        }
                    }
                    break;
                default:
                    z2 = false;
                    z4 = false;
                    break;
            }
        }
        if (z2 && JPEG.isNonStandardICC(colorSpace)) {
            z5 = true;
        }
        if (z2) {
            JFIFMarkerSegment jFIFMarkerSegment = new JFIFMarkerSegment();
            this.markerSequence.add(jFIFMarkerSegment);
            if (z5) {
                try {
                    jFIFMarkerSegment.addICC((ICC_ColorSpace) colorSpace);
                } catch (IOException e2) {
                }
            }
        }
        if (z3) {
            this.markerSequence.add(new AdobeMarkerSegment(i2));
        }
        if (z8) {
            this.markerSequence.add(new DQTMarkerSegment(compressionQuality, z4));
        }
        if (z9) {
            this.markerSequence.add(new DHTMarkerSegment(z4));
        }
        this.markerSequence.add(new SOFMarkerSegment(z6, z7, z4, bArr, numComponents));
        if (!z6) {
            this.markerSequence.add(new SOSMarkerSegment(z4, bArr, numComponents));
        }
        if (!isConsistent()) {
            throw new InternalError("Default image metadata is inconsistent");
        }
    }

    MarkerSegment findMarkerSegment(int i2) {
        for (MarkerSegment markerSegment : this.markerSequence) {
            if (markerSegment.tag == i2) {
                return markerSegment;
            }
        }
        return null;
    }

    MarkerSegment findMarkerSegment(Class cls, boolean z2) {
        if (z2) {
            for (MarkerSegment markerSegment : this.markerSequence) {
                if (cls.isInstance(markerSegment)) {
                    return markerSegment;
                }
            }
            return null;
        }
        ListIterator listIterator = this.markerSequence.listIterator(this.markerSequence.size());
        while (listIterator.hasPrevious()) {
            MarkerSegment markerSegment2 = (MarkerSegment) listIterator.previous();
            if (cls.isInstance(markerSegment2)) {
                return markerSegment2;
            }
        }
        return null;
    }

    private int findMarkerSegmentPosition(Class cls, boolean z2) {
        if (z2) {
            ListIterator listIterator = this.markerSequence.listIterator();
            int i2 = 0;
            while (listIterator.hasNext()) {
                if (!cls.isInstance((MarkerSegment) listIterator.next())) {
                    i2++;
                } else {
                    return i2;
                }
            }
            return -1;
        }
        ListIterator listIterator2 = this.markerSequence.listIterator(this.markerSequence.size());
        int size = this.markerSequence.size() - 1;
        while (listIterator2.hasPrevious()) {
            if (!cls.isInstance((MarkerSegment) listIterator2.previous())) {
                size--;
            } else {
                return size;
            }
        }
        return -1;
    }

    private int findLastUnknownMarkerSegmentPosition() {
        ListIterator listIterator = this.markerSequence.listIterator(this.markerSequence.size());
        int size = this.markerSequence.size() - 1;
        while (listIterator.hasPrevious()) {
            if (!((MarkerSegment) listIterator.previous()).unknown) {
                size--;
            } else {
                return size;
            }
        }
        return -1;
    }

    protected Object clone() {
        JPEGMetadata jPEGMetadata = null;
        try {
            jPEGMetadata = (JPEGMetadata) super.clone();
        } catch (CloneNotSupportedException e2) {
        }
        if (this.markerSequence != null) {
            jPEGMetadata.markerSequence = cloneSequence();
        }
        jPEGMetadata.resetSequence = null;
        return jPEGMetadata;
    }

    private List cloneSequence() {
        if (this.markerSequence == null) {
            return null;
        }
        ArrayList arrayList = new ArrayList(this.markerSequence.size());
        Iterator it = this.markerSequence.iterator();
        while (it.hasNext()) {
            arrayList.add(((MarkerSegment) it.next()).clone());
        }
        return arrayList;
    }

    @Override // javax.imageio.metadata.IIOMetadata
    public Node getAsTree(String str) {
        if (str == null) {
            throw new IllegalArgumentException("null formatName!");
        }
        if (this.isStream) {
            if (str.equals(JPEG.nativeStreamMetadataFormatName)) {
                return getNativeTree();
            }
        } else {
            if (str.equals(JPEG.nativeImageMetadataFormatName)) {
                return getNativeTree();
            }
            if (str.equals(IIOMetadataFormatImpl.standardMetadataFormatName)) {
                return getStandardTree();
            }
        }
        throw new IllegalArgumentException("Unsupported format name: " + str);
    }

    IIOMetadataNode getNativeTree() throws DOMException {
        IIOMetadataNode iIOMetadataNode;
        IIOMetadataNode iIOMetadataNode2;
        Iterator it = this.markerSequence.iterator();
        if (this.isStream) {
            iIOMetadataNode = new IIOMetadataNode(JPEG.nativeStreamMetadataFormatName);
            iIOMetadataNode2 = iIOMetadataNode;
        } else {
            IIOMetadataNode iIOMetadataNode3 = new IIOMetadataNode("markerSequence");
            if (!this.inThumb) {
                iIOMetadataNode = new IIOMetadataNode(JPEG.nativeImageMetadataFormatName);
                IIOMetadataNode iIOMetadataNode4 = new IIOMetadataNode("JPEGvariety");
                iIOMetadataNode.appendChild(iIOMetadataNode4);
                JFIFMarkerSegment jFIFMarkerSegment = (JFIFMarkerSegment) findMarkerSegment(JFIFMarkerSegment.class, true);
                if (jFIFMarkerSegment != null) {
                    it.next();
                    iIOMetadataNode4.appendChild(jFIFMarkerSegment.getNativeNode());
                }
                iIOMetadataNode.appendChild(iIOMetadataNode3);
            } else {
                iIOMetadataNode = iIOMetadataNode3;
            }
            iIOMetadataNode2 = iIOMetadataNode3;
        }
        while (it.hasNext()) {
            iIOMetadataNode2.appendChild(((MarkerSegment) it.next()).getNativeNode());
        }
        return iIOMetadataNode;
    }

    @Override // javax.imageio.metadata.IIOMetadata
    protected IIOMetadataNode getStandardChromaNode() throws DOMException {
        this.hasAlpha = false;
        SOFMarkerSegment sOFMarkerSegment = (SOFMarkerSegment) findMarkerSegment(SOFMarkerSegment.class, true);
        if (sOFMarkerSegment == null) {
            return null;
        }
        IIOMetadataNode iIOMetadataNode = new IIOMetadataNode("Chroma");
        IIOMetadataNode iIOMetadataNode2 = new IIOMetadataNode("ColorSpaceType");
        iIOMetadataNode.appendChild(iIOMetadataNode2);
        int length = sOFMarkerSegment.componentSpecs.length;
        IIOMetadataNode iIOMetadataNode3 = new IIOMetadataNode("NumChannels");
        iIOMetadataNode.appendChild(iIOMetadataNode3);
        iIOMetadataNode3.setAttribute("value", Integer.toString(length));
        if (findMarkerSegment(JFIFMarkerSegment.class, true) != null) {
            if (length == 1) {
                iIOMetadataNode2.setAttribute("name", "GRAY");
            } else {
                iIOMetadataNode2.setAttribute("name", "YCbCr");
            }
            return iIOMetadataNode;
        }
        AdobeMarkerSegment adobeMarkerSegment = (AdobeMarkerSegment) findMarkerSegment(AdobeMarkerSegment.class, true);
        if (adobeMarkerSegment != null) {
            switch (adobeMarkerSegment.transform) {
                case 0:
                    if (length == 3) {
                        iIOMetadataNode2.setAttribute("name", "RGB");
                        break;
                    } else if (length == 4) {
                        iIOMetadataNode2.setAttribute("name", "CMYK");
                        break;
                    }
                    break;
                case 1:
                    iIOMetadataNode2.setAttribute("name", "YCbCr");
                    break;
                case 2:
                    iIOMetadataNode2.setAttribute("name", "YCCK");
                    break;
            }
            return iIOMetadataNode;
        }
        if (length < 3) {
            iIOMetadataNode2.setAttribute("name", "GRAY");
            if (length == 2) {
                this.hasAlpha = true;
            }
            return iIOMetadataNode;
        }
        boolean z2 = true;
        for (int i2 = 0; i2 < sOFMarkerSegment.componentSpecs.length; i2++) {
            int i3 = sOFMarkerSegment.componentSpecs[i2].componentId;
            if (i3 < 1 || i3 >= sOFMarkerSegment.componentSpecs.length) {
                z2 = false;
            }
        }
        if (z2) {
            iIOMetadataNode2.setAttribute("name", "YCbCr");
            if (length == 4) {
                this.hasAlpha = true;
            }
            return iIOMetadataNode;
        }
        if (sOFMarkerSegment.componentSpecs[0].componentId == 82 && sOFMarkerSegment.componentSpecs[1].componentId == 71 && sOFMarkerSegment.componentSpecs[2].componentId == 66) {
            iIOMetadataNode2.setAttribute("name", "RGB");
            if (length == 4 && sOFMarkerSegment.componentSpecs[3].componentId == 65) {
                this.hasAlpha = true;
            }
            return iIOMetadataNode;
        }
        if (sOFMarkerSegment.componentSpecs[0].componentId == 89 && sOFMarkerSegment.componentSpecs[1].componentId == 67 && sOFMarkerSegment.componentSpecs[2].componentId == 99) {
            iIOMetadataNode2.setAttribute("name", "PhotoYCC");
            if (length == 4 && sOFMarkerSegment.componentSpecs[3].componentId == 65) {
                this.hasAlpha = true;
            }
            return iIOMetadataNode;
        }
        boolean z3 = false;
        int i4 = sOFMarkerSegment.componentSpecs[0].HsamplingFactor;
        int i5 = sOFMarkerSegment.componentSpecs[0].VsamplingFactor;
        for (int i6 = 1; i6 < sOFMarkerSegment.componentSpecs.length; i6++) {
            if (sOFMarkerSegment.componentSpecs[i6].HsamplingFactor != i4 || sOFMarkerSegment.componentSpecs[i6].VsamplingFactor != i5) {
                z3 = true;
                break;
            }
        }
        if (z3) {
            iIOMetadataNode2.setAttribute("name", "YCbCr");
            if (length == 4) {
                this.hasAlpha = true;
            }
            return iIOMetadataNode;
        }
        if (length == 3) {
            iIOMetadataNode2.setAttribute("name", "RGB");
        } else {
            iIOMetadataNode2.setAttribute("name", "CMYK");
        }
        return iIOMetadataNode;
    }

    @Override // javax.imageio.metadata.IIOMetadata
    protected IIOMetadataNode getStandardCompressionNode() throws DOMException {
        IIOMetadataNode iIOMetadataNode = new IIOMetadataNode("Compression");
        IIOMetadataNode iIOMetadataNode2 = new IIOMetadataNode("CompressionTypeName");
        iIOMetadataNode2.setAttribute("value", "JPEG");
        iIOMetadataNode.appendChild(iIOMetadataNode2);
        IIOMetadataNode iIOMetadataNode3 = new IIOMetadataNode("Lossless");
        iIOMetadataNode3.setAttribute("value", "FALSE");
        iIOMetadataNode.appendChild(iIOMetadataNode3);
        int i2 = 0;
        Iterator it = this.markerSequence.iterator();
        while (it.hasNext()) {
            if (((MarkerSegment) it.next()).tag == 218) {
                i2++;
            }
        }
        if (i2 != 0) {
            IIOMetadataNode iIOMetadataNode4 = new IIOMetadataNode("NumProgressiveScans");
            iIOMetadataNode4.setAttribute("value", Integer.toString(i2));
            iIOMetadataNode.appendChild(iIOMetadataNode4);
        }
        return iIOMetadataNode;
    }

    @Override // javax.imageio.metadata.IIOMetadata
    protected IIOMetadataNode getStandardDimensionNode() throws DOMException {
        float f2;
        IIOMetadataNode iIOMetadataNode = new IIOMetadataNode("Dimension");
        IIOMetadataNode iIOMetadataNode2 = new IIOMetadataNode("ImageOrientation");
        iIOMetadataNode2.setAttribute("value", "normal");
        iIOMetadataNode.appendChild(iIOMetadataNode2);
        JFIFMarkerSegment jFIFMarkerSegment = (JFIFMarkerSegment) findMarkerSegment(JFIFMarkerSegment.class, true);
        if (jFIFMarkerSegment != null) {
            if (jFIFMarkerSegment.resUnits == 0) {
                f2 = jFIFMarkerSegment.Xdensity / jFIFMarkerSegment.Ydensity;
            } else {
                f2 = jFIFMarkerSegment.Ydensity / jFIFMarkerSegment.Xdensity;
            }
            IIOMetadataNode iIOMetadataNode3 = new IIOMetadataNode("PixelAspectRatio");
            iIOMetadataNode3.setAttribute("value", Float.toString(f2));
            iIOMetadataNode.insertBefore(iIOMetadataNode3, iIOMetadataNode2);
            if (jFIFMarkerSegment.resUnits != 0) {
                float f3 = jFIFMarkerSegment.resUnits == 1 ? 25.4f : 10.0f;
                IIOMetadataNode iIOMetadataNode4 = new IIOMetadataNode("HorizontalPixelSize");
                iIOMetadataNode4.setAttribute("value", Float.toString(f3 / jFIFMarkerSegment.Xdensity));
                iIOMetadataNode.appendChild(iIOMetadataNode4);
                IIOMetadataNode iIOMetadataNode5 = new IIOMetadataNode("VerticalPixelSize");
                iIOMetadataNode5.setAttribute("value", Float.toString(f3 / jFIFMarkerSegment.Ydensity));
                iIOMetadataNode.appendChild(iIOMetadataNode5);
            }
        }
        return iIOMetadataNode;
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r5v0 */
    /* JADX WARN: Type inference failed for: r5v1 */
    /* JADX WARN: Type inference failed for: r5v2, types: [javax.imageio.metadata.IIOMetadataNode] */
    @Override // javax.imageio.metadata.IIOMetadata
    protected IIOMetadataNode getStandardTextNode() throws DOMException {
        IIOMetadataNode iIOMetadataNode = 0;
        if (findMarkerSegment(254) != null) {
            iIOMetadataNode = new IIOMetadataNode("Text");
            for (MarkerSegment markerSegment : this.markerSequence) {
                if (markerSegment.tag == 254) {
                    COMMarkerSegment cOMMarkerSegment = (COMMarkerSegment) markerSegment;
                    IIOMetadataNode iIOMetadataNode2 = new IIOMetadataNode("TextEntry");
                    iIOMetadataNode2.setAttribute("keyword", "comment");
                    iIOMetadataNode2.setAttribute("value", cOMMarkerSegment.getComment());
                    iIOMetadataNode.appendChild(iIOMetadataNode2);
                }
            }
        }
        return iIOMetadataNode;
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r5v2, types: [javax.imageio.metadata.IIOMetadataNode] */
    @Override // javax.imageio.metadata.IIOMetadata
    protected IIOMetadataNode getStandardTransparencyNode() throws DOMException {
        IIOMetadataNode iIOMetadataNode = null;
        if (this.hasAlpha) {
            ?? iIOMetadataNode2 = new IIOMetadataNode("Transparency");
            IIOMetadataNode iIOMetadataNode3 = new IIOMetadataNode(SoftMask.SOFT_MASK_TYPE_ALPHA);
            iIOMetadataNode3.setAttribute("value", "nonpremultiplied");
            iIOMetadataNode2.appendChild(iIOMetadataNode3);
            iIOMetadataNode = iIOMetadataNode2;
        }
        return iIOMetadataNode;
    }

    @Override // javax.imageio.metadata.IIOMetadata
    public boolean isReadOnly() {
        return false;
    }

    @Override // javax.imageio.metadata.IIOMetadata
    public void mergeTree(String str, Node node) throws IIOInvalidTreeException, DOMException, NumberFormatException {
        List listCloneSequence;
        if (str == null) {
            throw new IllegalArgumentException("null formatName!");
        }
        if (node == null) {
            throw new IllegalArgumentException("null root!");
        }
        if (this.resetSequence == null) {
            this.resetSequence = cloneSequence();
            listCloneSequence = this.resetSequence;
        } else {
            listCloneSequence = cloneSequence();
        }
        if (this.isStream && str.equals(JPEG.nativeStreamMetadataFormatName)) {
            mergeNativeTree(node);
        } else if (!this.isStream && str.equals(JPEG.nativeImageMetadataFormatName)) {
            mergeNativeTree(node);
        } else if (!this.isStream && str.equals(IIOMetadataFormatImpl.standardMetadataFormatName)) {
            mergeStandardTree(node);
        } else {
            throw new IllegalArgumentException("Unsupported format name: " + str);
        }
        if (!isConsistent()) {
            this.markerSequence = listCloneSequence;
            throw new IIOInvalidTreeException("Merged tree is invalid; original restored", node);
        }
    }

    private void mergeNativeTree(Node node) throws IIOInvalidTreeException, DOMException, NumberFormatException {
        String nodeName = node.getNodeName();
        if (nodeName != (this.isStream ? JPEG.nativeStreamMetadataFormatName : JPEG.nativeImageMetadataFormatName)) {
            throw new IIOInvalidTreeException("Invalid root node name: " + nodeName, node);
        }
        if (node.getChildNodes().getLength() != 2) {
            throw new IIOInvalidTreeException("JPEGvariety and markerSequence nodes must be present", node);
        }
        mergeJFIFsubtree(node.getFirstChild());
        mergeSequenceSubtree(node.getLastChild());
    }

    private void mergeJFIFsubtree(Node node) throws IIOInvalidTreeException {
        if (node.getChildNodes().getLength() != 0) {
            Node firstChild = node.getFirstChild();
            JFIFMarkerSegment jFIFMarkerSegment = (JFIFMarkerSegment) findMarkerSegment(JFIFMarkerSegment.class, true);
            if (jFIFMarkerSegment != null) {
                jFIFMarkerSegment.updateFromNativeNode(firstChild, false);
            } else {
                this.markerSequence.add(0, new JFIFMarkerSegment(firstChild));
            }
        }
    }

    private void mergeSequenceSubtree(Node node) throws IIOInvalidTreeException, DOMException, NumberFormatException {
        NodeList childNodes = node.getChildNodes();
        for (int i2 = 0; i2 < childNodes.getLength(); i2++) {
            Node nodeItem = childNodes.item(i2);
            String nodeName = nodeItem.getNodeName();
            if (nodeName.equals("dqt")) {
                mergeDQTNode(nodeItem);
            } else if (nodeName.equals("dht")) {
                mergeDHTNode(nodeItem);
            } else if (nodeName.equals("dri")) {
                mergeDRINode(nodeItem);
            } else if (nodeName.equals("com")) {
                mergeCOMNode(nodeItem);
            } else if (nodeName.equals("app14Adobe")) {
                mergeAdobeNode(nodeItem);
            } else if (nodeName.equals("unknown")) {
                mergeUnknownNode(nodeItem);
            } else if (nodeName.equals("sof")) {
                mergeSOFNode(nodeItem);
            } else if (nodeName.equals("sos")) {
                mergeSOSNode(nodeItem);
            } else {
                throw new IIOInvalidTreeException("Invalid node: " + nodeName, nodeItem);
            }
        }
    }

    private void mergeDQTNode(Node node) throws IIOInvalidTreeException, DOMException, NumberFormatException {
        ArrayList arrayList = new ArrayList();
        for (MarkerSegment markerSegment : this.markerSequence) {
            if (markerSegment instanceof DQTMarkerSegment) {
                arrayList.add(markerSegment);
            }
        }
        if (!arrayList.isEmpty()) {
            NodeList childNodes = node.getChildNodes();
            for (int i2 = 0; i2 < childNodes.getLength(); i2++) {
                Node nodeItem = childNodes.item(i2);
                int attributeValue = MarkerSegment.getAttributeValue(nodeItem, null, "qtableId", 0, 3, true);
                DQTMarkerSegment dQTMarkerSegment = null;
                int i3 = -1;
                for (int i4 = 0; i4 < arrayList.size(); i4++) {
                    DQTMarkerSegment dQTMarkerSegment2 = (DQTMarkerSegment) arrayList.get(i4);
                    int i5 = 0;
                    while (true) {
                        if (i5 >= dQTMarkerSegment2.tables.size()) {
                            break;
                        }
                        if (attributeValue != ((DQTMarkerSegment.Qtable) dQTMarkerSegment2.tables.get(i5)).tableID) {
                            i5++;
                        } else {
                            dQTMarkerSegment = dQTMarkerSegment2;
                            i3 = i5;
                            break;
                        }
                    }
                    if (dQTMarkerSegment != null) {
                        break;
                    }
                }
                if (dQTMarkerSegment != null) {
                    dQTMarkerSegment.tables.set(i3, dQTMarkerSegment.getQtableFromNode(nodeItem));
                } else {
                    DQTMarkerSegment dQTMarkerSegment3 = (DQTMarkerSegment) arrayList.get(arrayList.size() - 1);
                    dQTMarkerSegment3.tables.add(dQTMarkerSegment3.getQtableFromNode(nodeItem));
                }
            }
            return;
        }
        DQTMarkerSegment dQTMarkerSegment4 = new DQTMarkerSegment(node);
        int iFindMarkerSegmentPosition = findMarkerSegmentPosition(DHTMarkerSegment.class, true);
        int iFindMarkerSegmentPosition2 = findMarkerSegmentPosition(SOFMarkerSegment.class, true);
        int iFindMarkerSegmentPosition3 = findMarkerSegmentPosition(SOSMarkerSegment.class, true);
        if (iFindMarkerSegmentPosition != -1) {
            this.markerSequence.add(iFindMarkerSegmentPosition, dQTMarkerSegment4);
            return;
        }
        if (iFindMarkerSegmentPosition2 != -1) {
            this.markerSequence.add(iFindMarkerSegmentPosition2, dQTMarkerSegment4);
        } else if (iFindMarkerSegmentPosition3 != -1) {
            this.markerSequence.add(iFindMarkerSegmentPosition3, dQTMarkerSegment4);
        } else {
            this.markerSequence.add(dQTMarkerSegment4);
        }
    }

    private void mergeDHTNode(Node node) throws IIOInvalidTreeException, DOMException, NumberFormatException {
        ArrayList arrayList = new ArrayList();
        for (MarkerSegment markerSegment : this.markerSequence) {
            if (markerSegment instanceof DHTMarkerSegment) {
                arrayList.add(markerSegment);
            }
        }
        if (!arrayList.isEmpty()) {
            NodeList childNodes = node.getChildNodes();
            for (int i2 = 0; i2 < childNodes.getLength(); i2++) {
                Node nodeItem = childNodes.item(i2);
                NamedNodeMap attributes = nodeItem.getAttributes();
                int attributeValue = MarkerSegment.getAttributeValue(nodeItem, attributes, "htableId", 0, 3, true);
                int attributeValue2 = MarkerSegment.getAttributeValue(nodeItem, attributes, Constants.ATTRNAME_CLASS, 0, 1, true);
                DHTMarkerSegment dHTMarkerSegment = null;
                int i3 = -1;
                for (int i4 = 0; i4 < arrayList.size(); i4++) {
                    DHTMarkerSegment dHTMarkerSegment2 = (DHTMarkerSegment) arrayList.get(i4);
                    int i5 = 0;
                    while (true) {
                        if (i5 >= dHTMarkerSegment2.tables.size()) {
                            break;
                        }
                        DHTMarkerSegment.Htable htable = (DHTMarkerSegment.Htable) dHTMarkerSegment2.tables.get(i5);
                        if (attributeValue != htable.tableID || attributeValue2 != htable.tableClass) {
                            i5++;
                        } else {
                            dHTMarkerSegment = dHTMarkerSegment2;
                            i3 = i5;
                            break;
                        }
                    }
                    if (dHTMarkerSegment != null) {
                        break;
                    }
                }
                if (dHTMarkerSegment != null) {
                    dHTMarkerSegment.tables.set(i3, dHTMarkerSegment.getHtableFromNode(nodeItem));
                } else {
                    DHTMarkerSegment dHTMarkerSegment3 = (DHTMarkerSegment) arrayList.get(arrayList.size() - 1);
                    dHTMarkerSegment3.tables.add(dHTMarkerSegment3.getHtableFromNode(nodeItem));
                }
            }
            return;
        }
        DHTMarkerSegment dHTMarkerSegment4 = new DHTMarkerSegment(node);
        int iFindMarkerSegmentPosition = findMarkerSegmentPosition(DQTMarkerSegment.class, false);
        int iFindMarkerSegmentPosition2 = findMarkerSegmentPosition(SOFMarkerSegment.class, true);
        int iFindMarkerSegmentPosition3 = findMarkerSegmentPosition(SOSMarkerSegment.class, true);
        if (iFindMarkerSegmentPosition != -1) {
            this.markerSequence.add(iFindMarkerSegmentPosition + 1, dHTMarkerSegment4);
            return;
        }
        if (iFindMarkerSegmentPosition2 != -1) {
            this.markerSequence.add(iFindMarkerSegmentPosition2, dHTMarkerSegment4);
        } else if (iFindMarkerSegmentPosition3 != -1) {
            this.markerSequence.add(iFindMarkerSegmentPosition3, dHTMarkerSegment4);
        } else {
            this.markerSequence.add(dHTMarkerSegment4);
        }
    }

    private void mergeDRINode(Node node) throws IIOInvalidTreeException {
        DRIMarkerSegment dRIMarkerSegment = (DRIMarkerSegment) findMarkerSegment(DRIMarkerSegment.class, true);
        if (dRIMarkerSegment != null) {
            dRIMarkerSegment.updateFromNativeNode(node, false);
            return;
        }
        DRIMarkerSegment dRIMarkerSegment2 = new DRIMarkerSegment(node);
        int iFindMarkerSegmentPosition = findMarkerSegmentPosition(SOFMarkerSegment.class, true);
        int iFindMarkerSegmentPosition2 = findMarkerSegmentPosition(SOSMarkerSegment.class, true);
        if (iFindMarkerSegmentPosition != -1) {
            this.markerSequence.add(iFindMarkerSegmentPosition, dRIMarkerSegment2);
        } else if (iFindMarkerSegmentPosition2 != -1) {
            this.markerSequence.add(iFindMarkerSegmentPosition2, dRIMarkerSegment2);
        } else {
            this.markerSequence.add(dRIMarkerSegment2);
        }
    }

    private void mergeCOMNode(Node node) throws IIOInvalidTreeException {
        insertCOMMarkerSegment(new COMMarkerSegment(node));
    }

    private void insertCOMMarkerSegment(COMMarkerSegment cOMMarkerSegment) {
        int iFindMarkerSegmentPosition = findMarkerSegmentPosition(COMMarkerSegment.class, false);
        boolean z2 = findMarkerSegment(JFIFMarkerSegment.class, true) != null;
        int iFindMarkerSegmentPosition2 = findMarkerSegmentPosition(AdobeMarkerSegment.class, true);
        if (iFindMarkerSegmentPosition != -1) {
            this.markerSequence.add(iFindMarkerSegmentPosition + 1, cOMMarkerSegment);
            return;
        }
        if (z2) {
            this.markerSequence.add(1, cOMMarkerSegment);
        } else if (iFindMarkerSegmentPosition2 != -1) {
            this.markerSequence.add(iFindMarkerSegmentPosition2 + 1, cOMMarkerSegment);
        } else {
            this.markerSequence.add(0, cOMMarkerSegment);
        }
    }

    private void mergeAdobeNode(Node node) throws IIOInvalidTreeException {
        AdobeMarkerSegment adobeMarkerSegment = (AdobeMarkerSegment) findMarkerSegment(AdobeMarkerSegment.class, true);
        if (adobeMarkerSegment != null) {
            adobeMarkerSegment.updateFromNativeNode(node, false);
        } else {
            insertAdobeMarkerSegment(new AdobeMarkerSegment(node));
        }
    }

    private void insertAdobeMarkerSegment(AdobeMarkerSegment adobeMarkerSegment) {
        boolean z2 = findMarkerSegment(JFIFMarkerSegment.class, true) != null;
        int iFindLastUnknownMarkerSegmentPosition = findLastUnknownMarkerSegmentPosition();
        if (z2) {
            this.markerSequence.add(1, adobeMarkerSegment);
        } else if (iFindLastUnknownMarkerSegmentPosition != -1) {
            this.markerSequence.add(iFindLastUnknownMarkerSegmentPosition + 1, adobeMarkerSegment);
        } else {
            this.markerSequence.add(0, adobeMarkerSegment);
        }
    }

    private void mergeUnknownNode(Node node) throws IIOInvalidTreeException {
        MarkerSegment markerSegment = new MarkerSegment(node);
        int iFindLastUnknownMarkerSegmentPosition = findLastUnknownMarkerSegmentPosition();
        boolean z2 = findMarkerSegment(JFIFMarkerSegment.class, true) != null;
        int iFindMarkerSegmentPosition = findMarkerSegmentPosition(AdobeMarkerSegment.class, true);
        if (iFindLastUnknownMarkerSegmentPosition != -1) {
            this.markerSequence.add(iFindLastUnknownMarkerSegmentPosition + 1, markerSegment);
        } else if (z2) {
            this.markerSequence.add(1, markerSegment);
        }
        if (iFindMarkerSegmentPosition != -1) {
            this.markerSequence.add(iFindMarkerSegmentPosition, markerSegment);
        } else {
            this.markerSequence.add(0, markerSegment);
        }
    }

    private void mergeSOFNode(Node node) throws IIOInvalidTreeException {
        SOFMarkerSegment sOFMarkerSegment = (SOFMarkerSegment) findMarkerSegment(SOFMarkerSegment.class, true);
        if (sOFMarkerSegment != null) {
            sOFMarkerSegment.updateFromNativeNode(node, false);
            return;
        }
        SOFMarkerSegment sOFMarkerSegment2 = new SOFMarkerSegment(node);
        int iFindMarkerSegmentPosition = findMarkerSegmentPosition(SOSMarkerSegment.class, true);
        if (iFindMarkerSegmentPosition != -1) {
            this.markerSequence.add(iFindMarkerSegmentPosition, sOFMarkerSegment2);
        } else {
            this.markerSequence.add(sOFMarkerSegment2);
        }
    }

    private void mergeSOSNode(Node node) throws IIOInvalidTreeException {
        SOSMarkerSegment sOSMarkerSegment = (SOSMarkerSegment) findMarkerSegment(SOSMarkerSegment.class, true);
        SOSMarkerSegment sOSMarkerSegment2 = (SOSMarkerSegment) findMarkerSegment(SOSMarkerSegment.class, false);
        if (sOSMarkerSegment != null) {
            if (sOSMarkerSegment != sOSMarkerSegment2) {
                throw new IIOInvalidTreeException("Can't merge SOS node into a tree with > 1 SOS node", node);
            }
            sOSMarkerSegment.updateFromNativeNode(node, false);
            return;
        }
        this.markerSequence.add(new SOSMarkerSegment(node));
    }

    private void mergeStandardTree(Node node) throws IIOInvalidTreeException, DOMException {
        this.transparencyDone = false;
        NodeList childNodes = node.getChildNodes();
        for (int i2 = 0; i2 < childNodes.getLength(); i2++) {
            Node nodeItem = childNodes.item(i2);
            String nodeName = nodeItem.getNodeName();
            if (nodeName.equals("Chroma")) {
                mergeStandardChromaNode(nodeItem, childNodes);
            } else if (nodeName.equals("Compression")) {
                mergeStandardCompressionNode(nodeItem);
            } else if (nodeName.equals("Data")) {
                mergeStandardDataNode(nodeItem);
            } else if (nodeName.equals("Dimension")) {
                mergeStandardDimensionNode(nodeItem);
            } else if (nodeName.equals("Document")) {
                mergeStandardDocumentNode(nodeItem);
            } else if (nodeName.equals("Text")) {
                mergeStandardTextNode(nodeItem);
            } else if (nodeName.equals("Transparency")) {
                mergeStandardTransparencyNode(nodeItem);
            } else {
                throw new IIOInvalidTreeException("Invalid node: " + nodeName, nodeItem);
            }
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    private void mergeStandardChromaNode(Node node, NodeList nodeList) throws IIOInvalidTreeException, DOMException {
        int i2;
        if (this.transparencyDone) {
            throw new IIOInvalidTreeException("Transparency node must follow Chroma node", node);
        }
        Node firstChild = node.getFirstChild();
        if (firstChild == null || !firstChild.getNodeName().equals("ColorSpaceType")) {
            return;
        }
        String nodeValue = firstChild.getAttributes().getNamedItem("name").getNodeValue();
        boolean z2 = false;
        boolean z3 = false;
        int i3 = 0;
        boolean z4 = false;
        byte[] bArr = {1, 2, 3, 4};
        if (nodeValue.equals("GRAY")) {
            i2 = 1;
            z2 = true;
        } else if (nodeValue.equals("YCbCr")) {
            i2 = 3;
            z2 = true;
            z4 = true;
        } else if (nodeValue.equals("PhotoYCC")) {
            i2 = 3;
            z3 = true;
            i3 = 1;
            bArr[0] = 89;
            bArr[1] = 67;
            bArr[2] = 99;
        } else if (nodeValue.equals("RGB")) {
            i2 = 3;
            z3 = true;
            i3 = 0;
            bArr[0] = 82;
            bArr[1] = 71;
            bArr[2] = 66;
        } else if (nodeValue.equals("XYZ") || nodeValue.equals("Lab") || nodeValue.equals("Luv") || nodeValue.equals("YxY") || nodeValue.equals("HSV") || nodeValue.equals("HLS") || nodeValue.equals("CMY") || nodeValue.equals("3CLR")) {
            i2 = 3;
        } else if (nodeValue.equals("YCCK")) {
            i2 = 4;
            z3 = true;
            i3 = 2;
            z4 = true;
        } else if (nodeValue.equals("CMYK")) {
            i2 = 4;
            z3 = true;
            i3 = 0;
        } else if (nodeValue.equals("4CLR")) {
            i2 = 4;
        } else {
            return;
        }
        boolean zWantAlpha = false;
        int i4 = 0;
        while (true) {
            if (i4 >= nodeList.getLength()) {
                break;
            }
            Node nodeItem = nodeList.item(i4);
            if (!nodeItem.getNodeName().equals("Transparency")) {
                i4++;
            } else {
                zWantAlpha = wantAlpha(nodeItem);
                break;
            }
        }
        if (zWantAlpha) {
            i2++;
            z2 = false;
            if (bArr[0] == 82) {
                bArr[3] = 65;
                z3 = false;
            }
        }
        JFIFMarkerSegment jFIFMarkerSegment = (JFIFMarkerSegment) findMarkerSegment(JFIFMarkerSegment.class, true);
        AdobeMarkerSegment adobeMarkerSegment = (AdobeMarkerSegment) findMarkerSegment(AdobeMarkerSegment.class, true);
        SOFMarkerSegment sOFMarkerSegment = (SOFMarkerSegment) findMarkerSegment(SOFMarkerSegment.class, true);
        SOSMarkerSegment sOSMarkerSegment = (SOSMarkerSegment) findMarkerSegment(SOSMarkerSegment.class, true);
        if (sOFMarkerSegment != null && sOFMarkerSegment.tag == 194 && sOFMarkerSegment.componentSpecs.length != i2 && sOSMarkerSegment != null) {
            return;
        }
        if (!z2 && jFIFMarkerSegment != null) {
            this.markerSequence.remove(jFIFMarkerSegment);
        }
        if (z2 && !this.isStream) {
            this.markerSequence.add(0, new JFIFMarkerSegment());
        }
        if (z3) {
            if (adobeMarkerSegment == null && !this.isStream) {
                insertAdobeMarkerSegment(new AdobeMarkerSegment(i3));
            } else {
                adobeMarkerSegment.transform = i3;
            }
        } else if (adobeMarkerSegment != null) {
            this.markerSequence.remove(adobeMarkerSegment);
        }
        boolean z5 = false;
        boolean z6 = false;
        int[] iArr = z4 ? new int[]{0, 1, 1, 0} : new int[]{0, 0, 0, 0};
        if (sOFMarkerSegment != null) {
            SOFMarkerSegment.ComponentSpec[] componentSpecArr = sOFMarkerSegment.componentSpecs;
            boolean z7 = sOFMarkerSegment.tag == 194;
            this.markerSequence.set(this.markerSequence.indexOf(sOFMarkerSegment), new SOFMarkerSegment(z7, false, z4, bArr, i2));
            for (int i5 = 0; i5 < componentSpecArr.length; i5++) {
                if (componentSpecArr[i5].QtableSelector != iArr[i5]) {
                    z5 = true;
                }
            }
            if (z7) {
                boolean z8 = false;
                for (int i6 = 0; i6 < componentSpecArr.length; i6++) {
                    if (bArr[i6] != componentSpecArr[i6].componentId) {
                        z8 = true;
                    }
                }
                if (z8) {
                    for (MarkerSegment markerSegment : this.markerSequence) {
                        if (markerSegment instanceof SOSMarkerSegment) {
                            SOSMarkerSegment sOSMarkerSegment2 = (SOSMarkerSegment) markerSegment;
                            for (int i7 = 0; i7 < sOSMarkerSegment2.componentSpecs.length; i7++) {
                                int i8 = sOSMarkerSegment2.componentSpecs[i7].componentSelector;
                                for (int i9 = 0; i9 < componentSpecArr.length; i9++) {
                                    if (componentSpecArr[i9].componentId == i8) {
                                        sOSMarkerSegment2.componentSpecs[i7].componentSelector = bArr[i9];
                                    }
                                }
                            }
                        }
                    }
                }
            } else if (sOSMarkerSegment != null) {
                for (int i10 = 0; i10 < sOSMarkerSegment.componentSpecs.length; i10++) {
                    if (sOSMarkerSegment.componentSpecs[i10].dcHuffTable != iArr[i10] || sOSMarkerSegment.componentSpecs[i10].acHuffTable != iArr[i10]) {
                        z6 = true;
                    }
                }
                this.markerSequence.set(this.markerSequence.indexOf(sOSMarkerSegment), new SOSMarkerSegment(z4, bArr, i2));
            }
        } else if (this.isStream) {
            z5 = true;
            z6 = true;
        }
        if (z5) {
            ArrayList arrayList = new ArrayList();
            for (MarkerSegment markerSegment2 : this.markerSequence) {
                if (markerSegment2 instanceof DQTMarkerSegment) {
                    arrayList.add(markerSegment2);
                }
            }
            if (!arrayList.isEmpty() && z4) {
                boolean z9 = false;
                Iterator<E> it = arrayList.iterator();
                while (it.hasNext()) {
                    Iterator it2 = ((DQTMarkerSegment) it.next()).tables.iterator();
                    while (it2.hasNext()) {
                        if (((DQTMarkerSegment.Qtable) it2.next()).tableID == 1) {
                            z9 = true;
                        }
                    }
                }
                if (!z9) {
                    DQTMarkerSegment.Qtable qtable = null;
                    Iterator<E> it3 = arrayList.iterator();
                    while (it3.hasNext()) {
                        for (DQTMarkerSegment.Qtable qtable2 : ((DQTMarkerSegment) it3.next()).tables) {
                            if (qtable2.tableID == 0) {
                                qtable = qtable2;
                            }
                        }
                    }
                    DQTMarkerSegment dQTMarkerSegment = (DQTMarkerSegment) arrayList.get(arrayList.size() - 1);
                    dQTMarkerSegment.tables.add(dQTMarkerSegment.getChromaForLuma(qtable));
                }
            }
        }
        if (z6) {
            ArrayList arrayList2 = new ArrayList();
            for (MarkerSegment markerSegment3 : this.markerSequence) {
                if (markerSegment3 instanceof DHTMarkerSegment) {
                    arrayList2.add(markerSegment3);
                }
            }
            if (!arrayList2.isEmpty() && z4) {
                boolean z10 = false;
                Iterator<E> it4 = arrayList2.iterator();
                while (it4.hasNext()) {
                    Iterator it5 = ((DHTMarkerSegment) it4.next()).tables.iterator();
                    while (it5.hasNext()) {
                        if (((DHTMarkerSegment.Htable) it5.next()).tableID == 1) {
                            z10 = true;
                        }
                    }
                }
                if (!z10) {
                    DHTMarkerSegment dHTMarkerSegment = (DHTMarkerSegment) arrayList2.get(arrayList2.size() - 1);
                    dHTMarkerSegment.addHtable(JPEGHuffmanTable.StdDCLuminance, true, 1);
                    dHTMarkerSegment.addHtable(JPEGHuffmanTable.StdACLuminance, true, 1);
                }
            }
        }
    }

    private boolean wantAlpha(Node node) {
        boolean z2 = false;
        Node firstChild = node.getFirstChild();
        if (firstChild.getNodeName().equals(SoftMask.SOFT_MASK_TYPE_ALPHA) && firstChild.hasAttributes() && !firstChild.getAttributes().getNamedItem("value").getNodeValue().equals(Separation.COLORANT_NONE)) {
            z2 = true;
        }
        this.transparencyDone = true;
        return z2;
    }

    private void mergeStandardCompressionNode(Node node) throws IIOInvalidTreeException {
    }

    private void mergeStandardDataNode(Node node) throws IIOInvalidTreeException {
    }

    private void mergeStandardDimensionNode(Node node) throws IIOInvalidTreeException {
        int length;
        JFIFMarkerSegment jFIFMarkerSegment = (JFIFMarkerSegment) findMarkerSegment(JFIFMarkerSegment.class, true);
        if (jFIFMarkerSegment == null) {
            boolean z2 = false;
            SOFMarkerSegment sOFMarkerSegment = (SOFMarkerSegment) findMarkerSegment(SOFMarkerSegment.class, true);
            if (sOFMarkerSegment != null && ((length = sOFMarkerSegment.componentSpecs.length) == 1 || length == 3)) {
                z2 = true;
                for (int i2 = 0; i2 < sOFMarkerSegment.componentSpecs.length; i2++) {
                    if (sOFMarkerSegment.componentSpecs[i2].componentId != i2 + 1) {
                        z2 = false;
                    }
                }
                AdobeMarkerSegment adobeMarkerSegment = (AdobeMarkerSegment) findMarkerSegment(AdobeMarkerSegment.class, true);
                if (adobeMarkerSegment != null) {
                    if (adobeMarkerSegment.transform != (length == 1 ? 0 : 1)) {
                        z2 = false;
                    }
                }
            }
            if (z2) {
                jFIFMarkerSegment = new JFIFMarkerSegment();
                this.markerSequence.add(0, jFIFMarkerSegment);
            }
        }
        if (jFIFMarkerSegment != null) {
            NodeList childNodes = node.getChildNodes();
            for (int i3 = 0; i3 < childNodes.getLength(); i3++) {
                Node nodeItem = childNodes.item(i3);
                NamedNodeMap attributes = nodeItem.getAttributes();
                String nodeName = nodeItem.getNodeName();
                if (nodeName.equals("PixelAspectRatio")) {
                    Point pointFindIntegerRatio = findIntegerRatio(Float.parseFloat(attributes.getNamedItem("value").getNodeValue()));
                    jFIFMarkerSegment.resUnits = 0;
                    jFIFMarkerSegment.Xdensity = pointFindIntegerRatio.f12370x;
                    jFIFMarkerSegment.Xdensity = pointFindIntegerRatio.f12371y;
                } else if (nodeName.equals("HorizontalPixelSize")) {
                    int iRound = (int) Math.round(1.0d / (Float.parseFloat(attributes.getNamedItem("value").getNodeValue()) * 10.0d));
                    jFIFMarkerSegment.resUnits = 2;
                    jFIFMarkerSegment.Xdensity = iRound;
                } else if (nodeName.equals("VerticalPixelSize")) {
                    int iRound2 = (int) Math.round(1.0d / (Float.parseFloat(attributes.getNamedItem("value").getNodeValue()) * 10.0d));
                    jFIFMarkerSegment.resUnits = 2;
                    jFIFMarkerSegment.Ydensity = iRound2;
                }
            }
        }
    }

    private static Point findIntegerRatio(float f2) {
        float fAbs = Math.abs(f2);
        if (fAbs <= 0.005f) {
            return new Point(1, 255);
        }
        if (fAbs >= 255.0f) {
            return new Point(255, 1);
        }
        boolean z2 = false;
        if (fAbs < 1.0d) {
            fAbs = 1.0f / fAbs;
            z2 = true;
        }
        int i2 = 1;
        int iRound = Math.round(fAbs);
        float fAbs2 = Math.abs(fAbs - iRound);
        while (fAbs2 > 0.005f) {
            i2++;
            iRound = Math.round(i2 * fAbs);
            fAbs2 = Math.abs(fAbs - (iRound / i2));
        }
        return z2 ? new Point(i2, iRound) : new Point(iRound, i2);
    }

    private void mergeStandardDocumentNode(Node node) throws IIOInvalidTreeException {
    }

    private void mergeStandardTextNode(Node node) throws IIOInvalidTreeException {
        NodeList childNodes = node.getChildNodes();
        for (int i2 = 0; i2 < childNodes.getLength(); i2++) {
            NamedNodeMap attributes = childNodes.item(i2).getAttributes();
            Node namedItem = attributes.getNamedItem("compression");
            boolean z2 = true;
            if (namedItem != null && !namedItem.getNodeValue().equals(Separation.COLORANT_NONE)) {
                z2 = false;
            }
            if (z2) {
                insertCOMMarkerSegment(new COMMarkerSegment(attributes.getNamedItem("value").getNodeValue()));
            }
        }
    }

    private void mergeStandardTransparencyNode(Node node) throws IIOInvalidTreeException {
        if (!this.transparencyDone && !this.isStream) {
            boolean zWantAlpha = wantAlpha(node);
            JFIFMarkerSegment jFIFMarkerSegment = (JFIFMarkerSegment) findMarkerSegment(JFIFMarkerSegment.class, true);
            AdobeMarkerSegment adobeMarkerSegment = (AdobeMarkerSegment) findMarkerSegment(AdobeMarkerSegment.class, true);
            SOFMarkerSegment sOFMarkerSegment = (SOFMarkerSegment) findMarkerSegment(SOFMarkerSegment.class, true);
            SOSMarkerSegment sOSMarkerSegment = (SOSMarkerSegment) findMarkerSegment(SOSMarkerSegment.class, true);
            if ((sOFMarkerSegment == null || sOFMarkerSegment.tag != 194) && sOFMarkerSegment != null) {
                int length = sOFMarkerSegment.componentSpecs.length;
                if ((length == 2 || length == 4) != zWantAlpha) {
                    if (zWantAlpha) {
                        int i2 = length + 1;
                        if (jFIFMarkerSegment != null) {
                            this.markerSequence.remove(jFIFMarkerSegment);
                        }
                        if (adobeMarkerSegment != null) {
                            adobeMarkerSegment.transform = 0;
                        }
                        SOFMarkerSegment.ComponentSpec[] componentSpecArr = new SOFMarkerSegment.ComponentSpec[i2];
                        for (int i3 = 0; i3 < sOFMarkerSegment.componentSpecs.length; i3++) {
                            componentSpecArr[i3] = sOFMarkerSegment.componentSpecs[i3];
                        }
                        byte b2 = (byte) (((byte) sOFMarkerSegment.componentSpecs[0].componentId) > 1 ? 65 : 4);
                        componentSpecArr[i2 - 1] = sOFMarkerSegment.getComponentSpec(b2, sOFMarkerSegment.componentSpecs[0].HsamplingFactor, sOFMarkerSegment.componentSpecs[0].QtableSelector);
                        sOFMarkerSegment.componentSpecs = componentSpecArr;
                        SOSMarkerSegment.ScanComponentSpec[] scanComponentSpecArr = new SOSMarkerSegment.ScanComponentSpec[i2];
                        for (int i4 = 0; i4 < sOSMarkerSegment.componentSpecs.length; i4++) {
                            scanComponentSpecArr[i4] = sOSMarkerSegment.componentSpecs[i4];
                        }
                        scanComponentSpecArr[i2 - 1] = sOSMarkerSegment.getScanComponentSpec(b2, 0);
                        sOSMarkerSegment.componentSpecs = scanComponentSpecArr;
                        return;
                    }
                    int i5 = length - 1;
                    SOFMarkerSegment.ComponentSpec[] componentSpecArr2 = new SOFMarkerSegment.ComponentSpec[i5];
                    for (int i6 = 0; i6 < i5; i6++) {
                        componentSpecArr2[i6] = sOFMarkerSegment.componentSpecs[i6];
                    }
                    sOFMarkerSegment.componentSpecs = componentSpecArr2;
                    SOSMarkerSegment.ScanComponentSpec[] scanComponentSpecArr2 = new SOSMarkerSegment.ScanComponentSpec[i5];
                    for (int i7 = 0; i7 < i5; i7++) {
                        scanComponentSpecArr2[i7] = sOSMarkerSegment.componentSpecs[i7];
                    }
                    sOSMarkerSegment.componentSpecs = scanComponentSpecArr2;
                }
            }
        }
    }

    @Override // javax.imageio.metadata.IIOMetadata
    public void setFromTree(String str, Node node) throws IIOInvalidTreeException {
        if (str == null) {
            throw new IllegalArgumentException("null formatName!");
        }
        if (node == null) {
            throw new IllegalArgumentException("null root!");
        }
        if (this.isStream && str.equals(JPEG.nativeStreamMetadataFormatName)) {
            setFromNativeTree(node);
            return;
        }
        if (!this.isStream && str.equals(JPEG.nativeImageMetadataFormatName)) {
            setFromNativeTree(node);
        } else {
            if (!this.isStream && str.equals(IIOMetadataFormatImpl.standardMetadataFormatName)) {
                super.setFromTree(str, node);
                return;
            }
            throw new IllegalArgumentException("Unsupported format name: " + str);
        }
    }

    private void setFromNativeTree(Node node) throws IIOInvalidTreeException {
        if (this.resetSequence == null) {
            this.resetSequence = this.markerSequence;
        }
        this.markerSequence = new ArrayList();
        String nodeName = node.getNodeName();
        if (nodeName != (this.isStream ? JPEG.nativeStreamMetadataFormatName : JPEG.nativeImageMetadataFormatName)) {
            throw new IIOInvalidTreeException("Invalid root node name: " + nodeName, node);
        }
        if (!this.isStream) {
            if (node.getChildNodes().getLength() != 2) {
                throw new IIOInvalidTreeException("JPEGvariety and markerSequence nodes must be present", node);
            }
            Node firstChild = node.getFirstChild();
            if (firstChild.getChildNodes().getLength() != 0) {
                this.markerSequence.add(new JFIFMarkerSegment(firstChild.getFirstChild()));
            }
        }
        setFromMarkerSequenceNode(this.isStream ? node : node.getLastChild());
    }

    void setFromMarkerSequenceNode(Node node) throws IIOInvalidTreeException {
        NodeList childNodes = node.getChildNodes();
        for (int i2 = 0; i2 < childNodes.getLength(); i2++) {
            Node nodeItem = childNodes.item(i2);
            String nodeName = nodeItem.getNodeName();
            if (nodeName.equals("dqt")) {
                this.markerSequence.add(new DQTMarkerSegment(nodeItem));
            } else if (nodeName.equals("dht")) {
                this.markerSequence.add(new DHTMarkerSegment(nodeItem));
            } else if (nodeName.equals("dri")) {
                this.markerSequence.add(new DRIMarkerSegment(nodeItem));
            } else if (nodeName.equals("com")) {
                this.markerSequence.add(new COMMarkerSegment(nodeItem));
            } else if (nodeName.equals("app14Adobe")) {
                this.markerSequence.add(new AdobeMarkerSegment(nodeItem));
            } else if (nodeName.equals("unknown")) {
                this.markerSequence.add(new MarkerSegment(nodeItem));
            } else if (nodeName.equals("sof")) {
                this.markerSequence.add(new SOFMarkerSegment(nodeItem));
            } else if (nodeName.equals("sos")) {
                this.markerSequence.add(new SOSMarkerSegment(nodeItem));
            } else {
                throw new IIOInvalidTreeException("Invalid " + (this.isStream ? "stream " : "image ") + "child: " + nodeName, nodeItem);
            }
        }
    }

    private boolean isConsistent() {
        SOFMarkerSegment sOFMarkerSegment = (SOFMarkerSegment) findMarkerSegment(SOFMarkerSegment.class, true);
        JFIFMarkerSegment jFIFMarkerSegment = (JFIFMarkerSegment) findMarkerSegment(JFIFMarkerSegment.class, true);
        AdobeMarkerSegment adobeMarkerSegment = (AdobeMarkerSegment) findMarkerSegment(AdobeMarkerSegment.class, true);
        boolean z2 = true;
        if (!this.isStream) {
            if (sOFMarkerSegment != null) {
                int length = sOFMarkerSegment.componentSpecs.length;
                int iCountScanBands = countScanBands();
                if (iCountScanBands != 0 && iCountScanBands != length) {
                    z2 = false;
                }
                if (jFIFMarkerSegment != null) {
                    if (length != 1 && length != 3) {
                        z2 = false;
                    }
                    for (int i2 = 0; i2 < length; i2++) {
                        if (sOFMarkerSegment.componentSpecs[i2].componentId != i2 + 1) {
                            z2 = false;
                        }
                    }
                    if (adobeMarkerSegment != null && ((length == 1 && adobeMarkerSegment.transform != 0) || (length == 3 && adobeMarkerSegment.transform != 1))) {
                        z2 = false;
                    }
                }
            } else {
                SOSMarkerSegment sOSMarkerSegment = (SOSMarkerSegment) findMarkerSegment(SOSMarkerSegment.class, true);
                if (jFIFMarkerSegment != null || adobeMarkerSegment != null || sOFMarkerSegment != null || sOSMarkerSegment != null) {
                    z2 = false;
                }
            }
        }
        return z2;
    }

    private int countScanBands() {
        ArrayList arrayList = new ArrayList();
        for (MarkerSegment markerSegment : this.markerSequence) {
            if (markerSegment instanceof SOSMarkerSegment) {
                for (SOSMarkerSegment.ScanComponentSpec scanComponentSpec : ((SOSMarkerSegment) markerSegment).componentSpecs) {
                    Integer num = new Integer(scanComponentSpec.componentSelector);
                    if (!arrayList.contains(num)) {
                        arrayList.add(num);
                    }
                }
            }
        }
        return arrayList.size();
    }

    void writeToStream(ImageOutputStream imageOutputStream, boolean z2, boolean z3, List list, ICC_Profile iCC_Profile, boolean z4, int i2, JPEGImageWriter jPEGImageWriter) throws IOException {
        if (z3) {
            JFIFMarkerSegment.writeDefaultJFIF(imageOutputStream, list, iCC_Profile, jPEGImageWriter);
            if (!z4 && i2 != -1 && i2 != 0 && i2 != 1) {
                z4 = true;
                jPEGImageWriter.warningOccurred(13);
            }
        }
        for (MarkerSegment markerSegment : this.markerSequence) {
            if (markerSegment instanceof JFIFMarkerSegment) {
                if (!z2) {
                    ((JFIFMarkerSegment) markerSegment).writeWithThumbs(imageOutputStream, list, jPEGImageWriter);
                    if (iCC_Profile != null) {
                        JFIFMarkerSegment.writeICC(iCC_Profile, imageOutputStream);
                    }
                }
            } else if (markerSegment instanceof AdobeMarkerSegment) {
                if (!z4) {
                    if (i2 != -1) {
                        AdobeMarkerSegment adobeMarkerSegment = (AdobeMarkerSegment) markerSegment.clone();
                        adobeMarkerSegment.transform = i2;
                        adobeMarkerSegment.write(imageOutputStream);
                    } else if (z3) {
                        AdobeMarkerSegment adobeMarkerSegment2 = (AdobeMarkerSegment) markerSegment;
                        if (adobeMarkerSegment2.transform == 0 || adobeMarkerSegment2.transform == 1) {
                            adobeMarkerSegment2.write(imageOutputStream);
                        } else {
                            jPEGImageWriter.warningOccurred(13);
                        }
                    } else {
                        markerSegment.write(imageOutputStream);
                    }
                }
            } else {
                markerSegment.write(imageOutputStream);
            }
        }
    }

    @Override // javax.imageio.metadata.IIOMetadata
    public void reset() {
        if (this.resetSequence != null) {
            this.markerSequence = this.resetSequence;
            this.resetSequence = null;
        }
    }

    public void print() {
        for (int i2 = 0; i2 < this.markerSequence.size(); i2++) {
            ((MarkerSegment) this.markerSequence.get(i2)).print();
        }
    }
}
