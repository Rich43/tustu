package com.sun.imageio.plugins.gif;

import java.io.UnsupportedEncodingException;
import java.util.Iterator;
import java.util.List;
import javax.imageio.metadata.IIOInvalidTreeException;
import javax.imageio.metadata.IIOMetadataFormatImpl;
import javax.imageio.metadata.IIOMetadataNode;
import org.apache.commons.net.ftp.FTP;
import org.icepdf.core.pobjects.graphics.Separation;
import org.w3c.dom.DOMException;
import org.w3c.dom.Node;

/* loaded from: rt.jar:com/sun/imageio/plugins/gif/GIFImageMetadata.class */
public class GIFImageMetadata extends GIFMetadata {
    static final String nativeMetadataFormatName = "javax_imageio_gif_image_1.0";
    static final String[] disposalMethodNames = {Separation.COLORANT_NONE, "doNotDispose", "restoreToBackgroundColor", "restoreToPrevious", "undefinedDisposalMethod4", "undefinedDisposalMethod5", "undefinedDisposalMethod6", "undefinedDisposalMethod7"};
    public int imageLeftPosition;
    public int imageTopPosition;
    public int imageWidth;
    public int imageHeight;
    public boolean interlaceFlag;
    public boolean sortFlag;
    public byte[] localColorTable;
    public int disposalMethod;
    public boolean userInputFlag;
    public boolean transparentColorFlag;
    public int delayTime;
    public int transparentColorIndex;
    public boolean hasPlainTextExtension;
    public int textGridLeft;
    public int textGridTop;
    public int textGridWidth;
    public int textGridHeight;
    public int characterCellWidth;
    public int characterCellHeight;
    public int textForegroundColor;
    public int textBackgroundColor;
    public byte[] text;
    public List applicationIDs;
    public List authenticationCodes;
    public List applicationData;
    public List comments;

    @Override // com.sun.imageio.plugins.gif.GIFMetadata, javax.imageio.metadata.IIOMetadata
    public /* bridge */ /* synthetic */ void mergeTree(String str, Node node) throws IIOInvalidTreeException {
        super.mergeTree(str, node);
    }

    protected GIFImageMetadata(boolean z2, String str, String str2, String[] strArr, String[] strArr2) {
        super(z2, str, str2, strArr, strArr2);
        this.interlaceFlag = false;
        this.sortFlag = false;
        this.localColorTable = null;
        this.disposalMethod = 0;
        this.userInputFlag = false;
        this.transparentColorFlag = false;
        this.delayTime = 0;
        this.transparentColorIndex = 0;
        this.hasPlainTextExtension = false;
        this.applicationIDs = null;
        this.authenticationCodes = null;
        this.applicationData = null;
        this.comments = null;
    }

