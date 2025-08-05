package com.sun.imageio.plugins.jpeg;

import com.sun.org.apache.xalan.internal.templates.Constants;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.imageio.metadata.IIOInvalidTreeException;
import javax.imageio.metadata.IIOMetadataNode;
import javax.imageio.plugins.jpeg.JPEGHuffmanTable;
import javax.imageio.stream.ImageOutputStream;
import org.w3c.dom.DOMException;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/* loaded from: rt.jar:com/sun/imageio/plugins/jpeg/DHTMarkerSegment.class */
class DHTMarkerSegment extends MarkerSegment {
    List tables;

    DHTMarkerSegment(boolean z2) {
        super(196);
        this.tables = new ArrayList();
        this.tables.add(new Htable(JPEGHuffmanTable.StdDCLuminance, true, 0));
        if (z2) {
            this.tables.add(new Htable(JPEGHuffmanTable.StdDCChrominance, true, 1));
        }
        this.tables.add(new Htable(JPEGHuffmanTable.StdACLuminance, false, 0));
        if (z2) {
            this.tables.add(new Htable(JPEGHuffmanTable.StdACChrominance, false, 1));
        }
    }

    DHTMarkerSegment(JPEGBuffer jPEGBuffer) throws IOException {
        super(jPEGBuffer);
        this.tables = new ArrayList();
        int length = this.length;
        while (true) {
            int i2 = length;
            if (i2 > 0) {
                Htable htable = new Htable(jPEGBuffer);
                this.tables.add(htable);
                length = i2 - (17 + htable.values.length);
            } else {
                jPEGBuffer.bufAvail -= this.length;
                return;
            }
        }
    }

    DHTMarkerSegment(JPEGHuffmanTable[] jPEGHuffmanTableArr, JPEGHuffmanTable[] jPEGHuffmanTableArr2) {
        super(196);
        this.tables = new ArrayList();
        for (int i2 = 0; i2 < jPEGHuffmanTableArr.length; i2++) {
            this.tables.add(new Htable(jPEGHuffmanTableArr[i2], true, i2));
        }
        for (int i3 = 0; i3 < jPEGHuffmanTableArr2.length; i3++) {
            this.tables.add(new Htable(jPEGHuffmanTableArr2[i3], false, i3));
        }
    }

    DHTMarkerSegment(Node node) throws IIOInvalidTreeException {
        super(196);
        this.tables = new ArrayList();
        NodeList childNodes = node.getChildNodes();
        int length = childNodes.getLength();
        if (length < 1 || length > 4) {
            throw new IIOInvalidTreeException("Invalid DHT node", node);
        }
        for (int i2 = 0; i2 < length; i2++) {
            this.tables.add(new Htable(childNodes.item(i2)));
        }
    }

    @Override // com.sun.imageio.plugins.jpeg.MarkerSegment
    protected Object clone() {
        DHTMarkerSegment dHTMarkerSegment = (DHTMarkerSegment) super.clone();
        dHTMarkerSegment.tables = new ArrayList(this.tables.size());
        Iterator it = this.tables.iterator();
        while (it.hasNext()) {
            dHTMarkerSegment.tables.add(((Htable) it.next()).clone());
        }
        return dHTMarkerSegment;
    }

    @Override // com.sun.imageio.plugins.jpeg.MarkerSegment
    IIOMetadataNode getNativeNode() throws DOMException {
        IIOMetadataNode iIOMetadataNode = new IIOMetadataNode("dht");
        for (int i2 = 0; i2 < this.tables.size(); i2++) {
            iIOMetadataNode.appendChild(((Htable) this.tables.get(i2)).getNativeNode());
        }
        return iIOMetadataNode;
    }

    @Override // com.sun.imageio.plugins.jpeg.MarkerSegment
    void write(ImageOutputStream imageOutputStream) throws IOException {
    }

    @Override // com.sun.imageio.plugins.jpeg.MarkerSegment
    void print() {
        printTag("DHT");
        System.out.println("Num tables: " + Integer.toString(this.tables.size()));
        for (int i2 = 0; i2 < this.tables.size(); i2++) {
            ((Htable) this.tables.get(i2)).print();
        }
        System.out.println();
    }

    Htable getHtableFromNode(Node node) throws IIOInvalidTreeException {
        return new Htable(node);
    }

