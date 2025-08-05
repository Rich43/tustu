package net.lingala.zip4j.model;

/* loaded from: zip4j_1.3.1.jar:net/lingala/zip4j/model/Zip64ExtendedInfo.class */
public class Zip64ExtendedInfo {
    private int header;
    private int size;
    private long compressedSize = -1;
    private long unCompressedSize = -1;
    private long offsetLocalHeader = -1;
    private int diskNumberStart = -1;

    public int getHeader() {
        return this.header;
    }

    public void setHeader(int header) {
        this.header = header;
    }

    public int getSize() {
        return this.size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public long getCompressedSize() {
        return this.compressedSize;
    }

    public void setCompressedSize(long compressedSize) {
        this.compressedSize = compressedSize;
    }

    public long getUnCompressedSize() {
        return this.unCompressedSize;
    }

    public void setUnCompressedSize(long unCompressedSize) {
        this.unCompressedSize = unCompressedSize;
    }

    public long getOffsetLocalHeader() {
        return this.offsetLocalHeader;
    }

    public void setOffsetLocalHeader(long offsetLocalHeader) {
        this.offsetLocalHeader = offsetLocalHeader;
    }

    public int getDiskNumberStart() {
        return this.diskNumberStart;
    }

    public void setDiskNumberStart(int diskNumberStart) {
        this.diskNumberStart = diskNumberStart;
    }
}
