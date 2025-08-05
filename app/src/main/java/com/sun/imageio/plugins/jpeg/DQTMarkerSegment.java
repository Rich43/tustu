package com.sun.imageio.plugins.jpeg;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.imageio.IIOException;
import javax.imageio.metadata.IIOInvalidTreeException;
import javax.imageio.metadata.IIOMetadataNode;
import javax.imageio.plugins.jpeg.JPEGQTable;
import javax.imageio.stream.ImageOutputStream;
import org.w3c.dom.DOMException;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/* loaded from: rt.jar:com/sun/imageio/plugins/jpeg/DQTMarkerSegment.class */
class DQTMarkerSegment extends MarkerSegment {
    List tables;

    DQTMarkerSegment(float f2, boolean z2) {
        super(219);
        this.tables = new ArrayList();
        this.tables.add(new Qtable(true, f2));
        if (z2) {
            this.tables.add(new Qtable(false, f2));
        }
    }

    DQTMarkerSegment(JPEGBuffer jPEGBuffer) throws IOException {
        super(jPEGBuffer);
        this.tables = new ArrayList();
        int length = this.length;
        while (true) {
            int i2 = length;
            if (i2 > 0) {
                Qtable qtable = new Qtable(jPEGBuffer);
                this.tables.add(qtable);
                length = i2 - (qtable.data.length + 1);
            } else {
                jPEGBuffer.bufAvail -= this.length;
                return;
            }
        }
    }

    DQTMarkerSegment(JPEGQTable[] jPEGQTableArr) {
        super(219);
        this.tables = new ArrayList();
        for (int i2 = 0; i2 < jPEGQTableArr.length; i2++) {
            this.tables.add(new Qtable(jPEGQTableArr[i2], i2));
        }
    }

    DQTMarkerSegment(Node node) throws IIOInvalidTreeException {
        super(219);
        this.tables = new ArrayList();
        NodeList childNodes = node.getChildNodes();
        int length = childNodes.getLength();
        if (length < 1 || length > 4) {
            throw new IIOInvalidTreeException("Invalid DQT node", node);
        }
        for (int i2 = 0; i2 < length; i2++) {
            this.tables.add(new Qtable(childNodes.item(i2)));
        }
    }

    @Override // com.sun.imageio.plugins.jpeg.MarkerSegment
    protected Object clone() {
        DQTMarkerSegment dQTMarkerSegment = (DQTMarkerSegment) super.clone();
        dQTMarkerSegment.tables = new ArrayList(this.tables.size());
        Iterator it = this.tables.iterator();
        while (it.hasNext()) {
            dQTMarkerSegment.tables.add(((Qtable) it.next()).clone());
        }
        return dQTMarkerSegment;
    }

    @Override // com.sun.imageio.plugins.jpeg.MarkerSegment
    IIOMetadataNode getNativeNode() throws DOMException {
        IIOMetadataNode iIOMetadataNode = new IIOMetadataNode("dqt");
        for (int i2 = 0; i2 < this.tables.size(); i2++) {
            iIOMetadataNode.appendChild(((Qtable) this.tables.get(i2)).getNativeNode());
        }
        return iIOMetadataNode;
    }

    @Override // com.sun.imageio.plugins.jpeg.MarkerSegment
    void write(ImageOutputStream imageOutputStream) throws IOException {
    }

    @Override // com.sun.imageio.plugins.jpeg.MarkerSegment
    void print() {
        printTag("DQT");
        System.out.println("Num tables: " + Integer.toString(this.tables.size()));
        for (int i2 = 0; i2 < this.tables.size(); i2++) {
            ((Qtable) this.tables.get(i2)).print();
        }
        System.out.println();
    }

    Qtable getChromaForLuma(Qtable qtable) {
        Qtable qtable2;
        boolean z2 = true;
        int i2 = 1;
        while (true) {
            int i3 = i2;
            qtable.getClass();
            if (i3 >= 64) {
                break;
            }
            if (qtable.data[i2] == qtable.data[i2 - 1]) {
                i2++;
            } else {
                z2 = false;
                break;
            }
        }
        if (z2) {
            qtable2 = (Qtable) qtable.clone();
            qtable2.tableID = 1;
        } else {
            int i4 = 0;
            int i5 = 1;
            while (true) {
                int i6 = i5;
                qtable.getClass();
                if (i6 >= 64) {
                    break;
                }
                if (qtable.data[i5] > qtable.data[i4]) {
                    i4 = i5;
                }
                i5++;
            }
            qtable2 = new Qtable(JPEGQTable.K2Div2Chrominance.getScaledInstance(qtable.data[i4] / JPEGQTable.K1Div2Luminance.getTable()[i4], true), 1);
        }
        return qtable2;
    }

    Qtable getQtableFromNode(Node node) throws IIOInvalidTreeException {
        return new Qtable(node);
    }

    /* loaded from: rt.jar:com/sun/imageio/plugins/jpeg/DQTMarkerSegment$Qtable.class */
    class Qtable implements Cloneable {
        int elementPrecision;
        int tableID;
        final int QTABLE_SIZE = 64;
        int[] data;
        private final int[] zigzag;

