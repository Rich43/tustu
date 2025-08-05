package com.sun.imageio.plugins.jpeg;

import com.sun.imageio.plugins.jpeg.JPEG;
import java.awt.Point;
import java.awt.color.ColorSpace;
import java.awt.color.ICC_ColorSpace;
import java.awt.color.ICC_Profile;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.ComponentColorModel;
import java.awt.image.DataBufferByte;
import java.awt.image.IndexColorModel;
import java.awt.image.Raster;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import javax.imageio.IIOException;
import javax.imageio.IIOImage;
import javax.imageio.ImageReader;
import javax.imageio.ImageTypeSpecifier;
import javax.imageio.event.IIOReadProgressListener;
import javax.imageio.metadata.IIOInvalidTreeException;
import javax.imageio.metadata.IIOMetadataNode;
import javax.imageio.stream.ImageInputStream;
import javax.imageio.stream.ImageOutputStream;
import javax.imageio.stream.MemoryCacheImageOutputStream;
import org.w3c.dom.DOMException;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/* loaded from: rt.jar:com/sun/imageio/plugins/jpeg/JFIFMarkerSegment.class */
class JFIFMarkerSegment extends MarkerSegment {
    int majorVersion;
    int minorVersion;
    int resUnits;
    int Xdensity;
    int Ydensity;
    int thumbWidth;
    int thumbHeight;
    JFIFThumbRGB thumb;
    ArrayList extSegments;
    ICCMarkerSegment iccSegment;
    private static final int THUMB_JPEG = 16;
    private static final int THUMB_PALETTE = 17;
    private static final int THUMB_UNASSIGNED = 18;
    private static final int THUMB_RGB = 19;
    private static final int DATA_SIZE = 14;
    private static final int ID_SIZE = 5;
    private final int MAX_THUMB_WIDTH = 255;
    private final int MAX_THUMB_HEIGHT = 255;
    private final boolean debug = false;
    private boolean inICC;
    private ICCMarkerSegment tempICCSegment;

    JFIFMarkerSegment() {
        super(224);
        this.thumb = null;
        this.extSegments = new ArrayList();
        this.iccSegment = null;
        this.MAX_THUMB_WIDTH = 255;
        this.MAX_THUMB_HEIGHT = 255;
        this.debug = false;
        this.inICC = false;
        this.tempICCSegment = null;
        this.majorVersion = 1;
        this.minorVersion = 2;
        this.resUnits = 0;
        this.Xdensity = 1;
        this.Ydensity = 1;
        this.thumbWidth = 0;
        this.thumbHeight = 0;
    }

    JFIFMarkerSegment(JPEGBuffer jPEGBuffer) throws IOException {
        super(jPEGBuffer);
        this.thumb = null;
        this.extSegments = new ArrayList();
        this.iccSegment = null;
        this.MAX_THUMB_WIDTH = 255;
        this.MAX_THUMB_HEIGHT = 255;
        this.debug = false;
        this.inICC = false;
        this.tempICCSegment = null;
        jPEGBuffer.bufPtr += 5;
        byte[] bArr = jPEGBuffer.buf;
        int i2 = jPEGBuffer.bufPtr;
        jPEGBuffer.bufPtr = i2 + 1;
        this.majorVersion = bArr[i2];
        byte[] bArr2 = jPEGBuffer.buf;
        int i3 = jPEGBuffer.bufPtr;
        jPEGBuffer.bufPtr = i3 + 1;
        this.minorVersion = bArr2[i3];
        byte[] bArr3 = jPEGBuffer.buf;
        int i4 = jPEGBuffer.bufPtr;
        jPEGBuffer.bufPtr = i4 + 1;
        this.resUnits = bArr3[i4];
        byte[] bArr4 = jPEGBuffer.buf;
        int i5 = jPEGBuffer.bufPtr;
        jPEGBuffer.bufPtr = i5 + 1;
        this.Xdensity = (bArr4[i5] & 255) << 8;
        int i6 = this.Xdensity;
        byte[] bArr5 = jPEGBuffer.buf;
        int i7 = jPEGBuffer.bufPtr;
        jPEGBuffer.bufPtr = i7 + 1;
        this.Xdensity = i6 | (bArr5[i7] & 255);
        byte[] bArr6 = jPEGBuffer.buf;
        int i8 = jPEGBuffer.bufPtr;
        jPEGBuffer.bufPtr = i8 + 1;
        this.Ydensity = (bArr6[i8] & 255) << 8;
        int i9 = this.Ydensity;
        byte[] bArr7 = jPEGBuffer.buf;
        int i10 = jPEGBuffer.bufPtr;
        jPEGBuffer.bufPtr = i10 + 1;
        this.Ydensity = i9 | (bArr7[i10] & 255);
        byte[] bArr8 = jPEGBuffer.buf;
        int i11 = jPEGBuffer.bufPtr;
        jPEGBuffer.bufPtr = i11 + 1;
        this.thumbWidth = bArr8[i11] & 255;
        byte[] bArr9 = jPEGBuffer.buf;
        int i12 = jPEGBuffer.bufPtr;
        jPEGBuffer.bufPtr = i12 + 1;
        this.thumbHeight = bArr9[i12] & 255;
        jPEGBuffer.bufAvail -= 14;
        if (this.thumbWidth > 0) {
            this.thumb = new JFIFThumbRGB(jPEGBuffer, this.thumbWidth, this.thumbHeight);
        }
    }

    JFIFMarkerSegment(Node node) throws IIOInvalidTreeException {
        this();
        updateFromNativeNode(node, true);
    }

    @Override // com.sun.imageio.plugins.jpeg.MarkerSegment
    protected Object clone() {
        JFIFMarkerSegment jFIFMarkerSegment = (JFIFMarkerSegment) super.clone();
        if (!this.extSegments.isEmpty()) {
            jFIFMarkerSegment.extSegments = new ArrayList();
            Iterator it = this.extSegments.iterator();
            while (it.hasNext()) {
                jFIFMarkerSegment.extSegments.add(((JFIFExtensionMarkerSegment) it.next()).clone());
            }
        }
        if (this.iccSegment != null) {
            jFIFMarkerSegment.iccSegment = (ICCMarkerSegment) this.iccSegment.clone();
        }
        return jFIFMarkerSegment;
    }

    void addJFXX(JPEGBuffer jPEGBuffer, JPEGImageReader jPEGImageReader) throws IOException {
        this.extSegments.add(new JFIFExtensionMarkerSegment(jPEGBuffer, jPEGImageReader));
    }

    void addICC(JPEGBuffer jPEGBuffer) throws IOException {
        if (!this.inICC) {
            if (this.iccSegment != null) {
                throw new IIOException("> 1 ICC APP2 Marker Segment not supported");
            }
            this.tempICCSegment = new ICCMarkerSegment(jPEGBuffer);
            if (!this.inICC) {
                this.iccSegment = this.tempICCSegment;
                this.tempICCSegment = null;
                return;
            }
            return;
        }
        if (this.tempICCSegment.addData(jPEGBuffer)) {
            this.iccSegment = this.tempICCSegment;
            this.tempICCSegment = null;
        }
    }

