package javax.imageio.plugins.jpeg;

import java.util.Locale;
import javax.imageio.ImageWriteParam;

/* loaded from: rt.jar:javax/imageio/plugins/jpeg/JPEGImageWriteParam.class */
public class JPEGImageWriteParam extends ImageWriteParam {
    private JPEGQTable[] qTables;
    private JPEGHuffmanTable[] DCHuffmanTables;
    private JPEGHuffmanTable[] ACHuffmanTables;
    private boolean optimizeHuffman;
    private String[] compressionNames;
    private float[] qualityVals;
    private String[] qualityDescs;

    public JPEGImageWriteParam(Locale locale) {
        super(locale);
        this.qTables = null;
        this.DCHuffmanTables = null;
        this.ACHuffmanTables = null;
        this.optimizeHuffman = false;
        this.compressionNames = new String[]{"JPEG"};
        this.qualityVals = new float[]{0.0f, 0.3f, 0.75f, 1.0f};
        this.qualityDescs = new String[]{"Low quality", "Medium quality", "Visually lossless"};
        this.canWriteProgressive = true;
        this.progressiveMode = 0;
        this.canWriteCompressed = true;
        this.compressionTypes = this.compressionNames;
        this.compressionType = this.compressionTypes[0];
        this.compressionQuality = 0.75f;
    }

    @Override // javax.imageio.ImageWriteParam
    public void unsetCompression() {
        if (getCompressionMode() != 2) {
            throw new IllegalStateException("Compression mode not MODE_EXPLICIT!");
        }
        this.compressionQuality = 0.75f;
    }

    @Override // javax.imageio.ImageWriteParam
    public boolean isCompressionLossless() {
        if (getCompressionMode() != 2) {
            throw new IllegalStateException("Compression mode not MODE_EXPLICIT!");
        }
        return false;
    }

    @Override // javax.imageio.ImageWriteParam
    public String[] getCompressionQualityDescriptions() {
        if (getCompressionMode() != 2) {
            throw new IllegalStateException("Compression mode not MODE_EXPLICIT!");
        }
        if (getCompressionTypes() != null && getCompressionType() == null) {
            throw new IllegalStateException("No compression type set!");
        }
        return (String[]) this.qualityDescs.clone();
    }

    @Override // javax.imageio.ImageWriteParam
    public float[] getCompressionQualityValues() {
        if (getCompressionMode() != 2) {
            throw new IllegalStateException("Compression mode not MODE_EXPLICIT!");
        }
        if (getCompressionTypes() != null && getCompressionType() == null) {
            throw new IllegalStateException("No compression type set!");
        }
        return (float[]) this.qualityVals.clone();
    }

    public boolean areTablesSet() {
        return this.qTables != null;
    }

    public void setEncodeTables(JPEGQTable[] jPEGQTableArr, JPEGHuffmanTable[] jPEGHuffmanTableArr, JPEGHuffmanTable[] jPEGHuffmanTableArr2) {
        if (jPEGQTableArr == null || jPEGHuffmanTableArr == null || jPEGHuffmanTableArr2 == null || jPEGQTableArr.length > 4 || jPEGHuffmanTableArr.length > 4 || jPEGHuffmanTableArr2.length > 4 || jPEGHuffmanTableArr.length != jPEGHuffmanTableArr2.length) {
            throw new IllegalArgumentException("Invalid JPEG table arrays");
        }
        this.qTables = (JPEGQTable[]) jPEGQTableArr.clone();
        this.DCHuffmanTables = (JPEGHuffmanTable[]) jPEGHuffmanTableArr.clone();
        this.ACHuffmanTables = (JPEGHuffmanTable[]) jPEGHuffmanTableArr2.clone();
    }

    public void unsetEncodeTables() {
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

    public void setOptimizeHuffmanTables(boolean z2) {
        this.optimizeHuffman = z2;
    }

    public boolean getOptimizeHuffmanTables() {
        return this.optimizeHuffman;
    }
}
