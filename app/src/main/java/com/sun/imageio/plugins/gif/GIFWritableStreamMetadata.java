package com.sun.imageio.plugins.gif;

import javax.imageio.metadata.IIOInvalidTreeException;
import javax.imageio.metadata.IIOMetadataFormatImpl;
import org.w3c.dom.Node;

/* loaded from: rt.jar:com/sun/imageio/plugins/gif/GIFWritableStreamMetadata.class */
class GIFWritableStreamMetadata extends GIFStreamMetadata {
    static final String NATIVE_FORMAT_NAME = "javax_imageio_gif_stream_1.0";

    public GIFWritableStreamMetadata() {
        super(true, NATIVE_FORMAT_NAME, "com.sun.imageio.plugins.gif.GIFStreamMetadataFormat", null, null);
        reset();
    }

    @Override // com.sun.imageio.plugins.gif.GIFStreamMetadata, javax.imageio.metadata.IIOMetadata
    public boolean isReadOnly() {
        return false;
    }

    @Override // com.sun.imageio.plugins.gif.GIFStreamMetadata, com.sun.imageio.plugins.gif.GIFMetadata, javax.imageio.metadata.IIOMetadata
    public void mergeTree(String str, Node node) throws IIOInvalidTreeException {
        if (str.equals(NATIVE_FORMAT_NAME)) {
            if (node == null) {
                throw new IllegalArgumentException("root == null!");
            }
            mergeNativeTree(node);
        } else {
            if (str.equals(IIOMetadataFormatImpl.standardMetadataFormatName)) {
                if (node == null) {
                    throw new IllegalArgumentException("root == null!");
                }
                mergeStandardTree(node);
                return;
            }
            throw new IllegalArgumentException("Not a recognized format!");
        }
    }

    @Override // com.sun.imageio.plugins.gif.GIFStreamMetadata, javax.imageio.metadata.IIOMetadata
    public void reset() {
        this.version = null;
        this.logicalScreenWidth = -1;
        this.logicalScreenHeight = -1;
        this.colorResolution = -1;
        this.pixelAspectRatio = 0;
        this.backgroundColorIndex = 0;
        this.sortFlag = false;
        this.globalColorTable = null;
    }

    @Override // com.sun.imageio.plugins.gif.GIFStreamMetadata, com.sun.imageio.plugins.gif.GIFMetadata
    protected void mergeNativeTree(Node node) throws IIOInvalidTreeException {
        if (!node.getNodeName().equals(NATIVE_FORMAT_NAME)) {
            fatal(node, "Root must be javax_imageio_gif_stream_1.0");
        }
        Node firstChild = node.getFirstChild();
        while (true) {
            Node node2 = firstChild;
            if (node2 != null) {
                String nodeName = node2.getNodeName();
                if (nodeName.equals("Version")) {
                    this.version = getStringAttribute(node2, "value", null, true, versionStrings);
                } else if (nodeName.equals("LogicalScreenDescriptor")) {
                    this.logicalScreenWidth = getIntAttribute(node2, "logicalScreenWidth", -1, true, true, 1, 65535);
                    this.logicalScreenHeight = getIntAttribute(node2, "logicalScreenHeight", -1, true, true, 1, 65535);
                    this.colorResolution = getIntAttribute(node2, "colorResolution", -1, true, true, 1, 8);
                    this.pixelAspectRatio = getIntAttribute(node2, "pixelAspectRatio", 0, true, true, 0, 255);
                } else if (nodeName.equals("GlobalColorTable")) {
                    int intAttribute = getIntAttribute(node2, "sizeOfGlobalColorTable", true, 2, 256);
                    if (intAttribute != 2 && intAttribute != 4 && intAttribute != 8 && intAttribute != 16 && intAttribute != 32 && intAttribute != 64 && intAttribute != 128 && intAttribute != 256) {
                        fatal(node2, "Bad value for GlobalColorTable attribute sizeOfGlobalColorTable!");
                    }
                    this.backgroundColorIndex = getIntAttribute(node2, "backgroundColorIndex", 0, true, true, 0, 255);
                    this.sortFlag = getBooleanAttribute(node2, "sortFlag", false, true);
                    this.globalColorTable = getColorTable(node2, "ColorTableEntry", true, intAttribute);
                } else {
                    fatal(node2, "Unknown child of root node!");
                }
                firstChild = node2.getNextSibling();
            } else {
                return;
            }
        }
    }