    void addICC(ICC_ColorSpace iCC_ColorSpace) throws IOException {
        if (this.iccSegment != null) {
            throw new IIOException("> 1 ICC APP2 Marker Segment not supported");
        }
        this.iccSegment = new ICCMarkerSegment(iCC_ColorSpace);
    }

    @Override // com.sun.imageio.plugins.jpeg.MarkerSegment
    IIOMetadataNode getNativeNode() throws DOMException {
        IIOMetadataNode iIOMetadataNode = new IIOMetadataNode("app0JFIF");
        iIOMetadataNode.setAttribute("majorVersion", Integer.toString(this.majorVersion));
        iIOMetadataNode.setAttribute("minorVersion", Integer.toString(this.minorVersion));
        iIOMetadataNode.setAttribute("resUnits", Integer.toString(this.resUnits));
        iIOMetadataNode.setAttribute("Xdensity", Integer.toString(this.Xdensity));
        iIOMetadataNode.setAttribute("Ydensity", Integer.toString(this.Ydensity));
        iIOMetadataNode.setAttribute("thumbWidth", Integer.toString(this.thumbWidth));
        iIOMetadataNode.setAttribute("thumbHeight", Integer.toString(this.thumbHeight));
        if (!this.extSegments.isEmpty()) {
            IIOMetadataNode iIOMetadataNode2 = new IIOMetadataNode("JFXX");
            iIOMetadataNode.appendChild(iIOMetadataNode2);
            Iterator it = this.extSegments.iterator();
            while (it.hasNext()) {
                iIOMetadataNode2.appendChild(((JFIFExtensionMarkerSegment) it.next()).getNativeNode());
            }
        }
        if (this.iccSegment != null) {
            iIOMetadataNode.appendChild(this.iccSegment.getNativeNode());
        }
        return iIOMetadataNode;
    }

    void updateFromNativeNode(Node node, boolean z2) throws IIOInvalidTreeException {
        NamedNodeMap attributes = node.getAttributes();
        if (attributes.getLength() > 0) {
            int attributeValue = getAttributeValue(node, attributes, "majorVersion", 0, 255, false);
            this.majorVersion = attributeValue != -1 ? attributeValue : this.majorVersion;
            int attributeValue2 = getAttributeValue(node, attributes, "minorVersion", 0, 255, false);
            this.minorVersion = attributeValue2 != -1 ? attributeValue2 : this.minorVersion;
            int attributeValue3 = getAttributeValue(node, attributes, "resUnits", 0, 2, false);
            this.resUnits = attributeValue3 != -1 ? attributeValue3 : this.resUnits;
            int attributeValue4 = getAttributeValue(node, attributes, "Xdensity", 1, 65535, false);
            this.Xdensity = attributeValue4 != -1 ? attributeValue4 : this.Xdensity;
            int attributeValue5 = getAttributeValue(node, attributes, "Ydensity", 1, 65535, false);
            this.Ydensity = attributeValue5 != -1 ? attributeValue5 : this.Ydensity;
            int attributeValue6 = getAttributeValue(node, attributes, "thumbWidth", 0, 255, false);
            this.thumbWidth = attributeValue6 != -1 ? attributeValue6 : this.thumbWidth;
            int attributeValue7 = getAttributeValue(node, attributes, "thumbHeight", 0, 255, false);
            this.thumbHeight = attributeValue7 != -1 ? attributeValue7 : this.thumbHeight;
        }
        if (node.hasChildNodes()) {
            NodeList childNodes = node.getChildNodes();
            int length = childNodes.getLength();
            if (length > 2) {
                throw new IIOInvalidTreeException("app0JFIF node cannot have > 2 children", node);
            }
            for (int i2 = 0; i2 < length; i2++) {
                Node nodeItem = childNodes.item(i2);
                String nodeName = nodeItem.getNodeName();
                if (nodeName.equals("JFXX")) {
                    if (!this.extSegments.isEmpty() && z2) {
                        throw new IIOInvalidTreeException("app0JFIF node cannot have > 1 JFXX node", node);
                    }
                    NodeList childNodes2 = nodeItem.getChildNodes();
                    int length2 = childNodes2.getLength();
                    for (int i3 = 0; i3 < length2; i3++) {
                        this.extSegments.add(new JFIFExtensionMarkerSegment(childNodes2.item(i3)));
                    }
                }
                if (nodeName.equals("app2ICC")) {
                    if (this.iccSegment != null && z2) {
                        throw new IIOInvalidTreeException("> 1 ICC APP2 Marker Segment not supported", node);
                    }
                    this.iccSegment = new ICCMarkerSegment(nodeItem);
                }
            }
        }
    }

    int getThumbnailWidth(int i2) {
        if (this.thumb != null) {
            if (i2 == 0) {
                return this.thumb.getWidth();
            }
            i2--;
        }
        return ((JFIFExtensionMarkerSegment) this.extSegments.get(i2)).thumb.getWidth();
    }

    int getThumbnailHeight(int i2) {
        if (this.thumb != null) {
            if (i2 == 0) {
                return this.thumb.getHeight();
            }
            i2--;
        }
        return ((JFIFExtensionMarkerSegment) this.extSegments.get(i2)).thumb.getHeight();
    }

    BufferedImage getThumbnail(ImageInputStream imageInputStream, int i2, JPEGImageReader jPEGImageReader) throws IOException {
        BufferedImage thumbnail;
        jPEGImageReader.thumbnailStarted(i2);
        if (this.thumb != null && i2 == 0) {
            thumbnail = this.thumb.getThumbnail(imageInputStream, jPEGImageReader);
        } else {
            if (this.thumb != null) {
                i2--;
            }
            thumbnail = ((JFIFExtensionMarkerSegment) this.extSegments.get(i2)).thumb.getThumbnail(imageInputStream, jPEGImageReader);
        }
        jPEGImageReader.thumbnailComplete();
        return thumbnail;
    }

    void write(ImageOutputStream imageOutputStream, JPEGImageWriter jPEGImageWriter) throws IOException {
        write(imageOutputStream, null, jPEGImageWriter);
    }

