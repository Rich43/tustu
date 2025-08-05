package com.sun.imageio.plugins.bmp;

import com.sun.imageio.plugins.common.I18N;
import com.sun.imageio.plugins.common.ImageUtil;
import com.sun.org.apache.xml.internal.security.utils.Constants;
import java.io.UnsupportedEncodingException;
import java.util.List;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.metadata.IIOMetadataFormatImpl;
import javax.imageio.metadata.IIOMetadataNode;
import org.apache.commons.net.ftp.FTP;
import org.icepdf.core.pobjects.graphics.SoftMask;
import org.w3c.dom.DOMException;
import org.w3c.dom.Node;

/* loaded from: rt.jar:com/sun/imageio/plugins/bmp/BMPMetadata.class */
public class BMPMetadata extends IIOMetadata implements BMPConstants {
    public static final String nativeMetadataFormatName = "javax_imageio_bmp_1.0";
    public String bmpVersion;
    public int width;
    public int height;
    public short bitsPerPixel;
    public int compression;
    public int imageSize;
    public int xPixelsPerMeter;
    public int yPixelsPerMeter;
    public int colorsUsed;
    public int colorsImportant;
    public int redMask;
    public int greenMask;
    public int blueMask;
    public int alphaMask;
    public int colorSpace;
    public double redX;
    public double redY;
    public double redZ;
    public double greenX;
    public double greenY;
    public double greenZ;
    public double blueX;
    public double blueY;
    public double blueZ;
    public int gammaRed;
    public int gammaGreen;
    public int gammaBlue;
    public int intent;
    public byte[] palette;
    public int paletteSize;
    public int red;
    public int green;
    public int blue;
    public List comments;

    public BMPMetadata() {
        super(true, nativeMetadataFormatName, "com.sun.imageio.plugins.bmp.BMPMetadataFormat", null, null);
        this.palette = null;
        this.comments = null;
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
        throw new IllegalArgumentException(I18N.getString("BMPMetadata0"));
    }

    private String toISO8859(byte[] bArr) {
        try {
            return new String(bArr, FTP.DEFAULT_CONTROL_ENCODING);
        } catch (UnsupportedEncodingException e2) {
            return "";
        }
    }

