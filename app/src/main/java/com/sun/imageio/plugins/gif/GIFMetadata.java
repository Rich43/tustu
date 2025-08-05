package com.sun.imageio.plugins.gif;

import javax.imageio.metadata.IIOInvalidTreeException;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.metadata.IIOMetadataFormatImpl;
import org.w3c.dom.DOMException;
import org.w3c.dom.Node;

/* loaded from: rt.jar:com/sun/imageio/plugins/gif/GIFMetadata.class */
abstract class GIFMetadata extends IIOMetadata {
    static final int UNDEFINED_INTEGER_VALUE = -1;

    protected abstract void mergeNativeTree(Node node) throws IIOInvalidTreeException;

    protected abstract void mergeStandardTree(Node node) throws IIOInvalidTreeException;

    protected static void fatal(Node node, String str) throws IIOInvalidTreeException {
        throw new IIOInvalidTreeException(str, node);
    }

    protected static String getStringAttribute(Node node, String str, String str2, boolean z2, String[] strArr) throws IIOInvalidTreeException, DOMException {
        Node namedItem = node.getAttributes().getNamedItem(str);
        if (namedItem == null) {
            if (!z2) {
                return str2;
            }
            fatal(node, "Required attribute " + str + " not present!");
        }
        String nodeValue = namedItem.getNodeValue();
        if (strArr != null) {
            if (nodeValue == null) {
                fatal(node, "Null value for " + node.getNodeName() + " attribute " + str + "!");
            }
            boolean z3 = false;
            int length = strArr.length;
            int i2 = 0;
            while (true) {
                if (i2 >= length) {
                    break;
                }
                if (!nodeValue.equals(strArr[i2])) {
                    i2++;
                } else {
                    z3 = true;
                    break;
                }
            }
            if (!z3) {
                fatal(node, "Bad value for " + node.getNodeName() + " attribute " + str + "!");
            }
        }
        return nodeValue;
    }

    protected static int getIntAttribute(Node node, String str, int i2, boolean z2, boolean z3, int i3, int i4) throws IIOInvalidTreeException, DOMException {
        String stringAttribute = getStringAttribute(node, str, null, z2, null);
        if (stringAttribute == null || "".equals(stringAttribute)) {
            return i2;
        }
        int i5 = i2;
        try {
            i5 = Integer.parseInt(stringAttribute);
        } catch (NumberFormatException e2) {
            fatal(node, "Bad value for " + node.getNodeName() + " attribute " + str + "!");
        }
        if (z3 && (i5 < i3 || i5 > i4)) {
            fatal(node, "Bad value for " + node.getNodeName() + " attribute " + str + "!");
        }
        return i5;
    }

    protected static float getFloatAttribute(Node node, String str, float f2, boolean z2) throws IIOInvalidTreeException, DOMException {
        String stringAttribute = getStringAttribute(node, str, null, z2, null);
        if (stringAttribute == null) {
            return f2;
        }
        return Float.parseFloat(stringAttribute);
    }

    protected static int getIntAttribute(Node node, String str, boolean z2, int i2, int i3) throws IIOInvalidTreeException {
        return getIntAttribute(node, str, -1, true, z2, i2, i3);
    }

    protected static float getFloatAttribute(Node node, String str) throws IIOInvalidTreeException {
        return getFloatAttribute(node, str, -1.0f, true);
    }

    protected static boolean getBooleanAttribute(Node node, String str, boolean z2, boolean z3) throws IIOInvalidTreeException, DOMException {
        Node namedItem = node.getAttributes().getNamedItem(str);
        if (namedItem == null) {
            if (!z3) {
                return z2;
            }
            fatal(node, "Required attribute " + str + " not present!");
        }
        String nodeValue = namedItem.getNodeValue();
        if (nodeValue.equals("TRUE") || nodeValue.equals("true")) {
            return true;
        }
        if (nodeValue.equals("FALSE") || nodeValue.equals("false")) {
            return false;
        }
        fatal(node, "Attribute " + str + " must be 'TRUE' or 'FALSE'!");
        return false;
    }

    protected static boolean getBooleanAttribute(Node node, String str) throws IIOInvalidTreeException {
        return getBooleanAttribute(node, str, false, true);
    }

    protected static int getEnumeratedAttribute(Node node, String str, String[] strArr, int i2, boolean z2) throws IIOInvalidTreeException, DOMException {
        Node namedItem = node.getAttributes().getNamedItem(str);
        if (namedItem == null) {
            if (!z2) {
                return i2;
            }
            fatal(node, "Required attribute " + str + " not present!");
        }
        String nodeValue = namedItem.getNodeValue();
        for (int i3 = 0; i3 < strArr.length; i3++) {
            if (nodeValue.equals(strArr[i3])) {
                return i3;
            }
        }
        fatal(node, "Illegal value for attribute " + str + "!");
        return -1;
    }

    protected static int getEnumeratedAttribute(Node node, String str, String[] strArr) throws IIOInvalidTreeException {
        return getEnumeratedAttribute(node, str, strArr, -1, true);
    }

    protected static String getAttribute(Node node, String str, String str2, boolean z2) throws IIOInvalidTreeException {
        Node namedItem = node.getAttributes().getNamedItem(str);
        if (namedItem == null) {
            if (!z2) {
                return str2;
            }
            fatal(node, "Required attribute " + str + " not present!");
        }
        return namedItem.getNodeValue();
    }

    protected static String getAttribute(Node node, String str) throws IIOInvalidTreeException {
        return getAttribute(node, str, null, true);
    }

    protected GIFMetadata(boolean z2, String str, String str2, String[] strArr, String[] strArr2) {
        super(z2, str, str2, strArr, strArr2);
    }

    @Override // javax.imageio.metadata.IIOMetadata
    public void mergeTree(String str, Node node) throws IIOInvalidTreeException {
        if (str.equals(this.nativeMetadataFormatName)) {
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

    protected byte[] getColorTable(Node node, String str, boolean z2, int i2) throws IIOInvalidTreeException {
        byte[] bArr = new byte[256];
        byte[] bArr2 = new byte[256];
        byte[] bArr3 = new byte[256];
        int i3 = -1;
        Node firstChild = node.getFirstChild();
        if (firstChild == null) {
            fatal(node, "Palette has no entries!");
        }
        while (firstChild != null) {
            if (!firstChild.getNodeName().equals(str)) {
                fatal(node, "Only a " + str + " may be a child of a " + firstChild.getNodeName() + "!");
            }
            int intAttribute = getIntAttribute(firstChild, "index", true, 0, 255);
            if (intAttribute > i3) {
                i3 = intAttribute;
            }
            bArr[intAttribute] = (byte) getIntAttribute(firstChild, "red", true, 0, 255);
            bArr2[intAttribute] = (byte) getIntAttribute(firstChild, "green", true, 0, 255);
            bArr3[intAttribute] = (byte) getIntAttribute(firstChild, "blue", true, 0, 255);
            firstChild = firstChild.getNextSibling();
        }
        int i4 = i3 + 1;
        if (z2 && i4 != i2) {
            fatal(node, "Unexpected length for palette!");
        }
        byte[] bArr4 = new byte[3 * i4];
        int i5 = 0;
        for (int i6 = 0; i6 < i4; i6++) {
            int i7 = i5;
            int i8 = i5 + 1;
            bArr4[i7] = bArr[i6];
            int i9 = i8 + 1;
            bArr4[i8] = bArr2[i6];
            i5 = i9 + 1;
            bArr4[i9] = bArr3[i6];
        }
        return bArr4;
    }
}