    void write(ImageOutputStream imageOutputStream, BufferedImage bufferedImage, JPEGImageWriter jPEGImageWriter) throws IOException {
        int iMin = 0;
        int iMin2 = 0;
        int length = 0;
        int[] pixels = null;
        if (bufferedImage != null) {
            int width = bufferedImage.getWidth();
            int height = bufferedImage.getHeight();
            if (width > 255 || height > 255) {
                jPEGImageWriter.warningOccurred(12);
            }
            iMin = Math.min(width, 255);
            iMin2 = Math.min(height, 255);
            pixels = bufferedImage.getRaster().getPixels(0, 0, iMin, iMin2, (int[]) null);
            length = pixels.length;
        }
        this.length = 16 + length;
        writeTag(imageOutputStream);
        imageOutputStream.write(new byte[]{74, 70, 73, 70, 0});
        imageOutputStream.write(this.majorVersion);
        imageOutputStream.write(this.minorVersion);
        imageOutputStream.write(this.resUnits);
        write2bytes(imageOutputStream, this.Xdensity);
        write2bytes(imageOutputStream, this.Ydensity);
        imageOutputStream.write(iMin);
        imageOutputStream.write(iMin2);
        if (pixels != null) {
            jPEGImageWriter.thumbnailStarted(0);
            writeThumbnailData(imageOutputStream, pixels, jPEGImageWriter);
            jPEGImageWriter.thumbnailComplete();
        }
    }

    void writeThumbnailData(ImageOutputStream imageOutputStream, int[] iArr, JPEGImageWriter jPEGImageWriter) throws IOException {
        int length = iArr.length / 20;
        if (length == 0) {
            length = 1;
        }
        for (int i2 = 0; i2 < iArr.length; i2++) {
            imageOutputStream.write(iArr[i2]);
            if (i2 > length && i2 % length == 0) {
                jPEGImageWriter.thumbnailProgress((i2 * 100.0f) / iArr.length);
            }
        }
    }

    void writeWithThumbs(ImageOutputStream imageOutputStream, List list, JPEGImageWriter jPEGImageWriter) throws IOException {
        if (list != null) {
            JFIFExtensionMarkerSegment jFIFExtensionMarkerSegment = null;
            if (list.size() == 1) {
                if (!this.extSegments.isEmpty()) {
                    jFIFExtensionMarkerSegment = (JFIFExtensionMarkerSegment) this.extSegments.get(0);
                }
                writeThumb(imageOutputStream, (BufferedImage) list.get(0), jFIFExtensionMarkerSegment, 0, true, jPEGImageWriter);
                return;
            }
            write(imageOutputStream, jPEGImageWriter);
            for (int i2 = 0; i2 < list.size(); i2++) {
                JFIFExtensionMarkerSegment jFIFExtensionMarkerSegment2 = null;
                if (i2 < this.extSegments.size()) {
                    jFIFExtensionMarkerSegment2 = (JFIFExtensionMarkerSegment) this.extSegments.get(i2);
                }
                writeThumb(imageOutputStream, (BufferedImage) list.get(i2), jFIFExtensionMarkerSegment2, i2, false, jPEGImageWriter);
            }
            return;
        }
        write(imageOutputStream, jPEGImageWriter);
    }

    private void writeThumb(ImageOutputStream imageOutputStream, BufferedImage bufferedImage, JFIFExtensionMarkerSegment jFIFExtensionMarkerSegment, int i2, boolean z2, JPEGImageWriter jPEGImageWriter) throws IOException {
        ColorModel colorModel = bufferedImage.getColorModel();
        ColorSpace colorSpace = colorModel.getColorSpace();
        if (colorModel instanceof IndexColorModel) {
            if (z2) {
                write(imageOutputStream, jPEGImageWriter);
            }
            if (jFIFExtensionMarkerSegment == null || jFIFExtensionMarkerSegment.code == 17) {
                writeJFXXSegment(i2, bufferedImage, imageOutputStream, jPEGImageWriter);
                return;
            }
            jFIFExtensionMarkerSegment.setThumbnail(((IndexColorModel) colorModel).convertToIntDiscrete(bufferedImage.getRaster(), false));
            jPEGImageWriter.thumbnailStarted(i2);
            jFIFExtensionMarkerSegment.write(imageOutputStream, jPEGImageWriter);
            jPEGImageWriter.thumbnailComplete();
            return;
        }
        if (colorSpace.getType() == 5) {
            if (jFIFExtensionMarkerSegment == null) {
                if (z2) {
                    write(imageOutputStream, bufferedImage, jPEGImageWriter);
                    return;
                } else {
                    writeJFXXSegment(i2, bufferedImage, imageOutputStream, jPEGImageWriter);
                    return;
                }
            }
            if (z2) {
                write(imageOutputStream, jPEGImageWriter);
            }
            if (jFIFExtensionMarkerSegment.code == 17) {
                writeJFXXSegment(i2, bufferedImage, imageOutputStream, jPEGImageWriter);
                jPEGImageWriter.warningOccurred(14);
                return;
            } else {
                jFIFExtensionMarkerSegment.setThumbnail(bufferedImage);
                jPEGImageWriter.thumbnailStarted(i2);
                jFIFExtensionMarkerSegment.write(imageOutputStream, jPEGImageWriter);
                jPEGImageWriter.thumbnailComplete();
                return;
            }
        }
        if (colorSpace.getType() == 6) {
            if (jFIFExtensionMarkerSegment == null) {
                if (z2) {
                    write(imageOutputStream, expandGrayThumb(bufferedImage), jPEGImageWriter);
                    return;
                } else {
                    writeJFXXSegment(i2, bufferedImage, imageOutputStream, jPEGImageWriter);
                    return;
                }
            }
            if (z2) {
                write(imageOutputStream, jPEGImageWriter);
            }
            if (jFIFExtensionMarkerSegment.code == 19) {
                writeJFXXSegment(i2, expandGrayThumb(bufferedImage), imageOutputStream, jPEGImageWriter);
                return;
            }
            if (jFIFExtensionMarkerSegment.code == 16) {
                jFIFExtensionMarkerSegment.setThumbnail(bufferedImage);
                jPEGImageWriter.thumbnailStarted(i2);
                jFIFExtensionMarkerSegment.write(imageOutputStream, jPEGImageWriter);
                jPEGImageWriter.thumbnailComplete();
                return;
            }
            if (jFIFExtensionMarkerSegment.code == 17) {
                writeJFXXSegment(i2, bufferedImage, imageOutputStream, jPEGImageWriter);
                jPEGImageWriter.warningOccurred(15);
                return;
            }
            return;
        }
        jPEGImageWriter.warningOccurred(9);
    }

    /* loaded from: rt.jar:com/sun/imageio/plugins/jpeg/JFIFMarkerSegment$IllegalThumbException.class */
    private class IllegalThumbException extends Exception {
        private IllegalThumbException() {
        }
    }

    private void writeJFXXSegment(int i2, BufferedImage bufferedImage, ImageOutputStream imageOutputStream, JPEGImageWriter jPEGImageWriter) throws IOException {
        try {
            JFIFExtensionMarkerSegment jFIFExtensionMarkerSegment = new JFIFExtensionMarkerSegment(bufferedImage);
            jPEGImageWriter.thumbnailStarted(i2);
            jFIFExtensionMarkerSegment.write(imageOutputStream, jPEGImageWriter);
            jPEGImageWriter.thumbnailComplete();
        } catch (IllegalThumbException e2) {
            jPEGImageWriter.warningOccurred(9);
        }
    }