    private Node getNativeTree() throws DOMException {
        IIOMetadataNode iIOMetadataNode = new IIOMetadataNode(nativeMetadataFormatName);
        addChildNode(iIOMetadataNode, "BMPVersion", this.bmpVersion);
        addChildNode(iIOMetadataNode, "Width", new Integer(this.width));
        addChildNode(iIOMetadataNode, "Height", new Integer(this.height));
        addChildNode(iIOMetadataNode, "BitsPerPixel", new Short(this.bitsPerPixel));
        addChildNode(iIOMetadataNode, "Compression", new Integer(this.compression));
        addChildNode(iIOMetadataNode, "ImageSize", new Integer(this.imageSize));
        IIOMetadataNode iIOMetadataNodeAddChildNode = addChildNode(iIOMetadataNode, "PixelsPerMeter", null);
        addChildNode(iIOMetadataNodeAddChildNode, "X", new Integer(this.xPixelsPerMeter));
        addChildNode(iIOMetadataNodeAddChildNode, Constants._TAG_Y, new Integer(this.yPixelsPerMeter));
        addChildNode(iIOMetadataNode, "ColorsUsed", new Integer(this.colorsUsed));
        addChildNode(iIOMetadataNode, "ColorsImportant", new Integer(this.colorsImportant));
        int iCharAt = 0;
        for (int i2 = 0; i2 < this.bmpVersion.length(); i2++) {
            if (Character.isDigit(this.bmpVersion.charAt(i2))) {
                iCharAt = this.bmpVersion.charAt(i2) - '0';
            }
        }
        if (iCharAt >= 4) {
            IIOMetadataNode iIOMetadataNodeAddChildNode2 = addChildNode(iIOMetadataNode, "Mask", null);
            addChildNode(iIOMetadataNodeAddChildNode2, "Red", new Integer(this.redMask));
            addChildNode(iIOMetadataNodeAddChildNode2, "Green", new Integer(this.greenMask));
            addChildNode(iIOMetadataNodeAddChildNode2, "Blue", new Integer(this.blueMask));
            addChildNode(iIOMetadataNodeAddChildNode2, SoftMask.SOFT_MASK_TYPE_ALPHA, new Integer(this.alphaMask));
            addChildNode(iIOMetadataNode, "ColorSpaceType", new Integer(this.colorSpace));
            IIOMetadataNode iIOMetadataNodeAddChildNode3 = addChildNode(iIOMetadataNode, "CIEXYZEndPoints", null);
            addXYZPoints(iIOMetadataNodeAddChildNode3, "Red", this.redX, this.redY, this.redZ);
            addXYZPoints(iIOMetadataNodeAddChildNode3, "Green", this.greenX, this.greenY, this.greenZ);
            addXYZPoints(iIOMetadataNodeAddChildNode3, "Blue", this.blueX, this.blueY, this.blueZ);
            addChildNode(iIOMetadataNode, "Intent", new Integer(this.intent));
        }
        if (this.palette != null && this.paletteSize > 0) {
            IIOMetadataNode iIOMetadataNodeAddChildNode4 = addChildNode(iIOMetadataNode, "Palette", null);
            int length = this.palette.length / this.paletteSize;
            int i3 = 0;
            for (int i4 = 0; i4 < this.paletteSize; i4++) {
                IIOMetadataNode iIOMetadataNodeAddChildNode5 = addChildNode(iIOMetadataNodeAddChildNode4, "PaletteEntry", null);
                int i5 = i3;
                int i6 = i3 + 1;
                this.red = this.palette[i5] & 255;
                int i7 = i6 + 1;
                this.green = this.palette[i6] & 255;
                i3 = i7 + 1;
                this.blue = this.palette[i7] & 255;
                addChildNode(iIOMetadataNodeAddChildNode5, "Red", new Byte((byte) this.red));
                addChildNode(iIOMetadataNodeAddChildNode5, "Green", new Byte((byte) this.green));
                addChildNode(iIOMetadataNodeAddChildNode5, "Blue", new Byte((byte) this.blue));
                if (length == 4) {
                    i3++;
                    addChildNode(iIOMetadataNodeAddChildNode5, SoftMask.SOFT_MASK_TYPE_ALPHA, new Byte((byte) (this.palette[i3] & 255)));
                }
            }
        }
        return iIOMetadataNode;
    }

    @Override // javax.imageio.metadata.IIOMetadata
    protected IIOMetadataNode getStandardChromaNode() throws DOMException {
        if (this.palette != null && this.paletteSize > 0) {
            IIOMetadataNode iIOMetadataNode = new IIOMetadataNode("Chroma");
            IIOMetadataNode iIOMetadataNode2 = new IIOMetadataNode("Palette");
            int length = this.palette.length / this.paletteSize;
            iIOMetadataNode2.setAttribute("value", "" + length);
            int i2 = 0;
            for (int i3 = 0; i3 < this.paletteSize; i3++) {
                IIOMetadataNode iIOMetadataNode3 = new IIOMetadataNode("PaletteEntry");
                iIOMetadataNode3.setAttribute("index", "" + i3);
                int i4 = i2;
                int i5 = i2 + 1;
                iIOMetadataNode3.setAttribute("red", "" + ((int) this.palette[i4]));
                int i6 = i5 + 1;
                iIOMetadataNode3.setAttribute("green", "" + ((int) this.palette[i5]));
                i2 = i6 + 1;
                iIOMetadataNode3.setAttribute("blue", "" + ((int) this.palette[i6]));
                if (length == 4 && this.palette[i2] != 0) {
                    i2++;
                    iIOMetadataNode3.setAttribute("alpha", "" + ((int) this.palette[i2]));
                }
                iIOMetadataNode2.appendChild(iIOMetadataNode3);
            }
            iIOMetadataNode.appendChild(iIOMetadataNode2);
            return iIOMetadataNode;
        }
        return null;
    }