    public GIFImageMetadata() {
        this(true, nativeMetadataFormatName, "com.sun.imageio.plugins.gif.GIFImageMetadataFormat", null, null);
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
        throw new IllegalArgumentException("Not a recognized format!");
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
        IIOMetadataNode iIOMetadataNode2 = new IIOMetadataNode("ImageDescriptor");
        iIOMetadataNode2.setAttribute("imageLeftPosition", Integer.toString(this.imageLeftPosition));
        iIOMetadataNode2.setAttribute("imageTopPosition", Integer.toString(this.imageTopPosition));
        iIOMetadataNode2.setAttribute("imageWidth", Integer.toString(this.imageWidth));
        iIOMetadataNode2.setAttribute("imageHeight", Integer.toString(this.imageHeight));
        iIOMetadataNode2.setAttribute("interlaceFlag", this.interlaceFlag ? "TRUE" : "FALSE");
        iIOMetadataNode.appendChild(iIOMetadataNode2);
        if (this.localColorTable != null) {
            IIOMetadataNode iIOMetadataNode3 = new IIOMetadataNode("LocalColorTable");
            int length = this.localColorTable.length / 3;
            iIOMetadataNode3.setAttribute("sizeOfLocalColorTable", Integer.toString(length));
            iIOMetadataNode3.setAttribute("sortFlag", this.sortFlag ? "TRUE" : "FALSE");
            for (int i2 = 0; i2 < length; i2++) {
                IIOMetadataNode iIOMetadataNode4 = new IIOMetadataNode("ColorTableEntry");
                iIOMetadataNode4.setAttribute("index", Integer.toString(i2));
                int i3 = this.localColorTable[3 * i2] & 255;
                int i4 = this.localColorTable[(3 * i2) + 1] & 255;
                int i5 = this.localColorTable[(3 * i2) + 2] & 255;
                iIOMetadataNode4.setAttribute("red", Integer.toString(i3));
                iIOMetadataNode4.setAttribute("green", Integer.toString(i4));
                iIOMetadataNode4.setAttribute("blue", Integer.toString(i5));
                iIOMetadataNode3.appendChild(iIOMetadataNode4);
            }
            iIOMetadataNode.appendChild(iIOMetadataNode3);
        }
        IIOMetadataNode iIOMetadataNode5 = new IIOMetadataNode("GraphicControlExtension");
        iIOMetadataNode5.setAttribute("disposalMethod", disposalMethodNames[this.disposalMethod]);
        iIOMetadataNode5.setAttribute("userInputFlag", this.userInputFlag ? "TRUE" : "FALSE");
        iIOMetadataNode5.setAttribute("transparentColorFlag", this.transparentColorFlag ? "TRUE" : "FALSE");
        iIOMetadataNode5.setAttribute("delayTime", Integer.toString(this.delayTime));
        iIOMetadataNode5.setAttribute("transparentColorIndex", Integer.toString(this.transparentColorIndex));
        iIOMetadataNode.appendChild(iIOMetadataNode5);
        if (this.hasPlainTextExtension) {
            IIOMetadataNode iIOMetadataNode6 = new IIOMetadataNode("PlainTextExtension");
            iIOMetadataNode6.setAttribute("textGridLeft", Integer.toString(this.textGridLeft));
            iIOMetadataNode6.setAttribute("textGridTop", Integer.toString(this.textGridTop));
            iIOMetadataNode6.setAttribute("textGridWidth", Integer.toString(this.textGridWidth));
            iIOMetadataNode6.setAttribute("textGridHeight", Integer.toString(this.textGridHeight));
            iIOMetadataNode6.setAttribute("characterCellWidth", Integer.toString(this.characterCellWidth));
            iIOMetadataNode6.setAttribute("characterCellHeight", Integer.toString(this.characterCellHeight));
            iIOMetadataNode6.setAttribute("textForegroundColor", Integer.toString(this.textForegroundColor));
            iIOMetadataNode6.setAttribute("textBackgroundColor", Integer.toString(this.textBackgroundColor));
            iIOMetadataNode6.setAttribute("text", toISO8859(this.text));
            iIOMetadataNode.appendChild(iIOMetadataNode6);
        }
        int size = this.applicationIDs == null ? 0 : this.applicationIDs.size();
        if (size > 0) {
            Node iIOMetadataNode7 = new IIOMetadataNode("ApplicationExtensions");
            for (int i6 = 0; i6 < size; i6++) {
                IIOMetadataNode iIOMetadataNode8 = new IIOMetadataNode("ApplicationExtension");
                iIOMetadataNode8.setAttribute("applicationID", toISO8859((byte[]) this.applicationIDs.get(i6)));
                iIOMetadataNode8.setAttribute("authenticationCode", toISO8859((byte[]) this.authenticationCodes.get(i6)));
                iIOMetadataNode8.setUserObject((byte[]) ((byte[]) this.applicationData.get(i6)).clone());
                iIOMetadataNode7.appendChild(iIOMetadataNode8);
            }
            iIOMetadataNode.appendChild(iIOMetadataNode7);
        }
        int size2 = this.comments == null ? 0 : this.comments.size();
        if (size2 > 0) {
            Node iIOMetadataNode9 = new IIOMetadataNode("CommentExtensions");
            for (int i7 = 0; i7 < size2; i7++) {
                IIOMetadataNode iIOMetadataNode10 = new IIOMetadataNode("CommentExtension");
                iIOMetadataNode10.setAttribute("value", toISO8859((byte[]) this.comments.get(i7)));
                iIOMetadataNode9.appendChild(iIOMetadataNode10);
            }
            iIOMetadataNode.appendChild(iIOMetadataNode9);
        }
        return iIOMetadataNode;
    }