    void addHtable(JPEGHuffmanTable jPEGHuffmanTable, boolean z2, int i2) {
        this.tables.add(new Htable(jPEGHuffmanTable, z2, i2));
    }

    /* loaded from: rt.jar:com/sun/imageio/plugins/jpeg/DHTMarkerSegment$Htable.class */
    class Htable implements Cloneable {
        int tableClass;
        int tableID;
        private static final int NUM_LENGTHS = 16;
        short[] numCodes;
        short[] values;

        Htable(JPEGBuffer jPEGBuffer) {
            this.numCodes = new short[16];
            this.tableClass = jPEGBuffer.buf[jPEGBuffer.bufPtr] >>> 4;
            byte[] bArr = jPEGBuffer.buf;
            int i2 = jPEGBuffer.bufPtr;
            jPEGBuffer.bufPtr = i2 + 1;
            this.tableID = bArr[i2] & 15;
            for (int i3 = 0; i3 < 16; i3++) {
                byte[] bArr2 = jPEGBuffer.buf;
                int i4 = jPEGBuffer.bufPtr;
                jPEGBuffer.bufPtr = i4 + 1;
                this.numCodes[i3] = (short) (bArr2[i4] & 255);
            }
            int i5 = 0;
            for (int i6 = 0; i6 < 16; i6++) {
                i5 += this.numCodes[i6];
            }
            this.values = new short[i5];
            for (int i7 = 0; i7 < i5; i7++) {
                byte[] bArr3 = jPEGBuffer.buf;
                int i8 = jPEGBuffer.bufPtr;
                jPEGBuffer.bufPtr = i8 + 1;
                this.values[i7] = (short) (bArr3[i8] & 255);
            }
        }

        Htable(JPEGHuffmanTable jPEGHuffmanTable, boolean z2, int i2) {
            this.numCodes = new short[16];
            this.tableClass = z2 ? 0 : 1;
            this.tableID = i2;
            this.numCodes = jPEGHuffmanTable.getLengths();
            this.values = jPEGHuffmanTable.getValues();
        }

        Htable(Node node) throws IIOInvalidTreeException {
            this.numCodes = new short[16];
            if (node.getNodeName().equals("dhtable")) {
                NamedNodeMap attributes = node.getAttributes();
                if (attributes.getLength() != 2) {
                    throw new IIOInvalidTreeException("dhtable node must have 2 attributes", node);
                }
                this.tableClass = MarkerSegment.getAttributeValue(node, attributes, Constants.ATTRNAME_CLASS, 0, 1, true);
                this.tableID = MarkerSegment.getAttributeValue(node, attributes, "htableId", 0, 3, true);
                if (node instanceof IIOMetadataNode) {
                    JPEGHuffmanTable jPEGHuffmanTable = (JPEGHuffmanTable) ((IIOMetadataNode) node).getUserObject();
                    if (jPEGHuffmanTable == null) {
                        throw new IIOInvalidTreeException("dhtable node must have user object", node);
                    }
                    this.numCodes = jPEGHuffmanTable.getLengths();
                    this.values = jPEGHuffmanTable.getValues();
                    return;
                }
                throw new IIOInvalidTreeException("dhtable node must have user object", node);
            }
            throw new IIOInvalidTreeException("Invalid node, expected dqtable", node);
        }

        protected Object clone() {
            Htable htable = null;
            try {
                htable = (Htable) super.clone();
            } catch (CloneNotSupportedException e2) {
            }
            if (this.numCodes != null) {
                htable.numCodes = (short[]) this.numCodes.clone();
            }
            if (this.values != null) {
                htable.values = (short[]) this.values.clone();
            }
            return htable;
        }

        IIOMetadataNode getNativeNode() {
            IIOMetadataNode iIOMetadataNode = new IIOMetadataNode("dhtable");
            iIOMetadataNode.setAttribute(Constants.ATTRNAME_CLASS, Integer.toString(this.tableClass));
            iIOMetadataNode.setAttribute("htableId", Integer.toString(this.tableID));
            iIOMetadataNode.setUserObject(new JPEGHuffmanTable(this.numCodes, this.values));
            return iIOMetadataNode;
        }

        void print() {
            System.out.println("Huffman Table");
            System.out.println("table class: " + (this.tableClass == 0 ? "DC" : "AC"));
            System.out.println("table id: " + Integer.toString(this.tableID));
            new JPEGHuffmanTable(this.numCodes, this.values).toString();
        }
    }
}
