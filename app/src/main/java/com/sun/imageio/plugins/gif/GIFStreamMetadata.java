package com.sun.imageio.plugins.gif;

import javax.imageio.metadata.IIOInvalidTreeException;
import javax.imageio.metadata.IIOMetadataFormatImpl;
import javax.imageio.metadata.IIOMetadataNode;
import org.w3c.dom.DOMException;
import org.w3c.dom.Node;

/* loaded from: rt.jar:com/sun/imageio/plugins/gif/GIFStreamMetadata.class */
public class GIFStreamMetadata extends GIFMetadata {
    static final String nativeMetadataFormatName = "javax_imageio_gif_stream_1.0";
    public String version;
    public int logicalScreenWidth;
    public int logicalScreenHeight;
    public int colorResolution;
    public int pixelAspectRatio;
    public int backgroundColorIndex;
    public boolean sortFlag;
    public byte[] globalColorTable;
    static final String[] versionStrings = {"87a", "89a"};
    static final String[] colorTableSizes = {"2", "4", "8", "16", "32", "64", "128", "256"};

    @Override // com.sun.imageio.plugins.gif.GIFMetadata, javax.imageio.metadata.IIOMetadata
    public /* bridge */ /* synthetic */ void mergeTree(String str, Node node) throws IIOInvalidTreeException {
        super.mergeTree(str, node);
    }

    protected GIFStreamMetadata(boolean z2, String str, String str2, String[] strArr, String[] strArr2) {
        super(z2, str, str2, strArr, strArr2);
        this.globalColorTable = null;
    }

