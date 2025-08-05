package com.sun.imageio.plugins.gif;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import javax.imageio.metadata.IIOInvalidTreeException;
import javax.imageio.metadata.IIOMetadataFormatImpl;
import javax.imageio.metadata.IIOMetadataNode;
import org.apache.commons.net.ftp.FTP;
import org.icepdf.core.pobjects.graphics.Separation;
import org.w3c.dom.Node;

/* loaded from: rt.jar:com/sun/imageio/plugins/gif/GIFWritableImageMetadata.class */
class GIFWritableImageMetadata extends GIFImageMetadata {
    static final String NATIVE_FORMAT_NAME = "javax_imageio_gif_image_1.0";

    GIFWritableImageMetadata() {
        super(true, NATIVE_FORMAT_NAME, "com.sun.imageio.plugins.gif.GIFImageMetadataFormat", null, null);
    }

    @Override // com.sun.imageio.plugins.gif.GIFImageMetadata, javax.imageio.metadata.IIOMetadata
    public boolean isReadOnly() {
        return false;
    }

    @Override // com.sun.imageio.plugins.gif.GIFImageMetadata, javax.imageio.metadata.IIOMetadata
    public void reset() {
        this.imageLeftPosition = 0;
        this.imageTopPosition = 0;
        this.imageWidth = 0;
        this.imageHeight = 0;
        this.interlaceFlag = false;
        this.sortFlag = false;
        this.localColorTable = null;
        this.disposalMethod = 0;
        this.userInputFlag = false;
        this.transparentColorFlag = false;
        this.delayTime = 0;
        this.transparentColorIndex = 0;
        this.hasPlainTextExtension = false;
        this.textGridLeft = 0;
        this.textGridTop = 0;
        this.textGridWidth = 0;
        this.textGridHeight = 0;
        this.characterCellWidth = 0;
        this.characterCellHeight = 0;
        this.textForegroundColor = 0;
        this.textBackgroundColor = 0;
        this.text = null;
        this.applicationIDs = null;
        this.authenticationCodes = null;
        this.applicationData = null;
        this.comments = null;
    }

    private byte[] fromISO8859(String str) {
        try {
            return str.getBytes(FTP.DEFAULT_CONTROL_ENCODING);
        } catch (UnsupportedEncodingException e2) {
            return "".getBytes();
        }
    }

    @Override // com.sun.imageio.plugins.gif.GIFImageMetadata, com.sun.imageio.plugins.gif.GIFMetadata
    protected void mergeNativeTree(Node node) throws IIOInvalidTreeException {
        if (!node.getNodeName().equals(NATIVE_FORMAT_NAME)) {
            fatal(node, "Root must be javax_imageio_gif_image_1.0");
        }
        Node firstChild = node.getFirstChild();
        while (true) {
            Node node2 = firstChild;
            if (node2 != null) {
                String nodeName = node2.getNodeName();
                if (nodeName.equals("ImageDescriptor")) {
                    this.imageLeftPosition = getIntAttribute(node2, "imageLeftPosition", -1, true, true, 0, 65535);
                    this.imageTopPosition = getIntAttribute(node2, "imageTopPosition", -1, true, true, 0, 65535);
                    this.imageWidth = getIntAttribute(node2, "imageWidth", -1, true, true, 1, 65535);
                    this.imageHeight = getIntAttribute(node2, "imageHeight", -1, true, true, 1, 65535);
                    this.interlaceFlag = getBooleanAttribute(node2, "interlaceFlag", false, true);
                } else if (nodeName.equals("LocalColorTable")) {
                    int intAttribute = getIntAttribute(node2, "sizeOfLocalColorTable", true, 2, 256);
                    if (intAttribute != 2 && intAttribute != 4 && intAttribute != 8 && intAttribute != 16 && intAttribute != 32 && intAttribute != 64 && intAttribute != 128 && intAttribute != 256) {
                        fatal(node2, "Bad value for LocalColorTable attribute sizeOfLocalColorTable!");
                    }
                    this.sortFlag = getBooleanAttribute(node2, "sortFlag", false, true);
                    this.localColorTable = getColorTable(node2, "ColorTableEntry", true, intAttribute);
                } else if (nodeName.equals("GraphicControlExtension")) {
                    String stringAttribute = getStringAttribute(node2, "disposalMethod", null, true, disposalMethodNames);
                    this.disposalMethod = 0;
                    while (!stringAttribute.equals(disposalMethodNames[this.disposalMethod])) {
                        this.disposalMethod++;
                    }
                    this.userInputFlag = getBooleanAttribute(node2, "userInputFlag", false, true);
                    this.transparentColorFlag = getBooleanAttribute(node2, "transparentColorFlag", false, true);
                    this.delayTime = getIntAttribute(node2, "delayTime", -1, true, true, 0, 65535);
                    this.transparentColorIndex = getIntAttribute(node2, "transparentColorIndex", -1, true, true, 0, 65535);
                } else if (nodeName.equals("PlainTextExtension")) {
                    this.hasPlainTextExtension = true;
                    this.textGridLeft = getIntAttribute(node2, "textGridLeft", -1, true, true, 0, 65535);
                    this.textGridTop = getIntAttribute(node2, "textGridTop", -1, true, true, 0, 65535);
                    this.textGridWidth = getIntAttribute(node2, "textGridWidth", -1, true, true, 1, 65535);
                    this.textGridHeight = getIntAttribute(node2, "textGridHeight", -1, true, true, 1, 65535);
                    this.characterCellWidth = getIntAttribute(node2, "characterCellWidth", -1, true, true, 1, 65535);
                    this.characterCellHeight = getIntAttribute(node2, "characterCellHeight", -1, true, true, 1, 65535);
                    this.textForegroundColor = getIntAttribute(node2, "textForegroundColor", -1, true, true, 0, 255);
                    this.textBackgroundColor = getIntAttribute(node2, "textBackgroundColor", -1, true, true, 0, 255);
                    this.text = fromISO8859(getStringAttribute(node2, "text", "", false, null));
                } else if (nodeName.equals("ApplicationExtensions")) {
                    IIOMetadataNode iIOMetadataNode = (IIOMetadataNode) node2.getFirstChild();
                    if (!iIOMetadataNode.getNodeName().equals("ApplicationExtension")) {
                        fatal(node2, "Only a ApplicationExtension may be a child of a ApplicationExtensions!");
                    }
                    String stringAttribute2 = getStringAttribute(iIOMetadataNode, "applicationID", null, true, null);
                    String stringAttribute3 = getStringAttribute(iIOMetadataNode, "authenticationCode", null, true, null);
                    Object userObject = iIOMetadataNode.getUserObject();
                    if (userObject == null || !(userObject instanceof byte[])) {
                        fatal(iIOMetadataNode, "Bad user object in ApplicationExtension!");
                    }
                    if (this.applicationIDs == null) {
                        this.applicationIDs = new ArrayList();
                        this.authenticationCodes = new ArrayList();
                        this.applicationData = new ArrayList();
                    }
                    this.applicationIDs.add(fromISO8859(stringAttribute2));
                    this.authenticationCodes.add(fromISO8859(stringAttribute3));
                    this.applicationData.add(userObject);
                } else if (nodeName.equals("CommentExtensions")) {
                    Node firstChild2 = node2.getFirstChild();
                    if (firstChild2 != null) {
                        while (firstChild2 != null) {
                            if (!firstChild2.getNodeName().equals("CommentExtension")) {
                                fatal(node2, "Only a CommentExtension may be a child of a CommentExtensions!");
                            }
                            if (this.comments == null) {
                                this.comments = new ArrayList();
                            }
                            this.comments.add(fromISO8859(getStringAttribute(firstChild2, "value", null, true, null)));
                            firstChild2 = firstChild2.getNextSibling();
                        }
                    }
                } else {
                    fatal(node2, "Unknown child of root node!");
                }
                firstChild = node2.getNextSibling();
            } else {
                return;
            }
        }
    }