    private static BufferedImage expandGrayThumb(BufferedImage bufferedImage) {
        BufferedImage bufferedImage2 = new BufferedImage(bufferedImage.getWidth(), bufferedImage.getHeight(), 1);
        bufferedImage2.getGraphics().drawImage(bufferedImage, 0, 0, null);
        return bufferedImage2;
    }

    static void writeDefaultJFIF(ImageOutputStream imageOutputStream, List list, ICC_Profile iCC_Profile, JPEGImageWriter jPEGImageWriter) throws IOException {
        new JFIFMarkerSegment().writeWithThumbs(imageOutputStream, list, jPEGImageWriter);
        if (iCC_Profile != null) {
            writeICC(iCC_Profile, imageOutputStream);
        }
    }

    @Override // com.sun.imageio.plugins.jpeg.MarkerSegment
    void print() {
        printTag("JFIF");
        System.out.print("Version ");
        System.out.print(this.majorVersion);
        System.out.println(".0" + Integer.toString(this.minorVersion));
        System.out.print("Resolution units: ");
        System.out.println(this.resUnits);
        System.out.print("X density: ");
        System.out.println(this.Xdensity);
        System.out.print("Y density: ");
        System.out.println(this.Ydensity);
        System.out.print("Thumbnail Width: ");
        System.out.println(this.thumbWidth);
        System.out.print("Thumbnail Height: ");
        System.out.println(this.thumbHeight);
        if (!this.extSegments.isEmpty()) {
            Iterator it = this.extSegments.iterator();
            while (it.hasNext()) {
                ((JFIFExtensionMarkerSegment) it.next()).print();
            }
        }
        if (this.iccSegment != null) {
            this.iccSegment.print();
        }
    }

    /* loaded from: rt.jar:com/sun/imageio/plugins/jpeg/JFIFMarkerSegment$JFIFExtensionMarkerSegment.class */
    class JFIFExtensionMarkerSegment extends MarkerSegment {
        int code;
        JFIFThumb thumb;
        private static final int DATA_SIZE = 6;
        private static final int ID_SIZE = 5;

        JFIFExtensionMarkerSegment(JPEGBuffer jPEGBuffer, JPEGImageReader jPEGImageReader) throws IOException {
            super(jPEGBuffer);
            jPEGBuffer.bufPtr += 5;
            byte[] bArr = jPEGBuffer.buf;
            int i2 = jPEGBuffer.bufPtr;
            jPEGBuffer.bufPtr = i2 + 1;
            this.code = bArr[i2] & 255;
            jPEGBuffer.bufAvail -= 6;
            if (this.code == 16) {
                this.thumb = JFIFMarkerSegment.this.new JFIFThumbJPEG(jPEGBuffer, this.length, jPEGImageReader);
                return;
            }
            jPEGBuffer.loadBuf(2);
            byte[] bArr2 = jPEGBuffer.buf;
            int i3 = jPEGBuffer.bufPtr;
            jPEGBuffer.bufPtr = i3 + 1;
            int i4 = bArr2[i3] & 255;
            byte[] bArr3 = jPEGBuffer.buf;
            int i5 = jPEGBuffer.bufPtr;
            jPEGBuffer.bufPtr = i5 + 1;
            int i6 = bArr3[i5] & 255;
            jPEGBuffer.bufAvail -= 2;
            if (this.code == 17) {
                this.thumb = JFIFMarkerSegment.this.new JFIFThumbPalette(jPEGBuffer, i4, i6);
            } else {
                this.thumb = JFIFMarkerSegment.this.new JFIFThumbRGB(jPEGBuffer, i4, i6);
            }
        }

        JFIFExtensionMarkerSegment(Node node) throws IIOInvalidTreeException {
            super(224);
            NamedNodeMap attributes = node.getAttributes();
            if (attributes.getLength() > 0) {
                this.code = getAttributeValue(node, attributes, "extensionCode", 16, 19, false);
                if (this.code == 18) {
                    throw new IIOInvalidTreeException("invalid extensionCode attribute value", node);
                }
            } else {
                this.code = 18;
            }
            if (node.getChildNodes().getLength() != 1) {
                throw new IIOInvalidTreeException("app0JFXX node must have exactly 1 child", node);
            }
            Node firstChild = node.getFirstChild();
            String nodeName = firstChild.getNodeName();
            if (nodeName.equals("JFIFthumbJPEG")) {
                if (this.code == 18) {
                    this.code = 16;
                }
                this.thumb = JFIFMarkerSegment.this.new JFIFThumbJPEG(firstChild);
            } else if (nodeName.equals("JFIFthumbPalette")) {
                if (this.code == 18) {
                    this.code = 17;
                }
                this.thumb = JFIFMarkerSegment.this.new JFIFThumbPalette(firstChild);
            } else {
                if (nodeName.equals("JFIFthumbRGB")) {
                    if (this.code == 18) {
                        this.code = 19;
                    }
                    this.thumb = JFIFMarkerSegment.this.new JFIFThumbRGB(firstChild);
                    return;
                }
                throw new IIOInvalidTreeException("unrecognized app0JFXX child node", node);
            }
        }

        JFIFExtensionMarkerSegment(BufferedImage bufferedImage) throws IllegalThumbException {
            super(224);
            ColorModel colorModel = bufferedImage.getColorModel();
            int type = colorModel.getColorSpace().getType();
            if (colorModel.hasAlpha()) {
                throw new IllegalThumbException();
            }
            if (colorModel instanceof IndexColorModel) {
                this.code = 17;
                this.thumb = JFIFMarkerSegment.this.new JFIFThumbPalette(bufferedImage);
            } else if (type == 5) {
                this.code = 19;
                this.thumb = JFIFMarkerSegment.this.new JFIFThumbRGB(bufferedImage);
            } else {
                if (type == 6) {
                    this.code = 16;
                    this.thumb = JFIFMarkerSegment.this.new JFIFThumbJPEG(bufferedImage);
                    return;
                }
                throw new IllegalThumbException();
            }
        }

        void setThumbnail(BufferedImage bufferedImage) {
            try {
                switch (this.code) {
                    case 16:
                        this.thumb = JFIFMarkerSegment.this.new JFIFThumbJPEG(bufferedImage);
                        break;
                    case 17:
                        this.thumb = JFIFMarkerSegment.this.new JFIFThumbPalette(bufferedImage);
                        break;
                    case 19:
                        this.thumb = JFIFMarkerSegment.this.new JFIFThumbRGB(bufferedImage);
                        break;
                }
            } catch (IllegalThumbException e2) {
                throw new InternalError("Illegal thumb in setThumbnail!", e2);
            }
        }

        @Override // com.sun.imageio.plugins.jpeg.MarkerSegment
        protected Object clone() {
            JFIFExtensionMarkerSegment jFIFExtensionMarkerSegment = (JFIFExtensionMarkerSegment) super.clone();
            if (this.thumb != null) {
                jFIFExtensionMarkerSegment.thumb = (JFIFThumb) this.thumb.clone();
            }
            return jFIFExtensionMarkerSegment;
        }

