package net.lingala.zip4j.model;

/* loaded from: zip4j_1.3.1.jar:net/lingala/zip4j/model/DataDescriptor.class */
public class DataDescriptor {
    private String crc32;
    private int compressedSize;
    private int uncompressedSize;

    public String getCrc32() {
        return this.crc32;
    }

    public void setCrc32(String crc32) {
        this.crc32 = crc32;
    }

    public int getCompressedSize() {
        return this.compressedSize;
    }

    public void setCompressedSize(int compressedSize) {
        this.compressedSize = compressedSize;
    }

    public int getUncompressedSize() {
        return this.uncompressedSize;
    }

    public void setUncompressedSize(int uncompressedSize) {
        this.uncompressedSize = uncompressedSize;
    }
}