        Qtable(boolean z2, float f2) {
            JPEGQTable scaledInstance;
            this.QTABLE_SIZE = 64;
            this.zigzag = new int[]{0, 1, 5, 6, 14, 15, 27, 28, 2, 4, 7, 13, 16, 26, 29, 42, 3, 8, 12, 17, 25, 30, 41, 43, 9, 11, 18, 24, 31, 40, 44, 53, 10, 19, 23, 32, 39, 45, 52, 54, 20, 22, 33, 38, 46, 51, 55, 60, 21, 34, 37, 47, 50, 56, 59, 61, 35, 36, 48, 49, 57, 58, 62, 63};
            this.elementPrecision = 0;
            if (z2) {
                this.tableID = 0;
                scaledInstance = JPEGQTable.K1Div2Luminance;
            } else {
                this.tableID = 1;
                scaledInstance = JPEGQTable.K2Div2Chrominance;
            }
            if (f2 != 0.75f) {
                float fConvertToLinearQuality = JPEG.convertToLinearQuality(f2);
                if (z2) {
                    scaledInstance = JPEGQTable.K1Luminance.getScaledInstance(fConvertToLinearQuality, true);
                } else {
                    scaledInstance = JPEGQTable.K2Div2Chrominance.getScaledInstance(fConvertToLinearQuality, true);
                }
            }
            this.data = scaledInstance.getTable();
        }

        Qtable(JPEGBuffer jPEGBuffer) throws IIOException {
            this.QTABLE_SIZE = 64;
            this.zigzag = new int[]{0, 1, 5, 6, 14, 15, 27, 28, 2, 4, 7, 13, 16, 26, 29, 42, 3, 8, 12, 17, 25, 30, 41, 43, 9, 11, 18, 24, 31, 40, 44, 53, 10, 19, 23, 32, 39, 45, 52, 54, 20, 22, 33, 38, 46, 51, 55, 60, 21, 34, 37, 47, 50, 56, 59, 61, 35, 36, 48, 49, 57, 58, 62, 63};
            this.elementPrecision = jPEGBuffer.buf[jPEGBuffer.bufPtr] >>> 4;
            byte[] bArr = jPEGBuffer.buf;
            int i2 = jPEGBuffer.bufPtr;
            jPEGBuffer.bufPtr = i2 + 1;
            this.tableID = bArr[i2] & 15;
            if (this.elementPrecision != 0) {
                throw new IIOException("Unsupported element precision");
            }
            this.data = new int[64];
            for (int i3 = 0; i3 < 64; i3++) {
                this.data[i3] = jPEGBuffer.buf[jPEGBuffer.bufPtr + this.zigzag[i3]] & 255;
            }
            jPEGBuffer.bufPtr += 64;
        }

        Qtable(JPEGQTable jPEGQTable, int i2) {
            this.QTABLE_SIZE = 64;
            this.zigzag = new int[]{0, 1, 5, 6, 14, 15, 27, 28, 2, 4, 7, 13, 16, 26, 29, 42, 3, 8, 12, 17, 25, 30, 41, 43, 9, 11, 18, 24, 31, 40, 44, 53, 10, 19, 23, 32, 39, 45, 52, 54, 20, 22, 33, 38, 46, 51, 55, 60, 21, 34, 37, 47, 50, 56, 59, 61, 35, 36, 48, 49, 57, 58, 62, 63};
            this.elementPrecision = 0;
            this.tableID = i2;
            this.data = jPEGQTable.getTable();
        }

        Qtable(Node node) throws IIOInvalidTreeException {
            this.QTABLE_SIZE = 64;
            this.zigzag = new int[]{0, 1, 5, 6, 14, 15, 27, 28, 2, 4, 7, 13, 16, 26, 29, 42, 3, 8, 12, 17, 25, 30, 41, 43, 9, 11, 18, 24, 31, 40, 44, 53, 10, 19, 23, 32, 39, 45, 52, 54, 20, 22, 33, 38, 46, 51, 55, 60, 21, 34, 37, 47, 50, 56, 59, 61, 35, 36, 48, 49, 57, 58, 62, 63};
            if (node.getNodeName().equals("dqtable")) {
                NamedNodeMap attributes = node.getAttributes();
                int length = attributes.getLength();
                if (length < 1 || length > 2) {
                    throw new IIOInvalidTreeException("dqtable node must have 1 or 2 attributes", node);
                }
                this.elementPrecision = 0;
                this.tableID = MarkerSegment.getAttributeValue(node, attributes, "qtableId", 0, 3, true);
                if (node instanceof IIOMetadataNode) {
                    JPEGQTable jPEGQTable = (JPEGQTable) ((IIOMetadataNode) node).getUserObject();
                    if (jPEGQTable == null) {
                        throw new IIOInvalidTreeException("dqtable node must have user object", node);
                    }
                    this.data = jPEGQTable.getTable();
                    return;
                }
                throw new IIOInvalidTreeException("dqtable node must have user object", node);
            }
            throw new IIOInvalidTreeException("Invalid node, expected dqtable", node);
        }

        protected Object clone() {
            Qtable qtable = null;
            try {
                qtable = (Qtable) super.clone();
            } catch (CloneNotSupportedException e2) {
            }
            if (this.data != null) {
                qtable.data = (int[]) this.data.clone();
            }
            return qtable;
        }

        IIOMetadataNode getNativeNode() {
            IIOMetadataNode iIOMetadataNode = new IIOMetadataNode("dqtable");
            iIOMetadataNode.setAttribute("elementPrecision", Integer.toString(this.elementPrecision));
            iIOMetadataNode.setAttribute("qtableId", Integer.toString(this.tableID));
            iIOMetadataNode.setUserObject(new JPEGQTable(this.data));
            return iIOMetadataNode;
        }

        void print() {
            System.out.println("Table id: " + Integer.toString(this.tableID));
            System.out.println("Element precision: " + Integer.toString(this.elementPrecision));
            new JPEGQTable(this.data).toString();
        }
    }
}
