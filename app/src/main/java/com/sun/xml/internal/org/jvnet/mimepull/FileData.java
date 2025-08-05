package com.sun.xml.internal.org.jvnet.mimepull;

import java.nio.ByteBuffer;

/* loaded from: rt.jar:com/sun/xml/internal/org/jvnet/mimepull/FileData.class */
final class FileData implements Data {
    private final DataFile file;
    private final long pointer;
    private final int length;

    FileData(DataFile file, ByteBuffer buf) {
        this(file, file.writeTo(buf.array(), 0, buf.limit()), buf.limit());
    }

    FileData(DataFile file, long pointer, int length) {
        this.file = file;
        this.pointer = pointer;
        this.length = length;
    }

    @Override // com.sun.xml.internal.org.jvnet.mimepull.Data
    public byte[] read() {
        byte[] buf = new byte[this.length];
        this.file.read(this.pointer, buf, 0, this.length);
        return buf;
    }

    @Override // com.sun.xml.internal.org.jvnet.mimepull.Data
    public long writeTo(DataFile file) {
        throw new IllegalStateException();
    }

    @Override // com.sun.xml.internal.org.jvnet.mimepull.Data
    public int size() {
        return this.length;
    }

    @Override // com.sun.xml.internal.org.jvnet.mimepull.Data
    public Data createNext(DataHead dataHead, ByteBuffer buf) {
        return new FileData(this.file, buf);
    }
}
