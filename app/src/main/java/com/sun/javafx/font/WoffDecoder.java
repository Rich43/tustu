package com.sun.javafx.font;

import com.sun.javafx.font.FontFileReader;
import java.util.Arrays;
import java.util.zip.Inflater;

/* loaded from: jfxrt.jar:com/sun/javafx/font/WoffDecoder.class */
class WoffDecoder extends FontFileWriter {
    WoffHeader woffHeader;
    WoffDirectoryEntry[] woffTableDirectory;

    public void decode(FontFileReader input) throws Exception {
        input.reset();
        initWoffTables(input);
        if (this.woffHeader == null || this.woffTableDirectory == null) {
            throw new Exception("WoffDecoder: failure reading header");
        }
        int format = this.woffHeader.flavor;
        if (format != 65536 && format != 1953658213 && format != 1330926671) {
            throw new Exception("WoffDecoder: invalid flavor");
        }
        short numTables = this.woffHeader.numTables;
        setLength(this.woffHeader.totalSfntSize);
        writeHeader(format, numTables);
        Arrays.sort(this.woffTableDirectory, (o1, o2) -> {
            return o1.offset - o2.offset;
        });
        Inflater decompressor = new Inflater();
        int offset = 12 + (numTables * 16);
        for (int i2 = 0; i2 < this.woffTableDirectory.length; i2++) {
            WoffDirectoryEntry table = this.woffTableDirectory[i2];
            writeDirectoryEntry(table.index, table.tag, table.origChecksum, offset, table.origLength);
            FontFileReader.Buffer buffer = input.readBlock(table.offset, table.comLength);
            byte[] bytes = new byte[table.comLength];
            buffer.get(0, bytes, 0, table.comLength);
            if (table.comLength != table.origLength) {
                decompressor.setInput(bytes);
                byte[] output = new byte[table.origLength];
                int length = decompressor.inflate(output);
                if (length != table.origLength) {
                    throw new Exception("WoffDecoder: failure expanding table");
                }
                decompressor.reset();
                bytes = output;
            }
            seek(offset);
            writeBytes(bytes);
            offset += (table.origLength + 3) & (-4);
        }
        decompressor.end();
    }

    void initWoffTables(FontFileReader input) throws Exception {
        long filesize = input.getLength();
        if (filesize < 44) {
            throw new Exception("WoffDecoder: invalid filesize");
        }
        FontFileReader.Buffer buffer = input.readBlock(0, 44);
        WoffHeader header = new WoffHeader(buffer);
        int i2 = header.numTables;
        if (header.signature != 2001684038) {
            throw new Exception("WoffDecoder: invalid signature");
        }
        if (header.reserved != 0) {
            throw new Exception("WoffDecoder: invalid reserved != 0");
        }
        if (filesize < 44 + (i2 * 20)) {
            throw new Exception("WoffDecoder: invalid filesize");
        }
        WoffDirectoryEntry[] tableDirectory = new WoffDirectoryEntry[i2];
        int headerOffset = 44 + (i2 * 20);
        int size = 12 + (i2 * 16);
        FontFileReader.Buffer buffer2 = input.readBlock(44, i2 * 20);
        for (int i3 = 0; i3 < i2; i3++) {
            WoffDirectoryEntry table = new WoffDirectoryEntry(buffer2, i3);
            tableDirectory[i3] = table;
            if (table.tag <= 0) {
                throw new Exception("WoffDecoder: table directory not ordered by tag");
            }
            int startOffset = table.offset;
            int endOffset = table.offset + table.comLength;
            if (headerOffset > startOffset || startOffset > filesize) {
                throw new Exception("WoffDecoder: invalid table offset");
            }
            if (startOffset > endOffset || endOffset > filesize) {
                throw new Exception("WoffDecoder: invalid table offset");
            }
            if (table.comLength > table.origLength) {
                throw new Exception("WoffDecoder: invalid compressed length");
            }
            size += (table.origLength + 3) & (-4);
            if (size > header.totalSfntSize) {
                throw new Exception("WoffDecoder: invalid totalSfntSize");
            }
        }
        if (size != header.totalSfntSize) {
            throw new Exception("WoffDecoder: invalid totalSfntSize");
        }
        this.woffHeader = header;
        this.woffTableDirectory = tableDirectory;
    }

    /* loaded from: jfxrt.jar:com/sun/javafx/font/WoffDecoder$WoffHeader.class */
    static class WoffHeader {
        int signature;
        int flavor;
        int length;
        short numTables;
        short reserved;
        int totalSfntSize;
        short majorVersion;
        short minorVersion;
        int metaOffset;
        int metaLength;
        int metaOrigLength;
        int privateOffset;
        int privateLength;

        WoffHeader(FontFileReader.Buffer buffer) {
            this.signature = buffer.getInt();
            this.flavor = buffer.getInt();
            this.length = buffer.getInt();
            this.numTables = buffer.getShort();
            this.reserved = buffer.getShort();
            this.totalSfntSize = buffer.getInt();
            this.majorVersion = buffer.getShort();
            this.minorVersion = buffer.getShort();
            this.metaOffset = buffer.getInt();
            this.metaLength = buffer.getInt();
            this.metaOrigLength = buffer.getInt();
            this.privateOffset = buffer.getInt();
            this.privateLength = buffer.getInt();
        }
    }

    /* loaded from: jfxrt.jar:com/sun/javafx/font/WoffDecoder$WoffDirectoryEntry.class */
    static class WoffDirectoryEntry {
        int tag;
        int offset;
        int comLength;
        int origLength;
        int origChecksum;
        int index;

        WoffDirectoryEntry(FontFileReader.Buffer buffer, int index) {
            this.tag = buffer.getInt();
            this.offset = buffer.getInt();
            this.comLength = buffer.getInt();
            this.origLength = buffer.getInt();
            this.origChecksum = buffer.getInt();
            this.index = index;
        }
    }
}