        @Override // com.sun.imageio.plugins.jpeg.MarkerSegment
        IIOMetadataNode getNativeNode() throws DOMException {
            IIOMetadataNode iIOMetadataNode = new IIOMetadataNode("app0JFXX");
            iIOMetadataNode.setAttribute("extensionCode", Integer.toString(this.code));
            iIOMetadataNode.appendChild(this.thumb.getNativeNode());
            return iIOMetadataNode;
        }

        void write(ImageOutputStream imageOutputStream, JPEGImageWriter jPEGImageWriter) throws IOException {
            this.length = 8 + this.thumb.getLength();
            writeTag(imageOutputStream);
            imageOutputStream.write(new byte[]{74, 70, 88, 88, 0});
            imageOutputStream.write(this.code);
            this.thumb.write(imageOutputStream, jPEGImageWriter);
        }

        @Override // com.sun.imageio.plugins.jpeg.MarkerSegment
        void print() {
            printTag("JFXX");
            this.thumb.print();
        }
    }

    /* loaded from: rt.jar:com/sun/imageio/plugins/jpeg/JFIFMarkerSegment$JFIFThumb.class */
    abstract class JFIFThumb implements Cloneable {
        long streamPos;

        abstract int getLength();

        abstract int getWidth();

        abstract int getHeight();

        abstract BufferedImage getThumbnail(ImageInputStream imageInputStream, JPEGImageReader jPEGImageReader) throws IOException;

        abstract void print();

        abstract IIOMetadataNode getNativeNode();

        abstract void write(ImageOutputStream imageOutputStream, JPEGImageWriter jPEGImageWriter) throws IOException;

        protected JFIFThumb() {
            this.streamPos = -1L;
        }

        protected JFIFThumb(JPEGBuffer jPEGBuffer) throws IOException {
            this.streamPos = -1L;
            this.streamPos = jPEGBuffer.getStreamPosition();
        }

        protected Object clone() {
            try {
                return super.clone();
            } catch (CloneNotSupportedException e2) {
                return null;
            }
        }
    }

    /* loaded from: rt.jar:com/sun/imageio/plugins/jpeg/JFIFMarkerSegment$JFIFThumbUncompressed.class */
    abstract class JFIFThumbUncompressed extends JFIFThumb {
        BufferedImage thumbnail;
        int thumbWidth;
        int thumbHeight;
        String name;

        JFIFThumbUncompressed(JPEGBuffer jPEGBuffer, int i2, int i3, int i4, String str) throws IOException {
            super(jPEGBuffer);
            this.thumbnail = null;
            this.thumbWidth = i2;
            this.thumbHeight = i3;
            jPEGBuffer.skipData(i4);
            this.name = str;
        }

        JFIFThumbUncompressed(Node node, String str) throws IIOInvalidTreeException, DOMException, NumberFormatException {
            super();
            this.thumbnail = null;
            this.thumbWidth = 0;
            this.thumbHeight = 0;
            this.name = str;
            NamedNodeMap attributes = node.getAttributes();
            int length = attributes.getLength();
            if (length > 2) {
                throw new IIOInvalidTreeException(str + " node cannot have > 2 attributes", node);
            }
            if (length != 0) {
                int attributeValue = MarkerSegment.getAttributeValue(node, attributes, "thumbWidth", 0, 255, false);
                this.thumbWidth = attributeValue != -1 ? attributeValue : this.thumbWidth;
                int attributeValue2 = MarkerSegment.getAttributeValue(node, attributes, "thumbHeight", 0, 255, false);
                this.thumbHeight = attributeValue2 != -1 ? attributeValue2 : this.thumbHeight;
            }
        }

        JFIFThumbUncompressed(BufferedImage bufferedImage) {
            super();
            this.thumbnail = null;
            this.thumbnail = bufferedImage;
            this.thumbWidth = bufferedImage.getWidth();
            this.thumbHeight = bufferedImage.getHeight();
            this.name = null;
        }

        void readByteBuffer(ImageInputStream imageInputStream, byte[] bArr, JPEGImageReader jPEGImageReader, float f2, float f3) throws IOException {
            int iMax = Math.max((int) ((bArr.length / 20) / f2), 1);
            int i2 = 0;
            while (i2 < bArr.length) {
                imageInputStream.read(bArr, i2, Math.min(iMax, bArr.length - i2));
                i2 += iMax;
                float length = (((i2 * 100.0f) / bArr.length) * f2) + f3;
                if (length > 100.0f) {
                    length = 100.0f;
                }
                jPEGImageReader.thumbnailProgress(length);
            }
        }

        @Override // com.sun.imageio.plugins.jpeg.JFIFMarkerSegment.JFIFThumb
        int getWidth() {
            return this.thumbWidth;
        }

        @Override // com.sun.imageio.plugins.jpeg.JFIFMarkerSegment.JFIFThumb
        int getHeight() {
            return this.thumbHeight;
        }

        @Override // com.sun.imageio.plugins.jpeg.JFIFMarkerSegment.JFIFThumb
        IIOMetadataNode getNativeNode() {
            IIOMetadataNode iIOMetadataNode = new IIOMetadataNode(this.name);
            iIOMetadataNode.setAttribute("thumbWidth", Integer.toString(this.thumbWidth));
            iIOMetadataNode.setAttribute("thumbHeight", Integer.toString(this.thumbHeight));
            return iIOMetadataNode;
        }

        @Override // com.sun.imageio.plugins.jpeg.JFIFMarkerSegment.JFIFThumb
        void write(ImageOutputStream imageOutputStream, JPEGImageWriter jPEGImageWriter) throws IOException {
            if (this.thumbWidth > 255 || this.thumbHeight > 255) {
                jPEGImageWriter.warningOccurred(12);
            }
            this.thumbWidth = Math.min(this.thumbWidth, 255);
            this.thumbHeight = Math.min(this.thumbHeight, 255);
            imageOutputStream.write(this.thumbWidth);
            imageOutputStream.write(this.thumbHeight);
        }

        void writePixels(ImageOutputStream imageOutputStream, JPEGImageWriter jPEGImageWriter) throws IOException {
            if (this.thumbWidth > 255 || this.thumbHeight > 255) {
                jPEGImageWriter.warningOccurred(12);
            }
            this.thumbWidth = Math.min(this.thumbWidth, 255);
            this.thumbHeight = Math.min(this.thumbHeight, 255);
            JFIFMarkerSegment.this.writeThumbnailData(imageOutputStream, this.thumbnail.getRaster().getPixels(0, 0, this.thumbWidth, this.thumbHeight, (int[]) null), jPEGImageWriter);
        }

        @Override // com.sun.imageio.plugins.jpeg.JFIFMarkerSegment.JFIFThumb
        void print() {
            System.out.print(this.name + " width: ");
            System.out.println(this.thumbWidth);
            System.out.print(this.name + " height: ");
            System.out.println(this.thumbHeight);
        }
    }