    @Override // javax.imageio.metadata.IIOMetadata
    public IIOMetadataNode getStandardChromaNode() throws DOMException {
        IIOMetadataNode iIOMetadataNode = new IIOMetadataNode("Chroma");
        IIOMetadataNode iIOMetadataNode2 = new IIOMetadataNode("ColorSpaceType");
        iIOMetadataNode2.setAttribute("name", "RGB");
        iIOMetadataNode.appendChild(iIOMetadataNode2);
        IIOMetadataNode iIOMetadataNode3 = new IIOMetadataNode("NumChannels");
        iIOMetadataNode3.setAttribute("value", this.transparentColorFlag ? "4" : "3");
        iIOMetadataNode.appendChild(iIOMetadataNode3);
        IIOMetadataNode iIOMetadataNode4 = new IIOMetadataNode("BlackIsZero");
        iIOMetadataNode4.setAttribute("value", "TRUE");
        iIOMetadataNode.appendChild(iIOMetadataNode4);
        if (this.localColorTable != null) {
            Node iIOMetadataNode5 = new IIOMetadataNode("Palette");
            int length = this.localColorTable.length / 3;
            for (int i2 = 0; i2 < length; i2++) {
                IIOMetadataNode iIOMetadataNode6 = new IIOMetadataNode("PaletteEntry");
                iIOMetadataNode6.setAttribute("index", Integer.toString(i2));
                iIOMetadataNode6.setAttribute("red", Integer.toString(this.localColorTable[3 * i2] & 255));
                iIOMetadataNode6.setAttribute("green", Integer.toString(this.localColorTable[(3 * i2) + 1] & 255));
                iIOMetadataNode6.setAttribute("blue", Integer.toString(this.localColorTable[(3 * i2) + 2] & 255));
                iIOMetadataNode5.appendChild(iIOMetadataNode6);
            }
            iIOMetadataNode.appendChild(iIOMetadataNode5);
        }
        return iIOMetadataNode;
    }

    @Override // javax.imageio.metadata.IIOMetadata
    public IIOMetadataNode getStandardCompressionNode() throws DOMException {
        IIOMetadataNode iIOMetadataNode = new IIOMetadataNode("Compression");
        IIOMetadataNode iIOMetadataNode2 = new IIOMetadataNode("CompressionTypeName");
        iIOMetadataNode2.setAttribute("value", "lzw");
        iIOMetadataNode.appendChild(iIOMetadataNode2);
        IIOMetadataNode iIOMetadataNode3 = new IIOMetadataNode("Lossless");
        iIOMetadataNode3.setAttribute("value", "TRUE");
        iIOMetadataNode.appendChild(iIOMetadataNode3);
        IIOMetadataNode iIOMetadataNode4 = new IIOMetadataNode("NumProgressiveScans");
        iIOMetadataNode4.setAttribute("value", this.interlaceFlag ? "4" : "1");
        iIOMetadataNode.appendChild(iIOMetadataNode4);
        return iIOMetadataNode;
    }