    @Override // com.sun.imageio.plugins.gif.GIFImageMetadata, com.sun.imageio.plugins.gif.GIFMetadata
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
                        if (node3 == null) {
                            break;
                        }
                        if (node3.getNodeName().equals("Palette")) {
                            this.localColorTable = getColorTable(node3, "PaletteEntry", false, -1);
                            break;
                        }
                        firstChild2 = node3.getNextSibling();
                    }
                } else if (nodeName.equals("Compression")) {
                    Node firstChild3 = node2.getFirstChild();
                    while (true) {
                        Node node4 = firstChild3;
                        if (node4 == null) {
                            break;
                        }
                        if (node4.getNodeName().equals("NumProgressiveScans")) {
                            if (getIntAttribute(node4, "value", 4, false, true, 1, Integer.MAX_VALUE) > 1) {
                                this.interlaceFlag = true;
                            }
                        } else {
                            firstChild3 = node4.getNextSibling();
                        }
                    }
                } else if (nodeName.equals("Dimension")) {
                    Node firstChild4 = node2.getFirstChild();
                    while (true) {
                        Node node5 = firstChild4;
                        if (node5 != null) {
                            String nodeName2 = node5.getNodeName();
                            if (nodeName2.equals("HorizontalPixelOffset")) {
                                this.imageLeftPosition = getIntAttribute(node5, "value", -1, true, true, 0, 65535);
                            } else if (nodeName2.equals("VerticalPixelOffset")) {
                                this.imageTopPosition = getIntAttribute(node5, "value", -1, true, true, 0, 65535);
                            }
                            firstChild4 = node5.getNextSibling();
                        }
                    }
                } else if (nodeName.equals("Text")) {
                    Node firstChild5 = node2.getFirstChild();
                    while (true) {
                        Node node6 = firstChild5;
                        if (node6 != null) {
                            if (node6.getNodeName().equals("TextEntry") && getAttribute(node6, "compression", Separation.COLORANT_NONE, false).equals(Separation.COLORANT_NONE) && Charset.isSupported(getAttribute(node6, "encoding", FTP.DEFAULT_CONTROL_ENCODING, false))) {
                                byte[] bArrFromISO8859 = fromISO8859(getAttribute(node6, "value"));
                                if (this.comments == null) {
                                    this.comments = new ArrayList();
                                }
                                this.comments.add(bArrFromISO8859);
                            }
                            firstChild5 = node6.getNextSibling();
                        }
                    }
                } else if (nodeName.equals("Transparency")) {
                    Node firstChild6 = node2.getFirstChild();
                    while (true) {
                        Node node7 = firstChild6;
                        if (node7 == null) {
                            break;
                        }
                        if (node7.getNodeName().equals("TransparentIndex")) {
                            this.transparentColorIndex = getIntAttribute(node7, "value", -1, true, true, 0, 255);
                            this.transparentColorFlag = true;
                            break;
                        }
                        firstChild6 = node7.getNextSibling();
                    }
                }
                firstChild = node2.getNextSibling();
            } else {
                return;
            }
        }
    }

    @Override // com.sun.imageio.plugins.gif.GIFImageMetadata, javax.imageio.metadata.IIOMetadata
    public void setFromTree(String str, Node node) throws IIOInvalidTreeException {
        reset();
        mergeTree(str, node);
    }
}