    /* loaded from: rt.jar:com/sun/imageio/plugins/jpeg/JFIFMarkerSegment$JFIFThumbRGB.class */
    class JFIFThumbRGB extends JFIFThumbUncompressed {
        JFIFThumbRGB(JPEGBuffer jPEGBuffer, int i2, int i3) throws IOException {
            super(jPEGBuffer, i2, i3, i2 * i3 * 3, "JFIFthumbRGB");
        }

        JFIFThumbRGB(Node node) throws IIOInvalidTreeException {
            super(node, "JFIFthumbRGB");
        }

        JFIFThumbRGB(BufferedImage bufferedImage) throws IllegalThumbException {
            super(bufferedImage);
        }

        @Override // com.sun.imageio.plugins.jpeg.JFIFMarkerSegment.JFIFThumb
        int getLength() {
            return this.thumbWidth * this.thumbHeight * 3;
        }

        @Override // com.sun.imageio.plugins.jpeg.JFIFMarkerSegment.JFIFThumb
        BufferedImage getThumbnail(ImageInputStream imageInputStream, JPEGImageReader jPEGImageReader) throws IOException {
            imageInputStream.mark();
            imageInputStream.seek(this.streamPos);
            DataBufferByte dataBufferByte = new DataBufferByte(getLength());
            readByteBuffer(imageInputStream, dataBufferByte.getData(), jPEGImageReader, 1.0f, 0.0f);
            imageInputStream.reset();
            return new BufferedImage((ColorModel) new ComponentColorModel(JPEG.JCS.sRGB, false, false, 1, 0), Raster.createInterleavedRaster(dataBufferByte, this.thumbWidth, this.thumbHeight, this.thumbWidth * 3, 3, new int[]{0, 1, 2}, (Point) null), false, (Hashtable<?, ?>) null);
        }

        @Override // com.sun.imageio.plugins.jpeg.JFIFMarkerSegment.JFIFThumbUncompressed, com.sun.imageio.plugins.jpeg.JFIFMarkerSegment.JFIFThumb
        void write(ImageOutputStream imageOutputStream, JPEGImageWriter jPEGImageWriter) throws IOException {
            super.write(imageOutputStream, jPEGImageWriter);
            writePixels(imageOutputStream, jPEGImageWriter);
        }
    }

    /* loaded from: rt.jar:com/sun/imageio/plugins/jpeg/JFIFMarkerSegment$JFIFThumbPalette.class */
    class JFIFThumbPalette extends JFIFThumbUncompressed {
        private static final int PALETTE_SIZE = 768;

        JFIFThumbPalette(JPEGBuffer jPEGBuffer, int i2, int i3) throws IOException {
            super(jPEGBuffer, i2, i3, 768 + (i2 * i3), "JFIFThumbPalette");
        }

        JFIFThumbPalette(Node node) throws IIOInvalidTreeException {
            super(node, "JFIFThumbPalette");
        }

        JFIFThumbPalette(BufferedImage bufferedImage) throws IllegalThumbException {
            super(bufferedImage);
            if (((IndexColorModel) this.thumbnail.getColorModel()).getMapSize() > 256) {
                throw new IllegalThumbException();
            }
        }

        @Override // com.sun.imageio.plugins.jpeg.JFIFMarkerSegment.JFIFThumb
        int getLength() {
            return (this.thumbWidth * this.thumbHeight) + 768;
        }

        @Override // com.sun.imageio.plugins.jpeg.JFIFMarkerSegment.JFIFThumb
        BufferedImage getThumbnail(ImageInputStream imageInputStream, JPEGImageReader jPEGImageReader) throws IOException {
            imageInputStream.mark();
            imageInputStream.seek(this.streamPos);
            byte[] bArr = new byte[768];
            float length = 768.0f / getLength();
            readByteBuffer(imageInputStream, bArr, jPEGImageReader, length, 0.0f);
            DataBufferByte dataBufferByte = new DataBufferByte(this.thumbWidth * this.thumbHeight);
            readByteBuffer(imageInputStream, dataBufferByte.getData(), jPEGImageReader, 1.0f - length, length);
            imageInputStream.read();
            imageInputStream.reset();
            IndexColorModel indexColorModel = new IndexColorModel(8, 256, bArr, 0, false);
            return new BufferedImage((ColorModel) indexColorModel, Raster.createWritableRaster(indexColorModel.createCompatibleSampleModel(this.thumbWidth, this.thumbHeight), dataBufferByte, null), false, (Hashtable<?, ?>) null);
        }

        @Override // com.sun.imageio.plugins.jpeg.JFIFMarkerSegment.JFIFThumbUncompressed, com.sun.imageio.plugins.jpeg.JFIFMarkerSegment.JFIFThumb
        void write(ImageOutputStream imageOutputStream, JPEGImageWriter jPEGImageWriter) throws IOException {
            super.write(imageOutputStream, jPEGImageWriter);
            byte[] bArr = new byte[768];
            IndexColorModel indexColorModel = (IndexColorModel) this.thumbnail.getColorModel();
            byte[] bArr2 = new byte[256];
            byte[] bArr3 = new byte[256];
            byte[] bArr4 = new byte[256];
            indexColorModel.getReds(bArr2);
            indexColorModel.getGreens(bArr3);
            indexColorModel.getBlues(bArr4);
            for (int i2 = 0; i2 < 256; i2++) {
                bArr[i2 * 3] = bArr2[i2];
                bArr[(i2 * 3) + 1] = bArr3[i2];
                bArr[(i2 * 3) + 2] = bArr4[i2];
            }
            imageOutputStream.write(bArr);
            writePixels(imageOutputStream, jPEGImageWriter);
        }
    }

    /* loaded from: rt.jar:com/sun/imageio/plugins/jpeg/JFIFMarkerSegment$JFIFThumbJPEG.class */
    class JFIFThumbJPEG extends JFIFThumb {
        JPEGMetadata thumbMetadata;
        byte[] data;
        private static final int PREAMBLE_SIZE = 6;

        JFIFThumbJPEG(JPEGBuffer jPEGBuffer, int i2, JPEGImageReader jPEGImageReader) throws IOException {
            super(jPEGBuffer);
            this.thumbMetadata = null;
            this.data = null;
            long j2 = this.streamPos + (i2 - 6);
            jPEGBuffer.iis.seek(this.streamPos);
            this.thumbMetadata = new JPEGMetadata(false, true, jPEGBuffer.iis, jPEGImageReader);
            jPEGBuffer.iis.seek(j2);
            jPEGBuffer.bufAvail = 0;
            jPEGBuffer.bufPtr = 0;
        }