    @Override // com.sun.imageio.plugins.gif.GIFStreamMetadata, com.sun.imageio.plugins.gif.GIFMetadata
    protected void mergeStandardTree(Node node) throws IIOInvalidTreeException {
        if (!node.getNodeName().equals(IIOMetadataFormatImpl.standardMetadataFormatName)) {
            fatal(node, "Root must be javax_imageio_1.0");
        }
        Node firstChild = node.getFirstChild();
        while (true) {
            Node node2 = firstChild;
            if (node2 != null) {
                String nodeName = node2.getNodeName();
                if (nodeName.equals("Chroma")) {
                    Node firstChild2 = node2.getFirstChild();
                    while (true) {
                        Node node3 = firstChild2;
                        if (node3 != null) {
                            String nodeName2 = node3.getNodeName();
                            if (nodeName2.equals("Palette")) {
                                this.globalColorTable = getColorTable(node3, "PaletteEntry", false, -1);
                            } else if (nodeName2.equals("BackgroundIndex")) {
                                this.backgroundColorIndex = getIntAttribute(node3, "value", -1, true, true, 0, 255);
                            }
                            firstChild2 = node3.getNextSibling();
                        }
                    }
                } else if (nodeName.equals("Data")) {
                    Node firstChild3 = node2.getFirstChild();
                    while (true) {
                        Node node4 = firstChild3;
                        if (node4 == null) {
                            break;
                        }
                        if (node4.getNodeName().equals("BitsPerSample")) {
                            this.colorResolution = getIntAttribute(node4, "value", -1, true, true, 1, 8);
                            break;
                        }
                        firstChild3 = node4.getNextSibling();
                    }
                } else if (nodeName.equals("Dimension")) {
                    Node firstChild4 = node2.getFirstChild();
                    while (true) {
                        Node node5 = firstChild4;
                        if (node5 != null) {
                            String nodeName3 = node5.getNodeName();
                            if (nodeName3.equals("PixelAspectRatio")) {
                                float floatAttribute = getFloatAttribute(node5, "value");
                                if (floatAttribute == 1.0f) {
                                    this.pixelAspectRatio = 0;
                                } else {
                                    this.pixelAspectRatio = Math.max(Math.min((int) ((floatAttribute * 64.0f) - 15.0f), 255), 0);
                                }
                            } else if (nodeName3.equals("HorizontalScreenSize")) {
                                this.logicalScreenWidth = getIntAttribute(node5, "value", -1, true, true, 1, 65535);
                            } else if (nodeName3.equals("VerticalScreenSize")) {
                                this.logicalScreenHeight = getIntAttribute(node5, "value", -1, true, true, 1, 65535);
                            }
                            firstChild4 = node5.getNextSibling();
                        }
                    }
                } else if (nodeName.equals("Document")) {
                    Node firstChild5 = node2.getFirstChild();
                    while (true) {
                        Node node6 = firstChild5;
                        if (node6 == null) {
                            break;
                        }
                        if (node6.getNodeName().equals("FormatVersion")) {
                            String stringAttribute = getStringAttribute(node6, "value", null, true, null);
                            int i2 = 0;
                            while (true) {
                                if (i2 >= versionStrings.length) {
                                    break;
                                }
                                if (!stringAttribute.equals(versionStrings[i2])) {
                                    i2++;
                                } else {
                                    this.version = stringAttribute;
                                    break;
                                }
                            }
                        } else {
                            firstChild5 = node6.getNextSibling();
                        }
                    }
                }
                firstChild = node2.getNextSibling();
            } else {
                return;
            }
        }
    }

    @Override // com.sun.imageio.plugins.gif.GIFStreamMetadata, javax.imageio.metadata.IIOMetadata
    public void setFromTree(String str, Node node) throws IIOInvalidTreeException {
        reset();
        mergeTree(str, node);
    }
}
