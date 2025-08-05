package javax.imageio.plugins.jpeg;

import javax.imageio.ImageReadParam;

/* loaded from: rt.jar:javax/imageio/plugins/jpeg/JPEGImageReadParam.class */
public class JPEGImageReadParam extends ImageReadParam {
    private JPEGQTable[] qTables = null;
    private JPEGHuffmanTable[] DCHuffmanTables = null;
    private JPEGHuffmanTable[] ACHuffmanTables = null;

    public boolean areTablesSet() {
        return this.qTables != null;
    }

    public void setDecodeTables(JPEGQTable[] jPEGQTableArr, JPEGHuffmanTable[] jPEGHuffmanTableArr, JPEGHuffmanTable[] jPEGHuffmanTableArr2) {
        if (jPEGQTableArr == null || jPEGHuffmanTableArr == null || jPEGHuffmanTableArr2 == null || jPEGQTableArr.length > 4 || jPEGHuffmanTableArr.length > 4 || jPEGHuffmanTableArr2.length > 4 || jPEGHuffmanTableArr.length != jPEGHuffmanTableArr2.length) {
            throw new IllegalArgumentException("Invalid JPEG table arrays");
        }
        this.qTables = (JPEGQTable[]) jPEGQTableArr.clone();
        this.DCHuffmanTables = (JPEGHuffmanTable[]) jPEGHuffmanTableArr.clone();
        this.ACHuffmanTables = (JPEGHuffmanTable[]) jPEGHuffmanTableArr2.clone();
    }

    public void unsetDecodeTables() {
        this.qTables = null;
        this.DCHuffmanTables = null;
        this.ACHuffmanTables = null;
    }

    public JPEGQTable[] getQTables() {
        if (this.qTables != null) {
            return (JPEGQTable[]) this.qTables.clone();
        }
        return null;
    }

    public JPEGHuffmanTable[] getDCHuffmanTables() {
        if (this.DCHuffmanTables != null) {
            return (JPEGHuffmanTable[]) this.DCHuffmanTables.clone();
        }
        return null;
    }

    public JPEGHuffmanTable[] getACHuffmanTables() {
        if (this.ACHuffmanTables != null) {
            return (JPEGHuffmanTable[]) this.ACHuffmanTables.clone();
        }
        return null;
    }
}