    @Override // javax.imageio.metadata.IIOMetadata
    public IIOMetadataNode getStandardDataNode() throws DOMException {
        IIOMetadataNode iIOMetadataNode = new IIOMetadataNode("Data");
        IIOMetadataNode iIOMetadataNode2 = new IIOMetadataNode("SampleFormat");
        iIOMetadataNode2.setAttribute("value", "Index");
        iIOMetadataNode.appendChild(iIOMetadataNode2);
        return iIOMetadataNode;
    }

    @Override // javax.imageio.metadata.IIOMetadata
    public IIOMetadataNode getStandardDimensionNode() throws DOMException {
        IIOMetadataNode iIOMetadataNode = new IIOMetadataNode("Dimension");
        IIOMetadataNode iIOMetadataNode2 = new IIOMetadataNode("ImageOrientation");
        iIOMetadataNode2.setAttribute("value", "Normal");
        iIOMetadataNode.appendChild(iIOMetadataNode2);
        IIOMetadataNode iIOMetadataNode3 = new IIOMetadataNode("HorizontalPixelOffset");
        iIOMetadataNode3.setAttribute("value", Integer.toString(this.imageLeftPosition));
        iIOMetadataNode.appendChild(iIOMetadataNode3);
        IIOMetadataNode iIOMetadataNode4 = new IIOMetadataNode("VerticalPixelOffset");
        iIOMetadataNode4.setAttribute("value", Integer.toString(this.imageTopPosition));
        iIOMetadataNode.appendChild(iIOMetadataNode4);
        return iIOMetadataNode;
    }

    @Override // javax.imageio.metadata.IIOMetadata
    public IIOMetadataNode getStandardTextNode() throws DOMException {
        if (this.comments == null) {
            return null;
        }
        Iterator it = this.comments.iterator();
        if (!it.hasNext()) {
            return null;
        }
        IIOMetadataNode iIOMetadataNode = new IIOMetadataNode("Text");
        while (it.hasNext()) {
            try {
                String str = new String((byte[]) it.next(), FTP.DEFAULT_CONTROL_ENCODING);
                IIOMetadataNode iIOMetadataNode2 = new IIOMetadataNode("TextEntry");
                iIOMetadataNode2.setAttribute("value", str);
                iIOMetadataNode2.setAttribute("encoding", FTP.DEFAULT_CONTROL_ENCODING);
                iIOMetadataNode2.setAttribute("compression", Separation.COLORANT_NONE);
                iIOMetadataNode.appendChild(iIOMetadataNode2);
            } catch (UnsupportedEncodingException e2) {
                throw new RuntimeException("Encoding ISO-8859-1 unknown!");
            }
        }
        return iIOMetadataNode;
    }

    @Override // javax.imageio.metadata.IIOMetadata
    public IIOMetadataNode getStandardTransparencyNode() throws DOMException {
        if (!this.transparentColorFlag) {
            return null;
        }
        IIOMetadataNode iIOMetadataNode = new IIOMetadataNode("Transparency");
        IIOMetadataNode iIOMetadataNode2 = new IIOMetadataNode("TransparentIndex");
        iIOMetadataNode2.setAttribute("value", Integer.toString(this.transparentColorIndex));
        iIOMetadataNode.appendChild(iIOMetadataNode2);
        return iIOMetadataNode;
    }

    @Override // javax.imageio.metadata.IIOMetadata
    public void setFromTree(String str, Node node) throws IIOInvalidTreeException {
        throw new IllegalStateException("Metadata is read-only!");
    }

    @Override // com.sun.imageio.plugins.gif.GIFMetadata
    protected void mergeNativeTree(Node node) throws IIOInvalidTreeException {
        throw new IllegalStateException("Metadata is read-only!");
    }

    @Override // com.sun.imageio.plugins.gif.GIFMetadata
    protected void mergeStandardTree(Node node) throws IIOInvalidTreeException {
        throw new IllegalStateException("Metadata is read-only!");
    }

    @Override // javax.imageio.metadata.IIOMetadata
    public void reset() {
        throw new IllegalStateException("Metadata is read-only!");
    }
}