    @Override // javax.imageio.metadata.IIOMetadata
    protected IIOMetadataNode getStandardCompressionNode() throws DOMException {
        IIOMetadataNode iIOMetadataNode = new IIOMetadataNode("Compression");
        IIOMetadataNode iIOMetadataNode2 = new IIOMetadataNode("CompressionTypeName");
        iIOMetadataNode2.setAttribute("value", BMPCompressionTypes.getName(this.compression));
        iIOMetadataNode.appendChild(iIOMetadataNode2);
        return iIOMetadataNode;
    }

    @Override // javax.imageio.metadata.IIOMetadata
    protected IIOMetadataNode getStandardDataNode() throws DOMException {
        IIOMetadataNode iIOMetadataNode = new IIOMetadataNode("Data");
        String str = "";
        if (this.bitsPerPixel == 24) {
            str = "8 8 8 ";
        } else if (this.bitsPerPixel == 16 || this.bitsPerPixel == 32) {
            str = "" + countBits(this.redMask) + " " + countBits(this.greenMask) + countBits(this.blueMask) + "" + countBits(this.alphaMask);
        }
        IIOMetadataNode iIOMetadataNode2 = new IIOMetadataNode("BitsPerSample");
        iIOMetadataNode2.setAttribute("value", str);
        iIOMetadataNode.appendChild(iIOMetadataNode2);
        return iIOMetadataNode;
    }

    @Override // javax.imageio.metadata.IIOMetadata
    protected IIOMetadataNode getStandardDimensionNode() throws DOMException {
        if (this.yPixelsPerMeter > 0.0f && this.xPixelsPerMeter > 0.0f) {
            IIOMetadataNode iIOMetadataNode = new IIOMetadataNode("Dimension");
            float f2 = this.yPixelsPerMeter / this.xPixelsPerMeter;
            IIOMetadataNode iIOMetadataNode2 = new IIOMetadataNode("PixelAspectRatio");
            iIOMetadataNode2.setAttribute("value", "" + f2);
            iIOMetadataNode.appendChild(iIOMetadataNode2);
            IIOMetadataNode iIOMetadataNode3 = new IIOMetadataNode("HorizontalPhysicalPixelSpacing");
            iIOMetadataNode3.setAttribute("value", "" + ((1 / this.xPixelsPerMeter) * 1000));
            iIOMetadataNode.appendChild(iIOMetadataNode3);
            IIOMetadataNode iIOMetadataNode4 = new IIOMetadataNode("VerticalPhysicalPixelSpacing");
            iIOMetadataNode4.setAttribute("value", "" + ((1 / this.yPixelsPerMeter) * 1000));
            iIOMetadataNode.appendChild(iIOMetadataNode4);
            return iIOMetadataNode;
        }
        return null;
    }

    @Override // javax.imageio.metadata.IIOMetadata
    public void setFromTree(String str, Node node) {
        throw new IllegalStateException(I18N.getString("BMPMetadata1"));
    }

    @Override // javax.imageio.metadata.IIOMetadata
    public void mergeTree(String str, Node node) {
        throw new IllegalStateException(I18N.getString("BMPMetadata1"));
    }

    @Override // javax.imageio.metadata.IIOMetadata
    public void reset() {
        throw new IllegalStateException(I18N.getString("BMPMetadata1"));
    }

    private String countBits(int i2) {
        int i3 = 0;
        while (i2 > 0) {
            if ((i2 & 1) == 1) {
                i3++;
            }
            i2 >>>= 1;
        }
        return i3 == 0 ? "" : "" + i3;
    }

    private void addXYZPoints(IIOMetadataNode iIOMetadataNode, String str, double d2, double d3, double d4) throws DOMException {
        IIOMetadataNode iIOMetadataNodeAddChildNode = addChildNode(iIOMetadataNode, str, null);
        addChildNode(iIOMetadataNodeAddChildNode, "X", new Double(d2));
        addChildNode(iIOMetadataNodeAddChildNode, Constants._TAG_Y, new Double(d3));
        addChildNode(iIOMetadataNodeAddChildNode, com.sun.org.apache.xalan.internal.xsltc.compiler.Constants.HASIDCALL_INDEX_SIG, new Double(d4));
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
}
