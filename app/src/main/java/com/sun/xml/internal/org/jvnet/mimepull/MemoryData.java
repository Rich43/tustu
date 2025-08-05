package com.sun.xml.internal.org.jvnet.mimepull;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.logging.Level;
import java.util.logging.Logger;

/* loaded from: rt.jar:com/sun/xml/internal/org/jvnet/mimepull/MemoryData.class */
final class MemoryData implements Data {
    private static final Logger LOGGER = Logger.getLogger(MemoryData.class.getName());
    private final byte[] data;
    private final int len;
    private final MIMEConfig config;

    MemoryData(ByteBuffer buf, MIMEConfig config) {
        this.data = buf.array();
        this.len = buf.limit();
        this.config = config;
    }

    @Override // com.sun.xml.internal.org.jvnet.mimepull.Data
    public int size() {
        return this.len;
    }

    @Override // com.sun.xml.internal.org.jvnet.mimepull.Data
    public byte[] read() {
        return this.data;
    }

    @Override // com.sun.xml.internal.org.jvnet.mimepull.Data
    public long writeTo(DataFile file) {
        return file.writeTo(this.data, 0, this.len);
    }

    @Override // com.sun.xml.internal.org.jvnet.mimepull.Data
    public Data createNext(DataHead dataHead, ByteBuffer buf) {
        if (!this.config.isOnlyMemory() && dataHead.inMemory >= this.config.memoryThreshold) {
            try {
                String prefix = this.config.getTempFilePrefix();
                String suffix = this.config.getTempFileSuffix();
                File tempFile = TempFiles.createTempFile(prefix, suffix, this.config.getTempDir());
                tempFile.deleteOnExit();
                if (LOGGER.isLoggable(Level.FINE)) {
                    LOGGER.log(Level.FINE, "Created temp file = {0}", tempFile);
                }
                tempFile.deleteOnExit();
                if (LOGGER.isLoggable(Level.FINE)) {
                    LOGGER.log(Level.FINE, "Created temp file = {0}", tempFile);
                }
                dataHead.dataFile = new DataFile(tempFile);
                if (dataHead.head != null) {
                    Chunk chunk = dataHead.head;
                    while (true) {
                        Chunk c2 = chunk;
                        if (c2 == null) {
                            break;
                        }
                        long pointer = c2.data.writeTo(dataHead.dataFile);
                        c2.data = new FileData(dataHead.dataFile, pointer, this.len);
                        chunk = c2.next;
                    }
                }
                return new FileData(dataHead.dataFile, buf);
            } catch (IOException ioe) {
                throw new MIMEParsingException(ioe);
            }
        }
        return new MemoryData(buf, this.config);
    }
}