        JFIFThumbJPEG(Node node) throws IIOInvalidTreeException {
            super();
            this.thumbMetadata = null;
            this.data = null;
            if (node.getChildNodes().getLength() > 1) {
                throw new IIOInvalidTreeException("JFIFThumbJPEG node must have 0 or 1 child", node);
            }
            Node firstChild = node.getFirstChild();
            if (firstChild != null) {
                if (!firstChild.getNodeName().equals("markerSequence")) {
                    throw new IIOInvalidTreeException("JFIFThumbJPEG child must be a markerSequence node", node);
                }
                this.thumbMetadata = new JPEGMetadata(false, true);
                this.thumbMetadata.setFromMarkerSequenceNode(firstChild);
            }
        }

        JFIFThumbJPEG(BufferedImage bufferedImage) throws IllegalThumbException {
            super();
            this.thumbMetadata = null;
            this.data = null;
            try {
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(4096);
                MemoryCacheImageOutputStream memoryCacheImageOutputStream = new MemoryCacheImageOutputStream(byteArrayOutputStream);
                JPEGImageWriter jPEGImageWriter = new JPEGImageWriter(null);
                jPEGImageWriter.setOutput(memoryCacheImageOutputStream);
                JPEGMetadata jPEGMetadata = (JPEGMetadata) jPEGImageWriter.getDefaultImageMetadata(new ImageTypeSpecifier(bufferedImage), null);
                MarkerSegment markerSegmentFindMarkerSegment = jPEGMetadata.findMarkerSegment(JFIFMarkerSegment.class, true);
                if (markerSegmentFindMarkerSegment == null) {
                    throw new IllegalThumbException();
                }
                jPEGMetadata.markerSequence.remove(markerSegmentFindMarkerSegment);
                jPEGImageWriter.write(new IIOImage(bufferedImage, (List<? extends BufferedImage>) null, jPEGMetadata));
                jPEGImageWriter.dispose();
                if (byteArrayOutputStream.size() > 65527) {
                    throw new IllegalThumbException();
                }
                this.data = byteArrayOutputStream.toByteArray();
            } catch (IOException e2) {
                throw new IllegalThumbException();
            }
        }

        @Override // com.sun.imageio.plugins.jpeg.JFIFMarkerSegment.JFIFThumb
        int getWidth() {
            int i2 = 0;
            SOFMarkerSegment sOFMarkerSegment = (SOFMarkerSegment) this.thumbMetadata.findMarkerSegment(SOFMarkerSegment.class, true);
            if (sOFMarkerSegment != null) {
                i2 = sOFMarkerSegment.samplesPerLine;
            }
            return i2;
        }

        @Override // com.sun.imageio.plugins.jpeg.JFIFMarkerSegment.JFIFThumb
        int getHeight() {
            int i2 = 0;
            SOFMarkerSegment sOFMarkerSegment = (SOFMarkerSegment) this.thumbMetadata.findMarkerSegment(SOFMarkerSegment.class, true);
            if (sOFMarkerSegment != null) {
                i2 = sOFMarkerSegment.numLines;
            }
            return i2;
        }

        /* loaded from: rt.jar:com/sun/imageio/plugins/jpeg/JFIFMarkerSegment$JFIFThumbJPEG$ThumbnailReadListener.class */
        private class ThumbnailReadListener implements IIOReadProgressListener {
            JPEGImageReader reader;

            ThumbnailReadListener(JPEGImageReader jPEGImageReader) {
                this.reader = null;
                this.reader = jPEGImageReader;
            }

            @Override // javax.imageio.event.IIOReadProgressListener
            public void sequenceStarted(ImageReader imageReader, int i2) {
            }

            @Override // javax.imageio.event.IIOReadProgressListener
            public void sequenceComplete(ImageReader imageReader) {
            }

            @Override // javax.imageio.event.IIOReadProgressListener
            public void imageStarted(ImageReader imageReader, int i2) {
            }

            @Override // javax.imageio.event.IIOReadProgressListener
            public void imageProgress(ImageReader imageReader, float f2) {
                this.reader.thumbnailProgress(f2);
            }

            @Override // javax.imageio.event.IIOReadProgressListener
            public void imageComplete(ImageReader imageReader) {
            }

            @Override // javax.imageio.event.IIOReadProgressListener
            public void thumbnailStarted(ImageReader imageReader, int i2, int i3) {
            }

            @Override // javax.imageio.event.IIOReadProgressListener
            public void thumbnailProgress(ImageReader imageReader, float f2) {
            }

            @Override // javax.imageio.event.IIOReadProgressListener
            public void thumbnailComplete(ImageReader imageReader) {
            }

            @Override // javax.imageio.event.IIOReadProgressListener
            public void readAborted(ImageReader imageReader) {
            }
        }

        @Override // com.sun.imageio.plugins.jpeg.JFIFMarkerSegment.JFIFThumb
        BufferedImage getThumbnail(ImageInputStream imageInputStream, JPEGImageReader jPEGImageReader) throws IOException {
            imageInputStream.mark();
            imageInputStream.seek(this.streamPos);
            JPEGImageReader jPEGImageReader2 = new JPEGImageReader(null);
            jPEGImageReader2.setInput(imageInputStream);
            jPEGImageReader2.addIIOReadProgressListener(new ThumbnailReadListener(jPEGImageReader));
            BufferedImage bufferedImage = jPEGImageReader2.read(0, null);
            jPEGImageReader2.dispose();
            imageInputStream.reset();
            return bufferedImage;
        }

        @Override // com.sun.imageio.plugins.jpeg.JFIFMarkerSegment.JFIFThumb
        protected Object clone() {
            JFIFThumbJPEG jFIFThumbJPEG = (JFIFThumbJPEG) super.clone();
            if (this.thumbMetadata != null) {
                jFIFThumbJPEG.thumbMetadata = (JPEGMetadata) this.thumbMetadata.clone();
            }
            return jFIFThumbJPEG;
        }

        @Override // com.sun.imageio.plugins.jpeg.JFIFMarkerSegment.JFIFThumb
        IIOMetadataNode getNativeNode() throws DOMException {
            IIOMetadataNode iIOMetadataNode = new IIOMetadataNode("JFIFthumbJPEG");
            if (this.thumbMetadata != null) {
                iIOMetadataNode.appendChild(this.thumbMetadata.getNativeTree());
            }
            return iIOMetadataNode;
        }

        @Override // com.sun.imageio.plugins.jpeg.JFIFMarkerSegment.JFIFThumb
        int getLength() {
            if (this.data == null) {
                return 0;
            }
            return this.data.length;
        }

        @Override // com.sun.imageio.plugins.jpeg.JFIFMarkerSegment.JFIFThumb
        void write(ImageOutputStream imageOutputStream, JPEGImageWriter jPEGImageWriter) throws IOException {
            int length = this.data.length / 20;
            if (length == 0) {
                length = 1;
            }
            int i2 = 0;
            while (i2 < this.data.length) {
                imageOutputStream.write(this.data, i2, Math.min(length, this.data.length - i2));
                i2 += length;
                float length2 = (i2 * 100.0f) / this.data.length;
                if (length2 > 100.0f) {
                    length2 = 100.0f;
                }
                jPEGImageWriter.thumbnailProgress(length2);
            }
        }