    public GIFStreamMetadata() {
        this(true, nativeMetadataFormatName, "com.sun.imageio.plugins.gif.GIFStreamMetadataFormat", null, null);
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

    private Node getNativeTree() throws DOMException {
        IIOMetadataNode iIOMetadataNode = new IIOMetadataNode(nativeMetadataFormatName);
        IIOMetadataNode iIOMetadataNode2 = new IIOMetadataNode("Version");
        iIOMetadataNode2.setAttribute("value", this.version);
        iIOMetadataNode.appendChild(iIOMetadataNode2);
        IIOMetadataNode iIOMetadataNode3 = new IIOMetadataNode("LogicalScreenDescriptor");
        iIOMetadataNode3.setAttribute("logicalScreenWidth", this.logicalScreenWidth == -1 ? "" : Integer.toString(this.logicalScreenWidth));
        iIOMetadataNode3.setAttribute("logicalScreenHeight", this.logicalScreenHeight == -1 ? "" : Integer.toString(this.logicalScreenHeight));
        iIOMetadataNode3.setAttribute("colorResolution", this.colorResolution == -1 ? "" : Integer.toString(this.colorResolution));
        iIOMetadataNode3.setAttribute("pixelAspectRatio", Integer.toString(this.pixelAspectRatio));
        iIOMetadataNode.appendChild(iIOMetadataNode3);
        if (this.globalColorTable != null) {
            IIOMetadataNode iIOMetadataNode4 = new IIOMetadataNode("GlobalColorTable");
            int length = this.globalColorTable.length / 3;
            iIOMetadataNode4.setAttribute("sizeOfGlobalColorTable", Integer.toString(length));
            iIOMetadataNode4.setAttribute("backgroundColorIndex", Integer.toString(this.backgroundColorIndex));
            iIOMetadataNode4.setAttribute("sortFlag", this.sortFlag ? "TRUE" : "FALSE");
            for (int i2 = 0; i2 < length; i2++) {
                IIOMetadataNode iIOMetadataNode5 = new IIOMetadataNode("ColorTableEntry");
                iIOMetadataNode5.setAttribute("index", Integer.toString(i2));
                int i3 = this.globalColorTable[3 * i2] & 255;
                int i4 = this.globalColorTable[(3 * i2) + 1] & 255;
                int i5 = this.globalColorTable[(3 * i2) + 2] & 255;
                iIOMetadataNode5.setAttribute("red", Integer.toString(i3));
                iIOMetadataNode5.setAttribute("green", Integer.toString(i4));
                iIOMetadataNode5.setAttribute("blue", Integer.toString(i5));
                iIOMetadataNode4.appendChild(iIOMetadataNode5);
            }
            iIOMetadataNode.appendChild(iIOMetadataNode4);
        }
        return iIOMetadataNode;
    }

    @Override // javax.imageio.metadata.IIOMetadata
    public IIOMetadataNode getStandardChromaNode() throws DOMException {
        IIOMetadataNode iIOMetadataNode = new IIOMetadataNode("Chroma");
        IIOMetadataNode iIOMetadataNode2 = new IIOMetadataNode("ColorSpaceType");
        iIOMetadataNode2.setAttribute("name", "RGB");
        iIOMetadataNode.appendChild(iIOMetadataNode2);
        IIOMetadataNode iIOMetadataNode3 = new IIOMetadataNode("BlackIsZero");
        iIOMetadataNode3.setAttribute("value", "TRUE");
        iIOMetadataNode.appendChild(iIOMetadataNode3);
        if (this.globalColorTable != null) {
            Node iIOMetadataNode4 = new IIOMetadataNode("Palette");
            int length = this.globalColorTable.length / 3;
            for (int i2 = 0; i2 < length; i2++) {
                IIOMetadataNode iIOMetadataNode5 = new IIOMetadataNode("PaletteEntry");
                iIOMetadataNode5.setAttribute("index", Integer.toString(i2));
                iIOMetadataNode5.setAttribute("red", Integer.toString(this.globalColorTable[3 * i2] & 255));
                iIOMetadataNode5.setAttribute("green", Integer.toString(this.globalColorTable[(3 * i2) + 1] & 255));
                iIOMetadataNode5.setAttribute("blue", Integer.toString(this.globalColorTable[(3 * i2) + 2] & 255));
                iIOMetadataNode4.appendChild(iIOMetadataNode5);
            }
            iIOMetadataNode.appendChild(iIOMetadataNode4);
            IIOMetadataNode iIOMetadataNode6 = new IIOMetadataNode("BackgroundIndex");
            iIOMetadataNode6.setAttribute("value", Integer.toString(this.backgroundColorIndex));
            iIOMetadataNode.appendChild(iIOMetadataNode6);
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
        return iIOMetadataNode;
    }

    @Override // javax.imageio.metadata.IIOMetadata
    public IIOMetadataNode getStandardDataNode() throws DOMException {
        IIOMetadataNode iIOMetadataNode = new IIOMetadataNode("Data");
        IIOMetadataNode iIOMetadataNode2 = new IIOMetadataNode("SampleFormat");
        iIOMetadataNode2.setAttribute("value", "Index");
        iIOMetadataNode.appendChild(iIOMetadataNode2);
        IIOMetadataNode iIOMetadataNode3 = new IIOMetadataNode("BitsPerSample");
        iIOMetadataNode3.setAttribute("value", this.colorResolution == -1 ? "" : Integer.toString(this.colorResolution));
        iIOMetadataNode.appendChild(iIOMetadataNode3);
        return iIOMetadataNode;
    }

    @Override // javax.imageio.metadata.IIOMetadata
    public IIOMetadataNode getStandardDimensionNode() throws DOMException {
        IIOMetadataNode iIOMetadataNode = new IIOMetadataNode("Dimension");
        IIOMetadataNode iIOMetadataNode2 = new IIOMetadataNode("PixelAspectRatio");
        float f2 = 1.0f;
        if (this.pixelAspectRatio != 0) {
            f2 = (this.pixelAspectRatio + 15) / 64.0f;
        }
        iIOMetadataNode2.setAttribute("value", Float.toString(f2));
        iIOMetadataNode.appendChild(iIOMetadataNode2);
        IIOMetadataNode iIOMetadataNode3 = new IIOMetadataNode("ImageOrientation");
        iIOMetadataNode3.setAttribute("value", "Normal");
        iIOMetadataNode.appendChild(iIOMetadataNode3);
        IIOMetadataNode iIOMetadataNode4 = new IIOMetadataNode("HorizontalScreenSize");
        iIOMetadataNode4.setAttribute("value", this.logicalScreenWidth == -1 ? "" : Integer.toString(this.logicalScreenWidth));
        iIOMetadataNode.appendChild(iIOMetadataNode4);
        IIOMetadataNode iIOMetadataNode5 = new IIOMetadataNode("VerticalScreenSize");
        iIOMetadataNode5.setAttribute("value", this.logicalScreenHeight == -1 ? "" : Integer.toString(this.logicalScreenHeight));
        iIOMetadataNode.appendChild(iIOMetadataNode5);
        return iIOMetadataNode;
    }

    @Override // javax.imageio.metadata.IIOMetadata
    public IIOMetadataNode getStandardDocumentNode() throws DOMException {
        IIOMetadataNode iIOMetadataNode = new IIOMetadataNode("Document");
        IIOMetadataNode iIOMetadataNode2 = new IIOMetadataNode("FormatVersion");
        iIOMetadataNode2.setAttribute("value", this.version);
        iIOMetadataNode.appendChild(iIOMetadataNode2);
        return iIOMetadataNode;
    }

    @Override // javax.imageio.metadata.IIOMetadata
    public IIOMetadataNode getStandardTextNode() {
        return null;
    }

    @Override // javax.imageio.metadata.IIOMetadata
    public IIOMetadataNode getStandardTransparencyNode() {
        return null;
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
