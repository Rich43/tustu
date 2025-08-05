package com.sun.xml.internal.org.jvnet.mimepull;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

/* loaded from: rt.jar:com/sun/xml/internal/org/jvnet/mimepull/MIMEConfig.class */
public class MIMEConfig {
    private static final int DEFAULT_CHUNK_SIZE = 8192;
    private static final long DEFAULT_MEMORY_THRESHOLD = 1048576;
    private static final String DEFAULT_FILE_PREFIX = "MIME";
    private static final Logger LOGGER = Logger.getLogger(MIMEConfig.class.getName());
    boolean parseEagerly;
    int chunkSize;
    long memoryThreshold;
    File tempDir;
    String prefix;
    String suffix;

    private MIMEConfig(boolean parseEagerly, int chunkSize, long inMemoryThreshold, String dir, String prefix, String suffix) {
        this.parseEagerly = parseEagerly;
        this.chunkSize = chunkSize;
        this.memoryThreshold = inMemoryThreshold;
        this.prefix = prefix;
        this.suffix = suffix;
        setDir(dir);
    }

    public MIMEConfig() {
        this(false, 8192, 1048576L, null, DEFAULT_FILE_PREFIX, null);
    }

    boolean isParseEagerly() {
        return this.parseEagerly;
    }

    public void setParseEagerly(boolean parseEagerly) {
        this.parseEagerly = parseEagerly;
    }

    int getChunkSize() {
        return this.chunkSize;
    }

    void setChunkSize(int chunkSize) {
        this.chunkSize = chunkSize;
    }

    long getMemoryThreshold() {
        return this.memoryThreshold;
    }

    public void setMemoryThreshold(long memoryThreshold) {
        this.memoryThreshold = memoryThreshold;
    }

    boolean isOnlyMemory() {
        return this.memoryThreshold == -1;
    }

    File getTempDir() {
        return this.tempDir;
    }

    String getTempFilePrefix() {
        return this.prefix;
    }

    String getTempFileSuffix() {
        return this.suffix;
    }

    public final void setDir(String dir) {
        if (this.tempDir == null && dir != null && !dir.equals("")) {
            this.tempDir = new File(dir);
        }
    }

    public void validate() {
        File fileCreateTempFile;
        if (!isOnlyMemory()) {
            try {
                if (this.tempDir == null) {
                    fileCreateTempFile = File.createTempFile(this.prefix, this.suffix);
                } else {
                    fileCreateTempFile = File.createTempFile(this.prefix, this.suffix, this.tempDir);
                }
                File tempFile = fileCreateTempFile;
                boolean deleted = tempFile.delete();
                if (!deleted && LOGGER.isLoggable(Level.INFO)) {
                    LOGGER.log(Level.INFO, "File {0} was not deleted", tempFile.getAbsolutePath());
                }
            } catch (Exception e2) {
                this.memoryThreshold = -1L;
            }
        }
    }
}