        @Override // com.sun.imageio.plugins.jpeg.JFIFMarkerSegment.JFIFThumb
        void print() {
            System.out.println("JFIF thumbnail stored as JPEG");
        }
    }

    static void writeICC(ICC_Profile iCC_Profile, ImageOutputStream imageOutputStream) throws IOException {
        int length = "ICC_PROFILE".length() + 1;
        int i2 = ((65535 - 2) - length) - 2;
        byte[] data = iCC_Profile.getData();
        int length2 = data.length / i2;
        if (data.length % i2 != 0) {
            length2++;
        }
        int i3 = 1;
        int i4 = 0;
        for (int i5 = 0; i5 < length2; i5++) {
            int iMin = Math.min(data.length - i4, i2);
            imageOutputStream.write(255);
            imageOutputStream.write(226);
            MarkerSegment.write2bytes(imageOutputStream, iMin + 2 + length + 2);
            imageOutputStream.write("ICC_PROFILE".getBytes("US-ASCII"));
            imageOutputStream.write(0);
            int i6 = i3;
            i3++;
            imageOutputStream.write(i6);
            imageOutputStream.write(length2);
            imageOutputStream.write(data, i4, iMin);
            i4 += iMin;
        }
    }

    /* loaded from: rt.jar:com/sun/imageio/plugins/jpeg/JFIFMarkerSegment$ICCMarkerSegment.class */
    class ICCMarkerSegment extends MarkerSegment {
        ArrayList chunks;
        byte[] profile;
        private static final int ID_SIZE = 12;
        int chunksRead;
        int numChunks;

        ICCMarkerSegment(ICC_ColorSpace iCC_ColorSpace) {
            super(226);
            this.chunks = null;
            this.profile = null;
            this.chunks = null;
            this.chunksRead = 0;
            this.numChunks = 0;
            this.profile = iCC_ColorSpace.getProfile().getData();
        }

        ICCMarkerSegment(JPEGBuffer jPEGBuffer) throws IOException {
            super(jPEGBuffer);
            this.chunks = null;
            this.profile = null;
            jPEGBuffer.bufPtr += 12;
            jPEGBuffer.bufAvail -= 12;
            this.length -= 12;
            int i2 = jPEGBuffer.buf[jPEGBuffer.bufPtr] & 255;
            this.numChunks = jPEGBuffer.buf[jPEGBuffer.bufPtr + 1] & 255;
            if (i2 > this.numChunks) {
                throw new IIOException("Image format Error; chunk num > num chunks");
            }
            if (this.numChunks == 1) {
                this.length -= 2;
                this.profile = new byte[this.length];
                jPEGBuffer.bufPtr += 2;
                jPEGBuffer.bufAvail -= 2;
                jPEGBuffer.readData(this.profile);
                JFIFMarkerSegment.this.inICC = false;
                return;
            }
            byte[] bArr = new byte[this.length];
            this.length -= 2;
            jPEGBuffer.readData(bArr);
            this.chunks = new ArrayList();
            this.chunks.add(bArr);
            this.chunksRead = 1;
            JFIFMarkerSegment.this.inICC = true;
        }

        ICCMarkerSegment(Node node) throws IIOInvalidTreeException {
            ICC_Profile iCC_Profile;
            super(226);
            this.chunks = null;
            this.profile = null;
            if ((node instanceof IIOMetadataNode) && (iCC_Profile = (ICC_Profile) ((IIOMetadataNode) node).getUserObject()) != null) {
                this.profile = iCC_Profile.getData();
            }
        }

        @Override // com.sun.imageio.plugins.jpeg.MarkerSegment
        protected Object clone() {
            ICCMarkerSegment iCCMarkerSegment = (ICCMarkerSegment) super.clone();
            if (this.profile != null) {
                iCCMarkerSegment.profile = (byte[]) this.profile.clone();
            }
            return iCCMarkerSegment;
        }

        boolean addData(JPEGBuffer jPEGBuffer) throws IOException {
            jPEGBuffer.bufPtr++;
            jPEGBuffer.bufAvail--;
            byte[] bArr = jPEGBuffer.buf;
            int i2 = jPEGBuffer.bufPtr;
            jPEGBuffer.bufPtr = i2 + 1;
            int i3 = (bArr[i2] & 255) << 8;
            byte[] bArr2 = jPEGBuffer.buf;
            int i4 = jPEGBuffer.bufPtr;
            jPEGBuffer.bufPtr = i4 + 1;
            int i5 = i3 | (bArr2[i4] & 255);
            jPEGBuffer.bufAvail -= 2;
            jPEGBuffer.bufPtr += 12;
            jPEGBuffer.bufAvail -= 12;
            int i6 = (i5 - 2) - 12;
            if ((jPEGBuffer.buf[jPEGBuffer.bufPtr] & 255) > this.numChunks) {
                throw new IIOException("Image format Error; chunk num > num chunks");
            }
            if (this.numChunks != (jPEGBuffer.buf[jPEGBuffer.bufPtr + 1] & 255)) {
                throw new IIOException("Image format Error; icc num chunks mismatch");
            }
            int i7 = i6 - 2;
            boolean z2 = false;
            byte[] bArr3 = new byte[i7];
            jPEGBuffer.readData(bArr3);
            this.chunks.add(bArr3);
            this.length += i7;
            this.chunksRead++;
            if (this.chunksRead < this.numChunks) {
                JFIFMarkerSegment.this.inICC = true;
            } else {
                this.profile = new byte[this.length];
                int length = 0;
                for (int i8 = 1; i8 <= this.numChunks; i8++) {
                    boolean z3 = false;
                    for (int i9 = 0; i9 < this.chunks.size(); i9++) {
                        byte[] bArr4 = (byte[]) this.chunks.get(i9);
                        if (bArr4[0] == i8) {
                            System.arraycopy(bArr4, 2, this.profile, length, bArr4.length - 2);
                            length += bArr4.length - 2;
                            z3 = true;
                        }
                    }
                    if (!z3) {
                        throw new IIOException("Image Format Error: Missing ICC chunk num " + i8);
                    }
                }
                this.chunks = null;
                this.chunksRead = 0;
                this.numChunks = 0;
                JFIFMarkerSegment.this.inICC = false;
                z2 = true;
            }
            return z2;
        }

        @Override // com.sun.imageio.plugins.jpeg.MarkerSegment
        IIOMetadataNode getNativeNode() {
            IIOMetadataNode iIOMetadataNode = new IIOMetadataNode("app2ICC");
            if (this.profile != null) {
                iIOMetadataNode.setUserObject(ICC_Profile.getInstance(this.profile));
            }
            return iIOMetadataNode;
        }

        @Override // com.sun.imageio.plugins.jpeg.MarkerSegment
        void write(ImageOutputStream imageOutputStream) throws IOException {
        }

        @Override // com.sun.imageio.plugins.jpeg.MarkerSegment
        void print() {
            printTag("ICC Profile APP2");
        }
    }
}
